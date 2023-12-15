rootProject.name = "pensjon-etterlatte-felles"

plugins {
    kotlin("jvm") version "1.9.21" apply false
}

include(
    "apps:etterlatte-proxy",
    "apps:etterlatte-notifikasjoner"
)
