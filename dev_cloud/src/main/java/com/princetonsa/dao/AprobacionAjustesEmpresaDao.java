
/*
 * Creado   23/08/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>AprobacionAjustesEmpresa</code>.
 *
 * @version 1.0, 23/08/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public interface AprobacionAjustesEmpresaDao 
{
    /**
	 * Metodo implementado para realizar una busqueda avanzada,por medio de un HashMap 
	 * que contiene los objetos con los respectivos nombres de los campos y los valores
	 * de cada uno de ellos por los cuales se filtra la busqueda.
	 * <code>numRegBusqueda</code> es la llave que lleva por defecto el mapa, con el número de 
	 * registros que se van adicionando al mapa, para la busqueda por campos.
	 * <code>1. </code>El mapa contendra objetos de tipo InfoDatos(id,value,descripcion), para las 
	 * columnas por las cuales se filtrara la busqueda, siendo el id=campo de la tabla, 
	 * el value=valor a buscar y la descripcion=operador(=,!=,is,not is).
	 * El parametro value del InfoDatos varia de tipo segun el tipo de Dato que alvergue 
	 * el campo de la tabla, pudiendo ser Varchar,Integer ó Float. Por ello la clase InfoDatos 
	 * contiene 3 constructores distintos (String,String,String),(String,itn,String) y (String,double,String). 
	 * La llave del mapa para identificar los registros tendra el siguiente formato 
	 * <code><campo_index></code>,siendo el index un <code>int</code> que comienza en 0 y
	 * se incrementa sucesivamente segun el numero de registros.
	 * <code>2. </code>Si la consulta posee inner join con otras tablas la llave para
	 * identificar los objetos tiene el formato <code><inner_index></code>, que hace 
	 * referencia al objeto infodatos con el constructor(String id,String value),
	 * el id=nombre de la tabla y el value=los campos del inner(ej. a.codigo=f,codigo).
	 * @param con Connection, conexión con la fuente de datos
	 * @param mapa HashMap, Con los campos y valores.
	 * @return ResultSet, Resultado de la consulta
	 * @author jarloc
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#consultaAvanzadaAjustes(java.sql.Connection,HashMap)
	 */
	public ResultSetDecorator consultaAvanzadaAjustes(Connection con,HashMap mapa);
	
	/**
	 * Metodo para actualizar los valores credito y debito
	 * de la factura
	 * @param con Connection
	 * @param numFact int, número de la factura
	 * @param vlrAjuste double, valor del ajuste
	 * @param institucion int, código de la institución
	 * @param tipoAjuste int, código del tipo del ajuste.
	 * @return boolean, true si es efectivo
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#actualizarValoresFacturas(java.sql.Connection,HashMap)
	 */
	public abstract boolean actualizarValoresFacturas(Connection con,
													        int numFact,double vlrAjuste,
													        int institucion,int tipoAjuste);
	
	/**
	 * Metodo para actualizar el estado del ajuste
	 * @param con Connection
	 * @param numAjuste double, número del ajuste
	 * @param institucion int, código de la institución
	 * @return boolean, true si es efectivo
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#actualizarEstadoAjustes(java.sql.Connection,HashMap)
	 */
	public abstract boolean actualizarEstadoAjustes(Connection con,double numAjuste,int institucion);
	
	/**
	 * metodo para actualizar los valores del detalle de
	 * factura, ajustes debito y credito.
	 * @param con Connection
	 * @param codSolicitud int, código de la solicitud
	 * @param numFactura int, númro de la factura
	 * @param ajuste double, valor del ajuste
	 * @param ajusteMedico double, valor del ajuste medico
	 * @param tipoAjuste int, código del tipo de ajuste
	 * @param actualizarAjusteMedicoPool 
	 * @return boolean, true si es efectivo
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#actualizarValoresFacturaServicios(java.sql.Connection,HashMap)
	 */
	public abstract boolean actualizarValoresFacturaServicios(Connection con,
																	int codSolicitud,int numFactura,
															        double ajuste,double ajusteMedico,
															        int tipoAjuste, boolean actualizarAjusteMedicoPool);
	
	/**
	 * Metodo para realizar la actualizacion de los
	 * valores de cuentas de cobro.
	 * @param con Connection
	 * @param numCxC double, número de la cuenta de cobro
	 * @param valorAjuste double, valor del ajuste
	 * @param tipoAjuste int, código del tipo de ajuste
	 * @param institucion int, código de la institución
	 * @param estadoCxC int, código del estado de la cuenta de cobro
	 * @return boolean, true si es efectivo
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#actualizarCuentaDeCobro(java.sql.Connection,HashMap)
	 */
	public abstract boolean actualizarCuentaDeCobro (Connection con,
													        double numCxC,double valorAjuste,
													        int tipoAjuste,int institucion,int estadoCxC);
	
	/**
	 * Metodo para realizar la insercion de la 
	 * aprobación del ajuste
	 * @param con Connection
	 * @param codAjuste double
	 * @param usuario String
	 * @param fechaApro String
	 * @return boolean, true efectivo
	 * @see com.princetonsa.dao.SqlBase.SqlBaseCierreSaldoInicialCarteraDao#insertarAprobacion(java.sql.Connection,HashMap)
	 */
	public abstract boolean insertarAprobacion(Connection con,double codAjuste,String usuario,String fechaApro);
    /**
     * metodo para consultar el detalle de facturas
     * para un ajuste, obteniendo el tipo de solicitud,
     * si es de cirugia ó no.
     * @param con Connection
     * @param codAjuste double
     * @param codFactura int 
     * @param institucion int 
     * @return HashMap
     */
    public abstract HashMap consultarDetalleFacturasXAjustes(Connection con,double codAjuste,int codFactura,int institucion);
    /**
     * metodo para generar la consulta del detalle de
     * las facturas a nivel de asocios, si existe información
     * de las mismas en ajustes de solicitudes cirugias, previamente
     * consultadas
     * @param con Connection
     * @param institucion int
     * @param codAjuste double
     * @param codFactura int 
     * @param detAsocFactSol int
     * @param codigoPkServArt 
     * @return HashMap
     */
    public abstract HashMap consultaDetalleFacturaAsocios(Connection con,int institucion,double codAjuste,int codFactura,int detAsocFactSol, int codigoPkServArt);
    
    public abstract HashMap consultaDetalleFacturaNivelAsocios(Connection con,int institucion,double codAjuste,int codFactura,int detAsocFactSol, int codigoPkServArt);
    /**
     * metodo para actualizar los valores del detalle de
     * asocio de facturas
     * @param con Connection
     * @param detAsocFactSol int
     * @param codServicio int
     * @param ajuste double, valor del ajuste
     * @param ajusteMedico double, valor del ajuste medico
     * @param tipoAjuste int, código del tipo de ajuste
     * @param actualizarAjusteMedicoPool 
     * @return boolean, true si es efectivo     
     */
    public abstract boolean actualizarValoresAsocioDetalleFactura(Connection con,int detAsocFactSol,int codServicio,double ajuste,double ajusteMedico,int tipoAjuste, boolean actualizarAjusteMedicoPool);

    /**
     * 
     * @param con
     * @param detPaqFacSol
     * @param ajuste
     * @param ajusteMedico
     * @param tipoAjuste
     * @param actualizarAjusteMedicoPool
     * @return
     */
    public abstract boolean actualizarValoresPaquetesDetalleFactura(Connection con,int detPaqFacSol,double ajuste,double ajusteMedico,int tipoAjuste, boolean actualizarAjusteMedicoPool);
    
    
    /**
     * 
     * @param con
     * @param institucion
     * @param codAjuste
     * @param codFactura
     * @param detAsocFactSol
     * @return
     */
	public HashMap consultaDetalleFacturaNivelPaquetes(Connection con, int institucion, double codAjuste, int codFactura, int detFacSol);
	
	/**
	 * Método que verifica si el ajuste y la factura tienen valor de pool mayor a 0
	 * @param con
	 * @param codigoAjustes
	 * @param codigoFactura
	 * @return
	 */
	public String tieneRegistroAjustePoolFactura(Connection con,String codigoAjustes,String codigoFactura);
	
	/**
	 * Método para actualizar el valor de los ajustes al pool
	 * @param con
	 * @param codigoAjustes
	 * @param codigoFactura
	 * @return
	 */
	public boolean actualizarValorAjustePool(Connection con,String codigoAjustes,String codigoFactura);
}
