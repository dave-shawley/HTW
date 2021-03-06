package huntthewumpus;

import java.util.ArrayList;
import java.util.List;

public class Game {
	private final static Direction[] ORDERING = { Direction.N, Direction.S, Direction.E, Direction.W };

	private final GameWorld world;
	private final GameDisplay display;
	private final CommandGenerator generator;
	private boolean gameIsRunning;
	private boolean wumpusIsFrozen;

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
		this.wumpusIsFrozen = false;
		this.world = world;
		this.display = display;
		this.generator = commandStream;
	}

	public boolean tick() {
		try {
			int startingArrowCount = world.getArrowCount();
			Command cmd = generator.getNextCommand();
			switch (cmd) {
			case MOVE_EAST:
			case MOVE_WEST:
			case MOVE_NORTH:
			case MOVE_SOUTH:
				world.movePlayer(cmd.direction());
				break;
			case SHOOT_EAST:
			case SHOOT_WEST:
			case SHOOT_SOUTH:
			case SHOOT_NORTH:
				if (world.getArrowCount() < 1) {
					display.showOutput("You don't have any arrows.");
				} else {
					world.shootArrow(cmd.direction());
					display.showOutput("The arrow flies away in silence.");
				}
				break;
			}

			tickWumpus();

			Room currentLocation = world.whereIsPlayer();
			if (currentLocation != null) {
				if (world.getArrowCount() > startingArrowCount) {
					display.showOutput("You found an arrow.");
				}
				printQuiverSize();
				printSounds(currentLocation);
				printSmells(currentLocation);
				printDirections(currentLocation);
			}
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

	public void reset() {
		world.reset();
		gameIsRunning = true;
	}

	public boolean hasTerminated() {
		return !gameIsRunning;
	}

	public void freezeWumpus() {
		wumpusIsFrozen = true;
	}

	public void thawWumpus() {
		wumpusIsFrozen = false;
	}

	protected void tickWumpus() {
		if (!wumpusIsFrozen && (Math.random() * 10.0) > 2.0) {
			world.moveWumpus();
		}
	}

	protected boolean isWumpusFrozen() {
		return wumpusIsFrozen;
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

	private void printSmells(Room r) {
		for (Direction d: ORDERING) {
			Room peer = r.getPeer(d);
			if (peer != null) {
				if (peer.getContents() == RoomObject.wumpus) {
					display.showOutput("You smell the Wumpus.");
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

	private void printQuiverSize() {
		int arrowCount = world.getArrowCount();
		if (arrowCount > 0) {
			display.showOutput(String.format("You have %d arrow%s.",
					arrowCount, (arrowCount > 1) ? "s": ""));
		} else {
			display.showOutput("You have no arrows.");
		}
	}
}
