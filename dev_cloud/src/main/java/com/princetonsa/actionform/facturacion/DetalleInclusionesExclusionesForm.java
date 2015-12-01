/*
 * Creado May 22, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * DetalleInclusionesExclusionesForm
 * com.princetonsa.actionform.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.actionform.facturacion;

import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.Utilidades;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 22, 2007
 */
public class DetalleInclusionesExclusionesForm extends ValidatorForm
{

	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private int indexSeleccionado;
	
	/**
	 * 
	 */
	private int posEliminar;
	
	/**
	 * 
	 */
	private HashMap inclusionesExclusiones;
	
	/**
	 * 
	 */
	private HashMap inclusionesExclusionesEliminados;
	
	
	/**
	 * 
	 */
	private HashMap agrupacionArticulos;
	
	
	/**
	 * 
	 */
	private HashMap agrupacionArticulosEliminados;
	
	/**
	 * 
	 */
	private HashMap articulos;
	
	/**
	 * 
	 */
	private HashMap articulosEliminados;
	
	/**
	 * 
	 */
	private HashMap agrupacionServicios;
	
	/**
	 * 
	 */
	private HashMap agrupacionServiciosEliminados;
	
	/**
	 * 
	 */
	private HashMap servicios;
	
	
	/**
	 * 
	 */
	private HashMap serviciosEliminados;
	
	/**
	 * 
	 */
	private Vector<InfoDatosString> inclusiones;
	
	/**
	 * 
	 */
	private HashMap centrosCosto;
	
	/**
	 * Mensaje de exito o fracaso en de la operacion
	 */
	private ResultadoBoolean mensaje = new ResultadoBoolean(false);
	
	/**
	 * 
	 *
	 */
	public void reset()
	{
		this.agrupacionArticulos=new HashMap();
		this.agrupacionArticulos.put("numRegistros", "0");
		this.agrupacionArticulosEliminados=new HashMap();
		this.agrupacionArticulosEliminados.put("numRegistros", "0");
		this.articulos=new HashMap();
		this.articulos.put("numRegistros", "0");
		this.articulosEliminados=new HashMap();
		this.articulosEliminados.put("numRegistros", "0");
		this.agrupacionServicios=new HashMap();
		this.agrupacionServicios.put("numRegistros", "0");
		this.agrupacionServiciosEliminados=new HashMap();
		this.agrupacionServiciosEliminados.put("numRegistros","0");
		this.servicios=new HashMap();
		this.servicios.put("numRegistros", "0");
		this.serviciosEliminados=new HashMap();
		this.serviciosEliminados.put("numRegistros", "0");
		this.indexSeleccionado=ConstantesBD.codigoNuncaValido;
		this.posEliminar=ConstantesBD.codigoNuncaValido;
		this.inclusionesExclusiones=new HashMap();
		this.inclusionesExclusiones.put("numRegistros", "0");
		this.inclusionesExclusionesEliminados=new HashMap();
		this.inclusionesExclusionesEliminados.put("numRegistros", "0");
		this.inclusiones=new Vector<InfoDatosString>();
		this.centrosCosto=new HashMap();
		this.centrosCosto.put("numRegistros", "0");
	}
	
	/**
	 * Método encargado de resetear el mensaje de éxito
	 */
	public void resetMensaje()
	{
		this.mensaje = new ResultadoBoolean(false);
	}
	
	/**
	 * 
	 *
	 */
	public void resetMapasEliminacion()
	{
		this.agrupacionArticulosEliminados=new HashMap();
		this.agrupacionArticulosEliminados.put("numRegistros", "0");
		this.articulosEliminados=new HashMap();
		this.articulosEliminados.put("numRegistros", "0");
		this.agrupacionServiciosEliminados=new HashMap();
		this.agrupacionServiciosEliminados.put("numRegistros","0");
		this.serviciosEliminados=new HashMap();
		this.serviciosEliminados.put("numRegistros", "0");
		this.posEliminar=ConstantesBD.codigoNuncaValido;
	}

	/**
	 * Método para validar la inserción de datos
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores=new ActionErrors();
		if(estado.equals("guardarIncluExclu"))
		{
			for(int i=0;i<Integer.parseInt(inclusionesExclusiones.get("numRegistros")+"");i++)
			{
				if((this.inclusionesExclusiones.get("codigoincluexclu_"+i)+"").equals(""))
				{
					errores.add("",new ActionMessage("errors.required","La Incusión / Exclusión del Registro "+(i+1)));
				}
				else
				{
					for(int j=0;j<i;j++)
					{
						if(
								(this.inclusionesExclusiones.get("codigoincluexclu_"+i)+"").equalsIgnoreCase(this.inclusionesExclusiones.get("codigoincluexclu_"+j)+"") &&
								(this.inclusionesExclusiones.get("codigocc_"+i)+"").equalsIgnoreCase(this.inclusionesExclusiones.get("codigocc_"+j)+"")
							)
						{
							errores.add("", new ActionMessage("errors.yaExiste","El registro "+(i+1)));
						}
					}
				}
			}
		}
		if(estado.equals("guardar"))
		{
			for(int i=0;i<Integer.parseInt(agrupacionArticulos.get("numRegistros")+"");i++)
			{
				if((this.agrupacionArticulos.get("incluye_"+i)+"").equals(""))
				{
					errores.add("",new ActionMessage("errors.required","Incluye/Excluye en Agrupacion Articulo registro "+(i+1)));
				}
				for(int j=0;j<i;j++)
				{
					if(
							(this.agrupacionArticulos.get("clase_"+i)+"").equalsIgnoreCase(this.agrupacionArticulos.get("clase_"+j)+"") &&
							(this.agrupacionArticulos.get("grupo_"+i)+"").equalsIgnoreCase(this.agrupacionArticulos.get("grupo_"+j)+"") &&
							(this.agrupacionArticulos.get("subgrupo_"+i)+"").equalsIgnoreCase(this.agrupacionArticulos.get("subgrupo_"+j)+"") &&
							(this.agrupacionArticulos.get("naturaleza_"+i)+"").equalsIgnoreCase(this.agrupacionArticulos.get("naturaleza_"+j)+"") 
						)
					{
						errores.add("", new ActionMessage("errors.yaExiste","En Agrupacion Articulo: La clase \""+this.agrupacionArticulos.get("nomclase_"+i)+"\" Grupo: \""+this.agrupacionArticulos.get("nomgrupo_"+i)+"\" SubGrupo: \""+this.agrupacionArticulos.get("nomsubgrupo_"+i)+"\" Naturaleza: \""+this.agrupacionArticulos.get("nomnaturaleza_"+i)+"\""));
					}
				}
			}
			for(int i=0;i<Integer.parseInt(articulos.get("numRegistros")+"");i++)
			{
				if((!this.articulos.containsKey("incluye_"+i))||(this.articulos.get("incluye_"+i)+"").equals(""))
				{
					errores.add("",new ActionMessage("errors.required","Incluye/Excluye en Articulo registro "+(i+1)));
				}
			}
			for(int i=0;i<Integer.parseInt(agrupacionServicios.get("numRegistros")+"");i++)
			{
				if((this.agrupacionServicios.get("incluye_"+i)+"").equals(""))
				{
					errores.add("",new ActionMessage("errors.required","Incluye/Excluye en Agrupacion Servicios registro "+(i+1)));
				}
				for(int j=0;j<i;j++)
				{
					if(
							(this.agrupacionServicios.get("tipopos_"+i)+"").equalsIgnoreCase(this.agrupacionServicios.get("tipopos_"+j)+"") &&
							(this.agrupacionServicios.get("gruposervicio_"+i)+"").equalsIgnoreCase(this.agrupacionServicios.get("gruposervicio_"+j)+"") &&
							(this.agrupacionServicios.get("tiposervicio_"+i)+"").equalsIgnoreCase(this.agrupacionServicios.get("tiposervicio_"+j)+"") &&
							(this.agrupacionServicios.get("especialidad_"+i)+"").equalsIgnoreCase(this.agrupacionServicios.get("especialidad_"+j)+"")
						)
					{
						errores.add("", new ActionMessage("errors.yaExiste","En Agrupacion Servicios: El Grupo: \""+this.agrupacionServicios.get("descgruposervicio_"+i)+"\" Tipo Servicio: \""+this.agrupacionServicios.get("nomtiposervicio_"+i)+"\" Especialidad: \""+this.agrupacionServicios.get("nomespecialidad_"+i)+"\" Tipo pos:  \""+this.agrupacionServicios.get("tipopos_"+i)+"\""));
					}
				}
			}
			for(int i=0;i<Integer.parseInt(servicios.get("numRegistros")+"");i++)
			{
				if((!this.servicios.containsKey("incluye_"+i))||(this.servicios.get("incluye_"+i)+"").equals(""))
				{
					errores.add("",new ActionMessage("errors.required","Incluye/Excluye en Servicios registro "+(i+1)));
				}
			}
		}
		return errores;
	}

	
	/**
	 * 
	 * @return
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}


	/**
	 * @return the agrupacionArticulos
	 */
	public HashMap getAgrupacionArticulos()
	{
		return agrupacionArticulos;
	}

	/**
	 * @param agrupacionArticulos the agrupacionArticulos to set
	 */
	public void setAgrupacionArticulos(HashMap agrupacionArticulos)
	{
		this.agrupacionArticulos = agrupacionArticulos;
	}

	/**
	 * @return the agrupacionServicios
	 */
	public HashMap getAgrupacionServicios()
	{
		return agrupacionServicios;
	}

	/**
	 * @param agrupacionServicios the agrupacionServicios to set
	 */
	public void setAgrupacionServicios(HashMap agrupacionServicios)
	{
		this.agrupacionServicios = agrupacionServicios;
	}

	/**
	 * @return the articulos
	 */
	public HashMap getArticulos()
	{
		return articulos;
	}

	/**
	 * @param articulos the articulos to set
	 */
	public void setArticulos(HashMap articulos)
	{
		this.articulos = articulos;
	}

	/**
	 * @return the servicios
	 */
	public HashMap getServicios()
	{
		return servicios;
	}

	/**
	 * @param servicios the servicios to set
	 */
	public void setServicios(HashMap servicios)
	{
		this.servicios = servicios;
	}
	

	/**
	 * @return the agrupacionArticulos
	 */
	public Object getAgrupacionArticulos(String key)
	{
		return agrupacionArticulos.get(key);
	}

	/**
	 * @param agrupacionArticulos the agrupacionArticulos to set
	 */
	public void setAgrupacionArticulos(String key,Object value)
	{
		this.agrupacionArticulos.put(key, value);
	}

	/**
	 * @return the agrupacionServicios
	 */
	public Object getAgrupacionServicios(String key)
	{
		return agrupacionServicios.get(key);
	}

	/**
	 * @param agrupacionServicios the agrupacionServicios to set
	 */
	public void setAgrupacionServicios(String key,Object value)
	{
		this.agrupacionServicios.put(key, value);
	}

	/**
	 * @return the articulos
	 */
	public Object getArticulos(String key)
	{
		return articulos.get(key);
	}

	/**
	 * @param articulos the articulos to set
	 */
	public void setArticulos(String key,Object value)
	{
		this.articulos.put(key, value);
	}

	/**
	 * @return the servicios
	 */
	public Object getServicios(String key)
	{
		return servicios.get(key);
	}

	/**
	 * @param servicios the servicios to set
	 */
	public void setServicios(String key,Object value)
	{
		this.servicios.put(key, value);
	}

	/**
	 * @return the indexSeleccionado
	 */
	public int getIndexSeleccionado()
	{
		return indexSeleccionado;
	}

	/**
	 * @param indexSeleccionado the indexSeleccionado to set
	 */
	public void setIndexSeleccionado(int indexSeleccionado)
	{
		this.indexSeleccionado = indexSeleccionado;
	}


	/**
	 * @return the agrupacionArticulosEliminados
	 */
	public HashMap getAgrupacionArticulosEliminados()
	{
		return agrupacionArticulosEliminados;
	}

	/**
	 * @param agrupacionArticulosEliminados the agrupacionArticulosEliminados to set
	 */
	public void setAgrupacionArticulosEliminados(
			HashMap agrupacionArticulosEliminados)
	{
		this.agrupacionArticulosEliminados = agrupacionArticulosEliminados;
	}


	/**
	 * @return the posEliminar
	 */
	public int getPosEliminar()
	{
		return posEliminar;
	}

	/**
	 * @param posEliminar the posEliminar to set
	 */
	public void setPosEliminar(int posEliminar)
	{
		this.posEliminar = posEliminar;
	}

	/**
	 * @return the articulosEliminados
	 */
	public HashMap getArticulosEliminados()
	{
		return articulosEliminados;
	}

	/**
	 * @param articulosEliminados the articulosEliminados to set
	 */
	public void setArticulosEliminados(HashMap articulosEliminados)
	{
		this.articulosEliminados = articulosEliminados;
	}

	/**
	 * @return the agrupacionServiciosEliminados
	 */
	public HashMap getAgrupacionServiciosEliminados()
	{
		return agrupacionServiciosEliminados;
	}

	/**
	 * @param agrupacionServiciosEliminados the agrupacionServiciosEliminados to set
	 */
	public void setAgrupacionServiciosEliminados(
			HashMap agrupacionServiciosEliminados)
	{
		this.agrupacionServiciosEliminados = agrupacionServiciosEliminados;
	}

	/**
	 * @return the serviciosEliminados
	 */
	public HashMap getServiciosEliminados()
	{
		return serviciosEliminados;
	}

	/**
	 * @param serviciosEliminados the serviciosEliminados to set
	 */
	public void setServiciosEliminados(HashMap serviciosEliminados)
	{
		this.serviciosEliminados = serviciosEliminados;
	}

	/**
	 * @return the viasDescuentos
	 */
	public HashMap getInclusionesExclusiones()
	{
		return inclusionesExclusiones;
	}


	/**
	 * @param viasDescuentos the viasDescuentos to set
	 */
	public void setInclusionesExclusiones(HashMap inclusionesExclusiones)
	{
		this.inclusionesExclusiones = inclusionesExclusiones;
	}

	/**
	 * @return the viasDescuentos
	 */
	public Object getInclusionesExclusiones( String key)
	{
		return inclusionesExclusiones.get(key);
	}


	/**
	 * @param viasDescuentos the viasDescuentos to set
	 */
	public void setInclusionesExclusiones(String key,Object value)
	{
		this.inclusionesExclusiones.put(key, value);
	}


	/**
	 * @return the viasDescuentosEliminados
	 */
	public HashMap getInclusionesExclusionesEliminados()
	{
		return inclusionesExclusionesEliminados;
	}


	/**
	 * @param viasDescuentosEliminados the viasDescuentosEliminados to set
	 */
	public void setInclusionesExclusionesEliminados(HashMap inclusionesExclusionesEliminados)
	{
		this.inclusionesExclusionesEliminados = inclusionesExclusionesEliminados;
	}



	/**
	 * @return the inclusiones
	 */
	public Vector<InfoDatosString> getInclusiones()
	{
		return inclusiones;
	}


	/**
	 * @param inclusiones the inclusiones to set
	 */
	public void setInclusiones(Vector<InfoDatosString> inclusiones)
	{
		this.inclusiones = inclusiones;
	}


	/**
	 * @return the centrosCosto
	 */
	public HashMap getCentrosCosto()
	{
		return centrosCosto;
	}


	/**
	 * @param centrosCosto the centrosCosto to set
	 */
	public void setCentrosCosto(HashMap centrosCosto)
	{
		this.centrosCosto = centrosCosto;
	}

	/**
	 * Método set del mensaje de éxito o fracaso
	 * @param mensaje
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * Método get el mensaje de éxito o fracaso
	 * @return ResultadoBoolean mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}


}
