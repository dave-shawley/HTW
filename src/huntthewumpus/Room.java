package huntthewumpus;

import java.util.HashMap;
import java.util.Map;

public class Room {
	private final int roomNumber;
	private final Map<Direction, Room> peerMap;
	private RoomObject content;


	public Room(int roomNumber) {
		this.roomNumber = roomNumber;
		this.peerMap = new HashMap<Direction, Room>();
		this.content = RoomObject.empty;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public void setPeer(Direction passageDirection, Room otherRoom) {
		peerMap.put(passageDirection, otherRoom);
	}

	public Room getPeer(Direction passageDirection) {
		return peerMap.get(passageDirection);
	}

	public void setContents(RoomObject object) {
		this.content = object;
	}

	public void clearContents() {
		content = RoomObject.empty;
	}

	public RoomObject getContents() {
		return content;
	}

	@Override
	public String toString() {
		return (new StringBuffer(""))
				.append(getClass().getCanonicalName()).append("(")
				.append("roomNumber=").append(getRoomNumber()).append(", ")
				.append("content=").append(getContents())
				.append(")").toString();
	}
}
