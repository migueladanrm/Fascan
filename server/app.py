from dotenv import load_dotenv
from flask import Flask, flash, request, redirect, url_for
import fascan
import os

app = Flask(__name__)


@app.route("/add", methods=["POST"])
def add_face():
    try:
        if request.method == "POST":
            if 0 < len(request.files):
                file = request.files["file"]
                added_face = fascan.add_face(file)

                if added_face != None:
                    return added_face, 201
                else:
                    return "error", 400
            else:
                return {"error": "No file was provided."}, 400
    except Exception as err:
        print(err)
        return {"error": "An unknown error was occurred."}, 400


@app.route("/detect", methods=["POST"])
def analyze_face():
    try:
        if request.method == "POST":
            if 0 < len(request.files):
                file = request.files["file"]
                result = fascan.detect_face(file, True)
                return result, 200
            else:
                return {"error": "No file was provided."}, 400
    except Exception as ex:
        return {"error": ex}, 400


if __name__ == "__main__":
    load_dotenv()
    app.run()
