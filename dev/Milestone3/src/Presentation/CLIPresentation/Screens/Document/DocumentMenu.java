package Presentation.CLIPresentation.Screens.Document;

import Globals.util.HumanInteraction;
import Presentation.CLIPresentation.Screens.Screen;

public class DocumentMenu extends Screen {
    private static final String[] menuOptions = {
            "Get transport document",    //1
            "Get destination document",  //2
            "Exit"                       //3
    };
    public DocumentMenu(Screen caller) {
        super(caller, menuOptions);
    }

    @Override
    public void run() {
        System.out.println("\nWelcome to the Document Management Menu!");
        int option = 0;
        while (option != 3) {
            option = runMenu();
            try {
                switch (option) {
                    case 1:
                        getTransportDocument();
                        break;
                    case 2:
                        getDestinationDocument();
                        break;
                    case 3:
                        endRun();
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again");
            }
        }
    }

    private void getDestinationDocument() {
        int ddSN = 0;
        try {
            ddSN = getSNOfDestDocument();
            //DestinationDocument dd = controller.getDestinationDocument(ddSN);
            //dd.display();

        } catch (HumanInteraction.OperationCancelledException ignore){
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void getTransportDocument() {
        int tdSN = 0;
        try {
            tdSN = getSNOfTranDocument();
            //TransportDocument td = controller.getTransportDocument(tdSN);
            //td.display();
        }catch (HumanInteraction.OperationCancelledException ignore){
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private int getSNOfDestDocument() throws HumanInteraction.OperationCancelledException {
        System.out.println("Enter order ID:");
        return HumanInteraction.getNumber(0);
    }
    private int getSNOfTranDocument() throws HumanInteraction.OperationCancelledException {
        System.out.println("Enter transport SN:");
        return HumanInteraction.getNumber(0);
    }
}
