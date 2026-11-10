package ui;

import model.responses.GameListResponseData;
import model.responses.ListGamesResponse;

public class ClientGuiMap {


    public static String LoggedOutHelp() {
        return String.format("""
                        %sregister <USERNAME> <PASSWORD> <EMAIL>%s - to create an account
                        %slogin <USERNAME> <PASSWORD>%s - to play chess
                        %squit%s - playing chess
                        %shelp%s - with possible commands%s
                        """,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.RESET_TEXT_COLOR);
    }

    public static String LoggedInHelp() {
        return String.format("""
                        %screate <NAME>%s - a game
                        %slist%s - games
                        %sjoin <ID> [WHITE|BLACK]%s - a game
                        %sobserve <ID>%s - a game
                        %slogout%s - when you are done
                        %squit%s - playing chess
                        %shelp%s - with possible commands%s
                        """,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.SET_TEXT_COLOR_GREEN,
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY,
                EscapeSequences.RESET_TEXT_COLOR);
    }

    public static String LoggedOutMessage() {
        return String.format("%sThank you for playing! Come again soon.%s\n", EscapeSequences.SET_TEXT_COLOR_GREEN, EscapeSequences.RESET_TEXT_COLOR);
    }

    public static String LoggedInMessage(String name) {
        return String.format("%sSuccessfully logged in! Welcome, %s%s\n", EscapeSequences.SET_TEXT_COLOR_GREEN, name, EscapeSequences.RESET_TEXT_COLOR);
    }

    public static String RegisteredMessage(String name) {
        return String.format("%sSuccessfully registered! Welcome, %s%s\n", EscapeSequences.SET_TEXT_COLOR_GREEN, name, EscapeSequences.RESET_TEXT_COLOR);
    }

    public static String GameList(ListGamesResponse list) {
        StringBuilder myString = new StringBuilder();
        for (int i = 1; i < list.getGames().size(); i++) {
            GameListResponseData game = list.getGames().get(i);
            String gameString = String.format("""
                    Game %s: %s
                      - White: %s
                      - Black: %s
                    """, i, game.gameName(), game.whiteUsername(), game.blackUsername());
            myString.append(gameString);
        }
        return myString.toString();
    }

}
