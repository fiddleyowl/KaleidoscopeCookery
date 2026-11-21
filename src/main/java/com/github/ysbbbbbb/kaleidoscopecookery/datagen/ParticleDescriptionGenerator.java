package com.github.ysbbbbbb.kaleidoscopecookery.datagen;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModParticles;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.data.ParticleDescriptionProvider;

public class ParticleDescriptionGenerator extends ParticleDescriptionProvider {
    public ParticleDescriptionGenerator(PackOutput output) {
        super(output);
    }

    @Override
    protected void addDescriptions() {
        this.spriteSet(ModParticles.COOKING.get(), Identifier.withDefaultNamespace("generic"), 8, true);
        this.spriteSet(ModParticles.STOCKPOT.get(), Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "stockpot"), 5, false);
    }
}
