package xyz.ichiidev.scpterminal.utils.functions;

import org.json.JSONArray;
import org.json.JSONObject;
import xyz.ichiidev.scpterminal.utils.Constants;
import xyz.ichiidev.scpterminal.utils.data_types.User;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class APIUtils {

	public static String getReport(String token, String report_name) {
		try {
			URL url = new URL(Constants.api_url + "?endpoint=get_report&token=" + token + "&file_name=" + report_name);
			URLConnection urlc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlc.getInputStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				JSONObject json = JSON.parse(inputLine);
				try {
					int err = json.getInt("error");
					if (err == 400 || err == 500) return "error";
					if (err == 403) return "insufficient_permissions";
					if (err == 404) return "unknown_report";
				} catch (Exception e) {
					URL u = new URL(json.getJSONObject("report_data").getString("file_name"));
					BufferedReader br = new BufferedReader(new InputStreamReader(u.openStream()));
					String line;
					StringBuilder sb = new StringBuilder();
					while ((line = br.readLine()) != null) sb.append(line).append("\n");
					return sb.toString();
				}
			}
		} catch (Exception e) {e.printStackTrace();}
		return "error";
	}

	public static User getUser(String token, String username) {
		User user = new User(null, 0, null, null);
		try {
			URL url = new URL(Constants.api_url + "?endpoint=get_user&token=" + token + "&username=" + username);
			URLConnection urlc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlc.getInputStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				JSONObject json = JSON.parse(inputLine);
				try {
					int err = json.getInt("error");
					if (err == 400 || err == 500) return new User("error", 0, null, null);
					if (err == 403) return new User("insufficient_permissions", 0, null, null);
					if (err == 404) return new User("unknown_user", 0, null, null);
				} catch (Exception e) {
					user.username = json.getJSONObject("user_data").getString("username");
					user.permission = json.getJSONObject("user_data").getInt("permission");
					user.full_name = json.getJSONObject("user_data").getString("full_name");
					user.rank = json.getJSONObject("user_data").getString("rank");
					return user;
				}
			}
		} catch (Exception e) {e.printStackTrace();}
		user.username = "error";
		return user;
	}

	public static ArrayList<String> getReportsList(String token) {
		ArrayList<String> names_list = new ArrayList<String>();
		try {
			URL url = new URL(Constants.api_url + "?endpoint=list_reports&token=" + token);
			URLConnection urlc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlc.getInputStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				JSONObject json = JSON.parse(inputLine);
				try {
					int err = json.getInt("error");
					if (err == 400 || err == 500) names_list.add("error");
					if (err == 404) names_list.add("no_report");
					if (err == 403) names_list.add("unknown_token");
					return names_list;
				} catch (Exception e) {
					JSONArray names = json.getJSONArray("reports");
					for(Object o: names){
						if ( o instanceof String ) {
							names_list.add(o.toString());
						}
					}
					return names_list;
				}
			}
		} catch (Exception e) {e.printStackTrace();}
		names_list.add("error");
		return names_list;
	}

	public static ArrayList<User> getUsersList(String token) {
		ArrayList<User> users = new ArrayList<User>();
		try {
			URL url = new URL(Constants.api_url + "?endpoint=list_users&token=" + token);
			URLConnection urlc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlc.getInputStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				JSONObject json = JSON.parse(inputLine);
				try {
					int err = json.getInt("error");
					if (err == 400 || err == 500) users.add(new User("error", 0, null, null));
					if (err == 404) users.add(new User("no_users", 0, null, null));
					if (err == 403) users.add(new User("unknown_token", 0, null, null));
				} catch (Exception e) {
					JSONArray usersArr = json.getJSONArray("users");
					for (Object o: usersArr) {
						if (o instanceof JSONObject) {
							users.add(new User(((JSONObject) o).getString("username"), ((JSONObject) o).getInt("permission"), ((JSONObject) o).getString("full_name"), ((JSONObject) o).getString("rank")));
						}
					}
					return users;
				}
			}
		} catch (Exception e) {e.printStackTrace();}
		users.add(new User("error", 0, null, null));
		return users;
	}
}
