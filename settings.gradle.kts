rootProject.name = "pensjon-etterlatte-felles"

plugins {
    kotlin("jvm") version "1.8.21" apply false
}

include(
    "apps:etterlatte-proxy",
    "apps:etterlatte-notifikasjoner"
)
