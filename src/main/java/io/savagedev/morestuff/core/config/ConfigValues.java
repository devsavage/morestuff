package io.savagedev.morestuff.core.config;

/*
 * ConfigValues.java
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

import io.savagedev.morestuff.core.config.values.ConfigBlacklistValues;
import io.savagedev.morestuff.core.config.values.ConfigBooleanValues;
import io.savagedev.morestuff.core.config.values.ConfigFloatValues;
import net.minecraftforge.common.config.Configuration;

public class ConfigValues
{
    public static ConfigBlacklistValues[] blacklistConfig = ConfigBlacklistValues.values();
    public static ConfigBooleanValues[] booleanConfig = ConfigBooleanValues.values();
    public static ConfigFloatValues[] floatConfig = ConfigFloatValues.values();

    public static void initConfigValues(Configuration config) {
        for(ConfigBlacklistValues currentConf : blacklistConfig) {
            currentConf.currentValues = config.getStringList(currentConf.name, currentConf.category, currentConf.defaultValues, currentConf.desc);
        }

        for(ConfigBooleanValues currentConf : booleanConfig) {
            currentConf.currentValue = config.getBoolean(currentConf.name, currentConf.category, currentConf.defaultValue, currentConf.desc);
        }

        for(ConfigFloatValues currentConf : floatConfig) {
            currentConf.currentValue = config.getFloat(currentConf.name, currentConf.category, currentConf.defaultValue, currentConf.min, currentConf.max, currentConf.desc);
        }
    }

}
