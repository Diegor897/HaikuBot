package haikubot.extra;

public final class Syllables {

	private static final Character[] _vowels = {'a', 'e', 'i', 'o', 'u', 'á', 'é', 'í', 'ó', 'ú', 'ü'};
	
	private static final Character[] _openVowels = {'a', 'e', 'o'};

	private static final Character[] _tildeClosed = {'í', 'ú'};
	
	private static final Character[] _punctuation = {'.', ';', ',', '!', '¡', '?', '¿', ':'};
	
	private static final String[] _consonantGroups = {
			"ps",
			"ll",
			"ch",
			"rr",
			"bl",
			"gl",
			"fl",
			"cl",
			"pl",
			"br",
			"gr",
			"fr",
			"cr",
			"pr",
			"dr",
			"tr"
	};
	
	private Syllables() {
		// EMPTY
	}
	
	public static int numSyllables(String word) {
		word = word.toLowerCase();
		int length = word.length();
		int numVowels = 0;
		int result = 1;
		
		if (isIn(word.charAt(length - 1), _punctuation)) {
			word = word.substring(0, length - 1);
			
			length--;
		}

		if (length > 1) {
			for (int i = 0; i < length - 1; i++) {
				Character c = word.charAt(i);
				Character next = word.charAt(i + 1);
				
				if (isIn(c, _vowels)) {
					numVowels++;
					
					if ((!isIn(next, _vowels) && (i + 1 < length - 1))
							|| (numVowels == 3)
							|| (isHiato(c, next))) {
						
						if (!((i == length - 3) && (!isIn(next, _vowels) && (!isIn(word.charAt(i + 2), _vowels))))) {
							result++;
						}
					} 
				} else {
					numVowels = 0;
				}
			}
		}
		
		return result;
	}
	
	private static boolean isHiato(Character c0, Character c1) {
		return c0.equals(c1) || 
				(isIn(c0, _openVowels) && isIn(c1, _openVowels)) ||
				(isIn(c0, _tildeClosed) && isIn(c1, _openVowels)) ||
				(isIn(c0, _openVowels) && isIn(c1, _tildeClosed));
	}
	
	private static boolean isConsonantGroups(Character c0, Character c1) {
		if (!isIn(c0, _vowels) && !isIn(c1, _vowels)) {
			String consonants = "" + c0 + c1;
			
			return isIn(consonants, _consonantGroups);
		} else {
			return false;
		}
	}
	
	private static <T> boolean isIn(T e, T[] es) {
		for (T aux : es) {
			if (e.equals(aux)) {
				return true;
			}
		}
		
		return false;
	}
}