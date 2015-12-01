/*
 * @(#)AnulacionFacturasForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.facturacion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.dto.facturacion.DtoIgualConsecutivoFactura;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ValidacionesAnulacionFacturas;

/**
 * Form que contiene todos los datos específicos para generar 
 * la consulta y anulacion de facturas
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Agosto 08, 2005
 * @author wrios 
 */
public class AnulacionFacturasForm extends ValidatorForm
{
    /**
	 * serial
	 */
	private static final long serialVersionUID = -5442811097981279728L;

	/**
     * estado de la accion
     */
    private String estado;
    
    /**
     * consecutivo de la factura
     */
    private String consecutivoFactura;
    
    /**
	 * Colección 
	 */
	@SuppressWarnings("unchecked")
	private Collection col;
	
	/**
	 * columna por la cual se quiere ordenar
	 */
	private String columna;
	
	/**
	 * ultima columna por la cual se ordeno
	 */
	private String ultimaPropiedad;
	
	/**
	 * codigo de la institucion
	 */
	private int codigoInstitucion;
	
	/**
	 * codigo de la institucion
	 */
	private int ingresoPaciente;
	
	/**
	 * codigo axioma de la factura
	 */
	private BigDecimal codigoFactura;
	
	/**
	 * boolean que indica si la factura es perteneciente a un ingreso odontologico
	 * o general
	 */
	private boolean esFacturaOdontologica;
	
	
	/**
	 * bool para cerrar o no la cuenta
	 * 0-->false
	 * 1--->true
	 * 2-->no sel
	 */
	private int cerrarCuenta;
	
	/**
	 * int para insertar el motivo de anulación
	 */
	private int motivoAnulacion;
	
	/**
	 * desc del motivo de anulacion
	 */
	private String descripcionMotivoAnulacion;
	
	/**
	 * observaciones de la anulacion;
	 */
	private String observaciones;
	
	/**
	 * Campo para los criterios de búsqueda
	 * captura la fecha inicial de anulación
	 */
	private String fechaInicialAnulacion;
	
	/**
	 * Campo para los criterios de búsqueda
	 * captura la fecha final de anulación
	 */
	private String fechaFinalAnulacion;

	/**
	 * Campo de para obtener el consecutivo de 
	 * anulacion de la factura
	 */
	private String consecutivoAnulacion;
	
	/**
	 * fecha de anulacion
	 */
	private String fechaAnulacion;
	
	/**
	 * hora de anulacion
	 */
	private String horaAnulacion;
	
	/**
	 * login del usuario que anula
	 */
	private String loginUsuario;
	
	/**
	 * (codigo ) estado de la cuenta
	 */
	private int codigoEstadoCuenta;
	
	/**
	 * (nombre) estado de la cuenta
	 */
	private String nombreEstadoCuenta;
	
	
	/**
	 * (codigo ) estado de la cuenta Madre
	 */
	private int codigoEstadoCuentaMadre;
	
	/**
	 * (nombre) estado de la cuenta Madre
	 */
	private String nombreEstadoCuentaMadre;
	
	/**
	 * boolean que indica si la busqueda fue realizada o no por el 
	 * consecutivo de anulacion
	 */
	private boolean esBusquedaXConsecutivoAnulacion;
	
	/**
	 * boolean que indica si la busqueda fue realizada o no por el 
	 * consecutivo de factura
	 */
	private boolean esBusquedaXConsecutivoFactura;
	
	/**
	 * boolean que indica si la busqueda fue realizada o no por el 
	 * login de usuario
	 */
	private boolean esBusquedaXLoginUsuarioAnulacion;
	
	/**
	 * codigo del centro de atencion
	 */
	private int codigoCentroAtencion;
	
	/**
	 * 
	 */
	private boolean mostrarAdvertenciaIngresos; 
	
	/**
     * 
     */
    private ArrayList<DtoIgualConsecutivoFactura> facturasMismoConsecutivo;
    
    /**
     * 
     */
    private int indiceConsecutivoCargar;
    
    /**
     * 
     */
    private boolean mostrarInformacion;
    
    /**
     * 
     */
    private String empresaInstitucion;
	
    /**
     * 
     */
    private String descEntidadSubcontratada;
    
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
		errores=super.validate(mapping,request);
		ValidacionesAnulacionFacturas objectValidacionesAnulacion= new ValidacionesAnulacionFacturas();
		if(this.estado.equals("cargarListadoFacturasMismoConsecutivo"))
		{				
		    validacionesConsecutivoFactura(errores, objectValidacionesAnulacion);
		}    
		if(this.estado.equals("anular"))
		{    
		    validacionesAnular(errores, objectValidacionesAnulacion, request);
		}    
		if(this.estado.equals("consultarAnulaciones"))
		{
		    validacionesConsulta(errores);    
		}
	    if(!errores.isEmpty())
		{
		    if(this.estado.equals("cargarListadoFacturasMismoConsecutivo"))
		    {    
		        this.setEstado("empezar");
		        this.mostrarInformacion=false;
		        errores.add("anulacion cancelada!!!!", new ActionMessage("error.facturacion.anulacionCancelada"));
		    }    
		    else if(this.estado.equals("anular"))
		    {    
		        this.setEstado("resultadoBusqueda");
		        this.mostrarInformacion=true;
		        errores.add("anulacion cancelada!!!!", new ActionMessage("error.facturacion.anulacionCancelada"));
		    }    
		    else if(this.estado.equals("consultarAnulaciones"))
		    {
		        this.setEstado("empezarConsultar");
		    }
		        
		}
		return errores;
	}

	/**
	 * @param errores
	 * @param con
	 * @param objectValidacionesAnulacion
	 */
	private void validacionesConsecutivoFactura(ActionErrors errores,
												ValidacionesAnulacionFacturas objectValidacionesAnulacion) 
	{
		if(this.consecutivoFactura.trim().equals(""))
		{	
			errores.add("Campo consecutivo factura vacio", new ActionMessage("errors.required","El campo # Factura "));
		}
		else
		{    
		    if(!objectValidacionesAnulacion.existeConsecutivoEnFactura(this.getConsecutivoFactura().trim(), this.getCodigoInstitucion()))
		    {
		        errores.add("No existe la factura", new ActionMessage("errors.noExiste", "El  # de factura "+this.getConsecutivoFactura()));
		    }
		}
	}	

	/**
	 * @param errores
	 * @param objectValidacionesAnulacion
	 */
	private void validacionesAnular(	ActionErrors errores, 
										ValidacionesAnulacionFacturas objectValidacionesAnulacion,
										HttpServletRequest request) 
	{
		if(this.getMotivoAnulacion()<=0)
		{
		    errores.add("", new ActionMessage("errors.required","El campo Motivo Anulación"));
		}
		if(this.getPuedoMostrarCerrarCuenta())
		{
		    if(this.getCerrarCuenta()==2)
		    {
		        errores.add("", new ActionMessage("errors.required","El campo ¿Cerrar Cuenta?"));
		    }
		}
		if(errores.isEmpty())
		{
			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			ActionErrors errores1= ValidacionesAnulacionFacturas.validacionesAnulacionBasicas(this.getCodigoFactura(), this.consecutivoFactura, usuario);
			if(errores1!=null)
			{
				errores= errores1;
			}
		}
	}
	
	/**
	 * @param errores
	 */
	private void validacionesConsulta(ActionErrors errores) {
		if(this.getFechaInicialAnulacion().trim().equals("") 
			&& this.getFechaFinalAnulacion().trim().equals("") 
			&& this.getConsecutivoAnulacion().trim().equals("") 
			&& this.getConsecutivoFactura().trim().equals("") 
			&& this.getLoginUsuario().trim().equals(""))
		{
		    errores.add("", new ActionMessage("errors.required","Para consultar las anulaciones debe por lo menos un campo de Fecha(s) - o consecutivo(s) o login del usuario que anula "));
		}
		else
		{    
		    if(this.getFechaInicialAnulacion().trim().equals("") && this.getFechaFinalAnulacion().trim().equals(""))
		    {
		        // nada
		    }
		    else
		    {   
			    if(this.getFechaInicialAnulacion().equals(""))
				{	
					errores.add("Campo Fecha Inicial vacio", new ActionMessage("errors.required","El campo Fecha Inicial"));
				}
			    else
				{
					if(!UtilidadFecha.validarFecha(this.getFechaInicialAnulacion()))
					{
						errores.add("Fecha inicial", new ActionMessage("errors.formatoFechaInvalido", " Inicial"));							
					}
					else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaInicialAnulacion(), UtilidadFecha.getFechaActual()))
					{
					    errores.add("Fecha inicial", new ActionMessage("errors.fechaAnteriorIgualActual","inicial", "actual"));
					}
				}
			    if(this.getFechaFinalAnulacion().equals(""))
				{	
					errores.add("Campo Fecha Final vacio", new ActionMessage("errors.required","El campo Fecha Final"));
				}
			    else
				{
					if(!UtilidadFecha.validarFecha(this.getFechaFinalAnulacion()))
					{
						errores.add("Fecha final", new ActionMessage("errors.formatoFechaInvalido", "Final"));							
					}
					else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaFinalAnulacion(), UtilidadFecha.getFechaActual()))
					{
					    errores.add("Fecha Final", new ActionMessage("errors.fechaPosteriorIgualActual","Final", "actual"));
					}
				}
			    if(errores.isEmpty())
			    {
			        if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaInicialAnulacion(), this.getFechaFinalAnulacion()))
			        {
			            errores.add("Fecha ini vs Fecha fin", new ActionMessage("errors.fechaAnteriorIgualActual", "inicial", "final"));
			        }
			        if(errores.isEmpty())
			        {
			        	int numeroMesesDiferencia=UtilidadFecha.numeroMesesEntreFechas(this.getFechaInicialAnulacion(), this.getFechaFinalAnulacion(),true);
			        	if(numeroMesesDiferencia>3)
			        	{
			        		errores.add("", new ActionMessage("errors.rangoMayorTresMeses", "PARA CONSULTAR FACTURAS ANULADAS"));
			        	}
			        }
			    }
		    }//else no    
		}
	}
	
	/**
	 * resetea los valores de la forma
	 */
	public void reset(boolean borrarCriteriosBusqueda)
	{
	    if(borrarCriteriosBusqueda)
	    {
	        this.consecutivoFactura="";
	        this.consecutivoAnulacion="";
	        this.fechaFinalAnulacion="";
		    this.fechaInicialAnulacion="";
		    this.esBusquedaXConsecutivoAnulacion=false;
		    this.esBusquedaXConsecutivoFactura=false;
		    this.esBusquedaXLoginUsuarioAnulacion=false;
		    this.esFacturaOdontologica= false;
	    }
	    
	    this.mostrarAdvertenciaIngresos=false;
	    this.motivoAnulacion=ConstantesBD.codigoNuncaValido;
	    this.observaciones="";
	    this.cerrarCuenta=2;
	    this.fechaAnulacion="";
	    this.horaAnulacion="";
	    this.loginUsuario="";
	    this.codigoEstadoCuenta=ConstantesBD.codigoNuncaValido;
	    this.nombreEstadoCuenta="";
	    this.codigoEstadoCuentaMadre=ConstantesBD.codigoNuncaValido;
	    this.nombreEstadoCuentaMadre="";
	    this.ingresoPaciente=ConstantesBD.codigoNuncaValido;
	    this.descripcionMotivoAnulacion="";
	    this.codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
	    this.facturasMismoConsecutivo=new ArrayList<DtoIgualConsecutivoFactura>();
	    this.indiceConsecutivoCargar=ConstantesBD.codigoNuncaValido;
	    this.mostrarInformacion=false;
	    this.empresaInstitucion="";
	    this.descEntidadSubcontratada="";
	}
	
	/**
     * @return Returns the consecutivoFactura.
     */
    public String getConsecutivoFactura() {
        return consecutivoFactura;
    }
    /**
     * @param consecutivoFactura The consecutivoFactura to set.
     */
    public void setConsecutivoFactura(String consecutivoFactura) {
        this.consecutivoFactura = consecutivoFactura;
    }
    /**
     * @return Returns the estado.
     */
    public String getEstado() {
        return estado;
    }
    /**
     * @param estado The estado to set.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    /**
	 * Retorna Colección 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection getCol() {
		return col;
	}
	/**
	 * Asigna Colección 
	 * @param collection
	 */
	@SuppressWarnings("unchecked")
	public void setCol(Collection collection) {
		col = collection;
	}
	/**
	 * size de la coleccion
	 * @return
	 */
	public int getColSize()
	{
		if(col!=null)
			return col.size();
		else
			return 0;
	}
	
    /**
     * @return Returns the codigoInstitucion.
     */
    public int getCodigoInstitucion() {
        return codigoInstitucion;
    }
    /**
     * @param codigoInstitucion The codigoInstitucion to set.
     */
    public void setCodigoInstitucion(int codigoInstitucion) {
        this.codigoInstitucion = codigoInstitucion;
    }
    
    /**
     * @return Returns the observaciones.
     */
    public String getObservaciones() {
        return observaciones;
    }
    /**
     * @param observaciones The observaciones to set.
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    /**
     * @return Returns the motivoAnulacion.
     */
    public int getMotivoAnulacion() {
        return motivoAnulacion;
    }
    /**
     * @param motivoAnulacion The motivoAnulacion to set.
     */
    public void setMotivoAnulacion(int motivoAnulacion) {
        this.motivoAnulacion = motivoAnulacion;
    }
    /**
     * indica si puedo o no mostrar cerrar cuenta
     * @param codigoInstitucion
     * @return
     */
    public boolean getPuedoMostrarCerrarCuenta()
    {
        if(ValoresPorDefecto.getCerrarCuentaAnulacionFactura(this.codigoInstitucion).trim().equals("true") && !this.isEsFacturaOdontologica())
            return true;
        else
            return false;
    }
    /**
     * @return Returns the cerrarCuenta.
     */
    public int getCerrarCuenta() {
        return cerrarCuenta;
    }
    /**
     * @param cerrarCuenta The cerrarCuenta to set.
     */
    public void setCerrarCuenta(int cerrarCuenta) {
        this.cerrarCuenta = cerrarCuenta;
    }
    /**
     * @return Returns the consecutivoAnulacion.
     */
    public String getConsecutivoAnulacion() {
        return consecutivoAnulacion;
    }
    /**
     * @param consecutivoAnulacion The consecutivoAnulacion to set.
     */
    public void setConsecutivoAnulacion(String consecutivoAnulacion) {
        this.consecutivoAnulacion = consecutivoAnulacion;
    }
    /**
     * @return Returns the fechaFinalAnulacion.
     */
    public String getFechaFinalAnulacion() {
        return fechaFinalAnulacion;
    }
    /**
     * @param fechaFinalAnulacion The fechaFinalAnulacion to set.
     */
    public void setFechaFinalAnulacion(String fechaFinalAnulacion) {
        this.fechaFinalAnulacion = fechaFinalAnulacion;
    }
    /**
     * @return Returns the fechaInicialAnulacion.
     */
    public String getFechaInicialAnulacion() {
        return fechaInicialAnulacion;
    }
    /**
     * @param fechaInicialAnulacion The fechaInicialAnulacion to set.
     */
    public void setFechaInicialAnulacion(String fechaInicialAnulacion) {
        this.fechaInicialAnulacion = fechaInicialAnulacion;
    }
    /**
     * @return Returns the codigoEstadoCuenta.
     */
    public int getCodigoEstadoCuenta() {
        return codigoEstadoCuenta;
    }
    /**
     * @param codigoEstadoCuenta The codigoEstadoCuenta to set.
     */
    public void setCodigoEstadoCuenta(int codigoEstadoCuenta) {
        this.codigoEstadoCuenta = codigoEstadoCuenta;
    }
    /**
     * @return Returns the descripcionMotivoAnulacion.
     */
    public String getDescripcionMotivoAnulacion() {
        return descripcionMotivoAnulacion;
    }
    /**
     * @param descripcionMotivoAnulacion The descripcionMotivoAnulacion to set.
     */
    public void setDescripcionMotivoAnulacion(String descripcionMotivoAnulacion) {
        this.descripcionMotivoAnulacion = descripcionMotivoAnulacion;
    }
    /**
     * @return Returns the fechaAnulacion.
     */
    public String getFechaAnulacion() {
        return fechaAnulacion;
    }
    /**
     * @param fechaAnulacion The fechaAnulacion to set.
     */
    public void setFechaAnulacion(String fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }
    /**
     * @return Returns the horaAnulacion.
     */
    public String getHoraAnulacion() {
        return horaAnulacion;
    }
    /**
     * @param horaAnulacion The horaAnulacion to set.
     */
    public void setHoraAnulacion(String horaAnulacion) {
        this.horaAnulacion = horaAnulacion;
    }
    /**
     * @return Returns the loginUsuario.
     */
    public String getLoginUsuario() {
        return loginUsuario;
    }
    /**
     * @param loginUsuario The loginUsuario to set.
     */
    public void setLoginUsuario(String loginUsuario) {
        this.loginUsuario = loginUsuario;
    }
    /**
     * @return Returns the nombreEstadoCuenta.
     */
    public String getNombreEstadoCuenta() {
        return nombreEstadoCuenta;
    }
    /**
     * @param nombreEstadoCuenta The nombreEstadoCuenta to set.
     */
    public void setNombreEstadoCuenta(String nombreEstadoCuenta) {
        this.nombreEstadoCuenta = nombreEstadoCuenta;
    }
    /**
     * @return Returns the columna.
     */
    public String getColumna() {
        return columna;
    }
    /**
     * @param columna The columna to set.
     */
    public void setColumna(String columna) {
        this.columna = columna;
    }
    /**
     * @return Returns the ultimaPropiedad.
     */
    public String getUltimaPropiedad() {
        return ultimaPropiedad;
    }
    /**
     * @param ultimaPropiedad The ultimaPropiedad to set.
     */
    public void setUltimaPropiedad(String ultimaPropiedad) {
        this.ultimaPropiedad = ultimaPropiedad;
    }
    /**
     * @return Returns the esBusquedaXConsecutivoAnulacion.
     */
    public boolean getEsBusquedaXConsecutivoAnulacion() {
        return esBusquedaXConsecutivoAnulacion;
    }
    /**
     * @param esBusquedaXConsecutivoAnulacion The esBusquedaXConsecutivoAnulacion to set.
     */
    public void setEsBusquedaXConsecutivoAnulacion(
            boolean esBusquedaXConsecutivoAnulacion) {
        this.esBusquedaXConsecutivoAnulacion = esBusquedaXConsecutivoAnulacion;
    }
    /**
     * @return Returns the esBusquedaXConsecutivoFactura.
     */
    public boolean getEsBusquedaXConsecutivoFactura() {
        return esBusquedaXConsecutivoFactura;
    }
    /**
     * @param esBusquedaXConsecutivoFactura The esBusquedaXConsecutivoFactura to set.
     */
    public void setEsBusquedaXConsecutivoFactura(
            boolean esBusquedaXConsecutivoFactura) {
        this.esBusquedaXConsecutivoFactura = esBusquedaXConsecutivoFactura;
    }
	/**
	 * @return Returns the esBusquedaXLoginUsuarioAnulacion.
	 */
	public boolean getEsBusquedaXLoginUsuarioAnulacion() {
		return esBusquedaXLoginUsuarioAnulacion;
	}
	/**
	 * @param esBusquedaXLoginUsuarioAnulacion The esBusquedaXLoginUsuarioAnulacion to set.
	 */
	public void setEsBusquedaXLoginUsuarioAnulacion(
			boolean esBusquedaXLoginUsuarioAnulacion) {
		this.esBusquedaXLoginUsuarioAnulacion = esBusquedaXLoginUsuarioAnulacion;
	}

	/**
	 * @return Returns the codigoCentroAtencion.
	 */
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion The codigoCentroAtencion to set.
	 */
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	/**
	 * @return the mostrarAdvertenciaIngresos
	 */
	public boolean getMostrarAdvertenciaIngresos() {
		return mostrarAdvertenciaIngresos;
	}

	/**
	 * @param mostrarAdvertenciaIngresos the mostrarAdvertenciaIngresos to set
	 */
	public void setMostrarAdvertenciaIngresos(boolean mostrarAdvertenciaIngresos) {
		this.mostrarAdvertenciaIngresos = mostrarAdvertenciaIngresos;
	}

	public int getIndiceConsecutivoCargar() {
		return indiceConsecutivoCargar;
	}

	public void setIndiceConsecutivoCargar(int indiceConsecutivoCargar) {
		this.indiceConsecutivoCargar = indiceConsecutivoCargar;
	}

	public boolean isMostrarInformacion() {
		return mostrarInformacion;
	}

	public void setMostrarInformacion(boolean mostrarInformacion) {
		this.mostrarInformacion = mostrarInformacion;
	}

	public String getEmpresaInstitucion() {
		return empresaInstitucion;
	}

	public void setEmpresaInstitucion(String empresaInstitucion) {
		this.empresaInstitucion = empresaInstitucion;
	}

	/**
	 * @return the descEntidadSubcontratada
	 */
	public String getDescEntidadSubcontratada() {
		return descEntidadSubcontratada;
	}

	/**
	 * @param descEntidadSubcontratada the descEntidadSubcontratada to set
	 */
	public void setDescEntidadSubcontratada(String descEntidadSubcontratada) {
		this.descEntidadSubcontratada = descEntidadSubcontratada;
	}

	/**
	 * @return the facturasMismoConsecutivo
	 */
	public ArrayList<DtoIgualConsecutivoFactura> getFacturasMismoConsecutivo() {
		return facturasMismoConsecutivo;
	}

	/**
	 * @param facturasMismoConsecutivo the facturasMismoConsecutivo to set
	 */
	public void setFacturasMismoConsecutivo(
			ArrayList<DtoIgualConsecutivoFactura> facturasMismoConsecutivo) {
		this.facturasMismoConsecutivo = facturasMismoConsecutivo;
	}

	/**
	 * @return the codigoFactura
	 */
	public BigDecimal getCodigoFactura() {
		return codigoFactura;
	}

	/**
	 * @param codigoFactura the codigoFactura to set
	 */
	public void setCodigoFactura(BigDecimal codigoFactura) {
		this.codigoFactura = codigoFactura;
	}

	/**
	 * @return the esFacturaOdontologica
	 */
	public boolean isEsFacturaOdontologica() {
		return esFacturaOdontologica;
	}

	/**
	 * @return the esFacturaOdontologica
	 */
	public boolean getEsFacturaOdontologica() {
		return esFacturaOdontologica;
	}
	
	/**
	 * @param esFacturaOdontologica the esFacturaOdontologica to set
	 */
	public void setEsFacturaOdontologica(boolean esFacturaOdontologica) {
		this.esFacturaOdontologica = esFacturaOdontologica;
	}

	/**
	 * @return the codigoEstadoCuentaMadre
	 */
	public int getCodigoEstadoCuentaMadre() {
		return codigoEstadoCuentaMadre;
	}

	/**
	 * @param codigoEstadoCuentaMadre the codigoEstadoCuentaMadre to set
	 */
	public void setCodigoEstadoCuentaMadre(int codigoEstadoCuentaMadre) {
		this.codigoEstadoCuentaMadre = codigoEstadoCuentaMadre;
	}

	/**
	 * @return the nombreEstadoCuentaMadre
	 */
	public String getNombreEstadoCuentaMadre() {
		return nombreEstadoCuentaMadre;
	}

	/**
	 * @param nombreEstadoCuentaMadre the nombreEstadoCuentaMadre to set
	 */
	public void setNombreEstadoCuentaMadre(String nombreEstadoCuentaMadre) {
		this.nombreEstadoCuentaMadre = nombreEstadoCuentaMadre;
	}

	/**
	 * @return the ingresoPaciente
	 */
	public int getIngresoPaciente() {
		return ingresoPaciente;
	}

	/**
	 * @param ingresoPaciente the ingresoPaciente to set
	 */
	public void setIngresoPaciente(int ingresoPaciente) {
		this.ingresoPaciente = ingresoPaciente;
	}
}