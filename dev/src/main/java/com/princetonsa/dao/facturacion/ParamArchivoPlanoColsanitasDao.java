package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public interface ParamArchivoPlanoColsanitasDao {
	
	/**
	 * Metodo para insertar la parametrizacion de archivos planos de colsanitas
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarArchivoPlanoColsanitas(Connection con,HashMap<String, Object> vo);
	
	/**
	 * Metodo que consulta las parametrizaciondes de archivos planos de colsanitas
	 * @param con
	 * @param convenio
	 * @return
	 */
	public HashMap consultarArchivoPlanoColsanitas(Connection con,int convenio);
	
	/**
	 * Metodo para modificar la parametrizacion de los archivos planos de colsanitas
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarArchivoPlanoColsanitas(Connection con,HashMap vo);
	
	/**
	 * Metodo para eliminar la parametrizacion de archivos planos de colsanitas
	 * @param con
	 * @param convenio
	 * @return
	 */
	public boolean eliminarArchivoPlanoColsanitas(Connection con,int convenio);

}
