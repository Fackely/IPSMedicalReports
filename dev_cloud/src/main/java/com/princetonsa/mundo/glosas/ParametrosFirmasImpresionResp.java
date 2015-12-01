package com.princetonsa.mundo.glosas;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadBD;

import com.princetonsa.actionform.glosas.ParametrosFirmasImpresionRespForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.ParametrosFirmasImpresionRespDao;

public class ParametrosFirmasImpresionResp {


	/**	 * Se inicializa el Dao	 */
	public static ParametrosFirmasImpresionRespDao aplicacionDao() {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getParametrosFirmasImpresionRespDao();
	}
	
	/** Metodo que devuelve el listado de los datos consultados de Firmas en Impresion Respuesta de Glosa
	 * @param con
	 * @return	 */
	public static HashMap listadoFirmas(Connection con) {		
		return aplicacionDao().listadoFirmas(con);
	}
	
	/** Metodo que devuelve el listado de los datos consultados de Firmas en Impresion Respuesta de Glosa sin Conexion
	 * @return	 */
	public static HashMap listadoFirmasSinConexion() {
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		HashMap<String, Object> mapaAux = new HashMap<String, Object>();
		
		mapaAux= aplicacionDao().listadoFirmas(con);
		
		UtilidadBD.closeConnection(con);
				
		return mapaAux;
	}

	
	/** Método que genera la inserción de una nueva firma en impresion de respuesta glosa, 
	 * @param con, forma, usuario, institucion
	 * @return	 */
	public boolean insertarFirmaImpresion(Connection con, String Usuarios, String Cargos, String usuario, int institucion) {
		HashMap<String, Object> criterios = new HashMap<String, Object>();

		Boolean transaccion = false;
		criterios.put("usuarioFirma", usuario);
		criterios.put("institucionFirma", institucion);
		criterios.put("tmpUsuario", Usuarios);
		criterios.put("tmpCargo", Cargos);
		transaccion = aplicacionDao().insertarFirmaImpresion(con, criterios);		
		return transaccion;
	}
	
	
	/** Método implementado para eliminar una Firma en Impresion de REspuesta Glosa
	 * @param con, conceptosGenerales
	 * @return	 */
	public boolean eliminarFirmaImpresion(Connection con, int codeSecuenFirma) {
		return aplicacionDao().eliminarFirmaImpresion(con, codeSecuenFirma);
	}


	/** Método implementado para eliminar todas las Firmas en Impresion de REspuesta Glosa
	 * @param con
	 * @return	 */
	public boolean eliminarTodaFirmaImpresion(Connection con) {
		return aplicacionDao().eliminarTodaFirmaImpresion(con);
	}
	
	
	/** Método implementado para Modificar una Firma en Impresion de REspuesta Glosa
	 * @param con, forma, codigoInstitucion, loginUsuario
	 * @return	 */
	public boolean modificarFirmaImpresion(Connection con, ParametrosFirmasImpresionRespForm forma, int codigoInstitucion, String loginUsuario) {
		HashMap<String, Object> criterios = new HashMap<String, Object>();

		criterios.put("codigo", forma.getFrmFirmasImpresion("tmpCodMod"));
		criterios.put("usuario", forma.getFrmFirmasImpresion("tmpUsuario"));
		criterios.put("cargo", forma.getFrmFirmasImpresion("tmpCargo"));
		
		return aplicacionDao().modificarFirmaImpresion(con, criterios);
	}

}