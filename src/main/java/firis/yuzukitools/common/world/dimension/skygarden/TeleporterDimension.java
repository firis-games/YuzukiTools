package firis.yuzukitools.common.world.dimension.skygarden;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * 空中庭園から戻る際のテレポート
 * @author computer
 *
 */
public class TeleporterDimension implements net.minecraftforge.common.util.ITeleporter {

	protected final WorldServer world;
	protected final double x;
	protected final double y;
	protected final double z;
	
	public TeleporterDimension(WorldServer world, double x, double y, double z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	@Override
	public void placeEntity(World world, Entity entity, float yaw) {
		//Playerの位置調整
		entity.setLocationAndAngles(
				this.x, 
				this.y, 
				this.z, entity.rotationYaw, 0.0F);
        entity.motionX = 0.0D;
        entity.motionY = 0.0D;
        entity.motionZ = 0.0D;
	}

}
