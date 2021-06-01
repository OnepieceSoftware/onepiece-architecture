package software.onepiece.onepiecefantasy.canvas.interfaces

data class Canvas(val entries: MutableMap<Position, String>, val with: Int, val height: Int)