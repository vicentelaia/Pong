import pt.isel.canvas.*

const val ORANGE = 0xFFA500
const val GOLDEN = 0xDAA520
const val SILVER = 0xC0C0C0
const val BLOCK_WIDTH = 32
const val BLOCK_HEIGHT = 15

/**
 * Block information
 * [p], [width], [height], [color], [lives]
 * @property [p] block position
 * @property [width] block width
 * @property [height] block height
 * @property [color] block color
 * @property [lives] block lives
 */
data class Block(val p : Position, val width : Int, val height : Int, val color : Int, val lives : Int)

fun initialBlocks() : List<Block> = createBlocks(level1)

fun Block.draw(cv : Canvas){
    cv.drawRect(p.x,p.y,width,height,color)
    cv.drawRect(p.x+1,p.y+1,width-2,height-2, BLACK,1)
}

fun Block.reflect(b : Ball) = when {
    b.p.x in p.x - BALL_RADIUS..p.x && b.v.dx > 0 ||
    b.p.x in p.x + BLOCK_WIDTH..p.x + BLOCK_WIDTH + BALL_RADIUS && b.v.dx < 0 -> Ball(b.p,Velocity(-b.v.dx,b.v.dy))
    b.p.y in p.y - BALL_RADIUS..p.y && b.v.dy > 0 ||
    b.p.y in p.y + BLOCK_HEIGHT..p.y + BLOCK_HEIGHT + BALL_RADIUS && b.v.dy < 0 -> Ball(b.p,Velocity(b.v.dx,-b.v.dy))
    else -> Ball(b.p,b.v)
}

fun Block.points() : Int =
    when (color) {
        WHITE -> 1
        ORANGE -> 2
        CYAN -> 3
        GREEN -> 4
        RED -> 6
        BLUE -> 7
        MAGENTA -> 8
        YELLOW -> 9
        else -> 0
    }

fun Block.hit() : Block = Block(p,width,height,color,if (color != GOLDEN) lives-1 else lives)

