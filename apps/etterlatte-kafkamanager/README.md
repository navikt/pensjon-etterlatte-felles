# Etterlatte Kafka Manager

App som gjør det enklere å følge søknadsflyten når den er sendt til backend. 

Ingress: https://etterlatte-kafka-manager.intern.dev.nav.no 

Kan eksemepelvis søke etter nøkkelen SøknadID (ID returnert fra databasen i [innsendt-soeknad](../../apps/innsendt-soeknad))

NB: endringer i etterlatte-kafka-manager.yaml deployes ikke automatisk, man må manuelt kjøre `kubectl apply -f etterlatte-kafka-manager.yaml` i namespace etterlatte i riktig miljø.
