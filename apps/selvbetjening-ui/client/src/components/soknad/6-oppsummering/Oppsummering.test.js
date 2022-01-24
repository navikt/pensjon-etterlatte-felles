import { render, fireEvent } from "@testing-library/react";
import * as JSutils from "nav-frontend-js-utils";
import { AccordionItem } from "./AccordionItem";
import TekstGruppe from "./fragmenter/TekstGruppe";
import Oppsummering from "./Oppsummering";

jest.mock("react-i18next", () => ({
    useTranslation: () => ({
        t: jest.fn((key) => key),
        i18n: {
            changeLanguage: () => new Promise(() => {}),
        },
    })
}));

JSutils.guid = jest.fn(() => "123");
describe("Oppsummering", () => {
    it("Snapshot", () => {
        const { container } = render(<Oppsummering />);
        expect(container).toMatchSnapshot();
    });
});

describe("Test accordionItem", () => {
    it("skal endre aria-expanded", () => {
        const { container, getByText } = render(
            <AccordionItem tittel="Testtittel" defaultOpen={false}>
                Innhold
            </AccordionItem>
        );
        expect(container.querySelectorAll("[aria-expanded]")[0].getAttribute("aria-expanded")).toBe("false");
        fireEvent.click(getByText("Testtittel"));
        expect(container.querySelectorAll("[aria-expanded]")[0].getAttribute("aria-expanded")).toBe("true");
    });
    it("skal rendre innholdet synlig", () => {
        const { getByText } = render(
            <AccordionItem tittel="Testtittel" defaultOpen={true}>
                Innhold
            </AccordionItem>
        );
        expect(getByText("Innhold")).toBeDefined()
    });
});

describe("Tekstgruppe", () => {
    it("Skal rendre testittel og testcontent", () => {
        const { getByText } = render(<TekstGruppe tittel="Testtittel" innhold={"Testcontent"} />);
        expect(getByText("Testtittel")).toBeDefined();
        expect(getByText("Testcontent")).toBeDefined();
    })
})