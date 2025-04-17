package event;

import game.GameMode;
import game.GameState;

public class GameResultEvent implements ChessEvent {

	private GameState gameState;
	private GameMode gameMode;
	private int winner;

	public GameResultEvent(GameState gameState, GameMode gameMode, int winner) {
		this.gameState = gameState;
		this.gameMode = gameMode;
		this.winner = winner;
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

	@Override
	public ChessEventType getType() {
		return ChessEventType.GAME_RESULT;
	}

}
