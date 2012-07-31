package huntthewumpus;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import huntthewumpus.fixtures.ProgrammableGenerator;
import huntthewumpus.fixtures.RecordingGameDisplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Test;


public class GameTest {
	@Test
	public void testConstructorArguments() {
		GameWorld world = new GameWorld();
		GameDisplay display = new RecordingGameDisplay();

		for (int i=0; i<2; ++i) {
			for (int j=0; j<2; ++j) {
				for (int k=0; k<2; ++k) {
					boolean exceptionCaught = false;
					try {
						CommandGenerator generator = null;
						if (world == null || display == null) {
							// this is the only case where we want a generator
							generator = new ProgrammableGenerator();
						}
						new Game(world, display, generator);
					} catch (IllegalArgumentException exc) {
						exceptionCaught = true;
					}
					assertThat(exceptionCaught, is(true));
				}
				display = null;
			}
			world = null;
		}
	}

	@Test
	public void gameConstructorRequiresNonNullArguments() {
		new Game(new GameWorld(), new RecordingGameDisplay(), new ProgrammableGenerator());
	}

	@Test
	public void commandsAreTakenFromGenerator() {
		GameBundle g = GameBundle.forInput("n");
		g.tick();
		assertThat(g.generator.getCommandIndex(), is(1));
	}

	@Test
	public void moveCommandsCallCorrectWorldMethods() {
		Direction[] expected = { Direction.E, Direction.N, Direction.W, Direction.S };
		final List<Direction> recorded = new ArrayList<Direction>();
		GameWorld world = new GameWorld() {
			@Override public void movePlayer(Direction d) {
				recorded.add(d);
			}
		};

		world.setPlayerLocation(world.getRoom(0));
		ProgrammableGenerator generator = new ProgrammableGenerator("E", "N", "W", "S");
		Game g = new Game(world, new RecordingGameDisplay(), generator);

		for (int i=0; i<expected.length; ++i) {
			g.tick();
		}
		assertThat(recorded.size(), is(equalTo(expected.length)));
		assertThat(recorded, is(equalTo(Arrays.asList(expected))));
	}

	@Test
	public void playerDiesWhenEnteringRoomWithPit() {
		GameBundle g = GameBundle.forInput("E");
		Room playerRoom = g.world.whereIsPlayer();
		Room pitRoom = g.world.getRoom(1);
		g.world.connectRooms(playerRoom, Direction.E, pitRoom);
		pitRoom.setContents(RoomObject.pit);

		g.tick();
		assertTrue(g.display.lineWasDisplayed("You fall into a pit and die."));
		assertTrue(g.display.lineWasDisplayed("Game over."));
		assertTrue(g.hasTerminated());
	}

	@Test
	public void playerHearsBats() {
		GameBundle g = GameBundle.forInput("E");
		Room playerRoom = g.world.whereIsPlayer();
		Room batRoom = g.world.getRoom(2);
		g.world.connectRooms(playerRoom, Direction.E, g.world.getRoom(1));
		g.world.connectRooms(g.world.getRoom(1), Direction.E, batRoom);
		g.world.setPlayerLocation(playerRoom);
		batRoom.setContents(RoomObject.bats);

		g.tick();
		assertTrue(g.display.lineWasDisplayed("You hear chirping."));
	}

	@Test
	public void playerHearsPit() {
		GameBundle g = GameBundle.forInput("E");
		Room playerRoom = g.world.getRoom(0);
		Room pitRoom = g.world.getRoom(2);
		g.world.connectRooms(playerRoom, Direction.E, g.world.getRoom(1));
		g.world.connectRooms(g.world.getRoom(1), Direction.E, pitRoom);
		g.world.setPlayerLocation(playerRoom);
		pitRoom.setContents(RoomObject.pit);

		g.tick();
		assertTrue(g.display.lineWasDisplayed("You hear wind."));
	}

	@Test
	public void playerWarnedOnInvalidMove() {
		GameBundle g = GameBundle.withPlayerInRoom(0);
		g.doCommand("E");
		assertTrue(g.display.lineWasDisplayed("You can't go east from here."));
		g.doCommand("W");
		assertTrue(g.display.lineWasDisplayed("You can't go west from here."));
		g.doCommand("N");
		assertTrue(g.display.lineWasDisplayed("You can't go north from here."));
		g.doCommand("S");
		assertTrue(g.display.lineWasDisplayed("You can't go south from here."));
	}

	@Test
	public void playerCanRest() {
		GameBundle g = GameBundle.withPlayerInRoom(0);
		g.doCommand("rest");
		g.doCommand("r");
	}

	@Test
	public void playerRemindedOfArrowCount() {
		GameBundle g = GameBundle.withPlayerInRoom(0);
		g.world.connectRooms(g.world.getRoom(0), Direction.N, g.world.getRoom(1));
		g.world.getRoom(1).addArrows(1);
		g.world.connectRooms(g.world.getRoom(1), Direction.N, g.world.getRoom(2));
		g.world.getRoom(2).addArrows(1);

		g.doCommand("rest");
		assertTrue(g.display.lineWasDisplayed("You have no arrows."));
		g.doCommand("north");
		assertTrue(g.display.lineWasDisplayed("You found an arrow."));
		assertTrue(g.display.lineWasDisplayed("You have 1 arrow."));
		g.doCommand("north");
		assertTrue(g.display.lineWasDisplayed("You found an arrow."));
		assertTrue(g.display.lineWasDisplayed("You have 2 arrows."));
	}

	@Test
	public void gameHasNotTerminatedByDefault() {
		Game g = GameBundle.emptyMap();
		assertThat(g.hasTerminated(), is(false));
	}

	@Test
	public void restWorksWithoutPlayer() {
		GameBundle g = GameBundle.emptyMap();
		g.doCommand("r");
	}

	@Test
	public void unknownCommandsDisplayErrorMessage() {
		String randomInputValue = UUID.randomUUID().toString();
		GameBundle g = GameBundle.withPlayerInRoom(0);
		g.doCommand(randomInputValue);
		assertTrue(g.display.lineWasDisplayed(String.format("I don't know how to %s.", randomInputValue)));
	}

	@Test
	public void directionsAreReportedAfterResting() {
		GameBundle g = GameBundle.withPlayerInRoom(0);
		// TODO is this required?
//		g.doCommand("rest");
//		assertTrue(g.display.getDisplayedOutput().isEmpty());

		g.world.connectRooms(g.world.getRoom(0), Direction.N, g.world.getRoom(1));
		g.doCommand("rest");
		assertTrue(g.display.lineWasDisplayed("You can go north from here."));

		g.world.connectRooms(g.world.getRoom(0), Direction.E, g.world.getRoom(2));
		g.doCommand("rest");
		assertTrue(g.display.lineWasDisplayed("You can go north and east from here."));

		g.world.connectRooms(g.world.getRoom(0), Direction.S, g.world.getRoom(3));
		g.doCommand("rest");
		assertTrue(g.display.lineWasDisplayed("You can go north, south and east from here."));

		g.world.connectRooms(g.world.getRoom(0), Direction.W, g.world.getRoom(4));
		g.doCommand("rest");
		assertTrue(g.display.lineWasDisplayed("You can go north, south, east and west from here."));
	}

	@Test(timeout=10)
	public void playerSmellsWumpus() {
		GameBundle g = GameBundle.withPlayerInRoom(0);
		Room wumpusRoom = g.world.getRoom(2);
		g.world.connectRooms(g.world.whereIsPlayer(), Direction.E, g.world.getRoom(1));
		g.world.connectRooms(g.world.getRoom(1), Direction.N, wumpusRoom);
		g.world.setWumpusLocation(wumpusRoom);

		waitForWumpusToMove(g);
		assertTrue(g.display.lineWasDisplayed("You smell the Wumpus."));
	}

	@Test(timeout=10) @SuppressWarnings("unchecked")
	public void wumpusMovesToAdjacentRoomWithEachTick() {
		GameBundle g = GameBundle.withPlayerInRoom(0);
		Room wumpusRoom = createWumpusMap(g);
		for (int i=0; i<5; ++i) {
			g.world.setWumpusLocation(wumpusRoom);
			waitForWumpusToMove(g);
			assertThat(
					g.world.whereIsWumpus(),
					is(anyOf(sameInstance(g.world.getRoom(1)),
                             sameInstance(g.world.getRoom(3)),
                             sameInstance(g.world.getRoom(4)),
                             sameInstance(g.world.getRoom(5)))));
		}
	}

	@Test
	public void wumpusCanBeFrozen() throws Exception {
		GameBundle g = GameBundle.withPlayerInRoom(0);
		Room wumpusRoom = createWumpusMap(g);
		g.world.setWumpusLocation(wumpusRoom);
		g.freezeWumpus();
		for (int i=0; i<5; ++i) {
			g.doCommand("rest");
			assertThat(g.world.whereIsWumpus(), is(sameInstance(wumpusRoom)));
		}
	}

	@Test(timeout=10)
	public void wumpusCanBeThawed() throws Exception {
		GameBundle g = GameBundle.withPlayerInRoom(0);
		Room wumpusRoom = createWumpusMap(g);
		g.world.setWumpusLocation(wumpusRoom);
		g.freezeWumpus();
		for (int i=0; i<5; ++i) {
			g.doCommand("rest");
			assertThat(g.world.whereIsWumpus(), is(sameInstance(wumpusRoom)));
		}

		Room wumpusStartRoom = g.world.whereIsWumpus();
		g.thawWumpus();
		for (int i=0; i<5; ++i) {
			g.world.setWumpusLocation(wumpusStartRoom);
			waitForWumpusToMove(g);
			assertThat(g.world.whereIsWumpus(), is(not(sameInstance(wumpusStartRoom))));
		}
	}

	@Test
	public void wumpusSleepsOccasionally() {
		GameBundle g = GameBundle.withPlayerInRoom(0);
		g.world.connectRooms(g.world.getRoom(1), Direction.E, g.world.getRoom(2));
		g.world.setWumpusLocation(g.world.getRoom(1));

		boolean wumpusHasSlept = false;
		for (int i=0; i<100 && !wumpusHasSlept; ++i) {
			Room startLocation = g.world.whereIsWumpus();
			g.doCommand("r");
			Room endLocation = g.world.whereIsWumpus();
			wumpusHasSlept = startLocation.getRoomNumber() == endLocation.getRoomNumber();
		}
		assertTrue(wumpusHasSlept);
	}

	@Test
	public void playerNotifiedWhenAnArrowIsPickedUp() {
		GameBundle g = GameBundle.withPlayerInRoom(0);
		g.world.connectRooms(g.world.getRoom(0), Direction.N, g.world.getRoom(1));
		g.world.getRoom(1).addArrows(12);
		g.doCommand("N");
		assertTrue(g.display.lineWasDisplayed("You found an arrow."));
	}

	@Test
	public void playerCannotShootWithoutArrows() {
		GameBundle g = GameBundle.withPlayerInRoom(0);
		g.world.connectRooms(g.world.getRoom(0), Direction.E, g.world.getRoom(1));
		g.world.connectRooms(g.world.getRoom(0), Direction.W, g.world.getRoom(2));
		g.world.connectRooms(g.world.getRoom(0), Direction.N, g.world.getRoom(3));
		g.world.connectRooms(g.world.getRoom(0), Direction.S, g.world.getRoom(4));
		for (Direction d: Direction.values()) {
			g.doCommand("shoot " + d.longName());
			assertTrue("failed to shoot " + d.longName(),
					g.display.lineWasDisplayed("You don't have any arrows."));
			g.display.clearOutput();
		}
	}

	@Test
	public void arrowsMakeNoise() throws Exception {
		GameBundle g = GameBundle.withPlayerInRoom(0);
		g.world.connectRooms(g.world.getRoom(0), Direction.E, g.world.getRoom(1));
		g.world.connectRooms(g.world.getRoom(0), Direction.W, g.world.getRoom(2));
		g.world.addArrows(1);
		g.doCommand("shoot east");
		assertTrue(g.display.lineWasDisplayed("The arrow flies away in silence."));
	}

	@Test
	public void gameEndsWhenPlayerShootsWall() throws Exception {
		GameBundle g = GameBundle.withPlayerInRoom(0);
		g.world.addArrows(1);
		g.doCommand("shoot east");
		assertTrue(g.display.lineWasDisplayed("The arrow bounced off the wall and killed you."));
		assertTrue(g.hasTerminated());
	}

	@Test
	public void gameEndsWhenPlayerKillsWumpus() {
		GameBundle g = GameBundle.withPlayerInRoom(0);
		g.world.connectRooms(g.world.whereIsPlayer(), Direction.N, g.world.getRoom(1));
		g.world.getRoom(1).setContents(RoomObject.wumpus);
		g.world.addArrows(1);
		g.doCommand("shoot north");
		assertTrue(g.display.lineWasDisplayed("You have killed the Wumpus."));
		assertTrue(g.hasTerminated());
	}

	@Test
	public void gameCanBeReset() {
		GameBundle g = GameBundle.withPlayerInRoom(0);
		g.world.connectRooms(g.world.whereIsPlayer(), Direction.N, g.world.getRoom(1));
		g.world.getRoom(1).setContents(RoomObject.wumpus);
		g.world.addArrows(1);
		g.doCommand("shoot north");
		assertTrue(g.hasTerminated());

		g.reset();
		assertFalse(g.hasTerminated());
		assertThat(g.world.whereIsPlayer(), is(nullValue()));
		assertThat(g.world.whereIsWumpus(), is(nullValue()));
	}

	/**
	 * Create a cross map with the wumpus in the center and the player two
	 * rooms away.  After resting, the wumpus should be in one of the
	 * adjacent rooms.  Run this a few times to be semi, sorta, sure or call
	 * {@link #waitForWumpusToMove(GameBundle)} to be really sure.
	 * <pre>
	 *      [4]
	 *   [1][2][3]  <-- wumpus in room 2
	 *      [5]
	 *      [0] <-- player here
	 * </pre>
	 */
	private Room createWumpusMap(GameBundle g) {
		Room playerRoom = g.world.whereIsPlayer();
		Room wumpusRoom = g.world.getRoom(2);
		g.world.connectRooms(playerRoom, Direction.N, g.world.getRoom(5));
		g.world.connectRooms(wumpusRoom, Direction.W, g.world.getRoom(1));
		g.world.connectRooms(wumpusRoom, Direction.E, g.world.getRoom(3));
		g.world.connectRooms(wumpusRoom, Direction.N, g.world.getRoom(5));
		g.world.connectRooms(wumpusRoom, Direction.S, g.world.getRoom(5));
		return wumpusRoom;
	}

	/**
	 * The wumpus is free to rest from time to time.  Call this method to keep
	 * ticking until it stops being so lazy.
	 */
	private void waitForWumpusToMove(GameBundle g) {
		do {
			g.doCommand("rest");
		} while (g.wumpusSlept);
	}
}


/**
 * I am a {@link Game} instance with a bunch of useful defaults.
 * <p>
 * I bundle together the following instances and make them publicly available as members:
 * <ul>
 *  <li>a new {@link GameWorld}
 *  <li>a {@link ProgrammableGenerator} for the input that you specify
 *  <li>a {@link RecordingGameDisplay} that captures any output
 * </ul>
 * I also make sure that the player is on the map before the game is started.
 */
class GameBundle extends Game {
	public final GameWorld world;
	public final ProgrammableGenerator generator;
	public final RecordingGameDisplay display;
	public boolean wumpusSlept;

	static public GameBundle forInput(String... input) {
		GameBundle bundle = new GameBundle(new GameWorld(), new ProgrammableGenerator(input), new RecordingGameDisplay());
		bundle.world.setPlayerLocation(bundle.world.getRoom(0));
		return bundle;
	}

	static public GameBundle withPlayerInRoom(int roomNumber) {
		GameBundle bundle = new GameBundle(new GameWorld(), new ProgrammableGenerator(), new RecordingGameDisplay());
		bundle.world.setPlayerLocation(bundle.world.getRoom(roomNumber));
		return bundle;
	}

	static public GameBundle emptyMap() {
		return new GameBundle(new GameWorld(), new ProgrammableGenerator(), new RecordingGameDisplay());
	}

	public boolean doCommand(String cmd) {
		generator.addCommand(cmd);
		return this.tick();
	}

	private GameBundle(GameWorld world, ProgrammableGenerator generator, RecordingGameDisplay display) {
		super(world, display, generator);
		this.world = world;
		this.generator = generator;
		this.display = display;
		this.wumpusSlept = false;
	}

	@Override
	protected void tickWumpus() {
		Room initialLocation = world.whereIsWumpus();
		super.tickWumpus();
		Room finalLocation = world.whereIsWumpus();
		wumpusSlept = !isWumpusFrozen() && initialLocation != null
				&& initialLocation == finalLocation;
	}

}
