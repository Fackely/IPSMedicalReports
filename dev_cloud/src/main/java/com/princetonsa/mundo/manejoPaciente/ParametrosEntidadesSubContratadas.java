package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.manejoPaciente.ParametrosEntidadesSubContratadasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.ParametrosEntidadesSubContratadasDao;
import com.princetonsa.mundo.UsuarioBasico;




public class ParametrosEntidadesSubContratadas
{
	/**
	 *Atributos parametrosentidadesSubContratadas
	 */
	//private ParametrosEntidadesSubContratadasDao parametrosEntidadesSubContratadasDao;
	private static Logger logger = Logger.getLogger(ParametrosEntidadesSubContratadas.class);
	
	/**
	 * indices para trabajar con la BD 
	 */
	public static final String [] indices = {"codigo0_","codigoParam1_","convenio2_","valor3_","viaIngreso4_","nombre5_","institucion6_",
	  										 "usuarioModifica7_","estaBd8_","centroAtencion9_"};
	
	/**
	 * indices para manejar los nombre de los campos entre al 
	 * jsp y el mundo. 
	 */
	public static final String [] nombresCampo ={
												 ConstantesIntegridadDominio.acronimoMontoCobroXViaIngreso,//0
												 ConstantesIntegridadDominio.acronimoOrigenAdminisionXViaIngreso,//1
												 ConstantesIntegridadDominio.acronimoTipoPacienteXViaIngreso,//2
												 ConstantesIntegridadDominio.acronimoAreaIngresoPacientesXViaIngreso,//3
												 ConstantesIntegridadDominio.acronimoValidarRequeridoNumeroAutorizacionIngreso,//4
												 ConstantesIntegridadDominio.acronimoFarmaciaCargosDirectosArticulos,//5
												 ConstantesIntegridadDominio.acronimoCodigoUtilizadoArticulos,//6
												 ConstantesIntegridadDominio.acronimoProfecionalResponde,//7
												 ConstantesIntegridadDominio.acronimoTipoTarifaCargosDirectos,//8
												 ConstantesIntegridadDominio.acronimoAfectaInventariosCargosInventarios,//9
												 ConstantesIntegridadDominio.acronimoMedicoAdmisionesEgresoHospitalizacion,//10
												 ConstantesIntegridadDominio.acronimoMedicoAdmisionesEgresoUrgencias,//11
												 ConstantesIntegridadDominio.acronimoEspecialidadResponde//12
												};
	
	/*-----------------------------------------------------------
	 *         				METODOS CENSO DE CAMAS
	 ------------------------------------------------------------*/
	
	/**
	 * Constructor de la clase Censo de Camas
	 */
	/*
	public void ParametrosEntidadesSubContratadas()
	{
		DaoFactory MyFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		parametrosEntidadesSubContratadasDao = MyFactory.getParametrosEntidadesSubContratadasDao();
	}*/
	
	
	/**
	 * Se inicializa el Dao
	 */
	
	public static ParametrosEntidadesSubContratadasDao parametrosEntidadesSubContratadasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getParametrosEntidadesSubContratadasDao();
	}
	
	
	
	/**
	 * Metodo encargado de consultar los datos
	 * del la tabla det_param_entid_subcontratada
	 * @param
	 * HashMap parametros
	 * ----------------------------------------------
	 * 			KEY'S DEL HASHMAP PARAMETROS
	 * ----------------------------------------------
	 *  -- codigoParam1_ --> Requerido
	 *  -- convenio2_ --> Opcional
	 *  -- viaIngreso4_ --> Opcional
	 *  -- institucion6_ --> Requerido
	 *  @return 
	 *  HashMap mapa 
	 *  -----------------------------------------------
	 *  			KEY'S DEL HASHMAP MAPA
	 *  -----------------------------------------------
	 *  -- codigo0_
	 *  -- codigoParam1_
	 *  -- convenio2_
	 *  -- valor3_
	 *  -- viaIngreso4_
	 *  -- nombre5_
	 *  -- institucion6_
	 *  -- usuarioModifica7_
	 */
	public static HashMap consultarParametros (Connection connection, HashMap parametros)
	{
		return parametrosEntidadesSubContratadasDao().consultarParametros(connection, parametros);
	}
	
	/**
	 * Metodo encargado de guardar los datos en la tabla 
	 * det_param_entid_subcontratada
	 * @param connection
	 * @param datos
	 * ---------------------------------------------
	 * 			KEY'S DEL HASHMAP DATOS
	 * ---------------------------------------------
	 * -- codigoParam1_
	 * -- convenio2_
	 * -- valor3_
	 * -- usuarioModifica7_
	 * -- viaIngreso4_
	 * -- institucion6_
	 * -- nombre5_
	 @return boolean 
	 */
	public static boolean guardarParametros (Connection connection, HashMap datos)
	{
		return parametrosEntidadesSubContratadasDao().guardarParametros(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de modificar los datos en la tabla 
	 * det_param_entid_subcontratada, puediendo filtrarlos
	 * por convenio, via de ingreso, institucion, nombre, seccion
	 * @param connection
	 * @param datos
	 * ---------------------------------------------
	 * 			KEY'S DEL HASHMAP DATOS
	 * ---------------------------------------------
	 * -- codigoParam1_ --> Requerido
	 * -- convenio2_ --> Opcional
	 * -- valor3_ --> --> Opcional
	 * -- usuarioModifica7_ -->Requerido
	 * -- viaIngreso4_ --> Opcional
	 * -- institucion6_ -->Requerido
	 * -- nombre5_ -->Requerido
	 @return boolean 
	 */
	public static boolean modificarParametros (Connection connection, HashMap datos)
	{
		return parametrosEntidadesSubContratadasDao().modificarParametros(connection, datos);
	}
	
	/**
	 * Metodo encargado de inicializar la pagina
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @return
	 */
	public static ActionForward  initParametros (Connection connection,  ParametrosEntidadesSubContratadasForm forma, ActionMapping mapping)
	{
		//carga los valores de las secciones
		forma.setSecciones(UtilidadesManejoPaciente.obtenerSeccionesParametrosEntidadesSubcontratadas(connection));
		//inicializando el codigo de la seccion en -1
		forma.setCodigoSeccion(ConstantesBD.codigoNuncaValido);
		try 
		{
			UtilidadBD.cerrarConexion(connection);
		} 
		catch (Exception e) 
		{
			logger.info("\n problema cerrando la conexion en initParametros "+e);
			
		}
		
		return mapping.findForward("principal");
	}
	/**
	 * Metodo encargado de identificar que seccion es la que se va a cargar
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @return
	 */
	public static ActionForward cargarSeccion (Connection connection,ParametrosEntidadesSubContratadasForm forma, ActionMapping mapping,UsuarioBasico usuario)
	{
		//se formatea el hashmap deonde se van a guardar los datos
		forma.reset();
		
		//se pregunta si la seccion a cargar es tipo informacion ingreso cuenta
		if (forma.getCodigoSeccion()==ConstantesBD.tipoInformacionIngresoCuenta)
		{
			//se cargan los convenios
			forma.setConvenios(Utilidades.obtenerConvenios(connection, "", "", false, "", true));
		}
		
		//se pregunta si la seccion a cargar es tipo cargos directos
		if (forma.getCodigoSeccion()==ConstantesBD.tipoCargosDirectos)
		{
			//se consultan los datos
			organizarCriterios(connection, forma, usuario);
			//se cargan los profecionales de sistema axioma
			forma.setProfecionales((UtilidadesManejoPaciente.obtenerProfesionales(connection, usuario.getCodigoInstitucionInt(), true, false, "","")));
			//Se cargan las especialidades correspondientes al profesional actual
			if(!UtilidadTexto.isEmpty(forma.getParametros(nombresCampo[7]).toString()))
			{
				forma.setEspecialidades(Utilidades.obtenerEspecialidadesMedico(connection, forma.getParametros(nombresCampo[7]).toString()));
			}
			else
			{
				HashMap<String, Object> mapa = new HashMap<String, Object>();
				mapa.put("numRegistros","0");
				forma.setEspecialidades(mapa);
			}
			//se cargan los centros de atencion
			forma.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(connection, usuario.getCodigoInstitucionInt(),""));
			
		}
		
		//se pregunta si la seccion a cargar es parametros generales
		if (forma.getCodigoSeccion()==ConstantesBD.parametrosGenerales)
		{
			//se consultan los datos
			organizarCriterios(connection, forma, usuario);
			//se cargan los profecionales de sistema axioma
			forma.setProfecionales((UtilidadesManejoPaciente.obtenerProfesionales(connection, usuario.getCodigoInstitucionInt(), true, false, "","")));
		}
		
		try 
		{
			UtilidadBD.cerrarConexion(connection);
		}
		catch (Exception e) 
		{
			logger.info("\n problema cerrando la conexion "+e);
		}
		
		
		return mapping.findForward("principal");
	}
	
	
	/**
	 * Metodo encargado de cargar la subseccion Vias de ingreso
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @return
	 */
	public static ActionForward cargarSubSeccionViasIngreso (Connection connection, ParametrosEntidadesSubContratadasForm forma, ActionMapping mapping, UsuarioBasico usuario)
	{
		HashMap parametros = new HashMap ();
			resetParametros(forma);
		if ((forma.getParametros(indices[2])+"").equals("-1"))
			forma.setEstado("cargarSeccion");
		else
		{
			/*-------------------------------------------------
			 * se ingresan los parametros de busqueda
			 -------------------------------------------------*/
			//convenio
			/*parametros.put(indices[2], forma.getParametros(indices[2]));
			//nombre --> acronimo del campo
			parametros.put(indices[5], ConstantesIntegridadDominio.acronimoValidarRequeridoNumeroAutorizacionIngreso);
			//codigo_param
			parametros.put(indices[1], forma.getCodigoSeccion()); 
			
			//institucion
			parametros.put(indices[6], usuario.getCodigoInstitucionInt()); 
			/*-------------------------------------------------
			 * fin del ingreso de los parametros de busqueda
			 -------------------------------------------------*/
			//se buscan los datos
			/*parametros.putAll(consultarParametros(connection, parametros));
			
			//logger.info("\n el valor de subseccion es "+parametros);
			//logger.info("\n la respuesta es "+parametros);
			if (parametros.containsKey("numRegistros") && Integer.parseInt(parametros.get("numRegistros")+"")==0 )
			{
				//nombre campo
				forma.setParametros(nombresCampo[4],"N");// si no tiene valor se le asigna N
				//estaBd
				forma.setParametros(indices[8]+nombresCampo[4], ConstantesBD.acronimoNo);
			}
			else
				if (parametros.containsKey("numRegistros") && Integer.parseInt(parametros.get("numRegistros")+"")>0)
				{
					//nombre campo
					forma.setParametros(nombresCampo[4],parametros.get(indices[3]+"0"));
					//estaBd
					forma.setParametros(indices[8]+nombresCampo[4], parametros.get(indices[8]+"0"));
				}
				//se saca una copia de los datos del mapa para saber mas adelante si fueron modificados
			  forma.setParametrosClone(Listado.copyMap(forma.getParametros(), nombresCampo));
			  */
				organizarCriterios(connection, forma, usuario);
				forma.setViasIngreso(Utilidades.obtenerViasIngreso(connection,""));		
			try 
			{
				UtilidadBD.cerrarConexion(connection);	
			}
			catch (SQLException e) 
			{
				logger.info("\n problema cerrando la conexion "+e);
			}
		}
		return mapping.findForward("principal");
	}
	
	
	
	/**
	 * Metodo encargado de cargar los valores de 
	 * parametros entidades subcontratadas
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @return
	 */
	public static ActionForward cargarValoresSeccion(Connection connection, ParametrosEntidadesSubContratadasForm forma,UsuarioBasico usuario, ActionMapping mapping)
	{
		HashMap parametros = new HashMap ();
		resetParametros(forma);
		if ((forma.getParametros(indices[4])+"").equals("-1"))
			forma.setEstado("cargarSubSeccionViaingreso");
		else
		{
			/*-----------------------------------------------------------------------------------------------------
			 * se ingresan los parametros de busqueda de los datos de la tabla det_param_entidades_subContratadas
			 -----------------------------------------------------------------------------------------------------*/
			//convenio
			/*parametros.put(indices[2], forma.getParametros(indices[2]));
			//codigo_param
			parametros.put(indices[1], forma.getCodigoSeccion()); 
			//via de ingreso
			parametros.put(indices[4], forma.getParametros(indices[4]));
			//institucion
			parametros.put(indices[6], usuario.getCodigoInstitucionInt()); 
			/*----------------------------------------------------------------------------------------------------------
			 * fin del ingreso de los parametros de busqueda de los datos de la tabla det_param_entidades_subContratadas
			 -----------------------------------------------------------------------------------------------------------*/
			
			//se buscan los datos
			/*parametros.putAll(consultarParametros(connection, parametros));
			
			/*----------------------------------------------------------------
			 * se organiza el mapa para mostrarlo en la jsp
			 ----------------------------------------------------------------*/
			
		/*	for (int i=0; i<Integer.parseInt(parametros.get("numRegistros")+"");i++)
			{
				for(int j=0;j<nombresCampo.length;j++)
				{	
					if ((parametros.get(indices[5]+i)+"").equals(nombresCampo[j]))
					{
						//se carga el valor en el indice del nombre
						forma.setParametros(nombresCampo[j], parametros.get(indices[3]+i));
						//estaBd
						forma.setParametros(indices[8]+nombresCampo[j], parametros.get(indices[8]+i));
						
					}
				}
			}
					
			for(int j=0;j<nombresCampo.length;j++)
			{
				if (!forma.getParametros().containsKey(nombresCampo[j]))
				{
					//se inicializa el parametro
					forma.setParametros(nombresCampo[j], "");
					//estaBd
					forma.setParametros(indices[8]+nombresCampo[j], ConstantesBD.acronimoNo);
				}
			}
		
			//se saca una copia de los datos del mapa para saber mas adelante si fueron modificados
			  forma.setParametrosClone(Listado.copyMap(forma.getParametros(), nombresCampo));
			
			/*----------------------------------------------------------------
			 * fin de la organizacion del mapa para mostrarlo en la jsp
			 ----------------------------------------------------------------*/
				organizarCriterios(connection, forma, usuario);
			//limpiamos el hashmap para volverlo a utilizar
				parametros.clear();
			/*-------------------------------------------------------------
			 * se ingresan los parametros de busqueda de monto de cobro
			 --------------------------------------------------------------*/
			//convenio
			parametros.put("convenio", forma.getParametros(indices[2]));
			//via de ingreso
			parametros.put("viaIngreso", forma.getParametros(indices[4]));
			//activo
			parametros.put("activo", ValoresPorDefecto.getValorTrueParaConsultas());
			//fecha referencia
			parametros.put("fechaReferencia", UtilidadFecha.getFechaActual());
			
			/*------------------------------------------------------------------
			 * Se cargan los selects que la jsp va a utilizar
			 -------------------------------------------------------------------*/	
			//se cargan los montos de cobro
			forma.setMontosCobro(UtilidadesManejoPaciente.obtenerMontosCobroVigentes(connection, parametros));
			//se cargan los origenes de admision
			forma.setOrigenAdmision(cargarOrigenAdmision(connection, forma));
			//se carga el tipo de paciente
			forma.setTiposPaciente(UtilidadesManejoPaciente.ObtenerTiposPaciente(connection,Integer.parseInt(forma.getParametros(indices[4])+"")));
			forma.setNumElementosTP(forma.getNumElementosTP());
			//se carga el area de ingreso por via de ingreso
			forma.setArea(cargarAreaIngresoPaciente(connection, forma, usuario));
			//se cargan los centros de atencion
			forma.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(connection, usuario.getCodigoInstitucionInt(),""));
			
			try 
			{
				UtilidadBD.cerrarConexion(connection);
			}
			catch (Exception e) 
			{
				logger.info("\n problema cerrando la conexion "+e);
			}
		}
		
		return mapping.findForward("principal");
	}
	
	
	/**
	 * Metodo encargado de cargar los valores de la seccion
	 * que filtra con centro de atencion
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @return
	 */
	public static ActionForward cargarSeccionCentroCosto(Connection connection,ParametrosEntidadesSubContratadasForm forma,UsuarioBasico usuario, ActionMapping mapping)
	{
		//se cargan los centros de atencion
		forma.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(connection, usuario.getCodigoInstitucionInt(),""));
		
		
		
		return mapping.findForward("principal");
	}
	
	
	
	
	/**
	 * Metodo encargado de consultar todos los origenes de admision
	 * y luego mediante el ingreso se filtran.
	 * @param connection
	 * @param forma
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> cargarOrigenAdmision (Connection connection,ParametrosEntidadesSubContratadasForm forma)
	{
		ArrayList<HashMap<String, Object>> Origenes = new ArrayList<HashMap<String,Object>>();
		ArrayList<HashMap<String, Object>> tmp = new ArrayList<HashMap<String,Object>>();
		
		//se consultan todos los origenes de admision
		tmp=UtilidadesManejoPaciente.obtenerOrigenesAdmision(connection);
				
			//se recorre el array de origen admision para saber como queda cargado el select
			for (HashMap elemento: tmp)
			{
				
				if (Integer.parseInt(elemento.get("codigo")+"")==ConstantesBD.codigoOrigenAdmisionHospitalariaEsRemitido)
				{
					HashMap dato = new HashMap ();
					dato.put("codigo", elemento.get("codigo"));
					dato.put("nombre", elemento.get("nombre"));
					
					Origenes.add(dato);
				}
								
				//se pregunta si la via de ingreso es diferente de urgencias
				if (!(forma.getParametros(indices[4])+"").equals(ConstantesBD.codigoViaIngresoUrgencias+""))
				{
				
					if (Integer.parseInt(elemento.get("codigo")+"")==ConstantesBD.codigoOrigenAdmisionHospitalariaEsConsultaExterna)
					{
						HashMap dato = new HashMap ();
						dato.put("codigo", elemento.get("codigo"));
						dato.put("nombre", elemento.get("nombre"));
						
						Origenes.add(dato);
					}
				}
								
				//se pregunta si la via de ingreso es diferente de ambulatorio
				if (!(forma.getParametros(indices[4])+"").equals(ConstantesBD.codigoViaIngresoAmbulatorios+""))
				{
				
					if (Integer.parseInt(elemento.get("codigo")+"")==ConstantesBD.codigoOrigenAdmisionHospitalariaEsUrgencias)
					{
						HashMap dato = new HashMap ();
						dato.put("codigo", elemento.get("codigo"));
						dato.put("nombre", elemento.get("nombre"));
						
						Origenes.add(dato);
					}
				}
				
				//se pregunta si la via de ingreso es diferente de ambulatorio
				if ((forma.getParametros(indices[4])+"").equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
				{
				
					if (Integer.parseInt(elemento.get("codigo")+"")==ConstantesBD.codigoOrigenAdmisionHospitalariaEsNacidoInstitucion)
					{
						HashMap dato = new HashMap ();
						dato.put("codigo", elemento.get("codigo"));
						dato.put("nombre", elemento.get("nombre"));
						
						Origenes.add(dato);
					}
				}
				
			}
			
		return Origenes;
	}
	
	
	/**
	 * Metodo  encargado de cargar los tipos de paciente
	 * filtrando por via de ingreso
	 * @param connection
	 * @param forma
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> cargarTipoPaciente (Connection connection,ParametrosEntidadesSubContratadasForm forma)
	{
		ArrayList<HashMap<String, Object>> tiposPaciente = new ArrayList<HashMap<String,Object>>();
		ArrayList<HashMap<String, Object>> tmp = new ArrayList<HashMap<String,Object>>();
		//se consultan todos los tipos de paciente
		tmp=UtilidadesManejoPaciente.ObtenerTiposPaciente(connection,Integer.parseInt(forma.getParametros(indices[4])+""));
		
		for (HashMap elemento: tmp)
		{
//			se pregunta si la via de ingreso es diferente de urgencias
			if (!(forma.getParametros(indices[4])+"").equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
			{
			
				if ((elemento.get("codigo")+"").equals(ConstantesBD.tipoPacienteAmbulatorio))
				{
					HashMap dato = new HashMap ();
					dato.put("acronimo", elemento.get("acronimo"));
					dato.put("nombre", elemento.get("nombre"));
					
					tiposPaciente.add(dato);
				}
			}
		}
		
		return tiposPaciente;
	}
	
	
	/**
	 * Metodo encargado consultar las areas de ingreso
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> cargarAreaIngresoPaciente (Connection connection,ParametrosEntidadesSubContratadasForm forma, UsuarioBasico usuario)
	{
				
		HashMap criterios = new HashMap ();
		
		//institucion
		criterios.put("institucion", usuario.getCodigoInstitucionInt());
		//via de ingreso
		criterios.put("viaIngreso",forma.getParametros(indices[4]));
		//tipo paciente
		criterios.put("tipoPaciente",forma.getParametros(nombresCampo[2]));
		//centro atencion
		criterios.put("centroAtencion", forma.getParametros(indices[9]));
		
		return UtilidadesManejoPaciente.ObtenerAreaIngreso(connection, criterios);
	}
	
	/**
	 * Metodo encargado de guardar los datos que estan antes
	 * de seleccionar la via de ingreso
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	public static ActionForward accionGuardar (Connection connection,ParametrosEntidadesSubContratadasForm forma,UsuarioBasico usuario,ActionMapping mapping)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		
		logger.info("\n :::::::: entro a acionGuardar ::::::: "+forma.getParametros());
		
	/*
		//eliminar
		for(int j=0;j<nombresCampo.length;j++)
		{
			
			
			if((forma.getParametros(indices[8]+nombresCampo[j])+"").trim().equals(ConstantesBD.acronimoSi))
			{				
				if (!UtilidadCadena.noEsVacio(forma.getParametros(nombresCampo[j])+""))
				{
					
				}
				
				 //transacction=eliminarBD(connection, forma);						 
			}
		}		
		*/
		for(int j=0;j<nombresCampo.length;j++)
		{				
			//modificar			
			
			if((forma.getParametros(indices[8]+nombresCampo[j])+"").trim().equals(ConstantesBD.acronimoSi))
			{
				logger.info("\n entre a  el if de modificar -->"+forma.getParametros(nombresCampo[j])+"  clone -->"+forma.getParametrosClone().get(nombresCampo[j]));
				if(!(forma.getParametros(nombresCampo[j])+"").equals(forma.getParametrosClone().get(nombresCampo[j])+""))
				{	
					transacction = modificarBD(connection, forma, nombresCampo[j], usuario);
					
				}	
				
			}
			
			//insertar			
			else if((forma.getParametros(indices[8]+nombresCampo[j])+"").trim().equals(ConstantesBD.acronimoNo))
			{
				
				 transacction = insertarBD(connection, forma, nombresCampo[j], usuario);
				 				
			}
			
			
		}
		
		if(transacction)
		{
			organizarCriterios(connection, forma, usuario);
			UtilidadBD.finalizarTransaccion(connection);			
			logger.info("----->INSERTO 100% CONSENTIMIENTO INFORMADO");
			forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(connection);
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
			forma.setMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(connection);
		}
		
			forma.setEstado(forma.getEstadoAnt());
		return mapping.findForward("principal");
		
	}
	
	
	
	/**
	 * 
	 * @param connection
	 * @param forma
	 * @param campo
	 * @param usuario
	 * @return
	 */
	public static boolean insertarBD (Connection connection, ParametrosEntidadesSubContratadasForm forma, String campo, UsuarioBasico usuario)
	{
		HashMap datos = new HashMap ();
		boolean operacionTrue=true;
		
		
		if (forma.getParametros().containsKey(campo) && !(forma.getParametros(campo)+"").equals("") && !(forma.getParametros(campo)+"").equals("-1"))
		{
			//logger.info("\n :::::::::: entro a insertarBD :::::::: "+forma.getParametros(campo));
			
			//codigo param
			datos.put(indices[1], forma.getCodigoSeccion());
			
			//convenio
			if (forma.getParametros().containsKey(indices[2]) && !(forma.getParametros(indices[2])+"").equals("") && !(forma.getParametros(indices[2])+"").equals("-1"))
				datos.put(indices[2], forma.getParametros(indices[2]));
			//valor
			if (forma.getParametros().containsKey(campo) && !(forma.getParametros(campo).equals("")))
				datos.put(indices[3], forma.getParametros(campo));
			//usuario Modifica
			datos.put(indices[7], usuario.getLoginUsuario());
			
			//via ingreso
			if (forma.getParametros().containsKey(indices[4]) && !(forma.getParametros(indices[4])+"").equals("") && !(forma.getParametros(indices[4])+"").equals("-1"))
				//se valida que los campos que no van por via de ingreso
				if (!campo.equals(nombresCampo[4]))
					datos.put(indices[4], forma.getParametros(indices[4]));
			
			//institucion
			datos.put(indices[6], usuario.getCodigoInstitucionInt());
			
			//nombre
			datos.put(indices[5], campo);
			
//			FILTRO ::::::::::::::: centro atencion
			if (forma.getParametros().containsKey(indices[9]) && !(forma.getParametros(indices[9])+"").equals("") && !(forma.getParametros(indices[9])+"").equals("-1"))
				//aqui se ponen los campos que van filtrados por centro atencion
				if (campo.equals(nombresCampo[3]) || campo.equals(nombresCampo[5]))
					datos.put(indices[9], forma.getParametros(indices[9]));
			
			operacionTrue=guardarParametros(connection, datos);
		}
		
		
		return operacionTrue;
	}
	
	/**
	 * Metodo encargado de organizar los datos para ser 
	 * enviados al sqlbase para que modifique los datos.
	 * @param connection
	 * @param forma
	 * @param campo
	 * @param usuario
	 * @return
	 */
	public static boolean modificarBD (Connection connection, ParametrosEntidadesSubContratadasForm forma, String campo, UsuarioBasico usuario)
	{
		
		
		HashMap datos = new HashMap ();
		boolean operacionTrue=true;
		if (forma.getParametros().containsKey(campo) && !(forma.getParametros(campo)+"").equals("") && !(forma.getParametros(campo)+"").equals("-1"))
		{
			//logger.info("\n :::::::::: entro a ModificarBD :::::::: "+forma.getParametros());
			
			//codigo param
			datos.put(indices[1], forma.getCodigoSeccion());
			
			//FILTRO ::::: convenio
			if (forma.getParametros().containsKey(indices[2]) && !(forma.getParametros(indices[2])+"").equals("") && !(forma.getParametros(indices[2])+"").equals("-1"))
				datos.put(indices[2], forma.getParametros(indices[2]));
			//valor
			if (forma.getParametros().containsKey(campo) && !(forma.getParametros(campo).equals("")))
				datos.put(indices[3], forma.getParametros(campo));
			//usuario Modifica
			datos.put(indices[7], usuario.getLoginUsuario());
			
			//FILTRO :::::: via ingreso
			if (forma.getParametros().containsKey(indices[4]) && !(forma.getParametros(indices[4])+"").equals("") && !(forma.getParametros(indices[4])+"").equals("-1"))
				//se valida que los campos que no van por via de ingreso
				if (!campo.equals(nombresCampo[4]))
					datos.put(indices[4], forma.getParametros(indices[4]));
			
			//institucion
			datos.put(indices[6], usuario.getCodigoInstitucionInt());
			//nombre
			datos.put(indices[5], campo);
			
			//FILTRO ::::::::::::::: centro atencion
			if (forma.getParametros().containsKey(indices[9]) && !(forma.getParametros(indices[9])+"").equals("") && !(forma.getParametros(indices[9])+"").equals("-1"))
				//aqui se ponen los campos que van filtrados por centro atencion
				if (campo.equals(nombresCampo[3])|| campo.equals(nombresCampo[5]))
					datos.put(indices[9], forma.getParametros(indices[9]));
				
			operacionTrue=modificarParametros(connection, datos);
		}
		
		return operacionTrue;
	}
	
	/**
	 * Metodo encargado de filtrar la seccion 
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	public static ActionForward cargarSubseccionCentrosAtencion (Connection connection,ParametrosEntidadesSubContratadasForm forma,ActionMapping mapping, UsuarioBasico usuario)
	{
		logger.info("\n entro cargarSubseccionCentrosAtencion -->"+forma.getParametros());
		if ((forma.getParametros(indices[9])+"").equals(ConstantesBD.codigoNuncaValido+"") && forma.getCodigoSeccion()==ConstantesBD.tipoInformacionIngresoCuenta)
			forma.setEstado("cargarValoresSeccion");
		
		if ((forma.getParametros(nombresCampo[2])+"").equals(ConstantesBD.codigoNuncaValido+""))
			forma.setEstado("cargarValoresSeccion");
		
		if ((forma.getParametros(indices[9])+"").equals(ConstantesBD.codigoNuncaValido+"") && forma.getCodigoSeccion()==ConstantesBD.tipoCargosDirectos)
			forma.setEstado("cargarSeccion");
		else
			if (forma.getCodigoSeccion()==ConstantesBD.tipoInformacionIngresoCuenta)
			{
//				se carga el area ingreso del paciente
				forma.setArea(cargarAreaIngresoPaciente(connection, forma, usuario));
				//se budcan los datos 
				organizarCriterios(connection, forma, usuario);
				
			}
			else
				if (forma.getCodigoSeccion()==ConstantesBD.tipoCargosDirectos)
				{
					//organizarCriterios(connection, forma, usuario);

					forma.setFarmacia(UtilidadesManejoPaciente.obtenerCentrosCosto(connection, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaSubalmacen+"", false, Integer.parseInt(forma.getParametros(indices[9])+"")));
				}
		return mapping.findForward("principal");
	}
	

	/**
	 * Metodo encargado de dar formato al hashmap
	 * parametros.
	 * @param forma
	 */
	public static void resetParametros (ParametrosEntidadesSubContratadasForm forma)
	{
		//logger.info("\n entre a reset "+forma.getParametros());
		//se pregunta si la seccion a cargar es tipo informacion ingreso cuenta
		if (forma.getCodigoSeccion()==ConstantesBD.tipoInformacionIngresoCuenta)
		{
			if (forma.getEstado().equals("cargarSubSeccionViaingreso"))
			{
				//se formatea la via de ingreso
				String tmp="",tmp2="";
				//validar como requerido autorizacion en el ingreso
				//tmp2=forma.getParametros(nombresCampo[4])+"";
				//convenio
				tmp=forma.getParametros(indices[2])+"";
				//forma.setParametros(indices[4],"-1");
				forma.reset();
				forma.setParametros(indices[2], tmp);
				//forma.setParametros(nombresCampo[4], tmp2);
				
				
			}
			
			if (forma.getEstado().equals("cargarValoresSeccion"))
			{
				String tmp="",tmp2="",tmp3="",tmp4="";
				tmp=forma.getParametros(indices[2])+"";
				tmp2=forma.getParametros(indices[4])+"";
				tmp3=forma.getParametros(nombresCampo[4])+"";
				tmp4=forma.getParametros(indices[8]+nombresCampo[4])+"";
				forma.reset();
				//se formatea la via de ingreso
				//forma.setParametros(indices[9],"-1");
				forma.setParametros(indices[2], tmp);
				forma.setParametros(indices[4], tmp2);
				forma.setParametros(nombresCampo[4], tmp3);
				forma.setParametros(indices[8]+nombresCampo[4], tmp4);
				
			}
		}
		
		//se pregunta si la seccion a cargar es tipo cargos directos
		if (forma.getCodigoSeccion()==ConstantesBD.tipoCargosDirectos)
		{
			
		}
		
		//se pregunta si la seccion a cargar es parametros generales
		if (forma.getCodigoSeccion()==ConstantesBD.parametrosGenerales)
		{
			
		}
		
	}
	
	/**
	 * Metodo encargado de consultar los datos de 
	 * la seccion parametros tipo cargos directos
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void cargarDatosSeccionCargosDirectos (Connection connection,ParametrosEntidadesSubContratadasForm forma,UsuarioBasico usuario)
	{
		HashMap parametros = new HashMap ();
		forma.reset();
		/*-----------------------------------------------------------------------------------------------------
		 * se ingresan los parametros de busqueda de los datos de la tabla det_param_entidades_subContratadas
		 -----------------------------------------------------------------------------------------------------*/
		//codigo_param
		parametros.put(indices[1], forma.getCodigoSeccion()); 
		//institucion
		parametros.put(indices[6], usuario.getCodigoInstitucionInt()); 
		
		/*----------------------------------------------------------------------------------------------------------
		 * fin del ingreso de los parametros de busqueda de los datos de la tabla det_param_entidades_subContratadas
		 -----------------------------------------------------------------------------------------------------------*/
		
		
	}
	
	/**
	 * Metodo encargado de consultar los datos de la BD 
	 * organizando el modo de filtrado dependiendo de
	 * la seccion en la que se encuentre
	 * 
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void organizarCriterios (Connection connection,ParametrosEntidadesSubContratadasForm forma,UsuarioBasico usuario)
	{
		logger.info("\n entre a organizarCriterios -->"+forma.getParametros()+" \n estado --> "+forma.getEstado());
		
		HashMap parametros = new HashMap ();
		resetParametros(forma);
		HashMap datosTmp= new HashMap ();
		//////////////////////////////////////////////////////////////////////////////////////////
		// se almacenan los criterios
		//MODIFICACION POR TAREA 9272
		datosTmp.put(nombresCampo[2], forma.getParametros(nombresCampo[2]));
		datosTmp.put(indices[9], forma.getParametros(indices[9]));
		datosTmp.put(nombresCampo[1], forma.getParametros(nombresCampo[1]));
		datosTmp.put(nombresCampo[0], forma.getParametros(nombresCampo[0]));
		
		/////////////////////////////////////////////////////////////////////////////////////////
		//logger.info("\n los parametros son"+forma.getParametros());
	//	if (forma.getEstado().equals("cargarSubSeccionViaingreso"))
		//	forma.reset();
		
		//codigo_param
		parametros.put(indices[1], forma.getCodigoSeccion()); 
		//institucion
		parametros.put(indices[6], usuario.getCodigoInstitucionInt()); 
		/*----------------------------------------------------------------------------------
		 * se organizan los criterios de busca para poder aplicar los filtros dependiendo de
		 * la seccion donde este y el filtro de cada una de ella.
		 -----------------------------------------------------------------------------------*/
		//se pregunta si la seccion a cargar es tipo informacion ingreso cuenta
		if (forma.getCodigoSeccion()==ConstantesBD.tipoInformacionIngresoCuenta)
		{
			if (forma.getEstado().equals("cargarSubSeccionViaingreso") || forma.getEstadoAnt().equals("cargarSubSeccionViaingreso") ||
				forma.getEstado().equals("cargarValoresSeccion")|| forma.getEstadoAnt().equals("cargarValoresSeccion")||
				forma.getEstado().equals("cargarSubSeccionCentrosAtencion")|| forma.getEstadoAnt().equals("cargarSubSeccionCentrosAtencion"))
			{
				//logger.info("\n entre a aqui ::::::::::::::::11111111111111111::::::::::::::::::");
				//convenio
				parametros.put(indices[2], forma.getParametros(indices[2]));
				//nombre --> acronimo del campo
				parametros.put(indices[5], ConstantesIntegridadDominio.acronimoValidarRequeridoNumeroAutorizacionIngreso);
				
				
				
				parametros.putAll(consultarParametros(connection, parametros));
				
				forma.setParametros(organizarDatos(parametros, forma.getParametros(), forma.getCodigoSeccion()));
				
				if (forma.getEstado().equals("cargarValoresSeccion")|| forma.getEstadoAnt().equals("cargarValoresSeccion") || 
					forma.getEstado().equals("cargarSubSeccionCentrosAtencion")|| forma.getEstadoAnt().equals("cargarSubSeccionCentrosAtencion"))
				{
					//logger.info("\n entre a aqui ::::::::::::::::22222222222222222222::::::::::::::::::");
					// se le remueve el nombre
					parametros.remove(indices[5]);
					//via de ingreso
					parametros.put(indices[4], forma.getParametros(indices[4]));
					
					parametros.putAll(consultarParametros(connection, parametros));
					
					forma.setParametros(organizarDatos(parametros, forma.getParametros(), forma.getCodigoSeccion()));
			
					if (forma.getEstado().equals("cargarSubSeccionCentrosAtencion")|| forma.getEstadoAnt().equals("cargarSubSeccionCentrosAtencion"))
					{
						//logger.info("\n entre a aqui ::::::::::::::::33333333333333333333333::::::::::::::::::");
						//centro_atencion
						parametros.put(indices[9], datosTmp.get(indices[9]));
						
						parametros.putAll(consultarParametros(connection, parametros));
						
					
						
						if (Integer.parseInt(parametros.get("numRegistros")+"")==0)
						{
							forma.getParametros().remove(nombresCampo[3]);
							forma.getParametros().remove(indices[8]+nombresCampo[3]);
							
						}
						forma.setParametros(organizarDatos(parametros, forma.getParametros(), forma.getCodigoSeccion()));
						
						///////////////////////////////////////////////////////////////////////////////////////////////////////////
						//MODIFICACION POR TAREA 9272
						forma.setParametros(nombresCampo[2], datosTmp.get(nombresCampo[2]));
						forma.setParametros(nombresCampo[1], datosTmp.get(nombresCampo[1]));
						forma.setParametros(nombresCampo[0], datosTmp.get(nombresCampo[0]));
						//////////////////////////////////////////////////////////////////////////////////////////////////////////
						
						//logger.info("\n \n el valor de la consulta al final es "+forma.getParametros()+"\n \n");
					}
				
				}
				
				
				
			}
		}
		else
			//se pregunta si la seccion a cargar es tipo cargos directos
			if (forma.getCodigoSeccion()==ConstantesBD.tipoCargosDirectos)
			{
				if (forma.getEstado().equals("cargarSeccion") ||  forma.getEstadoAnt().equals("cargarSeccion") || 
					forma.getEstado().equals("cargarSubSeccionCentrosAtencion") ||  forma.getEstadoAnt().equals("cargarSubSeccionCentrosAtencion"))
				{
					
					parametros.putAll(consultarParametros(connection, parametros));
					forma.setParametros(organizarDatos(parametros, forma.getParametros(), forma.getCodigoSeccion()));
				
					if (forma.getEstado().equals("cargarSubSeccionCentrosAtencion") ||  forma.getEstadoAnt().equals("cargarSubSeccionCentrosAtencion"))
					{
						//centro_atencion
						parametros.put(indices[9], forma.getParametros(indices[9]));
						
						parametros.putAll(consultarParametros(connection, parametros));
						
						if (Integer.parseInt(parametros.get("numRegistros")+"")==0)
						{
							forma.getParametros().remove(nombresCampo[5]);
							forma.getParametros().remove(indices[8]+nombresCampo[5]);
							
						}
						forma.setParametros(organizarDatos(parametros, forma.getParametros(), forma.getCodigoSeccion()));
						
					}
				
				}
				logger.info("\n \n el valor de la consulta al final es "+forma.getParametros()+"\n \n");
				
			}
			else
				//se pregunta si la seccion a cargar es parametros generales
				if (forma.getCodigoSeccion()==ConstantesBD.parametrosGenerales)
				{
					
					parametros.putAll(consultarParametros(connection, parametros));
					forma.setParametros(organizarDatos(parametros, forma.getParametros(), forma.getCodigoSeccion()));
				}
		/*-----------------------------------------------------------------------------------------
		 * fin de organizacion de criterios busqueda
		 ------------------------------------------------------------------------------------------*/
		

		//se saca una copia de los datos del mapa para saber mas adelante si fueron modificados
		  forma.setParametrosClone(Listado.copyMap(forma.getParametros(), nombresCampo));
		
		/*----------------------------------------------------------------
		 * fin de la organizacion del mapa para mostrarlo en la jsp
		 ----------------------------------------------------------------*/
		
		//  logger.info("\n al salir de organizador "+forma.getParametros());
	}
	
	
	public static HashMap organizarDatos (HashMap datos,HashMap result,int codigoSeccion)
	{
		
		
		for (int i=0; i<Integer.parseInt(datos.get("numRegistros")+"");i++)
		{
			for(int j=0;j<nombresCampo.length;j++)
			{	
				if ((datos.get(indices[5]+i)+"").equals(nombresCampo[j]))
				{
					//se carga el valor en el indice del nombre
					result.put(nombresCampo[j], datos.get(indices[3]+i));
					//estaBd
					result.put(indices[8]+nombresCampo[j], datos.get(indices[8]+i));
					
				}
			}
		}
				
		for(int j=0;j<nombresCampo.length;j++)
		{
			if (!result.containsKey(nombresCampo[j]))
			{
				if ((nombresCampo[j].equals(nombresCampo[4]) && codigoSeccion==ConstantesBD.tipoInformacionIngresoCuenta) || (nombresCampo[j].equals(nombresCampo[9]) && codigoSeccion==ConstantesBD.tipoCargosDirectos))
					result.put(nombresCampo[j], ConstantesBD.acronimoNo);
				else
					//se inicializa el parametro
					result.put(nombresCampo[j], "");
				//estaBd
				result.put(indices[8]+nombresCampo[j], ConstantesBD.acronimoNo);
				
			}
		}
		
		return result;
	}
	
	/**
	 * Metodo encargado de filtrar la seccion 
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	public static ActionForward buscarEspecialidadXProfesional (Connection connection,ParametrosEntidadesSubContratadasForm forma,ActionMapping mapping, UsuarioBasico usuario)
	{
		forma.setEspecialidades(Utilidades.obtenerEspecialidadesMedico(connection, forma.getParametros(nombresCampo[7]).toString()));
		return mapping.findForward("principal");
	}
	
	
	
}