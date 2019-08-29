package firis.yuzukitools.common.recipe.develop;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;

import firis.yuzukitools.api.recipe.RecipesKitchenGarden;
import firis.yuzukitools.common.recipe.json.JsonRecipesKitchenGarden;
import net.minecraft.launchwrapper.Launch;

public class CreateJsonRecipe {

	/**
	 * 開発環境のみで動かす
	 * @throws IOException 
	 */
	public static void create() {
		
		if (!(Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")) {
			//開発環境でない場合は何もしない
			return;
		}
		
		//家庭菜園のレシピを出力
		Path gardenPath = Paths.get("cstm_recipe", "kitchen_garden");
		
		//フォルダがない場合は生成
		try {
			if (!Files.isDirectory(gardenPath)) {
				Files.createDirectories(gardenPath);
			}
		} catch (IOException e) {
			System.out.println(e.toString());
			return;
		}
		
		//レシピを出力する
		for (RecipesKitchenGarden recipe : RecipesKitchenGarden.recipes) {
			
			String modid = recipe.getSeed().getItem().getRegistryName().getResourceDomain();
			JsonRecipesKitchenGarden jsonRecipe = JsonRecipesKitchenGarden.toJson(recipe);
			
			//json化
			String jsonStr = new GsonBuilder()
					.serializeNulls()
					.setPrettyPrinting()
					.disableHtmlEscaping()
					.create().toJson(jsonRecipe);
			
			//出力
			String fileName = jsonRecipe.seed.replace(":", "_") + ".json";
			List<String> jsonList = new ArrayList<>();
			jsonList.add(jsonStr);
			
			//Path
			Path dict = Paths.get("cstm_recipe", "kitchen_garden", modid);
			Path path = Paths.get("cstm_recipe", "kitchen_garden", modid, fileName);
			
			try {
				if (!Files.isDirectory(dict)) {
					Files.createDirectories(dict);
				}
				Files.write(path, jsonList, Charset.forName("UTF-8"), StandardOpenOption.CREATE_NEW);
			} catch (IOException e) {
			}
		}
	}
	
}
