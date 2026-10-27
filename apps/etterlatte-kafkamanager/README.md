# Etterlatte Kafka Manager

App som gjør det enklere å følge søknadsflyten når den er sendt til backend. 

Ingress: https://etterlatte-kafka-manager.intern.dev.nav.no 

Kan eksemepelvis søke etter nøkkelen SøknadID (ID returnert fra databasen i [innsendt-soeknad](../../apps/innsendt-soeknad))

## Deploy

Appen skal bare finnes i dev og deployes kun til `dev-gcp`.

Workflowen [etterlatte-kafkamanager](../../.github/workflows/app-etterlatte-kafkamanager.yaml)
bygger og publiserer et image med samme fellesworkflow som de andre containerappene.
Image-referansen fra bygget settes automatisk inn i [.nais/dev.yaml](.nais/dev.yaml).
Du trenger ikke kjøre `kubectl apply` eller kopiere image-referanser.

Endringer i appmappa eller workflowen deployes automatisk når de merges til `main`.
For å deploye på nytt velger du **Actions → etterlatte-kafkamanager → Run workflow** i GitHub.
Workflowen har ingen prod-jobb og bruker ikke den delte deploy-workflowen som også kan deploye til prod.

Versjonen av POAOs Kafka Manager er angitt i [Dockerfile](Dockerfile).
Oppdater den der når du vil ta i bruk en ny versjon.
