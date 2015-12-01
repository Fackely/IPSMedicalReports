package com.princetonsa.actionform.historiaClinica;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadValidacion;
import util.Utilidades;

public class EscalasForm extends ValidatorForm 
{

	
	private String estado;
	
	private String codigoEscala;
	
	private String nombreEscala;
	
	private String requiereObservaciones;
	
	private HashMap<String, Object> mapaConsultaEscalas;
	
	private HashMap<String, Object> mapaSecciones;
	
	private HashMap<String, Object> mapaCampos;
	
	private HashMap<String, Object> mapaFactores;
	
	private HashMap<String, Object> mapaDetalleEscala;
	
	private int indexSeccion;
	
	private int indexSeleccionado;
	
	private int indiceEscala;
	
	private String escala;
	
	private ResultadoBoolean mostrarMensaje;
	
	private int posEliminar;
	
	///
	private int indiceFactorEliminar;
	
	private int indiceCampoEliminar;
	
	private int indiceSeccionEliminar;
	
	
	
	/**
	 * 
	 *
	 */
	public void reset()
	{
		this.estado="";
		this.codigoEscala="";
		this.nombreEscala="";
		this.requiereObservaciones="";
		this.mapaConsultaEscalas= new HashMap<String, Object>();
		this.mapaConsultaEscalas.put("numRegistros", "0");
		this.mapaSecciones= new HashMap<String, Object>();
		this.mapaSecciones.put("numRegistros", "0");
		this.mapaCampos= new HashMap<String, Object>();
		this.mapaCampos.put("numRegistros_0", "0");
		this.mapaFactores= new HashMap<String, Object>();
		this.mapaFactores.put("numRegistros", "0");
		this.mapaDetalleEscala= new HashMap<String, Object>();
		this.mapaDetalleEscala.put("numRegistros", "0");
		this.indexSeccion=ConstantesBD.codigoNuncaValido;
		this.indexSeleccionado=ConstantesBD.codigoNuncaValido;
		this.indiceEscala=ConstantesBD.codigoNuncaValido;
		this.escala="";
		this.posEliminar=ConstantesBD.codigoNuncaValido;
		
		this.indiceFactorEliminar=ConstantesBD.codigoNuncaValido;
		this.indiceCampoEliminar=ConstantesBD.codigoNuncaValido;
		this.indiceSeccionEliminar=ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 */
	public void resetMensaje()
	{
		this.mostrarMensaje=new ResultadoBoolean(false,"");
	}
	

	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("guardar"))
		{
			
			int numRegSeccion= Utilidades.convertirAEntero(this.mapaSecciones.get("numRegistros")+"");
			
			int numRegFactor= Utilidades.convertirAEntero(this.mapaFactores.get("numRegistros")+"");
			
			/*
			 * es asignado automaticamente.
			if(this.codigoEscala.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","El codigo de la escala "));
			}
			*/
			if(this.nombreEscala.equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","El nombre de la escala "));
			}
			if(numRegSeccion<1)
			{
				errores.add("codigo", new ActionMessage("errors.required","Por lo menos parametrizar una seccion "));
			}
			if(numRegFactor<1)
			{
				errores.add("codigo", new ActionMessage("errors.required","Por lo menos parametrizar un Factor de prediccion "));
			}
			//lgchavez xplanner [id=31983] validacion secciones.
			String secerr="";
			for(int i=0;i<numRegSeccion;i++)
			{
				if (secerr.indexOf(" "+i+" ")<0)
				{
					for(int k=0;k<numRegSeccion;k++)
					{
						if((this.mapaSecciones.get("nombreseccion_"+i)+"").trim().equals((this.mapaSecciones.get("nombreseccion_"+k)+"").trim())
								&& i!=k)
						{
							errores.add("SeccionesMismoNombre", new ActionMessage("errors.notEspecific","El nombre de La Seccion "+(i+1)+" tiene que ser diferente a la de La Seccion "+(k+1)+", por favor verifique"));
							secerr+=ConstantesBD.separadorSplit+" "+k+" ";
						}
					}
				}
			}
			
			
			
			for(int i=0;i<numRegSeccion;i++)
			{
				
				if((this.mapaSecciones.get("nombreseccion_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Nombre de la Seccion "+(i+1)));
				}
				if((this.mapaSecciones.get("tiporespuesta_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El tipo de respuesta de la seccion "+(i+1)));
				}
				if((Utilidades.convertirAEntero(mapaCampos.get("numRegistros_"+i)+"")<1))
				{
					errores.add("codigo", new ActionMessage("errors.required","Por lo menos parametrizar un campo para la seccion "+(i+1)));
				}
				for(int j=0;j<Utilidades.convertirAEntero(mapaCampos.get("numRegistros_"+i)+"");j++)
				{
					if((this.mapaCampos.get("nombrecampo_"+i+"_"+j)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","El nombre del campo del registro "+(j+1)+ " de la seccion "+(i+1)));
					}
					if((this.mapaCampos.get("valorminimo_"+i+"_"+j)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","El valor minimo del campo "+(j+1)+ " de la seccion "+(i+1)));
					}
					if((this.mapaCampos.get("valormaximo_"+i+"_"+j)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","El valor maximo del campo "+(j+1)+ " de la seccion "+(i+1)));
					}
					if((Utilidades.convertirADouble(this.mapaCampos.get("valormaximo_"+i+"_"+j)+""))<(Utilidades.convertirADouble(this.mapaCampos.get("valorminimo_"+i+"_"+j)+"")))
					{
						errores.add("codigo", new ActionMessage("errors.notEspecific","El valor minimo del registro "+(j+1)+" de la seccion "+(i+1)+ " debe ser menor que el valor maximo. "));
					}
					else
					{
						for(int k=0;k<j;k++)
						{
							double rangoInicialA= Utilidades.convertirADouble(this.mapaCampos.get("valorminimo_"+i+"_"+j)+"");
							double rangoFinalA= Utilidades.convertirADouble(this.mapaCampos.get("valormaximo_"+i+"_"+j)+"");
							double rangoInicialB= Utilidades.convertirADouble(this.mapaCampos.get("valorminimo_"+i+"_"+k)+"");
							double rangoFinalB= Utilidades.convertirADouble(this.mapaCampos.get("valormaximo_"+i+"_"+k)+"");
							if(UtilidadValidacion.hayCruceNumerosDouble(rangoInicialA, rangoFinalA, rangoInicialB, rangoFinalB))
							{
								errores.add("descripcion",new ActionMessage("errors.notEspecific","Existen cruces en los rangos de la seccion "+(i+1)+  " entre el campo Nro. "+(j+1)+" y el campo Nro. "+(k+1)+". Favor Verificar."));
							}
						}
					}
					
				}
			}
			for(int i=0;i<numRegFactor;i++)
			{
				/*
				 * se asigna automaticamente.
				if((this.mapaFactores.get("codigofactor_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Codigo del Factor de Predicion del registro "+(i+1)));
				}
				*/
				if((this.mapaFactores.get("nombrefactor_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Nombre del Factor de Predicion del registro "+(i+1)));
				}
				if((this.mapaFactores.get("valorinicial_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Valor Inicial del Factor de Predicion del registro "+(i+1)));
				}
				if((this.mapaFactores.get("valorfinal_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Valor Final del Factor de Predicion del registro "+(i+1)));
				}
				if((Utilidades.convertirADouble(this.mapaFactores.get("valorinicial_"+i)+"")>(Utilidades.convertirADouble(this.mapaFactores.get("valorfinal_"+i)+""))))
				{
					errores.add("codigo", new ActionMessage("errors.notEspecific","El valor inicial del registro "+(i+1)+ " debe ser menor que el valor final"));
				}
				else
				{
					for(int j=0;j<i;j++)
					{
						if((this.mapaFactores.get("codigofactor_"+i)+"").equalsIgnoreCase(this.mapaFactores.get("codigofactor_"+j)+""))
						{
							errores.add("", new ActionMessage("errors.yaExiste","El Factor de Prediccion con código "+this.mapaFactores.get("codigofactor_"+i)));
						}
						double rangoInicialA= Utilidades.convertirADouble(this.mapaFactores.get("valorinicial_"+i)+"");
						double rangoFinalA= Utilidades.convertirADouble(this.mapaFactores.get("valorfinal_"+i)+"");
						double rangoInicialB= Utilidades.convertirADouble(this.mapaFactores.get("valorinicial_"+j)+"");
						double rangoFinalB= Utilidades.convertirADouble(this.mapaFactores.get("valorfinal_"+j)+"");
						if(UtilidadValidacion.hayCruceNumerosDouble(rangoInicialA, rangoFinalA, rangoInicialB, rangoFinalB))
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Existen cruces en los rangos de los factores de prediccion, entre el factor Nro. "+(i+1)+" y el factor Nro. "+(j+1)+". Favor Verificar."));
						}
					}
				}
					
			}
			if(!errores.isEmpty())
				this.estado="continuar";
			
		}
		else if(this.estado.equals("modificar"))
		{
			
			int numRegSeccion= Utilidades.convertirAEntero(this.mapaSecciones.get("numRegistros")+"");
			
			int numRegFactor= Utilidades.convertirAEntero(this.mapaFactores.get("numRegistros")+"");
			
			if(this.mapaDetalleEscala.get("codigoescala_0").equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","El codigo de la escala "));
			}
			if(this.mapaDetalleEscala.get("nombreescala_0").equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","El nombre de la escala "));
			}
			
		
			
			
			
			for(int i=0;i<numRegSeccion;i++)
			{
				
				if((this.mapaSecciones.get("nombreseccion_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Nombre de la Seccion "+(i+1)));
				}
				if((this.mapaSecciones.get("tiporespuesta_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El tipo de respuesta de la seccion "+(i+1)));
				}
				if((Utilidades.convertirAEntero(mapaCampos.get("numRegistros_"+i)+"")<1))
				{
					errores.add("codigo", new ActionMessage("errors.required","Por lo menos parametrizar un campo para la seccion "+(i+1)));
				}
				for(int j=0;j<Utilidades.convertirAEntero(mapaCampos.get("numRegistros_"+i)+"");j++)
				{
					if((this.mapaCampos.get("nombrecampo_"+mapaSecciones.get("codigopkseccion_"+i)+"_"+j)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","El nombre del campo del registro "+(j+1)+ " de la seccion "+(i+1)));
					}
					if((this.mapaCampos.get("valorminimo_"+mapaSecciones.get("codigopkseccion_"+i)+"_"+j)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","El valor minimo del campo "+(j+1)+ " de la seccion "+(i+1)));
					}
					if((this.mapaCampos.get("valormaximo_"+mapaSecciones.get("codigopkseccion_"+i)+"_"+j)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","El valor maximo del campo "+(j+1)+ " de la seccion "+(i+1)));
					}
					if((Utilidades.convertirAEntero(this.mapaCampos.get("valormaximo_"+mapaSecciones.get("codigopkseccion_"+i)+"_"+j)+""))<(Utilidades.convertirAEntero(this.mapaCampos.get("valorminimo_"+mapaSecciones.get("codigopkseccion_"+i)+"_"+j)+"")))
					{
						errores.add("codigo", new ActionMessage("errors.integerMenorQue","El valor minimo del registro "+(i+1)+" de la seccion "+(j+1), "el valor maximo "));
					}
					else
					{
						for(int k=0;k<j;k++)
						{
							double rangoInicialA= Utilidades.convertirADouble(this.mapaCampos.get("valorminimo_"+mapaSecciones.get("codigopkseccion_"+i)+"_"+j)+"");
							double rangoFinalA= Utilidades.convertirADouble(this.mapaCampos.get("valormaximo_"+mapaSecciones.get("codigopkseccion_"+i)+"_"+j)+"");
							double rangoInicialB= Utilidades.convertirADouble(this.mapaCampos.get("valorminimo_"+mapaSecciones.get("codigopkseccion_"+i)+"_"+k)+"");
							double rangoFinalB= Utilidades.convertirADouble(this.mapaCampos.get("valormaximo_"+mapaSecciones.get("codigopkseccion_"+i)+"_"+k)+"");
							if(UtilidadValidacion.hayCruceNumerosDouble(rangoInicialA, rangoFinalA, rangoInicialB, rangoFinalB))
							{
								errores.add("descripcion",new ActionMessage("errors.notEspecific","Existen cruces en los rangos de la seccion "+(i+1)+  " entre el campo Nro. "+(j+1)+" y el campo Nro. "+(k+1)+". Favor Verificar."));
							}
						}
					}
				}
			}
			for(int i=0;i<numRegFactor;i++)
			{
				/*
				if((this.mapaFactores.get("codigofactor_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Codigo del Factor de Predicion del registro "+(i+1)));
				}
				*/
				if((this.mapaFactores.get("nombrefactor_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Nombre del Factor de Predicion del registro "+(i+1)));
				}
				if((this.mapaFactores.get("valorinicial_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Valor Inicial del Factor de Predicion del registro "+(i+1)));
				}
				if((this.mapaFactores.get("valorfinal_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Valor Final del Factor de Predicion del registro "+(i+1)));
				}
				if((Utilidades.convertirAEntero(this.mapaFactores.get("valorinicial_"+i)+"")>(Utilidades.convertirADouble(this.mapaFactores.get("valorfinal_"+i)+""))))
				{
					errores.add("codigo", new ActionMessage("errors.integerMenorQue","El valor inicial del registro "+(i+1), "el valor final"));
				}
				else
				{
					for(int j=0;j<i;j++)
					{
						if((this.mapaFactores.get("codigofactor_"+i)+"").equalsIgnoreCase(this.mapaFactores.get("codigofactor_"+j)+""))
						{
							errores.add("", new ActionMessage("errors.yaExiste","El Factor de Prediccion con código "+this.mapaFactores.get("codigofactor_"+i)));
						}
						double rangoInicialA= Utilidades.convertirADouble(this.mapaFactores.get("valorinicial_"+i)+"");
						double rangoFinalA= Utilidades.convertirADouble(this.mapaFactores.get("valorfinal_"+i)+"");
						double rangoInicialB= Utilidades.convertirADouble(this.mapaFactores.get("valorinicial_"+j)+"");
						double rangoFinalB= Utilidades.convertirADouble(this.mapaFactores.get("valorfinal_"+j)+"");
						if(UtilidadValidacion.hayCruceNumerosDouble(rangoInicialA, rangoFinalA, rangoInicialB, rangoFinalB))
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Existen cruces en los rangos de los factores de prediccion, entre el factor Nro. "+(i+1)+" y el factor Nro. "+(j+1)+". Favor Verificar."));
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
	public String getEstado() {
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaConsultaEscalas() {
		return mapaConsultaEscalas;
	}

	/**
	 * 
	 * @param mapaConsultaEscalas
	 */
	public void setMapaConsultaEscalas(HashMap<String, Object> mapaConsultaEscalas) {
		this.mapaConsultaEscalas = mapaConsultaEscalas;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaConsultaEscalas(String key)
	{
		return mapaConsultaEscalas.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaConsultaEscalas(String key,Object value)
	{
		this.mapaConsultaEscalas.put(key, value);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getCodigoEscala() {
		return codigoEscala;
	}

	/**
	 * 
	 * @param codigoEscala
	 */
	public void setCodigoEscala(String codigoEscala) {
		this.codigoEscala = codigoEscala;
	}

	/**
	 * 
	 * @return
	 */
	public String getNombreEscala() {
		return nombreEscala;
	}

	/**
	 * 
	 * @param nombreEscala
	 */
	public void setNombreEscala(String nombreEscala) {
		this.nombreEscala = nombreEscala;
	}

	/**
	 * 
	 * @return
	 */
	public String getRequiereObservaciones() {
		return requiereObservaciones;
	}

	/**
	 * 
	 * @param requiereObservaciones
	 */
	public void setRequiereObservaciones(String requiereObservaciones) {
		this.requiereObservaciones = requiereObservaciones;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaCampos() {
		return mapaCampos;
	}

	/**
	 * 
	 * @param mapaCampos
	 */
	public void setMapaCampos(HashMap<String, Object> mapaCampos) {
		this.mapaCampos = mapaCampos;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaCampos(String key)
	{
		return mapaCampos.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaCampos(String key,Object value)
	{
		this.mapaCampos.put(key, value);
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaFactores() {
		return mapaFactores;
	}

	/**
	 * 
	 * @param mapaFactores
	 */
	public void setMapaFactores(HashMap<String, Object> mapaFactores) {
		this.mapaFactores = mapaFactores;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaFactores(String key)
	{
		return mapaFactores.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaFactores(String key,Object value)
	{
		this.mapaFactores.put(key, value);
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaSecciones() {
		return mapaSecciones;
	}

	/**
	 * 
	 * @param mapaSecciones
	 */
	public void setMapaSecciones(HashMap<String, Object> mapaSecciones) {
		this.mapaSecciones = mapaSecciones;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaSecciones(String key)
	{
		return mapaSecciones.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaSecciones(String key,Object value)
	{
		this.mapaSecciones.put(key, value);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getIndexSeccion() {
		return indexSeccion;
	}

	/**
	 * 
	 * @param indexSeccion
	 */
	public void setIndexSeccion(int indexSeccion) {
		this.indexSeccion = indexSeccion;
	}

	/**
	 * 
	 * @return
	 */
	public int getIndexSeleccionado() {
		return indexSeleccionado;
	}

	/**
	 * 
	 * @param indexSeleccionado
	 */
	public void setIndexSeleccionado(int indexSeleccionado) {
		this.indexSeleccionado = indexSeleccionado;
	}
	
	/**
	 * 
	 * @return
	 */
	public ResultadoBoolean getMostrarMensaje() {
		return mostrarMensaje;
	}
	
	/**
	 * 
	 * @param mostrarMensaje
	 */
	public void setMostrarMensaje(ResultadoBoolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getEscala() {
		return escala;
	}
	
	/**
	 * 
	 * @param escala
	 */
	public void setEscala(String escala) {
		this.escala = escala;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getIndiceEscala() {
		return indiceEscala;
	}
	
	/**
	 * 
	 * @param indiceEscala
	 */
	public void setIndiceEscala(int indiceEscala) {
		this.indiceEscala = indiceEscala;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, Object> getMapaDetalleEscala() {
		return mapaDetalleEscala;
	}

	/**
	 * 
	 * @param mapaDetalleEscala
	 */
	public void setMapaDetalleEscala(HashMap<String, Object> mapaDetalleEscala) {
		this.mapaDetalleEscala = mapaDetalleEscala;
	}
	
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaDetalleEscala(String key)
	{
		return mapaDetalleEscala.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaDetalleEscala(String key,Object value)
	{
		this.mapaDetalleEscala.put(key, value);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getPosEliminar() {
		return posEliminar;
	}

	/**
	 * 
	 * @param posEliminar
	 */	
	public void setPosEliminar(int posEliminar) {
		this.posEliminar = posEliminar;
	}

	/**
	 * 
	 * @return
	 */
	public int getIndiceCampoEliminar() {
		return indiceCampoEliminar;
	}
	
	/**
	 * 
	 * @param indiceCampoEliminar
	 */
	public void setIndiceCampoEliminar(int indiceCampoEliminar) {
		this.indiceCampoEliminar = indiceCampoEliminar;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getIndiceFactorEliminar() {
		return indiceFactorEliminar;
	}

	/**
	 * 
	 * @param indiceFactorEliminar
	 */
	public void setIndiceFactorEliminar(int indiceFactorEliminar) {
		this.indiceFactorEliminar = indiceFactorEliminar;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getIndiceSeccionEliminar() {
		return indiceSeccionEliminar;
	}
	
	/**
	 * 
	 * @param indiceSeccionEliminar
	 */
	public void setIndiceSeccionEliminar(int indiceSeccionEliminar) {
		this.indiceSeccionEliminar = indiceSeccionEliminar;
	}
	
	
}
