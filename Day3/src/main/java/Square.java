public class Square {
    private int ID;
    private int x;
    private int y;
    private int width;
    private int height;

    Square(){}

    Square(int ID, int x, int y, int width, int height) {
        this.ID = ID;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getX2 () {
        return x + width;
    }

    public int getY2() {
        return y + height;
    }
}
