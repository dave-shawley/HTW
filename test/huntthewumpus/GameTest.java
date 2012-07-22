package huntthewumpus;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
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
		g.world.addPitInCavern(pitRoom);

		g.tick();
		assertThat(g.display.getOutputLine(0), is(equalTo("You fall into a pit and die.")));
		assertThat(g.display.getOutputLine(1), is(equalTo("Game over.")));
		assertThat(g.hasTerminated(), is(true));
	}

	@Test
	public void playerHearsBats() {
		GameBundle g = GameBundle.forInput("E");
		Room playerRoom = g.world.whereIsPlayer();
		Room batRoom = g.world.getRoom(2);
		g.world.connectRooms(playerRoom, Direction.E, g.world.getRoom(1));
		g.world.connectRooms(g.world.getRoom(1), Direction.E, batRoom);
		g.world.setPlayerLocation(playerRoom);
		g.world.addBatsInCavern(batRoom);

		g.tick();
		assertThat(g.display.getOutputLine(0), is(equalTo("You hear chirping.")));
	}

	@Test
	public void playerHearsPit() {
		GameBundle g = GameBundle.forInput("E");
		Room playerRoom = g.world.getRoom(0);
		Room batRoom = g.world.getRoom(2);
		g.world.connectRooms(playerRoom, Direction.E, g.world.getRoom(1));
		g.world.connectRooms(g.world.getRoom(1), Direction.E, batRoom);
		g.world.setPlayerLocation(playerRoom);
		g.world.addPitInCavern(batRoom);

		g.tick();
		assertThat(g.display.getOutputLine(0), is(equalTo("You hear wind.")));
	}

	@Test
	public void playerWarnedOnInvalidMove() {
		GameBundle g = GameBundle.forInput("E", "W", "N", "S");
		g.tick();
		g.tick();
		g.tick();
		g.tick();
		assertThat(g.display.getOutputLine(0), is(equalTo("You can't go east from here.")));
		assertThat(g.display.getOutputLine(1), is(equalTo("You can't go west from here.")));
		assertThat(g.display.getOutputLine(2), is(equalTo("You can't go north from here.")));
		assertThat(g.display.getOutputLine(3), is(equalTo("You can't go south from here.")));
	}

	@Test
	public void playerCanRest() {
		GameBundle g = GameBundle.forInput("rest", "R");
		g.tick();
		g.tick();
	}

	@Test
	public void gameHasNotTerminatedByDefault() {
		Game g = new GameBundle();
		assertThat(g.hasTerminated(), is(false));
	}

	@Test
	public void unknownCommandsDisplayErrorMessage() {
		String randomInputValue = UUID.randomUUID().toString();
		GameBundle g = GameBundle.forInput(randomInputValue);
		g.tick();
		assertThat(g.display.getOutputLine(0),
				is(equalTo(String.format("I don't know how to %s.", randomInputValue))));
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

	static public GameBundle forInput(String... input) {
		return new GameBundle(0, new GameWorld(), new ProgrammableGenerator(input),
				new RecordingGameDisplay());
	}

	public GameBundle() {
		this(0, new GameWorld(), new ProgrammableGenerator(), new RecordingGameDisplay());
	}

	/**
	 * Extracted from primary constructor so that we can capture the instances
	 * that are fed into the {@link Game} constructor.
	 */
	private GameBundle(int playerRoomNumber, GameWorld world, ProgrammableGenerator generator,
			RecordingGameDisplay display)
	{
		super(world, display, generator);
		this.world = world;
		this.generator = generator;
		this.display = display;
		this.world.setPlayerLocation(this.world.getRoom(playerRoomNumber));
	}
}
