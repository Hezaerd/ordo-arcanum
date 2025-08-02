package com.hezaerd.lumos.datagen.provider;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.util.Identifier;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

public abstract class ServerLanguageProvider implements DataProvider {
    protected final FabricDataOutput dataOutput;
    private final String languageCode;
    private final String customNamespace;

    protected ServerLanguageProvider(FabricDataOutput dataOutput, String languageCode) {
        this(dataOutput, languageCode, null);
    }

    protected ServerLanguageProvider(FabricDataOutput dataOutput, String languageCode, String customNamespace) {
        this.dataOutput = dataOutput;
        this.languageCode = languageCode;
        this.customNamespace = customNamespace;
    }

    public abstract void generateTranslations(FabricLanguageProvider.TranslationBuilder translationBuilder);

    public CompletableFuture<?> run(DataWriter writer) {
        TreeMap<String, String> translationEntries = new TreeMap<>();
        this.generateTranslations((key, value) -> {
            Objects.requireNonNull(key);
            Objects.requireNonNull(value);
            if (translationEntries.containsKey(key)) {
                throw new RuntimeException("Existing translation key found - " + key + " - Duplicate will be ignored.");
            } else {
                translationEntries.put(key, value);
            }
        });
        JsonObject langEntryJson = new JsonObject();

        for(Map.Entry<String, String> entry : translationEntries.entrySet()) {
            langEntryJson.addProperty(entry.getKey(), entry.getValue());
        }

        return DataProvider.writeToPath(writer, langEntryJson, this.getLangFilePath(this.languageCode));
    }

    private Path getLangFilePath(String code) {
        String namespace = this.customNamespace != null ? this.customNamespace : this.dataOutput.getModId();
        return this.dataOutput.getResolver(DataOutput.OutputType.DATA_PACK, "lang").resolveJson(new Identifier(namespace, code));
    }

    public String getName() {
        return "Language (%s)".formatted(this.languageCode);
    }
}
