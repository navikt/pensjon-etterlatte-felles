# etterlatte-pdfgenrs
PDF generator for etterlatte og barnepensjon, based on [pdfgenrs](https://github.com/navikt/pdfgenrs).
Kjør lokalt docker image med `./run_development.sh`.

For å trigge PDF-gen må du gå til følgende url (`HTTP_GET`):

#### Søknad

http://localhost:8081/api/v1/genpdf/eypdfgen/omstillingsstoenad_v1

http://localhost:8081/api/v1/genpdf/eypdfgen/barnepensjon_v2

Templatene bruker testdata fra JSON-filer med samme navn som template i `data/eypdfgen`.
I produksjon sendes JSON-data i POST-bodyen.

#### Inntektsjustering

http://localhost:8081/api/v1/genpdf/omsendringer/oms_meldt_inn_endring_v1