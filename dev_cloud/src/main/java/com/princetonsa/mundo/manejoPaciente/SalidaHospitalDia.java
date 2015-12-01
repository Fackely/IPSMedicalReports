package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.ObjetoReferencia;
import util.UtilidadFecha;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.actionform.cargos.CargosDirectosForm;
import com.princetonsa.actionform.manejoPaciente.SalidaHospitalDiaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.ReingresoSalidaHospiDiaDao;
import com.princetonsa.dto.manejoPaciente.DtoReingresoSalidaHospiDia;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.EstanciaAutomatica;
import com.princetonsa.mundo.medicamentos.DespachoMedicamentos;
import com.princetonsa.mundo.solicitudes.Solicitud;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class SalidaHospitalDia {
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private ReingresoSalidaHospiDiaDao objetoDao;
	
	/**
	 * inicializa el acceso a la base de datos obteniendo el respectivo DAO
	 * @param tipoBD
	 * @return
	 */
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getReingresoSalidaHospiDiaDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
	}
	
	/**
	 * Constructor de la clase
	 *
	 */
	public SalidaHospitalDia()
	{
		init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Metodo para actualizar el registro del paciente de hospital dia como de salida
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean actualizarSalidaHospitalDia(Connection con, DtoReingresoSalidaHospiDia dto)
	{
		return objetoDao.updateSalida(con, dto); 
	}
	
	/**
	 * Metodo para listar los pacientes registrados como permanencia diurna
	 * @param con
	 * @param esSalida
	 * @param codigoCentroAtencion
	 * @return
	 */
	public static HashMap listadoPacientesReingresoOSalida(Connection con, HashMap criteriosBusqueda)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReingresoSalidaHospiDiaDao().listadoPacientesReingresoOSalida(con, criteriosBusqueda);
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param codigoCuenta
	 * @param paciente
	 * @param user
	 * @param tipoSolicitud
	 * @param centroCostoSolicitado
	 * @param numeroAutorizacion
	 * @return
	 * @throws SQLException
	 */
	public int insertarSolicitud(Connection con, SalidaHospitalDiaForm forma,PersonaBasica paciente) 
	{
		int centroCosto=UtilidadesHistoriaClinica.obtenerCentroCostoCuenta(con, forma.getCuenta());
		//se instancia el objeto solicitud
		Solicitud solicitud=new Solicitud();
		solicitud.clean();
		solicitud.setFechaSolicitud(forma.getFecha());
		solicitud.setHoraSolicitud(forma.getHora());
		solicitud.setTipoSolicitud(new InfoDatosInt(ConstantesBD.codigoTipoSolicitudEstancia));
		solicitud.setEspecialidadSolicitante(new InfoDatosInt(ConstantesBD.codigoEspecialidadMedicaNinguna));
		solicitud.setOcupacionSolicitado(new InfoDatosInt(ConstantesBD.codigoOcupacionMedicaNinguna));
		solicitud.setCentroCostoSolicitante(new InfoDatosInt(centroCosto));
		solicitud.setCentroCostoSolicitado(new InfoDatosInt(centroCosto));
	    solicitud.setCodigoCuenta(forma.getCuenta());
	    solicitud.setCobrable(true);
	    solicitud.setVaAEpicrisis(false);
	    solicitud.setUrgente(false);
	    //solicitud.setEstadoFacturacion(new InfoDatosInt(ConstantesBD.codigoEstadoFPendiente));
	    try
	    {
	        int numeroSolicitud=solicitud.insertarSolicitudGeneralTransaccional(con, ConstantesBD.continuarTransaccion);
	        //se instancia este objeto para cambiar el estado Historia Clinica de la solicitud
	        DespachoMedicamentos despacho=new DespachoMedicamentos();
	        despacho.setNumeroSolicitud(numeroSolicitud);
	        //se cambia el estado Historia Clínica de la solicitud
	        int resp=despacho.cambiarEstadoMedicoSolicitudTransaccional(con,ConstantesBD.continuarTransaccion,ConstantesBD.codigoEstadoHCCargoDirecto/*,""*/);
	        if(resp<=0)
	        	return 0;
	        else
	        	return numeroSolicitud;
	    }
	    catch(SQLException sqle)
	    {
			return 0;
	    }
	}
	
	/**
	 * Metodo que valida si para la fecha el paciente ya tiene salida
	 * @param con
	 * @param cuenta
	 * @param fecha
	 * @return
	 */
	public boolean existeSalidaXFecha(Connection con,int cuenta,String fecha)
	{
		return objetoDao.existeSalidaXFecha(con, cuenta, fecha);
	}
}
