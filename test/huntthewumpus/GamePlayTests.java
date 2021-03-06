package huntthewumpus;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;


public class GamePlayTests {
	private static final int WEST_ROOM = 1;
	private static final int CENTER_ROOM = 2;
	private static final int EAST_ROOM = 3;
	private static final int NORTH_ROOM = 4;
	private static final int SOUTH_ROOM = 5;
	private static final int FAR_NORTH_ROOM = 6;
	private GameWorld world;


	@Test
	public void movePlayerEast() throws Exception {
		world.movePlayer(Direction.E);
		assertThat(world.whereIsPlayer(), is(sameInstance(world.getRoom(EAST_ROOM))));
	}

	@Test
	public void movePlayerWest() throws Exception {
		world.movePlayer(Direction.W);
		assertThat(world.whereIsPlayer(), is(sameInstance(world.getRoom(WEST_ROOM))));
	}

	@Test
	public void movePlayerNorth() throws Exception {
		world.movePlayer(Direction.N);
		assertThat(world.whereIsPlayer(), is(sameInstance(world.getRoom(NORTH_ROOM))));
	}

	@Test
	public void movePlayerSouth() throws Exception {
		world.movePlayer(Direction.S);
		assertThat(world.whereIsPlayer(), is(sameInstance(world.getRoom(SOUTH_ROOM))));
	}

	@Test
	public void playerCannotMoveIntoRoomWithBats() throws Exception {
		world.getRoom(EAST_ROOM).setContents(RoomObject.bats);
		world.movePlayer(Direction.E);
		assertThat(world.whereIsPlayer(), is(not(nullValue())));
		assertThat(world.whereIsPlayer(), is(not(world.getRoom(EAST_ROOM))));
	}

	@Test
	public void arrowContinuesUntilItHitsAWall() throws Exception {
		world.getRoom(WEST_ROOM).addArrows(10);
		world.movePlayer(Direction.W); // will pick up 10 arrows
		world.shootArrow(Direction.E);
		assertThat(world.getRoom(EAST_ROOM).getArrowCount(), is(equalTo(1)));

		world.movePlayer(Direction.E);
		world.shootArrow(Direction.N);
		assertThat(world.getRoom(FAR_NORTH_ROOM).getArrowCount(), is(equalTo(1)));

		world.shootArrow(Direction.S);
		assertThat(world.getRoom(SOUTH_ROOM).getArrowCount(), is(equalTo(1)));
	}

	@Test(expected=GameOver.class)
	public void gameEndsIfPlayerShootsWallInSameRoom() throws Exception {
		world.addArrows(5);
		world.movePlayer(Direction.E);
		world.shootArrow(Direction.E);
	}

	@Test(expected=GameOver.class)
	public void wumpusDiesWhenHitWithArrow() throws Exception {
		world.addArrows(1);
		world.getRoom(NORTH_ROOM).setContents(RoomObject.wumpus);
		world.shootArrow(Direction.N);
	}


	@Before
	public void createCrossShapedMap() {
		world = new GameWorld();
		world.connectRooms(world.getRoom(CENTER_ROOM), Direction.N, world.getRoom(NORTH_ROOM));
		world.connectRooms(world.getRoom(CENTER_ROOM), Direction.E, world.getRoom(EAST_ROOM));
		world.connectRooms(world.getRoom(CENTER_ROOM), Direction.S, world.getRoom(SOUTH_ROOM));
		world.connectRooms(world.getRoom(CENTER_ROOM), Direction.W, world.getRoom(WEST_ROOM));
		world.connectRooms(world.getRoom(NORTH_ROOM), Direction.N, world.getRoom(FAR_NORTH_ROOM));
		world.setPlayerLocation(world.getRoom(CENTER_ROOM));
	}

}
