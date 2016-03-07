/*
 * Creado en 13/12/2006
 *
 * Princeton S.A.
 */
package util;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;

/**
 * @author Wilson Rios
 *
 * Princeton S.A.
 */
public class UtilidadImpresion
{
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static HashMap obtenerEncabezadoPaciente(Connection con, String idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadImpresionDao().obtenerEncabezadoPaciente(con, idCuenta);
	}
	
	/**
	 * <b>arreglarCampoStringImpresion</b> - M�todo que arregla los campos para la impresi�n en reportes.
	 * Se implementa debido a que en algunos campos, cuando hay l�neas de texto que exceden los 138 caracteres, 
	 * el reporte corta la l�nea, pero no se cuenta el salto de l�nea como una l�nea nueva, por lo que al finalizar el texto,
	 * no hay espacio (visualmente) para la l�nea, y no muestra informaci�n.  
	 * @param campo
	 * @return
	 */
	public static String arreglarCampoStringImpresion(String campo) {
		String strAux = campo;
		if (campo != null) {
			String str[] = campo.split("\n");
			for (int i=0; i<str.length-1; i++) {
				int times = str[i].length()/138;
				while (times > 0) {
					strAux += "\n";
					times--;
				}
			}
		}
		return strAux;
	}
}
