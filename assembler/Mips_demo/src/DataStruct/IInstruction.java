package DataStruct;
import util.ImmProcessors;

public class IInstruction extends Instruction {
    private int regsource;
    private int regtarget;

    public IInstruction( int line, int col, String opname, int rs, int rt, long imm )
        throws Exception
    {
        super( line, col, opname, imm );
        regsource = rs;
        regtarget = rt;
        if( imm > 65535 || imm < Short.MIN_VALUE ) {
        	String msg = "[ERROR]: ";
            msg += opname.toLowerCase() + " on line " + line;
            msg += " has an immediate value larger than 16 bits.";
            throw new Exception( msg );
        }
    }

    public IInstruction( int line, int col, String opname, int rs, int rt, String l )
        throws Exception
    {
        super( line, col, opname, l );
        regsource = rs;
        regtarget = rt;
        if( ! code.canHaveLabel ) {
        	String msg = "[ERROR]: ";
            msg += code.toString() + " on line: " + line;
            msg += " has a label. This operation does not take labels.\n";
//            System.err.println(msg);
            throw new Exception( msg );
        }
    }

    public boolean resolveLabel( long addr ) {
        if( code.canHaveLabel) {
            if( code.imm == ImmProcessors.UPPER16 ) {
                addr = addr >>> 16;
                immediate = addr;
            } else if( code.imm == ImmProcessors.BRANCH ) {
                addr = addr - ( offset + 4 );
                addr = addr >> 2;
                immediate = addr;
            } else if( code.imm == ImmProcessors.JUMP ) {
                immediate = addr >> 2;
            } else {
                immediate = addr;
            }
        }
        return true;
    }

    public int getRD() { return 0; }
    public int getRS() { return regsource & FIVEBIT_MASK; }
    public int getRT() { return regtarget & FIVEBIT_MASK; }

    public long getEncoded() {
        long inst = super.getEncoded();
        long imm =  processImmediate();
        long rt = ( regtarget << RT_SHIFT );
        long rs = ( regsource << RS_SHIFT );
        inst |= imm | rt | rs;
        return inst;
    }
}
