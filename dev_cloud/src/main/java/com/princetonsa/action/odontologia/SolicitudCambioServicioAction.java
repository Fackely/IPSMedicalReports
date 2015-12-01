/**
 * 
 */
package com.princetonsa.action.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoResponsableCobertura;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.odontologia.InfoTarifaServicioPresupuesto;
import util.odontologia.UtilidadOdontologia;

import com.princetonsa.actionform.odontologia.SolicitudCambioServicioForm;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.odontologia.DtoFiltroConsultaServiciosPaciente;
import com.princetonsa.dto.odontologia.DtoMotivosCambioServicio;
import com.princetonsa.dto.odontologia.DtoProcesoCentralizadoConfirmacionCambioServicios;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosAnterioresCita;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosNuevosCita;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;
import com.princetonsa.dto.odontologia.DtoServiciosOdontologiaCambioServicio;
import com.princetonsa.dto.odontologia.DtoSolictudCambioServicioCita;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.CargosOdon;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.odontologia.CitaOdontologica;
import com.princetonsa.mundo.odontologia.MotivosCambioServicioMundo;
import com.princetonsa.mundo.odontologia.ProcesoCentralizadoConfirmacionCambioServicios;
import com.princetonsa.mundo.odontologia.SolicitudCambioServicioMundo;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.UnidadesConsulta;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.odontologia.unidadagendaserviciotipocitaodonto.UnidadAgendaServTipoCitaOdonFabricaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.unidadagendaserviciotipocitaodonto.IUnidadesConsultaServicio;


/**
 * @author armando
 *
 */
public class SolicitudCambioServicioAction extends Action 
{

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if(form instanceof SolicitudCambioServicioForm)
		{
			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			SolicitudCambioServicioForm forma=(SolicitudCambioServicioForm) form;
			forma.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
			if(forma.getEstado().equals("empezar"))
			{
				this.accionEmpezar(forma,usuario);
				this.verificarServiciosAsignadosACita(forma);
				this.verificarChecksOrdenServicio(forma);
				return mapping.findForward("principal");
			}
			if(forma.getEstado().equals("calcularTarifa"))
			{
				ActionErrors errores = new ActionErrors();
				calcularTarifaActualOPresupuestoContratado(forma,usuario,request,errores);
				return mapping.findForward("principal");
			}
			if(forma.getEstado().equals("confirmar"))
			{
				if(forma.getCita().getEstado().equals(ConstantesIntegridadDominio.acronimoReservado))
				{
					ActionErrors errores = new ActionErrors();
					calcularTarifaActualOPresupuestoContratado(forma,usuario,request,errores);
				}
				return this.accionConfirmarCita(forma,usuario,mapping,request);
			}
			if(forma.getEstado().equals("recargar"))
			{
				return mapping.findForward("principal");
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	
	private void verificarChecksOrdenServicio(SolicitudCambioServicioForm forma)
	{
		int proghramaHallazgoPieza=0;
		for(DtoServiciosOdontologiaCambioServicio servicio:forma.getServiciosPaciente())
		{
			if(proghramaHallazgoPieza!=servicio.getServicioOdontologico().getProgramaHallazgoPieza().getCodigoPk())
			{
				proghramaHallazgoPieza=servicio.getServicioOdontologico().getProgramaHallazgoPieza().getCodigoPk();
				servicio.setHabilitarSeleccion(true);
			}
			else
			{
				//int orden=servicio.getServicioOdontologico().getOrdenServicio();
				boolean permitirAsignar=true;
				/* Se quita esta parte temporalmente para permitir asignar
				 * servicios de cualquier orden, mientras se define
				 * como va a quedar
				for(DtoServiciosOdontologiaCambioServicio servicioOrdenInf:forma.getServiciosPaciente())
				{
					if(
							servicioOrdenInf.getServicioOdontologico().getProgramaHallazgoPieza().getCodigoPk()==proghramaHallazgoPieza
							&&
							servicioOrdenInf.getServicioOdontologico().getOrdenServicio()==(orden-1))
					{
						if(!servicioOrdenInf.isServicioCitaAsignada())
						{
							permitirAsignar=false;
							break;
						}
					}
				}*/
				servicio.setHabilitarSeleccion(permitirAsignar);
			}
		}
	}


	/**
	 * 
	 * @param forma
	 * @param mapping 
	 * @param request 
	 */
	private ActionForward accionConfirmarCita(SolicitudCambioServicioForm forma,UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) throws IPSException 
	{
		cargarServiciosNuevaCita(forma);
		DtoSolictudCambioServicioCita dto=cargarDtoCambiarServiciosCita(forma,usuario.getLoginUsuario()); 
		//parametro validar abono para atencion de la cita
		
		DtoProcesoCentralizadoConfirmacionCambioServicios procesoConfirmacion=new DtoProcesoCentralizadoConfirmacionCambioServicios();
			
		ActionErrors errores = new ActionErrors();
		
		forma.setResumen(false);
		Connection con=UtilidadBD.abrirConexion();
		PersonaBasica paciente=new PersonaBasica();
		paciente.setCodigoPersona(forma.getCita().getCodigoPaciente());
		try {
			paciente.cargar(con, paciente.getCodigoPersona());
			paciente.cargarPaciente2(con, paciente.getCodigoPersona(), usuario.getCodigoInstitucion(), usuario.getCodigoCentroAtencion()+"");
		} catch (SQLException e) 
		{
			Log4JManager.error("ERROR CARGANDO EL PACIENTE",e);
		}
		UtilidadBD.closeConnection(con);
		//SI ESTA RESERVADA, O SI ESTA ASIGNADA PERO EL FLUJO ES DESDE EL AGENDAMIENTO, SE DEBE GENERAR LA CONFIRMACION Y LA SOLICITUD.
		Log4JManager.info("1");
		if(dto.getEstadoCita().equals(ConstantesIntegridadDominio.acronimoReservado)||(dto.getEstadoCita().equals(ConstantesIntegridadDominio.acronimoAsignado) && forma.getFlujo().equals("AGENDA")))
		{
			Log4JManager.info("2");
			procesoConfirmacion=ProcesoCentralizadoConfirmacionCambioServicios.generarConfirmacionCambioServiciosOdontologicos(dto,paciente,usuario, true);
			if(dto.getEstadoCita().equals(ConstantesIntegridadDominio.acronimoAsignado) && forma.getFlujo().equals("AGENDA"))
			{
				if(procesoConfirmacion.getErrores().size()>0)
				{
					for(String error:procesoConfirmacion.getErrores())
					{
						errores.add("error.errorEnBlanco", new ActionMessage("error.errorEnBlanco", error));
					}
					saveErrors(request, errores);
					forma.setMostrarTarifas(false);
					return mapping.findForward("paginaErroresActionErrorsSinCabezote");
				}
				forma.setResumen(true);
				forma.setMensaje(new ResultadoBoolean(true,"Proceso exitoso."));
				return mapping.findForward("principal");
			}
			forma.setResumen(true);
			return mapping.findForward("principal");
		}
		else
		{
			Log4JManager.info("3");
			if(!validarAbonoAtencionCitaOdo(paciente,forma))//si no valida abono, debe generar la confirmacio automatica
			{
				Log4JManager.info("4");
				forma.setResumen(true);
				forma.setMensaje(new ResultadoBoolean(true,"Se generó cambio de servicio. Proceso Exitoso."));
				procesoConfirmacion=ProcesoCentralizadoConfirmacionCambioServicios.generarConfirmacionCambioServiciosOdontologicos(dto,paciente,usuario, true);
				
			}
			else
			{
				Log4JManager.info("5");
				if(forma.getaCargoDelPaciente().size()>0)
				{
					double totalSaldoPac=forma.getValorTotalDevolucionPaciente()+forma.getAbonosDisponiblesPaciente();
					if((totalSaldoPac-forma.getValorTotalServiciosPaciente())>=0)
					{
						Log4JManager.info("6");
						forma.setResumen(true);
						forma.setMensaje(new ResultadoBoolean(true,"Se generó cambio de servicio. Proceso Exitoso."));
						procesoConfirmacion=ProcesoCentralizadoConfirmacionCambioServicios.generarConfirmacionCambioServiciosOdontologicos(dto,paciente,usuario, true);
					}
					else
					{
						Log4JManager.info("7");
						if(UtilidadTexto.getBoolean(ValoresPorDefecto.getRequierGenerarSolicitudCambioServicio(forma.getCodigoInstitucion())))
						{
							if(SolicitudCambioServicioMundo.generarSolicitudCambioServicio(dto))
							{
								Log4JManager.info("8");
								forma.setMensaje(new ResultadoBoolean(true,"Se genero soliciutd "+dto.getCodigoPk()+" de cambio de servicio. proceso exitoso."));
								forma.setResumen(true);
							}
							else
							{
								Log4JManager.info("10");
								forma.setMensaje(new ResultadoBoolean(true,"Proceso no exitoso. No se genero solicitud de cambio de servicio."));
							}
							return mapping.findForward("principal");
						}
						else
						{
							Log4JManager.info("11");
							procesoConfirmacion=ProcesoCentralizadoConfirmacionCambioServicios.generarConfirmacionCambioServiciosOdontologicos(dto,paciente,usuario, true);
							
						}
					}
				}
				else
				{
					Log4JManager.info("12");
					forma.setResumen(true);
					forma.setMensaje(new ResultadoBoolean(true,"Se generó cambio de servicio. Proceso Exitoso."));
					procesoConfirmacion=ProcesoCentralizadoConfirmacionCambioServicios.generarConfirmacionCambioServiciosOdontologicos(dto,paciente,usuario, true);
					
				}
			}
		}	
		
		if(procesoConfirmacion.getErrores().size()>0)
		{
			for(String error:procesoConfirmacion.getErrores())
			{
				errores.add("error.errorEnBlanco", new ActionMessage("error.errorEnBlanco", error));
			}
			saveErrors(request, errores);
			forma.setMostrarTarifas(false);
			return mapping.findForward("paginaErroresActionErrorsSinCabezote");
		}
		forma.setDtoConfirmacion(procesoConfirmacion);
		return mapping.findForward(procesoConfirmacion.getForward());
		
	}


	/**
	 *se cargan nuevamente los servicios a el array de acargopaciente y acargoconvenio, ya que despues de calcular la tarifa se pueden modificar los motivos de cambio. 
	 * @param forma
	 */
	private void cargarServiciosNuevaCita(SolicitudCambioServicioForm forma) 
	{
		/*
		 * Este arraylist se envia a la vista para mostrar las tarifas de los sevicios
		 */
		forma.setaCargoDelPaciente(new ArrayList<DtoServiciosOdontologiaCambioServicio>());
		forma.setaCargoDelConvenio(new ArrayList<DtoServiciosOdontologiaCambioServicio>());

		ArrayList<DtoServiciosOdontologiaCambioServicio> listaServicios=forma.getServiciosPaciente();
		for(int i=0;i<listaServicios.size();i++)
		{
			DtoServiciosOdontologiaCambioServicio servicio=listaServicios.get(i);
			if((servicio.isCambiarServicio())||(!servicio.isCambiarServicio()&&servicio.isServicioCitaAsignada()))//si es un servicio que se adiciona, o es un servicio de la cita que se mantiene, se debe calcular la tarifa.
			{
				if(servicio.isCambiarServicio())
				{
					if(!servicio.isServicioCitaAsignada())//si se cambia el servicio, y no era de la cita es por que se esta agregando.
					{
						if(servicio.getServicioOdontologico().getPacientePagaAtencion())
						{
							forma.getaCargoDelPaciente().add(servicio);
						}
						else
						{
							forma.getaCargoDelConvenio().add(servicio);
						}
					}
				}
				else
				{
					if(servicio.getServicioOdontologico().getPacientePagaAtencion())
					{
						forma.getaCargoDelPaciente().add(servicio);
					}
					else
					{
						forma.getaCargoDelConvenio().add(servicio);
					}
				}
			}
		}
	}


	/**
	 * 
	 * @param paciente
	 * @param forma 
	 * @return
	 */
	private boolean validarAbonoAtencionCitaOdo(PersonaBasica paciente, SolicitudCambioServicioForm forma) 
	{
		ArrayList<DtoSubCuentas> listaResponsables=forma.getResponsablesCuenta();
		for(DtoSubCuentas responsable:listaResponsables)
		{
			boolean pacientePagaAtencion=Contrato.pacientePagaAtencion(responsable.getContrato());
			if(pacientePagaAtencion)
			{
				boolean validaAbonoParaAtencionOdont=Contrato.pacienteValidaBonoAtenOdo(responsable.getContrato());
				if(validaAbonoParaAtencionOdont)
				{
					return true;
				}
			}
		}
		return false;
	}


	/**
	 * 
	 * @param forma
	 * @return
	 */
	private DtoSolictudCambioServicioCita cargarDtoCambiarServiciosCita(SolicitudCambioServicioForm forma,String usuario) 
	{
		DtoSolictudCambioServicioCita dto=new DtoSolictudCambioServicioCita();
		
		dto.setCita(forma.getCita());
		dto.setEstado(ConstantesIntegridadDominio.acronimoEstadoSolicitado);
		dto.setFechaSolicitud(UtilidadFecha.getFechaActual());
		dto.setHoraSolicitud(UtilidadFecha.getHoraActual());
		dto.setInstitucion(forma.getCodigoInstitucion());
		dto.setObservacionesGenerales(forma.getObservacionesGenerales());
		dto.setUsuarioSolicita(usuario);
		dto.setCodigoPaciente(forma.getCita().getCodigoPaciente());
		dto.setEstadoCita(forma.getCita().getEstado());
		cargarProgramaServiciosAnterioresNuevos(forma,dto);
		return dto;
	}


	/**
	 * 
	 * @param forma 
	 * @param dto
	 */
	private void cargarProgramaServiciosAnterioresNuevos(SolicitudCambioServicioForm forma, DtoSolictudCambioServicioCita dto) 
	{

		ArrayList<DtoProgramasServiciosNuevosCita> arrayTemporal=new ArrayList<DtoProgramasServiciosNuevosCita>();
		//cargando los nuevos a cargo del paciente.
		for(DtoServiciosOdontologiaCambioServicio servicios:forma.getaCargoDelPaciente())
		{
			DtoProgramasServiciosNuevosCita dtoArray=new DtoProgramasServiciosNuevosCita();
			dtoArray.setCambio(servicios.isServicioCitaAsignada()?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi);
			DtoMotivosCambioServicio motivo=new DtoMotivosCambioServicio();
			motivo.setCodigoPk(servicios.getMotivoCambioServicio());
			dtoArray.setMotivo(motivo);
			dtoArray.setObservaciones(servicios.getObservacionesCambioServicio());
			dtoArray.setProgramaHallazgoPieza(servicios.getServicioOdontologico().getProgramaHallazgoPieza());
			dtoArray.setCodigoPrograma(servicios.getServicioOdontologico().getCodigoPrograma());
			dtoArray.setServicio(servicios.getServicioOdontologico());
			arrayTemporal.add(dtoArray);
		}

		//cargando los nuevos a cargo del convenio
		for(DtoServiciosOdontologiaCambioServicio servicios:forma.getaCargoDelConvenio())
		{
			DtoProgramasServiciosNuevosCita dtoArray=new DtoProgramasServiciosNuevosCita();
			dtoArray.setCambio(servicios.isServicioCitaAsignada()?ConstantesBD.acronimoNo:ConstantesBD.acronimoSi);
			DtoMotivosCambioServicio motivo=new DtoMotivosCambioServicio();
			motivo.setCodigoPk(servicios.getMotivoCambioServicio());
			dtoArray.setMotivo(motivo);
			dtoArray.setObservaciones(servicios.getObservacionesCambioServicio());
			dtoArray.setProgramaHallazgoPieza(servicios.getServicioOdontologico().getProgramaHallazgoPieza());
			dtoArray.setCodigoPrograma(servicios.getServicioOdontologico().getCodigoPrograma());
			dtoArray.setServicio(servicios.getServicioOdontologico());
			arrayTemporal.add(dtoArray);
		}
		
		dto.setProgServNuevos(arrayTemporal);

		//Cargando los viejos
		
		ArrayList<DtoProgramasServiciosAnterioresCita> arrayTemporalAnteriores=new ArrayList<DtoProgramasServiciosAnterioresCita>();
		for(DtoServiciosOdontologiaCambioServicio servicio:forma.getServiciosPaciente())
		{
			if(servicio.isServicioCitaAsignada())
			{
				DtoProgramasServiciosAnterioresCita dtoArray=new DtoProgramasServiciosAnterioresCita();
				dtoArray.setServicio(servicio.getServicioOdontologico().getCodigoServicio());
				dtoArray.setCambio(servicio.isServicioCitaAsignada()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				DtoMotivosCambioServicio motivo=new DtoMotivosCambioServicio();
				motivo.setCodigoPk(servicio.getMotivoCambioServicio());
				dtoArray.setMotivo(motivo);
				dtoArray.setObservaciones(servicio.getObservacionesCambioServicio());
				dtoArray.setProgramaHallazgoPieza(servicio.getServicioOdontologico().getProgramaHallazgoPieza());
				dtoArray.setCodigoPrograma(servicio.getServicioOdontologico().getCodigoPrograma());
				dtoArray.setDtoServicio(servicio.getServicioOdontologico());
				
				arrayTemporalAnteriores.add(dtoArray);
			}
		}
		dto.setProgServAnteriores(arrayTemporalAnteriores);
	}


	private void accionEmpezar(SolicitudCambioServicioForm forma, UsuarioBasico usuario) 
	{
		int codigoCita=forma.getCodigoCitaCambiarServicio();
		forma.reset();
		forma.setCita(CitaOdontologica.obtenerCitaOdontologica(codigoCita, usuario.getCodigoInstitucionInt()));
		
		DtoFiltroConsultaServiciosPaciente filtro=new DtoFiltroConsultaServiciosPaciente();
		filtro.setInstitucion(usuario.getCodigoInstitucionInt());
		filtro.setUnidadAgenda(forma.getCita().getAgendaOdon().getUnidadAgenda());
		filtro.setCodigoMedico(usuario.getCodigoPersona());
		filtro.setCodigoPaciente(forma.getCita().getCodigoPaciente());
		String tipoServicio="";
		if(forma.getCita().getTipo().equals(ConstantesIntegridadDominio.acronimoTipoAtencionTratamiento))
		{
			tipoServicio="'"+ConstantesBD.codigoServicioProcedimiento+"', '"+ConstantesBD.codigoServicioCargosConsultaExterna+"'";
		}
		else
		{
			tipoServicio="'"+ConstantesBD.codigoServicioCargosConsultaExterna+"'";
		}
		//
		filtro.setTipoServicio(tipoServicio);
		filtro.setCodigoCita(forma.getCita().getCodigoPk());
		
		filtro.setActivo(true);
		filtro.setValidadrPresupuestoContratado(ConstantesBD.acronimoSi);
		filtro.setCambiarServicioOdontologico(ConstantesBD.acronimoNo);
		filtro.setCasoBusquedaServicio(ConstantesBD.acronimoConPlanTrataActivoEnProceso);
		filtro.setBuscarPlanTratamiento(true);
		filtro.setOrdenarPorPHP(true);
		cargarServiciosPaciente(forma,UtilidadOdontologia.obtenerServiciosPaciente(filtro));
		forma.setCita(CitaOdontologica.obtenerCitaOdontologica(filtro.getCodigoCita(), filtro.getInstitucion()));
		
		/*
		 * Cargar Informacion Paciente 
		 */
		Paciente paciente= new Paciente();
		Connection con2= UtilidadBD.abrirConexion();
		try {
			paciente.cargarPaciente(con2, forma.getCita().getCodigoPaciente());
			
		} catch (SQLException e) {
			
		}
		
		UtilidadBD.cerrarObjetosPersistencia(null, null, con2);
		
		forma.getCita().setNombrePaciente(paciente.getNombres());
		forma.getCita().setTipoIdentificacionPac(paciente.getTipoIdentificacion(Boolean.TRUE));
		forma.getCita().setNumeroIdentificacionPac(paciente.getNumeroIdentificacion());
		
		
		UtilidadTransaccion.getTransaccion().begin(); //Empezar Transaccion
		IUnidadesConsultaServicio servicioUnidadConsulta=UnidadAgendaServTipoCitaOdonFabricaServicio.crearUnidadConsultaServicio();
		UnidadesConsulta unidadConsulta=servicioUnidadConsulta.buscarUnidadConsultaId(forma.getCita().getAgendaOdon().getUnidadAgenda());
		forma.getCita().getAgendaOdon().setNombreEspecialidadUniAgen(ValoresPorDefecto.getIntegridadDominio(unidadConsulta.getDescripcion()).toString());
		UtilidadTransaccion.getTransaccion().commit(); //Terminar Transaccion
		
		
		
		
	//	DtoUsuario dtoUsuario = new DtoUsuario();
	//	dtoUsuario.setLogin(forma.getCita().getUsuarioConfirma().getLoginUsuario());
	 
	//	DtoPersonas dtoPersona= UtilidadesAdministracion.cargarPersona(dtoUsuario);
		
		
		forma.setMotivosCambios(MotivosCambioServicioMundo.consultarMotivosCambioServicios(usuario.getCodigoInstitucionInt(), ""));
	}


	/**
	 * 
	 * @param forma
	 * @param obtenerServiciosPaciente
	 */
	private void cargarServiciosPaciente(SolicitudCambioServicioForm forma,ArrayList<DtoServicioOdontologico> serviciosPaciente) 
	{
		for(DtoServicioOdontologico dto:serviciosPaciente)
		{
			DtoServiciosOdontologiaCambioServicio dtoSP=new DtoServiciosOdontologiaCambioServicio();
			dtoSP.setServicioOdontologico(dto);
			dtoSP.setServicioCitaAsignada(false);
			dtoSP.setCambiarServicio(false);
			dtoSP.setMotivoCambioServicio(ConstantesBD.codigoNuncaValido);
			dtoSP.setObservacionesCambioServicio("");
			forma.getServiciosPaciente().add(dtoSP);
		}
	}

	private void verificarServiciosAsignadosACita(SolicitudCambioServicioForm forma) 
	{
		for(DtoServiciosOdontologiaCambioServicio servicio:forma.getServiciosPaciente())
		{
			for(DtoServicioCitaOdontologica servicioCita:forma.getCita().getServiciosCitaOdon())
			{
				if(!servicio.isServicioCitaAsignada())
				{
					if(servicio.getServicioOdontologico().getProgramaHallazgoPieza().getCodigoPk()==servicioCita.getCodigoProgramaHallazgoPieza()&&servicio.getServicioOdontologico().getCodigoServicio()==servicioCita.getServicio())
					{
						servicio.setServicioCitaAsignada(true);
					}
					else
					{
						servicio.setServicioCitaAsignada(false);
					}
				}
			}
		}
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param persona
	 * @param errores
	 */
	private void calcularTarifaActualOPresupuestoContratado(	SolicitudCambioServicioForm forma, UsuarioBasico usuario,
																HttpServletRequest request, 
																ActionErrors errores) throws IPSException 
	{
		Connection con=UtilidadBD.abrirConexion();
		PersonaBasica paciente=new PersonaBasica();
		paciente.setCodigoPersona(forma.getCita().getCodigoPaciente());
		try {
			paciente.cargar(con, paciente.getCodigoPersona());
			paciente.cargarPaciente2(con, paciente.getCodigoPersona(), usuario.getCodigoInstitucion(), usuario.getCodigoCentroAtencion()+"");
		} catch (SQLException e) 
		{
			Log4JManager.error("ERROR CARGANDO EL PACIENTE",e);
		}
		

		//se inicializan los arraylist de tarifas
		forma.setaCargoDelPaciente(new ArrayList<DtoServiciosOdontologiaCambioServicio>());
		forma.setaCargoDelConvenio(new ArrayList<DtoServiciosOdontologiaCambioServicio>());
		
		ArrayList<DtoSubCuentas> listaResponsables=UtilidadesHistoriaClinica.obtenerResponsablesIngreso(
							con,
							paciente.getCodigoIngreso(),
							true, // Traer todos los responsables (Facturados y no facturasdos)
							new String[0], // Exluir responsables
							false, // Solamente PYP
							"", // Sub cuenta
							paciente.getCodigoUltimaViaIngreso());
		
		forma.setResponsablesCuenta(listaResponsables);
		
		/*
		 * Este arraylist se envia a la vista para mostrar las tarifas de los sevicios
		 */
		ArrayList<DtoServiciosOdontologiaCambioServicio> listaServicios=forma.getServiciosPaciente();
		double valorTotalServiciosPaciente=0;
		double valorTotalServiciosConvenio=0;
		for(int i=0;i<listaServicios.size();i++)
		{
			DtoServiciosOdontologiaCambioServicio servicio=listaServicios.get(i);
			if((servicio.isCambiarServicio())||(!servicio.isCambiarServicio()&&servicio.isServicioCitaAsignada()))//si es un servicio que se adiciona, o es un servicio de la cita que se mantiene, se debe calcular la tarifa.
			{
				InfoTarifaServicioPresupuesto tarifa=new InfoTarifaServicioPresupuesto();
				InfoResponsableCobertura infoResponsableCobertura=new InfoResponsableCobertura();
				for(int w=0; w<listaResponsables.size(); w++)
				{
					DtoSubCuentas subCuenta= listaResponsables.get(w);
					if(servicio.getServicioOdontologico().getCodigoPresuOdoProgSer()>0)
					{
						CargosOdon.obtenerInfoPresupuestoContratadoProgSer(con,servicio.getServicioOdontologico().getCodigoPresuOdoProgSer(), infoResponsableCobertura, tarifa, UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(forma.getCodigoInstitucion())), servicio.getServicioOdontologico().getCodigoServicio(), new BigDecimal(servicio.getServicioOdontologico().getCodigoPlanTratamiento()));
			
						if(!tarifa.getError().equals(""))
						{
							tarifa.setError(tarifa.getError()+" para el servicio "+servicio.getServicioOdontologico().getDescripcionServicio());
						}
						
					}
					else
					{
						Log4JManager.info("Obtener info Tarifa Unitaria Por Servicio ");
						infoResponsableCobertura = Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), servicio.getServicioOdontologico().getCodigoServicio(), Integer.parseInt(usuario.getCodigoInstitucion()), false, subCuenta.getSubCuenta());
						int convenio=infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo();
						tarifa=CargosOdon.obtenerTarifaUnitariaXServicio(servicio.getServicioOdontologico().getCodigoServicio(), convenio, subCuenta.getContrato(), "", usuario.getCodigoInstitucionInt(), new BigDecimal(paciente.getCodigoCuenta()), false /*en este punto no existe programa, por esa razon la cobertura se hace a nivel del servicio*/, usuario.getCodigoCentroCosto());
					}
					
					if(tarifa.getError().equals(""))
					{
						// Como no hay error encontro cobertura y tarifa
						servicio.getServicioOdontologico().setInfoTarifa(tarifa);
						servicio.getServicioOdontologico().setResponsableServicio(infoResponsableCobertura.getDtoSubCuenta());
						servicio.getServicioOdontologico().setPacientePagaAtencion(Contrato.pacientePagaAtencion(infoResponsableCobertura.getDtoSubCuenta().getContrato()));
						if(servicio.isCambiarServicio())
						{
							if(!servicio.isServicioCitaAsignada())//si se cambia el servicio, y no era de la cita es por que se esta agregando.
							{
								if(servicio.getServicioOdontologico().getPacientePagaAtencion())
								{
									valorTotalServiciosPaciente+=tarifa.getValorTarifaTotalConDctos().doubleValue();
									forma.getaCargoDelPaciente().add(servicio);
								}
								else
								{
									valorTotalServiciosConvenio+=tarifa.getValorTarifaTotalConDctos().doubleValue();
									forma.getaCargoDelConvenio().add(servicio);
								}
							}
						}
						else
						{
							if(servicio.getServicioOdontologico().getPacientePagaAtencion())
							{
								valorTotalServiciosPaciente+=tarifa.getValorTarifaTotalConDctos().doubleValue();
								forma.getaCargoDelPaciente().add(servicio);
							}
							else
							{
								valorTotalServiciosConvenio+=tarifa.getValorTarifaTotalConDctos().doubleValue();
								forma.getaCargoDelConvenio().add(servicio);
							}
						}
						// no debo validar nada mï¿½s
						w=listaResponsables.size();
						break;
					}
				}
				if(tarifa==null || !tarifa.getError().equals(""))
				{
					errores.add("error tarifa", new ActionMessage("errors.notEspecific", tarifa.getError()));
				}
			}
		}
		if(errores.isEmpty())
		{
			forma.setMostrarTarifas(true);
			forma.setValorTotalServiciosPaciente(valorTotalServiciosPaciente);
			forma.setValorTotalServiciosConvenio(valorTotalServiciosConvenio);
			double abonoCita=Utilidades.obtenerAbonoPacienteTipoYNumeroDocumento(paciente.getCodigoPersona(),ConstantesBD.tipoMovimientoAbonoSalidaReservaAbono,forma.getCita().getCodigoPk())-Utilidades.obtenerAbonoPacienteTipoYNumeroDocumento(paciente.getCodigoPersona(),ConstantesBD.tipoMovimientoAbonoAnulacionReservaAbono,forma.getCita().getCodigoPk());
			forma.setValorTotalDevolucionPaciente(abonoCita);
			forma.setAbonosDisponiblesPaciente(Utilidades.obtenerAbonosDisponiblesPaciente(paciente.getCodigoPersona(),paciente.getCodigoIngreso(), usuario.getCodigoInstitucionInt()));
		}
		else
		{
			forma.setMostrarTarifas(false);
			saveErrors(request, errores);
		}
		UtilidadBD.closeConnection(con);
	}
}
