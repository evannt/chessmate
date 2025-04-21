package ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import chess.Piece;
import chess.PieceType;
import gui.ChessBoardPainter;
import util.ImageUtil;

public class PawnPromotionPrompt extends JDialog {

	private static final long serialVersionUID = 1834479053981065833L;

	private static final ImageIcon WQUEEN = new ImageIcon(ImageUtil.getImage(Piece.PIECE_PATH + "qw.png"));
	private static final ImageIcon WKNIGHT = new ImageIcon(ImageUtil.getImage(Piece.PIECE_PATH + "nw.png"));
	private static final ImageIcon WROOK = new ImageIcon(ImageUtil.getImage(Piece.PIECE_PATH + "rw.png"));
	private static final ImageIcon WBISHOP = new ImageIcon(ImageUtil.getImage(Piece.PIECE_PATH + "bw.png"));

	private static final ImageIcon BQUEEN = new ImageIcon(ImageUtil.getImage(Piece.PIECE_PATH + "qb.png"));
	private static final ImageIcon BKNIGHT = new ImageIcon(ImageUtil.getImage(Piece.PIECE_PATH + "nb.png"));
	private static final ImageIcon BROOK = new ImageIcon(ImageUtil.getImage(Piece.PIECE_PATH + "rb.png"));
	private static final ImageIcon BBISHOP = new ImageIcon(ImageUtil.getImage(Piece.PIECE_PATH + "bb.png"));

	private PieceType promotionSelection;

	public PawnPromotionPrompt(int color, int x, int y) {
		setUndecorated(true);
		setPreferredSize(new Dimension(ChessBoardPainter.TILE_SIZE, 4 * ChessBoardPainter.TILE_SIZE));
		setLocation(x, y);

		JPanel promotionPanel = new JPanel();
		promotionPanel.setLayout(new GridLayout(4, 0));

		JButton queenButton;
		JButton knightButton;
		JButton rookButton;
		JButton bishopButton;
		if (color == Piece.WHITE) {
			queenButton = createPromotionButton(PieceType.WQUEEN, WQUEEN);
			knightButton = createPromotionButton(PieceType.WKNIGHT, WKNIGHT);
			rookButton = createPromotionButton(PieceType.WROOK, WROOK);
			bishopButton = createPromotionButton(PieceType.WBISHOP, WBISHOP);
		} else {
			queenButton = createPromotionButton(PieceType.BQUEEN, BQUEEN);
			knightButton = createPromotionButton(PieceType.BKNIGHT, BKNIGHT);
			rookButton = createPromotionButton(PieceType.BROOK, BROOK);
			bishopButton = createPromotionButton(PieceType.BBISHOP, BBISHOP);
		}
		promotionPanel.add(queenButton);
		promotionPanel.add(knightButton);
		promotionPanel.add(rookButton);
		promotionPanel.add(bishopButton);

		addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				dispose();
			}
		});

		add(promotionPanel);

		pack();
		setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	public PieceType getPromotionSelection() {
		return promotionSelection;
	}

	private JButton createPromotionButton(PieceType promotionType, ImageIcon icon) {
		JButton button = new JButton(icon);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(true);

		Color backgroundColor = this.getBackground();
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				promotionSelection = promotionType;
				closePrompt();
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

	private void closePrompt() {
		SwingUtilities.getWindowAncestor(this).dispose();
	}

}
