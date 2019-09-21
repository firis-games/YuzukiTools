package firis.core.common.helper;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathsHelper {

	/**
	 * Configフォルダ配下の絶対パスを取得する
	 * @param path
	 * @return
	 */
	public static Path getConfigPath(String path) {
		return Paths.get(System.getProperty("user.dir"), "config", "yuzukitools", path);
	}
	
}
