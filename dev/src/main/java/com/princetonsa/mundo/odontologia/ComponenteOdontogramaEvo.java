package com.princetonsa.mundo.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.odontologia.InfoDetallePlanTramiento;
import util.odontologia.InfoHallazgoSuperficie;
import util.odontologia.InfoOdontograma;
import util.odontologia.InfoPlanTratamiento;
import util.odontologia.InfoProgramaServicioPlan;
import util.odontologia.InfoServicios;
import util.odontologia.UtilidadOdontologia;

import com.princetonsa.dto.odontologia.DTOHistoricoOdontograma;
import com.princetonsa.dto.odontologia.DtoDetallePlanTratamiento;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoLogPlanTratamiento;
import com.princetonsa.dto.odontologia.DtoLogProgServPlant;
import com.princetonsa.dto.odontologia.DtoOdontograma;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdoConvenio;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologico;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosPlanT;
import com.princetonsa.dto.odontologia.DtoSectorSuperficieCuadrante;
import com.princetonsa.enu.general.CarpetasArchivos;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortPiezasDentales;
import com.princetonsa.sort.odontologia.SortSuperficiesDentales;
import com.servinte.axioma.hibernate.HibernateUtil;

@SuppressWarnings("all")
public class ComponenteOdontogramaEvo 
{
	public static final String codigoEstadoGuardarOdonto = "guardarOdont";	
	private static Logger logger = Logger.getLogger(ComponenteOdontogramaEvo.class);

	private ActionErrors errores;
	private String forwardOdont;
	private String estadoInterno;
	private Connection conInterna;
	private int tipoHistoricoPlanTraramiento=1;
	private int tipoHistoricoProgramasPlanTrat=2;
	private int tipoHistoricoServiciosPlanTrat=3;
//	private String colorDeEnProceso = "99CCFF";
	public static final int codigoTipoHallazgoDiente = 2;
	private ArrayList<InfoDetallePlanTramiento> arrayDientesXML;
	
	/**
	 * 
	 * */
	public ComponenteOdontogramaEvo()
	{
		reset();
	}
	
	public void reset()
	{
		forwardOdont = "";
		errores = new ActionErrors();
		estadoInterno = "";
	}
	
	/**
	 * 
	 * */
	public InfoOdontograma centralAcciones(InfoOdontograma info,String estadoExterno,PersonaBasica paciente)
	{
		estadoInterno = estadoExterno;
		
		logger.info("\n\nisLlamadoSeccionCita >> "+info.isLlamadoSeccionCita());
		
		logger.info("EL ESTADO INTERNO DENTRO DE COMPONENTE ODONTOGRAMA----->"+estadoExterno);

		//Estados Generales
		if(estadoInterno.equals("empezar"))
		{
			info = accionCargarOdontograma(info);
			this.forwardOdont = "";
		}
		else if(estadoInterno.equals("guardarOdont"))
		{
			accionGuardarPrincipal(info, paciente);
		}
		else if(estadoInterno.equals("actualizarPlantCita"))
		{
			this.forwardOdont = "retazoPlanTCita";
		}
		//Estados Seccion Detalle Plan de Tratamiento
		else if(estadoInterno.equals("actualizarInfoOdonto"))
		{
			this.forwardOdont = "";
			this.estadoInterno = "abrirProgCita";
		}
		//Estado Seccion Otras Evoluciones
		else if(estadoInterno.equals("guardarOtrosHallazgos"))
		{
			UtilidadBD.iniciarTransaccion(getConInterna());
			if(!actualizarSeccEvolPiezasDentales(getConInterna(), info))
				UtilidadBD.abortarTransaccion(getConInterna());
			UtilidadBD.finalizarTransaccion(getConInterna());
			
		}else if(estadoInterno.equals("inacExclusionProg"))
		{
			inactivarServiciosProg(info, ConstantesBD.acronimoSi);
			
			if(info.isLlamadoSeccionCita())
			{
				info.setSeccionAplica(ConstantesIntegridadDominio.acronimoDetalle+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoOtro);
				this.forwardOdont = "retazoPlanTCita";
			}
			else
				this.forwardOdont = "retazoOtroHall";
			
		}else if(estadoInterno.equals("actiExclusionProg"))
		{
			inactivarServiciosProg(info, ConstantesBD.acronimoNo);
			
			if(info.isLlamadoSeccionCita())
			{
				info.setSeccionAplica(ConstantesIntegridadDominio.acronimoDetalle+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoOtro);
				this.forwardOdont = "retazoPlanTCita";
			}
			else
				this.forwardOdont = "retazoOtroHall";
		}else if(estadoInterno.equals("accionInclusion"))
		{
			inclusionProgServ(info);
			
			if(info.isLlamadoSeccionCita())
			{
				info.setSeccionAplica(ConstantesIntegridadDominio.acronimoDetalle+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoOtro);
				this.forwardOdont = "retazoPlanTCita";
			}
			else
				this.forwardOdont = "retazoOtroHall"; //retazoInclusion
		}else if(estadoInterno.equals("garantiaProg"))
		{
			garantiaServiciosProg(info,ConstantesBD.acronimoSi);
			
			if(info.isLlamadoSeccionCita())
			{
				info.setSeccionAplica(ConstantesIntegridadDominio.acronimoDetalle+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoOtro);
				this.forwardOdont = "retazoPlanTCita";
			}
			else
				this.forwardOdont = "retazoOtroHall";
		}else if(estadoInterno.equals("actGarantiaProg"))
		{
			garantiaServiciosProg(info,ConstantesBD.acronimoNo);
			
			if(info.isLlamadoSeccionCita())
			{
				info.setSeccionAplica(ConstantesIntegridadDominio.acronimoDetalle+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoOtro);
				this.forwardOdont = "retazoPlanTCita";
			}
			else
				this.forwardOdont = "retazoOtroHall";
		}else if(estadoInterno.equals("accionEvoOrdServ")||estadoInterno.equals("accionEvoOrdServDetalle"))
		{
			// FIXME validacionEvolucionOrdenServicios(info); CAMBIO TEMPORAL MIENTRAS SE DEFINE COMO VA A SER ESTA VALIDACIÓN
			validacionEvolucionOrdenServicios(info);
			
			if(info.isLlamadoSeccionCita())
			{
				info.setSeccionAplica(ConstantesIntegridadDominio.acronimoDetalle+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoOtro);
				this.forwardOdont = "retazoPlanTCita";
			}
			else
				this.forwardOdont = "retazoOtroHall";
			
		}else if(estadoInterno.equals("motivoCanExcGar"))
		{
			this.forwardOdont = "motivoCanExcGar";
		}else if(estadoInterno.equals("accionMotivoCanExcGar"))
		{
			accionMotivoCanExcGar(info);
			this.forwardOdont = "";
		}
		else if(estadoInterno.equals("accionAbrirPopupPlanT"))
		{
			this.forwardOdont = "retazoOtroHall";
		}
		else if(estadoInterno.equals("accionActualizarPopupPlanT"))
		{
			this.forwardOdont = "retazoOtroHall";
		}
		//******* Estado Seccion Inclusiones ***********
		
		else if(estadoInterno.equals("accionAbrirPopupInclus"))
		{
			this.forwardOdont = "retazoInclusion";
		}
		else if(estadoInterno.equals("empezarNuevaInclusion"))
		{
			info = accionEmpezarNuevaInclusion(info);
			this.forwardOdont = "nuevaInclusion";
		}
		else if(estadoInterno.equals("addHallazgoOtro"))
		{
			info = accionAddHallazgoOtro(info);
			this.forwardOdont = "nuevaInclusion";
		}else if(estadoInterno.equals("addHallazgoBoca"))
		{
			info = accionAddHallazgoBoca(info);
			this.forwardOdont = "nuevaInclusion";
		}
		else if(estadoInterno.equals("centralBusquedaOdoOtro"))
		{
			accionAbrirBusquedaOdo(info,ConstantesIntegridadDominio.acronimoOtro);
			this.forwardOdont = "centralBusquedaOdo";
		}		
		else if(estadoInterno.equals("centralBusquedaOdoBoca"))
		{
			accionAbrirBusquedaOdo(info,ConstantesIntegridadDominio.acronimoBoca);
			this.forwardOdont = "centralBusquedaOdo";
		}
		else if (estadoInterno.equals("addPrgServOtro"))
		{				
			if(estadoInterno.equals("addPrgServOtro") && !accionAddServPlanTr(info,ConstantesIntegridadDominio.acronimoOtro))
				this.estadoInterno = "centralBusquedaOdoOtro";
		    	
		    this.forwardOdont = "nuevaInclusion";
		}
		else if(estadoInterno.equals("addPrgServBoca"))
		{
			if(estadoInterno.equals("addPrgServBoca") && !accionAddServPlanTr(info,ConstantesIntegridadDominio.acronimoBoca))
				this.estadoInterno = "centralBusquedaOdoBoca";
		    	
		    this.forwardOdont = "nuevaInclusion";
		}
		else if(estadoInterno.equals("elimiProgServPlaTra"))
		{
			info = accionEliminarProgServPlanTratamiento(info);
			this.forwardOdont = "nuevaInclusion";
		}
		else if(estadoInterno.equals("actualizarXAddProgInclusion"))
		{
			this.forwardOdont = "nuevaInclusion";
			this.estadoInterno = "addPrgServOtro";
		}
		else if(estadoInterno.equals("guardarNuevaInclusionOtros"))
		{
			info = accionGuardarNuevaInclusion(info,ConstantesIntegridadDominio.acronimoOtro);
			this.forwardOdont ="nuevaInclusion";
		}
		else if(estadoInterno.equals("guardarNuevaInclusionBoca"))
		{
			info = accionGuardarNuevaInclusion(info,ConstantesIntegridadDominio.acronimoBoca);
			this.forwardOdont ="nuevaInclusion";
		}
		else if(estadoInterno.equals("actualizarXCerrarNuevaInclusion"))
		{
			this.forwardOdont ="";
		}		
		else if(estadoInterno.equals("actualizarNuevaInclusion"))
		{			
			 this.forwardOdont = "nuevaInclusion";			
		}
		else if(estadoInterno.equals("actualizarRetazoInclusion"))
		{
			this.forwardOdont="retazoInclusion";
		}
		else if (estadoInterno.equals("elimiProgInclusion"))
		{
			info = accionEliminarProgServInclusion(info);
			this.forwardOdont="retazoInclusion";
		}
		
		//Estado Seccion Exclusiones
		
		//Estado Seccion Garantias
		
		//Estado Seccion Historico Plan de Tratamiento
		
		else if(estadoInterno.equals("historicoPlanTratamiento"))
		{
			info = accionConsultarHistorico(info);
			this.forwardOdont = "historicoPlanTrataOdo";
		}		
		//Estado Seccion Cita Programada
//		else if(estadoInterno.equals("empezarProximaCita"))
//		{
//			info = accionEmpezarProximaCita(info);
//			
//			String mensaje = "";
//			
//			if(!(info.getArrayServProxCita()!=null && info.getArrayServProxCita().size()>0)){
//				
//				MessageResources mensages=MessageResources.getMessageResources("mensajes.ApplicationResources");
//					
//				mensaje = mensages.getMessage("error.evolucion.proximaCita.noProgramasServicios");
//			}
//			
//			info.setMensajeInformativo(mensaje);
//			
//			
//			this.forwardOdont = "programacionCitaOdonto";
//		}
		/*
		 * Este estado se eliminó, ya que en documentación no se habla de un
		 * botón eliminar que permita hacer esto, la columna en el JSP
		 * common/seccionesParametrizables/seccionOdontogramaEvo/seccionGuardarCita.jsp
		 * también se eliminó
		 *
		else if(estadoInterno.equals("eliminarServProxCitaSelec"))
		{
			info = accionEliminarServicioProximaCita(info);
			this.forwardOdont = "programacionCitaOdonto";
		}*/
		
		//Seccion Plan Tratamiento
		
		return info;
	}
	

	

	//************************************************************************************
	//Metodos Estados Generales
	//************************************************************************************
	
	/**
	 * metodo que es llamado desde la evolucion de odontologia quien maneja las conexiones y 
	 * finaliza o aborta las transacciones, el odontograma es una adicion a la evolucion por esto
	 * no debe realizar la gestion de conexiones, los errores encontrados en el componente se adicionan
	 * al atributo errores y es pasado a la evolucion y este aborta todo el proceso de guardado
	 * */
	public void accionGuardarPrincipal(InfoOdontograma info, PersonaBasica paciente)
	{
		//Posibles estados para los planes de tratamiento que se puedan actualizar
		ArrayList<String> estados = new ArrayList<String>();
        estados.add(ConstantesIntegridadDominio.acronimoEstadoActivo);
        estados.add(ConstantesIntegridadDominio.acronimoEnProceso);
        estados.add(ConstantesIntegridadDominio.acronimoTerminado);
        estados.add(ConstantesIntegridadDominio.acronimoCancelado);
        estados.add(ConstantesIntegridadDominio.acronimoInactivo);
		
		logger.info("\n\n");
		logger.info("***************************************");
		logger.info("Actualizar Informacion Odontograma Evolucion ");
		/*
		 * Debe actualizar el estado del det_plan 
		 */
		//****************************************************
		logger.info("Info Por Confirmar >> "+info.getPorConfirmar());
		
//	
//		boolean resultadoGuardarProximaCita=true;
//		
		
//		/*
//		 * 1. GUARDAR CITA 
//		 */
//		if(info.getArrayServProxCita().size()>0 && info.getPorConfirmar().equals(ConstantesBD.acronimoNo))
//		{
//			resultadoGuardarProximaCita=accionGuardarProximaCita(info);
//		}
//		
		/*
		 * 2. GUARDAR PLAN TRATAMIENTO
		 *
		 */
		
		//if(info.getPorConfirmar().equals(ConstantesBD.acronimoNo))
		//{
		// Se verifica si se debe actualizar el estado del plan de tratamiento
		
		BigDecimal codigoPlanTratamiento = PlanTratamiento.obtenerUltimoCodigoPlanTratamiento(paciente.getCodigoIngreso(), estados, "");
		/*
		 * Si el código del plan de tratamiento es -1 entonces es un plan de tratamiento
		 * terminado, por lo tanto no hay que modificarle nada
		 */
		
		if(codigoPlanTratamiento.intValue()>0 && info.getPorConfirmar().equals(ConstantesBD.acronimoNo) && codigoPlanTratamiento!=null)
		{
			if(info.puedoTerminarPlanTratamiento())
			{
				DtoPlanTratamientoOdo dtoPlan = new DtoPlanTratamientoOdo();
				dtoPlan.setCodigoPk(codigoPlanTratamiento);
				dtoPlan.setEstado(ConstantesIntegridadDominio.acronimoTerminado);
				dtoPlan.setUsuarioModifica(info.getUsuarioActual());
				dtoPlan.setCodigoCita(new BigDecimal(info.getCodigoCita()));
				dtoPlan.setCodigoValoracion(new BigDecimal(info.getCodigoValoracion()));
				dtoPlan.setCodigoEvolucion(new BigDecimal(info.getCodigoEvolucion()));
				dtoPlan.setPorConfirmar(ConstantesBD.acronimoSi);
				dtoPlan.setEspecialidad(new InfoDatosInt(info.getEspecialidad()));
				if(!PlanTratamiento.modificar(dtoPlan, dtoPlan, getConInterna()))
				{
					adicionarError("errors.notEspecific","No se Logro Actualizar Plan de tratamiento a terminado "+info.getInfoPlanTrata().getCodigoPk());
				}
				
			}else{
					
				DtoPlanTratamientoOdo dtoPlan = new DtoPlanTratamientoOdo();
				dtoPlan.setCodigoPk(codigoPlanTratamiento);
				dtoPlan.setEstado(ConstantesIntegridadDominio.acronimoEnProceso);
				dtoPlan.setUsuarioModifica(info.getUsuarioActual());
				dtoPlan.setCodigoCita(new BigDecimal(info.getCodigoCita()));
				dtoPlan.setCodigoValoracion(new BigDecimal(info.getCodigoValoracion()));
				dtoPlan.setCodigoEvolucion(new BigDecimal(info.getCodigoEvolucion()));
				dtoPlan.setPorConfirmar(ConstantesBD.acronimoNo);
				dtoPlan.setEspecialidad(new InfoDatosInt(info.getEspecialidad()));
				
				if(!PlanTratamiento.modificar(dtoPlan, dtoPlan, getConInterna()))
				{
					adicionarError("errors.notEspecific","No se Logro Actualizar Plan de tratamiento a terminado "+info.getInfoPlanTrata().getCodigoPk()); 
				}
			}
		}
		
		if(info.getPorConfirmar().equals(ConstantesBD.acronimoNo) && errores.isEmpty())
						{
			/*
			 * Si no hay errores procedo a guardar la imagen generada del odontograma
							 */
			DtoOdontograma dto = new DtoOdontograma();
			dto.setCodigoPaciente(info.getCodigoPaciente());
			dto.getIngreso().setCodigo(info.getIdIngresoPaciente());
			dto.setValoracion(info.getCodigoValoracion());
			dto.setIndicativo(info.getIndicadorOdontograma());
			dto.setEvolucion(Utilidades.convertirADouble(info.getCodigoEvolucion()+""));
			dto.setInstitucion(info.getInstitucion());
							dto.getCentroAtencion().setCodigo(info.getCodigoCentroAtencion());
			dto.getUsuarioModifica().setUsuarioModifica(info.getUsuarioActual().getUsuarioModifica());
			dto.getUsuarioModifica().setFechaModifica(UtilidadFecha.getFechaActual());
			dto.getUsuarioModifica().setHoraModifica(UtilidadFecha.getHoraActual());
			
			//En el sqlBase se inserta el codigoPk del Odontograma
			
			dto.setImagen(info.getInfoPlanTrata().getImagen());
			
			dto.setHistorico(new DTOHistoricoOdontograma());
			dto.getHistorico().setCodigoPlanTratamiento(info.getInfoPlanTrata().getCodigoPk());
			dto.getHistorico().setEstadoPlanTratamiento(info.getDtoInfoPlanTratamiento().getEstado());
			DtoPresupuestoOdontologico dtoPresupuesto=new DtoPresupuestoOdontologico();
			// siempre debe ser contratado
			// FIXME, arreglar flujo para cuando no valide presupuesto contratado
			dtoPresupuesto.setEstado(ConstantesIntegridadDominio.acronimoContratadoContratado);
			dtoPresupuesto.setCodigoPaciente(new InfoDatosInt(paciente.getCodigoPersona()));
			
			ArrayList<DtoPresupuestoOdontologico> presupuesto = new ArrayList<DtoPresupuestoOdontologico>();
			presupuesto = PresupuestoOdontologico.cargarPresupuesto(dtoPresupuesto);
			
			if (presupuesto != null && presupuesto.size() > 0) {
				dtoPresupuesto=PresupuestoOdontologico.cargarPresupuesto(dtoPresupuesto).get(0);
				dto.getHistorico().setCodigoPresupuesto(dtoPresupuesto.getCodigoPK());
				dto.getHistorico().setEstadoPresupuesto(dtoPresupuesto.getEstado());
			}
			
			//Log4JManager.info("Voy a insertar el odontograma de evolución");
			PlanTratamiento.guardarOdontograma(getConInterna(), dto);
			//Log4JManager.info("Finalicé inserción del odontograma de evolución");
		}
		//}
		
//		else{
//			
//			adicionarError("errors.notEspecific","No se Logro Actualizar Odontograma de Evolución. Cita Programada");
//		}
			
		/*
		 * MODIFICAR EL DET PLAN DE TRATAMIENTO
		 */
		
		/*
		 * MODIFICAR LOS PROGRAMAS SERVICIOS DEL PLAN DE TRATAMIENTO
		 */
		if(actualizarSeccPlanTratamiento(getConInterna(), info))
		{
			if(actualizarSeccEvolPiezasDentales(getConInterna(),info))
			{
				if(actualizarSeccEvolBoca(getConInterna(), info))
				{
					// Guardar Inclusiones en BD
					if(accionGuardarInclusionesEnBD(getConInterna(),info))
					{					
			
						Log4JManager.info( info.getPorConfirmar() );
						
						//Guardar Próxima Cita
					
					}
					else					 
						adicionarError("errors.notEspecific","No se Logro Actualizar Odontograma de Evolución. Inclusiones");
				}
				else
					adicionarError("errors.notEspecific","No se Logro Actualizar Odontograma de Evolución. Sección Evolución Boca");
			}
			else
				adicionarError("errors.notEspecific","No se Logro Actualizar Odontograma de Evolución. Sección Evolución en Piezas Dentales");
		}
		else
			adicionarError("errors.notEspecific","No se Logro Actualizar Odontograma de Evolución. Sección Detalle de Plan de Tratamiento");
			
		//****************************************************
		
		logger.info("Fin Actualizar Odontograma Evolucion");
		logger.info("***************************************");
		logger.info("\n\n");
		
	}
	
	
	
	
	
	/**
	 * Llena la informacion del dto de detalle de planta de tratamiento 
	 * */
	public DtoDetallePlanTratamiento llenarDtoDetallePlanTratConInfo(
			InfoHallazgoSuperficie info,
			DtoPlanTratamientoOdo dto,
			int piezaDental,
			String seccion)
	{
		DtoDetallePlanTratamiento dtoDetalle = new DtoDetallePlanTratamiento();
		dtoDetalle.setPlanTratamiento(dto.getCodigoPk().doubleValue());
		dtoDetalle.setPiezaDental(piezaDental);
		
		if(info.getSuperficieOPCIONAL().getCodigo() > 0)
			dtoDetalle.setSuperficie(info.getSuperficieOPCIONAL().getCodigo());

		dtoDetalle.setHallazgo(info.getHallazgoREQUERIDO().getCodigo());
		dtoDetalle.setSeccion(seccion);
		
		dtoDetalle.setClasificacion(info.getClasificacion().getValue());
		dtoDetalle.setPorConfirmar(ConstantesBD.acronimoSi);
		dtoDetalle.setConvencion(Utilidades.convertirAEntero(info.getCodigoConvencion()));
		dtoDetalle.getFechaUsuarioModifica().setUsuarioModifica(dto.getUsuarioModifica().getUsuarioModifica());
		dtoDetalle.getFechaUsuarioModifica().setFechaModifica(UtilidadFecha.getFechaActual());
		dtoDetalle.getFechaUsuarioModifica().setHoraModifica(UtilidadFecha.getHoraActual());
		dtoDetalle.setEspecialidad(dto.getEspecialidad());
		dtoDetalle.setActivo(info.getExisteBD().isActivo()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		dtoDetalle.setValoracion(dto.getCodigoValoracion());
		dtoDetalle.setEvolucion(dto.getCodigoEvolucion());
		
		return dtoDetalle;
	}
	
	/**
	 * Llena la informacion del dto detalle de programas y servicios
	 * */
	public DtoProgramasServiciosPlanT llenarDtoProgramasPlanTratConInfo(
			double codigoPrograma,
			DtoDetallePlanTratamiento dto,
			InfoServicios info)
	{
		DtoProgramasServiciosPlanT dtoProg = new DtoProgramasServiciosPlanT();
		dtoProg.setDetPlanTratamiento(new BigDecimal(dto.getCodigo()));
		
		if(codigoPrograma > 0)
			dtoProg.getPrograma().setCodigo(codigoPrograma);
		
		dtoProg.getServicio().setCodigo(info.getServicio().getCodigo());
		dtoProg.setEstadoPrograma(ConstantesIntegridadDominio.acronimoEstadoPendiente);
		dtoProg.setConvencion(dto.getConvencion());
		dtoProg.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
		dtoProg.setIndicativoPrograma(ConstantesIntegridadDominio.acronimoInicial);
		dtoProg.setIndicativoServicio(ConstantesIntegridadDominio.acronimoInicial);
		dtoProg.setPorConfirmado(ConstantesBD.acronimoSi);
		dtoProg.setEspecialidad(dto.getEspecialidad());
		dtoProg.getUsuarioModifica().setUsuarioModifica(dto.getFechaUsuarioModifica().getUsuarioModifica());
		dtoProg.getUsuarioModifica().setFechaModifica(UtilidadFecha.getFechaActual());
		dtoProg.getUsuarioModifica().setHoraModifica(UtilidadFecha.getHoraActual());
		dtoProg.setOrdenServicio(info.getOrderServicio());
		dtoProg.setActivo(info.getExisteBD().isActivo()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		dtoProg.setInclusion(info.getInclusion());
		dtoProg.setValoracion(dto.getValoracion());
		dtoProg.setEvolucion(dto.getEvolucion());
		return dtoProg;
	}
	
	/**
	 * Guarda la Información de la Sección Otros Hallazgos
	 * */
	public boolean guardarSeccionOtrosHallazgos(Connection con,InfoOdontograma info,DtoPlanTratamientoOdo dtoPlan)
	{
		logger.info("\n\n");
		logger.info("**>> SECCION OTROS HALLAZGOS ");
		
		//GUARDA INFORMACION DE LA SECCION DETALLE PLAN TRATAMIENTO INICIAL
		DtoDetallePlanTratamiento dtoHallazgo =  new DtoDetallePlanTratamiento();
		DtoInfoFechaUsuario usuarioModifica = new DtoInfoFechaUsuario();
		usuarioModifica.setFechaModifica(UtilidadFecha.getFechaActual());
		usuarioModifica.setHoraModifica(UtilidadFecha.getHoraActual());
		usuarioModifica.setUsuarioModifica(info.getUsuarioActual().getUsuarioModifica());
		dtoHallazgo.setFechaUsuarioModifica(usuarioModifica);

		//Recorre los dientes
		for(InfoDetallePlanTramiento diente:info.getInfoPlanTrata().getSeccionOtrosHallazgos())
		{
			if(diente.getExisteBD().isActivo())
			{
				if(diente.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
				{
					//Actualiza el diente si se cambio
					if(diente.getPieza().getCodigo() != diente.getPiezaOld().getCodigo())
					{
						DtoDetallePlanTratamiento cambios = new DtoDetallePlanTratamiento();
						cambios.setPiezaDental(diente.getPieza().getCodigo());
						cambios.setCodigo(diente.getCodigoPkDetalle().doubleValue());
						cambios.setFechaUsuarioModifica(usuarioModifica);
						
						if(!PlanTratamiento.actualizarDetPlanTratamiento(con, cambios))
						{
							adicionarError("errors.notEspecific","No se Logro Actualizar el Detalle del Plan de Tratamiento");
							return false;
						}
					}
					
					//Recorre los hallazgos y actualiza si hay cambios
					for(InfoHallazgoSuperficie hallazgo:diente.getDetalleSuperficie())
					{
						if(hallazgo.getHallazgoREQUERIDO().getCodigo() != hallazgo.getHallazgoREQUERIDOOld().getCodigo())
						{
							DtoDetallePlanTratamiento cambios = new DtoDetallePlanTratamiento();
							cambios.setHallazgo(hallazgo.getHallazgoREQUERIDO().getCodigo());
							cambios.setCodigo(diente.getCodigoPkDetalle().doubleValue());
							cambios.setFechaUsuarioModifica(usuarioModifica);
							
							if(!PlanTratamiento.actualizarDetPlanTratamiento(con, cambios))
							{
								adicionarError("errors.notEspecific","No se Logro Actualizar el Detalle del Plan de Tratamiento");
								return false;
							}
							
							//Inactiva los programas/servicios asociados al hallazgo anterior
							DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
							parametrosServ.setDetPlanTratamiento(diente.getCodigoPkDetalle());
							parametrosServ.setActivo(ConstantesBD.acronimoNo);
							
							if(!PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ))
							{
								adicionarError("errors.notEspecific","No se Inactivo Programas/Servicios");
								return false;
							}
						}
						
						if(hallazgo.getSuperficieOPCIONAL().getCodigo() != hallazgo.getSuperficieOPCIONALOld().getCodigo())
						{
							DtoDetallePlanTratamiento cambios = new DtoDetallePlanTratamiento();
							cambios.setSuperficie(hallazgo.getSuperficieOPCIONAL().getCodigo());
							cambios.setCodigo(diente.getCodigoPkDetalle().doubleValue());
							cambios.setFechaUsuarioModifica(usuarioModifica);
							
							if(!PlanTratamiento.actualizarDetPlanTratamiento(con, cambios))
							{
								adicionarError("errors.notEspecific","No se Logro Actualizar el Detalle del Plan de Tratamiento");
								return false;
							}
						}

						//Actualiza el codigo Pk del Detalle del plan de tratamiento
						dtoHallazgo.setCodigo(diente.getCodigoPkDetalle().doubleValue());
						
						//Recorre los programas y servicios
						for(InfoProgramaServicioPlan programa:hallazgo.getProgramasOservicios())
						{
							//logger.info("es programa >> "+programa.getCodigoPkProgramaServicio().intValue()+" "+programa.getExisteBD().getActivo()+" "+programa.getExisteBD().getValue());
							//Evalua si es por programas o servicios
							if(programa.getCodigoPkProgramaServicio().intValue() > 0)
							{
								if(programa.getExisteBD().isActivo())
								{
									if(!programa.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
									{
										//logger.info("numero de servicios >> "+programa.getListaServicios().size());
										//Recorre los servicios del programa
										for(InfoServicios servicios:programa.getListaServicios())
										{
											DtoProgramasServiciosPlanT dtoProg =  llenarDtoProgramasPlanTratConInfo(
																						programa.getCodigoPkProgramaServicio().doubleValue(),
																						dtoHallazgo,
																						servicios);

											dtoProg.setCodigoPk(new BigDecimal(PlanTratamiento.guardarProgramasServicio(con,dtoProg)));
											if(dtoProg.getCodigoPk().doubleValue() <= 0)
											{
												adicionarError("errors.notEspecific","No se Logro Guardar Los Programas/Servicios");
												return false;
											}
										}
									}
								}
								else
								{
									if(programa.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
									{
										for(InfoServicios servicios:programa.getListaServicios())
										{
											DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
											parametrosServ.setCodigoPk(servicios.getCodigoPkProgServ());
											parametrosServ.setActivo(ConstantesBD.acronimoNo);
											
											if(!PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ))
											{
												adicionarError("errors.notEspecific","No se Inactivo Programas/Servicios");
												return false;
											}
										}
									}
								}
							}
							else
							{
								//Recorre los servicios del programa
								for(InfoServicios servicios:programa.getListaServicios())
								{
									if(servicios.getExisteBD().isActivo())
									{
										if(!servicios.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
										{
											DtoProgramasServiciosPlanT dtoProg =  llenarDtoProgramasPlanTratConInfo(
																						ConstantesBD.codigoNuncaValidoDouble,
																						dtoHallazgo,
																						servicios);
											
											//logger.info("\n guarda SERVICIOS ");
											dtoProg.setCodigoPk(new BigDecimal(PlanTratamiento.guardarProgramasServicio(con,dtoProg)));
											if(dtoProg.getCodigoPk().doubleValue() <= 0)
											{
												adicionarError("errors.notEspecific","No se Logro Guardar Los Programas/Servicios");
												return false;
											}
										}
									}
									else
									{
										if(servicios.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
										{
											DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
											parametrosServ.setCodigoPk(servicios.getCodigoPkProgServ());
											parametrosServ.setActivo(ConstantesBD.acronimoNo);
											
											if(!PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ))
											{
												adicionarError("errors.notEspecific","No se Inactivo Programas/Servicios");
												return false;
											}
										}
									}
								}
							}
						}
					}
				}
				//Diente no existe y esta activo
				else
				{
					//Recorre los hallazgos
					for(InfoHallazgoSuperficie hallazgo:diente.getDetalleSuperficie())
					{
						dtoHallazgo = 	llenarDtoDetallePlanTratConInfo(
											hallazgo,
											dtoPlan,
											diente.getPieza().getCodigo(),
											ConstantesIntegridadDominio.acronimoOtro);
						
						dtoHallazgo.setCodigo(PlanTratamiento.guardarDetPlanTratamiento(con, dtoHallazgo));
						
						//logger.info("\n hallazgo Superficie a guardar >> "+hallazgo.getSuperficieOPCIONAL().getNombre()+" "+hallazgo.getHallazgoREQUERIDO().getNombre()+" pieza >> "+diente.getPieza().getCodigo()+" codigppk >> "+dtoHallazgo.getCodigo());
						if(dtoHallazgo.getCodigo() <= 0)
						{
							adicionarError("errors.notEspecific","No se Logro Guardar El Detalle del Plan de Tratamiento");
							return false;
						}
							
						//******************************************************************************************************************
						//logger.info("numero de programas >> "+hallazgo.getProgramasOservicios().size());
						//Recorre los programas y servicios
						for(InfoProgramaServicioPlan programa:hallazgo.getProgramasOservicios())
						{
							//logger.info("es programa >> "+programa.getCodigoPkProgramaServicio().intValue()+" "+programa.getExisteBD().getActivo()+" "+programa.getExisteBD().getValue());
							//Evalua si es por programas o servicios
							if(programa.getCodigoPkProgramaServicio().intValue() > 0)
							{
								if(programa.getExisteBD().isActivo())
								{	
									//logger.info("numero de servicios >> "+programa.getListaServicios().size());
									//Recorre los servicios del programa
									for(InfoServicios servicios:programa.getListaServicios())
									{
										DtoProgramasServiciosPlanT dtoProg =  llenarDtoProgramasPlanTratConInfo(
																					programa.getCodigoPkProgramaServicio().doubleValue(),
																					dtoHallazgo,
																					servicios);

										dtoProg.setCodigoPk(new BigDecimal(PlanTratamiento.guardarProgramasServicio(con,dtoProg)));
										if(dtoProg.getCodigoPk().doubleValue() <= 0)
										{
											adicionarError("errors.notEspecific","No se Logro Guardar Los Programas/Servicios");
											return false;
										}
									}
								}
							}
							else
							{
								//Recorre los servicios del programa
								for(InfoServicios servicios:programa.getListaServicios())
								{
									if(servicios.getExisteBD().isActivo())
									{	
										DtoProgramasServiciosPlanT dtoProg =  llenarDtoProgramasPlanTratConInfo(
																					ConstantesBD.codigoNuncaValidoDouble,
																					dtoHallazgo,
																					servicios);
										
										//logger.info("\n guarda SERVICIOS ");
										dtoProg.setCodigoPk(new BigDecimal(PlanTratamiento.guardarProgramasServicio(con,dtoProg)));
										if(dtoProg.getCodigoPk().doubleValue() <= 0)
										{
											adicionarError("errors.notEspecific","No se Logro Guardar Los Programas/Servicios");
											return false;
										}
									}
								}
							}
						}
					}
				}
			}
			else
			{
				//INATIVACIÓN
				if(diente.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
				{
					//Se modifica el diente a Activo NO
					boolean inactivo = false;
					DtoDetallePlanTratamiento parametros = new DtoDetallePlanTratamiento();
					parametros.setCodigo(diente.getCodigoPkDetalle().doubleValue());
					parametros.setActivo(ConstantesBD.acronimoNo);
							
					inactivo = PlanTratamiento.actualizarActivoDetallePlanTrat(con, parametros);
					if(!inactivo)
					{
						adicionarError("errors.notEspecific","No se Logro Inactivar el Detalle del Plan de Tratamiento en la Inactivación");
						return false;
					}
					
					//Recorre los hallazgos
					for(InfoHallazgoSuperficie hallazgo:diente.getDetalleSuperficie())
					{
						//solo inactiva los que esten guardados en base de datos
						if(hallazgo.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
						{
							//Se modifica los programas y servicios a activo NO
							boolean inactivarPrograma = false;
							//Recorre los programas/servicios
							for(InfoProgramaServicioPlan programas:hallazgo.getProgramasOservicios())
							{
								inactivarPrograma = false;
								
								//si es programa
								if(programas.getCodigoPkProgramaServicio().doubleValue() > 0 && 
									programas.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
										inactivarPrograma = true;
								
								for(InfoServicios servicios:programas.getListaServicios())
								{
									if((servicios.getExisteBD().getValue().equals(ConstantesBD.acronimoSi) && 
										servicios.getExisteBD().isActivo() && !inactivarPrograma) || inactivarPrograma)
									{
										DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
										parametrosServ.setCodigoPk(servicios.getCodigoPkProgServ());
										parametrosServ.setActivo(ConstantesBD.acronimoNo);

										inactivo = PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ);
										if(!inactivo)
										{
											adicionarError("errors.notEspecific","No se Inactivo Programas/Servicios");
											return false;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return true;
	}
	
	
	/**
	 * Guarda la Información de la Sección Hallazgos Boca
	 * */
	public boolean guardarSeccionHallazgosBoca(Connection con,InfoOdontograma info,DtoPlanTratamientoOdo dtoPlan)
	{
		logger.info("\n\n");
		logger.info("**>> SECCION HALLAZGOS BOCA ");
		//GUARDA INFORMACION DE LA SECCION DETALLE PLAN TRATAMIENTO INICIAL
		DtoDetallePlanTratamiento dtoHallazgo =  new DtoDetallePlanTratamiento();
		DtoInfoFechaUsuario usuarioModifica = new DtoInfoFechaUsuario();
		usuarioModifica.setFechaModifica(UtilidadFecha.getFechaActual());
		usuarioModifica.setHoraModifica(UtilidadFecha.getHoraActual());
		usuarioModifica.setUsuarioModifica(info.getUsuarioActual().getUsuarioModifica());
		dtoHallazgo.setFechaUsuarioModifica(usuarioModifica);

		//Recorre los dientes
		for(InfoHallazgoSuperficie hallazgo:info.getInfoPlanTrata().getSeccionHallazgosBoca())
		{
			if(hallazgo.getExisteBD().isActivo())
			{
				if(hallazgo.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
				{
					if(hallazgo.getHallazgoREQUERIDO().getCodigo() != hallazgo.getHallazgoREQUERIDOOld().getCodigo())
					{
						DtoDetallePlanTratamiento cambios = new DtoDetallePlanTratamiento();
						cambios.setHallazgo(hallazgo.getHallazgoREQUERIDO().getCodigo());
						cambios.setCodigo(hallazgo.getCodigoPkDetalle().doubleValue());
						cambios.setFechaUsuarioModifica(usuarioModifica);
						
						if(!PlanTratamiento.actualizarDetPlanTratamiento(con, cambios))
						{
							adicionarError("errors.notEspecific","No se Logro Actualizar el Detalle del Plan de Tratamiento");
							return false;
						}
						
						//Inactiva los programas/servicios asociados al hallazgo anterior
						DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
						parametrosServ.setDetPlanTratamiento(hallazgo.getCodigoPkDetalle());
						parametrosServ.setActivo(ConstantesBD.acronimoNo);
						
						if(!PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ))
						{
							adicionarError("errors.notEspecific","No se Inactivo Programas/Servicios");
							return false;
						}
					}
					
					//Actualiza el codigo Pk del Detalle del plan de tratamiento
					dtoHallazgo.setCodigo(hallazgo.getCodigoPkDetalle().doubleValue());

					//Recorre los programas y servicios
					for(InfoProgramaServicioPlan programa:hallazgo.getProgramasOservicios())
					{
						//logger.info("es programa >> "+programa.getCodigoPkProgramaServicio().intValue()+" "+programa.getExisteBD().getActivo()+" "+programa.getExisteBD().getValue());
						//Evalua si es por programa o servicios
						if(programa.getCodigoPkProgramaServicio().intValue() > 0)
						{
							if(programa.getExisteBD().isActivo())
							{
								if(!programa.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
								{
									//logger.info("numero de servicios >> "+programa.getListaServicios().size());
									//Recorre los servicios del programa
									for(InfoServicios servicios:programa.getListaServicios())
									{
										DtoProgramasServiciosPlanT dtoProg =  llenarDtoProgramasPlanTratConInfo(
																					programa.getCodigoPkProgramaServicio().doubleValue(),
																					dtoHallazgo,
																					servicios);

										dtoProg.setCodigoPk(new BigDecimal(PlanTratamiento.guardarProgramasServicio(con,dtoProg)));
										if(dtoProg.getCodigoPk().doubleValue() <= 0)
										{
											adicionarError("errors.notEspecific","No se Logro Guardar Los Programas/Servicios");
											return false;
										}
									}
								}
							}
							else
							{
								if(programa.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
								{
									for(InfoServicios servicios:programa.getListaServicios())
									{
										DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
										parametrosServ.setCodigoPk(servicios.getCodigoPkProgServ());
										parametrosServ.setActivo(ConstantesBD.acronimoNo);
										
										if(!PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ))
										{
											adicionarError("errors.notEspecific","No se Inactivo Programas/Servicios");
											return false;
										}
									}
								}
							}
						}
						else
						{
							//Recorre los servicios del programa
							for(InfoServicios servicios:programa.getListaServicios())
							{
								if(servicios.getExisteBD().isActivo())
								{
									if(!servicios.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
									{
										DtoProgramasServiciosPlanT dtoProg =  llenarDtoProgramasPlanTratConInfo(
																					ConstantesBD.codigoNuncaValidoDouble,
																					dtoHallazgo,
																					servicios);
										
										//logger.info("\n guarda SERVICIOS ");
										dtoProg.setCodigoPk(new BigDecimal(PlanTratamiento.guardarProgramasServicio(con,dtoProg)));
										if(dtoProg.getCodigoPk().doubleValue() <= 0)
										{
											adicionarError("errors.notEspecific","No se Logro Guardar Los Programas/Servicios");
											return false;
										}
									}
								}
								else
								{
									if(servicios.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
									{
										DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
										parametrosServ.setCodigoPk(servicios.getCodigoPkProgServ());
										parametrosServ.setActivo(ConstantesBD.acronimoNo);
										
										if(!PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ))
										{
											adicionarError("errors.notEspecific","No se Inactivo Programas/Servicios");
											return false;
										}
									}
								}
							}
						}
					}
				}
				//Diente no existe y esta activo
				else
				{
					dtoHallazgo = 	llenarDtoDetallePlanTratConInfo(
										hallazgo,
										dtoPlan,
										ConstantesBD.codigoNuncaValido,
										ConstantesIntegridadDominio.acronimoBoca);
						
					dtoHallazgo.setCodigo(PlanTratamiento.guardarDetPlanTratamiento(con, dtoHallazgo));
						
					//logger.info("\n hallazgo Superficie a guardar >> "+hallazgo.getSuperficieOPCIONAL().getNombre()+" "+hallazgo.getHallazgoREQUERIDO().getNombre()+" codigppk >> "+dtoHallazgo.getCodigo());
					if(dtoHallazgo.getCodigo() <= 0)
					{
						adicionarError("errors.notEspecific","No se Logro Guardar El Detalle del Plan de Tratamiento");
						return false;
					}
							
					//******************************************************************************************************************
					//logger.info("numero de programas >> "+hallazgo.getProgramasOservicios().size());
					//Recorre los programas y servicios
					for(InfoProgramaServicioPlan programa:hallazgo.getProgramasOservicios())
					{
						//logger.info("es programa >> "+programa.getCodigoPkProgramaServicio().intValue()+" "+programa.getExisteBD().getActivo()+" "+programa.getExisteBD().getValue());
						//Evalua si es por programas o servicios
						if(programa.getCodigoPkProgramaServicio().intValue() > 0)
						{
							if(programa.getExisteBD().isActivo())
							{	
								//logger.info("numero de servicios >> "+programa.getListaServicios().size());
								//Recorre los servicios del programa
								for(InfoServicios servicios:programa.getListaServicios())
								{
									DtoProgramasServiciosPlanT dtoProg =  llenarDtoProgramasPlanTratConInfo(
																				programa.getCodigoPkProgramaServicio().doubleValue(),
																				dtoHallazgo,
																				servicios);

									dtoProg.setCodigoPk(new BigDecimal(PlanTratamiento.guardarProgramasServicio(con,dtoProg)));
									if(dtoProg.getCodigoPk().doubleValue() <= 0)
									{
										adicionarError("errors.notEspecific","No se Logro Guardar Los Programas/Servicios");
										return false;
									}
								}
							}
						}
						else
						{
							//Recorre los servicios del programa
							for(InfoServicios servicios:programa.getListaServicios())
							{
								if(servicios.getExisteBD().isActivo())
								{	
									DtoProgramasServiciosPlanT dtoProg =  llenarDtoProgramasPlanTratConInfo(
																				ConstantesBD.codigoNuncaValidoDouble,
																				dtoHallazgo,
																				servicios);
									
									//logger.info("\n guarda SERVICIOS ");
									dtoProg.setCodigoPk(new BigDecimal(PlanTratamiento.guardarProgramasServicio(con,dtoProg)));
									if(dtoProg.getCodigoPk().doubleValue() <= 0)
									{
										adicionarError("errors.notEspecific","No se Logro Guardar Los Programas/Servicios");
										return false;
									}
								}
							}
						}
					}
				}
			}
			else
			{
				//INATIVACIÓN
				//solo inactiva los que esten guardados en base de datos
				if(hallazgo.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
				{
					boolean inactivo = false;
					DtoDetallePlanTratamiento parametros = new DtoDetallePlanTratamiento();
					parametros.setCodigo(hallazgo.getCodigoPkDetalle().doubleValue());
					parametros.setActivo(ConstantesBD.acronimoNo);
					
					inactivo = PlanTratamiento.actualizarActivoDetallePlanTrat(con, parametros);
					if(!inactivo)
					{
						adicionarError("errors.notEspecific","No se Logro Inactivar el Detalle del Plan de Tratamiento en la Inactivación");
						return false;
					}
					
					//Se modifica los programas y servicios a activo NO
					boolean inactivarPrograma = false;
					//Recorre los programas/servicios
					for(InfoProgramaServicioPlan programas:hallazgo.getProgramasOservicios())
					{
						inactivarPrograma = false;
						
						//si es programa
						if(programas.getCodigoPkProgramaServicio().doubleValue() > 0 && 
							programas.getExisteBD().getValue().equals(ConstantesBD.acronimoSi))
								inactivarPrograma = true;
						
						for(InfoServicios servicios:programas.getListaServicios())
						{
							if((servicios.getExisteBD().getValue().equals(ConstantesBD.acronimoSi) && 
								servicios.getExisteBD().isActivo() && !inactivarPrograma) || inactivarPrograma)
							{
								DtoProgramasServiciosPlanT parametrosServ = new DtoProgramasServiciosPlanT();
								parametrosServ.setCodigoPk(servicios.getCodigoPkProgServ());
								parametrosServ.setActivo(ConstantesBD.acronimoNo);

								inactivo = PlanTratamiento.actualizarActivoProgServPlanTr(con, parametrosServ);
								if(!inactivo)
								{
									adicionarError("errors.notEspecific","No se Inactivo Programas/Servicios");
									return false;
								}
							}
						}
					}
				}
			}
		}
		
		return true;
	}

	

	/**
	 * 
	 * */
	public InfoOdontograma accionEmpezar(
								InfoOdontograma info,
								int codigoPaciente,
								int codigoIngreso,
								int edadPaciente,
								int codigoCita,
								int codigoValoracion,
								int codigoEvolucion,
								String indicadorPlanTrata,
								String indicadorOdonto,
								UsuarioBasico usuarioActual,
								String estado,
								PersonaBasica paciente)
	{
		info.setInstitucion(usuarioActual.getCodigoInstitucionInt());
		info.setCodigoPaciente(codigoPaciente);
		info.setIdIngresoPaciente(codigoIngreso);
		info.setEdadPaciente(edadPaciente);
		info.setCodigoCita(codigoCita);
		info.setCodigoValoracion(codigoValoracion);
		info.setCodigoEvolucion(codigoEvolucion);
		info.setIndicadorPlantTratamiento(indicadorPlanTrata);
		info.setIndicadorOdontograma(indicadorOdonto);
		info.setCodigoMedico(usuarioActual.getCodigoPersona());
		info.getUsuarioActual().setUsuarioModifica(usuarioActual.getLoginUsuario());
		info.setCodigoCentroAtencion(usuarioActual.getCodigoCentroAtencion());
		
		info.setArrayHallazgosDiente(HallazgosOdontologicos.busquedaConvencionesHallagos(info.getInstitucion(),ConstantesIntegridadDominio.acronimoAplicaADiente ,true,true));
		info.setArrayHallazgosSuperficie(HallazgosOdontologicos.busquedaConvencionesHallagos(info.getInstitucion(),ConstantesIntegridadDominio.acronimoAplicaASuperficie ,true,true));
		info.setArrayHallazgosBoca(HallazgosOdontologicos.busquedaConvencionesHallagos(info.getInstitucion(),ConstantesIntegridadDominio.acronimoAplicaABoca ,false,false));
		
		info.setArraySuperficies(PlanTratamiento.cargarSuperficiesDiente(info.getInstitucion()));
		
		info.setEsBuscarPorPrograma(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(info.getInstitucion()));
	
		return centralAcciones(info,estado,paciente);
	}
	
	/**
	 * @param InfoOdontograma info
	 * */
	private InfoOdontograma accionCargarOdontograma(InfoOdontograma info)
	{
		//Cargar los Valores por Defecto
		info.setValidaPresuOdontCont(ValoresPorDefecto.getValidaPresupuestoOdoContratado(info.getInstitucion()));
		info.setUtilizaProgOdontIns(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(info.getInstitucion()));
		info.setInstRegistraAtenExt(ValoresPorDefecto.getInstitucionRegistraAtencionExterna(info.getInstitucion()));
		String codigoTarifarioServicios = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(info.getInstitucion());
	    logger.info("\n\n Codigo Tarifario >> "+codigoTarifarioServicios);	
		//Cargar Los Motivos de Cancelación/Exclusion/Garantía
		MotivosAtencionOdontologica motivo = new MotivosAtencionOdontologica();
		ArrayList<Integer> filtroEstados= new ArrayList<Integer>();
		filtroEstados.add(ConstantesBD.CancelarProgramasServicio); 
		filtroEstados.add(ConstantesBD.ExcluirProgramasServicio);
		filtroEstados.add(ConstantesBD.SolicitarGarantia);
		info.setArrayMotivoCancelacion(motivo.consultarMotivoAtencionO(filtroEstados, info.getInstitucion()));
		
		//Carga la Información de las convenciones parametrizadas
		info.setInfoPlanTrata(new InfoPlanTratamiento());
		info.setXmlOdontograma("");

		//Inicializa la información del Plan de Tratamiento
		info.getInfoPlanTrata().setCodigoPk(PlanTratamiento.obtenerUltimoCodigoPlanTratamiento(info.getIdIngresoPaciente(), new ArrayList<String>(), "" /*porConfirmar*/));
		
		//validamos que cargue el código
		if(info.getInfoPlanTrata().getCodigoPk().doubleValue()>0)
		{
			//Cargamos la información del encabezado del plan de tratamiento
			DtoPlanTratamientoOdo parametros = new DtoPlanTratamientoOdo();
			parametros.setCodigoPk(info.getInfoPlanTrata().getCodigoPk());
			parametros.setInstitucion(info.getInstitucion());
			info.setDtoInfoPlanTratamiento(PlanTratamiento.consultarPlanTratamiento(parametros));
			logger.info("Estado plan tratamiento "+info.getDtoInfoPlanTratamiento().getEstado());

			//Verifica los estados
			if( 
					info.getDtoInfoPlanTratamiento().getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoCancelado) ||
						info.getDtoInfoPlanTratamiento().getEstado().equals(ConstantesIntegridadDominio.acronimoInactivo))
			{
				info.setInfoPlanTrata(new InfoPlanTratamiento());
			}
			else if(info.getDtoInfoPlanTratamiento().getEstado().equals(ConstantesIntegridadDominio.acronimoTerminado) ||
					info.getDtoInfoPlanTratamiento().getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoActivo) || 
					info.getDtoInfoPlanTratamiento().getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoEnProceso) ||
						info.getDtoInfoPlanTratamiento().getEstado().equals(ConstantesIntegridadDominio.acronimoSuspendidoTemporalmente))
			{
				
				//*****************************************************************************************************
				//*****************************************************************************************************
				//SECCION PLAN TRATAMIENTO INICIAL
				
				//Carga las Piezas Dentales
				ArrayList<InfoDatosInt> arrayPiezas= PlanTratamiento.obtenerPiezas(
														info.getInfoPlanTrata().getCodigoPk(),
														ConstantesIntegridadDominio.acronimoDetalle, "" /*porConfirmar*/);
				
				//Se iteran las piezas para obtener los hallazgos ligados a la pieza
				//y se asignan a los detalles
				DtoDetallePlanTratamiento parametrosSuperficies = new DtoDetallePlanTratamiento();
				parametrosSuperficies.setPlanTratamiento(info.getDtoInfoPlanTratamiento().getCodigoPk().doubleValue());
				parametrosSuperficies.setSeccion(ConstantesIntegridadDominio.acronimoDetalle);
				parametrosSuperficies.setActivo(ConstantesBD.acronimoSi);
				
				for(InfoDatosInt pieza: arrayPiezas)
				{
					InfoDetallePlanTramiento hallazgoSuperficie= new InfoDetallePlanTramiento();
					
					//Información de la pieza
					hallazgoSuperficie.setPieza(pieza);
					hallazgoSuperficie.getExisteBD().setActivo(true);
					hallazgoSuperficie.getExisteBD().setValue(ConstantesBD.acronimoSi);
					parametrosSuperficies.setPiezaDental(pieza.getCodigo());
					
					hallazgoSuperficie.setDetalleSuperficie(PlanTratamiento.obtenerHallazgosSuperficies(parametrosSuperficies));
					
					DtoProgramasServiciosPlanT parametrosProgServ = new DtoProgramasServiciosPlanT();
					parametrosProgServ.setCodigoCita(new BigDecimal(info.getCodigoCita()));
					parametrosProgServ.setActivo(ConstantesBD.acronimoSi);
					parametrosProgServ.setEstadosProgramasOservicios(new ArrayList<String>());
					parametrosProgServ.setBuscarProgramas(info.getEsBuscarPorPrograma());
					parametrosProgServ.setUsuarioModifica(info.getUsuarioActual());
					parametrosProgServ.setCodigoTarifario(codigoTarifarioServicios);
					
					//recorre los hallazgos y carga sus programas y servicios
					for(InfoHallazgoSuperficie hallsuper : hallazgoSuperficie.getDetalleSuperficie())
					{
						parametrosProgServ.setDetPlanTratamiento(hallsuper.getCodigoPkDetalle());
						hallsuper.setProgramasOservicios(PlanTratamiento.obtenerProgramasOServicios(parametrosProgServ));
					}

					info.getInfoPlanTrata().getSeccionHallazgosDetalle().add(hallazgoSuperficie);
					
					info.getInfoPlanTrata().getSeccionProgServCita().add(hallazgoSuperficie);
				}
				
				//*****************************************************************************************************
				//*****************************************************************************************************
				//SECCION PLAN TRATAMIENTO INICIAL PERO LLENENDOLO EN LA SECCION DE PROGRAMAS/SERVICIOS CITA
				
				//Carga las Piezas Dentales
//				arrayPiezas=PlanTratamiento.obtenerPiezas(
//														info.getInfoPlanTrata().getCodigoPk(),
//														ConstantesIntegridadDominio.acronimoDetalle, "" /*porConfirmar*/);
//				
//				//iteramos las piezas para obtener los hallazgos ligados a la pieza
//				//y los seteamos a los detalles
//				parametrosSuperficies = new DtoDetallePlanTratamiento();
//				parametrosSuperficies.setPlanTratamiento(info.getDtoInfoPlanTratamiento().getCodigoPk().doubleValue());
//				parametrosSuperficies.setSeccion(ConstantesIntegridadDominio.acronimoDetalle);
//				parametrosSuperficies.setActivo(ConstantesBD.acronimoSi);
//				
//				logger.info("Voy a adicionar los hallazgoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooos");
//				
//				for(InfoDatosInt pieza: arrayPiezas)
//				{
//					InfoDetallePlanTramiento hallazgoSuperficie= new InfoDetallePlanTramiento();
//					
//					//Información de la pieza
//					hallazgoSuperficie.setPieza(pieza);
//					hallazgoSuperficie.getExisteBD().setActivo(true);
//					hallazgoSuperficie.getExisteBD().setValue(ConstantesBD.acronimoSi);
//					parametrosSuperficies.setPiezaDental(pieza.getCodigo());
//					
//					logger.info("llamadollamadollamadollamadollamadollamadollamadollamadollamadollamadollamadollamadollamado 2");
//					hallazgoSuperficie.setDetalleSuperficie(PlanTratamiento.obtenerHallazgosSuperficies(parametrosSuperficies));
//					
//					DtoProgramasServiciosPlanT parametrosProgServ = new DtoProgramasServiciosPlanT();
//					parametrosProgServ.setCodigoCita(new BigDecimal(info.getCodigoCita()));
//					parametrosProgServ.setActivo(ConstantesBD.acronimoSi);
//					parametrosProgServ.setEstadosProgramasOservicios(new ArrayList<String>());
//					parametrosProgServ.setBuscarProgramas(info.getEsBuscarPorPrograma());
//					parametrosProgServ.setCodigoTarifario(codigoTarifarioServicios);
//					
//					//recorre los hallazgos y carga sus programas y servicios
//					for(InfoHallazgoSuperficie hallsuper : hallazgoSuperficie.getDetalleSuperficie())
//					{
//						parametrosProgServ.setDetPlanTratamiento(hallsuper.getCodigoPkDetalle());
//						hallsuper.setProgramasOservicios(PlanTratamiento.obtenerProgramasOServicios(parametrosProgServ));						
//						
//					}
//					
//					
//					logger.info("Voy a adicionar los hallazgoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooos"+hallazgoSuperficie.getPieza());
//                    
//					
//					
//					info.getInfoPlanTrata().getSeccionProgServCita().add(hallazgoSuperficie);
//				}
				
				//*****************************************************************************************************
				//*****************************************************************************************************
				//SECCION OTROS HALLAZGOS
				//Carga las Piezas Dentales
				arrayPiezas= PlanTratamiento.obtenerPiezas(
								info.getInfoPlanTrata().getCodigoPk(),
								ConstantesIntegridadDominio.acronimoOtro, "" /*porConfirmar*/);
				
				//iteramos las piezas para obtener los hallazgos ligados a la pieza
				//y los seteamos a los detalles
				parametrosSuperficies = new DtoDetallePlanTratamiento();
				parametrosSuperficies.setPlanTratamiento(info.getDtoInfoPlanTratamiento().getCodigoPk().doubleValue());
				parametrosSuperficies.setSeccion(ConstantesIntegridadDominio.acronimoOtro);
				parametrosSuperficies.setActivo(ConstantesBD.acronimoSi);
				
				for(InfoDatosInt pieza: arrayPiezas)
				{	
					InfoDetallePlanTramiento hallazgoSuperficie= new InfoDetallePlanTramiento();
					
					
					//Informacion de la pieza
					hallazgoSuperficie.setPieza(pieza);
					hallazgoSuperficie.getExisteBD().setActivo(true);
					hallazgoSuperficie.getExisteBD().setValue(ConstantesBD.acronimoSi);
					parametrosSuperficies.setPiezaDental(pieza.getCodigo());
					
					hallazgoSuperficie.setDetalleSuperficie(PlanTratamiento.obtenerHallazgosSuperficies(parametrosSuperficies));
					
					DtoProgramasServiciosPlanT parametrosProgServ = new DtoProgramasServiciosPlanT();
					parametrosProgServ.setCodigoCita(new BigDecimal(info.getCodigoCita()));
					parametrosProgServ.setActivo(ConstantesBD.acronimoSi);
					parametrosProgServ.setEstadosProgramasOservicios(new ArrayList<String>());
					parametrosProgServ.setBuscarProgramas(info.getEsBuscarPorPrograma());
					parametrosProgServ.setCodigoTarifario(codigoTarifarioServicios);
					
					
					//recorre los hallazgos y carga sus programas y servicios
					for(InfoHallazgoSuperficie hallsuper : hallazgoSuperficie.getDetalleSuperficie())
					{
						parametrosProgServ.setDetPlanTratamiento(hallsuper.getCodigoPkDetalle());
						hallsuper.setProgramasOservicios(PlanTratamiento.obtenerProgramasOServicios(parametrosProgServ));
					}
					
					

					info.getInfoPlanTrata().getSeccionOtrosHallazgos().add(hallazgoSuperficie);
					
					info.getInfoPlanTrata().getSeccionProgServCitaOtro().add(hallazgoSuperficie);
				}
//				
//				//*****************************************************************************************************
//				//*****************************************************************************************************
//				//SECCION OTROS HALLAZGOS
//				//Carga las Piezas Dentales
//				arrayPiezas= PlanTratamiento.obtenerPiezas(
//								info.getInfoPlanTrata().getCodigoPk(),
//								ConstantesIntegridadDominio.acronimoOtro, "" /*porConfirmar*/);
//				
//				//iteramos las piezas para obtener los hallazgos ligados a la pieza
//				//y los seteamos a los detalles
//				parametrosSuperficies = new DtoDetallePlanTratamiento();
//				parametrosSuperficies.setPlanTratamiento(info.getDtoInfoPlanTratamiento().getCodigoPk().doubleValue());
//				parametrosSuperficies.setSeccion(ConstantesIntegridadDominio.acronimoOtro);
//				parametrosSuperficies.setActivo(ConstantesBD.acronimoSi);
//				
//				for(InfoDatosInt pieza: arrayPiezas)
//				{	
//					InfoDetallePlanTramiento hallazgoSuperficie= new InfoDetallePlanTramiento();
//					
//					
//					//Informacion de la pieza
//					hallazgoSuperficie.setPieza(pieza);
//					hallazgoSuperficie.getExisteBD().setActivo(true);
//					hallazgoSuperficie.getExisteBD().setValue(ConstantesBD.acronimoSi);
//					parametrosSuperficies.setPiezaDental(pieza.getCodigo());
//					
//					hallazgoSuperficie.setDetalleSuperficie(PlanTratamiento.obtenerHallazgosSuperficies(parametrosSuperficies));
//					
//					DtoProgramasServiciosPlanT parametrosProgServ = new DtoProgramasServiciosPlanT();
//					parametrosProgServ.setCodigoCita(new BigDecimal(info.getCodigoCita()));
//					parametrosProgServ.setActivo(ConstantesBD.acronimoSi);
//					parametrosProgServ.setEstadosProgramasOservicios(new ArrayList<String>());
//					parametrosProgServ.setBuscarProgramas(info.getEsBuscarPorPrograma());
//					parametrosProgServ.setCodigoTarifario(codigoTarifarioServicios);
//					
//					
//					//recorre los hallazgos y carga sus programas y servicios
//					for(InfoHallazgoSuperficie hallsuper : hallazgoSuperficie.getDetalleSuperficie())
//					{
//						parametrosProgServ.setDetPlanTratamiento(hallsuper.getCodigoPkDetalle());
//						hallsuper.setProgramasOservicios(PlanTratamiento.obtenerProgramasOServicios(parametrosProgServ));
//					}
//					
//					
//					info.getInfoPlanTrata().getSeccionProgServCitaOtro().add(hallazgoSuperficie);
//				}
				
				
				//*****************************************************************************************************
				//*****************************************************************************************************
				//SECCION HALLAZGOS BOCA
				
				//Carga los dientes,hallazgos, superficies
				ArrayList<InfoHallazgoSuperficie> arrayInfoHallSuperBoca = new ArrayList<InfoHallazgoSuperficie>();
				ArrayList<InfoDetallePlanTramiento> arrayIinfoDetalle = new ArrayList<InfoDetallePlanTramiento>();
				
				parametrosSuperficies = new DtoDetallePlanTratamiento();
				parametrosSuperficies.setPlanTratamiento(info.getDtoInfoPlanTratamiento().getCodigoPk().doubleValue());
				parametrosSuperficies.setSeccion(ConstantesIntegridadDominio.acronimoBoca);
				parametrosSuperficies.setActivo(ConstantesBD.acronimoSi);
				arrayIinfoDetalle = PlanTratamiento.obtenerHallazgosSuperficiesSeccionOtrayBoca(parametrosSuperficies);
					
				DtoProgramasServiciosPlanT parametrosProgServ = new DtoProgramasServiciosPlanT();
				parametrosProgServ.setActivo(ConstantesBD.acronimoSi);
				parametrosProgServ.setEstadosProgramasOservicios(new ArrayList<String>());
				parametrosProgServ.setBuscarProgramas(info.getEsBuscarPorPrograma());
				parametrosProgServ.setCodigoTarifario(codigoTarifarioServicios);

				for(InfoDetallePlanTramiento infoDetalle: arrayIinfoDetalle)
				{
					//recorre los hallazgos y carga sus programas y servicios
					for(InfoHallazgoSuperficie hallsuper : infoDetalle.getDetalleSuperficie())
					{
						parametrosProgServ.setDetPlanTratamiento(hallsuper.getCodigoPkDetalle());
						hallsuper.setProgramasOservicios(PlanTratamiento.obtenerProgramasOServicios(parametrosProgServ));
						arrayInfoHallSuperBoca.add(hallsuper);
					}
				}
				
				info.getInfoPlanTrata().setSeccionHallazgosBoca(arrayInfoHallSuperBoca);
			}
		}
		
		info.setXmlOdontograma(getXmlPlanTratamiento(info));
		llenadoSeccionProgServCitAsig(info);
		cargarPosiblesEstadosProgServ(info);
		return info;
	}

	/**
	 * Metodo que Carga el historico de la Evolución ( Utilizado para la IMPRESION )
	 * @param con
	 * @param info
	 * @param codigoInstitucionInt
	 * @param bigDecimal
	 */
	public void accionCargarOdontogramaHisConf(Connection con,InfoOdontograma info, int codigoInstitucion,BigDecimal codigoEvolucion) {
		
      PlanTratamiento.consultarHisConfPlanTratamiento(con, info.getInfoPlanTrata());
	  String codigoTarifario= ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion);	
		
		if(info.getInfoPlanTrata().getCodigoPk().longValue()>0)
		{		
		
			//*****************************************************************************************************
			//*****************************************************************************************************
			//SECCION PLAN TRATAMIENTO INICIAL
			
			//Carga las Piezas Dentales
			ArrayList<InfoDatosInt> arrayPiezas= PlanTratamiento.obtenerPiezasHisConf(con, info.getInfoPlanTrata(), ConstantesIntegridadDominio.acronimoDetalle);
	
            		
			//iteramos las piezas para obtener los hallazgos ligados a la pieza
			//y los seteamos a los detalles
			DtoDetallePlanTratamiento parametrosSuperficies = new DtoDetallePlanTratamiento();
			parametrosSuperficies.setPlanTratamiento(info.getInfoPlanTrata().getCodigoPk().doubleValue());
			parametrosSuperficies.setSeccion(ConstantesIntegridadDominio.acronimoDetalle);
			parametrosSuperficies.setActivo(ConstantesBD.acronimoSi);
			parametrosSuperficies.setValoracion(info.getInfoPlanTrata().getValoracion());
			parametrosSuperficies.setEvolucion(info.getInfoPlanTrata().getEvolucion());
		
			
			
			
			for(InfoDatosInt pieza: arrayPiezas)
			{				
				
				InfoDetallePlanTramiento hallazgoSuperficie= new InfoDetallePlanTramiento();
				
				//Informacion de la pieza
				hallazgoSuperficie.setPieza(pieza);
				hallazgoSuperficie.getExisteBD().setActivo(true);
				hallazgoSuperficie.getExisteBD().setValue(ConstantesBD.acronimoSi);
				parametrosSuperficies.setPiezaDental(pieza.getCodigo());
				parametrosSuperficies.setPath(info.getPathNombreContexo());
				
				hallazgoSuperficie.setDetalleSuperficie(PlanTratamiento.obtenerHallazgosSuperficiesHisConf(con, parametrosSuperficies));
				
				
				DtoProgramasServiciosPlanT parametrosProgServ = new DtoProgramasServiciosPlanT();
				parametrosProgServ.setActivo(ConstantesBD.acronimoSi);
				parametrosProgServ.setEstadosProgramasOservicios(new ArrayList<String>());
				parametrosProgServ.setBuscarProgramas(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(codigoInstitucion));
				parametrosProgServ.setValoracion(info.getInfoPlanTrata().getValoracion());
				parametrosProgServ.setEvolucion(info.getInfoPlanTrata().getEvolucion());
				parametrosProgServ.setCodigoTarifario(codigoTarifario);
		
				
				
				//recorre los hallazgos y carga sus programas y servicios
				for(InfoHallazgoSuperficie hallsuper : hallazgoSuperficie.getDetalleSuperficie())
				{
					parametrosProgServ.setDetPlanTratamiento(hallsuper.getCodigoPkDetalle());
					
					hallsuper.setProgramasOservicios(PlanTratamiento.obtenerProgramasOServiciosHisConf(con, parametrosProgServ));					
					hallsuper.getExisteBD().setValue(ConstantesBD.acronimoSi);
					
					//Recorre los servicios
					for(InfoProgramaServicioPlan progServ:hallsuper.getProgramasOservicios())
					{	
						//Actualiza la información para los permisos de eliminar
						logger.info("CodigoPK Programa "+progServ.getCodigoPkProgramaServicio().doubleValue());
						if(progServ.getCodigoPkProgramaServicio().doubleValue() > 0)
						{
							progServ.getExisteBD().setEsEliminable(false);
							progServ.setInactivarProg(ConstantesBD.acronimoSi);
						}
						logger.info("Tamaño Lista Servicios "+progServ.getListaServicios().size());
						if(progServ.getListaServicios().size()>0)
						{
							for(InfoServicios servicios:progServ.getListaServicios())
							{
								servicios.getExisteBD().setEsEliminable(false);
								servicios.setInactivarServ(ConstantesBD.acronimoSi);
							}
						}
					}
				}

				info.getInfoPlanTrata().getSeccionHallazgosDetalle().add(hallazgoSuperficie);
				info.getInfoPlanTrata().getSeccionProgServCita().add(hallazgoSuperficie);
				
				
			} // FIN  	SECCION PLAN TRATAMIENTO INICIAL
			
			
			
			
			
			
			//*****************************************************************************************************
			//*****************************************************************************************************
			//SECCION OTROS HALLAZGOS
			
			//Carga los dientes,hallazgos, superficies
			ArrayList<InfoDetallePlanTramiento> hallazgoSuperficie = new ArrayList<InfoDetallePlanTramiento>();
			
			parametrosSuperficies = new DtoDetallePlanTratamiento();
			parametrosSuperficies.setPlanTratamiento(info.getInfoPlanTrata().getCodigoPk().doubleValue());
			parametrosSuperficies.setSeccion(ConstantesIntegridadDominio.acronimoOtro);
			parametrosSuperficies.setActivo(ConstantesBD.acronimoSi);
			parametrosSuperficies.setValoracion(info.getInfoPlanTrata().getValoracion());
			parametrosSuperficies.setEvolucion(info.getInfoPlanTrata().getEvolucion());
			
			
			hallazgoSuperficie = PlanTratamiento.obtenerHallazgosSuperficiesSeccionOtrayBocaHisConf(con,parametrosSuperficies);
				
			DtoProgramasServiciosPlanT parametrosProgServ = new DtoProgramasServiciosPlanT();
			parametrosProgServ.setActivo(ConstantesBD.acronimoSi);
			parametrosProgServ.setEstadosProgramasOservicios(new ArrayList<String>());
			parametrosProgServ.setBuscarProgramas(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(codigoInstitucion));
			parametrosProgServ.setValoracion(info.getInfoPlanTrata().getValoracion());
			parametrosProgServ.setEvolucion(info.getInfoPlanTrata().getEvolucion());
			parametrosProgServ.setCodigoTarifario(codigoTarifario);
			
			
			
			
			
			for(InfoDetallePlanTramiento pieza: hallazgoSuperficie)
			{
				//recorre los hallazgos y carga sus programas y servicios
				for(InfoHallazgoSuperficie hallsuper : pieza.getDetalleSuperficie())
				{
					parametrosProgServ.setDetPlanTratamiento(hallsuper.getCodigoPkDetalle());
					hallsuper.setProgramasOservicios(PlanTratamiento.obtenerProgramasOServiciosHisConf(con, parametrosProgServ));
					hallsuper.getExisteBD().setValue(ConstantesBD.acronimoSi);
					
					//Recorre los servicios
					for(InfoProgramaServicioPlan progServ:hallsuper.getProgramasOservicios())
					{
						logger.info("CodigoPK Programa "+progServ.getCodigoPkProgramaServicio().doubleValue());
						//Actualiza la información para los permisos de eliminar
						if(progServ.getCodigoPkProgramaServicio().doubleValue() > 0)
						{
							progServ.getExisteBD().setEsEliminable(false);
						    progServ.setInactivarProg(ConstantesBD.acronimoSi);
					    }
						
						logger.info("Tamaño Lista Servicios "+progServ.getListaServicios().size());
						if(progServ.getListaServicios().size()>0){
							for(InfoServicios servicios:progServ.getListaServicios()){
								servicios.getExisteBD().setEsEliminable(false);
								servicios.setInactivarServ(ConstantesBD.acronimoSi);
							}
						}
					}
				}
				
			}
					
			
			info.getInfoPlanTrata().setSeccionOtrosHallazgos(hallazgoSuperficie);
			ArrayList<InfoDetallePlanTramiento> seccionProgServCitaOtro=new ArrayList<InfoDetallePlanTramiento>();
			seccionProgServCitaOtro.addAll(hallazgoSuperficie);
			info.getInfoPlanTrata().setSeccionProgServCitaOtro(seccionProgServCitaOtro);
			
			
			

			//*****************************************************************************************************
			//*****************************************************************************************************
			//SECCION HALLAZGOS BOCA
			
			//Carga los dientes,hallazgos, superficies
			ArrayList<InfoHallazgoSuperficie> arrayInfoHallSuperBoca = new ArrayList<InfoHallazgoSuperficie>();
			ArrayList<InfoDetallePlanTramiento> arrayIinfoDetalle = new ArrayList<InfoDetallePlanTramiento>();
			
			parametrosSuperficies = new DtoDetallePlanTratamiento();
			parametrosSuperficies.setPlanTratamiento(info.getInfoPlanTrata().getCodigoPk().doubleValue());
			parametrosSuperficies.setSeccion(ConstantesIntegridadDominio.acronimoBoca);
			parametrosSuperficies.setActivo(ConstantesBD.acronimoSi);
			parametrosSuperficies.setValoracion(info.getInfoPlanTrata().getValoracion());
			parametrosSuperficies.setEvolucion(info.getInfoPlanTrata().getEvolucion());
			arrayIinfoDetalle = PlanTratamiento.obtenerHallazgosSuperficiesSeccionOtrayBocaHisConf(con, parametrosSuperficies);	
			parametrosProgServ = new DtoProgramasServiciosPlanT();
			parametrosProgServ.setActivo(ConstantesBD.acronimoSi);
			parametrosProgServ.setEstadosProgramasOservicios(new ArrayList<String>());
			parametrosProgServ.setBuscarProgramas(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(codigoInstitucion));
			parametrosProgServ.setValoracion(info.getInfoPlanTrata().getValoracion());
			parametrosProgServ.setEvolucion(info.getInfoPlanTrata().getEvolucion());
			parametrosProgServ.setCodigoTarifario(codigoTarifario);
			
			
			
			
			for(InfoDetallePlanTramiento infoDetalle: arrayIinfoDetalle)
			{
				//recorre los hallazgos y carga sus programas y servicios
				for(InfoHallazgoSuperficie hallsuper : infoDetalle.getDetalleSuperficie())
				{
					parametrosProgServ.setDetPlanTratamiento(hallsuper.getCodigoPkDetalle());
					hallsuper.setProgramasOservicios(PlanTratamiento.obtenerProgramasOServiciosHisConf(con, parametrosProgServ));					
					hallsuper.getExisteBD().setValue(ConstantesBD.acronimoSi);
					
					//Recorre los servicios
					for(InfoProgramaServicioPlan progServ:hallsuper.getProgramasOservicios())
					{
						//Actualiza la información para los permisos de eliminar
						logger.info("CodigoPK Programa "+progServ.getCodigoPkProgramaServicio().doubleValue());
						if(progServ.getCodigoPkProgramaServicio().doubleValue() > 0){
							progServ.getExisteBD().setEsEliminable(false);
							progServ.setInactivarProg(ConstantesBD.acronimoSi);
						}
						logger.info("Tamaño Lista Servicios "+progServ.getListaServicios().size());
						if(progServ.getListaServicios().size()>0)
						{
							for(InfoServicios servicios:progServ.getListaServicios()){
								servicios.getExisteBD().setEsEliminable(false);
								servicios.setInactivarServ(ConstantesBD.acronimoSi);
							}
						}
					}
					
					arrayInfoHallSuperBoca.add(hallsuper);
				}
			}
			
			
			
			
			info.getInfoPlanTrata().setSeccionHallazgosBoca(arrayInfoHallSuperBoca);			
			
		}
		logger.info("codigoEvolucion "+codigoEvolucion);
		info.getInfoPlanTrata().setImagen(PlanTratamiento.cargarOdontograma(codigoEvolucion, false).getImagen());
		info.setXmlOdontograma(getXmlPlanTratamiento(info));
		llenadoSeccionProgServCitAsig(info);
		
		
		
	}
	
	
	
	
	/**
	 * metodo de llenado de la seccion programas/servcios cita
	 * @param info
	 */
	private void llenadoSeccionProgServCitAsig(InfoOdontograma info)
	{
		int contDet=0, contDetAux=0;
		boolean bandOrdSupf = false;
		SortSuperficiesDentales sort = new SortSuperficiesDentales();
		SortPiezasDentales sortPD = new SortPiezasDentales(); 
		sort.setPatronOrdenar("superficie");
		sortPD.setPatronOrdenar("pieza");
		// atributos auxiliares para el llenado de la seccion programas/servicios cita
		//info.getInfoPlanTrata().setSeccionProgServCita(llenadoSeccionProgServCita(info));
		ArrayList<InfoDetallePlanTramiento> seccionOtrHallAux = info.getInfoPlanTrata().getSeccionProgServCitaOtro();
		info.getInfoPlanTrata().setSeccionProgServCita(removerServicioNoCita(info.getInfoPlanTrata().getSeccionProgServCita(), info.getCodigoCita(), ConstantesIntegridadDominio.acronimoDetalle, info));
		seccionOtrHallAux = removerServicioNoCita(seccionOtrHallAux, info.getCodigoCita(), ConstantesIntegridadDominio.acronimoOtro, info);
		
		
		ArrayList<InfoDetallePlanTramiento> detPlanTemporal=new ArrayList<InfoDetallePlanTramiento>();
		for(InfoDetallePlanTramiento elem: info.getInfoPlanTrata().getSeccionProgServCita())
		{
			contDetAux=0;
			Iterator<InfoDetallePlanTramiento> iteSecOtroHallAux=seccionOtrHallAux.iterator();
			//for(InfoDetallePlanTramiento elem1: seccionOtrHallAux)
			while(iteSecOtroHallAux.hasNext())
			{
				InfoDetallePlanTramiento elem1=iteSecOtroHallAux.next();
				if(elem.getPieza().getCodigo()==elem1.getPieza().getCodigo())
				{
					for(InfoHallazgoSuperficie supf: elem1.getDetalleSuperficie())
					{
						//en caso de presentar problemas de concurrencia por el acceso simultaneo a la posicion de memoria, se debe comentaria la siguiente linea, y quitar el comentario a la siguiente.
						elem.getDetalleSuperficie().add(supf);
						//info.getInfoPlanTrata().getSeccionProgServCita().get(contDet).getDetalleSuperficie().add(supf);
					}
					// se remueve el nodo despues de haber sido transpasado a la seccion programas/servicio de cita
					//seccionOtrHallAux.remove(contDetAux);
					iteSecOtroHallAux.remove();
					bandOrdSupf = true;
					break;
				}
				else
				{
					//no se puede adicionar de una, se debe adicionar a una array temporal para luego adicionarlo.
					//info.getInfoPlanTrata().getSeccionProgServCita().add(elem1);
					detPlanTemporal.add(elem1);
					
					//se debe eliminar del arraylist
					//seccionOtrHallAux.remove(contDetAux);
					iteSecOtroHallAux.remove();
				}
				contDetAux++;
			}
			// realizar el ordenamiento de las superficies de la pieza repectiva
			if(bandOrdSupf)
			{
				Collections.sort(elem.getDetalleSuperficie(), sort);
			}
			bandOrdSupf = false;
			contDet++;
		}
		
		for(InfoDetallePlanTramiento elem1: detPlanTemporal)
		{
			info.getInfoPlanTrata().getSeccionProgServCita().add(elem1);
		}
		
		
		// realizar el ordenamiento por piezas dentales
		Collections.sort(info.getInfoPlanTrata().getSeccionProgServCita(), sortPD);
		
		// se realiza el marcado de los servicios de la seccion boca que hacen parte de la cita 
		// que se esta evolucionado
		marcarProgServCitaSecBoca(info);
	}
	
	
	/**
	 * marcar los servicios de la seccion boca que no hagan parte de la cita
	 * @param info
	 */
	private void marcarProgServCitaSecBoca(InfoOdontograma info)
	{
		boolean band = false;
		for(InfoHallazgoSuperficie superf: info.getInfoPlanTrata().getSeccionHallazgosBoca()){
			for(InfoProgramaServicioPlan programa: superf.getProgramasOservicios()){
				for(InfoServicios servicio: programa.getListaServicios())
				{
					 ArrayList<BigDecimal> citasServicio;
						if( ( info.getInfoPlanTrata().getValoracion()!=null && info.getInfoPlanTrata().getValoracion().longValue()>0) ||  ( info.getInfoPlanTrata().getEvolucion()!=null && info.getInfoPlanTrata().getEvolucion().longValue()>0) )
						{
						  citasServicio = UtilidadOdontologia.consultarCitaXProgSerPlanTratHisConf(servicio.getCodigoPkProgServ(), info.getInfoPlanTrata());
						
						}else{
						   citasServicio = UtilidadOdontologia.consultarCitaXProgSerPlanTrat(servicio.getCodigoPkProgServ());	 
					     }				
					
					logger.info("\n\n Codigo Cita >> "+info.getCodigoCita());
					if(!citasServicio.contains(new BigDecimal(info.getCodigoCita())))
						servicio.setServicioParCita(ConstantesBD.acronimoNo);
					else
						band = true;
				}
				if(!band)
					programa.setProgramasParCita(ConstantesBD.acronimoNo);
				band = false;
			}
		}
	}
	
	/**
	 * metodo que remueve los servicios que no hagan parte de la cita que se esta evolucionando 
	 * tanto de la seccion detalle como la de otros
	 * @param array
	 * @param codigoCita
	 * @param seccionAplica
	 * @return
	 */
	private ArrayList<InfoDetallePlanTramiento> removerServicioNoCita(
			ArrayList<InfoDetallePlanTramiento> array, 
			int codigoCita,
			String seccionAplica,
			InfoOdontograma info)
	{
		int contDet=0, contSupf=0, contProg=0, contServ=0;
		boolean bandProg = false;
		//ArrayList<Integer> posRemover= new ArrayList<Integer>(); 
		//InfoServicios servicio = new InfoServicios();
		for(InfoDetallePlanTramiento elem: array)
		{
			contSupf=0;
			for(InfoHallazgoSuperficie superf: elem.getDetalleSuperficie())
			{
				contProg=0;
				for(InfoProgramaServicioPlan programa: superf.getProgramasOservicios())
				{
					contServ=0;
					for(InfoServicios servicio: programa.getListaServicios())
					{
						ArrayList<BigDecimal> citsaServicio;
						if( ( info.getInfoPlanTrata().getValoracion()!=null && info.getInfoPlanTrata().getValoracion().longValue()>0) ||  ( info.getInfoPlanTrata().getEvolucion()!=null && info.getInfoPlanTrata().getEvolucion().longValue()>0) )
						{
						  citsaServicio = UtilidadOdontologia.consultarCitaXProgSerPlanTratHisConf(servicio.getCodigoPkProgServ(), info.getInfoPlanTrata());
						
						}else{
						   citsaServicio = UtilidadOdontologia.consultarCitaXProgSerPlanTrat(servicio.getCodigoPkProgServ());	 
					     }
						 
						//servicio = (InfoServicios)programa.getListaServicios().get(i);
						//se remueven los servicios que no pertenecen a la cita 
						//que se esta evolucionando 
						
						for(int i=0; i<citsaServicio.size();i++)
						{
							logger.info(" - Array "+citsaServicio.get(i));
						}
						if(!citsaServicio.contains(new BigDecimal(codigoCita))){
							if(seccionAplica.equals(ConstantesIntegridadDominio.acronimoDetalle))
								info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).getListaServicios().get(contServ).setServicioParCita(ConstantesBD.acronimoNo);
							else if(seccionAplica.equals(ConstantesIntegridadDominio.acronimoOtro))
								info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).getListaServicios().get(contServ).setServicioParCita(ConstantesBD.acronimoNo);
							servicio.setServicioParCita(ConstantesBD.acronimoNo);
						}
						else{// se hace el llenado de la posiciones y de la seccion a la que aplica segun el array original
							logger.info("\n seccionAplica >>"+seccionAplica);
							if(seccionAplica.equals(ConstantesIntegridadDominio.acronimoDetalle)){
								info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).getListaServicios().get(contServ).setPosDet(contDet);
								info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).getListaServicios().get(contServ).setPosSupf(contSupf);
								info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).getListaServicios().get(contServ).setPosProg(contProg);
								info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).getListaServicios().get(contServ).setPosServ(contServ);
								info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).getListaServicios().get(contServ).setSeccionAplica(seccionAplica);
							}else if(seccionAplica.equals(ConstantesIntegridadDominio.acronimoOtro)){
								info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).getListaServicios().get(contServ).setPosDet(contDet);
								info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).getListaServicios().get(contServ).setPosSupf(contSupf);
								info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).getListaServicios().get(contServ).setPosProg(contProg);
								info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).getListaServicios().get(contServ).setPosServ(contServ);
								info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).getListaServicios().get(contServ).setSeccionAplica(seccionAplica);
							}
							servicio.setPosDet(contDet);
							servicio.setPosSupf(contSupf);
							servicio.setPosProg(contProg);
							servicio.setPosServ(contServ);
							servicio.setSeccionAplica(seccionAplica);
							//servicio.setSeccionAplica(ConstantesIntegridadDominio.acronimoDetalle+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoOtro);
							bandProg = true;
						}
						contServ++;	
					}
					
					if(bandProg){
						if(seccionAplica.equals(ConstantesIntegridadDominio.acronimoDetalle)){
							info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).setPosDet(contDet);
							info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).setPosSupf(contSupf);
							info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).setPosProg(contProg);
							info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).setSeccionAplica(seccionAplica);
						}else if(seccionAplica.equals(ConstantesIntegridadDominio.acronimoOtro)){
							info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).setPosDet(contDet);
							info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).setPosSupf(contSupf);
							info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).setPosProg(contProg);
							info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).setSeccionAplica(seccionAplica);
						}
						programa.setPosDet(contDet);
						programa.setPosSupf(contSupf);
						programa.setPosProg(contProg);
						programa.setSeccionAplica(seccionAplica);
						//programa.setSeccionAplica(ConstantesIntegridadDominio.acronimoDetalle+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoOtro);
					}else{
						if(seccionAplica.equals(ConstantesIntegridadDominio.acronimoDetalle))
							info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).setProgramasParCita(ConstantesBD.acronimoNo);
						else if(seccionAplica.equals(ConstantesIntegridadDominio.acronimoOtro))
							info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(contDet).getDetalleSuperficie().get(contSupf).getProgramasOservicios().get(contProg).setProgramasParCita(ConstantesBD.acronimoNo);
						programa.setProgramasParCita(ConstantesBD.acronimoNo);
					}
					bandProg = false;
					contProg++;
				}
				contSupf++;
			}
			contDet++;
		}
		return array;
	}
	

	/*private ArrayList<InfoDetallePlanTramiento> llenadoSeccionProgServCita(InfoOdontograma info)
	{
		ArrayList<InfoDetallePlanTramiento> seccionDetHall = new ArrayList<InfoDetallePlanTramiento>();
		InfoDetallePlanTramiento infoDet = new InfoDetallePlanTramiento();
		for(int i=0;i<info.getInfoPlanTrata().getSeccionHallazgosDetalle().size();i++)
		{
			infoDet = (InfoDetallePlanTramiento)info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(i);
			seccionDetHall.add(infoDet);
		}
		return seccionDetHall;
	}*/
	
	//************************************************************************************
	//Metodos Detalle Plan de Tratamiento
	//************************************************************************************
	
	/**
	 * Obtiene la información del plan de tratamiento en XML 
	 * */
	private String getXmlPlanTratamiento(InfoOdontograma info)
	{

		String cadena = "";
		this.arrayDientesXML = new ArrayList<InfoDetallePlanTramiento>();

		//Seccion de plan de tratamiento Detalle
		getIndicadorXmlPlanTratamientoDetalle(info);
		//Seccion de Plan de tratamiento Otros Hallazgos
		getIndicadorXmlPlanTratamientoOtrosHall(info);

		//Recorre las piezas dentales
		for(InfoDetallePlanTramiento pieza:arrayDientesXML)
		{
			String cadenaTmp = "";
			
			//En caso de no existir detalle
			if(pieza.getDetalleSuperficie().size() <= 0)
			{

				cadenaTmp+= "<superficie codigo = '"+ConstantesBD.codigoNuncaValido+"' nombre = 'diente'>"+
								 "<hallazgo>" ;

									/*
									 * Si la pieza está como inactiva es porque se debe sombrear
									 * Ojo con esas propiedades, se prestan para confusiones
									 */
									if(!pieza.getExisteBD().isActivo())
									{
										cadenaTmp+="<color>S</color>";
									}
									else
									{
										if(!pieza.getExisteBD().getValue().isEmpty())
										{	
											cadenaTmp+="<path>"+CarpetasArchivos.CONVENCION.getRuta()+""+pieza.getExisteBD().getValue()+"</path>";
										}
									} 
				
				cadenaTmp+=		 "</hallazgo>"+
							 "</superficie>";
			}
			
			//Recorre las superficies
			logger.info("numero de superficies "+pieza.getDetalleSuperficie().size());
			for(InfoHallazgoSuperficie superficie:pieza.getDetalleSuperficie())
			{
				//*******************************************************************************
				if(superficie.getSuperficieOPCIONAL().getCodigo() > 0 )
				{
					String color="";
					if(!superficie.getExisteBD().isActivo())// Si este campo no viene activo pinta de azul
					{
						color="S";
					}
					
					String path="";
					if(!superficie.getExisteBD().getValue().isEmpty())
					{
						path=CarpetasArchivos.CONVENCION.getRuta()+""+superficie.getExisteBD().getValue();
					}
					
					cadenaTmp+=
							"<superficie codigo = '"+superficie.getSuperficieOPCIONAL().getCodigo()+"' nombre = '"+superficie.getSuperficieOPCIONAL().getNombre()+"' sector = '"+superficie.getSuperficieOPCIONAL().getCodigo2()+"' >"+
								"<hallazgo>"+
							 		"<codigo>"+superficie.getHallazgoREQUERIDO().getCodigo()+"</codigo>" +
							 		"<descripcion>"+UtilidadTexto.cambiarCaracteresEspeciales(superficie.getHallazgoREQUERIDO().getNombre())+"</descripcion>" +
							 		"<path>"+path+"</path>" +
							 		"<convencion>"+superficie.getCodigoConvencion()+"</convencion>" +
									"<color>"+color+"</color>" +
								"</hallazgo>"+
							"</superficie>";
				}
				//********************************************************************************
			}
			
			cadena+= "<diente pieza = '"+pieza.getPieza().getCodigo()+"'>"+cadenaTmp+"</diente>";
		
		}
		
		if(!cadena.equals(""))
			cadena = "<contenido>"+cadena+"</contenido>";

		logger.info("valor del XML >> "+cadena);
		return cadena;
	}
	
	//************************************************************************************************
	
	
	
	
	
	/**
	 * 	ACTUALIZAR EL DETALLE DEL PLAN DE TRATAMIENTO, LOS PROGRAMAS Y SERVICIOS.
	 * @param con
	 * @param info
	 * @return
	 */
	private boolean actualizarSeccPlanTratamiento(Connection con, InfoOdontograma info)
	{ 
		PlanTratamiento planTratamiento = new PlanTratamiento();
		InfoDatosString actualizar = new InfoDatosString();
		boolean evaluarProgyServ = false;
		String estPrograma = "";
		
		Log4JManager.info("**********************************************************");
		
		Log4JManager.info("Codigo_plan->"+ info.getInfoPlanTrata().getCodigoPk());
		

	
	
	
		//********************************************************************************
		// Actualizacion Evolución Programas Servicios Detalle Plan Tratamiento
		for(InfoDetallePlanTramiento elem: info.getInfoPlanTrata().getSeccionHallazgosDetalle())//detalleSuperficio
		{
		
			for(InfoHallazgoSuperficie detallePlan: elem.getDetalleSuperficie())
			{
				
				
				/*
				 * MODIFICAR EL DETALLE DEL PLAN DE TRATAMIENTO
				 */
				actualizarPlanTratamiento(con, info, detallePlan);
				
				
				/*
				 * ITERACION DE PROGRAMAS 
				 */
				for(InfoProgramaServicioPlan progServPlanT: detallePlan.getProgramasOservicios())
				{
					if(info.getUtilizaProgOdontIns().equals(ConstantesBD.acronimoSi))
					{  
						// si maneja programas
						log(info, progServPlanT);
						
						
		
					
						
						if(!progServPlanT.getNewEstadoProg().equals("")&&!progServPlanT.getEstadoPrograma().equals(progServPlanT.getNewEstadoProg()))
						{
							
							
							actualizar = obtenerCasoActualizacion(progServPlanT.getNewEstadoProg());
							
							logger.info("casoActualizacion PROG: "+actualizar.getCodigo());
							logger.info("estado PROG: "+actualizar.getNombre());
							
							//	actualizar.getCodigo().charAt(3);
							if(!actualizar.getCodigo().equals("")&&!actualizar.getNombre().equals(""))
							{
								if(actualizar.getCodigo().charAt(0)==ConstantesBD.acronimoRegistrarGarantias)
								{
									evaluarProgyServ = true;
								}
								
								
								if(!PlanTratamiento.actualizacionEstadosPSOtrasEvoluciones(con, 
										actualizar.getCodigo().charAt(0), 
										actualizar.getNombre(), 
										ConstantesIntegridadDominio.acronimoMostrarProgramas, 
										"", 
										info.getUsuarioActual().getUsuarioModifica(), 
										detallePlan.getCodigoPkDetalle().intValue(), 
										progServPlanT.getCodigoPkProgramaServicio().intValue(), 
										ConstantesBD.codigoNuncaValido,
										Utilidades.convertirAEntero(progServPlanT.getMotivoCancelacion().getCodigo()),
										info.getCodigoCita(),
										info.getCodigoValoracion(),
										info.getCodigoEvolucion(),
										info.getPorConfirmar()))
								{	
									return false;
								
								}
								else
								{
									//** CAMBIOS por EXCLUSIONES Marzo 2010** 
									//Se actualizan los estados de los servicios en el caso de que se haya elegido los siguientes estados
									String estadoProgExcl="";
									
									if(progServPlanT.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoExcluido) ||
											progServPlanT.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoExcluido))
									  {
										 String[] arregloEstado = progServPlanT.getEstadoPrograma().split(ConstantesBD.separadorSplit); 
											
										   if(arregloEstado.length>0)
										    {
											   estadoProgExcl= arregloEstado.length>1?arregloEstado[1]:arregloEstado[0];
										    }
										   if((info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi) && estadoProgExcl.equals(ConstantesIntegridadDominio.acronimoPorAutorizar) )
													|| info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo) && estadoProgExcl.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente) )
											{
											   for(InfoServicios elem3:progServPlanT.getListaServicios())
												{
												   elem3.setNewEstado(estadoProgExcl);
												}
											}
										
									  }
								
								}
							}
						}
						
						if(!progServPlanT.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoEstadoCancelado)
								&& !progServPlanT.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoInclusion)
								&& !progServPlanT.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoExcluido))
						{
							
							if(!actualizarEvolucionServicio(con, 
									planTratamiento, 
									progServPlanT.getListaServicios(),
									info.getUsuarioActual().getUsuarioModifica(), 
									detallePlan.getCodigoPkDetalle().intValue(), 
									progServPlanT.getCodigoPkProgramaServicio().intValue(),
									info.getUtilizaProgOdontIns(),
									info.getCodigoCita(),
									info.getCodigoValoracion(),
									info.getCodigoEvolucion(),
									info.getPorConfirmar()))
								return false;
							evaluarProgyServ = false;
						}
						
					}
					else if(info.getUtilizaProgOdontIns().equals(ConstantesBD.acronimoNo))
					{// si es solo por servicios
						if(!actualizarEvolucionServicio(con, 
								planTratamiento, 
								progServPlanT.getListaServicios(),
								info.getUsuarioActual().getUsuarioModifica(), 
								detallePlan.getCodigoPkDetalle().intValue(), 
								progServPlanT.getCodigoPkProgramaServicio().intValue(),
								info.getUtilizaProgOdontIns(),
								info.getCodigoCita(),
								info.getCodigoValoracion(),
								info.getCodigoEvolucion(),
								info.getPorConfirmar()))
							return false;
					}
				}
			}
		}
		// FIN Actualizacion Evolución Programas Servicios Detalle Plan Tratamiento
		
		
			
		return true;
	}
	
	
	
	/**
	 * TODO FALTA MANEJO DE LA TRANSACCION 
	 * @param con
	 * @param info
	 * @param detallePlan
	 * @author Edgar Carvajal
	 */
	private void actualizarPlanTratamiento(Connection con, InfoOdontograma info, InfoHallazgoSuperficie detallePlan) 
	{
		DtoDetallePlanTratamiento dtoDetPlanTratamiento = new DtoDetallePlanTratamiento();
		dtoDetPlanTratamiento.setCodigo(detallePlan.getCodigoPkDetalle().doubleValue());
		dtoDetPlanTratamiento.setPlanTratamiento(info.getInfoPlanTrata().getCodigoPk().doubleValue());
		PlanTratamiento.modificarDetallePlanTratamiento(dtoDetPlanTratamiento, con);
	}

	
	/**
	 * Log
	 * @param info
	 * @param progServPlanT
	 */
	private void log(InfoOdontograma info,
			InfoProgramaServicioPlan progServPlanT) {
		logger.info("\n\n\n\n\n\n\n\n\n");
		logger.info("Estado Prog: -"+progServPlanT.getEstadoPrograma()+"-");
		logger.info("New Estado Prog: -"+progServPlanT.getNewEstadoProg()+"-");
		logger.info(" INFO por CONFIRMAR "+info.getPorConfirmar());
	}
	
	
	/**
	 * 
	 * @param piezaDental
	 * @param isSombra
	 * @param codigoSuper
	 * @param nombreSuper
	 * @param sector
	 * @param fechaUltimaModificacion
	 * @param horaUltimaModificacion
	 * @param path
	 * @param programasServicios
	 */
	private void addArrayDientesXML(
									int piezaDental,
									boolean isSombra,
									int codigoSuper,
									String nombreSuper,
									int sector,
									String fechaUltimaModificacion,
									String horaUltimaModificacion,
									String path, 
									ArrayList<InfoProgramaServicioPlan> programasServicios
									)
	{
		boolean encontroDiente = false;
		boolean encontroSuperficie = false;
		
		//Busca el diente
		for(InfoDetallePlanTramiento pieza:arrayDientesXML)
		{
			//Encontro el diente
			if(pieza.getPieza().getCodigo() == piezaDental)
			{
				encontroDiente = true;
				
				//Evalua si es Toda la Pieza o solo Superficie
				//POR DIENTE
				if(codigoSuper <= 0)
				{
					//la pieza ya posee convencion
					if(pieza.getExisteBD().isActivo() && !isSombra)
					{
						//Valida la fecha
						if(UtilidadFecha.compararFechas(fechaUltimaModificacion,horaUltimaModificacion,pieza.getFechaUltimaModProgServ(),pieza.getHoraUltimaModProgServ()).isTrue())
						{
							pieza.getExisteBD().setActivo(true);
							pieza.getExisteBD().setValue(path);
							pieza.setFechaUltimaModProgServ(fechaUltimaModificacion);
							pieza.setHoraUltimaModProgServ(fechaUltimaModificacion);
							pieza.setDetalleSuperficie(new ArrayList<InfoHallazgoSuperficie>());
						}
					}
					else if(!pieza.getExisteBD().isActivo() && !isSombra)
					{
						pieza.getExisteBD().setActivo(true);
						pieza.getExisteBD().setValue(path);
						pieza.setFechaUltimaModProgServ(fechaUltimaModificacion);
						pieza.setHoraUltimaModProgServ(fechaUltimaModificacion);
						pieza.setDetalleSuperficie(new ArrayList<InfoHallazgoSuperficie>());
					}
					else if(!pieza.getExisteBD().isActivo() && isSombra)
					{
						pieza.getExisteBD().setActivo(false);
						pieza.setDetalleSuperficie(new ArrayList<InfoHallazgoSuperficie>());
						pieza.setFechaUltimaModProgServ("");
						pieza.setHoraUltimaModProgServ("");
					}
				}//POR SUPERFICIE
				else
				{
					encontroSuperficie =  false;
					boolean quitarConvDiente = false;
					
					//diente ya posee convencion
					if(pieza.getExisteBD().isActivo())
					{
						//Valida la fecha
						if(UtilidadFecha.compararFechas(fechaUltimaModificacion,horaUltimaModificacion,pieza.getFechaUltimaModProgServ(),pieza.getHoraUltimaModProgServ()).isTrue())
						{
							quitarConvDiente = true;
						}
					}
					else
					{
						quitarConvDiente = true;
					}
					
					//Busco las Superficies
					for(InfoHallazgoSuperficie superficie:pieza.getDetalleSuperficie())
					{
						if(superficie.getSuperficieOPCIONAL().getCodigo() == codigoSuper)
						{
							encontroSuperficie = true;
							
							if(superficie.getExisteBD().isActivo() && quitarConvDiente)
							{
								//Valida la fecha
								if(UtilidadFecha.compararFechas(fechaUltimaModificacion,horaUltimaModificacion,superficie.getFechaUltimaModProgServ(),superficie.getHoraUltimaModProgServ()).isTrue())
								{
									superficie.getExisteBD().setActivo(true);
									superficie.getExisteBD().setValue(path);
									superficie.setFechaUltimaModProgServ(fechaUltimaModificacion);
									superficie.setHoraUltimaModProgServ(fechaUltimaModificacion);
								}
							}
							else if(!superficie.getExisteBD().isActivo() && quitarConvDiente && !isSombra)
							{
								superficie.getExisteBD().setActivo(true);
								superficie.getExisteBD().setValue(path);
								superficie.setFechaUltimaModProgServ(fechaUltimaModificacion);
								superficie.setHoraUltimaModProgServ(fechaUltimaModificacion);
							}
							if(!superficie.getExisteBD().isActivo() && quitarConvDiente && isSombra)
							{
								superficie.getExisteBD().setActivo(false);
								superficie.setFechaUltimaModProgServ("");
								superficie.setHoraUltimaModProgServ("");
							}
						}
					}
					
					if(!encontroSuperficie)
					{
						InfoHallazgoSuperficie nuevaSuper = new InfoHallazgoSuperficie();
						
						if(isSombra)
						{
							nuevaSuper.getExisteBD().setActivo(false);
						}
						else
						{
							nuevaSuper.getExisteBD().setActivo(true);
							nuevaSuper.getExisteBD().setValue(path);
						}
						
						nuevaSuper.setFechaUltimaModProgServ(fechaUltimaModificacion);
						nuevaSuper.setHoraUltimaModProgServ(fechaUltimaModificacion);
						nuevaSuper.getSuperficieOPCIONAL().setCodigo(codigoSuper);
						nuevaSuper.getSuperficieOPCIONAL().setNombre(nombreSuper);
						nuevaSuper.getSuperficieOPCIONAL().setCodigo2(sector);
						nuevaSuper.setProgramasOservicios(programasServicios);
						
						
						pieza.getDetalleSuperficie().add(nuevaSuper);
					}
				}
			}
		}

		//Si no encontron el diente lo añade
		if(!encontroDiente)
		{
			InfoDetallePlanTramiento nuevaPieza = new InfoDetallePlanTramiento();
			nuevaPieza.getPieza().setCodigo(piezaDental);
			
			if(isSombra)
			{
				nuevaPieza.getExisteBD().setActivo(false);
			}
			else
			{
				nuevaPieza.getExisteBD().setActivo(true);
				nuevaPieza.getExisteBD().setValue(path);
			}
			
			nuevaPieza.setFechaUltimaModProgServ(fechaUltimaModificacion);
			nuevaPieza.setHoraUltimaModProgServ(fechaUltimaModificacion);
			
			if(codigoSuper > 0)
			{
				InfoHallazgoSuperficie nuevaSuper = new InfoHallazgoSuperficie();
				
				if(isSombra)
				{
					nuevaSuper.getExisteBD().setActivo(false);
				}
				else
				{
					nuevaSuper.getExisteBD().setActivo(true);
					nuevaSuper.getExisteBD().setValue(path);
				}
				
				nuevaSuper.setFechaUltimaModProgServ(fechaUltimaModificacion);
				nuevaSuper.setHoraUltimaModProgServ(fechaUltimaModificacion);
				nuevaSuper.getSuperficieOPCIONAL().setCodigo(codigoSuper);
				nuevaSuper.getSuperficieOPCIONAL().setNombre(nombreSuper);
				nuevaSuper.getSuperficieOPCIONAL().setCodigo2(sector);
				nuevaSuper.setProgramasOservicios(programasServicios);
				nuevaPieza.getDetalleSuperficie().add(nuevaSuper);
			}
			nuevaPieza.setFechaUltimaModProgServ(fechaUltimaModificacion);
			nuevaPieza.setHoraUltimaModProgServ(fechaUltimaModificacion);
						
			this.arrayDientesXML.add(nuevaPieza);
		}

	}
	
	//**************************************************************************************************************
	
	/**
	 * Obtiene la información del plan de tratamiento en XML
	 * @param InfoOdontograma info
	 * */
	private void getIndicadorXmlPlanTratamientoOtrosHall(InfoOdontograma info)
	{
		boolean isSombra = false;
		String convencion = "";
		int codigoSuper = ConstantesBD.codigoNuncaValido;
		String nombreSuper = "";
		int sector = ConstantesBD.codigoNuncaValido;
		String fechaUltimaModificacion = "";
		String horaUltimaModificacion = "";
		ArrayList<InfoProgramaServicioPlan> programaServicio=new ArrayList<InfoProgramaServicioPlan>();

		//Recorre las piezas dentales
		for(InfoDetallePlanTramiento pieza:info.getInfoPlanTrata().getSeccionOtrosHallazgos())
		{
			isSombra = false;
			codigoSuper = ConstantesBD.codigoNuncaValido;
			nombreSuper = "";
			sector = ConstantesBD.codigoNuncaValido;
			fechaUltimaModificacion = "";
			horaUltimaModificacion = "";
			String path="";
			
			if(pieza.getExisteBD().isActivo())
			{
				boolean hayAlgoEnDiente = false;
				
				//Recorre las superficies
				for(InfoHallazgoSuperficie superficie:pieza.getDetalleSuperficie())
				{
					//Busca el ultimo programa que se ha terminado
					InfoDatosInt ultiProTer = superficie.getPosUltimoProgramaOServicioTerminado();
					
					//Evalua que este el codigo y que No este por confirmar
					if(superficie.esAlgunProgramaOServicioEnProceso())
					{
						isSombra = true;
						hayAlgoEnDiente = true;
						//complemento = "	<color>"+this.colorDeEnProceso+"</color>" +
						//			  "	<path></path>";
					}
					else if(ultiProTer.getCodigo() > 0 && !ultiProTer.getActivo())
					{
						//Consulta la información de la convención a dibujar
						isSombra = false;
						convencion = ultiProTer.getDescripcion();
						hayAlgoEnDiente = true;
						//complemento = "	<color></color>"+
						 //			  "	<path>"+ultiProTer.getDescripcion()+"</path>";
					} 
					
					//*******************************************************************************
				
					if(hayAlgoEnDiente)
					{	
						if(superficie.getSuperficieOPCIONAL().getCodigo() > 0 
								&& superficie.getSuperficieOPCIONAL().getActivo())
						{
							sector = superficie.getSuperficieOPCIONAL().getCodigo2();
							codigoSuper = superficie.getSuperficieOPCIONAL().getCodigo();
							nombreSuper = superficie.getSuperficieOPCIONAL().getNombre();
							path = superficie.getHallazgoREQUERIDO().getDescripcion();
							programaServicio = superficie.getProgramasOservicios();
							
						}
						else
						{
							codigoSuper = ConstantesBD.codigoNuncaValido;
						}
						
						addArrayDientesXML(
								pieza.getPieza().getCodigo(),
								isSombra, 
								codigoSuper, 
								nombreSuper, 
								sector, 
								fechaUltimaModificacion,
								horaUltimaModificacion,
								path,
								programaServicio
								);
						
					}
					//********************************************************************************
				}
			}
		}
	}
	
	//**************************************************************************************************************
	
	/**
	 * Obtiene la información del plan de tratamiento en XML
	 * @param InfoOdontograma info
	 * */
	private void getIndicadorXmlPlanTratamientoDetalle(InfoOdontograma info)
	{
		boolean isSombra = false;
		int codigoSuper = ConstantesBD.codigoNuncaValido;
		String nombreSuper = "";
		int sector = ConstantesBD.codigoNuncaValido;
		String fechaUltimaModificacion = "";
		String horaUltimaModificacion = "";
		String path="";
		ArrayList<InfoProgramaServicioPlan> programasServicios=new ArrayList<InfoProgramaServicioPlan>();

		//Recorre las piezas dentales
		for(InfoDetallePlanTramiento pieza:info.getInfoPlanTrata().getSeccionHallazgosDetalle())
		{
			isSombra = false;
			path = "";
			codigoSuper = ConstantesBD.codigoNuncaValido;
			nombreSuper = "";
			sector = ConstantesBD.codigoNuncaValido;
			fechaUltimaModificacion = "";
			horaUltimaModificacion = "";
			
			if(pieza.getExisteBD().isActivo())
			{
				boolean hayAlgoEnDiente = false;
				
				//Recorre las superficies
				for(InfoHallazgoSuperficie superficie:pieza.getDetalleSuperficie())
				{
					//Busca el ultimo programa que se ha terminado
					InfoDatosInt ultiProTer = superficie.getPosUltimoProgramaOServicioTerminado();
					
					if(!superficie.getSuperficieTerminada())
					{
						isSombra = true;
						hayAlgoEnDiente = true;
					}
					else if(ultiProTer.getCodigo() >= 0 && !ultiProTer.getActivo())
					{
						//Consulta la información de la convención a dibujar
						isSombra = false;
						path = ultiProTer.getDescripcion();
						hayAlgoEnDiente = true;
					}
					
					//*******************************************************************************
				
					if(hayAlgoEnDiente)
					{	
						if(superficie.getSuperficieOPCIONAL().getCodigo() > 0 
								&& superficie.getSuperficieOPCIONAL().getActivo())
						{
							sector = superficie.getSuperficieOPCIONAL().getCodigo2();
							codigoSuper = superficie.getSuperficieOPCIONAL().getCodigo();
							nombreSuper = superficie.getSuperficieOPCIONAL().getNombre();
							programasServicios = superficie.getProgramasOservicios();
						}
						else
						{
							codigoSuper = ConstantesBD.codigoNuncaValido;
						}
						
						addArrayDientesXML(
								pieza.getPieza().getCodigo(),
								isSombra, 
								codigoSuper, 
								nombreSuper, 
								sector, 
								fechaUltimaModificacion,
								horaUltimaModificacion,
								path,
								programasServicios);
						
					}
					//********************************************************************************
				}
			}
		}
	}
	
	//************************************************************************************
	//Otras Evoluciones
	//************************************************************************************
	
	private boolean actualizarSeccEvolPiezasDentales(Connection con, InfoOdontograma info)
	{
		PlanTratamiento planTratamiento = new PlanTratamiento();
		InfoDatosString actualizar = new InfoDatosString();
		boolean evaluarProgyServ = false;
		//********************************************************************************
		// Actualizacion Evolución Programas Servicios OTROS HALLAZGOS
		for(InfoDetallePlanTramiento elem: info.getInfoPlanTrata().getSeccionOtrosHallazgos())
		{
			for(InfoHallazgoSuperficie detallePlan: elem.getDetalleSuperficie())
			{
				
				/*
				 *MODIFICAR EL DETALLE PLAN DE TRATAMIENTO
				 *NOTA.
				 *SE MODIFICA PORQUE SE TIEN QUE LLEVAR EL REGISTRO DEL LOG DEL DETALLE DEL PLAN  
				 */
				actualizarPlanTratamiento(con, info, detallePlan);
				
				
				
				for(InfoProgramaServicioPlan progServPlanT: detallePlan.getProgramasOservicios())
				{
					if(info.getUtilizaProgOdontIns().equals(ConstantesBD.acronimoSi))
					{   // si maneja programas
						if(!progServPlanT.getNewEstadoProg().equals("")&&!progServPlanT.getEstadoPrograma().equals(progServPlanT.getNewEstadoProg()))
						{
							actualizar = obtenerCasoActualizacion(progServPlanT.getNewEstadoProg());
							//logger.info("casoActualizacion PROG: "+actualizar.getCodigo());
							//logger.info("estado PROG: "+actualizar.getNombre());
							if(!actualizar.getCodigo().equals("")&&!actualizar.getNombre().equals(""))
							{
								if(actualizar.getCodigo().charAt(0)==ConstantesBD.acronimoRegistrarGarantias)
									evaluarProgyServ = true;
								if(!PlanTratamiento.actualizacionEstadosPSOtrasEvoluciones(con, 
										actualizar.getCodigo().charAt(0), 
										actualizar.getNombre(), 
										ConstantesIntegridadDominio.acronimoMostrarProgramas, 
										"", 
										info.getUsuarioActual().getUsuarioModifica(), 
										detallePlan.getCodigoPkDetalle().intValue(), 
										progServPlanT.getCodigoPkProgramaServicio().intValue(), 
										ConstantesBD.codigoNuncaValido,
										Utilidades.convertirAEntero(progServPlanT.getMotivoCancelacion().getCodigo()),
										info.getCodigoCita(),
										info.getCodigoValoracion(),
										info.getCodigoEvolucion(),
										info.getPorConfirmar()))
									return false;
							}
						}
						
						//if(evaluarProgyServ)
						if(!progServPlanT.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoEstadoCancelado)
								&& !progServPlanT.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoInclusion)
								&& !progServPlanT.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoExcluido))
						{
							if(!actualizarEvolucionServicio(con, 
									planTratamiento, 
									progServPlanT.getListaServicios(),
									info.getUsuarioActual().getUsuarioModifica(), 
									detallePlan.getCodigoPkDetalle().intValue(), 
									progServPlanT.getCodigoPkProgramaServicio().intValue(),
									info.getUtilizaProgOdontIns(),
									info.getCodigoCita(),
									info.getCodigoValoracion(),
									info.getCodigoEvolucion(),
									info.getPorConfirmar())) 
								return false;
							evaluarProgyServ = false;
						}
						else
							if(!progServPlanT.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoEstadoCancelado)
									&& (progServPlanT.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoInclusion)
									|| progServPlanT.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoExcluido)))
							{
								//logger.info("Entra a evolucionar el servicio especifico");
								if(!actualizarEvolucionServicio(con, 
										planTratamiento, 
										progServPlanT.getListaServicios(),
										info.getUsuarioActual().getUsuarioModifica(),
										detallePlan.getCodigoPkDetalle().intValue(),
										progServPlanT.getCodigoPkProgramaServicio().intValue(),
										info.getUtilizaProgOdontIns(),
										info.getCodigoCita(),
										info.getCodigoValoracion(),
										info.getCodigoEvolucion(),
										info.getPorConfirmar()))
									return false;
								evaluarProgyServ = false;
							}
						
					}else if(info.getUtilizaProgOdontIns().equals(ConstantesBD.acronimoNo))
					{// si es solo por servicios
						if(!actualizarEvolucionServicio(con, 
								planTratamiento, 
								progServPlanT.getListaServicios(),
								info.getUsuarioActual().getUsuarioModifica(), 
								detallePlan.getCodigoPkDetalle().intValue(), 
								progServPlanT.getCodigoPkProgramaServicio().intValue(),
								info.getUtilizaProgOdontIns(),
								info.getCodigoCita(),
								info.getCodigoValoracion(),
								info.getCodigoEvolucion(),
								info.getPorConfirmar()))
							return false;
					}
				}
			}
		}
		//logger.info("\n\n\n\n\n\n\n\n\n");
		// FIN Actualizacion Evolución Programas Servicios OTROS HALLAZGOS
		//********************************************************************************
		return true;
	}
	
	/**
	 * metodo de actualizacion evolucion del servicio
	 * @param con
	 * @param planTratamiento
	 * @param servicios
	 * @param usuario
	 * @param codigoPkDetallePlanT
	 * @param codigoProgrma
	 * @param porConfirmar 
	 * @return
	 */
	private boolean actualizarEvolucionServicio(Connection con,
			PlanTratamiento planTratamiento,
			ArrayList<InfoServicios> servicios, 
			String usuario, 
			int codigoPkDetallePlanT,
			int codigoProgrma,
			String utilizaProgOdontIns,
			int codigoCita,
			int valoracion,
			int evolucion, String porConfirmar)
	{
		boolean result = true;
		InfoDatosString actualizar = new InfoDatosString();
		logger.info("\n\n >>>>>>>>  ENTRO Para Actualizar ");
		if(servicios.size()>0)
		{
			for(InfoServicios elem: servicios)
			{
				logger.info("Estado Serv: -"+elem.getEstadoServicio()+"-");
				logger.info("New Estado Serv: -"+elem.getNewEstado()+"-");
				if(!elem.getNewEstado().equals("")&&!elem.getEstadoServicio().equals(elem.getNewEstado()))
				{
					actualizar = obtenerCasoActualizacion(elem.getNewEstado());
					//logger.info("casoActualizacion SERVICIO: "+actualizar.getCodigo());
					//logger.info("estado SERVICIO: "+actualizar.getNombre());
					if(!actualizar.getCodigo().equals("")&&!actualizar.getNombre().equals(""))
					{
						if(utilizaProgOdontIns.equals(ConstantesBD.acronimoNo)&&actualizar.getNombre().equals(ConstantesIntegridadDominio.acronimoGarantia))
							actualizar.setNombre(ConstantesIntegridadDominio.acronimoEstadoPendiente);
						else if(utilizaProgOdontIns.equals(ConstantesBD.acronimoSi)&&actualizar.getNombre().equals(ConstantesIntegridadDominio.acronimoGarantia))
							actualizar.setNombre(ConstantesIntegridadDominio.acronimoPorAutorizar);
						
						if(!PlanTratamiento.actualizacionEstadosPSOtrasEvoluciones(con, 
								actualizar.getCodigo().charAt(0),
								actualizar.getNombre(), 
								ConstantesIntegridadDominio.acronimoMostrarServicios, 
								"", 
								usuario, 
								codigoPkDetallePlanT, 
								codigoProgrma, 
								elem.getServicio().getCodigo(),
								Utilidades.convertirAEntero(elem.getMotivoCancelacion().getCodigo()),
								codigoCita,
								valoracion,
								evolucion,
								porConfirmar
								))
							return false;
					}
				}
				
				if(elem.getPorConfirmar().equals(ConstantesBD.acronimoSi) && elem.getExisteBD().isActivo() && (elem.getExisteBD().isEsEliminable() || !elem.getExisteBD().getValue().equals(ConstantesBD.acronimoSi)))
				{
					logger.info("Nuevo Hallazgo ..!!!" );
					logger.info("Estado Serv: -"+elem.getEstadoServicio()+"-");
					logger.info("New Estado Serv: -"+elem.getNewEstado()+"-");
					actualizar = obtenerCasoActualizacion(elem.getEstadoServicio());
				   	if(!planTratamiento.actualizacionEstadosPSOtrasEvoluciones(con, 
							actualizar.getCodigo().charAt(0),
							actualizar.getNombre(), 
							ConstantesIntegridadDominio.acronimoMostrarServicios, 
							"", 
							usuario, 
							codigoPkDetallePlanT, 
							codigoProgrma, 
							elem.getServicio().getCodigo(),
							Utilidades.convertirAEntero(elem.getMotivoCancelacion().getCodigo()),
							codigoCita,
							valoracion,
							evolucion,
							porConfirmar
							))
				   		
						return false;
					
				}
				
			}
		}
		return result;
	}
	
	/**
	 * metodo de cargado de lo los posibles estados de evolucion del programa servicio
	 * @param InfoOdontograma info
	 */
	private void cargarPosiblesEstadosProgServ(InfoOdontograma info)
	{
		//********************************************************************
		// llenado posibles estados hallazgos detalle
		boolean bandBloqueoProg = false;
		for(InfoDetallePlanTramiento elem: info.getInfoPlanTrata().getSeccionHallazgosDetalle())
		{
			for(InfoHallazgoSuperficie detalleHallazgo: elem.getDetalleSuperficie())
			{
				for(InfoProgramaServicioPlan programa: detalleHallazgo.getProgramasOservicios())
				{
					if(info.getUtilizaProgOdontIns().equals(ConstantesBD.acronimoSi))
					{
						// se adicionan los posibles estados de evolucion del programa
						programa.setArrayPosEstProg(llenadoPosiblesEstados("",
									programa.getListaServicios(),
									"", 
									info.getUtilizaProgOdontIns(), 
									info.getValidaPresuOdontCont(),
									info.getInstRegistraAtenExt(),
									programa.getEstadoPrograma(),
									info.getCodigoCita(),
									ConstantesIntegridadDominio.acronimoMostrarProgramas,
									detalleHallazgo.getCodigoPkDetalle().intValue(),
									programa.getCodigoPkProgramaServicio().intValue(),
									ConstantesBD.codigoNuncaValido,
									programa.getPorConfirmar(),
									programa.getInclusion(),
									programa.getGarantia()));
					}
					
					// se inhabilitan los servicios
					inactivarServiciosProg(programa.getListaServicios(), habilitarEvolucionServicioProg(programa.getListaServicios(), info.getCodigoCita()));
					
					if(programa.getListaServicios().size()>0)
					{
						for(InfoServicios servicio: programa.getListaServicios())
						{
							//verificar la cita de atencion con la cita del servicio al que corresponde
							if(!programa.getEstadoPrograma().equals(ConstantesIntegridadDominio.acronimoEstadoCancelado)
								&& servicio.getInactivarServ().equals(ConstantesBD.acronimoNo))
							{
								if(info.getCodigoCita()==servicio.getCodigoCita().intValue())
								{
									//&& ((servicio.getPorConfirmar().equals(ConstantesBD.acronimoSi)) || (servicio.getPorConfirmar().equals(ConstantesBD.acronimoNo)&&servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoRealizadoInterno)))
									servicio.setInactivarServ(ConstantesBD.acronimoNo);
									if(servicio.getPorConfirmar().equals(ConstantesBD.acronimoSi))										
									{
										servicio.setMarcarServ(ConstantesBD.acronimoSi);
										logger.info("\n\n\n >>>>>>>>> POR CONFIRMAR EN SII 1>>>>>>>>>");
									}
								}else
								{
									if(info.getCodigoCita()!=servicio.getCodigoCita().intValue())
									{
										if(servicio.getPorConfirmar().equals(ConstantesBD.acronimoNo))										
											servicio.setInactivarServ(ConstantesBD.acronimoNo);			
											
										else if(servicio.getPorConfirmar().equals(ConstantesBD.acronimoSi))
										{
											if(programa.getEstadoPrograma().equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoInclusion))
											{
												servicio.setInactivarServ(ConstantesBD.acronimoSi);
											}
											else
											{
												servicio.setInactivarServ(ConstantesBD.acronimoNo);//CAMBIOOOO Mayo 23 2010 (estaba en Si)
												servicio.setMarcarServ(ConstantesBD.acronimoSi);
											}
											
										}
									}else{
										servicio.setInactivarServ(ConstantesBD.acronimoNo);
									}
								}
							}else
							{
								servicio.setInactivarServ(ConstantesBD.acronimoSi);
							}
							
							if(servicio.getInactivarServ().equals(ConstantesBD.acronimoNo))
							{
								// antes de adicionar los posibles estados evaluar el porconfirmar del 
								// servicios para determinar si se puede hacer un reversion de la evolucion
								// previamente realizada.
								/**if(servicio.getPorConfirmar().equals(ConstantesBD.acronimoSi)
									&&((!servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoContratado) && info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi)) 
									||(!servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente) && info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo))))
								{
									String estadoLogProgServ = info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi)?ConstantesIntegridadDominio.acronimoContratado:ConstantesIntegridadDominio.acronimoEstadoPendiente;
									// se adicionan los posibles estados de evolucion del programa
									servicio.setArrayPosEstServ(llenadoPosiblesEstados(estadoLogProgServ,
											elem2.getListaServicios(),
											"", 
											info.getUtilizaProgOdontIns()/*ConstantesBD.acronimoNo*, 
											info.getValidaPresuOdontCont(),
											info.getInstRegistraAtenExt(),
											elem2.getEstadoPrograma(),
											info.getCodigoCita(),
											ConstantesIntegridadDominio.acronimoMostrarServicios,
											elem1.getCodigoPkDetalle().intValue(),
											elem2.getCodigoPkProgramaServicio().intValue(),
											servicio.getServicio().getCodigo(),
											servicio.getPorConfirmar(),
											servicio.getInclusion(),
											servicio.getGarantia()));
								}else{*/
									// se adicionan los posibles estados de evolucion del programa
									servicio.setArrayPosEstServ(llenadoPosiblesEstados(servicio.getEstadoServicio(),
											programa.getListaServicios(),
											"", 
											info.getUtilizaProgOdontIns()/*ConstantesBD.acronimoNo*/, 
											info.getValidaPresuOdontCont(),
											info.getInstRegistraAtenExt(),
											programa.getEstadoPrograma(),
											info.getCodigoCita(),
											ConstantesIntegridadDominio.acronimoMostrarServicios,
											detalleHallazgo.getCodigoPkDetalle().intValue(),
											programa.getCodigoPkProgramaServicio().intValue(),
											servicio.getServicio().getCodigo(),
											servicio.getPorConfirmar(),
											servicio.getInclusion(),
											servicio.getGarantia()));
								//}
								
								//servicio.g
								
								//se verifica si se pueden o no habilitar para la proxima cita
								if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi)
										&& servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoContratado))
								{
									servicio.setHabilitarServ(ConstantesBD.acronimoSi);
								}else if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo)
										&& servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
								{
									servicio.setHabilitarServ(ConstantesBD.acronimoSi);
								}else{
									servicio.setHabilitarServ(ConstantesBD.acronimoNo);
								}
							}else
							{
								bandBloqueoProg = true;
							}
						}
					}
					if(bandBloqueoProg)
					{
						programa.setInactivarProg(ConstantesBD.acronimoSi);
					}
					bandBloqueoProg = false;
				}
			}
		}
		//********************************************************************
		
	
		//********************************************************************
		// llenado posibles estados otros hallazgos
		bandBloqueoProg = false;
		for(InfoDetallePlanTramiento elem: info.getInfoPlanTrata().getSeccionOtrosHallazgos())
		{
			for(InfoHallazgoSuperficie elem1: elem.getDetalleSuperficie())
			{
				for(InfoProgramaServicioPlan elem2: elem1.getProgramasOservicios())
				{
					if(info.getUtilizaProgOdontIns().equals(ConstantesBD.acronimoSi))
					{
						// se adicionan los posibles estados de evolucion del programa
						elem2.setArrayPosEstProg(llenadoPosiblesEstados("",
									elem2.getListaServicios(),
									"", 
									info.getUtilizaProgOdontIns(), 
									info.getValidaPresuOdontCont(),
									info.getInstRegistraAtenExt(),
									elem2.getEstadoPrograma(),
									info.getCodigoCita(),
									ConstantesIntegridadDominio.acronimoMostrarProgramas,
									elem1.getCodigoPkDetalle().intValue(),
									elem2.getCodigoPkProgramaServicio().intValue(),
									ConstantesBD.codigoNuncaValido,
									elem2.getPorConfirmar(),
									elem2.getInclusion(),
									elem2.getGarantia()));
					}
					
					// se inhabilitan los servicios
					inactivarServiciosProg(elem2.getListaServicios(), habilitarEvolucionServicioProg(elem2.getListaServicios(), info.getCodigoCita()));
					
					if(elem2.getListaServicios().size()>0)
					{
						for(InfoServicios servicio: elem2.getListaServicios())
						{
							logger.info("\n\n\n\n\n\n\n\n\n\n\n");
							logger.info("Servicio Por COnfirmar "+servicio.getPorConfirmar());
						   /*logger.info("Codigo Cita: "+info.getCodigoCita());
							logger.info("Codigo Cita Serv: "+servicio.getCodigoCita().intValue());
							logger.info("\n\n\n\n\n\n\n\n\n\n\n");*/
							//verificar la cita de atencion con la cita del servicio al que corresponde
							if(!elem2.getEstadoPrograma().equals(ConstantesIntegridadDominio.acronimoEstadoCancelado)
								&& servicio.getInactivarServ().equals(ConstantesBD.acronimoNo))
							{
								if(info.getCodigoCita()==servicio.getCodigoCita().intValue())
								{
									//&& ((servicio.getPorConfirmar().equals(ConstantesBD.acronimoSi)) || (servicio.getPorConfirmar().equals(ConstantesBD.acronimoNo)&&servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoRealizadoInterno)))
									servicio.setInactivarServ(ConstantesBD.acronimoNo);
									if(servicio.getPorConfirmar().equals(ConstantesBD.acronimoSi))
										servicio.setMarcarServ(ConstantesBD.acronimoSi);
								}else
								{
									if(info.getCodigoCita()!=servicio.getCodigoCita().intValue())
									{
										if(servicio.getPorConfirmar().equals(ConstantesBD.acronimoNo))
											servicio.setInactivarServ(ConstantesBD.acronimoNo);
										else if(servicio.getPorConfirmar().equals(ConstantesBD.acronimoSi))
										{
											logger.info("\n\n\n >>>>>>>>> POR CONFIRMAR EN SII 1>>>>>>>>> CodigoCita "+ info.getCodigoCita()+ " >> codcitaServ "+servicio.getCodigoCita());
											servicio.setInactivarServ(ConstantesBD.acronimoNo);//CAMBIO Mayo 23 2010 (estaba en Si)
											servicio.setMarcarServ(ConstantesBD.acronimoSi);
										}
									}else{
										servicio.setInactivarServ(ConstantesBD.acronimoNo);
									}
								}
							}else
							{logger.info("\n\n\n >>>>>>>>> POR CONFIRMAR EN SII 2222 >>>>>>>>> ");
								servicio.setInactivarServ(ConstantesBD.acronimoSi);
							}
							if(servicio.getInactivarServ().equals(ConstantesBD.acronimoNo))
							{
								// antes de adicionar los posibles estados evaluar el porconfirmar del 
								// servicios para determinar si se puede hacer un reversion de la evolucion
								// previamente realizada.
								/**if(servicio.getPorConfirmar().equals(ConstantesBD.acronimoSi)
									&&((!servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoContratado) && info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi)) 
									||(!servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente) && info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo))))
								{
									String estadoLogProgServ = info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi)?ConstantesIntegridadDominio.acronimoContratado:ConstantesIntegridadDominio.acronimoEstadoPendiente;
									// se adicionan los posibles estados de evolucion del programa
									servicio.setArrayPosEstServ(llenadoPosiblesEstados(estadoLogProgServ,
											elem2.getListaServicios(),
											"", 
											info.getUtilizaProgOdontIns()/*ConstantesBD.acronimoNo*, 
											info.getValidaPresuOdontCont(),
											info.getInstRegistraAtenExt(),
											elem2.getEstadoPrograma(),
											info.getCodigoCita(),
											ConstantesIntegridadDominio.acronimoMostrarServicios,
											elem1.getCodigoPkDetalle().intValue(),
											elem2.getCodigoPkProgramaServicio().intValue(),
											servicio.getServicio().getCodigo(),
											servicio.getPorConfirmar(),
											servicio.getInclusion(),
											servicio.getGarantia()));
								}else{*/
									// se adicionan los posibles estados de evolucion del programa
									servicio.setArrayPosEstServ(llenadoPosiblesEstados(servicio.getEstadoServicio(),
											elem2.getListaServicios(),
											"", 
											info.getUtilizaProgOdontIns()/*ConstantesBD.acronimoNo*/, 
											info.getValidaPresuOdontCont(),
											info.getInstRegistraAtenExt(),
											elem2.getEstadoPrograma(),
											info.getCodigoCita(),
											ConstantesIntegridadDominio.acronimoMostrarServicios,
											elem1.getCodigoPkDetalle().intValue(),
											elem2.getCodigoPkProgramaServicio().intValue(),
											servicio.getServicio().getCodigo(),
											servicio.getPorConfirmar(),
											servicio.getInclusion(),
											servicio.getGarantia()));
								//}
								
								//se verifica si se pueden o no habilitar para la proxima cita
								if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi)
										&& servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoContratado))
								{
									servicio.setHabilitarServ(ConstantesBD.acronimoSi);
								}else if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo)
										&& servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
								{
									servicio.setHabilitarServ(ConstantesBD.acronimoSi);
								}else{
									servicio.setHabilitarServ(ConstantesBD.acronimoNo);
								}
						}else
								bandBloqueoProg = true;
						}
					}
					if(bandBloqueoProg)
						elem2.setInactivarProg(ConstantesBD.acronimoSi);
					bandBloqueoProg = false;
				}
			}
		}
		//********************************************************************
		
		//********************************************************************
		// llenado posibles estados hallazgos boca
		bandBloqueoProg = false;
		for(InfoHallazgoSuperficie elem1: info.getInfoPlanTrata().getSeccionHallazgosBoca())
		{
			for(InfoProgramaServicioPlan elem2: elem1.getProgramasOservicios())
			{
				if(info.getUtilizaProgOdontIns().equals(ConstantesBD.acronimoSi))
				{
					// se adicionan los posibles estados de evolucion del programa
					elem2.setArrayPosEstProg(llenadoPosiblesEstados("",
								elem2.getListaServicios(),
								"", 
								info.getUtilizaProgOdontIns(), 
								info.getValidaPresuOdontCont(),
								info.getInstRegistraAtenExt(),
								elem2.getEstadoPrograma(),
								info.getCodigoCita(),
								ConstantesIntegridadDominio.acronimoMostrarProgramas,
								elem1.getCodigoPkDetalle().intValue(),
								elem2.getCodigoPkProgramaServicio().intValue(),
								ConstantesBD.codigoNuncaValido,
								elem2.getPorConfirmar(),
								elem2.getInclusion(),
								elem2.getGarantia()));
				}
				
				// se inhabilitan los servicios
				inactivarServiciosProg(elem2.getListaServicios(), habilitarEvolucionServicioProg(elem2.getListaServicios(), info.getCodigoCita()));
				
				if(elem2.getListaServicios().size()>0)
				{
					for(InfoServicios servicio: elem2.getListaServicios())
					{
						//verificar la cita de atencion con la cita del servicio al que corresponde
						if(!elem2.getEstadoPrograma().equals(ConstantesIntegridadDominio.acronimoEstadoCancelado)
							&& servicio.getInactivarServ().equals(ConstantesBD.acronimoNo))
						{
							if(info.getCodigoCita()==servicio.getCodigoCita().intValue())
							{
								//&& ((servicio.getPorConfirmar().equals(ConstantesBD.acronimoSi)) || (servicio.getPorConfirmar().equals(ConstantesBD.acronimoNo)&&servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoRealizadoInterno)))
								servicio.setInactivarServ(ConstantesBD.acronimoNo);
								if(servicio.getPorConfirmar().equals(ConstantesBD.acronimoSi))
									servicio.setMarcarServ(ConstantesBD.acronimoSi);
							}else
							{
								if(info.getCodigoCita()!=servicio.getCodigoCita().intValue())
								{
									if(servicio.getPorConfirmar().equals(ConstantesBD.acronimoNo))
										servicio.setInactivarServ(ConstantesBD.acronimoNo);
									else if(servicio.getPorConfirmar().equals(ConstantesBD.acronimoSi))
									{
										servicio.setInactivarServ(ConstantesBD.acronimoNo);//CAMBIOOOO Mayo 23 2010 (estaba en Si)
										servicio.setMarcarServ(ConstantesBD.acronimoSi);
									}
								}else{
									servicio.setInactivarServ(ConstantesBD.acronimoNo);
								}
							}
						}else
							servicio.setInactivarServ(ConstantesBD.acronimoSi);
						
						if(servicio.getInactivarServ().equals(ConstantesBD.acronimoNo))
						{
							// antes de adicionar los posibles estados evaluar el porconfirmar del 
							// servicios para determinar si se puede hacer un reversion de la evolucion
							// previamente realizada.
							/**if(servicio.getPorConfirmar().equals(ConstantesBD.acronimoSi)
								&&((!servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoContratado) && info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi)) 
								||(!servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente) && info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo))))
							{
								String estadoLogProgServ = info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi)?ConstantesIntegridadDominio.acronimoContratado:ConstantesIntegridadDominio.acronimoEstadoPendiente;
								// se adicionan los posibles estados de evolucion del programa
								servicio.setArrayPosEstServ(llenadoPosiblesEstados(estadoLogProgServ,
										elem2.getListaServicios(),
										"", 
										info.getUtilizaProgOdontIns()/*ConstantesBD.acronimoNo*, 
										info.getValidaPresuOdontCont(),
										info.getInstRegistraAtenExt(),
										elem2.getEstadoPrograma(),
										info.getCodigoCita(),
										ConstantesIntegridadDominio.acronimoMostrarServicios,
										elem1.getCodigoPkDetalle().intValue(),
										elem2.getCodigoPkProgramaServicio().intValue(),
										servicio.getServicio().getCodigo(),
										servicio.getPorConfirmar(),
										servicio.getInclusion(),
										servicio.getGarantia()));
							}else{*/
								// se adicionan los posibles estados de evolucion del programa
								servicio.setArrayPosEstServ(llenadoPosiblesEstados(servicio.getEstadoServicio(),
										elem2.getListaServicios(),
										"", 
										info.getUtilizaProgOdontIns()/*ConstantesBD.acronimoNo*/, 
										info.getValidaPresuOdontCont(),
										info.getInstRegistraAtenExt(),
										elem2.getEstadoPrograma(),
										info.getCodigoCita(),
										ConstantesIntegridadDominio.acronimoMostrarServicios,
										elem1.getCodigoPkDetalle().intValue(),
										elem2.getCodigoPkProgramaServicio().intValue(),
										servicio.getServicio().getCodigo(),
										servicio.getPorConfirmar(),
										servicio.getInclusion(),
										servicio.getGarantia()));
							//}
							
							//se verifica si se pueden o no habilitar para la proxima cita
							if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi)
									&& servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoContratado))
							{
								servicio.setHabilitarServ(ConstantesBD.acronimoSi);
							}else if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo)
									&& servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
							{
								servicio.setHabilitarServ(ConstantesBD.acronimoSi);
							}else{
								servicio.setHabilitarServ(ConstantesBD.acronimoNo);
							}
					}else
							bandBloqueoProg = true;
					}
				}
				if(bandBloqueoProg)
					elem2.setInactivarProg(ConstantesBD.acronimoSi);
				bandBloqueoProg = false;
			}
		}
		//********************************************************************
		
		
	}
	
	/**
	 * metodo que obtiene los posible estado a los que puede evolucionar un servicio o programa
	 * @param porConfirmar 
	 * @param garantia 
	 * @param inclusion 
	 * @param ArrayList<InfoServicios> servicios
	 * @param String seccionAplica
	 * @param String utilizaProgOdonIns
	 * @param String presupuestoOdontCont
	 * @param String estadoPrograma
	 * @return ArrayList<InfoDatosString>
	 */
	private ArrayList<InfoDatosString> llenadoPosiblesEstados(String estServicio,
			ArrayList<InfoServicios> servicios,
			String seccionAplica, 
			String utilizaProgOdonIns,
			String presupuestoOdontCont,
			String intsRegistraAtenExt,
			String estadoPrograma,
			int codigoCita,
			String casoLlenado,
			int detPlanTratamiento,
			int codigoPrograma,
			int codigoServicio, 
			String porConfirmar, 
			String inclusion, 
			String garantia)
	{
		ArrayList<InfoDatosString> array = new ArrayList<InfoDatosString>();
		// seteo inicial de todos los posible estados posible a los que un servicio o progrma
		// del plan de tratamiento pueda evolucionar
		InfoDatosString realizadoInterno = new InfoDatosString(ConstantesIntegridadDominio.acronimoRealizadoInterno,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoRealizadoInterno).toString());
		InfoDatosString realizadoExterno = new InfoDatosString(ConstantesIntegridadDominio.acronimoRealizadoExterno,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoRealizadoExterno).toString());
		InfoDatosString porAutorizarExc = new InfoDatosString(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoExcluido,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoPorAutorizar).toString()+" - "+ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoExcluido).toString());
		InfoDatosString porAutorizarInc = new InfoDatosString(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoInclusion,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoPorAutorizar).toString()+" - "+ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoInclusion).toString());
		InfoDatosString registroGarantia = new InfoDatosString(ConstantesIntegridadDominio.acronimoGarantia,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoGarantia).toString());
		InfoDatosString registroCancelacion = new InfoDatosString(ConstantesIntegridadDominio.acronimoEstadoCancelado,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoEstadoCancelado).toString());
		InfoDatosString excluido = new InfoDatosString(ConstantesIntegridadDominio.acronimoExcluido,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoExcluido).toString());
		InfoDatosString contratado = new InfoDatosString(ConstantesIntegridadDominio.acronimoContratado,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoContratado).toString());
		InfoDatosString pendiente = new InfoDatosString(ConstantesIntegridadDominio.acronimoEstadoPendiente,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoEstadoPendiente).toString());
		InfoDatosString porAutorizarGar = new InfoDatosString(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoGarantia,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoPorAutorizar).toString()+" - "+ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoGarantia).toString());
		InfoDatosString enProceso = new InfoDatosString(ConstantesIntegridadDominio.acronimoEnProceso,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoEnProceso).toString());
		InfoDatosString noAutorizacion = new InfoDatosString(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoNoAutorizado,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoNoAutorizado).toString());
		InfoDatosString terminado = new InfoDatosString(ConstantesIntegridadDominio.acronimoTerminado,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTerminado).toString());
		InfoDatosString porAutorizar = new InfoDatosString(ConstantesIntegridadDominio.acronimoPorAutorizar,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoPorAutorizar).toString());
		
		// Se verifica si la institución maneja progrmaas o Servicios
		
		logger.info("estado programa: "+estadoPrograma);
		logger.info("estado servicio: "+estServicio);
		//*******************************************CASO SERVICIO********************************************************************
		// se evalue el valor del estado del servicio
		if(casoLlenado.equals(ConstantesIntegridadDominio.acronimoMostrarServicios))
		{
			if(!estServicio.equals(""))
			{
				///Se agrega al arreglo el estado actual
				if(estServicio.equals(ConstantesIntegridadDominio.acronimoRealizadoInterno))
				{
					array.add(realizadoInterno);
				}
				else if(estServicio.equals(ConstantesIntegridadDominio.acronimoRealizadoExterno))
				{
					array.add(realizadoExterno);
				}
				else if(estServicio.equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoExcluido))
				{
					array.add(porAutorizarExc);
				}
				else if(estServicio.equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoInclusion))
				{
					array.add(porAutorizarInc);
				}
				else if(estServicio.equals(ConstantesIntegridadDominio.acronimoGarantia))
				{
					array.add(registroGarantia);
				}
				else if(estServicio.equals(ConstantesIntegridadDominio.acronimoEstadoCancelado))
				{
					if(!array.contains(registroCancelacion))
					{
						array.add(registroCancelacion);
					}
				}
				else if(estServicio.equals(ConstantesIntegridadDominio.acronimoExcluido))
				{
					array.add(excluido);
				}
				else if (estServicio.equals(ConstantesIntegridadDominio.acronimoContratado))
				{
					array.add(contratado);
				}
				else if (estServicio.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
				{
					array.add(pendiente);//>>>>>>>>>> OJOOOOOOOOOO
				}
				else if (estServicio.equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoGarantia))
				{
					array.add(porAutorizarGar);
				}
				else if (estServicio.equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoNoAutorizado))
				{
					array.add(noAutorizacion);
				}
				else if (estServicio.equals(ConstantesIntegridadDominio.acronimoPorAutorizar))
				{
					array.add(porAutorizar);
				}
				
				
				boolean servicioNoTieneCitas=PlanTratamiento.verificarServiciosAgendados(detPlanTratamiento, codigoPrograma, codigoServicio, codigoCita);
				
				// se verifica si la institucion maneja presupuesto o no 
				if(presupuestoOdontCont.equals(ConstantesBD.acronimoSi))
				{
					logger.info("Entra CASO 1");
					// Ser evaluan los estados del servicio y segun el estado se 
					// adicionan los posibles estados a los que se puede actualizar el servicio
					if(estServicio.equals(ConstantesIntegridadDominio.acronimoContratado))
					{
						/*if(!seccionAplica.equals(ConstantesIntegridadDominio.acronimoGarantia)
							&& !seccionAplica.equals(ConstantesIntegridadDominio.acronimoInclusion))
						{*/
						array.add(realizadoInterno);
						if(intsRegistraAtenExt.equals(ConstantesBD.acronimoSi))
							array.add(realizadoExterno);
						//}
						
						if(utilizaProgOdonIns.equals(ConstantesBD.acronimoNo))
						{
							if(servicioNoTieneCitas)
							{
								array.add(porAutorizarExc); 
							}
							if(!array.contains(registroCancelacion))
							{
								array.add(registroCancelacion);
							}
						}
					}
					else if(estServicio.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente)&&utilizaProgOdonIns.equals(ConstantesBD.acronimoNo))
					{
						//if(seccionAplica.equals(ConstantesIntegridadDominio.acronimoInclusion))
						array.add(porAutorizarInc);
					}
					else if(estServicio.equals(ConstantesIntegridadDominio.acronimoRealizadoInterno)&&!UtilidadTexto.getBoolean(porConfirmar))
					{
						array.add(registroGarantia);
					}
					
					//Mientras que todavía se encuentre porConfirmar = S se puede reversar el cambio
					if(UtilidadTexto.getBoolean(porConfirmar))
					{
						
						if(estServicio.equals(ConstantesIntegridadDominio.acronimoRealizadoInterno))
						{
							array.add(contratado);
							
							if(intsRegistraAtenExt.equals(ConstantesBD.acronimoSi))
								array.add(realizadoExterno);
							
							if(utilizaProgOdonIns.equals(ConstantesBD.acronimoNo))
							{
								if(servicioNoTieneCitas)
									array.add(porAutorizarExc);
								if(!array.contains(registroCancelacion))
								{
									array.add(registroCancelacion);
								}
							}
						}
						else if(estServicio.equals(ConstantesIntegridadDominio.acronimoRealizadoExterno))
						{
							array.add(contratado);
							array.add(realizadoInterno);
							
							if(utilizaProgOdonIns.equals(ConstantesBD.acronimoNo))
							{
								if(servicioNoTieneCitas)
									array.add(porAutorizarExc);
								if(!array.contains(registroCancelacion))
								{
									array.add(registroCancelacion);
								}
							}
						}
						
						else if(estServicio.equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoGarantia))
						{
							array.add(realizadoInterno);
						}
						else if(estServicio.equals(ConstantesIntegridadDominio.acronimoEstadoCancelado))
						{
							array.add(contratado);
							array.add(realizadoInterno);
							
							if(intsRegistraAtenExt.equals(ConstantesBD.acronimoSi))
								array.add(realizadoExterno);
							
							if(servicioNoTieneCitas)
								array.add(porAutorizarExc);
						}
						else if(estServicio.equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoInclusion)&&utilizaProgOdonIns.equals(ConstantesBD.acronimoNo))
						{
							array.add(pendiente);
						}
						else if(estServicio.equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoExcluido)&&utilizaProgOdonIns.equals(ConstantesBD.acronimoNo))
						{
							array.add(contratado);
						}
						
					}
				}
				else if(presupuestoOdontCont.equals(ConstantesBD.acronimoNo))
				{
					logger.info("Entra CASO 2");
					if(estServicio.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
					{
						/*if(!seccionAplica.equals(ConstantesIntegridadDominio.acronimoGarantia)
							&& !seccionAplica.equals(ConstantesIntegridadDominio.acronimoInclusion))
						{*/
						array.add(realizadoInterno);
						if(intsRegistraAtenExt.equals(ConstantesBD.acronimoSi))
							array.add(realizadoExterno);
						//}
						
						if(utilizaProgOdonIns.equals(ConstantesBD.acronimoNo))
						{
							if(servicioNoTieneCitas)
								array.add(porAutorizarExc);
							if(!array.contains(registroCancelacion))
							{
								array.add(registroCancelacion);
							}
						}
					}
					else if(estServicio.equals(ConstantesIntegridadDominio.acronimoRealizadoInterno))
					{
						array.add(registroGarantia);
					}
					
					//Mientras que todavía se encuentre porConfirmar = S se puede reversar el cambio
					if(UtilidadTexto.getBoolean(porConfirmar))
					{
						if(estServicio.equals(ConstantesIntegridadDominio.acronimoRealizadoInterno))
						{
							
							array.add(pendiente);
							if(intsRegistraAtenExt.equals(ConstantesBD.acronimoSi))
								array.add(realizadoExterno);
							
							if(utilizaProgOdonIns.equals(ConstantesBD.acronimoNo))
							{
								if(servicioNoTieneCitas)
									array.add(porAutorizarExc);
								if(!array.contains(registroCancelacion))
								{
									array.add(registroCancelacion);
								}
							}
						}
						else if(estServicio.equals(ConstantesIntegridadDominio.acronimoRealizadoExterno))
						{
							array.add(contratado);
							array.add(realizadoInterno);
							
							if(utilizaProgOdonIns.equals(ConstantesBD.acronimoNo))
							{
								if(servicioNoTieneCitas)
									array.add(porAutorizarExc);
								array.add(registroCancelacion);
							}
						}
						
						
						else if(estServicio.equals(ConstantesIntegridadDominio.acronimoEstadoCancelado))
						{
							array.add(contratado);
							array.add(realizadoInterno);
							
							if(intsRegistraAtenExt.equals(ConstantesBD.acronimoSi))
								array.add(realizadoExterno);
							
							if(servicioNoTieneCitas)
								array.add(porAutorizarExc);
						}
						//Caso de garantia
						else if(estServicio.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente)&&UtilidadTexto.getBoolean(garantia))
						{
							array.add(realizadoInterno);
						}
						
						
					}
				}
				
				
			}
		}
		//*****************************************************************************************************************************
		//***************************************CASO PROGRAMAS*************************************************************************
		else if(casoLlenado.equals(ConstantesIntegridadDominio.acronimoMostrarProgramas))
		{
			//Se agrega al arreglo el estado actual
			if(estadoPrograma.equals(ConstantesIntegridadDominio.acronimoTerminado))
			{
				array.add(terminado);
			}
			else if(estadoPrograma.equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoExcluido))
			{
				array.add(porAutorizarExc);
			}
			else if(estadoPrograma.equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoInclusion))
			{
				array.add(porAutorizarInc);
			}
			else if(estadoPrograma.equals(ConstantesIntegridadDominio.acronimoGarantia))
			{
				array.add(registroGarantia);
			}
			else if(estadoPrograma.equals(ConstantesIntegridadDominio.acronimoEstadoCancelado))
			{
				if(!array.contains(registroCancelacion))
				{
					array.add(registroCancelacion);
				}
			}
			else if(estadoPrograma.equals(ConstantesIntegridadDominio.acronimoExcluido))
			{
				array.add(excluido);
			}
			else if (estadoPrograma.equals(ConstantesIntegridadDominio.acronimoContratado))
			{
				array.add(contratado);
			}
			else if (estadoPrograma.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
			{
				array.add(pendiente);
			}
			else if (estadoPrograma.equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoGarantia))
			{
				array.add(porAutorizarGar);
			}
			else if (estadoPrograma.equals(ConstantesIntegridadDominio.acronimoEnProceso))
			{
				array.add(enProceso);
			}
			else if (estadoPrograma.equals(ConstantesIntegridadDominio.acronimoEstadoSolicitudDctoNoAutorizado))
			{
				array.add(noAutorizacion);
			}
			
			
			if(utilizaProgOdonIns.equals(ConstantesBD.acronimoSi)) // Maneja Programa
			{
				//logger.info("VALOR DEL PARÁMETRO PRESUPUESTO ODONTOLOGICA: "+presupuestoOdontCont);
				
				//array.add(porAutorizarInc);
				if(presupuestoOdontCont.equals(ConstantesBD.acronimoSi))
				{
					logger.info("Entra CASO 3");
					//logger.info("verificar estados servicios programa: "+verificarEstadosServiciosProg(servicios,ConstantesIntegridadDominio.acronimoContratado,""));
					//logger.info("verificar servicios agendades: "+PlanTratamiento.verificarServiciosAgendados(detPlanTratamiento, codigoPrograma, codigoServicio, codigoCita));
					//Verificar que todos los servicios del programa esten en estado contratado
					if(verificarEstadosServiciosProg(servicios,ConstantesIntegridadDominio.acronimoContratado,"") && noTieneServiciosAgendados(servicios))
						//&& PlanTratamiento.verificarServiciosAgendados(detPlanTratamiento, codigoPrograma, codigoServicio, codigoCita))
						array.add(porAutorizarExc); // se bloquean todos los servicios asociados al programa
					if(estadoPrograma.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
					{
						array.add(porAutorizarInc);
					}
					else if(estadoPrograma.equals(ConstantesIntegridadDominio.acronimoTerminado)&&!UtilidadTexto.getBoolean(porConfirmar))
					{
						// verificar que los servicios del programa estenn por confirmar N
						if(verificarEstadosServiciosProg(servicios,"",ConstantesBD.acronimoNo))
							array.add(registroGarantia);// hay que tener en cuenta que solo dejaria evolucionar los servicios RI
					}
					else if(estadoPrograma.equals(ConstantesIntegridadDominio.acronimoEnProceso))
					{
						if(!array.contains(registroCancelacion))
						{
							array.add(registroCancelacion);
						}
					}
					
					//Si el programa aun está por confirmar se deben seguir mostrando
					if(UtilidadTexto.getBoolean(porConfirmar))
					{
						
						if(estadoPrograma.equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoExcluido)||
								estadoPrograma.equals(ConstantesIntegridadDominio.acronimoExcluido))
						{
							array.add(contratado);
							
						}
						else if(estadoPrograma.equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoInclusion))
						{
							array.add(pendiente);
							
						}
						else if(estadoPrograma.equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoGarantia))
						{
							array.add(terminado);
						}
						else if(estadoPrograma.equals(ConstantesIntegridadDominio.acronimoEstadoCancelado))
						{
							array.add(enProceso);
						}
						
						
						
					}
				}else{
					if(presupuestoOdontCont.equals(ConstantesBD.acronimoNo))
					{
						logger.info("Entra CASO 4");
						if(estadoPrograma.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente)
							&& PlanTratamiento.verificarServiciosAgendados(detPlanTratamiento, codigoPrograma, codigoServicio, codigoCita))
						{
							array.add(excluido);
						}else if(estadoPrograma.equals(ConstantesIntegridadDominio.acronimoTerminado)&&!UtilidadTexto.getBoolean(porConfirmar))
						{
							// verificar que los servicios del programa estenn por confirmar N
							if(verificarEstadosServiciosProg(servicios,"",ConstantesBD.acronimoNo))
								array.add(registroGarantia);// hay que tener en cuenta que solo dejaria evolucionar los servicios RI
						}
						
						//Si el programa aun está por confirmar se deben seguir mostrando
						if(UtilidadTexto.getBoolean(porConfirmar))
						{
							
							if(estadoPrograma.equals(ConstantesIntegridadDominio.acronimoExcluido))
							{
								array.add(pendiente);
								
							}
							else if(estadoPrograma.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente)&&UtilidadTexto.getBoolean(garantia))
							{
								array.add(terminado);
							}
							else if(estadoPrograma.equals(ConstantesIntegridadDominio.acronimoEstadoCancelado))
							{
								array.add(enProceso);
							}
						}
					}
				}
				
				//la cancelacion por programa aplica si tiene o no presupuesto odontologico contratado
				if(estadoPrograma.equals(ConstantesIntegridadDominio.acronimoEnProceso))
				{
					//logger.info("Entra CASO Común");
					// evaluar que no exista un servicio que no pertenezca a la cita y que este por confirma S
					if(verificarServiciosProgCancelar(servicios, codigoCita))
					{
						if(!array.contains(registroCancelacion))
						{
							array.add(registroCancelacion);
						}
					}
				}
			}
		}
		//***************************************************************************************************************
		
		logger.info("NUMERO DE ELEMENTOS DEL ARREGLO DE ESTADOS (caso llenado "+casoLlenado+")!!! "+array.size());
		for(InfoDatosString ele:array)
		{
			logger.info("estados: "+ele.getCodigo()+", "+ele.getValue());
		}
		return array;
	}
	
	/**
	 * 
	 * @param servicios
	 * @return
	 */
	private boolean noTieneServiciosAgendados(ArrayList<InfoServicios> servicios) 
	{
		ArrayList<String> estadosCita=new ArrayList<String>();
		estadosCita.add(ConstantesIntegridadDominio.acronimoAsignado);
		estadosCita.add(ConstantesIntegridadDominio.acronimoAtendida);
		estadosCita.add(ConstantesIntegridadDominio.acronimoAreprogramar);
		estadosCita.add(ConstantesIntegridadDominio.acronimoReservado);
		
		for(InfoServicios elem: servicios)
		{
			if(elem.getCodigoCitaFechaActual(estadosCita).longValue()>0)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * verificar 
	 * @param servicios
	 * @return
	 */
	private boolean verificarEstadosServiciosProg(ArrayList<InfoServicios> servicios, String estado, String porConfirmar)
	{
		boolean result = true;
		for(InfoServicios elem: servicios)
		{
			if(!estado.equals(""))
			{
				if(!elem.getEstadoServicio().equals(estado))
				{
					result = false;
					break;
				}
			}else  if(!porConfirmar.equals(elem.getPorConfirmar()))
			{
				result = false;
				break;
			}
				
		}
		return result;
	}
	
	/**
	 * verificar si el programa puede evolucionar a la cancelacion del mismo
	 * @param servicios
	 * @param codigoCita
	 * @return
	 */
	private boolean verificarServiciosProgCancelar(ArrayList<InfoServicios> servicios, int codigoCita)
	{
		boolean result = true;
		for(InfoServicios elem: servicios)
		{
			if(codigoCita!=elem.getCodigoCita().intValue()&&elem.getPorConfirmar().equals(ConstantesBD.acronimoSi))
			{
				result = false;
				break;
			}
		}
		return result;
	}

	/**
	 * metodo que evalua los servicios
	 * @param servicios
	 * @param codigoCita
	 * @return
	 */
	private int habilitarEvolucionServicioProg(ArrayList<InfoServicios> servicios, int codigoCita)
	{
		int posicion = 0;
		for(InfoServicios elem: servicios)
		{
			if(codigoCita!=elem.getCodigoCita().intValue()&&elem.getPorConfirmar().equals(ConstantesBD.acronimoSi)
				|| (elem.getPorConfirmar().equals(ConstantesBD.acronimoNo)
					&&(elem.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente)||elem.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoContratado))))
			{
				return posicion;
			}
			posicion++;
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * M&eacute;todo que recorre los servicios que se deben inactivar
	 * @param servicios ArrayList<InfoServicios> Listado de servicios a inactivar
	 * @param posicion
	 */
	private void inactivarServiciosProg(ArrayList<InfoServicios> servicios, int posicion)
	{
		if(posicion>0)
		{
			for(int i=posicion-1;i>ConstantesBD.codigoNuncaValido;i--)
			{
				if(!servicios.get(i).getPorConfirmar().equals(ConstantesBD.acronimoNo))
				{
					servicios.get(i).setInactivarServ(ConstantesBD.acronimoSi);
				}
			}
		}
	}
	
	/**
	 * M&eacute;etodo que obtiene el caso de actualizaci&oacute;n al que aplica
	 * @param newEstado
	 * @return
	 */
	private InfoDatosString obtenerCasoActualizacion(String newEstado)
	{
		String casoActualizar = "";
		String estadoAcualizar = "";
		String[] newEstProgServ = null;
		if(!newEstado.equals(""))
		{
			newEstProgServ = newEstado.split(ConstantesBD.separadorSplit);
			if(newEstProgServ.length>1)
			{
				if(newEstProgServ[0].equals(ConstantesIntegridadDominio.acronimoPorAutorizar))
				{
					if(newEstProgServ[1].equals(ConstantesIntegridadDominio.acronimoExcluido))
					{
						casoActualizar = String.valueOf(ConstantesBD.acronimoRegistrarExclusiones);
						estadoAcualizar = ConstantesIntegridadDominio.acronimoPorAutorizar;
					}
					else if(newEstProgServ[1].equals(ConstantesIntegridadDominio.acronimoInclusion))
					{
						casoActualizar = String.valueOf(ConstantesBD.acronimoRegistrarInclusiones);
						estadoAcualizar = ConstantesIntegridadDominio.acronimoPorAutorizar;
					}
					else if(newEstProgServ[1].equals(ConstantesIntegridadDominio.acronimoGarantia))
					{
						casoActualizar = String.valueOf(ConstantesBD.acronimoRegistrarGarantias);
						estadoAcualizar = ConstantesIntegridadDominio.acronimoPorAutorizar;
					}
				}
			}else if(newEstado.equals(ConstantesIntegridadDominio.acronimoRealizadoInterno))
			{
				casoActualizar = String.valueOf(ConstantesBD.acronimoRegistroServicioEvolucionar);
				estadoAcualizar = ConstantesIntegridadDominio.acronimoRealizadoInterno;
			}else if(newEstado.equals(ConstantesIntegridadDominio.acronimoRealizadoExterno))
			{
				casoActualizar = String.valueOf(ConstantesBD.acronimoRegistrarAtencionExterna);
				estadoAcualizar = ConstantesIntegridadDominio.acronimoRealizadoExterno;
			}else if(newEstado.equals(ConstantesIntegridadDominio.acronimoGarantia))
			{
				casoActualizar = String.valueOf(ConstantesBD.acronimoRegistrarGarantias);
				estadoAcualizar = ConstantesIntegridadDominio.acronimoGarantia;
			}else if(newEstado.equals(ConstantesIntegridadDominio.acronimoEstadoCancelado))
			{
				casoActualizar = String.valueOf(ConstantesBD.acronimoRegistrarCancelaciones);
				estadoAcualizar = ConstantesIntegridadDominio.acronimoEstadoCancelado;
			}else if(newEstado.equals(ConstantesIntegridadDominio.acronimoExcluido))
			{
				casoActualizar = String.valueOf(ConstantesBD.acronimoRegistrarExclusiones);
				estadoAcualizar = ConstantesIntegridadDominio.acronimoExcluido;
			}else if(newEstado.equals(ConstantesIntegridadDominio.acronimoContratado))
			{
				casoActualizar = String.valueOf(ConstantesBD.acronimoRegistrarContratado); 
				estadoAcualizar = ConstantesIntegridadDominio.acronimoContratado;
			}else if(newEstado.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
			{
				casoActualizar = String.valueOf(ConstantesBD.acronimoRegistrarPendiente);
				estadoAcualizar = ConstantesIntegridadDominio.acronimoEstadoPendiente;
			}
		}
		return new InfoDatosString(casoActualizar,estadoAcualizar);
	}
	
	/**
	 * metodo que activa o inactica los servicios de un programa especifico
	 * @param String seccion
	 * @param info
	 * @param inactivarServicio
	 * @param utilizaProgOdontIns
	 */
	private void inactivarServiciosProg(InfoOdontograma info, String inactivarServicio)
	{
		//***************************************************************************
		// Inactivar servicios asociados a un programa de la seccion detalle/otros/boca hallazgos
		// el inactivar o activar aplica tanto para el programa como para el servicio.
		InfoDetallePlanTramiento elem = new InfoDetallePlanTramiento();
		InfoHallazgoSuperficie elem1 = new InfoHallazgoSuperficie();
		InfoProgramaServicioPlan elem2 = new InfoProgramaServicioPlan();
		
		if(info.getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoOtro))
			elem = (InfoDetallePlanTramiento) info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1());
		else if(info.getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoDetalle))
			elem = (InfoDetallePlanTramiento) info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1());
		
		if(!info.getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoBoca))
			elem1 = (InfoHallazgoSuperficie) elem.getDetalleSuperficie().get(info.getIndicador2());
		else
			elem1 = (InfoHallazgoSuperficie) info.getInfoPlanTrata().getSeccionHallazgosBoca().get(info.getIndicador2());
		
		elem2 = (InfoProgramaServicioPlan) elem1.getProgramasOservicios().get(info.getIndicador3());
		elem2.setNewEstadoProg(info.getNewEstadoProg());
		if(elem2.getListaServicios().size()>0)
		{
			if(info.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoExcluido)
				|| info.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoExcluido))
				elem2.setExclusion(inactivarServicio);
			else
				elem2.setExclusion(ConstantesBD.acronimoNo);
			
			if(!info.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoExcluido)
				|| !info.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoExcluido)
				|| !info.getNewEstadoProg().equals(info.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoGarantia)) 
				|| !info.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoEstadoCancelado))
			{
				elem2.getMotivoCancelacion().setCodigo("");
				elem2.getMotivoCancelacion().setNombre("");
				elem2.getMotivoCancelacion().setDescripcion("");
			}
			
			for(InfoServicios servicio: elem2.getListaServicios())
			{
				servicio.setInactivarServ(inactivarServicio);
				servicio.setHabilitarServ(inactivarServicio); 
				
				//se verifica si se pueden o no habilitar el servicio para la proxima cita
				if(inactivarServicio.equals(ConstantesBD.acronimoNo))
				{
					logger.info("servicio.getNewEstado(): "+servicio.getNewEstado());
					logger.info("info.getValidaPresuOdontCont(): "+info.getValidaPresuOdontCont());
					if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi))
					{
						logger.info("paos por aqui 47:4");
						servicio.setHabilitarServ(ConstantesBD.acronimoSi);
						servicio.setNewEstado(ConstantesIntegridadDominio.acronimoContratado);
						//SE vuelve a llenar el arreglo de posibles estados
						
						servicio.setArrayPosEstServ(llenadoPosiblesEstados(
								servicio.getNewEstado(), 
								elem2.getListaServicios(),
								"",
								info.getUtilizaProgOdontIns(), 
								info.getValidaPresuOdontCont(),
								info.getInstRegistraAtenExt(), 
								elem2.getNewEstadoProg(), 
								info.getCodigoCita(), 
								ConstantesIntegridadDominio.acronimoMostrarServicios,
								ConstantesBD.codigoNuncaValido, 
								elem2.getCodigoPkProgramaServicio().intValue(),
								servicio.getServicio().getCodigo(),
								servicio.getPorConfirmar(),
								servicio.getInclusion(),
								servicio.getGarantia()));
					}else if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo))
					{
						servicio.setHabilitarServ(ConstantesBD.acronimoSi);
						servicio.setNewEstado(ConstantesIntegridadDominio.acronimoEstadoPendiente);
						//SE vuelve a llenar el arreglo de posibles estados
						servicio.setArrayPosEstServ(llenadoPosiblesEstados(
								servicio.getNewEstado(), 
								elem2.getListaServicios(),
								"",
								info.getUtilizaProgOdontIns(), 
								info.getValidaPresuOdontCont(),
								info.getInstRegistraAtenExt(), 
								elem2.getNewEstadoProg(), 
								info.getCodigoCita(), 
								ConstantesIntegridadDominio.acronimoMostrarServicios,
								ConstantesBD.codigoNuncaValido, 
								elem2.getCodigoPkProgramaServicio().intValue(),
								servicio.getServicio().getCodigo(),
								servicio.getPorConfirmar(),
								servicio.getInclusion(),
								servicio.getGarantia()));
					}else{
						servicio.setHabilitarServ(ConstantesBD.acronimoNo);
					}
				}else
					servicio.setHabilitarServ(ConstantesBD.acronimoNo);
				logger.info("servicio.getNewEstado(): "+servicio.getNewEstado());
			}
		}
		
		//************************************** Programas N Superficies ***********************************Tarea 158745
		
		
		for(InfoHallazgoSuperficie superficie:elem.getDetalleSuperficie())
		{
			if(superficie.getExisteBD().isActivo())
			{
				if(superficie.getHallazgoREQUERIDO().getCodigo()==elem1.getHallazgoREQUERIDO().getCodigo())
				{
					for(InfoProgramaServicioPlan prog:superficie.getProgramasOservicios())
					{
						if(prog.getExisteBD().isActivo())
						{
							if(prog.getProgHallazgoPieza().getCodigoPk()==elem2.getProgHallazgoPieza().getCodigoPk() && elem2.getProgHallazgoPieza().getCodigoPk()!=ConstantesBD.codigoNuncaValido )
							{							
							   prog.setNewEstadoProg(info.getNewEstadoProg());  
							  
							   if(prog.getListaServicios().size()>0)
							  {	
								if(info.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoExcluido)
										|| info.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoExcluido))
									prog.setExclusion(inactivarServicio);
									else
										prog.setExclusion(ConstantesBD.acronimoNo);
									
									if(!info.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoExcluido)
										|| !info.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoExcluido)
										|| !info.getNewEstadoProg().equals(info.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoGarantia)) 
										|| !info.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoEstadoCancelado))
									{
										prog.getMotivoCancelacion().setCodigo("");
										prog.getMotivoCancelacion().setNombre("");
										prog.getMotivoCancelacion().setDescripcion("");
									}
							  }
								
									for(InfoServicios servicioInterno: prog.getListaServicios())
									{																		
										servicioInterno.setInactivarServ(inactivarServicio);
										servicioInterno.setHabilitarServ(inactivarServicio); 
										
										//se verifica si se pueden o no habilitar el servicio para la proxima cita
										if(inactivarServicio.equals(ConstantesBD.acronimoNo))
										{
											logger.info("servicio.getNewEstado(): "+servicioInterno.getNewEstado());
											logger.info("info.getValidaPresuOdontCont(): "+info.getValidaPresuOdontCont());
											if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi))
											{
												logger.info("paos por aqui 47:4");
												servicioInterno.setHabilitarServ(ConstantesBD.acronimoSi);
												servicioInterno.setNewEstado(ConstantesIntegridadDominio.acronimoContratado);
												//SE vuelve a llenar el arreglo de posibles estados
												
												servicioInterno.setArrayPosEstServ(llenadoPosiblesEstados(
														servicioInterno.getNewEstado(), 
														elem2.getListaServicios(),
														"",
														info.getUtilizaProgOdontIns(), 
														info.getValidaPresuOdontCont(),
														info.getInstRegistraAtenExt(), 
														elem2.getNewEstadoProg(), 
														info.getCodigoCita(), 
														ConstantesIntegridadDominio.acronimoMostrarServicios,
														ConstantesBD.codigoNuncaValido, 
														elem2.getCodigoPkProgramaServicio().intValue(),
														servicioInterno.getServicio().getCodigo(),
														servicioInterno.getPorConfirmar(),
														servicioInterno.getInclusion(),
														servicioInterno.getGarantia()));
											}else if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo))
											{
												servicioInterno.setHabilitarServ(ConstantesBD.acronimoSi);
												servicioInterno.setNewEstado(ConstantesIntegridadDominio.acronimoEstadoPendiente);
												//SE vuelve a llenar el arreglo de posibles estados
												servicioInterno.setArrayPosEstServ(llenadoPosiblesEstados(
														servicioInterno.getNewEstado(), 
														elem2.getListaServicios(),
														"",
														info.getUtilizaProgOdontIns(), 
														info.getValidaPresuOdontCont(),
														info.getInstRegistraAtenExt(), 
														elem2.getNewEstadoProg(), 
														info.getCodigoCita(), 
														ConstantesIntegridadDominio.acronimoMostrarServicios,
														ConstantesBD.codigoNuncaValido, 
														elem2.getCodigoPkProgramaServicio().intValue(),
														servicioInterno.getServicio().getCodigo(),
														servicioInterno.getPorConfirmar(),
														servicioInterno.getInclusion(),
														servicioInterno.getGarantia()));
											}else{
												servicioInterno.setHabilitarServ(ConstantesBD.acronimoNo);
											}
										}else
											servicioInterno.setHabilitarServ(ConstantesBD.acronimoNo);
										logger.info("servicio.getNewEstado(): "+servicioInterno.getNewEstado());									
										
								}
							}
						}
					}
				}
			}
		}
		//***********************************************************************************************************************
		
		
		
		
		//***************************************************************************
	}
	
	/**
	 * metodo que pone en garantia los servicios del un programa 
	 * @param info
	 * @param inactivarServicio
	 */
	private void garantiaServiciosProg(InfoOdontograma info, String inactivarServicio)
	{
		//***************************************************************************
		// poner en garantia los servicios asociados a un programa de la seccion detalle/otros/boca hallazgos
		InfoDetallePlanTramiento elem = new InfoDetallePlanTramiento();
		InfoHallazgoSuperficie elem1 = new InfoHallazgoSuperficie();
		InfoProgramaServicioPlan elem2 = new InfoProgramaServicioPlan();
		
		if(info.getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoOtro))
			elem = (InfoDetallePlanTramiento) info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1());
		else if(info.getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoDetalle))
			elem = (InfoDetallePlanTramiento) info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1());
		
		if(!info.getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoBoca))
			elem1 = (InfoHallazgoSuperficie) elem.getDetalleSuperficie().get(info.getIndicador2());
		else
			elem1 = (InfoHallazgoSuperficie) info.getInfoPlanTrata().getSeccionHallazgosBoca().get(info.getIndicador2());
		
		elem2 = (InfoProgramaServicioPlan) elem1.getProgramasOservicios().get(info.getIndicador3());
		elem2.setNewEstadoProg(info.getNewEstadoProg());
		if(elem2.getListaServicios().size()>0)
		{
			elem2.setExclusion(ConstantesBD.acronimoNo);
			elem2.setInclusion(ConstantesBD.acronimoNo);
			elem2.setGarantia(inactivarServicio);
			if(inactivarServicio.equals(ConstantesBD.acronimoNo)){
				elem2.getMotivoCancelacion().setCodigo("");
				elem2.getMotivoCancelacion().setNombre("");
				elem2.getMotivoCancelacion().setDescripcion("");
			}
			
			for(InfoServicios servicio: elem2.getListaServicios())
			{
				servicio.setInactivarServ(inactivarServicio);
				servicio.setHabilitarServ(inactivarServicio);
				servicio.setGarantia(inactivarServicio);
				servicio.setExclusion(ConstantesBD.acronimoNo);
				servicio.setInclusion(ConstantesBD.acronimoNo);
				if(inactivarServicio.equals(ConstantesBD.acronimoSi))
					servicio.setNewEstado(ConstantesIntegridadDominio.acronimoGarantia);
				else{
					servicio.setNewEstado(servicio.getEstadoServicio());
					servicio.getMotivoCancelacion().setCodigo("");
					servicio.getMotivoCancelacion().setNombre("");
					servicio.getMotivoCancelacion().setDescripcion("");
				}
				//se verifica si se pueden o no habilitar el servicio para la proxima cita
				if(inactivarServicio.equals(ConstantesBD.acronimoNo))
				{
					logger.info("servicio.getNewEstado(): "+servicio.getNewEstado());
					if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi)
							&& servicio.getNewEstado().equals(ConstantesIntegridadDominio.acronimoContratado))
					{
						servicio.setHabilitarServ(ConstantesBD.acronimoSi);
					}else if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo)
							&& servicio.getNewEstado().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
					{
						servicio.setHabilitarServ(ConstantesBD.acronimoSi);
					}else{
						servicio.setHabilitarServ(ConstantesBD.acronimoNo);
					}
				}else
					servicio.setHabilitarServ(ConstantesBD.acronimoNo);
			}
		}
		//***************************************************************************
	}
	
	private void inclusionProgServ(InfoOdontograma info)
	{
		//***************************************************************************
		// Inclusion de un programa de la seccion detalle/otros/boca hallazgos
		InfoDetallePlanTramiento piezaSeleccionada = new InfoDetallePlanTramiento();
		InfoHallazgoSuperficie superficieSeleccionada = new InfoHallazgoSuperficie();
		InfoProgramaServicioPlan programaSeleccionado = new InfoProgramaServicioPlan();
		
		if(info.getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoOtro))
			piezaSeleccionada = (InfoDetallePlanTramiento) info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1());
		else if(info.getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoDetalle))
			piezaSeleccionada = (InfoDetallePlanTramiento) info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1());
		
		if(!info.getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoBoca))
			superficieSeleccionada = (InfoHallazgoSuperficie) piezaSeleccionada.getDetalleSuperficie().get(info.getIndicador2());
		else
			superficieSeleccionada = (InfoHallazgoSuperficie) info.getInfoPlanTrata().getSeccionHallazgosBoca().get(info.getIndicador2());
		
		programaSeleccionado = (InfoProgramaServicioPlan) superficieSeleccionada.getProgramasOservicios().get(info.getIndicador3());
		
		if(info.getUtilizaProgOdontIns().equals(ConstantesBD.acronimoSi))
		{
			programaSeleccionado.setNewEstadoProg(info.getNewEstadoProg());
			programaSeleccionado.setEstadoPrograma(info.getNewEstadoProg());
			programaSeleccionado.setInclusion(ConstantesBD.acronimoSi);
			programaSeleccionado.setExclusion(ConstantesBD.acronimoNo);
			programaSeleccionado.setGarantia(ConstantesBD.acronimoNo);
			programaSeleccionado.setInactivarProg(ConstantesBD.acronimoSi);
			if(programaSeleccionado.getListaServicios().size()>0)
			{
				for(InfoServicios servicio: programaSeleccionado.getListaServicios())
				{
					if(info.getUtilizaProgOdontIns().equals(ConstantesBD.acronimoSi))
					{
						servicio.setEstadoServicio(info.getNewEstadoProg());
						servicio.setNewEstado(info.getNewEstadoProg());
						servicio.setInactivarServ(ConstantesBD.acronimoSi);
						servicio.setInclusion(ConstantesBD.acronimoSi);
						servicio.setExclusion(ConstantesBD.acronimoNo);
						servicio.setGarantia(ConstantesBD.acronimoNo);
					}
				}
			}
			
			
			for(InfoHallazgoSuperficie superficie:piezaSeleccionada.getDetalleSuperficie())
			{
				if(superficie.getExisteBD().isActivo())
				{
					if(superficie.getHallazgoREQUERIDO().getCodigo()==superficieSeleccionada.getHallazgoREQUERIDO().getCodigo())
					{
						for(InfoProgramaServicioPlan prog:superficie.getProgramasOservicios())
						{
							if(prog.getExisteBD().isActivo())
							{
								if(prog.getProgHallazgoPieza().getCodigoPk()==programaSeleccionado.getProgHallazgoPieza().getCodigoPk())
								{
									prog.setNewEstadoProg(info.getNewEstadoProg());
									prog.setEstadoPrograma(info.getNewEstadoProg());
									prog.setInclusion(ConstantesBD.acronimoSi);
									prog.setExclusion(ConstantesBD.acronimoNo);
									prog.setGarantia(ConstantesBD.acronimoNo);
									prog.setInactivarProg(ConstantesBD.acronimoSi);

									for(InfoServicios servicioInterno: prog.getListaServicios())
									{
										servicioInterno.setEstadoServicio(info.getNewEstadoProg());
										servicioInterno.setNewEstado(info.getNewEstadoProg());
										servicioInterno.setInactivarServ(ConstantesBD.acronimoSi);
										servicioInterno.setInclusion(ConstantesBD.acronimoSi);
										servicioInterno.setExclusion(ConstantesBD.acronimoNo);
										servicioInterno.setGarantia(ConstantesBD.acronimoNo);
									}
								}
							}
						}
					}
				}
			}
			
			
		}else{
			programaSeleccionado.getListaServicios().get(info.getIndicador4()).setEstadoServicio(info.getNewEstadoProg());
			programaSeleccionado.getListaServicios().get(info.getIndicador4()).setNewEstado(info.getNewEstadoProg());
			programaSeleccionado.getListaServicios().get(info.getIndicador4()).setInactivarServ(ConstantesBD.acronimoSi);
			programaSeleccionado.getListaServicios().get(info.getIndicador4()).setInclusion(ConstantesBD.acronimoSi);
			programaSeleccionado.getListaServicios().get(info.getIndicador4()).setExclusion(ConstantesBD.acronimoNo);
			programaSeleccionado.getListaServicios().get(info.getIndicador4()).setGarantia(ConstantesBD.acronimoNo);
		}
		//***************************************************************************
		
	}
	
	/**
	 * Método que valida el órden de los servicios en el momento de cambiarles el estado
	 * @param info {@link InfoOdontograma} Informaci&oacute;n del odontograma.
	 */
	private void validacionEvolucionOrdenServicios(InfoOdontograma info)
	{
		//***************************************************************************
		// Validacion evolucion orden servicios Detalle/Otros/Boca Hallazgos
		InfoDetallePlanTramiento elem = new InfoDetallePlanTramiento();
		InfoHallazgoSuperficie elem1 = new InfoHallazgoSuperficie();
		InfoProgramaServicioPlan elem2 = new InfoProgramaServicioPlan();

		/*
		 * Indicador 1  --> Pieza {la primera posicion es (1) y represnta la primera pieza}
		 * Indicador 2  --> Superficie {la primera posicion es (0) y represnta la primera superficie}
		 * Indicador 3  --> Contprog {la primera posicion es (0) y represnta el primer programa}
		 * Indicador 4  --> resultValEvoOrd[0] -- Ni idea  
		 * Indicador 5  --> resultValEvoOrd[1] -- Ni idea  -- orden del servicio
		 */
		if(info.getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoOtro))
			elem = (InfoDetallePlanTramiento) info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1());
		else if(info.getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoDetalle))
			elem = (InfoDetallePlanTramiento) info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1());
		
		if(!info.getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoBoca))
			elem1 = (InfoHallazgoSuperficie) elem.getDetalleSuperficie().get(info.getIndicador2());
		else
			elem1 = (InfoHallazgoSuperficie) info.getInfoPlanTrata().getSeccionHallazgosBoca().get(info.getIndicador2());
		
		elem2 = (InfoProgramaServicioPlan) elem1.getProgramasOservicios().get(info.getIndicador3());
		
		String[] codigosServProg = info.getEstadosServicios().split(",");
		
		if(elem2.getListaServicios().size()>0)
		{
			logger.info(info.getEstadosServicios());
			/*
			 * Indicador 5 es el índice del servicio seleccionado
			 */
			/*
			 * COMENTARIOS ARMANDO:
			 * EL INDICADOR 4, ME DA UN IDENTIFICADOR PARA CUANDO CAMBIO EL ESTADO DEL SERVICION.
			 * EN LA EVOLUCION, LOS INDICADORES PARA ESTOS ESTADOS ES.
			 * 1. en el caso de que no se pueda modificar el estado por problema con ordenes.
			 * 2. CUANDO EL ESTADO QUE SE ASIGNA ES CONTRATADO.
			 * 3. CUANDO EL ESTADO QUE SE ASIGNA ES REALIZADO INTERNO.
			 * al parecer, el indicador5 es el orden del servicio que estoy evaluando.
			 * 
			 * 
			 * El indicador por la logica que se tiene aca abjo, es el indice del servicio, pero tambien envian este indicador en -1 cuando es el programa el modificado.
			 */
			switch (info.getIndicador4()) {
				case 1:  // caso 1, cuando no se puede cambiar el estado, por ejemplo cuando se cambia de contratado a RI y hay servicios de menor orden que no se han cambiado todavia.
				case 2:
					//en el caso de que se quiere cambiar el servicio a estado contratado.
					int cont = 0;
					for(InfoServicios servicio: elem2.getListaServicios())
					{
						if(cont<codigosServProg.length) //VERFICO SI YA SAQUE TODOS LOS ESTADOS.
						{
							if(info.getIndicador5()!=ConstantesBD.codigoNuncaValido) //si es difrerente al programa, es decir si es un servicio el que se esta modificando.
							{
								//tmp = nombreAtributoOdontograma+".infoPlanTrata."+contenedorAux+"["+listaServicios.getPosDet()+"].detalleSuperficie["+listaServicios.getPosSupf()+"].programasOservicios["+listaServicios.getPosProg()+"].listaServicios["+listaServicios.getPosServ()+"].inactivarServ";
								//if(estadoInterno.equals("accionEvoOrdServ")||(estadoInterno.equals("accionEvoOrdServDetalle")&&((servicio.getInclusion().equals(ConstantesBD.acronimoNo) || servicio.getInclusion().equals(""))&& !servicio.getServicioParCita().equals(ConstantesBD.acronimoNo))))
								if(estadoInterno.equals("accionEvoOrdServDetalle"))
								{
									if((servicio.getInclusion().equals(ConstantesBD.acronimoNo) || servicio.getInclusion().equals(""))&& !servicio.getServicioParCita().equals(ConstantesBD.acronimoNo))
									{
										if(servicio.getOrderServicio()>info.getIndicador5()) // si el orden del servicio es mayor al indice del servicio que estoy midificando, entonces el servicio conserba el estado actual.
										{
											servicio.setNewEstado(servicio.getEstadoServicio());
											servicio.setHabilitarServ(ConstantesBD.acronimoSi);
										}
										else // si el orden del servicio es menor o igual, se asigna el servicio que se asigno en el jsp.
										{
											servicio.setNewEstado(codigosServProg[cont]);
											if(codigosServProg[cont].equals(ConstantesIntegridadDominio.acronimoContratado)
												|| codigosServProg[cont].equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
												servicio.setHabilitarServ(ConstantesBD.acronimoSi);
											else
												servicio.setHabilitarServ(ConstantesBD.acronimoNo);
										}
										cont++;
									}
								}
								else
								{
									if(servicio.getOrderServicio()>info.getIndicador5()) // si el orden del servicio es mayor al indice del servicio que estoy midificando, entonces el servicio conserba el estado actual.
									{
										servicio.setNewEstado(servicio.getEstadoServicio());
										servicio.setHabilitarServ(ConstantesBD.acronimoSi);
									}
									else // si el orden del servicio es menor o igual, se asigna el servicio que se asigno en el jsp.
									{
										servicio.setNewEstado(codigosServProg[cont]);
										if(codigosServProg[cont].equals(ConstantesIntegridadDominio.acronimoContratado)
											|| codigosServProg[cont].equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
											servicio.setHabilitarServ(ConstantesBD.acronimoSi);
										else
											servicio.setHabilitarServ(ConstantesBD.acronimoNo);
									}
									cont++;
								}
							}
						}
						else // si el programa, entonces 
						{
							if(codigosServProg.length>cont) // si el no se han recorrido todos los servicios del programa, ingersa.
							{
								servicio.setNewEstado(codigosServProg[cont]);  //se asigna el servicio que se asigno en el jsp. 
								if(codigosServProg[cont].equals(ConstantesIntegridadDominio.acronimoContratado)
									|| codigosServProg[cont].equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
									servicio.setHabilitarServ(ConstantesBD.acronimoSi);
								else
									servicio.setHabilitarServ(ConstantesBD.acronimoNo);
							}
							else // si se recorrieron todos los servicios del programa, se sale del ciclo.
							{
								break;
							}
							cont++;
						}
						
					}
					break;
				case 3:
					//en el caso de que se quiere modificar el servicio a estado realizado interno.
					elem2.getListaServicios().get(info.getIndicador5()).setNewEstado(codigosServProg[codigosServProg.length-1]);
					if(codigosServProg[codigosServProg.length-1].equals(ConstantesIntegridadDominio.acronimoGarantia))
					{
						elem2.getListaServicios().get(info.getIndicador5()).setGarantia(ConstantesBD.acronimoSi);
						elem2.getListaServicios().get(info.getIndicador5()).setExclusion(ConstantesBD.acronimoNo);
						elem2.getListaServicios().get(info.getIndicador5()).setInclusion(ConstantesBD.acronimoNo);
					}else if(codigosServProg[codigosServProg.length-1].equals(ConstantesIntegridadDominio.acronimoRealizadoInterno)){
						//logger.info("\n\n Nuevo estado >> "+elem2.getListaServicios().get(info.getIndicador5()).getNewEstado());
						elem2.getListaServicios().get(info.getIndicador5()).setGarantia(ConstantesBD.acronimoNo);
						elem2.getListaServicios().get(info.getIndicador5()).setExclusion(ConstantesBD.acronimoNo);
						//elem2.getListaServicios().get(info.getIndicador5()).setInclusion(ConstantesBD.acronimoNo);
						elem2.getListaServicios().get(info.getIndicador5()).setHabilitarServ(ConstantesBD.acronimoNo);
						elem2.getListaServicios().get(info.getIndicador5()).getMotivoCancelacion().setCodigo("");
						elem2.getListaServicios().get(info.getIndicador5()).getMotivoCancelacion().setNombre("");
						elem2.getListaServicios().get(info.getIndicador5()).getMotivoCancelacion().setDescripcion("");
						elem2.getListaServicios().get(info.getIndicador5()).setEliminarSeleccionProxCita(ConstantesBD.acronimoSi);
					}
					break;
				default:
					break;
			}
			
		}
		//***************************************************************************
	}
	
	/**
	 * metodo que asigna el motivo cancelacion/exclusion/garantia
	 * @param info
	 */
	private void accionMotivoCanExcGar(InfoOdontograma info)
	{
		//***************************************************************************
		// motivo exclusion/cancelacion/garantia programa/servicio DETALLE/OTROS/BOCA HALLAZGOS
		InfoDetallePlanTramiento elem = new InfoDetallePlanTramiento();
		InfoHallazgoSuperficie elem1 = new InfoHallazgoSuperficie();
		InfoProgramaServicioPlan elem2 = new InfoProgramaServicioPlan();
		
		Log4JManager.info(info.getIndicador1());
		Log4JManager.info(info.getIndicador2());
		Log4JManager.info(info.getIndicador3());
		Log4JManager.info(info.getSeccionAplica());
		
		boolean asignado=false;
		
		if(info.getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoOtro))
			elem = (InfoDetallePlanTramiento) info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1());
		else if(info.getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoDetalle))
			elem = (InfoDetallePlanTramiento) info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1());
		else if(info.getSeccionAplica().equals(ConstantesIntegridadDominio.acronimoBoca))
		{
			elem1 = (InfoHallazgoSuperficie) info.getInfoPlanTrata().getSeccionHallazgosBoca().get(info.getIndicador2());
			asignado=true;
		}
		
		//XPLANNER 154553, NO SE QUE ESTABA PENSANDO EL QUE DESARROLLO ESTA COSA, indicador1, indicador2, indicador3.... 
		//LO QUE + O - ENTENDI
		//1---> PIEZA
		//2---> SUPERFICIE
		//3---> PROGRAMA
		//4---> MOTIVO
		//5---> SERVICIO
		
		if(!asignado)
		{	
			elem1 = (InfoHallazgoSuperficie) elem.getDetalleSuperficie().get(info.getIndicador2());
		}	
		
		elem2 = (InfoProgramaServicioPlan) elem1.getProgramasOservicios().get(info.getIndicador3());
		
		/*
		 * Nota * Con el indicador5 lleno quiere decir que se estab tratando de llenar el motivo de atención de un servicio del programa,
		 * pero si el campo viene vacío se refiere a solo el motivo de un programa.
		 */
		if(info.getIndicador5()==ConstantesBD.codigoNuncaValido)
		{// programa
			elem2.getMotivoCancelacion().setCodigo(info.getArrayMotivoCancelacion().get(info.getIndicador4()).getCodigoPk()+"");
			elem2.getMotivoCancelacion().setNombre(info.getArrayMotivoCancelacion().get(info.getIndicador4()).getNombre());
			elem2.getMotivoCancelacion().setDescripcion(info.getArrayMotivoCancelacion().get(info.getIndicador4()).getCodigo());
		}else{//servicio
			elem2.getListaServicios().get(info.getIndicador4()).getMotivoCancelacion().setCodigo(info.getArrayMotivoCancelacion().get(info.getIndicador5()).getCodigoPk()+"");
			elem2.getListaServicios().get(info.getIndicador4()).getMotivoCancelacion().setNombre(info.getArrayMotivoCancelacion().get(info.getIndicador5()).getNombre());
			elem2.getListaServicios().get(info.getIndicador4()).getMotivoCancelacion().setDescripcion(info.getArrayMotivoCancelacion().get(info.getIndicador5()).getCodigo());
		}
		//***************************************************************************
		
		//************************************** Programas N Superficies ***********************************Tarea 158745******
		
		for(InfoHallazgoSuperficie superficie:elem.getDetalleSuperficie())
		{
			if(superficie.getExisteBD().isActivo())
			{
				if(superficie.getHallazgoREQUERIDO().getCodigo()==elem1.getHallazgoREQUERIDO().getCodigo())
				{
					for(InfoProgramaServicioPlan prog:superficie.getProgramasOservicios())
					{
						if(prog.getExisteBD().isActivo())
						{
							if(prog.getProgHallazgoPieza().getCodigoPk()==elem2.getProgHallazgoPieza().getCodigoPk())
							{
								if(info.getIndicador5()==ConstantesBD.codigoNuncaValido)
								{// programa
									prog.getMotivoCancelacion().setCodigo(info.getArrayMotivoCancelacion().get(info.getIndicador4()).getCodigoPk()+"");
									prog.getMotivoCancelacion().setNombre(info.getArrayMotivoCancelacion().get(info.getIndicador4()).getNombre());
									prog.getMotivoCancelacion().setDescripcion(info.getArrayMotivoCancelacion().get(info.getIndicador4()).getCodigo());
									
									if(prog.getListaServicios().size()>0)
									{
										for(InfoServicios servicioInterno: prog.getListaServicios())
										{											
											servicioInterno.getMotivoCancelacion().setCodigo(info.getArrayMotivoCancelacion().get(info.getIndicador4()).getCodigoPk()+"");
											servicioInterno.getMotivoCancelacion().setNombre(info.getArrayMotivoCancelacion().get(info.getIndicador4()).getNombre());
											servicioInterno.getMotivoCancelacion().setDescripcion(info.getArrayMotivoCancelacion().get(info.getIndicador4()).getCodigo());
										}
									}
								}
							}
						}	
					}	
				}
			}
		}
		
		//******************************************************************************************************************
		
		
		
		
	}
	
	
	//************************************************************************************
	//Metodos Inclusiones
	//************************************************************************************

	
	/**
	 * Metodo para Realizar el Insert en Base de Datos de las Inclusiones
	 */
	private boolean accionGuardarInclusionesEnBD(Connection con, InfoOdontograma info)
	{
		/*PlanTratamiento planTratamiento = new PlanTratamiento();
		InfoDatosString actualizar = new InfoDatosString();*/
		String estadoProgIncl="";
		int orderServicio=ConstantesBD.codigoNuncaValido;
		double codDetalle=0.0;
		BigDecimal detalle=new BigDecimal(0);
		
		if(info.getUtilizaProgOdontIns().equals(ConstantesBD.acronimoSi))
		{	
			logger.info ("Plan Tratamiento >> "+info.getInfoPlanTrata().getCodigoPk());
			
			// Sección detalle
			for(InfoDetallePlanTramiento elem: info.getInfoPlanTrata().getSeccionHallazgosDetalle()) //seccion Detalle
			{			
				for(InfoHallazgoSuperficie elem2 : elem.getDetalleSuperficie())  // Detalle
				{	
					logger.info("Codigo Detalle Existente Otros>>"+elem2.getCodigoPkDetalle()); // ojo aca
					
					// Se realiza Insercion de Detalle ( Nueva Inclusion )
					if(Utilidades.convertirAEntero(elem2.getCodigoPkDetalle()+"")<=0)
					{	    	
						
						DtoDetallePlanTratamiento detallePlan= new DtoDetallePlanTratamiento();
						
						detallePlan.setPlanTratamiento(Utilidades.convertirADouble(info.getInfoPlanTrata().getCodigoPk()+""));
						detallePlan.setPiezaDental(elem.getPieza().getCodigo());
						detallePlan.setSuperficie(Utilidades.convertirAEntero(elem2.getSuperficieOPCIONAL().getCodigo()+""));
						detallePlan.setHallazgo(elem2.getHallazgoREQUERIDO().getCodigo());
						detallePlan.setSeccion(ConstantesIntegridadDominio.acronimoOtro);
						detallePlan.setActivo(ConstantesBD.acronimoSi);
						detallePlan.setPorConfirmar(ConstantesBD.acronimoNo);	
						
						info.getUsuarioActual().setFechaModifica(UtilidadFecha.getFechaActual());
						info.getUsuarioActual().setHoraModifica(UtilidadFecha.getHoraActual());
						
						detallePlan.setFechaUsuarioModifica(info.getUsuarioActual());
						
						//TODO OJO CON ESTE GUARDAR DETALLE ??
						codDetalle = PlanTratamiento.guardarDetalle(detallePlan , con);
						
						if(codDetalle>0)
						{
							logger.info("INSERTO DETALLE OTROS");  
							
							for(InfoProgramaServicioPlan elem3 : elem2.getProgramasOservicios()) // Programas
							{
								logger.info("Programa es inclusion >> "+elem3.getInclusion()+ " es ACTIVO >>"+elem3.getExisteBD().isActivo()+ "  Nombre >>"+elem3.getNombreProgramaServicio());
								logger.info("Estado Prog: -"+elem3.getEstadoPrograma()+"-");
								logger.info("New Estado Prog: -"+elem3.getNewEstadoProg()+"-");
								logger.info("Cod Detalle : "+codDetalle);
								
								if(elem3.getInclusion().equals(ConstantesBD.acronimoSi) && elem3.getExisteBD().isActivo())
								{								 	
									String[] arregloEstado = elem3.getEstadoPrograma().split(ConstantesBD.separadorSplit);
									
									if(arregloEstado.length>0)
									{
										estadoProgIncl= arregloEstado[0];
									}
									if((info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi) && estadoProgIncl.equals(ConstantesIntegridadDominio.acronimoPorAutorizar) )
											|| info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo) && estadoProgIncl.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente) )
									{										
										for(InfoServicios elem4: elem3.getListaServicios())
										{
											logger.info("Cod Servicio "+elem4.getServicio().getCodigo());
											DtoProgramasServiciosPlanT dtoPrograma= new DtoProgramasServiciosPlanT();	
											dtoPrograma.setCodigoPk(elem3.getCodigoPkProgramaServicio());
											InfoDatosDouble prog= new InfoDatosDouble();
											prog.setCodigo(Utilidades.convertirADouble(elem3.getCodigoPkProgramaServicio()+""));
											dtoPrograma.setPrograma(prog);
											InfoDatosInt serv = new InfoDatosInt();
											serv.setCodigo(Utilidades.convertirAEntero(elem4.getServicio().getCodigo()+""));
											dtoPrograma.setServicio(serv);
											dtoPrograma.setDetPlanTratamiento(new BigDecimal(codDetalle));
											dtoPrograma.setActivo(ConstantesBD.acronimoSi);											
											if(arregloEstado.length>0)
											{
												dtoPrograma.setEstadoPrograma(arregloEstado[0]);
												dtoPrograma.setEstadoServicio(elem4.getEstadoServicio());
											}
											
											dtoPrograma.setIndicativoPrograma(ConstantesIntegridadDominio.acronimoInicial);
											dtoPrograma.setIndicativoServicio(ConstantesIntegridadDominio.acronimoInicial);
											
											dtoPrograma.setInclusion(elem3.getInclusion());													
											dtoPrograma.setCodigoCita(new BigDecimal(info.getCodigoCita())); 
											dtoPrograma.setValoracion(new BigDecimal(info.getCodigoValoracion()));
											dtoPrograma.setEvolucion(new BigDecimal(info.getCodigoEvolucion()));
											dtoPrograma.setPorConfirmado(info.getPorConfirmar());
											
											info.getUsuarioActual().setFechaModifica(UtilidadFecha.getFechaActual());
											info.getUsuarioActual().setHoraModifica(UtilidadFecha.getHoraActual());
											
											dtoPrograma.setUsuarioModifica(info.getUsuarioActual());
											
											detalle=new BigDecimal(codDetalle);
											
											logger.info("\n\nEL VALOR DEL PROGRAMA ----->"+dtoPrograma.getCodigoPk().intValue());
											
											orderServicio=PlanTratamiento.cargarOrdenServicio(dtoPrograma.getCodigoPk().intValue(), detalle ,con, true);
											
											dtoPrograma.setOrdenServicio(orderServicio);
											
											logger.info(" SE realiza INSERT ES INCLUSION OTROS");
											if(PlanTratamiento.insertarInclusionenBD(con,dtoPrograma)<=0)
											{											
												logger.info("Ocurrio un error en la Insercion de la Inclusion Otross");
												return false;
											}	
										}
									}
									
								}
							}
						}else
						{
							logger.info("ERROR EN INSERCION DETALLE OTROS");
							return false;
						}
					}else// Se realiza actulizacion de Programa 
					{
						for(InfoProgramaServicioPlan programa : elem2.getProgramasOservicios()) // Programas
						{
							logger.info("Programa es inclusion >> "+programa.getInclusion()+ " es ACTIVO >>"+programa.getExisteBD().isActivo()+ "  Nombre >>"+programa.getNombreProgramaServicio());
							logger.info("Estado Prog: -"+programa.getEstadoPrograma()+"-");
							logger.info("New Estado Prog: -"+programa.getNewEstadoProg()+"-");
							logger.info("Cod Detalle : "+elem2.getCodigoPkDetalle());
							
							
							String[] arregloEstado = programa.getEstadoPrograma().split(ConstantesBD.separadorSplit);
							
							if(arregloEstado.length>0)
							{
								estadoProgIncl= arregloEstado[0];
							}
							if((info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi) && estadoProgIncl.equals(ConstantesIntegridadDominio.acronimoPorAutorizar) )
									|| info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo) && estadoProgIncl.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente) )
							{
								if(!programa.getInclusion().equals(""))
								{
									for(InfoServicios servicio: programa.getListaServicios())
									{
										logger.info("Cod Servicio "+servicio.getServicio().getCodigo());
										DtoProgramasServiciosPlanT dtoPrograma= new DtoProgramasServiciosPlanT();	
										dtoPrograma.setCodigoPk(programa.getCodigopk());
										dtoPrograma.setEstadoPrograma(programa.getEstadoPrograma().split(ConstantesBD.separadorSplit)[0]);
										dtoPrograma.setEstadoServicio(servicio.getEstadoServicio().split(ConstantesBD.separadorSplit)[0]);
										DtoInfoFechaUsuario usuarioModifica = new DtoInfoFechaUsuario();
										usuarioModifica.setFechaModifica(UtilidadFecha.getFechaActual());
										usuarioModifica.setHoraModifica(UtilidadFecha.getHoraActual());
										usuarioModifica.setUsuarioModifica(info.getUsuarioActual().getUsuarioModifica());
										dtoPrograma.setUsuarioModifica(usuarioModifica);
										dtoPrograma.setPorConfirmado(null);
										InfoDatosDouble prog= new InfoDatosDouble();
										prog.setCodigo(Utilidades.convertirADouble(programa.getCodigoPkProgramaServicio()+""));
										dtoPrograma.setPrograma(prog);
										InfoDatosInt serv = new InfoDatosInt();
										serv.setCodigo(Utilidades.convertirAEntero(servicio.getServicio().getCodigo()+""));
										dtoPrograma.setServicio(serv);
										dtoPrograma.setDetPlanTratamiento(elem2.getCodigoPkDetalle());
										
										if(programa.getInclusion().equals(ConstantesBD.acronimoSi) && programa.getExisteBD().isActivo())
										{	
											dtoPrograma.setInclusion(ConstantesBD.acronimoSi);
											dtoPrograma.setActivo(ConstantesBD.acronimoSi);
										}
										else
										{	
											if(programa.getInclusion().equals(ConstantesBD.acronimoNo))
											{   
												dtoPrograma.setInclusion(ConstantesBD.acronimoNo);												 
											}
										}	
										
										
										if(!PlanTratamiento.actualizarInclusionProgServPlanTr(con, dtoPrograma))
										{
											Log4JManager.error("Ocurrio un error en la Actualizacion del Programa de la Inclusion Otross");
											return false;
										}
										if(!PlanTratamiento.modicarEstadosDetalleProgServ(dtoPrograma, con))
										{
											Log4JManager.error("Ocurrio un error en la Actualizacion del Programa de la Inclusion Otross");
											return false;
										}
									}
								}
							}								  
						}
					}
				}
			}
			
			logger.info("Recorrido Seccion OTROS");		
			// Recorrido Seccion Otros	
			for(InfoDetallePlanTramiento elem: info.getInfoPlanTrata().getSeccionOtrosHallazgos()) //seccion Otros Hallazgos
			{			
				for(InfoHallazgoSuperficie elem2 : elem.getDetalleSuperficie())  // Detalle
				{	
					logger.info("Codigo Detalle Existente Otros>>"+elem2.getCodigoPkDetalle()); // ojo aca
					
					// Se realiza Insercion de Detalle ( Nueva Inclusion )
					if(Utilidades.convertirAEntero(elem2.getCodigoPkDetalle()+"")<=0)
					{	    	
						DtoDetallePlanTratamiento detallePlan= new DtoDetallePlanTratamiento();
						
						detallePlan.setPlanTratamiento(Utilidades.convertirADouble(info.getInfoPlanTrata().getCodigoPk()+""));
						detallePlan.setPiezaDental(elem.getPieza().getCodigo());
						detallePlan.setSuperficie(Utilidades.convertirAEntero(elem2.getSuperficieOPCIONAL().getCodigo()+""));
						detallePlan.setHallazgo(elem2.getHallazgoREQUERIDO().getCodigo());
						detallePlan.setSeccion(ConstantesIntegridadDominio.acronimoOtro);
						detallePlan.setActivo(ConstantesBD.acronimoSi);
						detallePlan.setPorConfirmar(ConstantesBD.acronimoNo);	
						
						info.getUsuarioActual().setFechaModifica(UtilidadFecha.getFechaActual());
						info.getUsuarioActual().setHoraModifica(UtilidadFecha.getHoraActual());
						
						detallePlan.setFechaUsuarioModifica(info.getUsuarioActual());
						
						codDetalle = PlanTratamiento.guardarDetalle(detallePlan , con);
						
						if(codDetalle>0)
						{
							logger.info("INSERTO DETALLE OTROS");  
							
							for(InfoProgramaServicioPlan elem3 : elem2.getProgramasOservicios()) // Programas
							{
								logger.info("Programa es inclusion >> "+elem3.getInclusion()+ " es ACTIVO >>"+elem3.getExisteBD().isActivo()+ "  Nombre >>"+elem3.getNombreProgramaServicio());
								logger.info("Estado Prog: -"+elem3.getEstadoPrograma()+"-");
								logger.info("New Estado Prog: -"+elem3.getNewEstadoProg()+"-");
								logger.info("Cod Detalle : "+codDetalle);
								
								if(elem3.getInclusion().equals(ConstantesBD.acronimoSi) && elem3.getExisteBD().isActivo())
								{								 	
									String[] arregloEstado = elem3.getEstadoPrograma().split(ConstantesBD.separadorSplit);
									
									if(arregloEstado.length>0)
									{
										estadoProgIncl= arregloEstado[0];
									}
									if((info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi) && estadoProgIncl.equals(ConstantesIntegridadDominio.acronimoPorAutorizar) )
											|| info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo) && estadoProgIncl.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente) )
									{									
										for(InfoServicios elem4: elem3.getListaServicios())
										{
											logger.info("Cod Servicio "+elem4.getServicio().getCodigo());
											DtoProgramasServiciosPlanT dtoPrograma= new DtoProgramasServiciosPlanT();	
											dtoPrograma.setCodigoPk(elem3.getCodigoPkProgramaServicio());
											InfoDatosDouble prog= new InfoDatosDouble();
											prog.setCodigo(Utilidades.convertirADouble(elem3.getCodigoPkProgramaServicio()+""));
											dtoPrograma.setPrograma(prog);
											InfoDatosInt serv = new InfoDatosInt();
											serv.setCodigo(Utilidades.convertirAEntero(elem4.getServicio().getCodigo()+""));
											dtoPrograma.setServicio(serv);
											dtoPrograma.setDetPlanTratamiento(new BigDecimal(codDetalle));
											dtoPrograma.setActivo(ConstantesBD.acronimoSi);											
											if(arregloEstado.length>0)
											{
												dtoPrograma.setEstadoPrograma(arregloEstado[0]);
												dtoPrograma.setEstadoServicio(elem4.getEstadoServicio());
											}
											
											dtoPrograma.setIndicativoPrograma(ConstantesIntegridadDominio.acronimoInicial);
											dtoPrograma.setIndicativoServicio(ConstantesIntegridadDominio.acronimoInicial);
											
											dtoPrograma.setInclusion(elem3.getInclusion());													
											dtoPrograma.setCodigoCita(new BigDecimal(info.getCodigoCita())); 
											dtoPrograma.setValoracion(new BigDecimal(info.getCodigoValoracion()));
											dtoPrograma.setEvolucion(new BigDecimal(info.getCodigoEvolucion()));
											dtoPrograma.setPorConfirmado(info.getPorConfirmar());
											
											info.getUsuarioActual().setFechaModifica(UtilidadFecha.getFechaActual());
											info.getUsuarioActual().setHoraModifica(UtilidadFecha.getHoraActual());
											
											dtoPrograma.setUsuarioModifica(info.getUsuarioActual());
											
											detalle=new BigDecimal(codDetalle);
											
											logger.info("\n\nEL VALOR DEL PROGRAMA ----->"+dtoPrograma.getCodigoPk().intValue());
											
											orderServicio=PlanTratamiento.cargarOrdenServicio(dtoPrograma.getCodigoPk().intValue(), detalle ,con, true);
											
											dtoPrograma.setOrdenServicio(orderServicio);
											
											logger.info(" SE realiza INSERT ES INCLUSION OTROS");
											if(PlanTratamiento.insertarInclusionenBD(con,dtoPrograma)<=0)
											{											
												logger.info("Ocurrio un error en la Insercion de la Inclusion Otross");
												return false;
											}	
										}
									}
								}
							}
						}else
						{
							logger.info("ERROR EN INSERCION DETALLE OTROS");
							return false;
						}
					}else// Se realiza actulizacion de Programa 
					{
						for(InfoProgramaServicioPlan programa : elem2.getProgramasOservicios()) // Programas
						{
							logger.info("Programa es inclusion >> "+programa.getInclusion()+ " es ACTIVO >>"+programa.getExisteBD().isActivo()+ "  Nombre >>"+programa.getNombreProgramaServicio());
							logger.info("Estado Prog: -"+programa.getEstadoPrograma()+"-");
							logger.info("New Estado Prog: -"+programa.getNewEstadoProg()+"-");
							logger.info("Cod Detalle : "+elem2.getCodigoPkDetalle());
							
							String[] arregloEstado = programa.getEstadoPrograma().split(ConstantesBD.separadorSplit);
							
							if(arregloEstado.length>0)
							{
								estadoProgIncl= arregloEstado[0];
							}
							if((info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi) && estadoProgIncl.equals(ConstantesIntegridadDominio.acronimoPorAutorizar) )
									|| info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo) && estadoProgIncl.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente) )
							{
								if(!programa.getInclusion().equals(""))
								{
									for(InfoServicios servicio: programa.getListaServicios())
									{
										logger.info("Cod Servicio "+servicio.getServicio().getCodigo());
										DtoProgramasServiciosPlanT dtoPrograma= new DtoProgramasServiciosPlanT();	
										dtoPrograma.setCodigoPk(programa.getCodigopk());
										dtoPrograma.setEstadoPrograma(programa.getEstadoPrograma().split(ConstantesBD.separadorSplit)[0]);
										dtoPrograma.setEstadoServicio(servicio.getEstadoServicio().split(ConstantesBD.separadorSplit)[0]);
										DtoInfoFechaUsuario usuarioModifica = new DtoInfoFechaUsuario();
										usuarioModifica.setFechaModifica(UtilidadFecha.getFechaActual());
										usuarioModifica.setHoraModifica(UtilidadFecha.getHoraActual());
										usuarioModifica.setUsuarioModifica(info.getUsuarioActual().getUsuarioModifica());
										dtoPrograma.setUsuarioModifica(usuarioModifica);
										dtoPrograma.setPorConfirmado(null);
										InfoDatosDouble prog= new InfoDatosDouble();
										prog.setCodigo(Utilidades.convertirADouble(programa.getCodigoPkProgramaServicio()+""));
										dtoPrograma.setPrograma(prog);
										InfoDatosInt serv = new InfoDatosInt();
										serv.setCodigo(Utilidades.convertirAEntero(servicio.getServicio().getCodigo()+""));
										dtoPrograma.setServicio(serv);
										dtoPrograma.setDetPlanTratamiento(elem2.getCodigoPkDetalle());
										
										if(programa.getInclusion().equals(ConstantesBD.acronimoSi) && programa.getExisteBD().isActivo())
										{	
											dtoPrograma.setInclusion(ConstantesBD.acronimoSi);
											dtoPrograma.setActivo(ConstantesBD.acronimoSi);
										}
										else
										{	
											if(programa.getInclusion().equals(ConstantesBD.acronimoNo))
											{   
												dtoPrograma.setInclusion(ConstantesBD.acronimoNo);												 
											}
										}	
										if(!PlanTratamiento.actualizarInclusionProgServPlanTr(con, dtoPrograma))
										{
											logger.info("Ocurrio un error en la Actualizacion del Programa de la Inclusion Otross");
											return false;
										}
										if(!PlanTratamiento.modicarEstadosDetalleProgServ(dtoPrograma, con))
										{
											Log4JManager.error("Ocurrio un error en la Actualizacion del Programa de la Inclusion Otross");
											return false;
										}
									}
								}
							}								  
						}
					}
				}
			}
			
			logger.info("Recorrido Seccion BOCA");
			// Recorrido Seccion Boca
			for(InfoHallazgoSuperficie elem: info.getInfoPlanTrata().getSeccionHallazgosBoca()) //seccion Hallazgos Boca
			{	
				logger.info("Codigo Detalle Existente Boca>>"+elem.getCodigoPkDetalle()); // ojo aca
				// Inserta Detalle por Inclusion Nueva
				if(Utilidades.convertirAEntero(elem.getCodigoPkDetalle()+"")<=0)
				{
					DtoDetallePlanTratamiento detallePlan= new DtoDetallePlanTratamiento();
					
					detallePlan.setPlanTratamiento(Utilidades.convertirADouble(info.getInfoPlanTrata().getCodigoPk()+""));
					detallePlan.setSuperficie(Utilidades.convertirAEntero(elem.getSuperficieOPCIONAL().getCodigo()+""));
					detallePlan.setHallazgo(elem.getHallazgoREQUERIDO().getCodigo());
					detallePlan.setSeccion(ConstantesIntegridadDominio.acronimoBoca);
					detallePlan.setActivo(ConstantesBD.acronimoSi);
					detallePlan.setPorConfirmar(ConstantesBD.acronimoNo);	
					
					info.getUsuarioActual().setFechaModifica(UtilidadFecha.getFechaActual());
					info.getUsuarioActual().setHoraModifica(UtilidadFecha.getHoraActual());
					
					detallePlan.setFechaUsuarioModifica(info.getUsuarioActual());
					
					codDetalle = PlanTratamiento.guardarDetalle(detallePlan , con);
					
					if(codDetalle>0)
					{
						logger.info("INSERTO DETALLE BOCA"); 
						for(InfoProgramaServicioPlan elem2 : elem.getProgramasOservicios()) // Programas
						{
							logger.info("NUEVA : ");
							logger.info("Programa es inclusion >> "+elem2.getInclusion()+ "es ACTIVO >>"+elem2.getExisteBD().isActivo() + "  Nombre >>"+elem2.getNombreProgramaServicio());
							logger.info("Estado Prog: -"+elem2.getEstadoPrograma()+"-");
							logger.info("New Estado Prog: -"+elem2.getNewEstadoProg()+"-");
							logger.info("Programa Servicio "+ elem2.getCodigoPkProgramaServicio());
							logger.info("Cod Detalle "+codDetalle); 
							
							
							if(elem2.getInclusion().equals(ConstantesBD.acronimoSi) && elem2.getExisteBD().isActivo())
							{
								String[] arregloEstado = elem2.getEstadoPrograma().split(ConstantesBD.separadorSplit);
								
								if(arregloEstado.length>0)
								{
									estadoProgIncl= arregloEstado[0];
								}
								if((info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi) && estadoProgIncl.equals(ConstantesIntegridadDominio.acronimoPorAutorizar) )
										|| info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo) && estadoProgIncl.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente) )
								{
									for(InfoServicios elem3: elem2.getListaServicios())
									{									
										DtoProgramasServiciosPlanT dtoPrograma= new DtoProgramasServiciosPlanT();	
										dtoPrograma.setCodigoPk(elem2.getCodigoPkProgramaServicio());
										InfoDatosDouble prog= new InfoDatosDouble();
										prog.setCodigo(Utilidades.convertirADouble(elem2.getCodigoPkProgramaServicio()+""));
										dtoPrograma.setPrograma(prog);
										dtoPrograma.setDetPlanTratamiento(new BigDecimal(codDetalle));
										InfoDatosInt serv = new InfoDatosInt();
										serv.setCodigo(Utilidades.convertirAEntero(elem3.getServicio().getCodigo()+""));
										dtoPrograma.setServicio(serv);
										
										dtoPrograma.setActivo(ConstantesBD.acronimoSi);
										
										if(arregloEstado.length>0)
										{
											dtoPrograma.setEstadoPrograma(arregloEstado[0]);
											dtoPrograma.setEstadoServicio(elem3.getEstadoServicio());
										}
										dtoPrograma.setIndicativoPrograma(ConstantesIntegridadDominio.acronimoInicial);
										dtoPrograma.setIndicativoServicio(ConstantesIntegridadDominio.acronimoInicial);
										
										dtoPrograma.setInclusion(elem2.getInclusion());													
										dtoPrograma.setCodigoCita(new BigDecimal(info.getCodigoCita()));
										dtoPrograma.setValoracion(new BigDecimal(info.getCodigoValoracion()));
										dtoPrograma.setEvolucion(new BigDecimal(info.getCodigoEvolucion()));
										dtoPrograma.setPorConfirmado(info.getPorConfirmar());
										
										info.getUsuarioActual().setFechaModifica(UtilidadFecha.getFechaActual());
										info.getUsuarioActual().setHoraModifica(UtilidadFecha.getHoraActual());
										dtoPrograma.setUsuarioModifica(info.getUsuarioActual());
										
										logger.info(" SE realiza INSERT ES INCLUSION BOCA");
										if(PlanTratamiento.insertarInclusionenBD(con,dtoPrograma)<=0)
										{ 					
											logger.info("Ocurrio un error en la Insercion de la Inclusion Boca");
											return false;
										}
									}
								}
							}
						}						  
					}
					else
					{
						logger.info("ERROR EN INSERCION DETALLE BOCA"); 
						return false;
					}
				}
				else
				{
					//Actualiza Programa en Detalle Existente (Inclusion = S )
					for(InfoProgramaServicioPlan programa : elem.getProgramasOservicios()) // Programas
					{
						logger.info("ACTUALIZACION : ");
						logger.info("Programa es inclusion >> "+programa.getInclusion()+ " es ACTIVO >>"+programa.getExisteBD().isActivo()+ "  Nombre >>"+programa.getNombreProgramaServicio());
						logger.info("Estado Prog: -"+programa.getEstadoPrograma()+"-");
						logger.info("New Estado Prog: -"+programa.getNewEstadoProg()+"-");
						logger.info("Cod Detalle : "+elem.getCodigoPkDetalle());
						
						
						String[] arregloEstado = programa.getEstadoPrograma().split(ConstantesBD.separadorSplit);
						
						if(arregloEstado.length>0)
						{
							estadoProgIncl= arregloEstado[0];
						}
						
						if((info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi) && estadoProgIncl.equals(ConstantesIntegridadDominio.acronimoPorAutorizar) )
								|| (info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo) && estadoProgIncl.equals(ConstantesIntegridadDominio.acronimoEstadoPendiente)) )
						{
							if(!programa.getInclusion().equals(""))
							{  
								/*
								 * ACTUALIZAR PLAN DE TRATAMIENTO
								 */
								for(InfoServicios servicio: programa.getListaServicios())
								{
									logger.info("Cod Servicio "+servicio.getServicio().getCodigo());
									DtoProgramasServiciosPlanT dtoPrograma= new DtoProgramasServiciosPlanT();	
									dtoPrograma.setEspecialidad(new InfoDatosInt(info.getEspecialidad()));
									dtoPrograma.setEstadoPrograma(programa.getEstadoPrograma().split(ConstantesBD.separadorSplit)[0]);
									dtoPrograma.setEstadoServicio(servicio.getEstadoServicio().split(ConstantesBD.separadorSplit)[0]);
									DtoInfoFechaUsuario usuarioModifica = new DtoInfoFechaUsuario();
									usuarioModifica.setFechaModifica(UtilidadFecha.getFechaActual());
									usuarioModifica.setHoraModifica(UtilidadFecha.getHoraActual());
									usuarioModifica.setUsuarioModifica(info.getUsuarioActual().getUsuarioModifica());
									dtoPrograma.setUsuarioModifica(usuarioModifica);
									dtoPrograma.setPorConfirmado(null);
									dtoPrograma.setCodigoPk(programa.getCodigopk());
									InfoDatosDouble prog= new InfoDatosDouble();
									prog.setCodigo(Utilidades.convertirADouble(programa.getCodigoPkProgramaServicio()+""));
									dtoPrograma.setPrograma(prog);
									InfoDatosInt serv = new InfoDatosInt();
									serv.setCodigo(Utilidades.convertirAEntero(servicio.getServicio().getCodigo()+""));
									dtoPrograma.setServicio(serv);
									dtoPrograma.setDetPlanTratamiento(elem.getCodigoPkDetalle());
									
									if(programa.getInclusion().equals(ConstantesBD.acronimoSi) && programa.getExisteBD().isActivo())
									{
										dtoPrograma.setInclusion(ConstantesBD.acronimoSi);
										dtoPrograma.setActivo(ConstantesBD.acronimoSi);
									}
									else
									{
										if(programa.getInclusion().equals(ConstantesBD.acronimoNo))
										{
											dtoPrograma.setInclusion(ConstantesBD.acronimoNo);												   
										}
									}
									
									if(!PlanTratamiento.actualizarInclusionProgServPlanTr(con, dtoPrograma))
									{
										logger.info("Ocurrio un error en la Actuaizacion de la Inclusion Boca");
										return false;
									}
									if(!PlanTratamiento.modicarEstadosDetalleProgServ(dtoPrograma, con))
									{
										Log4JManager.error("Ocurrio un error en la Actualizacion del Programa de la Inclusion Boca");
										return false;
									}
								}
							}
						}					     
					}
				}		    
			}
		}
		
		return true;
	}
	
	
	/**
	 * Metodo para guardar una nueva Inclusion en la estructura del plan de tratamiento ( en Memoria ) 
	 */
	private InfoOdontograma accionGuardarNuevaInclusion(InfoOdontograma info,String acronimoSeccion) 
	{		
		if(info.getNuevaInclusion().getDetalleSuperficie().size()>0)
		{
			if(acronimoSeccion.equals(ConstantesIntegridadDominio.acronimoBoca))	
			{
				if(!existeInclusion(info, ConstantesIntegridadDominio.acronimoBoca))
				{
					if(info.getNuevaInclusion().getDetalleSuperficie().get(0).getNumProgramasServiciosActivos() > 0)
					{
						info.getInfoPlanTrata().getSeccionHallazgosBoca().add(info.getNuevaInclusion().getDetalleSuperficie().get(0));
					}	    
					else
					{
						this.adicionarError("errors.required", "Programa/Servicio");
						logger.info("NO inserta por que no Programas Asociados");
					}
				}
				else
				{
					this.adicionarError("errors.notEspecific", "No se ingresó Inclusión ...Ya existe  ");
					logger.info("\n\n ********* EXISTE INCLUSION BOCA ********");
				}	
			}	
			else
			{
				if(acronimoSeccion.equals(ConstantesIntegridadDominio.acronimoOtro))
				{
					if(!existeInclusion(info, ConstantesIntegridadDominio.acronimoOtro)) 
					{
						if(info.getNuevaInclusion().getDetalleSuperficie().get(0).getNumProgramasServiciosActivos() > 0)
						{
							info.getInfoPlanTrata().getSeccionOtrosHallazgos().add(info.getNuevaInclusion());
						} 
						else
						{
							this.adicionarError("errors.required", "Programa/Servicio");
							logger.info("NO inserta por que no Programas Asociados");
						} 
					} 
					else
					{
						this.adicionarError("errors.notEspecific", "No se ingresó Inclusión ...Ya existe  ");
						logger.info("\n\n ********* EXISTE INCLUSION OTRO ********");
					}	
				}
			}
		}
		else
		{
			this.adicionarError("errors.notEspecific", "Debe ingresar Datos de la Nueva Inclusión ...  ");
		}
			
		if(this.errores.isEmpty())
		{
			this.estadoInterno = "IngresoExitoInclusion";
		}	
			
		return info;
	}
	
	
	/**
	 * Adiciona un nuevo hallazgo boca
	 * @param  InfoOdontograma info 
	 * */
	public InfoOdontograma accionNuevoHallazgoBoca(InfoOdontograma info)
	{
		InfoHallazgoSuperficie nuevo = new InfoHallazgoSuperficie();
		nuevo.getExisteBD().setEstaBD(false);
		nuevo.getExisteBD().setActivo(true);
		info.getNuevaInclusion().getDetalleSuperficie().add(nuevo);		
		info.getInfoPlanTrata().getSeccionHallazgosBoca().add(info.getNuevaInclusion().getDetalleSuperficie().get(0));
		return info;
	}
	
	/**
	 * Adiciona una nuevo otro hallazgo 
	 * @param  InfoOdontograma info
	 * */
	public InfoOdontograma accionNuevoOtroHallazgo(InfoOdontograma info)
	{
				
		InfoHallazgoSuperficie nuevoHallazgo = new InfoHallazgoSuperficie();
		info.getNuevaInclusion().getDetalleSuperficie().add(nuevoHallazgo);
		
		info.getInfoPlanTrata().getSeccionOtrosHallazgos().add(info.getNuevaInclusion());
		return info;
	}	
	
	/**
	 * Metodo para Buscar los porgramas asociados a un Hallazgo
	 * @param info
	 * @param seccion
	 */
	private void accionAbrirBusquedaOdo(InfoOdontograma info,String seccion) {
		logger.info("SECCION >>"+seccion);
		logger.info("Nueva inclusion posDetalleSuper>>"+info.getIndicador2());
		String codigoTarifarioServicios= ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(info.getInstitucion());
		logger.info("\n Codigo Tarifario accionAbrirBusquedaOdo >>"+codigoTarifarioServicios);
		
		if(seccion.equals(ConstantesIntegridadDominio.acronimoOtro))
		{
			//Carga la información de la pieza/hallazgo/superficie
			info.getInfoGeneral().setCodigo(info.getNuevaInclusion().getPieza().getCodigo()+"");
			info.getInfoGeneral().setDescripcion(info.getNuevaInclusion().getDetalleSuperficie().get(info.getIndicador2()).getHallazgoREQUERIDO().getNombre());
			info.getInfoGeneral().setNombre(info.getNuevaInclusion().getDetalleSuperficie().get(info.getIndicador2()).getSuperficieOPCIONAL().getNombre());

			
			if(info.getNuevaInclusion().getDetalleSuperficie().get(info.getIndicador2()).getHallazgoREQUERIDO().getCodigo2() == ComponenteOdontogramaEvo.codigoTipoHallazgoDiente)
			{
				//Numero de Superficies con el mismo hallazgo dentro del mismo diente
				info.getInfoGeneral().setValor(new BigDecimal(ConstantesBD.codigoNuncaValido));
				//Indica si que la busqueda proviene de un hallazgo de diente
				info.getInfoGeneral().setIndicador(true);
			}
			else
			{		
				//Numero de Superficies con el mismo hallazgo dentro del mismo diente
				info.getInfoGeneral().setValor(new BigDecimal(info.getNuevaInclusion().getNumeroSuperParaHallazgo(info.getIndicador3())));
				//Indica si que la busqueda proviene de un hallazgo de diente
				info.getInfoGeneral().setIndicador(false);
			}
		}
		else if(seccion.equals(ConstantesIntegridadDominio.acronimoBoca))
		{
			//Carga la información de la pieza/hallazgo/superficie
			info.getInfoGeneral().setDescripcion(info.getNuevaInclusion().getDetalleSuperficie().get(info.getIndicador2()).getHallazgoREQUERIDO().getNombre());
			
			//Indica si que la busqueda proviene de un hallazgo de diente
			info.getInfoGeneral().setIndicador(true);
			
			//Numero de Superficies con el mismo hallazgo dentro del mismo diente
			info.getInfoGeneral().setValor(new BigDecimal(ConstantesBD.codigoNuncaValido));
		}
		
		DtoProgramasServiciosPlanT parametros = new DtoProgramasServiciosPlanT();
		parametros.setBuscarProgramas(info.getEsBuscarPorPrograma());
		parametros.setCodigoHallazgo(info.getIndicador3());	
		parametros.setCodigoTarifario(codigoTarifarioServicios);
		
		info.setArrayProgServiPlanT(new ArrayList<InfoProgramaServicioPlan>());
		info.setArrayProgServiPlanT(PlanTratamiento.obtenerListadoProgramasServicio(parametros));
	}
	
	/**
	 * Metodo para inicializar una Nueva Inclusion
	 * @param info
	 * @return
	 */
	private InfoOdontograma accionEmpezarNuevaInclusion(InfoOdontograma info) {
		
		/**
		 * info.getIndicador1()= contPieza
		 * info.getIndicador2()= contSuperficie
		 * info.getIndicador3()= contProgramas
		 * info.getIndicador4()= contListServicio 
		 */			
			info.setArrayDientesUsados(ComponenteOdontograma.llenarDienteUsados(info));
			InfoDetallePlanTramiento nuevo = new InfoDetallePlanTramiento();
			nuevo.getExisteBD().setEstaBD(false);
			nuevo.getExisteBD().setActivo(true);
			info.setNuevaInclusion(nuevo);			
			
			return info;
	}
	
	/**
	 * Metodo para adicionar un Hallazgo OTRO a una Inclusion
	 * @param info
	 * @return
	 */
	public InfoOdontograma accionAddHallazgoOtro(InfoOdontograma info)
	{
		logger.info("ENTRA ha adicionar ");
		String codigoTarifarioServicios= ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(info.getInstitucion());
		
		// info.getIndicador2() >> tipo de hallazgo (boca o superficie) 
		if(info.getIndicador1() >= 0 && 
				info.getIndicador2() >= 0 && 
					info.getIndicador3() >= 0)
		{
			 
			 
			InfoDetallePlanTramiento diente = info.getNuevaInclusion();
			diente.resetDetalleSuperficie();
			
			InfoHallazgoSuperficie hallazgo = new InfoHallazgoSuperficie();
			hallazgo.getHallazgoREQUERIDO().setCodigo(info.getIndicador3());
			hallazgo.getHallazgoREQUERIDO().setNombre(info.obtenerNombreHallazgo(info.getIndicador2(),info.getIndicador3()));
			
			hallazgo.getHallazgoREQUERIDO().setCodigo2(info.getIndicador2());
			hallazgo.getInfoRegistroHallazgo().setFechaModifica(UtilidadFecha.getFechaActual());
			hallazgo.getInfoRegistroHallazgo().setHoraModifica(UtilidadFecha.getHoraActual());
			
			//Carga el Programa o Servicios parametrizados por defecto para el Hallazgo
			DtoProgramasServiciosPlanT parametros = new DtoProgramasServiciosPlanT();
			parametros.setCodigoHallazgo(info.getIndicador3());
			parametros.setBuscarProgramas(info.getEsBuscarPorPrograma());
			parametros.setCodigoTarifario(codigoTarifarioServicios);
			
			InfoProgramaServicioPlan progsev = PlanTratamiento.obtenerProgramaServicioParamHallazgo(parametros);
				progsev.getExisteBD().setActivo(true);
				progsev.getExisteBD().setValue(ConstantesBD.acronimoNo);
				progsev.setInclusion(ConstantesBD.acronimoSi);
			
			if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi))
			{
				progsev.setEstadoPrograma(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoInclusion);
				progsev.setNewEstadoProg(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoInclusion);
			    progsev.setInactivarProg(ConstantesBD.acronimoSi);
			}	
			else
			{
				progsev.setEstadoPrograma(ConstantesIntegridadDominio.acronimoEstadoPendiente);	
				progsev.setNewEstadoProg(ConstantesIntegridadDominio.acronimoEstadoPendiente);
			}	
				
			if(progsev.getCodigoPkProgramaServicio().intValue()>0)
			   {
				progsev.setListaServicios(PlanTratamiento.cargarServiciosParamPrograma(progsev.getCodigoPkProgramaServicio().intValue(),codigoTarifarioServicios ));
				
				logger.info("OTROS size Servicios Programa1: "+progsev.getListaServicios().size());
				for(InfoServicios elem: progsev.getListaServicios())
				{
					logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
					logger.info("Codigo Servicio: "+elem.getServicio().getCodigo());
					logger.info("Servicio: "+elem.getServicio().getNombre());					
					elem.setInclusion(ConstantesBD.acronimoSi);
					if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi))
					{
					   elem.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
					   elem.setInactivarServ(ConstantesBD.acronimoSi);
					}else
					{
						elem.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);	
						elem.setArrayPosEstServ(llenadoPosiblesEstados(
								elem.getEstadoServicio(), 
								progsev.getListaServicios(),
								"",
								info.getUtilizaProgOdontIns(), 
								info.getValidaPresuOdontCont(),								
								info.getInstRegistraAtenExt(), 
								progsev.getEstadoPrograma(), 
								info.getCodigoCita(), 
								ConstantesIntegridadDominio.acronimoMostrarServicios,
								ConstantesBD.codigoNuncaValido, 
								progsev.getCodigoPkProgramaServicio().intValue(),
								elem.getServicio().getCodigo(),
								elem.getPorConfirmar(),
								elem.getInclusion(),
								elem.getGarantia()));
						
						
						//elem.getArrayPosEstServ().add(new InfoDatosString(ConstantesIntegridadDominio.acronimoEstadoPendiente,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoEstadoPendiente).toString()));
					}
					logger.info("ARRAY Posibles estados SERVICIO SIZE >> "+elem.getArrayPosEstServ().size());
					logger.info("Inclusion Servicio: "+elem.getInclusion());	
					logger.info("New Inclusion Servicio: "+elem.getInclusion());
					logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
				
				}
				// Cambio 19 Marzo 2010 
				
				/*if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo))
				{		
				   progsev.setArrayPosEstProg(llenadoPosiblesEstados(
						"", 
						progsev.getListaServicios(), 
						"", 
						info.getUtilizaProgOdontIns(), 
						info.getValidaPresuOdontCont(),
						info.getInstRegistraAtenExt(), 
						progsev.getEstadoPrograma(), 
						info.getCodigoCita(), 
						ConstantesIntegridadDominio.acronimoMostrarProgramas, 
						ConstantesBD.codigoNuncaValido, 
						progsev.getCodigoPkProgramaServicio().intValue(), 
						ConstantesBD.codigoNuncaValido,
						progsev.getPorConfirmar(),
						progsev.getInclusion(),
						progsev.getGarantia()));
				}*/
				
				
				hallazgo.getProgramasOservicios().add(progsev);
			   }
			else
				logger.info("..:no encontro programa/servicio para el hallazgo > "+info.getIndicador2());
			
			diente.setArraySuperficiesDiente(cargarSuperficiesDiente(info.getArraySuperficies(),info.getNuevaInclusion().getPieza().getCodigo()));
			
			diente.getDetalleSuperficie().add(hallazgo);
			
			
			logger.info(" tamaño array DETALLE Otro>>"+diente.getDetalleSuperficie().size());
		}
		
		return info;
	}
	
	
	/**
	 * Metodo para adicionar un Hallazgo Boca a una Inclusion
	 * @param info
	 * @return
	 */
	public InfoOdontograma accionAddHallazgoBoca(InfoOdontograma info)
	{
		String codigoTarifarioServicios= ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(info.getInstitucion());
		
		if(info.getIndicador1() >= 0 && 
				info.getIndicador2() >= 0 && 
					info.getIndicador3() >= 0)
		{
			InfoHallazgoSuperficie hallazgo = new InfoHallazgoSuperficie();
			info.getNuevaInclusion().resetDetalleSuperficie();
			
			hallazgo.getHallazgoREQUERIDO().setCodigo(info.getIndicador3());
			hallazgo.getHallazgoREQUERIDO().setNombre(info.obtenerNombreHallazgo(info.getIndicador2(),info.getIndicador3()));
			
			logger.info("Nombre hallazgo Boca>>"+hallazgo.getHallazgoREQUERIDO().getNombre());
			hallazgo.getHallazgoREQUERIDO().setCodigo2(info.getIndicador2());
			hallazgo.getInfoRegistroHallazgo().setFechaModifica(UtilidadFecha.getFechaActual());
			hallazgo.getInfoRegistroHallazgo().setHoraModifica(UtilidadFecha.getHoraActual());
			
			//Carga el Programa o Servicios parametrizados por defecto para el Hallazgo
			DtoProgramasServiciosPlanT parametros = new DtoProgramasServiciosPlanT();
			parametros.setCodigoHallazgo(info.getIndicador3());
			parametros.setBuscarProgramas(info.getEsBuscarPorPrograma());
			parametros.setCodigoTarifario(codigoTarifarioServicios);
			
			InfoProgramaServicioPlan progsev = PlanTratamiento.obtenerProgramaServicioParamHallazgo(parametros);
			
			progsev.getExisteBD().setActivo(true);
			progsev.getExisteBD().setValue(ConstantesBD.acronimoNo);
			progsev.setInclusion(ConstantesBD.acronimoSi);
			
			if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi)){
				progsev.setEstadoPrograma(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoInclusion);
				progsev.setNewEstadoProg(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoInclusion);				
				progsev.setInactivarProg(ConstantesBD.acronimoSi);
			}else
			{
				progsev.setEstadoPrograma(ConstantesIntegridadDominio.acronimoEstadoPendiente);
				progsev.setNewEstadoProg(ConstantesIntegridadDominio.acronimoEstadoPendiente);
			}
			
			if(progsev.getCodigoPkProgramaServicio().intValue()>0)
			{
                progsev.setListaServicios(PlanTratamiento.cargarServiciosParamPrograma(progsev.getCodigoPkProgramaServicio().intValue(), codigoTarifarioServicios));
				
				logger.info("BOCA size Servicios Programa1: "+progsev.getListaServicios().size());
				for(InfoServicios elem: progsev.getListaServicios())
				{
					logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
					logger.info("Codigo Servicio: "+elem.getServicio().getCodigo());
					logger.info("Servicio: "+elem.getServicio().getNombre());
					if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi))
					  {
						   elem.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
						   elem.setInactivarServ(ConstantesBD.acronimoSi);
					  }
					  else
					  {
						elem.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);  
						elem.setArrayPosEstServ(llenadoPosiblesEstados(
								elem.getEstadoServicio(), 
								progsev.getListaServicios(),
								"",
								info.getUtilizaProgOdontIns(), 
								info.getValidaPresuOdontCont(),
								info.getInstRegistraAtenExt(), 
								progsev.getEstadoPrograma(), 
								info.getCodigoCita(), 
								ConstantesIntegridadDominio.acronimoMostrarServicios,
								ConstantesBD.codigoNuncaValido, 
								progsev.getCodigoPkProgramaServicio().intValue(),
								elem.getServicio().getCodigo(),
								elem.getPorConfirmar(),
								elem.getInclusion(),
								elem.getGarantia()));
					}
					
					logger.info("Inclusion Servicio: "+elem.getInclusion());
					elem.setInclusion(ConstantesBD.acronimoSi);
					logger.info("New Inclusion Servicio: "+elem.getInclusion());
					logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
				}
				
				// Cambio 19 abril 2010 
				/*if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo))
				{		
				   progsev.setArrayPosEstProg(llenadoPosiblesEstados(
						"", 
						progsev.getListaServicios(), 
						"", 
						info.getUtilizaProgOdontIns(), 
						info.getValidaPresuOdontCont(),
						info.getInstRegistraAtenExt(), 
						progsev.getEstadoPrograma(), 
						info.getCodigoCita(), 
						ConstantesIntegridadDominio.acronimoMostrarProgramas, 
						ConstantesBD.codigoNuncaValido, 
						progsev.getCodigoPkProgramaServicio().intValue(), 
						ConstantesBD.codigoNuncaValido,
						progsev.getPorConfirmar(),
						progsev.getInclusion(),
						progsev.getGarantia()));
				}*/
				
				hallazgo.getProgramasOservicios().add(progsev);
			}	
			else
				logger.info("..:no encontro programa/  para el hallazgo > "+info.getIndicador2());
			
						
			info.getNuevaInclusion().getDetalleSuperficie().add(hallazgo);
			 
		
			logger.info(" tamaño array DETALLE Boca>>"+info.getNuevaInclusion().getDetalleSuperficie().size());
		}
		
		
		return info;
	}

	/**
	 * Metodo para adicionar un Programa/Servicio a una Inclusion
	 * @param info
	 * @param seccion
	 * @return
	 */
	private boolean accionAddServPlanTr(InfoOdontograma info,String seccion)
	{
		logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		logger.info("Adicionar SERVICIO");
		logger.info("Indicador: "+info.getIndicador1());
		logger.info("Seccion: "+seccion);
		logger.info("Tamaño Arreglo: "+info.getArrayProgServiPlanT().size());
		logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		BigDecimal codigoPkPrgServ = new BigDecimal(info.getArrayProgServiPlanT().get(info.getIndicador1()).getCodigoPkProgramaServicio().intValue());
	    String codigoTarifarioServicios= ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(info.getInstitucion());
		
	    if(seccion.equals(ConstantesIntegridadDominio.acronimoOtro))
		{
			logger.info("valores "+info.getArrayProgServiPlanT().get(info.getIndicador1()).getNumeroSuperficies());
			//Verifica el numero de superficies que posee el programa/servicio
			if(info.getArrayProgServiPlanT().get(info.getIndicador1()).getNumeroSuperficies() == 1 
					|| info.getInfoGeneral().isIndicador())
			{	
				//Carga la información del programa/servicio
				InfoProgramaServicioPlan nuevoProgServ = new InfoProgramaServicioPlan();
				DtoProgramasServiciosPlanT parametros = new DtoProgramasServiciosPlanT();
				parametros.setPrograma(new InfoDatosDouble(codigoPkPrgServ.doubleValue(),""));
				parametros.setCodigoTarifario(codigoTarifarioServicios);
				
				nuevoProgServ = PlanTratamiento.obtenerInfoProgramaServicios(parametros);
				nuevoProgServ.setNombreProgramaServicio(nuevoProgServ.getCodigoAmostrar()+" "+nuevoProgServ.getNombreProgramaServicio());
				
				nuevoProgServ.getExisteBD().setActivo(true);
				nuevoProgServ.getExisteBD().setValue(ConstantesBD.acronimoNo);
				nuevoProgServ.setInclusion(ConstantesBD.acronimoSi);
				
				nuevoProgServ.setListaServicios(PlanTratamiento.cargarServiciosParamPrograma(Utilidades.convertirAEntero(codigoPkPrgServ.toString()+""),codigoTarifarioServicios));
				
				if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi))
				{
					nuevoProgServ.setEstadoPrograma(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoInclusion);
					nuevoProgServ.setNewEstadoProg(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoInclusion);
					nuevoProgServ.setInactivarProg(ConstantesBD.acronimoSi); 
				}	
				else
				{
					nuevoProgServ.setEstadoPrograma(ConstantesIntegridadDominio.acronimoEstadoPendiente);	
					nuevoProgServ.setNewEstadoProg(ConstantesIntegridadDominio.acronimoEstadoPendiente);
					
					nuevoProgServ.setArrayPosEstProg(llenadoPosiblesEstados(
							"", 
							nuevoProgServ.getListaServicios(), 
							"", 
							info.getUtilizaProgOdontIns(), 
							info.getValidaPresuOdontCont(),
							info.getInstRegistraAtenExt(), 
							nuevoProgServ.getEstadoPrograma(), 
							info.getCodigoCita(), 
							ConstantesIntegridadDominio.acronimoMostrarProgramas, 
							ConstantesBD.codigoNuncaValido, 
							nuevoProgServ.getCodigoPkProgramaServicio().intValue(), 
							ConstantesBD.codigoNuncaValido,
							nuevoProgServ.getPorConfirmar(),
							nuevoProgServ.getInclusion(),
							nuevoProgServ.getGarantia()));
				}	
				
				
				
				logger.info("size Servicios Programa: "+nuevoProgServ.getListaServicios().size());
				for(InfoServicios elem: nuevoProgServ.getListaServicios())
				{
					logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
					logger.info("Codigo Servicio: "+elem.getServicio().getCodigo());
					logger.info("Servicio: "+elem.getServicio().getNombre());
					if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi))
					{
						   elem.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
						   elem.setInactivarServ(ConstantesBD.acronimoSi);
					}	
					else
					{
						elem.setArrayPosEstServ(llenadoPosiblesEstados(
								elem.getEstadoServicio(), 
								nuevoProgServ.getListaServicios(),
								"",
								info.getUtilizaProgOdontIns(), 
								info.getValidaPresuOdontCont(),
								info.getInstRegistraAtenExt(), 
								nuevoProgServ.getEstadoPrograma(), 
								info.getCodigoCita(), 
								ConstantesIntegridadDominio.acronimoMostrarServicios,
								ConstantesBD.codigoNuncaValido, 
								nuevoProgServ.getCodigoPkProgramaServicio().intValue(),
								elem.getServicio().getCodigo(),
								elem.getPorConfirmar(),
								elem.getInclusion(),
								elem.getGarantia()));
					}
					logger.info("Inclusion Servicio: "+elem.getInclusion());
					elem.setInclusion(ConstantesBD.acronimoSi);
					logger.info("New Inclusion Servicio: "+elem.getInclusion());
					logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
				}
						
				 
				info.getNuevaInclusion().getDetalleSuperficie().get(0).getProgramasOservicios().add(nuevoProgServ);
				logger.info("añadio Otros");
			}
		}
		
		else if(seccion.equals(ConstantesIntegridadDominio.acronimoBoca))
		{
			//Carga la información del programa/servicio
			InfoProgramaServicioPlan nuevoProgServ = new InfoProgramaServicioPlan();
			DtoProgramasServiciosPlanT parametros = new DtoProgramasServiciosPlanT();
			parametros.setPrograma(new InfoDatosDouble(codigoPkPrgServ.doubleValue(),""));
			parametros.setCodigoTarifario(codigoTarifarioServicios);
			
			nuevoProgServ = PlanTratamiento.obtenerInfoProgramaServicios(parametros);
			nuevoProgServ.setNombreProgramaServicio(nuevoProgServ.getCodigoAmostrar()+" "+nuevoProgServ.getNombreProgramaServicio());
			nuevoProgServ.getExisteBD().setActivo(true);
			nuevoProgServ.getExisteBD().setValue(ConstantesBD.acronimoNo);
			nuevoProgServ.setInclusion(ConstantesBD.acronimoSi);
			
			nuevoProgServ.setListaServicios(PlanTratamiento.cargarServiciosParamPrograma(Utilidades.convertirAEntero(codigoPkPrgServ.toString()+""), codigoTarifarioServicios));
			
			
			if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi))
			{
				nuevoProgServ.setEstadoPrograma(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoInclusion);
				nuevoProgServ.setNewEstadoProg(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoInclusion);
				nuevoProgServ.setInactivarProg(ConstantesBD.acronimoSi);
			}	
			else
			{
				nuevoProgServ.setEstadoPrograma(ConstantesIntegridadDominio.acronimoEstadoPendiente);	
				nuevoProgServ.setNewEstadoProg(ConstantesIntegridadDominio.acronimoEstadoPendiente);
				
				nuevoProgServ.setArrayPosEstProg(llenadoPosiblesEstados(
						"", 
						nuevoProgServ.getListaServicios(), 
						"", 
						info.getUtilizaProgOdontIns(), 
						info.getValidaPresuOdontCont(),
						info.getInstRegistraAtenExt(), 
						nuevoProgServ.getEstadoPrograma(), 
						info.getCodigoCita(), 
						ConstantesIntegridadDominio.acronimoMostrarProgramas, 
						ConstantesBD.codigoNuncaValido, 
						nuevoProgServ.getCodigoPkProgramaServicio().intValue(), 
						ConstantesBD.codigoNuncaValido,
						nuevoProgServ.getPorConfirmar(),
						nuevoProgServ.getInclusion(),
						nuevoProgServ.getGarantia()));
			}	
			
			
			
			logger.info("size Servicios Programa: "+nuevoProgServ.getListaServicios().size());
			for(InfoServicios elem: nuevoProgServ.getListaServicios())
			{
				logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
				logger.info("Codigo Servicio: "+elem.getServicio().getCodigo());
				logger.info("Servicio: "+elem.getServicio().getNombre());
				if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi))
				   {
					   elem.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
					   elem.setInactivarServ(ConstantesBD.acronimoSi);
				   } 
					else
					{
						elem.setArrayPosEstServ(llenadoPosiblesEstados(
								elem.getEstadoServicio(), 
								nuevoProgServ.getListaServicios(),
								"",
								info.getUtilizaProgOdontIns(), 
								info.getValidaPresuOdontCont(),
								info.getInstRegistraAtenExt(), 
								nuevoProgServ.getEstadoPrograma(), 
								info.getCodigoCita(), 
								ConstantesIntegridadDominio.acronimoMostrarServicios,
								ConstantesBD.codigoNuncaValido, 
								nuevoProgServ.getCodigoPkProgramaServicio().intValue(),
								elem.getServicio().getCodigo(),
								elem.getPorConfirmar(),
								elem.getInclusion(),
								elem.getGarantia()));
					}
				logger.info("Inclusion Servicio: "+elem.getInclusion());
				elem.setInclusion(ConstantesBD.acronimoSi);
				logger.info("New Inclusion Servicio: "+elem.getInclusion());
				logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
			}		
			
			info.getNuevaInclusion().getDetalleSuperficie().get(0).getProgramasOservicios().add(nuevoProgServ);
			logger.info("añadio Boca");
		}
		
		return true;
	}	
	
	/**
	 * Metodo para eliminar un programa Servicio de una Nueva Inclusion
	 * @param info
	 * @return
	 */
	private InfoOdontograma accionEliminarProgServPlanTratamiento(InfoOdontograma info)
	{	
		logger.info("\n\n ENTRO A ELIMINAR Prog Nueva Inclusion");
		logger.info("indicador3 >>"+info.getIndicador3());
		if(info.getIndicador1() >= 0 && 
			info.getIndicador2() >= 0 && 
				info.getIndicador3() >= 0)
		{			
			info.getNuevaInclusion().getDetalleSuperficie().get(0).getProgramasOservicios().get(info.getIndicador3()).getExisteBD().setActivo(false);
			
			for (InfoServicios elem: info.getNuevaInclusion().getDetalleSuperficie().get(0).getProgramasOservicios().get(info.getIndicador3()).getListaServicios())
			{
				elem.getExisteBD().setActivo(false);
				elem.setInclusion(ConstantesBD.acronimoNo);
			}
		}
		
		return info;
	}
	
	
	/**
	 * Metodo para eliminar Un programa de la seccion de Incluciones 
	 * @param info
	 * @return
	 */
	private InfoOdontograma accionEliminarProgServInclusion(InfoOdontograma info) {
		
		logger.info("\n\n ENTRO A ELIMINAR Prog Inclusion");
		logger.info("indicador3 >>"+info.getIndicador3());
		if(info.getIndicador1() >= 0 && 
			info.getIndicador2() >= 0 && 
				info.getIndicador3() >= 0)
		{
		  	
			 if (info.getIndicador4()==3) // Sección detalle
			 {
				 /*
				 if(info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2()).getNumProgramasServiciosActivos() == 1)
				  {	
					info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2()).getExisteBD().setActivo(false);					
					logger.info("(OTRO) Se elimina TODA la Incusion...!");
				  }
				  */
				
				 InfoDetallePlanTramiento piezaSeleccionada=info.getInfoPlanTrata().getSeccionHallazgosDetalle().get(info.getIndicador1());
				 InfoHallazgoSuperficie superficieSeleccionada=piezaSeleccionada.getDetalleSuperficie().get(info.getIndicador2());

				 InfoProgramaServicioPlan progSeleccionado=superficieSeleccionada.getProgramasOservicios().get(info.getIndicador3());
				 progSeleccionado.setInclusion(ConstantesBD.acronimoNo);
				 
				 progSeleccionado.setNewEstadoProg(ConstantesIntegridadDominio.acronimoEstadoPendiente);
				 progSeleccionado.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
				 progSeleccionado.setInactivarProg(ConstantesBD.acronimoNo);

				     
				 for(InfoServicios elem: progSeleccionado.getListaServicios())
				 {
					 elem.setInclusion(ConstantesBD.acronimoNo);
					 elem.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
				 }
			
				for(InfoHallazgoSuperficie superficie:piezaSeleccionada.getDetalleSuperficie())
				{
					if(superficie.getExisteBD().isActivo())
					{
						if(superficie.getHallazgoREQUERIDO().getCodigo()==superficieSeleccionada.getHallazgoREQUERIDO().getCodigo())
						{
							for(InfoProgramaServicioPlan prog:superficie.getProgramasOservicios())
							{
								if(prog.getExisteBD().isActivo())
								{
									if(prog.getProgHallazgoPieza().getCodigoPk()==progSeleccionado.getProgHallazgoPieza().getCodigoPk())
									{
										prog.setNewEstadoProg(ConstantesIntegridadDominio.acronimoEstadoPendiente);
										prog.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
										prog.setInactivarProg(ConstantesBD.acronimoNo);
										prog.setInclusion(ConstantesBD.acronimoNo);

										for(InfoServicios elemInterno: prog.getListaServicios())
										{
										elemInterno.setInclusion(ConstantesBD.acronimoNo);
										elemInterno.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
										}
									}
								}
							}
						}
					}
				}

				
			 }else
			 if (info.getIndicador4()==1) // Otros
			 {
				 /*
				 if(info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2()).getNumProgramasServiciosActivos() == 1)
				  {	
					info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1()).getDetalleSuperficie().get(info.getIndicador2()).getExisteBD().setActivo(false);					
					logger.info("(OTRO) Se elimina TODA la Incusion...!");
				  }
				  */
				
				 InfoDetallePlanTramiento piezaSeleccionada=info.getInfoPlanTrata().getSeccionOtrosHallazgos().get(info.getIndicador1());
				 InfoHallazgoSuperficie superficieSeleccionada=piezaSeleccionada.getDetalleSuperficie().get(info.getIndicador2());
				 
				 InfoProgramaServicioPlan progSeleccionado=superficieSeleccionada.getProgramasOservicios().get(info.getIndicador3());
				 progSeleccionado.setInclusion(ConstantesBD.acronimoNo);
				 
				 progSeleccionado.setNewEstadoProg(ConstantesIntegridadDominio.acronimoEstadoPendiente);
				 progSeleccionado.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
				 progSeleccionado.setInactivarProg(ConstantesBD.acronimoNo);

				     
				 for(InfoServicios elem: progSeleccionado.getListaServicios())
				 {
					 elem.setInclusion(ConstantesBD.acronimoNo);
					 elem.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
				 }
				 /*
				for(InfoDetallePlanTramiento pieza:info.getInfoPlanTrata().getSeccionOtrosHallazgos())
				{
					if(pieza.getExisteBD().isActivo())
					{
						if(pieza.getPieza().getCodigo()==piezaSeleccionada.getPieza().getCodigo())
						{
							InfoHallazgoSuperficie superficie=pieza.getDetalleSuperficie().get(0);
							if(superficie.getExisteBD().isActivo())
							{
								if(superficie.getHallazgoREQUERIDO().getCodigo()==superficieSeleccionada.getHallazgoREQUERIDO().getCodigo())
								{
									for(InfoProgramaServicioPlan prog:superficie.getProgramasOservicios())
									{
										if(prog.getExisteBD().isActivo())
										{
											if(prog.getProgHallazgoPieza().getCodigoPk()==progSeleccionado.getProgHallazgoPieza().getCodigoPk())
											{
												prog.setNewEstadoProg(ConstantesIntegridadDominio.acronimoEstadoPendiente);
												prog.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
												prog.setInactivarProg(ConstantesBD.acronimoNo);
												
												for(InfoServicios elemInterno: prog.getListaServicios())
												{
													elemInterno.setInclusion(ConstantesBD.acronimoNo);
													elemInterno.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
												}
											 }
										 }
									 }
								 }
							 }
						 }
					}
				 }
				 */
					for(InfoHallazgoSuperficie superficie:piezaSeleccionada.getDetalleSuperficie())
					{
						if(superficie.getExisteBD().isActivo())
						{
							if(superficie.getHallazgoREQUERIDO().getCodigo()==superficieSeleccionada.getHallazgoREQUERIDO().getCodigo())
							{
								for(InfoProgramaServicioPlan prog:superficie.getProgramasOservicios())
								{
									if(prog.getExisteBD().isActivo())
									{
										if(prog.getProgHallazgoPieza().getCodigoPk()==progSeleccionado.getProgHallazgoPieza().getCodigoPk())
										{
											prog.setNewEstadoProg(ConstantesIntegridadDominio.acronimoEstadoPendiente);
											prog.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
											prog.setInactivarProg(ConstantesBD.acronimoNo);
											prog.setInclusion(ConstantesBD.acronimoNo);

											for(InfoServicios elemInterno: prog.getListaServicios())
											{
											elemInterno.setInclusion(ConstantesBD.acronimoNo);
											elemInterno.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
											}
										}
									}
								}
							}
						}
					}

				 
			
				
			 }else
				 if (info.getIndicador4()==2) // BOCA
				 {	
					 /*
					 if(info.getInfoPlanTrata().getSeccionHallazgosBoca().get(info.getIndicador2()).getNumProgramasServiciosActivos() == 1)
					 {	 
						 info.getInfoPlanTrata().getSeccionHallazgosBoca().get(info.getIndicador2()).getExisteBD().setActivo(false);
						logger.info("(BOCA) Se ELIMINA toda la Inclusion...!");
					 }	*/
					 InfoProgramaServicioPlan prog=info.getInfoPlanTrata().getSeccionHallazgosBoca().get(info.getIndicador2()).getProgramasOservicios().get(info.getIndicador3());
					 prog.setInclusion(ConstantesBD.acronimoNo);
					 
					 prog.setNewEstadoProg(ConstantesIntegridadDominio.acronimoEstadoPendiente);
					 prog.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
					 prog.setInactivarProg(ConstantesBD.acronimoNo);

					 for(InfoServicios elem : info.getInfoPlanTrata().getSeccionHallazgosBoca().get(info.getIndicador2()).getProgramasOservicios().get(info.getIndicador3()).getListaServicios())
					 {
						 elem.setInclusion(ConstantesBD.acronimoNo);
						 elem.setEstadoServicio(ConstantesIntegridadDominio.acronimoEstadoPendiente);
					 }
					
					
				 }
		}
		
		return info;
	}
	
	/**
	 * Metodo para verificar si una inclusion existe 
	 * @param info
	 * @param seccion
	 * @return
	 */
	private boolean existeInclusion(InfoOdontograma info, String seccion)
	{
		boolean existe= false;
		boolean band1=false;// pieza
		boolean band2=false;// hallazgo
		boolean band3=false;// superficie
		
		if(seccion.equals(ConstantesIntegridadDominio.acronimoOtro))
		{
			for(InfoDetallePlanTramiento elem: info.getInfoPlanTrata().getSeccionOtrosHallazgos()) //seccion Otros Hallazgos
			{			
					for(InfoHallazgoSuperficie elem2 : elem.getDetalleSuperficie())  // Detalle
					{						 
						for(InfoProgramaServicioPlan elem3 : elem2.getProgramasOservicios()) // Programas
						{
							logger.info("Programa es inclusion >> "+elem3.getInclusion()+ "es ACTIVO >>"+elem3.getExisteBD().isActivo());
							
							if(elem3.getInclusion().equals(ConstantesBD.acronimoSi) && elem3.getExisteBD().isActivo())
							{		
								//logger.info("OTRO >> ES INCLUSION ");
								if(elem.getPieza().getCodigo()==info.getNuevaInclusion().getPieza().getCodigo())
								 {
								  //logger.info("OTRO >> EXISTE MISMA PIEZA");	
								  band1=true;	
							     }
								if(elem2.getHallazgoREQUERIDO().getCodigo()==info.getNuevaInclusion().getDetalleSuperficie().get(0).getHallazgoREQUERIDO().getCodigo())
								 {
									//logger.info("OTRO >> EXISTE MISMO HALLAZGO");
									 band2=true; 
								 }
								 if(elem2.getSuperficieOPCIONAL().getCodigo()==info.getNuevaInclusion().getDetalleSuperficie().get(0).getSuperficieOPCIONAL().getCodigo())
								 {
									 //logger.info("OTRO >> EXISTE MISMA SUPERFICIE");
									 band3=true;
								 }
								 if(band1 && band2 && band3)
								 {
									// logger.info("OTRO >> RETORNA VERDADERO");
									 return true;
								 } 
						    }
					   }
				    }
			}
		}else if(seccion.equals(ConstantesIntegridadDominio.acronimoBoca))
		{
			
			for(InfoHallazgoSuperficie elem: info.getInfoPlanTrata().getSeccionHallazgosBoca()) //seccion Hallazgos Boca
			{				
				for(InfoProgramaServicioPlan elem2 : elem.getProgramasOservicios()) // Programas
				{
					if(elem2.getInclusion().equals(ConstantesBD.acronimoSi) && elem2.getExisteBD().isActivo())
					{
						
						if(elem.getHallazgoREQUERIDO().getCodigo()==info.getNuevaInclusion().getDetalleSuperficie().get(0).getHallazgoREQUERIDO().getCodigo())
						 {
							 band2=true; 
						 }
						 if(elem.getSuperficieOPCIONAL().getCodigo()==info.getNuevaInclusion().getDetalleSuperficie().get(0).getSuperficieOPCIONAL().getCodigo())
						 {
							 band3=true;
						 }
						 if(band2 && band3)
							 return true;					
					}
				}				
			}
			
		}			
		
		return existe;
	}
		
	//************************************************************************************
	//Metodos Exclusiones
	//************************************************************************************
	
	//************************************************************************************
	//Metodos Garantias
	//************************************************************************************
	
	//************************************************************************************
	//Metodos Histórico Plan de Tratamiento
	//************************************************************************************
	
	private InfoOdontograma accionConsultarHistorico(InfoOdontograma info) {
		
		/**
		 * info.getIndicador1()= Código Tipo Histórico
		 * info.getIndicador2()= Código Detalle Plan Tratamiento
		 * info.getIndicador3()= Código Programa
		 * info.getIndicador4()= Código Servicio 
		 */	
		
		 info.setTipoHistorico(info.getIndicador1());
				
		if(info.getTipoHistorico()==this.tipoHistoricoPlanTraramiento)
		{
			DtoLogPlanTratamiento parametrosDtoBusqueda=new DtoLogPlanTratamiento();
			parametrosDtoBusqueda.setPlanTratamiento(Utilidades.convertirADouble(info.getInfoPlanTrata().getCodigoPk()+""));
			parametrosDtoBusqueda.setHistoricoPlanT(ConstantesBD.acronimoSi);
			info.setArrayLogPlanTratamiento(PlanTratamiento.cargarLogs(parametrosDtoBusqueda));			
		}
		
		if(info.getTipoHistorico()==this.tipoHistoricoProgramasPlanTrat)
		{
			
			DtoLogProgServPlant parametrosDtoBusquedaProg = new DtoLogProgServPlant();
			info.setIdProgramaServicioLog(PlanTratamiento.obtenerCodPkLogServPlanT(info.getIndicador2(), info.getIndicador3(), ConstantesBD.codigoNuncaValido)+"");
			parametrosDtoBusquedaProg.setProgServPlant(Utilidades.convertirADouble(info.getIdProgramaServicioLog()));
			parametrosDtoBusquedaProg.setHistoricoProgServ(ConstantesBD.acronimoSi);
		    info.setArrayLogProgramaServicioPlan(PlanTratamiento.cargarLogProgramas(parametrosDtoBusquedaProg));
		}
		
		if(info.getTipoHistorico()==this.tipoHistoricoServiciosPlanTrat)
		{
			DtoLogProgServPlant parametrosDtoBusquedaProg = new DtoLogProgServPlant();					
			info.setIdProgramaServicioLog(PlanTratamiento.obtenerCodPkLogServPlanT(info.getIndicador2(), info.getIndicador3(), info.getIndicador4())+"");
			parametrosDtoBusquedaProg.setProgServPlant(Utilidades.convertirADouble(info.getIdProgramaServicioLog()));
			parametrosDtoBusquedaProg.setHistoricoProgServ(ConstantesBD.acronimoNo);
		    info.setArrayLogProgramaServicioPlan(PlanTratamiento.cargarLogProgramas(parametrosDtoBusquedaProg));
		}
		
		return info;
	}
	
	
	
	
	//************************************************************************************
	//Metodos Cita Programada
	//************************************************************************************
	
//	/**
//	 * Metodo encargado de listar los servicios seleccionados para la proxima cita
//	 */
//	private InfoOdontograma accionEmpezarProximaCita(InfoOdontograma info) 
//	{
//		ArrayList<InfoServicios> arrayServ=new ArrayList<InfoServicios>();
//		info.resetServiciosProxCita();
//		logger.info("ENTRO PROGRMAR PROXIMA CITA");
//		 
// 
//		 //****************** SECCION HALLAZGOS ***************************************************************
//		 
//		for(InfoDetallePlanTramiento pieza: info.getInfoPlanTrata().getSeccionHallazgosDetalle())
//		{
//			for(InfoHallazgoSuperficie hallazgo: pieza.getDetalleSuperficie())
//			{
//				for(InfoProgramaServicioPlan programa: hallazgo.getProgramasOservicios())
//				{
//					int contServ1 = 0;
//					if(programa.getListaServicios().size()>0)
//					{
//						for(InfoServicios servicio: programa.getListaServicios())
//						{
//							if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi))
//							{
//								if(servicio.getIncluirCita().equals(ConstantesBD.acronimoSi) && servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoContratado))
//								{
//									/*
//									 * Se realiza esta modificación para no evaluar el orden de los servicios.
//									 * 
//									if(contServ1>0)
//									{
//										for(int i= contServ1-1; i >= 0; i--)
//										{
//											if(!programa.getListaServicios().get(i).getIncluirCita().equals(ConstantesBD.acronimoSi) && programa.getListaServicios().get(i).getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoContratado))
//											{
//												if(!programa.getListaServicios().get(i).getNewEstado().equals(ConstantesIntegridadDominio.acronimoRealizadoInterno))
//												{
//												  servicio.setMsjErrorServicioCita(new InfoDatosString("servicioSinEvo","El servicio seleccionado no se puede programar, existen servicios sin evolución"));
//												  i= -1;
//												}else
//												{
//													servicio.setMsjErrorServicioCita(new InfoDatosString());
//												}
//											}else
//											{
//												servicio.setMsjErrorServicioCita(new InfoDatosString());
//											}
//										}
//									}*/
//									
//									servicio.setMsjErrorServicioCita(new InfoDatosString());
//									servicio.setProgramaAsociado(programa);
//									programa.setHallazgoAsociado(hallazgo);
//									hallazgo.setPiezaAsociada(pieza);
//									arrayServ.add(servicio);
//								} 	
//							}else
//							{
//								if( info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo))
//								{
//									if(servicio.getIncluirCita().equals(ConstantesBD.acronimoSi) && servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
//									{
//										/*
//										 * Se realiza esta modificación para no evaluar el orden de los servicios.
//										 * 
//										if(contServ1 > 0)
//										{								    				   
//											for(int i= contServ1-1; i >= 0; i--)
//											{
//												if(!programa.getListaServicios().get(i).getIncluirCita().equals(ConstantesBD.acronimoSi) && programa.getListaServicios().get(i).getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
//												{
//													if(!programa.getListaServicios().get(i).getNewEstado().equals(ConstantesIntegridadDominio.acronimoRealizadoInterno))
//													{
//													  servicio.setMsjErrorServicioCita(new InfoDatosString("servicioSinEvo","El servicio seleccionado no se puede programar, existen servicios sin evolución"));
//													  i= -1;
//													}else
//													{
//														servicio.setMsjErrorServicioCita(new InfoDatosString());
//													}
//												}
//												else
//												{
//													servicio.setMsjErrorServicioCita(new InfoDatosString());	
//												}
//											}
//										}*/
//										servicio.setMsjErrorServicioCita(new InfoDatosString());
//										servicio.setProgramaAsociado(programa);
//										programa.setHallazgoAsociado(hallazgo);
//										hallazgo.setPiezaAsociada(pieza);
//										arrayServ.add(servicio);
//									}					    		   
//								}
//							}
//							
//							contServ1++;
//						}							
//					}
//				}
//			}
//		}	
//		 
//  //************* SECCION OTROS HALLAZGOS ******************************************************************
//		 
//	    for(InfoDetallePlanTramiento pieza: info.getInfoPlanTrata().getSeccionOtrosHallazgos())
//	    {
//	    	for(InfoHallazgoSuperficie hallazgo: pieza.getDetalleSuperficie())
//	    	{
//	    		for(InfoProgramaServicioPlan programa: hallazgo.getProgramasOservicios())
//	    		{
//	    			int contServ = 0;
//	    			if(programa.getListaServicios().size()>0)
//	    			{
//						for(InfoServicios servicio: programa.getListaServicios())
//						{
//							logger.info("ENTRO Lista Servicios CONT >> "+contServ);
//							logger.info("ENTRO Lista Servicios Validar Presupuesto >> "+info.getValidaPresuOdontCont());
//							logger.info("ENTRO Lista Servicios IncluirCita >> "+servicio.getIncluirCita());
//							if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi))
//							{
//								if(servicio.getIncluirCita().equals(ConstantesBD.acronimoSi) && servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoContratado))
//								{
//									/*
//									 * Se realiza esta modificación para no evaluar el orden de los servicios.
//									 * 
//									if(contServ>0)
//									{
//										for(int i= contServ-1; i >= 0; i--)
//										{
//											if(!programa.getListaServicios().get(i).getIncluirCita().equals(ConstantesBD.acronimoSi) && programa.getListaServicios().get(i).getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoContratado))
//											{
//												if(!programa.getListaServicios().get(i).getNewEstado().equals(ConstantesIntegridadDominio.acronimoRealizadoInterno))
//												{
//												  servicio.setMsjErrorServicioCita(new InfoDatosString("servicioSinEvo","El servicio seleccionado no se puede programar, existen servicios sin evolución"));
//												  i= -1;
//												}else
//												{
//													servicio.setMsjErrorServicioCita(new InfoDatosString());
//												}
//											}else
//											{
//												servicio.setMsjErrorServicioCita(new InfoDatosString());	
//											}
//										}
//									}*/
//									servicio.setMsjErrorServicioCita(new InfoDatosString());
//									servicio.setProgramaAsociado(programa);
//									programa.setHallazgoAsociado(hallazgo);
//									hallazgo.setPiezaAsociada(pieza);
//									arrayServ.add(servicio);
//								} 	
//								
//							}else
//							{
//								if( info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo))
//								{
//									if(servicio.getIncluirCita().equals(ConstantesBD.acronimoSi) && servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
//									{
//										/*
//										 * Se realiza esta modificación para no evaluar el orden de los servicios.
//										 * 
//										if(contServ > 0)
//										{													   
//											for(int i= contServ-1; i >= 0; i--)
//											{
//												if(!programa.getListaServicios().get(i).getIncluirCita().equals(ConstantesBD.acronimoSi) && programa.getListaServicios().get(i).getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
//												{
//													if(!programa.getListaServicios().get(i).getNewEstado().equals(ConstantesIntegridadDominio.acronimoRealizadoInterno))
//													{
//													  servicio.setMsjErrorServicioCita(new InfoDatosString("servicioSinEvo","El servicio seleccionado no se puede programar, existen servicios sin evolución"));
//													  i= -1;
//													}else
//													{
//														servicio.setMsjErrorServicioCita(new InfoDatosString());
//													}
//												}
//												else
//												{
//													servicio.setMsjErrorServicioCita(new InfoDatosString());	
//												}
//											}
//										}*/											   
//										servicio.setMsjErrorServicioCita(new InfoDatosString());
//										servicio.setProgramaAsociado(programa);
//										programa.setHallazgoAsociado(hallazgo);
//										hallazgo.setPiezaAsociada(pieza);
//										arrayServ.add(servicio);
//									}								   
//								}
//							}									
//							contServ++;
//						}
//					}
//				}
//			}
//		}
//	    
//	  //************* SECCION BOCA ******************************************************************
//		 
//    	for(InfoHallazgoSuperficie hallazgo: info.getInfoPlanTrata().getSeccionHallazgosBoca())
//    	{
//    		for(InfoProgramaServicioPlan programa: hallazgo.getProgramasOservicios())
//    		{
//    			int contServ = 0;
//    			if(programa.getListaServicios().size()>0)
//    			{
//					for(InfoServicios servicio: programa.getListaServicios())
//					{
//						logger.info("ENTRO Lista Servicios CONTA >> "+contServ);
//						logger.info("ENTRO Lista Servicios Validar Presupuesto >> "+info.getValidaPresuOdontCont());
//						logger.info("ENTRO Lista Servicios IncluirCita >> "+servicio.getIncluirCita());
//						if(info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoSi))
//						{
//							if(servicio.getIncluirCita().equals(ConstantesBD.acronimoSi) && servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoContratado))
//							{
//								/*
//								 * Se realiza esta modificación para no evaluar el orden de los servicios.
//								 * 
//								if(contServ>0)
//								{
//									for(int i= contServ-1; i >= 0; i--)
//									{
//										if(!programa.getListaServicios().get(i).getIncluirCita().equals(ConstantesBD.acronimoSi) && programa.getListaServicios().get(i).getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoContratado))
//										{
//											if(!programa.getListaServicios().get(i).getNewEstado().equals(ConstantesIntegridadDominio.acronimoRealizadoInterno))
//											{
//											  servicio.setMsjErrorServicioCita(new InfoDatosString("servicioSinEvo","El servicio seleccionado no se puede programar, existen servicios sin evolución"));
//											  i= -1;
//											}else
//											{
//												servicio.setMsjErrorServicioCita(new InfoDatosString());
//											}
//										}else
//										{
//											servicio.setMsjErrorServicioCita(new InfoDatosString());	
//										}
//									}
//								}*/
//								servicio.setMsjErrorServicioCita(new InfoDatosString());
//								servicio.setProgramaAsociado(programa);
//								programa.setHallazgoAsociado(hallazgo);
//								hallazgo.setPiezaAsociada(null);
//								arrayServ.add(servicio);
//							} 	
//							
//						}else
//						{
//							if( info.getValidaPresuOdontCont().equals(ConstantesBD.acronimoNo))
//							{
//								if(servicio.getIncluirCita().equals(ConstantesBD.acronimoSi) && servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
//								{
//									/*
//									 * Se realiza esta modificación para no evaluar el orden de los servicios.
//									 * 
//									if(contServ > 0)
//									{													   
//										for(int i= contServ-1; i >= 0; i--)
//										{
//											if(!programa.getListaServicios().get(i).getIncluirCita().equals(ConstantesBD.acronimoSi) && programa.getListaServicios().get(i).getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente))
//											{
//												if(!programa.getListaServicios().get(i).getNewEstado().equals(ConstantesIntegridadDominio.acronimoRealizadoInterno))
//												{
//												  servicio.setMsjErrorServicioCita(new InfoDatosString("servicioSinEvo","El servicio seleccionado no se puede programar, existen servicios sin evolución"));
//												  i= -1;
//												}else
//												{
//													servicio.setMsjErrorServicioCita(new InfoDatosString());
//												}
//											}
//											else
//											{
//												servicio.setMsjErrorServicioCita(new InfoDatosString());	
//											}
//										}
//									}*/
//									
//									servicio.setMsjErrorServicioCita(new InfoDatosString());
//									servicio.setProgramaAsociado(programa);
//									programa.setHallazgoAsociado(hallazgo);
//									hallazgo.setPiezaAsociada(null);
//									arrayServ.add(servicio);
//								}								   
//							}
//						}									
//						contServ++;
//					}
//				}
//			}
//		}
//		
//	    
//		info.setArrayServProxCita(arrayServ);
//		return info;
//	}
	
	/**
	 * 
	 * @param info
	 */
	private void accionDesasociarServiciosEliminadosProximaCita(InfoOdontograma info)
	{
		for(InfoServicios servicioProximaCita:info.getArrayServProxCita())
		{
			if(servicioProximaCita.getEliminarSeleccionProxCita().equals(ConstantesBD.acronimoSi))
			{
				for(InfoDetallePlanTramiento elemInfo: info.getInfoPlanTrata().getSeccionHallazgosDetalle())
				{
					for(InfoHallazgoSuperficie elemInfo1: elemInfo.getDetalleSuperficie())
					{
						for(InfoProgramaServicioPlan elemInfo2: elemInfo1.getProgramasOservicios())
						{
							for(InfoServicios servicioPlan: elemInfo2.getListaServicios())
							{
								if(servicioProximaCita.getCodigoPkProgServ()==servicioPlan.getCodigoPkProgServ())
								{
									servicioPlan.setIncluirCita(ConstantesBD.acronimoNo);
								}
							}
						}
						
					}
				}
				for(InfoDetallePlanTramiento elemInfo: info.getInfoPlanTrata().getSeccionOtrosHallazgos())
				{
					for(InfoHallazgoSuperficie elemInfo1: elemInfo.getDetalleSuperficie())
					{
						for(InfoProgramaServicioPlan elemInfo2: elemInfo1.getProgramasOservicios())
						{
							for(InfoServicios servicioPlan: elemInfo2.getListaServicios())
							{
								if(servicioProximaCita.getCodigoPkProgServ()==servicioPlan.getCodigoPkProgServ())
								{
									servicioPlan.setIncluirCita(ConstantesBD.acronimoNo);
								}
							}
						}
						
					}
				}
			}
		}
		
	}
	
//	/**
//	 * Metodo para Realizar la Insercion de la Proxima Cita Progrmada
//	 * @param info
//	 * @return
//	 */
//	private boolean accionGuardarProximaCita(InfoOdontograma info) 
//	{
//		if(CitaOdontologica.insertarProximaCitaOdontologia(getConInterna(), info.getArrayServProxCita(), info.getFechaProxCita(), info.getCodigoPaciente(), info.getUsuarioActual().getUsuarioModifica() , info.getCodigoEvolucion(), info.getCentroCosto() )<=0)
//		{			
//			this.adicionarError("errors.notEspecific","NO se Guardo la Programación de la Proxima Cita");
//			return false;
//		}
//		
//		return true;
//	}
	
//	/**
//	 * Metodo para eliminar un Servicio Seleccionado para una proxima cita
//	 * @param info
//	 * @return
//	 */
//	private InfoOdontograma accionEliminarServicioProximaCita(InfoOdontograma info) {
//			
//		 int posEliminar= info.getPosServProxCita();
//		 info.getArrayServProxCita().get(posEliminar).setEliminarSeleccionProxCita(ConstantesBD.acronimoSi);
//		 accionDesasociarServiciosEliminadosProximaCita(info);
//		 return info;
//	}

	 
	
	
	//************************************************************************************
	//Metodos Plan Tratamiento
	//************************************************************************************
	
	/**
	 * @param Connection con
	 * @param InfoOdontograma info
	 * */ 
	private boolean actualizarSeccEvolBoca(Connection con, InfoOdontograma info)
	{
		PlanTratamiento planTratamiento = new PlanTratamiento();
		InfoDatosString actualizar = new InfoDatosString();
		boolean evaluarProgyServ = false;
		String estPrograma = "";
		//********************************************************************************
		// Actualizacion Evolución Programas Servicios Evolucion Boca
		for(InfoHallazgoSuperficie detallePlan: info.getInfoPlanTrata().getSeccionHallazgosBoca())
		{
			for(InfoProgramaServicioPlan progServPlanT: detallePlan.getProgramasOservicios())
			{
				
				/*
				 *MODIFICAR EL DETALLE PLAN DE TRATAMIENTO
				 *NOTA.
				 *SE MODIFICA PORQUE SE TIEN QUE LLEVAR EL REGISTRO DEL LOG DEL DETALLE DEL PLAN  
				 */
				actualizarPlanTratamiento(con, info, detallePlan);
				
				
				
				
				if(info.getUtilizaProgOdontIns().equals(ConstantesBD.acronimoSi))
				{   // si maneja programas
					//logger.info("\n\n\n\n\n\n\n\n\n");
					//logger.info("Estado Prog: -"+elem2.getEstadoPrograma()+"-");
					//logger.info("New Estado Prog: -"+elem2.getNewEstadoProg()+"-");
					if(!progServPlanT.getNewEstadoProg().equals("")&&!progServPlanT.getEstadoPrograma().equals(progServPlanT.getNewEstadoProg()))
					{
						actualizar = obtenerCasoActualizacion(progServPlanT.getNewEstadoProg());
						//logger.info("casoActualizacion PROG: "+actualizar.getCodigo());
						//logger.info("estado PROG: "+actualizar.getNombre());
						if(!actualizar.getCodigo().equals("")&&!actualizar.getNombre().equals(""))
						{
							if(actualizar.getCodigo().charAt(0)==ConstantesBD.acronimoRegistrarGarantias)
								evaluarProgyServ = true;
							if(!planTratamiento.actualizacionEstadosPSOtrasEvoluciones(con, 
									actualizar.getCodigo().charAt(0), 
									actualizar.getNombre(), 
									ConstantesIntegridadDominio.acronimoMostrarProgramas, 
									"", 
									info.getUsuarioActual().getUsuarioModifica(), 
									detallePlan.getCodigoPkDetalle().intValue(), 
									progServPlanT.getCodigoPkProgramaServicio().intValue(), 
									ConstantesBD.codigoNuncaValido,
									Utilidades.convertirAEntero(progServPlanT.getMotivoCancelacion().getCodigo()),
									info.getCodigoCita(),
									info.getCodigoValoracion(),
									info.getCodigoEvolucion(),
									info.getPorConfirmar()))
								return false;
						}
					}
					
					/*logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
					logger.info("evaluarProgyServ: "+evaluarProgyServ);
					logger.info("New Estado Programa: "+elem2.getNewEstadoProg());
					logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
					*/
					//if(evaluarProgyServ)
					
					logger.info("elem2.getNewEstadoProg()------------------>"+progServPlanT.getNewEstadoProg());
					
					if(!progServPlanT.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoEstadoCancelado)
							&& !progServPlanT.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoInclusion)
							&& !progServPlanT.getNewEstadoProg().equals(ConstantesIntegridadDominio.acronimoPorAutorizar+ConstantesBD.separadorSplit+ConstantesIntegridadDominio.acronimoExcluido))
					{
						//logger.info("Entra a evolucionar el servicio especifico");
						if(!actualizarEvolucionServicio(con, 
								planTratamiento, 
								progServPlanT.getListaServicios(),
								info.getUsuarioActual().getUsuarioModifica(),
								detallePlan.getCodigoPkDetalle().intValue(),
								progServPlanT.getCodigoPkProgramaServicio().intValue(),
								info.getUtilizaProgOdontIns(),
								info.getCodigoCita(),
								info.getCodigoValoracion(),
								info.getCodigoEvolucion(),
								info.getPorConfirmar()))
							return false;
						evaluarProgyServ = false;
					}
					
				}else if(info.getUtilizaProgOdontIns().equals(ConstantesBD.acronimoNo))
				{// si es solo por servicios
					if(!actualizarEvolucionServicio(con, 
							planTratamiento, 
							progServPlanT.getListaServicios(),
							info.getUsuarioActual().getUsuarioModifica(), 
							detallePlan.getCodigoPkDetalle().intValue(), 
							progServPlanT.getCodigoPkProgramaServicio().intValue(),
							info.getUtilizaProgOdontIns(),
							info.getCodigoCita(),
							info.getCodigoValoracion(),
							info.getCodigoEvolucion(),
							info.getPorConfirmar()))
						return false;
				}
			}
		}
		//logger.info("\n\n\n\n\n\n\n\n\n");
		// FIN Actualizacion Evolución Programas Servicios Detalle Plan Tratamiento
		//********************************************************************************
		return true;
	}
	
	
	private ArrayList<DtoSectorSuperficieCuadrante> cargarSuperficiesDiente(ArrayList<DtoSectorSuperficieCuadrante> arraySuperficies, int codigoPieza) 
	{
		ArrayList<DtoSectorSuperficieCuadrante> superficies=new ArrayList<DtoSectorSuperficieCuadrante>();
		
		for(DtoSectorSuperficieCuadrante superficie:arraySuperficies)
		{
			if(superficie.getPieza()==codigoPieza)
			{
				superficies.add(superficie);
			}
		}
		
		return superficies;
	}
	 
	//************************************************************************************
	//Metodos GET y SET
	//************************************************************************************
	
	public ActionErrors getErrores() {
		return errores;
	}

	public void setErrores(ActionErrors errores) {
		this.errores = errores;
	}

	public String getEstadoInterno() {
		return estadoInterno;
	}

	public void setEstadoInterno(String estadoInterno) {
		this.estadoInterno = estadoInterno;
	}

	public String getForwardOdont() {
		return forwardOdont;
	}

	public void setForwardOdont(String forwardOdont) {
		this.forwardOdont = forwardOdont;
	}

	/**
	 * @return the tipoHistoricoServiciosPlanTrat
	 */
	public int getTipoHistoricoServiciosPlanTrat() {
		return tipoHistoricoServiciosPlanTrat;
	}

	/**
	 * @param tipoHistoricoServiciosPlanTrat the tipoHistoricoServiciosPlanTrat to set
	 */
	public void setTipoHistoricoServiciosPlanTrat(int tipoHistoricoServiciosPlanTrat) {
		this.tipoHistoricoServiciosPlanTrat = tipoHistoricoServiciosPlanTrat;
	}

	/**
	 * @return the conInterna
	 */
	public Connection getConInterna() {
		
		if(conInterna==null){
			
			conInterna = HibernateUtil.obtenerConexion();
		}
		
		return conInterna;
	}

	/**
	 * @param conInterna the conInterna to set
	 */
	public void setConInterna(Connection conInterna) {
		this.conInterna = conInterna;
	}
	
	/**
	 * 
	 * */
	public void adicionarError(String error,String descripcion)
	{
		this.errores.add("descripcion",new ActionMessage(error,descripcion));
	}

	

}