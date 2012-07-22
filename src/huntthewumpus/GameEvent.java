package huntthewumpus;


public class GameEvent extends Exception {
	private static final long serialVersionUID = 1L;

	public GameEvent(String gameMessage) {
		super(gameMessage);
	}

	public String getGameMessage() {
		return getMessage();
	}

	public boolean shouldGameTerminate() {
		return false;
	}
}
