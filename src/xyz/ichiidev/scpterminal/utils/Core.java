package xyz.ichiidev.scpterminal.utils;

import xyz.ichiidev.scpterminal.utils.data_types.User;
import xyz.ichiidev.scpterminal.utils.functions.APIUtils;
import xyz.ichiidev.scpterminal.utils.functions.ConsoleUtils;

import java.io.*;
import java.util.ArrayList;

public class Core {

	public boolean activated;

	public void execute(User user) throws IOException, InterruptedException {
		this.activated = true;
		System.out.println(ConsoleUtils.readFile("/logo.txt") + "\n\n");
		while (this.activated) commands(user);
	}

	public void commands(User user) throws IOException, InterruptedException {
		Console terminal = System.console();
		String[] command_args = terminal.readLine(user.username + "@SCPFoundation.fr://Terminal>").split(" ");
		switch (command_args[0]) {
			case "close":
				System.out.println("Deconnexion en cours...");
				this.activated = false;
				break;
			case "help":
				System.out.println("Liste des commandes:\n- help: Affichez l'aide\n- close: Fermez le terminal\n- show: Affichez un rapport de la base de donnée\n- clear: Nettoyez la console");
				break;
			case "show":
				String report;
				try {report = command_args[1];} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("[ERROR] Merci de spécifier un rapport à rechercher");
					break;
				}
				String report_text = APIUtils.getReport(user.token, report);
				if (report_text.equals("error")) {System.out.println("Une erreur s'est produite avec la commande, merci de la signaler sur le GitHub (https://github.com/IchiiSama/SCP_Terminal)"); break;}
				if (report_text.equals("unknown_report")) {System.out.println("[ERROR] Le rapport recherché n'existe pas"); break;}
				if (report_text.equals("insufficient_permissions")) {System.out.println("[ERROR] Vous n'avez pas la permission de lire ce rapport"); break;}
				// PrintStream out = new PrintStream(System.out, true, "UTF-8");
				System.out.println(report_text);
				// System.out.println("Ceci est un test effectué pour tester l'encodage UwU (SDszdésds,ddd::!!!è)");
				break;
			case "clear":
				ConsoleUtils.clearConsole();
				execute(user);
				break;
			case "info":
				String username;
				try {username = command_args[1];} catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("[ERROR] Merci de spécifier l'utilisateur à rechercher");
					break;
				}
				User gotUser = APIUtils.getUser(user.token, username);
				if (gotUser.username.equals("error")) {System.out.println("Une erreur s'est produite avec la commande, merci de la signaler sur le GitHub (https://github.com/IchiiSama/SCP_Terminal)"); break;}
				if (gotUser.username.equals("unknown_user")) {System.out.println("[ERROR] L'utilisateur recherché n'existe pas"); break;}
				if (gotUser.username.equals("insufficient_permissions")) {System.out.println("[ERROR] Vous n'avez pas la permission de voir cet utilisateur"); break;}
				StringBuilder msg = new StringBuilder();
				msg.append("Informations de l'utilisateur\n");
				msg.append("Adresse: ").append(gotUser.username).append("@FondationSCP.fr\n");
				msg.append("Nom Complet: ").append(gotUser.full_name).append("\n");
				msg.append("Grade: ").append(gotUser.rank).append("\n");
				msg.append("Accréditation: ").append(gotUser.permission);
				System.out.println(msg.toString());
				break;
			case "list":
				String sub;
				try {sub = command_args[1];} catch (ArrayIndexOutOfBoundsException e) {System.out.println("[ERROR] Merci de spécifier la liste que vous voulez consulter"); break;}
				switch (sub) {
					case "reports":
						ArrayList<String> reportsList = APIUtils.getReportsList(user.token);
						if (reportsList.get(0).equals("error")) {System.out.println("Une erreur s'est produite avec la commande, merci de la signaler sur le GitHub (https://github.com/IchiiSama/SCP_Terminal)"); break;}
						if (reportsList.get(0).equals("no_report")) {System.out.println("[ERROR] Aucun rapport ne vous est accessibles"); break;}
						if (reportsList.get(0).equals("unknown_token")) {System.out.println("[ERROR] Session incorrecte");}
						msg = new StringBuilder();
						msg.append("Liste des rapports accessibles:" + "\n");
						int report_number = 0;
						for (String n: reportsList) {
							msg.append("- ").append(n);
							if (report_number < 2) {msg.append("              "); report_number++;}
							else {msg.append("\n"); report_number = 0;}
						}
						System.out.println(msg.toString());
						break;
					case "users":
						ArrayList<User> users = APIUtils.getUsersList(user.token);
						if (users.get(0).username.equals("error")) {System.out.println("Une erreur s'est produite avec la commande, merci de la signaler sur le GitHub (https://github.com/IchiiSama/SCP_Terminal)"); break;}
						if (users.get(0).username.equals("no_users")) {System.out.println("[ERROR] Aucun rapport ne vous est accessibles"); break;}
						if (users.get(0).username.equals("unknown_token")) {System.out.println("[ERROR] Session incorrecte");}
						msg = new StringBuilder();
						msg.append("Liste des utilisateurs accessibles\n");
						int users_number = 0;
						for (User n: users) {
							msg.append("- ").append(n.username);
							if (users_number < 2) {msg.append("              "); users_number++;}
							else {msg.append("\n"); users_number = 0;}
						}
						System.out.println(msg.toString());
						break;
					default: System.out.println("[ERROR] Cette liste n'existe pas, veuilliez entrer \"user\" ou \"reports\"");
				}
				break;
			default: System.out.println("[ERROR] Commande Incorrecte, veuilliez faire help pour afficher l'aide");
		}
		System.out.println();
	}
}
