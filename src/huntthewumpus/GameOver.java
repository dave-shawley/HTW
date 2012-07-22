package huntthewumpus;

public class GameOver extends GameEvent {
	private static final long serialVersionUID = 1L;

	public GameOver(String message) {
		super(message);
	}

	@Override
	public boolean shouldGameTerminate() {
		return true;
	}
}
