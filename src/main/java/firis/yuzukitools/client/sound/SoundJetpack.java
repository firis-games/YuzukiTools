package firis.yuzukitools.client.sound;

import java.lang.ref.WeakReference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.client.event.JetpackClientTickEventHandler;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

/**
 * Jetpack用サウンド
 * @author computer
 *
 */
public class SoundJetpack extends PositionedSound implements ITickableSound {

	@Nonnull
	private WeakReference<EntityPlayer> playerReference;
	
	private static final ResourceLocation SOUND = new ResourceLocation(YuzukiTools.MODID, "item.jetpack");
	
	public SoundJetpack(@Nonnull EntityPlayer player) {
		
		super(SOUND, SoundCategory.PLAYERS);
		
		this.playerReference = new WeakReference<>(player);
		
		this.xPosF = (float) player.posX;
		this.yPosF = (float) player.posY;
		this.zPosF = (float) player.posZ;
		
		this.repeat = true;
		this.repeatDelay = 0;
		this.volume = 1.0F;
	}

	@Override
	public void update() {
		//音を鳴らす制御
		EntityPlayer player = getPlayer();
		
		if (player == null || player.isDead) {
			 this.donePlaying = true;
			 this.volume = 0.0F;
		}
		
		if (isActive()) {
			this.volume = Math.min(this.volume + 0.1F, 1.0F);
		} else {
			this.volume = Math.max(this.volume - 0.1F, 0.0F);
		}
		
	}
	
	private boolean donePlaying = false;
	
	@Override
	public boolean isDonePlaying() {
		return donePlaying;
	}
	
	/**
	 * 音を鳴らせる状態かを判断する
	 * @return
	 */
	public boolean isActive() {
		return JetpackClientTickEventHandler.lastKeyjump || JetpackClientTickEventHandler.lastKeyBoost;
	}
	
	
    @Nullable
    private EntityPlayer getPlayer() {
        return playerReference.get();
    }

    @Override
    public float getXPosF() {
        EntityPlayer player = getPlayer();
        if (player != null) {
            this.xPosF = (float) player.posX;
        }
        return this.xPosF;
    }

    @Override
    public float getYPosF() {
        EntityPlayer player = getPlayer();
        if (player != null) {
            this.yPosF = (float) player.posY;
        }
        return this.yPosF;
    }

    @Override
    public float getZPosF() {
        EntityPlayer player = getPlayer();
        if (player != null) {
            this.zPosF = (float) player.posZ;
        }
        return this.zPosF;
    }

}
