package pt.uma.tpsi.ad.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PowerBrick extends Brick {

    private BrickGrid brickGrid;

    public PowerBrick(SpriteBatch batch, int posX, int posY, BrickGrid brickGrid) {
        super(batch, posX, posY, "powerup_brick.png");
        this.brickGrid = brickGrid;
    }

    @Override
    public boolean onCollision() {
        if (!collided) {
            collided = true;
            brickGrid.destroyAllBricks();
        }
        return true;
    }
}
