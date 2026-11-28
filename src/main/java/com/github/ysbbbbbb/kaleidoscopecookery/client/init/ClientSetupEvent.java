package com.github.ysbbbbbb.kaleidoscopecookery.client.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.client.animation.CustomArmPose;
import com.github.ysbbbbbb.kaleidoscopecookery.client.gui.overlay.*;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.StrawHatModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.block.*;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacerReloadListener;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.ponder.init.PonderCompat;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.item.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterRangeSelectItemModelPropertyEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import org.jspecify.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import static net.neoforged.neoforge.client.gui.VanillaGuiLayers.CROSSHAIR;


@EventBusSubscriber(value = Dist.CLIENT, modid = KaleidoscopeCookery.MOD_ID)
public class ClientSetupEvent {
    private static final Identifier STRAW_HAT_TEXTURE = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "textures/models/armor/straw_hat.png");
    private static final Identifier STRAW_HAT_FLOWER_TEXTURE = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "textures/models/armor/straw_hat_flower.png");
    private static final Identifier FARMER_ARMOR_TEXTURE = Identifier.withDefaultNamespace("textures/models/armor/cookery_farmer_layer_1.png");
    private static final Identifier FARMER_LEGGINGS_TEXTURE = Identifier.withDefaultNamespace("textures/models/armor/cookery_farmer_layer_2.png");

    private static List<RecipeHolder<StockpotRecipe>> stockpotRecipes = List.of();

    @SubscribeEvent
    @SuppressWarnings("all")
    public static void onClientSetup(FMLClientSetupEvent event) {
        // 其他模组兼容
        PonderCompat.init();
    }

    @SubscribeEvent
    public static void onRecipesReceived(RecipesReceivedEvent event) {
        stockpotRecipes = List.copyOf(event.getRecipeMap().byType(ModRecipes.STOCKPOT_RECIPE));
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        stockpotRecipes = List.of();
    }

    public static List<RecipeHolder<StockpotRecipe>> getStockpotRecipes() {
        return stockpotRecipes;
    }

    @SubscribeEvent
    public static void registerItemModelProperties(RegisterRangeSelectItemModelPropertyEvent event) {
        for (ItemModelProperty property : ItemModelProperty.values()) {
            event.register(property.id, property.type());
        }
    }

    @SubscribeEvent
    public static void onEntityRenderers(EntityRenderersEvent.RegisterRenderers evt) {
        BlockEntityRenderers.register(ModBlocks.POT_BE.get(), PotBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.FRUIT_BASKET_BE.get(), FruitBasketBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.CHOPPING_BOARD_BE.get(), ChoppingBoardBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.STOCKPOT_BE.get(), StockpotBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.KITCHENWARE_RACKS_BE.get(), KitchenwareRacksBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.KITCHEN_SHELF_BE.get(), KitchenShelfBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.BAMBOO_TRAY_BE.get(), BambooTrayBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.CHAIR_BE.get(), ChairBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.TABLE_BE.get(), TableBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.SHAWARMA_SPIT_BE.get(), ShawarmaSpitBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.MILLSTONE_BE.get(), MillstoneBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.RECIPE_BLOCK_BE.get(), RecipeBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.STEAMER_BE.get(), SteamerBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.FOOD_BITE_THREE_BY_THREE_BE.get(), FoodBiteThreeByThreeBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.TEAPOT_BE.get(), TeapotBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.TRASH_CAN_BE.get(), TrashCanBlockEntityRender::new);
        BlockEntityRenderers.register(ModBlocks.TEA_BANNER_BE.get(), TeaBannerBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiLayersEvent event) {
        event.registerAbove(CROSSHAIR, Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "pot_overlay"), new PotOverlay());
        event.registerAbove(CROSSHAIR, Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "teapot_overlay"), new TeapotOverlay());
        event.registerAbove(CROSSHAIR, Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "trash_can_overlay"), new TrashCanOverlay());
        event.registerAbove(CROSSHAIR, Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "steamer_overlay"), new SteamerOverlay());
        event.registerAbove(CROSSHAIR, Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "kitchen_shelf_overlay"), new KitchenShelfOverlay());
    }

    @SubscribeEvent
    public static void onRegisterClientReloadListeners(AddClientReloadListenersEvent event) {
        event.addListener(Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "item_render_replacer"),
                new ItemRenderReplacerReloadListener());
    }

    @SubscribeEvent
    @SuppressWarnings("all")
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        // LiftBlockItem - 举起姿势
        event.registerItem(new IClientItemExtensions() {
            private final HumanoidModel.ArmPose liftPose = CustomArmPose.LIFT_POSE.getValue();

            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entity, InteractionHand hand, ItemStack stack) {
                if (!stack.isEmpty()) {
                    return liftPose;
                }
                return HumanoidModel.ArmPose.EMPTY;
            }
        }, ModItems.COLD_CUT_HAM_SLICES.get());

        // StrawHatItem - 草帽模型
        event.registerItem(new IClientItemExtensions() {
            private final Map<HumanoidModel<?>, StrawHatModel<?>> models = new IdentityHashMap<>();
            private @Nullable EntityModelSet cachedModelSet;

            @Override
            public int getArmorLayerTintColor(ItemStack stack, EquipmentClientInfo.Layer layer, int layerIdx, int fallbackColor) {
                if (stack.getItem() instanceof StrawHatItem && layerIdx != 0) {
                    return 0;
                }
                return IClientItemExtensions.super.getArmorLayerTintColor(stack, layer, layerIdx, fallbackColor);
            }

            @Override
            public Model<?> getGenericArmorModel(ItemStack stack, EquipmentClientInfo.LayerType type, Model original) {
                if (!(stack.getItem() instanceof StrawHatItem) || !(original instanceof HumanoidModel<?> humanoid)) {
                    return original;
                }
                EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
                if (this.cachedModelSet != modelSet) {
                    this.models.clear();
                    this.cachedModelSet = modelSet;
                }
                return this.models.computeIfAbsent(humanoid, model ->
                        new StrawHatModel<>(modelSet.bakeLayer(StrawHatModel.LAYER_LOCATION), model));
            }

            @Override
            public Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type,
                                              EquipmentClientInfo.Layer layer, Identifier fallback) {
                if (stack.getItem() instanceof StrawHatItem hat) {
                    if (hat.hasFlower()) {
                        return STRAW_HAT_FLOWER_TEXTURE;
                    }
                    return STRAW_HAT_TEXTURE;
                }
                if (stack.is(ModItems.FARMER_LEGGINGS.get())) {
                    return FARMER_LEGGINGS_TEXTURE;
                }
                return FARMER_ARMOR_TEXTURE;
            }
        }, ModItems.STRAW_HAT.get(), ModItems.STRAW_HAT_FLOWER.get(), ModItems.FARMER_CHEST_PLATE.get(),
                ModItems.FARMER_LEGGINGS.get(), ModItems.FARMER_BOOTS.get());
    }

    public enum ItemModelProperty implements RangeSelectItemModelProperty {
        HAS_OIL(KitchenShovelItem.HAS_OIL_PROPERTY),
        USING(StockpotLidItem.USING_PROPERTY),
        PULL(RawDoughItem.PULL_PROPERTY),
        HAS_RECIPE(RecipeItem.HAS_RECIPE_PROPERTY),
        HAS_ITEMS(TransmutationLunchBagItem.HAS_ITEMS_PROPERTY);

        private final Identifier id;
        private final MapCodec<ItemModelProperty> codec = MapCodec.unit(this);

        ItemModelProperty(Identifier id) {
            this.id = id;
        }

        @Override
        public MapCodec<ItemModelProperty> type() {
            return this.codec;
        }

        @Override
        public float get(ItemStack stack, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
            LivingEntity entity = owner == null ? null : owner.asLivingEntity();
            return switch (this) {
                case HAS_OIL -> stack.is(ModItems.KITCHEN_SHOVEL.get())
                        ? KitchenShovelItem.getTexture(stack, level, entity, seed)
                        : OilPotItem.getTexture(stack, level, entity, seed);
                case USING -> StockpotLidItem.getTexture(stack, level, entity, seed);
                case PULL -> RawDoughItem.getTexture(stack, level, entity, seed);
                case HAS_RECIPE -> RecipeItem.getTexture(stack, level, entity, seed);
                case HAS_ITEMS -> stack.is(ModItems.TRANSMUTATION_LUNCH_BAG.get())
                        ? TransmutationLunchBagItem.getTexture(stack, level, entity, seed)
                        : SteamerItem.getTexture(stack, level, entity, seed);
            };
        }
    }

}
