package tradable;

import book.BookSide;
import exceptions.InvalidParameterException;
import exceptions.NullParameterException;
import price.Price;

public class TradableImpl implements Tradable {

    private String user;
    private String product;
    private Price price;
    private BookSide side;
    private int originalVolume;

    private int remainingVolume;
    private int cancelledVolume = 0;
    private int filledVolume = 0;
    private String id;

    public TradableImpl(String user, String product, Price price, int originalVolume, BookSide side) throws InvalidParameterException, NullParameterException {
        setUser(user);
        setProduct(product);
        setPrice(price);
        setBookSide(side);
        setOriginalVolume(originalVolume);
        setRemainingVolume(originalVolume);
        setId();
    }

    public void setUser(String user) throws InvalidParameterException {
        if(user.length() != 3) {
            throw new InvalidParameterException("User is " + user.length() + " characters long. Expected length: 3");
        }

        for (int i = 0; i < user.length(); i++) {
            if (!Character.isLetter(user.charAt(i)) || Character.isWhitespace(user.charAt(i))) {
                throw new InvalidParameterException("Found invalid character '" + user.charAt(i) + "' in user string " + user);
            }
        }

        this.user = user;
    }

    public void setProduct(String product) throws InvalidParameterException {
        if(product.isEmpty() || product.length() > 5) {
            throw new InvalidParameterException("Product is " + product.length() + " characters long. Expected length: 5");
        }

        for (int i = 0; i < product.length(); i++) {
            if (!Character.isLetter(product.charAt(i)) || Character.isWhitespace(product.charAt(i))) {
                if(product.charAt(i) != '.') {
                    throw new InvalidParameterException("Found invalid character '" + product.charAt(i) + "' in product string " + product);
                }
            }
        }

        this.product = product;
    }

    public void setPrice(Price price) throws NullParameterException {
        if(price == null) {
            throw new NullParameterException("Price is null");
        }

        this.price = price;
    }

    public void setBookSide(BookSide side) throws InvalidParameterException {
        if(side == null) {
            throw new InvalidParameterException("Side is null");
        }

        this.side = side;
    }

    public void setOriginalVolume(int originalVolume) throws InvalidParameterException {
        if (originalVolume < 0 || originalVolume > 10000) {
            throw new InvalidParameterException("originalVolume " + originalVolume + " is less than 0 or greater than 10,000");
        }
        this.originalVolume = originalVolume;
    }

    private void setId() {
        this.id = getUser() + getProduct() + getPrice().toString() + System.nanoTime();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getRemainingVolume() {
        return this.remainingVolume;
    }

    @Override
    public void setCancelledVolume(int newVol) {
        this.cancelledVolume = newVol;
    }

    @Override
    public int getCancelledVolume() {
        return this.cancelledVolume;
    }

    @Override
    public void setRemainingVolume(int newVol) {
        this.remainingVolume = newVol;
    }

    @Override
    public TradableDTO makeTradableDTO() {
        return new TradableDTO(this);
    }

    @Override
    public Price getPrice() {
        return price;
    }

    @Override
    public void setFilledVolume(int newVol) {
        this.filledVolume = newVol;
    }

    @Override
    public int getFilledVolume() {
        return filledVolume;
    }

    @Override
    public BookSide getSide() {
        return side;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getProduct() {
        return product;
    }

    @Override
    public int getOriginalVolume() {
        return originalVolume;
    }
}
