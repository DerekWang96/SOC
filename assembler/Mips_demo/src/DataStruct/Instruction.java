package DataStruct;
import util.InstructionCode;

public abstract class Instruction {
    //对机器码不同部分进行移位的位数
	public static final int FUNCTSHIFT = 0;
	public static final int SELSHIFT = 0;
    public static final int SHAMSHIFT = 6;
    public static final int RD_SHIFT = 11;
    public static final int RT_SHIFT = 16;
    public static final int RS_SHIFT = 21;
    public static final int OPSHIFT = 26;
    //掩码
    public static final int FIVEBIT_MASK = 0x01F;
    public static final long WORD_MASK = 0x00000000FFFFFFFFL;
    
    private int linenum;
    private int colnum;
    protected long offset;
    protected String label;
    protected InstructionCode code;
	public long immediate;


    public Instruction( int line, int col, String opname ) {
        code = InstructionCode.valueOf( opname );
        linenum = line;
        colnum = col;
        label = null;
        offset = 0;
        immediate = 0;
    }

    public Instruction( int line, int col, String opname, String l ) {
        this( line, col, opname );
        label = l;
    }

    public Instruction( int line, int col, String opname, long i ) {
        this( line, col, opname );
        immediate = i;
    }

    public long processImmediate() {
        return code.imm.immProcessor(immediate );
    }

    public int lineNum() {
        return linenum;
    }

    public int colNum() {
        return colnum;
    }

    public boolean hasLabel() {
        return ( code.canHaveLabel && label != null );
    }

    public String getLabel() {
        return label;
    }

    public void setOffset( long a ) {
        offset = a;
    }

    public long getOffset() {
        return offset;
    }
    
    public InstructionCode getCode() {
		return code;
	}

	public void setCode(InstructionCode code) {
		this.code = code;
	}

    public abstract boolean resolveLabel( long addr ); //不同种命令解析lable的抽象函数
    
    public abstract int getRD();
    public abstract int getRS();
    public abstract int getRT();

    public long getEncoded() {
        long inst = 0;
        inst |= ( code.opcode << OPSHIFT );
        inst |= ( code.funct << FUNCTSHIFT );
        return inst & WORD_MASK ;
    }

    public String toString() {
        return code.out.insFormatter( offset, 
                         code,
                         getRD(),
                         getRS(),
                         getRT(),
                         processImmediate(),
                         label,
                         getEncoded() );
    }
}
