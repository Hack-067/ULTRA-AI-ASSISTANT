import os

def explain_repo(repo_path="."):

    files = []

    for root, dirs, filenames in os.walk(repo_path):
        for file in filenames:
            files.append(file)

    report = f"""
ULTRA Repository Report

Total Files:
{len(files)}

Files:
"""

    for f in files:
        report += f"\n- {f}"

    return report