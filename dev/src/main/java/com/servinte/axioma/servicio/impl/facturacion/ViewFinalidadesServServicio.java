package com.servinte.axioma.servicio.impl.facturacion;

import java.util.List;

import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IViewFinalidadesServMundo;
import com.servinte.axioma.orm.ViewFinalidadesServ;
import com.servinte.axioma.servicio.interfaz.facturacion.IViewFinalidadesServServicio;

public class ViewFinalidadesServServicio implements IViewFinalidadesServServicio{

	private IViewFinalidadesServMundo mundo;
	
	public ViewFinalidadesServServicio(){
		mundo = FacturacionFabricaMundo.crearViewFinalidadesServMundo(); 
	}
	
	@Override
	public List<ViewFinalidadesServ> obtenerViewFinalidadesServ(
			int codigoServicio) {
		return mundo.obtenerViewFinalidadesServ(codigoServicio);
	}

	public void setMundo(IViewFinalidadesServMundo mundo) {
		this.mundo = mundo;
	}

	public IViewFinalidadesServMundo getMundo() {
		return mundo;
	}

}
