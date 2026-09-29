from openai import OpenAI
import os

TOKEN = os.getenv("HF_TOKEN")
if not TOKEN:
    raise ValueError("HF_TOKEN environment variable not set")

client = OpenAI(
    base_url="https://router.huggingface.co/v1",
    api_key=TOKEN
)

while True:
    user = input("You: ")

    response = client.chat.completions.create(
        model="Qwen/Qwen2.5-Coder-32B-Instruct",
        messages=[
            {
                "role":"system",
                "content":"You are ULTRA, an AI coding assistant that helps write code, debug errors and explain solutions."
            },
            {
                "role":"user",
                "content":user
            }
        ]
    )

    print("ULTRA:", response.choices[0].message.content)