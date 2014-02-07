package fr.minecraftforgefrance.terminal;

import java.awt.Dimension;

@SuppressWarnings("serial")
public class ReadOnlyTerminal extends Terminal{
	 
	private TerminalOutput output;
	
	public ReadOnlyTerminal() {
		this(new TerminalOutput());
	}
	
	public ReadOnlyTerminal(TerminalOutput output) {
		this.setTitle("Read only terminal");
		this.output = output;
		this.add(this.output);
		pack();
	}
	
	public ReadOnlyTerminal(String title, Dimension dimension, TerminalOutput output) {
		this(output);
		this.setTitle(title);
		this.output.setPreferredSize(dimension);
		pack();
	}

	public TerminalOutput getOutput() {
		return output;
	}

	public void setOutput(TerminalOutput output) {
		this.output = output;
	}
	
	public static void main(String[] a) {
		new ReadOnlyTerminal("Test", new Dimension(800,600), new TerminalOutput(20)).setVisible(true);
	}
}
