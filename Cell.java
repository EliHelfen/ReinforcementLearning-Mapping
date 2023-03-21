
public class Cell {
    Feature f;
    public Cell(char c) {
        switch (c) {
            case 'S': this.f = Feature.START;
                break;
            case 'G': this.f = Feature.GOAL;
                break;
            case 'B': this.f = Feature.BLOCK;
                break;
            case 'M': this.f = Feature.MINE;
                break;
            case 'C': this.f = Feature.CLIFF;
                break;
            default: this.f = Feature.EMPTY;
                break;
        }
    }

    public Feature getF() {
        return f;
    }

    public void printSymbol() {
        switch (this.f) {
            case START: System.out.print("S");
                break;
            case BLOCK: System.out.print("█");
                break;
            case CLIFF: System.out.print("C");
                break;
            case GOAL: System.out.print("G");
                break;
            case MINE: System.out.print("M");
                break;
            default: System.out.print(" ");
                break; 
        }
    }
}
