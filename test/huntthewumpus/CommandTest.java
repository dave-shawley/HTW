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

	private void verifyUpperAndLowerCase(String value, Command expected) throws Exception {
		assertThat(Command.fromInputString(value), is(equalTo(expected)));
		assertThat(Command.fromInputString(value.toLowerCase()), is(equalTo(expected)));
		assertThat(Command.fromInputString(value.toUpperCase()), is(equalTo(expected)));
	}

}
