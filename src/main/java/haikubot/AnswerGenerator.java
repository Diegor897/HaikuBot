package haikubot;

import haikubot.extra.Pair;

public final class AnswerGenerator {

	private static AnswerGenerator _instance;
	
	private AnswerGenerator() {
		// EMPTY
	}
	
	public static AnswerGenerator get() {
		if (_instance == null) {
			_instance = new AnswerGenerator();
		}
		
		return _instance;
	}
	
	public Pair<String, Boolean> generate(String text, boolean mentioned) {
		String s = "";
		boolean b = mentioned;
		
		if (text == null || text.equals("")) {
			s = "Tu Tweet no es un Haiku.";
		} else {
			s = "¡Enhorabuena tu Tweet es un Haiku!\n\n" + text;
			b = true;
		}
		
		return new Pair<>(s, b);
	}
}