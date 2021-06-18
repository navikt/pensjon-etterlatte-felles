import { IValg } from "./Spoersmaal";

export enum NySivilstatus {
    ekteskap = "ekteskap",
    partnerskap = "partnerskap",
    samboerskap = "samboerskap",
    ingen = "ingen"
}

export enum OpploesningAarsak {
    doedsfall = "Dødsfall",
    skilsmisse = "Skilsmisse",
    bruddSamboerskap = "Brudd i samboerskap",
}

export enum ForholdTilAvdoed {
    gjenlevendeEktefelle = "Gjenlevende ektefelle",
    gjenlevendePartner = "Gjenlevende partner",
    gjenlevendeSamboer = "Gjenlevende samboer",
    ugiftMenForsoerget = "Ugift, men ble forsørget av den avdøde",
}

export enum BarnRelasjon {
    fellesbarnMedAvdoede = "Fellesbarn m/avdøde",
    avdoedesSaerkullsbarn = "Avdødes særkullsbarn",
    egneSaerkullsbarn = "Egne særkullsbarn",
}

export interface IBarn {
    fornavn?: string;
    etternavn?: string;
    foedselsnummer?: string;
    brukeAnnenKonto?: {
        svar?: IValg;
        kontonummer?: string;
    };
    relasjon?: string;
    bosattUtland?: {
        svar?: IValg;
        statsborgerskap?: string;
        land?: string;
    };
}

export interface IAvdoed {
    fornavn?: string;
    etternavn?: string;
    foedselsnummer?: string;
    doedsdato?: Date | null;
    statsborgerskap?: string;
    bosetning?: IValg;
    doedsfallAarsak?: IValg;
    boddEllerJobbetUtland?: IValg;
    haddePensjonsgivendeInntekt?: {
        svar?: IValg;
        beskrivelse?: string;
    }
    mottokPensjonAndreLand?: {
        svar?: IValg;
        beskrivelse?: string;
    }
    harAvtjentMilitaerTjeneste?: {
        svar?: IValg;
        beskrivelse?: string;
    }
}

export enum SamboerInntekt {
    arbeidsinntekt = "arbeidsinntekt",
    pensjon = "pensjon",
    kapitalinntekt = "kapitalinntekt",
    andreYtelser = "andreYtelser",
}

export interface ISamboer {
    navn?: string;
    foedselsnummer?: string;
    hattBarnEllerVaertGift?: IValg;
    harInntekt?: {
        svar?: IValg;
        inntektstype?: SamboerInntekt[];
        samletBruttoinntektPrAar?: string;
    }
}

export interface IKontaktinfo {
    telefonnummer?: string;
    epost?: string;
}

export interface INySivilstatus {
    nySivilstatusEtterDoedsfallet?: NySivilstatus; // 2.13
    datoForInngaaelse?: Date;
    nySivilstatusOpploest?: IValg; // 2.14
    aarsakForOpploesningen?: OpploesningAarsak; // 2.15
    datoForOpploesningen?: Date;
}

export interface IForholdAvdoed {
    forholdTilAvdoede?: ForholdTilAvdoed; // 2.9
    datoForInngaattPartnerskap?: Date | null;
    // hvis gjenlevende samboer:
    varSkiltFoerDoedsfall?: IValg; // 2.11
    datoForSkilsmisse?: Date | null; // hvis ja over
    mottokBidragFraAvdoede?: IValg; // 2.12
    bidragBeloepPrAar?: string; // hvis ja over
    hattBarnEllerVaertGift?: IValg;
}

export interface ISoeker {
    bostedsadresseBekreftet?: IValg;
    kontaktinfo?: IKontaktinfo;
    kontonummer?: string; // 2.8
    flyktning?: IValg;
    oppholderSegINorge?: IValg; // 2.7
    oppholdsland?: string; // 2.7
    medlemFolketrygdenUtland?: IValg;
    forholdTilAvdoed?: IForholdAvdoed;
    nySivilstatus?: INySivilstatus;
    samboer?: ISamboer; // 2.16
}
