package com.tstu.htmlProcessing.textBlockEntities;

import java.util.ArrayList;
import java.util.List;

import com.tstu.htmlProcessing.HTMLProcessing;
import com.tstu.htmlProcessing.HtmlCode;

public class HtmlBlock implements IHtmlTag {

	private String[] openTag;
	private String[] closeTag;

	private List<String> block;
	private HtmlBlockType type;

	public HtmlBlock() {
	}

	public HtmlBlock(HtmlBlockType type) {
		super();
		this.type = type;
		getTags();
	}

	public String[] getOpenTag() {
		return openTag;
	}

	public void setOpenTag(String[] openTag) {
		this.openTag = openTag;
	}

	public String[] getCloseTag() {
		return closeTag;
	}

	public void setCloseTag(String[] closeTag) {
		this.closeTag = closeTag;
	}

	public List<String> getBlock(HtmlCode html) {
		block = new ArrayList<String>();
		if (block.isEmpty()) {
			HTMLProcessing proc = new HTMLProcessing(html, this);
			block = proc.getBlockByTags();
		}
		return block;
	}

	public HtmlBlockType getType() {
		return type;
	}

	public void setType(HtmlBlockType type) {
		this.type = type;
	}

	@Override
	public void getTags() {
		//inizialization
		switch (type) {
		case HEADINGS:
			openTag = OPEN_TAGS_FOR_HEADINGS;
			closeTag = CLOSE_TAGS_FOR_HEADINGS;
			break;
		case TITLE:
			openTag = new String[1];
			closeTag = new String[1];
			openTag[0] = OPEN_TAG_FOR_TITLE;
			closeTag[0] = CLOSE_TAG_FOR_TITLE;
			break;
		case PARAGRAPH:
			openTag = new String[1];
			closeTag = new String[1];
			openTag[0] = OPEN_TAG_FOR_PARAGRAPH;
			closeTag[0] = CLOSE_TAG_FOR_PARAGRAPH;
		case TEXT:
			openTag = OPEN_TAG_FOR_TEXT;
			closeTag = CLOSE_TAG_FOR_TEXT;
			break;
		default:
			System.out.println("Invalid type!");
			break;

		}
	}
}
