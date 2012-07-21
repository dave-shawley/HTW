package huntthewumpus;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;


public class GameWorldTest {

	private GameWorld world;
	private Room westRoom;
	private Room centerRoom;
	private Room eastRoom;
	private Room northRoom;
	private Room southRoom;

	@Before
	public void createWorld() {
		world = new GameWorld();
		westRoom = world.getRoom(1);
		centerRoom = world.getRoom(2);
		eastRoom = world.getRoom(3);
		northRoom = world.getRoom(4);
		southRoom = world.getRoom(5);
		world.connectRooms(centerRoom, Direction.N, northRoom);
		world.connectRooms(centerRoom, Direction.E, eastRoom);
		world.connectRooms(centerRoom, Direction.S, southRoom);
		world.connectRooms(centerRoom, Direction.W, westRoom);
	}


	@Test
	public void verifyCrossShapedMap() {
		assertThat(centerRoom.getPeer(Direction.E), is(sameInstance(eastRoom)));
		assertThat(centerRoom.getPeer(Direction.W), is(sameInstance(westRoom)));
		assertThat(centerRoom.getPeer(Direction.N), is(sameInstance(northRoom)));
		assertThat(centerRoom.getPeer(Direction.S), is(sameInstance(southRoom)));
	}

	@Test
	public void gameWorldTracksPlayerLocation() {
		world.setPlayerLocation(centerRoom);
		assertThat(world.whereIsPlayer(), is(sameInstance(centerRoom)));
	}

	@Test
	public void getRoomAssignsRoomNumber() {
		Room r = world.getRoom(42);
		assertThat(r.getRoomNumber(), is(42));
	}

	@Test(expected=PlayerNotFound.class)
	public void gameWorldFailsWithoutPlayer() {
		world.whereIsPlayer();
	}

}
