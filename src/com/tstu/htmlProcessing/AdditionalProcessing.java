package com.tstu.htmlProcessing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.tstu.configuration.Config;
import com.tstu.io.Controller;

public class AdditionalProcessing {

	private List<String> text;
	private List<String> specSymbols;

	public AdditionalProcessing() {
	}

	public AdditionalProcessing(List<String> text) {
		this.text = text;
	}

	public List<String> processText() {
		specSymbols = Config.getInstance(false).getSpecSymbols();
		removeUnnecessarySpace();
		processEachLine();
		removeSpecSymbols();
		Controller.window.addToLogs("Additional processing success!");
		return text;
	}

	private void removeUnnecessarySpace() {
		Boolean symbolExist = null;
		int numberOfEmptyRows = 0;
		Iterator<String> iter = text.iterator();
		String line = null;
		while (iter.hasNext()) {
			line = iter.next();
			symbolExist = false;
			for (int i = 0; i != line.length(); i++) {
				if (line.charAt(i) != ' ') {
					symbolExist = true;
					numberOfEmptyRows = 0;
					break;
				}
			}
			if (!symbolExist)
				numberOfEmptyRows++;
			if (numberOfEmptyRows > 1) {
				iter.remove();
				removeUnnecessarySpace();
				break;
			}
		}
	}
	
	private void processEachLine() {
		for (String line : text) {
			while (line.contains("  ")) {
				line = line.replace("  ", " ");
			}
		}
	}
	
	private void removeSpecSymbols() {
		List<String> targetText = new ArrayList<>();
		for (String row : text) {
			if(row.contains("&"))
				row = stringWithoutSpecSymbols(row);
			targetText.add(row);
		}
		this.text = targetText;
	}
	
	private String stringWithoutSpecSymbols(String row) {
		for (String symbol : specSymbols) {
			if (row.contains(symbol))
				row = StringUtils.replace(row, symbol," ");
		}
		return row;
	}
	public List<String> getText() {
		return text;
	}

	public void setText(List<String> text) {
		this.text = text;
	}

}
