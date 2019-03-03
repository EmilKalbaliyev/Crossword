import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;

class CPanel extends JPanel {
	
	private static final int DIM = 30;
	private static Crossword crossword;
	private Font sfont = new Font("SansSerif", Font.PLAIN, DIM / 3);
	private Font lfont = new Font("SansSerif", Font.PLAIN, DIM - 12);

	private static boolean mShowAnswers = false;
	private Word wrd = null;
	private int post = 0;

	public CPanel(Crossword cross) {
		crossword = cross;

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				requestFocus();
				hitTest(e.getPoint());

			}
		});

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				keyTest(e.getKeyCode(), e.getKeyChar());
			}
		});
	}

	public void showAnswers(boolean showAnswers) {
		mShowAnswers = showAnswers;
		repaint();
	}

	public Dimension getPreferredSize() {
		return new Dimension((crossword.getSize().width * DIM) + 1, (crossword.getSize().height * DIM) + 1);
	}

	public void paint(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, getSize().width, getSize().height);

		Iterator words =crossword.getWords();
		g.setColor(Color.WHITE);
		ArrayList list = new ArrayList();
		while (words.hasNext())
			list.add(drawCells(g, (Word) words.next()));

		if (wrd != null) {
			g.setColor(Color.CYAN); // BOZ XETT
			drawCells(g, wrd);
		}

		Iterator numbers = list.iterator();
		g.setColor(Color.BLACK);
		g.setFont(sfont);
		FontMetrics fm = g.getFontMetrics();
		while (numbers.hasNext()) {
			Number number = (Number) numbers.next();
			g.drawString(number.number, number.x + 3, number.y + fm.getAscent() - 2);// draw
																						// numbers
		}

		words = crossword.getWords();
		g.setFont(lfont);
		fm = g.getFontMetrics();
		while (words.hasNext())
			drawWord(g, fm, (Word) words.next());
	}

	private class Number {
		public int x;
		public int y;
		public String number;

		Number(int x, int y, String number) {
			this.x = x;
			this.y = y;
			this.number = number;
		}
	}

	private void keyTest(int code, char key) {
		if (wrd == null)
			return;

		String attempt;
		switch (code) {
		case KeyEvent.VK_BACK_SPACE:
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_UP:
			post = Math.max(0, --post);
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_DOWN:
			post = Math.min(wrd.getLength() - 1, ++post);
			break;
		default:
			if (!Character.isLetter(key))
				return;
			attempt = wrd.getAttempt();
			if (attempt.length() > post) {
				attempt = attempt.substring(0, post) + key + attempt.substring(post + 1);
			} else {
				for (int i = attempt.length(); i < post; i++)
					attempt += " ";
				attempt += String.valueOf(key);
			}
			wrd.setAttempt(attempt);
			post = Math.min(wrd.getLength(), ++post);
			break;
		}
		repaint();
	}

	private void hitTest(Point point) {
		Word word = crossword.getWord(new Point(point.x / DIM, point.y / DIM), wrd);
		if (word == null || wrd != word) {
			wrd = word;
			post = 0;
			repaint();
		} else if (post != 0) {
			post = 0;
			repaint();
		}
	}

	private Number drawCells(Graphics g, Word word) {
		int x = initX(word);
		int y = initY(word);

		Number number = new Number(x, y, String.valueOf(word.getNumber()));
		Color b = null;

		for (int i = 0; i < word.getLength(); i++) {
			if (i == post && word == wrd) {
				b = g.getColor();
				g.setColor(Color.RED);
			}
			g.fillRect(x + 1, y + 1, DIM - 1, DIM - 1);
			x = shiftX(x, word);
			y = shiftY(y, word);
			if (b != null) {
				g.setColor(b);
				b = null;
			}
		}

		return number;
	}

	private void drawWord(Graphics g, FontMetrics f, Word word) {
		int x = initX(word);
		int y = initY(word);
		for (int i = 0; i < word.getLength(); i++) {
			String letter = "";
			g.setColor(Color.BLACK);
			if (mShowAnswers)
				letter = word.getWord().substring(i, i + 1).toUpperCase();
			else {
				letter = word.getAttempt().length() <= i ? "" : word.getAttempt().substring(i, i + 1);
				letter = letter.toUpperCase();
				if (letter.length() > 0 && word.getWord().charAt(i) != letter.charAt(0))
					letter = letter.toUpperCase();
			}
			g.drawString(letter, (x + (DIM / 2)) - (f.stringWidth(letter) / 2), y + f.getAscent() + 6);
			x = shiftX(x, word);
			y = shiftY(y, word);
		}
	}

	public static boolean check(Word word) {
		boolean a = false;
		for (int i = 0; i < word.getLength(); i++) {
			String letter = "";
			letter = letter.toUpperCase();
			if (mShowAnswers)
				letter = word.getWord().substring(i, i + 1).toUpperCase();
			else {
				letter = word.getAttempt().length() <= i ? "" : word.getAttempt().substring(i, i + 1);
				if (letter.length() > 0 && word.getWord().charAt(i) != letter.charAt(0)){
					a = true;
				letter = letter.toUpperCase();
			}}

		}
		return a;
	}

	private static int initX(Word word) {
		return word.getStart().x * DIM;
	}

	private static int initY(Word word) {
		return word.getStart().y * DIM;
	}

	private static int shiftX(int x, Word word) {
		return x + (word.getDirection() == Word.ACROSS ? DIM : 0);
	}

	private static int shiftY(int y, Word word) {
		return y + (word.getDirection() == Word.ACROSS ? 0 : DIM);
	}

     }