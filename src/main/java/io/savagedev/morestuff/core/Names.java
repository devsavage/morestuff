package io.savagedev.morestuff.core;

/*
 * Names.java
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

public class Names
{
    public static final class Items {
        public static final String soulAxe = "soulAxe";
        public static final String soulHoe = "soulHoe";
        public static final String soulPickaxe = "soulPickaxe";
        public static final String soulShovel = "soulShovel";
        public static final String soulSword = "soulSword";
        public static final String soulHelmet = "soulHelmet";
        public static final String soulChestplate = "soulChestplate";
        public static final String soulLeggings = "soulLeggings";
        public static final String soulBoots = "soulBoots";
        public static final String soulMatterRaw = "soulMatterRaw";
        public static final String soulMatter = "soulMatter";
        public static final String infusionStone = "infusionStone";
        public static final String emeraldRod = "emeraldRod";
        public static final String magnet = "magnet";
    }

    public static final class Blocks {
        public static final String infuser = "blockInfuser";
        public static final String infuserActive = "blockInfuserActive";
    }

    public static final class NBT {
        public static final String UUID_MOST_SIG = "UUIDMostSig";
        public static final String UUID_LEAST_SIG = "UUIDLeastSig";
        public static final String OWNER = "Owner";
        public static final String CUSTOM_NAME = "CustomName";
        public static final String STATE = "TEState";
        public static final String ITEMS = "Items";
        public static final String FUEL_BURN_TIME = "FuelBurnTime";
        public static final String INFUSE_TIME = "InfuseTime";
        public static final String CURRENT_INFUSE_TIME = "CurrentInfuseTime";
        public static final String TOTAL_INFUSE_TIME = "TotalInfuseTime";
        public static final String TOTAL_FUEL_STORED = "TotalStoredFuel";
    }

    public static final class Container {
        public static final String infuser = "container." + Reference.MOD_DOMAIN + "blockInfuser";
    }

    public static final class Gui {
        public static final int infuserIndex = 0;
        public static final String infuserResourceLocation = "textures/gui/GuiInfuser.png";
    }

    public static final class Common {
        public static final String INFUSED = "Infused";
        public static final String ACTIVE = "Active";
        public static final String SOUND_TIMER = "SoundTimer";
    }
}
