package Entities;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static List<Produit> cartItems = new ArrayList<>();

    public static void addToCart(Produit produit) {
        cartItems.add(produit);
    }

    public static List<Produit> getCartItems() {
        return cartItems;
    }

    public static void clearCart() {
        cartItems.clear();
    }
}

