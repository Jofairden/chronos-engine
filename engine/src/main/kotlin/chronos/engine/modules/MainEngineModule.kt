package chronos.engine.modules

import org.koin.dsl.module

val engineModule =
  module(createdAtStart = true) {
    includes(
      httpClientModule,
      gsonModule,
      apiModules
    )
  }
