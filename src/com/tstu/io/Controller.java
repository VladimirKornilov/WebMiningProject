package com.tstu.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.StringUtils;

import com.tstu.configuration.Config;
import com.tstu.gui.MainWindow;
import com.tstu.htmlProcessing.HtmlCode;
import com.tstu.htmlProcessing.textBlockEntities.HtmlBlock;
import com.tstu.htmlProcessing.textBlockEntities.HtmlBlockType;
import com.tstu.search.GetRequest;
import com.tstu.search.SearchResultProcessing;

public class Controller extends Thread {

	public static MainWindow window;
	public static List<String> exceptionsLog = new ArrayList<>();
	private static CountDownLatch cdl;
	private static Config config;
	private static String absolutePath = null;
	
	//File names for text mining
	private static List<String> fileNames = new ArrayList<String>();
	
	@Override
	public void run() {
		try {
			main();
		} catch (InterruptedException e) {
			window.addToLogs("Interrupted Exception");
			exceptionsLog.add("InterruptedException with thread " + Thread.currentThread());
		}
	}

	public static void main() throws InterruptedException {
		long startTime = System.currentTimeMillis();
		long finishTime;
		double resultTimeSec;
		List<String> stringUrls = new ArrayList<>();
		switch(window.getInputType()){
		case CONFIG:
			config = Config.getInstance(true);
			stringUrls = readFromTxt(config.getUrlsFilePath());
			break;
		case MANUAL:
			config = Config.getInstance(false);
			stringUrls.add(window.getTextField().getText());
			break;
		case SEARCH:
			config = Config.getInstance(false);
			GetRequest get;
			for (int i = 0; i <= config.getCountOfPages(); i++) {
				get = new GetRequest(window.getTextField().getText(), i);
				stringUrls.addAll(SearchResultProcessing.getUrlsFromSearchResult(get.getHtml(), 10));
			}
			break;
		default:
			return;
		}
	
		Thread thread = null;
		cdl = new CountDownLatch(stringUrls.size());
		for (String strUrl : stringUrls) {
			thread = new Thread() {
				@Override
				public void run() {
					try {
						URL url = new URL(strUrl);
						HtmlCode html = new HtmlCode(url);
						writeToTxt(html.getHtml(), config.getHtmlFilePath() + generateNameForFile(strUrl));
						HtmlBlock title = new HtmlBlock(HtmlBlockType.TITLE);
						window.addToLogs("Title: " + title.getBlock(html));
						HtmlBlock headings = new HtmlBlock(HtmlBlockType.HEADINGS);
						writeToTxt(headings.getBlock(html),
								config.getHtmlStructFilePath() + generateNameForFile(strUrl));
						HtmlBlock text = new HtmlBlock(HtmlBlockType.TEXT);
						writeToTxt(text.getBlock(html), config.getHtmlMainTextFilePath() + generateNameForFile(strUrl));
						cdl.countDown();
					} catch (MalformedURLException e) {
						window.addToLogs("Invalid url " + strUrl);
						exceptionsLog.add("Invalid url " + strUrl);
						cdl.countDown();
					} catch (Exception e) {
						window.addToLogs("Unknown exception when reading " + strUrl);
						exceptionsLog.add("Unknown exception when reading " + strUrl);
						cdl.countDown();
					}
				}
			};
			thread.start();
		}
		cdl.await();
		finishTime = System.currentTimeMillis();
		resultTimeSec = ((double)finishTime - (double)startTime)/1000;
		window.addToLogs("\n");
		window.addToLogs("-----------------------------------------------------------------------------");
		window.addToLogs("Processing complete");
		window.addToLogs("Execution time is " + resultTimeSec + " sec");
		if (exceptionsLog.size() == 0) {
			window.addToLogs("There are no exceptions in progress of execution!");
		} else {
			window.addToLogs("There are exceptions in progress of execution:");
			window.addToLogs("\n");
			for (String log : exceptionsLog)
				window.addToLogs(log);
		}
		window.addToLogs("-----------------------------------------------------------------------------");
		Runnable clustering = new Runnable() {

			@Override
			public void run() {
				window.addToLogs("Executing of text clustering");

				String textMiningPath = config.getTextMiningPath();
				if (textMiningPath != null && StringUtils.isNotBlank(textMiningPath)) {
					String fileNamesStr = getFileNamesAsSingleString();
					StringBuffer output = new StringBuffer();
					try {
						File f = new File(textMiningPath);
						String command = "python stage_text_processor.py " + fileNamesStr;
						Process p = Runtime.getRuntime().exec(command, null, f);
						window.addToLogs(command + fileNamesStr);
						p.waitFor();
						BufferedReader reader = new BufferedReader(new InputStreamReader(p.getErrorStream()));

						String line = "";
						while ((line = reader.readLine()) != null) {
							output.append(line + "\n");
						}
						window.addToLogs(output.toString());
						window.addToLogs("Clustering complete!");
					} catch (IOException | InterruptedException e) {
						window.addToLogs("Exception with running clustering algorithm!");
						e.printStackTrace();
					}
				}
			}
		};
		clustering.run();
	}
	
	private static String getFileNamesAsSingleString() {
		String result = "";
		for(int i = 0; i != fileNames.size();i++) {
			if(i != 0)
				result+= ';' + fileNames.get(i);
			else result += fileNames.get(i);
		}
		return result;
	}
	
	public static synchronized void writeToTxt(List<String> source, String path) {
		if (source != null && !source.isEmpty()) {
			String directory = path.substring(0, (path.lastIndexOf('/') + 1));
			File folder = new File(directory);
			if (!folder.exists())
				if (folder.mkdirs())
					window.addToLogs("Directory " + directory + "created successfully!");
			try (Writer fw = new OutputStreamWriter(new FileOutputStream(path, false),StandardCharsets.UTF_8)) {
				for (String str : source) {
					if (str != null) {
						fw.write(str);
						fw.write("\r\n");
					}
				}
				if(path.contains(config.getHtmlMainTextFilePath())) {
					String fileName = path.substring(path.lastIndexOf('/'), path.length());
					absolutePath = folder.getAbsolutePath();
					fileNames.add(absolutePath + fileName);
				}
			} catch (FileNotFoundException e) {
				window.addToLogs("Could not create a txt file " + directory);
				exceptionsLog.add("Could not create a txt file " + directory);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				window.addToLogs("Cannot write to " + path + "!");
				exceptionsLog.add("Cannot write to " + path + "!");
			}
		}
	}
	
	public static void writeGoogleToTxt(String text, String path) {
		try (Writer fw = new OutputStreamWriter(new FileOutputStream(path, false),StandardCharsets.UTF_8)) {
			fw.write(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static synchronized List<String> readFromTxt(String path) {
		List<String> result = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			String line = "";
			while ((line = reader.readLine()) != null) {
				result.add(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			window.addToLogs("File " + path + " not found!");
			exceptionsLog.add("File " + path + " not found!");
		} catch (IOException e) {
			window.addToLogs("Cannot read " + path + "!");
			exceptionsLog.add("Cannot read " + path + "!");
		}
		return result;
	}

	private static String generateNameForFile(String url) {
		int position = 0;
		String result = url;
		try {
			position = result.indexOf("//") + 2;
			if (position == -1)
				position = result.indexOf("\\") + 2;
			if (position == -1 && position == 0)
				throw new IOException();
			if (result.length() > 200)
				result = result.substring(0, 200);
		} catch (IOException e) {
			window.addToLogs("Error in url " + url);
			exceptionsLog.add("Error in url " + url);
		}
		return result.substring(position).replace('/', '%') + ".txt";
	}
}
