package book;

import exceptions.InvalidPriceException;
import exceptions.NullParameterException;
import price.Price;
import tradable.Tradable;
import tradable.TradableDTO;
import user.UserManager;

import java.util.*;

import static book.BookSide.BUY;
import static book.BookSide.SELL;

public class ProductBookSide {

    private BookSide side;
    private final TreeMap<Price, ArrayList<Tradable>> bookEntries;

    public ProductBookSide(BookSide side) throws NullParameterException {
        setSide(side);

        if(side == BUY) {
            bookEntries = new TreeMap<>(Collections.reverseOrder());
        } else {
            bookEntries = new TreeMap<>();
        }
    }

    private void setSide(BookSide side) throws NullParameterException {
        if(side == null) {
            throw new NullParameterException("Side is null");
        }
        this.side = side;
    }

    private BookSide getSide() {
        return side;
    }

    public TradableDTO add(Tradable o) {
        if(!bookEntries.containsKey(o.getPrice())) {
            bookEntries.put(o.getPrice(), new ArrayList<>());
        }

        TradableDTO addedTradable;

        bookEntries.get(o.getPrice()).add(o);

        System.out.println("**ADD: " + o);

        addedTradable = new TradableDTO(o);
        UserManager.getInstance().updateTradable(addedTradable.user(), addedTradable);
        return addedTradable;
    }

    public TradableDTO cancel(String tradableId) {
        TradableDTO canceledTradable = null;

        Iterator<Price> priceIterator = bookEntries.keySet().iterator();

        while (priceIterator.hasNext()) {
            Price price = priceIterator.next();
            Iterator<Tradable> tradableIterator = bookEntries.get(price).iterator();

            while (tradableIterator.hasNext()) {
                Tradable tradable = tradableIterator.next();

                if(tradable.getId().equals(tradableId)) {
                    System.out.println("**CANCEL: " + tradable);
                    tradableIterator.remove();
                    tradable.setCancelledVolume(tradable.getRemainingVolume());
                    tradable.setRemainingVolume(0);

                    if(bookEntries.get(price).isEmpty()) {
                        priceIterator.remove();
                    }

                    canceledTradable = new TradableDTO(tradable);

                }
            }
        }
        if (canceledTradable != null) {
            UserManager.getInstance().updateTradable(canceledTradable.user(), canceledTradable);
        }
        return canceledTradable;
    }

    public TradableDTO removeQuotesForUser(String userName) {
        Iterator<Tradable> tradableIterator;

        TradableDTO canceledQuote = null;

        for(Price p : bookEntries.keySet()) {
            tradableIterator = bookEntries.get(p).iterator();

            while (tradableIterator.hasNext()) {
                Tradable tradable = tradableIterator.next();

                if(tradable.getUser().equals(userName)) {
                    canceledQuote = cancel(tradable.getId());
                    UserManager.getInstance().updateTradable(canceledQuote.user(), canceledQuote);
                    return canceledQuote;
                }
            }
        }

        return canceledQuote;
    }

    public Price topOfBookPrice() {
        if(bookEntries.isEmpty()) {
            return null;
        }

        return bookEntries.firstKey();
    }

    public int topOfBookVolume() {
        int volume = 0;

        if(side == BUY) {
            if(!bookEntries.isEmpty()) {
                for(Tradable t : bookEntries.get(bookEntries.firstKey())) {
                    volume += t.getRemainingVolume();
                }
            }
        }

        if (side == SELL) {
            if(!bookEntries.isEmpty()) {
                for(Tradable t : bookEntries.get(bookEntries.firstKey())) {
                    volume += t.getRemainingVolume();
                }
            }
        }
        return volume;
    }

    public void tradeOut(Price price, int volToTrade) {
        Price topBookPrice = topOfBookPrice();
        ArrayList<Tradable> atPrice;
        int totalVolAtPrice = 0;
        TradableDTO tradableDTO = null;

        if(topBookPrice == null) {
            return;
        }

        try {
            if(topBookPrice.greaterThan(price)) {
                return;
            }
        } catch (InvalidPriceException e) {
            return;
        }

        atPrice = bookEntries.get(price);

        if(atPrice == null) {
            atPrice = bookEntries.get(topBookPrice);
        }

        for (Tradable t : atPrice) {
            totalVolAtPrice += t.getRemainingVolume();
        }

        if(volToTrade >= totalVolAtPrice) {
            for(Tradable t : atPrice) {
                int rv = t.getRemainingVolume();
                t.setFilledVolume(t.getOriginalVolume());
                t.setRemainingVolume(0);
                System.out.println("\tFULL FILL: (" + getSide() + " " + rv + ") " + t);
                tradableDTO = new TradableDTO(t);
            }
            bookEntries.remove(bookEntries.firstKey());
            if (tradableDTO != null) {
                UserManager.getInstance().updateTradable(tradableDTO.user(), tradableDTO);
            }
        } else {
            int remainder = volToTrade;
            for(Tradable t : atPrice) {
                double ratio = (double) t.getRemainingVolume() / totalVolAtPrice;
                int toTrade = (int) Math.ceil(volToTrade * ratio);
                toTrade = Math.min(toTrade, remainder);
                t.setFilledVolume(t.getFilledVolume() + toTrade);
                t.setRemainingVolume(t.getRemainingVolume() - toTrade);
                System.out.println("\tPARTIAL FILL: (" + getSide() + " " + toTrade + ") " + t);
                remainder -= toTrade;
                tradableDTO = new TradableDTO(t);
            }
            if (tradableDTO != null) {
                UserManager.getInstance().updateTradable(tradableDTO.user(), tradableDTO);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Side: ").append(getSide());

        if (bookEntries.isEmpty()) {
            sb.append("\n\t<Empty>");
        }

        for(Map.Entry<Price, ArrayList<Tradable>> entry : bookEntries.entrySet()) {
            sb.append("\n\t" + "Price: ").append(entry.getKey()).append(":");
            for(Tradable t : entry.getValue()) {
                sb.append("\n\t\t").append(t);
            }
        }

        return sb.toString();
    }
}
