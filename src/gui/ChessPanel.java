package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JPanel;

import event.ChessEvent;
import event.ChessEventListener;
import event.ChessEventType;
import event.GameResultEvent;
import event.UpdateBoardEvent;
import game.GameManager;
import game.GameMode;
import game.SoundType;
import ui.GameResultIndicator;
import ui.SoundManager;
import ui.UI;
import util.BoardUtil;

public class ChessPanel extends JPanel implements ChessEventListener {

	private static final long serialVersionUID = -2612936424651279335L;

	public static final int SCREEN_WIDTH = ChessBoardPainter.TILE_SIZE * 15;
	public static final int SCREEN_HEIGHT = ChessBoardPainter.TILE_SIZE * 11;

	private ChessFrame parent;

	private UI userInterface;
	private SoundManager soundManager;
	private GameManager gameManager;
	private ChessBoardPainter chessBoardPainter;

	public ChessPanel(ChessFrame parent) {
		this.parent = parent;
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

	public ChessFrame getFrame() {
		return parent;
	}

	public void setupGame(GameMode gameMode, int selectedColor) {
		removeAll();
		revalidate();
		userInterface = new UI();
		soundManager = new SoundManager();

		gameManager = new GameManager(gameMode, selectedColor);
		chessBoardPainter = new ChessBoardPainter(this);
		gameManager.addSubscriber(chessBoardPainter, ChessEventType.values());
		gameManager.addSubscriber(this, ChessEventType.values());
		addUI();
		userInterface.updateTurnIndicator(gameManager.getTurn());
		soundManager.playSound(SoundType.GAME_START.getSoundKey());
	}

	public void restartGame() {
		removeAll();
		revalidate();
		gameManager.restartGame();
		addUI();
		repaint();
		soundManager.playSound(SoundType.GAME_START.getSoundKey());
	}

	public void addUI() {
		userInterface.undoButton.addActionListener((e) -> gameManager.undoMove());
		userInterface.redoButton.addActionListener((e) -> gameManager.redoMove());
		add(userInterface.turnIndicatorDisplay);
		add(userInterface.undoButton);
		add(userInterface.redoButton);
		add(userInterface.moveLogPane);
		userInterface.updateMoveLog(gameManager.getMoveLog());
		userInterface.updateTurnIndicator(gameManager.getTurn());
	}

	public void chessPanelMousePressed(MouseEvent e) {
		int rank = (e.getY() / ChessBoardPainter.TILE_SIZE) - ChessBoardPainter.START_RANK;
		int file = (e.getX() / ChessBoardPainter.TILE_SIZE) - ChessBoardPainter.START_FILE;
		if (rank < 0 || rank > 7 || file < 0 || file > 7) {
			gameManager.setSelectedSquare(-1);
			return;
		} else {
			if (gameManager.isGameOngoing() && gameManager.isHumanTurn()) {
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
			if (gameManager.isGameOngoing() && gameManager.isHumanTurn()) {
				int square = BoardUtil.getIndexFromCoordinate(rank, file);

				gameManager.mouseReleased(e, square);
			}
		}
		repaint();
	}

	public void chessPanelMouseDragged(MouseEvent e) {
		if (gameManager.isGameOngoing() && gameManager.isHumanTurn()) {
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
		chessBoardPainter.highlightMove(graphics2D, gameManager.getMoveLog().getLastMove());
		chessBoardPainter.drawPieces(graphics2D, gameManager.getPosition());

		graphics2D.dispose();
	}

	@Override
	public void update(ChessEvent event) {
		if (event instanceof UpdateBoardEvent boardUpdate) {
			soundManager.playSound(boardUpdate.getSoundType().getSoundKey());
			userInterface.updateMoveLog(gameManager.getMoveLog());
			userInterface.updateTurnIndicator(gameManager.getTurn());
			repaint();
		} else if (event instanceof GameResultEvent gameResult) {
			soundManager.playSound(SoundType.GAME_END.getSoundKey());
			repaint();
			GameResultIndicator gameResultIndicator = new GameResultIndicator(gameResult, this);
			switch (gameResultIndicator.getResultChoice()) {
			case RESTART_GAME:
				restartGame();
				break;
			case NEW_GAME:
				parent.switchPanel(ChessFrame.MAIN_MENU);
				break;
			case NONE:
				userInterface.rematchButton.addActionListener((e) -> {
					restartGame();
				});
				userInterface.newGameButton.addActionListener((e) -> {
					parent.switchPanel(ChessFrame.MAIN_MENU);
				});
				add(userInterface.rematchButton);
				add(userInterface.newGameButton);

				revalidate();
				repaint();
				break;
			}
		}
	}

}
