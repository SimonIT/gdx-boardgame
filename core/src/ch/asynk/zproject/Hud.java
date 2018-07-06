package ch.asynk.zproject;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.math.Rectangle;

import ch.asynk.zproject.engine.ui.Button;
import ch.asynk.zproject.engine.ui.Patch;
import ch.asynk.zproject.engine.ui.Alignment;
import ch.asynk.zproject.engine.ui.Root;
import ch.asynk.zproject.engine.Touchable;

public class Hud implements Disposable, Touchable
{
    private final Rectangle rect;
    private final Sprite corner;
    private final Root root;
    private final Button hello;
    private final Button next;

    public Hud(final Assets assets)
    {
        this.corner = new Sprite(assets.getTexture(assets.CORNER));
        this.rect = new Rectangle(0, 0, 0 ,0);

        this.root = new Root(2);
        this.root.setPadding(30);

        this.hello = new Button(assets.getFont(assets.FONT_25), assets.getNinePatch(assets.PATCH, 23, 23, 23 ,23), 10, 15);
        this.hello.write("Hello");
        this.root.add(this.hello);

        this.next = new Button(assets.getFont(assets.FONT_25), assets.getNinePatch(assets.PATCH, 23, 23, 23 ,23), 20, 0);
        this.next.write("NEXT");
        this.next.setPosition(100, 100);
        this.root.add(this.next);
    }

    @Override public void dispose()
    {
        corner.getTexture().dispose();
    }

    @Override public boolean touch(float x, float y)
    {
        if (rect.contains(x, y)) {
            if (root.touch(x, y)) {
                ZProject.debug("Hud", String.format("touchDown : %f %f", x, y));
                return true;
            }
        }
        return false;
    }

    public void resize(float width, float height)
    {
        rect.set(0, 0, width, height);
        this.root.resize(width, height);
    }

    public void draw(Batch batch)
    {
        drawButtons(batch);
        drawCorners(batch);
    }

    public void drawCorners(Batch batch)
    {
        float right = rect.x + rect.width - corner.getWidth();
        float top = rect.y + rect.height - corner.getHeight();
        corner.setRotation(0);
        corner.setPosition(rect.x, top);
        corner.draw(batch);
        corner.setRotation(90);
        corner.setPosition(rect.x, rect.y);
        corner.draw(batch);
        corner.setRotation(180);
        corner.setPosition(right, rect.y);
        corner.draw(batch);
        corner.setPosition(right, top);
        corner.setRotation(270);
        corner.draw(batch);
    }

    private void drawButtons(Batch batch)
    {
        hello.setAlignment(Alignment.TOP_LEFT);
        hello.setLabelAlignment(Alignment.BOTTOM_CENTER);
        hello.update();
        root.draw(batch);
        drawHello(batch, Alignment.TOP_CENTER, Alignment.BOTTOM_RIGHT);
        drawHello(batch, Alignment.TOP_RIGHT, Alignment.TOP_LEFT);
        drawHello(batch, Alignment.MIDDLE_LEFT, Alignment.TOP_CENTER);
        drawHello(batch, Alignment.MIDDLE_CENTER, Alignment.TOP_RIGHT);
        drawHello(batch, Alignment.MIDDLE_RIGHT, Alignment.MIDDLE_LEFT);
        drawHello(batch, Alignment.BOTTOM_LEFT, Alignment.MIDDLE_CENTER);
        drawHello(batch, Alignment.BOTTOM_CENTER, Alignment.MIDDLE_RIGHT);
        drawHello(batch, Alignment.BOTTOM_RIGHT, Alignment.BOTTOM_LEFT);
    }

    private void drawHello(Batch batch, Alignment alignment1, Alignment alignment2)
    {
        hello.setAlignment(alignment1);
        hello.setLabelAlignment(alignment2);
        hello.update();
        hello.draw(batch);
    }

    public void drawDebug(ShapeRenderer debugShapes)
    {
        root.drawDebug(debugShapes);
    }
}
