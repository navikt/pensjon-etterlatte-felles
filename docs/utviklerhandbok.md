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