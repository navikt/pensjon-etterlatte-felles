# ey-pdfgen
PDF generator for etterlatte og barnepensjon. Kjør lokalt docker image med  `./run_development.sh`

For å trigge PDF-gen må du gå til følgende url (`HTTP_GET`):

#### Søknad

http://localhost:8081/api/v1/genpdf/eypdfgen/omstillingsstoenad_v1

http://localhost:8081/api/v1/genpdf/eypdfgen/barnepensjon_v2

Templatene vil bruke flettedata fra json-fil med samme navn som template i `data/eypdfgen`

#### Inntektsjustering

http://localhost:8081/api/v1/genpdf/inntektsjustering/inntektsjustering_nytt_aar_v1