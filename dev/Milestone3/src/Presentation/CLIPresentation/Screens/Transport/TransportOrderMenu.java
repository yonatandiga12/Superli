package Presentation.CLIPresentation.Screens.Transport;

import Globals.util.HumanInteraction;
import Presentation.CLIPresentation.Objects.Transport.TransportOrder;
import Presentation.CLIPresentation.Screens.Screen;

public class TransportOrderMenu extends Screen {
    private static final String[] menuOptions = {
            "Add product",               //1
            "Remove product",            //2
            "Update product quantity",   //3
            "Close order",               //4
            "Exit"                       //5
    };

    public TransportOrderMenu(Screen caller) {
        super(caller, menuOptions);
    }

    @Override
    public void run() {
        //TODO remove after update
        System.out.println("\nCreate Transport Order: not access in this point");
        /*int srcID = 0;
        int dstID = 0;
        try {
            srcID = getSiteID("source");
            dstID = getSiteID("destination");
        }catch (HumanInteraction.OperationCancelledException e) {
            endRun();
            return;
        }
        TransportOrder to = new TransportOrder(srcID, dstID);
        int option = 0;
        while (option != 4 && option != 5) {
            option = runMenu();
            try {
                switch (option) {
                    case 1:
                        addProduct(to);
                        break;
                    case 2:
                        removeProduct(to);
                        break;
                    case 3:
                        updateProduct(to);
                        break;
                    case 4:
                        try{
                            closeOrder(to);
                            endRun();
                        }
                        catch (Exception e){
                            option = 0;
                            throw e;
                        }
                        break;
                    case 5:
                        endRun();
                        break;
                }
            } catch (HumanInteraction.OperationCancelledException ignore){
            }catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again");
            }
        }*/
    }

    private void closeOrder(TransportOrder to) throws Exception {
        to.canCloseOrder();
        controller.addTransportOrder(to.getSrcID(), to.getDstID(), to.getProductList());
    }

    private void updateProduct(TransportOrder to) throws Exception {
        int productSN = getSerialNumber();
        int productQuantity = getProductQuantity();
        to.updateProduct(productSN, productQuantity);
    }

    private void removeProduct(TransportOrder to) throws Exception {
        int productSN = getSerialNumber();
        to.removeProduct(productSN);
    }

    private void addProduct(TransportOrder to) throws Exception {
        int productSN = getSerialNumber();
        int productQuantity = getProductQuantity();
        to.addProduct(productSN, productQuantity);
    }

    private int getSiteID(String siteType) throws HumanInteraction.OperationCancelledException {
        System.out.println("Enter " + siteType + " ID:");
        return HumanInteraction.getNumber(0);
    }
    private int getSerialNumber() throws HumanInteraction.OperationCancelledException {
        System.out.println("Enter product serial number:");
        return HumanInteraction.getNumber(0);
    }
    private int getProductQuantity() throws HumanInteraction.OperationCancelledException {
        System.out.println("Enter product quantity:");
        return HumanInteraction.getNumber(0);
    }
    
}
