package huntthewumpus;

public enum Command {
	MOVE_EAST, MOVE_SOUTH, MOVE_NORTH, MOVE_WEST, REST;

	public static Command fromInputString(String input) throws UnknownCommand {
		input = input.toLowerCase();

		if (input.equals("rest") || input.equals("r")) {
			return REST;
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

}
