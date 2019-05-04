package cn.compscosys.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

public class LoggerConfigureReader {
	private static String configPath = "." + File.separator + "resources" + File.separator + "log4j2.xml";
	public static Logger logger = LoggerConfigureReader.loggerSetup();
	
	public static final String setConfigPath(String configPath) {
		LoggerConfigureReader.configPath = configPath;
		return LoggerConfigureReader.configPath;
	}
	
	public static final Logger loggerSetup() {
		Logger logger = null;
		logger = loggerSetup(logger);
		return logger;
	}
	
	public static final Logger loggerSetup(Logger logger) {
		try {
			File file = new File(LoggerConfigureReader.configPath);
			ConfigurationSource source = new ConfigurationSource(new FileInputStream(file), file);
			Configurator.initialize(null, source);
			logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		return logger;
	}
}
