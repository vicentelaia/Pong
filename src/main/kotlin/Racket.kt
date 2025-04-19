import pt.isel.canvas.Canvas
import pt.isel.canvas.RED
import pt.isel.canvas.WHITE

const val RACKET_WIDTH = 60
const val RACKET_HEIGHT = 10

/**
 * Racket information
 * @property [x] racket horizontal position
 */
data class Racket(val x : Int)

fun Racket.draw(cv : Canvas,a : Area){
    val y = a.height - 50
    cv.erase()
    cv.drawRect(x-30,y,RACKET_WIDTH,RACKET_HEIGHT, WHITE)
    cv.drawRect(x-30,y,RACKET_WIDTH - 50,RACKET_HEIGHT - 5, RED)
    cv.drawRect(x+20,y,RACKET_WIDTH - 50,RACKET_HEIGHT - 5, RED)
    cv.drawRect(x-20,y,RACKET_WIDTH - 45,RACKET_HEIGHT - 5,0xff6666)
    cv.drawRect(x+5,y,RACKET_WIDTH - 45,RACKET_HEIGHT - 5,0xff6666)
}

fun move(x: Int,a : Area) : Racket =
    when {
        x >= a.width - RACKET_WIDTH/2 -> Racket(a.width - RACKET_WIDTH/2)
        x <= RACKET_WIDTH/2 -> Racket(RACKET_WIDTH/2)
        else -> Racket(x)
    }

fun Racket.reflect(b : Int) : Int =
    when (b) {
        in (x - 5 .. x + 5) -> 0
        in (x - 20 .. x - 6) -> -1
        in (x + 6 .. x + 20) -> +1
        in (x - 30 - BALL_RADIUS .. x - 21) -> -3
        in (x + 21 .. x + 30 + BALL_RADIUS) -> +3
        else -> b
    }

fun Racket.ballFollowRacket(a : Area) : Ball = Ball(Position(x,a.height-50 - BALL_RADIUS),Velocity(0,0))

/**
 * list of initial ball attached to racket
 */
fun Racket.initialBall(a : Area) : List<Ball> = listOf(ballFollowRacket(a),Ball(Position(a.width/2,a.height/2 + 100),Velocity(0,1)))