
/*
 * Creado   8/04/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.cartera.DtoFiltroReporteCuentasCobro;
import com.princetonsa.dto.cartera.DtoResultadoReporteCuentaCobro;

/**
 * Esta <i>interface</i> define el contrato de operaciones 
 * que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>CuentasCobro</code>.
 *
 * @version 1.0, 8/04/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public interface CuentasCobroDao 
{
	
    //******************* METODOS PARA MOVIMIENTOS CXC ***********************************//
    /**
	 * Metodo para consultar por el codigo de la factura, si
	 * tiene  glosas �/y castigos
	 * @param con Connection, conexi�n con la fuente de datos
	 * @param codFactura int, codigo de la factura
	 * @param vGlosa, boolean true si es consulta de glosas
	 * @param vPago, boolean true si es consulta de pagos
	 * @param institucion int, c�digo de la instituci�n
	 * @param sumAjustes, boolean true si es consulta de sumatoria de ajustes
	 * @see com.princetonsa.dao.CuentasCobroDao#validacionesDevolucionCXC(java.sql.Connection,int,boolean,boolean,int)
	 * @return ResultSet
	 */
	public ResultSetDecorator validacionesDevolucionCXC (Connection con, 
																        int codFactura,
																        boolean vGlosa,
																        boolean vPago,
																        boolean sumAjustes,																        
																        int institucion);
    
	/**
	 * implementado para insertar una CXC que ha sido anulada.
	 * @param con connection con la fuente de datos
	 * @param numCXC double numero de la cuenta de cobro
	 * @param usuario Strig login del usuario 
	 * @param fecha, String fecha de la anulaci�n
	 * @param hora String hora de la anulaci�n
	 * @param observacion String observaci�n
	 * @param tipoMovimiento int codigo del tipo de anualci�n
	 * @param institucion int, c�digo de la instituci�n
	 * @see com.princetonsa.dao.CuentasCobroDao#insertarAnulacionCXC(java.sql.Connection,double,String,String,String,String,int,int)
	 * @return boolean true si es efectivo.
	 */
	public boolean insertarAnulacionCXC (Connection con, 
														        double numCXC,
														        String usuario,
														        String fecha, 
														        String hora,
														        String observacion, 
														        int tipoMovimiento,
														        int institucion);
    
	/**
	 * Metodo implementado para anular una cuenta de cobro.
	 * @param con Connection con la fuente de datos
	 * @param numCXC double numero de la cuenta de cobro
	 * @param institucion int, c�digo de la instituci�n
	 * @see com.princetonsa.dao.CuentasCobroDao#anularCXC(java.sql.Connection,double,int)
	 * @return boolean true si es efectivo, de lo contrario false.
	 */
	public boolean anularCXC (Connection con, double numCXC,int institucion);
    
    /**
	 * Metodo implementado para realizar la modificaci�n
	 * de la cuenta de cobro.
	 * @param con Connection, conexion con la fuente de datos
	 * @param numCxC, double n�mero de la cxc
	 * @param fechaFinal String fecha final de la cxc
	 * @param fechaInicial String fecha inicial de la cxc
	 * @param observaciones String observaciones de la generaci�n de la cxc
     * @param fechaElaboracion String
	 * @see com.princetonsa.dao.SqlBaseCuentasCobroDao#modificarCuentaCobro(java.sql.Connection,double,String,String,String,String,double,String)
	 * @return boolean true si es efectivo, false de lo contrario.
	 * @author jarloc
	 */
	public boolean modificarCuentaCobro(Connection con, 
														        double numCxC,
														        String fechaFinal,
														        String fechaInicial, 
														        String observaciones,
														        String usuarioGen,
														        double valorInicial, 
														        String fechaElaboracion);
    
    /**
	 * Metodo implementado para liberar todas las facturas
	 * que pertenecen a una cuenta de cobro especifica.
	 * se liberan todas las facturas colocando el
	 * campo en latabla que almacena el numero de la CXC
	 * en null, pero solo las facturas que pertenecen a
	 * una CXC especifica.
	 * 
	 * @param con Connection con la fuente de datos
	 * @param numeroCxc, double numero de la cuenta de cobro
	 * @param codInstitucion int, c�digo de la instituci�n
	 * @see com.princetonsa.dao.CuentasCobroDao#liberarFacturasCXC(java.sql.Connection,double,int)
	 * @return boolean, true existoso, false de lo contrario
	 * @author jarloc
	 */
	public boolean liberarFacturasCXC(Connection con, double numeroCxc,int codInstitucion);
	
	/**
	 * Metodo implementado para eliminar un registro de la
	 * tabla <code>vias_ingreso_cxc</code>, segun el 
	 * n�mero de la CXC y la V�a de Ingreso.
	 * @param con Connection, conexion con la fuente de datos
	 * @param numCxC double, numero de la cuenta de cobro
	 * @param viaIngreso int, codigo de la via de ingreso.
	 * @param institucion int, c�digo de la instituci�n
	 * @see com.princetonsa.dao.CuentasCobroDao#borrarRegistroCXCdeViasIngresoCXC(java.sql.Connection,double,int)
	 * @return boolean, true existoso, false de lo contrario
	 */
	public boolean borrarRegistroCXCdeViasIngresoCXC(Connection con, double numCxC, int viaIngreso,int institucion);
	
	/**
	 * Metodo implementado para adicionar un registro en la
	 * tabla vias_ingreso_cxc, segun su numero de cuenta de cobro
	 * y la via de ingreso.
	 * @param con Connection, conexion con la fuente de datos
	 * @param numCxC, double numero de la cuenta de cobro
	 * @param viaIngreso, int c�digo de la v�a de ingreso.
	 * @param institucion int, c�digo de la instituci�n
	 * @see com.princetonsa.dao.CuentasCobroDao#adicionarRegistroCXCaViasIngresoCXC(java.sql.Connection,double,int)
	 * @return boolean, true existoso, false de lo contrario
	 */
	public boolean adicionarRegistroCXCaViasIngresoCXC(Connection con, double numCxC, int viaIngreso,int institucion);
    
    //******************* FIN METODOS PARA MOVIMIENTOS CXC ***********************************//
    
    //******************M�TODOS PARA INACTIVACI�N DE FACTURAS***************************
	/**
	 * M�todo usado para validar si la factura puede ser inactivada.
	 * Retorna una cadena de etiquetas de mensajes de error separados por '-'
	 * @param con
	 * @param consecutivoFactura
	 * @param institucion
	 * @return
	 */
	public String validacionInactivacionFacturas(Connection con,int codigoFactura);
	
	/**
	 * M�todo para cargar los datos de la factura antes de ser inactivada
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public Collection cargarDatosFactura(Connection con,int codigoFactura);
	
	/**
	 * M�todo para insertar una inactivaci�n de facturas
	 * @param con
	 * @param numeroCuentaCobro
	 * @param codigoFactura
	 * @param loginUsuario
	 * @param observaciones
	 * @param institucion
	 * @param estado transaccion
	 * 
	 * @return
	 */
	public int insertarInactivacionFactura(Connection con,double numeroCuentaCobro,int codigoFactura,String loginUsuario, String observaciones,int institucion,String estado);
	

	/**
	 * M�todo usado para registrar la inactivaci�n de la factura en la cuenta de cobro,
	 * restando el valor de la factura en el valor inicial y el saldo de la cuenta
	 * @param con
	 * @param numCuentaCobro
	 * @param valorFactura
	 * @param institucion
	 * @param estado
	 * @return
	 */
	public int actualizarInactivacionEnCuentaCobro(Connection con,double numCuentaCobro,double valorFactura,int institucion,String estado);
	
	//****************M�TODOS PARA RADICACI�N DE CUENTAS DE COBRO *********************************************
	/**
	 * M�todo que hace las validaciones antes de radicar una cuenta de cobro
	 * @param con
	 * @param numCuentaCobro
	 * @param institucion
	 * @return
	 */
	public String validacionRadicacion(Connection con,double numCuentaCobro,int institucion);


	/**
	 * Metodo que carga una cuenta de Cobro dado su codigo.
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public ResultSetDecorator cargarCuentaCobro(Connection con, double cuentaCobro, int institucion);

	/**
	 * Metodo para cargar las vias de ingreso relacionada a una cuenta de cobro
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public ResultSetDecorator cargarViasIngreso(Connection con, double cuentaCobro, int institucion);

	/**
	 * Metodo para cargar los movimiento que tiene una cuenta de cobro.
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return ResultSet
	 */
	public ResultSetDecorator cargarMovimientoCxC(Connection con, double cuentaCobro, int institucion);

	/**
	 * Metodo que retorna un ResultSetDecorator Con los codigos de las facturas
	 * realiconadas a una cuenta de cobro.
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public ResultSetDecorator facturasCuentaCobro(Connection con, double cuentaCobro, int institucion);
	
	/**
	 * M�todo usado para registrar la radicaci�n de una cuenta de cobro
	 * @param con
	 * @param numCuentaCobro
	 * @param fechaRadicacion
	 * @param numRadicacion
	 * @param usuarioRadica
	 * @param observacionRadicacion
	 * @return
	 */
	public int insertarRadicacion(Connection con,
			double numCuentaCobro,String fechaRadicacion,String numRadicacion,
			String usuarioRadica,String observacionRadicacion,int institucion);

	//**********************METODOS PARA LA IMPRESION***********************
	
	
	/**
	 * M�todo para cargar la anulacion, observacion, hora y fecha de la misma de 
	 * una cuenta de cobro
	 */
	public Collection movimientosCxC (Connection con, double numeroCuentaCobro)throws SQLException;
	
	/**
	 * M�todo que carga los datos necesarios de las solicitudes pertenecientes
	 * a una cuenta y especificos para la impresion
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 * @throws SQLException
	 */
	public Collection cargarDatosImpresionResumida(Connection con,  double numCuentaCobro) throws SQLException;

	/**
	 * M�todo para cargar el nit de un convenio segun una cuenta de cobro
	 * @param con
	 * @param numCuentaCobro
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator convenioxCuentaCobro(Connection con, double numCuentaCobro) throws SQLException;
	/**
	 * @param con
	 * @param codigo
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param viasIngresoSeleccionadas
	 * @param numeroCuentaCobro
	 * @param institucion
	 * @param codigoCentroAtencion 
	 * @return
	 */
	public ResultSetDecorator consultarFacturasCxC(Connection con, int codigo, String fechaInicial, String fechaFinal, String viasIngresoSeleccionadas, double numeroCuentaCobro, int institucion, int codigoCentroAtencion);

	/**
	 * @param con
	 */
	public ResultSetDecorator siguienteCuentaCobro(Connection con);

	/**
	 * Metodo para realizar la insercion de la cabecera de una
	 * cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @param codigoConvenio
	 * @param estado
	 * @param fechaElaboracion
	 * @param horaElaboracion
	 * @param usuario
	 * @param valorInicial
	 * @param saldo
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param observacionesGen
	 * @param institucion
	 * @param centroAtencion 
	 * @return
	 */
	public boolean insertarEncabezadoCuentaCobro(Connection con, double numeroCuentaCobro, int codigoConvenio, int estado, String fechaElaboracion, String horaElaboracion, String usuario, double valorInicial, double saldo, String fechaInicial, String fechaFinal, String observacionesGen, int institucion, int centroAtencion);

	/**
	 * @param con
	 * @param numeroCuentaCobro
	 * @param institucion
	 * @param string
	 * @return
	 */
	public boolean insertarViasIngresCxC(Connection con, double numeroCuentaCobro, int viaIngreso, int institucion);

	/**
	 * Metodo para actualizar la cuenta de cobro de una factura
	 * dado el numero de la cuenta de cobro y la factura.
	 * @param con Connection, conexi�n con la fuente de datos.
	 * @param numeroCuentaCobro double, n�mero de la cuenta de cobro
	 * @param factura int, n�mero de la factura
	 * @param institucion int, c�digo de la instituci�n
	 * @return boolean
	 */
	public boolean actulizarNumeroCxCFactura(Connection con, double numeroCuentaCobro, int factura,int institucion);

	/**
	 * Metodo para cargar los datos minimos de una factura
	 * para la generacion de una cuenta de cobro por factura.
	 * dando el consecutivo de la factura.
	 * @param con
	 * @param consecutivoFactura
	 * @param institucion
	 */
	public ResultSetDecorator cargarFacutraParaCxC(Connection con, int codigoFactura);

	
	/**
	 * M�todo que consulta todas las facturas de una cuenta de cobro
	 * segun la fecha de elaboracion
	 * @param con
	 * @param numeroCuentaCobro
	 * @param fecha
	 * @return
	 */
	public  ResultSetDecorator cargarFacturasPorFecha(Connection con, double numeroCuentaCobro, String fecha);
	
	
	/**
	 * M�todo que me devualve las facturas segun una via de ingreso y su numero de cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @param viaIngreso
	 * @return
	 */
	public  ResultSetDecorator cargarFacturasPorViaIngreso(Connection con, double numeroCuentaCobro, String viaIngreso);
	
	/**
	 * @param con
	 * @param numeroCuentaCobro
	 * @param viaIngreso
	 * @return
	 */
	public int cargarFacturaPorConsecutivo(Connection con, double numeroCuentaCobro, int consecutivo_factura);

	/**
	 * Metodo para verificar si una cuenta de cobro existe
	 * @param con Connection, conexi�n con la fuente de datos
	 * @param institucion int, codigo de la instituci�n
	 * @param numeroCuentaCobro double, n�mero de la cuenta de cobro
	 * @return ResultSet
	 */
	public ResultSetDecorator existeCodigoCuentaCobroEnBD (Connection con,int institucion,double numeroCuentaCobro);
	
	/**
	 * Metodo para verificar si una via de ingreso
	 * posee entrada en la tabla vias_ingreso_cxc
	 * @param con Connection, conexi�n con la fuente de datos
	 * @param institucion int, codigo de la instituci�n
	 * @param viaIngreso int, c�digo de la via de ingreso
	 * @param numCXC double, n�mero de la cuenta de cobro
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCuentasCobroDao#existeViaIngresoCxCEnBD(Connection,int,int,double)
	 * @return ResultSet
	 * @author jarloc
	 */
	public int existeViaIngresoCxCEnBD (Connection con,int institucion,int viaIngreso,double numCxC);

	/**
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @return
	 */
	public boolean eliminarViasIngresoCxC(Connection con, double cuentaCobro, int institucion);

	/**
	 * @param con
	 * @param d
	 * @param i
	 * @param institucion
	 * @return
	 */
	public boolean actualizaValorCartera(Connection con, double valorCartera, int codigoFactura, int institucion);
	
	/**
	 * Metodo implementado para verificar si una cuenta
	 * de cobro esta radicada
	 * @param con Connection, conexi�n con la fuente de datos
	 * @param institucion int, codigo de la instituci�n	
	 * @param numCXC double, n�mero de la cuenta de cobro
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCuentasCobroDao#cuentaCobroPoseeRadicacion(Connection,int,double)
	 * @return boolean, true si es radicada
	 * @author jarloc
	 */
	public boolean cuentaCobroPoseeRadicacion (Connection con,int institucion,double numCXC);

	/**
	 * M�todo para consultar los estados de la Glosa correspondiente a la Factura a Inactivar
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public HashMap estadoGlosasFacturas(Connection con,int consecutivoFactura);
	
	/**
	 * M�todo para determinar si la factura posee una glosa en estado respondida y que no tenga ajustes asociados con el fin de no permitir inactivarla
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public int requiereGlosaInactivar(Connection con,int consecutivoFactura);

	public boolean aprobarCuentasCobro(Connection con,int cuenta, String usuario, int institucion);

	public HashMap consultaFacturasCxc(Connection con,double numCuentaCobro,int institucion);

	public boolean guardarDetMovimientosCxc(Connection con,int factura, double numCuentaCobro,int institucion);

	public int consultarFacturaEnProcesoAudi(int factura);

	/**
	 * 
	 * @param dtoFiltro
	 * @return
	 */
	public ArrayList<DtoResultadoReporteCuentaCobro> generarReporteCuentaCobro(DtoFiltroReporteCuentasCobro dtoFiltro);

}
