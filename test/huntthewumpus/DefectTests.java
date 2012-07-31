package huntthewumpus;

import org.junit.Test;


public class DefectTests {

	@Test
	public void hearPit() throws Exception {
		//       [6]
		//       [7]
		// [1][2][3][4][5]
		//       [8]
		//       [9]
		GameWorld w = new GameWorld();
		w.connectRooms(w.getRoom(1), Direction.E, w.getRoom(2));
		w.connectRooms(w.getRoom(2), Direction.E, w.getRoom(3));
		w.connectRooms(w.getRoom(3), Direction.E, w.getRoom(4));
		w.connectRooms(w.getRoom(4), Direction.E, w.getRoom(5));
		w.connectRooms(w.getRoom(6), Direction.S, w.getRoom(7));
		w.connectRooms(w.getRoom(7), Direction.S, w.getRoom(3));
		w.connectRooms(w.getRoom(3), Direction.S, w.getRoom(8));
		w.connectRooms(w.getRoom(8), Direction.S, w.getRoom(9));

		w.getRoom(3).setContents(RoomObject.pit);

		w.setPlayerLocation(w.getRoom(5));
		w.movePlayer(Direction.W);
		w.movePlayer(Direction.E);

		w.setPlayerLocation(w.getRoom(9));
		w.movePlayer(Direction.N);
	}

}
