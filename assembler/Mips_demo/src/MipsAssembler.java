import java_cup.runtime.Symbol;
import util.LogOutoutFactory;

import java.io.*;

import DataStruct.DataDeclaration;

public class MipsAssembler {
    public static final int PARAMETER_ERROR = 10;
    @SuppressWarnings("unused")
	public static void main( String [] args ) {
        int exitcode = 0;
        if( true ) {
            Symbol resultSymbol = null;
            String filename = args[0];
//            String filename = "D:/Develop/JavaDevelop/Mips_demo/src/goodmips.asm";
            try {
                MipsLexer ml = new MipsLexer( new FileInputStream( filename ) );
                MipsParser mp = new MipsParser( ml );
                resultSymbol = mp.parse();

                MipsAbstractSyntax mas = (MipsAbstractSyntax)resultSymbol.value;
                mas.processCode();
                
                System.out.println(mas.textFormatter());
                System.out.println(mas.dataFormatter());
                
                PrintWriter out = new PrintWriter( filename + "Ins.out" );
                out.print( mas.textFileFormatter());
                out.close();
                
                out = new PrintWriter( filename + "Data.out" );
                out .print( mas.dataFileFormatter());
                out.close();
                for(int i = 0; i < 4;i++ ){
                    out = new PrintWriter( filename + "Data"+i+".out" );
                    out .print( mas.dataFourFilesFormatter().get(i));
                    out.close();
                }
                System.out.println(LogOutoutFactory.getInstance().getOutput().toString());
                

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
