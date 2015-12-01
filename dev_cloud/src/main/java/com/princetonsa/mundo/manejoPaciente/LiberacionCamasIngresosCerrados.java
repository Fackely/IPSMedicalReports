package com.princetonsa.mundo.manejoPaciente;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import com.princetonsa.actionform.manejoPaciente.LiberacionCamasIngresosCerradosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.LiberacionCamasIngresosCerradosDao;
import com.princetonsa.mundo.Cama;
import com.princetonsa.mundo.TrasladoCamas;
import com.princetonsa.mundo.UsuarioBasico;






/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class LiberacionCamasIngresosCerrados
{

	private static Logger logger = Logger.getLogger(LiberacionCamasIngresosCerrados.class);
	/*----------------------------------------------------------------
	 * indices para la liberacion de camas de ingresos cerrados
	 -----------------------------------------------------------------*/
	public static String [] indicesListado = {"fechaIngreso0_","id1_","consecutivo2_","fechaCierre3_","codigoCama4_","nombreCama5_","centroAtencion6"};
	
	public static String [] indicesDetalle = {"fechaIngreso0","id1","consecutivo2","viaIngreso3","area4","nombreCama5","tipoPaciente6","numeroCuenta7",
											  "fechaCierre8","facturas9","fechaFacturas10","centroAtencion11","ingreso12","cama13","observaciones14",
											  "liberar15","operacionTrue16"};
	
		//---------------------------------------------------------------
	
	/*-----------------------------------------------------------
	 *         METODOS LIBERACION CAMAS DE INGRESOS  CERRADOS
	 ------------------------------------------------------------*/
			
	/**
	 * Se inicializa el Dao
	 */
	
	public static LiberacionCamasIngresosCerradosDao liberacionCamasIngresosCerradosDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getLiberacionCamasIngresosCerradosDao();
	}
	
	/**
	 * Metodo encargado de consultar los ingresos
	 * @param connection
	 * @param criterios
	 * ---------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------
	 * -- centroAtencion6 --> Requerido
	 * @return mapa
	 * -------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------
	 * --fechaIngreso0_
	 * -- id1_
	 * -- consecutivo2_
	 * -- fechaCierre3_
	 * -- codigoCama4_
	 * -- nombreCama5_
	 */
	private static HashMap consultaIngresos (Connection connection, HashMap criterios)
	{
		return liberacionCamasIngresosCerradosDao().consultaIngresos(connection, criterios);
	}
	
	/**
	 * Metodo encargado de consultar el detalle de 
	 * l afuncionalidad liberacion camas ingresos cerrados
	 * @param connection
	 * @param criterios
	 * ---------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------
	 * -- centroAtencion11 --> Requerido
	 * @return mapa
	 * -------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------
	 * -- id1_
	 * -- consecutivo2_
	 * -- viaIngreso3_
	 * -- area4_
	 * -- nombreCama5_
	 * -- tipoPaciente6_
	 * -- numeroCuenta7_
	 * -- fechaCierre8_
	 * -- facturas9_
	 * -- fechaFacturas10_
	 * -- centroAtencion11
	 */
	private static HashMap consultaDetalle (Connection connection, HashMap criterios)
	{
		return liberacionCamasIngresosCerradosDao().consultaDetalle(connection, criterios);
	}
	
	/**
	 * Metodo encargado de consultar los ingresos
	 * @param connection
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap consultaIngresos (Connection connection, String centroAtencion)
	{
		HashMap criterios = new HashMap ();
		criterios.put(indicesListado[6], centroAtencion);
		return consultaIngresos(connection, criterios);
	}
	
	
	/**
	 * Metodo encargado de consultar el detalle
	 * @param connection
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap consultaDetalle (Connection connection, String centroAtencion, String ingreso)
	{
		HashMap criterios = new HashMap ();
		//centro atencion
		criterios.put(indicesDetalle[11], centroAtencion);
		//ingreso
		criterios.put(indicesDetalle[12], ingreso);
		return consultaDetalle(connection, criterios);
	}
	
	public static ActionForward accionOrdenarMapa(Connection connection, LiberacionCamasIngresosCerradosForm forma,ActionMapping mapping)
	{
		
		int numReg = Integer.parseInt(forma.getListadoIngresos("numRegistros")+"");
		//se casa el centro de atencion
		String centroAten=forma.getListadoIngresos(indicesListado[6])+"";
		forma.setListadoIngresos(Listado.ordenarMapa(indicesListado, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getListadoIngresos(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setListadoIngresos("numRegistros",numReg+"");
		forma.setListadoIngresos(indicesListado[6],centroAten);
		
		
		try 
		{
			UtilidadBD.cerrarConexion(connection);
		}
		catch (Exception e)
		{
			logger.error("\n Problema cerrando la conexion en accion Ordenar Mapa "+e);
		}
		
		
		return mapping.findForward("listado");
	}
	/**
	 * metodo encargado de guardar
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @throws SQLException
	 */
	public static void guardar (Connection connection, LiberacionCamasIngresosCerradosForm forma, UsuarioBasico usuario) throws SQLException
	{
		if (UtilidadTexto.getBoolean(forma.getDetalle(indicesDetalle[15])+""))
		{
			
			boolean transacction = UtilidadBD.iniciarTransaccion(connection);
			int ban=0;
			logger.info("----->INICIANDO TRANSACCION ....");
					
			Cama cama = new Cama();
			//se actualiza el estado de la cama
			logger.info("\n voy a actualizar el estado de la cama ...."+forma.getDetalle(indicesDetalle[13])+"   estao --> "+ValoresPorDefecto.getCodigoEstadoCama(usuario.getCodigoInstitucionInt())+"");
			ban=cama.cambiarEstadoCama(connection,forma.getDetalle(indicesDetalle[13])+"",Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoEstadoCama(usuario.getCodigoInstitucionInt())+""));
			if (ban<1)
				transacction=false;
			
			//ACTUALIZACION DE LA ESTANCIA DE LA CAMA
			TrasladoCamas objetoTraslado= new TrasladoCamas();
			if (transacction)
			{
				logger.info("\n voy a actualizar la estancia de la cama ....");
				transacction=objetoTraslado.actualizarFechaHoraFinalizacion(connection,Utilidades.convertirAEntero(forma.getDetalle(indicesDetalle[7])+""),UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual() ,ConstantesBD.finTransaccion,forma.getDetalle(indicesDetalle[14])+"");
			}
			//////////////////////////////////////
			
			if(transacction)
			{
				UtilidadBD.finalizarTransaccion(connection);			
				logger.info("----->TRANSACCION AL 100% ....");
				forma.setDetalle(indicesDetalle[16], ConstantesBD.acronimoSi);
			}
			else
			{
				UtilidadBD.abortarTransaccion(connection);
			}
		}
	}
}