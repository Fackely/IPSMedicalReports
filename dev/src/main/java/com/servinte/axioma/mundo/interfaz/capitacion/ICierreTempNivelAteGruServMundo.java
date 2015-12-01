package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalServicio;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.orm.CierreTempNivelAteGruServ;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 7/02/2011
 */
public interface ICierreTempNivelAteGruServMundo {
	
	/**
	 * Este método se encarga de consultar el cierre temporal de un 
	 * servicio según el contrato, el nivel de atención y el grupo de servicio dado.
	 * 
	 * @author Angela Aguirre
	 * @param 
	 * @return ArrayList<CierreTempNivelAteGruServ>
	 * @throws BDException 
	 */
	public ArrayList<CierreTempNivelAteGruServ> buscarCierreTemporalNivelAtencionGrupoServicio(DTOBusquedaCierreTemporalServicio dtoParametros) throws BDException;
	
	/**
	 * 
	 * Implementación del método attachDirty de la super clase  CierreTempNivelAteGruServHome
	 * 
	 * @param CierreTempNivelAteGruServ cierre
	 * @return boolean
	 * @author Angela Maria Aguirre
	 *
	 */
	public boolean sincronizarCierreTemporal(CierreTempNivelAteGruServ cierre);
	
	
	/**
	 * 
	 * Este Método se encarga de eliminar un registro de la tabla de 
	 * cierre temporal de presupuesto
	 * 
	 * @param CierreTempNivelAteGruServ cierre
	 * @author, Angela Maria Aguirre
	 * @throws BDException 
	 *
	 */
	public void eliminarRegistro(CierreTempNivelAteGruServ cierre);
	
	/**
	 * Este método obtiene el valor acumulado del Cierre Temporal
	 * para el convenio, contrato, Servicio y la fecha ingresados
	 * 	
	 * @param codContrato
	 * @param fecha
	 * @param codigoGrupoServicio
	 * @param consecutivoNivelAtencion
	 * @return Double con el resultado de la operación
	 * @author Ricardo Ruiz
	 */
	public Double obtenerValorCierreTemporalNivelGrupoServicios(int codContrato, Date fecha, 
							int codigoGrupoServicio, long consecutivoNivelAtencion);

}
