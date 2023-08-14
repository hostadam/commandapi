package com.github.hostadam.handler;

import com.github.hostadam.Command;
import com.github.hostadam.impl.BukkitCommand;
import com.github.hostadam.impl.CommandImpl;
import com.github.hostadam.parameter.ParameterConverter;
import com.github.hostadam.parameter.impl.BooleanConverter;
import com.github.hostadam.parameter.impl.IntConverter;
import com.github.hostadam.parameter.impl.OfflinePlayerConverter;
import com.github.hostadam.parameter.impl.OnlinePlayerConverter;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class CommandHandler {

    private CommandMap map;
    private final Map<Class<?>, ParameterConverter<?>> parameters;
    private final Map<String, BukkitCommand> commands;

    public CommandHandler() {
        this.parameters = new HashMap<>();
        this.commands = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        try {
            final Server server = Bukkit.getServer();
            Field field = server.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            this.map = (CommandMap) field.get(server);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        this.addConverter(Boolean.class, new BooleanConverter());
        this.addConverter(IntConverter.class, new IntConverter());
        this.addConverter(OfflinePlayerConverter.class, new OfflinePlayerConverter());
        this.addConverter(OnlinePlayerConverter.class, new OnlinePlayerConverter());
    }

    public void register(Object object) {
        if(this.map == null) return;
        for(Method method : object.getClass().getMethods()) {
            if(!method.isAnnotationPresent(Command.class)) {
                continue;
            }

            Command command = method.getAnnotation(Command.class);
            CommandImpl impl = new CommandImpl(command, method, object);
            String name = command.labels()[0];

            if(name.contains(" ")) {
                String[] split = name.split(" ");
                BukkitCommand parentCommand = this.getCommandByLabel(split[0]);
                if(parentCommand != null) parentCommand.addSubCommand(impl);
            } else {
                List<String> aliases = new ArrayList<>();
                if(command.labels().length > 1) {
                    aliases.addAll(List.of(Arrays.copyOfRange(command.labels(), 1, command.labels().length)));
                }

                BukkitCommand bukkitCommand = new BukkitCommand(this, impl, name, command.description(), command.usage(), aliases);
                this.map.register(name, bukkitCommand);
                aliases.forEach(alias -> this.commands.put(alias, bukkitCommand));
            }
        }
    }

    public BukkitCommand getCommandByLabel(String name) {
        return this.commands.get(name);
    }

    public void addConverter(Class<?> clazz, ParameterConverter<?> converter) {
        this.parameters.put(clazz, converter);
    }

    public <T> ParameterConverter<T> getConverter(Class<T> clazz) {
        return (ParameterConverter<T>) this.parameters.get(clazz);
    }
}
