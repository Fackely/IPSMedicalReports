package com.princetonsa.mundo.glosas;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadValidacion;
import util.Utilidades;

import com.princetonsa.actionform.glosas.ConceptosEspecificosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.ConceptosEspecificosDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Anexo 685
 * Fecha: Septiembre de 2008
 * @author Ing. Felipe Perez Granda
 * @mail lfperez@princetonsa.com
 */

public class ConceptosEspecificos
{

	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	public static Logger logger = Logger.getLogger(ConceptosGenerales.class);
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 *----------------------------------------------*/
	
	//indices 
	private static final String [] indicesConceptos={"codigo_","consecutivo_","descripcion_","activo_","estaBd_","eliminar_"};
	
	
	
	/**
	 * Se inicializa el Dao
	 */
	
	private static ConceptosEspecificosDao conceptosEspecificosDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConceptosEspecificosDao();
	}
	
	
	/**
	 * Metodo encargado de consultar la informacion de
	 * conceptos especificos glosas, en la tabla "conceptos_especificos".
	 * @author Felipe Perez
	 * @param connection
	 * @param criterios
	 * ---------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------
	 * -- institucion --> Requerido
	 * -- action --> Opcional
	 * @return mapa
	 * --------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * --------------------------------
	 * codigo_,consecutivo_,descripcion_,
	 * activo_,estaBd_
	 */
	private static HashMap consultaConceptosEspecificos (Connection connection,HashMap criterios)
	{
		return conceptosEspecificosDao().consultaConceptosEspecificos(connection, criterios);
	}
	
	/**
	 * Metodo encargado de insetar
	 * los datos de conceptos especificos.
	 * @author Felipe Perez
	 * @param connection
	 * @param datos
	 * -----------------------------
	 * KEY'S DEL MAPA DATOS
	 * -----------------------------
	 * -- codigo --> Requerido
	 * -- descripcion --> Requerido
	 * -- activo --> Requerido
	 * -- institucion --> Requerido
	 * @return false/true
	 */
	private static boolean insertarConceptosEspecificos (Connection connection, HashMap datos)
	{
		return conceptosEspecificosDao().insertarConceptosEspecificos(connection, datos);
	}
	
	/**
	 * Método encargado de actualizar la tabla de "conceptos_especificos".
	 * 
	 * 
	 * @author Felipe Pérez
	 * @param connection
	 * @param datos
	 * ------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------
	 * -- codigo		--> requerido
	 * -- consecutivo	--> requerido
	 * -- descripcion	--> requerido
	 * -- activo		--> requerido
	 * -- institucion	--> requerido
	 * -- usuarioModifica -->  Requerido
	 * @return false/true
	 */
	private static boolean actualizaConceptosEspecificos (Connection connection,HashMap datos )
	{
		return conceptosEspecificosDao().actualizaConceptosEspecificos(connection, datos);
	}
	
	/**
	 * Método encargado de eliminar datos de la tabla
	 * conceptos_especificos
	 * @author Felipe Pérez
	 * @param connection
	 * @param datos
	 * ------------------------------------
	 * Keys el mapa de datos
	 * -- codigo 		--> Requerido
	 * -- institucion 	--> Requerido
	 * -----------------------------------
	 * @return true/false
	 */
	private static boolean eliminarConceptosEspecificos(Connection connection, HashMap datos)
	{
		return conceptosEspecificosDao().eliminarConceptosEspecificos(connection, datos);
	}
	
	/**
	 * Metodo encargado de iniciar la funcionalidad,
	 * consulta los valores de las BD.
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void empezar (Connection connection,ConceptosEspecificosForm forma,UsuarioBasico usuario)
	{
		logger.info("\nentre a empezar");
		logger.info("===> mantenerDatos="+forma.getMantenerDatos());
		boolean mantenerLosDatos= forma.getMantenerDatos();
		if(mantenerLosDatos == false)
		{
			//se formatean los valores de la forma
			//pager
			forma.resetpager();
			//los conceptos
			forma.reset();
			
		}
		else
		{
			logger.info("No se van a resetar los valores en empezar");
		}
		
		//se crea un hashmap para almacenar los criterios de busqueda
		HashMap criterios = new HashMap ();
		//institucion
		criterios.put("institucion", usuario.getCodigoInstitucion());
		//se hace la consulta de la base de datos
		forma.setConceptosEspecificos(consultaConceptosEspecificos(connection, criterios));
		
		//se verifica si se pueden eliminar los registros
		int numReg = Utilidades.convertirAEntero(forma.getConceptosEspecificos("numRegistros")+"");
		
		for (int i=0;i<numReg;i++)
		{
			forma.setConceptosEspecificos("eliminar_"+i, UtilidadValidacion.esConceptoEspecificoUsado(
					connection, forma.getConceptosEspecificos("codigo_"+i)+"", usuario.getCodigoInstitucion()));
		}
	}
	
	/**
	 * Metodo encargao de crear nuevos registros
	 * @param forma
	 */
	public static void nuevo (ConceptosEspecificosForm forma)
	{
		forma.setConceptoModificar("codigo_0", "");
		forma.setConceptoModificar("descripcion_0", "");
		forma.setConceptoModificar("activo_0", ConstantesBD.acronimoSi);
		forma.setConceptoModificar("estaBd_0", ConstantesBD.acronimoNo);
		forma.setIndex("0");
		forma.setConceptoModificar("numRegistros", 1);
		/*
		 * Variable mantenerDatos me indica si tengo que mantener los datos en la JSP
		 * true: mantiene los datos (NO se hacen los reset) guardar
		 * false NO mantiene los datos (SI se hace el reset) nuevo
		 * resetMantenerDatos() coloca la variable mantenerDatos en false
		 */
		forma.resetMantenerDatos();
		forma.setModificacion(false);
	}

	
	/**
	 * Metodo encargado de insertar o actualizar conceptos especificos.
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static ActionErrors guardar (Connection connection,ConceptosEspecificosForm forma,UsuarioBasico usuario)
	{
		/*
		 * La variable redirección me indica que forward debe de tener tomar el metodo guardar
		 * true: volver a la misma pagina para modificar
		 * false: volver a la pagina de nuevo
		 */
		forma.setRedireccion(false);
		
		/*
		 * Variable mantenerDatos me indica si tengo que mantener los datos en la JSP
		 * true: mantiene los datos (NO se hacen los reset) guardar
		 * false NO mantiene los datos (SI se hace el reset) nuevo
		 */
		forma.setMantenerDatos(true);
		
		/*
		 * Variable esModificacion me indica se se hizo una modificación o una inserción
		 * true: Modificación
		 * false: Inserción
		 */
		boolean esModificacion = false;
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		ActionErrors errores = new ActionErrors();
		logger.info("----->INICIANDO TRANSACCION ....");
	
		/*
		 * Modificar			
		 */
		if((forma.getConceptoModificar("estaBd_0")+"").trim().equals(ConstantesBD.acronimoSi))
		{
			esModificacion = true;
			if (esModificado(forma))
			{
				/*
				 * Cuando la descripción viene vacío o viene NULL 
				 */
				if(forma.getConceptoModificar("descripcion_0").equals(""))
				{
					/*
					logger.info("===> La descripción viene vacía, se sacará un error");
					errores.add("descripcion_0", new ActionMessage("errors.required", "La descripción no puede estar vacía, "));
					*/
					forma.setRedireccion(true);
					forma.setModificacion(true);
				}
				else
				{
					HashMap datos = new HashMap ();
					datos.put("codigo", forma.getConceptoModificar("codigo_0"));
					datos.put("descripcion", forma.getConceptoModificar("descripcion_0"));
					datos.put("activo", forma.getConceptoModificar("activo_0"));
					datos.put("institucion", usuario.getCodigoInstitucion());
					datos.put("usuarioModifica", usuario.getLoginUsuario());
					transacction=actualizaConceptosEspecificos(connection, datos);
				}
			}
		}
		
		/*
		 * Insertar
		 */
		else if((forma.getConceptoModificar("estaBd_0")+"").trim().equals(ConstantesBD.acronimoNo))
		{
			esModificacion = false;
			logger.info("===> Insertar: Vamos a imprimir los valores a agregar a la BD");
			logger.info("===> Insertar: codigo: "+forma.getConceptoModificar("codigo_0"));
			logger.info("===> Insertar: descripcion: "+forma.getConceptoModificar("descripcion_0"));
			logger.info("===> Insertar: activo: "+forma.getConceptoModificar("activo_0"));
			logger.info("===> Insertar: institucion: "+usuario.getCodigoInstitucion());
			
			/*
			 * Cuando el código viene vacío
			 */
			if(forma.getConceptoModificar("codigo_0").equals(""))
			{
				logger.info("===> El código viene vacío, se sacará un error");
				forma.setRedireccion(true);
			}
			
			else
			{
				try
				{
					Integer.parseInt(forma.getConceptoModificar("codigo_0")+"");
					logger.info("===> El código Si era numero");
				}
				catch (Exception e) 
				{
					/*
					 * Cuando el codigo ha sido pegado con cntl v y es un caracter
					 */
					logger.info("===> El código No era numero");
					forma.setRedireccion(true);
				}
			}
			
			/*
			 * Cuando la descripción viene vacío 
			 */
			if(forma.getConceptoModificar("descripcion_0").equals(""))
			{
				logger.info("===> La descripción viene vacía, se sacará un error");
				forma.setRedireccion(true);
			}
			
			/*
			 * Hacemos la transacción para insertar los datos
			 */
			HashMap datos = new HashMap ();
			datos.put("codigo", forma.getConceptoModificar("codigo_0"));
			datos.put("descripcion", forma.getConceptoModificar("descripcion_0"));
			datos.put("activo", forma.getConceptoModificar("activo_0"));
			datos.put("institucion", usuario.getCodigoInstitucion());
			datos.put("usuarioModifica", usuario.getLoginUsuario());
			
			if(forma.getRedireccion()==false)
			{
				logger.info("===> Vamos a empezar con la transacción");
				transacction=insertarConceptosEspecificos(connection, datos);
			}
			else
			{
				logger.info("===> La Transacción NO se realizará");
			}
			
		}
		if(transacction)
		{
			/*
			 * Insertar
			 */
			
			/*
			 * Cuando el código viene vacío
			 */
			if(forma.getConceptoModificar("codigo_0").equals(""))
			{
				logger.info("===> El código viene vacío, se sacará un error");
				errores.add("codigo_0", new ActionMessage("errors.required", "El código no puede estar vacío, "));
				forma.setRedireccion(true);
			}
			else
			{
				try
				{
					if(Integer.parseInt(forma.getConceptoModificar("codigo_0")+"")<=0)
					{
						errores.add("codigo_0", new ActionMessage
								("error.errorEnBlanco", "El código debe de ser mayor a cero, "));
						forma.setRedireccion(true);
					}
					logger.info("===> El código Si era numero");
				}
				catch (Exception e) 
				{
					/*
					 * Cuando el codigo ha sido pegado con cntl v y es un caracter
					 */
					logger.info("===> El código No era numero");
					errores.add("codigo_0", new ActionMessage
							("error.errorEnBlanco", "El código era un caracter, debe de ser numerico, "));
					forma.setRedireccion(true);
				}
			}
			
			/*
			 * Cuando la descripción viene vacío 
			 */
			if(forma.getConceptoModificar("descripcion_0").equals(""))
			{
				logger.info("===> La descripción viene vacía, se sacará un error");
				errores.add("descripcion_0", new ActionMessage("errors.required", "La descripción no puede estar vacía, "));
				forma.setRedireccion(true);
			}
			
			if(errores.isEmpty())
				UtilidadBD.finalizarTransaccion(connection);
			else
				UtilidadBD.abortarTransaccion(connection);
			
			if(!forma.getConceptoModificar("codigo_0").equals(""))
			{
				if(!forma.getConceptoModificar("descripcion_0").equals(""))
				{
					if(forma.getRedireccion()==false)
					{
						logger.info("===> Entré a la operación realizada con éxito");
						forma.setMensaje(new ResultadoBoolean(true,"Operacion Realizada Con Exito!"));
						logger.info("----->TRANSACCION AL 100% ....");
						/*
						 * Con ésta validación, garantizamos que se trate de una modificación Y NO de una inserción
						 */
						if(esModificacion == true)
						{
							String [] indices ={"activo_","codigo_","descripcion_",""};
							/*
							 * Generación del log tipo archivo al Modificar
							 */
							logger.info("===> Voy a generar log al modificar");
							Utilidades.generarLogGenerico(
									forma.getConceptoModificar(), 
									forma.getConceptoModificarClone(), 
									usuario.getLoginUsuario(), 
									false, 
									0, 
									ConstantesBD.logConceptosEspecificosGlosasCodigo, 
									indices);
						}
					}
				}
			}
		}
		else
		{
			if(!forma.getConceptoModificar("codigo_0").equals(""))
				if(!forma.getConceptoModificar("descripcion_0").equals(""))
				{
					errores.add("codigo_0", new ActionMessage
							("error.errorEnBlanco", "EL CÓDIGO "+(forma.getConceptoModificar("codigo_0"))+
									" YA SE ENCUENTRA ASIGNADO, POR FAVOR VERIFIQUE  "));
					forma.setRedireccion(true);
				}
			
			UtilidadBD.abortarTransaccion(connection);
		}
		logger.info("===> Voy a imprimir Redireccion: "+forma.getRedireccion());
		//se vuelve a consultar los datos
		empezar(connection, forma, usuario);
		return errores;
	}
	
	/**
	 * Metodo encargado de eliminar un registro de la BD y
	 * crear log tipo archivo de la eliminacion.
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void eliminar (Connection connection, ConceptosEspecificosForm forma,UsuarioBasico usuario)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		logger.info("----->INICIANDO TRANSACCION ....");
		
		HashMap datos = new HashMap ();
		datos.put("codigo", forma.getConceptoModificar("codigo_0"));
		datos.put("institucion", usuario.getCodigoInstitucion());
		//se elimina en la bd
		//////////////////////////////////////////////////////////////////////
		transacction=eliminarConceptosEspecificos(connection, datos);
		//////////////////////////////////////////////////////////////////////
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);
			forma.setMensaje(new ResultadoBoolean(true,"Operacion Realizada Con Exito!"));
			String [] indices ={"activo_","codigo_","descripcion_",""};
			/*
			 * Generación del log tipo archivo al Eliminar
			 */
			logger.info("===> Voy a generar log al eliminar");
			Utilidades.generarLogGenerico(
					forma.getConceptoModificar(), 
					forma.getConceptoModificarClone(), 
					usuario.getLoginUsuario(), 
					true, 
					0, 
					ConstantesBD.logConceptosEspecificosGlosasCodigo, 
					indices);
			logger.info("----->TRANSACCION AL 100% ....");
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
			forma.setMensaje(new ResultadoBoolean(true,"La Operacion No Pudo Ser Realizada Con Exito!"));
		}
		//se vuelve a consultar los datos
		empezar(connection, forma, usuario);	
	}
	
	/**
	 * Metodo encargado de identificar si hubo modificacion
	 * @param forma
	 * @return
	 */
	private static boolean  esModificado(ConceptosEspecificosForm forma)
	{
		if (!(forma.getConceptoModificar("codigo_0")+"").equals(forma.getConceptoModificarClone("codigo_0")+"") ||
			!(forma.getConceptoModificar("descripcion_0")+"").equals(forma.getConceptoModificarClone("descripcion_0")+"") || 
			!(forma.getConceptoModificar("activo_0")+"").equals(forma.getConceptoModificarClone("activo_0")+""))
			return true;
		
		
		return false;
	}
	
	/**
	 * Metodo encargado de cargar un registro para ser modificado.
	 * @param forma
	 */
	public static void cargar (ConceptosEspecificosForm forma)
	{
		logger.info("\n entre a cargar con el index --> "+forma.getIndex());
		//se resetean los valores previos
		forma.resetModicacion();
		//se carga el cocepto a ser modificado
		forma.setConceptoModificar(Listado.copyOnIndexMap(forma.getConceptosEspecificos(), forma.getIndex(), indicesConceptos));
		//se saca una copia del concepto a ser modificado para luego compararlo, e identificar si es modificacion o insercion
		forma.setConceptoModificarClone((HashMap)forma.getConceptoModificar().clone());
	}
	
	/**
	 * Se encarga del ordenamiento almacenando los criteros
	 * de busqueda.
	 * 
	 * @param forma
	 */
	public static void ordenar (ConceptosEspecificosForm forma)
	{
		int numReg = Integer.parseInt(forma.getConceptosEspecificos("numRegistros")+"");

		forma.setConceptosEspecificos(Listado.ordenarMapa(indicesConceptos, 
				forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getConceptosEspecificos(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setConceptosEspecificos("numRegistros", numReg);
		
	}
}