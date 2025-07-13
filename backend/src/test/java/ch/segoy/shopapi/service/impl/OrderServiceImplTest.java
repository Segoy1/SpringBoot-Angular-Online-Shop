package ch.segoy.shopapi.service.impl;

import ch.segoy.shopapi.entity.OrderMain;
import ch.segoy.shopapi.entity.ProductInOrder;
import ch.segoy.shopapi.entity.ProductInfo;
import ch.segoy.shopapi.enums.OrderStatusEnum;
import ch.segoy.shopapi.exception.MyException;
import ch.segoy.shopapi.repository.OrderRepository;
import ch.segoy.shopapi.repository.ProductInfoRepository;
import ch.segoy.shopapi.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
public class OrderServiceImplTest {

  @Mock private OrderRepository orderRepository;

  @Mock private ProductInfoRepository productInfoRepository;

  @Mock private ProductService productService;

  @InjectMocks private OrderServiceImpl orderService;

  private OrderMain orderMain;

  private ProductInfo productInfo;

  @BeforeEach
  public void setUp() {
    orderMain = new OrderMain();
    orderMain.setOrderId(1L);
    orderMain.setOrderStatus(OrderStatusEnum.NEW.getCode());

    ProductInOrder productInOrder = new ProductInOrder();
    productInOrder.setProductId("1");
    productInOrder.setCount(10);

    Set<ProductInOrder> set = new HashSet<>();
    set.add(productInOrder);

    orderMain.setProducts(set);

    productInfo = new ProductInfo();
    productInfo.setProductStock(10);
  }

  @Test
  public void finishSuccessTest() {
    when(orderRepository.findByOrderId(orderMain.getOrderId())).thenReturn(orderMain);

    OrderMain orderMainReturn = orderService.finish(orderMain.getOrderId());

    assertThat(orderMainReturn.getOrderId()).isEqualTo(orderMain.getOrderId());
    assertThat(orderMainReturn.getOrderStatus()).isEqualTo(OrderStatusEnum.FINISHED.getCode());
  }

  @Test
  public void finishStatusCanceledTest() {
    orderMain.setOrderStatus(OrderStatusEnum.CANCELED.getCode());

    when(orderRepository.findByOrderId(orderMain.getOrderId())).thenReturn(orderMain);

    assertThatThrownBy(
            () -> {
              orderService.finish(orderMain.getOrderId());
            })
        .isInstanceOf(MyException.class)
        .hasMessageContaining("Status is not correct");
  }

  @Test
  public void finishStatusFinishedTest() {
    orderMain.setOrderStatus(OrderStatusEnum.FINISHED.getCode());

    when(orderRepository.findByOrderId(orderMain.getOrderId())).thenReturn(orderMain);

    assertThatThrownBy(
            () -> {
              orderService.finish(orderMain.getOrderId());
            })
        .isInstanceOf(MyException.class)
        .hasMessageContaining("Status is not correct");
  }

  @Test
  public void cancelSuccessTest() {
    when(orderRepository.findByOrderId(orderMain.getOrderId())).thenReturn(orderMain);
    when(productInfoRepository.findByProductId(
            orderMain.getProducts().iterator().next().getProductId()))
        .thenReturn(productInfo);

    OrderMain orderMainReturn = orderService.cancel(orderMain.getOrderId());

    assertThat(orderMainReturn.getOrderId()).isEqualTo(orderMain.getOrderId());
    assertThat(orderMainReturn.getOrderStatus()).isEqualTo(OrderStatusEnum.CANCELED.getCode());
    assertThat(orderMainReturn.getProducts().iterator().next().getCount()).isEqualTo(10);
  }

  @Test
  public void cancelNoProduct() {
    when(orderRepository.findByOrderId(orderMain.getOrderId())).thenReturn(orderMain);
    orderMain.setProducts(new HashSet<>());

    OrderMain orderMainReturn = orderService.cancel(orderMain.getOrderId());

    assertThat(orderMainReturn.getOrderId()).isEqualTo(orderMain.getOrderId());
    assertThat(orderMainReturn.getOrderStatus()).isEqualTo(OrderStatusEnum.CANCELED.getCode());
  }

  @Test
  public void cancelStatusCanceledTest() {
    orderMain.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
    when(orderRepository.findByOrderId(orderMain.getOrderId())).thenReturn(orderMain);
    assertThatThrownBy(() -> orderService.cancel(orderMain.getOrderId()))
        .isInstanceOf(MyException.class);
  }

  @Test
  public void cancelStatusFinishTest() {
    orderMain.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
    when(orderRepository.findByOrderId(orderMain.getOrderId())).thenReturn(orderMain);
    assertThatThrownBy(() -> orderService.cancel(orderMain.getOrderId()))
        .isInstanceOf(MyException.class);
  }
}
