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
		input = input.toLowerCase();

		if (input.equals("rest") || input.equals("r")) {
			return REST;
		}

		if (input.startsWith("shoot ")) {
			input = input.substring("shoot ".length());
			for (Direction d: Direction.values()) {
				if (d.toString().equalsIgnoreCase(input) || d.longName().equalsIgnoreCase(input)) {
					switch (d) {
					case E: return SHOOT_EAST;
					case W: return SHOOT_WEST;
					case N: return SHOOT_NORTH;
					case S: return SHOOT_SOUTH;
					}
				}
			}
			throw new UnknownCommand(input);
		}

		if (input.startsWith("go ")) {
			// movement command
			input = input.substring(3);
		}

		for (Direction d: Direction.values()) {
			if (d.toString().equalsIgnoreCase(input) || d.longName().equalsIgnoreCase(input)) {
				switch (d) {
				case E: return MOVE_EAST;
				case W: return MOVE_WEST;
				case N: return MOVE_NORTH;
				case S: return MOVE_SOUTH;
				}
			}
		}

		throw new UnknownCommand(input);
	}

	public Direction direction() {
		return this.direction;
	}

}
