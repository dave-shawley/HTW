package huntthewumpus;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;


public class WorldCreationTests {
	private GameWorld world;

	@Before
	public void createWorld() {
		world = new GameWorld();
	}

	@Test
	public void roomIsCreatedOnDemand() {
		Room r = world.getRoom(1);
		assertThat(r, is(not(nullValue())));
	}

	@Test
	public void roomHasContents() {
		Room r = world.getRoom(1);
		r.setContents(RoomObject.player);
		assertThat(r.getContents(), is(RoomObject.player));
	}

	@Test
	public void numberedRoomsAreOnlyCreatedOnce() {
		Room r = world.getRoom(1);
		assertThat(world.getRoom(1), is(sameInstance(r)));
	}

	@Test
	public void roomConnectionsAreBidirecional() {
		Room roomOne = world.getRoom(1);
		Room roomTwo = world.getRoom(2);
		world.connectRooms(roomOne, Direction.E, roomTwo);
		assertThat(roomOne.getPeer(Direction.E), is(sameInstance(roomTwo)));
		assertThat(roomTwo.getPeer(Direction.W), is(sameInstance(roomOne)));
	}

}
