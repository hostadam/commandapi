package com.github.hostadam.parameter.impl;

import com.github.hostadam.parameter.ParameterConverter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IntConverter implements ParameterConverter<Integer> {
    @Override
    public Integer convert(String arg) {
        return Integer.parseInt(arg);
    }

    @Override
    public void error(CommandSender sender, String arg) {
        sender.sendMessage("§c'" + arg + "' is not a valid number.");
    }
}
