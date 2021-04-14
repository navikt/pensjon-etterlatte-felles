import { FC, useEffect } from "react";
import { Route, useRouteMatch } from "react-router";
import Stegindikator from "nav-frontend-stegindikator/lib/stegindikator";
import { useStegContext } from "../../context/steg/StegContext";
import { useHistory } from "react-router-dom";
import { StegActionTypes } from "../../context/steg/steg";
import { Hovedknapp, Knapp } from "nav-frontend-knapper";
import { Panel } from "nav-frontend-paneler";
import { sendSoeknad } from "../../api";
import { useSoknadContext } from "../../context/soknad/SoknadContext";

const SoknadDialog: FC = () => {
    const history = useHistory();
    const { path } = useRouteMatch();

    const { state, dispatch } = useStegContext();

    const { aktivtSteg, steg } = state;

    useEffect(() => {
        history.push(`/soknad/steg/${aktivtSteg}`);
    }, [history, aktivtSteg]);

    // const settSteg = () => dispatch({type: StegActionTypes.NESTE })
    const forrige = () => dispatch({ type: StegActionTypes.FORRIGE });
    const neste = () => {
        if (aktivtSteg !== steg.length) {
            dispatch({ type: StegActionTypes.NESTE });
        } else {
            // TODO: Side for "ferdig søknad"
        }
    };

    const soeknad = useSoknadContext().state;
    const send = () => sendSoeknad(soeknad).then((r) => console.log(r));

    const alleSteg = steg.map(({ disabled }, index) => {
        return {
            index,
            label: `${index}`, // TODO: Legge til label
            disabled,
        };
    });

    return (
        <>
            {aktivtSteg && (
                <Stegindikator
                    aktivtSteg={aktivtSteg - 1}
                    steg={alleSteg}
                    onChange={(index) => {
                        history.push(`${path}/steg/${index + 1}`);
                    }}
                />
            )}

            <div className={"app"}>
                {state.steg.map((steg, index) => {
                    const stegNr = index + 1;

                    return (
                        <Route key={index} path={`${path}/steg/${stegNr}`}>
                            <Panel>
                                <steg.component />
                            </Panel>
                        </Route>
                    );
                })}

                <section className={"navigasjon-rad"}>
                    {aktivtSteg > 1 && <Knapp onClick={forrige}>Tilbake</Knapp>}

                    {aktivtSteg < steg.length && <Hovedknapp onClick={neste}>Neste</Hovedknapp>}

                    {aktivtSteg === steg.length && <Hovedknapp onClick={send}>Send søknad</Hovedknapp>}
                </section>
            </div>
        </>
    );
};

export default SoknadDialog;
