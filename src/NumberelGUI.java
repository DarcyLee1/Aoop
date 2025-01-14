import javax.swing.SwingUtilities;

public class NumberelGUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(NumberelGUI::createAndShowGUI);
    }

    public static void createAndShowGUI() {
        INumberleModel model = new NumberleModel();
        NumberleController controller = new NumberleController(model);
        NumberleView view = new NumberleView(model, controller);
    }

}