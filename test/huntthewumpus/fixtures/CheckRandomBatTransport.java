package huntthewumpus.fixtures;

import huntthewumpus.GameWorld;


public class CheckRandomBatTransport {
	private final int[] playerLocations = new int[6];
	private int cavernQuery;

	public CheckRandomBatTransport() {
		GameWorld world = GameDriver.getWorld();
		world.addBatsInCavern(world.getRoom(2));
		for (int i=0; i<1000; ++i) {
			world.setPlayerLocation(world.getRoom(1));
			GameDriver.executeCommand("E");
			playerLocations[world.whereIsPlayer().getRoomNumber()] += 1;
		}
	}

	public void setCavern(int query) {
		cavernQuery = query;
	}

	public int count() {
		return playerLocations[cavernQuery];
	}
}
