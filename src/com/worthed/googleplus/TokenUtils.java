package com.worthed.googleplus;

public class TokenUtils {

	public TokenUtils() {
		// TODO Auto-generated constructor stub
	}

	// http://worthed.com/#access_token=1422018522.f55ddc4.81a550ee1d8b4b13866da1d3a106f055
	
	private static String KEY_TOKEN = "access_token=";
	private static String KEY_CODE = "code=";
	private static String KEY_ERROR = "error=";
	
	public static String getToken(String url) {
		return getParm(url, KEY_TOKEN);
	}
	
	public static String getCode(String url) {
		return getParm(url, KEY_CODE);
	}
	
	public static String getError(String url) {
		return getParm(url, KEY_ERROR);
	}
	
	private static String getParm(String url, String key) {
		if (url.contains(key)) {
			int start = url.indexOf(key) + key.length();
			int end = start;
			String lastStr = url.substring(start);
			char[] lastChars = lastStr.toCharArray();
			for (int i = 0; i < lastChars.length; i++) {
				if (lastChars[i] == '&') {
					end += i;
					break;
				}
			}
			return url.substring(start, end);
		} 
		return null;
	}
	
}
