package dev.antirat;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@Mod(modid = "antirat", useMetadata=true)
public class AntiRat {

    public static Minecraft MC = Minecraft.getMinecraft();

    public static ArrayList<String> rats = new ArrayList<>();
    public static boolean sentLogOnWorldJoin = false;

    public static String changed;
    public static boolean changedToken;

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!sentLogOnWorldJoin) {
            rats.forEach((String message) -> {
                MC.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.DARK_GRAY + "AntiRat >> " + EnumChatFormatting.GRAY + message));
            });

            sentLogOnWorldJoin = true;
        }
    }

}
