import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        Manager manager = new Manager();
        WBTable newTableA = manager.readPnAList();
        WBTable newTableJ = manager.readPnJList();

        //Utils.indexCheck(newTableA);
        //Utils.indexCheck(newTableJ);


        //manager.processCampaignsAndPrepareChangesList(newTableA, true);
        //manager.processCampaignsAndPrepareChangesList(newTableJ,true);
        //System.out.println(newTableA.paths.get("priceChanges"));
        manager.processPricesAndPrepareChangesList(newTableA);

        }

    }