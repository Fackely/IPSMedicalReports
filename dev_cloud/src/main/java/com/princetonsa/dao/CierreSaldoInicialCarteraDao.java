
/*
 * Creado   14/07/2005
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
 * cierre saldo inicial cartera
 *
 * @version 1.0, 14/07/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public interface CierreSaldoInicialCarteraDao 
{
   /**
    * Metodo para obtener las facturas menores a una fecha,
    * segun la institucion, que aun no tengan cierre, y se 
    * encuentren en estado facturadas.
	 * @param con
	 * @param fecha String
	 * @param institucion int 
	 * @param esCierreInicial boolean, true si es cierreInicial, false si es cierre
	 * @return ResultSet
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#cargarFacutras(java.sql.Connection,String,int)
	 * @author jarloc
	 */
	public ResultSetDecorator cargarFacutras(Connection con, String fecha, int institucion,boolean esCierreInicial);
	
	/**
	 * Metodo implementado para insertar los datos generales
	 * de un cierre de saldos iniciales de cartera.
	 * @param con Connection, conexion con la fuente de datos
	 * @param year String, año del cierre
	 * @param mes String, mes del cierre
	 * @param obser String, observaciones
	 * @param tipoCierre String, tipo de cierre
	 * @param usuario String, login del usuario
	 * @param institucion int,codigo de la institución	 
	 * @return boolean, true inserto.
	 * @see com.princetonsa.dao.sqlbase.SqlBaseCierreSaldoInicialCarteraDao#insertarCierresSaldos(java.sql.Connection,String,String,String,String,String,int)
	 * @author jarloc
	 */
	public boolean insertarCierresSaldos(Connection con, 
												        String year,String mes,
												        String obser,String tipoCierre,
												        String usuario,int institucion);
	
	/**
	 * metodo implementado para insertar los valores 
	 * por convenio, de un cierre de saldos iniciales
	 * de cartera.
	 * @param con Connection, conexión con la fuente de datos
	 * @param codCierre int, código del cierre
	 * @param codConvenio int, código del convenio
	 * @param vlrCartera double, valor de cartera
	 * @param vlrAjusteDebito double, valor de ajustes debito
	 * @param vlrAjusteCredito double, valor de ajustes credito
	 * @param vlrPago double, valor de pagos	
	 * @return boolean, true si inserto.
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#cargarFacutras(java.sql.Connection,int,int,double,double,double,double)
	 * @author jarloc
	 */
	public boolean insertarValoresCierreXConvenio(Connection con, 
															        int codCierre,int codConvenio,
															        double vlrCartera,double vlrAjusteDebito, 
															        double vlrAjusteCredito, double vlrPago);
	
	/**
	 * metodo para consultar el ultimo código
	 * de la secuencia 
	 * @param con Connection, conexión con la fuente de datos
	 * @return ResultSet
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#ultimoCodigoCierre(java.sql.Connection)
	 * @author jarloc
	 */
	public ResultSetDecorator ultimoCodigoCierre (Connection con,int institucion);
	
	/**
	 * metodo para cerrar las facturas,
	 * actualizado el campo de cierre en false
	 * @param con Connection con la fuente de datos	 
	 * @param codFact int, codigo de la factura	 
	 * @return boolean, true si actualizo
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#cerrarFacturas(java.sql.Connection)
	 * @author jarloc
	 */
	public boolean cerrarFacturas (Connection con,int codFact);
	
	/**
	 * Metodo paa consultar el cierre insertado
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigo  int
	 * @return ResultSet
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#cargarResumenCierre(java.sql.Connection,int)
	 * @author jarloc
	 */
	public ResultSetDecorator cargarResumenCierre(Connection con, int codigo);
	
	/**
	 * 
	 * @param con Connection, conexión con la fuente de datos
	 * @param year String, año del cierre
	 * @param mes String, mes del cierre
	 * @param tipoCierre String, tipo del cierre (Anual-Inicial-Mensual)
	 * @param institucion int, código de la institución
	 * @return boolean, true si posee cierre
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#existeCierreAnualMensual(java.sql.Connection,int,int,String,int)
	 * @author jarloc
	 */
	public boolean existeCierreAnualMensual(Connection con,int year,int mes,String tipoCierre,int institucion);
	
	/**
	 * 
	 * @param con Connection, conexión con la fuente de datos
	 * @param year int, año del cierre
	 * @param tipoCierre String, tipo del cierre(Anual-Mensual-Inicial)
	 * @param institucion int, código de la institución.
	 * @return int, mes del cierre
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#ultimoCierreGenerado(java.sql.Connection,int,String,int)
	 * @author jarloc
	 */
	public int ultimoCierreGenerado(Connection con,int year,String tipoCierre,int institucion);
	
	/**
	 * Metodo para consultar las facturas que fueron anuladas
	 * en el mes del cierre y que poseen fecha de generacion de
	 * la factura anterior a la fecha del cierre.
	 * @param con Connection, conexión con la fuente de datos
	 * @param institucion int, código de la institución
	 * @param fecha String, fecha del cierre
	 * @return ResultSet
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#facturasAnuladasEnMesDeCierre(java.sql.Connection,int,String)
	 * @author jarloc
	 */
	public ResultSetDecorator facturasAnuladasEnMesDeCierre(Connection con, int institucion, String fecha);
	
	/**
	 * Metodo para consultar los meses de cierre
	 * generados en el año.
	 * @param con Connection
	 * @param institucion in
	 * @param tipoCierre String
	 * @param anioCierre int
	 * @return ResultSet
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#cierresMensuales(java.sql.Connection,int,String,int)
	 * @author jarloc 
	 */
	public ResultSetDecorator cierresMensuales (Connection con,int institucion,String tipoCierre,int anioCierre);
	
	/**
	 * Metodo para consultar el codigo del cierre
	 * que sera eliminado.
	 * @param con Connection
	 * @param yearCierre int
	 * @param mesCierre int
	 * @param institucion int
	 * @return int, codigo del cierre
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#obtenerCodigoCierreActualizar(java.sql.Connection,int,int,int)
	 * @author jarloc
	 */
	public int obtenerCodigoCierreActualizar (Connection con, int yearCierre,int mesCierre,int institucion);
	
	/**
	 * Metodo para realizar la eliminación en la
	 * tabla de cierres_saldos,cuado se recalculan los
	 * valores por convenio de un cierre mensual.
	 * @param con Connection 
	 * @param codigoCierre int
	 * @return boolean, true si realizo actualizacion
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#actualizarCierresSaldos(java.sql.Connection,int)
	 * @author jarloc
	 */
	public boolean eliminarCierresSaldos(Connection con,int codigoCierre);
	
	/**
	 * Metodo para realizar la eliminación de
	 * los valoeres de la tabla de cierre_saldos_convenio
	 * @param con Connection	  
	 * @param codigoCierre int	 
	 * @return boolean, true si es efectivo
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#actualizarCierresSaldosXConvenio(java.sql.Connection,int)
	 * @author jarloc
	 */
	public boolean eliminarCierresSaldosXConvenio(Connection con,int codigoCierre);
	
	/**
	 * Metodo para consultar los cierres que se 
	 * generaron durante el año
	 * @param con Connection 
	 * @param institucion int 
	 * @param anioCierre int
	 * @return ResultSet
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#consultarCierresGeneradosEnElAnio(java.sql.Connection,int,int)
	 * @author jarloc
	 */
	public ResultSetDecorator consultarCierresGeneradosEnElAnio(Connection con,int institucion,int anioCierre);
	
	/**
	 * Metodo para realizar la busqueda avanzada
	 * de los cierre de cartera mensuales, filtarndo
	 * por convenio,año ó mes.
	 * @param con Connection
	 * @param anioCierre int
	 * @param mesCierre int 
	 * @param institucion int
	 * @param convenio String[] 
	 * @return ResultSet
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#consultaAvanzadaCierresMensuales(java.sql.Connection,int,int,int,String[])
	 * @author jarloc
	 */
	public ResultSetDecorator consultaAvanzadaCierresMensuales(Connection con,int anioCierre,int mesCierre,int institucion,String[] convenio);

	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public String obtenerFechaCierreSaldoIncialCartera(Connection con, int institucion);

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ResultSetDecorator cargarResumenCierreSaldoInicial(Connection con, int institucion);

	public int[] validarExistenFacturas(Connection con, String fecha,
			int institucion, boolean esCierreInicial);
}
