package huntthewumpus;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class RoomTest {

	@Test
	public void roomsAreNumbered() {
		Room r = new Room(1);
		assertThat(r.getRoomNumber(), is(1));
	}

	@Test
	public void roomHasPeersInCardinalDirections() {
		//    [4]
		// [1][2][3]
		//    [5]
		Room r = new Room(2);
		Room westernRoom = new Room(1);
		Room easternRoom = new Room(3);
		Room northernRoom = new Room(4);
		Room southernRoom = new Room(5);

		r.setPeer(Direction.W, westernRoom);
		r.setPeer(Direction.E, easternRoom);
		r.setPeer(Direction.N, northernRoom);
		r.setPeer(Direction.S, southernRoom);

		assertThat(r.getPeer(Direction.E), is(sameInstance(easternRoom)));
		assertThat(r.getPeer(Direction.W), is(sameInstance(westernRoom)));
		assertThat(r.getPeer(Direction.N), is(sameInstance(northernRoom)));
		assertThat(r.getPeer(Direction.S), is(sameInstance(southernRoom)));
	}

	@Test
	public void toStringIncludesRoomNumber() {
		Room r = new Room(42);
		assertThat(r.toString().contains(String.valueOf(r.getRoomNumber())), is(true));
	}

	@Test
	public void roomHasContents() {
		Room r = new Room(42);
		r.setContents(RoomObject.player);
		assertThat(r.getContents(), is(RoomObject.player));
	}

	@Test
	public void roomIsEmptyByDefault() {
		Room r = new Room(42);
		assertThat(r.getContents(), is(RoomObject.empty));
		assertThat(r.containsArrows(), is(false));
	}

	@Test
	public void roomCanContainArrows() {
		Room r = new Room(0);
		r.addArrows(12);
		assertThat(r.containsArrows(), is(true));
		assertThat(r.getArrowCount(), is(equalTo(12)));
	}

	@Test
	public void takeArrowsRemovesAllArrows() {
		Room r = new Room(0);
		r.addArrows(12);
		assertThat(r.takeArrows(), is(equalTo(12)));
		assertThat(r.containsArrows(), is(false));
	}
}
