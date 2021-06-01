package software.onepiece.onepiecefantasy.characters.gamelogic

import software.onepiece.onepiecefantasy.characters.model.Character

object CharacterController {

    enum class Direction {
        Right, Left, Up, Down,
    }

    fun move(character: Character, ground: Ground, dir: Direction) {
        when(dir) {
            Direction.Right -> if (ground.freeSpot(character.position.x + 1, character.position.y)) character.position.x++
            Direction.Left -> if (ground.freeSpot(character.position.x - 1, character.position.y)) character.position.x--
            Direction.Up -> if (ground.freeSpot(character.position.x, character.position.y - 1)) character.position.y--
            Direction.Down -> if (ground.freeSpot(character.position.x, character.position.y + 1)) character.position.y++
        }
    }
}