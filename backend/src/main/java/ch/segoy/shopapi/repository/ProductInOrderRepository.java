package ch.segoy.shopapi.repository;

import ch.segoy.shopapi.entity.ProductInOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created By Zhu Lin on 3/14/2018.
 */
public interface ProductInOrderRepository extends JpaRepository<ProductInOrder, Long> {

}
