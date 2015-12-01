package com.princetonsa.actionform.interfaz;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.administracion.DtoConceptosRetencion;
import com.princetonsa.dto.interfaz.DtoEventosParam1E;
import com.princetonsa.dto.interfaz.DtoInterfazParamContaS1E;
import com.princetonsa.dto.interfaz.DtoLogParamGenerales1E;
import com.princetonsa.dto.interfaz.DtoTiposInterfazDocumentosParam1E;
import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;


public class ParamInterfazSistema1EForm extends ValidatorForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//	--------------------Atributos
	private Logger logger = Logger.getLogger(InterfazSistemaUnoForm.class);
	private String estado;
	private DtoInterfazParamContaS1E dtoInterfazParam;
	private DtoTiposInterfazDocumentosParam1E dtoTiposInterfaz;
	private ArrayList<HashMap<String, Object>> terceroFacturasParticulares;
	private ArrayList<HashMap<String, Object>> terceroMontoPagoPaciente;
	private HashMap centrosAtencion;
	private HashMap tiposConsecutivo;
	private HashMap unidadesFuncionales;
	private HashMap cuentaContable;
	public ArrayList<DtoTiposInterfazDocumentosParam1E> arrayTiposDoc;
	private ArrayList<HashMap<String, Object>> tiposDoc;
	private ArrayList<HashMap<String, Object>> tiposCon;
	String indice;
	private DtoLogParamGenerales1E dtoTiposInterfazAux;
	private String patronOrdenar;
	private String estadoAnterior;
	
	
	// Anexo 823 Cambio en Funcionalidades
	private ArrayList<DtoConceptosRetencion> arrayConcpRen = new ArrayList<DtoConceptosRetencion>();
	private int posicion;
	private String seccion;
	private boolean errorGuar;
	// Fin Anexo 823 Cambio en Funcionalidades
	
	//Cambios Anexo 833 Agosto 14
	private ArrayList<HashMap<String,Object>> eventos;
	private DtoEventosParam1E dtoEventosParam1e;
	private ArrayList<DtoEventosParam1E> listaEventos;
	private int indiceEvento;
	private String esConsulta;
	//Fin cambios anexo 833
	
	//Cambio Noviembre 2 tipo movimiento
	private boolean movimientoPaciente;
	
	/**
	 * lista centros de costo MT-1453
	 */
	private ArrayList<DtoCentroCosto> listaCentrosCosto = new ArrayList<DtoCentroCosto>();
	//	--------------- Fin Atributos
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
    	ActionErrors errores = new ActionErrors();
    	errores = super.validate(mapping, request);
    	
    	if (this.estado.equals("guardar"))
    	{
    		if (UtilidadTexto.isEmpty(this.dtoInterfazParam.getRealizarCalRetenCxh()))
    				errores.add("", new ActionMessage("errors.required","Elegir una opción en Realizar Cálculo de Retención en Cuentas por Pagar Honorarios de Factura Paciente"));
    		if (UtilidadTexto.isEmpty(this.dtoInterfazParam.getRealizarCalRestenCxes()))
    			errores.add("", new ActionMessage("errors.required","Elegir una opción en Realizar Cálculo de Retención en Cuentas por Pagar Entidades Subcontratadas "));
    		if (UtilidadTexto.isEmpty(this.dtoInterfazParam.getRealizarCalAutoretCxCC()))
    			errores.add("", new ActionMessage("errors.required","Elegir una opción en Realizar Cálculo de Autoretención en CxC Capitación "));
    		if (UtilidadTexto.isEmpty(this.dtoInterfazParam.getRealizarCalRetenCxda()))
    			errores.add("", new ActionMessage("errors.required","Elegir una opción en Realizar Cálculo de Retención en Cuentas por Pagar Autorización Servicios Entidades Subcontratadas  "));
    		if (UtilidadTexto.isEmpty(this.dtoInterfazParam.getRealizarCalAutoretFp()))
    			errores.add("", new ActionMessage("errors.required","Elegir una opción en Realizar Cálculo de Autorretención en Facturación de pacientes  "));
    		if (UtilidadTexto.isEmpty(this.dtoInterfazParam.getRealizarCalAutoretFv()))
    			errores.add("", new ActionMessage("errors.required","Elegir una opción en Realizar Cálculo de Autorretención en Facturación Varia  "));
    		
	    	if (!UtilidadTexto.isEmpty(this.dtoInterfazParam.getFechaControlDesmarcar()))
	    	{
    			if(!UtilidadFecha.esFechaValidaSegunAp(this.dtoInterfazParam.getFechaControlDesmarcar()))			
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "de Control Para Desmarcar Documentos Procesados "+this.dtoInterfazParam.getFechaControlDesmarcar()));
    			else
    			{
    				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.dtoInterfazParam.getFechaControlDesmarcar(), UtilidadFecha.getFechaActual()))
    					errores.add("", new ActionMessage("errors.debeSerNumeroMenorIgual", "La fecha de control para desmarcar documentos procesados "+UtilidadFecha.conversionFormatoFechaAAp(this.dtoInterfazParam.getFechaControlDesmarcar()), "la Fecha Actual "+UtilidadFecha.getFechaActual()));
    			}
    			
	    	}
	    	if(!errores.isEmpty())
	    		this.setErrorGuar(true);
    	}
    	
    	/*if (this.estado.equals("guardarDoc"))
    	{
    		if (UtilidadTexto.isEmpty(this.dtoTiposInterfaz.getTipoDocumento()))
    			errores.add(this.dtoTiposInterfaz.getTipoDocumento(), new ActionMessage("errors.required","El Tipo de documento "));
    	}
    	*/
    	return errores;
	}
	public void reset()
	{
		dtoInterfazParam = new DtoInterfazParamContaS1E();
		dtoTiposInterfaz = new DtoTiposInterfazDocumentosParam1E();
		dtoTiposInterfazAux = new DtoLogParamGenerales1E();
		this.terceroFacturasParticulares = new ArrayList<HashMap<String,Object>>();
		this.terceroMontoPagoPaciente = new ArrayList<HashMap<String,Object>>();
		this.centrosAtencion = new HashMap();
		this.tiposConsecutivo = new HashMap<String, Object>();
		this.tiposConsecutivo.put("numRegistros", "0");
		this.unidadesFuncionales=new HashMap();
		this.unidadesFuncionales.put("numRegistros", "0");
		this.cuentaContable = new HashMap();
		this.cuentaContable.put("numRegistros", "0");
		this.arrayTiposDoc=new ArrayList<DtoTiposInterfazDocumentosParam1E>();
		this.tiposCon=new ArrayList<HashMap<String,Object>>();
		this.tiposDoc=new ArrayList<HashMap<String,Object>>();
		this.eventos=new ArrayList<HashMap<String,Object>>();
		this.indice="";
		this.arrayConcpRen = new ArrayList<DtoConceptosRetencion>();
		this.posicion = ConstantesBD.codigoNuncaValido;
		this.seccion = "";
		this.errorGuar = false;
		this.dtoEventosParam1e= new DtoEventosParam1E();
		this.listaEventos=new ArrayList<DtoEventosParam1E>();
		this.indiceEvento=0;
		this.esConsulta=ConstantesBD.acronimoNo;
		this.patronOrdenar="";
		this.estadoAnterior="";
		this.movimientoPaciente=false;
		this.setListaCentrosCosto(new ArrayList<DtoCentroCosto>());
	}
	public void resetTiposCon()
	{
		this.tiposConsecutivo = new HashMap<String, Object>();
		this.tiposConsecutivo.put("numRegistros", "0");
	}
	
	public void resetEventos()
	{
		this.dtoEventosParam1e=new DtoEventosParam1E();
		this.errorGuar=false;
	}

	public String getEstado() {
		return estado;
	}

	public ArrayList<HashMap<String, Object>> getTerceroFacturasParticulares() {
		return terceroFacturasParticulares;
	}
	public void setTerceroFacturasParticulares(
			ArrayList<HashMap<String, Object>> terceroFacturasParticulares) {
		this.terceroFacturasParticulares = terceroFacturasParticulares;
	}
	public ArrayList<HashMap<String, Object>> getTerceroMontoPagoPaciente() {
		return terceroMontoPagoPaciente;
	}
	public void setTerceroMontoPagoPaciente(
			ArrayList<HashMap<String, Object>> terceroMontoPagoPaciente) {
		this.terceroMontoPagoPaciente = terceroMontoPagoPaciente;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public DtoInterfazParamContaS1E getDtoInterfazParam() {
		return dtoInterfazParam;
	}
	public void setDtoInterfazParam(DtoInterfazParamContaS1E dtoInterfazParam) {
		this.dtoInterfazParam = dtoInterfazParam;
	}
	public DtoTiposInterfazDocumentosParam1E getDtoTiposInterfaz() {
		return dtoTiposInterfaz;
	}
	public void setDtoTiposInterfaz(
			DtoTiposInterfazDocumentosParam1E dtoTiposInterfaz) {
		this.dtoTiposInterfaz = dtoTiposInterfaz;
	}
	public HashMap getCentrosAtencion() {
		return centrosAtencion;
	}
	public void setCentrosAtencion(HashMap centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}
	public HashMap getTiposConsecutivo() {
		return tiposConsecutivo;
	}
	public void setTiposConsecutivo(HashMap tipoSConsecutivo) {
		this.tiposConsecutivo = tipoSConsecutivo;
	}
	
	public Object getTiposConsecutivo(String llave) {
		return tiposConsecutivo.get(llave);
	}

	public void setTiposConsecutivo(String llave, Object obj) {
		this.tiposConsecutivo.put(llave, obj);
	}
	public HashMap getUnidadesFuncionales() {
		return unidadesFuncionales;
	}
	public void setUnidadesFuncionales(HashMap unidadesFuncionales) {
		this.unidadesFuncionales = unidadesFuncionales;
	}
	public HashMap getCuentaContable() {
		return cuentaContable;
	}
	public void setCuentaContable(HashMap cuentaContable) {
		this.cuentaContable = cuentaContable;
	}
	public ArrayList<DtoTiposInterfazDocumentosParam1E> getArrayTiposDoc() {
		return arrayTiposDoc;
	}
	public void setArrayTiposDoc(
			ArrayList<DtoTiposInterfazDocumentosParam1E> arrayTiposDoc) {
		this.arrayTiposDoc = arrayTiposDoc;
	}
	public ArrayList<HashMap<String, Object>> getTiposDoc() {
		return tiposDoc;
	}
	public void setTiposDoc(ArrayList<HashMap<String, Object>> tiposDoc) {
		this.tiposDoc = tiposDoc;
	}
	public ArrayList<HashMap<String, Object>> getTiposCon() {
		return tiposCon;
	}
	public void setTiposCon(ArrayList<HashMap<String, Object>> tiposCon) {
		this.tiposCon = tiposCon;
	}
	public String getIndice() {
		return indice;
	}
	public void setIndice(String indice) {
		this.indice = indice;
	}
	public DtoLogParamGenerales1E getDtoTiposInterfazAux() {
		return dtoTiposInterfazAux;
	}
	public void DtoLogParamGenerales1E(DtoLogParamGenerales1E dtoTiposInterfazAux) {
		this.dtoTiposInterfazAux = dtoTiposInterfazAux;
	}
	/**
	 * @return the arrayConcpRen
	 */
	public ArrayList<DtoConceptosRetencion> getArrayConcpRen() {
		return arrayConcpRen;
	}
	/**
	 * @param arrayConcpRen the arrayConcpRen to set
	 */
	public void setArrayConcpRen(ArrayList<DtoConceptosRetencion> arrayConcpRen) {
		this.arrayConcpRen = arrayConcpRen;
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
	 * @return the seccion
	 */
	public String getSeccion() {
		return seccion;
	}
	/**
	 * @param seccion the seccion to set
	 */
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}
	/**
	 * @return the errorGuar
	 */
	public boolean isErrorGuar() {
		return errorGuar;
	}
	/**
	 * @param errorGuar the errorGuar to set
	 */
	public void setErrorGuar(boolean errorGuar) {
		this.errorGuar = errorGuar;
	}
	public ArrayList<HashMap<String, Object>> getEventos() {
		return eventos;
	}
	public void setEventos(ArrayList<HashMap<String, Object>> eventos) {
		this.eventos = eventos;
	}
	public DtoEventosParam1E getDtoEventosParam1e() {
		return dtoEventosParam1e;
	}
	public void setDtoEventosParam1e(DtoEventosParam1E dtoEventosParam1e) {
		this.dtoEventosParam1e = dtoEventosParam1e;
	}
	public ArrayList<DtoEventosParam1E> getListaEventos() {
		return listaEventos;
	}
	public void setListaEventos(ArrayList<DtoEventosParam1E> listaEventos) {
		this.listaEventos = listaEventos;
	}
	public int getIndiceEvento() {
		return indiceEvento;
	}
	public void setIndiceEvento(int indiceEvento) {
		this.indiceEvento = indiceEvento;
	}
	public String getEsConsulta() {
		return esConsulta;
	}
	public void setEsConsulta(String esConsulta) {
		this.esConsulta = esConsulta;
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
	 * @return the estadoAnterior
	 */
	public String getEstadoAnterior() {
		return estadoAnterior;
	}
	/**
	 * @param estadoAnterior the estadoAnterior to set
	 */
	public void setEstadoAnterior(String estadoAnterior) {
		this.estadoAnterior = estadoAnterior;
	}
	
	public void resetEveCarGlosa()
	{
		this.eventos = new ArrayList<HashMap<String, Object>>();
		this.listaEventos = new ArrayList<DtoEventosParam1E>();
		this.dtoEventosParam1e = new DtoEventosParam1E();
	}
	public boolean isMovimientoPaciente() {
		return movimientoPaciente;
	}
	public void setMovimientoPaciente(boolean movimientoPaciente) {
		this.movimientoPaciente = movimientoPaciente;
	}
	/**
	 * @param listaCentrosCosto the listaCentrosCosto to set
	 */
	public void setListaCentrosCosto(ArrayList<DtoCentroCosto> listaCentrosCosto) {
		this.listaCentrosCosto = listaCentrosCosto;
	}
	/**
	 * @return the listaCentrosCosto
	 */
	public ArrayList<DtoCentroCosto> getListaCentrosCosto() {
		return listaCentrosCosto;
	}
}