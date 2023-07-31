import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        Manager manager = new Manager();
        WBTable newTableA = manager.readPnList("csv/pnalist.csv");
        newTableA.setInfo("А");
        WBTable newTableJ = manager.readPnList("csv/pnjlist.csv");
        newTableJ.setInfo("Ж");
        WBTable newTableV = manager.readPnList("csv/pnvlist.csv");
        newTableV.setInfo("В");


        manager.processCampaignsAndPrepareChangesList(newTableA);
        manager.processCampaignsAndPrepareChangesList(newTableJ);
        manager.processCampaignsAndPrepareChangesList(newTableV);

        //manager.processPricesAndPrepareChangesList(newTableJ);
        //manager.processPricesAndPrepareChangesList(newTableA);

        }

    }