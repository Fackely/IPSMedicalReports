/*
 * Creado en 27/11/2005
 *
 * Karenth Marín
 * Si.Es.
 */
package com.sies.actionform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.sies.mundo.SiEsConstantes;
import com.sies.mundo.Turno;

import util.UtilidadFecha;

/**
 * @author Karenth Marín
 *
 * Si.Es.
 */
public class GenerarReporteNominaForm extends ActionForm
{
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Para manejar el estado en el que va a realizar la accion
	 */
	private String estado; 
	
	/**
	 * para manejar la fecha de inicio
	 */
	private String fechaInicio;
	
	/**
	 * Para manejar la fecha de Finalización de la asociación
	 */
	private String fechaFin;
	
	/**
	 * Para recibir el listado de las enfermeras
	 */
	private Collection<HashMap<String, Object>> listado;
	
	/**
	 * Listado de las Categorías
	 */
	private Collection<HashMap<String, Object>> categorias;
	
	/**
	 * Listado de tipos de vinculación
	 */
	private Collection<HashMap<String, Object>> tiposVinculacion;
	
	/**
	 * Listado de turnos
	 */
	private HashMap<Integer, Turno> listadoTurnos;
	
	/**
	 * Para guaradar el codigo de la categoria
	 */
	private int codigoCategoria;
	
	/**
	 * Código del tipo de vinculación
	 */
	private int codigoTipoVinculacion;
	
	/**
	 * Para el manejo del nombre de la categoria
	 */
	private String nombreCategoria;
	
	/**
	 * Nombre del tipo de vinculación
	 */
	private String nombreTipoVinculacion;
	
	/**
	 * Para generar el reporte segun una categoria
	 */
	private int codigocuadro;
	
	/**
	 * Maneja un cencepto determinado para generar el reporte plano
	 */
	private String concepto;
	
	/**
	 * hashMap para guardar los turnos que vienen de la base de datos
	 */
	private HashMap<String, Object> turnos;
	 
	/**
	 * para manejar la cantidad de profesionales de la salud que resultaron de la consulta
	 */
	private int cantProfesionales;
	
	/**
	 * Para manejar la cantidad de fechas resultantes de la consulta
	 */
	private int cantFechas;
	    
    /**
     * Listado de fechas
     */
    private ArrayList<String> listadoFechas;
    
    /**
     * Formato del Archovo que se desea generar
     */
    private short formatoArchivo=0;
    
	public ActionErrors validate(
			@SuppressWarnings("unused")	ActionMapping mapping, 
			@SuppressWarnings("unused")	HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		boolean fechaI=UtilidadFecha.validarFecha(fechaInicio);
		boolean fechaF=UtilidadFecha.validarFecha(fechaFin);
		
		//System.out.println("Estado Forma: "+estado);
		
		if (estado.equals("mostrarReporte"))
		{
			if (this.fechaInicio.equals(""))
		    {
		        errores.add("Fecha Inicio", new ActionMessage("errors.required", "El campo Fecha de Inicio"));
		    } 
		    if (this.fechaFin.equals(""))
		    {
		        errores.add("Fecha Fin", new ActionMessage("errors.required", "El campo Fecha de Finalización"));
		    } 
		    if (!this.fechaInicio.equals("") && !this.fechaFin.equals(""))
		    {
		        if (!fechaI)
		        {
		            errores.add("La Fecha de Inicio debe estar en formato dd/mm/aa", new ActionMessage("errors.formatoFechaInvalido", "de inicio"));
		        }
			
		        if (!fechaF)
		        {
		            errores.add("La Fecha de Fin debe estar en formato dd/mm/aa", new ActionMessage("errors.formatoFechaInvalido", "de finalización"));
		        }
		        if (codigoCategoria==0)
		        {
		            errores.add("No ha Elegido Ninguna Categoria", new ActionMessage("errors.required", "El campo Categoria"));
		        }
		        if (fechaI && fechaF)
		        {
		            if (UtilidadFecha.conversionFormatoFechaABD(fechaInicio).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaFin))>=0)
		            {
		            	errores.add("La Fecha de finalización del Reporte debe ser mayor que la fecha de incio", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de Inicio" , "de Finalización" ));
		            }			                
	            }
	            
		    }
		    if(!errores.isEmpty())
		    	estado="empezar";
		}
		return errores;
	}
	
	public void clean()
	{
		estado="empezar";
		fechaInicio="";
		fechaFin="";
		listado= new ArrayList<HashMap<String, Object>>();
		codigoCategoria=0;
		nombreCategoria="";
		codigoTipoVinculacion=0;
		nombreTipoVinculacion="";
		turnos=new HashMap<String, Object>();
		cantProfesionales=0;
		cantFechas=0;
		listadoFechas=new ArrayList<String>();
		listadoTurnos=new HashMap<Integer, Turno>();
		formatoArchivo=0;
		formatoArchivo=SiEsConstantes.FORMATO_ARCHIVO_PDF;
	}

	/**
	 * @return cantFechas
	 */
	public int getCantFechas()
	{
		return cantFechas;
	}

	/**
	 * @param cantFechas Asigna cantFechas
	 */
	public void setCantFechas(int cantFechas)
	{
		this.cantFechas = cantFechas;
	}

	/**
	 * @return cantProfesionales
	 */
	public int getCantProfesionales()
	{
		return cantProfesionales;
	}

	/**
	 * @param cantProfesionales Asigna cantProfesionales
	 */
	public void setCantProfesionales(int cantProfesionales)
	{
		this.cantProfesionales = cantProfesionales;
	}

	/**
	 * @return codigoCategoria
	 */
	public int getCodigoCategoria()
	{
		return codigoCategoria;
	}

	/**
	 * @param codigoCategoria Asigna codigoCategoria
	 */
	public void setCodigoCategoria(int codigoCategoria)
	{
		this.codigoCategoria = codigoCategoria;
	}

	public String getNombreCategoria() 
	{
		return nombreCategoria;
	}

	public void setNombreCategoria(String nombreCategoria)
	{
		this.nombreCategoria = nombreCategoria;
	}

	/**
	 * @return estado
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado Asigna estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * @return fechaFin
	 */
	public String getFechaFin()
	{
		return fechaFin;
	}

	/**
	 * @param fechaFin Asigna fechaFin
	 */
	public void setFechaFin(String fechaFin)
	{
		this.fechaFin = fechaFin;
	}

	/**
	 * @return fechaInicio
	 */
	public String getFechaInicio()
	{
		return fechaInicio;
	}

	/**
	 * @param fechaInicio Asigna fechaInicio
	 */
	public void setFechaInicio(String fechaInicio)
	{
		this.fechaInicio = fechaInicio;
	}

	public int getCodigocuadro() 
	{
		return codigocuadro;
	}

	public void setCodigocuadro(int codigocuadro) 
	{
		this.codigocuadro = codigocuadro;
	}

	public String getConcepto() 
	{
		return concepto;
	}

	public void setConcepto(String concepto) 
	{
		this.concepto = concepto;
	}

	/**
	 * @return listado
	 */
	public Collection<HashMap<String, Object>> getListado()
	{
		return listado;
	}

	/**
	 * @param listado Asigna listado
	 */
	public void setListado(Collection<HashMap<String, Object>> listado)
	{
		this.listado = listado;
	}

	/**
	 * @return listadoFechas
	 */
	public ArrayList<String> getListadoFechas()
	{
		return listadoFechas;
	}

	/**
	 * @param listadoFechas Asigna listadoFechas
	 */
	public void setListadoFechas(ArrayList<String> listadoFechas)
	{
		this.listadoFechas = listadoFechas;
	}

	/**
	 * @return listadoTurnos
	 */
	public HashMap<Integer, Turno> getListadoTurnos()
	{
		return listadoTurnos;
	}

	/**
	 * @param listadoTurnos Asigna listadoTurnos
	 */
	public void setListadoTurnos(HashMap<Integer, Turno> listadoTurnos)
	{
		this.listadoTurnos = listadoTurnos;
	}

	/**
	 * @return turnos
	 */
	public HashMap<String, Object> getTurnos()
	{
		return turnos;
	}

	/**
	 * @param turnos Asigna turnos
	 */
	public void setTurnos(HashMap<String, Object> turnos)
	{
		this.turnos = turnos;
	}

	/**
	 * @return Retorna categorias
	 */
	public Collection<HashMap<String, Object>> getCategorias()
	{
		return categorias;
	}

	/**
	 * @param Asigna categorias
	 */
	public void setCategorias(Collection<HashMap<String, Object>> categorias)
	{
		this.categorias = categorias;
	}

	/**
	 * @return Retorna tiposVinculacion
	 */
	public Collection<HashMap<String, Object>> getTiposVinculacion()
	{
		return tiposVinculacion;
	}

	/**
	 * @param tiposVinculacion Asigna tiposVinculacion
	 */
	public void setTiposVinculacion(Collection<HashMap<String, Object>> tiposVinculacion)
	{
		this.tiposVinculacion = tiposVinculacion;
	}

	/**
	 * @return Retorna codigoTipoVinculacion
	 */
	public int getCodigoTipoVinculacion()
	{
		return codigoTipoVinculacion;
	}

	/**
	 * @param codigoTipoVinculacion Asigna codigoTipoVinculacion
	 */
	public void setCodigoTipoVinculacion(int codigoTipoVinculacion)
	{
		this.codigoTipoVinculacion = codigoTipoVinculacion;
	}

	/**
	 * @return Retorna nombreTipoVinculacion
	 */
	public String getNombreTipoVinculacion()
	{
		return nombreTipoVinculacion;
	}

	/**
	 * @param nombreTipoVinculacion Asigna nombreTipoVinculacion
	 */
	public void setNombreTipoVinculacion(String nombreTipoVinculacion)
	{
		this.nombreTipoVinculacion = nombreTipoVinculacion;
	}

	/**
	 * @return Obtiene formatoArchivo
	 */
	public short getFormatoArchivo()
	{
		return formatoArchivo;
	}

	/**
	 * @param formatoArchivo Asigna formatoArchivo
	 */
	public void setFormatoArchivo(short formatoArchivo)
	{
		this.formatoArchivo = formatoArchivo;
	}
}	
