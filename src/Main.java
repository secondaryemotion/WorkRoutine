import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        Manager manager = new Manager();
        WBTable newTableA = manager.readPnAList();
        WBTable newTableJ = manager.readPnJList();

        Utils.indexCheck(newTableA);
        Utils.indexCheck(newTableJ);


        manager.writeCampaignsToStopAndEnd(newTableA);
        manager.writeCampaignsToStopAndEnd(newTableJ);

        }

    }