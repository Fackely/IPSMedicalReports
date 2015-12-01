package com.servinte.axioma.dao.impl.facturacion;

import com.princetonsa.dto.cargos.DtoTercero;
import com.servinte.axioma.dao.interfaz.facturacion.ITercerosDAO;
import com.servinte.axioma.orm.Terceros;
import com.servinte.axioma.orm.delegate.facturacion.TerceroDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link ITercerosDAO}.
 * @author Diana Carolina G
 *
 */


public class TercerosDAO implements ITercerosDAO {
	
	private TerceroDelegate tercerosDelegate;

	public TercerosDAO(){
		setTercerosDel(new TerceroDelegate());
	}
	
	public void setTercerosDel(TerceroDelegate tercerosDel) {
		this.tercerosDelegate = tercerosDel;
	}

	public TerceroDelegate getTercerosDel() {
		return tercerosDelegate;
	}

	@Override
	public Terceros findById(int id) {
		return tercerosDelegate.findById(id);
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
		return tercerosDelegate.obtenerTerceroXEntidadSub(codEntidadSubcontratada);
	}
	

}
