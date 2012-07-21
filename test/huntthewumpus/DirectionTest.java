package huntthewumpus;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class DirectionTest {

	@Test
	public void directionsAreReversible() {
		assertThat(Direction.E.reverse(), is(Direction.W));
		assertThat(Direction.W.reverse(), is(Direction.E));
		assertThat(Direction.S.reverse(), is(Direction.N));
		assertThat(Direction.N.reverse(), is(Direction.S));
	}

}
