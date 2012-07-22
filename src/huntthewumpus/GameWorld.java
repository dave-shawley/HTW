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

	public void movePlayer(Direction direction) throws GameOver, InvalidMove {
		Room currentLocation = whereIsPlayer();
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
		}
	}

	/**
	 * Keep moving the player until in a room without bats.
	 *
	 * @param nextRoom  the room that the player is moving into
	 * @return the final room that the player ends up in
	 */
	private Room handleBatsInRoom(Room nextRoom) {
		while (nextRoom.getContents() == RoomObject.bats) {
			int randomRoomIndex = (int) (Math.random() * rooms.size());
			for (Room r: rooms.values()) {
				nextRoom = r;
				if (randomRoomIndex-- <= 0) {
					break;
				}
			}
		}
		return nextRoom;
	}

	/**
	 * @return the room that the player is currently in
	 * @throws PlayerNotFound if the map does not contain a player
	 */
	public Room whereIsPlayer() {
		for (Room r: rooms.values()) {
			if (r.getContents() == RoomObject.player) {
				return r;
			}
		}
		throw new PlayerNotFound();
	}

	public void setPlayerLocation(Room room) {
		try {
			Room currentLocation = whereIsPlayer();
			currentLocation.clearContents();
		} catch (PlayerNotFound e) {
			// ignore this
		}
		room.setContents(RoomObject.player);
	}

	public void addBatsInCavern(Room room) {
		room.setContents(RoomObject.bats);
	}

	public void addPitInCavern(Room room) {
		room.setContents(RoomObject.pit);
	}

}
