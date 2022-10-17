rootProject.name = "pensjon-etterlatte-felles"

plugins {
    kotlin("jvm") version "1.7.20" apply false
}

include(
    "apps:etterlatte-proxy",
    "apps:etterlatte-notifikasjoner"
)
