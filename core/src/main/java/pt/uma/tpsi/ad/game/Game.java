package pt.uma.tpsi.ad.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pt.uma.tpsi.ad.entities.Ball;
import pt.uma.tpsi.ad.entities.PlayerPaddle;


public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    private BackgroundManagement backgroundManagement;
    private BitmapFont font;
    private PlayerPaddle player;
    private Ball ball;


    @Override
    public void create() {
        Gdx.graphics.setWindowedMode(1280, 720);
        batch = new SpriteBatch();
        player = new PlayerPaddle(batch);
        player.create();
        ball = new Ball(batch);
        ball.create();
        backgroundManagement = new BackgroundManagement(batch);
        font = new BitmapFont(Gdx.files.internal("gamefont.fnt"),
            Gdx.files.internal("gamefont.png"), false);

    }

    @Override
    public void render() {

        // Render
        batch.begin();
        backgroundManagement.render();
        player.render();
        ball.render();
        if (ball.getBoundingBox().overlaps(player.getBoundingBox())) {
            if (ball.getDirectiony() == -1) {
                ball.reverseDirectionY();
            }
            if (ball.getDirectiony() == 1){
                ball.setAngleFromPaddleHit(player.getBoundingBox());
            }
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

}
