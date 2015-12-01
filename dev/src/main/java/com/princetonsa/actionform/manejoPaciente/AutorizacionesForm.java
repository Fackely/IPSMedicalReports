package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoCuentaAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDetAutorizacionEst;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;


public class AutorizacionesForm extends ValidatorForm
{
	/**
	 * 
	 * */
	private String estado;
	
	/**
	 * Son los codigos de las solicitudes o det_cargos separados por coma
	 * */
	private String codigosEvaluar;
	
	/**
	 * guarda informacion de indicadores
	 * */
	private HashMap indicadoresMap = new HashMap();
	
	/**
	 * guarda informacion de indicadores
	 * */
	private HashMap indicadoresConsultaMap = new HashMap();
	
	/**
	 * guarda información de otras funcionalidades desde donde se llama la actual
	 * */
	private HashMap indicadoresOtrasFunc = new HashMap();
	
	/**
	 * Dto de Autorizacion
	 * */
	private DtoAutorizacion autorizacionDto;
	
	/**
	 * Dto de Autorizacion
	 * */
	private DtoAutorizacion autorizacionDtoAux;
	
	/**
	 * Indicador de posicion para los datos adjuntos
	 * */
	private int posAdjuntos;
	
	/**
	 * Indicador de posicion para los datos adjuntos para la respuesta
	 * */
	private int posAdjuntosResp;
	
	//Arreglos
	private ArrayList<HashMap<String, Object>> tiposSerSolArray = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> coberturasSaludArray = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> origenesAtencionArray = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> mediosEnvioArray = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> entidadesEnvioArray = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> conveniosArray = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> profesionalesSolictaArray = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> tiposVigenciaArray = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> cargosUsuarioArray = new ArrayList<HashMap<String,Object>>();
	private ArrayList<DtoCuentaAutorizacion> listadoCuentasArray = new ArrayList<DtoCuentaAutorizacion>();
	private ArrayList<DtoDetAutorizacionEst> listadoEstanciasDisp = new ArrayList<DtoDetAutorizacionEst>();
	private ArrayList<DtoAutorizacion> listadoHistoArray = new ArrayList<DtoAutorizacion>();
	
	//***********************************************
	
	public void reset()
	{		 	
		this.autorizacionDto = new DtoAutorizacion();
		this.autorizacionDtoAux = new DtoAutorizacion();
		this.tiposSerSolArray = new ArrayList<HashMap<String,Object>>();
		this.coberturasSaludArray = new ArrayList<HashMap<String,Object>>();
		this.origenesAtencionArray = new ArrayList<HashMap<String,Object>>();
		this.mediosEnvioArray = new ArrayList<HashMap<String,Object>>();
		this.entidadesEnvioArray = new ArrayList<HashMap<String,Object>>();
		this.conveniosArray = new ArrayList<HashMap<String,Object>>();
		this.profesionalesSolictaArray = new ArrayList<HashMap<String,Object>>();
		this.tiposVigenciaArray = new ArrayList<HashMap<String,Object>>(); 
		this.posAdjuntos = ConstantesBD.codigoNuncaValido;
		this.posAdjuntosResp = ConstantesBD.codigoNuncaValido;
		this.cargosUsuarioArray = new ArrayList<HashMap<String,Object>>();
		this.indicadoresOtrasFunc = new HashMap();
		this.listadoEstanciasDisp = new ArrayList<DtoDetAutorizacionEst>();
		this.listadoHistoArray = new ArrayList<DtoAutorizacion>();
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCodigosEvaluar() {
		return codigosEvaluar;
	}
	
	public String getCodigosEvaluarXComas() {
		String [] codigos = {""};
		String respuesta = "";
		
		if(!this.codigosEvaluar.isEmpty() && 
				!this.codigosEvaluar.equals(""))
		{
			codigos = this.codigosEvaluar.split("@");
			for(int i = 0; i < codigos.length; i++)
				respuesta += codigos[i]+((i==(codigos.length-1))?"":",");				
		}
		
		return respuesta;
	}

	public void setCodigosEvaluar(String codigoEvaluar) {
		this.codigosEvaluar = codigoEvaluar;
	}
	
	public String [] getCodigosEvaluarArray()
	{
		String [] codigos = {""};
		
		if(!this.codigosEvaluar.isEmpty() && 
				!this.codigosEvaluar.equals(""))
		{
			codigos = this.codigosEvaluar.split("@");
		}
		
		return codigos;
	}

	public HashMap getIndicadoresMap() {
		return indicadoresMap;
	}

	public void setIndicadoresMap(HashMap indicadoresMap) {
		this.indicadoresMap = indicadoresMap;
	}
	
	public Object getIndicadoresMap(String key) {
		return indicadoresMap.get(key);
	}

	public void setIndicadoresMap(String key,Object value) {
		this.indicadoresMap.put(key,value);
	}

	public DtoAutorizacion getAutorizacionDto() {
		return autorizacionDto;
	}

	public void setAutorizacionDto(DtoAutorizacion autorizacionDto) {
		this.autorizacionDto = autorizacionDto;
	}

	public ArrayList<HashMap<String, Object>> getTiposSerSolArray() {
		return tiposSerSolArray;
	}

	public void setTiposSerSolArray(
			ArrayList<HashMap<String, Object>> tiposSerSolArray) {
		this.tiposSerSolArray = tiposSerSolArray;
	}

	public ArrayList<HashMap<String, Object>> getCoberturasSaludArray() {
		return coberturasSaludArray;
	}

	public void setCoberturasSaludArray(
			ArrayList<HashMap<String, Object>> coberturasSaludArray) {
		this.coberturasSaludArray = coberturasSaludArray;
	}

	public ArrayList<HashMap<String, Object>> getOrigenesAtencionArray() {
		return origenesAtencionArray;
	}

	public void setOrigenesAtencionArray(
			ArrayList<HashMap<String, Object>> origenesAtencionArray) {
		this.origenesAtencionArray = origenesAtencionArray;
	}

	public ArrayList<HashMap<String, Object>> getMediosEnvioArray() {
		return mediosEnvioArray;
	}

	public void setMediosEnvioArray(
			ArrayList<HashMap<String, Object>> mediosEnvioArray) {
		this.mediosEnvioArray = mediosEnvioArray;
	}

	public ArrayList<HashMap<String, Object>> getEntidadesEnvioArray() {
		return entidadesEnvioArray;
	}

	public void setEntidadesEnvioArray(
			ArrayList<HashMap<String, Object>> entidadesEnvioArray) {
		this.entidadesEnvioArray = entidadesEnvioArray;
	}
	
	public void setTipoOrdenInd(String valor)
	{
		this.indicadoresMap.put("tipoOrdenInd",valor);
	}
	
	public String getTipoOrdenInd()
	{
		return this.indicadoresMap.containsKey("tipoOrdenInd")?this.indicadoresMap.get("tipoOrdenInd").toString():ConstantesBD.codigoNuncaValido+"";
	}
	
	public void setFechaInd(String valor)
	{
		this.indicadoresMap.put("fechaInd",valor);
	}
	
	public String getFechaInd()
	{
		return this.indicadoresMap.containsKey("fechaInd")?this.indicadoresMap.get("fechaInd").toString():"";
	}
	
	public void setCantidadInd(String valor)
	{
		this.indicadoresMap.put("cantidadInd",valor);
	}
	
	public String getCantidadInd()
	{
		return this.indicadoresMap.containsKey("cantidadInd")?this.indicadoresMap.get("cantidadInd").toString():"";
	}
	
	public void setCodigoDetAutoInd(String valor)
	{
		this.indicadoresMap.put("codigoDetAutoInd",valor);
	}
	
	public void setCodigoDetAutoCon(String valor)
	{
		this.indicadoresMap.put("codigoDetAutoCon",valor);
	}
	
	public String getCodigoDetAutoInd()
	{
		return this.indicadoresMap.containsKey("codigoDetAutoInd")?this.indicadoresMap.get("codigoDetAutoInd").toString():ConstantesBD.codigoNuncaValido+"";
	}
	
	public String getCodigoDetAutoCon()
	{
		return this.indicadoresMap.containsKey("codigoDetAutoCon")?this.indicadoresMap.get("codigoDetAutoCon").toString():ConstantesBD.codigoNuncaValido+"";
	}
	
	public void setCodigoPkAutoInd(String valor)
	{
		this.indicadoresMap.put("codigoPkAutoInd",valor);
	}
	
	public void setCodigoPkAutoCon(String valor)
	{
		this.indicadoresMap.put("codigoPkAutoCon",valor);
	}
	
	public String getCodigoPkAutoInd()
	{
		return this.indicadoresMap.containsKey("codigoPkAutoInd")?this.indicadoresMap.get("codigoPkAutoInd").toString():ConstantesBD.codigoNuncaValido+"";
	}
	
	public String getCodigoPkAutoCon()
	{
		return this.indicadoresMap.containsKey("codigoPkAutoCon")?this.indicadoresMap.get("codigoPkAutoCon").toString():ConstantesBD.codigoNuncaValido+"";
	}
	
	public void setIngresoInd(String valor)
	{
		this.indicadoresMap.put("ingresoId",valor);
	}
	
	public String getIngresoInd()
	{
		return this.indicadoresMap.containsKey("ingresoId")?this.indicadoresMap.get("ingresoId").toString():ConstantesBD.codigoNuncaValido+"";
	}
	
	public void setSubCuentaInd(String valor)
	{
		this.indicadoresMap.put("subCuentaId",valor);
	}
	
	public String getSubCuentaInd()
	{
		return this.indicadoresMap.containsKey("subCuentaId")?this.indicadoresMap.get("subCuentaId").toString():ConstantesBD.codigoNuncaValido+"";
	}
	
	public void setConvenioInd(String valor)
	{
		this.indicadoresMap.put("convenioId",valor);
	}
	
	public String getConvenioInd()
	{
		return this.indicadoresMap.containsKey("convenioId")?this.indicadoresMap.get("convenioId").toString():ConstantesBD.codigoNuncaValido+"";
	}
	
	public void setCuentaInd(String valor)
	{
		this.indicadoresMap.put("cuentaId",valor);
	}
	
	public String getTipoAutoInd()
	{
		return this.indicadoresMap.containsKey("tipoAutoInd")?this.indicadoresMap.get("tipoAutoInd").toString():"";
	}
	
	public void setTipoAutoInd(String valor)
	{
		this.indicadoresMap.put("tipoAutoInd",valor);
	}
	
	public void setConvenioEnvioInd(String valor)
	{
		this.indicadoresMap.put("convenioEnvioId",valor);
	}
	
	public String getConvenioEnvioInd()
	{
		return this.indicadoresMap.containsKey("convenioEnvioId")?this.indicadoresMap.get("convenioEnvioId").toString():ConstantesBD.codigoNuncaValido+"";
	}	
	
	public void setProfeSolInd(String valor)
	{
		this.indicadoresMap.put("ProfSolInd",valor);
	}
	
	public String getProfeSolInd()
	{
		return this.indicadoresMap.containsKey("ProfSolInd")?this.indicadoresMap.get("ProfSolInd").toString():ConstantesBD.codigoNuncaValido+"";
	}
	
	public String getCuentaInd()
	{
		return this.indicadoresMap.containsKey("cuentaId")?this.indicadoresMap.get("cuentaId").toString():ConstantesBD.codigoNuncaValido+"";
	}
	
	public void setNombreAtri(String valor)
	{
		this.indicadoresMap.put("nombreAtri",valor);
	}
	
	public String getActualizarAtrasInd()
	{
		return this.indicadoresMap.containsKey("actualizarAtras")?this.indicadoresMap.get("actualizarAtras").toString():ConstantesBD.acronimoNo+"";
	}
	
	public String getEstadoActualizarAtrasInd()
	{
		return this.indicadoresMap.containsKey("estadoActualizarAtras")?this.indicadoresMap.get("estadoActualizarAtras").toString():"";
	}
	
	public String getNombreAtri()
	{
		return this.indicadoresMap.containsKey("nombreAtri")?this.indicadoresMap.get("nombreAtri").toString():"";
	}
	
	public ArrayList<HashMap<String, Object>> getConveniosArray() {
		return conveniosArray;
	}

	public void setConveniosArray(ArrayList<HashMap<String, Object>> conveniosArray) {
		this.conveniosArray = conveniosArray;
	}

	public ArrayList<HashMap<String, Object>> getProfesionalesSolictaArray() {
		return profesionalesSolictaArray;
	}

	public void setProfesionalesSolictaArray(
			ArrayList<HashMap<String, Object>> profesionalesSolictaArray) {
		this.profesionalesSolictaArray = profesionalesSolictaArray;
	}

	public int getPosAdjuntos() {
		return posAdjuntos;
	}

	public void setPosAdjuntos(int posAdjuntos) {
		this.posAdjuntos = posAdjuntos;
	}

	public ArrayList<HashMap<String, Object>> getTiposVigenciaArray() {
		return tiposVigenciaArray;
	}

	public void setTiposVigenciaArray(
			ArrayList<HashMap<String, Object>> tiposVigenciaArray) {
		this.tiposVigenciaArray = tiposVigenciaArray;
	}

	public ArrayList<HashMap<String, Object>> getCargosUsuarioArray() {
		return cargosUsuarioArray;
	}

	public void setCargosUsuarioArray(
			ArrayList<HashMap<String, Object>> cargosUsuarioArray) {
		this.cargosUsuarioArray = cargosUsuarioArray;
	}

	public int getPosAdjuntosResp() {
		return posAdjuntosResp;
	}

	public void setPosAdjuntosResp(int posAdjuntosResp) {
		this.posAdjuntosResp = posAdjuntosResp;
	}

	public DtoAutorizacion getAutorizacionDtoAux() {
		return autorizacionDtoAux;
	}

	public void setAutorizacionDtoAux(DtoAutorizacion autorizacionDtoAux) {
		this.autorizacionDtoAux = autorizacionDtoAux;
	}

	public HashMap getIndicadoresOtrasFunc() {
		return indicadoresOtrasFunc;
	}

	public void setIndicadoresOtrasFunc(HashMap indicadoresOtrasFunc) {
		this.indicadoresOtrasFunc = indicadoresOtrasFunc;
	}	
	
	public Object getIndicadoresOtrasFunc(String key) {
		return indicadoresOtrasFunc.get(key);
	}

	public void setIndicadoresOtrasFunc(String key,Object value) {
		this.indicadoresOtrasFunc.put(key,value);
	}
	
	public void setFuncInd(String valor)
	{
		this.indicadoresMap.put("funcInd",valor);
	}
	
	public String getFuncInd()
	{
		return this.indicadoresMap.containsKey("funcInd")?this.indicadoresMap.get("funcInd").toString():"";
	}

	public ArrayList<DtoCuentaAutorizacion> getListadoCuentasArray() {
		return listadoCuentasArray;
	}

	public void setListadoCuentasArray(
			ArrayList<DtoCuentaAutorizacion> listadoCuentasArray) {
		this.listadoCuentasArray = listadoCuentasArray;
	}

	public ArrayList<DtoDetAutorizacionEst> getListadoEstanciasDisp() {
		return listadoEstanciasDisp;
	}

	public void setListadoEstanciasDisp(
			ArrayList<DtoDetAutorizacionEst> listadoEstanciasDisp) {
		this.listadoEstanciasDisp = listadoEstanciasDisp;
	}

	public ArrayList<DtoAutorizacion> getListadoHistoArray() {
		return listadoHistoArray;
	}

	public void setListadoHistoArray(ArrayList<DtoAutorizacion> listadoHistoArray) {
		this.listadoHistoArray = listadoHistoArray;
	}

	public HashMap getIndicadoresConsultaMap() {
		return indicadoresConsultaMap;
	}

	public void setIndicadoresConsultaMap(HashMap indicadoresConsultaMap) {
		this.indicadoresConsultaMap = indicadoresConsultaMap;
	}
}