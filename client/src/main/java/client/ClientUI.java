package client;

import exceptions.BasicResponseException;
import ui.ClientGuiMap;
import ui.EscapeSequences;

import java.util.*;

import static java.util.Map.entry;

public class ClientUI {
    private State state = State.SIGNEDOUT;
    private String serverUrl;
    private CommandHandler commandHandler;

    private static final Map<String, Integer> COMMAND_LIST = Map.ofEntries(
            entry("register", 3),
            entry("login", 2),
            entry("quit", 0),
            entry("create", 1),
            entry("list", 0),
            entry("join", 2),
            entry("observe", 1),
            entry("logout", 0),
            entry("help", 0)
    );

    public ClientUI(String url) {
        commandHandler = new CommandHandler(url);
    }

    public void run() throws BasicResponseException {
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
            if (!checkParams(cmd, params)) {
                System.out.println("Invalid command or paramaters, please try again");
                continue;
            }
            if (Objects.equals(cmd, "help")) {
                help();
            } else if (state == State.SIGNEDOUT) {
                handlePreLogin(cmd, params);
            } else {
                handlePostLogin(cmd, params);
            }
        }
    }

    private void handlePreLogin(String command, String[] params) throws BasicResponseException {
        switch (command) {
            case "register" -> commandHandler.register(params);
        }
    }

    private void handlePostLogin(String command, String[] params) {
        System.out.println(command);
    }

    private void help() {
        if (state == State.SIGNEDOUT) {
            System.out.println(ClientGuiMap.LoggedOutHelp());
        } else {
            System.out.println(ClientGuiMap.LoggedInHelp());
        }
    }

    private boolean checkParams(String command, String[] params) {
        if (!COMMAND_LIST.containsKey(command)) {
            return false;
        }
        return COMMAND_LIST.get(command) == params.length;
    }


}
