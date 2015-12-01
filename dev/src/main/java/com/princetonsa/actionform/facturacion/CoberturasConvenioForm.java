/*
 * Creado May 17, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * CoberturasConvenioForm
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

import com.princetonsa.dto.facturacion.DtoProExeCobConvXCont;
import com.princetonsa.dto.odontologia.DtoDetalleProgramas;

import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 17, 2007
 */
public class CoberturasConvenioForm extends ValidatorForm
{
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private int maxPageItems;
	
	/**
	 * 
	 */
	private String linkSiguiente;
	
	/**
	 * 
	 */
	private int offset;
	
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
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
	private int contrato;
	
	
	/**
	 * 
	 */
	private int codigoConvenio;

	
	/**
	 * 
	 */
	private HashMap coberturas;
	
	
	/**
	 * 
	 */
	private HashMap excepciones;
	
	/**
	 * 
	 */
	private HashMap coberturasEliminados;
	
	/**
	 * 
	 */
	private HashMap excepcionesEliminados;
	
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
	
	
	private Vector<InfoDatosString> selCoberturas;
	
	/**
	 * 
	 */
	private Vector<InfoDatosString> selViasIngreso;
	
	
	/**
	 * 
	 */
	private Vector<InfoDatosString> selNaturalezaPaciente;
	
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
	private int posMapa;
	
	/**
	 * 
	 */
	private String codigoConvenioStr;
	
	/**
	 * 
	 */
	private String tipoAtencionConvenio;
	
	/**
	 * 
	 */
	private DtoProExeCobConvXCont excepcionCobConvenio;
	
	/**
	 * 
	 */
	private ArrayList<DtoProExeCobConvXCont> listadoExcepcionCobConvenio;
	
	/**
	 * 
	 */
	private int posExcepcion;
	
	/**
	 * 
	 */
	private ArrayList<DtoDetalleProgramas> listadoDetalleProgramas;
	
	
	/**
	 * 
	 */
	private HashMap excepcionesOriginales;
	
	/**
	 * 
	 */
	private HashMap coberturasOriginales;
	
	/**
	 * 
	 */
	private String mensaje;
	
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
		this.coberturas=new HashMap();
		this.coberturas.put("numRegistros", "0");
		this.excepciones=new HashMap();
		this.excepciones.put("numRegistros", "0");
		this.coberturasEliminados=new HashMap();
		this.coberturasEliminados.put("numRegistros", "0");
		this.excepcionesEliminados=new HashMap();
		this.excepcionesEliminados.put("numRegistros", "0");
		this.linkSiguiente="";
		this.offset=0;
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.selCoberturas=new Vector<InfoDatosString>();
		this.selViasIngreso=new Vector<InfoDatosString>();
		this.selNaturalezaPaciente=new Vector<InfoDatosString>();
		this.contrato=ConstantesBD.codigoNuncaValido;
		this.codigoConvenio=ConstantesBD.codigoNuncaValido;
		this.convenios=new ArrayList();
    	this.contratos=new ArrayList();
    	this.posMapa=ConstantesBD.codigoNuncaValido;
    	this.codigoConvenioStr="";
    	this.tipoAtencionConvenio="";
    	this.excepcionCobConvenio=new DtoProExeCobConvXCont();
    	this.listadoExcepcionCobConvenio=new ArrayList<DtoProExeCobConvXCont>();
    	this.posExcepcion=ConstantesBD.codigoNuncaValido;
    	this.listadoDetalleProgramas=new ArrayList<DtoDetalleProgramas>();
    	this.excepcionesOriginales=new HashMap();
    	this.coberturasOriginales=new HashMap();
    	this.mensaje="";
	}
	
	public void resetExcepcion()
	{
		this.excepcionCobConvenio=new DtoProExeCobConvXCont();
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
	
	public void resetSelects()
	{
		this.selCoberturas=new Vector<InfoDatosString>();
		this.selViasIngreso=new Vector<InfoDatosString>();
		this.selNaturalezaPaciente=new Vector<InfoDatosString>();
	}

	/**
	 * Método para validar la inserción de datos
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores=new ActionErrors();
		if(estado.equals("guardarCoberturaConvenio"))
		{
			for(int i=0;i<Integer.parseInt(coberturas.get("numRegistros")+"");i++)
			{
				
				if(UtilidadTexto.isEmpty(this.coberturas.get("codigocobertura_"+i)+""))
				{
					errores.add("",new ActionMessage("errors.required","La cobertura del registro "+(i+1)));
				}
				if(UtilidadTexto.isEmpty(this.coberturas.get("prioridad_"+i)+""))
				{
					errores.add("",new ActionMessage("errors.required","La Prioridad del registro "+(i+1)));
				}
				for(int j=0;j<i;j++)
				{
					if((this.coberturas.get("codigocobertura_"+i)+"").equalsIgnoreCase(this.coberturas.get("codigocobertura_"+j)+""))
					{
						errores.add("", new ActionMessage("errors.yaExiste","El registro "+(i+1)));
					}
				}
			}
			if(!this.esPrioridadCorrecta())
	    	{
				errores.add("errors.errorPrioridad", new ActionMessage("errors.errorPrioridad"));
	    	}
			for(int i=0;i<Integer.parseInt(excepciones.get("numRegistros")+"");i++)
			{
				for(int j=0;j<i;j++)
				{
					if(
							(this.excepciones.get("viaingreso_"+i)+"").equalsIgnoreCase(this.excepciones.get("viaingreso_"+j)+"") &&
							(this.excepciones.get("natpaciente_"+i)+"").equalsIgnoreCase(this.excepciones.get("natpaciente_"+j)+"")
						)
					{
						errores.add("", new ActionMessage("errors.yaExiste","El registro "+(i+1)+" de excepciones"));
					}
				}
			}
		}
		if(estado.equals("guardar"))
		{
			for(int i=0;i<Integer.parseInt(agrupacionArticulos.get("numRegistros")+"");i++)
			{
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
				if(
						(this.agrupacionArticulos.get("clase_"+i)+"").trim().equals("") &&
						(this.agrupacionArticulos.get("grupo_"+i)+"").trim().equals("") &&
						(this.agrupacionArticulos.get("subgrupo_"+i)+"").trim().equals("") &&
						(this.agrupacionArticulos.get("naturaleza_"+i)+"").trim().equals("") &&
						((this.agrupacionArticulos.get("requiereautorizacion_"+i)+"").trim().equals("N")||(this.agrupacionArticulos.get("requiereautorizacion_"+i)+"").trim().equals("")) &&
						(this.agrupacionArticulos.get("semanasmincotizacion_"+i)+"").trim().equals("") &&
						(this.agrupacionArticulos.get("cantidad_"+i)+"").trim().equals("") &&
						((this.agrupacionArticulos.get("presfactura_"+i)+"").trim().equals("N")||(this.agrupacionArticulos.get("presfactura_"+i)+"").trim().equals("")) &&
						((this.agrupacionArticulos.get("incluido_"+i)+"").trim().equals("N")||(this.agrupacionArticulos.get("incluido_"+i)+"").trim().equals("")) 
					)
				{
					errores.add("errors.minimoCampos", new ActionMessage("errors.minimoCampos","1 Campo En Agrupacion Articulos registro("+(i+1)+")","Inserción / modificación"));
				}
			}
			for(int i=0;i<Integer.parseInt(agrupacionServicios.get("numRegistros")+"");i++)
			{
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
				if(
						((this.agrupacionServicios.get("tipopos_"+i)+"").trim().equals("N") ||(this.agrupacionServicios.get("tipopos_"+i)+"").trim().equals("")) &&
						(this.agrupacionServicios.get("gruposervicio_"+i)+"").trim().equals("") &&
						(this.agrupacionServicios.get("tiposervicio_"+i)+"").trim().equals("") &&
						(this.agrupacionServicios.get("especialidad_"+i)+"").trim().equals("") &&
						((this.agrupacionServicios.get("requiereautorizacion_"+i)+"").trim().equals("N")||(this.agrupacionServicios.get("requiereautorizacion_"+i)+"").trim().equals("")) &&
						(this.agrupacionServicios.get("semanasmincotizacion_"+i)+"").trim().equals("") &&
						(this.agrupacionServicios.get("cantidad_"+i)+"").trim().equals("") &&
						((this.agrupacionServicios.get("incluido_"+i)+"").trim().equals("N")||(this.agrupacionServicios.get("incluido_"+i)+"").trim().equals("")) 

					)
				{
					errores.add("errors.minimoCampos", new ActionMessage("errors.minimoCampos","1 Campo En Agrupacion Servicios registro("+(i+1)+")","Inserción / modificación"));
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
		int[] prioridades=new int[Integer.parseInt(coberturas.get("numRegistros")+"")];
		//pasar las prioridades a un vector.
		for(int a=0;a<prioridades.length;a++)
		{
			prioridades[a]=Utilidades.convertirAEntero(coberturas.get("prioridad_"+a)+"");
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
	 * @return the maxPageItems
	 */
	public int getMaxPageItems()
	{
		return maxPageItems;
	}

	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems)
	{
		this.maxPageItems = maxPageItems;
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
	 * @return the coberturas
	 */
	public HashMap getCoberturas()
	{
		return coberturas;
	}


	/**
	 * @param coberturas the coberturas to set
	 */
	public void setCoberturas(HashMap coberturas)
	{
		this.coberturas = coberturas;
	}
	

	/**
	 * @return the coberturas
	 */
	public Object getCoberturas(String key)
	{
		return coberturas.get(key);
	}


	/**
	 * @param coberturas the coberturas to set
	 */
	public void setCoberturas(String key,Object value)
	{
		this.coberturas.put(key, value);
	}


	/**
	 * @return the coberturasEliminados
	 */
	public HashMap getCoberturasEliminados()
	{
		return coberturasEliminados;
	}


	/**
	 * @param coberturasEliminados the coberturasEliminados to set
	 */
	public void setCoberturasEliminados(HashMap coberturasEliminados)
	{
		this.coberturasEliminados = coberturasEliminados;
	}


	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}


	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente = linkSiguiente;
	}


	/**
	 * @return the offset
	 */
	public int getOffset()
	{
		return offset;
	}


	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset)
	{
		this.offset = offset;
	}


	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar()
	{
		return patronOrdenar;
	}


	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar)
	{
		this.patronOrdenar = patronOrdenar;
	}


	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron()
	{
		return ultimoPatron;
	}


	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron)
	{
		this.ultimoPatron = ultimoPatron;
	}


	/**
	 * @return the selCoberturas
	 */
	public Vector<InfoDatosString> getSelCoberturas()
	{
		return selCoberturas;
	}


	/**
	 * @param selCoberturas the selCoberturas to set
	 */
	public void setSelCoberturas(Vector<InfoDatosString> selCoberturas)
	{
		this.selCoberturas = selCoberturas;
	}


	/**
	 * @return the selNaturalezaPaciente
	 */
	public Vector<InfoDatosString> getSelNaturalezaPaciente()
	{
		return selNaturalezaPaciente;
	}


	/**
	 * @param selNaturalezaPaciente the selNaturalezaPaciente to set
	 */
	public void setSelNaturalezaPaciente(
			Vector<InfoDatosString> selNaturalezaPaciente)
	{
		this.selNaturalezaPaciente = selNaturalezaPaciente;
	}


	/**
	 * @return the selViasIngreso
	 */
	public Vector<InfoDatosString> getSelViasIngreso()
	{
		return selViasIngreso;
	}


	/**
	 * @param selViasIngreso the selViasIngreso to set
	 */
	public void setSelViasIngreso(Vector<InfoDatosString> selViasIngreso)
	{
		this.selViasIngreso = selViasIngreso;
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
	 * @return the excepcionesEliminados
	 */
	public Object getExcepcionesEliminados(String key)
	{
		return excepcionesEliminados.get(key);
	}


	/**
	 * @param excepcionesEliminados the excepcionesEliminados to set
	 */
	public void setExcepcionesEliminados(String key,Object value)
	{
		this.excepcionesEliminados.put(key, value);
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
	
	/**
	 * @return the posMapa
	 */
	public int getPosMapa() {
		return posMapa;
	}


	/**
	 * @param posMapa the posMapa to set
	 */
	public void setPosMapa(int posMapa) {
		this.posMapa = posMapa;
	}


	public String getCodigoConvenioStr() {
		return codigoConvenioStr;
	}


	public void setCodigoConvenioStr(String codigoConvenioStr) {
		this.codigoConvenioStr = codigoConvenioStr;
	}


	public String getTipoAtencionConvenio() {
		return tipoAtencionConvenio;
	}


	public void setTipoAtencionConvenio(String tipoAtencionConvenio) {
		this.tipoAtencionConvenio = tipoAtencionConvenio;
	}


	public DtoProExeCobConvXCont getExcepcionCobConvenio() {
		return excepcionCobConvenio;
	}


	public void setExcepcionCobConvenio(DtoProExeCobConvXCont excepcionCobConvenio) {
		this.excepcionCobConvenio = excepcionCobConvenio;
	}


	public ArrayList<DtoProExeCobConvXCont> getListadoExcepcionCobConvenio() {
		return listadoExcepcionCobConvenio;
	}


	public void setListadoExcepcionCobConvenio(
			ArrayList<DtoProExeCobConvXCont> listadoExcepcionCobConvenio) {
		this.listadoExcepcionCobConvenio = listadoExcepcionCobConvenio;
	}
	
	public int getPosExcepcion() {
		return posExcepcion;
	}
	
	public void setPosExcepcion(int posExcepcion) {
		this.posExcepcion = posExcepcion;
	}


	public ArrayList<DtoDetalleProgramas> getListadoDetalleProgramas() {
		return listadoDetalleProgramas;
	}


	public void setListadoDetalleProgramas(
			ArrayList<DtoDetalleProgramas> listadoDetalleProgramas) {
		this.listadoDetalleProgramas = listadoDetalleProgramas;
	}

	public HashMap getExcepcionesOriginales() {
		return excepcionesOriginales;
	}

	public void setExcepcionesOriginales(HashMap excepcionesOriginales) {
		this.excepcionesOriginales = excepcionesOriginales;
	}

	public HashMap getCoberturasOriginales() {
		return coberturasOriginales;
	}

	public void setCoberturasOriginales(HashMap coberturasOriginales) {
		this.coberturasOriginales = coberturasOriginales;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
	
	
	
}
