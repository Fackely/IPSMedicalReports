/*
 * Creado   08/08/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.capitacion;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;

/**
 * Interfaz Dao de las funciones de acceso a la fuente de datos
 * para el cierre cartera capitacion
 *
 * @version 1.0, 08/08/2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 */
public interface CierresCarteraCapitacionDao 
{
	/**
	 * Metodo paa consultar el cierre insertado
	 * @param con Connection, conexión con la fuente de datos
	 * @param codigo  
	 * @param codigoInstitucion
	 * @return ResultSet
	 */
	public ResultSetDecorator cargarResumenCierreCarteraCapitacion(Connection con, String codigo, int codigoInstitucion);

	/**
	 * metodo para consultar el ultimo código
	 * de la secuencia 
	 * @param con Connection, conexión con la fuente de datos
	 * @return ResultSet
	 */
	public ResultSetDecorator ultimoCodigoCierre (Connection con,int institucion, String tipoCierre);
	
	/**
	 * metodo que verifica la existencia de cuentas de cobro radicadas dada una fecha y mes cierre
	 * @param con
	 * @param yearCierre
	 * @param mesCierre
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean existenCxCCapitadasRadicadasDadaFecha(Connection con, String yearCierre, String mesCierre, int codigoInstitucion );
	

	/**
	 * metodo que inserta el cierre de cartera capitacion
	 * RETORNA el codigo insertado
	 * @param con
	 * @param codigoInstitucion
	 * @param yearCierre
	 * @param mesCierre
	 * @param observaciones
	 * @param tipoCierre
	 * @param loginUsuario
	 * @return
	 */
	public int insertCierreCarteraCapitacion(Connection con, int codigoInstitucion, String yearCierre, String mesCierre, String observaciones, String tipoCierre, String loginUsuario );
	
	/**
	 * Inserta el detalle del cierre cartera x convenio
	 * @param con
	 * @param codigoCierre
	 * @param codigoConvenio
	 * @param valorCartera
	 * @param valorAjusteDebito
	 * @param valorAjusteCredito
	 * @param valorPago
	 * @return
	 */
	public boolean insertDetalleCierreCarteraXConvenio(Connection con, String codigoCierre, int codigoConvenio, String valorCartera, String valorAjusteDebito, String valorAjusteCredito, String valorPago );
	
	/**
	 * metodo que obtiene los valores de los pagos - ajustes debito credito y valor cartera - para posteriormente insertarlos en el cierre 
	 * @param con
	 * @param codigoInstitucion
	 * @param yearCierre
	 * @param mesCierre
	 * @return
	 */
	public HashMap getPagosAjustesValorCarteraXConvenio(Connection con, int codigoInstitucion, String yearCierre, String mesCierre);
	
	/**
	 * metodo que evalua si existe o no cierre para una anio y fecha dados
	 * @param con
	 * @param yearCierre
	 * @param tipoCierre
	 * @return
	 */
	public boolean existeCierre(Connection con, String yearCierre, String tipoCierre, int codigoInstitucion);
	
	/**
	 * metodo para evaluar si las CxC estan radicadas para un anio dado
	 * @param con
	 * @param yearCierre
	 * @return
	 */
	public HashMap estanCxCRadicadasParaCierreAnual (Connection con, String yearCierre, int codigoInstitucion);
	
	/**
	 * metodo que obtiene el resumen de los valores de los cierres de cartera para un anio y tipo cierre dado
	 * @param con
	 * @param yearCierre
	 * @param codigoInstitucion
	 * @param tipoCierre
	 * @return
	 */
	public HashMap getResumenValoresCierresCartera(Connection con, String yearCierre, int codigoInstitucion, String tipoCierre);

	
	/**
	 * metodo que obtiene los valores de los pagos - ajustes debito credito y valor cartera -  para el cierre anual
	 *  
	 * @param con
	 * @param codigoInstitucion
	 * @param yearCierre
	 * @return
	 */
	public HashMap getPagosAjustesValorCarteraXConvenioCierreAnual(Connection con, int codigoInstitucion, String yearCierre);
}
