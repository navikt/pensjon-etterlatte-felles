FROM node:20.5.1-bullseye-slim

WORKDIR /usr/src/app

COPY . ./

RUN yarn

ENV NODE_ENV="production"

EXPOSE 8080

CMD ["yarn", "start"]
