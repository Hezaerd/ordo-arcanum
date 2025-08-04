package com.hezaerd.protego;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.util.Identifier;

public final class ModLib {
	private ModLib() {}

	public static final String MOD_ID = "protego";
	public static final String MOD_NAME = "Protego";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
