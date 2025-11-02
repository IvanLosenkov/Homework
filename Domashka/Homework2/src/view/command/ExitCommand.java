package org.LosenkovIvan.view.command;

import lombok.Getter;

public class ExitCommand implements Command {
    @Getter
    private final String description = "Выход";

    @Override
    public void execute() {
        System.out.println("Завершение работы приложения...");
    }
}