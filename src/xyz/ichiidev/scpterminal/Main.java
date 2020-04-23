package xyz.ichiidev.scpterminal;

import xyz.ichiidev.scpterminal.utils.Core;
import xyz.ichiidev.scpterminal.utils.Startup;
import xyz.ichiidev.scpterminal.utils.data_types.User;
import xyz.ichiidev.scpterminal.utils.functions.ConsoleUtils;

import java.io.IOException;
import java.io.PrintStream;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
		System.setOut(new PrintStream(System.out, true, "UTF-8"));
		ConsoleUtils.clearConsole();
		if (!(Startup.boot().equals("ready"))) return;
		ConsoleUtils.clearConsole();
		System.out.println("Bienvenue sur le terminal francophone de la Fondation SCP !\nN0US T3NONS à RAPPElER QUE TOUT @CCèS fRAUDULEUX à NOTRE BASE DE DONN2E EST PASSIBLE D'UNE PEINE d4EMPRISONNEMENT à VIE.\n\n\nSciPNET>> Connexion établie ! Redirection de la connexion !");
		User user = Startup.apiAuthenticate();
		if (user.token.equals("wrong_pass") || user.token.equals("APIError")) {Thread.sleep(2000); return;}
		ConsoleUtils.clearConsole();
		Core core = new Core();
		core.execute(user);
		Thread.sleep(2000);
	}
}
