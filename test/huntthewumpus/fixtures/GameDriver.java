package huntthewumpus.fixtures;

import huntthewumpus.Direction;
import huntthewumpus.GameWorld;

public class GameDriver {
	private static final GameWorld WORLD = new GameWorld();

	public static GameWorld getWorld() {
		return WORLD;
	}

	public static void executeCommand(String command) {
		if (command.equalsIgnoreCase("E")) {
			WORLD.movePlayer(Direction.valueOf("E"));
		}
	}

}
