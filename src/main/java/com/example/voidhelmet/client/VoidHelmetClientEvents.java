package com.example.voidhelmet.client;

import com.example.voidhelmet.VoidHelmetMod;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VoidHelmetMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VoidHelmetClientEvents {
    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        if (VoidHelmetMod.isWearing(player)) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onRenderName(RenderNameTagEvent event) {
        if (event.getEntity() instanceof Player player && VoidHelmetMod.isWearing(player)) {
            event.setContent(net.minecraft.network.chat.Component.empty());
            event.setCanceled(true);
        }
    }
}
