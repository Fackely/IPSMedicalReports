
/*
 * Creado   24/05/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao;

import java.sql.Connection;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Interfaz para el acceder a la fuente de datos de 
 * conceptos de pago cartera.
 *
 * @version 1.0, 24/05/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public interface ConceptosCarteraDao 
{
    
    /**
	 * metodo para insertar conceptos de pago cartera.
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigo String, codigo del concepto	
	 * @param institucion int, codigo de la institución.
	 * @param descripcion String, descripción del concepto
	 * @param codTipo int, código del tipo de concepto
	 * @param codCuenta double, código de la cuenta
	 * @param porcentaje double, porcentaje del concepto
	 * @return boolean, true efectivo, false de lo contrario.
	 * @see com.princetonsa.dao.SqlBaseConceptosCarteraDao#insertarConcepto(Connection, String,String,int,int,double)
	 */
	public boolean insertarConceptosPago (Connection con, 
													        String codigo,													        
													        int institucion,
													        String descripcion, 
													        int codTipo, 
													        double codCuenta, 
													        double porcentaje);
	
	/**
	 * metodo para realizar la consulta de los
	 * conceptos de pago cartera.
	 * @param con Connection, conexión con la fuente de datos
	 * @param codInstitucion int, código de la institución
	 * @return ResultSet
	 * @see com.princetonsa.dao.SqlBaseConceptosCarteraDao#consultaConceptos(Connection, String,String,int,int,double)
	 */
	public ResultSetDecorator consultaConceptosPago (Connection con,int codInstitucion);
	
	/**
	 * metodo para modificar conceptos de pago cartera.
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigo String, codigo del concepto
	 * @param codigoOld String, código original.
	 * @param descripcion String, descripción del concepto
	 * @param codTipo int, código del tipo de concepto
	 * @param codCuenta double, código de la cuenta
	 * @param porcentaje double, porcentaje del concepto
	 * @param institucion int, código de la institución
	 * @return boolean, true efectivo, false de lo contrario.
	 * @see com.princetonsa.dao.SqlBaseConceptosCarteraDao#modicarConceptos(Connection, String,String,int,int,double,int)
	 */
	public boolean modicarConceptosPago (Connection con,
	        												String codigo, 
	        												String codigoOld,
															String descripcion, 
															int codTipo, 
															double codCuenta, 
															double porcentaje,
															int institucion);
	
	/**
	 * metodo para eliminar conceptos de pago cartera
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigo String, código del conpto
	 * @param institucion int, código de la institución
	 * @return boolean, true efectivo.
	 * @see com.princetonsa.dao.SqlBaseConceptosCarteraDao#eliminarConceptos(Connection, String,int)
	 */
	public boolean eliminarConceptosPago (Connection con, String codigo,int institucion);
	
	/**
	 * metodo para realizar la consulta de los
	 * conceptos de pago cartera que tienen relación con
	 * aplicación de pagos.
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigoConcepto String, código del concepto
	 * @param institucion int, código de la institución
	 * @return ResultSet
	 * @see com.princetonsa.dao.SqlBaseConceptosCarteraDao#consultaConceptos(Connection,String,int
	/**
	 * metodo para modificar conceptos de pago cartera.
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigo String, codigo del concepto
	 * @param descripcion String, descripción del concepto
	 * @param codTipo int, código del tipo de concepto
	 * @param codCuenta double, código de la cuenta
	 * @param porcentaje double, porcentaje del concepto
	 * @param codInstitucion int, código de la institución
	 * @return boolean, true efectivo, false de lo contrario.
	 * @see com.princetonsa.dao.SqlBaseConceptosCarteraDao#modicarConceptos(Connection, String,String,int,int,double,int)
	 */
	public ResultSetDecorator busquedaAvanzadaPago (Connection con,
		        												String codigo, 
																String descripcion, 
																int codTipo, 
																double codCuenta, 
																double porcentaje,
																int codInstitucion);
	
	/**
	 * metodo para realizar la consulta de los
	 * conceptos de pago cartera que tienen relación con
	 * aplicación de pagos.
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigoConcepto String, código del concepto
	 * @param institucion int, código de la institución
	 * @return ResultSet
	 * @see com.princetonsa.dao.SqlBaseConceptosCarteraDao#consultaConceptos(Connection,String)
	 */
	public ResultSetDecorator consultaRelacionConceptosPago (Connection con, String codigoConcepto,int institucion);
	
	/**
	 * metodo para realizar la consulta de los
	 * conceptos de ajustes cartera.
	 * @param con Connection, conexión con la fuente de datos
	 * @return ResultSet
	 * @see com.princetonsa.dao.SqlBaseConceptosCarteraDao#consultaConceptosAjustes(Connection, int)
	 */
	public ResultSetDecorator consultaConceptosAjustes (Connection con,int codInstitucion);
	

	/**
	 * metodo para insertar conceptos de ajustes cartera.
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigo String, codigo del concepto
	 * @param descripcion String, descripción del concepto	 
	 * @param codCuenta double, código de la cuenta
	 * @param naturaleza int, código de la naturaleza.
	 * @param tipoCartera @todo
	 * @return boolean, true efectivo, false de lo contrario.
	 * @see com.princetonsa.dao.SqlBaseConceptosCarteraDao#insertarConceptosAjustes(Connection, String,int,String,double,int, int)
	 */
	public boolean insertarConceptosAjustes (Connection con, 
													        String codigo, 
													        int institucion,
													        String descripcion,													         
													        double codCuenta, 
													        int naturaleza,
													        int tipoCartera);
	
	/**
	 * metodo para modificar conceptos de ajustes cartera.
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigo String, codigo del concepto modificado
	 * @param codigoOld String, código original.
	 * @param descripcion String, descripción del concepto	 
	 * @param codCuenta double, código de la cuenta
	 * @param naturaleza int, código de la naturaleza
	 * @param institucion int, código de la institución
	 * @param tipoCartera
	 * @return boolean, true efectivo, false de lo contrario.
	 * @see com.princetonsa.dao.SqlBaseConceptosCarteraDao#modicarConceptosAjustes(Connection, String,String,double,int,int)
	 */
	public boolean modicarConceptosAjustes(Connection con,
	        												String codigo,
	        												String codigoOld,
															String descripcion,															
															double codCuenta, 
															int naturaleza,
															int institucion,
															int tipoCartera);
	
	/**
	 * metodo para eliminar conceptos de ajustes cartera
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigo String, código del conpto
	 * @param institucion int, código de la institución
	 * @return boolean, true efectivo.
	 * @see com.princetonsa.dao.SqlBaseConceptosCarteraDao#eliminarConceptosAjustes(Connection, String,int)
	 */
	public boolean eliminarConceptosAjustes (Connection con, String codigo,int institucion);
	
	/**
	 * metodo para modificar conceptos de pago cartera.
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigo String, codigo del concepto
	 * @param descripcion String, descripción del concepto	 	
	 * @param codCuenta double, código de la cuenta
	 * @param naturaleza int, código de la naturaleza
	 * @param tipoCartera 
	 * @param institucion int, código de la institución
	 * @return boolean, true efectivo, false de lo contrario.
	 * @see com.princetonsa.dao.SqlBaseConceptosCarteraDao#busquedaAvanzadaAjustes(Connection, String,String,double,int)
	 */
	public ResultSetDecorator busquedaAvanzadaAjustes (Connection con,
		        												String codigo, 
																String descripcion,																
																double codCuenta,																
																int naturaleza,
																int codInstitucion, int tipoCartera);
	
	/**
	 * metodo para realizar la consulta de los
	 * conceptos de ajustes cartera que tienen relación con
	 * ajustes y/o conceptos castigo cartera.
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigoConcepto String, código del concepto
	 * @param institucion int, código de la institución
	 * @return ResultSet
	 * @see com.princetonsa.dao.SqlBaseConceptosCarteraDao#consultaRelacionConceptosAjustes(Connection,String)
	 */
	public int consultaRelacionConceptosAjustes (Connection con, String codigoConcepto,int institucion);

	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public ResultSetDecorator consultaConceptosGlosas(Connection con, int codigoInstitucionInt);

	/**
	 * @param con
	 * @param string
	 * @param codigoInstitucionInt
	 * @return
	 */
	public ResultSetDecorator consultaRelacionConceptosGlosas(Connection con, String codigo, int codigoInstitucionInt);

	/**
	 * Método encargado de insertar un concepto glosa
	 * @param con
	 * @param codigoConcepto
	 * @param codigoInstitucionInt
	 * @param descripcion
	 * @param cuentaDebito
	 * @param cuentaCredito
	 * @param tipoConcepto
	 * @param conceptoGeneral
	 * @param conceptoEspecifico
	 * @param usuarioModifica
	 */
	public int insertarConceptoGlosa(
			Connection con, 
			String codigoConcepto, 
			int codigoInstitucionInt, 
			String descripcion, 
			double cuentaDebito, 
			double cuentaCredito,
			String tipoConcepto,
			String conceptoGeneral,
			String conceptoEspecifico,
			String usuarioModifica);

	/**
	 * Método encargado de modificar un concepto glosa
	 * @param con
	 * @param codigoConcepto
	 * @param codigoInstitucionInt
	 * @param descripcion
	 * @param cuentaDebito
	 * @param cuentaCredito
	 * @param tipoConcepto
	 * @param conceptoGeneral
	 * @param conceptoEspecifico
	 * @param usuarioModifica
	 * @return
	 */
	public int modificarConceptoGlosas(
			Connection con, 
			String codigoConceptoAntiguo, 
			int codigoInstitucionInt, 
			String codigoConcepto, 
			String descripcion, 
			double cuentaDebito, 
			double cuentaCredito,
			String tipoConcepto,
			String conceptoGeneral,
			String conceptoEspecifico,
			String usuarioModifica);
	
	/**
	 * @param con
	 * @param codigoConceptoAntiguo
	 * @param codigoInstitucionInt
	 * @return
	 */
	public int eliminarConceptoGlosas(Connection con, String codigoConceptoAntiguo, int codigoInstitucionInt);

	/**
	 * @param con
	 * @param string
	 * @param codigoInstitucionInt
	 * @param string2
	 * @param d
	 * @param e
	 * @param String tipoConcepto
	 * @param String conceptoGeneral
	 * @param String conceptoEspecifico
	 * @return
	 */
	public ResultSetDecorator busquedaAvanzadaGlosas(
			Connection con, 
			String codigo, 
			int codigoInstitucionInt, 
			String descripcion, 
			double cuentaDebito, 
			double cuentaCredito,
			String tipoConcepto,
			String conceptoGeneral,
			String conceptoEspecifico);
	
	
	//CONCEPTO CASTIGO
	/**
	 * @param con
	 * @param codigoConcepto
	 * @param codigoInstitucionInt
	 * @param descripcion
	 * @param cuentaDebito
	 * @param cuentaCredito
	 */
	public boolean insertarConceptoCastigo(Connection con, String codigoConcepto, int codigoInstitucionInt, String descripcion, String ajusteCredito);

	/**
	 * @param con
	 * @param codigoConcepto
	 * @param codigoInstitucionInt
	 * @param descripcion
	 * @param cuentaDebito
	 * @param cuentaCredito
	 * @return
	 */
	public boolean modificarConceptoCastigo(Connection con, int codigoInstitucionInt,String codigoConceptoAntiguo, String codigoConcepto, String descripcion, String ajusteCredito);

	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public ResultSetDecorator consultaConceptoCastigo(Connection con, int codigoInstitucionInt);

	/**
	 * @param con
	 * @param string
	 * @param codigoInstitucionInt
	 * @return
	 */
	public ResultSetDecorator consultaRelacionConceptoCastigo(Connection con, String codigo, int codigoInstitucionInt);

	/**
	 * @param con
	 * @param codigo
	 * @param codigoInstitucionInt
	 * @param descripcion
	 * @param ajusteCredito
	 * @return
	 */
	public ResultSetDecorator busquedaAvanzadaCastigo(Connection con, String codigo, int codigoInstitucionInt, String descripcion,String ajusteCredito);
	
	/**
	 * @param con
	 * @param codigoConcepto
	 * @return
	 */
	public boolean eliminarConceptoCastigo(Connection con, String codigoConcepto);

  /////Fin concepto castigo
	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public ResultSetDecorator consultaConceptosRespuestasGlosas(Connection con, int codigoInstitucionInt);

	/**
	 * @param con
	 * @param string
	 * @param codigoInstitucionInt
	 * @return
	 */
	public ResultSetDecorator consultaRelacionConceptosRespuestasGlosas(Connection con, String codigo, int codigoInstitucionInt);

	/**
	 * @param con
	 * @param codigoConceptoAntiguo
	 * @param codigoInstitucionInt
	 * @return
	 */
	public int eliminarConceptoRespuestaGlosas(Connection con, String codigoConceptoAntiguo, int codigoInstitucionInt);

	/**
	 * @param con
	 * @param codigoConcepto
	 * @param codigoInstitucionInt
	 * @param descripcion
	 * @return
	 */
	public int insertarConceptoRespuestaGlosas(Connection con, String codigoConcepto, int codigoInstitucionInt, String descripcion);

	/**
	 * @param con
	 * @param codigoConceptoAntiguo
	 * @param codigoInstitucionInt
	 * @param codigoConcepto
	 * @param descripcion
	 * @return
	 */
	public int modificarConceptoRespuestaGlosas(Connection con, String codigoConceptoAntiguo, int codigoInstitucionInt, String codigoConcepto, String descripcion);

	/**
	 * @param con
	 * @param string
	 * @param codigoInstitucionInt
	 * @param string2
	 * @return
	 */
	public ResultSetDecorator busquedaAvanzadaRespuestasGlosas(Connection con, String codigo, int codigoInstitucionInt, String descripcion);
	
	/**
	 * Metodo que consulta si un concepto tipo glosa ha sido utilizado o no para validar si se puede o no eliminar
	 * @param connection
	 * @param criterios
	 * @return
	 */
	public boolean consultarConceptosGlosa (Connection con, String codConcepto);
}
