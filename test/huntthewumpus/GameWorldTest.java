package huntthewumpus;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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
		//    [4]
		// [1][2][3]
		//    [5]
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
	public void gameWorldTracksWumpusLocation() {
		world.setPlayerLocation(northRoom);
		world.setWumpusLocation(southRoom);
		assertThat(world.whereIsWumpus(), is(sameInstance(southRoom)));
	}

	@Test
	public void playerPicksUpArrows() throws Exception {
		world.setPlayerLocation(centerRoom);
		northRoom.addArrows(1);
		assertThat(world.getArrowCount(), is(0));
		world.movePlayer(Direction.N);
		assertThat(world.getArrowCount(), is(1));
		assertThat(northRoom.containsArrows(), is(false));
	}

	@Test
	public void whereIsWumpusReturnsNullWithoutWumpus() {
		assertThat(world.whereIsWumpus(), is(nullValue()));
	}

	@Test
	public void getRoomAssignsRoomNumber() {
		Room r = world.getRoom(42);
		assertThat(r.getRoomNumber(), is(42));
	}

	@Test(expected=PlayerNotFound.class)
	public void movePlayerFailsWithoutPlayer() throws Exception {
		world.movePlayer(Direction.E);
	}

	@Test(expected=GameOver.class)
	public void gameEndsWhenPlayerEntersRoomWithPit() throws Exception {
		world.setPlayerLocation(centerRoom);
		eastRoom.setContents(RoomObject.pit);
		world.movePlayer(Direction.E);
	}

	@Test
	public void setPlayerLocationResetsExistingLocation() throws Exception {
		world.setPlayerLocation(westRoom);
		world.movePlayer(Direction.E);
		assertThat(world.whereIsPlayer(), is(sameInstance(centerRoom)));
		world.movePlayer(Direction.E);
		assertThat(world.whereIsPlayer(), is(sameInstance(eastRoom)));
		world.setPlayerLocation(southRoom);
		world.movePlayer(Direction.N);
		assertThat(world.whereIsPlayer(), is(sameInstance(centerRoom)));
	}

	@Test(expected=InvalidMove.class)
	public void cannotMoveWithoutPeer() throws Exception {
		world.setPlayerLocation(northRoom);
		world.movePlayer(Direction.N);
	}

	@Test
	public void locationIsNotChangedOnInvalidMove() throws Exception {
		world.setPlayerLocation(southRoom);
		try {
			world.movePlayer(Direction.S);
		} catch (InvalidMove e) {
			// expected
		}
		assertThat(world.whereIsPlayer(), is(sameInstance(southRoom)));
	}

	@Test
	public void twoRoomCavernWithBatsWorks() throws Exception {
		GameWorld world = new GameWorld();
		Room playerRoom = world.getRoom(0);
		Room batsRoom = world.getRoom(1);
		world.connectRooms(playerRoom, Direction.S, batsRoom);
		world.setPlayerLocation(playerRoom);
		batsRoom.setContents(RoomObject.bats);
		world.movePlayer(Direction.S);


		Room newLocation = world.whereIsPlayer();
		assertThat(newLocation, is(sameInstance(playerRoom)));
		assertThat(newLocation.getContents(), is(RoomObject.player));
	}

	@Test
	public void moveWumpusChoosesNewLocation() {
		GameWorld world = new GameWorld();
		Room wumpusRoom = world.getRoom(0);
		world.connectRooms(wumpusRoom, Direction.E, world.getRoom(1));
		world.setWumpusLocation(wumpusRoom);
		world.moveWumpus();

		Room newLocation = world.whereIsWumpus();
		assertThat(newLocation, is(sameInstance(world.getRoom(1))));
		assertThat(newLocation.getContents(), is(RoomObject.wumpus));
	}

	@Test
	public void moveWumpusChoosesNewLocationAtRandom() {
		// The wumpus is supposed to move into a random adjacent room with
		// every clock tick.  Since we are dealing with "random" here, the test
		// runs for a bunch of ticks and keeps track of which room the wumpus
		// moved to.  We should see that the wumpus visited each peer room.
		int[] counters = new int[6];
		for (int i=0; i<100; ++i) {
			world.setWumpusLocation(world.getRoom(2));
			assertThat(world.whereIsWumpus().getRoomNumber(), is(2));
			world.moveWumpus();

			Room newLocation = world.whereIsWumpus();
			counters[newLocation.getRoomNumber()]++;
		}
		for (int i=1; i<counters.length; ++i) {
			if (i != 2 && counters[i] == 0) {
				for (int j=0; j<counters.length; ++j) {
					System.err.print(" " + String.valueOf(counters[j]));
				}
				System.err.println();
				fail(String.format("counter[%d] is zero", i));
			}
		}
	}

	@Test
	public void resetWorldClearsMap() {
		world.setPlayerLocation(centerRoom);
		world.setWumpusLocation(northRoom);
		southRoom.setContents(RoomObject.pit);
		eastRoom.setContents(RoomObject.bats);

		world.reset();

		assertThat(centerRoom.getContents(), is(RoomObject.empty));
		assertThat(northRoom.getContents(), is(RoomObject.empty));
		assertThat(southRoom.getContents(), is(RoomObject.empty));
		assertThat(eastRoom.getContents(), is(RoomObject.empty));
	}
}
