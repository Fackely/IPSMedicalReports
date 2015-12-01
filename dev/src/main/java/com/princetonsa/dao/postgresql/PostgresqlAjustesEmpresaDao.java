/*
 * @(#)PostgresqlAjustesEmpresaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import util.ConstantesBD;

import com.princetonsa.dao.AjustesEmpresaDao;
import com.princetonsa.dao.sqlbase.SqlBaseAjustesEmpresaDao;

/**
 * Implementación postgresql de las funciones de acceso a la fuente de datos
 * para los ajustes de empresa
 *
 * @version 1.0, Julio 22 / 2005	
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 */
public class PostgresqlAjustesEmpresaDao implements AjustesEmpresaDao
{
	
	/**
	 * Cadena para insertar el cabezote del ajuste.
	 */
	private static String insertaAjusteGeneral="INSERT INTO ajustes_empresa(codigo,consecutivo_ajuste,tipo_ajuste,institucion,castigo_cartera,concepto_castigo_cartera,fecha_ajuste,fecha_elaboracion,hora_elaboracion,usuario,cuenta_cobro,concepto_ajuste,metodo_ajuste,valor_ajuste,observaciones,estado,ajuste_reversado,cod_ajuste_reversado) values(nextval('seq_ajustes_empresa'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * 
	 */
	private static String tomarValorSecuencia="select nextval('seq_ajustes_empresa') as codigo ";
	
	/**
     * Cadena para insertar la reversion de un ajuste a nivel de ajus_det_fact_empresa.
     */
    private static String cadenaIsertarAjusteServiciosCirugiaReversion="INSERT INTO ajus_asocios_fact_empresa (" +
    																				" codigo_pk," +
    																				" codigo_pk_ser_art," +
    																				" codigo_ajuste," +
    																				" factura," +
    																				" consecutivo_aso_det_fac," +
    																				" det_aso_fac_solicitud," +
    																				" servicio_asocio," +
    																				" codigo_medico," +
    																				" pool," +
    																				" valor_ajuste," +
    																				" valor_ajuste_pool," +
    																				" valor_ajuste_institucion," +
    																				" concepto_ajuste," +
    																				" institucion" +
    																		" ) " +
    																		" select " +
    																				" nextval('cartera.seq_ajusasodetfacemp_pk')," +
    																				" ?," + //llaves
    																				" ?," +  //codigo ajuste
    																				" factura," + //factura
    																				" consecutivo_aso_det_fac," + //consecutivo
    																				" det_aso_fac_solicitud," + 
    																				" servicio_asocio," +
    																				" codigo_medico," + //codigo medico
    																				" pool," +
    																				" valor_ajuste," +
    																				" valor_ajuste_pool," +
    																				" valor_ajuste_institucion," +
    																				" concepto_ajuste," +
    																				" institucion " +
    																		" from ajus_asocios_fact_empresa " +
    																		" where codigo_pk_ser_art=?";
    
    /**
     * Cadena para insertar la reversion de un ajuste a nivel de ajus_det_fact_empresa.
     */
    private static String cadenaIsertarAjusteDetallePaqueteReversion="INSERT INTO ajus_paquetizacion_det_factura(" +
																		    " codigo_pk," +
																			" codigo_pk_ser_art," +
																			" codigo_ajuste," +
    																		" factura," +
    																		" det_fac_solicitud," +
    																		" paq_det_factura," +
    																		" valor_ajuste," +
    																		" valor_ajuste_pool," +
    																		" valor_ajuste_institucion," +
    																		" concepto_ajuste," +
    																		" institucion" +
    																		") " +
    																		" select " +
    																			" nextval('cartera.seq_ajuspaqdetfacemp_pk')," +
    																			" ?," + //llaves
    																			" ?," +
    																			" factura," +
    																			" det_fac_solicitud," +
    																			" paq_det_factura," +
    																			" valor_ajuste," +
    																			" valor_ajuste_pool," +
    																			" valor_ajuste_institucion," +
    																			" concepto_ajuste," +
    																			" institucion " +
    																		" from ajus_paquetizacion_det_factura " +
    																		" where codigo_pk_ser_art=?";

    
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
	 * @return >0 en caso de que no se genere error en la insercion.
	 */
	public int ingresarAjusteGeneral(Connection con, String consecutivo, int tipo_ajuste, int institucion, boolean castigo_cartera, 
			String conceptoCastigoCartera, String fechaAjuste,String fechaElaboracion,String horaElaboracion,String usuario, 
			double cuentaCobro,String conceptoAjuste,String metodoAjuste,double valorAjuste,String observaciones, int estado, boolean reversado, double codAjusteReversado )
	{
		return SqlBaseAjustesEmpresaDao.ingresarAjusteGeneral(con,consecutivo,tipo_ajuste,institucion,castigo_cartera,conceptoCastigoCartera,fechaAjuste,fechaElaboracion,horaElaboracion,usuario,cuentaCobro,conceptoAjuste,metodoAjuste,valorAjuste,observaciones,estado,reversado,codAjusteReversado,insertaAjusteGeneral);
	}
	
	
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
	public int ingresarAjustesFactura(Connection con,double codigo,int factura, String metodoAjuste, double valorAjuste,String conceptoAjuste,int institucion)
	{
		return SqlBaseAjustesEmpresaDao.ingresarAjustesFactura(con,codigo,factura,metodoAjuste,valorAjuste,conceptoAjuste,institucion);
	}

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
								String metodoAjuste,double valorAjuste,double valorAjustePool, double valorAjusteInstitucion,String conceptoAjuste,int institucion)
	{
		return SqlBaseAjustesEmpresaDao.ingresarAjustedDetalleFactura(con,codigo,factura,detFacturaSolicitud,pool,codigoMedicoResponsable,metodoAjuste,valorAjuste,valorAjustePool,valorAjusteInstitucion,conceptoAjuste,institucion);
	}
	
	/**
	 * @param con
	 * @param codigoFactura
	 * @param castigoCartera
	 * @param ajustesCxCRadicada
	 */
	public ResultSetDecorator cargarUnaFactura(Connection con, int codigoFactura, boolean castigoCartera, boolean ajustesCxCRadicada, boolean modificacion, double codigoAjuste)
	{
		return SqlBaseAjustesEmpresaDao.cargarUnaFactura(con,codigoFactura,castigoCartera,ajustesCxCRadicada,modificacion, codigoAjuste);
	}
	

	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public ResultSetDecorator cargarDetalleFactura(Connection con, int codigoFactura,boolean servicio)
	{
		return SqlBaseAjustesEmpresaDao.cargarDetalleFactura(con,codigoFactura,servicio);
	}
	
	/**
	 * @param con
	 * @param codigoFactura
	 * @param campoBusqueda
	 * @param valorCampoBusqueda
	 * @param servicios
	 * @return
	 */
	public ResultSetDecorator cargarDetalleFacturaAvanzada(Connection con, int codigoFactura, String campoBusqueda, String valorCampoBusqueda, boolean servicios)
	{
		return SqlBaseAjustesEmpresaDao.cargarDetalleFacturaAvanzada(con,codigoFactura,campoBusqueda,valorCampoBusqueda,servicios);
	}

	
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
	public int updateAjustedDetalleFactura(Connection con,int codigopk, double codigo, int factura, int detFacturaSolicitud, String metodoAjuste, double valorAjuste,double valorAjustePool, double valorAjusteInstitucion,String conceptoAjuste,int institucion)
	{
		return SqlBaseAjustesEmpresaDao.updateAjustedDetalleFactura(con,codigopk,codigo,factura,detFacturaSolicitud,metodoAjuste,valorAjuste,valorAjustePool,valorAjusteInstitucion,conceptoAjuste,institucion);
	}
	
	/**
	 * @param con
	 * @param codigoAjuste
	 * @param codigoFactura
	 * @return
	 */
	public ResultSetDecorator cargarDetalleAjusteFactura(Connection con, double codigoAjuste, int codigoFactura,boolean servicio)
	{
		return SqlBaseAjustesEmpresaDao.cargarDetalleAjusteFactura(con,codigoAjuste,codigoFactura,servicio);
	}

	/**
	 * @param con
	 * @param codigoFactura
	 * @param codigoArticuloAgrupacion
	 * @return
	 */
	public ResultSetDecorator cargarDetalleFacturasArticuloAgrupado(Connection con, int codigoFactura, int codigoArticuloAgrupacion)
	{
		return SqlBaseAjustesEmpresaDao.cargarDetalleFacturasArticuloAgrupado(con, codigoFactura, codigoArticuloAgrupacion);
	}
	
	
	/**
	 * @param con
	 * @param cuentaCobro
	 * @param castigoCartera
	 * @param ajustesCxCRadicada
	 * @param modificacion
	 * @return
	 */
	public ResultSetDecorator cargarCuentaCobro(Connection con, double cuentaCobro, int institucion, boolean castigoCartera, boolean ajustesCxCRadicada,boolean modificacion)
	{
		return SqlBaseAjustesEmpresaDao.cargarCuentaCobro(con,cuentaCobro,institucion,castigoCartera,ajustesCxCRadicada,modificacion);
	}
	
	
	/**
	 * @param con
	 * @param numeroCuentaCobro
	 * @return
	 */
	public ResultSetDecorator cargarFacturasCuentaCobro(Connection con, double numeroCuentaCobro, int institucion)
	{
		return SqlBaseAjustesEmpresaDao.cargarFacturasCuentaCobro(con,numeroCuentaCobro,institucion);		
	}
	
	/**
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public ResultSetDecorator cargarFacturasCuentaCobroAjuste(Connection con, double codigoAjuste)
	{
		return SqlBaseAjustesEmpresaDao.cargarFacturasCuentaCobroAjuste(con,codigoAjuste);
	}

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
	public int updateAjusteFacturaEmpresa(Connection con,double codigo,int factura, String metodoAjuste, double valorAjuste,String conceptoAjuste,int institucion)
	{
		return SqlBaseAjustesEmpresaDao.updateAjusteFacturaEmpresa(con,codigo,factura,metodoAjuste,valorAjuste,conceptoAjuste,institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @param nivel
	 */
	public void eliminarAjuste(Connection con, double codigoAjuste,int nivel)
	{
		SqlBaseAjustesEmpresaDao.eliminarAjuste(con, codigoAjuste,nivel);
	}
	
	
	/**
	 * @param con
	 * @param codigoAjuste
	 * @param codFacturas
	 * @return
	 */
	public int eliminarAjusteFacturaNoEstan(Connection con, double codigoAjuste, String codFacturas)
	{
		return SqlBaseAjustesEmpresaDao.eliminarAjusteFacturaNoEstan(con, codigoAjuste,codFacturas);
	}
	
	/**
	 * @param con
	 * @param codigo
	 * @param factura
	 * @return
	 */
	public int eliminarAjusteServicio(Connection con, double codigo, int factura)
	{
		return SqlBaseAjustesEmpresaDao.eliminarAjusteServicio(con,codigo,factura);
	}
	
	
	/**
	 * @param con
	 * @param consecutivoAjuste
	 * @param tipoAjuste
	 * @param institucion
	 * @return
	 */
	public ResultSetDecorator cargarEncabezadoAjuste(Connection con, String consecutivoAjuste, String tipoAjuste, int institucion,int estado)
	{
		return SqlBaseAjustesEmpresaDao.cargarEncabezadoAjuste(con,consecutivoAjuste,tipoAjuste,institucion,estado);
	}
	
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
	public int actualizarAjuste(Connection con, double codigo, boolean castigoCartera, String conceptoCastigoCartera, String fechaAjuste, String fechaElaboracion, String horaElaboracion, String conceptoAjuste, String metodoAjuste, double valorAjuste, String observaciones)
	{
		return SqlBaseAjustesEmpresaDao.actualizarAjuste(con, codigo, castigoCartera, conceptoCastigoCartera, fechaAjuste, fechaElaboracion, horaElaboracion, conceptoAjuste, metodoAjuste, valorAjuste, observaciones);
	}
	
	/**
	 * @param con
	 * @param codigoAjuste
	 * @param codigoEstadoCarteraAnulado
	 * @return
	 */
	public int cambiarEstadoAjuste(Connection con, double codigoAjuste, int codigoEstadoCarteraAnulado)
	{
		return SqlBaseAjustesEmpresaDao.cambiarEstadoAjuste(con,codigoAjuste,codigoEstadoCarteraAnulado);
	}
	
	/**
	 * @param con
	 * @param numeroCuentaCobro
	 * @param institucion
	 * @param valorCampoBusqueda
	 * @return
	 */
	public ResultSetDecorator cargarFacturasCuentaCobroAvanzada(Connection con, double numeroCuentaCobro, int institucion, String valorCampoBusqueda)
	{
		return SqlBaseAjustesEmpresaDao.cargarFacturasCuentaCobroAvanzada(con,numeroCuentaCobro,institucion,valorCampoBusqueda);
	}
	
	/**
	 * @param con
	 * @param codigoAjuste
	 * @param motivoAnulacion
	 * @param loginUsuario
	 * @param fechaActual
	 * @param horaActual
	 * @return
	 */
	public boolean anularAjuste(Connection con, double codigoAjuste, String motivoAnulacion, String loginUsuario, String fechaActual, String horaActual)
	{
		return  SqlBaseAjustesEmpresaDao.anularAjuste(con,codigoAjuste, motivoAnulacion,loginUsuario,fechaActual,horaActual);
	}
	
	/**
	 * Metodo que inserta un ajuste de reversion.
	 * @param con
	 * @param codigo
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
	public boolean ingresarAjusteReversion(Connection con, String consecutivoAjuste, int tipoAjuste, String fechaAjuste, String fechaElaboracion, String horaElaboracion, String usuario, String observaciones, int estado, boolean ajusteReversado, double codAjusteReversado)
	{
		PreparedStatementDecorator ps;
		try {
			ps =  new PreparedStatementDecorator(con.prepareStatement(tomarValorSecuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return  SqlBaseAjustesEmpresaDao.ingresarAjusteReversion(con,rs.getInt("codigo"),consecutivoAjuste,tipoAjuste,fechaAjuste,fechaElaboracion,horaElaboracion,usuario,observaciones,estado,ajusteReversado,codAjusteReversado,cadenaIsertarAjusteServiciosCirugiaReversion,cadenaIsertarAjusteDetallePaqueteReversion);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @param con
	 * @param codigoAjuste
	 * @param valorCampo
	 * @return
	 */
	public boolean cambiarAtributoReversion(Connection con, double codigoAjuste, boolean valorCampo)
	{
		return  SqlBaseAjustesEmpresaDao.cambiarAtributoReversion(con,codigoAjuste,valorCampo);
	}
	
	/**
	 * @param con
	 * @param fechaAjuste
	 * @param institucion
	 * @return
	 */
	public ResultSetDecorator buscarAjustesAprobadosPorFechaParaReversion(Connection con, String fechaAjuste, int institucion)
	{
		return SqlBaseAjustesEmpresaDao.buscarAjustesAprobadosPorFechaParaReversion(con,fechaAjuste,institucion);
	}
    
    /**
     * 
     * @param con
     * @param detFactSolicitud
     * @param servicioCirugia
     * @return
     */
    public HashMap cargarAsociosServiciosCirugia(Connection con, int detFactSolicitud)
    {
        return SqlBaseAjustesEmpresaDao.cargarAsociosServiciosCirugia(con,detFactSolicitud);
    }
    
    /**
     * 
     * @param con
     * @param detFactSolicitud
     * @return
     */
    public HashMap cargarDetallePaquete(Connection con, int detFactSolicitud)
    {
        return SqlBaseAjustesEmpresaDao.cargarDetallePaquete(con,detFactSolicitud);
    }
    

    /**
     * 
     * @param con
     * @param map
     * @return
     */
    public boolean insertarAsociosServicio(Connection con, HashMap mapa)
    {
        return SqlBaseAjustesEmpresaDao.insertarAsociosServicio(con,mapa);
    }
    

    /**
     * 
     * @param con
     * @param codAjuste
     * @param factura
     * @param detFacSol
     * @param servicioGeneral
     * @return
     */
    public int eliminarAjusteServicioAsocios(Connection con,int codigopk, double codAjuste, int factura, int detFacSol)
    {
        return SqlBaseAjustesEmpresaDao.eliminarAjusteServicioAsocios(con,codigopk,codAjuste,factura,detFacSol);
    }
    

    /**
     * Metodo que actualiza el valor del ajuste institucion y pool para los
     * servicios de cirugia, ya que debe tomar la sumatoria del los servicios de los asocios.
     * @param con
     */
    public int updateValAjusteInstPoolSerCx(Connection con, int codigopk,double codAjuste, int factura, int detFacSol)
    {
        return SqlBaseAjustesEmpresaDao.updateValAjusteInstPoolSerCx(con,codigopk,codAjuste,factura,detFacSol);
    }
    
    /**
     * 
     * @param con
     * @param codAjuste
     * @param factura
     * @param detFacSol
     * @return
     */
    public HashMap consultarServiciosAsociosAjustes(Connection con, int codigopk,double codAjuste, int factura, int detFacSol)
    {
        return SqlBaseAjustesEmpresaDao.consultarServiciosAsociosAjustes(con,codigopk,codAjuste,factura,detFacSol);
    }
    

    /**
     * 
     * @param con
     * @param codigo
     * @param factura
     * @param detFactSolicitud
     * @return
     */
	public int eliminarAjusteDetallePaquetes(Connection con,int codigopk, double codigo, int factura, int detFactSolicitud)
	{
		return SqlBaseAjustesEmpresaDao.eliminarAjusteDetallePaquetes(con,codigopk, codigo, factura, detFactSolicitud);
	}
	


	/**
	 * 
	 * @param con
	 * @param detallePaquete
	 * @return
	 */
	public boolean insertarDetallePaquetes(Connection con, HashMap detallePaquete)
	{
		return SqlBaseAjustesEmpresaDao.insertarDetallePaquetes(con,detallePaquete);
	}


	/**
	 * 
	 */
	public int obtenerCodigoFacturaAjuste(Connection con, double codigoAjuste) 
	{
		return SqlBaseAjustesEmpresaDao.obtenerCodigoFacturaAjuste(con,codigoAjuste);
	}
}
