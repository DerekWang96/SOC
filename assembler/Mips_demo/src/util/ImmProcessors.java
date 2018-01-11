package util;
public class ImmProcessors {
    public static final ImmediateProcessInterface NONE = new ImmediateProcessInterface() {
        public long immProcessor( long i ) {
            return 0;
        }
    };

    public static final ImmediateProcessInterface SHAMT = new ImmediateProcessInterface() {
        public long immProcessor( long i ) {
            return i & IMM5;
        }
    };
    
    public static final ImmediateProcessInterface SEL = new ImmediateProcessInterface() {
        public long immProcessor( long i ) {
            return i & IMM3;
        }
    };

    public static final ImmediateProcessInterface LOWER16 = new ImmediateProcessInterface() {
        public long immProcessor( long i ) {
            return i & IMM16;
        }
    };
    
    public static final ImmediateProcessInterface UPPER16 = new ImmediateProcessInterface() {
        public long immProcessor( long i ) {
            return i & IMM16;
        }
    };

    public static final ImmediateProcessInterface BRANCH = new ImmediateProcessInterface() {
        public long immProcessor( long i ) {
            return i & IMM16;
        }
    };

    public static final ImmediateProcessInterface JUMP = new ImmediateProcessInterface() {
        public long immProcessor( long i ) {
            return i & IMM26;
        }
    };
}
