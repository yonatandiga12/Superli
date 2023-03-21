package Presentation.CLIPresentation;
import Presentation.BackendController;
import Presentation.CLIPresentation.Screens.MainMenu;


public class Main {
    public static void main(String[] args){
        new Thread(new MainMenu(new BackendController())).start();
    }

}
