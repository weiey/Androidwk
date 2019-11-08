package com.weiey.app;


public class AppConfig {
	public static final boolean DEBUG = BuildConfig.DEBUG;
	/**
	 * SP保存环境配置键
	 */
	public static final String URL_CONFIG = "urlConfig";

	/***
	 * app接口接口切换配置
	 * 0 debug环境
	 * 1 release环境
	 * 2 dev环境
	 ***/
	public static final int APP_EVN = BuildConfig.APP_EVN;
}
