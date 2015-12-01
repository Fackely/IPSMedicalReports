package com.princetonsa.mundo.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.odontologia.InfoServicios;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.odontologia.CitaOdontologicaDao;
import com.princetonsa.dto.cargos.DtoDetalleCargo;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosPlanT;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoSolictudCambioServicioCita;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.facturacion.AbonosYDescuentos;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudConsultaExterna;
import com.princetonsa.mundo.solicitudes.SolicitudProcedimiento;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * 
 * @author Víctor Hugo Gómez L. 
 *
 */
@SuppressWarnings("unchecked")
public class CitaOdontologica 
{
	/**
	 * logger
	 */
	static Logger logger = Logger.getLogger(CitaOdontologica.class);
	
	/**
	 * Instancia DAO
	 * */
	public static CitaOdontologicaDao getCitaOdontologicaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCitaOdontologicaDao();		
	}
	
	/**
	 * se obtiene un listado de los servicios de cada cita odontologica
	 * 
	 * @param con
	 * @param codigoCita
	 * @param institucion
	 * @param codigoPaciente
	 * @return ArrayList<DtoCitaOdontologica>
	 */
	public static ArrayList<DtoServicioCitaOdontologica> consultarServiciosCitaOdontologica(Connection con,int codigCita,int institucion, int codigoPaciente)
	{
		return getCitaOdontologicaDao().consultarServiciosCitaOdontologica(con, codigCita,institucion, codigoPaciente);
	}
	
	/**
	 * se obtiene un listado de las citas odontologicas x una agenda especifica
	 * @param con
	 * @param institucion 
	 * @param HashMap parametros
	 * @return ArrayList<DtoCitaOdontologica>
	 */
	public static ArrayList<DtoCitaOdontologica> consultarCitaOdontologicaXAgenda(Connection con, int codigoAgenda, int institucion)
	{
		HashMap parametros = new HashMap(); 
		parametros.put("codigo_agenda", codigoAgenda);
		parametros.put("institucion", institucion);
		
		return getCitaOdontologicaDao().consultarCitaOdontologicaXAgenda(con, parametros, null);
	}
	
	/**
	 * se obtiene un listado de las citas odontologicas x un paciente especifico
	 * @param con
	 * @param institucion 
	 * @param codigoIngreso 
	 * @param minutosReserva 
	 * @param todasLasCitas 
	 * @param todasLasCitas 
	 * @param HashMap parametros
	 * @return ArrayList<DtoCitaOdontologica>
	 */
	public static ArrayList<DtoCitaOdontologica> consultarCitaOdontologicaXPaciente(Connection con, UsuarioBasico usuario, int codPaciente,int institucion, String fechaFiltro,String horaFiltro,int minutosEspera, int codigoIngreso, int minutosReserva)
	{
		HashMap parametros = new HashMap();
		parametros.put("usuario", usuario);
		parametros.put("codigo_paciente", codPaciente);
		parametros.put("institucion", institucion);
		parametros.put("codigo_ingreso", codigoIngreso);
		
		if(!fechaFiltro.equals("") && !horaFiltro.equals("")){
			
			String[] fechaHora = UtilidadFecha.incrementarMinutosAFechaHora(fechaFiltro, horaFiltro, minutosEspera*-1, false);
			parametros.put("fechaFiltro", fechaHora[0]);
			parametros.put("horaFiltro", fechaHora[1]);
			
			String[] fechaHoraReserva = UtilidadFecha.incrementarMinutosAFechaHora(fechaFiltro, horaFiltro, minutosReserva*-1, false);
			parametros.put("fechaFiltroReserva", fechaHoraReserva[0]);
			parametros.put("horaFiltroReserva", fechaHoraReserva[1]);
			
		}else
		{
			parametros.put("fechaFiltro", "");
			parametros.put("horaFiltro", "");
			parametros.put("fechaFiltroReserva", "");
			parametros.put("horaFiltroReserva", "");
		}
		
		return getCitaOdontologicaDao().consultarCitaOdontologicaXPaciente(con, parametros);
	}
	
	
	/**
	 * metodo que inserta una cita odontologica
	 * @param con
	 * @param dto
	 * @return
	 */
	public static int insertarCitaOdontologica(Connection con, DtoCitaOdontologica dto)
	{
		return getCitaOdontologicaDao().insertarCitaOdontologica(con, dto);
	}
	
	/**
	 * actualizar srvicios citas odontologicas
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizarServicioCitaOdontologica(Connection con, DtoServicioCitaOdontologica dto)
	{
		return getCitaOdontologicaDao().actualizarServicioCitaOdontologica(con, dto);
	}
	
	/**
	 * metodo que consulta una cita especifica
	 * @param con
	 * @param codigo_cita
	 * @return
	 */
	public static DtoCitaOdontologica obtenerCitaOdontologica(Connection con, int codigo_cita, int codInstitucion)
	{
		return getCitaOdontologicaDao().obtenerCitaOdontologica(con, codigo_cita, codInstitucion);
	}
	
	/**
	 * 
	 * @param codigoCita
	 * @param institucion
	 */
	public static DtoCitaOdontologica obtenerCitaOdontologica(int codigoCita, int institucion)
	{
		DtoCitaOdontologica cita=new DtoCitaOdontologica();
		Connection con=UtilidadBD.abrirConexion();
		cita=obtenerCitaOdontologica(con, codigoCita, institucion);
		UtilidadBD.closeConnection(con);
		return cita;
	}

	/**
	 * metodo de insert logs cita y servicios odontologicos
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean cambiarSerOdontologicos(Connection con, DtoCitaOdontologica dto)
	{
		/*if(insertLogCitaOdontologica(con, dto)>0)
		{*/
			//modificar la cita odontologica
			if(actualizarCitaOdontologica(con, dto))
			{
				for(DtoServicioCitaOdontologica elem: dto.getServiciosCitaOdon())
				{
					logger.info("se modifica: "+elem.getModificadoSer());
					logger.info("se elimina: "+elem.getEliminarSer());
					logger.info("es nuevo: "+elem.getNuevoServicio());
					elem.setUsuarioModifica(dto.getUsuarioModifica());
					if(elem.getModificadoSer().equals(ConstantesBD.acronimoSi))
					{
						if(insertLogServiciosCitaOdon(con, elem)<=0)
							return false;
					}else{
						if(elem.getEliminarSer().equals(ConstantesBD.acronimoSi))
						{
							if(insertLogServiciosCitaOdon(con, elem)>0)
							{
								if(!inactivarServicioCitaOdontologica(con, elem.getUsuarioModifica(), elem.getCodigoPk()))
									return false;
							}else
								return false;
						}else{
							if(elem.getNuevoServicio().equals(ConstantesBD.acronimoSi))
							{
								if(insertarServiciosCitaOdontologica(con, elem)<=0)
									return false;
							}
						}
					}
				}
				return true;
			}else
				return false;
		/*}else
			return false;*/
	}
	
	/**
	 * insert de log de citas odontologicas
	 * @param con
	 * @param dto
	 * @return
	 */
	public static int insertLogCitaOdontologica(Connection con, DtoCitaOdontologica dto)
	{
		return getCitaOdontologicaDao().insertLogCitaOdontologica(con, dto);
	}
	
	/**
	 * metodo que inserta el log servicios cita odontologica
	 * @param con
	 * @param dto
	 * @return
	 */
	public static int insertLogServiciosCitaOdon (Connection con, DtoServicioCitaOdontologica dto)
	{
		return getCitaOdontologicaDao().insertLogServiciosCitaOdon(con, dto);
	}
	
	/**
	 * metodo que inactiva el servicio de una cita odontologica
	 * @param con
	 * @param usuario
	 * @param codigoPk
	 * @return
	 */
	public static boolean inactivarServicioCitaOdontologica(Connection con, String usuario, int codigoPk)
	{
		return getCitaOdontologicaDao().inactivarServicioCitaOdontologica(con, usuario, codigoPk);
	}
	
	/**
	 * insert servicios citas odontologicas
	 * @param con
	 * @param dto
	 * @return
	 */
	public static int insertarServiciosCitaOdontologica(Connection con, DtoServicioCitaOdontologica dto)
	{
		return getCitaOdontologicaDao().insertarServiciosCitaOdontologica(con, dto);
	}
	
	/**
	 * actualizar cita odontologica
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizarCitaOdontologica(Connection con, DtoCitaOdontologica dto)
	{
		return getCitaOdontologicaDao().actualizarCitaOdontologica(con, dto);
	}

	/**
	 * actualizar cita odontológica
	 * @param con
	 * @param dto
	 * @param esCambioServicio Indica si es un cambio de servicio
	 * @return
	 */
	public static boolean actualizarCitaOdontologicaYServicios(Connection con, DtoCitaOdontologica dto, boolean esCambioServicio)
	{
		return getCitaOdontologicaDao().actualizarCitaOdontologicaYServicios(con, dto, esCambioServicio);
	}

	/**
	 * metodo que verifica si al menos un servicios asociado a una cita esta facturado
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public boolean almenosUnServicioFacturado(Connection con, int codigoCita)
	{
		return getCitaOdontologicaDao().almenosUnServicioFacturado(con, codigoCita);
	}
	
	//*****************************************************************************
	
	/**
	 * Método implementado para generar la solicitud de cada servicio odontologica de la agenda
	 * @param garantia 
	 */
	public static BigDecimal generarSolicitud(
			Connection con,
			DtoServicioCitaOdontologica servicioCita,
			PersonaBasica paciente,
			UsuarioBasico usuario,
			int codigoCentroCosto,
			int codigoEspecialidad,
			int codigoMedico,
			int codigoOcupacionMedica,
			ActionErrors errores,
			boolean garantia,
			int codMedicoAgendaSeleccionada)
	{
		
		logger.info("\n\n\n\n\n " +
					"GARANTIA--->"+garantia+"" +
					"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		
		final int codigoServicio = servicioCita.getServicio();
		String fechaActual = UtilidadFecha.getFechaActual(con);
		String horaActual = UtilidadFecha.getHoraActual(con);
		int numeroSolicitud = ConstantesBD.codigoNuncaValido;
		int codigoTipoSolicitud = ConstantesBD.codigoNuncaValido;
			
		logger.info("%%%%%%%%%%%%%%%%codigo del tipo de servicio !!"+servicioCita.getCodigoTipoServicio());
		if(servicioCita.getCodigoTipoServicio().equals(ConstantesBD.codigoServicioInterconsulta+""))
		{
			codigoTipoSolicitud = ConstantesBD.codigoTipoSolicitudCita; 
			SolicitudConsultaExterna solicitudConsulta = new SolicitudConsultaExterna();
			solicitudConsulta.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea()));
			solicitudConsulta.setCentroCostoSolicitado(new InfoDatosInt(codigoCentroCosto));
			solicitudConsulta.setCodigoMedicoSolicitante(ConstantesBD.codigoNuncaValido);
			solicitudConsulta.setCobrable(!garantia);
			solicitudConsulta.setCodigoCuenta(paciente.getCodigoCuenta());
			solicitudConsulta.setCodigoServicioSolicitado(codigoServicio);
			solicitudConsulta.setEspecialidadSolicitante(new InfoDatosInt(codigoEspecialidad));
			solicitudConsulta.setEstadoHistoriaClinica(new InfoDatosInt(ConstantesBD.codigoEstadoHCSolicitada));
			solicitudConsulta.setFechaSolicitud(fechaActual);
			solicitudConsulta.setHoraSolicitud(horaActual);
			
			if(codigoOcupacionMedica>0)
			{
				solicitudConsulta.setOcupacionSolicitado(new InfoDatosInt(codigoOcupacionMedica) );
			}
			solicitudConsulta.setTipoSolicitud(new InfoDatosInt(codigoTipoSolicitud) );
			solicitudConsulta.setUrgente(false);
			solicitudConsulta.setVaAEpicrisis(false);
			solicitudConsulta.setSolPYP(false);
			try 
			{
				logger.info("Se va a generar la solicitud de consulta!!");
				solicitudConsulta.insertarSolicitudConsultaExternaTransaccional(con, ConstantesBD.continuarTransaccion);
			} 
			catch (SQLException e) 
			{
				logger.error("Error al generar la solicitud de tipo consulta ",e);
			}
			
			numeroSolicitud = solicitudConsulta.getNumeroSolicitud();
		}
		else if(servicioCita.getCodigoTipoServicio().equals(ConstantesBD.codigoServicioProcedimiento+""))
		{
			codigoTipoSolicitud = ConstantesBD.codigoTipoSolicitudProcedimiento;
			SolicitudProcedimiento	solicitudProcedimiento= new SolicitudProcedimiento();
			solicitudProcedimiento.setFechaSolicitud(UtilidadFecha.getFechaActual(con));
			solicitudProcedimiento.setHoraSolicitud(UtilidadFecha.getHoraActual(con));
			solicitudProcedimiento.setSolPYP(false);
			solicitudProcedimiento.setTipoSolicitud(new InfoDatosInt (codigoTipoSolicitud, ""));
			solicitudProcedimiento.setCobrable(true);
			solicitudProcedimiento.setEspecialidadSolicitante(new InfoDatosInt(codigoEspecialidad, ""));
			if(codigoOcupacionMedica>0)
			{
				solicitudProcedimiento.setOcupacionSolicitado(new InfoDatosInt(codigoOcupacionMedica, ""));
			}
			solicitudProcedimiento.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea(), ""));
			solicitudProcedimiento.setCentroCostoSolicitado(new InfoDatosInt(codigoCentroCosto, ""));
			solicitudProcedimiento.setCodigoMedicoSolicitante(ConstantesBD.codigoNuncaValido);
			solicitudProcedimiento.setCodigoCentroAtencionCuentaSol(usuario.getCodigoCentroAtencion());
			solicitudProcedimiento.setCodigoCuenta(paciente.getCodigoCuenta());
			solicitudProcedimiento.setCodigoPaciente(paciente.getCodigoPersona());
			solicitudProcedimiento.setDatosMedico("");
			solicitudProcedimiento.setVaAEpicrisis(false);
			solicitudProcedimiento.setUrgente(false);
			solicitudProcedimiento.setComentario("");
			solicitudProcedimiento.setNombreOtros("");
			solicitudProcedimiento.setTieneCita(false);
			
			int numeroDocumento=solicitudProcedimiento.numeroDocumentoSiguiente(con);

			/*Obtener el codigo de la finalidad*/
			ArrayList<HashMap<String, Object>> finalidades = Utilidades.obtenerFinalidadesServicio(con, codigoServicio, usuario.getCodigoInstitucionInt());
			//Si solo hay una finalidad se asigna automaticamente, de lo contrario se deja vacío
			if(finalidades.size()==1)
				solicitudProcedimiento.setFinalidad(Integer.parseInt(((HashMap)finalidades.get(0)).get("codigo").toString()));
			else
				solicitudProcedimiento.setFinalidad(0);
					
			/* Obtener el código del servicio de la solicitud */
			solicitudProcedimiento.setCodigoServicioSolicitado(codigoServicio);
			solicitudProcedimiento.setFinalizadaRespuesta(false);
			//solicitud.setRespuestaMultiple(Utilidades.esServicioRespuestaMultiple(con,codigoServicio+""));
			solicitudProcedimiento.setRespuestaMultiple(false); //para odontologia no aplicaría respuesta multiple aunque se encuentre parametrizado
			solicitudProcedimiento.setMultiple(false);

			
			solicitudProcedimiento.setFrecuencia(ConstantesBD.codigoNuncaValido);

			/* Genera ls solicitud */
			try 
			{
				numeroSolicitud = solicitudProcedimiento.insertarTransaccional(con, ConstantesBD.continuarTransaccion, numeroDocumento, paciente.getCodigoCuenta(), false,ConstantesBD.codigoNuncaValido+"");
			} 
			catch (SQLException e) 
			{
				logger.error("Error al generar la solicitud de procedimientos ",e);
			}
			
			solicitudProcedimiento.setNumeroSolicitud(numeroSolicitud);
			
			
			
		}
		
		///Actualizacion del pool de la solicitud*******************************************************************
		if(codigoMedico>0)
		{
			Solicitud sol=new Solicitud();
			ArrayList array=Utilidades.obtenerPoolesMedico(con,UtilidadFecha.getFechaActual(con),codigoMedico);
			if(array.size()==1)
				sol.actualizarPoolSolicitud(con,numeroSolicitud,Integer.parseInt(array.get(0)+""));
		}
		
		if(numeroSolicitud>0)
		{
			String subCuentaCoberturaOPCIONAL="";
			
			if(	servicioCita.getInfoResponsableCobertura()!=null 
				&& servicioCita.getInfoResponsableCobertura().getDtoSubCuenta()!=null 
				&& !UtilidadTexto.isEmpty(servicioCita.getInfoResponsableCobertura().getDtoSubCuenta().getSubCuenta()))
			{
				subCuentaCoberturaOPCIONAL= servicioCita.getInfoResponsableCobertura().getDtoSubCuenta().getSubCuenta();
			}
			
			servicioCita.setNumeroSolicitud(numeroSolicitud);
			Cargos cargos= new Cargos();
			boolean inserto;
			//cargos.setCodigoMedicoAgendaSeleccionada(codMedicoAgendaSeleccionada);
			if(!garantia)
			{
				
				logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n servicioCita.getInfoTarifa().getEstadoFacturacion()---->"+servicioCita.getInfoTarifa().getEstadoFacturacion()+"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
				
				/*
				inserto= cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	
						con, 
						usuario, 
						paciente, 
						false//dejarPendiente
						numeroSolicitud, 
						ConstantesBD.codigoTipoSolicitudCita //codigoTipoSolicitudOPCIONAL, 
						,paciente.getCodigoCuenta()
						,codigoCentroCosto//codigoCentroCostoEjecutaOPCIONAL, 
						,codigoServicio//codigoServicioOPCIONAL, 
						,1//cantidadServicioOPCIONAL, 
						,servicioCita.getInfoTarifa().getValorTarifaUnitaria().doubleValue()//valorTarifaOPCIONAL, 
						,ConstantesBD.codigoNuncaValido //codigoEvolucionOPCIONAL,
						// "" -- numeroAutorizacionOPCIONAL
						""//esPortatil,
						servicioCita.getInfoTarifa().getEstadoFacturacion()==ConstantesBD.codigoEstadoFExento?true:false,
						fechaActual,
						//wwwwwwwwwwwwwwwwwwwwwwww
						subCuentaCoberturaOPCIONAL,
						0 //porcentajeDescuentoComercialOPCIONAL,
						,servicioCita.getInfoTarifa().getValorDescuentoComercial(),
						servicioCita.getInfoTarifa().getPorcentajeDescuentoPromocionUnitario(),
						servicioCita.getInfoTarifa().getValorDescuentoPromocionUnitario(),
						servicioCita.getInfoTarifa().getPorcentajeHonorarioPromocion(),
						servicioCita.getInfoTarifa().getValorHonorarioPromocion(),
						servicioCita.getCodigoPrograma(),
						servicioCita.getInfoTarifa().getPorcentajeDecuentoBonoUnitario(),
						servicioCita.getInfoTarifa().getValorDescuentoBonoUnitario(),
						servicioCita.getInfoTarifa().getPorcentajeDctoOdontologicoCALCULADO() //porcentajeDctoOdontologico, 
						,servicioCita.getInfoTarifa().getValorDctoOdontologicoCALCULADO()//valorDescuentoOdontologico,
						,servicioCita.getInfoTarifa().getDetallePaqueteOdonConvenio(),
						servicioCita.getInfoTarifa().getEsquemaTarifarioPaquete(),
						servicioCita.getTipoCita(),
						//servicioCita.getInfoTarifa().getPresupuestoOdontologicoMigrado());
						 
						 */
			}
			else
			{
				logger.info("entro garantia trueeeeeeeeeeeeee");
				
				inserto= cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	
						con, 
						usuario, 
						paciente, 
						false/*dejarPendiente*/, 
						numeroSolicitud, 
						ConstantesBD.codigoTipoSolicitudCita /*codigoTipoSolicitudOPCIONAL*/, 
						paciente.getCodigoCuenta(), 
						codigoCentroCosto/*codigoCentroCostoEjecutaOPCIONAL*/, 
						codigoServicio/*codigoServicioOPCIONAL*/, 
						1/*cantidadServicioOPCIONAL*/, 
						0, 
						ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
						/* "" -- numeroAutorizacionOPCIONAL*/
						""/*esPortatil*/,
						true,
						fechaActual,
						subCuentaCoberturaOPCIONAL);
			}
			 
			/*
			 if(!inserto)
			 {
				 errores.add("", new ActionMessage("errors.notEspecific","Problemas generando el cargo para el servicio "+servicioCita.getNombreServicio()+". Favor reportar inconsistencia al administrador del sistema"));
			 }
			 */
		}
		else
		{
			errores.add("", new ActionMessage("errors.notEspecific","Problemas generando la solicitud para el servicio "+servicioCita.getNombreServicio()));
		}
		
		
		
		
		
		
		 BigDecimal numSolicitud = new BigDecimal(numeroSolicitud);
		return numSolicitud;
	}
	
	/**
	 * Metodo q retorna arraylist de citas segun parametros de busqueda funcionaluidad consulta externa/consultar citas
	 * @param connection
	 * @param codigoPaciente
	 * @param fechaHoraInicial
	 * @param fechaHorafinal
	 * @param centroAtencion
	 * @param unidadConsulta
	 * @param estadosCita
	 * @param consultorio
	 * @param tipoCita
	 * @param profesionalSalud
	 * @param institucion 
	 * @return
	 */
	public static ArrayList<DtoCitaOdontologica> consultaCitaOdontologicaConsExt (Connection connection,int codigoPaciente, String fechaHoraInicial, 
			String fechaHorafinal, String centroAtencion, int unidadConsulta, String[] estadosCita, int consultorio, String tipoCita, 
			int profesionalSalud, int institucion){
		return getCitaOdontologicaDao().consultaCitaOdontologicaConsExt(connection, codigoPaciente, fechaHoraInicial, fechaHorafinal, centroAtencion, unidadConsulta, estadosCita, consultorio, tipoCita, profesionalSalud,institucion);
	}
	
	/**
	 * Método q consulta el programa, servicio, garantia y estado en el plande tratamiento
	 * @param connection
	 * @param institucion
	 * @param cita
	 * @return
	 */
	public static ArrayList<DtoProgramasServiciosPlanT> consultarDetalleCitaConsExterna(Connection connection, int institucion, int cita){
		return getCitaOdontologicaDao().consultarDetalleCitaConsExterna(connection, institucion, cita);
	}
	
	/**
	 * metodo que obtiene los datos informativos del centro de atención
	 * @param consecutivoCenAten
	 * @return
	 */
	public static InfoDatosString obtenerInfoCentroAtencion(int consecutivoCenAten)
	{
		return getCitaOdontologicaDao().obtenerInfoCentroAtencion(consecutivoCenAten);
	}
	
	/**
	 * Método para cargar la informacion de la cita asociada a un servicio de un plan de tratamiento
	 * @param con
	 * @param codigoPkProgServ
	 * @param codigoInstitucion
	 * @return
	 */
	public static DtoCitaOdontologica obtenerCitaOdontologicaXProgServPlanT(Connection con,BigDecimal codigoPkProgServ,int codigoInstitucion)
	{
		return getCitaOdontologicaDao().obtenerCitaOdontologicaXProgServPlanT(con, codigoPkProgServ, codigoInstitucion);
	}
	
	
	/**
	 * M&eacute;todo para insertar los servicios a un pr&oacute;xima cita 
	 * @param con
	 * @param arrayServicios
	 * @param centroCosto
	 * @return
	 */
	public static int insertarProximaCitaOdontologia(Connection con, ArrayList<InfoServicios> arrayServicios, String fechaCita, int codigoPaciente, String loginUsuario , int codigoEvolucion, int centroCosto, int totalDuracionCita)
	{
		return getCitaOdontologicaDao().insertarProximaCitaOdontologia(con,arrayServicios,fechaCita, codigoPaciente, loginUsuario, ConstantesIntegridadDominio.acronimoTipoAtencionTratamiento , codigoEvolucion, centroCosto, totalDuracionCita);	
	}

	/**
	 *Metodo para insertar los servicios a un proxima cita 
	 * @param con
	 * @param arrayServicios
	 * @param centroCosto
	 * @return
	 */
													
	public static int insertarProximaCitaOdontologia(Connection con, ArrayList<InfoServicios> arrayServicios, String fechaCita, int codigoPaciente, String loginUsuario, String tipoCita, int centroCosto)
	{
		return getCitaOdontologicaDao().insertarProximaCitaOdontologia(con,arrayServicios,fechaCita, codigoPaciente, loginUsuario, tipoCita, ConstantesBD.codigoNuncaValido /* TODO  CARVAJAL  OJO SE NECESITA EL CODIGO DE LA EVOLUCION  AQUI ? */, centroCosto, ConstantesBD.codigoNuncaValido);	
	}
	
	/**
	 * Metodo para Consultar Cita Odontologica asociada a unos parametros de Busqueda ( por Rango )
	 * @param con
	 * @param parametrosBusqueda
	 * @param centroAtencionUsuario TODO
	 * @return
	 */
	public static ArrayList<DtoCitaOdontologica> consultarCitaOdontologicaXRango(Connection con, HashMap parametrosBusqueda, int centroAtencionUsuario) {
		
		return getCitaOdontologicaDao().consultarCitaOdontologicaXRango(con, parametrosBusqueda, centroAtencionUsuario);
	}
	
	
	/**
	 * Metodo para actualizar Cita Odontologica por Reprogramacion ( Cita Existente)
	 * @param con
	 * @param codigoCita
	 * @param codAgenda
	 * @param fechaCita
	 * @param horaInicio
	 * @param horaFin
	 * @param duracion
	 * @param usuarioModifica
	 * @param tipoCita
	 * @param arrayServicios
	 * @return
	 */
	public static boolean actualizarCitaOdontologicaXReprogramacion(Connection con, int codigoCita, int codAgenda, String fechaCita ,String horaInicio, String horaFin, int duracion, String usuarioModifica,String tipoCita, ArrayList<DtoServicioCitaOdontologica> arrayServicios)
	{
		return getCitaOdontologicaDao().actualizarCitaOdontologicaXReprogramacion(con, codigoCita,codAgenda, fechaCita, horaInicio, horaFin, duracion, usuarioModifica,tipoCita, arrayServicios);
	}

	/**
	 * Metodo para actualizar Cita Odontologica por Reserva (Cita Existente)
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizarCitaOdontologicaXReserva(Connection con,DtoCitaOdontologica dto) {
		
		return getCitaOdontologicaDao().actualizarCitaOdontologicaXReserva(con,dto);
	}

	/**
	 * Método que verifica si existen cupos disponibles en los espacios de tiempo continuos
	 * en caso de que en la agenda se selecciones servicios conduración mayor a la
	 * asignada inicialmente.  
	 * @param con
	 * @param codigoAge
	 * @param horaInicio
	 * @param horaFinal
	 * @param return true en caso de poder asignar el nuevo cupo
	 */
	public static boolean validarCuposSiguientesDisponibles(DtoAgendaOdontologica dto) {
		return getCitaOdontologicaDao().validarCuposSiguientesDisponibles(dto);
	}

	public static boolean asignarSolicitudServicioCitaOdo(Connection con,
			DtoServicioCitaOdontologica servicioCita) {
		return getCitaOdontologicaDao().asignarSolicitudServicioCitaOdo(con, servicioCita);
		
	}

	public static ArrayList<DtoCitaOdontologica> consultarProxCitasPacienteXCancelacion(int codigo, double codigoPrograma, int ordenServicio,int codigoInstitucionInt, String fecha, String horaFinal,
			String loginUsuario) {
		
		return getCitaOdontologicaDao().consultarProxCitasPacienteXCancelacion(codigo, codigoPrograma,ordenServicio,codigoInstitucionInt,fecha,  horaFinal, loginUsuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param cita
	 * @param usuario
	 * @param codigoPaciente
	 * @param tipoCancelacion
	 * @param motivoCancelacion
	 * @param iniciarFinalizarTransaccion
	 * @param ingreso 
	 * @return
	 */
	public static ResultadoBoolean cancelarCita(	Connection con, 
													DtoCitaOdontologica cita, 
													UsuarioBasico usuario, 
													int codigoPaciente, 
													String tipoCancelacion, 
													int motivoCancelacion,
													boolean iniciarFinalizarTransaccion, 
													Integer ingreso,
													String estadoCita
													
													) throws IPSException
	{
		ResultadoBoolean resultado= new ResultadoBoolean(true);
		
		//llena la cita con la que voy a trabajar
		if(iniciarFinalizarTransaccion)
		{	
			UtilidadBD.iniciarTransaccion(con);
		}	
		// revisa si la cita esta asignada
		double valorAbonos=obtenerValorCargadoCita(con, cita.getServiciosCitaOdon());
		if(cita.getEstado().equals(ConstantesIntegridadDominio.acronimoAsignado))
		{
			logger.info("llega cuando la cita esta asignada");
			
			for(DtoServicioCitaOdontologica elem: cita.getServiciosCitaOdon())
			{
				logger.info("\n\n numero solicitud "+elem.getNumeroSolicitud());
				logger.info("\n Tipo CITA >>>> "+cita.getTipo());
			  
				// Si la cita es de Interconsulta NOOO cambia el estado de la SOLICITUD...
				//Se valida que la cita sea diferente de Interconsulta para anular la solicitud	
				if(!cita.getTipo().equals(ConstantesIntegridadDominio.acronimoRemisionInterconsulta))
				{
					//logger.info.("orden servicio >>"+ )
					if(elem.getFacturado().equals(ConstantesBD.acronimoNo))
					{
						if(!Solicitud.cambiarEstadosSolicitudStatico(con,
								elem.getNumeroSolicitud(), 
								ConstantesBD.codigoEstadoFAnulada, 
								ConstantesBD.codigoEstadoHCAnulada).isTrue())
						{
							logger.info("se aborta la transaccion por errores en la anulacion de solicitudes");
							UtilidadBD.abortarTransaccion(con);
							resultado.setResultado(false);
							resultado.setDescripcion("errores en la anulacion de solicitudes");
							break;
						}
					}
					
					if(ConstantesIntegridadDominio.acronimoAreprogramar.equals(estadoCita)){
						
						elem.setEliminarSer(ConstantesBD.acronimoNo);
						
					}else{
						
						elem.setEliminarSer(ConstantesBD.acronimoSi);
						
						//Se elimina la relación con la cita Programada.
						
						//IServCitasAsociadasAProgramadaDAO dao = AgendaOdontologicaFabricaDAO.crearServCitasAsociadasAProgramadaDAO();
						//dao.eliminarServicioCitaAsociada(cita.getCodigoPk(), elem);
					}	
				 }else{
					 
					 if(ConstantesIntegridadDominio.acronimoAreprogramar.equals(estadoCita)){
							elem.setEliminarSer(ConstantesBD.acronimoNo);
							
						}else{
							elem.setEliminarSer(ConstantesBD.acronimoSi);
							
							//IServCitasAsociadasAProgramadaDAO dao = AgendaOdontologicaFabricaDAO.crearServCitasAsociadasAProgramadaDAO();
							//dao.eliminarServicioCitaAsociada(cita.getCodigoPk(), elem);
						}
				 }
			}
			
			if(resultado.isTrue())
			{	
				if(!CitaOdontologica.devolverReservaAbonosCita(con, valorAbonos, usuario, codigoPaciente, cita.getCodigoPk(), ingreso))
				{
					logger.info("se aborta la transaccion por errores en la eliminacion abonos");
					UtilidadBD.abortarTransaccion(con);
					resultado.setResultado(false);
					resultado.setDescripcion("errores en la devolucion de reserva abonos");
				}
			}
		}
		else // si no esta asignada debe estar reservada
		{
			/*
			 * Dependiendo del estado en el que debe quedar la cita, se eliminan o no los servicios.
			 * Cancelar = Eliminar los servicios
			 * A Reprogramar = No se eliminan los servicios.
			 */
			for(DtoServicioCitaOdontologica elem: cita.getServiciosCitaOdon())//armando
			{
				if(!ConstantesIntegridadDominio.acronimoAreprogramar.equals(estadoCita))
				{
					elem.setEliminarSer(ConstantesBD.acronimoSi);
					
					//Se elimina la relación con la cita Programada.
					
					//IServCitasAsociadasAProgramadaDAO dao = AgendaOdontologicaFabricaDAO.crearServCitasAsociadasAProgramadaDAO();
					//dao.eliminarServicioCitaAsociada(cita.getCodigoPk(), elem);
					
				}
				logger.info("reservada");
			}
		}
		
		if(resultado.isTrue())
		{
			// verifica el motivo de la cita 
//			if(tipoCancelacion.equals(ConstantesBD.acronimoNo))//Institución
//			{
//				cita.setEstado(ConstantesIntegridadDominio.acronimoAreprogramar);
//			}
			
			cita.setEstado(estadoCita);
//			else
//			{      
			Log4JManager.info("***************tipo cancelacion es igual a  "+ConstantesBD.acronimoSi);
			
			boolean eliminarCupo=true;
			
	/*		if(tipoCancelacion.equals(ConstantesBD.acronimoSi) || 
					tipoCancelacion.equals(ConstantesBD.acronimoNo))//paciente e instituciòn
			{*/
				
				/*
				 *Estado cita 
				 */
				cita.setEstado(estadoCita);
				cita.setMotivoCancelacion(motivoCancelacion); //
				
				/*
				/*
				 * XPlanner 2008 166700
				 
				if(cita.getEstado().equals(ConstantesIntegridadDominio.acronimoAreprogramar))
				{
					cita.setEstado(ConstantesIntegridadDominio.acronimoCancelado);	
				}
				else
				{
					cita.setEstado(ConstantesIntegridadDominio.acronimoAreprogramar);	
				}*/
				
				
				Log4JManager.info("*****************el estado llega   "+cita.getEstado());
				Log4JManager.info("*****************el estado llega   "+cita.getMotivoCancelacion());
		//	}
			
			cita.setUsuarioModifica(usuario.getLoginUsuario());
			if(CitaOdontologica.cambiarSerOdontologicos(con, cita))
			{	
				// liberar cupo de la unidad de agenda odontológica cuando es motivo de cancelacion es por paciente
				if(eliminarCupo)
				{
				
					// aumenta el cupo de la cita 
					if(AgendaOdontologica.modificarCuposAgendaOdontologica(con,cita.getAgenda(), cita.getHoraInicio(), 
							 cita.getHoraFinal(),usuario.getLoginUsuario(), false, usuario.getCodigoInstitucionInt())<=0)
					{
						UtilidadBD.abortarTransaccion(con);
						resultado.setResultado(false);
						resultado.setDescripcion("errores en la modificacion de cupos de la agenda");
					}
				}
				
				//ESTO NO ESTA EN EL DOCUMENTO, ES UN METODO QUE ESTA EN EL ACTION DE AGENDAODONTOLOGICAACTION
				/*if(listaProximasCitasCancelar.size()>0)
				{
					return cancelarProximasCitas(con,forma,usuario, mapping, request);				  
				}*/	
			}
		}	
		
		if(!resultado.isTrue())
		{
			UtilidadBD.abortarTransaccion(con);
		}
		else 
		{
			if(iniciarFinalizarTransaccion)
			{	
				String tipoCancel = "";
				
				if (tipoCancelacion.trim().equals(ConstantesBD.acronimoSi)) {
					tipoCancel = ConstantesIntegridadDominio.acronimoPaciente;
				}else if (tipoCancelacion.trim().equals(ConstantesBD.acronimoNo)) {
					tipoCancel = ConstantesIntegridadDominio.acronimoInstitucion;
				}else if (tipoCancelacion.trim().equals(ConstantesIntegridadDominio.acronimoAmbos)) {
					tipoCancel = ConstantesIntegridadDominio.acronimoAmbos;
				}
				
				boolean procesoExitoso = getCitaOdontologicaDao().actualizarTipoCancelacionCita(con, tipoCancel, cita.getCodigoPk());
				
				if (procesoExitoso) {
					UtilidadTransaccion.getTransaccion().commit();
					UtilidadBD.finalizarTransaccion(con);
				}else{
					UtilidadBD.abortarTransaccion(con);
				}
			}	
		}
		
		return resultado;
	}
	
	
	/**
	 * Método para calcular los valores cargados a la cita
	 * @param serviciosCitaOdon
	 * @return
	 */
	public static double obtenerValorCargadoCita(Connection con, ArrayList<DtoServicioCitaOdontologica> serviciosCitaOdon) throws IPSException {
		double valorReversaAbono=0;
		
		@SuppressWarnings("unused")
		double neto=0;
		
		for(DtoServicioCitaOdontologica elem: serviciosCitaOdon)
		{
			DtoDetalleCargo detalleCargoConsulta = new DtoDetalleCargo();
			detalleCargoConsulta.setNumeroSolicitud(elem.getNumeroSolicitud());
			ArrayList<DtoDetalleCargo> listaDetalle=Cargos.cargarDetalleCargos(con, detalleCargoConsulta);
			for(DtoDetalleCargo detalleCargo:listaDetalle)
			{
				boolean pacientePagaAtencion=Contrato.pacientePagaAtencion(detalleCargo.getCodigoContrato());
				if(pacientePagaAtencion)
				{
					//dtoDetalle.setValorPool( (((dtoDetalle.getValorTotal()-valorTmpDescuentoComercial -dtoDetalle.getValorDctoBono().doubleValue()-dtoDetalle.getValorDctoProm().doubleValue()-dtoDetalle.getValorDctoOdo().doubleValue())   *dtoDetalle.getPorcentajePool())/100));
					Log4JManager.info("--Valor total -->"+detalleCargo.getValorTotalCargado());
					Log4JManager.info("--Descuento Odont -->"+detalleCargo.getValorDescuentoOdontologico().doubleValue());
					Log4JManager.info("--Descuento Bono-->"+detalleCargo.getValorDescuentoBono() );
					Log4JManager.info("--Descuento  Promocion Servicio-->"+detalleCargo.getValorDescuentoPromocionServicio() );
					Log4JManager.info("--Porcentaje Descuento-->"+detalleCargo.getPorcentajeDescuento() );
					Log4JManager.info("--Valor Unitario Cargado-->"+detalleCargo.getValorUnitarioCargado() );
					Log4JManager.info("--Valor Tarifa-->"+detalleCargo.getValorUnitarioTarifa() );
					Log4JManager.info("--Valor Unidad Neta->"+detalleCargo.getValorUnitarioNeto() );
					
					valorReversaAbono+=detalleCargo.getValorTotalNeto().doubleValue();
					
					
				}
			}
		}
		
		Log4JManager.info(" total Valor Reserva Abono -->"+valorReversaAbono);
		
		
		return valorReversaAbono;
	}
	
	/**
	 * Método para anular la reserva reservar los abonos o los anticipos del paciente (Valor total de todos los servicios)
	 * @param con
	 * @param valorAbonos
	 * @param usuario
	 * @param codigoCita
	 * @param ingreso
	 * @param persona
	 * @return true en caso de devolver correctamente los abonos
	 * @author Juan David
	 */
	public static boolean devolverReservaAbonosCita(Connection con, double valorAbonos, UsuarioBasico usuario, int codigoPersona, int codigoCita, Integer ingreso)
	{
		if(valorAbonos>0)
		{
			return AbonosYDescuentos.insertarMovimientoAbonos(con, codigoPersona, codigoCita, ConstantesBD.tipoMovimientoAbonoAnulacionReservaAbono, valorAbonos, usuario.getCodigoInstitucionInt(), ingreso, usuario.getCodigoCentroAtencion())>0;
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param valorAbonos
	 * @param usuario
	 * @param codigoPersona
	 * @param codigoCita
	 * @return
	 */
	public static boolean devolverReservaAbonosCita(double valorAbonos, UsuarioBasico usuario, int codigoPersona, int codigoCita, Integer ingreso)
	{
		boolean retornar=true;
		Connection con=UtilidadBD.abrirConexion();
		if(valorAbonos>0)
		{
			retornar=AbonosYDescuentos.insertarMovimientoAbonos(con, codigoPersona, codigoCita, ConstantesBD.tipoMovimientoAbonoAnulacionReservaAbono, valorAbonos, usuario.getCodigoInstitucionInt(), ingreso, usuario.getCodigoCentroAtencion())>0;
		}
		UtilidadBD.closeConnection(con);
		return retornar;
	}
	
	
	/**
	 * 
	 * @param valorAbonos
	 * @param usuario
	 * @param codigoPersona
	 * @param codigoCita
	 * @return
	 */
	public static boolean reservaAbonosCita(double valorAbonos, UsuarioBasico usuario, int codigoPersona, int codigoCita, Integer ingreso)
	{
		boolean retornar=true;
		Connection con=UtilidadBD.abrirConexion();
		if(valorAbonos>0)
		{
			retornar=AbonosYDescuentos.insertarMovimientoAbonos(con, codigoPersona, codigoCita, ConstantesBD.tipoMovimientoAbonoSalidaReservaAbono, valorAbonos, usuario.getCodigoInstitucionInt(), ingreso, usuario.getCodigoCentroAtencion())>0;
		}
		UtilidadBD.closeConnection(con);
		return retornar;
	}
	


	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param acronimoestadocancelado
	 * @param loginUsuario
	 * @return
	 */
	public static boolean cambiarEstadoCita(Connection con, BigDecimal codigo,String estado, String loginUsuario) 
	{
		return getCitaOdontologicaDao().cambiarEstadoCita(con, codigo, estado, loginUsuario);
	}

//	/**
//	 * 
//	 * Metodo para obtener el ultimo codigo pk de la cita de un programa servicio del plan tratamiento dado el codigo_pk de la tabla programas servicios plan t
//	 * @param codigoPkProgServ
//	 * @param fechaFormatoAppNOREQUERIDA
//	 * @return
//	 * @author   Wilson Rios
//	 * @version  1.0.0 
//	 * @param estado 
//	 * @see
//	 */
//	public static BigDecimal obtenerCodigoPkUltimaCitaProgServPlanT(BigDecimal codigoPkProgServPlanT, String fechaFormatoAppNOREQUERIDA, ArrayList<String> estado) 
//	{
//		return getCitaOdontologicaDao().obtenerCodigoPkUltimaCitaProgServPlanT(codigoPkProgServPlanT, fechaFormatoAppNOREQUERIDA,estado);
//	}
	
	
	/**
	 * CARGA EL CODIGO DE LA CITA 
	 * @author Edgar Carvajal Ruiz
	 * @param dto
	 * @return
	 */
	public static  BigDecimal obtenerCodigoPkUltimaCitaProgServPlanTXEvolucion( DtoPlanTratamientoOdo dto)
	{
		return getCitaOdontologicaDao().obtenerCodigoPkUltimaCitaProgServPlanTXEvolucion(dto);
	}
	
	
	/**
	 * METODO QUE CONSULTA LA CITA CON RESPECTO AL LA EVOLUCION 
	 * NOTA SE ENVIA CON DTO PLAN DE TRATAMIENTO PARA LA ESCALABILIDAD DE LA CONSULTA
	 * @author Edgar Carvajal Ruiz
	 * @param dto
	 * @return
	 */
	public static  DtoCitaOdontologica cargarCitaXEvolucionPlanTratamiento(
			DtoPlanTratamientoOdo dto) 
	{
		return getCitaOdontologicaDao().cargarCitaXEvolucionPlanTratamiento(dto);
	}
	
	
	
	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param codigoCita
	 * @return
	 */
	public static  boolean esCitaConfirmada(int codigoCita) 
	{
		return getCitaOdontologicaDao().esCitaConfirmada(codigoCita);
	}

	/**
	 * Verifica si las citas están siendo atendidas
	 * @param codigoPkCita codigo_pk (Llave primaria) de la cita
	 * @param con Conexión con la BD
	 * @return true en caso de que la cita esté siendo atendida, false de lo contrario
	 */
	public static boolean estaCitaEnAtencion(int codigoPkCita, Connection con) {
		return getCitaOdontologicaDao().estaCitaEnAtencion(codigoPkCita, con);
	}

	
	/**
	 * Método que actualiza el programa Hallazgo pieza de un servicio asociado 
	 * a una cita odontológica
	 * 
	 * @param con
	 * @param programaHallazgoPieza
	 * @param codigoCita
	 * @return
	 */
	public static boolean actualizarProgramaHallazgoPiezaServicioCita(Connection con, long programaHallazgoPieza, long codigoPk) 
	{
		return getCitaOdontologicaDao().actualizarProgramaHallazgoPiezaServicioCita(con, programaHallazgoPieza, codigoPk);
	}
	
	/**
	 * Este mètodo se encarga de consultar el histórico de estados
	 * de una cita odontológica. 
	 *
	 * @param con
	 * @param codigoCita
	 * @return
	 *
	 * @autor Yennifer Guerrero
	 */
	public static ArrayList<DtoCitaOdontologica> obtenerHistoricoEstadosCita(Connection con, int codigoCita){
		return getCitaOdontologicaDao().obtenerHistoricoEstadosCita(con, codigoCita);
	}
	
	public static List<Integer> consultarCitaAsociadaProgramada(Connection con, int codigoCitaProgramada){
		return getCitaOdontologicaDao().consultarCitaAsociadaProgramada(con, codigoCitaProgramada);
	}
	
	public static ArrayList<DtoSolictudCambioServicioCita> obtenerSolicitudesCambioServicio (Connection con, int codigoCita, int codigoInstitucion){
		//return getCitaOdontologicaDao().obtenerSolicitudesCambioServicio(con, codigoCita, codigoInstitucion);
		return new ArrayList<DtoSolictudCambioServicioCita>();
	}
}
