package firis.core.common.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import com.google.common.base.Optional;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class JsonHelper {

	
	/**
	 * ItemStackを文字列へ変換する
	 * NBTは非対応
	 */
	public static String toString(@Nonnull ItemStack stack) {
		
		ResourceLocation item = Item.REGISTRY.getNameForObject(stack.getItem());
		Integer meta = -1;
		Integer count = stack.getCount();
		
		//Metadata
		if (stack.getItem().getHasSubtypes()) {
			meta = stack.getMetadata();
		}
		
		//文字列化
		String itemStr = item.toString();
		if (meta != -1) {
			itemStr += ":" + meta.toString();
		}
		
		if (count != 1) {
			itemStr += "*" + count.toString();
		}
		
		return itemStr;
		
	}
	
	/**
	 * IBlockStateを文字列へ変換する
	 * @param state
	 * @return
	 */
	public static String toString(@Nonnull IBlockState state) {
		
		ResourceLocation block = Block.REGISTRY.getNameForObject(state.getBlock());
		
		List<String> propList = new ArrayList<String>();
		//IPropertyを文字列化
		for (Entry<IProperty<?>, Comparable<?>> entry : state.getProperties().entrySet()) {
			 IProperty<?> iproperty = entry.getKey();
			 Comparable<?> ipropertyvalue = entry.getValue();
			 
			 String prop = iproperty.getName();
			 String propValue = getIPropertyName(iproperty, ipropertyvalue);
			 propList.add(prop + "=" + propValue);
		}
		
		//文字列化
		String blockStr = block.toString();
		if (propList.size() >= 0) {
			blockStr += "[" + String.join(",", propList) + "]";
		}
		
		return blockStr;
	}

	
	/**
	 * 文字列からItemStackへ変換する
	 * @param itemstack
	 * @return
	 * @throws JsonHelperException 
	 */
	public static ItemStack fromStringItemStack(String itemstack) throws JsonHelperException {
		
		String modid = "";
		String itemid = "";
		Integer metadata = 0;
		Integer count = 0;
		
		String work = itemstack;
		boolean error = false;

		String[] counts = work.split("\\*");
		//countの分割
		if (counts.length == 1) {
			//count設定なし
			count = 1;
		} else if (counts.length == 2){
			//count設定あり
			try {
				count = Integer.parseInt(counts[1]);
			} catch(NumberFormatException e) {
				error = true;
			}
			work = counts[0];
		} else {
			error = true;			
		}

		String[] items = work.split(":");
		
		if (items.length == 2) {
			//metadataなし
			modid = items[0];
			itemid = items[1];
		} else if (items.length == 3){
			//metadataあり
			modid = items[0];
			itemid = items[1];
			try {
				metadata = Integer.parseInt(items[2]);
			} catch(NumberFormatException e) {
				error = true;
			}
		} else {
			error = true;
		}
		
		if (error) throw new JsonHelperException("fromStringItemStack");
		
		ResourceLocation rl = new ResourceLocation(modid, itemid);
		Item item = Item.REGISTRY.getObject(rl);
		
		if (item == null) throw new JsonHelperException("fromStringItemStack");
		
		return new ItemStack(item, count, metadata);
	}
	
	/**
	 * 文字列からIBlockStateへ変換する
	 * @param blockstate
	 * @return
	 * @throws JsonHelperException 
	 */
	public static IBlockState fromStringBlockState(String blockstate) throws JsonHelperException {
		
		String modid = "";
		String blockid = "";
		String propertys = "";
		
		String work = blockstate;
		boolean error = false;
		
		//propertyの取得
		String[] props = work.split("\\[");
		if (props.length == 1) {
			//propertyなし
			work = props[0];
		} else if (props.length == 2) {
			//propertyあり
			String[] propsAft = (props[1] + " ").split("\\]");
			if (propsAft.length == 2) {
				propertys = propsAft[0];
				work = props[0];
			} else {
				error = true;
			}
		} else {
			error = true;
		}
		
		//Blockの取得
		String[] blocks = work.split(":");
		if (blocks.length == 2) {
			modid = blocks[0];
			blockid = blocks[1];
		} else {
			error = true;
		}
		
		//エラーの場合はnull
		if (error == true) throw new JsonHelperException("fromStringItemStack");
		
		ResourceLocation rl = new ResourceLocation(modid, blockid);
		Block block = Block.REGISTRY.getObject(rl);
		
		if (block == null)  throw new JsonHelperException("fromStringItemStack");
		
		IBlockState state = block.getDefaultState();
		
		//propertyがある場合
		if (!"".equals(propertys)) {
			
			for (String property : propertys.split(",")) {
				
				String[] entrys = property.split("=");
				if (entrys.length != 2) {
					error = true;
					break;
				}
				
				String propKey = entrys[0];
				String propValue = entrys[1];
				
				//プロパティオブジェクト取得
				IProperty<?> propObj = block.getBlockState().getProperty(propKey);
				if (propObj == null) {
					error = true;
					break;
				}
				state = withStringProperty(state, propObj, propValue);
			}
		}
		
		if (error == true) throw new JsonHelperException("fromStringItemStack");;
		
		return state;
	}
	
	@SuppressWarnings("unchecked")
	private static <T extends Comparable<T>> String getIPropertyName(IProperty<T> iproperty, Comparable<?> value) {
		return iproperty.getName((T) value);
	}

	/**
	 * 文字列型のPropertyをIBlockStateへ設定する
	 * @param state
	 * @param iproperty
	 * @param value
	 * @return
	 * @throws JsonHelperException 
	 */
	private static <T extends Comparable<T>> IBlockState withStringProperty(IBlockState state, IProperty<T> iproperty, String value) throws JsonHelperException {
		try {
			Optional<T> propValueObj = iproperty.parseValue(value);
			return state.withProperty(iproperty, propValueObj.get());			
		} catch (Exception e){
			throw new JsonHelperException("withStringProperty");
		}
	}
	
	
	/**
	 * JsonHelperのException
	 * @author computer
	 *
	 */
	public static class JsonHelperException extends Exception {
		
		private static final long serialVersionUID = 1L;

		public JsonHelperException(String message) {
			super(message);
		}
	}
}
