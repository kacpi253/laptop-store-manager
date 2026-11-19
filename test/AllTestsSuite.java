import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        LaptopTest.class,
        BranchTest.class,
        StoreManagerTest.class,
        StoreFacadeTest.class,
        GenericTableModelTest.class
})
public class AllTestsSuite {
}