package util;
public class InstFormatters {
    public static final InstructionFormatterInterface RFMT = new InstructionFormatterInterface() {
        public String insFormatter( long offset, InstructionCode c, int rd, int rs, int rt, long imm, String l, long e ) {
            return String.format("Offset: " + ADDRESS + ", %s " + REG + ", " + REG + ", " + REG + META,
                                                               offset,
                                                               c.toString(),
                                                               rd,
                                                               rs,
                                                               rt,
                                                               c.opcode,
                                                               c.funct,
                                                               e );
        }

    };   
    
    public static final InstructionFormatterInterface RMDFMT = new InstructionFormatterInterface() { //两个寄存器
        public String insFormatter( long offset, InstructionCode c, int rd, int rs, int rt, long imm, String l, long e ) {
            return String.format("Offset: " + ADDRESS + ", %s " + REG + ", " + REG + META,
                                                               offset,
                                                               c.toString(),
                                                               rt,
                                                               rs,                                                              
                                                               c.opcode,
                                                               c.funct,
                                                               e );
        }

    };
    
    public static final InstructionFormatterInterface RTFFMT = new InstructionFormatterInterface() { //一个寄存器
        public String insFormatter( long offset, InstructionCode c, int rd, int rs, int rt, long imm, String l, long e ) {
            return String.format("Offset: " + ADDRESS + ", %s " + REG + ", " + REG + META,
                                                               offset,
                                                               c.toString(),
                                                               rd,                                                              
                                                               c.opcode,
                                                               c.funct,
                                                               e );
        }

    };
    
    public static final InstructionFormatterInterface RTTFMT = new InstructionFormatterInterface() { //一个寄存器
        public String insFormatter( long offset, InstructionCode c, int rd, int rs, int rt, long imm, String l, long e ) {
            return String.format("Offset: " + ADDRESS + ", %s " + REG + META,
                                                               offset,
                                                               c.toString(),
                                                               rt,                                                              
                                                               c.opcode,
                                                               c.funct,
                                                               e );
        }

    };
   
    public static final InstructionFormatterInterface SHIFTFMT = new InstructionFormatterInterface() {
        public String insFormatter( long offset, InstructionCode c, int rd, int rs, int rt, long imm, String l, long e ) {
            return String.format( "Offset: " + ADDRESS + ", %s " + REG + ", " + REG + ", %d" + META,
                                                               offset,
                                                               c.toString(),
                                                               rt,
                                                               rd,
                                                               imm,
                                                               c.opcode,
                                                               c.funct,
                                                               e );
        }

    };

    public static final InstructionFormatterInterface IFMT = new InstructionFormatterInterface() {
        public String insFormatter( long offset, InstructionCode c, int rd, int rs, int rt, long imm, String l, long e ) {
            String labelFmt = ( l != null && c.canHaveLabel  ) ? "%s" : "%d";
            Object immOrLab = ( l != null && c.canHaveLabel  ) ? l : imm;
            return String.format("Offset: " +  ADDRESS + ", %s " + REG + ", " + REG + ", " + labelFmt + META,
                                                               offset,
                                                               c.toString(),
                                                               rt,
                                                               rs,
                                                               immOrLab,
                                                               c.opcode,
                                                               c.funct,
                                                               e );
        }

    };

    public static final InstructionFormatterInterface IBFMT = new InstructionFormatterInterface() {
        public String insFormatter( long offset, InstructionCode c, int rd, int rs, int rt, long imm, String l, long e ) {
            String labelFmt = ( l != null && c.canHaveLabel  ) ? "%s" : "%d";
            Object immOrLab = ( l != null && c.canHaveLabel  ) ? l : imm;
            return String.format("Offset: " +  ADDRESS + ", %s " + REG + ", " + REG + ", " + labelFmt + META,
                                                               offset,
                                                               c.toString(),
                                                               rs,
                                                               rt,
                                                               immOrLab,
                                                               c.opcode,
                                                               c.funct,
                                                               e );
        }

    };

    public static final InstructionFormatterInterface ISINGLETREG = new InstructionFormatterInterface() {
        public String insFormatter( long offset, InstructionCode c, int rd, int rs, int rt, long imm, String l, long e ) {
            String labelFmt = ( l != null && c.canHaveLabel  ) ? "%s" : "%d";
            Object immOrLab = ( l != null && c.canHaveLabel  ) ? l : imm;
            return String.format("Offset: " + ADDRESS + ", %s " + REG + ", " + labelFmt + META,
                                                               offset,
                                                               c.toString(),
                                                               rt,
                                                               immOrLab,
                                                               c.opcode,
                                                               c.funct,
                                                               e );
        }

    };
    
    public static final InstructionFormatterInterface ISINGLESREG = new InstructionFormatterInterface() {
        public String insFormatter( long offset, InstructionCode c, int rd, int rs, int rt, long imm, String l, long e ) {
            String labelFmt = ( l != null && c.canHaveLabel  ) ? "%s" : "%d";
            Object immOrLab = ( l != null && c.canHaveLabel  ) ? l : imm;
            return String.format("Offset: " + ADDRESS + ", %s " + REG + ", " + labelFmt + META,
                                                               offset,
                                                               c.toString(),
                                                               rs,
                                                               immOrLab,
                                                               c.opcode,
                                                               c.funct,
                                                               e );
        }

    };

    public static final InstructionFormatterInterface IMEMFMT = new InstructionFormatterInterface() {
        public String insFormatter( long offset, InstructionCode c, int rd, int rs, int rt, long imm, String l, long e ) {
            return String.format("Offset: " + ADDRESS + ", %s " + REG + ", %d(" + REG + ") " + META,
                                                               offset,
                                                               c.toString(),
                                                               rt,
                                                               imm,
                                                               rs,
                                                               c.opcode,
                                                               c.funct,
                                                               e );
        }

    };

    public static final InstructionFormatterInterface JFMT = new InstructionFormatterInterface() {
        public String insFormatter( long offset, InstructionCode c, int rd, int rs, int rt, long imm, String l, long e ) {
            String labelFmt = ( l != null && c.canHaveLabel  ) ? "%s" : "%d";
            Object immOrLab = ( l != null && c.canHaveLabel  ) ? l : imm;
            return String.format( "Offset: " +  ADDRESS + ", %s " + labelFmt + META,
                                                               offset,
                                                               c.toString(),
                                                               immOrLab,
                                                               c.opcode,
                                                               c.funct,
                                                               e );
        }

    };

    public static final InstructionFormatterInterface SYSFMT = new InstructionFormatterInterface() {
        public String insFormatter( long addr, InstructionCode c, int rd, int rs, int rt, long imm, String l, long e ) {
            return String.format("Offset: " + ADDRESS + ", %s" + META,
                                                               addr,
                                                               c.name(),
                                                               c.opcode,
                                                               c.funct,
                                                               e );
        }

    };
}
