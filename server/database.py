from os import environ
import json
import numpy as np
import psycopg2


def get_connection():
    conn = None
    try:
        conn = psycopg2.connect(environ["DB_CONNECTION_STRING"])
    except (Exception, psycopg2.DatabaseError) as error:
        print(error)
    return conn


def insert_face(encodings, resource, custom_attrs={}) -> dict:
    face = None

    encodings = encodings.tolist()

    conn = get_connection()
    cur = conn.cursor()
    cur.execute("INSERT INTO face(encodings, resource, custom_attributes) VALUES(%s, %s, %s) RETURNING *;",
                (json.dumps(encodings), resource, json.dumps(custom_attrs)))
    rows = cur.fetchall()
    face = rows[0]

    conn.commit()

    cur.close()
    conn.close()

    return {"id": face[0], "resource": face[2], "custom_attributes": face[3],  "created_at": face[4]}


def get_face(id: str) -> dict:
    conn = get_connection()
    cur = conn.cursor()
    cur.execute(f"SELECT * FROM face WHERE id = '{id}'::uuid LIMIT 1;")

    rows = cur.fetchall()

    cur.close()
    conn.close()

    if 0 < len(rows):
        face = rows[0]
        return {"id": face[0], "resource": face[2], "custom_attributes": face[3],  "created_at": face[4]}
    else:
        return None


def get_face_encodings() -> []:
    data = []

    conn = get_connection()
    cur = conn.cursor()
    cur.execute("SELECT id, encodings FROM face;")

    rows = cur.fetchall()

    for row in rows:
        data.append({"id": row[0], "encodings": row[1]})

    cur.close()
    conn.close()

    return data
