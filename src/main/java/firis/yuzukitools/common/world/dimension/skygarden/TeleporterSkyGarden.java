package firis.yuzukitools.common.world.dimension.skygarden;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TeleporterSkyGarden implements net.minecraftforge.common.util.ITeleporter {

	protected final WorldServer world;
	protected final int meta;
	
	public TeleporterSkyGarden(WorldServer world, int meta) {
		this.world = world;
		this.meta = meta;
	}


	@Override
	public void placeEntity(World world, Entity entityIn, float yaw) {
		//オーバーワールドの場合は何もしない
		if (world.provider.getDimension() == DimensionType.OVERWORLD.getId()) return; 
		
		//スポーンポイント位置調整
		BlockPos pos = SkyGardenManager.getInstance().getSpawnPoint(this.meta);
		
		//Playerの位置調整
        entityIn.setLocationAndAngles((double)pos.getX() + 0.5, 
        		(double)pos.getY(), 
        		(double)pos.getZ() + 0.5, entityIn.rotationYaw, 0.0F);
        entityIn.motionX = 0.0D;
        entityIn.motionY = 0.0D;
        entityIn.motionZ = 0.0D;
	}
}
