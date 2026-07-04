package Sathish292004.repository;

import Sathish292004.model.Cart;
import Sathish292004.model.CartItem;
import Sathish292004.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepo extends JpaRepository<CartItem, Integer> {

    List<CartItem> findByCart(Cart cart);

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);

}