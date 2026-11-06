package ui;

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

}
