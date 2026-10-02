// Portert fra ey-pdfgen/templates/omsendringer/oms_meldt_inn_endring_v1.hbs
#import "/lib/ey.typ": *

#let data = json("/data/omsendringer/oms_meldt_inn_endring_v1.json")
#let tittel = "Meldt inn endringer for Omstillingsstønad med sakId " + tekst(data.at("sakId", default: none))

#show: dokument.with(tittel: tittel)

#header(tittel)

#h4[Det ble oppgitt #tekst(data.at("tidspunkt", default: none))]

#opplysning[Endringstype][#tekst(data.at("type", default: none))]

#let endringer = data.at("endringer", default: none)
#if har(endringer) {
  opplysning[Beskrivelse av endring][#tekst(endringer)]
}

#let inntekt = data.at("forventetInntektTilNesteAar", default: none)
#if har(inntekt) [
  #h4[Forventet inntekt for #tekst(inntekt.at("inntektsaar", default: none))]

  #let alderspensjon = inntekt.at("skalGaaAvMedAlderspensjon", default: none)
  #if har(alderspensjon) {
    opplysning[Skal gå av med alderspensjon neste år][#tekst(alderspensjon)]
    if alderspensjon == "JA" {
      opplysning[Når skal du gå av med alderspensjon?][
        #tekst(inntekt.at("datoForAaGaaAvMedAlderspensjon", default: none))
      ]
    }
  }

  #for (felt, spoersmaal) in (
    ("arbeidsinntekt", "Arbeidsinntekter og andre utbetalinger"),
    ("naeringsinntekt", "Næringsinntekt"),
    ("inntektFraUtland", "Inntekt fra utland"),
  ) {
    let beloep = inntekt.at(felt, default: none)
    if har(beloep) {
      opplysning(spoersmaal, tekst(beloep) + " kr")
    }
  }

  #let afp = inntekt.at("afpInntekt", default: none)
  #if har(afp) {
    opplysning[AFP][#tekst(afp) kr]
    opplysning[Tjenesteordning du får AFP fra][#tekst(inntekt.at("afpTjenesteordning", default: none))]
  }
]
