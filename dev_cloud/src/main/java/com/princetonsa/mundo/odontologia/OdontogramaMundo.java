package com.princetonsa.mundo.odontologia;



import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import util.reportes.InfoEncabezadoReporte;


/**
 * 
 * @author Edgar Carvajal
 * Este Mundo ES DIFERNTE  A LOS ODONTOLGRAMA DE EVOLUCION Y VALORACION.
 * ESTA CLASE SE CREA PARA DISMINUIR EL ACOPLAMENTO ENTRE FUNCIONALIDDES 
 * PLANTRATIENTO Y ODONTOGRAMA
 *
 */
public class OdontogramaMundo 
{
	

	
	
	/**
	 * METODO PARA CARGAR LA INFORMACION RESPECTIVA DEL ENCABEZADO DEL REPORTE ODONTOGRAMA
	 * @author Edgar Carvajal Ruiz
	 * @param infoPlan
	 * @param institucionBasica
	 */
	public static  InfoEncabezadoReporte cargarInformacionEncabezadoReporte(DtoPlanTratamientoOdo dtoPla , InstitucionBasica institucionBasica , PersonaBasica paciente , UsuarioBasico usuario)
	{
		InfoEncabezadoReporte infoEncabezado = new InfoEncabezadoReporte();
		
		infoEncabezado.setRazonSocial(institucionBasica.getRazonSocial());
		//infoEncabezado.setLogoInstiucion(institucionBasica.getLogoReportes());
		infoEncabezado.setNit(institucionBasica.getNit());
		infoEncabezado.setDireccion(institucionBasica.getDireccion());
		infoEncabezado.setTelefono(institucionBasica.getTelefono());
		
		infoEncabezado.setLogoInstiucion(institucionBasica.getLogoReportes());
		//infoEncabezado.set
		
		
		infoEncabezado.setNombrePaciente(paciente.getPrimerApellido()+" "+paciente.getSegundoApellido()+" "+paciente.getPrimerNombre()+" "+paciente.getSegundoNombre());
		infoEncabezado.setEdadPaciente(String.valueOf(paciente.getEdad()));
		infoEncabezado.setIdentificacionPaciente(paciente.getTipoIdentificacionPersona(Boolean.FALSE) +" "+paciente.getNumeroIdentificacionPersona());
		infoEncabezado.setHoraRegistro(dtoPla.getHoraGrabacion());
		infoEncabezado.setFechaRegistro(dtoPla.getFechaGrabacion());
		
		//infoEncabezado.setDigitoVerficacion(paciente.get)
		
		infoEncabezado.setNombreProfesionalSalud(usuario.getNombreUsuario());
		infoEncabezado.setIngresoPaciente(String.valueOf(dtoPla.getIngreso()));
		infoEncabezado.setSexoPaciente(paciente.getSexo());
		
		infoEncabezado.setNombreCentroAtencion(usuario.getCentroAtencion());
		
		
		//infoEncabezado.setNombreProfesionalSalud(usuario.getEspecialidadesMedico());  // TODO CARGAR ESPECIALIDADES 
		
		return infoEncabezado;
	}

}
