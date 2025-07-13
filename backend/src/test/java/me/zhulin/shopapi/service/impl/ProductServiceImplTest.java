package me.zhulin.shopapi.service.impl;

import me.zhulin.shopapi.entity.ProductInfo;
import me.zhulin.shopapi.enums.ProductStatusEnum;
import me.zhulin.shopapi.exception.MyException;
import me.zhulin.shopapi.repository.ProductInfoRepository;
import me.zhulin.shopapi.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
public class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductInfoRepository productInfoRepository;

    @Mock
    private CategoryService categoryService;

    private ProductInfo productInfo;

    @BeforeEach
    public void setUp() {
        productInfo = new ProductInfo();
        productInfo.setProductId("1");
        productInfo.setProductStock(10);
        productInfo.setProductStatus(1);
    }

    @Test
    public void increaseStockTest() {
        when(productInfoRepository.findByProductId(productInfo.getProductId())).thenReturn(productInfo);

        productService.increaseStock("1", 10);

        Mockito.verify(productInfoRepository, Mockito.times(1)).save(productInfo);
    }

    @Test
    public void increaseStockExceptionTest() {
        assertThatThrownBy(() -> productService.increaseStock("1", 10))
            .isInstanceOf(MyException.class);
    }

    @Test
    public void decreaseStockTest() {
        when(productInfoRepository.findByProductId(productInfo.getProductId())).thenReturn(productInfo);

        productService.decreaseStock("1", 9);

        Mockito.verify(productInfoRepository, Mockito.times(1)).save(productInfo);
    }

    @Test
    public void decreaseStockValueLesserEqualTest() {
        when(productInfoRepository.findByProductId(productInfo.getProductId())).thenReturn(productInfo);

        assertThatThrownBy(() -> productService.decreaseStock("1", 10))
            .isInstanceOf(MyException.class);
    }

    @Test
    public void decreaseStockExceptionTest() {
        assertThatThrownBy(() -> productService.decreaseStock("1", 10))
            .isInstanceOf(MyException.class);
    }

    @Test
    public void offSaleTest() {
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());

        when(productInfoRepository.findByProductId(productInfo.getProductId())).thenReturn(productInfo);

        productService.offSale("1");

        Mockito.verify(productInfoRepository, Mockito.times(1)).save(productInfo);
    }

    @Test
    public void offSaleStatusDownTest() {
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());

        when(productInfoRepository.findByProductId(productInfo.getProductId())).thenReturn(productInfo);

        assertThatThrownBy(() -> productService.offSale("1"))
            .isInstanceOf(MyException.class);
    }

    @Test
    public void offSaleProductNullTest() {
        when(productInfoRepository.findByProductId(productInfo.getProductId())).thenReturn(null);

        assertThatThrownBy(() -> productService.offSale("1"))
            .isInstanceOf(MyException.class);
    }

    @Test
    public void onSaleTest() {
        productInfo.setProductStatus(ProductStatusEnum.DOWN.getCode());

        when(productInfoRepository.findByProductId(productInfo.getProductId())).thenReturn(productInfo);

        productService.onSale("1");

        Mockito.verify(productInfoRepository, Mockito.times(1)).save(productInfo);
    }

    @Test
    public void onSaleStatusUpTest() {
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        when(productInfoRepository.findByProductId(productInfo.getProductId())).thenReturn(productInfo);
        assertThatThrownBy(() -> productService.onSale("1"))
            .isInstanceOf(MyException.class);
    }

    @Test
    public void onSaleProductNullTest() {
        when(productInfoRepository.findByProductId(productInfo.getProductId())).thenReturn(null);
        assertThatThrownBy(() -> productService.offSale("1"))
            .isInstanceOf(MyException.class);
    }

    @Test
    public void updateTest() {
        productService.update(productInfo);

        Mockito.verify(productInfoRepository, Mockito.times(1)).save(productInfo);
    }

    @Test
    public void updateProductStatusBiggerThenOneTest() {
        productInfo.setProductStatus(2);
        assertThatThrownBy(() -> productService.update(productInfo))
            .isInstanceOf(MyException.class);
    }

    @Test
    public void deleteTest() {
        when(productInfoRepository.findByProductId(productInfo.getProductId())).thenReturn(productInfo);

        productService.delete("1");

        Mockito.verify(productInfoRepository, Mockito.times(1)).delete(productInfo);
    }

    @Test
    public void deleteProductNullTest() {
        productService.delete("1");
    }
}
