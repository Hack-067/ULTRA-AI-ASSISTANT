WAKE_WORD = "ultra"

def detect(text):

    return WAKE_WORD in text.lower()