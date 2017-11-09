import java.util.*;
import DataStruct.*;

public class MipsAbstractSyntax {
	public static final long DATASEC_START = 0x1001000;
	Hashtable<String, Label> symtable = new Hashtable<>();
	Set<String> lableSet= new HashSet<String>();
	ArrayList<Instruction> ilist = new ArrayList<Instruction>();
	ArrayList<ArrayList<Instruction>> insListArray = new ArrayList<>();
	ArrayList<ArrayList<DataDeclaration>> dataArray = new ArrayList<>();
	ArrayList<DataDeclaration> data;
	public ArrayList<DataSection> dataSections  = new ArrayList<DataSection>();
	public ArrayList<TextSection> textSections  = new ArrayList<TextSection>();
	
	MipsAbstractSyntax(){};
	public void processCode() throws Exception{

		//处理地址
		long length = 0;
		for(int i = 0 ; i < textSections.size() ;i++){
			ArrayList<Instruction> ilist = new ArrayList<Instruction>();
			TextSection temp = textSections.get(i);
			ilist.addAll(textSections.get(i).getInstructionList());
			insListArray.add(ilist);
			if(textSections.get(i).startAdress == 0){
				temp.setStartAddress(length/4);
				length += textSections.get(i).getInstructionList().size()*4;
			}
		}
		
		long dataLengthtemp = 0;
		for(int i = 0 ; i < dataSections.size();i++){
			ArrayList<DataDeclaration> data = new ArrayList<>();
			DataSection temp = dataSections.get(i);
			data = temp.getDataList();
			temp.setStartAdress(dataLengthtemp);
			dataSections.set(i, temp);
			dataArray.add(data);
			dataLengthtemp += temp.getLength();
//			System.out.println("temp length: "+ temp.getLength());
		}
		
		//查重
		for(DataSection ds : dataSections){
			for (Label l : ds.symtable.values()) {
				Label m = symtable.get(l.getName());
				if (m != null) {
					String msg = "Symbol " + l.getName() + " declared more than once, both in data section. \n";				
					throw new Exception(msg);
				}
				l.setOffset(l.getOffset()+ds.getStartAdress());
				symtable.put(l.getName(), l);
			}
		}
		for(TextSection ts : textSections){
			for (Label l : ts.symtable.values()) {
				Label m = symtable.get(l.getName());
				if (m != null) {
					String msg = "Symbol " + l.getName() + " declared more than once, once in data section. \n";				
					throw new Exception(msg);
				}
				l.setOffset(l.getOffset()+ts.getStartAddress());
				symtable.put(l.getName(), l);
			}
		}

		for(ArrayList<Instruction> ilist : insListArray){
			for (Instruction i : ilist ) {			
				if (i.hasLabel()) {
					String l = i.getLabel();
					Label match = symtable.get(l);
					String msg;
					if (match == null){
						msg = "Unable to resolve lable: " + i;
						throw new Exception(msg);
					}
					else
						i.resolveLabel(match.getOffset()); // 没有错误则对lable进行解析，转化为imm						
						System.err.println(String.format("%s %04X  0x%04X", match.getName(), match.getOffset(),i.immediate));
				}
			}
		}

		

}

	public String textFormatter() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i< insListArray.size();i++){
			ArrayList<Instruction> ilist = new ArrayList<Instruction>();
			ilist = insListArray.get(i);
			sb.append("\n*****one text section******\n");
			sb.append(String.format("start address: 0x%04X \n", textSections.get(i).startAdress));
			for (Instruction ins : ilist) {
				sb.append(String.format("0x%08X || ", ins.getEncoded()));
				sb.append(ins.toString());
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	public String dataFormatter(){
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for(ArrayList<DataDeclaration> data : dataArray){			
			if (data.size() > 0) {
				sb.append("\n******data section******\n");
				sb.append(String.format("data section start: 0x%04X\n", dataSections.get(count).getStartAdress()));
				sb.append(String.format("data section legnth: 0x%04X\n", dataSections.get(count).getLength()));
				long wordaddr = 0x1001000L;
				for (DataDeclaration d : data) {
					sb.append(String.format("0x%08X ", wordaddr));
					int bytes = 0;
					long dataword = 0;
					for (Byte b : d) {
						if (bytes == 4) {
							sb.append(String.format("0x%08X\n", dataword));
							wordaddr += 4;
							sb.append(String.format("0x%08X ", wordaddr));
							dataword = 0;
							bytes = 0;
						}
						long bval = (long) b.byteValue() & DataItem.BYTEMASK;
						bval = bval << 8 * bytes;
						dataword |= bval;
						bytes++;
					}
					sb.append(String.format("0x%08X\n", dataword));
					wordaddr += 4;
					dataword = 0;
					bytes = 0;
				}
			}
			count++;
		}
		return sb.toString();
	}
	public String assemble() {
		StringBuilder sb = new StringBuilder();
		for (Instruction i : ilist) {
			sb.append(String.format("0x%08X || ", i.getEncoded()));
			sb.append(i.toString());
			sb.append("\n");
		}
		if (data.size() > 0) {
			sb.append("DATA SEGMENT\n");
			long wordaddr = 0x1001000L;
			for (DataDeclaration d : data) {
				sb.append(String.format("0x%08X ", wordaddr));
				int bytes = 0;
				long dataword = 0;
				for (Byte b : d) {
					if (bytes == 4) {
						sb.append(String.format("0x%08X\n", dataword));
						wordaddr += 4;
						sb.append(String.format("0x%08X ", wordaddr));
						dataword = 0;
						bytes = 0;
					}
					long bval = (long) b.byteValue() & DataItem.BYTEMASK;
					bval = bval << 8 * bytes;
					dataword |= bval;
					bytes++;
				}
				sb.append(String.format("0x%08X\n", dataword));
				wordaddr += 4;
				dataword = 0;
				bytes = 0;
			}
		}

		return sb.toString();

	}

	public String toString() {
		StringBuilder sb = new StringBuilder("MipsAbstractSyntax(\n\tSymbolTable(\n");
		for (Label l : symtable.values()) {
			sb.append("\t\t");
			sb.append(l.toString());
			sb.append("\n");
		}
		sb.append("\t)\n\tInstructions(\n");
		for (Instruction i : ilist) {
			sb.append("\t\t");
			sb.append(i.toString());
			sb.append("\n");
		}
		sb.append("\t)\n\tData(\n");
		for (DataDeclaration d : data) {
			sb.append("\t\t");
			sb.append(d.toString());
			sb.append("\n");
		}
		sb.append("\t)\n)");
		return sb.toString();
	}

}
