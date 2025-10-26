package org.LosenkovIvan.view.command;

import lombok.Getter;
import org.LosenkovIvan.view.ConsoleUI;

public class ReadUserCommand implements Command {
    @Getter
    private final String description = "Найти пользователя по ID";

    private  final ConsoleUI consoleUI;

    public ReadUserCommand(ConsoleUI consoleUI) {
        this.consoleUI = consoleUI;
    }

    @Override
    public void execute() {
        consoleUI.readUser();
    }
}