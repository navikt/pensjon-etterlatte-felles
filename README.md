# pensjon-etterlatte

Monorepo med apper som er felles for Team Etterlatte


## Apper

[etterlatte-kafkamanager](apps/etterlatte-kafkamanager) \
Kafka Manager for å enkelt se flyten til en søknad.

[etterlatte-node-server](apps/etterlatte-node-server) \
Felles node backend for Team Etterlatte sine React-apper.

[etterlatte-notifikasjoner](apps/etterlatte-notifikasjoner) \
App som sender notifikasjoner (e-post, sms, melding på nav.no) til sluttbrukeren.

[etterlatte-pdl-proxy](apps/etterlatte-pdl-proxy) \
Ny proxy for å tillate kommunikasjon mellom GCP og On-Prem.

[etterlatte-proxy](apps/etterlatte-proxy) \
Proxy for å tillate kommunikasjon mellom GCP og On-Prem.

[ey-pdfgen](apps/ey-pdfgen) \
Enkel app for opprettelse av PDF til journalføring. Benytter seg av [pdfgen](https://github.com/navikt/pdfgen)


# Bygg og deploy

En app bygges og deployes automatisk når en endring legges til i `main`. 

For å trigge **manuell deploy** kan du gå til `Actions -> (velg workflow) -> Run workflow from <branch>`


# Henvendelser

Spørsmål knyttet til koden eller prosjektet kan stilles som issues her på GitHub.


## For NAV-ansatte

Interne henvendelser kan sendes via Slack i kanalen #po-pensjon-team-etterlatte.
