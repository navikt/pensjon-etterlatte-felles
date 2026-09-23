#let data = json("/data/eypdfgen/omstillingsstoenad_v1.json")
#let label(key) = str(key).replace("_", " ")
#let render(value, depth: 0) = {
  if value == none { return [] }
  if type(value) == dictionary {
    let result = ()
    for key in value.keys() {
      let child = value.at(key)
      if type(child) == dictionary or type(child) == array {
        result.push([#heading(level: calc.min(depth + 2, 3))[#label(key)] #render(child, depth: depth + 1)])
      } else {
        result.push([#strong[#label(key)]: #str(child) #linebreak()])
      }
    }
    result
  } else if type(value) == array {
    [#for item in value { render(item, depth: depth) }]
  } else {
    [#str(value)]
  }
}

#set document(title: "Søknad om omstillingsstønad")
#set page(paper: "a4", margin: (top: 18mm, bottom: 18mm, left: 18mm, right: 18mm),
  header: grid(columns: (1fr, auto), image("/resources/Navlogo.png", width: 28mm), []),
  footer: align(right)[#counter(page).display()])
#set text(font: "Source Sans Pro", lang: "nb", size: 10pt)
= Søknad om omstillingsstønad

#render(data)
