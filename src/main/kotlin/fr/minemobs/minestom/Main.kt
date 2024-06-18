package fr.minemobs.minestom

import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Player
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.block.Block
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.DimensionType


fun main() {
    val server = MinecraftServer.init()
    initServer()
    server.start("0.0.0.0", 25565)
    println("Server started on 0.0.0.0:25565")
}

fun initServer() {
    val fullBrightType = DimensionType.builder().ambientLight(2.0f).build()
    val fullbright = MinecraftServer.getDimensionTypeRegistry().register(NamespaceID.from("sunderia:full_bright"), fullBrightType)
    val instanceManager = MinecraftServer.getInstanceManager()
    val instanceContainer = instanceManager.createInstanceContainer(fullbright)

    // Set the ChunkGenerator
    instanceContainer.setGenerator { it.modifier().fillHeight(0, 40, Block.STONE) }

    initEventHandlers(instanceContainer)
}

fun initEventHandlers(instanceContainer: InstanceContainer) {
    // Add an event callback to specify the spawning instance (and the spawn position)
    val globalEventHandler = MinecraftServer.getGlobalEventHandler()
    globalEventHandler.addListener(AsyncPlayerConfigurationEvent::class.java) { event ->
        val player: Player = event.player
        event.spawningInstance = instanceContainer
        player.respawnPoint = Pos(.0, 42.0, .0)
    }
}
