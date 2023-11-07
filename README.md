# Additional Java rules for Bazel

Rules:

- [executable_jar](docs/executable_jar.md)
- [javadoc](docs/javadoc.md)
- [junit_tests](docs/junit_tests.md) (macro)

## Usage

```bazel
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
    name = "extra_rules_java",
    sha256 = <sha>,
    strip_prefix = "extra_rules_java-{}".format(<tag>),
    url = "https://github.com/joca-bt/extra_rules_java/releases/download/{0}/extra_rules_java-{0}.tar.gz".format(<tag>),
)
```
