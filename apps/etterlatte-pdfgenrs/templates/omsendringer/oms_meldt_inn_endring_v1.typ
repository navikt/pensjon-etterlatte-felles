#let data = json("/data/omsendringer/oms_meldt_inn_endring_v1.json")
#let label(key) = str(key).replace("_", " ")
#let render(value, depth: 0) = {
  if type(value) == dictionary {
    for key in value.keys() {
      let child = value.at(key)
      if type(child) == dictionary or type(child) == array {
        heading(level: calc.min(depth + 2, 3))[#label(key)]
        render(child, depth: depth + 1)
      } else { strong[#label(key)]: #str(child) #linebreak() }
    }
  } else if type(value) == array {
    for item in value { render(item, depth: depth) }
  } else { str(value) }
}
#set document(title: "Meldt inn endring")
#set page(paper: "a4", margin: 18mm, footer: align(right)[#counter(page).display()])
#set text(font: "Source Sans Pro", lang: "nb", size: 10pt)
= Meldt inn endring
#render(data)
