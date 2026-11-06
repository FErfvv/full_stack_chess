package client;

import ui.ClientGuiMap;
import ui.EscapeSequences;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class ClientUI {
    private State state = State.SIGNEDOUT;

    public void run() {
        System.out.println("Welcome to Chess. Type Help to get started.");
        Scanner scanner = new Scanner(System.in);
        while (!state.equals(State.QUIT)) {
            switch (state) {
                case SIGNEDOUT -> System.out.print("(Signed Out) >> ");
                case SIGNEDIN -> System.out.print("(Signed In) >> ");
            }

            String input = scanner.nextLine();
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (Objects.equals(cmd, "help")) {
                help();
            } else if (state == State.SIGNEDOUT) {
                handlePreLogin(cmd, params);
            } else {
                handlePostLogin(cmd, params);
            }
        }
    }

    private void handlePreLogin(String command, String[] params) {

    }

    private void handlePostLogin(String command, String[] params) {
        System.out.println(command);
    }

    private void help() {
        System.out.printf(ClientGuiMap.LoggedOutHelp());
    }
}
