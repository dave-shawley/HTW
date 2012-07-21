package huntthewumpus.fixtures;

import huntthewumpus.Direction;
import huntthewumpus.GameWorld;
import huntthewumpus.Room;


public class MakeMap {
	private final GameWorld world;
	private Room startRoom;
	private Room endRoom;
	private Direction direction;

	public MakeMap() {
		world = GameDriver.getWorld();
	}

	public void execute() {
		world.connectRooms(startRoom, direction, endRoom);
	}

	public void setStart(int startRoom) {
		this.startRoom = world.getRoom(startRoom);
	}

	public void setEnd(int endRoom) {
		this.endRoom = world.getRoom(endRoom);
	}

	public void setDirection(String direction) {
		this.direction = Direction.valueOf(direction);
	}
}
