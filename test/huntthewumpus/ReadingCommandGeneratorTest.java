package huntthewumpus;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.UUID;

import org.junit.Test;


public class ReadingCommandGeneratorTest {

	@Test
	public void commandsAreParsedFromInput() {
		StringBuilder commandSequence = new StringBuilder();
		commandSequence.append("Go east").append("\n")
		               .append("Go west").append("\n")
		               .append("rest").append("\n");

		ReadingCommandGenerator generator = new ReadingCommandGenerator(new StringReader(commandSequence.toString()));
		assertThat(generator.getNextCommandString(), is(equalTo("Go east")));
		assertThat(generator.getNextCommandString(), is(equalTo("Go west")));
		assertThat(generator.getNextCommandString(), is(equalTo("rest")));
	}

	@Test
	public void commandGeneratorParsesMovementCommands() throws Exception {
		StringBuilder commandSequence = new StringBuilder();
		commandSequence.append("Go east").append("\n")
		               .append("W").append("\n")
		               .append("south").append("\n")
		               .append("NORTH").append("\n");

		ReadingCommandGenerator generator = new ReadingCommandGenerator(new StringReader(commandSequence.toString()));
		assertThat(generator.getNextCommand(), is(equalTo(Command.MOVE_EAST)));
		assertThat(generator.getNextCommand(), is(equalTo(Command.MOVE_WEST)));
		assertThat(generator.getNextCommand(), is(equalTo(Command.MOVE_SOUTH)));
		assertThat(generator.getNextCommand(), is(equalTo(Command.MOVE_NORTH)));
	}

	@Test
	public void restCommandIsParsed() throws Exception {
		ReadingCommandGenerator generator = new ReadingCommandGenerator(new StringReader("R\nrest"));
		assertThat(generator.getNextCommand(), is(equalTo(Command.REST)));
	}

	@Test(expected=RuntimeException.class)
	public void readerExceptionsAreTranslated() {
		final IOException cause = new IOException("induced exception");
		Reader failingReader = new Reader() {
			@Override public void close() throws IOException {}
			@Override public int read(char[] arg0, int arg1, int arg2) throws IOException {
				throw cause;
			}
		};

		ReadingCommandGenerator generator = new ReadingCommandGenerator(failingReader);
		try {
			generator.getNextCommandString();
		} catch (RuntimeException exc) {
			assertThat(exc.getCause(), is(sameInstance((Throwable) cause)));
			throw exc;
		}
	}

	@Test
	public void unknownCommandResultsInCorrectException() throws Exception {
		boolean exceptionCaught = false;
		String randomInput = UUID.randomUUID().toString();
		ReadingCommandGenerator generator = new ReadingCommandGenerator(new StringReader(randomInput));
		try {
			generator.getNextCommand();
		} catch (UnknownCommand exc) {
			assertThat(exc.getInputString(), is(equalTo(randomInput)));
			exceptionCaught = true;
		}
		if (!exceptionCaught) {
			fail("expected exception not generated");
		}
	}

}
