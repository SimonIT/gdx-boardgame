package ch.asynk.zproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ch.asynk.zproject.ZProject;
import ch.asynk.zproject.GameHud;
import ch.asynk.zproject.GameBoard;
import ch.asynk.zproject.engine.Camera;

public class GameScreen implements Screen
{
    private static final float INPUT_DELAY = 0.1f;              // filter out touches after gesture
    private static final float ZOOM_SCROLL_FACTOR = .1f;
    private static final float ZOOM_GESTURE_FACTOR = .01f;

    private static final boolean DEBUG = true;

    private final ZProject zproject;
    private final GameHud hud;
    private final GameBoard board;
    private final Camera camera;
    private final SpriteBatch batch;
    private ShapeRenderer debugShapes = null;

    private final Vector2 dragPos = new Vector2();
    private final Vector3 boardTouch = new Vector3();
    private final Vector3 hudTouch = new Vector3();

    private boolean paused;
    private float inputDelay;
    private boolean inputBlocked;

    public GameScreen(final ZProject zproject)
    {
        this.zproject = zproject;
        this.hud = new GameHud(zproject.assets);
        this.board = new GameBoard(zproject.assets);
        this.batch = new SpriteBatch();
        this.camera = new Camera(10, board.getWidth(), board.getHeight(), 1.0f, 0.3f, false);
        Gdx.input.setInputProcessor(getMultiplexer(this));
        this.paused = false;
        this.inputDelay = 0f;
        this.inputBlocked = false;
        if (DEBUG) this.debugShapes = new ShapeRenderer();
    }

    @Override public void render(float delta)
    {
        if (paused) return;

        if (inputBlocked) {
            inputDelay -= delta;
            if (inputDelay <= 0f)
                inputBlocked = false;
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.applyMapViewport();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        board.draw(batch);
        batch.end();

        camera.applyHudViewport();
        batch.setProjectionMatrix(camera.getHudMatrix());
        batch.begin();
        hud.draw(batch);
        batch.end();

        if (DEBUG) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            debugShapes.setAutoShapeType(true);
            debugShapes.setProjectionMatrix(camera.getHudMatrix());
            debugShapes.begin();
            hud.drawDebug(debugShapes);
            debugShapes.end();
        }

    }

    @Override public void resize(int width, int height)
    {
        if (paused) return;
        ZProject.debug("GameScreen", String.format("resize (%d,%d)",width, height));
        camera.updateViewport(width, height);
        hud.resize(camera.getHud().width, camera.getHud().height);
    }

    @Override public void dispose()
    {
        ZProject.debug("GameScreen", "dispose()");
        batch.dispose();
        if (debugShapes != null) debugShapes.dispose();
        hud.dispose();
        board.dispose();
    }

    @Override public void show()
    {
        ZProject.debug("GameScreen", "show()");
    }

    @Override public void hide()
    {
        ZProject.debug("GameScreen", "hide()");
    }

    @Override public void pause()
    {
        ZProject.debug("pause() ");
        paused = true;
    }

    @Override public void resume()
    {
        ZProject.debug("resume() ");
        paused = false;
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void zoom(float dz)
    {
        camera.zoom(dz);
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private InputMultiplexer getMultiplexer(final GameScreen screen)
    {
        final InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new InputAdapter() {
            @Override public boolean scrolled(int amount)
            {
                screen.zoom(amount * ZOOM_SCROLL_FACTOR);
                return true;
            }
            @Override public boolean touchDown(int x, int y, int pointer, int button)
            {
                if (inputBlocked) return true;
                if (button == Input.Buttons.LEFT) {
                    dragPos.set(x, y);
                    camera.unproject(x, y, boardTouch);
                    camera.unprojectHud(x, y, hudTouch);
                    if(!hud.touch(hudTouch.x, hudTouch.y))
                        board.touch(boardTouch.x, boardTouch.y);
                }
                return true;
            }
            @Override public boolean touchDragged(int x, int y, int pointer)
            {
                int dx = (int) (dragPos.x - x);
                int dy = (int) (dragPos.y - y);
                dragPos.set(x, y);
                camera.translate(dx, dy);
                return true;
            }
        });
        multiplexer.addProcessor(new GestureDetector(new GestureAdapter() {
            @Override public boolean zoom(float initialDistance, float distance)
            {
                if (initialDistance > distance)
                    screen.zoom(ZOOM_GESTURE_FACTOR);
                else
                    screen.zoom(-ZOOM_GESTURE_FACTOR);
                inputBlocked = true;
                inputDelay = INPUT_DELAY;
                return true;
            }
        }));

        return multiplexer;
    }
}