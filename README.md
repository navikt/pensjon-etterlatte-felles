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
