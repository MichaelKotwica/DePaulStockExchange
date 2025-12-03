package marketobserver;

import exceptions.InvalidPriceException;
import exceptions.NullParameterException;
import price.Price;
import price.PriceFactory;

public class CurrentMarketTracker {

    private static final CurrentMarketTracker instance = new CurrentMarketTracker();

    private CurrentMarketTracker() {
        // Singleton, Private Constructor
    }

    public static CurrentMarketTracker getInstance() {
        return instance;
    }

    public void updateMarket(String symbol, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume) throws InvalidPriceException, NullParameterException {
        Price marketWidth;

        if(sellPrice == null || buyPrice == null) {
            marketWidth = PriceFactory.makePrice(0);
        } else {
            marketWidth = sellPrice.subtract(buyPrice);
        }

        if(buyPrice == null) {
            buyPrice = PriceFactory.makePrice(0);
        }
        if(sellPrice == null) {
            sellPrice = PriceFactory.makePrice(0);
        }

        CurrentMarketSide currentBuyMarketSide = new CurrentMarketSide(buyPrice, buyVolume);
        CurrentMarketSide currentSellMarketSide = new CurrentMarketSide(sellPrice, sellVolume);

        StringBuilder sb = new StringBuilder();

        sb.append("*********** Current Market ***********")
                .append("\n* " + symbol + "   " + currentBuyMarketSide + " - " + currentSellMarketSide + " [" + marketWidth + "]")
                .append("\n**************************************");

        System.out.println(sb);
        CurrentMarketPublisher.getInstance().acceptCurrentMarket(symbol, currentBuyMarketSide, currentSellMarketSide);
    }
}
