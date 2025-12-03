package price;

import exceptions.InvalidPriceException;

import java.util.HashMap;

public abstract class PriceFactory {

    private static HashMap<Integer, Price> priceMap = new HashMap<>();

    public static Price makePrice(int value) {

        if (priceMap.containsKey(value)) {
            return priceMap.get(value);
        }

        Price price = new Price(value);
        priceMap.put(value, price);

        return price;
    }

    public static Price makePrice(String stringValueIn) throws InvalidPriceException {
        if (stringValueIn.isEmpty()) {
            throw new InvalidPriceException("empty string");
        }

        int decimalCount = 0;
        int dollarCount = 0;
        int negativeCount = 0;
        int decimalPlaces = 0;
        boolean foundDecimal = false;

        int centsValue;

        // Check if there is more than one decimal
        // Check for valid number of decimal places
        // Check if there is more than 1 dollar sign
        for(char c : stringValueIn.toCharArray()) {
            if(c == '.') {
                decimalCount++;
                foundDecimal = true;
            }
            if(c == '$') {
                dollarCount++;
            }
            if(c == '-') {
                negativeCount++;
            }
            if(foundDecimal) {
                decimalPlaces++;
            }
            if(foundDecimal && c == ',') {
                throw new InvalidPriceException("comma after decimal");
            }
        }

        if(foundDecimal) {
            decimalPlaces--;
        }

        //System.out.println("stringValueIn: " + stringValueIn);
        //System.out.println("decimal places: " + decimalPlaces);

        if(decimalCount > 1) {
            throw new InvalidPriceException("multiple decimals");
        }

        if(decimalPlaces == 1) {
            throw new InvalidPriceException("too few decimal places");
        }

        if(decimalPlaces > 2) {
            throw new InvalidPriceException("too many decimal places");
        }

        if(stringValueIn.contains("$") && !stringValueIn.startsWith("$")) {
            throw new InvalidPriceException("dollar sign in wrong place");
        }

        if(dollarCount > 1) {
            throw new InvalidPriceException("too many dollar signs");
        }

        if(negativeCount > 1) {
            throw new InvalidPriceException("too many negative signs");
        }

        if(stringValueIn.contains("-") && stringValueIn.contains("$") && stringValueIn.startsWith("$") && stringValueIn.indexOf("-") != 1) {
            throw new InvalidPriceException("negative sign in wrong place");
        }

        if(!foundDecimal || decimalPlaces == 0) {
            stringValueIn += "00";
        }

        try {
            centsValue = Integer.parseInt(stringValueIn.replace("$", "")
                    .replace(".", "")
                    .replace(",", ""));
        } catch (Exception e) {
            throw new InvalidPriceException("Invalid characters in stringValueIn: " + stringValueIn);
        }

        return makePrice(centsValue);
    }
}
