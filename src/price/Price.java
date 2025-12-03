package price;

import exceptions.InvalidPriceException;

import java.util.Objects;

public class Price implements Comparable <Price> {

    private int centsValue;

    public Price(int centsValue) {
        setValue(centsValue);
    }

    private void setValue(int cents) {
        this.centsValue = cents;
    }

    public boolean isNegative() {
        return centsValue < 0;
    }

    public Price add(Price p) throws InvalidPriceException {
        if(p == null) {
            throw new InvalidPriceException("Price parameter is null");
        } else return PriceFactory.makePrice(this.centsValue + p.centsValue);
    }

    public Price subtract(Price p) throws InvalidPriceException {
        if(p == null) {
            throw new InvalidPriceException("Price parameter is null");
        } else return PriceFactory.makePrice(this.centsValue - p.centsValue);
    }

    public Price multiply(int n) {
        return PriceFactory.makePrice(this.centsValue * n);
    }

    public boolean greaterOrEqual(Price p) throws InvalidPriceException {
        if (p == null) {
            throw new InvalidPriceException("Price parameter is null");
        } else return this.centsValue >= p.centsValue;
    }

    public boolean lessOrEqual(Price p) throws InvalidPriceException {
        if (p == null) {
            throw new InvalidPriceException("Price parameter is null");
        } else return this.centsValue <= p.centsValue;
    }

    public boolean greaterThan(Price p) throws InvalidPriceException {
        if (p == null) {
            throw new InvalidPriceException("Price parameter is null");
        } else return this.centsValue > p.centsValue;
    }

    public boolean lessThan(Price p) throws InvalidPriceException {
        if (p == null) {
            throw new InvalidPriceException("Price parameter is null");
        } else return this.centsValue < p.centsValue;
    }

    @Override
    public int compareTo(Price o) {
        if (o == null) return -1;
        return centsValue - o.centsValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return centsValue == price.centsValue;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(centsValue);
    }

    @Override
    public String toString() {
        return "$" + String.format("%,.2f", (double) centsValue / 100);
    }
}
