package firis.core.common.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class ResourceHelper {

	/**
	 * 指定リソースパス配下にあるファイルリストを生成する
	 * eclipse起動時とjar起動時で挙動が異なる
	 * @param resource
	 * @return
	 */
	public static List<String> getResourceList(String resourcePath) {
		
		ClassLoader loader = FMLCommonHandler.instance().getClass().getClassLoader();
		URL resource = loader.getResource(resourcePath);
		String protocol = resource.getProtocol();
		
		List<String> resourceList = new ArrayList<>();
		
		//開発環境での実行
		if ("file".equals(protocol)) {
			//ファイル一覧を取得
			try {
				List<Path> filePathList = Files.walk(Paths.get(resource.toURI())).collect(Collectors.toList());
				String rootPath = resource.getPath();
				for (Path filePath : filePathList) {
					//ディレクトリは対象外とする
					if (Files.isDirectory(filePath)) {
						continue;
					}
					
					String path = filePath.toUri().getPath();
					
					//rootパスを削除
					path = path.replace(rootPath, "");
					path = resourcePath + path;
					
					resourceList.add(path);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		//jarでの起動
		} else if("jar".equals(protocol)){
			//jarからファイル一覧を取得
			try {
				JarURLConnection jarUrlConnection = null;
				jarUrlConnection = (JarURLConnection) resource.openConnection();
				JarFile jarFile = jarUrlConnection.getJarFile();
				
				Enumeration<JarEntry> entry = jarFile.entries(); 
				while (entry.hasMoreElements()) {
					JarEntry jarEntry = entry.nextElement();
					String jarPath = jarEntry.getName();
					//ディレクトリは対象外とする
					if (jarEntry.isDirectory()) {
						continue;
					}
					//resourcePathと前方一致する場合のみ取得
					if (jarPath.startsWith(resourcePath)) {
						resourceList.add(jarPath);
					}
				}
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return resourceList;
	}
	
	/**
	 * 指定パスのリソースを取得する
	 * @return
	 */
	public static String getResourceString(String resource) {
		String json = "";
		ClassLoader loader = FMLCommonHandler.instance().getClass().getClassLoader();
		
		InputStream stream = loader.getResourceAsStream(resource);
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		try {
			String line;
			while ((line = br.readLine()) != null) {
				json += line;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	/**
	 * Configフォルダ配下にあるファイルリストを取得する
	 * eclipse起動時とjar起動時で挙動が異なる
	 * @param resource
	 * @return
	 */
	public static List<String> getConfigFileList(String resourcePath) {
		
		List<String> resourceList = new ArrayList<>();
		
		try {
			//rootフォルダのチェック
			Path configRoot = Paths.get(System.getProperty("user.dir"), "config", "yuzukitools");
			if (!Files.isDirectory(configRoot)) {
				//フォルダ作成
				Files.createDirectories(configRoot);
			}
			//指定パスのディレクトリを確認
			Path config = Paths.get(configRoot.toString(), resourcePath);
			if (!Files.isDirectory(config)) {
				//フォルダ作成
				Files.createDirectories(config);
			}
			//フォルダの配下を検索する
			List<Path> filePathList = Files.walk(config).collect(Collectors.toList());
			for (Path filePath : filePathList) {
				//ディレクトリは対象外とする
				if (Files.isDirectory(filePath)) {
					continue;
				}
				
				String path = filePath.toUri().getPath();
				
				//rootパスを削除
				path = path.replace(configRoot.toUri().getPath(), "");
				
				resourceList.add(path);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resourceList;
	}
	
	/**
	 * 指定パスのリソースを取得する
	 * @return
	 */
	public static String getConfigFileString(String resource) {
		
		String json = "";
		try {
			//ファイルチェック
			Path filePath = Paths.get(System.getProperty("user.dir"), "config", "yuzukitools", resource);
			
			if (!Files.exists(filePath)) {
				return "";
			}
			//改行コードは無視
			List<String> fileList = Files.readAllLines(filePath);
			json = String.join("", fileList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
}
