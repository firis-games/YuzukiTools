package firis.yuzukitools.common.instanthouse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import firis.core.common.helper.PathsHelper;
import firis.core.common.helper.ResourceHelper;
import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.YuzukiTools.YKItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * インスタントハウスManager
 * 
 * 独自形式のTemplateManager
 * assetsと外部フォルダからNBTを取得する
 * @author computer
 *
 */
public class InstantHouseManager {
	
	/**
	 * テンプレート保存用マップ
	 */
	protected static Map<String, Template> templateMap;
	
	public static String DEFAULT_HOUSE = "house/ykt_default_house";
	
	/**
	 * Templateの読み込み
	 */
	public static void init() {
		
		//Template初期化
		InstantHouseManager.templateMap = new HashMap<>();
		
		//標準の家を設定
		setDefaultHouse();
		
		List<String> configList = ResourceHelper.getConfigFileList("instant_house");
		
		for (String path : configList) {
			
			Path file = PathsHelper.getConfigPath(path);
			NBTTagCompound compound = null;
			try {
				compound = CompressedStreamTools.readCompressed(Files.newInputStream(file));
			} catch (IOException e) {
			}
			if (compound != null) {
				
				//拡張子除去
				String name = path.substring(0, path.length() - 4);
				
				Template template = new Template();
				template.read(compound);
				templateMap.put(name, template);
			}
		}
	}
	
	/**
	 * 標準のインスタントハウスはassetsから取得する
	 */
	public static void setDefaultHouse() {
		
		String name = InstantHouseManager.DEFAULT_HOUSE;
		ResourceLocation rl = new ResourceLocation(YuzukiTools.MODID, name);
		Template template = InstantHouseManager.getTemplateToJar(rl);
		
		templateMap.put(name, template);
		
	}
	
	/**
	 * Template取得
	 * @param path
	 */
	public static Template getTemplate(String path) {
		Template template = null;
		if (templateMap.containsKey(path)) {
			template = templateMap.get(path);
		}
		return template;
	}
	
	/**
	 * Jarファイル内のテンプレートを取得する
	 * @return
	 */
	public static Template getTemplateToJar(ResourceLocation rl) {
		
		//ファイル読み込み
		InputStream inputstream = FMLCommonHandler.instance()
				.getClass().getClassLoader()
				.getResourceAsStream("assets/" + rl.getResourceDomain() + "/structures/" + rl.getResourcePath() + ".nbt");
		
		NBTTagCompound nbttagcompound = null;
		
		try {
			nbttagcompound = CompressedStreamTools.readCompressed(inputstream);
		} catch (IOException e) {
		}
		
		//Template生成
		Template template = new Template();
		template.read(nbttagcompound);
		
		return template;
	}
	
	/**
	 * NBT付のItemStackを作成する
	 * 対象パスが存在しない場合はItemStack.EMPTYを返却する
	 * @param path
	 */
	public static ItemStack getItemStack(String path) {
		ItemStack stack = ItemStack.EMPTY;
		if (templateMap.containsKey(path)) {
			stack = new ItemStack(YKItems.INSTANT_HOUSE);
	    	NBTTagCompound nbt = new NBTTagCompound();
	    	nbt.setString("template", path);
	    	stack.setTagCompound(nbt);
		}
		return stack;
	}
	
	/**
	 * クリエイティブタブ登録用ItemStackList
	 */
	public static List<ItemStack> getCreativeItemStackList() {
		
		List<ItemStack> list = new ArrayList<>();
		for (String key : templateMap.keySet()) {
			list.add(getItemStack(key));
		}
		return list;
	}
}
