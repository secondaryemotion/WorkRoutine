import java.io.IOException;
import java.util.ArrayList;

public class Manager {


    public WBTable readPnList(String path) {
        String pnFile = Utils.readFile(path);
        assert pnFile != null;
        pnFile = pnFile.replace("liana_redkalist_2,3_12XXX","liana_redkalist_23_12XXX");
        String[] tableLines = pnFile.split("\n");
        WBTable PnList = new WBTable();
        PnList.createTable(tableLines);
        return PnList;
    }

    public void processCampaignsAndPrepareChangesList(WBTable table, boolean isNewYear) throws IOException {
        ArrayList<String> stopCampaigns = new ArrayList<>();
        ArrayList<String> endCampaigns = new ArrayList<>();
        for (int i = 1; i < table.data.length; i++) {
            if (!table.getValueByColumnIndex(i, 0).equals("")) {
                Unit currentUnit = new Unit(table.data[i], table.indexes);
                int turnoverRate = currentUnit.turnoverAll;
                int remainderWB = currentUnit.remainderWB;
                int remainderInWaiting = currentUnit.remainderInWaiting;
                int remainderInStock = currentUnit.remainderInStock;
                int remainderTotal = remainderWB + remainderInStock;
                float currentProfit = currentUnit.currentProfit;
                String itemGroup = currentUnit.itemGroup;
                String itemID = currentUnit.itemID;

                if (currentProfit >= 29){
                    stopCampaigns.add(itemID);
                    continue;
                }

                if (!table.illiquid.contains(itemGroup) && turnoverRate < 24) { // оборачиваемость менее 17 дней
                    stopCampaigns.add(itemID);
                    continue;
                }
                if (table.illiquid.contains(itemGroup) && turnoverRate < 45) { // оборачиваемость менее 40 дней
                    stopCampaigns.add(itemID);
                    continue;
                }
                if (remainderTotal < 50) {
                    //список для приостановки
                    if (table.illiquid.contains(itemGroup) && remainderInWaiting == 0) { // неликвид с малым остатком
                        endCampaigns.add(itemID);//список для закрытия
                    }
                    stopCampaigns.add(itemID);//добавляем тк табличка считает разницу между списками
                    continue;
                }


            }
        }
        Utils.writeArrayListToFile(stopCampaigns, table.paths.get("campaignsPath"));
        Utils.writeArrayListToFile(endCampaigns, table.paths.get("campaignsEndPath"));
        System.out.println("Скрипт выполнен");
    }

    public void processCampaignsAndPrepareChangesList(WBTable table) throws IOException {
        processCampaignsAndPrepareChangesList(table, false);
    }

    public void processPricesAndPrepareChangesList(WBTable table) throws IOException {
        ArrayList<String> priceChanges = new ArrayList<>();
        ArrayList<String> idsToAddToActive = new ArrayList<>();
        ArrayList<String> idsToAddToNew = new ArrayList<>();
        ArrayList<String> idsToAddToIlliquid = new ArrayList<>();
        ArrayList<String> idsToOrder = new ArrayList<>();
        ArrayList<String> idsToShip = new ArrayList<>();
        for (int i = 1; i < table.data.length; i++) {
            if (!table.getValueByColumnIndex(i, 0).equals("")) {
                Unit currentUnit = new Unit(table.data[i], table.indexes);

                if (currentUnit.itemGroup.equals("Актив/Новый год / Сезонные")){
                    continue;
                } //скипаем нг


                if (currentUnit.salePrice >= currentUnit.currentPrice){
                    if (currentUnit.turnoverAll <= 3) {
                        int priceChange = currentUnit.countPriceChangeForProfit(currentUnit.currentProfit + 15);
                        priceChanges.add(currentUnit.itemID + "," + priceChange + ",товар в акции но кончается +15%");
                    }
                    continue;
                } //не трогаем ребят в акции, только если малый остаток

                if (table.activeGroups.contains(currentUnit.itemGroup) && currentUnit.currentProfit < 15 && currentUnit.recentSales >= 5) {
                    int priceChange = currentUnit.countPriceChangeForProfit(25);
                    priceChanges.add(currentUnit.itemID + "," + priceChange + ",заниженный актив");
                    continue;
                }


                if (currentUnit.turnoverWB < 15) { //хорошо продается
                    String result = priceChangeForLowTurnover(currentUnit, table);
                    priceChanges.add(result);
                    if (currentUnit.remainderInStock > 0) {
                        idsToShip.add(currentUnit.itemID);
                    } else if (currentUnit.remainderInWaiting == 0) {
                        idsToOrder.add(currentUnit.itemID);
                    }
                    if (currentUnit.itemGroup.equals(table.newGroup)){
                        idsToAddToActive.add(currentUnit.itemID);
                    }
                } else if (currentUnit.turnoverWB < 20) { //небольшое повышение до 20 дней
                int priceChange = currentUnit.countPriceChangeForProfit(currentUnit.currentProfit + 2);
                priceChanges.add(currentUnit.itemID + "," + priceChange + ",повышение 2% до 20 дней");
                                }

                if (currentUnit.turnoverWB > 100 && currentUnit.remainderWB > 40) { //не продаются, остаток от 40
                    String result = priceChangeForHighTurnover(currentUnit, table);
                    priceChanges.add(result);

                    if (table.activeGroups.contains(currentUnit.itemGroup) && currentUnit.currentProfit < 20) {
                        idsToAddToNew.add(currentUnit.itemID);
                    } else if (currentUnit.itemGroup.equals(table.newGroup) && currentUnit.currentProfit < 10) {
                        idsToAddToIlliquid.add(currentUnit.itemID);
                    }
                }
            }
        }
        Utils.writeArrayListToFile(priceChanges, table.paths.get("pricesToChangePath"));
        Utils.writeArrayListToFile(idsToAddToActive, table.paths.get("idsToAddToActivePath"));
        Utils.writeArrayListToFile(idsToAddToNew, table.paths.get("idsToAddToNewPath"));
        Utils.writeArrayListToFile(idsToAddToIlliquid, table.paths.get("idsToAddToIlliquidPath"));
        Utils.writeArrayListToFile(idsToOrder, table.paths.get("idsToOrderPath"));
        Utils.writeArrayListToFile(idsToShip, table.paths.get("idsToShipPath"));
        System.out.println("Скрипт выполнен");

    }

    private static String priceChangeForLowTurnover(Unit currentUnit, WBTable table) {
        if (currentUnit.itemGroup.equals(table.illiquid)) { //неликвиды
            if (currentUnit.currentProfit < 0) { //если рент меньше 0, рент +5%
                int priceChange = currentUnit.countPriceChangeForProfit(currentUnit.currentProfit + 5);
                return (currentUnit.itemID + "," + priceChange + ",неликвид до 0% +5%");
            }
        } else if (table.activeGroups.contains(currentUnit.itemGroup) || currentUnit.itemGroup.equals(table.newGroup)) {
            if (currentUnit.turnoverWB < 10) {
                int priceChange = currentUnit.countPriceChangeForProfit(currentUnit.currentProfit + 5);
                return (currentUnit.itemID + "," + priceChange + ",актив +5%");
            } else {
                int priceChange = currentUnit.countPriceChangeForProfit(currentUnit.currentProfit + 3);
                return (currentUnit.itemID + "," + priceChange + ",актив +3%");
            }
        }
        return "";
    }

    private static String priceChangeForHighTurnover(Unit currentUnit, WBTable table) {
        if (currentUnit.itemGroup.equals(table.illiquid)) {
            if (currentUnit.currentProfit >= 15) { //если рент больше 15%, снизить до 15%
                int priceChange = currentUnit.countPriceChangeForProfit(15);
                return (currentUnit.itemID + "," + priceChange + ",неликвид больше 15% в 15%");
            } else if (currentUnit.currentProfit > -20) { //снижаем на 2%
                int priceChange = currentUnit.countPriceChangeForProfit(currentUnit.currentProfit - 2);
                return (currentUnit.itemID + "," + priceChange + ",неликвид ниже 15% -2%");
            }
        } else if (currentUnit.itemGroup.equals(table.newGroup)) {
            if (currentUnit.currentProfit >= 20) { //если рент больше 20%, снизить до 20%
                int priceChange = currentUnit.countPriceChangeForProfit(20);
                return (currentUnit.itemID + "," + priceChange + ",новинки выше 20% в 20%");
            } else if (currentUnit.currentProfit >= 10) { //снижаем на 2%
                int priceChange = currentUnit.countPriceChangeForProfit(currentUnit.currentProfit - 2);
                return (currentUnit.itemID + "," + priceChange + ",новинки выше 10% -2%");
            }
        } else if (table.activeGroups.contains(currentUnit.itemGroup)) {
            if (currentUnit.currentProfit >= 25) { //если рент больше 25%, снизить до 25%
                int priceChange = currentUnit.countPriceChangeForProfit(25);
                return (currentUnit.itemID + "," + priceChange + ",актив выше 25% в 25%");
            } else { //снижаем на 2%
                int priceChange = currentUnit.countPriceChangeForProfit(currentUnit.currentProfit - 2);
                return (currentUnit.itemID + "," + priceChange + ",актив -2%");
            }
        }
        return "";
    }
}
