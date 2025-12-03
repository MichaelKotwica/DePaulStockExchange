package book;

import exceptions.InvalidParameterException;
import exceptions.InvalidPriceException;
import exceptions.NullParameterException;
import marketobserver.CurrentMarketTracker;
import price.Price;
import quote.Quote;
import tradable.Tradable;
import tradable.TradableDTO;

import static book.BookSide.BUY;
import static book.BookSide.SELL;

public class ProductBook {

    private String product;
    ProductBookSide buySide;
    ProductBookSide sellSide;

    public ProductBook(String product) throws InvalidParameterException, NullParameterException {
        setProduct(product);
        setBuySide(new ProductBookSide(BUY));
        setSellSide(new ProductBookSide(SELL));
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

    private void setBuySide(ProductBookSide buySide) {
        this.buySide = buySide;
    }

    private void setSellSide(ProductBookSide sellSide) {
        this.sellSide = sellSide;
    }

    public TradableDTO add(Tradable t) throws NullParameterException, InvalidPriceException {
        if(t == null) {
            throw new NullParameterException("tradable passed in is null.");
        }

        TradableDTO tradable = null;

        if(t.getSide() == BUY) {
            tradable = buySide.add(t);
        }

        if(t.getSide() == SELL) {
            tradable = sellSide.add(t);
        }

        tryTrade();
        updateMarket();

        return tradable;
    }

    public TradableDTO[] add(Quote qte) throws NullParameterException, InvalidPriceException {
        if(qte == null) {
            throw new NullParameterException("Quote passed in is null.");
        }

        TradableDTO[] dtos = new TradableDTO[2];

        removeQuotesForUser(qte.getUser());

        dtos[0] = buySide.add(qte.getQuoteSide(BUY));
        dtos[1] = sellSide.add(qte.getQuoteSide(SELL));

        tryTrade();

        return dtos;
    }

    public TradableDTO cancel(BookSide side, String orderId) throws NullParameterException, InvalidPriceException {

        if(orderId == null) {
            throw new NullParameterException("orderId is null");
        }

        TradableDTO canceledDTO = null;

        if(side == BUY) {
            canceledDTO = buySide.cancel(orderId);
        }

        if(side == SELL) {
            canceledDTO = sellSide.cancel(orderId);
        }
        updateMarket();
        return canceledDTO;
    }

    public TradableDTO[] removeQuotesForUser(String userName) throws NullParameterException, InvalidPriceException {
        if(userName == null) {
            throw new NullParameterException("userName is null");
        }

        TradableDTO[] quotes = new TradableDTO[2];

        quotes[0] = buySide.removeQuotesForUser(userName);
        quotes[1] = sellSide.removeQuotesForUser(userName);

        updateMarket();

        return quotes;
    }

    public void tryTrade() throws InvalidPriceException {
        Price topBuyPrice = buySide.topOfBookPrice();
        Price topSellPrice = sellSide.topOfBookPrice();

        if(topBuyPrice == null || topSellPrice == null) {
            return;
        }

        int totalToTrade = Integer.max(buySide.topOfBookVolume(), sellSide.topOfBookVolume());

        while(totalToTrade > 0) {

            topBuyPrice = buySide.topOfBookPrice();
            topSellPrice = sellSide.topOfBookPrice();

            if(topBuyPrice == null || topSellPrice == null) {
                return;
            }

            if(topSellPrice.greaterThan(topBuyPrice)) {
                return;
            }

            int toTrade = Integer.min(buySide.topOfBookVolume(), sellSide.topOfBookVolume());

            buySide.tradeOut(topBuyPrice, toTrade);
            sellSide.tradeOut(topBuyPrice, toTrade);

            totalToTrade -= toTrade;
        }
    }

    public String getTopOfBookString(BookSide side) {
        String returnStr = "";
        String topBuySidePrice;
        String topSellSidePrice;

        try {
            topBuySidePrice = buySide.topOfBookPrice().toString();
        } catch (Exception e) {
            topBuySidePrice = "$0.00";
        }

        try {
            topSellSidePrice = sellSide.topOfBookPrice().toString();
        } catch (Exception e) {
            topSellSidePrice = "$0.00";
        }

        if(side == BUY) {
            returnStr = "Top of BUY book: " + topBuySidePrice + " x " + buySide.topOfBookVolume();
        }

        if (side == SELL) {
            returnStr = "Top of SELL book: " + topSellSidePrice + " x " + sellSide.topOfBookVolume();
        }

        return returnStr;
    }

    private void updateMarket() throws InvalidPriceException, NullParameterException {
        Price topBuyPrice = buySide.topOfBookPrice();
        int topBuyVolume = buySide.topOfBookVolume();
        Price topSellPrice = sellSide.topOfBookPrice();
        int topSellVolume = sellSide.topOfBookVolume();

        CurrentMarketTracker.getInstance().updateMarket(product, topBuyPrice, topBuyVolume, topSellPrice, topSellVolume);
    }

    @Override
    public String toString() {
        return "\nProduct Book: " + product + "\n" + buySide.toString() + "\n" + sellSide.toString();
    }
}
