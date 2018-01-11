package util;
public interface ImmediateProcessInterface {
    public static final long IMM16  = 0x000000000000FFFFL;
    public static final long IMM5   = 0x000000000000001FL;
    public static final long IMM3   = 0x0000000000000007L;
    public static final long IMM26  = 0x0000000003FFFFFFL;
    public abstract long immProcessor ( long i );
}
