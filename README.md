# Additional Java rules for Bazel

Rules:

- [executable_jar](#executable_jar)
- [javadoc](#javadoc)

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

## Rules

### executable_jar

```bazel
executable_jar(name, libs, main_class)
```

Generates an executable jar for a set of [libraries](https://bazel.build/reference/be/java#java_library).

**Arguments**

| Name       | Type           | Mandatory |
| ---        | ---            | ---       |
| name       | Name           | Yes       |
| libs       | List of labels | Yes       |
| main_class | String         | Yes       |

### javadoc

```bazel
javadoc(name, exclude, javacopts, libs, links, packages, title)
```

Generates the Javadoc for a set of [libraries](https://bazel.build/reference/be/java#java_library).

**Arguments**

| Name      | Type            | Mandatory | Default                                                 |
| ---       | ---             | ---       | ---                                                     |
| name      | Name            | Yes       |                                                         |
| exclude   | List of strings | No        | []                                                      |
| javacopts | List of strings | No        | []                                                      |
| libs      | List of labels  | Yes       |                                                         |
| links     | List of strings | No        | ["https://docs.oracle.com/en/java/javase/17/docs/api/"] |
| packages  | List of strings | Yes       |                                                         |
| title     | String          | No        | ""                                                      |
