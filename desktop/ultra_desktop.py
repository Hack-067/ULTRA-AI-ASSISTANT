import os
import threading
import tkinter as tk
from tkinter import messagebox, scrolledtext

from openai import OpenAI

MODEL = "Qwen/Qwen2.5-Coder-32B-Instruct"
SYSTEM_PROMPT = (
    "You are ULTRA, a helpful coding and productivity assistant. "
    "Give clear, practical answers and ask for missing details when needed."
)


class UltraDesktop(tk.Tk):
    def __init__(self):
        super().__init__()
        self.title("ULTRA AI Assistant")
        self.geometry("760x620")
        self.minsize(520, 420)
        self.client = None
        self.messages = [{"role": "system", "content": SYSTEM_PROMPT}]
        self._build_ui()

    def _build_ui(self):
        self.output = scrolledtext.ScrolledText(self, state="disabled", wrap=tk.WORD)
        self.output.pack(fill=tk.BOTH, expand=True, padx=12, pady=(12, 6))
        self.output.tag_configure("user", foreground="#155eef")
        self.output.tag_configure("ultra", foreground="#087443")

        row = tk.Frame(self)
        row.pack(fill=tk.X, padx=12, pady=(0, 12))
        self.input = tk.Entry(row)
        self.input.pack(side=tk.LEFT, fill=tk.X, expand=True)
        self.input.bind("<Return>", lambda _event: self.send())
        self.send_button = tk.Button(row, text="Send", command=self.send)
        self.send_button.pack(side=tk.RIGHT, padx=(8, 0))
        self.input.focus_set()

    def _write(self, text, tag):
        self.output.configure(state="normal")
        self.output.insert(tk.END, text + "\n\n", tag)
        self.output.configure(state="disabled")
        self.output.see(tk.END)

    def _get_client(self):
        if self.client is not None:
            return self.client
        token = os.getenv("HF_TOKEN")
        if not token:
            raise RuntimeError("Set the HF_TOKEN environment variable before starting ULTRA.")
        self.client = OpenAI(
            base_url="https://router.huggingface.co/v1",
            api_key=token,
        )
        return self.client

    def send(self):
        message = self.input.get().strip()
        if not message:
            return
        self.input.delete(0, tk.END)
        self._write(f"You: {message}", "user")
        self.send_button.configure(state=tk.DISABLED)
        threading.Thread(target=self._request, args=(message,), daemon=True).start()

    def _request(self, message):
        try:
            self.messages.append({"role": "user", "content": message})
            result = self._get_client().chat.completions.create(
                model=MODEL,
                messages=self.messages,
            )
            answer = result.choices[0].message.content or "No response returned."
            self.messages.append({"role": "assistant", "content": answer})
            self.after(0, lambda: self._write(f"ULTRA: {answer}", "ultra"))
        except Exception as error:
            if self.messages and self.messages[-1]["role"] == "user":
                self.messages.pop()
            error_message = str(error)
            self.after(0, lambda: messagebox.showerror("ULTRA error", error_message))
        finally:
            self.after(0, lambda: self.send_button.configure(state=tk.NORMAL))


if __name__ == "__main__":
    UltraDesktop().mainloop()
