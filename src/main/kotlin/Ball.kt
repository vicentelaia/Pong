import pt.isel.canvas.CYAN
import pt.isel.canvas.Canvas

const val BALL_RADIUS = 7

/**
 * Ball information
 * [p], [v]
 * @property [p] ball position
 * @property [v] ball  velocity
 */
data class Ball(val p: Position,val v: Velocity)

data class Position(val x : Int, val y : Int)
data class Velocity(val dx: Int, val dy: Int)

operator fun Position.plus(v:Velocity) = Position(x + v.dx, y + v.dy)

fun Ball.draw(cv : Canvas) = cv.drawCircle(p.x,p.y,BALL_RADIUS,CYAN)

/**
 * @return true if the ball hits racket
 */
fun Ball.ballHitsRacket(a : Area, r: Racket) : Boolean = p.y + BALL_RADIUS in a.height - 50..a.height-50+RACKET_HEIGHT && v.dy > 0 && p.x in r.x - 30 - BALL_RADIUS..r.x + 30 + BALL_RADIUS

/**
 * @return true if the ball is attached to racket
 */
fun Ball.ballIsInRacket( a :Area) : Boolean = p.y == a.height-50-BALL_RADIUS && v.dy == 0

/**
 * list of initial lives
 */
fun lives(a : Area) : List<Ball> =
    listOf(
        Ball(Position(20, a.height - 12),Velocity(0,0)),
        Ball(Position(42, a.height - 12),Velocity(0,0)),
        Ball(Position(64, a.height - 12),Velocity(0,0)),
        Ball(Position(86, a.height - 12),Velocity(0,0)),
        Ball(Position(108, a.height - 12),Velocity(0,0)),
    )

fun Ball.shotBall(r: Racket,a: Area): Ball = if(ballIsInRacket(a)) Ball(Position(r.x,a.height-50-BALL_RADIUS),Velocity(0,-4)) else Ball(p,v)

/**
 * Reflects the ball on canvas and racket
 * @return the modified ball
 */
fun Ball.step(a : Area, r: Racket): Ball =
    when {
        p.x !in BALL_RADIUS..a.width - BALL_RADIUS -> Ball(Position(p.x-v.dx,p.y),Velocity(-v.dx,v.dy))
        p.y < BALL_RADIUS -> Ball(Position(p.x,p.y-v.dy),Velocity(v.dx,-v.dy))
        ballHitsRacket(a,r) -> Ball(Position(p.x,p.y-v.dy),maxVelocity(r))
        ballIsInRacket(a) -> r.ballFollowRacket(a)
        else -> Ball(p + v,v)
    }

fun Ball.maxVelocity (r:Racket):Velocity =
    when {
        ((v.dx + r.reflect(p.x)) > 6) -> Velocity(6,-v.dy)
        ((v.dx + r.reflect(p.x)) < -6) -> Velocity(-6,-v.dy)
        else -> Velocity(v.dx + r.reflect(p.x),-v.dy)
    }

/**
 * @return true if ball hits a block
 */
fun Ball.ballHitsBlock(block: Block) : Boolean = p.x in block.p.x - BALL_RADIUS..block.p.x + BLOCK_WIDTH + BALL_RADIUS
                                                && p.y in block.p.y - BALL_RADIUS..block.p.y + BLOCK_HEIGHT + BALL_RADIUS

/**
 * Reflects the ball on the blocks in specific cases
 * @return the modified ball
 */
fun Ball.reflectedBall(hitBlocks : List<Block>) =
    when(hitBlocks.size) {
        2 -> if (hitBlocks[0].p.x != hitBlocks[1].p.x) Ball(p, Velocity(v.dx, -v.dy)) else Ball(p, Velocity(-v.dx, v.dy))
        3 -> Ball(p, Velocity(-v.dx, -v.dy))
        else -> hitBlocks[0].reflect(this)
    }