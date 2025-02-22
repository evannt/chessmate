package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

import chess.Position;
import util.BoardUtil;

public class ChessPanel extends JPanel implements ChessGui, Runnable {

	// TODO Move board drawing methods to ChessBoardPainter

	// TODO Drags onto same square to snap back to the center

	private static final long serialVersionUID = -2612936424651279335L;

	public static final int SCREEN_WIDTH = ChessBoardPainter.TILE_SIZE * 14;
	public static final int SCREEN_HEIGHT = ChessBoardPainter.TILE_SIZE * 11;

	// private GameManager gameManager;
	private ChessBoardPainter chessBoardPainter;

	public ChessPanel() {
		setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		setDoubleBuffered(true); // improve game rendering performance
		setFocusable(true);
		setBackground(ChessBoardPainter.DARK_GRAY);
		chessBoardPainter = new ChessBoardPainter();
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				chessPanelMousePressed(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				chessPanelMouseReleased(e);
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				chessPanelMouseDragged(e);
			}
		});
	}

	// Mouse Control Flow

	// Mouse pressed -> set the selection
	// Mouse released -> obtain the released selection and compare against the set selection
	// if the intial click equals the released location
	// The user has selected the current piece in attempt to move
	// If the active piece is empty then the user has not yet attempt to move
	// Otherwise the user is attempting to move the piece

	public void chessPanelMousePressed(MouseEvent e) {
		int rank = (e.getY() / ChessBoardPainter.TILE_SIZE) - ChessBoardPainter.START_RANK;
		int file = (e.getX() / ChessBoardPainter.TILE_SIZE) - ChessBoardPainter.START_FILE;
		if (rank < 0 || rank > 7 || file < 0 || file > 7) {
			setSelectedSquare(-1);
			return;
		}
		int square = BoardUtil.getIndexFromCoordinate(rank, file);
//		setSelectedSquare(square);
//		System.out.println("Selected square: " + square);
		chessBoardPainter.mousePressed(e, square);
		repaint();
	}

	public void chessPanelMouseReleased(MouseEvent e) {
		int rank = (e.getY() / ChessBoardPainter.TILE_SIZE) - ChessBoardPainter.START_RANK;
		int file = (e.getX() / ChessBoardPainter.TILE_SIZE) - ChessBoardPainter.START_FILE;
		if (rank < 0 || rank > 7 || file < 0 || file > 7) {
			setSelectedSquare(-1);
			return;
		}
		int square = BoardUtil.getIndexFromCoordinate(rank, file);
		chessBoardPainter.mouseReleased(e, square);

//		setSelectedSquare(square);
		repaint();
	}

	public void chessPanelMouseDragged(MouseEvent e) {
		chessBoardPainter.mouseDragged(e);
		repaint();
	}

	@Override
	public void setBoardPosition(Position position) {
		chessBoardPainter.setPosition(position);
		repaint();
	}

	@Override
	public void setSelectedSquare(int square) {
		chessBoardPainter.setSelectedSquare(square);
		repaint();
	}

	@Override
	public void reqeustPiecePromotion() {
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D graphics2D = (Graphics2D) g;
		chessBoardPainter.drawBorder(graphics2D);
		chessBoardPainter.drawBoard(graphics2D);
		chessBoardPainter.highlightSelectedSquare(graphics2D);
		chessBoardPainter.drawPieces(graphics2D);

//		drawBorder(graphics2D);
//		drawBoard(graphics2D);
//		gameManager.draw(graphics2D);

		graphics2D.dispose();
	}

	@Override
	public void run() {
		System.out.println("RUN");
	}

}
