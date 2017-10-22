import java_cup.runtime.Symbol;

%%
%class MipsLexer
%cup
%line
%column
%ignorecase
%eofclose

%{
    private static final boolean DEBUG = false;
    private StringBuffer string = new StringBuffer();
    private void error(String message) {
        System.err.println( "Error at line " + (yyline+1) +
                            ", col " + (yycolumn+1) +
                            " : " + message );
    }

    private void debugSymbols( int t, int l, int c, Object o ) {
        if( DEBUG ) {
        String str = Integer.toString( t );

        System.err.print( "Returning symbol:" + str );
        System.err.print( " line: " + (l+1) );
        System.err.print( " col: " + (c+1) );
        if( o != null ) {
            System.err.print( " o: " + o.toString() );
        }
        System.err.println();
        }
    }

    private Symbol symbol(int type) {
        debugSymbols( type, yyline, yycolumn, null );
        return new Symbol(type, yyline, yycolumn);
    }

    private Symbol symbol(int type, Object value) {
        debugSymbols( type, yyline, yycolumn, value );
        return new Symbol(type, yyline, yycolumn, value);
    }

    private Long convertToValid32I( String str, int base ) {
        long num = Long.parseLong( str, base );
        Long ret = null;

        if( num > Integer.MAX_VALUE || num > 4294967295L ) {
            error( str + " cannot be represented by the word size (32-bits)." +
                   "Maximum value allowed is " + 4294967295L );
        } else if( num < Integer.MIN_VALUE ) {
            error( str + " cannot be represented by the word size (32-bits)." +
                   "Minimum value allowed is " + Integer.MIN_VALUE );
        } else {
            ret = new Long( num );
        }

        return ret;
    }

    private Integer convertToValidReg( String str ) {
        int r = Integer.parseInt( str );
        Integer ret = null;
        if( r > 31 ) {
            error( str + " is not a valid register. Too large." );
        } else {
            ret = new Integer( r );
        }
        return ret;
    }
%} 
int         = 0 | -?[1-9][0-9]*
new_line    = \r|\n|\r\n|\z
space       = [ \t\f]
whitespace  = {new_line} | {space}
comment     = "#" ~{new_line}
ident       = [a-zA-Z_] [a-zA-Z_0-9]*
HEX         = "0x" [0-9A-Fa-f]{1,8}

EOL         = {comment} | {new_line} ( {whitespace} | {comment})* 
LABEL       = {ident}
IMM         =({int})
REG         = "$r" [0-9]+
STARTTEXT   = ".text"
STARTDATA   = ".data"

ROPER       = add | addu | sub | subu | slt | sltu | and | andu | nor | or | xor | sllv | srlv | srav 
RMDOPER     = mlt | mltu | div | divu
RSOPER      = sll | srl | sra
RTFOPER     = mfhi | mflo
RTTOPER     = mthi | mtlo 
RSPOPER     = mfc0 | mtc0
RJROPER     = jr
BREAK       = break
SYSCALL     = syscall
ERET        = eret
JALR        = jalr
NOP         = nop

IOPER       = addi | addiu | andi | ori | xori | slti | sltiu
IMEMOPER    = lw | sw | lb | lbu | lh | lhu | sb | sh 
ISINGREGOP  = lui | bgez | bgtz | blez | bltz | bgezal | bltzal 
IBRANCHOP   = bne | beq

JOPER       = j | jal

NOP         = nop

DASCIIZ     = ".asciiz"
DASCII     = ".ascii"
DBYTE       = ".byte"
DHALFWORD   = ".halfword"
DWORD       = ".word"
DSPACE      = ".space"


%state STRING
%%

<YYINITIAL> {
{IMM}                       {
                                Long val = convertToValid32I( yytext(), 10 );
                                System.out.println("[Lexer]: at line: "+ yyline  + " find IMM "+yytext());
                                if( val != null ) return symbol( sym.IMM, val );
                                else throw new Error( "Lexer failed due to bad integer value at ."+yyline  );
                            }


{HEX}                       {
                                Long val = convertToValid32I( yytext().substring(2), 16 );
                                System.out.println("[Lexer]: at line: "+ yyline  + " find IMM "+yytext());
                                if( val != null ) return symbol( sym.IMM, val );
                                else throw new Error( "Lexer failed due to bad integer value at "+yyline );
                            }

{ROPER}                     {   System.out.println("[Lexer]: at line: "+ yyline  + " find "+yytext());
                                return symbol( sym.ROPER, yytext() ); }
{RSOPER}                     { System.out.println("[Lexer]: at line: "+ yyline  + " find "+yytext());
                                return symbol( sym.RSOPER, yytext() ); }
{RMDOPER}                    {  System.out.println("[Lexer]: at line: "+ yyline  + " find "+yytext());    
                                return symbol( sym.RMDOPER, yytext() ); }
{RTFOPER}                    { System.out.println("[Lexer]: at line: "+ yyline  + " find "+yytext());
                                return symbol( sym.RTFOPER, yytext() ); }
{RTTOPER}                    { System.out.println("[Lexer]: at line: "+ yyline  + " find "+yytext());
                                return symbol( sym.RTTOPER, yytext() ); }
{RSPOPER}                    { System.out.println("[Lexer]: at line: "+ yyline  + " find "+yytext());
                                return symbol( sym.RSPOPER, yytext() ); }
{RJROPER}                     { System.out.println("[Lexer]: at line: "+ yyline  + " find "+yytext());
                                return symbol( sym.RJROPER, yytext() ); }
{BREAK}                     { System.out.println("[Lexer]: at line: "+ yyline + " find "+yytext());
                                return symbol( sym.BREAK, yytext() ); }
{ERET}                     { System.out.println("[Lexer]: at line: "+ yyline + " find "+yytext());
                                return symbol( sym.ERET, yytext() ); }
{JALR}                     { System.out.println("[Lexer]: at line: "+ yyline + " find "+yytext());
                                return symbol( sym.JALR, yytext() ); }
{NOP}                     { System.out.println("[Lexer]: at line: "+ yyline + " find "+yytext());
                                return symbol( sym.NOP, yytext() ); }                                
{SYSCALL}                   { System.out.println("[Lexer]: at line: "+ yyline + " find "+yytext());
                                return symbol( sym.SYSCALL, yytext() ); }

{IOPER}                     { System.out.println("[Lexer]: at line: "+ yyline + " find "+yytext());
                                return symbol( sym.IOPER, yytext() ); }
{IBRANCHOP}                  { System.out.println("At line: "+ yyline + " find "+yytext());
                                return symbol( sym.IBRANCHOP, yytext() ); }
{IMEMOPER}                  { System.out.println("[Lexer]: at line: "+ yyline + " find "+yytext());
                                return symbol( sym.IMEMOPER, yytext() ); }                                
{ISINGREGOP}                { System.out.println("[Lexer]: at line: "+ yyline+ " find "+yytext());
                                return symbol( sym.ISINGREGOP, yytext() ); }
{JOPER}                     { System.out.println("[Lexer]: at line: "+ yyline + " find "+yytext());
                                return symbol( sym.JOPER, yytext() ); }

{DASCIIZ}       { return symbol( sym.DASCIIZ ); }
{DASCII}       { return symbol( sym.DASCII ); }
{DBYTE}       { return symbol( sym.DBYTE ); }
{DHALFWORD}       { return symbol( sym.DHALFWORD ); }
{DWORD}       { return symbol( sym.DWORD ); }
{DSPACE}       { return symbol( sym.DSPACE ); }

{REG}                       {   
                                System.out.println("[Lexer]: at line: "+ yyline+ " find REG "+yytext());
                                Integer r = convertToValidReg( yytext().substring(2) ); 
                                if( r != null ) return symbol( sym.REG, r );
                                else throw new Error( "Lexer failed due to invalid register specification." );
                            }
{EOL}                       { if (DEBUG) System.err.println( "EOL match: < " + yytext() + " >" ); return symbol( sym.EOL ); }
","                         { return symbol( sym.COMMA ); }
":"                         { return symbol( sym.COLON ); }
"("                         { return symbol( sym.LPAREN ); }
")"                         { return symbol( sym.RPAREN ); }
 \"                         { string.setLength(0); yybegin(STRING); }
{STARTTEXT}                 { return symbol( sym.STARTTEXT ); }
{STARTDATA}                 { return symbol( sym.STARTDATA ); }
{LABEL}                     { System.out.println("[Lexer]: at line: "+ yyline+ " find LABLE "+yytext());
                              return symbol( sym.LABEL, yytext() ); }

{space}                     { /* IGNORE */ if( DEBUG) System.err.println("Ignoreing Space! <" +yytext()+ ">"); }
//{comment}                   { /* IGNORE */ System.err.println("Ignoreing Comment! <" +yytext()+ ">"); }

}
<STRING> {
  \"                             { yybegin(YYINITIAL); 
                                   return symbol(sym.STRLITERAL, 
                                   string.toString()); }
  [^\n\r\"\\]+                   { string.append( yytext() ); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }

  \\r                            { string.append('\r'); }
  \\\"                           { string.append('\"'); }
  \\                             { string.append('\\'); }
}


/* error fallback */
.|\n                         { 
                                error( "Unknown token! Did you accidentally enter a wrong letter?" );
                                throw new Error("Illegal character <"+yytext()+"> at line : " + yyline); 
                             }
