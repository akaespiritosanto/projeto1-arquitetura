package pt.uma.tpsi.ad.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import pt.uma.tpsi.ad.game.Animator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class BrickGrid extends ArrayList<Brick> {
    private final SpriteBatch batch;
    private Animator explosionAnimation;
    private boolean explosionActive = false;
    private int rows = 8;
    private int cols = 20;
    private int horizontalSpacing = 6;
    private int verticalSpacing = 6;
    private int topMargin = 64;

    private final Random random = new Random();

    public BrickGrid(SpriteBatch batch) {
        this.batch = batch;
        explosionAnimation = new Animator(batch, "explosion.png", 5, 2 );
    }

    public void create() {
        createGrid(rows, cols);
        explosionAnimation.create();
    }

    public void createGrid(int rows, int cols) {
        this.clear();
        this.rows = rows;
        this.cols = cols;

        NormalBrick brickTest = new NormalBrick(batch, 0, 0);
        brickTest.create();
        int brickWidth = (int) brickTest.getBoundingBox().getWidth();
        int brickHeight = (int) brickTest.getBoundingBox().getHeight();

        int totalWidth = cols * brickWidth + (cols - 1) * horizontalSpacing;
        int startX = (Gdx.graphics.getWidth() - totalWidth) / 2;
        int startYTop = Gdx.graphics.getHeight() - topMargin - brickHeight;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = startX + j * (brickWidth + horizontalSpacing);
                int y = startYTop - i * (brickHeight + verticalSpacing);
                Brick brick = randomizeBrick(x, y);
                brick.create();
                this.add(brick);
            }
        }
    }

    public Brick randomizeBrick(int x, int y) {
        double roll = random.nextDouble();
        if (roll < 0.60) {
            return new NormalBrick(batch, x, y);
        } else if (roll < 0.85) {
            return new StrongBrick(batch, x, y);
        } else if (roll < 0.99) {
            return new IndestructibleBrick(batch, x, y);
        } else {
            return new PowerBrick(batch, x, y, this);
        }
    }

    public boolean isWon() {
        if (this.isEmpty()) {
            return true;
        }
        for (Brick brick : this) {
            if (!(brick instanceof IndestructibleBrick)) {
                return false;
            }
        }
        return true;
    }

    public boolean isExplosionActive() {
        return explosionActive;
    }

    public void render() {
        for (Brick brick : this) {
            brick.render();
        }

        if (explosionActive) {
            int explosionX = (Gdx.graphics.getWidth() - explosionAnimation.getWidth()) / 2;
            int explosionY = (Gdx.graphics.getHeight() - explosionAnimation.getHeight()) / 2;
            explosionAnimation.render(explosionX, explosionY);
            if (explosionAnimation.isFinished()) {
                explosionActive = false;
            }
        }
    }

    public void destroyAllBricks() {
        explosionActive = true;
        Iterator<Brick> iterator = this.iterator();
        while (iterator.hasNext()) {
            Brick brick = iterator.next();
            if (!(brick instanceof IndestructibleBrick)) {
                iterator.remove();
            }
        }
    }

    public int checkCollisions(Ball ball) {
        if (ball == null || ball.getBoundingBox() == null || explosionActive) return 0;
        Rectangle ballBox = ball.getBoundingBox();

        Iterator<Brick> iterator = this.iterator();
        while (iterator.hasNext()) {
            Brick brick = iterator.next();
            Rectangle boundingBox = brick.getBoundingBox();
            if (boundingBox == null) continue;

            if (ballBox.overlaps(boundingBox)) {
                if (!brick.isCollided()) {
                    handleCollision(ball, brick);
                    boolean destroyed = brick.onCollision();
                    if (destroyed && !(brick instanceof PowerBrick)) {
                        iterator.remove();
                        return brick.getPoints();
                    }
                }
                break;
            }
        }
        return 0;
    }

    public void handleCollision(Ball ball, Brick brick) {
        if (ball != null) {
            ball.forceDown();
        }
    }
}
