/*
 * 11 Abril, 2008 
 */
package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;


/**
 * @author Sebastián Gómez R. 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para Registro Rips Cargos Directos
 */
public class SqlBaseRegistroRipsCargosDirectosDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseRegistroRipsCargosDirectosDao.class);
	
	/**
	 * Sección SELECt que consulta las cuentas  que tienen solicitudes de cargos directos
	 */
	private static final String listadoCuentas_SELECT_Str = "SELECT "+ 
		"i.consecutivo || CASE WHEN i.anio_consecutivo <> '' THEN ' - ' || i.anio_consecutivo ELSE '' END AS consecutivo_ingreso, "+
		"getintegridaddominio(i.estado) as estado_ingreso, "+
		"to_char(getfechaingreso(c.id,c.via_ingreso),'"+ConstantesBD.formatoFechaAp+"') AS fecha_ingreso, "+
		"substr(gethoraingreso(c.id,c.via_ingreso),0,6) AS hora_ingreso, "+
		"c.id as id_cuenta, "+ 
		"c.id_ingreso as id_ingreso, "+
		"to_char(c.fecha_apertura,'"+ConstantesBD.formatoFechaAp+"') as fecha_apertura, "+ 
		"substr(c.hora_apertura,0,6) AS hora_apertura, "+ 
		"getnombreestadocuenta(c.estado_cuenta) as estado_cuenta, "+
		"getnombreviaingreso(c.via_ingreso) || ' - ' || getnombretipopaciente(c.tipo_paciente) as via_ingreso, "+
		"getnombreconvenioxingreso(c.id_ingreso) as responsable, "+ 
		"getcentroatencioncc(c.area) AS centro_atencion, " +
		"getnombrepersona(c.codigo_paciente) as nombre_paciente," +
		"c.codigo_paciente as codigo_paciente "+ 
		"FROM ingresos i "+ 
		"INNER JOIN cuentas c ON(c.id_ingreso=i.id) ";
	
	/**
	 * Sección WHERE 01 que consulta las cuentas que tienen solicitudes de cargos directos
	 */
	private static final String listadoCuentas_WHERE_01_Str = "WHERE "+ 
		"c.codigo_paciente = ? and "+ 
		"tieneCuentaCargoDirecto(c.id) = '"+ConstantesBD.acronimoSi+"' "+ 
		"ORDER BY consecutivo_ingreso DESC";
	
	/**
	 * Cadena que consulta las solicitudes de cargos directos de una cuenta
	 */
	private static final String listadoSolicitudes_Str = "SELECT * FROM "+
		"( "+ 
		"SELECT "+ 
		"s.consecutivo_ordenes_medicas as orden, "+
		"s.numero_solicitud as numero_solicitud, "+
		"to_char(s.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"') AS fecha_solicitud, " +
		"s.fecha_solicitud as fecha_solicitud_bd, "+
		"substr(s.hora_solicitud,0,6) AS hora_solicitud, "+
		"getnombretiposervicio(gettiposervicio(cd.servicio_solicitado)) AS tipo_servicio, " +
		"gettiposervicio(cd.servicio_solicitado) as codigo_tipo_servicio, "+ 
		"'(' || getcodigoespecialidad(cd.servicio_solicitado) || '-' || cd.servicio_solicitado || ') ' || getnombreservicio(cd.servicio_solicitado,"+ConstantesBD.codigoTarifarioCups+") as nombre_servicio, "+
		"s.tipo as tipo_solicitud, " +
		"cd.servicio_solicitado as codigo_servicio "+
		"FROM solicitudes s "+ 
		"INNER JOIN cargos_directos cd on(cd.numero_solicitud=s.numero_solicitud) "+ 
		"WHERE "+ 
		"s.cuenta = ? AND " +
		"s.tipo = "+ConstantesBD.codigoTipoSolicitudCargosDirectosServicios+" AND " +
		"(essolicitudtotalpendiente(s.numero_solicitud) = '"+ConstantesBD.acronimoSi+"' OR essolicitudanuladainactiva(s.numero_solicitud) = '"+ConstantesBD.acronimoNo+"' ) " +
		"UNION "+ 
		"SELECT "+ 
		"s.consecutivo_ordenes_medicas as orden, "+
		"s.numero_solicitud as numero_solicitud, "+
		"to_char(s.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"') AS fecha_solicitud, "+
		"s.fecha_solicitud as fecha_solicitud_bd, "+
		"substr(s.hora_solicitud,0,6) AS hora_solicitud, "+
		"getnombretiposervicio(gettiposervicio(scs.servicio)) AS tipo_servicio, "+
		"gettiposervicio(scs.servicio) as codigo_tipo_servicio, "+
		"'(' || getcodigoespecialidad(scs.servicio) || '-' || scs.servicio || ') ' || getnombreservicio(scs.servicio,"+ConstantesBD.codigoTarifarioCups+") as nombre_servicio," +
		"s.tipo as tipo_solicitud, "+
		"scs.servicio as codigo_servicio "+
		"FROM solicitudes s "+ 
		"INNER JOIN solicitudes_cirugia sc ON(sc.numero_solicitud=s.numero_solicitud) "+ 
		"INNER JOIN sol_cirugia_por_servicio scs ON(scs.numero_solicitud=sc.numero_solicitud AND scs.consecutivo = 1) "+ 
		"WHERE "+ 
		"s.cuenta = ? AND " +
		"sc.ind_qx IN ('"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugiaCargoDirecto+"','"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruentoCargoDirecto+"') AND "+
		"(essolicitudtotalpendiente(s.numero_solicitud) = '"+ConstantesBD.acronimoSi+"' OR essolicitudanuladainactiva(s.numero_solicitud) = '"+ConstantesBD.acronimoNo+"' ) " +
		") t "+ 
		"ORDER BY t.orden asc";
	
	/**
	 * Método para consultar el listado de cuentas que tienen soolicitudes de cargos directos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> listadoCuentas(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			int codigoPaciente = Utilidades.convertirAEntero(campos.get("codigoPaciente").toString());
			String consulta = listadoCuentas_SELECT_Str;
			
			if(codigoPaciente>0)
				consulta += listadoCuentas_WHERE_01_Str;
			else
			{
				//**************************FLUJO POR PERIODO*********************************************************************
				int centroAtencion = Utilidades.convertirAEntero(campos.get("centroAtencion").toString());
				String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
				String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
				int estadoCuenta = Utilidades.convertirAEntero(campos.get("estadoCuenta").toString());
				int viaIngreso = Utilidades.convertirAEntero(campos.get("viaIngreso").toString());
				int convenio = Utilidades.convertirAEntero(campos.get("convenio").toString());
				
				consulta += "INNER JOIN centros_costo cc ON(cc.codigo=c.area) ";
				
				if(convenio>0)
				{
					consulta = consulta.replaceAll("SELECT", "SELECT DISTINCT");
					consulta += "INNER JOIN sub_cuentas sc ON(sc.ingreso=c.id_ingreso) ";
				}
				
				consulta += "WHERE cc.centro_atencion = "+centroAtencion+" AND (c.fecha_apertura BETWEEN '"+fechaInicial+"' AND '"+fechaFinal+"') ";
				
				if(estadoCuenta>=0)
					consulta += " AND c.estado_cuenta = "+estadoCuenta;
				if(viaIngreso>0)
					consulta += " AND c.via_ingreso = "+viaIngreso;
				if(convenio>0)
					consulta += " AND sc.convenio = "+convenio;
				
				
				consulta += " AND tieneCuentaCargoDirecto(c.id) = '"+ConstantesBD.acronimoSi+"' "+ 
							"ORDER BY consecutivo_ingreso DESC";
				//*****************FIN FLUJO POR PERIODO*****************************************************************
			}
			
			logger.info("LA CONSULTA ES=> "+consulta);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			if(codigoPaciente>0)
				pst.setInt(1,codigoPaciente);
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true); 
			
		}
		catch (SQLException e) 
		{
			logger.error("Error en listadoCuentas: "+e);
		}
		return resultados;
		
	}
	
	/**
	 * Método que consulta el listado de solicitudes de cargos directos de una cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> listadoSolicitudes(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(listadoSolicitudes_Str, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("idCuenta")+""));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("idCuenta")+""));
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
		}
		catch(SQLException e)
		{
			logger.error("Error en listadoSolicitudes: "+e);
		}
		return resultados;
	}
}
