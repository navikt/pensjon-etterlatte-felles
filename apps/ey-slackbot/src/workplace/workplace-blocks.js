const blocks = [
    {
        "type": "divider"
    },
    {
        "type": "section",
        "text": {
            "type": "mrkdwn",
            "text": ":zoom-icon: Hjemme / Zoom"
        },
        "accessory": {
            "type": "button",
            "text": {
                "type": "plain_text",
                "text": ":zoom-icon:",
                "emoji": true
            },
            "value": "home",
            "action_id": "button_select_workplace"
        }
    },
    {
        "type": "section",
        "text": {
            "type": "mrkdwn",
            "text": ":office: Kontoret (FYA1)"
        },
        "accessory": {
            "type": "button",
            "text": {
                "type": "plain_text",
                "text": ":office:",
                "emoji": true
            },
            "value": "office",
            "action_id": "button_select_workplace"
        }
    },
    {
        "type": "section",
        "text": {
            "type": "mrkdwn",
            "text": ":desert_island: Ferie / fri"
        },
        "accessory": {
            "type": "button",
            "text": {
                "type": "plain_text",
                "text": ":desert_island:",
                "emoji": true
            },
            "value": "vacation",
            "action_id": "button_select_workplace"
        }
    },
    {
        "type": "section",
        "text": {
            "type": "mrkdwn",
            "text": ":face_with_thermometer: Syk"
        },
        "accessory": {
            "type": "button",
            "text": {
                "type": "plain_text",
                "text": ":face_with_thermometer:",
                "emoji": true
            },
            "value": "sick",
            "action_id": "button_select_workplace"
        }
    },
    {
        "type": "section",
        "text": {
            "type": "mrkdwn",
            "text": ":shrug: Vet ikke ..."
        },
        "accessory": {
            "type": "button",
            "text": {
                "type": "plain_text",
                "text": ":shrug:",
                "emoji": true
            },
            "value": "dontknow",
            "action_id": "button_select_workplace"
        }
    }
]

const initWorkplaceBlocks = (title) => {
    const heading = {
        "type": "section",
        "text": {
            "type": "mrkdwn",
            "text": title
        }
    }

    return [heading, ...blocks]
}

module.exports = initWorkplaceBlocks
