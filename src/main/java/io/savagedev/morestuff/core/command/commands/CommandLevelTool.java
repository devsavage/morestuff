package io.savagedev.morestuff.core.command.commands;

/*
 * CommandLevelTool.java
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

import io.savagedev.morestuff.common.items.base.ItemSoulTool;
import io.savagedev.morestuff.core.command.ISubCommand;
import io.savagedev.morestuff.core.handler.ToolLevelHandler;
import io.savagedev.morestuff.core.helpers.LogHelper;
import io.savagedev.morestuff.core.helpers.NBTHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

import java.util.List;

public class CommandLevelTool implements ISubCommand
{
    public static CommandLevelTool instance = new CommandLevelTool();

    @Override
    public String getCommandName() {
        return "leveltool";
    }

    @Override
    public int getPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandNotFoundException {
        ItemStack currentItem = sender.getEntityWorld().getPlayerEntityByName(sender.getName()).getHeldItemMainhand();
        if(currentItem != null) {
            EntityPlayer player = sender.getEntityWorld().getPlayerEntityByName(sender.getName());
            if(currentItem.hasTagCompound() && currentItem.getTagCompound().hasKey(ToolLevelHandler.SOUL_TOOL_TAG)) {
                if(!ToolLevelHandler.isMaxToolLevel(currentItem, NBTHelper.getTagCompound(currentItem, ToolLevelHandler.SOUL_TOOL_TAG))) {
                    if(args.length == 2) {
                        if(args[1].equals("max")) {
                            ToolLevelHandler.setMaxLevel(player, currentItem);
                        }
                    } else {
                        ToolLevelHandler.levelUp(currentItem, player);

                        sender.addChatMessage(new TextComponentString("Your " + currentItem.getDisplayName() + " has been leveled up to level " + ToolLevelHandler.getToolLevel(currentItem,NBTHelper.getTagCompound(currentItem, ToolLevelHandler.SOUL_TOOL_TAG)) + "."));
                    }
                } else {
                    sender.addChatMessage(new TextComponentTranslation(("info.morestuff.command.leveltool.max")));
                }
            } else {
                sender.addChatMessage(new TextComponentTranslation(("info.morestuff.command.leveltool.hand")));
            }
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length == 2) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "max");
        }

        return null;
    }
}
