visibility("private")

def extract_jars(jars, dir):
    return " && ".join(["unzip -q -o {} -d {}".format(jar, dir) for jar in jars])
