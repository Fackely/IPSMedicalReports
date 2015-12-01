/*
* @(#)UtilidadFecha.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje   : Java
* Compilador : J2SDK 1.4.1_01
*
*/

package util;
import java.sql.Connection;
import java.sql.Time;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.dao.DaoFactory;
import com.servinte.axioma.dto.comun.DtoAnio;
import com.servinte.axioma.dto.comun.DtoMes;

/**
* Esta clase ofrece un conjunto de métodos útiles para el manejo de
* fechas en la aplicación
*
*	@version 1.0, Aug 6, 2003
*/
public class UtilidadFecha
{ 
	
/**
	* Método que cambia el formato de la fecha manejado por el usuario (dd/mm/aaaa)
	* ->estándar Latinoamericano) al formato manejado por la fuente de
	* datos (yyyy-mm-dd -> estándar Norteamericano)
	*
	* @param fecha Fecha en el formato manejado por el usuario (dd/mm/aaaa)
	* @return String Fecha en el formato manejado por la fuente de datos
	*/
	public static String conversionFormatoFechaABD(String fecha)
	{
		if(fecha != null && !(fecha = fecha.trim() ).equals("") )
		{
			String[] arregloFecha = (fecha).split("/");
			if(arregloFecha.length == 3)
			{
				fecha = new String();
				fecha = arregloFecha[2] + "-" + arregloFecha[1 ] +"-" + arregloFecha[0];
			}
		}
		else
			fecha = "";

		return fecha;
	}

	/**
	* Este m&eacute;todo convierte una fecha en formato yyyy-mm-dd a formato
	* dd/mm/aaaa
	*
	* @param fechaOriginal La fecha a convertir (en formato yyyy-mm-dd)
	* @return La fecha en formato dd/mm/aaaa
	*/
	public static String conversionFormatoFechaAAp(String fecha) throws NumberFormatException
	{
	    //Si el tamaño de la fecha supera los
	    //10 caracteres, significa que la BD
	    //nos está entregando una fecha con 
	    //hora adjuntada (Ej. Oracle)
	    if (fecha!= null&& fecha.length()>10)
	    {
	        fecha=fecha.substring(0,10);
	    }
	    
		if(fecha != null && !(fecha = fecha.trim() ).equals("") )
		{
			String[] arregloFecha = (fecha).split("-");
			if(arregloFecha.length == 3)
			{
				fecha = new String();
				fecha = arregloFecha[2] + "/" + arregloFecha[1] + "/" + arregloFecha[0];
			}
		}
		else
			fecha = "";

		return fecha;
	}

	/**
	* Este método convierte un objeto <code>java.util.Date</code> en una cadena representando la
	* fecha en formato dd/mm/aaaa
	*
	* @param ad_fecha La fecha a convertir
	* @return La fecha en formato dd/mm/aaaa
	*/
	public static String conversionFormatoHoraAAp(Date ad_fecha)throws NumberFormatException
	{
		StringBuffer lsb_fecha = new StringBuffer();
		if(ad_fecha != null)
		{
			SimpleDateFormat lsdf_sdf;
			lsdf_sdf = new SimpleDateFormat("H:mm");
			lsdf_sdf.setLenient(false);
			lsb_fecha = lsdf_sdf.format(ad_fecha, lsb_fecha, new FieldPosition(DateFormat.YEAR_FIELD) );
		}
		return lsb_fecha.toString();
	}


	/**
	* Este método convierte un objeto <code>java.util.Date</code> en una cadena representando la
	* fecha en formato dd/mm/aaaa
	*
	* @param ad_fecha La fecha a convertir
	* @return La fecha en formato dd/mm/aaaa
	*/
	public static String conversionFormatoFechaAAp(Date ad_fecha)throws NumberFormatException
	{
		StringBuffer lsb_fecha = new StringBuffer();
		if(ad_fecha != null)
		{
			SimpleDateFormat lsdf_sdf;
			lsdf_sdf = new SimpleDateFormat("dd/MM/yyyy");
			lsdf_sdf.setLenient(false);
			lsb_fecha = lsdf_sdf.format(ad_fecha, lsb_fecha, new FieldPosition(DateFormat.YEAR_FIELD) );
		}
		return lsb_fecha.toString();
	}

	/**
	* Este método convierte un objeto <code>java.util.Date</code> en una
	* cadena representando la fecha en formato yyyy-mm-dd
	*
	* @param ad_fecha La fecha a convertir
	* @return La fecha en formato yyyy-mm-dd
	*/
	public static String conversionFormatoFechaABD(Date ad_fecha)
	{
		StringBuffer lsb_fecha = new StringBuffer();
		if(ad_fecha != null)
		{
			SimpleDateFormat lsdf_sdf;
			lsdf_sdf = new SimpleDateFormat("yyyy-MM-dd");
			lsdf_sdf.setLenient(false);
			lsb_fecha = lsdf_sdf.format(ad_fecha, lsb_fecha, new FieldPosition(DateFormat.YEAR_FIELD) );
		}
		return lsb_fecha.toString();
	}

	/**
	* Este método convierte un tipo long en una
	* cadena representando la fecha en formato yyyy-mm-dd
	*
	* @param al_fecha La fecha a convertir (en formato yyyy-mm-dd)
	* @return La fecha en formato yyyy-mm-dd
	*/
	public static String conversionFormatoFechaABD(long al_fecha)
	{
		return conversionFormatoFechaABD(new Date(al_fecha) );
	}

	/**
	* Este método convierte un objeto <code>java.util.Date</code> en una
	* cadena representando la hora en formato HH:mm
	*
	* @param ad_hora La hora a convertir (en formato HH:mm)
	* @return La hora en formato HH:mm
	*/
	public static String conversionFormatoHoraABD(Date ad_hora)
	{
		StringBuffer lsb_hora = new StringBuffer();

		if(ad_hora != null)
		{
			SimpleDateFormat lsdf_sdf;
			lsdf_sdf = new SimpleDateFormat("HH:mm");
			lsdf_sdf.setLenient(false);
			lsb_hora = lsdf_sdf.format(ad_hora, lsb_hora, new FieldPosition(DateFormat.HOUR_OF_DAY0_FIELD) );
		}

		return lsb_hora.toString();
	}
	
	
	
	/**
	* Este método se encarga de formatear una hora en formato
	* hh:mm a otra en formato HH:mm
	*
	* @param ad_hora La hora a convertir
	* @return La hora en formato HH:mm
	*/
	public static String conversionFormatoHoraABD(String ad_hora)
	{
		String horaFormateada = "";
		
		if(ad_hora != null)
		{
			SimpleDateFormat lsdf_sdf;
			
			try {
				
				lsdf_sdf = new SimpleDateFormat("HH:mm");
				Date date = lsdf_sdf.parse(ad_hora);
				horaFormateada =  lsdf_sdf.format(date);
				
			} catch (Exception e) {
			
				e.printStackTrace();
			}
		}

		return horaFormateada;
	}
	
	
	
	/**
	* Este método se encarga de formatear una hora en formato
	* HH:mm a otra en formato hh:mm
	*
	* @param ad_hora La hora a convertir
	* @return La hora en formato hh:mm
	*/
	public static String conversionFormatoHoraAAp(String ad_hora)
	{
		String horaFormateada = "";
		
		if(ad_hora != null)
		{
			SimpleDateFormat lsdf_sdf;
			
			try {
				
				lsdf_sdf = new SimpleDateFormat("HH:mm");
				Date date = lsdf_sdf.parse(ad_hora);
				SimpleDateFormat nuevoFormato = new SimpleDateFormat("hh:mm a");
				
				horaFormateada =  nuevoFormato.format(date);
				
			} catch (Exception e) {
			
				e.printStackTrace();
			}
		}

		return horaFormateada;
	}
	

	/**
	* Este método convierte un objeto tipo long en una
	* cadena representando la hora en formato HH:mm
	*
	* @param ad_hora La hora a convertir (en formato HH:mm)
	* @return La hora en formato HH:mm
	*/
	public static String conversionFormatoHoraABD(long al_hora)
	{
		return conversionFormatoHoraABD(new Date(al_hora) );
	}

	/**
	* Método que convierte una hora del formato que viene de la BD
	* a formato hh:mm
	* @param hora
	*/
	public static String convertirHoraACincoCaracteres(String hora)
	{
		if(hora==null)
			return "";
		else if(hora.length()<5)
			return "";
		else
			return hora.substring(0,5);
	}

	/**
	* Método que obtiene la fecha actual del sistema
	* (en formato de la aplicación)
	* @return
	*/
	public static String getFechaActual()
	{
		return conversionFormatoFechaAAp(Utilidades.capturarFechaBD());
	}
	

	/**
	 * Obtiene el año actual
	 * @return Año en formato YYYY
	 */
	public static int getAnioActual()
	{
		return Integer.parseInt(Utilidades.capturarFechaBD().split("-")[0]);
	}

	
	/**
	* Método que obtiene la fecha actual del sistema
	* (en formato de la aplicación)
	* @return
	*/
	public static Date getFechaActualTipoBD()
	{
		SimpleDateFormat fechaTempo=new SimpleDateFormat("yyyy-MM-dd");
		try 
		{
			return fechaTempo.parse(Utilidades.capturarFechaBD());
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	* Método que obtiene la fecha actual del sistema
	* (en formato de la aplicación)
	* @return
	*/
	public static Date getFechaActualTipoBD(Connection con)
	{
		SimpleDateFormat fechaTempo=new SimpleDateFormat("yyyy-MM-dd");
		try 
		{
			return fechaTempo.parse(Utilidades.capturarFechaBD(con));
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	* Método que obtiene la fecha actual del sistema
	* (en formato de la aplicación)
	* @return
	*/
	public static String getFechaActual(Connection con)
	{
		return conversionFormatoFechaAAp(Utilidades.capturarFechaBD(con));
	}
	
	/**
	 * Metodo que retorna el año, mes o dia actual,
	 * segun el parametro enviado como String, 
	 * "dia", "mes","anio".
	 * @param tipoFecha String, parametro para identificar el tipo de fecha
	 * a retornar[año ó mes ó dia]
	 * @return int, con el año ó mes ó día
	 */
	public static int getMesAnioDiaActual (String tipoFecha)
	{	   
	   String dato = Utilidades.capturarFechaBD();
	   String[] fechaActual = dato.split("-");
	   if(tipoFecha.equals("anio"))
	       return Integer.parseInt(fechaActual[0]);
	   else if(tipoFecha.equals("mes"))
	       return Integer.parseInt(fechaActual[1]);
	   else if(tipoFecha.equals("dia"))
	       return Integer.parseInt(fechaActual[2]);
	   else
	   {
	       return -1;
	   }
	}
	
	/**
	 * Obtiene el dia mes o ano de una fecha determinada, segun el parametro enviado como String
	 * @param tipoFecha "dia", "mes", "anio"
	 * @param fecha en formato de la aplicación
	 * @return
	 */
	public static int getMesAnoDiaFecha(String tipoFecha, String fecha)
	{
	   String[] camposFecha = fecha.split("/");
	   if(tipoFecha.equals("anio"))
	       return Integer.parseInt(camposFecha[2]);
	   else if(tipoFecha.equals("mes"))
	       return Integer.parseInt(camposFecha[1]);
	   else if(tipoFecha.equals("dia"))
	       return Integer.parseInt(camposFecha[0]);
	   else
	   {
	       return -1;
	   }
	}

	/**
	* Método que obtiene la fecha actual del sistema
	* (en formato de la aplicación)
	* @return
	*/
	public static String getHoraActual()
	{
		return Utilidades.capturarHoraBD();
	}
	
	/**
	* Método que obtiene la fecha actual del sistema
	* (en formato de la aplicación)
	* @return
	*/
	public static String getHoraActual(Connection con)
	{
		return Utilidades.capturarHoraBD(con);
	}
	
	/**
	* Método que obtiene la fecha actual del sistema
	* (en formato de la aplicación)
	* @return
	*/
	public static String getHoraSegundosActual()
	{
		return Utilidades.capturarHoraSegundosBD();
	}
	
	/**
	* Método que obtiene la fecha actual del sistema
	* (en formato de la aplicación)
	* @return
	*/
	public static String getHoraSegundosActual(Connection con)
	{
		return Utilidades.capturarHoraSegundosBD(con);
	}
	
	/**
	 * Calcula la edad de la persona con base en la fecha actual
	 * @param fechaNacimiento
	 * @param fechaComparacion
	 * @return
	 */
	public static int calcularEdad(String fechaNacimiento)
	{
		String fechaActual = UtilidadFecha.getFechaActual();
		int diaComp = UtilidadFecha.getMesAnoDiaFecha("dia", fechaActual);
		int mesComp = UtilidadFecha.getMesAnoDiaFecha("mes", fechaActual);
		int anioComp = UtilidadFecha.getMesAnoDiaFecha("anio", fechaActual);

		int diaNac = UtilidadFecha.getMesAnoDiaFecha("dia", fechaNacimiento);
		int mesNac = UtilidadFecha.getMesAnoDiaFecha("mes", fechaNacimiento);
		int anioNac = UtilidadFecha.getMesAnoDiaFecha("anio", fechaNacimiento);
		
		return calcularEdad(String.valueOf(diaNac), String.valueOf(mesNac), String.valueOf(anioNac), String.valueOf(diaComp), String.valueOf(mesComp), String.valueOf(anioComp));
	}

	/**
	* Dados el dia, mes y año de nacimiento de una persona, y una fecha cualquiera
	* calcula la edad de la persona en la fecha dada, en años.
	* @param diaNacimiento cadena de texto con el dia en el cual nacio la persona
	* @param mesNacimiento cadena de texto con el mes en el cual nacio la persona
	* @param annoNacimiento cadena de texto con el año en el cual nacio la persona
	* @param diaComparacion Dia con respecto al cual se desea sacar la edad
	* @param mesComparacion Mes con respecto al cual se desea sacar la edad
	* @param annoComparacion Año con respecto al cual se desea sacar la edad
	* @return la edad de la persona, en años
	*/
	public static int calcularEdad(String diaNacimiento, String mesNacimiento, String annoNacimiento, int diaComparacion, int mesComparacion, int annoComparacion)
	{
		int diaNac=0, mesNac=0, annoNac=0;

		try
		{
			// Fecha de nacimiento de la persona
			diaNac  = Integer.parseInt(diaNacimiento.trim());
			mesNac  = Integer.parseInt(mesNacimiento.trim());
			annoNac = Integer.parseInt(annoNacimiento.trim());
		}
		catch(NumberFormatException nfe)
		{
			nfe.printStackTrace();
		}

		// Cálculo de la edad, en años.
		int edadEnAnios = annoComparacion - annoNac;

		if(mesComparacion < mesNac)
			edadEnAnios--;
		else if(mesComparacion == mesNac)
		{
			if(diaComparacion < diaNac)
				edadEnAnios--;
		}

		return edadEnAnios;
	}

	/**
	* Dados el dia, mes y año de nacimiento de una persona y una fecha cualquiera
	* calcula la edad de la persona en la fecha dada, en años.
	* @param diaNacimiento cadena de texto con el dia en el cual nacio la persona
	* @param mesNacimiento cadena de texto con el mes en el cual nacio la persona
	* @param annoNacimiento cadena de texto con el año en el cual nacio la persona
	* @param diaComparacion Dia con respecto al cual se desea sacar la edad
	* @param mesComparacion Mes con respecto al cual se desea sacar la edad
	* @param annoComparacion Año con respecto al cual se desea sacar la edad
	* @return la edad de la persona, en años
	*/
	public static int calcularEdad(String diaNacimiento, String mesNacimiento, String annoNacimiento, String diaComparacion, String mesComparacion, String annoComparacion)
	{
		return calcularEdad(diaNacimiento, mesNacimiento, annoNacimiento, Integer.parseInt(diaComparacion), Integer.parseInt(mesComparacion), Integer.parseInt(annoComparacion));
	}

	/**
	* Dados el dia, mes y año de nacimiento de una persona  y una fecha cualquiera,
	* calcula su edad en la fecha dada, con las siguientes consideraciones :
	* edad &lt; 1 mes : edad en días
	* edad &lt; 1 año : edad en meses y días
	* 1 año &lt;= edad &lt;= 5 años : edad en años y meses
	* edad &gt; 5 años : edad en años
	* @param annoNacimiento año en el cual nació la persona
	* @param mesNacimiento mes en el cual nació la persona
	* @param diaNacimiento día en el cual nació la persona
	* @param diaComparacion Dia con respecto al cual se desea sacar la edad
	* @param mesComparacion Mes con respecto al cual se desea sacar la edad
	* @param annoComparacion Año con respecto al cual se desea sacar la edad
	* @return la edad de la persona, como se explicó arriba
	*/

	public static String calcularEdadDetallada(int ai_annoNacimiento, int ai_mesNacimiento, int ai_diaNacimiento, int ai_diaComparacion, int ai_mesComparacion, int ai_annoComparacion)
	{
		Date				ld_comparacion;
		Date				ld_nacimiento;
		GregorianCalendar	lc_calendario;
		int					li_annos;
		int					li_dias;
		int					li_meses;
		String				ls_edad;
	
	//MT 6131
	   MessageResources etiqueta= MessageResources.getMessageResources("mensajes.ApplicationResources");
	   
	   	lc_calendario = new GregorianCalendar();
		lc_calendario.setLenient(true);

		/* Obterer la fecha de comparación */
		lc_calendario.clear();
		lc_calendario.set(ai_annoComparacion, ai_mesComparacion - 1, ai_diaComparacion);
		ld_comparacion = lc_calendario.getTime();

		/* Obtener la fecha de nacimiento */
		lc_calendario.clear();
		lc_calendario.set(ai_annoNacimiento, ai_mesNacimiento - 1, ai_diaNacimiento);
		ld_nacimiento = lc_calendario.getTime();

		/* Validar que la fecha de nacimiento sea válida con respecto a fecha de comparación */
		if(ld_nacimiento.after(ld_comparacion) )
			return "La persona todavía no ha nacido";

		/* Validar que la fecha de nacimiento no sea igual a la fehca de comparacion */
		if(ld_nacimiento.equals(ld_comparacion) )
			return "0";

		/* Obtener la edad en años */
		li_annos = ai_annoComparacion - ai_annoNacimiento;

		lc_calendario.clear();
		lc_calendario.set(ai_annoComparacion, ai_mesNacimiento - 1, ai_diaNacimiento);

		/* Obtener la edad en meses */
		if( (li_meses = ai_mesComparacion - ai_mesNacimiento) < 0)
			li_meses += 12;

		if(ld_comparacion.before(lc_calendario.getTime() ) )
		{
			li_annos--;

			if(li_meses == 0)
				li_meses = 12;
		}

		if(ai_diaComparacion < ai_diaNacimiento)
			li_meses--;

		/* Obtener la edad en días */
		lc_calendario.clear();
		lc_calendario.set(ai_annoNacimiento + li_annos + ( (ai_mesNacimiento + li_meses) / 12), ( (ai_mesNacimiento + li_meses) % 12)  - 1, ai_diaNacimiento);

		if(lc_calendario.get(Calendar.MONTH) + 1 == ai_mesComparacion)
			li_dias = ai_diaComparacion - ai_diaNacimiento;
		else
			li_dias = lc_calendario.getActualMaximum(Calendar.DAY_OF_MONTH) + ai_diaComparacion - lc_calendario.get(Calendar.DAY_OF_MONTH);

		/* En este punto ya sabemos la edad en años, meses y días */
		ls_edad = new String();
       //MT 6131
		if(li_annos > 0)
			ls_edad = li_annos + (li_annos > 1 ? " "+etiqueta.getMessage("prompt.anios"): " "+etiqueta.getMessage("prompt.anio"));

		if(li_annos <= 5 && li_meses > 0)
			ls_edad += (ls_edad.equals("") ? "" : " y ") + li_meses + (li_meses > 1 ? " meses" : " mes");

		if(li_annos < 1 && li_dias > 0)
			ls_edad += (ls_edad.equals("") ? "" : " y ") + li_dias + (li_dias > 1 ? " días" : " dia");

		return ls_edad;
	}
	
	/**
	* Dados el dia, mes y año de nacimiento de una persona  y una fecha cualquiera,
	* calcula su edad en la fecha dada.
	* @param annoNacimiento año en el cual nació la persona
	* @param mesNacimiento mes en el cual nació la persona
	* @param diaNacimiento día en el cual nació la persona
	* @param diaComparacion Dia con respecto al cual se desea sacar la edad
	* @param mesComparacion Mes con respecto al cual se desea sacar la edad
	* @param annoComparacion Año con respecto al cual se desea sacar la edad
	* @return la edad de la persona, como se explicó arriba
	*/

	public static String calcularEdadDetalladaCompleta(int ai_annoNacimiento, int ai_mesNacimiento, int ai_diaNacimiento, int ai_diaComparacion, int ai_mesComparacion, int ai_annoComparacion)
	{
		Date				ld_comparacion;
		Date				ld_nacimiento;
		GregorianCalendar	lc_calendario;
		int					li_annos;
		int					li_dias;
		int					li_meses;
		String				ls_edad;

		lc_calendario = new GregorianCalendar();
		lc_calendario.setLenient(true);

		/* Obterer la fecha de comparación */
		lc_calendario.clear();
		lc_calendario.set(ai_annoComparacion, ai_mesComparacion - 1, ai_diaComparacion);
		ld_comparacion = lc_calendario.getTime();

		/* Obtener la fecha de nacimiento */
		lc_calendario.clear();
		lc_calendario.set(ai_annoNacimiento, ai_mesNacimiento - 1, ai_diaNacimiento);
		ld_nacimiento = lc_calendario.getTime();

		/* Validar que la fecha de nacimiento sea válida con respecto a fecha de comparación */
		if(ld_nacimiento.after(ld_comparacion) )
			return "La persona todavía no ha nacido";

		/* Validar que la fecha de nacimiento no sea igual a la fehca de comparacion */
		if(ld_nacimiento.equals(ld_comparacion) )
			return "0";

		/* Obtener la edad en años */
		li_annos = ai_annoComparacion - ai_annoNacimiento;

		lc_calendario.clear();
		lc_calendario.set(ai_annoComparacion, ai_mesNacimiento - 1, ai_diaNacimiento);

		/* Obtener la edad en meses */
		if( (li_meses = ai_mesComparacion - ai_mesNacimiento) < 0)
			li_meses += 12;

		if(ld_comparacion.before(lc_calendario.getTime() ) )
		{
			li_annos--;

			if(li_meses == 0)
				li_meses = 12;
		}

		if(ai_diaComparacion < ai_diaNacimiento)
			li_meses--;

		/* Obtener la edad en días */
		lc_calendario.clear();
		lc_calendario.set(ai_annoNacimiento + li_annos + ( (ai_mesNacimiento + li_meses) / 12), ( (ai_mesNacimiento + li_meses) % 12)  - 1, ai_diaNacimiento);

		if(lc_calendario.get(Calendar.MONTH) + 1 == ai_mesComparacion)
			li_dias = ai_diaComparacion - ai_diaNacimiento;
		else
			li_dias = lc_calendario.getActualMaximum(Calendar.DAY_OF_MONTH) + ai_diaComparacion - lc_calendario.get(Calendar.DAY_OF_MONTH);

		/* En este punto ya sabemos la edad en años, meses y días */
		ls_edad = new String();

		if(li_annos > 0)
			ls_edad = li_annos + (li_annos > 1 ? " años" : " año");

		if(li_meses > 0)
			ls_edad += (ls_edad.equals("") ? "" : " y ") + li_meses + (li_meses > 1 ? " meses" : " mes");

		if(li_dias > 0)
			ls_edad += (ls_edad.equals("") ? "" : " y ") + li_dias + (li_dias > 1 ? " días" : " dia");

		return ls_edad;
	}
	
	/**
	 * Método que calcula la fecha de un paciente desde la fecha de nacimiento
	 * hasta la fecha de apertura de la cuenta y retorna un formato:
	 *  1=> Años
	 * 	2=> Meses
	 * 	3=> Días
	 * @param fecha nacimiento
	 * @param fecha apertura
	 * @return InfoDatos
	 */
	public static InfoDatos calcularEdadFormato(String fechaNacimiento, String fechaApertura) 
	{
		String fechaN[]=UtilidadFecha.conversionFormatoFechaAAp(fechaNacimiento).split("/");
		String fechaA[]=UtilidadFecha.conversionFormatoFechaAAp(fechaApertura).split("/");
		String formato;
		int edad;
		
		formato = "1";
		edad = UtilidadFecha.calcularEdad(fechaN[0],fechaN[1],fechaN[2],Integer.parseInt(fechaA[0]),Integer.parseInt(fechaA[1]),Integer.parseInt(fechaA[2]));
		
		//Si no es en años se calcula meses
		if(edad<=0)
		{
			formato = "2";
			edad = UtilidadFecha.numeroMesesEntreFechas(UtilidadFecha.conversionFormatoFechaAAp(fechaNacimiento),UtilidadFecha.conversionFormatoFechaAAp(fechaApertura),false);
			
			//Si no es meses se calcula días
			if(edad<=0)
			{
				formato = "3";
				edad = UtilidadFecha.numeroDiasEntreFechas(UtilidadFecha.conversionFormatoFechaAAp(fechaNacimiento),UtilidadFecha.conversionFormatoFechaAAp(fechaApertura));
			}
		}
		
		
		//codigo=> edad, value=>formato
		return new InfoDatos(edad,formato);
	}
	
	/**
	* Dados el dia, mes y año de nacimiento de una persona  y una fecha cualquiera,
	* calcula su edad en la fecha dada.
	* Y retorna un vector con lo siguiente [anios,meses,dias]
	* 
	* Las fechas deben estar en formato (yyyy-mm-dd)
	* @param Fecha Inicial
	* @param FechaFinal
	* @return la edad de la persona, como se explicó arriba
	*/

	public static int[] calcularVectorEdad(String fechaInicial, String fechaFinal)
	{
		String[] fnTemp=fechaInicial.split("-");
		String[] fvTemp=fechaFinal.split("-");
		return calcularVectorEdad(Integer.parseInt(fnTemp[0]), Integer.parseInt(fnTemp[1]), Integer.parseInt(fnTemp[2]), Integer.parseInt(fvTemp[2]), Integer.parseInt(fvTemp[1]), Integer.parseInt(fvTemp[0]));
		
	}
	
	/**
	* Dados el dia, mes y año de nacimiento de una persona  y una fecha cualquiera,
	* calcula su edad en la fecha dada.
	* Y retorna un vector con lo siguiente [anios,meses,dias]
	* @param annoNacimiento año en el cual nació la persona
	* @param mesNacimiento mes en el cual nació la persona
	* @param diaNacimiento día en el cual nació la persona
	* @param diaComparacion Dia con respecto al cual se desea sacar la edad
	* @param mesComparacion Mes con respecto al cual se desea sacar la edad
	* @param annoComparacion Año con respecto al cual se desea sacar la edad
	* @return la edad de la persona, como se explicó arriba
	*/

	public static int[] calcularVectorEdad(int ai_annoNacimiento, int ai_mesNacimiento, int ai_diaNacimiento, int ai_diaComparacion, int ai_mesComparacion, int ai_annoComparacion)
	{
		Date				ld_comparacion;
		Date				ld_nacimiento;
		GregorianCalendar	lc_calendario;
		int					li_annos;
		int					li_dias;
		int					li_meses;
		
		int[] edad={0,0,0};

		lc_calendario = new GregorianCalendar();
		lc_calendario.setLenient(true);

		/* Obterer la fecha de comparación */
		lc_calendario.clear();
		lc_calendario.set(ai_annoComparacion, ai_mesComparacion - 1, ai_diaComparacion);
		ld_comparacion = lc_calendario.getTime();

		/* Obtener la fecha de nacimiento */
		lc_calendario.clear();
		lc_calendario.set(ai_annoNacimiento, ai_mesNacimiento - 1, ai_diaNacimiento);
		ld_nacimiento = lc_calendario.getTime();

		/* Validar que la fecha de nacimiento sea válida con respecto a fecha de comparación */
		if(ld_nacimiento.after(ld_comparacion) )
			return edad;

		/* Validar que la fecha de nacimiento no sea igual a la fehca de comparacion */
		if(ld_nacimiento.equals(ld_comparacion) )
			return edad;

		/* Obtener la edad en años */
		li_annos = ai_annoComparacion - ai_annoNacimiento;

		lc_calendario.clear();
		lc_calendario.set(ai_annoComparacion, ai_mesNacimiento - 1, ai_diaNacimiento);

		/* Obtener la edad en meses */
		if( (li_meses = ai_mesComparacion - ai_mesNacimiento) < 0)
			li_meses += 12;

		if(ld_comparacion.before(lc_calendario.getTime() ) )
		{
			li_annos--;

			if(li_meses == 0)
				li_meses = 12;
		}

		if(ai_diaComparacion < ai_diaNacimiento)
			li_meses--;

		/* Obtener la edad en días */
		lc_calendario.clear();
		lc_calendario.set(ai_annoNacimiento + li_annos + ( (ai_mesNacimiento + li_meses) / 12), ( (ai_mesNacimiento + li_meses) % 12)  - 1, ai_diaNacimiento);

		if(lc_calendario.get(Calendar.MONTH) + 1 == ai_mesComparacion)
			li_dias = ai_diaComparacion - ai_diaNacimiento;
		else
			li_dias = lc_calendario.getActualMaximum(Calendar.DAY_OF_MONTH) + ai_diaComparacion - lc_calendario.get(Calendar.DAY_OF_MONTH);

		edad[0]=li_annos;
		edad[1]=li_meses;
		edad[2]=li_dias;
		return edad;
	}

	/**
	* Dados el dia, mes y año de nacimiento de una persona  y una fecha cualquiera,
	* calcula su edad en la fecha dada, con las siguientes consideraciones :
	* edad &lt; 1 mes : edad en días
	* edad &lt; 1 año : edad en meses y días
	* 1 año &lt;= edad &lt;= 5 años : edad en años y meses
	* edad &gt; 5 años : edad en años
	* @param annoNacimiento año en el cual nació la persona
	* @param mesNacimiento mes en el cual nació la persona
	* @param diaNacimiento día en el cual nació la persona
	* @param diaComparacion Dia con respecto al cual se desea sacar la edad
	* @param mesComparacion Mes con respecto al cual se desea sacar la edad
	* @param annoComparacion Año con respecto al cual se desea sacar la edad
	* @return la edad de la persona, como se explicó arriba
	*/
	public static String calcularEdadDetallada(int annoNacimiento, int mesNacimiento, int diaNacimiento, String diaComparacion, String mesComparacion, String annoComparacion)
	{
		return calcularEdadDetallada(annoNacimiento, mesNacimiento, diaNacimiento, Integer.parseInt(diaComparacion), Integer.parseInt(mesComparacion), Integer.parseInt(annoComparacion));
	}

	/**
	* Este método calcula el número de dias que hay entre una fecha
	* especificadas (La primera debe ser la mayor, en caso contrario
	* retorna -1)
	*
	* @param dia Día de la fecha especificada
	* @param mes Mes de la fecha especificada
	* @param anno Año de la fecha especificada
	* @return
	*/
	public static int numeroDiasEntreFechas(int dia1, int mes1, int anno1, int dia2, int mes2, int anno2)
	{
		GregorianCalendar calendarioActual = new GregorianCalendar();
		// Los meses en Java empiezan en 0

		int mesJava1 = mes1 - 1,mesJava2 = mes2 - 1, edadEnDias=0;
		// Fecha1

		calendarioActual.clear();
		calendarioActual.set(anno1, mesJava1, dia1);
		Date fechaActual = calendarioActual.getTime();

		//Fecha Dada
		GregorianCalendar calendarioFechaDada = new GregorianCalendar();
		calendarioFechaDada.clear();
		calendarioFechaDada.set(anno2, mesJava2, dia2);
		Date fechaDada=calendarioFechaDada.getTime();

		if(fechaDada.compareTo(fechaActual)>0)
		{
			//Esto pasa si la primera fecha es mayor que la segunda
			return -1;
		}

		while(fechaActual.compareTo(fechaDada)>0)
		{
			//Aumentamos el número de dias
			edadEnDias++;
			calendarioFechaDada.add(Calendar.DAY_OF_MONTH, 1);
			fechaDada=calendarioFechaDada.getTime();
		}

		return edadEnDias;
	}
	
	/**
	 * Este método calcula el número de dias que hay entre una fecha
	 * especificadas (La primera debe ser la mayor, en caso contrario
	 * retorna -1)
	 * @param fechaMayor string con la fecha mayor en formato de la aplicacion
	 * @param fechaMenor string con la fecha menor en formato de la aplicacion
	 * @return el numero de días entre las dos fechas
	 */
	public static int numeroDiasEntreFechas(String fechaMenor, String fechaMayor)
	{
		if(validarFecha(fechaMenor)&&validarFecha(fechaMayor))
		{
	        String[] arregloFechaMayor = (fechaMayor).split("/");
			
		    int diaMayor= Integer.parseInt(arregloFechaMayor[0]);
		    int mesMayor= Integer.parseInt(arregloFechaMayor[1]);
		    int anioMayor= Integer.parseInt(arregloFechaMayor[2]);
		    
		    String[] arregloFechaMenor = (fechaMenor).split("/");
			
		    int diaMenor= Integer.parseInt(arregloFechaMenor[0]);
		    int mesMenor= Integer.parseInt(arregloFechaMenor[1]);
		    int anioMenor= Integer.parseInt(arregloFechaMenor[2]);
		     
		    return numeroDiasEntreFechas(diaMayor,mesMayor,anioMayor,diaMenor,mesMenor,anioMenor);
		}
		else
			return 0;
	}

	/**
	* Valida el formato de una cadena representando una fecha. Este formato debe ser
	* dd/mm/aaaa
	* @param as_fecha Cadena representado una fecha
	*/
	public static boolean validarFecha(String as_fecha)
	{
		return UtilidadFecha.esFechaValidaSegunAp(as_fecha);
	}
	
	/**
	* Valida el formato de una cadena representando una fecha. Este formato debe ser
	* mm/aaaa
	* @param as_fecha Cadena representado una fecha
	*/
	public static boolean validarFechaMMYYYY(String as_fecha)
	{	   
	    return UtilidadFecha.esFechaValidaSegunApMMYYYY(as_fecha);
	}

	/**
	 * Este método valida que una fecha dada sea válida, tomando incluso los
	 * casos en años biciestos.
	 * @param anio Entero con el año de la fecha a validar
	 * @param mes Entero con el mes de la fecha a validar
	 * @param dia Entero con el día de la fecha a validar
	 * @return un objeto <code>RespuestaValidacion</code>
	 * que dice si la fecha es válida y en caso que no lo
	 * sea, se explica porque.
	 */
	public static RespuestaValidacion validacionFecha(int anio, int mes, int dia)
	{
		//Primero revisamos que tanto el día como el
		//mes sean positivos
		if (dia <1||mes<1)
		{
			return new RespuestaValidacion ("Fecha Invalida, Recuerde que tanto el d&iacute;a como el mes deben ser positivos", false);
		}
		//Primero revisamos los casos especiales que involucran los
		//años
		if (anio%400==0)
		{
			if (mes==2&&dia<=29)
			{
				return new RespuestaValidacion ("La fecha es válida", true);
			}
			else if (mes==2&&dia>29)
			{
				return new RespuestaValidacion ("Fecha Inválida. El mes de Febrero para un año biciesto solo tiene 29 dias", false);
			}
		}

		if (anio%4==0&&!(anio%100==0))
		{
			if (mes==2&&dia<=29)
			{
				return new RespuestaValidacion ("La fecha es válida", true);
			}
			else if (mes==2&&dia>29)
			{
				return new RespuestaValidacion ("Fecha Inválida. El mes de Febrero para un año biciesto solo tiene 29 dias", false);
			}
		}

		//Si llegamos a este punto es porque el caso que tenemos no
		//corresponde a año biciesto mes de febrero dia 29, así que revisamos el resto
		//de casos
		if (mes==1)
		{
			if (dia>31)
				return new RespuestaValidacion ("Fecha Inv&aacute;lida. El mes de Enero solo tiene 31 dias", false);
			else
				return new RespuestaValidacion ("La fecha es v&aacute;lida", true);
		}
		else if (mes==2)
		{
			if (dia>28)
				return new RespuestaValidacion ("Fecha Inv&aacute;lida. El mes de Febrero para un año no bisiesto solo tiene 28 dias", false);
			else
				return new RespuestaValidacion ("La fecha es v&aacute;lida", true);
		}
		else if (mes==3)
		{
			if (dia>31)
				return new RespuestaValidacion ("Fecha Inv&aacute;lida. El mes de Marzo solo tiene 31 dias", false);
			else
				return new RespuestaValidacion ("La fecha es v&aacute;lida", true);
		}
		else if (mes==4)
		{
			if (dia>30)
				return new RespuestaValidacion ("Fecha Inv&aacute;lida. El mes de Abril solo tiene 30 dias", false);
			else
				return new RespuestaValidacion ("La fecha es v&aacute;lida", true);
		}
		else if (mes==5)
		{
			if (dia>31)
				return new RespuestaValidacion ("Fecha Inv&aacute;lida. El mes de Mayo solo tiene 31 dias", false);
			else
				return new RespuestaValidacion ("La fecha es v&aacute;lida", true);
		}
		else if (mes==6)
		{
			if (dia>30)
				return new RespuestaValidacion ("Fecha Inv&aacute;lida. El mes de Junio solo tiene 30 dias", false);
			else
				return new RespuestaValidacion ("La fecha es v&aacute;lida", true);
		}
		else if (mes==7)
		{
			if (dia>31)
				return new RespuestaValidacion ("Fecha Inv&aacute;lida. El mes de Julio solo tiene 31 dias", false);
			else
				return new RespuestaValidacion ("La fecha es v&aacute;lida", true);
		}
		else if (mes==8)
		{
			if (dia>31)
				return new RespuestaValidacion ("Fecha Inv&aacute;lida. El mes de Agosto solo tiene 31 dias", false);
			else
				return new RespuestaValidacion ("La fecha es v&aacute;lida", true);
		}
		else if (mes==9)
		{
			if (dia>30)
				return new RespuestaValidacion ("Fecha Inv&aacute;lida. El mes de Septiembre solo tiene 30 dias", false);
			else
				return new RespuestaValidacion ("La fecha es v&aacute;lida", true);
		}
		else if (mes==10)
		{
			if (dia>31)
				return new RespuestaValidacion ("Fecha Inv&aacute;lida. El mes de Octubre solo tiene 31 dias", false);
			else
				return new RespuestaValidacion ("La fecha es v&aacute;lida", true);
		}
		else if (mes==11)
		{
			if (dia>30)
			{
				return new RespuestaValidacion ("Fecha Inv&aacute;lida. El mes de Noviembre solo tiene 30 dias", false);
			}
			else
			{
				return new RespuestaValidacion ("La fecha es v&aacute;lida", true);
			}
		}
		else if (mes==12)
		{
			if (dia>31)
				return new RespuestaValidacion ("Fecha Inv&aacute;lida. El mes de Diciembre solo tiene 31 dias", false);
			else
				return new RespuestaValidacion ("La fecha es v&aacute;lida", true);
		}

		//Llegados a este punto sabemos que no esta en un mes legal

		return new RespuestaValidacion ("Fecha Invalida. Revise el mes que acaba de ingresar", false);
	}

	/**
	 * Este método valida que un String contenga la hora en formato
	 * hh:mm (militar). Es claro que también revisa que los valores
	 * de hora y minuto.
	 * @param hora Un String con la hora
	 * @return un objeto <code>RespuestaValidacion</code>
	 * que dice si la hora es válida y en caso que no lo
	 * sea, se explica porque.
	 */
	public static RespuestaValidacion validacionHora(String hora)
	{
		RespuestaValidacion respuesta= new RespuestaValidacion ("No hay problema para crear este ingreso", true);
		String nuevaHora=hora;
		try
		{
			StringTokenizer aPartir= new StringTokenizer(hora, ":");
			String horaReal, minutoReal;
			int horaRealInt, minutoRealInt;

			horaReal=aPartir.nextToken();
			if(horaReal.length()==1)
			{
				horaReal='0'+horaReal;
			}
			minutoReal=aPartir.nextToken();
			if(minutoReal.length()<2)
			{
				return new RespuestaValidacion ("Revise el formato de la hora, no es valido", false);
			}
			horaRealInt=Integer.parseInt(horaReal);
			minutoRealInt=Integer.parseInt(minutoReal);
			nuevaHora=horaReal+":"+minutoReal;

			if ( minutoRealInt> 59 ||minutoRealInt < 0)
				return new RespuestaValidacion ("Recuerde que los minutos solo pueden estar entre 0 y 59", false);
			if ( horaRealInt> 23 ||horaRealInt < 0)
				return new RespuestaValidacion ("Recuerde que la hora puede estar solo entre 0 y 23 (Hora Militar)", false);
		}
		catch (Exception e)
		{
			return new RespuestaValidacion ("Revise el formato de la hora, no es valido", false);
		}

		respuesta.nuevoValor=nuevaHora;
		return respuesta;
	}

	/**
	 * Dice si la fecha/hora 1 es mayor o igual a la fecha/hora 2 o no.
	 * @return boolean, true si la fecha/hora 1 es mayor o igual a la fecha/hora
	 * 2, flase de lo contrario.
	 */
	public static ResultadoBoolean compararFechas(String fecha1, String hora1, String fecha2, String hora2)
	{
		Date fechaD2 = null;
		Date fechaD1 = null;
		Date fechaHoraD2 = null;
		Date fechaHoraD1 = null;

		final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy:HH:mm");

		try
		{
			/**
			* Tipo Modificacion: Segun incidencia 5721
			* Autor: Alejandro Aguirre Luna
			* usuario: aleagulu
			* Fecha: 18/01/2013
			**/
			/*Crear variable de tipo RespuestaValidacion para validar mediante el metodo validacionHora la hora de evolución*/
			RespuestaValidacion ValidarHoraEvolucion = validacionHora(hora2);
			/*El estado puedoSeguir devuelve false en caso de encontrar en la hora un formato inadecueado*/
			if(!ValidarHoraEvolucion.puedoSeguir){
				/*Lanzar la exception NumberFormatException pasando como argumento el mensaje de error reportado por ValidarHoraEvolucion*/
				throw new NumberFormatException(ValidarHoraEvolucion.textoRespuesta);
			}
			fechaD2 = dateFormatter.parse(fecha2);
			fechaD1 = dateFormatter.parse(fecha1);
			fechaHoraD2 = dateTimeFormatter.parse(fecha2 + ":" + hora2);
			fechaHoraD1 = dateTimeFormatter.parse(fecha1 + ":" + hora1);
		}
		catch (java.text.ParseException e)
		{
			return new ResultadoBoolean(false, "Problemas en el método de comparar fechas, no se pudieron poner en el formato");
		}
		/*Capturar la exception NumberFormatException generada al encontrar un formato de hora invalido*/
		catch(NumberFormatException nfe){
			/*Retornar el mensaje a la vista almacenado en la excepciòn generada*/
			return new ResultadoBoolean(false,nfe.getMessage());
		}
		
		if( fechaD1.compareTo(fechaD2) < 0 )
		{
			return new ResultadoBoolean(false, "La fecha y hora de la evolucion debe ser menor/igual a la fecha y hora actual.");
		}

		else
		if( fechaHoraD1.compareTo(fechaHoraD2) < 0 )
		{
			return new ResultadoBoolean(false, "La fecha y hora de la evolucion debe ser menor/igual a la fecha y hora actual.");
		}
		
		
		return new ResultadoBoolean(true);
	}
	
	
	/**
	 * Dice si la fecha/hora 1 es menor o igual a la fecha/hora 2 o no.
	 * @return boolean, true si la fecha/hora 1 es menor o igual a la fecha/hora
	 * 2, flase de lo contrario.
	 */
	public static ResultadoBoolean compararFechasMenorOIgual(String fecha1, String hora1, String fecha2, String hora2)
	{
		Date fechaD2 = null;
		Date fechaD1 = null;
		Date fechaHoraD2 = null;
		Date fechaHoraD1 = null;

		final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy:HH:mm");

		try
		{
			fechaD2 = dateFormatter.parse(fecha2);
			fechaD1 = dateFormatter.parse(fecha1);
			fechaHoraD2 = dateTimeFormatter.parse(fecha2 + ":" + hora2);
			fechaHoraD1 = dateTimeFormatter.parse(fecha1 + ":" + hora1);
		}
		catch (java.text.ParseException e)
		{
			return new ResultadoBoolean(false, "Problemas en el método de comparar fechas menor o igual, no se pudieron poner en el formato");
		}

		if( fechaHoraD1.compareTo(fechaHoraD2) <= 0 )
		{
			return new ResultadoBoolean(true);
		}

		return new ResultadoBoolean(false);
	}
	
	
	/**
	 * Método que revisa si una fecha es válida utilizando los métodos
	 * de la aplicación
	 * 
	 * @param fechaARevisar
	 * @return
	 */
	public static boolean esFechaValidaSegunAp (String fechaARevisar)
	{
		if (fechaARevisar==null)
		{
			return false;
		}
		// Fecha actual y patrón de fecha a utilizar en las validaciones
		final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		try 
		{
			dateFormatter.parse(fechaARevisar);
			//Si pasa esta prueba, solo revisamos que el tamaño de mes y año
			//sea el esperado
			String arregloTemp[]=fechaARevisar.split("/");
			if (arregloTemp[0].length()!=2||arregloTemp[1].length()!=2||arregloTemp[2].length()!=4)
			{
				return false;
			}
			RespuestaValidacion respuesta=validacionFecha(Integer.parseInt(arregloTemp[2]), Integer.parseInt(arregloTemp[1]), Integer.parseInt(arregloTemp[0]));
			return respuesta.puedoSeguir;
		}	
		catch (java.text.ParseException e) 
		{
			return false;
		}
		catch (Exception e) 
		{
			return false;
		}
	}
	
	/**
	 * Método que revisa si una fecha es válida utilizando los métodos
	 * de la aplicación
	 * 
	 * @param fechaARevisar
	 * @return
	 */
	public static boolean esFechaValidaSegunApMMYYYY (String fechaARevisar)
	{	    
		if (fechaARevisar==null)
		{
			return false;
		}
		// Fecha actual y patrón de fecha a utilizar en las validaciones
		final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/yyyy");
		try 
		{		    
		    dateFormatter.parse(fechaARevisar);
			//Si pasa esta prueba, solo revisamos que el tamaño de mes y año
			//sea el esperado
			String arregloTemp[]=fechaARevisar.split("/");			
			if (arregloTemp[0].length()!=2||arregloTemp[1].length()!=4)
			{
				return false;				
			}			
			RespuestaValidacion respuesta=validacionFecha(Integer.parseInt(arregloTemp[1]), Integer.parseInt(arregloTemp[0]), 15);
			
			return respuesta.puedoSeguir;
		}	
		catch (java.text.ParseException e) 
		{
			return false;
		}
		catch (Exception e) 
		{
			return false;
		}
	}
	
//	
	/**
	 * Determina si una hora es menor que otra de referencia. Las horas deben estar en formato hh:mm,
	 *  
	 * @param hora = horaARevisar
	 *	@param hora1 = comparación 
	 * @return true cuando es <,
	 * 				 false cuando es >=	
	 */
	public static boolean esHoraMenorQueOtraReferencia(String hora, String hora1){
	
		String[] codigoNombre = hora1.split(":");
		String[] codigoNombre1 = hora.split(":");
		
		if(Integer.parseInt(codigoNombre1[0]) < Integer.parseInt(codigoNombre[0]))
			return true;
		else 
			if(Integer.parseInt(codigoNombre1[0]) == Integer.parseInt(codigoNombre[0]))
			{
				if(Integer.parseInt(codigoNombre1[1]) < Integer.parseInt(codigoNombre[1]))
					return true;
				else
					return false;
				
			}	
		return false;	 
	}
	
	
	/**
	 * Determina si una hora es menor igual que otra de referencia. Las horas deben estar en formato hh:mm,
	 *  
	 * @param hora = horaARevisar
	 *	@param hora1 = comparación 
	 * @return true cuando es <,
	 * 				 false cuando es >=	
	 */
	public static boolean esHoraMenorIgualQueOtraReferencia(String hora, String hora1){
	
		String[] codigoNombre = hora1.split(":");
		String[] codigoNombre1 = hora.split(":");
		
		if(Integer.parseInt(codigoNombre1[0]) < Integer.parseInt(codigoNombre[0]))
			return true;
		else 
			if(Integer.parseInt(codigoNombre1[0]) == Integer.parseInt(codigoNombre[0]))
			{
				if(Integer.parseInt(codigoNombre1[1]) <= Integer.parseInt(codigoNombre[1]))
					return true;
				else
					return false;
				
			}	
		return false;	 
	}
	
	
	
	/**
	 * Determina si una fecha es menor que otra de referencia. Las fechas deben estar en formato dd/mm/aaaa,
	 *  
	 * @param fecha = fechaARevisar
	 *	@param fecha1 = comparación 
	 * @return true cuando es <,
	 * 				 false cuando es >=	
	 */
	public static boolean esFechaMenorQueOtraReferencia(String fecha, String fecha1)
	{
		String[] codigoNombre = fecha1.split("/", 3);
		String[] codigoNombre1 = fecha.split("/", 3);
		
		
		if(Integer.parseInt(codigoNombre1[2]) < Integer.parseInt(codigoNombre[2]))
			return true;
		else if(Integer.parseInt(codigoNombre1[2]) == Integer.parseInt(codigoNombre[2]))
		{
			if(Integer.parseInt(codigoNombre1[1]) < Integer.parseInt(codigoNombre[1]))
			return true;
			else if(Integer.parseInt(codigoNombre1[1]) == Integer.parseInt(codigoNombre[1]))
			{		
				if(Integer.parseInt(codigoNombre1[0]) < Integer.parseInt(codigoNombre[0]))
					return true;
				else if(Integer.parseInt(codigoNombre1[0]) == Integer.parseInt(codigoNombre[0]))	
					return false;
			}
		}	
		return false;	 
	}
	
	/**
	 * Determina si una fecha es menor que otra de referencia. Las fechas deben estar en formato dd/mm/aaaa,
	 *  
	 * @param fecha = fechaARevisar
	 *	@param fecha1 = comparación 
	 * @return true cuando es MenorIgual False cueando es Mayor
	 * @author artotor
	 */
	public static boolean esFechaMenorIgualQueOtraReferencia(String fecha, String fecha1)
	{
		String[] codigoNombre = fecha1.split("/", 3);
		String[] codigoNombre1 = fecha.split("/", 3);
		
		if(Integer.parseInt(codigoNombre1[2]) < Integer.parseInt(codigoNombre[2]))
			return true;
		else if(Integer.parseInt(codigoNombre1[2]) == Integer.parseInt(codigoNombre[2]))
		{
			if(Integer.parseInt(codigoNombre1[1]) < Integer.parseInt(codigoNombre[1]))
			return true;
			else if(Integer.parseInt(codigoNombre1[1]) == Integer.parseInt(codigoNombre[1]))
			{		
				if(Integer.parseInt(codigoNombre1[0]) <= Integer.parseInt(codigoNombre[0]))
					return true;
			}
		}	
		return false;	 
	}
	
	/**
	 * Funci&oacute;n que calcula la fecha de nacimiento (aproximada) según la edad
	 * Nota * esta función requiere de una mejor perfección pues soporta
	 *solo un número específico de meses (hasta 48 meses = 4 años), si se pasa de este
	 *valor, la fecha de nacimiento será calculada de acuerdo a los años
	 * @param tipo (1 => en años, 0 => en meses)
	 * @param edad
	 * @return fecha de nacimiento
	 */
	public static String calcularFechaNacimiento(int tipo,int edad){
		//es necesario validar antes que la edad sea positiv
		String fechaActual=UtilidadFecha.getFechaActual();
		String[] fechaSplit=fechaActual.split("/");
		int mesActual=Integer.parseInt(fechaSplit[1]);
		String fecha="";

		//si la edad es en meses
		int meses;
		int residuo=0;
		String mesesStr="";
		if(tipo==0){
			if(mesActual>edad){
				meses=mesActual-edad;
				if(meses<10)
					mesesStr="0"+meses;
				fecha=fechaSplit[0]+"/"+mesesStr+"/"+fechaSplit[2];
			}
			else
			{
				if(edad<(12+mesActual)){
					meses=mesActual-edad+12;
					if(meses<10)
						mesesStr="0"+meses;
					fecha=fechaSplit[0]+"/"+mesesStr+"/"+(Integer.parseInt(fechaSplit[2])-1);
				}
				else if(edad<(24+mesActual)){
					meses=mesActual-edad+24;
					if(meses<10)
						mesesStr="0"+meses;
					fecha=fechaSplit[0]+"/"+mesesStr+"/"+(Integer.parseInt(fechaSplit[2])-2);
				}
				
				else if(edad<(36+mesActual)){
					meses=mesActual-edad+36;
					if(meses<10)
						mesesStr="0"+meses;
					fecha=fechaSplit[0]+"/"+mesesStr+"/"+(Integer.parseInt(fechaSplit[2])-3);
				}
				else
				{
					//se busca una aproximación en años para la edad
					residuo=edad%12;
					edad=edad/12;
					if(residuo>=5)
						edad++;
					tipo=1;
				}
			}
		}
		if(tipo==1){
			fecha=fechaSplit[0]+"/"+fechaSplit[1]+"/"+(Integer.parseInt(fechaSplit[2])-edad);
		}
		return fecha;
	}
	
	/**
	 * Método para obtener el nombre el mes ingresando el número de mes
	 * @param mes
	 * @return
	 */
	public static String obtenerNombreMes(int mes){
		String nombre="";
		
		switch(mes)
		{
			case 1:
				nombre="Enero";
				break;
			case 2:
				nombre="Febrero";
				break;
			case 3:
				nombre="Marzo";
				break;
			case 4:
				nombre= "Abril";
				break;
			case 5:
				nombre= "Mayo";
				break;
			case 6:
				nombre= "Junio";
				break;
			case 7:
				nombre= "Julio";
				break;
			case 8:
				nombre= "Agosto";
				break;
			case 9:
				nombre= "Septiembre";
				break;
			case 10:
				nombre= "Octubre";
				break;
			case 11:
				nombre= "Noviembre";
				break;
			case 12:
				nombre= "Diciembre";
				break;
		}
		return nombre;
	}
	
	
	/**
	 * Metodo para obtener el nombre
	 * del día de la semana de acuerdo a una fecha dada en formato
	 * de la aplicacion
	 * @param fecha
	 * @return
	 */
	public static String obtenerNombreDia(String fecha)
	{
	    int diaB=3;
	    int mesB=1;
	    int anioB=2005;
	    
	    String nombreDia="";
	   
		if(fecha != null && !fecha.equals("") )
		{
		    String[] arregloFecha = (fecha).split("/");
			
		    int dia= Integer.parseInt(arregloFecha[0]);
		    int mes= Integer.parseInt(arregloFecha[1]);
		    int anio= Integer.parseInt(arregloFecha[2]);
		    
		    int numeroDias=numeroDiasEntreFechas(dia,mes,anio,diaB,mesB,anioB);
		   
		    int diaSemana=numeroDias%7; 
		   
		    switch(diaSemana)
			{
				case 0:
				    nombreDia="Lunes";
				    break;  
				case 1:
				    nombreDia="Martes";
				    break;
				case 2:
				    nombreDia="Miercoles";
				    break;
				case 3:
				    nombreDia= "Jueves";
				    break;
				case 4:
				    nombreDia= "Viernes";
				    break;
				case 5:
				    nombreDia= "Sabado";
				    break;
				case 6:
				    nombreDia= "Domingo";
				    break;
			}
		}
		else
			nombreDia = "";

			    
	    return nombreDia;
	    
	}
	
	
	/**
	 * Metodo que retorna true en caso de que la fecha de comparación se encuentre
	 * entre los rangos de las dos fechas específicadas o sea igual a alguna de ellas,
	 * la fecha de Inicio debe ser menor que la fecha de fin, en caso que no lo 
	 * sea retona false.
	 * las fechas que ingresan deben estar en el formato de la aplicación.
	 * @param fechaInicio
	 * @param fechaFin
	 * @param fechaCompara
	 * @return
	 */
	public static boolean validarFechaRango(String fechaInicio, String fechaFin, String fechaCompara)
	{
	    int comparaI=0;
	    int comparaF=0;
	    
	    String fechaInicioB=conversionFormatoFechaABD(fechaInicio);
	    String fechaFinB=conversionFormatoFechaABD(fechaFin);
	    String fechaComparaB=conversionFormatoFechaABD(fechaCompara);
	    
	    	    	    
	    boolean esta=false;
	    
	    int fechaMayor=fechaInicioB.compareTo(fechaFinB);
	    
	    
	    
	    if (!fechaInicio.equals("") && !fechaFin.equals("") && !fechaCompara.equals(""))
	    {
	        if (fechaMayor<=0)
	        {
	            comparaI=fechaInicioB.compareTo(fechaComparaB);
	            comparaF=fechaFinB.compareTo(fechaComparaB);
	           
	            if (comparaI<=0 && comparaF>=0)
	                esta=true;
	        }
	    }   
	    
	    return esta;
	}
	
	
	/**
	 * Metodo que consulta el número del día de mes, a partir de una fecha dada en el 
	 * formato de la aplicación
	 * @param fecha
	 * @return
	 */
	public static int diaMes(String fecha)
	{
	    int dia=0;
	       
	    if(fecha != null && !fecha.equals("") )
		{
		    String[] arregloFecha = (fecha).split("/");
			
		    dia= Integer.parseInt(arregloFecha[0]);
		    
		}
	    
	    return dia;
	}
	
	
	/**
	 * Metodo que retorna el número del dia de la semana al que
	 * corresponde una fecha, retorna -1 si lo entra ninguna fecha o se fecha
	 * es igual a null, la fecha debe entrar en el formato de la aplicación
	 * 0 --> Lunes ... 6 --> Domingo 
	 * @param fecha
	 * @return
	 */
	public static int obtenerNumeroDiaSeman(String fecha)
	{
	    int diaB=3;
	    int mesB=1;
	    int anioB=2005;
	    int diaSemana=-1;
	   
	   
		if(fecha != null && !fecha.equals("") )
		{
		    String[] arregloFecha = (fecha).split("/");
			
		    int dia= Integer.parseInt(arregloFecha[0]);
		    int mes= Integer.parseInt(arregloFecha[1]);
		    int anio= Integer.parseInt(arregloFecha[2]);
		    
		    int numeroDias=numeroDiasEntreFechas(dia,mes,anio,diaB,mesB,anioB);
		   
		    diaSemana=numeroDias%7; 
		    
		    
		}
		return diaSemana;
	    
	}
	
	/**
	 * M&eacute;todo para obtener la fecha anterior o posterior de una fecha de referencia
	 * partiendo de un número de días de distancia
	 * @param dias
	 * @param fechaReferencia
	 * @param posterior => datos True/false que indica si se calcula fecha posterior
	 * o anterior a la fecha de referencia
	 * @return fecha Anterior o Posterior a N días de la fecha Referencia
	 */
	public static String calcularFechaSobreFechaReferencia(int dias,String fechaReferencia,boolean posterior)
	{
		String fechaUso=conversionFormatoFechaAAp(fechaReferencia);
		if(validarFecha(fechaUso))
		{
			String vector[]=fechaUso.split("/");
			GregorianCalendar calendar=new GregorianCalendar(Integer.parseInt(vector[2]),Integer.parseInt(vector[1])-1,Integer.parseInt(vector[0]));
			if(posterior)
				calendar.add(Calendar.DAY_OF_YEAR,dias);
			else
				calendar.add(Calendar.DAY_OF_YEAR,-dias);
			
			return conversionFormatoFechaAAp(calendar.getTime());
		}
		else
		{
			return fechaReferencia;
		}
	}

	/**
	 * Método que suma o resta días de la semana
	 * @param fechaActual
	 * @param numero de días con signo
	 * @param estaEnFormatoBD Indica si la fecha recibida como parámetro se encuentra o no en el formato de la BD
	 * @return Fecha modificada
	 */
	public static String incrementarDiasAFecha(String fechaActual, int incremento, boolean estaEnFormatoBD)
	{
		GregorianCalendar calendario=new GregorianCalendar();
		int dia=0;
		int mes=0;
		int anio=0;
		if(estaEnFormatoBD)
		{
			dia=Integer.parseInt(fechaActual.split("-")[2]);
			mes=Integer.parseInt(fechaActual.split("-")[1]);
			anio=Integer.parseInt(fechaActual.split("-")[0]);
		}
		else
		{
			dia=Integer.parseInt(fechaActual.split("/")[0]);
			mes=Integer.parseInt(fechaActual.split("/")[1]);
			anio=Integer.parseInt(fechaActual.split("/")[2]);
		}
		calendario.set(anio, mes-1, dia);
		calendario.add(GregorianCalendar.DATE, incremento);
		dia=calendario.get(Calendar.DAY_OF_MONTH);
		mes=calendario.get(Calendar.MONTH)+1;
		anio=calendario.get(Calendar.YEAR);
		String fechaModificada="";
		if(estaEnFormatoBD)
		{
			fechaModificada=""+anio+"-";
			if(mes<10)
			{
				fechaModificada+="0";
			}
			fechaModificada+=mes+"-";
			if(dia<10)
			{
				fechaModificada+="0";
			}
			fechaModificada+=dia;
		}
		else
		{
			if(dia<10)
			{
				fechaModificada+="0";
			}
			fechaModificada+=dia;
			fechaModificada+="/";
			if(mes<10)
			{
				fechaModificada+="0";
			}
			fechaModificada+=mes;
			fechaModificada+="/"+anio;
		}
		return fechaModificada;
	}
	
	/**
	 * Método que suma o resta meses a una fecha
	 * @param fechaActual
	 * @param numero de días con signo
	 * @param estaEnFormatoBD Indica si la fecha recibida como parámetro se encuentra o no en el formato de la BD
	 * @return Fecha modificada
	 */
	public static String incrementarMesesAFecha(String fechaActual, int incremento, boolean estaEnFormatoBD)
	{
		GregorianCalendar calendario=new GregorianCalendar();
		int dia=0;
		int mes=0;
		int anio=0;
		if(estaEnFormatoBD)
		{
			dia=Integer.parseInt(fechaActual.split("-")[2]);
			mes=Integer.parseInt(fechaActual.split("-")[1]);
			anio=Integer.parseInt(fechaActual.split("-")[0]);
		}
		else
		{
			dia=Integer.parseInt(fechaActual.split("/")[0]);
			mes=Integer.parseInt(fechaActual.split("/")[1]);
			anio=Integer.parseInt(fechaActual.split("/")[2]);
		}
		calendario.set(anio, mes-1, dia);
		calendario.add(GregorianCalendar.MONTH, incremento);
		dia=calendario.get(Calendar.DAY_OF_MONTH);
		mes=calendario.get(Calendar.MONTH)+1;
		anio=calendario.get(Calendar.YEAR);
		String fechaModificada="";
		if(estaEnFormatoBD)
		{
			fechaModificada=""+anio+"-";
			if(mes<10)
			{
				fechaModificada+="0";
			}
			fechaModificada+=mes+"-";
			if(dia<10)
			{
				fechaModificada+="0";
			}
			fechaModificada+=dia;
		}
		else
		{
			if(dia<10)
			{
				fechaModificada+="0";
			}
			fechaModificada+=dia;
			fechaModificada+="/";
			if(mes<10)
			{
				fechaModificada+="0";
			}
			fechaModificada+=mes;
			fechaModificada+="/"+anio;
		}
		return fechaModificada;
	}

	/**
	 * Método que suma o resta minutos a una hora dada por parámetro
	 * @param hora
	 * @param numero de minutos con signo
	 * @return hora modificada
	 */
	public static String incrementarMinutosAHora(String hora, int incremento)
	{
		GregorianCalendar calendario=new GregorianCalendar();
		
		int horas=0;
		int minutos=0;
		horas=Integer.parseInt(hora.split(":")[0]);
		minutos=Integer.parseInt(hora.split(":")[1]);
		calendario.set(GregorianCalendar.HOUR_OF_DAY, horas);
		calendario.set(GregorianCalendar.MINUTE, minutos);
		
		calendario.add(GregorianCalendar.MINUTE, incremento);
		horas=calendario.get(Calendar.HOUR_OF_DAY);
		minutos=calendario.get(Calendar.MINUTE);
	
		String horasNueva=horas+"";
		String minutosNueva=minutos+"";
		
		if(horasNueva.length()==1)
			horasNueva="0"+horasNueva;
		if(minutosNueva.length()==1)
			minutosNueva="0"+minutosNueva;
		
		String horaModificada=horasNueva+":"+minutosNueva;
						
		return horaModificada;
	}
	
	/**
	 * Método que suma o resta minutos a una hora dada por parámetro, si el número de minutos sobrepasa el día
	 * también se incrementa o decrementa la fecha enviada por parámetro
	 * @param fecha
	 * @param hora (hh:mm)
	 * @param incremento ->numero de minutos con signo
	 * @param estaEnFormatoBD (la fecha)
	 * @author amruiz
	 * @return fecha[0],hora[1]
	 */
	public static String[] incrementarMinutosAFechaHora(String fecha, String hora, int incremento, boolean estaEnFormatoBD)
	{
		
		int horas=Integer.parseInt(hora.split(":")[0]);
		int minutos=Integer.parseInt(hora.split(":")[1]);
		int totalMinutos=(horas*60)+minutos;
		
		String horaModificada=incrementarMinutosAHora(hora, incremento);
		
		String fechaModificada=fecha;
		
		//-------Se desea decrementar los minutos---------//
		if (incremento < 0)
		{
			int minutosDec=incremento*-1;
			
			if (totalMinutos < minutosDec)
				{
					int numDias=(minutosDec-totalMinutos)/1440;
					numDias++;
					fechaModificada=incrementarDiasAFecha(fecha, numDias*-1, estaEnFormatoBD);
				}
		}//if incremento <0
		
		//---------Se desea incrementar los minutos-------//
		if(incremento>0)
		{
			if (totalMinutos+incremento >= 1440)
			{
				int numDias=(incremento-(1440-totalMinutos))/1440;
				numDias++;
				fechaModificada=incrementarDiasAFecha(fecha, numDias, estaEnFormatoBD);
			}
		}
		
		String fechaHora[]={fechaModificada, horaModificada};
		return fechaHora;
	}

	/**
	* Este método calcula el número de meses que hay entre una fecha
	* especificadas (La primera debe ser la mayor, en caso contrario
	* retorna -1)
	*
	* @param dia1 Día de la fecha Menor
	* @param mes1 Mes de la fecha Menor
	* @param anno1 Año de la fecha Menor
	* @param dia2 Día de la fecha Mayor
	* @param mes2 Mes de la fecha Mayor
	* @param anno2 Año de la fecha Mayor
	 * @param tomarDias: para saber si se toman los días sobrantes como un mes
	* @return
	*/
	public static int numeroMesesEntreFechas(int dia1, int mes1, int anno1, int dia2, int mes2, int anno2, boolean tomarDias)
	{
		GregorianCalendar calendarioActual = new GregorianCalendar();
		// Los meses en Java empiezan en 0

		int mesJava1 = mes1 - 1,mesJava2 = mes2 - 1, nroMeses=0;
		// Fecha1

		calendarioActual.clear();
		calendarioActual.set(anno1, mesJava1, dia1);
		Date fechaActual = calendarioActual.getTime();

		//Fecha Dada
		GregorianCalendar calendarioFechaDada = new GregorianCalendar();
		calendarioFechaDada.clear();
		calendarioFechaDada.set(anno2, mesJava2, dia2);
		Date fechaDada=calendarioFechaDada.getTime();

		if(fechaDada.compareTo(fechaActual)>0)
		{
			//Esto pasa si la primera fecha es mayor que la segunda
			return -1;
		}
		
		calendarioFechaDada.add(Calendar.MONTH, 1);
		fechaDada=calendarioFechaDada.getTime();
		
		//logger.info("fechaActual=> "+fechaActual);
		//logger.info("fechaDada=> "+fechaDada);
		
		while(fechaActual.compareTo(fechaDada)>=0)
		{
			//Aumentamos el número de dias
			nroMeses++;
			calendarioFechaDada.add(Calendar.MONTH, 1);
			fechaDada=calendarioFechaDada.getTime();
		}
		
		/*if (dia1 != dia2)
			nroMeses-=1;*/
		
		if(tomarDias&&dia2 != dia1 && fechaActual.compareTo(fechaDada)!=0)
			nroMeses++;

		return nroMeses;
	}
	
	/**
	 * Este método calcula el número de meses que hay entre una fecha
	 * especificadas (La primera debe ser la menor, en caso contrario
	 * retorna -1)
	 * @param fechaMenor string con la fecha menor en formato de la aplicacion
	 * @param fechaMayor string con la fecha mayor en formato de la aplicacion
	 * @param tomarDias: para saber si se toman los dpias sobrantes como un mes
	 * @return el numero de días entre las dos fechas
	 */
	public static int numeroMesesEntreFechas(String fechaMenor, String fechaMayor,boolean tomarDias)
	{
        String[] arregloFechaMayor = (fechaMayor).split("/");
		
	    int diaMayor= Integer.parseInt(arregloFechaMayor[0]);
	    int mesMayor= Integer.parseInt(arregloFechaMayor[1]);
	    int anioMayor= Integer.parseInt(arregloFechaMayor[2]);
	    
	    String[] arregloFechaMenor = (fechaMenor).split("/");
		
	    int diaMenor= Integer.parseInt(arregloFechaMenor[0]);
	    int mesMenor= Integer.parseInt(arregloFechaMenor[1]);
	    int anioMenor= Integer.parseInt(arregloFechaMenor[2]);
	     
	    return numeroMesesEntreFechas(diaMayor,mesMayor,anioMayor,diaMenor,mesMenor,anioMenor,tomarDias);

	}
	
	/**
	 * Este método calcula el número de meses que hay entre una fecha
	 * especificadas (La primera debe ser la menor, en caso contrario
	 * retorna -1)
	 * @param fechaMenor string con la fecha menor en formato de la aplicacion
	 * @param fechaMayor string con la fecha mayor en formato de la aplicacion
	 * @return el numero de días entre las dos fechas
	 */
	public static int numeroMesesEntreFechasExacta(String fechaMenor, String fechaMayor)
	{
        String[] arregloFechaMayor = (fechaMayor).split("/");
		
	    int diaMayor= Integer.parseInt(arregloFechaMayor[0]);
	    int mesMayor= Integer.parseInt(arregloFechaMayor[1]);
	    int anioMayor= Integer.parseInt(arregloFechaMayor[2]);
	    
	    String[] arregloFechaMenor = (fechaMenor).split("/");
		
	    int diaMenor= Integer.parseInt(arregloFechaMenor[0]);
	    int mesMenor= Integer.parseInt(arregloFechaMenor[1]);
	    int anioMenor= Integer.parseInt(arregloFechaMenor[2]);
	     
	    return numeroMesesEntreFechasExacta(diaMayor,mesMayor,anioMayor,diaMenor,mesMenor,anioMenor);
	}
	/**
	* Este método calcula el número de meses que hay entre una fecha
	* especificadas (La primera debe ser la mayor, en caso contrario
	* retorna -1)
	*
	* @param dia1 Día de la fecha Menor
	* @param mes1 Mes de la fecha Menor
	* @param anno1 Año de la fecha Menor
	* @param dia2 Día de la fecha Mayor
	* @param mes2 Mes de la fecha Mayor
	* @param anno2 Año de la fecha Mayor
	* @return
	*/
	public static int numeroMesesEntreFechasExacta(int dia1, int mes1, int anno1, int dia2, int mes2, int anno2)
	{
		GregorianCalendar calendarioActual = new GregorianCalendar();
		// Los meses en Java empiezan en 0

		int mesJava1 = mes1 - 1,mesJava2 = mes2 - 1, nroMeses=0;
		// Fecha1

		calendarioActual.clear();
		calendarioActual.set(anno1, mesJava1, dia1);
		Date fechaActual = calendarioActual.getTime();

		//Fecha Dada
		GregorianCalendar calendarioFechaDada = new GregorianCalendar();
		calendarioFechaDada.clear();
		calendarioFechaDada.set(anno2, mesJava2, dia2);
		Date fechaDada=calendarioFechaDada.getTime();

		if(fechaDada.compareTo(fechaActual)>0)
		{
			//Esto pasa si la primera fecha es mayor que la segunda
			return -1;
		}
		
		calendarioFechaDada.add(Calendar.MONTH, 1);
		fechaDada=calendarioFechaDada.getTime();
		
		while(fechaActual.compareTo(fechaDada)>=0)
		{
			//Aumentamos el número de dias
			nroMeses++;
			calendarioFechaDada.add(Calendar.MONTH, 1);
			fechaDada=calendarioFechaDada.getTime();
		}
		
		/*if (dia1 != dia2)
			nroMeses-=1;*/

		return nroMeses;
	}
	
	/**
	 * Metodo que devuelve la edad ya sea en años, meses o dias.
	 * los parametros deben ir en el siguiente formato DD/MM/AAAA
	 * @author Jhony Alexander Duque A.
	 * @param fechaNacimiento
	 * @param fechaActual
	 * @return
	 */
	public static String calcularEdadDetallada (String fechaNacimiento,String fechaActual)
	{
		String edad="";
		String []nacimiento;
		String [] actual;
		//se toman las fehcas y se dividen en año, mes y dia
		//para ingresarla a la funcion calcularEdadDetallada.
		nacimiento=fechaNacimiento.split("/");
		actual=fechaActual.split("/");
		edad=calcularEdadDetallada(Integer.parseInt(nacimiento[2].toString()),Integer.parseInt(nacimiento[1].toString()), Integer.parseInt(nacimiento[0].toString()), Integer.parseInt(actual[0].toString()), Integer.parseInt(actual[1].toString()), Integer.parseInt(actual[2].toString()));
		
		
		return edad;
	}
	
	/**
	 * Adicionado por Jhony Alexander Duque A.
	 * Metodo que devuelve la cantidad de minutos entre fechas.
	 * @param fechaInicial
	 * @param horaInicial
	 * @param fechaFinal
	 * @param horaFinal
	 * @return minutos
	 */
	public static int numeroMinutosEntreFechas (String fechaInicial,String horaInicial,String fechaFinal,String horaFinal)
	{

		if(horaInicial.trim().length()==4)
			horaInicial="0"+horaInicial.trim();
		
		if(horaFinal.trim().length()==4)
			horaFinal="0"+horaFinal.trim();
		
		String[] fechaHora;
		int minutos = 0;
						
		while(!UtilidadFecha.compararFechas(fechaInicial,horaInicial,fechaFinal,UtilidadFecha.convertirHoraACincoCaracteres(horaFinal)).isTrue())
		{
			//logger.info("fechaInicial->"+fechaInicial+" horaInicial->"+horaInicial+" minutos-->"+minutos);
			
			fechaHora = UtilidadFecha.incrementarMinutosAFechaHora(fechaInicial,horaInicial,1,false);
			minutos ++;
			
			fechaInicial =  fechaHora[0];
			horaInicial = fechaHora[1];
			
			//logger.info("incrementa");
		}
		
		
		return minutos;
	}
	
	
	
	
	
	/**
	 * Valida la existencia de traslape entre dos fechas
	 * @param String fechaInicialA
	 * @param String fechaFinalA
	 * @param String fechaInicialB
	 * @param String fechaFinalB
	 * @return
	 * */
	public static boolean existeTraslapeEntreFechas(String fechaInicialA, String fechaFinalA,String fechaInicialB, String fechaFinalB)
	{	
		long fechaA = 0;
		long fechaB = 0;
		long fechaAf = 0;		
		SimpleDateFormat lsdf_fechas;
		
		
		/* Exije una interpretación estricta del formato esperado */
		lsdf_fechas	= new SimpleDateFormat("dd/MM/yyyy");
		lsdf_fechas.setLenient(false);
		
		//Valida el Cruce de Horarios								
		try
		{
			fechaA = lsdf_fechas.parse(fechaInicialA).getTime();							
			fechaB = lsdf_fechas.parse(fechaInicialB).getTime();							
			
			fechaAf = lsdf_fechas.parse(fechaFinalA).getTime();
											
			if(fechaA > fechaB)
			{					
				fechaB = lsdf_fechas.parse(fechaInicialA).getTime();									
				
				fechaA =  lsdf_fechas.parse(fechaInicialB).getTime();
				fechaAf = lsdf_fechas.parse(fechaFinalB).getTime();
			}					
			
			
			if(fechaB >= fechaA && fechaB <= fechaAf)			
				return true;
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Método que realiza el calculo de la duración entre 2 fecha/hora y lo retorna en formato HH:MM
	 * @param fechaInicial
	 * @param horaInicial
	 * @param fechaFinal
	 * @param horaFinal
	 * @return
	 */
	public static String calcularDuracionEntreFechas(String fechaInicial,String horaInicial,String fechaFinal,String horaFinal)
	{
		String[] fechaHora;
		int minutos = 0;
		int horas = 0;
		String duracion = "";
		
		if(horaInicial.trim().length()==4)
			horaInicial="0"+horaInicial.trim();
		
		if(horaFinal.trim().length()==4)
			horaFinal="0"+horaFinal.trim();
		
		while(!UtilidadFecha.compararFechas(fechaInicial,UtilidadFecha.convertirHoraACincoCaracteres(horaInicial),fechaFinal,UtilidadFecha.convertirHoraACincoCaracteres(horaFinal)).isTrue())
		{
			fechaHora = UtilidadFecha.incrementarMinutosAFechaHora(fechaInicial,horaInicial,1,false);
			minutos ++;
			if(minutos==60)
			{
				minutos = 0;
				horas ++;
			}
			fechaInicial =  fechaHora[0];
			horaInicial = fechaHora[1];
			duracion = UtilidadCadena.rellenarCaracter(horas+"", 2, true, "0") +":" + UtilidadCadena.rellenarCaracter(minutos+"", 2, true, "0");
			//logger.info("fechaInicial->"+fechaInicial+" horaInicial->"+horaInicial+" minutos-->"+minutos);
		}
		
		return duracion;
	}
	
	/**
	 * Metodo que dada una fechaEvaluar/horaEvaluar, evalua que este entre la fechaInicial - HoraInicial y FechaFinal - Hora Final,
	 * NOTA LAS FECHAS DEBEN ESTAR EN FORMATO DD/MM/AAAA 
	 * @return
	 */
	public static boolean betweenFechas(String fechaEvaluar, String horaEvaluar, String fechaInicial, String horaInicial, String fechaFinal, String horaFinal)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().betweenFechas(UtilidadFecha.conversionFormatoFechaABD(fechaEvaluar), horaEvaluar, UtilidadFecha.conversionFormatoFechaABD(fechaInicial), horaInicial, UtilidadFecha.conversionFormatoFechaABD(fechaFinal), horaFinal);

	}
	
	/**
	 * 
	 * @param horaInicial
	 * @param horaFinal
	 * @return
	 */
	public static long numeroMilisegundosEntreFechas(String fechaInicial, String horaInicial, String fechaFinal, String horaFinal)
	{
		int numeroMinutos= numeroMinutosEntreFechas(fechaInicial, horaInicial, fechaFinal, horaFinal);
		long milisegundos= (numeroMinutos*60)*1000;
		return milisegundos;
	}
	
	/**
	 * Método para obtener la fecha del primer o ultimo día del mes segun el mes de la fecha dada
	 * @param fechaDada
	 * @return
	 */
	public static String obtenerFechaDiaMesSegunFechaDada(String fechaDada,boolean primerDia)
	{
		String fechaDadaBD = conversionFormatoFechaABD(fechaDada);
		String[] vectorFecha = fechaDadaBD.split("-");
		String fechaObtenida = "";
		
		
		if(vectorFecha.length==3)
		{
			//Se verifica si se deseaba consultar el primer día
			if(primerDia)
				fechaObtenida = "01/"+vectorFecha[1]+"/"+vectorFecha[0];
			//Procedimiento para hallar el ultimo día del mes
			else
			{
				//Segun el mes
				switch(Utilidades.convertirAEntero(vectorFecha[1]))
				{
					case 1: //enero
					case 3: //marzo
					case 5: //mayo
					case 7: //julio
					case 8: //agosto
					case 10: //octubre
					case 12: //diciembre
						fechaObtenida = "31/"+vectorFecha[1]+"/"+vectorFecha[0];
					break;
					case 4: //Abril
					case 6: //Junio
					case 9: //Septiembre
					case 11: //Noviembre
						fechaObtenida = "30/"+vectorFecha[1]+"/"+vectorFecha[0];
					break;
					case 2: //febrero
						//Se hace la validacion de bisiesto (divisible entre 4 , pero no divisible entre 100)
						if(Utilidades.convertirAEntero(vectorFecha[0])%4==0&&Utilidades.convertirAEntero(vectorFecha[0])%100!=0)
							fechaObtenida = "29/"+vectorFecha[1]+"/"+vectorFecha[0];
						else
							fechaObtenida = "28/"+vectorFecha[1]+"/"+vectorFecha[0];
						
					break;
				}
			}
		}
		
		
		
		return fechaObtenida;
	}
	
	
	/**
	 * Esta sentencia modifica la definición del motor de BD, para que trabaje con Fechas en formato YYYY-DD-MM
	 * @return
	 */
	public static boolean actualizarFormatoFechaBD()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesDao().actualizarFormatoFechaBD();
	}

	/**
	 * Metodo que recibe una fecha sin importar el formato bd - app, y le quita la hora, 
	 * problema identificado en oracle
	 * @return
	 */
	public static String obtenerFechaSinHora(String fecha) 
	{
		//formato bd
		Pattern p = Pattern.compile("\\d\\d\\d\\d-\\d([\\d])?-\\d([\\d])?");   // expresion
		Matcher m = p.matcher(fecha);  
		if(m.find()) 
		{
			return m.group();
		}
		
		//de lo contrario retornamos formato app
		Pattern p1 = Pattern.compile("\\d([\\d])?/\\d([\\d])?/\\d\\d\\d\\d");   // expresion
		Matcher m1 = p1.matcher(fecha);  
		if(m1.find()) 
		{
			return m1.group();
		}
		
		// si no retornamos la fecha de entrada
		return fecha;
		
	}
	
	/**
	* Este método convierte un objeto <code>java.util.Date</code> en una cadena representando la
	* fecha en formato dd/mm/aaaa
	*
	* @param ad_fecha La fecha a convertir
	* @return La fecha en formato dd/mm/aaaa
	 * @throws ParseException 
	*/
	public static Date conversionFormatoFechaStringDate(String fecha)
	{
		fecha=conversionFormatoFechaABD(fecha);
		SimpleDateFormat fechaTempo=new SimpleDateFormat("yyyy-MM-dd");
		
		try 
		{
			return fechaTempo.parse(fecha);
		} 
		catch (ParseException e) 
		{
			Log4JManager.error("La cadena que se intenta convertir no contiene una fecha valida: " + fecha + " : " + e);
		}
		return null;
	
	}
	
	/**
	* Este método convierte un objeto <code>java.util.Date</code> en una cadena representando la
	* fecha en formato dd/mm/aaaa hh:mm:ss
	*
	* @param ad_fecha La fecha a convertir
	* @return La fecha y hora en formato Date dd/mm/aaaa hh:mm
	 * @throws ParseException 
	*/
	public static Date conversionFormatoFechaHoraStringDate(String fecha)
	{

		SimpleDateFormat fechaTempo=new SimpleDateFormat("dd/MM/yyyy hh:mm");
		
		try 
		{
			return fechaTempo.parse(fecha);
		} 
		catch (ParseException e) 
		{
			Log4JManager.error("La cadena que se intenta convertir no contiene una fecha valida: " + fecha + " : " + e);
		}
		return null;
	
	}
	
	
	/**
	* Este método convierte un objeto <code>java.lang.String</code> en una
	* la hora en formato Date (HH:mm)
	*
	* @return La hora en formato Date HH:mm
	*/
	public static Date getHoraActualFormatoDate()
	{
		String time = getHoraActual();
		
		DateFormat sdf = new SimpleDateFormat("HH:mm");
		Date date = new Date();
		try
		{
			date = sdf.parse(time);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			
		}
		return date;
	}
	
	
	/**
	* Este método convierte un <code>java.lang.String</code> hora (HH:mm) en un
	* Date (HH:mm)
	*
	* @return La hora en formato Date HH:mm
	*/
	public static Date convertirHoraFormatoDate(String hora)
	{
		DateFormat sdf = new SimpleDateFormat("HH:mm");
		Date date = new Date();
		
		try
		{
			date = sdf.parse(hora);
			
		}catch (Exception e)
		{
			e.printStackTrace();
			
		}
		
		return date;
	}
	
	/**
	* Este método convierte un <code>java.lang.String</code> hora (HH:mm) en un
	* Date (HH:mm:ss)
	*
	* @return La hora en formato Date HH:mm:ss
	*/
	public static Date conversionHoraFormatoDate(String hora)
	{
		Time fecFormatoTime = null;
		try {			
			SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm", new Locale("es", "ES"));
			fecFormatoTime = new java.sql.Time(sdf.parse(hora).getTime());
			return fecFormatoTime;
			
		} catch (ParseException ex) {
			Log4JManager.error(ex);
			return fecFormatoTime;
		}
	}
	
	/**
	 * Este m&eacute;todo se encarga de hallar la diferencia entre las fechas que le llegan 
	 * como par&aacute;metro.
	 * @param fechaA fecha inicial desde la cual se hace la comparaci&oacute;n
	 * @param fechaB fecha final desde la cual se hace la comparaci&oacute;n
	 * @param dataType atributo que indica si el resultado de la diferencia se desea obtener en: 
	 * 1= años, 2=meses, 5= dias.
	 * años, meses...
	 * @return diference atributo tipo long que indica la diferencia entre las fechas seg&uacute;n
	 * el par&aacute;metro tipo int ingresado.
	 * 
	 * @author Yennifer Guerrero
	 */
	public static long getDiferenceBetweenDates(Calendar fechaA,
			Calendar fechaB, int dataType) {
		long diference = 0;
		if (fechaB != null && fechaA != null) {
			diference = fechaB.getTimeInMillis() - fechaA.getTimeInMillis();
			switch (dataType) {
			case Calendar.YEAR:
				diference = diference / 31536000000L;
				break;
			case Calendar.MONTH:
				diference = diference / 2592000000L;
				break;
			case Calendar.DATE:
				diference = diference / 86400000;
				break;
			case Calendar.HOUR:
				diference = diference / 3600000;
				break;
			case Calendar.MINUTE:
				diference = diference / 60000;
				break;
			case Calendar.SECOND:
				diference = diference / 1000;
				break;
			}
		}
		return diference;
	}
	/**
	 * Este m&eacute;todo se encarga de convertir las fechas a tipo Calendar
	 * para poder hacer la implementaci&oacute;n del m&eacute;todo encargado
	 * de calcular la diferencia en años, meses o d&iacute;as entre dos fechas.
	 * @param fechaIni fecha inicial desde la cual se desea hacer la comparaci&oacute;n.
	 * @param fechaFin fecha final desde la cual se desea hacer la comparaci&oacute;n.
	 * @param dataType atributo que indica si el resultado de la diferencia se desea obtener en d&iacute;as,
	 * años, meses...
	 * @return
	 * @throws ParseException
	 * @author Yennifer Guerrero
	 */
	public static long obtenerDiferenciaEntreFechas (String fechaIni, String fechaFin, int dataType) throws ParseException{
		
		long valorRango=0;
		
		Date fechaInicial = UtilidadFecha.conversionFormatoFechaStringDate(fechaIni); 
		Date fechaFinal = UtilidadFecha.conversionFormatoFechaStringDate(fechaFin);
		 
		 Calendar fechaInicialCal=Calendar.getInstance();
		 Calendar fechaFinCal=Calendar.getInstance();
		 
		 fechaInicialCal.setTime(fechaInicial); 
		 fechaFinCal.setTime(fechaFinal);  
		 
		valorRango = getDiferenceBetweenDates(fechaInicialCal,fechaFinCal, dataType);
			
		return valorRango;
  }
	
	/**
	 * Este método convierte una fecha tipo String al formato: mes (palabra completa) día  de año.
	 * Ej: Abril 20 de 1990
	 * @param fecha 
	 * @return fecha con el nuevo tipo de formato
	 * @throws ParseException
	 * @author Fabian Becerra
	 */
	public static String conversionFormatoFechaDescripcionMesCompleto (String fecha) {
		Date fechadate=UtilidadFecha.conversionFormatoFechaStringDate(fecha);
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat("MMMMM dd", new Locale("ES"));
		SimpleDateFormat formatoDeFechaDia = new SimpleDateFormat("yyyy", new Locale("ES"));
		 
		String fechaFormateada=formatoDeFecha.format(fechadate);
		String fechaFormateadaDia=formatoDeFechaDia.format(fechadate);
		fechaFormateada = fechaFormateada.replace(fechaFormateada.substring(0, 1), fechaFormateada.substring(0, 1).toUpperCase());
		fechaFormateada = fechaFormateada+" de "+fechaFormateadaDia;
		fechaFormateada=fechaFormateada.toLowerCase();
		String caracter=fechaFormateada.charAt(0)+"";
		fechaFormateada=caracter.toUpperCase()+fechaFormateada.substring(1);
		
		return fechaFormateada;
	}
	
	
	
	
	/**
	 * Retorna el último día del mes según la combinación mes-año.
	 * Funciona tambien para años bisiestos.
	 * 
	 * @param mes 	numero del mes. Ej: Enero = 01, Febrero 02, Diciembre = 12
	 * @param anio 	año para la combinación
	 * 
	 * @return dia último día del mes
	 * 
	 * @author Cristhian Murillo
	 */
	public static int obtenerUltimoDiaMesPorAnio(int mes, int anio)
	{
		Calendar calendar = GregorianCalendar.getInstance();
		
		if( (mes>0) && (mes<13) ){
			return  calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
		}

		return ConstantesBD.codigoNuncaValido;
	}
	
	
	/**
	 * Metodo para convertir fecha en palabras
	 * recibe un String con formato yyyy-MM-dd
	 * @author Edgar Carvajal
	 * @param datos
	 * @return
	 */
	public static String  convertirFechaPalbraApp(String datos)
	{
		Date fecha;
		String retorno="";
		try
		{
			fecha = new SimpleDateFormat("dd/MM/yyyy", new Locale("ES")).parse(datos);
			SimpleDateFormat formateador = new SimpleDateFormat( "MMMM dd 'de' yyyy", new Locale("ES"));
			retorno= formateador.format(fecha);
			
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		
		
		return retorno;
				
	}
	
	/**
	 * Metodo para retornar la lista de DtoAnio entre  dos fechas Dadas
	 * @author Ricardo Ruiz
	 * @param Date fechaInicio
	 * @param Date fechaFin
	 * @return List<DtoAnio> 
	 */
	public static List<DtoAnio>  obtenerListaDtoAnios(Date fechaInicio, Date fechaFin)
	{
		List<DtoAnio> aniosDto= new ArrayList<DtoAnio>();
		if(fechaInicio != null && fechaFin != null){
			if(fechaInicio.getTime() <= fechaFin.getTime()){
				Calendar fchInicio = Calendar.getInstance();
				fchInicio.setTime(fechaInicio);
				Calendar fchFin = Calendar.getInstance();
				fchFin.setTime(fechaFin);
				for(int i=fchInicio.get(Calendar.YEAR); i<=fchFin.get(Calendar.YEAR); i++){
					DtoAnio anio = new DtoAnio();
					anio.setCodigo(i);
					anio.setDescripcion(String.valueOf(i));
					aniosDto.add(anio);
				}
			}
			else{
				throw new IllegalArgumentException("La fecha inicio debe ser menor a la fecha fin");
			}
		}
		else{
			throw new IllegalArgumentException("Las fechas no pueden ser nulas");
		}
		
		return aniosDto;
				
	}
	
	
	/**
	 * Metodo para retornar la lista de DtoMes de un Año
	 * @author Ricardo Ruiz
	 * @return List<DtoAnio> 
	 */
	public static List<DtoMes>  obtenerListaDtoMes()
	{
		List<DtoMes> mesesDto= new ArrayList<DtoMes>();
		MessageResources mensajes=MessageResources.getMessageResources("mensajes.ApplicationResources");
		String allmeses = mensajes.getMessage("anio.meses");
		String [] descMeses = allmeses.split(",");
		for(int i=0 ;i<descMeses.length; i++){
			DtoMes mes = new DtoMes();
			mes.setCodigo(i);
			mes.setDescripcion(descMeses[i]);
			mesesDto.add(mes);
		}
		return mesesDto;
				
	}
	
	
	/**
	 * Metodo para retornar el nombre del mes de un properties
	 * teniendo en cuenta que Enero=0, Febrero=1...... Diciembre=11
	 * @author Ricardo Ruiz
	 * @return String 
	 */
	public static String  obtenerNombreMesProperties(int codigoMes)
	{
		MessageResources mensajes=MessageResources.getMessageResources("mensajes.ApplicationResources");
		String allmeses = mensajes.getMessage("anio.meses");
		String [] descMeses = allmeses.split(",");
		return descMeses[codigoMes];
				
	}
	
	/**
	 * Metodo para retornar asignar horas, minutos, segundos y milisegundos a una fecha
	 * @author Ricardo Ruiz
	 * @param Calendar
	 * @param hora
	 * @param minuto
	 * @param segundo
	 * @return milisegundo
	 */
	public static Calendar  asignarHoraMinutoSegundoMiliSegundo(Calendar calendar,int hora, int minuto, int segundo, int milisegundo)
	{
		calendar.set(Calendar.HOUR_OF_DAY, hora);
		calendar.set(Calendar.MINUTE, minuto);
		calendar.set(Calendar.SECOND, segundo);
		calendar.set(Calendar.MILLISECOND, milisegundo);
		return calendar;
				
	}
	
	
}