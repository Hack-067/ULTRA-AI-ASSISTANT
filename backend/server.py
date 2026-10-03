from flask import Flask
from flask import request
from flask import jsonify

app = Flask(__name__)

@app.route("/chat", methods=["POST"])

def chat():

    user = request.json["message"]

    return jsonify(
        {
            "reply": f"ULTRA: {user}"
        }
    )

if __name__ == "__main__":

    app.run(
        host="0.0.0.0",
        port=5000
    )