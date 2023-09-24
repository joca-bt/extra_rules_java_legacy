# executable_jar

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
