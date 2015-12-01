/**
 * 
 */
package com.servinte.axioma.vista.odontologia;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.princetonsa.dto.odontologia.DtoMotivosCambioServicio;
import com.servinte.axioma.servicio.fabrica.MotivosCambioServicioFabrica;
import com.servinte.axioma.servicio.interfaz.odontologia.IMotivosCambioServicio;

/**
 * Componente de presentacion para usar con MotivosCambioServicio y usado por DWR
 * @author armando
 *
 */
public class MotivosCambioServicioView 
{
	/**
	 * 
	 */
	private IMotivosCambioServicio motivosCambioServicio;

	/**
	 * 
	 */
	public MotivosCambioServicioView()
	{
		motivosCambioServicio=MotivosCambioServicioFabrica.crearMotivosCambioServicio();
	}
	
	/**
	 * 
	 * @param codigoInstitucion
	 * @param tipoMotivo
	 * @return
	 */
	public Collection<DtoMotivosCambioServicio> listarMotivosCambioServicio(int codigoInstitucion,String tipoMotivo)
	{
		
		
		try
		{
		
			
			return motivosCambioServicio.listarMotivosCambioServicio(codigoInstitucion, tipoMotivo);
		}
		catch(Exception e)
		{
			throw new NullPointerException("Error "+e.getMessage());
		}
	}
}
