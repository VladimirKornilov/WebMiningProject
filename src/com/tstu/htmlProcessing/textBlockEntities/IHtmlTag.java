package com.tstu.htmlProcessing.textBlockEntities;

public interface IHtmlTag {

	String[] OPEN_TAGS_FOR_HEADINGS = { "<h1", "<h2", "<h3", "<h4", "<h5", "<h6" };
	String[] CLOSE_TAGS_FOR_HEADINGS = { "/h1>", "/h2>", "/h3>", "/h4>", "/h5>", "/h6>" };
	String OPEN_TAG_FOR_TITLE = "<title";
	String CLOSE_TAG_FOR_TITLE = "/title>";
	String OPEN_TAG_FOR_PARAGRAPH = "<p";
	String CLOSE_TAG_FOR_PARAGRAPH = "/p>";
	String[] OPEN_TAG_FOR_TEXT = { "<h1", "<h2", "<h3", "<h4", "<h5", "<h6", "<p", "<li" };
	String[] CLOSE_TAG_FOR_TEXT = { "/h1>", "/h2>", "/h3>", "/h4>", "/h5>", "/h6>", "/p>", "/li>" };

	void getTags();
}
