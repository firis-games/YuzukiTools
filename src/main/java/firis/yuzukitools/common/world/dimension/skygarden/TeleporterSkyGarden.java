package firis.yuzukitools.common.world.dimension.skygarden;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterSkyGarden extends Teleporter {

	protected WorldServer world;
	
	public TeleporterSkyGarden(WorldServer worldIn) {
		super(worldIn);
		world = worldIn;
	}

	/**
	 * 座標移動のみを行う
	 */
	public void placeInPortal(Entity entityIn, float rotationYaw)
    {
		//オーバーワールドの場合は何もしない
		if (world.provider.getDimension() == DimensionType.OVERWORLD.getId()) return; 
		
		//スポーンポイント位置調整
		BlockPos pos = world.provider.getRandomizedSpawnPoint();
		
		//Playerの位置調整
        entityIn.setLocationAndAngles((double)pos.getX() + 0.5, 
        		(double)pos.getY(), 
        		(double)pos.getZ() + 0.5, entityIn.rotationYaw, 0.0F);
        entityIn.motionX = 0.0D;
        entityIn.motionY = 0.0D;
        entityIn.motionZ = 0.0D;
    }
}
