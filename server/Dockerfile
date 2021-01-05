FROM node:current-slim

WORKDIR /app

COPY package*.json ./

RUN npm i

COPY . .

RUN npm run build

EXPOSE ${SERVER_PORT}

ENTRYPOINT ["node", "dist/index.js"]