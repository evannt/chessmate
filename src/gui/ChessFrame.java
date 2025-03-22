package gui;

import javax.swing.JFrame;

public class ChessFrame extends JFrame {

	private static final long serialVersionUID = -2536492765344084132L;

	public ChessFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("Chess");

		ChessPanel chessPanel = new ChessPanel();

		add(chessPanel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
