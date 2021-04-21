import { createContext, FC, useContext, useReducer } from "react";
import { ActionTypes, ISoeknad, ISoeknadAction, SoeknadProps } from "./soknad";

const STORAGE_KEY = "etterlatte-store";

const json = localStorage.getItem(STORAGE_KEY);
const storedState = json ? JSON.parse(json) : null;

const initialState: ISoeknad = storedState || {
    innloggetBruker: null,
    stoenadType: null,
    opplysningerOmSoekeren: null,
    opplysningerOmDenAvdoede: null,
    opplysningerOmBarn: [],
    tidligereArbeidsforhold: [],
    naavaerendeArbeidsforhold: null,
    andreYtelser: null,
};

const reducer = (state: ISoeknad, action: ISoeknadAction) => {
    switch (action.type) {
        case ActionTypes.TILBAKESTILL: {
            return {
                innloggetBruker: null,
                stoenadType: null,
                opplysningerOmSoekeren: null,
                opplysningerOmDenAvdoede: null,
                opplysningerOmBarn: [],
                tidligereArbeidsforhold: [],
                naavaerendeArbeidsforhold: null,
                andreYtelser: null,
            };
        }
        case ActionTypes.OPPDATER_VALGTE_STOENADER:
            return { ...state, stoenadType: action.payload };
        case ActionTypes.OPPDATER_SOEKER:
            return { ...state, opplysningerOmSoekeren: action.payload };
        case ActionTypes.OPPDATER_AVDOED:
            return { ...state, opplysningerOmDenAvdoede: action.payload };
        case ActionTypes.LEGG_TIL_BARN: {
            const { opplysningerOmBarn } = state;

            opplysningerOmBarn.push(action.payload);

            return { ...state, opplysningerOmBarn };
        }
        case ActionTypes.LEGG_TIL_TIDLIGERE_ARBEIDSFORHOLD: {
            return { ...state, tidligereArbeidsforhold: [...state.tidligereArbeidsforhold, action.payload] };
        }
        case ActionTypes.FJERN_TIDLIGERE_ARBEIDSFORHOLD: {
            const indexToDelete: number = action.payload;

            const tidligereArbeidsforhold = [
                ...state.tidligereArbeidsforhold.filter((_: any, index: number) => index !== indexToDelete),
            ];

            return { ...state, tidligereArbeidsforhold };
        }
        case ActionTypes.OPPDATER_NAAVAERENDE_ARBEIDSFORHOLD:
            return { ...state, naavaerendeArbeidsforhold: action.payload };
        case ActionTypes.OPPDATER_ANDRE_YTELSER:
            return { ...state, andreYtelser: action.payload };
        default:
            return state;
    }
};

const SoknadContext = createContext<SoeknadProps>({
    state: initialState,
    dispatch: () => {},
});

const useSoknadContext = () => useContext(SoknadContext);

const SoknadProvider: FC = ({ children }) => {
    const [state, dispatch] = useReducer(reducer, initialState);

    localStorage.setItem(STORAGE_KEY, JSON.stringify(state));

    return <SoknadContext.Provider value={{ state, dispatch }}>{children}</SoknadContext.Provider>;
};

export { useSoknadContext, SoknadProvider };
