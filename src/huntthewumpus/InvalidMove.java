package huntthewumpus;

public class InvalidMove extends GameEvent {
	private static final long serialVersionUID = 1L;

	public InvalidMove(Direction attemptedMove) {
		super(String.format("You can't go %s from here.", attemptedMove.longName()));
	}
}
