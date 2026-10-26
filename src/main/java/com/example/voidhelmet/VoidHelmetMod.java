package com.example.voidhelmet;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import java.util.List;

@Mod(VoidHelmetMod.MODID)
public class VoidHelmetMod {
    public static final String MODID = "voidhelmet";
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> VOID_HELMET = ITEMS.register("void_helmet",
        () -> new ArmorItem(VoidArmorMaterial.INSTANCE, ArmorItem.Type.HELMET,
            new Item.Properties().stacksTo(1).fireResistant()) {
            @Override
            public Component getName(ItemStack stack) {
                return Component.literal("Шлем Пустоты");
            }
            @Override
            public boolean isFoil(ItemStack stack) { return true; }
        });

    public VoidHelmetMod() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) event.accept(VOID_HELMET);
    }

    public static boolean isWearing(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).is(VOID_HELMET.get());
    }

    @Mod.EventBusSubscriber(modid = MODID)
    public static class CommonEvents {
        @SubscribeEvent
        public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
            if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) return;
            Player player = event.player;
            if (isWearing(player)) {
                // Keep potion/status effects from showing or affecting the wearer.
                List<net.minecraft.world.effect.MobEffectInstance> effects = List.copyOf(player.getActiveEffects());
                for (var effect : effects) player.removeEffect(effect.getEffect());
            }
        }
    }

    public enum VoidArmorMaterial implements ArmorMaterial {
        INSTANCE;
        @Override
        public int getDurabilityForType(ArmorItem.Type type) {
            return switch (type) {
                case HELMET -> 11 * 25;
                case CHESTPLATE -> 16 * 25;
                case LEGGINGS -> 15 * 25;
                case BOOTS -> 13 * 25;
            };
        }
        @Override public int getDefenseForType(ArmorItem.Type type) { return type == ArmorItem.Type.HELMET ? 3 : 0; }
        @Override public int getEnchantmentValue() { return 30; }
        @Override public SoundEvent getEquipSound() { return SoundEvents.ARMOR_EQUIP_NETHERITE; }
        @Override public Ingredient getRepairIngredient() { return Ingredient.of(Items.DIAMOND); }
        @Override public String getName() { return MODID + ":void_helmet"; }
        @Override public float getToughness() { return 2.0f; }
        @Override public float getKnockbackResistance() { return 0.1f; }
    }
}
