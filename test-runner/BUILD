java_import(
    name = "junit5",
    jars = [
        "jars/junit-platform-console-standalone-1.10.0.jar",
    ],
    visibility = ["//visibility:public"],
)

java_library(
    name = "test-runner",
    srcs = glob(["src/main/java/**/*.java"]),
    deps = [
        ":junit5",
    ],
    visibility = ["//visibility:public"],
)
