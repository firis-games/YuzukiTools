package firis.yuzukitools.common.tileentity;

import firis.yuzukitools.common.event.CursedStoneSpawnHandler;
import net.minecraft.util.ITickable;

/**
 * 呪いの石
 * @author firis-games
 *
 */
public class YKTileCursedStone extends AbstractTileEntity implements ITickable {

	/**
	 * 毎tick処理
	 */
	@Override
	public void update() {
		//リストに登録
		if (!this.isInvalid()) {
			CursedStoneSpawnHandler.AddBlock(this);
		}
	}
	
	/**
	 * ブロック削除時
	 */
	@Override
	public void invalidate() {
		super.invalidate();
		CursedStoneSpawnHandler.removeBlock(this);
	}

}
