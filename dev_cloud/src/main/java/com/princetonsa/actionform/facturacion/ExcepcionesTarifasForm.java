/**
 * 
 */
package com.princetonsa.actionform.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
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
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;

/**
 * @author axioma
 *
 */
public class ExcepcionesTarifasForm extends ValidatorForm
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
	private int indexPorcentaje;
	
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
	private HashMap<String, Object> porcentajeAgrupacionArticulos;
	
	/**
	 * 
	 */
	private HashMap<String, Object> porcentajeArticulos;
	
	/**
	 * 
	 */
	private HashMap<String, Object> porcentajeAgrupacionServicios;
	
	/**
	 * 
	 */
	private HashMap<String, Object> porcentajeServicios;
	
	/**
	 * 
	 */
	private HashMap<String, Object> porcentajeAgrupacionArticulosEliminados;
	
	/**
	 * 
	 */
	private HashMap<String, Object> porcentajeArticulosEliminados;
	
	/**
	 * 
	 */
	private HashMap<String, Object> porcentajeAgrupacionServiciosEliminados;
	
	/**
	 * 
	 */
	private HashMap<String, Object> porcentajeServiciosEliminados;
	
	
	/**
	 * 
	 */
	private Vector<InfoDatosString> selViasIngreso;
	
	/**
	 * 
	 */
	private Vector<InfoDatosString> selTiposComplejidad;
	
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
	 private boolean detalleExcepcionAnterior=false;
	 
	 /**
	  * 
	  */
	 private String fechaVigencia="";
	 
	 /**
	  * 
	  */
	 private String nombreViaIngreso="";
	 
	 /**
	  * 
	  */
	 private String nombreCentroAtencion="";
	 
	 /**
	  * 
	  */
	 private String nombreTipoComplejidad="";
	 
	 /**
	  * Identificador del codigo de la seccion que se esta manejando el porcentaje.
	  * 0---Agrupacion Articulos.
	  * 1---Articulos.
	  * 2---Agrupacion Servicios.
	  * 3---Servicios.
	  */
	 private int codigoSeccionPocentaje;
	 
	 
	 /**
	  * 
	  */
	 private boolean puedoModificar;
	 
	 private boolean operacionTrue=false;
	
	 /**
	  * 
	  */
	 private HashMap selCentrosAtencion;
	 
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
		this.indexPorcentaje=ConstantesBD.codigoNuncaValido;
		this.posEliminar=ConstantesBD.codigoNuncaValido;
		this.excepciones=new HashMap();
		this.excepciones.put("numRegistros", "0");
		this.excepcionesEliminados=new HashMap();
		this.excepcionesEliminados.put("numRegistros", "0");
		this.excepcionesAnteriores=new HashMap();
		this.excepcionesAnteriores.put("numRegistros", "0");
		this.selViasIngreso=new Vector<InfoDatosString>();
		this.selCentrosAtencion= new HashMap();
		this.contrato=ConstantesBD.codigoNuncaValido;
		this.codigoConvenio=ConstantesBD.codigoNuncaValido;
		this.convenios=new ArrayList();
    	this.contratos=new ArrayList();
    	this.selTiposComplejidad=new Vector<InfoDatosString>();
    	this.posMapa=ConstantesBD.codigoNuncaValido;
    	this.porcentajeAgrupacionArticulos=new HashMap<String, Object>();
    	this.porcentajeArticulos=new HashMap<String, Object>();
    	this.porcentajeAgrupacionServicios=new HashMap<String, Object>();
    	this.porcentajeServicios=new HashMap<String, Object>();
    	this.porcentajeAgrupacionArticulosEliminados=new HashMap<String, Object>();
    	this.porcentajeArticulosEliminados=new HashMap<String, Object>();
    	this.porcentajeAgrupacionServiciosEliminados=new HashMap<String, Object>();
    	this.porcentajeServiciosEliminados=new HashMap<String, Object>();
    	this.detalleExcepcionAnterior=false;
    	this.fechaVigencia="";
    	this.nombreCentroAtencion="";
    	this.nombreViaIngreso="";
    	this.nombreTipoComplejidad="";
    	this.codigoSeccionPocentaje=ConstantesBD.codigoNuncaValido;
    	this.puedoModificar=true;
    	this.operacionTrue=false;
	}
	
	public void resetOpTrue()
	{
		this.operacionTrue=false;
	}
	
	public boolean isOperacionTrue() {
		return operacionTrue;
	}


	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
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
			Connection con=UtilidadBD.abrirConexion();
			for(int i=0;i<Integer.parseInt(excepciones.get("numRegistros")+"");i++)
			{
				if((this.excepciones.get("fechavigencia_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","La Fecha de Vigencia del registro "+(i+1)));
				}
				if(!UtilidadTexto.isEmpty(this.getExcepciones("fechavigencia_"+i)+""))
				{
					boolean centinelaErrorFechas=false;
					if(!UtilidadFecha.esFechaValidaSegunAp(this.getExcepciones("fechavigencia_"+i)+""))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia "+this.getExcepciones("fechavigencia_"+i)));
						centinelaErrorFechas=true;
					}
					/* Tarea Xplanner 39626
					if(!centinelaErrorFechas)
					{						
						if (!UtilidadCadena.noEsVacio(this.getExcepciones("codigo_"+i)+""))
							if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(),  this.getExcepciones("fechavigencia_"+i)+""))
							{
								errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getExcepciones("fechavigencia_"+i), "Actual "+UtilidadFecha.getFechaActual()));
							}					
					}
					*/
				}
				
				for(int j=0;j<i;j++)
				{
					/*
					if(
							(this.excepciones.get("viaingreso_"+i)+"").equalsIgnoreCase(this.excepciones.get("viaingreso_"+j)+"") && 
							(this.excepciones.get("tipocomplejidad_"+i)+"").equalsIgnoreCase(this.excepciones.get("tipocomplejidad_"+j)+"")&&
							(this.excepciones.get("tipopaciente_"+i)+"").equalsIgnoreCase(this.excepciones.get("tipopaciente_"+j)+"")&&
							(this.excepciones.get("fechavigencia_"+i)+"").equalsIgnoreCase(this.excepciones.get("fechavigencia_"+j)+"")
						)
					*/
				
					//--
					if(UtilidadValidacion.convenioManejaComplejidad(con, this.codigoConvenio))
					{
						if(
								(this.excepciones.get("viaingreso_"+i)+"").equalsIgnoreCase(this.excepciones.get("viaingreso_"+j)+"") &&
								(this.excepciones.get("codcentroatencion_"+i)+"").equalsIgnoreCase(this.excepciones.get("codcentroatencion_"+j)+"") &&
								(this.excepciones.get("tipopaciente_"+i)+"").equalsIgnoreCase(this.excepciones.get("tipopaciente_"+j)+"") &&
								(this.excepciones.get("tipocomplejidad_"+i)+"").equalsIgnoreCase(this.excepciones.get("tipocomplejidad_"+j)+"") &&
								(this.excepciones.get("fechavigencia_"+i)+"").equalsIgnoreCase(this.excepciones.get("fechavigencia_"+j)+"")
						)
						{
							errores.add("", new ActionMessage("errors.yaExiste","El registro "+(i+1)));
						}
					}
					else{
						if(
								(this.excepciones.get("viaingreso_"+i)+"").equalsIgnoreCase(this.excepciones.get("viaingreso_"+j)+"") &&
								(this.excepciones.get("codcentroatencion_"+i)+"").equalsIgnoreCase(this.excepciones.get("codcentroatencion_"+j)+"") &&
								(this.excepciones.get("tipopaciente_"+i)+"").equalsIgnoreCase(this.excepciones.get("tipopaciente_"+j)+"") &&
								(this.excepciones.get("fechavigencia_"+i)+"").equalsIgnoreCase(this.excepciones.get("fechavigencia_"+j)+"")
						)
						{
								errores.add("", new ActionMessage("errors.yaExiste","El registro "+(i+1)));
						}
					}
				
				}
			}
			
			try {
				UtilidadBD.cerrarConexion(con);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
							(this.agrupacionArticulos.get("naturaleza_"+i)+"").equalsIgnoreCase(this.agrupacionArticulos.get("naturaleza_"+j)+"")&&
							(this.agrupacionArticulos.get("valorbase_"+i)+"").equalsIgnoreCase(this.agrupacionArticulos.get("valorbase_"+j)+"")&&
							(this.agrupacionArticulos.get("fechavigencia_"+i)+"").equalsIgnoreCase(this.agrupacionArticulos.get("fechavigencia_"+j)+"")
						)
					{
						errores.add("", new ActionMessage("errors.yaExiste","En Agrupacion Articulo: La clase \""+this.agrupacionArticulos.get("nomclase_"+i)+"\" Grupo: \""+this.agrupacionArticulos.get("nomgrupo_"+i)+"\" SubGrupo: \""+this.agrupacionArticulos.get("nomsubgrupo_"+i)+"\" Naturaleza: \""+this.agrupacionArticulos.get("nomnaturaleza_"+i)+"\""));
					}
				}
				
				//////validaciones  de porcentaje.   0 --> seccion agurpoacion articulos.
				int seccion=0;
				int tamanio=Utilidades.convertirAEntero((porcentajeAgrupacionArticulos.get("numRegistros_"+seccion+"_"+i)+""),true);
				for(int p=0;p<tamanio;p++)
				{
					try
					{
						if(UtilidadTexto.isEmpty((porcentajeAgrupacionArticulos.get("porcentaje_"+seccion+"_"+i+"_"+p)+"")))
						{
							errores.add("", new ActionMessage("errors.required","El porcentaje "+(p+1)+". En la Agrupacion Articulo: "+(i+1)+""));
						}
						else
						{
							double porcentaje=Double.parseDouble(porcentajeAgrupacionArticulos.get("porcentaje_"+seccion+"_"+i+"_"+p)+"");
							if(porcentaje<0)
							{
								errores.add("errors.MayorIgualQue", new ActionMessage("errors.MayorIgualQue","El porcentaje "+(p+1)+". En la Agrupacion Articulo: "+(i+1)+"","0"));
							}
						}
					}
					catch(Exception e)
					{
						errores.add("eerrors.MayorIgualQue", new ActionMessage("errors.MayorIgualQue","El porcentaje "+(p+1)+". En la Agrupacion Articulo: "+(i+1)+"","0"));
					}
					
					if(UtilidadTexto.isEmpty((porcentajeAgrupacionArticulos.get("suma_"+seccion+"_"+i+"_"+p)+"")))
					{
						errores.add("", new ActionMessage("errors.required","El campo +- del porcentaje "+(p+1)+". En la Agrupacion Articulo: "+(i+1)+""));
					}
					
					try
					{
						if(UtilidadTexto.isEmpty((porcentajeAgrupacionArticulos.get("prioridad_"+seccion+"_"+i+"_"+p)+"")))
						{
							errores.add("", new ActionMessage("errors.required","La prioridad del porcentaje "+(p+1)+". En la Agrupacion Articulo: "+(i+1)+""));
						}
						else
						{
							int prioridad=Integer.parseInt(porcentajeAgrupacionArticulos.get("prioridad_"+seccion+"_"+i+"_"+p)+"");
							if(prioridad<=0)
							{
								errores.add("errors.integerMayorQue", new ActionMessage("errors.integerMayorQue","La prioridad del porcentaje "+(p+1)+". En la Agrupacion Articulo: "+(i+1)+""));
							}
						}
					}
					catch(Exception e)
					{
						errores.add("eerrors.MayorQue", new ActionMessage("errors.integerMayorQue","La prioridad del porcentaje "+(p+1)+". En la Agrupacion Articulo: "+(i+1)+""));
					}
					
				}
				
				//---------------------------------------------------------------------------------------------------------------------------
				
				
				if((!(porcentajeAgrupacionArticulos.get("valorajuste_"+i)+"").trim().equals(""))&&((porcentajeAgrupacionArticulos.get("suma_"+i)+"").trim().equals("")))
				{
					errores.add("",new ActionMessage("errors.required","En Agrupacion Articulo:  Registro  "+(i+1)+" La opcion + - " ));
				}
				
				if((!(agrupacionArticulos.get("valorajuste_"+i)+"").trim().equals(""))&&((agrupacionArticulos.get("baseexcepcion_"+i)+"").trim().equals("")))
				{
					errores.add("",new ActionMessage("errors.required","En Agrupacion Articulo: Registro  "+(i+1)+" Base Excepcion " ));
				}
				
				if(tamanio>0 && !(agrupacionArticulos.get("valorajuste_"+i)+"").trim().equals(""))
				{
					errores.add("",new ActionMessage("error.errorEnBlanco","En Agrupacion Articulo: Registro  "+(i+1)+". El valor ajuste y los porcentajes son excluyentes, por favor seleccionar una sola opcion.  " ));
				}
				
				if(tamanio>0 && !(agrupacionArticulos.get("nuevatarifa_"+i)+"").trim().equals(""))
				{
					errores.add("",new ActionMessage("error.errorEnBlanco","En Agrupacion Articulo: Registro  "+(i+1)+". La nueva tarifa y los porcentajes son excluyentes, por favor seleccionar una sola opcion.  " ));
				}
				
				//-------------------------------------------------------------------------------------------------------------------------
				//modificado por la tarea 77906
				if(tamanio>0 && !UtilidadCadena.noEsVacio(agrupacionArticulos.get("baseexcepcion_"+i)+""))
				{
					errores.add("",new ActionMessage("errors.required","En Agrupacion Articulo: Registro  "+(i+1)+" Base Excepcion " ));
				}
				//-------------------------------------------------------------------------------------------------------------------------
				
				
				
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
						/* Tarea Xplanner 39626
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(),  this.getAgrupacionArticulos("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getAgrupacionArticulos("fechavigencia_"+i), "Actual "+UtilidadFecha.getFechaActual()));
						}
						*/

						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getExcepciones("fechavigencia_"+this.getIndexSeleccionado())+"", this.getAgrupacionArticulos("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getAgrupacionArticulos("fechavigencia_"+i), "Vigencia definida a nivel de convenio/contrato "+this.getExcepciones("fechavigencia_"+this.getIndexSeleccionado())));
						}
					}
				}
			}
			for(int i=0;i<Integer.parseInt(articulos.get("numRegistros")+"");i++)
			{
				
				//////validaciones  de porcentaje.   1 --> seccion articulos.
				int seccion = 1;
				int tamanio=Utilidades.convertirAEntero((porcentajeArticulos.get("numRegistros_"+seccion+"_"+i)+""),true);
				for(int p=0;p<tamanio;p++)
				{
					try
					{
						if(UtilidadTexto.isEmpty((porcentajeArticulos.get("porcentaje_"+seccion+"_"+i+"_"+p)+"")))
						{
							errores.add("", new ActionMessage("errors.required","El porcentaje "+(p+1)+". En el Articulo: "+(i+1)+""));
						}
						else
						{
							double porcentaje=Double.parseDouble(porcentajeArticulos.get("porcentaje_"+seccion+"_"+i+"_"+p)+"");
							if(porcentaje<0)
							{
								errores.add("errors.MayorIgualQue", new ActionMessage("errors.MayorIgualQue","El porcentaje "+(p+1)+".En el Articulo: "+(i+1)+"","0"));
							}
						}
					}
					catch(Exception e)
					{
						errores.add("errors.MayorIgualQue", new ActionMessage("errors.MayorIgualQue","El porcentaje "+(p+1)+". En el Articulo: "+(i+1)+"","0"));
					}
					
					if(UtilidadTexto.isEmpty((porcentajeArticulos.get("suma_"+seccion+"_"+i+"_"+p)+"")))
					{
						errores.add("", new ActionMessage("errors.required","El campo +- del porcentaje "+(p+1)+". En el Articulo: "+(i+1)+""));
					}
					
					try
					{
						if(UtilidadTexto.isEmpty((porcentajeArticulos.get("prioridad_"+seccion+"_"+i+"_"+p)+"")))
						{
							errores.add("", new ActionMessage("errors.required","La prioridad del porcentaje "+(p+1)+". En el Articulo: "+(i+1)+""));
						}
						else
						{
							int prioridad=Integer.parseInt(porcentajeArticulos.get("prioridad_"+seccion+"_"+i+"_"+p)+"");
							if(prioridad<=0)
							{
								errores.add("errors.integerMayorQue", new ActionMessage("errors.integerMayorQue","La prioridad del porcentaje "+(p+1)+". En el Articulo: "+(i+1)+""));
							}
						}
					}
					catch(Exception e)
					{
						errores.add("errors.MayorQue", new ActionMessage("errors.integerMayorQue","La prioridad del porcentaje "+(p+1)+". En el Articulo: "+(i+1)+""));
					}
					
				}
				
				
				if((!(articulos.get("valorajuste_"+i)+"").trim().equals(""))&&((articulos.get("suma_"+i)+"").trim().equals("")))
				{
					errores.add("",new ActionMessage("errors.required","En Articulos: Registro  "+(i+1)+" La opcion + - " ));
				}
				
				if((!(articulos.get("valorajuste_"+i)+"").trim().equals(""))&&((articulos.get("baseexcepcion_"+i)+"").trim().equals("")))
				{
					errores.add("",new ActionMessage("errors.required","En Articulos: Registro  "+(i+1)+" Base Excepcion " ));
				}
				
				//-------------------------------------------------------------------------------------------------------------------------
				//modificado por la tarea 77906
				if(tamanio>0 && !UtilidadCadena.noEsVacio(articulos.get("baseexcepcion_"+i)+""))
				{
					errores.add("",new ActionMessage("errors.required","En Articulos: Registro  "+(i+1)+" Base Excepcion " ));
				}
				//------------------------------------------------------------------------------------------------------------
				
				
				if(tamanio>0 && !(articulos.get("valorajuste_"+i)+"").trim().equals(""))
				{
					errores.add("",new ActionMessage("error.errorEnBlanco","En Articulos: Registro  "+(i+1)+". El valor ajuste y los porcentajes son excluyentes, por favor seleccionar una sola opcion.  " ));
				}
				if(tamanio>0 && !(articulos.get("nuevatarifa_"+i)+"").trim().equals(""))
				{
					errores.add("",new ActionMessage("error.errorEnBlanco","En Articulos: Registro  "+(i+1)+". La nueva tarifa y los porcentajes son excluyentes, por favor seleccionar una sola opcion.  " ));
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
						/* Tarea Xplanner 39626
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getArticulos("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getArticulos("fechavigencia_"+i), "Actual "+UtilidadFecha.getFechaActual()));
						}
						*/
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getExcepciones("fechavigencia_"+this.getIndexSeleccionado())+"", this.getArticulos("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getArticulos("fechavigencia_"+i), "Vigencia definida a nivel de convenio/contrato "+this.getExcepciones("fechavigencia_"+this.getIndexSeleccionado())));
						}
					}
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
							(this.agrupacionServicios.get("especialidad_"+i)+"").equalsIgnoreCase(this.agrupacionServicios.get("especialidad_"+j)+"")&&
							(this.agrupacionServicios.get("fechavigencia_"+i)+"").equalsIgnoreCase(this.agrupacionServicios.get("fechavigencia_"+j)+"")
						)
					{
						errores.add("", new ActionMessage("errors.yaExiste","En Agrupacion Servicios: El Grupo: \""+this.agrupacionServicios.get("descgruposervicio_"+i)+"\" Tipo Servicio: \""+this.agrupacionServicios.get("nomtiposervicio_"+i)+"\" Especialidad: \""+this.agrupacionServicios.get("nomespecialidad_"+i)+"\" Tipo pos:  \""+this.agrupacionServicios.get("tipopos_"+i)+"\""));
					}
				}
				

				//////validaciones  de porcentaje.   2 --> seccion agurpoacion servicios.
				int seccion=2;
				int tamanio=Utilidades.convertirAEntero((porcentajeAgrupacionServicios.get("numRegistros_"+seccion+"_"+i)+""),true);
				for(int p=0;p<tamanio;p++)
				{
					try
					{
						if(UtilidadTexto.isEmpty((porcentajeAgrupacionServicios.get("porcentaje_"+seccion+"_"+i+"_"+p)+"")))
						{
							errores.add("", new ActionMessage("errors.required","El porcentaje "+(p+1)+". En la Agrupacion servicios: "+(i+1)+""));
						}
						else
						{
							double porcentaje=Double.parseDouble(porcentajeAgrupacionServicios.get("porcentaje_"+seccion+"_"+i+"_"+p)+"");
							if(porcentaje<0)
							{
								errores.add("errors.MayorIgualQue", new ActionMessage("errors.MayorIgualQue","El porcentaje "+(p+1)+". En la Agrupacion servicios: "+(i+1)+"","0"));
							}
						}
					}
					catch(Exception e)
					{
						errores.add("errors.MayorIgualQue", new ActionMessage("errors.MayorIgualQue","El porcentaje "+(p+1)+". En la Agrupacion servicios: "+(i+1)+"","0"));
					}
					
					if(UtilidadTexto.isEmpty((porcentajeAgrupacionServicios.get("suma_"+seccion+"_"+i+"_"+p)+"")))
					{
						errores.add("", new ActionMessage("errors.required","El campo +- del porcentaje "+(p+1)+". En la Agrupacion servicios: "+(i+1)+""));
					}
					
					try
					{
						if(UtilidadTexto.isEmpty((porcentajeAgrupacionServicios.get("prioridad_"+seccion+"_"+i+"_"+p)+"")))
						{
							errores.add("", new ActionMessage("errors.required","La prioridad del porcentaje "+(p+1)+". En la Agrupacion servicios: "+(i+1)+""));
						}
						else
						{
							int prioridad=Integer.parseInt(porcentajeAgrupacionServicios.get("prioridad_"+seccion+"_"+i+"_"+p)+"");
							if(prioridad<=0)
							{
								errores.add("errors.integerMayorQue", new ActionMessage("errors.integerMayorQue","La prioridad del porcentaje "+(p+1)+". En la Agrupacion servicios: "+(i+1)+""));
							}
						}
					}
					catch(Exception e)
					{
						errores.add("errors.MayorQue", new ActionMessage("errors.integerMayorQue","La prioridad del porcentaje "+(p+1)+". En la Agrupacion servicios: "+(i+1)+""));
					}
					
				}
				
				if((!(agrupacionServicios.get("valorajuste_"+i)+"").trim().equals(""))&&((agrupacionServicios.get("suma_"+i)+"").trim().equals("")))
				{
					errores.add("",new ActionMessage("errors.required","En Agrupacion Servicios: Registro  "+(i+1)+" La opcion + - " ));
				}
				
				if((!(agrupacionServicios.get("porcentajeexcepcion_"+i)+"").trim().equals("")||!(agrupacionServicios.get("valorajuste_"+i)+"").trim().equals(""))&&((agrupacionServicios.get("baseexcepcion_"+i)+"").trim().equals("")))
				{
					errores.add("",new ActionMessage("errors.required","En Agrupacion Servicios: Registro  "+(i+1)+" Base Excepcion " ));
				}
				
				if(tamanio>0 && !(agrupacionServicios.get("valorajuste_"+i)+"").trim().equals(""))
				{
					errores.add("",new ActionMessage("error.errorEnBlanco","En Agrupacion Servicios: Registro  "+(i+1)+". El valor ajuste y los porcentajes son excluyentes, por favor seleccionar una sola opcion.  " ));
				}
				if(tamanio>0 && !(agrupacionServicios.get("nuevatarifa_"+i)+"").trim().equals(""))
				{
					errores.add("",new ActionMessage("error.errorEnBlanco","En Agrupacion Servicios: Registro  "+(i+1)+". La nueva tarifa y los porcentajes son excluyentes, por favor seleccionar una sola opcion.  " ));
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
						/* Tarea Xplanner 39626
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getAgrupacionServicios("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getAgrupacionServicios("fechavigencia_"+i), "Actual "+UtilidadFecha.getFechaActual()));
						}
						*/
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getExcepciones("fechavigencia_"+this.getIndexSeleccionado())+"", this.getAgrupacionServicios("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getAgrupacionServicios("fechavigencia_"+i), "Vigencia definida a nivel de convenio/contrato "+this.getExcepciones("fechavigencia_"+this.getIndexSeleccionado())));
						}
					}
				}
			}
			for(int i=0;i<Integer.parseInt(servicios.get("numRegistros")+"");i++)
			{
				
//////			validaciones  de porcentaje.   2 --> seccion servicios.
				int seccion=3;
				int tamanio=Utilidades.convertirAEntero((porcentajeServicios.get("numRegistros_"+seccion+"_"+i)+""),true);
				for(int p=0;p<tamanio;p++)
				{
					try
					{
						if(UtilidadTexto.isEmpty((porcentajeServicios.get("porcentaje_"+seccion+"_"+i+"_"+p)+"")))
						{
							errores.add("", new ActionMessage("errors.required","El porcentaje "+(p+1)+". En el Servicio: "+(i+1)+""));
						}
						else
						{
							double porcentaje=Double.parseDouble(porcentajeServicios.get("porcentaje_"+seccion+"_"+i+"_"+p)+"");
							if(porcentaje<0)
							{
								errores.add("errors.MayorIgualQue", new ActionMessage("errors.MayorIgualQue","El porcentaje "+(p+1)+". En el Servicio: "+(i+1)+"","0"));
							}
						}
					}
					catch(Exception e)
					{
						errores.add("errors.MayorIgualQue", new ActionMessage("errors.MayorIgualQue","El porcentaje "+(p+1)+". En el Servicio: "+(i+1)+"","0"));
					}
					
					if(UtilidadTexto.isEmpty((porcentajeServicios.get("suma_"+seccion+"_"+i+"_"+p)+"")))
					{
						errores.add("", new ActionMessage("errors.required","El campo +- del porcentaje "+(p+1)+". En el Servicio: "+(i+1)+""));
					}
					
					try
					{
						if(UtilidadTexto.isEmpty((porcentajeServicios.get("prioridad_"+seccion+"_"+i+"_"+p)+"")))
						{
							errores.add("", new ActionMessage("errors.required","La prioridad del porcentaje "+(p+1)+". En el Servicio: "+(i+1)+""));
						}
						else
						{
							int prioridad=Integer.parseInt(porcentajeServicios.get("prioridad_"+seccion+"_"+i+"_"+p)+"");
							if(prioridad<=0)
							{
								errores.add("errors.integerMayorQue", new ActionMessage("errors.integerMayorQue","La prioridad del porcentaje "+(p+1)+". En el Servicio: "+(i+1)+""));
							}
						}
					}
					catch(Exception e)
					{
						errores.add("errors.MayorQue", new ActionMessage("errors.integerMayorQue","La prioridad del porcentaje "+(p+1)+". En el Servicio: "+(i+1)+""));
					}
					
				}
				
				if((!(servicios.get("valorajuste_"+i)+"").trim().equals(""))&&((servicios.get("suma_"+i)+"").trim().equals("")))
				{
					errores.add("",new ActionMessage("errors.required","En Servicios: Registro  "+(i+1)+" La opcion + - " ));
				}
				if((!(servicios.get("porcentajeexcepcion_"+i)+"").trim().equals("")||!(servicios.get("valorajuste_"+i)+"").trim().equals(""))&&((servicios.get("baseexcepcion_"+i)+"").trim().equals("")))
				{
					errores.add("",new ActionMessage("errors.required","En Servicios: Registro  "+(i+1)+" Base Excepcion " ));
				}
				if(tamanio>0 && !(servicios.get("valorajuste_"+i)+"").trim().equals(""))
				{
					errores.add("",new ActionMessage("error.errorEnBlanco","En Servicios: Registro  "+(i+1)+". El valor ajuste y los porcentajes son excluyentes, por favor seleccionar una sola opcion.  " ));
				}
				if(tamanio>0 && !(servicios.get("nuevatarifa_"+i)+"").trim().equals(""))
				{
					errores.add("",new ActionMessage("error.errorEnBlanco","En Servicios: Registro  "+(i+1)+". La nueva tarifa y los porcentajes son excluyentes, por favor seleccionar una sola opcion.  " ));
				}	
				if((this.getServicios("fechavigencia_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","La Fecha de Vigencia de la Agrupacion Servicio "+(i+1)));
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
						/* Tarea Xplanner 39626
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getServicios("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getServicios("fechavigencia_"+i), "Actual "+UtilidadFecha.getFechaActual()));
						}
						*/
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


	public HashMap getExcepciones() {
		return excepciones;
	}


	public void setExcepciones(HashMap excepciones) {
		this.excepciones = excepciones;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getExcepciones(String key) {
		return excepciones.get(key);
	}


	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setExcepciones(String key,Object value) {
		this.excepciones.put(key, value);
	}


	public HashMap getExcepcionesEliminados() {
		return excepcionesEliminados;
	}


	public void setExcepcionesEliminados(HashMap excepcionesEliminados) {
		this.excepcionesEliminados = excepcionesEliminados;
	}


	public Vector<InfoDatosString> getSelTiposComplejidad() {
		return selTiposComplejidad;
	}


	public void setSelTiposComplejidad(Vector<InfoDatosString> selTiposComplejidad) {
		this.selTiposComplejidad = selTiposComplejidad;
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


	public int getIndexPorcentaje() {
		return indexPorcentaje;
	}


	public void setIndexPorcentaje(int indexPorcentaje) {
		this.indexPorcentaje = indexPorcentaje;
	}


	public HashMap getExcepcionesAnteriores() {
		return excepcionesAnteriores;
	}


	public void setExcepcionesAnteriores(HashMap excepcionesAnteriores) {
		this.excepcionesAnteriores = excepcionesAnteriores;
	}


	public boolean isDetalleExcepcionAnterior() {
		return detalleExcepcionAnterior;
	}


	public void setDetalleExcepcionAnterior(boolean detalleExcepcionAnterior) {
		this.detalleExcepcionAnterior = detalleExcepcionAnterior;
	}


	public String getFechaVigencia() {
		return fechaVigencia;
	}


	public void setFechaVigencia(String fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}


	public String getNombreViaIngreso() {
		return nombreViaIngreso;
	}


	public void setNombreViaIngreso(String nombreViaIngreso) {
		this.nombreViaIngreso = nombreViaIngreso;
	}


	public String getNombreTipoComplejidad() {
		return nombreTipoComplejidad;
	}


	public void setNombreTipoComplejidad(String nombreTipoComplejidad) {
		this.nombreTipoComplejidad = nombreTipoComplejidad;
	}


	public int getCodigoSeccionPocentaje() {
		return codigoSeccionPocentaje;
	}


	public void setCodigoSeccionPocentaje(int codigoSeccionPocentaje) {
		this.codigoSeccionPocentaje = codigoSeccionPocentaje;
	}


	public HashMap<String, Object> getPorcentajeAgrupacionArticulos() {
		return porcentajeAgrupacionArticulos;
	}


	public void setPorcentajeAgrupacionArticulos(
			HashMap<String, Object> porcentajeAgrupacionArticulos) {
		this.porcentajeAgrupacionArticulos = porcentajeAgrupacionArticulos;
	}


	public HashMap<String, Object> getPorcentajeAgrupacionServicios() {
		return porcentajeAgrupacionServicios;
	}


	public void setPorcentajeAgrupacionServicios(
			HashMap<String, Object> porcentajeAgrupacionServicios) {
		this.porcentajeAgrupacionServicios = porcentajeAgrupacionServicios;
	}


	public HashMap<String, Object> getPorcentajeArticulos() {
		return porcentajeArticulos;
	}


	public void setPorcentajeArticulos(HashMap<String, Object> porcentajeArticulos) {
		this.porcentajeArticulos = porcentajeArticulos;
	}


	public HashMap<String, Object> getPorcentajeServicios() {
		return porcentajeServicios;
	}


	public void setPorcentajeServicios(HashMap<String, Object> porcentajeServicios) {
		this.porcentajeServicios = porcentajeServicios;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Object getPorcentajeAgrupacionArticulos(String key) {
		return porcentajeAgrupacionArticulos.get(key);
	}


	public void setPorcentajeAgrupacionArticulos(String key,Object value) 
	{
		this.porcentajeAgrupacionArticulos.put(key, value);
	}


	public Object getPorcentajeAgrupacionServicios(String key) 
	{
		return porcentajeAgrupacionServicios.get(key);
	}


	public void setPorcentajeAgrupacionServicios(String key,Object value) 
	{
		this.porcentajeAgrupacionServicios.put(key, value);
	}


	public Object getPorcentajeArticulos(String key) 
	{
		return porcentajeArticulos.get(key);
	}


	public void setPorcentajeArticulos(String key,Object value) 
	{
		this.porcentajeArticulos.put(key, value);
	}


	public Object getPorcentajeServicios(String key) 
	{
		return porcentajeServicios.get(key);
	}


	public void setPorcentajeServicios(String key,Object value) 
	{
		this.porcentajeServicios.put(key, value);
	}


	public HashMap<String, Object> getPorcentajeAgrupacionArticulosEliminados() {
		return porcentajeAgrupacionArticulosEliminados;
	}


	public void setPorcentajeAgrupacionArticulosEliminados(
			HashMap<String, Object> porcentajeAgrupacionArticulosEliminados) {
		this.porcentajeAgrupacionArticulosEliminados = porcentajeAgrupacionArticulosEliminados;
	}


	public HashMap<String, Object> getPorcentajeAgrupacionServiciosEliminados() {
		return porcentajeAgrupacionServiciosEliminados;
	}


	public void setPorcentajeAgrupacionServiciosEliminados(
			HashMap<String, Object> porcentajeAgrupacionServiciosEliminados) {
		this.porcentajeAgrupacionServiciosEliminados = porcentajeAgrupacionServiciosEliminados;
	}


	public HashMap<String, Object> getPorcentajeArticulosEliminados() {
		return porcentajeArticulosEliminados;
	}


	public void setPorcentajeArticulosEliminados(
			HashMap<String, Object> porcentajeArticulosEliminados) {
		this.porcentajeArticulosEliminados = porcentajeArticulosEliminados;
	}


	public HashMap<String, Object> getPorcentajeServiciosEliminados() {
		return porcentajeServiciosEliminados;
	}


	public void setPorcentajeServiciosEliminados(
			HashMap<String, Object> porcentajeServiciosEliminados) {
		this.porcentajeServiciosEliminados = porcentajeServiciosEliminados;
	}


	public boolean isPuedoModificar() {
		return puedoModificar;
	}


	public void setPuedoModificar(boolean puedoModificar) {
		this.puedoModificar = puedoModificar;
	}

	/**
	 * @return the selCentrosAtencion
	 */
	public HashMap getSelCentrosAtencion() {
		return selCentrosAtencion;
	}

	/**
	 * @param selCentrosAtencion the selCentrosAtencion to set
	 */
	public void setSelCentrosAtencion(HashMap selCentrosAtencion) {
		this.selCentrosAtencion = selCentrosAtencion;
	}

	/**
	 * @return the nombreCentroAtencion
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * @param nombreCentroAtencion the nombreCentroAtencion to set
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}


}
