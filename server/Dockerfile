FROM python:3.8-slim

WORKDIR /app

# copy the dependencies file to the working directory
COPY requirements.txt .

# install dependencies
RUN apt-get update && apt-get install build-essential cmake -y
#RUN pip install --upgrade pip  && apt-get install -y --fix-missing && pip install --no-cache-dir -r requirements.txt
RUN pip install -r requirements.txt

# copy the content of the local src directory to the working directory
COPY . .

EXPOSE 7000

# command to run on container start
CMD [ "python", "./app.py" ]
