/**
 * 
 */
package com.servinte.axioma.cron.task;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IProcesosInactivacionUsuarioCaducidadPassword;
import com.servinte.axioma.orm.LogProcesoCadupwdUsu;
import com.servinte.axioma.orm.LogProcesoCadupwdUsuHome;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * @author axioma
 *
 */
public class ProcesoActivacionInactivacionPasswordUsuariosTask implements Runnable
{
	@Override
	public void run() 
	{
		IProcesosInactivacionUsuarioCaducidadPassword proc=AdministracionFabricaMundo.crearProcesosInactivacionUsuarioCaducidadPassword();
		int passCaducados=proc.procesoCaducidadPassword();
		
		LogProcesoCadupwdUsuHome dao=new LogProcesoCadupwdUsuHome();
		LogProcesoCadupwdUsu dto=new LogProcesoCadupwdUsu();
		dto.setCantidad(passCaducados);
		dto.setExitoso(ConstantesBD.acronimoSi);
		dto.setFechaEjecucion(UtilidadFecha.getFechaActualTipoBD());
		dto.setHoraEjecucion(UtilidadFecha.getHoraActual());
		dao.persist(dto);
		UtilidadTransaccion.getTransaccion().commit();
		Log4JManager.info("TERMINANDO ProcesoActivacionInactivacionPasswordUsuariosTask");
	}

}
