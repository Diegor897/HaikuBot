package haikubot;

import java.util.ArrayList;
import java.util.List;

import haikubot.extra.Pair;
import io.github.redouane59.twitter.dto.tweet.TweetV2;

public final class TweetAnalizer {
	
	private static final String _dontsPath = "lists/donts.txt";
	
	private static final String _bugsPath = "lists/bugs.txt";
	
	private static final String _bugCommand = "!reportbug";
	
	private static final String _dontCommand = "!nohaikus";

	private static TweetAnalizer _instance;
	
	private static TweetGateway _tweetGateway;
	
	private static HaikuRecognizer _haikuRecognizer;
	
	private static AnswerGenerator _answerGenerator;
	
	private static String _query;
	
	private static boolean _recursive;
	
	private static boolean _once;
	
	private static boolean _recent;
	
	private static boolean _mentioned;
	
	private static int _results;
	
	private static long _iniTime;
	
	private static List<String> _dontIds;
	
	private TweetAnalizer(String query, boolean once, boolean recursive, int results, boolean recent, boolean mentioned) throws Exception {
		if (!recent && !mentioned) {
			throw new Exception("Lack of tweet retrieving method specified");
		}
		
		_tweetGateway = TweetGateway.get();
		_haikuRecognizer = HaikuRecognizer.get();
		_answerGenerator = AnswerGenerator.get();
		
		_query = "(" + query + ") lang:es";
		_once = once;
		_recursive = recursive;
		_results = results;
		
		_recent = recent;
		_mentioned = mentioned;
		
		_dontIds = new ArrayList<>();
	}
	
	public static TweetAnalizer build(String query, boolean once, boolean recursive, int results, boolean recent, boolean mentioned) throws Exception {
		if (_instance == null) {
			_instance = new TweetAnalizer(query, once, recursive, results, recent, mentioned);
		}
		
		return _instance;
	}
	
	public static TweetAnalizer get() throws Exception {
		if (_instance == null) {
			throw new Exception("Null TweetAnalizer Instance");
		}
		
		return _instance;
	}
	
	public void run() throws Exception {
		do {
			_iniTime = System.currentTimeMillis();
			FileManager.load(_dontsPath, _dontIds);
			
			if (_recent) {
				List<TweetV2.TweetData> tweetsR = _tweetGateway.getRecentTweets(_query, _recursive, _results);
				
				if (tweetsR != null) {
					for (TweetV2.TweetData tweet : tweetsR) {
						if (!_dontIds.contains(tweet.getAuthorId())) {
							Pair<String, Boolean> aux = _answerGenerator.generate(_haikuRecognizer.analize(tweet.getText()), false);
							
							_tweetGateway.answer(tweet, aux.getFirst(), aux.getSecond());
						}
					}
				}
			}
			
			if (_mentioned) {
				List<Pair<TweetV2.TweetData, TweetV2.TweetData>> tweetsM = _tweetGateway.getMentionedTweets(_recursive, _results);
				
				if (tweetsM != null) {
					for (Pair<TweetV2.TweetData, TweetV2.TweetData> tweetPair : tweetsM) {
						if (!isSpecial(tweetPair.getSecond())) {
							if (!_dontIds.contains(tweetPair.getFirst().getAuthorId())) {
								Pair<String, Boolean> aux = _answerGenerator.generate(_haikuRecognizer.analize(tweetPair.getFirst().getText()), true);
								
								_tweetGateway.answer(tweetPair.getSecond(), aux.getFirst(), aux.getSecond());
							}
						} else {
							FileManager.load(_dontsPath, _dontIds);
						}
					}
				}
			}

			while (!_once && !TimerService.minsPassed(5, _iniTime));
			
		} while (!_once);
	}
	
	private boolean isSpecial(TweetV2.TweetData tweet) throws Exception {
		boolean result = false;
		String text = tweet.getText();
		
		if (text.contains(_bugCommand)) {
			result = true;

			FileManager.appendToFile(text.replace(TweetGateway._botHandle + " " + _bugCommand, ""), _bugsPath);
			
		} else if (text.contains(_dontCommand)) {
			result = true;
			
			FileManager.appendToFile(tweet.getAuthorId(), _dontsPath);
		}
		
		return result;
	}
}
