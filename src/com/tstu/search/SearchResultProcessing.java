package com.tstu.search;

import java.util.ArrayList;
import java.util.List;
public class SearchResultProcessing {
	
	public static List<String> getUrlsFromSearchResult(String html, int maxSearchResults) {

		List<String> urls = new ArrayList<String>();
		int startIndex;
		String url = "";
		for (int i = 0; i != maxSearchResults; i++) {
			startIndex = html.indexOf("<h3 class=\"r\"><a href=\"") + 23;
			if (startIndex == -1)
				break;
			while (html.charAt(startIndex) != '\"') {
				url += html.charAt(startIndex);
				startIndex++;
			}
			if (url.indexOf("http") == 0) {
				urls.add(url);
			}
			url = "";
			html = html.substring(startIndex);
		}

		return urls;
	}
}
