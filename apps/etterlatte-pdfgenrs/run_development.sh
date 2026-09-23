#!/bin/bash

CURRENT_PATH="$(cd "$(dirname "$1")"; pwd)/$(basename "$1")"

docker pull --platform "${PDFGENRS_PLATFORM:-linux/amd64}" ghcr.io/navikt/pdfgenrs:1.0.39
docker run \
        --platform "${PDFGENRS_PLATFORM:-linux/amd64}" \
        -v $CURRENT_PATH/templates:/app/templates \
        -v $CURRENT_PATH/fonts:/app/fonts \
        -v $CURRENT_PATH/data:/app/data \
        -v $CURRENT_PATH/resources:/app/resources \
        -p 8081:8080 \
        -e DEV_MODE=true \
        -it \
        --rm \
        ghcr.io/navikt/pdfgenrs:1.0.39
