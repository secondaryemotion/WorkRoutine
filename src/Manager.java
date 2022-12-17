import java.io.IOException;
import java.util.ArrayList;

public class Manager {

    public WBTable readPnAList(){
        String pnaPath = "csv/pnalist.csv";
        String pnaFile = Utils.readFile(pnaPath);
        String[] tableLines = pnaFile.split("\n");
        WBTable PnAList = new WBTable();
        PnAList.createTable(tableLines);
        PnAList.setInfo("А");
        return PnAList;
    }

    public WBTable readPnJList(){
        String pnjPath = "csv/pnjlist.csv";
        String pnjFile = Utils.readFile(pnjPath);
        String[] tableLines = pnjFile.split("\n");
        WBTable PnJList = new WBTable();
        PnJList.createTable(tableLines);
        PnJList.setInfo("Ж");
        return PnJList;
    }

    public void processCampaignsAndPrepareChangesList(WBTable table, boolean isNewYear) throws IOException {
        ArrayList<String> stopCampaigns = new ArrayList<>();
        ArrayList<String> endCampaigns = new ArrayList<>();
        int turnoverIndex = table.turnoverIndex;
        int remainderIndex = table.remainderIndex;
        for (int i = 1; i < table.data.length; i++){
            if (!table.getValueByColumnIndex(i,turnoverIndex).equals("")) {
                int turnoverRate = table.getIntegerValueByColumn(i,turnoverIndex);
                int remainderWB = table.getIntegerValueByColumn(i,remainderIndex);
                int remainderInWaiting = table.getIntegerValueByColumn(i,remainderIndex+2);
                int remainderInStock = table.getIntegerValueByColumn(i,remainderIndex+3);
                int remainderTotal = remainderWB+remainderInStock;
                String itemGroup = table.getValueByColumnIndex(i,2);
                String itemID = table.getValueByColumnIndex(i, 0);

                if (turnoverRate < 30) { // оборачиваемость менее 30 дней
                    if (itemGroup.equals("Актив/Новый год / Сезонные") && isNewYear){
                        if (turnoverRate < (29-16)) {
                            stopCampaigns.add(table.getValueByColumnIndex(i, 0));
                            continue;
                        }
                        continue;
                    }
                    stopCampaigns.add(table.getValueByColumnIndex(i, 0));
                    continue;
                }
                if (remainderTotal < 50){
                    if (table.illiquid.contains(itemGroup) && remainderInWaiting == 0){ // неликвид с малым остатком
                        endCampaigns.add(itemID);//список для закрытия
                        stopCampaigns.add(itemID);//добавляем тк табличка считает разницу между списками
                    } else {
                        stopCampaigns.add(itemID); //список для приостановки
                    }
                }
            }
        }
        Utils.writeArrayListToFile(stopCampaigns,table.paths.get("campaignsPath"));
        Utils.writeArrayListToFile(endCampaigns,table.paths.get("campaignsEndPath"));
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
        //Utils.askForColumnNumber("обор",table.header);
        int turnoverWBIndex = table.turnoverIndex + 4;
        int currentProfitIndex = 8; //мэджик намба но оно экшели редко меняется, потом прикручу проверку
        int remainderIndex = table.remainderIndex;
        int commissionIndex = table.commissionIndex;
        for (int i = 1; i < table.data.length; i++) {
            if (!table.getValueByColumnIndex(i, turnoverWBIndex).equals("")) {
                int turnoverWB = table.getIntegerValueByColumn(i,turnoverWBIndex);
                int remainderWB = table.getIntegerValueByColumn(i,remainderIndex);
                int remainderInWaiting = table.getIntegerValueByColumn(i,remainderIndex+2);
                int remainderInStock = table.getIntegerValueByColumn(i,remainderIndex+3);
                String itemGroup = table.getValueByColumnIndex(i,2);
                String itemID = table.getValueByColumnIndex(i, 0);
                float currentProfit = (float) table.getIntegerValueByColumn(i,currentProfitIndex);
                float currentPrice = (float) table.getIntegerValueByColumn(i,3);
                float currentCommission = (float) table.getIntegerValueByColumn(i,commissionIndex);

                if (turnoverWB < 15) { //скоро продадутся
                    if (itemGroup.equals(table.illiquid)){ //неликвиды
                        if (currentProfit < 0) { //если рент меньше 0, рент +5%
                            int priceChange = countPriceChangeForProfit(currentProfit,currentProfit+5,currentCommission,currentPrice);
                            priceChanges.add(itemID + "," + priceChange + ",неликвид до 0% +5%");
                        }
                        if (remainderInStock != 0){ //если есть на складе - отгрузить, даже если меняли цену
                            idsToShip.add(itemID);
                            continue;
                        }
                        continue;
                    }
                    if (table.activeGroups.contains(itemGroup) || itemGroup.equals(table.newGroup)){
                        //для актива и новинок одна логика здесь
                        if (remainderInStock >= 100) { //есть на складе, поднимаем на 5% и отгружаем
                            idsToShip.add(itemID);
                            int priceChange = countPriceChangeForProfit(currentProfit,currentProfit+5,currentCommission,currentPrice);
                            priceChanges.add(itemID + "," + priceChange + ",актив есть на мс +5%");
                            if (itemGroup.equals(table.newGroup)){
                                idsToAddToActive.add(itemID);
                            }
                            continue;
                        }
                        if (remainderInWaiting != 0){ //нет на складе, есть ожидание, поднимаем на 10%
                            int priceChange = countPriceChangeForProfit(currentProfit,currentProfit+10,currentCommission,currentPrice);
                            priceChanges.add(itemID + "," + priceChange + ",актив на складе менее 100 +10%");
                        } else { //нет на складе, нет ожидания. добавляем на закупку, +15%
                            idsToOrder.add(itemID);
                            int priceChange = countPriceChangeForProfit(currentProfit,currentProfit+15,currentCommission,currentPrice);
                            priceChanges.add(itemID + "," + priceChange+",актив нет ожидания +15%");
                        }
                        if (itemGroup.equals(table.newGroup)){ //новинки также предлагаем в актив
                            idsToAddToActive.add(itemID);
                        }
                        continue;
                    }
                } else if (turnoverWB > 120 && remainderWB > 40){ //не продаются, остаток от 40
                    if (itemGroup.equals(table.illiquid)){
                        if (currentProfit >= 15){ //если рент больше 15%, снизить до 15%
                            int priceChange = countPriceChangeForProfit(currentProfit,15,currentCommission,currentPrice);
                            priceChanges.add(itemID + "," + priceChange+",неликвид больше 15% в 15%");
                        } else if (currentProfit > -20){ //снижаем на 2%
                            int priceChange = countPriceChangeForProfit(currentProfit,currentProfit-2,currentCommission,currentPrice);
                            priceChanges.add(itemID + "," + priceChange+",неликвид ниже 15% -2%");
                        }
                        continue; //ренту ещё ниже вообще не трогаем
                    } else if (itemGroup.equals(table.newGroup)){
                        if (currentProfit >= 20){ //если рент больше 20%, снизить до 20%
                            int priceChange = countPriceChangeForProfit(currentProfit,20,currentCommission,currentPrice);
                            priceChanges.add(itemID + "," + priceChange+",новинки выше 20% в 20%");
                        } else if (currentProfit >= 10) { //снижаем на 2%
                            int priceChange = countPriceChangeForProfit(currentProfit, currentProfit - 2, currentCommission, currentPrice);
                            priceChanges.add(itemID + "," + priceChange +",неликвид выше 10% -2%");
                        } else {//предлагаем в неликвид
                            idsToAddToIlliquid.add(itemID);
                        }
                    } else if (table.activeGroups.contains(itemGroup)) {
                        if (currentProfit >= 25) { //если рент больше 25%, снизить до 25%
                            int priceChange = countPriceChangeForProfit(currentProfit, 25, currentCommission, currentPrice);
                            priceChanges.add(itemID + "," + priceChange+",актив выше 25% в 25%");
                        } else if (currentProfit >= 20) { //снижаем на 2%
                            int priceChange = countPriceChangeForProfit(currentProfit, currentProfit - 2, currentCommission, currentPrice);
                            priceChanges.add(itemID + "," + priceChange+",актив выше 20% -2%");
                        } else {//предлагаем в новинки
                            idsToAddToNew.add(itemID);
                        }
                    }
                } else if (turnoverWB < 20){
                    int priceChange = countPriceChangeForProfit(currentProfit, currentProfit+1, currentCommission, currentPrice);
                    priceChanges.add(itemID + "," + priceChange+",повышение 2% до 20 дней");
                }


            }
        }
        Utils.writeArrayListToFile(priceChanges,table.paths.get("pricesToChangePath"));
        Utils.writeArrayListToFile(idsToAddToActive,table.paths.get("idsToAddToActivePath"));
        Utils.writeArrayListToFile(idsToAddToNew,table.paths.get("idsToAddToNewPath"));
        Utils.writeArrayListToFile(idsToAddToIlliquid,table.paths.get("idsToAddToIlliquidPath"));
        Utils.writeArrayListToFile(idsToOrder,table.paths.get("idsToOrderPath"));
        Utils.writeArrayListToFile(idsToShip,table.paths.get("idsToShipPath"));
        System.out.println("Скрипт выполнен");

    }
    private static int countPriceChangeForProfit(float baseProfit, float targetProfit, float commission, float price){
        float commissionRate = commission/price;
        float priceRemainder = price*(1-commissionRate-0.05f-baseProfit/100);
        float newPrice = priceRemainder/(1-targetProfit/100-commissionRate-0.05f);
        return Math.round(newPrice-price);
    }

    }
