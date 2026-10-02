// Portert fra ey-pdfgen/templates/notat/tom_mal.hbs
//
// Gjør om et Slate-dokument fra notat-editoren i Gjenny til PDF.
// Forventer {"tittel": "...", "payload": {"slate": [element, ...]}}, se SlatePDFMal i etterlatte-brev-api.
//
// Avvik fra den gamle malen: tekstbiter i samme avsnitt eller overskrift står nå på samme linje.
// Før ble hver bit et eget avsnitt, så "2. Rett til " + "<STØNAD>" (plassholder) havnet på to linjer.
#import "/lib/ey.typ": *

#let data = json("/data/notat/tom_mal.json")
// PDF/UA krever en tittel, så tom tittel erstattes.
#let tittel = {
  let t = tekst(hent(data, "tittel"))
  if t == "" { "Notat" } else { t }
}

// All tekst i en node og dens barn, slått sammen.
#let tekst-fra(node) = {
  if type(node) != dictionary { return "" }
  let t = node.at("text", default: none)
  if t != none { return t }
  let barn = node.at("children", default: none)
  if barn == none { return "" }
  barn.map(tekst-fra).sum(default: "")
}

#let barn-av(node) = {
  let barn = node.at("children", default: none)
  if barn == none { () } else { barn }
}

#let elementer = barn-av((children: hent(data, "payload", "slate")))

#show: dokument.with(tittel: tittel)

#header(tittel)

#container({
  // heading-three blir nivå 2 i PDF-en hvis det ikke har vært noen heading-two før.
  let har-h2 = false

  for element in elementer {
    let elementtype = element.at("type", default: none)
    let barn = barn-av(element)

    if elementtype == "heading-two" {
      har-h2 = true
      h2(tekst-fra(element))
    } else if elementtype == "heading-three" {
      h3(tekst-fra(element), nivaa: if har-h2 { 3 } else { 2 })
    } else if elementtype == "paragraph" {
      // Tekstbiter samles til ett avsnitt. En punktliste inne i avsnittet avslutter avsnittet.
      let avsnitt = ""
      for b in barn {
        if b.at("type", default: none) == "bulleted-list" {
          if avsnitt != "" { par(avsnitt) }
          avsnitt = ""
          punktliste(barn-av(b).map(tekst-fra))
        } else {
          avsnitt += tekst-fra(b)
        }
      }
      // Tomme avsnitt ga ingen ekstra luft i HTML (margene kollapset), så de hoppes over.
      if avsnitt != "" { par(avsnitt) }
    } else if elementtype == "bulleted-list" {
      // Hvert punkt var sin egen <ul> i den gamle malen, og fikk dermed 1em luft mellom seg.
      punktliste(barn.map(tekst-fra), luft: 1em)
    }
  }
})
