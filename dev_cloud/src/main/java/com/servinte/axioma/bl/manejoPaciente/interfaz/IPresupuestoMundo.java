package com.servinte.axioma.bl.manejoPaciente.interfaz;
import java.util.List;

import com.servinte.axioma.dto.manejoPaciente.EncabezadoRepUsuConDto;
import com.servinte.axioma.dto.manejoPaciente.UsuariosConsumidoresPresupuestoDto;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author davgommo
 * @version 1.0
 * @created 20-jun-2012 02:24:01 p.m.
 */
public interface IPresupuestoMundo {

	/**
	 * 
	 * @param usuariosConsumidores
	 */
	public List<UsuariosConsumidoresPresupuestoDto> consultarUsuariosConsumidoresAutor(String fechaInicial, String fechaFinal, String autorizaciones, String convenio, String viaIngreso, String grupoSeleccionado, String inventarioSeleccionado, String nombreDiagnostico, String valorInicial, String valorFinal,String tipoIdentificacion, String numeroIdentificacion) throws IPSException;

	public EncabezadoRepUsuConDto obtenerEncabezado() throws IPSException;
	

}