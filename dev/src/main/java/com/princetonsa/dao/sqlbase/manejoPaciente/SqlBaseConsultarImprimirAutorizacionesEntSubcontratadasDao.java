package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacionEntSubContratada;
import com.princetonsa.dto.manejoPaciente.DtoProrrogaAutorizacionEntSub;
import com.princetonsa.mundo.manejoPaciente.AutorizacionesEntidadesSubcontratadas;

public class SqlBaseConsultarImprimirAutorizacionesEntSubcontratadasDao {

	private static Logger logger = Logger.getLogger(SqlBaseConsultarImprimirAutorizacionesEntSubcontratadasDao.class);
	
	
	/**
	 * Cadena Sql para realizar la consulta de las autorizaciones de las entidades Subcontratadas
	 */
	private static String consultaAutorizacionesStr="SELECT " +
							"autoriz.consecutivo AS codigo, " +
							"to_char(autoriz.fecha_autorizacion, 'YYYY-MM-DD') AS fechaautorizacion, " +
							"autoriz.hora_autorizacion AS horaautorizacion, " +
							"autoriz.entidad_autorizada_sub AS codentidadautorizada, " +
							"getdescentitadsubcontratada(autoriz.entidad_autorizada_sub) AS nomentidadautorizada, " +
							"ent.telefono AS telefonoentidad, " +
							"ent.direccion AS direccionentidad, " +
							"aess.numero_solicitud AS numerosolicitud, " + 
							"getConsecutivoSolicitud(aess.numero_solicitud) AS consecutivosolicitud, " +
							"autoriz.convenio AS codconvenio, " +
							"getNombreConvenio(autoriz.convenio) AS nomconvenio, " +
							"to_char(autoriz.fecha_vencimiento, 'YYYY-MM-DD') AS fechavencimiento, " +							
							"solic.tipo AS tiposolicitud,"+
							"solic.acronimo_diagnostico AS acronimodiagnostico,"+ 
							"solic.tipo_cie_diagnostico AS tipociediagnostico,"+ 
							/*-----------------servicios-----------------------*/
							"autoservi.servicio AS codservicio,"+
							"getnombreservicio(autoservi.servicio, " +ConstantesBD.separadorSplit+" ) AS nomservicio, " +//
							"autoservi.cantidad AS cantservicio,"+
							/*-------------------------------------------------*/
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
							"per.fecha_nacimiento as fechanacimiento, " +
							"tipaf.nombre as tipoafiliado, "+
							"subc.semanas_cotizacion as semanascotizacion, "+
							"estsoc.descripcion as clasificacionsocioeconomica, "+
							"solic.fecha_solicitud as fechaorden, "+	
							"natarti.nombre as naturalezaarticulo, "+
							"refser.codigo_propietario as codigopropietario, "+
							"to_char(autoriz.fecha_anulacion, 'YYYY-MM-DD') AS fechaanulacion, " +
							"autoriz.hora_anulacion AS horaanulacion, " +
							"autoriz.usuario_anulacion AS usuarioanulacion,  " +
							"autoriz.motivo_anulacion AS motivoanulacion, " +
							"autoriz.consecutivo_autorizacion AS consecutivoautorizacion, " +
							"autoriz.anio_consecutivo AS anioconsecutivo, " +
							"con.tipo_contrato AS codtipocontrato, " +
							"getNombreTipoContrato(con.tipo_contrato) AS nomtipocontrato, " +
							/*------------------------Medicamento e Insumos---------------------------------*/
							"autoarticu.articulo AS codarticulo, "+
							"solic.consecutivo_ordenes_medicas AS consOrdenMedica, "+
							"arti.descripcion AS descriparticulo, "+
							"unidad.nombre AS unidadarticulo, "+
							"autoarticu.nro_dosis_total AS dosistotal, "+//"autoarticu.cantidad AS cantarticulo, "+	
							"solmedi.observaciones_generales AS obsarticulo,"+
							/*------------------------Medicamentos------------------------------------------*/
							"autoarticu.dosis AS dosismedicamento, "+
							"autoarticu.frecuencia AS frecumedicamento, "+
							"autoarticu.tipo_frecuencia AS tipofrecuemedicamento, "+
							"autoarticu.dias_tratamiento AS diasmedicamento, "+
							"autoarticu.via AS viamedicamento, "+
							"natura.acronimo AS esmedicamento "+														
							/*------------------------------------------------------------------------------*/
							"FROM autorizaciones_entidades_sub autoriz " +
							"INNER JOIN manejopaciente.pacientes pac ON (pac.codigo_paciente = autoriz.codigo_paciente) " +
							"INNER JOIN administracion.personas per ON (per.codigo = pac.codigo_paciente) "+
							"INNER JOIN manejopaciente.ingresos ing ON (ing.codigo_paciente = pac.codigo_paciente) "+
							"INNER JOIN manejopaciente.sub_cuentas subc ON (subc.ingreso = ing.id) "+
							"INNER JOIN manejopaciente.tipos_afiliado tipaf ON (tipaf.acronimo = subc.tipo_afiliado) "+
							"INNER JOIN manejopaciente.estratos_sociales estsoc ON (estsoc.codigo = subc.clasificacion_socioeconomica) "+
							"INNER JOIN convenios con ON (con.codigo = autoriz.convenio) " +
							"INNER JOIN facturacion.entidades_subcontratadas ent ON (ent.codigo_pk = autoriz.entidad_autorizada_sub)" +
							
							// PermitirAutorizarDiferenteDeSolicitudes
							"INNER JOIN ordenes.auto_entsub_solicitudes aess ON (aess.autorizacion_ent_sub = autoriz.consecutivo)" +
							"INNER JOIN ordenes.solicitudes solic ON (solic.numero_solicitud = aess.numero_solicitud)" + 
							 
							
							"LEFT JOIN manejopaciente.autorizaciones_ent_sub_servi autoservi ON (autoservi.autorizacion_ent_sub = autoriz.consecutivo)"+
							"LEFT JOIN facturacion.servicios ser ON (ser.codigo = autoservi.servicio)"+
							"LEFT JOIN facturacion.referencias_servicio refser ON (refser.servicio = ser.codigo)"+
							
							"LEFT JOIN manejopaciente.autorizaciones_ent_sub_articu autoarticu ON (autoarticu.autorizacion_ent_sub = autoriz.consecutivo)"+							
							"LEFT JOIN inventarios.articulo arti ON (arti.codigo = autoarticu.articulo) "+
							"LEFT JOIN inventarios.naturaleza_articulo natarti ON (arti.naturaleza = natarti.acronimo AND arti.institucion=natarti.institucion) "+
							
							"LEFT JOIN ordenes.solicitudes_medicamentos solmedi ON (solmedi.numero_solicitud=solic.numero_solicitud)"+
							"LEFT JOIN inventarios.naturaleza_articulo natura ON (natura.acronimo=arti.naturaleza)"+
																					
							"LEFT JOIN inventarios.unidad_medida unidad ON (unidad.acronimo=arti.unidad_medida)"+
																					
							"WHERE 1=1 AND autoriz.codigo_paciente = ?  AND subc.nro_prioridad=1 AND (refser.tipo_tarifario="+ConstantesBD.separadorSplit+" OR refser.tipo_tarifario is null) ORDER BY autoriz.fecha_autorizacion DESC ";
	
	
	/**
	 * Cadena Sql que realiza la consulta de las entidades Subcontratadas
	 */
	private static String consultaEntidadesSubContratadasStr= "SELECT DISTINCT " +
						   "codigo_pk AS codigopk, " +
						   "institucion AS codinstitucion, " +
						   "getNombreInstitucion(institucion) AS nominstitucion," +
						   "razon_social AS razonsocial  " +
						   "FROM entidades_subcontratadas " +
						   "WHERE activo = '"+ConstantesBD.acronimoSi+"' ";
	
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
	 * Cadena Sql que realiza la consulta de Prorrogas de una Autorizacion
	 */
	private static String consultaProrrogasAutorizacion= "SELECT " +
			              "consecutivo AS consecutivo, " +
						  "autorizacion AS numautorizacion, " +
						  "to_char(fecha_vencimiento_inicial, 'YYYY-MM-DD') AS fechavencimientoinicial, " +
						  "to_char(fecha_vencimiento_nueva, 'YYYY-MM-DD') AS fechavencimientonueva, " +
						  "to_char(fecha_prorroga, 'YYYY-MM-DD') AS fechaprorroga, " +
						  "hora_prorroga AS horaprorroga, " +
						  "usuario_prorroga  AS usuarioprorroga " +
						  "FROM " +
						  "prorroga_aut_entidad_sub " +
						  "WHERE " +
						  "autorizacion = ? "; 
						
	
	/**
	 * Metodo para consultar las autorizaciones de entidades subcontratadas
	 * @param codPaciente
	 * @param codInstitucion
	 * @return
	 */
	
	@SuppressWarnings({ "static-access" })
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
			
			logger.info("\n CADENA CONSULTA AUTORIZACION ENTIDADES SUBCONTRATADAS >>  cadena >> "+cadenaConsulta+"  CodigoPaciente >> "+codPaciente);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoAutorizacionEntSubContratada autorizacion=new DtoAutorizacionEntSubContratada();
				autorizacion.setCodigoPk(rs.getInt("codigo")+"");
				autorizacion.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaAAp((rs.getString("fechaautorizacion")==null?"":rs.getString("fechaautorizacion"))));
				autorizacion.setHoraAutorizacion(rs.getString("horaautorizacion"));
				autorizacion.setCodEntidadAutorizada(rs.getInt("codentidadautorizada"));
				autorizacion.setNomEntidadAutorizada(rs.getString("nomentidadautorizada"));
				autorizacion.setDirEntidadAutorizada(rs.getString("direccionentidad"));
				autorizacion.setTelEntidadAutorizada(rs.getString("telefonoentidad"));
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
				autorizacion.setMotivoAnulacion(rs.getString("motivoanulacion"));
				autorizacion.setConsecutivoAutorizacion(rs.getString("consecutivoautorizacion"));
				autorizacion.setAnoConsecutivo(rs.getString("anioconsecutivo"));
				autorizacion.setCodTipoContrato(rs.getInt("codtipocontrato"));
				autorizacion.setNomTipoContrato(rs.getString("nomtipocontrato"));				
				autorizacion.setTipoIdPacinte(rs.getString("tipoidpaciente"));
				autorizacion.setNumIdPaciente(rs.getString("identificacionpaciente"));
				autorizacion.setIngreso(consultarIngreso(rs.getInt("numerosolicitud")));
				autorizacion.setArrayProrrogas(consultarProrrogasAutorizacion(rs.getInt("codigo")));
				autorizacion.setTipoAfiliado(rs.getString("tipoafiliado"));
				autorizacion.setClasificacionSocioeconomica(rs.getString("clasificacionsocioeconomica"));
				autorizacion.setFechaOrden(rs.getDate("fechaorden"));
				autorizacion.setEdadPaciente(UtilidadFecha.calcularEdad(UtilidadFecha.conversionFormatoFechaAAp(rs.getDate("fechanacimiento")))+"");
				autorizacion.setSemanasCotizacion((rs.getInt("semanascotizacion")));
				autorizacion.setNaturalezaArticulo((rs.getString("naturalezaarticulo")));
				autorizacion.setAcronimoDiagnostico((rs.getString("acronimodiagnostico")));
				autorizacion.setTipoCieDiagnostico((rs.getInt("tipociediagnostico")));
				autorizacion.setCodigoPropietario((rs.getString("codigopropietario")));
				
				
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
	@SuppressWarnings({ "rawtypes", "static-access" })
	public static ArrayList<DtoAutorizacionEntSubContratada> obtenerAutorizacionesEntSubContrXRango(HashMap parametros, int institucion) 
	{
		ArrayList<DtoAutorizacionEntSubContratada> autorizaciones = new ArrayList<DtoAutorizacionEntSubContratada>();
		AutorizacionesEntidadesSubcontratadas autorizacionEntSubCont= new AutorizacionesEntidadesSubcontratadas();
		String cadenaParametros = new String("");
		String tar=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion);
		String cadenaConsulta =consultaAutorizacionesStr;
		cadenaConsulta = cadenaConsulta.replace("AND autoriz.codigo_paciente = ?", "");
		
		cadenaConsulta=cadenaConsulta.replace(ConstantesBD.separadorSplit, tar);
		
		 //Parametro Fechas
		if(parametros.containsKey("fechaAutorizacionInicial") && 
				parametros.containsKey("fechaAutorizacionFinal") && 
					!parametros.get("fechaAutorizacionInicial").toString().equals("") && 
						!parametros.get("fechaAutorizacionFinal").toString().equals(""))
		    {
			cadenaParametros += " to_char(autoriz.fecha_autorizacion, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaAutorizacionInicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaAutorizacionFinal").toString())+"'  ";
			
		    }
         
		if(parametros.containsKey("tipoAutorizacion") && !parametros.get("tipoAutorizacion").toString().equals(""))
		  {
			  cadenaParametros += " AND autoriz.tipo = '"+parametros.get("tipoAutorizacion")+"'  ";
		  }
		
		if(parametros.containsKey("estadoAutorizacion") && !parametros.get("estadoAutorizacion").toString().equals(""))
		  {
			  if(parametros.get("estadoAutorizacion").toString().equals("*"))
			  {
				  cadenaParametros += " AND autoriz.estado IN ('"+ConstantesIntegridadDominio.acronimoAutorizado+"', '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"')  ";
			  }else
			    {
			    cadenaParametros += " AND autoriz.estado = '"+parametros.get("estadoAutorizacion")+"'  ";
			    }
		  }
		
		if(parametros.containsKey("entidadAutorizada") && !parametros.get("entidadAutorizada").toString().equals(""))
		  {
			  cadenaParametros += " AND autoriz.entidad_autorizada_sub = '"+parametros.get("entidadAutorizada")+"'  ";
		  }
		
		if(parametros.containsKey("ordenInicial") && !parametros.get("ordenInicial").toString().equals(""))
		  {
			  cadenaParametros += " AND getConsecutivoSolicitud(autoriz.numero_solicitud) BETWEEN '"+parametros.get("ordenInicial")+"' AND '"+parametros.get("ordenFinal")+ "'  ";
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
			
			
			logger.info("\n CADENA CONSULTA AUTORIZACION ENTIDADES SUBCONTRATADAS  PERIODO>>  cadena >> "+cadenaConsulta);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoAutorizacionEntSubContratada autorizacion=new DtoAutorizacionEntSubContratada();
				autorizacion.setCodigoPk(rs.getInt("codigo")+"");
				autorizacion.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaAAp((rs.getString("fechaautorizacion")==null?"":rs.getString("fechaautorizacion"))));
				autorizacion.setHoraAutorizacion(rs.getString("horaautorizacion"));
				autorizacion.setCodEntidadAutorizada(rs.getInt("codentidadautorizada"));
				autorizacion.setNomEntidadAutorizada(rs.getString("nomentidadautorizada"));
				autorizacion.setDirEntidadAutorizada(rs.getString("direccionentidad"));
				autorizacion.setTelEntidadAutorizada(rs.getString("telefonoentidad"));
				autorizacion.setNumeroSolicitud(rs.getInt("consecutivosolicitud")+"");
				autorizacion.setCodConvenio(rs.getInt("codconvenio"));
				autorizacion.setNomConvenio(rs.getString("nomconvenio"));
				autorizacion.setFechaVencimiento(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechavencimiento")));
				autorizacion.setTipoSolicitud(rs.getInt("tiposolicitud")); //Se añade para mostrar el detalle de servicios o medicamentos
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
				autorizacion.setMotivoAnulacion(rs.getString("motivoanulacion"));
				autorizacion.setConsecutivoAutorizacion(rs.getString("consecutivoautorizacion"));
				autorizacion.setAnoConsecutivo(rs.getString("anioconsecutivo"));
				autorizacion.setCodTipoContrato(rs.getInt("codtipocontrato"));
				autorizacion.setNomTipoContrato(rs.getString("nomtipocontrato"));
				//autorizacion.setNivel(autorizacionEntSubCont.nivelServicio(rs.getInt("codservicio")));
				autorizacion.setTipoIdPacinte(rs.getString("tipoidpaciente"));
				autorizacion.setNumIdPaciente(rs.getString("identificacionpaciente"));
				autorizacion.setIngreso(consultarIngreso(rs.getInt("numerosolicitud")));
				autorizacion.setArrayProrrogas(consultarProrrogasAutorizacion(rs.getInt("codigo")));
				autorizacion.setTipoAfiliado(rs.getString("tipoafiliado"));
				autorizacion.setClasificacionSocioeconomica(rs.getString("clasificacionsocioeconomica"));
				autorizacion.setFechaOrden(rs.getDate("fechaorden"));
				autorizacion.setEdadPaciente(UtilidadFecha.calcularEdad(UtilidadFecha.conversionFormatoFechaAAp(rs.getDate("fechanacimiento")))+"");
				autorizacion.setSemanasCotizacion((rs.getInt("semanascotizacion")));
				autorizacion.setNaturalezaArticulo((rs.getString("naturalezaarticulo")));
				autorizacion.setAcronimoDiagnostico((rs.getString("acronimodiagnostico")));
				autorizacion.setTipoCieDiagnostico((rs.getInt("tipociediagnostico")));
				autorizacion.setCodigoPropietario((rs.getString("codigopropietario")));
				
				autorizaciones.add(autorizacion);
				
			}
			 rs.close();
			 ps.close(); 
			
		}
		catch (Exception e) {			
			e.printStackTrace();
			logger.info("\n error en  consulta Autorizaciones entidades subcontratadas PERIODO >>  cadena >> "+cadenaConsulta+" ");
			
		}		
		
		UtilidadBD.closeConnection(con);
		return autorizaciones;
	}
	
	
	
	
	
	
	
	
	/**
	 * Metodo que lista las entidades Subcontratadas
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public  static HashMap listaEntidadesSubcontratadas()
	{
		HashMap entidades= new HashMap();
		String cadena=consultaEntidadesSubContratadasStr;
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			
			
			logger.info("\n CADENA CONSULTA ENTIDADES SUBCONTRATADAS >>  cadena >> "+cadena );
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			entidades= UtilidadBD.cargarValueObject(rs, true, false);
		
		}catch (Exception e) {			
			e.printStackTrace();
			logger.info("\n error en  consulta Autorizaciones entidades subcontratadas >>  cadena >> "+cadena+" ");
			
		}
		UtilidadBD.closeConnection(con);
		
		return entidades;
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
	 * 
	 * @param autorizacion
	 * @return
	 */
	public static ArrayList<DtoProrrogaAutorizacionEntSub> consultarProrrogasAutorizacion(int autorizacion)
	{
		ArrayList<DtoProrrogaAutorizacionEntSub> arrayProrrogas= new ArrayList<DtoProrrogaAutorizacionEntSub>();
		
		String cadena=consultaProrrogasAutorizacion;
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1, autorizacion);
			
			logger.info("\n Cadena Consulta Prorrogas Autorizacion >>  cadena >> "+cadena );
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			 while(rs.next())
			    {
				 DtoProrrogaAutorizacionEntSub dto=new DtoProrrogaAutorizacionEntSub();
				 dto.setConsecutivo(rs.getInt("consecutivo")+"");
				 dto.setAutorizacion(rs.getInt("numautorizacion")+"");
				 dto.setFechaVencimientoInicial(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechavencimientoinicial")));
				 dto.setFechaVencimientoNueva(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechavencimientonueva")));
				 dto.setFechaProrroga(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaprorroga")));
	     		 dto.setHoraProrroga(rs.getString("horaprorroga"));
	     		 dto.setUsuarioProrroga(rs.getString("usuarioprorroga"));
	     		 arrayProrrogas.add(dto);
			    }
	          rs.close();
	          ps.close();
		
		}catch (Exception e) {			
			e.printStackTrace();
			logger.info("\n error en  consulta Prorrogas Autorizacion (autorizaciones Ent Sub Contratadas >>  cadena >> "+cadena+" ");
			
		}
		UtilidadBD.closeConnection(con);
		logger.info("\n \n \n Numero de Prorrogas  >>  Nro >>  "+arrayProrrogas.size() );
		return arrayProrrogas;
	}
	
	
	
}
