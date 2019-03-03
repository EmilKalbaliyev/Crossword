import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Main {
	public static void main(String[] argv) throws FileNotFoundException {
		final File f = new File("crossword.txt");
		Scanner in = new Scanner(f);
		int numberOfWords = in.nextInt();
		final Word[] words = new Word[numberOfWords];
		for (int i = 0; i < numberOfWords; i++) {
			int number = in.nextInt();
			String word = in.next();
			int x = in.nextInt();
			int y = in.nextInt();
			String direction = in.next();
			int dir = 0;
			if (direction.equals("across")) {
				dir = 1;

			}
			if (direction.equals("down")) {
				dir = 0;

			}
			String description = in.nextLine();
			description = in.nextLine();
			words[i] = new Word(description, word, new Point(x, y), number, dir);
		}

		final CPanel crossword = new CPanel(new Crossword(words));

		final JButton check = new JButton("Check");
		check.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int a = 0;
				for (int i = 0; i < words.length; i++) {
					Word word = words[i];
					if (CPanel.check(word) == true) {
						a++;
					}
				}
				if (a > 0) {
					JOptionPane.showMessageDialog(null, "Oops,you have mistake.");
				} else {
					JOptionPane.showMessageDialog(null, "Congratulations, you have not made any mistakes");
				}
			}
		});

		final JTextArea across = new JTextArea();
		across.setEditable(false);
		across.setText("Across:\n");

		final JTextArea down = new JTextArea();
		down.setEditable(false);
		down.setText("Down:\n");

		for (int j = 0; j < words.length; j++) {
			Word clue = words[j];
			String text = "\n(" + clue.getNumber() + ") " + clue.getHint();
			if (clue.getDirection() == Word.ACROSS)
				across.setText(across.getText() + text);
			else
				down.setText(down.getText() + text);
		}

		final JFrame frames = new JFrame("Hints");
		frames.getContentPane().setLayout(new GridLayout(0, 2, 4, 4));
		frames.getContentPane().add(new JScrollPane(across));
		frames.getContentPane().add(new JScrollPane(down));
		frames.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frames.pack();
		frames.setLocation(440, 0);
		frames.setVisible(true);

		final JFrame frame = new JFrame("Crossword");
		frame.getContentPane().add(crossword, BorderLayout.CENTER);
		frame.getContentPane().add(check, BorderLayout.SOUTH);
		final JButton clear = new JButton("Clear");
		clear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				frame.dispose();
				frames.dispose();
				String[] s = null;
				try {
					main(s);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});

		frame.getContentPane().add(clear, BorderLayout.WEST);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(true);
		frame.setVisible(true);
	}

}

