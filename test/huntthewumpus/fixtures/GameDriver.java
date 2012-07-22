package huntthewumpus.fixtures;

import huntthewumpus.Game;
import huntthewumpus.GameWorld;


public class GameDriver {
	private static GameDriver CURRENT_DRIVER;

	private final Game game;
	private final RecordingGameDisplay display;
	private final ProgrammableGenerator generator;
	private final GameWorld world;

	public static GameWorld getWorld() {
		return CURRENT_DRIVER.world;
	}

	public static void executeCommand(String command) {
		CURRENT_DRIVER.enterCommand(command);
	}


	public GameDriver() {
		CURRENT_DRIVER = this;
		display = new RecordingGameDisplay();
		world = new GameWorld();
		generator = new ProgrammableGenerator();
		game = new Game(world, display, generator);
	}

	public void putInCavern(String gameObject, int cavernNumber) {
		if (gameObject.equalsIgnoreCase("player")) {
			world.setPlayerLocation(world.getRoom(cavernNumber));
		} else if (gameObject.equalsIgnoreCase("pit")) {
			world.addPitInCavern(world.getRoom(cavernNumber));
		} else if (gameObject.equalsIgnoreCase("bats")) {
			world.addBatsInCavern(world.getRoom(cavernNumber));
		}
	}

	public boolean enterCommand(String command) {
		try {
			generator.addCommand(command);
			return game.tick();
		} catch (RuntimeException e) {
			e.printStackTrace(System.out);
			throw e;
		}
	}

	public boolean cavernHas(int cavernNumber, String gameObject) {
		if (gameObject.equalsIgnoreCase("player")) {
			return world.whereIsPlayer().getRoomNumber() == cavernNumber;
		}
		return false;
	}

	public boolean messageWasPrinted(String message) {
		return message.equalsIgnoreCase(display.popDisplayedOutput());
	}

	public boolean gameTerminated() {
		return game.hasTerminated();
	}
}

