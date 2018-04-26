package com.tstu.htmlProcessing;

import java.util.ArrayList;
import java.util.List;

import com.tstu.htmlProcessing.textBlockEntities.HtmlBlock;
import com.tstu.io.Controller;

public class HTMLProcessing implements Runnable{
	private HtmlCode htmlCode;

	private HtmlBlock htmlBlock;
	
	private List<String> blockByTags;

	public HTMLProcessing() {
	}

	public HTMLProcessing(HtmlCode htmlCode, HtmlBlock htmlBlock) {
		this.htmlCode = htmlCode;
		this.htmlBlock = htmlBlock;
	}

	public void processBlock() {
		List<String> resultBlock = new ArrayList<String>();
		String row = "";
		String indent = "";
		HtmlStringProcessing htmlProc = new HtmlStringProcessing();
		Boolean partOfNeededBlockDetect = false;
		String[] openTags = htmlBlock.getOpenTag();
		String[] closeTags = htmlBlock.getCloseTag();
		int countOfTags = openTags.length;
		Boolean tagClosed = false;
		for (String line : htmlCode.getHtml()) {
			if (line != null) {
				for (int i = 0; i != countOfTags; i++) {
					if(tagClosed)
						partOfNeededBlockDetect = false;
					indent = htmlProc.getIndent(i);
					if ((line.contains(openTags[i] + " ") || line.contains(openTags[i] + ">"))
							&& (line.contains(closeTags[i]))) {
						partOfNeededBlockDetect = true;
						tagClosed=true;
						break;
					} else {
						if (line.contains(openTags[i] + " ") || line.contains(openTags[i] + ">")) {						
							partOfNeededBlockDetect = true;
							tagClosed = false;
							break;
						}
						if (line.contains(closeTags[i])) {
							partOfNeededBlockDetect = true;
							tagClosed = true;
							break;
						}
					}
				}
				if (partOfNeededBlockDetect) {
					row+=line+" ";
					htmlProc.setStringToProcess(row);
					row = htmlProc.getStringWithoutTags();
					if(!(row == null || row == ""||row==" "||row=="\r\n"))
						resultBlock.add(indent + row);
					row = "";
					if(tagClosed)
						partOfNeededBlockDetect = false;
				}
			}
		}
		AdditionalProcessing proc = new AdditionalProcessing(resultBlock);
		resultBlock = proc.processText();
		if (resultBlock.isEmpty())
			Controller.window.addToLogs("There is no " + htmlBlock.getType().toString() + " block on this page :'-(");
		else
			Controller.window.addToLogs("Getting " + htmlBlock.getType().toString() + " Block Success!");
		this.blockByTags = resultBlock;
	}
	
	public List<String> getBlockByTags() {
		this.run();
		return this.blockByTags;
	}
	@Override
	public void run() {
		processBlock();
	}

}
