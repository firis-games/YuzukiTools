package firis.yuzukitools.common.instanthouse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

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
	protected static Map<String, InstantHouseInfo> templateMap;
	
	public static String DEFAULT_HOUSE = "house/ykt_default_house";
	
	/**
	 * Templateの読み込み
	 */
	public static void init() {
		
		//Template初期化
		InstantHouseManager.templateMap = new HashMap<>();
		
		//標準の家を設定
		setDefaultHouse();
		
		Gson gson = new Gson();
		
		List<String> configList = ResourceHelper.getConfigFileList("instant_house");
		
		for (String path : configList) {
			
			//名前と拡張子に分割
			String name = path.substring(0, path.length() - 4);
			String ext = path.substring(path.length() - 4, path.length());
			
			//NBTの場合のみ処理を続行
			if (!".nbt".equals(ext.toLowerCase())) continue;
			
			//NBTの取得
			Path file = PathsHelper.getConfigPath(path);
			NBTTagCompound compound = null;
			try {
				compound = CompressedStreamTools.readCompressed(Files.newInputStream(file));
			} catch (IOException e) {
			}
			
			//NBT取得できない場合はスキップ
			if (compound == null) continue;
			
			//Jsonの取得
			String jsonFile = ResourceHelper.getConfigFileString(name + ".json");
			JsonInstantHouseInfo json = gson.fromJson(jsonFile, JsonInstantHouseInfo.class);
			
			//テンプレート情報を生成
			Template template = new Template();
			template.read(compound);
			
			InstantHouseInfo info = new InstantHouseInfo(template, json);
			templateMap.put(name, info);

		}
	}
	
	/**
	 * 標準のインスタントハウスはassetsから取得する
	 */
	public static void setDefaultHouse() {
		
		String name = InstantHouseManager.DEFAULT_HOUSE;
		ResourceLocation rl = new ResourceLocation(YuzukiTools.MODID, name);
		Template template = InstantHouseManager.getTemplateToJar(rl);
		
		InstantHouseInfo info = new InstantHouseInfo(template, null);
		templateMap.put(name, info);
		
	}
	
	/**
	 * Template取得
	 * @param path
	 */
	public static Template getTemplate(String path) {
		Template template = null;
		if (templateMap.containsKey(path)) {
			template = templateMap.get(path).getTemplate();
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
			
			InstantHouseInfo info = templateMap.get(path);
			
			stack = new ItemStack(YKItems.INSTANT_HOUSE);
	    	NBTTagCompound nbt = new NBTTagCompound();
	    	nbt.setString("template", path);
	    	
	    	//表示名が設定されている場合
	    	if (!"".equals(info.getDisplay())) {
	    		NBTTagCompound nameTag = new NBTTagCompound();
	    		nameTag.setString("Name", info.getDisplay());
	    		nbt.setTag("display", nameTag);
	    	}
	    	
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
	
	/**
	 * アイコン表示用のアイテムスタックを取得する
	 * @param stack
	 * @return
	 */
	public static ItemStack getIconItemStack(ItemStack stack) {
		ItemStack iconStack = ItemStack.EMPTY;
		
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("template")) {
			String template = stack.getTagCompound().getString("template");
			if (templateMap.containsKey(template)) {
				iconStack = templateMap.get(template).getIconItemStack();
			}
		}
		return iconStack;
		
	}
}
