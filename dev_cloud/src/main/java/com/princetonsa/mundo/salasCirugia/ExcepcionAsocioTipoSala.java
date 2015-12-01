/*
 * Sep 20, 2005
 *
 */
package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import com.princetonsa.actionform.salasCirugia.ExcepcionAsocioTipoSalaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ExcepcionAsocioTipoSalaDao;
import com.princetonsa.dao.sqlbase.SqlBaseExcepcionAsocioTipoSalaDao;
import com.princetonsa.mundo.UsuarioBasico;


/**
 * Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Parametrización de Excepción Asocio Tipo Sala
 * Modificada el 21/11/07 por Jhony Alexander Duque A.
 * por documento 525 --> Cambio en Funcionalidades de parametrizacion
 * 						 Cirugia por DyT.
 */
public class ExcepcionAsocioTipoSala 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(ExcepcionAsocioTipoSala.class);
	
	/**
	 * inidices que maneja que manejan los hashmap
	 */
	public final static String [] indices = SqlBaseExcepcionAsocioTipoSalaDao.indices0;
	
	
	
	/**
	 * Instancia el Dao
	 * */
	public static ExcepcionAsocioTipoSalaDao getExcepcionAsocioTipoSalaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionAsocioTipoSalaDao();
	}	
	
	//*****************MÉTODOS************************************************
	
	/**
	 *Metodo encargado de modificar los datos de la tabla det_ex_asocio_tipo_sala
	 *Los parametros son:
	 *@Connection connection
	 *@HashMap datos
	 *------------------------------------
	 *	     KEY'S DEL HASHMAP DATOS
	 *------------------------------------
	 * -- tipoServicio --> Opcional
	 * -- tipoCirugia --> Opcional
	 * -- tipoLiquidacion --> Requerido
	 * -- cantidad --> Requerido
	 * -- liquidarSobreAsocio --> Opcional
	 * -- asocio --> Requerido
	 * -- codigo --> Requerido
	 * -- codExAsocTipSala --> Requerido
	 * -- usuarioModifica --> Requerido
	 * ------------------------------------
	 * @return boolean
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.		
	 **/
	public static boolean modificar1 (Connection connection,HashMap datos)
	{
		return getExcepcionAsocioTipoSalaDao().modificar1(connection, datos);
	}
	
	/**
	 * Metodo encargado de consultar la informacion  de las tablas
	 * det_ex_asocio_tipo_sala y  ex_asocio_tipo_sala
	 * Los parametros son :
	 * @Connection connection
	 * @HashMap Criterios
	 * ------------------------------
	 * KEY'S DEL HASHMAP CRITERIOS
	 * ------------------------------
	 * -- codigo --> Requerido
	 * -- institucion --> Requerido
	 * -- tipoServicio --> Opcional
	 * -- tipoCirugia --> Opcional
	 * -- tipoSala --> Opcional
	 * -- asocio --> Opcional
	 * -- tipoLiquidacion --> Opcional
	 * -- liquidarSobreAsocio --> Opcional
	 * -- esqTarPart --> Opcional
	 * -- esqTarGen --> Opcional
	 * -- tipoEsqTar --> Requerido
	 * --------------------------------
	 * @return HashMap
	 * -------------------------------
	 * KEY'S DEL HASHMAP QUE RETORNA
	 * -------------------------------
	 * --codigo_
	 * --codExAsocTipSala_
	 * --tipoServicio_
	 * --tipoCirugia_
	 * --tipoLiquidacion_
	 * --cantidad_
	 * --asocio_
	 * --liquidarSobreAsocio_
	 * --estaBd_
	 * --usuarioModifica_
	 * --tipoSala_
	 **/
	public static HashMap buscar (Connection connection,HashMap criterios)
	{
		return getExcepcionAsocioTipoSalaDao().buscar(connection, criterios);
	}
	
	/**
	 * Metodo encargado de insertar los datos tango en el encabezado
	 *  (ex_asocio_tipo_sala) como en el cuerpo (det_ex_asocio_tipo_sala) 
	 * Los Parametros son:
	 * ---------------------------------------
	 * LOS KEY'S DEL HASHMAP DATOS SON
	 * ---------------------------------------
	 * -- esqTarParticular --> Opcional
	 * -- esqTarGeneral --> Opcional
	 * -- tipoSala --> Requerido
	 * -- institucion --> Requerido
	 * -- usuarioModifica --> Requerido 
	 * -- codExAsocTipSala --> Requerido
	 * -- tipoServicio --> Opcional
	 * -- tipoCirugia --> Opcional
	 * -- tipoLiquidacion --> Requerido
	 * -- cantidad --> Requerido
	 * -- liquidarSobreAsocio --> Opcional
	 * -- asocio --> Requerido
	 * -- usuarioModifica --> Requerido
	 * @return boolean
	 *	--true --> la operacion re realizo correctamente
	 *  --false --> la operacion se aborto.			
	 **/
	public static boolean insertarGeneral (Connection connection, HashMap datos) 
	{
		return getExcepcionAsocioTipoSalaDao().insertarGeneral(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de determinar en que porte hace el borrado
	 * de los datos, ya sea en el encabezado o en el detalle
	 * @param connection
	 * @param datos
	 * ------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * ------------------------------
	 *-- codigo0_ --> Requerido
	 *-- codExAsocTipSala1_ --> Requerido
	 *-- institucion14_ --> Requerido
	 * @return
	 */
	public static boolean eliminarGeneral (Connection connection, HashMap datos)
	{
		return getExcepcionAsocioTipoSalaDao().eliminarGeneral(connection, datos);
	}
	
	/*--------------------------------------------------
	 * Metodos  Adicionados por Jhony Alexander Duque A.
	 ---------------------------------------------------*/
	
	public static void initSelect (Connection connection, ExcepcionAsocioTipoSalaForm forma,UsuarioBasico usuario)
	{
		//se cargan los tipos de sala
		forma.setTipoSala(Utilidades.obtenerTiposSala(connection,usuario.getCodigoInstitucionInt(),"","f"));
		
		//se cargan todos los valores de esquema tarifario tanto generales como especificos
		forma.setEsqTarFio(Utilidades.obtenerEsquemasTarifariosGenPartInArray(connection, usuario.getCodigoInstitucionInt(),"porcentajescx"));
		
	}
	
	
	/**
	 * Método usado para prepararlos datos en el inicio
	 * de la opcion Ingresar/Modificar Excepciones de Asocios
	 * @param con
	 * @param excepcionesForm
	 * @param mapping
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	public static ActionForward accionEmpezar(Connection con, ExcepcionAsocioTipoSalaForm forma, ActionMapping mapping, UsuarioBasico usuario) throws SQLException 
	{
			
			//limpiar datos
			forma.reset();
			forma.resetBusqueda();
			forma.resetPager();
	
			initSelect(con, forma, usuario);
			cargarValoresSelects(con, forma);
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		
	}
	
	
	/**
	 * Método usado para prepararlos datos en el inicio
	 * de la opcion Ingresar/Modificar Excepciones de Asocios
	 * @param con
	 * @param excepcionesForm
	 * @param mapping
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	public static ActionForward accionEmpezarConsulta(Connection con, ExcepcionAsocioTipoSalaForm forma, ActionMapping mapping, UsuarioBasico usuario) throws SQLException 
	{
			
			//limpiar datos
			forma.reset();
			forma.resetBusqueda();
			forma.resetPager();
			forma.resetSelect();
	
			initSelect(con, forma, usuario);
			cargarValoresSelects(con, forma);
			
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		
	}
	
public static ActionForward accionBuscar (Connection connection,ExcepcionAsocioTipoSalaForm forma, ActionMapping mapping,UsuarioBasico usuario) 
	{
		logger.info("\n ::::ENTRE A ACCION BUSCAR:::");
		logger.info("\n Hashmap criterios " + forma.getCriteriosBusqueda());
		//se formatea la variable donde se almacenan los datos de la busqueda
		forma.reset();
		//se formatea los datos del pager
		forma.resetPager();
		HashMap criterios = new HashMap ();
		
		if (forma.getCriteriosBusqueda().containsKey(indices[15]) && UtilidadCadena.noEsVacio(forma.getCriteriosBusqueda(indices[15])+"") && !(forma.getCriteriosBusqueda(indices[15])+"").equals("-1") )
		{
			
			//se pregunta por el codigo en el encabezado para ese esquema tarifario
			
			// datos [0] ==> codigo Esquema tarifario particular
			// datos [1] ==> tipo de esquema tarifario
			String [] datos ={"-1","-1","-1"};
			try 
			{
				datos = (forma.getCriteriosBusqueda(indices[15])+"").split(ConstantesBD.separadorSplit);
			} 
			catch (Exception e) 
			{
				logger.error("\n problema separando el codigo del esquema tarifario del tipo");
				
			}
			//se arman los datos para el criterios de busqueda.
			//1)se le ingresa el tipo de esquema tarifario  "particular o General"
			criterios.put(indices[13],datos[1]);
			
			//2) se valida de que el tipo del tarifario es general
			if (datos[1].equals("0"))
			{
				//aqui se le ingresa el valor a tarifario general
				criterios.put(indices[12], datos[0]);
				//aqui se le pone el valor a tarifario particular en -1
				criterios.put(indices[11], ConstantesBD.codigoNuncaValido);
			}
			else//3) se valida de que tipo de tarifario es particular
				if (datos[1].equals("1"))
				{
					//aqui se le ingresa el valor al tarifario particular
					criterios.put(indices[11], datos[0]);
					//aqui se le pone el valor a tarifario general en -1
					criterios.put(indices[12], ConstantesBD.codigoNuncaValido);
				}
			
			//4)se valida de que el tipo de sala venga en los criterios de busqueda
			if (forma.getCriteriosBusqueda().containsKey(indices[10]) && !forma.getCriteriosBusqueda(indices[10]).equals("") && !(forma.getCriteriosBusqueda(indices[10])+"").equals("-1"))
				criterios.put(indices[10], forma.getCriteriosBusqueda(indices[10]));
			
			//5)se valida de que el tipo de servicio venga en los criterios de busqueda
			if (forma.getCriteriosBusqueda().containsKey(indices[2]) && !forma.getCriteriosBusqueda(indices[2]).equals("") && !(forma.getCriteriosBusqueda(indices[2])+"").equals("-1"))
				criterios.put(indices[2], forma.getCriteriosBusqueda(indices[2]));
			
			//6)se valida de que el tipo de cirugia venga en los criterios de busqueda
			if (forma.getCriteriosBusqueda().containsKey(indices[3]) && !forma.getCriteriosBusqueda(indices[3]).equals("") && !(forma.getCriteriosBusqueda(indices[3])+"").equals("-1"))
				criterios.put(indices[3], forma.getCriteriosBusqueda(indices[3]));
			
			//7)se valida de que el asocio venga en los criterios de busqueda
			if (forma.getCriteriosBusqueda().containsKey(indices[6]) && !forma.getCriteriosBusqueda(indices[6]).equals("") && !(forma.getCriteriosBusqueda(indices[6])+"").equals("-1"))
				criterios.put(indices[6], forma.getCriteriosBusqueda(indices[6]));
			
			//8)se valida de que el tipo de liquidacion venga en los criterios de busqueda
			if (forma.getCriteriosBusqueda().containsKey(indices[4]) && !forma.getCriteriosBusqueda(indices[4]).equals("") && !(forma.getCriteriosBusqueda(indices[4])+"").equals("-1"))
				criterios.put(indices[4], forma.getCriteriosBusqueda(indices[4]));
			
			//se inicializa la institucion
			criterios.put(indices[14], usuario.getCodigoInstitucionInt());
		
		}
		//logger.info("\n Hashmap criterios antes de entrar a buscar" + criterios);
		//se hace la busqueda por los criterios escojidos
		forma.setExcepTipoSala(buscar(connection, criterios));
		Listado.copyMap(forma.getExcepTipoSala(), forma.getExcepTipoSalaClone(), Integer.parseInt(forma.getExcepTipoSala("numRegistros")+""), indices);
		
		//logger.info("\n el hashmap original "+forma.getExcepTipoSalaClone());
		//se cargan los select con los datos
		cargarValoresSelects(connection, forma);
		
		try	
		{
			UtilidadBD.cerrarConexion(connection);
		}
		catch (Exception e)
		{
			logger.info("problema cerrando la conexion "+e);
		}
		
		
		return mapping.findForward("principal");
		
	}
	
	
	/**
	 * metodo encargado de agregar nuevos datos a la forma
	 * @param forma
	 * @throws SQLException 
	 */
public static ActionForward accionNuevo(ExcepcionAsocioTipoSalaForm forma,UsuarioBasico usuario,Connection connection, ActionMapping mapping, HttpServletRequest request, HttpServletResponse  response, int codogoInst ) throws SQLException
	{
		logger.info("\n Entre a accionNuevo con el hashmap "+forma.getCriteriosBusqueda());
		int pos = Integer.parseInt(forma.getExcepTipoSala("numRegistros").toString());
		//String[] indices = (String[])forma.getExcepTipoSala("INDICES_MAPA");
		
		
			//en esta parte se valida si existen datos ya parametrizados para ese esquema tarifario y  tipo sala
			
				if (forma.getExcepTipoSala().containsKey(indices[1]+(pos-1)) && !forma.getExcepTipoSala(indices[1]+(pos-1)).equals("") && !forma.getExcepTipoSala(indices[1]+(pos-1)).equals("-1"))
					//se verifica si ya existe encabezado para ese esquema tarifario"codigoCab"
					forma.setExcepTipoSala(indices[1]+pos, forma.getExcepTipoSala(indices[1]+(pos-1)));
				else
					forma.setExcepTipoSala(indices[1]+pos, ConstantesBD.codigoNuncaValido);
			
			
				if (forma.getExcepTipoSala().containsKey(indices[1]+pos) && !forma.getExcepTipoSala(indices[1]+pos).equals("") && !forma.getExcepTipoSala(indices[1]+pos).equals("-1"))
				{
					String [] datos ={"-1","-1","-1"};
					try 
					{	//se divide el codigo y el tipo de tarifario
						datos = (forma.getCriteriosBusqueda(indices[15])+"").split(ConstantesBD.separadorSplit);
					} 
					catch (Exception e) 
					{
						logger.error("\n problema separando el codigo del esquema tarifario del tipo");
						
					}
					
					//se arman los datos para el criterios de busqueda.
					//1)se le ingresa el tipo de esquema tarifario  "particular o General"
					forma.setExcepTipoSala(indices[13]+pos,datos[1]);
					
					//2) se valida de que el tipo del tarifario es general
					if (datos[1].equals("0"))
					{
						//aqui se le ingresa el valor a tarifario general
						forma.setExcepTipoSala(indices[12]+pos, datos[0]);
						//aqui se le pone el valor a tarifario particular en -1
						forma.setExcepTipoSala(indices[11]+pos, ConstantesBD.codigoNuncaValido);
					}
					else//3) se valida de que tipo de tarifario es particular
						if (datos[1].equals("1"))
						{
							//aqui se le ingresa el valor al tarifario particular
							forma.setExcepTipoSala(indices[11]+pos, datos[0]);
							//aqui se le pone el valor a tarifario general en -1
							forma.setExcepTipoSala(indices[12]+pos, ConstantesBD.codigoNuncaValido);
						}
					
					
				}
			
		//4)se le introduce el valor de tipo sala al hashmap criterios de busqueda
		forma.setExcepTipoSala(indices[10]+pos, forma.getCriteriosBusqueda(indices[10]));
		//se inicializa el tipo de servicio en -1
		forma.setExcepTipoSala(indices[2]+pos, ConstantesBD.codigoNuncaValido);
		//se inicializa el tipo Qx en -1
		forma.setExcepTipoSala(indices[3]+pos, ConstantesBD.codigoNuncaValido);
		//se inicializa el asocio en -1
		forma.setExcepTipoSala(indices[6]+pos, ConstantesBD.codigoNuncaValido);
		//se inicializa el tipo de liquidacion en -1
		forma.setExcepTipoSala(indices[4]+pos, ConstantesBD.codigoNuncaValido);
		//se inicializa la cantidad en -1
		forma.setExcepTipoSala(indices[5]+pos, "");
		//se inicializa liquidar sobre asocio -1
		forma.setExcepTipoSala(indices[7]+pos, "");
		//se inicializa el usuario que modifica
		forma.setExcepTipoSala(indices[9]+pos,usuario.getLoginUsuario());
		//se inicializa el estaBD_
		forma.setExcepTipoSala(indices[8]+pos,ConstantesBD.acronimoNo);
		//se inicializa la institucion
		forma.setExcepTipoSala(indices[14]+pos, usuario.getCodigoInstitucionInt());
		//se inicializa numRegistros en lo que tenia + 1
		forma.setExcepTipoSala("numRegistros",(pos+1)+"");
		
		logger.info("\n\n el valor del hashmap "+forma.getExcepTipoSala());
		
	UtilidadBD.cerrarConexion(connection);
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(codogoInst),Integer.parseInt(forma.getExcepTipoSala("numRegistros").toString()), response, request, "ingresarExcepcionAsocio.jsp",true);
	}
	


/**
 * Metodo encargado de cargar los selects para mostrar los datos. 
 * @param connection
 * @param forma
 */
public static void cargarValoresSelects (Connection connection, ExcepcionAsocioTipoSalaForm forma)
{
	forma.resetSelect();
	String tiposerv="'Q','R','D'";
	String tiposLiq="'U','P','V'";
	//se cargan los tipos de servicios filtrados por tiposerv
	forma.setTiposServicio(UtilidadesFacturacion.obtenerTiposServicio(connection, tiposerv, ""));
	//se cargan los servicios filtrandolos por tiposerv y que se encuentren activos
	//forma.setServicios(Utilidades.obtenerServicos(connection, tiposerv, ConstantesBD.valorTrueEnString));
	//se cargan los tipod de cirugia
	forma.setTiposCirugia(Utilidades.obtenerTipoCirugia(connection, ""));
	//se cargan los asocios
	forma.setAsocios(Utilidades.obtenerAsocios(connection, "", "", ""));
	//se cargan los valores de tipos de liquidacion
	forma.setTiposLiquidacion(UtilidadesFacturacion.obtenerTiposLiquidacion(connection, tiposLiq));
	
}
	


/**
 * Guarda, Modifica o Elimina un Registro
 * @param con
 * @param forma
 * @param mapping
 * @param usuario
 * @return ActionForward
 * */
public static ActionForward accionGuardarRegistros(Connection connection, ExcepcionAsocioTipoSalaForm forma, ActionMapping mapping,UsuarioBasico usuario)
{
	boolean transacction = UtilidadBD.iniciarTransaccion(connection);
	logger.info("----->INICIANDO TRANSACCION ....");
			
	logger.info("valor del hasmap al entar a accionGuardarRegistros "+forma.getExcepTipoSala());
	logger.info("valor del hasmap eliminados al entar a accionGuardarRegistros "+forma.getExcepTipoSalaEliminados());
	//eliminar
	for(int i=0;i<Integer.parseInt(forma.getExcepTipoSalaEliminados("numRegistros")+"");i++)
	{
		logger.info("\n eliminar");
	//	logger.info("Entro a eliminar BD :::::::::::::contador I...>"+i);
	//	logger.info("Hashmap:::::::::::"+forma.getAsociosXRangoTiempoEliminadoMap());
		if(eliminarBD(connection, forma, i,usuario.getCodigoInstitucionInt()))		
		{				
			
			 transacction=true;						 
		}
	}		
	
	for(int i=0;i<Integer.parseInt(forma.getExcepTipoSala("numRegistros")+"");i++)
	{			logger.info("\n modificar");
		//modificar			
		
		if((forma.getExcepTipoSala(indices[8]+i)+"").trim().equals(ConstantesBD.acronimoSi))
		{
			HashMap tmp1 = new HashMap ();
			HashMap tmp2 = new HashMap ();
			tmp1=Listado.copyOnIndexMap(forma.getExcepTipoSalaClone(), i+"", (String[])forma.getExcepTipoSala("INDICES_MAPA"));
			//logger.info("estoy en modificar, el valor tmp "+tmp1);
			if(existeModificacion(forma,i,tmp1))
			{	
											
			
				logger.info("ENTRO A antes de copyindexmap::::::::.."+i);
				tmp2=Listado.copyOnIndexMap(forma.getExcepTipoSala(), i+"", (String[])forma.getExcepTipoSala("INDICES_MAPA"));
				//logger.info("EL VALOR DE TMP2 ES "+tmp2);
				transacction = modificarBD(connection, tmp2, usuario);
				
			}	
			
		}
		
		//insertar			
		else 
			if((forma.getExcepTipoSala(indices[8]+i)+"").trim().equals(ConstantesBD.acronimoNo))
		{
				logger.info("\n insertarr");
			//logger.info("Entro a insertar consentimiento informado "+forma.getConsentimientoInfMap()); 
			HashMap tmp = new HashMap ();
			//se copian los datos a un hashmap de una sola posicion la "0"
			tmp=Listado.copyOnIndexMap(forma.getExcepTipoSala(), i+"", indices);
			//logger.info("\n salio de copy "+tmp);
			//se inserta en la BD
			transacction = insertarBD(connection, tmp, usuario);
			 
			
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
	
	
	
	return accionBuscar(connection, forma, mapping, usuario); 		
}


/**
 * Este metodo se encarga de insertar en la BD los nuevos datos; 
 * @param connection
 * @param forma
 * @param i
 * @param usuario
 * @return
 */
private static boolean insertarBD (Connection connection, HashMap temp,UsuarioBasico usuario)
{
	logger.info("\n\n::::::::::::ENTRO A INSERTARBD::::::\n\n");
	logger.info("valor de haspmap al entrar"+temp);
	
	//se carga un HashMap datos con los valores a insertar en la BD
	HashMap datos = new HashMap ();
	
	/*------------------------------
	 * Estos valores son requeridos
	 -------------------------------*/
	//se agrega el tipo de sala
	datos.put(indices[10], temp.get(indices[10]+"0"));
	//se agrega el tipo de liquidacion
	datos.put(indices[4], temp.get(indices[4]+"0"));
	//se agrega la cantidad
	datos.put(indices[5], temp.get(indices[5]+"0"));
	//se agrega el asocio
	datos.put(indices[6], temp.get(indices[6]+"0"));
	//se agrega usuario modifia
	datos.put(indices[9], temp.get(indices[9]+"0"));
	//se agrega la institucion
	datos.put(indices[14], temp.get(indices[14]+"0"));
	
	/*-------------------------------------
	 * fin de valores requeridos
	 -------------------------------------*/
	
	/*------------------------------------------
	 * ahora los valores que pueden o no ir
	 --------------------------------------------*/
	//se agrega el codigo del encabezado
	datos.put(indices[1], temp.get(indices[1]+"0"));
	//se agrega el tipo de servicio
	datos.put(indices[2], temp.get(indices[2]+"0"));
	//se agrega el tipo de Qx
	datos.put(indices[3], temp.get(indices[3]+"0"));
	//se agrega liquidar sobre asocio
	datos.put(indices[7], temp.get(indices[7]+"0"));
	//se agrega el tipo equema tarifario particular
	datos.put(indices[11], temp.get(indices[11]+"0"));
	//se agrega el tipo equema tarifario general
	datos.put(indices[12], temp.get(indices[12]+"0"));
	
	
	return insertarGeneral(connection, datos);
}


/**
 * Este metodo se encarga de insertar en la BD los nuevos datos; 
 * @param connection
 * @param forma
 * @param i
 * @param usuario
 * @return
 */
private static boolean modificarBD (Connection connection, HashMap temp,UsuarioBasico usuario)
{
	logger.info("\n\n::::::::::::ENTRO A MODIFICARBD ::::::\n\n");
	logger.info("valor de haspmap al entrar"+temp);
	
	//se carga un HashMap datos con los valores a insertar en la BD
	HashMap datos = new HashMap ();
	
	/*------------------------------
	 * Estos valores son requeridos
	 -------------------------------*/
	
	//se agrega el tipo de liquidacion
	datos.put(indices[4], temp.get(indices[4]+"0"));
	//se agrega la cantidad
	datos.put(indices[5], temp.get(indices[5]+"0"));
	//se agrega el asocio
	datos.put(indices[6], temp.get(indices[6]+"0"));
	//se agrega usuario modifia
	datos.put(indices[9], temp.get(indices[9]+"0"));
	
	
	/*-------------------------------------
	 * fin de valores requeridos
	 -------------------------------------*/
	
	/*------------------------------------------
	 * ahora los valores que pueden o no ir
	 --------------------------------------------*/
	//se agrega el codigo del encabezado
	datos.put(indices[1], temp.get(indices[1]+"0"));
	//se agrega el tipo de servicio
	datos.put(indices[2], temp.get(indices[2]+"0"));
	//se agrega el tipo de Qx
	datos.put(indices[3], temp.get(indices[3]+"0"));
	//se agrega liquidar sobre asocio
	datos.put(indices[7], temp.get(indices[7]+"0"));
	//se agrega liquidar sobre asocio
	datos.put(indices[0], temp.get(indices[0]+"0"));
	
	return modificar1(connection, datos);
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
public static ActionForward accionEliminarCampo(ExcepcionAsocioTipoSalaForm forma, HttpServletRequest request, HttpServletResponse response,int codigoInstitucion, ActionMapping mapping) 
{
	//logger.info("Entra a accionEliminarCampo :::::::::::::::::::::::::::");
	int numRegMapEliminados=Integer.parseInt(forma.getExcepTipoSalaEliminados("numRegistros")+"");
	
	//logger.info("accionEliminarCampo y el numRegMapEliminados"+numRegMapEliminados);
	int ultimaPosMapa=(Integer.parseInt(forma.getExcepTipoSala("numRegistros")+"")-1);
	
	//poner la informacion en el otro mapa.
	//String[] indicesl= (String[])forma.getExcepTipoSala("INDICES_MAPA");		
	
	for(int i=0;i<indices.length;i++)
	{ 
		//solo pasar al mapa los registros que son de BD
		if((forma. getExcepTipoSala(indices[8]+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
		{
			forma.setExcepTipoSalaEliminados(indices[i]+""+numRegMapEliminados, forma.getExcepTipoSala(indices[i]+""+forma.getIndexEliminado()));
		}
	}		
	
	if((forma.getExcepTipoSala(indices[8]+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
	{			
		forma.setExcepTipoSalaEliminados("numRegistros", (numRegMapEliminados+1));
	}
	
	//acomodar los registros del mapa en su nueva posicion
	for(int i=Integer.parseInt(forma.getIndexEliminado()+"");i<ultimaPosMapa;i++)
	{
		for(int j=0;j<indices.length;j++)
		{
			forma.setExcepTipoSala(indices[j]+""+i,forma.getExcepTipoSala(indices[j]+""+(i+1)));
		}
	}
	
	//ahora eliminamos el ultimo registro del mapa.
	for(int j=0;j<indices.length;j++)
	{
		forma.getExcepTipoSala().remove(indices[j]+""+ultimaPosMapa);
	}
	
	//ahora actualizamos el numero de registros en el mapa.
	forma.setExcepTipoSala("numRegistros",ultimaPosMapa);
	return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(codigoInstitucion),Integer.parseInt(forma.getExcepTipoSala("numRegistros").toString()), response, request, "ingresarExcepcionAsocio.jsp",Integer.parseInt(forma.getIndexEliminado().toString())==ultimaPosMapa);
	
	
}


/**
 * Carga los datos a eliminar en la BD; el parametro "i"
 * es el encargado de manejar la posicion dentro del HashMap
 * @param connection
 * @param forma
 * @return
 */
private static boolean eliminarBD (Connection connection, ExcepcionAsocioTipoSalaForm forma,int i,int institucion)
{
	logger.info(":::::::::ENTRO A ELIMINARBD::::::::::::");
	logger.info("\n el valor de i es "+ i);
	logger.info("\n asocios eliminados "+forma.getExcepTipoSalaEliminados());
	logger.info("\n asocios "+forma.getExcepTipoSala());
	logger.info("\n*****************************************************\n\n");
	
	HashMap datos = new HashMap ();
	//codigo encabezado
	datos.put(indices[1], forma.getExcepTipoSalaEliminados(indices[1]+i));
	//codigo detalle
	datos.put(indices[0],  forma.getExcepTipoSalaEliminados(indices[0]+i));
	//codigo institucion
	datos.put(indices[14], institucion);
	
	return eliminarGeneral(connection, datos);

 
}

/**
 * verifica si el registro se ha modificado
 * @param connection
 * @param forma
 * @param mundo
 * @param int pos
 * */
private static boolean existeModificacion(ExcepcionAsocioTipoSalaForm forma, int pos,HashMap temp)
{
	logger.info("\n EXISTEMODIFICACION :::::::::: ");
	logger.info("\n\n ::: original ::::::"+temp);
	logger.info("\n\n ::: modificada ::::::"+forma.getExcepTipoSala());
			
	
	for(int i=0;i<indices.length;i++)
	{		logger.info("::::: "+i+" :::::");
		if(temp.containsKey(indices[i]+"0") && forma.getExcepTipoSala().containsKey(indices[i]+pos))
		{				
			if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getExcepTipoSala(indices[i]+pos)+"").trim())))
			{
						
				return true;
				
			}				
		}
	}	

	
	return false;		
}

/**
 * Método usado para hacer los procesos vinculados con el cambio
 * de página en el listado de excepciones asocio por tipo sala
 * @param con
 * @param forma
 * @param response
 * @return
 */
public  static ActionForward accionRedireccion(Connection connection, ExcepcionAsocioTipoSalaForm forma, HttpServletResponse response) 
{
	try 
	{
		UtilidadBD.cerrarConexion(connection);
		response.sendRedirect(forma.getLinkSiguiente());
		return null;
		
	} 
	catch (Exception e) 
	{
		logger.error("\n Problema cerrando la conexion en accion Redireccion "+e);
	}
	return null;
				
	    
}


/**
 * metodo encargado del ordenamiento de la forma
 * @param forma
 */
public static ActionForward accionOrdenarMapa(Connection connection, ExcepcionAsocioTipoSalaForm forma,ActionMapping mapping)
{
	
	int numReg = Integer.parseInt(forma.getExcepTipoSala("numRegistros")+"");
	forma.setExcepTipoSala(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getExcepTipoSala(), numReg));
	forma.setUltimoPatron(forma.getPatronOrdenar());
	forma.setExcepTipoSala("numRegistros",numReg+"");
	forma.setExcepTipoSala("INDICES_MAPA",indices);	
	
	try 
	{
		UtilidadBD.cerrarConexion(connection);
	}
	catch (Exception e)
	{
		logger.error("\n Problema cerrando la conexion en accion Ordenar Mapa "+e);
	}
	
	
	return mapping.findForward("principal");
}


	/*-----------------------------------------------------
	 * Fin de metodos adicionados.
	 -----------------------------------------------------*/
}
