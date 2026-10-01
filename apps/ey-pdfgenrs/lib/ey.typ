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

// Setter opp side, font og overskrifter. Brukes som `#show: dokument.with(tittel: ...)`.
// Tittel er påkrevd fordi pdfgenrs lager PDF/UA-1.
#let dokument(tittel: none, body) = {
  assert(tittel != none, message: "dokument() krever en tittel")

  set document(title: tittel)
  set page(paper: "a4", margin: sidemarger)
  set text(font: "Source Sans Pro", lang: "nb", size: px(16))

  // h1 i de gamle malene
  show heading.where(level: 1): set text(size: px(20), weight: "bold")
  show heading.where(level: 1): set block(above: 0pt, below: 0pt)
  // h4 i de gamle malene. Nivå 2 her, siden PDF/UA ikke tillater hopp i overskriftsnivå.
  show heading.where(level: 2): set text(size: px(16), weight: "bold")
  show heading.where(level: 2): set block(above: 1.33em, below: 1.33em)

  body
}

// Lyseblått felt øverst med Nav-logo og tittel (#header, .navlogo og .title).
#let header(tittel) = block(
  width: 100%,
  fill: farge-header,
  inset: (x: 0.7cm),
  above: 0pt,
  below: 0pt,
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
    heading(level: 1, tittel),
  ),
)

// Rad med spørsmål og svar i to kolonner (.row.opplysning og .col).
#let opplysning(spoersmaal, svar) = block(
  width: 100%,
  inset: 0.1cm,
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
