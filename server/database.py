import psycopg2
from os import environ


def get_connection():
    conn = None
    try:
        conn = psycopg2.connect(environ["DB_CONNECTION_STRING"])
    except (Exception, psycopg2.DatabaseError) as error:
        print(error)
    return conn


def insert_face(encodings, resource, custom_attrs={}):
    face = None

    conn = get_connection()
    cur = conn.cursor()
    cur.execute("INSERT INTO face(encodings, resource, custom_attributes) VALUES(%s, %s, %s) RETURNING *;",
                (encodings, resource, custom_attrs))
    rows = cur.fetchall()
    face = rows[0]

    cur.close()
    conn.close()

    return face
