package DataStruct;
import java.util.*;

import util.LogOutoutFactory;
public class TextSection {
    public ArrayList<Instruction> ilist;
    public Hashtable<String,Label> symtable;
    public Long startAdress = (long) 0x0;

	public TextSection() {
        ilist = new ArrayList<Instruction>();
        symtable = new Hashtable<String,Label>();
    }

    public void appendTextSection( TextSection ts ) throws Exception {
        ArrayList<Instruction> list = ts.getInstructionList();
        Hashtable<String,Label> syms = ts.getSymTable();
        if( list.size() == 0 ) {
            // no instructions, only labels, add labels
            for( Label l: syms.values() ) {
                addLabel( l );
            }
        } else {
            for( Instruction i: list ) {
                // Add Labels that are at instruction address
                for( Label l: syms.values() ) {
                    if( l.getOffset() == i.getOffset() ) {
                        addLabel( l );
                        syms.remove( l.getName() );
                    } else if ( l.getOffset() > i.getOffset() ) {
                        break;
                    }
                }
                addInstruction( i );
            }
            // Add labels that are after last instruction
            if( syms.size() > 0 ) {
                for( Label l: syms.values() ) {
                    addLabel( l );
                }
            }
        }
    }

    public void addInstructions( ArrayList<Instruction> list ) {
        for( Instruction i: list ) {
            i.setOffset( ilist.size() * 4 );
            ilist.add( i );
        }
    }

    public void addInstruction( Instruction i ) {
        i.setOffset( ilist.size() * 4 );
        ilist.add( i );
    }

    public void addLabel( Label l ) throws Exception {
        if( symtable.containsKey( l.getName() ) ) {
        	LogOutoutFactory.append("(Error): "+l + " was declared more than once in one text section.\n");
            throw new Exception( l + " was declared more than once in one text section." );
        }
        l.setOffset( ilist.size() * 4 );//一个字节一个地址 所以一条指令4个字
        symtable.put( l.getName(), l );
    }

    public Hashtable<String,Label> getSymTable() {
        return symtable;
    }

    public ArrayList<Instruction> getInstructionList() {
        return ilist;
    }

    public Long getStartAddress() {
		return startAdress;
	}

	public void setStartAddress(Long address) {
//		System.out.println("text section set address: " + address);
		this.startAdress = address;
	}

	@Override
	public String toString() {
		return "TextSection [ilist=" + ilist + ", symtable=" + symtable + "]";
	}
}
