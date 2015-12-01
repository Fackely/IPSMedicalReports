/*
 * Creado el Apr 18, 2006
 * por Julian Montoya
 */
package com.princetonsa.mundo.interfaz;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;

import com.princetonsa.dao.interfaz.CuentaServicioDao;


public class CuentaServicio {

	
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(CuentaServicio.class);
	
	/**
	 * para almacenar el centro de costo.
	 */
	private String centroCosto;

	/**
	 * Mapa para el manejo de las consultas
	 */
	private HashMap mapa;
	
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private CuentaServicioDao  cuentaServicioDao = null;


	
	/**
	 * Constructor de la clase, inicializa en vacío todos los atributos
	 */
	public CuentaServicio ()
	{
		reset();
		this.init(System.getProperty("TIPOBD"));
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
			cuentaServicioDao = myFactory.getCuentaServicioDao();
			wasInited = (cuentaServicioDao != null);
		}
		return wasInited;
	}
	
	
	/**
	 * Metodo para limpiar el objeto
	 *
	 */
	private void reset()
	{
		//this.mapa = new HashMap();
	}

	/**
	 * Metodo para cargar los grupos.
	 * @param institucion 
	 * @return
	 * @throws SQLException 
	 */

	public HashMap cargarGrupos(Connection con, int tipoGrupo, int institucion) throws SQLException 
	{
		if (cuentaServicioDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (cuentaServicioDao - cargarGruposServicio )");
		}
		
		int cc = 0, grupoServicio = -1,  especilidad = -1;
		String tipoServicio = "";
		
		if ( UtilidadCadena.noEsVacio(this.centroCosto) ) 
		{
			cc = Utilidades.convertirAEntero(this.centroCosto); 
		}
		if ( UtilidadCadena.noEsVacio(this.mapa.get("grupoSeleccionado")+"") )
		{
			grupoServicio = Utilidades.convertirAEntero(this.mapa.get("grupoSeleccionado")+"");	
		}
		if ( UtilidadCadena.noEsVacio(this.mapa.get("tipoServicioSeleccionado")+"") )
		{
			tipoServicio = this.mapa.get("tipoServicioSeleccionado")+"";	
		}
		if ( UtilidadCadena.noEsVacio(this.mapa.get("especialidadSeleccionada")+"") )
		{
			especilidad = Utilidades.convertirAEntero(this.mapa.get("especialidadSeleccionada")+"");	
		}
		
		logger.info("EL CENTRO DE COSTO ES------>"+centroCosto);
		return cuentaServicioDao.cargarGrupos(con, tipoGrupo, cc, grupoServicio, tipoServicio, institucion, especilidad);	 
	}
 

	/**
	 * Metodo para insertar los grupos.
	 * @param loginUsuario 
	 * @return
	 * @throws SQLException 
	 * @throws SQLException 
	 */
	 @SuppressWarnings("unused")
	public int insertarGrupos(Connection con, int tipoGrupo, String loginUsuario) throws SQLException
	{
		 
		 
		 DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int  resp1=0;
		int  resp1Nul=0;
			
		if (cuentaServicioDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (cuentaServicioDao - insertarGrupos )");
		}
		
		//-------------------------------------------------------------------------------------------------------------------
		//----Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		inicioTrans=myFactory.beginTransaction(con);
		//--si se desea insertar grupos de servicios.
		if (tipoGrupo == 0)
		{
			int nroGrp = 0;
			if ( UtilidadCadena.noEsVacio(this.getMapa("numRegistros")+"") )
			{
				nroGrp = Integer.parseInt(this.getMapa("numRegistros")+"");  
			}
		    
			if (nroGrp > 0)
			{
				String esParaTodos = this.getMapa("todosGruposServicios")+"";
				String esParaTodosVigAnt = this.getMapa("todosGruposServiciosVigAnt")+"";
				String esParaTodosCtaCosto= this.getMapa("todosGruposServiciosCtaCosto")+"";
			

					boolean hayTodos = false;
					boolean hayTodosVigAnt = false;
					boolean hayTodosCtaCosto=false;
					
					for (int i = 0; i < nroGrp; i++)
					{
						int cod = Integer.parseInt(this.getMapa("codigo_grupo_" + i) + "");		  //-Codigo del grupo
						String cuentaIngreso = (this.getMapa("cuentaingreso_" + i) + "");		  //-informacion ingresada.
						boolean modificado = (this.getMapa("modificado_" + i) + "").equals("-1") ? false : true;
						String cuenta_ho = this.getMapa("h_cuentaingreso_" + i) + "";	 		  //-Para verificar si se modifico.	 
						//
						String cuentaIngresoVigAnterior = (this.getMapa("cuenta_ingreso_vig_anterior_" + i) + "");		  //-informacion ingresada.
						String cuentaIngresoVigAnterior_ho = this.getMapa("h_cta_ingreso_vig_anterior_" + i) + "";	 		  //-Para verificar si se modifico.						
						//
						
						//Cambios Anexo 809
						String cuentaCosto=(this.getMapa("cuentacosto_"+i)+"");
						String cuentaCostoHo=(this.getMapa("cuentacostoho_"+i)+"");
						if (cod == -1)
						{
							hayTodos = true;
							hayTodosVigAnt = true;
							hayTodosCtaCosto=true;
						}
						if (
							( 
								( 
										(UtilidadCadena.noEsVacio(cuentaIngreso) && Integer.parseInt(cuentaIngreso)>0 ) 
										|| 
										( !UtilidadCadena.noEsVacio(cuentaIngreso) && modificado )  
								) 
								&& 
								!cuenta_ho.equals(cuentaIngreso) 
								|| 
								( 
									(UtilidadCadena.noEsVacio(cuentaIngresoVigAnterior) && Integer.parseInt(cuentaIngresoVigAnterior)>0) 
									|| 
									( !UtilidadCadena.noEsVacio(cuentaIngresoVigAnterior) && modificado )
								) 
								&& 
								!cuentaIngresoVigAnterior_ho.equals(cuentaIngresoVigAnterior)
							)
							||
							(
									UtilidadCadena.noEsVacio(cuentaCosto)
									|| 
								( !UtilidadCadena.noEsVacio(cuentaCosto) && modificado )
									
							)
							&& !cuentaCostoHo.equals(cuentaCosto)
						)
						{	
							if (modificado) //-Se debe generar el LOG
							{
								String log = "\n================================MODIFICACION DE GRUPOS DE SERVICIOS================================";
								if (cod != -1) 
									{
										log += "\n CODIGO GRUPO :  " + cod;
									    log += "\n NOMBRE GRUPO :  " + this.getMapa("nombre_grupo_" + i); 
									}
								else
									{
										log += "\n NOMBRE GRUPO : TODOS LOS GRUPOS."; 
									}
									   log += "\n CUENTA INGRESO ANTERIOR  :  " + cuenta_ho; 
									   log += "\n CUENTA INGRESO NUEVA  :  " + cuentaIngreso; 
									   log += "\n CUENTA VIGENCIA ANTERIOR NUEVA  :  " + cuentaIngresoVigAnterior; 
									   log += "\n CUENTA VIGENCIA ANTERIOR ANTERIOR  :  " + cuentaIngresoVigAnterior_ho;
									   log += "\n CUENTA COSTO NUEVA  :  " + cuentaCosto;
									   log += "\n CUENTA COSTO ANTERIOR  :  " + cuentaCostoHo;
									   
									   log += "\n==================================================================================================";
								//-- Generar el log. 
								LogsAxioma.enviarLog(ConstantesBD.logInterfazFactServicioGrpModCodigo, log, ConstantesBD.tipoRegistroLogModificacion, loginUsuario);
							}
							
							logger.info("paso  valdiacion: "+cuentaCosto);
							//---El cero es para indicar que se van a insertar grupos. 
							resp1=cuentaServicioDao.insertarDatos(con, 0, Integer.parseInt(this.centroCosto), cod, cuentaIngreso, modificado, "", "",cuentaIngresoVigAnterior, cuentaCosto); //-El cero es para insercion de grupos
							resp1Nul=cuentaServicioDao.eliminarCuentasContablesNulas (con,0);
						}
						else 
						{
							resp1 = 1; 
						}
												
						//---Para romper el ciclo y hacer el rollback
						if (resp1 < 1) { break; }
					}
					

					//--- Insertar El Registro que representa todos los grupos.
					if ( (UtilidadCadena.noEsVacio(esParaTodos) && !hayTodos &&  Integer.parseInt(esParaTodos)>0) || ( UtilidadCadena.noEsVacio(esParaTodosVigAnt) && !hayTodosVigAnt &&  Integer.parseInt(esParaTodosVigAnt)>0 )
							||(UtilidadCadena.noEsVacio(esParaTodosCtaCosto) && !hayTodosCtaCosto &&  Integer.parseInt(esParaTodosCtaCosto)>0))
					{
						//-- El cero es para indicar que se van a insertar grupos
						resp1=cuentaServicioDao.insertarDatos(con, 0, Integer.parseInt(this.centroCosto), -1, esParaTodos, false, "", "", esParaTodosVigAnt,esParaTodosCtaCosto); //-El cero es para insercion de grupos
						resp1Nul=cuentaServicioDao.eliminarCuentasContablesNulas (con,0);
					}

				//}	
					
			}	
		}

		//--si se desea insertar grupos de Tipos de servicios.
		if (tipoGrupo == 1)
		{
			int nroTpSer = 0;
			if ( UtilidadCadena.noEsVacio(this.getMapa("nroRegTiposServicios")+"") )
			{
				nroTpSer = Integer.parseInt(this.getMapa("nroRegTiposServicios")+"");  
			}
			
		    
			if (nroTpSer > 0)
			{
				String esParaTodos = this.getMapa("todosTiposServicios")+"";
				String esParaTodosVigAnt = this.getMapa("todosTiposServiciosVigAnt")+"";
				String esparaTodosCuentaCosto=this.getMapa("todosCuentaCosto")+"";
				
					//----------Insertar los grupos de servicios especificos.
					boolean hayTodos = false;
					boolean hayTodosVigAnt = false;
					boolean hayTodosCuentaCosto= false;
					
					for (int i = 0; i < nroTpSer; i++)
					{
							
						String cod = this.getMapa("codigo_tipo_servicio_" + i) + "";		 		  //-Codigo del tipo 
						String cuentaIngreso = (this.getMapa("cuentaingreso_tser_" + i) + "");		  //-informacion ingresada.
						String cuenta_ho = (this.getMapa("h_cuentaingreso_tser_" + i) + "");		  //-informacion De La BD.
						boolean modificado = (this.getMapa("modificado_tser_" + i) + "").equals("-1") ? false : true;
						//
						String cuentaIngresoVigAnterior = (this.getMapa("cta_ingr_vig_ant_tser_" + i) + "");		  //-informacion ingresada.
						String cuentaIngresoVigAnterior_ho = this.getMapa("h_cta_ing_vig_ant_" + i) + "";	 		  //-Para verificar si se modifico.						
						//
						
						//Cambios Anexo 809
						String cuentaCosto=(this.getMapa("cuentacostotser_" + i) + "");
						String cuentaCostoH=(this.getMapa("cuentacostotserho_" + i) + "");
						
						
						if (cod.equals("1")) 
						{ hayTodos = true; 
						  hayTodosVigAnt = true;
						  hayTodosCuentaCosto=true;
						}
						
						logger.info("\n\n Centro Costo  " + this.centroCosto + "  Codigo Grupo " + cod + " cuentaIngreso  "  + cuentaIngreso + "  modificado  " + modificado + "\n CuentaCosto "+cuentaCosto);
						
						if ( ( 
								(UtilidadCadena.noEsVacio(cuentaIngreso) && Integer.parseInt(cuentaIngreso)>0 ) 
								|| 
								( !UtilidadCadena.noEsVacio(cuentaIngreso) && modificado )  
							)
							&& !cuenta_ho.equals(cuentaIngreso)
							|| 
							( (UtilidadCadena.noEsVacio(cuentaIngresoVigAnterior) && Integer.parseInt(cuentaIngresoVigAnterior)>0 ) 
								|| ( !UtilidadCadena.noEsVacio(cuentaIngresoVigAnterior) && modificado )  ) 
							&& !cuentaIngresoVigAnterior_ho.equals(cuentaIngresoVigAnterior) 
							|| 
							(
									UtilidadCadena.noEsVacio(cuentaCosto)
									|| 
								( !UtilidadCadena.noEsVacio(cuentaCosto) && modificado )
									
							)
							&& !cuentaCostoH.equals(cuentaCosto)
							)
						{
							if (modificado) //-Se debe generar el LOG
							{
								String log = "\n================================MODIFICACION DE TIPOS DE SERVICIOS================================"; 
									  if (!cod.equals("1"))
									  {										   
									   log += "\n CODIGO TIPO SERVICIO :  " + cod; 
									   log += "\n NOMBRE TIPO SERVICIO :  " + this.getMapa("nombre_tipo_servicio_" + i);
									  }
									  else
									  {
									   log += "\n NOMBRE TIPO SERVICIO : TODOS LOS TIPOS DE SERVICIOS. ";
									  }
									   log += "\n CUENTA INGRESO ANTERIOR  :  " + cuenta_ho; 
									   log += "\n CUENTA INGRESO NUEVA  :  " + cuentaIngreso; 
									   log += "\n CUENTA VIGENCIA ANTERIOR NUEVA  :  " + cuentaIngresoVigAnterior; 
									   log += "\n CUENTA VIGENCIA ANTERIOR ANTERIOR  :  " + cuentaIngresoVigAnterior_ho;
									   log += "\n CUENTA COSTO  NUEVA:  " + cuentaCosto;
									   log += "\n CUENTA COSTO  ANTERIOR:  " + cuentaCostoH;
									   log += "\n==================================================================================================";
								//-Generar el log 
								LogsAxioma.enviarLog(ConstantesBD.logInterfazFactServicioTipoModCodigo, log, ConstantesBD.tipoRegistroLogModificacion, loginUsuario);
							}
						
							//-Obtener el numero del grupo del servicio.
							int grpSel = Integer.parseInt(this.mapa.get("grupoSeleccionado") + ""); 

							logger.info("voy a ingresar la cta de costo--->"+cuentaCosto);
							
							//---El cero es para indicar que se van a insertar grupos 
							resp1=cuentaServicioDao.insertarDatos(con, 1, Integer.parseInt(this.centroCosto), grpSel, cuentaIngreso, modificado, cod, "",cuentaIngresoVigAnterior,cuentaCosto); //-El Uno cero es para insercion de tipos
							resp1Nul=cuentaServicioDao.eliminarCuentasContablesNulas (con,1);
						}
						else 
						{
							resp1 = 1; 
						} 
						
						//---Para romper el ciclo y hacer el rollback
						if (resp1 < 1) { break; }
					}
					//--- Insertar El Registro que representa todos los grupos.
					if ( (UtilidadCadena.noEsVacio(esParaTodos) && !hayTodos &&  Integer.parseInt(esParaTodos)>0) || ( UtilidadCadena.noEsVacio(esParaTodosVigAnt) && !hayTodosVigAnt &&  Integer.parseInt(esParaTodosVigAnt)>0 ) ||
							( UtilidadCadena.noEsVacio(esparaTodosCuentaCosto) && !hayTodosCuentaCosto &&  Integer.parseInt(esparaTodosCuentaCosto)>0 ))
					{

						//-Obtener el numero del grupo del servicio.
						int grpSel = Integer.parseInt(this.mapa.get("grupoSeleccionado") + ""); 

						logger.info("\n\n El GRUPO  [" + grpSel +"] esParaTodos [" + esParaTodos +"] \n\n");		
						//---El cero es para indicar que se van a insertar grupos 
						resp1=cuentaServicioDao.insertarDatos(con, 1, Integer.parseInt(this.centroCosto), grpSel, esParaTodos, false, "1", "", esParaTodosVigAnt,esparaTodosCuentaCosto); //-El Uno cero es para insercion de tipos
						resp1Nul=cuentaServicioDao.eliminarCuentasContablesNulas (con,1);
					}
					
				//}	
					
			}	
		}
		
		
		//--si se desea insertar Especialidades de servicios.
		if (tipoGrupo == 2)
		{
			int nroEsp = 0;
			if ( UtilidadCadena.noEsVacio(this.getMapa("nroRegEspecialidades")+"") )
			{
				nroEsp = Integer.parseInt(this.getMapa("nroRegEspecialidades")+"");  
			}
			
		    
			if (nroEsp > 0) 
			{
				String esParaTodos = this.getMapa("todosEspecialidad")+"";
				String esParaTodosVigAnt = this.getMapa("todosEspecialidadVigAnt")+"";
				String esParaTodosCuentaCosto = this.getMapa("todosCuentaCosto")+"";

				
					//----------Insertar las especialidades especificas.
					boolean hayTodos = false;
					boolean hayTodosVigAnt = false;
					boolean hayTodosCuentaCosto= false;

					//-Obtener el numero del grupo del servicio y el tipo de servicio.
					String grpSel = this.mapa.get("grupoSeleccionado") + ""; 
					String tipoSerSel = this.mapa.get("tipoServicioSeleccionado") + "";
					

					for (int i = 0; i < nroEsp; i++)
					{
						int cod = Integer.parseInt( this.getMapa("codigo_especialidad_" + i) + "");		//-Codigo del tipo 
						String cuentaIngreso = (this.getMapa("cuentaingreso_esp_" + i) + "");			//-informacion ingresada.
						String cuenta_ho = (this.getMapa("h_cuentaingreso_esp_" + i) + "");		  	    //-informacion ingresada.
						boolean modificado = (this.getMapa("modificado_esp_" + i) + "").equals("-1") ? false : true;
						//
						String cuentaIngresoVigAnterior = (this.getMapa("cta_ing_vig_ant_esp_" + i) + "");		  //-informacion ingresada.
						String cuentaIngresoVigAnterior_ho = this.getMapa("h_cta_ing_vig_ant_esp_" + i) + "";	 		  //-Para verificar si se modifico.
						
						String cuentaCosto = (this.getMapa("cuentacostoesp_" + i) + "");		  //-informacion ingresada.
						String cuentaCostoH = this.getMapa("cuentacostoespho_" + i) + "";	 		  //-Para verificar si se modifico.
						//		
						if (cod == -1)	
						{ 
							hayTodos = true;
							hayTodosVigAnt = true;
							hayTodosCuentaCosto=true;
						}
						
						
						if ( ( (UtilidadCadena.noEsVacio(cuentaIngreso) && Integer.parseInt(cuentaIngreso)>0 ) 
								|| ( !UtilidadCadena.noEsVacio(cuentaIngreso) && modificado )  ) 
							&& !cuenta_ho.equals(cuentaIngreso) 
							|| ( (UtilidadCadena.noEsVacio(cuentaIngresoVigAnterior) && Integer.parseInt(cuentaIngresoVigAnterior)>0 ) 
									|| ( !UtilidadCadena.noEsVacio(cuentaIngresoVigAnterior) && modificado )  ) 
									&& !cuentaIngresoVigAnterior_ho.equals(cuentaIngresoVigAnterior)
							||UtilidadCadena.noEsVacio(cuentaCosto)
							|| 
							( !UtilidadCadena.noEsVacio(cuentaCosto) && modificado )
								&& !cuentaCostoH.equals(cuentaCosto))						
						{
							if (modificado) //-Se debe generar el LOG
							{
								String log = "\n================================MODIFICACION DE ESPECIALIDADES DE SERVICIOS================================"; 
									   
									   if ( cod != -1 )
									   {
										   log += "\n CODIGO ESPECIALIDAD DEL SERVICIO : " + cod; 
										   log += "\n NOMBRE ESPECIALIDAD DEL SERVICIO : " + this.getMapa("nombre_especialidad_" + i);
									   }
									   else 
									   {
										   log += "\n NOMBRE ESPECIALIDAD : TODAS LAS ESPECIALIDADES. ";
									   }
									   
									   log += "\n CUENTA INGRESO ANTERIOR  :  " + cuenta_ho; 
									   log += "\n CUENTA INGRESO NUEVA  :  " + cuentaIngreso; 
									   log += "\n CUENTA VIGENCIA ANTERIOR NUEVA  :  " + cuentaIngresoVigAnterior; 
									   log += "\n CUENTA VIGENCIA ANTERIOR ANTERIOR  :  " + cuentaIngresoVigAnterior_ho;
									   
									   log += "\n CUENTA COSTO NUEVA  :  " + cuentaCosto; 
									   log += "\n CUENTA COSTO ANTERIOR  :  " + cuentaCostoH;
									   log += "\n==================================================================================================";
								//-Generar el log 
								LogsAxioma.enviarLog(ConstantesBD.logInterfazFactServicioEspModCodigo, log, ConstantesBD.tipoRegistroLogModificacion, loginUsuario);
							}
						
							logger.info("valor de cuenta de costo--->"+cuentaCosto);
							//---El cero es para indicar que se van a insertar grupos 
							resp1=cuentaServicioDao.insertarDatos(con, 2, Integer.parseInt(this.centroCosto), cod, cuentaIngreso, modificado, grpSel, tipoSerSel, cuentaIngresoVigAnterior,cuentaCosto); //-El Uno cero es para insercion de tipos
							resp1Nul=cuentaServicioDao.eliminarCuentasContablesNulas (con,2);
						}
						else 
						{
							resp1 = 1; 
						} 
						
						//---Para romper el ciclo y hacer el rollback
						if (resp1 < 1) { break; }
					}
					//--- Insertar El Registro que representa todos los grupos.
					if ( (UtilidadCadena.noEsVacio(esParaTodos) && !hayTodos &&  Integer.parseInt(esParaTodos)>0) || ( UtilidadCadena.noEsVacio(esParaTodosVigAnt) && !hayTodosVigAnt &&  Integer.parseInt(esParaTodosVigAnt)>0 ) 
							|| (UtilidadCadena.noEsVacio(esParaTodosCuentaCosto) && !hayTodosCuentaCosto &&  Integer.parseInt(esParaTodosCuentaCosto)>0))
					{
						//---El cero es para indicar que se van a insertar grupos 
						logger.info("valor de cuenta de costo para todos--->"+esParaTodosCuentaCosto);
						resp1=cuentaServicioDao.insertarDatos(con, 2, Integer.parseInt(this.centroCosto), -1, esParaTodos, false, grpSel, tipoSerSel,esParaTodosVigAnt,esParaTodosCuentaCosto); //-El Uno cero es para insercion de tipos
						resp1Nul=cuentaServicioDao.eliminarCuentasContablesNulas (con,2);
					}					
					
				//}	
					
			}	
		}		

		//-- Si se desea insertar los servicios.
		if (tipoGrupo == 3)
		{
			int nroSer = 0;
			if ( UtilidadCadena.noEsVacio(this.getMapa("nroRegServicios")+"") )
			{
				nroSer = Integer.parseInt(this.getMapa("nroRegServicios")+"");  
			}
			
			if (nroSer > 0)
			{
				//----Insertar los servicios especificos
				for (int i = 0; i < nroSer; i++)
				{
					int cod = Integer.parseInt( this.getMapa("codigo_servicio_" + i) + "");	//-Codigo del tipo 
					String cuentaIngreso = (this.getMapa("cuentaingreso_ser_" + i) + "");			//-informacion ingresada.
					String cuenta_ho = (this.getMapa("h_cuentaingreso_ser_" + i) + "");		  	    //-informacion ingresada.
					boolean modificado = (this.getMapa("modificado_ser_" + i) + "").equals("-1") ? false : true;
					//
					String cuentaIngresoVigAnterior = (this.getMapa("cta_ing_vig_ant_ser_" + i) + "");		  //-informacion ingresada.
					String cuentaIngresoVigAnterior_ho = this.getMapa("h_cta_ing_vig_ant_ser_" + i) + "";	 		  //-Para verificar si se modifico.						
					//		
					
					String cuentaCosto= (this.getMapa("cuentacostoser_" + i) + "");		  //-informacion ingresada.
					String cuentaCostoH = this.getMapa("cuentacostoser_" + i) + "";	 		  //-Para verificar

					if ( 
							( (UtilidadCadena.noEsVacio(cuentaIngreso) && Integer.parseInt(cuentaIngreso)>0 ) 
									|| ( !UtilidadCadena.noEsVacio(cuentaIngreso) && modificado )  )
							&& !cuenta_ho.equals(cuentaIngreso) 
							||( (UtilidadCadena.noEsVacio(cuentaIngresoVigAnterior)	&& Integer.parseInt(cuentaIngresoVigAnterior)>0 ) 
								|| ( !UtilidadCadena.noEsVacio(cuentaIngresoVigAnterior) && modificado )  ) 
							&& !cuentaIngresoVigAnterior_ho.equals(cuentaIngresoVigAnterior) 
							||	(( UtilidadCadena.noEsVacio(cuentaCosto) && Utilidades.convertirAEntero(cuentaCosto)>0 )
									|| (!UtilidadCadena.noEsVacio(cuentaCosto) && modificado)) ||  !cuentaCostoH.equals(cuentaCosto)
						)
					{
						if (modificado) //-Se debe generar el LOG
						{
							String log = "\n================================MODIFICACION DE SERVICIOS================================"; 
								   log += "\n CODIGO :  " + cod; 
								   log += "\n NOMBRE :  " + this.getMapa("nombre_servicio_" + i); 
								   log += "\n CUENTA INGRESO ANTERIOR  :  " + cuenta_ho; 
								   log += "\n CUENTA INGRESO NUEVA  :  " + cuentaIngreso; 
								   log += "\n CUENTA VIGENCIA ANTERIOR NUEVA  :  " + cuentaIngresoVigAnterior; 
								   log += "\n CUENTA VIGENCIA ANTERIOR ANTERIOR  :  " + cuentaIngresoVigAnterior_ho;
								   log += "\n CUENTA COSTO NUEVA  :  " + cuentaCosto; 
								   log += "\n CUENTA COSTO ANTERIOR  :  " + cuentaCostoH;
								   log += "\n==================================================================================================";
							//-Generar el log 
							LogsAxioma.enviarLog(ConstantesBD.logInterfazFactServicioSerModCodigo, log, ConstantesBD.tipoRegistroLogModificacion, loginUsuario);
						}
					
						logger.info("VALOR DE LA CUENAT DE COSTO------->"+cuentaCosto);
						//---El cero es para indicar que se van a insertar grupos 
						resp1=cuentaServicioDao.insertarDatos(con, 3, Integer.parseInt(this.centroCosto), cod, cuentaIngreso, modificado, "", "",cuentaIngresoVigAnterior,cuentaCosto); //-El Uno cero es para insercion de tipos
						resp1Nul=cuentaServicioDao.eliminarCuentasContablesNulas (con,3);
					}
					else 
					{
						resp1 = 1; 
					} 
					
					//---Para romper el ciclo y hacer el rollback
					if (resp1 < 1) { break; }
				}
			}	
		}		
		
		
		//----Barre el mapa de dieta
		if (!inicioTrans||resp1<1)
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		
		
		return resp1;
	}	

	/**
	 * Metodo para eliminar las cuentas
	 * @param con
	 * @param loginUsuario 
	 * @param i
	 * @param loginUsuario
	 * @throws SQLException 
	 */
	 @SuppressWarnings("unused")
	public int eliminar(Connection con, int tablaDestino, String loginUsuario) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int  resp1=0; 
			
		if (cuentaServicioDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (cuentaServicioDao - eliminar )");
		}
		
		//-------------------------------------------------------------------------------------------------------------------
		//----Iniciamos la transacción
		boolean inicioTrans;
		inicioTrans=myFactory.beginTransaction(con);
		
		if ( 	UtilidadCadena.noEsVacio(this.mapa.get("posicionE")+"") 
				&& !(this.mapa.get("posicionE")+"").equals("-1")
			)
		{
			int grupoServicio = 0, especialidad = 0, servicio = 0;
			String tipoServicio = "";
			int posicion = Integer.parseInt(this.mapa.get("posicionE")+"");
			int cc = Integer.parseInt(this.centroCosto);

			String nombreCuenta = this.mapa.get("nombreCuenta")+"";
			String nombreCuentaEliminar = this.mapa.get("nombreCuentaVigAnt")+"";
			String nombreCuentaCosto = this.mapa.get("nombreCuentaCosto")+"";

			//--- Eliminar Grupos. 
			if (tablaDestino==0) 
				{ 
					grupoServicio = Integer.parseInt(this.mapa.get("grupoServicioE")+"");
				}

			//--- Eliminar Tipos. 
			if (tablaDestino==1) 
				{ 
					grupoServicio = Integer.parseInt(this.mapa.get("grupoSeleccionado")+"");
					tipoServicio = this.mapa.get("tipoServicioE")+"";
				}
			
			//--- Eliminar Especialidades.
			if (tablaDestino==2) 
				{
					grupoServicio = Integer.parseInt(this.mapa.get("grupoSeleccionado")+"");
					tipoServicio = this.mapa.get("tipoServicioSeleccionado")+"";
					especialidad = Integer.parseInt(this.mapa.get("especialidadServicioE")+"");					
				}	//--- Eliminar Especialidades.
			
			//--- Eliminar Servicios
			if (tablaDestino==3) 
			{
				grupoServicio = Integer.parseInt(this.mapa.get("grupoSeleccionado")+"");
				tipoServicio = this.mapa.get("tipoServicioSeleccionado")+"";
				especialidad = Integer.parseInt(this.mapa.get("especialidadSeleccionada")+"");					

				servicio = Integer.parseInt(this.mapa.get("servicioE")+"");					
			}
			
			//-- Enviar a eliminar.
			resp1 = cuentaServicioDao.eliminar(con, tablaDestino, cc,  grupoServicio, tipoServicio, especialidad, servicio);
			
			//-- Generar el LOG.
			if (resp1 > 0)
			{
				String log = "";
				if ( tablaDestino == 0 )
				{
					log = "\n================================ELIMINACIÓN DE GRUPOS DE SERVICIOS================================";
					if (grupoServicio != -1) 
						{
							log += "\n CÓDIGO GRUPO :  " + grupoServicio;
						    log += "\n NOMBRE GRUPO :  " + this.getMapa("nombre_grupo_" + posicion); 
						}
					else
						{
							log += "\n NOMBRE GRUPO : TODOS LOS GRUPOS."; 
						}
						    log += "\n CÓDIGO CUENTA INGRESO : " +  this.getMapa("cuentaingreso_" + posicion);  
						    //log += "\n NOMBRE CUENTA INGRESO : " +  nombreCuenta;
						    
						    log += "\n CÓDIGO CUENTA VIGENCIA ANTERIOR : " +  this.getMapa("cuenta_ingreso_vig_anterior_" + posicion);  
						    //log += "\n NOMBRE CUENTA VIGENCIA ANTERIOR : " +  nombreCuentaEliminar;
						    
						    log+=	"\n CÓDIGO CUENTA COSTO:"+ this.getMapa("cuentacosto_"+posicion);
						    						  
						    log += "\n==================================================================================================";
				}
				if ( tablaDestino == 1 )
				{
					log =  "\n================================ELIMINACIÓN DE TIPOS DE SERVICIOS================================";
					log += "\n CÓDIGO GRUPO :  " + grupoServicio;
			
					if (!tipoServicio.equals("1"))
					  {										   
						log += "\n CODIGO TIPO SERVICIO :  " + this.getMapa("codigo_tipo_servicio_" + posicion);  
						log += "\n NOMBRE TIPO SERVICIO :  " + this.getMapa("nombre_tipo_servicio_" + posicion);
					  }
					  else
					  {
					   log += "\n NOMBRE TIPO SERVICIO : TODOS LOS TIPOS DE SERVICIOS. ";
					  }
					
				    log += "\n CÓDIGO CUENTA INGRESO : " +  this.getMapa("cuentaingreso_tser_" + posicion);  
				    //log += "\n NOMBRE CUENTA INGRESO : " +  nombreCuenta;
				    
				    log += "\n CÓDIGO CUENTA VIGENCIA ANTERIOR : " +  this.getMapa("cta_ingr_vig_ant_tser_" + posicion);  
				    //log += "\n NOMBRE CUENTA VIGENCIA ANTERIOR : " +  nombreCuentaEliminar;
				    
				    log+=	"\n CÓDIGO CUENTA COSTO:"+ this.getMapa("cuentacosto_"+posicion);
				    
				    log += "\n==================================================================================================";
				}
				
				if ( tablaDestino == 2 )
				{
					log =  "\n================================ELIMINACIÓN DE ESPECIALIDADES DE SERVICIOS========================";
					log += "\n CÓDIGO GRUPO :  " + grupoServicio;
					log += "\n CÓDIGO TIPO SERVICIO :  " + tipoServicio;  
			
					   if ( especialidad != -1 )
					   {
						   log += "\n CODIGO ESPECIALIDAD DEL SERVICIO : " + especialidad; 
						   log += "\n NOMBRE ESPECIALIDAD DEL SERVICIO : " + this.getMapa("nombre_especialidad_" + posicion);
					   }
					   else 
					   {
						   log += "\n NOMBRE ESPECIALIDAD : TODAS LAS ESPECIALIDADES. ";
					   }
				    log += "\n CÓDIGO CUENTA INGRESO : " +  this.getMapa("cuentaingreso_esp_" + posicion);  
				    //log += "\n NOMBRE CUENTA INGRESO : " +  nombreCuenta;  
				    
				    log += "\n CÓDIGO CUENTA VIGENCIA ANTERIOR : " +  this.getMapa("cta_ing_vig_ant_esp_" + posicion);  
				    //log += "\n NOMBRE CUENTA VIGENCIA ANTERIOR : " +  nombreCuentaEliminar;
				    
				    log+=	"\n CÓDIGO CUENTA COSTO:"+ this.getMapa("cuentacosto_"+posicion);
				    
				    log += "\n==================================================================================================";
				}

				if ( tablaDestino == 2 )
				{
					log =  "\n================================ELIMINACIÓN DE ESPECIALIDADES DE SERVICIOS========================";
					log += "\n CÓDIGO GRUPO :  " + grupoServicio;
					log += "\n CÓDIGO TIPO SERVICIO :  " + tipoServicio;  
			
					   if ( especialidad != -1 )
					   {
						   log += "\n CODIGO ESPECIALIDAD DEL SERVICIO : " + especialidad; 
						   log += "\n NOMBRE ESPECIALIDAD DEL SERVICIO : " + this.getMapa("nombre_especialidad_" + posicion);
					   }
					   else 
					   {
						   log += "\n NOMBRE ESPECIALIDAD : TODAS LAS ESPECIALIDADES. ";
					   }
				    log += "\n CÓDIGO CUENTA INGRESO : " +  this.getMapa("cuentaingreso_esp_" + posicion);  
				    //log += "\n NOMBRE CUENTA INGRESO : " +  nombreCuenta;  

				    log += "\n CÓDIGO CUENTA VIGENCIA ANTERIOR : " +  this.getMapa("cta_ing_vig_ant_esp_" + posicion);  
				    //log += "\n NOMBRE CUENTA VIGENCIA ANTERIOR : " +  nombreCuentaEliminar;
				    
				    log+=	"\n CÓDIGO CUENTA COSTO:"+ this.getMapa("cuentacosto_"+posicion);
				    
				    log += "\n==================================================================================================";
				}

				if ( tablaDestino == 3 )
				{
					log =  "\n================================ELIMINACIÓN DE SERVICIOS==========================================";
					log += "\n CÓDIGO GRUPO :  " + grupoServicio;
					log += "\n CÓDIGO TIPO SERVICIO :  " + tipoServicio;  
					log += "\n CÓDIGO ESPECIALIDAD :  " + especialidad;  
			
				    log += "\n CODIGO DEL SERVICIO : " + servicio; 
				    log += "\n NOMBRE DEL SERVICIO : " + this.getMapa("nombre_servicio_" + posicion);
				    log += "\n CÓDIGO CUENTA INGRESO : " +  this.getMapa("cuentaingreso_ser_" + posicion);  
				    //log += "\n NOMBRE CUENTA INGRESO : " +  nombreCuenta;  
				    log += "\n CÓDIGO CUENTA VIGENCIA ANTERIOR : " +  this.getMapa("cta_ing_vig_ant_ser_" + posicion);  
				    //log += "\n NOMBRE CUENTA VIGENCIA ANTERIOR : " +  nombreCuentaEliminar;
				    
				    log+=	"\n CÓDIGO CUENTA COSTO:"+ this.getMapa("cuentacosto_"+posicion);
				    
				    log += "\n==================================================================================================";
				}

				
				//-- Generar el log. 
				LogsAxioma.enviarLog(ConstantesBD.logInterfazFactServicioGrpModCodigo, log, ConstantesBD.tipoRegistroLogEliminacion, loginUsuario);
			}
		}
		

		if (!inicioTrans||resp1<1)
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		
		
		return resp1;
	}


	/**
	 * @return Retorna mapa.
	 */
	public HashMap getMapa() {
		return mapa;
	}

	/**
	 * @param Asigna mapa.
	 */
	public void setMapa(HashMap mapa) {
		this.mapa = mapa;
	}

	/**
	 * @return Retorna mapa.
	 */
	public Object getMapa(String key)
	{
		return mapa.get(key);
	}

	/**
	 * @param Asigna mapa.
	 */
	public void setMapa(String key, Object obj) 
	{
		this.mapa.put(key, obj);
	}



	/**
	 * @return Retorna centroCosto.
	 */
	public String getCentroCosto() {
		return centroCosto;
	}



	/**
	 * @param Asigna centroCosto.
	 */
	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}




	
}
