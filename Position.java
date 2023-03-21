public class Position {
    int y;
    int x;

    public Position(int f, int s) {
        this.y = f;
        this.x = s;
    }

    public void setFirst(int f) {
        this.y = f;
    }

    public void setSecond(int s) {
        this.x = s;
    }

    public int getFirst() {
        return y;
    }

    public int getSecond() {
        return x;
    }

    public Position move(Direction d) {
        Position p = new Position(this.y, this.x);
        switch (d) {
            case UP: p.y--;
            break;
            case DOWN: p.y++;
            break;
            case LEFT: p.x--;
            break;
            case RIGHT: p.x++;
            break;
        }
        return p;
    }

    public boolean equals(int ey, int ex) {
        if (ex == x && ey == y) {
            return true;
        } else return false;
    }

    public void print() {
        //System.out.printf("X: %d, Y: %d ",x,y);
    }

}
