# pensjon-etterlatte-felles

Monorepo med apper som er felles for Team Etterlatte

## Gi en ny app tilgang til Kafka-topic "etterlatte.dodsmelding"
Legg inn appen i `.deploy/topic.yaml` for dev, `.deploy/topic-prod.yaml` for prod. Etter at det er lagt inn
kan du oppdatere topicet ved å kjøre 

## For å oppdatere endringer i topic yamler kjør dette:
### Obs: Må stå samme path som filen(e)
https://docs.nais.io/persistence/kafka/how-to/create/?h=kafka+topic#apply-the-topic-resource
```
kubectl apply -f .deploy/topic.yaml
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

[ey-slackbot](apps/ey-slackbot) \
Konfigurasjon av slackbot for teamet.

# Libs

- [common](common)


common er kode som er felles for Gjenny og søknadsdialogene. Dette er flytta over fra [pensjon-etterlatte-felles](https://github.com/navikt/pensjon-etterlatte-felles)-repoet.


# Tjenestespesifikasjoner

Dette repoet  inneholder tjenestespesifikasjoner for de tjenestene som NAV tilbyr internt, og som vi bruker i Team Etterlatte.
Dette er flytta over fra [pensjon-etterlatte-tjenestespesifikasjoner](https://github.com/navikt/pensjon-etterlatte-tjenestespesifikasjoner)-repoet.

De er maskinlesbare i form av WSDL/XSD/JSON-filer, og disse brukes som utgangspunkt for å
generere Javakode. Denne autogenererte koden blir kompilert og siden publisert, slik at konsumenter
kan bruke dem til å kommunisere med tjenestene.

### Gradle
```
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.navikt:pensjon-etterlatte-felles:navn-på-modul:Tag'
}
```

### Maven
```	
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
	
	<dependency>
	    <groupId>com.github.navikt.pensjon-etterlatte-felles</groupId>
	    <artifactId>navn-på-modul</artifactId>
	    <version>Tag</version>
	</dependency>
```


## Bygging
Koden bruker Jakarta-navnerommet, og forutsetter Java nyere enn 8.

`mvn install`

## Gjøre endringer, release

For å endre spesifikasjoner, lag en branch. Kjør bygget lokalt, da vil du
få siste endringer med `1-SNAPSHOT` som versjon. Test med en konsument at
endringene fungerer (sett versjon av tjenestespesifikasjoner til `0-SNAPSHOT` i konsumenten.)
Når testingen er ferdig, send en pull request til dette repoet.

Hver branch og pull request vil gå gjennom et CI-bygg.
Etter at en pull request er merget til main-branchen, vil
CI automatisk gjøre en release av hele repoet til Maven Central.
Alle modulene i dette repoet får samme versjonsnummer.
Versjonsnummeret til releasen blir `1.YYYY.MM.DD-HH-MM-commithash`.
---

# Kom i gang

Common-biblioteket i prosjektet blir released til Github Package Registry som krever autentisering. For å ta dem i bruk som dependencies i et annet prosjekt er det enkleste er å lage et [PAT (Personal Access Token)](https://github.com/settings/tokens).

1. Opprett [PAT](https://github.com/settings/tokens). I tilfelle lenken ikke fungerer går man til `Github -> Settings -> Developer settings -> Personal access tokens`
2. Huk av `read:packages`. Ikke legg til flere scopes enn nødvendig.
3. Tokenet legges i `.zshrc` med `export GITHUB_TOKEN=<token>`
4. Du må kanskje markere at tokenet skal være gyldig for NAV-organisasjonen for at det skal fungere, og i så fall må du autentisere det med SSO.


# Bygg og deploy

En app bygges og deployes automatisk når en endring legges til i `main`. 

For å trigge **manuell deploy** kan du gå til `Actions -> (velg workflow) -> Run workflow from <branch>`


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
* Når det skal logges at en exception har inntruffet, så skal man bruke loggemetoden som tar inn en Throwable som et 
argument, slik at loggrammeverkt kan håndtere hvordan den logges.
* Når det legges inn kasting og logging av feil så skal det legges med informasjon om hvilken konsekvens feilen har der
feilen kastes/logges 
* Kommenter på kjente feil du vet om i alerts i slack.

## Feilhåndtering
* Ikke fang og ignorer feil med mindre verdikjeden tar høyde for det.

## Felles kode (common / utils)
* Bruk av common er OK. Vurder å dele opp common etter domener og/eller integrasjoner.
* Bruk common-biblioteket for ting som er felles på tvers av apper.

## Lagring av dato i DB
Vi har en egen klasse for håndtering av tidspunkt. Den heter Tidspunkt

## Særnorske tegn
I koden holder vi oss til ASCII. Særnorske tegn erstattes derfor av kombinasjoner av andre tegn.

| Tegn | Kode | Eksempel norsk | Eksempel kode-norsk |
|------|------|----------------|---------------------|
| æ    | ae   | Særnorsk       | Saernorsk           |
| ø    | oe   | Øve            | Oeve                |
| å    | aa   | Låne           | Laane               |

## APIer
I apper hvor vi tilbyr APIer skal disse ha følgende url-struktur:
```
/api/{ressurs}
/api/{ressurs}/{ressursId}

Eks /api/behandling/{behandlingId}
```

Som hovedregel bruker vi vanlig http-verb for operasjoner:
```
Hent -> GET
Opprett -> POST
Oppdater -> PUT
Slett -> DELETE
Oppdater spesifikke felter -> PATCH

Eks 
POST /api/behandling -> Oppretter en behandling
GET /api/behandling/{behandlingId} -> Henter en behandling
```

Enkelte operasjoner trenger en mer spesifikk sti for å beskrive hva som gjøres. Eksempl på dette er:

```
POST /api/vedtak/{behandlingId}/attester
```

Retur-verdier skal følge vanlige konvensjoner:
```
Ok -> 200
Fant ikke ressurs -> 404 eller 204?
Noe feilet -> 500
```

# Henvendelser

Spørsmål knyttet til koden eller prosjektet kan stilles som issues her på GitHub.


## For NAV-ansatte

Interne henvendelser kan sendes via Slack i kanalen [#po-pensjon-team-etterlatte](https://nav-it.slack.com/archives/C01KJ597UAU).
