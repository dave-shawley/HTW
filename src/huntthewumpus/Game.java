package huntthewumpus;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private final static Direction[] ORDERING = { Direction.N, Direction.S, Direction.E, Direction.W };

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
			}

			Room currentLocation = world.whereIsPlayer();
			printSounds(currentLocation);
			printDirections(currentLocation);

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

	public boolean hasTerminated() {
		return !gameIsRunning;
	}

	private void printSounds(Room r) {
		for (Direction d: ORDERING) {
			Room peer = r.getPeer(d);
			if (peer != null) {
				if (peer.getContents() == RoomObject.bats) {
					display.showOutput("You hear chirping.");
				}
				if (peer.getContents() == RoomObject.pit) {
					display.showOutput("You hear wind.");
				}
			}
		}
	}

	private void printDirections(Room r) {
		List<String> directions = new ArrayList<String>(4);

		for (Direction dir: ORDERING) {
			if (r.getPeer(dir) != null) {
				directions.add(dir.longName());
			}
		}

		if (!directions.isEmpty()) {
			StringBuilder sb = new StringBuilder("You can go ");
			sb.append(directions.remove(0));
			while (directions.size() > 1) {
				sb.append(", ").append(directions.remove(0));
			}
			if (!directions.isEmpty()) {
				sb.append(" and ").append(directions.remove(0));
			}
			sb.append(" from here.");
			display.showOutput(sb.toString());
		}
	}

}
