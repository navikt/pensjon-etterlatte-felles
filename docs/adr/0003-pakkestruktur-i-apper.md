
# Pakkestruktur i apper

## Status
WIP

## Bakgrunn
Det er stor variasjon i hvordan apper er strukturert. Det er et behov for å rydde opp i dette for å sikre at appene er
konsistente på tvers.

## Alternativer
### 1. Fortsette som før
Vi gjør ingen endringer. Hver utvikler tar selv stilling til hva som er en god pakkestruktur i appene man jobber med.

### 2. Innfører en standard på pakkestruktur som gjelder på tvers av apper
For å sikre at appene er konsistente på tvers, innfører vi en standard. Dette vil gi fordeler som:
- Det vil gjøre det lettere å navigere og finne frem til ulike deler i appene.
- Unngår warnings fra editor på at pakkestruktur og fil plassering ikke henger sammen.
- Sikrer at appene ser relativt like ut

Forslag til navnestandard:
- `no.nav.etterlatte` (Pakkestruktur toppnivå - denne vil være uforandret)
- `no.nav.etterlatte.saksbehandling` (Pakkestruktur saksbehandling)
- `no.nav.etterlatte.selvbetjening` (Pakkestruktur selvbetjening)
- `no.nav.etterlatte.saksbehandling.appnavn` (Pakkestruktur for en app i saksbehandling)


#### Pakkestruktur innefor en app
En app kan ha egne pakker for hvert underdomene den håndterer. Feks vil `utbetaling` som nå håndterer `iverksetting` 
og `avstemming` ha disse som separate pakker. I tillegg vil det være naturlig å ha egne pakker for funksjonalitet 
som brukes på tvers av disse domenene (`common`) og konfigurasjon som bruker for å kjøre opp applikasjon (`config`).
Funksjonalitet som brukes på tvers av apper legges i `libs/common`.

Eksempel på en app som følger denne strukturen:
```
├─ utbetaling
│  ├─ common
│  ├─ config
│  ├─ avstemming
│  ├─ iverksetting
│  │  ├─ oppdrag
├─ Application.kt
```

Merk at her ligger `Application.kt` på utsiden av `utbetaling`-pakken. Dette er fordi byggscriptet som bygger appene
er felles, og derfor bruker en fast referanse innenfor hver app for å definere hva jar-filen skal starte i manifestet.

## Konklusjon
TBD



