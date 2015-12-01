/*
 * Created on 6/10/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package util; 


/**
  * @author artotor
  * Clase que retorna el valor en letras de un numero.
  */
 public class UtilidadN2T 
 {
 	
 	/**
 	 * Bandera que me indica si se refiere a unidades, miles, cienmiles, millones, etc..
 	 */
     private  static int flag;
     
     /**
      * Metodo para convertir la unidad a letras.
      * @param numero
      * @return numeroLetra.
      */
     public static String unidad(int numero)
     {
     	String numeroLetra="";
     	switch (numero)
 		{
 			case 9:
 				numeroLetra = "Nueve";
 				break;
 			case 8:
             	numeroLetra = "Ocho";
             	break;
             case 7:
             	numeroLetra = "Siete";
             	break;
             case 6:
             	numeroLetra = "Seis";
             	break;
             case 5:
             	numeroLetra = "Cinco";
             	break;
             case 4:
             	numeroLetra = "Cuatro";
             	break;
             case 3:
             	numeroLetra = "Tres";
             	break;
             case 2:
             	numeroLetra = "Dos";
             	break;
             case 1:
             	if (flag == 0)
             		numeroLetra = "Uno";
             	else 
             		numeroLetra = "Un";
             	break;
             case 0:
             	numeroLetra = "";
             	break;
 		}
     	return numeroLetra;
     }
     
     /**
      * Metodo que convierte la decena a letras.
      * @param numero
      * @return numeroLetras
      */
     public static String decena(int numero)
 	{
 		String numeroLetra="";
 		if (numero >= 90 && numero <= 99)
 		{
 			numeroLetra = "Noventa ";
 			if (numero > 90)
 				numeroLetra = numeroLetra.concat("y ").concat(unidad(numero - 90));
         }
 		else if (numero >= 80 && numero <= 89)
         {
 			numeroLetra = "Ochenta ";
 			if (numero > 80)
 				numeroLetra = numeroLetra.concat("y ").concat(unidad(numero - 80));
         }
 		else if (numero >= 70 && numero <= 79)
 		{
 			numeroLetra = "Setenta ";
 			if (numero > 70)
 				numeroLetra = numeroLetra.concat("y ").concat(unidad(numero - 70));
 		}
 		else if (numero >= 60 && numero <= 69)
 		{
 			numeroLetra = "Sesenta ";
 			if (numero > 60)
 				numeroLetra = numeroLetra.concat("y ").concat(unidad(numero - 60));
 		}
 		else if (numero >= 50 && numero <= 59)
 		{
 			numeroLetra = "Cincuenta ";
 			if (numero > 50)
 				numeroLetra = numeroLetra.concat("y ").concat(unidad(numero - 50));
 		}
 		else if (numero >= 40 && numero <= 49)
 		{
 			numeroLetra = "Cuarenta ";
 			if (numero > 40)
 				numeroLetra = numeroLetra.concat("y ").concat(unidad(numero - 40));
 		}
 		else if (numero >= 30 && numero <= 39)
 		{
 			numeroLetra = "Treinta ";
 			if (numero > 30)
 				numeroLetra = numeroLetra.concat("y ").concat(unidad(numero - 30));
 		}
 		else if (numero >= 20 && numero <= 29)
 		{
 			if (numero == 20)
 				numeroLetra = "Veinte ";
 			else
 				numeroLetra = "veinti".concat(unidad(numero - 20));
 		}
 		else if (numero >= 10 && numero <= 19)
 		{
 			switch (numero)
 			{
 				case 10:
 						numeroLetra = "Diez ";
 						break;
 				case 11:
 						numeroLetra = "Once ";
 						break;
 				case 12:
 						numeroLetra = "Doce ";
 						break;
 				case 13:
 						numeroLetra = "Trece ";
 						break;
 				case 14:
 						numeroLetra = "Catorce ";
 						break;
 				case 15:
 						numeroLetra = "Quince ";
 						break;
 				case 16:
 						numeroLetra = "Dieciseis ";
 						break;
 				case 17:
 						numeroLetra = "Diecisiete ";
 						break;
 				case 18:
 						numeroLetra = "Dieciocho ";
 						break;
 				case 19:
 						numeroLetra = "Diecinueve ";
 						break;
 			}       
 		}
 		else
 			numeroLetra = unidad(numero);
 		return numeroLetra;
 	}       

 	
 	/**
 	 * Metodo que retorna los numeros de centenas en letras.
 	 * @param numero
 	 * @return numeroLetras.
 	 */
     public  static String centena(int numero)
 	{
     	String numeroLetra="";
     	if (numero >= 100)
     	{
     		if (numero >= 900 && numero <= 999)
     		{
     			numeroLetra = "Novecientos ";
     			if (numero > 900)
     				numeroLetra = numeroLetra.concat(decena(numero - 900));
     		}
     		else if (numero >= 800 && numero <= 899)
     		{
     			numeroLetra = "Ochocientos ";
     			if (numero > 800)
     				numeroLetra = numeroLetra.concat(decena(numero - 800));
     		}
     		else if (numero >= 700 && numero <= 799)
     		{
     			numeroLetra = "Setecientos ";
     			if (numero > 700)
     				numeroLetra = numeroLetra.concat(decena(numero - 700));
     		}
     		else if (numero >= 600 && numero <= 699)
     		{
     			numeroLetra = "Seiscientos ";
     			if (numero > 600)
     				numeroLetra = numeroLetra.concat(decena(numero - 600));
     		}
     		else if (numero >= 500 && numero <= 599)
     		{
     			numeroLetra = "Quinientos ";
     			if (numero > 500)
     				numeroLetra = numeroLetra.concat(decena(numero - 500));
     		}
     		else if (numero >= 400 && numero <= 499)
     		{
     			numeroLetra = "Cuatrocientos ";
     			if (numero > 400)
     				numeroLetra = numeroLetra.concat(decena(numero - 400));
     		}
     		else if (numero >= 300 && numero <= 399)
     		{
     			numeroLetra = "Trescientos ";
     			if (numero > 300)
     				numeroLetra = numeroLetra.concat(decena(numero - 300));
     		}
     		else if (numero >= 200 && numero <= 299)
     		{
     			numeroLetra = "Doscientos ";
     			if (numero > 200)
     				numeroLetra = numeroLetra.concat(decena(numero - 200));
     		}
     		else if (numero >= 100 && numero <= 199)
     		{
     			if (numero == 100)
     				numeroLetra = "Cien ";
 				else
 					numeroLetra = "Ciento ".concat(decena(numero - 100));
     		}
     	}
     	else
     		numeroLetra = decena(numero);
     	return numeroLetra;       
 	}       

     /**
      * Metodo que retorna un numero de miles en letras
      * @param numero 
      * @return numeroLetras
      */
 	public static String miles(int numero)
 	{
 		String numeroLetra="";
 		if (numero >= 1000 && numero <2000)
 		{
 			numeroLetra = ("Mil ").concat(centena(numero%1000));
 		}
 		if (numero >= 2000 && numero <10000)
 		{
 			flag=1;
 			numeroLetra = unidad(numero/1000).concat(" Mil ").concat(centena(numero%1000));
 		}
 		if (numero < 1000)
 			numeroLetra = centena(numero);
 		return numeroLetra;
 	}            

 	/**
 	 * Metodo que retorna un numero de decmiles en letras
 	 * @param numero
 	 * @return numeroLetras
 	 */
 	public static String decmiles(int numero)
 	{
     	String numeroLetra="";
     	if (numero == 10000)
     		numeroLetra = "Diez mil";
     	if (numero > 10000 && numero <20000)
     	{
     		flag=1;
     		numeroLetra = decena(numero/1000).concat("Mil ").concat(centena(numero%1000));          
     	}
     	if (numero >= 20000 && numero <100000)
     	{
     		flag=1;
     		numeroLetra = decena(numero/1000).concat(" Mil ").concat(miles(numero%1000));           
     	}
     	if (numero < 10000)
     		numeroLetra = miles(numero);
     	return numeroLetra;
     }               

 	/**
 	 * Metodo que retorna un numero cienmiles en letras.
 	 * @param numero
 	 * @return numeroLetras
 	 */
 	public static String cienmiles(int numero)
 	{
 		String numeroLetras="";
 		if (numero == 100000)
 			numeroLetras = "Cien mil";
 		if (numero >= 100000 && numero <1000000)
 		{
 			flag=1;
 			numeroLetras = centena(numero/1000).concat(" Mil ").concat(centena(numero%1000));                
 		}
 		if (numero < 100000)
 			numeroLetras = decmiles(numero);
 		return numeroLetras;
 	}

 	/**
 	 * Metodo que retorna un numero millones en letra
 	 * @param numero
 	 * @return numeroLetras
 	 */
     public static String millon(int numero)
 	{
     	String numeroLetras="";
     	if (numero >= 1000000 && numero <2000000)
     	{
     		flag=1;
     		numeroLetras = ("Un Millon ").concat(cienmiles(numero%1000000));
     	}
     	if (numero >= 2000000 && numero <10000000)
     	{
     		flag=1;
     		numeroLetras = unidad(numero/1000000).concat(" Millones ").concat(cienmiles(numero%1000000));
     	}
     	if (numero < 1000000)
     		numeroLetras = cienmiles(numero);
     	return numeroLetras;
 	}
     
     /**
      * Metodo que retorna un numero decmillones en letra
      * @param numero
      * @return numeroLetras
      */
     public static String decmillon(int numero)
 	{
     	String numeroLetras="";
     	if (numero == 10000000)
     		numeroLetras = "Diez Millones";
     	if (numero > 10000000 && numero <20000000)
     	{
     		flag=1;
     		numeroLetras = decena(numero/1000000).concat(" Millones ").concat(cienmiles(numero%1000000));            
     	}
     	if (numero >= 20000000 && numero <100000000)
     	{
     		flag=1;
     		numeroLetras = decena(numero/1000000).concat(" Milllones ").concat(millon(numero%1000000));             
     	}
     	if (numero < 10000000)
     		numeroLetras = millon(numero);
     	return numeroLetras;
 	}

     /**
      * Metodo que retorna un numero cinmillones en letra
      * @param numero
      * @return numeroLetras
      */
 	public static String cienmillones(double numero)
 	{
 		String numeroLetras="";
 		if (numero >= 100000000 && numero <1000000000)
 		{
 			flag=1;
 			numeroLetras = ((centena((int)(numero/1000000))).concat(" Millones ")).concat(cienmiles((int)(numero%1000000)));            
 		}
 		if(numero < 100000000)
 			numeroLetras=decmillon((int)numero);
 		return numeroLetras;
 	}
 	

 	/**
 	 * Metodo que retorna un numero en letras para un rango de miles de millones
 	 * @param numero
 	 * @return numeroLetras
 	 */
     public static String milmillones(double numero)
     {
         String numeroCadena="";
         if (numero >= 1000000000 && numero<Double.parseDouble("10000000000"))
         {
                 flag=1;
                 numeroCadena = ((miles((int)(numero/1000000))).concat(" Millones ")).concat(cienmiles((int)(numero%1000000)));
         }
         if(numero < 1000000000)
                 numeroCadena=cienmillones(numero);
         return numeroCadena;
 	}
     
     /**
      * Metodo que retorna un numero en el rango de dec mil millones.
      * @param numero
      * @return numeroLetras
      */
     public static String decmilmillones(double numero)
 	{
     	String numeroLetras="";
     	if (numero == Double.parseDouble("10000000000"))
     		numeroLetras = "Diez Mil Millones";
     	if (numero > Double.parseDouble("10000000000") && numero <Double.parseDouble("20000000000"))
     	{
     		flag=1;
     		numeroLetras = decena((int)(numero/1000000000)).concat(" Mil ").concat(cienmillones((int)(numero%1000000000)));            
     	}
     	if (numero >= Double.parseDouble("20000000000") && numero <Double.parseDouble("100000000000"))
     	{
     		flag=1;
     		numeroLetras = cienmiles((int)(numero/1000000)).concat(" Milllones ").concat(millon((int)(numero%1000000)));             
     	}
     	if (numero < Double.parseDouble("10000000000"))
     		numeroLetras = milmillones(numero);
     	return numeroLetras;
 	}

     
     /**
      * Metodo que retorna un numero cinmilmillones en letra
      * @param numero
      * @return numeroLetras
      */
 	public static String cienmilmillones(double numero)
 	{
 		String numeroLetras="";
 		if (numero >= Double.parseDouble("100000000000") && numero <Double.parseDouble("1000000000000"))
 		{
 			flag=1;
 			numeroLetras = ((centena((int)(numero/1000000000))).concat(" Mil ")).concat(milmillones((int)(numero%1000000000)));            
 		}
 		if(numero < Double.parseDouble("100000000000"))
 			numeroLetras=decmilmillones(numero);
 		return numeroLetras;
 	}
     
 	/**
 	 * Metodo que retorna un numero billones en letra
 	 * @param numero
 	 * @return numeroLetras
 	 */
     public static String billon(double numero)
 	{
     	String numeroLetras="";
     	if (numero >= Double.parseDouble("1000000000000") && numero <Double.parseDouble("2000000000000"))
     	{
     		flag=1;
     		numeroLetras = ("Un Billon ").concat(cienmilmillones(numero%Double.parseDouble("1000000000000")));
     	}
     	if (numero >= Double.parseDouble("2000000000000") && numero <Double.parseDouble("10000000000000"))
     	{
     		flag=1;
     		numeroLetras = unidad((int)(numero/Double.parseDouble("1000000000000"))).concat(" billones ").concat(cienmilmillones(numero%Double.parseDouble("1000000000000")));
     	}
     	if (numero < Double.parseDouble("1000000000000"))
     		numeroLetras = cienmilmillones(numero);
     	return numeroLetras;
 	}
     
     
     /**
      * Metodo que retorna un numero decbillones en letra
      * @param numero
      * @return numeroLetras
      */
     public static  String decbillon(double numero)
 	{
     	String numeroLetras="";
     	if (numero == Double.parseDouble("10000000000000"))
     		numeroLetras = "Diez Billones";
     	if (numero > Double.parseDouble("10000000000000") && numero <Double.parseDouble("20000000000000"))
     	{
     		flag=1;
     		numeroLetras = decena((int)(numero/Double.parseDouble("1000000000000"))).concat(" billones ").concat(billon(numero%Double.parseDouble("1000000000000")));            
     	}
     	if (numero >= Double.parseDouble("20000000000000") && numero <Double.parseDouble("100000000000000"))
     	{
     		flag=1;
     		numeroLetras = decena((int)(numero/Double.parseDouble("1000000000000"))).concat(" billones ").concat(billon(numero%Double.parseDouble("1000000000000")));             
     	}
     	if (numero < Double.parseDouble("10000000000000"))
     		numeroLetras = billon(numero);
     	return numeroLetras;
 	}
     
     

     /*
      * Metodo que retorna un numero cin billones en letra
      * @param numero
      * @return numeroLetras
      */
 /*	public String cienbillones(double numero)
 	{
 		String numeroLetras="";
 		if (numero >= Double.parseDouble("100000000000000") && numero <Double.parseDouble("1000000000000000"))
 		{
 			flag=1;
 			numeroLetras = ((centena((int)(numero/Double.parseDouble("1000000000000"))).concat(" billones ")).concat(decbillon((int)(numero%Double.parseDouble("1000000000000")))));            
 		}
 		if(numero < Double.parseDouble("10000000000000"))
 			numeroLetras=decmillon((int)numero);
 		return numeroLetras;
 	}*/
 	
 	/**
 	 * Metodo que retorna el valor de un numero en letras y recibe las unidades
      * @param numero
      * @param unidades
      * @param unidadesDecimales
      * @return numeroLetras
      * 
 	 */
 	public static String convertirLetras(double numero, String unidad,String unidadDecimales)
 	{
 		//para numeros mas grandes a decbillones, el sistema lo aproxima y se pierde el numero real, tocaria hacer manejos de cadenas de texto
 		//debido a que no es necesario por ahora queda pendiente de implementar.
 		
 		//FIXME esto se debe arreglar cuando se solucione la parametrización de la moneda en el sistema
 		String numeroString=UtilidadTexto.formatearExponenciales(numero);
 		String resultado="";
 		if(numeroString.contains("."))
 		{
 			String unidades="",decimales="";
 			unidades=numeroString.substring(0,numeroString.indexOf("."));
 			decimales=numeroString.substring(numeroString.indexOf(".")+1);
 			if(Utilidades.convertirAEntero(decimales)>0)
 			{
 				//Solo se toman 2 decimales
 				if(decimales.length()>2)
 					decimales = decimales.substring(0, 2);
 				
 				resultado = decbillon(Double.parseDouble(unidades)).concat(" "+unidad)+"s con "+decbillon(Double.parseDouble(decimales)).concat(" "+unidadDecimales);
 			}
 			else if(Integer.parseInt(decimales)==0)
 			{
 				resultado = decbillon(Double.parseDouble(unidades)).concat(" "+unidad)+"s con cero "+unidadDecimales;
 			}
 			else
 			{
 				resultado = decbillon(Double.parseDouble(unidades)).concat(" "+unidad+"s");
 			}
 		}
 		else
 		{
 			resultado = decbillon(numero).concat(" "+unidad);
 		}
 		return resultado;
 	}
 	
 	/**
 	 * Metodo que retorna el valor de un numero en letras
      * @param numero
      * @return numeroLetras
 	 */
 	public static String convertirLetras(double numero)
 	{
// 		para numeros mas grandes a decbillones, el sistema lo aproxima y se pierde el numero real, tocaria hacer manejos de cadenas de texto
 		//debido a que no es necesario por ahora queda pendiente de implementar.
 		String numeroString=numero+"";
 		String resultado="";
 		if(numeroString.contains("."))
 		{
 			String unidades="",decimales="";
 			unidades=numeroString.substring(0,numeroString.indexOf("."));
 			decimales=numeroString.substring(numeroString.indexOf(".")+1);
 			if(Integer.parseInt(decimales)>0)
 			{
 				resultado = decbillon(Double.parseDouble(unidades))+" con "+decbillon(Double.parseDouble(decimales));
 			}
 			else
 			{
 				resultado = decbillon(Double.parseDouble(unidades));
 			}
 		}
 		else
 		{
 			resultado = decbillon(numero);
 		}
 		return resultado;
 	}
 	
 	/**
 	 * Metodo que retorna el valor de un numero en letras y recibe las unidades
      * @param numero
      * @param unidades
      * @param unidadesDecimales
      * @return numeroLetras
      * 
 	 */
 	public static String convertirLetras(String numero, String unidad,String unidadDecimales)
 	{
 		//para numeros mas grandes a decbillones, el sistema lo aproxima y se pierde el numero real, tocaria hacer manejos de cadenas de texto
 		//debido a que no es necesario por ahora queda pendiente de implementar.
 		
 		//FIXME esto se debe arreglar cuando se solucione la parametrización de la moneda en el sistema
 		String numeroString=numero;
 		String resultado="";
 		if(numeroString.contains("."))
 		{
 			String unidades="",decimales="";
 			unidades=numeroString.substring(0,numeroString.indexOf("."));
 			decimales=numeroString.substring(numeroString.indexOf(".")+1);
 			
 			if(Double.parseDouble(decimales)>0)
 			{
 				resultado = decbillon(Double.parseDouble(unidades)).concat(unidad)+"s con "+decbillon(Double.parseDouble(decimales)).concat(" "+unidadDecimales);
 			}
 			else if(Double.parseDouble(decimales)==0)
 			{
 				resultado = decbillon(Double.parseDouble(unidades)).concat(unidad)+"s con cero "+unidadDecimales;
 			}
 			else
 			{
 				resultado = decbillon(Double.parseDouble(unidades)).concat(unidad);
 			}
 		}
 		else
 		{
 			resultado = decbillon(Double.parseDouble(numero)).concat(" "+unidad);
 		}
 		return resultado;
 	}
 }
