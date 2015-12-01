package util.vista;

import java.util.ResourceBundle;

import org.apache.struts.action.ActionMessage;


import util.ValoresPorDefecto;

/**
 * Clase para generar
 * @author axioma
 *
 */
public class UtilidadVista
{
	public static String imprimirJSP(String nombreJSP)
	{
		if(ValoresPorDefecto.getMostrarNombreJSP()){
			String salida=
						"<tr bgcolor=\"#FFFFFF\">"+
							"<td>"+
								"<p class=\"barra_ubicacion\">"+
									ResourceBundle.getBundle("label.jsp")+
									"/seccionesParametrizables/seccionOdontogramaEvo/seccionGuardarCita.jsp"+
								"</p>"+
							"</td>"+
						"</tr>";
		}

		return new ActionMessage("label.jsp").toString();
		//out.println("");
	}
}
