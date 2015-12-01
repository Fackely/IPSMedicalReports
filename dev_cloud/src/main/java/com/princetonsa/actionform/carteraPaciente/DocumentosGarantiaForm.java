package com.princetonsa.actionform.carteraPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.servinte.axioma.orm.Ciudades;

/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 * */
public class DocumentosGarantiaForm extends ValidatorForm 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//---Atributos
	
	Logger logger = Logger.getLogger(DocumentosGarantiaForm.class);
	
	/*-----------------------------------------------------
	 * ATRIBUTOS DEL PAGER 
	 * ---------------------------------------------------*/
	
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * String Link Siguiente
	 * */
	private String linkSiguiente;	
	
	
	/*-----------------------------------------------------
	 * ATRIBUTOS DOCUMENTOS GARANTIA
	 * ---------------------------------------------------*/	
	/**
	 * HashMap lista Documentos Garantia 
	 * */
	private HashMap listaDocGarantiaMap;
	
	
	/**
	 * HashMap de Documentos de Garantia
	 * */
	private HashMap documentosGarantiaMap;
	
	/**
	 * indica el documento al cual se le estan generando los documentos garantia
	 * String IndexDocumentosMap
	 * */
	private String indexDocumentosMap;
	
	/**
	 * indica el tipo de documento a generar
	 * */
	private String indexTipoDocumento;
	
	
	/**
	 * HashMap de Centros de Atencion
	 */
	private HashMap centrosAtencionMap;
	
	/**
	 * Hashmap para la busuqeda por Rangos
	 */
	private HashMap busquedaRangosMap;
	
	/**
	 * indica si tiene permisos o no de modificar
	 */
	private boolean permisoModificar;
	
	
	/**
	 * indica si se refresca el paciente en session 
	 * */
	private boolean refrescarPaciente = false;
	
	/*-----------------------------------------------------
	 * ATRIBUTOS INGRESO PACIENTE 
	 * ---------------------------------------------------*/
	
	/**
	 * HasMap Ingresos
	 * */
	private HashMap ingresosMap;
	
	/**
	 * indica el ingreso al cual se le estan generando los documentos garantia
	 * String IndexIngresos
	 * */
	private String indexIngresosMap;
	
	/**
	 * indica cual es la clase de DeudorCo actual (Deudor o Codeudor)
	 * */
	private String valorClaseDeudorCo;
	
	/*-----------------------------------------------------
	 * ATRIBUTOS DEUDORCO PACIENTE 
	 * ---------------------------------------------------*/
	
	/**
	 * HashMap Deudorco
	 * */
	private HashMap deudorCoMap;
	
	/**
	 * HashMap donde se almacena la informacion del deudor
	 */
	private HashMap deudorMap;
	
	/**
	 * HashMap donde se almacena la informacion del codeudor
	 */
	private HashMap codeudorMap;
	
	/**
	 * HashMap TipoIdentificacion 
	 * */
	private ArrayList tipoIdentificacionMap;
	
	/**
	 * HashMap Paises
	 * */
	private ArrayList paisesMap;
	
	/**
	 * String Id Pais Seleccionado 
	 * */
	private String codigoPais;
	
	/**
	 * HashMap Ciudades
	 * */
	private ArrayList<Ciudades> ciudadesMap;	
	
	/**
	 * String codigo Barrio
	 * */
	private String codigoBarrio;
	
	/**
	 * String nombre barrio
	 * */
	private String nombreBarrio;
	
	/**
	 * String estado
	 * */
	private String estado;	
	
	/**
	 * Variable que oculta el encabezado 
	 */
	private boolean ocultarEncabezado = false;
	
	/**
	 * Se verifica si la funcionalidad es Dummy
	 */
	private boolean funcionalidadDummy = false;
	
	/**
	 * Variable que almacena el nombre de los documentos a imprimir
	 */
	private String documentosImpresion ="";
	
	private String ciudadExpedicion;
	
	private String ciudadResidencia;
	
	/**
	 * HashMap Ciudades
	 * */
	private ArrayList<Ciudades> ciudadesResidenciaMap;	
	
	/*-----------------------------------------------------
	 * FIN ATRIBUTOS DEUDORCO PACIENTE 
	 * ---------------------------------------------------*/
	/*-----------------------------------------------------
	 * ATRIBUTOS PARA LAS FUNCIONIALIDADES DUMMY 
	 * ---------------------------------------------------*/
	private boolean soloDocumentosVigentes = false;
	private String idIngreso = "";
	private String institucion = "";
	private String codigoPaciente = "";
	private String datosResponsablePaciente = "";
	private String idCuenta = "";
	
	/**********************************************************
	 * Atributos cambios anexo 762
	 **********************************************************/
	private boolean imprimePagare = true;
	
	private String mensaje = "";
	
	//---Fin Atributos

	
	//---Metodos
	
	/**
     * Validate the properties that have been set from this HTTP request, and
     * return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no recorded
     * error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
    */
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
    	ActionErrors errores = new ActionErrors();
    	errores = super.validate(mapping, request);    	
		
    	if(estado.equals("guardarDeudorCo"))
    	{    		
    		if(this.getDeudorCoMap("tipoIdentificacion").toString().equals(""))
    			errores.add("descripcion",new ActionMessage("errors.required","El tipo de Identificacion "));
    		
    		if(this.getDeudorCoMap("numeroIdentificacion").toString().equals(""))
    			errores.add("descripcion",new ActionMessage("errors.required","El Numero de Identificacion "));
    		
    		if(this.getDeudorCoMap("codigoPais").toString().equals(""))
    			errores.add("descripcion",new ActionMessage("errors.required","El Pais de Expedicion "));
    		
    		if(this.getDeudorCoMap("codigoCiudad").toString().equals(""))
    			errores.add("descripcion",new ActionMessage("errors.required","La Ciudad de Expedicion "));
    		
    		if(this.getDeudorCoMap("primerApellido").toString().equals(""))
    			errores.add("descripcion",new ActionMessage("errors.required","El Primer Apellido "));
    		
    		if(this.getDeudorCoMap("primerApellido").toString().equals(""))
    			errores.add("descripcion",new ActionMessage("errors.required","El Primer Apellido "));
    		
    		if(this.getDeudorCoMap("primerNombre").toString().equals(""))
    			errores.add("descripcion",new ActionMessage("errors.required","El Primer Nombre "));
    		
    		if(this.getDeudorCoMap("direccionReside").toString().equals(""))
    			errores.add("descripcion",new ActionMessage("errors.required","El Dirección de Residencia "));
    		
    		if(this.getDeudorCoMap("codigoPaisReside").toString().equals(""))
    			errores.add("descripcion",new ActionMessage("errors.required","El Codigo del Pais de Residencia "));
    		
    		if(this.getDeudorCoMap("codigoCiudadReside").toString().equals(""))
    			errores.add("descripcion",new ActionMessage("errors.required","El Codigo de la Ciudad de Residencia "));
    		
    		if(this.getDeudorCoMap("telefonoReside").toString().equals(""))
    			errores.add("descripcion",new ActionMessage("errors.required","El Telefono del Paciente "));    		
    		
    		if(this.getDeudorCoMap("fechaNacimiento").toString().equals(""))
    		{
    			errores.add("descripcion",new ActionMessage("errors.required","La Fecha de Nacimiento "));
    		}
    		else
    		{
				if(!UtilidadFecha.validarFecha(this.getDeudorCoMap("fechaNacimiento").toString()))
				{
					errores.add("descripcion",new ActionMessage("errors.invalid","La Fecha de Nacimiento "+this.getDeudorCoMap("fechaNacimiento").toString()+" "));
				}
				else
				{
					if(ValoresPorDefecto.getValidarEdadDeudorPaciente(Integer.parseInt(this.getIngresosMap("institucion_"+this.getIndexIngresosMap()).toString())).toString().equals(ConstantesBD.acronimoSi))
					{
						if(UtilidadFecha.calcularEdad(this.getDeudorCoMap("fechaNacimiento").toString()) < Integer.parseInt(ValoresPorDefecto.getAniosBaseEdadAdulta(Integer.parseInt(this.getIngresosMap("institucion_"+this.getIndexIngresosMap()).toString())).toString()))
							errores.add("descripcion",new ActionMessage("errors.invalid","La Edad debe ser Mayor a "+ValoresPorDefecto.getAniosBaseEdadAdulta(Integer.parseInt(this.getIngresosMap("institucion_"+this.getIndexIngresosMap()).toString())).toString()));
					}
				}
				
				//valida que la fecha no se ha igual a la actual
				if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",this.getDeudorCoMap("fechaNacimiento").toString(),"00:00").isTrue())
					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.getDeudorCoMap("fechaNacimiento").toString(),UtilidadFecha.getFechaActual()));						
    		}
    		
    		
    		if(this.getDeudorCoMap("tipoDeudorCo").equals(ConstantesIntegridadDominio.acronimoPaciente))
    		{    			
	    		if(!this.getDeudorCoMap("relacionPaciente").toString().equals(""))
	    			errores.add("descripcion",new ActionMessage("errors.invalid","El Campo Relacion Paciente en el caso Tipo Deudor igual Paciente debe estar Vacio. Campo Relacion Paciente  "));
    		}	
    		else
    		{    		
    			if(this.getDeudorCoMap("relacionPaciente").toString().equals(""))
	    			errores.add("descripcion",new ActionMessage("errors.required","La Relacion Paciente "));    		
    		}
    		
    		
    		if(this.getDeudorCoMap("tipoOcupacion").toString().equals(""))
    			errores.add("descripcion",new ActionMessage("errors.required","Tipo Ocupacion "));
    		
    		if(this.getDeudorCoMap("ocupacion").toString().equals(""))
    			errores.add("descripcion",new ActionMessage("errors.required","Ocupacion "));
    		
    		
    		if(this.getDeudorCoMap("tipoOcupacion").toString().equals(ConstantesIntegridadDominio.acronimoTipoTrabajadorEmpleado))
    		{
    			
    			if(this.getDeudorCoMap("empresa").toString().equals(""))
        			errores.add("descripcion",new ActionMessage("errors.required","Empresa "));
    			
    			if(this.getDeudorCoMap("cargo").toString().equals(""))
        			errores.add("descripcion",new ActionMessage("errors.required","Cargo "));
    			
    			if(this.getDeudorCoMap("antiguedad").toString().equals(""))
        			errores.add("descripcion",new ActionMessage("errors.required","Antiguedad ")); 
    			
    			if(this.getDeudorCoMap("direccionOficina").toString().equals(""))
        			errores.add("descripcion",new ActionMessage("errors.required","Direccion Oficina "));  			
    			
    			if(this.getDeudorCoMap("telefonoOficina").toString().equals(""))
        			errores.add("descripcion",new ActionMessage("errors.required","Telefono Oficina "));
    		}
    		else
	    		if(this.getDeudorCoMap("tipoOcupacion").toString().equals(ConstantesIntegridadDominio.acronimoTipoTrabajadorIndependiente))
	    		{
	    			if(this.getDeudorCoMap("direccionOficina").toString().equals(""))
	        			errores.add("descripcion",new ActionMessage("errors.required","Direccion Oficina "));  			
	    			
	    			if(this.getDeudorCoMap("telefonoOficina").toString().equals(""))
	        			errores.add("descripcion",new ActionMessage("errors.required","Telefono Oficina "));
	    		}
    		
    		if(this.getDeudorCoMap("nombresReferencia").toString().equals(""))
    			errores.add("descripcion",new ActionMessage("errors.required","Nombre de la Referencia "));
    		
    		if(this.getDeudorCoMap("direccionReferencia").toString().equals(""))
    			errores.add("descripcion",new ActionMessage("errors.required","Direccion de la Referencia "));
    		
    		if(this.getDeudorCoMap("telefonoReferencia").toString().equals(""))
    			errores.add("descripcion",new ActionMessage("errors.required","Telefono de la Referencia "));
    	}   	
    	
    	return errores;
	}


    
    
    /**
     * Incializa los atributos de DeudorCo
     * */
    public void reset()
    {
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	this.linkSiguiente="";
    	this.imprimePagare = true;
    	this.mensaje = "";
    	this.indexDocumentosMap = "";
    }
    
	/**
	 * Incializa los atributos de DeudorCo
	 * */
	public void resetDeudorCo()
	{
		this.ingresosMap = new HashMap();
		this.deudorCoMap = new HashMap();
		this.tipoIdentificacionMap = new ArrayList();
		this.paisesMap = new ArrayList();
		this.ciudadesMap = new ArrayList<Ciudades>();
		ciudadesResidenciaMap = new ArrayList<Ciudades>();
		
		this.indexIngresosMap ="";
		this.indexDocumentosMap = "";
		
		this.funcionalidadDummy = false;
		this.ocultarEncabezado = false;
		this.imprimePagare = true;
    	this.mensaje = "";
    	this.ciudadExpedicion="";
    	this.ciudadResidencia="";
	}
	
	
	public void resetBusquedaRangos ()
	{
		this.ingresosMap = new HashMap();
		this.ingresosMap.put("numRegistros", "0");
		this.ingresosMap.put("forward", "busquedaRangos");
		this.indexDocumentosMap = "";
		this.imprimePagare = true;
    	this.mensaje = "";
	}
	
	/**
	 * Inicializa los atributos de Documentos Garantia
	 * */
	public void resetDocumentosGarantia()
	{
		this.listaDocGarantiaMap = new HashMap();
		this.documentosGarantiaMap = new HashMap();
		this.indexTipoDocumento = "";
		this.imprimePagare = true;
    	this.mensaje = "";
    	this.indexDocumentosMap = "";
	}
	
	
	/**
	 * Inicializa los centros de Atencion 
	 * @param int institucion
	 * */
	public void iniciarCentroAtencion(int codigoInstitucion)
	{	
		this.centrosAtencionMap =Utilidades.obtenerCentrosAtencion(codigoInstitucion);		
	}
	
	
	public void iniciarBusquedaRangos ()
	{
		this.busquedaRangosMap = new HashMap();
	}
	
	
	
	/**
	 * @return the deudorCoMap
	 */
	public HashMap getDeudorCoMap() {
		return deudorCoMap;
	}
	
	
	
	/**
	 * @return the deudorCoMap
	 */
	public Object getDeudorCoMap(String key) {
		return deudorCoMap.get(key);
	}

	/**
	 * @param deudorCoMap the deudorCoMap to set
	 */
	public void setDeudorCoMap(HashMap deudorCoMap) {
		this.deudorCoMap = deudorCoMap;
	}
	
	/**
	 * @param deudorCoMap the deudorCoMap to set
	 */
	public void setDeudorCoMap(String key, Object value) {
		this.deudorCoMap.put(key, value);
		if(key.equals("codigoCiudad")){
			this.ciudadExpedicion = (String)value;
		}else{
			if(key.equals("codigoCiudadReside")){
				this.ciudadResidencia = (String)value;
			}
		}
	}


	/**
	 * @return the ingresosMap
	 */
	public HashMap getIngresosMap() {
		return ingresosMap;
	}
	
	/**
	 * @return the ingresosMap
	 */
	public Object getIngresosMap(String key) {
		return ingresosMap.get(key);
	}


	/**
	 * @param ingresosMap the ingresosMap to set
	 */
	public void setIngresosMap(HashMap ingresosMap) {
		this.ingresosMap = ingresosMap;
	}
	
	/**
	 * @param ingresosMap the ingresosMap to set
	 */
	public void setIngresosMap(String key, Object value) {
		this.ingresosMap.put(key, value);
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
	 * @return the indexIngresos
	 */
	public String getIndexIngresosMap() {
		return indexIngresosMap;
	}


	/**
	 * @param indexIngresos the indexIngresos to set
	 */
	public void setIndexIngresosMap(String indexIngresos) {
		this.indexIngresosMap = indexIngresos;
	}

	/**
	 * @return the paisesMap
	 */
	public ArrayList getPaisesMap() {
		return paisesMap;
	}


	/**
	 * @param paisesMap the paisesMap to set
	 */
	public void setPaisesMap(ArrayList paisesMap) {
		this.paisesMap = paisesMap;
	}


	


	/**
	 * @return the codigoPais
	 */
	public String getCodigoPais() {
		return codigoPais;
	}


	/**
	 * @param codigoPais the codigoPais to set
	 */
	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}


	/**
	 * @return the codigoBarrio
	 */
	public String getCodigoBarrio() {
		return codigoBarrio;
	}


	/**
	 * @param codigoBarrio the codigoBarrio to set
	 */
	public void setCodigoBarrio(String codigoBarrio) {
		this.codigoBarrio = codigoBarrio;
	}

	
	/**
	 * @return the nombreBarrio
	 */
	public String getNombreBarrio() {
		return nombreBarrio;
	}


	/**
	 * @param nombreBarrio the nombreBarrio to set
	 */
	public void setNombreBarrio(String nombreBarrio) {
		this.nombreBarrio = nombreBarrio;
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
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}


	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}


	/**
	 * @return the tipoIdentificacionMap
	 */
	public ArrayList getTipoIdentificacionMap() {
		return tipoIdentificacionMap;
	}


	/**
	 * @param tipoIdentificacionMap the tipoIdentificacionMap to set
	 */
	public void setTipoIdentificacionMap(ArrayList tipoIdentificacionMap) {
		this.tipoIdentificacionMap = tipoIdentificacionMap;
	}


	/**
	 * @return the valorClaseDeudorCo
	 */
	public String getValorClaseDeudorCo() {
		return valorClaseDeudorCo;
	}


	/**
	 * @param valorClaseDeudorCo the valorClaseDeudorCo to set
	 */
	public void setValorClaseDeudorCo(String valorClaseDeudorCo) {
		this.valorClaseDeudorCo = valorClaseDeudorCo;
	}




	/**
	 * @return the ocultarEncabezado
	 */
	public boolean isOcultarEncabezado() {
		return ocultarEncabezado;
	}




	/**
	 * @param ocultarEncabezado the ocultarEncabezado to set
	 */
	public void setOcultarEncabezado(boolean ocultarEncabezado) {
		this.ocultarEncabezado = ocultarEncabezado;
	}




	/**
	 * @return the funcionalidadDummy
	 */
	public boolean isFuncionalidadDummy() {
		return funcionalidadDummy;
	}




	/**
	 * @param funcionalidadDummy the funcionalidadDummy to set
	 */
	public void setFuncionalidadDummy(boolean funcionalidadDummy) {
		this.funcionalidadDummy = funcionalidadDummy;
	}




	/**
	 * @return the listaDocGarantia
	 */
	public HashMap getListaDocGarantiaMap() {
		return listaDocGarantiaMap;
	}



	/**
	 * @param listaDocGarantia the listaDocGarantia to set
	 */
	public void setListaDocGarantiaMap(HashMap listaDocGarantiaMap) {
		this.listaDocGarantiaMap = listaDocGarantiaMap;
	}

	/**
	 * @return the listaDocGarantia
	 */
	public Object getListaDocGarantiaMap(String key) {
		return listaDocGarantiaMap.get(key);
	}


	/**
	 * @param listaDocGarantia the listaDocGarantia to set
	 */
	public void setListaDocGarantiaMap(String key, Object value) {
		this.listaDocGarantiaMap.put(key, value);
	}




	/**
	 * @return the indexDocumentosMap
	 */
	public String getIndexDocumentosMap() {
		return indexDocumentosMap;
	}




	/**
	 * @param indexDocumentosMap the indexDocumentosMap to set
	 */
	public void setIndexDocumentosMap(String indexDocumentosMap) {
		this.indexDocumentosMap = indexDocumentosMap;
	}




	/**
	 * @return the indexTipoDocumento
	 */
	public String getIndexTipoDocumento() {
		return indexTipoDocumento;
	}




	/**
	 * @param indexTipoDocumento the indexTipoDocumento to set
	 */
	public void setIndexTipoDocumento(String indexTipoDocumento) {
		this.indexTipoDocumento = indexTipoDocumento;
	}




	/**
	 * @return the documentosGarantiaMap
	 */
	public HashMap getDocumentosGarantiaMap() {
		return documentosGarantiaMap;
	}




	/**
	 * @param documentosGarantiaMap the documentosGarantiaMap to set
	 */
	public void setDocumentosGarantiaMap(HashMap documentosGarantiaMap) {
		this.documentosGarantiaMap = documentosGarantiaMap;
	}
	
	
	/**
	 * @return the documentosGarantiaMap
	 */
	public Object getDocumentosGarantiaMap(String key) {
		return documentosGarantiaMap.get(key);
	}




	/**
	 * @param documentosGarantiaMap the documentosGarantiaMap to set
	 */
	public void setDocumentosGarantiaMap(String key, Object value) {
		this.documentosGarantiaMap.put(key, value);
	}




	/**
	 * @return the idIngreso
	 */
	public String getIdIngreso() {
		return idIngreso;
	}




	/**
	 * @param idIngreso the idIngreso to set
	 */
	public void setIdIngreso(String idIngreso) {
		this.idIngreso = idIngreso;
	}




	/**
	 * @return the institucion
	 */
	public String getInstitucion() {
		return institucion;
	}




	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}




	/**
	 * @return the codigoPaciente
	 */
	public String getCodigoPaciente() {
		return codigoPaciente;
	}




	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(String codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}




	/**
	 * @return the datosResponsablePaciente
	 */
	public String getDatosResponsablePaciente() {
		return datosResponsablePaciente;
	}




	/**
	 * @param datosResponsablePaciente the datosResponsablePaciente to set
	 */
	public void setDatosResponsablePaciente(String datosResponsablePaciente) {
		this.datosResponsablePaciente = datosResponsablePaciente;
	}




	/**
	 * @return the idCuenta
	 */
	public String getIdCuenta() {
		return idCuenta;
	}




	/**
	 * @param idCuenta the idCuenta to set
	 */
	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}




	/**
	 * @return the soloDocumentosVigentes
	 */
	public boolean isSoloDocumentosVigentes() {
		return soloDocumentosVigentes;
	}




	/**
	 * @param soloDocumentosVigentes the soloDocumentosVigentes to set
	 */
	public void setSoloDocumentosVigentes(boolean soloDocumentosVigentes) {
		this.soloDocumentosVigentes = soloDocumentosVigentes;
	}


/**
 * Metodo que retorna el HashMap completo de codeudor
 * @return codeudorMap
 */

	public HashMap getCodeudorMap() {
		return codeudorMap;
	}

	/**
	 * Metodo que retorna el valor del key que se ingrese
	 * @return Object
	 */
	public Object getCodeudorMap (String key){
		return codeudorMap.get(key);
	}

/**
 * Metodo que inserta en el HashMap codeudorMap el valor que viene variable
 * @param codeudorMap
 */
	public void setCodeudorMap(HashMap codeudorMap) {
		this.codeudorMap = codeudorMap;
	}
	
	/**
	 * Metodo que inserta en el HashMap codeudorMap el Valor y el key que llegan ocmo parametros 
	 * @param key
	 * @param value
	 */
	public void setCodeudorMap (String key,Object value)
	{
		this.codeudorMap.put(key, value);
	}

	

	/**
	 * Metodo que retorna el HashMap completo de codeudor
	 * @return codeudorMap
	 */
	public HashMap getDeudorMap() {
		return deudorMap;
	}

	/**
	 * Metodo que retorna el valor del key que se ingrese
	 * @return Object
	 */
	public Object getDeudorMap (String key){
		return deudorMap.get(key);
	}


	/**
	 * Metodo que inserta en el HashMap deudorMap el valor que viene variable
	 * @param codeudorMap
	 */
	public void setDeudorMap(HashMap deudorMap) {
		this.deudorMap = deudorMap;
	}
	
	/**
	 * Metodo que inserta en el HashMap deudorMap el Valor y el key que llegan ocmo parametros 
	 * @param key
	 * @param value
	 */
	public void setDeudorMap (String key,Object value)
	{
		this.deudorMap.put(key, value);
	}



	/**
	 * Metodo que devuelve un hashmap con los centros de atencion
	 * @return
	 */
	public HashMap getCentrosAtencionMap()
	{
		return centrosAtencionMap;
	}
	
	/**
	 * Metodo que devuelve un centro de atencion 
	 * @param key
	 * @return
	 */
	public Object getCentrosAtencionMap(String key)
	{
		return centrosAtencionMap.get(key);
	}



	/**
	 * Metodo que inserta en el hashMap de centrosAtencion
	 * @param centrosAtencion
	 */
	public void setCentrosAtencionMap(HashMap centrosAtencionMap) {
		this.centrosAtencionMap = centrosAtencionMap;
	}
	
	/**
	 * Metodo que inserta en el hashMap de centrosAtencion mediante un valor y un key
	 * @param centrosAtencion
	 */
	public void setCentrosAtencionMap(String key,Object value) {
		this.centrosAtencionMap.put(key, value);
	}



	/**
	 * Hashmap Bsuqueda por rangos
	 * @return
	 */
	public HashMap getBusquedaRangosMap()
	{
		return busquedaRangosMap;
	}
	
	public Object getBusquedaRangosMap(String key)
	{
		return busquedaRangosMap.get(key);
	}




	public void setBusquedaRangosMap(HashMap busquedaRangosMap)
	{
		this.busquedaRangosMap = busquedaRangosMap;
	}
	
	public void setBusquedaRangosMap(String key, Object value)
	{
		this.busquedaRangosMap.put(key, value);
	}




	public boolean isPermisoModificar() {
		return permisoModificar;
	}




	public void setPermisoModificar(boolean permisoModificar) {
		this.permisoModificar = permisoModificar;
	}




	/**
	 * @return the refrescarPaciente
	 */
	public boolean isRefrescarPaciente() {
		return refrescarPaciente;
	}




	/**
	 * @param refrescarPaciente the refrescarPaciente to set
	 */
	public void setRefrescarPaciente(boolean refrescarPaciente) {
		this.refrescarPaciente = refrescarPaciente;
	}




	public String getDocumentosImpresion() {
		return documentosImpresion;
	}




	public void setDocumentosImpresion(String documentosImpresion) {
		this.documentosImpresion = documentosImpresion;
	}




	/**
	 * @return the imprimePagare
	 */
	public boolean isImprimePagare() {
		return imprimePagare;
	}




	/**
	 * @param imprimePagare the imprimePagare to set
	 */
	public void setImprimePagare(boolean imprimePagare) {
		this.imprimePagare = imprimePagare;
	}




	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}




	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}




	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo ciudadesMap
	
	 * @return retorna la variable ciudadesMap 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<Ciudades> getCiudadesMap() {
		return ciudadesMap;
	}




	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo ciudadesMap
	
	 * @param valor para el atributo ciudadesMap 
	 * @author Angela Maria Aguirre 
	 */
	public void setCiudadesMap(ArrayList<Ciudades> ciudadesMap) {
		this.ciudadesMap = ciudadesMap;
	}




	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo ciudadExpedicion
	
	 * @return retorna la variable ciudadExpedicion 
	 * @author Angela Maria Aguirre 
	 */
	public String getCiudadExpedicion() {
		return ciudadExpedicion;
	}




	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo ciudadExpedicion
	
	 * @param valor para el atributo ciudadExpedicion 
	 * @author Angela Maria Aguirre 
	 */
	public void setCiudadExpedicion(String ciudadExpedicion) {
		this.ciudadExpedicion = ciudadExpedicion;
		this.deudorCoMap.put("codigoCiudad", ciudadExpedicion);
	}




	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo ciudadResidencia
	
	 * @return retorna la variable ciudadResidencia 
	 * @author Angela Maria Aguirre 
	 */
	public String getCiudadResidencia() {
		return ciudadResidencia;
	}




	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo ciudadResidencia
	
	 * @param valor para el atributo ciudadResidencia 
	 * @author Angela Maria Aguirre 
	 */
	public void setCiudadResidencia(String ciudadResidencia) {
		this.ciudadResidencia = ciudadResidencia;
		this.deudorCoMap.put("codigoCiudadReside", ciudadResidencia);
	}




	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo ciudadesResidenciaMap
	
	 * @return retorna la variable ciudadesResidenciaMap 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<Ciudades> getCiudadesResidenciaMap() {
		return ciudadesResidenciaMap;
	}




	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo ciudadesResidenciaMap
	
	 * @param valor para el atributo ciudadesResidenciaMap 
	 * @author Angela Maria Aguirre 
	 */
	public void setCiudadesResidenciaMap(ArrayList<Ciudades> ciudadesResidenciaMap) {
		this.ciudadesResidenciaMap = ciudadesResidenciaMap;
	}
	
	//---Fin Metodos
}