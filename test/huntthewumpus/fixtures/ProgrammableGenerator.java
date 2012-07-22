package huntthewumpus.fixtures;

import huntthewumpus.Command;
import huntthewumpus.CommandGenerator;
import huntthewumpus.TranslatingCommandGenerator;
import huntthewumpus.UnknownCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProgrammableGenerator
	extends TranslatingCommandGenerator
	implements CommandGenerator
{
	private int commandIndex;
	private final List<String> commandStream;

	public ProgrammableGenerator(String... commands) {
		commandIndex = 0;
		commandStream = new ArrayList<String>(Arrays.asList(commands));
	}

	public void addCommand(String cmd) {
		commandStream.add(cmd);
	}

	public int getCommandIndex() {
		return commandIndex;
	}

	@Override
	public String getNextCommandString() {
		String next = commandStream.get(commandIndex);
		System.out.println("INPUT>>> " + next);
		commandIndex += 1;
		return next;
	}

	@Override
	public Command getNextCommand() throws UnknownCommand {
		Command translated = super.getNextCommand();
		if (translated == null) {
			throw new RuntimeException(String.format("translation failure for %s", commandStream.get(commandIndex - 1)));
		}
		System.out.println("XLATE>>> " + translated);
		return translated;
	}

}
