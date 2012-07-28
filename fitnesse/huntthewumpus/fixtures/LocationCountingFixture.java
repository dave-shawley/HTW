package huntthewumpus.fixtures;

import huntthewumpus.Room;

import java.util.HashMap;
import java.util.Map;


public class LocationCountingFixture {
	private final Map<Integer, Integer> counters;
	private Integer cavernQuery;

	public LocationCountingFixture() {
		counters = new HashMap<Integer, Integer>();
	}

	public void setCavern(int query) {
		cavernQuery = Integer.valueOf(query);
	}

	public int count() {
		Integer value = counters.get(cavernQuery);
		if (value == null) {
			return 0;
		}
		return value.intValue();
	}

	protected void markRoom(Room r) {
		Integer roomKey = Integer.valueOf(r.getRoomNumber());
		Integer marker = counters.get(roomKey);
		if (marker == null) {
			marker = new Integer(0);
		}
		counters.put(roomKey, marker.intValue() + 1);
	}
}
