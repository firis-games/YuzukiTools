package firis.yuzukitools;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.logging.log4j.Logger;

import firis.core.client.ShaderHelper;
import firis.yuzukitools.client.tesr.YKTileInstantHouseSpRenderer;
import firis.yuzukitools.common.block.YKBlockBackpack;
import firis.yuzukitools.common.block.YKBlockElectricFurnace;
import firis.yuzukitools.common.block.YKBlockInstantHouse;
import firis.yuzukitools.common.block.YKBlockSolarCharger;
import firis.yuzukitools.common.item.YKItemBlockBackpack;
import firis.yuzukitools.common.item.YKItemRedstoneArmor;
import firis.yuzukitools.common.item.YKItemRedstoneAxe;
import firis.yuzukitools.common.item.YKItemRedstoneHoe;
import firis.yuzukitools.common.item.YKItemRedstonePickaxe;
import firis.yuzukitools.common.item.YKItemRedstoneShovel;
import firis.yuzukitools.common.item.YKItemShieldSword;
import firis.yuzukitools.common.item.YKItemToolHammeraxe;
import firis.yuzukitools.common.network.NetworkHandler;
import firis.yuzukitools.common.proxy.IProxy;
import firis.yuzukitools.common.proxy.ModGuiHandler;
import firis.yuzukitools.common.recipe.RecipesElectricFurnace;
import firis.yuzukitools.common.tileentity.YKTileBackpack;
import firis.yuzukitools.common.tileentity.YKTileElectricFurnace;
import firis.yuzukitools.common.tileentity.YKTileInstantHouse;
import firis.yuzukitools.common.tileentity.YKTileSolarCharger;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(
		modid = YuzukiTools.MODID, 
		name = YuzukiTools.NAME,
		version = YuzukiTools.VERSION,
		dependencies = YuzukiTools.MOD_DEPENDENCIES,
		acceptedMinecraftVersions = YuzukiTools.MOD_ACCEPTED_MINECRAFT_VERSIONS
)
@EventBusSubscriber
public class YuzukiTools
{
    public static final String MODID = "yuzukitools";
    public static final String NAME = "Yuzuki Tools";
    public static final String VERSION = "0.1";
    public static final String MOD_DEPENDENCIES = "required-after:forge@[1.12.2-14.23.5.2768,);after:jei@[1.12.2-4.13.1.220,)";
    public static final String MOD_ACCEPTED_MINECRAFT_VERSIONS = "[1.12.2]";

    private static Logger logger;
    
    @Instance(YuzukiTools.MODID)
    public static YuzukiTools INSTANCE;
    
    @SidedProxy(serverSide = "firis.yuzukitools.common.proxy.CommonProxy", 
    		clientSide = "firis.yuzukitools.client.proxy.ClientProxy")
    public static IProxy proxy;
    
    /**
     * クリエイティブタブ
     */
    public static final CreativeTabs YKCreativeTab = new CreativeTabs("tabYuzukiTools") {
    	@SideOnly(Side.CLIENT)
    	@Override
        public ItemStack getTabIconItem()
        {
            return new ItemStack(YKItems.YUZUKI_MEDAL);
        }
    };
    
    /**
     * アイテムインスタンス保持用
     */
    @ObjectHolder(YuzukiTools.MODID)
    public static class YKItems{
    	public final static Item YUZUKI_MEDAL = null;
    	public final static Item WOOD_HAMMERAXE = null;
    	public final static Item STONE_HAMMERAXE = null;
    	public final static Item IRON_HAMMERAXE = null;
    	public final static Item GOLD_HAMMERAXE = null;
    	public final static Item DIAMOND_HAMMERAXE = null;
    	public final static Item WOOD_SHIELD_SWORD = null;
    	public final static Item STONE_SHIELD_SWORD = null;
    	public final static Item IRON_SHIELD_SWORD = null;
    	public final static Item GOLD_SHIELD_SWORD = null;
    	public final static Item DIAMOND_SHIELD_SWORD = null;
    	public final static Item STONE_SHEARS = null;
    	public final static Item REDSTONE_INGOT = null;
    	public final static Item REDSTONE_PICKAXE = null;
    	public final static Item REDSTONE_AXE = null;
    	public final static Item REDSTONE_SHOVEL = null;
    	public final static Item REDSTONE_HOE = null;
    	public final static Item REDSTONE_HELMET = null;
    	public final static Item REDSTONE_CHESTPLATE = null;
    	public final static Item REDSTONE_LEGGINGS = null;
    	public final static Item REDSTONE_BOOTS = null;
    }
    
    /**
     * ブロックインスタンス保持用
     */
    @ObjectHolder(YuzukiTools.MODID)
    public static class YKBlocks{
    	public final static Block INSTANT_HOUSE = null;
    	public final static Block BACKPACK = null;
    	public final static Block SOLAR_CHARGER = null;
    	public final static Block ELECTRIC_FURNACE = null;
    }
    
    
    /**
     * GLSL
     */
    public static int shader_alpha = 0;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        
        logger.info("YuzukiTools Starting...");
        
        GameRegistry.registerTileEntity(YKTileInstantHouse.class, 
        		new ResourceLocation(YuzukiTools.MODID, "te_instant_house"));
        
        GameRegistry.registerTileEntity(YKTileBackpack.class, 
        		new ResourceLocation(YuzukiTools.MODID, "te_backpack"));
        
        GameRegistry.registerTileEntity(YKTileSolarCharger.class, 
        		new ResourceLocation(YuzukiTools.MODID, "te_solar_charger"));
        
        GameRegistry.registerTileEntity(YKTileElectricFurnace.class, 
        		new ResourceLocation(YuzukiTools.MODID, "te_electric_furnace"));
        
        //ネットワーク登録
        NetworkHandler.init();
                
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	//GUI登録
    	NetworkRegistry.INSTANCE.registerGuiHandler(YuzukiTools.INSTANCE, new ModGuiHandler());
    	
    	//キーバインディング登録
    	proxy.registerKeyBinding();
    	
    	//レイヤー登録
    	proxy.initLayerRenderer();
    	
    	//電気炉レシピ登録
    	RecipesElectricFurnace.init();
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    }
    
    /**
     * ブロックを登録するイベント
     */
    @SubscribeEvent
    protected static void registerBlocks(RegistryEvent.Register<Block> event)
    {
    	// インスタントハウス
        event.getRegistry().register(
                new YKBlockInstantHouse()
                .setRegistryName(MODID, "instant_house")
                .setUnlocalizedName("instant_house")
        );
        
    	// バックパック
        event.getRegistry().register(
                new YKBlockBackpack()
                .setRegistryName(MODID, "backpack")
                .setUnlocalizedName("backpack")
        );
        
    	// ソーラー充電器
        event.getRegistry().register(
                new YKBlockSolarCharger()
                .setRegistryName(MODID, "solar_charger")
                .setUnlocalizedName("solar_charger")
        );
        
        // 電気炉
        event.getRegistry().register(
                new YKBlockElectricFurnace()
                .setRegistryName(MODID, "electric_furnace")
                .setUnlocalizedName("electric_furnace")
        );
    }
    
    /**
     * アイテムを登録するイベント
     */
    @SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event)
    {
    	//結月メダル
    	event.getRegistry().register(new Item()
    			.setRegistryName(MODID, "yuzuki_medal")
    			.setUnlocalizedName("yuzuki_medal")
    			.setCreativeTab(YKCreativeTab)
    			.setFull3D());
    	
    	//木のハンマーアックス
    	event.getRegistry().register(new YKItemToolHammeraxe(ToolMaterial.WOOD)
    			.setRegistryName(MODID, "wood_hammeraxe")
    			.setUnlocalizedName("wood_hammeraxe"));
    	
    	//石のハンマーアックス
    	event.getRegistry().register(new YKItemToolHammeraxe(ToolMaterial.STONE)
    			.setRegistryName(MODID, "stone_hammeraxe")
    			.setUnlocalizedName("stone_hammeraxe"));
    	
    	//鉄のハンマーアックス
    	event.getRegistry().register(new YKItemToolHammeraxe(ToolMaterial.IRON)
    			.setRegistryName(MODID, "iron_hammeraxe")
    			.setUnlocalizedName("iron_hammeraxe"));
    	
    	//金のハンマーアックス
    	event.getRegistry().register(new YKItemToolHammeraxe(ToolMaterial.GOLD)
    			.setRegistryName(MODID, "gold_hammeraxe")
    			.setUnlocalizedName("gold_hammeraxe"));
    	
    	//ダイヤのハンマーアックス
    	event.getRegistry().register(new YKItemToolHammeraxe(ToolMaterial.DIAMOND)
    			.setRegistryName(MODID, "diamond_hammeraxe")
    			.setUnlocalizedName("diamond_hammeraxe"));
    	
    	//木のシールドソード
    	event.getRegistry().register(new YKItemShieldSword(ToolMaterial.WOOD)
    			.setRegistryName(MODID, "wood_shield_sword")
    			.setUnlocalizedName("wood_shield_sword"));

    	//石のシールドソード
    	event.getRegistry().register(new YKItemShieldSword(ToolMaterial.STONE)
    			.setRegistryName(MODID, "stone_shield_sword")
    			.setUnlocalizedName("stone_shield_sword"));
    	
    	//鉄のシールドソード
    	event.getRegistry().register(new YKItemShieldSword(ToolMaterial.IRON)
    			.setRegistryName(MODID, "iron_shield_sword")
    			.setUnlocalizedName("iron_shield_sword"));
    	
    	//金のシールドソード
    	event.getRegistry().register(new YKItemShieldSword(ToolMaterial.GOLD)
    			.setRegistryName(MODID, "gold_shield_sword")
    			.setUnlocalizedName("gold_shield_sword"));
    	
    	//ダイヤのシールドソード
    	event.getRegistry().register(new YKItemShieldSword(ToolMaterial.DIAMOND)
    			.setRegistryName(MODID, "diamond_shield_sword")
    			.setUnlocalizedName("diamond_shield_sword"));
    	
    	//石のハサミ
    	event.getRegistry().register(new ItemShears()
    			.setRegistryName(MODID, "stone_shears")
    			.setUnlocalizedName("stone_shears")
    			.setCreativeTab(YuzukiTools.YKCreativeTab)
    			.setMaxDamage(32));

    	//バックパック
    	event.getRegistry().register(new YKItemBlockBackpack()
    			.setRegistryName(MODID, "backpack"));
    	
    	//インスタントハウス
    	event.getRegistry().register(new ItemBlock(YKBlocks.INSTANT_HOUSE)
    			.setRegistryName(MODID, "instant_house"));
    	
    	//レッドストーンインゴット
    	event.getRegistry().register(new Item()
    			.setRegistryName(MODID, "redstone_ingot")
    			.setUnlocalizedName("redstone_ingot")
    			.setCreativeTab(YKCreativeTab));
    	
    	//レッドストーンのツルハシ
    	event.getRegistry().register(new YKItemRedstonePickaxe(ToolMaterial.IRON)
    			.setRegistryName(MODID, "redstone_pickaxe")
    			.setUnlocalizedName("redstone_pickaxe"));
    	
    	//レッドストーンの斧
    	event.getRegistry().register(new YKItemRedstoneAxe(ToolMaterial.IRON)
    			.setRegistryName(MODID, "redstone_axe")
    			.setUnlocalizedName("redstone_axe"));
    	
    	//レッドストーンのシャベル
    	event.getRegistry().register(new YKItemRedstoneShovel(ToolMaterial.IRON)
    			.setRegistryName(MODID, "redstone_shovel")
    			.setUnlocalizedName("redstone_shovel"));

    	//レッドストーンのクワ
    	event.getRegistry().register(new YKItemRedstoneHoe(ToolMaterial.IRON)
    			.setRegistryName(MODID, "redstone_hoe")
    			.setUnlocalizedName("redstone_hoe"));
    	
    	//ソーラー充電器
    	event.getRegistry().register(new ItemBlock(YKBlocks.SOLAR_CHARGER)
    			.setRegistryName(MODID, "solar_charger"));
    	
    	//電気炉
    	event.getRegistry().register(new ItemBlock(YKBlocks.ELECTRIC_FURNACE)
    			.setRegistryName(MODID, "electric_furnace"));
    	
    	//レッドストーンのヘルメット
    	event.getRegistry().register(new YKItemRedstoneArmor(EntityEquipmentSlot.HEAD)
    			.setRegistryName(MODID, "redstone_helmet")
    			.setUnlocalizedName("redstone_helmet"));
    	
    	//レッドストーンのチェストプレート
    	event.getRegistry().register(new YKItemRedstoneArmor(EntityEquipmentSlot.CHEST)
    			.setRegistryName(MODID, "redstone_chestplate")
    			.setUnlocalizedName("redstone_chestplate"));
    	
    	//レッドストーンのレギンス
    	event.getRegistry().register(new YKItemRedstoneArmor(EntityEquipmentSlot.LEGS)
    			.setRegistryName(MODID, "redstone_leggings")
    			.setUnlocalizedName("redstone_leggings"));
    	
    	//レッドストーンのブーツ
    	event.getRegistry().register(new YKItemRedstoneArmor(EntityEquipmentSlot.FEET)
    			.setRegistryName(MODID, "redstone_boots")
    			.setUnlocalizedName("redstone_boots"));
    	
    }
    
    /**
     * モデル登録イベント
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    protected static void registerModels(ModelRegistryEvent event)
    {
    	//YKItemsから自動でModelを登録する(メタデータは非対応)
    	for (Field filed : YKItems.class.getFields()) {
    		try {
    			int mod = filed.getModifiers();
    			//final かつ static
    			if (Modifier.isFinal(mod) && Modifier.isStatic(mod)) {
    				if(filed.get(null) instanceof Item) {
    					Item regItem = (Item) filed.get(null);
    					ModelLoader.setCustomModelResourceLocation(regItem, 0,
    			    			new ModelResourceLocation(regItem.getRegistryName(), "inventory"));
    				}
    			}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.info("registerModels Item error " + filed.getName());
			}
    	}
    	
    	//YKBlocksから自動でModelを登録する(メタデータは非対応)
    	for (Field filed : YKBlocks.class.getFields()) {
    		try {
    			int mod = filed.getModifiers();
    			//final かつ static
    			if (Modifier.isFinal(mod) && Modifier.isStatic(mod)) {
    				if(filed.get(null) instanceof Block) {
    					Block regBlock = (Block) filed.get(null);
    					ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(regBlock), 0,
    			    			new ModelResourceLocation(regBlock.getRegistryName(), "inventory"));
    				}
    			}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.info("registerModels Block error " + filed.getName());
			}
    	}

    	//インスタントハウスTESR
    	ClientRegistry.bindTileEntitySpecialRenderer(YKTileInstantHouse.class, new YKTileInstantHouseSpRenderer());
    	
    	//GLSLロード
    	if (Minecraft.getMinecraft().getResourceManager() 
    			instanceof SimpleReloadableResourceManager) {
	    	if(OpenGlHelper.shadersSupported) {
	    		shader_alpha = ShaderHelper.createProgram(
	    				"/assets/yuzukitools/shader/alpha.vert", 
	    				"/assets/yuzukitools/shader/alpha.frag");
	    	}
    	}
    }
    
}
