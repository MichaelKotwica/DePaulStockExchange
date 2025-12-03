package book;

import exceptions.DataValidationException;
import exceptions.InvalidParameterException;
import exceptions.InvalidPriceException;
import exceptions.NullParameterException;
import quote.Quote;
import tradable.Tradable;
import tradable.TradableDTO;
import user.UserManager;

import java.util.HashMap;
import java.util.Random;

public final class ProductManager {

    // Basic Eager initialization Singleton
    private static final ProductManager instance = new ProductManager();

    // String Product Symbol -> Product Book for the Symbol
    private HashMap<String, ProductBook> productBooks = new HashMap<>();

    private ProductManager() {
        // private constructor
    }

    public static ProductManager getInstance() {
        return instance;
    }

    public void addProduct(String symbol) {
        if(symbol == null) {
            throw new DataValidationException("Symbol is null");
        }
        try {
            productBooks.put(symbol, new ProductBook(symbol));
        } catch (InvalidParameterException | NullParameterException e) {
            throw new DataValidationException("Symbol " + symbol + " does does not meet symbol requirements for a productBook");
        }

    }

    public ProductBook getProductBook(String symbol) {
        if(!productBooks.containsKey(symbol)) {
            throw new DataValidationException("Symbol " + symbol + " does not exist");
        }
        return productBooks.get(symbol);
    }

    public String getRandomProduct() {
        if(productBooks.isEmpty()) {
            throw new DataValidationException("No products exist");
        }

        Random random = new Random();
        String productStr;

        Object[] productsArr = productBooks.keySet().toArray();

        productStr = productsArr[random.nextInt(productsArr.length)].toString();

        return productStr;
    }

    public TradableDTO addTradable(Tradable o) {
        if(o == null) {
            throw new DataValidationException("Tradable is null");
        }

        TradableDTO tradable = null;

        if(productBooks.containsKey(o.getProduct())) {
            try {
                tradable = productBooks.get(o.getProduct()).add(o);
            } catch (NullParameterException | InvalidPriceException e) {
                throw new DataValidationException(e.getMessage());
            }

            UserManager.getInstance().updateTradable(tradable.user(), tradable);
        }

        return tradable;
    }

    public TradableDTO[] addQuote(Quote q) {
        if(q == null) {
            throw new DataValidationException("Quote is null");
        }

        TradableDTO[] tradableDTOS = new TradableDTO[2];
        ProductBook productBook = productBooks.get(q.getSymbol());

        try {
            productBook.removeQuotesForUser(q.getUser());
        } catch (NullParameterException | InvalidPriceException e) {
            throw new DataValidationException(e.getMessage());
        }

        tradableDTOS[0] = addTradable(q.getQuoteSide(BookSide.BUY));
        tradableDTOS[1] = addTradable(q.getQuoteSide(BookSide.SELL));

        return tradableDTOS;
    }

    public TradableDTO cancel(TradableDTO o) {
        if(o == null) {
            throw new DataValidationException("Tradable DTO is null");
        }

        if(productBooks.containsKey(o.product())) {
            try {
               return productBooks.get(o.product()).cancel(o.side(), o.tradableId());
            } catch (NullParameterException | InvalidPriceException e) {
                System.out.println("Failed to cancel");
                return null;
            }
        }
        System.out.println("Failed to cancel");
        return null;
    }

    public TradableDTO[] cancelQuote(String symbol, String user) throws NullParameterException, InvalidPriceException {
        if(symbol == null) {
            throw new DataValidationException("Symbol is null");
        }
        if(user == null) {
            throw new DataValidationException("User is null");
        }
        if(!productBooks.containsKey(symbol)) {
            throw new DataValidationException("Symbol: " + symbol + " does not exist in productBooks");
        }

        ProductBook productBook = productBooks.get(symbol);
        return productBook.removeQuotesForUser(user);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(ProductBook productBook : productBooks.values()) {
            sb.append(productBook);
        }

        return sb.toString();
    }
}
