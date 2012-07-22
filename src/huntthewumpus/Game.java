package huntthewumpus;

public class Game {
	private final GameWorld world;
	private final GameDisplay display;
	private final CommandGenerator generator;
	private boolean gameIsRunning;

	public Game(GameWorld world, GameDisplay display, CommandGenerator commandStream) {
		if (world == null) {
			throw new IllegalArgumentException("a GameWorld is required");
		}
		if (display == null) {
			throw new IllegalArgumentException("a GameDisplay is required");
		}
		if (commandStream == null) {
			throw new IllegalArgumentException("a CommandGenerator is required");
		}

		this.gameIsRunning = true;
		this.world = world;
		this.display = display;
		this.generator = commandStream;
	}

	public boolean tick() {
		try {
			switch (generator.getNextCommand()) {
			case MOVE_EAST:
				world.movePlayer(Direction.E);
				break;
			case MOVE_WEST:
				world.movePlayer(Direction.W);
				break;
			case MOVE_NORTH:
				world.movePlayer(Direction.N);
				break;
			case MOVE_SOUTH:
				world.movePlayer(Direction.S);
				break;
//			case REST:
//				// nothing to do here.
//				break;
			}

			Room currentLocation = world.whereIsPlayer();
			processSoundsFromAdjacentRoom(currentLocation.getPeer(Direction.N));
			processSoundsFromAdjacentRoom(currentLocation.getPeer(Direction.S));
			processSoundsFromAdjacentRoom(currentLocation.getPeer(Direction.E));
			processSoundsFromAdjacentRoom(currentLocation.getPeer(Direction.W));

		} catch (GameEvent event) {
			display.showOutput(event.getGameMessage());
			if (event.shouldGameTerminate()) {
				display.showOutput("Game over.");
				gameIsRunning = false;
			}
		} catch (UnknownCommand e) {
			display.showOutput(String.format("I don't know how to %s.", e.getInputString()));
			return false;
		}
		return true;
	}

	private void processSoundsFromAdjacentRoom(Room r) {
		if (r == null) {
			return;
		}
		if (r.getContents() == RoomObject.bats) {
			display.showOutput("You hear chirping.");
		}
		if (r.getContents() == RoomObject.pit) {
			display.showOutput("You hear wind.");
		}
	}

	public boolean hasTerminated() {
		return !gameIsRunning;
	}

}
