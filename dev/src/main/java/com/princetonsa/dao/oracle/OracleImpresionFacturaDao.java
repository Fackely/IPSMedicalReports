/*
 * 
 * @author artotor
 *
 */

package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import util.ConstantesBD;

import com.princetonsa.dao.ImpresionFacturaDao;
import com.princetonsa.dao.sqlbase.SqlBaseImpresionFacturaDao;
import com.princetonsa.dto.facturacion.DtoDatosRepsonsableFactura;

/**
 * 
 * @author artotor
 *
 */
public class OracleImpresionFacturaDao implements ImpresionFacturaDao 
{
	
	/**
	 * Cadena para la consulta de los servicios de una factura, muestra los servicios agrupados menos las cirugias en formato de impresio estandar.
	 */
	private static String conSeccServiciosFormatoEstatico=" select " +
																"tabla.codigofactura as codigofactura," +
																"tabla.coddetfac as codigodetallefactura," +
																"tabla.servicio as servicio," +
																"tabla.solicitud as  solicitud," +
																"tabla.codigopropeitario as codigopropeitario," +
																"tabla.procedimiento as procedimiento," +
																"tabla.escirugia as escirugia," +
																"tabla.cantidad as cantidad," +
																"tabla.valorunitario as valorunitario," +
																"tabla.valortotal as valortotal " +
															" FROM " +
															"(" +
																" (SELECT f.codigo as codigofactura,'' as coddetfac,dfs.servicio as servicio,'' as solicitud," +
																"case when getcodigopropietario(f.convenio,dfs.servicio) is null or getcodigopropietario(f.convenio,dfs.servicio)='' or getnombreservicio(dfs.servicio,getcodimpservconvenio(f.convenio)) is null  or getnombreservicio(dfs.servicio,getcodimpservconvenio(f.convenio))='' then getcodigopropservicio2(dfs.servicio,"+ConstantesBD.codigoTarifarioCups+") else getcodigopropietario(f.convenio,dfs.servicio) end as codigopropeitario," +
	                                                            "case when getcodigopropietario(f.convenio,dfs.servicio) is null or getcodigopropietario(f.convenio,dfs.servicio)='' or getnombreservicio(dfs.servicio,getcodimpservconvenio(f.convenio)) is null  or getnombreservicio(dfs.servicio,getcodimpservconvenio(f.convenio))='' then getnombreservicio(dfs.servicio,"+ConstantesBD.codigoTarifarioCups+") else getnombreservicio(dfs.servicio,getcodimpservconvenio(f.convenio)) end as procedimiento," +
	                                                            "'false' as escirugia," +
	                                                            "sum(dfs.cantidad_cargo)||'' as cantidad, " +
	                                                            "(sum(dfs.valor_total)/sum(dfs.cantidad_cargo))||'' as valorunitario, " +
	                                                            "sum(dfs.valor_total)||'' as valortotal " +
	                                                            "from facturas f " +
	                                                            "inner join det_factura_solicitud dfs on (f.codigo=dfs.factura) " +
	                                                            "inner join solicitudes s on (dfs.solicitud=s.numero_solicitud) " +
	                                                            "where dfs.articulo is null and s.tipo<>"+ConstantesBD.codigoTipoSolicitudCirugia+
	                                                            "  and dfs.cantidad_cargo>0 " +
	                                                            "group by f.codigo, dfs.servicio, f.convenio) " +
	                                                            "UNION " +
	                                                            "(SELECT f.codigo as codigofactura,dfs.codigo||'' as coddetfac,dfs.servicio as servicio,dfs.solicitud||'' as solicitud," +
	                                                            "case when getcodigopropietario(f.convenio,dfs.servicio) is null or getcodigopropietario(f.convenio,dfs.servicio)='' or getnombreservicio(dfs.servicio,(select tarifario_oficial from esquemas_tarifarios where codigo=getEsquemaTarifarioSerArt(f.sub_cuenta,getcontratosubcuenta(f.sub_cuenta),dfs.servicio,'S'))) is null  or getnombreservicio(dfs.servicio,(select tarifario_oficial from esquemas_tarifarios where codigo=getEsquemaTarifarioSerArt(f.sub_cuenta,getcontratosubcuenta(f.sub_cuenta),dfs.servicio,'S')))='' then (select codigo_propietario from referencias_servicio where servicio=dfs.servicio and tipo_tarifario="+ConstantesBD.codigoTarifarioCups+") else getcodigopropietario(f.convenio,dfs.servicio) end as codigopropeitario," +
	                                                            "case when getcodigopropietario(f.convenio,dfs.servicio) is null or getcodigopropietario(f.convenio,dfs.servicio)='' or getnombreservicio(dfs.servicio,(select tarifario_oficial from esquemas_tarifarios where codigo=getEsquemaTarifarioSerArt(f.sub_cuenta,getcontratosubcuenta(f.sub_cuenta),dfs.servicio,'S'))) is null  or getnombreservicio(dfs.servicio,(select tarifario_oficial from esquemas_tarifarios where codigo=getEsquemaTarifarioSerArt(f.sub_cuenta,getcontratosubcuenta(f.sub_cuenta),dfs.servicio,'S')))='' then getnombreservicio(dfs.servicio,"+ConstantesBD.codigoTarifarioCups+") else getnombreservicio(dfs.servicio,(select tarifario_oficial from esquemas_tarifarios where codigo=getEsquemaTarifarioSerArt(f.sub_cuenta,getcontratosubcuenta(f.sub_cuenta),dfs.servicio,'S'))) end as procedimiento," +
	                                                            "'true' as escirugia," +
	                                                            "'' as cantidad, " +
	                                                            "'' as valorunitario, " +
	                                                            "'' as valortotal " +
	                                                            "from facturas f " +
	                                                            "inner join det_factura_solicitud dfs on (f.codigo=dfs.factura) " +
	                                                            "inner join solicitudes s on (dfs.solicitud=s.numero_solicitud) " +
	                                                            "where dfs.articulo is null and s.tipo="+ConstantesBD.codigoTipoSolicitudCirugia+")" +
	                                                           ") tabla " +
	                                                           "where tabla.codigofactura=? order by escirugia,procedimiento";
	
	
	/**
	 * Cadena para la consulta de los servicios de una factura, muestra los servicios agrupados menos las cirugias en formato de impresio Versalles.
	 */
	private static String conSeccServiciosFormatoVersalles=" select " +
																"tabla.codigofactura as codigofactura," +
																"tabla.coddetfac as codigodetallefactura," +
																"tabla.servicio as servicio," +
																"tabla.solicitud as  solicitud," +
																"tabla.codigopropeitario as codigopropeitario," +
																"tabla.procedimiento as procedimiento," +
																"tabla.escirugia as escirugia," +
																"tabla.cantidad as cantidad," +
																"tabla.valorunitario as valorunitario," +
																"tabla.valortotal as valortotal, " +
																"coalesce(manejopaciente.getAutoFacSerArt(tabla.codigofactura, tabla.servicio), ' ') as numeroautorizacion " +
															" FROM " +
															"(" +
																" (SELECT f.codigo as codigofactura,'' as coddetfac,dfs.servicio as servicio,'' as solicitud," +
																"case when getcodigopropietario(f.convenio,dfs.servicio) is null or getcodigopropietario(f.convenio,dfs.servicio)='' or getnombreservicio(dfs.servicio,getcodimpservconvenio(f.convenio)) is null  or getnombreservicio(dfs.servicio,getcodimpservconvenio(f.convenio))='' then getcodigopropservicio2(dfs.servicio,"+ConstantesBD.codigoTarifarioCups+") else getcodigopropietario(f.convenio,dfs.servicio) end as codigopropeitario," +
	                                                            "case when getcodigopropietario(f.convenio,dfs.servicio) is null or getcodigopropietario(f.convenio,dfs.servicio)='' or getnombreservicio(dfs.servicio,getcodimpservconvenio(f.convenio)) is null  or getnombreservicio(dfs.servicio,getcodimpservconvenio(f.convenio))='' then getnombreservicio(dfs.servicio,"+ConstantesBD.codigoTarifarioCups+") else getnombreservicio(dfs.servicio,getcodimpservconvenio(f.convenio)) end as procedimiento," +
	                                                            "'false' as escirugia," +
	                                                            "sum(dfs.cantidad_cargo)||'' as cantidad, " +
	                                                            "(sum(dfs.valor_total)/sum(dfs.cantidad_cargo))||'' as valorunitario, " +
	                                                            "sum(dfs.valor_total)||'' as valortotal " +
	                                                            "from facturas f " +
	                                                            "inner join det_factura_solicitud dfs on (f.codigo=dfs.factura) " +
	                                                            "inner join solicitudes s on (dfs.solicitud=s.numero_solicitud) " +
	                                                            "where dfs.articulo is null and s.tipo<>"+ConstantesBD.codigoTipoSolicitudCirugia+
	                                                            "  and dfs.cantidad_cargo>0 " +
	                                                            "group by f.codigo, dfs.servicio, f.convenio) " +
	                                                            "UNION " +
	                                                            "(SELECT f.codigo as codigofactura,dfs.codigo||'' as coddetfac,dfs.servicio as servicio,dfs.solicitud||'' as solicitud," +
	                                                            "case when getcodigopropietario(f.convenio,dfs.servicio) is null or getcodigopropietario(f.convenio,dfs.servicio)='' or getnombreservicio(dfs.servicio,(select tarifario_oficial from esquemas_tarifarios where codigo=getEsquemaTarifarioSerArt(f.sub_cuenta,getcontratosubcuenta(f.sub_cuenta),dfs.servicio,'S'))) is null  or getnombreservicio(dfs.servicio,(select tarifario_oficial from esquemas_tarifarios where codigo=getEsquemaTarifarioSerArt(f.sub_cuenta,getcontratosubcuenta(f.sub_cuenta),dfs.servicio,'S')))='' then (select codigo_propietario from referencias_servicio where servicio=dfs.servicio and tipo_tarifario="+ConstantesBD.codigoTarifarioCups+") else getcodigopropietario(f.convenio,dfs.servicio) end as codigopropeitario," +
	                                                            "case when getcodigopropietario(f.convenio,dfs.servicio) is null or getcodigopropietario(f.convenio,dfs.servicio)='' or getnombreservicio(dfs.servicio,(select tarifario_oficial from esquemas_tarifarios where codigo=getEsquemaTarifarioSerArt(f.sub_cuenta,getcontratosubcuenta(f.sub_cuenta),dfs.servicio,'S'))) is null  or getnombreservicio(dfs.servicio,(select tarifario_oficial from esquemas_tarifarios where codigo=getEsquemaTarifarioSerArt(f.sub_cuenta,getcontratosubcuenta(f.sub_cuenta),dfs.servicio,'S')))='' then getnombreservicio(dfs.servicio,"+ConstantesBD.codigoTarifarioCups+") else getnombreservicio(dfs.servicio,(select tarifario_oficial from esquemas_tarifarios where codigo=getEsquemaTarifarioSerArt(f.sub_cuenta,getcontratosubcuenta(f.sub_cuenta),dfs.servicio,'S'))) end as procedimiento," +
	                                                            "'true' as escirugia," +
	                                                            "'' as cantidad, " +
	                                                            "'' as valorunitario, " +
	                                                            "'' as valortotal " +
	                                                            "from facturas f " +
	                                                            "inner join det_factura_solicitud dfs on (f.codigo=dfs.factura) " +
	                                                            "inner join solicitudes s on (dfs.solicitud=s.numero_solicitud) " +
	                                                            "where dfs.articulo is null and s.tipo="+ConstantesBD.codigoTipoSolicitudCirugia+")" +
	                                                           ") tabla " +
	                                                           "where tabla.codigofactura=? order by escirugia,procedimiento";
	
	/**
	 * Metodo que realiza la consulta especifica para el encabezado de una impresion de factura en formato estatico.
	 * @param con
	 * @param codigoFactira
	 * @return
	 */
	public HashMap consultarSeccionPacienteFormatoEstatico(Connection con, String codigoFactira)
	{
		return SqlBaseImpresionFacturaDao.consultarSeccionPacienteFormatoEstatico(con,codigoFactira);
	}
	
	/**
	 * Metodo que realiza la consulta especifica para el encabezado de una impresion de factura en formato Versalles.
	 * @param con
	 * @param codigoFactira
	 * @return
	 */
	public HashMap consultarSeccionPacienteFormatoVersalles(Connection con, String codigoFactira)
	{
		return SqlBaseImpresionFacturaDao.consultarSeccionPacienteFormatoVersalles(con,codigoFactira);
	}

	/**
	 * Metodo que realiza la consulta especifica para el encabezado de una impresion de factura en formato estatico.
	 * @param con
	 * @param codigoFactira
	 * @return
	 */
	public HashMap anexoSolicitudesMedicamentosFechaFactura(Connection con, String codigoFactira)
	{
		return SqlBaseImpresionFacturaDao.anexoSolicitudesMedicamentosFechaFactura(con,codigoFactira);
	}
    /**
     * metodo para consultar la información de la institución
     * @param con Connection
     * @param codigoInstitucion int 
     * @return  HashMap
     */
    public HashMap consultarSeccionInstitucionFormatoEstatico( Connection con, int codigoInstitucion, String codigoFactura, double empresaInstitucion )
    {
        return SqlBaseImpresionFacturaDao.consultarSeccionInstitucionFormatoEstatico(con, codigoInstitucion, codigoFactura, empresaInstitucion);
    }
    /**
     * metodo para consultar la información de la institución para el formato de impresion de Versalles
     */
	public HashMap consultarSeccionInstitucionFormatoVersalles(Connection con,int codigoInstitucion, String codigoFactura,double empresaInstitucion) {
		return SqlBaseImpresionFacturaDao.consultarSeccionInstitucionFormatoVersalles(con, codigoInstitucion, codigoFactura, empresaInstitucion);
	}
    /**
     * metodo para consultar los servicios de una factura
     * @param con Connection
     * @param codigoFactira String
     * @return HashMap
     */
    public HashMap consultarSeccionServiciosFormatoEstatico( Connection con, String codigoFactira )
    {
        return SqlBaseImpresionFacturaDao.consultarSeccionServiciosFormatoEstatico(con, codigoFactira, conSeccServiciosFormatoEstatico);
    }
    
    /**
     * metodo para consultar los servicios de una factura para el formato de impresion de Versalles
     * @param con Connection
     * @param codigoFactira String
     * @return HashMap
     */
    public HashMap consultarSeccionServiciosFormatoVersalles( Connection con, String codigoFactira)
    {
        return SqlBaseImpresionFacturaDao.consultarSeccionServiciosFormatoVersalles(con, codigoFactira, conSeccServiciosFormatoVersalles);
    }
    
    /**
     * metodo para consultar el detalle de los servicios
     * @param con Connection
     * @param numSolicitud String 
     * @param codServicio String 
     * @return HashMap
     */
    public HashMap consultarSeccionDetServiciosFormatoEstatico( Connection con,String codigoDetalleFactura )
    {
        return SqlBaseImpresionFacturaDao.consultarSeccionDetServiciosFormatoEstatico(con, codigoDetalleFactura);
    }
    
    /**
     * metodo para consultar el detalle de los servicios para el formato de impresion de Versalles
     * @param con Connection
     * @param numSolicitud String 
     * @param codServicio String 
     * @return HashMap
     */
    public HashMap consultarSeccionDetServiciosFormatoVersalles( Connection con,String codigoDetalleFactura )
    {
        return SqlBaseImpresionFacturaDao.consultarSeccionDetServiciosFormatoVersalles(con, codigoDetalleFactura);
    }
    /**
     * metodo para consultar los articulos de la factura
     * @param con Connection
     * @param codigoFactira  String 
     * @return HashMap
     */
    public HashMap consultarSeccionArticulosFormatoEstatico( Connection con,String codigoFactira, String filtrarXInsumos )
    {
        return SqlBaseImpresionFacturaDao.consultarSeccionArticulosFormatoEstatico(con, codigoFactira, filtrarXInsumos);
    }
    
    /**
     * metodo para consultar los articulos de la factura para el formato de impresion de Versalles
     * @param con Connection
     * @param codigoFactira  String 
     * @return HashMap
     */
    public HashMap consultarSeccionArticulosFormatoVersalles( Connection con,String codigoFactira, String filtrarXInsumos )
    {
        return SqlBaseImpresionFacturaDao.consultarSeccionArticulosFormatoVersalles(con, codigoFactira, filtrarXInsumos);
    }
    
    /**
     * metodo para consultar los articulos de la factura
     * @param con Connection
     * @param codigoFactira  String 
     * @return HashMap
     */
	public HashMap anexoSolicitudesMedicamentosOrdenFechaFactura(Connection con, String codigoFactira)
	{
		return SqlBaseImpresionFacturaDao.anexoSolicitudesMedicamentosOrdenFechaFactura(con, codigoFactira);
	}
	

	/**
	 * 
	 * @param con
	 * @param codSubCuenta
	 * @return
	 */
	public HashMap consultarInfoCitaDadaCuenta(Connection con, String codSubCuenta)
	{
		return SqlBaseImpresionFacturaDao.consultarInfoCitaDadaCuenta(con,codSubCuenta);
	}
	
	/**
	 * Metodo que consulta valor letras factura por convenio
	 * y pasa valor a letras
	 * @param con
	 * @param codConvenio
	 * @return
	 */
	public HashMap consultarValorLetrasValor(Connection con, String codConvenio)
	{
		return SqlBaseImpresionFacturaDao.consultarValorLetrasValor(con, codConvenio);
	}
	
	/**
	 * @see com.princetonsa.dao.ImpresionFacturaDao#obtenerNombreResponsable(java.sql.Connection, java.lang.Integer, java.lang.Integer)
	 */
	public   DtoDatosRepsonsableFactura obtenerNombreResponsable(Connection con,Integer cuenta, Integer convenio) throws SQLException
	{
		return SqlBaseImpresionFacturaDao.obtenerNombreResponsable(con, cuenta, convenio);
	}
	
	/**
	 * @see com.princetonsa.dao.ImpresionFacturaDao#consultarTipoMontoFactura(java.sql.Connection, java.lang.Integer, java.lang.Integer)
	 */
	public  String consultarTipoMontoFactura(Connection con,Integer convenio, Integer ingreso) throws SQLException
	{
		return SqlBaseImpresionFacturaDao.consultarTipoMontoFactura(con, convenio, ingreso);
	}
}
