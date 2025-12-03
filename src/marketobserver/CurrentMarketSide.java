package marketobserver;

import exceptions.NullParameterException;
import price.Price;

public class CurrentMarketSide {

    private Price price;
    private int volume;

    public CurrentMarketSide(Price price, int volume) throws NullParameterException {
        setPrice(price);
        setVolume(volume);
    }

    private void setPrice(Price price) throws NullParameterException {
        if(price == null) {
            throw new NullParameterException("Price is null");
        }
        this.price = price;
    }

    private void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return price + "x" + volume;
    }
}
