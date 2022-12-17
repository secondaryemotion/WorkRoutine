import java.util.HashMap;
import java.util.Set;

public class WBTable {
    public String[] header;
    public String[][] data;
    public int turnoverIndex;
    public int remainderIndex;
    public int commissionIndex;
    public Set<String> activeGroups;
    public Set<String> illiquidGroups;
    public final String illiquid = "Неликвид";
    public final String newGroup = "Актив/Новинки";
    public HashMap<String,String> paths = new HashMap<>();

    public void createTable(String[] tableLines) {
        this.header = tableLines[0].split(",");
        this.data = new String[tableLines.length][this.header.length];
        for (int i = 1; i < tableLines.length; i++) {
            this.data[i] = tableLines[i].split(",");
        }
    }

    public String printLineByIndex(Integer index){
        String out = "";
        for (int i = 0; i < this.header.length; i++) {
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
            this.turnoverIndex = 30;
            this.remainderIndex = 26;
            this.commissionIndex = 50;
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
            this.turnoverIndex = 33;
            this.remainderIndex = 26;
            this.commissionIndex = 51;
            this.activeGroups = Set.of("Актив/Актив / Внесезонные","Актив/Проблемы с поставками / Дефицит",
                    "Актив/Спрос с сентября / Внесезонные","Актив","Актив/Весна - Лето (март-октябрь)",
                    "Актив/Осень / Сезонные","Актив/Новый год / Сезонные","Актив/Зима / Сезонные");
            this.illiquidGroups = Set.of("Неликвид/Весна - лето / Неликвид","Неликвид/Март / Сезонные",
            "Неликвид/Май / Сезонные (Пасха, 9 мая)","Неликвид/Лето (только лето) / Сезонные");
            this.paths.put("campaignsPath","output/campaigns/stopCampaignsJ.txt");
            this.paths.put("campaignsEndPath","output/campaigns/endCampaignsJ.txt");
            this.paths.put("pricesToChangePath","output/pricesA/outChangesJ.csv");
            } else {
            System.out.println("something went wrong");
        }
    }

    public String turnoverIndexCheck(){
        return this.header[this.turnoverIndex];
    }

    public String remainderIndexCheck(){
        return this.header[this.remainderIndex];
    }

    public String commissionIndexCheck(){
        return this.header[this.commissionIndex];
    }



}
