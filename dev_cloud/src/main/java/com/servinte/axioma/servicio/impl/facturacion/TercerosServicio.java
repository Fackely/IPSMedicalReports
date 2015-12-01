package com.servinte.axioma.servicio.impl.facturacion;

import com.princetonsa.dto.cargos.DtoTercero;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.ITercerosMundo;
import com.servinte.axioma.orm.Terceros;
import com.servinte.axioma.servicio.interfaz.facturacion.ITercerosServicio;

public class TercerosServicio implements ITercerosServicio {

	private ITercerosMundo tercerosMundo;
	
	
	public TercerosServicio(){
		setTercerosMundo(FacturacionFabricaMundo.crearTercerosMundo());
	}
	
	
	public void setTercerosMundo(ITercerosMundo tercerosMundo) {
		this.tercerosMundo = tercerosMundo;
	}



	public ITercerosMundo getTercerosMundo() {
		return tercerosMundo;
	}



	@Override
	public Terceros findById(int id) {
		return tercerosMundo.findById(id);
	}
	
	/**
	 * Obtener el tercero por el codigo pk de una entidad subcontratada 
	 * 
	 * @param codEntidadSubcontratada
	 * @return DtoTercero
	 * 
	 * @author Fabián Becerra
	 */
	public DtoTercero obtenerTerceroXEntidadSub (long codEntidadSubcontratada){
		return tercerosMundo.obtenerTerceroXEntidadSub(codEntidadSubcontratada);
	}

}
