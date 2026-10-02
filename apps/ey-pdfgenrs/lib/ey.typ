// Felles oppsett og komponenter for malene i ey-pdfgenrs.
//
// Målet er at PDF-ene skal se tilnærmet like ut som fra ey-pdfgen, så verdiene under
// er hentet fra ey-pdfgen/templates/eypdfgen/partials_v2/style.hbs.
//
// NB: Denne fila kan ikke ligge under templates/, fordi pdfgenrs krever at alle .typ-filer
// der ligger på formen templates/<app>/<mal>.typ. Den kopieres til /app/lib i Dockerfile.

// CSS-piksler er 1/96 tomme, altså 0.75pt.
#let px(n) = n * 0.75pt

#let farge-header = rgb("#C2EAF7")
#let farge-linje = rgb("#dddddd")
#let farge-dempet = rgb("#666666")
#let farge-panel = rgb("#efefef")

// TODO: kalibrer mot en PDF generert av ey-pdfgen.
#let sidemarger = 1.5cm

// Tilsvarer {{#if verdi includeZero=true}} i Handlebars: none, false, "", tomme lister og
// tomme objekter er usanne. Tall (også 0) regnes som sanne.
#let har(verdi) = (
  verdi != none and verdi != false and verdi != "" and verdi != () and verdi != (:)
)

// Henter en verdi fra nøstede JSON-objekter, f.eks. hent(data, "payload", "slate").
// Gir none hvis et ledd mangler eller er null, slik Handlebars gjør.
#let hent(objekt, ..noekler) = {
  let verdi = objekt
  for noekkel in noekler.pos() {
    if type(verdi) != dictionary { return none }
    verdi = verdi.at(noekkel, default: none)
  }
  verdi
}

// Gjør om en JSON-verdi til tekst slik Handlebars skriver den ut. none blir tom tekst.
#let tekst(verdi) = {
  if verdi == none {
    ""
  } else if type(verdi) == bool {
    if verdi { "true" } else { "false" }
  } else {
    str(verdi)
  }
}

// Tilsvarer {{iso_to_nor_date}}: "2023-06-14" eller "2023-06-14T12:00:00" blir "14.06.2023".
// Verdier som ikke er ISO-datoer skrives ut uendret.
#let nor-dato(verdi) = {
  let s = tekst(verdi)
  let treff = s.match(regex("^([0-9]{4})-([0-9]{2})-([0-9]{2})"))
  if treff == none { return s }
  let (aar, maaned, dag) = treff.captures
  dag + "." + maaned + "." + aar
}

// Tilsvarer {{breaklines}}: linjeskift i fritekst blir linjeskift i PDF-en.
#let linjeskift(verdi) = {
  let linjer = tekst(verdi).replace("\r\n", "\n").split("\n")
  linjer.map(linje => [#linje]).join(linebreak())
}

// Tilsvarer {{iso_to_nor_datetime}}. pdfgen-formateringen viser dato og timer/minutter.
#let nor-dato-tid(verdi) = {
  let s = tekst(verdi)
  let treff = s.match(regex("^([0-9]{4})-([0-9]{2})-([0-9]{2})T([0-9]{2}):([0-9]{2})"))
  if treff == none { return s }
  let (aar, maaned, dag, time, minutt) = treff.captures
  dag + "." + maaned + "." + aar + " " + time + ":" + minutt
}

// Tilsvarer {{capitalize_names}}: normaliserer mellomrom og bruker stor forbokstav
// etter mellomrom, bindestrek og apostrof.
#let navn(verdi) = {
  let s = tekst(verdi).trim().replace(regex("\\s+"), " ")
  let deler = s.split(" ")
  deler.map(ord => {
    ord.split("-").map(bindestrek => {
      bindestrek.split("'").map(del => {
        if del == "" { [] }
        else { upper(del.slice(0, 1)) + lower(del.slice(1)) }
      }).join("'")
    }).join("-")
  }).join(" ")
}

#let som-liste(verdi) = if type(verdi) == array { verdi } else { () }

// Setter opp side, font og avsnitt. Brukes som `#show: dokument.with(tittel: ...)`.
// Tittel er påkrevd fordi pdfgenrs lager PDF/UA-1.
#let dokument(tittel: none, sidetekst: none, body) = {
  assert(tittel != none, message: "dokument() krever en tittel")

  set document(title: tittel)
  set page(
    paper: "a4",
    margin: (top: sidemarger, bottom: 1.8cm, left: sidemarger, right: sidemarger),
    footer: context grid(
      columns: (1fr, auto),
      if sidetekst == none { [] } else { text(size: px(12), fill: farge-dempet, sidetekst) },
      text(size: px(12), align(right)[Side #counter(page).display() av #counter(page).final().at(0)]),
    ),
  )
  set text(font: "Source Sans Pro", lang: "nb", size: px(16))
  // <p> har 1em marg over og under i HTML.
  set par(spacing: 1em, leading: 0.65em)

  body
}

// Overskrift med samme utseende som h1–h4 i de gamle malene. Størrelse og marg er
// standardverdiene i HTML: h2 = 1.5em/0.83em, h3 = 1.17em/1em, h4 = 1em/1.33em.
//
// `nivaa` er det semantiske nivået i PDF-en og velges uavhengig av utseendet, siden
// PDF/UA ikke tillater at et nivå hoppes over (de gamle malene går f.eks. rett fra h1 til h4).
#let overskrift(innhold, nivaa: 2, stoerrelse: px(16), marg: 1.33) = {
  block(
    above: marg * stoerrelse,
    below: if marg == 0 { 0pt } else { 0.6 * stoerrelse },
    sticky: true,
    {
      set text(size: stoerrelse * 0.95, weight: "bold")
      show heading: set block(above: 0pt, below: 0pt)
      heading(level: nivaa, innhold)
    },
  )
}

#let h2(innhold, nivaa: 2) = overskrift(innhold, nivaa: nivaa, stoerrelse: px(24), marg: 1.4)
#let h3(innhold, nivaa: 3) = overskrift(innhold, nivaa: nivaa, stoerrelse: px(18.72), marg: 1)
#let h4(innhold, nivaa: 2) = overskrift(innhold, nivaa: nivaa, stoerrelse: px(15), marg: 1.33)
#let h5(innhold) = block(
  above: 1.67em,
  below: 1.67em,
  text(size: px(13.28) * 0.95, weight: "bold", innhold),
)

// Innholdet under headeren (.container).
#let container(body) = pad(x: 0.7cm, body)

// Punktliste som <ul>: 1em marg og innrykk slik at teksten starter 40px inn.
// `luft` er avstanden mellom punktene. TODO: kalibrer innrykk mot en PDF fra ey-pdfgen.
#let punktliste(punkter, luft: auto) = context block(
  above: 1em,
  below: 1em,
  list(
    indent: px(26),
    body-indent: px(8),
    tight: false,
    spacing: if luft == auto { 4pt } else { calc.max(luft.to-absolute(), 4pt) },
    ..punkter,
  ),
)

// Lyseblått felt øverst med Nav-logo og tittel (#header, .navlogo og .title).
#let header(tittel) = block(
  width: 100%,
  fill: farge-header,
  inset: (x: 0.7cm),
  above: 0pt,
  below: 0.6cm,
  grid(
    // Tittelen sto absolutt posisjonert 100px fra venstre kant.
    columns: (px(100) - 0.7cm, 1fr),
    align: horizon,
    pad(
      top: px(15),
      bottom: px(12),
      left: px(3),
      image("/resources/Navlogo.png", width: px(48), alt: "Nav-logo"),
    ),
    overskrift(tittel, nivaa: 1, stoerrelse: px(16), marg: 0),
  ),
)

// Rad med spørsmål og svar i to kolonner (.row.opplysning og .col).
#let opplysning(spoersmaal, svar) = block(
  width: 100%,
  inset: (x: 0.1cm, y: 0.22cm),
  above: 0pt,
  below: 0pt,
  stroke: (bottom: px(1) + farge-linje),
  text(size: px(14), grid(
    columns: (49%, 49%),
    column-gutter: px(4),
    align: horizon,
    stroke: (x, y) => if x == 0 { (right: px(1) + farge-linje) },
    spoersmaal,
    svar,
  )),
)

// Hjelpere for standardformen på søknadsopplysninger: {spoersmaal, svar: {innhold, verdi}}.
#let raddata(objekt) = {
  if har(objekt) {
    let svar = hent(objekt, "svar")
    let innhold = if type(svar) == dictionary { hent(svar, "innhold") } else { svar }
    opplysning(tekst(hent(objekt, "spoersmaal")), tekst(innhold))
  }
}

#let rad-dato(objekt) = {
  if har(objekt) {
    let svar = hent(objekt, "svar")
    opplysning(tekst(hent(objekt, "spoersmaal")), nor-dato(if type(svar) == dictionary { hent(svar, "innhold") } else { svar }))
  }
}

#let rad-liste(objekt) = {
  if har(objekt) {
    let svar = hent(objekt, "svar")
    let innhold = if type(svar) == array { svar.map(item => tekst(hent(item, "innhold"))) } else { (tekst(hent(svar, "innhold")),) }
    opplysning(
      tekst(hent(objekt, "spoersmaal")),
      punktliste(innhold, luft: 0pt),
    )
  }
}
