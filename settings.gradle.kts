rootProject.name = "pensjon-etterlatte-felles"

include(
    "apps:etterlatte-proxy",
    "apps:etterlatte-notifikasjoner",
    "apps:etterlatte-pdl-proxy",
    "libs:common",
    "libs:common-test",
    "libs:ktorclient-auth-clientcredentials",
    "jobs:post-til-kafka"
)
