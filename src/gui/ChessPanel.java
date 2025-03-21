package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

import chess.GameManager;
import chess.GameState;
import chess.Piece;
import chess.Position;
import util.BoardUtil;

public class ChessPanel extends JPanel implements ChessGui, Runnable {

	private static final long serialVersionUID = -2612936424651279335L;

	public static final int SCREEN_WIDTH = ChessBoardPainter.TILE_SIZE * 14;
	public static final int SCREEN_HEIGHT = ChessBoardPainter.TILE_SIZE * 11;

	private GameManager gameManager;
	private ChessBoardPainter chessBoardPainter;

	public ChessPanel() {
		setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		setDoubleBuffered(true); // improve game rendering performance
		setFocusable(true);
		setBackground(ChessBoardPainter.DARK_GRAY);

		gameManager = new GameManager(Piece.WHITE);
		chessBoardPainter = new ChessBoardPainter(Piece.WHITE);

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

	public void chessPanelMousePressed(MouseEvent e) {
		int rank = (e.getY() / ChessBoardPainter.TILE_SIZE) - ChessBoardPainter.START_RANK;
		int file = (e.getX() / ChessBoardPainter.TILE_SIZE) - ChessBoardPainter.START_FILE;
		if (rank < 0 || rank > 7 || file < 0 || file > 7) {
			// TODO Handle ui presses
			setSelectedSquare(-1);
			return;
		} else {
			int square = BoardUtil.getIndexFromCoordinate(rank, file);

			gameManager.mousePressed(e, square);
		}
		repaint();
	}

	public void chessPanelMouseReleased(MouseEvent e) {
		int rank = (e.getY() / ChessBoardPainter.TILE_SIZE) - ChessBoardPainter.START_RANK;
		int file = (e.getX() / ChessBoardPainter.TILE_SIZE) - ChessBoardPainter.START_FILE;
		if (rank < 0 || rank > 7 || file < 0 || file > 7) {
			gameManager.setGameState(GameState.HUMAN_TURN);
			gameManager.resetActivePiecePosition();
			setSelectedSquare(-1);
			return;
		} else {
			int square = BoardUtil.getIndexFromCoordinate(rank, file);
			gameManager.mouseReleased(e, square);
		}
		repaint();
	}

	public void chessPanelMouseDragged(MouseEvent e) {
		gameManager.mouseDragged(e);
		repaint();
	}

	@Override
	public void setBoardPosition(Position position) {
		gameManager.setPosition(position);
		repaint();
	}

	@Override
	public void setSelectedSquare(int square) {
		gameManager.setSelectedSquare(square);
		repaint();
	}

	public void requestPiecePromotion(Graphics2D graphics2D) {
		chessBoardPainter.drawPiecePromotionPrompt(graphics2D, Piece.WHITE, gameManager.getPromotedPiece());
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D graphics2D = (Graphics2D) g;
		chessBoardPainter.drawBorder(graphics2D);
		chessBoardPainter.drawBoard(graphics2D);
		chessBoardPainter.highlightSelectedSquare(graphics2D, gameManager.getSelectedSquare());
		chessBoardPainter.drawPieces(graphics2D, gameManager.getPosition());

		if (gameManager.getGameState() == GameState.PAWN_PROMOTION) {
			requestPiecePromotion(graphics2D);
		}

		graphics2D.dispose();
	}

	@Override
	public void run() {
		System.out.println("RUN");
	}

}
