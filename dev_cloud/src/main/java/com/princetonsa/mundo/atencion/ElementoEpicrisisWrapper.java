/*
 * @(#)ElementoEpicrisisWrapper.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01 
 *
 */

package com.princetonsa.mundo.atencion;

/**
 * Clase cuyos objetos se usarán para llenar los listados
 * genéricos (ordenados por fecha y hora) usados en
 * epicrisis. Implementa comparable para permitir este 
 * orden. La fecha debe almacenarse en formato 
 * AAAA-MM-DD (formato usado en BD), debido a que
 * de esta forma las comparaciones son mucho menos
 * intensivas en tiempo de ejecución
 *  
 * @version 1.0 Nov 13, 2003
 */

public class ElementoEpicrisisWrapper implements Comparable
{

	/**
	 * String con la fecha de creación de este elemento
	 * (Debe estar en formato AAAA-MM-DD)
	 */
	private String fecha;

	/**
	 * String con la hora de creación de este elemento
	 */
	private String hora;
	
	/**
	 * Tipo guardado en el atributo elemento 
	 */
	private int tipo;
	
	/**
	 * Objeto genérico con el elemento almacenado
	 */
	private Object elemento;

	/**
	 * Constantes válidas para el tipo
	 */
	public static final int VALORACION_HOSPITALARIA=1;
	public static final int VALORACION_URGENCIAS=2;
	public static final int VALORACION_PEDIATRICA_HOSPITALARIA=3;
	public static final int VALORACION_PEDIATRICA_URGENCIAS=4;
	public static final int VALORACION_INTERCONSULTA_HOSPITALARIA=5;
	public static final int VALORACION_INTERCONSULTA_URGENCIAS=6;
	public static final int VALORACION_INTERCONSULTA_PEDIATRICA_HOSPITALARIA=7;
	public static final int VALORACION_INTERCONSULTA_PEDIATRICA_URGENCIAS=8;
	//No va la última evolución pues esta tiene que aparecer junto al egreso
	//no es un item único
	public static final int PRIMERA_EVOLUCION=9;
	public static final int EVOLUCION_HOSPITALARIA=10;
	public static final int EVOLUCION_URGENCIAS=11;
	public static final int EVOLUCION_GENERAL=12;
	public static final int EGRESO=13;
	public static final int SOLICITUD_PROCEDIMIENTO=14;
	public static final int SOLICITUD_INTERCONSULTA=15;
	public static final int RESPUESTA_PROCEDIMIENTO=16;
	
	

	/**
	 * Constructor de esta clase, que recibe todos los atributos
	 * del objeto. Es la única manera de establecer estos datos,
	 * ya que no tienen set para evitar cambios información a
	 * mitad de camino
	 * 
	 * @param fecha Fecha en la que se creó el elemento que
	 * se encuentra dentro de este wrapper
	 * @param hora
	 * @param tipo
	 * @param elemento
	 */
	public ElementoEpicrisisWrapper (String fecha, String hora, int tipo, Object elemento)
	{
		this.fecha=fecha;
		this.hora=hora;
		this.tipo=tipo;
		this.elemento=elemento;	
	}

	/**
	 * Método que define como comparar dos objetos de tipo 
	 * ElementoEpicrisisWrapper. Primero se compara la fecha
	 * si es mayor este elemento es mayor y si las fechas son
	 * iguales se comparan las horas, usando el mismo criterio
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o)
	{

		//Si los dos son nulos que se puede hacer?, 
		//acá por defecto decimos que son iguales
		if (o==null)
		{
			return 0;
		}
		
		ElementoEpicrisisWrapper elemento2=(ElementoEpicrisisWrapper)o;
		
		//De nuevo tomamos los casos nulos
		
		if (this.getFecha()==null||this.getHora()==null||elemento2.getFecha()==null||elemento2.getHora()==null)
		{
			return 0;
		}
		
		//Si las fecha del primero es mayor que la del segundo
		//retornamos numero positivo
		int comparacionFecha=this.getFecha().compareTo(elemento2.getFecha());
		
		if (comparacionFecha>0)
		{
			return 1;
		}
		else if (comparacionFecha<0)
		{
			return -1;
		}
		else
		{
			//Fecha igual, criterio lo da la hora
			if (this.getHora().compareTo(elemento2.getHora())==0)
			{
				return 1;
			}
			else
			{
				return this.getHora().compareTo(elemento2.getHora());
			}
			
		}
		
	}
	//No se implementa equals por recomendacion

	/**
	 * @return
	 */
	public Object getElemento()
	{
		return elemento;
	}

	/**
	 * @return
	 */
	public String getFecha()
	{
		return fecha;
	}

	/**
	 * @return
	 */
	public String getHora()
	{
		return hora;
	}

	/**
	 * @return
	 */
	public int getTipo()
	{
		return tipo;
	}

}
