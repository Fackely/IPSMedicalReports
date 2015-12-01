package com.princetonsa.actionform.odontologia;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.numeros.UtilidadNumerosLetras;
import util.odontologia.InfoAntecedenteOdonto;
import util.odontologia.InfoDetallePlanTramiento;
import util.odontologia.InfoHallazgoSuperficie;
import util.odontologia.InfoOdontograma;
import util.odontologia.InfoProgramaServicioPlan;
import util.odontologia.InfoServicios;

import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.facturacion.DtoFacturaAutomaticaOdontologica;
import com.princetonsa.dto.historiaClinica.DtoEvolucion;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoComponenteIndicePlaca;
import com.princetonsa.dto.odontologia.DtoEvolucionOdontologica;
import com.princetonsa.dto.odontologia.DtoInfoAtencionCitaOdo;
import com.princetonsa.dto.odontologia.DtoProcesoCitaProgramada;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosPlanT;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.PlanTratamiento;

public class EvolucionOdontologicaForm  extends ValidatorForm {

	/**
	 * Versión serial
	 */
	
	/**
	 * 
	 */
	private int mutiploMinutosGeneracionCita;
	
	
	
	
	
	
	private static final long serialVersionUID = 1L;
	private String estado;
	private String estadoSecundario;
	
	private DtoPlantilla plantilla;
	
	private DtoEvolucion evolucion;
	
	//Elementos que se reciben de manera parametrizada
	private int codigoPlantilla;
	
	private double cita;
	
	private int  ingreso;
	
	private int numeroSolicitud;
	
	// Atributo comunt para toda la plantilla de valoracion odontologica
	private String porConfirmar;
	//Atributo para saber si la plantilla es para actualizar o nueva
	private String porActualizar;
	
	private DtoEvolucionOdontologica dtoEvolucionOdo;
	
	private ArrayList<DtoEvolucionOdontologica> listadoEvoluciones;
	
	private ArrayList<DtoPlantilla> listadoPlantillas;
	
	private DtoInfoAtencionCitaOdo dtoAtencionCita;
	
	private DtoCitaOdontologica dtoCita;
	
	
	/**
	 * Atributo para almacenar la posicion de la plantilla a la cual
	 * se desea ingresar
	 */
	private int posicionPlantilla;
	
	
	private String abrirPopUp;
	
	private String cerrarVentanaConf;
	
	private String secSeleccionarFormulario;
	
	private String ancla;
	
	//*******************ATRIBUTOS PARA EL COMPONENTE DE ODONTOGRAMA************
	/**
	 * 
	 * */
	private InfoOdontograma infoCompOdont;
	
	//***************************************************************************
	
	//*******************ATRIBUTOS PARA EL COMPONENTE DE ODONTOGRAMA EVOLUCION ************
	
	/**
	 * 
	 */
	private String existenServPendEvolucionar;
	
	//***************************************************************************
	
	public String getExistenServPendEvolucionar() {
		return existenServPendEvolucionar;
	}

	public void setExistenServPendEvolucionar(String existenServPendEvolucionar) {
		this.existenServPendEvolucionar = existenServPendEvolucionar;
	}

	//*******************ATRIBUTOS PARA EL COMPONENTE DE ANTECEDENTES ODONTOLOGICOS************
	/**
	 * 
	 */
	private InfoAntecedenteOdonto infoCompAnteOdont;
	//***************************************************************************
	
	
	//*******************ATRIBUTOS PARA EL COMPONENTE INDICE DE PLACA************
	/**
	 * 
	 */
	private DtoComponenteIndicePlaca compIndicePlaca;
	//***************************************************************************
	
	
	///*******************ATRIBUTOS PARA LA IMPRESION****************************************************
	private InstitucionBasica institucionBasica;
	private ArrayList<DtoPlantilla> plantillas;
	private DtoPaciente paciente;
	private String fechaNacimientoPacienteFormateado;
	private String titulo;
	private String tipoCitaFormateado;
	private String plantillaBase;
	private UsuarioBasico usuarioResumen;
	private String fechaResumen;
	private String horaResumen;
	private String nombreContexto;
	//************************************************************************************************
	
	/*******IMPRESION PDF**********/
	private EnumTiposSalida enumTipoSalida;
	private String nombreArchivoGenerado;
	private String redireccion;
	
	
	/*
	 * Atributo para controlar si el medico que comenzó la atención de la cita es el mismo que ha de confirmarla
	 */
	private String esMedicoConfirmacion;
	
	/**
	 * Facturas automaticas posiblemente generadas
	 */
	private DtoFacturaAutomaticaOdontologica facturasAutomaticas;
	
	/**
	 * Campo que contiene los mensajes informativos.
	 */
	private String mensajeInformativo;
	
	
	/**
	 * Código de la próxima cita registrada desde el proceso centralizado
	 * Próxima Cita Programada
	 */
	private int codigoProximaCitaRegistrada;
	
	/**
	 * Atributo que guarda temporalmente el estado antes de iniciar 
	 * el proceso centralizado de Proxima Cita con  el fin de reasignarlo 
	 * nuevamente cuando se retorne de dicho proceso.
	 */
	private String estadoTemporal;
	
	/**
	 * Atributo que indica si se debe o no iniciar el proceso centralizado 
	 * de asignación de próxima Cita
	 */
	private boolean abrePopUpProximaCita;
	
	/**
	 * Atributo que contiene toda la información que es necesaria
	 * para iniciar el proceso de Programación de la Próxima Cita
	 */
	private DtoProcesoCitaProgramada dtoProcesoCitaProgramada;  
	
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		if(this.estado.equals("validaProximaCita")){
			
			UtilidadNumerosLetras util = new UtilidadNumerosLetras();
			//mutiploMinutosGeneracionCita
			for( InfoServicios servicio: this.getInfoCompOdont().getArrayServProxCita() ){

				if( servicio.getDuracionCita()<=0)
				{
					errores.add("",	new ActionMessage("errors.notEspecific"," No se permite duración de cita menor o igual a cero "));
					break;
				}
			
				if( servicio.getDuracionCita()<=this.mutiploMinutosGeneracionCita)
				{
					errores.add("",	new ActionMessage("errors.notEspecific","El tiempo de duración ingresado deber ser multiplo de "+ util.toLetras(this.mutiploMinutosGeneracionCita) +"("+this.mutiploMinutosGeneracionCita+"). Por favor verifique "));
				}
				else if(servicio.getDuracionCita()%this.mutiploMinutosGeneracionCita!=0)
				{
					errores.add("",	new ActionMessage("errors.notEspecific","El tiempo de duración ingresado deber ser multiplo de "+ util.toLetras(this.mutiploMinutosGeneracionCita) +"("+this.mutiploMinutosGeneracionCita+"). Por favor verifique "));
				}
			}
		}

		
			
		return errores;
		
	}
	
	public EvolucionOdontologicaForm()
	{
		reset(null);
	}

	public void reset(HttpServletRequest request) 
	{
		this.ancla="";
		this.estado=request==null?"":request.getParameter("estado");
		this.estadoSecundario = "";
		//this.plantilla=new DtoPlantilla();
		this.evolucion=new DtoEvolucion();
		this.codigoPlantilla=request==null?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(request.getParameter("codigoPlantilla"));
		this.cita=request==null?ConstantesBD.codigoNuncaValidoDouble:Utilidades.convertirADouble(request.getParameter("cita"));
		this.porConfirmar="";
		this.porActualizar="";
		this.dtoEvolucionOdo=new DtoEvolucionOdontologica();
		this.listadoEvoluciones=new ArrayList<DtoEvolucionOdontologica>();
		this.listadoPlantillas=new ArrayList<DtoPlantilla>();
		this.dtoAtencionCita=new DtoInfoAtencionCitaOdo();
		this.posicionPlantilla=request==null?ConstantesBD.codigoNuncaValido:Utilidades.convertirAEntero(request.getParameter("posicionPlantilla"));
		this.dtoCita=new DtoCitaOdontologica();
		this.abrirPopUp=ConstantesBD.acronimoNo;
		this.cerrarVentanaConf=ConstantesBD.acronimoNo;
		this.secSeleccionarFormulario=ConstantesBD.acronimoNo;
		//*****ATRIBUTOS PARA EL COMPONENTE DE ANTECEDENTES ODONTOLOGICOS************
		this.infoCompAnteOdont = new InfoAntecedenteOdonto();
		//***************************************************************************
		//*****ATRIBUTOS PARA EL COMPONENTE INDICE DE PLACA**************************
		this.compIndicePlaca = new DtoComponenteIndicePlaca();
		//***************************************************************************
		//*****ATRIBUTOS PARA EL ODONTOGRAMA*****************************************
		this.infoCompOdont = new InfoOdontograma();
		//***************************************************************************
		
		this.existenServPendEvolucionar=ConstantesBD.acronimoNo;
		
		this.institucionBasica = new InstitucionBasica();
		this.plantillas = new ArrayList<DtoPlantilla>();
		this.paciente = new DtoPaciente();
		this.usuarioResumen = new UsuarioBasico();
		this.fechaResumen = "";
		this.horaResumen = "";
		this.esMedicoConfirmacion="";
		
		this.facturasAutomaticas= new DtoFacturaAutomaticaOdontologica();
		//this.mutiploMinutosGeneracionCita=ConstantesBD.codigoNuncaValido;
		
		
		this.setCodigoProximaCitaRegistrada(ConstantesBD.codigoNuncaValido);
		this.setEstadoTemporal("");
		this.abrePopUpProximaCita = false;
		this.setMensajeInformativo(null);
		
		this.setDtoProcesoCitaProgramada(new DtoProcesoCitaProgramada());
	}

	public String getAncla() {
		return ancla;
	}

	public void setAncla(String ancla) {
		this.ancla = ancla;
	}

	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public DtoPlantilla getPlantilla() {
		return plantilla;
	}


	public void setPlantilla(DtoPlantilla plantilla) {
		this.plantilla = plantilla;
	}


	public DtoEvolucion getEvolucion() {
		return evolucion;
	}


	public void setEvolucion(DtoEvolucion evolucion) {
		this.evolucion = evolucion;
	}


	public int getCodigoPlantilla() {
		return codigoPlantilla;
	}


	public void setCodigoPlantilla(int codigoPlantilla) {
		this.codigoPlantilla = codigoPlantilla;
	}


	public int getIngreso() {
		return ingreso;
	}


	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}


	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}


	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}


	public double getCita() {
		return cita;
	}


	public void setCita(double cita) {
		this.cita = cita;
	}

	public String getPorConfirmar() {
		return porConfirmar;
	}

	public void setPorConfirmar(String porConfirmar) {
		this.porConfirmar = porConfirmar;
	}

	public String getPorActualizar() {
		return porActualizar;
	}

	public void setPorActualizar(String porActualizar) {
		this.porActualizar = porActualizar;
	}

	public DtoEvolucionOdontologica getDtoEvolucionOdo() {
		return dtoEvolucionOdo;
	}

	public void setDtoEvolucionOdo(DtoEvolucionOdontologica dtoEvolucionOdo) {
		this.dtoEvolucionOdo = dtoEvolucionOdo;
	}

	public ArrayList<DtoEvolucionOdontologica> getListadoEvoluciones() {
		return listadoEvoluciones;
	}

	public void setListadoEvoluciones(
			ArrayList<DtoEvolucionOdontologica> listadoEvoluciones) {
		this.listadoEvoluciones = listadoEvoluciones;
	}

	public ArrayList<DtoPlantilla> getListadoPlantillas() {
		return listadoPlantillas;
	}

	public void setListadoPlantillas(ArrayList<DtoPlantilla> listadoPlantillas) {
		this.listadoPlantillas = listadoPlantillas;
	}

	public DtoInfoAtencionCitaOdo getDtoAtencionCita() {
		return dtoAtencionCita;
	}

	public void setDtoAtencionCita(DtoInfoAtencionCitaOdo dtoAtencionCita) {
		this.dtoAtencionCita = dtoAtencionCita;
	}

	public int getPosicionPlantilla() {
		return posicionPlantilla;
	}

	public void setPosicionPlantilla(int posicionPlantilla) {
		this.posicionPlantilla = posicionPlantilla;
	}

	public DtoCitaOdontologica getDtoCita() {
		return dtoCita;
	}

	public void setDtoCita(DtoCitaOdontologica dtoCita) {
		this.dtoCita = dtoCita;
	}

	public String getAbrirPopUp() {
		return abrirPopUp;
	}

	public void setAbrirPopUp(String abrirPopUp) {
		this.abrirPopUp = abrirPopUp;
	}

	public String getCerrarVentanaConf() {
		return cerrarVentanaConf;
	}

	public void setCerrarVentanaConf(String cerrarVentanaConf) {
		this.cerrarVentanaConf = cerrarVentanaConf;
	}

	public InfoOdontograma getInfoCompOdont() {
		return infoCompOdont;
	}

	public void setInfoCompOdont(InfoOdontograma infoCompOdont) {
		this.infoCompOdont = infoCompOdont;
	}

	public InfoAntecedenteOdonto getInfoCompAnteOdont() {
		return infoCompAnteOdont;
	}

	public void setInfoCompAnteOdont(InfoAntecedenteOdonto infoCompAnteOdont) {
		this.infoCompAnteOdont = infoCompAnteOdont;
	}

	public DtoComponenteIndicePlaca getCompIndicePlaca() {
		return compIndicePlaca;
	}

	public void setCompIndicePlaca(DtoComponenteIndicePlaca compIndicePlaca) {
		this.compIndicePlaca = compIndicePlaca;
	}

	public String getEstadoSecundario() {
		return estadoSecundario;
	}

	public void setEstadoSecundario(String estadoSecundario) {
		this.estadoSecundario = estadoSecundario;
	}
	
	public void setPosOdonto(String value)
	{
		this.infoCompOdont.setStringPosPosiciones(value);
	}
	
	public String getPosOdonto()
	{
		return this.infoCompOdont.getStringPosPosiciones(); 
	}

	public void setNewEstadoProg(String newEstadoProg)
	{
		this.infoCompOdont.setNewEstadoProg(newEstadoProg);
	}

	public void setEstadosServicios(String estadosServicios)
	{
		this.infoCompOdont.setEstadosServicios(estadosServicios);
	}

	public String getSeccionAplicaEst()
	{
		return this.infoCompOdont.getSeccionAplica();
	}
	
	public void setSeccionAplicaEst(String seccionAplica)
	{
		this.infoCompOdont.setSeccionAplica(seccionAplica);
	}
	
	public String getContenedorEst()
	{
		return this.infoCompOdont.getContenedor();
	}
	
	public void setContenedorEst(String contenedor) 
	{
		this.infoCompOdont.setContenedor(contenedor);
	}
	
	public String getSecSeleccionarFormulario() {
		return secSeleccionarFormulario;
	}

	public void setSecSeleccionarFormulario(String secSeleccionarFormulario) {
		this.secSeleccionarFormulario = secSeleccionarFormulario;
	}

	/**
	 * @return the institucionBasica
	 */
	public InstitucionBasica getInstitucionBasica() {
		return institucionBasica;
	}

	/**
	 * @param institucionBasica the institucionBasica to set
	 */
	public void setInstitucionBasica(InstitucionBasica institucionBasica) {
		this.institucionBasica = institucionBasica;
	}

	/**
	 * @return the plantillas
	 */
	public ArrayList<DtoPlantilla> getPlantillas() {
		return plantillas;
	}

	/**
	 * @param plantillas the plantillas to set
	 */
	public void setPlantillas(ArrayList<DtoPlantilla> plantillas) {
		this.plantillas = plantillas;
	}

	/**
	 * @return the paciente
	 */
	public DtoPaciente getPaciente() {
		return paciente;
	}

	/**
	 * @param paciente the paciente to set
	 */
	public void setPaciente(DtoPaciente paciente) {
		this.paciente = paciente;
	}

	/**
	 * @return the usuarioResumen
	 */
	public UsuarioBasico getUsuarioResumen() {
		return usuarioResumen;
	}

	/**
	 * @param usuarioResumen the usuarioResumen to set
	 */
	public void setUsuarioResumen(UsuarioBasico usuarioResumen) {
		this.usuarioResumen = usuarioResumen;
	}

	/**
	 * @return the fechaResumen
	 */
	public String getFechaResumen() {
		return fechaResumen;
	}

	/**
	 * @param fechaResumen the fechaResumen to set
	 */
	public void setFechaResumen(String fechaResumen) {
		this.fechaResumen = fechaResumen;
	}

	/**
	 * @return the horaResumen
	 */
	public String getHoraResumen() {
		return horaResumen;
	}

	/**
	 * @param horaResumen the horaResumen to set
	 */
	public void setHoraResumen(String horaResumen) {
		this.horaResumen = horaResumen;
	}
	
	
	public void setLlamadoSeccionCita(boolean campo)
	{
		this.infoCompOdont.setLlamadoSeccionCita(campo);
	}
	
	public boolean getLlamadoSeccionCita()
	{
		return this.infoCompOdont.isLlamadoSeccionCita();
	}

	/**
	 * @return the esMedicoConfirmacion
	 */
	public String getEsMedicoConfirmacion() {
		return esMedicoConfirmacion;
	}

	/**
	 * @param esMedicoConfirmacion the esMedicoConfirmacion to set
	 */
	public void setEsMedicoConfirmacion(String esMedicoConfirmacion) {
		this.esMedicoConfirmacion = esMedicoConfirmacion;
	}

	/**
	 * @return the facturasAutomaticas
	 */
	public DtoFacturaAutomaticaOdontologica getFacturasAutomaticas() {
		return facturasAutomaticas;
	}

	/**
	 * @param facturasAutomaticas the facturasAutomaticas to set
	 */
	public void setFacturasAutomaticas(
			DtoFacturaAutomaticaOdontologica facturasAutomaticas) {
		this.facturasAutomaticas = facturasAutomaticas;
	}

	/**
	 * 
	 * Metodo para validar que realicen los programas servicios seleccionados para evolucionar
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public ActionErrors validacionesEsProgramasServiciosRealizadosInterno(int institucion) 
	{
		//infoCompOdont.infoPlanTrata.seccionProgServCita[0].detalleSuperficie[0].programasOservicios
		ActionErrors errores= new ActionErrors(); 
	
		for(InfoDetallePlanTramiento infoDetalle: this.getInfoCompOdont().getInfoPlanTrata().getSeccionHallazgosDetalle())
		{
			if(infoDetalle.getExisteBD().isActivo())
			{	
				for(InfoHallazgoSuperficie infoHallazgo: infoDetalle.getDetalleSuperficie())
				{
					if(infoHallazgo.getExisteBD().isActivo())
					{
						for(InfoProgramaServicioPlan infoProgramaServ:  infoHallazgo.getProgramasOservicios())
						{
							if(infoProgramaServ.getCodigoPkProgramaServicio().intValue() > 0) {
								if(infoProgramaServ.getExisteBD().isActivo()) {
									for(InfoServicios servicio: infoProgramaServ.getListaServicios()) {
										if((servicio.getInclusion().equals(ConstantesBD.acronimoNo) || servicio.getInclusion().equals(""))
												&& !servicio.getServicioParCita().equals(ConstantesBD.acronimoNo))
										{
											if(servicio.getInactivarServ().equals(ConstantesBD.acronimoNo))
											{
												if(!servicio.getNewEstado().equals(ConstantesIntegridadDominio.acronimoRealizadoInterno))
												{
													errores.add("", new ActionMessage("errors.notEspecific","El servicio "+servicio.getCodigoMostrar()+" "+servicio.getServicio().getNombre()+" del programa "+infoProgramaServ.getNombreEstadoPrograma()+" debe ser marcado como Realizado Interno"));
												}
											}
										}
									}
								}
							}
						}
					}	
				}
			}	
		}
		
		for(InfoHallazgoSuperficie infoHallazgo: this.getInfoCompOdont().getInfoPlanTrata().getSeccionHallazgosBoca())
		{
			if(infoHallazgo.getExisteBD().isActivo())
			{
				for(InfoProgramaServicioPlan infoProgramaServ:  infoHallazgo.getProgramasOservicios())
				{
					if(infoProgramaServ.getExisteBD().isActivo())
					{
						for(InfoServicios servicio: infoProgramaServ.getListaServicios())
						{
							if((servicio.getInclusion().equals(ConstantesBD.acronimoNo) || servicio.getInclusion().equals(""))
									&& !servicio.getServicioParCita().equals(ConstantesBD.acronimoNo))
							{
								if(servicio.getInactivarServ().equals(ConstantesBD.acronimoNo))
								{
									if(!servicio.getNewEstado().equals(ConstantesIntegridadDominio.acronimoRealizadoInterno))
									{
										errores.add("", new ActionMessage("errors.notEspecific","El servicio "+servicio.getCodigoMostrar()+" "+servicio.getServicio().getNombre()+" del programa "+infoProgramaServ.getNombreEstadoPrograma()+" debe ser marcado como Realizado Interno"));
									}
								}
							}	
						}
					}	
				}
			}
		}

		for(InfoDetallePlanTramiento infoInclusiones: this.getInfoCompOdont().getInfoPlanTrata().getSeccionOtrosHallazgos()) {
			if(infoInclusiones.getExisteBD().isActivo()) {	
				for(InfoHallazgoSuperficie infoDetalleSuperficie: infoInclusiones.getDetalleSuperficie()) {
					if(infoDetalleSuperficie.getExisteBD().isActivo()) {
						for(InfoProgramaServicioPlan infoProgramaServ:  infoDetalleSuperficie.getProgramasOservicios()) {
							if(infoProgramaServ.getCodigoPkProgramaServicio().intValue() > 0) {
								if(infoProgramaServ.getExisteBD().isActivo()) {
									for(InfoServicios servicio: infoProgramaServ.getListaServicios()) {
										if(servicio.getInclusion().equals(ConstantesBD.acronimoSi)
												&& !servicio.getServicioParCita().equals(ConstantesBD.acronimoNo)) {
											if(servicio.getInactivarServ().equals(ConstantesBD.acronimoNo)) {
												if(!servicio.getNewEstado().equals(ConstantesIntegridadDominio.acronimoRealizadoInterno)) {
													errores.add("",new ActionMessage(
															"errors.notEspecific","El servicio " 
															+ servicio.getCodigoMostrar()
															+ " "
															+ servicio.getServicio().getNombre()
															+ " del programa "
															+ infoProgramaServ.getNombreEstadoPrograma()
															+ " debe ser marcado como Realizado Interno"));
												}
											}
										}
									}
								}
							}
						}
					}	
				}
			}	
		}

		return errores;
	}
	
	
	/**
	 * Método que se encarga de evaluar si existen o no servicios pendientes
	 * por evolucionar.
	 * 
	 * @param institucion
	 * @param existeComponenteOdontograma 
	 * @return
	 */
	public String validacionExistenServiPendienteXEvolucionar(int institucion, boolean existeComponenteOdontograma) 
	{
		for(InfoDetallePlanTramiento infoDetalle: this.getInfoCompOdont().getInfoPlanTrata().getSeccionHallazgosDetalle())
		{
			if(infoDetalle.getExisteBD().isActivo())
			{	
				for(InfoHallazgoSuperficie infoHallazgo: infoDetalle.getDetalleSuperficie())
				{
					if(infoHallazgo.getExisteBD().isActivo())
					{
						for(InfoProgramaServicioPlan infoProgramaServ:  infoHallazgo.getProgramasOservicios())
						{
							if(infoProgramaServ.getExisteBD().isActivo())
							{
								for(InfoServicios servicio: infoProgramaServ.getListaServicios())
								{
									if(servicio.getInactivarServ().equals(ConstantesBD.acronimoNo))
									{
										if(servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoContratado) && !servicio.getNewEstado().equals(ConstantesIntegridadDominio.acronimoRealizadoInterno) )
										{
											return ConstantesBD.acronimoSi;											
										}
									}
								}
							}
						}
					}
				}
			}	
		}
		
		for(InfoHallazgoSuperficie infoHallazgo: this.getInfoCompOdont().getInfoPlanTrata().getSeccionHallazgosBoca())
		{
			if(infoHallazgo.getExisteBD().isActivo())
			{
				for(InfoProgramaServicioPlan infoProgramaServ:  infoHallazgo.getProgramasOservicios())
				{
					if(infoProgramaServ.getExisteBD().isActivo())
					{
						for(InfoServicios servicio: infoProgramaServ.getListaServicios())
						{
							if(servicio.getInactivarServ().equals(ConstantesBD.acronimoNo))
							{
								if(servicio.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoContratado) && !servicio.getNewEstado().equals(ConstantesIntegridadDominio.acronimoRealizadoInterno))
								{
									return ConstantesBD.acronimoSi;  
								}									
							}
						}
					}	
				}
			}
		}

		/* 
		 * Si para esta plantilla no existe el componente odontograma se debe evaluar si existen servicios 
		 * sin evolucionar para el paciente.
		 */
		if(!existeComponenteOdontograma){
			
			ArrayList<InfoServicios> listaServiciosContratados = new ArrayList<InfoServicios>();
			
			String codigoTarifarioServicios = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion);

			ArrayList<String> estados = new ArrayList<String>();
			estados.add(ConstantesIntegridadDominio.acronimoEnProceso);
			
			ArrayList<BigDecimal> codigosPlanTratamiento = PlanTratamiento.obtenerCodigoPlanTratamiento(dtoCita.getCodigoPaciente(), estados , ConstantesBD.acronimoNo);
			
			if(codigosPlanTratamiento.size()>0){
				
				for (BigDecimal codigoPlanTratamiento : codigosPlanTratamiento) {
					
					if(codigoPlanTratamiento!=null && codigoPlanTratamiento.intValue()>ConstantesBD.codigoNuncaValido){
						
						DtoProgramasServiciosPlanT parametrosProgServ = new DtoProgramasServiciosPlanT();
						parametrosProgServ.setEstadoServicio(ConstantesIntegridadDominio.acronimoContratado);
						parametrosProgServ.setCodigoTarifario(codigoTarifarioServicios);
						parametrosProgServ.setCodigoPlanTratamiento(codigoPlanTratamiento.intValue());
				
						listaServiciosContratados = PlanTratamiento.cargarServiciosDeProgramasPlanT(parametrosProgServ);
						
						break;
					}
				}
			}
		
			if(listaServiciosContratados.size()>0){
				
				return ConstantesBD.acronimoSi;
				
			}else{
				
				return ConstantesBD.acronimoNo;
			}
			
		}else{
			
			return ConstantesBD.acronimoNo;
		}

	}

	public void setMutiploMinutosGeneracionCita(int mutiploMinutosGeneracionCita) {
		this.mutiploMinutosGeneracionCita = mutiploMinutosGeneracionCita;
	}

	public int getMutiploMinutosGeneracionCita() {
		return mutiploMinutosGeneracionCita;
	}

	/**
	 * @param mensajeInformativo the mensajeInformativo to set
	 */
	public void setMensajeInformativo(String mensajeInformativo) {
		this.mensajeInformativo = mensajeInformativo;
	}

	/**
	 * @return the mensajeInformativo
	 */
	public String getMensajeInformativo() {
		return mensajeInformativo;
	}

	/**
	 * @param codigoProximaCitaRegistrada the codigoProximaCitaRegistrada to set
	 */
	public void setCodigoProximaCitaRegistrada(int codigoProximaCitaRegistrada) {
		this.codigoProximaCitaRegistrada = codigoProximaCitaRegistrada;
	}

	/**
	 * @return the codigoProximaCitaRegistrada
	 */
	public int getCodigoProximaCitaRegistrada() {
		return codigoProximaCitaRegistrada;
	}

	/**
	 * @param estadoTemporal the estadoTemporal to set
	 */
	public void setEstadoTemporal(String estadoTemporal) {
		this.estadoTemporal = estadoTemporal;
	}

	/**
	 * @return the estadoTemporal
	 */
	public String getEstadoTemporal() {
		return estadoTemporal;
	}

	/**
	 * @param abrePopUpProximaCita the abrePopUpProximaCita to set
	 */
	public void setAbrePopUpProximaCita(boolean abrePopUpProximaCita) {
		this.abrePopUpProximaCita = abrePopUpProximaCita;
	}

	/**
	 * @return the abrePopUpProximaCita
	 */
	public boolean isAbrePopUpProximaCita() {
		return abrePopUpProximaCita;
	}

	/**
	 * @param dtoProcesoCitaProgramada the dtoProcesoCitaProgramada to set
	 */
	public void setDtoProcesoCitaProgramada(DtoProcesoCitaProgramada dtoProcesoCitaProgramada) {
		this.dtoProcesoCitaProgramada = dtoProcesoCitaProgramada;
	}

	/**
	 * @return the dtoProcesoCitaProgramada
	 */
	public DtoProcesoCitaProgramada getDtoProcesoCitaProgramada() {
		return dtoProcesoCitaProgramada;
	}

	/**
	 * Método que devuelve la fecha de nacimiento del paciente
	 * en formato mes completo
	 * @return fechaNacimientoPacienteFormateado
	 */
	public String getFechaNacimientoPacienteFormateado() {
		fechaNacimientoPacienteFormateado=UtilidadFecha.conversionFormatoFechaDescripcionMesCompleto(this.paciente.getFechaNacimiento());
		return fechaNacimientoPacienteFormateado;
	}

	/**
	 * Método que almacena el valor del atributo fechaNacimientoPacienteFormateado
	 * @param fechaNacimientoPacienteFormateado
	 */
	public void setFechaNacimientoPacienteFormateado(
			String fechaNacimientoPacienteFormateado) {
		this.fechaNacimientoPacienteFormateado = fechaNacimientoPacienteFormateado;
	}

	/**
	 * Método que devuelve el titulo que debe llevar el reporte en PDF
	 * y debe ser descrito como parámetro
	 * @return titulo
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * Método que almacena el valor del atributo titulo
	 * @param titulo
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * Método que devuelve el tipo de cita como nombre completo
	 * utilizado en el reporte como subtitulo
	 * @return tipoCitaFormateado
	 */
	public String getTipoCitaFormateado() {
		  if(!UtilidadTexto.isEmpty(this.dtoCita.getTipo()))
			{
				if(this.dtoCita.getTipo().equals(ConstantesIntegridadDominio.acronimoAuditoria)){
					this.tipoCitaFormateado="AUDITORÍA";
				}else
				if(this.dtoCita.getTipo().equals(ConstantesIntegridadDominio.acronimoPrioritaria)){
					this.tipoCitaFormateado="ATENCIÓN PRIORITARIA";
				}else
				if(this.dtoCita.getTipo().equals(ConstantesIntegridadDominio.acronimoControlCitaOdon)){
					this.tipoCitaFormateado="CONTROL";	
				}else
				if(this.dtoCita.getTipo().equals(ConstantesIntegridadDominio.acronimoRemisionInterconsulta)){
						this.tipoCitaFormateado="INTERCONSULTA";	
				}else
				if(this.dtoCita.getTipo().equals(ConstantesIntegridadDominio.acronimoTipoAtencionTratamiento)){
						this.tipoCitaFormateado="TRATAMIENTO";	
				}else
				if(this.dtoCita.getTipo().equals(ConstantesIntegridadDominio.acronimoRevaloracion)){
						this.tipoCitaFormateado="REVALORACIÓN";	
				}else
				if(this.dtoCita.getTipo().equals(ConstantesIntegridadDominio.acronimoTipoCitaOdonValoracionInicial)){
						this.tipoCitaFormateado="VALORACIÓN INICIAL";	
				}
				
				
			}
		return tipoCitaFormateado;
	}

	/**
	 * Método que almacena el valor del atributo tipoCitaFormateado
	 * @param tipoCitaFormateado
	 */
	public void setTipoCitaFormateado(String tipoCitaFormateado) {
		this.tipoCitaFormateado = tipoCitaFormateado;
	}

	/**
	 * Método que devuelve el nombre de la plantilla base 
	 * @return plantillaBase
	 */
	public String getPlantillaBase() {
		if(!UtilidadTexto.isEmpty(this.plantillaBase))
			this.plantillaBase=this.plantillaBase.toUpperCase(new Locale("ES"));
		else
			this.plantillaBase="";
		return plantillaBase;
	}


	/**
	 * Método que almacena el valor del atributo plantillaBase
	 * @param plantillaBase
	 */
	public void setPlantillaBase(String plantillaBase) {
		this.plantillaBase = plantillaBase;
	}

	
	/**
	 * Método que almacena el valor del atributo enumTipoSalida
	 * @param enumTipoSalida
	 */
	public void setEnumTipoSalida(EnumTiposSalida enumTipoSalida) {
		this.enumTipoSalida = enumTipoSalida;
	}

	/**
	 * Método que devuelve el tipo de salida que en este caso será PDF 
	 * @return enumTipoSalida
	 */
	public EnumTiposSalida getEnumTipoSalida() {
		return enumTipoSalida;
	}

	
	/**
	 * Método que almacena el valor del atributo nombreArchivoGenerado
	 * @param nombreArchivoGenerado
	 */
	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}

	/**
	 * Método que devuelve el nombre del archivo generado en la
	 * generación del PDF
	 * @return nombreArchivoGenerado
	 */
	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}

	/**
	 * Método que almacena el valor del atributo nombreContexto
	 * @param nombreContexto
	 */
	public void setNombreContexto(String nombreContexto) {
		this.nombreContexto = nombreContexto;
	}

	/**
	 * Método que devuelve el nombre del contexto para
	 * obtener la ruta hacia las imagenes de odontograma e indice 
	 * de placa utilizadas en el reporte
	 * @return nombreContexto
	 */
	public String getNombreContexto() {
		return nombreContexto;
	}

	public void setRedireccion(String redireccion) {
		this.redireccion = redireccion;
	}

	public String getRedireccion() {
		return redireccion;
	}
	
	
	
	
	
	
}