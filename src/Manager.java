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
        ArrayList<String> out = new ArrayList<>();
        ArrayList<String> outEnd = new ArrayList<>();
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
                    out.add(table.getValueByColumnIndex(i, 0));
                    continue;
                }
                if (remainderTotal < 50){
                    if (table.illiquid.contains(itemGroup) && remainderInWaiting == 0){ // неликвид с малым остатком
                        outEnd.add(itemID);//список для закрытия
                        out.add(itemID);//добавляем тк табличка считает разницу между списками
                    } else {
                        out.add(itemID); //список для приостановки
                    }
                }
            }
        }
        Utils.writeArrayListToFile(out,table.outPath);
        Utils.writeArrayListToFile(outEnd,table.outEndPath);
        System.out.println("Скрипт выполнен");
    }


}
