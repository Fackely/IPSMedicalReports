/**
 * 
 */
package com.princetonsa.mundo.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoResponsableCobertura;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.odontologia.InfoTarifaServicioPresupuesto;

import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoProcesoCentralizadoConfirmacionCambioServicios;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosAnterioresCita;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosNuevosCita;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;
import com.princetonsa.dto.odontologia.DtoSolictudCambioServicioCita;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.CargosOdon;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author armando
 *
 */
public class ProcesoCentralizadoConfirmacionCambioServicios 
{
	
	/**
	 * 
	 */
	private static Logger logger=Logger.getLogger(ProcesoCentralizadoConfirmacionCambioServicios.class);
	
	/**
	 * @param paciente 
	 * @param usuario 
	 * 
	 */
	public static DtoProcesoCentralizadoConfirmacionCambioServicios generarConfirmacionCambioServiciosOdontologicos(DtoSolictudCambioServicioCita solicitud,PersonaBasica paciente, UsuarioBasico usuario, boolean generarSolicitudAutomatica) throws IPSException
	{
		
		DtoProcesoCentralizadoConfirmacionCambioServicios proceso=new DtoProcesoCentralizadoConfirmacionCambioServicios();
		
		//1. calcular la tarifa de los nuevos servicios de la cita.
		calcularTarifaNuevosServiciosCita(proceso,solicitud,paciente,usuario);
		
		if(solicitud.getCita().getEstado().equals(ConstantesIntegridadDominio.acronimoReservado))
		{
			generarConfirmacionCambioServicio(proceso,solicitud,usuario,paciente,generarSolicitudAutomatica);
			generarNuevaReservaCitaOdontologica(proceso,solicitud,usuario,paciente);
		}
		else
		{
			//2 calcular el valor del saldo abono disponible del paciente.
			calcularSaldoAbonoDisponiblePaciente(proceso,solicitud,usuario,paciente);
			
			//3 validar si requiere abonos.
			validarAbonoRequerido(proceso,solicitud);
			
			//si no se han presentado errores, se continua con el proceso.
			if(proceso.getErrores().size()<=0)
			{
				//4. registrar reversion reserva de abonos.
				CitaOdontologica.devolverReservaAbonosCita(proceso.getValorAbonoDevolucion(), usuario, paciente.getCodigoPersona(), solicitud.getCita().getCodigoPk(), paciente.getCodigoIngreso()); 
				
				//5. registrar reservar abonos con nuevos servicios
				CitaOdontologica.reservaAbonosCita(proceso.getValorTotalServicios(), usuario, paciente.getCodigoPersona(), solicitud.getCita().getCodigoPk(), paciente.getCodigoIngreso());
	
				//6 registrar actualización de la cita con los nuevos servicios
				generarNuevaCitaOdontologica(proceso,solicitud,usuario,paciente);
	
				//si no se presentaron errores, entonces genera la confirmación.
				if(proceso.getErrores().size()<=0)
				{
					generarConfirmacionCambioServicio(proceso,solicitud,usuario,paciente,generarSolicitudAutomatica);
					proceso.setForward("principal");
					//proceso.setForward("paginaPrincipalConfirmacionCambioServicio");
	
				}
				else
				{
					proceso.setForward("paginaErroresActionErrorsSinCabezote");
				}
			}
			else
			{
				proceso.setForward("paginaErroresActionErrorsSinCabezote");
			}
		}
		return proceso;
	}

	
	/**
	 * 
	 * @param proceso
	 * @param solicitud
	 * @param usuario
	 * @param paciente
	 * @param generarSolicitudAutomatica 
	 */
	private static void generarConfirmacionCambioServicio(
			DtoProcesoCentralizadoConfirmacionCambioServicios proceso,
			DtoSolictudCambioServicioCita solicitud, UsuarioBasico usuario,
			PersonaBasica paciente, boolean generarSolicitudAutomatica) 
	{
		solicitud.setFechaConfirma(UtilidadFecha.getFechaActual());
		solicitud.setHoraConfirma(UtilidadFecha.getHoraActual());
		solicitud.setUsuarioConfirma(usuario.getLoginUsuario());
		solicitud.setEstado(ConstantesIntegridadDominio.acronimoEstadoConfirmado);
		if(generarSolicitudAutomatica)
		{
			SolicitudCambioServicioMundo.generarSolicitudCambioServicio(solicitud);
			SolicitudCambioServicioMundo.confirmarSolicitudCambioServicio(solicitud,ConstantesBD.acronimoSi);
		}
		else
		{
			SolicitudCambioServicioMundo.confirmarSolicitudCambioServicio(solicitud,ConstantesBD.acronimoNo);
		}
		proceso.setSolicitud(solicitud);
	}


	
	
	
	
	/**
	 * 
	 * @param proceso
	 * @param solicitud
	 * @param usuario
	 * @param paciente
	 */
	private static void generarNuevaReservaCitaOdontologica(
			DtoProcesoCentralizadoConfirmacionCambioServicios proceso,
			DtoSolictudCambioServicioCita solicitud, UsuarioBasico usuario,
			PersonaBasica paciente) 
	{
		
		
		logger.info("\n\n Cambiar Servicio");
		int totalDuracionServicios=0;
		for(DtoProgramasServiciosNuevosCita servicioNuevos:solicitud.getProgServNuevos())
		{
			totalDuracionServicios=totalDuracionServicios+servicioNuevos.getServicio().getMinutosduracionNuevos();
		}
		solicitud.getCita().setHoraFinal(UtilidadFecha.incrementarMinutosAHora(solicitud.getCita().getHoraInicio(), totalDuracionServicios));
		solicitud.getCita().setDuracion(totalDuracionServicios);
								
		modificarCitaXReserva(usuario,paciente,solicitud,proceso);
		
		
		/*
		
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		String estadoCita="";
		DtoCitaOdontologica cita=solicitud.getCita();
		cita.setUsuarioModifica(usuario.getLoginUsuario());
		
						
		
		ArrayList<DtoServicioCitaOdontologica> serviciosViejosCitaOdonAbono= new ArrayList<DtoServicioCitaOdontologica>();
		serviciosViejosCitaOdonAbono=CitaOdontologica.consultarServiciosCitaOdontologica(con, solicitud.getCita().getCodigoPk(),usuario.getCodigoInstitucionInt()); 
		
		
		//elimina cargos
		for(DtoServicioCitaOdontologica elem: serviciosViejosCitaOdonAbono)
		{
			if(elem.getFacturado().equals(ConstantesBD.acronimoNo))
			{
				elem.setEliminarSer(ConstantesBD.acronimoSi);
			}
		}
		
		ArrayList<DtoServicioCitaOdontologica> listaServiciosParaModificarSolicitud=new ArrayList<DtoServicioCitaOdontologica>();

		
		for(DtoProgramasServiciosNuevosCita servicioNuevos:solicitud.getProgServNuevos())
		{
			DtoServicioOdontologico servicio=servicioNuevos.getServicio();
			DtoServicioCitaOdontologica servicioCita=new DtoServicioCitaOdontologica();
			
			servicioCita.setCitaOdontologica(solicitud.getCita().getCodigoPk());
			servicioCita.setUnidadAgenda(solicitud.getCita().getAgendaOdon().getUnidadAgenda());
			servicioCita.setServicio(servicio.getCodigoServicio());
			servicioCita.setDuracion(servicio.getMinutosduracionNuevos());
			servicioCita.setProgramaHallazgoPieza(servicio.getProgramaHallazgoPieza());
			servicioCita.setCodigoPresuOdoProgSer(servicio.getCodigoPresuOdoProgSer());
			servicioCita.setGarantiaServicio(servicio.getGarantiaServicio());
			servicioCita.setCodigoTipoServicio(servicio.getCodigoTipoServicio());
			servicioCita.setNombreServicio(servicio.getDescripcionServicio());
			servicioCita.setInfoTarifa(servicio.getInfoTarifa());

			
			servicioCita.setTipoCita(solicitud.getCita().getTipo());
			servicioCita.setEstadoCita(solicitud.getCita().getEstado());
			servicioCita.setCodigoAgenda(solicitud.getCita().getAgenda());
			int contrato=servicio.getResponsableServicio().getContrato();
			servicioCita.setInfoResponsableCobertura(new InfoResponsableCobertura());
			servicioCita.getInfoResponsableCobertura().setDtoSubCuenta(new DtoSubCuentas());
			servicioCita.getInfoResponsableCobertura().getDtoSubCuenta().setContrato(contrato);
			servicioCita.getInfoResponsableCobertura().getDtoSubCuenta().setSubCuenta(servicio.getResponsableServicio().getSubCuenta());
			servicioCita.setFechaCita(solicitud.getCita().getAgendaOdon().getFecha());
			servicioCita.setHoraInicio(cita.getHoraInicio());
			servicioCita.setHoraFinal(cita.getHoraFinal());
			servicioCita.setActivo(ConstantesBD.acronimoSi);
			servicioCita.setUsuarioModifica(usuario.getLoginUsuario());
			
			boolean pacientePagaAtencion=Contrato.pacientePagaAtencion(contrato);
							
			if(pacientePagaAtencion)
			{
				servicio.setAplicaAnticipo(ConstantesBD.acronimoNo);
				servicio.setAplicaAbono(ConstantesBD.acronimoSi);
				servicioCita.setAplicaAnticipo(ConstantesBD.acronimoNo);
				servicioCita.setAplicaAbono(ConstantesBD.acronimoSi);
			}
			else
			{
				servicio.setAplicaAnticipo(ConstantesBD.acronimoSi);
				servicio.setAplicaAbono(ConstantesBD.acronimoNo);
				servicioCita.setAplicaAnticipo(ConstantesBD.acronimoSi);
				servicioCita.setAplicaAbono(ConstantesBD.acronimoNo);
			}
			boolean garantia=servicio.getInfoTarifa()==null||servicio.getInfoTarifa()!=null&&servicio.getGarantiaServicio().equals(ConstantesBD.acronimoSi);
			listaServiciosParaModificarSolicitud.add(servicioCita);
		}
		
							
		DtoCitaOdontologica dto=cita;
		dto.setServiciosCitaOdon(listaServiciosParaModificarSolicitud);		
		if(!CitaOdontologica.actualizarCitaOdontologicaYServicios(con, dto))
		{
			UtilidadBD.abortarTransaccion(con);
			proceso.getErrores().add("ERROR ACTUALIZANDO LA CITA Y SUS SERVICIOS");
		}
		
		
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
		
		proceso.setProcesoExito(true);
		
		*/
	}

	
	private static void modificarCitaXReserva(UsuarioBasico usuario,
			PersonaBasica paciente, DtoSolictudCambioServicioCita solicitud,
			DtoProcesoCentralizadoConfirmacionCambioServicios proceso) 
	{		
		String estadoCita=solicitud.getCita().getEstado();
		
		HashMap parametros= new HashMap();
		parametros.put("codigoCita", solicitud.getCita().getCodigoPk());
		parametros.put("fecha",solicitud.getCita().getFechaReserva());
		parametros.put("horaInicioCita", solicitud.getCita().getHoraInicio());	
		parametros.put("horaFinalCita", solicitud.getCita().getHoraFinal());
		parametros.put("horaInicio",solicitud.getCita().getHoraInicio());
		parametros.put("horaFinal", solicitud.getCita().getHoraFinal());
		parametros.put("codAgendaAnterior",solicitud.getCita().getAgenda());
		parametros.put("codAgendaActual",solicitud.getCita().getAgendaOdon().getCodigoPk());
		parametros.put("codUnidadAgendaAnterior",solicitud.getCita().getAgendaOdon().getUnidadAgenda());
		parametros.put("codUnidadAgendaActual",solicitud.getCita().getAgendaOdon().getUnidadAgenda());
		parametros.put("usuario", usuario.getLoginUsuario());
		parametros.put("codInstitucion", usuario.getCodigoInstitucionInt());
		parametros.put("estadoCita", estadoCita);
	    
		int codigoPKCita=solicitud.getCita().getCodigoPk();				
	
		DtoCitaOdontologica dto = new DtoCitaOdontologica();
		dto.setCodigoPk(codigoPKCita);
		dto.setEstado(ConstantesIntegridadDominio.acronimoReservado);
		dto.setTipo(solicitud.getCita().getTipo());		
		dto.setAgenda(solicitud.getCita().getAgendaOdon().getCodigoPk());
		dto.setDuracion(solicitud.getCita().getDuracion());
		dto.setHoraInicio(solicitud.getCita().getHoraInicio());
		dto.setHoraFinal(solicitud.getCita().getHoraFinal());
		dto.setFechaReserva(UtilidadFecha.getFechaActual());
		dto.setHoraReserva(UtilidadFecha.getHoraActual());
		dto.setUsuarioReserva(usuario.getLoginUsuario());
		dto.setPorConfirmar(ConstantesBD.acronimoNo);
		dto.setCodigoPaciente(paciente.getCodigoPersona());// 2420
		//dto.setConvenio(Utilidades.convertirAEntero(forma.getConvenioPac()));// persona.getCodigoConvenio() - ConstantesBD.codigoNuncaValido 
		dto.setUsuarioModifica(usuario.getLoginUsuario());
		dto.setFechaConfirmacion(solicitud.getCita().getFechaConfirmacion());
		dto.setHoraConfirmacion(solicitud.getCita().getHoraConfirmacion());
		dto.setConfirmacion(solicitud.getCita().getConfirmacion());
		dto.setObservacionesConfirmacion(solicitud.getCita().getObservacionesConfirmacion());
		
		for(DtoProgramasServiciosNuevosCita servicioNuevos:solicitud.getProgServNuevos())
		{
			DtoServicioCitaOdontologica dtoSer = new DtoServicioCitaOdontologica();
			dtoSer.setServicio(servicioNuevos.getServicio().getCodigoServicio());
			dtoSer.setDuracion(servicioNuevos.getServicio().getMinutosduracionNuevos());
			dtoSer.setProgramaHallazgoPieza(servicioNuevos.getServicio().getProgramaHallazgoPieza());

			dtoSer.setActivo(ConstantesBD.acronimoSi);
			dtoSer.setUsuarioModifica(usuario.getLoginUsuario());
			dtoSer.setEstadoCita(solicitud.getCita().getEstado());
			dtoSer.setCodigoAgenda(solicitud.getCita().getAgendaOdon().getCodigoPk());
			dtoSer.setFechaCita(solicitud.getCita().getAgendaOdon().getFecha());
			dtoSer.setHoraInicio(solicitud.getCita().getHoraInicio());
			dtoSer.setHoraFinal(solicitud.getCita().getHoraFinal());
			dtoSer.setUnidadAgenda(solicitud.getCita().getAgendaOdon().getUnidadAgenda());
			dtoSer.setAplicaAbono(ConstantesBD.acronimoNo);
			dtoSer.setAplicaAnticipo(ConstantesBD.acronimoNo);
			dto.getServiciosCitaOdon().add(dtoSer);
		}
		
		AgendaOdontologica.modificarCitaOdontologica(dto, parametros);
	}


	/**
	 * 
	 * @param proceso
	 * @param solicitud
	 * @param usuario
	 * @param paciente
	 */
	private static void generarNuevaCitaOdontologica(
			DtoProcesoCentralizadoConfirmacionCambioServicios proceso,
			DtoSolictudCambioServicioCita solicitud, UsuarioBasico usuario,
			PersonaBasica paciente) 
	{
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		String estadoCita="";
		DtoCitaOdontologica cita=solicitud.getCita();
		cita.setUsuarioModifica(usuario.getLoginUsuario());
		
						
		
		ArrayList<DtoServicioCitaOdontologica> serviciosViejosCitaOdonAbono= new ArrayList<DtoServicioCitaOdontologica>();
		serviciosViejosCitaOdonAbono=CitaOdontologica.consultarServiciosCitaOdontologica(con, solicitud.getCita().getCodigoPk(),usuario.getCodigoInstitucionInt(), paciente.getCodigoPersona()); 
		
		
		
		//elimina cargos
		for(DtoServicioCitaOdontologica elem: serviciosViejosCitaOdonAbono)
		{
			if(elem.getFacturado().equals(ConstantesBD.acronimoNo))
			{
				if(!Solicitud.cambiarEstadosSolicitudStatico(con,
						elem.getNumeroSolicitud(), 
						ConstantesBD.codigoEstadoFAnulada, 
						ConstantesBD.codigoEstadoHCAnulada).isTrue())
				{
					logger.info("se aborta la transaccion por errores en la anulacion de solicitudes");
					UtilidadBD.abortarTransaccion(con);
					proceso.getErrores().add("se aborta la transaccion por errores en la anulacion de solicitudes");
					
					break;
				}
				elem.setEliminarSer(ConstantesBD.acronimoSi);
			}
		}

		ArrayList<DtoServicioCitaOdontologica> listaServiciosParaModificarSolicitud=new ArrayList<DtoServicioCitaOdontologica>();

		
		for(DtoProgramasServiciosNuevosCita servicioNuevos:solicitud.getProgServNuevos())
		{
			DtoServicioOdontologico servicio=servicioNuevos.getServicio();
			DtoServicioCitaOdontologica servicioCita=new DtoServicioCitaOdontologica();
			
			servicioCita.setCitaOdontologica(solicitud.getCita().getCodigoPk());
			servicioCita.setUnidadAgenda(solicitud.getCita().getAgendaOdon().getUnidadAgenda());
			servicioCita.setServicio(servicio.getCodigoServicio());
			servicioCita.setDuracion(servicio.getMinutosduracionNuevos());
			servicioCita.setProgramaHallazgoPieza(servicio.getProgramaHallazgoPieza());
			servicioCita.setCodigoPresuOdoProgSer(servicio.getCodigoPresuOdoProgSer());
			servicioCita.setGarantiaServicio(servicio.getGarantiaServicio());
			servicioCita.setCodigoTipoServicio(servicio.getCodigoTipoServicio());
			servicioCita.setNombreServicio(servicio.getDescripcionServicio());
			servicioCita.setInfoTarifa(servicio.getInfoTarifa());

			if(servicio.getInfoTarifa() != null) {
				servicioCita.setValorTarifa(servicio.getInfoTarifa().getValorTarifaTotalConDctos());
			}
			
			logger.info("\n\n\n\n\n\n\n\n\n\n*************************************   *********************************  servicioCita  ---> "+servicio.getInfoTarifa().getEstadoFacturacion());
			
			servicioCita.setTipoCita(solicitud.getCita().getTipo());
			servicioCita.setEstadoCita(solicitud.getCita().getEstado());
			servicioCita.setCodigoAgenda(solicitud.getCita().getAgenda());
			int contrato=servicio.getResponsableServicio().getContrato();
			servicioCita.setInfoResponsableCobertura(new InfoResponsableCobertura());
			servicioCita.getInfoResponsableCobertura().setDtoSubCuenta(new DtoSubCuentas());
			servicioCita.getInfoResponsableCobertura().getDtoSubCuenta().setContrato(contrato);
			servicioCita.getInfoResponsableCobertura().getDtoSubCuenta().setSubCuenta(servicio.getResponsableServicio().getSubCuenta());
			servicioCita.setFechaCita(solicitud.getCita().getAgendaOdon().getFecha());
			servicioCita.setHoraInicio(cita.getHoraInicio());
			servicioCita.setHoraFinal(cita.getHoraFinal());
			servicioCita.setActivo(ConstantesBD.acronimoSi);
			servicioCita.setUsuarioModifica(usuario.getLoginUsuario());
			
			boolean pacientePagaAtencion=Contrato.pacientePagaAtencion(contrato);
							
			if(pacientePagaAtencion)
			{
				servicio.setAplicaAnticipo(ConstantesBD.acronimoNo);
				servicio.setAplicaAbono(ConstantesBD.acronimoSi);
				servicioCita.setAplicaAnticipo(ConstantesBD.acronimoNo);
				servicioCita.setAplicaAbono(ConstantesBD.acronimoSi);
			}
			else
			{
				servicio.setAplicaAnticipo(ConstantesBD.acronimoSi);
				servicio.setAplicaAbono(ConstantesBD.acronimoNo);
				servicioCita.setAplicaAnticipo(ConstantesBD.acronimoSi);
				servicioCita.setAplicaAbono(ConstantesBD.acronimoNo);
			}
			boolean garantia=servicio.getInfoTarifa()==null||servicio.getInfoTarifa()!=null&&servicio.getGarantiaServicio().equals(ConstantesBD.acronimoSi);
			//int numeroSolicitud=CitaOdontologica.generarSolicitud(con, servicioCita, paciente, usuario, solicitud.getCita().getCodigoCentroCosto(), solicitud.getCita().getAgendaOdon().getEspecialidadUniAgen(), usuario.getCodigoPersona(), ConstantesBD.codigoOcupacionMedicaNinguna, new ActionErrors(), garantia).intValue();
			int numeroSolicitud = 0;
			
			logger.info("\n\nnumero de solicitud: "+numeroSolicitud);
			
			servicioCita.setNumeroSolicitud(numeroSolicitud);
			listaServiciosParaModificarSolicitud.add(servicioCita);
		}
		
							
		//cambia el estado de la cita a asignada
		DtoCitaOdontologica dto=cita;
		dto.setServiciosCitaOdon(listaServiciosParaModificarSolicitud);		
		if(!CitaOdontologica.actualizarCitaOdontologicaYServicios(con, dto, true))
		{
			UtilidadBD.abortarTransaccion(con);
			proceso.getErrores().add("ERROR ACTUALIZANDO LA CITA Y SUS SERVICIOS");
		}
		
		
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
		
		proceso.setProcesoExito(true);
	}


	/**
	 * 
	 * @param proceso
	 * @param solicitud
	 */
	private static void validarAbonoRequerido(
			DtoProcesoCentralizadoConfirmacionCambioServicios proceso,
			DtoSolictudCambioServicioCita solicitud) 
	{
		for(DtoSubCuentas responsable:proceso.getResponsables())
		{
			boolean pacientePagaAtencion=Contrato.pacientePagaAtencion(responsable.getContrato());
			if(pacientePagaAtencion)
			{
				boolean validaAbonoParaAtencionOdont=Contrato.pacienteValidaBonoAtenOdo(responsable.getContrato());
				if(validaAbonoParaAtencionOdont)
				{
					if(proceso.getValorTotalServicios()>proceso.getValorAbonoDisponiblePaciente())
					{
						proceso.getErrores().add("Los abonos disponibles "+proceso.getValorAbonoDisponiblePaciente()+" no cubre valor total del servicio: "+proceso.getValorTotalServicios());
					}
				}
			}
		}
	}


	/**
	 * 
	 * @param proceso
	 * @param solicitud
	 * @param usuario
	 * @param paciente
	 */
	private static void calcularSaldoAbonoDisponiblePaciente(
			DtoProcesoCentralizadoConfirmacionCambioServicios proceso,
			DtoSolictudCambioServicioCita solicitud, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		double abonoCita=Utilidades.obtenerAbonoPacienteTipoYNumeroDocumento(paciente.getCodigoPersona(),ConstantesBD.tipoMovimientoAbonoSalidaReservaAbono,solicitud.getCita().getCodigoPk())-Utilidades.obtenerAbonoPacienteTipoYNumeroDocumento(paciente.getCodigoPersona(),ConstantesBD.tipoMovimientoAbonoAnulacionReservaAbono,solicitud.getCita().getCodigoPk());
		proceso.setValorAbonoDevolucion(abonoCita);
		double valorAbono=proceso.getValorAbonoDevolucion()+Utilidades.obtenerAbonosDisponiblesPaciente(paciente.getCodigoPersona(),paciente.getCodigoIngreso(), usuario.getCodigoInstitucionInt());
		logger.info("*************ABONOS********");
		logger.info("valor_abono--->"+valorAbono);
		logger.info("valro abono descuento--->"+proceso.getValorAbonoDevolucion());
		logger.info("abono actualpaciente--->"+(valorAbono-proceso.getValorAbonoDevolucion()));
		proceso.setValorAbonoDisponiblePaciente(valorAbono);

	}

	/**
	 * 
	 * @param proceso
	 * @param solicitud
	 * @param paciente
	 * @param usuario
	 */
	private static void calcularTarifaNuevosServiciosCita(
			DtoProcesoCentralizadoConfirmacionCambioServicios proceso,
			DtoSolictudCambioServicioCita solicitud, PersonaBasica paciente, UsuarioBasico usuario) throws IPSException 
	{
		Connection con=UtilidadBD.abrirConexion();
		
		proceso.setResponsables(UtilidadesHistoriaClinica.obtenerResponsablesIngreso(
				con,
				paciente.getCodigoIngreso(),
				true, // Traer todos los responsables (Facturados y no facturados)
				new String[0], // Excluir responsables
				false, // Solamente PYP
				"", // Sub cuenta
				paciente.getCodigoUltimaViaIngreso()));

		ArrayList<DtoProgramasServiciosNuevosCita> listaServicios=solicitud.getProgServNuevos();
		double valorTotalServicios=0;
		
		for(int i=0;i<listaServicios.size();i++)
		{
			DtoServicioOdontologico servicio=listaServicios.get(i).getServicio();
			
			if(UtilidadTexto.getBoolean(servicio.getGarantiaServicio()))
			{
				servicio.setInfoTarifa(null);
			}
			else
			{
				InfoTarifaServicioPresupuesto tarifa=new InfoTarifaServicioPresupuesto();
				InfoResponsableCobertura infoResponsableCobertura=new InfoResponsableCobertura();
				for(int w=0; w<proceso.getResponsables().size(); w++)
				{
					DtoSubCuentas subCuenta= proceso.getResponsables().get(w);
					logger.info("\n\n Codigo Presupuesto Prog >> "+ servicio.getCodigoPresuOdoProgSer());
					if(servicio.getCodigoPresuOdoProgSer()>0)
					{
						logger.info("OBTENER INFO presupuesto >> ");
						CargosOdon.obtenerInfoPresupuestoContratadoProgSer(con,servicio.getCodigoPresuOdoProgSer(), infoResponsableCobertura, tarifa, UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(solicitud.getInstitucion())), servicio.getCodigoServicio(), new BigDecimal(listaServicios.get(i).getServicio().getCodigoPlanTratamiento()));
			
						if(!tarifa.getError().equals(""))
						{
							tarifa.setError(tarifa.getError()+" para el servicio "+servicio.getDescripcionServicio());
						}
						
					}
					else
					{
						logger.info("Obtener info Tarifa Unitaria Por Servicio ");
						infoResponsableCobertura = Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), servicio.getCodigoServicio(), Integer.parseInt(usuario.getCodigoInstitucion()), false, subCuenta.getSubCuenta());
						int convenio=infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo();
						tarifa=CargosOdon.obtenerTarifaUnitariaXServicio(servicio.getCodigoServicio(), convenio, subCuenta.getContrato(), "", usuario.getCodigoInstitucionInt(), new BigDecimal(paciente.getCodigoCuenta()), false /*en este punto no existe programa, por esa razon la cobertura se hace a nivel del servicio*/, usuario.getCodigoCentroCosto());
					}
					
					if(tarifa.getError().equals(""))
					{
						// Como no hay error encontro cobertura y tarifa
						servicio.setInfoTarifa(tarifa);
						servicio.setResponsableServicio(infoResponsableCobertura.getDtoSubCuenta());
						servicio.setPacientePagaAtencion(Contrato.pacientePagaAtencion(infoResponsableCobertura.getDtoSubCuenta().getContrato()));
						if(servicio.getPacientePagaAtencion())
						{
							valorTotalServicios+=tarifa.getValorTarifaTotalConDctos().doubleValue();
						}
						// no debo validar nada más
						w=proceso.getResponsables().size();
						break;
					}
				}
				if(tarifa==null || !tarifa.getError().equals(""))
				{
					proceso.getErrores().add("No se pudo calcular la tarifa del servicio, por favor verifique");
				}
			}
		}
		if(proceso.getErrores().size()>0)
		{
			proceso.setValorTotalServicios(0);
		}
		else
		{
			proceso.setValorTotalServicios(valorTotalServicios);
		}
	}


	/**
	 * 
	 * @param proceso
	 * @param solicitud
	 * @param paciente
	 * @param usuario
	 */
	public static void calcularTarifaServiciosCita(
			DtoProcesoCentralizadoConfirmacionCambioServicios proceso,
			DtoSolictudCambioServicioCita solicitud, PersonaBasica paciente, UsuarioBasico usuario) throws IPSException 
	{
		Connection con=UtilidadBD.abrirConexion();
		
		proceso.setResponsables(UtilidadesHistoriaClinica.obtenerResponsablesIngreso(
				con,
				paciente.getCodigoIngreso(),
				true, // Traer todos los responsables (Facturados y no facturados)
				new String[0], // Excluir responsables
				false, // Solamente PYP
				"", // Sub cuenta
				paciente.getCodigoUltimaViaIngreso()));
	
		ArrayList<DtoProgramasServiciosNuevosCita> listaServicios=solicitud.getProgServNuevos();
		double valorTotalServicios=0;
		
		for(int i=0;i<listaServicios.size();i++)
		{
			DtoServicioOdontologico servicio=listaServicios.get(i).getServicio();
			
			if(UtilidadTexto.getBoolean(servicio.getGarantiaServicio()))
			{
				servicio.setInfoTarifa(null);
			}
			else
			{
				InfoTarifaServicioPresupuesto tarifa=new InfoTarifaServicioPresupuesto();
				InfoResponsableCobertura infoResponsableCobertura=new InfoResponsableCobertura();
				for(int w=0; w<proceso.getResponsables().size(); w++)
				{
					DtoSubCuentas subCuenta= proceso.getResponsables().get(w);
					logger.info("\n\n Codigo Presupuesto Prog >> "+ servicio.getCodigoPresuOdoProgSer());
					if(servicio.getCodigoPresuOdoProgSer()>0)
					{
						logger.info("OBTENER INFO presupuesto >> ");
						CargosOdon.obtenerInfoPresupuestoContratadoProgSer(con,servicio.getCodigoPresuOdoProgSer(), infoResponsableCobertura, tarifa, UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(solicitud.getInstitucion())), servicio.getCodigoServicio(), new BigDecimal(listaServicios.get(i).getServicio().getCodigoPlanTratamiento()));
			
						if(!tarifa.getError().equals(""))
						{
							tarifa.setError(tarifa.getError()+" para el servicio "+servicio.getDescripcionServicio());
						}
						
					}
					else
					{
						logger.info("Obtener info Tarifa Unitaria Por Servicio ");
						infoResponsableCobertura = Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), servicio.getCodigoServicio(), Integer.parseInt(usuario.getCodigoInstitucion()), false, subCuenta.getSubCuenta());
						int convenio=infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo();
						tarifa=CargosOdon.obtenerTarifaUnitariaXServicio(servicio.getCodigoServicio(), convenio, subCuenta.getContrato(), "", usuario.getCodigoInstitucionInt(), new BigDecimal(paciente.getCodigoCuenta()), false /*en este punto no existe programa, por esa razon la cobertura se hace a nivel del servicio*/, usuario.getCodigoCentroCosto());
					}
					
					if(tarifa.getError().equals(""))
					{
						// Como no hay error encontró cobertura y tarifa
						servicio.setInfoTarifa(tarifa);
						servicio.setResponsableServicio(infoResponsableCobertura.getDtoSubCuenta());
						servicio.setPacientePagaAtencion(Contrato.pacientePagaAtencion(infoResponsableCobertura.getDtoSubCuenta().getContrato()));
						if(servicio.getPacientePagaAtencion())
						{
							valorTotalServicios+=tarifa.getValorTarifaTotalConDctos().doubleValue();
						}
						// no debo validar nada más
						w=proceso.getResponsables().size();
						break;
					}
				}
				if(tarifa==null || !tarifa.getError().equals(""))
				{
					proceso.getErrores().add("No se pudo calcular la tarifa del servicio, por favor verifique");
				}
			}
		}
		if(proceso.getErrores().size()>0)
		{
			proceso.setValorTotalServicios(0);
		}
		else
		{
			proceso.setValorTotalServicios(valorTotalServicios);
		}
		
		// Ahora se calculan los valores para los servicios anteriores
		valorTotalServicios=0;
		
		// para los servicios anteriores
		ArrayList<DtoProgramasServiciosAnterioresCita> listaServiciosAnteriores=solicitud.getProgServAnteriores();
		for(int i=0;i<listaServiciosAnteriores.size();i++)
		{
			DtoServicioOdontologico servicio=listaServiciosAnteriores.get(i).getDtoServicio();
			
			if(UtilidadTexto.getBoolean(servicio.getGarantiaServicio()))
			{
				servicio.setInfoTarifa(null);
			}
			else
			{
				InfoTarifaServicioPresupuesto tarifa=new InfoTarifaServicioPresupuesto();
				InfoResponsableCobertura infoResponsableCobertura=new InfoResponsableCobertura();
				for(int w=0; w<proceso.getResponsables().size(); w++)
				{
					DtoSubCuentas subCuenta= proceso.getResponsables().get(w);
					logger.info("\n\n Codigo Presupuesto Prog >> "+ servicio.getCodigoPresuOdoProgSer());
					if(servicio.getCodigoPresuOdoProgSer()>0)
					{
						logger.info("OBTENER INFO presupuesto >> ");
						CargosOdon.obtenerInfoPresupuestoContratadoProgSer(con,servicio.getCodigoPresuOdoProgSer(), infoResponsableCobertura, tarifa, UtilidadTexto.getBoolean(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(solicitud.getInstitucion())), servicio.getCodigoServicio(), new BigDecimal(servicio.getCodigoPlanTratamiento()));
			
						if(!tarifa.getError().equals(""))
						{
							tarifa.setError(tarifa.getError()+" para el servicio "+servicio.getDescripcionServicio());
						}
						
					}
					else
					{
						logger.info("Obtener info Tarifa Unitaria Por Servicio ");
						infoResponsableCobertura = Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), servicio.getCodigoServicio(), Integer.parseInt(usuario.getCodigoInstitucion()), false, subCuenta.getSubCuenta());
						int convenio=infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo();
						tarifa=CargosOdon.obtenerTarifaUnitariaXServicio(servicio.getCodigoServicio(), convenio, subCuenta.getContrato(), "", usuario.getCodigoInstitucionInt(), new BigDecimal(paciente.getCodigoCuenta()), false /*en este punto no existe programa, por esa razon la cobertura se hace a nivel del servicio*/, usuario.getCodigoCentroCosto());
					}
					
					if(tarifa.getError().equals(""))
					{
						// Como no hay error encontró cobertura y tarifa
						servicio.setInfoTarifa(tarifa);
						servicio.setResponsableServicio(infoResponsableCobertura.getDtoSubCuenta());
						servicio.setPacientePagaAtencion(Contrato.pacientePagaAtencion(infoResponsableCobertura.getDtoSubCuenta().getContrato()));
						if(servicio.getPacientePagaAtencion())
						{
							valorTotalServicios+=tarifa.getValorTarifaTotalConDctos().doubleValue();
						}
						// no debo validar nada más
						w=proceso.getResponsables().size();
						break;
					}
				}
				if(tarifa==null || !tarifa.getError().equals(""))
				{
					proceso.getErrores().add("No se pudo calcular la tarifa del servicio, por favor verifique");
				}
			}
		}
		if(proceso.getErrores().size()>0)
		{
			proceso.setValorTotalServiciosAnteriores(0);
		}
		else
		{
			proceso.setValorTotalServiciosAnteriores(valorTotalServicios);
		}

	}


	
}
