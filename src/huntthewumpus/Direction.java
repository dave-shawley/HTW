package huntthewumpus;

public enum Direction {
	E, W, S, N;

	public Direction reverse() {
		switch (this) {
		case E: return W;
		case W: return E;
		case S: return N;
		case N: return S;
		}
		throw new RuntimeException(this.toString() + " is an illegal Direction");
	}

	public String longName() {
		switch (this) {
		case E: return "east";
		case W: return "west";
		case N: return "north";
		case S: return "south";
		}
		throw new RuntimeException(this.toString() + " is an illegal Direction");
	}

	public static Direction matchString(String name) {
		for (Direction d: Direction.values()) {
			if (d.name().equalsIgnoreCase(name) || d.longName().equalsIgnoreCase(name)) {
				return d;
			}
		}
		return null;
	}

}

