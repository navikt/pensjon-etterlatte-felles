FROM node:24-bookworm-slim as base

WORKDIR /usr/src/app

COPY src ./src
COPY package.json ./package.json
COPY yarn.lock ./yarn.lock

ENV NODE_ENV="production"
RUN yarn


FROM gcr.io/distroless/nodejs20-debian12 as prod
COPY --from=base /usr/src/app /app

USER nonroot
ENV NODE_ENV="production"

WORKDIR /app
EXPOSE 8080

CMD ["./src/app.js"]
