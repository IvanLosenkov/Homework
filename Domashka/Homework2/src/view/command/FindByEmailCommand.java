package org.LosenkovIvan.view.command;

import lombok.Getter;
import org.LosenkovIvan0.view.ConsoleUI;

public class FindByEmailCommand implements Command {
    private final ConsoleUI consoleUI;

    @Getter
    private final String description = "Найти пользователя по email";

    public FindByEmailCommand(ConsoleUI consoleUI) {
        this.consoleUI = consoleUI;
    }

    @Override
    public void execute() {
        consoleUI.findByEmail();
    }
}