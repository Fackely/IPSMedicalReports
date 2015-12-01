/*
 * Creado el 19-may-2004
 *
 * AXIOMA
 *
 * Autor: Juan David Ramírez
 * juan@princetonSA.com
 * 
 * EsquemasTarifariosForm.java
 */
package com.princetonsa.actionform.cargos;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.Utilidades;

/**
 * @author Juan David Ramírez
 *
 * juan@princetonSA.com
 */
public class EsquemasTarifariosForm extends ActionForm
{
	/**
	 * Manejo del estado del flujo
	 */
	private String estado;
	
	/**
	 * Manejo a segundo nivel del estado
	 * del flujo
	 */
	private String accionAFinalizar;

	/**
	 * Código del esquema tarifario
	 */
	private int codigo;
	
	/**
	 * Nombre del esquema tarifario
	 */
	private String nombre;
	
	/**
	 * Código del tarifario Oficial de este esquema 
	 * tarifario
	 */
	private int tarifarioOficial;
	
	/**
	 * Nombre del tarifario Oficial de este esquema 
	 * tarifario
	 */
	private String nombreTarifarioOficial;
	
	/**
	 * Acrónimo del método de ajuste de este 
	 * esquema tarifario
	 */
	private char metodoAjuste;
	
	/**
	 * Nombre del método de ajuste de este 
	 * esquema tarifario
	 */
	private String nombreMetodoAjuste;
	
	/**
	 * Boolean que indica si este esquema tarifario
	 * es de inventario o no
	 */
	private boolean esInventario;
	
	/**
	 * Boolean que indica si el esquema tarifario
	 * está activo o no
	 */
	private boolean activo;
	
	/**
	 * Si es ISS almacena UVRs
	 * Si es SOAT almacena Salario Mínimo
	 */
	private float cantidad;
	private float cantidadAux; //auxiliar para la modificacion
	
	/**
	 * Manejo de checbox
	 */
	private HashMap map;
	
	/**
	 * Manejo de resultados de la busqueda
	 */
	private Collection resultadosBusqueda;
	
	/**
	 * String para esInventario, en busqueda avanzada
	 *
	 */
	private char inventarioAux;

	/**
	 * Reset NO estandar, para limpiar al terminar todo el proceso, NO al
	 * cambiar de página. Limpia T ODOS los datos menos el estado
	 */
	public void reset ()
	{
		this.codigo=0;
		this.esInventario=false;
		this.metodoAjuste='0';
		this.nombre="";
		this.tarifarioOficial=-1;
		this.activo=true;
		this.inventarioAux = ' ';
		this.cantidad = 0;
		this.cantidadAux = 0;
		this.map=new HashMap();
	}
	/**
	 * @return El estado del flujo
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @return Returns the cantidadAux.
	 */
	public float getCantidadAux() {
		return cantidadAux;
	}
	/**
	 * @param cantidadAux The cantidadAux to set.
	 */
	public void setCantidadAux(float cantidadAux) {
		this.cantidadAux = cantidadAux;
	}
	/**
	 * Asignar el estado del flujo
	 * @param estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * @return
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @return
	 */
	public boolean getEsInventario() {
		return esInventario;
	}

	/**
	 * @return
	 */
	public char getMetodoAjuste() {
		return metodoAjuste;
	}

	/**
	 * @return
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @return
	 */
	public int getTarifarioOficial() {
		return tarifarioOficial;
	}

	/**
	 * @param i
	 */
	public void setCodigo(int i) {
		codigo = i;
	}

	/**
	 * @param b
	 */
	public void setEsInventario(boolean b) {
		esInventario = b;
	}

	/**
	 * @param c
	 */
	public void setMetodoAjuste(char c) {
		metodoAjuste = c;
	}

	/**
	 * @param string
	 */
	public void setNombre(String string) {
		nombre = string;
	}

	/**
	 * @param i
	 */
	public void setTarifarioOficial(int i) {
		tarifarioOficial = i;
	}

	/**
	 * @return
	 */
	public String getAccionAFinalizar() {
		return accionAFinalizar;
	}

	/**
	 * @param string
	 */
	public void setAccionAFinalizar(String string) {
		accionAFinalizar = string;
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

		if (estado!=null&&estado.equals("empezar"))
		{
			this.reset();
		}
		
		// Perform validator framework validations
		ActionErrors errors = new ActionErrors();

		//Aqui van las validaciones que solo ocurren al momento
		//que se quiere cambiar algo en la BD y específicamente
		//cuando se quiere insertar 
		if (estado.equals("salir")&&accionAFinalizar.equals("insertar"))
		{
			if (tarifarioOficial<=0 && !this.esInventario)
			{
				errors.add("tarifarioOficialInvalido", new ActionMessage("errors.seleccion", "tarifario oficial"));
			}
			if (metodoAjuste=='0')
			{
				errors.add("metodoAjusteInvalido", new ActionMessage("errors.seleccion", "método de ajuste"));
			}
			if (nombre==null || nombre.equals("") || !Utilidades.validarEspacios(nombre))
			{
				errors.add("nombreEsquemaVacio", new ActionMessage("errors.nombreVacio", "de esquema tarifario"));
			}
			
		}
		if (estado.equals("salir")&&accionAFinalizar.equals("modificar"))
		{
			if (metodoAjuste=='0')
			{
				errors.add("metodoAjusteInvalido", new ActionMessage("errors.seleccion", "método de ajuste"));
			}
			if (nombre==null || !Utilidades.validarEspacios(nombre))
			{
				errors.add("nombreEsquemaVacio", new ActionMessage("errors.nombreVacio", "de esquema tarifario"));
			}
			//****validación para el valor base********************************
			if(this.tarifarioOficial==ConstantesBD.codigoTarifarioSoat)
			{
					this.cantidad = this.cantidadAux;
			}
			//*****************************************************************
		}
		else if (estado.equals("resultadoBusqueda"))
		{
			if(request.getParameter("criteriosBusqueda(check_1)")==null)
			{
				map.put("check_1","off");
			}
			else if(map.containsKey("check_1")&&(map.get("check_1").equals("on")))
			{
				if(codigo==0)
				{
					errors.add("codigoInvalido", new ActionMessage("errors.required", "Campo Codigo"));
					estado="paginaBusqueda";
				}
			}
			
			if(request.getParameter("criteriosBusqueda(check_2)")==null)
			{
				map.put("check_2","off");
			}
			else if(map.containsKey("check_2")&&(map.get("check_2").equals("on")))
			{
				if(nombre.equals("") || !Utilidades.validarEspacios(nombre))
				{
					errors.add("nombreInvalido", new ActionMessage("errors.required", "Campo Nombre"));
					estado="paginaBusqueda";
				}
			}

			if(request.getParameter("criteriosBusqueda(check_3)")==null)
			{
				map.put("check_3","off");
				tarifarioOficial=0;
			}
			else if(map.containsKey("check_3")&&(map.get("check_3").equals("on")))
			{
				if(tarifarioOficial<=0)
				{
					errors.add("tarifarioInvalido", new ActionMessage("errors.seleccion", "Tarifario Oficial"));
					estado="paginaBusqueda";
				}
			}

			if(request.getParameter("criteriosBusqueda(check_4)")==null)
			{
				map.put("check_4","off");
				metodoAjuste=' ';
			}
			else if(map.containsKey("check_4")&&(map.get("check_4").equals("on")))
			{
				if(metodoAjuste==' ')
				{
					errors.add("metodoAjusteInvalido", new ActionMessage("errors.required", "Campo Metodo de Ajustel"));
					estado="paginaBusqueda";
				}
			}
			
			if(request.getParameter("criteriosBusqueda(check_5)")==null)
			{
				map.put("check_5","off");
				inventarioAux=' ';
			}
			else if(map.containsKey("check_5")&&(map.get("check_5").equals("on")))
			{
				if(inventarioAux==' ')
				{
					errors.add("esInventario", new ActionMessage("errors.required", "Campo Es Inventario"));
					estado="paginaBusqueda";
				}
				else if(inventarioAux=='t')
				{
					inventarioAux='0';
					esInventario=true;
				}
				else if(inventarioAux=='f')
				{
					inventarioAux='0';
					esInventario=false;
				}
			}
		}
		return errors;
	}
	

	public Object getCriteriosBusqueda(String key)
	{
		if(map.containsKey(key))
		{
			if(map.get(key).equals("on"))
				return "on";
			else
				return "off";
		}
		else
			return "off";
	}
									
	public void setCriteriosBusqueda(String key, Object value)
	{
		String val = (String) value;
		if (val != null&&val.equals("on"))
		{
			val = "on";
		}
		else
			val="off";
		if(map!=null)
		{
			map.put(key, val);
		}
	}
	/**
	 * Retorna los resultados de la busqueda
	 * @return
	 */
	public Collection getResultadosBusqueda() {
		return resultadosBusqueda;
	}

	/**
	 * Asigna los resultados de la busqueda
	 * @param resultados
	 */
	public void setResultadosBusqueda(Collection resultados) {
		resultadosBusqueda = resultados;
	}

	/**
	 * @return
	 */
	public String getNombreMetodoAjuste() {
		return nombreMetodoAjuste;
	}

	/**
	 * @return
	 */
	public String getNombreTarifarioOficial() {
		return nombreTarifarioOficial;
	}

	/**
	 * @param string
	 */
	public void setNombreMetodoAjuste(String string) {
		nombreMetodoAjuste = string;
	}

	/**
	 * @param string
	 */
	public void setNombreTarifarioOficial(String string) {
		nombreTarifarioOficial = string;
	}

    /**
     * @return Returns the activo.
     */
    public boolean getActivo()
    {
        return activo;
    }
    /**
     * @param activo The activo to set.
     */
    public void setActivo(boolean activo)
    {
        this.activo = activo;
    }
   
    /**
     * @return Returns the inventarioAux.
     */
    public char getInventarioAux() {
        return inventarioAux;
    }
    /**
     * @param inventarioAux The inventarioAux to set.
     */
    public void setInventarioAux(char inventarioAux) {
        this.inventarioAux = inventarioAux;
    }
	/**
	 * @return Returns the cantidad.
	 */
	public float getCantidad() {
		return cantidad;
	}
	/**
	 * @param cantidad The cantidad to set.
	 */
	public void setCantidad(float cantidad) {
		this.cantidad = cantidad;
	}
}
