const { Issuer, TokenSet } = require("openid-client");
const logger = require("../log/logger");
const config = require("../config");

const idportenConfig = config.idporten;

class IDportenClient {
    #client = null;
    #issuer = null;

    constructor() {
        logger.info("Setter opp ID-porten");

        this.#init().then((client) => {
            this.#client = client;
        }).catch(() => process.exit(1));
    }

    authUrl = (session) => {
        return this.#client.authorizationUrl({
            scope: idportenConfig.scope,
            redirect_uri: idportenConfig.redirectUri,
            response_type: idportenConfig.responseType[0],
            response_mode: "query",
            nonce: session.nonce,
            state: session.state,
            resource: "https://nav.no",
            acr_values: "Level4",
        });
    };

    endSessionUrl = (idToken, postLogoutRedirectUri) => {
        return this.#client.endSessionUrl({
            id_token_hint: idToken,
            post_logout_redirect_uri: postLogoutRedirectUri
        });
    };

    validateOidcCallback = async (req) => {
        const params = this.#client.callbackParams(req);

        const { nonce, state } = req.session;

        const additionalClaims = {
            clientAssertionPayload: {
                aud: this.#issuer,
            },
        };

        return this.#client
                .callback(idportenConfig.redirectUri, params, { nonce, state }, additionalClaims)
                .catch((err) => Promise.reject(`error in oidc callback: ${err}`))
                .then(async (tokenSet) => {
                    return tokenSet;
                });
    };

    refresh = (oldTokenSet) => {
        const additionalClaims = {
            clientAssertionPayload: {
                aud: this.#issuer,
            },
        };
        return this.#client.refresh(new TokenSet(oldTokenSet), additionalClaims);
    };

    #init = async () => {
        const idporten = await Issuer.discover(idportenConfig.discoveryUrl);
        this.#issuer = idporten.issuer;

        logger.info(`Discovered IDPorten @ ${this.#issuer}`);

        try {
            const idportenJwk = JSON.parse(idportenConfig.clientJwk);

            const client = new idporten.Client(
                    {
                        client_id: idportenConfig.clientID,
                        token_endpoint_auth_method: "private_key_jwt",
                        token_endpoint_auth_signing_alg: "RS256",
                        redirect_uris: [idportenConfig.redirectUri],
                        response_types: ["code"],
                    },
                    {
                        keys: [idportenJwk],
                    }
            );

            logger.info("Opprettet klient for ID-porten");

            return Promise.resolve(client);
        } catch (err) {
            logger.error("Feil oppsto under opprettelsen av klient for ID-porten", err);
            return Promise.reject(err);
        }
    };
}

module.exports = IDportenClient;
