import java.util.Scanner;

public class NumberleCLI {

    public static void main(String[] args) {
        NumberleModel model = new NumberleModel();
        NumberleController controller = new NumberleController(model);
        Scanner scanner = new Scanner(System.in);

        controller.startNewGame();
        System.out.println("Welcome to Numberel Game!");
        System.out.println("Target Equation: " + controller.getTargetEquation());

        while (!controller.isGameOver()) {
            System.out.println("Remaining Attempts: " + controller.getRemainingAttempts());

            System.out.print("Enter your guess: ");
            String input = scanner.nextLine();

            controller.processInput(input);

            if (controller.isGameWon()) {
                System.out.println("Congratulations! You've won the game.");
            } else if (controller.isGameOver()) {
                System.out.println("Game Over. The correct number was: " + controller.getTargetEquation());
            } else {
                System.out.println("Incorrect guess. Try again.");
            }
            if (controller.isGameWon() || controller.isGameOver()) {
                System.out.print("Do you want to play again? (Y/N): ");
                String playAgain = scanner.nextLine();
                if (playAgain.equalsIgnoreCase("Y")) {
                    controller.startNewGame();
                    System.out.println("Target Equation: " + controller.getTargetEquation());
                } else {
                    System.out.println("Thank you for playing Numberel Game!");
                }
            }
        }

        scanner.close();
    }

}
