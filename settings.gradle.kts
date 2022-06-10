rootProject.name = "pensjon-etterlatte-felles"

include(
    "apps:etterlatte-proxy",
    "apps:etterlatte-notifikasjoner",
    "apps:etterlatte-pdl-proxy",
    "jobs:post-til-kafka"
)
