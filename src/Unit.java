import java.util.HashMap;

public class Unit {

    public String itemID;
    public int turnoverWB;
    public int remainderWB;
    public int remainderInWaiting;
    public int remainderInStock;
    public String itemGroup;
    public int turnoverAll;
    float currentProfit;
    float currentPrice;
    float currentCommission;
    float salePrice;
    int recentSales;

    Unit(String[] unitLine, HashMap<String,Integer> indexes) {
        this.itemID = unitLine[indexes.get(Fields.itemIDName)];
        this.turnoverWB = Utils.parseIntForNA(unitLine[indexes.get(Fields.turnoverWBName)]);
        this.remainderWB = Utils.parseIntForNA(unitLine[indexes.get(Fields.remainderWBName)]);
        this.remainderInStock = Utils.parseIntForNA(unitLine[indexes.get(Fields.remainderInStockName)]);
        this.remainderInWaiting = Utils.parseIntForNA(unitLine[indexes.get(Fields.remainderInWaitingName)]);
        this.itemGroup = unitLine[indexes.get(Fields.itemGroupName)];
        this.turnoverAll = Utils.parseIntForNA(unitLine[indexes.get(Fields.turnoverAllName)]);
        this.currentProfit = (float) Utils.parseIntForNA(unitLine[indexes.get(Fields.currentProfitName)]);
        this.currentPrice = (float) Utils.parseIntForNA(unitLine[indexes.get(Fields.currentPriceName)]);
        this.currentCommission = (float) Utils.parseIntForNA(unitLine[indexes.get(Fields.currentCommissionName)]);
        this.salePrice = (float) Utils.parseIntForNA(unitLine[indexes.get(Fields.salePriceName)]);
        this.recentSales = Utils.parseIntForNA(unitLine[indexes.get(Fields.recentSalesName)]);
    }

    public int countPriceChangeForProfit(float targetProfit){
        float commissionRate = this.currentCommission/this.currentPrice;
        float priceRemainder = this.currentPrice*(1-commissionRate-0.05f-this.currentProfit/100);
        float newPrice = priceRemainder/(1-targetProfit/100-commissionRate-0.05f);
        return Math.round(newPrice-this.currentPrice);
    }

}
