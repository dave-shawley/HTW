package huntthewumpus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;


/**
 * I translate text from a {@link Reader} into a series of commands.
 */
public class ReadingCommandGenerator
	extends TranslatingCommandGenerator
	implements CommandGenerator
{
	private final BufferedReader reader;

	public ReadingCommandGenerator(Reader commandStream) {
		reader = new BufferedReader(commandStream);
	}

	@Override public String getNextCommandString() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			throw new RuntimeException("failed to get next command", e);
		}
	}
}
