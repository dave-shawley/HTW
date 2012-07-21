package huntthewumpus;

import java.util.HashMap;
import java.util.Map;


public class GameWorld {
	private final Map<Integer, Room> rooms = new HashMap<Integer, Room>();

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

	public void movePlayer(Direction direction) {
		Room currentLocation = whereIsPlayer();
		Room nextRoom = currentLocation.getPeer(direction);

		while ("bats".equalsIgnoreCase(nextRoom.getContents())) {
			int randomRoomIndex = (int) (Math.random() * rooms.size());
			for (Room r: rooms.values()) {
				nextRoom = r;
				if (randomRoomIndex-- <= 0) {
					break;
				}
			}
		}

		if (nextRoom != currentLocation) {
			setPlayerLocation(nextRoom);
			currentLocation.setContents(null);
		}
	}

	public Room whereIsPlayer() {
		for (Room r: rooms.values()) {
			if ("player".equals(r.getContents())) {
				return r;
			}
		}
		throw new PlayerNotFound();
	}

	public void setPlayerLocation(Room room) {
		room.setContents("player");
	}

	public void addBatsInCavern(Room room) {
		room.setContents("bats");
	}

}
