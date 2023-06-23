FROM node:20.3.0-bullseye-slim

WORKDIR /usr/src/app

COPY . ./

RUN yarn

ENV NODE_ENV="production"

EXPOSE 8080

CMD ["yarn", "start"]
