package unitTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs all unit tests.
 *
 * @author Aden
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({
    	unitTests.CharacterTests.class,
        unitTests.DataTests.class,
        unitTests.GraphicsTests.class,
        unitTests.MiscTests.class,
        unitTests.UserTests.class,
})
public class CompleteTestSuite {
}
