package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DtoInconsistenciasProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.capitacion.DtoTotalProcesoPresupuestoCapitado;

public interface IProcesoCargosCuentaConvenioContratoPresupuestoCapitacionMundo {

	/** 
	 * Este M�todo realiza el proceso de Cargos a la cuenta para el cierre. 
	 * Anexo 1030.
	 * 
	 * @author Camilo G�mez
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> realizarProcesoCargosCuenta(DtoProcesoPresupuestoCapitado dtoProcesoPresupuestoCapitado);
	

	/**
	 * @return valor de totalListaAgrupadaServicio
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> obtenerTotalListaAgrupadaServicio();

	/**
	 * @return valor de totalListaAgrupadaArticulo
	 */
	public ArrayList<DtoTotalProcesoPresupuestoCapitado> obtenerTotalListaAgrupadaArticulo();
	
	public ArrayList<DtoInconsistenciasProcesoPresupuestoCapitado> obtenerListaInconsistenciasProcesoCargos();

}
