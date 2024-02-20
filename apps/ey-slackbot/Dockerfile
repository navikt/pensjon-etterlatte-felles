FROM node:20-bookworm-slim as base

WORKDIR /usr/src/app

COPY . ./

RUN yarn

ENV NODE_ENV="production"

FROM gcr.io/distroless/nodejs20-debian12 as prod
COPY --from=base /usr/src/app /app

USER nonroot
ENV NODE_ENV="production"

WORKDIR /app
EXPOSE 8080

CMD ["start"]
