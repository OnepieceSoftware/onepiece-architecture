package software.onepiece.onepiecefantasy.game.graphicsengine

import software.onepiece.onepiecefantasy.game.state.GameState
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

class GameKeyListener : KeyAdapter() {

    override fun keyPressed(e: KeyEvent) {
        when (e.keyChar) {
            'w' -> GameState.keyUp = true
            'a' -> GameState.keyLeft = true
            's' -> GameState.keyDown = true
            'd' -> GameState.keyRight = true
        }
    }

    override fun keyReleased(e: KeyEvent) {
        when (e.keyChar) {
            'w' -> GameState.keyUp = false
            'a' -> GameState.keyLeft = false
            's' -> GameState.keyDown = false
            'd' -> GameState.keyRight = false
        }
    }
}
