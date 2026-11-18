```java
package mattresssalesapp;

/**
 * Implementation of IProductCalculations.
 * VAT = 15 %, Commission = 15 %, Total = price + VAT + commission.
 */
public class ProductCalculations implements IProductCalculations {

    private static final double VAT_RATE = 0.15;
    private static final double COMMISSION_RATE = 0.15;

    private final double price;

    public ProductCalculations(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }

    @Override
    public double getVat() {
        return price * VAT_RATE;
    }

    @Override
    public double getCommission() {
        return price * COMMISSION_RATE;
    }

    @Override
    public double getTotal() {
        return price + getVat() + getCommission();
    }

    public double getPrice() {
        return price;
    }
}
```
