# ULTRA AI ASSISTANT

Current Features

- AI Chat
- Voice Support
- Repository Analysis
- Error Debugging
- Accessibility Service
- Text To Speech
- Windows desktop app
- Android APK client

Roadmap

- Floating Bubble
- APK Build
- Wake Word
- Screen Reading

## Run the agent

Install the backend dependencies, set a Hugging Face token, and start the API:

```bash
pip install -r backend/requirements.txt
export HF_TOKEN=your_token
uvicorn backend.api:app --host 0.0.0.0 --port 8000
```

The Android app connects to this API. Use `http://10.0.2.2:8000` in the
Android emulator, or the computer's LAN IP on a physical device.

## Build downloads

Push a tag such as `v1.0.0`, or run the **Build downloads** workflow manually.
GitHub Actions publishes a Windows `.exe` and Android `.apk` as workflow
artifacts; tagged builds also create a GitHub Release with both files.