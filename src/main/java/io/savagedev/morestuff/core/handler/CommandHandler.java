package io.savagedev.morestuff.core.handler;

/*
 * CommandHandler.java
 * Copyright (C) 2018 Savage - github.com/devsavage
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;
import io.savagedev.morestuff.core.Reference;
import io.savagedev.morestuff.core.command.commands.CommandHelp;
import io.savagedev.morestuff.core.command.ISubCommand;
import io.savagedev.morestuff.core.command.commands.CommandLevelTool;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommandHandler extends CommandBase
{
    public static CommandHandler instance = new CommandHandler();

    private static TMap<String, ISubCommand> commands = new THashMap<String, ISubCommand>();

    static {
        registerSubCommand(CommandHelp.instance);
        registerSubCommand(CommandLevelTool.instance);
    }

    @Override
    public String getCommandName() {
        return Reference.MOD_ID;
    }

    @Override
    public List<String> getCommandAliases() {
        List<String> altName = new ArrayList<String>();

        altName.add("ms");

        return altName;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + getCommandName() + " help";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) {
            args = new String[] { "help" };
        }

        ISubCommand command = commands.get(args[0]);

        if (command != null) {
            if (canUseCommand(sender, command.getPermissionLevel(), command.getCommandName())) {
                command.execute(server, sender, args);
                return;
            }

            throw new CommandException("commands.generic.permission");
        }

        throw new CommandNotFoundException("info.morestuff.command.notFound");
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, commands.keySet());
        } else if (commands.containsKey(args[0])) {
            return commands.get(args[0]).getTabCompletionOptions(server, sender, args);
        }

        return null;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return -1;
    }

    public static boolean registerSubCommand(ISubCommand subCommand) {
        if(!commands.containsKey(subCommand.getCommandName())) {
            commands.put(subCommand.getCommandName(), subCommand);
            return true;
        }

        return false;
    }

    public static Set<String> getCommandList() {
        return commands.keySet();
    }

    public static int getCommandPermission(String command) {
        return getCommandExists(command) ? commands.get(command).getPermissionLevel() : Integer.MAX_VALUE;
    }

    public static boolean getCommandExists(String command) {
        return commands.containsKey(command);
    }

    public static boolean canUseCommand(ICommandSender sender, int permission, String name) {
        return getCommandExists(name) && (sender.canCommandSenderUseCommand(permission, "morestuff " + name) || (sender instanceof EntityPlayerMP && permission <= 0));
    }

    public static void initCommands(FMLServerStartingEvent event) {
        event.registerServerCommand(instance);
    }
}
