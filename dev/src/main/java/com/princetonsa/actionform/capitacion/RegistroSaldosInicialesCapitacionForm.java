/*
 * Creado   08/08/2006
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.actionform.capitacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.princetonsa.mundo.capitacion.CuentaCobroCapitacion;

import util.UtilidadFecha;
import util.ValoresPorDefecto;

/**
 * Forma
 *
 * @version 1.0, 08/08/2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 */
public class RegistroSaldosInicialesCapitacionForm extends ActionForm 
{
	 /**
     * estado del workflow
     */
    private String estado;
    
    /**
	 * criterios busqueda cuentas cobro
	 */
	private HashMap criteriosBusquedaMap;
	
	/**
	 * HashMap con los datos del listado, búsqueda avanzada (pager)
	 */
	private HashMap listadoMap;
	
	/**
	 * HashMap con los datos del resumen
	 */
	private HashMap resumenMap;
	
	/**
     * para la nevagación del pager, cuando se ingresa
     * un registro nuevo.
     */
    private String linkSiguiente;
	
    /**
     * check para colocar todos los seleccione en 'si' o en 'no'
     */
    private String checkSeleccionTodos;
    
    /**
     * almacena el indice por el cual 
     * se va ordenar el HashMap
     */
    private String patronOrdenar;
    
    /**
     * almacena el ultimo indice por el 
     * cual se ordeno el HashMap
     */
    private String ultimoPatron;
	
    /**
	 * Offset del HashMap
	 */
	private int offsetHash;
	
	 /**
     * index 
     */
    private String index;
	
	/**
     * inicializar atributos de esta forma     
     */
    public void reset ()
    {
    	this.criteriosBusquedaMap= new HashMap();
    	this.codigoInstitucion=-1;
    	this.listadoMap= new HashMap();
    	this.resumenMap= new HashMap();
    	this.linkSiguiente="";
		this.checkSeleccionTodos="si";
		this.patronOrdenar = "";
        this.ultimoPatron = "";
        this.offsetHash=0;
        this.index="";
    }

    /**
     * codigoInstitucion
     */
    private int codigoInstitucion;
    
    /**
	 * Metodo de validación
	 * @param mapping
	 * @param request
	 * @return errores ActionError, especifica los errores.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		if(this.estado.equals("continuarRegistroCuentaCobro"))
		{
			double tope=0, cuentaCobro=0;
			String topeStr=ValoresPorDefecto.getTopeConsecutivoCxCSaldoICapitacion(this.codigoInstitucion)+"";
			try
			{
				tope=Double.parseDouble(topeStr);
			}
			catch (Exception e) 
			{
				errores.add("", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro", "tope consecutivo saldo inicial capitación"));
			}
			try
			{
				cuentaCobro=Double.parseDouble(this.criteriosBusquedaMap.get("cuentaCobro").toString());
			}
			catch (Exception e) 
			{
				errores.add("", new ActionMessage("errors.integerMenorQue", "El Número de Cuenta Cobro", "el tope "+topeStr));
			}
			if(errores.isEmpty())
			{
				if(cuentaCobro>tope)
				{
					errores.add("", new ActionMessage("errors.integerMenorQue", "El Número de Cuenta Cobro", "el tope "+topeStr));
				}
				if(errores.isEmpty())
				{
					if(CuentaCobroCapitacion.existeNumeroCxC(cuentaCobro+"", this.codigoInstitucion))
					{
						errores.add("", new ActionMessage("errors.yaExiste", "El Número de Cuenta Cobro "+cuentaCobro));
					}
				}
			}
			if(this.getCriteriosBusquedaMap("fechaElaboracion").trim().equals(""))
			{
				errores.add("", new ActionMessage("errors.required","La Fecha Elaboración"));
			}
			else
			{
				if(!UtilidadFecha.validarFecha(this.getCriteriosBusquedaMap("fechaElaboracion")))
				{
					errores.add("Fecha elaboración", new ActionMessage("errors.formatoFechaInvalido", " Elaboración"));
				}
				else
				{	
					//la fecha elaboracion debe ser menor a la del sistema
					if(!UtilidadFecha.esFechaMenorQueOtraReferencia(this.getCriteriosBusquedaMap("fechaElaboracion").trim(), UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","Elaboración "+this.getCriteriosBusquedaMap("fechaElaboracion"), "actual "+UtilidadFecha.getFechaActual()));
					}
					//la fecha final debe ser menor igual a la de parametros generales
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriteriosBusquedaMap("fechaElaboracion").trim(), "31/"+ValoresPorDefecto.getFechaCorteSaldoInicialCCapitacion(this.codigoInstitucion)))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","Elaboración "+this.getCriteriosBusquedaMap("fechaElaboracion"), "de corte saldo inicial capitación "+ValoresPorDefecto.getFechaCorteSaldoInicialCCapitacion(this.codigoInstitucion)));
					}
				}	
			}
			if(this.getCriteriosBusquedaMap("codigoConvenio").equals(""))
			{
				errores.add("", new ActionMessage("errors.required","El convenio"));
			}
			//se verifica que no existan errores para poder validar contra una fecha elaboracion valida 
			if(errores.isEmpty())
			{	
				if(this.getCriteriosBusquedaMap("fechaInicial").trim().equals(""))
				{
					errores.add("", new ActionMessage("errors.required","La Fecha Inicial"));
				}
				else
				{
					if(!UtilidadFecha.validarFecha(this.getCriteriosBusquedaMap("fechaInicial")))
					{
						errores.add("Fecha inicial", new ActionMessage("errors.formatoFechaInvalido", " Inicial"));
					}
					else
					{	
						//la fecha elaboracion debe ser menor a la del sistema
						if(!UtilidadFecha.esFechaMenorQueOtraReferencia(this.getCriteriosBusquedaMap("fechaInicial").trim(), UtilidadFecha.getFechaActual()))
						{
							errores.add("", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","Inicial "+this.getCriteriosBusquedaMap("fechaInicial"), "actual "+UtilidadFecha.getFechaActual()));
						}
						//la fecha inicial debe ser mayor igual a de elaboracion
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getCriteriosBusquedaMap("fechaInicial").trim(), this.getCriteriosBusquedaMap("fechaElaboracion")))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual","Inicial "+this.getCriteriosBusquedaMap("fechaInicial"), "de elaboración "+this.getCriteriosBusquedaMap("fechaElaboracion")));
						}
					}	
				}
			
				//se verifica que no existan errores para poder validar contra fechas validas
				if(errores.isEmpty())
				{
					if(!UtilidadFecha.validarFecha(this.getCriteriosBusquedaMap("fechaFinal")))
					{
						errores.add("Fecha final", new ActionMessage("errors.formatoFechaInvalido", "Final"));
					}
					else
					{	
						//la fecha elaboracion debe ser menor a la del sistema
						if(!UtilidadFecha.esFechaMenorQueOtraReferencia(this.getCriteriosBusquedaMap("fechaFinal").trim(), UtilidadFecha.getFechaActual()))
						{
							errores.add("", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","Final "+this.getCriteriosBusquedaMap("fechaFinal"), "actual "+UtilidadFecha.getFechaActual()));
						}
						//la fecha inicial debe ser menor a la final
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriteriosBusquedaMap("fechaInicial").trim(), this.getCriteriosBusquedaMap("fechaFinal")))
						{
							errores.add("", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","Inicial "+this.getCriteriosBusquedaMap("fechaInicial"), "final "+this.getCriteriosBusquedaMap("fechaFinal")));
						}
						//la fecha final debe ser mayor igual a elaboracion
						if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getCriteriosBusquedaMap("fechaFinal").trim(), this.getCriteriosBusquedaMap("fechaElaboracion")))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual","Final "+this.getCriteriosBusquedaMap("fechaFinal"), "elaboración "+this.getCriteriosBusquedaMap("fechaElaboracion")));
						}
					}	
				}
			
			}
		}
		
		if(this.estado.equals("guardar"))
		{
			int numeroRegistros= Integer.parseInt(this.getListadoMap("numRegistros"));
			for (int w=0; w<numeroRegistros; w++)
			{
				//primero validamos los datos que no estan en bd y que no han sido eliminados y tienen seleccion
				if(	this.getListadoMap("fueeliminada_"+w).equals("false") 
					&& this.getListadoMap("estabd_"+w).equals("false")
					&& this.getListadoMap("seleccion_"+w).equals("si"))
				{
					// primero validamos que todos los registros tengan datos validos
					
					if(this.getListadoMap("codigocontrato_"+w).trim().equals(""))
					{
						errores.add("", new ActionMessage("errors.required","El número de contrato para el registro "+w));
					}
					if(!UtilidadFecha.validarFecha(this.getListadoMap("fechainicial_"+w)))
					{
						errores.add("Fecha inicial", new ActionMessage("errors.formatoFechaInvalido", " Inicial del registro +"+w));
					}
					if(!UtilidadFecha.validarFecha(this.getListadoMap("fechafinal_"+w)))
					{
						errores.add("Fecha Final", new ActionMessage("errors.formatoFechaInvalido", " Final del registro +"+w));
					}
					
					//validaciones del valor total
					double valor=0;
					boolean centinelaEntroError=false;
					try
					{
						valor=Double.parseDouble(this.getListadoMap("valortotal_"+w));
					}
					catch (Exception e) 
					{
						errores.add("", new ActionMessage("errors.integerMayorQue", "El valor total del registro "+w,"0"));
						centinelaEntroError=true;
					}
					if(!centinelaEntroError )
					{
						if(valor<=0)
							errores.add("", new ActionMessage("errors.integerMayorQue", "El valor total del registro "+w,"0"));
					}
					
					//validamos el saldo factura (debe tener lo mismo del valor total pero lo validamos de todas maneras)
					valor=0;
					centinelaEntroError=false;
					try
					{
						valor=Double.parseDouble(this.getListadoMap("saldo_"+w));
					}
					catch (Exception e) 
					{
						errores.add("", new ActionMessage("errors.integerMayorQue", "El saldo del registro "+w,"0"));
						centinelaEntroError=true;
					}
					if(!centinelaEntroError )
					{
						if(valor<=0)
							errores.add("", new ActionMessage("errors.integerMayorQue", "El saldo del registro "+w,"0"));
					}
					
					//si en este punto no existen errores entonces debemos validar los rangos de las fechas
					if(errores.isEmpty())
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getListadoMap("fechainicial_"+w).trim(), this.getListadoMap("fechafinal_"+w).trim()))
						{
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual"," Inicial "+this.getListadoMap("fechainicial_"+w)+" del registro número "+w, " final "+this.getListadoMap("fechafinal_"+w)));
						}
						if(errores.isEmpty())
						{
							if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getListadoMap("fechainicial_"+w), this.getListadoMap("FECHA_INICIAL"))
								|| UtilidadFecha.esFechaMenorQueOtraReferencia(this.getListadoMap("FECHA_FINAL"), this.getListadoMap("fechafinal_"+w)) )
							{
								errores.add("", new ActionMessage("errors.notEspecific","EL rango de fechas ["+this.getListadoMap("fechainicial_"+w)+" - "+this.getListadoMap("fechafinal_"+w)+"] del registro "+w+" debe estar en el rango de fechas "+this.getListadoMap("FECHA_INICIAL")+" - "+this.getListadoMap("FECHA_FINAL")+" de la cuenta cobro "));
							}
						}
					}
				}
			}
		}
		if(!errores.isEmpty())
		{
			if(this.getEstado().equals("guardar"))
				this.setEstado("continuarRegistroCuentaCobro");
			else
				this.setEstado("continuarMostrarErrores");
		}
		
		return errores;
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
	 * @return Returns.
	 */
	public HashMap getCriteriosBusquedaMap() {
		return criteriosBusquedaMap;
	}

	/**
	 * @param cuentaCobroMap The cuentaCobroMap to set.
	 */
	public void setCriteriosBusquedaMap(HashMap cuentaCobroMap) {
		this.criteriosBusquedaMap = cuentaCobroMap;
	}	
	
	/**
	 * @return Returns the getCriteriosBusquedaMap.
	 */
	public String getCriteriosBusquedaMap(String key) {
		return criteriosBusquedaMap.get(key).toString();
	}

	/**
	 * @param listadoMap The cuentaCobroMap to set.
	 */
	public void setCriteriosBusquedaMap(String key, String valor) {
		this.criteriosBusquedaMap.put(key, valor) ;
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
	 * @return Returns the listadoMap.
	 */
	public HashMap getListadoMap() {
		return listadoMap;
	}

	/**
	 * @param listadoMap The listadoMap to set.
	 */
	public void setListadoMap(HashMap listadoMap) {
		this.listadoMap = listadoMap;
	}
	
	/**
	 * @return Returns the listadoMap.
	 */
	public String getListadoMap(String key) {
		return listadoMap.get(key).toString();
	}

	/**
	 * @param listadoMap The listadoMap to set.
	 */
	public void setListadoMap(String key, String valor) {
		this.listadoMap.put(key, valor) ;
	}

	/**
	 * @return Returns the checkSeleccionTodos.
	 */
	public String getCheckSeleccionTodos() {
		return checkSeleccionTodos;
	}

	/**
	 * @param checkSeleccionTodos The checkSeleccionTodos to set.
	 */
	public void setCheckSeleccionTodos(String checkSeleccionTodos) {
		this.checkSeleccionTodos = checkSeleccionTodos;
	}

	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return Returns the patronOrdenar.
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar The patronOrdenar to set.
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return Returns the ultimoPatron.
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron The ultimoPatron to set.
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}
	 /**
     * @return Returns the offsetHash.
     */
    public int getOffsetHash() {
        return offsetHash;
    }
    /**
     * @param offsetHash The offsetHash to set.
     */
    public void setOffsetHash(int offsetHash) {
        this.offsetHash = offsetHash;
    }

	/**
	 * @return Returns the index.
	 */
	public String getIndex() {
		return index;
	}

	/**
	 * @param index The index to set.
	 */
	public void setIndex(String index) {
		this.index = index;
	}

	/**
	 * @return Returns the resumenMap.
	 */
	public HashMap getResumenMap() {
		return resumenMap;
	}

	/**
	 * @param resumenMap The resumenMap to set.
	 */
	public void setResumenMap(HashMap resumenMap) {
		this.resumenMap = resumenMap;
	}

	/**
	 * @param listadoMap The listadoMap to set.
	 */
	public void setResumenMap(String key, String valor) {
		this.resumenMap.put(key, valor) ;
	}
	
	/**
	 * @return Returns the listadoMap.
	 */
	public Object getResumenMap(String key) {
		return resumenMap.get(key);
	}

	/**
	 * @param listadoMap The listadoMap to set.
	 */
	public void setResumenMap(String key, Object valor) {
		this.resumenMap.put(key, valor) ;
	}
	
}
