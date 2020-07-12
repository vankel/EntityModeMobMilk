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

import java.util.ArrayList;
import java.util.List;
import mod.ymt.cmn.Utils;
import mod.ymt.milk.ItemMilkStew;

public class mod_MobMilk extends BaseMod {
	public static ItemMilkStew milkStew = null;

	@MLProp(min = 0)
	public static int milkStewItemId = 2601;

	@Override
	public String getPriorities() {
		return "required-after:mod_YMTLib";
	}

	@Override
	public String getVersion() {
		return "151v2";
	}

	@Override
	public void load() {
		// �~���N�V�`���[�ǉ�
		if (0 < milkStewItemId) {
			milkStew = new ItemMilkStew(milkStewItemId);
			milkStew.setUnlocalizedName("milkStew");
			Utils.addName(milkStew, "milk bowl", "�~���N�{�E��");
			Utils.addName(new ItemStack(milkStew, 0, 0), "milk bowl", "�~���N�{�E��");
			for (int i = 1; i < milkStew.getMaxDamage(); i++) {
				Utils.addName(new ItemStack(milkStew, 0, i), "milk stew", "�~���N�V�`���[");
			}

			// �~���N�{�E�����~���N�V�`���[�̃��V�s�ʎY
			addStewReceipe(null, 0);
			addStewReceipe(Item.chickenRaw, 6);
			addStewReceipe(Item.porkRaw, 8);
			addStewReceipe(Item.beefRaw, 8);
		}
		// EntityMode �L����
		MBLMMEntityMode_MobMilk.setEnable(true);
	}

	private void addStewReceipe(Item meat, int meatAmount) {
		// �ؓ������� = 8
		// �{�� = 6
		// �p�� = 5
		// �ɂ񂶂񁕂��Ⴊ���� = 4
		// ���� = 2
		for (int i = 0; i < 16; i++) {
			List<Object> receipe = new ArrayList<Object>();
			receipe.add(new ItemStack(milkStew, 1, 0));
			int foodAmount = 0;
			if (meat != null) {
				receipe.add(meat);
				foodAmount += meatAmount;
			}
			if (bitOn(i, 1)) {
				receipe.add(Item.bread);
				foodAmount += 5;
			}
			if (bitOn(i, 2)) {
				receipe.add(Item.carrot);
				foodAmount += 4;
			}
			if (bitOn(i, 4)) {
				receipe.add(Item.potato);
				foodAmount += 4;
			}
			if (bitOn(i, 8) && 0 < foodAmount) { // �����P�Ƃ̃��V�s�͔F�߂Ȃ�
				receipe.add(Item.sugar);
				foodAmount += 2;
			}
			if (0 < foodAmount) {
				foodAmount += 2; // �񕜗ʃ{�[�i�X +2
				int damage = Math.max(1, milkStew.getMaxDamage() - foodAmount);
				ModLoader.addShapelessRecipe(new ItemStack(milkStew, 1, damage), receipe.toArray());
			}
		}
	}

	private static boolean bitOn(int i, int b) {
		return (i & b) == b;
	}
}
