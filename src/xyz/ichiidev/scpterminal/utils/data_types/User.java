package xyz.ichiidev.scpterminal.utils.data_types;

public class User {
	public String token;
	public String username;
	public int permission;
	public String full_name;
	public String rank;

	public User(String token, String username, int permission) {
		this.token = token;
		this.username = username;
		this.permission = permission;
	}
	public User(String username, int permission, String full_name, String rank) {
		this.username = username;
		this.permission = permission;
		this.full_name = full_name;
		this.rank = rank;
	}
}
