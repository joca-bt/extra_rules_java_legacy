# Additional Java rules for Bazel

Rules:

- [javadoc](docs/javadoc.md)

## Usage

```bazel
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
    name = "extra_rules_java",
    sha256 = <sha>,
    strip_prefix = "extra_rules_java-{}".format(<tag>),
    url = "https://github.com/joca-bt/extra_rules_java/archive/{}.tar.gz".format(<tag>),
)
```
