import java.util.*;
import DataStruct.*;
import util.LogOutoutFactory;

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
				temp.setStartAddress(length);
//				System.err.println("Length:"+ length);
				textSections.set(i,temp);
//				System.err.println(textSections.get(i).getStartAddress());				
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
					LogOutoutFactory.append("(Error): "+msg+"\n");
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
					LogOutoutFactory.append("(Error): "+msg+"\n");
					throw new Exception(msg);
				}
				l.setOffset(l.getOffset()+ts.getStartAddress()/4);
//				System.err.println(l.getName());
//				System.err.println(String.format( "0x%08X", l.getOffset()));
				symtable.put(l.getName(), l);
			}
		}
		int count = 0;
		for(ArrayList<Instruction> ilist : insListArray){		
			for (Instruction i : ilist ) {			
				if (i.hasLabel()) {
					String l = i.getLabel();
					Label match = symtable.get(l);
					String msg;
					if (match == null){
						msg = "Unable to resolve lable: " + i;
						LogOutoutFactory.append("(Error): "+msg+"\n");
						throw new Exception(msg);
					}
					else
						i.resolveLabel(match.getOffset()); // 没有错误则对lable进行解析，转化为imm						
//						System.err.println(String.format("%s %04X  0x%04X", match.getName(), match.getOffset(),i.immediate));
				}
			}
			count++;
		}

		

}
	
	public ArrayList<String> dataFourFilesFormatter() {
		ArrayList<String> result = new ArrayList<String>();
		StringBuilder sb = new StringBuilder();
		StringBuilder sb0 = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		StringBuilder sb3 = new StringBuilder();
		int count = 0;
		int i = 0; //补0计数器
		sb.append("memory_initialization_radix = 2;\n");
		sb.append("memory_initialization_vector = \n");
		sb0.append("memory_initialization_radix = 2;\n");
		sb0.append("memory_initialization_vector = \n");
		sb1.append("memory_initialization_radix = 2;\n");
		sb1.append("memory_initialization_vector = \n");
		sb2.append("memory_initialization_radix = 2;\n");
		sb2.append("memory_initialization_vector = \n");
		sb3.append("memory_initialization_radix = 2;\n");
		sb3.append("memory_initialization_vector = \n");
		for(ArrayList<DataDeclaration> data : dataArray){			
			if (data.size() > 0) {
				long wordaddr = 0x0000000L;
				for (DataDeclaration d : data) {
//					sb.append(String.format("0x%08X ", wordaddr));
					int bytes = 0;
					long dataword = 0;
					ArrayList<Byte> temp = new ArrayList<Byte>();
//					for(int i = 0 ;i<d.data.size();i++){//小端存储
//						temp.add(d.data.get(d.data.size()-1-i));
//					}
					for(int j = 0 ;j<d.data.size();j++){//大端存储
						temp.add(d.data.get(j));
					}
					for (Byte b : temp) {
						if (bytes == 4) {
							sb.append(String.format("%32s,\n", Long.toBinaryString(dataword)).replace(' ', '0'));
							String tempStr = String.format("%32s,\n", Long.toBinaryString(dataword)).replace(' ', '0');
							sb0.append(tempStr.substring(0,8)+",\n");
							sb1.append(tempStr.substring(8,16)+",\n");
							sb2.append(tempStr.substring(16,24)+",\n");
							sb3.append(tempStr.substring(24,32)+",\n");
							i++;
							wordaddr += 4;
							dataword = 0;
							bytes = 0;
						}
						long bval = (long) b.byteValue() & DataItem.BYTEMASK;
						bval = bval << 8 * (3-bytes);
						dataword |= bval;
						bytes++;
					}
					sb.append(String.format("%32s,\n", Long.toBinaryString(dataword)).replace(' ', '0'));
					String tempStr = String.format("%32s,\n", Long.toBinaryString(dataword)).replace(' ', '0');
					sb0.append(tempStr.substring(0,8)+",\n");
					sb1.append(tempStr.substring(8,16)+",\n");
					sb2.append(tempStr.substring(16,24)+",\n");
					sb3.append(tempStr.substring(24,32)+",\n");
					i++;
					wordaddr += 4;
					dataword = 0;
					bytes = 0;
				}
			}
			count++;
		}
		for(int j = i ; j <= 16383 ; j++){
			if(j == 16383){
//				sb.append(String.format("%08X;\n",0));
				sb.append(String.format("%32s;\n", Long.toBinaryString(0)).replace(' ', '0'));
				sb0.append(String.format("%8s;\n", Long.toBinaryString(0)).replace(' ', '0'));
				sb1.append(String.format("%8s;\n", Long.toBinaryString(0)).replace(' ', '0'));
				sb2.append(String.format("%8s;\n", Long.toBinaryString(0)).replace(' ', '0'));
				sb3.append(String.format("%8s;\n", Long.toBinaryString(0)).replace(' ', '0'));
			}

			else{
				sb.append(String.format("%32s,\n", Long.toBinaryString(0)).replace(' ', '0'));
//				sb.append(String.format("%08X,\n",0));
				sb0.append(String.format("%8s,\n", Long.toBinaryString(0)).replace(' ', '0'));
				sb1.append(String.format("%8s,\n", Long.toBinaryString(0)).replace(' ', '0'));
				sb2.append(String.format("%8s,\n", Long.toBinaryString(0)).replace(' ', '0'));
				sb3.append(String.format("%8s,\n", Long.toBinaryString(0)).replace(' ', '0'));
			}

		}
		result.add(sb0.toString());
		result.add(sb1.toString());
		result.add(sb2.toString());
		result.add(sb3.toString());
		result.add(sb.toString());
		return result;
	}
	
	public String dataFileFormatter(){
			StringBuilder sb = new StringBuilder();
			int count = 0;
			int i = 0; //补0计数器
			sb.append("memory_initialization_radix = 16;\n");
			sb.append("memory_initialization_vector = \n");
			for(ArrayList<DataDeclaration> data : dataArray){			
				if (data.size() > 0) {
					long wordaddr = 0x0000000L;
					for (DataDeclaration d : data) {
//						sb.append(String.format("0x%08X ", wordaddr));
						int bytes = 0;
						long dataword = 0;
						ArrayList<Byte> temp = new ArrayList<Byte>();
//						for(int i = 0 ;i<d.data.size();i++){//小端存储
//							temp.add(d.data.get(d.data.size()-1-i));
//						}
						for(int j = 0 ;j<d.data.size();j++){//大端存储
							temp.add(d.data.get(j));
						}
						for (Byte b : temp) {
							if (bytes == 4) {
								sb.append(String.format("%32s,\n", Long.toBinaryString(dataword)).replace(' ', '0'));
								String tempStr = String.format("%32s,\n", Long.toBinaryString(dataword)).replace(' ', '0');
//								System.err.println("dataSection:"+tempStr.substring(24, 32));
								i++;
								wordaddr += 4;
								dataword = 0;
								bytes = 0;
							}
							long bval = (long) b.byteValue() & DataItem.BYTEMASK;
							bval = bval << 8 * (3-bytes);
							dataword |= bval;
							bytes++;
						}
						sb.append(String.format("%32s,\n", Long.toBinaryString(dataword)).replace(' ', '0'));
						String tempStr = String.format("%32s,\n", Long.toBinaryString(dataword)).replace(' ', '0');
//						System.err.println("dataSection:"+tempStr.substring(24,32));
						i++;
						wordaddr += 4;
						dataword = 0;
						bytes = 0;
					}
				}
				count++;
			}
			for(int j = i ; j <= 16383 ; j++){
				if(j == 16383)
//					sb.append(String.format("%08X;\n",0));
					sb.append(String.format("%32s;\n", Long.toBinaryString(0)).replace(' ', '0'));
				else
					sb.append(String.format("%32s,\n", Long.toBinaryString(0)).replace(' ', '0'));
//					sb.append(String.format("%08X,\n",0));
			}
			return sb.toString();
		}
	
	public String textFileFormatter(){
		StringBuilder sb = new StringBuilder();
		long Mem[] = new long[16384];
		Boolean dirty[] = new Boolean[16384];
		for(int i = 0; i <= 16383 ;i++){
			dirty[i] = false;			
		}
		sb.append("memory_initialization_radix = 16;\n");
		sb.append("memory_initialization_vector = \n");
		for(int i = 0 ; i< insListArray.size();i++){
			ArrayList<Instruction> ilist = new ArrayList<Instruction>();
			ilist = insListArray.get(i);
//			sb.append("\n*****one text section******\n");
//			sb.append(String.format("start address: 0x%04X \n", textSections.get(i).startAdress));
			long startAdress = textSections.get(i).startAdress/4;
			for (Instruction ins : ilist) {
				Mem[(int) startAdress] = ins.getEncoded();
				if (dirty[(int)startAdress]) {
					LogOutoutFactory.append("(Warning): Text memory unit:"+String.format(" 0x%4s", Long.toBinaryString(startAdress)).replace(' ', '0')+ " is written more than once, may as well check.\n");
					System.err.println("(Warning): Text memory unit:"+String.format(" 0x%4s", Long.toBinaryString(startAdress)).replace(' ', '0')+ " is written more than once, may as well check.\n");
				}
				dirty[(int)startAdress] = true;
				startAdress++;
			}
		}
		
		for(int i = 0; i <= 16383 ;i++){
			if(i == 16383)
				sb.append(String.format("%32s;\n", Long.toBinaryString(Mem[i])).replace(' ', '0'));
			else
//				sb.append(String.format("%08X,\n", Mem[i]));
				sb.append(String.format("%32s,\n", Long.toBinaryString(Mem[i])).replace(' ', '0'));
			
		}
		return sb.toString();
	
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
				sb.append(String.format("Address: 0x%08X " , ins.getOffset() + textSections.get(i).startAdress));
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
				long wordaddr = 0x0000000L;
				for (DataDeclaration d : data) {
					sb.append(String.format("0x%08X ", wordaddr));
					int bytes = 0;
					long dataword = 0;
					ArrayList<Byte> temp = new ArrayList<Byte>();
//					for(int i = 0 ;i<d.data.size();i++){//小端存储
//						temp.add(d.data.get(d.data.size()-1-i));
//					}
					for(int i = 0 ;i<d.data.size();i++){//大端存储
						temp.add(d.data.get(i));
					}
					for (Byte b : temp) {
						if (bytes == 4) {
							sb.append(String.format("0x%08X\n", dataword));
							wordaddr += 4;
							sb.append(String.format("0x%08X ", wordaddr));
							dataword = 0;
							bytes = 0;
						}
						long bval = (long) b.byteValue() & DataItem.BYTEMASK;
						bval = bval << 8 * (3-bytes);
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
