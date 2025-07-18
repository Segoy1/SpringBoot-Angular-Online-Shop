package ch.segoy.shopapi.service.impl;

import ch.segoy.shopapi.entity.Cart;
import ch.segoy.shopapi.entity.ProductInOrder;
import ch.segoy.shopapi.entity.User;
import ch.segoy.shopapi.exception.MyException;
import ch.segoy.shopapi.repository.CartRepository;
import ch.segoy.shopapi.repository.OrderRepository;
import ch.segoy.shopapi.repository.ProductInOrderRepository;
import ch.segoy.shopapi.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class CartServiceImplTest {

  @InjectMocks private CartServiceImpl cartService;

  @Mock private ProductService productService;

  @Mock private ProductInOrderRepository productInOrderRepository;

  @Mock private CartRepository cartRepository;

  @Mock private OrderRepository orderRepository;

  private User user;

  private ProductInOrder productInOrder;

  private Set<ProductInOrder> set;

  private Cart cart;

  @BeforeEach
  public void setUp() {
    user = new User();
    cart = new Cart();

    user.setEmail("email@email.com");
    user.setName("Name");
    user.setPhone("Phone Test");
    user.setAddress("Address Test");

    productInOrder = new ProductInOrder();
    productInOrder.setProductId("1");
    productInOrder.setCount(10);
    productInOrder.setProductPrice(BigDecimal.valueOf(1));

    set = new HashSet<>();
    set.add(productInOrder);

    cart.setProducts(set);

    user.setCart(cart);
  }

  @Test
  public void mergeLocalCartTest() {
    cartService.mergeLocalCart(set, user);

    Mockito.verify(cartRepository, Mockito.times(1)).save(cart);
    Mockito.verify(productInOrderRepository, Mockito.times(1)).save(productInOrder);
  }

  @Test
  public void mergeLocalCartTwoProductTest() {
    ProductInOrder productInOrder2 = new ProductInOrder();
    productInOrder2.setProductId("2");
    productInOrder2.setCount(10);

    user.getCart().getProducts().add(productInOrder2);

    cartService.mergeLocalCart(set, user);

    Mockito.verify(cartRepository, Mockito.times(1)).save(cart);
    Mockito.verify(productInOrderRepository, Mockito.times(1)).save(productInOrder);
    Mockito.verify(productInOrderRepository, Mockito.times(1)).save(productInOrder2);
  }

  @Test
  public void mergeLocalCartNoProductTest() {
    user.getCart().setProducts(new HashSet<>());

    cartService.mergeLocalCart(set, user);

    Mockito.verify(cartRepository, Mockito.times(1)).save(cart);
    Mockito.verify(productInOrderRepository, Mockito.times(1)).save(productInOrder);
  }

  @Test
  public void deleteTest() {
    cartService.delete("1", user);

    Mockito.verify(productInOrderRepository, Mockito.times(1)).deleteById(productInOrder.getId());
  }

  @Test
  public void deleteNoProductTest() {
    assertThatThrownBy(
            () -> {
              cartService.delete("", user);
            })
        .isInstanceOf(MyException.class)
        .hasMessageContaining("Status is not correct");
  }

  @Test
  public void deleteNoUserTest() {

    assertThatThrownBy(
            () -> {
              cartService.delete("1", null);
            })
        .isInstanceOf(MyException.class)
        .hasMessageContaining("Status is not correct");
  }

  @Test
  public void checkoutTest() {
    cartService.checkout(user);

    Mockito.verify(productInOrderRepository, Mockito.times(1)).save(productInOrder);
  }
}
