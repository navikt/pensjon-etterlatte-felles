FROM node:16

WORKDIR /usr/src/app

COPY . ./

RUN yarn

ENV NODE_ENV="production"

EXPOSE 8080

CMD ["yarn", "start"]