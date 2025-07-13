package ch.segoy.shopapi.service.impl;

import ch.segoy.shopapi.entity.Cart;
import ch.segoy.shopapi.entity.ProductInOrder;
import ch.segoy.shopapi.entity.User;
import ch.segoy.shopapi.repository.ProductInOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
public class ProductInOrderServiceImplTest {

  @Mock private ProductInOrderRepository productInOrderRepository;

  @InjectMocks private ProductInOrderServiceImpl productInOrderService;

  private User user;

  private ProductInOrder productInOrder;

  @BeforeEach
  public void setUp() {
    user = new User();
    Cart cart = new Cart();

    productInOrder = new ProductInOrder();
    productInOrder.setProductId("1");

    Set set = new HashSet<>();
    set.add(productInOrder);

    cart.setProducts(set);

    user.setCart(cart);
  }

  @Test
  public void updateTest() {
    productInOrderService.update("1", 10, user);

    Mockito.verify(productInOrderRepository, Mockito.times(1)).save(productInOrder);
  }

  @Test
  public void findOneTest() {
    ProductInOrder productInOrderReturn = productInOrderService.findOne("1", user);

    assertThat(productInOrderReturn.getProductId()).isEqualTo(productInOrder.getProductId());
  }
}
