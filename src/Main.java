// Please add any imports here as you have done in previous assignments

import book.ProductManager;
import marketobserver.CurrentMarketPublisher;
import price.PriceFactory;
import quote.Quote;
import tradable.Order;
import tradable.TradableDTO;
import user.User;
import user.UserManager;

import static book.BookSide.BUY;
import static book.BookSide.SELL;

public class Main {

    public static void main(String[] args) {

        try {
            System.out.println("\nSetup: Initialize Products");
            ProductManager.getInstance().addProduct("WMT");
            ProductManager.getInstance().addProduct("TGT");

            System.out.println("\nSetup: Initialize Users");
            UserManager.getInstance().init(
                    new String[]{"ANA", "BOB", "COD", "DIG", "EST"});

            User u1 = UserManager.getInstance().getUser("ANA");
            User u2 = UserManager.getInstance().getUser("BOB");
            User u3 = UserManager.getInstance().getUser("COD");
            User u4 = UserManager.getInstance().getUser("DIG");
            User u5 = UserManager.getInstance().getUser("EST");

            CurrentMarketPublisher.getInstance().subscribeCurrentMarket("WMT", u1);
            CurrentMarketPublisher.getInstance().subscribeCurrentMarket("TGT", u1);

            CurrentMarketPublisher.getInstance().subscribeCurrentMarket("WMT", u2);
            CurrentMarketPublisher.getInstance().subscribeCurrentMarket("TGT", u2);

            CurrentMarketPublisher.getInstance().subscribeCurrentMarket("WMT", u3);
            CurrentMarketPublisher.getInstance().subscribeCurrentMarket("TGT", u3);

            CurrentMarketPublisher.getInstance().subscribeCurrentMarket("TGT", u4);

            CurrentMarketPublisher.getInstance().subscribeCurrentMarket("WMT", u5);

            CurrentMarketPublisher.getInstance().unSubscribeCurrentMarket("TGT", u2);

            ////////////////////////////////////////////////////////////////////////////
            System.out.println("\nStep A) Build up book sides with Quotes (no trades)");
            ProductManager.getInstance().addQuote(
                    new Quote("TGT",
                            PriceFactory.makePrice(15990), 75,
                            PriceFactory.makePrice(16000), 75,
                            "ANA"));
            ProductManager.getInstance().addQuote(
                    new Quote("TGT",
                            PriceFactory.makePrice(15990), 100,
                            PriceFactory.makePrice(16000), 100,
                            "BOB"));
            System.out.println(ProductManager.getInstance().getProductBook("TGT").toString());

            ////////////////////////////////////////////////////////////////////////////
            System.out.println("\n\nStep B) Enter an Order that trades with the SELL side quotes");
            ProductManager.getInstance().addTradable(
                    new Order("COD", "TGT", PriceFactory.makePrice(16000), 100, BUY));
            System.out.println(ProductManager.getInstance().getProductBook("TGT").toString());

            ////////////////////////////////////////////////////////////////////////////
            System.out.println("\n\nStep C) Change user ANA's Quote");
            ProductManager.getInstance().addQuote(
                    new Quote("TGT",
                            PriceFactory.makePrice(15985), 111,
                            PriceFactory.makePrice(16000), 111,
                            "ANA"));
            System.out.println(ProductManager.getInstance().getProductBook("TGT").toString());

            ////////////////////////////////////////////////////////////////////////////
            System.out.println("\n\nStep E) Enter an Order that trades out the BUY side quotes");
            System.out.println(ProductManager.getInstance().getProductBook("TGT").toString());
            ProductManager.getInstance().addTradable(
                    new Order("DIG", "TGT", PriceFactory.makePrice(15985), 211, SELL));
            System.out.println(ProductManager.getInstance().getProductBook("TGT").toString());

            ////////////////////////////////////////////////////////////////////////////
            System.out.println("\n\nStep F) Add a BUY TGT order from user DIG that does not trade");
            TradableDTO t1 = ProductManager.getInstance().addTradable(
                    new Order("DIG", "TGT", PriceFactory.makePrice(15985), 211, BUY));
            System.out.println(ProductManager.getInstance().getProductBook("TGT").toString());

            ////////////////////////////////////////////////////////////////////////////
            System.out.println("\n\nStep G) Print users and their OrderDTO's (user order doesn't matter)");
            System.out.println(UserManager.getInstance().toString());

            ////////////////////////////////////////////////////////////////////////////
            System.out.println("\n\nStep H) Print product books");
            System.out.println(ProductManager.getInstance().toString());

            ////////////////////////////////////////////////////////////////////////////
            System.out.println("\n\nStep I) Enter 3 BUY side orders and 1 quote for WMT");
            TradableDTO t2 = ProductManager.getInstance().addTradable(
                    new Order("COD", "WMT", PriceFactory.makePrice(6010), 50, BUY));
            TradableDTO t3 = ProductManager.getInstance().addTradable(
                    new Order("DIG", "WMT", PriceFactory.makePrice(6010), 100, BUY));
            TradableDTO t4 = ProductManager.getInstance().addTradable(
                    new Order("EST", "WMT", PriceFactory.makePrice(6010), 75, BUY));
            ProductManager.getInstance().addQuote(
                    new Quote("WMT",
                            PriceFactory.makePrice(6010), 111,
                            PriceFactory.makePrice(6012), 111,
                            "ANA"));
            System.out.println(ProductManager.getInstance().getProductBook("WMT").toString());

            ////////////////////////////////////////////////////////////////////////////
            System.out.println("\n\nStep J) Display current market values received by all user");
            System.out.println(u1.getUserId() + ":\n" + u1.getCurrentMarkets());
            System.out.println(u2.getUserId() + ":\n" + u2.getCurrentMarkets());
            System.out.println(u3.getUserId() + ":\n" + u3.getCurrentMarkets());
            System.out.println(u4.getUserId() + ":\n" + u4.getCurrentMarkets());
            System.out.println(u5.getUserId() + ":\n" + u5.getCurrentMarkets());

            ////////////////////////////////////////////////////////////////////////////
            System.out.println("\n\nStep K) Cancel ANA's WMT quote");
            ProductManager.getInstance().cancelQuote("WMT", "ANA");

            ////////////////////////////////////////////////////////////////////////////
            System.out.println("\n\nStep L) Display current market values received by all user");
            System.out.println(u1.getUserId() + ":\n" + u1.getCurrentMarkets());
            System.out.println(u2.getUserId() + ":\n" + u2.getCurrentMarkets());
            System.out.println(u3.getUserId() + ":\n" + u3.getCurrentMarkets());
            System.out.println(u4.getUserId() + ":\n" + u4.getCurrentMarkets());
            System.out.println(u5.getUserId() + ":\n" + u5.getCurrentMarkets());

            ////////////////////////////////////////////////////////////////////////////
            System.out.println("\n\nStep M) Cancel any open orders and quotes");
            ProductManager.getInstance().cancelQuote("TGT", "ANA");
            ProductManager.getInstance().cancelQuote("TGT", "BOB");
            ProductManager.getInstance().cancel(t1);
            ProductManager.getInstance().cancel(t2);
            ProductManager.getInstance().cancel(t3);
            ProductManager.getInstance().cancel(t4);

            ////////////////////////////////////////////////////////////////////////////
            System.out.println("\n\nStep N) Display current market values received by all user");
            System.out.println(u1.getUserId() + ":\n" + u1.getCurrentMarkets());
            System.out.println(u2.getUserId() + ":\n" + u2.getCurrentMarkets());
            System.out.println(u3.getUserId() + ":\n" + u3.getCurrentMarkets());
            System.out.println(u4.getUserId() + ":\n" + u4.getCurrentMarkets());
            System.out.println(u5.getUserId() + ":\n" + u5.getCurrentMarkets());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
