package software.onepiece.onepiecefantasy.game.graphicsengine

import software.onepiece.onepiecefantasy.canvas.interfaces.Canvas
import software.onepiece.onepiecefantasy.canvas.interfaces.Position
import software.onepiece.onepiecefantasy.characters.graphics.CharacterDrawer
import software.onepiece.onepiecefantasy.game.state.GameState
import software.onepiece.onepiecefantasy.world.graphics.WorldDrawer
import java.awt.Font
import javax.swing.BorderFactory
import javax.swing.JFrame
import javax.swing.JTextArea
import javax.swing.WindowConstants


object Graphics {

    private val canvas = Canvas(mutableMapOf(), GameState.world.width, GameState.world.height)
    private val textArea: JTextArea

    init {
        val frame = JFrame().apply {
            setSize(400, 310)
            isResizable = false
            defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        }
        textArea = JTextArea().apply {
            isEditable = false
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
            font = Font("monospaced", Font.PLAIN, 12)
            addKeyListener(GameKeyListener())
        }
        frame.add(textArea)
        frame.isVisible = true
    }

    fun draw() {
        canvas.entries.clear()
        WorldDrawer.draw(GameState.world, canvas)
        CharacterDrawer.draw(GameState.character, canvas)
        val b = StringBuilder()
        (0 until canvas.height).forEach { y ->
            (0 until canvas.with).forEach { x ->
                b.append(canvas.entries.getOrElse(Position(x, y)) { " " })
            }
            b.append('\n')
        }
        textArea.text = b.toString()
    }
}