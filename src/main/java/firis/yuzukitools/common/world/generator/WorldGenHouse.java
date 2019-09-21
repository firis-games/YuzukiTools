package firis.yuzukitools.common.world.generator;

import java.util.Random;

import firis.yuzukitools.common.instanthouse.InstantHouseManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;

public class WorldGenHouse extends WorldGenerator {

	protected String template;
	protected EnumFacing facing;
	
	public WorldGenHouse() {
		this.template = "";
		this.facing = EnumFacing.NORTH;
	}
	
	public WorldGenHouse(String template, EnumFacing facing) {
		this.template = template;
		this.facing = facing;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos position) {
		
		if ("".equals(this.template)) return false;
		
		//InstantHouseManagerから取得する
		Template template = InstantHouseManager.getTemplate(this.template);
		
        PlacementSettings placementsettings =  new PlacementSettings();
        
        BlockPos pos = position;
        
        int facing_x = 1;
        int facing_z = 0;
        
        //位置調整
        //北（標準）
        switch (this.facing) {
        case NORTH:
        	placementsettings.setRotation(Rotation.CLOCKWISE_180);
            pos = position.up().north(facing_x).west(facing_z);
        	break;
        case SOUTH:
        	placementsettings.setRotation(Rotation.NONE);
            pos = position.up().south(facing_x).east(facing_z);
        	break;
        case EAST:
        	placementsettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
            pos = position.up().north(facing_z).east(facing_x);
        	break;
        case WEST:
        	placementsettings.setRotation(Rotation.CLOCKWISE_90);
            pos = position.up().south(facing_z).west(facing_x);
        	break;
       	default:
        }
        
        //構造体設置
        template.addBlocksToWorldChunk(world, pos, placementsettings);

        return true;
	}
}
