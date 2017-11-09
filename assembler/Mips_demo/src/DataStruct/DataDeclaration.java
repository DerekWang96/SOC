package DataStruct;

import java.util.ArrayList;
import java.util.Iterator;

public class DataDeclaration implements Iterable<Byte>  {
	public String lable;
	public Long startAddress = (long) 0x00000000;
	public ArrayList<Byte> data = new ArrayList<Byte>();
    private int line;
    private int col;
    public int length = 0;
	public ArrayList<DataItem> dataItems = new ArrayList<DataItem>();
    
    public DataDeclaration(){}
    
    public DataDeclaration(String lable , int line ,int col){
    	this.lable = lable;
    	this.line = line;
    	this.col = col;
    }
	public Long getStartAddress() {
		return startAddress;
	}

	public void setStartAddress(Long start) {
		this.startAddress = start;
	}

	public String lable() {
		return lable;
	}
    public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getColum() {
		return col;
	}

	public void setColum(int col) {
		this.col = col;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	public void addData(DataItem di){
		dataItems.add(di);
		data.addAll(di.data);
		length +=di.length;
	}
    public Iterator<Byte> iterator() {
        return data.iterator();
    }
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("lable :%s \n", lable));
		int bytes = 0;
		long dataword = 0;
		long wordaddr = 0x0001000L;
		for (Byte b : data) {
			if (bytes == 4) {
				sb.append(String.format("0x%08X ", dataword));
				wordaddr += 4;
				sb.append(String.format("0x%08X \n", wordaddr));
				dataword = 0;
				bytes = 0;
			}
			long bval = (long) b.byteValue() & DataItem.BYTEMASK;
			bval = bval << 8 * bytes;
			dataword |= bval;
			bytes++;
		}
		return sb.toString();

//		return String.format( "Name: %s, Code %X", lable ,data );
	}
}
