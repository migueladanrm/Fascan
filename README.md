# Fascan: Face Scanner

## ¿Cómo desplegar?
Para ejecutar el servidor de Fascan es necesario utilizar Docker con el siguiente archivo *.env*:

```
PROJECT_NAME = fascan
SERVER_PORT = 8000
```
Entonces, debe ejecutar el siguiente comando:
```
docker-compose --project-name fascan up --build -d
```