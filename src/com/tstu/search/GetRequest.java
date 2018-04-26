package com.tstu.search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class GetRequest {
	private String html;
	private String userRequest;
	private int page;
	
	public GetRequest(String userRequest, int page) {
		this.userRequest = userRequest;
		this.page = page;
		createRequest();
	}
	private void createRequest() {
		try {
			final String googleUrl = "https://www.google.com/search?q=";
			formatRequest();
			URL u;
			if (page == 0) {
				u = new URL(googleUrl + userRequest + "&amp;sourceid=chrome&amp;gws_rd=ssl");
			} else {
				u = new URL(googleUrl + userRequest + "&amp;sourceid=chrome&amp;gws_rd=ssl&start=" + (10*page));
			}
			HttpURLConnection connection = (HttpURLConnection) u.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

			StringBuffer sb = new StringBuffer();
			char[] buf = new char[1024];
			int x;
			while (true) {
				x = reader.read(buf);
				if (x == -1) {
					reader.close();
					break;
				}
				if (x == 1024)
					sb.append(buf);
				if (x < 1024)
					sb.append(buf, 0, x);
			}
			this.setHtml(sb.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void formatRequest() {
		userRequest = userRequest.replaceAll("[\\s]{2,}", " ");
		userRequest = userRequest.replaceAll(" ","+");
	}
	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}
	public String getUserRequest() {
		return userRequest;
	}
}
