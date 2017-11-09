package DataStruct;
/*
	在数据段对数据的定义
 */
import java.util.*;
public class DataItem implements Iterable<Byte> { 
    
	protected long startAddress;
    protected int length; //byte的length
    protected ArrayList<Byte> data;  //用来存放数据的机器码
    private int line;
    private int col;
 
    public static final long BYTEMASK = 0x00000000000000FFL; //不同位数的数据的掩码
    public static final long HWMASK = 0x000000000000FFFFL;
    public static final long WMASK = 0x00000000FFFFFFFFL;
    public static final Byte ZERO = new Byte( (byte)0 ); //8 bits

    public DataItem( int l, int c) {
        data = new ArrayList<Byte>();
    }

    public DataItem( int l, int c, int len ) {
    	//space
        this( l,c );
        pad( len );
    }

    public DataItem( int l, int c, ArrayList<String> slist, boolean terminate ) { 
    	//ASCIZ&ASCI
    	this(l,c);

        for( String s: slist ) { 
            addString( s, terminate );
        }
    }

    public DataItem( int l , int c, ArrayList<Long> immlist, int bitcount ) throws Exception {
    	//word&halfword&byte
    	this(l,c);
        long MASK = 0;
        switch( bitcount ) {
            case 8:
                MASK = BYTEMASK;
                break;
            case 16:
                MASK = HWMASK;
                break;
            case 32:
                MASK = WMASK;
                break;
        }

        for( Long v: immlist ) {
            long val = v.longValue();
            if( ( val & ( ~MASK ) ) != 0 ) {
                String msg = "Value: " + val + " in data section label " + l;
                msg += " is greater than " + bitcount + " bits wide.";
                throw new Exception( msg );
            }

            for( int i = 0 ; i < bitcount; i += 8 ) {
                byte cur = (byte)(((val & MASK) >> i*8) & BYTEMASK);
                data.add( cur );
                length++;
            }
        }

        
    }

    public void addString( String s, boolean terminate ) {
        char [] strc = s.toCharArray();
        for( int i = 0; i < strc.length; i++ ) {
            data.add( new Byte( (byte)strc[i] ) );
        }

        length += strc.length;

        if( terminate ) {  // asciiz 在后边加 \0
            data.add( ZERO ); 
            length++;
        }
    }

    public void pad( int count ) {
        for( int i = 0; i < count; i++ ) {
            data.add( ZERO );
            length++;
        }
    }

    public int length() {
        return length;
    }
    
    public void setStartAddress( long addr ) {
        startAddress = addr;
    }

    public long address() {
        return startAddress;
    }

    public int line() {
        return line;
    }

    public int column() {
        return col;
    }

    public String toString() { 
        return String.format( "%s(start:0x%08X,len:%d)", startAddress, length );
    }

    public Iterator<Byte> iterator() {
        return data.iterator();
    }
}
