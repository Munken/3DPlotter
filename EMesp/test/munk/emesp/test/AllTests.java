package munk.emesp.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ OperatorNodeTest.class, ValueNodeTest.class, IsConstantVisitorTest.class })
public class AllTests {

}
