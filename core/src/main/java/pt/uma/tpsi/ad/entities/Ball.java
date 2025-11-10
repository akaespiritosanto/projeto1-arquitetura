package pt.uma.tpsi.ad.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import pt.uma.tpsi.ad.game.Animator;

public class Ball {


    private Animator animator;
    private int posX, posY;
    private Rectangle boundingBox;
    private int directionY;
    private int directionX;
    private int speedX, speedY;
    private boolean outOfbounds;


    public Ball(SpriteBatch batch) {
        animator = new Animator(batch, "fireball.png", 2, 2);
        this.directionY = 1;
        this.directionX = 1;
        this.speedY = 3;
        this.speedX = 3;

    }

    public void create() {
        animator.create();
        posX = (Gdx.graphics.getWidth() / 2) - this.animator.getWidth() / 2;
        posY = this.animator.getHeight() * 2;
        boundingBox = new Rectangle(posX, posY, animator.getWidth(), animator.getHeight());
    }

    public void render() {
        posY += (speedY * directionY);
        posX += (speedX * directionX);

        if (posY > Gdx.graphics.getHeight() - animator.getHeight()){
            directionY = -1;
        }
        else if (posY < 0) {
            outOfbounds = true;
            // For now, just bounce off the bottom
            // Later, you can add game over logic here
        }

        // Right wall
        if (posX > Gdx.graphics.getWidth() - animator.getWidth()) {
            directionX = -1;
        }
        // Left wall
        else if (posX < 0) {
            directionX = 1;
        }

        boundingBox.setPosition(posX, posY);
        animator.render(posX, posY);
    }

    public void setAngleFromPaddleHit(Rectangle paddle) {
        // 1. Get centers
        float ballCenterX = this.boundingBox.x + this.boundingBox.width / 2;
        float paddleCenterX = paddle.x + paddle.width / 2;

        // 2. Calculate normalized offset (from -1.0 for far left, to +1.0 for far right)
        float normalizedOffset = (ballCenterX - paddleCenterX) / (paddle.width / 2);

        // 3. Define the maximum horizontal speed
        float maxSpeedX = 6.0f;

        // 4. Set new horizontal speed based on how far from the center the ball hit
        // Math.abs() ensures the speed is positive
        this.speedX = (int) (Math.abs(normalizedOffset) * maxSpeedX);

        // Ensure a minimum horizontal speed to prevent the ball from
        // just going straight up and down.
        if (this.speedX < 1) {
            this.speedX = 1;
        }

        // 5. REQUIREMENT 2: Set horizontal direction based on the sign of the offset
        if (normalizedOffset < 0) {
            this.directionX = -1; // Hit left side, go left
        } else {
            this.directionX = 1;  // Hit right side, go right
        }

        // 6. REQUIREMENT 1 (on collision): Always bounce up
        this.directionY = 1;
    }




    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public int getDirectiony() {
        return directionY;
    }

    public int getDirectionx() {
        return directionX;
    }

    public void reverseDirectionY() {
        this.directionY = 1;
    }

    public void reverseDirectionX() {
        this.directionX *= -1;
    }
}
