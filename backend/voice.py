import speech_recognition as sr

def listen():

    r = sr.Recognizer()

    with sr.Microphone() as source:
        print("Listening...")
        audio = r.listen(source)

    return r.recognize_google(audio)