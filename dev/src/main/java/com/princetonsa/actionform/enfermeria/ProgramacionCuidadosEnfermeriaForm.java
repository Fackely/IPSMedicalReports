package com.princetonsa.actionform.enfermeria;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.enfermeria.DtoCuidadosEnfermeria;
import com.princetonsa.dto.enfermeria.DtoFrecuenciaCuidadoEnferia;

public class ProgramacionCuidadosEnfermeriaForm extends ValidatorForm 
{	
	private String estado;
	
	private String areaFiltro;
	
	private String pisoFiltro;
	
	private String habitacionFiltro;
	
	private String programados;
	
	private String codigoPkFrecCuidadosEnferFiltro;
	
	private String codigoCuidadoEnferFiltro;
	
	private boolean esOtroCuidadoFiltro;
	
	private int indicePaciente;
	
	private int indiceCuidado;
	
	private HashMap mapaListadoPacientes;
	
	private DtoFrecuenciaCuidadoEnferia frecuenciaCuidados;
	
	private DtoCuidadosEnfermeria dtoCuidadosEnfermeria;
	
	private ArrayList<DtoFrecuenciaCuidadoEnferia> cuidados;
	
	private ArrayList<DtoFrecuenciaCuidadoEnferia> cuidadosTemporal;
	
	private ArrayList<DtoCuidadosEnfermeria> programacionCuidados;
	
	private ArrayList<HashMap<String,Object>> arrayTipoFrecuencias;
	
	private ResultadoBoolean mostrarMensaje;
	
	private String programarMasivo;
	
	private String programarPaciente;
	
	private String puedeProgramar;


	/**
	 * 
	 * 
	 */
	public void reset()
	{
		this.estado="";
		this.areaFiltro="";
		this.pisoFiltro="";
		this.habitacionFiltro="";
		this.programados="";
		this.mapaListadoPacientes = new HashMap();
		this.mapaListadoPacientes.put("numRegistros", "0");
		this.indicePaciente=ConstantesBD.codigoNuncaValido;
		this.indiceCuidado=ConstantesBD.codigoNuncaValido;
		this.codigoPkFrecCuidadosEnferFiltro = "";
		this.codigoCuidadoEnferFiltro = "";
		this.esOtroCuidadoFiltro = false;
		
		this.frecuenciaCuidados = new DtoFrecuenciaCuidadoEnferia();
		this.cuidados = new ArrayList<DtoFrecuenciaCuidadoEnferia>();
		this.cuidadosTemporal = new ArrayList<DtoFrecuenciaCuidadoEnferia>();
		this.arrayTipoFrecuencias = new ArrayList<HashMap<String,Object>>();
		this.programacionCuidados = new ArrayList<DtoCuidadosEnfermeria>();
		this.dtoCuidadosEnfermeria = new DtoCuidadosEnfermeria();
		
		this.mostrarMensaje= new ResultadoBoolean(false, "");
		this.programarMasivo=ConstantesBD.acronimoNo;
		this.programarPaciente=ConstantesBD.acronimoNo;
		this.puedeProgramar=ConstantesBD.acronimoNo;
	}

	
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("consultaListado"))
		{
			if(this.areaFiltro.equals("")&&this.pisoFiltro.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.notEspecific","Debe seleccionar un Area o un piso para realizar su busqueda"));
			}
			/*if(!this.tipoDeudor.equals("")&&this.deudor.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","El Deudor "));
			}*/
		}
		return errores;
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return
	 */
	public String getAreaFiltro() {
		return areaFiltro;
	}

	/**
	 * 
	 * @param areaFiltro
	 */
	public void setAreaFiltro(String areaFiltro) {
		this.areaFiltro = areaFiltro;
	}

	/**
	 * 
	 * @return
	 */
	public String getPisoFiltro() {
		return pisoFiltro;
	}

	/**
	 * 
	 * @param pisoFiltro
	 */
	public void setPisoFiltro(String pisoFiltro) {
		this.pisoFiltro = pisoFiltro;
	}

	/**
	 * 
	 * @return
	 */
	public String getHabitacionFiltro() {
		return habitacionFiltro;
	}

	/**
	 * 
	 * @param habitacionFiltro
	 */
	public void setHabitacionFiltro(String habitacionFiltro) {
		this.habitacionFiltro = habitacionFiltro;
	}

	/**
	 * 
	 * @return
	 */
	public String getProgramados() {
		return programados;
	}

	/**
	 * 
	 * @param programados
	 */
	public void setProgramados(String programados) {
		this.programados = programados;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaListadoPacientes() {
		return mapaListadoPacientes;
	}

	/**
	 * 
	 * @param mapaListadoPacientes
	 */
	public void setMapaListadoPacientes(HashMap mapaListadoPacientes) {
		this.mapaListadoPacientes = mapaListadoPacientes;
	}

	/**
	 * 
	 * @return
	 */
	public DtoFrecuenciaCuidadoEnferia getFrecuenciaCuidados() {
		return frecuenciaCuidados;
	}

	/**
	 * 
	 * @param frecuenciaCuidados
	 */
	public void setFrecuenciaCuidados(DtoFrecuenciaCuidadoEnferia frecuenciaCuidados) {
		this.frecuenciaCuidados = frecuenciaCuidados;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoFrecuenciaCuidadoEnferia> getCuidados() {
		return cuidados;
	}
	
	/**
	 * 
	 * @param cuidados
	 */
	public void setCuidados(ArrayList<DtoFrecuenciaCuidadoEnferia> cuidados) {
		this.cuidados = cuidados;
	}

	/**
	 * 
	 * @return
	 */
	public int getIndicePaciente() {
		return indicePaciente;
	}

	/**
	 * 
	 * @param indicePaciente
	 */
	public void setIndicePaciente(int indicePaciente) {
		this.indicePaciente = indicePaciente;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getArrayTipoFrecuencias() {
		return arrayTipoFrecuencias;
	}

	/**
	 * 
	 * @param arrayTipoFrecuencias
	 */
	public void setArrayTipoFrecuencias(
			ArrayList<HashMap<String, Object>> arrayTipoFrecuencias) {
		this.arrayTipoFrecuencias = arrayTipoFrecuencias;
	}

	/**
	 * @return the programacionCuidados
	 */
	public ArrayList<DtoCuidadosEnfermeria> getProgramacionCuidados() {
		return programacionCuidados;
	}

	/**
	 * @param programacionCuidados the programacionCuidados to set
	 */
	public void setProgramacionCuidados(
			ArrayList<DtoCuidadosEnfermeria> programacionCuidados) {
		this.programacionCuidados = programacionCuidados;
	}

	/**
	 * @return the codigoPkFrecCuidadosEnferFiltro
	 */
	public String getCodigoPkFrecCuidadosEnferFiltro() {
		return codigoPkFrecCuidadosEnferFiltro;
	}

	/**
	 * @param codigoPkFrecCuidadosEnferFiltro the codigoPkFrecCuidadosEnferFiltro to set
	 */
	public void setCodigoPkFrecCuidadosEnferFiltro(
			String codigoPkFrecCuidadosEnferFiltro) {
		this.codigoPkFrecCuidadosEnferFiltro = codigoPkFrecCuidadosEnferFiltro;
	}

	/**
	 * @return the codigoCuidadoEnferFiltro
	 */
	public String getCodigoCuidadoEnferFiltro() {
		return codigoCuidadoEnferFiltro;
	}

	/**
	 * @param codigoCuidadoEnferFiltro the codigoCuidadoEnferFiltro to set
	 */
	public void setCodigoCuidadoEnferFiltro(String codigoCuidadoEnferFiltro) {
		this.codigoCuidadoEnferFiltro = codigoCuidadoEnferFiltro;
	}

	/**
	 * @return the esOtroCuidadoFiltro
	 */
	public boolean isEsOtroCuidadoFiltro() {
		return esOtroCuidadoFiltro;
	}

	/**
	 * @param esOtroCuidadoFiltro the esOtroCuidadoFiltro to set
	 */
	public void setEsOtroCuidadoFiltro(boolean esOtroCuidadoFiltro) {
		this.esOtroCuidadoFiltro = esOtroCuidadoFiltro;
	}

	/**
	 * @return the dtoCuidadosEnfermeria
	 */
	public DtoCuidadosEnfermeria getDtoCuidadosEnfermeria() {
		return dtoCuidadosEnfermeria;
	}
     
	/**
	 * @param dtoCuidadosEnfermeria the dtoCuidadosEnfermeria to set
	 */
	public void setDtoCuidadosEnfermeria(DtoCuidadosEnfermeria dtoCuidadosEnfermeria) {
		this.dtoCuidadosEnfermeria = dtoCuidadosEnfermeria;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getIndiceCuidado() {
		return indiceCuidado;
	}

	/**
	 * 
	 * @param indiceCuidado
	 */
	public void setIndiceCuidado(int indiceCuidado) {
		this.indiceCuidado = indiceCuidado;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoFrecuenciaCuidadoEnferia> getCuidadosTemporal() {
		return cuidadosTemporal;
	}

	/**
	 * 
	 * @param cuidadosTemporal
	 */
	public void setCuidadosTemporal(
			ArrayList<DtoFrecuenciaCuidadoEnferia> cuidadosTemporal) {
		this.cuidadosTemporal = cuidadosTemporal;
	}
	
	/**
	 * 
	 * @return
	 */
	public ResultadoBoolean getMostrarMensaje() {
		return mostrarMensaje;
	}

	/**
	 * 
	 * @param mostrarMensaje
	 */
	public void setMostrarMensaje(ResultadoBoolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getProgramarMasivo() {
		return programarMasivo;
	}

	/**
	 * 
	 * @param programarMasivo
	 */
	public void setProgramarMasivo(String programarMasivo) {
		this.programarMasivo = programarMasivo;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getProgramarPaciente() {
		return programarPaciente;
	}

	/**
	 * 
	 * @param programarPaciente
	 */
	public void setProgramarPaciente(String programarPaciente) {
		this.programarPaciente = programarPaciente;
	}

	/**
	 * 
	 * @return
	 */
	public String getPuedeProgramar() {
		return puedeProgramar;
	}

	/**
	 * 
	 * @param puedeProgramar
	 */
	public void setPuedeProgramar(String puedeProgramar) {
		this.puedeProgramar = puedeProgramar;
	}
	
	
}