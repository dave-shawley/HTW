package huntthewumpus;

public class UnknownCommand extends Exception {
	private static final long serialVersionUID = 1L;
	private final String commandString;

	public UnknownCommand(String input) {
		super(String.format("unknown command '%s'", input));
		this.commandString = input;
	}

	public String getInputString() {
		return commandString;
	}

}
