package huntthewumpus;

public class PlayerNotFound extends RuntimeException {
	private static final long serialVersionUID = 5177097418278721004L;

	public PlayerNotFound() {
		super("player not found in map");
	}
}
