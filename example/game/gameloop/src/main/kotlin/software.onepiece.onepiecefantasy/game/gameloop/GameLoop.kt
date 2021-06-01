package software.onepiece.onepiecefantasy.game.gameloop

import software.onepiece.onepiecefantasy.characters.gamelogic.CharacterController
import software.onepiece.onepiecefantasy.characters.gamelogic.Ground
import software.onepiece.onepiecefantasy.game.state.GameState

object GameLoop {

    fun proceed() {
        if (GameState.keyUp) {
            CharacterController.move(GameState.character, WorldAsGround, CharacterController.Direction.Up)
        }
        if (GameState.keyLeft) {
            CharacterController.move(GameState.character, WorldAsGround, CharacterController.Direction.Left)
        }
        if (GameState.keyDown) {
            CharacterController.move(GameState.character, WorldAsGround, CharacterController.Direction.Down)
        }
        if (GameState.keyRight) {
            CharacterController.move(GameState.character, WorldAsGround, CharacterController.Direction.Right)
        }
    }

    object WorldAsGround : Ground {
        override fun freeSpot(x: Int, y: Int) =
            y < GameState.world.height - 1 && y > 0 && x > 0 && x < GameState.world.width - 1
    }
}