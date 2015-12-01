/**
 * 
 */
package util;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.mundo.Paciente;


/**
 * @author JorOsoVe
 *
 */
public class UtilidadesDWR 
{
	public static String validarPacienteIgualNombre(String primerNombre,String segundoNombre,String primerApellido,String segundoApellido,String tipoID,String numeroID)
	{
		String resultado="";
		Connection con=UtilidadBD.abrirConexion();
		
		try
		{
			ArrayList<HashMap<String, Object>> resultadoArray=Paciente.validarPacienteIgualNombreStatico(con,primerNombre,segundoNombre,primerApellido,segundoApellido,tipoID,numeroID);

			if(resultadoArray.size()>0)
			{
				String contenido = "Los nombres y apellidos <b>"+primerNombre+" "+segundoNombre+" "+primerApellido+" "+segundoApellido+"</b> ya han sido ingresados para los siguientes paciente: <br> ";
				contenido+=" <ul> ";
	
				for(HashMap elemento:resultadoArray)
					contenido += "<li>"+elemento.get("nombrePersona")+" "+elemento.get("tipoId") + ". " + elemento.get("numeroId") + " </li> ";
				contenido+=" </ul> ";
				contenido += "¿Desea Continuar?";
				//**************************************************************************************************
				
				resultado=contenido;
			}
		}
		catch(Exception e)
		{
			Log4JManager.error("ERROR",e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		return resultado;
	}

}
