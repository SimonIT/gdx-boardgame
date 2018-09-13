package ch.asynk.gdx.board.engine.ui;

public enum Alignment
{
    ABSOLUTE,           // Root
    RELATIVE,           // Default
    TOP_LEFT,
    TOP_RIGHT,
    TOP_CENTER,
    MIDDLE_LEFT,
    MIDDLE_RIGHT,
    MIDDLE_CENTER,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    BOTTOM_CENTER;

    public Alignment verticalMirror()
    {
        switch(this) {
            case TOP_LEFT:
                return TOP_RIGHT;
            case MIDDLE_LEFT:
                return MIDDLE_RIGHT;
            case BOTTOM_LEFT:
                return BOTTOM_RIGHT;
            case TOP_RIGHT:
                return TOP_LEFT;
            case MIDDLE_RIGHT:
                return MIDDLE_LEFT;
            case BOTTOM_RIGHT:
                return BOTTOM_LEFT;
        }
        return this;
    }

    public Alignment horizontalMirror()
    {
        switch(this) {
            case TOP_LEFT:
                return BOTTOM_LEFT;
            case TOP_CENTER:
                return BOTTOM_CENTER;
            case TOP_RIGHT:
                return BOTTOM_RIGHT;
            case BOTTOM_LEFT:
                return TOP_LEFT;
            case BOTTOM_CENTER:
                return TOP_CENTER;
            case BOTTOM_RIGHT:
                return TOP_RIGHT;
        }
        return this;
    }

    public boolean isTop()
    {
        switch(this) {
            case TOP_LEFT:
            case TOP_CENTER:
            case TOP_RIGHT:
                return true;
        }
        return false;
    }

    public boolean isMiddle()
    {
        boolean r = false;
        switch(this) {
            case MIDDLE_LEFT:
            case MIDDLE_CENTER:
            case MIDDLE_RIGHT:
                return true;
        }
        return false;
    }

    public boolean isBottom()
    {
        boolean r = false;
        switch(this) {
            case BOTTOM_LEFT:
            case BOTTOM_CENTER:
            case BOTTOM_RIGHT:
                return true;
        }
        return false;
    }

    public boolean isLeft()
    {
        boolean r = false;
        switch(this) {
            case TOP_LEFT:
            case MIDDLE_LEFT:
            case BOTTOM_LEFT:
                return true;
        }
        return false;
    }

    public boolean isRight()
    {
        boolean r = false;
        switch(this) {
            case TOP_RIGHT:
            case MIDDLE_RIGHT:
            case BOTTOM_RIGHT:
                return true;
        }
        return false;
    }

    public boolean isCenter()
    {
        switch(this) {
            case TOP_CENTER:
            case MIDDLE_CENTER:
            case BOTTOM_CENTER:
                return true;
        }
        return false;
    }

    public float getX(Element element, float width)
    {
        float x = element.getInnerX();
        switch(this) {
            case TOP_LEFT:
            case MIDDLE_LEFT:
            case BOTTOM_LEFT:
                break;
            case TOP_CENTER:
            case MIDDLE_CENTER:
            case BOTTOM_CENTER:
                x += ((element.getInnerWidth() - width) / 2);
                break;
            case TOP_RIGHT:
            case MIDDLE_RIGHT:
            case BOTTOM_RIGHT:
                x += (element.getInnerWidth() - width);
                break;
        }
        return x;
    }

    public float getY(Element element, float height)
    {
        float y = element.getInnerY();
        switch(this) {
            case TOP_LEFT:
            case TOP_CENTER:
            case TOP_RIGHT:
                y += (element.getInnerHeight() - height);
                break;
            case MIDDLE_LEFT:
            case MIDDLE_CENTER:
            case MIDDLE_RIGHT:
                y += ((element.getInnerHeight() - height) / 2);
                break;
            case BOTTOM_LEFT:
            case BOTTOM_CENTER:
            case BOTTOM_RIGHT:
                break;
        }
        return y;
    }
}
