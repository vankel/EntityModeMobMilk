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
package mod.ymt.milk;

import java.util.List;
import mod.ymt.cmn.Utils;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class ItemMilkStew extends ItemFood {
	private Icon[] textures = null;

	public ItemMilkStew(int id) {
		super(id, 0, 0, false);
		setMaxDamage(20);
		setHasSubtypes(true);
		setMaxStackSize(1);
		setPotionEffect(Potion.regeneration.id, 5, 0, 1.0F);
	}

	@Override
	public Icon getIconFromDamage(int metadata) {
		if (textures == null)
			return null;
		return textures[metadata == 0 ? 0 : 1];
	}

	@Override
	public EnumAction getItemUseAction(ItemStack item) {
		if (item.getItemDamage() == 0)
			return EnumAction.drink;
		else
			return EnumAction.eat;
	}

	@Override
	public void getSubItems(int itemId, CreativeTabs tabs, List list) {
		list.add(new ItemStack(itemId, 1, 0));
		list.add(new ItemStack(itemId, 1, 1));
	}

	@Override
	public String getUnlocalizedName(ItemStack item) {
		return super.getUnlocalizedName() + "." + item.getItemDamage();
	}

	@Override
	public ItemStack onEaten(ItemStack item, World world, EntityPlayer player) {
		if (Utils.isServerSide(world)) {
			// クリア
			player.clearActivePotions();
			if (item.getItemDamage() > 0) { // 満腹度上昇
				// player.getFoodStats().addStats(8, 0.8f);	// 8, 0.8 が牛ステーキと同じ回復量
				int foodAmount = item.getMaxDamage() - item.getItemDamage();
				player.getFoodStats().addStats(foodAmount, 0.6f);
				world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
				onFoodEaten(item, world, player);
			}
		}
		item.stackSize--;
		return new ItemStack(Item.bowlEmpty);
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		this.textures = new Icon[]{
			par1IconRegister.registerIcon("mod.ymt.milkbowl"), par1IconRegister.registerIcon("mod.ymt.milkstew"),
		};
	}
}
