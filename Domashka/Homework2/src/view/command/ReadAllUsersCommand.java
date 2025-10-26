package org.LosenkovIvan.view.command;

import lombok.Getter;
import org.LosenkovIvan.view.ConsoleUI;

public class ReadAllUsersCommand implements Command {
    private final ConsoleUI consoleUI;

    @Getter
    private final String description = "Показать всех пользователей";

    public ReadAllUsersCommand(ConsoleUI consoleUI) {
        this.consoleUI = consoleUI;
    }

    @Override
    public void execute() {
        consoleUI.readAllUsers();
    }
}