
public class Environment {
    public int width;
    public int height;
    Cell[][] cell;

    public Environment(int w, int h){
        this.width = w;
        this.height = h;
        this.cell = new Cell[h][w];
    }

    public void setCell(Cell c, int x, int y) {
        this.cell[y][x] = c;
    }

    public void setCell(Cell[][] c) {
        this.cell = c;
    }

    public void setCellWall(Cell[] cells, int y) {
        this.cell[y] = cells;
    }

    public Cell getCell(int y, int x) {
        return cell[y][x];
    }

    public Cell getCell(Position p) {
        return getCell(p.y,p.x);
    }

    public Position getCell(Feature feat){
        for (int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                if(cell[y][x].f == feat) {
                    return new Position(y, x);
                }
            }
        }
        return null;
    }

    
    public boolean checkMove(Position p) {
        if (p.x < 0 || p.x >= width || p.y < 0 || p.y >= height) {
            //System.out.println("Movement Failed by exit stage");
            return false;
        } else if (getCell(p).f == Feature.BLOCK) {
            //System.out.println("Movement Failed by block");
            return false;
        } else {
            //System.out.println("Movement Success");
            return true;
        }
    }

    public int rewardPosition(Position p) {
        switch (getCell(p).f) {
            case MINE: return -100;
            case CLIFF: return -20;
            case GOAL: return 10;
            default: return -1;
        }
    }

    public void show(){
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                cell[y][x].printSymbol();
            }
            System.out.println();
        }
    }
    public void show(Agent a){
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                if (a.position.equals(y,x)) {
                    a.printSymbol();
                } else {
                    cell[y][x].printSymbol();
                }
            }
            System.out.println();
        }
    }

    public void show(Agent a, boolean u) {
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                if (cell[y][x].f == Feature.EMPTY || cell[y][x].f == Feature.CLIFF || cell[y][x].f == Feature.START) {
                    a.showPolicy(y, x, u);
                } else {
                    cell[y][x].printSymbol();
                }
            }
            System.out.println();
        }
    }
}
