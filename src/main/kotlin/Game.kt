import pt.isel.canvas.*

/**
 * Game information
 * [area], [ball], [racket], [blocks], [lives], [score]
 * @property area the size of the game
 * @property ball current ball
 * @property racket the player racket
 * @property blocks the blocks of the game
 * @property lives game lives
 * @property score the player score
 * @property level the game level
 */
data class Game(val area : Area, val ball : List<Ball>, val racket : Racket, val blocks : List<Block>, val lives : List<Ball>, val score : Int, val level : Int)


/**
 * Area information
 * @property width the width of the game
 * @property height the height of the game
 */
data class Area(val width : Int, val height : Int)

/**
 * Creates the starting game with a list of 1 ball, 72 blocks and 5 lives
 * @param [width] width of the game
 * @param [height] height of the game
 */
fun startGame(width: Int,height: Int) : Game{
    val a = Area(width,height)
    val r = Racket(a.width/2)
    val ball = r.initialBall(a)
    val blocks = initialBlocks()
    val lives = lives(a)
    return Game(a,ball,r,blocks,lives,0,1)
}

/**
 * Draws all elements of the game
 * @param [cv] Canvas
 */
fun Game.draw(cv : Canvas){
    cv.erase()
    racket.draw(cv,area)
    ball.forEach{it.draw(cv)}
    blocks.forEach{it.draw(cv)}
    lives.forEach{it.draw(cv)}
    drawScore(cv)
    drawWinLoss(cv)
    drawLevel(cv)
}

/**
 * Moves racket
 * @return the modified game
 */
fun Game.moveRacket(x : Int) : Game{
    val r = move(x,area)
    return Game(area,ball,r,blocks,lives,score,level)
}

/**
 * Moves the ball and checks if it is in game or if it hit a block
 * @return the modified game
 */
fun Game.step() : Game{
    val movedBall = if(win()) racket.initialBall(area) else ball.map {it.step(area,racket)}
    val leftBall = movedBall.filter{!isOutGame(it)}
    val game = Game(area,leftBall,racket,blocks,lives,score,level)
    return game.ball.fold(game,{acc: Game, ball: Ball -> acc.blockCollision(ball) })

}

/**
 * Reflects the ball on the blocks, deletes the blocks and accumulates player score
 * @return the modified game
 */
fun Game.blockCollision(b : Ball) : Game{
    val hitBlocks = blocks.filter {b.ballHitsBlock(it)}
    if (hitBlocks.isEmpty()) return this
    val removeBall = ball - b
    val newBall = b.reflectedBall(hitBlocks)
    val hit = hitBlocks.map {it.hit()}.filter { it.lives!=0 }
    val score = hitBlocks.fold(score,{acc, block -> acc + block.points()})
    return Game(area,removeBall + newBall,racket,blocks - hitBlocks + hit,lives,score,level)
}


/**
 * Shoots ball and updates player lives
 * @return the modified game
 */
fun Game.shootBall() : Game{
    val shot = if(ball.isEmpty() && lives.isNotEmpty()) racket.initialBall(area) else ball.map { it.shotBall(racket,area)}
    val removeLife = if (ball.isEmpty() && lives.isNotEmpty()) lives - lives.last() else lives
    return Game(area,shot,racket,blocks,removeLife,score,level)
}


/**
 * Checks if the ball is not in-game
 * @return true if the ball is out of the game area
 */
fun Game.isOutGame(b: Ball): Boolean = b.p.y + BALL_RADIUS > area.height && b.v.dy > 0


/**
 * Checks if the player lost
 * @return true if the player lost
 */
fun Game.gameOver() : Boolean = lives.isEmpty() && ball.isEmpty()


/**
 * Checks if the player won the game
 * @return true if the player won
 */
fun Game.win() : Boolean = level == 2 && blocks.all { it.color == GOLDEN }

/**
 * Checks if the player won the 1st level
 * @return true if the player won
 */
fun Game.winLvl1() = level == 1 && blocks.all { it.color == GOLDEN }


/**
 * Draws win or loss
 */
fun Game.drawWinLoss(cv: Canvas) {
    when {
        win() -> cv.drawText(area.width / 2 - 40, area.height / 2, "Finish", YELLOW, 25)
        gameOver() -> cv.drawText(10, area.height - 5, "Game Over", RED, 25)
    }
}
/**
 * Draws game score
 */
fun Game.drawScore(cv: Canvas) =
    cv.drawText(area.width/2 - BLOCK_WIDTH,area.height-5,if (win()) (score + lives.size*10).toString() else score.toString(), WHITE,20)


/**
 * Draws current game level
 */
fun Game.drawLevel(cv: Canvas) = cv.drawText(area.width - BLOCK_WIDTH*3,area.height - 10,"Level: $level", WHITE,20)


/**
 * Plays the game sounds
 */
fun Game.sounds(g:Game) {
    when{
        win() && blocks.size != g.blocks.size  -> playSound("win")
        ball.any { it.p.y < BALL_RADIUS || it.p.x !in BALL_RADIUS..area.width - BALL_RADIUS } -> playSound("hitCanvas")
        ball.any { it.ballHitsRacket(area, racket) } -> playSound("hitRacket")
        blocks.size != g.blocks.size || blocks.any { bl -> ball.any { it.ballHitsBlock(bl) } } -> playSound("hitBlock")
        lives.isNotEmpty() && ball.any { it.p.y in area.height-2*BALL_RADIUS..area.height } -> playSound("outGame")
        lives.isEmpty() && ball.any { it.p.y in area.height-2*BALL_RADIUS..area.height } -> playSound("gameOver")
    }
}

/**
 * Creates a new game with a new level
 * @return the modified game
 */
fun Game.nextLevel() : Game = if (winLvl1()) Game(area, racket.initialBall(area), racket, createBlocks(level2), lives, score, level + 1)
                                else Game(area, ball, racket, blocks, lives, score, level)