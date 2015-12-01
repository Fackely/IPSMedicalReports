/*
 * @(#)PostgresqlEstadoCuentaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import util.ConstantesBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.EstadoCuentaDao;
import com.princetonsa.dao.sqlbase.SqlBaseEstadoCuentaDao;

/**
 * Esta clase implementa el contrato estipulado en 
 * <code>EgresoDao</code>, proporcionando los servicios
 * de acceso a una base de datos PostgreSQL requeridos 
 * por la clase <code>EstadoCuenta</code>
 * 
 *	@version 1.0, 5/08/2004
 */
public class PostgresqlEstadoCuentaDao implements EstadoCuentaDao 
{

	/**
	 * Consulta los Codigos de la Facturas asociadas a una subcuenta con o sin solicitud
	 */
	private static String consultaCodigosFacturas="SELECT getcodigosfacturas(?,?) AS codf ";
	
	/**
	 * Implementación del método que carga todas las
	 * cuentas del paciente en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.EstadoCuentaDao#cargarTodasCuentaPaciente (Connection , int ) throws SQLException 
	 */
	public ArrayList<HashMap<String, Object>> cargarTodasCuentaPaciente (Connection con, int codigoPaciente) 
	{
		return SqlBaseEstadoCuentaDao.cargarTodasCuentaPaciente (con, codigoPaciente) ;
	}
	
	/**
	 * Método que consulta todos los convenios de un ingreso
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public  ArrayList<HashMap<String, Object>> cargarTodosConvenioIngreso (Connection con, String idIngreso) 
	{
		String cargarTodosConveniosIngresoStr = "SELECT "+ 
			"sc.sub_cuenta, "+ 
			"sc.convenio AS codigo_convenio, "+
			"con.tipo_regimen AS tipo_regimen, "+
			"CASE WHEN con.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"' THEN getNomDeudorIngreso(sc.ingreso) ELSE con.nombre ||' - '|| coalesce(getnumerocontrato(sc.contrato),'') END  AS nombre_convenio, "+  
			"getnombreestrato(sc.clasificacion_socioeconomica) as estrato_social, "+
			"CASE WHEN dg.porcentaje IS NULL THEN coalesce(dg.valor,0) ||'' ELSE '% ' || dg.porcentaje  END AS nombre_monto_cobro, "+
			"tipm.nombre as tipo_monto, "+ 
			"sc.naturaleza_paciente as codigo_naturaleza, "+ 
			"getnomnatpacientes(sc.naturaleza_paciente) as nombre_naturaleza," +
			"CASE WHEN con.info_adic_ingreso_convenios = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END as es_convenio_poliza,   "+
			"(SELECT list(fac.consecutivo_factura||'') FROM facturas fac WHERE fac.sub_cuenta=sc.sub_cuenta AND fac.estado_facturacion="+ConstantesBD.codigoEstadoFacturacionFacturada+" ) AS numerofactura " +
			"FROM sub_cuentas sc "+ 
			"INNER JOIN convenios con ON (con.codigo=sc.convenio) " +
			"LEFT OUTER JOIN detalle_monto mc ON (mc.detalle_codigo=sc.monto_cobro) "+
			"LEFT OUTER JOIN tipos_monto tipm ON (mc.tipo_monto_codigo=tipm.codigo) "+ 
			"LEFT JOIN detalle_monto_general dg ON (mc.detalle_codigo=dg.detalle_codigo) "+
			"WHERE sc.ingreso = ? "+ 
			"ORDER BY nombre_convenio";
		
		return SqlBaseEstadoCuentaDao.cargarTodosConvenioIngreso(con, idIngreso,cargarTodosConveniosIngresoStr);
	}
	
	/**
	  * Método que consulta las solicitudes de un convenio
	  * @param con
	  * @param campos
	  * @return
	  */
	public HashMap<String, Object> cargarTodasSolicitudesSubCuenta (Connection con, HashMap campos)
	{
		return SqlBaseEstadoCuentaDao.cargarTodasSolicitudesSubCuenta(con, campos);
	}
	
	/**
	 * Método que realiza la busqueda avanzada de las solicitudes de una sub cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> buscarSolicitudesSubCuenta(Connection con,HashMap campos)
	{
		return SqlBaseEstadoCuentaDao.buscarSolicitudesSubCuenta(con, campos);
	}
	
	/**
	 * Valor total de los cargos (Independiente convenio o paciente)
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public double valorTotalCargos(Connection con, String idSubCuenta, boolean incluyePyp)
	{
		return SqlBaseEstadoCuentaDao.valorTotalCargos(con, idSubCuenta,incluyePyp);
	}
	
	/**
	 * Método que carga la informacion del monto de cobro para un convenio específico
	 * @param con
	 * @param idSubCuenta
	 * @return
	 */
	public HashMap cargarMontoCobroSubCuenta(Connection con,String idSubCuenta)
	{
		return SqlBaseEstadoCuentaDao.cargarMontoCobroSubCuenta(con, idSubCuenta);
	}
	
	/***
	 * Metodo para consulta los abonos de una cuenta evaluando cada uno de sus casos
	 * @param con
	 * @param idCuenta
	 * @param codigoPaciente
	 * @return
	 * @throws SQLException
	 */
	public double consultarAbonos (Connection con, int idCuenta, int codigoPaciente, Integer ingreso) 
	{
		return SqlBaseEstadoCuentaDao.consultarAbonos(con, idCuenta, codigoPaciente, ingreso);
	}
	
	/**
	 * Método que consulta el detalle del cargo de un solicitud de servicio/articulo
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> cargarDetalleCargoServicioArticulo(Connection con,HashMap campos)
	{
		return SqlBaseEstadoCuentaDao.cargarDetalleCargoServicioArticulo(con, campos);
	}
	
	/**
	 * 
	 * M&eacute;todo para mostrar el detalle de la solicitud con despacho de equivalentes 
	 * @author Diana Ruiz
	 * @param con
	 * @param campos
	 * @return
	 * 
	 */
	public HashMap<String, Object> cargarDetalleCargoArticuloConEquivalente(Connection con,HashMap campos)
	{
		return SqlBaseEstadoCuentaDao.cargarDetalleCargoArticuloConEquivalente(con, campos);
	}
	
	/**
	 * Método que carga el detalle del cargo de una cirugía
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> cargarDetalleCargoCirugia(Connection con, HashMap campos)
	{
		return SqlBaseEstadoCuentaDao.cargarDetalleCargoCirugia(con, campos);
	}
	
	/**
	 * Metodo que obtiene los numeros de carnet de un paciente
	 * @param con
	 * @param convenio
	 * @param ingreso
	 * @return
	 */
	public String obtenerCarnet(Connection con, int convenio, int ingreso)
	{
		return SqlBaseEstadoCuentaDao.obtenerCarnet(con, convenio, ingreso);
	}
	
	/**
	 * Metodo que Consulta la fecha de Egreso segun Estado Cuenta
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public HashMap<String, Object> fechaEgreso(Connection con, int ingreso)
	{
		return SqlBaseEstadoCuentaDao.fechaEgreso(con, ingreso);
	}
	

	/**
	 * Metodo que consulta los codios de las facturas asociadas a un cargo
	 * @param con
	 * @param subCuenta
	 * @param solicitud
	 * @return
	 */
	public String codigosFacturas(Connection con, int subCuenta, int solicitud)
	{
		return SqlBaseEstadoCuentaDao.codigosFacturas(con, subCuenta, solicitud,consultaCodigosFacturas);
	}

	/**
	 * 
	 */
	public String obtenerFechaValidacionEsquemas(Connection con,String idSubCuenta) 
	{
		return SqlBaseEstadoCuentaDao.obtenerFechaValidacionEsquemas(con,idSubCuenta);
	}
	
	/**
	 * Metodo para verificar si una consulta arroja resultados
	 * @param con
	 * @param consulta
	 * @return
	 */
	public boolean resultadosConsulta(Connection con, String consulta)
	{
		return SqlBaseEstadoCuentaDao.resultadosConsulta(con, consulta);
	}
	
	/**
	 * Metodo que consulta Tipo Monto y Valores Paciente
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public HashMap tipoMontoValoresPaciente(Connection con, int subCuenta)
	{
		return SqlBaseEstadoCuentaDao.tipoMontoValoresPaciente(con, subCuenta);
	}
	
	/**
	 * Método que carga el detalle de una solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> cargarDetalleServicioArticuloSolicitud(Connection con,HashMap campos)
	{
		return SqlBaseEstadoCuentaDao.cargarDetalleServicioArticuloSolicitud(con, campos);
	}
	
	/**
	 * 
	 * M&eacute;todo que carga el detalle de la solicitud en el pop up 
	 * @author Diana Ruiz
	 * @param con
	 * @param campos
	 * @return
	 * 
	 */
	public HashMap<String, Object> cargarDetalleServicioArticuloSolicitudPopUp(Connection con,HashMap campos)
	{
		return SqlBaseEstadoCuentaDao.cargarDetalleServicioArticuloSolicitudPopUp(con, campos);
	}
	
	/**
	 * M&eacute;todo para verificar si para la solicitud hay despacho de equivalentes 
	 * @author Diana Ruiz
	 * @param con
	 * @param campos
	 * @return
	 */
	
	public boolean despachoEquivalentes(Connection con,HashMap campos)
    {
        return SqlBaseEstadoCuentaDao.despachoEquivalentes(con, campos);
    }
	
}
