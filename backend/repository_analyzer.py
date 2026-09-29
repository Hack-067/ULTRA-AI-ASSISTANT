import os

def scan_project(path):

    files = []

    for root, dirs, filenames in os.walk(path):

        for file in filenames:
            files.append(file)

    return files