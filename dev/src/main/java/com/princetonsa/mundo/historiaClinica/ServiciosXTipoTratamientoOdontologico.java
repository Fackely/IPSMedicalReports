package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;
import com.princetonsa.actionform.historiaClinica.ServiciosXTipoTratamientoOdontologicoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.ServiciosXTipoTratamientoOdontologicoDao;
import com.princetonsa.mundo.UsuarioBasico;





/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class ServiciosXTipoTratamientoOdontologico
{
	/**
	 *Atributos Servicios X Tipo Tratamiento Odontologico
	 */
	private static Logger logger = Logger.getLogger(ServiciosXTipoTratamientoOdontologico.class);
	
	
	
	
	//--------indices--------------
	public static final String [] indicesServicioXTipoTratamientoOdontologico = {"consecutivo0_","codigoTipoTratamiento1_","nombreTipoTratamiento2_","codigoCupsServicio3_","nombreServicio4_",
																				 "codigoAxiomaServicio5_","uet6_","estaBd7_","institucion8_","usuarioModifica9_"};
	
	
	/**
	 * Se inicializa el Dao
	 */
	
	public static ServiciosXTipoTratamientoOdontologicoDao serviciosXTipoTratamientoOdontologicoDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getServiciosXTipoTratamientoOdontologicoDao();
	}
	
	
	/**
	 * Metodo encargado de consultar los datos de 
	 * servicios por tipo tratamiento odontologico 
	 * en la tabla serv_x_tipo_trat_odont
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param criterios
	 * --------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ----------------------------
	 * -- institucion --> Requerido
	 * -- tipoTratamientoOdontologico --> Requerido
	 * @return Mapa
	 * -------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------
	 * consecutivo0_,codigoTipoTratamiento1_,
	 * nombreTipoTratamiento2_,codigoCupsServicio3_,
	 * nombreServicio4_,codigoAxiomaServicio5_,
	 * uet6_,estaBd7_
	 */
	public static  HashMap consultarServiciosXTratamientoOdontologico (Connection connection,HashMap criterios)
	{
		return serviciosXTipoTratamientoOdontologicoDao().consultarServiciosXTratamientoOdontologico(connection, criterios);
	}
	
	
	/**
	 * Metodo encargado de ingresar los datos de servicios
	 * por tipo de tratamiento odontologico en la 
	 * tabla serv_x_tipo_trat_odont
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param datos
	 * ---------------------------
	 * KEY'S DEL MAPA DATOS
	 * ---------------------------
	 * codigoTipoTratamiento1_,codigoAxiomaServicio5_,
	 * uet6_,institucion8_,usuarioModifica9_
	 * @return false/true
	 */
	public static boolean insertarServicosXTipoTratamiento (Connection connection, HashMap datos)
	{
		return serviciosXTipoTratamientoOdontologicoDao().insertarServicosXTipoTratamiento(connection, datos);
	}
	
	
	/**
	 *Metodo encargado de eliminar un registro de 
	 *servicios por tipo tratamiento odontologico en 
	 *la tabla  serv_x_tipo_trat_odont
	 *@author Jhony Alexander Duque A.
	 *@param connection
	 *@param consecutivo
	 *@return false/true 
	 */
	public static boolean eliminarServicosXTipoTratamiento (Connection connection, String consecutivo)
	{
		return serviciosXTipoTratamientoOdontologicoDao().eliminarServicosXTipoTratamiento(connection, consecutivo);
	}
	
	/**
	 * Metodo encargado de actualizar los datos de servicio por tipo de tratamiento
	 * odontologico en la tabla serv_x_tipo_trat_odont
	 * @author Jhony Alexander Duque.
	 * @param connection
	 * @param datos
	 * ---------------------
	 * KEY'S DEL MAPA DATOS
	 * ---------------------
	 * -- codigoAxiomaServicio5_ --> Requerido
	 * -- uet6_ --> Opional
	 * -- usuarioModifica9_ --> Requerido
	 * -- consecutivo0_ --> Requerido
	 * @return false/true
	 */
	public  static boolean actualizarServicioXTipoTratamientoOdontologico (Connection connection, HashMap datos)
	{
		return serviciosXTipoTratamientoOdontologicoDao().actualizarServicioXTipoTratamientoOdontologico(connection, datos);
	}
	
	/**
	 * inicializa el select de tipos de tratamiento
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void empezar (Connection connection,ServiciosXTipoTratamientoOdontologicoForm forma,UsuarioBasico usuario )
	{
		HashMap criterios = new HashMap ();
		//institucion
		criterios.put("institucion", usuario.getCodigoInstitucion());
		//activo
		criterios.put("activo", ConstantesBD.acronimoSi);
		
		forma.setTiposTratamientoOdontologico(Utilidades.obtenerTiposTratamientoOdontologico(connection, criterios));
	}
	
	
	/**
	 * Metodo encargado de buscar los datos
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void buscar (Connection connection,ServiciosXTipoTratamientoOdontologicoForm forma,UsuarioBasico usuario )
	{
		//se limpian los datos anteriores
		forma.reset_servicios();
		
		HashMap criterios = new HashMap ();
		//institucion
		criterios.put("institucion", usuario.getCodigoInstitucion());
		//tipo tratamiento
		criterios.put("tipoTratamientoOdontologico", forma.getTipoTratamiento());
		if (!forma.getTipoTratamiento().equals(ConstantesBD.codigoNuncaValido+""))
			forma.setServXTipTratOdont(consultarServiciosXTratamientoOdontologico(connection, criterios));
		else
			forma.setEstado("empezar");
		
		//se saca un duplicado
		forma.setServXTipTratOdontOld((HashMap)forma.getServXTipTratOdont().clone());
		
	}
	
	/**
	 * Encargado de crear un nuevo registro
	 * @param forma
	 * @param usuario
	 */
	public static void nuevo (ServiciosXTipoTratamientoOdontologicoForm forma,UsuarioBasico usuario)
	{
		int numReg = Utilidades.convertirAEntero(forma.getServXTipTratOdont("numRegistros")+"");
		//tipo tratamiento
		forma.setServXTipTratOdont(indicesServicioXTipoTratamientoOdontologico[1]+numReg, ConstantesBD.codigoNuncaValido);
		//codigo del servicio
		forma.setServXTipTratOdont(indicesServicioXTipoTratamientoOdontologico[5]+numReg, ConstantesBD.codigoNuncaValido);
		// uet
		forma.setServXTipTratOdont(indicesServicioXTipoTratamientoOdontologico[6]+numReg, "");
		//institucion
		forma.setServXTipTratOdont(indicesServicioXTipoTratamientoOdontologico[8]+numReg, usuario.getCodigoInstitucion());
		//usuario modifica
		forma.setServXTipTratOdont(indicesServicioXTipoTratamientoOdontologico[9]+numReg, usuario.getLoginUsuario());
		//estaBd
		forma.setServXTipTratOdont(indicesServicioXTipoTratamientoOdontologico[7]+numReg, ConstantesBD.acronimoNo);
		//se aumenta numRegistros
		forma.setServXTipTratOdont("numRegistros", (numReg+1));
	}
	
	
	
	public static void guardar (Connection connection, ServiciosXTipoTratamientoOdontologicoForm forma,UsuarioBasico usuario)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		logger.info("----->INICIANDO TRANSACCION ....");
		String indices [] ={"nombreTipoTratamiento2_","nombreServicio4_","uet6_",""};
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getEliminados("numRegistros")+"");i++)
		{
			logger.info("Entro a eliminar BD :::::::::::::contador I...>"+i);
			if (transacction)
			{
				transacction=eliminarServicosXTipoTratamiento(connection, forma.getEliminados(indicesServicioXTipoTratamientoOdontologico[0]+i)+"");
				if (transacction)//se genera el log
					Utilidades.generarLogGenerico(forma.getEliminados(), forma.getServXTipTratOdontOld(), usuario.getLoginUsuario(), true, i, ConstantesBD.logServiciosXTipoTratamientoOdontologicoCodigo, indices);
			}
		}		
		
		for(int i=0;i<Integer.parseInt(forma.getServXTipTratOdont("numRegistros")+"");i++)
		{			
			//modificar			
			
			if(transacction && (forma.getServXTipTratOdont(indicesServicioXTipoTratamientoOdontologico[7]+i)+"").trim().equals(ConstantesBD.acronimoSi))
			{
				
				if(!(forma.getServXTipTratOdont(indicesServicioXTipoTratamientoOdontologico[5]+i)+"").equals(forma.getServXTipTratOdontOld(indicesServicioXTipoTratamientoOdontologico[5]+i)+"")
					|| !(forma.getServXTipTratOdont(indicesServicioXTipoTratamientoOdontologico[6]+i)+"").equals(forma.getServXTipTratOdontOld(indicesServicioXTipoTratamientoOdontologico[6]+i)+""))
				{	
					logger.info("Entro a modificar BD :::::::::::::contador I...>"+i);
					HashMap datos = new HashMap ();
					//codigo servicio
					datos.put(indicesServicioXTipoTratamientoOdontologico[5], forma.getServXTipTratOdont(indicesServicioXTipoTratamientoOdontologico[5]+i));
					//uet
					datos.put(indicesServicioXTipoTratamientoOdontologico[6], forma.getServXTipTratOdont(indicesServicioXTipoTratamientoOdontologico[6]+i));
					//usuario modifica
					datos.put(indicesServicioXTipoTratamientoOdontologico[9], usuario.getLoginUsuario());
					//consecutivo
					datos.put(indicesServicioXTipoTratamientoOdontologico[0], forma.getServXTipTratOdont(indicesServicioXTipoTratamientoOdontologico[0]+i));
					
					transacction=actualizarServicioXTipoTratamientoOdontologico(connection, datos);
					
					if (transacction)//se genera el log
						Utilidades.generarLogGenerico(forma.getServXTipTratOdont(), forma.getServXTipTratOdontOld(), usuario.getLoginUsuario(), false, i, ConstantesBD.logServiciosXTipoTratamientoOdontologicoCodigo, indices);
						
				}	
				
			}
			
			//insertar			
			else if( transacction && (forma.getServXTipTratOdont(indicesServicioXTipoTratamientoOdontologico[7]+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				logger.info("Entro a insertar BD :::::::::::::contador I...>"+i);
				HashMap datos = new HashMap ();
				
				//tipo tratamiento
				datos.put(indicesServicioXTipoTratamientoOdontologico[1], forma.getTipoTratamiento());
				//servicio
				datos.put(indicesServicioXTipoTratamientoOdontologico[5], forma.getServXTipTratOdont(indicesServicioXTipoTratamientoOdontologico[5]+i));
				//uet
				datos.put(indicesServicioXTipoTratamientoOdontologico[6], forma.getServXTipTratOdont(indicesServicioXTipoTratamientoOdontologico[6]+i));
				//intitucion
				datos.put(indicesServicioXTipoTratamientoOdontologico[8], usuario.getCodigoInstitucion());
				//usuario modifica
				datos.put(indicesServicioXTipoTratamientoOdontologico[9], usuario.getLoginUsuario());
				
				transacction=insertarServicosXTipoTratamiento(connection, datos);
				
			}
			
		}
		
		
		forma.setOperacionTrue(transacction);
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
			logger.info("----->TRANSACCION AL 100% ....");
			
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}
		
		buscar(connection, forma, usuario);
			
	}
	
	
	/**
	 * Metodo encargado eliminar un registro
	 * @param forma
	 * @param request
	 * @param response
	 * @param codigoInstitucion
	 * @return
	 */
	public static ActionForward accionEliminarCampo(ServiciosXTipoTratamientoOdontologicoForm forma, HttpServletRequest request, HttpServletResponse response,int codigoInstitucion) 
	{
		//logger.info("Entra a accionEliminarCampo :::::::::::::::::::::::::::");
		int numRegMapEliminados=Integer.parseInt(forma.getEliminados("numRegistros")+"");
		
		//logger.info("accionEliminarCampo y el numRegMapEliminados"+numRegMapEliminados);
		int ultimaPosMapa=(Integer.parseInt(forma.getServXTipTratOdont("numRegistros")+"")-1);
		
				
		for(int i=0;i<indicesServicioXTipoTratamientoOdontologico.length;i++)
		{ 
			//solo pasar al mapa los registros que son de BD
			if((forma.getServXTipTratOdont(indicesServicioXTipoTratamientoOdontologico[7]+forma.getIndex())+"").trim().equals(ConstantesBD.acronimoSi))
			{
				forma.setEliminados(indicesServicioXTipoTratamientoOdontologico[i]+numRegMapEliminados, forma.getServXTipTratOdont(indicesServicioXTipoTratamientoOdontologico[i]+forma.getIndex()));
			}
		}		
		
		if((forma.getServXTipTratOdont(indicesServicioXTipoTratamientoOdontologico[7]+forma.getIndex())+"").trim().equals(ConstantesBD.acronimoSi))
		{			
			forma.setEliminados("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=Integer.parseInt(forma.getIndex());i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indicesServicioXTipoTratamientoOdontologico.length;j++)
			{
				forma.setServXTipTratOdont(indicesServicioXTipoTratamientoOdontologico[j]+i,forma.getServXTipTratOdont(indicesServicioXTipoTratamientoOdontologico[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indicesServicioXTipoTratamientoOdontologico.length;j++)
		{
			forma.getServXTipTratOdont().remove(indicesServicioXTipoTratamientoOdontologico[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamos el numero de registros en el mapa.
		forma.setServXTipTratOdont("numRegistros",ultimaPosMapa);		
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(codigoInstitucion),Integer.parseInt(forma.getServXTipTratOdont("numRegistros").toString()), response, request, "serviciosXTipoTratamientoOdontologico.jsp",Integer.parseInt(forma.getIndex().toString())==ultimaPosMapa);
	}
	
	
}