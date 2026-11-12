package client;

import chess.ChessGame;
import com.google.gson.Gson;
import exceptions.BasicResponseException;
import model.ErrorResponseMessage;
import model.responses.ListGamesResponse;
import ui.ClientGuiMap;

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
            entry("help", 0),
            entry("test", 0)
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
        try {
            switch (command) {
                case "register":
                    commandHandler.register(params);
                    System.out.print(ClientGuiMap.registeredMessage(params[0]));
                    state = State.SIGNEDIN;
                    break;
                case "login":
                    commandHandler.login(params);
                    System.out.print(ClientGuiMap.loggedInMessage(params[0]));
                    state = State.SIGNEDIN;
                    break;
                case "quit":
                    System.out.print(ClientGuiMap.loggedOutMessage());
                    state = State.QUIT;
                    break;
                case "test":
                    System.out.println(ClientGuiMap.renderGame(new ChessGame(), ChessGame.TeamColor.WHITE));
                    break;
            }
        } catch (BasicResponseException e) {
            System.out.println(new Gson().fromJson(e.getMessage(), ErrorResponseMessage.class).message());
        }

    }

    private void handlePostLogin(String command, String[] params) {
        try {
            switch (command) {
                case "quit":
                    commandHandler.logout();
                    System.out.println(ClientGuiMap.loggedOutMessage());
                    state = State.QUIT;
                    break;
                case "logout":
                    commandHandler.logout();
                    state = State.SIGNEDOUT;
                    break;
                case "list":
                    ListGamesResponse gameList = commandHandler.listGames();
                    System.out.println(ClientGuiMap.gameList(gameList));
                    break;
                case "join":
                    ChessGame game = commandHandler.joinGame(params);
                    ChessGame.TeamColor teamColor = Objects.equals(params[1], "white") ?
                            ChessGame.TeamColor.WHITE :
                            ChessGame.TeamColor.BLACK;
                    System.out.println(ClientGuiMap.renderGame(game, teamColor));
                    break;
                case "observe":
                    ChessGame newGame = commandHandler.joinGame(params);
                    System.out.println(ClientGuiMap.renderGame(newGame, ChessGame.TeamColor.WHITE));
                    break;
                case "create":
                    commandHandler.createGame(params);
                    System.out.println(ClientGuiMap.gameCreated());
                    break;

            }
        } catch (BasicResponseException e) {
            System.out.println(new Gson().fromJson(e.getMessage(), ErrorResponseMessage.class).message());
        }
    }

    private void help() {
        if (state == State.SIGNEDOUT) {
            System.out.println(ClientGuiMap.loggedOutHelp());
        } else {
            System.out.println(ClientGuiMap.loggedInHelp());
        }
    }

    private boolean checkParams(String command, String[] params) {
        if (!COMMAND_LIST.containsKey(command)) {
            return false;
        }
        return COMMAND_LIST.get(command) == params.length;
    }


}
