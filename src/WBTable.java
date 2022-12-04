public class WBTable {
    public String[] header;
    public String[][] data;

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
}
