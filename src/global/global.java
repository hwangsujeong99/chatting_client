package global;

import java.util.concurrent.ConcurrentHashMap;

import model.Client_info;

public class global {

	public static String s_ipaddr = "";
	public static int s_port = 0;

	public static ConcurrentHashMap<String, Client_info> registered_list = new ConcurrentHashMap<>();

}
