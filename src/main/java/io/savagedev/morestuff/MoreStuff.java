package io.savagedev.morestuff;

/*
 * MoreStuff.java
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

import io.savagedev.morestuff.core.events.ExplosionEventListener;
import io.savagedev.morestuff.core.events.MobDropsEventListener;
import io.savagedev.morestuff.core.handler.*;
import io.savagedev.morestuff.core.keybindings.KeyInputEventHandler;
import io.savagedev.morestuff.core.network.handler.ServerPacketHandler;
import io.savagedev.morestuff.core.network.messages.MessageFlightKeyPressed;
import io.savagedev.morestuff.core.proxy.CommonProxy;
import io.savagedev.morestuff.core.helpers.LogHelper;
import io.savagedev.morestuff.core.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = Reference.MOD_ID, version = Reference.MOD_VERSION, name = Reference.MOD_NAME, acceptedMinecraftVersions = Reference.MC_VERSIONS, guiFactory = Reference.MOD_GUI_FACTORY)
public class MoreStuff
{
    public static final Logger LOG = LogManager.getLogger(Reference.MOD_ID);
    public static boolean developmentEnvironment = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    public static SimpleNetworkWrapper networkWrapper = null;

    @Mod.Instance(Reference.MOD_ID)
    public static MoreStuff instance;

    public static CreativeTabs tabMoreStuff = new CreativeTabs(Reference.MOD_ID + ".creativeTab") {
        @Override
        public Item getTabIconItem() {
            return ObjHandler.soulMatter;
        }
    };

    private File configDirectory;

    @SidedProxy(serverSide = Reference.MOD_COMMON_PROXY, clientSide = Reference.MOD_CLIENT_PROXY)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        configDirectory = new File(event.getModConfigurationDirectory(), Reference.MOD_NAME);
        ConfigHandler.init(new File(getConfigDirectory(), Reference.MOD_NAME + ".cfg"));

        networkWrapper = new SimpleNetworkWrapper(Reference.MOD_ID);
        networkWrapper.registerMessage(ServerPacketHandler.class, ServerPacketHandler.MoreStuffMessage.class, 0, Side.SERVER);
        networkWrapper.registerMessage(MessageFlightKeyPressed.MessageHandler.class, MessageFlightKeyPressed.class, 1, Side.SERVER);

        ObjHandler.registerBlocks();
        ObjHandler.registerTileEntities();
        ObjHandler.registerItems();

        MinecraftForge.EVENT_BUS.register(new MobDropsEventListener());
        MinecraftForge.EVENT_BUS.register(new ExplosionEventListener());
        MinecraftForge.EVENT_BUS.register(new ExplosionEventListener());

        proxy.preInit();

        LogHelper.info("Pre-Initialization Complete");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(MoreStuff.instance, new GuiHandler());

        (new RecipeHandler()).initTools();
        (new RecipeHandler()).initArmor();

        RecipeHandler.init();

        MinecraftForge.EVENT_BUS.register(new KeyInputEventHandler());

        LogHelper.info("Initialization Complete");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        LogHelper.info("Post-Initialization Complete");
    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        CommandHandler.initCommands(event);
        LogHelper.info("Server Starting Event Complete");
    }

    private File getConfigDirectory() {
        return this.configDirectory;
    }
}
