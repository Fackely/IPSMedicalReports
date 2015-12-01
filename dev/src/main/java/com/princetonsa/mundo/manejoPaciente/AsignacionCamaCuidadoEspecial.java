package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.BloqueosConcurrencia;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;


import com.princetonsa.actionform.manejoPaciente.AsignacionCamaCuidadoEspecialAPisoForm;
import com.princetonsa.actionform.manejoPaciente.AsignacionCamaCuidadoEspecialForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.AsignacionCamaCuidadoEspecialAPisoDao;
import com.princetonsa.dao.manejoPaciente.AsignacionCamaCuidadoEspecialDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseAsignacionCamaCuidadoEspecialDao;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.TrasladoCamas;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Luis Gabriel Chavez Salazar.
 * lgchavez@princetonsa.com
 */

public class AsignacionCamaCuidadoEspecial
{
	private Logger logger = Logger.getLogger(AsignacionCamaCuidadoEspecial.class);
	
	
	/*--------------------------------------
	 * INDICES
	 ----------------------------------------*/
	public static final String [] indicesListado={"ingreso0_","paciente1_","identPac2_","fechaNacimiento3_","sexoPac4_","fechaHoraOrden5_",
												  "profesional6_","centroCosto7_","tipoMonitoreo8_","diagnosticoPpal9_","convenio10_","cama11_"};
	

	/*-----------------------------------------------------------
	 *         METODOS ASIGNACION CAMA CUIDADO ESPECIAL
	 ------------------------------------------------------------*/
	
	/**
	 * Se inicializa el Dao
	 */
	
	public static AsignacionCamaCuidadoEspecialDao asignacionCamaCuidadoEspecialDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAsignacionCamaCuidadoEspecialDao();
	}
	
	private HashMap consultaPacientes (Connection connection, int tipoMonitoreo)
	{
		return asignacionCamaCuidadoEspecialDao().consultaPacientes(connection, tipoMonitoreo);
	}
	
	
	
	
	public void cargarDetalle (Connection connection,AsignacionCamaCuidadoEspecialForm forma, UsuarioBasico usuario)
	{
		//seconsulta del detalle
		
		
		
		forma.setDetalleIngresar(consultaDetalle(connection, forma.getListadoPacientes("idCuenta_"+forma.getIndex())+"", Utilidades.convertirAEntero(forma.getMonitoreo())));
		//fecha
		forma.setDetalleIngresar("fecha",UtilidadFecha.getFechaActual());
		//hora
		forma.setDetalleIngresar("hora",UtilidadFecha.getHoraActual());
		//se cargan los centros de costo.
		forma.setCCosto(Utilidades.getCentrosCosto(connection, ConstantesBD.codigoViaIngresoHospitalizacion+"", usuario.getCodigoCentroAtencion(), usuario.getCodigoInstitucionInt(), ConstantesBD.tipoPacienteHospitalizado, Integer.parseInt(forma.getMonitoreo()),true,false,false));
		//se carga el usuario que efectúa esta admision
		forma.setDetalleIngresar("usuario", usuario.getNombreUsuario()+"("+usuario.getLoginUsuario()+")");
		
	}
	
	public static HashMap consultaDetalle (Connection connection,String cuenta, int monitoreo)
	{
		HashMap criterios = new HashMap ();
		criterios.put("idCuenta_", cuenta);
		criterios.put("tipoMonitoreo", monitoreo);
		return consultaDetalle(connection, criterios);
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
	 * -- camaActual_
	 * -- tipoHbitActual_
	 * -- cCostoActual_
	 * -- tipoMonActual_
	 * -- codigoCamaActual_
	 * -- codigoTipoMonitoreo_
	 */
	private static HashMap consultaDetalle (Connection connection, HashMap criterios)
	{
		return asignacionCamaCuidadoEspecialDao().consultaDetalle(connection, criterios);
	}
	
	/**
	 * Metodo que registra en (ingresos_cuidados_especiales) con estado activo e indicativo manual
	 * @param connection
	 * @param datos
	 * @return
	 */
	public int guardarIngresoCuidadosEspeciales(Connection connection, HashMap datos)
	{
		return asignacionCamaCuidadoEspecialDao().guardarIngresoCuidadosEspeciales(connection, datos);
	}
	
	
	
	public static ActionErrors trasladoPaciente (AsignacionCamaCuidadoEspecialForm forma, Connection con, PersonaBasica paciente, UsuarioBasico medico) throws SQLException
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
	    int codigoPaciente=Utilidades.convertirAEntero(forma.getDetalleIngresar("codigoPaciente_0")+"");
	    /**se toma la cuenta**/
	    int cuenta=Utilidades.convertirAEntero(forma.getDetalleIngresar("cuenta_0")+"");
	   	 int codigoCamaActual = Utilidades.convertirAEntero(forma.getDetalleIngresar("codigoCamaActual_0")+"");
		 /**se toma la cama nueva**/
		 int codigoCamaNueva = Utilidades.convertirAEntero(forma.getDetalleIngresar("codCama")+"");
	    /**se toma la fecha del traslado**/
		 String fechaTraslado =forma.getDetalleIngresar("fecha")+"";
		 /**se toma la hora del traslado**/
		 String horaTraslado =forma.getDetalleIngresar("hora")+"";   
		 /**el convenio de mayor prioridad**/
		 int convenio=Utilidades.convertirAEntero(forma.getDetalleIngresar("convenio_0")+"");
		 
		 String observaciones=forma.getDetalleIngresar("observ").toString();
		 
		 
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}