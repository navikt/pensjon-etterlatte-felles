
# Navnestandard på kafka-medlinger

## Status
Besluttet sommer 2022 og alle meldinger i løsninger er ryddet opp i. 

## Bakgrunn
Stor variasjon i navngivning, der en variabel ofte har 2-3 ulike varianter. Behov for en standard løsning. 

## Alternativer
### Innføre en navnestandard 
Vi blir enige om en navnestandard, og alle apper må forholde seg til det.

### Ikke innføre en navnestandard 
Alle meldinger kan fortsette å skrives litt vilkårlig. 


## Konklusjon
Felles-nøkler er flyttet til et eget bibliotek og skal hentes derfra.  
Disse hentes i appen rapidsandrivers-extra, under StandardKeys.kt, og inkluderer
- @correlation_id
- @event_name
- @behov


For egne meldinger gjelder følgende:

- CamelCase for alle keynavn vi setter selv på toppnivå i melding (eg sakId, behandlingId)
- @ brukes bare på "system-keys", så alle egne meldinger skal ikke ha @. 



