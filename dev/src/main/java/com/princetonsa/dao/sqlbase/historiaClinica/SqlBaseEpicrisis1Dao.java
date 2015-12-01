package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.princetonsa.dto.epicrisis.DtoCirugiaEpicrisis;
import com.princetonsa.dto.epicrisis.DtoEpicrisis1;
import com.princetonsa.dto.epicrisis.DtoEpicrisisSecciones;
import com.princetonsa.dto.epicrisis.DtoEventoAdversoEpicrisis;
import com.princetonsa.dto.epicrisis.DtoEvolucionEpicrisis;
import com.princetonsa.dto.epicrisis.DtoInterpretacionSolicitud;
import com.princetonsa.dto.epicrisis.DtoMedicamentoEpicrisis;
import com.princetonsa.dto.epicrisis.DtoMedicamentosAdminEpicrisis;
import com.princetonsa.dto.epicrisis.DtoNotasAclaratoriasEpicrisis;
import com.princetonsa.dto.epicrisis.DtoNotasCirugiaEpicrisis;
import com.princetonsa.dto.epicrisis.DtoProcedimientosEpicrisis;
import com.princetonsa.dto.epicrisis.DtoServiciosCirugiaEpicrisis;
import com.princetonsa.dto.epicrisis.DtoValoracionEpicrisis;
import com.princetonsa.dto.epicrisis.DtoValoracionHospitalizacionEpicrisis;
import com.princetonsa.dto.epicrisis.DtoValoracionUrgenciasEpicrisis;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.Epicrisis1;
import com.princetonsa.mundo.historiaClinica.Epicrisis1Evoluciones;
import com.princetonsa.mundo.historiaClinica.Epicrisis1Valoraciones;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.ordenesmedicas.cirugias.HojaQuirurgica;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author wilson
 *
 */
public class SqlBaseEpicrisis1Dao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger= Logger.getLogger(SqlBaseEpicrisis1Dao.class);
	
	/**
	 * 
	 */
	private static String order=" order by fecha,hora ";
	
	/**
	 * obtiene los tipos de evolucion dados los combinados de via ingreso - tipo paciente
	 * @param con
	 * @param viasIngresoTiposPaciente
	 * @return
	 */
	public static HashMap<Object, Object> obtenerTiposEvolucion(Connection con, ArrayList<InfoDatosInt> viasIngresoTiposPaciente )
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		mapa.put("numRegistros", "0");
		String consulta="SELECT distinct " +
							"t1.codigo as codigotipoevolucion, " +
							"t1.nombre as nombretipoevolucion, " +
							"'"+ConstantesBD.acronimoNo+"' as seleccionado " +
						"FROM " +
							"tipos_evol_epi_via_ing t " +
							"INNER JOIN tipos_evol_epicrisis t1 ON(t.tipo_evol_epicrisis=t1.codigo) " +
						"WHERE ";
		
		boolean esPrimero=true;
		
		for(int w=0; w<viasIngresoTiposPaciente.size(); w++)
		{
			if(!esPrimero)
				consulta+=" or ";
			consulta+= "(via_ingreso="+viasIngresoTiposPaciente.get(w).getCodigo()+" AND tipo_paciente='"+viasIngresoTiposPaciente.get(w).getNombre()+"') ";
			esPrimero=false;
		}
		
		consulta+=" order by 2 ";
							
		logger.info("\n obtenerTiposEvolucion->"+consulta+" \n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param criteriosBusquedaMap
	 * @param ingreso
	 * @return
	 */
	public static HashMap<Object, Object> cargarCuadroTexto(Connection con, Vector<String> cuentas, HashMap<Object, Object> criteriosBusquedaMap, int ingreso, boolean cargarTodas, boolean cargarValoracionesIniciales)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		mapa.put("numRegistros", "0");
		String consulta="SELECT " +
							"tabla.codigopk, " +
							"tabla.fecha, " +
							"tabla.fechabd, " +
							"tabla.hora, " +
							"tabla.contenido, " +
							"tabla.codigotipoevolucion, " +
							"'"+ConstantesBD.acronimoNo+"' as resaltar, " +
							"tabla.infoautomatica " +
						"FROM" +
							"( ";
		
		
		boolean esPrimero=true;
		for(int w=0; w<Utilidades.convertirAEntero(criteriosBusquedaMap.get("numRegistros")+""); w++)
		{
			if(UtilidadTexto.getBoolean( criteriosBusquedaMap.get("seleccionado_"+w)+"") || cargarTodas)
			{
				//CARGAMOS LAS VALORACIONES INICIALES SI ES NECESARIO DE HOSPITALIZACION
				if(cargarValoracionesIniciales)
				{
					if(!esPrimero)
						consulta+=" UNION ";
					consulta+="(";
					consulta+=" SELECT " +
									"e.numero_solicitud as codigopk, " +
									"to_char(e.fecha, 'DD/MM/YYYY') as fecha, " +
									"e.fecha as fechabd, " +
									"substr(e.hora,1,5) as hora, " +
									UtilidadBD.convertirDatoClob("e.contenido", Epicrisis1.maximoContenidoCuadroTexto, 1)+" as contenido, " +
									ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialHosp +" as codigotipoevolucion, " +
									UtilidadBD.convertirDatoClob("e.info_automatica", Epicrisis1.maximoContenidoCuadroTexto, 1)+" as infoautomatica "+
								"FROM " +
									"epicrisis_solicitudes e " +
									"inner join solicitudes s on(s.numero_solicitud=e.numero_solicitud) " +
									"INNER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud) " +
									"INNER JOIN val_hospitalizacion vh ON(vh.numero_solicitud=s.numero_solicitud)" +
								"WHERE " +
									"e.tipo = "+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+" "+
									"and s.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
									//ESTAS SE DEBEN CARGAR INDEPENDIENTE DEL RANGO DE FECHAS, SI DICEN LO CONTRARIO SOLO ES DESCOMENTARIAR 
									//"and s.fecha_solicitud between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' "+
									"and s.estado_historia_clinica in( "+ConstantesBD.codigoEstadoHCInterpretada+", "+ConstantesBD.codigoEstadoHCRespondida+" ) "+
									"and v.cuidado_especial='"+ConstantesBD.acronimoNo+"' ";
						
					consulta+=") ";
					esPrimero=false;
					
					//CARGAMOS LAS VALORACIONES DE URGENCIAS
					if(!esPrimero)
						consulta+=" UNION ";
					consulta+="(";
					consulta+=" SELECT " +
									"e.numero_solicitud as codigopk, " +
									"to_char(e.fecha, 'DD/MM/YYYY') as fecha, " +
									"e.fecha as fechabd, " +
									"substr(e.hora,1,5) as hora, " +
									UtilidadBD.convertirDatoClob("e.contenido", Epicrisis1.maximoContenidoCuadroTexto, 1)+" as contenido, " +
									ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialUrg +" as codigotipoevolucion, " +
									UtilidadBD.convertirDatoClob("e.info_automatica", Epicrisis1.maximoContenidoCuadroTexto, 1)+" as infoautomatica "+
								"FROM " +
									"epicrisis_solicitudes e " +
									"inner join solicitudes s on(s.numero_solicitud=e.numero_solicitud) " +
									"INNER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud) " +
									"INNER JOIN valoraciones_urgencias vu ON(vu.numero_solicitud=s.numero_solicitud)" +
								"WHERE " +
									"e.tipo = "+ConstantesBD.codigoTipoSolicitudInicialUrgencias+" "+
									"and s.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
									//ESTAS SE DEBEN CARGAR INDEPENDIENTE DEL RANGO DE FECHAS, SI DICEN LO CONTRARIO SOLO ES DESCOMENTARIAR 
									//"and s.fecha_solicitud between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' "+
									"and s.estado_historia_clinica in( "+ConstantesBD.codigoEstadoHCInterpretada+", "+ConstantesBD.codigoEstadoHCRespondida+" ) "+
									"and v.cuidado_especial='"+ConstantesBD.acronimoNo+"' ";
						
					consulta+=") ";
					esPrimero=false;
					
					//CARGAMOS LAS VALORACIONES INICIALES DE CUIDADOS ESPECIALES
					if(!esPrimero)
						consulta+=" UNION ";
					consulta+="(";
					consulta+=" SELECT " +
									"e.numero_solicitud as codigopk, " +
									"to_char(e.fecha, 'DD/MM/YYYY') as fecha, " +
									"e.fecha as fechabd, " +
									"substr(e.hora,1,5) as hora, " +
									UtilidadBD.convertirDatoClob("e.contenido", Epicrisis1.maximoContenidoCuadroTexto, 1)+" as contenido, " +
									ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialHosp +" as codigotipoevolucion, " +
									UtilidadBD.convertirDatoClob("e.info_automatica", Epicrisis1.maximoContenidoCuadroTexto, 1)+" as infoautomatica "+
								"FROM " +
									"epicrisis_solicitudes e " +
									"inner join solicitudes s on(s.numero_solicitud=e.numero_solicitud) " +
									"INNER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud) " +
									"INNER JOIN ingresos_cuidados_especiales i ON(i.valoracion=s.numero_solicitud) " +
								"WHERE " +
									"e.tipo= "+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+" "+
									"and s.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
									//"and s.fecha_solicitud between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' "+
									"and s.estado_historia_clinica in( "+ConstantesBD.codigoEstadoHCInterpretada+", "+ConstantesBD.codigoEstadoHCRespondida+" ) "+
									"and v.cuidado_especial='"+ConstantesBD.acronimoSi+"' " +
									//con esta condicion identifico que es una valoracion inicial
									"and i.valoracion=i.valoracion_orden " +
									//segun acuerdo con Paula nunca se considera valoracion inicial a los cuidados especiales cuando existe previamente una valoracion de hospitalizacion 
									"and (" +
											"select count(1) " +
											"from " +
												"solicitudes s1 " +
												"inner join valoraciones v1 on(v1.numero_solicitud=s1.numero_solicitud) " +
											"where " +
												"s1.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
												"and s1.tipo ="+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+" " +
												"and v1.cuidado_especial='"+ConstantesBD.acronimoNo+"'" +
										")<=0 ";
					consulta+=") ";
					esPrimero=false;
					
				}
				
				//PRIMERO EVOLUCIONES
				if(Utilidades.convertirAEntero(criteriosBusquedaMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisRegistroEvoluciones || cargarTodas)
				{
					if(!esPrimero)
						consulta+=" UNION ";
					consulta+="(";
					consulta+=" SELECT " +
									"e.evolucion as codigopk, " +
									"to_char(e.fecha, 'DD/MM/YYYY') as fecha, " +
									"e.fecha as fechabd, " +
									"substr(e.hora,1,5) as hora, " +
									UtilidadBD.convertirDatoClob("e.contenido", Epicrisis1.maximoContenidoCuadroTexto, 1)+" as contenido, " +
									ConstantesBD.codigoTipoEvolucionEpicrisisRegistroEvoluciones +" as codigotipoevolucion, " +
									"'' as infoautomatica "+
								"FROM " +
									"epicrisis_evoluciones e " +
									"INNER JOIN evoluciones ev ON(e.evolucion=ev.codigo) " +
									"INNER JOIN solicitudes s ON(s.numero_solicitud=ev.valoracion) " +
								"WHERE  " +
									"s.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
									"and ev.fecha_evolucion between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' " +
									"and s.estado_historia_clinica <> "+ConstantesBD.codigoEstadoHCAnulada+" " +
									//no podemos cargar la evolucion que genero el egreso
									"and ev.codigo NOT IN (SELECT eg.evolucion FROM egresos eg WHERE eg.cuenta=s.cuenta and eg.codigo_medico is not null ) ";
					consulta+=") ";
					esPrimero=false;
				}
				
				//SEGUNDO EVENTOS ADVERSOS ADMINISTRATIVOS
				if(Utilidades.convertirAEntero(criteriosBusquedaMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisEventosAdversosAdministrativos || cargarTodas)
				{
					if(!esPrimero)
						consulta+=" UNION ";
					consulta+="(";
					consulta+=" SELECT " +
									"e.codigo_evento as codigopk, " +
									"to_char(e.fecha, 'DD/MM/YYYY') as fecha, " +
									"fecha as fechabd, " +
									"substr(e.hora,1,5) as hora, " +
									UtilidadBD.convertirDatoClob("e.contenido", Epicrisis1.maximoContenidoCuadroTexto, 1)+" as contenido, " +
									ConstantesBD.codigoTipoEvolucionEpicrisisEventosAdversosAdministrativos +" as codigotipoevolucion, " +
									"'' as infoautomatica "+
								"FROM " +
									"epicrisis_eventos_adversos e " +
									"INNER JOIN registro_eventos_adversos r ON(r.codigo=e.codigo_evento) " +
									"INNER JOIN eventos_adversos e1 ON(e1.codigopk= r.evento_adverso) " +
								"WHERE " +
									"r.ingreso="+ingreso+" " +
									"and r.activo='"+ConstantesBD.acronimoSi+"' " +
									"and r.fecha_registro between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' " +
									"and e1.tipo='"+ConstantesIntegridadDominio.acronimoTipoEventoAdministrativo+"' ";
					consulta+=") ";
					esPrimero=false;
				}
				
				//TERCERO EVENTOS ADVERSOS ASISTENCIALES
				if(Utilidades.convertirAEntero(criteriosBusquedaMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisEventosAdversosAsistenciales || cargarTodas)
				{
					if(!esPrimero)
						consulta+=" UNION ";
					consulta+="(";
					consulta+=" SELECT " +
									"e.codigo_evento as codigopk, " +
									"to_char(e.fecha, 'DD/MM/YYYY') as fecha, " +
									"e.fecha as fechabd, " +
									"substr(e.hora,1,5) as hora, " +
									UtilidadBD.convertirDatoClob("e.contenido", Epicrisis1.maximoContenidoCuadroTexto, 1)+" as contenido, " +
									ConstantesBD.codigoTipoEvolucionEpicrisisEventosAdversosAsistenciales +" as codigotipoevolucion, " +
									"'' as infoautomatica "+
								"FROM " +
									"epicrisis_eventos_adversos e " +
									"INNER JOIN registro_eventos_adversos r ON(r.codigo=e.codigo_evento) " +
									"INNER JOIN eventos_adversos e1 ON(e1.codigopk= r.evento_adverso) " +
								"WHERE " +
									"r.ingreso="+ingreso+" " +
									"and r.activo='"+ConstantesBD.acronimoSi+"' " +
									"and r.fecha_registro between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' " +
									"and e1.tipo='"+ConstantesIntegridadDominio.acronimoTipoEventoAsistencial+"' ";
					consulta+=") ";
					esPrimero=false;
				}
				
				//CUARTO PROCEDIMIENTOS
				if(Utilidades.convertirAEntero(criteriosBusquedaMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisProcedimientos || cargarTodas)
				{
					if(!esPrimero)
						consulta+=" UNION ";
					consulta+="(";
					consulta+=" SELECT " +
									"e.numero_solicitud as codigopk, " +
									"to_char(e.fecha, 'DD/MM/YYYY') as fecha, " +
									"e.fecha as fechabd, " +
									"substr(e.hora,1,5) as hora, " +
									UtilidadBD.convertirDatoClob("e.contenido", Epicrisis1.maximoContenidoCuadroTexto, 1)+" as contenido, " +
									ConstantesBD.codigoTipoEvolucionEpicrisisProcedimientos +" as codigotipoevolucion, " +
									"'' as infoautomatica "+
								"FROM " +
									"epicrisis_solicitudes e " +
									"INNER JOIN res_sol_proc r ON(r.numero_solicitud = e.numero_solicitud) " +
									"INNER JOIN sol_procedimientos sp ON(sp.numero_solicitud=r.numero_solicitud) " +
									"INNER JOIN solicitudes s ON(s.numero_solicitud=r.numero_solicitud) " +
								"WHERE  " +
									"e.tipo= "+ConstantesBD.codigoTipoSolicitudProcedimiento+" "+
									"and s.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
									"and r.fecha_ejecucion between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' "+
									"and s.estado_historia_clinica <> "+ConstantesBD.codigoEstadoHCAnulada+" ";
					consulta+=") ";
					esPrimero=false;
				}
				
				//QUINTO MEDICAMENTOS
				if(Utilidades.convertirAEntero(criteriosBusquedaMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisMedicamentos || cargarTodas)
				{
					if(!esPrimero)
						consulta+=" UNION ";
					consulta+="(";
					consulta+=" SELECT " +
									"e.codigo_administracion as codigopk, " +
									"to_char(e.fecha, 'DD/MM/YYYY') as fecha, " +
									"e.fecha as fechabd, " +
									"substr(e.hora,1,5) as hora, " +
									UtilidadBD.convertirDatoClob("e.contenido", Epicrisis1.maximoContenidoCuadroTexto, 1)+" as contenido, " +
									ConstantesBD.codigoTipoEvolucionEpicrisisMedicamentos +" as codigotipoevolucion, " +
									"'' as infoautomatica "+
								"from " +
									"epicrisis_admin_med e " +
									"INNER JOIN admin_medicamentos a ON(e.codigo_administracion=a.codigo) " +
									"INNER JOIN solicitudes s ON(s.numero_solicitud=a.numero_solicitud) " +
								"WHERE  " +
									"s.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
									"and a.fecha_grabacion between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' "+
									"and s.estado_historia_clinica <> "+ConstantesBD.codigoEstadoHCAnulada+" ";
					consulta+=") ";
					esPrimero=false;
				}
				
				//SEXTO SOLICITUDES CX
				if(Utilidades.convertirAEntero(criteriosBusquedaMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisCirugia || cargarTodas)
				{
					if(!esPrimero)
						consulta+=" UNION ";
					consulta+="(";
					consulta+=" SELECT " +
									"e.numero_solicitud as codigopk, " +
									"to_char(e.fecha, 'DD/MM/YYYY') as fecha, " +
									"e.fecha as fechabd, " +
									"substr(e.hora,1,5) as hora, " +
									UtilidadBD.convertirDatoClob("e.contenido", Epicrisis1.maximoContenidoCuadroTexto, 1)+" as contenido, " +
									ConstantesBD.codigoTipoEvolucionEpicrisisCirugia +" as codigotipoevolucion, " +
									"'' as infoautomatica "+
								"FROM " +
									"epicrisis_solicitudes e " +
									"inner join solicitudes_cirugia sc ON(sc.numero_solicitud=e.numero_solicitud) " +
									"INNER JOIN solicitudes s ON(s.numero_solicitud=sc.numero_solicitud) " +
								"WHERE  " +
									"e.tipo= "+ConstantesBD.codigoTipoSolicitudCirugia+" "+
									"and s.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
									"and sc.fecha_inicial_cx between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' "+
									"and s.estado_historia_clinica in( "+ConstantesBD.codigoEstadoHCInterpretada+", "+ConstantesBD.codigoEstadoHCRespondida+" ) ";
					consulta+=") ";
					esPrimero=false;
				}
				
				//SEPTIMO INTERCONSULTAS
				if(Utilidades.convertirAEntero(criteriosBusquedaMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisInterconsulta || cargarTodas)
				{
					if(!esPrimero)
						consulta+=" UNION ";
					consulta+="(";
					consulta+=" SELECT " +
									"e.numero_solicitud as codigopk, " +
									"to_char(e.fecha, 'DD/MM/YYYY') as fecha, " +
									"e.fecha as fechabd, " +
									"substr(e.hora,1,5) as hora, " +
									UtilidadBD.convertirDatoClob("e.contenido", Epicrisis1.maximoContenidoCuadroTexto, 1)+" as contenido, " +
									ConstantesBD.codigoTipoEvolucionEpicrisisInterconsulta +" as codigotipoevolucion, " +
									"'' as infoautomatica "+
								"FROM " +
									"epicrisis_solicitudes e " +
									"inner join solicitudes_inter si on(si.numero_solicitud=e.numero_solicitud)	" +
									"INNER JOIN solicitudes s ON(s.numero_solicitud=si.numero_solicitud) " +
									"INNER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud)  " +
								"WHERE  " +
									"e.tipo= "+ConstantesBD.codigoTipoSolicitudInterconsulta+" "+
									"and s.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
									"and s.fecha_solicitud between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' "+
									"and s.estado_historia_clinica in( "+ConstantesBD.codigoEstadoHCInterpretada+", "+ConstantesBD.codigoEstadoHCRespondida+" ) ";
					consulta+=") ";
					esPrimero=false;
				}
				
				//OCTAVO CUIDADOS ESPECIALES
				if(Utilidades.convertirAEntero(criteriosBusquedaMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisCuidadosEspeciales
						|| cargarTodas)
				{
					if(!esPrimero)
						consulta+=" UNION ";
					consulta+="(";
					consulta+=" SELECT " +
									"e.numero_solicitud as codigopk, " +
									"to_char(e.fecha, 'DD/MM/YYYY') as fecha, " +
									"e.fecha as fechabd, " +
									"substr(e.hora,1,5) as hora, " +
									UtilidadBD.convertirDatoClob("e.contenido", Epicrisis1.maximoContenidoCuadroTexto, 1)+" as contenido, " +
									criteriosBusquedaMap.get("codigotipoevolucion_"+w) +" as codigotipoevolucion, " +
									UtilidadBD.convertirDatoClob("e.info_automatica", Epicrisis1.maximoContenidoCuadroTexto, 1)+" as infoautomatica "+
								"FROM " +
									"epicrisis_solicitudes e " +
									"inner join solicitudes s on(s.numero_solicitud=e.numero_solicitud) " +
									"INNER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud) " +
									"INNER JOIN ingresos_cuidados_especiales i ON(i.valoracion=s.numero_solicitud) " +
								"WHERE " +
									"e.tipo= "+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+" "+
									"and s.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
									"and s.fecha_solicitud between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' "+
									"and s.estado_historia_clinica in( "+ConstantesBD.codigoEstadoHCInterpretada+", "+ConstantesBD.codigoEstadoHCRespondida+" ) "+
									"and v.cuidado_especial='"+ConstantesBD.acronimoSi+"' " +
									//con esto identifico que no es una valoracion inicial 
									//"and i.valoracion<>i.valoracion_orden " +-------------->SE REEMPLAZA POR ESTE ENUNCIADO DE PAULA 2008-08-01
									//segun acuerdo con Paula nunca se considera valoracion inicial a los cuidados especiales cuando existe previamente una valoracion de hospitalizacion 
									"and (" +
											"(" +
												"i.valoracion=i.valoracion_orden " +
												"AND (" +
														"select count(1) " +
														"from " +
															"solicitudes s1 " +
															"inner join valoraciones v1 on(v1.numero_solicitud=s1.numero_solicitud) " +
														"where " +
															"s1.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
															"and s1.fecha_solicitud between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' "+
															"and s1.estado_historia_clinica in( "+ConstantesBD.codigoEstadoHCInterpretada+", "+ConstantesBD.codigoEstadoHCRespondida+" ) "+
															"and s1.tipo ="+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+" " +
															"and v1.cuidado_especial='"+ConstantesBD.acronimoNo+"' " +
													")>0 " +
											") OR i.valoracion<>i.valoracion_orden " +
										") ";
					consulta+=") ";
					esPrimero=false;
				}
				
			}
			
			if(cargarTodas)
				w=Utilidades.convertirAEntero(criteriosBusquedaMap.get("numRegistros")+"");
		}
		
		consulta+=") tabla order by ";
		consulta+=" tabla.fechabd asc, tabla.hora asc ";
		
		logger.info("\n cargarCuadroTexto->"+consulta+" \n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			for(int w=0; w<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); w++)
			{
				mapa.put("contenido_"+w, mapa.get("contenido_"+w).toString().toUpperCase());
			}
			
			logger.info("\n\n************************MAPA->"+mapa);
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param criteriosBusquedaMap
	 * @param ingreso
	 * @param valoracionesInicialesAmostrar 
	 * @return
	 */
	public static HashMap<Object, Object> obtenerFechaHoraSolicitudes(Connection con, Vector<String> cuentas, HashMap<Object, Object> criteriosBusquedaMap, int ingreso, HashMap<Object, Object> valoracionesInicialesAmostrar, String castTexto, String order)
	{
		logger.info("criteriosBusqueda---->"+criteriosBusquedaMap);
		
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		mapa.put("numRegistros", "0");
		String consulta="SELECT " +
							"tabla.fecha, " +
							"tabla.fechabd, " +
							"tabla.hora, " +
							"tabla.codigopk, " +
							"tabla.codigotipoevolucion, " +
							"tabla.nombretipoevolucion, " +
							"tabla.yaenviadoepicrisis, " +
							"tabla.enviarepicrisis " +
						"FROM" +
							"( ";
		boolean esPrimero=true;
		
		//primero hacemos la selección de las valoraciones iniciales seleccionadas
		for(int w=0; w<Utilidades.convertirAEntero(valoracionesInicialesAmostrar.get("numRegistros")+"");w++)
		{
			logger.info("sel->"+valoracionesInicialesAmostrar.get("seleccionadaval_"+w));
			if(UtilidadTexto.getBoolean( valoracionesInicialesAmostrar.get("seleccionadaval_"+w)+""))
			{
				logger.info("entra");
				if(!esPrimero)
					consulta+=" UNION ";
				
				int tipoEvolucionEpicrisis= (valoracionesInicialesAmostrar.get("tiposolicitud_"+w)+"").equals(ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+"")?ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialHosp:ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialUrg;
				
				//VALORACIONES INICIALES
				consulta+="(";
				consulta+=" SELECT " +
								"to_char(v.fecha_valoracion, 'DD/MM/YYYY') as fecha, " +
								"v.fecha_valoracion||'' as fechabd, " +
								"substr(v.hora_valoracion,1,5) as hora, " +
								"s.numero_solicitud as codigopk, " +
								""+tipoEvolucionEpicrisis+" as codigotipoevolucion, " +
								"'"+valoracionesInicialesAmostrar.get("titulovaloracion_"+w)+"'"+castTexto+" as nombretipoevolucion, " +
								//"si.epicrisis as yaenviadoepicrisis, " +
								"'"+ConstantesBD.acronimoNo+"' as yaenviadoepicrisis, " +
								"'"+ConstantesBD.acronimoNo+"' as enviarepicrisis " +
							"FROM " +
								"solicitudes s	" +
								"INNER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud)  " +
							"WHERE  " +
								"s.numero_solicitud = "+valoracionesInicialesAmostrar.get("numerosolicitud_"+w);
				consulta+=") ";
				esPrimero=false;
				
			}
		}
		
		//segundo hacemos el select de las evoluciones
		for(int w=0; w<Utilidades.convertirAEntero(criteriosBusquedaMap.get("numRegistros")+""); w++)
		{
			logger.info("codigo tipo evol-->"+criteriosBusquedaMap.get("codigotipoevolucion_"+w)+" sel->"+criteriosBusquedaMap.get("seleccionado_"+w));
			if(UtilidadTexto.getBoolean( criteriosBusquedaMap.get("seleccionado_"+w)+""))
			{
				logger.info("entra");
				if(!esPrimero)
					consulta+=" UNION ";
				
				//PRIMERO REGISTRO DE EVOLUCIONES
				if(Utilidades.convertirAEntero(criteriosBusquedaMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisRegistroEvoluciones)
				{
					consulta+="(";
					consulta+=" SELECT " +
									"to_char(e.fecha_evolucion, 'DD/MM/YYYY') as fecha, " +
									"e.fecha_evolucion||'' as fechabd, " +
									"substr(e.hora_evolucion||'', 1,5) as hora, " +
									"e.codigo as codigopk, " +
									""+criteriosBusquedaMap.get("codigotipoevolucion_"+w)+" as codigotipoevolucion, " +
									"'"+criteriosBusquedaMap.get("nombretipoevolucion_"+w)+"'"+castTexto+" as nombretipoevolucion, " +
									//al menos una seccion es enviada desde la evolucion
									"'"+ConstantesBD.acronimoSi+"' as yaenviadoepicrisis, " +
									"'"+ConstantesBD.acronimoNo+"' as enviarepicrisis " +
								"FROM " +
									"evoluciones e " +
									"INNER JOIN solicitudes s ON(s.numero_solicitud=e.valoracion) " +
								"WHERE  " +
									"s.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
									"and e.fecha_evolucion between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' " +
									"and s.estado_historia_clinica <> "+ConstantesBD.codigoEstadoHCAnulada+" " +
									//no podemos cargar la evolucion que genero el egreso
									"and e.codigo NOT IN (SELECT eg.evolucion FROM egresos eg WHERE eg.cuenta=s.cuenta and eg.codigo_medico is not null and eg.destino_salida in("+ConstantesBD.codigoDestinoSalidaDadoDeAlta+", "+ConstantesBD.codigoDestinoSalidaRemitidoOtroNivelComplejidad+", "+ConstantesBD.codigoDestinoSalidaVoluntaria+") ) ";
					consulta+=") ";
					esPrimero=false;
				}
				
				//SEGUNDO EVENTOS ADVERSOS ADMINISTRATIVOS
				if(Utilidades.convertirAEntero(criteriosBusquedaMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisEventosAdversosAdministrativos)
				{
					consulta+="(";
					consulta+=" SELECT " +
									"to_char(r.fecha_registro, 'DD/MM/YYYY') as fecha, " +
									"r.fecha_registro||'' as fechabd, " +
									"substr(r.hora_registro||'', 1,5) as hora,  " +
									"r.codigo as codigopk, " +
									""+criteriosBusquedaMap.get("codigotipoevolucion_"+w)+" as codigotipoevolucion, " +
									"'"+criteriosBusquedaMap.get("nombretipoevolucion_"+w)+"'"+castTexto+" as nombretipoevolucion, " +
									"r.epicrisis as yaenviadoepicrisis, " +
									"'"+ConstantesBD.acronimoNo+"' as enviarepicrisis " +
								"FROM " +
									"registro_eventos_adversos r " +
									"INNER JOIN eventos_adversos e ON(e.codigopk= r.evento_adverso) " +
								"WHERE " +
									"r.ingreso="+ingreso+" " +
									"and r.activo='"+ConstantesBD.acronimoSi+"' " +
									"and r.fecha_registro between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' " +
									"and e.tipo='"+ConstantesIntegridadDominio.acronimoTipoEventoAdministrativo+"' ";
					consulta+=") ";
					esPrimero=false;
				}
				
				//TERCERO EVENTOS ADVERSOS ASISTENCIALES
				if(Utilidades.convertirAEntero(criteriosBusquedaMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisEventosAdversosAsistenciales)
				{
					consulta+="(";
					consulta+=" SELECT " +
									"to_char(r.fecha_registro, 'DD/MM/YYYY') as fecha, " +
									"r.fecha_registro||'' as fechabd, "+
									"substr(r.hora_registro||'', 1,5) as hora,  " +
									"r.codigo as codigopk, " +
									""+criteriosBusquedaMap.get("codigotipoevolucion_"+w)+" as codigotipoevolucion, " +
									"'"+criteriosBusquedaMap.get("nombretipoevolucion_"+w)+"'"+castTexto+" as nombretipoevolucion, " +
									"r.epicrisis as yaenviadoepicrisis, " +
									"'"+ConstantesBD.acronimoNo+"' as enviarepicrisis " +
								"FROM " +
									"registro_eventos_adversos r " +
									"INNER JOIN eventos_adversos e ON(e.codigopk= r.evento_adverso) " +
								"WHERE " +
									"r.ingreso="+ingreso+" " +
									"and r.activo='"+ConstantesBD.acronimoSi+"' " +
									"and r.fecha_registro between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' " +
									"and e.tipo='"+ConstantesIntegridadDominio.acronimoTipoEventoAsistencial+"' ";
					consulta+=") ";
					esPrimero=false;
				}
				
				//CUARTO PROCEDIMIENTOS
				if(Utilidades.convertirAEntero(criteriosBusquedaMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisProcedimientos)
				{
					consulta+="(";
					consulta+=" SELECT " +
									"to_char(r.fecha_ejecucion, 'DD/MM/YYYY') as fecha, " +
									"r.fecha_ejecucion||'' as fechabd, " +
									"substr(r.hora_ejecucion||'', 1,5) as hora, " +
									"s.numero_solicitud as codigopk, " +
									""+criteriosBusquedaMap.get("codigotipoevolucion_"+w)+" as codigotipoevolucion, " +
									"'"+criteriosBusquedaMap.get("nombretipoevolucion_"+w)+"'"+castTexto+" as nombretipoevolucion, " +
									"sp.epicrisis as yaenviadoepicrisis, " +
									"'"+ConstantesBD.acronimoNo+"' as enviarepicrisis " +
								"FROM " +
									"res_sol_proc r " +
									"INNER JOIN sol_procedimientos sp ON(sp.numero_solicitud=r.numero_solicitud) " +
									"INNER JOIN solicitudes s ON(s.numero_solicitud=r.numero_solicitud) " +
								"WHERE  " +
									"s.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
									"and r.fecha_ejecucion between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' "+
									"and s.estado_historia_clinica <> "+ConstantesBD.codigoEstadoHCAnulada+" ";
					consulta+=") ";
					esPrimero=false;
				}
				
				//QUINTO MEDICAMENTOS
				if(Utilidades.convertirAEntero(criteriosBusquedaMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisMedicamentos)
				{
					consulta+="(";
					consulta+=" SELECT " +
									"to_char(a.fecha_grabacion, 'DD/MM/YYYY') as fecha, " +
									"a.fecha_grabacion||'' as fechabd, " +
									"substr(a.hora_grabacion||'', 1, 5) as hora, " +
									"a.codigo as codigopk, " +
									""+criteriosBusquedaMap.get("codigotipoevolucion_"+w)+" as codigotipoevolucion, " +
									"'"+criteriosBusquedaMap.get("nombretipoevolucion_"+w)+"'"+castTexto+" as nombretipoevolucion, " +
									"a.epicrisis as yaenviadoepicrisis, " +
									"'"+ConstantesBD.acronimoNo+"' as enviarepicrisis " +
								"from " +
									"admin_medicamentos a " +
									"INNER JOIN solicitudes s ON(s.numero_solicitud=a.numero_solicitud) " +
								"WHERE  " +
									"s.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
									"and a.fecha_grabacion between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' "+
									"and s.estado_historia_clinica <> "+ConstantesBD.codigoEstadoHCAnulada+" ";
					consulta+=") ";
					esPrimero=false;
				}
				
				//SEXTO SOLICITUDES CX
				if(Utilidades.convertirAEntero(criteriosBusquedaMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisCirugia)
				{
					consulta+="(";
					consulta+=" SELECT " +
									"to_char(sc.fecha_inicial_cx, 'DD/MM/YYYY') as fecha, " +
									"sc.fecha_inicial_cx||'' as fechabd, " +
									"substr(sc.hora_inicial_cx||'', 1,5) as hora, " +
									"sc.numero_solicitud as codigopk, " +
									""+criteriosBusquedaMap.get("codigotipoevolucion_"+w)+" as codigotipoevolucion, " +
									"'"+criteriosBusquedaMap.get("nombretipoevolucion_"+w)+"'"+castTexto+" as nombretipoevolucion, " +
									//siempre se envia la descripcion qx
									"'"+ConstantesBD.acronimoSi+"' as yaenviadoepicrisis, " +
									"'"+ConstantesBD.acronimoNo+"' as enviarepicrisis " +
								"FROM " +
									"solicitudes_cirugia sc " +
									"INNER JOIN solicitudes s ON(s.numero_solicitud=sc.numero_solicitud) " +
								"WHERE  " +
									"s.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
									"and sc.fecha_inicial_cx between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' "+
									"and s.estado_historia_clinica in( "+ConstantesBD.codigoEstadoHCInterpretada+", "+ConstantesBD.codigoEstadoHCRespondida+" ) ";
					consulta+=") ";
					esPrimero=false;
				}
				
				//SEPTIMO INTERCONSULTAS
				if(Utilidades.convertirAEntero(criteriosBusquedaMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisInterconsulta)
				{
					consulta+="(";
					consulta+=" SELECT " +
									"to_char(v.fecha_valoracion, 'DD/MM/YYYY') as fecha, " +
									"v.fecha_valoracion||'' as fechabd, " +
									"substr(v.hora_valoracion,1,5) as hora, " +
									"si.numero_solicitud as codigopk, " +
									""+criteriosBusquedaMap.get("codigotipoevolucion_"+w)+" as codigotipoevolucion, " +
									"'"+criteriosBusquedaMap.get("nombretipoevolucion_"+w)+"'"+castTexto+" as nombretipoevolucion, " +
									//"si.epicrisis as yaenviadoepicrisis, " +
									"'"+ConstantesBD.acronimoNo+"' as yaenviadoepicrisis, " +
									"'"+ConstantesBD.acronimoNo+"' as enviarepicrisis " +
								"FROM " +
									"solicitudes_inter si	" +
									"INNER JOIN solicitudes s ON(s.numero_solicitud=si.numero_solicitud) " +
									"INNER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud)  " +
								"WHERE  " +
									"s.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
									"and s.fecha_solicitud between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' "+
									"and s.estado_historia_clinica in( "+ConstantesBD.codigoEstadoHCInterpretada+", "+ConstantesBD.codigoEstadoHCRespondida+" ) ";
					consulta+=") ";
					esPrimero=false;
				}
				
				//OCTAVO CUIDADOS ESPECIALES
				if(Utilidades.convertirAEntero(criteriosBusquedaMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisCuidadosEspeciales)
				{
					logger.info("entra cuidado especial");
					consulta+="(";
					consulta+=" SELECT " +
									"to_char(v.fecha_valoracion, 'DD/MM/YYYY') as fecha, " +
									"v.fecha_valoracion||'' as fechabd, " +
									"substr(v.hora_valoracion,1,5) as hora, " +
									"s.numero_solicitud as codigopk, " +
									""+criteriosBusquedaMap.get("codigotipoevolucion_"+w)+" as codigotipoevolucion, " +
									"'"+criteriosBusquedaMap.get("nombretipoevolucion_"+w)+"'"+castTexto+" as nombretipoevolucion, " +
									"'"+ConstantesBD.acronimoNo+"' as yaenviadoepicrisis, " +
									"'"+ConstantesBD.acronimoNo+"' as enviarepicrisis " +
								"FROM " +
									"solicitudes s  " +
									"INNER JOIN valoraciones v ON(s.numero_solicitud=v.numero_solicitud) " +
									"INNER JOIN ingresos_cuidados_especiales i ON(i.valoracion=s.numero_solicitud) " +
								"WHERE " +
									"s.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
									"and s.fecha_solicitud between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' "+
									"and s.estado_historia_clinica in( "+ConstantesBD.codigoEstadoHCInterpretada+", "+ConstantesBD.codigoEstadoHCRespondida+" ) "+
									"and v.cuidado_especial='"+ConstantesBD.acronimoSi+"' "+
//									con esto identifico que no es una valoracion inicial 
									//"and i.valoracion<>i.valoracion_orden " +-------------->SE REEMPLAZA POR ESTE ENUNCIADO DE PAULA 2008-08-01
									//segun acuerdo con Paula nunca se considera valoracion inicial a los cuidados especiales cuando existe previamente una valoracion de hospitalizacion 
									"and (" +
											"(" +
												"i.valoracion=i.valoracion_orden " +
												"AND (" +
														"select count(1) " +
														"from " +
															"solicitudes s1 " +
															"inner join valoraciones v1 on(v1.numero_solicitud=s1.numero_solicitud) " +
														"where " +
															"s1.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentas, false)+") " +
															"and s1.fecha_solicitud between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechainicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechafinal")+"")+"' "+
															"and s1.estado_historia_clinica in( "+ConstantesBD.codigoEstadoHCInterpretada+", "+ConstantesBD.codigoEstadoHCRespondida+" ) "+
															"and s1.tipo ="+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+" " +
															"and v1.cuidado_especial='"+ConstantesBD.acronimoNo+"' " +
													")>0 " +
											") OR i.valoracion<>i.valoracion_orden " +
										") ";
					
					
					consulta+=") ";
					esPrimero=false;
				}
			}
		}
		
		consulta+=") tabla order by ";
		if(UtilidadTexto.getBoolean( criteriosBusquedaMap.get("presentacionxfechahora")+""))
			consulta+=" tabla.fechabd asc, tabla.hora asc ";
		else
			consulta+=" tabla.nombretipoevolucion, tabla.fechabd asc, tabla.hora asc ";
		
		logger.info("\n obtenerFechaHoraSolicitudes->"+consulta+" \n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			//cargamos las secciones de las valoraciones iniciales
			mapa= cargarSeccionesValoracionesInicialesEnviadasEpicrisis(con, mapa, Utilidades.convertirAEntero(criteriosBusquedaMap.get("institucion")+""), order);
			
			//luego cargamos el enviar epicrisis de las secciones de la cx
			mapa= cargarSeccionesCxEnviadasEpicrisis(con, mapa);
			
			//luego de esto cargamos el enviar a epicrisis de cada una de las subsecciones de la evolucion
			mapa= cargarSeccionesEvolucionEnviadasEpicrisis(con, mapa, Utilidades.convertirAEntero(criteriosBusquedaMap.get("institucion")+""), order);
			
			//cargamos las secciones de interconsultas
			mapa= cargarSeccionesInterconsultasEnviadasEpicrisis(con, mapa, Utilidades.convertirAEntero(criteriosBusquedaMap.get("institucion")+""), order);
			
			//cargamos las secciones de cuidados especiales
			mapa= cargarSeccionesCuidadosEspecialesEnviadasEpicrisis(con, mapa, Utilidades.convertirAEntero(criteriosBusquedaMap.get("institucion")+""), order);
			
			logger.info("\n\n************************MAPA->"+mapa);
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		
		return mapa;
	}
	
	//////**********************************************************PARTE DE CX/*****************************************************************
	//////***************************************************************************************************************************************
	//////***************************************************************************************************************************************
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	private static HashMap<Object, Object> cargarSeccionesCxEnviadasEpicrisis(Connection con, HashMap<Object, Object> fechasHorasMap) 
	{
		for(int w=0; w<Utilidades.convertirAEntero(fechasHorasMap.get("numRegistros")+""); w++)
		{
			if(Utilidades.convertirAEntero(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisCirugia)
			{
				fechasHorasMap= cargarServiciosCxEnviadosEpicrisis(con, Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+""), fechasHorasMap);
				
				//siempre se envia la descripcion qx
				fechasHorasMap.put("yaenviadadescripcionqx_"+w, ConstantesBD.acronimoSi);
				fechasHorasMap.put("enviardescripcionqx_"+w, ConstantesBD.acronimoNo);
				
				//siempre se envia la justificacion no pos
				//fechasHorasMap.put("yaenviadajusqx_"+w, ConstantesBD.acronimoSi);
				//fechasHorasMap.put("enviarjusqx_"+w, ConstantesBD.acronimoNo);
				
				fechasHorasMap= cargarSalidaEnviadaEpicrisis(con, Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+""), fechasHorasMap);
				fechasHorasMap= cargarNotasEnfermeriaEpicrisis(con, Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+""), fechasHorasMap);
				fechasHorasMap= cargarNotasRecuperacionEpicrisis(con, Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+""), fechasHorasMap);
			}
		}
		return fechasHorasMap;
	}

	/**
	 * 
	 * @param con
	 * @param i
	 * @param fechasHorasMap
	 * @return
	 */
	private static HashMap<Object, Object> cargarNotasRecuperacionEpicrisis(Connection con, int numeroSolicitud, HashMap<Object, Object> fechasHorasMap) 
	{
		String consulta=" SELECT DISTINCT " +
							"to_char(dnr.fecha_recuperacion,'DD/MM/YYYY') as fecha, " +
							"to_char(dnr.hora_recuperacion,'HH24:MI') as hora,  " +
							"epicrisis as epicrisis " +
						"FROM " +
							"det_notas_recuperacion dnr " +
							"INNER JOIN usuarios u ON ( u.codigo_persona = dnr.codigo_enfermera ) " +
							"INNER JOIN medicos m ON ( m.codigo_medico = dnr.codigo_enfermera )  " +
						"WHERE " +
							"dnr.numero_solicitud =? AND dnr.valor <> '' "+order;
		
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs = null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, numeroSolicitud);
			rs=new ResultSetDecorator(ps.executeQuery());
			
			int contador=0;
			while(rs.next())
			{
				fechasHorasMap.put("yaenviadanotarecuperacion_"+numeroSolicitud+"_"+contador, rs.getString("epicrisis"));
				fechasHorasMap.put("enviarnotarecuperacion_"+numeroSolicitud+"_"+contador, ConstantesBD.acronimoNo);
				contador++;
			}
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		
		return fechasHorasMap;
	}

	/**
	 * 
	 * @param con
	 * @param i
	 * @param fechasHorasMap
	 * @return
	 */
	private static HashMap<Object, Object> cargarNotasEnfermeriaEpicrisis(Connection con, int numeroSolicitud, HashMap<Object, Object> fechasHorasMap) 
	{
		String consulta="SELECT " +
							"codigo as codigo, " +
							"epicrisis as yaenviadanotaenfermeria "+
						"FROM " +
							"notas_enfermeria ne "+
						"WHERE " +
							"ne.numero_solicitud=? "+
						"ORDER BY ne.fecha_nota, ne.hora_nota ";
		
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs = null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, numeroSolicitud);
			rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				fechasHorasMap.put("yaenviadanotaenfermeria_"+numeroSolicitud+"_"+rs.getInt("codigo"), rs.getString("yaenviadanotaenfermeria"));
				fechasHorasMap.put("enviarnotaenfermeria_"+numeroSolicitud+"_"+rs.getInt("codigo"), ConstantesBD.acronimoNo);
			}
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		
		return fechasHorasMap;
	}

	/**
	 * 
	 * @param con
	 * @param i
	 * @param fechasHorasMap
	 * @return
	 */
	private static HashMap<Object, Object> cargarSalidaEnviadaEpicrisis(Connection con, int numeroSolicitud, HashMap<Object, Object> fechasHorasMap) 
	{
		String consulta="SELECT epicrisis_salida_pac FROM solicitudes_cirugia where numero_solicitud=?";
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, numeroSolicitud);
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				fechasHorasMap.put("yaenviadasalidasala_"+numeroSolicitud, rs.getString("epicrisis_salida_pac"));
				fechasHorasMap.put("enviarsalidasala_"+numeroSolicitud, ConstantesBD.acronimoNo);
			}
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		
		return fechasHorasMap;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	private static HashMap<Object, Object> cargarServiciosCxEnviadosEpicrisis(Connection con, int numeroSolicitud, HashMap<Object, Object> fechasHorasMap)
	{
		String consulta=" SELECT " +
							"s.servicio as servicio, " +
							"s.epicrisis as yaenviadoserviciocxepicrisis "+
						"FROM " +
							"sol_cirugia_por_servicio s " +
						"WHERE " +
							"s.numero_solicitud=? order by s.consecutivo ";
		
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, numeroSolicitud);
			rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				fechasHorasMap.put("yaenviadoserviciocxepicrisis_"+numeroSolicitud+"_"+rs.getInt("servicio"), rs.getString("yaenviadoserviciocxepicrisis"));
				fechasHorasMap.put("enviarserviciocxepicrisis_"+numeroSolicitud+"_"+rs.getInt("servicio"), ConstantesBD.acronimoNo);
			}
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		
		return fechasHorasMap;
	}
	
	//////**********************************************************PARTE DE EVOLUCIONES/*****************************************************************
	//////***************************************************************************************************************************************
	//////***************************************************************************************************************************************
	
	
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	private static HashMap<Object, Object> cargarSeccionesEvolucionEnviadasEpicrisis(Connection con, HashMap<Object, Object> fechasHorasMap, int institucion, String order) 
	{
		for(int w=0; w<Utilidades.convertirAEntero(fechasHorasMap.get("numRegistros")+""); w++)
		{
			if(Utilidades.convertirAEntero(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisRegistroEvoluciones)
			{
				fechasHorasMap= cargarSeccionesEvolucionesEnviadasEpicrisis(con, Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+""), institucion, fechasHorasMap, w, order);
			}
		}
		return fechasHorasMap;
	}	
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param institucion
	 * @param fechasHorasMap
	 * @return
	 */
	private static HashMap<Object, Object> cargarSeccionesEvolucionesEnviadasEpicrisis(Connection con, int evolucion, int institucion, HashMap<Object, Object> fechasHorasMap, int indice, String order)
	{
		String consulta="";
		
		int plantilla=Plantillas.obtenerCodigoPlantillaXEvolucion(con, evolucion);
		
		//primero se trata de colocar los check de las funcionalidades que tienen una plantilla asociada
		consulta=	"(" +
						"SELECT " +
							"psf.codigo_pk as codigo, " +
							"getEnvEpicrisisSeccion("+evolucion+", p.fun_param, psf.codigo_pk, psf.fun_param_secciones_fijas) as yaenviadaseccion, " +
							"'"+ConstantesBD.acronimoNo+"' as enviarseccion " +
						"FROM " +
							"plantillas p " +
							"INNER JOIN plantillas_sec_fijas psf ON(psf.plantilla=p.codigo_pk) " +
						"WHERE " +
							"p.codigo_pk="+plantilla+" "+order+ //@todo_oracle, no funciona en oracle ->"order by psf.orden " +
					")" +
					"UNION "+
					"(" +
						"SELECT " +
							"fpsf.codigo_pk as codigo, " +
							"getEnvEpicrisisSeccion("+evolucion+", fpsf.fun_param, 0, fpsf.codigo_pk) as yaenviadaseccion, " +
							"'"+ConstantesBD.acronimoNo+"' as enviarseccion " +
						"FROM " +
							"fun_param_secciones_fijas fpsf " +
						"where " +
							"fpsf.fun_param="+ConstantesCamposParametrizables.funcParametrizableEvolucion+" " +
							"and fpsf.institucion= "+institucion+" " +
							"and fpsf.codigo_pk not in (select fun_param_secciones_fijas from plantillas_sec_fijas where plantilla="+plantilla+" )" +
					")";
		
		
		logger.info("\n cargarSeccionesEvolucionesEnviadasEpicrisis-->"+consulta+"\n");
		
		
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				fechasHorasMap.put("yaenviadaseccion_"+indice+"_"+rs.getString(1), rs.getString(2));
				fechasHorasMap.put("enviarseccion_"+indice+"_"+rs.getString(1), rs.getString(3));
			}
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		return fechasHorasMap;
	}
	
	//////**********************************************************PARTE DE VALORACIONES INICIALES/*****************************************************************
	//////***************************************************************************************************************************************
	//////***************************************************************************************************************************************
	
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	private static HashMap<Object, Object> cargarSeccionesValoracionesInicialesEnviadasEpicrisis(Connection con, HashMap<Object, Object> fechasHorasMap, int institucion, String order) 
	{
		for(int w=0; w<Utilidades.convertirAEntero(fechasHorasMap.get("numRegistros")+""); w++)
		{
			if(Utilidades.convertirAEntero(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialHosp
			 	|| Utilidades.convertirAEntero(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialUrg)
			{
				fechasHorasMap= cargarSeccionesValoracionesEnviadasEpicrisis(con, Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+""), institucion, fechasHorasMap, w, order);
			}
		}
		return fechasHorasMap;
	}
	
	
	
	
	//////**********************************************************PARTE DE INTERCONSULTAS/*****************************************************************
	//////***************************************************************************************************************************************
	//////***************************************************************************************************************************************
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	private static HashMap<Object, Object> cargarSeccionesInterconsultasEnviadasEpicrisis(Connection con, HashMap<Object, Object> fechasHorasMap, int institucion, String order) 
	{
		for(int w=0; w<Utilidades.convertirAEntero(fechasHorasMap.get("numRegistros")+""); w++)
		{
			if(Utilidades.convertirAEntero(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisInterconsulta)
			{
				fechasHorasMap= cargarSeccionesValoracionesEnviadasEpicrisis(con, Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+""), institucion, fechasHorasMap, w, order);
				fechasHorasMap.put("enviarinterpretacion_"+w, ConstantesBD.acronimoNo);
			}
		}
		return fechasHorasMap;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param institucion
	 * @param fechasHorasMap
	 * @return
	 */
	private static HashMap<Object, Object> cargarSeccionesValoracionesEnviadasEpicrisis(Connection con, int numeroSolicitud, int institucion, HashMap<Object, Object> fechasHorasMap, int indice, String order)
	{
		String consulta="";
		
		int plantilla=Plantillas.obtenerCodigoPlantillaXIngreso(con, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, numeroSolicitud);
		
		//primero se trata de colocar los check de las funcionalidades que tienen una plantilla asociada
		consulta=	"(" +
						"SELECT " +
							"psf.codigo_pk as codigo, " +
							"getEnvEpicrisisSeccion("+numeroSolicitud+", p.fun_param, psf.codigo_pk, psf.fun_param_secciones_fijas) as yaenviadaseccion, " +
							"'"+ConstantesBD.acronimoNo+"' as enviarseccion " +
						"FROM " +
							"plantillas p " +
							"INNER JOIN plantillas_sec_fijas psf ON(psf.plantilla=p.codigo_pk) " +
						"WHERE " +
							"p.codigo_pk="+plantilla+" "+order+" "+ //@todo_oracle order by psf.orden " +//no funciona para oracle
					")" +
					"UNION "+
					"(" +
						"SELECT " +
							"fpsf.codigo_pk as codigo, " +
							"getEnvEpicrisisSeccion("+numeroSolicitud+", fpsf.fun_param, 0, fpsf.codigo_pk) as yaenviadaseccion, " +
							"'"+ConstantesBD.acronimoNo+"' as enviarseccion " +
						"FROM " +
							"fun_param_secciones_fijas fpsf " +
						"where " +
							"fpsf.fun_param="+obtenerCodigoFunParam(con, numeroSolicitud)+" " +
							"and fpsf.institucion= "+institucion+" " +
							"and fpsf.codigo_pk not in (select coalesce(fun_param_secciones_fijas,"+ConstantesBD.codigoNuncaValido+") from plantillas_sec_fijas where plantilla="+plantilla+" )" +
					")";
		
		
		logger.info("\n cargarSeccionesValoracionesEnviadasEpicrisis-->"+consulta+"\n");
		
		
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs = null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				fechasHorasMap.put("yaenviadaseccion_"+indice+"_"+rs.getString(1), rs.getString(2));
				fechasHorasMap.put("enviarseccion_"+indice+"_"+rs.getString(1), rs.getString(3));
			}
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		return fechasHorasMap;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	private static int obtenerCodigoFunParam(Connection con, int numeroSolicitud)
	{
		String tipoSolicitud=Utilidades.obtenerCodigoTipoSolicitud(con, numeroSolicitud+"");
		if(tipoSolicitud.equals(ConstantesBD.codigoTipoSolicitudInicialUrgencias+""))
			return ConstantesCamposParametrizables.funcParametrizableValoracionUrgencias;
		else if(tipoSolicitud.equals(ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+""))
			return ConstantesCamposParametrizables.funcParametrizableValoracionHospitalizacion;
		else if(tipoSolicitud.equals(ConstantesBD.codigoTipoSolicitudInterconsulta+""))
			return ConstantesCamposParametrizables.funcParametrizableValoracionInterconsulta;
		else
			return ConstantesBD.codigoNuncaValido;

	}
	
	//////**********************************************************PARTE DE CUIDADOS ESPECIALES/*****************************************************************
	//////***************************************************************************************************************************************
	//////***************************************************************************************************************************************
	
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	private static HashMap<Object, Object> cargarSeccionesCuidadosEspecialesEnviadasEpicrisis(Connection con, HashMap<Object, Object> fechasHorasMap, int institucion, String order) 
	{
		for(int w=0; w<Utilidades.convertirAEntero(fechasHorasMap.get("numRegistros")+""); w++)
		{
			if(Utilidades.convertirAEntero(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisCuidadosEspeciales)
			{
				fechasHorasMap= cargarSeccionesValoracionesEnviadasEpicrisis(con, Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+""), institucion, fechasHorasMap, w, order);
			}
		}
		return fechasHorasMap;
	}

	//////**********************************************************PARTE DE DETALLE EPICRISIS/*****************************************************************
	//////***************************************************************************************************************************************
	//////***************************************************************************************************************************************
	
	
	
	/**
	 * Metodo que carga el detalle de la epicrisis, es un mapa que contiene los dto de epicrisis dependiendo del tipo de evolucion - solicitud dada,
	 * para optimizar recursos siempre se carga los detalles que esten dentro del rango del paginador (metodologia JIT).
	 * @param con
	 * @param fechasHorasMap
	 * @param indiceInicialPager
	 * @param indiceFinalPager
	 * @return
	 */
	public static HashMap<Object, Object> cargarDetalleEpicrisis(Connection con, HashMap<Object, Object> fechasHorasMap, int indiceInicialPager, int indiceFinalPager, UsuarioBasico usuario, PersonaBasica paciente)
	{
		HashMap<Object, Object> detalleMap= new HashMap<Object, Object>();
		detalleMap.put("numRegistros", "0");
		for(int w=indiceInicialPager; (w<Utilidades.convertirAEntero(fechasHorasMap.get("numRegistros")+"") && w<indiceFinalPager); w++)
		{
			logger.info("DETALLE-->"+w+" tipo evol->"+fechasHorasMap.get("codigotipoevolucion_"+w));
			if(Utilidades.convertirAEntero(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisRegistroEvoluciones)
			{
				DtoEvolucionEpicrisis dtoEvolucionEpicrisis= new DtoEvolucionEpicrisis();
				dtoEvolucionEpicrisis.setDtoEvolucion(Epicrisis1Evoluciones.cargarMundoEvolucion(Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+""), con));
				dtoEvolucionEpicrisis.setDtoPlantilla(Epicrisis1Evoluciones.cargarPlantillaEvolucion(con, usuario.getCodigoInstitucionInt(), Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+"")));
				detalleMap.put("DETALLE_"+w, dtoEvolucionEpicrisis);
			}
			else if(Utilidades.convertirAEntero(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisEventosAdversosAdministrativos
					|| Utilidades.convertirAEntero(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisEventosAdversosAsistenciales)
			{
				detalleMap.put("DETALLE_"+w, cargarEventoAdversoEpicrisis(con, Double.parseDouble(fechasHorasMap.get("codigopk_"+w)+"")));
			}
			else if(Utilidades.convertirAEntero(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisProcedimientos)
			{
				detalleMap.put("DETALLE_"+w, cargarProcedimientoEpicrisis(con, Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+""), usuario.getCodigoInstitucionInt()));
			}
			else if(Utilidades.convertirAEntero(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisMedicamentos)
			{
				detalleMap.put("DETALLE_"+w, cargarMedicamentosAdmin(con, Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+""), usuario.getCodigoInstitucionInt()));
			}
			else if(Utilidades.convertirAEntero(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisCirugia)
			{
				detalleMap.put("DETALLE_"+w, cargarCirugiaEpicrisis(con, Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+""), usuario.getCodigoInstitucionInt()));
			}
			else if(Utilidades.convertirAEntero(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisInterconsulta)
			{
				DtoValoracionEpicrisis dtoValoracionEpicrisis= new DtoValoracionEpicrisis();
				dtoValoracionEpicrisis.setDtoPlantilla(Epicrisis1Valoraciones.cargarPlantillaValoracion(con, usuario.getCodigoInstitucionInt(), ConstantesCamposParametrizables.funcParametrizableValoracionInterconsulta, Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+"")));
				dtoValoracionEpicrisis.setDtoValoracion(Epicrisis1Valoraciones.cargarMundoValInterconsulta(dtoValoracionEpicrisis.getDtoPlantilla(),  Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+""), con, paciente));
				dtoValoracionEpicrisis.setDtoInterpretacion(cargarInterpretacionSolicitud(con, Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+"")));
				dtoValoracionEpicrisis.setJustificacionNoPos(Epicrisis1.cargarInfoJusNoPos(con, Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+""), false, usuario.getCodigoInstitucionInt(), false, ConstantesBD.codigoNuncaValido));
				detalleMap.put("DETALLE_"+w, dtoValoracionEpicrisis);
			}
			else if(Utilidades.convertirAEntero(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisCuidadosEspeciales
					|| Utilidades.convertirAEntero(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialHosp)
			{
				DtoValoracionHospitalizacionEpicrisis dtoValoracionEpicrisis= new DtoValoracionHospitalizacionEpicrisis();
				dtoValoracionEpicrisis.setDtoPlantilla(Epicrisis1Valoraciones.cargarPlantillaValoracion(con, usuario.getCodigoInstitucionInt(), ConstantesCamposParametrizables.funcParametrizableValoracionHospitalizacion, Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+"")));
				dtoValoracionEpicrisis.setDtoValHospitalizacion(Epicrisis1Valoraciones.cargarMundoValHospitalizacion(dtoValoracionEpicrisis.getDtoPlantilla(),  Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+""), con, usuario, paciente));
				detalleMap.put("DETALLE_"+w, dtoValoracionEpicrisis);
			}
			else if(Utilidades.convertirAEntero(fechasHorasMap.get("codigotipoevolucion_"+w)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialUrg)
			{
				DtoValoracionUrgenciasEpicrisis dtoValoracionEpicrisis= new DtoValoracionUrgenciasEpicrisis();
				dtoValoracionEpicrisis.setDtoPlantilla(Epicrisis1Valoraciones.cargarPlantillaValoracion(con, usuario.getCodigoInstitucionInt(), ConstantesCamposParametrizables.funcParametrizableValoracionUrgencias, Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+"")));
				dtoValoracionEpicrisis.setDtoValUrgencias(Epicrisis1Valoraciones.cargarMundoValUrgencias(dtoValoracionEpicrisis.getDtoPlantilla(), Utilidades.convertirAEntero(fechasHorasMap.get("codigopk_"+w)+""), con, usuario, paciente));
				detalleMap.put("DETALLE_"+w, dtoValoracionEpicrisis);
			}
		}
		return detalleMap;
	}

	
	/**
	 * 
	 * @param con
	 * @param i
	 * @return
	 */
	private static DtoEventoAdversoEpicrisis cargarEventoAdversoEpicrisis(Connection con, double codigoPkRegEvento) 
	{
		DtoEventoAdversoEpicrisis dto= new DtoEventoAdversoEpicrisis();
		String consulta="SELECT " +
							"to_char(r.fecha_registro, 'DD/MM/YYYY') as fecha, " +
							"substr(r.hora_registro, 1, 5) as hora, " +
							"getnombreusuario(r.usuario_modifica) as usuario, " +
							"coalesce(r.gestionado, '"+ConstantesBD.acronimoNo+"') as gestionado, " +
							"getnomcentrocosto(r.centro_costo) as centrocosto, " +
							"e.descripcion as evento, " +
							"getintegridaddominio(e.tipo) as tipoevento, " +
							"c.nombre as clasificacion " +
						"FROM " +
							"registro_eventos_adversos r " +
							"inner join eventos_adversos e ON (r.evento_adverso=e.codigopk) " +
							"inner join clasificaciones_eventos c ON (e.clasificacion_evento=c.codigo) " +
						"where " +
							"r.codigo=? and r.activo='"+ConstantesBD.acronimoSi+"' ";
		
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setDouble(1, codigoPkRegEvento);
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				dto.setFecha(rs.getString("fecha"));
				dto.setHora(rs.getString("hora"));
				dto.setUsuario(rs.getString("usuario"));
				dto.setGestionado(rs.getString("gestionado"));
				dto.setCentroCostoPaciente(rs.getString("centrocosto"));
				dto.setEvento(rs.getString("evento"));
				dto.setTipoEvento(rs.getString("tipoEvento"));
				dto.setClasificacion(rs.getString("clasificacion"));
				dto.setObservaciones(observacionesEventosAdversos(con, codigoPkRegEvento));
			}
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		return dto;
	}
 
	/**
	 * 
	 * @param con
	 * @param codigoPkRegEvento
	 * @return
	 */
	private static String observacionesEventosAdversos(Connection con, double codigoPkRegEvento)
	{
		String consulta= "SELECT observaciones FROM observ_eventos_adversos WHERE registro_evento_adverso=? ";
		String observ="";
		
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs= null;
		
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setDouble(1, codigoPkRegEvento);
			rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				observ+=rs.getString(1)+", ";
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		return observ;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	private static DtoProcedimientosEpicrisis cargarProcedimientoEpicrisis(Connection con, int numeroSolicitud, int institucion)
	{
		DtoProcedimientosEpicrisis dto= new DtoProcedimientosEpicrisis();
		String consulta="SELECT " +
							"to_char(r.fecha_ejecucion, 'DD/MM/YYYY') as fecharespuesta, " +
							"substr(r.hora_ejecucion, 1, 5) as horarespuesta, " +
							"getdatosmedico1(s.codigo_medico_responde) as responsablerespuesta, " +
							"to_char(s.fecha_solicitud, 'DD/MM/YYYY') as fechasolicitud, " +
							"substr(s.hora_solicitud, 1,5) as horasolicitud, " +
							"getdatosmedico1(s.codigo_medico) as medicoordena, " +
							"getnombreespecialidad(s.especialidad_solicitante) as especialidadmedicoordena, " +
							"getnomcentrocosto(s.centro_costo_solicitante) as centrocostoordena, " +
							"getnomcentrocosto(s.centro_costo_solicitado) as centrocostoejecuta, " +
							"getespecialidadesmedico1(s.codigo_medico_responde, ' - ') as especialidadresponde, " +
							"getnombreservicio(sp.codigo_servicio_solicitado, "+ConstantesBD.codigoTarifarioCups+") as servicio, " +
							"coalesce(s.interpretacion,'') as interpretacion,  " +
							//true y false en cadena...............
							"case when (getSolicitudTieneIncluidos(s.numero_solicitud)>0) then 'true' else 'false' end as incluyeserviciosarticulos "+
						"FROM " +
							"solicitudes s " +
							"INNER JOIN sol_procedimientos sp ON(sp.numero_solicitud=s.numero_solicitud)  " +
							"INNER JOIN res_sol_proc r ON(s.numero_solicitud=r.numero_solicitud) " +
						"WHERE " +
							"s.numero_solicitud=? ";
		
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs= null;
		try 
		{
			logger.info("consultaProcEpi-->"+consulta+" -->"+numeroSolicitud);
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, numeroSolicitud);
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				dto.setFechaRespuesta(rs.getString("fecharespuesta"));
				dto.setHoraRespuesta(rs.getString("horarespuesta"));
				dto.setResponsableRespuesta(rs.getString("responsablerespuesta"));
				dto.setFechaOrden(rs.getString("fechasolicitud"));
				dto.setHoraOrden(rs.getString("horasolicitud"));
				dto.setResponsableOrden(rs.getString("medicoordena"));
				dto.setEspecialidadOrdena(rs.getString("especialidadmedicoordena"));
				dto.setCentroCostoOrdena(rs.getString("centrocostoordena"));
				dto.setCentroCostoEjecuta(rs.getString("centrocostoejecuta"));
				dto.setEspecialidadResponde(rs.getString("especialidadresponde"));
				dto.setServicio(rs.getString("servicio"));
				dto.setInterpretacion(rs.getString("interpretacion"));
				dto.setIncluyeServiciosArticulos(rs.getBoolean("incluyeserviciosarticulos"));
				dto.setNumeroSolicitud(numeroSolicitud);
				dto.setJustificacionNoPos(Epicrisis1.cargarInfoJusNoPos(con, numeroSolicitud, false, institucion, false, ConstantesBD.codigoNuncaValido/*codigoArticuloServicioOPCIONAL*/));
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		return dto;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoAdmin
	 * @return
	 */
	private static DtoMedicamentosAdminEpicrisis cargarMedicamentosAdmin(Connection con, int codigoAdmin, int institucion)
	{
		DtoMedicamentosAdminEpicrisis dto= new DtoMedicamentosAdminEpicrisis();
		String consulta="SELECT " +
							"to_char(a.fecha_grabacion, 'DD/MM/YYYY') as fechaadmin, " +
							"substr(a.hora_grabacion, 1, 5) as horaadmin, " +
							"getnombreusuario(a.usuario) as responsableadmin, " +
							"to_char(s.fecha_solicitud,'DD/MM/YYYY') as fechaorden, " +
							"substr(s.hora_solicitud, 1, 5) as horaorden, " +
							"getdatosmedico1(s.codigo_medico) as medicoordena, " +
							"getnombreespecialidad(s.especialidad_solicitante) as especialidadmedicoordena, " +
							"getnomcentrocosto(s.centro_costo_solicitante) as centrocostoordena, " +
							"getnomcentrocosto(s.centro_costo_solicitado) as centrocostodespacha, " +
							"getnomcentrocosto(a.centro_costo_admin) as centrocostoadmin " +
						"FROM " +
							"solicitudes s " +
							"INNER JOIN solicitudes_medicamentos sm ON (sm.numero_solicitud=s.numero_solicitud) " +
							"INNER JOIN admin_medicamentos a ON(a.numero_solicitud=s.numero_solicitud) " +
						"where " +
							"a.codigo=? ";
		
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs = null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, codigoAdmin);
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				dto.setFechaAdmin(rs.getString("fechaadmin"));
				dto.setHoraAdmin(rs.getString("horaadmin"));
				dto.setResponsableAdmin(rs.getString("responsableadmin"));
				dto.setFechaOrden(rs.getString("fechaorden"));
				dto.setHoraOrden(rs.getString("horaorden"));
				dto.setMedicoOrdena(rs.getString("medicoordena"));
				dto.setEspecialidadOrdena(rs.getString("especialidadmedicoordena"));
				dto.setCentroCostoOrdena(rs.getString("centrocostoordena"));
				dto.setCentroCostoDespacha(rs.getString("centrocostodespacha"));
				dto.setCentroCostoAdmin(rs.getString("centrocostoadmin"));
				dto.setMedicamentos(cargarDetalleAdmin(con, codigoAdmin, institucion));
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		return dto;
	}

	/**
	 * 
	 * @param con
	 * @param codigoAdmin
	 * @return
	 */
	private static ArrayList<DtoMedicamentoEpicrisis> cargarDetalleAdmin(Connection con, int codigoAdmin, int institucion) 
	{
		ArrayList<DtoMedicamentoEpicrisis> array= new ArrayList<DtoMedicamentoEpicrisis>();
		String consulta=" select " +
							"am.numero_solicitud as numsol, " +
							"da.articulo as codart, " +
							"getdescarticulo(da.articulo) as descarticulo, " +
							"coalesce(ds.dosis, '') as dosis, " +
							"coalesce(ds.frecuencia||'','') ||' '|| coalesce(ds.tipo_frecuencia, '') as frecuencia, " +
							"coalesce(ds.via, '') as via,  " +
							"coalesce(ds.dosis, '') as dosisadmin, " +
							"da.cantidad||'' as unidadesconsumidas, " +
							"da.adelanto_x_necesidad, " +
							"da.nada_via_oral, " +
							"da.usuario_rechazo " +
						"FROM " +
							"admin_medicamentos am " +
							"INNER JOIN detalle_admin da ON(da.administracion=am.codigo) " +
							"INNER JOIN detalle_solicitudes ds ON(ds.numero_solicitud=am.numero_solicitud and ds.articulo=da.articulo) " +
						"WHERE " +
							"da.administracion=? ";
		
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, codigoAdmin);
			rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoMedicamentoEpicrisis dto= new DtoMedicamentoEpicrisis();
				dto.setDescripcionMedicamento(rs.getString("descarticulo"));
				dto.setDosis(rs.getString("dosis"));
				dto.setFrecuencia(rs.getString("frecuencia"));
				dto.setVia(rs.getString("via"));
				dto.setDosisAdmin(rs.getString("dosis"));
				dto.setUnidadesConsumidas(rs.getString("unidadesconsumidas"));
				dto.setJustificacionNoPos(Epicrisis1.cargarInfoJusNoPos(con, rs.getInt("numsol"), true, institucion, false, rs.getInt("codart")));
				dto.setAdelantoXNecesidad(rs.getString("adelanto_x_necesidad"));
				dto.setNadaViaOral(rs.getString("nada_via_oral"));
				dto.setUsuarioRechazo(rs.getString("usuario_rechazo"));
				array.add(dto);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		return array;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	private static DtoCirugiaEpicrisis cargarCirugiaEpicrisis(Connection con, int numeroSolicitud, int institucion)
	{
		DtoCirugiaEpicrisis dto= new DtoCirugiaEpicrisis();
		String consulta=	"SELECT " +
								"s.numero_solicitud as numerosolicitud, " +
								"to_char(sc.fecha_inicial_cx, 'DD/MM/YYYY') as fechacirugia, " +
								"substr(sc.hora_inicial_cx, 1, 5) as horacirugia, " +
								"to_char(s.fecha_solicitud, 'DD/MM/YYYY') as fechaorden, " +
								"substr(s.hora_solicitud, 1, 5) as horaorden, " +
								"getnombreespecialidad(s.especialidad_solicitante) as especialidadmedicoordena, " +
								"getnomcentrocosto(s.centro_costo_solicitado) as centrocostosolicitado, " +
								"coalesce(sc.duracion_final_cx, '') as duracionfinalcx, " +
								"COALESCE(to_char(sc.fecha_ingreso_sala, 'DD/MM/YYYY')||'', '') as fechaingresosala, " +
								"COALESCE(substr(sc.hora_ingreso_sala, 1, 5), '') as horaingresosala, " +
								"COALESCE(to_char(sc.fecha_salida_sala, 'DD/MM/YYYY')||'', '') as fechasalidasala, " +
								"COALESCE(substr(sc.hora_salida_sala, 1, 5), '') as horasalidasala, " +
								"COALESCE(sc.salida_sala_paciente||'', '') as salidasalapaciente " +
							"FROM " +
								"solicitudes s " +
								"INNER JOIN solicitudes_cirugia sc on(sc.numero_solicitud=s.numero_solicitud) " +
							"WHERE s.numero_solicitud=? ";
		
		logger.info("\n Cx Epicrisis-->"+consulta+" -numso->"+numeroSolicitud+" \n");
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, numeroSolicitud);
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				dto.setNumeroSolicitud(rs.getInt("numerosolicitud"));
				dto.setFechaCx(rs.getString("fechacirugia"));
				dto.setHoraCx(rs.getString("horacirugia"));
				dto.setFechaSolicitud(rs.getString("fechaorden"));
				dto.setHoraSolicitud(rs.getString("horaorden"));
				dto.setEspecialidadOrdena(rs.getString("especialidadmedicoordena"));
				dto.setCentroCostoSolicitado(rs.getString("centrocostosolicitado"));
				dto.setDuracionFinalCx(rs.getString("duracionfinalcx"));
				dto.setServicios(cargarServiciosCx(con, numeroSolicitud, institucion));
				dto.setDiagnosticosPreoperatorio(cargarDxPreoperatorios(con, numeroSolicitud));
				
				//se carga la descripcion qx
				dto.setDescripcionQx( HojaQuirurgica.consultarCamposTextoHQx(con,numeroSolicitud+"", ConstantesIntegridadDominio.acronimoTipoInformacionQuirurgica));
				dto.setFechaIngresoSala(rs.getString("fechaingresosala"));
				dto.setHoraIngresoSala(rs.getString("horaingresosala"));
				dto.setFechaSalidaSala(rs.getString("fechasalidasala"));
				dto.setHoraSalidaSala(rs.getString("horasalidasala"));
				dto.setSalidaSalaPaciente(rs.getString("salidasalapaciente"));
				
				dto.setNotasEnfermeria(cargarNotasEnfermeriaEpicrisis(con, numeroSolicitud));
				dto.setNotasRecuperacion(cargarNotasRecuperacion(con, numeroSolicitud));
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		return dto;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	private static HashMap<Object, Object> cargarDxPreoperatorios(Connection con, int numeroSolicitud) 
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		mapa.put("numRegistros", "0");
		
		String consultaDx= "SELECT " +
								"dpcx.diagnostico as acronimo, " +
								"dpcx.tipo_cie AS tipoccie, " +
								"CASE WHEN dpcx.principal='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS principal, " +
								"getnombrediagnostico(dpcx.diagnostico,dpcx.tipo_cie) AS nombrediagnostico " +
							"FROM " +
								"diag_preoperatorio_cx dpcx " +
							"WHERE " +
								"dpcx.numero_solicitud=? ";
		
		logger.info("\n cargarDxpreoperatorio ->"+consultaDx+" "+numeroSolicitud+" \n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaDx));
			ps.setInt(1, numeroSolicitud);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
			
		}
		
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	private static ArrayList<DtoServiciosCirugiaEpicrisis> cargarServiciosCx(Connection con, int numeroSolicitud, int institucion) 
	{
		ArrayList<DtoServiciosCirugiaEpicrisis> array= new ArrayList<DtoServiciosCirugiaEpicrisis>();
		String consulta=" SELECT " +
							"s.codigo as codigo, " +
							"s.servicio as codigoservicio, " +
							"getnombreservicio(s.servicio,"+ConstantesBD.codigoTarifarioCups+") as servicio, " +
							"coalesce(getnombreespecialidad(s.especialidad), '') as especialidadinterviene, " +
							"getdescqxhq(s.codigo) as descqx " +
						"FROM " +
							"sol_cirugia_por_servicio s " +
						"WHERE " +
							"s.numero_solicitud=? order by s.consecutivo ";
		
		logger.info("\n cargarServiciosCx->"+consulta+ " "+numeroSolicitud);
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, numeroSolicitud);
			rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoServiciosCirugiaEpicrisis dto= new DtoServiciosCirugiaEpicrisis();
				dto.setCodigo(rs.getInt("codigo"));
				dto.setCodigoServicio(rs.getInt("codigoservicio"));
				dto.setServicio(rs.getString("servicio"));
				dto.setEspecialidadInterviene(rs.getString("especialidadinterviene"));
				dto.setDescripcionQx(rs.getString("descqx"));
				dto.setDiagnosticosPostoperatorios(cargarDxPostoperatorios(con, rs.getInt("codigo")));
				
				dto.setJustificacionNoPos(Epicrisis1.cargarInfoJusNoPos(con, numeroSolicitud, false /*esArticulo*/, institucion, false, ConstantesBD.codigoNuncaValido));
				
				array.add(dto);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		return array;
	}

	/**
	 * 
	 * @param con
	 * @param int1
	 * @return
	 */
	private static HashMap<Object, Object> cargarDxPostoperatorios(Connection con, int codigoSolCxServicio) 
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		mapa.put("numRegistros", "0");
		
		String consulta="SELECT " +
							"d.acronimo as acronimo, " +
							"ds.tipo_cie AS tipo_cie, " +
							"ds.principal AS principal, " +
							"ds.complicacion AS complicacion, " +
							"d.nombre AS nombrediagnostico " +
						"FROM " +
							"diagnosticos d " +
							"INNER JOIN diag_post_opera_sol_cx ds ON(d.acronimo=ds.diagnostico AND ds.tipo_cie=d.tipo_cie) " +
							"INNER JOIN INFORME_QX_POR_ESPECIALIDAD informe ON(ds.COD_INFORME_ESPECIALIDAD=informe.codigo) "+
							"INNER JOIN sol_cirugia_por_servicio sol on(informe.codigo=sol.COD_INFORME_ESPECIALIDAD) "+
							"WHERE sol.codigo=? ";
		
		logger.info("\n cargarDxPostoperatorios ->"+consulta+" "+codigoSolCxServicio+" \n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, codigoSolCxServicio);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	private static ArrayList<DtoNotasCirugiaEpicrisis> cargarNotasEnfermeriaEpicrisis(Connection con, int numeroSolicitud)
	{
		ArrayList<DtoNotasCirugiaEpicrisis> array= new ArrayList<DtoNotasCirugiaEpicrisis>();
		String consulta="SELECT " +
							"ne.codigo as codigo, " +
							"to_char(ne.fecha_nota,'"+ConstantesBD.formatoFechaAp +"') as fecha, " +
							"to_char(ne.hora_nota, 'HH24:MI') as hora, "+
							"ne.nota as nota, "+
							"getnombrepersona(ne.codigo_enfermera) as enfermera "+
						"FROM " +
							"notas_enfermeria ne "+
						"WHERE " +
							"ne.numero_solicitud=? "+
						"ORDER BY ne.fecha_nota, ne.hora_nota ";
		
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, numeroSolicitud);
			rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoNotasCirugiaEpicrisis dto= new DtoNotasCirugiaEpicrisis();
				dto.setCodigo(rs.getInt("codigo"));
				dto.setFecha(rs.getString("fecha"));
				dto.setHora(rs.getString("hora"));
				dto.setDescripcion(rs.getString("nota"));
				dto.setResponsable(rs.getString("enfermera"));
				array.add(dto);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		return array;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	private static ArrayList<DtoNotasCirugiaEpicrisis> cargarNotasRecuperacion(Connection con, int numeroSolicitud) 
	{
		ArrayList<DtoNotasCirugiaEpicrisis> array= new ArrayList<DtoNotasCirugiaEpicrisis>();
		String consulta=" SELECT " +
							"to_char(dnr.fecha_recuperacion,'DD/MM/YYYY') as fecha, " +
							"to_char(dnr.hora_recuperacion,'HH24:MI') as hora,  " +
							"getnombrepersona(dnr.codigo_enfermera) ||' - '|| m.numero_registro AS responsable, " +
							"getnotasrecuperacion(dnr.numero_solicitud, dnr.fecha_recuperacion, dnr.hora_recuperacion) as nota " +
						"FROM " +
							"det_notas_recuperacion dnr " +
							"INNER JOIN usuarios u ON ( u.codigo_persona = dnr.codigo_enfermera ) " +
							"INNER JOIN medicos m ON ( m.codigo_medico = dnr.codigo_enfermera )  " +
						"WHERE " +
							"dnr.numero_solicitud =? AND dnr.valor <> ''  GROUP BY 1,2,3,4 "+order;
		
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, numeroSolicitud);
			rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoNotasCirugiaEpicrisis dto= new DtoNotasCirugiaEpicrisis();
				dto.setFecha(rs.getString("fecha"));
				dto.setHora(rs.getString("hora"));
				dto.setDescripcion(rs.getString("nota"));
				dto.setResponsable(rs.getString("responsable"));
				array.add(dto);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		return array;
	}

	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public static boolean insertarEpicrisisEventosAdversos(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		String insert="INSERT INTO epicrisis_eventos_adversos (	" +
																"codigo_evento, " +		//1
																"fecha, " +				//2
																"hora,	" +				//3
																"contenido, " +			//4
																"usuario_modifica, " +	//5
																"fecha_modifica, " +		
																"hora_modifica) " +		
																"VALUES " +
																"(?,?,?,?,?,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+")";		
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insert));
			ps.setDouble(1, Utilidades.convertirADouble(cuadroTextoMap.get("codigopk_"+indice)+""));
			ps.setDate(2, Date.valueOf(cuadroTextoMap.get("fechabd_"+indice)+""));
			ps.setString(3, cuadroTextoMap.get("hora_"+indice)+"");
			ps.setString(4, cuadroTextoMap.get("contenido_"+indice)+"");
			ps.setString(5, loginUsuario);
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public static boolean modificarEpicrisisEventosAdversos(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		String insert="update epicrisis_eventos_adversos set contenido=?, usuario_modifica=?, fecha_modifica=current_date, hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" where codigo_evento=? ";
		PreparedStatementDecorator ps= null;
		try
		{
			 ps= new PreparedStatementDecorator(con.prepareStatement(insert));
			
			ps.setString(1, cuadroTextoMap.get("contenido_"+indice)+"");
			ps.setString(2, loginUsuario);
			ps.setDouble(3, Utilidades.convertirADouble(cuadroTextoMap.get("codigopk_"+indice)+""));
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		
			
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public static boolean insertarEpicrisisSolicitudes(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		String insert="INSERT INTO epicrisis_solicitudes (	" +
																"numero_solicitud, " +	//1
																"tipo, "+				//2
																"fecha, " +				//3
																"hora,	" +				//4
																"contenido, " +			//5
																"usuario_modifica, " +	//6
																"info_automatica, "+	//7	
																"fecha_modifica, " +		
																"hora_modifica) " +		
																"VALUES " +
																"(?,?,?,?,?,?,?,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+")";		
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insert));
			
			Utilidades.imprimirMapa(cuadroTextoMap);
			
			ps.setInt(1, Utilidades.convertirAEntero(cuadroTextoMap.get("codigopk_"+indice)+""));
			
			if(Utilidades.convertirAEntero(cuadroTextoMap.get("codigotipoevolucion_"+indice)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisProcedimientos)
				ps.setInt(2, ConstantesBD.codigoTipoSolicitudProcedimiento);
			
			else if(Utilidades.convertirAEntero(cuadroTextoMap.get("codigotipoevolucion_"+indice)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisCirugia)
				ps.setInt(2, ConstantesBD.codigoTipoSolicitudCirugia);
			
			else if(Utilidades.convertirAEntero(cuadroTextoMap.get("codigotipoevolucion_"+indice)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisInterconsulta)
				ps.setInt(2, ConstantesBD.codigoTipoSolicitudInterconsulta);
			
			else if(Utilidades.convertirAEntero(cuadroTextoMap.get("codigotipoevolucion_"+indice)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisCuidadosEspeciales
					|| Utilidades.convertirAEntero(cuadroTextoMap.get("codigotipoevolucion_"+indice)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialHosp)
				ps.setInt(2, ConstantesBD.codigoTipoSolicitudInicialHospitalizacion);
			
			else if(Utilidades.convertirAEntero(cuadroTextoMap.get("codigotipoevolucion_"+indice)+"")==ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialUrg)
				ps.setInt(2, ConstantesBD.codigoTipoSolicitudInicialUrgencias);
			
			if(UtilidadTexto.isEmpty(cuadroTextoMap.get("fechabd_"+indice)+""))
				ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			else
				ps.setDate(3, Date.valueOf(cuadroTextoMap.get("fechabd_"+indice)+""));
			
			if(UtilidadTexto.isEmpty(cuadroTextoMap.get("hora_"+indice)+""))
				ps.setString(4, UtilidadFecha.getHoraActual());
			else
				ps.setString(4, cuadroTextoMap.get("hora_"+indice)+"");
			ps.setString(5, cuadroTextoMap.get("contenido_"+indice)+" ");
			ps.setString(6, loginUsuario);
			
			if(UtilidadTexto.isEmpty(cuadroTextoMap.get("infoautomatica_"+indice)+""))
				ps.setNull(7, Types.VARCHAR);
			else
				ps.setString(7, cuadroTextoMap.get("infoautomatica_"+indice)+"");
			
			logger.info("LLEGA A INSERTAR LA SOLICITUD DE EPICRISIS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		
			
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public static boolean modificarEpicrisisSolicitudes(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		String insert="update epicrisis_solicitudes set contenido=?, usuario_modifica=?, info_automatica=?, fecha_modifica=current_date, hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" where numero_solicitud=? ";
		
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insert));
			
			ps.setString(1, cuadroTextoMap.get("contenido_"+indice)+" ");
			ps.setString(2, loginUsuario);
			if(UtilidadTexto.isEmpty(cuadroTextoMap.get("infoautomatica_"+indice)+""))
				ps.setNull(3, Types.VARCHAR);
			else
				ps.setString(3, cuadroTextoMap.get("infoautomatica_"+indice)+"");
			ps.setInt(4, Utilidades.convertirAEntero(cuadroTextoMap.get("codigopk_"+indice)+""));
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		
			
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public static boolean insertarEpicrisisAdminMed(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		String insert="INSERT INTO epicrisis_admin_med  (	" +
																"codigo_administracion , " +	//1
																"fecha, " +				//2
																"hora,	" +				//3
																"contenido, " +			//4
																"usuario_modifica, " +	//5
																"fecha_modifica, " +		
																"hora_modifica) " +		
																"VALUES " +
																"(?,?,?,?,?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+")";		
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insert));
			ps.setInt(1, Utilidades.convertirAEntero(cuadroTextoMap.get("codigopk_"+indice)+""));
			ps.setDate(2, Date.valueOf(cuadroTextoMap.get("fechabd_"+indice)+""));
			ps.setString(3, cuadroTextoMap.get("hora_"+indice)+"");
			ps.setString(4, cuadroTextoMap.get("contenido_"+indice)+"");
			ps.setString(5, loginUsuario);
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public static boolean modificarEpicrisisAdminMed(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		String insert="update epicrisis_admin_med set contenido=?, usuario_modifica=?, fecha_modifica=current_date, hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" where codigo_administracion=? ";
		PreparedStatementDecorator ps=null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insert));
			
			ps.setString(1, cuadroTextoMap.get("contenido_"+indice)+"");
			ps.setString(2, loginUsuario);
			ps.setInt(3, Utilidades.convertirAEntero(cuadroTextoMap.get("codigopk_"+indice)+""));
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public static boolean insertarEpicrisisEvoluciones(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		String insert="INSERT INTO epicrisis_evoluciones (	" +
																"evolucion, " +			//1
																"fecha, " +				//2
																"hora,	" +				//3
																"contenido, " +			//4
																"usuario_modifica, " +	//5
																"fecha_modifica, " +		
																"hora_modifica) " +		
																"VALUES " +
																"(?,?,?,?,?,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+")";		
		PreparedStatementDecorator ps=null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insert));
			ps.setInt(1, Utilidades.convertirAEntero(cuadroTextoMap.get("codigopk_"+indice)+""));
			ps.setDate(2, Date.valueOf(cuadroTextoMap.get("fechabd_"+indice)+""));
			ps.setString(3, cuadroTextoMap.get("hora_"+indice)+"");
			ps.setString(4, cuadroTextoMap.get("contenido_"+indice)+"");
			ps.setString(5, loginUsuario);
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param cuadroTextoMap
	 * @param indice
	 * @param loginUsuario
	 * @return
	 */
	public static boolean modificarEpicrisisEvoluciones(Connection con, HashMap<Object, Object> cuadroTextoMap, int indice, String loginUsuario)
	{
		String insert="update epicrisis_evoluciones set contenido=?, usuario_modifica=?, fecha_modifica=current_date, hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" where evolucion=? ";
		PreparedStatementDecorator ps=null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insert));
			
			ps.setString(1, cuadroTextoMap.get("contenido_"+indice)+"");
			ps.setString(2, loginUsuario);
			ps.setInt(3, Utilidades.convertirAEntero(cuadroTextoMap.get("codigopk_"+indice)+""));
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoPk
	 * @param codigoTipoEvolucion
	 * @return
	 */
	public static boolean existeEpicrisis(Connection con, String codigoPk, int codigoTipoEvolucion)
	{
		String consulta="";
		
		if(codigoTipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisEventosAdversosAdministrativos
			|| codigoTipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisEventosAdversosAsistenciales)
		{
			consulta="SELECT codigo_evento FROM epicrisis_eventos_adversos WHERE codigo_evento="+codigoPk;
		}
		else if(codigoTipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisCirugia
				|| codigoTipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisInterconsulta
				|| codigoTipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisProcedimientos
				|| codigoTipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisCuidadosEspeciales
				|| codigoTipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialHosp
				|| codigoTipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialUrg)
		{
			consulta="SELECT numero_solicitud FROM epicrisis_solicitudes WHERE numero_solicitud="+codigoPk;
		}
		else if(codigoTipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisMedicamentos)
		{
			consulta="SELECT codigo_administracion FROM epicrisis_admin_med WHERE codigo_administracion="+codigoPk;
		}
		else if(codigoTipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisRegistroEvoluciones)
		{
			consulta="SELECT evolucion FROM epicrisis_evoluciones WHERE evolucion="+codigoPk;
		}
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			
			if(ps.executeQuery().next())
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param fechaHorasMap
	 * @return
	 */
	public static boolean actualizarIndicativoEnviadoEpicrisisEventosAdversos(Connection con, String codigo, String epicrisisSN)
	{
		String consulta="UPDATE registro_eventos_adversos SET epicrisis=? where codigo="+codigo;
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setString(1, epicrisisSN);
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param fechaHorasMap
	 * @return
	 */
	public static boolean actualizarIndicativoEnviadoEpicrisisProcedimientos(Connection con, String codigo, String epicrisisSN)
	{
		String consulta="UPDATE sol_procedimientos SET epicrisis=? where numero_solicitud="+codigo;
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setString(1, epicrisisSN);
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param fechaHorasMap
	 * @return
	 */
	public static boolean actualizarIndicativoEnviadoEpicrisisAdminMed(Connection con, String codigo, String epicrisisSN)
	{
		String consulta="UPDATE admin_medicamentos SET epicrisis=? where codigo="+codigo;
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setString(1, epicrisisSN);
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param epicrisisSN
	 * @return
	 */
	public static boolean actualizarIndicaticoEnviadoEpicrisisCxServicio(Connection con, String codigo, String epicrisisSN)
	{
		String consulta="UPDATE sol_cirugia_por_servicio SET epicrisis=? where codigo="+codigo;
		PreparedStatementDecorator ps=null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setString(1, epicrisisSN);
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param epicrisisSN
	 * @return
	 */
	public static boolean actualizarIndicaticoEnviadoEpicrisisCxSalidaPaciente(Connection con, String codigo, String epicrisisSN)
	{
		String consulta="UPDATE solicitudes_cirugia SET epicrisis_salida_pac=? where numero_solicitud="+codigo;
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setString(1, epicrisisSN);
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param epicrisisSN
	 * @return
	 */
	public static boolean actualizarIndicaticoEnviadoEpicrisisCxNotasEnfermeria(Connection con, String codigo, String epicrisisSN)
	{
		String consulta="UPDATE notas_enfermeria SET epicrisis=? where codigo="+codigo;
		PreparedStatementDecorator ps=null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setString(1, epicrisisSN);
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param epicrisisSN
	 * @return
	 */
	public static boolean actualizarIndicaticoEnviadoEpicrisisCxNotasRecuperacion(Connection con, String codigo, String epicrisisSN)
	{
		String consulta="UPDATE det_notas_recuperacion SET epicrisis=? where numero_solicitud="+codigo;
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setString(1, epicrisisSN);
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPk
	 * @param tipoEvolucion
	 * @return
	 */
	public static boolean eliminarEpicrisis(Connection con, String codigoPk, int tipoEvolucion)
	{
		String tabla="", pk="";
		if(tipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisEventosAdversosAdministrativos
			|| tipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisEventosAdversosAsistenciales)
		{
			tabla=" epicrisis_eventos_adversos ";
			pk=" codigo_evento ";
		}
		else if(tipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisMedicamentos)
		{
			tabla=" epicrisis_admin_med ";
			pk=" codigo_administracion ";
		}
		else if(tipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisCirugia
				|| tipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisProcedimientos
				|| tipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisInterconsulta
				|| tipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisCuidadosEspeciales
				|| tipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialHosp
				|| tipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisValoracionInicialUrg)
		{
			tabla=" epicrisis_solicitudes ";
			pk=" numero_solicitud ";
		}
		else if(tipoEvolucion==ConstantesBD.codigoTipoEvolucionEpicrisisRegistroEvoluciones)
		{
			tabla=" epicrisis_evoluciones ";
			pk=" evolucion ";
		}
		
		String consulta="DELETE FROM "+tabla+" WHERE "+pk+"="+codigoPk;
		logger.info("\n\n eliminarEpicrisis-->"+consulta+"\n\n");
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizarEpicrisis(Connection con, DtoEpicrisis1 dto)
	{
		String consulta="UPDATE epicrisis1 SET usuario_modifica=?, fecha_modifica=CURRENT_DATE, hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+", finalizada=?, contenido_encabezado=?, fecha_contenido=?, hora_contenido=?  WHERE ingreso=? ";
		PreparedStatementDecorator ps=null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setString(1, dto.getUsuarioModifica());
			
			if(dto.getFinalizada())
				ps.setString(2, ConstantesBD.acronimoSi);
			else
				ps.setString(2, ConstantesBD.acronimoNo);
			
			if(UtilidadTexto.isEmpty(dto.getContenidoEncabezado().toString()))
				ps.setNull(3, Types.VARCHAR);
			else
				ps.setString(3, dto.getContenidoEncabezado().toString());
			
			if(UtilidadTexto.isEmpty(dto.getFechaContenido()))
				ps.setNull(4, Types.DATE);
			else
				ps.setDate(4, Date.valueOf( UtilidadFecha.conversionFormatoFechaABD(dto.getFechaContenido())));
			
			if(UtilidadTexto.isEmpty(dto.getHoraContenido()))
				ps.setNull(5, Types.CHAR);
			else
				ps.setString(5, dto.getHoraContenido());
			
			ps.setInt(6, dto.getIngreso());
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean insertarEncabezadoEpicrisis(Connection con, DtoEpicrisis1 dto)
	{
		String consulta="INSERT INTO epicrisis1 (	ingreso, " +				//1
													"usuario_modifica,	" +		//2
													"fecha_modifica,	" +
													"hora_modifica,		" +
													"finalizada, " +			//3
													"contenido_encabezado, " +	//4
													"fecha_contenido, " +		//5
													"hora_contenido " +		//6
													") VALUES (?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?, ?, ?) ";
		
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, dto.getIngreso());
			ps.setString(2, dto.getUsuarioModifica());
			if(dto.getFinalizada())
				ps.setString(3, ConstantesBD.acronimoSi);
			else
				ps.setString(3, ConstantesBD.acronimoNo);
			
			if(UtilidadTexto.isEmpty(dto.getContenidoEncabezado().toString()))
				ps.setString(4, dto.getContenidoEncabezado().toString());
			else
				ps.setNull(4, Types.VARCHAR);
			
			if(UtilidadTexto.isEmpty(dto.getFechaContenido()))
				ps.setNull(5, Types.DATE);
			else
				ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaContenido())));
			
			if(UtilidadTexto.isEmpty(dto.getHoraContenido()))
				ps.setNull(6, Types.CHAR);
			else
				ps.setString(6, dto.getHoraContenido());
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static boolean existeEncabezadoEpicrisis(Connection con, int ingreso)
	{
		String consulta="SELECT ingreso from epicrisis1 where ingreso=?";
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, ingreso);
			if(ps.executeQuery().next())
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static boolean existeFinalizacionEpicrisis(Connection con, int ingreso)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta="SELECT finalizada from epicrisis1 where ingreso=?";
			pst= con.prepareStatement(consulta);
			pst.setInt(1, ingreso);
			rs=pst.executeQuery();
			if(rs.next())
			{	
				return UtilidadTexto.getBoolean(rs.getString(1));
			}		
		}
		catch(SQLException sqe){
			logger.error("############## SQLException ERROR existeFinalizacionEpicrisis",sqe);
		}
		catch(Exception e){
			logger.error("############## ERROR existeFinalizacionEpicrisis", e);
		}finally{
			if (pst != null){
				try{
					pst.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static double insertarNotaAclaratoria(Connection con, DtoNotasAclaratoriasEpicrisis dto)
	{
		double codigo=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		String consulta="INSERT INTO epicrisis_notas_aclaratorias (" +
																	"codigo, " +			//1
																	"ingreso, " +			//2
																	"usuario_registra, " +	//3
																	"usuario_modifica, " +	//4	
																	"nota," +				//5
																	"fecha_registra, " +	//6	
																	"hora_registra, " +		//7
																	"fecha_modifica, " +	//8
																	"hora_modifica " +		//9	
																") " +
																"VALUES " +
																"(?, ?, ?, ?, ?, ?, ?, ?, ?) ";
		
		PreparedStatementDecorator ps=null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_epicrisis_notas_a");
			ps.setDouble(1, codigo);
			ps.setInt(2, dto.getIngreso());
			ps.setString(3, dto.getLoginUsuario());
			ps.setString(4, dto.getLoginUsuario());
			ps.setString(5, dto.getNota());
			ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFecha())));
			ps.setString(7, dto.getHora());
			ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFecha())));
			ps.setString(9, dto.getHora());
			
			if(ps.executeUpdate()>0)
				return codigo;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		return ConstantesBD.codigoNuncaValidoDoubleNegativo;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarNotaAclaratoria(Connection con, double codigo)
	{
		String consulta="DELETE FROM epicrisis_notas_aclaratorias where codigo=? ";
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setDouble(1, codigo);
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean modificarNotaAclaratoria(Connection con, DtoNotasAclaratoriasEpicrisis dto )
	{
		String consulta="UPDATE epicrisis_notas_aclaratorias " +
							"SET usuario_modifica=?, " +
							"nota=?," +
							"fecha_modifica=CURRENT_DATE, " +
							"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
							"WHERE codigo=?";
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setString(1, dto.getLoginUsuario());
			ps.setString(2, dto.getNota());
			ps.setDouble(3, dto.getCodigo());
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoEventoInstCC
	 * @param codigoEvento
	 * @return
	 */
	public static HashMap<Object, Object> cargarNotasAclaratorias (Connection con, int ingreso)
	{
		//primero se carga el dto de eventos
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		
		// como puede manejar n registros entonces no se carga un Dto sino un mapa
		String cadena= "SELECT " +
							"e.codigo as codigo, " +
							"e.ingreso as ingreso, " +
							"getnombreusuario(e.usuario_modifica) as nombremedico, " +
							"e.usuario_modifica as loginusuario, "+
							"to_char(e.fecha_modifica, 'DD/MM/YYYY') as fecha, " +
							"e.fecha_modifica as fechabd, " +
							"e.hora_modifica as hora, " +
							"e.nota as nota, " +
							"'"+ConstantesBD.acronimoSi+"' as estabd " +
						"FROM " +
							"epicrisis_notas_aclaratorias e " +
						"where " +
							"e.ingreso=? " +
							"order by fechabd, hora  ";
		
		logger.info("\ncargarNotasAclaratorias->"+cadena+" -->"+ingreso+"\n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, ingreso);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static HashMap<Object, Object> cargarMedicoElaboraEpicrisis(Connection con, int ingreso)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		mapa.put("numRegistros", 0);
		String cadena=	"SELECT " +
							"u.codigo_persona as codigo, " +
							"getdatosmedico(e.usuario_modifica) as medico, " +
							"getespecialidadesmedico(e.usuario_modifica, ' - ') as especialidades, " +
							"CASE WHEN e.finalizada='"+ConstantesBD.acronimoSi+"' then 'EPICRISIS FINALIZADA' else 'EPICRISIS NO FINALIZADA' end as comentariofinalizada " +
						"FROM " +
							"epicrisis1 e " +
						"INNER JOIN " +
							"usuarios u ON (u.login=e.usuario_modifica) " +
						"WHERE " +
							"e.ingreso=? ";
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, ingreso);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static Vector<String> obtenerAntecedentesParametrizadosEpicrisis(Connection con, int institucion)
	{
		String cadena="SELECT codigo_func FROM antecedentes_epicrisis WHERE institucion=? order by orden ";
		Vector<String> vector= new Vector<String>();
		
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, institucion);
			rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				vector.add(rs.getInt(1)+"");
			}
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		
		return vector;
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static double existeEpicrisisSeccionesEnviadas(Connection con, DtoEpicrisisSecciones dto)
	{
		String consulta="select codigo from epicrisis_secciones where 1=1 ";
		
		if(dto.getNumeroSolicitud()>0)
			consulta+=" and numero_solicitud= "+dto.getNumeroSolicitud();
		else if(dto.getEvolucion()>0)
			consulta+=" and evolucion= "+dto.getEvolucion();
		
		if(dto.getPlantillaSecFija()>0)
			consulta+=" and plantilla_seccion_fija="+dto.getPlantillaSecFija();
		else if(dto.getFunParamSeccionFija()>0)
			consulta+=" and fun_param_seccion_fija="+dto.getFunParamSeccionFija();
		
		if(dto.getFunParam()>0)
			consulta+=" and fun_param ="+dto.getFunParam();
		
		logger.info("\n\nexisteEpicrisisSeccionesEnviadas--->"+consulta);
		
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				return rs.getDouble(1);
			}
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
			
		return ConstantesBD.codigoNuncaValidoDoubleNegativo;
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static double insertarEpicrisisSecciones(Connection con, DtoEpicrisisSecciones dto)
	{
		double codigo=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		String consulta="INSERT INTO epicrisis_secciones (	codigo,"+ 					//1
		 													"numero_solicitud, "+		//2
		 													"evolucion, "+				//3
		 													"ingreso, "+				//4
		 													"plantilla_seccion_fija, "+	//5
		 													"fun_param_seccion_fija, "+	//6
		 													"fun_param, "+				//7
		 													"usuario_modifica, "+		//8
		 													"fecha_modifica, "+			
		 													"hora_modifica) "+			
		 													"VALUES " +
															"(?, ?, ?, ?, " +
															"?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+") ";
		
		PreparedStatementDecorator ps= null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_epicrisis_secciones");
			ps.setDouble(1, codigo);
			
			if(dto.getNumeroSolicitud()>0)
				ps.setInt(2, dto.getNumeroSolicitud());
			else
				ps.setNull(2, Types.INTEGER);
			
			if(dto.getEvolucion()>0)
				ps.setInt(3, dto.getEvolucion());
			else
				ps.setNull(3, Types.INTEGER);
			
			ps.setInt(4, dto.getIngreso());
			
			if(dto.getPlantillaSecFija()>0)
				ps.setInt(5, dto.getPlantillaSecFija());
			else
				ps.setNull(5, Types.INTEGER);
			
			if(dto.getFunParamSeccionFija()>0)
				ps.setDouble(6, dto.getFunParamSeccionFija());
			else
				ps.setNull(6, Types.NUMERIC);
			
			ps.setInt(7, dto.getFunParam());
			ps.setString(8, dto.getUsuario());
			
			if(ps.executeUpdate()>0)
				return codigo;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return ConstantesBD.codigoNuncaValidoDoubleNegativo;
	}
	
	/**
	 * metodo que consulta las valoraciones iniciales de hospitalizacion y de urgencias
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public static HashMap<Object, Object> cargarValoracionesIniciales(Connection con, Vector<String> cuentasIngreso)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		mapa.put("numRegistros", "0");
		String consulta="(" +
							"SELECT " +
								"s.numero_solicitud as numerosolicitud, " +
								"s.tipo as tiposolicitud, " +
								"c.via_ingreso as viaingreso, " +
								"c.tipo_paciente as tipopaciente, " +
								"t.nombre ||' - '|| (CASE WHEN v.cuidado_especial='"+ConstantesBD.acronimoSi+"' THEN ' CUIDADO ESPECIAL - ' ELSE '' END ) || tp.nombre  as titulovaloracion, " +
								"'"+ConstantesBD.acronimoNo+"' as seleccionadaval " +
							"from " +
								"solicitudes s " +
								"inner join valoraciones v on(v.numero_solicitud=s.numero_solicitud) " +
								"inner join cuentas c ON(c.id=s.cuenta) " +
								"inner join tipos_solicitud t on(t.codigo=s.tipo) " +
								"inner join tipos_paciente tp on(tp.acronimo=c.tipo_paciente) " +
							"where " +
								"s.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasIngreso, false)+") " +
								"and s.tipo in("+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+", "+ConstantesBD.codigoTipoSolicitudInicialUrgencias+") " +
								"and v.cuidado_especial='"+ConstantesBD.acronimoNo+"' " +
						")" +
						"UNION " +
						"(" +
							"SELECT " +
								"s.numero_solicitud as numerosolicitud, " +
								"s.tipo as tiposolicitud, " +
								"c.via_ingreso as viaingreso, " +
								"c.tipo_paciente as tipopaciente, " +
								"t.nombre ||' - '|| (CASE WHEN v.cuidado_especial='"+ConstantesBD.acronimoSi+"' THEN ' CUIDADO ESPECIAL - ' ELSE '' END ) || tp.nombre  as titulovaloracion, " +
								"'"+ConstantesBD.acronimoNo+"' as seleccionadaval " +
							"from " +
								"solicitudes s " +
								"inner join valoraciones v on(v.numero_solicitud=s.numero_solicitud) " +
								"inner join ingresos_cuidados_especiales i on(v.numero_solicitud=i.valoracion) " +
								"inner join cuentas c ON(c.id=s.cuenta) " +
								"inner join tipos_solicitud t on(t.codigo=s.tipo) " +
								"inner join tipos_paciente tp on(tp.acronimo=c.tipo_paciente) " +
							"where " +
								"s.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasIngreso, false)+") " +
								"and s.tipo in("+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+", "+ConstantesBD.codigoTipoSolicitudInicialUrgencias+") " +
								//con esta condicion identifico que es una valoracion inicial
								"and i.valoracion=i.valoracion_orden " +
								//segun acuerdo con Paula nunca se considera valoracion inicial a los cuidados especiales cuando existe previamente una valoracion de hospitalizacion 
								"and (" +
										"select count(1) " +
										"from " +
											"solicitudes s1 " +
											"inner join valoraciones v1 on(v1.numero_solicitud=s1.numero_solicitud) " +
										"where " +
											"s1.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasIngreso, false)+") " +
											"and s1.tipo ="+ConstantesBD.codigoTipoSolicitudInicialHospitalizacion+" " +
											"and v1.cuidado_especial='"+ConstantesBD.acronimoNo+"'" +
									")<=0 " +
						")";
		
		logger.info("\ncargarValoracionesIniciales--->"+consulta);
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param cuentasIngreso
	 * @param usuario
	 * @return
	 */
	public static HashMap<Object, Object> cargarUltimaEvolucion(Connection con, Vector<String> cuentasIngreso, UsuarioBasico usuario)
	{
		HashMap<Object, Object> detalleMap= new HashMap<Object, Object>();
		detalleMap.put("numRegistros", "0");
		String consulta="SELECT " +
							"max(e.codigo) as ultimaevol " +
						"from " +
							"evoluciones e " +
							"inner join solicitudes s on(e.valoracion=s.numero_solicitud) " +
						"where " +
							"s.cuenta in("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(cuentasIngreso, false)+") " +
							"and s.estado_historia_clinica<>"+ConstantesBD.codigoEstadoHCAnulada+" ";
		
		logger.info("\n cargarUltimaEvolucion-->"+consulta+"\n");
		
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs = null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				DtoEvolucionEpicrisis dtoEvolucionEpicrisis= new DtoEvolucionEpicrisis();
				dtoEvolucionEpicrisis.setDtoEvolucion(Epicrisis1Evoluciones.cargarMundoEvolucion( rs.getInt("ultimaevol"), con));
				dtoEvolucionEpicrisis.setDtoPlantilla(Epicrisis1Evoluciones.cargarPlantillaEvolucion(con, usuario.getCodigoInstitucionInt(), rs.getInt("ultimaevol")));
				detalleMap.put("DETALLE_0", dtoEvolucionEpicrisis);
				detalleMap.put("numRegistros", 1);
			}
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		return detalleMap;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	private static DtoInterpretacionSolicitud cargarInterpretacionSolicitud(Connection con, int numeroSolicitud)
	{
		DtoInterpretacionSolicitud dto= new DtoInterpretacionSolicitud();
		String consulta="SELECT " +
							"to_char(s.fecha_interpretacion, 'DD/MM/YYYY') AS fechainterpretacion, " +
							"substr(s.hora_interpretacion,1,5) as horainterpretacion, " +
							"getdatosmedico1(s.codigo_medico_interpretacion) as medicointerpreta, " +
							"s.interpretacion as interpretacion, " +
							"si.epicrisis_interpretacion as enviadaepicrisisinterpretacion " +
						"FROM " +
							"solicitudes s inner join solicitudes_inter si on(si.numero_solicitud=s.numero_solicitud) " +
						"where " +
							"s.numero_solicitud=? ";
		
		PreparedStatementDecorator ps=null;
		ResultSetDecorator rs= null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, numeroSolicitud);
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				dto.setFechaInterpretacion(rs.getString("fechaInterpretacion"));
				dto.setHoraInterpretacion(rs.getString("horainterpretacion"));
				dto.setInterpretacion(rs.getString("interpretacion"));
				dto.setMedicoInterpreta(rs.getString("medicointerpreta"));
				dto.setEnviadaEpicrisis(UtilidadTexto.getBoolean(rs.getString("enviadaepicrisisinterpretacion")));
			}
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
		}
		return dto;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param epicrisisSN
	 * @return
	 */
	public static boolean actualizarIndicativoEnviadoEpicrisisInterpretacion(Connection con, String codigo, String epicrisisSN)
	{
		String consulta="UPDATE solicitudes_inter SET epicrisis_interpretacion=? where numero_solicitud="+codigo;
		PreparedStatementDecorator ps=null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setString(1, epicrisisSN);
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap<Object, Object> cargarInfoJusNoPos(Connection con, int numeroSolicitud, boolean esArticulo, int institucion, int codigoArticuloServicioOPCIONAL)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		mapa.put("numRegistros", "0");
		String consulta=""; 
		
		if(esArticulo)
		{
			consulta=	"SELECT " +
							"jaf.observacion as resumen_his_cli, " +
							"getdescarticulo(jas.articulo)||' NO POS' as descripcion " +
						"FROM " +
							"justificacion_art_fijo jaf " +
							"INNER JOIN justificacion_art_sol jas ON(jas.codigo=jaf.justificacion_art_sol) " +
						"where " +
							"jas.numero_solicitud=? ";
			
			if(codigoArticuloServicioOPCIONAL>0)
			{
				consulta+="AND jas.articulo= "+codigoArticuloServicioOPCIONAL+" ";
			}
		}
		else
		{
			consulta= 	"SELECT " +
							"jsf.observacion as resumen_his_cli, " +
							"getnombreservicio(jss.servicio,"+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion)+") ||' - NO POS' as descripcion " +
						"FROM " +
							"justificacion_serv_fijo jsf " +
							"INNER JOIN justificacion_serv_sol jss ON(jss.codigo=jsf.justificacion_serv_sol) " +
						"where " +
							"jss.solicitud=? ";
			
			if(codigoArticuloServicioOPCIONAL>0)
			{
				consulta+="AND jss.servicio= "+codigoArticuloServicioOPCIONAL+" ";
			}
		}
		
		logger.info("\n cargarInfoJusNoPos--->"+consulta);
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, numeroSolicitud);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
		}
		
		return mapa;
		
	}
	
	/**
	 * 
	 * @param idIngreso
	 * @return
	 */
	public static DtoEpicrisis1 cargarEpicrisis1(int idIngreso, String consulta)
	{
		DtoEpicrisis1 dto= new DtoEpicrisis1();
		ResultSetDecorator rs= null;
		PreparedStatementDecorator ps=  null;
		Connection con= UtilidadBD.abrirConexion();
		try
		{
			
			logger.info("consulta->"+consulta+" ->"+idIngreso);
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1, idIngreso);
			
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{	
				dto.setContenidoEncabezado(new StringBuilder(rs.getString("contenidoencabezado")));
				dto.setFechaContenido(rs.getString("fechacontenido"));
				dto.setFinalizada(UtilidadTexto.getBoolean(rs.getString("finalizada")));
				dto.setHoraContenido(rs.getString("horacontenido"));
				dto.setIngreso(idIngreso);
				dto.setUsuarioModifica(rs.getString("usuariomodifica"));
			}	
			
		
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			UtilidadBD.closeConnection(con);
		}
		return dto;
	}
	
	/**
	 * 
	 * @param valoracionesIniciales
	 * @return
	 */
	public static boolean esValoracionInicialEnEpicrisis(HashMap<Object, Object> valoracionesIniciales)
	{
		String consulta=" SELECT numero_solicitud from epicrisis_solicitudes where numero_solicitud in("+ConstantesBD.codigoNuncaValido;
		boolean retorna=false;
		Connection con= UtilidadBD.abrirConexion();
		PreparedStatementDecorator ps=null;
		try
		{
			for(int w=0; w<Utilidades.convertirAEntero(valoracionesIniciales.get("numRegistros")+""); w++)
			{
				consulta+=", "+valoracionesIniciales.get("numerosolicitud_"+w);
			}
			
			consulta+=") ";
		
			logger.info("\n\n\nesValoracionInicialEnEpicrisis-->"+consulta+"\n\n\n");
			
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			
			if(ps.executeQuery().next())
				retorna= true;
			UtilidadBD.closeConnection(con);
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEpicrisis1Dao " + sqlException.toString() );
				}
			}
			
			UtilidadBD.closeConnection(con);
		}
		return retorna;
	}
}