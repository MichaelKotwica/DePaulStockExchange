package user;

import exceptions.InvalidParameterException;
import marketobserver.CurrentMarketObserver;
import marketobserver.CurrentMarketSide;
import tradable.TradableDTO;

import java.util.HashMap;

public class User implements CurrentMarketObserver {
    private String userId;
    private HashMap<String, TradableDTO> tradables = new HashMap<>();
    private HashMap<String, CurrentMarketSide[]> currentMarkets = new HashMap<>();

    public User(String userId) throws InvalidParameterException {
        setUserId(userId);
    }

    private void setUserId(String id) throws InvalidParameterException {
        if(id == null) {
            throw new InvalidParameterException("Id is null");
        }

        if(id.length() != 3) {
            throw new InvalidParameterException("User is " + id.length() + " characters long. Expected length: 3");
        }

        for (int i = 0; i < id.length(); i++) {
            if (!Character.isLetter(id.charAt(i)) || Character.isWhitespace(id.charAt(i))) {
                throw new InvalidParameterException("Found invalid character '" + id.charAt(i) + "' in user string " + id);
            }
        }

        this.userId = id;
    }

    public void updateTradable(TradableDTO o) {
        if (o != null) {
            tradables.put(o.tradableId(), o);
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getCurrentMarkets() {
        StringBuilder sb = new StringBuilder();

        for(String symbol: currentMarkets.keySet()) {
            sb.append(symbol).append("   ").append(currentMarkets.get(symbol)[0]).append(" - ").append(currentMarkets.get(symbol)[1]).append("\n");
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  User Id: ").append(userId);

        for(TradableDTO tradable : tradables.values()) {
            sb.append("\n\t").append("Product: " + tradable.product() + ", Price: " + tradable.price() +
                    ", OriginalVolume: " + tradable.originalVolume() + ", RemainingVolume: " + tradable.remainingVolume() +
                    ", CancelledVolume: " + tradable.cancelledVolume() + ", FilledVolume: " + tradable.filledVolume() +
                    ", User: " + tradable.user() + ", Side: " + tradable.side() + ", Id: " + tradable.tradableId());
        }

        return sb.toString();
    }

    @Override
    public void updateCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide) {
        CurrentMarketSide[] currentMarketSides = new CurrentMarketSide[2];
        currentMarketSides[0] = buySide;
        currentMarketSides[1] = sellSide;

        currentMarkets.put(symbol, currentMarketSides);
    }
}
