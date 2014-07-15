/**
 * Copyright 2014 Zhenguo Jin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.worthed.googleplus;

import com.google.gson.annotations.Expose;

/**
 * @author jingle1267@163.com
 * 
 */
public class GoogleCode {

	@Expose
	private String device_code;
	@Expose
	private String user_code;
	@Expose
	private String verification_url;
	@Expose
	private int expires_in;
	@Expose
	private int interval;

	public GoogleCode(String device_code, String user_code,
			String verification_url, int expires_in, int interval) {
		super();
		this.device_code = device_code;
		this.user_code = user_code;
		this.verification_url = verification_url;
		this.expires_in = expires_in;
		this.interval = interval;
	}

	public String getDevice_code() {
		return device_code;
	}

	public void setDevice_code(String device_code) {
		this.device_code = device_code;
	}

	public String getUser_code() {
		return user_code;
	}

	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}

	public String getVerification_url() {
		return verification_url;
	}

	public void setVerification_url(String verification_url) {
		this.verification_url = verification_url;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

}
