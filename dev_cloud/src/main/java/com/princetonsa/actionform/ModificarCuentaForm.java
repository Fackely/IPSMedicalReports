/*
 * Created on 03-may-2004
 *
 */
package com.princetonsa.actionform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosString;

/**
 * @author juanda
 *
 */
public class ModificarCuentaForm extends ValidatorForm
{
	
	/**
	 * Forma para almacenar todos los datos del traslado de camas
	 */
	private TrasladoCamasForm trasladoCamasForm;
	//***************** ATRIBUTOS GENERALES *****************************************************************
	/**
	 * Estado
	 */
	private String estado="";
	
	/**
	 * Código de la cuenta que se está modificando
	 */
	private int codigoCuenta=0;
	
	//**************ATRIBUTOS PARA EL FLUJO DE CUENTAS ASOCIADAS**********************************************
	private ArrayList<HashMap<String, Object>> cuentasAsocio = new ArrayList<HashMap<String,Object>>(); 
	
	//**************ATRIBUTOS PARA EL FLUJO DE LA MODIFICACION DE LA CUENTA*******************************+
	private HashMap<String, Object> cuenta = new HashMap<String, Object>();
	private HashMap<String, Object> variosConvenios = new HashMap<String, Object>(); //mapa donde se almacena la informacion de varios convenios
	private HashMap<String, Object> verificacion = new HashMap<String, Object>(); //mapa donde se guara la verificacion del convenio ppal
	private HashMap<String, Object> responsable = new HashMap<String, Object>(); //mapa donde se guarda la informacion del responsable paciente
	private HashMap<String, Object> cama = new HashMap<String, Object>(); //mapa donde se guarda la información de la cama
	private String centroAtencion;
	
	//Estructuras de datos
	private ArrayList<HashMap<String, Object>> convenios = new ArrayList<HashMap<String,Object>>();
	private HashMap conveniosArp = new HashMap();
	private ArrayList<HashMap<String, Object>> origenesAdmisiones = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> contratos = new ArrayList<HashMap<String,Object>>();
	private HashMap estratosSociales = new HashMap();
	private HashMap tiposAfiliado =  new HashMap();
	private ArrayList<HashMap<String, Object>> montosCobro = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> tiposPaciente = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> coberturasSalud = new ArrayList<HashMap<String,Object>>();
	private HashMap areas = new HashMap();
	private Vector<InfoDatosString> tiposComplejidad = new Vector<InfoDatosString>();
	private Vector<InfoDatosString> naturalezasPaciente = new Vector<InfoDatosString>();
	private ArrayList<HashMap<String, Object>> tiposIdentificacion = new ArrayList<HashMap<String,Object>>(); //se usa para la poliza
	private ArrayList<HashMap<String, Object>> tiposIdResponsable = new ArrayList<HashMap<String,Object>>(); //se usa para el resp. paciente
	private ArrayList<HashMap<String, Object>> paises = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> ciudades = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> ciudadesExp = new ArrayList<HashMap<String,Object>>();
	private HashMap tiposMonitoreo = new HashMap();
	
	
	//Variables para control y validaciones
	private boolean tieneRegistroAccidenteTransito;
	private boolean tieneRegistroAccidenteTransitoAnulado;
	private boolean permisosAccidenteTransito;
	private boolean mostrarVentanaAccidenteTransito = false;

	private Long codigoRegistroAccidenteTransito;

	private boolean registroInconsistencias;
	
	private boolean tieneRegistroEventoCatastrofico;
	private boolean tieneRegistroEventoCatastroficoAnulado;
	private boolean permisosEventoCatastrofico;
	private boolean mostrarVentanaEventoCatastrofico = false;
	
	private boolean puedoModificarOrigenAdmision;
	private boolean deboAbrirReferencia = false;
	private String pathReferencia = "";
	private int puedoModificarCama;
	
	private boolean puedoModificarArea;
	
	//Variables temporales para filtros Ajax
	private String codigoConvenio="";
	private String codigoContrato = "";
	private String codigoEstratoSocial = "";
	private String codigoTipoAfiliado = "";
	private String fechaAfiliacion = "";
	private String codigoTipoIdResponsable = "";
	private String numeroIdResponsable = "";
	private String codigoPaisId = "";
	private String codigoPaisResidencia = "";
	private String codigoArea = "";
	private int posicion = 0;
	
	//Atributos para el manejo de mensajes
	ArrayList<String> mensajes = new ArrayList<String>();
	
	//Atributos para el manejo de la impresion
	private String codigoSubCuentaImpresion;
	
	private boolean fueModificadoResponsable; //variable que indica si fue modificado el responsable
	private boolean fueEliminadoResponsable; //variable que indica si fue eliminado el responsable de la cuenta
	//*********************************************************************************************************************
	//*********************ATRIBUTOS PARA EL MANEJO DE LA MODIFICACION DE CONVENIOS (ADICIONALES)***************************
	private int posConvenio ; //lleva la posicion del convenio seleccionado
	//***********************************************************************************************************************
	//********************ATRIBUTOS QUE TIENEN QUE VER CON PRESUPUESTO VENEZUELA***************
	private boolean presupuestoPaciente;
	private HashMap<String, Object> presupuestos = new HashMap<String, Object>();
	private int posPresupuesto;
	//*****************************************************************************************
	
		// Manejo de Mensajes de Advertencia de sin Contrato y Controla Anticipos
    private Boolean controlMensaje;
    
    private boolean asignarCama;
	
	
	
	/**
	 * Método que limpia los atributos de esta 
	 * clase
	 */
	public void reset()
	{
		//atributos generales
		this.codigoCuenta=0;
		this.estado="";
		
		//atributos para el flujo de cuentas asociadas
		this.cuentasAsocio = new ArrayList<HashMap<String,Object>>();
		
		//atributos para el flujo de la modificacion de la cuenta
		this.cuenta = new HashMap<String, Object>();
		this.variosConvenios = new HashMap<String, Object>();
		this.verificacion = new HashMap<String, Object>();
		this.responsable = new HashMap<String, Object>();
		this.cama = new HashMap<String, Object>();
		this.centroAtencion="";
		
		///Estructuras de datos
		this.convenios = new ArrayList<HashMap<String,Object>>();
		this.conveniosArp = new HashMap();
		this.origenesAdmisiones = new ArrayList<HashMap<String,Object>>();
		this.contratos = new ArrayList<HashMap<String,Object>>();
		this.estratosSociales = new HashMap();
		this.tiposAfiliado=new HashMap();
		this.montosCobro = new ArrayList<HashMap<String,Object>>();
		this.tiposPaciente = new ArrayList<HashMap<String,Object>>();
		this.coberturasSalud = new ArrayList<HashMap<String,Object>>();
		this.areas = new HashMap();
		this.tiposComplejidad = new Vector<InfoDatosString>();
		this.naturalezasPaciente = new Vector<InfoDatosString>();
		this.tiposIdentificacion = new ArrayList<HashMap<String,Object>>(); //se usa para la poliza
		this.tiposIdResponsable = new ArrayList<HashMap<String,Object>>(); //se usa para el resp. paciente
		this.paises = new ArrayList<HashMap<String,Object>>();
		this.ciudades = new ArrayList<HashMap<String,Object>>();
		this.ciudadesExp = new ArrayList<HashMap<String,Object>>();
		this.tiposMonitoreo = new HashMap();
	
		//Variables para control y validaciones
		this.tieneRegistroAccidenteTransito = false;
		this.tieneRegistroAccidenteTransitoAnulado = false;
		this.permisosAccidenteTransito = false;
		
		this.tieneRegistroEventoCatastrofico = false;
		this.tieneRegistroEventoCatastroficoAnulado = false;
		this.puedoModificarOrigenAdmision = false;
		
		this.permisosEventoCatastrofico = false;
		this.puedoModificarArea = false;
		
		this.puedoModificarCama=ConstantesBD.codigoNuncaValido;
		
		this.registroInconsistencias=false;
		
		//atributos para el manejo de mensajes
		this.mensajes = new ArrayList<String>();
		
		//Atributos para el manejo de la impresion
		this.codigoSubCuentaImpresion = "";
		
		this.fueModificadoResponsable = false; //variable que indica si fue modificado el responsable
		this.fueEliminadoResponsable = false; //variable que indica si fue eliminado el responsable de la cuenta
		
		//ATRIBUTOS PARA LA MODIFICACION DE CONVENIOS
		this.posConvenio = 0;
		
		//********************ATRIBUTOS QUE TIENEN QUE VER CON PRESUPUESTO VENEZUELA***************
		this.presupuestoPaciente = false;
		this.presupuestos = new HashMap<String, Object>();
		this.posPresupuesto = 0;
	//*****************************************************************************************
		this.controlMensaje = false;
		
		this.asignarCama = false;
		this.setCodigoEstratoSocial("");
		this.setCodigoTipoAfiliado("");
		
		this.trasladoCamasForm=null;
		this.codigoRegistroAccidenteTransito=null;
	}
	
	
	/**
	 * @return estado de la cuenta
	 */
	public String getEstado()
	{
		return estado;
	}


	/**
	 * @param string
	 * Método para definir el estado de la forma
	 */
	public void setEstado(String estado)
	{
		this.estado=estado;
	}

	

	/**
	 * @return
	 */
	public int getCodigoCuenta()
	{
		return codigoCuenta;
	}

	

    /**
     * @return Retorna el/la codigoConvenio.
     */
    public String getCodigoConvenio() {
        return codigoConvenio;
    }
    /**
     * El/La codigoConvenio a establecer.
     * @param codigoConvenio 
     */
    public void setCodigoConvenio(String codigoConvenio) {
        this.codigoConvenio = codigoConvenio;
    }
   
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
		// Perform validator framework validations
		ActionErrors errors = super.validate(mapping, request);
		
		return errors;
	}




	




	/**
	 * @return Returns the puedoModificarArea.
	 */
	public boolean isPuedoModificarArea() {
		return puedoModificarArea;
	}




	/**
	 * @param puedoModificarArea The puedoModificarArea to set.
	 */
	public void setPuedoModificarArea(boolean puedoModificarArea) {
		this.puedoModificarArea = puedoModificarArea;
	}





	/**
	 * @return the mostrarVentanaAccidenteTransito
	 */
	public boolean isMostrarVentanaAccidenteTransito() {
		return mostrarVentanaAccidenteTransito;
	}




	/**
	 * @param mostrarVentanaAccidenteTransito the mostrarVentanaAccidenteTransito to set
	 */
	public void setMostrarVentanaAccidenteTransito(
			boolean mostrarVentanaAccidenteTransito) {
		this.mostrarVentanaAccidenteTransito = mostrarVentanaAccidenteTransito;
	}




	/**
	 * @return the mostrarVentanaEventoCatastrofico
	 */
	public boolean isMostrarVentanaEventoCatastrofico() {
		return mostrarVentanaEventoCatastrofico;
	}




	/**
	 * @param mostrarVentanaEventoCatastrofico the mostrarVentanaEventoCatastrofico to set
	 */
	public void setMostrarVentanaEventoCatastrofico(
			boolean mostrarVentanaEventoCatastrofico) {
		this.mostrarVentanaEventoCatastrofico = mostrarVentanaEventoCatastrofico;
	}




	/**
	 * @return the tieneRegistroAccidenteTransito
	 */
	public boolean isTieneRegistroAccidenteTransito() {
		return tieneRegistroAccidenteTransito;
	}




	/**
	 * @param tieneRegistroAccidenteTransito the tieneRegistroAccidenteTransito to set
	 */
	public void setTieneRegistroAccidenteTransito(
			boolean tieneRegistroAccidenteTransito) {
		this.tieneRegistroAccidenteTransito = tieneRegistroAccidenteTransito;
	}




	/**
	 * @return the tieneRegistroAccidenteTransitoAnulado
	 */
	public boolean isTieneRegistroAccidenteTransitoAnulado() {
		return tieneRegistroAccidenteTransitoAnulado;
	}




	/**
	 * @param tieneRegistroAccidenteTransitoAnulado the tieneRegistroAccidenteTransitoAnulado to set
	 */
	public void setTieneRegistroAccidenteTransitoAnulado(
			boolean tieneRegistroAccidenteTransitoAnulado) {
		this.tieneRegistroAccidenteTransitoAnulado = tieneRegistroAccidenteTransitoAnulado;
	}




	/**
	 * @return the tieneRegistroEventoCatastrofico
	 */
	public boolean isTieneRegistroEventoCatastrofico() {
		return tieneRegistroEventoCatastrofico;
	}




	/**
	 * @param tieneRegistroEventoCatastrofico the tieneRegistroEventoCatastrofico to set
	 */
	public void setTieneRegistroEventoCatastrofico(
			boolean tieneRegistroEventoCatastrofico) {
		this.tieneRegistroEventoCatastrofico = tieneRegistroEventoCatastrofico;
	}




	/**
	 * @return the tieneRegistroEventoCatastroficoAnulado
	 */
	public boolean isTieneRegistroEventoCatastroficoAnulado() {
		return tieneRegistroEventoCatastroficoAnulado;
	}




	/**
	 * @param tieneRegistroEventoCatastroficoAnulado the tieneRegistroEventoCatastroficoAnulado to set
	 */
	public void setTieneRegistroEventoCatastroficoAnulado(
			boolean tieneRegistroEventoCatastroficoAnulado) {
		this.tieneRegistroEventoCatastroficoAnulado = tieneRegistroEventoCatastroficoAnulado;
	}





	/**
	 * @return the conveniosArp
	 */
	public HashMap getConveniosArp() {
		return conveniosArp;
	}

	/**
	 * @param conveniosArp the conveniosArp to set
	 */
	public void setConveniosArp(HashMap conveniosArp) {
		this.conveniosArp = conveniosArp;
	}
	
	/**
	 * @return retorna elemento del mapa conveniosArp
	 */
	public Object getConveniosArp(String key) {
		return conveniosArp.get(key);
	}

	/**
	 * @param Asigna elemento al mapa conveniosArp 
	 */
	public void setConveniosArp(String key,Object obj) {
		this.conveniosArp.put(key, obj);
	}








	/**
	 * @return the deboAbrirReferencia
	 */
	public boolean isDeboAbrirReferencia() {
		return deboAbrirReferencia;
	}




	/**
	 * @param deboAbrirReferencia the deboAbrirReferencia to set
	 */
	public void setDeboAbrirReferencia(boolean deboAbrirReferencia) {
		this.deboAbrirReferencia = deboAbrirReferencia;
	}




	/**
	 * @return the origenesAdmisiones
	 */
	public ArrayList<HashMap<String, Object>> getOrigenesAdmisiones() {
		return origenesAdmisiones;
	}




	/**
	 * @param origenesAdmisiones the origenesAdmisiones to set
	 */
	public void setOrigenesAdmisiones(
			ArrayList<HashMap<String, Object>> origenesAdmisiones) {
		this.origenesAdmisiones = origenesAdmisiones;
	}




	/**
	 * @return the puedoModificarOrigenAdmision
	 */
	public boolean isPuedoModificarOrigenAdmision() {
		return puedoModificarOrigenAdmision;
	}




	/**
	 * @param puedoModificarOrigenAdmision the puedoModificarOrigenAdmision to set
	 */
	public void setPuedoModificarOrigenAdmision(boolean puedoModificarOrigenAdmision) {
		this.puedoModificarOrigenAdmision = puedoModificarOrigenAdmision;
	}




	/**
	 * @return the cuentasAsocio
	 */
	public ArrayList<HashMap<String, Object>> getCuentasAsocio() {
		return cuentasAsocio;
	}




	/**
	 * @param cuentasAsocio the cuentasAsocio to set
	 */
	public void setCuentasAsocio(ArrayList<HashMap<String, Object>> cuentasAsocio) {
		this.cuentasAsocio = cuentasAsocio;
	}

	/**
	 * @return the cuenta
	 */
	public HashMap<String, Object> getCuenta() {
		return cuenta;
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(HashMap<String, Object> cuenta) {
		this.cuenta = cuenta;
	}
	
	/**
	 * @return elemento de la cuenta
	 */
	public Object getCuenta(String key) {
		return cuenta.get(key);
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(String key,Object obj) {
		this.cuenta.put(key, obj);
	}
	/**
	 * @return the variosConvenios
	 */
	public HashMap<String, Object> getVariosConvenios() {
		return variosConvenios;
	}

	/**
	 * @param variosConvenios the variosConvenios to set
	 */
	public void setVariosConvenios(HashMap<String, Object> variosConvenios) {
		this.variosConvenios = variosConvenios;
	}
	
	
	/**
	 * @return elemento variosConvenios
	 */
	public Object getVariosConvenios(String key) {
		return variosConvenios.get(key);
	}

	/**
	 * @param Asigna elemento a variosConvenios 
	 */
	public void setVariosConvenios(String key,Object obj) {
		this.variosConvenios.put(key,obj);
	}


	/**
	 * @return the verificacion
	 */
	public HashMap<String, Object> getVerificacion() {
		return verificacion;
	}
	/**
	 * @param verificacion the verificacion to set
	 */
	public void setVerificacion(HashMap<String, Object> verificacion) {
		this.verificacion = verificacion;
	}
	/**
	 * @return elemento del mapa  verificacion
	 */
	public Object getVerificacion(String key) {
		return verificacion.get(key);
	}
	/**
	 * @param Asigna elemento al mapa verificacion 
	 */
	public void setVerificacion(String key,Object obj) {
		this.verificacion.put(key,obj);
	}

	/**
	 * @return the responsable
	 */
	public HashMap<String, Object> getResponsable() {
		return responsable;
	}

	/**
	 * @param responsable the responsable to set
	 */
	public void setResponsable(HashMap<String, Object> responsable) {
		this.responsable = responsable;
	}

	/**
	 * @return the responsable
	 */
	public Object getResponsable(String key) {
		return responsable.get(key);
	}

	/**
	 * @param responsable the responsable to set
	 */
	public void setResponsable(String key,Object obj) {
		this.responsable.put(key,obj);
	}

	/**
	 * @return the contratos
	 */
	public ArrayList<HashMap<String, Object>> getContratos() {
		return contratos;
	}

	/**
	 * @param contratos the contratos to set
	 */
	public void setContratos(ArrayList<HashMap<String, Object>> contratos) {
		this.contratos = contratos;
	}

	/**
	 * @return the estratosSociales
	 */
	public HashMap getEstratosSociales() {
		return estratosSociales;
	}

	/**
	 * @param estratosSociales the estratosSociales to set
	 */
	public void setEstratosSociales(HashMap estratosSociales) {
		this.estratosSociales = estratosSociales;
	}
	
	/**
	 * @return Elemento del mapa estratosSociales
	 */
	public Object getEstratosSociales(String key) {
		return estratosSociales.get(key);
	}

	/**
	 * @param Asigna elemento al mapa estratosSociales 
	 */
	public void setEstratosSociales(String key,Object obj) {
		this.estratosSociales.put(key, obj);
	}

	/**
	 * @return the estratosSociales
	 */
	public HashMap getTiposAfiliado() {
		return tiposAfiliado;
	}

	/**
	 * @param estratosSociales the estratosSociales to set
	 */
	public void setTiposAfiliado(HashMap tiposAfiliado) {
		this.tiposAfiliado = tiposAfiliado;
	}
	
	/**
	 * @return Elemento del mapa estratosSociales
	 */
	public Object getTiposAfiliado(String key) {
		return tiposAfiliado.get(key);
	}

	/**
	 * @param Asigna elemento al mapa estratosSociales 
	 */
	public void setTiposAfiliado(String key,Object obj) {
		this.tiposAfiliado.put(key, obj);
	}
	


	/**
	 * @return the montosCobro
	 */
	public ArrayList<HashMap<String, Object>> getMontosCobro() {
		return montosCobro;
	}




	/**
	 * @param montosCobro the montosCobro to set
	 */
	public void setMontosCobro(ArrayList<HashMap<String, Object>> montosCobro) {
		this.montosCobro = montosCobro;
	}

	/**
	 * @return the tiposPaciente
	 */
	public ArrayList<HashMap<String, Object>> getTiposPaciente() {
		return tiposPaciente;
	}

	/**
	 * @param tiposPaciente the tiposPaciente to set
	 */
	public void setTiposPaciente(ArrayList<HashMap<String, Object>> tiposPaciente) {
		this.tiposPaciente = tiposPaciente;
	}

	/**
	 * @return the areas
	 */
	public HashMap getAreas() {
		return areas;
	}

	/**
	 * @param areas the areas to set
	 */
	public void setAreas(HashMap areas) {
		this.areas = areas;
	}




	/**
	 * @return the mensajes
	 */
	public ArrayList<String> getMensajes() {
		return mensajes;
	}

	/**
	 * @return the mensajes
	 */
	public int getSizeMensajes() {
		return mensajes.size();
	}


	/**
	 * @param mensajes the mensajes to set
	 */
	public void setMensajes(ArrayList<String> mensajes) {
		this.mensajes = mensajes;
	}




	/**
	 * @return the convenios
	 */
	public ArrayList<HashMap<String, Object>> getConvenios() {
		return convenios;
	}




	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList<HashMap<String, Object>> convenios) {
		this.convenios = convenios;
	}




	/**
	 * @return the tiposComplejidad
	 */
	public Vector<InfoDatosString> getTiposComplejidad() {
		return tiposComplejidad;
	}




	/**
	 * @param tiposComplejidad the tiposComplejidad to set
	 */
	public void setTiposComplejidad(Vector<InfoDatosString> tiposComplejidad) {
		this.tiposComplejidad = tiposComplejidad;
	}




	/**
	 * @return the naturalezasPaciente
	 */
	public Vector<InfoDatosString> getNaturalezasPaciente() {
		return naturalezasPaciente;
	}




	/**
	 * @param naturalezasPaciente the naturalezasPaciente to set
	 */
	public void setNaturalezasPaciente(Vector<InfoDatosString> naturalezasPaciente) {
		this.naturalezasPaciente = naturalezasPaciente;
	}




	/**
	 * @return the tiposIdentificacion
	 */
	public ArrayList<HashMap<String, Object>> getTiposIdentificacion() {
		return tiposIdentificacion;
	}

	/**
	 * Retorna el número de contratos del arreglo de contratos
	 * @return
	 */
	public int getNumContratos()
	{
		return this.contratos.size();
	}


	/**
	 * @param tiposIdentificacion the tiposIdentificacion to set
	 */
	public void setTiposIdentificacion(
			ArrayList<HashMap<String, Object>> tiposIdentificacion) {
		this.tiposIdentificacion = tiposIdentificacion;
	}




	/**
	 * @return the permisosAccidenteTransito
	 */
	public boolean isPermisosAccidenteTransito() {
		return permisosAccidenteTransito;
	}




	/**
	 * @param permisosAccidenteTransito the permisosAccidenteTransito to set
	 */
	public void setPermisosAccidenteTransito(boolean permisosAccidenteTransito) {
		this.permisosAccidenteTransito = permisosAccidenteTransito;
	}




	/**
	 * @return the permisosEventoCatastrofico
	 */
	public boolean isPermisosEventoCatastrofico() {
		return permisosEventoCatastrofico;
	}




	/**
	 * @param permisosEventoCatastrofico the permisosEventoCatastrofico to set
	 */
	public void setPermisosEventoCatastrofico(boolean permisosEventoCatastrofico) {
		this.permisosEventoCatastrofico = permisosEventoCatastrofico;
	}




	/**
	 * @return the tiposIdResponsable
	 */
	public ArrayList<HashMap<String, Object>> getTiposIdResponsable() {
		return tiposIdResponsable;
	}




	/**
	 * @param tiposIdResponsable the tiposIdResponsable to set
	 */
	public void setTiposIdResponsable(
			ArrayList<HashMap<String, Object>> tiposIdResponsable) {
		this.tiposIdResponsable = tiposIdResponsable;
	}




	/**
	 * @return the ciudades
	 */
	public ArrayList<HashMap<String, Object>> getCiudades() {
		return ciudades;
	}




	/**
	 * @param ciudades the ciudades to set
	 */
	public void setCiudades(ArrayList<HashMap<String, Object>> ciudades) {
		this.ciudades = ciudades;
	}




	/**
	 * @return the ciudadesExp
	 */
	public ArrayList<HashMap<String, Object>> getCiudadesExp() {
		return ciudadesExp;
	}




	/**
	 * @param ciudadesExp the ciudadesExp to set
	 */
	public void setCiudadesExp(ArrayList<HashMap<String, Object>> ciudadesExp) {
		this.ciudadesExp = ciudadesExp;
	}




	/**
	 * @return the paises
	 */
	public ArrayList<HashMap<String, Object>> getPaises() {
		return paises;
	}




	/**
	 * @param paises the paises to set
	 */
	public void setPaises(ArrayList<HashMap<String, Object>> paises) {
		this.paises = paises;
	}




	/**
	 * @return the fechaAfiliacion
	 */
	public String getFechaAfiliacion() {
		return fechaAfiliacion;
	}




	/**
	 * @param fechaAfiliacion the fechaAfiliacion to set
	 */
	public void setFechaAfiliacion(String fechaAfiliacion) {
		this.fechaAfiliacion = fechaAfiliacion;
	}




	/**
	 * @return the codigoTipoIdResponsable
	 */
	public String getCodigoTipoIdResponsable() {
		return codigoTipoIdResponsable;
	}




	/**
	 * @param codigoTipoIdResponsable the codigoTipoIdResponsable to set
	 */
	public void setCodigoTipoIdResponsable(String codigoTipoIdResponsable) {
		this.codigoTipoIdResponsable = codigoTipoIdResponsable;
	}




	/**
	 * @return the numeroIdResponsable
	 */
	public String getNumeroIdResponsable() {
		return numeroIdResponsable;
	}




	/**
	 * @param numeroIdResponsable the numeroIdResponsable to set
	 */
	public void setNumeroIdResponsable(String numeroIdResponsable) {
		this.numeroIdResponsable = numeroIdResponsable;
	}




	/**
	 * @return the codigoPaisId
	 */
	public String getCodigoPaisId() {
		return codigoPaisId;
	}




	/**
	 * @param codigoPaisId the codigoPaisId to set
	 */
	public void setCodigoPaisId(String codigoPaisId) {
		this.codigoPaisId = codigoPaisId;
	}




	/**
	 * @return the codigoPaisResidencia
	 */
	public String getCodigoPaisResidencia() {
		return codigoPaisResidencia;
	}




	/**
	 * @param codigoPaisResidencia the codigoPaisResidencia to set
	 */
	public void setCodigoPaisResidencia(String codigoPaisResidencia) {
		this.codigoPaisResidencia = codigoPaisResidencia;
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
	 * @param codigoCuenta the codigoCuenta to set
	 */
	public void setCodigoCuenta(int codigoCuenta) {
		this.codigoCuenta = codigoCuenta;
	}


	/**
	 * @return the pathReferencia
	 */
	public String getPathReferencia() {
		return pathReferencia;
	}


	/**
	 * @param pathReferencia the pathReferencia to set
	 */
	public void setPathReferencia(String pathReferencia) {
		this.pathReferencia = pathReferencia;
	}


	/**
	 * @return the posConvenio
	 */
	public int getPosConvenio() {
		return posConvenio;
	}


	/**
	 * @param posConvenio the posConvenio to set
	 */
	public void setPosConvenio(int posConvenio) {
		this.posConvenio = posConvenio;
	}




	/**
	 * @return the codigoSubCuentaImpresion
	 */
	public String getCodigoSubCuentaImpresion() {
		return codigoSubCuentaImpresion;
	}


	/**
	 * @param codigoSubCuentaImpresion the codigoSubCuentaImpresion to set
	 */
	public void setCodigoSubCuentaImpresion(String codigoSubCuentaImpresion) {
		this.codigoSubCuentaImpresion = codigoSubCuentaImpresion;
	}


	/**
	 * @return the posPresupuesto
	 */
	public int getPosPresupuesto() {
		return posPresupuesto;
	}


	/**
	 * @param posPresupuesto the posPresupuesto to set
	 */
	public void setPosPresupuesto(int posPresupuesto) {
		this.posPresupuesto = posPresupuesto;
	}


	/**
	 * @return the presupuestoPaciente
	 */
	public boolean isPresupuestoPaciente() {
		return presupuestoPaciente;
	}


	/**
	 * @param presupuestoPaciente the presupuestoPaciente to set
	 */
	public void setPresupuestoPaciente(boolean presupuestoPaciente) {
		this.presupuestoPaciente = presupuestoPaciente;
	}


	/**
	 * @return the presupuestos
	 */
	public HashMap<String, Object> getPresupuestos() {
		return presupuestos;
	}


	/**
	 * @param presupuestos the presupuestos to set
	 */
	public void setPresupuestos(HashMap<String, Object> presupuestos) {
		this.presupuestos = presupuestos;
	}
	

	/**
	 * @return the presupuestos
	 */
	public Object getPresupuestos(String key) {
		return presupuestos.get(key);
	}


	/**
	 * @param presupuestos the presupuestos to set
	 */
	public void setPresupuestos(String key,Object obj) {
		this.presupuestos.put(key,obj);
	}


	/**
	 * @return the fueEliminadoResponsable
	 */
	public boolean isFueEliminadoResponsable() {
		return fueEliminadoResponsable;
	}


	/**
	 * @param fueEliminadoResponsable the fueEliminadoResponsable to set
	 */
	public void setFueEliminadoResponsable(boolean fueEliminadoResponsable) {
		this.fueEliminadoResponsable = fueEliminadoResponsable;
	}


	/**
	 * @return the fueModificadoResponsable
	 */
	public boolean isFueModificadoResponsable() {
		return fueModificadoResponsable;
	}


	/**
	 * @param fueModificadoResponsable the fueModificadoResponsable to set
	 */
	public void setFueModificadoResponsable(boolean fueModificadoResponsable) {
		this.fueModificadoResponsable = fueModificadoResponsable;
	}


	/**
	 * @return the puedoModificarCama
	 */
	public int getPuedoModificarCama() {
		return puedoModificarCama;
	}


	/**
	 * @param puedoModificarCama the puedoModificarCama to set
	 */
	public void setPuedoModificarCama(int puedoModificarCama) {
		this.puedoModificarCama = puedoModificarCama;
	}


	/**
	 * @return the cama
	 */
	public HashMap<String, Object> getCama() {
		return cama;
	}


	/**
	 * @param cama the cama to set
	 */
	public void setCama(HashMap<String, Object> cama) {
		this.cama = cama;
	}
	
	/**
	 * @return the cama
	 */
	public Object getCama(String key) {
		return cama.get(key);
	}


	/**
	 * @param cama the cama to set
	 */
	public void setCama(String key,Object obj) {
		this.cama.put(key, obj);
	}


	/**
	 * @return the centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}


	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}


	public Boolean getControlMensaje() {
		return controlMensaje;
	}


	public void setControlMensaje(Boolean controlMensaje) {
		this.controlMensaje = controlMensaje;
	}


	/**
	 * @return the tiposMonitoreo
	 */
	public HashMap getTiposMonitoreo() {
		return tiposMonitoreo;
	}


	/**
	 * @param tiposMonitoreo the tiposMonitoreo to set
	 */
	public void setTiposMonitoreo(HashMap tiposMonitoreo) {
		this.tiposMonitoreo = tiposMonitoreo;
	}
	
	/**
	 * @return the tiposMonitoreo
	 */
	public Object getTiposMonitoreo(String key) {
		return tiposMonitoreo.get(key);
	}


	/**
	 * @param tiposMonitoreo the tiposMonitoreo to set
	 */
	public void setTiposMonitoreo(String key,Object obj) {
		this.tiposMonitoreo.put(key,obj);
	}


	/**
	 * @return the codigoArea
	 */
	public String getCodigoArea() {
		return codigoArea;
	}


	/**
	 * @param codigoArea the codigoArea to set
	 */
	public void setCodigoArea(String codigoArea) {
		this.codigoArea = codigoArea;
	}


	/**
	 * @return the asignarCama
	 */
	public boolean isAsignarCama() {
		return asignarCama;
	}


	/**
	 * @param asignarCama the asignarCama to set
	 */
	public void setAsignarCama(boolean asignarCama) {
		this.asignarCama = asignarCama;
	}


	/**
	 * @return the codigoContrato
	 */
	public String getCodigoContrato() {
		return codigoContrato;
	}


	/**
	 * @param codigoContrato the codigoContrato to set
	 */
	public void setCodigoContrato(String codigoContrato) {
		this.codigoContrato = codigoContrato;
	}


	/**
	 * @return the coberturasSalud
	 */
	public ArrayList<HashMap<String, Object>> getCoberturasSalud() {
		return coberturasSalud;
	}


	/**
	 * @param coberturasSalud the coberturasSalud to set
	 */
	public void setCoberturasSalud(
			ArrayList<HashMap<String, Object>> coberturasSalud) {
		this.coberturasSalud = coberturasSalud;
	}
	
	/**
	 * Método para obtener el numero de coberturas salud
	 * @return
	 */
	public int getNumCoberturasSalud()
	{
		return this.coberturasSalud.size();
	}

     
	/**
	 *  Se puede realizar Registro envio informe inconsistencias
	 * @return
	 */
	public boolean isRegistroInconsistencias() {
		return registroInconsistencias;
	}

   /**
    * Asigna valor a Registro envio inconsistencias
    * @param registroInconsistencias
    */
	public void setRegistroInconsistencias(boolean registroInconsistencias) {
		this.registroInconsistencias = registroInconsistencias;
	}


/**
 * @param codigoEstratoSocial the codigoEstratoSocial to set
 */
public void setCodigoEstratoSocial(String codigoEstratoSocial) {
	this.codigoEstratoSocial = codigoEstratoSocial;
}


/**
 * @return the codigoEstratoSocial
 */
public String getCodigoEstratoSocial() {
	return codigoEstratoSocial;
}


/**
 * @param codigoTipoAfiliado the codigoTipoAfiliado to set
 */
public void setCodigoTipoAfiliado(String codigoTipoAfiliado) {
	this.codigoTipoAfiliado = codigoTipoAfiliado;
}


/**
 * @return the codigoTipoAfiliado
 */
public String getCodigoTipoAfiliado() {
	return codigoTipoAfiliado;
}


/**
 * @return Retorna trasladoCamasForm
 */
public TrasladoCamasForm getTrasladoCamasForm() {
	return trasladoCamasForm;
}


/**
 * @param trasladoCamasForm Asigna el atributo trasladoCamasForm
 */
public void setTrasladoCamasForm(TrasladoCamasForm trasladoCamasForm) {
	this.trasladoCamasForm = trasladoCamasForm;
}


/**
 * @return Retorna codigoRegistroAccidenteTransito
 */
public Long getCodigoRegistroAccidenteTransito() {
	return codigoRegistroAccidenteTransito;
}


/**
 * @param codigoRegistroAccidenteTransito Asigna el atributo codigoRegistroAccidenteTransito
 */
public void setCodigoRegistroAccidenteTransito(
		Long codigoRegistroAccidenteTransito) {
	this.codigoRegistroAccidenteTransito = codigoRegistroAccidenteTransito;
}

	
}
