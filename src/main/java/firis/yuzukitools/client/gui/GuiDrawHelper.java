package firis.yuzukitools.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class GuiDrawHelper {
	
	/**
	 * ゲージ描画用Helperクラス
	 * 描画テクスチャのサイズは16×16を想定する
	 */
	public static void drawGage(GuiContainer gui, TextureAtlasSprite texture, 
			int x, int y, int width, int height, double percent) {
	
        //ゲージのy軸を計算(最大値height)
      	int gage_height = (int) Math.floor(percent * (double)height);
      	
      	//縦軸の描画数
      	int xLoop = (int) Math.ceil((double)height / (double)16);
      	
      	//横軸の描画数
      	int yLoop = (int) Math.ceil((double)width / (double)16);

      	//下から上へ最大4回描画する
      	//　16*3　+　2分のゲージ
      	for (int idx = 0; idx < xLoop; idx++) {
      		
      		//ゲージの初期位置
      		int y_base = height - (16 * (idx + 1));
      		
      		//描画初期位置
      		int y_start = y_base;
      		//ゲージの長さ
      		int y_length = Math.min(16, gage_height - (16 * idx));
      		
      		//マイナスの場合は処理を終了
      		if (y_length < 0) {
      			break;
      		}
      		//残りゲージによるy軸計算
      		if (y_length < 16) {
      			y_start = y_start + (16 - y_length);
      		}
      		
      		//ノーマルはループ分描画
      		for (int idxY = 0; idxY < yLoop; idxY++) {
      			
      			int x_size = 16;
      			if (idxY + 1 == yLoop) {
      				x_size = (width % 16) == 0 ? 16 : (width % 16);
      			}
      			
      			//テクスチャをサイズに合わせられてるよう
      			//圧縮しないように表示する方法はいまのところ不明
      			//アナログにやるならゲージを描画した後に背景を上書きすれば擬似的にはできる
      			gui.drawTexturedModalRect(x + (16 * idxY), y + y_start, texture, x_size, y_length);
      			
      		}
      		
      	}
	}
	
	
	/**
	 * かまどなどの矢印描画用Helperクラス
	 */
	public static void drawArrow(GuiContainer gui, int x, int y, 
			int tex_x, int tex_y, int width, int height, double percent) {
		//widthサイズを計算
		int drawWidth = (int) Math.floor(percent * (double)width);
		if (drawWidth == 0) return;
		gui.drawTexturedModalRect(x, y, tex_x, tex_y, drawWidth, height);
	}

}
