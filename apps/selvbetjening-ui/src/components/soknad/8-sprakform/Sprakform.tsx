import { FC } from "react";
import "../../../App.less";
import { Panel } from "nav-frontend-paneler";
import { RadioPanelGruppe } from "nav-frontend-skjema";
import { Systemtittel } from "nav-frontend-typografi";
import { Hovedknapp, Knapp } from "nav-frontend-knapper";
import { useHistory } from "react-router-dom";
import { SoknadActionTypes, useSoknadContext } from "../../../context/SoknadContext";

interface Props {
    forrigeSteg?: number;
    nesteSteg?: number;
}

const Sprakform: FC<Props> = ({ forrigeSteg, nesteSteg }) => {
    const history = useHistory();

    // @ts-ignore
    const { state, dispatch } = useSoknadContext();

    function setLanguage(e: any) {
        const target = e.target as HTMLInputElement;

        dispatch({ type: SoknadActionTypes.SET_SPRAK, payload: target.value });
    }

    return (
        <div className="app">
            {/* Steg 8 */}
            <Panel>
                <Systemtittel>8 Språkform</Systemtittel>

                <RadioPanelGruppe
                    name={"language"}
                    legend={"Hvilken språkform ønsker du i svaret?"}
                    radios={[
                        { label: "Bokmål", value: "Bokmål" },
                        { label: "Nynorsk (dette er feil svar)", value: "Nynorsk" },
                    ]}
                    checked={state.sprak}
                    onChange={setLanguage}
                />
            </Panel>

            <Panel>
                {forrigeSteg && <Knapp onClick={() => history.push(`/soknad/steg/${forrigeSteg}`)}>Tilbake</Knapp>}
                {nesteSteg && <Hovedknapp onClick={() => history.push(`/soknad/steg/${nesteSteg}`)}>Neste</Hovedknapp>}
            </Panel>
        </div>
    );
};

export default Sprakform;
