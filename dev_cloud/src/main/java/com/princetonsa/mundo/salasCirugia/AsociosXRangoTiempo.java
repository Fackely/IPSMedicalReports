package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import com.princetonsa.actionform.salasCirugia.AsociosXRangoTiempoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.salasCirugia.AsociosXRangoTiempoDao;
import com.princetonsa.dto.salasCirugia.DtoAsociosXRangoTiempo;
import com.princetonsa.dto.salasCirugia.DtoDetalleAsociosXRangoTiempo;
import com.princetonsa.mundo.UsuarioBasico;




/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com
 **/
public class AsociosXRangoTiempo
{
	/*-------------------------------------
	 * ATRIBUTO PARA EL MANEJO DEL LOGGER
	 -------------------------------------*/
	
	private static Logger logger = Logger.getLogger(AsociosXRangoTiempo.class);
	
	/*----------------------------------------
	 * FIN ATRIBUTO PARA EL MANEJO DEL LOGGER
	 ----------------------------------------*/
	
	
	
	/**
	 *instancia el dao 
	 */
	public static AsociosXRangoTiempoDao asociosXRangoTiempoDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAsociosXRangoTiempoDao();		
	}	
	
	
	
	
	/**
	 * Metodo encargado de realizar la busqueda de
	 * de los asocios por rango de tiempo
	 * @param connection
	 * @param criterios
	 * @return
	 */
	private static HashMap busqueda (Connection connection, DtoAsociosXRangoTiempo criterios)
	{
		
		return asociosXRangoTiempoDao().consultarAsociosPorRangoFecha(connection, criterios);
	}
	
	
	/**
	 * Metodo encargado de inicializar los valores de
	 * para los criterios de busqueda iniciales 
	 * @param connection
	 * @param codigoInst
	 * @param forma
	 */
	public static void init (Connection connection, int codigoInst,AsociosXRangoTiempoForm forma)
	{
		/////////////////////////////////////////////////////////////////////////////////////////////
		//se modifica la obtencion de los convenios filtrando por la vigencia de los contratos
		//modificado por tarea 2606
		////////////////////////////////////////////////////////////////////////////////////////////
		//se cargan todos los convenios existentes.
		forma.setConvenios(Utilidades.obtenerConvenios(connection, "", "", true, "", true));
		//se cargan todos los valores de esquema tarifario tanto generales como especificos
		//forma.setEsqTarfio(Utilidades.obtenerEsquemasTarifariosInArray(false, codigoInst));
		forma.setEsqTarfio(Utilidades.obtenerEsquemasTarifariosGenPartInArray(connection, codigoInst,"porcentajescx"));
	
	}
	
	
	public static void cargarValoresSelects (Connection connection, AsociosXRangoTiempoForm forma)
	{
		forma.resetSelect();
		String tiposerv="'Q','R','D'";
		//se cargan los tipos de servicios filtrados por tiposerv
		forma.setTiposServicio(UtilidadesFacturacion.obtenerTiposServicio(connection, tiposerv, ""));
		//se cargan los servicios filtrandolos por tiposerv y que se encuentren activos
		//forma.setServicios(Utilidades.obtenerServicos(connection, tiposerv, ConstantesBD.valorTrueEnString));
		//se cargan los tipod de cirugia
		forma.setTiposCirugia(Utilidades.obtenerTipoCirugia(connection, ""));
		//se cargan los tipos de anestecia
		forma.setTiposAnestesia(Utilidades.obtenerTipoAnestecia(connection, ""));
		//se cargan los asocios
		forma.setAsocios(Utilidades.obtenerAsocios(connection, "", "", ""));
		
	}
	
	/**
	 * Metodo encargado de eliminar los datos de la tabla 
	 * asocios_x_rango_tiempo filtrandolos por codigo e institucion.
	 * @param connection
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	private static boolean eliminar (Connection connection,int codigo,int institucion )
	{
		return asociosXRangoTiempoDao().eliminar(connection, codigo, institucion);
		
	}
	
	/**
	 * Metodo que se encarga de verificar si se borra
	 * el encabezado o no en las tablas.
	 * @param connection
	 * @param codcab
	 * @param codigodet
	 * @param institucion
	 * @return
	 */
	public static boolean eliminarGeneral (Connection connection,int codcab, int codigodet, int institucion)
	{
		return asociosXRangoTiempoDao().eliminarGeneral(connection, codcab, codigodet, institucion);
	}
	
	
	/**
	 * metodo encargado de agregar nuevos datos a la forma
	 * @param forma
	 */
	public static void accionNuevo(AsociosXRangoTiempoForm forma,UsuarioBasico usuario,Connection connection )
	{
		logger.info("\n Entre a accionNuevo con el hashmap "+forma.getAsociosXRangoTiempo());
		int pos = Integer.parseInt(forma.getAsociosXRangoTiempo("numRegistros").toString());
		String[] indices = (String[])forma.getAsociosXRangoTiempo("INDICES_MAPA");
		int consecutivo =0;
		
		for (String indice:indices)
		{
			forma.setAsociosXRangoTiempo(indice+pos, "");
		}
		
		if (forma.getCriteriosBusqueda().containsKey(ConstantesIntegridadDominio.acronimoConvenio) && Integer.parseInt(forma.getCriteriosBusqueda(ConstantesIntegridadDominio.acronimoConvenio)+"")>0)
		{	
			
			forma.setAsociosXRangoTiempo("codigoEncabe_"+pos,forma.getFechas("consecutivo_"+forma.getIndexDestino()));
			forma.setAsociosXRangoTiempo("esqTar_"+pos,-1);
			
		}
		else
			if (forma.getCriteriosBusqueda().containsKey(ConstantesIntegridadDominio.acronimoTarifario) && !forma.getCriteriosBusqueda(ConstantesIntegridadDominio.acronimoTarifario).equals("-1"))
			{
				String [] datos ={"-1","-1"};
				try 
				{
					datos = (forma.getCriteriosBusqueda(ConstantesIntegridadDominio.acronimoTarifario)+"").split(ConstantesBD.separadorSplit);
				} 
				catch (Exception e) 
				{
					logger.error("\n problema separando el codigo del esquema tarifario del tipo");
					
				}
				
				//se pregunta si el esquema tarifario ya se encuentra dentro del encabezado.
				consecutivo=consultarSqtarfio(connection, Integer.parseInt(datos[0]), usuario.getCodigoInstitucionInt(),Integer.parseInt(datos[1]));
				
				if (consecutivo>0)
				{
					
					forma.setAsociosXRangoTiempo("codigoEncabe_"+pos,consecutivo);
					forma.setAsociosXRangoTiempo("esqTar_"+pos, -1);
					forma.setAsociosXRangoTiempo("tipoEsq_"+pos,-1);
				}
				else
				{
					
					forma.setAsociosXRangoTiempo("esqTar_"+pos, datos[0]);
					forma.setAsociosXRangoTiempo("tipoEsq_"+pos, datos[1]);
					forma.setAsociosXRangoTiempo("codigoEncabe_"+pos,-1);
				}
				
			}
		HashMap ini = new HashMap ();
		ini.put("numRegistros", 0);
		forma.setAsociosXRangoTiempo("detalle_"+pos,ini);
		
		forma.setAsociosXRangoTiempo("servicio_"+pos, -1);
		forma.setAsociosXRangoTiempo("tipoCirugia_"+pos, -1);
		forma.setAsociosXRangoTiempo("estaBd_"+pos,ConstantesBD.acronimoNo);
		forma.setAsociosXRangoTiempo("institucion_"+pos, usuario.getCodigoInstitucionInt());
		forma.setAsociosXRangoTiempo("numRegistros",(pos+1)+"");
		
		logger.info("\n\n el valor del hashmap "+forma.getAsociosXRangoTiempo());
	
	}
	

	
	/**
	 * Metodo encargado de organizar los datos
	 * del criterio de busqueda  y luego realizar la busqueda
	 * y almacenar el resultado en el HashMap de la forma.  
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @return
	 */
	public static  ActionForward buscar (Connection connection, AsociosXRangoTiempoForm forma, ActionMapping mapping,UsuarioBasico usuario)
	{
		forma.reset();
		int codcab =0;
		DtoAsociosXRangoTiempo  criterios = new DtoAsociosXRangoTiempo();
		
		//logger.info("\n\n el valor de los criterios "+forma.getCriteriosBusqueda());
		
		//se consulta toda l ainformacion d elos registros
//		forma.setAsociosXRangoTiempoAll(busqueda(connection, criterios));
		//logger.info("\n\n:::TODOS LOS REGISTROS (ALL) ==> "+forma.getAsociosXRangoTiempoAll());
		
		//se evalua si la busqueda se va a realizar por convenio o por esquema tarifario
		if (forma.getCriteriosBusqueda().containsKey(ConstantesIntegridadDominio.acronimoConvenio) && UtilidadCadena.noEsVacio(forma.getCriteriosBusqueda(ConstantesIntegridadDominio.acronimoConvenio+"")+"") && Integer.parseInt(forma.getCriteriosBusqueda(ConstantesIntegridadDominio.acronimoConvenio)+"")>0)
		{
			logger.info("\n index ==>   "+forma.getIndexDestino()+"\n");
			logger.info("\n consecutivo ==> "+forma.getFechas("consecutivo_"+forma.getIndexDestino())+"\n");
			logger.info("\n hashmap fechas ==> "+forma.getFechas()+"\n");
			criterios.setCodigoCabeza(Integer.parseInt(forma.getFechas("consecutivo_"+forma.getIndexDestino())+""));
			
			forma.setAsociosXRangoTiempo(busqueda(connection, criterios));
			//se saca una copia de la informacion para luego utilizarla ((HashMap)forma.getAsociosXRangoTiempo().clone())
			forma.setAsociosXRangoTiempoClone(busqueda(connection, criterios));
			
	
			
		}		
		else
			if (forma.getCriteriosBusqueda().containsKey(ConstantesIntegridadDominio.acronimoTarifario) && UtilidadCadena.noEsVacio(forma.getCriteriosBusqueda(ConstantesIntegridadDominio.acronimoTarifario+"")+"") && !forma.getCriteriosBusqueda(ConstantesIntegridadDominio.acronimoTarifario+"").equals("-1") )
				{
					logger.info("entre a esquema tarifario "+forma.getCriteriosBusqueda(ConstantesIntegridadDominio.acronimoTarifario)+"\n\n");
					//se pregunta por el codigo en el encabezado para ese esquema tarifario
					String [] datos ={"-1","-1"};
					try 
					{
						datos = (forma.getCriteriosBusqueda(ConstantesIntegridadDominio.acronimoTarifario)+"").split(ConstantesBD.separadorSplit);
					} 
					catch (Exception e) 
					{
						logger.error("\n problema separando el codigo del esquema tarifario del tipo");
						
					}
				
				logger.info("::::::::::::::1::::::::::.datos 0"+datos[0]+" datos 1"+datos[1]);
					codcab=consultarSqtarfio(connection, Integer.parseInt(datos[0]), usuario.getCodigoInstitucionInt(),Integer.parseInt(datos[1]));
					logger.info("::::::::::::::2::::::::::::::."+codcab);	
					if (codcab>0)
					{
						criterios.setCodigoCabeza(codcab);
//						se almacena la respuesta en el hashmap de la forma.
						forma.setAsociosXRangoTiempo(busqueda(connection, criterios));
						//se saca una copia de la informacion para luego utilizarla ((HashMap)forma.getAsociosXRangoTiempo().clone())
						forma.setAsociosXRangoTiempoClone(busqueda(connection, criterios));

					}
					else
					{
						String [] indices = {"codigo_","codigoEncabe_","tipoServicio_","servicio_","nombreServicio_","tipoCirugia_",
								"tipoAnestesia_","asocio_","tipoTiempoBase_","minRangIni_","minRangFin_","tipoEsq_",
								"minFracAdic_","valorFracAdic_","estaBd_","valorAsocio_","detalle_","esqTar_","institucion_","liquidarpor_"};
						
						forma.setAsociosXRangoTiempo("numRegistros", 0);
						forma.setAsociosXRangoTiempo("INDICES_MAPA", indices);
						forma.setAsociosXRangoTiempoClone("numRegistros", 0);
						forma.setAsociosXRangoTiempoClone("INDICES_MAPA", indices);
					}
						
				
				}
		
		
		//logger.info("\n\n::::::: el valor de la fecha ini"+criterios.getFechaInicial());
		
		//logger.info("\n\n::::::: el valor de la fecha fin"+criterios.getFechaFinal());
		
		//se cargan los select con los datos
		cargarValoresSelects(connection, forma);
		
		
		logger.info("\n\n el resultado de la consulta "+forma.getAsociosXRangoTiempo());
		
		return mapping.findForward("frame");
	}
	
	
	/**
	 * Elimina un Registro del HashMap de Consentimiento Informado, Adicciona el Registro Eliminado en el HashMap de Caonsentimiento Informado Eliminados
	 * @param forma
	 * @param request
	 * @param response
	 * @param mapping
	 * @return ActionForward
	 */
	public static ActionForward accionEliminarCampo(AsociosXRangoTiempoForm forma, HttpServletRequest request, HttpServletResponse response,int codigoInstitucion) 
	{
		//logger.info("Entra a accionEliminarCampo :::::::::::::::::::::::::::");
		int numRegMapEliminados=Integer.parseInt(forma.getAsociosXRangoTiempoEliminadoMap("numRegistros")+"");
		
		//logger.info("accionEliminarCampo y el numRegMapEliminados"+numRegMapEliminados);
		int ultimaPosMapa=(Integer.parseInt(forma.getAsociosXRangoTiempo("numRegistros")+"")-1);
		
		//poner la informacion en el otro mapa.
		String[] indices= (String[])forma.getAsociosXRangoTiempo("INDICES_MAPA");		
		
		for(int i=0;i<indices.length;i++)
		{ 
			//solo pasar al mapa los registros que son de BD
			if((forma. getAsociosXRangoTiempo("estaBd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
			{
				forma.setAsociosXRangoTiempoEliminadoMap(indices[i]+""+numRegMapEliminados, forma.getAsociosXRangoTiempo(indices[i]+""+forma.getIndexEliminado()));
			}
		}		
		
		if((forma.getAsociosXRangoTiempo("estaBd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
		{			
			forma.setAsociosXRangoTiempoEliminadoMap("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=Integer.parseInt(forma.getIndexEliminado().toString());i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				forma.setAsociosXRangoTiempo(indices[j]+""+i,forma.getAsociosXRangoTiempo(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getAsociosXRangoTiempo().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamos el numero de registros en el mapa.
		forma.setAsociosXRangoTiempo("numRegistros",ultimaPosMapa);		
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(codigoInstitucion),Integer.parseInt(forma.getAsociosXRangoTiempo("numRegistros").toString()), response, request, "frameAsociosXRangoTiempo.jsp",Integer.parseInt(forma.getIndexEliminado().toString())==ultimaPosMapa);
	}
	
	
	/**
	 *Metodo en cargado de modificar los Ascoios por rango de tiempo
	 *@param connection
	 *@param asociosXrangoTiempo
	 *@return boolean 
	 */
	private static boolean modificarAsociosRangoTiempo(Connection connection, DtoAsociosXRangoTiempo asociosXrangoTiempo)
	{
		return asociosXRangoTiempoDao().modificarAsociosRangoTiempo(connection, asociosXrangoTiempo);
	}
	
	/**
	 *Metodo en cargado de insertar los Ascoios por rango de tiempo
	 *@param connection
	 *@param asociosXrangoTiempo
	 *@return boolean 
	 */
	private static boolean insertarAsociosRangoTiempo(Connection connection, DtoAsociosXRangoTiempo asociosXrangoTiempo)
	{
		return asociosXRangoTiempoDao().insertarAsociosRangoTiempo(connection, asociosXrangoTiempo);
		
	}
	
	public static ActionForward obtenerFechasAsocios (Connection connection,ActionMapping mapping,AsociosXRangoTiempoForm forma)
	{
		//se formatena los datos de la forma
		forma.reset();
		forma.resetVigencias();
		//se ingresan las vigencias a de cada convenio
		 forma.setFechas(asociosXRangoTiempoDao().obtenerFechasAsocios(connection,Integer.parseInt(forma.getCriteriosBusqueda("CONVEN")+"")));
		
		 //se le saca una copia al resultado de la consulta origignal.
		 Listado.copyMap(forma.getFechas(), forma.getFechasClone(), Integer.parseInt(forma.getFechas("numRegistros")+""), (String []) forma.getFechas("INDICES_MAPA"));
		
		 logger.info("\n el resultado es "+forma.getFechas());
		 
		 UtilidadBD.closeConnection(connection);
		return mapping.findForward("vigencias");
	}
	
	
	/**
	 * Guarda, Modifica o Elimina un Registro
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return ActionForward
	 * */
	public static ActionForward accionGuardarRegistros(Connection connection, AsociosXRangoTiempoForm forma, ActionMapping mapping,UsuarioBasico usuario)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		logger.info("----->INICIANDO TRANSACCION ....");
		String estadoEliminar="ninguno";		
		logger.info("valor del hasmap al entar a accionGuardarRegistros "+forma.getAsociosXRangoTiempo());
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getAsociosXRangoTiempoEliminadoMap("numRegistros")+"");i++)
		{
		//	logger.info("Entro a eliminar BD :::::::::::::contador I...>"+i);
		//	logger.info("Hashmap:::::::::::"+forma.getAsociosXRangoTiempoEliminadoMap());
			if(eliminarBD(connection, forma, i,usuario.getCodigoInstitucionInt()))		
			{				
				 estadoEliminar="operacionTrue";
				 transacction=true;						 
			}
		}		
		
		for(int i=0;i<Integer.parseInt(forma.getAsociosXRangoTiempo("numRegistros")+"");i++)
		{			
			//modificar			
			
			if((forma.getAsociosXRangoTiempo("estaBd_"+i)+"").trim().equals(ConstantesBD.acronimoSi))
			{
				HashMap tmp1 = new HashMap ();
				HashMap tmp2 = new HashMap ();
				tmp1=Listado.copyOnIndexMap(forma.getAsociosXRangoTiempoClone(), i+"", (String[])forma.getAsociosXRangoTiempo("INDICES_MAPA"));
				logger.info("estoy en modificar, el valor tmp "+tmp1);
				if(existeModificacion(forma,i,tmp1))
				{	
												
				
					logger.info("ENTRO A antes de copyindexmap::::::::..");
					tmp2=Listado.copyOnIndexMap(forma.getAsociosXRangoTiempo(), i+"", (String[])forma.getAsociosXRangoTiempo("INDICES_MAPA"));
					logger.info("EL VALOR DE TMP2 ES "+tmp2);
					transacction = modificarBD(connection, tmp2,usuario,tmp1);
					estadoEliminar="operacionTrue";
				}	
				
			}
			
			//insertar			
			else if((forma.getAsociosXRangoTiempo("estaBd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				//logger.info("Entro a insertar consentimiento informado "+forma.getConsentimientoInfMap()); 
				HashMap tmp = new HashMap ();
				//se copian los datos a un hashmap de una sola posicion la "0"
				tmp=Listado.copyOnIndexMap(forma.getAsociosXRangoTiempo(), i+"", (String[])forma.getAsociosXRangoTiempo("INDICES_MAPA"));
				//se inserta en la BD
				transacction = insertarBD(connection, tmp, usuario);
				 
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
		
		
		
		return AsociosXRangoTiempo.buscar(connection, forma, mapping,usuario);		 		
	}
	
	
	

	
	/**
	 * metodo encargado del ordenamiento de la forma
	 * @param forma
	 */
	public static void accionOrdenarMapa(AsociosXRangoTiempoForm forma)
	{
		String[] indices = (String[])forma.getAsociosXRangoTiempo("INDICES_MAPA");
		int numReg = Integer.parseInt(forma.getAsociosXRangoTiempo("numRegistros")+"");
		forma.setAsociosXRangoTiempo(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getAsociosXRangoTiempo(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setAsociosXRangoTiempo("numRegistros",numReg+"");
		forma.setAsociosXRangoTiempo("INDICES_MAPA",indices);		
	}
	
	/**
	 * metodo encargado del ordenamiento de la forma
	 * @param forma
	 */
	public static void accionOrdenarMapaVig(AsociosXRangoTiempoForm forma)
	{
		String[] indices = (String[])forma.getFechas("INDICES_MAPA");
		int numReg = Integer.parseInt(forma.getFechas("numRegistros")+"");
		forma.setFechas(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getFechas(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setFechas("numRegistros",numReg+"");
		forma.setFechas("INDICES_MAPA",indices);		
	}
	
	
	
	
	
	/**
	 * Carga los datos a eliminar en la BD; el parametro "i"
	 * es el encargado de manejar la posicion dentro del HashMap
	 * @param connection
	 * @param forma
	 * @return
	 */
	private static boolean eliminarBD (Connection connection, AsociosXRangoTiempoForm forma,int i,int institucion)
	{
		logger.info(":::::::::ENTRO A ELIMINARBD::::::::::::");
		logger.info("\n el valor de i es "+ i);
		logger.info("\n asocios eliminados "+forma.getAsociosXRangoTiempoEliminadoMap());
		logger.info("\n asocios "+forma.getAsociosXRangoTiempo());
		logger.info("\n*****************************************************\n\n");
		
		boolean result = false;
	//	if (forma.)
		return eliminarGeneral(connection, Integer.parseInt(forma.getAsociosXRangoTiempoEliminadoMap("codigoEncabe_"+i)+""), Integer.parseInt(forma.getAsociosXRangoTiempoEliminadoMap("codigo_"+i)+""), institucion);
	
	 
	}
	
	
	/**
	 * verifica si el registro se ha modificado
	 * @param connection
	 * @param forma
	 * @param mundo
	 * @param int pos
	 * */
	private static boolean existeModificacion(AsociosXRangoTiempoForm forma, int pos,HashMap temp)
	{
		HashMap detalletmp = new HashMap();
		detalletmp=(HashMap)temp.get("detalle_0");
		
		HashMap detalle = new HashMap();
		detalle=(HashMap)forma.getAsociosXRangoTiempo("detalle_"+pos);
		//logger.info("\n\n ::: original ::::::"+temp);
		//logger.info("\n\n ::: modificada ::::::"+temp);
		String[] indices = (String[])forma.getAsociosXRangoTiempo("INDICES_MAPA");		
		
		for(int i=0;i<indices.length;i++)
		{		
			if(temp.containsKey(indices[i]+"0")&&forma.getAsociosXRangoTiempo().containsKey(indices[i]+pos))
			{				
				if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getAsociosXRangoTiempo(indices[i]+pos)).toString().trim())))
				{
							
					return true;
					
				}				
			}
		}	
		//logger.info("estoy evaluando el detalle " +detalletmp);
		if(detalletmp != null && !detalletmp.equals(""))
		{
			for (int j =0; j < Integer.parseInt(detalletmp.get("numRegistros")+"");j++)
				if(!((detalletmp.get("valorCodigo_"+j)+"").trim().equals((detalle.get("valorCodigo_"+j)).toString().trim())))
					return true;
		}
	
		
		return false;		
	}
	
	/**
	 * este metodo se encarga de guardar los cambios en la BD; 
	 * 
	 * @param connection
	 * @param forma
	 * @return
	 */
	private static  boolean modificarBD (Connection connection,HashMap temp,UsuarioBasico usuario, HashMap mapOri )
	{
		logger.info("\n\n::::::::::::ENTRO A MODIFICARBD::::::\n\n");
		logger.info("\nvalor de haspmap al entrar temp"+temp);
		
		logger.info("\nvalor de haspmap al mapOri"+mapOri);
	
		DtoAsociosXRangoTiempo as = new DtoAsociosXRangoTiempo();
		//se le introducen los datos a los dto.
		as.setAsocio(Utilidades.convertirAEntero(temp.get("asocio_0")+""));
		as.setLiquidarPor(temp.get("liquidarpor_0").toString());
		as.setCodigo(Utilidades.convertirAEntero(temp.get("codigo_0")+""));
		as.setCodigoCabeza(Utilidades.convertirAEntero(temp.get("codigoEncabe_0")+""));
		as.setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		as.setHoraModifica(UtilidadFecha.getHoraActual());
		as.setMinFracAdicional(Utilidades.convertirAEntero(temp.get("minFracAdic_0")+""));
		as.setMinutosRangoFinal(Utilidades.convertirAEntero(temp.get("minRangFin_0")+""));
		as.setMinutosRangoInicial(Utilidades.convertirAEntero(temp.get("minRangIni_0")+""));
		as.setServicio(Utilidades.convertirAEntero(temp.get("servicio_0")+""));
		as.setTipoAnestesia(Utilidades.convertirAEntero(temp.get("tipoAnestesia_0")+""));
		as.setTipoCirugia(temp.get("tipoCirugia_0")+"");
		as.setTipoServicio(temp.get("tipoServicio_0")+"");
		as.setTipoTiempoBase(temp.get("tipoTiempoBase_0")+"");
		as.setUsuarioModifica(usuario.getLoginUsuario());
		as.setValorAsocio(temp.get("valorAsocio_0")+"");
		as.setValorFracAdiconal(temp.get("valorFracAdic_0")+"");
		
		//se carga el valor del detalle
		ArrayList<DtoDetalleAsociosXRangoTiempo> detalle = new ArrayList<DtoDetalleAsociosXRangoTiempo>();
		
		
		
		HashMap detalletmp = new HashMap();
		HashMap detalleOri = new HashMap();
		detalletmp=(HashMap)temp.get("detalle_0");
		detalleOri = (HashMap)mapOri.get("detalle_0");
		
		logger.info("\n\nel valor del hashmap detalletmp "+detalletmp);
		
		for (int j=0; j< Integer.parseInt(detalletmp.get("numRegistros")+"");j++)
		{
			DtoDetalleAsociosXRangoTiempo elemento = new DtoDetalleAsociosXRangoTiempo();
			if (detalletmp.get("codAsocio_"+j) != null)
				elemento.setCodigoDetAsocXRangTiem(Integer.parseInt(detalletmp.get("codAsocio_"+j)+""));
			else
				elemento.setCodigoDetAsocXRangTiem(-1);
			if (detalletmp.get("codTarOfi_"+j) != null)
				elemento.setCodigoTarifarioOficial(Integer.parseInt(detalletmp.get("codTarOfi_"+j)+""));
			else
				elemento.setCodigoTarifarioOficial(-1);
			if (!detalletmp.get("valorCodigo_"+j).toString().equals(""))
				elemento.setNumeroCodigoTarifarioOficial(detalletmp.get("valorCodigo_"+j)+"");
			else
				elemento.setNumeroCodigoTarifarioOficial("");
			
			if (detalleOri.containsKey("valorCodigo_"+j) && !detalleOri.get("valorCodigo_"+j).toString().equals(""))
				elemento.setNumeroCodigoTarifarioOficialOld(detalleOri.get("valorCodigo_"+j)+"");
			else
				elemento.setNumeroCodigoTarifarioOficialOld("");
			
			detalle.add(elemento);
		}
		
		as.setDetalleAsociosRangoTiempo(detalle);
		
		return AsociosXRangoTiempo.modificarAsociosRangoTiempo(connection, as);
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
		
		
		DtoAsociosXRangoTiempo as = new DtoAsociosXRangoTiempo();
		//se le introducen los datos a los dto.
		as.setAsocio(Integer.parseInt(temp.get("asocio_0")+""));
		as.setLiquidarPor(temp.get("liquidarpor_0").toString());
		as.setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		as.setHoraModifica(UtilidadFecha.getHoraActual());
		if (!temp.get("minFracAdic_0").equals(""))
			as.setMinFracAdicional(Integer.parseInt(temp.get("minFracAdic_0")+""));
		else
			as.setMinFracAdicional(-1);
		as.setMinutosRangoFinal(Integer.parseInt(temp.get("minRangFin_0")+""));
		as.setMinutosRangoInicial(Integer.parseInt(temp.get("minRangIni_0")+""));
		as.setServicio(Integer.parseInt(temp.get("servicio_0")+""));
		as.setTipoAnestesia(Integer.parseInt(temp.get("tipoAnestesia_0")+""));
		as.setTipoCirugia(temp.get("tipoCirugia_0")+"");
		as.setTipoServicio(temp.get("tipoServicio_0")+"");
		as.setTipoTiempoBase(temp.get("tipoTiempoBase_0")+"");
		as.setUsuarioModifica(usuario.getLoginUsuario());
		as.setValorAsocio(temp.get("valorAsocio_0")+"");
		as.setValorFracAdiconal(temp.get("valorFracAdic_0")+"");
		as.setInstitucion(Integer.parseInt(temp.get("institucion_0")+""));
		
		if (temp.containsKey("tipoEsq_0") && !temp.get("tipoEsq_0").equals("") && !temp.get("tipoEsq_0").equals("-1"))
			as.setTipoEsq(Integer.parseInt(temp.get("tipoEsq_0")+""));
		else
			as.setTipoEsq(-1);
		
		//aqui se pregunta para saber si el esquema tarifario no 
		//esta en el encabezado
		if (temp.containsKey("esqTar_0") && Integer.parseInt(temp.get("esqTar_0")+"")>0)
			as.setEsqTar(Integer.parseInt(temp.get("esqTar_0")+""));
		//aqui se pregunta si el esquema tarifario si esta en 
		//en el encabezado
		if (temp.containsKey("codigoEncabe_0") && Integer.parseInt(temp.get("codigoEncabe_0")+"")>0)
			as.setCodigoCabeza(Integer.parseInt(temp.get("codigoEncabe_0")+""));
		
//		se carga el valor del detalle
		ArrayList<DtoDetalleAsociosXRangoTiempo> detalle = new ArrayList<DtoDetalleAsociosXRangoTiempo>();
		
		
		
		HashMap detalletmp = new HashMap();
		detalletmp=(HashMap)temp.get("detalle_0");
		
	logger.info("\n\nel valor del hashmap detalletmp "+detalletmp);
		
		for (int j=0; j< Integer.parseInt(detalletmp.get("numRegistros")+"");j++)
		{
			DtoDetalleAsociosXRangoTiempo elemento = new DtoDetalleAsociosXRangoTiempo();
			if (detalletmp.get("codAsocio_"+j) != null)
				elemento.setCodigoDetAsocXRangTiem(Integer.parseInt(detalletmp.get("codAsocio_"+j)+""));
			else
				elemento.setCodigoDetAsocXRangTiem(-1);
			if (detalletmp.get("codTarOfi_"+j) != null)
				elemento.setCodigoTarifarioOficial(Integer.parseInt(detalletmp.get("codTarOfi_"+j)+""));
			else
				elemento.setCodigoTarifarioOficial(-1);
			if (!detalletmp.get("valorCodigo_"+j).toString().equals(""))
				elemento.setNumeroCodigoTarifarioOficial(detalletmp.get("valorCodigo_"+j)+"");
			else
				elemento.setNumeroCodigoTarifarioOficial("");
			
			detalle.add(elemento);
		}
		
		as.setDetalleAsociosRangoTiempo(detalle);
		
		
	
		return AsociosXRangoTiempo.insertarAsociosRangoTiempo(connection, as);
	}
	
	
	/**
	 * Verifica cules son los tarifarios oficiales para poderlos pintar en la jsp
	 * @param connection
	 * @param forma
	 */
	public static void cargarTarifariosOficiales (Connection connection,AsociosXRangoTiempoForm forma)
	{
			
		forma.setTarifariosOficiales(UtilidadesFacturacion.cargarTarifariosOficiales(connection));
				
	}
	
	/**
	 * 
	 * @param forma
	 */
	public static void cargarDetalle (Connection connection,AsociosXRangoTiempoForm forma)
	{
		logger.info("\n\n ::::entre a cargar detalle ==> "+forma.getAsociosXRangoTiempo());
		logger.info("\n\n ::::Origen ==> "+forma.getOrigen());
		HashMap ta = new HashMap ();
		HashMap de = new HashMap ();
		if (forma.getAsociosXRangoTiempo().containsKey("detalle_"+forma.getIndex()) )
		{
			
			forma.setDetalleCodigos((HashMap)forma.getAsociosXRangoTiempo("detalle_"+forma.getIndex()));
			if (forma.getDetalleCodigos().isEmpty())
				forma.setDetalleCodigos("numRegistros", 0);
		}
			
		//logger.info("\n::1:::");
		
		de = forma.getDetalleCodigos();
		ta=UtilidadesFacturacion.cargarTarifariosOficiales(connection);
		
		for (int i=0;i<Integer.parseInt(ta.get("numRegistros")+"");i++)
		{
			if (!estaValorEnHashmap((ta.get("codigo_"+i)+""), de))
			{
				//logger.info("entre al if");
				 int numreg=Integer.parseInt(de.get("numRegistros")+"");
				de.put("codTarOfi_"+numreg, ta.get("codigo_"+i));
				de.put("valorCodigo_"+numreg, "");
				de.put("nombreTarfio_"+numreg, ta.get("nombre_"+i) );
				de.put("numRegistros", numreg+1);
			}
			
				
		}
		
				
	}
	
	
public static boolean estaValorEnHashmap (String value,HashMap tmp)
{
	logger.info("entra a preguntar "+value+"\n\n haspmap "+tmp);
	int numReg = Integer.parseInt(tmp.get("numRegistros")+"");
	
	for (int i=0;i<numReg;i++)
	{
		//logger.info("\n\n:::::::::::.PREGUNTA SI SON IGUALES ::::::"+tmp.get("codTarOfi_"+i)+" = "+value);
		if (Integer.parseInt(tmp.get("codTarOfi_"+i)+"")== Integer.parseInt(value))
		{
			//logger.info("\n\n:::::::::::.SI SON IGUALES::::::"+tmp.get("codTarOfi_"+i)+" = "+value);
			return true;
		}
	}
	
	return false;
}
	
	public static void guardarDetalle (AsociosXRangoTiempoForm forma)
	{
		HashMap tmp = new HashMap ();
		tmp.put("numRegistros", 0);
		int index = Integer.parseInt(tmp.get("numRegistros")+"");
		for (int i=0; i<Integer.parseInt(forma.getDetalleCodigos("numRegistros")+"");i++)
		{
				tmp.put("codTarOfi_"+index, forma.getDetalleCodigos("codTarOfi_"+i));
				tmp.put("valorCodigo_"+index, forma.getDetalleCodigos("valorCodigo_"+i));
				tmp.put("nombreTarfio_"+index, forma.getDetalleCodigos("nombreTarfio_"+i));
				tmp.put("codAsocio_"+index, forma.getDetalleCodigos("codAsocio_"+i));
				index++;
		}	
		
		tmp.put("numRegistros", index+"");
		
		forma.setAsociosXRangoTiempo("detalle_"+forma.getIndex(), tmp);
	}
	
	
	
	
	
	/**
	 * Metodo encargado de consultar el codigo del consecutivo
	 * de la cabeza del esquema tarifario.
	 * @param connection
	 * @param esqtarfio
	 * @return
	 */
	public static int consultarSqtarfio (Connection connection, int esqtarfio,int institucion,int tipoEsq)
	{
		return asociosXRangoTiempoDao().consultarSqtarfio(connection, esqtarfio, institucion,tipoEsq);
	}
	
	
//::::::::::::::::::::::::::::::::::METODOS PARA TRABAJO CON EL ENCABEZADO DE CONVENIOS:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::	
	
	
	
	public static ActionForward accionGuardarRegistrosCabeza(Connection connection, AsociosXRangoTiempoForm forma, ActionMapping mapping,UsuarioBasico usuario)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		logger.info("----->INICIANDO TRANSACCION ....");
		String estadoEliminar="ninguno";		
		logger.info("valor del hasmap al entar a accionGuardarRegistrosCabeza "+forma.getFechas());
		logger.info("valor del hasmap al entar a accionGuardarRegistrosCabeza  fechas eliminadas"+forma.getFehcasEliminadoMap());
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getFehcasEliminadoMap("numRegistros")+"");i++)
		{
		//	logger.info("Entro a eliminar BD :::::::::::::contador I...>"+i);
		//	logger.info("Hashmap:::::::::::"+forma.getAsociosXRangoTiempoEliminadoMap());
			if(eliminarVigBD(connection, forma, i,usuario.getCodigoInstitucionInt()))		
			{				
				 estadoEliminar="operacionTrue";
				 transacction=true;						 
			}
		}		
		
		for(int i=0;i<Integer.parseInt(forma.getFechas("numRegistros")+"");i++)
		{			
			//modificar			
			
			if((forma.getFechas("estaBd_"+i)+"").trim().equals(ConstantesBD.acronimoSi))
			{
				HashMap tmp1 = new HashMap ();
				HashMap tmp2 = new HashMap ();
				tmp1=Listado.copyOnIndexMap(forma.getFechasClone(), i+"", (String[])forma.getFechas("INDICES_MAPA"));
				logger.info("estoy en modificar, el valor tmp "+tmp1);
				if(existeModificacionVig(forma,i,tmp1))
				{	
												
				
					logger.info("ENTRO A antes de copyindexmap::::::::..");
					tmp2=Listado.copyOnIndexMap(forma.getFechas(), i+"", (String[])forma.getFechas("INDICES_MAPA"));
					logger.info("EL VALOR DE TMP2 ES "+tmp2);
					transacction = modificarVigBD(connection, tmp2,usuario,tmp1);
					estadoEliminar="operacionTrue";
				}	
				
			}
			
			//insertar			
			else if((forma.getFechas("estaBd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				//logger.info("Entro a insertar consentimiento informado "+forma.getConsentimientoInfMap()); 
				HashMap tmp = new HashMap ();
				//se copian los datos a un hashmap de una sola posicion la "0"
				tmp=Listado.copyOnIndexMap(forma.getFechas(), i+"", (String[])forma.getFechas("INDICES_MAPA"));
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
		
		
		
		return AsociosXRangoTiempo.obtenerFechasAsocios(connection, mapping, forma);
	}
	
	
	
	
	/**
	 *Metodo encargado de eliminar los campos en el encabezado. 
	 * @param forma
	 * @param request
	 * @param response
	 * @param codigoInstitucion
	 * @return
	 */
	public static ActionForward accionEliminarCampoVig(AsociosXRangoTiempoForm forma, HttpServletRequest request, HttpServletResponse response,int codigoInstitucion) 
	{
		//logger.info("Entra a accionEliminarCampo :::::::::::::::::::::::::::");
		int numRegMapEliminados=Integer.parseInt(forma.getFehcasEliminadoMap("numRegistros")+"");
		
		//logger.info("accionEliminarCampo y el numRegMapEliminados"+numRegMapEliminados);
		int ultimaPosMapa=(Integer.parseInt(forma.getFechas("numRegistros")+"")-1);
		
		//poner la informacion en el otro mapa.
		String[] indices= (String[])forma.getFechas("INDICES_MAPA");		
		
		for(int i=0;i<indices.length;i++)
		{ 
			//solo pasar al mapa los registros que son de BD
			if((forma.getFechas("estaBd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
			{
				forma.setFehcasEliminadoMap(indices[i]+""+numRegMapEliminados, forma.getFechas(indices[i]+""+forma.getIndexEliminado()));
			}
		}		
		
		if((forma.getFechas("estaBd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
		{			
			forma.setFehcasEliminadoMap("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=Integer.parseInt(forma.getIndexEliminado().toString());i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				forma.setFechas(indices[j]+""+i,forma.getFechas(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getFechas().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamos el numero de registros en el mapa.
		forma.setFechas("numRegistros",ultimaPosMapa);		
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(codigoInstitucion),Integer.parseInt(forma.getFechas("numRegistros").toString()), response, request, "frameVigenciasConvenio.jsp",Integer.parseInt(forma.getIndexEliminado().toString())==ultimaPosMapa);
	}
	
	
	
	/**
	 * Metodo encargado de verificar si existe algun tipo cambio 
	 * en el encabezado
	 * @param forma
	 * @param pos
	 * @param temp
	 * @return
	 */
	private static boolean existeModificacionVig(AsociosXRangoTiempoForm forma, int pos,HashMap temp)
	{
		
		logger.info("\n\n ::: original ::::::"+temp);
		//logger.info("\n\n ::: modificada ::::::"+temp);
		String[] indices = (String[])forma.getFechas("INDICES_MAPA");		
		
		for(int i=0;i<indices.length;i++)
		{		
			if(temp.containsKey(indices[i]+"0")&&forma.getFechas().containsKey(indices[i]+pos))
			{				
				if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getFechas(indices[i]+pos)).toString().trim())))
				{
							
					return true;
					
				}				
			}
		}	
	
	
		
		return false;		
	}
	
	
	private static boolean eliminarVigBD (Connection connection, AsociosXRangoTiempoForm forma,int i,int institucion)
	{
		logger.info(":::::::::ENTRO A ELIMINARVIGBD::::::::::::");
		logger.info("\n el valor de i es "+ i);
		logger.info("\n vigencias eliminados "+forma.getFehcasEliminadoMap());

		logger.info("\n*****************************************************\n\n");
		return elimiarcabezado(connection, Integer.parseInt(forma.getFehcasEliminadoMap("consecutivo_"+i)+""), Integer.parseInt(forma.getFehcasEliminadoMap("institucion_"+i)+""));		
	}
	
	/**
	 * Metodo encargado de eliminar los datos del encabezado.
	 * @param connection
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static boolean elimiarcabezado (Connection connection, int codigo , int institucion)
	{
		return asociosXRangoTiempoDao().elimiarcabezado(connection, codigo, institucion);
	}
	
	
	private static boolean insertarVigBD (Connection connection, HashMap temp,UsuarioBasico usuario)
	{
		logger.info("\n\n::::::::::::ENTRO A INSERTARVIGBD::::::\n\n");
		logger.info("valor de haspmap al entrar"+temp);
		
		HashMap datos = new HashMap ();
		
		datos.put("fecIniAsoc", temp.get("fecIniAsoc_0"));
		datos.put("fecFinAsoc", temp.get("fecFinAsoc_0"));
		datos.put("institucion", temp.get("institucion_0"));
		datos.put("convenio", temp.get("convenio_0"));
		datos.put("usuarioModifica", temp.get("usuarioModifica_0"));
		
		
		
	
		
	
		return AsociosXRangoTiempo.insertarencazado(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de insertar datos del encabezado,
	 * a la Tabla (cabeza_asoc_x_ran_tiem).
	 * ----------------------------------
	 *             PARAMETROS
	 * ----------------------------------
	 * @connection	Connection
	 * @encabezado	HashMap
	 * ------------------------------------
	 * 		   KEY'S DE ENCABEZADO
	 * -------------------------------------
	 * --convenio --> Opcional
	 * --esqTar --> Opcional
	 * --fecIniAsoc --> Opcional
	 * --fecFinAsoc --> Opcional
	 * --institucion --> Requerido
	 * --usuarioModifica --> Requerido
	 * ------------------------------------
	 * 		RETORNA UN BOOLEANO
	 * ------------------------------------
	 */
	public static boolean insertarencazado (Connection connection, HashMap encabezado)
	{
		return asociosXRangoTiempoDao().insertarencazado(connection, encabezado);
	}
	
	

	/**
	 * metodo encargado de agregar nuevos datos a la forma
	 * @param forma
	 */
	public static void accionNuevaVigencias (AsociosXRangoTiempoForm forma,UsuarioBasico usuario,Connection connection )
	{
		logger.info("\n Entre a accionNuevaVigencias con el hashmap "+forma.getFechas());
		int pos = Integer.parseInt(forma.getFechas("numRegistros").toString());
		String[] indices = (String[])forma.getFechas("INDICES_MAPA");
		
		forma.setFechas("consecutivo_"+pos, -1);
		forma.setFechas("fecIniAsoc_"+pos, "");
		forma.setFechas("fecFinAsoc_"+pos, "");
		forma.setFechas("convenio_"+pos, forma.getCriteriosBusqueda(ConstantesIntegridadDominio.acronimoConvenio));
		forma.setFechas("esqTar_"+pos, -1);
		forma.setFechas("tipoEsq_"+pos, -1);
		forma.setFechas("institucion_"+pos, usuario.getCodigoInstitucionInt());
		forma.setFechas("usuarioModifica_"+pos, usuario.getLoginUsuario());
		forma.setFechas("estaBd_"+pos, ConstantesBD.acronimoNo);
		forma.setFechas("esUsa_"+pos, 0);
		forma.setFechas("numRegistros",(pos+1)+"");
		forma.setFechas("INDICES_MAPA",indices);
		logger.info("\n\n el valor del hashmap "+forma.getFechas());
	
	}
	
	/**
	 * este metodo se encarga de guardar los cambios en la BD; 
	 * 
	 * @param connection
	 * @param forma
	 * @return
	 */
	private static  boolean modificarVigBD (Connection connection,HashMap temp,UsuarioBasico usuario, HashMap mapOri )
	{
		logger.info("\n\n::::::::::::ENTRO A MODIFICARBD::::::\n\n");
		logger.info("\nvalor de haspmap al entrar temp"+temp);
		

		HashMap datos = new HashMap ();
		
		datos.put("consecutivo", temp.get("consecutivo_0"));
		datos.put("fecIniAsoc", temp.get("fecIniAsoc_0"));
		datos.put("fecFinAsoc", temp.get("fecFinAsoc_0"));
		datos.put("institucion", temp.get("institucion_0"));
		datos.put("convenio", temp.get("convenio_0"));
		datos.put("usuarioModifica", temp.get("usuarioModifica_0"));
		
		return AsociosXRangoTiempo.modificarEncabezado(connection, datos);
	}
	
	/**
	 * Metodo encargado de modificar los datos del encabezado.
	 * @param connection
	 * @param encab
	 * @return
	 */
	public static  boolean modificarEncabezado (Connection connection,HashMap encab)
	{
		return asociosXRangoTiempoDao().modificarEncabezado(connection, encab);
	}
	
	public static void resetBusquedaAvanzada (AsociosXRangoTiempoForm forma)
	{
//		inicializando las busuqeda abanzada
		forma.setCriteriosBusqueda("tipoServicio", "");
		forma.setCriteriosBusqueda("tipoCirugia", "");
		forma.setCriteriosBusqueda("tipoAnestesia", "-1");
		forma.setCriteriosBusqueda("asocio", "-1");
	}
	
	/**
	 * Metodo encargado de organizar el dto para enviarselo
	 * a la busqueda
	 * @param connection
	 * @param criterios
	 */
	public static ActionForward busquedaAvanzada (Connection connection, AsociosXRangoTiempoForm forma, UsuarioBasico usuario, ActionMapping  mapping)
	{
		forma.reset();
		
		
		
		

		int codcab =0;
		HashMap criterios = new HashMap ();
		criterios = forma.getCriteriosBusqueda();
		
		DtoAsociosXRangoTiempo  dto = new DtoAsociosXRangoTiempo();
		
		if (UtilidadCadena.noEsVacio(criterios.get("servicio")+"") && !(criterios.get("servicio")+"").equals(ConstantesBD.codigoNuncaValido+""))
			dto.setServicio(Utilidades.convertirAEntero(criterios.get("servicio")+""));
		
		if (criterios.containsKey("tipoServicio") && !criterios.get("tipoServicio").equals("") )
			dto.setTipoServicio(criterios.get("tipoServicio")+"");
			
		if (criterios.containsKey("tipoCirugia") && !criterios.get("tipoCirugia").equals(""))
			dto.setTipoCirugia(criterios.get("tipoCirugia")+"");
			
		if (criterios.containsKey("tipoAnestesia") && !criterios.get("tipoAnestesia").equals("") && Integer.parseInt(criterios.get("tipoAnestesia")+"")>0)
			dto.setTipoAnestesia(Integer.parseInt(criterios.get("tipoAnestesia")+""));
					
		if (criterios.containsKey("asocio") && !criterios.get("asocio").equals("") && Integer.parseInt(criterios.get("asocio")+"")>0)
			dto.setAsocio(Integer.parseInt(criterios.get("asocio")+""));
			
		
		if (forma.getCriteriosBusqueda().containsKey(ConstantesIntegridadDominio.acronimoConvenio) && UtilidadCadena.noEsVacio(forma.getCriteriosBusqueda(ConstantesIntegridadDominio.acronimoConvenio+"")+"") && Integer.parseInt(forma.getCriteriosBusqueda(ConstantesIntegridadDominio.acronimoConvenio)+"")>0)
		{
			
			dto.setCodigoCabeza(Integer.parseInt(forma.getFechas("consecutivo_"+forma.getIndexDestino())+""));
			
			forma.setAsociosXRangoTiempo(busqueda(connection, dto));
			
			
		}
		else
			if (forma.getCriteriosBusqueda().containsKey(ConstantesIntegridadDominio.acronimoTarifario) && UtilidadCadena.noEsVacio(forma.getCriteriosBusqueda(ConstantesIntegridadDominio.acronimoTarifario+"")+"") && !forma.getCriteriosBusqueda(ConstantesIntegridadDominio.acronimoTarifario+"").equals("-1"))
				{
					//logger.info("entre a esquema tarifario "+forma.getCriteriosBusqueda(ConstantesIntegridadDominio.acronimoTarifario)+"\n\n");
					//se pregunta por el codigo en el encabezado para ese esquema tarifario
					String [] datos ={"-1","-1"};
					try 
					{
						datos = (forma.getCriteriosBusqueda(ConstantesIntegridadDominio.acronimoTarifario)+"").split(ConstantesBD.separadorSplit);
					} 
					catch (Exception e) 
					{
						logger.error("\n problema separando el codigo del esquema tarifario del tipo");
						
					}
			
				
					codcab=consultarSqtarfio(connection, Integer.parseInt(datos[0]), usuario.getCodigoInstitucionInt(),Integer.parseInt(datos[1]));
					if (codcab>0)
					{
						dto.setCodigoCabeza(codcab);
//						se almacena la respuesta en el hashmap de la forma.
						forma.setAsociosXRangoTiempo(busqueda(connection, dto));
						
					}
					else
					{
						String [] indices = {"codigo_","codigoEncabe_","tipoServicio_","servicio_","nombreServicio_","tipoCirugia_",
								"tipoAnestesia_","asocio_","tipoTiempoBase_","minRangIni_","minRangFin_","tipoEsq_",
								"minFracAdic_","valorFracAdic_","estaBd_","valorAsocio_","detalle_","esqTar_","institucion_"};
						
						forma.setAsociosXRangoTiempo("numRegistros", 0);
						forma.setAsociosXRangoTiempo("INDICES_MAPA", indices);
						forma.setAsociosXRangoTiempoClone("numRegistros", 0);
						forma.setAsociosXRangoTiempoClone("INDICES_MAPA", indices);
					}
		
		
		
				}
		UtilidadBD.closeConnection(connection);
		return mapping.findForward("busquedaavanzada");
		
	}
	
}

