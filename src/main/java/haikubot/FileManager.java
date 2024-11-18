package haikubot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public final class FileManager {

	private FileManager() {
		// EMPTY
	}
	
	public static void appendToFile(String text, String path) throws IOException {
		String s = "\n" + text;
		
	    Files.write(Paths.get(path), s.getBytes(), StandardOpenOption.APPEND);
	}
	
	public static void load(String path, List<String> list) throws IOException {
		try(BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line = br.readLine();

		    while (line != null) {
		    	list.add(line);
		        line = br.readLine();
		    }
		}
	}
}