package chronos.engine.implementation.apis.swapzone

import chronos.engine.implementation.models.swapzone.NetworkId
import chronos.engine.implementation.models.swapzone.NetworkObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/swapzone/networks")
class SwapzoneController @Autowired constructor(
    private val service: SwapzoneService
) {
    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<NetworkObject> = service.findById(NetworkId(id))?.let {
        ResponseEntity.ok(it)
    } ?: ResponseEntity.notFound().build()

    @GetMapping("/all")
    suspend fun getAll(): ResponseEntity<List<NetworkObject>> {
        val list = service.getAll()
        return if (list.isEmpty()) ResponseEntity.notFound().build()
        else ResponseEntity.ok(list)
    }
}