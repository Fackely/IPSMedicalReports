package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.FormatoJustServNoposDao;
import com.princetonsa.dao.inventarios.SeccionesDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseFormatoJustArtNoposDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseFormatoJustServNoposDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseSeccionesDao;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.FormatoJustServNopos;
import com.princetonsa.mundo.inventarios.Secciones;

/**
 * 
 * @author axioma
 *
 */
public class OracleFormatoJustServNoposDao implements FormatoJustServNoposDao
{
	/**
	 * 
	 * @param con
	 * @param formatoJust
	 */
	public HashMap consultar(Connection con, FormatoJustServNopos formatoJust, PersonaBasica paciente, UsuarioBasico usuario, boolean nueva)
	{
		return SqlBaseFormatoJustServNoposDao.consultar(con, formatoJust, paciente, usuario, nueva);
	}
	
	/**
	 * 
	 * @param con
	 * @param numJustificacion
	 * @return
	 */
	public HashMap consultarDiagnosticos(Connection con, int numJustificacion)
	{
		return SqlBaseFormatoJustServNoposDao.consultarDiagnosticos(con, numJustificacion);
	}
	
	/**
	 * Metodo para ingresar una justificación No Pos de Servicios.
	 * Los campos solicitud y ordenAmbulatoria son excluyentes!
	 * @param con
	 * @param institucion
	 * @param usuarioModifica
	 * @param justificacion
	 * @param solicitud
	 * @param ordenAmbulatoria
	 * @param servicio
	 * @param profesional
	 * @return
	 */
	public String ingresarJustificacion(Connection con, int institucion, String usuario_modifica, HashMap justificacion, int solicitud, int ordenAmbulatoria, int servicio, int profesional)
	{
		return SqlBaseFormatoJustServNoposDao.ingresarJustificacion(con, institucion, usuario_modifica, justificacion, solicitud, ordenAmbulatoria, servicio, profesional);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa justificacion
	 * @param solicitud
	 * @return
	 */
	public String actualizarJustificacion(Connection con, int institucion, String usuario_modifica, HashMap justificacion, int solicitud, int servicio)
	{
		return SqlBaseFormatoJustServNoposDao.actualizarJustificacion(con, institucion, usuario_modifica, justificacion, solicitud, servicio);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param subcuenta
	 * @param cantidad
	 * @return
	 */
	public boolean ingresarResponsable(Connection con, String numeroSolicitud, int subcuenta, int cantidad)
	{
		return SqlBaseFormatoJustServNoposDao.ingresarResponsable(con, numeroSolicitud, subcuenta, cantidad);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap<String, Object> subCuentas(Connection con, int numeroSolicitud)
	{
		return SqlBaseFormatoJustServNoposDao.subCuentas(con, numeroSolicitud);
	}
	
	/**
	 * Metodo para revisar existencia de Justificaciones y modificarlas segun distribucion
	 * @param con
	 * @param numSol
	 * @param subCuenta
	 * @param cantidad
	 * @return
	 */
	public int revisarJustificacionDistribucion(Connection con, String numSol, String subCuenta, String cantidad, String servicio, String usuario)
	{
		return SqlBaseFormatoJustServNoposDao.revisarJustificacionDistribucion(con, numSol, subCuenta, cantidad, servicio, usuario);
	}
	
	/**
	 * Metodo para validar Convenio y Articulo No Pos
	 * @param con
	 * @param articulo
	 * @param subCuenta
	 * @return
	 */
	public boolean validarArtConvJustificacion(Connection con, String servicio, String subCuenta)
	{
		return SqlBaseFormatoJustServNoposDao.validarArtConvJustificacion(con, servicio, subCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @return
	 */
	public boolean existeJustificacion(Connection con, String numeroSolicitud, String servicio)
	{
		return SqlBaseFormatoJustServNoposDao.existeJustificacion(con, numeroSolicitud, servicio);
	}
	
	/**
	 * Metodo para consultar una justificacion historica
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @return
	 */
	public HashMap<String, Object> consultarJustificacionHistorica(Connection con, String numeroSolicitud, String servicio)
	{
		return SqlBaseFormatoJustServNoposDao.consultarJustificacionHistorica(con, numeroSolicitud, servicio);
	}
}