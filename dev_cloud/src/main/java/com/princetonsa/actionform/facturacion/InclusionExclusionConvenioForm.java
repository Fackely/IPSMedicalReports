/*
 * Creado May 23, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * InclusionExclusionConvenioForm
 * com.princetonsa.actionform.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 23, 2007
 */
public class InclusionExclusionConvenioForm extends ValidatorForm
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
	private HashMap excepciones;

	/**
	 * 
	 */
	private HashMap excepcionesAnteriores;
	
	/**
	 * 
	 */
	private HashMap excepcionesEliminados;
	
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
	 * 
	 */
	private int contrato;
	
	
	/**
	 * 
	 */
	private int codigoConvenio;
	
	
	/**
	  * 
	  */
	 private ArrayList convenios;
	 
	 /**
	  * 
	  */
	 private ArrayList contratos;
	 
	 /**
	  * 
	  */
	 private boolean excepcionesAnterior=false;
	 
	 /**
	  * 
	  */
	 private String fechaVigencia;
	 
	 /**
	  * 
	  */
	 private String nomCentroCosto;
	
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
		this.excepciones=new HashMap();
		this.excepciones.put("numRegistros", "0");
		this.excepcionesAnteriores=new HashMap();
		this.excepcionesAnteriores.put("numRegistros", "0");
		this.excepcionesEliminados=new HashMap();
		this.excepcionesEliminados.put("numRegistros", "0");
		this.inclusiones=new Vector<InfoDatosString>();
		this.centrosCosto=new HashMap();
		this.centrosCosto.put("numRegistros", "0");
		this.contrato=ConstantesBD.codigoNuncaValido;
		this.codigoConvenio=ConstantesBD.codigoNuncaValido;
		this.convenios=new ArrayList();
    	this.contratos=new ArrayList();
    	this.excepcionesAnterior=false;
    	this.fechaVigencia="";
    	this.nomCentroCosto="";
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
		if(estado.equals("guardarGeneral"))
		{
			for(int i=0;i<Integer.parseInt(inclusionesExclusiones.get("numRegistros")+"");i++)
			{
				if((this.inclusionesExclusiones.get("prioridad_"+i)+"").equals(""))
				{
					errores.add("",new ActionMessage("errors.required","La Prioridad del Registro "+(i+1)));
				}
				
				
				if((this.inclusionesExclusiones.get("fechavigencia_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","La Fecha de Vigencia de la Inclusion/Exclucion"+(i+1)));
				}
				if(!UtilidadTexto.isEmpty(this.inclusionesExclusiones.get("fechavigencia_"+i)+""))
				{
					boolean centinelaErrorFechas=false;
					if(!UtilidadFecha.esFechaValidaSegunAp(this.inclusionesExclusiones.get("fechavigencia_"+i)+""))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia "+this.inclusionesExclusiones.get("fechavigencia_"+i)));
						centinelaErrorFechas=true;
					}
					if(!centinelaErrorFechas)
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), this.inclusionesExclusiones.get("fechavigencia_"+i)+""))
						{
							if(
									((this.inclusionesExclusiones.get("tiporegistro_"+i)+"").equals("BD")&&!(this.inclusionesExclusiones.get("fechavigencia_"+i)+"").equals((this.inclusionesExclusiones.get("fechavigenciaanterior_"+i)+"")))||
									!(this.inclusionesExclusiones.get("tiporegistro_"+i)+"").equals("BD")
								)
							{
								errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.inclusionesExclusiones.get("fechavigencia_"+i), "Actual "+UtilidadFecha.getFechaActual()));
							}
						}
					}
				}
				
				if((this.inclusionesExclusiones.get("codigoincluexclucc_"+i)+"").equals(""))
				{
					errores.add("",new ActionMessage("errors.required","La Incusión / Exclusión del Registro "+(i+1)));
				}
				else
				{
					for(int j=0;j<i;j++)
					{
						if(
								(this.inclusionesExclusiones.get("codigoincluexclucc_"+i)+"").equalsIgnoreCase(this.inclusionesExclusiones.get("codigoincluexclucc_"+j)+"")
								&&(this.inclusionesExclusiones.get("fechavigencia_"+i)+"").equalsIgnoreCase(this.inclusionesExclusiones.get("fechavigencia_"+j)+"") 

							)
						{
							errores.add("", new ActionMessage("errors.yaExiste","En Inclusiones/Exclusiones El registro "+(i+1)));
						}
					}
				}
			}
			if(!this.esPrioridadCorrecta())
	    	{
				errores.add("errors.errorPrioridad", new ActionMessage("errors.errorPrioridad"));
	    	}

			for(int i=0;i<Integer.parseInt(excepciones.get("numRegistros")+"");i++)
			{
				
				if((this.excepciones.get("fechavigencia_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","La Fecha de Vigencia de la excepcion"+(i+1)));
				}
				if(!UtilidadTexto.isEmpty(this.excepciones.get("fechavigencia_"+i)+""))
				{
					boolean centinelaErrorFechas=false;
					if(!UtilidadFecha.esFechaValidaSegunAp(this.excepciones.get("fechavigencia_"+i)+""))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia "+this.excepciones.get("fechavigencia_"+i)));
						centinelaErrorFechas=true;
					}
					if(!centinelaErrorFechas)
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), this.excepciones.get("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.excepciones.get("fechavigencia_"+i), "Actual "+UtilidadFecha.getFechaActual()));
						}
					}
				}
				
				
				for(int j=0;j<i;j++)
				{
					if(
							(this.excepciones.get("centrocosto_"+i)+"").equalsIgnoreCase(this.excepciones.get("centrocosto_"+j)+"") 
							&&(this.excepciones.get("fechavigencia_"+i)+"").equalsIgnoreCase(this.excepciones.get("fechavigencia_"+j)+"") 
						)
					{
						errores.add("", new ActionMessage("errors.yaExiste","En excepciones de Inclusión/Exclusión El registro "+(i+1)));
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
							(this.agrupacionArticulos.get("fechavigencia_"+i)+"").equalsIgnoreCase(this.agrupacionArticulos.get("fechavigencia_"+j)+"") &&
							(this.agrupacionArticulos.get("naturaleza_"+i)+"").equalsIgnoreCase(this.agrupacionArticulos.get("naturaleza_"+j)+"") 
						)
					{
						errores.add("", new ActionMessage("errors.yaExiste","En Agrupacion Articulo: La clase \""+this.agrupacionArticulos.get("nomclase_"+i)+"\" Grupo: \""+this.agrupacionArticulos.get("nomgrupo_"+i)+"\" SubGrupo: \""+this.agrupacionArticulos.get("nomsubgrupo_"+i)+"\" Naturaleza: \""+this.agrupacionArticulos.get("nomnaturaleza_"+i)+"\""));
					}
				}
				
				
				if((this.getAgrupacionArticulos("fechavigencia_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","La Fecha de Vigencia de la Agrupacion Articulos "+(i+1)));
				}
				
				if(!UtilidadTexto.isEmpty(this.getAgrupacionArticulos("fechavigencia_"+i)+""))
				{
					boolean centinelaErrorFechas=false;
					if(!UtilidadFecha.esFechaValidaSegunAp(this.getAgrupacionArticulos("fechavigencia_"+i)+""))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia "+this.getAgrupacionArticulos("fechavigencia_"+i)));
						centinelaErrorFechas=true;
					}
					if(!centinelaErrorFechas)
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getAgrupacionArticulos("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getAgrupacionArticulos("fechavigencia_"+i), "Actual "+UtilidadFecha.getFechaActual()));
						}
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.excepciones.get("fechavigencia_"+this.getIndexSeleccionado())+"", this.getAgrupacionArticulos("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getAgrupacionArticulos("fechavigencia_"+i), "Vigencia definida a nivel de convenio/contrato "+this.excepciones.get("fechavigencia_"+this.getIndexSeleccionado())));
						}
					}
				}
				
				
			}
			for(int i=0;i<Integer.parseInt(articulos.get("numRegistros")+"");i++)
			{
				if((!this.articulos.containsKey("incluye_"+i))||(this.articulos.get("incluye_"+i)+"").equals(""))
				{
					errores.add("",new ActionMessage("errors.required","Incluye/Excluye en Articulo registro "+(i+1)));
				}
				
				if((this.getArticulos("fechavigencia_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","La Fecha de Vigencia del Articulo "+(i+1)));
				}
				
				if(!UtilidadTexto.isEmpty(this.getArticulos("fechavigencia_"+i)+""))
				{
					boolean centinelaErrorFechas=false;
					if(!UtilidadFecha.esFechaValidaSegunAp(this.getArticulos("fechavigencia_"+i)+""))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia "+this.getArticulos("fechavigencia_"+i)));
						centinelaErrorFechas=true;
					}
					if(!centinelaErrorFechas)
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getArticulos("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getArticulos("fechavigencia_"+i), "Actual "+UtilidadFecha.getFechaActual()));
						}
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getExcepciones("fechavigencia_"+this.getIndexSeleccionado())+"", this.getArticulos("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getArticulos("fechavigencia_"+i), "Vigencia definida a nivel de convenio/contrato "+this.getExcepciones("fechavigencia_"+this.getIndexSeleccionado())));
						}
					}
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
							(this.agrupacionServicios.get("fechavigencia_"+i)+"").equalsIgnoreCase(this.agrupacionServicios.get("fechavigencia_"+j)+"") &&
							(this.agrupacionServicios.get("especialidad_"+i)+"").equalsIgnoreCase(this.agrupacionServicios.get("especialidad_"+j)+"")
						)
					{
						errores.add("", new ActionMessage("errors.yaExiste","En Agrupacion Servicios: El Grupo: \""+this.agrupacionServicios.get("descgruposervicio_"+i)+"\" Tipo Servicio: \""+this.agrupacionServicios.get("nomtiposervicio_"+i)+"\" Especialidad: \""+this.agrupacionServicios.get("nomespecialidad_"+i)+"\" Tipo pos:  \""+this.agrupacionServicios.get("tipopos_"+i)+"\""));
					}
				}
				
				

				if((this.getAgrupacionServicios("fechavigencia_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","La Fecha de Vigencia de la Agrupacion Servicio "+(i+1)));
				}
				
				
				if(!UtilidadTexto.isEmpty(this.getAgrupacionServicios("fechavigencia_"+i)+""))
				{
					boolean centinelaErrorFechas=false;
					if(!UtilidadFecha.esFechaValidaSegunAp(this.getAgrupacionServicios("fechavigencia_"+i)+""))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia "+this.getAgrupacionServicios("fechavigencia_"+i)));
						centinelaErrorFechas=true;
					}
					if(!centinelaErrorFechas)
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getAgrupacionServicios("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getAgrupacionServicios("fechavigencia_"+i), "Actual "+UtilidadFecha.getFechaActual()));
						}
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getExcepciones("fechavigencia_"+this.getIndexSeleccionado())+"", this.getAgrupacionServicios("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getAgrupacionServicios("fechavigencia_"+i), "Vigencia definida a nivel de convenio/contrato "+this.getExcepciones("fechavigencia_"+this.getIndexSeleccionado())));
						}
					}
				}
				
			}
			for(int i=0;i<Integer.parseInt(servicios.get("numRegistros")+"");i++)
			{
				if((!this.servicios.containsKey("incluye_"+i))||(this.servicios.get("incluye_"+i)+"").equals(""))
				{
					errores.add("",new ActionMessage("errors.required","Incluye/Excluye en Servicios registro "+(i+1)));
				}
				


				if((this.getServicios("fechavigencia_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","La Fecha de Vigencia del Servicio "+(i+1)));
				}
				
				if(!UtilidadTexto.isEmpty(this.getServicios("fechavigencia_"+i)+""))
				{
					boolean centinelaErrorFechas=false;
					if(!UtilidadFecha.esFechaValidaSegunAp(this.getServicios("fechavigencia_"+i)+""))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia "+this.getServicios("fechavigencia_"+i)));
						centinelaErrorFechas=true;
					}
					if(!centinelaErrorFechas)
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getServicios("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getServicios("fechavigencia_"+i), "Actual "+UtilidadFecha.getFechaActual()));
						}
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getExcepciones("fechavigencia_"+this.getIndexSeleccionado())+"", this.getServicios("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getServicios("fechavigencia_"+i), "Vigencia detalle "+this.getExcepciones("fechavigencia_"+this.getIndexSeleccionado())));
						}
					}
				}
			}
		}
		return errores;
	}

	
	/**
	 * Metodo que valida las prioridades de los responsables, deben estar consecutivas desde 1.
	 * @return
	 */
	private boolean esPrioridadCorrecta() 
	{
		int[] prioridades=new int[Integer.parseInt(inclusionesExclusiones.get("numRegistros")+"")];
		//pasar las prioridades a un vector.
		for(int a=0;a<prioridades.length;a++)
		{
			prioridades[a]=Utilidades.convertirAEntero(inclusionesExclusiones.get("prioridad_"+a)+"");
		}
		
		//ordenar el vector
		for(int a=0;a<prioridades.length;a++)
		{
			int temp=0;
			for(int j=0;j<a;j++)
			{
				if(prioridades[a]<prioridades[j])
				{
					temp=prioridades[a];
					prioridades[a]=prioridades[j];
					prioridades[j]=temp;
				}
			}
		}
		
		//verificar que las prioridades sean consecutivas desde uno, para esto verificamos el valor=(pos+1), ya que pos inicia en 0
		for(int a=0;a<prioridades.length;a++)
		{
			if(prioridades[a]!=(a+1))
				return false;
		}
		return true;
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
	 * @return the excepciones
	 */
	public HashMap getExcepciones()
	{
		return excepciones;
	}


	/**
	 * @param excepciones the excepciones to set
	 */
	public void setExcepciones(HashMap excepciones)
	{
		this.excepciones = excepciones;
	}


	/**
	 * @return the excepciones
	 */
	public Object getExcepciones(String key)
	{
		return excepciones.get(key);
	}


	/**
	 * @param excepciones the excepciones to set
	 */
	public void setExcepciones(String key,Object value)
	{
		this.excepciones.put(key, value);
	}

	/**
	 * @return the excepcionesEliminados
	 */
	public HashMap getExcepcionesEliminados()
	{
		return excepcionesEliminados;
	}


	/**
	 * @param excepcionesEliminados the excepcionesEliminados to set
	 */
	public void setExcepcionesEliminados(HashMap excepcionesEliminados)
	{
		this.excepcionesEliminados = excepcionesEliminados;
	}


	/**
	 * @return the codigoConvenio
	 */
	public int getCodigoConvenio()
	{
		return codigoConvenio;
	}


	/**
	 * @param codigoConvenio the codigoConvenio to set
	 */
	public void setCodigoConvenio(int codigoConvenio)
	{
		this.codigoConvenio = codigoConvenio;
	}


	/**
	 * @return the contrato
	 */
	public int getContrato()
	{
		return contrato;
	}


	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(int contrato)
	{
		this.contrato = contrato;
	}


	/**
	 * @return the contratos
	 */
	public ArrayList getContratos()
	{
		return contratos;
	}


	/**
	 * @param contratos the contratos to set
	 */
	public void setContratos(ArrayList contratos)
	{
		this.contratos = contratos;
	}


	/**
	 * @return the convenios
	 */
	public ArrayList getConvenios()
	{
		return convenios;
	}


	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList convenios)
	{
		this.convenios = convenios;
	}


	public boolean isExcepcionesAnterior() {
		return excepcionesAnterior;
	}


	public void setExcepcionesAnterior(boolean detalleDescuentoAnterior) {
		this.excepcionesAnterior = detalleDescuentoAnterior;
	}


	public String getFechaVigencia() {
		return fechaVigencia;
	}


	public void setFechaVigencia(String fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}


	public HashMap getExcepcionesAnteriores() {
		return excepcionesAnteriores;
	}


	public void setExcepcionesAnteriores(HashMap excepcionesAnteriores) {
		this.excepcionesAnteriores = excepcionesAnteriores;
	}


	public String getNomCentroCosto() {
		return nomCentroCosto;
	}


	public void setNomCentroCosto(String nomCentroCosto) {
		this.nomCentroCosto = nomCentroCosto;
	}


}
