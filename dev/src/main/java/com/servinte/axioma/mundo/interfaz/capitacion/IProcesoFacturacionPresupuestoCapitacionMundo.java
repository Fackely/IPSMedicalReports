package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoTotalProcesoPresupuestoCapitado;

public interface IProcesoFacturacionPresupuestoCapitacionMundo 
{

	/**
	 * Metodo que se encarga de hacer el proceso de facturación llamado desde el cierre de presupuesto de capitación
	 * @param dtoProcesoPresupuestoCapitado
	 * @return
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> realizarProcesoFacturacion(DtoProcesoPresupuestoCapitado dtoProcesoPresupuestoCapitado);
	
	
	/**
	 * @return valor de totalListaAgrupadaServicioNivelAtencionGrupoServicio
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> obtenerTotalListaAgrupadaServicioNivelAtencionGrupoServicio();


	/**
	 * @return valor de totalListaAgrupadaArticuloNivelAtencionClaseInventario
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> obtenerTotalListaAgrupadaArticuloNivelAtencionClaseInventario();
}
