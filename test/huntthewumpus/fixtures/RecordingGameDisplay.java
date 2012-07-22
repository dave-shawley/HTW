package huntthewumpus.fixtures;

import huntthewumpus.GameDisplay;

import java.util.ArrayList;
import java.util.List;

public class RecordingGameDisplay implements GameDisplay {
	private final List<String> outputList = new ArrayList<String>();

	@Override
	public void showOutput(String message) {
		System.out.println("OUTPUT>> " + message);
		outputList.add(message);
	}

	public List<String> getDisplayedOutput() {
		return outputList;
	}

	public String popDisplayedOutput() {
		String output = null;
		if (!outputList.isEmpty()) {
			output = outputList.remove(0);
		}
		return output;
	}

	public String getOutputLine(int lineNo) {
		verifyThatOutputWasRecorded();
		return outputList.get(lineNo);
	}

	private void verifyThatOutputWasRecorded() {
		if (outputList.isEmpty()) {
			throw new RuntimeException("no output was recorded");
		}
	}
}
