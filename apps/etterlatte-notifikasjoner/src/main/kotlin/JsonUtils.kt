package no.nav.etterlatte

import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.cfg.DateTimeFeature
import tools.jackson.databind.cfg.EnumFeature
import tools.jackson.module.kotlin.jacksonMapperBuilder

val mapper: ObjectMapper =
    jacksonMapperBuilder()
        .enable(EnumFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
        .build()