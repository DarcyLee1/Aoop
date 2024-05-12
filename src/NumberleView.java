// NumberleView.java

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class NumberleView implements Observer {

    private static final Integer SIZE = 278;
    private static NumberleController controller;
    private static String inputTextField = "";
    private final JLabel targetEquation = new JLabel("Target Equation: ");
    private final JLabel attemptsLabel = new JLabel("Attempts remaining: ");
    private static final JButton submitButton = new JButton("Enter");
    private static final JButton restartButton = new JButton("Restart");
    private static final List<List<Input>> inputList = new ArrayList<>();
    private static Integer inputIndex = 0;
    private static Integer inputRowIndex = 0;
    private static final Map<String, JPanel> bottomList = new HashMap<>();

    private static final JButton tooShort = new JButton("Short");
    private static final JButton mastHaveEqual = new JButton("Must Have Equal");
    private static final JButton mastHaveOperator = new JButton("Must Have Operator");
    private static final JButton mastHaveNumber = new JButton("Must Have Number");
    private static final JButton invalidEquation = new JButton("Invalid Equation");
    private static final JButton notEqual = new JButton("Not Equal");

    public static final class Input {
        private JPanel jPanel;
        private JLabel jLabel;

        public Input() {
        }

        public Input(JPanel jPanel, JLabel jLabel) {
            this.jPanel = jPanel;
            this.jLabel = jLabel;
        }

        public JPanel getjPanel() {
            return jPanel;
        }

        public void setjPanel(JPanel jPanel) {
            this.jPanel = jPanel;
        }

        public JLabel getjLabel() {
            return jLabel;
        }

        public void setjLabel(JLabel jLabel) {
            this.jLabel = jLabel;
        }
    }

    public NumberleView(INumberleModel model, NumberleController controller) {
        NumberleView.controller = controller;
        NumberleView.controller.startNewGame();
        ((NumberleModel) model).addObserver(this);
        initializeFrame();
        NumberleView.controller.setView(this);
        update((NumberleModel) model, null);


        targetEquation.setFont(new Font("Arial", Font.PLAIN, 14));
        attemptsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    public void initializeFrame() {
        final JFrame frame = new JFrame("Numberle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(4 * SIZE, 3 * SIZE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - frame.getWidth()) / 2;
        int y = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(x, y);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
        frame.addKeyListener(new KeyboardListener());
        submitButton.addActionListener(action -> enterActionListener(frame));
        restartButton.addActionListener(action -> handleRestart(frame));
        String[] options = {"OK"};
        tooShort.addActionListener(action -> {
            JOptionPane.showOptionDialog(
                frame,
                "Too short.",
                "Warning",
                JOptionPane.YES_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[0]
            );
        });
        mastHaveEqual.addActionListener(action -> {
            JOptionPane.showOptionDialog(
                frame,
                "Must have equal '=' sign.",
                "Warning",
                JOptionPane.YES_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[0]
            );
        });
        mastHaveOperator.addActionListener(action -> {
            JOptionPane.showOptionDialog(
                frame,
                "Must have operator(+、-、*、/).",
                "Warning",
                JOptionPane.YES_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[0]
            );
        });
        mastHaveNumber.addActionListener(action -> {
            JOptionPane.showOptionDialog(
                frame,
                "Must have number(0-9).",
                "Warning",
                JOptionPane.YES_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[0]
            );
        });
        invalidEquation.addActionListener(action -> {
            JOptionPane.showOptionDialog(
                frame,
                "Invalid equation. e.g. 10+2=12",
                "Warning",
                JOptionPane.YES_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[0]
            );
        });
        notEqual.addActionListener(action -> {
            JOptionPane.showOptionDialog(
                frame,
                "The left side is not equal to the right side.",
                "Warning",
                JOptionPane.YES_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[0]
            );
        });


        JPanel topPanel = new JPanel();
        JPanel bottomPanel = new JPanel();


        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 0.7;

        gbc.insets = new Insets(6, 6, 6, 6);
        frame.add(topPanel, gbc);


        gbc.gridy = 1;
        gbc.weighty = 0.3;
        JPanel buttonPanel1 = new JPanel();
        JPanel buttonPanel2 = new JPanel();
        buttonPanel1.setLayout(new GridLayout(1, 10, 10, 10));
        buttonPanel2.setLayout(new GridLayout(1, 7, 10, 10));

        final int o = 2;
        for (int i = 1; i <= 10; i++) {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setPreferredSize(new Dimension(50 * o, 50 * o));
            String value = i == 10 ? "0" : String.valueOf(i);
            JLabel label = new JLabel(value);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            panel.setBackground(new Color(189,186,189));
            panel.add(label, BorderLayout.CENTER);
            bottomList.put(value, panel);
            buttonPanel1.add(panel);
        }

        String[] operations = {"delete", "+", "-", "*", "/", "=", "enter"};
        for (String operation : operations) {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            if (operation.equals("delete") || operation.equals("enter"))
                panel.setPreferredSize(new Dimension(75 * o, 50 * o));
            else {
                panel.setPreferredSize(new Dimension(70 * o, 50 * o));
            }
            JLabel label = new JLabel(operation);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            panel.setBackground(new Color(189,186,189));
            panel.add(label, BorderLayout.CENTER);
            buttonPanel2.add(panel);
            bottomList.put(operation, panel);
        }

        bottomPanel.add(buttonPanel1);
        bottomPanel.add(buttonPanel2);

        frame.add(bottomPanel, gbc);


        JPanel leftTopPanel = new JPanel();

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS)); // 设置布局为BoxLayout，垂直排列


        labelPanel.add(targetEquation);
        labelPanel.add(Box.createVerticalStrut(10));
        labelPanel.add(attemptsLabel);


        leftTopPanel.add(labelPanel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.15;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        topPanel.setLayout(new GridBagLayout());
        topPanel.add(leftTopPanel, gbc);


        JPanel centerTopPanel = new JPanel();

        GridLayout gridLayout = new GridLayout(6, 7, 10, 10);


        centerTopPanel.setLayout(gridLayout);


        for (int i = 0; i < 42; i++) {
            JPanel cell = new JPanel();
            cell.setPreferredSize(new Dimension(40, 40));
            cell.setBackground(new Color(250, 250, 250));

            cell.setLayout(new BorderLayout());
            JLabel label = new JLabel(" ");
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
            cell.add(label, BorderLayout.CENTER);

            if (inputList.size() != inputIndex + 1) {
                inputList.add(new ArrayList<>());
            }
            inputList.get(inputIndex).add(new Input(cell, label));

            if (inputList.get(inputIndex).size() == 7) {
                inputIndex++;
            }

            centerTopPanel.add(cell);
        }
        inputIndex = 0;
        inputRowIndex = 0;

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        topPanel.add(centerTopPanel, gbc);


        JPanel rightTopPanel = new JPanel();
        JButton restart = new JButton("Restart");
        restart.addActionListener(action -> handleRestart(frame));
        rightTopPanel.add(restart);

        gbc.gridx = 2;
        gbc.weightx = 0.15;
        topPanel.add(rightTopPanel, gbc);

//        frame.pack();
        frame.setVisible(true);
    }

    public void enterActionListener(JFrame frame) {
        String[] options = {"Restart", "Cancel"};

        if (controller.isGameWon()) {
            int wonResult = JOptionPane.showOptionDialog(
                frame,
                "Congratulations! You've won the game.",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
            );
            if (wonResult == 0) {
                restartButton.doClick();
            }
            return;
        }

        if (controller.isGameOver()) {
            int gameOverResult = JOptionPane.showOptionDialog(
                frame,
                "Game Over. The correct number was: " + controller.getTargetEquation(),
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[0]
            );
            if (gameOverResult == 0) {
                restartButton.doClick();
            }
            return;
        }
        controller.processInput(inputTextField);
        inputTextField = "";
        if (controller.isGameWon()) {
            int wonResult = JOptionPane.showOptionDialog(
                frame,
                "Congratulations! You've won the game.",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
            );
            if (wonResult == 0) {
                restartButton.doClick();
            }
            return;
        }
        if (controller.isGameOver()) {
            int gameOverResult = JOptionPane.showOptionDialog(
                frame,
                "Game Over. The correct number was: " + controller.getTargetEquation(),
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[0]
            );
            if (gameOverResult == 0) {
                restartButton.doClick();
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        targetEquation.setText("Target Equation: " + controller.getTargetEquation());
        attemptsLabel.setText("Attempts remaining: " + controller.getRemainingAttempts());
    }

    public static void handleRestart(JFrame frame) {
        inputIndex = 0;
        inputRowIndex = 0;
        inputTextField = "";
        frame.requestFocusInWindow();
        controller.startNewGame();
        inputList.forEach(inputs -> inputs.forEach(input -> input.getjLabel().setText(" ")));
        inputList.forEach(it -> it.forEach(input -> input.getjPanel().setBackground(new Color(250, 250, 250))));
        bottomList.forEach((key, value) -> value.setBackground(new Color(189,186,189)));
    }

    public static void handleInput(KeyEvent e) {
        if (inputList.get(0).get(0).getjLabel().getText().equals(" ")) {
            inputIndex = 0;
        }
        if (inputRowIndex == 7 && inputIndex == 6) {
            return;
        }
        if (inputRowIndex < 7) {
            inputList.get(inputIndex).get(inputRowIndex).getjLabel().setText(String.valueOf(e.getKeyChar()));
            inputRowIndex++;
        }
    }

    public static void handleEnter(KeyEvent e) {
        if (inputRowIndex != 7) {
            tooShort.doClick();
            return;
        }

        final String input = inputList.get(inputIndex).stream()
                .map(input1 -> input1.getjLabel().getText())
                .reduce("", String::concat);

        if (!input.contains("=")) {
            mastHaveEqual.doClick();
            return;
        } else if (!input.matches(".*[+\\-*/].*")) {
            mastHaveOperator.doClick();
            return;
        } else if (!input.matches(".*[0-9].*")) {
            mastHaveNumber.doClick();
            return;
            // 校验表达式是否合法
        } else if (!isValidEquation(input)) {
            invalidEquation.doClick();
            return;
        }

        String[] split = input.split("=");
        String left = split[0];
        String right = split[1];
        if (!left.contains("+") && !left.contains("-") && !left.contains("*") && !left.contains("/")) {
            left = split[1];
            right = split[0];
        }

        final char[] targetEquationChars = controller.getTargetEquation().toCharArray();
        for (int i = 0; i < targetEquationChars.length; i++) {
            char equationChar = targetEquationChars[i];
            if (inputList.get(inputIndex).get(i).getjLabel().getText().charAt(0) == equationChar) {
                inputList.get(inputIndex).get(i).getjPanel().setBackground(Color.GREEN);
                bottomList.get(targetEquationChars[i] + "").setBackground(Color.GREEN);
            } else {
                boolean isExist = false;
                for (char targetEquationChar : targetEquationChars) {
                    if (inputList.get(inputIndex).get(i).getjLabel().getText().charAt(0) == targetEquationChar) {
                        isExist = true;
                        break;
                    }
                }
                if (isExist) {
                    inputList.get(inputIndex).get(i).getjPanel().setBackground(Color.ORANGE);
                    String text = inputList.get(inputIndex).get(i).getjLabel().getText();
                    Color background = bottomList.get(text).getBackground();
                    if (!background.equals(Color.GREEN)) {
                        bottomList.get(text).setBackground(Color.ORANGE);
                    }
                } else {
                    inputList.get(inputIndex).get(i).getjPanel().setBackground(Color.GRAY);
                    String text = inputList.get(inputIndex).get(i).getjLabel().getText();
                    bottomList.get(text).setBackground(Color.GRAY);
                }
            }

        }

        inputTextField = inputList.get(inputIndex).stream().map(input1 -> input1.getjLabel().getText()).reduce("", String::concat);
        submitButton.doClick();

        if (!controller.isGameWon() || !controller.isGameOver()) {
            inputIndex++;
            inputRowIndex = 0;
        }
    }

    private static boolean isValidEquation(String input) {
        String operator = "+-*/=";
        if (operator.contains(input.charAt(0) + "") || operator.contains(input.charAt(input.length() - 1) + "")) {
            return false;
        }
        if (!input.contains("=")) {
            return false;
        }
        int count = 0;
        for (int i = 0; i < input.length(); i++) {
            if (operator.contains(input.charAt(i) + "")) {
                count++;
            } else {
                count = 0;
            }
            if (count > 1) {
                return false;
            }
        }
        return true;
    }



    public static final class KeyboardListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9') {
                handleInput(e);
            } else if (e.getKeyChar() == '-') {
                handleInput(e);
            } else if (e.getKeyChar() == '+') {
                handleInput(e);
            } else if (e.getKeyChar() == '*') {
                handleInput(e);
            } else if (e.getKeyChar() == '/') {
                handleInput(e);
            } else if (e.getKeyChar() == '=') {
                handleInput(e);
            } else if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                handleEnter(e);
            } else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                if (inputRowIndex == 0) {
                    return;
                }
                inputList.get(inputIndex).get(inputRowIndex - 1).getjLabel().setText(" ");
                inputRowIndex--;
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

    }

}
