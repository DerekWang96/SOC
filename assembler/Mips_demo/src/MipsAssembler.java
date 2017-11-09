import java_cup.runtime.Symbol;
import java.io.*;

import DataStruct.DataDeclaration;

public class MipsAssembler {
    public static final int PARAMETER_ERROR = 10;
    @SuppressWarnings("unused")
	public static void main( String [] args ) {
        int exitcode = 0;
        if( true ) {
            Symbol resultSymbol = null;
//            String filename = args[0];
            String filename = "D:/Develop/JavaDevelop/Mips_demo/src/goodmips.asm";
            try {
                MipsLexer ml = new MipsLexer( new FileInputStream( filename ) );
                MipsParser mp = new MipsParser( ml );
                resultSymbol = mp.parse();

                MipsAbstractSyntax mas = (MipsAbstractSyntax)resultSymbol.value;
//                for(DataDeclaration d  : mas.dataSections.get(1).dataDeclarations){
//                	System.out.println(d);
//                }
//                System.out.println(mas.textSections.get(0).address);
                mas.processCode();
                
                System.out.println(mas.textFormatter());
                System.out.println(mas.dataFormatter());
//                System.out.println(mas.dataSections.get(0).symtable);
//                System.out.println(mas.dataSections );
//                System.out.println(mas.textSections);
//
//                PrintWriter out = new PrintWriter( filename + ".out" );
//                out.print( mas.assemble() );
//                out.close();

            } catch (IOException e) {
                System.err.println("[ERROR]: " + e.getMessage() );
                exitcode = PARAMETER_ERROR;
            } catch (Exception e) {
                e.printStackTrace( System.err );
                exitcode = 255;
            }

        } else {
            System.err.println( "Usage: java MipsAssembler [input file]" );
            exitcode = 1;
        }

        if( exitcode != 0 ) {
            System.exit( exitcode );
        }
    }
}
