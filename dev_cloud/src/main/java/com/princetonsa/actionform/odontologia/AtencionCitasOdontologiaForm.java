/*
 * Nov 05, 2009
 */
package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.facturacion.DtoFacturaAutomaticaOdontologica;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoInfoAtencionCitaOdo;
import com.princetonsa.dto.odontologia.DtoInicioAtencionCita;
import com.princetonsa.mundo.InstitucionBasica;
import com.servinte.axioma.orm.Ingresos;

/**
 * 
 * Form para el manejo de la Atención de Citas Odontológicas
 * @author Sebastián Gómez R.
 *
 */
public class AtencionCitasOdontologiaForm extends ValidatorForm 
{
	/**
	 * Versión serial
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Estado de control del flujo
	 */
	private String estado ;
	
	/**
	 * Atributo que controla el estado del boton volver
	 */
	private String volver;
	
	private String fecha;
	private String profesionalAgendado;
	private ArrayList<DtoCitaOdontologica> citas;
	private DtoInicioAtencionCita filtroInicioAtencion;
	private String porConfirmar;

	/**
	 * Parámetro para saber si el usuario que ingresa es un odontologo
	 */
	private boolean odontologo;
	
	/**
	 * Arreglo donde se almacenan los profesionales de la salud
	 */
	private ArrayList<HashMap<String, Object>> profesionales;
	
	/**
	 * Posicion de la cita elegida
	 */
	private int posicion;
	
	/**
	 * Atributos para el manejo de la informacion de atencion de citas
	 */
	private DtoInfoAtencionCitaOdo infoAtencionCitaOdo;
	
	/**
	 * Atributo para almacenar la posicion de la plantilla a la cual
	 * se desea ingresar
	 */
	private int posicionPlantilla;
	
	private String secSeleccionarFormulario;
	
	/**
	 * Facturas automaticas posiblemente generadas
	 */
	private DtoFacturaAutomaticaOdontologica facturasAutomaticas;
	
	/**
	 * Institución Básica.
	 * 
	 */
	private InstitucionBasica institucion;
	
	
	private String path;
	private List<Ingresos> listaIngresosPorPaciente;
	private String ingresoSeleccionado;
	private int posicionPlantillaHistorico;
	private int codigoPlantilla;
	
	/**
	 * Almacena un valor que determina si se debe deshabilitar o no la fecha y hora de atención
	 * de la cita según el parámetro general.
	 */
	private boolean deshabilitaFechaHora;
	
	
	/**
	 * Método para limpiar los datos
	 */
	public void reset()
	{
		this.filtroInicioAtencion = new DtoInicioAtencionCita();
		this.estado = "";
		this.fecha = "";
		this.profesionalAgendado = "";
		this.citas = new ArrayList<DtoCitaOdontologica>();
		this.odontologo = false;
		this.profesionales = new ArrayList<HashMap<String,Object>>();
		this.posicion = ConstantesBD.codigoNuncaValido;
		this.codigoPlantilla = ConstantesBD.codigoNuncaValido;
		this.infoAtencionCitaOdo = new DtoInfoAtencionCitaOdo();
		this.posicionPlantilla = ConstantesBD.codigoNuncaValido;
		this.secSeleccionarFormulario=ConstantesBD.acronimoSi;
		this.facturasAutomaticas= new DtoFacturaAutomaticaOdontologica();
		this.institucion = null;
		this.filtroInicioAtencion.setFechaInicioAtencion(UtilidadFecha.getFechaActualTipoBD());
		this.filtroInicioAtencion.setHoraInicioAtencion(UtilidadFecha.getHoraActual());
		this.ingresoSeleccionado=null;
		this.volver="atender";
		this.deshabilitaFechaHora = false;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the profesionalAgendado
	 */
	public String getProfesionalAgendado() {
		return profesionalAgendado;
	}

	/**
	 * @param profesionalAgendado the profesionalAgendado to set
	 */
	public void setProfesionalAgendado(String profesionalAgendado) {
		this.profesionalAgendado = profesionalAgendado;
	}

	/**
	 * @return the citas
	 */
	public ArrayList<DtoCitaOdontologica> getCitas() {
		return citas;
	}

	/**
	 * @param citas the citas to set
	 */
	public void setCitas(ArrayList<DtoCitaOdontologica> citas) {
		this.citas = citas;
	}

	/**
	 * @return the odontologo
	 */
	public boolean isOdontologo() {
		return odontologo;
	}

	/**
	 * @param odontologo the odontologo to set
	 */
	public void setOdontologo(boolean odontologo) {
		this.odontologo = odontologo;
	}

	/**
	 * @return the profesionales
	 */
	public ArrayList<HashMap<String, Object>> getProfesionales() {
		return profesionales;
	}

	/**
	 * @param profesionales the profesionales to set
	 */
	public void setProfesionales(ArrayList<HashMap<String, Object>> profesionales) {
		this.profesionales = profesionales;
	}
	
	/**
	 * Número de profesionales
	 * @return
	 */
	public int getNumProfesionales()
	{
		return this.profesionales.size();
	}
	
	/**
	 * Número de citas
	 * @return
	 */
	public int getNumCitas()
	{
		return this.citas.size();
	}

	/**
	 * @return the posicion
	 */
	public int getPosicion() {
		return posicion;
	}

	/**
	 * @param posicion the posicion to set
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	/**
	 * @return the infoAtencionCitaOdo
	 */
	public DtoInfoAtencionCitaOdo getInfoAtencionCitaOdo() {
		return infoAtencionCitaOdo;
	}

	/**
	 * @param infoAtencionCitaOdo the infoAtencionCitaOdo to set
	 */
	public void setInfoAtencionCitaOdo(DtoInfoAtencionCitaOdo infoAtencionCitaOdo) {
		this.infoAtencionCitaOdo = infoAtencionCitaOdo;
	}

	/**
	 * @return the posicionPlantilla
	 */
	public int getPosicionPlantilla() {
		return posicionPlantilla;
	}

	/**
	 * @param posicionPlantilla the posicionPlantilla to set
	 */
	public void setPosicionPlantilla(int posicionPlantilla) {
		this.posicionPlantilla = posicionPlantilla;
	}

	public String getSecSeleccionarFormulario() {
		return secSeleccionarFormulario;
	}

	public void setSecSeleccionarFormulario(String secSeleccionarFormulario) {
		this.secSeleccionarFormulario = secSeleccionarFormulario;
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
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(InstitucionBasica institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the institucion
	 */
	public InstitucionBasica getInstitucion() {
		return institucion;
	}

	/**
	 * Este método se encarga de realizar las validaciones de 
	 * los datos ingresados por el usuario
	 * 
	 * @param ActionMapping
	 * @param HttpServletRequest
	 * @return ActionErrors
	 * 
	 *  @author Fabian Becerra
	 */
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1){
		// TODO Auto-generated method stub
		ActionErrors errores=null;
		errores=new ActionErrors();
		if(estado.equals("atender")){
			
			this.setPorConfirmar(citas.get(posicion).getPorConfirmar());
			
		}else
		if(estado.equals("abrirPlantilla")){
			
			//validaciones rango de fecha inicio atencion
			if(this.filtroInicioAtencion.getFechaInicioAtencion()==null){
				
				errores.add("La fecha de inicio de Atención es requerida", 
						new ActionMessage("errors.required", "El campo Fecha Inicio Atención"));
				
			}
			if(UtilidadTexto.isEmpty(this.filtroInicioAtencion.getHoraInicioAtencion())){
				errores.add("La hora de inicio de Atención es requerida", 
						new ActionMessage("errors.required", "El campo Hora Inicio Atención"));
				
			}
			
			
			if(this.filtroInicioAtencion.getFechaInicioAtencion()!=null&&!UtilidadTexto.isEmpty(this.filtroInicioAtencion.getHoraInicioAtencion()))
			{
				String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
						this.filtroInicioAtencion.getFechaInicioAtencion());
				String fechaActual = UtilidadFecha.getFechaActual();
				String horaActual=UtilidadFecha.getHoraActual();
				
				String fechaAsignacionCita=citas.get(posicion).getFechaModifica();
				fechaAsignacionCita=UtilidadFecha.conversionFormatoFechaAAp(fechaAsignacionCita);
				String horaAsignacionCita=citas.get(posicion).getHoraModifica();
				
				ResultadoBoolean mayor=UtilidadFecha.compararFechas(fechaInicial,filtroInicioAtencion.getHoraInicioAtencion(),fechaAsignacionCita,horaAsignacionCita);
				ResultadoBoolean menor=UtilidadFecha.compararFechasMenorOIgual(fechaInicial,filtroInicioAtencion.getHoraInicioAtencion(),fechaActual,horaActual);
				
				if(!mayor.isResultado()){
					errores.add("FECHA INICIAL MAYOR QUE FECHA ACTUAL.", new ActionMessage(
							 "errors.fechaPosteriorIgualActual"," Asignación Cita "+fechaAsignacionCita+" "+horaAsignacionCita," Inicio Atención "+fechaInicial+" "+filtroInicioAtencion.getHoraInicioAtencion()));
				}else
				if(menor.isResultado()){
						
					errores.add("FECHA INICIAL MAYOR QUE FECHA ACTUAL.", new ActionMessage(
										 "errors.fechaPosteriorIgualActual"," Inicio Atención "+fechaInicial+" "+filtroInicioAtencion.getHoraInicioAtencion()," Actual "+fechaActual+" "+horaActual));
				}
								
				
				
				
			}
			
			
		}
		
			
	     return errores;
	}

	public void setPorConfirmar(String porConfirmar) {
		this.porConfirmar = porConfirmar;
	}

	public String getPorConfirmar() {
		return porConfirmar;
	}

	public DtoInicioAtencionCita getFiltroInicioAtencion() {
		return filtroInicioAtencion;
	}

	public void setFiltroInicioAtencion(DtoInicioAtencionCita filtroInicioAtencion) {
		this.filtroInicioAtencion = filtroInicioAtencion;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setListaIngresosPorPaciente(List<Ingresos> listaIngresosPorPaciente) {
		this.listaIngresosPorPaciente = listaIngresosPorPaciente;
	}

	public List<Ingresos> getListaIngresosPorPaciente() {
		return listaIngresosPorPaciente;
	}

	public void setIngresoSeleccionado(String ingresoSeleccionado) {
		this.ingresoSeleccionado = ingresoSeleccionado;
	}

	public String getIngresoSeleccionado() {
		return ingresoSeleccionado;
	}

	public void setVolver(String volver) {
		this.volver = volver;
	}

	public String getVolver() {
		return volver;
	}

	public void setPosicionPlantillaHistorico(int posicionPlantillaHistorico) {
		this.posicionPlantillaHistorico = posicionPlantillaHistorico;
	}

	public int getPosicionPlantillaHistorico() {
		return posicionPlantillaHistorico;
	}

	public void setCodigoPlantilla(int codigoPlantilla) {
		this.codigoPlantilla = codigoPlantilla;
	}

	public int getCodigoPlantilla() {
		return codigoPlantilla;
	}

	/**
	 * @return Retorna el atributo deshabilitaFechaHora
	 */
	public boolean isDeshabilitaFechaHora() {
		return deshabilitaFechaHora;
	}

	/**
	 * @param deshabilitaFechaHora Asigna el atributo deshabilitaFechaHora
	 */
	public void setDeshabilitaFechaHora(boolean deshabilitaFechaHora) {
		this.deshabilitaFechaHora = deshabilitaFechaHora;
	}
}
