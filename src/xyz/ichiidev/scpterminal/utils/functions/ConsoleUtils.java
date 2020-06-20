package xyz.ichiidev.scpterminal.utils.functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ConsoleUtils {
	public static void clearConsole() throws IOException, InterruptedException {
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	}

	public static String readFile(String filename) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(ConsoleUtils.class.getResourceAsStream(filename), StandardCharsets.UTF_8))) {
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) { sb.append(line).append("\n"); };
			br.close();
			return sb.toString();
		}
	}
}
