package firis.yuzukitools.common.tileentity;

import firis.yuzukitools.common.event.AmuletStoneSpawnsEventHandler;
import net.minecraft.util.ITickable;

/**
 * 魔除けの石
 * @author firis-games
 *
 */
public class YKTileAmuletStone extends AbstractTileEntity implements ITickable {

	/**
	 * 毎tick処理
	 */
	@Override
	public void update() {
		//リストに登録
		if (!this.isInvalid()) {
			AmuletStoneSpawnsEventHandler.AddBlock(this);
		}
	}
	
	/**
	 * ブロック削除時
	 */
	@Override
	public void invalidate() {
		super.invalidate();
		AmuletStoneSpawnsEventHandler.removeBlock(this);
	}

}
