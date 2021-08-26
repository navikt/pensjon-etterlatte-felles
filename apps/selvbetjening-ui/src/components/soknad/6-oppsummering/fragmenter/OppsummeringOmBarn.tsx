import { SkjemaGruppe } from "nav-frontend-skjema";
import TekstGruppe from "./TekstGruppe";
import { v4 as uuid } from "uuid";
import Lenke from "nav-frontend-lenker";
import { EditFilled } from "@navikt/ds-icons";
import Ekspanderbartpanel from "nav-frontend-ekspanderbartpanel";
import React from "react";
import { useTranslation } from "react-i18next";
import { StegPath } from "../../../../context/steg/steg";
import { IBarn, IOmBarn } from "../../../../typer/person";
import ObjectTreeReader from "../../../../utils/ObjectTreeReader";
import { Panel } from "@navikt/ds-react";

const OppsummeringOmBarn = ({
    opplysningerOmBarn,
    senderSoeknad,
}: {
    opplysningerOmBarn: IOmBarn;
    senderSoeknad: boolean;
}) => {
    const { t, i18n } = useTranslation();

    const otr = new ObjectTreeReader(i18n);

    const getBaseKey = (string: string) => {
        return string.replace(/(.\d+)/g, "");
    };

    return (
        <Ekspanderbartpanel tittel={t("omBarn.tittel")} className={"oppsummering"} apen={true}>
            {opplysningerOmBarn.barn?.map((barn, i: number) => {
                const tekster: any[] = otr.traverse<IBarn>(barn, "omBarn");

                return (
                    <SkjemaGruppe key={`${barn.foedselsnummer}_${i}`} legend={`${barn.fornavn} ${barn.etternavn}`}>
                        <Panel border>
                            {tekster.map(({ key, val }) => (
                                <TekstGruppe key={uuid()} tittel={t(getBaseKey(key))} innhold={t(val)} id={key}/>
                            ))}
                        </Panel>
                    </SkjemaGruppe>
                );
            })}

            <TekstGruppe
                id={"omBarn.gravidEllerNyligFoedt"}
                tittel={t("omBarn.gravidEllerNyligFoedt")}
                innhold={t(opplysningerOmBarn.gravidEllerNyligFoedt!)}
            />

            <Lenke href={`/soknad/steg/${StegPath.OmBarn}`} className={senderSoeknad ? "disabled" : ""}>
                <EditFilled />
                <span>{t("felles.endreSvar")}</span>
            </Lenke>
        </Ekspanderbartpanel>
    );
};

export default OppsummeringOmBarn;
