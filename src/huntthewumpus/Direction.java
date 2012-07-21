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

}
