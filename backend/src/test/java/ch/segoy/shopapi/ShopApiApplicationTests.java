package ch.segoy.shopapi;

import ch.segoy.shopapi.service.impl.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        CartServiceImplTest.class,
        CategoryServiceImplTest.class,
        OrderServiceImplTest.class,
        ProductInOrderServiceImplTest.class,
        ProductServiceImplTest.class,
        UserServiceImplTest.class
})
public class ShopApiApplicationTests {
}
