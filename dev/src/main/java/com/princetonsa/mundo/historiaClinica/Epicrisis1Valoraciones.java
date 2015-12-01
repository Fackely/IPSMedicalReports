package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;

import util.ConstantesBD;

import com.princetonsa.dto.historiaClinica.DtoValoracion;
import com.princetonsa.dto.historiaClinica.DtoValoracionHospitalizacion;
import com.princetonsa.dto.historiaClinica.DtoValoracionUrgencias;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * 
 * @author wilson
 *
 */
public class Epicrisis1Valoraciones 
{
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param codigoFuncionalidad
	 * @param numeroSolicitud
	 * @return
	 */
	public static DtoPlantilla cargarPlantillaValoracion(Connection con, int institucion, int codigoFuncionalidad, int numeroSolicitud)
	{
		
		DtoPlantilla plantilla= new DtoPlantilla();
		
		
		plantilla=	Plantillas.cargarPlantillaXSolicitud(
															con, 
															institucion, 
															codigoFuncionalidad, 
															ConstantesBD.codigoNuncaValido /*cc*/, 
															ConstantesBD.codigoNuncaValido /*sexo*/, 
															ConstantesBD.codigoNuncaValido, 
															//Si no hay centro de costo se intenta buscar el consecutivo de la plantilla 
															Plantillas.obtenerCodigoPlantillaXIngreso(con, ConstantesBD.codigoNuncaValido/*codigoIngreso*/, ConstantesBD.codigoNuncaValido /*codigoPaciente*/, numeroSolicitud),
															true, 
															ConstantesBD.codigoNuncaValido/*codigoPaciente*/, 
															ConstantesBD.codigoNuncaValido/*codigoIngreso*/, 
															numeroSolicitud,
															ConstantesBD.codigoNuncaValido, // codigo Sexo
															ConstantesBD.codigoNuncaValido, //dias edad paciente
															false);
		return plantilla;	
	}
	
	/**
	 * 
	 * @param dtoPlantilla
	 * @param numeroSolicitud
	 * @param con
	 * @param paciente
	 * @return
	 */
	public static DtoValoracion cargarMundoValInterconsulta(DtoPlantilla dtoPlantilla, int numeroSolicitud, Connection con, PersonaBasica paciente)
	{
		DtoValoracion dtoValConsulta= new DtoValoracion();
		Valoraciones mundoValoracion = new Valoraciones();
		mundoValoracion.setPlantilla(dtoPlantilla);
		mundoValoracion.cargarInterconsulta(con, paciente, numeroSolicitud+"");
		dtoValConsulta= mundoValoracion.getValoracion();
		//respuestaForm.getAdvertencias().addAll(mundoValoracion.getAdvertencias());
		//respuestaForm.setNumeroAutorizacion(mundoValoracion.getNumeroAutorizacion());
		return dtoValConsulta;
	}
	
	/**
	 * 
	 * @param dtoPlantilla
	 * @param numeroSolicitud
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param codigoCita
	 * @return
	 */
	public static DtoValoracion cargarMundoValConsulta(DtoPlantilla dtoPlantilla, int numeroSolicitud, Connection con, UsuarioBasico usuario, PersonaBasica paciente, int codigoCita)
	{
		DtoValoracion dtoValConsulta= new DtoValoracion();
		Valoraciones mundoValoracion = new Valoraciones();
		mundoValoracion.setPlantilla(dtoPlantilla);
		mundoValoracion.cargarConsulta(con, paciente,numeroSolicitud+"", codigoCita+"");
		dtoValConsulta= mundoValoracion.getValoracion();
		//valoracionForm.setNumeroAutorizacion(mundoValoracion.getNumeroAutorizacion());
		//valoracionForm.setFechaConsulta(mundoValoracion.getFechaConsulta());
		//valoracionForm.setHoraConsulta(mundoValoracion.getHoraConsulta());
		return dtoValConsulta;
	}
	
	
	/**
	 * 
	 * @param dtoPlantilla
	 * @param numeroSolicitud
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	public static DtoValoracionUrgencias cargarMundoValUrgencias(DtoPlantilla dtoPlantilla, int numeroSolicitud, Connection con, UsuarioBasico usuario, PersonaBasica paciente)
	{
		DtoValoracionUrgencias dtoValUrg= new DtoValoracionUrgencias();
		Valoraciones mundoValoracion = new Valoraciones();
		mundoValoracion.setPlantilla(dtoPlantilla);
		mundoValoracion.setNumeroSolicitud(numeroSolicitud+"");
		mundoValoracion.cargarUrgencias(con,usuario,paciente,paciente.getCodigoPersona(),false);
		dtoValUrg= mundoValoracion.getValoracionUrgencias();
		return dtoValUrg;
	}
	
	/**
	 * 
	 * @param dtoPlantilla
	 * @param numeroSolicitud
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	public static DtoValoracionHospitalizacion cargarMundoValHospitalizacion(DtoPlantilla dtoPlantilla, int numeroSolicitud, Connection con, UsuarioBasico usuario, PersonaBasica paciente)
	{
		DtoValoracionHospitalizacion dtoValHosp= new DtoValoracionHospitalizacion();
		Valoraciones mundoValoracion = new Valoraciones();
		mundoValoracion.setPlantilla(dtoPlantilla);
		mundoValoracion.setNumeroSolicitud(numeroSolicitud+"");
		mundoValoracion.cargarHospitalizacion(con,usuario,paciente,false);
		dtoValHosp=mundoValoracion.getValoracionHospitalizacion();
		return dtoValHosp;
	}

	
}
