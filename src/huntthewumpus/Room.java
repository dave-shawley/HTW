package huntthewumpus;

import java.util.HashMap;
import java.util.Map;

public class Room {
	private final int roomNumber;
	private final Map<Direction, Room> peerMap;
	private String content;


	public Room(int roomNumber) {
		this.roomNumber = roomNumber;
		this.peerMap = new HashMap<Direction, Room>();
		this.content = null;
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

	public void setContents(String contents) {
		this.content = contents;
	}

	public String getContents() {
		return content;
	}

	@Override
	public String toString() {
		return (new StringBuffer(""))
				.append(getClass().getCanonicalName()).append("(")
				.append("roomNumber=").append(getRoomNumber())
				.append(")").toString();
	}
}
