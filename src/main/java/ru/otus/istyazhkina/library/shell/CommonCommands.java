package ru.otus.istyazhkina.library.shell;

import org.h2.tools.Console;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.sql.SQLException;

@ShellComponent
public class CommonCommands {

    @ShellMethod(value = "Open H2 interface", key = {"interface"})
    public void openH2Interface() throws SQLException {
        Console.main();
    }
}
