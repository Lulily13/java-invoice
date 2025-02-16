package pl.edu.agh.mwo.invoice;

import pl.edu.agh.mwo.invoice.product.Product;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Invoice {
    private Map<Product, Integer> productsWithQuantities = new HashMap<>();

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Products cannot be null");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity of products must be greater than zero");
        }

        productsWithQuantities.put(product, productsWithQuantities.getOrDefault(product, 0) + quantity);
    }

    public BigDecimal getNetPrice() {
        BigDecimal netPrice = BigDecimal.ZERO;
        for (Map.Entry<Product, Integer> entry : productsWithQuantities.entrySet()) {
            netPrice = netPrice.add(entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
        }
        return netPrice;
    }

    public BigDecimal getTax() {
        BigDecimal totalTax = BigDecimal.ZERO;
        for (Map.Entry<Product, Integer> entry : productsWithQuantities.entrySet()) {
            BigDecimal productTax = entry.getKey().getPrice()
                    .multiply(entry.getKey().getTaxPercent())
                    .multiply(BigDecimal.valueOf(entry.getValue()));
            totalTax = totalTax.add(productTax);
        }
        return totalTax;
    }

    public BigDecimal getGrossPrice() {
        return getNetPrice().add(getTax());
    }
}