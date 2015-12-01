package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;


/**
 * @author Andrés Eugenio Silva Monsalve 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la parametrización de Reportes Procedimientos Esteticos
 */
public class SqlBaseReporteProcedimientosEsteticosDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseCondicionesXServiciosDao.class);
	
	/**
	 * Cadena que consulta el consecutivo de la Condicion de Servicios x servicio
	 */
	private static final String consultarProcedimientosEsteticosStr = 	
	
	"SELECT "+
	"to_char(sol.fecha_interpretacion,'"+ConstantesBD.formatoFechaAp+"') AS fecha_interpretacion, "+
	"getnombreservicio (solcise.servicio, "+ConstantesBD.codigoTarifarioCups+") AS nombre_servicio, "+ 
	"sol.consecutivo_ordenes_medicas AS consecutivo_orden, " +
	"cue.codigo_paciente AS codigo_paciente, "+
	"getnombrepersona (cue.codigo_paciente) AS nombre_paciente, "+ 
	"getidpaciente (cue.codigo_paciente) AS id_paciente, "+
	"getvalorordencx (sol.numero_solicitud) AS valor_orden, "+ 
	"sol.numero_solicitud  AS numero_solicitud, "+
	"getNumCxSol(sol.numero_solicitud) AS num_servicios "+
	"FROM serv_esteticos se "+
	"INNER JOIN sol_cirugia_por_servicio solcise ON(solcise.servicio=se.servicio) "+ 
	"INNER JOIN solicitudes_cirugia solci ON(solci.numero_solicitud=solcise.numero_solicitud) "+
	"INNER JOIN solicitudes sol ON(sol.numero_solicitud=solcise.numero_solicitud) "+ 
	"INNER JOIN cuentas cue ON(cue.id=sol.cuenta) "+ 
	"WHERE ";
	 
		
	/**
	 * Cadena que consulta el detalle de un cargo estetico
	 */
	private static final String consultarDetalleCargoEsteticoStr = "SELECT "+ 
		"dc.convenio AS codigo_convenio, "+
		"getnombreconvenio(dc.convenio) AS nombre_convenio, "+
		"getcodigoespecialidad(dc.servicio_cx) || '-' || dc.servicio_cx || ' ' || getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+") AS nombre_cirugia, "+
		"ta.nombre_asocio || ' (' || getnombretiposervicio(ta.tipos_servicio)|| ')' AS nombre_asocio, "+
		"sum(dc.valor_total_cargado) AS valor, "+
		"sc.consecutivo AS consecutivo "+ 
		"FROM det_cargos dc "+ 
		"INNER JOIN sol_cirugia_por_servicio sc ON(sc.numero_solicitud = dc.solicitud AND sc.servicio = dc.servicio_cx) "+ 
		"INNER JOIN tipos_asocio ta ON(ta.codigo=dc.tipo_asocio) "+ 
		"WHERE "+ 
		"dc.solicitud = ? AND dc.eliminado='"+ConstantesBD.acronimoNo+"' "+ 
		"GROUP BY codigo_convenio,nombre_convenio,nombre_cirugia,consecutivo,dc.tipo_asocio,ta.nombre_asocio,ta.tipos_servicio "+ 
		"ORDER BY nombre_convenio,consecutivo,dc.tipo_asocio"; 
	
	/**
	 * Cadena que consulta los materiales especiales de la solicitud
	 */
	private static final String consultarMaterialesEspecialesEsteticoStr = "SELECT "+ 
		"dc.convenio AS codigo_convenio, "+
		"getnombreconvenio(dc.convenio) AS nombre_convenio, "+
		"getdescarticulo(dc.articulo) AS nombre_articulo, "+
		"sum(coalesce(dc.cantidad_cargada,0)) AS cantidad, "+
		"sum(coalesce(dc.valor_total_cargado,0)) AS valor "+
		"FROM det_cargos dc "+ 
		"WHERE "+ 
		"dc.solicitud = ? AND dc.articulo is not null and dc.eliminado='"+ConstantesBD.acronimoNo+"' "+ 
		"GROUP BY codigo_convenio,nombre_convenio,dc.articulo "+ 
		"ORDER BY nombre_convenio,nombre_articulo";
	
	/**
	 * Método implementado para consultar el Procedimiento Estetico
	 * de la parametrizacion de hoja de gastos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarProcedimientosEsteticos(Connection con,HashMap campos)
	{
		try
		{
			String consulta = consultarProcedimientosEsteticosStr;
			
			consulta += " se.institucion = "+campos.get("institucion")+" ";
			
			if(!campos.get("gEstetico").toString().equals(""))
			{
				//Se filtra por grupo estético
				consulta += " AND se.grupos_esteticos = '"+campos.get("gEstetico")+"' ";
			}
			
			consulta += " AND (solci.fecha_inicial_cx BETWEEN ? AND ? OR solci.fecha_final_cx BETWEEN ? AND ?) ";
			
			if(campos.get("tipoRep").toString().equals(ConstantesIntegridadDominio.acronimoTipoReporteRealizado))
			{
//				Se anexa a la consulta si el tipo de reporte es realizada
				consulta += "AND (sol.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCInterpretada+" AND esSolCxCargada(sol.numero_solicitud) = '"+ConstantesBD.acronimoSi+"') ";
			}
			else if(campos.get("tipoRep").toString().equals(ConstantesIntegridadDominio.acronimoTipoReporteFacturado))
			{
//				Se anexa a la consulta si el tipo de Reporte es Facturada
				consulta +="AND esSolCxFacturada(sol.numero_solicitud) = '"+ConstantesBD.acronimoSi+"' ";
			}
			
			if(!campos.get("centroAte").toString().equals(""))
			{
//				Se anexa a la consulta si esta solo se hace por un Centro de Atencion
				consulta += "AND getCentroAtencionSol( solcise.numero_solicitud ) = "+campos.get("centroAte")+" ";
			}
			
			consulta += " ORDER BY sol.fecha_interpretacion,sol.consecutivo_ordenes_medicas ";
			
		 logger.info("CONSULTA FINAL====>  "+consulta+"\ngrupoEstetico: "+campos.get("gEstetico")+"\ninstitucion: "+campos.get("institucion")+"\nfechaIni: "+campos.get("fechaIni")+"\nfechaFin: "+campos.get("fechaFin"));
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,campos.get("fechaIni"));
			pst.setObject(2,campos.get("fechaFin"));
			pst.setObject(3,campos.get("fechaIni"));
			pst.setObject(4,campos.get("fechaFin"));
					
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,true);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error consultando los Procedimientos Esteticos :"+e);
			return null;
		}
	}
	
	
	/**
	 * Método que consulta el detalle del cargo de una solicitud estética
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> consultarDetalleCargoEstetico(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarDetalleCargoEsteticoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(campos.get("numeroSolicitud")+""));
			
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				
				elemento.put("codigoConvenio", rs.getObject("codigo_convenio"));
				elemento.put("nombreConvenio", rs.getObject("nombre_convenio"));
				elemento.put("nombreCirugia", rs.getObject("nombre_cirugia"));
				elemento.put("nombreAsocio", rs.getObject("nombre_asocio"));
				elemento.put("valor", UtilidadTexto.formatearValores(rs.getString("valor")));
				elemento.put("consecutivo", rs.getObject("consecutivo"));
				
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarDetalleCargoEstetico: "+e);
		}
		
		return resultados;
		
	}
	
	/**
	 * Método para cargar los materiales especiales de la solicitud de cirugía
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> consultarMaterialesEspecialesEstetico(Connection con,HashMap campos)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarMaterialesEspecialesEsteticoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1,Utilidades.convertirAEntero(campos.get("numeroSolicitud")+""));
			
			ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				HashMap<String, Object> elemento = new HashMap<String, Object>();
				
				elemento.put("codigoConvenio", rs.getObject("codigo_convenio"));
				elemento.put("nombreConvenio", rs.getObject("nombre_convenio"));
				elemento.put("nombreArticulo", rs.getObject("nombre_articulo"));
				elemento.put("cantidad", rs.getObject("cantidad"));
				elemento.put("valor", UtilidadTexto.formatearValores(rs.getString("valor")));
				elemento.put("valorDouble", rs.getString("valor"));
				
				
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarMaterialesEspecialesEstetico: "+e);
		}
		return resultados;
	}

}