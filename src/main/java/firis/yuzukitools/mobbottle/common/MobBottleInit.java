package firis.yuzukitools.mobbottle.common;

import firis.mobbottle.client.teisr.FTileMobBottleItemStackRenderer;
import firis.mobbottle.client.tesr.FTileMobBottleSpRenderer;
import firis.mobbottle.common.config.FirisConfig;
import firis.yuzukitools.YuzukiTools.YKItems;
import firis.yuzukitools.mobbottle.client.event.YKMobBottlePlusModelBakeEventHandler;
import firis.yuzukitools.mobbottle.common.tileentity.FTileEntityMobBottlePlus;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * モブボトル導入時の初期化処理
 * @author firis-games
 *
 */
public class MobBottleInit {

	/**
     * モブボトル系の処理はメソッドを分割
     * @param event
     */
    public static void registerModelsMobBottle(ModelRegistryEvent event)
    {
    	//モブボトルTESR
    	ClientRegistry.bindTileEntitySpecialRenderer(FTileEntityMobBottlePlus.class, new FTileMobBottleSpRenderer());
    	
    	if (FirisConfig.cfg_general_enable_mob_bottle_teisr) {
    		
	    	//モブボトルのTEISR登録
	    	YKItems.MOB_BOTTLE_PLUS.setTileEntityItemStackRenderer(new FTileMobBottleItemStackRenderer());
	    	
			//モブボトルモデル登録
			MinecraftForge.EVENT_BUS.register(YKMobBottlePlusModelBakeEventHandler.class);
			
    	}
    }
    
}
