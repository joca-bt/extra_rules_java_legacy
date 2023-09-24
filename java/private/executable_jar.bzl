load(":depsets.bzl", "set_difference", "to_paths")
load(":files.bzl", "copy_files", "write_file")
load(":jars.bzl", "extract_jars")

def _manifest(ctx):
    return """\
Main-Class: extrarulesjava.jarloader.JarLoader
Start-Class: {}\
""".format(ctx.attr.main_class)

def _executable_jar_impl(ctx):
    app = depset(transitive = [depset(lib[JavaInfo].runtime_output_jars) for lib in ctx.attr.libs])
    libs = depset(transitive = [lib[JavaInfo].transitive_runtime_jars for lib in ctx.attr.libs])
    libs = set_difference(libs, app)
    output = ctx.actions.declare_file("{}.jar".format(ctx.label.name))

    java_home = ctx.attr._jdk[java_common.JavaRuntimeInfo].java_home
    tmp_dir = "{}/_{}/".format(output.dirname, output.basename)
    app_dir = "{}/app/".format(tmp_dir)
    lib_dir = "{}/lib/".format(tmp_dir)
    manifest_file = "{}/manifest".format(tmp_dir)

    commands = [
        "rm -rf {}".format(tmp_dir),
        "mkdir {} {}".format(tmp_dir, lib_dir),
        extract_jars(to_paths(app), app_dir),
        copy_files(to_paths(libs), lib_dir),
        extract_jars(to_paths(ctx.attr._jar_loader.files), tmp_dir),
        write_file(manifest_file, _manifest(ctx)),
        "{0}/bin/jar -c -f {1} -m {2} -C {3} app/ -C {3} extrarulesjava/".format(java_home, output.path, manifest_file, tmp_dir),
        "{}/bin/jar -u -0 -f {} -C {} lib/".format(java_home, output.path, tmp_dir),
    ]

    ctx.actions.run_shell(
        command = " && ".join(commands),
        inputs = ctx.files._jdk + ctx.files._jar_loader + app.to_list() + libs.to_list(),
        outputs = [output],
    )

    return [DefaultInfo(files = depset([output]))]

executable_jar = rule(
    doc = "Generates an executable jar for a set of libraries.",
    implementation = _executable_jar_impl,
    attrs = {
        "libs": attr.label_list(
            allow_empty = False,
            mandatory = True,
            providers = [JavaInfo],
        ),
        "main_class": attr.string(
            mandatory = True,
        ),
        "_jdk": attr.label(
            default = "@bazel_tools//tools/jdk:current_java_runtime",
            providers = [java_common.JavaRuntimeInfo],
        ),
        "_jar_loader": attr.label(
            default = "@extra_rules_java//jar-loader",
            providers = [JavaInfo],
        ),
    },
)
