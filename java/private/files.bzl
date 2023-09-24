visibility("private")

def copy_files(files, dir):
    return "cp {} {}".format(" ".join(files), dir)

def write_file(file, content):
    return "echo '{}' > {}".format(content, file)
