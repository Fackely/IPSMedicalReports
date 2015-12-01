
/*
 * Creado   Jun 17 2005
 * Modificado Sep 28 2005
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.actionform.tesoreria;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.ResultadoBoolean;

/**
 * @author Angela Bibiana Cardona
 * @author Sebastián Gómez R.
 * 
 * Form que contiene todos los datos específicos 
 * para generar la consulta de conceptos de ingreso tesoreria
 * Y adicionalmente hace el manejo de <code>reset</code> 
 * de la forma y la validación <code>validate</code> 
 * de errores de datos de entrada.
 *
 * 
 */

public class ConceptoTesoreriaForm extends ActionForm
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ConceptoTesoreriaForm.class);
	
	
	/**
     * manejo de estados de la forma
     */
    private String estado;
    
    /**
	 * Variable usada para almacenar la posicion del mapa donde
	 * se encuentra ubicado el registro
	 */
    private int pos;
    
    /**
	 * Cadena que almacena información de los registros utilizados en el
	 * sistema que no pueden ser modificados o eliminados
	 */
	private String registrosUsados;
    
    
    /**
     * para almacenar todos los
     * datos correspondientes al formulario.
     * estados de los datos.
     */
    private HashMap mapConceptos;
    
   
    
    /**
     * para la nevagación del pager, cuando se ingresa
     * un registro nuevo.
     */
    private String linkSiguiente;
    
   
    /**
     * para controlar el numero de registros del
     * HashMap.
     */
    private int numRegistros;
    
    /**
     * para controlar la página actual
     * del pager.
     */
    private int offsetHash;
    
    /**
     * Objeto usado para almacenar los criterios de la búsqueda avanzada
     */
    private HashMap mapBusqueda=new HashMap();
    
    /**
     * Parámetros usados para la ordenación
     */
    private String indice;
    private String ultimoIndice;
    
    /**
     * Variable que indica si el registro es modificable o no
     */
    private boolean esModificar;
    
    /**
     * Variable que indica si se va a ingresar o modificar concepto ingreso tesoreria
     */
    private boolean esInsertar;
    
    /**
     * Atributos que representan un registro de un concepto de ingreso de tesoreria
     */
    private String codigo;
    private String codigoDetalle;
    private String descripcion;
    private int codigoTipoPago;
    private String nombreTipoPago;
    private String codigoFiltroValor;
    private String cuenta;
    private int codigoDocumentoIngreso;
    private int codigoDocumentoAnulacion;
    private int codigoCentroCosto;
    private int nit;
    private boolean activo;
    
    private String rubroPresupuestal;
    
    /**
     * Arreglos para almacenar los tipos de datos
     */
    private Collection tiposPago;
    private Collection tiposDocContabilidad;
    private Collection centrosCosto;
    private Collection terceros;
    
    public ResultadoBoolean mostrarMensaje;
    
    //Boolean ara determinar si se peude modificar el concepto si éste no está ya asociaro a un RC
    private boolean poseeRC; 
    
    public void reset ()
    {
      
       this.mapConceptos = new HashMap ();
       this.mapBusqueda = new HashMap ();
       this.linkSiguiente = "";
       this.numRegistros = 0;
       this.offsetHash = 0;
       this.estado="";
       this.pos=-1;
       this.registrosUsados="";
       this.indice = "";
       this.ultimoIndice = "";
       this.rubroPresupuestal="";
       this.resetDetalle();
       this.poseeRC=false;
       this.mostrarMensaje=new ResultadoBoolean(false);
    }
    
    public void resetDetalle()
    {
    	this.esModificar = false;
    	this.esInsertar = false;
    	this.codigoDetalle = "";
    	this.codigo = "";
    	this.descripcion = "";
    	this.codigoTipoPago = 0;
    	this.nombreTipoPago = "";
    	this.codigoFiltroValor = "";
        this.cuenta = "";
        this.codigoDocumentoIngreso = 0;
        this.codigoDocumentoAnulacion = 0;
        this.codigoCentroCosto = 0;
        this.nit = ConstantesBD.codigoNuncaValido;
        this.activo = true;
    	
    	this.tiposPago = new ArrayList();
    	this.tiposDocContabilidad = new ArrayList();
    	this.centrosCosto = new ArrayList();
    	this.terceros = new ArrayList();
    	this.mostrarMensaje=new ResultadoBoolean(false);
    	
    }
    
    public ResultadoBoolean getMostrarMensaje() {
		return mostrarMensaje;
	}

	public void setMostrarMensaje(ResultadoBoolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}

	/**
	 * Metodo de validación
	 * @param mapping
	 * @param request
	 * @return errores ActionError, especifica los errores.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		if(estado.equals("guardarModificar")||estado.equals("guardarInsertar"))
		{
			if(this.codigo.equals(""))
				errores.add("es requerido", new ActionMessage("errors.required","El código"));
			if(this.descripcion.equals(""))
				errores.add("es requerido", new ActionMessage("errors.required","La descripción"));
			//las opciones que aparecen en el select de tipo de pago ninguna es menor a 0
			//por tal motivo se elimina esta validacion.
			//tarea No.39077
			//if(this.codigoTipoPago<0)
				//errores.add("es requerido", new ActionMessage("errors.required","El tipo de pago"));
			
			
		}
		return errores;
	}
	
	/**
	 * Método que revisa si el registro ya fue verificado
	 * @param pos
	 * @param posiciones
	 * @return
	 */
	private boolean yaFueVerificado(int pos, String posiciones) {
		boolean verificado=false;
		if(posiciones.length()>0)
		{
			String vector[]=posiciones.split(ConstantesBD.separadorSplit);
			for(int i=0;i<vector.length;i++)
			{
				//se compara si el registro actual es igual a alguno de los antes
				//verificados y considerados como repetidos
				if((this.mapConceptos.get("codigo_"+pos)+"").compareTo(this.mapConceptos.get("codigo_"+Integer.parseInt(vector[i]))+"")==0)
					verificado=true;
			}
		}
		
		return verificado;
	}
	
    /**
     * @return Retorna estado.
     */
    public String getEstado() {
        return estado;
    }
    /**
     * @param estado Asigna estado.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    
	/**
     * @return Retorna mapConceptos.
     */
    public HashMap getMapConceptos() {
        return mapConceptos;
    }
    /**
     * @param mapConceptos Asigna mapConceptos.
     */
    public void setMapConceptos(HashMap mapConceptos) {
        this.mapConceptos = mapConceptos;
    }
    /**
     * @param key String.
     * @return Object.
     */
    public Object getMapConceptos(String key) {
        return mapConceptos.get(key);
    }
    /**
     * @param key String.
     * @param value Object.
     */
    public void setMapConceptos(String key,Object value) {
        this.mapConceptos.put(key,value);
    }
        
  
   
    /**
     * @return Retorna linkSiguiente.
     */
    public String getLinkSiguiente() {
        return linkSiguiente;
    }
    /**
     * @param linkSiguiente Asigna linkSiguiente.
     */
    public void setLinkSiguiente(String linkSiguiente) {
        this.linkSiguiente = linkSiguiente;
    }
      
    /**
     * @return Retorna numRegistros.
     */
    public int getNumRegistros() {
        return numRegistros;
    }
    /**
     * @param numRegistros Asigna numRegistros.
     */
    public void setNumRegistros(int numRegistros) {
        this.numRegistros = numRegistros;
    }
    /**
     * @return Retorna offset.
     */
    public int getOffsetHash() {
        return offsetHash;
    }
    /**
     * @param offset Asigna offset.
     */
    public void setOffsetHash(int offsetHash) {
        this.offsetHash = offsetHash;
    }
    
    
	/**
	 * @return Returns the pos.
	 */
	public int getPos() {
		return pos;
	}
	/**
	 * @param pos The pos to set.
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}
	/**
	 * @return Returns the registrosUsados.
	 */
	public String getRegistrosUsados() {
		return registrosUsados;
	}
	/**
	 * @param registrosUsados The registrosUsados to set.
	 */
	public void setRegistrosUsados(String registrosUsados) {
		this.registrosUsados = registrosUsados;
	}
	/**
	 * @return Returns the mapBusqueda.
	 */
	public HashMap getMapBusqueda() {
		return mapBusqueda;
	}
	/**
	 * @param mapBusqueda The mapBusqueda to set.
	 */
	public void setMapBusqueda(HashMap mapBusqueda) {
		this.mapBusqueda = mapBusqueda;
	}
	/**
	 * @return Retorna un elemento del mapBusqueda.
	 */
	public Object getMapBusqueda(String key) {
		return mapBusqueda.get(key);
	}
	/**
	 * @param asigna un elemento al mapBusqueda
	 */
	public void setMapBusqueda(String key,Object obj) {
		this.mapBusqueda.put(key,obj);
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
	 * @return Returns the codigo.
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return Returns the codigoTipoPago.
	 */
	public int getCodigoTipoPago() {
		return codigoTipoPago;
	}

	/**
	 * @param codigoTipoPago The codigoTipoPago to set.
	 */
	public void setCodigoTipoPago(int codigoTipoPago) {
		this.codigoTipoPago = codigoTipoPago;
	}

	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return Returns the esModificar.
	 */
	public boolean isEsModificar() {
		return esModificar;
	}

	/**
	 * @param esModificar The esModificar to set.
	 */
	public void setEsModificar(boolean esModificar) {
		this.esModificar = esModificar;
	}

	/**
	 * @return Returns the nombreTipoPago.
	 */
	public String getNombreTipoPago() {
		return nombreTipoPago;
	}

	/**
	 * @param nombreTipoPago The nombreTipoPago to set.
	 */
	public void setNombreTipoPago(String nombreTipoPago) {
		this.nombreTipoPago = nombreTipoPago;
	}

	/**
	 * @return Returns the tiposPago.
	 */
	public Collection getTiposPago() {
		return tiposPago;
	}

	/**
	 * @param tiposPago The tiposPago to set.
	 */
	public void setTiposPago(Collection tiposPago) {
		this.tiposPago = tiposPago;
	}

	/**
	 * @return Returns the codigoDocumentoIngreso.
	 */
	public int getCodigoDocumentoIngreso() {
		return codigoDocumentoIngreso;
	}

	/**
	 * @param codigoDocumentoIngreso The codigoDocumentoIngreso to set.
	 */
	public void setCodigoDocumentoIngreso(int codigoDocumentoIngreso) {
		this.codigoDocumentoIngreso = codigoDocumentoIngreso;
	}

	/**
	 * @return Returns the codigoFiltroValor.
	 */
	public String getCodigoFiltroValor() {
		return codigoFiltroValor;
	}

	/**
	 * @param codigoFiltroValor The codigoFiltroValor to set.
	 */
	public void setCodigoFiltroValor(String codigoFiltroValor) {
		this.codigoFiltroValor = codigoFiltroValor;
	}

	/**
	 * @return Returns the cuenta.
	 */
	public String getCuenta() {
		return cuenta;
	}

	/**
	 * @param cuenta The cuenta to set.
	 */
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	/**
	 * @return Returns the tiposDocContabilidad.
	 */
	public Collection getTiposDocContabilidad() {
		return tiposDocContabilidad;
	}

	/**
	 * @param tiposDocContabilidad The tiposDocContabilidad to set.
	 */
	public void setTiposDocContabilidad(Collection tiposDocContabilidad) {
		this.tiposDocContabilidad = tiposDocContabilidad;
	}

	/**
	 * @return Returns the codigoDocumentoAnulacion.
	 */
	public int getCodigoDocumentoAnulacion() {
		return codigoDocumentoAnulacion;
	}

	/**
	 * @param codigoDocumentoAnulacion The codigoDocumentoAnulacion to set.
	 */
	public void setCodigoDocumentoAnulacion(int codigoDocumentoAnulacion) {
		this.codigoDocumentoAnulacion = codigoDocumentoAnulacion;
	}

	/**
	 * @return Returns the centrosCosto.
	 */
	public Collection getCentrosCosto() {
		return centrosCosto;
	}

	/**
	 * @param centrosCosto The centrosCosto to set.
	 */
	public void setCentrosCosto(Collection centrosCosto) {
		this.centrosCosto = centrosCosto;
	}

	/**
	 * @return Returns the codigoCentroCosto.
	 */
	public int getCodigoCentroCosto() {
		return codigoCentroCosto;
	}

	/**
	 * @param codigoCentroCosto The codigoCentroCosto to set.
	 */
	public void setCodigoCentroCosto(int codigoCentroCosto) {
		this.codigoCentroCosto = codigoCentroCosto;
	}

	/**
	 * @return Returns the nit.
	 */
	public int getNit() {
		return nit;
	}

	/**
	 * @param nit The nit to set.
	 */
	public void setNit(int nit) {
		this.nit = nit;
	}

	/**
	 * @return Returns the terceros.
	 */
	public Collection getTerceros() {
		return terceros;
	}

	/**
	 * @param terceros The terceros to set.
	 */
	public void setTerceros(Collection terceros) {
		this.terceros = terceros;
	}

	/**
	 * @return Returns the activo.
	 */
	public boolean isActivo() {
		return activo;
	}

	/**
	 * @param activo The activo to set.
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	/**
	 * 
	 * @return
	 */
	public String getCodigoDetalle() {
		return codigoDetalle;
	}

	/**
	 * 
	 * @param codigoDetalle
	 */
	public void setCodigoDetalle(String codigoDetalle) {
		this.codigoDetalle = codigoDetalle;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEsInsertar() {
		return esInsertar;
	}

	/**
	 * 
	 * @param esInsertar
	 */
	public void setEsInsertar(boolean esInsertar) {
		this.esInsertar = esInsertar;
	}

	public String getRubroPresupuestal() {
		return rubroPresupuestal;
	}

	public void setRubroPresupuestal(String rubroPresupuestal) {
		this.rubroPresupuestal = rubroPresupuestal;
	}

	public boolean isPoseeRC() {
		return poseeRC;
	}

	public void setPoseeRC(boolean poseeRC) {
		this.poseeRC = poseeRC;
	}	
}