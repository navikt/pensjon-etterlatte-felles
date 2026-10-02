// Portert fra ey-pdfgen/templates/eypdfgen/omstillingsstoenad_v1.hbs og partials_v2.
#import "/lib/ey.typ": *

#let data = json("/data/eypdfgen/omstillingsstoenad_v1.json")
#let spraak = tekst(hent(data, "spraak"))
#let tittel = "Søknad om omstillingsstønad"

#let velg(en, nn, nb) = if spraak == "en" { en } else if spraak == "nn" { nn } else { nb }
#let overskrift4(tittel, nivaa: 2) = h4(tittel, nivaa: nivaa)
#let svarverdi(objekt) = tekst(hent(objekt, "svar", "verdi"))
#let ja(objekt) = svarverdi(objekt) == "JA"
#let navn-paa(person) = [#navn(tekst(hent(person, "fornavn", "svar"))) #navn(tekst(hent(person, "etternavn", "svar")))]
#let personnummer(person) = {
  let fnr = hent(person, "foedselsnummer", "svar")
  if har(fnr) { " (" + tekst(fnr) + ")" }
  else { " (" + tekst(hent(person, "foedselsdato", "svar")) + ")" }
}
#let rad-personalia(person) = {
  for felt in ("fornavn", "etternavn", "foedselsnummer", "foedselsdato") {
    raddata(hent(person, felt))
  }
}
#let rad-med-status(objekt, detalj) = {
  raddata(objekt)
  if ja(objekt) { raddata(detalj) }
}
#let rad-dato-status(objekt, dato) = {
  raddata(objekt)
  if ja(objekt) { rad-dato(dato) }
}
#let ramme(body) = block(
  inset: (x: 0.2cm, y: 0.05cm),
  above: 0.7cm,
  below: 0.7cm,
  fill: farge-panel,
  stroke: px(1) + farge-linje,
  body,
)
#let rad-flervalg(objekt) = {
  if har(objekt) {
    let valg = hent(objekt, "svar")
    let innhold = if type(valg) == array { valg.map(item => tekst(hent(item, "innhold"))) } else { () }
    opplysning(tekst(hent(objekt, "spoersmaal")), punktliste(innhold, luft: 0pt))
  }
}

#show: dokument.with(
  tittel: tittel,
  sidetekst: if har(hent(data, "imageTag")) {
    [#text(fill: farge-dempet, size: px(12))[#tekst(hent(data, "template")) - #tekst(hent(data, "imageTag"))]]
  } else { [] },
)

#header(tittel)

#container[
  // Innsender og eventuell søker på vegne av.
  #grid(
    columns: (1fr, 1fr),
    column-gutter: px(8),
    align: top,
    [
      #overskrift4(velg("Sent by:", "Sendt inn av:", "Sendt inn av:"))
      #navn-paa(hent(data, "innsender")) #personnummer(hent(data, "innsender"))
      #linebreak()
      #nor-dato-tid(hent(data, "mottattDato"))
    ],
    {
      let innsender-fnr = hent(data, "innsender", "foedselsnummer", "svar")
      let soeker-fnr = hent(data, "soeker", "foedselsnummer", "svar")
      if innsender-fnr != soeker-fnr [
        #overskrift4(velg("On behalf of", "På vegne av", "På vegne av"))
        #navn-paa(hent(data, "soeker"))
        #personnummer(hent(data, "soeker"))
      ]
    },
  )

  #let maalform = if spraak == "nn" { "Nynorsk" } else if spraak == "en" { "English" } else { "Bokmål" }
  #overskrift4(velg("Language:", "Målform:", "Målform:"))
  #maalform

  // Utbetalingsinformasjon.
  #let utbetaling = hent(data, "utbetalingsInformasjon")
  #h2(velg("Payment information", "Utbetalingsinformasjon", "Utbetalingsinformasjon"))
  #overskrift4(
    [#navn(tekst(hent(utbetaling, "svar", "innhold"))) #velg("account", "konto", "konto")],
  )
  #opplysning(tekst(hent(utbetaling, "spoersmaal")), navn(tekst(hent(utbetaling, "svar", "innhold"))))
  #for felt in ("kontonummer", "utenlandskBankNavn", "utenlandskBankAdresse", "iban", "swift") [
    #raddata(hent(utbetaling, "opplysning", felt))
  ]

  // Gjenlevende.
  #let soeker = hent(data, "soeker")
  #h2(velg("About the survivor", "Om den attlevande", "Om den gjenlevende"))
  #rad-personalia(soeker)
  #opplysning(tekst(hent(soeker, "statsborgerskap", "spoersmaal")), tekst(hent(soeker, "statsborgerskap", "svar")))
  #opplysning(tekst(hent(soeker, "sivilstatus", "spoersmaal")), tekst(hent(soeker, "sivilstatus", "svar")))
  #if har(hent(soeker, "adresse")) [
    #opplysning(tekst(hent(soeker, "adresse", "spoersmaal")), tekst(hent(soeker, "adresse", "svar")))
  ]
  #raddata(hent(soeker, "flyktning"))
  #raddata(hent(soeker, "bostedsAdresse"))

  #let kontakt = hent(soeker, "kontaktinfo")
  #overskrift4(velg("Contact information", "Kontaktinfo", "Kontaktinfo"), nivaa: 3)
  #raddata(hent(kontakt, "telefonnummer"))

  // Nåværende oppholdsland.
  #let opphold = hent(soeker, "oppholdUtland")
  #overskrift4(velg("Current country of residence", "Opphaldsland", "Oppholdsland"), nivaa: 3)
  #raddata(opphold)
  #if ja(opphold) [
    #let detaljer = hent(opphold, "opplysning")
    #rad-med-status(hent(detaljer, "oppholderSegIUtlandet"), none)
    #if svarverdi(hent(detaljer, "oppholderSegIUtlandet")) == "JA" [
      #raddata(hent(detaljer, "oppholdsland"))
      #rad-dato(hent(detaljer, "oppholdFra"))
      #rad-dato(hent(detaljer, "oppholdTil"))
    ]
    #if svarverdi(hent(detaljer, "oppholderSegIUtlandet")) == "NEI" [
      #raddata(hent(detaljer, "bosattLand"))
    ]
  ]

  // Sivilstand, samboer og relasjon til avdøde.
  #let ny-sivilstatus = hent(soeker, "nySivilstatus")
  #overskrift4(velg("Current marital status", "Noverande sivilstand", "Sivilstanden i dag"), nivaa: 3)
  #raddata(ny-sivilstatus)
  #if har(hent(ny-sivilstatus, "opplysning")) [
    #let samboer = hent(ny-sivilstatus, "opplysning")
    #overskrift4(velg("Cohabiting partner", "Sambuar", "Samboer"), nivaa: 4)
    #rad-personalia(samboer)
    #raddata(hent(samboer, "fellesBarnEllertidligereGift"))
  ]

  #let forhold = hent(soeker, "forholdTilAvdoede")
  #overskrift4(velg("Relationship with the deceased", "Relasjonen til avdøde", "Relasjonen til avdøde"), nivaa: 3)
  #raddata(hent(forhold, "relasjon"))
  #for felt in ("datoForInngaattPartnerskap", "datoForInngaattSamboerskap", "datoForSkilsmisse", "datoForSamlivsbrudd") [
    #rad-dato(hent(forhold, felt))
  ]
  #for felt in ("fellesBarn", "samboereMedFellesBarnFoerGiftemaal", "tidligereGift", "omsorgForBarn", "mottokBidrag") [
    #raddata(hent(forhold, felt))
  ]
  #if ja(hent(forhold, "mottokBidrag")) [#raddata(hent(forhold, "mottokBidrag", "opplysning"))]

  #let omsorg = hent(soeker, "omsorgForBarn")
  #overskrift4(velg("Childcare", "Omsorg for barn", "Omsorg for barn"), nivaa: 3)
  #raddata(omsorg)
  #raddata(hent(soeker, "uregistrertEllerVenterBarn"))

  // Gjenlevendes arbeid, utdanning og situasjon.
  #h2(velg("Information about the survivor's current situation", "Opplysingar om attlevandes situasjon", "Opplysninger om gjenlevendes situasjon"))
  #let arbeid = hent(soeker, "arbeidOgUtdanning")
  #overskrift4(velg("Work and education", "Arbeid og utdanning", "Arbeid og utdanning"), nivaa: 3)
  #rad-flervalg(hent(arbeid, "dinSituasjon"))

  #let arbeidsforhold = hent(arbeid, "arbeidsforhold", "svar")
  #if har(arbeidsforhold) [
    #for jobb in arbeidsforhold [
      #let jobbinnhold = [
        #overskrift4(velg("About your employer", "Om arbeidsforhold", "Om arbeidsgiver"), nivaa: 4)
        #raddata(hent(jobb, "arbeidsgiver"))
        #raddata(hent(jobb, "arbeidsmengde"))
        #let ansettelse = hent(jobb, "ansettelsesforhold")
        #raddata(ansettelse)
        #if svarverdi(ansettelse) == "MIDLERTIDIG" { raddata(hent(jobb, "harSluttdato")) }
        #if ja(hent(jobb, "harSluttdato")) { rad-dato(hent(jobb, "sluttdato")) }
        #rad-med-status(hent(jobb, "endretArbeidssituasjon"), hent(jobb, "endretArbeidssituasjon", "opplysning"))
        #raddata(hent(jobb, "sagtOppEllerRedusert"))
      ]
      #if arbeidsforhold.len() > 1 { ramme(jobbinnhold) } else { jobbinnhold }
    ]
  ]

  #let naeringer = hent(arbeid, "selvstendig", "svar")
  #if har(naeringer) [
    #overskrift4(velg("About your business", "Om næringa", "Om næringen"), nivaa: 3)
    #for naering in naeringer [
      #let naeringsinnhold = [
        #raddata(hent(naering, "firmanavn"))
        #raddata(hent(naering, "orgnr"))
        #raddata(hent(naering, "arbeidsmengde"))
        #rad-med-status(hent(naering, "endretArbeidssituasjon"), hent(naering, "endretArbeidssituasjon", "opplysning"))
      ]
      #if naeringer.len() > 1 { ramme(naeringsinnhold) } else { naeringsinnhold }
    ]
  ]

  #let etablerer = hent(arbeid, "etablererVirksomhet", "svar")
  #if har(etablerer) [
    #overskrift4(velg("Setting up business", "Etablerer verksemd", "Etablerer virksomhet"), nivaa: 3)
    #raddata(hent(etablerer, "virksomheten"))
    #raddata(hent(etablerer, "orgnr"))
    #let plan = hent(etablerer, "forretningsplan")
    #raddata(plan)
    #if ja(plan) [#raddata(hent(etablerer, "samarbeidMedNav"))]
  ]

  #let tilbud = hent(arbeid, "tilbud", "svar")
  #if har(tilbud) [
    #overskrift4(velg("Received a job offer", "Tilbod om jobb", "Tilbud om jobb"), nivaa: 3)
    #raddata(hent(tilbud, "nyttArbeidssted"))
    #rad-dato(hent(tilbud, "ansettelsesdato"))
    #let ansettelse = hent(tilbud, "ansettelsesforhold")
    #raddata(ansettelse)
    #raddata(hent(tilbud, "arbeidsmengde"))
    #if svarverdi(ansettelse) == "MIDLERTIDIG" or svarverdi(ansettelse) == "TILKALLINGSVIKAR" [
      #raddata(hent(tilbud, "harSluttdato"))
    ]
    #if ja(hent(tilbud, "harSluttdato")) [#rad-dato(hent(tilbud, "sluttdato"))]
    #raddata(hent(tilbud, "aktivitetsplan"))
  ]

  #let arbeidssoker = hent(arbeid, "arbeidssoeker", "svar")
  #if har(arbeidssoker) [
    #overskrift4(velg("Job seeker", "Arbeidssøkjar", "Arbeidssøker"), nivaa: 3)
    #let registrert = hent(arbeidssoker, "registrertArbeidssoeker")
    #rad-med-status(registrert, hent(arbeidssoker, "aktivitetsplan"))
  ]

  #let utdanning = hent(arbeid, "utdanning", "svar")
  #if har(utdanning) [
    #overskrift4(velg("Education", "Utdanning", "Utdanning"), nivaa: 3)
    #for felt in ("studiested", "studie", "studieform") [#raddata(hent(utdanning, felt))]
    #if svarverdi(hent(utdanning, "studieform")) == "DELTID" [#raddata(hent(utdanning, "studieprosent"))]
    #for felt in ("startDato", "sluttDato") [#rad-dato(hent(utdanning, felt))]
    #for felt in ("godkjentUtdanning", "aktivitetsplan") [#raddata(hent(utdanning, felt))]
    #raddata(hent(arbeid, "utdanning", "navn"))
  ]

  #let annen-situasjon = hent(arbeid, "annenSituasjon", "svar")
  #if har(annen-situasjon) [
    #overskrift4(velg("Other", "Anna", "Annet"), nivaa: 3)
    #rad-flervalg(hent(annen-situasjon, "beskrivelse"))
    #raddata(hent(annen-situasjon, "annet"))
  ]

  #let fullfoert-utdanning = hent(soeker, "fullfoertUtdanning")
  #if har(fullfoert-utdanning) [
    #opplysning(tekst(hent(fullfoert-utdanning, "spoersmaal")), punktliste(som-liste(hent(fullfoert-utdanning, "svar")).map(item => tekst(hent(item, "innhold"))), luft: 0pt))
  ]

  // Gjenlevendes inntekter og ytelser.
  #h2(velg("Information about the survivor's current income", "Opplysingar om attlevandes inntekt", "Opplysninger om gjenlevendes inntekt"))
  #let inntekt = hent(soeker, "inntektOgPensjon")
  #let alderspensjon = hent(inntekt, "skalGaaAvMedAlderspensjon")
  #if har(alderspensjon) [
    #overskrift4(velg("Information about retirement pension", "Opplysingar om alderspensjon", "Opplysninger om alderspensjon"), nivaa: 3)
    #rad-dato-status(hent(alderspensjon, "valg"), hent(alderspensjon, "datoForAaGaaAvMedAlderspensjon"))
    #if svarverdi(hent(alderspensjon, "valg")) == "TAR_ALLEREDE_UT_ALDERSPENSJON" [
      #rad-dato(hent(alderspensjon, "datoForAaGaaAvMedAlderspensjon"))
    ]
  ]

  #for felt in (
    "inntektFremTilDoedsfallet",
    "forventetInntektIAar",
    "forventetInntektTilNesteAar",
  ) [
    #let inntektsdel = hent(inntekt, felt)
    #if har(inntektsdel) [
      #h4(tekst(hent(inntektsdel, "spoersmaal")), nivaa: 3)
      #let svar = hent(inntektsdel, "svar")
      #raddata(hent(svar, "arbeidsinntekt"))
      #let naeringsinntekt = hent(svar, "naeringsinntekt")
      #raddata(hent(naeringsinntekt, "inntekt"))
      #if tekst(hent(naeringsinntekt, "inntekt", "svar", "innhold")) != "0" [
        #let jevnt = hent(naeringsinntekt, "erNaeringsinntektOpptjentJevnt")
        #raddata(hent(jevnt, "valg"))
        #if svarverdi(hent(jevnt, "valg")) == "NEI" [#raddata(hent(jevnt, "beskrivelse"))]
      ]
      #let afp = hent(svar, "afpInntekt")
      #if har(afp) [
        #raddata(hent(afp, "inntekt"))
        #if tekst(hent(afp, "inntekt", "svar", "innhold")) != "0" [#raddata(hent(afp, "tjenesteordning"))]
      ]
      #raddata(hent(svar, "inntektFraUtland"))
      #let andre = hent(svar, "andreInntekter")
      #raddata(hent(andre, "valg"))
      #if svarverdi(hent(andre, "valg")) == "JA" [
        #raddata(hent(andre, "inntekt"))
        #raddata(hent(andre, "beskrivelse"))
      ]
      #let paavirkning = hent(svar, "noeSomKanPaavirkeInntekten")
      #raddata(hent(paavirkning, "valg"))
      #if svarverdi(hent(paavirkning, "valg")) == "JA" [
        #raddata(hent(paavirkning, "grunnTilPaavirkelseAvInntekt"))
        #if svarverdi(hent(paavirkning, "grunnTilPaavirkelseAvInntekt")) == "ANNEN_GRUNN" [
          #raddata(hent(paavirkning, "beskrivelse"))
        ]
      ]
    ]
  ]

  #for (ytelsefelt, ytelsetittel) in (
    ("ytelserNAV", velg("Information about benefits from NAV", "Opplysingar om ytingar frå NAV", "Opplysninger om ytelser fra NAV")),
    ("ytelserAndre", velg("Information about benefits from others", "Opplysingar om ytingar frå andre", "Opplysninger om ytelser fra andre")),
  ) [
    #overskrift4(ytelsetittel, nivaa: 3)
    #let ytelser = hent(inntekt, ytelsefelt)
    #raddata(hent(ytelser, "soektOmYtelse"))
    #if har(hent(ytelser, "tjenestepensjonsordning")) [
      #raddata(hent(ytelser, "tjenestepensjonsordning", "type"))
      #raddata(hent(ytelser, "tjenestepensjonsordning", "utbetaler"))
    ]
    #raddata(hent(ytelser, "utland", "svar"))
    #if ja(hent(ytelser, "soektOmYtelse")) [#rad-flervalg(hent(ytelser, "soektYtelse"))]
    #if svarverdi(hent(ytelser, "svar", "endringAvInntekt", "grunn")) == "ANNEN_GRUNN" [
      #raddata(hent(ytelser, "svar", "endringAvInntekt", "annenGrunn"))
    ]
    #raddata(hent(ytelser, "pensjonsordning"))
  ]

  // Avdøde.
  #let avdoed = hent(data, "avdoed")
  #h2(velg("The deceased’s personal details", "Opplysingar om avdøde", "Opplysninger om avdøde"))
  #rad-personalia(avdoed)
  #raddata(hent(avdoed, "statsborgerskap"))
  #rad-dato(hent(avdoed, "datoForDoedsfallet"))
  #raddata(hent(avdoed, "doedsaarsakSkyldesYrkesskadeEllerYrkessykdom"))

  #let utenlands = hent(avdoed, "utenlandsopphold")
  #overskrift4(velg("Time spent outside Norway", "Opphald utanfor Noreg", "Opphold utenfor Norge"), nivaa: 3)
  #raddata(utenlands)
  #if ja(utenlands) [
    #let oppholdsliste = hent(utenlands, "opplysning")
    #for landopphold in som-liste(oppholdsliste) [
      #let landinnhold = [
        #h5(tekst(hent(landopphold, "land", "svar", "innhold")))
        #raddata(hent(landopphold, "land"))
        #rad-dato(hent(landopphold, "fraDato"))
        #rad-dato(hent(landopphold, "tilDato"))
        #rad-flervalg(hent(landopphold, "oppholdsType"))
        #raddata(hent(landopphold, "medlemFolketrygd"))
        #raddata(hent(landopphold, "pensjonsutbetaling"))
      ]
      #if oppholdsliste.len() > 1 { ramme(landinnhold) } else { landinnhold }
    ]
  ]

  #let militaer = hent(avdoed, "militaertjeneste")
  #if har(militaer) [
    #overskrift4(velg("Military or civil service", "Militær eller sivil førstegongsteneste", "Militær eller sivil førstegangstjeneste"), nivaa: 3)
    #rad-med-status(militaer, hent(militaer, "opplysning"))
  ]

  // Barn.
  #let barn = hent(data, "barn")
  #h2(velg("Information about children", "Opplysingar om barn", "Opplysninger om barn"))
  #if har(barn) [
    #h3(velg("Registered children:", "Registrerte barn:", "Registrerte barn:"), nivaa: 3)
    #for barnedata in barn [
      #let barninnhold = [
        #h5(navn-paa(barnedata))
        #rad-personalia(barnedata)
        #opplysning(tekst(hent(barnedata, "statsborgerskap", "spoersmaal")), tekst(hent(barnedata, "statsborgerskap", "svar")))
        #let utenlands-adresse = hent(barnedata, "utenlandsAdresse")
        #raddata(utenlands-adresse)
        #if ja(utenlands-adresse) [
          #raddata(hent(utenlands-adresse, "opplysning", "land"))
          #raddata(hent(utenlands-adresse, "opplysning", "adresse"))
        ]
        #raddata(hent(barnedata, "dagligOmsorg"))
        #let verge = hent(barnedata, "verge")
        #raddata(verge)
        #if svarverdi(verge) == "JA" [
          #let verge-info = hent(verge, "opplysning")
          #opplysning(velg("Guardian", "Verje", "Verge"), [#navn-paa(verge-info)#personnummer(verge-info)])
        ]
        #for forelder in som-liste(hent(barnedata, "foreldre")) [
          #opplysning(
            velg("Parent", "Forelder", "Forelder"),
            [#navn-paa(forelder)#personnummer(forelder)],
          )
        ]
        #raddata(hent(barnedata, "ufoeretrygd"))
        #raddata(hent(barnedata, "arbeidsavklaringspenger"))
      ]
      #if barn.len() > 1 { ramme(barninnhold) } else { barninnhold }
    ]
  ] else [
    #emph(velg(
      "The sender/applicant have not supplied any information regarding children.",
      "Innsender/søkar har ikkje gitt opplysingar om eventuelle barn.",
      "Innsender/søker har ikke opplyst om eventuelle barn.",
    ))
  ]
]
