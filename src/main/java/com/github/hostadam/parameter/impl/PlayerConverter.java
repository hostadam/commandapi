package com.github.hostadam.parameter.impl;

import com.github.hostadam.parameter.ParameterConverter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerConverter implements ParameterConverter<Player> {
    @Override
    public Player convert(String arg) {
        return Bukkit.getPlayer(arg);
    }

    @Override
    public void error(CommandSender sender, String arg) {
        sender.sendMessage("§c'" + arg + "' is not a valid player.");
    }
}
