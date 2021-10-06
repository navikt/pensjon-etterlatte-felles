import { SkjemaGruppe } from "nav-frontend-skjema";
import React, { useState } from "react";
import { useTranslation } from "react-i18next";
import classNames from "classnames";
import { useSoknadContext } from "../../context/soknad/SoknadContext";
import { useBrukerContext } from "../../context/bruker/BrukerContext";
import { ActionTypes as BrukerAction } from "../../context/bruker/bruker";
import { ActionTypes as SoknadAction } from "../../context/soknad/soknad";
import { erDato } from "../../utils/dato";
import { BodyShort, Button, Loader, Modal, Heading } from "@navikt/ds-react";

if (process.env.NODE_ENV !== "test") Modal.setAppElement!!("#root");

interface KnappProps {
    label?: string;
    onClick?: () => void;
}

const Navigasjon = ({
    neste,
    forrige,
    send,
    disabled,
}: {
    neste?: KnappProps;
    forrige?: KnappProps;
    send?: KnappProps;
    disabled?: boolean;
}) => {
    const { t, i18n } = useTranslation();
    const {
        state: { sistLagretDato },
        dispatch: soknadDispatch,
    } = useSoknadContext();

    const { dispatch: brukerDispatch } = useBrukerContext();

    const dtf = Intl.DateTimeFormat(i18n.language, {
        day: "numeric",
        month: "long",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit",
    });

    let sistLagret;
    if (!!sistLagretDato && erDato(sistLagretDato)) {
        sistLagret = dtf.format(new Date(sistLagretDato));
    }

    const [isOpen, setIsOpen] = useState(false);

    const avbryt = () => {
        soknadDispatch({ type: SoknadAction.TILBAKESTILL });
        brukerDispatch({ type: BrukerAction.TILBAKESTILL });

        window.location.href = "https://www.nav.no";
    };

    return (
        <>
            <SkjemaGruppe className="navigasjon-footer">
                <div className={classNames("navigasjon-rad", disabled && "disabled")}>
                    {!!forrige && (
                        <Button variant={"primary"} type={"button"} onClick={forrige.onClick}>
                            {forrige.label || t("knapp.tilbake")}
                        </Button>
                    )}

                    {!!neste && (
                        <Button variant={"primary"} type={"button"} onClick={neste.onClick}>
                            {neste.label || t("knapp.neste")}
                        </Button>
                    )}

                    {!!send && (
                        <Button variant={"primary"} type={"button"} onClick={send.onClick}>
                            {send.label || t("knapp.sendSoeknad")} {disabled && <Loader />}
                        </Button>
                    )}
                </div>

                {!!sistLagret && (
                    <BodyShort size={"small"} spacing className={"center mute"}>
                        {t("felles.sistLagret")}: {sistLagret}
                    </BodyShort>
                )}
            </SkjemaGruppe>

            <SkjemaGruppe className={classNames("navigasjon-rad", disabled && "disabled")}>
                <Button variant={"secondary"} type={"button"} onClick={() => setIsOpen(true)}>
                    {t("knapp.avbryt")}
                </Button>
            </SkjemaGruppe>

            <Modal open={isOpen} onClose={() => setIsOpen(false)} className="spoersmaal-modal modal">
                <SkjemaGruppe>
                    <Heading size={"medium"}>{t("avbrytModal.spoersmaal")}</Heading>
                </SkjemaGruppe>

                <SkjemaGruppe>
                    <Button variant={"primary"} type={"button"} onClick={() => setIsOpen(false)}>
                        {t("avbrytModal.svarNei")}
                    </Button>
                </SkjemaGruppe>

                <SkjemaGruppe>
                    <Button variant={"primary"} type={"button"} onClick={avbryt}>
                        {t("avbrytModal.svarJa")}
                    </Button>
                </SkjemaGruppe>
            </Modal>
        </>
    );
};

export default Navigasjon;
