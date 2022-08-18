# ADR Navnestandard på hendelser som publiseres

## Status
Besluttet 2022-06-09 i utviklersync-møte

## Bakgrunn
En del  av arkitekturen som har vokst fram i løsningen er at applikasjoner reagerer på hendelsene til hverandre.
Det kan oppleves rotete og lite intuitivt om alt om hvordan en hendelse ser ut defineres i hver app.

## Alternativer
### Innføre en navnestandard for hendelser
Vi blir enige om en navnestandard, og alle apper må forholde seg til det.
En slik navnestandard kan for eksempel utformes slik at det er enkelt å se hvor hendelsen kommer fra og hva den gjelder, uten detaljert kunskap om alle appene.

### Ikke innføre en navnestandard for hendelser
Vi lar alle apper lage sine egne navn på hendelser helt uavhengig av andre apper.

## Konklusjon
Vi vil ha en felles navnestandard på hendelsene våre.
Hendelser publiseres med navn på formatet `DOMENE:HENDELSE`. For eksempel publiserer behandling hendelsen `BEHANDLING:OPPRETTET` når en behandling er opprettet.
