package client;

import java.util.Scanner;

public class ClientUI {
    private State state = State.SIGNEDOUT;

    public void run() {
        System.out.println("Welcome to Chess. Type Help to get started.");
        Scanner scanner = new Scanner(System.in);
        while (!state.equals(State.QUIT)) {
            String input = scanner.nextLine();
            if (state == State.SIGNEDOUT) {
                handlePreLogin(input);
            } else {
                handlePostLogin(input);
            }
        }
    }

    private void handlePreLogin(String input) {
        System.out.println(input);
    }

    private void handlePostLogin(String input) {
        System.out.println(input);
    }
}
