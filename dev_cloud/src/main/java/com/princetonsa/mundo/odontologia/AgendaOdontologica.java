package com.princetonsa.mundo.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoResponsableCobertura;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.odontologia.InfoTarifaServicioPresupuesto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.odontologia.AgendaOdontologicaDao;
import com.princetonsa.dto.cargos.DtoDetalleCargo;
import com.princetonsa.dto.comun.DtoResultado;
import com.princetonsa.dto.facturacion.DtoSeccionConvenioPaciente;
import com.princetonsa.dto.manejoPaciente.DtoInformacionBasicaIngresoPaciente;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.CargosOdon;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.Cuentas;
import com.servinte.axioma.orm.CuentasHome;
import com.servinte.axioma.orm.Ingresos;
import com.servinte.axioma.orm.IngresosHome;
import com.servinte.axioma.orm.NaturalezaPacientes;
import com.servinte.axioma.orm.NaturalezaPacientesHome;
import com.servinte.axioma.orm.SubCuentas;
import com.servinte.axioma.orm.SubCuentasHome;
import com.servinte.axioma.persistencia.UtilidadPersistencia;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ICuentasServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IIngresosServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ISubCuentasServicio;

/**
 * 
 * @author V�ctor Hugo G�mez L.
 *
 */
@SuppressWarnings({"unused","unchecked"})
public class AgendaOdontologica {

	/**
	 * logger
	 */
	static Logger logger = Logger.getLogger(AgendaOdontologica.class);
	
	/**
	 * Instancia DAO
	 * */
	public static AgendaOdontologicaDao getAgendaOdontologicaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAgendaOdontologicaDao();		
	}
	
	/**
	 * se obtiene un listado de los horarios de atencion que cumple con los parametros selecionados
	 * @param con
	 * @param centroAtencion
	 * @param unidadConsulta
	 * @param consultorio
	 * @param diaSemana
	 * @param codigoMedico
	 * @return
	 */
	public static ArrayList<DtoAgendaOdontologica> cosultarAgendaOdontologica(Connection con, 
			String centroAtencion, 
			String unidadConsulta, 
			String codigoMedico,
			String fecha,
			String tipoCita)
	{
		return getAgendaOdontologicaDao().cosultarAgendaOdontologica(con, centroAtencion, unidadConsulta, codigoMedico, fecha, tipoCita);
	}
	
	/**
	 * Convenciones de Colores por Unidad de Agenda
	 * @param con
	 * @param unidadesAgenda
	 * @return
	 */
	public static ArrayList<InfoDatosString> convencionColorUnidaAgenda(Connection con, String unidadesAgenda, String fecha)
	{
		return getAgendaOdontologicaDao().convencionColorUnidaAgenda(con, unidadesAgenda, fecha);
	}

	/**
	 * se obtienen los consultorios sin repetirse nunguno
	 * @param dto
	 * @return
	 */
	public static ArrayList<InfoDatosInt> consultoriosAgendaOdontologica(ArrayList<DtoAgendaOdontologica> dto, String[] horaIniAgenOdon, String[] horaFinAgenOdon, String fechaDesplazamiento)
	{
		ArrayList<InfoDatosInt> array = new ArrayList<InfoDatosInt>();
		boolean bandAdd = true;
		for(int i=0;i<dto.size();i++)
		{
			if(fechaDesplazamiento.equals(dto.get(i).getFecha()))
			{
				if(array.size()>0)
				{
					for(int j=0;j<array.size();j++)
					{
						if(dto.get(i).getConsultorio()==array.get(j).getCodigo())
							bandAdd = true;
					}
					if(!bandAdd){
						InfoDatosInt datoCon = new InfoDatosInt(dto.get(i).getConsultorio(),dto.get(i).getDescripcionConsultorio());
						array.add(datoCon);
					}
				}else{
					InfoDatosInt datoCon = new InfoDatosInt(dto.get(i).getConsultorio(),dto.get(i).getDescripcionConsultorio());
					array.add(datoCon);
				}
				bandAdd = false;
			}
			
			if(horaIniAgenOdon[0].equals("") || UtilidadFecha.esHoraMenorQueOtraReferencia(dto.get(i).getHoraInicio(), horaIniAgenOdon[0]))
				horaIniAgenOdon[0] = dto.get(i).getHoraInicio();
			
			if(horaFinAgenOdon[0].equals("") || !UtilidadFecha.esHoraMenorQueOtraReferencia(dto.get(i).getHoraFin(), horaFinAgenOdon[0]))
				horaFinAgenOdon[0] = dto.get(i).getHoraFin();
		}
		return array;
	}

	/**
	 * 
	 * M&eacute;todo que retorna un arraylist de CitasOdontologicas
	 *
	 * @param con
	 * @param usuario
	 * @param codPaciente
	 * @param institucion
	 * @param fechaFiltro
	 * @param horaFiltro
	 * @param minutosEspera
	 * @param codigoIngreso
	 * @param minutosReserva 
	 * @return
	 */
	public static ArrayList<DtoCitaOdontologica> consultarCitasPaciente(Connection con, UsuarioBasico usuario, int codPaciente,int institucion, String fechaFiltro, String horaFiltro,int minutosEspera, int codigoIngreso, int minutosReserva)
	{
		
		ArrayList<DtoCitaOdontologica> citasPaciente = CitaOdontologica.consultarCitaOdontologicaXPaciente(con, usuario, codPaciente, institucion,fechaFiltro, horaFiltro, minutosEspera, codigoIngreso, minutosReserva);
		boolean mostrarProgramadas = false;
		
		if (citasPaciente != null && citasPaciente.size() > 0) {
			
			for (DtoCitaOdontologica dto : citasPaciente) {
				if (dto.getEstado().trim().equals(ConstantesIntegridadDominio.acronimoProgramado)) {
					
					ArrayList<DtoServicioCitaOdontologica> serviciosAtendidos = new ArrayList<DtoServicioCitaOdontologica>();
					int codigoCitaProgramada = dto.getCodigoPk();
					ArrayList<DtoServicioCitaOdontologica> serviciosCitaProgramada = CitaOdontologica.consultarServiciosCitaOdontologica(con, codigoCitaProgramada, institucion, codPaciente);
					List<Integer> listadoCitasAsociadas = CitaOdontologica.consultarCitaAsociadaProgramada(con, codigoCitaProgramada);
					ArrayList<DtoServicioCitaOdontologica> serviciosCita = (ArrayList<DtoServicioCitaOdontologica>) dto.getServiciosCitaOdon().clone();
					if (listadoCitasAsociadas != null && listadoCitasAsociadas.size() > 0) {
						
						// Si son iguales es porque todos los servicios han sido atendidos.
						if (listadoCitasAsociadas.size() != dto.getServiciosCitaOdon().size()) {
							
							for (Integer codigoCitaAsociada : listadoCitasAsociadas) {
								
								ArrayList<DtoServicioCitaOdontologica> serviciosCitaAsociada = CitaOdontologica.consultarServiciosCitaOdontologica(con, codigoCitaAsociada, institucion, codPaciente);
								
								serviciosAtendidos.addAll(serviciosCitaAsociada);
								ArrayList<DtoServicioCitaOdontologica> servicios = (ArrayList<DtoServicioCitaOdontologica>) serviciosCitaProgramada.clone();
								
								if (serviciosCitaProgramada != null && serviciosCitaProgramada.size() > 0 &&
										serviciosCitaAsociada != null && serviciosCitaAsociada.size() > 0) {
									
									if (serviciosCitaAsociada.size() != dto.getServiciosCitaOdon().size()) {
										
										for (int i = 0; i < serviciosCitaProgramada.size(); i++) {
												
											if (i< serviciosCitaAsociada.size()) {
												
												boolean contiene = false;
												
												for (DtoServicioCitaOdontologica servicioAsociada : serviciosCitaAsociada){
													
													if (i < serviciosCitaAsociada.size()) {
														if (servicioAsociada.getServicio() == serviciosCitaProgramada.get(i).getServicio() 
																&& servicioAsociada.getSuperficies().get(0).getProgHallazgoPieza().getCodigoPk() == serviciosCitaProgramada.get(i).getSuperficies().get(0).getProgHallazgoPieza().getCodigoPk()) {
															contiene = true;
															break;
														}
													}
												}
												
												if (contiene) {
													
													if (i == servicios.size()) {
														servicios.remove(i-1);
													}else{
														//servicios.remove(i);
														servicios.get(i).setAsignadoAOtraCita(true);
													}
												}
											}else{
												break;
											}
										}
									}
								}
								
								if (servicios != null && servicios.size() > 0) {
									mostrarProgramadas = true;
									dto.setServiciosCitaOdon(servicios);
									serviciosCitaProgramada = servicios;
								}
							}
						}
					}
					
					if (serviciosAtendidos.size() == 0 && ( listadoCitasAsociadas == null || listadoCitasAsociadas.size() == 0 )) {
						mostrarProgramadas = true;
						break;
					}else if (serviciosCita.size() == serviciosAtendidos.size()) {
						mostrarProgramadas = false;
					}
				}
			}
		}
		if (!mostrarProgramadas ) {
			
			ArrayList<DtoCitaOdontologica> citas = new ArrayList<DtoCitaOdontologica>();
			
			for (DtoCitaOdontologica cita : citasPaciente) {
				if (!cita.getEstado().trim().equals(ConstantesIntegridadDominio.acronimoProgramado)) {
					citas.add(cita);
				}
			}
			
			citasPaciente = citas;
		}
		
		return citasPaciente;
	}
	
	
	public static ArrayList<DtoCitaOdontologica> consultarCitasXRango(Connection con, HashMap parametrosBusqueda, int centroAtencionUsuario)
	{
		return CitaOdontologica.consultarCitaOdontologicaXRango(con, parametrosBusqueda, centroAtencionUsuario);
	}
	
	/**
	 * modificar el numero de cupos de la agenda odontologica
	 * @param con
	 * @param codigoPK
	 * @param horaFin
	 * @param usuarioModifica
	 * @param institucion
	 * @param cupos
	 * @return
	 */
	public static int modificarCuposAgendaOdontologica(Connection con, 
			int codigoPK,
			String horaInicio,
			String horaFin,
			String usuarioModifica, boolean disminuir, int institucion)
	{
		return getAgendaOdontologicaDao().modificarCuposAgendaOdontologica(con, codigoPK, horaInicio, horaFin, usuarioModifica, disminuir, institucion);
	}

	/**
	 * Metodo para Reprogramar una cita Existente
	 * @param parametros
	 * @param forma 
	 * @param usuario 
	 */
	public static int actualizarCitaXReprogramacion(Connection con,HashMap parametros) {
		
		int resp=0, modificarCuposAgenAnt= 0, modificarCuposAgenNueva=0;	
		ArrayList<DtoServicioCitaOdontologica> arrayDtoServicios= new ArrayList<DtoServicioCitaOdontologica>();
		
		
		//Actualizar Cupos Agenda Anterior
		modificarCuposAgenAnt=getAgendaOdontologicaDao().modificarCuposAgendaOdontologica(con, Utilidades.convertirAEntero(parametros.get("codAgendaAnterior")+""), UtilidadFecha.conversionFormatoFechaABD(parametros.get("horaInicioCita")+""), UtilidadFecha.conversionFormatoFechaABD(parametros.get("horaFinal")+""), parametros.get("usuario")+"", false,Utilidades.convertirAEntero(parametros.get("codInsititucion")+""));
		//Actualizar Cupos Agenda Nueva
		modificarCuposAgenNueva=getAgendaOdontologicaDao().modificarCuposAgendaOdontologica(con, Utilidades.convertirAEntero(parametros.get("codAgendaActual")+""), UtilidadFecha.conversionFormatoFechaABD(parametros.get("horaInicio")+""), UtilidadFecha.conversionFormatoFechaABD(parametros.get("horaFinal")+""), parametros.get("usuario")+"", true,Utilidades.convertirAEntero(parametros.get("codInsititucion")+""));
		
		if (modificarCuposAgenAnt >0 && modificarCuposAgenNueva>0)
		{
			logger.info("MODIFICO CORRECTAMENTE cupos X Reprogramacion....");
			arrayDtoServicios = (ArrayList<DtoServicioCitaOdontologica>)parametros.get("arrayDtoServicios");
			if(CitaOdontologica.actualizarCitaOdontologicaXReprogramacion(con, Utilidades.convertirAEntero(parametros.get("codigoCita")+""), Utilidades.convertirAEntero(parametros.get("codAgendaActual")+""), parametros.get("fecha")+"", parametros.get("horaInicio")+"", parametros.get("horaFinal")+"", Utilidades.convertirAEntero(parametros.get("duracionCita")+""), parametros.get("usuario")+"",parametros.get("tipoCita")+"", arrayDtoServicios))
			  {
				resp=1;				
			  }
		}else
		{	
		   resp=0;
		}
	 
		return resp;
	}

	/**
	 * Metodo para Reservar una cita existente
	 * @param con
	 * @param dto
	 * @param parametros
	 * @return
	 */
	public int modificarCitaOdontologica(Connection con,DtoCitaOdontologica dto, HashMap parametros) {
		
		
		int resp=0, modificarCuposAgenAnt= 0, modificarCuposAgenNueva=0;	
		
		if(!parametros.get("estadoCita").equals(ConstantesIntegridadDominio.acronimoProgramado))
		{
		 //Actualizar Cupos Agenda Anterior
		 modificarCuposAgenAnt=getAgendaOdontologicaDao().modificarCuposAgendaOdontologica(con, Utilidades.convertirAEntero(parametros.get("codAgendaAnterior")+""), UtilidadFecha.conversionFormatoFechaABD(parametros.get("horaInicioCita")+""),UtilidadFecha.conversionFormatoFechaABD(parametros.get("horaFinalCita")+""), parametros.get("usuario")+"", false, Utilidades.convertirAEntero(parametros.get("codInstitucion")+""));
		 logger.info("modificar Anterior >>"+modificarCuposAgenAnt);
	     }else
	     {
	    	 modificarCuposAgenAnt=1;
	     }
		//Actualizar Cupos Agenda Nueva
		modificarCuposAgenNueva=getAgendaOdontologicaDao().modificarCuposAgendaOdontologica(con, Utilidades.convertirAEntero(parametros.get("codAgendaActual")+""), UtilidadFecha.conversionFormatoFechaABD(parametros.get("horaInicio")+""), UtilidadFecha.conversionFormatoFechaABD(parametros.get("horaFinal")+""), parametros.get("usuario")+"", true, Utilidades.convertirAEntero(parametros.get("codInstitucion")+""));
		logger.info("modificar Nuevo >>"+modificarCuposAgenNueva);		
		
		if (modificarCuposAgenAnt >0 && modificarCuposAgenNueva>0)
		{
			logger.info("MODIFICO CORRECTAMENTE cupos X Reserva....");
			if(CitaOdontologica.actualizarCitaOdontologicaXReserva(con,dto))
			 {
				return resp=1;
			 }
			
		}
		
		return resp;
	}
	
	
	/**
	 * Metodo para Reservar una cita existente
	 * @param con
	 * @param dto
	 * @param parametros
	 * @return
	 */
	public static int modificarCitaOdontologica(DtoCitaOdontologica dto, HashMap parametros) 
	{
		Connection con=UtilidadBD.abrirConexion();
		
		int resp=0, modificarCuposAgenAnt= 0, modificarCuposAgenNueva=0;	
		
		if(!parametros.get("estadoCita").equals(ConstantesIntegridadDominio.acronimoProgramado))
		{
			//Actualizar Cupos Agenda Anterior
			modificarCuposAgenAnt=getAgendaOdontologicaDao().modificarCuposAgendaOdontologica(con, Utilidades.convertirAEntero(parametros.get("codAgendaAnterior")+""), UtilidadFecha.conversionFormatoHoraABD(parametros.get("horaInicioCita")+""),UtilidadFecha.conversionFormatoHoraABD(parametros.get("horaFinalCita")+""), parametros.get("usuario")+"", false, Utilidades.convertirAEntero(parametros.get("codInstitucion")+""));
			logger.info("modificar Anterior >>"+modificarCuposAgenAnt);
			
		}else{
			
			modificarCuposAgenAnt=1;
	    }
		//Actualizar Cupos Agenda Nueva
		modificarCuposAgenNueva=getAgendaOdontologicaDao().modificarCuposAgendaOdontologica(con, Utilidades.convertirAEntero(parametros.get("codAgendaActual")+""), UtilidadFecha.conversionFormatoHoraABD(parametros.get("horaInicio")+""), UtilidadFecha.conversionFormatoHoraABD(parametros.get("horaFinal")+""), parametros.get("usuario")+"", true, Utilidades.convertirAEntero(parametros.get("codInstitucion")+""));
		logger.info("modificar Nuevo >>"+modificarCuposAgenNueva);		
		
		if (modificarCuposAgenAnt >0 && modificarCuposAgenNueva>0)
		{
			logger.info("MODIFICO CORRECTAMENTE cupos X Reserva....");
			if(CitaOdontologica.actualizarCitaOdontologicaXReserva(con,dto)){
				
				UtilidadBD.closeConnection(con);
				return resp=1;
			}
		}
		
		UtilidadBD.closeConnection(con);
		return resp;
	}
		
	
	/**
	 * Metodo para Reversar el valor de lo a Abonos
	 * @param listaServicios
	 * @param persona
	 * @param usuario
	 * @return
	 */
	public int reversarValordeAbonos(ArrayList<DtoServicioOdontologico> listaServicios,  PersonaBasica persona, UsuarioBasico usuario) throws IPSException
	{
		Connection con=UtilidadBD.abrirConexion();
		ArrayList<DtoSubCuentas> listaResponsables=UtilidadesHistoriaClinica.obtenerResponsablesIngreso(
							con,
							persona.getCodigoIngreso(),
							true, // Traer todos los responsables (Facturados y no facturasdos)
							new String[0], // Exluir responsables
							false, // Solamente PYP
							"", // Sub cuenta
							persona.getCodigoUltimaViaIngreso());
		
		/*
		 * Este arraylist se env�a a la vista para mostrar las tarifas de los sevicios
		 */
		ArrayList<DtoServicioCitaOdontologica> listaTarifasServicios=new ArrayList<DtoServicioCitaOdontologica>();
		
		for(int i=0;i<listaServicios.size();i++)
		{
			if(listaServicios.size()>0)
			{
				DtoServicioOdontologico servicio=listaServicios.get(i);
				if(UtilidadTexto.getBoolean(servicio.getAsociarSerCita()))
				{
					listadoSubcuentas: for(DtoSubCuentas subCuenta:listaResponsables)
					{
						logger.info("contrato "+subCuenta.getContrato());
					   if(Contrato.pacientePagaAtencion(subCuenta.getContrato()))
						{	
						  InfoResponsableCobertura infoResponsableCobertura = Cobertura.validacionCoberturaServicio(con, persona.getCodigoIngreso()+"", persona.getCodigoUltimaViaIngreso(), persona.getCodigoTipoPaciente(), servicio.getCodigoServicio(), Integer.parseInt(usuario.getCodigoInstitucion()), false, "" /*subCuentaCoberturaOPCIONAL*/);
						  int convenio=infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo();
						  ArrayList<InfoTarifaServicioPresupuesto> listaTarifas=CargosOdon.obtenerTarifaUnitariaXPresupuestoOservicio(0.0, servicio.getCodigoServicio(), convenio, subCuenta.getContrato(), "", usuario.getCodigoInstitucionInt(), new BigDecimal(persona.getCodigoCuenta()));
						
						  InfoTarifaServicioPresupuesto tarifa=listaTarifas.get(0);
						  if(tarifa.getError().equals(""))
						     {
							  // Como no hay error encontr� cobertura y tarifa
							  logger.info("tarifa "+tarifa.getValorTarifaUnitaria());
							   servicio.setInfoTarifa(tarifa);
							  // no debo validar nada m�s
							  break listadoSubcuentas;
						     }
						 }
					}
				}
			}
		}
		
		return 0;
	}
	
	
	
	/**
	 * Metodo para Cancelar una cita Odontologica
	 * @param con
	 * @param citaProxima
	 * @param usuario
	 * @param ingreso
	 * @return
	 */
	public static boolean cancelarProximaCitaPaciente(Connection con, DtoCitaOdontologica citaProxima, UsuarioBasico usuario, int codigoPaciente, String tipoCancelacion, String codMotivoCancelacion, Integer ingreso) throws IPSException
	{
		boolean exito= true;
		
		if(citaProxima.getEstado().equals(ConstantesIntegridadDominio.acronimoAsignado))
		{
			logger.info("llega cuando la cita esta asignada");
			
			for(DtoServicioCitaOdontologica elem: citaProxima.getServiciosCitaOdon())
			{
				logger.info("numero solicitud "+elem.getNumeroSolicitud());
				//logger.info.("orden servicio >>"+ )
				if(elem.getFacturado().equals(ConstantesBD.acronimoNo))
				{
					if(!Solicitud.cambiarEstadosSolicitudStatico(con,
							elem.getNumeroSolicitud(), 
							ConstantesBD.codigoEstadoFAnulada, 
							ConstantesBD.codigoEstadoHCAnulada).isTrue())
					{
						exito = false;						
						break;
					}
					elem.setEliminarSer(ConstantesBD.acronimoSi);
				}
			}
			if(exito)
			{ 			
				double tarifas=0;
				for(DtoServicioCitaOdontologica elem: citaProxima.getServiciosCitaOdon())
				{
					DtoDetalleCargo detalleCargoConsulta = new DtoDetalleCargo();
					detalleCargoConsulta.setNumeroSolicitud(elem.getNumeroSolicitud());
					ArrayList<DtoDetalleCargo> listaDetalle=Cargos.cargarDetalleCargos(con, detalleCargoConsulta);
					double valorReversaAbono=0;
					for(DtoDetalleCargo detalleCargo:listaDetalle)
					{
						valorReversaAbono+=detalleCargo.getValorTotalCargado();
					}
					if(elem.getFacturado().equals(ConstantesBD.acronimoNo) && !UtilidadTexto.getBoolean(elem.getGarantiaServicio()))
					{
						tarifas+=elem.getInfoTarifa().getValorTarifaUnitaria().doubleValue();
					}
				}		  
			} 	
			
		}
		else // si no esta asignada debe estar reservada
		{
			for(DtoServicioCitaOdontologica elem: citaProxima.getServiciosCitaOdon())
			{
				elem.setEliminarSer(ConstantesBD.acronimoSi);
				logger.info("reservada");
			}
		}
		

		boolean eliminarCupo=false;
		// verifica el motivo de la cita 
		if(tipoCancelacion.equals(ConstantesBD.acronimoNo))//institucion
		{
			citaProxima.setEstado(ConstantesIntegridadDominio.acronimoAreprogramar);
		}
		else
		{      
		   		
			if(tipoCancelacion.equals(ConstantesBD.acronimoSi))//paciente
			{
				citaProxima.setEstado(ConstantesIntegridadDominio.acronimoEstadoCancelado);
				eliminarCupo=true;
			}
			logger.info("*****************el estado llega   "+citaProxima.getEstado());
			citaProxima.setMotivoCancelacion(Utilidades.convertirAEntero(codMotivoCancelacion));
		}
		citaProxima.setUsuarioModifica(usuario.getLoginUsuario());
		if(CitaOdontologica.cambiarSerOdontologicos(con, citaProxima))
		{	
			// liberar cupo de la unidad de agenda odontologica cuando es motivo de cancelacion es por paciente
			if(eliminarCupo)
			{			
				// aumenta el cupo de la cita 
				if(AgendaOdontologica.modificarCuposAgendaOdontologica(con,citaProxima.getAgenda(), citaProxima.getHoraInicio(), 
				  citaProxima.getHoraFinal(),usuario.getLoginUsuario(), false, usuario.getCodigoInstitucionInt())<0)
				{
					exito= false;
				}
				
			}		
			
		}
		
		return exito;
	}
	
	
	
	/**
	 * Valida existe un convenio que sea TipoTarjetaCliente = NO
	 * @param arrayConveniosParametrizadosPorDefecto
	 * @return
	 */
	public boolean existeConvenioActivoTipoTarjetaClienteNo(ArrayList<HashMap<String, Object>> arrayConveniosParametrizadosPorDefecto)
	{
		boolean existeTipoTarjetaClienteNo = false;
			
		for (HashMap<String, Object> hashMapConvenio : arrayConveniosParametrizadosPorDefecto){
			if(hashMapConvenio.get("activo").equals(true)){
				if(hashMapConvenio.get("esConvenioTarjCliente").toString().equals(ConstantesBD.acronimoNo)){
					existeTipoTarjetaClienteNo = true;
				}
			}
		}
		
		return existeTipoTarjetaClienteNo;
	}
	
	
	
	
	/**
	 * Realiza las validaciones espec�ficas del anexo 1131: Validaciones Selecci�n Convenio
	 * Secci�n 1 
	 * @author Cristhian Murillo
	 * 
	 * @param paciente
	 * @param tipoCita
	 * @param usuario
	 * @return boolean
	 */
	public boolean validacionesMostrarConvenioAsignacioncita(PersonaBasica paciente, String tipoCita, UsuarioBasico usuario)
	{
		boolean mostrarConvenio = false;
		
		// Se valida el ingreso del paciente
		if(paciente.getCodigoIngreso()<=0)
		{
			mostrarConvenio  = true;
		}
		else 
		{
			/* * Lista de tipo de citas espec�ficas para esta validaci�n seg�n anexo 1131 */
			ArrayList<String> listaTiposCitaComparar = new ArrayList<String>();
				listaTiposCitaComparar.add(ConstantesIntegridadDominio.acronimoAuditoria);
				listaTiposCitaComparar.add(ConstantesIntegridadDominio.acronimoControlCitaOdon);
				listaTiposCitaComparar.add(ConstantesIntegridadDominio.acronimoPrioritaria);
				listaTiposCitaComparar.add(ConstantesIntegridadDominio.acronimoRemisionInterconsulta);
				listaTiposCitaComparar.add(ConstantesIntegridadDominio.acronimoTipoCitaOdonValoracionInicial);
				listaTiposCitaComparar.add(ConstantesIntegridadDominio.acronimoRevaloracion);
			
			if(listaTiposCitaComparar.contains(tipoCita))
			{
				// Si el tipo de cita del parametro corresponde con la lista de citas definidas:
				mostrarConvenio  = true;
			}
			else if(tipoCita.equals(ConstantesIntegridadDominio.acronimoTratamiento))
			{
				// Si el tipo de cita del parametro corresponde con el tipo de cita Tratamiento:
				if(ValoresPorDefecto.getValidaPresupuestoOdoContratadoBoolean(usuario.getCodigoInstitucionInt()))
				{
					mostrarConvenio  = false;
				}
				else
				{
					mostrarConvenio  = true;
				}
			}
		}
		
		return mostrarConvenio;
	}
	
	
	
	
	/**
	 * Anexo 1131
	 * Carga los convenios parametrizados por defecto
	 * 
	 * @author Cristhian Murillo
	 * @return ArrayList<DtoSeccionConvenioPaciente>
	 */
	public ArrayList<DtoSeccionConvenioPaciente> obtenerConveniosParametrizadosPorDefecto()
	{
		ArrayList<HashMap<String, Object>> arrayConveniosParametrizadosPorDefecto = ValoresPorDefecto.getConveniosAMostrarPresupuestoOdo();	
		
		ArrayList<DtoSeccionConvenioPaciente> listaConveniosParametrizadosPorDefecto = new ArrayList<DtoSeccionConvenioPaciente>();
		
		for (HashMap<String, Object> hashMapConvenio : arrayConveniosParametrizadosPorDefecto) 
		{
			
			if(hashMapConvenio.get("esConvenioTarjCliente").toString().equals(ConstantesBD.acronimoNo))
			{
				
				DtoSeccionConvenioPaciente dtoSeccionConvenioPaciente;
				dtoSeccionConvenioPaciente = new DtoSeccionConvenioPaciente();
				
				dtoSeccionConvenioPaciente.setPorDefecto(ConstantesBD.acronimoSiChar);
				dtoSeccionConvenioPaciente.setCodigoContrato(hashMapConvenio.get("codigoContrato").toString());
				dtoSeccionConvenioPaciente.setNumeroContrato(hashMapConvenio.get("numeroContrato").toString());
				dtoSeccionConvenioPaciente.setCodigoConvenio(hashMapConvenio.get("codigoConvenio").toString());
				dtoSeccionConvenioPaciente.setDescripcionConvenio(hashMapConvenio.get("descripcionConvenio").toString());
				
				listaConveniosParametrizadosPorDefecto.add(dtoSeccionConvenioPaciente);
				
			}
		}
		
		return listaConveniosParametrizadosPorDefecto;
	}
	
	
	
	/**
	 * Anexo 1131
	 * Obtiene el esquema tarifario
	 * @param contrato
	 * @return
	 */
	public static int obtenerEsquemaTarifarioTarjetaCliente(double contrato)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTarjetaClienteDao().obtenerEsquemaTarifarioTarjetaCliente(contrato); 
	}
	
	
	
	
	/**
	 * Construcci�n para el paciente de:
	 * 	-Ingreso
	 * 	-Cuenta
	 * 	-SubCuenta
	 * 
	 * @param persona
	 * @return DtoResultado
	 * @author Cristhian Murillo
	 */
	public DtoResultado crearIngresoPaciente(PersonaBasica persona, DtoInformacionBasicaIngresoPaciente dtoInfoBasica)
	{
		DtoResultado resultado = new DtoResultado();
		resultado.setExitoso(true);
		String valorConsecutivo = "";
		
        try {
        	
			// CREAR INGRESO.
			IIngresosServicio ingresosServicio = ManejoPacienteServicioFabrica.crearIngresosServicio();
			Ingresos ingreso = ingresosServicio.crearIngresos(dtoInfoBasica);
			
			// Se marca como utilizado el consecutivo
			valorConsecutivo = ingreso.getConsecutivo();
			Connection conH=UtilidadPersistencia.getPersistencia().obtenerConexion();
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH, ConstantesBD.nombreConsecutivoIngresos, dtoInfoBasica.getInstitucion().getCodigo(), valorConsecutivo+"", ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);

			new IngresosHome().persist(ingreso);
			dtoInfoBasica.setIngreso(ingreso);

			
			// CREAR CUENTA.
			ICuentasServicio cuentasServicio = ManejoPacienteServicioFabrica.crearCuentasServicio();
			Cuentas cuenta = cuentasServicio.crearCuentas(dtoInfoBasica);
			new CuentasHome().persist(cuenta);
			
			
			// CREAR SUB-CUENTA.
			ISubCuentasServicio subCuentasServicio = ManejoPacienteServicioFabrica.crearSubCuentasServicio();
			SubCuentas subCuentas = subCuentasServicio.crearSubCuentas(dtoInfoBasica);
			new SubCuentasHome().persist(subCuentas);

			// Se guarda la llave primaria para ser asignada al guardar los servicios
			resultado.setPk2(subCuentas.getSubCuenta()+"");
			
			
        } catch (Exception e) 
        { 
        	resultado.setExitoso(false); 
        	// Se libera el consecutivo
        	if(valorConsecutivo != ""){
    			Connection con=UtilidadBD.abrirConexion();
        		UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoIngresos, dtoInfoBasica.getInstitucion().getCodigo(), valorConsecutivo+"", ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
        		UtilidadBD.closeConnection(con);
        	}
        }
		
		return resultado;
	}
	
	
	

	/**
	 * Actualiza el ingreso del paciente. Espec�ficamente:
	 * 	-SubCuenta (el el paciente no tiene el convenio asociado se lo asigna)
	 * 
	 * @param persona
	 * @param listaServicios
	 * @param dtoInfoBasica
	 * @param usuario
	 * 
	 * @return DtoResultado
	 * 
	 * @author Cristhian Murillo
	 */
	public DtoResultado actualizarIngresoPaciente(PersonaBasica persona, DtoInformacionBasicaIngresoPaciente dtoInfoBasica, 
			ArrayList<DtoServicioOdontologico> listaServicios, UsuarioBasico usuario)
	{
		DtoResultado resultado = new DtoResultado();
		resultado.setExitoso(true);
		
	    // Si ya tiene ingreso actualizar las subcuentas
        ISubCuentasServicio subCuentasServicio = ManejoPacienteServicioFabrica.crearSubCuentasServicio();
        
		ArrayList<SubCuentas> listaSubcuentasPaciente = subCuentasServicio.listarCuentasPorPaciente(persona.getCodigoPersona());
		ArrayList<SubCuentas> listaSubCuentasActualizar = new ArrayList<SubCuentas>();

		for(DtoServicioOdontologico servicio : listaServicios)
		{
			boolean convenioExisteEnSubCuenta = false;
			for (SubCuentas subCuentas : listaSubcuentasPaciente){
				// Se toman solo los servicios seleccionados
				if(servicio.getAsociarSerCita().equals(ConstantesBD.acronimoSi)){
					if(servicio.getResponsableServicio().getConvenio().getCodigo() == subCuentas.getConvenios().getCodigo()){
						convenioExisteEnSubCuenta = true;
			}}}
			
			// Si no existe el convenio
			if(!convenioExisteEnSubCuenta)
			{
				SubCuentas subCuenta;
				subCuenta = new SubCuentas();
				//FIXME implementar la arquitectura de los servicios
				subCuenta.setPacientes(dtoInfoBasica.getPaciente());
				subCuenta.setUsuarios(dtoInfoBasica.getUsuario());
				subCuenta.setFechaModifica(dtoInfoBasica.getFechaActual());
				subCuenta.setFacturado(dtoInfoBasica.getAcronimoNo().charAt(0));
				subCuenta.setHoraModifica(dtoInfoBasica.getHoraActual());
				NaturalezaPacientes naturalezaPacientes = new NaturalezaPacientes();
				NaturalezaPacientesHome npH = new NaturalezaPacientesHome();
				naturalezaPacientes = npH.findById(dtoInfoBasica.getNaturalezaPacienteNinguno());
				subCuenta.setNaturalezaPacientes(naturalezaPacientes);
				
				/*
					Con este metodo se puede enviar el servicio para crear el DtoInformacionBasicaIngresoPaciente apropiado apra la subcuenta.
					Tener en cuenta que solo serviria para asignar los valores de aca para abajo. Los dem�s ya vienen en el dto recibido.
					IIngresosServicio ingresosServicio = ManejoPacienteServicioFabrica.crearIngresosServicio();
					DtoInformacionBasicaIngresoPaciente dtoInfoBasicaServicio = ingresosServicio.construirDtoInformacionBasicaIngresoPaciente(servicio, usuario);
				*/
				/*
				subCuenta.setConvenios(new ConveniosHome().findById(servicio.getResponsableServicio().getConvenio().getCodigo()));
				subCuenta.setContratos(new ContratosHome().findById(servicio.getResponsableServicio().getContrato()));
				subCuenta.setIngresos(new IngresosHome().findById(persona.getCodigoIngreso()));
				SubCuentas subCuentas = subCuentasServicio.crearSubCuentas(dtoInfoBasica);
				
				listaSubCuentasActualizar.add(subCuentas);
				*/
			}
		}	
			
		
		try 
		{      
			int prioridad = 0;
			for (SubCuentas subCuentas : listaSubCuentasActualizar) 
			{
				prioridad +=1;
				subCuentas.setNroPrioridad(prioridad); 
				new SubCuentasHome().persist(subCuentas);
			}
		} catch (Exception e) { resultado.setExitoso(false); }
		
		return resultado;
	}
	
	/**
	 * Este m�todo se encarga de consultar el hist�rico de estados
	 * de una cita odontol�gica. 
	 *
	 * @param con
	 * @param codigoPk
	 * @return
	 *
	 * @autor Yennifer Guerrero
	 */
	public static ArrayList<DtoCitaOdontologica> obtenerHistoricoEstadosCita(Connection con, int codigoPk){
		return CitaOdontologica.obtenerHistoricoEstadosCita(con, codigoPk);
	}
 }
