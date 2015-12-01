package com.princetonsa.dao.facturacion;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoResponsableFacturaOdontologica;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * Interface de la factura odontologica
 * @author axioma
 *
 */
public interface FacturaOdontologicaDao 
{
	/**
	 * Metodo que carga los responsables de la cuenta 
	 * @param cuenta
	 * @param centroAtencion
	 * @return
	 */
	public ArrayList<DtoResponsableFacturaOdontologica> cargarResponsables(BigDecimal cuenta, int centroAtencion, int institucion, ArrayList<BigDecimal> filtroCargosFacturaAutomatica) throws BDException;	
}
