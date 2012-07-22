package huntthewumpus;

public interface CommandGenerator {
	String getNextCommandString();
	Command getNextCommand() throws UnknownCommand;
}
