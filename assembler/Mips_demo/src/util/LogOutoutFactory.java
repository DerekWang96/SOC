package util;

public class LogOutoutFactory {
	private static StringBuilder output = new StringBuilder();
	private static final LogOutoutFactory factory = new LogOutoutFactory();
	public StringBuilder getOutput() {
		return output;
	}
	public void setOutput(StringBuilder output) {
		LogOutoutFactory.output = output;
	}
	public static LogOutoutFactory getInstance(){
		return factory;
	}
	public static void append(String s){
		output.append(s);
	}
}
