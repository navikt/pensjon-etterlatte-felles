# pensjon-etterlatte-felles

Monorepo med apper som er felles for Team Etterlatte

## Gi en ny app tilgang til Kafka-topic "etterlatte.dodsmelding"
Legg inn appen i `topic.yaml` for dev, `topic-prod.yaml` for prod. Etter at det er lagt inn
kan du oppdatere topicet ved å kjøre 

```
kubectl apply -f topic.yaml
```

i konteksten `dev-gcp`. For prod må du bruke `topic-prod.yaml` i stedet, og kontekst
`prod-gcp`.


## Apper

[etterlatte-kafkamanager](apps/etterlatte-kafkamanager) \
Kafka Manager for å enkelt se flyten til en søknad.

[etterlatte-notifikasjoner](apps/etterlatte-notifikasjoner) \
App som sender notifikasjoner (e-post, sms, melding på nav.no) til sluttbrukeren.

[etterlatte-proxy](apps/etterlatte-proxy) \
Proxy for å tillate kommunikasjon mellom GCP og On-Prem.

[ey-pdfgen](apps/ey-pdfgen) \
Enkel app for opprettelse av PDF til journalføring. Benytter seg av [pdfgen](https://github.com/navikt/pdfgen)


# Kom i gang

Noen avhengigheter i prosjektet ligger i Github Package Registry som krever autentisering. Det enkleste er å lage en [PAT (Personal Access Token)](https://github.com/settings/tokens).

1. Opprett [PAT](https://github.com/settings/tokens). I tilfelle lenken ikke fungerer går man til `Github -> Settings -> Developer settings -> Personal access tokens`
2. Huk av `read:packages`. Ikke legg til flere scopes enn nødvendig.
3. Tokenet legges i `.zshrc` med `export GITHUB_TOKEN=<token>`


# Bygg og deploy

En app bygges og deployes automatisk når en endring legges til i `main`. 

For å trigge **manuell deploy** kan du gå til `Actions -> (velg workflow) -> Run workflow from <branch>`


# Henvendelser

Spørsmål knyttet til koden eller prosjektet kan stilles som issues her på GitHub.


## For NAV-ansatte

Interne henvendelser kan sendes via Slack i kanalen [#po-pensjon-team-etterlatte](https://nav-it.slack.com/archives/C01KJ597UAU).

# Utviklerhåndbok
## Pakkestruktur
Alle filer under samme source-root skal ha en pakkestruktur som er konsekvent relativt til hverandre
## Advarsler og tips fra intelliJ
* Gjør som IntelliJ sier
## Commit-meldinger
* Bruk branch og squash den.
* Skriv commit-meldinger som gir mening for andre.
## Lokalt utviklingsmiljø
* Oppsett av app – burde fungere lokalt (se brev-api for eksempel)
* Kan vi få lokal frontend til å fungere mot dev-gcp?
## Logging / Monitorering
* En feilsituasjon skal bare logges en gang
* Når det skal logges at en exception har inntruffet, så skal man bruke leggemetoden som tar inn en Throwable som et 
argument, slik at loggrammeverkt kan håndtere hvordan den logges.
* Når det legges inn kasting og logging av feil så skal det legges med informasjon om hvilken konsekvens feilen har der
feilen kastes/logges 
* Kommenter på kjente feil du vet om i alerts i slack.
## Feilhåndtering
* Ikke fang og ignorer feil med mindre verdikjeden tar høyde for det.
## Felles kode (common / utils)
* Bruk av common er OK. Vurder å dele opp common etter domener og/eller integrasjoner.
* Bruk pensjon-etterlatte-libs for ting som er felles på tvers av apper.
## Lagring av dato i DB
Vi har en egen klasse for håndtering av tidspunkt. Den heter Tidspunkt
## Særnorske tegn
I koden holder vi oss til ASCII. Særnorske tegn erstattes derfor av kombinasjoner av andre tegn.

| Tegn | Kode | Eksempel norsk | Eksempel kode-norsk |
|------|------|----------------|---------------------|
| æ    | ae   | Særnorsk       | Saernorsk           |
| ø    | oe   | Øve            | Oeve                |
| å    | aa   | Låne           | Laane               |