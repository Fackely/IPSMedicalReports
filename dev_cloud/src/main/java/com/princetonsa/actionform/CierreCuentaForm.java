/*
 * @(#)CierreCuentaForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */
package com.princetonsa.actionform;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ElementoApResource;
import util.UtilidadFecha;

/**
 * Form que contiene todos los datos espec�ficos para generar 
 * el cierre de cuenta.
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validaci�n de errores de datos de entrada.
 * @version 1.0, Mayo 3, 2004
 * @author <a href="mailto:sgomez@PrincetonSA.com">Sebastian Gomez</a>,
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 */
public class CierreCuentaForm extends ValidatorForm
{
	/**
	 * id de la cuenta
	 */
	private int idCuenta;
    
	/**
	 * Fecha apertura de la cuenta
	 */
	private String fechaAperturaCuenta;
	
	/**
	 * Codigo del convenio
	 */
	private int codigoConvenio;
	
	/**
	 * Nombre del Convenio
	 */
	private String nombreConvenio;
	
	/**
	 * Codigo de la via de ingreso
	 */
	private int codigoViaIngreso;
	
	/**
	 * Desc de la via de ingreso
	 */
	private String descripcionViaIngreso;
	
	/**
	 * codigo de la clasificacionSocioEconomica
	 */
	private int codigoClasificacionSocioEconomica;
	
	/**
	 * Descripcion de la clasificacion SocioEconomica
	 */
	private String descripcionClasificacionSocioEconomica;
	
	/**
	 * Acronimo del tipo de afiliacion
	 */
	private String acronimoTipoAfiliado;
	
	/**
	 * Nombre del tipo de afiliacion
	 */
	private String nombreTipoAfiliado;
	
	/**
	 * Codigo del monto de cobro
	 */
	private int codigoMontoCobro;
	
	/**
	 * Nombre del tipo Monto cobro
	 */
	private String nombreMontoCobro;
	
	/**
	 * Acronimo del tipo regimen
	 */
	private String acronimoTipoRegimen;

	/**
	 * Nombre del tipo de regimen
	 */
	private String nombreTipoRegimen;
	
	/**
	 * motivo del cierre de la cuenta
	 */
	private String motivoCierre;
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Objeto usado para manejar los datos generales
	 * de las cuentas del caso asocio
	 */
	private HashMap datosAsocio= new HashMap();
	
	/**
	 * ArrayList para almacenar la lista de entidades subcontratadas 
	 * */
	private ArrayList entidadesSubList;
	
	/**
	 * String para almacenar el codigo de la entidad subcontratada
	 * */
	private String codigoEntidadSub;
	
	/**
	 * Arreglo para almacenar los mensajes de advertencia
	 */
	private ArrayList<ElementoApResource> advertencias = new ArrayList<ElementoApResource>();
	//***********OBJETOS PARA LA CONSULTA DEL CIERRE***********************************************
	
	/**
	 * listado de las cuentas cerradas
	 */
	private HashMap listadoCuentas;
	
	private String indice;
	
	private String ultimoIndice;
	
	/**
	 * par�metros de cosnulta de cierre
	 * 
	 */
	private String fechaCierreInicial;
	private String fechaCierreFinal;
	private int cuentaInicial;
	private int cuentaFinal;
	
	
	/**
	 * Variable para almacenar el centro de atencion de la busqueda de cuentas cerradas por rangos.
	 */
	private int centroAtencion;
	
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
		if(estado.equals("empezar"))
		{				
			errores=super.validate(mapping,request);
			//@todo //Utilizar ValidacionesCierreCuenta	
		}
		else if(estado.equals("cerrar"))
		{
			if(this.motivoCierre.equals(""))
				errores.add("motivo requerido", new ActionMessage("errors.required", "El motivo de cierre"));
			
			if(!errores.isEmpty())
				this.estado="empezar";
		}
		else if(estado.equals("busquedaTodos"))
		{
			
			/** VALIDACI�N DE LOS N�MERO DE CUENTAS **/
			if(this.cuentaInicial==0&&this.cuentaFinal>0)
				errores.add("Id Cuenta Inicial", new ActionMessage("errors.required", "La Cuenta Inicial de Cierre"));
			if(this.cuentaFinal==0&&this.cuentaInicial>0)
				errores.add("Id Cuenta Final", new ActionMessage("errors.required", "La Cuenta Final de Cierre"));
			if(this.cuentaFinal<this.cuentaInicial&&this.cuentaInicial!=0&&this.cuentaFinal!=0)
				errores.add("cuenta final menor", new ActionMessage("errors.debeSerNumeroMayor", "La cuenta final de cierre","la cuenta inicial de cierre"));
				
				
			/** VALIDACI�N DE LAS FECHAS **/
			//indicadores de validaci�n
			int resp1=0;
			int resp2=0;
			
			if(!this.fechaCierreInicial.equals(""))
			{
				resp1=1;
				if(UtilidadFecha.validarFecha(this.getFechaCierreInicial()))
					resp1=2;
				else
					errores.add("fecha cierre inicial", new ActionMessage("errors.formatoFechaInvalido",this.getFechaCierreInicial()));
			}
			
			if(!this.fechaCierreFinal.equals(""))
			{
				resp2=1;
				if(UtilidadFecha.validarFecha(this.getFechaCierreFinal()))
					resp2=2;
				else
					errores.add("fecha cierre final", new ActionMessage("errors.formatoFechaInvalido",this.getFechaCierreFinal()));
			}
			
			//revisar si las fechas son v�lidas
			if(resp1==2&&resp2==2)
			{
				boolean exito = true;
				if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaCierreInicial())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
				{
					errores.add("fecha cierre inicial", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "inicial de cierre", "actual"));
					exito = false;
				}
				
				if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaCierreFinal())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
				{
					errores.add("fecha cierre final", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "final de cierre", "actual"));
					exito = false;
				}
				
				if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaCierreInicial())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaCierreFinal()))>0)
				{
					errores.add("fecha inicial mayor a la fecha final", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "inicial de cierre", "final de cierre"));
					exito = false;
				}
				
				//Si las fechas son correctas se verifica que no se pase en meses
				if(exito)
				{
					if(UtilidadFecha.numeroMesesEntreFechas(this.getFechaCierreInicial(),this.getFechaCierreFinal(),true)>3)
						errores.add("Rango de fechas > a 3 meses",new ActionMessage("error.facturacion.cierreCuenta.rangoMayorTresMeses"));
				}
					
			}
			else
			{
				if(resp1==0&&resp2>0)
				{
					errores.add("La Fecha Cierre Inicial", new ActionMessage("errors.required", "La Fecha Inicial de Cierre"));
				}
				
				if(resp2==0&&resp1>0)
				{
					errores.add("La Fecha Cierre Final", new ActionMessage("errors.required", "La Fecha Final de Cierre"));
				}
			}
			
			/** VALIDACI�N DE TODOS **/
			if(this.cuentaInicial==0&&this.cuentaFinal==0&&this.fechaCierreInicial.equals("")&&this.fechaCierreFinal.equals("")&&this.centroAtencion==0)
			{
				errores.add("Rangos de b�squeda", new ActionMessage("errors.required", "Alguno de los rangos de b�squeda"));
			}
			
		}
		return errores;
	}
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
	    this.idCuenta=ConstantesBD.codigoNuncaValido;
	    this.fechaAperturaCuenta="";
	    this.codigoConvenio=ConstantesBD.codigoNuncaValido;
	    this.nombreConvenio="";
	    this.codigoViaIngreso=ConstantesBD.codigoNuncaValido;
	    this.descripcionViaIngreso="";
	    this.codigoClasificacionSocioEconomica=ConstantesBD.codigoNuncaValido;
	    this.descripcionClasificacionSocioEconomica="";
	    this.acronimoTipoAfiliado="";
	    this.nombreTipoAfiliado="";
	    this.codigoMontoCobro=ConstantesBD.codigoNuncaValido;
	    this.nombreMontoCobro="";
	    this.acronimoTipoRegimen="";
	    this.nombreTipoRegimen="";
	    this.motivoCierre="";
	    this.listadoCuentas=new HashMap();
	    this.fechaCierreFinal="";
	    this.fechaCierreInicial="";
	    this.cuentaInicial=0;
	    this.cuentaFinal=0;
	    this.centroAtencion=0;
	    this.datosAsocio=new HashMap();
	    this.entidadesSubList = new ArrayList();	    
	    this.advertencias = new ArrayList<ElementoApResource>();
	}
	
    /**
     * @return Returns the acronimoTipoAfiliado.
     */
    public String getAcronimoTipoAfiliado() {
        return acronimoTipoAfiliado;
    }
    /**
     * @param acronimoTipoAfiliado The acronimoTipoAfiliado to set.
     */
    public void setAcronimoTipoAfiliado(String acronimoTipoAfiliado) {
        this.acronimoTipoAfiliado = acronimoTipoAfiliado;
    }
    /**
     * @return Returns the acronimoTipoRegimen.
     */
    public String getAcronimoTipoRegimen() {
        return acronimoTipoRegimen;
    }
    /**
     * @param acronimoTipoRegimen The acronimoTipoRegimen to set.
     */
    public void setAcronimoTipoRegimen(String acronimoTipoRegimen) {
        this.acronimoTipoRegimen = acronimoTipoRegimen;
    }
    /**
     * @return Returns the codigoClasificacionSocioEconomica.
     */
    public int getCodigoClasificacionSocioEconomica() {
        return codigoClasificacionSocioEconomica;
    }
    /**
     * @param codigoClasificacionSocioEconomica The codigoClasificacionSocioEconomica to set.
     */
    public void setCodigoClasificacionSocioEconomica(
            int codigoClasificacionSocioEconomica) {
        this.codigoClasificacionSocioEconomica = codigoClasificacionSocioEconomica;
    }
    /**
     * @return Returns the codigoConvenio.
     */
    public int getCodigoConvenio() {
        return codigoConvenio;
    }
    /**
     * @param codigoConvenio The codigoConvenio to set.
     */
    public void setCodigoConvenio(int codigoConvenio) {
        this.codigoConvenio = codigoConvenio;
    }
    /**
     * @return Returns the codigoMontoCobro.
     */
    public int getCodigoMontoCobro() {
        return codigoMontoCobro;
    }
    /**
     * @param codigoMontoCobro The codigoMontoCobro to set.
     */
    public void setCodigoMontoCobro(int codigoMontoCobro) {
        this.codigoMontoCobro = codigoMontoCobro;
    }
    /**
     * @return Returns the codigoViaIngreso.
     */
    public int getCodigoViaIngreso() {
        return codigoViaIngreso;
    }
    /**
     * @param codigoViaIngreso The codigoViaIngreso to set.
     */
    public void setCodigoViaIngreso(int codigoViaIngreso) {
        this.codigoViaIngreso = codigoViaIngreso;
    }
    /**
     * @return Returns the descripcionClasificacionSocioEconomica.
     */
    public String getDescripcionClasificacionSocioEconomica() {
        return descripcionClasificacionSocioEconomica;
    }
    /**
     * @param descripcionClasificacionSocioEconomica The descripcionClasificacionSocioEconomica to set.
     */
    public void setDescripcionClasificacionSocioEconomica(
            String descripcionClasificacionSocioEconomica) {
        this.descripcionClasificacionSocioEconomica = descripcionClasificacionSocioEconomica;
    }
    /**
     * @return Returns the descripcionViaIngreso.
     */
    public String getDescripcionViaIngreso() {
        return descripcionViaIngreso;
    }
    /**
     * @param descripcionViaIngreso The descripcionViaIngreso to set.
     */
    public void setDescripcionViaIngreso(String descripcionViaIngreso) {
        this.descripcionViaIngreso = descripcionViaIngreso;
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
     * @return Returns the fechaAperturaCuenta.
     */
    public String getFechaAperturaCuenta() {
        return fechaAperturaCuenta;
    }
    /**
     * @param fechaAperturaCuenta The fechaAperturaCuenta to set.
     */
    public void setFechaAperturaCuenta(String fechaAperturaCuenta) {
        this.fechaAperturaCuenta = fechaAperturaCuenta;
    }
    /**
     * @return Returns the idCuenta.
     */
    public int getIdCuenta() {
        return idCuenta;
    }
    /**
     * @param idCuenta The idCuenta to set.
     */
    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }
    /**
     * @return Returns the nombreConvenio.
     */
    public String getNombreConvenio() {
        return nombreConvenio;
    }
    /**
     * @param nombreConvenio The nombreConvenio to set.
     */
    public void setNombreConvenio(String nombreConvenio) {
        this.nombreConvenio = nombreConvenio;
    }
    /**
     * @return Returns the nombreMontoCobro.
     */
    public String getNombreMontoCobro() {
        return nombreMontoCobro;
    }
    /**
     * @param nombreMontoCobro The nombreMontoCobro to set.
     */
    public void setNombreMontoCobro(String nombreMontoCobro) {
        this.nombreMontoCobro = nombreMontoCobro;
    }
    /**
     * @return Returns the nombreTipoAfiliado.
     */
    public String getNombreTipoAfiliado() {
        return nombreTipoAfiliado;
    }
    /**
     * @param nombreTipoAfiliado The nombreTipoAfiliado to set.
     */
    public void setNombreTipoAfiliado(String nombreTipoAfiliado) {
        this.nombreTipoAfiliado = nombreTipoAfiliado;
    }
    /**
     * @return Returns the nombreTipoRegimen.
     */
    public String getNombreTipoRegimen() {
        return nombreTipoRegimen;
    }
    /**
     * @param nombreTipoRegimen The nombreTipoRegimen to set.
     */
    public void setNombreTipoRegimen(String nombreTipoRegimen) {
        this.nombreTipoRegimen = nombreTipoRegimen;
    }
	/**
	 * @return Returns the motivoCierre.
	 */
	public String getMotivoCierre() {
		return motivoCierre;
	}
	/**
	 * @param motivoCierre The motivoCierre to set.
	 */
	public void setMotivoCierre(String motivoCierre) {
		this.motivoCierre = motivoCierre;
	}
	/**
	 * @return Returns the listadoCuentas.
	 */
	public HashMap getListadoCuentas() {
		return listadoCuentas;
	}
	/**
	 * @param listadoCuentas The listadoCuentas to set.
	 */
	public void setListadoCuentas(HashMap listadoCuentas) {
		this.listadoCuentas = listadoCuentas;
	}
	/**
	 * @return Returns the indice.
	 */
	public String getIndice() {
		return indice;
	}
	/**
	 * @param indice The indice to set.
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}
	/**
	 * @return Returns the ultimoIndice.
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}
	/**
	 * @param ultimoIndice The ultimoIndice to set.
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}
	/**
	 * @return Returns the cuentaFinal.
	 */
	public int getCuentaFinal() {
		return cuentaFinal;
	}
	/**
	 * @param cuentaFinal The cuentaFinal to set.
	 */
	public void setCuentaFinal(int cuentaFinal) {
		this.cuentaFinal = cuentaFinal;
	}
	/**
	 * @return Returns the cuentaInicial.
	 */
	public int getCuentaInicial() {
		return cuentaInicial;
	}
	/**
	 * @param cuentaInicial The cuentaInicial to set.
	 */
	public void setCuentaInicial(int cuentaInicial) {
		this.cuentaInicial = cuentaInicial;
	}
	/**
	 * @return Returns the fechaCierreFinal.
	 */
	public String getFechaCierreFinal() {
		return fechaCierreFinal;
	}
	/**
	 * @param fechaCierreFinal The fechaCierreFinal to set.
	 */
	public void setFechaCierreFinal(String fechaCierreFinal) {
		this.fechaCierreFinal = fechaCierreFinal;
	}
	/**
	 * @return Returns the fechaCierreInicial.
	 */
	public String getFechaCierreInicial() {
		return fechaCierreInicial;
	}
	/**
	 * @param fechaCierreInicial The fechaCierreInicial to set.
	 */
	public void setFechaCierreInicial(String fechaCierreInicial) {
		this.fechaCierreInicial = fechaCierreInicial;
	}
	/**
	 * @return Returns the datosAsocio.
	 */
	public HashMap getDatosAsocio() {
		return datosAsocio;
	}
	/**
	 * @param datosAsocio The datosAsocio to set.
	 */
	public void setDatosAsocio(HashMap datosAsocio) {
		this.datosAsocio = datosAsocio;
	}
	
	/**
	 * @return Returns the datosAsocio.
	 */
	public Object getDatosAsocio(String key) {
		return datosAsocio.get(key);
	}
	/**
	 * @param datosAsocio The datosAsocio to set.
	 */
	public void setDatosAsocio(String key,Object objeto) {
		this.datosAsocio.put(key,objeto);
	}

	public int getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the entidadesSubList
	 */
	public ArrayList getEntidadesSubList() {
		return entidadesSubList;
	}

	/**
	 * @param entidadesSubList the entidadesSubList to set
	 */
	public void setEntidadesSubList(ArrayList entidadesSubList) {
		this.entidadesSubList = entidadesSubList;
	}

	/**
	 * @return the codigoEntidadSub
	 */
	public String getCodigoEntidadSub() {
		return codigoEntidadSub;
	}

	/**
	 * @param codigoEntidadSub the codigoEntidadSub to set
	 */
	public void setCodigoEntidadSub(String codigoEntidadSub) {
		this.codigoEntidadSub = codigoEntidadSub;
	}

	/**
	 * @return the advertencias
	 */
	public ArrayList<ElementoApResource> getAdvertencias() {
		return advertencias;
	}

	/**
	 * @param advertencias the advertencias to set
	 */
	public void setAdvertencias(ArrayList<ElementoApResource> advertencias) {
		this.advertencias = advertencias;
	}
}
