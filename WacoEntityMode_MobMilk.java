/**
 * Copyright 2013 Yamato
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.minecraft.src;

import mod.ymt.cmn.Utils;
import mod.ymt.milk.mod_MobMilk;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author Yamato
 *
 */
public class WacoEntityMode_MobMilk extends LMM_EntityModeBase {
	private static boolean enable = false;

	static {
		System.out.println("initializing WacoEntityMode_MobMilk");
	}

	public WacoEntityMode_MobMilk(LMM_EntityLittleMaid owner) {
		super(owner);
	}

	@Override
	public void addEntityMode(EntityAITasks pDefaultMove, EntityAITasks pDefaultTargeting) {
		;
	}

	@Override
	public boolean changeMode(EntityPlayer pentityplayer) {
		return false;
	}

	@Override
	public boolean interact(EntityPlayer player, ItemStack pitemstack) {
		if (isEnable()) {
			if (tryInteractBucket(player) || tryInteractBottle(player)) {
				if (Utils.isServerSide(owner.worldObj)) {
					owner.worldObj.setEntityState(owner, (byte) 7);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public int priority() {
		return 9999;
	}

	public static boolean isEnable() {
		return mod_MobMilk.milkStew != null;
	}

	private static boolean tryInteractBottle(EntityPlayer player) {
		if (mod_MobMilk.milkStew == null) {	// ミルク未登録のときは何もしない
			return false;
		}
		ItemStack item = player.getCurrentEquippedItem();
		if (item != null && item.itemID == Item.bowlEmpty.itemID) {
			item.stackSize--;
			ItemStack milkItem = new ItemStack(mod_MobMilk.milkStew, 1, 0);
			if (item.stackSize <= 0) {
				player.inventory.setInventorySlotContents(player.inventory.currentItem, milkItem);
			}
			else if (!player.inventory.addItemStackToInventory(milkItem)) {
				player.dropPlayerItem(milkItem);
			}
			return true;
		}
		return false;
	}

	private static boolean tryInteractBucket(EntityPlayer player) {
		ItemStack item = player.getCurrentEquippedItem();
		if (item != null && item.itemID == Item.bucketEmpty.itemID) {
			item.stackSize--;
			ItemStack milkBucket = new ItemStack(Item.bucketMilk.itemID, 1, 0);
			if (item.stackSize <= 0) {
				player.inventory.setInventorySlotContents(player.inventory.currentItem, milkBucket);
			}
			else if (!player.inventory.addItemStackToInventory(milkBucket)) {
				player.dropPlayerItem(milkBucket);
			}
			return true;
		}
		return false;
	}
}
