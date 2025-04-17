package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import chess.Piece;
import event.GameResultEvent;
import game.GameMode;
import game.GameState;
import gui.ChessFrame;

public class GameResultIndicator extends JDialog {

	private static final long serialVersionUID = 846143884373726332L;

	private static final int BUTTON_WIDTH = 250;
	private static final int BUTTON_HEIGHT = 50;

	private static final Font TITLE_FONT = new Font("Georgia", Font.BOLD, 40);
	private static final Font BUTTON_FONT = new Font("Georgia", Font.BOLD, 20);

	private GameResultChoice resultChoice = GameResultChoice.NONE;

	public GameResultIndicator(GameResultEvent gameResult, JPanel parent) {
		JPanel panel = new JPanel(new BorderLayout(15, 10));
		panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel outcomeMessage = new JLabel(getOutcomeMessage(gameResult));
		outcomeMessage.setFont(TITLE_FONT);
		outcomeMessage.setHorizontalAlignment(JLabel.CENTER);

		JPanel buttonPanel = new JPanel(new BorderLayout(15, 10));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JButton rematchButton = createGameResultButton("Rematch", GameResultChoice.RESTART_GAME);

		JButton newGameButton = createGameResultButton("New Game", GameResultChoice.NEW_GAME);

		buttonPanel.add(rematchButton, BorderLayout.WEST);
		buttonPanel.add(newGameButton, BorderLayout.EAST);

		panel.add(outcomeMessage, BorderLayout.NORTH);
		panel.add(buttonPanel, BorderLayout.SOUTH);
		add(panel);

		pack();
		setLocationRelativeTo(parent);
		setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	public GameResultChoice getResultChoice() {
		return resultChoice;
	}

	private JButton createGameResultButton(String text, GameResultChoice gameResultChoice) {
		JButton button = new JButton(text);
		button.setFont(BUTTON_FONT);
		button.setForeground(ChessFrame.DARK_GRAY);
		button.setBackground(ChessFrame.LIGHT_GRAY);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);

		button.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

		button.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				button.setBackground(ChessFrame.DARK_GRAY_ALT);
			}

			public void mouseExited(MouseEvent evt) {
				button.setBackground(ChessFrame.LIGHT_GRAY);
			}
		});

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resultChoice = gameResultChoice;
				dispose();
			}
		});

		return button;
	}

	private String getOutcomeMessage(GameResultEvent gameResult) {
		if (gameResult.getGameState() == GameState.STALEMATE) {
			return "Draw";
		}
		return (gameResult.getGameMode() == GameMode.PLAY_FRIEND ?
				gameResult.getWinner() == Piece.WHITE ? "White" : "Black" :
				"You") + " Won!";

	}

}
