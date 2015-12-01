package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.manejoPaciente.DtoInformeInconsisenBD;
import com.princetonsa.dto.manejoPaciente.DtoVariablesIncorrectasenBD;

import util.ConstantesBD;

public class RegistroEnvioInformInconsisenBDForm extends ValidatorForm{

	/**
	 * Maneja el estado del action
	 * */
	private String estado;
	
	/**
	 *  Mapa que contiene los ingresos asociados a un paciente
	 */
    private HashMap ingresosPaciente;

    /**
     *  Mapa que contiene los convenios Responsables asociados a un paciente
     */
    private HashMap conveniosResponsables;
    
    /**
     *  Dto que contiene los datos del Informe de Iconsistencias
     */
    private DtoInformeInconsisenBD informeIconsistencias = new DtoInformeInconsisenBD();
    
    /**
     * Mapa que contiene los tipos de Incosistencias
     */
    private HashMap tiposInconsistencias;
    
    /**
     * Contiene el tipo de inconsistencia 
     */
    private String inconsistencia;
    
    /**
	 * Almacena los parametros para filtro desde un llamado a esta funcionalidad
	 * */
	private HashMap parametrosFiltros = new HashMap();
	
	/**
	 *  Mapa que Almacena los tipos de Identificacion existentes en el sistema 
	 */
	private HashMap tiposIdentificacion = new HashMap();
	
	/**
	 * Mapa que almacena las diferentes posibles variables incorrectas existentes en el sistema
	 */
	private HashMap mapVariablesIncorrectas=new HashMap();
	
	/**
	 * Arreglo que almacena los tipos de Identificacion que no manejan consecutivo
	 */
	private ArrayList<HashMap<String, Object>> arrayTiposIdentificacion;
	
	/**
	 * Arreglo que contiene las Variabes Incorrectas del Informe de Inconsistencias en BD
	 */
	private ArrayList< DtoVariablesIncorrectasenBD> arrayVariablesIncorrectas;
	
	/**
	 * Almacena el listado de ciudades
	 */
	private HashMap ciudades=new HashMap();
	
	/**
	 * Almacena el listado de departamentos
	 */
	private HashMap  departamentos = new HashMap();
	
	/**
	 *  Almacena el listado de empresas con Indicador Direccion Territorial = Si
	 */
    private ArrayList<HashMap<String, Object>> arrayEmpresas;
	
    /**
	 * Almacena la informacion de los Medios de Envio
	 * */
	private ArrayList<HashMap<String, Object>> listadoArrayMediosEnvio;
	
	/**
	 * Almacena las variables incorrectas del informe
	 */
	private String[] seleccionVariablesInco;
    
	/**
	 * Contiene la posicion  del ingreso dentro del listado de Ingresos asociados al  Paciente
	 */
	private int posIngreso;
	
	/**
	 * Contiene la posicion  del convenio dentro del listado de Convenios asociados al  Paciente
	 */
	private int posConvenio;
	
	/**
	 * Almacena el codigo del ingreso
	 */
	private String codigoIngreso;
	
	/**
	 * Almacena la cuenta 
	 */
	private String cuenta;
	
	/**
	 * Almacena el nombre del paciente
	 */
	private  String nomPaciente;
	
	/**
	 * Contiene el estado del Tipo de Incosistencia seleccionado, se utiliza para filtrar las 
	 * variables incorrectas
	 */
	private String filtroTipoInconsistencia;
	
	/**
	 * Contiene la confirmacion del envio
	 */
	private String confirmarEnvio;
	
	/**
	 * Establece si el Informe ya fue enviado
	 */
	private String estadoEnviado;
	
	/**
	 * Contiene las diferentes coberturas de salud de un convenio segun el tipo de regimen
	 */
	private HashMap coberturasSalud;
	
	/**
	 * Indica que la funcionalidad es de registro y envio Inconsistencias
	 */
	private String esFuncRegEnvio;

	/**
	 * 
	 */
	public RegistroEnvioInformInconsisenBDForm()
	{
	 this.reset();	
	}
	
	
	public void reset()
	{
		this.estado=new String("");
		this.confirmarEnvio=new String("N");
		this.estadoEnviado=new String("");
		this.posIngreso=ConstantesBD.codigoNuncaValido;
		this.filtroTipoInconsistencia=new String("");
		this.informeIconsistencias=new DtoInformeInconsisenBD();
		this.ingresosPaciente=new HashMap();
		this.ingresosPaciente.put("numRegistros", "0");
		this.conveniosResponsables=new HashMap();
		this.conveniosResponsables.put("numRegistros","0");
		this.tiposInconsistencias=new HashMap();
		this.tiposInconsistencias.put("numRegistros", "0");
		this.inconsistencia=new String("");
		this.tiposIdentificacion=new HashMap();
		this.tiposIdentificacion.put("numRegistros", "0");
		this.ciudades=new HashMap();
		this.ciudades.put("numRegistros", "0");
		this.departamentos=new HashMap();
		this.departamentos.put("numRegistros", "0");
		this.mapVariablesIncorrectas=new HashMap();
		this.mapVariablesIncorrectas.put("numRegistros", "0");
		this.coberturasSalud=new HashMap();
		this.coberturasSalud.put("numRegistros","0");
		this.arrayTiposIdentificacion=new ArrayList<HashMap<String, Object>>();
		this.arrayVariablesIncorrectas=new ArrayList<DtoVariablesIncorrectasenBD>();
		this.arrayEmpresas=new ArrayList<HashMap<String, Object>>();
		this.listadoArrayMediosEnvio=new ArrayList<HashMap<String, Object>>();
		this.seleccionVariablesInco=new String [100];
		this.parametrosFiltros.put("operacionExitosa",ConstantesBD.acronimoNo);
		this.codigoIngreso=new String("");
        this.cuenta=new String("");
        this.nomPaciente=new String("");
        this.esFuncRegEnvio=new String(ConstantesBD.acronimoSi);
	}
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public HashMap getIngresosPaciente() {
		return ingresosPaciente;
	}

	public void setIngresosPaciente(HashMap ingresosPaciente) {
		this.ingresosPaciente = ingresosPaciente;
	}

	public HashMap getConveniosResponsables() {
		return conveniosResponsables;
	}

	public void setConveniosResponsables(HashMap conveniosResponsables) {
		this.conveniosResponsables = conveniosResponsables;
	}
	
	
	public Object getParametrosFiltros(String key) {
		return parametrosFiltros.get(key);
	}

	public void setParametrosFiltros(String key, Object value) {
		this.parametrosFiltros.put(key,value);
	}
	
	public String getCuentaFiltro() 
	{
		if(this.parametrosFiltros.get("cuentaFiltro")!=null)
		{
			return this.parametrosFiltros.get("cuentaFiltro").toString();
		}
		else
		{
			return "";
		}
	}

	public void setCuentaFiltro(String valor) {
		this.parametrosFiltros.put("cuentaFiltro",valor);
	}
	
	public String getCodigoPkInforme() 
	{
		if(this.parametrosFiltros.get("codigoPkInforme")!=null)
		{
			return this.parametrosFiltros.get("codigoPkInforme").toString();
		}
		else
		{
			return "";
		}
	}

	public void setCodigoPkInforme(String valor) {
		this.parametrosFiltros.put("codigoPkInforme",valor);
	}
	
	public String getIngresoFiltro() 
	{
		if(this.parametrosFiltros.get("ingresoFiltro")!=null)
		{
			return this.parametrosFiltros.get("ingresoFiltro").toString();
		}
		else
		{
			return "";
		}
		
	}
	
	public void setIngresoFiltro(String valor) {
		this.parametrosFiltros.put("ingresoFiltro",valor);
	}
	
	public void setViaIngresoFiltro(String valor) {
		this.parametrosFiltros.put("viaIngresoFiltro",valor);
	}
	
	public String getViaIngresoFiltro() 
	{
		if(this.parametrosFiltros.get("viaIngresoFiltro")!=null)
		{
			return this.parametrosFiltros.get("viaIngresoFiltro").toString();
		}
		else
		{
			return "";
		}
	}
	
	public String getConvenioFiltro() 
	{
		if(this.parametrosFiltros.get("convenioFiltro")!=null)
		{
			return this.parametrosFiltros.get("convenioFiltro").toString();
		}
		else
		{
			return "";
		}
	}

	public void setConvenioFiltro(String valor) {
		this.parametrosFiltros.put("convenioFiltro",valor);
	}
	
	public void setSubCuentaFiltro(String valor) {
		this.parametrosFiltros.put("subCuentaFiltro",valor);
	}
	
	public String getSubCuentaFiltro() 
	{
		if(this.parametrosFiltros.get("subCuentaFiltro")!=null)
		{
			return this.parametrosFiltros.get("subCuentaFiltro").toString();
		}
		else
		{
			return "";
		}
	}
	
	public String getConvenioEnvioFiltro() 
	{
		if(this.parametrosFiltros.get("convenioEnvioFiltro")!=null)
		{
			return this.parametrosFiltros.get("convenioEnvioFiltro").toString();
		}
		else
		{
			return "";
		}
	}

	public void setConvenioEnvioFiltro(String valor) {
		this.parametrosFiltros.put("convenioEnvioFiltro",valor);
	}
	
	public String getFechaInicialEnvio() 
	{
		if(this.parametrosFiltros.get("fechaInicialEnvio")!=null)
		{
			return this.parametrosFiltros.get("fechaInicialEnvio").toString();
		}
		else
		{
			return "";
		}
	}

	public void setFechaInicialEnvio(String valor) {
		this.parametrosFiltros.put("fechaInicialEnvio",valor);
	}
	
	public String getFechaFinalEnvio() 
	{
		if(this.parametrosFiltros.get("fechaFinalEnvio")!=null)
		{
			return this.parametrosFiltros.get("fechaFinalEnvio").toString();
		}
		else
		{
			return "";
		}
	}

	public void setFechaFinalEnvio(String valor) {
		this.parametrosFiltros.put("fechaFinalEnvio",valor);
	}


	public int getPosIngreso() {
		return posIngreso;
	}


	public void setPosIngreso(int posIngreso) {
		this.posIngreso = posIngreso;
	}


	public DtoInformeInconsisenBD getInformeIconsistencias() {
		return informeIconsistencias;
	}


	public void setInformeIconsistencias(
			DtoInformeInconsisenBD informeIconsistencias) {
		this.informeIconsistencias = informeIconsistencias;
	}


	public HashMap getParametrosFiltros() {
		return parametrosFiltros;
	}


	public void setParametrosFiltros(HashMap parametrosFiltros) {
		this.parametrosFiltros = parametrosFiltros;
	}


	public HashMap getTiposInconsistencias() {
		return tiposInconsistencias;
	}


	public void setTiposInconsistencias(HashMap tiposInconsistencias) {
		this.tiposInconsistencias = tiposInconsistencias;
	}

	
	public Object getTiposInconsistencias(String key) {
		return tiposInconsistencias.get(key);
	}

	public void setTiposInconsistencias(String key, Object value) {
		this.tiposInconsistencias.put(key,value);
	}

	public String getInconsistencia() {
		return inconsistencia;
	}

	public void setInconsistencia(String inconsistencia) {
		this.inconsistencia = inconsistencia;
	}


	public HashMap getTiposIdentificacion() {
		return tiposIdentificacion;
	}


	public void setTiposIdentificacion(HashMap tiposIdentificacion) {
		this.tiposIdentificacion = tiposIdentificacion;
	}


	public HashMap getCiudades() {
		return ciudades;
	}


	public void setCiudades(HashMap ciudades) {
		this.ciudades = ciudades;
	}


	public HashMap getDepartamentos() {
		return departamentos;
	}


	public void setDepartamentos(HashMap departamentos) {
		this.departamentos = departamentos;
	}


	public ArrayList<HashMap<String, Object>> getArrayTiposIdentificacion() {
		return arrayTiposIdentificacion;
	}


	public void setArrayTiposIdentificacion(
			ArrayList<HashMap<String, Object>> arrayTiposIdentificacion) {
		this.arrayTiposIdentificacion = arrayTiposIdentificacion;
	}


	public HashMap getMapVariablesIncorrectas() {
		return mapVariablesIncorrectas;
	}


	public void setMapVariablesIncorrectas(HashMap mapVariablesIncorrectas) {
		this.mapVariablesIncorrectas = mapVariablesIncorrectas;
	}


	public ArrayList<DtoVariablesIncorrectasenBD> getArrayVariablesIncorrectas() {
		return arrayVariablesIncorrectas;
	}


	public void setArrayVariablesIncorrectas(
			ArrayList<DtoVariablesIncorrectasenBD> arrayVariablesIncorrectas) {
		this.arrayVariablesIncorrectas = arrayVariablesIncorrectas;
	}


	public ArrayList<HashMap<String, Object>> getArrayEmpresas() {
		return arrayEmpresas;
	}


	public void setArrayEmpresas(ArrayList<HashMap<String, Object>> arrayEmpresas) {
		this.arrayEmpresas = arrayEmpresas;
	}


	public ArrayList<HashMap<String, Object>> getListadoArrayMediosEnvio() {
		return listadoArrayMediosEnvio;
	}


	public void setListadoArrayMediosEnvio(
			ArrayList<HashMap<String, Object>> listadoArrayMediosEnvio) {
		this.listadoArrayMediosEnvio = listadoArrayMediosEnvio;
	}


	public String getFiltroTipoInconsistencia() {
		return filtroTipoInconsistencia;
	}


	public void setFiltroTipoInconsistencia(String filtroTipoInconsistencia) {
		this.filtroTipoInconsistencia = filtroTipoInconsistencia;
	}


	public String getConfirmarEnvio() {
		return confirmarEnvio;
	}


	public void setConfirmarEnvio(String confirmarEnvio) {
		this.confirmarEnvio = confirmarEnvio;
	}


	public String getEstadoEnviado() {
		return estadoEnviado;
	}


	public void setEstadoEnviado(String estadoEnviado) {
		this.estadoEnviado = estadoEnviado;
	}


	public String[] getSeleccionVariablesInco() {
		return seleccionVariablesInco;
	}

	public String getSeleccionVariablesInco(int pos)
	{
	   return seleccionVariablesInco[pos];	
	}
	
	public void setSeleccionVariablesInco(String nombre, int pos)
	{
		seleccionVariablesInco[pos]=nombre;
		
	}

	public void setSeleccionVariablesInco(String[] seleccionVariablesInco) {
		this.seleccionVariablesInco = seleccionVariablesInco;
	}


	public HashMap getCoberturasSalud() {
		return coberturasSalud;
	}


	public void setCoberturasSalud(HashMap coberturasSalud) {
		this.coberturasSalud = coberturasSalud;
	}


	public String getCodigoIngreso() {
		return codigoIngreso;
	}


	public void setCodigoIngreso(String codigoIngreso) {
		this.codigoIngreso = codigoIngreso;
	}


	public String getCuenta() {
		return cuenta;
	}


	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}


	public String getNomPaciente() {
		return nomPaciente;
	}


	public void setNomPaciente(String nomPaciente) {
		this.nomPaciente = nomPaciente;
	}


	public int getPosConvenio() {
		return posConvenio;
	}


	public void setPosConvenio(int posConvenio) {
		this.posConvenio = posConvenio;
	}


	public String getEsFuncRegEnvio() {
		return esFuncRegEnvio;
	}


	public void setEsFuncRegEnvio(String esFuncRegEnvio) {
		this.esFuncRegEnvio = esFuncRegEnvio;
	}

}
