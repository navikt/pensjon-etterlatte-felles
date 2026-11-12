#import "/lib/ey.typ": *

#let data = json("/data/eypdfgen/utvidet_omstillingsstoenad_v1.json")
#let spraak = tekst(hent(data, "spraak"))
#let tittel = "Søknad om utvidet omstillingsstønad"
#let velg(en, nn, nb) = if spraak == "en" { en } else if spraak == "nn" { nn } else { nb }
#let svarverdi(objekt) = tekst(hent(objekt, "svar", "verdi"))
#let rad-personalia(person) = {
  for felt in ("fornavn", "etternavn", "foedselsnummer", "foedselsdato") {
    raddata(hent(person, felt))
  }
}
#let personnavn(person) = [
  #tekst(hent(person, "fornavn", "svar")) #tekst(hent(person, "etternavn", "svar"))
]

#show: dokument.with(
  tittel: tittel,
  sidetekst: if har(hent(data, "imageTag")) {
    [#text(fill: farge-dempet, size: px(12))[#tekst(hent(data, "template")) - #tekst(hent(data, "imageTag"))]]
  } else { [] },
)

#header(tittel)

#container[
  #let innsender = hent(data, "innsender")
  #let soeker = hent(data, "soeker")
  #grid(
    columns: (1fr, 1fr),
    column-gutter: px(8),
    align: top,
    [
      #h4(velg("Sent by:", "Sendt inn av:", "Sendt inn av:"))
      #personnavn(innsender) (#tekst(hent(innsender, "foedselsnummer", "svar")))
      #linebreak()
      #nor-dato-tid(hent(data, "mottattDato"))
    ],
    {
      if hent(innsender, "foedselsnummer", "svar") != hent(soeker, "foedselsnummer", "svar") [
        #h4(velg("On behalf of", "På vegne av", "På vegne av"))
        #personnavn(soeker)
        #let ident = hent(soeker, "foedselsnummer", "svar")
        (#tekst(if har(ident) { ident } else { hent(soeker, "foedselsdato", "svar") }))
      ]
    },
  )

  #h4(velg("Language:", "Målform:", "Målform:"))
  #if spraak == "en" { "English" } else if spraak == "nn" { "Nynorsk" } else { "Bokmål" }

  #let utbetaling = hent(data, "utbetalingsInformasjon")
  #if har(utbetaling) [
    #h2(velg("Payment information", "Utbetalingsinformasjon", "Utbetalingsinformasjon"))
    #h4(
      [#navn(tekst(hent(utbetaling, "svar", "innhold"))) #velg("account", "konto", "konto")],
      nivaa: 3,
    )
    #opplysning(
      tekst(hent(utbetaling, "spoersmaal")),
      navn(tekst(hent(utbetaling, "svar", "innhold"))),
    )
    #for felt in ("kontonummer", "utenlandskBankNavn", "utenlandskBankAdresse", "iban", "swift") [
      #raddata(hent(utbetaling, "opplysning", felt))
    ]
  ]

  #h2(velg("Personal information", "Personopplysningar", "Personopplysninger"))
  #rad-personalia(soeker)

  #h2(velg("Your situation today", "Situasjonen din i dag", "Situasjonen din i dag"))
  #let sivilstand = hent(data, "situasjonenDinIDag", "nySivilstand")
  #raddata(sivilstand)
  #let samboer = hent(sivilstand, "opplysning")
  #if svarverdi(sivilstand) == "NY_SAMBOER" and har(samboer) [
    #h4(velg("Cohabiting partner", "Sambuar", "Samboer"), nivaa: 3)
    #rad-personalia(samboer)
    #raddata(hent(samboer, "fellesBarnEllertidligereGift"))
  ]

  #h2(velg("Education and measures", "Utdanning og tiltak", "Utdannelse og tiltak"))
  #let aktivitet = hent(data, "utdanningOgTiltak")
  #raddata(hent(aktivitet, "aktivitet"))
  #let utdanning = hent(aktivitet, "utdanning")
  #if har(utdanning) [
    #for felt in ("studiested", "studie") [#raddata(hent(utdanning, felt))]
    #for felt in ("startDato", "sluttDato") [#rad-dato(hent(utdanning, felt))]
    #for felt in ("studieform", "studieprosent") [#raddata(hent(utdanning, felt))]
  ]
  #let tiltak = hent(aktivitet, "arbeidsrettetTiltak")
  #if har(tiltak) [
    #for felt in ("tiltak", "omfang") [#raddata(hent(tiltak, felt))]
  ]
  #raddata(hent(aktivitet, "aktivitetsplan"))

  #h2(velg("Income", "Inntekt", "Inntekt"))
  #let inntekt = hent(data, "inntekt")
  #raddata(hent(inntekt, "arbeidsinntekt"))
  #let naering = hent(inntekt, "naeringsinntekt")
  #raddata(hent(naering, "inntekt"))
  #if har(hent(naering, "inntekt")) and tekst(hent(naering, "inntekt", "svar", "innhold")) != "0" [
    #let jevn-inntekt = hent(naering, "erNaeringsinntektOpptjentJevnt")
    #raddata(hent(jevn-inntekt, "valg"))
    #if svarverdi(hent(jevn-inntekt, "valg")) == "NEI" [
      #raddata(hent(jevn-inntekt, "beskrivelse"))
    ]
  ]
  #raddata(hent(inntekt, "inntektFraUtland"))
  #let andre = hent(inntekt, "andreInntekter")
  #raddata(hent(andre, "valg"))
  #if svarverdi(hent(andre, "valg")) == "JA" [
    #for felt in ("inntekt", "beskrivelse") [#raddata(hent(andre, felt))]
  ]
  #let endring = hent(inntekt, "noeSomKanPaavirkeInntekten")
  #raddata(hent(endring, "valg"))
  #if svarverdi(hent(endring, "valg")) == "JA" [
    #raddata(hent(endring, "grunnTilPaavirkelseAvInntekt"))
    #if svarverdi(hent(endring, "grunnTilPaavirkelseAvInntekt")) == "ANNEN_GRUNN" [
      #raddata(hent(endring, "beskrivelse"))
    ]
  ]
]
