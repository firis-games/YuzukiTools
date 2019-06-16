package firis.core.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

import firis.yuzukitools.YuzukiTools;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;

public final class ShaderHelper {

	/**
	 * 
	 * @param shader
	 * @param callback
	 */
	public static void useShader(int shader) {
		
		if (!OpenGlHelper.shadersSupported) {
			return;
		}

		GlStateManager.disableLighting();

		ARBShaderObjects.glUseProgramObjectARB(shader);


		if(shader != 0) {
			int time = ARBShaderObjects.glGetUniformLocationARB(shader, "time");
			ARBShaderObjects.glUniform1iARB(time, 0);

			int alpha = ARBShaderObjects.glGetUniformLocationARB(shader, "alpha");
			ARBShaderObjects.glUniform1fARB(alpha, 0.4F);
		}
	}
	
	public static void releaseShader() {
		if (!OpenGlHelper.shadersSupported) {
			return;
		}
		GlStateManager.disableLighting();
		
		ARBShaderObjects.glUseProgramObjectARB(0);
	}
	
	/**
	 * glslシェーダ構築
	 * @param vert
	 * @param frag
	 * @return
	 */
	public static int createProgram(String vert, String frag) {
		int vertId = 0, fragId = 0, program;
		if(vert != null)
			vertId = createShader(vert, ARBVertexShader.GL_VERTEX_SHADER_ARB);
		if(frag != null)
			fragId = createShader(frag, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);

		program = ARBShaderObjects.glCreateProgramObjectARB();
		if(program == 0)
			return 0;

		if(vert != null)
			ARBShaderObjects.glAttachObjectARB(program, vertId);
		if(frag != null)
			ARBShaderObjects.glAttachObjectARB(program, fragId);

		ARBShaderObjects.glLinkProgramARB(program);
		if(ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
			//エラー
			return 0;
		}

		ARBShaderObjects.glValidateProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
			//エラー
			return 0;
		}

		return program;
	}
    
    private static int createShader(String filename, int shaderType){
		int shader = 0;
		try {
			shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

			if(shader == 0)
				return 0;

			ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
			ARBShaderObjects.glCompileShaderARB(shader);

			if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
				throw new RuntimeException("Error creating shader: ");

			return shader;
		}
		catch(Exception e) {
			ARBShaderObjects.glDeleteObjectARB(shader);
			e.printStackTrace();
			return -1;
		}
	}
    
    private static String readFileAsString(String filename) throws Exception {
		InputStream in = YuzukiTools.class.getResourceAsStream(filename);

		if(in == null)
			return "";

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
			return reader.lines().collect(Collectors.joining("\n"));
		}
    }
}
