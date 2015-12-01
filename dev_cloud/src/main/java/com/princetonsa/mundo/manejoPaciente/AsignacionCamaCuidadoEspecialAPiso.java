package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;
import com.princetonsa.actionform.manejoPaciente.AsignacionCamaCuidadoEspecialAPisoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.AsignacionCamaCuidadoEspecialAPisoDao;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.TrasladoCamas;
import com.princetonsa.mundo.UsuarioBasico;







/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class AsignacionCamaCuidadoEspecialAPiso
{
	private static Logger logger = Logger.getLogger(AsignacionCamaCuidadoEspecialAPiso.class);
	
	
	/*--------------------------------------
	 * INDICES
	 ----------------------------------------*/
	/**
	 * Indices utilizados para el listado
	 */
	public static final String [] indicesListado={"ingreso0_","paciente1_","identPac2_","fechaNacimiento3_","sexoPac4_","fechaHoraOrden5_",
												  "profesional6_","centroCosto7_","tipoMonitoreo8_","diagnosticoPpal9_","convenio10_","cama11_",
												  "idCuenta12_","codigoPersona13_"};
		
	/**
	 * indices utilizados para el manejo del detalle 
	 */
	public static final String [] indicesDetalle={"fecha0","hora1","cCosto2","camaActual3","tipoHbitActual4","cCostoActual5","tipoMonActual6","piso7",
												  "habit8","tipoHabit9","cama10","tipoUsuario11","cCosto12","observ13","usuario14","codCama15",
												  "codigoCamaActual16","codigoTipoMonitoreo17","codigoPaciente18","cuenta19","convenio20","ingreso21",
												  "fechaHoraOrden22","codIngresoCuidadoEspecial23"};
	
	public static final String [] indicesIngCuiEsp ={"fechaFinaliza0","horaFinaliza1","usuarioFinaliza2","estado3","codigoIngresoCuidadoEspecial4"};
	/*-----------------------------------------------------------
	 *         METODOS ASIGNACION CAMA CUIDADO ESPECIAL
	 ------------------------------------------------------------*/

	
	
	/**
	 * Se inicializa el Dao
	 */
	
	public static AsignacionCamaCuidadoEspecialAPisoDao asignacionCamaCuidadoEspecialDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAsignacionCamaCuidadoEspecialAPisoDao();
	}
	
	/**
	 * Metodo encargado de consultasr los pacientes de cuidados especiales
	 * @param connection
	 * @param criterios
	 * @return mapa
	 * -----------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -----------------------------------
	 * -- ingreso0_
	 * -- paciente1_
	 * -- identPac2_
	 * -- fechaNacimiento3_
	 * -- sexoPac4_
	 * -- fechaHoraOrden5_
	 * -- profesional6_
	 * -- centroCosto7_
	 * -- tipoMonitoreo8_
	 * -- diagnosticoPpal9_
	 * -- convenio10_
	 * -- cama11_
	 */
	private HashMap consultaPacientes (Connection connection, HashMap criterios)
	{
		return asignacionCamaCuidadoEspecialDao().consultaPacientes(connection, criterios);
	}
	
	/**
	 * Metodo encargado de consultar la informacion a mostrar en el detalle
	 * @param connection
	 * @param criterios
	 * ----------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ----------------------------
	 * -- idCuenta12_
	 * @return mapa
	 * ---------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ---------------------------
	 * -- camaActual3_
	 * -- tipoHbitActual4_
	 * -- cCostoActual5_
	 * -- tipoMonActual6_
	 * -- codigoCamaActual16_
	 * -- codigoTipoMonitoreo17_
	 */
	private static HashMap consultaDetalle (Connection connection, HashMap criterios)
	{
		return asignacionCamaCuidadoEspecialDao().consultaDetalle(connection, criterios);
	}
	
	/**
	 * Metodo encargado de actualizar el estado de ingreso a cuidados especiales
	 * a FINALIZADA.
	 * @param connection
	 * @param criterios
	 * -----------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ------------------------------
	 * -- fechaFinaliza0
	 * -- horaFinaliza1
	 * -- usuarioFinaliza2
	 * -- estado3
	 * -- ingreso4
	 * @return true/false
	 */
	private static boolean actualizarEstadoIngresoCuiEsp (Connection connection,HashMap criterios)
	{
		return asignacionCamaCuidadoEspecialDao().actualizarEstadoIngresoCuiEsp(connection, criterios);
	}
	
	public static boolean actualizarEstadoIngresoCuiEsp (Connection  connection,String fechaFin,String horaFin, String usuario, String estado,String  codigoIngCuiEsp)
	{
		HashMap criterios = new HashMap ();
		//fecha final
		criterios.put(indicesIngCuiEsp[0] , fechaFin);
		//hora fin 
		criterios.put(indicesIngCuiEsp[1] , horaFin);
		//usuario fin
		criterios.put(indicesIngCuiEsp[2] , usuario);
		//estado
		criterios.put(indicesIngCuiEsp[3] , estado);
		//codigo ingreso cuidado especial
		criterios.put(indicesIngCuiEsp[4] , codigoIngCuiEsp);
		
		return actualizarEstadoIngresoCuiEsp(connection, criterios);
	}
	
	
	
	
	public static HashMap consultaDetalle (Connection connection,String cuenta)
	{
		HashMap criterios = new HashMap ();
		criterios.put(indicesListado[12], cuenta);
		return consultaDetalle(connection, criterios);
	}
	
	public HashMap consultarPaciente (Connection connection)
	{
		//en el momento no se le envian criterios
		//pero se pone para mas adelante
		HashMap criterios = new HashMap ();
		
		criterios=consultaPacientes(connection, criterios);
		
		//se iteran todos los registros para acomodar la edad del paciente
		int numReg =Utilidades.convertirAEntero(criterios.get("numRegistros")+"");
		for (int i=0;i<numReg;i++)
			criterios.put(indicesListado[3]+i, UtilidadFecha.calcularEdadDetallada(UtilidadFecha.conversionFormatoFechaAAp(criterios.get(indicesListado[3]+i)+""), UtilidadFecha.getFechaActual()+""));
					
		return criterios; 
	}
	
	public void cargarDetalle (Connection connection,AsignacionCamaCuidadoEspecialAPisoForm forma, UsuarioBasico usuario,PersonaBasica paciente,HttpServletRequest request)
	{
		//se carga el paciente
		cargarPaciente(connection, Utilidades.convertirAEntero(forma.getListadoPacientes(indicesListado[13]+forma.getIndex())+""), usuario, paciente, request);
		//seconsulta del detalle
		forma.setDetalle(consultaDetalle(connection, forma.getListadoPacientes(indicesListado[12]+forma.getIndex())+""));
		logger.info("\n el detalle --> "+forma.getDetalle());
		//fecha
		forma.setDetalle(indicesDetalle[0],UtilidadFecha.getFechaActual());
		//hora
		forma.setDetalle(indicesDetalle[1],UtilidadFecha.getHoraActual());
		//se cargan los centros de costo.
		forma.setCCosto(Utilidades.getCentrosCosto(connection, ConstantesBD.codigoViaIngresoHospitalizacion+"", usuario.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt(),"",ConstantesBD.codigoNuncaValido,false,true,false));
		//se carga el usuario que efectúa esta admision
		forma.setDetalle(indicesDetalle[14], usuario.getNombreUsuario()+" ("+usuario.getLoginUsuario()+")");
		
	}
	
	
	private static ActionErrors trasladoPaciente (AsignacionCamaCuidadoEspecialAPisoForm forma, Connection con, PersonaBasica paciente, UsuarioBasico medico) throws SQLException
	{	
	    TrasladoCamas trasladoCamas= new TrasladoCamas();
	    
	    ActionErrors errores = new ActionErrors(); 
	      /**Iniciamos la transaccion**/
	    UtilidadBD.iniciarTransaccion(con);
	    
	    /**#########################################################################
		  * 			VARIABLES PARA EL TRASLADO DE CAMAS
		  ############################################################################*/
	    
	    /**Para tomar el estado definido en valores por defecto para una cama despues de desocuparla**/
	    int tmp=Integer.parseInt(ValoresPorDefecto.getCodigoEstadoCama(medico.getCodigoInstitucionInt()));
	     /**se toma el codigo del paciente**/
	    int codigoPaciente=Utilidades.convertirAEntero(forma.getDetalle(indicesDetalle[18])+"");
	    /**se toma la cuenta**/
	    int cuenta=Utilidades.convertirAEntero(forma.getDetalle(indicesDetalle[19])+"");
	   	 int codigoCamaActual = Utilidades.convertirAEntero(forma.getDetalle(indicesDetalle[16])+"");
		 /**se toma la cama nueva**/
		 int codigoCamaNueva = Utilidades.convertirAEntero(forma.getDetalle(indicesDetalle[15])+"");
	    /**se toma la fecha del traslado**/
		 String fechaTraslado =forma.getDetalle(indicesDetalle[0])+"";
		 /**se toma la hora del traslado**/
		 String horaTraslado =forma.getDetalle(indicesDetalle[1])+"";   
		 /**el convenio de mayor prioridad**/
		 int convenio=Utilidades.convertirAEntero(forma.getDetalle(indicesDetalle[20])+"");
		 /** las observaciones **/
		 String observaciones=forma.getDetalle(indicesDetalle[13]).toString();
		 /**#########################################################################
		  * HASTA AQUI VAN LAS VARIABLES PARA EL TRASLADO DE CAMAS
		  ############################################################################*/
		 
	    /**Validacion de concurrencia en el momento de asignar la cama**/
	    ArrayList filtro = new ArrayList();
	    filtro.add(codigoCamaActual+"");
	    UtilidadBD.bloquearRegistro(con, BloqueosConcurrencia.bloqueoCama, filtro);
	    filtro.clear();
	    filtro.add(codigoCamaNueva+"");
	    UtilidadBD.bloquearRegistro(con, BloqueosConcurrencia.bloqueoCama, filtro);
	    if(Utilidades.obtenerCodigoEstadoCama(con,codigoCamaNueva)!=ConstantesBD.codigoEstadoCamaDisponible && Utilidades.obtenerCodigoEstadoCama(con,codigoCamaNueva)!=ConstantesBD.codigoEstadoCamaReservada)
		{
			 
            errores.add("Cama estado diferente", new ActionMessage("error.cama.estadoDiferenteDisponible","SELECCIONADA"));
            UtilidadBD.abortarTransaccion(con);
            UtilidadBD.closeConnection(con);            
            return errores; 
		}
	    
	    /**Actualizo la fecha y hora de finalizacion de ocupacion de la cama actual del paciente**/
	    trasladoCamas.actualizarFechaHoraFinalizacionNoTransaccional(con, cuenta, UtilidadFecha.conversionFormatoFechaABD(fechaTraslado),horaTraslado,"");
	    
	    /**Inserscion del tralado de cama**/
	    trasladoCamas.insertarTrasladoCamaPaciente(con, UtilidadFecha.conversionFormatoFechaABD(fechaTraslado), horaTraslado,codigoCamaNueva,codigoCamaActual,medico.getCodigoInstitucionInt(), medico.getLoginUsuario(), codigoPaciente,cuenta,convenio,observaciones);
	    
	    /**Modifico el estado de la cama anterior al estado definido en Parametros Generales**/
    	trasladoCamas.modificarEstadoCama(con,tmp,codigoCamaActual,medico.getCodigoInstitucionInt());
    	
	    /**Modifico el estado de la cama a la cual se le traslada el paciente a estado Ocupada**/
	    trasladoCamas.modificarEstadoCama(con,ConstantesBD.codigoEstadoCamaOcupada,codigoCamaNueva,medico.getCodigoInstitucionInt());
	    
	    UtilidadBD.finalizarTransaccion(con);
	    		
		return 	errores;
		
	}
	
	public static ActionErrors guardar (Connection connection,AsignacionCamaCuidadoEspecialAPisoForm forma,  PersonaBasica paciente, UsuarioBasico medico,UsuarioBasico usuario,HttpServletRequest request) throws SQLException
	{
		  ActionErrors errores = new ActionErrors(); 
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		logger.info("----->INICIANDO TRANSACCION ....");
		
		////////////////////////////////////////////////////
		//pasar el estado de ingreso de cuidados especiales a  finalizado
		transacction=actualizarEstadoIngresoCuiEsp(connection, UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), medico.getLoginUsuario(), ConstantesIntegridadDominio.acronimoEstadoFinalizado, forma.getDetalle(indicesDetalle[23])+"");
		
		//cambio de centro de costo al que se encuentra el paciente
		if (transacction)
			transacction=Utilidades.actualizarAreaCuenta(connection, Utilidades.convertirAEntero(forma.getDetalle(indicesDetalle[19])+"") ,Utilidades.convertirAEntero(forma.getDetalle(indicesDetalle[2])+""));
		//se guarda el traslado de la cama
		if (transacction)
			errores=trasladoPaciente(forma, connection, paciente, medico);
			
		if (!errores.isEmpty())
			transacction=false;
			
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
			logger.info("----->TRANSACCION AL 100% ....");
			forma.setOperacionTrue(true);
			//se carga el paciente
			cargarPaciente(connection, Utilidades.convertirAEntero(forma.getListadoPacientes(indicesListado[13]+forma.getIndex())+""), usuario, paciente, request);
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}
		
		
		
		return errores;
	}
	
	
	/**
	 * metodo encargado del ordenamiento de la forma
	 * @param forma
	 */
	public static void accionOrdenarMapa(AsignacionCamaCuidadoEspecialAPisoForm forma)
	{
		
		int numReg = Utilidades.convertirAEntero(forma.getListadoPacientes("numRegistros")+"");
		forma.setListadoPacientes(Listado.ordenarMapa(indicesListado, forma.getPatronOrdenar(), forma.getUltimoPatron(),forma.getListadoPacientes() , numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setListadoPacientes("numRegistros", numReg);
				
	}
	
	/**
	 * Metodo encargado de cargar el paciente en sesion.
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 */
	public static void cargarPaciente (Connection connection,int codigoPersona,UsuarioBasico usuario,PersonaBasica paciente,HttpServletRequest request )
	{
		
		paciente.setCodigoPersona(codigoPersona);
		
		UtilidadesManejoPaciente.cargarPaciente(connection, usuario, paciente, request);
			
	}
	
}