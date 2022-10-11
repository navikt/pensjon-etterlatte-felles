rootProject.name = "pensjon-etterlatte-felles"

include(
    "apps:etterlatte-proxy",
    "apps:etterlatte-notifikasjoner",
    "jobs:post-til-kafka"
)
