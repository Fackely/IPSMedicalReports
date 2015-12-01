package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DtoInconsistenciasProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoTotalProcesoPresupuestoCapitado;

public interface IProcesoAutorizacionesPresupuestoCapitacionMundo {

	/** 
	 * Este Método realiza el proceso de Autorizaciones para el cierre. 
	 * Anexo 1027.
	 * 
	 * @author Camilo Gómez
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> realizarProcesoAutorizacion(DtoProcesoPresupuestoCapitado dtoProcesoPresupuestoCapitado);

	
	/**
	 * @return valor de totalListaAgrupadaServicio
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> obtenerTotalListaAgrupadaServicio();


	/**
	 * @return valor de totalListaAgrupadaArticulo
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> obtenerTotalListaAgrupadaArticulo();
	
	
	public ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> obtenerListaInconsistenciasProcesoAutoriz();
}
