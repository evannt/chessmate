package event;

public abstract class ChessEvent {

	private int targetSquare;

	public ChessEvent(int targetSquare) {
		this.targetSquare = targetSquare;
	}

	public int getTargetSquare() {
		return targetSquare;
	}

}
