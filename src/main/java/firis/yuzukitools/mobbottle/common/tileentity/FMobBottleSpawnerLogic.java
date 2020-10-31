package firis.yuzukitools.mobbottle.common.tileentity;

import firis.yuzukitools.YuzukiTools.YKBlocks;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * モブボトルプラス用スポーン処理
 * @author firis-games
 *
 */
public class FMobBottleSpawnerLogic extends MobSpawnerBaseLogic {
	
	private final FTileEntityMobBottlePlus tileentity;
	
	public FMobBottleSpawnerLogic(FTileEntityMobBottlePlus tile) {
		this.tileentity = tile;
	}

	@Override
	public void broadcastEvent(int id) {
		this.tileentity.getWorld().addBlockEvent(this.tileentity.getPos(), YKBlocks.MOB_BOTTLE_PLUS, id, 0);
	}

	@Override
	public World getSpawnerWorld() {
		return this.tileentity.getWorld();
	}

	@Override
	public BlockPos getSpawnerPosition() {
		return this.tileentity.getPos();
	}
	
	@Override
	public boolean isActivated() {
		return !isRedStonePower();
	}
	
	/**
	 * レッドストーン信号を受けているかを判断する
	 * @return
	 */
	protected boolean isRedStonePower() {
		int redstone = 0;
		for(EnumFacing dir : EnumFacing.VALUES) {
			int redstoneSide = tileentity.getWorld().getRedstonePower(tileentity.getPos().offset(dir), dir);
			redstone = Math.max(redstone, redstoneSide);
		}
		return redstone > 0;
	}

}
