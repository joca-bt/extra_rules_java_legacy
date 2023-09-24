visibility("private")

def to_paths(depset):
    return [element.path for element in depset.to_list()]
