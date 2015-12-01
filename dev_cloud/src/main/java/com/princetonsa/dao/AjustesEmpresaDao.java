/*
 * @(#)AjustesEmpresaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 *  Interfaz para el acceder a la fuente de datos de los ajustes de empresa
 *
 * @version 1.0, Julio 22 / 2005	
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 */
public interface AjustesEmpresaDao 
{

	/**
	 * Metodo para incertar el encabezado de un ajuste, esta informacion y la general del ajuste.
	 * @param con
	 * @param cadena, sentencia SQL se recibe ya que varia de acuerdo al motor da Base de datos.
	 * @param codigo, codigo del ajuste, se obtiene a traves de la secuencia seq_ajustes_empresa.
	 * @param consecutivo, consecutivo parametrizable por cada institucion.
	 * @param tipo_ajuste, tipo de ajuste debito factura, debito cuenta cobro, credito factura, credito cuenta cobro.
	 * @param institucion.
	 * @param castigo_cartera, indica si el ajuste es un castigo o no.
	 * @param conceptoCastigoCartera, en caso de que sea castigo se requiere el cocepto de castigo.
	 * @param fechaAjuste
	 * @param fechaElaboracion
	 * @param horaElaboracion
	 * @param usuario
	 * @param cuentaCobro
	 * @param conceptoAjuste, concepto de porque se hizo el ajuste.
	 * @param metodoAjuste, metodo de ajuste A-Automatico, P-Porcentual, M-Manual
	 * @param valorAjuste
	 * @param observaciones
	 * @param estado, Estado del ajuste, Generado-Aprobado-Anulado
	 * @param codAjusteReversado
	 * @param reversado
	 * @return >0 en caso de que no se genere error en la insercion.
	 */
	public int ingresarAjusteGeneral(Connection con, String consecutivo, int tipo_ajuste, int institucion, boolean castigo_cartera, 
			String conceptoCastigoCartera, String fechaAjuste,String fechaElaboracion,String horaElaboracion,String usuario, 
			double cuentaCobro,String conceptoAjuste,String metodoAjuste,double valorAjuste,String observaciones, int estado, boolean reversado, double codAjusteReversado );
	
	
	/**
	 * Metodo para ingresar el ajuste referente a una factura.
	 * @param con
	 * @param codigo, Codigo del ajuste, el mismo obtenido para la insercion del ajuste general.
	 * @param factura, FActura a la que se le aplica el ajuste.
	 * @param metodoAjuste, Metodo de Ajuste utilizado.
	 * @param valorAjuste
	 * @param conceptoAjuste
	 * @param institucion
	 * @return >0 en caso de que no se genere error en la insercion.
	 */
	public int ingresarAjustesFactura(Connection con,double codigo,int factura, String metodoAjuste, double valorAjuste,String conceptoAjuste,int institucion);
	
	/**
	 * Metodo para ingresa el detalle de los ajustes de una factura.
	 * @param con, Conexion
	 * @param codigo, Codigo del ajuste, el mismo obtenido para la insercion del ajuste general. 
	 * @param factura, factura,
	 * @param detFacturaSolicitud, detalle de la factura, de esta forma se hace referencia directa a los servicios y los articulos.
	 * @param pool, pool del medico que respondio el servicio.
	 * @param codigoMedicoResponsable
	 * @param metodoAjuste
	 * @param valorAjuste
	 * @param valorAjustePool
	 * @param valorAjusteInstitucion
	 * @param conceptoAjuste
	 * @param institucion
	 * @return
	 */
	public int ingresarAjustedDetalleFactura(Connection con, double codigo, int factura, int detFacturaSolicitud, int pool,int codigoMedicoResponsable,
								String metodoAjuste,double valorAjuste,double valorAjustePool, double valorAjusteInstitucion,String conceptoAjuste,int institucion);


	/**
	 * @param con
	 * @param codigoFactura
	 * @param castigoCartera
	 * @param ajustesCxCRadicada
	 * @param modificacion
	 * @param codigoAjuste
	 */
	public ResultSetDecorator cargarUnaFactura(Connection con, int codigoFactura, boolean castigoCartera, boolean ajustesCxCRadicada, boolean modificacion, double codigoAjuste);


	/**
	 * @param con
	 * @param codigoFactura
	 * @param servicios
	 * @return
	 */
	public ResultSetDecorator cargarDetalleFactura(Connection con, int codigoFactura, boolean servicios);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param factura
	 * @param detFacturaSolicitud
	 * @param metodoAjuste
	 * @param valorAjuste
	 * @param valorAjustePool
	 * @param valorAjusteInstitucion
	 * @param conceptoAjuste
	 * @return
	 */
	public int updateAjustedDetalleFactura(Connection con,int codigopk, double codigo, int factura, int detFacturaSolicitud, String metodoAjuste, double valorAjuste,double valorAjustePool, double valorAjusteInstitucion,String conceptoAjuste,int institucion);


	/**
	 * @param con
	 * @param codigoAjuste
	 * @param codigoFactura
	 * @param servicio
	 * @return
	 */
	public ResultSetDecorator cargarDetalleAjusteFactura(Connection con, double codigoAjuste, int codigoFactura, boolean servicio);


	/**
	 * @param con
	 * @param codigoFactura
	 * @param codigoArticuloAgrupacion
	 * @return
	 */
	public ResultSetDecorator cargarDetalleFacturasArticuloAgrupado(Connection con, int codigoFactura, int codigoArticuloAgrupacion);


	/**
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @param castigoCartera
	 * @param ajustesCxCRadicada
	 * @param modificacion
	 * @return
	 */
	public ResultSetDecorator cargarCuentaCobro(Connection con, double cuentaCobro, int institucion, boolean castigoCartera, boolean ajustesCxCRadicada, boolean modificacion);


	/**
	 * @param con
	 * @param numeroCuentaCobro
	 * @param institucion
	 * @return
	 */
	public ResultSetDecorator cargarFacturasCuentaCobro(Connection con, double numeroCuentaCobro, int institucion);


	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public ResultSetDecorator cargarFacturasCuentaCobroAjuste(Connection con, double codigoAjuste);


	/**
	 * @param con
	 * @param codigo, Codigo del ajuste, el mismo obtenido para la insercion del ajuste general.
	 * @param factura, FActura a la que se le aplica el ajuste.
	 * @param metodoAjuste, Metodo de Ajuste utilizado.
	 * @param valorAjuste
	 * @param conceptoAjuste
	 * @param institucion
	 * @return >0 en caso de que no se genere error en la insercion.
	 */
	public int updateAjusteFacturaEmpresa(Connection con,double codigo,int factura, String metodoAjuste, double valorAjuste,String conceptoAjuste,int institucion);


	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @param nivel
	 */
	public void eliminarAjuste(Connection con, double codigoAjuste,int nivel);


	/**
	 * @param con
	 * @param codigoAjuste
	 * @param codFacturas
	 * @return
	 */
	public int eliminarAjusteFacturaNoEstan(Connection con, double codigoAjuste, String codFacturas);


	/**
	 * @param con
	 * @param codigo
	 * @param factura
	 * @return
	 */
	public int eliminarAjusteServicio(Connection con, double codigo, int factura);


	/**
	 * @param con
	 * @param consecutivoAjuste
	 * @param tipoAjuste
	 * @param institucion
	 * @param estado
	 * @return
	 */
	public ResultSetDecorator cargarEncabezadoAjuste(Connection con, String consecutivoAjuste, String tipoAjuste, int institucion, int estado);

	/**
	 * @param con
	 * @param codigo, codigo del ajuste, se obtiene a traves de la secuencia seq_ajustes_empresa.
	 * @param castigo_cartera, indica si el ajuste es un castigo o no.
	 * @param conceptoCastigoCartera, en caso de que sea castigo se requiere el cocepto de castigo.
	 * @param fechaAjuste
	 * @param fechaElaboracion
	 * @param horaElaboracion
	 * @param conceptoAjuste, concepto de porque se hizo el ajuste.
	 * @param metodoAjuste, metodo de ajuste A-Automatico, P-Porcentual, M-Manual
	 * @param valorAjuste
	 * @param observaciones
	 * @return >0 en caso de que no se genere error en la insercion.
	 * 
	 */
	public int actualizarAjuste(Connection con, double codigo, boolean castigoCartera, String conceptoCastigoCartera, String fechaAjuste, String fechaElaboracion, String horaElaboracion, String conceptoAjuste, String metodoAjuste, double valorAjuste, String observaciones);


	/**
	 * @param con
	 * @param codigoAjuste
	 * @param codigoEstadoCarteraAnulado
	 * @return
	 */
	public int cambiarEstadoAjuste(Connection con, double codigoAjuste, int codigoEstadoCarteraAnulado);


	/**
	 * @param con
	 * @param codigoFactura
	 * @param campoBusqueda
	 * @param valorCampoBusqueda
	 * @param servicios
	 * @return
	 */
	public ResultSetDecorator cargarDetalleFacturaAvanzada(Connection con, int codigoFactura, String campoBusqueda, String valorCampoBusqueda, boolean servicios);


	/**
	 * @param con
	 * @param numeroCuentaCobro
	 * @param institucion
	 * @param valorCampoBusqueda
	 * @return
	 */
	public ResultSetDecorator cargarFacturasCuentaCobroAvanzada(Connection con, double numeroCuentaCobro, int institucion, String valorCampoBusqueda);


	/**
	 * @param con
	 * @param codigoAjuste
	 * @param motivoAnulacion
	 * @param loginUsuario
	 * @param fechaActual
	 * @param horaActual
	 * @return
	 */
	public boolean anularAjuste(Connection con, double codigoAjuste, String motivoAnulacion, String loginUsuario, String fechaActual, String horaActual);


	/**
	 * Metodo que inserta un ajuste de reversion.
	 * @param con
	 * @param consecutivoAjuste
	 * @param tipoAjuste
	 * @param fechaAjuste
	 * @param fechaElaboracion
	 * @param horaElaboracion
	 * @param usuario
	 * @param observaciones
	 * @param estado
	 * @param ajusteResversado
	 * @param codAjusteReversado
	 * @return
	 */
	public boolean ingresarAjusteReversion(Connection con, String consecutivoAjuste, int tipoAjuste, String fechaAjuste, String fechaElaboracion, String horaElaboracion, String usuario, String observaciones, int estado, boolean ajusteResversado, double codAjusteReversado);


	/**
	 * @param con
	 * @param codigoAjuste
	 * @param valorCampo
	 * @return
	 */
	public boolean cambiarAtributoReversion(Connection con, double codigoAjuste, boolean valorCampo);


	/**
	 * @param con
	 * @param fechaAjuste
	 * @param institucion
	 * @return
	 */
	public ResultSetDecorator buscarAjustesAprobadosPorFechaParaReversion(Connection con, String fechaAjuste, int institucion);

    /**
     * 
     * @param con
     * @param detFactSolicitud
     * @return
     */
    public HashMap cargarAsociosServiciosCirugia(Connection con, int detFactSolicitud);

    /**
     * 
     * @param con
     * @param detFactSolicitud
     * @return
     */
    public HashMap cargarDetallePaquete(Connection con,int detFactSolicitud);

    /**
     * 
     * @param con
     * @param map
     * @return
     */
    public boolean insertarAsociosServicio(Connection con, HashMap mapa);


    /**
     * 
     * @param con
     * @param codAjuste
     * @param factura
     * @param detFacSol
     * @param servicioGeneral
     * @return
     */
    public int eliminarAjusteServicioAsocios(Connection con,int codigopk, double codAjuste, int factura, int detFacSol);


    /**
     * Metodo que actualiza el valor del ajuste institucion y pool para los
     * servicios de cirugia, ya que debe tomar la sumatoria del los servicios de los asocios.
     * @param con
     */
    public int updateValAjusteInstPoolSerCx(Connection con,int codigopk, double codAjuste, int factura, int detFacSol);


    /**
     * 
     * @param con
     * @param codAjuste
     * @param factura
     * @param detFacSol
     * @return
     */
    public HashMap consultarServiciosAsociosAjustes(Connection con,int codigopk, double codAjuste, int factura, int detFacSol);


    /**
     * 
     * @param con
     * @param codigo
     * @param factura
     * @param detFactSolicitud
     * @return
     */
	public int eliminarAjusteDetallePaquetes(Connection con,int codigopk, double codigo, int factura, int detFactSolicitud);


	/**
	 * 
	 * @param con
	 * @param detallePaquete
	 * @return
	 */
	public boolean insertarDetallePaquetes(Connection con, HashMap detallePaquete);


	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public int obtenerCodigoFacturaAjuste(Connection con, double codigoAjuste);


	
}
