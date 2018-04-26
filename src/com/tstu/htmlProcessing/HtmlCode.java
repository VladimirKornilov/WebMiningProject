package com.tstu.htmlProcessing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.tstu.htmlProcessing.connectionUtils.ContentType;
import com.tstu.io.Controller;

/**
 * Class for processing HTML Code
 * 
 * @author Vladimir
 *
 */
public class HtmlCode {

	private URL url;
	private String path;
	private List<String> html;
	private String encoding = "UTF-8";
	/**
	 * Create instance of class without parameters
	 */
	public HtmlCode() {
	}

	/**
	 * Get HTML code from url
	 * @param url
	 *            URL address of the readable page
	 * @param path
	 *            Path to the text file that will contain the html code
	 */
	public HtmlCode(URL url) {
		this.url = url;
		getHTMLCode();
	}
	public HtmlCode(String path) throws IOException
	{
		this.path = path;
		getHTMLFromFile();
	}
	public HtmlCode(List<String> htmlCode)
	{
		this.html = htmlCode;
	}

	private void getHTMLCode() {
		html = new ArrayList<String>();
		try {
			determineEncoding();
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),encoding));
			String line = reader.readLine();
			while (line != null) {
				line = reader.readLine();
				html.add(line);
			}
			reader.close();
			Controller.window.addToLogs("Reading HTML from url = " + url + " success!");
		} catch (IOException e) {
			Controller.window.addToLogs("Cannot read HTML from url = " + url);
			Controller.exceptionsLog.add("Cannot read HTML from url = " + url);
		}
		catch (Exception e)
		{
			Controller.window.addToLogs("Unknown Error in HTML from url = " + url);
			Controller.exceptionsLog.add("Unknown Error in HTML from url = " + url);
		}
	}
	
	private void getHTMLFromFile() throws IOException {
		html = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));
			String line = "";
			while ((line = reader.readLine()) != null) {
				html.add(line);
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Controller.window.addToLogs("File with HTML not found!");
			Controller.exceptionsLog.add("File with HTML not found!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Controller.window.addToLogs("Error with loading HTML file!");
			Controller.exceptionsLog.add("Error with loading HTML file!");
		}
		catch(Exception e) {
			Controller.window.addToLogs("Unknown error with HTML!");
			Controller.exceptionsLog.add("Unknown error with HTML!");
		}
		finally {
			reader.close();
		}
	}
	
	private void determineEncoding() {
		ContentType contentType = new ContentType(url);
		String charSet = contentType.getCharSet();
		if(charSet != null)
			encoding = charSet; 
	}
	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}
	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public List<String> getHtml() {
		return html;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setHtml(List<String> html) {
		this.html = html;
	}

}
