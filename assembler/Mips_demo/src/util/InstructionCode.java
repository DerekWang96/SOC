package util;
public enum InstructionCode {
//包含了指令转换为机器码的所有可能用到的属性，在指令和立即数的格式化时，使用了interface
//*****R-instructions*******
    //-------RFMT--------------
	ADD ((byte)0x0, (byte)0x20, false, InstFormatters.RFMT, ImmProcessors.NONE ),
    ADDU ((byte)0x1, (byte)0x21, false, InstFormatters.RFMT, ImmProcessors.NONE ),
    SUB ((byte)0x0, (byte)0x22, false, InstFormatters.RFMT, ImmProcessors.NONE ),
    SUBU ((byte)0x0, (byte)0x23, false, InstFormatters.RFMT, ImmProcessors.NONE ),  
    AND ((byte)0x0, (byte)0x24, false, InstFormatters.RFMT, ImmProcessors.NONE ),
    OR ((byte)0x0, (byte)0x25, false, InstFormatters.RFMT, ImmProcessors.NONE ),
    NOR ((byte)0x0, (byte)0x27, false, InstFormatters.RFMT, ImmProcessors.NONE ),
    XOR ((byte)0x0, (byte)0x26, false, InstFormatters.RFMT, ImmProcessors.NONE ),
    SLT ((byte)0x0, (byte)0x2a, false, InstFormatters.RFMT, ImmProcessors.NONE ),
    SLTU ((byte)0x0, (byte)0x2b, false, InstFormatters.RFMT, ImmProcessors.NONE ),
    SLLV ((byte)0x0, (byte)0x04, false, InstFormatters.RFMT, ImmProcessors.NONE ),
    SRLV ((byte)0x0, (byte)0x06, false, InstFormatters.RFMT, ImmProcessors.NONE ),
    SRAV ((byte)0x0, (byte)0x07, false, InstFormatters.RFMT, ImmProcessors.NONE ),     
    //-------RMDFMT--------------
    MULT((byte)0x0, (byte)0x18, false, InstFormatters.RMDFMT, ImmProcessors.NONE ),
    MULTU((byte)0x0, (byte)0x19, false, InstFormatters.RMDFMT, ImmProcessors.NONE ),
    DIV((byte)0x0, (byte)0x1a, false, InstFormatters.RMDFMT, ImmProcessors.NONE ),
    DIVU((byte)0x0, (byte)0x1b, false, InstFormatters.RMDFMT, ImmProcessors.NONE ),
    //-------RTFFMT--------------
    MFHI((byte)0x0, (byte)0x20, false, InstFormatters.RTFFMT, ImmProcessors.NONE ),
    MFLO((byte)0x0, (byte)0x22, false, InstFormatters.RTFFMT, ImmProcessors.NONE ),
    //-------RTTFMT--------------
    MTHI((byte)0x0, (byte)0x21, false, InstFormatters.RTTFMT, ImmProcessors.NONE ),
    MTLO((byte)0x0, (byte)0x23, false, InstFormatters.RTTFMT, ImmProcessors.NONE ),
    JR((byte)0x0, (byte)0x8, false, InstFormatters.RTTFMT, ImmProcessors.NONE ),
    //-------SHIFTFMT--------------
    SLL ((byte)0x0, (byte)0x0, true, InstFormatters.SHIFTFMT, ImmProcessors.SHAMT ),
    SRL ((byte)0x0, (byte)0x2, true, InstFormatters.SHIFTFMT, ImmProcessors.SHAMT ),
    SRA ((byte)0x0, (byte)0x3, true, InstFormatters.SHIFTFMT, ImmProcessors.SHAMT ),
    //-------SYSFMT--------------
    ERET ((byte)0x0, (byte)0x18, false, InstFormatters.SYSFMT, ImmProcessors.NONE ),
    SYSCALL ((byte)0x0, (byte)0xc, false, InstFormatters.SYSFMT, ImmProcessors.NONE ),
    BREAK ((byte)0x0, (byte)0xd, false, InstFormatters.SYSFMT, ImmProcessors.NONE ),
    NOP ((byte)0x0, (byte)0x0, false, InstFormatters.SYSFMT, ImmProcessors.NONE ),
    //-------------------------------
    MFC0 ((byte)0x10, (byte)0x00, false, InstFormatters.SHIFTFMT, ImmProcessors.SEL ),
    MTC0 ((byte)0x10, (byte)0x00, false, InstFormatters.SHIFTFMT, ImmProcessors.SEL ),
//*****I-instructions*******
    //-------IFMT--------------
    ADDI ((byte)0x8, (byte)0x0, true, InstFormatters.IFMT, ImmProcessors.LOWER16 ),
    ADDIU ((byte)0x9, (byte)0x0, true, InstFormatters.IFMT, ImmProcessors.LOWER16 ),
    ANDI ((byte)0xc, (byte)0x0, true, InstFormatters.IFMT, ImmProcessors.LOWER16 ),
    ORI ((byte)0xd, (byte)0x0, true, InstFormatters.IFMT, ImmProcessors.LOWER16 ),
    XORI ((byte)0xe, (byte)0x0, true, InstFormatters.IFMT, ImmProcessors.LOWER16 ),
    SLTI ((byte)0xe, (byte)0x0, true, InstFormatters.IFMT, ImmProcessors.LOWER16 ),
    SLTIU ((byte)0xe, (byte)0x0, true, InstFormatters.IFMT, ImmProcessors.LOWER16 ),
    //-------IBFMT--------------
    BEQ ((byte)0x4, (byte)0x0, true, InstFormatters.IBFMT, ImmProcessors.BRANCH ),     
    BNE ((byte)0x5, (byte)0x0, true, InstFormatters.IBFMT, ImmProcessors.BRANCH ), 
    //-------IMEMFMT--------------
    LW ((byte)0x23, (byte)0x0, true, InstFormatters.IMEMFMT, ImmProcessors.LOWER16 ),     
    SW ((byte)0x2b, (byte)0x0, true, InstFormatters.IMEMFMT, ImmProcessors.LOWER16 ),
    LB ((byte)0x10, (byte)0x0, true, InstFormatters.IMEMFMT, ImmProcessors.LOWER16 ),
    LBU ((byte)0x14, (byte)0x0, true, InstFormatters.IMEMFMT, ImmProcessors.LOWER16 ),
    LH ((byte)0x11, (byte)0x0, true, InstFormatters.IMEMFMT, ImmProcessors.LOWER16 ),
    LHU ((byte)0x15, (byte)0x0, true, InstFormatters.IMEMFMT, ImmProcessors.LOWER16 ),
    SB ((byte)0x2b, (byte)0x0, true, InstFormatters.IMEMFMT, ImmProcessors.LOWER16 ),
    SH ((byte)0x2b, (byte)0x0, true, InstFormatters.IMEMFMT, ImmProcessors.LOWER16 ),
    //-------ISINGLETREG--------------
    LUI ((byte)0xf, (byte)0x0, true, InstFormatters.ISINGLETREG, ImmProcessors.LOWER16 ),
    //-------ISINGLETREG--------------
    BGEZ ((byte)0x1, (byte)0x0, true, InstFormatters.ISINGLESREG, ImmProcessors.JUMP ),
    BGTZ ((byte)0x7, (byte)0x0, true, InstFormatters.ISINGLESREG, ImmProcessors.JUMP ),
    BLEZ ((byte)0x6, (byte)0x0, true, InstFormatters.ISINGLESREG, ImmProcessors.JUMP ),
    BLTZ ((byte)0x1, (byte)0x0, true, InstFormatters.ISINGLESREG, ImmProcessors.JUMP ),
    BGEZAL ((byte)0x1, (byte)0x0, true, InstFormatters.ISINGLESREG, ImmProcessors.JUMP ),
    BLTZAL ((byte)0x1, (byte)0x0, true, InstFormatters.ISINGLESREG, ImmProcessors.JUMP ),
    
//*****J-instructions*******
    J ((byte)0x2, (byte)0x0, true, InstFormatters.JFMT, ImmProcessors.JUMP ),
	JAL ((byte)0x3, (byte)0x0, true, InstFormatters.JFMT, ImmProcessors.JUMP );
    
	public byte opcode;
    public byte funct;
    public boolean canHaveLabel;
    public InstructionFormatterInterface out; 
    public ImmediateProcessInterface imm;

    InstructionCode( byte opcode, byte funct, boolean label, InstructionFormatterInterface o, ImmediateProcessInterface i ) {
        this.opcode = opcode;
        this.funct = funct;
        this.canHaveLabel = label;
        this.out = o;
        this.imm = i;
    }
}
