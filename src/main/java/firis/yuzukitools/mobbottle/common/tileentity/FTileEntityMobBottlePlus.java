package firis.yuzukitools.mobbottle.common.tileentity;

import firis.mobbottle.common.tileentity.FTileEntityMobBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;


/**
 * 
 * @author firis-games
 *
 */
public class FTileEntityMobBottlePlus extends FTileEntityMobBottle {

	private final MobSpawnerBaseLogic spawnerLogic = new FMobBottleSpawnerLogic(this);
	
	/**
	 * モブボトルの初期化
	 */
	@Override
	public void initMobBottle(ItemStack stack, EnumFacing facing) {
		
		super.initMobBottle(stack, facing);
		
		//スポナー情報設定
		if (this.isMob) {
			NBTTagCompound nbt = (NBTTagCompound) this.itemStackNBT.getTag("tag");
			nbt = (NBTTagCompound) nbt.getTag("Mob");
			String mobId = nbt.getString("id");
			this.spawnerLogic.setEntityId(new ResourceLocation(mobId));
			this.spawnerLogic.resetTimer();
		}
		
	}
	
	
	/**
	 * @Intarface ITickable
	 */
	@Override
	public void update() {
		super.update();
		//モブが入っている場合はスポーン処理
		if (this.isMob) {
			this.spawnerLogic.updateSpawner();
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		
		super.readFromNBT(compound);
		this.spawnerLogic.readFromNBT(compound);
		
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		
		compound = super.writeToNBT(compound);
		this.spawnerLogic.writeToNBT(compound);
		
		return compound;
	}
	
}
