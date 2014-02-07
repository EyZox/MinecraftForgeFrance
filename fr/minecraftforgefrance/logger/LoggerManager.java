package fr.minecraftforgefrance.logger;

import java.awt.Dimension;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import fr.minecraftforgefrance.terminal.ReadOnlyTerminal;
import fr.minecraftforgefrance.terminal.TerminalOutput;
import fr.minecraftforgefrance.utils.Date;

public class LoggerManager {
	
	private static class handler extends Handler {
		private PrintWriter pw;
		
		public handler(Path logFile) throws IOException  {
			pw = new PrintWriter(new FileWriter(logFile.toFile()));
		}

		@Override
		public void close() throws SecurityException {
			pw.close();
		}

		@Override
		public void flush() {
			pw.flush();
			
		}

		@Override
		public void publish(LogRecord record) {
			pw.println("<"+Date.getJJMMAAAAHMS()+"> [" + record.getLevel().getLocalizedName() + "] " + record.getMessage());
			
		}
	}
		
	
	private static Logger logger;
	
	public static ReadOnlyTerminal init(Logger logger, String filename) {
		LoggerManager.logger = logger;
		try {
			LoggerManager.logger.addHandler(new handler(Paths.get(filename)));
			return null;
		} catch (IOException e) {
			System.err.println("[WARNING] Unable to create log "+filename+" : "+e.getMessage());
			ReadOnlyTerminal terminal = new ReadOnlyTerminal(filename,new Dimension(300,300), new TerminalOutput(logger));
			return terminal;
		}
	}
	
	public Logger getLogger() {
		return logger;
	}
}