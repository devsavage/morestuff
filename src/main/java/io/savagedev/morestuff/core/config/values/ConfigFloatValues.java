package io.savagedev.morestuff.core.config.values;

/*
 * ConfigFloatValues.java
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

import io.savagedev.morestuff.core.config.ConfigCategories;

public enum ConfigFloatValues
{
    DEFAULT_FLIGHT_SPEED("Default Flight Speed", ConfigCategories.ITEMS, 0.05F, 0.01F, 0.5F, "Set the base flight speed for the Soul Matter Chestplate"),
    KEYBOUND_FLIGHT_SPEED("Keybound Flight Speed", ConfigCategories.ITEMS, 0.10F, 0.06F, 0.15F, "Set the flight speed when the flight speed keybind is pressed.");

    public final String name;
    public final String category;
    public final float defaultValue;
    public final float min;
    public final float max;
    public final String desc;

    public float currentValue;

    ConfigFloatValues(String name, ConfigCategories category, float defaultValue, float min, float max, String desc) {
        this.name = name;
        this.category = category.name;
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
        this.desc = desc;
    }

    public float getValue() {
        return this.currentValue;
    }
}
