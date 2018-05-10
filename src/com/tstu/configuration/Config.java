package com.tstu.configuration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.tstu.io.Controller;

public class Config {

	private static Config instance;
	private String urlsFilePath;
	private String htmlFilePath;
	private String htmlStructFilePath;
	private String htmlMainTextFilePath;
	private String specSymbolsFilePath;
	private String textMiningPath;
	private List<String> config;
	private List<String> specSymbols;
	private int countOfPages = 0;
	private boolean isUrlsPathReadingNeeded;

	public static Config getInstance(boolean isUrlsPathReadingNeeded) {
		if (instance == null)
			instance = new Config(isUrlsPathReadingNeeded);
		return instance;
	}

	private Config(boolean isUrlsPathReadingNeeded) {
		this.isUrlsPathReadingNeeded = isUrlsPathReadingNeeded;
		config = new ArrayList<String>();
		configFileToList();
		configParse();
		fillSpecSymbolsList();
	}

	public List<String> getSpecSymbols() {
		return specSymbols;
	}

	public String getUrlsFilePath() {
		return urlsFilePath;
	}

	public void setUrlsFilePath(String urlsFilePath) {
		this.urlsFilePath = urlsFilePath;
	}

	public String getHtmlFilePath() {
		return htmlFilePath;
	}

	public String getHtmlStructFilePath() {
		return htmlStructFilePath;
	}

	public String getHtmlMainTextFilePath() {
		return htmlMainTextFilePath;
	}

	public int getCountOfPages() {
		return countOfPages;
	}

	public String getTextMiningPath() {
		return textMiningPath;
	}

	private void configParse() {
		try {
			int n = config.size();
			if (n > 0) {
				urlsFilePath = config.get(0);
				if (StringUtils.isBlank(urlsFilePath) && isUrlsPathReadingNeeded)
					throw new IOException();
				if (n > 1) {
					htmlFilePath = config.get(1);
					if (StringUtils.isBlank(htmlFilePath))
						htmlFilePath = "data/html/";
					if (n > 2) {
						htmlStructFilePath = config.get(2);
						if (StringUtils.isBlank(htmlStructFilePath))
							htmlStructFilePath = "data/structures/";
						if (n > 3) {
							htmlMainTextFilePath = config.get(3);
							if (StringUtils.isBlank(htmlMainTextFilePath))
								htmlMainTextFilePath = "data/text/";
							if (n > 4) {
								specSymbolsFilePath = config.get(4);
								if (StringUtils.isBlank(specSymbolsFilePath))
									specSymbolsFilePath = "specSymbols.txt";
								if(n > 5) {
									countOfPages  = Integer.valueOf(config.get(5));
									if(n > 6) {
										textMiningPath = config.get(6);
									}
								}
							}
						}
					}
				}
			}
		} catch (IOException exc) {
			Controller.window.addToLogs("File path to urls is empty!");
			Controller.exceptionsLog.add("File path to urls is empty!");
		}
	}

	private synchronized void configFileToList() {
		try (BufferedReader reader = new BufferedReader(new FileReader("config.txt"))) {
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (!isComment(line))
					config.add(line);
			}
			Controller.window.addToLogs("Configuration reading success!");
		} catch (FileNotFoundException e) {
			Controller.window.addToLogs("Configure file not found!");
			Controller.exceptionsLog.add("Configure file not found!");
		} catch (IOException e) {
			Controller.window.addToLogs(
					"Cannot read configuration file!(It is possible that there are errors in configuration file)");
			Controller.exceptionsLog
					.add("Cannot read configuration file!(It is possible that there are errors in configuration file)");
		} catch (Exception e) {
			Controller.window.addToLogs("Unknown error in configuration file!");
			Controller.exceptionsLog.add("Unknown error in configuration file!");
		}
	}

	private Boolean isComment(String line) {
		Boolean symbolExist = false;
		if (line.charAt(0) == '#') {
			symbolExist = true;
		}
		return symbolExist;
	}

	private void fillSpecSymbolsList() {
		this.specSymbols = Controller.readFromTxt(specSymbolsFilePath);
	}

}
