import pt.isel.canvas.*

fun main(){
    onStart {
        val cv = Canvas(13 * BLOCK_WIDTH,600,BLACK)
        var game = startGame(cv.width,cv.height)
        loadSounds("hitRacket","hitCanvas","hitBlock","outGame","gameOver","win")

        cv.onMouseMove {game = game.moveRacket(it.x)}

        cv.onMouseDown {game = game.shootBall()}

        cv.onTimeProgress(10){
            val oldGame = game
            game = game.step()
            game = game.nextLevel()
            game.draw(cv)
            game.sounds(oldGame)
        }
    }
    onFinish {}
}