package com.princetonsa.mundo.parametrizacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.parametrizacion.AccesosVascularesHADao;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.ordenesmedicas.procedimientos.RespuestaProcedimientos;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudProcedimiento;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * 
 * @author wilson
 *
 */
public class AccesosVascularesHA 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static AccesosVascularesHADao aDao;
	
	/**
	 * Maneja los logs del módulo de control de Solicitudes
	 */
	private static Logger logger = Logger.getLogger(AccesosVascularesHA.class);
	
	/**
	 * constructor de la clase
	 *
	 */
	public AccesosVascularesHA()  
	{
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			aDao = myFactory.getAccesosVascularesHADao();
			wasInited = (aDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
    public static double insertar(Connection con, HashMap<Object, Object> mapa)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAccesosVascularesHADao().insertar(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param viaAerea
     * @param articulo
     * @return
     */
    public static boolean eliminar(Connection con, double codigoPKAccesoVascularHA)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAccesosVascularesHADao().eliminar(con, codigoPKAccesoVascularHA);
    }
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public static boolean modificar(Connection con, HashMap<Object, Object> mapa)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAccesosVascularesHADao().modificar(con, mapa);
    }
    
    /**
     * 
     * @param con
     * @param centroCosto
     * @param institucion
     * @return
     */
    public static HashMap<Object, Object> cargarTiposAccesoVascularCCInst( Connection con, int centroCosto, int institucion)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAccesosVascularesHADao().cargarTiposAccesoVascularCCInst(con, centroCosto, institucion);
    }
    
    /**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap<Object, Object> obtenerListadoAccesosVasculares(Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAccesosVascularesHADao().obtenerListadoAccesosVasculares(con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param articulo
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
    public static HashMap<Object, Object> cargarLocalizacionesXTipoAcceso( Connection con, int tipoAccesoVascular, int centroCosto, int institucion)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAccesosVascularesHADao().cargarLocalizacionesXTipoAcceso(con, tipoAccesoVascular, centroCosto, institucion);
    }
    
    /**
     * 
     * @param con
     * @param fecha
     * @param hora
     * @param codigoPKAccesoVascularHA
     * @return
     */
	public static boolean existeAccesoVascularHAFechaHora(String fecha, String hora, double codigoPKAccesoVascularHA)
	{
		Connection con= UtilidadBD.abrirConexion();
		boolean w= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAccesosVascularesHADao().existeAccesoVascularHAFechaHora(con, fecha, hora, codigoPKAccesoVascularHA);
		UtilidadBD.closeConnection(con);
		return w;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPKAccesoVascularHA
	 * @return
	 */
	public static boolean actualizarGeneroConsumo(Connection con, double codigoPKAccesoVascularHA,int codigoDetMateQx)
	{
		HashMap parametros = new HashMap();		
		parametros.put("codigoPKAccesoVascularHA", codigoPKAccesoVascularHA);
		parametros.put("codigoDetMateQx", codigoDetMateQx);
		logger.info("parametros >> "+parametros);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAccesosVascularesHADao().actualizarGeneroConsumo(con,parametros);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPKAccesoVascularHA
	 * @return
	 */
	public static boolean actualizarGeneroOrden(Connection con, double codigoPKAccesoVascularHA)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAccesosVascularesHADao().actualizarGeneroOrden(con, codigoPKAccesoVascularHA);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param usuario
	 * @param paciente
	 * @return
	 */
	public static boolean generarOrdenProcedimientosAccesosVasculares(Connection con, int numeroSolicitud, UsuarioBasico usuario, PersonaBasica paciente) throws IPSException
	{
		HashMap<Object, Object> mapa= obtenerListadoAccesosVasculares(con, numeroSolicitud);
		
		for(int w=0; w<Integer.parseInt(mapa.get("numRegistros").toString()); w++)
		{
			int servicio= Utilidades.convertirAEntero(mapa.get("servicio_"+w)+"");
			boolean generoConsumo= UtilidadTexto.getBoolean(mapa.get("generoconsumo_"+w)+"");
			
			if(servicio>0 && !generoConsumo)
			{
				double codigoPKAccesoVascularHA=Double.parseDouble(mapa.get("cod_acc_vascular_hoja_anes_"+w)+"");
				String fechaAccesoVascularAp=mapa.get("fecha_"+w)+"";
				String horaAccesoVascular=mapa.get("hora_"+w)+"";
				
				if(!generarOrdenProcedimientosAutomatica(con, codigoPKAccesoVascularHA, paciente, usuario, servicio, fechaAccesoVascularAp, horaAccesoVascular))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigoPKAccesoVascularHA
	 * @param fechaSolicitud
	 * @param horaSolicitud
	 * @param paciente
	 * @param usuario
	 * @param servicio
	 * @param fechaAccesoVascularAp
	 * @param horaAccesoVascular
	 * @return
	 */
	public static boolean generarOrdenProcedimientosAutomatica(	Connection con, 
																double codigoPKAccesoVascularHA, 
																PersonaBasica paciente,
																UsuarioBasico usuario,
																int servicio,
																String fechaAccesoVascularAp,
																String horaAccesoVascular) throws IPSException
	{
		//primero debemos insertar una solicitud basica
		Solicitud objectSolicitud= new Solicitud();
	    int numeroSolicitudInsertado=0;
	    
	    objectSolicitud.setFechaSolicitud(fechaAccesoVascularAp);
	    objectSolicitud.setHoraSolicitud(horaAccesoVascular);
	    objectSolicitud.setTipoSolicitud(new InfoDatosInt(ConstantesBD.codigoTipoSolicitudProcedimiento));
	    //objectSolicitud.setNumeroAutorizacion("");
	    objectSolicitud.setEspecialidadSolicitante(new InfoDatosInt(ConstantesBD.codigoEspecialidadMedicaNinguna));
	    objectSolicitud.setOcupacionSolicitado(new InfoDatosInt(ConstantesBD.codigoOcupacionMedicaNinguna));
	    
	    //segun margarita, es ambos es el codigo de area, lo cual creo es incorrecto
	    objectSolicitud.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea()));
	    objectSolicitud.setCentroCostoSolicitado(new InfoDatosInt(paciente.getCodigoArea()));
	    
	    objectSolicitud.setCodigoCuenta(paciente.getCodigoCuenta());
	    objectSolicitud.setCobrable(true);
	    objectSolicitud.setVaAEpicrisis(false);
	    objectSolicitud.setUrgente(false);
	    //primero lo inserto como pendiente, pero si mas adelante es exitoso el cargo entonces le hago un update a  cargada
	    objectSolicitud.setEstadoHistoriaClinica(new InfoDatosInt(ConstantesBD.codigoEstadoHCSolicitada));
	    try
	    { 
	        numeroSolicitudInsertado=objectSolicitud.insertarSolicitudGeneralTransaccional(con, ConstantesBD.continuarTransaccion);
	    }
	    catch(SQLException sqle)
	    {
	        sqle.printStackTrace();
			numeroSolicitudInsertado= 0;
	    }
	    
	    logger.info("\n NUMERO SOLICITUD INSERTADA-->"+numeroSolicitudInsertado);
	    
	    if(numeroSolicitudInsertado<=0)
	    	return false;
	    
	    //si llega aca entonces tenemos que insertar la solicitud de procedimientos
	    SolicitudProcedimiento solProc= new SolicitudProcedimiento();
	    
	    try 
	    {
			if(!SolicitudProcedimiento.insertarSolProc(con, numeroSolicitudInsertado, servicio, ""/*nombreOtros*/, ""/*comentarios*/, solProc.numeroDocumentoSiguiente(con), false/*esMultiple*/, -1 /*frecuencia*/, 0 /*tipoFrecuencia*/, ConstantesBD.codigoNuncaValido /*finalidad*/, false /*respuestaMultiple*/, true /*finalizadaRespuesta*/, paciente.getCodigoCuenta(), false /*finalizar*/, "" /*portatil*/))
	    		return false;
		} 
	    catch (Exception e) 
	    {
			e.printStackTrace();
		}
	    
	    //GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA 
	    Cargos cargos= new Cargos();
	    cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
																			usuario, 
																			paciente, 
																			true/*dejarPendiente*/, 
																			numeroSolicitudInsertado, 
																			ConstantesBD.codigoTipoSolicitudProcedimiento /*codigoTipoSolicitudOPCIONAL*/, 
																			paciente.getCodigoCuenta(), 
																			ConstantesBD.codigoNuncaValido/*codigoCentroCostoEjecutaOPCIONAL*/, 
																			ConstantesBD.codigoNuncaValido/*codigoServicioOPCIONAL*/, 
																			ConstantesBD.codigoNuncaValido/*cantidadServicioOPCIONAL*/, 
																			ConstantesBD.codigoNuncaValido/*valorTarifaOPCIONAL*/, 
																			ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
																			/*""--numeroAutorizacionOPCIONAL*/
																			""/*esPortatil*/,false,"",
																			"" /*subCuentaCoberturaOPCIONAL*/);
	    
	    //LUEGO SE INSERTA LA RESPUESTA
	    RespuestaProcedimientos respProc= new RespuestaProcedimientos();
	    respProc.setResultados("ACCESOS VASCULARES");
	    respProc.setObservaciones("ACCESOS VASCULARES");
	    respProc.setNumeroSolicitud(numeroSolicitudInsertado+"");
	    respProc.setFechaEjecucion(fechaAccesoVascularAp);
	    respProc.setCodigoTipoRecargo(ConstantesBD.codigoTipoRecargoSinRecargo);
	    respProc.setComentariosHistoriaClinica("");
	    respProc.setHoraEjecucion(horaAccesoVascular);
	    
	    if(!respProc.insertarRespuestaYGenerarCargo(con, usuario, usuario.getLoginUsuario(), paciente, null))
	    	return false;
	    
	    RespuestaProcedimientos.finaizarRespuestaSolProcedimiento(con, numeroSolicitudInsertado+"", ConstantesBD.acronimoSi);
		
		//sexto se cambia el estado a respondida
		Solicitud.cambiarEstadosSolicitudStatico(con, numeroSolicitudInsertado, 0 /*AL ENVIAR EL 0 NO CAMBIA EL ESTADO DEL CARGO*/, ConstantesBD.codigoEstadoHCRespondida);
		
		//se cambia a genero cargo el acceso vascular
		if(!actualizarGeneroOrden(con, codigoPKAccesoVascularHA))
			return false;
		
		return true;
	    
	}
	
}
