import java.util.*;

public class WBTable {
    public List<String> header;
    public String[][] data;
    public Set<String> activeGroups;
    public Set<String> illiquidGroups;
    public final String illiquid = "Неликвид";
    public final String newGroup = "Актив/Новинки";
    public HashMap<String,String> paths = new HashMap<>();
    public HashMap<String,Integer> indexes = new HashMap<>();

    public void createTable(String[] tableLines) {
        this.header = Arrays.asList(tableLines[0].split(","));
        this.data = new String[tableLines.length][this.header.size()];
        for (int i = 1; i < tableLines.length; i++) {
            this.data[i] = tableLines[i].split(",");
        }
    }

    public String printLineByIndex(Integer index){
        String out = "";
        for (int i = 0; i < this.header.size(); i++) {
            out += (" " + this.data[index][i]);
        }
        return out;
    }

    public String getValueByColumnIndex(Integer rowIndex, Integer columnIndex){
        return this.data[rowIndex][columnIndex];
    }

    public Integer getIntegerValueByColumn(Integer rowIndex, Integer columnIndex){
        return Utils.parseIntForNA(this.getValueByColumnIndex(rowIndex, columnIndex));
    }

    public void setInfo(String ip){
        if (ip.equals("А")){
            this.activeGroups = Set.of("Актив/Актив / Внесезонные","Актив/Проблемы с поставками / Дефицит",
            "Актив/Спрос с сентября / Внесезонные","Актив","Актив/Весна - Лето (март-октябрь)",
            "Актив/Осень / Сезонные","Актив/Новый год / Сезонные","Актив/Не оформлено");
            this.illiquidGroups = Set.of("Неликвид/Весна - лето / Неликвид","Неликвид/Март / Сезонные");
            this.paths.put("campaignsPath","output/campaigns/stopCampaignsA.txt");
            this.paths.put("campaignsEndPath","output/campaigns/endCampaignsA.txt");

        } else if (ip.equals("Ж")){
            this.activeGroups = Set.of("Актив/Актив / Внесезонные","Актив/Проблемы с поставками / Дефицит",
                    "Актив/Спрос с сентября / Внесезонные","Актив","Актив/Весна - Лето (март-октябрь)",
                    "Актив/Осень / Сезонные","Актив/Новый год / Сезонные","Актив/Зима / Сезонные","Актив/Не оформлено");
            this.illiquidGroups = Set.of("Неликвид/Весна - лето / Неликвид","Неликвид/Март / Сезонные",
            "Неликвид/Май / Сезонные (Пасха 9 мая)");
            this.paths.put("campaignsPath","output/campaigns/stopCampaignsJ.txt");
            this.paths.put("campaignsEndPath","output/campaigns/endCampaignsJ.txt");
            } else if (ip.equals("В")){
            this.activeGroups = Set.of("Актив/Актив / Внесезонные","Актив/Проблемы с поставками / Дефицит",
                    "Актив/Спрос с сентября / Внесезонные","Актив","Актив/Весна - Лето (март-октябрь)",
                    "Актив/Осень / Сезонные","Актив/Новый год / Сезонные","Актив/Зима / Сезонные","Актив/Не оформлено");
            this.illiquidGroups = Set.of("Неликвид/Весна - лето / Неликвид","Неликвид/Март / Сезонные",
                    "Неликвид/Май / Сезонные (Пасха 9 мая)");
            this.paths.put("campaignsPath","output/campaigns/stopCampaignsV.txt");
            this.paths.put("campaignsEndPath","output/campaigns/endCampaignsV.txt");
        }
        this.setIndexes();
    }

    public void setIndexes(){

        this.indexes.put(Fields.turnoverWBName,this.header.indexOf(Fields.turnoverWBName));
        this.indexes.put(Fields.currentProfitName,this.header.indexOf(Fields.currentProfitName));
        this.indexes.put(Fields.currentPriceName,this.header.indexOf(Fields.currentPriceName));
        this.indexes.put(Fields.currentCommissionName,this.header.indexOf(Fields.currentCommissionName));
        this.indexes.put(Fields.remainderWBName,this.header.indexOf(Fields.remainderWBName));
        this.indexes.put(Fields.remainderInWaitingName,this.header.indexOf(Fields.remainderInWaitingName));
        this.indexes.put(Fields.remainderInStockName,this.header.indexOf(Fields.remainderInStockName));
        this.indexes.put(Fields.itemGroupName,this.header.indexOf(Fields.itemGroupName));
        this.indexes.put(Fields.itemIDName,this.header.indexOf(Fields.itemIDName));
        this.indexes.put(Fields.saleProfitName,this.header.indexOf(Fields.saleProfitName));
        this.indexes.put(Fields.turnoverAllName,this.header.indexOf(Fields.turnoverAllName));
        this.indexes.put(Fields.salePriceName,this.header.indexOf(Fields.salePriceName));
    }

    public int getColumnIndex(String columnName){
        try {
            return this.indexes.get(columnName);
        } catch (Exception e) {
            System.out.println("wrong column name");
            return 0;
        }
    }

    public String[] getUnitByIndex(int index){
        return this.data[index];
    }


}
