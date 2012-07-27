package huntthewumpus.fixtures;

import huntthewumpus.Game;
import huntthewumpus.GameWorld;


public class GameDriver {
	private static GameDriver CURRENT_DRIVER;

	private Game game;
	private RecordingGameDisplay display;
	private ProgrammableGenerator generator;
	private GameWorld world;

	public static GameWorld getWorld() {
		return CURRENT_DRIVER.world;
	}

	public static void executeCommand(String command) {
		CURRENT_DRIVER.enterCommand(command);
	}


	public GameDriver() {
		CURRENT_DRIVER = this;
		clearMap();
	}

	public void putInCavern(String gameObject, int cavernNumber) {
		if (gameObject.equalsIgnoreCase("player")) {
			world.setPlayerLocation(world.getRoom(cavernNumber));
		} else if (gameObject.equalsIgnoreCase("pit")) {
			world.addPitInCavern(world.getRoom(cavernNumber));
		} else if (gameObject.equalsIgnoreCase("bats")) {
			world.addBatsInCavern(world.getRoom(cavernNumber));
		} else if (gameObject.equalsIgnoreCase("wumpus")) {
			world.setWumpusLocation(world.getRoom(cavernNumber));
		}
	}

	public boolean enterCommand(String command) {
		display.clearOutput();
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
		boolean foundMessage = false;
		for (String outputLine: display.getDisplayedOutput()) {
			foundMessage = foundMessage || outputLine.equalsIgnoreCase(message);
		}
		return foundMessage;
	}

	public boolean gameTerminated() {
		return game.hasTerminated();
	}

	public void clearMap() {
		display = new RecordingGameDisplay();
		world = new GameWorld();
		generator = new ProgrammableGenerator();
		game = new Game(world, display, generator);
	}

	public void freezeWumpus(boolean frozen) {
		if (frozen) {
			game.freezeWumpus();
		} else {
			game.thawWumpus();
		}
	}

}

