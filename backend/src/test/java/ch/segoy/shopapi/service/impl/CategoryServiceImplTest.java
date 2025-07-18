package ch.segoy.shopapi.service.impl;

import ch.segoy.shopapi.entity.ProductCategory;
import ch.segoy.shopapi.exception.MyException;
import ch.segoy.shopapi.repository.ProductCategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
public class CategoryServiceImplTest {

  @InjectMocks private CategoryServiceImpl categoryService;

  @Mock private ProductCategoryRepository productCategoryRepository;

  @Test
  public void findByCategoryTypeTest() {
    ProductCategory productCategory = new ProductCategory();
    productCategory.setCategoryId(1);

    Mockito.when(productCategoryRepository.findByCategoryType(productCategory.getCategoryId()))
        .thenReturn(productCategory);

    categoryService.findByCategoryType(productCategory.getCategoryId());

    Mockito.verify(productCategoryRepository, Mockito.times(1))
        .findByCategoryType(productCategory.getCategoryId());
  }

  @Test
  public void findByCategoryTypeExceptionTest() {
    ProductCategory productCategory = new ProductCategory();
    productCategory.setCategoryId(1);

    Mockito.when(productCategoryRepository.findByCategoryType(productCategory.getCategoryId()))
        .thenReturn(null);

    assertThatThrownBy(() -> {
      categoryService.findByCategoryType(productCategory.getCategoryId());
    }).isInstanceOf(MyException.class)
      .hasMessageContaining("Category does not exit!");
  }
}
