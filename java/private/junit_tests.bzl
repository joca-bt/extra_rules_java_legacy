_FORBIDDEN_ARGUMENTS = [
    "args",
    "main_class",
    "use_testrunner",
]

def junit_tests(name, **kwargs):
    for argument in _FORBIDDEN_ARGUMENTS:
        if argument in kwargs:
            fail("Forbidden argument: {}.".format(argument))

    native.java_test(
        name = name,
        main_class = "extrarulesjava.testrunner.TestRunner",
        use_testrunner = False,
        args = [
            "{}/{}.jar".format(native.package_name(), name),
        ],
        runtime_deps = kwargs.pop("runtime_deps", []) + [
            "@extra_rules_java//test-runner",
        ],
        **kwargs,
    )
