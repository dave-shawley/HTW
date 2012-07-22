package huntthewumpus;

/**
 * I simply use {@link Command#fromInputString(String)} to translate a command string into a command.
 */
public abstract class TranslatingCommandGenerator implements CommandGenerator {

	@Override public Command getNextCommand() throws UnknownCommand {
		return Command.fromInputString(getNextCommandString());
	}

}
