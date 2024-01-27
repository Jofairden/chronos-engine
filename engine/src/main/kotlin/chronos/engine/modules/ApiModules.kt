package chronos.engine.modules

import org.koin.dsl.module

val apiModules = module {
  includes(
    coincodexModule,
    coincodexHttpModule,
    swapzoneModule,
    swapzoneHttpModule,
    mobulaModule,
    mobulaHttpModule,
  )
}
