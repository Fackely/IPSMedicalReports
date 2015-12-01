/**
 * 
 */
package com.servinte.axioma.cron.task;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IProcesosInactivacionUsuarioCaducidadPassword;
import com.servinte.axioma.orm.LogProcesoInactivacionUsu;
import com.servinte.axioma.orm.LogProcesoInactivacionUsuHome;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * @author axioma
 *
 */
public class ProcesoActivacionInactivacionUsuariosTask implements Runnable 
{

	@Override
	public void run() 
	{
		IProcesosInactivacionUsuarioCaducidadPassword proc=AdministracionFabricaMundo.crearProcesosInactivacionUsuarioCaducidadPassword();
		int usuarios=proc.procesoInactivacionUsuario();
		
		LogProcesoInactivacionUsuHome dao=new LogProcesoInactivacionUsuHome();
		LogProcesoInactivacionUsu dto=new LogProcesoInactivacionUsu();
		dto.setCantidad(usuarios);
		dto.setExitoso(ConstantesBD.acronimoSi);
		dto.setFechaEjecucion(UtilidadFecha.getFechaActualTipoBD());
		dto.setHoraEjecucion(UtilidadFecha.getHoraActual());
		dao.persist(dto);
		UtilidadTransaccion.getTransaccion().commit();
		Log4JManager.info("TERMINANDO ProcesoActivacionInactivacionUsuariosTask");
	}

}
