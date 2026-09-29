import os

def scan_repo(path="."):

    files = []

    for root, dirs, filenames in os.walk(path):
        for file in filenames:
            files.append(os.path.join(root, file))

    return files