// Portert fra ey-pdfgen/templates/eypdfgen/barnepensjon_v2.hbs og partials_v2.
#import "/lib/ey.typ": *

#let data = json("/data/eypdfgen/barnepensjon_v2.json")
#let spraak = tekst(hent(data, "spraak"))
#let velg(en, nn, nb) = if spraak == "en" { en } else if spraak == "nn" { nn } else { nb }
#let tittel = velg("Application for children’s pension", "Søknad om barnepensjon", "Søknad om barnepensjon")
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
#let ramme(body) = block(
  inset: (x: 0.2cm, y: 0.05cm),
  above: 0.7cm,
  below: 0.7cm,
  fill: farge-panel,
  stroke: px(1) + farge-linje,
  body,
)
#let som-liste(verdi) = if type(verdi) == array { verdi } else { () }
#let rad-flervalg(objekt) = {
  if har(objekt) {
    let valg = hent(objekt, "svar")
    if type(valg) == array {
      opplysning(tekst(hent(objekt, "spoersmaal")), punktliste(valg.map(item => tekst(hent(item, "innhold"))), luft: 0pt))
    } else {
      raddata(objekt)
    }
  }
}
#let utenlandsopphold(objekt) = {
  let oppholdsliste = som-liste(hent(objekt, "opplysning"))
  [
    #overskrift4(velg("Time spent outside Norway", "Opphald utanfor Noreg", "Opphold utenfor Norge"), nivaa: 3)
    #raddata(objekt)
    #if ja(objekt) {
      for opphold in oppholdsliste {
        let innhold = [
          #h5(tekst(hent(opphold, "land", "svar", "innhold")))
          #raddata(hent(opphold, "land"))
          #rad-dato(hent(opphold, "fraDato"))
          #rad-dato(hent(opphold, "tilDato"))
          #rad-flervalg(hent(opphold, "oppholdsType"))
          #raddata(hent(opphold, "medlemFolketrygd"))
          #raddata(hent(opphold, "pensjonsutbetaling"))
        ]
        if oppholdsliste.len() > 1 { ramme(innhold) } else { innhold }
      }
    }
  ]
}
#let barn-innhold(barn) = {
  let utenlands-adresse = hent(barn, "utenlandsAdresse")
  let verge = hent(barn, "verge")
  [
    #h5(navn-paa(barn))
    #rad-personalia(barn)
    #opplysning(tekst(hent(barn, "statsborgerskap", "spoersmaal")), tekst(hent(barn, "statsborgerskap", "svar")))
    #raddata(utenlands-adresse)
    #if ja(utenlands-adresse) [
      #raddata(hent(utenlands-adresse, "opplysning", "land"))
      #raddata(hent(utenlands-adresse, "opplysning", "adresse"))
    ]
    #raddata(hent(barn, "dagligOmsorg"))
    #raddata(verge)
    #if ja(verge) {
      let verge-info = hent(verge, "opplysning")
      opplysning(velg("Guardian", "Verje", "Verge"), [#navn-paa(verge-info)#personnummer(verge-info)])
    }
    #for forelder in som-liste(hent(barn, "foreldre")) [
      #opplysning(velg("Parent", "Forelder", "Forelder"), [#navn-paa(forelder)#personnummer(forelder)])
    ]
    #raddata(hent(barn, "ufoeretrygd"))
    #raddata(hent(barn, "arbeidsavklaringspenger"))
  ]
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
    [
      #let innsender-fnr = hent(data, "innsender", "foedselsnummer", "svar")
      #let soeker-fnr = hent(data, "soeker", "foedselsnummer", "svar")
      #if innsender-fnr != soeker-fnr [
        #overskrift4(velg("On behalf of", "På vegne av", "På vegne av"))
        #navn-paa(hent(data, "soeker"))
        #personnummer(hent(data, "soeker"))
      ]
    ],
  )

  #overskrift4(velg("Language:", "Målform:", "Målform:"))
  #if spraak == "en" { "English" } else if spraak == "nn" { "Nynorsk" } else { "Bokmål" }

  // Utbetalingsinformasjon.
  #let utbetaling = hent(data, "utbetalingsInformasjon")
  #h2(velg("Payment information", "Utbetalingsinformasjon", "Utbetalingsinformasjon"))
  #overskrift4([#navn(tekst(hent(utbetaling, "svar", "innhold"))) #velg("account", "konto", "konto")])
  #opplysning(tekst(hent(utbetaling, "spoersmaal")), navn(tekst(hent(utbetaling, "svar", "innhold"))))
  #for felt in ("kontonummer", "utenlandskBankNavn", "utenlandskBankAdresse", "iban", "swift") {
    raddata(hent(utbetaling, "opplysning", felt))
  }

  // Opplysninger om barnet som søker.
  #let soeker = hent(data, "soeker")
  #h2(velg("Information about the child (applicant)", "Opplysingar om barnet (søkaren)", "Opplysninger om barnet (søkeren)"))
  #barn-innhold(soeker)

  #let bosatt = hent(soeker, "bosattNorge")
  #if har(bosatt) [
    #overskrift4(velg("Stays abroad", "Opphald utland", "Opphold utland"), nivaa: 3)
    #raddata(bosatt)
    #if ja(bosatt) [
      #let detaljer = hent(bosatt, "opplysning")
      #raddata(hent(detaljer, "oppholdLand"))
      #rad-dato(hent(detaljer, "oppholdFra"))
      #rad-dato(hent(detaljer, "oppholdTil"))
    ]
  ]

  // Opplysninger om foreldre.
  #for forelder in som-liste(hent(data, "foreldre")) [
    #if hent(forelder, "type") == "GJENLEVENDE_FORELDER" [
      #h2(velg("Information about the surviving parent", "Opplysingar om attlevande forelder", "Opplysninger om gjenlevende forelder"))
      #rad-personalia(forelder)
      #opplysning(tekst(hent(forelder, "statsborgerskap", "spoersmaal")), tekst(hent(forelder, "statsborgerskap", "svar")))
      #opplysning(tekst(hent(forelder, "adresse", "spoersmaal")), tekst(hent(forelder, "adresse", "svar")))
      #overskrift4(velg("Contact information", "Kontaktinfo", "Kontaktinfo"), nivaa: 3)
      #raddata(hent(forelder, "kontaktinfo", "telefonnummer"))
    ] else if hent(forelder, "type") == "AVDOED" [
      #h2(velg("The deceased’s personal details", "Opplysingar om avdøde", "Opplysninger om avdøde"))
      #rad-personalia(forelder)
      #raddata(hent(forelder, "statsborgerskap"))
      #rad-dato(hent(forelder, "datoForDoedsfallet"))
      #raddata(hent(forelder, "doedsaarsakSkyldesYrkesskadeEllerYrkessykdom"))
      #if har(hent(forelder, "utenlandsopphold")) {
        utenlandsopphold(hent(forelder, "utenlandsopphold"))
      }
      #let militaer = hent(forelder, "militaertjeneste")
      #if har(militaer) [
        #overskrift4(velg("Military or civil service", "Militær eller sivil førstegongsteneste", "Militær eller sivil førstegangstjeneste"), nivaa: 3)
        #raddata(militaer)
        #if ja(militaer) { raddata(hent(militaer, "opplysning")) }
      ]
    ]
  ]

  // Ukjent forelder oppgis direkte av søkeren.
  #let ukjent = hent(soeker, "ukjentForelder")
  #if har(ukjent) [
    #h2(velg("Unknown parent", "Ukjent forelder", "Ukjent forelder"))
    #raddata(ukjent)
  ]

  // Søsken.
  #h2("Opplysninger om søsken")
  #let soesken = som-liste(hent(data, "soesken"))
  #if soesken.len() > 0 [
    #for soeskenbarn in soesken [
      #let innhold = barn-innhold(soeskenbarn)
      #if soesken.len() > 1 { ramme(innhold) } else { innhold }
    ]
  ] else [
    #emph(velg(
      "The sender/applicant have not supplied any information regarding siblings.",
      "Innsender/søkar har ikkje gitt opplysingar om eventuelle sysken.",
      "Innsender/søker har ikke opplyst om eventuelle søsken.",
    ))
  ]
]
