package software.onepiece.onepiecefantasy.world.graphics

import software.onepiece.onepiecefantasy.canvas.interfaces.Canvas
import software.onepiece.onepiecefantasy.canvas.interfaces.Position
import software.onepiece.onepiecefantasy.world.model.World

object WorldDrawer {

    fun draw(world: World, canvas: Canvas) {
        (0 until world.width).forEach { x ->
            canvas.entries[Position(x, 0)] = "X"
            canvas.entries[Position(x, world.height - 1)] = "X"
        }
        (0 until world.height).forEach { y ->
            canvas.entries[Position(0, y)] = "X"
            canvas.entries[Position(world.width - 1, y)] = "X"
        }
    }
}