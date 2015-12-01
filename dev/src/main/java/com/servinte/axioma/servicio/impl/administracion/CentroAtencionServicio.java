package com.servinte.axioma.servicio.impl.administracion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.RecibosCajaId;
import com.servinte.axioma.servicio.interfaz.administracion.ICentroAtencionServicio;


/**
 * 
 * @author axioma
 *
 */
public class CentroAtencionServicio implements ICentroAtencionServicio
{

	@Override
	public ArrayList<DtoCentrosAtencion> listarCentrosAtencion(boolean activo)
	{
		return AdministracionFabricaMundo.crearCentroAtencionMundo().listarCentrosAtencion(activo);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> listarCentrosAtencion (DtoCentrosAtencion dtoCentrosAtencion){
		return AdministracionFabricaMundo.crearCentroAtencionMundo().listarCentrosAtencion(dtoCentrosAtencion);
	}
	
	@Override
	public ArrayList<DtoCentrosAtencion> obtenerCentrosAtencionIngresos (List<Integer> ingresos){
		return AdministracionFabricaMundo.crearCentroAtencionMundo().obtenerCentrosAtencionIngresos(ingresos);
	}
	
	@Override
	public CentroAtencion buscarPorCodigoPK(int codigo){
		return AdministracionFabricaMundo.crearCentroAtencionMundo().buscarPorCodigo(codigo);
	}

	@Override
	public ArrayList<DtoCentrosAtencion> obtenerCentrosAtencionPresupuestos(
			List<Long> presupuesto) {
		return AdministracionFabricaMundo.crearCentroAtencionMundo().obtenerCentrosAtencionPresupuestos(presupuesto);
	}

	@Override
	public ArrayList<DtoCentrosAtencion> obtenerCentrosAtencionRecibosCaja(
			List<RecibosCajaId> numeroRC) {
		return AdministracionFabricaMundo.crearCentroAtencionMundo().obtenerCentrosAtencionRecibosCaja(numeroRC);
	}


}
