package huntthewumpus.fixtures;

import huntthewumpus.GameWorld;


public class CheckRandomBatTransport extends LocationCountingFixture {
	public CheckRandomBatTransport() {
		super();
		GameWorld world = GameDriver.getWorld();
		world.addBatsInCavern(world.getRoom(2));
		for (int i=0; i<1000; ++i) {
			world.setPlayerLocation(world.getRoom(1));
			GameDriver.executeCommand("E");
			markRoom(world.whereIsPlayer());
		}
	}
}
