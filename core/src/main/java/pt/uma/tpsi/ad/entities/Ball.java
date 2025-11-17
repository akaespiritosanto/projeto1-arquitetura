package pt.uma.tpsi.ad.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import pt.uma.tpsi.ad.game.Animator;

public class Ball {


    private Animator animator;
    private int posX, posY;
    private Rectangle boundingBox;
    private int directionY;
    private int directionX;
    private int angle;
    private boolean outOfbounds;


    public Ball(SpriteBatch batch) {
        animator = new Animator(batch, "fireball.png", 2, 2);
        this.directionY = 1;
        this.directionX = 1;
        this.angle = 2;
    }

    public void create() {
        animator.create();
        posX = (Gdx.graphics.getWidth() / 2) - this.animator.getWidth() / 2;
        posY = this.animator.getHeight() * 2;
        boundingBox = new Rectangle(posX, posY, animator.getWidth(), animator.getHeight());
    }

    public void render() {
        if (!outOfbounds) {
            posY += (angle * directionY);
            posX += (angle * directionX);

            if (posY > Gdx.graphics.getHeight() - animator.getHeight()) {
                directionY = -1;
            } else if (posY < -animator.getHeight()) {
                outOfbounds = true;
            }

            if (posX > Gdx.graphics.getWidth() - animator.getWidth()) {
                directionX = -1;
            } else if (posX < 0) {
                directionX = 1;
            }
        }

        boundingBox.setPosition(posX, posY);
        animator.render(posX, posY);
    }

    public void setAngleFromPaddleHit(Rectangle paddle) {
        float paddleThird = paddle.width / 3;
        float paddleLeft = paddle.x;
        float paddleMiddle = paddle.x + paddleThird;
        float paddleRight = paddle.x + 2 * paddleThird;

                int ballCenterX = posX + animator.getWidth() / 2;

        if (ballCenterX >= paddleLeft && ballCenterX < paddleMiddle) {
            directionX = -1;
            angle = 2;
        } else if (ballCenterX >= paddleMiddle && ballCenterX < paddleRight) {
            directionX = 0;
            angle = 2;
        } else {
            directionX = 1;
            angle = 2;
        }
        directionY = 1;
    }




    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public boolean isOutOfBounds() {
        return outOfbounds;
    }

    public int getDirectiony() {
        return directionY;
    }

    public void forceDown() {
        this.directionY = -1;
    }
}
