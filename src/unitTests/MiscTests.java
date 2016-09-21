package unitTests;

import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

/**
 * A set of tests for things that do not come under the other categories.
 *
 * @author Aden
 */

public class MiscTests {

	/**
	 * Tests to make sure classes with private constructors that throw AssertionErrors
	 * cannot actually be initialised.
	 */
	
	@Test
	public void testNonInitialisable() {
		try {
			Class<?> testClass = Class.forName("util.Logging");
			
			Constructor<?> cons = testClass.getDeclaredConstructor();
			cons.setAccessible(true);
			
			try {
				cons.newInstance();
			}
			
			catch (InvocationTargetException e){
				if(e.getCause() instanceof AssertionError) {
					return;
				}
				
				fail();
			} 
			
			catch (InstantiationException e) {
				e.printStackTrace();
			} 
			
			catch (IllegalAccessException e) {
				e.printStackTrace();
			} 
			
			catch (IllegalArgumentException e) {
				e.printStackTrace();
			} 
			
		} 
		
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail();
		} 
		
		catch (NoSuchMethodException e) {
			e.printStackTrace();
		} 
		
		catch (SecurityException e) {
			e.printStackTrace();
		}
		
		fail();
	}
}
