package ch.segoy.shopapi.service;

import ch.segoy.shopapi.entity.ProductInOrder;
import ch.segoy.shopapi.entity.User;

/**
 * Created By Zhu Lin on 1/3/2019.
 */
public interface ProductInOrderService {
    void update(String itemId, Integer quantity, User user);
    ProductInOrder findOne(String itemId, User user);
}
