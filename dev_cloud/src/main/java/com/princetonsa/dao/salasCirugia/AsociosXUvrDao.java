package com.princetonsa.dao.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.sqlbase.salasCirugia.SqlBaseAsociosXUvrDao;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public interface AsociosXUvrDao {
	
	/**
	 * Metodo que inserta en las tablas asocios_x_uvr y asocios_x_uvr_tipo_sala que son las dos maestras
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarAsociosUvrMaestro(Connection con,HashMap vo,boolean guardarXTipoAsocio);
	
	/**
	 * Metodo que inserta el detalle de los registros asocios por uvr el cual sirve si se inserta por convenio o esquema tarifario
	 * @param con
	 * @param vo
	 * @return
	 */
	public int insertarDetalleAsocioXUvr(Connection con, HashMap vo);
	
	/**
	 * Metodo que consulta el maestro de los asocios por uvr de tipo sala de donde se desprenderan los detalles sea por esquema tarifario o por convenio
	 * @param con
	 * @param esquemaTarifario
	 * @param convenio
	 * @return
	 */
	public HashMap consultarAsociosXUvrTipoSala(Connection con,int esquemaTarifario,int convenio,int codigoAsocioXUvr);
	
	/**
	 * Metodo que carga en un mapa la informacion del detalle de los asocios por uvr validando si viene por convenio o por esquema tarifario y por el codigo del asocio uvr para validar las vigencias cuando viene por convenio
	 * @param con
	 * @param codigoAsocioUvr
	 * @param tipoAsocio
	 * @param esquemaTarifario
	 * @param convenio
	 * @return
	 */
	public HashMap consultarDetalleAsociosXUvr(Connection con,int codigoAsocioUvr,int tipoAsocio,int esquemaTarifario,int convenio,String tipoServicio,int tipoAnestesia,String ocupacion,String especialidad,String tipoEspecialista,int tipoLiquidacion);
	
	/**
	 * Metodo que segun la accion sobre los codigos de tarifarios oficiales los inserta actualiza o elimina
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean actualizarDetCodigosAsociosXUvr(Connection con, HashMap<String, Object> vo);
	
	/**
	 * Metodo que carga un mapa con las vigencias existentes segun el convenio y el tipo de asocio
	 * @param con
	 * @param convenio
	 * @param tipoAsocio
	 * @return
	 */
	public HashMap consultarVigenciasXConvenio(Connection con,int convenio);
	
	public boolean insertarAsociosXUvrXTipoAsocio(Connection con,HashMap vo,int codigoAsocioUvr);
	
	/**
	 * Metodo para la actualizacion de los detalles de asocios por uvr
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarDetalle(Connection con,HashMap vo);
	
	/**
	 * Metodo para la eliminacion del detalle de asocios por uvr
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarDetalle(Connection con,int codigo);
	
	/**
	 * Metodo que elimina un registro del segundo maestro que es asocio por uvr por tipo de asocio
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarAsocioXUvrTipoAsocio(Connection con,int codigo);
	
	/**
	 * Metodo que elimina un asocio por uvr cuando este viene por convenio
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarAsocioUvrXConvenio(Connection con,int codigo);
	
	/**
	 * Metodo que modifica las vigencias por convenio
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarVigencias(Connection con,HashMap vo);

	
	/**
	 * Metodo encargado de mopdificar asocios por sala
	 * @param connection
	 * @param tipoAsocio
	 * @param tipoSala
	 * @param codigo
	 * @return
	 */
	public boolean modificarAsociosUvrSala (Connection connection,String tipoAsocio, String tipoSala, String codigo,String liquidarPor);
	
	
	
}
