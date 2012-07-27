package huntthewumpus.fixtures;

import huntthewumpus.GameWorld;


public class CheckRandomWumpusMovement extends LocationCountingFixture {
	public CheckRandomWumpusMovement() {
		super();
		GameWorld world = GameDriver.getWorld();
		for (int i=0; i<1000; ++i) {
			world.setWumpusLocation(world.getRoom(2));
			GameDriver.executeCommand("rest");
			markRoom(world.whereIsWumpus());
		}
	}
}
