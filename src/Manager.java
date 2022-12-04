import java.io.IOException;
import java.util.ArrayList;

public class Manager {

    public WBTable readPnAList(){
        String pnaPath = "csv/pnalist.csv";
        String pnaFile = Utils.readFile(pnaPath);
        String[] tableLines = pnaFile.split("\n");
        WBTable PnAList = new WBTable();
        PnAList.createTable(tableLines);
        return PnAList;
    }

    public void writeCampaignsToStop(WBTable table) throws IOException {
        ArrayList<String> out = new ArrayList<>();
        int turnoverIndex = Utils.askForColumnNumber("оборачиваемость всех",table.header);
        int remainderIndex = Utils.askForColumnNumber("остаток вб",table.header);
        for (int i = 1; i < table.data.length; i++){
            if (!table.getValueByColumnIndex(i,turnoverIndex).equals("")) {
                int turnoverRate = Integer.parseInt(table.getValueByColumnIndex(i, turnoverIndex));
                if (turnoverRate < 25) {
                    out.add(table.getValueByColumnIndex(i, 0));
                }
                switch (table.getValueByColumnIndex(i,2)){
                    case ("Актив/Осень / Сезонные"): {}
                    case ("Актив/Весна - Лето (март-октябрь)"): {}
                    case ("Актив/Актив / Внесезонные"): {}
                    case ("Актив"): {
                        int remainder = table.getIntegerValueByColumn(i,remainderIndex)+
                                table.getIntegerValueByColumn(i, remainderIndex+1)+
                                table.getIntegerValueByColumn(i, remainderIndex+2)+
                                table.getIntegerValueByColumn(i, remainderIndex+3);
                        if (remainder < 50 && turnoverRate > 25){
                            out.add(table.getValueByColumnIndex(i, 0)+"check");
                        }
                    }


                }
            }

        }
        Utils.writeArrayListToFile(out,"output/stopCampaignsA.txt");

    }


}
