import java.util.Set;

public class WBTable {
    public String[] header;
    public String[][] data;
    public int turnoverIndex;
    public int remainderIndex;
    public Set<String> activeGroups;
    public Set<String> illiquidGroups;
    public String illiquid = "Неликвид";
    public String newGroups = "Актив/Новинки";
    public String outPath;
    public String outEndPath;

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
            this.turnoverIndex = 32;
            this.remainderIndex = 25;
            this.activeGroups = Set.of("Актив/Актив / Внесезонные","Актив/Проблемы с поставками / Дефицит",
            "Актив/Спрос с сентября / Внесезонные","Актив","Актив/Весна - Лето (март-октябрь)",
            "Актив/Осень / Сезонные","Актив/Новый год / Сезонные");
            this.illiquidGroups = Set.of("Неликвид/Весна - лето / Неликвид","Неликвид/Март / Сезонные");
            this.outPath = "output/stopCampaignsA.txt";
            this.outEndPath = "output/endCampaignsA.txt";
        } else if (ip.equals("Ж")){
            this.turnoverIndex = 33;
            this.remainderIndex = 26;
            this.activeGroups = Set.of("Актив/Актив / Внесезонные","Актив/Проблемы с поставками / Дефицит",
                    "Актив/Спрос с сентября / Внесезонные","Актив","Актив/Весна - Лето (март-октябрь)",
                    "Актив/Осень / Сезонные","Актив/Новый год / Сезонные","Актив/Зима / Сезонные");
            this.illiquidGroups = Set.of("Неликвид/Весна - лето / Неликвид","Неликвид/Март / Сезонные",
            "Неликвид/Май / Сезонные (Пасха, 9 мая)","Неликвид/Лето (только лето) / Сезонные");
            this.outPath = "output/stopCampaignsJ.txt";
            this.outEndPath = "output/endCampaignsJ.txt";
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



}
