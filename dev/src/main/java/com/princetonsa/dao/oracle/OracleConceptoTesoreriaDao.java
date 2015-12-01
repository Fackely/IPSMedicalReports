
/*
 * Creado   Junio 20 2005
 * Modificado Sep 27 2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dao.ConceptoTesoreriaDao;
import com.princetonsa.dao.sqlbase.SqlBaseConceptoTesoreriaDao;

/**
 * @author Sebasti�n G�mez
 *
 * Clase que maneja los m�todos prop�os de Oracle para el acceso a la fuente
 * de datos en la funcionalidad Conceptos Ingreso Tesorer�a
 */
public class OracleConceptoTesoreriaDao implements ConceptoTesoreriaDao 
{

    /**
	 * metodo para insertar conceptos de ingreso tesoreria.
	 * @param con Connection, conexi�n con la fuente de datos
	 * @param codigoInstitucion int, codigo de la institucion.
	 * @param codigo String, codigo del concepto
	 * @param descripcion String, descripci�n del concepto
	 * @param codTipoIngreso int, c�digo del tipo de concepto
	 * @param codFiltro int, filtro del tipo de concepto
	 * @param cuenta double, c�digo de la cuenta
	 * @param tipoDocum, c�digo del tipo de documento
	 * @param centroCosto int
	 * @param nit int
	 * @param activo boolean
	 * @return boolean, true efectivo, false de lo contrario.
	 *
	public boolean insertar(Connection con, int codigoInstitucion, String codigo, String descripcion, int codTipoIng, int codFiltro, float cuenta, int tipoDocum, int centroCosto, int nit, boolean activo)
	{
	   return SqlBaseConceptoTesoreriaDao.insertar(con,codigoInstitucion,codigo,descripcion,codTipoIng,codFiltro,cuenta,tipoDocum,centroCosto,nit,activo);
	}
	
	/**
	 * metodo para realizar la consulta del concepto ingreso tesoreria.
	 * @param con Connection, conexi�n con la fuente de datos
	 * @param codInstitucion
	 * @return ResultSet
	 *
	public ResultSetDecorator consultar(Connection con, int codInstitucion)
	{
	    return SqlBaseConceptoTesoreriaDao.consultar(con,codInstitucion);  
	}
	
	/**
	 * metodo para modificar conceptos de ingreso tesoreria.
	 * @param con Connection, conexi�n con la fuente de datos
	 * @param codigo String, codigo del concepto
	 * @param codigoOld String, c�digo original.
	 * @param descripcion String, descripci�n del concepto
	 * @param codTipo int, c�digo del tipo de concepto
	 * @param codCuenta double, c�digo de la cuenta
	 * @param porcentaje double, porcentaje del concepto
	 * @param institucion int, c�digo de la instituci�n
	 * @return boolean, true efectivo, false de lo contrario.
	 * *
	public boolean modificar (Connection con,
								int codigoInstitucion,
								String codigo,
								String codigoOld,
								String descripcion, 
								int codTipo,
								int codFiltro,
								double cuenta, 
								int tipoDocum,
								int centroCosto,
								int nit, boolean activo)
	{
	    return SqlBaseConceptoTesoreriaDao.modificar(con,
				codigoInstitucion,
				codigo,
				codigoOld,
				descripcion, 
				codTipo,
				codFiltro,
				cuenta, 
				tipoDocum,
				centroCosto,
				nit, activo); 
	}
	
	/**
	 * metodo para eliminar concepto ingreso tesoreria
	 * @param con Connection, conexi�n con la fuente de datos
	 * @param codigo String, c�digo del concepto
	 * @param institucion int, c�digo de la instituci�n
	 * @return boolean, true efectivo.
	 *
	public boolean eliminar(Connection con, String codigo,int institucion)
	{
	    return SqlBaseConceptoTesoreriaDao.eliminar(con,codigo,institucion);  
	}
	
	
	/**
	 * metodo para buscar conceptos ingreso tesoreria.
	 * @param con Connection, conexi�n con la fuente de datos
	 * @param codigo String, codigo del concepto
	 * @param codigoOld String, c�digo original.
	 * @param descripcion String, descripci�n del concepto
	 * @param codTipo int, c�digo del tipo de concepto
	 * @param codCuenta double, c�digo de la cuenta
	 * @param porcentaje double, porcentaje del concepto
	 * @param institucion int, c�digo de la instituci�n
	 * @return boolean, true efectivo, false de lo contrario.
	 *
	public ResultSetDecorator busquedaAvanzada (Connection con, int codigoInstitucion, String codigo, String descripcion, int codigoTipo, int codigoFiltro, double cuenta, int tipoDocumento, int centroCosto, int nit, boolean activo)
	{
	    return SqlBaseConceptoTesoreriaDao.busquedaAvanzada(con,codigoInstitucion,codigo,descripcion,codigoTipo,codigoFiltro,cuenta,tipoDocumento,centroCosto,nit,activo);
		 
	}*/
	
	/**
	 * M�todo usado para cargar los conceptos de ingreso de tesorer�a 
	 * parameteizados por instituci�n
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarConceptosTesoreria(Connection con,int institucion)
	{
		return SqlBaseConceptoTesoreriaDao.cargarConceptosTesoreria(con,institucion);
	}
	
	/**
	 * 
	 */
	public String cargarTipoPagoEspecial (Connection con, int codigo)
	{
		return SqlBaseConceptoTesoreriaDao.cargarTipoPagoEspecial(con, codigo);
	}
	
	/**
	 * M�todo usado para ingresar un nuevo concepto de tesoreria
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param valor
	 * @param cuenta
	 * @param docum_ingreso
	 * @param docum_anulacion
	 * @param centroCosto
	 * @param nit
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public int insertarConceptoTesoreria (
			Connection con, 
			String codigo, 
			String descripcion, 
			int tipo, 
			String valor,
			String cuenta, 
			int documIngreso, 
			int documAnulacion,
			int centroCosto, 
			int nit, 
			boolean activo,
			int institucion,String rubroPresupuestal)
	{
		return SqlBaseConceptoTesoreriaDao.insertarConceptoTesoreria(con,codigo,descripcion,tipo,valor,cuenta,documIngreso,documAnulacion,centroCosto,nit,activo,institucion,rubroPresupuestal);
	}
	
	/**
	 * M�todo usado para actualizar un registro de conceptos ingreso tesorer�a
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param valor
	 * @param cuenta
	 * @param documIngreso
	 * @param documAnulacion
	 * @param centroCosto
	 * @param nit
	 * @param activo
	 * @param codigoAntiguo
	 * @param institucion
	 * @return
	 */
	public int actualizarConceptoTesoreria (Connection con,
			String codigo, 
			String descripcion, 
			int tipo, 
			String valor,
			String cuenta, 
			int documIngreso, 
			int documAnulacion,
			int centroCosto, 
			int nit, 
			boolean activo,
			String codigoAntiguo,
			int institucion,String rubroPresupuestal)
	{
		return SqlBaseConceptoTesoreriaDao.actualizarConceptoTesoreria(con,codigo,descripcion,tipo,valor,cuenta,documIngreso,documAnulacion,centroCosto,nit,activo,codigoAntiguo,institucion,rubroPresupuestal);
	}
	
	/**
	 * M�todo para eliminar un concepto de ingreso tesoreria
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public int eliminarConceptoTesoreria(Connection con,String codigo,int institucion)
	{
		return SqlBaseConceptoTesoreriaDao.eliminarConceptoTesoreria(con,codigo,institucion);
	}
	
	/**
	 * M�todo usado para cargar un registro de los conceptos de ingreso de tesoreria
	 * por su c�digo y la instituci�n
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public HashMap cargarConceptoTesoreria(Connection con,String codigo,int institucion)
	{
		return SqlBaseConceptoTesoreriaDao.cargarConceptoTesoreria(con,codigo,institucion);
	}
	
	/**
	 * M�todo usado para la b�squeda avanzada de los conceptos de tesorer�a
	 * en la opci�n de consulta
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param valor
	 * @param cuenta
	 * @param documIngreso
	 * @param documAnulacion
	 * @param centroCosto
	 * @param nit
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public HashMap busquedaConceptosTesoreria(
			Connection con,
			String codigo,
			String descripcion,
			int tipo,
			String valor,
			String cuenta,
			int documIngreso,
			int documAnulacion,
			int centroCosto,
			int nit,
			String activo,
			int institucion)
	{
		return SqlBaseConceptoTesoreriaDao.busquedaConceptosTesoreria(con,codigo,descripcion,tipo,valor,cuenta,documIngreso,documAnulacion,centroCosto,nit,activo,institucion);
	}
	
	/**
	 * M�todo implementado para verificar que el concepto de ingreso
	 * tesorer�a no se est� utilizando en otras funcionalidades
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public boolean revisarUsoConcepto(Connection con,String codigo,int institucion)
	{
		return SqlBaseConceptoTesoreriaDao.revisarUsoConcepto(con,codigo,institucion);
	}
	
	/**
	 * M�todo usado para la b�squeda avanzada de los registros vinculados
	 * con el ingreso/modificaci�n de los conceptos de ingreso tesorer�a
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param valor
	 * @param cuenta
	 * @param documIngreso
	 * @param documAnulacion
	 * @param centroCosto
	 * @param nit
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public HashMap busquedaConceptosTesoreria2(
			Connection con,
			String codigo,
			String descripcion,
			int tipo,
			String valor,
			String cuenta,
			int documIngreso,
			int documAnulacion,
			int centroCosto,
			int nit,
			String activo,
			int institucion)
	{
		return SqlBaseConceptoTesoreriaDao.busquedaConceptosTesoreria2(con,codigo,descripcion,tipo,valor,cuenta,documIngreso,documAnulacion,centroCosto,nit,activo,institucion);
	}
	
	/**
	 * M�todo que carga los tipos de pagos
	 * @param con
	 * @return
	 */
	public Collection cargarTiposPagos(Connection con)
	{
		return SqlBaseConceptoTesoreriaDao.cargarTiposPagos(con);
	}
	
	/**
	 * M�todo que carga los tipos de documentos de contabilidad
	 * @param con
	 * @param institucion
	 * @return
	 */
	public Collection cargarTiposDocContabilidad(Connection con,int institucion)
	{
		return SqlBaseConceptoTesoreriaDao.cargarTiposDocContabilidad(con,institucion);
	}
	
	/**
	 * M�todo que carga los centros de costo
	 * @param con
	 * @param institucion
	 * @return
	 */
	public Collection cargarCentrosCosto(Connection con,int institucion)
	{
		return SqlBaseConceptoTesoreriaDao.cargarCentrosCosto(con,institucion);
	}
	
	/**
	 * M�todo que carga los terceros
	 * @param con
	 * @param institucion
	 * @return
	 */
	public Collection cargarTerceros(Connection con,int institucion)
	{
		return SqlBaseConceptoTesoreriaDao.cargarTerceros(con,institucion);
	}
}