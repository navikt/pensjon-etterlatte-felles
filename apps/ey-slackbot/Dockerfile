FROM node:21.5.0-bullseye-slim

WORKDIR /usr/src/app

COPY . ./

RUN yarn

ENV NODE_ENV="production"

EXPOSE 8080

CMD ["yarn", "start"]
