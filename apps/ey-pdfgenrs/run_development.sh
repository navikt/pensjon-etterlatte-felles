#!/bin/bash
# Bygger og kjører ey-pdfgenrs lokalt på port 8082 (ey-pdfgen bruker 8081, så begge kan kjøre samtidig).
# Malene lastes ved oppstart, så scriptet må kjøres på nytt etter endringer.

set -e
cd "$(dirname "$0")"

docker build -t ey-pdfgenrs-local .
docker run \
        -v "$(pwd)/data:/app/data" \
        -p 8082:8080 \
        -e DEV_MODE=true \
        -it \
        --rm \
        ey-pdfgenrs-local
