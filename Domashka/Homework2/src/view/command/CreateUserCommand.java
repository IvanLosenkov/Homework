package org.LosenkovIvan.view.command;

import lombok.Getter;
import org.LosenkovIvan.view.ConsoleUI;

public class CreateUserCommand implements Command {
    @Getter
    private final String description = "Создать пользователя";
    private final ConsoleUI consoleUI;

    public CreateUserCommand(ConsoleUI consoleUI) {
        this.consoleUI = consoleUI;
    }

    @Override
    public void execute() {
        consoleUI.createUser();
    }
}