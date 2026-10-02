# ey-pdfgenrs

Erstatter [ey-pdfgen](../ey-pdfgen), siden [pdfgen](https://github.com/navikt/pdfgen) ikke vedlikeholdes etter desember 2026.
Bygger på [pdfgenrs](https://github.com/navikt/pdfgenrs), som bruker [Typst](https://typst.app/docs/)-maler i stedet for Handlebars/HTML.

Appene kjører side om side til alle maler er portert og verifisert. Malstiene er de samme som i ey-pdfgen,
så klientene trenger bare å bytte base-URL.

## Struktur

| Mappe        | Innhold                                                                                    |
|--------------|--------------------------------------------------------------------------------------------|
| `templates/` | Maler. Må ligge nøyaktig på formen `templates/<app>/<mal>.typ`, ingen undermapper.         |
| `lib/`       | Felles Typst-kode (sideoppsett, header, opplysningsrader). Importeres med `/lib/ey.typ`.   |
| `data/`      | Testdata for lokal kjøring, `data/<app>/<mal>.json`.                                       |
| `fonts/`     | Source Sans Pro, samme font som i ey-pdfgen.                                              |
| `resources/` | Bilder, f.eks. Nav-logo.                                                                   |

I malene leses flettedata med `json("/data/<app>/<mal>.json")`.

## Portert

Avkrysning betyr at malen er skrevet om til Typst. PDF-ene må fortsatt kompileres og sammenlignes
visuelt med ey-pdfgen før de kan regnes som verifisert.

- [x] `omsendringer/oms_meldt_inn_endring_v1`
- [x] `notat/tom_mal`
- [x] `notat/klage_oversendelse_blankett`
- [x] `eypdfgen/omstillingsstoenad_v1`
- [x] `eypdfgen/barnepensjon_v2`

## Kjøre lokalt

`./run_development.sh` bygger og starter appen på port 8082 med `DEV_MODE=true`. Malene lastes ved oppstart,
så scriptet må kjøres på nytt etter endringer.

PDF med testdata fra `data/`:

http://localhost:8082/api/v1/genpdf/omsendringer/oms_meldt_inn_endring_v1

http://localhost:8082/api/v1/genpdf/notat/tom_mal

http://localhost:8082/api/v1/genpdf/notat/klage_oversendelse_blankett

http://localhost:8082/api/v1/genpdf/eypdfgen/omstillingsstoenad_v1

http://localhost:8082/api/v1/genpdf/eypdfgen/barnepensjon_v2

Med egne data:

```sh
curl -X POST http://localhost:8082/api/v1/genpdf/omsendringer/oms_meldt_inn_endring_v1 \
  -H "Content-Type: application/json" \
  --data @data/omsendringer/oms_meldt_inn_endring_v1.json \
  --output oms.pdf
```

For å sammenligne med gammel versjon kan ey-pdfgen kjøres samtidig på port 8081.
