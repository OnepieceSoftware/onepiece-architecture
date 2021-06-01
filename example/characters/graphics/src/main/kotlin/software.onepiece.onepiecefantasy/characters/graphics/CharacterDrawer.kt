package software.onepiece.onepiecefantasy.characters.graphics

import software.onepiece.onepiecefantasy.canvas.interfaces.Canvas
import software.onepiece.onepiecefantasy.canvas.interfaces.Position
import software.onepiece.onepiecefantasy.characters.model.Character

object CharacterDrawer {

    fun draw(character: Character, canvas: Canvas) {
        canvas.entries[Position(character.position.x, character.position.y)] = character.look
    }
}