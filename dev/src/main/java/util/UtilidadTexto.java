/*
 * @(#)UtilidadTexto.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase utilitaria, con varios m�todos para manipular cadenas de texto.
 *
 * @version 1.1, Sep 29, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class UtilidadTexto {

	/**
	 * Cadena constante con una expresi�n regular que representa todos los posibles patrones
	 * de texto acentuados para la letra 'A', tanto en may�sculas como en min�sculas.
	 */
	private static final String regexpA = "[a����A����]";

	/**
	 * Cadena constante con una expresi�n regular que representa todos los posibles patrones
	 * de texto acentuados para la letra 'E', tanto en may�sculas como en min�sculas.
	 */
	private static final String regexpE = "[e����E����]";

	/**
	 * Cadena constante con una expresi�n regular que representa todos los posibles patrones
	 * de texto acentuados para la letra 'I', tanto en may�sculas como en min�sculas.
	 */
	private static final String regexpI = "[i����I����]";

	/**
	 * Cadena constante con una expresi�n regular que representa todos los posibles patrones
	 * de texto acentuados para la letra 'O', tanto en may�sculas como en min�sculas.
	 */
	private static final String regexpO = "[o����O����]";

	/**
	 * Cadena constante con una expresi�n regular que representa todos los posibles patrones
	 * de texto acentuados para la letra 'U', tanto en may�sculas como en min�sculas.
	 */
	private static final String regexpU = "[u����U����]";

	/**
	 * Cadena constante con una expresi�n regular que representa todos los posibles patrones
	 * de texto para la letra '�', tanto en may�sculas como en min�sculas.
	 */
	private static final String regexpN = "[��]";

	/**
	 * Este metodo me permite separar los codigos de los textos en los resultados que me envia
	 * la pagina de entrada de datos, o en general, cualquier cadena de la forma "codigo-nombre".
	 * Recibe dos parametros, el resultado como tal y el numero de c�digos que hay
	 * (Ej: "2-3-Ciudad De la luz" tiene dos codigos) y me devuelve los codigos y el texto
	 * separados en un arreglo de Strings.
	 * @param frase la cadena que quiero procesar
	 * @param numCodigos numero de separadores, en este caso, '-' que tiene la cadena
	 * @return un arreglo con los <i>tokens</i> encontrados en la cadena
	 */
	public static String [] separarNombresDeCodigos (String frase, int numCodigos)	{

		if (numCodigos > 0 && frase != null && !frase.trim().equals("")) {

			String [] aRetornar = new String [numCodigos+1];
	
			if (frase.indexOf('-') != -1) {
	
				StringTokenizer tokenizer = new StringTokenizer (frase, "-");
				int i, tamanosAcumulados=0;
	
				//Utilizando el StringTokenizer se sacaran todos los codigos
				for (i=0;i<numCodigos;i++)
				{
					aRetornar[i] = tokenizer.nextToken();
					tamanosAcumulados += aRetornar[i].length();
				}
	
				/* Como el �ltimo texto puede incluir guiones no usamos StringTokenizer,
				   sino sacamos la �ltima parte de la frase, restando la longitud de las
				   palabras sacadas MAS el numero de guiones que hay (es exactamente el n�mero de c�digos) */
	
				aRetornar[i] = frase.substring( tamanosAcumulados+numCodigos, frase.length() );
				return aRetornar;
	
			}
	
			else {
	
				for (int j = 0; j < aRetornar.length; j++) {
					aRetornar[j] = "";
				}
				return aRetornar;
	
			}

		}

		else {
			return null;
		}

	}

	/**
	 * Este m�todo toma una cadena de texto y reemplaza todas las ocurrencias de los
	 * caracteres 'a', 'e', 'i', 'o', 'u', y '�' (bien sea en may�sculas o en min�sculas)
	 * por una expresi�n regular que cubre todos los posibles casos de un caracter
	 * acentuado en espa�ol.
	 * @param str cadena de texto que se desea procesar
	 * @return cadena de texto con sus vocales y �'s reemplazadas por expresiones regulares,
	 * o "str" si "str" es nula o vac�a
	 */
	public static String convertToRegexp (String str) {

		if (str != null && !str.equals("")) {

			str = str.replaceAll(regexpA,regexpA);
			str = str.replaceAll(regexpE,regexpE);
			str = str.replaceAll(regexpI,regexpI);
			str = str.replaceAll(regexpO,regexpO);
			str = str.replaceAll(regexpU,regexpU);
			str = str.replaceAll(regexpN,regexpN);
			str = str.trim();

		}

		return str;

	}

	/**
	 * Reemplaza los acentos y las �'s de una cadena de texto por el caracter no acentuado.
	 * @param la cadena que se desea procesar
	 * @return la cadena de texto sin acentos y sin �'s
	 */
	public static String removeAccents (String str) {

		if (str != null && !str.equals("")) {

			str = str.replaceAll("[����]", "a");
			str = str.replaceAll("[����]", "e");
			str = str.replaceAll("[����]", "i");
			str = str.replaceAll("[����]", "o");
			str = str.replaceAll("[����]", "u");
			str = str.replaceAll("�", "n");
			str = str.replaceAll("[����]", "A");
			str = str.replaceAll("[����]", "E");
			str = str.replaceAll("[����]", "I");
			str = str.replaceAll("[����]", "O");
			str = str.replaceAll("[����]", "U");
			str = str.replaceAll("�", "N");

		}

		return (str != null) ? str : "";

	}

	/**
	 * "Aplana" una cadena de texto, quit�ndole espacios, acentos y �'s, marcando
	 * el inicio de cada palabra posterior a la primera mediante el uso de may�sculas
	 * (como la convenci�n de nombres de variables en Java).
	 * @param str la cadena que se desea aplanar
	 * @return la cadena aplanada, o una cadena vac�a si "str" es nula
	 */
	public static String flattenString (String str) {

		if (str != null && !str.equals("")) {

			str = str.trim();

			if (str.length() > 0) {

				str = removeAccents(str.toLowerCase());

				StringTokenizer strtok = new StringTokenizer (str, " -\f\n\r\t");
				String tmp, resp = strtok.nextToken();
				char [] tmpArray;

				while (strtok.hasMoreTokens()) {

					tmp = strtok.nextToken();
					tmpArray = tmp.toCharArray();
					tmpArray[0] = Character.toUpperCase(tmpArray[0]);
					resp += new String(tmpArray);

				}

				str = resp;

			}

		}

		return (str != null) ? str : "";

	}

	/**
	 * Dada una cadena con el archivo .jsp que proporciona una funcionalidad del
	 * sistema, genera el patr�n de directorios que debe usarse para otorgar
	 * acceso a un rol sobre dicha funcionalidad.
	 * @param str cadena con la URL absoluta del primer archivo .jsp que se usa
	 * para acceder a una funcionalidad dentro del sistema.
	 * @return el patr�n de texto que da acceso a toda la funcionalidad, o una
	 * cadena vac�a si str es nulo.
	 */
	public static String generatePattern (String str) {

		if (str != null && !str.equals("")) {
			int end = str.lastIndexOf('/');
			if (end == -1) {
				return "/" + str;
			}
			else {
				return "/" + str.substring(0, end+1) + "*";
			}
		}

		else {
			return "";
		}

	}
	/**
	 * Este m�todo recibe una frase y la deja en formato listo para
	 * presentaci�n, primera letra de cada palabra en may�scula.
	 * (Muy importante sobre todo mostrando valores de ciudades y
	 * dem�s)
	 * @param frase que se quiere convertir a formato normal
	 * @return String frase con formato estandar (Primera letra palabras en
	 * May�scula)
	 */
	public static String fraseAEstandar (String frase)
	{
		char letra='a', letraAnterior='a';
		String salida="";
		if (frase==null||frase.length()<1)
			return frase;
		int i;
		for (i=0;i<frase.length();i++)
		{
			letra=frase.charAt(i);
			if (i==0)
			{
				letra=Character.toUpperCase(letra);
			}
			else if (letraAnterior==' '||letraAnterior=='.'||letraAnterior=='('||letraAnterior==')') 
			{
				letra=Character.toUpperCase(letra);
			}
			else
			{
				letra=Character.toLowerCase(letra);
			}
			letraAnterior=letra;
			salida=salida + letra;
		}
		return salida;
	}
	
	/**
	 * Utilidad que le pone a los valores un formato estandar,
	 * separa con puntos de miles y pone ceros(0) a los decimales
	 * en caso de que el valor no tenga parte decimal, aproximando
	 * en caso tal que lo necesite.
	 * @param valor Cadena con el valor que se desea formatear
	 * @param numeroDecimales N�mero de decimales que desea que tenga el texto formateado
	 * @param ponerPuntos booleano que indica si debe poner puntos de separaci�n de miles
	 * @param manejarComaSeparacionDecimales Booleano para definir si la separaci�n de decimales se hace con comas o con puntos
	 * @return cadena formateada de acuerdo a los par�metros pasados
	 */

	public static String formatearValores(String valor, int numeroDecimales, boolean ponerPuntos, boolean manejarComaSeparacionDecimales)
		{
			String formateado="";
			
			try
			{
				double valorDouble=Double.parseDouble(valor);
				
				//int cifras=(int) Math.pow(10,numeroDecimales);
			    //valorDouble=Math.rint(valorDouble*cifras)/cifras;
			    
			    BigDecimal big = new BigDecimal(valorDouble);
			    big = big.setScale(numeroDecimales, RoundingMode.HALF_UP);
				
	    		valorDouble=big.doubleValue();
	    
			    formateado=valorDouble+"";
			    String[] partes=formateado.split("\\.");
			    if(partes.length>1){
			    	if(partes[1].length()<numeroDecimales){
			    		for(int i=partes[1].length();i<numeroDecimales;i++){
			    			formateado+="0";
			    		}
			    	}
			    }else{
			    	if(numeroDecimales>0){
				    	formateado+=".";
				    	for(int i=0;i<numeroDecimales;i++){
			    			formateado+="0";
			    		}
			    	}
			    }
			    
			    if(ponerPuntos&&valorDouble>99){
				    DecimalFormat format=new DecimalFormat(ConstantesBD.formatoMonetarioBeanWrite);
					formateado=format.format(Double.parseDouble(formateado));
					
					if(valorDouble==0.0){
						formateado=formateado.substring(2,formateado.length());
					}
			    }
			    if(manejarComaSeparacionDecimales){
					formateado=formateado.replace(",", ";");
					formateado=formateado.replace(".", ",");
					formateado=formateado.replace(";", ".");
				}
			}
			catch(NumberFormatException e)
			{
				formateado="";
			}
			
			return formateado;
		}

	/**
	 * Como muchas veces se tiene un double con valores de moneda no es
	 * necesario (y en ocasiones es molesto) mostrar todas las cifras, sino
	 * basta con dar el n�mero y sus dos primeras cifras... De todas maneras,
	 * despu�s se puede sobrecargar el m�todo, para que soporte precisi�n.
	 * @param numero el n�mero que se quiere procesar
	 * @return el n�mero, formateado.
	 */
	public static String darFormatoADouble (double numero)
	{
		String arregloTemp[], temp;
		temp=numero+"";
		arregloTemp=temp.split("[.,]");
		if (arregloTemp.length==2)
		{
			if (arregloTemp[0]!=null&&arregloTemp[1]!=null)
			{
				if (arregloTemp[1].length()>2)
				{
					return arregloTemp[0] + "." + arregloTemp[1].substring(0,2);
				}
				else
				{
					return arregloTemp[0] + "." + arregloTemp[1];
				}
			}
		}
		return "El n�mero no estaba en el formato correcto" ;
	}
	
	/**
	 * M�todo que dice si dos boolean son iguales, usando un
	 * rango definido en ConstantesBD
	 * 
	 * @param numero1 Primer n�mero de la comparaci�n
	 * @param numero2 Segundo n�mero de la comparaci�n
	 * @return
	 */
	public static boolean sonIguales (double numero1, double numero2)
	{
		if (numero1 + ConstantesBD.margenErrorDouble >= numero2 && numero2 + ConstantesBD.margenErrorDouble >= numero1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * M�todo que convierte una observaci�n con \n a formato
	 * html (<br>)
	 * @param observacionOriginal Observaci�n a la que se desea
	 * convertir a html
	 * @return
	 */
	public static String observacionAHTML (String observacionOriginal)
	{
		if (observacionOriginal==null)
		{
			return "";
		}
		else
		{
			String observaciones=observacionOriginal;
			while(observaciones.indexOf('\r')>=0)
			{
				if(observaciones.indexOf('\r')==0)
				{
					observaciones=observaciones.substring(observaciones.indexOf('\r')+1, observaciones.length());
				}
				else
				{
					observaciones=observaciones.substring(0, observaciones.indexOf('\r'))+observaciones.substring(observaciones.indexOf('\r')+1, observaciones.length());
				}
			}
			while(observaciones.indexOf('\n')>=0)
			{
				if(observaciones.indexOf('\n')==0)
				{
					observaciones=observaciones.substring(observaciones.indexOf('\n')+1, observaciones.length());
				}
				else
				{
					observaciones=observaciones.substring(0, observaciones.indexOf('\n'))+"<br>"+observaciones.substring(observaciones.indexOf('\n')+1, observaciones.length());
				}
			}
			String observacionesTempo=observaciones;
			observaciones="";
			for(int i=0; i<observacionesTempo.length(); i++)
			{
				char caracter=observacionesTempo.charAt(i);
				if(caracter=='\'')
				{
					observaciones+='\\';
					observaciones+='\\';
					observaciones+='\\';
					observaciones+='\'';
				}
				else
				{
					observaciones+=caracter;
				}
			}
			

			return observaciones;
		}
	}
	
	/**
	 * M�todo que convierte una observaci�n con \n a formato plano
	 * @param observacionOriginal Observaci�n a la que se desea
	 * convertir a html
	 * @return
	 */
	public static String observacionATextoPlano (String observacionOriginal)
	{
		if (observacionOriginal==null)
		{
			return "";
		}
		else
		{
			String observaciones=observacionOriginal;
			while(observaciones.indexOf('\r')>=0)
			{
				observaciones = observaciones.replaceAll("\r"," ");
			}
			while(observaciones.indexOf('\n')>=0)
			{
				observaciones = observaciones.replaceAll("\n"," ");
			}

			return observaciones;
		}
	}
	
	/**
	 * M�todo que cambia los <BR> por  saltos de linea ASCCI
	 * @param observacionOriginal
	 * @return
	 */
	public static String deshacerCodificacionHTML(String observacionOriginal)
	{
		if (observacionOriginal==null)
		{
			return "";
		}
		else
		{
			String observaciones=observacionOriginal;
			while(observaciones.indexOf("<br>")>=0)
			{
				if(observaciones.indexOf("<br>")==0)
				{
					observaciones=observaciones.substring(observaciones.indexOf("<br>")+4, observaciones.length());
				}
				else
				{
					observaciones=observaciones.substring(0, observaciones.indexOf("<br>"))+'\n'+observaciones.substring(observaciones.indexOf("<br>")+4, observaciones.length());
				}
			}
			while(observaciones.indexOf("<BR>")>=0)
			{
				if(observaciones.indexOf("<BR>")==0)
				{
					observaciones=observaciones.substring(observaciones.indexOf("<BR>")+4, observaciones.length());
				}
				else
				{
					observaciones=observaciones.substring(0, observaciones.indexOf("<BR>"))+'\n'+observaciones.substring(observaciones.indexOf("<BR>")+4, observaciones.length());
				}
			}
			return observaciones;
		}
	}

	/**
	 * M�todo que permite, en los campos de observaciones (que deben
	 * llevar el nombre del m�dico, sus especialidades y dem�s en caso 
	 * de m�dico y el nombre y login en caso de usuario) agregar un dato
	 * extra 
	 * 
	 * @param observacionOriginal String con la observaci�n original
	 * @param nuevaObservacion String con la observaci�n a agregar
	 * @param usuario usuario responsable de esta observaci�n a agregar 
	 * @param validarNull Par�metro que indica si se deben validar las observaciones en null
	 * @return cadena con los datos extra del usuario � del m�dico
	 */
	public static String agregarTextoAObservacion (String observacionOriginal, String nuevaObservacion, UsuarioBasico usuario, boolean validarNull)
	{
		String observaciones = new String();
		
		if (usuario!=null)
		{
			if(!validarNull && observacionOriginal==null)
			{
				observacionOriginal="";
			}
			if(!validarNull && nuevaObservacion==null)
			{
				nuevaObservacion="";
			}
			if((nuevaObservacion==null || nuevaObservacion.equals("")) && validarNull)
			{
				observaciones="";
			}
			else
			{	
				if( observacionOriginal != null )
				{
					observaciones = observacionOriginal.replaceAll("<br>", "\n");
					observaciones += "\n\n"+UtilidadFecha.getFechaActual()+"  "+UtilidadFecha.getHoraActual();
				}
				else
					observaciones = UtilidadFecha.getFechaActual()+"  "+UtilidadFecha.getHoraActual();
				
				if(!nuevaObservacion.equals(""))
				{
					observaciones += "\n"+nuevaObservacion;
				}
				
				//if (UtilidadValidacion.esMedico(usuario))
				if(UtilidadValidacion.esProfesionalSalud(usuario))
				{
					observaciones += "\n"+usuario.getInformacionGeneralPersonalSalud();
				}
				else
				{
					observaciones += "\n "+usuario.getNombreUsuario() + " (" +usuario.getLoginUsuario() + ")";
				}
			}	
		}
		
		return observaciones;
	}

	
	
	/**
	 * @param observacionOriginal
	 * @param nuevaObservacion
	 * @param usuario
	 * @param validarNull
	 * @return observacion con datos de usuario con separador de split = @@@@@
	 */
	public static String agregarTextoAObservacionConSeparadorSplit (String observacionOriginal, String nuevaObservacion, UsuarioBasico usuario, boolean validarNull)
	{
		String observaciones = new String();
		
		if (usuario!=null)
		{
			if(!validarNull && observacionOriginal==null)
			{
				observacionOriginal="";
			}
			if(!validarNull && nuevaObservacion==null)
			{
				nuevaObservacion="";
			}
			if((nuevaObservacion==null || nuevaObservacion.equals("")) && validarNull)
			{
				observaciones="";
			}
			else
			{	
				if( observacionOriginal != null )
				{
					observaciones = observacionOriginal.replaceAll("<br>", "\n");
					observaciones += "\n\n"+ConstantesBD.separadorSplit+UtilidadFecha.getFechaActual()+"  "+UtilidadFecha.getHoraActual()+ConstantesBD.separadorSplit;
				}
				else
					observaciones = UtilidadFecha.getFechaActual()+"  "+UtilidadFecha.getHoraActual();
				
				if(!nuevaObservacion.equals(""))
				{
					observaciones += "\n"+ConstantesBD.separadorSplit+nuevaObservacion+ConstantesBD.separadorSplit;
				}
				
				//if (UtilidadValidacion.esMedico(usuario))
				if(UtilidadValidacion.esProfesionalSalud(usuario))
				{
					observaciones += "\n"+ConstantesBD.separadorSplit+usuario.getInformacionGeneralPersonalSalud()+ConstantesBD.separadorSplit;
				}
				else
				{
					observaciones += "\n "+usuario.getNombreUsuario() + " (" +usuario.getLoginUsuario() + ")";
				}
			}	
		}
		
		return observaciones;
	}

	
	
	
	
	/**
	 * M�todo que permite, en los campos de observaciones (que deben
	 * llevar el nombre del m�dico, sus especialidades y dem�s en caso 
	 * de m�dico y el nombre y login en caso de usuario) agregar un dato
	 * extra 
	 * 
	 * @param observacionOriginal String con la observaci�n original
	 * @param nuevaObservacion String con la observaci�n a agregar
	 * @param usuario usuario responsable de esta observaci�n a agregar 
	 * @param validarNull Par�metro que indica si se deben validar las observaciones en null
	 * @return cadena con los datos extra del usuario � del m�dico
	 */
	public static String agregarTextoAObservacionFechaGrabacion (String observacionOriginal, String nuevaObservacion, UsuarioBasico usuario, boolean validarNull)
	{
		String observaciones = new String();
		
		if (usuario!=null)
		{
			if(!validarNull && observacionOriginal==null)
			{
				observacionOriginal="";
			}
			if(!validarNull && nuevaObservacion==null)
			{
				nuevaObservacion="";
			}
			if((nuevaObservacion==null || nuevaObservacion.equals("")) && validarNull)
			{
				observaciones="";
			}
			else
			{	
				if( observacionOriginal != null )
				{
					observaciones = observacionOriginal.replaceAll("<br>", "\n");
					observaciones += "\n\nFecha/Hora Grabaci�n: "+UtilidadFecha.getFechaActual()+"  "+UtilidadFecha.getHoraActual();
				}
				else
					observaciones = "Fecha/Hora Grabaci�n: " + UtilidadFecha.getFechaActual()+"  "+UtilidadFecha.getHoraActual();
				
				if(!nuevaObservacion.equals(""))
				{
					observaciones += "\n"+nuevaObservacion;
				}
				
				//if (UtilidadValidacion.esMedico(usuario))
				if(UtilidadValidacion.esProfesionalSalud(usuario))
				{
					observaciones += "\n"+usuario.getInformacionGeneralPersonalSalud();
				}
				else
				{
					observaciones += "\n"+usuario.getNombreUsuario() + " (" +usuario.getLoginUsuario() + ")";
				}
			}	

		}
		
		return observaciones;
	}

	/**
	 * M�todo que dado un double y un formato (Ej ##0.00),
	 * nos devuelve un StringBuffer con el double en el formato
	 * deseado (Con el formato del ejemplo obtenemos el 
	 * double en un String con unicamente 2 cifras decimales) 
	 * 
	 * @param numeroDouble El n�mero (double) al cual le
	 * queremos aplicar el formato  
	 * @param formato formato que deseamos aplicar
	 * @return
	 */
	public static StringBuffer getDoubleConFormatoEspecifico (double numeroDouble, String formato)
	{
		DecimalFormat format = new DecimalFormat(formato);
		FieldPosition f = new FieldPosition(0);
		StringBuffer s = new StringBuffer();
		return format.format(numeroDouble, s, f);
	}
	

	/**
	 * metodo que devuelve la representacion en String de un numero decimal grande sin exponente
	 * @param doubleNum
	 * @return  el numero double sin exponente
	 */
	public static String getBigDecimalString(double doubleNum){
		String doubleStr = Double.toString(doubleNum);
		BigDecimal bDec = new BigDecimal(doubleStr);
		doubleStr = bDec.toString(); 
		return doubleStr;
 }


	/**
	 * metodo que trunca el numero de decimales a un String que representa un numero double ( debe estar sin exponente )
	 * @param doubleStr  el String que contiene el numero double
	 * @param numberOfDecimals  el numero de decimales que seran tenidos en cuenta
	 * @return  la cadena con el numero double truncado con el numero de decimales deseados
	 */
	public static String truncateDouble(String doubleStr, int numberOfDecimals){
	  String resultStr = "";
	  int idxPoint = -1;
	  int strLength = doubleStr.length(); 
	  int nDec = 0;
	  
	  idxPoint = doubleStr.indexOf(".");
	  
	  if(idxPoint == -1)
	  	return doubleStr;
	  
	  nDec = strLength - (idxPoint  +1);
	  if ( nDec <= numberOfDecimals )
	  	return doubleStr;
	  
	  resultStr = doubleStr.substring(0, (idxPoint +numberOfDecimals +1));
	  
		return resultStr;
	}

	
	
	/**
	 * metodo que trunca un double o float para un numero determinado de decimales
	 * @param doubleNum    el objeto de tipo double
	 * @param numOfDecimals  el numero de decimales que se quiere truncar
	 * @return un nuevo double truncado
	 */
	public static double truncateDouble(double doubleNum, int numOfDecimals){
		double result = 0.00;
		String doubleStr = getBigDecimalString(doubleNum); 
		doubleStr = truncateDouble(doubleStr, numOfDecimals);
		try{
		  result  = Double.parseDouble(doubleStr);
		}
		catch(Exception e){
		  e.printStackTrace();	
		}
		return result;
	}


	/**
	 * M�todo que me devuelve un vector con las posibilidades 
	 * con las que se debe reemplazar un texto en caso de querer
	 * buscar por todas las alternativas que se ofrecer�an en caso
	 * de soportar expresiones regulares. SOLO DEVUELVE LOS
	 * CASOS EN MAYUSCULAS
	 *  
	 * @param texto
	 * @return
	 */
	public static Vector generarVectorExpresionRegularMayuscula(String texto)
	{
		Vector vectorReal=new Vector(10,5);
		int i, j, k;
		texto=texto.toUpperCase();

		for (i=0;i<texto.length();i++)
		{

			char arregloEncontrado[]=arregloAReemplazar(texto.charAt(i));

			//Dos casos grandes, si el vector tiene tama�o 0 o no
			if (vectorReal.size()==0)
			{
				//Si es "Vocal" el arreglo tiene tama�o mayor a 0
				if (arregloEncontrado.length>0)
				{
					for (j=0;j<arregloEncontrado.length;j++)
					{
					vectorReal.add(texto.substring(0,i) + arregloEncontrado[j]);
					}
				}
				else
				{
					//No se hace nada
				}
			}
			else
			{
			//Si es "Vocal" el arreglo tiene tama�o mayor a 0
				if (arregloEncontrado.length>0)
				{
					Vector vectorTrabajo=new Vector(10,5);
					String elementoTemporal;
					//Por Cada uno de los elementos del vector real
					//se crean tantos como caracteres en el arreglo
					for (j=0;j<vectorReal.size();j++)
					{
					elementoTemporal=(String)vectorReal.get(j);
					for (k=0;k<arregloEncontrado.length;k++)
					{
						vectorTrabajo.add(elementoTemporal + arregloEncontrado[k]);
					}
					}
					vectorReal=vectorTrabajo;
				}
				else
				{
					//En caso de consonante todos los elementos crecen
					Vector vectorTrabajo=new Vector(10,5);
					for (j=0;j<vectorReal.size();j++)
					{
					vectorTrabajo.add( ( (String) vectorReal.get(j) )+ texto.charAt(i) );
					}
					vectorReal=vectorTrabajo;
				}
			}
		}

		//Si no entro ninguna vez, quiere decir que no hab�an vocales
		//luego el vector contiene un �nico elemento
		if (vectorReal.size()==0)
		{
			vectorReal.add(texto);
		}

		return vectorReal;
	}

	/**
	 * M�todo que recibe un caracter y decide por cuales
	 * caracteres debe ser reemplazado para simular una
	 * expresi�n regular en una BD que no la soporte
	 * 
	 * @param caracter C�racter a revisar
	 * @return
	 */
	private static char[] arregloAReemplazar (char caracter)
	{
		int i;
		char arregloA[]={'A', '�', 'a', '�'};
		char arregloE[]={'E', '�', 'e', '�'};
		char arregloI[]={'I', '�', 'i', '�'};
		char arregloO[]={'O', '�', 'o', '�'};
		char arregloU[]={'U', '�', '�', 'u', '�', '�'};

		for (i=0;i<arregloA.length;i++)
		{
			if (arregloA[i]==caracter)
			{
			return arregloA;
			}
		}
		for (i=0;i<arregloE.length;i++)
		{
			if (arregloE[i]==caracter)
			{
			return arregloE;
			}
		}
		for (i=0;i<arregloI.length;i++)
		{
			if (arregloI[i]==caracter)
			{
			return arregloI;
			}
		}
		for (i=0;i<arregloO.length;i++)
		{
			if (arregloO[i]==caracter)
			{
				return arregloO;
			}
		}
		for (i=0;i<arregloU.length;i++)
		{
			if (arregloU[i]==caracter)
			{
				return arregloU;
			}
		}
		return new char[0];
	}
	
	/**
	 * M�todo para Strings que se comporta como 
	 * los m�todos empty y notEmpty de struts
	 *
	 * @param textoAEvaluar Texto al que se le
	 * desea saber si est� vac�o o no
	 * @return
	 */
	public static boolean isEmpty(String textoAEvaluar)
	{
	    if (textoAEvaluar==null||textoAEvaluar.trim().isEmpty() ||textoAEvaluar.trim().equals("null"))
	    {
	        return true;
	    }
	    else
	    {
	        return false;
	    }
	}
	
	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public static boolean isEmpty(int codigo) {
		
		return false;
	}
	
	
	/**
	 * Convierte un Vector( String[] ) a una lista.(ArrayList<<String>>)
	 * Retorna un ArrayList<<String>>
	 * @param listaString
	 * @return
	 */
	public static ArrayList<String> convertirColeccionLista(String[] listaString)
	{
		ArrayList<String> newLista = new ArrayList<String>();
		for(String list :listaString)
		{
			newLista.add(list);
		}
		return newLista;
	}
	
	
	
	
	
	/**
	 * METODO QUE CONVIERTE UN STRING SEPARADO POR COMAS A UNA ARRAYLIST DE INTEGER
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static ArrayList<Integer> convertirStringArrayList(String lista)
	{
		ArrayList<Integer> listInteger= new ArrayList<Integer>();
		
		
		if(!UtilidadTexto.isEmpty(lista))
		{
			String[] listaString = lista.split(",");
			
			
			for(String str: listaString)
			{
				listInteger.add(new Integer(str.trim()));
			}
		}
		
		
		return  listInteger;
	}
	
	
	
	
	
	/**
	 * M�todo para Strings que se comporta como 
	 * los m�todos empty y notEmpty de struts
	 *
	 * @param textoAEvaluar Texto al que se le
	 * desea saber si est� vac�o o no
	 * @return
	 */
	public static boolean isEmpty2(String textoAEvaluar)
	{
	    if (textoAEvaluar==null||textoAEvaluar.equals("") ||textoAEvaluar.trim().equals("null"))
	    {
	        return true;
	    }
	    else
	    {
	        return false;
	    }
	}
	
	/**
	 * 
	 * @param textoAEvaluar
	 * @return
	 */
	public static boolean isNumber(String textoAEvaluar)
	{
		try
		{
			Double.parseDouble(textoAEvaluar);
		}
		catch (Exception e) 
		{
			return false;
		}
		return true;
	}
	
	/**
	 * M�todo que copia el contenido de un ArrayList
	 * en un arreglo de Strings. No revisa tama�os,
	 * usuario se asegura de esto
	 * 
	 * @param origen ArrayList de origen
	 * @param destino String[] de destino
	 */
	public static void copiarArrayListAArreglo (ArrayList origen, String[] destino)
	{
		for(int i=0; i<origen.size(); i++)
		{
		    destino[i] = origen.get(i) + "";
		}
	}
	
	/**
	 * M�todo que revisa si un entero se encuentra 
	 * en un arreglo de enteros
	 * 
	 * @param arreglo arreglo de enteros donde se
	 * busca el elemento 
	 * @param elemBuscado Entero con el elemento
	 * buscado
	 * @return
	 */
	public static boolean estaEnArreglo (int arreglo[], int elemBuscado)
	{
	    if (arreglo==null)
	    {
	        return false;
	    }
	    else
	    {
	        int i;
	        for (i=0;i<arreglo.length;i++)
	        {
	            if (arreglo[i]==elemBuscado)
	            {
	                return true;
	            }
	        }
	    }
	    return false;
	}

	/**
	 * Este m�todo revisa que todos los elementos de un arreglo sean mayores 
	 * que 0
	 *  
	 * @param respArreglo Arreglo a revisar
	 * @return true si todos los elementos de un arreglo son mayores 
	 * que 0, false de lo contrario 
	 */	
	public static boolean revisarArregloResp(int respArreglo[])
	{
		int i;
		for (i=0;i<respArreglo.length;i++)
		{
			if (respArreglo[0]<=0)
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * M�todo que se encarga de retornar el valor booleano
	 * de la cadena enviada (No discrimina may�sculas y min�sculas)
	 * @param validarEstadoSolicitudesInterpretadas
	 * @return true en caso de que la cadena enviada contenga los valores "true", "t" � "1"
	 * false en caso de que la cadena enviada contenga los valores "false", "f" � "0"
	 * Si la cadena enviada no se encuentra en el rango de valores descrito
	 * se env�a un log a la salida del sistema y retorna false.
	 */
	public static boolean getBoolean(String cadenaConBoolean)
	{
		if(cadenaConBoolean==null)
		{
			return false;
		}
		if(cadenaConBoolean.equals("null"))
		{
			return false;
		}
		cadenaConBoolean=cadenaConBoolean.toLowerCase();
		
		if(cadenaConBoolean.equalsIgnoreCase("true") || cadenaConBoolean.equalsIgnoreCase("t") || cadenaConBoolean.equalsIgnoreCase("1") || cadenaConBoolean.equalsIgnoreCase("on") || cadenaConBoolean.equalsIgnoreCase("s") || cadenaConBoolean.equalsIgnoreCase("y") || cadenaConBoolean.equalsIgnoreCase("si") || cadenaConBoolean.equalsIgnoreCase("yes") || cadenaConBoolean.equalsIgnoreCase(ConstantesBD.acronimoSi))
		{
			return true;
		}
		else if(cadenaConBoolean.equalsIgnoreCase("false") || cadenaConBoolean.equalsIgnoreCase("f") || cadenaConBoolean.equalsIgnoreCase("0") || cadenaConBoolean.equalsIgnoreCase("off") || cadenaConBoolean.equalsIgnoreCase("n")  || cadenaConBoolean.equalsIgnoreCase("N")  ||   cadenaConBoolean.equalsIgnoreCase("no") || cadenaConBoolean.equalsIgnoreCase(""))
		{
			return false;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean getBoolean(Object datoBoolean) {
		if(datoBoolean instanceof Boolean)
		{
			Boolean b=(Boolean)datoBoolean;
			if(b!=null)
			{
				return b;
			}
			else
			{
				return false;
			}
		}
		String bStr=datoBoolean+"";
		return getBoolean(bStr);
	}

	/**
	 * Adici�n Sebasti�n
	 * M�todo para arreglar las tildes que se da�an en los par�metros que son
	 * pasados de JSP a JSP
	 * @param cadena con las tildes da�adas
	 * @return cadena arreglada
	 */
	public static String organizarTildes(String cadena){
		
		try{
			
			String nueva_cadena="";
			for(int i=0;i<cadena.length();i++){
				if(cadena.charAt(i)=='�'){
					if(cadena.charAt(i+1)=='�'){
						nueva_cadena+="�";
						i++;
					}
					
					else if(cadena.charAt(i+1)=='�'){
						nueva_cadena+="�";
						i++;
					}
					else if(cadena.charAt(i+1)=='�'){
						nueva_cadena+="�";
						i++;
					}
					else{
						nueva_cadena+="�";
						i++;
					}
					
				}
				
				else if(cadena.charAt(i)=='&'&&cadena.charAt(i+2)=='a'&&cadena.charAt(i+3)=='c'&&
						cadena.charAt(i+4)=='u'&&cadena.charAt(i+5)=='t'&&cadena.charAt(i+6)=='e'&&
						cadena.charAt(i+7)==';')
				{
					if(cadena.charAt(i+1)=='a'){
						nueva_cadena+="�";
						i+=7;
					}
					
					else if(cadena.charAt(i+1)=='e'){
						nueva_cadena+="�";
						i+=7;
					}
					else if(cadena.charAt(i+1)=='i'){
						nueva_cadena+="�";
						i+=7;
					}
					else if(cadena.charAt(i+1)=='o')
					{
						nueva_cadena+="�";
						i+=7;
					}
					else if(cadena.charAt(i+1)=='u')
					{
						nueva_cadena+="�";
						i+=7;
					}
				}
				else{
						nueva_cadena+=cadena.charAt(i);
					}
			}
			
			
			
			
			return nueva_cadena;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
			
		}
	}
	/**
	 * M�todo que codifica o descodifica cadenas que contienen expresiones regulares,
	 * las cuales pueden ser escritas en observaciones y justificaciones o cualquier otra cadena, esto se hace con
	 * el prop�sito de no tener problemas con el java.util.regex o cuando sea necesario enviar por el request alg�n string
	 * (que pueda contener estos valores) en el url. Ej= pagina?cadenaAcodificar=coment%%&estado=empezar
	 * en este caso enviaria= pagina?cadenaAcodificar=comentppoorrcceennttppoorrcceenntt&estado=empezar
	 * y al descodificarla hace el proceso inverso y seria obtenida sin errores en el request.getAttribute
	 * @param cadena, cadena a codificar o descodificar
	 * @param esCodificar, boolean que indica si true=codificar o false=descodificar
	 * @return
	 */
	public static String codificarDescodificarCadenaCaracteresEspeciales(String cadena, boolean esCodificar)
	{
	    String cadenaCodificadaODescodificada="";
	    boolean reemplazo=false;
	    //el tamanio debe corresponder en ambos vectores
	    String vectorCaracteresEspeciales[]={"%", "&" , "(" , ")" , "/" , "?" , "$" , "*" , "~" , "^" , "." , "+" , "[" , "]" , "{" , "}", "'"};
	    String vectorCodificacionCaracteres[]={	"ppoorrcceenntt","aammppeerrssanntt", "ppaarreenntteessiissaabbrriirr", 
	            													"ppaarreenntteessiissscceerraarr", "ssllaasshh", "iinnccooggnniittaa", 
	            													"ppeessooss", "aasstteerriissccoo", "bbiirrgguulliillllaa", "eexxppoonneenncc",
	            													"ppuunnttoo", "ssuummaa", "ccoorrcchheetteeaabbrriirr", "ccoorrcchheetteecceerraarr",
	            													"yyaavveeaabbrriirr", "yyaavveecceerraarr","ccoommiillaasseenncciillaa"};
	    try
	    {
		    for(int cont=0; cont<cadena.length(); cont++)
			{
		        if(esCodificar)
		        {
		            reemplazo=false;
		            for(int cont1=0; cont1<vectorCaracteresEspeciales.length; cont1++)
		            {
		                if((cadena.charAt(cont)+"").equals(vectorCaracteresEspeciales[cont1]))
		                {
		                    cadenaCodificadaODescodificada+= vectorCodificacionCaracteres[cont1];
		                    reemplazo=true;
		                }
		            }
		            if(!reemplazo)
		                cadenaCodificadaODescodificada+= cadena.charAt(cont);
		        }	
				else
				{
				    for(int cont1=0; cont1<vectorCaracteresEspeciales.length; cont1++)
		            {
				        cadena=cadena.replaceAll(vectorCodificacionCaracteres[cont1], vectorCaracteresEspeciales[cont1] );
		            }
				    cadenaCodificadaODescodificada=cadena;
				}
		 	}
	        return cadenaCodificadaODescodificada;
	   }
	    catch(Exception e)
	    {
	        e.printStackTrace();
	        return cadena;
	    }
	}
	
	
	/**
	 * M�todo que formatea una numero exponencial a su
	 * respectivo valor flotante
	 * @param valor
	 * @return
	 */
	public static String formatearExponenciales(double valor)
	{
		Locale.setDefault(Locale.ENGLISH); //por defecto siempre debe ser ingl�s
		NumberFormat formato= new DecimalFormat("#.##########");
		return formato.format(valor); 
	}
    
    /**
     * Metodo que retorna un String enviando el patron con el que se quiereFormatear.
     * @param valor, double a formatear
     * @param patron, patron para utilizar el formato.
     * @return String
     */
    public static String formatearValores(double valor,String patron)
    {
    	Locale.setDefault(Locale.ENGLISH); //por defecto siempre debe ser ingl�s
        DecimalFormat formato = new DecimalFormat(patron);
        return formato.format(valor);
    }
    
    /**
     * Metodo que retorna un String, con el valor de un double unidades-decenas-centenas-millones, y 2 decimales.
     * @param valor, double a formatear
     * @param patron, patron para utilizar el formato.
     * @return String
     */
    public static String formatearValores(double valor)
    {
        DecimalFormat formato=new DecimalFormat("###,##0.00");
    	//DecimalFormat formato=new DecimalFormat("#");
        //return formatearValores(formato.format(valor),2,true,true); 
        Locale.setDefault(Locale.ENGLISH); //por defecto siempre debe ser ingl�s
        return formato.format(valor);
    }
    /**
     * Metodo que retorna un String enviando el patron con el que se quiereFormatear.
     * @param valor, double a formatear
     * @param patron, patron para utilizar el formato.
     * @return String
     */
    public static String formatearValores(String valor,String patron)
    {
    	Locale.setDefault(Locale.ENGLISH); //por defecto siempre debe ser ingl�s
        DecimalFormat formato = new DecimalFormat(patron);
        if(Utilidades.convertirADouble(valor)!=ConstantesBD.codigoNuncaValido)
        	return formato.format(Utilidades.convertirADouble(valor));
        else
        	return valor;
    }
    
    /**
     * Metodo que retorna un String, con el valor de un double unidades-decenas-centenas-millones, y 2 decimales.
     * @param valor, double a formatear
     * @param patron, patron para utilizar el formato.
     * @return String
     */
    public static String formatearValores(String valor)
    {
    	if(UtilidadTexto.isEmpty(valor))
    		valor="0";
    	Locale.setDefault(Locale.ENGLISH); //por defecto siempre debe ser ingl�s
        DecimalFormat formato=new DecimalFormat("###,##0.00");
    	//DecimalFormat formato=new DecimalFormat("#####");
        //return formatearValores(formato.format(Double.parseDouble(valor)),2,true,true); 
        return formato.format(Double.parseDouble(valor));
    }
    
    /**
     * Metodo que retorna un String, con el valor de un double unidades-decenas-centenas-millones, y 2 decimales.
     * @param valor, double a formatear
     * @param patron, patron para utilizar el formato.
     * @return String
     */
    public static String formatearValoresSinDecimales(String valor)
    {
    	if(UtilidadTexto.isEmpty(valor))
    		valor="0";
    	Locale.setDefault(Locale.ENGLISH); //por defecto siempre debe ser ingl�s
        DecimalFormat formato=new DecimalFormat("###,##0");
    	//DecimalFormat formato=new DecimalFormat("#####");
        //return formatearValores(formato.format(Double.parseDouble(valor)),2,true,true); 
        return formato.format(Double.parseDouble(valor));
    }
    
    /**
     * M�todo para saber si un n�mero est� expresado en forma exponencial
     * @param numero
     * @return true si esta en forma exponencial sino retorna false
     */
    public static boolean numeroEsExponencial (String numero)
    {
    	String numUpper=numero.toUpperCase();
    	
    	if(numUpper.indexOf("E")!=-1)
    		return true;
    	else
    		return false;
    }
    
    /**
     * 
     * @param cadena
     * @return
     */
    public static String removerComillasJavaScript(String cadena)
    {
    	cadena=cadena.replaceAll("\"", "");
    	cadena=cadena.replaceAll("'", "");
    	return cadena;
    }
    
    /**
     * 
     * @param valor
     * @return
     */    
    public static int aproximarSiguienteUnidad(String valor)
    {
    	valor=UtilidadTexto.formatearValores(Double.parseDouble(valor), "#.####################");
    	valor=valor.replace(".", "@@@@");
    	String vector[]=valor.split("@@@@");
    	int incremento=0;
    	if(vector.length==1)
    		return Integer.parseInt(valor);
    	int unidad= Integer.parseInt(vector[0]);
    	int decimal=0;
    	//para evitar error cuando el valor excede el integer
    	if(vector[1].length()>10)
    		decimal=1;
    	else
    		decimal= Integer.parseInt(vector[1]);
    	if(decimal>0)
    		incremento++;
    	return unidad+incremento;
    }
    
    /**
     * 
     * @param valor
     * @return
     */    
    public static int aproximarAnteriorUnidad(String valor)
    {
    	valor=UtilidadTexto.formatearValores(Double.parseDouble(valor), "#.####################");
    	valor=valor.replace(".", "@@@@");
    	String vector[]=valor.split("@@@@");
    	int unidad= Integer.parseInt(vector[0]);
    	return unidad;
    }
    
    /**
	 * M�todo usado para quitar los caracteres especiales 
	 * en los campos de texto
	 * @param cadena
	 * @return
	 */
	public static String revisarCaracteresEspeciales(String cadena)
	{
		String nuevaCadena="";
		for(int i=0;i<cadena.length();i++)
		{
			if(cadena.charAt(i)>=48&&cadena.charAt(i)<=57||
					cadena.charAt(i)>=65&&cadena.charAt(i)<=90||
					cadena.charAt(i)>=97&&cadena.charAt(i)<=122||
					cadena.charAt(i)==32||cadena.charAt(i)=='�'||
					cadena.charAt(i)=='�'||cadena.charAt(i)=='�'||
					cadena.charAt(i)=='�'||cadena.charAt(i)=='�'||
					cadena.charAt(i)=='�'||cadena.charAt(i)=='�')
			{
				nuevaCadena+=cadena.charAt(i);
			}
		}
		
		return nuevaCadena;
	}
	
	
	/**
	 * M�todo para editar los espacios en una cadena con un tama�o limite
	 * @param campo
	 * @param tamano
	 * @param limite
	 * @param esNumerico: si es num�rico los espacios se a�aden antes, si es cadena los espacios se a�aden despu�s
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
	 * M�todo que separa la parte entera de la decimal de una cantidad num�rica
	 * @param cadena
	 * @return
	 */
	public static String[] separarParteEnteraYDecimal(String cadena)
	{
		String entera = "", decimal = "";
		boolean paso = false;
		for(int i=0;i<cadena.length();i++)
		{
			if(cadena.charAt(i)=='.')
				paso = true;
			else
			{
				if(!paso)
					entera += cadena.charAt(i);
				else
					decimal += cadena.charAt(i);
			}
		}
		
		if(decimal.equals(""))
			decimal = "0";
		
		String[] vector = {entera,decimal};
		
		return vector;
	}
    
	/**
	 * 
	 * @param vector
	 * @param usarComillas
	 * @return
	 */
	public static String convertirVectorACodigosSeparadosXComas(Vector vector, boolean usarComillas)
	{
		String cadena="";
		String caracterReservado="";
		if(usarComillas)
			caracterReservado="'";
		
		for(int w=0; w<vector.size(); w++)
		{
			if(w>0 && w<(vector.size()))
				cadena+=",";
			String element="";
			try{
				element=new BigDecimal(vector.get(w)+"").longValue()+"";
			}
			catch(NumberFormatException nfe){
				element=vector.get(w)+"";
			}
			cadena+= caracterReservado+element+caracterReservado;
		}
		return cadena;
	}
	
	
	/**
	 * 
	 * @param vector
	 * @param usarComillas
	 * @return
	 */
	public static String convertirVectorACodigosSeparadosXOr(Vector vector, String entidad,  boolean usarOR)
	{
		String cadena="";
		String caracterReservado="";
		if(usarOR)
			caracterReservado="'";
		
		for(int w=0; w<vector.size(); w++)
		{
			if(w>0 && w<(vector.size()))
				cadena+=" OR ";
			String element="";
			try{
				element=new BigDecimal(vector.get(w)+"").longValue()+"";
			}
			catch(NumberFormatException nfe){
				element=vector.get(w)+"";
			}
			cadena+= entidad +" = "+caracterReservado+element+caracterReservado;
		}
		return cadena;
	}
	
	/**
	 * 
	 * @param String
	 * @param String
	 * @return Vector
	 */
	public static Vector StringToVector(String cadena)
	   {
	       
	       StringTokenizer st = new StringTokenizer(cadena, ",");
	       Vector v = new Vector();
	       while (st.hasMoreElements())
	       {
	           v.add(st.nextElement());
	       }
	       return v;
	       
	   }
	
	/**
	 * 
	 * @param vector
	 * @param usarComillas
	 * @return
	 */
	public static String convertirArrayIntegerACodigosSeparadosXComas(ArrayList<Integer> array)
	{
		String cadena="";
		
		int w=0;
		for(Integer i: array)
		{
			if(w>0 && w<(array.size()))
				cadena+=",";
			cadena+= i;
			w++;
		}
		
		return cadena;
	}

	/**
	 * 
	 * @param vector
	 * @param usarComillas
	 * @return
	 */
	public static String convertirArrayDoubleACodigosSeparadosXComas(ArrayList<Double> array)
	{
		String cadena="";
		
		int w=0;
		for(Double i: array)
		{
			if(w>0 && w<(array.size()))
				cadena+=",";
			cadena+= i;
			w++;
		}
		return cadena;
	}
	
	/**
	 * 
	 * @param vector
	 * @param usarComillas
	 * @return
	 */
	public static String convertirArrayBigDecimalACodigosSeparadosXComas(ArrayList<BigDecimal> array)
	{
		String cadena="";
		int w=0;
		for(BigDecimal i: array)
		{
			if(w>0 && w<(array.size()))
				cadena+=",";
			cadena+= i;
			w++;
		}
		return cadena;
	}
	

	/**
	 * 
	 * @param vector
	 * @param usarComillas
	 * @return
	 */
	public static String convertirArrayStringACodigosSeparadosXComas(ArrayList<String> array)
	{
		String cadena="";
		
		int w=0;
		for(String i: array)
		{
			if(w>0 && w<(array.size()))
				cadena+=",";
			cadena+= "'"+i+"'";
			w++;
		}
		return cadena;
	}

	public static String convertirArrayStringACodigosSeparadosXComas(String[] array)
	{
		String cadena="";
		
		int w=0;
		for(String i: array)
		{
			if(w>0 && w<(array.length))
				cadena+=",";
			cadena+= "'"+i+"'";
			w++;
		}
		return cadena;
	}

	
	/**
	 * 
	 * @param vector
	 * @param usarComillas
	 * @return
	 */
	public static String convertirArrayStringACodigosSeparadosXComasSinComillas(ArrayList<String> array)
	{
		String cadena="";
		
		int w=0;
		for(String i: array)
		{
			if(w>0 && w<(array.size()))
				cadena+=",";
			cadena+= ""+i+"";
			w++;
		}
		return cadena;
	}
	
	
	/**
	 * 
	 * @param cadena
	 * @return String
	 */
	public static String convertirSN(String cadena)
	{
		if(UtilidadTexto.getBoolean(cadena))
			return ConstantesBD.acronimoSi;
		else
			return ConstantesBD.acronimoNo;
	}
	
	
	 /**
	  * Convierte a S o a N un valor booleano
	  * @param booleano
	  * @return String
	  */
	 public static String convertirSN(boolean booleano)
	 {
	  if(booleano)
	   return ConstantesBD.acronimoSi;
	  else
	   return ConstantesBD.acronimoNo;
	 }
	 
	
	/**
	 * 
	 * @param cadena
	 * @return
	 */
	public static String imprimirSiNo(String cadena)
	{
		if(UtilidadTexto.getBoolean(cadena))
			return "Si";
		else
			return "No";
	}
	
	/**
	 * M�todo usado para cambiar los caracteres especiales por letras normales 
	 * en los campos de texto
	 * @param cadena
	 * @return
	 */
	public static String cambiarCaracteresEspeciales(String cadena)
	{
		String nuevaCadena="";
		String caracter="";
		for(int i=0;i<cadena.length();i++)
		{
			if(cadena.charAt(i)=='�')
			{
				caracter="a";
				nuevaCadena+=caracter;
			}
			else
				if(cadena.charAt(i)=='�')
				{
					caracter="e";
					nuevaCadena+=caracter;
				}
				else
					if(cadena.charAt(i)=='�')
					{
						caracter="i";
						nuevaCadena+=caracter;
					}
					else
						if(cadena.charAt(i)=='�')
						{
							caracter="o";
							nuevaCadena+=caracter;
						}
						else
							if(cadena.charAt(i)=='�')
							{
								caracter="u";
								nuevaCadena+=caracter;
							}
							else
								if(cadena.charAt(i)=='�')
								{
									caracter="n";
									nuevaCadena+=caracter;
								}
								else
									if(cadena.charAt(i)=='�')
									{
										caracter="N";
										nuevaCadena+=caracter;
									}
									else
										if(cadena.charAt(i)=='�')
										{
											caracter="U";
											nuevaCadena+=caracter;
										}
			
			else if(cadena.charAt(i)=='�')
			{
				caracter="A";
				nuevaCadena+=caracter;
			}
			else
				if(cadena.charAt(i)=='�')
				{
					caracter="E";
					nuevaCadena+=caracter;
				}
				else
					if(cadena.charAt(i)=='�')
					{
						caracter="I";
						nuevaCadena+=caracter;
					}
					else
						if(cadena.charAt(i)=='�')
						{
							caracter="O";
							nuevaCadena+=caracter;
						}
						else
							if(cadena.charAt(i)=='�')
							{
								caracter="U";
								nuevaCadena+=caracter;
							}
							else
							{
								nuevaCadena+=cadena.charAt(i);
							}
		}
		return nuevaCadena;
	}
	
	/**
	 * Utilidad que le pone a los valores un formato estandar,
	 * separa con comas los miles y pone ceros separados por un punto(0) a los decimales
	 * en caso de que el valor no tenga parte decimal, aproximando
	 * en caso tal que lo necesite.
	 * @param valor Cadena con el valor que se desea formatear
	 * @param numeroDecimales N�mero de decimales que desea que tenga el texto formateado
	 * @param ponerPuntos booleano que indica si debe poner puntos de separaci�n de miles
	 * @param manejarComaSeparacionDecimales Booleano para definir si la separaci�n de decimales se hace con comas o con puntos
	 * @return cadena formateada de acuerdo a los par�metros pasados
	 */
	public static String formatearValoresConComas(String valor, int numeroDecimales, boolean ponerComas, boolean manejarPuntoSeparacionDecimales)
	{
		String formateado="";
		try
		{
			Double.parseDouble(valor);
			String[] partes=new String[2];
			partes[0]="";
			partes[1]="";
			for(int i=0, j=0; i<valor.length(); i++)
			{
				char ch=valor.charAt(i);
				if(ch!='.')
				{
					partes[j]+=valor.charAt(i);
				}
				else
				{
					j++;
				}
			}

			int largo=partes[1].length();
			largo=(largo>numeroDecimales)?numeroDecimales:largo;
			boolean deboSumar=false;
			String tempoFormateado="";
			for(int i=0; i<largo+1; i++)
			{
				if(i!=largo)
				{
					tempoFormateado+=partes[1].charAt(i);
				}
				else
				{
					int ultimoDigito=0;
					if(i>=partes[1].length())
					{
						ultimoDigito=0;
					}
					else
					{
						ultimoDigito=Integer.parseInt(partes[1].charAt(i)+"");	
					}
					if(ultimoDigito>=5)
					{
						if(largo==0)
						{
							deboSumar=true;
						}
						else
						{
							tempoFormateado=(Integer.parseInt(tempoFormateado)+1)+"";
						}
					}
				}
			}

			largo=partes[0].length();
			for(int i=largo-1, j=0; i>=0; i--,j++)
			{
				if(i==largo-1 && deboSumar)
				{
					formateado=(Integer.parseInt(partes[0].charAt(i)+"")+1)+"";
				}
				else
				{
					formateado=partes[0].charAt(i)+formateado;
				}
				if(j==2 && i!=0 && ponerComas)
				{
					formateado=","+formateado;
					j=-1;
				}
			}
			if(numeroDecimales!=0)
			{
				largo=tempoFormateado.length();
				if(manejarPuntoSeparacionDecimales)
				{
					formateado=formateado+"."+tempoFormateado;
				}
				else
				{
					formateado=formateado+","+tempoFormateado;
				}
				for(int i=largo;i<numeroDecimales;i++)
				{
					formateado+="0";
				}
			}
		}
		catch(NumberFormatException e)
		{
			formateado="";
		}
		return formateado;
	}

	public static String getBooleanSegunTipoBD(boolean datoBooleano) {
		String tipoBD=System.getProperty("TIPOBD");
		if(tipoBD.equals("POSTGRES"))
		{
			return datoBooleano+"";
		}else if(tipoBD.equals("ORACLE"))
		{
			return datoBooleano?"1":"0";
		}else
		{
			return datoBooleano+"";
		}
	}

	
	public static String getNombreEstadoSegunBoolean(boolean datoBooleano) {
		if(datoBooleano){
			return "Activo";
		}
		else{
			return "Inactivo";
		}
	}

	
	/**
	 * @param cadena
	 * @return cadena con la primera letra en mayuscula
	 */
	public static String primeraLetraMayuscula(String cadena){
		String primeraLetra="";
		if (cadena!=null && !cadena.isEmpty()) {
			cadena=cadena.toLowerCase();
			primeraLetra=cadena.substring(0,1);
			primeraLetra=primeraLetra.toUpperCase();
			primeraLetra+=cadena.substring(1,cadena.length());
			
		}
		return primeraLetra;
	}
	
	
	/**
	 * Funcion que realiza el split de forma centralizada segun el valor que contenga la cadena splitter
	 * @param cadena
	 * @param splitter
	 * @return arreglo separado segun el parametro ingresado
	 */
	public static String[] splitBy(String cadena,String splitter){
		return cadena.split(splitter);
	}
	
	/**
	 * Recibe una palabra y convierte la primera letra a May�scula.
	 * y las dem�s en min�scula.
	 * 
	 * @param palabraAconvertir
	 * @return palabra en min�scula con la primera en may�scula.
	 */
	public static String convertirPrimeraLetraCapital (String palabraAconvertir){
	
		if(!isEmpty(palabraAconvertir) && palabraAconvertir.length()>1){

			palabraAconvertir = palabraAconvertir.toLowerCase();
			palabraAconvertir = palabraAconvertir.substring(0,1).toUpperCase() + palabraAconvertir.substring(1);
		}
		
		return palabraAconvertir;
	}
	
	/**
	 * Este m�todo reemplaza las comillas encontradas en una cadena recuperada
	 * de la bd para que pueda ser interpretada por un input type=text escapando
	 * las mismas
	 * @param cadena
	 * @return
	 */
	public static String reemplazarComillas(String cadena) {
		return cadena.replaceAll("\"", "&#34");
	}
	
	

	/**
	 * Capitaliza una cadena de texto. La primera mayuscila y el resto minuscula
	 * @param palabra
	 * @author Cristhian Murillo
	 * 
	 * @return capitalizada
	 */
	public static String capitalizar(String palabra)
	{
		String capitalizada = "";
		
		if(!isEmpty(palabra)){
			capitalizada = palabra.substring(0, 1).toUpperCase() + palabra.substring(1).toLowerCase();
		}
		
		return capitalizada;
	}
	
/**
 * Una serie de metodos para convertir numeros a letras
 */
    private static final String[] UNIDADES = { "", "UNO ", "DOS ", "TRES ",
            "CUATRO ", "CINCO ", "SEIS ", "SIETE ", "OCHO ", "NUEVE ", "DIEZ ",
            "ONCE ", "DOCE ", "TRECE ", "CATORCE ", "QUINCE ", "DIECISEIS",
            "DIECISIETE", "DIECIOCHO", "DIECINUEVE", "VEINTE" };

    private static final String[] DECENAS = { "VENTI", "TREINTA ", "CUARENTA ",
            "CINCUENTA ", "SESENTA ", "SETENTA ", "OCHENTA ", "NOVENTA ",
            "CIEN " };

    private static final String[] CENTENAS = { "CIENTO ", "DOSCIENTOS ",
            "TRESCIENTOS ", "CUATROCIENTOS ", "QUINIENTOS ", "SEISCIENTOS ",
            "SETECIENTOS ", "OCHOCIENTOS ", "NOVECIENTOS " };

    /**
* Convierte a letras un numero de la forma 123,456.32 
*
* @param number
* Numero en representacion texto
* @throws NumberFormatException
* Si valor del numero no es valido (fuera de rango o )
* @return Numero en letras
*/
    public static String convertNumberToLetter(String number)
            throws NumberFormatException {
        return convertNumberToLetter(Double.parseDouble(number));
    }

    /**
* Convierte un numero en representacion numerica a uno en representacion de
* texto. El numero es valido si esta entre 0 y 999'999.999
*
* @param number
* Numero a convertir en una variable double
* @return Numero convertido a texto
* @throws NumberFormatException
* Si el numero esta fuera del rango
*/
    public static String convertNumberToLetter(double doubleNumber)
            throws NumberFormatException {

        StringBuilder converted = new StringBuilder();

        String patternThreeDecimalPoints = "#.###";

        DecimalFormat format = new DecimalFormat(patternThreeDecimalPoints);
        format.setRoundingMode(RoundingMode.DOWN);

        // formateamos el numero, para ajustarlo a el formato de tres puntos
        // decimales
        String formatedDouble = format.format(doubleNumber);
        doubleNumber = Double.parseDouble(formatedDouble);

        // Validamos que sea un numero legal
        if (doubleNumber > 999999999)
            throw new NumberFormatException(
                    "El numero es mayor de 999'999.999, "
                            + "no es posible convertirlo");

        if (doubleNumber < 0)
            throw new NumberFormatException("El numero debe ser positivo");

        String splitNumber[] = String.valueOf(doubleNumber).replace('.', '#')
                .split("#");

        // Descompone el trio de millones
        int millon = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0],
                8))
                + String.valueOf(getDigitAt(splitNumber[0], 7))
                + String.valueOf(getDigitAt(splitNumber[0], 6)));
        if (millon == 1)
            converted.append("UN MILLON ");
        else if (millon > 1)
            converted.append(convertNumber(String.valueOf(millon))
                    + "MILLONES ");

        // Descompone el trio de miles
        int miles = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0],
                5))
                + String.valueOf(getDigitAt(splitNumber[0], 4))
                + String.valueOf(getDigitAt(splitNumber[0], 3)));
        if (miles == 1)
            converted.append("MIL ");
        else if (miles > 1)
            converted.append(convertNumber(String.valueOf(miles)) + "MIL ");

        // Descompone el ultimo trio de unidades
        int cientos = Integer.parseInt(String.valueOf(getDigitAt(
                splitNumber[0], 2))
                + String.valueOf(getDigitAt(splitNumber[0], 1))
                + String.valueOf(getDigitAt(splitNumber[0], 0)));
        if (cientos == 1)
            converted.append("UNO");

        if (millon + miles + cientos == 0)
            converted.append("CERO");
        if (cientos > 1)
            converted.append(convertNumber(String.valueOf(cientos)));

        
        // Descompone los decimales
        int centavos = Integer.parseInt(String.valueOf(getDigitAt(
                splitNumber[1], 2))
                + String.valueOf(getDigitAt(splitNumber[1], 1))
                + String.valueOf(getDigitAt(splitNumber[1], 0)));
        if (centavos == 1)
            converted.append(" PUNTO UNO");
        else if (centavos > 1)
            converted.append(" PUNTO " + convertNumber(String.valueOf(centavos)));

        return converted.toString();
    }

    /**
* Convierte los trios de numeros que componen las unidades, las decenas y
* las centenas del numero.
*
* @param number
* Numero a convetir en digitos
* @return Numero convertido en letras
*/
    private static String convertNumber(String number) {

        if (number.length() > 3)
            throw new NumberFormatException(
                    "La longitud maxima debe ser 3 digitos");

        // Caso especial con el 100
        if (number.equals("100")) {
            return "CIEN";
        }

        StringBuilder output = new StringBuilder();
        if (getDigitAt(number, 2) != 0)
            output.append(CENTENAS[getDigitAt(number, 2) - 1]);

        int k = Integer.parseInt(String.valueOf(getDigitAt(number, 1))
                + String.valueOf(getDigitAt(number, 0)));

        if (k <= 20)
            output.append(UNIDADES[k]);
        else if (k > 30 && getDigitAt(number, 0) != 0)
            output.append(DECENAS[getDigitAt(number, 1) - 2] + "Y "
                    + UNIDADES[getDigitAt(number, 0)]);
        else
            output.append(DECENAS[getDigitAt(number, 1) - 2]
                    + UNIDADES[getDigitAt(number, 0)]);

        return output.toString();
    }

    /**
* Retorna el digito numerico en la posicion indicada de derecha a izquierda
*
* @param origin
* Cadena en la cual se busca el digito
* @param position
* Posicion de derecha a izquierda a retornar
* @return Digito ubicado en la posicion indicada
*/
    private static int getDigitAt(String origin, int position) {
        if (origin.length() > position && position >= 0)
            return origin.charAt(origin.length() - position - 1) - 48;
        return 0;
    }
	
    /**
     * M�todo que parte un texto dado, cada n cantidad de caracteres (sin partir palabras)
     * @param texto
     * @return
     */
    public static String partirTextoNCaracteres (String texto, int maximoCaracteres) {
    	String [] arregloTexto = texto.split(" ");
		int caracteres = 0;
		StringBuilder strBldr = new StringBuilder();
		for (String s : arregloTexto) {
			if (caracteres <= maximoCaracteres) {
				if (s.contains("\n")) {
					strBldr.append(s);
					caracteres = s.length();
				} else {
					strBldr.append(s + " ");
					caracteres += s.length() + 1;
				}
			} else {
				strBldr.append("\n" + s + " ");
				caracteres = s.length() + 1;
			}
		}
		return strBldr.toString();
    }

}