package net.devmc.terrabossaddons.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.devmc.terrabossaddons.TerraBossAddons;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.HashMap;
import java.util.Map;

public class AngerComponent implements Component {

	int anger;
	private final Map<PlayerEntity, Integer> angerMap = new HashMap<>();

	public int getAnger(PlayerEntity player) {
		return angerMap.getOrDefault(player, 0);
	}

	public void setAnger(PlayerEntity player, int anger) {
		angerMap.put(player, anger);
	}

	public Map<PlayerEntity, Integer> getAllAngerLevels() {
		return angerMap;
	}

	@Override
	public void readFromNbt(NbtCompound tag) {
		angerMap.clear();
		NbtList list = tag.getList("angerLevels", NbtElement.COMPOUND_TYPE);
		for (int i = 0; i < list.size(); i++) {
			NbtCompound entry = list.getCompound(i);
			PlayerEntity player = TerraBossAddons.server.getPlayerManager().getPlayer(entry.getUuid("player"));
			int anger = entry.getInt("anger");
			angerMap.put(player, anger);
		}
	}

	@Override
	public void writeToNbt(NbtCompound tag) {
		NbtList list = new NbtList();
		for (Map.Entry<PlayerEntity, Integer> entry : angerMap.entrySet()) {
			NbtCompound entryTag = new NbtCompound();
			entryTag.putString("player", entry.getKey().getUuidAsString());
			entryTag.putInt("anger", entry.getValue());
			list.add(entryTag);
		}
		tag.put("angerLevels", list);
	}
}
