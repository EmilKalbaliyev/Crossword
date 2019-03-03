import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

class Crossword {
	private ArrayList words = new ArrayList();
	private Dimension size = new Dimension(0, 0);

	public Crossword(Word[] word) {
		for (int j = 0; j < word.length; j++)
			words.add(word[j]);
		calculateSize();
	}

	public Dimension getSize() {
		return size;
	}

	private void calculateSize() {
		size = new Dimension(12, 12);
	}

	Iterator getWords() {
		return words.iterator();
	}

	Word getWord(Point point, Word exst) {
		boolean f = false;
		Iterator words = getWords();
		while (words.hasNext()) {
			Word word = (Word) words.next();
			if (word.getDirection() == Word.ACROSS) {
				if (point.y == word.getStart().y && point.x >= word.getStart().x
						&& point.x <= word.getStart().x + word.getLength()) {
					f = true;
					if (word != exst)
						return word;
				}
			} else if (point.x == word.getStart().x && point.y >= word.getStart().y
					&& point.y <= word.getStart().y + word.getLength()) {
				f = true;
				if (word != exst)
					return word;
			}
		}
		return f ? exst : null;
	}
}