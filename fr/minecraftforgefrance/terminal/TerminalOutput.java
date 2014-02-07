package fr.minecraftforgefrance.terminal;

import java.awt.Color;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import fr.minecraftforgefrance.utils.Date;

@SuppressWarnings("serial")
public class TerminalOutput extends JScrollPane{
	private class outputHandler extends Handler {
		@Override
		public void close() throws SecurityException {
			sysoln("[INFO] fermeture du log");
		}

		// I already use buffer
		@Override
		public void flush() {}

		@Override
		public void publish(LogRecord record) {
			if(record.getLevel().intValue() >= logger.getLevel().intValue()) {
				if(record.getLevel() == Level.SEVERE) {
					syserrln(parse(record));
				}else {
					sysoln(parse(record));
				}
			}
		}
		
		private String parse(LogRecord record) {
			return "<"+Date.getJJMMAAAAHMS()+"> ["+record.getLevel()+"] : "+record.getMessage();
		}
		
	}
	
	private static final Pattern newLine = Pattern.compile("\n");
	private Style defaultStyle, errStyle;
	private JTextPane textPanel;
	private Logger logger;
	private StyledDocument doc;
	private int lines = 0;
	private int bufferSize;
	
	//to remove it when new logger is set
	private Handler handler;
	
	public TerminalOutput(Logger logger) {
		this();
		this.setLogger(logger);
	}
	
	public TerminalOutput(Logger logger, int bufferSize) {
		this(bufferSize);
		this.setLogger(logger);
	}
	
	public TerminalOutput() {
		this(Color.WHITE, Color.RED, Color.BLACK);
	}

	public TerminalOutput(Color defaultColor, Color errColor ,Color backgroundColor) {
		this(128,Color.WHITE, Color.RED, Color.BLACK);
	}

	public TerminalOutput(int bufferSize) {
		this(bufferSize,Color.WHITE, Color.RED, Color.BLACK);
	}

	public TerminalOutput(int bufferSize, Color defaultColor, Color errColor ,Color backgroundColor) {
		this(null,bufferSize,defaultColor,errColor,backgroundColor);
	}
	
	public TerminalOutput(Logger logger, int bufferSize, Color defaultColor, Color errColor ,Color backgroundColor) {
		if(bufferSize < 1) throw new NumberFormatException("bufferSize must be >= 1");
		this.setBufferSize(bufferSize);
		this.handler = new outputHandler();
		this.setLogger(logger);
		textPanel = new JTextPane();
		textPanel.setEditable(false);
		textPanel.setBackground(backgroundColor);
		defaultStyle = textPanel.getStyle("default");
		StyleConstants.setForeground(defaultStyle, defaultColor);
		errStyle = textPanel.addStyle("errStyle", defaultStyle);
		StyleConstants.setForeground(errStyle, errColor);
		doc = (StyledDocument)textPanel.getDocument();

		getViewport().add(textPanel);
	}

	public void syso(String msg) {
		write(msg,defaultStyle);
	}

	public void syserr(String msg) {
		write(msg,errStyle);
	}

	public void sysoln(String msg) {
		syso(msg+"\n");
	}

	public void syserrln(String msg) {
		syserr(msg+"\n");
	}

	protected void write(String msg, Style s) {
		try {
			doc.insertString(doc.getEndPosition().getOffset(), msg, s);
			lines += getLines(msg);

			int length = 0;
			for(int i = 0; i< lines - bufferSize;i++) {
				try {
					length += doc.getText(length, doc.getLength()-(length-1)).indexOf("\n")+1;
				} catch (BadLocationException e) {
					System.err.println("Error while finding length () : "+e.getMessage());
					break;
				} 
			}

			if(length > 0) {
				doc.remove(0, length);
				lines = bufferSize;
			}

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					getVerticalScrollBar().setValue(getVerticalScrollBar().getMaximum());
				}
			});

		} catch (BadLocationException e) {
			System.err.println("Error while writing message : "+e.getMessage());
		}
	}

	protected int getLines(String msg)  {
		Matcher m = newLine.matcher(msg);
		int count = 0;
		while (m.find()){
			count +=1;
		}
		return count;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getLines() {
		return lines;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		if(this.logger != null) this.logger.removeHandler(handler);
		this.logger = logger;
		this.logger.addHandler(handler);
	}

	public Handler getHandler() {
		return handler;
	}

	public Style getDefaultStyle() {
		return defaultStyle;
	}

	public Style getErrStyle() {
		return errStyle;
	}

}
