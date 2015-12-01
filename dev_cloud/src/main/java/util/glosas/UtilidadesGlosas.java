package util.glosas;

import java.sql.Connection;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.UtilidadesGlosasDao;
import com.princetonsa.dto.glosas.DtoDetalleFacturaGlosa;
import com.princetonsa.dto.glosas.DtoGlosa;

/**
 * @author Gio
 */
public class UtilidadesGlosas {
	
	/**
	 * Manejador de mensajes
	 */
	private static Logger logger = Logger.getLogger(UtilidadesGlosas.class);
	
	/**
	 * instancia del DAO
	 * @return
	 */
	public static UtilidadesGlosasDao getUtilidadesGlosasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesGlosasDao();
	}
	
	/**
	 * Obtener las glosas de una factura
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public static ArrayList<DtoGlosa> obtenerGlosasFactura(Connection con, String codigoFactura)
	{
		return getUtilidadesGlosasDao().obtenerGlosasFactura(con, codigoFactura);
	}

	/**
	 * 
	 * @param con
	 * @param codigoGlosa
	 * @return
	 */
	public static ArrayList<DtoDetalleFacturaGlosa> obtenerDetalleGlosaFactura(Connection con, String codigoGlosa) {
		return getUtilidadesGlosasDao().obtenerDetalleGlosaFactura(con, codigoGlosa);
	}

	/**
	 * Obtener Total Soportado Respuesta de Glosa
	 * @param con
	 * @param informacionRespuesta
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static double obtenerTotalSoportadoRespuesta(Connection con,
			int informacionRespuesta, int codigoInstitucion) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesGlosasDao().obtenerTotalSoportadoRespuesta(con, informacionRespuesta, codigoInstitucion);
	}

	/**
	 * Obtener Total Soportado Aceptado de Glosa
	 * @param con
	 * @param informacionRespuesta
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static double obtenerTotalAceptadoRespuesta(Connection con,
			int informacionRespuesta, int codigoInstitucion) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesGlosasDao().obtenerTotalAceptadoRespuesta(con, informacionRespuesta, codigoInstitucion);
	}
}
