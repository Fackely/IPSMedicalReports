/**
 * Juan David Ramírez 31/05/2006
 * Princeton S.A.
 */
package com.princetonsa.actionform.inventarios;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * @author Juan David Ramírez
 *
 */
public class ArticulosXMezclaForm extends ValidatorForm
{
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Manejo de los estados para el flujo de la funcionalidad
	 */
	private String estado;

	/**
	 * Código de la mezcla ingresada
	 */
	private int mezcla;
	
	/**
	 * Nombre de la mezcla ingresada
	 */
	private String nombreMezcla;
	
	/**
	 * Listado de las mezclas para seleccionar
	 */
	private Collection listadoMezclas;

	/**
	 * Listado de los articulos para seleccionar
	 */
	private Collection listadoArticulos;
	
	/**
	 * Manejo de los articulos por mezcla
	 */
	private HashMap mapaArticulos;
	
	/**
	 * Indice del articulo eliminado
	 */
	private int eliminado;
	
	/**
	 * Manejo del ordenamiento
	 * propiedad por la cual se quiere ordenar
	 */
	private String propiedad;
	
	/**
	 * Manejo del ordenamiento
	 * propiedad por la cual se encuentra ordenado
	 */
	private String ultimaPropiedad;
	
	/**
	 * Método para resetear la clase
	 */
	public void reset()
	{
		this.mezcla=0;
		this.nombreMezcla="";
		this.listadoMezclas=null;
		this.listadoArticulos=null;
		this.eliminado=0;
		this.mapaArticulos=new HashMap();
		this.mapaArticulos.put("numRegistros", "0");
		this.propiedad="";
		this.ultimaPropiedad="";
	}
	
	/**
	 * @return Retorna estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado Asigna estado.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}
	
	/**
	 * Método para realizar las validaciones de la clase
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(estado.equalsIgnoreCase("guardar"))
		{
			int numRegistros= Integer.parseInt(mapaArticulos.get("numRegistros").toString());
			Vector codigosArticulos=new Vector();
			for(int i=0; i<numRegistros; i++)
			{
				//boolean esInsertado=true;
				int articulo=Integer.parseInt(mapaArticulos.get("articulo_"+i).toString());
				if(articulo==0)
				{
					errores.add("requerido "+i, new ActionMessage("errors.required", "El articulo ("+(i+1)+")"));
				}
				else if(codigosArticulos.contains(articulo))
				{
					errores.add("requerido "+i, new ActionMessage("error.articulosPorMezcla.articuloRepetido", mapaArticulos.get("nombre_"+i)));
				}
				else
				{
					codigosArticulos.add(articulo);
				}
			}
		}
		return errores;
	}

	/**
	 * @return Retorna listadoMezclas.
	 */
	public Collection getListadoMezclas()
	{
		return listadoMezclas;
	}

	/**
	 * @param listadoMezclas Asigna listadoMezclas.
	 */
	public void setListadoMezclas(Collection listadoMezclas)
	{
		this.listadoMezclas = listadoMezclas;
	}

	/**
	 * @return Retorna mezcla.
	 */
	public int getMezcla()
	{
		return mezcla;
	}

	/**
	 * @param mezcla Asigna mezcla.
	 */
	public void setMezcla(int mezcla)
	{
		this.mezcla = mezcla;
	}

	/**
	 * @return Retorna nombreMezcla.
	 */
	public String getNombreMezcla()
	{
		return nombreMezcla;
	}

	/**
	 * @param nombreMezcla Asigna nombreMezcla.
	 */
	public void setNombreMezcla(String nombreMezcla)
	{
		this.nombreMezcla = nombreMezcla;
	}

	/**
	 * @return Retorna mapaArticulos.
	 */
	public HashMap getMapaArticulos()
	{
		return mapaArticulos;
	}

	/**
	 * @param mapaArticulos Asigna mapaArticulos.
	 */
	public void setMapaArticulos(HashMap mapaArticulos)
	{
		this.mapaArticulos = mapaArticulos;
	}

	/**
	 * @return Retorna mapaArticulos.
	 */
	public Object getMapaArticulos(Object key)
	{
		return mapaArticulos.get(key);
	}

	/**
	 * @return Retorna mapaArticulos.
	 */
	public void removeMapaArticulos(Object key)
	{
		mapaArticulos.remove(key);
	}
	
	/**
	 * @param mapaArticulos Asigna mapaArticulos.
	 */
	public void setMapaArticulos(Object key, Object value)
	{
		this.mapaArticulos.put(key, value);
	}
	
	
	/**
	 * @param mapaArticulos Asigna mapaArticulos.
	 */
	public int getTamanioListadoMezclas()
	{
		return listadoMezclas.size();
	}

	/**
	 * @return Retorna listadoArticulos.
	 */
	public Collection getListadoArticulos()
	{
		return listadoArticulos;
	}

	/**
	 * @param listadoArticulos Asigna listadoArticulos.
	 */
	public void setListadoArticulos(Collection listadoArticulos)
	{
		this.listadoArticulos = listadoArticulos;
	}

	/**
	 * @return Retorna eliminado.
	 */
	public int getEliminado()
	{
		return eliminado;
	}

	/**
	 * @param eliminado Asigna eliminado.
	 */
	public void setEliminado(int eliminado)
	{
		this.eliminado = eliminado;
	}

	/**
	 * @return Retorna propiedad.
	 */
	public String getPropiedad()
	{
		return propiedad;
	}

	/**
	 * @param propiedad Asigna propiedad.
	 */
	public void setPropiedad(String propiedad)
	{
		this.propiedad = propiedad;
	}

	/**
	 * @return Retorna ultimaPropiedad.
	 */
	public String getUltimaPropiedad()
	{
		return ultimaPropiedad;
	}

	/**
	 * @param ultimaPropiedad Asigna ultimaPropiedad.
	 */
	public void setUltimaPropiedad(String ultimaPropiedad)
	{
		this.ultimaPropiedad = ultimaPropiedad;
	}

}
