package model;

import java.util.ArrayList;

public class Client_info {

	String username;
	String name;
	String nickname;
	String email;
	String brith;
	String password;
	Object ip_info;

	public ArrayList<String> friend_arr= new ArrayList<>();;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBrith() {
		return brith;
	}

	public void setBrith(String brith) {
		this.brith = brith;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;

	}

	public Object getIp_info() {
		return ip_info;
	}

	public void setIp_info(Object ip_info) {
		this.ip_info = ip_info;
	}

}
