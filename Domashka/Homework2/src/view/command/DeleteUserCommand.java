package org.LosenkovIvan.view.command;

import lombok.Getter;
import org.LosenkovIvan.view.ConsoleUI;

public class DeleteUserCommand implements Command {
    private final ConsoleUI consoleUI;

    @Getter
    private final String description = "Удалить пользователя";

    public DeleteUserCommand(ConsoleUI consoleUI) {
        this.consoleUI = consoleUI;
    }

    @Override
    public void execute() {
        consoleUI.deleteUser();
    }
}