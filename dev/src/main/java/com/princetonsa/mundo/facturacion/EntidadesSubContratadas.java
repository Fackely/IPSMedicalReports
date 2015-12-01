package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.facturacion.EntidadesSubContratadasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.EntidadesSubContratadasDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseEntidadesSubContratadasDao;
import com.princetonsa.dto.facturacion.DTOEstanciaViaIngCentroCosto;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.IEstanciaViaIngCentroCostoServicio;


/**
 * 
 * @author Jhony Alexander Duque A.
 * 02/01/2008
 *
 */

public class EntidadesSubContratadas 
{
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private static Logger logger = Logger.getLogger(EntidadesSubContratadas.class);
	
	
	public final static String [] indices ={"codigoPk0_0","codigo1_0","institucion2_0","razonSocial3_0","tercero4_0","codigoMinsalud5_0",
			"direccion6_0","telefono7_0","personaContactar8_0","observaciones9_0","estaBd10_0","usuarioModifica11_0","codigoEntidad12",
			"descripcionEntidad13","codigoDet14_","codigoEntSub15_","fechaInicial16_","fechaFinal17_","esqTarServ18_","esqTarInv19_",
			"nombreTercero20_0","estaBd21_","codigo1_","razonSocial3_","nombreTercero20_","codigoPk0_","tercero4_","codigoMinsalud5_",
			"institucion2_","direccion6_","telefono7_","estaBd10_","observaciones9_","personaContactar8_","esUsado34_", 
			"estancia35_0","via_ing36_","centro_costo_asoc37_","codigoCentroAtencionCub_0"};
			
	
	
	
	/**
	 * Instancia DAO
	 * */
	public static EntidadesSubContratadasDao entidadesSubContratadasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEntidadesSubContratadasDao();		
	}	
	
	
	/**
	 * Busqueda del tercero, 
	 * este metodo busca por numero de identificacion
	 * o por la descripcion 
	 */
	@SuppressWarnings("rawtypes")
	public static Collection buscarEntidad(Connection con, String numIdent, String descripcionEntidad)
	{
		return entidadesSubContratadasDao().buscarEntidad(con, numIdent, descripcionEntidad);
	}
	
	
	public static void buscarTercero (Connection connection, EntidadesSubContratadasForm forma)
	{
		forma.setResultados(buscarEntidad(connection, forma.getTerceros(indices[12])+"", forma.getTerceros(indices[13])+""));
	}
	
	/**
	 * Metodo encargado de insertar los datos en 
	 * la tabla entidades_subcontratadas
	 * @param connection
	 * @param datos
	 * ---------------------------------
	 * 	 	KEY'S DEL HASHMAP DATOS
	 * ---------------------------------
	 * -- codigo1_ --> Requerido
	 * -- institucion2_ --> Requerido 
	 * -- razonSocial3_ --> Requerido
	 * -- tercero4_ --> Requerido
	 * -- codigoMinsalud5_ --> Requerido
	 * -- direccion6_ --> Opcional
	 * -- telefono7_ --> Opcional
	 * -- personaContactar8_ --> Opcional
	 * -- observaciones9_ --> Opcional
	 * -- usuarioModifica11_ --> Requerido
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static ResultadoBoolean insertar0 (Connection connection,HashMap datos)
	{
		return entidadesSubContratadasDao().insertar0(connection, datos); 
	}
	
	/**
	 * Metodo encargado de insertar de los datos en 
	 * la tabla det_entidades_subcontratadas
	 * @param connection
	 * @param datos
	 * -----------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * -----------------------------------
	 * -- codigoDet14_ --> Requerido
	 * -- codigoEntSub15_ --> Requerido
	 * -- fechaInicial16_ --> Requerido
	 * -- fechaFinal17_ --> Requerido
	 * -- esqTarServ18_ --> Requerido
	 * -- esqTarInv19_ --> Requerido
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static  boolean insertar1 (Connection connection, HashMap datos)
	{
		return entidadesSubContratadasDao().insertar1(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de eliminar los datos de la tabla
	 * entidades_subcontratadas y sus respectivos detalles.
	 * @param connection
	 * @param criterios
	 * ---------------------------------
	 * KEY'S DEL HASHMAP CRITERIOS
	 * -----------------------------------
	 * -- codigoPk0_
	 * -- institucion2_
	 * -- codigoDet14_
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean eliminarEntidadConDetalle (Connection connection, HashMap criterios)
	{
		return SqlBaseEntidadesSubContratadasDao.eliminarEntidadConDetalle(connection, criterios); 
	}
	
	/**
	 * Metodo encargado de eliminar los datos de la 
	 * tabla det_entidades_subcontratadas	
	 * @param connection
	 * @param criterios
	 * ----------------------------
	 * KEY'S DEL HASHMAP CRITERIOS
	 * ----------------------------
	 *  -- codigoDet14_
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean eliminar1 (Connection connection, HashMap criterios)
	{
		return entidadesSubContratadasDao().eliminar1(connection, criterios);
	}
	
	/**
	 * Metodo encargado de buscar
	 * los datos de la tabla entidades_subcontratadas
	 * @param connection
	 * @param criterios
	 * ------------------------------
	 * 	KEY DEL MAPA CRITERIOS
	 * ------------------------------
	 * -- codigoPk0_
	 * -- institucion2_
	 * -- codigo1_
	 * @return
	 * ----------------------------------------------
	 * EL MAPA QUE RETORNA TIENE LOS SIGUIENTES KEY
	 * ----------------------------------------------
	 * -- codigoPk0_
	 * -- codigo1_
	 * -- institucion2_
	 * -- razonSocial3_
	 * -- tercero4_
	 * -- codigoMinsalud5_
	 * -- direccion6_
	 * -- telefono7_
	 * -- personaContactar8_
	 * -- observaciones9_
	 * -- estaBd10_
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap buscar0 (Connection connection,HashMap criterios)
	{
		return entidadesSubContratadasDao().buscar0(connection, criterios);
	}
	
	/**
	 * Metodo encargado de Buscar los detalles de
	 * vigencias
	 * @param connection
	 * @param criterios
	 * ----------------------------------------
	 * KEY'S DEL HASHMAP CRITERIOS
	 * ----------------------------------------
	 * -- codigoPk0_
	 * -- institucion2_
	 * 
	 * @return
	 * -----------------------------------------
	 * 		KEYS DEL MAPA QUE RETORNA
	 * -----------------------------------------
	 * -- codigoDet14_
	 * -- codigoEntSub15_
	 * -- fechaInicial16_
	 * -- fechaFinal17_
	 * -- esqTarServ18_
	 * -- esqTarInv19_
	 * -- estaBd21_
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public static HashMap buscar1 (Connection connection,HashMap criterios)
	{
		return entidadesSubContratadasDao().buscar1(connection, criterios);
	}
	
	
	
	/**
	 * Metodo encargado de modificar los datos del 
	 * detalle 
	 * @param connection
	 * @param criterios
	 * -------------------------------------------
	 * 		KEY'S DEL HASHMAP CRITERIOS
	 * -------------------------------------------
	 * -- fechaInicial16_
	 * -- fechaFinal17_
	 * -- esqTarServ18_
	 * -- esqTarInv19_
	 * -- usuarioModifica11_0
	 * -- codigoDet14_
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean modificar1 (Connection connection, HashMap criterios)
	{
		return entidadesSubContratadasDao().modificar1(connection, criterios);
	}
	
	
	/**
	 * metodo encargado de modificar los datos 
	 * de la tabla entidades_subcontratadas
	 * @param connection
	 * @param datos
	 * -----------------------------------------
	 * 		  KEY'S DEL HASHMAP DATOS
	 * -----------------------------------------
	 * -- codigo1_ --> Requerido
	 * -- institucion2_ --> Requerido 
	 * -- razonSocial3_ --> Requerido
	 * -- tercero4_ --> Requerido
	 * -- codigoMinsalud5_ --> Requerido
	 * -- direccion6_ --> Opcional
	 * -- telefono7_ --> Opcional
	 * -- personaContactar8_ --> Opcional
	 * -- observaciones9_ --> Opcional
	 * -- usuarioModifica11_ --> Requerido
	 * -- codigoPk0_  --> Requerido
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean modificar0 (Connection connection,HashMap datos)
	{
		return  entidadesSubContratadasDao().modificar0(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de consultar la descripcion
	 * de la entidad subcontratada filtrando por el
	 * ingreso
	 * @param connection
	 * @param ingreso
	 * @return Descripcion entidad subcontratada.
	 */
	public  static String getDescripcionEntidadSubXIngreso (Connection connection, String ingreso)
	{
		return entidadesSubContratadasDao().getDescripcionEntidadSubXIngreso(connection, ingreso);
	}
	
	/**
	 * Metodo encargado de realizar las operaciones en la BD
	 * Insertar, Eliminar y Modificar (detalle de excepciones Qx)
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ActionForward accionGuardarRegistros(EntidadesSubContratadasForm forma, 
			ActionMapping mapping, UsuarioBasico usuario, 
			List<DTOEstanciaViaIngCentroCosto> listaEstanciaViaIngreso)throws Exception{
		
		Connection connection = HibernateUtil.obtenerConexion();
		UtilidadTransaccion.getTransaccion().begin();
		boolean transaction=UtilidadTransaccion.getTransaccion().isActive();
		
		IEstanciaViaIngCentroCostoServicio estanciaServicio= FacturacionServicioFabrica.crearEstanciaViaIngCentroCosto();
		
		
		
		forma.setCuentaCxp(forma.getCuentaCxp());
		forma.setDiasVenFact(forma.getDiasVenFact());
		forma.setActivo(forma.isActivo());
		forma.setUsuarios(forma.getUsuarios());
		forma.setEntidadesSubContratadas("cuentaCxp", forma.getCuentaCxp() );
		forma.setEntidadesSubContratadas("diasVenFact", forma.getDiasVenFact() );
		forma.setEntidadesSubContratadas("activo", forma.isActivo() );
		forma.setEntidadesSubContratadas("usuarios", forma.getUsuarios() );
//		forma.setEntidadesSubContratadas("centroAtencionCub", forma.getCentroAtencionCub() );
			
		
	/*	
		datos.put("activo", temp.get("activo"));
		datos.put("cuentaCxp", temp.get("cuentaCxp"));
	*/	
		logger.info("\n\n\n MAPA USUARIOS DESDE EL ACTION ...."+forma.getUsuariosSelMap());
		
		logger.info("----->INICIANDO TRANSACCION ....");
		logger.info("valor del hasmap al entar a accionGuardarRegistros "+forma.getEntidadesSubContratadas());
		//logger.info("valor del hasmap eliminados al entar a accionGuardarRegistros "+forma.getResultBusquedaEliminado());
		/*
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getResultBusquedaEliminado("numRegistros")+"");i++)
		{
			logger.info("Entro a eliminar BD :::::::::::::contador I...>"+i);
		//	logger.info("Hashmap:::::::::::"+forma.getAsociosXRangoTiempoEliminadoMap());
			if(eliminarBD(connection, forma, i,usuario.getCodigoInstitucionInt()))		
			{				
				//busqueda(connection, forma, usuario);
				 transacction=true;						 
			}
		}		
		*/
	
	
		
		//modificar			
		if((forma.getEntidadesSubContratadas(indices[10])+"").trim().equals(ConstantesBD.acronimoSi))
		{
			//Se quita esta validacion porque la validación está muy obsoleta
			/*if(existeModificacion(forma))
			{*/	
				logger.info("\n::::::::::::::: SI EXISTE MODIFICACION:::::::::\n");						
				//logger.info("ENTRO A antes de copyindexmap::::::::.."+forma.getEntidadesSubContratadas());
				//tmp2=Listado.copyOnIndexMap(forma.getExcepcionesQx(), i+"", indices);
				transaction = modificarBD(connection, forma.getEntidadesSubContratadas(), usuario);
				// temp.get(indices[0])
				
				// Transaccion
				if( forma.getEntidadesSubContratadas().get(indices[0])!=null){
				 
				 accionSettearEntidadSubContratada(listaEstanciaViaIngreso, forma.getEntidadesSubContratadas().get(indices[0]).toString());
				 estanciaServicio.modificarDTOEstanciaViaIngCentroCosto(listaEstanciaViaIngreso);
				}
				
				
				
			//}
			logger.info("valor de transaccion 1: "+transaction);
			if(transaction){
				for(int s=0;s<(Utilidades.convertirAEntero(forma.getUsuariosSelMap("numRegistros")+""));s++)
				{
					if((forma.getUsuariosSelMap("nuevo_"+s)+"").equals(ConstantesBD.acronimoSi))
					{
						HashMap criterios= new HashMap();
						criterios.put("entidadSubcontratada", forma.getEntidadesSubContratadas("codigoPk0_0"));
						
						criterios.put("usuario", forma.getUsuariosSelMap("login_"+s)+"");
						criterios.put("usuarioModifica", usuario.getLoginUsuario()+"");
						logger.info("valor de transaccion 2.1 (insertar usuario): "+transaction);
						transaction= entidadesSubContratadasDao().insertarUsuarioEntidadSub(connection,criterios);
					}
					
					
					if((forma.getUsuariosSelMap("eliminar_"+s)+"").equals(ConstantesBD.acronimoSi))
					{							
						logger.info("valor de transaccion 2.2 (se elimina usuario): "+transaction);
						transaction= entidadesSubContratadasDao().eliminarusu(connection, forma.getUsuariosSelMap("consecutivo_"+s)+"");
					}
				}
			}
			logger.info("valor de transaccion 3: "+transaction);
			if(transaction)
			{
				
				UtilidadTransaccion.getTransaccion().commit();
				connection = HibernateUtil.obtenerConexion();
				//UtilidadBD.finalizarTransaccion(connection);
				busqueda(connection, forma, usuario, mapping,false);
				//UtilidadBD.cerrarConexion(connectionDOS);
				
				logger.info("----->TRANSACCION AL 100% ....");
				forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
				//UtilidadBD.finalizarTransaccion(connection);
			}
			else
			{
				UtilidadTransaccion.getTransaccion().rollback();
				UtilidadBD.abortarTransaccion(connection);
				forma.setMensaje(new ResultadoBoolean(true,"NO SE PUDO MODIFICAR EL REGISTRO."));
				UtilidadBD.abortarTransaccion(connection);
			}
			return cargarEntidad(connection, forma, usuario, mapping);
		}
			
		//insertar
		else if((forma.getEntidadesSubContratadas(indices[10])+"").trim().equals(ConstantesBD.acronimoNo))
		{
			//se inserta en la BD
			ResultadoBoolean result = new ResultadoBoolean(false,""); 
			result = (ResultadoBoolean) insertarBD(connection, forma.getEntidadesSubContratadas(), usuario);
				
			transaction = result.isTrue();
			
			
			/*
			 * Guardar estancias de vias de ingreso por centros de costo
			 * Cambio Agosto06/2010
			 * Autor: Diana Carolina G
			 * Descripcion: Se inserta por cada entidad subcontratada que permita estancia, un detalle de 
			 * Vias ingreso por Centro de Costo.
			 */
			if(transaction){
				
				
				
				
				
				accionSettearEntidadSubContratada(listaEstanciaViaIngreso,result.getDescripcion());
				for(DTOEstanciaViaIngCentroCosto dtoEstanciaViaIngreso :listaEstanciaViaIngreso ){
					
					estanciaServicio.insertarEstancia(dtoEstanciaViaIngreso);
				}		
				
			
			}
			
						
			if(transaction)
			{
				for(int s=0;s<(Utilidades.convertirAEntero(forma.getUsuariosSelMap("numRegistros")+""));s++)
				{
					if((forma.getUsuariosSelMap("eliminar_"+s)+"").equals(ConstantesBD.acronimoNo))
					{
						if((forma.getUsuariosSelMap("nuevo_"+s)+"").equals(ConstantesBD.acronimoSi))
						{
							HashMap criterios= new HashMap();
							criterios.put("entidadSubcontratada", result.getDescripcion());
						
							criterios.put("usuario", forma.getUsuariosSelMap("login_"+s)+"");
							criterios.put("usuarioModifica", usuario.getLoginUsuario()+"");
							
							transaction= entidadesSubContratadasDao().insertarUsuarioEntidadSub(connection,criterios);
						}
					}
				}
			}
			
			logger.info("\n\n\n\n MAPA : "+forma.getEntidadesSubContratadas()+"\n\n\n\n");
			
			if (transaction)
			{
				forma.setOperacionTrue(true);
				
				buscarEntidades(connection, forma, usuario);
				busqueda(connection, forma, usuario, mapping,false);
				logger.info("----->TRANSACCION AL 100% ....");
				//Se resetean todos los elementos
				inicial(forma);
				busqueda(connection, forma, usuario, mapping,true);
				forma.setEntidadesSubContratadas("seccionInsertar", ConstantesBD.acronimoNo);
				forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
				//UtilidadBD.finalizarTransaccion(connection);
				UtilidadTransaccion.getTransaccion().commit();
			} else {
				UtilidadTransaccion.getTransaccion().rollback();
				forma.setMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			}			
			
			return mapping.findForward("principal");
		}
		
		return mapping.findForward("principal");
	}

	
	

	/**
	 * 
	 * @param listaEstanciaViaIngreso
	 * @param result
	 */
	private static void accionSettearEntidadSubContratada(
			List<DTOEstanciaViaIngCentroCosto> listaEstanciaViaIngreso,
			String codigoEntidadSub) {
		for(DTOEstanciaViaIngCentroCosto dtoEstanciaViaIngreso :listaEstanciaViaIngreso ){
			dtoEstanciaViaIngreso.setEntidadSubcontratada(Utilidades.convertirALong(codigoEntidadSub));
		}
	}
	
	
	
	/**
	 * Este metodo se encarga de insertar en la BD los nuevos datos; 
	 * @param connection
	 * @param forma
	 * @param i
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static boolean modificarBD (Connection connection, HashMap temp,UsuarioBasico usuario)
	{
		logger.info("\n\n::::::::::::ENTRO A MODIFICARBD ::::::\n\n");
		logger.info("valor de haspmap al entrar"+temp);
		
		//se carga un HashMap datos con los valores a insertar en la BD
		HashMap datos = new HashMap ();
		
		/*------------------------------
		 * Estos valores son requeridos
		 -------------------------------*/
		//codigo pk
		datos.put(indices[0], temp.get(indices[0]));
			
		//1) codigo
		datos.put(indices[1], temp.get(indices[1]));
		//2)institucion
		datos.put(indices[2], usuario.getCodigoInstitucionInt());
		//3) razon social
		datos.put(indices[3], temp.get(indices[3]));
		//4) tercero
		if (temp.containsKey(indices[12]) && !(temp.get(indices[12])+"").equals(""))
			datos.put(indices[4], temp.get(indices[12]));
		else
			datos.put(indices[4], temp.get(indices[4]));
		
		
		
		//5) codigo minsalud
		datos.put(indices[5], temp.get(indices[5]));
		//6)direccion
		if (temp.containsKey(indices[6]) && !(temp.get(indices[6])+"").equals(""))
			datos.put(indices[6], temp.get(indices[6]));
		//7)telefono
		if (temp.containsKey(indices[7]) && !(temp.get(indices[7])+"").equals(""))
			datos.put(indices[7], temp.get(indices[7]));
		//8)persona a contactar
		if (temp.containsKey(indices[8]) && !(temp.get(indices[8])+"").equals(""))
			datos.put(indices[8], temp.get(indices[8]));
		//9)observaciones
		if (temp.containsKey(indices[9]) && !(temp.get(indices[9])+"").equals(""))
			datos.put(indices[9], temp.get(indices[9]));
		//11)Usuario Modifica
		datos.put(indices[11], usuario.getLoginUsuario());
		//35)permite_estancia_paciente
		if (temp.containsKey(indices[35]) && !(temp.get(indices[35])+"").equals(""))
			datos.put(indices[35], temp.get(indices[35]));
		
		
		for(int i=0;i<Utilidades.convertirAEntero(temp.get("numRegistros")+"");i++)
		{
			datos.put("diasvenfacturas", temp.get("diasvenfacturas_"+i));
			
		}
		datos.put("activo", temp.get("activo"));
		datos.put("cuentaCxp", temp.get("cuentaCxp"));
		datos.put("centroAtencionCub", temp.get("centroAtencionCub"));
		
					
		
		
	
		return modificar0(connection, datos);
	}
	
	
	
	
	/**
	 * verifica si el registro se ha modificado
	 * @param connection
	 * @param forma
	 * @param mundo
	 * @param int pos
	 * */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private static boolean existeModificacion (EntidadesSubContratadasForm forma)
	{
		logger.info("\n EXISTEMODIFICACION :::::::::: ");

		//logger.info("\n\n ::: modificada ::::::"+forma.getEntidadesSubContratadas());
		//logger.info("\n\n ::: original ::::::"+forma.getEntidadesSubcontratadasClone());
		
		String [] indicesMod = {"tercero4_","codigoMinsalud5_","direccion6_","telefono7_",
								"observaciones9_","personaContactar8_","codigo1_","razonSocial3_"};
		
		HashMap ori = new HashMap ();
				ori.putAll(forma.getEntidadesSubcontratadasClone());
				
		HashMap mod = new HashMap ();
				mod.putAll(forma.getEntidadesSubContratadas());
				
		if(ori.containsKey(indicesMod[1]+"0") && mod.containsKey("codigoEntidad12"))
		{				
			if(!((ori.get(indicesMod[1]+"0")+"").trim().equals((mod.get("codigoEntidad12")+"").trim())))
			{
						
				return true;
				
			}				
		}
		
	logger.info("\n\n\nori : "+ori);
	logger.info("\n\n\nmod : "+mod);
		
		for(int i=0;i<indicesMod.length;i++)
		{		//logger.info("::::: "+i+" :::::");
			if(ori.containsKey(indicesMod[i]+"0") && mod.containsKey(indicesMod[i]+"0"))
			{				
				if(!((ori.get(indicesMod[i]+"0")+"").trim().equals((mod.get(indicesMod[i]+"0")+"").trim())))
				{
							
					return true;
					
				}				
			}
		}	

		
		return false;		
	}
	
	
	
	public static ActionForward accionEliminar(Connection connection, EntidadesSubContratadasForm forma, ActionMapping mapping,UsuarioBasico usuario)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		
		logger.info("----->INICIANDO TRANSACCION ....");
		//logger.info("valor del hasmap al entar a accionGuardarRegistros "+forma.getEntidadesSubContratadas());
		logger.info("valor del hasmap eliminados al entar a accionGuardarRegistros "+forma.getResultBusquedaEliminado());
	
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getResultBusquedaEliminado("numRegistros")+"");i++)
		{
			//logger.info("Entro a eliminar BD :::::::::::::contador I...>"+i);
		//	logger.info("Hashmap:::::::::::"+forma.getAsociosXRangoTiempoEliminadoMap());
			if(eliminarBD(connection, forma, i,usuario.getCodigoInstitucionInt()))		
			{				
				//busqueda(connection, forma, usuario);
				 transacction=true;						 
			}
		}		
		
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
			logger.info("----->TRANSACCION AL 100% ....");
			forma.resetBusq();
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}
		
		
		
		return mapping.findForward("principal");
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * Carga los datos a eliminar en la BD; el parametro "i"
	 * es el encargado de manejar la posicion dentro del HashMap
	 * @param connection
	 * @param forma
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static boolean eliminarBD (Connection connection, EntidadesSubContratadasForm forma,int i,int institucion)
	{
		logger.info(":::::::::ENTRO A ELIMINARBD::::::::::::");
		//logger.info("\n el valor de i es "+ i);
		//logger.info("\n entidades eliminadas "+forma.getResultBusquedaEliminado());
		//logger.info("\n*****************************************************\n\n");
		
		HashMap datos = new HashMap ();
		//institucion
		datos.put(indices[2], institucion);
		//codigo institucion
		datos.put(indices[15], forma.getResultBusquedaEliminado(indices[25]+i));
		
			return eliminarEntidadConDetalle(connection, datos);

	 
	}
	
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void buscarEntidades (Connection connection, EntidadesSubContratadasForm forma, UsuarioBasico usuario)
	{
		HashMap datos = new HashMap ();
		datos.put(indices[1], forma.getEntidadesSubContratadas(indices[1]));
		datos.put(indices[2], usuario.getCodigoInstitucionInt());
		forma.setEntidadesSubContratadas(buscar0(connection, datos));
		forma.setEntidadesSubContratadas("seccionInsertar", "S");
	}
	
	/**
	 * Metodo encargado de cargar los
	 * datos de una entidad  en la vista. 
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ActionForward cargarEntidad (Connection connection, EntidadesSubContratadasForm forma, UsuarioBasico usuario, ActionMapping mapping)
	{
		try{
			UtilidadTransaccion.getTransaccion().begin();
			HashMap datos = new HashMap ();
			datos.put(indices[0], forma.getIndexDestino());
			datos.put(indices[2], usuario.getCodigoInstitucionInt());
			forma.setUsuarios(ConstantesBD.codigoNuncaValido+"");
			forma.setEntidadesSubContratadas(buscar0(connection, datos));
			forma.setEntidadesSubContratadas("seccionInsertar", "S");
			
				
				forma.getEntidadesSubContratadas();
	//Se elimina este campo por Incidencia Mantis 1221			
	//			String codigoCentroAtencion = forma.getEntidadesSubContratadas().get(indices[38]).toString();
	//			if(!UtilidadTexto.isEmpty(codigoCentroAtencion)){
	//				forma.setCentroAtencionCub(codigoCentroAtencion);
	//			}
				
				
				
			
			//logger.info("MAPA ENTIDADES SU"+forma.getEntidadesSubContratadas());
			/*forma.setEntidadesSubContratadas("cuentaCxp", forma.getCuentaCxp() );
			forma.setEntidadesSubContratadas("diasVenFact", forma.getDiasVenFact());
			forma.setEntidadesSubContratadas("diasVenFact", forma.getDiasVenFact() );
			forma.setEntidadesSubContratadas("activo", forma.isActivo() );
			*/datos.put("cuentaCxp", forma.getCuentaCxp());
			datos.put("diasVenFact", forma.getDiasVenFact());
			datos.put("activo", forma.isActivo());
			datos.put("usuarios", forma.getUsuarios());
			forma.setActivo(UtilidadTexto.getBoolean(forma.getEntidadesSubContratadas("activo_0")+""));
			//logger.info("\n\n\n\n\n\n\nMAPA DATOS CARGAR :"+datos);
			//logger.info("DIAS : "+forma.getDiasVenFact());
			//logger.info("\n\n\n\n\n\n\nMAPA Entidades CARGAR :"+forma.getEntidadesSubContratadas());*/
			//se saca una copia de los datos
			//logger.info("\n ************************************");
			//logger.info("\n entidades subcontratadas "+forma.getEntidadesSubContratadas());
			forma.setEntidadesSubcontratadasClone(Listado.copyMap(forma.getEntidadesSubContratadas(), forma.getEntidadesSubcontratadasClone(), 1, indices));
			int consecutivo=0;
			for(int i=0; i<Utilidades.convertirAEntero(forma.getEntidadesSubContratadas("numRegistros")+""); i++)
			{
				 consecutivo = Utilidades.convertirAEntero(forma.getEntidadesSubContratadas("codigoPk0_"+i)+"");
			}
			
			/*
			 * Consultar Estancias
			 *  
			 */
			
			if(consecutivo>0){
			
				/*
				 * Cargar las la lista de Estancia por  Entidad SubContrada 
				 */
				//UtilidadTransaccion.getTransaccion().begin();
				IEstanciaViaIngCentroCostoServicio servicioEstancia=FacturacionServicioFabrica.crearEstanciaViaIngCentroCosto();
				List<DTOEstanciaViaIngCentroCosto> lista= servicioEstancia.listarEstanciasxEntidadesSubContratadas(Long.parseLong(String.valueOf(consecutivo)));
				forma.setListaEstanciaviaIngCentroCosto(lista);
				//UtilidadTransaccion.getTransaccion().commit();
			}
			
			
			forma.setUsuariosSelMap(usuariosEntidadSub(connection,consecutivo));
			logger.info("	ESTOS SON LOS USUARIOS");
			Utilidades.imprimirMapa(forma.getUsuariosSelMap());
			Utilidades.imprimirMapa(forma.getEntidadesSubContratadas());
	
			for(int i=0;i<(Utilidades.convertirAEntero(forma.getUsuariosSelMap("numRegistros")+""));i++)
			{
				forma.setUsuariosSelMap("eliminar_"+i, ConstantesBD.acronimoNo);
				forma.setUsuariosSelMap("nuevo_"+i, ConstantesBD.acronimoNo);
			}
			//logger.info("\n ************************************");
			//logger.info("\n entidades subcontratadasclone "+forma.getEntidadesSubcontratadasClone());
			UtilidadTransaccion.getTransaccion().commit();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return mapping.findForward("principal");
	}
	
	
	
	




		
		//return entidadesSubContratadasDao().usuariosEntidadSub(connection, entidadesSubContratadas);



	private static HashMap<String, Object> usuariosEntidadSub(
			Connection connection, int consecutivo) {
		
		return entidadesSubContratadasDao().usuariosEntidadSub(connection, consecutivo);
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ActionForward busqueda (Connection connection, EntidadesSubContratadasForm forma, UsuarioBasico usuario,ActionMapping mapping,boolean resetear)
	{
		if(resetear)
		{
			forma.resetPager();
			forma.resetBusq();
		}
		
		HashMap datos = new HashMap ();
		
		//institucion
		datos.put(indices[2], usuario.getCodigoInstitucionInt());
		
		//codigo digitado
		if (forma.getCriteriosBusqueda().containsKey(indices[1]) && !(forma.getCriteriosBusqueda(indices[1])+"").equals(""))
			datos.put(indices[1], forma.getCriteriosBusqueda(indices[1]));
		
		//razon social 
		if (forma.getCriteriosBusqueda().containsKey(indices[3]) && !(forma.getCriteriosBusqueda(indices[3])+"").equals(""))
			datos.put(indices[3], forma.getCriteriosBusqueda(indices[3]));
		
		//numero id.
		if (forma.getCriteriosBusqueda().containsKey(indices[4]) && !(forma.getCriteriosBusqueda(indices[4])+"").equals(""))
			datos.put(indices[4], forma.getCriteriosBusqueda(indices[4]));
		
		forma.setResultBusqueda(buscar0(connection, datos));
		
		if (Integer.parseInt(forma.getResultBusqueda("numRegistros")+"")>0)
			forma.setEntidadesSubContratadas("seccionInsertar", "N");
		
		for (int i=0;i<Integer.parseInt(forma.getResultBusqueda("numRegistros")+"");i++)
		{
			forma.setResultBusqueda(indices[34]+i, Utilidades.esEntidadSubcontratadaUsado(forma.getResultBusqueda(indices[25]+i)+"", usuario.getCodigoInstitucion()));
		}
		
					
		
		
		forma.setCriteriosBusqueda("seccionBusquedaAvanzada", ConstantesBD.acronimoSi);
		forma.setEntidadesSubContratadas("seccionInsertar", ConstantesBD.acronimoSi);
		//forma.setEntidadesSubContratadas("cuentaCxp", forma.getCuentaCxp() );
		//Se resetea criterios de busqueda
		forma.setCriteriosBusqueda(indices[1], "");
		forma.setCriteriosBusqueda(indices[3], "");
		forma.setCriteriosBusqueda(indices[4], "");

//Se elimina este campo por Incidencia Mantis 1221					
//		forma.setMapCentrosAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
//		logger.info("mapa centros");
//		Utilidades.imprimirMapa(forma.getMapCentrosAtencion());
		forma.setUsuariosMap(Utilidades.obtenerUsuarios(connection, usuario.getCodigoInstitucionInt(),true));
		
		Object vias = new Object();
		HashMap viasIng = new HashMap();
		vias= Utilidades.obtenerViasIngreso(false);
		viasIng = (HashMap) vias;
		//forma.setMapViasIngreso(viasIng);
		forma.setMapViasIngreso(viasIng);
		logger.info("mapa vias");
		Utilidades.imprimirMapa(forma.getMapViasIngreso());
		
		if(resetear)
		{
			UtilidadBD.closeConnection(connection);
		}

		return mapping.findForward("principal");
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void busqueda (Connection connection, EntidadesSubContratadasForm forma, UsuarioBasico usuario)
	{
		forma.resetPager();
		
		HashMap datos = new HashMap ();
		
		//institucion
		datos.put(indices[2], usuario.getCodigoInstitucionInt());
		
		//codigo digitado
		if (forma.getCriteriosBusqueda().containsKey(indices[1]) && !(forma.getCriteriosBusqueda(indices[1])+"").equals(""))
		datos.put(indices[1], forma.getCriteriosBusqueda(indices[1]));
		
		//razon social 
		if (forma.getCriteriosBusqueda().containsKey(indices[3]) && !(forma.getCriteriosBusqueda(indices[3])+"").equals(""))
			datos.put(indices[3], forma.getCriteriosBusqueda(indices[3]));
		
		//numero id.
		if (forma.getCriteriosBusqueda().containsKey(indices[12]) && !(forma.getCriteriosBusqueda(indices[12])+"").equals(""))
			datos.put(indices[12], forma.getCriteriosBusqueda(indices[12]));
		
		
		forma.setResultBusqueda(buscar0(connection, datos));
		
		
		
		if (Integer.parseInt(forma.getResultBusqueda("numRegistros")+"")>0)
			forma.setEntidadesSubContratadas("seccionInsertar", "N");
		
				
		
	}
	
	
	/**
 	 *Metodo encargado de insertar los datos a la BD
	 * @param connection
	 * @param temp
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static ResultadoBoolean insertarBD (Connection connection, HashMap temp,UsuarioBasico usuario)
	{
		logger.info("MAPA TEMPORAL INSERTAR BD ---> "+temp);
		HashMap datos = new HashMap ();
		  //EntidadesSubContratadasForm
		//1) codigo
		datos.put(indices[1], temp.get(indices[1]));
		//2)institucion
		datos.put(indices[2], usuario.getCodigoInstitucionInt());
		//3) razon social
		datos.put(indices[3], temp.get(indices[3]));
		//4) tercero
		datos.put(indices[4], temp.get(indices[4]));
		//5) codigo minsalud
		datos.put(indices[5], temp.get(indices[5]));
		//6)direccion
		if (temp.containsKey(indices[6]) && !(temp.get(indices[6])+"").equals(""))
			datos.put(indices[6], temp.get(indices[6]));
		//7)telefono
		if (temp.containsKey(indices[7]) && !(temp.get(indices[7])+"").equals(""))
			datos.put(indices[7], temp.get(indices[7]));
		//8)persona a contactar
		if (temp.containsKey(indices[8]) && !(temp.get(indices[8])+"").equals(""))
			datos.put(indices[8], temp.get(indices[8]));
		//9)observaciones
		if (temp.containsKey(indices[9]) && !(temp.get(indices[9])+"").equals(""))
			datos.put(indices[9], temp.get(indices[9]));
		//11)Usuario Modifica
		datos.put(indices[11], usuario.getLoginUsuario());
		
		datos.put("cuentaCxp", temp.get("cuentaCxp"));
		datos.put("diasVenFact", temp.get("diasVenFact"));
		datos.put("activo", temp.get("activo"));
		datos.put("usuarios", temp.get("usuarios"));
		datos.put("centroAtencionCub", temp.get("centroAtencionCub"));
		
		//35)permite estancia pacientes
		if (temp.containsKey(indices[35]) && !(temp.get(indices[35])+"").equals(""))
			datos.put(indices[35], temp.get(indices[35]));

		return insertar0(connection, datos);
		
		
	}
	
	
	public static void inicial ( EntidadesSubContratadasForm forma)
	{		
		forma.reset();
		forma.resetBusqueda();
		
	}
	
	
	/**
	 * Metodo encargado de  los valores de los selects
	 * de esquemas tarifarios inventarios y servicios
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void initSelects (Connection connection, EntidadesSubContratadasForm forma, UsuarioBasico usuario)
	{
		// carga los esquemas tarifarios de inventarios
		forma.setEsqTarInv(Utilidades.obtenerEsquemasTarifariosInArray(true, usuario.getCodigoInstitucionInt(),ConstantesBD.codigoNuncaValido));
		
		//carga los esquemas tarifarios de servicios
		forma.setEsqTarServ(Utilidades.obtenerEsquemasTarifariosInArray(false, usuario.getCodigoInstitucionInt(),ConstantesBD.codigoNuncaValido));
	}
	
	
	/**
	 * Metodo encargado de inicializar los valores del detalle
	 * de vigencias
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ActionForward initDetalle (Connection connection, EntidadesSubContratadasForm forma, UsuarioBasico usuario, ActionMapping mapping,HttpServletRequest request,HttpServletResponse response)
	{
		// se formatea el valor del detalle
		forma.resetDetalle();
		forma.resetPager();
		//se cargan los selects
		initSelects(connection, forma, usuario);
		HashMap criterios = new  HashMap ();
		
		criterios.put(indices[2], usuario.getCodigoInstitucionInt());
		criterios.put(indices[1], forma.getIndexDestino());
		forma.setDetalle(buscar1(connection, criterios));
		forma.setDetalleClone(Listado.copyMap(forma.getDetalle(), forma.getDetalleClone(), Integer.parseInt(forma.getDetalle("numRegistros")+""), indices));
		
		//return mapping.findForward("detalleVig");
		
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getDetalle("numRegistros").toString()), response, request, "detalleVigencias.jsp",true);
	}
	
	
	public static ActionForward nuevaVig (Connection connection, EntidadesSubContratadasForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletResponse response, HttpServletRequest request)
	{
		int pos = Integer.parseInt(forma.getDetalle("numRegistros")+"");
		//codigo entidad subcontratada
		forma.setDetalle(indices[15]+pos, forma.getEntidadesSubContratadas(indices[0]));
		//fecha inicial
		forma.setDetalle(indices[16]+pos, "");
		//fecha final
		forma.setDetalle(indices[17]+pos, "");
		//esquema tarifario servicos
		forma.setDetalle(indices[18]+pos, "-1");
		//esquema tarifario inventario
		forma.setDetalle(indices[19]+pos, "-1");
		//institucion
		forma.setDetalle(indices[2]+pos, usuario.getCodigoInstitucionInt());
		//usuario modifica 
		forma.setDetalle(indices[11]+pos,  usuario.getLoginUsuario());
		//esta BD1
		forma.setDetalle(indices[21]+pos, ConstantesBD.acronimoNo);
		//se actualiza numRegistros
		forma.setDetalle("numRegistros", (pos+1)+"");
		//se agrega el indice del mapa
		forma.setDetalle("INDICES_MAPA", indices);
		
		try
		{
			//se cierra la conexion
			UtilidadBD.cerrarConexion(connection);
		} 
		catch (SQLException e)
		{
		 logger.info("problema cerrando la conexion en nuevaVig "+e);
		}
			
			//se redirecciona  a la pag
			return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getDetalle("numRegistros").toString()), response, request, "detalleVigencias.jsp",true);		
		
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
	@SuppressWarnings({ "unused", "rawtypes" })
	public static ActionForward accionGuardarRegistrosVig(Connection connection, EntidadesSubContratadasForm forma, ActionMapping mapping,UsuarioBasico usuario,HttpServletRequest request,HttpServletResponse response)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		logger.info("----->INICIANDO TRANSACCION ....");
		String estadoEliminar="ninguno";		
		logger.info("valor del hasmap al entar a accionGuardarRegistrosVig "+forma.getDetalle());
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getDetalleEliminado("numRegistros")+"");i++)
		{
			logger.info("Entro a eliminar BD :::::::::::::contador I...>"+i);
			logger.info("Hashmap:::::::::::"+forma.getDetalleEliminado());
			if(eliminarVigBD(connection, forma, i,usuario.getCodigoInstitucionInt()))		
			{				
				 estadoEliminar="operacionTrue";
				 transacction=true;						 
			}
		}		
		
		for(int i=0;i<Integer.parseInt(forma.getDetalle("numRegistros")+"");i++)
		{			
			//modificar			
			
			if((forma.getDetalle(indices[21]+i)+"").trim().equals(ConstantesBD.acronimoSi))
			{
				HashMap tmp1 = new HashMap ();
				HashMap tmp2 = new HashMap ();
				tmp1=Listado.copyOnIndexMap(forma.getDetalleClone(), i+"", indices);
				logger.info("estoy en modificar, el valor tmp "+tmp1);
				if(existeModificacionVig(forma,i,tmp1))
				{	
												
				
					logger.info("ENTRO A antes de copyindexmap::::::::..");
					tmp2=Listado.copyOnIndexMap(forma.getDetalle(), i+"", indices);
					logger.info("EL VALOR DE TMP2 ES "+tmp2);
					transacction = modificarVigBD(connection, tmp2, usuario);
					
				}	
			
			}
			
			//insertar			
			else if((forma.getDetalle(indices[21]+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				//logger.info("Entro a insertar consentimiento informado "+forma.getConsentimientoInfMap()); 
				HashMap tmp = new HashMap ();
				//se copian los datos a un hashmap de una sola posicion la "0"
				tmp=Listado.copyOnIndexMap(forma.getDetalle(), i+"",indices);
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
		
		
		
		return initDetalle(connection, forma, usuario, mapping, request, response);
	}
	
	
	/**
	 * verifica si el registro se ha modificado
	 * @param connection
	 * @param forma
	 * @param mundo
	 * @param int pos
	 * */
	@SuppressWarnings("rawtypes")
	private static boolean existeModificacionVig (EntidadesSubContratadasForm forma, int pos,HashMap temp)
	{
		logger.info("\n EXISTEMODIFICACIONVIG :::::::::: ");
		logger.info("\n\n ::: original ::::::"+temp);
		logger.info("\n\n ::: modificada ::::::"+forma.getDetalle());
				
		
		for(int i=0;i<indices.length;i++)
		{		//logger.info("::::: "+i+" :::::");
			if(temp.containsKey(indices[i]+"0") && forma.getDetalle().containsKey(indices[i]+pos))
			{				
				if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getDetalle(indices[i]+pos)+"").trim())))
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
		datos.put(indices[16], temp.get(indices[16]+"0"));
		//se agrega fecha final
		datos.put(indices[17], temp.get(indices[17]+"0"));
		//se agrega el usuario modifica
		datos.put(indices[11], usuario.getLoginUsuario());
		//esquema tarifario servicio
		datos.put(indices[18], temp.get(indices[18]+"0"));
		//esquema tarifario inventario
		datos.put(indices[19], temp.get(indices[19]+"0"));
		//usuario modifica
		datos.put(indices[11], usuario.getLoginUsuario());
		
		//codigo del detalle
		datos.put(indices[14], temp.get(indices[14]+"0"));
		/*-------------------------------------
		 * fin de valores requeridos
		 -------------------------------------*/
		
		
		return modificar1(connection, datos);
	}
	
	
	
	/**
	 * Carga los datos a eliminar en la BD; el parametro "i"
	 * es el encargado de manejar la posicion dentro del HashMap
	 * @param connection
	 * @param forma
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static boolean eliminarVigBD (Connection connection, EntidadesSubContratadasForm forma,int i,int institucion)
	{
		logger.info(":::::::::ENTRO A ELIMINARVIGBD::::::::::::");
		logger.info("\n el valor de i es "+ i);
		logger.info("\n asocios eliminados "+forma.getDetalleEliminado());
		logger.info("\n*****************************************************\n\n");
		
		HashMap datos = new HashMap ();
		//codigo del detalle
		datos.put(indices[14], forma.getDetalleEliminado(indices[14]+i));
	
			return eliminar1(connection, datos);

	 
	}
	
	
	
	
	
	
	/**
	 * Metodo encargado de organizar los datos
	 * para que sean ingresados a la BD 
	 * @param connection
	 * @param temp
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static boolean insertarVigBD (Connection connection, HashMap temp,UsuarioBasico usuario)
	{
		logger.info("\n\n::::::::::::ENTRO A INSERTARVIGBD::::::\n\n");
		logger.info("valor de haspmap al entrar"+temp);
		
		HashMap datos = new HashMap ();
		
		
		//1) fecha inicial
		datos.put(indices[16], temp.get(indices[16]+"0"));
		//2) fecha final
		datos.put(indices[17] , temp.get(indices[17]+"0"));
		
		//3) esquema tarifario servicio
		datos.put(indices[18], temp.get(indices[18]+"0"));
		//4) esquema tarifario inventario
		datos.put(indices[19], temp.get(indices[19]+"0"));

		//5) institucion
		datos.put(indices[2], temp.get(indices[2]+"0"));
		//6) usuario modifica
		datos.put(indices[11], temp.get(indices[11]+"0"));
		
		//7) codigo entudades subcontratadas
		datos.put(indices[15], temp.get(indices[15]+"0"));
		
		return insertar1(connection, datos);
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
	public static ActionForward accionEliminarCampoVig(EntidadesSubContratadasForm forma, HttpServletRequest request, HttpServletResponse response,int codigoInstitucion, ActionMapping mapping) 
	{
		
		logger.info("\n\n\n\nPRO ACA ENTRARA?\n\n\n\n\n");
		//logger.info("Entra a accionEliminarCampo :::::::::::::::::::::::::::"+forma.getVigenciasEliminados());
		int numRegMapEliminados=Integer.parseInt(forma.getDetalleEliminado("numRegistros")+"");
		//logger.info(":::::::0:::::::::");
		//logger.info("accionEliminarCampo y el numRegMapEliminados"+numRegMapEliminados);
		int ultimaPosMapa=(Integer.parseInt(forma.getDetalle("numRegistros")+"")-1);
		//logger.info(":::::::1:::::::::");
		//poner la informacion en el otro mapa.
		//String[] indicesl= (String[])forma.getExcepTipoSala("INDICES_MAPA");		
		
		for(int i=0;i<indices.length;i++)
		{ 
			//logger.info(":::::::2:::::::::");
			//solo pasar al mapa los registros que son de BD
			if((forma.getDetalle(indices[21]+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
			{
				logger.info(":::::::3:::::::::");
				forma.setDetalleEliminado(indices[i]+""+numRegMapEliminados, forma.getDetalle(indices[i]+""+forma.getIndexEliminado()));
			}
		}		
		//logger.info(":::::::4:::::::::");
		if((forma.getDetalle(indices[21]+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
		{			
			logger.info(":::::::4:::::::::");
			forma.setDetalleEliminado("numRegistros", (numRegMapEliminados+1));
		}
		//logger.info(":::::::5:::::::::");
		//acomodar los registros del mapa en su nueva posicion
		for(int i=Integer.parseInt(forma.getIndexEliminado()+"");i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				forma.setDetalle(indices[j]+""+i,forma.getDetalle(indices[j]+""+(i+1)));
			}
		}
		//logger.info(":::::::6:::::::::");
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getDetalle().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamos el numero de registros en el mapa.
		forma.setDetalle("numRegistros",ultimaPosMapa);
		
		logger.info(" el valor de vigencias "+forma.getDetalle());
		logger.info(" el valor de vigencias eliminadas "+forma.getDetalleEliminado());
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(codigoInstitucion),Integer.parseInt(forma.getDetalle("numRegistros").toString()), response, request, "detalleVigencias.jsp",Integer.parseInt(forma.getIndexEliminado().toString())==ultimaPosMapa);
		
		
	}
	
	
	public static ActionForward accionEliminarCampo(EntidadesSubContratadasForm forma, HttpServletRequest request, HttpServletResponse response,int codigoInstitucion, ActionMapping mapping) 
	{
		logger.info("Entra a accionEliminarCampo :::::::::::::::::::::::::::"+forma.getResultBusquedaEliminado());
		logger.info("Entra a accionEliminarCampo :::::::::::::::::::::::::::"+forma.getResultBusqueda());
		int numRegMapEliminados=Integer.parseInt(forma.getResultBusquedaEliminado("numRegistros")+"");
		logger.info("numRegMapEliminados : "+numRegMapEliminados);
		//logger.info(":::::::0:::::::::");
		//logger.info("accionEliminarCampo y el numRegMapEliminados"+numRegMapEliminados);
		int ultimaPosMapa=(Integer.parseInt(forma.getResultBusqueda("numRegistros")+"")-1);
		logger.info("ultimaPosMapa : "+ultimaPosMapa);
		//logger.info(":::::::1:::::::::");
		//poner la informacion en el otro mapa.
		//String[] indicesl= (String[])forma.getExcepTipoSala("INDICES_MAPA");		
		
		for(int i=0;i<indices.length;i++)
		{ 
			logger.info(":::::::2:::::::::");
			//solo pasar al mapa los registros que son de BD
			if((forma.getResultBusqueda("estaBd10_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
			{
				logger.info(":::::::3:::::::::");
				forma.setResultBusquedaEliminado(indices[i]+""+numRegMapEliminados, forma.getResultBusqueda(indices[i]+""+forma.getIndexEliminado()));
			}
		}		
		//logger.info(":::::::4:::::::::");
		if((forma.getResultBusqueda("estaBd10_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
		{			
			logger.info(":::::::4:::::::::");
			forma.setResultBusquedaEliminado("numRegistros", (numRegMapEliminados+1));
		}
		logger.info(":::::::5:::::::::");
		//acomodar los registros del mapa en su nueva posicion
		for(int i=Integer.parseInt(forma.getIndexEliminado()+"");i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				forma.setResultBusqueda(indices[j]+""+i,forma.getResultBusqueda(indices[j]+""+(i+1)));
			}
		}
		logger.info(":::::::6:::::::::");
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getResultBusqueda().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamos el numero de registros en el mapa.
		forma.setResultBusqueda("numRegistros",ultimaPosMapa);
		
		//logger.info(" el valor de vigencias "+forma.getDetalle());
		//logger.info(" el valor de vigencias eliminadas "+forma.getDetalleEliminado());
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(codigoInstitucion),Integer.parseInt(forma.getResultBusqueda("numRegistros").toString()), response, request, "entidadesSubContratadas.jsp",Integer.parseInt(forma.getIndexEliminado().toString())==ultimaPosMapa);
		
		
	}
	
	
	/**
	 * Metodo encargado de validar las vigencias
	 * 
	 * @param forma
	 * @param errores
	 * @return
	 */
	public static ActionErrors accionValidar(EntidadesSubContratadasForm forma,ActionErrors errores)
	{
		
		if (forma.getEstado().equals("guardarVig")||forma.getEstado().equals("nuevaVig"))
		{
			for (int i= 0; i < Integer.parseInt(forma.getDetalle("numRegistros")+"");i++)
			{
				//1)se valida que la fecha inicial no venga vacia
				if ((forma.getDetalle(indices[16]+i)).equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial del registro "+(i+1)));
				
				//2)se valida que la fecha Final no venga vacia
				if ((forma.getDetalle(indices[17]+i)).equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","La Fecha Final del registro "+(i+1)));
				else
				{
					if ( forma.getDetalle().containsKey(indices[16]+i) && forma.getDetalle().containsKey(indices[17]+i) &&
							!forma.getDetalle(indices[16]+i).equals("") && !forma.getDetalle(indices[17]+i).equals("") &&
							!UtilidadFecha.esFechaMenorIgualQueOtraReferencia((forma.getDetalle(indices[16]+i)+""), (forma.getDetalle(indices[17]+i)+"")))
						errores.add("descripcion",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia"," Final "+forma.getDetalle(indices[17]+i)+" del registro "+(i+1), "Inicial "+forma.getDetalle(indices[16]+i)));
				}	
				
				//3)se valida que la fecha inicial no venga vacia
				if ((forma.getDetalle(indices[18]+i)).equals("-1"))
					errores.add("descripcion",new ActionMessage("errors.required","El Esquema Tarifario de Servicios del registro "+(i+1)));
				
				//4)se valida que la fecha inicial no venga vacia
				if ((forma.getDetalle(indices[19]+i)).equals("-1"))
					errores.add("descripcion",new ActionMessage("errors.required","El Esquema Tarifario de Inventarios del registro "+(i+1)));
				
				for (int j= i; j < Integer.parseInt(forma.getDetalle("numRegistros")+"");j++)
				{
					if (j>i && !(forma.getDetalle(indices[16]+i)+"").equals("") && !(forma.getDetalle(indices[17]+i)+"").equals("") && !(forma.getDetalle(indices[16]+j)+"").equals("") && !(forma.getDetalle(indices[17]+j)+"").equals(""))
					{
						// se pregunta por el cruze de fechas
						if (UtilidadFecha.existeTraslapeEntreFechas((forma.getDetalle(indices[16]+i)+""), (forma.getDetalle(indices[17]+i)+""), (forma.getDetalle(indices[16]+j)+""), (forma.getDetalle(indices[17]+j)+"")))
							{
								errores.add("descripcion",new ActionMessage("error.rangoFechasInvalido",(forma.getDetalle(indices[16]+j)+""),
										(forma.getDetalle(indices[17]+j)+"")+" del registro "+(j+1),(forma.getDetalle(indices[16]+i)+""),
										(forma.getDetalle(indices[17]+i)+"")+" del registro "+(i+1)));
								
								j= Integer.parseInt(forma.getDetalle("numRegistros")+"");
							}
					}
				
				
				}
				
				
			}
			
			
			
			
		}
		
		
		
		return errores;
	}
	
	
	/**
	 * Metodo Que ordena el mapa.
	 * @param mapaOrdenar
	 * @param forma
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
	public static  HashMap accionOrdenarMapa (HashMap mapaOrdenar, EntidadesSubContratadasForm forma)
	{
		HashMap tmp = new HashMap ();
		tmp=mapaOrdenar;
		
		int numReg = Integer.parseInt(mapaOrdenar.get("numRegistros")+"");
		mapaOrdenar = (Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),mapaOrdenar,numReg));	
		forma.setUltimoPatron(forma.getPatronOrdenar());
		mapaOrdenar.put("numRegistros",numReg+"");
		mapaOrdenar.put("INDICES_MAPA",indices);	
		logger.info("sale de ordenar ");
	
		
		
		
		return mapaOrdenar;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static HashMap asignarUsuarios(Connection con, EntidadesSubContratadasForm forma,UsuarioBasico usuario, ActionMapping mapping)
	{
		HashMap mapa = new HashMap();
		int n= Utilidades.convertirAEntero(forma.getUsuariosSelMap("numRegistros")+"");
		//Se agrega ésta validación apra que no se agreguen registros en blanco como la hacóa antes tarea 119697
		if (!forma.getUsuarios().split(ConstantesBD.separadorSplit)[0].equals(ConstantesBD.codigoNuncaValido+""))
		{
			forma.setUsuariosSelMap("login_"+n, forma.getUsuarios().split(ConstantesBD.separadorSplit)[0]);
			forma.setUsuariosSelMap("nombre_"+n, forma.getUsuarios().split(ConstantesBD.separadorSplit)[1]);
			forma.setUsuariosSelMap("eliminar_"+n, ConstantesBD.acronimoNo);
			forma.setUsuariosSelMap("nuevo_"+n, ConstantesBD.acronimoSi);
			forma.setUsuariosSelMap("numRegistros", n+1);
		}
		mapa= forma.getUsuariosSelMap();
		logger.info("\n\nMAPA USUARIOOOOOOS--------------->"+mapa);
		
		return mapa;
	}
	
	//Validación por tarea 119697
	public static boolean existeUsuario(Connection con, EntidadesSubContratadasForm forma,UsuarioBasico usuario, ActionMapping mapping)
	{
		int n= Utilidades.convertirAEntero(forma.getUsuariosSelMap("numRegistros")+"");
		boolean yaExisteusuario=false;
		
		for (int i=0;i<n;i++)
		{
			if(
				(forma.getUsuariosSelMap("login_"+i).toString().equals(forma.getUsuarios().split(ConstantesBD.separadorSplit)[0])&&
					forma.getUsuariosSelMap("nuevo_"+i).toString().equals(ConstantesBD.acronimoSi)&&
						forma.getUsuariosSelMap("eliminar_"+i).toString().equals( ConstantesBD.acronimoNo))
				||
				(forma.getUsuariosSelMap("login_"+i).toString().equals(forma.getUsuarios().split(ConstantesBD.separadorSplit)[0])&&
					forma.getUsuariosSelMap("nuevo_"+i).toString().equals(ConstantesBD.acronimoNo)&&
						forma.getUsuariosSelMap("eliminar_"+i).toString().equals( ConstantesBD.acronimoNo))
				)
				yaExisteusuario=true;
		}
		return yaExisteusuario;
	}
	//Fin Validación por tarea 119697

	
	@SuppressWarnings("rawtypes")
	public static HashMap eliminarUsuario(Connection con, EntidadesSubContratadasForm forma,UsuarioBasico usuario, ActionMapping mapping)
	{
		HashMap mapa = new HashMap();
		
		
		int n= Utilidades.convertirAEntero(forma.getIndiceElimUsu());
		
		forma.setUsuariosSelMap("eliminar_"+n, ConstantesBD.acronimoSi);
		
		mapa= forma.getUsuariosSelMap();
		
		logger.info("\n\nMAPA USUARIOOOOOOS--------------->"+mapa);
		
		return mapa;
	}
	
}