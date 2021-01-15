from face_recognition import face_encodings, load_image_file
from storage import upload_object
from database import insert_face

import numpy as np


def __extract_face_encodings(image_file):
    return face_encodings(load_image_file(image_file))[0]


def add_face(image_file, custom_attrs={}):
    obj_public_url = upload_object(image_file)

    if obj_public_url != None:
        encodings = __extract_face_encodings(image_file)

        face = insert_face(encodings, obj_public_url, custom_attrs)

        return face
    else:
        return None
