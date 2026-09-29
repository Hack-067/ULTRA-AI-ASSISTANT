import os
from pathlib import Path

from openai import OpenAI


MODEL = "Qwen/Qwen2.5-Coder-32B-Instruct"
SYSTEM_PROMPT_PATH = Path(__file__).with_name("system_prompt.tex")


def create_client():
    token = os.getenv("HF_TOKEN")
    if not token:
        raise RuntimeError("HF_TOKEN environment variable is not set")

    return OpenAI(
        base_url="https://router.huggingface.co/v1",
        api_key=token,
    )


def load_system_prompt():
    return SYSTEM_PROMPT_PATH.read_text(encoding="utf-8")


def main():
    try:
        client = create_client()
    except RuntimeError as error:
        print(f"ULTRA: {error}")
        return 1

    messages = [{"role": "system", "content": load_system_prompt()}]

    while True:
        try:
            user = input("You: ").strip()
        except (EOFError, KeyboardInterrupt):
            print()
            return 0

        if not user:
            continue
        if user.lower() in {"exit", "quit"}:
            return 0

        messages.append({"role": "user", "content": user})
        try:
            response = client.chat.completions.create(
                model=MODEL,
                messages=messages,
            )
        except Exception as error:
            messages.pop()
            print(f"ULTRA: request failed: {error}")
            continue

        answer = response.choices[0].message.content or "No response returned."
        messages.append({"role": "assistant", "content": answer})
        print("ULTRA:", answer)


if __name__ == "__main__":
    raise SystemExit(main())