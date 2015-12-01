package com.princetonsa.sort.odontologia;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.InfoDatosInt;
import util.InfoDatosStr;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.action.odontologia.ServicioHonorarioAction;



public class SortGenerico  implements Comparator<Object> {


	/**
	 * @return the segundoPatron
	 */
	public String getSegundoPatron() {
		return segundoPatron;
	}



	/**
	 * @param segundoPatron the segundoPatron to set
	 */
	public void setSegundoPatron(String segundoPatron) {
		this.segundoPatron = segundoPatron;
	}

	private static Logger logger = Logger.getLogger(ServicioHonorarioAction.class);
	/**
	 * 
	 */
	private String patronOrdenar;

	private boolean descendente;

	private String  segundoPatron;




	public SortGenerico(String patronOrdenarNuevo , boolean tipo) {
		this.patronOrdenar = patronOrdenarNuevo;
		this.descendente = tipo;
	}



	
	
	@Override
	public int compare(Object one, Object two) 
	{
		Object resultado=new Object();
		Object resultado2=new Object();
		
		Class<? extends Object> clase=one.getClass();
		Method metodo = null;
		try 
		{ 
			try
			{
				logger.info(metodo + " ==============   " +"get"+this.getPatronOrdenar());
				metodo = clase.getMethod("get"+this.getPatronOrdenar());
			}
			catch (NoSuchMethodException e) 
			{
				metodo = clase.getMethod("is"+this.getPatronOrdenar());
			}
			catch (NoSuchMethodError e) 
			{
				metodo = clase.getMethod("is"+this.getPatronOrdenar());
			}

			resultado=metodo.invoke(one, (Object[])null);
			resultado2=metodo.invoke(two, (Object[])null);
			logger.info("Resultado 1"+resultado);
			logger.info("Resultado 2"+resultado2);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		String valor1="";
		String valor2="";

		if(resultado instanceof InfoDatosStr)
		{
			InfoDatosStr res1=(InfoDatosStr)resultado;
			InfoDatosStr res2=(InfoDatosStr)resultado2;
			valor1=res1.getNombre().toUpperCase();
			valor2=res2.getNombre().toUpperCase();
			logger.info(valor1 + " <<< infostr >>> "+valor2);
			if(this.isDescendente())
			{
				return ( (valor1).compareTo((valor2)));
			}
			else
			{
				return ((valor1).compareTo((valor2)))*-1;
			}
		}
		else if(resultado instanceof InfoDatosInt)
		{
			InfoDatosInt res1=(InfoDatosInt)resultado;
			InfoDatosInt res2=(InfoDatosInt)resultado2;
			valor1=String.valueOf(res1.getNombre().toUpperCase());
			valor2=String.valueOf(res2.getNombre().toUpperCase());
			if(this.isDescendente())
			{
				return ( (valor1).compareTo((valor2)));
			}
			else
			{
				return ((valor1).compareTo((valor2)))*-1;
			}
		}
		else if(resultado instanceof BigDecimal)
		{
			valor1=  resultado.toString().toUpperCase();
			valor2= resultado2.toString().toUpperCase();

			if(this.isDescendente())
			{
				return (new Double(valor1).compareTo(new Double(valor2)));	
			}
			else
			{
				return (new Double(valor1).compareTo(new Double(valor2)))*-1;
			}
		}
		else if(resultado instanceof Number)
		{
			valor1=resultado.toString().toUpperCase();
			valor2=resultado2.toString().toUpperCase();
			
			if(this.isDescendente())
			{
				return (new Double(valor1).compareTo(new Double(valor2)));
			}
			else
			{
				return (new Double(valor2).compareTo(new Double(valor1)));
			}
		}
		else if( resultado instanceof Date && resultado2 instanceof Date)
		{	
			try{
				valor1=UtilidadFecha.conversionFormatoFechaABD((Date)resultado); 
				valor2=UtilidadFecha.conversionFormatoFechaABD((Date)resultado2);
			}
			catch (Exception e) {
				Log4JManager.info(e);
			}
			
			if(this.isDescendente())
			{
				return (((Date)resultado).compareTo((Date)resultado2));
			}
			else
			{
				return (((Date)resultado).compareTo((Date)resultado2))*-1;
			}
			
		}
		else
		{
			if(resultado==null && resultado2==null)
			{
				return 0;
			}
			if(resultado==null)
			{
				return -1;
			}
			if(resultado2==null)
			{
				return 1;
			}
			valor1=resultado.toString().toUpperCase();
			valor2=resultado2.toString().toUpperCase();
			
			try{
				Double valorInt1=Double.parseDouble(valor1);
				Double valorInt2=Double.parseDouble(valor2);
				
				if(this.isDescendente())
				{
					return valorInt1.compareTo(valorInt2);
				}
				else
				{
					return valorInt2.compareTo(valorInt1);
				}
			}catch (Exception e) {
				Log4JManager.info(e);
			}
			
			try{
				Integer valorInt1=Integer.parseInt(valor1);
				Integer valorInt2=Integer.parseInt(valor2);
				
				if(this.isDescendente())
				{
					return valorInt1.compareTo(valorInt2);
				}
				else
				{
					return valorInt2.compareTo(valorInt1);
				}
			}catch (Exception e) {
				Log4JManager.info(e);
			}
			
			
			if(this.isDescendente())
			{
				return (valor1.compareTo(valor2));
			}
			else
			{
				return (valor1.compareTo(valor2))*-1;
			}
		}
	}

	/**
	 * 
	 * @param o
	 * @return
	 */
	public boolean validarPrimitivo(Object o){
		return (o instanceof Integer || o instanceof Double); 
	}
	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the descendente
	 */
	public boolean isDescendente() {
		return descendente;
	}

	/**
	 * @return the descendente
	 */
	public boolean getDescendente() {
		return descendente;
	}

	/**
	 * @param descendente the descendente to set
	 */
	public void setDescendente(boolean descendente) {
		this.descendente = descendente;
	}

}
