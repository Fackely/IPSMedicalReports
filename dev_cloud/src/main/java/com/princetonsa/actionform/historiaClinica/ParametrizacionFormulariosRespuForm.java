/*
 * Creado en Mayo del 2008
 */
package com.princetonsa.actionform.historiaClinica;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.historiaClinica.parametrizacion.DtoCampoParametrizable;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoComponente;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoElementoParam;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoEscala;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantillaServDiag;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoSeccionParametrizable;

import util.ConstantesBD;


/**
 * @author Jose Eduardo Arias Doncel
 */
public class ParametrizacionFormulariosRespuForm extends ValidatorForm
{
	
	//Atributos para el Pager*******************************************
	
	/**
	 * 
	 * */
	private String linkSiguiente;
	
	/**
	 * 
	 * */
	private String patronOrdenar;
	
	/**
	 * Ultimp Patron Ordenar
	 * */
	private String ultimoPatronOrdenar;
	
	/**
	 * Indicador para los estado del action 
	 * */
	private String estado;
	
	/**
	 * Indicador para la consula de informacion en la funcionalidad
	 * */
	private String sePuedeConsultar;
	
	/**
	 * Indicador para el ingreso/modificación de la funcionalidad 
	 * */
	private String sePuedeModificar;
	
	/**
	 * 
	 * */
	private ArrayList<HashMap<String,Object>> arrayUtilitario1;
	private ArrayList<HashMap<String,Object>> arrayUtilitario2;
	
	//Atributos Indicadores **********************************************
	
	/**
	 * indicador del mapa de formularios parametrizados
	 * */
	private String indexListadoPlantilla;
	
	/**
	 * indicador de la posición de la sección Fija
	 * */
	private String indexSeccionFija;
	
	/**
	 * indicador de la posición del elemento de la seccion fija
	 * */
	private String indexElemento;
	
	
	/**
	 * Indicador de la posición del elemento de segundo 
	 * */
	private String indexSeccionNivel2;
	
	/**
	 * indicador de nivel 
	 * */
	private String indexNivel;
	
	/**
	 * indicador de Campo
	 * */
	private String indexCampo;
	
	/**
	 * Indicador de la opcion del campo
	 * */
	private String indexOpcionCampo;
	
	/**
	 * Indicador de Escala
	 * */
	private String indexEscala;
	
	/**
	 * Indicador de Componente
	 * */
	private String indexComponente;
	
	/**
	 * Indicador de Secciones Asociadas 
	 * */
	private String indexSeccionesAsocidas;
	
	/**
	 * Indicadot de Valores Asociados
	 * */
	private String indexValoresAsocidos;
	
	
	//********************************************************************
	
	/**
	 * listado de formularios parametrizados
	 * */
	private HashMap listadoPlantillaMap;
	
	/**
	 * Almacena la información de la plantilla
	 * */
	private HashMap plantillaMap;
	
	/**
	 * Almacena la información de la plantilla
	 * */
	private DtoPlantilla plantillaPreviewDto;
	
	/**
	 * Almaneca la informacion de la parametrizacion de servicios / Diagnosticos por plantilla
	 * */
	private ArrayList<DtoPlantillaServDiag> plantillaSerDiagArray;
	
	/**
	 * 
	 * */
	private ArrayList<HashMap<String,Object>> diagnosticosParamArray;	
	
	/**
	 * DtoPlantilla almacena la informacion de la plantilla parametrizada
	 * */
	private DtoPlantilla plantillaDto;
	
	/**
	 * DtoSeccionParametrizable almacena la información de una seccion parametrizable temporal  
	 * */
	private ArrayList<DtoSeccionParametrizable> listSeccionTemporal;
	
	/**
	 * DtoCampos almacena la informacion de Campos Parametrizables
	 * */
	private ArrayList<DtoCampoParametrizable> listCampoTemporal;
	
	/**
	 * ArrayList de Escalas
	 * */
	private ArrayList<DtoEscala> listEscalaTemporal;
	
	/**
	 * ArrayList de Temporal
	 * */
	private ArrayList<DtoComponente> listComponenteTemporal;
	
	/**
	 * ArrayList de Temporal
	 * */
	private ArrayList<DtoElementoParam> listElementoTemporal;	
	
	/**
	 * HashMap para el manejo de formulas
	 * */
	private HashMap formulaMap;
	
	/**
	 * String formulaComprobar
	 * */
	private String formulaComprobar;	

	/**
	 * DtoCampos almacena la informacion de Campos Parametrizables
	 * */
	private ArrayList<DtoCampoParametrizable> listCamposFormula;	
	
	//****************************************************************
	//****************************************************************
	//****************************************************************
	
	//***************************************************
	// Anexo 841
	private int codigoTipotmp; 
	private String manejaIMgtmp;
	private String imagenAsociartmp;
	// Fin Anexo 841
	//***************************************************
	
	
	
	private Boolean checkLinkOrdenesAmbulatorias;
	
	/**
	 * Inicializa los atributos de la forma
	 * */
	public void reset()
	{
		listadoPlantillaMap = new HashMap();
		indexListadoPlantilla = "";
		indexNivel = "";
		sePuedeConsultar = ConstantesBD.acronimoNo;
		sePuedeModificar = ConstantesBD.acronimoNo;
		plantillaMap = new HashMap();
		plantillaMap.put("numServicios","0");
		listSeccionTemporal = new ArrayList<DtoSeccionParametrizable>();		
		this.indexSeccionFija = "";
		this.indexElemento = "";
		this.indexSeccionNivel2 = "";
		this.listCampoTemporal = new ArrayList<DtoCampoParametrizable>();
		this.arrayUtilitario1 = new ArrayList<HashMap<String,Object>>();
		this.arrayUtilitario2 = new ArrayList<HashMap<String,Object>>();
		this.indexCampo = "";
		this.indexOpcionCampo = "";
		this.indexComponente = "";
		this.indexEscala = "";
		this.formulaMap = new HashMap();		
		this.formulaComprobar = "";
		this.plantillaSerDiagArray  = new ArrayList<DtoPlantillaServDiag>();
		this.plantillaPreviewDto = new DtoPlantilla();
		this.listElementoTemporal = new ArrayList<DtoElementoParam>();
		
		// Anexo 841
		this.codigoTipotmp = ConstantesBD.codigoNuncaValido;
		this.manejaIMgtmp = ConstantesBD.acronimoNo;
		this.imagenAsociartmp = "";
		this.checkLinkOrdenesAmbulatorias=false;
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
	 * @return the listadoPlantillaMap
	 */
	public HashMap getListadoPlantillaMap() {
		return listadoPlantillaMap;
	}

	/**
	 * @param listadoPlantillaMap the listadoPlantillaMap to set
	 */
	public void setListadoPlantillaMap(HashMap listadoPlantillaMap) {
		this.listadoPlantillaMap = listadoPlantillaMap;
	}	
	
	/**
	 * @return the listadoPlantillaMap
	 */
	public Object getListadoPlantillaMap(String key) {
		return listadoPlantillaMap.get(key);
	}

	/**
	 * @param listadoPlantillaMap the listadoPlantillaMap to set
	 */
	public void setListadoPlantillaMap(String key, Object value) {
		this.listadoPlantillaMap.put(key, value);
	}	

	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the sePuedeConsultar
	 */
	public String getSePuedeConsultar() {
		return sePuedeConsultar;
	}

	/**
	 * @param sePuedeConsultar the sePuedeConsultar to set
	 */
	public void setSePuedeConsultar(String sePuedeConsultar) {
		this.sePuedeConsultar = sePuedeConsultar;
	}

	/**
	 * @return the sePuedeModificar
	 */
	public String getSePuedeModificar() {
		return sePuedeModificar;
	}

	/**
	 * @param sePuedeModificar the sePuedeModificar to set
	 */
	public void setSePuedeModificar(String sePuedeModificar) {
		this.sePuedeModificar = sePuedeModificar;
	}

	/**
	 * @return the plantillaMap
	 */
	public HashMap getPlantillaMap() {
		return plantillaMap;
	}

	/**
	 * @param plantillaMap the plantillaMap to set
	 */
	public void setPlantillaMap(HashMap plantillaMap) {
		this.plantillaMap = plantillaMap;
	}
	
	/**
	 * @return the plantillaMap
	 */
	public Object getPlantillaMap(String key) {
		return plantillaMap.get(key);
	}

	/**
	 * @param plantillaMap the plantillaMap to set
	 */
	public void setPlantillaMap(String key,Object value) {
		this.plantillaMap.put(key, value);
	}

	/**
	 * @return the indexListadoPlantilla
	 */
	public String getIndexListadoPlantilla() {
		return indexListadoPlantilla;
	}

	/**
	 * @param indexListadoPlantilla the indexListadoPlantilla to set
	 */
	public void setIndexListadoPlantilla(String indexListadoPlantilla) {
		this.indexListadoPlantilla = indexListadoPlantilla;
	}

	/**
	 * @return the ultimoPatronOrdenar
	 */
	public String getUltimoPatronOrdenar() {
		return ultimoPatronOrdenar;
	}

	/**
	 * @param ultimoPatronOrdenar the ultimoPatronOrdenar to set
	 */
	public void setUltimoPatronOrdenar(String ultimoPatronOrdenar) {
		this.ultimoPatronOrdenar = ultimoPatronOrdenar;
	}

	/**
	 * @return the plantillaDto
	 */
	public DtoPlantilla getPlantillaDto() {
		return plantillaDto;
	}

	/**
	 * @param plantillaDto the plantillaDto to set
	 */
	public void setPlantillaDto(DtoPlantilla plantillaDto) {
		this.plantillaDto = plantillaDto;
	}

	/**
	 * @return the indexSeccionFija
	 */
	public String getIndexSeccionFija() {
		return indexSeccionFija;
	}

	/**
	 * @param indexSeccionFija the indexSeccionFija to set
	 */
	public void setIndexSeccionFija(String indexSeccionFija) {
		this.indexSeccionFija = indexSeccionFija;
	}

	/**
	 * @return the indexNivel
	 */
	public String getIndexNivel() {
		return indexNivel;
	}

	/**
	 * @param indexNivel the indexNivel to set
	 */
	public void setIndexNivel(String indexNivel) {
		this.indexNivel = indexNivel;
	}
	
	/**
	 * @return the listSeccionTemporal
	 */
	public ArrayList<DtoSeccionParametrizable> getListSeccionTemporal() {
		return listSeccionTemporal;
	}
	
	/**
	 * @return the listSeccionTemporal
	 */
	public DtoSeccionParametrizable getListSeccionTemporalPos(int index) {
		return listSeccionTemporal.get(index);
	}

	/**
	 * @param listSeccionTemporal the listSeccionTemporal to set
	 */
	public void setListSeccionTemporal(
			ArrayList<DtoSeccionParametrizable> listSeccionTemporal) {
		this.listSeccionTemporal = listSeccionTemporal;
	}

	/**
	 * @return the indexElemento
	 */
	public String getIndexElemento() {
		return indexElemento;
	}

	/**
	 * @param indexElemento the indexElemento to set
	 */
	public void setIndexElemento(String indexElemento) {
		this.indexElemento = indexElemento;
	}

	/**
	 * @return the indexSeccionNivel2
	 */
	public String getIndexSeccionNivel2() {
		return indexSeccionNivel2;
	}

	/**
	 * @param indexSeccionNivel2 the indexSeccionNivel2 to set
	 */
	public void setIndexSeccionNivel2(String indexSeccionNivel2) {
		this.indexSeccionNivel2 = indexSeccionNivel2;
	}

	/**
	 * @return the listCampoTemporal
	 */
	public ArrayList<DtoCampoParametrizable> getListCampoTemporal() {
		return listCampoTemporal;
	}
	
	/**
	 * @return the listCampoTemporal
	 */
	public DtoCampoParametrizable getListCampoTemporalPos(int index) {
		return listCampoTemporal.get(index);
	}
	

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
	 * @param listCampoTemporal the listCampoTemporal to set
	 */
	public void setListCampoTemporal(
			ArrayList<DtoCampoParametrizable> listCampoTemporal) {
		this.listCampoTemporal = listCampoTemporal;
	}
	

	/**
	 * @return the arrayUtilitario1
	 */
	public ArrayList<HashMap<String, Object>> getArrayUtilitario1() {
		return arrayUtilitario1;
	}

	/**
	 * @param arrayUtilitario1 the arrayUtilitario1 to set
	 */
	public void setArrayUtilitario1(
			ArrayList<HashMap<String, Object>> arrayUtilitario1) {
		this.arrayUtilitario1 = arrayUtilitario1;
	}

	/**
	 * @return the arrayUtilitario2
	 */
	public ArrayList<HashMap<String, Object>> getArrayUtilitario2() {
		return arrayUtilitario2;
	}

	/**
	 * @param arrayUtilitario2 the arrayUtilitario2 to set
	 */
	public void setArrayUtilitario2(
			ArrayList<HashMap<String, Object>> arrayUtilitario2) {
		this.arrayUtilitario2 = arrayUtilitario2;
	}

	/**
	 * @return the indexCampo
	 */
	public String getIndexCampo() {
		return indexCampo;
	}

	/**
	 * @param indexCampo the indexCampo to set
	 */
	public void setIndexCampo(String indexCampo) {
		this.indexCampo = indexCampo;
	}

	/**
	 * @return the indexOpcionCampo
	 */
	public String getIndexOpcionCampo() {
		return indexOpcionCampo;
	}

	/**
	 * @param indexOpcionCampo the indexOpcionCampo to set
	 */
	public void setIndexOpcionCampo(String indexOpcionCampo) {
		this.indexOpcionCampo = indexOpcionCampo;
	}	
	
	public void setIndexServDiag(String value)
	{
		this.plantillaMap.put("indexServDiag",value);
	}
	
	public void setEsOperacionExitosa(String value)
	{
		this.plantillaMap.put("esOperacionExitosa",value);
	}
	
	public String getEsOperacionExitosa()
	{
		return this.plantillaMap.get("esOperacionExitosa").toString();
	}

	/**
	 * @return the listComponenteTemporal
	 */
	public ArrayList<DtoComponente> getListComponenteTemporal() {
		return listComponenteTemporal;
	}

	/**
	 * @param listComponenteTemporal the listComponenteTemporal to set
	 */
	public void setListComponenteTemporal(
			ArrayList<DtoComponente> listComponenteTemporal) {
		this.listComponenteTemporal = listComponenteTemporal;
	}

	/**
	 * @return the listEscalaTemporal
	 */
	public ArrayList<DtoEscala> getListEscalaTemporal() {
		return listEscalaTemporal;
	}

	/**
	 * @param listEscalaTemporal the listEscalaTemporal to set
	 */
	public void setListEscalaTemporal(ArrayList<DtoEscala> listEscalaTemporal) {
		this.listEscalaTemporal = listEscalaTemporal;
	}

	/**
	 * @return the indexComponente
	 */
	public String getIndexComponente() {
		return indexComponente;
	}

	/**
	 * @param indexComponente the indexComponente to set
	 */
	public void setIndexComponente(String indexComponente) {
		this.indexComponente = indexComponente;
	}

	/**
	 * @return the indexEscala
	 */
	public String getIndexEscala() {
		return indexEscala;
	}

	/**
	 * @param indexEscala the indexEscala to set
	 */
	public void setIndexEscala(String indexEscala) {
		this.indexEscala = indexEscala;
	}

	/**
	 * @return the formulaMap
	 */
	public HashMap getFormulaMap() {
		return formulaMap;
	}

	/**
	 * @param formulaMap the formulaMap to set
	 */
	public void setFormulaMap(HashMap formulaMap) {
		this.formulaMap = formulaMap;
	}
	
	/**
	 * @return the formulaMap
	 */
	public Object getFormulaMap(String key) {
		return formulaMap.get(key);
	}

	/**
	 * @param formulaMap the formulaMap to set
	 */
	public void setFormulaMap(String key, Object value) {
		this.formulaMap.put(key, value);
	}

	/**
	 * @return the formulaComprobar
	 */
	public String getFormulaComprobar() {
		return formulaComprobar;
	}

	/**
	 * @param formulaComprobar the formulaComprobar to set
	 */
	public void setFormulaComprobar(String formulaComprobar) {
		this.formulaComprobar = formulaComprobar;
	}
	

	/**
	 * @return the listCamposFormula
	 */
	public ArrayList<DtoCampoParametrizable> getListCamposFormula() {
		return listCamposFormula;
	}

	/**
	 * @param listCamposFormula the listCamposFormula to set
	 */
	public void setListCamposFormula(
			ArrayList<DtoCampoParametrizable> listCamposFormula) {
		this.listCamposFormula = listCamposFormula;
	}

	/**
	 * @return the plantillaSerDiagArray
	 */
	public ArrayList<DtoPlantillaServDiag> getPlantillaSerDiagArray() {
		return plantillaSerDiagArray;
	}
	
	/**
	 * @return the plantillaSerDiagArray
	 */
	public ArrayList<DtoPlantillaServDiag> getPlantillaSerDiagArrayGroupServicio() 
	{				
		ArrayList<DtoPlantillaServDiag> respuesta = new ArrayList<DtoPlantillaServDiag>();
		boolean drapeau = false;
		
		for(DtoPlantillaServDiag dto:plantillaSerDiagArray)
		{			
			if(!dto.getEsEliminado().equals(ConstantesBD.acronimoSi))
			{
				drapeau = false;
				for(int i = 0; i < respuesta.size() && !drapeau; i++)
				{
					if(respuesta.get(i).getCodigoServicio() == dto.getCodigoServicio())
						drapeau = true;					
				}
				
				if(!drapeau)		
					respuesta.add(dto);			
			}
		}
		
		return respuesta;
	}	

	/**
	 * @param plantillaSerDiagArray the plantillaSerDiagArray to set
	 */
	public void setPlantillaSerDiagArray(
			ArrayList<DtoPlantillaServDiag> plantillaSerDiagArray) {
		this.plantillaSerDiagArray = plantillaSerDiagArray;
	}

	/**
	 * @return the diagnosticosParamArray
	 */
	public ArrayList<HashMap<String, Object>> getDiagnosticosParamArray() {
		return diagnosticosParamArray;
	}

	/**
	 * @param diagnosticosParamArray the diagnosticosParamArray to set
	 */
	public void setDiagnosticosParamArray(
			ArrayList<HashMap<String, Object>> diagnosticosParamArray) {
		this.diagnosticosParamArray = diagnosticosParamArray;
	}

	/**
	 * @return the plantillaPreviewDto
	 */
	public DtoPlantilla getPlantillaPreviewDto() {
		return plantillaPreviewDto;
	}

	/**
	 * @param plantillaPreviewDto the plantillaPreviewDto to set
	 */
	public void setPlantillaPreviewDto(DtoPlantilla plantillaPreviewDto) {
		this.plantillaPreviewDto = plantillaPreviewDto;
	}

	/**
	 * @return the listElementoTemporal
	 */
	public ArrayList<DtoElementoParam> getListElementoTemporal() {
		return listElementoTemporal;
	}

	/**
	 * @param listElementoTemporal the listElementoTemporal to set
	 */
	public void setListElementoTemporal(
			ArrayList<DtoElementoParam> listElementoTemporal) {
		this.listElementoTemporal = listElementoTemporal;
	}

	/**
	 * @return the indexSeccionesAsocidas
	 */
	public String getIndexSeccionesAsocidas() {
		return indexSeccionesAsocidas;
	}

	/**
	 * @param indexSeccionesAsocidas the indexSeccionesAsocidas to set
	 */
	public void setIndexSeccionesAsocidas(String indexSeccionesAsocidas) {
		this.indexSeccionesAsocidas = indexSeccionesAsocidas;
	}

	/**
	 * @return the indexValoresAsocidos
	 */
	public String getIndexValoresAsocidos() {
		return indexValoresAsocidos;
	}

	/**
	 * @param indexValoresAsocidos the indexValoresAsocidos to set
	 */
	public void setIndexValoresAsocidos(String indexValoresAsocidos) {
		this.indexValoresAsocidos = indexValoresAsocidos;
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