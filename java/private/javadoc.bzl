load(":depsets.bzl", "to_paths")
load(":jars.bzl", "extract_jars")

def _javadoc(ctx, deps, src_dir, javadoc_dir, java_home):
    command = [
        "{}/bin/javadoc".format(java_home),
        "-quiet",
        "-Xdoclint:-missing",
        "--class-path {}".format(":".join(deps)),
        "--source-path {}".format(src_dir),
        "-subpackages {}".format(":".join(ctx.attr.packages)),
        "-d {}".format(javadoc_dir),
        "-docencoding UTF-8",
        "-notimestamp",
    ]

    if ctx.attr.exclude:
        command.append("-exclude {}".format(":".join(ctx.attr.exclude)))

    command.extend(ctx.attr.javacopts)

    for link in ctx.attr.links:
        command.append("-link {}".format(link))

    if ctx.attr.title:
        command.append("-doctitle '{}'".format(ctx.attr.title))
        command.append("-windowtitle '{}'".format(ctx.attr.title))

    return " ".join(command)

def _javadoc_impl(ctx):
    srcs = depset(transitive = [depset(lib[JavaInfo].source_jars) for lib in ctx.attr.libs])
    deps = depset(transitive = [lib[JavaInfo].transitive_compile_time_jars for lib in ctx.attr.libs])
    output = ctx.actions.declare_file("{}.jar".format(ctx.label.name))

    java_home = ctx.attr._jdk[java_common.JavaRuntimeInfo].java_home
    tmp_dir = "{}/_{}/".format(output.dirname, output.basename)
    src_dir = "{}/src/".format(tmp_dir)
    javadoc_dir = "{}/javadoc/".format(tmp_dir)

    commands = [
        "rm -rf {}".format(tmp_dir),
        "mkdir {}".format(tmp_dir),
        extract_jars(to_paths(srcs), src_dir),
        _javadoc(ctx, to_paths(deps), src_dir, javadoc_dir, java_home),
        "{}/bin/jar -c -f {} -C {} .".format(java_home, output.path, javadoc_dir),
    ]

    ctx.actions.run_shell(
        command = " && ".join(commands),
        inputs = ctx.files._jdk + srcs.to_list() + deps.to_list(),
        outputs = [output],
    )

    return [DefaultInfo(files = depset([output]))]

javadoc = rule(
    doc = "Generates the Javadoc for a set of libraries.",
    implementation = _javadoc_impl,
    attrs = {
        "exclude": attr.string_list(),
        "javacopts": attr.string_list(),
        "libs": attr.label_list(
            allow_empty = False,
            mandatory = True,
            providers = [JavaInfo],
        ),
        "links": attr.string_list(
            default = ["https://docs.oracle.com/en/java/javase/17/docs/api/"],
        ),
        "packages": attr.string_list(
            allow_empty = False,
            mandatory = True,
        ),
        "title": attr.string(),
        "_jdk": attr.label(
            default = Label("@bazel_tools//tools/jdk:current_java_runtime"),
            providers = [java_common.JavaRuntimeInfo],
        ),
    },
)
