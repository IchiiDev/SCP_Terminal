package xyz.ichiidev.scpterminal.utils;

import org.json.JSONException;
import org.json.JSONObject;
import xyz.ichiidev.scpterminal.Main;
import xyz.ichiidev.scpterminal.utils.data_types.User;
import xyz.ichiidev.scpterminal.utils.functions.JSON;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class Startup {
	public static String boot() throws IOException {
		System.out.println("Connection to ichiidev.xyz database");
		try{
			URL url = new URL(Constants.api_url + "?endpoint=status");
			URLConnection urlc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlc.getInputStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				JSONObject json = JSON.parse(inputLine);
				if (json.getString("status") == "maintenance") {
					System.out.println("Mode maintenance activé. API non-accessible par le client");
					return "error";
				}
				if (json.getString("status") == "closed") {
					System.out.println("API Fermée, le client vas s'éteindre");
					return "error";
				}
				if (!(json.getJSONObject("required_client").getString("required").equals(Constants.client_version))) {
					if (!(json.getJSONObject("required_client").getString("warn").equals(Constants.client_version))) {
						System.out.println("Version de client trop ancienne, telechargez la version plus récente sur https://github.com/IchiiSama/SCP_Terminal/releases/");
						return "outdated_client";
					}
					else return "warn";
				}
				return "ready";
			}
			in.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return "default";
	}

	public static User apiAuthenticate() throws IOException {
		String token;
		Console terminal = System.console();
		System.out.println("Utilisateur:");
		String auth = terminal.readLine();
		System.out.println("Mot de passe:");
		String password = new String(terminal.readPassword());
		System.out.println("Authentification en cours...");;
		try {
			URL url = new URL(Constants.api_url + "?endpoint=connect&auth=" + auth + "&password=" + password);
			URLConnection urlc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(urlc.getInputStream(), StandardCharsets.UTF_8));

			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				JSONObject json = JSON.parse(inputLine);
				try {
					int err = json.getInt("error");
					if (err == 500) return new User("APIError", null, 0);
					if (err == 403) {
						System.out.println("\n\nUtilisateur ou Mot de passe incorrect ! Deconnexion en cours !");
						return new User("wrong_pass", null, 0);
					}
				} catch(JSONException e) { return new User(json.getJSONObject("connection_data").getString("token"), json.getJSONObject("connection_data").getString("username"), json.getJSONObject("connection_data").getInt("permission_level")); }
			}
		}
		catch(Exception e) { e.printStackTrace(); }
		return new User(null, null, 0);
	}
}
