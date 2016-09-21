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
        unitTests.DataTests.class,
        unitTests.UserTests.class,
        unitTests.MiscTests.class,
})
public class CompleteTestSuite {
}
