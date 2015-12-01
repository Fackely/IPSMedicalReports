package com.princetonsa.dao.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 * @author wilson
 *
 */
public interface SignosVitalesDao 
{
	/**
	 * Obtiene los signos vitales, dependiendo si existe informacion histotica en la bd, de los contrario postula el tiempo cero con la parametrizacion
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public HashMap<Object, Object> obtenerSignosVitalesHojaAnestesia (Connection con, int numeroSolicitud, int centroCosto, int institucion, String graficar);
	
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public boolean insertarTiempoSignoVitalAnestesia(Connection con, HashMap<Object, Object> mapaSignoVitalAnestesia);
    
	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public boolean modificarTiempoSignoVitalAnestesia(Connection con, HashMap<Object, Object> mapaSignoVitalAnestesia);
    
    /**
     * 
     * @param con
     * @param fecha
     * @param hora
     * @param numeroSolicitud
     * @return
     */
	public boolean existeTiempoSignoVitalAnestesia(Connection con, String fecha, String hora, int numeroSolicitud, int codigoTiempo);
	
	/**
	 * 
	 * @param con
	 * @param codTiempoSignoVital
	 * @param codSignoVitalAnestInst
	 * @return
	 */
	public boolean eliminarTiempoSignoVitalAnestesia(Connection con, int codTiempoSignoVital);
}
