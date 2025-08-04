package com.hezaerd.lumos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.util.Identifier;

public final class ModLib {
	private ModLib() {}

	public static final String MOD_ID = "lumos";
	public static final String MOD_NAME = "Lumos";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}
}
