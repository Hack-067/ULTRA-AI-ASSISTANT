from fastapi import FastAPI
from fastapi import HTTPException
from pydantic import BaseModel

try:
    from .ultra_agent import create_client, load_system_prompt, MODEL
except ImportError:
    from ultra_agent import create_client, load_system_prompt, MODEL

app = FastAPI()
client = None


class ChatRequest(BaseModel):
    message: str


@app.get("/health")
def health():
    return {"status": "ok", "configured": client is not None}


@app.post("/chat")
def chat(request: ChatRequest):
    global client

    if not request.message.strip():
        raise HTTPException(status_code=422, detail="message must not be empty")

    if client is None:
        try:
            client = create_client()
        except RuntimeError as error:
            raise HTTPException(status_code=503, detail=str(error)) from error

    try:
        response = client.chat.completions.create(
            model=MODEL,
            messages=[
                {"role": "system", "content": load_system_prompt()},
                {"role": "user", "content": request.message.strip()},
            ],
        )
    except Exception as error:
        raise HTTPException(status_code=502, detail=f"model request failed: {error}") from error

    return {
        "response": response.choices[0].message.content or "No response returned."
    }