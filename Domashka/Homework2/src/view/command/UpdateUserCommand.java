package org.LosenkovIvan.view.command;

import lombok.Getter;
import org.LosenkovIvan.view.ConsoleUI;

public class UpdateUserCommand implements Command {

    private final ConsoleUI consoleUI;

    @Getter
    private final String description = "Обновить пользователя";

    public UpdateUserCommand(ConsoleUI consoleUI) {
        this.consoleUI = consoleUI;
    }

    @Override
    public void execute() {
        consoleUI.updateUser();
    }
}