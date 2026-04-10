const CronJob = require('cron').CronJob
const initWorkplaceBlocks = require("./workplace-blocks")

const TIMEZONE = 'Europe/Oslo'
const SLACK_CHANNELS = ['team-efterlatte-privat']

const now = () => {
    return new Date()
        .toLocaleTimeString('no-NO', { timeZone: TIMEZONE })
}

const setupJob = (app) => {
    const onTick = async () => {
        console.log(`Running job @ ${now()}`)

        let title;
        if (new Date().getDay() === 5) {
            title = "Endelig helg! :star-struck: Hvor skal du jobbe på mandag?"
        } else {
            title = "Hvor skal du jobbe i morgen?"
        }

        for (const channel of SLACK_CHANNELS) {
            try {
                const result = await app.client.chat.postMessage({
                    channel: channel,
                    blocks: initWorkplaceBlocks(title),
                    text: 'Should display blocks containing buttons to select workplace'
                })
                if (result.ok) {
                    console.log(`Message sent OK to #${channel}`)
                } else {
                    console.error(`Error on postMessage to channel ${channel}: ${result.error}`)
                }
            } catch (e) {
                console.error(e)
            }
        }
    };

    // const time = '0 */5 10 * * 1-5' // Test cron
    const time = '00 55 13 * * 1-5' // kl 13:55:00, man-fre, alle uker, alle måneder

    console.log(`Init cronjob with crontime: ${time}`)

    const job = new CronJob(time, onTick, null, false, TIMEZONE)

    job.start()
}

module.exports = setupJob
