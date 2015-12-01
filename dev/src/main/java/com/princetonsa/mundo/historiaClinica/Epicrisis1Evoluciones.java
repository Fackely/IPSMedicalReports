package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;

import util.ConstantesBD;

import com.princetonsa.dto.historiaClinica.DtoEvolucion;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;

/**
 * 
 * @author wilson
 *
 */
public class Epicrisis1Evoluciones 
{
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param codigoFuncionalidad
	 * @param evolucion
	 * @return
	 */
	public static DtoPlantilla cargarPlantillaEvolucion(Connection con, int institucion, int evolucion)
	{
		DtoPlantilla plantilla= new DtoPlantilla(); 
		plantilla=Plantillas.cargarPlantillaXEvolucion(	con,
														institucion,
														ConstantesBD.codigoNuncaValido /*paciente.getCodigoArea()*/, 
														ConstantesBD.codigoNuncaValido /*paciente.getCodigoSexo()*/, 
														Plantillas.obtenerCodigoPlantillaXEvolucion(con, evolucion) /*codigoPlantilla*/,
														true /*consultarRegistro*/,
														ConstantesBD.codigoNuncaValido /*codigoPaciente*/,
														ConstantesBD.codigoNuncaValido /*codigoIngreso*/, 
														evolucion,
														ConstantesBD.codigoNuncaValido,
														ConstantesBD.codigoNuncaValido,
														false);
		return plantilla;
	}
	
	
	/**
	 * 
	 * @param evolucion
	 * @param con
	 * @return
	 */
	public static DtoEvolucion cargarMundoEvolucion(int evolucion, Connection con)
	{
		DtoEvolucion dto= new DtoEvolucion();
		dto=Evoluciones.cargarEvolucion(con, evolucion+"");
		return dto;
	}
}
