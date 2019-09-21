package firis.yuzukitools.common.world.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import firis.yuzukitools.YuzukiTools;
import firis.yuzukitools.YuzukiTools.YKItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class WorldGenHouse extends WorldGenerator {

	protected String template;
	protected EnumFacing facing;
	
	public WorldGenHouse() {
		this.template = "";
		this.facing = EnumFacing.NORTH;
	}
	
	public WorldGenHouse(String template, EnumFacing facing) {
		this.template = template;
		this.facing = facing;
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos position) {
		
		if ("".equals(this.template)) return false;
		
		//assetsからテンプレートを取得
		WorldServer worldserver = (WorldServer)world;
        TemplateManager templatemanager = worldserver.getStructureTemplateManager();
        Template template = templatemanager.getTemplate(null,
        		new ResourceLocation(YuzukiTools.MODID, this.template));
		
        PlacementSettings placementsettings =  new PlacementSettings();
        
        BlockPos pos = position;
        
        int facing_x = 7;
        int facing_z = 3;
        
        //位置調整
        //北（標準）
        switch (this.facing) {
        case NORTH:
            pos = position.up().north(facing_x).west(facing_z);
        	break;
        case SOUTH:
        	placementsettings.setRotation(Rotation.CLOCKWISE_180);
            pos = position.up().south(facing_x).east(facing_z);
        	break;
        case EAST:
        	placementsettings.setRotation(Rotation.CLOCKWISE_90);
            pos = position.up().north(facing_z).east(facing_x);
        	break;
        case WEST:
        	placementsettings.setRotation(Rotation.COUNTERCLOCKWISE_90);
            pos = position.up().south(facing_z).west(facing_x);
        	break;
       	default:
        }
        
        //構造体設置
        template.addBlocksToWorldChunk(world, pos, placementsettings);
        
        //アイテム追加
        this.insertChest(position, world);
        
		return true;
	}
	
	/**
	 * チェストへアイテムを仕込む
	 * @param position
	 * @param world
	 */
	public void insertChest(BlockPos position, World world) {
		
        //チェストにアイテムを仕込む
        BlockPos chestPos = position.up(2);
        
        int facing_x = 6;
        int facing_z = 2;
        
        //北（標準）
        switch (this.facing) {
        case NORTH:
        	chestPos = chestPos.north(facing_x).west(facing_z);
        	break;
        case SOUTH:
        	chestPos = chestPos.south(facing_x).east(facing_z);
        	break;
        case EAST:
        	chestPos = chestPos.north(facing_z).east(facing_x);
        	break;
        case WEST:
        	chestPos = chestPos.south(facing_z).west(facing_x);
        	break;
       	default:
        }
        
        //アイテムを追加する
        TileEntity tile = world.getTileEntity(chestPos);
        if (tile != null) {
        	IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        	if (handler != null) {
        		
        		List<ItemStack> stackList = new ArrayList<ItemStack>();
        		
        		//エンチャント
        		Enchantment efficiency = Enchantment.getEnchantmentByLocation("efficiency");
        		Enchantment unbreaking = Enchantment.getEnchantmentByLocation("unbreaking");
        		
        		ItemStack toolStack;
        		int randTool = 2;
        		//ツール
        		toolStack = new ItemStack(YKItems.STONE_HAMMERAXE);
        		if (world.rand.nextInt(randTool) == 0) {
	        		toolStack.addEnchantment(efficiency, 1);
	        		toolStack.addEnchantment(unbreaking, 1);
        		}
        		stackList.add(toolStack);
        		
        		//防具
        		stackList.add(new ItemStack(Items.LEATHER_HELMET));
        		stackList.add(new ItemStack(Items.LEATHER_CHESTPLATE));
        		stackList.add(new ItemStack(Items.LEATHER_LEGGINGS));
        		stackList.add(new ItemStack(Items.LEATHER_BOOTS));
        		
        		//防具に色を設定する
        		for (ItemStack stack : stackList) {
        			if (stack.getItem() instanceof ItemArmor) {
        				NBTTagCompound nbt = new NBTTagCompound();
        				NBTTagCompound color = new NBTTagCompound();
        				color.setInteger("color", 8991416); 
        				//紫色
        				nbt.setTag("display", color);
        				stack.setTagCompound(nbt);
        			}
        		}
        		
        		//チェストへアイテムを追加する
        		for (ItemStack stack : stackList) {
        			for (int slot = 0; slot < handler.getSlots(); slot++) {
                		stack = handler.insertItem(slot, stack, false);
                		if (stack.isEmpty()) {
                			break;
                		}
        			}
        		}
        	}
        }
	}

}
