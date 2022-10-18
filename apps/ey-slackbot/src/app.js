const {App} = require("@slack/bolt");
const setupWorkplaceJob = require('./workplace/workplace-poll-job')
const setupWorkplaceAction = require("./workplace/select-workplace-action");
const setupEventListener = require("./events");

const PORT = process.env.PORT || 3000

const app = new App({
    token: process.env.SLACK_BOT_TOKEN,
    signingSecret: process.env.SLACK_SIGNING_SECRET,
    port: PORT,
    customRoutes: [
        {
            path: '/internal/is_alive',
            method: ['GET'],
            handler: (req, res) => {
                res.writeHead(200)
                res.end('OK')
            }
        }
    ]
});

(async () => {
    await app.start(PORT);

    setupWorkplaceJob(app)

    console.log(`⚡️ Bolt app is running on port ${PORT}!`);
})();

setupEventListener(app)

setupWorkplaceAction(app)
