def detect_command(text):

    text = text.lower()

    if "write code" in text:
        return "generate_code"

    if "fix error" in text:
        return "debug"

    if "explain code" in text:
        return "explain"

    return "chat"