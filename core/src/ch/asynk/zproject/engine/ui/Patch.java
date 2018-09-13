package ch.asynk.gdx.board.engine.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;

public class Patch extends Element
{
    private NinePatch patch;

    public Patch(NinePatch patch)
    {
        super();
        this.patch = patch;
        setPosition(0, 0, patch.getTotalWidth(), patch.getTotalHeight());
    }

    @Override public void draw(Batch batch)
    {
        if (!visible) return;
        patch.draw(batch, getX(), getY(), getWidth(), getHeight());
    }
}
