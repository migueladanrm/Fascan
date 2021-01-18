from google.cloud import storage
from uuid import uuid4
from os import environ


def upload_object(file, name=None) -> str:
    client = storage.Client()
    bucket = client.bucket(environ["GC_BUCKET"])

    if name is None:
        name = f"{uuid4()}.jpg"

    blob = bucket.blob(name)
    blob.upload_from_file(file)

    print(f"Object uploaded: {blob.name}")

    return blob.public_url
