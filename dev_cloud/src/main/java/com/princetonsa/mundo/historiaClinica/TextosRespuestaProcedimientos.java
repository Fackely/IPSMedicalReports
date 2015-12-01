package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadValidacion;
import util.Utilidades;

import com.princetonsa.actionform.historiaClinica.TextosRespuestaProcedimientosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.TextosRespuestaProcedimientosDao;
import com.princetonsa.mundo.UsuarioBasico;


/**
 * Anexo 714
 * Creado el 17 de Septiembre de 2008
 * @author Ing. Felipe Perez Granda
 * @mail lfperez@princetonsa.com
 */

public class TextosRespuestaProcedimientos
{

	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	public static Logger logger = Logger.getLogger(TextosRespuestaProcedimientos.class);
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 *----------------------------------------------*/
	
	//indices 
	private static final String [] indicesConceptos = {"servicio_", "codigocupsservicio_", "nombrecupsservicio_"};
	
	//indices  
	private static final String [] indicesTextosRespuesta = {"codigo_", "servicio_", "descripciontexto_", "textopredeterminado_", "activo_", 
		"fechamodifica_"};

	/**
	 * Se inicializa el Dao
	 */
	
	private static TextosRespuestaProcedimientosDao TextosRespuestaProcedimientosDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTextosRespuestaProcedimientosDao();
	}

	/**
	 * Metodo encargado de consultar la informacion de
	 * TextosRespuestaProcedimientos - Historia Clinica, en la tabla "textos_resp_proc".
	 * @author Ing. Felipe Perez Granda
	 * @param connection
	 * @param criterios
	 * ---------------------------------
	 * ---------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------
	 * -- institucion 	--> Requerido
	 * -- action 		--> Opcional
	 * @return mapa
	 * --------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * --------------------------------
	 * codigo_,
	 * servicio_,
	 * descripcionTexto_,
	 * textoPredeterminado_,
	 * activo_,
	 * estaBd_
	 */
	public static HashMap consultaTextosRespuestaProcedimientos (Connection connection, int institucion)
	{
		return TextosRespuestaProcedimientosDao().consultaTextosRespuestaProcedimientos(connection, institucion);
	}
	
	/**
	 * Metodo encargado de insertar
	 * los datos de Textos Respuesta Procedimientos.
	 * @author Ing. Felipe Perez Gaanda
	 * @param connection
	 * @param datos
	 * -----------------------------
	 * KEY'S DEL MAPA DATOS
	 * -----------------------------
	 * -- codigo 				--> Requerido
	 * -- descripcionTexto	 	--> Requerido
	 * -- textoPredeterminado 	--> Requerido
	 * -- activo				--> Requerido
	 * -- institucion 			--> Requerido
	 * @return false/true
	 */
	private static boolean insertarTextosRespuestaProcedimientos (Connection connection, HashMap datos)
	{
		return TextosRespuestaProcedimientosDao().insertarTextosRespuestaProcedimientos(connection, datos);
	}
	
	/**
	 * Método encargado de cargar los textos respuesta procedimientos
	 * @param connection
	 * @param datos
	 * @return
	 */
	public static HashMap cargarTextosRespuestaProcedimientos (Connection connection, int codigoServicio, int institucion)
	{
		return TextosRespuestaProcedimientosDao().cargarTextosRespuestaProcedimientos(connection, codigoServicio, institucion);
	}
	
	/**
	 * Método encargado de actualizar la tabla de "textos_resp_proc".
	 * @author Ing. Felipe Pérez Granda
	 * @param connection
	 * @param datos
	 * ------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------
	 * -- codigo				--> Requerido
	 * -- servicio				--> Requerido
	 * -- descripcionTexto		--> Requerido
	 * -- textoPredetermiando 	--> Requerido
	 * -- activo				--> Requerido
	 * -- institucion			--> Requerido
	 * -- usuarioModifica		--> Requerido
	 * @return false/true
	 */
	private static boolean actualizaTextosRespuestaProcedimientos (Connection connection, HashMap datos )
	{
		return TextosRespuestaProcedimientosDao().actualizaTextosRespuestaProcedimientos(connection, datos);
	}
	
	/**
	 * Método encargado de eliminar datos de la tabla
	 * textos_resp_proc
	 * @author Ing. Felipe Pérez Granda
	 * @param connection
	 * @param datos
	 * ------------------------------------
	 * Keys el mapa de datos
	 * -- codigo		--> Requerido
	 * -- institucion 	--> Requerido
	 * -----------------------------------
	 * @return true/false
	 */
	private static boolean eliminarTextosRespuestaProcedimientos (Connection connection, int codigoServicio, int institucion)
	{
		return TextosRespuestaProcedimientosDao().eliminarTextosRespuestaProcedimientos(connection, codigoServicio, institucion);
	}
	
	/**
	 * Método encargado de eliminar un texto procedimiento
	 * @param connection
	 * @param codServicio
	 * @param codigoInstitucionInt
	 * @return
	 */
	private boolean eliminarTextoProcedimiento(Connection connection, int codigo, int codigoInstitucionInt)
	{
		return TextosRespuestaProcedimientosDao().eliminarTextoProcedimiento(connection, codigo, codigoInstitucionInt);
	}
	
	/**
	 * Metodo encargado de consultar mediante el servicio y la institución
	 * los códigos asociados a ese servicio en la tabla "textos_resp_proc".
	 * @author Ing. Felipe Perez Granda
	 * @param servicio
	 * @param institucion
	 * ---------------------------------
	 * ---------------------------------
	 * KEY'S DEL MAPA 
	 * ---------------------------------
	 * -- codigo
	 * @return mapa
	 */
	
	public static HashMap consultarCodigosTextos (Connection connection, String servicio, int institucion)
	{
		return TextosRespuestaProcedimientosDao().consultarCodigosTextos(connection, servicio, institucion);
	}
	
	/**
	 * Metodo encargado de iniciar la funcionalidad,
	 * consulta los valores de las BD.
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void empezar (Connection connection, TextosRespuestaProcedimientosForm forma, UsuarioBasico usuario)
	{
		forma.resetpager();
		forma.reset();
		forma.resetMensaje();
		forma.setTextosRespuestaProcedimientos(consultaTextosRespuestaProcedimientos(connection, usuario.getCodigoInstitucionInt()));
		HashMap<String, Object> mapaTemp = new HashMap<String, Object>();
		boolean esTextoUsado = false;
		int numRegistrosTemp;
		for(int i=0; i<Utilidades.convertirAEntero(forma.getTextosRespuestaProcedimientos("numRegistros")+""); i++)
		{
			mapaTemp = consultarCodigosTextos(connection, forma.getTextosRespuestaProcedimientos("servicio_"+i)+"", 
					usuario.getCodigoInstitucionInt());
			numRegistrosTemp = Utilidades.convertirAEntero(mapaTemp.get("numRegistros")+""); 
			for(int j=0; j<numRegistrosTemp; j++)
			{
				logger.info("===>Codigo: "+mapaTemp.get("codigo_"+j));
				esTextoUsado = UtilidadValidacion.esTextosRespuestaProcedimientosUsado(connection, mapaTemp.get("codigo_"+j)+"", 
						usuario.getCodigoInstitucion());
				logger.info("===>Validacion: "+esTextoUsado);
				forma.setTextosRespuestaProcedimientos("eliminar_"+i, esTextoUsado);
				if(esTextoUsado)
				{
					j = numRegistrosTemp;
				}
					
			}
		}
	}
		
	/**
	 * Metodo encargado de insertar o actualizar Textos Respuesta Procedimientos.
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void guardarInsertar (Connection connection,TextosRespuestaProcedimientosForm forma,UsuarioBasico usuario, int bandera)
	{
		HashMap<String, Object> criterios = new HashMap<String, Object>();
		//Validamo si el insertar es desde cero o el insertar es desde un servicio ya insertado
		if(forma.getEstado().equals("guardarInsertar"))
		{
			logger.info("===>Guardar Insertar...");
			logger.info("===>Los Criterios son"+criterios);
			logger.info("===>La bandera viene en... ="+bandera);
			
			if(bandera == 1)
			{
				logger.info("===> Método guardarInsertar La bandera viene en 1 !!!\n el flujo de la secuencia debe de ser... " +
						"nuevoTextoProcedimientos > guardarInsertar");
				logger.info("===> guardarInsertar Codigo Servicio: "+forma.getCodigoServicio());
				criterios.put("codigoServicio", forma.getCodigoServicio());
			}
			if(bandera == 2)
			{
				logger.info("===> Método guardarInsertar La bandera viene en 2 !!!\n el flujo de la secuencia debe de ser... " +
						"guardarInsertarModificacion > guardarInsertar");
				logger.info("===> Método guardarInsertar Codigo Servicio: "+forma.getTextosRespuestaProcedimientos("servicio_"+forma.getIndex()));
				criterios.put("codigoServicio", forma.getTextosRespuestaProcedimientos("servicio_"+forma.getIndex()));
			}
		}
		else if(forma.getEstado().equals("guardarInsertarModificacion"))
		{
			logger.info("===>Guardar Insertar Modificacion...");
			logger.info("Mira Aquí Pipe !!! ===>Codigo Servicio Seleccionado: "+forma.getTextosRespuestaProcedimientos("servicio_"+
					forma.getIndex()));
			criterios.put("codigoServicio", forma.getTextosRespuestaProcedimientos("servicio_"+forma.getIndex()));
		}
		
		criterios.put("descripcionTexto", forma.getDescripcionTexto());
		criterios.put("textoPredeterminado", forma.getTextoPredeterminado());
		criterios.put("activo", forma.getActivo());
		criterios.put("usuario", usuario.getLoginUsuario());
		criterios.put("institucion", usuario.getCodigoInstitucion());
		UtilidadBD.iniciarTransaccion(connection);
		boolean textoMenorA4000 = forma.getTextoMenorA4000();
		logger.info("===> textoMenorA4000="+textoMenorA4000);
		if(textoMenorA4000==true)
		{
			logger.info("===> Entré a hacer la transacción !!!");
			boolean transaccion = insertarTextosRespuestaProcedimientos(connection, criterios);
			if(transaccion)
			{
				forma.setMensaje(new ResultadoBoolean(true,"PROCESO REALIZADO CON ÉXITO !!!"));
				UtilidadBD.finalizarTransaccion(connection);
			}
			else
			{
				forma.setMensaje(new ResultadoBoolean(true,
						"SE PRESENTARÓN INCONVENIENTES EN EL ALMACENAMIENTO DE TEXTOS RESPUESTA PROECEDIMIENTOS"));
				UtilidadBD.abortarTransaccion(connection);
			}
		}
	}
	
	/**
	 * Metodo encargado de insertar o actualizar Textos Respuesta Procedimientos.
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static void guardarModificar (Connection connection,TextosRespuestaProcedimientosForm forma,UsuarioBasico usuario)
	{
		HashMap<String, Object> criterios = new HashMap<String, Object>();
		criterios.put("codigo", forma.getTextosProcedimientosServicio("codigo_"+forma.getPosicion()));
		criterios.put("codigoServicio", forma.getTextosProcedimientosServicio("servicio_"+forma.getPosicion()));
		criterios.put("descripcionTexto", forma.getTextosProcedimientosServicio("descripciontexto_"+forma.getPosicion()));
		criterios.put("textoPredeterminado", forma.getTextosProcedimientosServicio("textopredeterminado_"+forma.getPosicion()));
		criterios.put("activo", forma.getTextosProcedimientosServicio("activo_"+forma.getPosicion()));
		criterios.put("usuario", usuario.getLoginUsuario());
		criterios.put("institucion", usuario.getCodigoInstitucion());
		UtilidadBD.iniciarTransaccion(connection);
		
		boolean textoMenorA4000 = forma.getTextoMenorA4000();
		logger.info("===> textoMenorA4000="+textoMenorA4000);
		if(textoMenorA4000==true)
		{
			logger.info("===> Entré a hacer la transacción !!!");
			//boolean transaccion = insertarTextosRespuestaProcedimientos(connection, criterios);
			boolean transaccion = actualizaTextosRespuestaProcedimientos(connection, criterios);
			if(transaccion)
			{
				forma.setMensaje(new ResultadoBoolean(true,"PROCESO REALIZADO CON ÉXITO !!!"));
				UtilidadBD.finalizarTransaccion(connection);
			}
			else
			{
				forma.setMensaje(new ResultadoBoolean(true,
						"SE PRESENTARÓN INCONVENIENTES EN EL ALMACENAMIENTO DE TEXTOS RESPUESTA PROECEDIMIENTOS"));
				UtilidadBD.abortarTransaccion(connection);
			}
		}
	}
	
	/**
	 * Metodo encargado de eliminar un registro de la BD
	 * @param connection
	 * @param forma
	 * @param usuario
	 */
	public static ActionErrors eliminar (Connection connection, TextosRespuestaProcedimientosForm forma, UsuarioBasico usuario)
	{
		ActionErrors errores = new ActionErrors();
		boolean enTransaccion = false;
		logger.info("===>Posición Seleccionada: "+forma.getIndex());
		int codServicio = Utilidades.convertirAEntero(forma.getTextosRespuestaProcedimientos("servicio_"+forma.getIndex())+"");
		
		UtilidadBD.iniciarTransaccion(connection);
		
		//Cargamos los textos de respuesta de procedimientos asociados al servicio seleccionado
		cargar(connection, forma, usuario.getCodigoInstitucionInt());
		
		int numRegistros = Utilidades.convertirAEntero(forma.getTextosProcedimientosServicio("numRegistros")+"");
		
		//Recorremos todos los textos de respuesta de procedientos asociados al servicio con la intención de verificar
		//si al menos uno esta siendo utilizado en otra funcionalidad
		for(int i =0; i<numRegistros; i++)
		{
			//Validamos uno por uno el codigo de Textos de Respuesta de Procedimiento para verificar si esta siendo utilizado
			if(UtilidadValidacion.esTextosRespuestaProcedimientosUsado(connection, forma.getTextosProcedimientosServicio("codigo_"+i)+"", 
					usuario.getCodigoInstitucion()))
			{
				errores.add("consecutivoConcepto", new ActionMessage("error.historiaClinica.textosServiciosNoBorrable", 
						"Textos de Respuesta de Procedimientos"));
				UtilidadBD.abortarTransaccion(connection);
				i = numRegistros;
			}
		}
		
		//Validamos si vienen errores para con ello no eliminar el registro de la base de datos
		if(errores.isEmpty())
		{
			enTransaccion = eliminarTextosRespuestaProcedimientos(connection, codServicio, usuario.getCodigoInstitucionInt());
			if(enTransaccion)
			{
				forma.setMensaje(new ResultadoBoolean(true,"PROCESO REALIZADO CON ÉXITO !!!"));
				UtilidadBD.finalizarTransaccion(connection);
			}
			else
			{
				forma.setMensaje(new ResultadoBoolean(true,
						"SE PRESENTARÓN INCONVENIENTES EN LA ELIMINACIÓN DE TEXTOS RESPUESTA PROECEDIMIENTOS"));
				UtilidadBD.abortarTransaccion(connection);
			}
		}
		
		return errores;
		
	}
	
	/**
	 * Metodo encargado de cargar un registro para ser modificado.
	 * @param connection 
	 * @param forma
	 * @param institucion
	 */
	public static void cargar (Connection connection, TextosRespuestaProcedimientosForm forma, int institucion)
	{
		logger.info("===>Posición Seleccionada: "+forma.getIndex());
		int codServicio = Utilidades.convertirAEntero(forma.getTextosRespuestaProcedimientos("servicio_"+forma.getIndex())+"");
		//Llenamos el HashMap con los procedimientos de respuesta del servicio seleccionado
		forma.setTextosProcedimientosServicio(cargarTextosRespuestaProcedimientos(connection, codServicio, institucion));
	}
	
	/**
	 * Se encarga del ordenamiento almacenando los criteros
	 * de busqueda.
	 * @param forma
	 */
	public static void ordenar(Connection connection, TextosRespuestaProcedimientosForm forma, UsuarioBasico usuario)
	{
		int numReg = Integer.parseInt(forma.getTextosRespuestaProcedimientos("numRegistros")+"");
		forma.setTextosRespuestaProcedimientos(Listado.ordenarMapa(indicesConceptos, forma.getPatronOrdenar(), forma.getUltimoPatron(), 
				forma.getTextosRespuestaProcedimientos(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setTextosRespuestaProcedimientos("numRegistros", numReg);
		
		/*
		 * Mostrar Ocultar la imagen para eliminar
		 */
		HashMap<String, Object> mapaTemp = new HashMap<String, Object>();
		boolean esTextoUsado = false;
		int numRegistrosTemp;
		for(int i=0; i<Utilidades.convertirAEntero(forma.getTextosRespuestaProcedimientos("numRegistros")+""); i++)
		{
			mapaTemp = consultarCodigosTextos(connection, forma.getTextosRespuestaProcedimientos("servicio_"+i)+"", 
					usuario.getCodigoInstitucionInt());
			numRegistrosTemp = Utilidades.convertirAEntero(mapaTemp.get("numRegistros")+""); 
			for(int j=0; j<numRegistrosTemp; j++)
			{
				logger.info("===>Codigo: "+mapaTemp.get("codigo_"+j));
				esTextoUsado = UtilidadValidacion.esTextosRespuestaProcedimientosUsado(connection, mapaTemp.get("codigo_"+j)+"", 
						usuario.getCodigoInstitucion());
				logger.info("===>Validacion: "+esTextoUsado);
				forma.setTextosRespuestaProcedimientos("eliminar_"+i, esTextoUsado);
				if(esTextoUsado)
				{
					j = numRegistrosTemp;
				}
					
			}
		}
	}
	
	/**
	 * Se encarga del ordenamiento almacenando los criteros
	 * de busqueda.
	 * @param forma
	 */
	public static void ordenarTextos(TextosRespuestaProcedimientosForm forma)
	{
		int numReg = Integer.parseInt(forma.getTextosProcedimientosServicio("numRegistros")+"");
		forma.setTextosProcedimientosServicio(Listado.ordenarMapa(indicesTextosRespuesta, forma.getPatronOrdenar(), 
				forma.getUltimoPatron(), forma.getTextosProcedimientosServicio(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setTextosProcedimientosServicio("numRegistros", numReg);
	}

	/**
	 * Método encargado de eliminar un texto
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @return
	 */
	public ActionErrors eliminarTexto(Connection connection, TextosRespuestaProcedimientosForm forma, UsuarioBasico usuario)
	{
		ActionErrors errores = new ActionErrors();
		boolean enTransaccion = false;
		logger.info("===>Posición Seleccionada: "+forma.getPosicion());
		int codServicio = Utilidades.convertirAEntero(forma.getTextosProcedimientosServicio("codigo_"+forma.getPosicion())+"");
		UtilidadBD.iniciarTransaccion(connection);
		
		//Validamos si el codigo de texto de procedimiento a eliminar esta siendo usado en otra parte
		if(UtilidadValidacion.esTextosRespuestaProcedimientosUsado(connection, codServicio+"", usuario.getCodigoInstitucion()))
		{
			errores.add("consecutivoConcepto", new ActionMessage("error.historiaClinica.textosRespuestaNoBorrable", 
					"Texto de Respuesta de Procedimiento"));
			UtilidadBD.abortarTransaccion(connection);
		}
		
		//Validamos si vienen errores para con ello no eliminar el registro de la base de datos
		if(errores.isEmpty())
		{
			enTransaccion = eliminarTextoProcedimiento(connection, codServicio, usuario.getCodigoInstitucionInt());
			if(enTransaccion)
			{
				forma.setMensaje(new ResultadoBoolean(true, "PROCESO REALIZADO CON ÉXITO !!!"));
				UtilidadBD.finalizarTransaccion(connection);
			}
			else
			{
				forma.setMensaje(new ResultadoBoolean(true, 
						"SE PRESENTARÓN INCONVENIENTES EN LA ELIMINACIÓN DE TEXTOS RESPUESTA PROECEDIMIENTOS"));
				UtilidadBD.abortarTransaccion(connection);
			}
		}
		
		return errores;
	}

}