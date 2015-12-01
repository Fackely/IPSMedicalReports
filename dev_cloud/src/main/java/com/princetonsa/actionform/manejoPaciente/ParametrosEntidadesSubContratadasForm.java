package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;



public class ParametrosEntidadesSubContratadasForm extends ValidatorForm
{

	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manjar los logger de la clase ParametrosEntidadesSubContratadasForm
	 */
	Logger logger = Logger.getLogger(ParametrosEntidadesSubContratadasForm.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
	/*---------------------------------------------------
	 * ATRIBUTOS DE PARAMETROS ENTIDADES SUBCONTRATADAS
	 ---------------------------------------------------*/
	
	/**
	 * String estado; este es el encargado de administrar
	 * las acciones dentro del Action.
	 */
	private String estado;
	
	private String estadoAnt;
	/**
	 * almacenas los convenios
	 */
	private ArrayList<HashMap<String, Object>> convenios = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * almacena las vias de ingreso
	 */
	private ArrayList<HashMap<String, Object>> viasIngreso = new ArrayList<HashMap<String,Object>>();
		
	private ArrayList<HashMap<String, Object>> tiposPaciente = new ArrayList<HashMap<String,Object>>();
		
	private ArrayList<HashMap<String, Object>> area = new ArrayList<HashMap<String,Object>>();
	/**
	 * almacena los datos almacenados en la jsp
	 */
	private HashMap parametros = new HashMap ();
	
	private HashMap parametrosClone = new HashMap ();
	
	private ArrayList<HashMap<String, Object>> montosCobro = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * indica las partes e o secciones en las que esta dividida la jsp
	 */
	private ArrayList<HashMap<String, Object>> secciones = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * atributo encargado de almacenar los orignes de admision a listar en el select 
	 */
	private ArrayList<HashMap<String, Object>> origenAdmision = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * almacena los centros de atencion
	 */
	private ArrayList<HashMap<String, Object>> centrosAtencion = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * almacena los profecionales del sistema
	 */
	private ArrayList<HashMap<String, Object>> profecionales = new ArrayList<HashMap<String,Object>>();
	/**
	 * almacena las farmacias para cargos directos
	 */
	private ArrayList<HashMap<String, Object>> farmacia = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Indica la seccion que se selecciono
	 */
	private int codigoSeccion;
	
	/**
	 * Para guardar las especialidades por medico
	 * @return
	 */
	private HashMap especialidades= new HashMap();
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Numero de elementos de tipo paciente
	 */
	private int numElementosTP = ConstantesBD.codigoNuncaValido;


	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
	
	
	
	/*-----------------------------------------------------
	 * FIN ATRIBUTOS DE PARAMETROS ENTIDADES SUBCONTRATADAS
	 -----------------------------------------------------*/
	
	
	
	/*----------------------------------------------------
	 * 		METODOS SETTERS AND GETTERS
	 -----------------------------------------------------*/	
	

	public int getCodigoSeccion() {
		return codigoSeccion;
	}


	public void setCodigoSeccion(int codigoSeccion) {
		this.codigoSeccion = codigoSeccion;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	

	public ArrayList<HashMap<String, Object>> getArea() {
		return area;
	}


	public void setArea(ArrayList<HashMap<String, Object>> area) {
		this.area = area;
	}


	public ArrayList<HashMap<String, Object>> getConvenios() {
		return convenios;
	}


	public void setConvenios(ArrayList<HashMap<String, Object>> convenios) {
		this.convenios = convenios;
	}

	public ArrayList<HashMap<String, Object>> getOrigenAdmision() {
		return origenAdmision;
	}


	public void setOrigenAdmision(ArrayList<HashMap<String, Object>> origenAdmision) {
		this.origenAdmision = origenAdmision;
	}


	public HashMap getParametros() {
		return parametros;
	}


	public void setParametros(HashMap parametros) {
		this.parametros = parametros;
	}

	
	public Object getParametros(String key) {
		return parametros.get(key);
	}


	public void setParametros(String key,Object value) {
		this.parametros.put(key, value);
	}

	public ArrayList<HashMap<String, Object>> getTiposPaciente() {
		return tiposPaciente;
	}


	public void setTiposPaciente(ArrayList<HashMap<String, Object>> tiposPaciente) {
		this.tiposPaciente = tiposPaciente;
	}


	public ArrayList<HashMap<String, Object>> getViasIngreso() {
		return viasIngreso;
	}


	public void setViasIngreso(ArrayList<HashMap<String, Object>> viasIngreso) {
		this.viasIngreso = viasIngreso;
	}


	

	public ArrayList<HashMap<String, Object>> getSecciones() {
		return secciones;
	}


	public void setSecciones(ArrayList<HashMap<String, Object>> secciones) {
		this.secciones = secciones;
	}
	

	public ArrayList<HashMap<String, Object>> getMontosCobro() {
		return montosCobro;
	}


	public void setMontosCobro(ArrayList<HashMap<String, Object>> montosCobro) {
		this.montosCobro = montosCobro;
	}

	

	public HashMap getParametrosClone() {
		return parametrosClone;
	}


	public void setParametrosClone(HashMap parametrosClone) {
		this.parametrosClone = parametrosClone;
	}

	


	public ArrayList<HashMap<String, Object>> getCentrosAtencion() {
		return centrosAtencion;
	}


	public void setCentrosAtencion(
			ArrayList<HashMap<String, Object>> centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}
	
	public ArrayList<HashMap<String, Object>> getProfecionales() {
		return profecionales;
	}


	public void setProfecionales(ArrayList<HashMap<String, Object>> profecionales) {
		this.profecionales = profecionales;
	}
	
	public ArrayList<HashMap<String, Object>> getFarmacia() {
		return farmacia;
	}


	public void setFarmacia(ArrayList<HashMap<String, Object>> farmacia) {
		this.farmacia = farmacia;
	}
	
	

	public String getEstadoAnt() {
		return estadoAnt;
	}


	public void setEstadoAnt(String estadoAnt) {
		this.estadoAnt = estadoAnt;
	}

	/**
	 * @return the numElementosTP
	 */
	public int getNumElementosTP() {
		return numElementosTP;
	}

	/**
	 * @param numElementosTP the numElementosTP to set
	 */
	public void setNumElementosTP(int numElementosTP) {
		this.numElementosTP = tiposPaciente.size();
	}
	
	
	
	/*----------------------------------------------------
	 * 	FIN	METODOS SETTERS AND GETTERS
	 -----------------------------------------------------*/	
	
	/*--------------------------------------------------------------
	 * 						OTROS METODOS
	 --------------------------------------------------------------*/
	
	public HashMap getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(HashMap especialidades) {
		this.especialidades = especialidades;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errores = new ActionErrors ();
		errores = super.validate(mapping, request);
		
		
		
		
		
		
		
		return errores;
	}

	
	
	public  void reset ()
	{
		this.parametros = new HashMap ();
		this.setParametros("numRegistros", 0);
		this.parametrosClone = new HashMap ();
		this.especialidades=new HashMap();
		this.especialidades.put("numRegistros", "0");

	}
	/*--------------------------------------------------------------
	 * 					FIN	OTROS METODOS
	 --------------------------------------------------------------*/
	
}