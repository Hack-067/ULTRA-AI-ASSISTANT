import requests

def analyze(owner, repo):

    url = f"https://api.github.com/repos/{owner}/{repo}"

    data = requests.get(url).json()

    return {

        "name": data.get("name"),

        "stars": data.get("stargazers_count"),

        "forks": data.get("forks_count")
    }