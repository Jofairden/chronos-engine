package chronos.engine.implementation.api.swapzone.resouce

import io.ktor.resources.Resource

@Resource("/networks")
class SwapzoneNetworksResource {
  @Resource("{id}")
  class Id(val parent: SwapzoneNetworksResource = SwapzoneNetworksResource(), val id: Long)
}
