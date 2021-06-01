package software.onepiece.onepiecefantasy.game.main

import software.onepiece.onepiecefantasy.game.gameloop.GameLoop
import software.onepiece.onepiecefantasy.game.graphicsengine.Graphics
import kotlin.concurrent.timer

fun main() {
    Graphics.draw()
    timer("Game Loop", true, 0, 100) {
        GameLoop.proceed()
        Graphics.draw()
    }
}