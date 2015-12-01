package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacionEntSubContratada;
import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;
import com.princetonsa.mundo.manejoPaciente.AutorizacionesEntidadesSubcontratadas;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseProrrogarAnularAutorizacionesEntSubcontratadasDao {

	private static Logger logger = Logger.getLogger(SqlBaseProrrogarAnularAutorizacionesEntSubcontratadasDao.class);
	
	/**
	 * Cadena Sql para realizar la consulta de las autorizaciones de las entidades Subcontratadas
	 */
	private static String consultaAutorizacionesStr="SELECT " +
							"autoriz.consecutivo AS codigo, " +
							"to_char(autoriz.fecha_autorizacion, 'YYYY-MM-DD') AS fechaautorizacion, " +
							"autoriz.hora_autorizacion AS horaautorizacion, " +
							"autoriz.entidad_autorizada_sub AS codentidadautorizada, " +
							"getdescentitadsubcontratada(autoriz.entidad_autorizada_sub) AS nomentidadautorizada, " +
							
							// PermitirAutorizarDiferenteDeSolicitudes
							"aess.numero_solicitud AS numerosolicitud, " + 
							"getConsecutivoSolicitud(aess.numero_solicitud) AS consecutivosolicitud, " +
							
							"autoriz.convenio AS codconvenio, " +
							"getNombreConvenio(autoriz.convenio) AS nomconvenio, " +
							"to_char(autoriz.fecha_vencimiento, 'YYYY-MM-DD') AS fechavencimiento, " +
							"sol.tipo AS tiposolicitud,"+ 
							/*-SERVICIOS-----------------------*/							
							"autoservi.servicio AS codservicio,"+
							"getnombreservicio(autoservi.servicio, " +ConstantesBD.separadorSplit+" ) AS nomservicio, " +//
							"autoservi.cantidad AS cantservicio,"+							
							/*-ARTICULOS-----------------------*/							
							"autoarticu.articulo AS codarticulo, "+
							"sol.consecutivo_ordenes_medicas AS consOrdenMedica, "+
							"arti.descripcion AS descriparticulo, "+
							"unidad.nombre AS unidadarticulo, "+
							"autoarticu.nro_dosis_total AS dosistotal, "+//"autoarticu.cantidad AS cantarticulo, "+	
							"solmedi.observaciones_generales AS obsarticulo,"+							
							"autoarticu.dosis AS dosismedicamento, "+
							"autoarticu.frecuencia AS frecumedicamento, "+
							"autoarticu.tipo_frecuencia AS tipofrecuemedicamento, "+
							"autoarticu.dias_tratamiento AS diasmedicamento, "+
							"autoarticu.via AS viamedicamento, "+
							"natura.acronimo AS esmedicamento, "+
							/*---------------------------------*/
							"autoriz.observaciones AS observaciones, " +
							"autoriz.tipo AS tipo, " +
							"autoriz.estado AS estado, " +
							"autoriz.institucion AS codinstitucion, " +
							"getNombreInstitucion(autoriz.institucion) AS nominstitucion, "+
							"to_char(autoriz.fecha_modificacion, 'YYYY-MM-DD') AS fechamodifcacion ," +
							"autoriz.hora_modificacion AS horamodificacion, " +
							"autoriz.usuario_modificacion AS usuariomodificacion, " +
							"autoriz.codigo_paciente AS codpaciente," +
							"getNombrePersona(autoriz.codigo_paciente) AS nompaciente, " +
							"getTipoId(autoriz.codigo_paciente) AS tipoidpaciente, " +
							"getIdentificacionPaciente(autoriz.codigo_paciente) AS identificacionpaciente, " +
							"to_char(autoriz.fecha_anulacion, 'YYYY-MM-DD') AS fechaanulacion, " +
							"autoriz.hora_anulacion AS horaanulacion, " +
							"autoriz.usuario_anulacion AS usuarioanulacion,  " +
							"autoriz.motivo_anulacion AS motivoanulacion, " +
							"autoriz.consecutivo_autorizacion AS consecutivoautorizacion, " +
							"autoriz.anio_consecutivo AS anioconsecutivo, " +
							"con.tipo_contrato AS codtipocontrato, " +
							"getNombreTipoContrato(con.tipo_contrato) AS nomtipocontrato " +
							
							"FROM autorizaciones_entidades_sub autoriz " +
							"INNER JOIN convenios con ON (con.codigo = autoriz.convenio) " +
							// PermitirAutorizarDiferenteDeSolicitudes
							"INNER JOIN auto_entsub_solicitudes aess ON (aess.autorizacion_ent_sub = autoriz.consecutivo ) " + 
							"INNER JOIN solicitudes sol ON (sol.numero_solicitud = aess.numero_solicitud ) " + 
							
							"LEFT JOIN manejopaciente.autorizaciones_ent_sub_servi autoservi ON (autoservi.autorizacion_ent_sub = autoriz.consecutivo) "+
							"LEFT JOIN manejopaciente.autorizaciones_ent_sub_articu autoarticu ON (autoarticu.autorizacion_ent_sub = autoriz.consecutivo) "+							
							"LEFT JOIN inventarios.articulo arti ON (arti.codigo = autoarticu.articulo) "+
							"LEFT JOIN ordenes.solicitudes_medicamentos solmedi ON (solmedi.numero_solicitud=sol.numero_solicitud) "+ 
							"LEFT JOIN inventarios.naturaleza_articulo natura ON (natura.acronimo=arti.naturaleza)" +																	
							"LEFT JOIN inventarios.unidad_medida unidad ON (unidad.acronimo=arti.unidad_medida) "+
							
							"WHERE 1=1  "+
							"AND autoriz.estado = '"+ConstantesIntegridadDominio.acronimoAutorizado+"' "+
							"AND autoriz.codigo_paciente = ?  "+
							"AND sol.estado_historia_clinica  IN ("+ConstantesBD.codigoEstadoHCSolicitada+", "+ConstantesBD.codigoEstadoHCTomaDeMuestra+ ") "+
							//MT6166/Sanbarga/ver 1.1.6 se agrega condición del campo con.capitacion_subcontratada
							"AND ( con.capitacion_subcontratada ='"+ConstantesBD.acronimoNo+"' OR con.capitacion_subcontratada IS NULL) "+
							"ORDER BY autoriz.fecha_autorizacion DESC ";
	
	/**
	 * Cadena Sql para realizar la insercion de Prorroga de Autorizacion Entidad Subcontratada
	 */
	private static String insertarProrrogaAutorizacionStr="INSERT INTO prorroga_aut_entidad_sub (" +
						  "consecutivo, " +
						  "autorizacion, " +
						  "fecha_vencimiento_inicial, " +
						  "fecha_vencimiento_nueva, " +						  
						  "fecha_prorroga, " +
						  "hora_prorroga, " +
						  "usuario_prorroga )" +
						  "VALUES (?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ? ) ";
	
	/**
	 * Cadena Sql que realiza la acutualizacion de la tabla Autorizaciones Entidades Subcontratadas  por Prorroga de la misma
	 */
	private static String actualizacionAutorizacionXProrroga= "UPDATE  autorizaciones_entidades_sub SET " +
						  "fecha_vencimiento = ?,  " +
						  "fecha_modificacion= CURRENT_DATE, " +
						  "hora_modificacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
						  "usuario_modificacion =  ? " +
						  "WHERE consecutivo = ? ";
	
	/**
	 * Cadena Sql que realiza la acutualizacion de la tabla Autorizaciones Entidades Subcontratadas  por Anulacion de la misma
	 */
	private static String actualizacionAutorizacionXAnulacion= "UPDATE  autorizaciones_entidades_sub SET " +
						   "estado = '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"', " +
						   "contabilizado = '"+ConstantesBD.acronimoNo +"', " +
						   "fecha_anulacion = CURRENT_DATE, " +
						   "hora_anulacion =  "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
						   "usuario_anulacion = ?, " +
						   "motivo_anulacion = ?," +
						   "fecha_modificacion= CURRENT_DATE, " +
						   "hora_modificacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
						   "usuario_modificacion =  ? " +
						   "WHERE consecutivo = ? ";
	
	
	
	/**
	 * Cadena Sql para realizar la consulta del Ingreso asociado a Orden Medica o Solicitud de la Autorizacion
	 */
	private static String ingresoAutorizacionStr="SELECT " +
			              "ing.consecutivo AS ingreso " +
			              "FROM  solicitudes sol " +
			              "INNER JOIN cuentas cu ON (cu.id = sol.cuenta) " +
						  "INNER JOIN ingresos ing ON (ing.id = cu.id_ingreso) " +
						  "WHERE sol.numero_solicitud = ? "; 
			              

	/**
	 * Metodo para consultar las autorizaciones de entidades subcontratadas
	 * @param codPaciente
	 * @param codInstitucion
	 * @return
	 */
	
	@SuppressWarnings("static-access")
	public static ArrayList<DtoAutorizacionEntSubContratada> listadoAutorizacionesEntSub(int codPaciente, int codInstitucion)
	{
		ArrayList<DtoAutorizacionEntSubContratada> autorizaciones = new ArrayList<DtoAutorizacionEntSubContratada>();
		AutorizacionesEntidadesSubcontratadas autorizacionEntSubCont= new AutorizacionesEntidadesSubcontratadas();
		
		String tar=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codInstitucion);
		String cadenaConsulta =consultaAutorizacionesStr;
		
		cadenaConsulta=cadenaConsulta.replace(ConstantesBD.separadorSplit, tar);
		
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,codPaciente);
			
			logger.info("\n CADENA CONSULTA PRORROGA AUTORIZACION ENTIDADES SUBCONTRATADAS >>  cadena >> "+cadenaConsulta+"  CodigoPaciente >> "+codPaciente);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoAutorizacionEntSubContratada autorizacion=new DtoAutorizacionEntSubContratada();
				autorizacion.setCodigoPk(rs.getInt("codigo")+"");
				autorizacion.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaAAp((rs.getString("fechaautorizacion")==null?"":rs.getString("fechaautorizacion"))));
				autorizacion.setHoraAutorizacion(rs.getString("horaautorizacion"));
				autorizacion.setCodEntidadAutorizada(rs.getInt("codentidadautorizada"));
				autorizacion.setNomEntidadAutorizada(rs.getString("nomentidadautorizada"));
				autorizacion.setNumeroSolicitud(rs.getInt("consecutivosolicitud")+"");
				autorizacion.setCodConvenio(rs.getInt("codconvenio"));
				autorizacion.setNomConvenio(rs.getString("nomconvenio"));
				autorizacion.setFechaVencimiento(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechavencimiento")));
				autorizacion.setTipoSolicitud(rs.getInt("tiposolicitud"));
				/*----imprime servicios cuando tipoSolicitud es (5-procedimientos)(14-cirugias)(3-consulta)(4-interconsulta)----*/
				autorizacion.setCodServicio(rs.getInt("codservicio"));
				autorizacion.setNomServicio(rs.getString("nomservicio"));
				autorizacion.setCantidad(rs.getInt("cantservicio")+"");
				autorizacion.setNivel(autorizacionEntSubCont.nivelServicio(rs.getInt("codservicio")));
				autorizacion.setEs_medicamento(rs.getString("esmedicamento"));	
				autorizacion.setConseOrdenMedica(rs.getString("consOrdenMedica"));
			/*----imprime articulos cuando tipoSolicitud es (6-Medicamentos e Insumos)----*/
				autorizacion.setCodInsumo(rs.getString("codarticulo"));				 				
				autorizacion.setDescripInsumo(rs.getString("descriparticulo"));		 
				autorizacion.setUnidMedidaInsumo(rs.getString("unidadarticulo"));	 
				autorizacion.setNroDosisTotalInsumo(rs.getString("dosistotal"));		 
				autorizacion.setCodMedicamento(rs.getString("codarticulo"));			
				autorizacion.setDescripMedicamento(rs.getString("descriparticulo"));	
				autorizacion.setUnidMedidaMedicamento(rs.getString("unidadarticulo"));	
				autorizacion.setNroDosisTotalMedicamento(rs.getString("dosistotal"));		
				autorizacion.setObservaArticulos(rs.getString("obsarticulo"));											
				autorizacion.setDosisMedicamento(rs.getString("dosismedicamento"));		
				autorizacion.setFrecuenciaMedicamento(rs.getString("frecumedicamento"));
				autorizacion.setTipoFrecueMedicamento(rs.getString("tipofrecuemedicamento"));				
				autorizacion.setViaMedicamento(rs.getString("viamedicamento"));							
				autorizacion.setDiasTrataMedicamento(rs.getString("diasmedicamento"));
				//autorizacion.setCodServicio(rs.getInt("codservicio"));
				//autorizacion.setNomServicio(rs.getString("nomservicio"));
				//autorizacion.setCantidad(rs.getInt("cantidad")+"");
				autorizacion.setObservaciones(rs.getString("observaciones"));
				autorizacion.setTipoAutorizacion(rs.getString("tipo"));
				autorizacion.setEstado(rs.getString("estado"));
				autorizacion.setFechaModificacion(UtilidadFecha.conversionFormatoFechaAAp((rs.getString("fechamodifcacion")==null?"":rs.getString("fechamodifcacion")) ));
				autorizacion.setHoraModificacion(rs.getString("horamodificacion"));
				autorizacion.setUsuarioModificacion(rs.getString("usuariomodificacion"));
				autorizacion.setCodInstitucion(rs.getInt("codinstitucion"));
				autorizacion.setNomInstitucion(rs.getString("nominstitucion"));
				autorizacion.setCodPaciente(rs.getInt("codpaciente"));
				autorizacion.setNomPaciente(rs.getString("nompaciente"));   
				autorizacion.setFechaAnulacion(UtilidadFecha.conversionFormatoFechaAAp((rs.getString("fechaanulacion")==null?"":rs.getString("fechaanulacion"))));
				autorizacion.setHoraAnulacion(rs.getString("horaanulacion")==null?"":rs.getString("horaanulacion"));
				autorizacion.setUsuarioAnulacion(rs.getString("usuarioanulacion")==null?"":rs.getString("usuarioanulacion"));
				autorizacion.setConsecutivoAutorizacion(rs.getString("consecutivoautorizacion"));
				autorizacion.setAnoConsecutivo(rs.getString("anioconsecutivo"));
				autorizacion.setCodTipoContrato(rs.getInt("codtipocontrato"));
				autorizacion.setNomTipoContrato(rs.getString("nomtipocontrato"));
				autorizacion.setNivel(autorizacionEntSubCont.nivelServicio(rs.getInt("codservicio")));
				autorizacion.setTipoIdPacinte(rs.getString("tipoidpaciente"));
				autorizacion.setNumIdPaciente(rs.getString("identificacionpaciente"));
				autorizacion.setIngreso(consultarIngreso(rs.getInt("numerosolicitud")));
				
				autorizaciones.add(autorizacion);
				
			}
			 rs.close();
			 ps.close(); 
			
		}
		catch (Exception e) {			
			e.printStackTrace();
			logger.info("\n error en  consulta Autorizaciones entidades subcontratadas >>  cadena >> "+cadenaConsulta+" ");
			
		}
		UtilidadBD.closeConnection(con);	
		return autorizaciones;
	}
	
	
	/**
	 * Metodo que realiza una busqueda por fecha y otros parametros de las Autorizaciones de Entidades Sub-contratadas
	 * @param parametrosBusqueda
	 * @param institucion
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static ArrayList<DtoAutorizacionEntSubContratada> obtenerAutorizacionesEntSubContrXRango(HashMap parametros, int institucion) 
	{
		ArrayList<DtoAutorizacionEntSubContratada> autorizaciones = new ArrayList<DtoAutorizacionEntSubContratada>();
		AutorizacionesEntidadesSubcontratadas autorizacionEntSubCont= new AutorizacionesEntidadesSubcontratadas();
		String cadenaParametros = new String("");
		String tar=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion);
		String cadenaConsulta =consultaAutorizacionesStr;
		cadenaConsulta = cadenaConsulta.replace("AND autoriz.codigo_paciente = ?", "");
		
		cadenaConsulta=cadenaConsulta.replace(ConstantesBD.separadorSplit, tar);
		
		// Parametro Fechas
		if(parametros.containsKey("fechaAutorizacionInicial") && 
				parametros.containsKey("fechaAutorizacionFinal") && 
					!parametros.get("fechaAutorizacionInicial").toString().equals("") && 
						!parametros.get("fechaAutorizacionFinal").toString().equals(""))
		    {
			cadenaParametros += " to_char(autoriz.fecha_autorizacion,'YYYY-MM-DD')  BETWEEN  '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaAutorizacionInicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaAutorizacionFinal").toString())+"'  ";
			
		    }
         
		if(parametros.containsKey("tipoAutorizacion") && !parametros.get("tipoAutorizacion").toString().equals(""))
		  {
			  cadenaParametros += " AND autoriz.tipo = '"+parametros.get("tipoAutorizacion")+"'  ";
		  }
		
		if(parametros.containsKey("entidadAutorizada") && !parametros.get("entidadAutorizada").toString().equals(""))
		  {
			  cadenaParametros += " AND autoriz.entidad_autorizada_sub = '"+parametros.get("entidadAutorizada")+"'  ";
		  }
		
		if(parametros.containsKey("ordenInicial") && !parametros.get("ordenInicial").toString().equals(""))
		  {
			  cadenaParametros += " AND autoriz.numero_solicitud BETWEEN '"+parametros.get("ordenInicial")+"' AND '"+parametros.get("ordenFinal")+ "'  ";
		  }
		
		if(parametros.containsKey("autorizacionInicial") && !parametros.get("autorizacionInicial").toString().equals(""))
		  {
			  cadenaParametros += " AND autoriz.consecutivo_autorizacion BETWEEN '"+parametros.get("autorizacionInicial")+"' AND '"+parametros.get("autorizacionFinal")+ "'  ";
		  }
		
		cadenaConsulta = cadenaConsulta.replace("1=1", cadenaParametros);
		
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		
		
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			
			
			logger.info("\n CADENA CONSULTA AUTORIZACION ENTIDADES SUBCONTRATADAS  PERIODO (prorroga)>>  cadena >> "+cadenaConsulta);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoAutorizacionEntSubContratada autorizacion=new DtoAutorizacionEntSubContratada();
				autorizacion.setCodigoPk(rs.getInt("codigo")+"");
				autorizacion.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaAAp((rs.getString("fechaautorizacion")==null?"":rs.getString("fechaautorizacion"))));
				autorizacion.setHoraAutorizacion(rs.getString("horaautorizacion"));
				autorizacion.setCodEntidadAutorizada(rs.getInt("codentidadautorizada"));
				autorizacion.setNomEntidadAutorizada(rs.getString("nomentidadautorizada"));
				autorizacion.setNumeroSolicitud(rs.getInt("consecutivosolicitud")+"");
				autorizacion.setCodConvenio(rs.getInt("codconvenio"));
				autorizacion.setNomConvenio(rs.getString("nomconvenio"));
				autorizacion.setFechaVencimiento(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechavencimiento")));
				autorizacion.setTipoSolicitud(rs.getInt("tiposolicitud"));
			/*----imprime servicios cuando tipoSolicitud es (5-procedimientos)(14-cirugias)(3-consulta)(4-interconsulta)----*/
				autorizacion.setCodServicio(rs.getInt("codservicio"));
				autorizacion.setNomServicio(rs.getString("nomservicio"));
				autorizacion.setCantidad(rs.getInt("cantservicio")+"");
				autorizacion.setNivel(autorizacionEntSubCont.nivelServicio(rs.getInt("codservicio")));
				autorizacion.setEs_medicamento(rs.getString("esmedicamento"));	
				autorizacion.setConseOrdenMedica(rs.getString("consOrdenMedica"));
			/*----imprime articulos cuando tipoSolicitud es (6-Medicamentos e Insumos)----*/
				autorizacion.setCodInsumo(rs.getString("codarticulo"));				 				
				autorizacion.setDescripInsumo(rs.getString("descriparticulo"));		 
				autorizacion.setUnidMedidaInsumo(rs.getString("unidadarticulo"));	 
				autorizacion.setNroDosisTotalInsumo(rs.getString("dosistotal"));		 
				autorizacion.setCodMedicamento(rs.getString("codarticulo"));			
				autorizacion.setDescripMedicamento(rs.getString("descriparticulo"));	
				autorizacion.setUnidMedidaMedicamento(rs.getString("unidadarticulo"));	
				autorizacion.setNroDosisTotalMedicamento(rs.getString("dosistotal"));		
				autorizacion.setObservaArticulos(rs.getString("obsarticulo"));											
				autorizacion.setDosisMedicamento(rs.getString("dosismedicamento"));		
				autorizacion.setFrecuenciaMedicamento(rs.getString("frecumedicamento"));
				autorizacion.setTipoFrecueMedicamento(rs.getString("tipofrecuemedicamento"));				
				autorizacion.setViaMedicamento(rs.getString("viamedicamento"));							
				autorizacion.setDiasTrataMedicamento(rs.getString("diasmedicamento"));
				/*autorizacion.setCodServicio(rs.getInt("codservicio"));
				autorizacion.setNomServicio(rs.getString("nomservicio"));
				autorizacion.setCantidad(rs.getInt("cantidad")+"");*/
				autorizacion.setObservaciones(rs.getString("observaciones"));
				autorizacion.setTipoAutorizacion(rs.getString("tipo"));
				autorizacion.setEstado(rs.getString("estado"));
				autorizacion.setFechaModificacion(UtilidadFecha.conversionFormatoFechaAAp((rs.getString("fechamodifcacion")==null?"":rs.getString("fechamodifcacion")) ));
				autorizacion.setHoraModificacion(rs.getString("horamodificacion"));
				autorizacion.setUsuarioModificacion(rs.getString("usuariomodificacion"));
				autorizacion.setCodInstitucion(rs.getInt("codinstitucion"));
				autorizacion.setNomInstitucion(rs.getString("nominstitucion"));
				autorizacion.setCodPaciente(rs.getInt("codpaciente"));
				autorizacion.setNomPaciente(rs.getString("nompaciente"));				   
				autorizacion.setFechaAnulacion(UtilidadFecha.conversionFormatoFechaAAp((rs.getString("fechaanulacion")==null?"":rs.getString("fechaanulacion"))));
				autorizacion.setHoraAnulacion(rs.getString("horaanulacion")==null?"":rs.getString("horaanulacion"));
				autorizacion.setUsuarioAnulacion(rs.getString("usuarioanulacion")==null?"":rs.getString("usuarioanulacion"));
				autorizacion.setConsecutivoAutorizacion(rs.getString("consecutivoautorizacion"));
				autorizacion.setAnoConsecutivo(rs.getString("anioconsecutivo"));
				autorizacion.setCodTipoContrato(rs.getInt("codtipocontrato"));
				autorizacion.setNomTipoContrato(rs.getString("nomtipocontrato"));
				autorizacion.setNivel(autorizacionEntSubCont.nivelServicio(rs.getInt("codservicio")));
				autorizacion.setTipoIdPacinte(rs.getString("tipoidpaciente"));
				autorizacion.setNumIdPaciente(rs.getString("identificacionpaciente"));
				autorizacion.setIngreso(consultarIngreso(rs.getInt("numerosolicitud")));
				
				autorizaciones.add(autorizacion);
				
			}
			 rs.close();
			 ps.close(); 
			
		}
		catch (Exception e) {			
			e.printStackTrace();
			logger.info("\n error en  consulta Autorizaciones entidades subcontratadas PERIODO (prorroga)>>  cadena >> "+cadenaConsulta+" ");
			
		}		
	  
		UtilidadBD.closeConnection(con);
		return autorizaciones;
	}
	
	
	
	/**
	 * Metodo que consulta el numero Ingreso de Una orden ( Solicitud)
	 * @param solicitud
	 * @return
	 */
	public static String consultarIngreso(int solicitud)
	{
		String cadena=ingresoAutorizacionStr;
		String respuesta=new String("");
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1, solicitud);
			
			logger.info("\n Cadena Consulta Ingreso >>  cadena >> "+cadena );
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			 if(rs.next())
			    {
				respuesta= rs.getInt("ingreso")+"";
	     		}
	
		
		}catch (Exception e) {			
			e.printStackTrace();
			logger.info("\n error en  consulta Ingreso Solicitud (autorizaciones Ent Sub Contratadas >>  cadena >> "+cadena+" ");
			
		}
		UtilidadBD.closeConnection(con);
		return respuesta;
		
	}

 /**
  * Metodo que guarda los datos de la prorroga
  * @param con
  * @param parametrosProrroga
  * @return
  */
	public static HashMap guardarProrroga(Connection con, HashMap parametrosProrroga) {
		
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		resultado.put("codigoProrroga",ConstantesBD.codigoNuncaValido+"");
		resultado.put("error",errores);
		int consecutivoProrroga = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_prorr_autor_ent_sub_con");
		
		String cadenaInsercion = insertarProrrogaAutorizacionStr;
		String cadenaActualizacionAutorizacion= actualizacionAutorizacionXProrroga;
		
		if(parametrosProrroga.get("fechaProrroga").toString().equals(""))
		{	
		  errores.add("descripcion",new ActionMessage("errors.required","La Fecha de Prorroga"));
		  resultado.put("error",errores);
		}
		
			
		if(!parametrosProrroga.get("fechaProrroga").toString().equals(""))
		{
			if(!UtilidadFecha.validarFecha(parametrosProrroga.get("fechaProrroga").toString()))
			{
				errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," de Prorroga"));
				resultado.put("error",errores);
			}	
			
		  if(UtilidadFecha.esFechaMenorQueOtraReferencia(parametrosProrroga.get("fechaProrroga").toString() , UtilidadFecha.getFechaActual()))
		   {
			errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," de Prorroga"," Actual"));
			resultado.put("error",errores);
		   }
		  
		  if(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(parametrosProrroga.get("fechaProrroga").toString() , parametrosProrroga.get("fechaVencimientoInicial").toString()))
		   {
			errores.add("descripcion",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia"," de Prorroga"," Vencimiento Inicial"));
			resultado.put("error",errores);
		   }
	    }
		
		if(!errores.isEmpty())
		{
			logger.info("\n>>>>> ENTRO AL EMPTY.........");
			return resultado;
		}
		
		try{	   		 
	   		 
			 PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsercion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		     
	   		 ps.setInt(1, consecutivoProrroga);
	   		 ps.setInt(2, Utilidades.convertirAEntero(parametrosProrroga.get("autorizacion").toString()));
	   		 ps.setDate(3,  Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(parametrosProrroga.get("fechaVencimientoInicial").toString())));
	   		 ps.setDate(4,  Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(parametrosProrroga.get("fechaProrroga").toString())));	
	         ps.setString(5,  parametrosProrroga.get("usuarioProrroga").toString());
	   		 
	         if(ps.executeUpdate()>0){
	        	 ps.close();
	        	 try{
	        		 
	        	  PreparedStatementDecorator ps2 =  new PreparedStatementDecorator(con.prepareStatement(cadenaActualizacionAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        	  ps2.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(parametrosProrroga.get("fechaProrroga").toString())));
	        	  ps2.setString(2, parametrosProrroga.get("usuarioProrroga").toString());
	        	  ps2.setInt(3, Utilidades.convertirAEntero(parametrosProrroga.get("autorizacion").toString()));
	        	  
	        	  if(ps2.executeUpdate()>0)
	        	  {
	        		  resultado.put("codigoProrroga",parametrosProrroga.get("autorizacion").toString());
	        		  ps2.close();
	      	    	
	     	    	  return resultado;
	        	   }
	        	  
	        	 }
	     		catch (Exception e) {			
	     			 e.printStackTrace();
	     			 logger.info("\n \n error al Modificar Autorizacion Entidades Subcontratadas>>  cadena >>  "+cadenaActualizacionAutorizacion+" ");
	     			
	     		 }	
	     		
	        	 
	         }
		}
		catch (Exception e) {			
			 e.printStackTrace();
			 logger.info("\n \n error al  Insertar Prorroga Autorizacion Entidades Subcontratadas>>  cadena >>  "+cadenaInsercion+" ");
			
		 }	
		

		return null;
	}

/**
 * Metodo para guardar los datod de la anulacion
 * @param con
 * @param parametrosAnulacion
 * @return
 */
public static HashMap guardarAnulacion(Connection con, HashMap parametrosAnulacion) {
	
	ActionErrors errores = new ActionErrors();
	HashMap resultado = new HashMap();
	resultado.put("codigoAnulacion",ConstantesBD.codigoNuncaValido+"");
	resultado.put("error",errores);
	
	String cadenaActualizacionAutorizacion= actualizacionAutorizacionXAnulacion;
	
	if(parametrosAnulacion.get("motivoAnulacion").toString().equals(""))
	{	
	  errores.add("descripcion",new ActionMessage("errors.required","EL Motivo de Anulacion"));
	  resultado.put("error",errores);
	  return resultado;
	}

	try{
		 
  	  PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaActualizacionAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
  	  ps.setString(1, parametrosAnulacion.get("usuarioAnulacion").toString());
  	  ps.setString(2, parametrosAnulacion.get("motivoAnulacion").toString());
  	  ps.setString(3, parametrosAnulacion.get("usuarioAnulacion").toString());
  	  ps.setInt(4, Utilidades.convertirAEntero(parametrosAnulacion.get("autorizacion").toString()));
  	  
  	   if(ps.executeUpdate()>0)
  	    {
  		  resultado.put("codigoAnulacion",parametrosAnulacion.get("autorizacion").toString());
  		  ps.close();
	    	
	    	  return resultado;
  	     }
  	  
  	 }
		catch (Exception e) {			
			 e.printStackTrace();
			 logger.info("\n \n error al Modificar Autorizacion Entidades Subcontratadas>>  cadena >>  "+cadenaActualizacionAutorizacion+" ");
			
		}	
	
	return null;
	
}



	
	
	
}
