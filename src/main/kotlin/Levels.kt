import pt.isel.canvas.*

const val SKIN_COLOR = 0xffb4a9

val level1 = listOf(
    0,   YELLOW, YELLOW, YELLOW,    0,   WHITE, GOLDEN, WHITE,        0,    YELLOW, YELLOW, YELLOW,     0,
    0,   MAGENTA, MAGENTA, MAGENTA, 0,   ORANGE, ORANGE, ORANGE,      0,    MAGENTA, MAGENTA, MAGENTA,  0,
    0,   BLUE, BLUE, BLUE,          0,   CYAN, CYAN, CYAN,            0,    BLUE, BLUE, BLUE,           0,
    0,   RED, RED, RED,             0,   GREEN, GREEN, GREEN,         0,    RED, RED, RED,              0,
    0,   GREEN, GREEN, GREEN,       0,   RED, RED, RED,               0,    GREEN, GREEN, GREEN,        0,
    0,   CYAN, CYAN, CYAN,          0,   BLUE, BLUE, BLUE,            0,    CYAN, CYAN, CYAN,           0,
    0,   ORANGE, ORANGE, ORANGE,    0,   MAGENTA, MAGENTA, MAGENTA,   0,    ORANGE, ORANGE, ORANGE,     0,
    0,   WHITE, WHITE, WHITE,       0,   SILVER, SILVER, SILVER,      0,    WHITE, WHITE, WHITE,        0,
)

val level2 = listOf(
    0,0,0,0,0,                           YELLOW, YELLOW, SKIN_COLOR,                            0,0,0,0,0,
    0,0,0,0,                        YELLOW, YELLOW, SKIN_COLOR, SKIN_COLOR, BLUE,                 0,0,0,0,
    0,0,0,                     YELLOW, YELLOW, SKIN_COLOR, SKIN_COLOR, BLUE, BLUE, RED,             0,0,0,
    0,0,0,                     YELLOW, SKIN_COLOR, SKIN_COLOR, BLUE, BLUE, RED, RED,                0,0,0,
    0,0,                    YELLOW, SKIN_COLOR, SKIN_COLOR, BLUE, BLUE, RED, RED, GREEN, GREEN,       0,0,
    0,0,                    SKIN_COLOR, SKIN_COLOR, BLUE, BLUE, RED, RED, GREEN, GREEN, CYAN,         0,0,
    0,0,                    SKIN_COLOR, BLUE, BLUE, GOLDEN, GOLDEN, GOLDEN, GREEN, CYAN, CYAN,        0,0,
    0,0,                    BLUE, BLUE, RED, RED, GREEN, GREEN, CYAN, CYAN, ORANGE,                   0,0,
    0,0,                    BLUE, RED, RED, GREEN, GREEN, CYAN, CYAN, ORANGE, ORANGE,                 0,0,
    0,0,                    RED, RED, GREEN, GREEN, CYAN, CYAN, ORANGE, ORANGE, WHITE,                0,0,
    0,0,0,                     GREEN, GREEN, CYAN, CYAN, ORANGE, ORANGE, WHITE,                     0,0,0,
    0,0,0,                     GREEN, CYAN, CYAN, ORANGE, ORANGE, WHITE, WHITE,                     0,0,0,
    0,0,0,0,                        CYAN, ORANGE, ORANGE, WHITE, WHITE,                           0,0,0,0,
    0,0,0,0,0,                           ORANGE, WHITE, WHITE,                                  0,0,0,0,0,
)

fun createBlocks(colors : List<Int>) : List<Block> {
    var x = 0
    var y = 3 * BLOCK_HEIGHT
    val blocks = mutableListOf<Block>()

    colors.forEach{
        if (x >= 13 * BLOCK_WIDTH){
            x = 0
            y += BLOCK_HEIGHT
        }
        if (it!=0)
            blocks += Block(Position(x,y),BLOCK_WIDTH,BLOCK_HEIGHT,it,if (it==SILVER) 2 else 1)

        x += BLOCK_WIDTH
    }
    return blocks
}