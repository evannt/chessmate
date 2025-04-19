package event;

import game.GameMode;
import game.GameState;

public class GameResultEvent implements ChessEvent {

	private GameState gameState;
	private GameMode gameMode;
	private int winner;
	private boolean isHumanWinner;

	public GameResultEvent(GameState gameState, GameMode gameMode, int winner, boolean isHumanWinner) {
		this.gameState = gameState;
		this.gameMode = gameMode;
		this.winner = winner;
		this.isHumanWinner = isHumanWinner;
	}

	public GameState getGameState() {
		return gameState;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public int getWinner() {
		return winner;
	}

	public boolean isHumanWinner() {
		return isHumanWinner;
	}

	@Override
	public ChessEventType getType() {
		return ChessEventType.GAME_RESULT;
	}

}
