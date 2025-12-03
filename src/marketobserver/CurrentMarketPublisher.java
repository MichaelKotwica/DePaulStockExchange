package marketobserver;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrentMarketPublisher {

    private static final CurrentMarketPublisher instance = new CurrentMarketPublisher();

    // Symbol -> Observers
    private HashMap<String, ArrayList<CurrentMarketObserver>> filters = new HashMap<>();

    private CurrentMarketPublisher() {
        // Singleton, Private Constructor
    }

    public static CurrentMarketPublisher getInstance() {
        return instance;
    }

    public void subscribeCurrentMarket(String symbol, CurrentMarketObserver cmo) {
        if(!filters.containsKey(symbol)) {
            filters.put(symbol, new ArrayList<>());
        }
        filters.get(symbol).add(cmo);
    }

    public void unSubscribeCurrentMarket(String symbol, CurrentMarketObserver cmo) {
        if(!filters.containsKey(symbol)) {
            return;
        }
        filters.get(symbol).remove(cmo);
    }

    public void acceptCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide) {
        if(!filters.containsKey(symbol)) {
            return;
        }
        ArrayList<CurrentMarketObserver> observers = filters.get(symbol);

        for(CurrentMarketObserver observer: observers) {
            observer.updateCurrentMarket(symbol, buySide, sellSide);
        }
    }
}
