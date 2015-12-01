package com.princetonsa.mundo;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.NewMenuDao;
import com.princetonsa.dto.administracion.DtoModulo;

/**
 * Menu del Sistema
 * @author GIO
 */
public class NewMenu
{
	/**
	 * logger
	 */
	private static Logger logger = Logger.getLogger(NewMenu.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static NewMenuDao newMenuDao;
	
	/**
	 * Codigo de la Institución
	 */
	private String institucion;
	
	/**
	 * Login Usuario
	 */
	private String usuario;
	
	/**
	 * Modulos del Sistema
	 */
	private ArrayList<DtoModulo> modulos;
	
	/**
	 * Constructor
	 */
	public NewMenu()
	{
		this.reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * Reset
	 */
	public void reset() 
	{
		this.institucion="";
		this.usuario="";
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			newMenuDao = myFactory.getNewMenuDao();
			wasInited = (newMenuDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public static ArrayList<DtoModulo> cargar(Connection con, String loginUsuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNewMenuDao().cargar(con,loginUsuario);
	}
	
	/**
	 * 
	 * @param newMenu
	 * @return
	 */
	public static String crearJSCookMenu(ArrayList<DtoModulo> newMenu) {
		boolean ponerComaMod = false;
		boolean ponerComaFun = false;
		
		String menu = "[";
		
		// Se iteran los modulos
		for(int mod=0; mod<newMenu.size(); mod++){
			
			if(ponerComaMod)
				menu+=",";
			
			menu += "['','"+newMenu.get(mod).getNombre()+"','','','',";
			
			// Se iteran las funcionalidaddes
			ponerComaFun=false;
			for(int fun=0; fun<newMenu.get(mod).getFuncionalidades().size(); fun++){
				
				if(ponerComaFun)
					menu+=",";
				
				menu += "['','"+newMenu.get(mod).getFuncionalidades().get(fun).getEtiqueta()+"','../"+newMenu.get(mod).getFuncionalidades().get(fun).getArchivo()+"','_top', '']";
			
				ponerComaFun=true;
			}
				
			menu += "]";
			
			ponerComaMod=true;
		}
		
		// Se adiciona el modulo opciones
		menu += ",['','Opciones','','','', " +
				"['<img src=\"../imagenes/lock.png\">','Cambiar Contraseña','cambiarPwd.jsp','_top',''], " +
				"['<img src=\"../imagenes/logout.png\">','Salir','../logout.jsp','_top','']]";
		
		menu+="]";
		return menu;
	}

	/**
	 * @return the newMenuDao
	 */
	public static NewMenuDao getNewMenuDao() {
		return newMenuDao;
	}

	/**
	 * @param newMenuDao the newMenuDao to set
	 */
	public static void setNewMenuDao(NewMenuDao newMenuDao) {
		NewMenu.newMenuDao = newMenuDao;
	}

	/**
	 * @return the institucion
	 */
	public String getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the modulos
	 */
	public ArrayList<DtoModulo> getModulos() {
		return modulos;
	}

	/**
	 * @param modulos the modulos to set
	 */
	public void setModulos(ArrayList<DtoModulo> modulos) {
		this.modulos = modulos;
	}
	
}	