import { useEffect, useState } from "react";
import "./SoknadForside.less";
import { Panel } from "nav-frontend-paneler";
import { BekreftCheckboksPanel } from "nav-frontend-skjema";
import Lenke from "nav-frontend-lenker";
import { Normaltekst, Sidetittel, Undertittel } from "nav-frontend-typografi";
import { Hovedknapp } from "nav-frontend-knapper";
import Veileder from "nav-frontend-veileder";
import { useHistory } from "react-router-dom";
import ikon from "../../assets/ikoner/veileder.svg";
import { useTranslation } from "react-i18next";
import { useStegContext } from "../../context/steg/StegContext";
import { useBrukerContext } from "../../context/bruker/BrukerContext";
import { hentInnloggetPerson } from "../../api";
import { ActionTypes, IBruker } from "../../context/bruker/bruker";

const SoknadForside = () => {
    const { t } = useTranslation();
    const { state } = useStegContext();
    const history = useHistory();

    const brukerState = useBrukerContext().state;
    const dispatch = useBrukerContext().dispatch;

    useEffect(() => {
        if (!brukerState.foedselsnummer) {
            hentInnloggetPerson()
                .then((person: IBruker) => {
                    dispatch({ type: ActionTypes.HENT_INNLOGGET_BRUKER, payload: person });
                })
                .catch(() => {
                    if (process.env.NODE_ENV === "development") {
                        dispatch({ type: ActionTypes.INIT_TEST_BRUKER });
                    }
                });
        }
    }, [brukerState.foedselsnummer, dispatch]);

    const innloggetBrukerNavn = `${brukerState.fornavn} ${brukerState.etternavn}`;

    if (state.aktivtSteg !== 1) {
        history.push(`/soknad/steg/${state.aktivtSteg}`);
    }

    const [harBekreftet, settBekreftet] = useState(false);

    return (
        <>
            <Panel className={"forside"}>
                <Veileder tekst={`${t("forside.hei")}, ${innloggetBrukerNavn}`} posisjon="høyre">
                    <img alt="veileder" src={ikon} />
                </Veileder>

                <br />

                <section>
                    <Sidetittel>{t("forside.tittel")}</Sidetittel>

                    <Normaltekst>
                        <p>{t("forside.omYtelsene")}</p>

                        <Lenke href={"#"}>{t("forside.lenkeTilInfoOmYtelsene")}</Lenke>
                    </Normaltekst>
                </section>

                <section>
                    <Undertittel>{t("forside.riktigeOpplysninger.tittel")}</Undertittel>

                    <Normaltekst>
                        <p>{t("forside.riktigeOpplysninger.intro")}</p>

                        <p>{t("forside.riktigeOpplysninger.endringerMaaMeldesIfra")}</p>
                    </Normaltekst>
                </section>

                <section>
                    <Undertittel>{t("forside.dokumentasjon.tittel")}</Undertittel>

                    <Normaltekst>
                        <p>{t("forside.dokumentasjon.duFaarBeskjed")}</p>

                        <p>{t("forside.dokumentasjon.duFaarBeskjed2")}</p>

                        <Lenke href={"#"}>{t("forside.dokumentasjon.lenkeTilInformasjon")}</Lenke>
                    </Normaltekst>
                </section>

                <section>
                    <Undertittel>{t("forside.slikSoekerDu.tittel")}</Undertittel>

                    <Normaltekst>
                        <p>{t("forside.slikSoekerDu.kunRelevantInfo")}</p>

                        <p>{t("forside.slikSoekerDu.viLagrer")}</p>

                        <p>{t("forside.slikSoekerDu.dokumentasjonKanEttersendes")}</p>
                    </Normaltekst>
                </section>

                <section>
                    <Undertittel>{t("forside.samtykke.tittel")}</Undertittel>

                    <BekreftCheckboksPanel
                        label={t("forside.samtykke.bekreftelse")}
                        checked={harBekreftet}
                        onChange={(e) => settBekreftet((e.target as HTMLInputElement).checked)}
                    >
                        <p>{t("forside.samtykke.beskrivelse")}</p>

                        <Lenke href="#">{t("forside.samtykke.lesMer")}</Lenke>
                    </BekreftCheckboksPanel>
                </section>

                {harBekreftet && (
                    <Hovedknapp onClick={() => history.push(`/soknad/steg/1`)}>{t("forside.startSoeknad")}</Hovedknapp>
                )}
            </Panel>
        </>
    );
};

export default SoknadForside;
