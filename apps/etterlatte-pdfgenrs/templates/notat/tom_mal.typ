#let data = json("/data/notat/tom_mal.json")
#let label(key) = str(key).replace("_", " ")
#let render(value) = {
  if type(value) == dictionary {
    for key in value.keys() {
      let child = value.at(key)
      if type(child) == dictionary or type(child) == array {
        heading(level: 2)[#label(key)]
        render(child)
      } else {
        strong[#label(key)]: #str(child) #linebreak()
      }
    }
  } else if type(value) == array {
    for item in value { render(item) }
  } else { str(value) }
}
#set document(title: "Notat")
#set page(paper: "a4", margin: 18mm, footer: align(right)[#counter(page).display()])
#set text(font: "Source Sans Pro", lang: "nb", size: 10pt)
= Notat
#render(data)
