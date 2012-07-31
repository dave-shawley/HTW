package huntthewumpus;

import java.util.HashMap;
import java.util.Map;


public class GameWorld {
	private final Map<Integer, Room> rooms = new HashMap<Integer, Room>();
	private int numArrows = 0;

	public Room getRoom(int room) {
		Integer roomNumber = Integer.valueOf(room);
		Room r = rooms.get(roomNumber);
		if (r == null) {
			r = new Room(room);
			rooms.put(roomNumber, r);
		}
		return r;
	}

	public void connectRooms(Room startRoom, Direction passageDirection, Room endRoom) {
		startRoom.setPeer(passageDirection, endRoom);
		endRoom.setPeer(passageDirection.reverse(), startRoom);
	}

	public void movePlayer(Direction direction) throws GameOver, InvalidMove {
		Room currentLocation = whereIsPlayer();
		if (currentLocation == null) {
			throw new PlayerNotFound();
		}
		Room nextRoom = currentLocation.getPeer(direction);

		if (nextRoom == null) {
			throw new InvalidMove(direction);
		}
		nextRoom = handleBatsInRoom(nextRoom);

		if (nextRoom.getContents() == RoomObject.pit) {
			throw new GameOver("You fall into a pit and die.");
		}

		if (nextRoom != currentLocation) {
			nextRoom.setContents(RoomObject.player);
			currentLocation.clearContents();
			numArrows += nextRoom.takeArrows();
		}
	}

	/**
	 * Keep moving the player until in a room without bats.
	 *
	 * @param nextRoom  the room that the player is moving into
	 * @return the final room that the player ends up in
	 */
	private Room handleBatsInRoom(Room nextRoom) {
		if (nextRoom.getContents() == RoomObject.bats) {
			nextRoom = getRandomRoomNotContaining(RoomObject.bats);
		}
		return nextRoom;
	}

	/**
	 * Find a room that does not contain a certain object.
	 * @param objectToExclude  skip rooms with this object
	 * @return a random room that does not contain {@code objectToExclude}
	 */
	private Room getRandomRoomNotContaining(RoomObject objectToExclude) {
		Room nextRoom = null;
		do {
			int randomRoomIndex = (int) (Math.random() * rooms.size());
			for (Room r: rooms.values()) {
				nextRoom = r;
				if (randomRoomIndex-- <= 0) {
					break;
				}
			}
		} while (nextRoom.getContents() == objectToExclude);

		return nextRoom;
	}

	/**
	 * @return the room that the player is currently in, <code>null</code> if player not found
	 */
	public Room whereIsPlayer() {
		for (Room r: rooms.values()) {
			if (r.getContents() == RoomObject.player) {
				return r;
			}
		}
		return null;
	}

	public void setPlayerLocation(Room room) {
		Room currentLocation = whereIsPlayer();
		if (currentLocation != null) {
			currentLocation.clearContents();
		}
		room.setContents(RoomObject.player);
	}

	public void setWumpusLocation(Room room) {
		Room currentLocation = whereIsWumpus();
		if (currentLocation != null) {
			currentLocation.clearContents();
		}
		room.setContents(RoomObject.wumpus);
	}

	public Room whereIsWumpus() {
		for (Room r: rooms.values()) {
			if (r.getContents() == RoomObject.wumpus) {
				return r;
			}
		}
		return null;
	}

	public void moveWumpus() {
		Room wumpusLocation = whereIsWumpus();
		if (wumpusLocation != null) {
			int roomIndex = ((int) (Math.random() * 4.0)) + 1;
			while (roomIndex > 0) {
				for (Direction d: Direction.values()) {
					Room peer = wumpusLocation.getPeer(d);
					if (peer != null) {
						roomIndex--;
						if (roomIndex == 0) {
							setWumpusLocation(peer);
							break;
						}
					}
				}
			}
		}
	}

	public void addArrows(int numArrows) {
		this.numArrows += numArrows;
	}

	public int getArrowCount() {
		return numArrows;
	}

	public void shootArrow(Direction dir) throws GameOver {
		Room startRoom = whereIsPlayer();
		Room r = startRoom; // where the arrow currently is
		Room lastRoom = r;  // where the arrow lands
		while (r != null) {
			if (r.getContents() == RoomObject.wumpus) {
				throw new GameOver("You have killed the Wumpus.");
			}
			lastRoom = r;
			r = r.getPeer(dir);
		}
		if (lastRoom == startRoom) {
			throw new GameOver("The arrow bounced off the wall and killed you.");
		}
		lastRoom.addArrows(1);
		--numArrows;
	}

	public void reset() {
		for (Room r: rooms.values()) {
			r.clearContents();
		}
	}
}
