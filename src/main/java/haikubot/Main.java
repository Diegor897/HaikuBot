package haikubot;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {
	
	private static final String _queryPath = "query.txt";
	
	private static boolean _once;
	
	private static boolean _recursive;
	
	private static int _results;
	
	private static boolean _recent;
	
	private static boolean _mentioned;
	
	public static void main(String[] args) {		
		try {
			parseArgs(args);
			
			List<String> l = new ArrayList<>();
			FileManager.load(_queryPath, l);
			
			TweetAnalizer.build(l.get(0), _once, _recursive, _results, _recent, _mentioned).run();
			
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		}
	}
	
	private static void parseArgs(String[] args) throws ParseException {
		Options options = buildOptions();
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		
		parseOnce(cmd);
		parseRecursive(cmd);
		parseResults(cmd);
		parseMode(cmd);
	}
	
	private static Options buildOptions() {
		Options options = new Options();

		options.addOption(new Option("o", "once", true, "Do Haiku recognition once"));
		options.addOption(new Option("r", "recursive", true, "Paginate through all of the available retrieved tweets"));
		options.addOption(new Option("n", "num", true, "Number of tweets to fetch by page"));
		options.addOption(new Option("m", "mode", true, "Types of tweet retrieving methods"));

		return options;
	}
	
	private static void parseOnce(CommandLine line) {
		String s = line.getOptionValue('o');
		
		if (s != null) {
			_once = s.equals("t");
		} else {
			_once = true;
		}
	}
	
	private static void parseRecursive(CommandLine line) {
		String s = line.getOptionValue('r');
		
		if (s != null) {
			_recursive = s.equals("t");
		} else {
			_recursive = false;
		}
	}
	
	private static void parseResults(CommandLine line) {
		String s = line.getOptionValue('n');
		
		if (s != null) {
			_results = Integer.valueOf(s);
		} else {
			_results = 100;
		}
	}
	
	private static void parseMode(CommandLine line) {
		String s = line.getOptionValue('m');
		
		switch (s) {
		case "all":
			_recent = true;
			_mentioned = false;
			break;
		case "mentions":
			_recent = false;
			_mentioned = true;
			break;
		default:
			_recent = true;
			_mentioned = true;
			break;
		}
	}
}