package huntthewumpus;

import static org.hamcrest.CoreMatchers.equalTo;
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

	@Test
	public void directionsHaveLongNames() {
		assertThat(Direction.E.longName(), is(equalTo("east")));
		assertThat(Direction.W.longName(), is(equalTo("west")));
		assertThat(Direction.S.longName(), is(equalTo("south")));
		assertThat(Direction.N.longName(), is(equalTo("north")));
	}

	@Test
	public void validateDirectionStringMatching() {
		for (Direction dir: Direction.values()) {
			assertThat(Direction.matchString(dir.name().toLowerCase()), is(dir));
			assertThat(Direction.matchString(dir.longName().toLowerCase()), is(dir));
			assertThat(Direction.matchString(dir.name().toUpperCase()), is(dir));
			assertThat(Direction.matchString(dir.longName().toUpperCase()), is(dir));
		}
	}

	@Test
	public void coverageTests() {
		TestHelper.testEnumerationMethods(Direction.class);
	}
}
