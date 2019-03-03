import java.awt.Point;

public class Word {
		public static final int DOWN = 0;
		public static final int ACROSS = 1;

		private Point start;
		private String hint;
		private String word;
		private int number;
		private int direction;
		private String attempt = "";

		public Word(String descriptions, String words, Point starts, int numbers, int directions) {
			word = words;
			hint = descriptions;
			start = starts;
			number = numbers;
			direction = directions;
		}

		public Point getStart() {
			return start;
		}

		public String getHint() {
			return hint;
		}

		public String getWord() {
			return word;
		}

		public int getNumber() {
			return number;
		}

		public int getDirection() {
			return direction;
		}

		public int getLength() {
			return word.length();
		}

		public void setAttempt(String attempts) {
			attempt = attempts;
		}

		public String getAttempt() {
			return attempt;
		}
	}

