/*
 * @(#)EmpresaForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.cargos;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * Form que contiene todos los datos específicos para generar 
 * el Registro de Empresa.
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Mayo 3, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */

public class EmpresaForm extends ValidatorForm
{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean empezarConsulta;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(EmpresaForm.class);
	
	/**
	 * Código axioma de la empresa   	
	 */
	private int codigo;

	/**
	 * Nit de la empresa				
	 */	
	private int tercero;	
	
	/**
	 * entero que se utiliza para la modificación del nit de una empresa
	 */
	private int terceroNuevo;
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Razón social de la empresa
	 */
	private String razonSocial;
	
	/**
	 * Nombre del contacto de la empresa			
	 */
	private String nombreContacto;
	
	/**
	 * Telefono de la empresa					
	 */
	private String telefono;
	
	/**
	 * Dirección de la empresa			
	 */
	private String direccion;
		
	/**
	 * Correo electronico del contacto de la empresa		
	 */
	private String correo;
		
	/**
	 * Dice si la empresa está activa en el sistema o no.				
	 */
	private boolean activa;
	
	/**
	 * Colección con los datos del listado, ya sea para consulta,
	 * como también para búsqueda avanzada (pager)
	 */
	private Collection col=null;
	
	/**
	 * Offset para el pager 
	 */
	private int offset=0;
	
	/**
	 * Campo donde se restringe por qué criterios se va a buscar
	 */
	private String criteriosBusqueda[];
	
	/**
	 * Nit (terceros)
	 */
	private String nit;
	
	/**
	 * Nombre tercero
	 */
	private String descripcionTercero;
	
	/**
	 * Direccion de radicacion de cuentas
	 */
	private String direccionCuentas;
	
	/**
	 * Direccion de Sucursal local
	 */
	private String direccionSucursal;
	
	/**
	 * Telefono de Sucursal local
	 */
	private String telefonoSucursal;
	
	/**
	 * Nombre del representante legal
	 */
	private String representante;
	
	/**
	 * Observaciones sobre la empresa
	 */
	private String observaciones;
	
	/**
	 * Piis sede principal empresa
	 */
	private String paisPrincipal;
	
	/**
	 * Ciudad sede principal empresa
	 */
	private String ciudadPrincipal;
	
	/**
	 * Pais de radicacion cuentas
	 */
	private String paisCuentas;
	
	/**
	 * Ciudad radicacion cuentas
	 */
	private String ciudadCuentas;
	
	/**
	 * 
	 */
	private String codigoPaisPrincipal;
	
	/**
	 * 
	 */
	private String codigoCiudadPrincipal;
	
	/**
	 * 
	 */
	private String codigoPaisCuentas;
	
	/**
	 * 
	 */
	private String codigoCiudadCuentas;
	
	/**
	 * 
	 */
	private String deptoPrincipal;
	
	/**
	 * 
	 */
	private String deptoCuentas;
	
	/**
	 * 
	 * */
	private String faxSedePrincipal;
	
	/**
	 * 
	 * */
	private String faxSucursalLocal;
	
	/**
	 * 
	 * */
	private String direccionTerritorial;
	
	
	/**
	 * Auxiliar del campo boolean activa, para poder mandar
	 * un nuevo valor diferente de true o false en la búsqueda
	 * avanzada
	 */
	private int activaAux;
	
	/**
	 * Contiene el log con info original para
	 * almacenar esta info en el log tipo Archivo
	 */
	private String logInfoOriginalEmpresa;
	
	private String getnombreciudad;
	
	private String numeroIdentificacion;
	
	/**
	 * 
	 */
	private String numeroAfiliados;
	
	/**
	 * 
	 */
	private InfoDatosDouble nivelIngreso;
	
	/**
	 * 
	 */
	private InfoDatosInt formaPago;
	
	/**
	 * 
	 */
	private HashMap datosTercero;
	
	private boolean validarTerceros;
	
	
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
		ActionErrors errores= new ActionErrors();					
		//Validate para el insertar
		if(estado.equals("salir"))
		{
			errores = super.validate(mapping,request);
			if(tercero<=0)
			{
				errores.add("Campo Nit - Nombre de la Empresa  vacio", new ActionMessage("errors.required","El campo Nit - Nombre de la Empresa"));
			}
			if(razonSocial.equals("") || !Utilidades.validarEspacios(razonSocial))
			{	
				errores.add("Campo Razón Social vacio", new ActionMessage("errors.required","El campo Razón Social"));
			}
//			if(telefono.equals("") || !Utilidades.validarEspacios(telefono))
//			{
//				errores.add("Teléfono vacio", new ActionMessage("errors.required","El campo Teléfono Sede Principal"));
//			}
//			if(direccion.equals("") || !Utilidades.validarEspacios(direccion))
//			{
//				errores.add("Dirección vacio", new ActionMessage("errors.required","El campo Dirección Sede Principal"));
//			}
			if(ciudadPrincipal.equals("") || !Utilidades.validarEspacios(ciudadPrincipal) || UtilidadTexto.isEmpty(ciudadPrincipal))
			{
				errores.add("Ciudad Vacio", new ActionMessage("errors.required","El campo Ciudad Principal "));
			}
			if(UtilidadTexto.isEmpty(paisPrincipal) || !Utilidades.validarEspacios(paisPrincipal) || paisPrincipal.equalsIgnoreCase(""))
			{
				errores.add("Pais vacio", new ActionMessage("errors.required","El campo País Principal "));
			}
			if(ciudadCuentas.equals("") || !Utilidades.validarEspacios(ciudadCuentas) || UtilidadTexto.isEmpty(ciudadCuentas))
			{
				errores.add("Ciudad Vacio", new ActionMessage("errors.required","El campo Ciudad de Radicación Cuentas "));
			}
			if(UtilidadTexto.isEmpty(paisCuentas) || !Utilidades.validarEspacios(paisCuentas) || paisCuentas.equalsIgnoreCase(""))
			{
				errores.add("Pais vacio", new ActionMessage("errors.required","El campo País de Radicación Cuentas "));
			}

			if(this.numeroAfiliados.length()>0 && !UtilidadTexto.isNumber(this.numeroAfiliados))
			{
				errores.add("", new ActionMessage("errors.invalid","El campo Numero Afiliados "));
			}
			
			if (!errores.isEmpty() && estado.equals("salir"))
				this.estado="empezar";
			
			
			
			/*if(correo.indexOf("@")!=-1)
				errores.add("E-mail sin @", new ActionMessage("errors.email","El campo E-mail"));
			}*/
		}
		
		//Validar para la modificacion
		else if(estado.equals("guardarModificacion"))
		{
			errores = super.validate(mapping,request);
			if(tercero<=0)
			{
				errores.add("Campo Nit - Nombre de la Empresa  vacio", new ActionMessage("errors.required","El campo Nit - Nombre de la Empresa"));
			}
			if(razonSocial.equals("") || !Utilidades.validarEspacios(razonSocial))
			{	
				errores.add("Campo Razón Social vacio", new ActionMessage("errors.required","El campo Razón Social"));
			}
			
//			if(telefono.equals("") || !Utilidades.validarEspacios(telefono))
//			{
//				errores.add("Teléfono vacio", new ActionMessage("errors.required","El campo Teléfono Sede Principal"));
//			}
//			if(direccion.equals("") || !Utilidades.validarEspacios(direccion))
//			{
//				errores.add("Dirección vacio", new ActionMessage("errors.required","El campo Dirección Sede Principal"));
//			}
			if(codigoCiudadPrincipal.equals("") || !Utilidades.validarEspacios(codigoCiudadPrincipal) || UtilidadTexto.isEmpty(codigoCiudadPrincipal) || codigoCiudadPrincipal.equals(" "+ConstantesBD.separadorSplit+" "))
			{
				errores.add("Ciudad Vacio", new ActionMessage("errors.required","El campo Ciudad Principal "));
			}
			if(UtilidadTexto.isEmpty(codigoPaisPrincipal) || !Utilidades.validarEspacios(codigoPaisPrincipal) || codigoPaisPrincipal.equalsIgnoreCase(""))
			{
				errores.add("Pais vacio", new ActionMessage("errors.required","El campo País Principal "));
			}
			if(codigoCiudadCuentas.equals("") || !Utilidades.validarEspacios(codigoCiudadCuentas) || UtilidadTexto.isEmpty(codigoCiudadCuentas) || codigoCiudadCuentas.equals(" "+ConstantesBD.separadorSplit+" "))
			{
				errores.add("Ciudad Vacio", new ActionMessage("errors.required","El campo Ciudad de Radicación Cuentas "));
			}
			if(UtilidadTexto.isEmpty(codigoPaisCuentas) || !Utilidades.validarEspacios(codigoPaisCuentas) || codigoPaisCuentas.equalsIgnoreCase(""))
			{
				errores.add("Pais vacio", new ActionMessage("errors.required","El campo País de Radicación Cuentas "));
			}
			if(this.numeroAfiliados.length()>0 && !UtilidadTexto.isNumber(this.numeroAfiliados))
			{
				errores.add("", new ActionMessage("errors.invalid","El campo Numero Afiliados "));
			}
			if(!errores.isEmpty() && estado.equals("guardarModificacion"))
				this.estado="modificar";
		}
		
		else if(estado.equals("listar") || estado.equals("listarModificar"))
			errores = super.validate(mapping,request);
		
		else if(estado.equals("modificar"))
			errores = super.validate(mapping,request);
		
		else if(estado.equals("inserta"))
			mapping.findForward("principal");
		
		else if(estado.equals("busquedaAvanzada"))
			errores = super.validate(mapping,request);
		
		else if(estado.equals("resultadoBusquedaAvanzada"))
			errores = super.validate(mapping,request);
		
		return errores;
	}

	/**
	 *resetea todos los posible valores que se 
	 *utilizan en el registro de empresa
	 */
	public void reset()
	{
		this.validarTerceros=false;
		this.codigo=0;
		this.tercero= 0;
		this.estado="";
		this.razonSocial="";
		this.nombreContacto="";
		this.telefono="";
		this.direccion="";
		this.correo="";
		this.activa=true;
		this.terceroNuevo= 0;
		this.nit="";
		this.descripcionTercero="";
		this.direccionCuentas="";
		this.direccionSucursal="";
		this.telefonoSucursal="";
		this.representante="";
		this.observaciones="";
		this.paisPrincipal="";
		this.paisCuentas="";
		this.ciudadPrincipal="";
		this.ciudadCuentas="";
		this.codigoPaisPrincipal="";
		this.codigoCiudadPrincipal="";
		this.codigoPaisCuentas="";
		this.codigoCiudadCuentas="";
		this.deptoPrincipal="";
		this.deptoCuentas="";
		this.activaAux=0;
		this.numeroIdentificacion="";		
		this.logInfoOriginalEmpresa="";
		this.faxSedePrincipal = "";
		this.faxSucursalLocal = "";
		this.direccionTerritorial = ConstantesBD.acronimoNo;
		this.numeroAfiliados= "";
		this.nivelIngreso= new InfoDatosDouble();
		this.formaPago= new InfoDatosInt();
		this.datosTercero= new HashMap();
		}
	
	/**
	 * resetea en vector de strings que
	 * contiene los criterios de búsqueda 
	 *
	 */
	public void resetCriteriosBusqueda()
	{
		try
		{
			for(int k=0 ; k<criteriosBusqueda.length ; k++)
				criteriosBusqueda[k]="";
		}catch(Exception e)
		{
			logger.warn(" error en el reset de busqueda "+e);
		}
		
	}
	
	/**
	 * Retorna si la empresa está activa en el sistema o no
	 * @return  
	 */
	public boolean getActiva() {
		return activa;
	}

	/**
	 * Retorna el correo electronico del contacto de la empresa
	 * @return
	 */
	public String getCorreo() {
		return correo;
	}

	/**
	 *  Retorna la dirección de la empresa
	 * @return
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * Retorna el estado en que se encuentre el registro de empresa
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * Retorna el nombre del contacto de la empresa
	 * @return
	 */
	public String getNombreContacto() {
		return nombreContacto;
	}

	/**
	 * Retorna la razón social de la empresa
	 * @return
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * Retorna el telefono de la empresa
	 * @return
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * Asigna si la empresa está activa en el sistema o no
	 * @param b
	 */
	public void setActiva(boolean b) {
		activa = b;
	}

	/**
	 * Asigna el correo electronico del contacto de la empresa
	 * @param string
	 */
	public void setCorreo(String string) {
		correo = string;
	}

	/**
	 *  Asigna la dirección de la empresa
	 * @param string
	 */
	public void setDireccion(String string) {
		direccion = string;
	}

	/**
	 * Asigna el estado en que se encuentre la solicitud
	 * @param string
	 */
	public void setEstado(String string) {
		estado = string;
	}

	/**
	 * Asigna el nombre del contacto de la empresa
	 * @param string
	 */
	public void setNombreContacto(String string) {
		nombreContacto = string;
	}

	/**
	 * Asigna la razón social de la empresa
	 * @param string
	 */
	public void setRazonSocial(String string) {
		razonSocial = string;
	}

	/**
	 * Asigna el telefono de la empresa
	 * @param string
	 */
	public void setTelefono(String string) {
		telefono = string;
	}

	/**
	 * Asigna el código axioma de la empresa
	 * @return
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * Asigna el código axioma de la empresa
	 * @param i
	 */
	public void setCodigo(int i) {
		codigo = i;
	}

	/**
	 * Retorna el nit de la empresa
	 * @return
	 */
	public int getTercero() {
		return tercero;
	}

	/**
	 * Asigna el nit de la empresa
	 * @param i
	 */
	public void setTercero(int i) {
		tercero = i;
	}

	/**
	 * Retorna el nit de la empresa que se desea actualizar
	 * para modificar éste campo de la empresa
	 * @return
	 */
	public int getTerceroNuevo() {
		return terceroNuevo;
	}

	/**
	 * Asigna el nit nuevo
	 * para modificar éste campo de la empresa
	 * @param i
	 */
	public void setTerceroNuevo(int i) {
		terceroNuevo = i;
	}

	/**
	 * Retorna Colección para mostrar datos en el pager
	 * @return
	 */
	public Collection getCol() {
		return col;
	}
	
	/**
	 * Asigna Colección para mostrar datos en el pager
	 * @param collection
	 */
	public void setCol(Collection collection) {
		col = collection;
	}
	
	public int getColSize()
	{
		if(col!=null)
			return col.size();
		else
			return 0;
	}
	
	/**
	 * Retorna Offset del pager
	 * @return
	 */
	public int getOffset()
	{
		return offset;
	}

	/**
	 * Asigna Offset del pager
	 * @param i
	 */
	public void setOffset(int i) 
	{
		offset = i;
	}

	/**
	 * Retorna los criterios de búsqueda como strings
	 * @return
	 */
	public String[] getCriteriosBusqueda() {
		return criteriosBusqueda;
	}

	/**
	 * Asigna los criterios de búsqueda como strings
	 * @param strings
	 */
	public void setCriteriosBusqueda(String[] strings) {
		criteriosBusqueda = strings;
	}

	/**
	 * Retorna el nombre del tercero
	 * @return
	 */
	public String getDescripcionTercero() {
		return descripcionTercero;
	}

	/**
	 * Retorna el nit
	 * @return
	 */
	public String getNit() {
		return nit;
	}

	/**
	 * Asigna el nombre del tercero
	 * @param string
	 */
	public void setDescripcionTercero(String string) {
		descripcionTercero = string;
	}

	/**
	 * Asigna el nit
	 * @param string
	 */
	public void setNit(String string) {
		nit = string;
	}

	/**
	 * Retorna el Auxiliar del campo boolean activa, para poder mandar
	 * un nuevo valor diferente de true o false en la búsqueda
	 * avanzada
	 * @return
	 */
	public int getActivaAux() {
		return activaAux;
	}

	/**
	 * Asigna el Auxiliar del campo boolean activa, para poder mandar
	 * un nuevo valor diferente de true o false en la búsqueda
	 * avanzada
	 * @param i
	 */
	public void setActivaAux(int i) {
		activaAux = i;
	}

	/**
	 * retorna el log con info original para
	 * almacenar esta info en el log tipo Archivo
	 * @return
	 */
	public String getLogInfoOriginalEmpresa() {
		return logInfoOriginalEmpresa;
	}

	/**
	 * Asigna el log con info original para
	 * almacenar esta info en el log tipo Archivo
	 * @param string
	 */
	public void setLogInfoOriginalEmpresa(String string) {
		logInfoOriginalEmpresa = string;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCiudadCuentas() {
		return ciudadCuentas;
	}
	
	/**
	 * 
	 * @param ciudadCuentas
	 */
	public void setCiudadCuentas(String ciudadCuentas) {
		this.ciudadCuentas = ciudadCuentas;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCiudadPrincipal() {
		return ciudadPrincipal;
	}
	
	/**
	 * 
	 * @param ciudadPrincipal
	 */
	public void setCiudadPrincipal(String ciudadPrincipal) {
		this.ciudadPrincipal = ciudadPrincipal;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDireccionCuentas() {
		return direccionCuentas;
	}
	
	/**
	 * 
	 * @param direccionCuentas
	 */
	public void setDireccionCuentas(String direccionCuentas) {
		this.direccionCuentas = direccionCuentas;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDireccionSucursal() {
		return direccionSucursal;
	}
	
	/**
	 * 
	 * @param direccionSucursal
	 */
	public void setDireccionSucursal(String direccionSucursal) {
		this.direccionSucursal = direccionSucursal;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getObservaciones() {
		return observaciones;
	}
	
	/**
	 * 
	 * @param observaciones
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPaisCuentas() {
		return paisCuentas;
	}
	
	/**
	 * 
	 * @param paisCuentas
	 */
	public void setPaisCuentas(String paisCuentas) {
		this.paisCuentas = paisCuentas;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPaisPrincipal() {
		return paisPrincipal;
	}
	
	/**
	 * 
	 * @param paisPrincipal
	 */
	public void setPaisPrincipal(String paisPrincipal) {
		this.paisPrincipal = paisPrincipal;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRepresentante() {
		return representante;
	}
	
	/**
	 * 
	 * @param representante
	 */
	public void setRepresentante(String representante) {
		this.representante = representante;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTelefonoSucursal() {
		return telefonoSucursal;
	}
	
	/**
	 * 
	 * @param telefonoSucursal
	 */
	public void setTelefonoSucursal(String telefonoSucursal) {
		this.telefonoSucursal = telefonoSucursal;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDeptoCuentas() {
		return deptoCuentas;
	}
	
	/**
	 * 
	 * @param deptoCuentas
	 */
	public void setDeptoCuentas(String deptoCuentas) {
		this.deptoCuentas = deptoCuentas;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDeptoPrincipal() {
		return deptoPrincipal;
	}
	
	/**
	 * 
	 * @param deptoPrincipal
	 */
	public void setDeptoPrincipal(String deptoPrincipal) {
		this.deptoPrincipal = deptoPrincipal;
	}

	public String getGetnombreciudad() {
		return getnombreciudad;
	}

	public void setGetnombreciudad(String getnombreciudad) {
		this.getnombreciudad = getnombreciudad;
	}

	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}

	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}

	/**
	 * @return the codigoCiudadCuentas
	 */
	public String getCodigoCiudadCuentas() {
		return codigoCiudadCuentas;
	}

	/**
	 * @param codigoCiudadCuentas the codigoCiudadCuentas to set
	 */
	public void setCodigoCiudadCuentas(String codigoCiudadCuentas) {
		this.codigoCiudadCuentas = codigoCiudadCuentas;
	}

	/**
	 * @return the codigoCiudadPrincipal
	 */
	public String getCodigoCiudadPrincipal() {
		return codigoCiudadPrincipal;
	}

	/**
	 * @param codigoCiudadPrincipal the codigoCiudadPrincipal to set
	 */
	public void setCodigoCiudadPrincipal(String codigoCiudadPrincipal) {
		this.codigoCiudadPrincipal = codigoCiudadPrincipal;
	}

	/**
	 * @return the codigoPaisCuentas
	 */
	public String getCodigoPaisCuentas() {
		return codigoPaisCuentas;
	}

	/**
	 * @param codigoPaisCuentas the codigoPaisCuentas to set
	 */
	public void setCodigoPaisCuentas(String codigoPaisCuentas) {
		this.codigoPaisCuentas = codigoPaisCuentas;
	}

	/**
	 * @return the codigoPaisPrincipal
	 */
	public String getCodigoPaisPrincipal() {
		return codigoPaisPrincipal;
	}

	/**
	 * @param codigoPaisPrincipal the codigoPaisPrincipal to set
	 */
	public void setCodigoPaisPrincipal(String codigoPaisPrincipal) {
		this.codigoPaisPrincipal = codigoPaisPrincipal;
	}

	public String getFaxSedePrincipal() {
		return faxSedePrincipal;
	}

	public void setFaxSedePrincipal(String faxSedePrincipal) {
		this.faxSedePrincipal = faxSedePrincipal;
	}

	public String getFaxSucursalLocal() {
		return faxSucursalLocal;
	}

	public void setFaxSucursalLocal(String faxSucursalLocal) {
		this.faxSucursalLocal = faxSucursalLocal;
	}

	public String getDireccionTerritorial() {
		return direccionTerritorial;
	}

	public void setDireccionTerritorial(String direccionTerritorial) {
		this.direccionTerritorial = direccionTerritorial;
	}
	

	/**
	 * @return the numeroAfiliados
	 */
	public String getNumeroAfiliados() {
		return numeroAfiliados;
	}

	/**
	 * @param numeroAfiliados the numeroAfiliados to set
	 */
	public void setNumeroAfiliados(String numeroAfiliados) {
		this.numeroAfiliados = numeroAfiliados;
	}

	/**
	 * @return the nivelIngreso
	 */
	public InfoDatosDouble getNivelIngreso() {
		return nivelIngreso;
	}

	/**
	 * @param nivelIngreso the nivelIngreso to set
	 */
	public void setNivelIngreso(InfoDatosDouble nivelIngreso) {
		this.nivelIngreso = nivelIngreso;
	}

	/**
	 * @return the formaPago
	 */
	public InfoDatosInt getFormaPago() {
		return formaPago;
	}

	/**
	 * @param formaPago the formaPago to set
	 */
	public void setFormaPago(InfoDatosInt formaPago) {
		this.formaPago = formaPago;
	}

	public void setEmpezarConsulta(boolean empezarConsulta) {
		this.empezarConsulta = empezarConsulta;
	}

	public boolean isEmpezarConsulta() {
		return empezarConsulta;
	}

	public HashMap getDatosTercero() {
		return datosTercero;
	}

	public void setDatosTercero(HashMap datosTercero) {
		this.datosTercero = datosTercero;
	}

	public void setValidarTerceros(boolean validarTerceros) {
		this.validarTerceros = validarTerceros;
	}

	public boolean isValidarTerceros() {
		return validarTerceros;
	}
	
	

		
}
