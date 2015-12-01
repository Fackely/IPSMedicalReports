
/*
 * Creado   14/10/2004
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.actionform.cargos;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * ActionForm, contiene todoslos datos generales de un recargo de una tarifa. 
 * Y adicionalmente hace el manejo de reset de la forma y de validación de errores de datos. 
 *
 * @version 1.0, 17/08/2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Lopez</a>
 */
public class RecargoTarifasForm extends ActionForm {
    
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Almacena el codigo del recargo.
	 */
	private int codigoRecargo;
	
	/**
	 * Almacena el codigo del convenio
	 */
	private int codigoConvenio;
	
	/**
	 * Almacana el codigo del contrato
	 */
	private int codigoContrato;
	
	/**
	 * Almacena el codigo de la Via de Ingreso
	 */
	private int codigoViaIngreso;
	
	/**
	 * Almacena el codigo del tipo de recargo
	 */
	private int codigoTipoRecargo;
	
	/**
	 * Almacena el codigo del servicio
	 */
	private int codigoServicio;
	
	/**
	 * Almacena el codigo de la especialidad
	 */
	private int codigoEspecialidad;
	
	/**
	 * Porcentaje de recargo
	 */
	private double porcentaje;
	
	/**
	 * Valor del recargo
	 */
	private double valor;
	
	/**
	 * Estado del radiobutton del servicio
	 */
	private int servicioRadio;
	
	/**
	 * Estado del radiobutton de la especialidad.
	 */
	private int especialidadRadio;
	
	/**
	 * Almacena el nombre de la especialidad.
	 */
	private String nombreEspecialidad;
	
	/**
	 * Almacena el nombre del servicio.
	 */
	private String nombreServicio;
	
	/**
	 * Estado del radio de recargo.
	 */
	private int recargoRadio;
	
	/**
	 * Almacena el signo del porcentaje;
	 */
	private char signoPorcentaje;
	
	/**
	 * Almacen los datos de la consulta de Recargos para el resumen 
	 * despues de insertar.
	 */
	private Collection coleccionRecargo;
	
	/**
	 * Almacen los datos de la consulta Avanzada de Recargos  para modificar
	 */
	private Collection coleccionAvanzadaModificar;
	
	/**
	 * Almacena todos los datos de los registros existentes 
	 * de recargos para validar si ya existe.
	 */
	private Collection coleccionTodos;
	
	/**
	 * Almacen los datos de la consulta Avanzada de Recargos  para consultar
	 */
	private Collection coleccionAvanzadaConsultar;
	
	/**
	 * HashMap de Recargos
	 */
	private HashMap hashRecargos;
	
	/**
	 * true si existe recargo.
	 */
	private boolean existeRecargo;
	
	/**
	 * Almacena el numero de registros obtnidos de la consulta Avanzada
	 */
	private int numeroRegistrosRecargo;
	
	/**
	 * Almacena el estado del checkBox de Servicios
	 */
	private String checkServicio;
	
	/**
	 * Almacena el estado del checkBox de Especialidad.
	 */
	private String checkEspecialidad;
	
	/**
	 * Almacena el link de la pag siguiente del formulario
	 * para que no borre los datos en el pager.
	 */
	private String linkSiguiente;
	
	/**
	 * Códigos de los recargos que han sido modificados
	 * mostrarlos en el resumen
	 */
	public Vector codigosRecargosModificados = new Vector();
	
	/**
	 * Almacena el string de los logs de eliminacion o modificacion.
	 */
	public String log;
	
	/**
	 * Alamacena el tipo de paciente
	 */
	private String tipoPaciente;
	
	
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
		
		if( estado.equals("empezarInsertar") || estado.equals("buscarModificar") || estado.equals("empezarConsultar"))
		{
			// Contrato requerido
			if( this.codigoContrato <= 0 )
			{
				errores.add("contrato requerido", new ActionMessage("errors.required", "El contrato"));
			}
		}
		
	    if(this.estado.equals("salirGuardar"))
		{
		    if(this.codigoConvenio==-1 || this.codigoConvenio==0)
		    {
				errores.add("", new ActionMessage("errors.required", "El campo Convenio"));
			}
		    
		    if(this.codigoContrato==-1 || this.codigoContrato==0)
		    {
				errores.add("", new ActionMessage("errors.required", "El campo Contrato"));
			}
		    
		    if(this.codigoTipoRecargo==-1||codigoTipoRecargo==0)
		    {
				errores.add("", new ActionMessage("errors.required", "El campo Tipo de Recargo"));
			}
		    
		    if(this.codigoViaIngreso==-1)
		    {
				errores.add("", new ActionMessage("errors.required", "El campo Vía de Ingreso"));
			}
		    
		    if(this.servicioRadio!=-1)
		    {
				if(this.servicioRadio==0)
				{
					if(this.codigoServicio==-1)
					{
						errores.add("", new ActionMessage("errors.required", "El campo Servicio"));
					}
				}
				else
				{
					this.codigoServicio=0;
				}
			}
		    else
		    {
				errores.add("", new ActionMessage("errors.required", "El campo Servicio"));
			}
		    
		    if(this.especialidadRadio!=-1)
		    {
				if(this.especialidadRadio==0)
				{
					if(this.codigoEspecialidad==-1)
					{
						errores.add("", new ActionMessage("errors.required", "El campo Especialidad"));
					}
				}
				else
				{
					this.codigoEspecialidad=0;
				}
			}
		    else
		    {
				errores.add("", new ActionMessage("errors.required", "El campo Especialidad"));
			}
		    
		    if(this.recargoRadio!=-1)
		    {
				if(this.recargoRadio==0)
				{
					if(this.porcentaje<=0)
					{
						errores.add("", new ActionMessage("errors.floatMayorQue", "El campo Porcentaje","cero"));				
					}
					if(this.signoPorcentaje=='-')
					{
						this.porcentaje=-this.porcentaje;
					}
				}
				else if(this.recargoRadio==1)
				{
					if(this.valor<=0 )
					{
						errores.add("", new ActionMessage("errors.floatMayorQue", "El campo Valor","cero"));				
					}
				}
			}
		    else
		    {
				errores.add("recargo requerido", new ActionMessage("errors.required", "El Recargo (Porcentaje o Valor)"));
			}
		    
			if(this.codigoViaIngreso>0&&this.tipoPaciente.equals("-1"))
			{
				errores.add("Tipo Paciente ",new ActionMessage("errors.required"," Tipo Paciente "));
			}
		    
		    
		}
		
	    if(estado.equals("busquedaAvanzadaConsultar") || estado.equals("busquedaAvanzadaModificar"))
	    {
	      if(this.checkEspecialidad.equals("on"))
	      {
		        try
		        {
		            if(Integer.parseInt(this.getNombreEspecialidad()) < 0)
		       		{
		       			errores.add("numero menor/igual que 0", new ActionMessage("errors.integerMayorIgualQue","El codigo de la  Especialidad ","0")); 
		       			this.checkEspecialidad="off";
		       		}
		        }
		        
		        catch(NumberFormatException e)
		        {
			           errores.add("numeroNoEntero", new ActionMessage("errors.integer", "La busqueda de Especialidad por codigo "));
			           this.checkEspecialidad="off";
			    }
	      }
	      
	     
	      if(this.checkServicio.equals("on"))
	      {
		        try
		        {
		            if(Integer.parseInt(this.getNombreServicio()) < 0)
		       		{
		       			errores.add("numero menor/igual que 0", new ActionMessage("errors.integerMayorIgualQue","El codigo del Servicio ","0")); 
		       			this.checkServicio="off";
		       		}
		        }
		        
		        catch(NumberFormatException e)
		        {
			           errores.add("numeroNoEntero", new ActionMessage("errors.integer", "La busqueda de Servicio por codigo "));
			           this.checkServicio="off";
			    }
	      }
	    }
	    
		return errores;
		
	}
	
	
	/**
	 * Método que inicializa todos los atributos de la forma
	 */
    public void reset(boolean todo, boolean convenioContrato,boolean datos, boolean checks)
	{
	    if(todo)
	    {
	        this.codigoViaIngreso=-1;
		    this.codigoTipoRecargo=0;
		    this.codigoServicio=-1;
		    this.codigoEspecialidad=-1;
		    this.porcentaje=0.0;
		    this.valor=0.0;
		    this.servicioRadio=0;
		    this.especialidadRadio=0;
		    this.nombreEspecialidad="";
		    this.nombreServicio="";
		    this.recargoRadio=0;
		    this.signoPorcentaje='+';
		    this.numeroRegistrosRecargo=0;
		    this.linkSiguiente="";
		    this.codigoRecargo=0;
		    this.log="";
		      
		 }
	    
	    if(convenioContrato)
	    {
	        this.codigoConvenio=0;
		    this.codigoContrato=0;
	    }
	    
	    if(datos)
	    {
	        this.coleccionRecargo= null;
		    this.coleccionAvanzadaModificar = null;
		    this.coleccionTodos=null;
		    this.hashRecargos=new HashMap ();	
		    this.coleccionAvanzadaConsultar=null;
		    this.codigosRecargosModificados = new Vector ();
	    }
	    
	    if(checks)
	    {
	        this.checkServicio="";
		    this.checkEspecialidad="";
	    }
	    	    
	}

    /**
     * Reset especialmente para la accion de consultar/imprimir
     *
     */
    public void resetConsulta(boolean esp, boolean ser,boolean noLimpiar)
    {
        this.codigoViaIngreso=0;
	    this.codigoTipoRecargo=0;
	    this.servicioRadio=0;
	    this.especialidadRadio=0;
	    this.recargoRadio=0;
	    this.codigoEspecialidad=-1;
	    this.codigoServicio=-1;
	    this.codigoRecargo=0;
	    this.codigosRecargosModificados = new Vector ();
	    
	    if(esp)
	       this.nombreEspecialidad="";
	       	        
	    if(ser)
	       this.nombreServicio="";
		
	    if(noLimpiar)
	    {
	        this.porcentaje=0.0;
		    this.valor=0.0;
	    }
	        
    }
   
    /**
     * @return Retorna especialidadRadio.
     */
    public int getEspecialidadRadio() {
        return especialidadRadio;
    }
    /**
     * @param especialidadRadio Asigna especialidadRadio.
     */
    public void setEspecialidadRadio(int especialidadRadio) {
        this.especialidadRadio = especialidadRadio;
    }
    /**
     * @return Retorna codigoContrato.
     */
    public int getCodigoContrato() {
        return codigoContrato;
    }
    /**
     * @param codigoContrato Asigna codigoContrato.
     */
    public void setCodigoContrato(int codigoContrato) {
        this.codigoContrato = codigoContrato;
    }
    /**
     * @return Retorna codigoConvenio.
     */
    public int getCodigoConvenio() {
        return codigoConvenio;
    }
    /**
     * @param codigoConvenio Asigna codigoConvenio.
     */
    public void setCodigoConvenio(int codigoConvenio) {
        this.codigoConvenio = codigoConvenio;
    }
    /**
     * @return Retorna codigoEspecialidad.
     */
    public int getCodigoEspecialidad() {
        return codigoEspecialidad;
    }
    /**
     * @param codigoEspecialidad Asigna codigoEspecialidad.
     */
    public void setCodigoEspecialidad(int codigoEspecialidad) {
        this.codigoEspecialidad = codigoEspecialidad;
    }
    /**
     * @return Retorna codigoServicio.
     */
    public int getCodigoServicio() {
        return codigoServicio;
    }
    /**
     * @param codigoServicio Asigna codigoServicio.
     */
    public void setCodigoServicio(int codigoServicio) {
        this.codigoServicio = codigoServicio;
    }
    /**
     * @return Retorna codigoTipoRecargo.
     */
    public int getCodigoTipoRecargo() {
        return codigoTipoRecargo;
    }
    /**
     * @param codigoTipoRecargo Asigna codigoTipoRecargo.
     */
    public void setCodigoTipoRecargo(int codigoTipoRecargo) {
        this.codigoTipoRecargo = codigoTipoRecargo;
    }
    /**
     * @return Retorna codigoViaIngreso.
     */
    public int getCodigoViaIngreso() {
        return codigoViaIngreso;
    }
    /**
     * @param codigoViaIngreso Asigna codigoViaIngreso.
     */
    public void setCodigoViaIngreso(int codigoViaIngreso) {
        this.codigoViaIngreso = codigoViaIngreso;
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
     * @return Retorna porcentaje.
     */
    public double getPorcentaje() {
        return porcentaje;
    }
    /**
     * @param porcentaje Asigna porcentaje.
     */
    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }
    /**
     * @return Retorna servicioRadio.
     */
    public int getServicioRadio() {
        return servicioRadio;
    }
    /**
     * @param servicioRadio Asigna servicioRadio.
     */
    public void setServicioRadio(int servicioRadio) {
        this.servicioRadio = servicioRadio;
    }
    /**
     * @return Retorna valor.
     */
    public double getValor() {
        return valor;
    }
    /**
     * @param valor Asigna valor.
     */
    public void setValor(double valor) {
        this.valor = valor;
    }
    /**
     * @return Retorna nombreEspecialidad.
     */
    public String getNombreEspecialidad() {
        return nombreEspecialidad;
    }
    /**
     * @param nombreEspecialidad Asigna nombreEspecialidad.
     */
    public void setNombreEspecialidad(String nombreEspecialidad) {
        this.nombreEspecialidad = nombreEspecialidad;
    }
    
    /**
     * @return Retorna nombreServicio.
     */
    public String getNombreServicio() {
        return nombreServicio;
    }
    /**
     * @param nombreServicio Asigna nombreServicio.
     */
    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }
    /**
     * @return Retorna recargoRadio.
     */
    public int getRecargoRadio() {
        return recargoRadio;
    }
    /**
     * @param recargoRadio Asigna recargoRadio.
     */
    public void setRecargoRadio(int recargoRadio) {
        this.recargoRadio = recargoRadio;
    }
    /**
     * @return Retorna signoPorcentaje.
     */
    public char getSignoPorcentaje() {
        return signoPorcentaje;
    }
    /**
     * @param signoPorcentaje Asigna signoPorcentaje.
     */
    public void setSignoPorcentaje(char signoPorcentaje) {
        this.signoPorcentaje = signoPorcentaje;
    }
    /**
     * @return Retorna coleccionRecargo.
     */
    public Collection getColeccionRecargo() {
        return coleccionRecargo;
    }
    /**
     * @param coleccionRecargo Asigna coleccionRecargo.
     */
    public void setColeccionRecargo(Collection coleccionRecargo) {
        this.coleccionRecargo = coleccionRecargo;
    }
    /**
     * @return Retorna coleccionAvanzada.
     */
    public Collection getColeccionAvanzadaModificar() {
        return coleccionAvanzadaModificar;
    }
    /**
     * @param coleccionAvanzada Asigna coleccionAvanzada.
     */
    public void setColeccionAvanzadaModificar(Collection coleccionAvanzadaModificar) {
        this.coleccionAvanzadaModificar = coleccionAvanzadaModificar;
    }
    /**
     * @return Retorna coleccionTodos.
     */
    public Collection getColeccionTodos() {
        return coleccionTodos;
    }
    /**
     * @param coleccionTodos Asigna coleccionTodos.
     */
    public void setColeccionTodos(Collection coleccionTodos) {
        this.coleccionTodos = coleccionTodos;
    }
    /**
     * @return Retorna hashRecargos.
     */
    public HashMap getHashRecargos() {
        return hashRecargos;
    }
    /**
     * @param hashRecargos Asigna hashRecargos.
     */
    public void setHashRecargos(HashMap hashRecargos) {
        this.hashRecargos = hashRecargos;
    }
    
    /**
	 * Set del mapa de recargos
	 * @param key
	 * @param value
	 */

	public void setHashRecargos(String key, Object value) 
	{
		hashRecargos.put(key, value);
	}

	/**
	 * Get del mapa de recargos
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getHashRecargos(String key) 
	{
		return hashRecargos.get(key);
	}

    /**
     * @return Retorna existeRecargo.
     */
    public boolean isExisteRecargo() {
        return existeRecargo;
    }
    /**
     * @param existeRecargo Asigna existeRecargo.
     */
    public void setExisteRecargo(boolean existeRecargo) {
        this.existeRecargo = existeRecargo;
    }
    /**
     * @return Retorna numeroRegistrosRecargo.
     */
    public int getNumeroRegistrosRecargo() {
        return numeroRegistrosRecargo;
    }
    /**
     * @param numeroRegistrosRecargo Asigna numeroRegistrosRecargo.
     */
    public void setNumeroRegistrosRecargo(int numeroRegistrosRecargo) {
        this.numeroRegistrosRecargo = numeroRegistrosRecargo;
    }
    /**
     * @return Retorna checkEspecialidad.
     */
    public String getCheckEspecialidad() {
        return checkEspecialidad;
    }
    /**
     * @param checkEspecialidad Asigna checkEspecialidad.
     */
    public void setCheckEspecialidad(String checkEspecialidad) {
        this.checkEspecialidad = checkEspecialidad;
    }
    /**
     * @return Retorna checkServicio.
     */
    public String getCheckServicio() {
        return checkServicio;
    }
    /**
     * @param checkServicio Asigna checkServicio.
     */
    public void setCheckServicio(String checkServicio) {
        this.checkServicio = checkServicio;
    }
    /**
     * @return Retorna coleccionAvanzadaConsultar.
     */
    public Collection getColeccionAvanzadaConsultar() {
        return coleccionAvanzadaConsultar;
    }
    /**
     * @param coleccionAvanzadaConsultar Asigna coleccionAvanzadaConsultar.
     */
    public void setColeccionAvanzadaConsultar(
            Collection coleccionAvanzadaConsultar) {
        this.coleccionAvanzadaConsultar = coleccionAvanzadaConsultar;
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
     * @return Retorna codigoRecargo.
     */
    public int getCodigoRecargo() {
        return codigoRecargo;
    }
    /**
     * @param codigoRecargo Asigna codigoRecargo.
     */
    public void setCodigoRecargo(int codigoRecargo) {
        this.codigoRecargo = codigoRecargo;
    }
    
    /**
	 * @return Returns the codigosExcepcionesModificadas.
	 */
	public Vector getCodigosRecargosModificados() {
		return codigosRecargosModificados;
	}
	
	/**
	 * @param codigosExcepcionesModificadas The codigosExcepcionesModificadas to set.
	 */
	public void setCodigosRecargosModificados(Vector codigosRecargosModificados) {
		this.codigosRecargosModificados = codigosRecargosModificados;
	}
	
	/**
	 * Set del vector de codigos excepciones modificadas
	 * @param key
	 * @param value
	 */
	public void setCodigosRecargosModificados(int index, Object value) 
	{
	    codigosRecargosModificados.add(index, value);
	}
	
	/**
	 * Get del vector de excepciones modificadas
	 * Retorna el valor de un campo dado su index
	 */
	public Object getCodigosRecargosModificados(int  index) 
	{
		return codigosRecargosModificados.get(index);
	}

    /**
     * @return Retorna log.
     */
    public String getLog() {
        return log;
    }
    /**
     * @param log Asigna log.
     */
    public void setLog(String log) {
        this.log = log;
    }


	/**
	 * @return the tipoPaciente
	 */
	public String getTipoPaciente() {
		return tipoPaciente;
	}


	/**
	 * @param tipoPaciente the tipoPaciente to set
	 */
	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}
}
