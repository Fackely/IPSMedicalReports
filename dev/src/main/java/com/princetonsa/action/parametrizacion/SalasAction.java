/*
 * Sep 05/2005
 */
package com.princetonsa.action.parametrizacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.parametrizacion.SalasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.parametrizacion.Salas;
import com.princetonsa.mundo.parametrizacion.TipoSalas;

/**
 * @author Sebastián Gómez R
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Parametrización de Salas 
 */
public class SalasAction extends Action {
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(SalasAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response ) throws Exception
													{
		Connection con=null;
		try {
			if (response==null); //Para evitar que salga el warning
			if(form instanceof SalasForm)
			{

				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
					return mapping.findForward("paginaError");
				}

				//OBJETOS A USAR
				SalasForm salasForm =(SalasForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=salasForm.getEstado(); 
				logger.warn("Estado en SalasAction  ["+ estado + "]\n\n");

				if(estado == null)
				{
					salasForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Salas (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);				
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,salasForm,mapping,usuario,request, usuario.getCodigoCentroAtencion());
				}
				else if (estado.equals("listarSalas"))
				{
					return accionEmpezar(con,salasForm,mapping,usuario,request, salasForm.getCentroAtencion());
				}
				else if (estado.equals("nuevo"))
				{
					return accionNuevo(con,salasForm,mapping,request);
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con,salasForm,mapping,usuario,request);
				}
				else if(estado.equals("eliminar"))
				{
					return accionEliminar(con,salasForm,mapping,usuario,request);
				}
				else if(estado.equals("empezarDisponibilidadSala"))
				{
					/*
					 * Reseteamos el mensaje y Mostrar Mensaje
					 *//*
				salasForm.setMensaje(new ResultadoBoolean(false));
				salasForm.setMostrarMensajeEliminar(false);*/

					//-Para saber el estado de los registros eliminados de los rangos
					salasForm.setEstadoEliminacion(0);

					String consecutivoSala=salasForm.getCodigoSala();
					String descripcionSala=(String) salasForm.getSalas().get("descripcionSala");
					String descripcionTipoSala=(String) salasForm.getSalas().get("descripcionTipoSala");
					return accionEmpezarDisponibilidadSala(con,salasForm,mapping, consecutivoSala, descripcionSala, descripcionTipoSala, request);
				}
				else if (estado.equals("nuevoRangoDisponibilidad"))
				{
					/*
					 * Reseteamos el mensaje y Mostrar Mensaje
					 */
					salasForm.setMensaje(new ResultadoBoolean(false));
					salasForm.setMostrarMensajeEliminar(false);

					String consecutivoSala=salasForm.getCodigoSala();
					return accionNuevoRangoDisponibilidad(con,salasForm,mapping,request, consecutivoSala);
				}
				else if (estado.equals("guardarRangoDisponibilidad"))
				{ 
					return accionGuardarDisponibilidadSala (con,salasForm,mapping,request);
				}
				else if (estado.equals("eliminarRango"))
				{
					/*
					 * Reseteamos el mensaje
					 *//*
		    	salasForm.setMensaje(new ResultadoBoolean(false));*/
					String consecutivoSala=salasForm.getCodigoSala();
					//String descripcionSala=(String) salasForm.getSalas().get("descripcionSala");
					//String descripcionTipoSala=(String) salasForm.getSalas().get("descripcionTipoSala");

					return accionEliminarRango (con,salasForm,mapping, consecutivoSala, request);
				}

				else
				{
					salasForm.reset();
					logger.warn("Estado no valido dentro del flujo de salas (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);				
					return mapping.findForward("paginaError");
				}
			}			
			return null;	
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}

	/**
	 * Metodo para eliminar un rango de una sala especifica.
	 * @param con
	 * @param salasForm
	 * @param mapping
	 * @param descripcionTipoSala
	 * @param descripcionSala
	 * @param consecutivoSala
	 * @return
	 */
	private ActionForward accionEliminarRango(Connection con, SalasForm salasForm, ActionMapping mapping, String consecutivoSala, HttpServletRequest request) 
	{
		salasForm.setEmpezar(false);
		salasForm.setMostrarMensajeEliminar(true);
		//- Mundo Salas
		Salas salas=new Salas();
		llenarMundo(salasForm, salas, -1);
		salasForm.setEstadoEliminacion(salas.accionEliminarRango(con));
		int estadoEliminar = salasForm.getEstadoEliminacion();
		logger.info("Estado Eliminación: "+estadoEliminar);
		if(estadoEliminar != -2)
		{
			logger.info("===> SE ELIMINÓ CON ÉXITO !!!");
			salasForm.setMensaje(new ResultadoBoolean(true,"SE ELIMINÓ CON ÉXITO !!!"));
		}
		//-Para que retorne a la misma sala con su disponibilidad
		return accionEmpezarDisponibilidadSala(con,salasForm,mapping, consecutivoSala, salasForm.getDescripcionSala(), salasForm.getDescripcionTipoSala(), request);
	}

	/**
	 * Método usado para eliminar una sala
	 * @param con
	 * @param salasForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEliminar(
			Connection con, 
			SalasForm salasForm, 
			ActionMapping mapping, 
			UsuarioBasico usuario, 
			HttpServletRequest request) 
	{
		/*
		 * Inicializamos Mostrar Mensaje
		 */
		salasForm.setMostrarMensajeEliminar(true);
		//nuevo registro que se eliminará
		HashMap registroEliminacion=new HashMap();	
		int resp=0; //variable para almacenar el resultado de la eliminación
		
		//se instancia objeto de Salas
		Salas salas=new Salas();
		//se llena el mundo con los datos del formulario
		this.llenarMundo(salasForm,salas,0);
		//se verifica que sea un registro existente 
		if(salas.getConsecutivo()>0)
		{
			
			//se cargan los datos del registro a eliminar
			registroEliminacion=salas.cargarSala(con);
				
				
			//se realiza la eliminación
			resp=salas.eliminarSala(con);
			//se verifica resultado de la transacción
			if(resp<=0)
			{
				logger.error("No se pudo eliminar el registro");
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en SalasAction", "errors.sinEliminar", true);
			}
			//se inserta LOG
			this.generarLog(con,registroEliminacion,null,ConstantesBD.tipoRegistroLogEliminacion,usuario);
			
		}
		
		//se corren posiciones dentro del mapa para borrar el registro
		for(int i=salasForm.getPos();i<(salasForm.getNumRegistros()-1);i++)
		{
			salasForm.setSalas("consecutivo_"+i,salasForm.getSalas("consecutivo_"+(i+1)));
			salasForm.setSalas("codigo_"+i,salasForm.getSalas("codigo_"+(i+1)));
			salasForm.setSalas("tipo_"+i,salasForm.getSalas("tipo_"+(i+1)));
			salasForm.setSalas("activo_"+i,salasForm.getSalas("activo_"+(i+1)));
			salasForm.setSalas("descripcion_"+i,salasForm.getSalas("descripcion_"+(i+1)));
			salasForm.setSalas("es_usada_"+i,salasForm.getSalas("es_usada_"+(i+1)));
			
		}
		
		//se disminuye el número de registros
		int numRegistros=salasForm.getNumRegistros();
		numRegistros--;
		//se actualiza
		salasForm.setNumRegistros(numRegistros);
		salasForm.setSalas("numRegistros",numRegistros+"");
			
		try
		{
			//salasForm.setMensaje(new ResultadoBoolean(true,"SE HA ELIMINADO CON ÉXITO !!!"));
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		catch(SQLException e)
		{
			logger.error("Error en accionEliminar de SalasAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en SalasAction", "errors.problemasDatos", true);
		}
		
		
	}

	/**
	 * Método usado para ingresar o actualizar registros del listado
	 * de salas en la base de datos
	 * @param con
	 * @param salasForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, SalasForm salasForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) {
		try
		{
			//se inicia transacción
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).beginTransaction(con);
			HashMap registroAntiguo=new HashMap(); //objeto usado para almacenar el registro antes de ser modificado
			boolean exitoActualizacion=true;
			boolean exitoInsercion=true;
			
			String resp="";
			//se instancia objeto Salas
			Salas salas=new Salas();
			//se itera arreglo de registros
			for(int i=0;i<salasForm.getNumRegistros();i++)
			{
				this.llenarMundo(salasForm,salas,i);
				//se verifica que el registro sea nuevo o de modificación
				// -1 => registro nuevo
				if(salas.getConsecutivo()>0)
				{
					
					//obtener registro viejo
					registroAntiguo=salas.cargarSala(con);
					if(fueModificado(registroAntiguo,salas))
					{
						//se actualizan los datos
						resp=salas.actualizarSala(con);
						//se revisa estado actualizacion
						if(Integer.parseInt(resp)<=0)
							exitoActualizacion=false;
						else
							this.generarLog(con,registroAntiguo,salas,ConstantesBD.tipoRegistroLogModificacion,usuario);
						
					}
				}
				//se inserta nuevo registro
				else
				{
					//se insertan los datos
					resp=String.valueOf(salas.insertarSala(con,usuario.getCodigoInstitucionInt()));
					//se revisa estado actualizacion
					if(Integer.parseInt(resp)<=0)
						exitoInsercion=false;
					else
						//se le añade nuevo código al nuevo registro en el arreglo
						salasForm.setSalas("consecutivo_"+i,resp+"");
				}
			}
			
			//verificación de la transacción
			if(exitoActualizacion&&exitoInsercion)
			{
				//éxito!!!!
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);
				//salasForm.setMensaje(new ResultadoBoolean(true,"SE GUARDARON LOS CAMBIOS CON ÉXITO !!!"));
				return accionEmpezar(con,salasForm,mapping,usuario,request, salasForm.getCentroAtencion());
			}
			//el problema fue en la insercion
			else if(!exitoInsercion)
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en SalasAction", "errors.sinIngresar",true);
			}
			// o el problema fue en la actualización
			else
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en SalasAction", "errors.sinActualizar",true);
			}
			
		}
		catch(SQLException e)
		{
			try
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
			}
			catch(SQLException e1)
			{}
			logger.error("Error en accionGuardar de SalasAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en SalasAction", "errors.problemasDatos", true);
		}
	}

	

	/**
	 * Método usado para generar el LOG tipo archivo para
	 * la modificación/eliminación de registros en Salas
	 * @param con
	 * @param registro (registro antiguo)
	 * @param salas (nuevo registro)
	 * @param tipo (tipo de log Modificacion/Eliminacion)
	 * @param usuario
	 */
	private void generarLog(Connection con, HashMap registro, Salas salas, int tipo, UsuarioBasico usuario) {
		String log="";
		//se preparan los datos para obtener la información del tipo de sala
	    TipoSalas tipoSala=new TipoSalas();
	    HashMap datoTipoSala=new HashMap();
	    //***********************************************************
		if(tipo==ConstantesBD.tipoRegistroLogModificacion)
		{
			//se carga la información tipo de Sala original*****************
			tipoSala.setCodigo(Integer.parseInt(registro.get("tipo_0")+""));
			datoTipoSala=tipoSala.cargarTipoSala(con);
			//***************************************************
		    log="\n            ====INFORMACION ORIGINAL DE LA SALA===== " +
			"\n*  Codigo [" +registro.get("codigo_0")+"] "+
			"\n*  Descripción ["+registro.get("descripcion_0")+"] " +
			"\n*  Tipo de Sala ["+datoTipoSala.get("nombre_0")+"] " +
			"\n*  Activa ["+UtilidadTexto.getBoolean(registro.get("activo_0")+"")+"] " +
			"";

		    //se carga la información tipo de Sala después de modificacion*****************
			tipoSala.setCodigo(salas.getTipo());
			datoTipoSala=tipoSala.cargarTipoSala(con);
			//***************************************************
		    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN DE LA SALA===== " +
			"\n*  Código [" +salas.getCodigo()+"] "+
			"\n*  Descripción ["+salas.getDescripcion()+"] " +
			"\n*  Tipo de Sala ["+datoTipoSala.get("nombre_0")+"] " +
			"\n*  Activa ["+salas.isActivo()+"] " +
			""  ;
		}
		else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
		{
			//se carga la información tipo de Sala que se va a eliminar*****************
			tipoSala.setCodigo(Integer.parseInt(registro.get("tipo_0")+""));
			datoTipoSala=tipoSala.cargarTipoSala(con);
			//***************************************************
		    log="\n            ====INFORMACION ELIMINADA DE LA SALA===== " +
			"\n*  Codigo [" +registro.get("codigo_0")+"] "+
			"\n*  Descripción ["+registro.get("descripcion_0")+"] " +
			"\n*  Tipo de Sala ["+datoTipoSala.get("nombre_0")+"] " +
			"\n*  Activa ["+UtilidadTexto.getBoolean(registro.get("activo_0")+"")+"] " +
			""  ;
		}
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logSalasCodigo, log, tipo,usuario.getLoginUsuario());

		
	}

	/**
	 * Método para verificar si el registro fue modificado
	 * @param registro (antiguo)
	 * @param salas (nuevo)
	 * @return
	 */
	private boolean fueModificado(HashMap registro, Salas salas) {
		boolean cambio=false;
		//revision de las descripciones
		if(salas.getDescripcion().compareTo(registro.get("descripcion_0")+"")!=0)
			cambio=true;
		//revision del código de la sala
		if(salas.getCodigo()!=registro.get("codigo_0")+"")
			cambio=true;
		//revision del tipo de sala
		if(salas.getTipo()!=Integer.parseInt(registro.get("tipo_0")+""))
			cambio=true;
		//revision del campo activo
		if(salas.isActivo()!=UtilidadTexto.getBoolean(registro.get("activo_0")+""))
			cambio=true;
		return cambio;
	}
	
	/**
	 * Mètodo para verificar si el registro fue modificado
	 * @param registroAntiguo
	 * @param salas -> Registro nuevo
	 * @param pos
	 * @return true si hay cambio sino retorna false
	 */
	private boolean fueModificadoRango (HashMap registroAntiguo, Salas salas, int pos)
	{
		//Revisión del rango inicial
		if(salas.getRangoInicial().compareTo(registroAntiguo.get("rangoInicial_"+pos)+"") != 0)
			return true;
		
		//Revisión del rango final
		if(salas.getRangoFinal().compareTo(registroAntiguo.get("rangoFinal_"+pos)+"") != 0)
			return true;
				
		return false;
	}

	/**
	 * Método usado para cargar los datos del formulario al mundo de Salas
	 * @param salasForm
	 * @param salas
	 * @param posicion del registro
	 */
	private void llenarMundo(SalasForm salasForm, Salas salas, int pos) 
	{
		if(salasForm.getEstado().equals("guardar"))
		{
			salas.setConsecutivo(Integer.parseInt(salasForm.getSalas("consecutivo_"+pos)+""));
			salas.setCodigo(salasForm.getSalas("codigo_"+pos)+"");
			salas.setDescripcion(salasForm.getSalas("descripcion_"+pos)+"");
			salas.setTipo(Integer.parseInt(salasForm.getSalas("tipo_"+pos).toString().split(ConstantesBD.separadorSplit)[0]));
			salas.setActivo(UtilidadTexto.getBoolean(salasForm.getSalas("activo_"+pos)+""));
			salas.setCentroAtencion( salasForm.getCentroAtencion() );
			salas.setMedico(salasForm.getSalas("medico_"+pos)+"");
		}
		else if(salasForm.getEstado().equals("eliminar"))
		{
			salasForm.setCodigoRegistro(Integer.parseInt(salasForm.getSalas("consecutivo_"+salasForm.getPos())+""));
			salas.setConsecutivo(salasForm.getCodigoRegistro());
		}
		else if(salasForm.getEstado().equals("eliminarRango"))
		{
		
			salas.setCodigo(salasForm.getCodigoSala()); //-Codigo de la Sala 
			
			
			salas.setRangoInicial((String)salasForm.getDisponibilidadSalas().get("RangoIni"));	   //-Rangos a eliminar	
			salas.setRangoFinal((String)salasForm.getDisponibilidadSalas().get("RangoFin"));
		}
		
	}

	/**
	 * Método usado para generar un nuevo registro vacío para el
	 * listado de Salas
	 * @param con
	 * @param salasForm
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionNuevo(Connection con, SalasForm salasForm, ActionMapping mapping, HttpServletRequest request) {
		try
		{
			//se obtiene nueva posicion
			int pos=salasForm.getNumRegistros();
			
			//se crea un nuevo registro
			salasForm.setSalas("consecutivo_"+pos,"-1"); //se agrega un consecutivo -1
			salasForm.setSalas("codigo_"+pos,""); //se agrega código vacío
			salasForm.setSalas("tipo_"+pos,"0"); //sin tipo de sala
			salasForm.setSalas("activo_"+pos,"true"); //se desactiva por defecto
			salasForm.setSalas("descripcion_"+pos,""); //sin descripción
			salasForm.setSalas("quirurgica_"+pos,"false"); //no es quirurgica por defecto
			salasForm.setSalas("descTipoSala_"+pos,""); //sin descripción de tipo sala
			salasForm.setSalas("es_usada_"+pos,"0"); //toda sala nueva está en desuso
			salasForm.setSalas("medico_"+pos,"0"); //toda sala nueva está en desuso
			
			//se aumenta número de registros
			pos++;
			
			//se actualiza tamaño del arreglo en el formulario
			salasForm.setSalas("numRegistros",pos+"");
			salasForm.setNumRegistros(pos);
			
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		catch(SQLException e)
		{
			logger.error("Error en accionNuevo de SalasAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en SalasAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Método donde se realizan los procesos vinculados
	 * con el estado 'empezar'
	 * @param con
	 * @param salasForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param centroAtencion 
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, SalasForm salasForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request, int centroAtencion) {
		try
		{
			salasForm.setEmpezar(true);
			logger.info("Empezar = "+salasForm.getEmpezar());
			//se reinicia información
			String estado=salasForm.getEstado();
			String registrosUsados=salasForm.getRegistrosUsados();
			
			int cc = centroAtencion;
			/**if ( salasForm.getCentroAtencion() != 0 )
				cc = salasForm.getCentroAtencion();  
			else
				cc = usuario.getCodigoCentroAtencion();**/
		
			salasForm.reset();
			salasForm.setCentroAtencion(cc); 
			salasForm.setEstado(estado);
			salasForm.setRegistrosUsados(registrosUsados);
			//****se instancia objeto de Salas ********************
			Salas salas=new Salas();
			//***se cargan los salas actuales **************
			salasForm.setSalas(salas.cargarSalas(con,usuario.getCodigoInstitucionInt(), centroAtencion));
			//***se almacena el número de registros consultados **************
			salasForm.setNumRegistros(Integer.parseInt(salasForm.getSalas("numRegistros")+""));
			salasForm.setInstitucion(usuario.getCodigoInstitucion());
			
			salasForm.setMedicos(Utilidades.obtenerMedicosInstitucion(con, usuario.getCodigoInstitucion()));
			
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		catch(SQLException e)
		{
			logger.error("Error en accionEmpezar de SalasAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en SalasAction", "errors.problemasDatos", true);
		}
	}
	
	/**
	 * Método para hacer el llamado a la página para asignar la disponibilidad de la sala en el estado empezar
	 * @param con -> Conexión
	 * @param salasForm
	 * @param mapping
	 * @param consecutivoSala
	 * @param descripcionSala
	 * @param descripcionTipoSala
	 */
	
	private ActionForward accionEmpezarDisponibilidadSala(
			Connection con, 
			SalasForm salasForm, 
			ActionMapping mapping, 
			String consecutivoSala, 
			String descripcionSala, 
			String descripcionTipoSala,
			HttpServletRequest request)
	{
		logger.info("===> Empezar = "+salasForm.getEmpezar());
		logger.info("===> MostrarMensajeEliminar: "+salasForm.getMostrarMensajeEliminar());
		if(salasForm.getEmpezar()==true)
		{
			salasForm.setMensaje(new ResultadoBoolean(false));
		}/*
		else 
		/*
		 * Definición de los numRegistros para validar si se insertó correctamente o no
		 */
		int 
			numRegistrosAntes=-1, 
			numRegistrosDespues=-1;
		//Se resetea el form
		int cc = salasForm.getCentroAtencion();
		salasForm.reset();
		salasForm.setCentroAtencion(cc);
		
		//-Conservar el estado
		salasForm.setEstado("empezarDisponibilidadSala");
		salasForm.setDescripcionSala(descripcionSala);
		salasForm.setDescripcionTipoSala(descripcionTipoSala);
		//salasForm.setSalas("consecutivoSala_"+consecutivoSala, consecutivoSala+"");
		
		//****Se instancia objeto de Salas ********************
		Salas salas=new Salas();
		
		//***Se carga la disponibilidad de la sala **************
		salasForm.setDisponibilidadSalas(salas.cargarDisponibilidadSala (con, consecutivoSala));
		
		numRegistrosAntes = Integer.parseInt((salasForm.getNumRegDisponibilidad()+""));
		logger.info("numRegistros Antes: "+numRegistrosAntes);
		//***Se almacena el número de registros consultados de la disponibilidad de salas
		salasForm.setNumRegDisponibilidad(Integer.parseInt(salasForm.getDisponibilidadSalas("numRegistros")+""));
		numRegistrosDespues = Integer.parseInt((salasForm.getNumRegDisponibilidad()+""));
		logger.info("numRegistros Después: "+numRegistrosDespues);
		//boolean eliminar = salasForm.getMostrarMensajeEliminar();
		//boolean empezar = salasForm.getEmpezar();
		
		try
		{
			
			/*
			 * Validación del numRegistros para la inserción modificación
			 */
			/*
			if(numRegistrosAntes > numRegistrosDespues)
			{
				if(eliminar == true)
				{
					logger.info("Vamos a quitar el mensaje");
				}
				else
				{
					logger.info("Los Rangos de Hora se cruzan");
					ActionErrors errores = new ActionErrors();
					errores.add("descripcion",new ActionMessage("error.errorEnBlanco","El horario ingresado se cruza con uno ya existente"));
					saveErrors(request, errores);
				}
			}
			else if(numRegistrosAntes == numRegistrosDespues)
			{
				if(empezar == true)
				{
					logger.info("Vamos a quitar el mensaje Proceso Realizado Con Éxito");
				}
				else
				{
					/*
					 * Mensaje para el usuario, confirmando que el proceso se ha realizado con éxito
					 *//*
					logger.info("===> PROCESO REALIZADO CON ÉXITO !!!");
					salasForm.setMensaje(new ResultadoBoolean(true,"PROCESO REALIZADO CON ÉXITO !!!"));*/
				//}
			//}
			UtilidadBD.cerrarConexion(con);
		} 
		catch (SQLException e)
			{
				logger.error("Error en la acción disponibilidad de la sala");
			}
		return mapping.findForward("disponibilidadSala");
	}
	
	/**
	 * Método usado para generar un nuevo registro vacío para el
	 * listado de rangos de disponibilidad de la Sala
	 * @param con
	 * @param salasForm
	 * @param mapping
	 * @param request
	 * @param codigoSala
	 * @return
	 */
	private ActionForward accionNuevoRangoDisponibilidad(Connection con, SalasForm salasForm, ActionMapping mapping, HttpServletRequest request, String codigoSala) 
	{
		try
		{
			//Se obtiene nueva posicion
			int pos=salasForm.getNumRegDisponibilidad();
			
			//Se crea un nuevo registro
			salasForm.setDisponibilidadSalas("consecutivo_"+pos,"-1"); //se agrega un consecutivo -1 para indicar que es un nuevo rango
			salasForm.setDisponibilidadSalas("codigoSala_"+pos, codigoSala+"");
			salasForm.setDisponibilidadSalas("rangoInicial_"+pos,""); //se agrega un rango inicial vacío
			salasForm.setDisponibilidadSalas("rangoFinal_"+pos,""); //se agrega un rango final vacío
			
						
			//se aumenta número de registros
			pos++;
			
			//se actualiza tamaño del arreglo en el formulario
			salasForm.setDisponibilidadSalas("numRegistros",pos+"");
			salasForm.setNumRegDisponibilidad(pos);
			
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("disponibilidadSala");
		}
		catch(SQLException e)
		{
			logger.error("Error en accionNuevoRangoDisponibilidad de SalasAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en SalasAction", "errors.problemasDatos", true);
		}	
	}
	
	/**
	 * Método usado para ingresar o actualizar registros del listado
	 * de rangos de disponibilidad para una sala
	 * @param con
	 * @param salasForm
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarDisponibilidadSala (Connection con, SalasForm salasForm, ActionMapping mapping, HttpServletRequest request) 
	{
		/*
		 * Validaciones para accionGuardarDisponibilidadSala
		 */
		logger.info("===> Entré a accionGuardarDisponibilidadSala");
		ActionErrors errores = new ActionErrors();
		errores = validarDiponibilidadSala(con, salasForm ,errores);
		if(!errores.isEmpty())
		{
			logger.info("===> Hubieron Errores");
			saveErrors(request, errores);
			return mapping.findForward("disponibilidadSala");
		}
		
		try
		{
			//se inicia transacción
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).beginTransaction(con);
			HashMap registroAntiguo=new HashMap(); //objeto usado para almacenar el registro antes de ser modificado
			boolean exitoActualizacion=true;
			boolean exitoInsercion=true;
			String resp="";
			logger.info("\n\n\n empiezaaaaaa !!!!! salasForm.getNumRegDisponibilidad()--> "+salasForm.getNumRegDisponibilidad());
			//Se instancia objeto Salas
			Salas salas=new Salas();
			
			//Se itera el hashMap de registros de los rangos de disponibilidad
			for(int i=0;i<salasForm.getNumRegDisponibilidad();i++)
			{
				this.llenarMundoRangos(salasForm,salas,i);
				//Si el consecutivo es mayor que cero se debe modificar
				if(salas.getConsecutivo()>0)
				{
					//Se el registro viejo
					registroAntiguo=salas.cargarDisponibilidadSala(con, salasForm.getCodigoSala());
					if(fueModificadoRango(registroAntiguo,salas, i))
					{
						logger.info("\n\n\n entre a modificar !!!!!");
							//Se actualiza el registro
						resp=String.valueOf(salas.actualizarDisponibilidadSala (con, registroAntiguo.get("rangoInicial_"+i)+"", registroAntiguo.get("rangoFinal_"+i)+""));
							//se revisa estado actualizacion
							if(Integer.parseInt(resp)<=0)
								exitoActualizacion=false;
					}//if fue modificado
				}
				//se inserta nuevo registro
				else
				{
					logger.info("\n\n\n entre a insertar !!!!!");
					//Se inserta el rango de disponibilidad de la sala
					resp=salas.insertarDisponibilidadSala(con);
					//se revisa estado de la inserción
					if(Integer.parseInt(resp)<=0)
						exitoInsercion=false;
					else
						if(Integer.parseInt(resp)==1)
						{
							logger.info("\n \n \n voy a salir con el error");
							exitoInsercion=false;
							DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
							errores.add("descripcion",new ActionMessage("errors.rangoHoras",salas.getRangoInicial(),salas.getRangoFinal()));
							saveErrors(request, errores);
							return mapping.findForward("disponibilidadSala");
							/*
							
							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en SalasAction", "El Rango de Horas ",false);
							*/
						}
				}//else cuando es un nuevo rango
			}
	
			//Verificación de la transacción
			if(exitoActualizacion&&exitoInsercion)
			{
				//éxito!!!!
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);
				salasForm.setEmpezar(false);
				logger.info("Empezar = "+salasForm.getEmpezar());
				logger.info("===> SE GUARDARON LOS CAMBIOS CON ÉXITO !!!");
				salasForm.setMensaje(new ResultadoBoolean(true,"SE GUARDARON LOS CAMBIOS CON ÉXITO !!!"));
				
				return accionEmpezarDisponibilidadSala
				(
					con,
					salasForm,
					mapping,
					salasForm.getCodigoSala(), 
					salasForm.getDescripcionSala(), 
					salasForm.getDescripcionTipoSala(),
					request
				);
				
			}
			//el problema fue en la insercion
			else if(!exitoInsercion)
			{
				logger.info("NO exito Transaccion");
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en SalasAction", "errors.sinIngresar",true);
			}
			// o el problema fue en la actualización
			else
			{
				logger.info("Problema de Actualización");
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en SalasAction", "errors.sinActualizar",true);
			}
			
		}
		catch(SQLException e)
		{
			try
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
			}
			catch(SQLException e1)
			{}
			logger.error("Error en accionGuardarDisponibilidadSala de SalasAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en SalasAction", "errors.problemasDatos", true);
		}
	}
	
	/**
	 * Método usado para cargar los datos del formulario al mundo de Salas para el ingreso
	 * de los rangos de la disponibilidad
	 * @param salasForm
	 * @param salas
	 * @param posicion del registro
	 */
	private void llenarMundoRangos (SalasForm salasForm, Salas salas, int pos) 
	{
			salas.setConsecutivo(Integer.parseInt(salasForm.getDisponibilidadSalas("consecutivo_"+pos)+""));
			salas.setCodigo(salasForm.getDisponibilidadSalas("codigoSala_"+pos)+"");
			salas.setRangoInicial(salasForm.getDisponibilidadSalas("rangoInicial_"+pos)+"");
			salas.setRangoFinal(salasForm.getDisponibilidadSalas("rangoFinal_"+pos)+"");
	}
	
	private ActionErrors validarDiponibilidadSala(Connection con, SalasForm salasForm, ActionErrors errores)
	{
		logger.info("\n\n\n entre a  validarDiponibilidadSala !!!!!-->"+salasForm.getDisponibilidadSalas());
		/*
		 * rangoInicial_ y rangoFinal_ se definen en las posiciones (numRegistros-1), éste es el ultimo registro agregado
		 */
		int numRegistros = Integer.parseInt(salasForm.getDisponibilidadSalas("numRegistros")+"");
		String 
			rangoInicial= salasForm.getDisponibilidadSalas("rangoInicial_"+(numRegistros-1))+"",
			rangoFinal= salasForm.getDisponibilidadSalas("rangoFinal_"+(numRegistros-1))+"";
		
		logger.info("===> El mapa de disponibilidad es: "+salasForm.getDisponibilidadSalas());
		logger.info("===> Numero de Registros: "+numRegistros);
		logger.info("===> Rango Inicial = "+salasForm.getDisponibilidadSalas("RangoIni"));
		logger.info("===> Rango Final = "+salasForm.getDisponibilidadSalas("RangoFin"));
		//logger.info("===> rangoInicial_"+(numRegistros-1)+" =");
		//logger.info("===> rangoFinal_"+(numRegistros-1)+" = ");
		logger.info("===> rangoInicial_"+(numRegistros-1)+" ="+rangoInicial);
		logger.info("===> rangoFinal_"+(numRegistros-1)+" = "+rangoFinal);
		
		/*
		 * 1)Validacion Rango Inicial: Requerido Rango Inicial
		 */
		if (!UtilidadCadena.noEsVacio(rangoInicial))
		{
			errores.add("descripcion",new ActionMessage("errors.required","Seleccionar El Rango Inicial"));
		}
		
		/*
		 * 2)Validacion Rango Final: Requerido Rango Final
		 */
		if (!UtilidadCadena.noEsVacio(rangoFinal))
		{
			errores.add("descripcion",new ActionMessage("errors.required","Seleccionar El Rango Final"));
		}
		
		
		
		return errores;
	}
}