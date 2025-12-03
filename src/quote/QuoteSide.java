package quote;

import book.BookSide;
import exceptions.InvalidParameterException;
import exceptions.NullParameterException;
import price.Price;
import tradable.Tradable;
import tradable.TradableDTO;
import tradable.TradableImpl;

public class QuoteSide implements Tradable {

    private TradableImpl delegate;

    public QuoteSide(String user, String product, Price price, int originalVolume, BookSide side) throws InvalidParameterException, NullParameterException {
        delegate = new TradableImpl(user, product, price, originalVolume, side);
    }

    private void setUser(String user) throws InvalidParameterException {
        delegate.setUser(user);
    }

    private void setProduct(String product) throws InvalidParameterException {
        delegate.setProduct(product);
    }

    private void setPrice(Price price) throws NullParameterException {
        delegate.setPrice(price);
    }

    private void setOriginalVolume(int originalVolume) throws InvalidParameterException {
        delegate.setOriginalVolume(originalVolume);
    }

    private void setSide(BookSide side) throws InvalidParameterException {
        delegate.setBookSide(side);
    }

    @Override
    public String getId() {
        return delegate.getId();
    }

    @Override
    public int getRemainingVolume() {
        return delegate.getRemainingVolume();
    }

    @Override
    public void setCancelledVolume(int newVol) {
        delegate.setCancelledVolume(newVol);
    }

    @Override
    public int getCancelledVolume() {
        return delegate.getCancelledVolume();
    }

    @Override
    public void setRemainingVolume(int newVol) {
        delegate.setRemainingVolume(newVol);
    }

    @Override
    public TradableDTO makeTradableDTO() {
        return delegate.makeTradableDTO();
    }

    @Override
    public Price getPrice() {
        return delegate.getPrice();
    }

    @Override
    public void setFilledVolume(int newVol) {
        delegate.setFilledVolume(newVol);
    }

    @Override
    public int getFilledVolume() {
        return delegate.getFilledVolume();
    }

    @Override
    public BookSide getSide() {
        return delegate.getSide();
    }

    @Override
    public String getUser() {
        return delegate.getUser();
    }

    @Override
    public String getProduct() {
        return delegate.getProduct();
    }

    @Override
    public int getOriginalVolume() {
        return delegate.getOriginalVolume();
    }

    @Override
    public String toString() {
        return getUser() + " " + getSide() + " side quote for " + getProduct() + ": " + getPrice() +
                ", Orig Vol: " + getOriginalVolume() + ", Rem Vol: " + getRemainingVolume() +
                ", Fill Vol: " + getFilledVolume() + ", CXL Vol: " + getCancelledVolume() +
                ", ID: " + getId();
    }
}
