package chronos.engine.infrastructure.modules

import org.koin.dsl.module

val apiModules =
  module {
    includes(
      coincodexModule,
      swapzoneModule,
      mobulaModule,
    )
  }
