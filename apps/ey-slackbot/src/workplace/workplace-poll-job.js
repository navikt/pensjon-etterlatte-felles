const CronJob = require('cron').CronJob
const initWorkplaceBlocks = require("./workplace-blocks")

const TIMEZONE = 'Europe/Oslo'

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

        try {
            const result = await app.client.chat.postMessage({
                // channel: 'etterlatte-slackbot-test', // Test channel
                channel: 'team-etterlatte-intern',
                blocks: initWorkplaceBlocks(title),
                text: 'Should display blocks containing buttons to select workplace'
            })

            if (result.ok) {
                console.log('Message sent OK')
            } else {
                console.error(`Error on postMessage: ${result.error}`)
            }
        } catch (e) {
            console.error(e)
        }
    };

    // const time = '0 */5 10 * * 1-5' // Test cron
    const time = '0 35 11 * * 1-5'

    console.log(`Init cronjob with crontime: ${time}`)

    const job = new CronJob(time, onTick, null, false, TIMEZONE)

    job.start()
}

module.exports = setupJob
