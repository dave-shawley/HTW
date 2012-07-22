package huntthewumpus;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;


public class TestHelper {

	public static <E extends Enum<E>> void testEnumerationMethods(Class<E> enumClass) {
		try {
			Method valueOf = enumClass.getDeclaredMethod("valueOf", String.class);
			for (E value: enumClass.getEnumConstants()) {
				assertThat(value.toString(), is(not(nullValue())));
				assertThat(value.toString(), is(not(equalTo(""))));
				assertThat(valueOf.invoke(enumClass, value.toString()), is(equalTo((Object) value)));
			}
		} catch (Exception e) {
			throw new RuntimeException("failed to test enumeration " + enumClass, e);
		}
	}

}
