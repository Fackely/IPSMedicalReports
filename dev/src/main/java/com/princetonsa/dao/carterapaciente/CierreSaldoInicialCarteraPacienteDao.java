/**
 * 
 */
package com.princetonsa.dao.carterapaciente;

import java.util.ArrayList;

import com.princetonsa.dto.carteraPaciente.DtoCierreCarteraPaciente;
import com.princetonsa.dto.carteraPaciente.DtoDetCierreSaldoInicialCartera;

/**
 * @author armando
 *
 */
public interface CierreSaldoInicialCarteraPacienteDao 
{

	public abstract ArrayList<DtoDetCierreSaldoInicialCartera> consultarPosibleListadoDocumentosCierre(String anioCierre, String mesCierre);

	/**
	 * 
	 * @param cierreCarteraPaciente
	 * @return
	 */
	public abstract int insertarCierreSaldoInicial(DtoCierreCarteraPaciente cierreCarteraPaciente);

	/**
	 * 
	 * @return
	 */
	public abstract DtoCierreCarteraPaciente consultarCierreInicial(int institucion);

}
