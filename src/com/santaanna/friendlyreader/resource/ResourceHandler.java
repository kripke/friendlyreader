package com.santaanna.friendlyreader.resource;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

public final class ResourceHandler {
	
	private static ResourceHandler INSTANCE;
	
	private ResourceHandler() {}
	
	private static ResourceHandler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ResourceHandler();
		}
		return INSTANCE;
	}
	
	public static URL getTaggerPath() {
		return getInstance().getUrl("swedish-bidirectional-distsim2.tagger");
	}
	
	public static InputStream getSynonymsPath() {
		return getInstance().getInputStream("synonyms.xml");
	}
	
	private URL getUrl(String fileName) {
		return this.getClass().getResource(fileName);
	}
	
	private InputStream getInputStream(String fileName) {
		return this.getClass().getResourceAsStream(fileName);
	}
	
	public static void main(String[] args) {
		System.out.println(ResourceHandler.getSynonymsPath());
	}
	
}
