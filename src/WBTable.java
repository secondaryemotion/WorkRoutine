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
            "Актив/Осень / Сезонные","Актив/Новый год / Сезонные");
            this.illiquidGroups = Set.of("Неликвид/Весна - лето / Неликвид","Неликвид/Март / Сезонные");
            this.paths.put("campaignsPath","output/campaigns/stopCampaignsA.txt");
            this.paths.put("campaignsEndPath","output/campaigns/endCampaignsA.txt");
            this.paths.put("pricesToChangePath","output/pricesA/outChangesA.txt");
            this.paths.put("idsToAddToActivePath","output/pricesA/outAddToActiveA.txt");
            this.paths.put("idsToAddToNewPath","output/pricesA/outAddToNewA.txt");
            this.paths.put("idsToAddToIlliquidPath","output/pricesA/outAddToIlliquidA.txt");
            this.paths.put("idsToOrderPath","output/pricesA/outOrderA.txt");
            this.paths.put("idsToShipPath","output/pricesA/outShipA.txt");

        } else if (ip.equals("Ж")){
            this.activeGroups = Set.of("Актив/Актив / Внесезонные","Актив/Проблемы с поставками / Дефицит",
                    "Актив/Спрос с сентября / Внесезонные","Актив","Актив/Весна - Лето (март-октябрь)",
                    "Актив/Осень / Сезонные","Актив/Новый год / Сезонные","Актив/Зима / Сезонные");
            this.illiquidGroups = Set.of("Неликвид/Весна - лето / Неликвид","Неликвид/Март / Сезонные",
            "Неликвид/Май / Сезонные (Пасха 9 мая)","Неликвид/Лето (только лето) / Сезонные");
            this.paths.put("campaignsPath","output/campaigns/stopCampaignsJ.txt");
            this.paths.put("campaignsEndPath","output/campaigns/endCampaignsJ.txt");
            this.paths.put("pricesToChangePath","output/pricesJ/outChangesJ.txt");
            this.paths.put("idsToAddToActivePath","output/pricesJ/outAddToActiveJ.txt");
            this.paths.put("idsToAddToNewPath","output/pricesJ/outAddToNewJ.txt");
            this.paths.put("idsToAddToIlliquidPath","output/pricesJ/outAddToIlliquidJ.txt");
            this.paths.put("idsToOrderPath","output/pricesJ/outOrderJ.txt");
            this.paths.put("idsToShipPath","output/pricesJ/outShipJ.txt");
            } else {
            System.out.println("something went wrong");
        }
        this.setIndexes();
    }

    public void setIndexes(){
        String turnoverWBName = "Обор посл день ВБ";
        String currentProfitName = "Рентабельность";
        String currentPriceName = "Стоимость";
        String commissionName = "Новая комиссия";
        String remainderWBName = "Склад WB";
        String remainderInWaitingName = "Ожидание";
        String remainderInStockName = "Мойсклад";
        String itemGroupName = "Группа";
        String itemIDName = "Номенклатура";
        String saleProfitName = "Рентабельность с акцией -5";
        String turnoverAllName = "Обор посл день (весь)";
        this.indexes.put(turnoverWBName,this.header.indexOf(turnoverWBName));
        this.indexes.put(currentProfitName,this.header.indexOf(currentProfitName));
        this.indexes.put(currentPriceName,this.header.indexOf(currentPriceName));
        this.indexes.put(commissionName,this.header.indexOf(commissionName));
        this.indexes.put(remainderWBName,this.header.indexOf(remainderWBName));
        this.indexes.put(remainderInWaitingName,this.header.indexOf(remainderInWaitingName));
        this.indexes.put(remainderInStockName,this.header.indexOf(remainderInStockName));
        this.indexes.put(itemGroupName,this.header.indexOf(itemGroupName));
        this.indexes.put(itemIDName,this.header.indexOf(itemIDName));
        this.indexes.put(saleProfitName,this.header.indexOf(saleProfitName));
        this.indexes.put(turnoverAllName,this.header.indexOf(turnoverAllName));
    }

    public int getColumnIndex(String columnName){
        try {
            return this.indexes.get(columnName);
        } catch (Exception e) {
            System.out.println("wrong column name");
            return 0;
        }
    }


}
