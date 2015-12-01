/*
 * @(#)ExcepcionesTarifasForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.cargos;

import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.Utilidades;

/**
 * Form que contiene todos los datos específicos para generar 
 * las Excepciones de las Tarifas
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Oct 20, 2004
 * @author wrios 
 */
public class ExcepcionesTarifasForm extends ValidatorForm
{
	/**
	 * código de la excepción
	 */
	private int codigoExcepcion;
	
	/**
	 * Código del contrato asociado a la excepción
	 */
	private int codigoContrato;
	
	/**
	 * Número del contrato asociado a la excepción
	 */
	private String numeroContrato;
	
	/**
	 * Código de la Via de ingreso asociada a la excepcion
	 */
	private int codigoViaIngreso;
	
	/**
	 * Nombre de la Via de ingreso asociada a la excepcion
	 */
	private String nombreViaIngreso;

	/**
	 * Código del servicio asociado a la excepción 
	 */
	private int codigoServicio;
	
	/**
	 * Nombre del servicio asociado a la excepción 
	 */
	private String nombreServicio;

	/**
	 * Código especialidad asociada a la excepcion
	 */
	private int codigoEspecialidad;
	
	/**
	 * Nombre especialidad asociada a la excepcion
	 */
	private String nombreEspecialidad;
	
	/**
	 * 0 cuando el servicio está defindo, 1 cuando es "todos"
	 */
	private int servicioRadio;

	/**
	 * 0 cuando la especialidad está definda, 1 cuando es "todas"
	 */	
	private int especialidadRadio;
	
	/**
	 * 1, si la opción seleccionada es porcentaje, 2 si es valor ajuste, 3 si
	 * es nueva tarifa
	 */
	public int radioExcepcion;
	
	/**
	 * Porcentaje de descuento o aumento de la tarifa
	 */
	private double porcentaje;
	
	/**
	 * Signo del porcentaje "+" o "-"
	 */
	private char signoPorcentaje;
	
	/**
	 * Valor de ajuste de la excepción
	 */
	private double valorAjuste;
	
	/**
	 * Signo del valor del ajuste "+" o "-"
	 */
	private char signoValorAjuste;
	
	/**
	 * Nueva tarifa de la excepción
	 */
	private double nuevaTarifa;
	
	/**
	 * Estado dentro del flujo
	 */
	private String estado;
	
	/**
	 * cod del convenio
	 */
	private int convenio;
	
	/**
	 * Se utiliza en la busqueda para saber si es
	 * por nombre o por codigo Servicio
	 */
	private int radioBusquedaPorCodigoNombreServicio;
	
	/**
	 * Se utiliza en la busqueda para saber si es
	 * por nombre o por codigo Especialidad
	 */
	private int radioBusquedaPorCodigoNombreEspecialidad;
	
	/**
	 * Text en búsqueda avanzada para el servicio 
	 */
	private String criterioBusquedaServicio;
	
	/**
	 * Text en búsqueda avanzada para el especialidad 
	 */
	private String criterioBusquedaEspecialidad;
	
	/**
	 * Colección con los datos de los listados 
	 */
	private Collection col=null;

	/**
	 * Offset para el pager 
	 */
	private int offset=0;

	/**
	 * Este campo contiene el pageUrl para controlar el pager,
	 *  y conservar los valores del hashMap mediante un submit de
	 * JavaScript. (Integra pager -Valor Captura)
	 */
	private String linkSiguiente="";
	
	/**
	 * Contiene el mapa de las excepciones cuando se va
	 * ha modificar
	 */
	public  HashMap excepcionMap = new HashMap();
	
	/**
	 * Códigos de las excepciones que han sido modificados
	 * para poder mostrarlos en el resumen
	 */
	public Vector codigosExcepcionesModificadas= new Vector();
	
	/**
	 * almacena los logs
	 */
	public String log;
	
	/**
	 * Método que inicializa todos los atributos de la forma
	 */
	public void reset() 
	{
		//this.codigo = -1;
		this.porcentaje = 0.0;
		this.valorAjuste = 0.0;
		this.nuevaTarifa = 0.0;
		this.codigoViaIngreso = -1;
		this.nombreViaIngreso = "";
		this.codigoEspecialidad = -1;
		this.nombreEspecialidad = "";
		this.codigoServicio = -1;
		this.nombreServicio = "";
		this.codigoContrato = -1;
		this.numeroContrato = "";
		this.signoPorcentaje = ' ';
		this.signoValorAjuste = ' ';
		this.servicioRadio = -1;
		this.especialidadRadio = -1;
		this.radioExcepcion = -1;
		this.convenio=-1;
		this.radioBusquedaPorCodigoNombreEspecialidad=0;
		this.radioBusquedaPorCodigoNombreServicio=0;
		this.criterioBusquedaEspecialidad="";
		this.criterioBusquedaServicio="";
		this.linkSiguiente="";
		this.codigoExcepcion=0;
		this.excepcionMap=new HashMap();
		this.codigosExcepcionesModificadas= new Vector();
		this.log = "";
	}
	
	/**
	 * Validaciones con respecto al porcentaje, valorAjuste, nuevaTarifa
	 * @param i, indice del hashMap
	 * @return
	 */
	public ActionErrors validateHashMap(int i) 
	{
		ActionErrors errores = new ActionErrors();
		int tempoCodigoExcepcion=Integer.parseInt(getExcepcionMap("codigosExcepciones_"+i)+"");
			
		if(getExcepcionMap("porcentaje_"+i) !=null && !(getExcepcionMap("porcentaje_"+i)+"").equals(""))
		{
			try
			{
				double tempoPorcentaje= Double.valueOf(getExcepcionMap("porcentaje_"+i)+"").doubleValue();
				
				if(tempoPorcentaje<0.0)
				{
					errores.add("porcentaje mayor a cero", new ActionMessage("errors.floatMayorQue", "El porcentaje con código de excepción "+tempoCodigoExcepcion, "0"));
				}
				boolean validar= this.esMayorADosDecimales(tempoPorcentaje);
				
				if(validar)
				{
					errores.add("porcentaje mayor a cero", new ActionMessage("errors.numDecimales", "El porcentaje con código de excepción "+tempoCodigoExcepcion, "2"));
				}
			}
			catch(NumberFormatException e)
			{
				errores.add("porcentaje mayor a cero", new ActionMessage("errors.floatMayorQue", "El porcentaje con código de excepción "+tempoCodigoExcepcion, "0"));
			}
			
			if(getExcepcionMap("signoPorcentaje_"+i)==null && getExcepcionMap("signoPorcentaje1_"+i)==null) 
			{
				errores.add("seleccion signo porcentaje requerido", new ActionMessage("errors.seleccionsigno", "porcentaje con código de excepción "+tempoCodigoExcepcion));
			}
		}
		if(getExcepcionMap("valorAjuste_"+i) !=null && !(getExcepcionMap("valorAjuste_"+i)+"").equals(""))
		{
			try
			{
				double tempoValorAjuste= Double.valueOf(getExcepcionMap("valorAjuste_"+i)+"").doubleValue();
				if( tempoValorAjuste<0.0)
				{
					errores.add("valor ajuste mayor a cero", new ActionMessage("errors.floatMayorQue", "El valor de ajuste con código de excepción "+tempoCodigoExcepcion, "0"));
				}
				boolean validar= this.esMayorADosDecimales(tempoValorAjuste);
				if(validar)
				{
					errores.add("valor ajuste mayor a cero", new ActionMessage("errors.numDecimales", "El valor de ajuste con código de excepción "+tempoCodigoExcepcion , "2"));
				}
			}
			catch(NumberFormatException e)
			{
				errores.add("valor ajuste mayor a cero", new ActionMessage("errors.floatMayorQue", "El valor de ajuste con código de excepción "+tempoCodigoExcepcion, "0"));
			}
			if(getExcepcionMap("signoValorAjuste_"+i)==null && getExcepcionMap("signoValorAjuste1_"+i)==null) 
			{
				errores.add("seleccion signo valor Ajuste requerido", new ActionMessage("errors.seleccionsigno", "valor de ajuste con código de excepción "+tempoCodigoExcepcion));
			}
		}
		if(getExcepcionMap("nuevaTarifa_"+i) !=null && !(getExcepcionMap("nuevaTarifa_"+i)+"").equals(""))
		{
			try
			{
				double tempoNuevaTarifa= Double.valueOf(getExcepcionMap("nuevaTarifa_"+i)+"").doubleValue();
				if(tempoNuevaTarifa<0.0)
				{
					errores.add("nueva Tarifamayor a cero", new ActionMessage("errors.floatMayorQue", "La nueva Tarifa con código de excepción "+tempoCodigoExcepcion, "0"));
				}
			}
			catch(NumberFormatException e)
			{
				errores.add("nueva Tarifa mayor a cero", new ActionMessage("errors.floatMayorQue", "La nueva Tarifa con código de excepción "+tempoCodigoExcepcion, "0"));
			}
		}
		return errores;
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
		ActionErrors errores = new ActionErrors();
		
		if(estado.equals("listarModificar") || estado.equals("listarConsultar"))
		{
			String tempoEstado="";
			if(estado.equals("listarModificar")) tempoEstado="empezarBuscarModificar";
			else if(estado.equals("listarConsultar")) tempoEstado="empezarBuscarConsultar";
			
			if(!this.criterioBusquedaServicio.equals("") && this.criterioBusquedaServicio!=null)
			{
				if(this.radioBusquedaPorCodigoNombreServicio!=2 && this.radioBusquedaPorCodigoNombreServicio!=1)
				{
					errores.add("radio de búsqueda Servicio por código o por nombre", new ActionMessage("errors.required", "El criterio de búsqueda Servicio por código o por nombre"));
					this.estado=tempoEstado;
				}
				if(this.radioBusquedaPorCodigoNombreServicio==2)
				{
					try
					{
						Integer.parseInt(this.getCriterioBusquedaServicio());
					}
					catch(NumberFormatException e)
					{
						errores.add("", new ActionMessage("errors.integer", "Si realiza búsqueda de Servicio por código entonces el campo "));
						this.estado=tempoEstado;
					}
				}
			}
			if(!this.criterioBusquedaEspecialidad.equals("") && this.criterioBusquedaEspecialidad!=null)
			{
				if(this.radioBusquedaPorCodigoNombreEspecialidad!=2 && this.radioBusquedaPorCodigoNombreEspecialidad!=1)
				{
					errores.add("radio de búsqueda Especialidad por código o por nombre", new ActionMessage("errors.required", "El criterio de búsqueda Especialidad por código o por nombre"));
					this.estado=tempoEstado;
				}
				if(this.radioBusquedaPorCodigoNombreEspecialidad==2)
				{
					try
					{
						Utilidades.convertirAEntero(this.getCriterioBusquedaEspecialidad());
					}
					catch(NumberFormatException e)
					{
						errores.add("", new ActionMessage("errors.integer", "Si realiza búsqueda de Especialidad por código entonces el campo "));
						this.estado=tempoEstado;
					}
				}
			}
			// Validaciones porcentaje
			if( this.radioExcepcion == 1 )
			{
				try
				{
					if( Utilidades.convertirADouble(this.porcentaje+"") <= 0.0 )
					{
						errores.add("porcentaje mayor a cero", new ActionMessage("errors.floatMayorQue", "Si realiza búsqueda por (%) entonces el porcentaje", "0"));
						this.estado=tempoEstado;
						boolean validar= esMayorADosDecimales(this.porcentaje);
						
						if(validar)
						{
							errores.add("porcentaje mayor a cero", new ActionMessage("errors.numDecimales", "El porcentaje", "2"));
							this.estado=tempoEstado;
						}
					}
				}
				catch(NumberFormatException e)
				{
					errores.add("", new ActionMessage("errors.float", "Si realiza búsqueda por porcentaje excepción entonces el campo "));
					this.estado=tempoEstado;
				}	
				
				if( this.signoPorcentaje != '+' && this.signoPorcentaje != '-' )
				{
					errores.add("seleccion signo porcentaje requerido", new ActionMessage("errors.seleccionsigno", "porcentaje"));
					this.estado=tempoEstado;
				}
			}
			// Validaciones valor ajuste
			if( this.radioExcepcion == 2 )
			{
				try
				{
					if( Double.parseDouble(this.valorAjuste+"") <= 0.0 )
					{
						errores.add("valor ajuste mayor a cero", new ActionMessage("errors.floatMayorQue", "Si realiza búsqueda por valor de ajuste entonces el campo ", "0"));
						this.estado=tempoEstado;
						boolean validar= esMayorADosDecimales(this.valorAjuste);
						
						if(validar)
						{
							errores.add("valor ajuste mayor a cero", new ActionMessage("errors.numDecimales", "El Valor de Ajuste ", "2"));
							this.estado=tempoEstado;
						}
					}
				}
				catch(NumberFormatException e)
				{
					errores.add("", new ActionMessage("errors.float", "Si realiza búsqueda por valor de ajuste entonces el campo "));
					this.estado=tempoEstado;
				}	
				
				if( this.signoValorAjuste != '+' && this.signoValorAjuste != '-' )
				{
					errores.add("seleccion signo valor ajuste requerido", new ActionMessage("errors.seleccionsigno", "valor de ajuste"));
					this.estado=tempoEstado;
				}
			}
			if( this.radioExcepcion == 3 )
			{
				try
				{
					if( Double.parseDouble(this.nuevaTarifa+"") <= 0.0 )
					{
						errores.add("nueva Tarifa mayor a cero", new ActionMessage("errors.floatMayorQue", "Si realiza búsqueda por nueva Tarifa entonces el campo ", "0"));
						this.estado=tempoEstado;
					}
				}
				catch(NumberFormatException e)
				{
					errores.add("", new ActionMessage("errors.float", "Si realiza búsqueda por nueva Tarifa entonces el campo "));
					this.estado=tempoEstado;
				}	
			}
		}
		
		if( estado.equals("empezarInsertar") || estado.equals("empezarBuscarModificar"))
		{
			// Contrato requerido
			if( this.codigoContrato <= 0 )
			{
				errores.add("contrato requerido", new ActionMessage("errors.required", "El contrato"));
				this.estado="empezar";
			}
		}
		
		if( estado.equals("empezarBuscarConsultar"))
		{
			// Contrato requerido
			if( this.codigoContrato <= 0 )
			{
				errores.add("contrato requerido", new ActionMessage("errors.required", "El contrato"));
				this.estado="empezarConsultar";
			}
		}
		
		
		if( estado.equalsIgnoreCase("guardar") )
		{
			if( this.servicioRadio == 1 ) // Todos
			{
				this.codigoServicio = 0;
			}
			
			if( this.especialidadRadio == 1 ) // Todas
			{
				this.codigoEspecialidad = 0;
			}
			
			// Via de ingreso requerida
			if( this.codigoViaIngreso == -1 )
			{
				errores.add("via de ingreso requerida", new ActionMessage("errors.required", "La via de ingreso"));
			}
			
			// Contrato requerido
			if( this.codigoContrato <= 0 )
			{
				errores.add("contrato requerido", new ActionMessage("errors.required", "El contrato"));
			}
			
			// servicio requerido
			if( this.servicioRadio == -1 || this.codigoServicio == -1 )
			{
				errores.add("servicio requerido", new ActionMessage("errors.required", "El servicio"));
			}
			
			// especialidad requerida
			if( this.especialidadRadio == -1 || this.codigoEspecialidad == -1 )
			{
				errores.add("especialidad requerida", new ActionMessage("errors.required", "La especialidad"));
			}
			
			// especificacion de la excepcion requerida
			if( this.radioExcepcion == -1 || this.radioExcepcion == 0 )  // No selecciono ningun tipo de excepcion
			{
				errores.add("porcentaje, valor ajuste o nueva tarifa de la excepcion de tarifa requerido", new ActionMessage("errors.required", "La excepción (porcentaje, valor ajuste o nuevo valor)"));
			}
			
			// Validaciones porcentaje
			if( this.radioExcepcion == 1 )
			{
				if( this.porcentaje <= 0.0 )
				{
					errores.add("porcentaje mayor a cero", new ActionMessage("errors.floatMayorQue", "El porcentaje", "0"));
				}			
				
				boolean validar= esMayorADosDecimales(this.porcentaje);
				
				if(validar)
				{
					errores.add("porcentaje mayor a cero", new ActionMessage("errors.numDecimales", "El porcentaje", "2"));
				}
				
				if( this.signoPorcentaje != '+' && this.signoPorcentaje != '-' )
				{
					errores.add("seleccion signo porcentaje requerido", new ActionMessage("errors.seleccionsigno", "porcentaje"));
				}
			}
			
			// Validaciones valor ajuste
			if( this.radioExcepcion == 2 )
			{
				if( this.valorAjuste <= 0.0 )
				{
					errores.add("valor ajuste mayor a cero", new ActionMessage("errors.floatMayorQue", "El valor de ajuste", "0"));
				}	
				
				boolean validar= esMayorADosDecimales(this.valorAjuste);
				
				if(validar)
				{
					errores.add("valor ajuste mayor a cero", new ActionMessage("errors.numDecimales", "El Valor de Ajuste ", "2"));
				}
					
				if( this.signoValorAjuste != '+' && this.signoValorAjuste != '-' )
				{
					errores.add("seleccion signo valor ajuste requerido", new ActionMessage("errors.seleccionsigno", "valor de ajuste"));
				}
			}
			
			//			Validaciones nueva tarifa
			if( this.radioExcepcion == 3 )
			{
				if( this.nuevaTarifa <= 0.0 )
				{
					errores.add("nueva tarifa mayor a cero", new ActionMessage("errors.floatMayorQue", "La nueva tarifa", "0"));
				}				
			}
			
			// Si selecciona el codigo de servicio la especialidad no puede ser todas
			if( this.codigoServicio > 0 && this.codigoEspecialidad == 0 )
			{
				errores.add("", new ActionMessage("error.excepcion.serviciodefinidoespecialidadtodas"));				
			}
			
			if( errores != null && errores.isEmpty() )
			{
				// Para asignar el signo seleccionado
				if( this.radioExcepcion == 1 )
				{
					if( this.signoPorcentaje == '-' )
						this.porcentaje = -this.porcentaje;
				}
				else
				if( this.radioExcepcion == 2 )
				{
					if( this.signoValorAjuste == '-' )
						this.valorAjuste = -this.valorAjuste;
				}
			}			
		}
		
		return errores;
	}	
	
	
	public boolean esMayorADosDecimales(double a)
	{
		String temp= a+"";
		int j=0;
		for(int i=0; i<temp.length(); i++)
		{
			if(temp.charAt(i)=='.')
			{
				j=temp.length()-i-1;
			}
		}
		if(j>2)
		return true;
		else
		return false;
	}
	
	/**
	 * Retorna el porcentaje de descuento o aumento de la tarifa
	 * @return
	 */
	public double getPorcentaje()
	{
		return porcentaje;
	}

	/**
	 * Asigna el porcentaje de descuento o aumento de la tarifa
	 * @param porcentaje
	 */
	public void setPorcentaje(double porcentaje)
	{
		this.porcentaje = porcentaje;
	}

	/**
	 * Retorna el valor de ajuste de la excepción
	 * @return
	 */
	public double getValorAjuste()
	{
		return valorAjuste;
	}

	/**
	 * Asigna el valor de ajuste de la excepción
	 * @param valorAjuste
	 */
	public void setValorAjuste(double valorAjuste)
	{
		this.valorAjuste = valorAjuste;
	}

	/**
	 * Retorna la nueva tarifa de la excepción
	 * @return
	 */
	public double getNuevaTarifa()
	{
		return nuevaTarifa;
	}

	/**
	 * Asigna la nueva tarifa de la excepción
	 * @param nuevaTarifa
	 */
	public void setNuevaTarifa(double nuevaTarifa)
	{
		this.nuevaTarifa = nuevaTarifa;
	}

	/**
	 * Retorna el código del contrato asociado a la excepción
	 * @return
	 */
	public int getCodigoContrato()
	{
		return codigoContrato;
	}

	/**
	 * Asigna el código del contrato asociado a la excepción
	 * @param codigoContrato
	 */
	public void setCodigoContrato(int codigoContrato)
	{
		this.codigoContrato = codigoContrato;
	}

	/**
	 * Retorna el código del servicio asociado a la excepción 
	 * @return
	 */
	public int getCodigoServicio()
	{
		return codigoServicio;
	}

	/**
	 * Asigna el código del servicio asociado a la excepción 
	 * @param codigoServicio
	 */
	public void setCodigoServicio(int codigoServicio)
	{
		this.codigoServicio = codigoServicio;
	}

	/**
	 * Retorna el código especialidad asociada a la excepcion
	 * @return
	 */
	public int getCodigoEspecialidad()
	{
		return codigoEspecialidad;
	}

	/**
	 * Asigna el código especialidad asociada a la excepcion
	 * @param codigoEspecialidad 
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad)
	{
		this.codigoEspecialidad = codigoEspecialidad;
	}

	/**
	 * Retorna el ódigo de la Via de ingreso asociada a la excepcion
	 * @return
	 */
	public int getCodigoViaIngreso()
	{
		return codigoViaIngreso;
	}

	/**
	 * Asigna el código de la Via de ingreso asociada a la excepcion
	 * @param codigoViaIngreso
	 */
	public void setCodigoViaIngreso(int codigoViaIngreso)
	{
		this.codigoViaIngreso = codigoViaIngreso;
	}

	/**
	 * Retorna el estado dentro del flujo
	 * @return
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * Asigna el estado dentro del flujo
	 * @param estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * Retorna el signo del porcentaje "+" o "-"
	 * @return
	 */
	public char getSignoPorcentaje()
	{
		return signoPorcentaje;
	}

	/**
	 * Asigna el signo del porcentaje "+" o "-"
	 * @param c
	 */
	public void setSignoPorcentaje(char c)
	{
		signoPorcentaje = c;
	}

	/**
	 * Retorna el signo del valor del ajuste "+" o "-"
	 * @return
	 */
	public char getSignoValorAjuste()
	{
		return signoValorAjuste;
	}

	/**
	 * Asigna el signo del valor del ajuste "+" o "-"
	 * @param c
	 */
	public void setSignoValorAjuste(char c)
	{
		signoValorAjuste = c;
	}

	/**
	 * Retorna 0 cuando el servicio está defindo, 1 cuando es "todos"
	 * @return
	 */
	public int getServicioRadio()
	{
		return servicioRadio;
	}

	/**
	 * Asigna 0 cuando el servicio está defindo, 1 cuando es "todos"
	 * @param servicioRadio
	 */
	public void setServicioRadio(int servicioRadio)
	{
		this.servicioRadio = servicioRadio;
	}

	/**
	 * Retorna 0 cuando la especialidad está definda, 1 cuando es "todas"
	 * @return
	 */
	public int getEspecialidadRadio()
	{
		return especialidadRadio;
	}

	/**
	 * Asigna 0 cuando la especialidad está definda, 1 cuando es "todas"
	 * @param especialidadRadio
	 */
	public void setEspecialidadRadio(int especialidadRadio)
	{
		this.especialidadRadio = especialidadRadio;
	}

	/**
	 * Retorna 1, si la opción seleccionada es porcentaje, 2 si es valor ajuste, 3 si
	 * es nueva tarifa
	 * @return
	 */
	public int getRadioExcepcion()
	{
		return radioExcepcion;
	}

	/**
	 * Asigna 1, si la opción seleccionada es porcentaje, 2 si es valor ajuste, 3 si
	 * es nueva tarifa
	 * @param radioExcepcion
	 */
	public void setRadioExcepcion(int radioExcepcion)
	{
		this.radioExcepcion = radioExcepcion;
	}
	/**
	 * @return Returns the nombreEspecialidad.
	 */
	public String getNombreEspecialidad() {
		return nombreEspecialidad;
	}
	/**
	 * @param nombreEspecialidad The nombreEspecialidad to set.
	 */
	public void setNombreEspecialidad(String nombreEspecialidad) {
		this.nombreEspecialidad = nombreEspecialidad;
	}
	/**
	 * @return Returns the nombreServicio.
	 */
	public String getNombreServicio() {
		return nombreServicio;
	}
	/**
	 * @param nombreServicio The nombreServicio to set.
	 */
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}
	/**
	 * @return Returns the nombreViaIngreso.
	 */
	public String getNombreViaIngreso() {
		return nombreViaIngreso;
	}
	/**
	 * @param nombreViaIngreso The nombreViaIngreso to set.
	 */
	public void setNombreViaIngreso(String nombreViaIngreso) {
		this.nombreViaIngreso = nombreViaIngreso;
	}
	/**
	 * @return Returns the numeroContrato.
	 */
	public String getNumeroContrato() {
		return numeroContrato;
	}
	/**
	 * @param numeroContrato The numeroContrato to set.
	 */
	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}
	/**
	 * @return Returns the convenio.
	 */
	public int getConvenio() {
		return convenio;
	}
	/**
	 * @param convenio The convenio to set.
	 */
	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}
	/**
	 * @return Returns the radioBusquedaPorCodigoNombreEspecialidad.
	 */
	public int getRadioBusquedaPorCodigoNombreEspecialidad() {
		return radioBusquedaPorCodigoNombreEspecialidad;
	}
	/**
	 * @param radioBusquedaPorCodigoNombreEspecialidad The radioBusquedaPorCodigoNombreEspecialidad to set.
	 */
	public void setRadioBusquedaPorCodigoNombreEspecialidad(
			int radioBusquedaPorCodigoNombreEspecialidad) {
		this.radioBusquedaPorCodigoNombreEspecialidad = radioBusquedaPorCodigoNombreEspecialidad;
	}
	/**
	 * @return Returns the radioBusquedaPorCodigoNombreServicio.
	 */
	public int getRadioBusquedaPorCodigoNombreServicio() {
		return radioBusquedaPorCodigoNombreServicio;
	}
	/**
	 * @param radioBusquedaPorCodigoNombreServicio The radioBusquedaPorCodigoNombreServicio to set.
	 */
	public void setRadioBusquedaPorCodigoNombreServicio(
			int radioBusquedaPorCodigoNombreServicio) {
		this.radioBusquedaPorCodigoNombreServicio = radioBusquedaPorCodigoNombreServicio;
	}
	/**
	 * @return Returns the criterioBusquedaEspecialidad.
	 */
	public String getCriterioBusquedaEspecialidad() {
		return criterioBusquedaEspecialidad;
	}
	/**
	 * @param criterioBusquedaEspecialidad The criterioBusquedaEspecialidad to set.
	 */
	public void setCriterioBusquedaEspecialidad(
			String criterioBusquedaEspecialidad) {
		this.criterioBusquedaEspecialidad = criterioBusquedaEspecialidad;
	}
	/**
	 * @return Returns the criterioBusquedaServicio.
	 */
	public String getCriterioBusquedaServicio() {
		return criterioBusquedaServicio;
	}
	/**
	 * @param criterioBusquedaServicio The criterioBusquedaServicio to set.
	 */
	public void setCriterioBusquedaServicio(String criterioBusquedaServicio) {
		this.criterioBusquedaServicio = criterioBusquedaServicio;
	}
	/**
	 * Retorna Colección para mostrar datos en el pager
	 * @return
	 */
	public Collection getCol() {
		return col;
	}
	/**
	 * Asigna Colección para mostrar datos en el pager
	 * @param collection
	 */
	public void setCol(Collection collection) {
		col = collection;
	}

	public int getColSize()
	{
		if(col!=null)
			return col.size();
		else
			return 0;
	}
	/**
	 * Retorna Offset del pager
	 * @return
	 */
	public int getOffset()
	{
		return offset;
	}
	/**
	 * Asigna Offset del pager
	 * @param i
	 */
	public void setOffset(int i) 
	{
		offset = i;
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
	 * @return Returns the codigoExcepcion.
	 */
	public int getCodigoExcepcion() {
		return codigoExcepcion;
	}
	/**
	 * @param codigoExcepcion The codigoExcepcion to set.
	 */
	public void setCodigoExcepcion(int codigoExcepcion) {
		this.codigoExcepcion = codigoExcepcion;
	}
	/**
	 * Set del mapa de excepcion
	 * @param key
	 * @param value
	 */
	public void setExcepcionMap(String key, Object value) 
	{
		excepcionMap.put(key, value);
	}
	/**
	 * Get del mapa de excepcion
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getExcepcionMap(String key) 
	{
		return excepcionMap.get(key);
	}

	/**
	 * @return Returns the excepcionMap.
	 */
	public HashMap getExcepcionMap() {
		return excepcionMap;
	}
	/**
	 * @param excepcionMap The excepcionMap to set.
	 */
	public void setExcepcionMap(HashMap excepcionMap) {
		this.excepcionMap = excepcionMap;
	}

	/**
	 * @return Returns the codigosExcepcionesModificadas.
	 */
	public Vector getCodigosExcepcionesModificadas() {
		return codigosExcepcionesModificadas;
	}
	/**
	 * @param codigosExcepcionesModificadas The codigosExcepcionesModificadas to set.
	 */
	public void setCodigosExcepcionesModificadas(
			Vector codigosExcepcionesModificadas) {
		this.codigosExcepcionesModificadas = codigosExcepcionesModificadas;
	}
	
	/**
	 * Set del vector de codigos excepciones modificadas
	 * @param key
	 * @param value
	 */
	public void setCodigosExcepcionesModificadas(int index, Object value) 
	{
		codigosExcepcionesModificadas.add(index, value);
	}
	
	/**
	 * Get del vector de excepciones modificadas
	 * Retorna el valor de un campo dado su index
	 */
	public Object getCodigosExcepcionesModificadas(int  index) 
	{
		return codigosExcepcionesModificadas.get(index);
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
}
