package com.sies.mundo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.sies.dao.SiEsFactory;

import util.UtilidadBD;
import util.UtilidadFecha;

public class UtilidadSiEs
{
	public static ActionForward hacerMapping(Connection con, String nombreForward, ActionMapping mapping)
	{
		try
		{
			/*
			 * Método para cerrar la conexión (Pendiente)
			if(con!= null && !con.isClosed())
			{
				con.close();
			}
			*/
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			System.err.println("Error cerrando la conexión "+e);
		}
		return mapping.findForward(nombreForward);
	}

	public static ActionForward hacerMappingHibernate(String nombreForward, ActionMapping mapping)
	{
		HibernateUtil.endTransaction();
		return mapping.findForward(nombreForward);
	}

	/**
	 * Método para obtener el siguiente valor de una secuencia
	 * @param con
	 * @param nombreSecuencia
	 * @return siguiente valor de la secuencia
	 */
	public static int obtenerSiguienteValorSecuencia(Connection con, String nombreSecuencia)
	{
		return SiEsFactory.getDaoFactory().getUtilidadSiEsDao().obtenerValorSecuencia(con, nombreSecuencia);
	}
	
	/**
	 * Método que se encarga de retornar el valor booleano
	 * de la cadena enviada (No discrimina mayúsculas y minúsculas)
	 * @param validarEstadoSolicitudesInterpretadas
	 * @return true en caso de que la cadena enviada contenga los valores "true", "t" ó "1"
	 * false en caso de que la cadena enviada contenga los valores "false", "f" ó "0"
	 * Si la cadena enviada no se encuentra en el rango de valores descrito
	 * se envía un log a la salida del sistema y retorna false.
	 */
	public static boolean verificarBoolean(String valorBoolean)
	{
		if(valorBoolean==null)
		{
			return false;
		}
		if(valorBoolean.equals("null"))
		{
			return false;
		}
		valorBoolean=valorBoolean.toLowerCase();
		
		if(valorBoolean.equals("true") || valorBoolean.equals("t") || valorBoolean.equals("1") || valorBoolean.equals("on") || valorBoolean.equals("s") || valorBoolean.equals("y") || valorBoolean.equals("si") || valorBoolean.equals("yes"))
		{
			return true;
		}
		else if(valorBoolean.equals("false") || valorBoolean.equals("f") || valorBoolean.equals("0") || valorBoolean.equals("off") || valorBoolean.equals("n") || valorBoolean.equals("no")||valorBoolean.equals(""))
		{
			return false;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Método para consultar el código y el nombre de una persona en la BD
	 * @param con
	 * @param codigoEnfermera
	 * @return Collection<HashMap<String, Object>> Con los datos de la persona (codigo, nombre)
	 */
	public static String consultarNombrePersona(Connection con, int codigoPersona)
	{
		Collection<HashMap<String, Object>> col=SiEsFactory.getDaoFactory().getUtilidadSiEsDao().consultarNombrePersona(con, codigoPersona);
		Iterator<HashMap<String, Object>> iterador=col.iterator();
		while (iterador.hasNext())
		{
			HashMap<String, Object> element = iterador.next();
			return (String)element.get("nombre");
		}
		return null;
	}

	/**
	 * Método para consultar el código y el nombre de una persona en la BD
	 * @param con
	 * @param codigoEnfermera
	 * @return Collection<HashMap<String, Object>> Con los datos de la persona (codigo, nombre)
	 */
	public static Collection<HashMap<String, Object>> consultarCodigoNombrePersona(Connection con, int codigoPersona)
	{
		return SiEsFactory.getDaoFactory().getUtilidadSiEsDao().consultarNombrePersona(con, codigoPersona);
	}
	
	/**
	 * Método que convierte una observación con \n a formato
	 * html (<br>)
	 * @param observacionOriginal Observación a la que se desea
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
			return observaciones;
		}
	}
	
	/**
	 * Método que cambia los <BR> por  saltos de linea ASCCI
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
	 * Método que busca un listado de personas por nombre (criterio=false) o por código (criterio=true)
	 * @param textoBusqueda
	 * @param criterio
	 * @param institucion Institución
	 * @param centroCosto Centro de costo del Usuario
	 * @param centroAtencion Centro de Atención del usuario
	 * @param connection
	 * @return
	 */
	public static Collection<HashMap<String, Object>> busquedaAvanzadaPersonas(Connection con, String textoBusqueda, boolean criterio, int institucion, int centroCosto, int centroAtencion)
	{
		return SiEsFactory.getDaoFactory().getUtilidadSiEsDao().busquedaAvanzadaPersonas(con, textoBusqueda, criterio, institucion, centroCosto, centroAtencion);
	}

	/**
	 * Método para consultar la categoría a la cual pertenece un turno 
	 * @param con
	 * @param codigoTurno
	 * @return Código de la categoría
	 */
	public static Integer codigoCategoriaTurno(Connection con, int codigoTurno)
	{
		return SiEsFactory.getDaoFactory().getUtilidadSiEsDao().codigoCategoriaTurno(con, codigoTurno);
	}

	/**
	 * Método que genera un listado de fechas
	 * @param numeroDias
	 * @param fechaInicio
	 * @return ArrayList con el listado de las fechas
	 */
	public static ArrayList<String> generarListadoFechas(int numeroDias, String fechaInicio)
	{
		ArrayList<String> fechas=new ArrayList<String>();
		fechaInicio=UtilidadFecha.conversionFormatoFechaABD(fechaInicio);
		//fechas.add(fechaInicio);
		String fecha=fechaInicio;
		for(int i=0; i<numeroDias; i++)
		{
			fechas.add(fecha);
			fecha=UtilidadFecha.incrementarDiasAFecha(fechaInicio, (i+1), true);
		}
		return fechas;
	}
	
	/**
	 * Método que quita las tildes de una cadena
	 * @param palabra
	 * @return Palabra sin tildes
	 */
	public static String quitarTildes(String palabra)
	{
		String sinTilde=palabra;
		sinTilde=sinTilde.replace("á", "a");
		sinTilde=sinTilde.replace("é", "e");
		sinTilde=sinTilde.replace("í", "i");
		sinTilde=sinTilde.replace("ó", "o");
		sinTilde=sinTilde.replace("ú", "u");
		sinTilde=sinTilde.replace("Á", "A");
		sinTilde=sinTilde.replace("É", "E");
		sinTilde=sinTilde.replace("Í", "I");
		sinTilde=sinTilde.replace("Ó", "O");
		sinTilde=sinTilde.replace("Ú", "U");
		sinTilde=sinTilde.replace("ñ", "n");
		sinTilde=sinTilde.replace("Ñ", "N");
		return sinTilde;
	}

	/**
	 * Método que busca personas que no tienen turnos en un rango de fechas
	 * para in cuadro de turnos específico
	 * @param connection
	 * @param textoBusqueda
	 * @param criterio
	 * @param institucion
	 * @param centroCosto
	 * @param centroAtencion
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoCuadro
	 * @return
	 */
	public static Collection<HashMap<String, Object>> busquedaAvanzadaPersonas(Connection con, String textoBusqueda, boolean criterio, int institucion, int centroCosto, int centroAtencion, String fechaInicial, String fechaFinal, int codigoCuadro)
	{
		return SiEsFactory.getDaoFactory().getUtilidadSiEsDao().busquedaAvanzadaPersonas(con, textoBusqueda, criterio, institucion, centroCosto, centroAtencion, fechaInicial, fechaFinal, codigoCuadro);
	}
	
	/**
	 * Método para redondear un número dada la cantidad de decimales
	 * @param numero
	 * @param decimales
	 * @return
	 */
	public static double redondear( double numero, int decimales )
	{
		/*
		 *  Multiplico el número por 10 a la #decimales, redondeo
		 *  y luego divido entre 10 a la #decimales
		 */
		return Math.round(numero*Math.pow(10,decimales))/Math.pow(10,decimales);
	}

	/**
	 * Método para obtener el usuario de la sesión
	 * @param request
	 * @return UsuarioBasico
	 */
	public static UsuarioBasico obtenerUsuarioBasico(HttpServletRequest request)
	{
		UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		return usuario;
	}
	
	/**
	 * Método para listar los centros de costo
	 * @param con Conexión con la base de datos
	 * @return Listado de los centros de costo (Keys: "codigo", "nombre")
	 */
	public static Collection<HashMap<String, Object>>listarCentrosCosto(Connection con)
	{
		return SiEsFactory.getDaoFactory().getUtilidadSiEsDao().listarCentrosCosto(con);
	}

	//@TODO DE SIES
	public static boolean esAxioma() {
		
		return true;
	}
}
