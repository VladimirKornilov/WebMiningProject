package com.tstu.htmlProcessing;

public class HtmlStringProcessing {
	private String stringToProcess;

	public HtmlStringProcessing() {
	}

	public HtmlStringProcessing(String stringToProcess) {
		this.stringToProcess = stringToProcess;
	}

	public String getStringWithoutTags() {
		String line = "";
		Boolean tagOpened = false;
		for (int i = 0; i != stringToProcess.length(); i++) {
			if (stringToProcess.charAt(i) == '<') {
				tagOpened = true;
			} else if (stringToProcess.charAt(i) == '>') {
				tagOpened = false;
			} else if (!tagOpened) {
				line += stringToProcess.charAt(i);
			}
		}
		return line;
	}

	public String getIndent(int k) {
		String indent = "";
		int i = 0;
		while (i != k) {
			indent += " ";
			i++;
		}
		return indent;
	}

	public String getStringToProcess() {
		return stringToProcess;
	}

	public void setStringToProcess(String stringToProcess) {
		this.stringToProcess = stringToProcess;
	}
}
