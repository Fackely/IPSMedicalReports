package util;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author l-caball
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

/**
 * Método que verifica si una cadena está vacía
 * @param valor-> String cadena a verificar
 * @return true-> si no está vacía y false-> si está vacía
 */
public class UtilidadCadena
{
	
	/**
	 * Para hacer los logs de la aplicación
	 */
	private static Logger logger = Logger.getLogger(UtilidadCadena.class);
	
	public static boolean noEsVacio(String valor)
	{		
		if( valor != null && !valor.trim().equals("") && !valor.equals("null"))
			return true;
		else
			return false; 
	}

	/**
	 * Metodo que valida que la cadena no sea nula y que sea igual a la cadena comparacion.
	 * @param valor
	 * @param comparacion
	 * @return
	 */
	public static boolean vNull(String valor, String comparacion)
	{		
		if( valor != null && !valor.trim().equals("") && !valor.equals("null") && valor.equals(comparacion) )
			return true;
		else
			return false; 
	}

	/**
	 * Metodo que retorna un entero - si la cadena no es valida retorna un CERO.
	 * @param valor
	 * @return
	 */
	public static int vInt(String valor)
	{		
		if( valor != null && !valor.trim().equals("") && !valor.equals("null"))
		{
			try {
				return Integer.parseInt(valor);				
			} catch (Exception e) {
				return 0;
			}
		}	
		else
		{	
			return 0;
		}	
	}
	
	/**
	 * Metodo que retorna Verdadero si la cadena es mayor a un valor especificado.
	 * @param valor
	 * @return
	 */
	public static boolean vIntMayor(String valor, int valorComparar)
	{		
		if( valor != null && !valor.trim().equals("") && !valor.equals("null"))
		{
			try {
				 if (Integer.parseInt(valor) > valorComparar)
				     return true;
				 else 
					 return false;				
			} catch (Exception e) {
				return false;
			}
		}	
		else
		{	
			return false;
		}	
	}	

	/**
	 * Metodo que retorna Verdadero si la cadena es mayor a cero.
	 * @param valor
	 * @return
	 */
	public static boolean vIntMayor(String valor)
	{		
		if( valor != null && !valor.trim().equals("") && !valor.equals("null"))
		{
			try {
				 if (Integer.parseInt(valor) > 0)
				     return true;
				 else 
					 return false;				
			} catch (Exception e) {
				return false;
			}
		}	
		else
		{	
			return false;
		}	
	}	

	/**
	 * Metodo que retorna Verdadero si la cadena es igual a cero.
	 * @param valor
	 * @return
	 */
	public static boolean vIntIgual(String valor)
	{		
		if( valor != null && !valor.trim().equals("") && !valor.equals("null"))
		{
			try {
				 if (Integer.parseInt(valor) == 0)
				     return true;
				 else 
					 return false;				
			} catch (Exception e) {
				return false;
			}
		}	
		else
		{	
			return false;
		}	
	}	
	/**
	 * Metodo que retorna Verdadero si la cadena es igual a cero.
	 * @param valor
	 * @return
	 */
	public static boolean vIntIgual(String valor, int valorComparar)
	{		
		if( valor != null && !valor.trim().equals("") && !valor.equals("null"))
		{
			try {
				 if (Integer.parseInt(valor) == valorComparar)
				     return true;
				 else 
					 return false;				
			} catch (Exception e) {
				return false;
			}
		}	
		else
		{	
			return false;
		}	
	}	
	
	/**
	 * Metodo que retorna Verdadero si la cadena es Menor a cero.
	 * @param valor
	 * @return
	 */
	public static boolean vIntMenor(String valor, int valorComparar)
	{		
		if( valor != null && !valor.trim().equals("") && !valor.equals("null"))
		{
			try {
				 if (Integer.parseInt(valor) < valorComparar)
				     return true;
				 else 
					 return false;				
			} catch (Exception e) {
				return false;
			}
		}	
		else
		{	
			return false;
		}	
	}		

	/**
	 * Metodo que retorna Verdadero si la cadena es Menor a cero.
	 * @param valor
	 * @return
	 */
	public static boolean vIntMenor(String valor)
	{		
		if( valor != null && !valor.trim().equals("") && !valor.equals("null"))
		{
			try {
				 if (Integer.parseInt(valor) < 0)
				     return true;
				 else 
					 return false;				
			} catch (Exception e) {
				return false;
			}
		}	
		else
		{	
			return false;
		}	
	}		

	/**
	 * Metodo que retorna un String con ocurrencias separadas por comas 
	 * @param valor
	 * @return
	 */
	public static String listado(String columna, HashMap mapa, String codigoConcurrente, String NombreCodigo)
	{
		String resultado = "", aux= "";
		int nroRow = vInt(mapa.get("numRegistros")+"");
		boolean entro = false;
		
		for (int i=0; i<nroRow; i++)
		{
			aux = mapa.get(codigoConcurrente+"_"+i)+"";

			
			if ( aux.equals(NombreCodigo) )
			{
				entro = true;
				if ( resultado.equals("") )
					resultado = mapa.get(columna+"_"+i)+"";
				else
					resultado += ", "  + mapa.get(columna+"_"+i);
			}
			else
			{
				if (entro) { return resultado; }
			}
		}
		
		return resultado; 
	}
	/**
	 * Metodo que retorna un String con ocurrencias separadas por comas 
	 * @param valor
	 * @return
	 */
	public static String listado(String columna, HashMap mapa, String codigoConcurrente, String NombreCodigo, String codNumReg)
	{
		String resultado = "", aux= "";
		int nroRow = vInt(mapa.get(codNumReg)+"");
		boolean entro = false;
		
		for (int i=0; i<nroRow; i++)
		{
			aux = mapa.get(codigoConcurrente+"_"+i)+"";

			
			if ( aux.equals(NombreCodigo) )
			{
				entro = true;
				if ( resultado.equals("") )
					resultado = mapa.get(columna+"_"+i)+"";
				else
					resultado += ", "  + mapa.get(columna+"_"+i);
			}
			else
			{
				if (entro) { return resultado; }
			}
		}
		
		return resultado; 
	}
	
	
	/**
	 * Si la cadena es NULLA retorna ""
	 * @param valor
	 * @return
	 */
	public static String vString(String valor)
	{		
		if( valor == null || valor.equals("null"))
			return "";
		else
			return valor; 
	}
	
	
	
	/**
	 * Se le da formato a las observaciones generales. Es decir, se pegan a las
	 * anteriores, en caso que existan, se adiciona la fecha y hora de
	 * grabación, la cadena con las observaciones escritas y el medico que las
	 * ingresa con toda su información.
	 * @param 	String, nuevas. Nuevas observaciones.
	 * @param 	String, anteriores. Observaciones previamente ingresadas.
	 * @return 		String, cadena con las observaciones a guardar.
	 */
	public  static String cargarObservaciones(String nuevas, String anteriores, UsuarioBasico medico)
	{
		String observaciones = new String();
		
		if( !UtilidadCadena.noEsVacio(nuevas) )
		{
			observaciones = anteriores;	
		}
		else
		{
			String identificacionUsuario = medico.getNombreUsuario() +"  "+medico.getNumeroRegistroMedico() + ".   ";
			identificacionUsuario += medico.getEspecialidadesMedico();

			if( UtilidadCadena.noEsVacio(anteriores) )
			{
				anteriores = anteriores.replaceAll("<br>", "\n");		
				observaciones = anteriores;
			}

			observaciones += "\n"+UtilidadFecha.getFechaActual()+"  "+UtilidadFecha.getHoraActual()+"\n";
			nuevas = nuevas.replaceAll("<br>", "\n")+"\n";
			observaciones += nuevas;
			observaciones += identificacionUsuario+"\n\n";
		}
		
		return observaciones;		
	}
	
	/**
	 * Método que devuelve la posición de la primera ocurrencia de una cadena dada como argumento, el cuál busca en 
	 * un vector de strings[]
	 *@param vectorStrings
	 *@param cadena
	 *@return posicion o -1 si no encuentra la cadena
	 */
	public static int indexOf (String[] vectorStrings, String cadena)
	{
		for(int pos=0; pos<vectorStrings.length; pos++)
		{
			if (vectorStrings[pos].equals(cadena))
				return pos;
		}
		return -1;
	}
	
	/**
	 * Método que reemplaza en un String las ocurrencias de un caracter por un String dado
	 * se utiliza mucho para reemplazar \n por <br>  
	 * @param cadena
	 * @param caracterBuscado
	 * @param caracterReemplazo
	 * @return cadena con los reemplazos realizados
	 */
	public static String replaceToken (String cadena, char caracterBuscado, String caracterReemplazo)
	{
		for (int i=0; i<cadena.length(); i++)
			{
				if (cadena.charAt(i)==caracterBuscado)
				{
					if (i==0)
						cadena=caracterReemplazo+cadena.substring(i+1,cadena.length());
					else
						cadena=cadena.substring(0, i-1)+caracterReemplazo+cadena.substring(i+1,cadena.length());
				}
			}
		
		return cadena;
	}
	
	/**
	 * Método que valida un String que es un porcentaje, verificando que sea mayor o igual a cero,
	 * menor o igual a 100 y que tenga el número de decimales que se envía por parámetro, el formato del porcentaje
	 * debe estar validado
	 * @author amruiz
	 * @param porcentaje --> Porcentaje a validar (String)
	 * @param numDecimelas --> Número de decimales permitidos en el porcentaje
	 * @param separador --> Separador utilizado para los decimales
	 * @return 0 -> Si no hay errores 
	 * 					 1 -> Si no cumplió con la validación que sea mayor o igual a 0
	 * 					 2 -> Si no cumplió con la validación que sea menor o igual a 100
	 * 					 3 -> Si no cumplió con la validación que tenga el número de decimales especificados
	 * 					 4 -> Si no cumplió con la validación 1 y 3
	 * 				  	 5 -> Si no cumplió con la validación 2 y 3
	 * 					-1 -> Si el String porcentaje es vacío o null 
	 */
	public static int validarPorcentajeString (String porcentaje, int numDecimales, String separador)
	{
		int cont=0;
		
		if(noEsVacio(porcentaje))
		{
			try
			{
				float numPorcentaje=Float.parseFloat(porcentaje);
				
				//---Se verifica que sea mayor o igual a 0
				if(numPorcentaje<0)
					{
						cont++;
					}
			
				//--Se verifica que sea menor o igual a 100
				if(numPorcentaje>100)
					{
					cont+=2;	
					}
				
				//-----Se verifica el número de decimales que tiene
				if(porcentaje.indexOf(separador)!=-1)
				{
					int pos=porcentaje.indexOf(separador);
					
					if(porcentaje.length()-(pos+1)>numDecimales)
							cont+=3;
					
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			return cont;
		}
		else
			return -1;
		
	}
	
	/**
	 * Método que redondea un valor entero a la centena
	 * @param valor
	 * @return
	 */
	public static int redondearALaCentena(int valor)
	{
		//Se efectua el redonde a la centena
		String temp = valor + "";
		String decenas = "";
		if(temp.length()>=2)
		{
			decenas = temp.substring(temp.length()-2,temp.length());
			int dec = Integer.parseInt(decenas);
			if(dec>=50)
				valor = valor - dec + 100;
			else
				valor = valor - dec;
		}
		
		return valor;
	}
	
	/**
	 * Método que redondea un valor entero a la centena
	 * @param valor
	 * @return
	 */
	public static long redondearALaCentenaLong(long valor)
	{
		//Se efectua el redonde a la centena
		String temp = valor + "";
		String decenas = "";
		if(temp.length()>=2)
		{
			decenas = temp.substring(temp.length()-2,temp.length());
			long dec = Long.parseLong(decenas);
			if(dec>=50)
				valor = valor - dec + 100;
			else
				valor = valor - dec;
		}
		else
		{
			valor=0;
		}
		return valor;
	}
	
	/**
	 * Método que redondea un valor entero a la centena
	 * @param valor
	 * @return
	 */
	public static int redondearALaDecena(int valor)
	{
		//Se efectua el redonde a la centena
		String temp = valor + "";
		String decenas = "";
		if(temp.length()>=1)
		{
			decenas = temp.substring(temp.length()-1,temp.length());
			int dec = Integer.parseInt(decenas);
			if(dec>=5)
				valor = valor - dec + 10;
			else
				valor = valor - dec;
		}
		
		return valor;
	}
	
	/**
	 * Método que redondea un valor entero a la centena
	 * @param valor
	 * @return
	 */
	public static long redondearALaDecenaLong(long valor)
	{
		//Se efectua el redonde a la centena
		String temp = valor + "";
		String decenas = "";
		if(temp.length()>=1)
		{
			decenas = temp.substring(temp.length()-1,temp.length());
			long dec = Long.parseLong(decenas);
			if(dec>=5)
				valor = valor - dec + 10;
			else
				valor = valor - dec;
		}
		
		return valor;
	}
	
	/**
	 * Funcion que recibe un mapa y devuelve en una cadena separada por el parametro separador
	 * los valores que hayan del key respectivo (columna)
	 * @param mapa -> Mapa 
	 * @param keyColumna -> Nombre del key que se desea obtener en el string
	 * @param separador -> Separador de cada uno de los valores en el string
	 * @return
	 */
	public static String obtenerCadenaKey(HashMap mapa, String keyColumna, String separador) 
	{
		HashMap mp = new HashMap();
		StringBuffer cadena=new StringBuffer();
        int nroReg = UtilidadCadena.vInt(mapa.get("numRegistros")+"");
        
        for(int i=0; i<nroReg; i++)
        {
        	String valor=vString(mapa.get(keyColumna+"_"+i)+"");
        	
        	if(i!=0)
        		cadena.append(separador+valor);
        	else
        		cadena.append(valor);
        }
		
        return cadena.toString();
	}
	
	/**
	 * Método usado para quitar los caracteres especiales 
	 * en los campos de texto
	 * @param cadena
	 * @return
	 */
	public static boolean tieneCaracteresEspecialesNumeroId(String cadena)
	{
		boolean tieneCaracteresEspeciales = false; 
		for(int i=0;i<cadena.length();i++)
		{
			if((cadena.charAt(i)<48||cadena.charAt(i)>57)&& //números
				(cadena.charAt(i)<97||cadena.charAt(i)>122)&& //letras minusculas
				(cadena.charAt(i)<65||cadena.charAt(i)>90)&& //letras mayusculas
				cadena.charAt(i)!=45) //guión
			{
				tieneCaracteresEspeciales = true;
			}
		}
		
		return tieneCaracteresEspeciales;
	}
	
	/**
	 * Método usado para quitar los caracteres especiales 
	 * en los campos de texto
	 * @param cadena
	 * @return
	 */
	public static boolean tieneCaracteresEspecialesGeneral(String cadena)
	{
		boolean tieneCaracteresEspeciales = false; 
		for(int i=0;i<cadena.length();i++)
		{
			
			if((cadena.charAt(i)<48||cadena.charAt(i)>57)&& // numeros
				(cadena.charAt(i)<97||cadena.charAt(i)>122)&& //letras minusculas
				(cadena.charAt(i)<65||cadena.charAt(i)>90)&& //letras mayusculas
				(cadena.charAt(i)<192||cadena.charAt(i)>255)&& //letras raras y tildes
				cadena.charAt(i)!=45&& //guión 
				cadena.charAt(i)!=46&& //punto
				cadena.charAt(i)!=58&& //DOS PUNTOS
				cadena.charAt(i)!=32&& // espacio
				cadena.charAt(i)!='\n'&& // enter
				cadena.charAt(i)!='\r' // retorno de carro
				)
			{
				tieneCaracteresEspeciales = true;
			}
		}
		
		return tieneCaracteresEspeciales;
	}
	
	
	/**
	 * Método usado para verificar si un campo de texto de rips tiene caracteres especiales
	 * en los campos de texto
	 * @param cadena
	 * @return
	 */
	public static boolean tieneCaracteresEspecialesTextoRips(String cadena)
	{
		boolean tieneCaracteresEspeciales = false; 
		for(int i=0;i<cadena.length();i++)
		{
			if((cadena.charAt(i)<48||cadena.charAt(i)>57)&& // numeros
				(cadena.charAt(i)<97||cadena.charAt(i)>122)&& //letras minusculas
				(cadena.charAt(i)<65||cadena.charAt(i)>90)&& //letras mayusculas
				cadena.charAt(i)!=45&& //guión
				cadena.charAt(i)!=32&& // espacio
				cadena.charAt(i)!='á'&&
				cadena.charAt(i)!='é'&&
				cadena.charAt(i)!='í'&&
				cadena.charAt(i)!='ó'&&
				cadena.charAt(i)!='ú'&&
				cadena.charAt(i)!='ñ'&&
				cadena.charAt(i)!='Ñ'
				)
			{
				tieneCaracteresEspeciales = true;
			}
		}
		
		return tieneCaracteresEspeciales;
	}
	
	/**
	 * Método usado para verificar si un campo numerico de rips tiene caracteres especiales 
	 * en los campos de texto
	 * @param cadena
	 * @return
	 */
	public static boolean tieneCaracteresEspecialesNumericoRips(String cadena)
	{
		boolean tieneCaracteresEspeciales = false; 
		for(int i=0;i<cadena.length();i++)
		{
			if((cadena.charAt(i)<48||cadena.charAt(i)>57)&& // numeros
				cadena.charAt(i)!=46&& //punto
				cadena.charAt(i)!=32 // espacio
				)
			{
				tieneCaracteresEspeciales = true;
			}
		}
		
		return tieneCaracteresEspeciales;
	}
	
	/**
	 * Método que realiza la validacion de la alineacion de espacios en los campos rips
	 * dependiendo si el campo es numéroco de texto
	 * @param cadena
	 * @param esNumerico
	 * @return
	 */
	public static boolean alineacionEspaciosValidaCampoRips(String cadena,boolean esNumerico)
	{
		boolean valida = true;
		
		//Si la cadena tiene espacios sobrantes se realiza la validacion
		if(cadena.length()!=cadena.trim().length()&&cadena.trim().length()>0)
		{
			//Si el campo es numérico y termina en espacio quiere decir que no hay alineacion
			//a la derecha por tanto es errónea
			if(cadena.endsWith(" ")&&esNumerico)
				valida = false;
			
			//Si el campo es de texto y empieza por espacio quiere decir que no hay alineación
			//a la izquierda por tanto es erróneo
			if(cadena.startsWith(" ")&&!esNumerico)
				valida = false;
		}
		
		return valida;
	}
	
	/**
	 * Método para editar los espacios del campo en el archivo RIPS
	 * @param campo
	 * @param tamano
	 * @param limite
	 * @param esNumerico: si es numérico los espacios se añaden antes, si es cadena los espacios se añaden después
	 * @return
	 */
	public static String editarEspacios(String campo, int tamano, int limite,boolean esNumerico)
	{
		
		String aux;
		String espacios="";
		
		for(int i=tamano+1;i<=limite;i++)
			espacios+=" ";
		
		if(esNumerico)
			aux=espacios+campo;
		else
			aux=campo+espacios;
		
		return aux;
	}
	
	/**
	 * Método para rellenar una cadena con un caracter especial hasta un límite
	 * @param campo
	 * @param limite
	 * @param izquierda
	 * @param caracter
	 * @return
	 */
	public static String rellenarCaracter(String campo, int limite,boolean izquierda,String caracter)
	{
		
		for(int i=campo.length();i<limite;i++)
		{
			if(izquierda)
				campo = caracter + campo;
			else
				campo+=caracter;
		}
		
		
		return campo;
	}
	
	/**
	 * Método que cuenta el número de ocurrencias de un caracter en una cadena
	 * @param campo
	 * @param caracter
	 * @return
	 */
	public static int numeroOcurrenciasCaracter(String campo,char caracter)
	{
		int contador = 0;
		
		for(int i=0;i<campo.length();i++)
		{
			if(campo.charAt(i)==caracter)
				contador++;
		}
		return contador;
	}
	
	
	/**
	 * Método para transformar una formula a modo javascript
	 * especialmente cuando se menjan exponenciales
	 * @param cadena
	 * @return
	 */
	public static String transformacionFormulaJavascript(String cadena)
	{
		int posicion = cadena.lastIndexOf("^");

		while(posicion!=-1)
		{
			//System.out.println("la posicion: "+posicion);
			String base = cadena.substring(0,posicion);
			String exponencial = cadena.substring(posicion+1,cadena.length());
			logger.info("BASE INICIAL: "+base);
			logger.info("EXPONENCIAL INICIAL: "+exponencial);
			
	
			String parteExponencial = "";
			String sobraExponencial = "";
			boolean terminar = false;
			boolean parentesis = false;
			int contadorParentesis = 0;
			for(int i=0;i<exponencial.length();i++)
			{
				if(i==0)
				{
					if(exponencial.charAt(i)=='(')
						parentesis = true;
					else
						parentesis = false;
				}
				if(parentesis)
				{
					if(exponencial.charAt(i)=='(')
						contadorParentesis ++;
					if(exponencial.charAt(i)==')')
						contadorParentesis --;
				}
				else
				{
	
					if(exponencial.charAt(i)!='0'&&
						exponencial.charAt(i)!='1'&&
						exponencial.charAt(i)!='2'&&
						exponencial.charAt(i)!='3'&&
						exponencial.charAt(i)!='4'&&
						exponencial.charAt(i)!='5'&&
						exponencial.charAt(i)!='6'&&
						exponencial.charAt(i)!='7'&&
						exponencial.charAt(i)!='8'&&
						exponencial.charAt(i)!='9'&&
						exponencial.charAt(i)!=46&&
						exponencial.charAt(i)!='_')
						terminar = true;	
				}
	
				if(terminar)
					sobraExponencial += exponencial.charAt(i);
				else
					parteExponencial += exponencial.charAt(i);
	
				if(parentesis&&contadorParentesis<=0)
					terminar = true;
			}
	
			String parteBase = "";
			String sobraBase = "";
			terminar = false;
			parentesis = false;
			contadorParentesis = 0;
			for(int i=(base.length()-1);i>=0;i--)
			{
				if(i==(base.length()-1))
				{
					if(base.charAt(i)==')')
						parentesis = true;
					else
						parentesis = false;
				}
				if(parentesis)
				{
					if(base.charAt(i)==')')
                       contadorParentesis ++;
	                if(base.charAt(i)=='(')
        	           contadorParentesis --;

				}
				else
				{
				
	
					if(base.charAt(i)!='0'&&
						base.charAt(i)!='1'&&
						base.charAt(i)!='2'&&
						base.charAt(i)!='3'&&
						base.charAt(i)!='4'&&
						base.charAt(i)!='5'&&
						base.charAt(i)!='6'&&
						base.charAt(i)!='7'&&
						base.charAt(i)!='8'&&
						base.charAt(i)!='9'&&
						base.charAt(i)!=46&&
						base.charAt(i)!='_')
						terminar = true;
				}
				if(terminar)
					sobraBase = base.charAt(i) + sobraBase;
				else
					parteBase = base.charAt(i) + parteBase;

				if(parentesis&&contadorParentesis<=0)
					terminar = true;
			}

			logger.info("parteBase: "+parteBase);
			logger.info("sobraBase: "+sobraBase);
			cadena = sobraBase + "Math.pow("+parteBase+","+parteExponencial+")"+sobraExponencial;
			logger.info("Cadena resultante de la iteracion: "+cadena);

			posicion = cadena.lastIndexOf("^");
		}
		
		return cadena;
	}
	
	/**
	 * Método para convertir caracteres especiales a HTML
	 * @param cadena
	 * @return
	 */
	public static String convertirCaracteresEspecialesAHTML(String cadena)
	{
		String nuevaCadena = "";
		
		
		for(int i=0;i<cadena.length();i++)
		{
			if(cadena.charAt(i)=='á')
				nuevaCadena += "&aacute;";
			else if(cadena.charAt(i)=='é')
				nuevaCadena += "&eacute;";
			else if(cadena.charAt(i)=='í')
				nuevaCadena += "&iacute;";
			else if(cadena.charAt(i)=='ó')
				nuevaCadena += "&oacute;";
			else if(cadena.charAt(i)=='ú')
				nuevaCadena += "&uacute;";
			else if(cadena.charAt(i)=='Á')
				nuevaCadena += "&Aacute;";
			else if(cadena.charAt(i)=='É')
				nuevaCadena += "&Eacute;";
			else if(cadena.charAt(i)=='Í')
				nuevaCadena += "&Iacute;";
			else if(cadena.charAt(i)=='Ó')
				nuevaCadena += "&Oacute;";
			else if(cadena.charAt(i)=='Ú')
				nuevaCadena += "&Uacute;";
			else if(cadena.charAt(i)=='ñ')
				nuevaCadena += "&ntilde;";
			else if(cadena.charAt(i)=='Ñ')
				nuevaCadena += "&Ntilde;";
			else
				nuevaCadena += cadena.charAt(i);
		}
		
		return nuevaCadena;
	}
	

}
