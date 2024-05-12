public interface INumberleModel {
    int MAX_ATTEMPTS = 6;

    void initialize();
    void processInput(String input);
    boolean isGameOver();
    boolean isGameWon();
    String getTargetEquation();
    StringBuilder getCurrentGuess();
    int getRemainingAttempts();
    void startNewGame();
}