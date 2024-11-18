package haikubot;

import java.util.ArrayList;
import java.util.List;

import haikubot.extra.Syllables;

public final class HaikuRecognizer {
	
	private static final String _botHandle = "@HaikuBotUCM";
	
	private static HaikuRecognizer _instance;
	
	private static final int[] _numSyl = {5, 7, 5};
	
	private HaikuRecognizer() {
		// EMPTY
	}
	
	public static HaikuRecognizer get() {
		if (_instance == null) {
			_instance = new HaikuRecognizer();
		}
		
		return _instance;
	}
	
	public String analize(String text) {
		int counter = 0;
		int ind = 0;
		int sentence = 0;
		String haiku = "\"";
		String word = "";
		List<String> words = new ArrayList<>();
		boolean loopWords = true;
		boolean loopChars = true;
		
		for (String w : text.replace("\n", " ").replace(_botHandle, "").split("\\s+")) {
			if (!w.equals("")) {
				words.add(w);
			}
		}
		
		for (int i = ind; i < words.size(); i++) {
			if (loopWords) {
				word = words.get(i);
				
				haiku += word + " ";
				counter += Syllables.numSyllables(word);
				
				if (counter > _numSyl[sentence]) {
					i = ind;
					ind++;
					haiku = "\"";
					counter = 0;
					sentence = 0;
					
				} else if (counter == _numSyl[sentence]) {
					sentence++;
					counter = 0;
					
					haiku += "\n";
					
					if (sentence == 3) {
						loopWords = false;
					}
				}
			}
		}
		
		if (sentence < 3) {
			haiku = "";
			
		} else {
			for (int i = haiku.length() - 1; i > 0; i--) {
				if (loopChars) {
					Character c = haiku.charAt(i);
					
					if (!(c.equals(' ') || c.equals('\n'))) {
						haiku = haiku.substring(0, i + 1) + "\"";
						
						loopChars = false;
					}
				}
			}
		}
		
		return haiku;
	}
}