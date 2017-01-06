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
	 * <b>arreglarCampoStringImpresion</b> - Método que arregla los campos para la impresión en reportes.
	 * Se implementa debido a que en algunos campos, cuando hay líneas de texto que exceden los 138 caracteres, 
	 * el reporte corta la línea, pero no se cuenta el salto de línea como una línea nueva, por lo que al finalizar el texto,
	 * no hay espacio (visualmente) para la línea, y no muestra información.  
	 * @param campo
	 * @return
	 */
	public static String arreglarCampoStringImpresion(String campo) {
		return arreglarCampoStringImpresion(campo, 138);
	}
	
	/**
	 * <b>arreglarCampoStringImpresion</b> - Método que arregla los campos para la impresión en reportes.
	 * Se implementa debido a que en algunos campos, cuando hay líneas de texto que exceden los n caracteres, 
	 * el reporte corta la línea, pero no se cuenta el salto de línea como una línea nueva, por lo que al finalizar el texto,
	 * no hay espacio (visualmente) para la línea, y no muestra información.  
	 * @param campo
	 * @return
	 */
	public static String arreglarCampoStringImpresion(String campo, int cantCaracteres) {
		String strAux = campo;
		if (campo != null) {
			String str[] = campo.split("\n");
			for (int i=0; i<str.length; i++) {
				int times = str[i].length()/cantCaracteres;
//				while (times > 0) {
				if (times > 0) {
					strAux += "\n";
//					times--;
				}
			}
		}
		return strAux;
	}
}
