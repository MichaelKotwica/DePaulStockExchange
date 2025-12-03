package quote;

import book.BookSide;
import exceptions.InvalidParameterException;
import exceptions.NullParameterException;
import price.Price;

public class Quote {

    private String user;
    private String product;

    private QuoteSide buySide;
    private QuoteSide sellSide;

    public Quote(String symbol, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume, String userName) throws InvalidParameterException, NullParameterException {
        setUser(userName);
        setProduct(symbol);

        buySide = new QuoteSide(userName, symbol, buyPrice, buyVolume, BookSide.BUY);
        sellSide = new QuoteSide(userName, symbol, sellPrice, sellVolume, BookSide.SELL);
    }


    private void setUser(String user) throws InvalidParameterException {
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

    private void setProduct(String product) throws InvalidParameterException {
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

    public QuoteSide getQuoteSide(BookSide sideIn) {
        if (sideIn == BookSide.BUY) {
            return buySide;
        } else if (sideIn == BookSide.SELL) {
            return sellSide;
        }
        return null;
    }

    public String getSymbol() {
        return product;
    }

    public String getUser() {
        return user;
    }
}
