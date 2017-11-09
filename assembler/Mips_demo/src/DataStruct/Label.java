package DataStruct;
public class Label { 
    private int line;
    private int colum;
    private String name;
    private long offset;

    public Label( int l, int c, String n, long o ) {
        line = l;
        colum = c;
        name = n;
        offset = o;
    }

    public int getLine() {
        return line;
    }

    public int getColum() {
        return colum;
    }

    public String getName() {
        return name;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset( long a ) {
        offset = a;
    }

    public String toString() {
        return String.format( "%s(line: %d,col: %d,addr: 0x%08X)",
                              name,
                              line,
                              colum,
                              offset
                            );
    }
}
