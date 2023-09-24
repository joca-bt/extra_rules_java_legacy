# javadoc

```bazel
javadoc(name, javacopts, libs, links, packages, title)
```

Generates the Javadoc for a set of [libraries](https://bazel.build/reference/be/java#java_library).

**Arguments**

| Name      | Type            | Mandatory | Default                                                 |
| ---       | ---             | ---       | ---                                                     |
| name      | Name            | Yes       |                                                         |
| javacopts | List of strings | No        | []                                                      |
| libs      | List of labels  | Yes       |                                                         |
| links     | List of strings | No        | ["https://docs.oracle.com/en/java/javase/17/docs/api/"] |
| packages  | List of strings | Yes       |                                                         |
| title     | String          | No        | ""                                                      |
