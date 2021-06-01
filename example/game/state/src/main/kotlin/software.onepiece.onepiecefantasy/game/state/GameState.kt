package software.onepiece.onepiecefantasy.game.state

import software.onepiece.onepiecefantasy.characters.model.Character
import software.onepiece.onepiecefantasy.characters.model.Position
import software.onepiece.onepiecefantasy.world.model.World

object GameState {
    val character = Character("Judy", "❤", Position(4, 4))
    val world = World(54, 18)

    var keyUp = false
    var keyLeft = false
    var keyDown = false
    var keyRight = false
}