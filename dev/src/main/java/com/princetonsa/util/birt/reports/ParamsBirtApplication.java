/*
 * @(#)ParamsBirtApplication.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.5.0_06
 *
 */
package com.princetonsa.util.birt.reports;

/**
 * Clase encargada de mantener los parametros principales 
 * para la generacion de los reportes con birt
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 *
 */
public class ParamsBirtApplication 
{
	/**
	 * path del birt runtime
	 */
	private static String birtRuntimePath;
	
	/**
	 * path de los reportes
	 */
	private static String reportsPath;
	
	/**
	 * path del birtViewer
	 */
	private static String birtViewerPath;
	
	/**
	 * path de los reportes (templates)
	 */
	private static String reportsPathTemp;
	
	/**
	 * driver
	 */
	private static String driver;
	
	/**
	 * url Bd
	 */
	private static String urlBD;
	
	/**
	 * usuario BD
	 */
	private static String userBD;
	
	/**
	 * password encriptado
	 */
	private static String password;
	
	/**
	 * resetea los atributos de la clase
	 *
	 */
	public static void reset()
	{
		birtRuntimePath="";
		reportsPath="";
		birtViewerPath="";
		reportsPathTemp="";
		driver="";
		urlBD="";
		userBD="";
	}

	/**
	 * @return Returns the birtRuntimePath.
	 */
	public static String getBirtRuntimePath() {
		return birtRuntimePath;
	}

	/**
	 * @param birtRuntimePath The birtRuntimePath to set.
	 */
	public static void setBirtRuntimePath(String birtRuntimePath) {
		ParamsBirtApplication.birtRuntimePath = birtRuntimePath;
	}

	/**
	 * @return Returns the birtViewerPath.
	 */
	public static String getBirtViewerPath() {
		return birtViewerPath;
	}

	/**
	 * @param birtViewerPath The birtViewerPath to set.
	 */
	public static void setBirtViewerPath(String birtViewerPath) {
		ParamsBirtApplication.birtViewerPath = birtViewerPath;
	}

	/**
	 * @return Returns the driver.
	 */
	public static String getDriver() {
		return driver;
	}

	/**
	 * @param driver The driver to set.
	 */
	public static void setDriver(String driver) {
		ParamsBirtApplication.driver = driver;
	}

	/**
	 * @return Returns the passwordEncrypted.
	 */
	public static String getPassword() {
		return password;
	}

	/**
	 * @param passwordEncrypted The passwordEncrypted to set.
	 */
	public static void setPassword(String password) {
		ParamsBirtApplication.password = password;
	}

	/**
	 * @return Returns the reportsPath.
	 */
	public static String getReportsPath() {
		return reportsPath;
	}

	/**
	 * @param reportsPath The reportsPath to set.
	 */
	public static void setReportsPath(String reportsPath) {
		ParamsBirtApplication.reportsPath = reportsPath;
	}

	/**
	 * @return Returns the reportsPathTemp.
	 */
	public static String getReportsPathTemp() {
		return reportsPathTemp;
	}

	/**
	 * @param reportsPathTemp The reportsPathTemp to set.
	 */
	public static void setReportsPathTemp(String reportsPathTemp) {
		ParamsBirtApplication.reportsPathTemp = reportsPathTemp;
	}

	/**
	 * @return Returns the urlBD.
	 */
	public static String getUrlBD() {
		return urlBD;
	}

	/**
	 * @param urlBD The urlBD to set.
	 */
	public static void setUrlBD(String urlBD) {
		ParamsBirtApplication.urlBD = urlBD;
	}

	/**
	 * @return Returns the userBD.
	 */
	public static String getUserBD() {
		return userBD;
	}

	/**
	 * @param userBD The userBD to set.
	 */
	public static void setUserBD(String userBD) {
		ParamsBirtApplication.userBD = userBD;
	}
	
	

}
