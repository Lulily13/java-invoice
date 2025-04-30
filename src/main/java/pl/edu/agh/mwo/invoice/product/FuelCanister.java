package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public class FuelCanister extends Product {
    private static final BigDecimal EXCISE_TAX = new BigDecimal("5.56");

    public FuelCanister(String name, BigDecimal price, boolean isTaxable) {
        super(name, price, isTaxable ? new BigDecimal("0.23") : new BigDecimal("0"));
    }

    @Override
    public BigDecimal getPriceWithTax() {
        BigDecimal priceWithTax = super.getPrice().multiply(BigDecimal.ONE.add(getTaxPercent()));
        return priceWithTax.add(EXCISE_TAX);
    }
}



