// Portert fra ey-pdfgen/templates/notat/klage_oversendelse_blankett.hbs og notat/klageblankett/*.hbs
//
// Forventer {"tittel": "...", "payload": KlageBlankettPdfgenDTO}, se Klage.kt i etterlatte-behandling-model.
#import "/lib/ey.typ": *

#let data = json("/data/notat/klage_oversendelse_blankett.json")
#let tittel = {
  let t = tekst(hent(data, "tittel"))
  if t == "" { "Klage oversendelsesblankett" } else { t }
}
#let klage = hent(data, "payload")
#let formkrav = hent(klage, "formkrav")

// notat/klageblankett/erKravetOppfylt.hbs
#let oppfylt(verdi) = if har(verdi) [Oppfylt] else [Ikke oppfylt]

#show: dokument.with(tittel: tittel)

#header(tittel)

#container[
  // notat/klageblankett/informasjon.hbs
  #h3(nivaa: 2)[Informasjon om saken]

  #opplysning[Saksnummer][#tekst(hent(klage, "sakId"))]
  #opplysning[Saktype][#tekst(hent(klage, "sakTypeFormatert"))]
  #opplysning[Saken gjelder][#tekst(hent(klage, "sakGjelder"))]
  #opplysning[Klager][#tekst(hent(klage, "klager"))]
  #opplysning[Klagedato][#nor-dato(hent(klage, "klageDato"))]

  // notat/klageblankett/formkrav.hbs
  #h3(nivaa: 2)[Formkrav og klagefrist]

  #let vedtak = hent(formkrav, "vedtaketKlagenGjelder")
  #if har(vedtak) {
    opplysning[Dato vedtak attestert][#nor-dato(hent(vedtak, "datoAttestert"))]
    opplysning[Type vedtak][#tekst(hent(klage, "vedtakTypeFormatert"))]
  } else {
    opplysning[Vedtak som klages på][Det klages ikke på et konkret vedtak]
  }

  #opplysning[Er klagen signert?][#oppfylt(hent(formkrav, "erKlagenSignert"))]
  #opplysning[Er klager part i saken?][#oppfylt(hent(formkrav, "erKlagerPartISaken"))]
  #opplysning[Er klagen framsatt innen frist?][#oppfylt(hent(formkrav, "erKlagenFramsattInnenFrist"))]
  #opplysning[Klages det på konkrete elementer i vedtaket?][
    #oppfylt(hent(formkrav, "gjelderKlagenNoeKonkretIVedtaket"))
  ]

  #let begrunnelse = hent(formkrav, "begrunnelse")
  #if har(begrunnelse) {
    opplysning[Begrunnelse][#linjeskift(begrunnelse)]
  }

  // notat/klageblankett/vurdering.hbs
  #h3(nivaa: 2)[Vurdering]

  #opplysning[Utfall][Oppretthold vedtak]
  #opplysning[Hjemmel][#tekst(hent(klage, "hjemmel"))]
  // "ovesendelseTekst" er skrevet feil i DTO-en, men må matche.
  #opplysning[Innstilling til NAV Klageinstans][#linjeskift(hent(klage, "ovesendelseTekst"))]

  #let internKommentar = hent(klage, "internKommentar")
  #if har(internKommentar) {
    opplysning[Intern kommentar][#linjeskift(internKommentar)]
  }
]
