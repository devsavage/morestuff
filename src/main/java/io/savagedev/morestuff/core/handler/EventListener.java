package io.savagedev.morestuff.core.handler;

/*
 * EventListener.java
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

import io.savagedev.morestuff.core.helpers.LogHelper;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventListener
{
    @SubscribeEvent
    public void onEntityDrop(LivingDropsEvent event) {
        double rand = Math.random();

        if(event.getEntityLiving() instanceof EntityWither) {
            event.getEntityLiving().dropItem(ObjHandler.soulMatter, 3);
        } else if(event.getEntityLiving() instanceof EntityEnderman) {
            if (rand < 0.2D && rand > 0.14D) {
                event.getEntityLiving().dropItem(ObjHandler.soulMatterRaw, 1);
            }
        } else if(event.getEntityLiving() instanceof EntitySkeleton) {
            EntitySkeleton skeleton = (EntitySkeleton) event.getEntityLiving();
            if(skeleton.getSkeletonType() == SkeletonType.WITHER) {
                if (rand < 0.2D && rand > 0.16D) {
                    event.getEntityLiving().dropItem(ObjHandler.soulMatterRaw, 1);
                }
            }
        }
    }

    @SubscribeEvent
    public void onExplosionEvent(ExplosionEvent event) {
        if(event.getExplosion().getExplosivePlacedBy() instanceof EntityCreeper && ((EntityCreeper)event.getExplosion().getExplosivePlacedBy()).getPowered()) {
            event.getExplosion().getExplosivePlacedBy().dropItem(ObjHandler.soulMatter, 11);
        }
    }
}
