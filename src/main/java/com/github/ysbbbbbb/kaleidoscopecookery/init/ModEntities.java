package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ScarecrowEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ThrowableBaoziEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.Supplier;

@EventBusSubscriber
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, KaleidoscopeCookery.MOD_ID);

    public static Supplier<EntityType<SitEntity>> SIT = ENTITY_TYPES.register("sit", () -> SitEntity.TYPE);
    public static Supplier<EntityType<ScarecrowEntity>> SCARECROW = ENTITY_TYPES.register("scarecrow", () -> ScarecrowEntity.TYPE);
    public static Supplier<EntityType<ThrowableBaoziEntity>> THROWABLE_BAOZI = ENTITY_TYPES.register("throwable_baozi", () -> ThrowableBaoziEntity.TYPE);

    // 同步肩上实体的完整数据，客户端据此读取实体种类和外观。
    public static final EntityDataSerializer<CompoundTag> SCARECROW_SHOULDER_SERIALIZER = new EntityDataSerializer<>() {
        @Override
        public StreamCodec<? super RegistryFriendlyByteBuf, CompoundTag> codec() {
            return ByteBufCodecs.TRUSTED_COMPOUND_TAG;
        }

        @Override
        public CompoundTag copy(CompoundTag tag) {
            return tag.copy();
        }
    };

    @SubscribeEvent
    public static void registerEntityDataSerializers(RegisterEvent event) {
        event.register(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS,
                Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "scarecrow_shoulder"),
                () -> SCARECROW_SHOULDER_SERIALIZER);
    }

    @SubscribeEvent
    public static void addEntityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ScarecrowEntity.TYPE, ScarecrowEntity.createAttributes().build());
    }
}
