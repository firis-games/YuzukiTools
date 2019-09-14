package firis.core.common.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;

public class BlockPosHelper {

	/**
	 * 偶数直径の半球のBlockPos座標を生成する
	 * 0,0,0基点による座標のため実際に使う場合は計算が必要
	 * 球面座標系で座標計算
	 * @param radius 半径を設定する
	 * @param radius_y 高さの倍率1で普通の球体
	 * @param fill ブロック内部を埋め立てる場合はtrue
	 * @return
	 */
	public static List<BlockPos> getBlockPosHemisphere(int radius, double radius_y, boolean fill) {
	
		List<BlockPos> posList = new ArrayList<>();
		
		//0-180度が上半球
		//180-360度が下半球
		//処理負荷軽減のため下半球の90度分だけ座標を計算する
		//重複BlockPosは除外
		for (int i = 180; i < 270; i++) {
			double radian1 = Math.toRadians(i);
			//4分の1だけ生成する
			for (int j = 0; j < 90; j++) {
				double radian2 = Math.toRadians(j);
				double x = radius * Math.sin(radian1) * Math.sin(radian2);
				double z = radius * Math.sin(radian1) * Math.cos(radian2);
				double y = radius * Math.cos(radian1) * radius_y;
				x = Math.round(x);
				z = Math.round(z);
				y = Math.round(y);
				boolean posFlg = false;
				BlockPos pos = new BlockPos(x, y, z);
				for (BlockPos item : posList) {
					if (pos.equals(item)) {
						posFlg = true;
						break;
					}
				}
				if (!posFlg) {
					posList.add(pos);
				}
			}
		}
		
		ListIterator<BlockPos> posIterator = posList.listIterator();
		
		//埋め立てする
		if (fill) {
			posIterator = posList.listIterator();
			while (posIterator.hasNext()) {
				BlockPos pos = posIterator.next();
				//x軸方向で計算して埋めていく
				//重複して何度も座標計算しているはずだが速度的には問題ないはず
				boolean minus = false;
				if (pos.getX() < 0) {
					minus = true;
				}
				for (int x = Math.abs(pos.getX()); 0 <= x; x--) {
					int xpos = minus ? -x : x;
					BlockPos fillPos = new BlockPos(xpos, pos.getY(), pos.getZ());
					boolean posFlg = false;
					for (BlockPos item : posList) {
						if (fillPos.equals(item)) {
							posFlg = true;
							break;
						}
					}
					if (!posFlg) {
						posIterator.add(fillPos);
					}
				}
			}
		}
		
		//この時点では90度分しかないため
		//座標を回転して4分の3分を補完する
		//上半球がほしい場合はy方向を逆転させる
		posIterator = posList.listIterator();
		while (posIterator.hasNext()) {
			BlockPos pos = posIterator.next();
			//偶数球体の場合は基点座標を回転方向へずらす必要がある
			//奇数球体に対応する場合は方角による座標補正は不要
			//x z -1
			posIterator.add(pos.rotate(Rotation.CLOCKWISE_180).north().west());
			//z -1
			posIterator.add(pos.rotate(Rotation.CLOCKWISE_90).west());
			//x -1
			posIterator.add(pos.rotate(Rotation.COUNTERCLOCKWISE_90).north());
		}
		
		return posList;
				
	}
	
}
