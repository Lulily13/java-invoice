package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.FuelCanister;
import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    private static int lastUsedNumber = 0;
    private int number;

    private Map<Product, Integer> products = new LinkedHashMap<>();

    public Invoice() {
        this.number = ++lastUsedNumber;
    }

    public int getNumber() {
        return number;
    }

    public void addProduct(Product product) {
        addProduct(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException();
        }
        if (products.containsKey(product)) {
            int existingQuantity = products.get(product);
            products.put(product, existingQuantity + quantity);
        } else {
            products.put(product, quantity);
        }
    }


    public BigDecimal getNetTotal() {
        BigDecimal totalNet = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalNet = totalNet.add(product.getPrice().multiply(quantity));
        }
        return totalNet;
    }

    public BigDecimal getTaxTotal() {
        BigDecimal totalTax = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            if (product instanceof FuelCanister) {
                continue;
            }
            totalTax = totalTax.add(product.getPriceWithTax().subtract(product.getPrice()));
        }
        return totalTax;
    }

    public BigDecimal getGrossTotal() {
        BigDecimal totalGross = BigDecimal.ZERO;
        for (Product product : products.keySet()) {
            BigDecimal quantity = new BigDecimal(products.get(product));
            totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity));
        }
        return totalGross;
    }

    public String printInvoice() {
        StringBuilder output = new StringBuilder();
        output.append("Faktura nr ").append(this.number).append("\n");

        for (Product product : products.keySet()) {
            int quantity = products.get(product);
            String name = product.getName();
            BigDecimal price = product.getPrice().setScale(2, RoundingMode.HALF_UP);
            output.append(String.format("%s, %d szt., %s PLN\n", name, quantity, price.toPlainString()));
        }

        output.append("Liczba pozycji: ").append(products.size());

        return output.toString().trim();
    }


}