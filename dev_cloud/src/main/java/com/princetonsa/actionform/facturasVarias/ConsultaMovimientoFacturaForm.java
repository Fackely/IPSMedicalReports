package com.princetonsa.actionform.facturasVarias;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRDataSource;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import org.hibernate.criterion.Restrictions;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.consultaExterna.DtoConceptoFacturaVaria;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DtoReportePresupuestosOdontologicosContratadosConPromocion;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.RegionesCobertura;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * @author Mauricio Jaramillo
 * Fecha: Agosto de 2008
 */

public class ConsultaMovimientoFacturaForm extends ValidatorForm
{

	private String estado;
	
	/**
	 * Atributo usado para completar la ruta hacia la cual se debe 
	 * direccionar la aplicaci&oacute;n.
	 */
	private String path;
	/**
	 * Fecha Inicial
	 */
	private String fechaInicial;
	
	/**
	 * Fecha Final
	 */
	private String fechaFinal;
	
	/**
	 * String para el tipo de deudor seleccionado
	 */
	private String tipoDeudor;
	
	/**
	 * ArrayList con la toda la información de empresas o deudores parametrizada en el sistema
	 */
	private ArrayList<HashMap<String,Object>> deudores;
	
	/**
	 * Código del deudor seleccionado
	 */
	private String codigoDeudor;
	
	
	
	private String descripDeudor;
	
	/**
	 * Número de la factura digitado
	 */
	private String numeroFactura;
	
	/**
	 * HashMap con los resultados de la consulta de movimientos por factura
	 */
	private HashMap consultaMovimientoFactura;

	/**
	 * HashMap con los resultados de la consulta de movimientos por factura en la parte de detalle (Información Factura - Información Deudor)
	 */
	private HashMap consultaDetalleMovimientoFactura;
	
	/**
	 * HashMap con los resultados de la consulta de movimientos de detalle para la parte de movimientos de ajustes
	 */
	private HashMap consultaDetalleAjustesFactura;
	
	/**
	 * HashMap con los resultados de la consulta de movimientos de detalle para la parte de movimientos de pagos
	 */
	private HashMap consultaDetallePagosFactura;
	
	/**
	 * HashMap con los resultados de la consulta de movimientos de detalle para la parte de resumen de movimientos
	 */
	private HashMap consultaDetalleResumenFactura;
    
    /**
     * Posicion del Registro Seleccionado
     */
    private int posicion;
    
    /**
     * Patron para realizar el ordenamiento
     */
    private String patronOrdenar;
	
    /**
     * Ultimo patron por el que se realizo el ordenamiento
     */
    private String ultimoPatron;
    
    /***************************************
     * Atributos para listar en el jsp
     ****************************************/
    /**
	 * Atributo que almacena el listado de los paises.
	 */
	private ArrayList<Paises> listaPaises;
	
	/**
	 * Atributo que almacena el listado de las ciudades 
	 * pertenecientes a un pa&iacute;s determinado.
	 */
	private ArrayList<Ciudades> listaCiudades;
	
	/**
	 * Atributo que almacena el listado de regiones de 
	 * cobertura que se encuentran activas en el sistema.
	 */
	private ArrayList<RegionesCobertura> listaRegiones;
	
	/**
	 * Atributo que almacena el listado de empresas
	 * institucion existentes en el sistema.
	 */
	private ArrayList<EmpresasInstitucion> listaEmpresaInstitucion;
	
	/**
	 * Atributo que almacena el listado de los centros de atenci&oacute;n.
	 */
	private ArrayList<DtoCentrosAtencion> listaCentrosAtencion;
	
	/**
	 * Atributo que almacena el listado de los conceptos de factura.
	 */
	private ArrayList<DtoConceptoFacturaVaria> listaConceptos;
	/**
	 * Atributo que almacena el listado de los estados de factura.
	 */
	private ArrayList<DtoIntegridadDominio> listaEstados;
	
	/**
	 * Atributo donde se almacenan los usuarios existentes en el sistema.
	 */
	private ArrayList<DtoUsuarioPersona> usuarios;
	
	/**
	 * Atributo que indica si el campo de ciudad se encuentra deshabilitado.
	 */
	private boolean deshabilitaCiudad;
	
	/**
	 * Atributo que indica si el campo de regi&oacute;n se encuentra deshabilitado.
	 */
	private boolean deshabilitaRegion;
			
	/**
	 * Atributo que indica si la institucion-empresa es multiempresa o no.
	 */
	private String esMultiempresa;
    
	/***************************************
     * Fin Atributos para listar en el jsp
     ****************************************/
	/**
	 * Atributo que almacena el c&oacute;digo de un pa&iacute;s de residencia.
	 */
	private String codigoPaisResidencia;
	
	/**
	 * Atributo que almacena el c&oacute;digo que identifica a 
	 * una determinada ciudad.
	 */
	
	private String ciudadDeptoPais;
	
	/**
	 * Atributo que almacena el c&oacute;digo de la regi&oacute;n de cobertura.
	 */
	private long codigoRegion;
	
	/**
	 * Atributo que almacena el c&oacute;digo de la empresa-institucion.
	 */
	private long codigoEmpresaInstitucion;
	
	/**
	 * Atributo que almacena el primer valor de la llave
	 * compuesta que identifica una ciudad.
	 */
	private String codigoCiudad;
	
	/**
	 * Atributo que almacena el segundo valor de la llave
	 * compuesta que identifica una ciudad.
	 */
	private String codigoPais;
	
	/**
	 * Atributo que almacena el tercer valor de la llave
	 * compuesta que identifica una ciudad.
	 */
	private String codigoDpto;
	
	/**
	 * Atributo que almacena el c&oacute;digo del centro de 
	 * atenci&oacute;n.
	 */
	private Integer consecutivoCentroAtencion;
	
	/**
	 * Atributo que almacena la raz&oacute;n social
	 * de la instituci&oacute;n.
	 */
	private String razonSocial;
	
	/**
	 * Atributo que almacena la ruta del logo a mostrar en el reporte.
	 */
	private String rutaLogo;
	
	/**
	 * Atributo que almacena el login de
	 * usuario.
	 */
	private String loginUsuario;
	
	/**
	 * Almacena el nombre del archivo generado para luego ser visualizado
	 */
	private String nombreArchivoGenerado;
	/**
	 * Atributo que indica el tipo de salida de impresi&oacute;n 
	 * del reporte generado.
	 */
	private String tipoSalida;
	/**
	 * enumeración del tipo de salida.
	 */
	private EnumTiposSalida enumTipoSalida;	
	
	/**
	 * Atributo que almacena la ubicacion del logo
	 */
	private String ubicacionLogo;
	
	/**
	 * Atributo que almacena el nombre de
	 * usuario que proceso.
	 */
	private String nombreUsuarioProceso;
	
	/**
	 * Atributo que almacena el codigo de
	 * la institución.
	 */
	private long codigoInstitucion;
	
	/**
	 * Atributo que almacena el codigo del
	 * concepto.
	 */
	private String codigoConcepto;
	
	/**
	 * Atributo que almacena el codigo de
	 * estado de la factura varia.
	 */
	private String codigoestadoFacturaVaria;
	
	/**
	 * Atributo que almacena el nombre del
	 * estado de la factura varia.
	 */
	private String nombreEstadoFacturaVaria;
	
	/**
	 * Atributo que almacena la fecha inicial en el 
	 * formato requerido.
	 */
	private String fechaInicialFormateada;
	
	/**
	 * Atributo que almacena la fecha final en el 
	 * formato requerido.
	 */
	private String fechaFinalFormateada;
	
	/**
	 * Atributo que almacena el nombre del
	 * usuario por el cual se generó el reporte.
	 */
	private String usuario;
	 
	/** Objeto jasper para el subreporte de cambio Promocion */
	private JRDataSource dsResultadoCentroAten;
	/****************************************
     * ATRIBUTOS PARA EL PAGER
     ****************************************/

	/**
     * Para controlar la página actual del pager.
     */
    private int offset;
    
    /**
     * Para controlar el link siguiente del pager 
     */
    private String linkSiguiente;
    
    /**
     * Atributo para el manejo de la paginacion con memoria 
     */
    private int currentPageNumber;
    
    
    
    /****************************************
     * FIN ATRIBUTOS PARA EL PAGER
     ****************************************/
    	
	/**
	 * Metodo reset
	 */
	public void reset()
	{
		this.fechaInicial = "";
    	this.fechaFinal = "";
    	this.tipoDeudor = "";
    	this.deudores = new ArrayList<HashMap<String,Object>>();
    	this.codigoDeudor = "";
    	this.descripDeudor = "";
    	this.numeroFactura = "";
    	this.consultaMovimientoFactura = new HashMap();
    	this.consultaMovimientoFactura.put("numRegistros", "0");
    	this.consultaDetalleMovimientoFactura = new HashMap();
    	this.consultaDetalleMovimientoFactura.put("numRegistros", "0");
    	this.consultaDetalleAjustesFactura = new HashMap();
    	this.consultaDetalleAjustesFactura.put("numRegistros", "0");
    	this.consultaDetallePagosFactura = new HashMap();
    	this.consultaDetallePagosFactura.put("numRegistros", "0");
    	this.consultaDetalleResumenFactura = new HashMap();
    	this.consultaDetalleResumenFactura.put("numRegistros", "0");
    	this.posicion = ConstantesBD.codigoNuncaValido;
    	//PARA EL MANEJO DEL PAGER
    	this.currentPageNumber = 1;
        this.linkSiguiente = "";
        this.offset = 0;
        this.patronOrdenar = "";
    	this.ultimoPatron = "";
    	this.path="";
		this.listaPaises = new ArrayList<Paises>();
		this.listaCiudades = new ArrayList<Ciudades>();
		this.listaRegiones = new ArrayList<RegionesCobertura>();
		this.listaEmpresaInstitucion = new ArrayList<EmpresasInstitucion>();
		this.listaCentrosAtencion = new ArrayList<DtoCentrosAtencion>();
		this.listaConceptos = new ArrayList<DtoConceptoFacturaVaria>();
		this.listaEstados = new ArrayList<DtoIntegridadDominio>();
		this.usuarios = new ArrayList<DtoUsuarioPersona>();
		this.setDeshabilitaCiudad(false);
		this.deshabilitaRegion=false;
		this.setEsMultiempresa("");
		this.rutaLogo = "";
		this.codigoPaisResidencia="";
		this.ciudadDeptoPais="";
		this.codigoRegion=ConstantesBD.codigoNuncaValidoLong;
		this.codigoCiudad="";
		this.codigoPais="";
		this.codigoDpto="";
		this.codigoEmpresaInstitucion=ConstantesBD.codigoNuncaValidoLong;
		this.codigoestadoFacturaVaria="";
		this.codigoConcepto="";
		this.loginUsuario="";
		this.consecutivoCentroAtencion=ConstantesBD.codigoNuncaValido;
		this.razonSocial="";
		this.rutaLogo = "";
		this.ubicacionLogo = "";
		this.codigoInstitucion=ConstantesBD.codigoNuncaValidoLong;
		this.tipoSalida="";
		this.enumTipoSalida = null;
		this.nombreArchivoGenerado="";
		
	}
	
	/**
	 * Metodo para resetear el mapa y la variable posición
	 */
	public void resetDatosVolver()
	{
		this.consultaMovimientoFactura = new HashMap();
    	this.consultaMovimientoFactura.put("numRegistros", "0");
    	this.consultaDetalleMovimientoFactura = new HashMap();
    	this.consultaDetalleMovimientoFactura.put("numRegistros", "0");
    	this.consultaDetalleAjustesFactura = new HashMap();
    	this.consultaDetalleAjustesFactura.put("numRegistros", "0");
    	this.consultaDetallePagosFactura = new HashMap();
    	this.consultaDetallePagosFactura.put("numRegistros", "0");
    	this.consultaDetalleResumenFactura = new HashMap();
    	this.consultaDetalleResumenFactura.put("numRegistros", "0");
    	this.posicion = ConstantesBD.codigoNuncaValido;
	}
	
	/**
	* Control de Errores (Validaciones)
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(this.estado.equals("buscar"))
		{
			//Validamos los campos requeridos
			if(this.fechaInicial.trim().equals("") || this.fechaInicial.trim().equals("null"))
				errores.add("fechaInicial", new ActionMessage("errors.required", "La Fecha Inicial "));
			if(this.fechaFinal.trim().equals("") || this.fechaFinal.trim().equals("null"))
				errores.add("fechaFinal", new ActionMessage("errors.required", "La Fecha Final "));
			
			//Validamos si el campo Tipo Deudor viene con información para hacer requerido el campo Deudor
			if(!UtilidadTexto.isEmpty(this.tipoDeudor.toString()))
				if(this.codigoDeudor.trim().equals("") || this.fechaInicial.trim().equals("null"))
					errores.add("codigoDeudor", new ActionMessage("errors.required", "El Deudor "));
				
			//Validaciones de las fechas si no vienen vacias
			if(!UtilidadTexto.isEmpty(this.fechaInicial.toString()) || !UtilidadTexto.isEmpty(this.fechaFinal.toString()))
			{
				boolean centinelaErrorFechas = false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaInicial.toString()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.fechaInicial));
					centinelaErrorFechas = true;
				}
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaFinal.toString()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.fechaFinal));
					centinelaErrorFechas=true;
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial.toString(), this.fechaFinal.toString()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.fechaInicial, "Final "+this.fechaFinal));
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal.toString(), UtilidadFecha.getFechaActual().toString()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.fechaFinal, "Actual "+UtilidadFecha.getFechaActual()));
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial.toString(), UtilidadFecha.getFechaActual().toString()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.fechaInicial, "Actual "+UtilidadFecha.getFechaActual()));
					if(UtilidadFecha.numeroMesesEntreFechasExacta(this.fechaInicial, this.fechaFinal) >= 6)
						errores.add("", new ActionMessage("error.facturacion.maximoRangoFechas", "para Consultar Movimientos por Factura", "6", "180"));
				}
			}
			
			if (UtilidadTexto.isEmpty(this.codigoPaisResidencia)
					|| this.codigoPaisResidencia.trim().equals("-1")) {
				
				errores.add("PAÍS RESIDENCIA ES REQUERIDO.", new ActionMessage(
						 "errors.paisResidenciaRequerido"));
			}
			
		}
		return errores;
	}

	
	public String getDescripDeudor() {
		return descripDeudor;
	}

	public void setDescripDeudor(String descripDeudor) {
		this.descripDeudor = descripDeudor;
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
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return the tipoDeudor
	 */
	public String getTipoDeudor() {
		return tipoDeudor;
	}

	/**
	 * @param tipoDeudor the tipoDeudor to set
	 */
	public void setTipoDeudor(String tipoDeudor) {
		this.tipoDeudor = tipoDeudor;
	}

	/**
	 * @return the deudores
	 */
	public ArrayList<HashMap<String, Object>> getDeudores() {
		return deudores;
	}

	/**
	 * @param deudores the deudores to set
	 */
	public void setDeudores(ArrayList<HashMap<String, Object>> deudores) {
		this.deudores = deudores;
	}

	/**
	 * @return the codigoDeudor
	 */
	public String getCodigoDeudor() {
		return codigoDeudor;
	}

	/**
	 * @param codigoDeudor the codigoDeudor to set
	 */
	public void setCodigoDeudor(String codigoDeudor) {
		this.codigoDeudor = codigoDeudor;
	}

	/**
	 * @return the numeroFactura
	 */
	public String getNumeroFactura() {
		return numeroFactura;
	}

	/**
	 * @param numeroFactura the numeroFactura to set
	 */
	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	/**
	 * @return the consultaMovimientoFactura
	 */
	public HashMap getConsultaMovimientoFactura() {
		return consultaMovimientoFactura;
	}

	/**
	 * @param consultaMovimientoFactura the consultaMovimientoFactura to set
	 */
	public void setConsultaMovimientoFactura(HashMap consultaMovimientoFactura) {
		this.consultaMovimientoFactura = consultaMovimientoFactura;
	}

	/**
	 * @param key
	 * @return consultaMovimientoFactura
	 */	
	public Object getConsultaMovimientoFactura(String key){
		return consultaMovimientoFactura.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setConsultaMovimientoFactura(String key, Object value){
		this.consultaMovimientoFactura.put(key, value);
	}
	
	/**
	 * @return the consultaDetalleAjustesFactura
	 */
	public HashMap getConsultaDetalleAjustesFactura() {
		return consultaDetalleAjustesFactura;
	}

	/**
	 * @param consultaDetalleAjustesFactura the consultaDetalleAjustesFactura to set
	 */
	public void setConsultaDetalleAjustesFactura(HashMap consultaDetalleAjustesFactura) {
		this.consultaDetalleAjustesFactura = consultaDetalleAjustesFactura;
	}

	/**
	 * @param key
	 * @return consultaDetalleAjustesFactura
	 */	
	public Object getConsultaDetalleAjustesFactura(String key){
		return consultaDetalleAjustesFactura.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setConsultaDetalleAjustesFactura(String key, Object value){
		this.consultaDetalleAjustesFactura.put(key, value);
	}
	
	/**
	 * @return the consultaDetallePagosFactura
	 */
	public HashMap getConsultaDetallePagosFactura() {
		return consultaDetallePagosFactura;
	}

	/**
	 * @param consultaDetallePagosFactura the consultaDetallePagosFactura to set
	 */
	public void setConsultaDetallePagosFactura(HashMap consultaDetallePagosFactura) {
		this.consultaDetallePagosFactura = consultaDetallePagosFactura;
	}

	/**
	 * @param key
	 * @return consultaDetallePagosFactura
	 */	
	public Object getConsultaDetallePagosFactura(String key){
		return consultaDetallePagosFactura.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setConsultaDetallePagosFactura(String key, Object value){
		this.consultaDetallePagosFactura.put(key, value);
	}
	
	/**
	 * @return the consultaDetalleMovimientoFactura
	 */
	public HashMap getConsultaDetalleMovimientoFactura() {
		return consultaDetalleMovimientoFactura;
	}

	/**
	 * @param consultaDetalleMovimientoFactura the consultaDetalleMovimientoFactura to set
	 */
	public void setConsultaDetalleMovimientoFactura(HashMap consultaDetalleMovimientoFactura) {
		this.consultaDetalleMovimientoFactura = consultaDetalleMovimientoFactura;
	}

	/**
	 * @param key
	 * @return consultaDetalleMovimientoFactura
	 */	
	public Object getConsultaDetalleMovimientoFactura(String key){
		return consultaDetalleMovimientoFactura.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setConsultaDetalleMovimientoFactura(String key, Object value){
		this.consultaDetalleMovimientoFactura.put(key, value);
	}
	
	/**
	 * @return the consultaDetalleResumenFactura
	 */
	public HashMap getConsultaDetalleResumenFactura() {
		return consultaDetalleResumenFactura;
	}

	/**
	 * @param consultaDetalleResumenFactura the consultaDetalleResumenFactura to set
	 */
	public void setConsultaDetalleResumenFactura(HashMap consultaDetalleResumenFactura) {
		this.consultaDetalleResumenFactura = consultaDetalleResumenFactura;
	}

	/**
	 * @param key
	 * @return consultaDetalleResumenFactura
	 */	
	public Object getConsultaDetalleResumenFactura(String key){
		return consultaDetalleResumenFactura.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setConsultaDetalleResumenFactura(String key, Object value){
		this.consultaDetalleResumenFactura.put(key, value);
	}
	
	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
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
	 * @return the currentPageNumber
	 */
	public int getCurrentPageNumber() {
		return currentPageNumber;
	}

	/**
	 * @param currentPageNumber the currentPageNumber to set
	 */
	public void setCurrentPageNumber(int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
	
	public ArrayList<Paises> getListaPaises() {
		return listaPaises;
	}

	public void setListaPaises(ArrayList<Paises> listaPaises) {
		this.listaPaises = listaPaises;
	}

	public ArrayList<Ciudades> getListaCiudades() {
		return listaCiudades;
	}

	public void setListaCiudades(ArrayList<Ciudades> listaCiudades) {
		this.listaCiudades = listaCiudades;
	}

	public ArrayList<RegionesCobertura> getListaRegiones() {
		return listaRegiones;
	}

	public void setListaRegiones(ArrayList<RegionesCobertura> listaRegiones) {
		this.listaRegiones = listaRegiones;
	}

	public ArrayList<EmpresasInstitucion> getListaEmpresaInstitucion() {
		return listaEmpresaInstitucion;
	}

	public void setListaEmpresaInstitucion(
			ArrayList<EmpresasInstitucion> listaEmpresaInstitucion) {
		this.listaEmpresaInstitucion = listaEmpresaInstitucion;
	}

	public ArrayList<DtoCentrosAtencion> getListaCentrosAtencion() {
		return listaCentrosAtencion;
	}

	public void setListaCentrosAtencion(
			ArrayList<DtoCentrosAtencion> listaCentrosAtencion) {
		this.listaCentrosAtencion = listaCentrosAtencion;
	}

	public boolean isDeshabilitaCiudad() {
		return deshabilitaCiudad;
	}

	public void setDeshabilitaCiudad(boolean deshabilitaCiudad) {
		this.deshabilitaCiudad = deshabilitaCiudad;
	}

	public boolean isDeshabilitaRegion() {
		return deshabilitaRegion;
	}

	public void setDeshabilitaRegion(boolean deshabilitaRegion) {
		this.deshabilitaRegion = deshabilitaRegion;
	}

	public String getCodigoPaisResidencia() {
		return codigoPaisResidencia;
	}

	public void setCodigoPaisResidencia(String codigoPaisResidencia) {
		this.codigoPaisResidencia = codigoPaisResidencia;
	}

	public String getCiudadDeptoPais() {
		return ciudadDeptoPais;
	}

	public void setCiudadDeptoPais(String ciudadDeptoPais) {
		this.ciudadDeptoPais = ciudadDeptoPais;
	}

	public long getCodigoRegion() {
		return codigoRegion;
	}

	public void setCodigoRegion(long codigoRegion) {
		this.codigoRegion = codigoRegion;
	}

	
	

	public String getCodigoCiudad() {
		return codigoCiudad;
	}

	public void setCodigoCiudad(String codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}

	public String getCodigoPais() {
		return codigoPais;
	}

	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	public String getCodigoDpto() {
		return codigoDpto;
	}

	public void setCodigoDpto(String codigoDpto) {
		this.codigoDpto = codigoDpto;
	}

	public Integer getConsecutivoCentroAtencion() {
		return consecutivoCentroAtencion;
	}

	public void setConsecutivoCentroAtencion(Integer consecutivoCentroAtencion) {
		this.consecutivoCentroAtencion = consecutivoCentroAtencion;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getRutaLogo() {
		return rutaLogo;
	}

	public void setRutaLogo(String rutaLogo) {
		this.rutaLogo = rutaLogo;
	}

	public String getUbicacionLogo() {
		return ubicacionLogo;
	}

	public void setUbicacionLogo(String ubicacionLogo) {
		this.ubicacionLogo = ubicacionLogo;
	}

	public String getNombreUsuarioProceso() {
		return nombreUsuarioProceso;
	}

	public void setNombreUsuarioProceso(String nombreUsuarioProceso) {
		this.nombreUsuarioProceso = nombreUsuarioProceso;
	}

	public long getCodigoInstitucion() {
		return codigoInstitucion;
	}

	public void setCodigoInstitucion(long codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	public void setListaConceptos(ArrayList<DtoConceptoFacturaVaria> listaConceptos) {
		this.listaConceptos = listaConceptos;
	}

	public ArrayList<DtoConceptoFacturaVaria> getListaConceptos() {
		return listaConceptos;
	}

	public void setCodigoConcepto(String codigoConcepto) {
		this.codigoConcepto = codigoConcepto;
	}

	public String getCodigoConcepto() {
		return codigoConcepto;
	}

	public void setListaEstados(ArrayList<DtoIntegridadDominio> listaEstados) {
		this.listaEstados = listaEstados;
	}

	public ArrayList<DtoIntegridadDominio> getListaEstados() {
		return listaEstados;
	}
	
	public void setNombreEstadoFacturaVaria(String nombreEstadoFacturaVaria) {
		this.nombreEstadoFacturaVaria = nombreEstadoFacturaVaria;
	}

	public String getNombreEstadoFacturaVaria() {
		return nombreEstadoFacturaVaria;
	}

	public void setCodigoestadoFacturaVaria(String codigoestadoFacturaVaria) {
		this.codigoestadoFacturaVaria = codigoestadoFacturaVaria;
	}

	public String getCodigoestadoFacturaVaria() {
		return codigoestadoFacturaVaria;
	}

	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	public String getLoginUsuario() {
		return loginUsuario;
	}

	public void setUsuarios(ArrayList<DtoUsuarioPersona> usuarios) {
		this.usuarios = usuarios;
	}

	public ArrayList<DtoUsuarioPersona> getUsuarios() {
		return usuarios;
	}

	public void setCodigoEmpresaInstitucion(long codigoEmpresaInstitucion) {
		this.codigoEmpresaInstitucion = codigoEmpresaInstitucion;
	}

	public long getCodigoEmpresaInstitucion() {
		return codigoEmpresaInstitucion;
	}

	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}

	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}

	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	public String getTipoSalida() {
		return tipoSalida;
	}

	public void setDsResultadoCentroAten(JRDataSource dsResultadoCentroAten) {
		this.dsResultadoCentroAten = dsResultadoCentroAten;
	}

	public JRDataSource getDsResultadoCentroAten() {
		return dsResultadoCentroAten;
	}

	public void setFechaInicialFormateada(String fechaInicialFormateada) {
		this.fechaInicialFormateada = fechaInicialFormateada;
	}

	public String getFechaInicialFormateada() {
		fechaInicialFormateada = UtilidadFecha.conversionFormatoFechaAAp(fechaInicial);
		return fechaInicialFormateada;
	}

	public void setFechaFinalFormateada(String fechaFinalFormateada) {
		this.fechaFinalFormateada = fechaFinalFormateada;
	}

	public String getFechaFinalFormateada() {
		fechaFinalFormateada = UtilidadFecha.conversionFormatoFechaAAp(fechaFinal);
		return fechaFinalFormateada;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuario() {
		if (UtilidadTexto.isEmpty(loginUsuario) || 
				loginUsuario.trim().equals("-1") ) {
			
			usuario = "Todos";
		}else{
			usuario=loginUsuario;
		}
		return usuario;
	}

	public void setEnumTipoSalida(EnumTiposSalida enumTipoSalida) {
		this.enumTipoSalida = enumTipoSalida;
	}

	public EnumTiposSalida getEnumTipoSalida() {
		return enumTipoSalida;
	}

	public void setEsMultiempresa(String esMultiempresa) {
		this.esMultiempresa = esMultiempresa;
	}

	public String getEsMultiempresa() {
		return esMultiempresa;
	}

	
}