package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

import chess.Piece;
import event.ChessEvent;
import event.ChessEventListener;
import event.ChessEventType;
import event.ComputerMoveEvent;
import game.ComputerPlayer;
import game.GameManager;
import game.GameMode;
import game.HumanPlayer;
import game.MoveType;
import ui.SoundManager;
import ui.UI;
import util.BoardUtil;

public class ChessPanel extends JPanel implements ChessEventListener {

	private static final long serialVersionUID = -2612936424651279335L;

	public static final int SCREEN_WIDTH = ChessBoardPainter.TILE_SIZE * 15;
	public static final int SCREEN_HEIGHT = ChessBoardPainter.TILE_SIZE * 11;

	private UI userInterface;
	private SoundManager soundManager;
	private GameManager gameManager;
	private ChessBoardPainter chessBoardPainter;

	public ChessPanel() {
		setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		setDoubleBuffered(true); // improve game rendering performance
		setFocusable(true);
		setBackground(ChessBoardPainter.DARK_GRAY);
		setLayout(null);

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				chessPanelMousePressed(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				chessPanelMouseReleased(e);
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				chessPanelMouseDragged(e);
			}
		});

		setVisible(true);
	}

	public void setupGame(GameMode gameMode, int selectedColor) {
		userInterface = new UI();
		soundManager = new SoundManager();
		gameManager = switch (gameMode) {
		case PLAY_BOT -> selectedColor == Piece.WHITE ?
				new GameManager(new HumanPlayer(), new ComputerPlayer()) :
				new GameManager(new ComputerPlayer(), new HumanPlayer());
		case PLAY_FRIEND -> new GameManager(new HumanPlayer(), new HumanPlayer());
		};
		chessBoardPainter = new ChessBoardPainter(this);
		gameManager.addSubscriber(chessBoardPainter, ChessEventType.values());
		gameManager.addSubscriber(this, ChessEventType.values());
		add(userInterface.moveLogPane);
	}

	public void chessPanelMousePressed(MouseEvent e) {
		int rank = (e.getY() / ChessBoardPainter.TILE_SIZE) - ChessBoardPainter.START_RANK;
		int file = (e.getX() / ChessBoardPainter.TILE_SIZE) - ChessBoardPainter.START_FILE;
		if (rank < 0 || rank > 7 || file < 0 || file > 7) {
			// TODO Handle ui presses
			gameManager.setSelectedSquare(-1);
			return;
		} else {
			if (gameManager.isHumanTurn()) {
				int square = BoardUtil.getIndexFromCoordinate(rank, file);
				gameManager.mousePressed(e, square);
			}
		}
		repaint();
	}

	public void chessPanelMouseReleased(MouseEvent e) {
		int rank = (e.getY() / ChessBoardPainter.TILE_SIZE) - ChessBoardPainter.START_RANK;
		int file = (e.getX() / ChessBoardPainter.TILE_SIZE) - ChessBoardPainter.START_FILE;
		if (rank < 0 || rank > 7 || file < 0 || file > 7) {
			gameManager.resetActivePiecePosition();
			gameManager.setSelectedSquare(-1);
			return;
		} else {
			if (gameManager.isHumanTurn()) {
				int square = BoardUtil.getIndexFromCoordinate(rank, file);

				MoveType moveType = gameManager.mouseReleased(e, square);
				soundManager.playSound(moveType.getSoundKey());
			}
		}
		repaint();
	}

	public void chessPanelMouseDragged(MouseEvent e) {
		if (gameManager.isHumanTurn()) {
			gameManager.mouseDragged(e);
		}
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D graphics2D = (Graphics2D) g.create();
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		chessBoardPainter.drawBorder(graphics2D);
		chessBoardPainter.drawBoard(graphics2D);
		chessBoardPainter.highlightSelectedSquare(graphics2D, gameManager.getSelectedSquare());
		chessBoardPainter.drawPieces(graphics2D, gameManager.getPosition());
		userInterface.updateMoveLog(gameManager.getMoveLog());

		graphics2D.dispose();
	}

	@Override
	public void update(ChessEvent event) {
		if (event instanceof ComputerMoveEvent computerMove) {
			// highlight from and to square
			System.out.println("CHESS PANEL UPDATE");
			soundManager.playSound(computerMove.getMoveType().getSoundKey());
			repaint();
		}
	}

}
