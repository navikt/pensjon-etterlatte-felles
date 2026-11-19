#import "/lib/ey.typ": navn

// Tester at æøå/bindestrek/apostrof o.l. i navn ikke kræsjer pdf-generering
#assert.eq(navn("  LAMPE   BORD  "), "Lampe Bord")
#assert.eq(navn("åRE ØKS"), "Åre Øks")
#assert.eq(navn("éCLAIR æSKE"), "Éclair Æske")
#assert.eq(navn("LAMPE-åRE D'øR"), "Lampe-Åre D'Ør")
#assert.eq(navn("ø"), "Ø")
#assert.eq(navn("a\u{030a}RE"), "A\u{030a}re")
#assert.eq(navn(""), [])
#assert.eq(navn(none), [])
