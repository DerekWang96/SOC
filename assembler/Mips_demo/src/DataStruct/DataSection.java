package DataStruct;

import java.util.*;

import util.LogOutoutFactory;

public class DataSection {
	public ArrayList<DataDeclaration> dataDeclarations;
	public Hashtable<String, Label> symtable  ;
	public ArrayList<Byte> DSCode;
	public long startAdress = 0x00000000L ;
	public void setSymtable(Hashtable<String, Label> symtable) {
		this.symtable = symtable;
	}

	long next_free;

	public DataSection() {
		dataDeclarations = new ArrayList<DataDeclaration>();
		symtable = new Hashtable<String, Label>();
		next_free = startAdress;
	}

	public void addData(DataDeclaration d) throws Exception {
//		System.out.println("adding data.  "+ symtable.containsKey(d.lable));
		if (symtable.containsKey(d.lable)) {			
			LogOutoutFactory.append("(Error): "+ d.lable + " was declared more than once (in data section).\n");
			throw new Exception(d.lable + " was declared more than once (in data section).");
		}
		 d.setStartAddress( next_free );
		 next_free += d.data.size();
		 
		 int danglingBytes = (int)(next_free % 4L); //保证边界对齐，即两个数据声明不能在同一个字里，即使不够一个字也不行
		 if( danglingBytes > 0 ) {
		 next_free += ( 4 - danglingBytes );
		 for(int i = 0; i <(4- danglingBytes); i++){
			 d.data.add(new Byte( (byte)0 ));
		 	}
		 }
		dataDeclarations.add(d);
		symtable.put(d.lable, new Label(d.getLine(), d.getColum(), d.lable, d.getStartAddress()));
	}

	public Hashtable<String, Label> getSymTable() {
		return symtable;
	}

	public ArrayList<DataDeclaration> getDataList() {
		return dataDeclarations;
	}

	@Override
	public String toString() {
		return "DataSection [dataItems=" + dataDeclarations + ", DSCode=" + DSCode + ", next_free=" + next_free + "]";
	}

	public ArrayList<DataDeclaration> getDataDeclarations() {
		return dataDeclarations;
	}

	public void setDataDeclarations(ArrayList<DataDeclaration> dataDeclarations) {
		this.dataDeclarations = dataDeclarations;
	}

	public Hashtable<String, Label> getSymtable() {
		return symtable;
	}
	public long getStartAdress() {
		return startAdress;
	}

	public void setStartAdress(long startAdress) {
		this.startAdress = startAdress;
	}
	
	public long getLength(){
		long length = 0;
		for(DataDeclaration dd : dataDeclarations){
			length += dd.getLength();
		}
		return length;
	}
}
