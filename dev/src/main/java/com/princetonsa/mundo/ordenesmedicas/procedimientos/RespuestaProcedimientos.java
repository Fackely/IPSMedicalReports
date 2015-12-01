package com.princetonsa.mundo.ordenesmedicas.procedimientos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.inventarios.UtilidadInventarios;
import util.laboratorios.InterfazLaboratorios;
import util.laboratorios.UtilidadLaboratorios;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ordenesmedicas.procedimientos.RespuestaProcedimientosDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.cargos.DtoDetalleCargo;
import com.princetonsa.dto.facturacion.DtoArticuloIncluidoSolProc;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.facturacion.DtoServicioIncluidoSolProc;
import com.princetonsa.dto.ordenes.DtoProcedimiento;
import com.princetonsa.mundo.Cama;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.CargosDirectos;
import com.princetonsa.mundo.cargos.CargosEntidadesSubcontratadas;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.facturacion.Servicios_ArticulosIncluidosEnOtrosProcedimientos;
import com.princetonsa.mundo.inventarios.AlmacenParametros;
import com.princetonsa.mundo.inventarios.FormatoJustArtNopos;
import com.princetonsa.mundo.medicamentos.DespachoMedicamentos;
import com.princetonsa.mundo.solicitudes.DocumentosAdjuntos;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudMedicamentos;
import com.princetonsa.mundo.solicitudes.SolicitudProcedimiento;
import com.servinte.axioma.fwk.exception.IPSException;


/**
 * @author Jorge Armando Osorio Velasquez
 * @author Wilson Rios
 *
 */
@SuppressWarnings("unchecked")
public class RespuestaProcedimientos
{
	
	RespuestaProcedimientosDao objetoDao;
	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(RespuestaProcedimientos.class);
	
	/**
	 * 
	 * */
	public static final String codIngRangoRes = "1";
	
	/**
	 * 
	 * */
	public static final String codIngPacRes = "2";
	
	/**
	 * codigo de resopuesta
	 */
	private String codigoRespuesta;
	
	/**
	 * 
	 */
	private String numeroSolicitud;
	
	/**
	 * Fecha en que se ejecut� el procedimiento
	 */
	private String fechaEjecucion;
	
	/**
	 * Hora en se ejecut� el procedimiento.
	 */
	private String horaEjecucion;
	
	/**
	 * C�digo del tipo de recargo del procedimiento
	 */
	private int codigoTipoRecargo;
	
	/**
	 * 
	 */	
	private String nombreTipoRecargo;
	
	/**
	 * Comentario historica cl�nica 
	 */
	private String comentariosHistoriaClinica;
	
	/**
	 * Resultados del procedimiento
	 */
	private String resultados;
	
	/**
	 * 
	 */
	private String resultadosAnteriores;
	

	/**
	 * 
	 */
	private int finalidad;
	
	/**
	 * Observaciones del procedimiento
	 */
	private String observaciones;
	
	/**
	 * 
	 */
	private String finalizar;
	
	
//	********ATRIBUTOS DIAGN�STICOS PROCEDIMIENTOS***************************
	/**
	 * Objeto que almacena los diagn�sticos de la respuesta de procedimientos
	 */
	private HashMap diagnosticos = new HashMap();
	/**
	 * N�mero de diagn�sticos del procedimientos
	 */
	private int numDiagnosticos;
	//************************************************************************
	
	/**
	 * 
	 */
	private HashMap datosHistoriaClinica;
	
	/**
	 * 
	 */
	private SolicitudProcedimiento solicitudProcedimiento;
	
	/**
	 * 
	 */
	private String especialidadSolicitada;
	
	/**
	 * 
	 */
	private String nomEspecialidadSolicitada;
	
	/**
	 * portatil
	 */
	private String portatil=ConstantesBD.codigoNuncaValido+"";
	
	DocumentosAdjuntos documentosAdjuntos= new DocumentosAdjuntos();
	
	/**
	 * 
	 *
	 */
	public RespuestaProcedimientos()
	{
		this.reset(true);
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * 
	 *
	 */
	public void reset(boolean cleanAdjuntos)
	{
		this.numeroSolicitud="";
		this.datosHistoriaClinica= new HashMap();
		this.resultadosAnteriores="";
		this.solicitudProcedimiento=new SolicitudProcedimiento();
		this.finalizar="";
		this.fechaEjecucion=UtilidadFecha.getFechaActual();
		this.horaEjecucion=UtilidadFecha.getHoraActual();
		this.codigoTipoRecargo=ConstantesBD.codigoTipoRecargoSinRecargo;
		this.comentariosHistoriaClinica="";
		this.resultados="";
		this.observaciones="";
		
		this.diagnosticos = new HashMap();
		this.numDiagnosticos = 0;
		this.codigoRespuesta="";
		this.datosHistoriaClinica=new HashMap();
		
		if(cleanAdjuntos)
			this.documentosAdjuntos= new DocumentosAdjuntos();
		
		this.nombreTipoRecargo="";
		this.portatil=ConstantesBD.codigoNuncaValido+"";
		this.especialidadSolicitada="";
		this.nomEspecialidadSolicitada="";
	}

	/**
	 * @return Returns the nombreTipoRecargo.
	 */
	public String getNombreTipoRecargo() {
		return nombreTipoRecargo;
	}

	/**
	 * @param nombreTipoRecargo The nombreTipoRecargo to set.
	 */
	public void setNombreTipoRecargo(String nombreTipoRecargo) {
		this.nombreTipoRecargo = nombreTipoRecargo;
	}

	/**
	 * 
	 * @param tipoBD
	 */
	private void init(String tipoBD)
	{
		if (objetoDao == null) 
		{
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			objetoDao = myFactory.getRespuestaProcedimientosDao();
		}
		
	}
	
	/**
	 * 
	 * */
	private static RespuestaProcedimientosDao getRespuestaProcedimientosDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRespuestaProcedimientosDao();
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 */
	public HashMap generarListadoSolicitudesProcedimientosResponder(Connection con, HashMap vo)
	{
		return objetoDao.listadoSolicitudesProcedimientosResponder(con,vo);
	}
	
	/**
	 * 
	 */
	public String insertarRespuestaProcedimiento	(	Connection con,
																		String numeroSolicitud,
																		String fechaEjecucion,
																		String resultados,
																		String observaciones,
																		int tipoRecargo,
																		String comentarioHistoriaClinica,
																		String horaEjecucion,
																		int codigoMedicoResponde,
																		String loginUsuarioRegistraRes,
																		int finalidad,
																		String observacionCapitacion)
	{
		return objetoDao.insertarRespuestaProcedimiento(con, numeroSolicitud, fechaEjecucion, resultados, observaciones, tipoRecargo, comentarioHistoriaClinica, horaEjecucion,codigoMedicoResponde,loginUsuarioRegistraRes,finalidad, observacionCapitacion);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean finaizarRespuestaSolProcedimiento (	Connection con,
																String numeroSolicitud,
																String acronimofinalizar
															)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRespuestaProcedimientosDao().finaizarRespuestaSolProcedimiento(con, numeroSolicitud, acronimofinalizar);
	}
	
	/**
	 * M�todo implementado para insertar los diagn�sticos del
	 * procedimiento
	 * @param con
	 * @param esModificacion
	 * @return
	 */
	public static boolean insertarDiagnosticos(Connection con, boolean esModificacion, String codigoRespuesta, HashMap diagnosticos, int numDiagnosticos) 
	{
		logger.info("\n\n\ndiagnosticos--->"+diagnosticos);
		RespuestaProcedimientosDao dao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRespuestaProcedimientosDao();
		
		boolean resp=true;
		String auxS0 = "";
		int contador = 0;
		
		//Se verifica si es modificacion, para eliminar los registros actuales
		if(esModificacion)
			resp = dao.eliminarDiagnosticos(con, codigoRespuesta);
		
		logger.info("respuesta eliminacion-->"+resp);
		
		if(resp)
		{
			//INSERCI�N DEL DIAGN�STICO PRINCIPAL ******************************************
			if(diagnosticos.containsKey("principal") && diagnosticos.containsKey("complicacion"))
			{	
				auxS0= diagnosticos.get("principal").toString().split(ConstantesBD.separadorSplit)[0];
				//auxS0 = diagnosticos.get("acronimoPrincipal") + "";
				//se verifica si se insert� diagn�stico principal
				if(!auxS0.equals("")&&!auxS0.equals("null"))
				{
					resp = dao.insertarDiagnostico(
						con,
						codigoRespuesta,
						//diagnosticos.get("acronimoPrincipal")+"",
						diagnosticos.get("principal").toString().split(ConstantesBD.separadorSplit)[0],
						//Integer.parseInt(diagnosticos.get("tipoCiePrincipal")+""),
						Integer.parseInt(diagnosticos.get("principal").toString().split(ConstantesBD.separadorSplit)[1]),
						true,false,0,ConstantesBD.continuarTransaccion);
					if(!resp)
						return resp;
				}
				
				//INSERCI�N DEL DIAGN�STICO DE COMPLICACI�N ***************************************
				auxS0 = diagnosticos.get("complicacion").toString().split(ConstantesBD.separadorSplit)[0];
				//se verifica si se insert� diagn�stico de complicaci�n
				if(!auxS0.equals("")&&!auxS0.equals("null"))
				{
					resp = dao.insertarDiagnostico(
						con,
						codigoRespuesta,
						//diagnosticos.get("acronimoComplicacion")+"",
						diagnosticos.get("complicacion").toString().split(ConstantesBD.separadorSplit)[0],
						//Integer.parseInt(diagnosticos.get("tipoCieComplicacion")+""),
						Integer.parseInt(diagnosticos.get("complicacion").toString().split(ConstantesBD.separadorSplit)[1]),
						false,true,0,ConstantesBD.continuarTransaccion);
					
					if(!resp)
						return resp;
				}
				
				//INSERCI�N DE LOS DIAGN�STICOS RELACIONADOS *******************************************
				//iteraci�n de los diagn�sticos
				for(int i=0;i<numDiagnosticos;i++)
				{
					//se verifica si el diagn�stico fue chequeado
					if(UtilidadTexto.getBoolean(diagnosticos.get("checkRel_"+i)+""))
					{
						contador ++;
						
						resp = dao.insertarDiagnostico(
								con,
								codigoRespuesta,
								//diagnosticos.get("acronimoRel_"+i)+"",
								diagnosticos.get("relacionado_"+i).toString().split(ConstantesBD.separadorSplit)[0],
								//Integer.parseInt(diagnosticos.get("tipoCieRel_"+i)+""),
								Integer.parseInt(diagnosticos.get("relacionado_"+i).toString().split(ConstantesBD.separadorSplit)[1]),
								false,false,contador,ConstantesBD.continuarTransaccion);
						
						if(!resp)
							//se finaliza ciclo
							i = numDiagnosticos;
					}
				}
			}	
		}
		return resp;
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param medico
	 * @param paciente
	 * @return
	 */
	public int insertarNuevaSolicitudYGenerarEstructuraLabsoft(Connection con, SolicitudProcedimiento solProcedimiento, UsuarioBasico medico, PersonaBasica paciente)
	{
		int nuevaSolicitud=0;
		try 
		{
			logger.info("\n\n66666666666666666666666666666666666 entre a insertarNuevaSolicitudYGenerarEstructuraLabsoft");
			nuevaSolicitud = solProcedimiento.insertarTransaccional(
					con,
					ConstantesBD.continuarTransaccion,
					solProcedimiento.getNumeroDocumento(),
					paciente.getCodigoCuenta(),
					false,
					solProcedimiento.getPortatil());
			
			//GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA 
		    Cargos cargos= new Cargos();
		    cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
																				medico, 
																				paciente, 
																				true/*dejarPendiente*/, 
																				nuevaSolicitud, 
																				ConstantesBD.codigoTipoSolicitudProcedimiento /*codigoTipoSolicitudOPCIONAL*/, 
																				paciente.getCodigoCuenta(), 
																				ConstantesBD.codigoNuncaValido/*codigoCentroCostoEjecutaOPCIONAL*/, 
																				ConstantesBD.codigoNuncaValido/*codigoServicioOPCIONAL*/, 
																				1/*cantidadServicioOPCIONAL*/, 
																				ConstantesBD.codigoNuncaValido/*valorTarifaOPCIONAL*/, 
																				ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
																				/*""--numeroAutorizacionOPCIONAL*/
																				""/*esPortatil*/,false,this.fechaEjecucion,"");
		    
		    if(solicitudProcedimiento.getPortatilInt()>0)
		    {
			    cargos= new Cargos();
			    cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
																					medico, 
																					paciente, 
																					true/*dejarPendiente*/, 
																					nuevaSolicitud, 
																					ConstantesBD.codigoTipoSolicitudProcedimiento /*codigoTipoSolicitudOPCIONAL*/, 
																					paciente.getCodigoCuenta(), 
																					ConstantesBD.codigoNuncaValido/*codigoCentroCostoEjecutaOPCIONAL*/, 
																					solicitudProcedimiento.getPortatilInt()/*codigoServicioOPCIONAL*/, 
																					1/*cantidadServicioOPCIONAL*/, 
																					ConstantesBD.codigoNuncaValido/*valorTarifaOPCIONAL*/, 
																					ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
																					/*""--numeroAutorizacionOPCIONAL*/
																					ConstantesBD.acronimoSi/*esPortatil*/,false,this.fechaEjecucion,"");
		    }   
			
		   
		   
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			logger.info("error no inserto la nueva solicitud");
		}
		logger.info("nuevaSolicitud "+nuevaSolicitud);
		
		//****************LLAMADO A INTERFAZ DE LABORATORIOS***************************
		HashMap infoInterfaz = new HashMap();
		infoInterfaz.put("numeroDocumento",solProcedimiento.getNumeroDocumento()+"");
		infoInterfaz.put("fechaSolicitud",solProcedimiento.getFechaSolicitud());
		infoInterfaz.put("horaSolicitud",solProcedimiento.getHoraSolicitud());
		infoInterfaz.put("codigoMedico",solProcedimiento.getCodigoMedicoSolicitante()+"");
		infoInterfaz.put("nombreMedico",UtilidadValidacion.obtenerNombrePersona(con,solProcedimiento.getCodigoMedicoSolicitante()));
		infoInterfaz.put("institucion",medico.getCodigoInstitucion());
		infoInterfaz.put("observaciones",solProcedimiento.getComentario());
		Cama cama = new Cama();
		try 
		{
			cama.cargarCama(con,paciente.getCodigoCama()+"");
		} 
		catch (SQLException e) 
		{
			logger.error("Error al cargar informacion de la cama del paciente en insertarNuevaSolicitudYGenerarEstructuraLabsoft: "+e);
		}
		infoInterfaz.put("numeroCama",cama.getDescripcionCama());
		if(ValoresPorDefecto.getCliente().equals(ConstantesBD.clienteSHAIO))
		{
			//infoInterfaz.put("comentarioDiagnostico", UtilidadesHistoriaClinica.obtenerDescripcionDxIngresoPaciente(con, paciente.getCodigoCuenta(), paciente.getCodigoUltimaViaIngreso()));
			infoInterfaz.put("horaSistema",UtilidadFecha.getHoraSegundosActual(con));
			infoInterfaz.put("nitEmpresa",UtilidadesFacturacion.obtenerNitEmpresaConvenio(con, paciente.getCodigoConvenio()));
			infoInterfaz.put("nroCarnet",UtilidadesManejoPaciente.obtenerNroCarnetIngresoPaciente(con, paciente.getCodigoIngreso()));
			infoInterfaz.put("codigoEspecialidadSolicitante",solProcedimiento.getEspecialidadSolicitante().getCodigo());
			//infoInterfaz.put("ciePrevio",Utilidades.consultarDiagnosticosPaciente(con, paciente.getCodigoCuenta()+"", paciente.getCodigoUltimaViaIngreso()));
			//infoInterfaz.put("habitacionCama",cama.getCodigoDescriptivoHabitacion());
			
		}
		infoInterfaz.put("numeroSolicitud_0",solProcedimiento.getNumeroSolicitud()+"");
		infoInterfaz.put("estado_0",ConstantesBD.codigoEstadoHCSolicitada+"");
		infoInterfaz.put("centroCosto_0",solProcedimiento.getCentroCostoSolicitado().getCodigo()+"");
		infoInterfaz.put("urgente_0",solProcedimiento.getUrgente()+"");
		if(ValoresPorDefecto.getCliente().equals(ConstantesBD.clienteSHAIO))
		{
			infoInterfaz.put("codigoLaboratorio_0",UtilidadLaboratorios.obtenerCodigoLaboratorioServicio(con, solProcedimiento.getCodigoServicioSolicitado()));
			HashMap informacionServ = Utilidades.consultarInformacionServicio(con, solProcedimiento.getCodigoServicioSolicitado()+"", medico.getCodigoInstitucionInt());
			infoInterfaz.put("codigoCUPS_0",informacionServ.get("codigopropietario")+"");
			infoInterfaz.put("nombreServicio_0",informacionServ.get("nombre")+"");
		}
		else
		{
			infoInterfaz.put("codigoCUPS_0",Utilidades.obtenerCodigoPropietarioServicio(con,solProcedimiento.getCodigoServicioSolicitado()+"",ConstantesBD.codigoTarifarioCups));
		}
		
		infoInterfaz.put("numRegistros","1");
		InterfazLaboratorios.generarRegistroArchivo(infoInterfaz,paciente,ValoresPorDefecto.getCliente(),new ActionErrors());
		//**************************************************************************
		return nuevaSolicitud;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean insertarDocumentosAdjuntos(Connection con, int numeroSolicitud)
	{
		try 
		{
			return this.documentosAdjuntos.insertarEliminarDocumentosAdjuntosTransaccional(con, numeroSolicitud, ConstantesBD.continuarTransaccion).isTrue();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	//*********************************************************************************************************
	
	/**
	 * Carga el Dto con la informacion del procedimiento
	 * @param Connection con
	 * @param String numeroSolicitud
	 * @param String codigoPkRespProc
	 * @param int institucion
	 * @param int codigoCentroAtencion
	 * */
	public static DtoProcedimiento cargarDtoProcedimiento(
			Connection con,
			String codigoPaciente,
			String numeroSolicitud,
			String codigoPkRespProc,
			int institucion,
			int codigoCentroAtencion)
	{
		HashMap parametros = new HashMap();
		parametros.put("numeroSolicitud",numeroSolicitud);
		
		if(Utilidades.convertirAEntero(codigoPkRespProc) > 0)
			parametros.put("codigoPkRespProc",codigoPkRespProc);
		else
			parametros.put("codigoPkRespProc","");
		
		parametros.put("codigoPaciente",codigoPaciente);
		parametros.put("tarifario",ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion));
		parametros.put("codigoCentroAtencion", codigoCentroAtencion);
		
		DtoProcedimiento res = getRespuestaProcedimientosDao().cargarDtoProcedimiento(con, parametros);
		
		//Si retorna un registro historico se carga en el dtoEspecifico
		if(res.getRespuestaProceArray().size() == 1)
			res.setRespuestaProceEspecificoDto(res.getRespuestaProceArray().get(0));
		
		logger.info("valor del Codigo Pk Respuesta Procedimiento >> "+res.getRespuestaProceEspecificoDto().getCodigoPkRespuestaProce());
		
		return res; 
	}
	
	//*********************************************************************************************************
	
	/**
	 * Actualiza el numero de respuestas anteriores
	 * @param Connection con
	 * @param int numero
	 * @param String res_sol_proc
	 * */
	public static boolean actualizarNoRespuestasAnteriores(
			Connection con,
			int numero, 
			String res_sol_proc,
			int codigoCentroAtencion,
			int codigoGrupoServicio,
			int codigoCentroCostos)
	{
		HashMap parametros = new HashMap();
		parametros.put("numero",numero);
		parametros.put("res_sol_proc",res_sol_proc);
		parametros.put("codigoCentroAtencion",codigoCentroAtencion);
		parametros.put("codigoGrupoServicio",codigoGrupoServicio);
		parametros.put("codigoCentroCostos",codigoCentroCostos);
	
		return getRespuestaProcedimientosDao().actualizarNoRespuestasAnteriores(con, parametros);
	}

	//*********************************************************************************************************
	
	
	/**
	 * Guarda Otros Comentarios
	 * @param Connection con
	 * @param Connection con
	 * @param String codigoPkRespProc
	 * @param String codigoMedico
	 * @param String descripcion
	 * @param UsuarioBasico usuario
	 * */
	public static boolean guardarOtrosComentarios(
			Connection con,
			String codigoPkRespProc,
			String codigoMedico, 
			String descripcion,
			UsuarioBasico usuario)
	{
		HashMap parametros = new HashMap();
		
		parametros.put("res_sol_proc",codigoPkRespProc);
		parametros.put("codigo_medico",codigoMedico);
		parametros.put("descripcion",descripcion);
		parametros.put("usuario_modifica",usuario.getLoginUsuario());
		parametros.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("hora_modifica",UtilidadFecha.getHoraActual());
				
		return getRespuestaProcedimientosDao().guardarOtrosComentarios(con, parametros);	
	}
	
	//*********************************************************************************************************
	
	/**
	 * Guarda Muerte del Paciente desde Respuesta de Procedimientos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static boolean guardarMuertePacienteRespProc(
			Connection con,
			String codigoPkRespProc,
			String cuenta,
			String codigoPaciente,
			String diagnosticoMuerte,
			String diagnosticoMuerteCie,
			String fechaMuerte,
			String horaMuerte,
			String certificado)
	{
		HashMap parametros = new HashMap();
		
		parametros.put("codigoPkRespProc",codigoPkRespProc);
		parametros.put("cuenta",cuenta);
		parametros.put("codigoPaciente",codigoPaciente);
		parametros.put("diagnostico_muerte",diagnosticoMuerte);
		parametros.put("diagnostico_muerte_cie",diagnosticoMuerteCie);		
		parametros.put("fechaMuerte",UtilidadFecha.conversionFormatoFechaABD(fechaMuerte));
		parametros.put("horaMuerte",horaMuerte);		
		parametros.put("certificado",certificado);
				
		return getRespuestaProcedimientosDao().guardarMuertePacienteRespProc(con, parametros);	
	}
	
	//*********************************************************************************************************
	
	/**
	 * consulta el numero de respuestas anteriores
	 * @param Connection con
	 * @param int codigoGrupoServicio
	 * @param int codigoServicio (opcional -1)
	 * @param int codigoCentroCostos
	 * @param int numeroSolicitud (opcional -1)
	 * */
	public static int getNumeroRespuestasAnteriores(
			Connection con,
			int codigoCentroAtencion,
			int codigoGrupoServicio,			
			int codigoCentroCostos)
	{
		HashMap parametros = new HashMap();
		
		parametros.put("codigoCentroAtencion",codigoCentroAtencion);
		parametros.put("codigoGrupoServicio",codigoGrupoServicio);		
		parametros.put("codigoCentroCostos",codigoCentroCostos);			
		
		return getRespuestaProcedimientosDao().getNumeroRespuestasAnteriores(con, parametros);		
	}
	//*********************************************************************************************************
	
	/**
	 * Guarda la informacion de Otros Comentarios
	 * @param Connection con
	 * @param ArrayList<InfoDatosString> array
	 * @param String codigoPkRespProc
	 * @param UsuarioBasico usuario
	 * */
	@SuppressWarnings("deprecation")
	public static boolean guardarOtrosComentarios(
			Connection con,
			ArrayList<InfoDatosString> array,
			String codigoPkRespProc,
			UsuarioBasico usuario)
	{
		for(int i = 0; i < array.size(); i++)
		{
			if(array.get(i).getIndicativo().equals(ConstantesBD.acronimoNo))
			{
				if(!guardarOtrosComentarios(
						con,
						codigoPkRespProc,
						array.get(i).getCodigo(),
						array.get(i).getDescripcion(),						
						usuario))
					return false;								
			}
		}
		
		return true;
	}
	
	//*********************************************************************************************************
	//aca se cargan las respuestas
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 */
	public boolean cargarRespuestaProcedimientos(String numeroSolicitud1,  boolean esResumen, String codigoRespuesta, String codigoSolCxServ)
	{
		Connection con=UtilidadBD.abrirConexion();
		//primero se carga la solicitud 
		SolicitudProcedimiento solProc= new SolicitudProcedimiento();
		try 
		{
			solProc.cargarSolicitudProcedimiento(con, Integer.parseInt(numeroSolicitud1));
		} 
		catch (NumberFormatException e) 
		{
			UtilidadBD.closeConnection(con);
			e.printStackTrace();
			return false;
		} 
		catch (SQLException e) 
		{
			UtilidadBD.closeConnection(con);
			e.printStackTrace();
			return false;
		}
		
		this.setNumeroSolicitud(numeroSolicitud1);
		
		//solicitud
		this.setSolicitudProcedimiento(solProc);
		
		//se cargan los datos de la historia clinica
		this.setDatosHistoriaClinica(RespuestaProcedimientos.datosHistoriaClinica(con, numeroSolicitud1));
		
		//se cargan los resultados anteriores
		this.setResultadosAnteriores(RespuestaProcedimientos.cargarResultadosAnteriores(con, numeroSolicitud1,codigoSolCxServ));
		
		if(esResumen && !UtilidadTexto.isEmpty(codigoRespuesta))
		{
			ResultSetDecorator rs= this.cargarRespuestaBasica(con, codigoRespuesta);
			if(rs==null)
				return false;
			else
			{	
				try 
				{
					if(rs.next())
					{
						this.codigoRespuesta = codigoRespuesta;
						this.setFechaEjecucion(rs.getString("fechaEjecucion"));
						this.setHoraEjecucion(rs.getString("horaEjecucion"));
						this.setCodigoTipoRecargo(rs.getInt("codigoTipoRecargo"));
						this.setNombreTipoRecargo(rs.getString("nombreTipoRecargo"));
						this.setComentariosHistoriaClinica(rs.getString("comentarioHistoriaClinica"));
						this.setResultados(rs.getString("resultado"));
						this.setObservaciones(rs.getString("observaciones"));
					}
				} 
				catch (SQLException e) 
				{
					logger.info("error en cargar");
					e.printStackTrace();
					return false;
				}
			}
			//carga de los diagnosticos
			this.cargarDiagnosticos(con, codigoRespuesta);
			
			//carga de los documentos adjunetos
			this.documentosAdjuntos = new DocumentosAdjuntos();
			ResultadoBoolean resp=this.documentosAdjuntos.cargarDocumentosAdjuntos(con, Integer.parseInt(numeroSolicitud1), false, codigoRespuesta);
			if (!resp.isTrue()&&resp.getDescripcion()!=null&&!resp.getDescripcion().equals(""))
			{
			    //No carga bien si sale en false y hay alguna descripci�n
			    return false;
			}
		}
				
		UtilidadBD.closeConnection(con);
		return true;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoRespuesta
	 * @return
	 */
	public ResultSetDecorator cargarRespuestaBasica (Connection con, String codigoRespuesta)
	{
		return objetoDao.cargarRespuestaBasica(con, codigoRespuesta);
	}
	
	
	/**
	 *
	 *debe tener parametrizado el objeto
	 * @param con
	 * @param forma
	 * @return
	 */
	public boolean insertarRespuestaYGenerarCargo(Connection con, UsuarioBasico user,String loginUsuarioRegistra, PersonaBasica paciente, String observacionCapitacion) throws IPSException 
	{		
		String resultados= UtilidadCadena.cargarObservaciones(this.getResultados(), "", user);
		String observaciones= UtilidadCadena.cargarObservaciones(this.getObservaciones(), "", user);
		
		//valida que el tama�o de los resulados no exceda el maximo en tablas 4000		
		if(resultados.length() > 4000)
		{
			logger.info("\n\n\n\nse rompe la cadena de resultados, excede valor maximo >> "+resultados.length());
			resultados = resultados.substring(0,3999);
		}
		
		
		String codigoRespuesta = this.insertarRespuestaProcedimiento(
				con, 
				this.getNumeroSolicitud(),
				this.getFechaEjecucion(),
				resultados,
				observaciones,
				this.getCodigoTipoRecargo(),
				this.getComentariosHistoriaClinica(),
				this.getHoraEjecucion(),
				user.getCodigoPersona(),
				loginUsuarioRegistra,
				this.finalidad, 
				observacionCapitacion);
		
		this.setCodigoRespuesta(codigoRespuesta);
		boolean inserto=false;
		if(!codigoRespuesta.equals(""))
			inserto=true;
		if( inserto )
		{
			Solicitud sol=new Solicitud();
			//se actualiza el pool del m�dico--------------------------------------------------------------
		    ArrayList array=Utilidades.obtenerPoolesMedico(con,UtilidadFecha.getFechaActual(),user.getCodigoPersona());
			if(array.size()==1)
			{
				sol.actualizarPoolSolicitud(con,this.getNumeroSolicitudInt(),Integer.parseInt(array.get(0)+""));
			}
			//se actualiza el m�dico que responde --------------------------------------------------------------------------
		    try 
		    {
				sol.actualizarMedicoRespondeTransaccional(con, this.getNumeroSolicitudInt(), user, ConstantesBD.continuarTransaccion);
			} 
		    catch (SQLException e) 
		    {
				e.printStackTrace();
				logger.info("no actualizo el medico que responde");
			}
		    
		    try{
		      sol.actualizarEspecialidadTransaccional(con, this.getNumeroSolicitudInt(), Utilidades.convertirAEntero(this.getEspecialidadSolicitada()), ConstantesBD.continuarTransaccion);
		      
		    }catch(SQLException e) 
		    {
		    	e.printStackTrace();
				logger.info("no actualizo Especialidad Solicitada del  medico que responde");
		    }
		    
		    inserto=RespuestaProcedimientos.insertarDiagnosticos(con, false, codigoRespuesta, this.getDiagnosticos(), this.getNumDiagnosticos());
			
		    if(!inserto)
		    {
		    	logger.warn("No inserto los diagnosticos********");
		    	return false;
		    }

		    //recalcular el cargo siempre
		    //GENERACION DEL CARGO DE SERVICIO CUANDO SE RESPONDE
			Cargos cargos= new Cargos();
			//como solo es 1 servicio entonces no puede tener n responsables con cargo pendiente entonces le enviamos el convenio vacio
			cargos.recalcularCargoServicio(con, Integer.parseInt(numeroSolicitud), user, ConstantesBD.codigoNuncaValido/*codigoEvolucionOPCIONAL*/, ""/*observaciones*/, ConstantesBD.codigoNuncaValido /*codigoServicioOPCIONAL*/, ConstantesBD.codigoNuncaValido /*subCuentaResponsable*/, ConstantesBD.codigoNuncaValido /*codigoEsquemaTarifarioOPCIONAL*/, false /*filtrarSoloCantidadesMayoresCero*/, ConstantesBD.acronimoNo /*esComponentePaquete*/, ConstantesBD.acronimoNo/*esPortatil*/,this.fechaEjecucion);

			//verificamos si maneja portatil para recalcular tambien el cargo del portatil
			int codigoServicioPortatil=SolicitudProcedimiento.obtenerPortatilSolicitud(con, Integer.parseInt(numeroSolicitud));
			logger.info("Portatil asociado "+codigoServicioPortatil);
			if(codigoServicioPortatil>0)
			{
				cargos.recalcularCargoServicio(con, Integer.parseInt(numeroSolicitud), user, ConstantesBD.codigoNuncaValido/*codigoEvolucionOPCIONAL*/, ""/*observaciones*/, codigoServicioPortatil /*codigoServicioOPCIONAL*/, ConstantesBD.codigoNuncaValido /*subCuentaResponsable*/, ConstantesBD.codigoNuncaValido /*codigoEsquemaTarifarioOPCIONAL*/, false /*filtrarSoloCantidadesMayoresCero*/, ConstantesBD.acronimoNo /*esComponentePaquete*/, ConstantesBD.acronimoSi/*esPortatil*/,this.fechaEjecucion);
			}
			
		}	
		return inserto;
	}

	/**
	 * M�todo implementado para cargar los diagn�sticos de un procedimiento
	 * @param con
	 */
	private void cargarDiagnosticos(Connection con, String codigoRespuesta) 
	{
		int numRegistros = 0;
		int numDiag = 0;
		String auxS0 = "", auxS1 = "";
		this.diagnosticos = new HashMap();
		HashMap mapaAux = this.objetoDao.cargarDiagnosticos(con,codigoRespuesta);
		numRegistros = Integer.parseInt(mapaAux.get("numRegistros")+"");
		
		for(int i=0;i<numRegistros;i++)
		{
			//se toman los indicativos de tipo diagn�stico
			auxS0 = mapaAux.get("principal_"+i) + "";
			auxS1 = mapaAux.get("complicacion_"+i) + "";
			
			//DIAGN�STICO PRINCIPAL
			if(UtilidadTexto.getBoolean(auxS0)&&!UtilidadTexto.getBoolean(auxS1))
			{
				this.diagnosticos.put("acronimoPrincipal",mapaAux.get("acronimo_"+i));
				this.diagnosticos.put("tipoCiePrincipal",mapaAux.get("tipo_cie_"+i));
				this.diagnosticos.put("nombrePrincipal",mapaAux.get("nombre_"+i));
				this.diagnosticos.put("numeroPrincipal",mapaAux.get("numero_"+i));
				this.diagnosticos.put("principal", ""+mapaAux.get("acronimo_"+i)
													+ConstantesBD.separadorSplit+mapaAux.get("tipo_cie_"+i)
													+ConstantesBD.separadorSplit+mapaAux.get("nombre_"+i));
			}
			//DIAGN�STICO COMPLICACION
			else if(!UtilidadTexto.getBoolean(auxS0)&&UtilidadTexto.getBoolean(auxS1))
			{
				this.diagnosticos.put("acronimoComplicacion",mapaAux.get("acronimo_"+i));
				this.diagnosticos.put("tipoCieComplicacion",mapaAux.get("tipo_cie_"+i));
				this.diagnosticos.put("nombreComplicacion",mapaAux.get("nombre_"+i));
				this.diagnosticos.put("numeroComplicacion",mapaAux.get("numero_"+i));
				this.diagnosticos.put("complicacion", ""+mapaAux.get("acronimo_"+i)
						+ConstantesBD.separadorSplit+mapaAux.get("tipo_cie_"+i)
						+ConstantesBD.separadorSplit+mapaAux.get("nombre_"+i));
			}
			//DIAGN�STICOS RELACIONADOS
			else if(!UtilidadTexto.getBoolean(auxS0)&&!UtilidadTexto.getBoolean(auxS1))
			{
				this.diagnosticos.put("acronimoRel_"+numDiag,mapaAux.get("acronimo_"+i));
				this.diagnosticos.put("tipoCieRel_"+numDiag,mapaAux.get("tipo_cie_"+i));
				this.diagnosticos.put("nombreRel_"+numDiag,mapaAux.get("nombre_"+i));
				this.diagnosticos.put("numeroRel_"+numDiag,mapaAux.get("numero_"+i));
				this.diagnosticos.put("relacionado", ""+mapaAux.get("acronimo_"+i)
						+ConstantesBD.separadorSplit+mapaAux.get("tipo_cie_"+i)
						+ConstantesBD.separadorSplit+mapaAux.get("nombre_"+i));
				numDiag ++;
			}
		}
		this.numDiagnosticos = numDiag;
		this.diagnosticos.put("numRegistros",numRegistros+"");
		this.diagnosticos.put("numRegistrosDiagonosticosRelacionados",numDiag+"");
		
	}
	
	
	/**
	 * Consulta la informacion de los articulos incluidos dentro de una solicitud
	 * @param Connection con 
	 * @param HashMap parametros
	 * */
	public static HashMap cargarArticulosIncluidosSolicitud(Connection con,String numeroSolicitud)
	{
		HashMap parametros = new HashMap();
		parametros.put("numeroSolicitud",numeroSolicitud);		
		return getRespuestaProcedimientosDao().cargarArticulosIncluidosSolicitud(con,parametros);
	}
	
	
	/**
	 * Carga el dto con los servicios incluidos dentro de una solicitud
	 * @param Connection con
	 * @param String numeroSolicitud
	 * */
	public static ArrayList<DtoServicioIncluidoSolProc> cargarServiciosIncluidosSolicitudDto(Connection con, String numeroSolicitud, int institucion)
	{
		ArrayList<DtoServicioIncluidoSolProc> array= new ArrayList<DtoServicioIncluidoSolProc>();
		DtoServicioIncluidoSolProc dto;
		HashMap mapa = new HashMap();
		int numRegistros = 0;
		
		//Se consulta la informacion		
		mapa = cargarServiciosIncluidosSolicitud(con,numeroSolicitud, institucion);
		
		logger.info("#########################################################################################");
		Utilidades.imprimirMapa(mapa);
		
		
		numRegistros = Utilidades.convertirAEntero(mapa.get("numRegistros").toString());
		
		for(int i = 0; i < numRegistros; i++)
		{
			dto = new DtoServicioIncluidoSolProc();
			dto.setCentroCostoEjecuta(Utilidades.convertirAEntero(mapa.get("centroCostoEjecuta_"+i)+""));
			dto.setCodigoPropietarioIncluido(mapa.get("codigoPropietarioIncluido_"+i)+"");
			dto.setCodigoPropietarioPpal(mapa.get("codigoPropietarioPpal_"+i)+"");
			dto.setDescCentroCostoEjecuta(mapa.get("descripcionCcEjecuta_"+i)+"");
			dto.setDescServicioIncluido(mapa.get("descripcionServicioIncl_"+i)+"");
			dto.setDescServicioPpal(mapa.get("descripcionServicioPpal_"+i)+"");
			dto.setServicioIncluido(Utilidades.convertirAEntero(mapa.get("servicioIncluido_"+i)+""));
			dto.setServicioPpal(Utilidades.convertirAEntero(mapa.get("servicioPpal_"+i)+""));
			//este no se agraga porque debe venir agrupado
			//dto.setSolicitudIncluida(Utilidades.convertirAEntero(mapa.get("solicitud_incluida_"+i)+""));
			dto.setSolicitudPpal(Utilidades.convertirAEntero(mapa.get("solicitudPpal_"+i)+""));
			dto.setCantidad(Utilidades.convertirAEntero(mapa.get("cantidad_"+i)+""));
			
			array.add(dto);
		}
		return array;
	}
	
	
	/**
	 * Carga el dto con los articulos incluidos dentro de una solicitud
	 * @param Connection con
	 * @param String numeroSolicitud
	 * */
	public static ArrayList<DtoArticuloIncluidoSolProc> cargarArticulosIncluidosSolicitudDto(Connection con, String numeroSolicitud)
	{
		ArrayList<DtoArticuloIncluidoSolProc> array = new ArrayList<DtoArticuloIncluidoSolProc>();
		DtoArticuloIncluidoSolProc dto;
		HashMap mapa = new HashMap();
		int numRegistros = 0;
		
		//Se consulta la informacion		
		mapa.put("numeroSolicitud",numeroSolicitud);		
		mapa = getRespuestaProcedimientosDao().cargarArticulosIncluidosSolicitud(con,mapa);
		numRegistros = Utilidades.convertirAEntero(mapa.get("numRegistros").toString());
		
		logger.info("mapaa cargarArticulosIncluidosSolicitudDto----------> "+numeroSolicitud);
		//Utilidades.imprimirMapa(mapa);
		
		for(int i = 0; i < numRegistros; i++)
		{
			dto = new DtoArticuloIncluidoSolProc();
			dto.setSolicitudPpal(Utilidades.convertirAEntero(mapa.get("solicitudPpal_"+i).toString()));
			dto.setConsecutivoOrdenMedPpal(Utilidades.convertirAEntero(mapa.get("ordenPpal_"+i).toString()));
			dto.setArticuloIncluido(Utilidades.convertirAEntero(mapa.get("articulo_"+i).toString()));		
			dto.setCodigoInterfazArtIncluido(mapa.get("codigoInterfaz_"+i).toString());			
			dto.setDescripcionArticuloIncluido(mapa.get("descripcionArticulo_"+i).toString());
			dto.setServicioPpal(Utilidades.convertirAEntero(mapa.get("servicioPpal_"+i).toString()));
			dto.setDescripcionServicioPpal(mapa.get("descripcionServicio_"+i).toString());
			dto.setCodigoEspecialidadServPpal(Utilidades.convertirAEntero(mapa.get("especialidadServicioPpal_"+i).toString()));			
			dto.setCantidad(Utilidades.convertirAEntero(mapa.get("cantidad_"+i).toString()));
			dto.setCantidadMaxima(Utilidades.convertirAEntero(mapa.get("cantidadMaxima_"+i).toString()));
			dto.setFarmacia(Utilidades.convertirAEntero(mapa.get("farmacia_"+i).toString()));
			dto.setDescripcionFarmacia(mapa.get("descripcionFarmacia_"+i).toString());
			dto.setEsServicioIncluido(UtilidadTexto.getBoolean(mapa.get("esServicioIncluido_"+i).toString()));
			dto.setEsPos(UtilidadTexto.getBoolean(mapa.get("esPos_"+i).toString()));
			dto.setTieneJusticacionNoPos(UtilidadTexto.getBoolean(mapa.get("tienejus_"+i).toString()));			
			dto.setSolicitudIncluida(Utilidades.convertirAEntero(mapa.get("solicitudIncluida_"+i).toString()));
			
			array.add(dto);
		}	
		
		return array;
	}
	
	
	/**
	 * Consulta la informacion de los servicios incluidos dentro de una solicitud
	 * @param Connection con 
	 * @param HashMap parametros
	 * */
	public static HashMap cargarServiciosIncluidosSolicitud(Connection con,String numeroSolicitud, int institucion)
	{
		HashMap parametros = new HashMap();
		parametros.put("numeroSolicitud",numeroSolicitud);
		
		int tarifario= Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion));
		if(tarifario<0)
			tarifario=ConstantesBD.codigoTarifarioCups;
		parametros.put("tarifario", tarifario);
		
		return getRespuestaProcedimientosDao().cargarServiciosIncluidosSolicitud(con,parametros);
	}
	
	//***********************************************************************************************************************
	//***********************************************************************************************************************
	
	/**
	 * Genera los cargos directos para los articulos incluidos dentro del procedimiento
	 * @param Connection con 
	 * @param UsuarioBasico usuario
	 * @param PersonaBasica paciente
	 * @param HashMap mapaArtIncl
	 * */
	public static HashMap generarCargosDirectosArticulosIncluidos(
			Connection con,			 
			UsuarioBasico usuario,
			PersonaBasica paciente,
			int numeroSolicitudExt,
			HashMap mapa) throws IPSException
	{
		ArrayList<DtoArticuloIncluidoSolProc> array = new ArrayList<DtoArticuloIncluidoSolProc>();
		DtoArticuloIncluidoSolProc dto;
		
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros").toString()); i++)
		{
			dto = new DtoArticuloIncluidoSolProc();
			dto.setSolicitudPpal(Utilidades.convertirAEntero(mapa.get("solicitudPpal_"+i).toString()));
			dto.setConsecutivoOrdenMedPpal(Utilidades.convertirAEntero(mapa.get("ordenPpal_"+i).toString()));
			dto.setArticuloIncluido(Utilidades.convertirAEntero(mapa.get("articulo_"+i).toString()));		
			dto.setCodigoInterfazArtIncluido(mapa.get("codigoInterfaz_"+i).toString());			
			dto.setDescripcionArticuloIncluido(mapa.get("descripcionArticulo_"+i).toString());
			dto.setServicioPpal(Utilidades.convertirAEntero(mapa.get("servicioPpal_"+i).toString()));
			dto.setDescripcionServicioPpal(mapa.get("descripcionServicio_"+i).toString());
			dto.setCodigoEspecialidadServPpal(Utilidades.convertirAEntero(mapa.get("especialidadServicioPpal_"+i).toString()));			
			dto.setCantidad(Utilidades.convertirAEntero(mapa.get("cantidad_"+i).toString()));
			dto.setCantidadMaxima(Utilidades.convertirAEntero(mapa.get("cantidadMaxima_"+i).toString()));
			dto.setFarmacia(Utilidades.convertirAEntero(mapa.get("farmacia_"+i).toString()));
			dto.setDescripcionFarmacia(mapa.get("descripcionFarmacia_"+i).toString());
			dto.setEsServicioIncluido(UtilidadTexto.getBoolean(mapa.get("esServicioIncluido_"+i).toString()));
			dto.setEsPos(UtilidadTexto.getBoolean(mapa.get("esPos_"+i).toString()));
			dto.setTieneJusticacionNoPos(UtilidadTexto.getBoolean(mapa.get("tienejus_"+i).toString()));			
			dto.setSolicitudIncluida(Utilidades.convertirAEntero(mapa.get("solicitudIncluida_"+i).toString()));
			
			array.add(dto);
		}
		
			return generarCargosDirectosArticulosIncluidos(
					con,
					usuario,
					paciente,
					numeroSolicitudExt,
					array,
					new HashMap(),
					new HashMap(),
					new HashMap(),
					new HashMap(),
					new HashMap());		
	}
	
	//***********************************************************************************************************************
	//***********************************************************************************************************************
	
	/**
	 * Genera los cargos directos para los articulos incluidos dentro del procedimiento
	 * @param Connection con 
	 * @param UsuarioBasico usuario
	 * @param PersonaBasica paciente
	 * @param ArrayList<DtoArticuloIncluidoSolProc> arrayArtIncl
	 * */
	public static HashMap generarCargosDirectosArticulosIncluidos(
			Connection con,			 
			UsuarioBasico usuario,
			PersonaBasica paciente,
			int numeroSolicitudExt,
			ArrayList<DtoArticuloIncluidoSolProc> arrayArtIncl,
			HashMap justificacionMap,
			HashMap medicamentosNoPosMap,
			HashMap medicamentosPosMap,
			HashMap sustitutosNoPosMap,
			HashMap diagnosticosDefinitivos) throws IPSException
	{
		boolean exito = true;
		HashMap respuesta = new HashMap();
		int contMensajes = 0;
		DtoArticuloIncluidoSolProc dtoArticulos;
		int numeroSolicitud = ConstantesBD.codigoNuncaValido;
		int exArticulo = ConstantesBD.codigoNuncaValido; 
		int resultado = ConstantesBD.codigoNuncaValido;
		int estadoSolicitud = ConstantesBD.codigoNuncaValido;		
		int codigoCentroCostoSolicitante = Solicitud.obtenerCodigoCentroCostoSolicitante(con,numeroSolicitudExt+"");
		
		try
		{
			//Carga informacion basica de la solicitud
			Solicitud sol = new Solicitud();
			sol.cargar(con,numeroSolicitudExt);
			estadoSolicitud = sol.getEstadoHistoriaClinica().getCodigo();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Se recorre todos los medicamentos insertados
		for(int i = 0 ; i < arrayArtIncl.size(); i++)
		{
			dtoArticulos = arrayArtIncl.get(i);
			
			logger.info("\n\n\n\n\n\n valores del articulo >> "+dtoArticulos.getDescripcionArticuloIncluido()+" >> "+dtoArticulos.getCantidad()+" "+dtoArticulos.getSolicitudIncluida());
			
			//Modifica las cantidades del articulo incluido
			Servicios_ArticulosIncluidosEnOtrosProcedimientos.modificarCantidadArticulosIncluidosSolicitudProcedimientos(
					con, 
					dtoArticulos.getSolicitudPpal(),
					dtoArticulos.getArticuloIncluido(), 
					dtoArticulos.getCantidad(), 
					usuario.getLoginUsuario());
			
			//Se valida que la cantidad del articulo sea mayor a cero y que no se hubiera generado ya un cargo
			if(dtoArticulos.getCantidad() > 0 
					&& dtoArticulos.getSolicitudIncluida() <= 0)
			{
				//1) Se inserta la solicitud general
				numeroSolicitud = insertarSolicitudGeneral(
						con, 
						UtilidadFecha.getFechaActual(), 
						ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos, 
						codigoCentroCostoSolicitante,
						dtoArticulos.getFarmacia(),
						/*"",*/
						usuario.getCodigoPersona(),
						Utilidades.convertirAEntero(paciente.getCuentasPacienteArray(0).getCodigoCuenta())
						);
				
				if(numeroSolicitud>0)
				{
					logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n***********************************************************************");
					logger.info("numero solicitud >> "+numeroSolicitud);
					//Actualiza el numero de solicitud incluida
					justificacionMap.put(dtoArticulos.getArticuloIncluido()+"_numeroSolicitudIncluida",numeroSolicitud);					
					//Modifica las Solicitudes del articulo incluido
					Servicios_ArticulosIncluidosEnOtrosProcedimientos.modificarSolicitudInclArticulosIncluidosSolicitudProcedimientos(
							con, 
							dtoArticulos.getSolicitudPpal(),
							dtoArticulos.getArticuloIncluido(), 
							numeroSolicitud, 
							usuario.getLoginUsuario());
					
					
					//**************Si se afectan inventarios se deben realizar las validaciones cuando no hay existencias negativas 
					if(!AlmacenParametros.manejaExistenciasNegativas(con,dtoArticulos.getFarmacia(), usuario.getCodigoInstitucionInt()))
			        {
						exArticulo = Integer.parseInt(UtilidadInventarios.getExistenciasXArticulo(dtoArticulos.getArticuloIncluido(),dtoArticulos.getFarmacia(),usuario.getCodigoInstitucionInt()));
				        if(dtoArticulos.getCantidad() > exArticulo)
			    		{
				        	exito = false;
				        	respuesta.put("mensaje_"+contMensajes,"el art�culo "+dtoArticulos.getDescripcionArticuloIncluido()+" con existencia "+exArticulo+" para el almacen "+dtoArticulos.getDescripcionFarmacia()+" es insuficiente");
				        	contMensajes++;
			    		}						
			        }
										
					//****************************************************************************************************************
					if(exito)
			    	{					
						//*********SE INSERTA UNA SOLICITUD DE MEDICAMENTOS B�SICA***************************************
						SolicitudMedicamentos objetoSolicitudMedicamentos= new SolicitudMedicamentos();
					    objetoSolicitudMedicamentos.setNumeroSolicitud(numeroSolicitud);
					    objetoSolicitudMedicamentos.setObservacionesGenerales("");
					    objetoSolicitudMedicamentos.setCentroCostoPrincipal(dtoArticulos.getFarmacia()+"");
					    dtoArticulos.setSolicitudIncluida(numeroSolicitud);
					    resultado=objetoSolicitudMedicamentos.insertarUnicamenteSolMedicamentosTransaccional(con);
						//***********************************************************************************************
					    
					    if(resultado>0)
					    {
					    	//*********SE INSERTA EL DETALLE DE LA SOLICITUD DE MEDICAMENTOS*********************
					    	resultado = insertarDetalleSolicitudMedicamentos(con,numeroSolicitud,dtoArticulos,usuario.getCodigoInstitucionInt());
					    	//************************************************************************************
					    	
					    	if(resultado>0)
					    	{
				    			//**********SE INSERTA EL DESPACHO DE MEDICAMENTOS BASICO*******************************
				    			resultado = insertarDespachoMedicamentosBasico(con,numeroSolicitud,usuario);
				    			//**************************************************************************************
				    			
				    			if(resultado>0)
				    			{
				    				//*********SE INSERTA EL DETALLE DEL DESPACHO DE MEDICAMENTOS*****************************
				    				resultado = insertarDetalleDespachoMedicamentos(con,resultado,dtoArticulos,usuario); //resultado es el codigo del despacho
				    				//*****************************************************************************************
				    				
				    				if(resultado>0)
				    				{
				    					//*******SE ACTUALIZA EL ESTADO M�DICO DE LA SOLICITUD DE MEDICAMENTOS*********************
				    					resultado = cambiarEstadoMedicoSolicitudMedicamentos(con,numeroSolicitud/*,""*/);
				    					//*****************************************************************************************
				    					
				    					if(resultado>0)
				    					{
				    						//Se modifico por la Tarea 59745, con la intenci�n de devolver el codigo del convenio
				    						//*****SE GENERA LA INFORMACI�N DE CARGO Y SUBCUENTA******************************
				    						DtoDetalleCargo dtoCargo= generarInfoSubCuentaCargoMedicamentos(con,numeroSolicitud,codigoCentroCostoSolicitante,paciente,/*"",*/usuario,dtoArticulos);
				    						resultado = dtoCargo==null?0:1;
				    						//*******************************************************************************
				    						
				    						if(resultado>0)
				    						{
				    							//******SE GENERA LA INFORMACION DE CARGOS DIRECTOS**************************
				    							CargosDirectos cargo= new CargosDirectos();
				    						    cargo.llenarMundoCargoDirecto(numeroSolicitud,usuario.getLoginUsuario(),ConstantesBD.codigoTipoRecargoSinRecargo,ConstantesBD.codigoNuncaValido,"",true,"");
				    						    
				    						    resultado=cargo.insertar(con);
				    							//**************************************************************************
				    							
				    						    if(resultado>0)
				    						    {
				    						    	//**********SE ACTUALIZAN LAS EXISTENCIAS DEL ART�CULO*********************
				    						    	resultado = actualizarExistenciasArticulosAlmacen(con,dtoArticulos,dtoArticulos.getFarmacia(),/*"",*/usuario);
				    						    	//***************************************************************************
				    						    	
				    						    	if(resultado > 0)
				    						    	{
				    						    		if(UtilidadJustificacionPendienteArtServ.validarNOPOS(con, numeroSolicitud,dtoArticulos.getArticuloIncluido(),true,false, dtoCargo.getCodigoConvenio()))
				    						        	{
				    								       	//Pendiente la generacion de la justificacion... para la solicitud en estado en proceso (estadoSolicitud) se genera las justificaciones en estado pendiente
				    						    			/*
				    						    			 * En ning�n documento se especifica que se debe validar el estado de la solicitud
				    						    			 */
				    						    			//logger.info("Aqui est� el error en la validaci�n de los estados -- pendiente documentaci�n "+estadoSolicitud);
				    								       	if(estadoSolicitud == ConstantesBD.codigoEstadoHCEnProceso)
				    								       	{
				    							        		if(!UtilidadJustificacionPendienteArtServ.insertarJusNP(
				    							        				con,
				    							        				numeroSolicitud,
				    							        				dtoArticulos.getArticuloIncluido(),
				    							        				dtoArticulos.getCantidad(),
				    							        				usuario.getLoginUsuario(),
				    							        				true,
				    							        				false,
				    							        				Utilidades.convertirAEntero(dtoCargo.getCodigoSubcuenta()+""),""))				    							        	
				    							        			logger.error("No se pudo insertar la Justificacion No Pos del Articulo con codigo >>"+dtoArticulos.getArticuloIncluido());				    							        		
				    								       	}
				    						        	}
				    								    else
				    								    {
				    								    	logger.info("no se ejecuta la justificacion");
				    								    }
				    						    	}
				    						    	else
				    						    	{
				    						    		exito = false;
				    						    	}
				    						    } //Fin if validacion inserci�n del cargo directo de articulos			    							
				    						} //Fin if validacion inserci�n cargo del despacho de medicamentos
				    						else
				    							exito = false;
				    					}
				    				} //Fin if validacion inserci�n detalle del despacho de medicamentos
				    				else
				    					exito = false;
				    			} //Fin if validacion inserci�n despacho de medicamentos
				    			else
				    				exito = false;
					    		
					    	} //Fin if validacion inserci�n detalle solicitud medicamentos
					    	else
					    		exito = false;					    	
					    } //Fin if validacion inserci�n solicitud medicamentos b�sica
					    else
				    		exito = false;
			    	}
					else
			    		exito = false;						
				} //Fin if validacion al generar solicitud
				else
					exito = false;
			}				
		}
		
		if(exito 
			&& estadoSolicitud != ConstantesBD.codigoEstadoHCEnProceso)
		{
			//Inserta la informaci�n de la justificacion NO POS			
			formatoJustificacionNoPos(
					con,
					arrayArtIncl,
					justificacionMap,
					medicamentosNoPosMap,
					medicamentosPosMap,
					sustitutosNoPosMap,
					diagnosticosDefinitivos,
					usuario);
		}
				
		respuesta.put("contMensajes",contMensajes);
		respuesta.put("exito",exito);		
		
		logger.info("valor del mapa respuesta >> "+respuesta);
		return respuesta;
	}
		
	//***********************************************************************************************************************
	//***********************************************************************************************************************
		
	/**
	 * M�todo para insertar una solicitud estancia en la tabla solicitudes
	 * @param con
	 * @param estancia
	 * @param pos
	 * @param mapaCuentas
	 * @return
	 */
	private static int insertarSolicitudGeneral(
			Connection con,
			String fechaSolicitud,
			int tipoSolicitud,
			int codigoCentroCostoSolicitante,
			int codigoCentroCostoSolicitado,
			/*String numeroAutorizacion,*/
			int codigoMedicoResponde,
			int cuenta) 
	{
		//se instancia el objeto solicitud
		Solicitud solicitud=new Solicitud();
		solicitud.clean();
		solicitud.setFechaSolicitud(fechaSolicitud);
		solicitud.setHoraSolicitud(UtilidadFecha.getHoraActual());
		solicitud.setTipoSolicitud(new InfoDatosInt(tipoSolicitud));
		solicitud.setEspecialidadSolicitante(new InfoDatosInt(ConstantesBD.codigoEspecialidadMedicaNinguna));
		solicitud.setOcupacionSolicitado(new InfoDatosInt(ConstantesBD.codigoOcupacionMedicaNinguna));
		solicitud.setCentroCostoSolicitante(new InfoDatosInt(codigoCentroCostoSolicitante));
		solicitud.setCentroCostoSolicitado(new InfoDatosInt(codigoCentroCostoSolicitado));
	    solicitud.setCodigoCuenta(cuenta);
	    //solicitud.setNumeroAutorizacion(numeroAutorizacion);
	    solicitud.setCobrable(true);
	    solicitud.setVaAEpicrisis(false);
	    solicitud.setUrgente(false);
	    
	    try
	    {
	    	
	    	//1) Se inserta la solicitud
	        int numeroSolicitud=solicitud.insertarSolicitudGeneralTransaccional(con, ConstantesBD.continuarTransaccion);
	        
	        //2) Se cambia el estado de la solicitud de cargo directo
	        if(solicitud.cambiarEstadosSolicitud(con, numeroSolicitud, 0, ConstantesBD.codigoEstadoHCCargoDirecto).isTrue())
	        {
	        	UsuarioBasico medico = new UsuarioBasico();
	        	medico.setCodigoPersona(codigoMedicoResponde);
	        	medico.cargarUsuarioBasico(con, codigoMedicoResponde);
	        	
	        	//3) Se actualiza el m�dico que responde
	        	int resp = solicitud.actualizarMedicoRespondeTransaccional(con, numeroSolicitud, medico, ConstantesBD.continuarTransaccion);
	        	
	        	if(resp<=0)
		        	return 0;
		        else
		        	return numeroSolicitud;
	        }
	        
	        
	    }
	    catch(SQLException sqle)
	    {
	        logger.warn("Error en la transaccion del insert en la solicitud b�sica");
			
	    }
	    return 0;
	}
	
	//***********************************************************************************************************************
	//***********************************************************************************************************************	
	
	/**
	 * M�todo implementado para insertar el detalle de una solicitud de medicamentos
	 * @param numeroSolicitud
	 * @param articulos
	 * @param tipoArchivo 
	 * @return
	 */
	private static int insertarDetalleSolicitudMedicamentos(
			Connection con,
			int numeroSolicitud, 
			DtoArticuloIncluidoSolProc dtoArticulos, 
			int institucion) 
	{	
		SolicitudMedicamentos objetoSolicitudMedicamentos= new SolicitudMedicamentos();    
        if(objetoSolicitudMedicamentos.insertarUnicamenteDetalleSolicitudMedicamentos(
				            	con, 
				            	numeroSolicitud, 
				            	dtoArticulos.getArticuloIncluido(), 
				            	ValoresPorDefecto.getNumDiasTratamientoMedicamentos(institucion),
				            	0) < 1)
        	return ConstantesBD.codigoNuncaValido;       
	    
	    return 1;
	}
	
	//***********************************************************************************************************************
	//***********************************************************************************************************************
		
	/**
	 * M�todo para insertar el despacho de medicamentos b�sico
	 * @param Connection con
	 * @param numeroSolicitud
	 * @param UsuarioBasico usuario 
	 * @return
	 */
	private static int insertarDespachoMedicamentosBasico(Connection con,int numeroSolicitud,UsuarioBasico usuario) 
	{
		int codigoDespacho=ConstantesBD.codigoNuncaValido;
	    DespachoMedicamentos despacho = new DespachoMedicamentos();
	    despacho.setUsuario(usuario.getLoginUsuario());
	    despacho.setNumeroSolicitud(numeroSolicitud);
	    //este se puso en true para que pudiera generar el cargo, pero en realidad debia ser false, 
	    //esto se acordo con Margarita y Nury el 2005-07-01
	    despacho.setEsDirecto(true);
	    try
	    {
	        codigoDespacho=despacho.insertarDespachoBasicoUnicamenteTransaccional(con, ConstantesBD.continuarTransaccion);
	    }
	    catch(SQLException sqle)
	    {
	        logger.warn("Error en el insert del despacho b�sico" );
	        codigoDespacho = ConstantesBD.codigoNuncaValido;
	    }   
	    
	    return codigoDespacho;
	}
	
	//***********************************************************************************************************************
	//***********************************************************************************************************************
	
	/**
	 * M�todo implementado para insertar el detalle del despacho de medicamentos
	 * @param Connection con
	 * @param codigoDespacho
	 * @param dtoArticulos
	 * @param usuario 
	 * @return
	 */
	private static int insertarDetalleDespachoMedicamentos(
			Connection con,
			int codigoDespacho,
			DtoArticuloIncluidoSolProc dtoArticulos, UsuarioBasico usuario) throws IPSException 
	{	    
	    DespachoMedicamentos despacho= new DespachoMedicamentos();
	    int resp=ConstantesBD.codigoNuncaValido, resultado = 1;
	    //boolean insertoBienExcepcionFarmacia=false;
	       
        try
        {           
            resp=despacho.insertarDetalleDespachoUnicamenteTransaccional(		
            		con, 
            		ConstantesBD.continuarTransaccion, 
            		dtoArticulos.getArticuloIncluido(), 
            		dtoArticulos.getArticuloIncluido(), 
					codigoDespacho, 
					dtoArticulos.getCantidad(), 
					"","","","","","");   
            
            if(resp>0)
            {
            	//*****************GENERACI�N TARIFA ENTIDAD SUBCONTRATADA********************************
            	CargosEntidadesSubcontratadas cargos = new CargosEntidadesSubcontratadas();
            	cargos.generarCargoArticulo(
            		con, 
            		dtoArticulos.getFarmacia(), 
            		dtoArticulos.getArticuloIncluido(), 
            		ConstantesBD.codigoNuncaValido, 
            		dtoArticulos.getSolicitudIncluida()+"", 
            		"", 
            		UtilidadFecha.getFechaActual(con), 
            		UtilidadFecha.getHoraActual(con), 
            		false, 
            		usuario,"","");
            	
            	if(!cargos.getErroresProceso().isEmpty())
            	{
            		resp = 0;
            	}
            	//**************************************************************************
            	
            }
            	
        }
        catch(SQLException e)
        {
            resultado = 0;            
        }            
	      
	   	return resultado; 
	}
	

	//***********************************************************************************************************************
	//***********************************************************************************************************************
	
	/**
	 * M�todo implementado para cambiar el estado m�dico de la solicitud de medicamentos
	 * @param Connection con
	 * @param numeroSolicitud
	 * @param numeroAutorizacion 
	 * @return
	 */
	private static int cambiarEstadoMedicoSolicitudMedicamentos(Connection con,int numeroSolicitud/*, String numeroAutorizacion*/) 
	{
		int i=0;
	    int inserto= ConstantesBD.codigoNuncaValido;
	    DespachoMedicamentos despacho= new DespachoMedicamentos();
	    despacho.setNumeroSolicitud(numeroSolicitud);
	    
	    try
	    {
	       inserto =despacho.cambiarEstadoMedicoSolicitudTransaccional(con, ConstantesBD.continuarTransaccion, ConstantesBD.codigoEstadoHCCargoDirecto/*, numeroAutorizacion*/); 
	    }
	    catch(SQLException sqle)
	    {
	        logger.warn("Error en el insert del cambiar esatdo de la solicitud transaccional con indice ="+i +"   error-->"+sqle);
	        inserto = 0;
	    }
	    
		return inserto;
	}

	//***********************************************************************************************************************
	//***********************************************************************************************************************
	
	
	/**
	 * M�todo implementado para generar el cargo de medicamentos
	 * @param Connection con
	 * @param numeroSolicitud
	 * @param codigoCentroCostoSolicitante
	 * @param PersonaBasica paciente
	 * @param String numeroAutorizacion
	 * @param UsuarioBasico usuario
	 * @param DtoArticuloIncluidoSolProc dtoArticulos
	 * @return
	 */
	//Se modifico el m�todo por la Tarea 59745, devolviendo el DtoDetalleCargo
	private static DtoDetalleCargo generarInfoSubCuentaCargoMedicamentos(
			Connection con,			
			int numeroSolicitud,
			int codigoCentroCostoSolicitante,
			PersonaBasica paciente,
			/*String numeroAutorizacion,*/
			UsuarioBasico usuario,
			DtoArticuloIncluidoSolProc dtoArticulos) throws IPSException 
	{		    
        try
        {             
    	    Cargos cargoArticulos= new Cargos();
    	        	    
    	    if(cargoArticulos.generarSolicitudSubCuentaCargoArticulosEvaluandoCobertura(
    	    		con, 
					usuario, 
					paciente, 
					numeroSolicitud, 
					dtoArticulos.getArticuloIncluido(),
					dtoArticulos.getCantidad(),
					false/*dejarPendiente*/, 
					ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos/*codigoTipoSolicitudOPCIONAL*/, 
					Utilidades.convertirAEntero(paciente.getCuentasPacienteArray(0).getCodigoCuenta()),					 
					codigoCentroCostoSolicitante, 
					ConstantesBD.codigoNuncaValidoDouble /*valorTarifaOPCIONAL*/,
					false,
					UtilidadFecha.getFechaActual(),false /*tarifaNoModificada*/))  	    
    	    {
    			return cargoArticulos.getDtoDetalleCargo();
    		}
    	    
    	}
        catch(NumberFormatException e)
        {         
            e.printStackTrace();
        }      
	    
	    return null;
	}
		
	//***********************************************************************************************************************
	//***********************************************************************************************************************
		
	/**
	 * M�todo implementado para actualizar las existencias de los articulo por almacen
	 * @param articulos
	 * @param codigoFarmacia
	 * @param numeroAutorizacion 
	 * @param tipoArchivo 
	 * @return
	 */
	private static int actualizarExistenciasArticulosAlmacen(
			Connection con,
			DtoArticuloIncluidoSolProc dtoArticulos, 
			int codigoFarmacia, 
			/*String numeroAutorizacion,*/			
			UsuarioBasico usuario) 
	{				
        try
        {
            if(!UtilidadInventarios.actualizarExistenciasArticuloAlmacenTransaccional(  
					            	con, 
					            	dtoArticulos.getArticuloIncluido(),                  
					                codigoFarmacia, 
					                false, 
					                dtoArticulos.getCantidad(),                 
					                usuario.getCodigoInstitucionInt(), 
					                ConstantesBD.continuarTransaccion ))
            	return 0;	
            
        }       
        catch(SQLException sqle)
        {          
            return 0;
        }
        
        return 1;        
	}
	

	//***********************************************************************************************************************
	//***********************************************************************************************************************	
	
	public static boolean generarCargosDirectosArtIncluidosHojasQA(
			Connection con,
			UsuarioBasico usuario,
			PersonaBasica paciente,
			int numeroSolicitud,
			ArrayList<DtoArticuloIncluidoSolProc> array,
			HashMap justificacionMap,
			HashMap medicamentosNoPosMap,
			HashMap medicamentosPosMap,
			HashMap sustitutosNoPosMap,
			HashMap diagnosticosDefinitivos,			
			boolean finalizar) throws IPSException
	{
		
		@SuppressWarnings("unused")
		boolean result = true;
		HashMap respuesta = new HashMap();
		respuesta.put("exito",ConstantesBD.valorTrueEnString);
		
		if(finalizar)
		{
			//Realiza las solicitudes de cargos directos de articulos Incluidos
			respuesta = RespuestaProcedimientos.generarCargosDirectosArticulosIncluidos(
								con,
								usuario,
								paciente,
								numeroSolicitud,
								array,
								justificacionMap,
								medicamentosNoPosMap,
								medicamentosPosMap,
								sustitutosNoPosMap,
								diagnosticosDefinitivos);
		}
		else
		{
			for(int i = 0; i < array.size(); i++)
			{
				//Modifica las cantidades del articulo incluido
				Servicios_ArticulosIncluidosEnOtrosProcedimientos.modificarCantidadArticulosIncluidosSolicitudProcedimientos(
						con, 
						array.get(i).getSolicitudPpal(),
						array.get(i).getArticuloIncluido(), 
						array.get(i).getCantidad(), 
						usuario.getLoginUsuario());
				
				//Anula las solicitudes creadas 
				if(array.get(i).getSolicitudIncluida() > 0)
				{					
					if(Solicitud.cambiarEstadosSolicitudStatico(
							con,
							array.get(i).getSolicitudIncluida(),
							ConstantesBD.codigoEstadoFacturacionAnulada,
							ConstantesBD.codigoEstadoHCAnulada
							).isTrue())
					{					
						//Modifica las Solicitudes del articulo incluido
						Servicios_ArticulosIncluidosEnOtrosProcedimientos.modificarSolicitudInclArticulosIncluidosSolicitudProcedimientos(
								con, 
								array.get(i).getSolicitudPpal(),
								array.get(i).getArticuloIncluido(), 
								ConstantesBD.codigoNuncaValido, 
								usuario.getLoginUsuario());
					}
					else
					{
						logger.info("no se logro anular el estado de la solicitud de cargo directo >> "+array.get(i).getSolicitudIncluida());
						return false;
						
					}						
				}
			}
		}
		
		if(!UtilidadTexto.getBoolean(respuesta.get("exito").toString()))
		{										
			logger.info("\n\n #################################### No Se genero con exito los cargos directos \n\n");
			result = false;
		}
		else							
			logger.info("\n\n #################################### Se genero con exito los cargos directos \n\n");
		
		return true;		
	}
	
	
	
	//***********************************************************************************************************************
	//***********************************************************************************************************************
	
	/**
	 * Validaciones para los articulos incluidos
	 * @param Connection con
	 * @param ActionErrors errors
	 * @param ArrayList<DtoArticuloIncluidoSolProc> array
	 * */
	public static ActionErrors valicacionesArticulosIncluidosProc(			 
			ActionErrors errors, 
			ArrayList<DtoArticuloIncluidoSolProc> array,
			HashMap mapaJustificacionMap)	
	{		
		for(int i = 0; i < array.size(); i++)
		{
			//validacion de la justificacion NoPos			
			if(mapaJustificacionMap != null)
			{				
				if(!array.get(i).isEsPos() 
						&& array.get(i).getCantidad() > 0 
							&& array.get(i).getSolicitudIncluida() <=0)
    			{					
    				if(!mapaJustificacionMap.containsKey(array.get(i).getArticuloIncluido()+"_yajustifico") 
    						|| mapaJustificacionMap.get(array.get(i).getArticuloIncluido()+"_yajustifico").equals("false"))
    				{    					
    					errors.add("", new ActionMessage("errors.required", "La justificacion no pos del articulo "+array.get(i).getDescripcionArticuloIncluido()));        				
        			}
    			}
			}
			//************************************************************//
		}		
	
		return errors;
	}
	
	//***********************************************************************************************************************
	//***********************************************************************************************************************
	
	
	/**
	 * Guarda la informacion de la justificacion de los medicamentos
	 * @param Connection con
	 * @param ArrayList<DtoArticuloIncluidoSolProc> array
	 * @param HashMap mapaJustificacionMap
	 * @param UsuarioBasico usuario
	 * */
	 public static void formatoJustificacionNoPos(
			Connection con,
			ArrayList<DtoArticuloIncluidoSolProc> array,			
			HashMap justificacionMap,
			HashMap medicamentosNoPosMap,
			HashMap medicamentosPosMap,
			HashMap sustitutosNoPosMap,
			HashMap diagnosticosDefinitivos,	
			UsuarioBasico usuario) throws IPSException
	 {
		FormatoJustArtNopos fjan=new FormatoJustArtNopos();
		HashMap mapaTmp = new HashMap();
		mapaTmp.put("numRegistros","0");		
		
		logger.info("Tama�o del registro "+array.size());
		
		for(int w = 0; w < array.size(); w++ )
		{
			logger.info("es pos:"+array.get(w).isEsPos()+" cant:"+array.get(w).getCantidad()+" inc:"+array.get(w).getSolicitudIncluida());
			//Mt 6814 Seccion Mapas
			justificacionMap.put(array.get(w).getArticuloIncluido()+"_mapasecciones",justificacionMap.get("mapasecciones"));
			if(!array.get(w).isEsPos()
					&& array.get(w).getCantidad() > 0)
			{
				if(!UtilidadTexto.getBoolean(justificacionMap.get(array.get(w).getArticuloIncluido()+"_pendiente")) && array.get(w).getSolicitudIncluida()<=0)
				{
					logger.info("Entra a 2");
					{
						logger.info("Entra a 0--false");
						double subcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(con, array.get(w).getArticuloIncluido(), array.get(w).getSolicitudIncluida(), ConstantesBD.codigoNuncaValido, true);
						
						UtilidadJustificacionPendienteArtServ.insertarJusNP(
								con,
								array.get(w).getSolicitudIncluida(),
								array.get(w).getArticuloIncluido(),
								array.get(w).getCantidad(),
								usuario.getLoginUsuario(),
								true,
								false,
								Utilidades.convertirAEntero(subcuenta+""),"");	
					}
				}
				else
				{
					logger.info("Entra a 3");
	
					if(UtilidadTexto.getBoolean(justificacionMap.get(array.get(w).getArticuloIncluido()+"_pendiente")))
					{
						logger.info("Entra a 1--true");
						//frecuencia estaba en 0 int
						
						fjan.insertarJustificacion(	con,
													Utilidades.convertirAEntero(justificacionMap.get(array.get(w).getArticuloIncluido()+"_numeroSolicitudIncluida").toString()),
													ConstantesBD.codigoNuncaValido,
													justificacionMap,
													medicamentosNoPosMap,
													medicamentosPosMap,
													sustitutosNoPosMap,
													diagnosticosDefinitivos,
													array.get(w).getArticuloIncluido(),
													usuario.getCodigoInstitucionInt(), 
													"",
													ConstantesBD.continuarTransaccion,
													array.get(w).getArticuloIncluido(),
													"", 
													array.get(w).getCantidad()+"", 
													"", 
													0, 
													"", 
													"", 
													array.get(w).getCantidad(), 
													"0",
													usuario.getLoginUsuario()
													);
					}
				}
			}
		}
	 }
	
	//***********************************************************************************************************************
	//***********************************************************************************************************************
	 
	 /**
	  * Realiza las validaciones para el ingreso a la funcionalidad de respuesta de procedimientos
	  * entidades subcontratadas
	  * @param Connection con
	  * @param UsuarioBasico usuario 
	  * */
	 public static ActionErrors validacionesIngresoRespProcEntiSub(Connection con,UsuarioBasico usuario)
	 {
		String tipoEntidadEjecuta = UtilidadesManejoPaciente.obtenerTipoEntidadEjecutaCentroCosto(con,usuario.getCodigoCentroCosto());
		ActionErrors errores = new ActionErrors();
		
		logger.info("tipoEntidadEjecuta "+tipoEntidadEjecuta);
		
		if(tipoEntidadEjecuta.equals(ConstantesIntegridadDominio.acronimoExterna) 
				|| tipoEntidadEjecuta.equals(ConstantesIntegridadDominio.acronimoAmbos))
		{
			ArrayList<DtoEntidadSubcontratada> array = CargosEntidadesSubcontratadas.obtenerEntidadesSubcontratadasCentroCosto(con,usuario.getCodigoCentroCosto(),usuario.getCodigoInstitucionInt());
			
			if(array.size() <= 0)
			{
				errores.add("descripcion",new ActionMessage("errors.notEspecific","Centro de Costo no tiene Asociadas Entidades Subcontratadas. Por Favor Verifique"));
			}
			else
			{
				boolean validacion = false;
				
				for(int i = 0; i<array.size(); i++){
					logger.info("ENTIDAD "+i);
					if(UtilidadesOrdenesMedicas.tieneUsuarioEntidadSubcontratada(array.get(i).getConsecutivo(),usuario.getLoginUsuario()))
						validacion = true;
				}	
				
				logger.info("validadcion "+validacion);
				
				if(validacion)
				{
					if(!UtilidadValidacion.esProfesionalSalud(usuario))
					{
						validacion = false;
						
						for(int i = 0; i<array.size() && !validacion; i++)						
							validacion = array.get(i).isRespOtrosUsua();
						
						if(!validacion)
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Usuario no es Profesional de la Salud y no tiene permisos para Responder Procedimientos del Centro de Costo. Por Favor Verifique"));
						}
					}
				}
				else
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Usuario sin permiso para la Entidad Subcontratada. Por Favor Verifique"));					
				}
			}
		}
		else
		{
			errores.add("descripcion",new ActionMessage("errors.notEspecific","Opci�n para responder Procedimientos Entidades Subcontratadas. Por Favor Verifique"));
		}
		
		return errores;
	 }
	 
	 /**
	 * Actualiza el campo de Servicio Respuesta Procedimiento
	 * @param Connection con
	 * @param String codigoRespuestaProc
	 * @param String codigoCxServicio
	 * */
	public static boolean actualizarCodigoCxServicioRespProc (Connection con,String codigoRespuestaProc, String codigoCxServicio)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigoCxServicio", codigoCxServicio);
		parametros.put("codigoPk", codigoRespuestaProc);
		
		return getRespuestaProcedimientosDao().actualizarCodigoCxServicioRespProc(con, parametros); 
	}
	
	/**
	 * 
	 * @return
	 */
	private int getNumeroSolicitudInt() 
	{
		return Integer.parseInt(this.getNumeroSolicitud());
	}

	/** 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String cargarResultadosAnteriores (Connection con, String numeroSolicitud, String codigoCxServ)
	{
		HashMap parametros = new HashMap();
		parametros.put("numeroSolicitud",numeroSolicitud);
		parametros.put("codigoCxServ",codigoCxServ);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRespuestaProcedimientosDao().cargarResultadosAnteriores(con,parametros);
	}
	
	
	/**
	 * @return Returns the datosHistoriaClinica.
	 */
	public HashMap getDatosHistoriaClinica() {
		return datosHistoriaClinica;
	}

	/**
	 * @param datosHistoriaClinica The datosHistoriaClinica to set.
	 */
	public void setDatosHistoriaClinica(HashMap datosHistoriaClinica) {
		this.datosHistoriaClinica = datosHistoriaClinica;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap datosHistoriaClinica (Connection con, String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRespuestaProcedimientosDao().datosHistoriaClinica(con, numeroSolicitud);
	}
	
	
	/**
	 * @return Returns the resultadosAnteriores.
	 */
	public String getResultadosAnteriores() {
		return resultadosAnteriores;
	}

	/**
	 * @param resultadosAnteriores The resultadosAnteriores to set.
	 */
	public void setResultadosAnteriores(String resultadosAnteriores) {
		this.resultadosAnteriores = resultadosAnteriores;
	}

	/**
	 * @return Returns the solicitudProcedimientos.
	 */
	public SolicitudProcedimiento getSolicitudProcedimiento() {
		return solicitudProcedimiento;
	}

	/**
	 * @param solicitudProcedimientos The solicitudProcedimientos to set.
	 */
	public void setSolicitudProcedimiento(
			SolicitudProcedimiento solicitudProcedimiento) {
		this.solicitudProcedimiento = solicitudProcedimiento;
	}

	/**
	 * @return Returns the numeroSolicitud.
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud The numeroSolicitud to set.
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return Returns the codigoRespuesta.
	 */
	public String getCodigoRespuesta() {
		return codigoRespuesta;
	}

	/**
	 * @param codigoRespuesta The codigoRespuesta to set.
	 */
	public void setCodigoRespuesta(String codigoRespuesta) {
		this.codigoRespuesta = codigoRespuesta;
	}

	/**
	 * @return Returns the codigoTipoRecargo.
	 */
	public int getCodigoTipoRecargo() {
		return codigoTipoRecargo;
	}

	/**
	 * @param codigoTipoRecargo The codigoTipoRecargo to set.
	 */
	public void setCodigoTipoRecargo(int codigoTipoRecargo) {
		this.codigoTipoRecargo = codigoTipoRecargo;
	}

	/**
	 * @return Returns the comentariosHistoriaClinica.
	 */
	public String getComentariosHistoriaClinica() {
		return comentariosHistoriaClinica;
	}

	/**
	 * @param comentariosHistoriaClinica The comentariosHistoriaClinica to set.
	 */
	public void setComentariosHistoriaClinica(String comentariosHistoriaClinica) {
		this.comentariosHistoriaClinica = comentariosHistoriaClinica;
	}

	/**
	 * @return Returns the diagnosticos.
	 */
	public HashMap getDiagnosticos() {
		return diagnosticos;
	}

	/**
	 * @param diagnosticos The diagnosticos to set.
	 */
	public void setDiagnosticos(HashMap diagnosticos) {
		this.diagnosticos = diagnosticos;
	}

	/**
	 * @return Returns the fechaEjecucion.
	 */
	public String getFechaEjecucion() {
		return fechaEjecucion;
	}

	/**
	 * @param fechaEjecucion The fechaEjecucion to set.
	 */
	public void setFechaEjecucion(String fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}

	/**
	 * @return Returns the finalizar.
	 */
	public String getFinalizar() {
		return finalizar;
	}

	/**
	 * @param finalizar The finalizar to set.
	 */
	public void setFinalizar(String finalizar) {
		this.finalizar = finalizar;
	}

	/**
	 * @return Returns the horaEjecucion.
	 */
	public String getHoraEjecucion() {
		return horaEjecucion;
	}

	/**
	 * @param horaEjecucion The horaEjecucion to set.
	 */
	public void setHoraEjecucion(String horaEjecucion) {
		this.horaEjecucion = horaEjecucion;
	}

	/**
	 * @return Returns the numDiagnosticos.
	 */
	public int getNumDiagnosticos() {
		return numDiagnosticos;
	}

	/**
	 * @param numDiagnosticos The numDiagnosticos to set.
	 */
	public void setNumDiagnosticos(int numDiagnosticos) {
		this.numDiagnosticos = numDiagnosticos;
	}

	/**
	 * @return Returns the objetoDao.
	 */
	public RespuestaProcedimientosDao getObjetoDao() {
		return objetoDao;
	}

	/**
	 * @param objetoDao The objetoDao to set.
	 */
	public void setObjetoDao(RespuestaProcedimientosDao objetoDao) {
		this.objetoDao = objetoDao;
	}

	/**
	 * @return Returns the observaciones.
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones The observaciones to set.
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return Returns the resultados.
	 */
	public String getResultados() {
		return resultados;
	}

	/**
	 * @param resultados The resultados to set.
	 */
	public void setResultados(String resultados) {
		this.resultados = resultados;
	}

	/**
	 * @return Returns the documentosAdjuntos.
	 */
	public DocumentosAdjuntos getDocumentosAdjuntos() {
		return documentosAdjuntos;
	}

	/**
	 * @param documentosAdjuntos The documentosAdjuntos to set.
	 */
	public void setDocumentosAdjuntos(DocumentosAdjuntos documentosAdjuntos) {
		this.documentosAdjuntos = documentosAdjuntos;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitudInt
	 * @return
	 */
	public static String validacionCita(Connection con, int numeroSolicitudInt) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRespuestaProcedimientosDao().validacionCita(con, numeroSolicitudInt);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoResputa
	 * @param observacionesRes
	 */
	public static boolean actualizarObservacionesRespuesta(Connection con,String codigoResputa, String observacionesRes) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRespuestaProcedimientosDao().actualizarObservacionesRespuesta(con,codigoResputa,observacionesRes);
		
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean servicioRequiereInterpretacion(Connection con, int numeroSolicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRespuestaProcedimientosDao().servicioRequiereInterpretacion(con, numeroSolicitud);
	}
	
	/**
	 * M�todo para eliminar las respuestas de procedimientos de una solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean eliminarRespuestaProcedimientos(Connection con,int numeroSolicitud,int codigoCirugia)
	{
		HashMap campos = new HashMap();
		campos.put("numeroSolicitud", numeroSolicitud);
		campos.put("codigoCirugia", codigoCirugia);
		return getRespuestaProcedimientosDao().eliminarRespuestaProcedimientos(con, campos);
	}

	public String getPortatil() {
		return portatil;
	}

	public void setPortatil(String portatil) {
		this.portatil = portatil;
	}

	/**
	 * @return the especialidadSolicitada
	 */
	public String getEspecialidadSolicitada() {
		return especialidadSolicitada;
	}

	/**
	 * @param especialidadSolicitada the especialidadSolicitada to set
	 */
	public void setEspecialidadSolicitada(String especialidadSolicitada) {
		this.especialidadSolicitada = especialidadSolicitada;
	}

	/**
	 * @return the nomEspecialidadSolicitada
	 */
	public String getNomEspecialidadSolicitada() {
		return nomEspecialidadSolicitada;
	}

	/**
	 * @param nomEspecialidadSolicitada the nomEspecialidadSolicitada to set
	 */
	public void setNomEspecialidadSolicitada(String nomEspecialidadSolicitada) {
		this.nomEspecialidadSolicitada = nomEspecialidadSolicitada;
	}

	public int getFinalidad() {
		return finalidad;
	}

	public void setFinalidad(int finalidad) {
		this.finalidad = finalidad;
	}
}