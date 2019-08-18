package firis.core.common.helper;

import java.lang.reflect.Method;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToFindMethodException;

/**
 * Reflectionヘルパー
 * @author computer
 *
 */
public class ReflectionHelper {

    @Nonnull
    public static Method findMethod(@Nonnull Class<?> clazz, @Nonnull String methodName, @Nullable String methodObfName, Class<?>... parameterTypes)
    {
        Preconditions.checkNotNull(clazz);
        Preconditions.checkArgument(StringUtils.isNotEmpty(methodName), "Method name cannot be empty");

        String nameToFind;
        if (methodObfName == null || (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"))
        {
            nameToFind = methodName;
        }
        else
        {
            nameToFind = methodObfName;
        }

        Method m = null;
        while (clazz != null) {
        	try
            {
                m = clazz.getDeclaredMethod(nameToFind, parameterTypes);
                m.setAccessible(true);
                break;
            }
            catch (Exception e)
            {
            	clazz = clazz.getSuperclass();
            }
        }
        if (m == null) {
        	throw new UnableToFindMethodException(new NoSuchFieldException());
        }
        return m;
    }
    
    @Nonnull
    public static Method findMethod(@Nonnull Class<?> clazz, @Nonnull String methodName, @Nullable String methodObfName)
    {
        Preconditions.checkNotNull(clazz);
        Preconditions.checkArgument(StringUtils.isNotEmpty(methodName), "Method name cannot be empty");

        String nameToFind;
        if (methodObfName == null || (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"))
        {
            nameToFind = methodName;
        }
        else
        {
            nameToFind = methodObfName;
        }

        Method m = null;
        while (clazz != null) {
        	try
            {
                m = clazz.getDeclaredMethod(nameToFind);
                m.setAccessible(true);
                break;
            }
            catch (Exception e)
            {
            	clazz = clazz.getSuperclass();
            }
        }
        if (m == null) {
        	throw new UnableToFindMethodException(new NoSuchFieldException());
        }
        return m;
    }
	
}
