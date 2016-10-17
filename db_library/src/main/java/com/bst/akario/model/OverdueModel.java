package com.bst.akario.model;

import java.io.Serializable;

/**
 * 
 * @author will.Wu
 * 
 */
public class OverdueModel implements Serializable {

	private static final long serialVersionUID = 3263819407299425224L;

	private long getTime;
	private long maxAge;
	private String url;

	public OverdueModel(long getTime, long maxAge, String url) {
		super();
		this.getTime = getTime;
		this.maxAge = maxAge;
		this.url = url;
	}

	public long getGetTime() {
		return getTime;
	}

	public void setGetTime(long getTime) {
		this.getTime = getTime;
	}

	public long getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(long maxAge) {
		this.maxAge = maxAge;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
