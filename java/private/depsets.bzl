visibility("private")

def set_difference(a, b):
    b = {element: True for element in b.to_list()}
    return depset([element for element in a.to_list() if element not in b])

def to_paths(depset):
    return [element.path for element in depset.to_list()]
