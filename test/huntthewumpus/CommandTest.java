package huntthewumpus;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class CommandTest {

	@Test
	public void validateMovementCommands() throws Exception {
		for (Direction d: Direction.values()) {
			Command expected = null;
			switch (d) {
			case E:
				expected = Command.MOVE_EAST;
				break;
			case W:
				expected = Command.MOVE_WEST;
				break;
			case S:
				expected = Command.MOVE_SOUTH;
				break;
			case N:
				expected = Command.MOVE_NORTH;
				break;
			}
			verifyUpperAndLowerCase(d.toString(), expected);
			verifyUpperAndLowerCase(d.longName(), expected);
			verifyUpperAndLowerCase(String.format("Go %s", d.longName()), expected);
		}
	}

	@Test
	public void validateRestCommands() throws Exception {
		verifyUpperAndLowerCase("Rest", Command.REST);
		verifyUpperAndLowerCase("R", Command.REST);
	}

	@Test(expected=UnknownCommand.class)
	public void unknownCommandResultsInCorrectException() throws Exception {
		Command.fromInputString("");
	}

	@Test
	public void coverageTests() {
		TestHelper.testEnumerationMethods(Command.class);
	}

	@Test
	public void validateShootCommands() throws Exception {
		final Direction[] directions = { Direction.E, Direction.W, Direction.N, Direction.S };
		final Command[] commands = { Command.SHOOT_EAST, Command.SHOOT_WEST, Command.SHOOT_NORTH, Command.SHOOT_SOUTH };

		for (int index=0; index<directions.length; ++index) {
			verifyUpperAndLowerCase(
					"shoot " + directions[index].longName(),
					commands[index]);
			verifyUpperAndLowerCase(
					"shoot " + directions[index].name(),
					commands[index]);
			verifyUpperAndLowerCase(
					"s " + directions[index].name(),
					commands[index]);
			verifyUpperAndLowerCase(
					"s" + directions[index].name(),
					commands[index]);
		}
	}

	@Test
	public void moveCommandsHaveDirection() {
		assertThat(Command.MOVE_EAST.direction(), is(Direction.E));
		assertThat(Command.MOVE_WEST.direction(), is(Direction.W));
		assertThat(Command.MOVE_NORTH.direction(), is(Direction.N));
		assertThat(Command.MOVE_SOUTH.direction(), is(Direction.S));
	}

	@Test
	public void shootCommandsHaveDirection() {
		assertThat(Command.SHOOT_EAST.direction(), is(Direction.E));
		assertThat(Command.SHOOT_WEST.direction(), is(Direction.W));
		assertThat(Command.SHOOT_NORTH.direction(), is(Direction.N));
		assertThat(Command.SHOOT_SOUTH.direction(), is(Direction.S));
	}

	@Test(expected=UnknownCommand.class)
	public void cannotShootInFakeDirection() throws Exception {
		Command.fromInputString("shoot foo");
	}

	private void verifyUpperAndLowerCase(String value, Command expected) throws Exception {
		assertThat(Command.fromInputString(value), is(equalTo(expected)));
		assertThat(Command.fromInputString(value.toLowerCase()), is(equalTo(expected)));
		assertThat(Command.fromInputString(value.toUpperCase()), is(equalTo(expected)));
	}

}
