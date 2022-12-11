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

    public void writeCampaignsToStopAndEnd(WBTable table) throws IOException {
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
        Utils.writeArrayListToFile(stopCampaigns,table.campaignsPath);
        Utils.writeArrayListToFile(endCampaigns,table.campaignsEndPath);
        System.out.println("Скрипт выполнен");
    }

    public void analyzePrices(WBTable table) {
        ArrayList<String> priceChanges = new ArrayList<>();
        int turnoverWBIndex = table.turnoverIndex + 1;
        int currentProfitIndex = 8; //мэджик намба но оно экшели редко меняется, потом прикручу проверку
        int remainderIndex = table.remainderIndex;
        for (int i = 1; i < table.data.length; i++) {
            if (!table.getValueByColumnIndex(i, turnoverWBIndex).equals("")) {
                int turnoverWB = table.getIntegerValueByColumn(i,turnoverWBIndex);
                int remainderWB = table.getIntegerValueByColumn(i,remainderIndex);
                int remainderInWaiting = table.getIntegerValueByColumn(i,remainderIndex+2);
                int remainderInStock = table.getIntegerValueByColumn(i,remainderIndex+3);
                String itemGroup = table.getValueByColumnIndex(i,2);
                String itemID = table.getValueByColumnIndex(i, 0);

                if (turnoverWB <= 15){
                    if (table.activeGroups.contains(itemGroup)) {
                        if (remainderInStock > 100) {

                        }
                    }
                }

            }
        }

    }

    }
