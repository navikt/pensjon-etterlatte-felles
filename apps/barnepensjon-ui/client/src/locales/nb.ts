import { TKey, TNamespace, Translation } from '../context/language/translations'

const common = {
    firstName: 'Fornavn',
    lastName: 'Etternavn',
    name: 'Navn',
    fnrDnr: 'Fødselsnummer / d-nummer',
    address: 'Bostedsadresse',
    maritalStatus: 'Sivilstatus',
    citizenship: 'Statsborgerskap',
    phoneNumber: 'Telefonnummer',
    phoneNumberHelpText: 'Telefonnummeret er hentet fra Kontakt- og reservasjonsregisteret.',
    whyWeAsk: 'Hvorfor spør vi om dette',
    dateFormat: '(dd.mm.yyyy)',
    dateExample: 'eks. 01.11.2020',
    chooseCountry: 'Velg land',
    chooseLanguage: 'Velg språk',
    norway: 'Norge',
}

const navigation = {
    cancelApplicationTitle: 'Vil du avbryte søknaden?',
    cancelApplicationBody: 'Du kan fortsette nå eller senere. Søknaden din lagres i 72 timer.',
    continueApplicationButton: 'Nei, jeg vil fortsette',
    cancelApplicationButton: 'Ja, avbryt og fortsett senere',
    deleteApplicationButton: 'Ja, avbryt og slett søknaden',
}

const btn = {
    backButton: 'Tilbake',
    nextButton: 'Neste',
    saveButton: 'Lagre',
    cancelButton: 'Avbryt',
    removeButton: 'Fjern',
    editButton: 'Endre',
}

const loggedInUserInfo = {
    incorrectInfoMustBeCorrected:
        'Hvis opplysningene vi har om deg ikke stemmer, må du endre disse hos Folkeregisteret.',
    mostFieldsAreRequired: 'Alle felt må fylles ut, bortsett fra de som er markert som valgfrie.',
}

const paymentDetails = {
    title: 'Oppgi bankopplysninger',
    NORSK: 'Norsk',
    UTENLANDSK: 'Utenlandsk',
    bankAccount: 'Oppgi norsk kontonummer for utbetaling',
    information: 'Du kan bare ha ett kontonummer registrert hos NAV.',
    accountType: 'Ønsker du å motta utbetalingen på norsk eller utenlandsk bankkonto?',
    foreignBankName: 'Bankens navn',
    foreignBankAddress: 'Bankens fulle adresse',
    iban: 'IBAN-nummer',
    ibanHelpText: 'IBAN står for International Bank Account Number og er en internasjonal standard for kontonummer.',
    swift: 'Bankens S.W.I.F.T (BIC) adresse',
    swiftHelpText:
        'BIC står for Bank Identifier Code, og er den koden som identifiserer banken. BIC kalles også SWIFT, og er påkrevd ved betaling til en rekke land.',
    'taxWithhold.answer': 'Ønsker du at vi legger inn et skattetrekk for barnepensjonen?',
    'taxWithhold.helpText':
        'Barnepensjon er skattepliktig, men vi trekker ikke skatt av beløpet uten at vi får beskjed om det. Hvis du har spørsmål om skatt må du ta kontakt med Skatteetaten.',
    'taxWithhold.taxPercentage': 'Oppgi ønsket skattetrekk',
    'taxWithhold.placeholder': 'i prosent, eks. 20%',
    'taxWithhold.info': 'Vær oppmerksom på at frivillig skattetrekk må sendes inn på nytt hvert kalenderår.',
}

const radiobuttons = {
    JA: 'Ja',
    NEI: 'Nei',
    VET_IKKE: 'Vet ikke',
}

const frontPage = {
    frontPageTitle: 'Søknad om barnepensjon',
    helloUser: `Hei, {fornavn} {etternavn}`,
    startApplication: 'Start søknad',
    childMayBeApplicableForPension:
        'Barn som mister en eller begge foreldrene, kan motta økonomisk støtte. Pensjonen skal sikre inntekt til å leve og til boutgifter.\n\nDersom barnet er under 18 år, er det vergen som må søke om barnepensjon på barnets vegne. Hvis vergen ikke er en av foreldrene, må det legges ved en bekreftelse på oppnevnelse av verge fra tingretten eller Statsforvalteren.',
    childrenAboveLegalAge: 'Barn over 18 år, må vanligvis søke selv.',
    aboutChildrensPension: 'Mer informasjon om barnepensjon finner du på ',
    aboutChildrensPensionLink: 'nav.no/barnepensjon',
    aboutChildrensPensionHref: 'https://www.nav.no/barnepensjon',
    weNeedCorrectInformation: 'For å kunne behandle søknaden må du gi oss riktige opplysninger',
    importantChangesMustBeNotified:
        'Mottaker av barnepensjon må melde fra når det skjer viktige endringer som for eksempel endringer i bo- og familiesituasjon og ved flytting til utlandet. Barn over 18 år må også gi beskjed om endringer i inntekt og/eller utdanningssituasjon.',
    weWillRetrieveInfo: 'Vi vil hente informasjon',
    infoWeRetrieve:
        'I tillegg til den informasjonen du oppgir i søknaden, henter vi inn informasjon om deg, avdøde og søsken for å avgjøre om du har rett til stønad.' +
        '\nVi henter:',
    infoWeRetrieve_li1: '<strong>Personinformasjon</strong> fra Folkeregisteret',
    infoWeRetrieve_li2: '<strong>Inntektsinformasjon</strong> fra Skatteetaten',
    infoWeRetrieve_li3: 'Opplysninger om <strong>arbeidsforhold</strong> fra Arbeidsgiver- og arbeidstakerregisteret',
    infoWeRetrieve_li4: 'Opplysninger om <strong>annen støtte fra NAV</strong>',
    infoWeRetrieve_li5: 'Eventuelt informasjon fra <strong>utenlandske trygdemyndigheter</strong>',
    workInProgress:
        'Vi jobber med å forbedre denne søknaden. Inntil videre må du legge inn de fleste opplysningene manuelt.',
    howWeHandleDataHref:
        'https://www.nav.no/no/nav-og-samfunn/om-nav/personvern-i-arbeids-og-velferdsetaten/personvernerklaering-for-arbeids-og-velferdsetaten',
    howWeHandleDataLink: 'Slik behandler vi personopplysningene dine',
    aboutPrivacyHref:
        'https://www.nav.no/no/nav-og-samfunn/om-nav/personvern-i-arbeids-og-velferdsetaten/personvern-og-sikkerhet-pa-nav.no#chapter-3',
    aboutPrivacyLink: 'Om personvern og sikkerhet på nav.no',
    aboutTheApplicationTitle: 'Om søknaden',
    aboutTheApplicationDescription:
        'I søknaden stiller vi kun spørsmål som er relevante i din/barnet/barnas situasjon.\n\n' +
        'Vi lagrer søknaden i 72 timer og du kan ta pauser under utfylling. Søknaden kan også avbrytes og opplysningene som er lagt inn kan slettes.',
    consentTitle: 'Vi stoler på deg',
    consentDescription:
        'Du må gi oss riktige opplysninger for at vi skal kunne behandle søknaden. Hvis barnepensjon utbetales fordi opplysninger er utelatt eller det er gitt feil opplysninger, må pensjonen vanligvis betales tilbake.\n\n',
    consentToNav: 'Jeg, {fornavn} {etternavn}, bekrefter at jeg vil gi riktige og fullstendige opplysninger.',
}

const selectScenario = {
    selectSituationToContinue: 'Velg situasjon for å gå videre med søknaden',
    whoIsApplying: 'Hvem søker om barnepensjon?',
    PARENT: 'Jeg søker på vegne av mitt/mine barn',
    GUARDIAN: 'Jeg søker for ett eller flere barn jeg er verge for',
    CHILD: 'Jeg er fylt 18 år og søker på vegne av meg selv',
    parentApplicantInformation:
        'Du kan søke om barnepensjon til flere barn i denne søknaden. Du må oppgi alle søsken, uansett alder, fordi det kan ha betydning for beregningen av barnepensjon. \n\nDersom barna ikke er helsøsken, skal det benyttes en ny søknadsblankett for hvert barnekull.',
    childApplicantInformation1:
        'Hvis du er under 20 år og er under utdanning, er lærling eller praktikant, kan du søke om barnepensjon.',
    childApplicantInformation2:
        'Dersom du er under 21 år og er under utdanning, er lærling eller praktikant, kan du søke om barnepensjon dersom dødsfallet til en av foreldrene dine skyldes yrkesskade eller yrkessykdom.',
    aboutSurvivorsPensionTitle: 'Søke om gjenlevendepensjon til deg selv?',
    aboutSurvivorsPensionDescription:
        'Du kan ha rettigheter som gjenlevende hvis den andre forelderen til barnet ditt dør. Da kan du',
    aboutSurvivorsPensionLink: 'velge å søke om gjenlevendepensjon og barnepensjon i samme søknad.',
    aboutSurvivorsPensionHref: 'https://www.nav.no/gjenlevendepensjon',
    BOTH_PARENTS_DECEASED: 'Foreldreløs',
    BOTH_PARENTS_DECEASED_CHILD_APPLICANT: 'Jeg er foreldreløs',
    ONE_PARENT_DECEASED: 'En forelder er død',
}

const aboutYou = {
    title: 'Om deg',
    stayWhy:
        'Hvis du oppholder deg i et annet land enn Norge kan det avgjøre om du får gjenlevendepensjon og hvor mye du kan få.',
    addressOfResidenceConfirmed: 'Bor du på denne adressen?',
    alternativeAddress: 'Oppgi nåværende bostedsadresse',
    residesInNorway: 'Oppholder du deg for tiden i Norge?',
    countryOfResidence: 'Oppgi land',
    memberFolketrygdenAbroad: 'Er du medlem i folketrygden under opphold i et annet land enn Norge?',
    'contactInfo.phoneNumber': 'Telefonnummer',
    'subtitle.personalia': 'Personalia',
    'subtitle.informationAboutApplicant': 'Opplysninger om søker',
    'paymentDetails.accountType': paymentDetails.accountType,
    'paymentDetails.bankAccount': paymentDetails.bankAccount,
    'paymentDetails.foreignBankName': paymentDetails.foreignBankName,
    'paymentDetails.foreignBankAddress': paymentDetails.foreignBankAddress,
    'paymentDetails.iban': paymentDetails.iban,
    'paymentDetails.swift': paymentDetails.swift,
    'changeAnswerSummary.om-deg': 'Endre svar om deg',
}

const livingParent = {
    title: 'Om den gjenlevende',
    who: 'Oppgi informasjon om den gjenlevende forelderen.',
    address: 'Adresse',
    phoneNumberOptional: 'Telefonnummer (valgfri)',
}

const aboutTheDeceased = {
    title: 'Om den avdøde',
    'title.livingParent': 'Om den gjenlevende',
    firstParentTitle: 'Om den første forelderen',
    secondParentTitle: 'Om den andre forelderen',
    singleParentTitle: 'Om den avdøde',
    dateOfDeath: 'Når skjedde dødsfallet?',
    phoneNumber: 'Telefonnummer (valgfri)',
    abroadStaysTitle: 'Opphold utenfor Norge',
    workOrLivingAbroadCanAffectPension:
        'Vi trenger å vite om avdøde har bodd eller arbeidet utenfor Norge. Dette kan både påvirke hvor mye du kan få i gjenlevendepensjon og gi deg pensjonsrettigheter fra andre land.',
    didTheDeceasedLiveAbroad: 'Bodde eller arbeidet han eller hun i et annet land enn Norge etter fylte 16 år?',
    'staysAbroad.abroadStays.country': 'Land',
    'staysAbroad.abroadStays.type': 'Bodd og/eller arbeidet?',
    'staysAbroad.abroadStays.fromDate': 'Fra dato (valgfri)',
    'staysAbroad.abroadStays.toDate': 'Til dato (valgfri)',
    'staysAbroad.abroadStays.medlemFolketrygd': 'Var han eller hun medlem av folketrygden under oppholdet?',
    'staysAbroad.abroadStays.medlemFolketrygd.why':
        'Vi må vite om avdøde var medlem av folketrygden for å avgjøre rettigheten til barnepensjon.',
    'staysAbroad.abroadStays.pensionAmount': 'Oppgi eventuell pensjon han eller hun mottok fra dette landet (valgfri)',
    'selfEmplyment.title': 'Næringsinntekt',
    'selfEmplyment.ingress':
        'Vi trenger å vite om avdøde hadde inntekt som selvstendig næringsdrivende. Dette er viktig når vi skal beregne hvor mye du kan få i gjenlevendepensjon. Vi henter informasjon om andre inntekter.',
    'selfEmplyment.wasSelfEmployed': 'Var han eller hun selvstendig næringsdrivende?',
    'selfEmplyment.selfEmplymentDetails.income': 'Oppgi næringsinntekt fra kalenderåret før dødsfallet (valgfri)',
    'selfEmplyment.selfEmplymentDetails.income.placeholder': 'Samlet årsinntekt før skatt',
    'selfEmplyment.selfEmplymentDetails.incomeAtDeath': 'Hadde han eller hun næringsinntekt når dødsfallet skjedde?',
    otherTitle: 'Annet',
    occupationalInjury: 'Skyldes dødsfallet yrkesskade eller yrkessykdom?',
    'occupationalInjury.why':
        'Hvis dødsfallet skyldes godkjent yrkesskade eller yrkessykdom kan det avgjøre hvor mye du kan få.',
    'militaryService.completed':
        'Har han eller hun gjennomført militær eller sivil førstegangstjeneste som varte minst 30 dager?',
    'militaryService.why': 'Dette kan gi opptjening som tas med i beregningen av barnepensjonen.',
    'militaryService.period': 'Hvilke(-t) år? (valgfri)',
    'oppholdUtlandType.BODD': 'Bodd',
    'oppholdUtlandType.ARBEIDET': 'Arbeidet',
    'btn.addCountry': '+ Legg til flere land',
    'changeAnswerSummary.om-foreldrene': 'Endre svar om foreldre',
}

const aboutChildren = {
    childAppliedForPension: 'Søkt om barnepensjon',
    applyForThisChild: 'Søk om barnepensjon',
    userAppliesForChildrensPension: 'Ja, jeg søker om barnepensjon for barnet',
    onlyJointChildrenNecessary:
        'Det er kun nødvendig å opplyse om helsøsken under 22 år i denne søknaden. Det kreves en separat søknad for halvsøsken.',
    onlyChildrenUnder22Necessary: 'Det er kun nødvendig å opplyse om barn under 22 år i denne søknaden',
    livesIn: 'Bor i',
    'staysAbroad.answer': 'Bor barnet i et annet land enn Norge?',
    'staysAbroad.sibling.answer': 'Bor søskenet i et annet land enn Norge?',
    'staysAbroad.country': 'Land',
    'staysAbroad.address': 'Adresse i utlandet',
    'childHasGuardianship.lastName': 'Etternavn (valgfri)',
    'childHasGuardianship.firstName': 'Fornavn (valgfri)',
    'childHasGuardianship.name': 'Navn på verge',
    'childHasGuardianship.answer': 'Er det oppnevnt en verge for barnet?',
    'childHasGuardianship.fnr': 'Fødselsnummer til verge (valgfri)',
    'childHasGuardianship.fnrPlaceholder': '11 siffer',
    information:
        'Dersom du har eller har hatt barn kan det påvirke retten din til gjenlevendepensjon. Derfor må du oppgi alle barn, uavhengig av hvor gamle de er. Dette gjelder barn du har felles med avdøde, avdødes egne barn, og dine egne barn. \n\n Hvis du har felles barn under 18 år med avdøde kan du også søke om barnepensjon her. Barn over 18 år må søke selv.',
    infoRegardingSiblings: 'Her er info hvis det er søsken',
    infoCard_residence: 'BOSTED',
    infoCard_fnr: 'FØDSELSNUMMER',
    infoCard_citizenship: 'STATSBORGERSKAP',
    removeChildButton: 'Fjern fra søknad',
    addChildButton: '+ Legg til barn',
    addSiblingButton: '+ Legg til søsken',
    whoAreTheParents: 'Hvem er foreldre til barnet?',
    relationHelpText:
        'Vi må vite om dette er et barn du har felles med avdøde, avdødes eget barn, eller ditt eget barn.',
    aboutChildrenTitle: 'Om barn',
    titleModal: 'Om barnet',
    aboutTheSiblingTitle: 'Om søskenet',
    aboutSiblingsTitle: 'Om søsken',
    thisIsOptional: 'Dette er valgfritt',
    childBelongsToParents: 'Er {forelder1} og {forelder2} foreldrene til barnet?',
    youAndDeceasedAreTheParents: 'Er du og den avdøde foreldrene til barnet?',
    loggedInUserIsGuardian: 'Er du verge for dette barnet?',
    onlyGuardiansCanApply:
        'Det er kun mulig å søke om barnepensjon for barn du er verge til. Du må likevel oppgi informasjon om helsøsken under 20 år.',
    'changeAnswerSummary.om-barn': 'Endre svar om barn',
}

const summary = {
    summaryTitle: 'Oppsummering',
    readTheSummaryBeforeSending:
        'Les gjennom oppsummeringen av din søknad før du sender. \nHvis du trenger å gjøre endringer, kan du gå tilbake og gjøre det.',
    sendApplicationButton: 'Send søknad',
}

const error = {
    fixTheseErrorsToContinue: 'For å gå videre må du rette opp i dette:',
    'abroadStays.hasStaysAbroad.required': 'Oppgi om den avdøde har bodd eller jobbet i et annet land enn Norge',
    'addressOfResidenceConfirmed.required': 'Bostedsadresse må bekreftes/avkreftes',
    'alternativeAddress.required': 'Du må oppgi nåværende bostedsadresse',
    'citizenship.required': 'Oppgi statsborgerskap',
    'countryOfResidence.required': 'Oppgi nåværende oppholdsland',
    'dateOfDeath.required': 'Oppgi når dødsfallet skjedde',
    'firstName.required': 'Oppgi fornavn',
    'fnr.required': 'Oppgi fødselsnummer',
    'fnrDnr.duplicate': 'Duplikat fødselsnummer / d-nummer',
    'fnr.validate': 'Ikke et gyldig fødselsnummer',
    'fnrDnr.required': 'Oppgi fødselsnummer',
    'fnrDnr.validate': 'Ikke et gyldig fødselsnummer',
    'lastName.required': 'Oppgi etternavn',
    'address.required': 'Oppgi adresse',
    'phoneNumber.minLength': 'Telefonnummer er for kort',
    'child.required': 'Du må søke om barnepensjon for minst ett barn.',
    'child.validate': 'Du må søke om barnepensjon for minst ett barn.',
    'memberFolketrygdenAbroad.required': 'Oppgi om du er medlem i folketrygden under opphold i et annet land enn Norge',
    'militaryService.completed.required': 'Oppgi om avdøde har gjennomført verneplikt',
    'occupationalInjury.required': 'Oppgi om dødsfallet skyldes yrkesskade eller yrkessykdom',
    'paymentDetails.taxWithhold.answer.required': 'Oppgi om det ønskes å legge til skattetrekk ',
    'paymentDetails.taxWithhold.taxPercentage.required': 'Oppgi ønsket skattetrekk',
    'paymentDetails.bankAccount.required': 'Norsk kontonummer må fylles ut (11 siffer)',
    'paymentDetails.accountType.required': 'Du må velge mellom norsk eller utenlandsk bankkonto for utbetaling',
    'paymentDetails.foreignBankName.required': 'Navnet på den utenlandske banken må fylles ut',
    'paymentDetails.foreignBankAddress.required': 'Adressen til den utenlandske banken må fylles ut',
    'paymentDetails.iban.required': 'IBAN-nummer må fylles ut',
    'paymentDetails.iban.validate': 'Ugyldig IBAN-nummer',
    'paymentDetails.swift.required': 'Bankens S.W.I.F.T (BIC) adresse må fylles ut',
    'paymentDetails.swift.validate': 'Ugyldig SWIFT-kode',
    'residesInNorway.required': 'Opphold må besvares',
    'selfEmplyment.wasSelfEmployed.required': 'Oppgi om avdøde var selvstendig næringsdrivende',
    'staysAbroad.answer.required': 'Oppgi om personen er bosatt i et annet land enn Norge',
    'bothParents.required': 'Oppgi barnets relasjon til foreldre',
    'loggedInUserIsGuardian.required': 'Oppgi om du er verge for barnet',
    'childrensPension.paymentDetails.taxWithhold.answer.required': 'Oppgi om det ønskes å legge til skattetrekk ',
    'childrensPension.paymentDetails.taxWithhold.taxPercentage.required': 'Oppgi ønsket skattetrekk',
    'childrensPension.paymentDetails.accountType.required':
        'Du må velge mellom norsk eller utenlandsk bankkonto for utbetaling',
    'childrensPension.paymentDetails.foreignBankName.required': 'Navnet på den utenlandske banken må fylles ut',
    'childrensPension.paymentDetails.foreignBankAddress.required': 'Adressen til den utenlandske banken må fylles ut',
    'childrensPension.paymentDetails.iban.required': 'IBAN-nummer må fylles ut',
    'childrensPension.paymentDetails.iban.validate': 'Ugyldig IBAN-nummer',
    'childrensPension.paymentDetails.swift.required': 'Bankens S.W.I.F.T (BIC) adresse må fylles ut',
    'childrensPension.paymentDetails.swift.validate': 'Ugyldig SWIFT-kode',
    'childrensPension.paymentDetails.bankAccount.required': 'Norsk kontonummer må fylles ut (11 siffer)',
    'staysAbroad.country.required': 'Oppgi hvilke land barnet bor i',
    'staysAbroad.address.required': 'Oppgi barnets bostedsadresse',
    'childHasGuardianship.answer.required': 'Oppgi om det er oppnevnt verge for barnet',
}

const pageNotFound = {
    notFoundTitle: 'Oi, har du gått deg vill?',
    pageDoesNotExist: 'Denne siden finnes ikke.',
    pageDoesNotExistInfo:
        'Dersom du har klikket på en lenke på våre sider og endt opp her, kan du rapportere feilen her:',
    reportErrorLink: 'www.nav.no/tilbakemelding-feilogmangler',
    reportErrorHref: 'http://www.nav.no/tilbakemelding-feilogmangler',
    backButton: 'Klikk her for å gå tilbake',
}

const systemUnavailable = {
    guide: 'Å nei, søknaden fungerer ikke...',
    intro: 'Det er en feil i søknaden som gjør at den dessverre ikke fungerer som den skal.',
    description:
        'Vi beklager dette og jobber med å finne ut av det så raskt som mulig. I mellomtiden er det nok lurt å ta en pause og prøve igjen senere.',
    feedback: 'Er det fortsatt feil, kan du melde fra om det på ',
    feedbackLenke: 'www.nav.no/person/kontakt-oss/nb/tilbakemeldinger/feil-og-mangler',
    feedbackHref: 'https://www.nav.no/person/kontakt-oss/nb/tilbakemeldinger/feil-og-mangler',
    moreAboutBenefits: 'Du kan lese mer om ytelser til etterlatte på',
    moreAboutBenefitsLenke: 'www.nav.no/mistet-noen',
    moreAboutBenefitsHref: 'https://www.nav.no/mistet-noen',
    retryButton: 'Prøv igjen',
}

const logOutUser = {
    btn: 'X-ikon for å lukke meldingen',
    time: 'minutter',
    youWillBeLoggedOutIn: 'Du vil bli logget ut om',
    sendNowOrContinueLater: 'Du kan sende søknad nå eller fortsette senere. \n Søknaden din lagres i 72 timer.',
}

const receipt = {
    thankYou: 'Takk for din søknad!',
    pageTitle: 'Søknaden din er sendt til oss',
    contact: 'Hvis vi mangler informasjon for å behandle søknaden din, kontakter vi deg.',
    youMustNotifyRegardingChanges: 'Du må melde fra om endringer',
    importantChangesCanAffectYourRights:
        'Skjer det viktige endringer kan det påvirke retten til og/eller utbetaling av barnepensjon. Det kan for eksempel være ved:',
    changeInLivingSituation: 'endring i bo/familiesituasjon',
    changeAddressOrMoveAbroad: 'flytting eller opphold i et annet land over tid',
    childrenOver18MustNotify: 'Barn over 18 år som mottar barnepensjon må i tillegg gi beskjed om endring:',
    changeInEduation: 'i utdanningssituasjon',
    changedIncome: 'av arbeidsinntekt',
    moreAboutRightsAndDuties: 'Les mer om rettigheter og plikter på',
    moreAboutRightsAndDutiesLinkHref: 'https://nav.no/rettogplikt',
    moreAboutRightsAndDutiesLinkText: 'nav.no/rettogplikt',
    benefitsChangingTitle: 'Regelendringer på barnepensjon',
    benefitsChangingDescription1: 'Barnepensjon skal styrkes. ',
    benefitsChangingDescription1_link: 'De nye endringene',
    benefitsChangingDescription1_href: 'https://www.nav.no/barnepensjon#regel',
    benefitsChangingDescription2: 'gjelder fra tidligst 1. januar 2023.',
    viewCaseTitle: 'Sjekke status i saken?',
    viewCaseInfoContentPart1: 'Forelder eller verge kan ikke følge saken digitalt i Dine saker i',
    viewCaseInfoLinkHref1: 'https://www.nav.no/no/ditt-nav',
    viewCaseInfoLinkText1: 'Ditt NAV',
    viewCaseInfoContentPart2:
        'på barn det er søkt barnepensjon for. Hvis du har spørsmål om søknaden, må du kontakt oss på telefon 55 55 33 34.',
    viewCaseInfoLinkHref2: 'https://tjenester.nav.no/saksoversikt',
    viewCaseInfoContent3:
        'Barn over 18 år kan finne bekreftelse på at søknaden er mottatt i Dine saker og sjekke status i saken ved å logge inn i ',
    viewCaseInfoLinkHref3: 'https://tjenester.nav.no/saksoversikt',
    viewCaseInfoLinkText3: 'Ditt NAV',
    processingTimeText_part1: 'Barnepensjon følger samme ',
    processingTimeText_part2: ' som søknad om gjenlevendepensjon.',
    processingTimeHref4: 'https://www.nav.no/saksbehandlingstider',
    processingTimeLink4: 'saksbehandlingstid',
    closeApplicationButton: 'Avslutt',
}

const aboutParents = {
    aboutParentsTitle: 'Om foreldrene',
    firstParent: 'Forelder 1',
    secondParent: 'Forelder 2',
    survivingParent: 'Gjenlevende forelder',
    deceasedParent: 'Avdød forelder',
    addParentBtn: 'Legg til',
    addSurvivingParentBtn: 'Legg til gjenlevende forelder',
    addDeceasedParentBtn: 'Legg til avdød forelder',
    addFirstParentBtn: 'Legg til forelder 1',
    addSecondParentBtn: 'Legg til forelder 2',
    bothParentsRequired: 'Du må legge til opplysninger om begge foreldre for å fortsette søknaden.',
}

const yourSituation = {
    title: 'Din situasjon',
    whatsYourSituation: 'Hva er din situasjon?',
    timeUsedForEducation: 'Hvor mye tid bruker du på utdanningen?',
    whyDoYouApply: 'Hvorfor søker du barnepensjon etter fyllte 18 år',
    doYouHaveIncome: 'Har du lønnsinntekt?',
    BELOW50: 'Under 50%',
    OVER50: '50% eller mer',
    ORPHAN: 'Jeg er foreldreløs',
    OCCUPATIONAL_INJURY: 'Den avdøde moren eller faren min døde som følge av en godkjent yrkesskade',
    EDUCATION: 'Jeg tar utdanning',
    APPRENTICE: 'Jeg er lærling',
    INTERNSHIP: 'Jeg har praksisplass eller er praktikant',
    'changeAnswerSummary.din-situasjon': 'Endre svar om din situasjon',
}

const continueApplicationModal = {
    doYouWantToContinueWithTheApplication: 'Ønsker du å fortsette utfyllingen av påbegynt søknad?',
    yesContinueWithApplication: 'Ja, jeg vil fortsette der jeg slapp',
    noRestartApplication: 'Nei, start på nytt',
}

const texts: Record<TNamespace, Record<TKey, Translation>> = {
    aboutChildren,
    aboutTheDeceased,
    aboutYou,
    livingParent,
    aboutParents,
    logOutUser,
    error,
    common,
    frontPage,
    loggedInUserInfo,
    navigation,
    paymentDetails,
    btn,
    radiobuttons,
    selectScenario,
    pageNotFound,
    systemUnavailable,
    summary,
    receipt,
    yourSituation,
    continueApplicationModal,
}

export default texts
