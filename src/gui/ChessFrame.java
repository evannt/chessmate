package gui;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class ChessFrame extends JFrame {

	private static final long serialVersionUID = -2536492765344084132L;

	public ChessFrame() {
		JFrame window = new JFrame();

		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Chess");

		ChessPanel chessPanel = new ChessPanel();
		SwingUtilities.invokeLater(chessPanel);
		window.add(chessPanel);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

}
