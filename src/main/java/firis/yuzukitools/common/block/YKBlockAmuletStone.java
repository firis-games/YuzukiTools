package firis.yuzukitools.common.block;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.common.tileentity.YKTileAmuletStone;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * 魔除けの石
 * @author firis-games
 *
 */
public class YKBlockAmuletStone extends AbstractBlockContainer {

	/**
	 * コンストラクタ
	 * @param materialIn
	 */
	public YKBlockAmuletStone() {
		super(Material.ROCK);
		this.setHardness(5.0F);
		this.setResistance(20.0F);
		this.setLightLevel(0.5F);
		this.setCreativeTab(YuzukiTools.YKCreativeTab);
	}

	/**
	 * TileEntity
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new YKTileAmuletStone();
	}
	
}
