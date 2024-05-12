// NumberleModel.java
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Observable;

public class NumberleModel extends Observable implements INumberleModel {
    private String targetEquation;
    private StringBuilder currentGuess;
    private int remainingAttempts;
    private boolean gameWon;

    @Override
    public void initialize() {
        targetEquation = handleRandomEquation();
        currentGuess = new StringBuilder("       ");
        remainingAttempts = MAX_ATTEMPTS;
        gameWon = false;
        setChanged();
        notifyObservers();
    }

    public String randomEquation() throws IOException {
        String fileName = "equations.txt";
        String path = "src/" + fileName;
        File file = new File(path);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> equations = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            equations.add(line);
        }
        Random rand = new Random();
        return equations.get(rand.nextInt(equations.size()));
    }

    public String handleRandomEquation() {
        try {
            return randomEquation();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void processInput(String input) {
        remainingAttempts--;
        setChanged();
        notifyObservers();

        if (input.equals(targetEquation)) {
            gameWon = true;
        }
    }

    @Override
    public boolean isGameOver() {
        return remainingAttempts <= 0 || gameWon;
    }

    @Override
    public boolean isGameWon() {
        return gameWon;
    }

    @Override
    public String getTargetEquation() {
        return targetEquation;
    }

    @Override
    public StringBuilder getCurrentGuess() {
        return currentGuess;
    }

    @Override
    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    @Override
    public void startNewGame() {
        initialize();
    }

}
