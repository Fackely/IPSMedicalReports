package util;

import java.io.File;
import java.net.URL;

/**
 * Clase encargada de resolver las rutas absolutas del Webinf del src
 * @author Juan Camilo Gaviria Acosta
 */
public class PathSolver {

	/**
	 * Obtiene la ruta absoluta del webinf
	 * @return
	 */
	public static String getWEBINFPath(){
		URL url=PathSolver.class.getResource("PathSolver.class");
		File f=new File(url.getPath()).getParentFile().getParentFile().getParentFile();
		return f.getAbsolutePath().replaceAll("%20", " ");
	}

	/**
	 * Obtiene la ruta absoluta del src
	 * @return
	 */
	public static String getSRCPath(){
		URL url=PathSolver.class.getResource("PathSolver.class");
		File f=new File(url.getPath()).getParentFile().getParentFile().getParentFile().getParentFile();
		return f.getAbsolutePath().replaceAll("%20", " ");
	}
}
