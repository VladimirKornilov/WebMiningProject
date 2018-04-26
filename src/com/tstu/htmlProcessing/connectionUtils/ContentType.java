package com.tstu.htmlProcessing.connectionUtils;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import com.tstu.io.Controller;

public class ContentType {

	private String contentType = null;
	private String charSet = null;
	private String type = null;

	private URLConnection connection;
	private URL url;

	public ContentType(URL url) {
		this.url = url;
		connection = null;
		try {
			connection = url.openConnection();
			contentType = connection.getContentType();
		} catch (IOException e) {
			Controller.window.addToLogs("Connection to " + url + " Error!");
			Controller.exceptionsLog.add("Connection to " + url + " Error!");
		}
	}

	public ContentType(URLConnection connection) {
		this.connection = connection;
		url = connection.getURL();
		contentType = connection.getContentType();
	}

	public ContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}

	public String getCharSet() {
		if(charSet == null) {
			if(contentType != null && contentType.contains("="))
				charSet = contentType.split("=")[1];
		}	
		return charSet;
	}

	public String getType() {
		if(type==null) {
			if(contentType!=null)
				type = contentType.split(";")[0];
		}	
		return type;
	}
	public URLConnection getConnection() {
		return connection;
	}

	public URL getUrl() {
		return url;
	}

}
