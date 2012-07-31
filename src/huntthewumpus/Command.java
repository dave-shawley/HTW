package huntthewumpus;

public enum Command {

	REST,
	MOVE_EAST(Direction.E),
	MOVE_SOUTH(Direction.S),
	MOVE_NORTH(Direction.N),
	MOVE_WEST(Direction.W),
	SHOOT_EAST(Direction.E),
	SHOOT_WEST(Direction.W),
	SHOOT_NORTH(Direction.N),
	SHOOT_SOUTH(Direction.S);


	final private Direction direction;

	private Command() {
		this(null);
	}

	private Command(Direction associatedDirection) {
		direction = associatedDirection;
	}

	public static Command fromInputString(String input) throws UnknownCommand {
		Command decoded = null;
		input = input.toLowerCase();
		if (isRestCommand(input)) {
			decoded = REST;
		} else if (isShootCommand(input)) {
			decoded = decodeShootCommand(input);
		} else {
			decoded = decodeMovementCommand(input);
		}
		return decoded;
	}

	public Direction direction() {
		return this.direction;
	}



	private static boolean isRestCommand(String input) {
		return input.equals("rest") || input.equals("r");
	}

	private static boolean isShootCommand(String input) {
		return input.startsWith("shoot ") || input.startsWith("s ") ||
				(input.startsWith("s") && input.length() == 2);
	}

	private static Command decodeMovementCommand(String input) throws UnknownCommand {
		String dirString;
		if (input.startsWith("go ")) {
			dirString = input.substring(3);
		} else {
			dirString = input;
		}

		Direction d = Direction.matchString(dirString);
		if (d != null) {
			switch (d) {
			case E: return MOVE_EAST;
			case W: return MOVE_WEST;
			case N: return MOVE_NORTH;
			case S: return MOVE_SOUTH;
			}
		}
		throw new UnknownCommand(input);
	}

	private static Command decodeShootCommand(String input) throws UnknownCommand {
		String dirString;
		if (input.length() == 2) {
			dirString = input.substring(1);
		} else {
			dirString = input.substring(input.indexOf(' ') + 1);
		}
		Direction d = Direction.matchString(dirString);
		if (d != null) {
			switch (d) {
			case E: return SHOOT_EAST;
			case W: return SHOOT_WEST;
			case N: return SHOOT_NORTH;
			case S: return SHOOT_SOUTH;
			}
		}
		throw new UnknownCommand(input);
	}
}
