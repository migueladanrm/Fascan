from face_recognition import face_distance, face_encodings, load_image_file
from database import get_face, get_face_encodings, insert_face
from storage import upload_object
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


def detect_face(image_file, save=True):
    input_encodings = __extract_face_encodings(image_file)

    raw_encodings = get_face_encodings()
    source_encodings = []

    for obj in raw_encodings:
        source_encodings.append(np.array(obj["encodings"]))

    result = face_distance(source_encodings, input_encodings)
    idx, dist = 0, result[0]

    for i in range(0, len(result) - 1):
        tmp_dist = result[i]

        if tmp_dist < dist:
            idx = i
            dist = tmp_dist

    target_face = get_face(raw_encodings[idx]["id"])

    return {"id": target_face["id"], "resource": target_face["resource"], "distance": dist}
