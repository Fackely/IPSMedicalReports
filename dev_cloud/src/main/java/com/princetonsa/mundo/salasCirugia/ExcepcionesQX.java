/*
 * @(#)ExcepcionesQX.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 */
package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.manejoPaciente.UtilidadesManejoPaciente;
import com.princetonsa.actionform.salasCirugia.ExcepcionesQXForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ExcepcionesQXDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase para el manejo de las excepciones qx
 * @version 1.0, Sep 06, 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */

public class ExcepcionesQX 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private static Logger logger = Logger.getLogger(ExcepcionesQX.class);
	
	public final static String [] indices ={"detCodigoExcepcionQxCc0_","convenio1_","fechaInicial2_","fechaFinal3_","institucion4_","usuarioModifica5_",
											"codigoExcepcionQx6_","centroCosto7_","codigoExcepcionQxCc8_","tipoServicio9_","tipoCirugia10_",
											"tipoLiquidacion11_","tipoAsocio12_","servicio13_","liquidarSobreAsocio14_","especialidad15_",
											"grupoServicio16_","continuaMedico17_","continuaViaAcceso18_","valor19_","estaBd20_","tipoSala21_",
											"esUsa22_","contrato23_","tarOfi24_","nombreServicio25_","viaIngreso26_","tipoPaciente27_",
											"liquidarXPorcentaje28_","codigoCupsServicio29_"};
	
	/**
	 *instancia el dao 
	 */
	public static ExcepcionesQXDao excepcionesQXDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesQXDao();		
	}	
	
	
	/**
	 * Metodo encargado de Buscar las
	 * Vigencias para el convenio seleccionado.
	 * Parametros:
	 * @connection
	 * @criterios
	 * ------------------------------
	 * KEY'S DEL HASHMAP CRITERIOS
	 * ------------------------------
	 * -- convenio1_ --> Requerido
	 * -- institucion4_ --> Requerido
	 * ---------------------------------
	 * Retorna
	 * @return mapa
	 * --------------------------------
	 * KEY'S DEL HASHMAP MAPA
	 * --------------------------------
	 * -- codigoExcepcionQx6_
	 * -- fechaInicial2_
	 * -- fechaFinal3_ 
	 */
	public static HashMap buscar0 (Connection connection,HashMap criterios)
	{
		return excepcionesQXDao().buscar0(connection, criterios);
	}
	
	/**
	 * Metodo encargado de buscar los 
	 * centros de costo para una vigencia.
	 * @param connection
	 * @param criterios
	 * -----------------------------------------
	 * KEY'S PARA EL HASHMAP CRITERIOS
	 * -----------------------------------------
	 * -- codigoExcepcionQx6_
	 * ---------------------------------------
	 * @return
	 * ---------------------------------
	 * KEY'S DEL MAPA QUE RETORNA
	 * ---------------------------------
	 * -- codigoExcepcionQxCc8_
	 * -- centroCosto7_
	 */
	public static HashMap buscar1 (Connection connection, HashMap criterios )
	{
		return excepcionesQXDao().buscar1(connection, criterios);
	}
	
	/**
	 * Metodo encargado de identificar en que
	 * parte de la BD eliminar los datos
	 * @param connection
	 * @param datos
	 * 
	 * -- codigoExcepcionQxCc8_ --> Requerido
	 * -- detCodigoExcepcionQxCc0_ --> Requerido
	 * @return
	 */
	public static boolean eliminarGeneral (Connection connection, HashMap datos)
	{
		return excepcionesQXDao().eliminarGeneral(connection, datos);
	}
	
	/**
	 * Metodo encargado  de buscar los datos 
	 * de  excepciones quirurjicas que pertenecen
	 * a un convenio, una vigencia y un centro de costo.
	 * @param connection
	 * @param criterios
	 * ----------------------------------
	 * KEY'S DEL HASHMAP CRITERIOS
	 * ----------------------------------
	 * -- detCodigoExcepcionQxCc0_ --> Opcional
	 * -- centroCosto7_ --> Opcional
	 * -- especialidad15_ --> Opcional
	 * -- grupoServicio16_ --> Opcional
	 * -- tipoServicio9_ --> Opcional
	 * -- tipoSala21_ --> Opcional
	 * -- servicio13_ --> Opcional
	 * -- tipoAsocio12_ --> Opcional
	 * -- tipoCirugia10_ --> Opcional
	 * -- continuaMedico17_ --> Opcional
	 * -- continuaViaAcceso18_ --> Opcional
	 * -- tipoLiquidacion11_ --> Opcional
	 * -- valor19_ --> Opcional
	 * @return
	 */
	public static HashMap buscar2 (Connection connection, HashMap criterios )
	{
		return excepcionesQXDao().buscar2(connection, criterios);
	}
	
	/**
	 * Metodo encargado de insertar los datos
	 * en la tabla excepciones_qx.
	 * Parametros: 
	 * @connection
	 * @datos
	 * --------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * --------------------------
	 * -- convenio1_ --> Requerido
	 * -- fechaInicial2_ --> Requerido
	 * -- fechaFinal3_ --> Requerido
	 * -- institucion4_ --> Requerido
	 * -- usuarioModifica5_ --> Requerido
	 * -- contrato23_ --> Requerido
	 * @return boolean
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.				
	 */
	public static boolean insetar0 (Connection connection, HashMap datos)
	{
		return excepcionesQXDao().insetar0(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de insertar los datos de
	 * la tabla (det_excepciones_qx_cc) 
	 * @param connection
	 * @param datos
	 * --------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * --------------------------------
	 * -- codigoExcepcionQxCc8_ --> Requerido
	 * -- tipoServicio9_ --> Opcional
	 * -- tipoCirugia10_ --> Requerido
	 * -- tipoLiquidacion11_ --> Requerido
	 * -- tipoAsocio12_ --> Requerido
	 * -- servicio13_ --> Opcional
	 * -- liquidarSobreAsocio14_ --> Opcional
	 * -- especialidad15_ --> Opcional
	 * -- grupoServicio16_ --> Opcional
	 * -- continuaMedico17_ --> Opcional
	 * -- continuaViaAcceso18_ --> Opcional
	 * -- valor19_ --> Requerido
	 * -- usuarioModifica5_ --> Requerido
	 * -- tipoSala21_
	 * @return
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.	
	 */
	public static boolean insetar2 (Connection connection, HashMap datos)
	{
		return excepcionesQXDao().insetar2(connection, datos);
	}
	
	/**
	 *Metodo encargado de elegir donde seran insertados los datos
	 *ya sea en la tabla excepciones_qx_cc o en la tabla
	 *det_excepciones_qx_cc.
	 *@connection
	 *@datos
	 *-----------------------
	 *KEY'S DEL HASHMAP DATOS
	 *-----------------------
	 * -- codigoExcepcionQx6_ -->Requerido
	 * -- centroCosto7_ --> Requerido
	 * -- codigoExcepcionQxCc8_ --> Requerido
	 * -- tipoServicio9_ --> Opcional
	 * -- tipoCirugia10_ --> Requerido
	 * -- tipoLiquidacion11_ --> Requerido
	 * -- tipoAsocio12_ --> Requerido
	 * -- servicio13_ --> Opcional
	 * -- liquidarSobreAsocio14_ --> Opcional
	 * -- especialidad15_ --> Opcional
	 * -- grupoServicio16_ --> Opcional
	 * -- continuaMedico17_ --> Opcional
	 * -- continuaViaAcceso18_ --> Opcional
	 * -- valor19_ --> Requerido
	 * -- usuarioModifica5_ --> Requerido
	 * -- tipoSala21_
	 * 
	 */
	public static int insertarGeneral (Connection connection,HashMap datos)
	{
		return excepcionesQXDao().insertarGeneral(connection, datos);
	}
	/**
	 * Metodo encargado de eliminar los datos
	 * de la tabla  (excepciones_qx)
	 * @connection
	 * @datos
	 * ------------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * ------------------------------------
	 * -- codigoExcepcionQx6_ --> Requerido
	 * -- institucion4_ --> Requerido
	 * @return
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.	
	 */
	public static boolean eliminar0 (Connection connection,HashMap datos)
	{
		return excepcionesQXDao().eliminar0(connection, datos);
	}
	
	/**
	 * Metodo encargado de modificar los datos
	 * de la tabla excepciones_qx
	 * @connection
	 * @datos
	 * -------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * -------------------------------
	 * -- fechaInicial2_
	 * -- fechaFinal3_
	 * -- usuarioModifica5_
	 * -- ExcepcionQx6_
	 * -- institucion4_
	 *  * @return
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.	
	 */
	public static boolean modificar0 (Connection connection, HashMap datos)
	{
		return excepcionesQXDao().modificar0(connection, datos);
	}
	
	/**
	 * Metodo encargado de modificar los datos de
	 * la tabla (det_excepciones_qx_cc) 
	 * @param connection
	 * @param datos
	 * --------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * --------------------------------
	 * -- tipoServicio9_ --> Opcional
	 * -- tipoCirugia10_ --> Requerido
	 * -- tipoLiquidacion11_ --> Requerido
	 * -- tipoAsocio12_ --> Requerido
	 * -- servicio13_ --> Opcional
	 * -- liquidarSobreAsocio14_ --> Opcional
	 * -- especialidad15_ --> Opcional
	 * -- grupoServicio16_ --> Opcional
	 * -- continuaMedico17_ --> Opcional
	 * -- continuaViaAcceso18_ --> Opcional
	 * -- valor19_ --> Requerido
	 * -- usuarioModifica5_ --> Requerido
	 * -- detCodigoExcepcionQxCc0_ --> Requerido
	 * -- tipoSala21_ --> Opcional
	 * @return
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.	
	 */
	public static boolean modificar1 (Connection connection, HashMap datos)
	{
		return excepcionesQXDao().modificar1(connection, datos);
	}
	
	/**
	 * Metodo encargao de inicializar la aplicacion
	 * @param connection
	 * @param institucion
	 * @param forma
	 * @param mapping
	 * @return
	 */
	public static ActionForward accionEmpezar (Connection connection,int institucion, ExcepcionesQXForm forma, ActionMapping mapping )
	{
		forma.reset();
		forma.resetPager();
		forma.resetBusqueda();
		//se cargan los selects
		initSelects(connection, forma, institucion);
		//se cargan todos los convenios existentes.
		forma.setConvenios(Utilidades.obtenerConvenios(connection, "", "", true, "", true));
		try 
		{
			UtilidadBD.cerrarConexion(connection);
		}
		catch (SQLException e) 
		{
			logger.info("\n Problema cerrando la conexion en accion empezar "+e);
		}
		
		return mapping.findForward("principal");
	}
	
	
	


	/**
	 * Elimina un Registro del HashMap de excepTipoSala,
	 * Adicciona el Registro Eliminado en el HashMap de excepTipoSalaEliminados
	 * @param forma
	 * @param request
	 * @param response
	 * @param mapping
	 * @return ActionForward
	 */
	public static ActionForward accionEliminarCampoVig(ExcepcionesQXForm forma, HttpServletRequest request, HttpServletResponse response,int codigoInstitucion, ActionMapping mapping) 
	{
		//logger.info("Entra a accionEliminarCampo :::::::::::::::::::::::::::"+forma.getVigenciasEliminados());
		int numRegMapEliminados=Integer.parseInt(forma.getVigenciasEliminados("numRegistros")+"");
		//logger.info(":::::::0:::::::::");
		//logger.info("accionEliminarCampo y el numRegMapEliminados"+numRegMapEliminados);
		int ultimaPosMapa=(Integer.parseInt(forma.getVigencias("numRegistros")+"")-1);
		//logger.info(":::::::1:::::::::");
		//poner la informacion en el otro mapa.
		//String[] indicesl= (String[])forma.getExcepTipoSala("INDICES_MAPA");		
		
		for(int i=0;i<indices.length;i++)
		{ 
			//logger.info(":::::::2:::::::::");
			//solo pasar al mapa los registros que son de BD
			if((forma.getVigencias(indices[20]+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
			{
				logger.info(":::::::3:::::::::");
				forma.setVigenciasEliminados(indices[i]+""+numRegMapEliminados, forma.getVigencias(indices[i]+""+forma.getIndexEliminado()));
			}
		}		
		//logger.info(":::::::4:::::::::");
		if((forma.getVigencias(indices[20]+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
		{			
			logger.info(":::::::4:::::::::");
			forma.setVigenciasEliminados("numRegistros", (numRegMapEliminados+1));
		}
		//logger.info(":::::::5:::::::::");
		//acomodar los registros del mapa en su nueva posicion
		for(int i=Integer.parseInt(forma.getIndexEliminado()+"");i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				forma.setVigencias(indices[j]+""+i,forma.getVigencias(indices[j]+""+(i+1)));
			}
		}
		//logger.info(":::::::6:::::::::");
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getVigencias().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamos el numero de registros en el mapa.
		forma.setVigencias("numRegistros",ultimaPosMapa);
		
		logger.info(" el valor de vigencias "+forma.getVigencias());
		logger.info(" el valor de vigencias eliminadas "+forma.getVigenciasEliminados());
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(codigoInstitucion),Integer.parseInt(forma.getVigencias("numRegistros").toString()), response, request, "frameExcepcionesQxVigencias.jsp",Integer.parseInt(forma.getIndexEliminado().toString())==ultimaPosMapa);
		
		
	}
	
	/**
	 * Metodo encargado de inicializar los selects para la funcionalidad
	 * @param connection
	 * @param forma
	 * @param institucion
	 */
	public static void initSelects (Connection connection,ExcepcionesQXForm forma,int institucion)
	{
		forma.resetSelects();
		//acronimos por el cual filtar los tipos de servicio.
		String tiposerv="'Q','R','D'";
		
		//es Hashmap contiene los datos por el cual se va a filtrar los grupos de servicio
		HashMap criterios = new HashMap ();
		criterios.put("institucion", institucion);
		criterios.put("activo", "t");
		// 1)se carga el ArrayList centros de costo con los centros de costo de la busqueda.
		forma.setCentroCosto(UtilidadesManejoPaciente.obtenerCentrosCosto(connection, institucion, ConstantesBD.codigoTipoAreaDirecto+"", false, 0));
		//2)se cargan los tipos de servicios filtrados por tiposerv
		forma.setTiposServicio(UtilidadesFacturacion.obtenerTiposServicio(connection, tiposerv, ""));
		//4)se cargan los tipod de cirugia
		forma.setTiposCirugia(Utilidades.obtenerTipoCirugia(connection, ""));
		//5)se cargan los asocios
		forma.setAsocios(Utilidades.obtenerAsocios(connection, "", "", ""));
		//6)se cargan los tipos de sala
		forma.setTipoSala(Utilidades.obtenerTiposSala(connection,institucion,"","f"));
		//8)se cargan todas las especialidades
		forma.setEspecialidades(Utilidades.obtenerEspecialidadesEnArray(connection,ConstantesBD.codigoNuncaValido,ConstantesBD.codigoNuncaValido));
		//9)se cargan los grupos de servicio, validando por El Hasmap criterios		
		forma.setGruposServicio(Utilidades.obtenerGrupoServicios(connection, criterios));
		
	}
	
	public static void initContratos (Connection connection, ExcepcionesQXForm forma )
	{
		//1)contratos
		forma.setContratos(Utilidades.obtenerContratos(connection, Integer.parseInt(forma.getCriteriosBusqueda(indices[1])+""), false, false));
	}
	
	
	/**
	 * Metodo encargado se identificar cuales opciones son las que se 
	 * deben cargar en tipo liquidacion.
	 * @param connection
	 * @param tarfio
	 * @param forma
	 */
	public static void initTipoLiquidacion (Connection connection, String tarfio,ExcepcionesQXForm forma )
	{
		//logger.info("\n Entre a cargar liquidacion ==> "+tarfio);
		String tiposLiq="'P','T','UVR','G'";
		//filtar por los siguientes tipos de liquidacion
		if (tarfio.equals(ConstantesBD.codigoTarifarioISS+""))
			tiposLiq="'P','T','UVR'";
		else
			if (tarfio.equals(ConstantesBD.codigoTarifarioSoat+""))
				tiposLiq="'P','T','G'";
			
				
			
		//1)se cargan los valores de tipos de liquidacion
		forma.setTiposLiquidacion(UtilidadesFacturacion.obtenerTiposLiquidacion(connection, tiposLiq));
		
		
	
	}
	
	
	/**
	 * Metodo encargado de buscar las vigencias para el convenio
	 * seleccionado.
	 * @param connection
	 * @param forma
	 * @param institucion
	 * @param mapping
	 * @return
	 */
	public static ActionForward buscarVigencias (Connection connection, ExcepcionesQXForm forma, int institucion, ActionMapping mapping)
	{
		HashMap criterios = new HashMap ();
		//se cargan los criterios de busqueda.
		//aqui se carga el convenio
		forma.resetVigencias();
		forma.setCriteriosBusqueda(indices[7] , ConstantesBD.codigoNuncaValido);
		criterios.put(indices[1], forma.getCriteriosBusqueda(indices[1]));
		//aqui se carga la institucion
		criterios.put(indices[4], institucion);
		//se cargan las vigencias para ese convenio.
		forma.setVigencias(buscar0(connection, criterios));
		//sacamos una copia del mapa
		Listado.copyMap(forma.getVigencias(), forma.getVigenciasClone(), Integer.parseInt(forma.getVigencias("numRegistros")+""), indices);
		
		initContratos(connection, forma);
		
		logger.info("\n sali de las busuqedas ==> "+forma.getVigencias());
		UtilidadBD.closeConnection(connection);
		return mapping.findForward("framevig");
	}
	
	/**
	 * metodo encargado de agregar nuevas vigencias.
	 * @param forma
	 */
	public static ActionForward accionNuevaVigencias (ExcepcionesQXForm forma,UsuarioBasico usuario,Connection connection,HttpServletResponse response, HttpServletRequest request )
	{
		logger.info("\n Entre a accionNuevaVigencias con el hashmap "+forma.getVigencias());
		int pos = Integer.parseInt(forma.getVigencias("numRegistros").toString());
		
		//se agrega el convenio
		forma.setVigencias(indices[1]+pos, forma.getCriteriosBusqueda(indices[1]));
		//se agrega la fecha inicial
		forma.setVigencias(indices[2]+pos, "");
		//se agrega la fecha final
		forma.setVigencias(indices[3]+pos, "");
		//se agrega el contrato
		forma.setVigencias(indices[23]+pos, ConstantesBD.codigoNuncaValido);
		//se agrega la institucion
		forma.setVigencias(indices[4]+pos, usuario.getCodigoInstitucionInt());
		//se agrega el usuario 
		forma.setVigencias(indices[5]+pos, usuario.getLoginUsuario());
		//se agrega si esta en la BD
		forma.setVigencias(indices[20]+pos, ConstantesBD.acronimoNo);
		//se agrega si esta usado o no
		forma.setVigencias(indices[22]+pos, "0");
		//se actualiza numRegistros
		forma.setVigencias("numRegistros", (pos+1)+"");
		//se agrega el indice del mapa
		forma.setVigencias("INDICES_MAPA", indices);
	try
	{
		//se cierra la conexion
		UtilidadBD.cerrarConexion(connection);
	} 
	catch (SQLException e)
	{
	 logger.info("problema cerrando la conexion en accionNuevaVigencias "+e);
	}
		
		//se redirecciona  a la pag
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getVigencias("numRegistros").toString()), response, request, "frameExcepcionesQxVigencias.jsp",true);		
	
	}
	


	/**
	 * Metodo encargado de realizar las operaciones en la BD
	 * Insertar, Eliminar y Modificar (Vigencias)
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	public static ActionForward accionGuardarRegistrosVig(Connection connection, ExcepcionesQXForm forma, ActionMapping mapping,UsuarioBasico usuario)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		logger.info("----->INICIANDO TRANSACCION ....");
		String estadoEliminar="ninguno";		
		logger.info("valor del hasmap al entar a accionGuardarRegistrosVig "+forma.getVigencias());
		logger.info("valor del hasmap al entar a accionGuardarRegistrosVig  fechas eliminadas"+forma.getVigenciasEliminados());
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getVigenciasEliminados("numRegistros")+"");i++)
		{
		//	logger.info("Entro a eliminar BD :::::::::::::contador I...>"+i);
		//	logger.info("Hashmap:::::::::::"+forma.getAsociosXRangoTiempoEliminadoMap());
			if(eliminarVigBD(connection, forma, i,usuario.getCodigoInstitucionInt()))		
			{				
				 estadoEliminar="operacionTrue";
				 transacction=true;						 
			}
		}		
		
		for(int i=0;i<Integer.parseInt(forma.getVigencias("numRegistros")+"");i++)
		{			
			//modificar			
			
			if((forma.getVigencias(indices[20]+i)+"").trim().equals(ConstantesBD.acronimoSi))
			{
				HashMap tmp1 = new HashMap ();
				HashMap tmp2 = new HashMap ();
				tmp1=Listado.copyOnIndexMap(forma.getVigenciasClone(), i+"", indices);
				logger.info("estoy en modificar, el valor tmp "+tmp1);
				if(existeModificacionVig(forma,i,tmp1))
				{	
												
				
					logger.info("ENTRO A antes de copyindexmap::::::::..");
					tmp2=Listado.copyOnIndexMap(forma.getVigencias(), i+"", indices);
					logger.info("EL VALOR DE TMP2 ES "+tmp2);
					transacction = modificarVigBD(connection, tmp2, usuario);
					
				}	
			
			}
			
			//insertar			
			else if((forma.getVigencias(indices[20]+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				//logger.info("Entro a insertar consentimiento informado "+forma.getConsentimientoInfMap()); 
				HashMap tmp = new HashMap ();
				//se copian los datos a un hashmap de una sola posicion la "0"
				tmp=Listado.copyOnIndexMap(forma.getVigencias(), i+"",indices);
				//se inserta en la BD
				transacction = insertarVigBD(connection, tmp, usuario);
				 
				 estadoEliminar="operacionTrue";
			}
			
			// logger.info("\n\n valor i >> "+i);
		}
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
			logger.info("----->TRANSACCION AL 100% ....");
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}
		
		
		
		return buscarVigencias(connection, forma, usuario.getCodigoInstitucionInt(), mapping);
	}
	
	
	private static boolean insertarVigBD (Connection connection, HashMap temp,UsuarioBasico usuario)
	{
		logger.info("\n\n::::::::::::ENTRO A INSERTARVIGBD::::::\n\n");
		logger.info("valor de haspmap al entrar"+temp);
		
		HashMap datos = new HashMap ();
		
		String [] tmp ={ConstantesBD.codigoNuncaValido+"",ConstantesBD.codigoNuncaValido+""};
		try 
		{
			tmp = (temp.get(indices[23]+"0")+"").split(ConstantesBD.separadorSplit);
		} 
		catch (Exception e) 
		{
			logger.error("\n problema separando el contrato del tipo de tarifario oficial ");
			
		}
		
		//1) convenio
		datos.put(indices[1], temp.get(indices[1]+"0"));
		//2) fecha inicial
		datos.put(indices[2], temp.get(indices[2]+"0"));
		//3) fecha final
		datos.put(indices[3] , temp.get(indices[3]+"0"));
		//4) institucion
		datos.put(indices[4], temp.get(indices[4]+"0"));
		//5) usuario modifica
		datos.put(indices[5], temp.get(indices[5]+"0"));
		//6) contrato
		datos.put(indices[23], tmp[0]);
				
		
		return insetar0(connection, datos);
	}
	
	
	/**
	 * Carga los datos a eliminar en la BD; el parametro "i"
	 * es el encargado de manejar la posicion dentro del HashMap
	 * @param connection
	 * @param forma
	 * @return
	 */
	private static boolean eliminarVigBD (Connection connection, ExcepcionesQXForm forma,int i,int institucion)
	{
		logger.info(":::::::::ENTRO A ELIMINARVIGBD::::::::::::");
		logger.info("\n el valor de i es "+ i);
		logger.info("\n asocios eliminados "+forma.getVigenciasEliminados());
		logger.info("\n*****************************************************\n\n");
		
		HashMap datos = new HashMap ();
		//codigo encabezado
		datos.put(indices[6], forma.getVigenciasEliminados(indices[6]+i));
	
		//codigo institucion
		datos.put(indices[4], institucion);
		
	
		
		return eliminar0(connection, datos);

	 
	}
	
	
	/**
	 * verifica si el registro se ha modificado
	 * @param connection
	 * @param forma
	 * @param mundo
	 * @param int pos
	 * */
	private static boolean existeModificacionVig (ExcepcionesQXForm forma, int pos,HashMap temp)
	{
		logger.info("\n EXISTEMODIFICACIONVIG :::::::::: ");
		logger.info("\n\n ::: original ::::::"+temp);
		logger.info("\n\n ::: modificada ::::::"+forma.getVigencias());
				
		
		for(int i=0;i<indices.length;i++)
		{		//logger.info("::::: "+i+" :::::");
			if(temp.containsKey(indices[i]+"0") && forma.getVigencias().containsKey(indices[i]+pos))
			{				
				if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getVigencias(indices[i]+pos)+"").trim())))
				{
							
					return true;
					
				}				
			}
		}	

		
		return false;		
	}
	
	

	/**
	 * Este metodo se encarga de insertar en la BD los nuevos datos; 
	 * @param connection
	 * @param forma
	 * @param i
	 * @param usuario
	 * @return
	 */
	private static boolean modificarVigBD (Connection connection, HashMap temp,UsuarioBasico usuario)
	{
		logger.info("\n\n::::::::::::ENTRO A MODIFICARVIGBD ::::::\n\n");
		logger.info("valor de haspmap al entrar"+temp);
		
		//se carga un HashMap datos con los valores a insertar en la BD
		HashMap datos = new HashMap ();
		
		/*------------------------------
		 * Estos valores son requeridos
		 -------------------------------*/
		
		//se agrega fecha inicial
		datos.put(indices[2], temp.get(indices[2]+"0"));
		//se agrega fecha final
		datos.put(indices[3], temp.get(indices[3]+"0"));
		//se agrega el usuario modifica
		datos.put(indices[5], usuario.getLoginUsuario());
		//se agrega codigo de la tabla excepciones_qx 
		datos.put(indices[6], temp.get(indices[6]+"0"));
		//se agrega la instucion
		datos.put(indices[4], usuario.getCodigoInstitucionInt());
		
		
		/*-------------------------------------
		 * fin de valores requeridos
		 -------------------------------------*/
		
		
		return modificar0(connection, datos);
	}
	
	/**
	 * Metodo encargado de buscar el centro de costo
	 */
	public static ActionForward buscarCentroCosto (Connection connection, ExcepcionesQXForm forma,ActionMapping mapping)
	{
		HashMap criterios = new HashMap ();
		logger.info("\n el valor de las vigencias "+forma.getVigencias());
		logger.info("\n el valor de criterios busqueda "+forma.getCriteriosBusqueda());
		//se formatea el pager
		forma.resetPager();
		//1)se agrega el centro de costo 		
		if (forma.getCriteriosBusqueda().containsKey(indices[7]))
			criterios.put(indices[7], forma.getCriteriosBusqueda(indices[7]));
		//2)se agrega el tipo de servicio 		
		if (forma.getCriteriosBusqueda().containsKey(indices[9]))
			criterios.put(indices[9], forma.getCriteriosBusqueda(indices[9]));
		//3)se agrega el tipo de sala 		
		if (forma.getCriteriosBusqueda().containsKey(indices[21]))
			criterios.put(indices[21], forma.getCriteriosBusqueda(indices[21]));
		//4)se agrega el servicio	
		if (forma.getCriteriosBusqueda().containsKey(indices[13]))
			criterios.put(indices[13], forma.getCriteriosBusqueda(indices[13]));
		//5)se agrega el tipo de asocio	
		if (forma.getCriteriosBusqueda().containsKey(indices[12]))
			criterios.put(indices[12], forma.getCriteriosBusqueda(indices[12]));
		//6)se agrega el tipo de cirugia
		if (forma.getCriteriosBusqueda().containsKey(indices[10]))
			criterios.put(indices[10], forma.getCriteriosBusqueda(indices[10]));
		//7)se agrega continua medico 	
		if (forma.getCriteriosBusqueda().containsKey(indices[17]))
			criterios.put(indices[17], forma.getCriteriosBusqueda(indices[17]));
		//8)se agrega continua via de acceso	
		if (forma.getCriteriosBusqueda().containsKey(indices[18]))
			criterios.put(indices[18], forma.getCriteriosBusqueda(indices[18]));
		//9)se agrega el centro de costo 		
		if (forma.getCriteriosBusqueda().containsKey(indices[11]))
			criterios.put(indices[11], forma.getCriteriosBusqueda(indices[11]));
		//10)se agrega valor 		
		if (forma.getCriteriosBusqueda().containsKey(indices[19]))
			criterios.put(indices[19], forma.getCriteriosBusqueda(indices[19]));
		//11)se agrega el codigo de excepciones_qx	
		if (forma.getVigencias().containsKey(indices[6]+"0"))
			criterios.put(indices[6], forma.getCriteriosBusqueda(indices[6]));
		//12)se agrega la via de ingreso 		
		logger.info("====>Via Ingreso: "+forma.getCriteriosBusqueda(indices[26]));
		if (forma.getCriteriosBusqueda().containsKey(indices[26]))
			criterios.put(indices[26], forma.getCriteriosBusqueda(indices[26]));
		//13)se agrega el tipo paciente
		logger.info("====>Tipo Paciente: "+forma.getCriteriosBusqueda(indices[27]));
		if (forma.getCriteriosBusqueda().containsKey(indices[27]))
			criterios.put(indices[27], forma.getCriteriosBusqueda(indices[27]));
		
		forma.setExcepcionesQx(buscar2(connection, criterios));
		//se saca una copia de 
		Listado.copyMap(forma.getExcepcionesQx(), forma.getExcepcionesQxClone(), Integer.parseInt(forma.getExcepcionesQx("numRegistros")+""), indices);
		
		UtilidadBD.closeConnection(connection);
		return mapping.findForward("frame");
	}
	
	/**
	 * Metodo encargado de cargar el select tipo paciente
	 * segun lo seleccionado en el select via ingreso 
	 * @param con
	 * @param forma
	 * @param mapping 
	 * @param mundo 
	 * @return
	 */
	public static ActionForward cargarTipoPaciente(Connection con, ExcepcionesQXForm forma, ActionMapping mapping)
	{
		ArrayList tiposPaciente = new ArrayList();
		if(UtilidadCadena.noEsVacio(forma.getCriteriosBusqueda(indices[26])+""))
		{
			tiposPaciente = Utilidades.obtenerTiposPacientePorViaIngreso(con, forma.getCriteriosBusqueda(indices[26])+"");
			forma.setTiposPaciente(tiposPaciente);
		}
		//Reseteamos el select de tipo paciente
		else
			forma.setTiposPaciente(tiposPaciente);
		
		UtilidadBD.closeConnection(con);
		
		/*Validamos de donde se que estado se llama el método para
		 * retornar al forward correspondientes*/
		if(forma.getEstado().equals("recargar"))
			return mapping.findForward("frame");
		else if(forma.getEstado().equals("recargarBusqueda"))
			return mapping.findForward("busqueda");
		else
			return mapping.findForward("frame");
	}
	
	/**
	 * Cargar datos en el encabezado de la busqueda
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @return//
	 */
	public static ActionForward cargarencabezado (Connection connection,ExcepcionesQXForm forma, ActionMapping mapping)
	{
		logger.info("\n entre a cargar encabezado con index --> "+forma.getIndexDestino());
		
		//se carga la fecha inicial
		forma.setCriteriosBusqueda(indices[2], forma.getVigencias(indices[2]+forma.getIndexDestino()));
		//se carga la fecha final
		forma.setCriteriosBusqueda(indices[3], forma.getVigencias(indices[3]+forma.getIndexDestino()));
		//se carga el codigo del excepcion_qx
		if (forma.getVigencias().containsKey(indices[6]+forma.getIndexDestino()))
			forma.setCriteriosBusqueda(indices[6], forma.getVigencias(indices[6]+forma.getIndexDestino()));
				
		//aqui se divide el numero del contrato del esquema tarifario
		String contrato="";
		try 
		{
			contrato = (forma.getVigencias(indices[23]+forma.getIndexDestino())+"");
		} 
		catch (Exception e) 
		{
			logger.error("\n problema separando el contrato del tipo de tarifario oficial ");
		}
		
		//se carga el numero del contrato
		forma.setCriteriosBusqueda(indices[23], contrato);

		//Se cargan las Vias de Ingreso
		forma.setViasIngresos(Utilidades.obtenerViasIngreso(connection, ""));
		
		//initTipoLiquidacion(connection, tmp[1], forma);
		initTipoLiquidacion(connection, ConstantesBD.codigoNuncaValido+"", forma);
		try 
		{
			UtilidadBD.cerrarConexion(connection);
		} 
		catch (Exception e) 
		{
			 logger.info("problema cerrando la conexion "+e);
		}
		
		logger.info("\n :::::::::: EL VLAOR DEL CODIGO ES "+ forma.getCriteriosBusqueda());
				
		forma.reset();
		return mapping.findForward("frame");
	}
	
//*************************************************************************************************************************************************	
//*****************************DE AQUI EN ADELANTE SE ENCUENTRAN LOS METODOS PARA EL MANEJO DEL DETALLE DE EXCEPCIONES QX**************************
	
	/**
	 * metodo encargado de agregar nuevos datos
	 * al detalle .
	 * @param forma
	 * 
	 */
	public static ActionForward accionNueva (ExcepcionesQXForm forma,UsuarioBasico usuario,Connection connection,HttpServletResponse response, HttpServletRequest request )
	{
		logger.info("\n Entre a accionNueva con el hashmap "+forma.getVigencias());
		int pos = Integer.parseInt(forma.getExcepcionesQx("numRegistros").toString());
		
		//se el codigo de excepciones_qx
		forma.setExcepcionesQx(indices[6]+pos, forma.getCriteriosBusqueda(indices[6]));
		
		if (forma.getExcepcionesQx().containsKey(indices[8]+"0") && !(forma.getExcepcionesQx(indices[8]+"0")+"").equals("") && !(forma.getExcepcionesQx(indices[8]+"0")+"").equals(ConstantesBD.codigoNuncaValido+""))
			forma.setExcepcionesQx(indices[8]+pos,forma.getExcepcionesQx(indices[8]+"0") );
		//se agrega el centro costo 
		forma.setExcepcionesQx(indices[7]+pos, forma.getCriteriosBusqueda(indices[7]));
		//se agrega el tipo de servicio
		forma.setExcepcionesQx(indices[9]+pos, "");
		//se agrega el tipo de cirugia
		forma.setExcepcionesQx(indices[10]+pos, ConstantesBD.codigoNuncaValido);
		//se agrega el tipo de liquidacion
		forma.setExcepcionesQx(indices[11]+pos, ConstantesBD.codigoNuncaValido);
		//se agrega el tipo de asocio
		forma.setExcepcionesQx(indices[12]+pos, ConstantesBD.codigoNuncaValido);
		//se agrega el servicio
		forma.setExcepcionesQx(indices[13]+pos, ConstantesBD.codigoNuncaValido);
		//se agrega liquidar sobre asocio
		forma.setExcepcionesQx(indices[14]+pos, ConstantesBD.codigoNuncaValido);
		//se agrega la especialidad
		forma.setExcepcionesQx(indices[15]+pos, ConstantesBD.codigoNuncaValido);
		//se agrega el grupo del servicio
		forma.setExcepcionesQx(indices[16]+pos, ConstantesBD.codigoNuncaValido);
		//se agrega continua medico
		forma.setExcepcionesQx(indices[17]+pos, ConstantesBD.codigoNuncaValido);
		//se agrega continua via de acceso
		forma.setExcepcionesQx(indices[18]+pos, ConstantesBD.codigoNuncaValido);
		//se agrega el valor
		forma.setExcepcionesQx(indices[19]+pos, "");
		//se agrega el usuario 
		forma.setExcepcionesQx(indices[5]+pos, usuario.getLoginUsuario());
		//se agrega si esta en la BD
		forma.setExcepcionesQx(indices[20]+pos, ConstantesBD.acronimoNo);
		//codigo cups
		forma.setExcepcionesQx(indices[29]+pos, "");
		
		
		//se actualiza numRegistros
		forma.setExcepcionesQx("numRegistros", (pos+1)+"");
		//se agrega el indice del mapa
		forma.setExcepcionesQx("INDICES_MAPA", indices);
		try
		{
			//se cierra la conexion
			UtilidadBD.cerrarConexion(connection);
		} 
		catch (SQLException e)
		{
		 logger.info("problema cerrando la conexion en accionNuevaVigencias "+e);
		}
		
		//se redirecciona  a la pag
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getExcepcionesQx("numRegistros").toString()), response, request, "frameExcepcionesQX.jsp",true);		
	
	}
	
	/**
	 * Elimina un Registro del HashMap de excepTipoSala,
	 * Adicciona el Registro Eliminado en el HashMap de excepTipoSalaEliminados
	 * @param forma
	 * @param request
	 * @param response
	 * @param mapping
	 * @return ActionForward
	 */
	public static ActionForward accionEliminarCampo(ExcepcionesQXForm forma, HttpServletRequest request, HttpServletResponse response,int codigoInstitucion, ActionMapping mapping) 
	{
		//logger.info("Entra a accionEliminarCampo :::::::::::::::::::::::::::"+forma.getVigenciasEliminados());
		int numRegMapEliminados=Integer.parseInt(forma.getExcepcionesQxEliminados("numRegistros")+"");
		//logger.info(":::::::0:::::::::");
		//logger.info("accionEliminarCampo y el numRegMapEliminados"+numRegMapEliminados);
		int ultimaPosMapa=(Integer.parseInt(forma.getExcepcionesQx("numRegistros")+"")-1);
		//logger.info(":::::::1:::::::::");
		//poner la informacion en el otro mapa.
		//String[] indicesl= (String[])forma.getExcepTipoSala("INDICES_MAPA");		
		
		for(int i=0;i<indices.length;i++)
		{ 
			//logger.info(":::::::2:::::::::");
			//solo pasar al mapa los registros que son de BD
			if((forma.getExcepcionesQx(indices[20]+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
			{
				logger.info(":::::::3:::::::::");
				forma.setExcepcionesQxEliminados(indices[i]+""+numRegMapEliminados, forma.getExcepcionesQx(indices[i]+""+forma.getIndexEliminado()));
			}
		}		
		//logger.info(":::::::4:::::::::");
		if((forma.getExcepcionesQx(indices[20]+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
		{			
			logger.info(":::::::4:::::::::");
			forma.setExcepcionesQxEliminados("numRegistros", (numRegMapEliminados+1));
		}
		//logger.info(":::::::5:::::::::");
		//acomodar los registros del mapa en su nueva posicion
		for(int i=Integer.parseInt(forma.getIndexEliminado()+"");i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				forma.setExcepcionesQx(indices[j]+""+i,forma.getExcepcionesQx(indices[j]+""+(i+1)));
			}
		}
		//logger.info(":::::::6:::::::::");
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getExcepcionesQx().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamos el numero de registros en el mapa.
		forma.setExcepcionesQx("numRegistros",ultimaPosMapa);
		
		logger.info(" el valor de excepciones "+forma.getExcepcionesQx());
		logger.info(" el valor de excepciones eliminadas "+forma.getExcepcionesQxEliminados());
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(codigoInstitucion),Integer.parseInt(forma.getExcepcionesQx("numRegistros").toString()), response, request, "frameExcepcionesQX.jsp",Integer.parseInt(forma.getIndexEliminado().toString())==ultimaPosMapa);
		
		
	}
	
	
	/**
	 * Metodo encargado de realizar las operaciones en la BD
	 * Insertar, Eliminar y Modificar (detalle de excepciones Qx)
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	public  ActionForward accionGuardarRegistros(Connection connection, ExcepcionesQXForm forma, ActionMapping mapping,UsuarioBasico usuario)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		logger.info("----->INICIANDO TRANSACCION ....");
		logger.info("valor del hasmap al entar a accionGuardarRegistros "+forma.getExcepcionesQx());
		logger.info("valor del hasmap al entar a accionGuardarRegistros eliminados "+forma.getExcepcionesQxEliminados());
		int codigo=ConstantesBD.codigoNuncaValido;
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getExcepcionesQxEliminados("numRegistros")+"");i++)
		{
		//	logger.info("Entro a eliminar BD :::::::::::::contador I...>"+i);
		//	logger.info("Hashmap:::::::::::"+forma.getAsociosXRangoTiempoEliminadoMap());
			if (transacction)
				transacction=eliminarBD(connection, forma, i,usuario.getCodigoInstitucionInt());		
		}		
		
		for(int i=0;i<Integer.parseInt(forma.getExcepcionesQx("numRegistros")+"");i++)
		{		
			//modificar			
			if((forma.getExcepcionesQx(indices[20]+i)+"").trim().equals(ConstantesBD.acronimoSi) && transacction)
			{
				codigo= Utilidades.convertirAEntero(forma.getExcepcionesQx(indices[8]+i)+"");
				HashMap tmp1 = new HashMap ();
				HashMap tmp2 = new HashMap ();
				tmp1=Listado.copyOnIndexMap(forma.getExcepcionesQxClone(), i+"", indices);
				logger.info("estoy en modificar, el valor tmp "+tmp1);
				if(existeModificacion(forma,i,tmp1))
				{	
					logger.info("\n::::::::::::::: SI EXISTE MODIFICACION:::::::::\n");						
				
					logger.info("ENTRO A antes de copyindexmap::::::::..");
					tmp2=Listado.copyOnIndexMap(forma.getExcepcionesQx(), i+"", indices);
					logger.info("EL VALOR DE TMP2 ES "+tmp2);
					transacction = modificarBD(connection, tmp2, usuario);
				}
			}
			
			//insertar			
			else if((forma.getExcepcionesQx(indices[20]+i)+"").trim().equals(ConstantesBD.acronimoNo) && transacction)
			{
				//Llenamos la variable via ingreso y tipo paciente
				if(UtilidadCadena.noEsVacio(forma.getCriteriosBusqueda(indices[26])+""))
					forma.setExcepcionesQx(indices[26]+i, forma.getCriteriosBusqueda(indices[26]));
				else
					forma.setExcepcionesQx(indices[26]+i, ConstantesBD.codigoNuncaValido);
				
				if(UtilidadCadena.noEsVacio(forma.getCriteriosBusqueda(indices[27])+""))
					forma.setExcepcionesQx(indices[27]+i, forma.getCriteriosBusqueda(indices[27]));
				else
					forma.setExcepcionesQx(indices[27]+i, ConstantesBD.codigoNuncaValido);
				
				logger.info("===>Via Ingreso: "+forma.getExcepcionesQx(indices[26]+i));
				logger.info("===>Tipo Paciente: "+forma.getExcepcionesQx(indices[27]+i));
				
				HashMap tmp = new HashMap ();
				//se copian los datos a un hashmap de una sola posicion la "0"
				tmp=Listado.copyOnIndexMap(forma.getExcepcionesQx(), i+"",indices);
				//se inserta en la BD
				if (i>0)
					tmp.put(indices[8]+"0",codigo);
				
				codigo=insertarBD(connection, tmp, usuario);
				if (codigo>0)
					transacction=true;
				else
					transacction=false;
				 
			}
			
			// logger.info("\n\n valor i >> "+i);
		}
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
			forma.reserEliminados();
			logger.info("----->TRANSACCION AL 100% ....");
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}
		
		
		
		return buscarCentroCosto(connection, forma, mapping);
	}
	
	
	/**
 	 *Metodo encargado de insertar los datos a la BD
	 * @param connection
	 * @param temp
	 * @param usuario
	 * @return
	 */
	private static int insertarBD (Connection connection, HashMap temp,UsuarioBasico usuario)
	{
		logger.info("\n\n::::::::::::ENTRO A INSERTARBD::::::\n\n");
		logger.info("valor de haspmap al entrar"+temp);
		
		HashMap datos = new HashMap ();
		
		//1) se agrega el codigo de ExcepcionQx, este es requerido si el 
		//el el codigo ExcepcionQxCc es igual a -1		
		datos.put(indices[6], temp.get(indices[6]+"0"));
		//2) se agrega el Centro de costo
		datos.put(indices[7], temp.get(indices[7]+"0"));
		//3) se agrega el codigo excepcion Qx Cc
		if (temp.containsKey(indices[8]+"0") && !(temp.get(indices[8]+"0")+"").equals("") && !(temp.get(indices[8]+"0")+"").equals(ConstantesBD.codigoNuncaValido+""))
			datos.put(indices[8], temp.get(indices[8]+"0"));
		//4) se agrega el tipo de servicio
		datos.put(indices[9], temp.get(indices[9]+"0"));
		//5) se agrega el tipo de cirugia
		datos.put(indices[10], temp.get(indices[10]+"0"));
		//6) se agrega el tipo de liquidacion
		datos.put(indices[11], temp.get(indices[11]+"0"));
		//7) se agrega el tipo de Asocio
		datos.put(indices[12], temp.get(indices[12]+"0"));
		//8) se agrega el tipo el servicio
		datos.put(indices[13], temp.get(indices[13]+"0"));
		//9) se agrega liquidar sobre asocio
		datos.put(indices[14], temp.get(indices[14]+"0"));	
		//10) se agrega la especialidad
		datos.put(indices[15], temp.get(indices[15]+"0"));
		//11) se agregael grupo de servicio
		datos.put(indices[16], temp.get(indices[16]+"0"));
		//12) se agrega continua medico
		datos.put(indices[17], temp.get(indices[17]+"0"));
		//13) se agrega continua  via de acceso
		datos.put(indices[18], temp.get(indices[18]+"0"));
		//14) se agrega el valor
		datos.put(indices[19], temp.get(indices[19]+"0"));
		//15) se agrega el usuario que modifica
		datos.put(indices[5], temp.get(indices[5]+"0"));
		//16) se agrega el tipo sala
		datos.put(indices[21], temp.get(indices[21]+"0"));
		//17) se agrega el campo via ingreso
		datos.put(indices[26], temp.get(indices[26]+"0"));
		//18) se agrega el campo tipo paciente
		datos.put(indices[27], temp.get(indices[27]+"0"));
		//19) se agrega el campo liquidar % Cx. Múlt.
		datos.put(indices[28], temp.get(indices[28]+"0"));
		
		return insertarGeneral(connection, datos);
	}
	
	/**
 	 *Metodo encargado de insertar los datos a la BD
	 * @param connection
	 * @param temp
	 * @param usuario
	 * @return
	 */
	private static boolean modificarBD (Connection connection, HashMap temp,UsuarioBasico usuario)
	{
		logger.info("\n\n::::::::::::ENTRO A MODICARBD::::::\n\n");
		logger.info("valor de haspmap al entrar"+temp);
		
		HashMap datos = new HashMap ();
		
		//4) se agrega el tipo de servicio
		datos.put(indices[9], temp.get(indices[9]+"0"));
		//5) se agrega el tipo de cirugia
		datos.put(indices[10], temp.get(indices[10]+"0"));
		//6) se agrega el tipo de liquidacion
		datos.put(indices[11], temp.get(indices[11]+"0"));
		//7) se agrega el tipo de Asocio
		datos.put(indices[12], temp.get(indices[12]+"0"));
		//8) se agrega el tipo el servicio
		datos.put(indices[13], temp.get(indices[13]+"0"));
		//9) se agrega liquidar sobre asocio
		datos.put(indices[14], temp.get(indices[14]+"0"));	
		//10) se agrega la especialidad
		datos.put(indices[15], temp.get(indices[15]+"0"));
		//11) se agregael grupo de servicio
		datos.put(indices[16], temp.get(indices[16]+"0"));
		//12) se agrega continua medico
		datos.put(indices[17], temp.get(indices[17]+"0"));
		//13) se agrega continua  via de acceso
		datos.put(indices[18], temp.get(indices[18]+"0"));
		//14) se agrega el valor
		datos.put(indices[19], temp.get(indices[19]+"0"));
		//15) se agrega el usuario que modifica
		datos.put(indices[5], usuario.getLoginUsuario());
		//16) se agrega el tipo sala
		datos.put(indices[21], temp.get(indices[21]+"0"));
		//17) se agrega en codigo detCodigoQxCc
		datos.put(indices[0], temp.get(indices[0]+"0"));
		//18) se agrega el campo liq. % Cirugía Múltiple
		datos.put(indices[28], temp.get(indices[28]+"0"));
		
		return modificar1(connection, datos);
	}
	
	/**
	 * Carga los datos a eliminar en la BD; el parametro "i"
	 * es el encargado de manejar la posicion dentro del HashMap
	 * @param connection
	 * @param forma
	 * codigoExcepcionQxCc8_ --> Requerido
	 * -- detCodigoExcepcionQxCc0_ --> Requerido
	 * @return
	 */
	private static boolean eliminarBD (Connection connection, ExcepcionesQXForm forma,int i,int institucion)
	{
		logger.info(":::::::::ENTRO A ELIMINARBD::::::::::::");
		logger.info("\n el valor de i es "+ i);
		logger.info("\n excepciones eliminados "+forma.getExcepcionesQxEliminados());
		logger.info("\n*****************************************************\n\n");
		
		HashMap datos = new HashMap ();
		//codigo excepcionesQxCc
		datos.put(indices[8], forma.getExcepcionesQxEliminados(indices[8]+i));
	
		//codigo detCodigoExcepcioQxCc
		datos.put(indices[0], forma.getExcepcionesQxEliminados(indices[0]+i));
		
	
		
		return eliminarGeneral(connection, datos);
 
	}
	
	/**
	 * verifica si el registro se ha modificado
	 * @param connection
	 * @param forma
	 * @param mundo
	 * @param int pos
	 * */
	private static boolean existeModificacion (ExcepcionesQXForm forma, int pos,HashMap temp)
	{
		logger.info("\n EXISTEMODIFICACION :::::::::: ");
		logger.info("\n\n ::: original ::::::"+temp);
		logger.info("\n\n ::: modificada ::::::"+forma.getExcepcionesQx());
				
		
		for(int i=0;i<indices.length;i++)
		{		//logger.info("::::: "+i+" :::::");
			if(temp.containsKey(indices[i]+"0") && forma.getExcepcionesQx().containsKey(indices[i]+pos))
			{				
				if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getExcepcionesQx(indices[i]+pos)+"").trim())))
				{
							
					return true;
					
				}				
			}
		}	

		
		return false;		
	}	
	
	/**
	 * Metodo encargado de validar las vigencias
	 * 
	 * @param forma
	 * @param errores
	 * @return
	 */
	public static ActionErrors accionValidar(ExcepcionesQXForm forma,ActionErrors errores)
	{
		
		if (forma.getEstado().equals("guardarvig"))
		{
			logger.info("\n ::::::::::ENTRO A GUARDAR VIG EN FORM :::::: ");
			for (int i= 0; i < Integer.parseInt(forma.getVigencias("numRegistros")+"");i++)
			{
				//1)se valida que la fecha inicial no venga vacia
				if ((forma.getVigencias(indices[2]+i)).equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial del registro "+(i+1)));
				
				//2)se valida que la fecha Final no venga vacia
				if ((forma.getVigencias(indices[3]+i)).equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","La Fecha Final del registro "+(i+1)));
				else
				{
				
					if ( forma.getVigencias().containsKey(indices[2]+i) && forma.getVigencias().containsKey(indices[3]+i) &&
							!forma.getVigencias(indices[2]+i).equals("") && !forma.getVigencias(indices[3]+i).equals("") &&
							!UtilidadFecha.esFechaMenorIgualQueOtraReferencia((forma.getVigencias(indices[2]+i)+""), (forma.getVigencias(indices[3]+i)+"")))
						errores.add("descripcion",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia"," Final "+forma.getVigencias(indices[3]+i)+" del registro "+(i+1), "Inicial "+forma.getVigencias(indices[2]+i)));
					
					if (forma.getVigencias().containsKey(indices[20]+i) && (forma.getVigencias(indices[20]+i)+"").equals(ConstantesBD.acronimoNo))
						if ( forma.getVigencias().containsKey(indices[3]+i) && !forma.getVigencias(indices[3]+i).equals("") &&
							!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), (forma.getVigencias(indices[3]+i)+"")))
							errores.add("descripcion",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia"," Final "+forma.getVigencias(indices[3]+i)+" del registro "+(i+1), "Actual "+UtilidadFecha.getFechaActual()));
				}	
				//3)se valida que el contrato venga
				if ((forma.getVigencias(indices[23]+i)).equals("") || (forma.getVigencias(indices[23]+i)+"").equals(ConstantesBD.codigoNuncaValido+""))
					errores.add("descripcion",new ActionMessage("errors.required","El Contrato del registro "+(i+1)));
				
				
				
				for (int j= i; j < Integer.parseInt(forma.getVigencias("numRegistros")+"");j++)
				{
					if (j>i && !(forma.getVigencias(indices[2]+i)+"").equals("") && !(forma.getVigencias(indices[3]+i)+"").equals("") && !(forma.getVigencias(indices[2]+j)+"").equals("") && !(forma.getVigencias(indices[3]+j)+"").equals(""))
					{
						// se pregunta por el cruze de fechas
						if ((forma.getVigencias(indices[23]+i)+"").equals((forma.getVigencias(indices[23]+j)+"")))
							if (UtilidadFecha.existeTraslapeEntreFechas((forma.getVigencias(indices[2]+i)+""), (forma.getVigencias(indices[3]+i)+""), (forma.getVigencias(indices[2]+j)+""), (forma.getVigencias(indices[3]+j)+"")))
							{
								errores.add("descripcion",new ActionMessage("error.rangoFechasInvalido",(forma.getVigencias(indices[2]+j)+""),
										(forma.getVigencias(indices[3]+j)+"")+" del registro "+(j+1),(forma.getVigencias(indices[2]+i)+""),
										(forma.getVigencias(indices[3]+i)+"")+" del registro "+(i+1)));
								
								j= Integer.parseInt(forma.getVigencias("numRegistros")+"");
							}
					}
				
				
				}
				
				
				//se debe de validar el Unique que consta de los siguientes campos:
				//-- Contrato
				//-- Fecha Inicial
				//-- Fecha Final
			
				
				String unique1=forma.getVigencias(indices[23]+i)+""+forma.getVigencias(indices[2]+i)+forma.getVigencias(indices[3]+i);
				
				for (int k= 0; k!=i && k < Integer.parseInt(forma.getVigencias("numRegistros")+"");k++)
				{
					String unique2= forma.getVigencias(indices[23]+k)+""+forma.getVigencias(indices[2]+k)+forma.getVigencias(indices[3]+k);
					
					/*logger.info("\n :::::::::::Las cadenas a comparar son :::::::::::: k->"+k+" i->"+i);
					logger.info("\n::: unique1 ==>"+unique1);
					logger.info("\n::: unique2 ==>"+unique2);
					logger.info("\n ::::::::::::::::::::::::::::::::::::::::::::::::::::");
					*/
					if (unique1.equals(unique2))
					{
						logger.info("\n :::::::::::entre a son iguales :::::::::::: k->"+k+" i->"+i);
						errores.add("descripcion",new ActionMessage("error.noRegistroMismaInformacion","Fecha Inicial, Fecha Final  y Contrato en el registro "+(i+1)));		
					}
				
				}
				
				
			}
			
		}
		
		
		
		return errores;
	}
	
	
	/**
	 * metodo encargado del ordenamiento de la forma 
	 * las vigencias
	 * 
	 * @param forma
	 */
	public static void accionOrdenarMapaVig(ExcepcionesQXForm forma)
	{
		
		int numReg = Integer.parseInt(forma.getVigencias("numRegistros")+"");
		forma.setVigencias(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getVigencias(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setVigencias("numRegistros",numReg+"");
		forma.setVigencias("INDICES_MAPA",indices);		
	}
	
	/**
	 * metodo encargado del ordenamiento  
	 * @param forma
	 */
	public static void accionOrdenarMapa(ExcepcionesQXForm forma)
	{
		
		int numReg = Integer.parseInt(forma.getExcepcionesQx("numRegistros")+"");
		forma.setExcepcionesQx(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getExcepcionesQx(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setExcepcionesQx("numRegistros",numReg+"");
		forma.setExcepcionesQx("INDICES_MAPA",indices);		
	}
	
	public static ActionForward busqueda (Connection connection,ExcepcionesQXForm forma, ActionMapping mapping)
	{
		// formatear el pager
		forma.resetPager();
		//se formatean los valores existentes en la busqueda
		forma.reset();
		forma.resetBusquedaAvanzada();
		forma.setCriteriosBusqueda("seccionBusquedaAvanzada", true);
		try 
		{
			UtilidadBD.cerrarConexion(connection);
		}
		catch (Exception e)
		{
			logger.info("\n Problema cerrando la conexion ");
		}
		
		return mapping.findForward("busqueda");
	}
	
	
	/**
	 * Metodo encargado de buscar el centro de costo
	 */
	public static ActionForward busquedaAvanzada (Connection connection, ExcepcionesQXForm forma,ActionMapping mapping)
	{
		HashMap criterios = new HashMap ();
		logger.info("\n el valor de las excepciones "+forma.getVigencias());
		logger.info("\n el valor de criterios busqueda "+forma.getCriteriosBusqueda());
		//se formatea el pager
		forma.resetPager();
		
		//1)se agrega el centro de costo 		
		if (forma.getCriteriosBusqueda().containsKey(indices[7]))
			criterios.put(indices[7], forma.getCriteriosBusqueda(indices[7]));
		//2)se agrega el tipo de servicio 		
		if (forma.getCriteriosBusqueda().containsKey(indices[9]))
			criterios.put(indices[9], forma.getCriteriosBusqueda(indices[9]));
		//3)se agrega el tipo de sala 		
		if (forma.getCriteriosBusqueda().containsKey(indices[21]))
			criterios.put(indices[21], forma.getCriteriosBusqueda(indices[21]));
		//4)se agrega el servicio	
		if (forma.getCriteriosBusqueda().containsKey(indices[13]))
			criterios.put(indices[13], forma.getCriteriosBusqueda(indices[13]));
		//5)se agrega el tipo de asocio	
		if (forma.getCriteriosBusqueda().containsKey(indices[12]))
			criterios.put(indices[12], forma.getCriteriosBusqueda(indices[12]));
		//6)se agrega el tipo de cirugia
		if (forma.getCriteriosBusqueda().containsKey(indices[10]))
			criterios.put(indices[10], forma.getCriteriosBusqueda(indices[10]));
		//7)se agrega continua medico 	
		if (forma.getCriteriosBusqueda().containsKey(indices[17]))
			criterios.put(indices[17], forma.getCriteriosBusqueda(indices[17]));
		//8)se agrega continua via de acceso	
		if (forma.getCriteriosBusqueda().containsKey(indices[18]))
			criterios.put(indices[18], forma.getCriteriosBusqueda(indices[18]));
		//9)se agrega el tipo de liquidacion		
		if (forma.getCriteriosBusqueda().containsKey(indices[11]))
			criterios.put(indices[11], forma.getCriteriosBusqueda(indices[11]));
		//10)se agrega valor 		
		if (forma.getCriteriosBusqueda().containsKey(indices[19]))
			criterios.put(indices[19], forma.getCriteriosBusqueda(indices[19]));
		//11)se agrega el codigo de excepciones_qx	
		if (forma.getVigencias().containsKey(indices[6]+"0"))
			criterios.put(indices[6], forma.getCriteriosBusqueda(indices[6]));
		//12)se agrega la especialidad	
		if (forma.getCriteriosBusqueda().containsKey(indices[15]))
			criterios.put(indices[15], forma.getCriteriosBusqueda(indices[15]));
		//13)se agrega el grupo de servicio
		if (forma.getCriteriosBusqueda().containsKey(indices[16]))
			criterios.put(indices[16], forma.getCriteriosBusqueda(indices[16]));
		//14)se agrega la via de ingreso
		if (forma.getCriteriosBusqueda().containsKey(indices[26]))
			criterios.put(indices[26], forma.getCriteriosBusqueda(indices[26]));
		//15)se agrega el tipo paciente
		if (forma.getCriteriosBusqueda().containsKey(indices[27]))
			criterios.put(indices[27], forma.getCriteriosBusqueda(indices[27]));
		
		
		forma.setExcepcionesQx(buscar2(connection, criterios));
		//se saca una copia de 
		//Listado.copyMap(forma.getExcepcionesQx(), forma.getExcepcionesQxClone(), Integer.parseInt(forma.getExcepcionesQx("numRegistros")+""), indices);
		
		try 
		{
		 UtilidadBD.cerrarConexion(connection);	
		}
		catch (Exception e) 
		{
			logger.info("\n Problema Cerrando la conexion "+e);
		}
		return mapping.findForward("busqueda");
	}
	
	
	
}


