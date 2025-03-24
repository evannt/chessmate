package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import chess.Piece;

public class ChessColorSelector extends JDialog {

	private static final long serialVersionUID = -5505943938772399823L;

	private static final ImageIcon WHITE_COLOR_ICON = new ImageIcon(Piece.PIECE_PATH + "kw.png");
	private static final ImageIcon RANDOM_COLOR_ICON = new ImageIcon(Piece.PIECE_PATH + "kwb.png");
	private static final ImageIcon BLACK_COLOR_ICON = new ImageIcon(Piece.PIECE_PATH + "kb.png");

	private static final Font PROMPT_FONT = new Font("Georgia", Font.PLAIN, 18);

	private int selectedColor;

	public ChessColorSelector(JPanel parent) {
		selectedColor = -1;
		int randomColor = Math.random() > 0.5 ? Piece.WHITE : Piece.BLACK;

		JPanel buttonPanel = new JPanel(new BorderLayout(15, 10));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		JLabel prompt = new JLabel("Select Your Piece Color");
		prompt.setFont(PROMPT_FONT);
		prompt.setHorizontalAlignment(JLabel.CENTER);

		JButton whiteColorButton = createColorButton(Piece.WHITE, WHITE_COLOR_ICON);
		JButton randomColorButton = createColorButton(randomColor, RANDOM_COLOR_ICON);
		JButton blackColorButton = createColorButton(Piece.BLACK, BLACK_COLOR_ICON);

		buttonPanel.add(prompt, BorderLayout.NORTH);
		buttonPanel.add(whiteColorButton, BorderLayout.WEST);
		buttonPanel.add(randomColorButton, BorderLayout.CENTER);
		buttonPanel.add(blackColorButton, BorderLayout.EAST);

		add(buttonPanel);

		addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				dispose();
			}
		});

		setUndecorated(true);
		pack();
		setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(parent);
		setVisible(true);
	}

	public int getSelectedColor() {
		return selectedColor;
	}

	private JButton createColorButton(int color, ImageIcon icon) {
		JButton button = new JButton(icon);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(true);
		Color backgroundColor = this.getBackground();
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedColor = color;
				dispose();
			}
		});

		button.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent evt) {
				button.setBackground(backgroundColor.darker());
			}

			public void mouseExited(MouseEvent evt) {
				button.setBackground(backgroundColor);
			}
		});

		return button;
	}

}
