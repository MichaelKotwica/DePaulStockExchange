package tradable;

import book.BookSide;
import price.Price;

public interface Tradable {

    String getId();

    int getRemainingVolume();

    void setCancelledVolume(int newVol);

    int getCancelledVolume();

    void setRemainingVolume(int newVol);

    TradableDTO makeTradableDTO();

    Price getPrice();

    void setFilledVolume(int newVol);

    int getFilledVolume();

    BookSide getSide();

    String getUser();

    String getProduct();

    int getOriginalVolume();
}
