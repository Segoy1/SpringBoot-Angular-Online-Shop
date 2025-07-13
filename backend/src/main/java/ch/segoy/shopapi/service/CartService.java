package ch.segoy.shopapi.service;

import ch.segoy.shopapi.entity.Cart;
import ch.segoy.shopapi.entity.ProductInOrder;
import ch.segoy.shopapi.entity.User;

import java.util.Collection;

/**
 * Created By Zhu Lin on 3/10/2018.
 */
public interface CartService {
    Cart getCart(User user);

    void mergeLocalCart(Collection<ProductInOrder> productInOrders, User user);

    void delete(String itemId, User user);

    void checkout(User user);
}
