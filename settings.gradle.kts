rootProject.name = "pensjon-etterlatte-felles"

plugins {
    kotlin("jvm") version "2.0.20" apply false
}

include(
    "apps:etterlatte-proxy",
    "apps:etterlatte-notifikasjoner"
)
