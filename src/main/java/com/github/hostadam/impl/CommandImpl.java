package com.github.hostadam.impl;

import com.github.hostadam.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;
import java.util.List;

@Getter
@AllArgsConstructor
public class CommandImpl {

    private Command command;
    private Method method;
    private Object object;
}
