package com.princetonsa.actionform.historiaClinica;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;

import com.princetonsa.dto.historiaClinica.parametrizacion.DtoCampoParametrizable;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoComponente;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoEscala;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoOpcionCampoParam;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionParametrizable;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoValorOpcionCampoParam;

public class ParametrizacionPlantillasForm extends ValidatorForm 
{

	private int indiceEspecialidadEliminar;
	
	private String estado;
	
	private String plantillaBase;
	
	private String centroCosto;
	
	private String sexo;
	
	private HashMap especialidades;
	
	private HashMap especialidadesEliminadas;
	
	private HashMap mapaSecciones;
	
	private HashMap mapaCampos;
	
	private HashMap mapaEscalas;
	
	private HashMap mapaComponentes;
	
	private HashMap mapaOpciones;
	
	private HashMap mapaSeccionesFijas;
	
	private HashMap mapaConsultaEspecialidades;
	
	private HashMap<String, Object> mapaSeccionesExistentes;
	
	private HashMap<String, Object> mapaCamposExistentes;
	
	private HashMap<String, Object> mapaEscalasComponentes;
	
	private ResultadoBoolean mostrarMensaje;
	
	private DtoPlantilla plantillaDto;
	
	private ArrayList<DtoEscala> escalasList;
	
	private ArrayList<DtoComponente> componentesList;
	
	private ArrayList<DtoOpcionCampoParam> opcionesList;
	
	private ArrayList<DtoSeccionParametrizable> listSeccionTemporal;
	
	private ArrayList<DtoCampoParametrizable> listCampoTemporal;
	
	private String indexPlantilla;
	
	private String indexEspecialidad;
	
	private String indexSeccionFija;
	
	private String indexNivel;
	
	private String indexElemento;
	
	private String indexSeccionNivel2;
	
	private String indexCampo;
	
	private String indexOpcionCampo;
	
	private String indiceEspecialidad;
	
	private String especialidadParametrizable;
	
	private int indexSeccionEliminar;
	
	private int indexEscalaEliminar;
	
	private int indexComponenteEliminar;
	
	///////
	private ArrayList<DtoCampoParametrizable> listCamposFormula;
	
	private HashMap formulaMap;
	
	private String formulaComprobar;
	
	
	////Anexo728.
	private String indexOpciones;
	
	private ArrayList<DtoSeccionParametrizable> listElementoTemporal;
	
	private String indexSeccionesAsocidas;
	
	private String indexValoresAsocidos;
	
	private ArrayList<DtoValorOpcionCampoParam> listValoresTemporal;
	
	private String valoresAsociar;
	
	//***************************************************
	// Anexo 841
	private int codigoTipotmp; 
	private String manejaIMgtmp;
	private String imagenAsociartmp;
	// Fin Anexo 841
	//***************************************************
	
	
	////Anexo 843
	private String procesoASeguirOdontologia;
	
	private String nombre;
	
	private String tipoAtencion;
	
	private String especialidadOdon;
	
	private ArrayList<HashMap> especialidadesOdontologia;
	
	private HashMap plantillasOdontologia;
	
	private HashMap listadoPlantillasOdon;
	
	private String nombreSelect;
	
	private int codigoPkPlantilla;
	
	private int posPlantilla;
	
	private String mostrarElementosVista;
	
	/////Anexo 860
	private String tipoFuncionalidad;
	
	private Boolean checkLinkOrdenesAmbulatorias;
	
	/**
	 * 
	 *
	 */
	public void reset()
	{
		this.indiceEspecialidadEliminar=ConstantesBD.codigoNuncaValido;
		this.estado="";
		this.plantillaBase="";
		this.centroCosto="";
		this.sexo="";
		this.especialidades=new HashMap();
		this.especialidades.put("numRegistros", "0");
		this.especialidadesEliminadas=new HashMap();
		this.especialidadesEliminadas.put("numRegistros", "0");
		this.mapaSecciones=new HashMap();
		this.mapaSecciones.put("numRegistros", "0");
		this.mapaCampos=new HashMap();
		this.mapaCampos.put("numRegistros", "0");
		this.mapaEscalas=new HashMap();
		this.mapaEscalas.put("numRegistros", "0");
		this.mapaComponentes=new HashMap();
		this.mapaComponentes.put("numRegistros", "0");
		this.mapaOpciones=new HashMap();
		this.mapaOpciones.put("numRegistros", "0");
		this.mapaSeccionesFijas=new HashMap();
		this.mapaSeccionesFijas.put("numRegistros", "0");
		this.mapaConsultaEspecialidades=new HashMap();
		this.mapaConsultaEspecialidades.put("numRegistros", "0");
		this.mostrarMensaje= new ResultadoBoolean(false,"");
		this.escalasList= new ArrayList<DtoEscala>();
		this.plantillaDto=new DtoPlantilla();
		this.componentesList=new ArrayList<DtoComponente>();
		this.opcionesList=new ArrayList<DtoOpcionCampoParam>();
		this.listSeccionTemporal=new ArrayList<DtoSeccionParametrizable>();
		this.listCampoTemporal=new ArrayList<DtoCampoParametrizable>();
		this.indexSeccionFija="";
		this.indexNivel="";
		this.indexPlantilla="";
		this.indexEspecialidad="";
		this.indexElemento="";
		this.indexSeccionNivel2="";
		this.indexCampo="";
		this.indexOpcionCampo="";
		this.indiceEspecialidad="";
		
		this.indexSeccionEliminar=ConstantesBD.codigoNuncaValido;
		this.indexEscalaEliminar=ConstantesBD.codigoNuncaValido;
		this.indexComponenteEliminar=ConstantesBD.codigoNuncaValido;
		
		this.especialidadParametrizable="";
		
		this.mapaSeccionesExistentes= new HashMap<String, Object>();
		this.mapaSeccionesExistentes.put("numRegistros", "0");
		this.mapaCamposExistentes= new HashMap<String, Object>();
		this.mapaCamposExistentes.put("numRegistros", "0");
		this.mapaEscalasComponentes= new HashMap<String, Object>();
		this.mapaEscalasComponentes.put("numRegistros", "0");
		
		this.listCamposFormula= new ArrayList<DtoCampoParametrizable>();
		this.formulaComprobar="";
		this.formulaMap= new HashMap();
		this.formulaMap.put("numRegistros", "0");
		
		///Anexo728.
		this.indexOpciones="";
		this.listElementoTemporal = new ArrayList<DtoSeccionParametrizable>();
		this.indexSeccionesAsocidas="";
		this.indexValoresAsocidos="";
		this.listValoresTemporal = new ArrayList<DtoValorOpcionCampoParam>();
		this.valoresAsociar="";
		
		// Anexo 841
		this.codigoTipotmp = ConstantesBD.codigoNuncaValido;
		this.manejaIMgtmp = ConstantesBD.acronimoNo;
		this.imagenAsociartmp = "";
		//Anexo 843
		this.procesoASeguirOdontologia="";
		this.especialidadesOdontologia= new ArrayList<HashMap>();
		this.nombre="";
		this.nombreSelect="";
		this.tipoAtencion="";
		this.plantillasOdontologia=new HashMap();
		this.especialidadOdon="";
		this.listadoPlantillasOdon=new HashMap();
		this.codigoPkPlantilla=ConstantesBD.codigoNuncaValido;
		this.posPlantilla=ConstantesBD.codigoNuncaValido;
		this.mostrarElementosVista=ConstantesBD.acronimoNo;
		
		//Anexo 860
		this.tipoFuncionalidad="";
		this.checkLinkOrdenesAmbulatorias=false;
		
	}
	
	
	//Cambio pro Anexo 843 para inciializar todas éstos elementos cuando se cambia de tipod e plantilla
	//Debido a que son elementos que peuden usar otroas plantillas y no tengan en cuanta para realziar la parametrización
	public void resetElementosOdontologia()
	{
		//this.especialidadesOdontologia= new ArrayList<HashMap>();
		this.nombre="";
		this.tipoAtencion="";
		this.especialidadParametrizable="";
		this.plantillasOdontologia=new HashMap();
		this.especialidadOdon="";
		this.codigoPkPlantilla=ConstantesBD.codigoNuncaValido;
		this.posPlantilla=ConstantesBD.codigoNuncaValido;
		this.mostrarElementosVista=ConstantesBD.acronimoNo;
	}
	
	/**
	 * 
	 *
	 */
	public void resetElementos()
	{
		this.escalasList= new ArrayList<DtoEscala>();
		this.componentesList=new ArrayList<DtoComponente>();
		this.listSeccionTemporal=new ArrayList<DtoSeccionParametrizable>();
		this.listCampoTemporal=new ArrayList<DtoCampoParametrizable>();
		this.indexSeccionFija="";
		this.indexNivel="";
		this.indexPlantilla="";
		this.indexEspecialidad="";
		this.indexElemento="";
		this.indexSeccionNivel2="";
		this.indexCampo="";
		this.indexOpcionCampo="";
		this.indiceEspecialidad="";
		
		this.indexSeccionEliminar=ConstantesBD.codigoNuncaValido;
		this.indexEscalaEliminar=ConstantesBD.codigoNuncaValido;
		this.indexComponenteEliminar=ConstantesBD.codigoNuncaValido;
		
	}
	
	public void resetOdon()
	{
		this.procesoASeguirOdontologia="";
		this.tipoAtencion="";
		//this.especialidadParametrizable="";
		this.nombre="";
		this.nombreSelect="";
		this.mostrarElementosVista=ConstantesBD.acronimoNo;
	}
	
	
	public void resetPlantillaDto()
	{
		this.plantillaDto=  new DtoPlantilla();
	}
	/**
	 * 
	 *
	 */
	public void resetMensaje()
	{
		this.mostrarMensaje=new ResultadoBoolean(false,"");
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
	public String getPlantillaBase() {
		return plantillaBase;
	}

	/**
	 * 
	 * @param plantillaBase
	 */
	public void setPlantillaBase(String plantillaBase) {
		this.plantillaBase = plantillaBase;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getEspecialidades() {
		return especialidades;
	}

	/**
	 * 
	 * @param especialidades
	 */
	public void setEspecialidades(HashMap especialidades) {
		this.especialidades = especialidades;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getEspecialidades(String key) {
		return especialidades.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setEspecialidades(String key,Object value) {
		this.especialidades.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public int getIndiceEspecialidadEliminar() {
		return indiceEspecialidadEliminar;
	}

	/**
	 * 
	 * @param indiceEspecialidadEliminar
	 */
	public void setIndiceEspecialidadEliminar(int indiceEspecialidadEliminar) {
		this.indiceEspecialidadEliminar = indiceEspecialidadEliminar;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getEspecialidadesEliminadas() {
		return especialidadesEliminadas;
	}

	/**
	 * 
	 * @param especialidadesEliminadas
	 */
	public void setEspecialidadesEliminadas(HashMap especialidadesEliminadas) {
		this.especialidadesEliminadas = especialidadesEliminadas;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getEspecialidadesEliminadas(String key){
		return especialidadesEliminadas.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setEspecialidadesEliminadas(String key,Object value) {
		this.especialidadesEliminadas.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public String getCentroCosto() {
		return centroCosto;
	}

	/**
	 * 
	 * @param centroCosto
	 */
	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * 
	 * @return
	 */
	public String getSexo() {
		return sexo;
	}

	/**
	 * 
	 * @param sexo
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaCampos() {
		return mapaCampos;
	}

	/**
	 * 
	 * @param mapaCampos
	 */
	public void setMapaCampos(HashMap mapaCampos) {
		this.mapaCampos = mapaCampos;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaCampos(String key){
		return mapaCampos.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaCampos(String key,Object value) {
		this.mapaCampos.put(key, value);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public HashMap getMapaEscalas() {
		return mapaEscalas;
	}

	/**
	 * 
	 * @param mapaEscalas
	 */
	public void setMapaEscalas(HashMap mapaEscalas) {
		this.mapaEscalas = mapaEscalas;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaEscalas(String key) {
		return mapaEscalas.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaEscalas(String key,Object value) {
		this.mapaEscalas.put(key, value);
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap getMapaSecciones() {
		return mapaSecciones;
	}

	/**
	 * 
	 * @param mapaSecciones
	 */
	public void setMapaSecciones(HashMap mapaSecciones) {
		this.mapaSecciones = mapaSecciones;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaSecciones(String key) {
		return mapaSecciones.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaSecciones(String key,Object value) {
		this.mapaSecciones.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaComponentes() {
		return mapaComponentes;
	}

	/**
	 * 
	 * @param mapaComponentes
	 */
	public void setMapaComponentes(HashMap mapaComponentes) {
		this.mapaComponentes = mapaComponentes;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaComponentes(String key) {
		return mapaComponentes.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaComponentes(String key,Object value) {
		this.mapaComponentes.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaOpciones() {
		return mapaOpciones;
	}

	/**
	 * 
	 * @param mapaOpciones
	 */
	public void setMapaOpciones(HashMap mapaOpciones) {
		this.mapaOpciones = mapaOpciones;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaOpciones(String key) {
		return mapaOpciones.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaOpciones(String key,Object value) {
		this.mapaOpciones.put(key, value);
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
	public HashMap getMapaSeccionesFijas() {
		return mapaSeccionesFijas;
	}
	
	/**
	 * 
	 * @param mapaSeccionesFijas
	 */
	public void setMapaSeccionesFijas(HashMap mapaSeccionesFijas) {
		this.mapaSeccionesFijas = mapaSeccionesFijas;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap getMapaConsultaEspecialidades() {
		return mapaConsultaEspecialidades;
	}

	/**
	 * 
	 * @param mapaConsultaEspecialidades
	 */
	public void setMapaConsultaEspecialidades(HashMap mapaConsultaEspecialidades) {
		this.mapaConsultaEspecialidades = mapaConsultaEspecialidades;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaConsultaEspecialidades(String key) {
		return mapaConsultaEspecialidades.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaConsultaEspecialidades(String key,Object value) {
		this.mapaConsultaEspecialidades.put(key, value);
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoEscala> getEscalasList() {
		return escalasList;
	}

	
	public DtoEscala getEscalasListPos(int index) {
		return escalasList.get(index);
	}
	
	/**
	 * 
	 * @param escalasList
	 */
	public void setEscalasList(ArrayList<DtoEscala> escalasList) {
		this.escalasList = escalasList;
	}

	/**
	 * 
	 * @return
	 */
	public DtoPlantilla getPlantillaDto() {
		return plantillaDto;
	}

	/**
	 * 
	 * @param plantillaDto
	 */
	public void setPlantillaDto(DtoPlantilla plantillaDto) {
		this.plantillaDto = plantillaDto;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoComponente> getComponentesList() {
		return componentesList;
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public DtoComponente getComponentesListPos(int index) {
		return componentesList.get(index);
	}
	
	/**
	 * 
	 * @param componentesList
	 */
	public void setComponentesList(ArrayList<DtoComponente> componentesList) {
		this.componentesList = componentesList;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoOpcionCampoParam> getOpcionesList() {
		return opcionesList;
	}

	/**
	 * 
	 * @param opcionesList
	 */
	public void setOpcionesList(ArrayList<DtoOpcionCampoParam> opcionesList) {
		this.opcionesList = opcionesList;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getIndexNivel() {
		return indexNivel;
	}

	/**
	 * 
	 * @param indexNivel
	 */
	public void setIndexNivel(String indexNivel) {
		this.indexNivel = indexNivel;
	}

	/**
	 * 
	 * @return
	 */
	public String getIndexSeccionFija() {
		return indexSeccionFija;
	}

	/**
	 * 
	 * @param indexSeccionFija
	 */
	public void setIndexSeccionFija(String indexSeccionFija) {
		this.indexSeccionFija = indexSeccionFija;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getIndexEspecialidad() {
		return indexEspecialidad;
	}

	/**
	 * 
	 * @param indexEspecialidad
	 */
	public void setIndexEspecialidad(String indexEspecialidad) {
		this.indexEspecialidad = indexEspecialidad;
	}

	/**
	 * 
	 * @return
	 */
	public String getIndexPlantilla() {
		return indexPlantilla;
	}

	/**
	 * 
	 * @param indexPlantilla
	 */
	public void setIndexPlantilla(String indexPlantilla) {
		this.indexPlantilla = indexPlantilla;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoSeccionParametrizable> getListSeccionTemporal() {
		return listSeccionTemporal;
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public DtoCampoParametrizable getListCampoTemporalPos(int index) {
		return listCampoTemporal.get(index);
	}

	/**
	 * 
	 * @param listSeccionTemporal
	 */
	public void setListSeccionTemporal(
			ArrayList<DtoSeccionParametrizable> listSeccionTemporal) {
		this.listSeccionTemporal = listSeccionTemporal;
	}

	/**
	 * 
	 * @return
	 */
	public String getIndexElemento() {
		return indexElemento;
	}

	/**
	 * 
	 * @param indexElemento
	 */
	public void setIndexElemento(String indexElemento) {
		this.indexElemento = indexElemento;
	}

	/**
	 * 
	 * @return
	 */
	public String getIndexSeccionNivel2() {
		return indexSeccionNivel2;
	}

	/**
	 * 
	 * @param indexSeccionNivel2
	 */
	public void setIndexSeccionNivel2(String indexSeccionNivel2) {
		this.indexSeccionNivel2 = indexSeccionNivel2;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoCampoParametrizable> getListCampoTemporal() {
		return listCampoTemporal;
	}

	/**
	 * 
	 * @param listCampoTemporal
	 */
	public void setListCampoTemporal(
			ArrayList<DtoCampoParametrizable> listCampoTemporal) {
		this.listCampoTemporal = listCampoTemporal;
	}

	/**
	 * 
	 * @return
	 */
	public String getIndexCampo() {
		return indexCampo;
	}

	/**
	 * 
	 * @param indexCampo
	 */
	public void setIndexCampo(String indexCampo) {
		this.indexCampo = indexCampo;
	}

	/**
	 * 
	 * @return
	 */
	public String getIndexOpcionCampo() {
		return indexOpcionCampo;
	}

	/**
	 * 
	 * @param indexOpcionCampo
	 */
	public void setIndexOpcionCampo(String indexOpcionCampo) {
		this.indexOpcionCampo = indexOpcionCampo;
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public DtoSeccionParametrizable getListSeccionTemporalPos(int index) {
		return listSeccionTemporal.get(index);
	}

	/**
	 * 
	 * @return
	 */
	public String getIndiceEspecialidad() {
		return indiceEspecialidad;
	}

	/**
	 * 
	 * @param indiceEspecialidad
	 */
	public void setIndiceEspecialidad(String indiceEspecialidad) {
		this.indiceEspecialidad = indiceEspecialidad;
	}

	/**
	 * 
	 * @return
	 */
	public String getEspecialidadParametrizable() {
		return especialidadParametrizable;
	}

	/**
	 * 
	 * @param especialidadParametrizable
	 */
	public void setEspecialidadParametrizable(String especialidadParametrizable) {
		this.especialidadParametrizable = especialidadParametrizable;
	}

	/**
	 * 
	 * @return
	 */
	public int getIndexSeccionEliminar() {
		return indexSeccionEliminar;
	}

	/**
	 * 
	 * @param indexSeccionEliminar
	 */
	public void setIndexSeccionEliminar(int indexSeccionEliminar) {
		this.indexSeccionEliminar = indexSeccionEliminar;
	}

	/**
	 * 
	 * @return
	 */
	public int getIndexComponenteEliminar() {
		return indexComponenteEliminar;
	}

	/**
	 * 
	 * @param indexComponenteEliminar
	 */
	public void setIndexComponenteEliminar(int indexComponenteEliminar) {
		this.indexComponenteEliminar = indexComponenteEliminar;
	}

	/**
	 * 
	 * @return
	 */
	public int getIndexEscalaEliminar() {
		return indexEscalaEliminar;
	}

	/**
	 * 
	 * @param indexEscalaEliminar
	 */
	public void setIndexEscalaEliminar(int indexEscalaEliminar) {
		this.indexEscalaEliminar = indexEscalaEliminar;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaCamposExistentes() {
		return mapaCamposExistentes;
	}

	/**
	 * 
	 * @param mapaCamposExitentes
	 */
	public void setMapaCamposExistentes(HashMap<String, Object> mapaCamposExistentes) {
		this.mapaCamposExistentes = mapaCamposExistentes;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaCamposExistentes(String key) {
		return mapaCamposExistentes.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaCamposExistentes(String key,Object value) {
		this.mapaCamposExistentes.put(key, value);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaSeccionesExistentes() {
		return mapaSeccionesExistentes;
	}

	/**
	 * 
	 * @param mapaSeccionesExistentes
	 */
	public void setMapaSeccionesExistentes(
			HashMap<String, Object> mapaSeccionesExistentes) {
		this.mapaSeccionesExistentes = mapaSeccionesExistentes;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaSeccionesExistentes(String key) {
		return mapaSeccionesExistentes.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaSeccionesExistentes(String key,Object value) {
		this.mapaSeccionesExistentes.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaEscalasComponentes() {
		return mapaEscalasComponentes;
	}

	/**
	 * 
	 * @param mapaEscalasComponentes
	 */
	public void setMapaEscalasComponentes(
			HashMap<String, Object> mapaEscalasComponentes) {
		this.mapaEscalasComponentes = mapaEscalasComponentes;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaEscalasComponentes(String key) {
		return mapaEscalasComponentes.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaEscalasComponentes(String key,Object value) {
		this.mapaEscalasComponentes.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getFormulaMap() {
		return formulaMap;
	}

	/**
	 * 
	 * @param formulaMap
	 */
	public void setFormulaMap(HashMap formulaMap) {
		this.formulaMap = formulaMap;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getFormulaMap(String key) {
		return formulaMap.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */	
	public void setFormulaMap(String key,Object value) {
		this.formulaMap.put(key, value);
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoCampoParametrizable> getListCamposFormula() {
		return listCamposFormula;
	}

	/**
	 * 
	 * @param listCamposFormula
	 */
	public void setListCamposFormula(
			ArrayList<DtoCampoParametrizable> listCamposFormula) {
		this.listCamposFormula = listCamposFormula;
	}

	/**
	 * 
	 * @return
	 */
	public String getFormulaComprobar() {
		return formulaComprobar;
	}

	/**
	 * 
	 * @param formulaComprobar
	 */
	public void setFormulaComprobar(String formulaComprobar) {
		this.formulaComprobar = formulaComprobar;
	}
	
	/**
	 * 
	 * @param codigoPk
	 * @return
	 */
	public DtoCampoParametrizable getListCampoTemporalCodigoPk(String codigoPk)
	{
		for(int i=0; i<this.listCampoTemporal.size(); i++)
		{			
			if(this.listCampoTemporal.get(i).getCodigoPK().equals(codigoPk))
				return this.listCampoTemporal.get(i); 
		}
		
		return new DtoCampoParametrizable();
	}

	/**
	 * 
	 * @return
	 */
	public String getIndexOpciones() {
		return indexOpciones;
	}

	/**
	 * 
	 * @param indexOpciones
	 */
	public void setIndexOpciones(String indexOpciones) {
		this.indexOpciones = indexOpciones;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoSeccionParametrizable> getListElementoTemporal() {
		return listElementoTemporal;
	}

	/**
	 * 
	 * @param listElementoTemporal
	 */
	public void setListElementoTemporal(
			ArrayList<DtoSeccionParametrizable> listElementoTemporal) {
		this.listElementoTemporal = listElementoTemporal;
	}

	/**
	 * 
	 * @return
	 */
	public String getIndexSeccionesAsocidas() {
		return indexSeccionesAsocidas;
	}

	/**
	 * 
	 * @param indexSeccionesAsocidas
	 */
	public void setIndexSeccionesAsocidas(String indexSeccionesAsocidas) {
		this.indexSeccionesAsocidas = indexSeccionesAsocidas;
	}

	/**
	 * 
	 * @return
	 */
	public String getIndexValoresAsocidos() {
		return indexValoresAsocidos;
	}

	/**
	 * 
	 * @param indexValoresAsocidos
	 */
	public void setIndexValoresAsocidos(String indexValoresAsocidos) {
		this.indexValoresAsocidos = indexValoresAsocidos;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoValorOpcionCampoParam> getListValoresTemporal() {
		return listValoresTemporal;
	}

	/**
	 * 
	 * @param listValoresTemporal
	 */
	public void setListValoresTemporal(
			ArrayList<DtoValorOpcionCampoParam> listValoresTemporal) {
		this.listValoresTemporal = listValoresTemporal;
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public DtoValorOpcionCampoParam getListValoresTemporal(int index) {
		return listValoresTemporal.get(index);
	}

	/**
	 * 
	 * @return
	 */
	public String getValoresAsociar() {
		return valoresAsociar;
	}

	/**
	 * 
	 * @param valoresAsociar
	 */
	public void setValoresAsociar(String valoresAsociar) {
		this.valoresAsociar = valoresAsociar;
	}


	public String getProcesoASeguirOdontologia() {
		return procesoASeguirOdontologia;
	}

	public void setProcesoASeguirOdontologia(String procesoASeguirOdontologia) {
		this.procesoASeguirOdontologia = procesoASeguirOdontologia;
	}


	public ArrayList<HashMap> getEspecialidadesOdontologia() {
		return especialidadesOdontologia;
	}


	public void setEspecialidadesOdontologia(
			ArrayList<HashMap> especialidadesOdontologia) {
		this.especialidadesOdontologia = especialidadesOdontologia;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getTipoAtencion() {
		return tipoAtencion;
	}


	public void setTipoAtencion(String tipoAtencion) {
		this.tipoAtencion = tipoAtencion;
	}


	public HashMap getPlantillasOdontologia() {
		return plantillasOdontologia;
	}


	public void setPlantillasOdontologia(HashMap plantillasOdontologia) {
		this.plantillasOdontologia = plantillasOdontologia;
	}


	public String getNombreSelect() {
		return nombreSelect;
	}


	public void setNombreSelect(String nombreSelect) {
		this.nombreSelect = nombreSelect;
	}


	public String getEspecialidadOdon() {
		return especialidadOdon;
	}


	public void setEspecialidadOdon(String especialidadOdon) {
		this.especialidadOdon = especialidadOdon;
	}


	public HashMap getListadoPlantillasOdon() {
		return listadoPlantillasOdon;
	}


	public void setListadoPlantillasOdon(HashMap listadoPlantillasOdon) {
		this.listadoPlantillasOdon = listadoPlantillasOdon;
	}


	public int getCodigoPkPlantilla() {
		return codigoPkPlantilla;
	}


	public void setCodigoPkPlantilla(int codigoPkPlantilla) {
		this.codigoPkPlantilla = codigoPkPlantilla;
	}


	public int getPosPlantilla() {
		return posPlantilla;
	}

	public void setPosPlantilla(int posPlantilla) {
		this.posPlantilla = posPlantilla;
	}

	public String getTipoFuncionalidad() {
		return tipoFuncionalidad;
	}

	public void setTipoFuncionalidad(String tipoFuncionalidad) {
		this.tipoFuncionalidad = tipoFuncionalidad;
	}


	/**
	 * @return the manejaIMgtmp
	 */
	public String getManejaIMgtmp() {
		return manejaIMgtmp;
	}


	/**
	 * @param manejaIMgtmp the manejaIMgtmp to set
	 */
	public void setManejaIMgtmp(String manejaIMgtmp) {
		this.manejaIMgtmp = manejaIMgtmp;
	}


	/**
	 * @return the codigoTipotmp
	 */
	public int getCodigoTipotmp() {
		return codigoTipotmp;
	}


	/**
	 * @param codigoTipotmp the codigoTipotmp to set
	 */
	public void setCodigoTipotmp(int codigoTipotmp) {
		this.codigoTipotmp = codigoTipotmp;
	}


	/**
	 * @return the imagenAsociartmp
	 */
	public String getImagenAsociartmp() {
		return imagenAsociartmp;
	}


	/**
	 * @param imagenAsociartmp the imagenAsociartmp to set
	 */
	public void setImagenAsociartmp(String imagenAsociartmp) {
		this.imagenAsociartmp = imagenAsociartmp;
	}


	public String getMostrarElementosVista() {
		return mostrarElementosVista;
	}


	public void setMostrarElementosVista(String mostrarElementosVista) {
		this.mostrarElementosVista = mostrarElementosVista;
	}
	
	
	/**
	 * @return the checkLinkOrdenesAmbulatorias
	 */
	public Boolean getCheckLinkOrdenesAmbulatorias() {
		return checkLinkOrdenesAmbulatorias;
	}


	/**
	 * @param checkLinkOrdenesAmbulatorias the checkLinkOrdenesAmbulatorias to set
	 */
	public void setCheckLinkOrdenesAmbulatorias(Boolean checkLinkOrdenesAmbulatorias) {
		this.checkLinkOrdenesAmbulatorias = checkLinkOrdenesAmbulatorias;
	}
	
	
	
	
}
