
const setupEventListener = (app) => {
    app.event('app_home_opened', async ({ event, client, context }) => {
        try {
            const result = await client.views.publish({
                user_id: event.user,
                view: {
                    type: 'home',
                    callback_id: 'home_view',
                    blocks: [
                        {
                            "type": "image",
                            "image_url": "https://i.imgur.com/olkTFgz.jpeg",
                            "alt_text": "inspiration"
                        }
                    ]
                }
            })

            if (result.ok) {
                console.log(`Home page published successfully to user ${event.user}`)
            } else {
                console.error('Error when trying to publish app home view')
            }
        } catch (e) {
            console.error(e)
        }
    })
}

module.exports = setupEventListener
