package io.savagedev.morestuff.core.command.commands;

/*
 * CommandHelp.java
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

import io.savagedev.morestuff.core.command.ISubCommand;
import io.savagedev.morestuff.core.handler.CommandHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandHelp implements ISubCommand
{
    public static CommandHelp instance = new CommandHelp();

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public int getPermissionLevel() {
        return -1;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandNotFoundException {
        List<String> commandList = new ArrayList<String>(CommandHandler.getCommandList());
        Collections.sort(commandList, String.CASE_INSENSITIVE_ORDER);
        commandList.remove(getCommandName());

        for (int i = 0; i < commandList.size(); ++i) {
            String name = commandList.get(i);

            if (!CommandHandler.canUseCommand(sender, CommandHandler.getCommandPermission(name), name)) {
                commandList.remove(i--);
            }
        }

        final int pageSize = 7;
        int maxPages = (commandList.size() - 1) / pageSize;
        int page;

        try {
            page = args.length == 1 ? 0 : CommandBase.parseInt(args[1], 1, maxPages + 1) - 1;
        } catch (NumberInvalidException numberinvalidexception) {
            String commandName = args[1];

            if (!CommandHandler.getCommandExists(commandName))
                throw new CommandNotFoundException("info.morestuff.command.notFound");

            sender.addChatMessage(new TextComponentTranslation("info.morestuff.command." + commandName));
            return;
        }

        int maxIndex = Math.min((page + 1) * pageSize, commandList.size());
        ITextComponent textComponent = new TextComponentString(String.format("--- Showing help page %d of %d (/ms help <page>) ---", page + 1, maxPages + 1));
        textComponent.getStyle().setColor(TextFormatting.DARK_GREEN);
        sender.addChatMessage(textComponent);

        for (int i = page * pageSize; i < maxIndex; ++i) {
            ITextComponent textComponent1 = new TextComponentString("/morestuff " + TextFormatting.YELLOW + commandList.get(i));
            textComponent1.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/morestuff " + commandList.get(i)));
            sender.addChatMessage(textComponent1);
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length == 2) {
            return CommandBase.getListOfStringsMatchingLastWord(args, CommandHandler.getCommandList());
        }

        return null;
    }
}
