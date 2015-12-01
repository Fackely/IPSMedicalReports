/*
 * Creado en 2/08/2004
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author Juan David Ramírez López
 *
 * Princeton S.A.
 */
public class SqlBaseActivacionCargosDao
{
    
    /**
     * Cadena con sentencia SQL paralistar las solicitudes en estado cargadas o inactivas
     */
    private static String listar="SELECT DISTINCT "+ 
    	"s.numero_solicitud AS numero_solicitud, "+
    	"s.consecutivo_ordenes_medicas AS orden, "+
    	"coalesce(gettieneportatilsolicitud(s.numero_solicitud,dc.servicio),"+ConstantesBD.codigoNuncaValido+") as codigo_portatil, "+
    	"CASE WHEN s.tipo = "+ConstantesBD.codigoTipoSolicitudCirugia+" THEN " +
  			"getintegridaddominio(getIndCargoSolicitudCx(s.numero_solicitud)) " +
  		"ELSE  " +
  			"getnomtiposolicitud(s.tipo) " +
    	"END AS nombre_tipo, "+
    	"s.tipo AS codigo_tipo, "+
    	"getnomcentrocosto(s.centro_costo_solicitante) AS centro_costo_solicitante, "+
    	"getnomcentrocosto(s.centro_costo_solicitado) AS centro_costo_solicitado, "+
    	"to_char(s.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"') AS fecha, "+
    	"getestadosolhis(s.estado_historia_clinica) AS estado_medico "+ 
    	"FROM det_cargos dc  "+ 
    	"INNER JOIN solicitudes s ON(s.numero_solicitud=dc.solicitud) "+ 
    	"WHERE "+ 
    	"dc.sub_cuenta = ? AND "+
    	" dc.eliminado='"+ConstantesBD.acronimoNo+"' AND "+
    	"dc.estado IN ("+ConstantesBD.codigoEstadoFCargada+","+ConstantesBD.codigoEstadoFInactiva+") AND "+ 
    	"dc.tipo_solicitud NOT IN ("+
    		ConstantesBD.codigoTipoSolicitudMedicamentos+","+
    		ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos+","+
    		ConstantesBD.codigoTipoSolicitudPaquetes+
    	") AND "+
    	"dc.facturado = '"+ConstantesBD.acronimoNo+"' AND dc.es_portatil = '"+ConstantesBD.acronimoNo+"' and "+ 
    	"dc.valor_total_cargado > 0 ";
    
    /**
     * Cadena que consulta el detalle de cargo de una solicitud servicio
     */
    private static final String detallarSolicitud_01_Str = "SELECT "+ 
    	"dc.codigo_detalle_cargo AS codigo, "+
    	"getcodigoespecialidad(dc.servicio) || '-'  || dc.servicio AS codigo_servicio, "+
    	
    	//Modificado por la Tarea 49498
    	"getcodigopropservicio2(dc.servicio, "+ConstantesBD.codigoTarifarioCups+") AS codigo_cups, "+
    	
    	"getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombre_servicio, " +
    	"getnombreesquematarifario(dc.esquema_tarifario) AS esquema_tarifario, "+
    	"dc.cantidad_cargada AS cantidad, "+
    	"dc.valor_unitario_cargado AS valor_unitario, "+
    	"dc.valor_total_cargado AS valor_total, "+
    	"dc.estado AS codigo_estado, " +
    	"coalesce(dc.tipo_distribucion,'') AS tipo_distribucion, "+
    	"CASE WHEN dc.estado = "+ConstantesBD.codigoEstadoFCargada+" THEN 'Activo' ELSE 'Inactivo' END AS nombre_estado, "+
    	"coalesce(dc.paquetizado,'"+ConstantesBD.acronimoNo+"') AS paquetizado," +
    	"CASE WHEN dc.paquetizado = '"+ConstantesBD.acronimoSi+"' THEN getConsecutivoSolCargo(dc.cargo_padre) ELSE '' END AS orden_paquete," +
    	"dc.cod_sol_subcuenta AS cod_sol_subcuenta," +
    	"dc.convenio AS codigo_convenio,  " +
    	"dc.servicio AS servicio," +
    	"'' AS codigo_asocio," +
    	"'' AS nombre_asocio "+ 
    	"FROM det_cargos dc "+ 
    	"WHERE "+ 
    	"dc.sub_cuenta = ? AND "+ 
    	"dc.solicitud = ? AND "+ 
    	"dc.eliminado='"+ConstantesBD.acronimoNo+"' and "+
    	"dc.estado IN ("+ConstantesBD.codigoEstadoFCargada+","+ConstantesBD.codigoEstadoFInactiva+") AND "+ 
    	"dc.facturado = '"+ConstantesBD.acronimoNo+"' AND "+ 
    	"dc.valor_total_cargado > 0 "; 
    
    /**
     * Cadena que consulta del detalle de cargo de un asocio
     */
    private static final String detallarSolicitud_02_Str = "SELECT "+ 
    	"dc.codigo_detalle_cargo AS codigo, "+ 
    	"getcodigoespecialidad(dc.servicio_cx) || '-'  || dc.servicio_cx AS codigo_servicio, "+ 
    	
    	//Modificado por la Tarea 49498
    	"getcodigopropservicio2(dc.servicio_cx, "+ConstantesBD.codigoTarifarioCups+") AS codigo_cups, "+
    	
    	"getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+") AS nombre_servicio, "+
    	"getnombreesquematarifario(dc.esquema_tarifario) AS esquema_tarifario, "+
    	"dc.cantidad_cargada AS cantidad, "+ 
    	"dc.valor_unitario_cargado AS valor_unitario, "+ 
    	"dc.valor_total_cargado AS valor_total, "+
    	"dc.estado AS codigo_estado, "+ 
    	"coalesce(dc.tipo_distribucion,'') AS tipo_distribucion, "+
    	"CASE WHEN dc.estado = "+ConstantesBD.codigoEstadoFCargada+" THEN 'Activo' ELSE 'Inactivo' END AS nombre_estado, "+
    	"coalesce(dc.paquetizado,'"+ConstantesBD.acronimoNo+"') AS paquetizado, "+
    	"CASE WHEN dc.paquetizado = '"+ConstantesBD.acronimoSi+"' THEN getConsecutivoSolCargo(dc.cargo_padre) ELSE '' END AS orden_paquete, "+
    	"dc.cod_sol_subcuenta AS cod_sol_subcuenta, "+
    	"dc.convenio AS codigo_convenio, "+  
    	"dc.servicio_cx AS servicio, "+
    	"dc.tipo_asocio AS codigo_asocio, "+
    	"ta.nombre_asocio || ' (' || getnombretiposervicio(ta.tipos_servicio) || ')' AS nombre_asocio "+ 
    	"FROM det_cargos dc "+ 
    	"INNER JOIN tipos_asocio ta ON(ta.codigo=dc.tipo_asocio) "+
    	"WHERE "+ 
    	"dc.sub_cuenta = ? AND "+
    	"dc.eliminado='"+ConstantesBD.acronimoNo+"' and "+
    	"dc.solicitud = ? AND "+ 
    	"dc.servicio_cx = ? AND "+ 
    	"dc.tipo_asocio = ? AND "+ 
    	"dc.estado IN ("+ConstantesBD.codigoEstadoFCargada+","+ConstantesBD.codigoEstadoFInactiva+") AND "+ 
    	"dc.facturado = '"+ConstantesBD.acronimoNo+"' AND "+ 
    	"dc.valor_total_cargado > 0 ";
    
    /**
     * Cadena que consulta el detalle de los cargos de una cirugia de una sub_cuenta específica
     */
    private static final String detalleSolicitudCirugiaStr = "SELECT DISTINCT "+
    	"sc.consecutivo AS consecutivo, "+
    	"sc.servicio AS codigo_cirugia, "+
    	"getcodigoespecialidad(sc.servicio) || '-'  || sc.servicio AS codigo_servicio, "+
    	
    	//Modificado por la Tarea 49498
    	"getcodigopropservicio2(dc.servicio, "+ConstantesBD.codigoTarifarioCups+") AS codigo_cups, "+
    	
    	"getnombreservicio(sc.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombre_servicio, "+
    	"dc.tipo_asocio AS codigo_asocio, "+
    	"ta.nombre_asocio || ' (' || getnombretiposervicio(ta.tipos_servicio) || ')' AS nombre_asocio, "+
    	"CASE WHEN ta.tipos_servicio = '"+ConstantesBD.codigoServicioHonorariosCirugia+"' OR ta.tipos_servicio = '"+ConstantesBD.codigoServicioProcedimiento+"' THEN coalesce(getnomprofesionalasocio(dc.det_cx_honorarios),'') ELSE '' END AS nombre_profesional, " +
    	"ta.tipos_servicio AS tipo_servicio_asocio, " +
    	"coalesce(dc.det_cx_honorarios,dc.det_asocio_cx_salas_mat) AS consecutivo_asocio "+ 
    	"FROM det_cargos dc "+ 
    	"INNER JOIN sol_cirugia_por_servicio sc ON(sc.numero_solicitud=dc.solicitud AND sc.servicio=dc.servicio_cx) "+ 
    	"INNER JOIN tipos_asocio ta ON(ta.codigo=dc.tipo_asocio) "+ 
    	"WHERE "+ 
    	"dc.solicitud = ? AND "+ 
    	"dc.sub_cuenta = ? AND "+
    	"dc.eliminado='"+ConstantesBD.acronimoNo+"' AND "+
    	"dc.estado IN ("+ConstantesBD.codigoEstadoFCargada+","+ConstantesBD.codigoEstadoFInactiva+") AND "+ 
    	"dc.facturado = '"+ConstantesBD.acronimoNo+"' AND "+ 
    	"dc.valor_total_cargado > 0 "+ 
    	"ORDER BY consecutivo,nombre_asocio";
    
    /**
     * Método que realiza la consulta de las inactivaciones
     */
    private static final String consultaInactivacionesCargosStr = "SELECT "+ 
    	"getconsecutivosolicitud(ic.numero_solicitud) AS orden, "+ 
    	"getcodigoespecialidad(ic.servicio) || '-'  || ic.servicio AS codigo_servicio, "+ 
    	"getnombreservicio(ic.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombre_servicio, "+ 
    	"CASE WHEN ic.tipo_asocio IS NULL THEN '' ELSE ta.nombre_asocio || ' (' || getnombretiposervicio(ta.tipos_servicio) || ') ' || coalesce(getnomprofesionalasocio(ic.det_cx_honorarios),'') END AS nombre_asocio, "+ 
    	"to_char(ic.fecha,'"+ConstantesBD.formatoFechaAp+"') As fecha, "+
    	"ic.hora AS hora, "+
    	"CASE WHEN ic.activacion = '"+ConstantesBD.acronimoNo+"' THEN 'Inactivo' ELSE 'Activo' END AS activacion, "+
    	"CASE WHEN ic.paquetizado = '"+ConstantesBD.acronimoNo+"' THEN 'No' ELSE 'Sí' END AS paquetizado, "+
    	"ic.motivo AS motivo," +
    	"ic.valor AS valor," +
    	"ic.cantidad AS cantidad," +
    	"ic.usuario AS usuario  "+ 
    	"FROM inactivaciones_cargos ic "+ 
    	"LEFT OUTER JOIN tipos_asocio ta ON(ta.codigo=ic.tipo_asocio) "+ 
    	"WHERE "+ 
    	"ic.ingreso = ? AND "+ 
    	"ic.convenio = ? "+ 
    	"ORDER BY orden,codigo_servicio,nombre_asocio,fecha,hora,paquetizado";
    
    
    
	
    /**
     * Listar las solicitudes cargadas o inactivas
     * @param cuenta Codigo de la cuenta del paciente
     * @return Collection con las solicitudes en estado cargado o pendiente
     */
    @SuppressWarnings("rawtypes")
	public static HashMap listar(Connection con, String idSubCuenta)
    {
    	PreparedStatementDecorator listado=null;
    	ResultSetDecorator rs=null;
    	HashMap mapaRetorno=null;
    	try
		{
			listado= new PreparedStatementDecorator(con.prepareStatement(listar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			listado.setLong(1, Utilidades.convertirALong(idSubCuenta));
			rs=new ResultSetDecorator(listado.executeQuery());
			mapaRetorno=UtilidadBD.cargarValueObject(rs, true, true);
		}
    	catch(Exception e){
			Log4JManager.error("ERROR listar", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(listado != null){
					listado.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return mapaRetorno;
    }

	/**
	 * Cargar el detalle de cargos Activo y/o Inactivo de una solicitud incluyendo los paquetizados
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap<String, Object> detallarSolicitud(Connection con, HashMap campos)
	{
		HashMap<String,Object> resultado = new HashMap<String,Object>();
		int cont0 = 0, cont1 = 0;
		String consulta = "";
		boolean conMotivo = UtilidadTexto.getBoolean(campos.get("conMotivo").toString());
		PreparedStatementDecorator pst = null;
		PreparedStatementDecorator pst2 = null;
		ResultSetDecorator rs = null;
		ResultSetDecorator rs2 = null;
		try
		{
			int tipoSolicitud = Integer.parseInt(campos.get("codigoTipoSolicitud").toString()); 
			String tipoServicioAsocio = campos.get("tipoServicioAsocio").toString();
			String consecutivoAsocio = campos.get("consecutivoAsocio").toString();
			String esPortatil = campos.get("esPortatil").toString();
				
			//1) Se consulta el par de cargos que no están paquetizados------------------------------------------------------------
			//dependiendo del tipo de solicitud se edita la consulta
			if(tipoSolicitud==ConstantesBD.codigoTipoSolicitudCirugia)
			{
				consulta = detallarSolicitud_02_Str;
				
				//Según el tipo de servicio del asocio se evalua el filtro
				if(tipoServicioAsocio.equals(ConstantesBD.codigoServicioHonorariosCirugia+"")||tipoServicioAsocio.equals(ConstantesBD.codigoServicioProcedimiento+""))
					consulta += " AND dc.det_cx_honorarios = " + consecutivoAsocio;
				else
					consulta += " AND dc.det_asocio_cx_salas_mat = " + consecutivoAsocio;
			}
			else
				consulta = detallarSolicitud_01_Str;
			consulta += "  AND dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and dc.es_portatil = '"+esPortatil+"' ORDER BY codigo_estado";
			
			boolean cargoSinPaquete = false;
			
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setLong(1,Utilidades.convertirALong(campos.get("idSubCuenta")+""));
			pst.setObject(2,campos.get("numeroSolicitud"));
			
			if(tipoSolicitud==ConstantesBD.codigoTipoSolicitudCirugia)
			{
				pst.setObject(3,campos.get("codigoServicioCx"));
				pst.setObject(4,campos.get("tipoAsocio"));
			}
			rs = new ResultSetDecorator(pst.executeQuery());
			
			cont1 = 0;
			while(rs.next())
			{
				cargoSinPaquete = true;
				resultado.put("codigo_"+cont0+"_"+cont1, rs.getObject("codigo"));
				resultado.put("codigoServicio_"+cont0+"_"+cont1, rs.getObject("codigo_servicio"));
				resultado.put("codigoCups_"+cont0+"_"+cont1, rs.getObject("codigo_cups"));
				resultado.put("nombreServicio_"+cont0+"_"+cont1, rs.getObject("nombre_servicio"));
				resultado.put("esquemaTarifario_"+cont0+"_"+cont1, rs.getObject("esquema_tarifario"));
				resultado.put("cantidad_"+cont0+"_"+cont1, rs.getObject("cantidad"));
				resultado.put("valorTotal_"+cont0+"_"+cont1, rs.getObject("valor_total"));
				resultado.put("codigoEstado_"+cont0+"_"+cont1, rs.getObject("codigo_estado"));
				resultado.put("nombreEstado_"+cont0+"_"+cont1, rs.getObject("nombre_estado"));
				resultado.put("valorUnitario_"+cont0+"_"+cont1, rs.getObject("valor_unitario"));
				resultado.put("tipoDistribucion_"+cont0+"_"+cont1, rs.getObject("tipo_distribucion"));
				resultado.put("paquetizado_"+cont0+"_"+cont1, rs.getObject("paquetizado"));
				resultado.put("ordenPaquete_"+cont0+"_"+cont1, rs.getObject("orden_paquete"));
				resultado.put("codSolSubcuenta_"+cont0+"_"+cont1, rs.getObject("cod_sol_subcuenta"));
				resultado.put("codigoConvenio_"+cont0+"_"+cont1, rs.getObject("codigo_convenio"));
				resultado.put("servicio_"+cont0+"_"+cont1, rs.getObject("servicio"));
				resultado.put("codigoAsocio_"+cont0+"_"+cont1, rs.getObject("codigo_asocio")); // solo para cirugias
				resultado.put("nombreAsocio_"+cont0+"_"+cont1, rs.getObject("nombre_asocio")); // solo para cirugias
				//Se consulta el motivo de inactivacion si se requiere
				if(conMotivo)
					resultado.put("motivo_"+cont0+"_"+cont1, 
						consultarUltimoMotivoInactivacion(
							con,
							campos.get("numeroSolicitud").toString(),
							Integer.parseInt(campos.get("codigoTipoSolicitud").toString()),
							rs.getString("servicio"),
							rs.getString("codigo_convenio"),
							rs.getString("codigo_asocio"),
							rs.getInt("codigo_estado"),
							rs.getString("paquetizado"),
							tipoServicioAsocio,
							consecutivoAsocio
						)
					);
				cont1++;
			}
			
			
			//-------------------------------------------------------------------------------------------------------------------
			//2) Se consulta el par de cargos que se encuentren paquetizados agrupados por paquete--------------------------------
			if(tipoSolicitud==ConstantesBD.codigoTipoSolicitudCirugia)
			{
				consulta = detallarSolicitud_02_Str;
				
				//Según el tipo de servicio del asocio se evalua el filtro
				if(tipoServicioAsocio.equals(ConstantesBD.codigoServicioHonorariosCirugia+"")||tipoServicioAsocio.equals(ConstantesBD.codigoServicioProcedimiento+""))
					consulta += " AND dc.det_cx_honorarios = " + consecutivoAsocio;
				else
					consulta += " AND dc.det_asocio_cx_salas_mat = " + consecutivoAsocio;
			}
			else
				consulta = detallarSolicitud_01_Str;
			consulta += "  AND dc.paquetizado = '"+ConstantesBD.acronimoSi+"' and dc.es_portatil = '"+esPortatil+"' ORDER BY orden_paquete,codigo_estado";
			
			String ordenPaquete = "";
			
			pst2 =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst2.setObject(1,campos.get("idSubCuenta"));
			pst2.setObject(2,campos.get("numeroSolicitud"));
			
			if(tipoSolicitud==ConstantesBD.codigoTipoSolicitudCirugia)
			{
				pst2.setObject(3,campos.get("codigoServicioCx"));
				pst2.setObject(4,campos.get("tipoAsocio"));
			}
			
			rs2 = new ResultSetDecorator(pst2.executeQuery());
			
			while(rs2.next())
			{
				//Si es diferente se inicia un nuevo registro
				if(!ordenPaquete.equals(rs2.getString("orden_paquete")))
				{
					ordenPaquete = rs2.getString("orden_paquete");
					resultado.put("numRegistros_"+cont0,cont1+"");
					if(cargoSinPaquete)
						cont0++;
					else
						cargoSinPaquete = true;
					cont1 = 0;
				}
				resultado.put("codigo_"+cont0+"_"+cont1, rs2.getObject("codigo"));
				resultado.put("codigoServicio_"+cont0+"_"+cont1, rs2.getObject("codigo_servicio"));
				//Modificado por la Tarea 
				resultado.put("codigoCups_"+cont0+"_"+cont1, rs2.getObject("codigo_cups"));
				resultado.put("nombreServicio_"+cont0+"_"+cont1, rs2.getObject("nombre_servicio"));
				resultado.put("esquemaTarifario_"+cont0+"_"+cont1, rs2.getObject("esquema_tarifario"));
				resultado.put("cantidad_"+cont0+"_"+cont1, rs2.getObject("cantidad"));
				resultado.put("valorTotal_"+cont0+"_"+cont1, rs2.getObject("valor_total"));
				resultado.put("codigoEstado_"+cont0+"_"+cont1, rs2.getObject("codigo_estado"));
				resultado.put("tipoDistribucion_"+cont0+"_"+cont1, rs2.getObject("tipo_distribucion"));
				resultado.put("nombreEstado_"+cont0+"_"+cont1, rs2.getObject("nombre_estado"));
				resultado.put("valorUnitario_"+cont0+"_"+cont1, rs2.getObject("valor_unitario"));
				resultado.put("paquetizado_"+cont0+"_"+cont1, rs2.getObject("paquetizado"));
				resultado.put("ordenPaquete_"+cont0+"_"+cont1, rs2.getObject("orden_paquete"));
				resultado.put("codSolSubcuenta_"+cont0+"_"+cont1, rs2.getObject("cod_sol_subcuenta"));
				resultado.put("codigoConvenio_"+cont0+"_"+cont1, rs2.getObject("codigo_convenio"));
				resultado.put("servicio_"+cont0+"_"+cont1, rs2.getObject("servicio"));
				resultado.put("codigoAsocio_"+cont0+"_"+cont1, rs2.getObject("codigo_asocio")); // solo para cirugias
				resultado.put("nombreAsocio_"+cont0+"_"+cont1, rs2.getObject("nombre_asocio")); // solo para cirugias
				//Se consulta el motivo de inactivacion si se requiere
				if(conMotivo)
					resultado.put("motivo_"+cont0+"_"+cont1, 
						consultarUltimoMotivoInactivacion(
							con,
							campos.get("numeroSolicitud").toString(),
							Integer.parseInt(campos.get("codigoTipoSolicitud").toString()),
							rs2.getString("servicio"),
							rs2.getString("codigo_convenio"),
							rs2.getString("codigo_asocio"),
							rs2.getInt("codigo_estado"),
							rs2.getString("paquetizado"),
							tipoServicioAsocio,
							consecutivoAsocio
						)
					);
				cont1++;
			}
			
			resultado.put("numRegistros_"+cont0,cont1+"");
			cont0++;
			resultado.put("numRegistros",cont0+"");
			//---------------------------------------------------------------------------------------------------------------------
			
			return resultado;
		}
		catch(Exception e){
			Log4JManager.error("ERROR detallarSolicitud", e);
		}
		finally{
			try{
				if(rs2 != null){
					rs2.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return null;
	}
	
	/**
	 * Método que consulta el motivo de la inactivacion de un cargo
	 * @param con
	 * @param solicitud
	 * @param tipo
	 * @param servicio
	 * @param convenio
	 * @param asocio
	 * @param estado
	 * @param paquetizado
	 * @return
	 */
	private static String consultarUltimoMotivoInactivacion(Connection con, String solicitud, int tipo, String servicio, String convenio, String asocio, int estado,String paquetizado,String tipoServicioAsocio,String consecutivoAsocio) 
	{
		String motivo = "",activacion = "";
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
	
		try
		{
			String consulta = "SELECT motivo,activacion FROM inactivaciones_cargos " +
				"WHERE " +
				"numero_solicitud = "+solicitud+" AND " +
				"servicio = "+servicio+" AND "+
				"convenio = "+convenio+" AND " +
				"paquetizado = '"+paquetizado+"' ";
			
			
			
			if(tipo==ConstantesBD.codigoTipoSolicitudCirugia)
			{
				consulta += " AND tipo_asocio = "+asocio;
				
				if(tipoServicioAsocio.equals(ConstantesBD.codigoServicioHonorariosCirugia+"")||tipoServicioAsocio.equals(ConstantesBD.codigoServicioProcedimiento+""))
					consulta += " AND det_cx_honorarios = "+consecutivoAsocio;
				else
					consulta += " AND det_asocio_cx_salas_mat = "+consecutivoAsocio;
			}
			
			
			consulta += " ORDER BY codigo DESC";
			
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				motivo = rs.getString("motivo");
				activacion = rs.getString("activacion");
			}
			
			//El estado del log de inactivacion debe concordar con el estado del cargo, si no concuerda no es el motivo adecuado
			if((!UtilidadTexto.getBoolean(activacion)&&estado==ConstantesBD.codigoEstadoFCargada)
				||
				(UtilidadTexto.getBoolean(activacion)&&estado==ConstantesBD.codigoEstadoFInactiva)
				)
				motivo = "";
		}
		catch(Exception e){
			Log4JManager.error("ERROR consultarUltimoMotivoInactivacion", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return motivo;
			
	}

	/**
	 * Método implementado para realizar la acitvacion/inactivacion del cargo
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static ResultadoBoolean activarInactivarCargo(Connection con,HashMap campos)
	{
		ResultadoBoolean resp = new ResultadoBoolean(false,"Error al realizar el proceso de activación/inactivación");
		PreparedStatementDecorator pst1 = null;
		PreparedStatementDecorator pst2 = null;
		PreparedStatementDecorator pst3 = null;
		PreparedStatementDecorator pst4 = null;
		PreparedStatementDecorator pst5 = null;
		PreparedStatementDecorator pst6 = null;
		ResultSetDecorator rs = null;
		try
		{
			String consulta = "";
			int resp0 = 0, resp1 = 0, resp2 = 0, resp3 = 0, resp4 = 0;
			//*************SE TOMAN LOS CAMPOS*********************************
			String codigoCargo = campos.get("codigoCargo").toString();
			String codSolSubcuenta = campos.get("codSolSubcuenta").toString();
			int codigoEstadoCargo = Integer.parseInt(campos.get("codigoEstadoCargo").toString());
			int cantidadActual = Integer.parseInt(campos.get("cantidadActual").toString());
			int cantidadNueva = Integer.parseInt(campos.get("cantidadNueva").toString());
			int cantidadRestante = cantidadActual - cantidadNueva;
			double valorUnitario = Double.parseDouble(campos.get("valorUnitario").toString());
			String loginUsuario = campos.get("loginUsuario").toString();
			String numeroSolicitud = campos.get("numeroSolicitud").toString();
			String idIngreso = campos.get("idIngreso").toString();
			String codigoConvenio = campos.get("codigoConvenio").toString();
			String motivo = campos.get("motivo").toString();
			String codigoServicio = campos.get("codigoServicio").toString();
			String tipoAsocio = campos.get("tipoAsocio").toString();
			String paquetizado = campos.get("paquetizado").toString();
			int tipoSolicitud = Integer.parseInt(campos.get("tipoSolicitud").toString());
			String tipoServicioAsocio = campos.get("tipoServicioAsocio").toString();
			String consecutivoAsocio = campos.get("consecutivoAsocio").toString();
			
			//Secuencias
			String secuenciaSolicitudesSubCuenta = campos.get("secuenciaSolicitudesSubCuenta").toString();
			String secuenciaDetCargos = campos.get("secuenciaDetCargos").toString();
			String secuenciaInactivacionesCargos = campos.get("secuenciaInactivacionesCargos").toString();
				
			//por si existe otro cargo
			boolean existeOtroCargo = UtilidadTexto.getBoolean(campos.get("existeOtroCargo").toString());
			String codigoOtroCargo = campos.get("codigoOtroCargo").toString();
			String codSolSubcuentaOtro = campos.get("codSolSubcuentaOtro").toString();
			int cantidadOtroCargo = Integer.parseInt(campos.get("cantidadOtroCargo").toString());
			int codigoEstadoOtroCargo = 0;
			//*****************************************************************
			
			//*************TRANSACCIONES PARA GENERACION/MODIFICACION DEL OTRO CARGO************************
			//*********SE verifica cual debe ser el estado del otro cargo***********************
			if(codigoEstadoCargo==ConstantesBD.codigoEstadoFCargada)
				codigoEstadoOtroCargo = ConstantesBD.codigoEstadoFInactiva;
			else
				codigoEstadoOtroCargo = ConstantesBD.codigoEstadoFCargada;
			
			//Si ya existe el cargo simplemente se realiza actualizacion
			if(existeOtroCargo)
			{
				//Se actualiza el detalle del cargo
				consulta = "UPDATE det_cargos SET cantidad_cargada = ?, valor_total_cargado = ? WHERE codigo_detalle_cargo = ?";
				pst1 =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst1.setInt(1,(cantidadOtroCargo+cantidadNueva));
				pst1.setDouble(2, ((cantidadOtroCargo+cantidadNueva)*valorUnitario));
				pst1.setObject(3,codigoOtroCargo);
				resp0 = pst1.executeUpdate();
				
				//se actualiza el registro de solicitudes subcuenta
				consulta = "UPDATE solicitudes_subcuenta SET cantidad = ? WHERE codigo = ?";
				pst2 =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst2.setInt(1,(cantidadOtroCargo+cantidadNueva));
				pst2.setObject(2,codSolSubcuentaOtro);
				resp1 = pst2.executeUpdate();
				
			}
			//Si no existe el cargo se realiza un insert en det_cargo y solicitudes_sub_cuenta
			else
			{
				//Se inserta un nuevo registro de solicitudes subcuenta
				consulta="INSERT INTO solicitudes_subcuenta " +
					" (codigo,solicitud,sub_cuenta,servicio,articulo,porcentaje,cantidad,monto,cubierto,cuenta,tipo_solicitud,paquetizada,sol_subcuenta_padre,servicio_cx,tipo_asocio,tipo_distribucion,fecha_modifica,hora_modifica,usuario_modifica,det_cx_honorarios,det_asocio_cx_salas_mat) " +
					" (select " +
						secuenciaSolicitudesSubCuenta+"," +
						" solicitud," +
						" sub_cuenta," +
						" servicio," +
						" articulo," +
						" porcentaje," +
						cantidadNueva+"," +
						" monto," +
						" cubierto," +
						" cuenta," +
						" tipo_solicitud," +
						" paquetizada," +
						" sol_subcuenta_padre," +
						" servicio_cx," +
						" tipo_asocio," +
						" tipo_distribucion," +
						" current_date," +
						" "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
						"'"+loginUsuario+"', " +
						"det_cx_honorarios, " +
						"det_asocio_cx_salas_mat " +
					" from solicitudes_subcuenta " +
					" where codigo= "+codSolSubcuenta+" "+ //se basa del registro solicitudes_subcuenta del cargo principal
					")";
				pst1 =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				resp0 = pst1.executeUpdate();
				
				
				//Se consulta la secuencia recien insertada
				consulta = "SELECT max(codigo) AS codigo FROM solicitudes_subcuenta WHERE solicitud = "+numeroSolicitud+" AND eliminado='"+ConstantesBD.acronimoNo+"' ";
				pst2 =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				rs = new ResultSetDecorator(pst2.executeQuery());
				if(rs.next())
					codSolSubcuentaOtro = rs.getString("codigo");
				
				//Se inserta un nuevo registro en det_cargos
				consulta=" INSERT INTO det_cargos " +
					" (codigo_detalle_cargo,sub_cuenta,convenio,esquema_tarifario,cantidad_cargada,valor_unitario_tarifa,valor_unitario_cargado,valor_total_cargado,porcentaje_cargado,porcentaje_recargo,valor_unitario_recargo,porcentaje_dcto,valor_unitario_dcto,valor_unitario_iva,nro_autorizacion,estado,cubierto,tipo_distribucion,solicitud,servicio,articulo,servicio_cx,tipo_asocio,facturado,tipo_solicitud,paquetizado,cargo_padre,usuario_modifica,fecha_modifica,hora_modifica,cod_sol_subcuenta,observaciones,requiere_autorizacion,contrato,det_cx_honorarios,det_asocio_cx_salas_mat,es_portatil) " +
					" (select " +
						secuenciaDetCargos+"," +
						" sub_cuenta," +
						" convenio," +
						" esquema_tarifario," +
						cantidadNueva+"," +
						" valor_unitario_tarifa," +
						" valor_unitario_cargado," +
						cantidadNueva+"*valor_unitario_cargado," +
						" porcentaje_cargado," +
						" porcentaje_recargo," +
						" valor_unitario_recargo," +
						" porcentaje_dcto," +
						" valor_unitario_dcto, "+
						" valor_unitario_iva," +
						" nro_autorizacion," +
						codigoEstadoOtroCargo+" ," +
						" cubierto," +
						" tipo_distribucion," +
						" solicitud," +
						" servicio," +
						" articulo," +
						" servicio_cx," +
						" tipo_asocio," +
						" facturado," +
						" tipo_solicitud," +
						" paquetizado," +
						" cargo_padre," +
						"'"+loginUsuario+"'," +
						" current_date," +
						" "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
						codSolSubcuentaOtro+"," +
						" observaciones," +
						" requiere_autorizacion," +
						" contrato, " +
						" det_cx_honorarios, " +
						" det_asocio_cx_salas_mat, " +
						" es_portatil " +
					" from det_cargos " +
					" where codigo_detalle_cargo = "+codigoCargo+" "+ //se basa del registro de det_cargos del cargo principal
					" )";
				pst3 =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				resp1 = pst3.executeUpdate();
			}
			
			//******************************************************************************************
			
			
			//*************TRANSACCIONES PARA MODIFICACION/ELIMINACION DEL CARGO PRINCIPAL*****************
			if(resp0>0&&resp1>0)
			{
				//Si queda cantidad restante en el cargo se realiza actualización sobre el cargo
				if(cantidadRestante>0)
				{
					//Se actualiza el detalle del cargo principal
					consulta = "UPDATE det_cargos SET cantidad_cargada = ?, valor_total_cargado = ? WHERE codigo_detalle_cargo = ?";
					pst4 =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst4.setInt(1,cantidadRestante);
					pst4.setDouble(2, (cantidadRestante*valorUnitario));
					pst4.setObject(3,codigoCargo);
					resp2 = pst4.executeUpdate();
					
					//se actualiza el registro de solicitudes subcuenta del cargo principal
					consulta = "UPDATE solicitudes_subcuenta SET cantidad = ? WHERE codigo = ?";
					pst5 =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst5.setInt(1,cantidadRestante);
					pst5.setObject(2,codSolSubcuenta);
					resp3 = pst5.executeUpdate();
					
				}
				//De lo contrario se realiza eliminacion
				else
				{
					//Se elimina el detalle del cargo principal
					consulta = "DELETE FROM det_cargos WHERE codigo_detalle_cargo = ?";
					pst4 =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst4.setObject(1,codigoCargo);
					resp2 = pst4.executeUpdate();
					
					//se elimina el registro de solicitudes subcuenta del cargo principal
					consulta = "DELETE FROM solicitudes_subcuenta WHERE codigo = ?";
					pst5 =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst5.setObject(1,codSolSubcuenta);
					resp3 = pst5.executeUpdate();
				}
			}
			//******************************************************************************************
			
			//***********INSERCION DE LA INACTIVACION DE CARGO****************************************
			consulta = "INSERT INTO inactivaciones_cargos (codigo,numero_solicitud,tipo_solicitud,servicio,convenio,ingreso,tipo_asocio,fecha,hora,usuario,activacion,valor,cantidad,motivo,paquetizado,det_cx_honorarios,det_asocio_cx_salas_mat) " +
				" VALUES ("+secuenciaInactivacionesCargos+",?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?,?,?,?,?,?)";
			pst6 =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst6.setObject(1,numeroSolicitud);
			pst6.setInt(2,tipoSolicitud);
			pst6.setObject(3,codigoServicio);
			pst6.setObject(4,codigoConvenio);
			pst6.setObject(5,idIngreso);
			if(!tipoAsocio.equals(""))
				pst6.setObject(6,tipoAsocio);
			else
				pst6.setNull(6, Types.INTEGER);
			pst6.setObject(7,loginUsuario);
			pst6.setString(8,codigoEstadoOtroCargo==ConstantesBD.codigoEstadoFInactiva?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi);
			pst6.setObject(9,(cantidadNueva*valorUnitario));
			pst6.setInt(10,cantidadNueva);
			pst6.setObject(11,motivo);
			pst6.setString(12,paquetizado);
			
			if(tipoSolicitud==ConstantesBD.codigoTipoSolicitudCirugia&&(tipoServicioAsocio.equals(ConstantesBD.codigoServicioHonorariosCirugia+"")||tipoServicioAsocio.equals(ConstantesBD.codigoServicioProcedimiento+"")))
				pst6.setInt(13,Integer.parseInt(consecutivoAsocio));
			else
				pst6.setNull(13,Types.INTEGER);
			
			if(tipoSolicitud==ConstantesBD.codigoTipoSolicitudCirugia&&(tipoServicioAsocio.equals(ConstantesBD.codigoServicioMaterialesCirugia+"")||tipoServicioAsocio.equals(ConstantesBD.codigoServicioSalaCirugia+"")))
				pst6.setInt(14,Integer.parseInt(consecutivoAsocio));
			else
				pst6.setNull(14,Types.INTEGER);
			
			resp4 = pst6.executeUpdate();
			//****************************************************************************************
			
			//************VERIFICACION FINAL*******************************************************
			if(resp0>0&&resp1>0&&resp2>0&&resp3>0&&resp4>0)
				resp = new ResultadoBoolean(true,"");
			else
				resp = new ResultadoBoolean(false,"Error en bases de datos al realizar la inactivacion/activacion");
			//**************************************************************************************
		}
		catch(Exception e){
			Log4JManager.error("ERROR activarInactivarCargo", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst6 != null){
					pst6.close();
				}
				if(pst5 != null){
					pst5.close();
				}
				if(pst4 != null){
					pst4.close();
				}
				if(pst3 != null){
					pst3.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(pst1 != null){
					pst1.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resp;
	}
	
	/**
	 * Método que realiza la consulta de los cargos de una solicitud cirugia para una subcuenta específica
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap<String,Object> consultarDetalleCargosCirugia(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(detalleSolicitudCirugiaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setObject(1,campos.get("numeroSolicitud"));
			pst.setLong(2, Utilidades.convertirALong(campos.get("idSubCuenta")+""));
			rs=new ResultSetDecorator(pst.executeQuery());
			resultados = UtilidadBD.cargarValueObject(rs, true, true);
		}
		catch(Exception e){
			Log4JManager.error("ERROR consultarDetalleCargosCirugia", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resultados;
	}
	
	
	/**
	 * Método que realiza la consulta de las inactivaciones de cargos
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap<String, Object> consultarInactivacionesCargos(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultaInactivacionesCargosStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setObject(1,campos.get("idIngreso"));
			pst.setObject(2,campos.get("codigoConvenio"));
			rs=new ResultSetDecorator(pst.executeQuery());
			resultados = UtilidadBD.cargarValueObject(rs, true, true);
		}
		catch(Exception e){
			Log4JManager.error("ERROR consultarInactivacionesCargos", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resultados;
	}
	
}
