/**
 * 
 */
package com.servinte.axioma.bl.consultaExterna.impl;

import java.util.List;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.bl.consultaExterna.interfaz.ICitasMundo;
import com.servinte.axioma.delegate.consultaExterna.CitasDelegate;
import com.servinte.axioma.dto.administracion.ProfesionalSaludDto;
import com.servinte.axioma.dto.consultaExterna.CitaDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * @author jeilones
 * @created 23/10/2012
 *
 */
public class CitasMundo implements ICitasMundo {

	/**
	 * 
	 * @author jeilones
	 * @created 23/10/2012
	 */
	public CitasMundo() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.consultaExterna.interfaz.ICitasMundo#consultarCitasAtentidas(int)
	 */
	@Override
	public List<CitaDto> consultarCitasAtentidas(String loginUsuario,int codigoIngreso,int codigoPaciente)
			throws IPSException {
		try{
			CitasDelegate citasDelegate=new CitasDelegate();
			return citasDelegate.consultarCitasAtentidas(loginUsuario, codigoIngreso, codigoPaciente);
		}catch (IPSException ipse) {
			throw ipse;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.consultaExterna.interfaz.ICitasMundo#consultarProfesionalesAtiendenCitas(int)
	 */
	public List<ProfesionalSaludDto> consultarProfesionalesAtiendenCitas(int codigoInsitucion) throws IPSException{
		try{
			CitasDelegate citasDelegate=new CitasDelegate();
			return citasDelegate.consultarProfesionalesAtiendenCitas(codigoInsitucion);
		}catch (IPSException ipse) {
			throw ipse;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}

}
