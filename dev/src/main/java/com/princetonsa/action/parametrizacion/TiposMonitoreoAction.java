/*
 * Created on Aug 29, 2005
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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.parametrizacion.TiposMonitoreoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.parametrizacion.TiposMonitoreo;

/**
 * @author Sebastián Gómez
 *
 * Action usado para controlar los procesos de parametrización de los
 * tipos de monitoreo
 */
public class TiposMonitoreoAction extends Action {
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(TiposMonitoreoAction.class);
	
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
			if(form instanceof TiposMonitoreoForm)
			{
				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				//OBJETOS A USAR
				TiposMonitoreoForm tiposMonitoreoForm =(TiposMonitoreoForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=tiposMonitoreoForm.getEstado();
				tiposMonitoreoForm.setMensaje(new ResultadoBoolean(false));
				logger.warn("estado Tipos Monitoreo-->"+estado);


				if(estado == null)
				{
					tiposMonitoreoForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Tipos Monitoreo (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,tiposMonitoreoForm,mapping,usuario,request);
				}
				else if (estado.equals("modificar"))
				{
					return accionModificar(con,tiposMonitoreoForm,mapping,usuario,request);
				}
				else if (estado.equals("insertar"))
				{
					return accionInsertar(con,tiposMonitoreoForm,mapping,usuario,request);
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con,tiposMonitoreoForm,mapping,usuario,request);
				}
				else if (estado.equals("nuevo"))
				{
					return accionNuevo(con,tiposMonitoreoForm,mapping,request);
				}
				else if(estado.equals("eliminar"))
				{
					return accionEliminar(con,tiposMonitoreoForm,mapping,usuario,request);
				}
				else if(estado.equals("subirCentroN"))
				{
					return accionSubirCentro(con,tiposMonitoreoForm,mapping,request,1);
				}
				else if(estado.equals("subirCentroM"))
				{
					return accionSubirCentro(con,tiposMonitoreoForm,mapping,request,2);
				}
				else
				{
					tiposMonitoreoForm.reset();
					logger.warn("Estado no valido dentro del flujo de tipos de monitoreo (null) ");
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
	 * Métdo usado para eliminar un registro del listado de tipos de monitoreo
	 * @param con
	 * @param tiposMonitoreoForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEliminar(Connection con, TiposMonitoreoForm tiposMonitoreoForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) {
		
		HashMap registroEliminacion=new HashMap();	
		int resp=0; //variable para almacenar el resultado de la eliminación
		boolean esUtilizado=false;
		//se instancia objeto de Tipos monitoreo
		TiposMonitoreo tipos=new TiposMonitoreo();
		//se llena el mundo con los datos del formulario
		this.llenarMundoFinal(tiposMonitoreoForm,tipos);
		//se consulta si el registro está siendo utilizado en otras tablas
		esUtilizado=tipos.revisarUsoTipoMonitoreo(con);

		//se verifica que el registro no se esté utilizando
		if(!esUtilizado)
		{	
			//se cargan los datos del registro a eliminar
			registroEliminacion=tipos.cargarTipoMonitoreo(con);
			//se realiza la eliminación
			resp=tipos.eliminarTipoMonitoreo(con);
			//se verifica resultado de la transacción
			if(resp<=0)
			{
				logger.error("No se pudo eliminar el registro");
				tiposMonitoreoForm.setMensaje(new ResultadoBoolean(true, "Error. No se puede eliminar el registro."));
				//return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en TiposMonitoreoAction", "errors.sinEliminar", true);
			}
			else
			{
				tiposMonitoreoForm.setMensaje(new ResultadoBoolean(true,"Tipo de Monitoreo '"+tiposMonitoreoForm.getTiposMonitoreo("nombre_"+tiposMonitoreoForm.getIndexMap())+"' eliminado con exito."));
			}
			//se inserta LOG
			this.generarLog(registroEliminacion,null,ConstantesBD.tipoRegistroLogEliminacion,usuario);
		}

		//se cambia estado según resultado
		if(esUtilizado) {
			tiposMonitoreoForm.setEstado("error");
			tiposMonitoreoForm.setMensaje(new ResultadoBoolean(true, "Error. No se puede eliminar el registro."));
			
		}
		else
			tiposMonitoreoForm.setEstado("empezar");
		
		return accionEmpezar(con,tiposMonitoreoForm,mapping,usuario,request);
	}

	/**
	 * Método que crea un nuevo registro para insertar o modificar
	 * @param con
	 * @param tiposMonitoreoForm
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionNuevo(Connection con, TiposMonitoreoForm tiposMonitoreoForm, ActionMapping mapping, HttpServletRequest request) {
		tiposMonitoreoForm.setDescripcion("");
		tiposMonitoreoForm.setPrioridad("");
		tiposMonitoreoForm.setServicio("-1");
		tiposMonitoreoForm.setCheck("");	
		tiposMonitoreoForm.resetCG();
		tiposMonitoreoForm.setCentrosCostoMap(Utilidades.consultaCentrosCostoFiltros(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	private ActionForward accionSubirCentro(Connection con, TiposMonitoreoForm forma, ActionMapping mapping, HttpServletRequest request, int band) {
		String nombre="";
		for(int i=0;i<Integer.parseInt(forma.getCentrosCostoMap("numRegistros").toString());i++)
		{
			if(forma.getCentroCosto().equals(forma.getCentrosCostoMap("codigo_"+i).toString()))
			{
				nombre=forma.getCentrosCostoMap("nombre_"+i).toString();
				forma.setCentrosCostoMap("selec_"+i, ConstantesBD.acronimoSi);
			}
		}
		forma.setNumCentrosGenerados(forma.getNumCentrosGenerados()+1);
		
		forma.setCentrosGeneradosMap("codcc_"+forma.getNumCentrosGenerados(), forma.getCentroCosto());
		forma.setCentrosGeneradosMap("checkcc_"+forma.getNumCentrosGenerados(), "1");
		forma.setCentrosGeneradosMap("centro_"+forma.getNumCentrosGenerados(), nombre);
		forma.setCentrosGeneradosMap("usado_"+forma.getNumCentrosGenerados(), ConstantesBD.acronimoNo);
		forma.setCentrosGeneradosMap("numRegistros", forma.getNumCentrosGenerados());
		
		if(band==1)
			forma.setEstado("nuevo");
		else
			forma.setEstado("modificar");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
		
	}
	
	/**
	 * Metodo para Modificar
	 * @param con
	 * @param tiposMonitoreoForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionModificar(Connection con, TiposMonitoreoForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) {
		forma.setCentrosCostoMap(Utilidades.consultaCentrosCostoFiltros(con));
		int registros=0;
		if(!forma.getIndexMap().equals(""))
		{
			String codigo=forma.getTiposMonitoreo("codigo_"+forma.getIndexMap()).toString();
			forma.setDescripcion(forma.getTiposMonitoreo("nombre_"+forma.getIndexMap()).toString());
			forma.setPrioridad(forma.getTiposMonitoreo("prioridad_"+forma.getIndexMap()).toString());
			forma.setServicio(forma.getTiposMonitoreo("servicio_"+forma.getIndexMap()).toString());
			if(forma.getTiposMonitoreo("reqval_"+forma.getIndexMap()).toString().equals("S"))
				forma.setCheck("1");
			else
				forma.setCheck("0");
			for(int i=0;i<Integer.parseInt(forma.getCentrosPorTipo("numRegistros").toString());i++)
			{
				if(codigo.equals(forma.getCentrosPorTipo("tipo_"+i).toString()))
				{
					registros++;
					forma.setCentrosGeneradosMap("codcc_"+registros, forma.getCentrosPorTipo("centro_"+i));
					forma.setCentrosGeneradosMap("checkcc_"+registros, "1");
					forma.setCentrosGeneradosMap("centro_"+registros, forma.getCentrosPorTipo("nomcc_"+i));
					forma.setCentrosGeneradosMap("usado_"+registros, forma.getCentrosPorTipo("usado_"+i));
					for(int z=0;z<Integer.parseInt(forma.getCentrosCostoMap("numRegistros").toString());z++)
					{
						if(forma.getCentrosCostoMap("codigo_"+z).toString().equals(forma.getCentrosGeneradosMap("codcc_"+registros).toString()))
						{
							forma.setCentrosCostoMap("selec_"+z, ConstantesBD.acronimoSi);
						}
					}
				}
			}
			forma.setCentrosGeneradosMap("numRegistros", registros);
			forma.setNumCentrosGenerados(registros);
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Metodo para llenar las variables en el mundo para Insertar un nuevo tipo
	 * @param tiposMonitoreoForm
	 * @param tipos
	 * @param i
	 */
	private void llenarMundoFinal(TiposMonitoreoForm forma, TiposMonitoreo tipos)
	{
		if(forma.getEstado().equals("insertar"))
		{
			tipos.setNombre(forma.getDescripcion());
			tipos.setPrioridad(Utilidades.convertirAEntero(forma.getPrioridad()));
			tipos.setCheck(forma.getCheck());
			tipos.setServicio(Utilidades.convertirAEntero(forma.getServicio()));
		}
		else if(forma.getEstado().equals("eliminar"))
		{
			tipos.setCodigo(forma.getCodigoRegistro());
		}
		else if(forma.getEstado().equals("guardar"))
		{
			tipos.setNombre(forma.getDescripcion());
			tipos.setPrioridad(Utilidades.convertirAEntero(forma.getPrioridad()));
			tipos.setCheck(forma.getCheck());
			tipos.setServicio(Utilidades.convertirAEntero(forma.getServicio()));
			tipos.setCodigo(Integer.parseInt(forma.getTiposMonitoreo("codigo_"+forma.getIndexMap()).toString()));
		}
	}
	
	/**
	 * Metodo para insertar un nuevo tipo de monitoreo
	 * @param con
	 * @param tiposMonitoreoForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionInsertar(Connection con, TiposMonitoreoForm tiposMonitoreoForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{
		int resp=0;
		TiposMonitoreo tipos = new TiposMonitoreo();
		this.llenarMundoFinal(tiposMonitoreoForm,tipos);
		resp=tipos.insertarTipoMonitoreo(con,usuario.getCodigoInstitucionInt());
		if(resp>0)
		{
			for(int i=1;i<=Integer.parseInt(tiposMonitoreoForm.getCentrosGeneradosMap("numRegistros").toString());i++)
			{
				if(tiposMonitoreoForm.getCentrosGeneradosMap("checkcc_"+i).equals("1"))
				{
					tipos.setCentroCosto(Utilidades.convertirAEntero(tiposMonitoreoForm.getCentrosGeneradosMap("codcc_"+i).toString()));
					if(tipos.insertarCentros(con, resp, tipos.getCentroCosto()))
					{
						logger.info("bien>>>>>>"+i);
					}
					else
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en TiposMonitoreoAction", "errors.sinIngresar",true);
					}
				}
			}
			tiposMonitoreoForm.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		}
		else
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en TiposMonitoreoAction", "errors.sinIngresar",true);
		}
		
		tiposMonitoreoForm.setTiposMonitoreo(tipos.cargarTiposMonitoreo(con,usuario.getCodigoInstitucionInt()));
		tiposMonitoreoForm.setCentrosPorTipo(tipos.cargarCentrosPorTipo(con));
		tiposMonitoreoForm.setNumRegistros(Integer.parseInt(tiposMonitoreoForm.getTiposMonitoreo("numRegistros")+""));
		tiposMonitoreoForm.setCentrosCostoMap(Utilidades.consultaCentrosCostoFiltros(con));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método usado para actualiar o insertar un nuevo registro de tipos monitoreo
	 * @param con
	 * @param tiposMonitoreoForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, TiposMonitoreoForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) {
		logger.info("MAPAA CENTROS GENERADOS PA LOS CEHCK>>>>>>>>>>>"+forma.getCentrosGeneradosMap());
		int resp=0;
		TiposMonitoreo tipos = new TiposMonitoreo();
		forma.setMensaje(new ResultadoBoolean(false,""));
		llenarMundoFinal(forma, tipos);
		resp=tipos.actualizarTiposMonitoreo(con);
		if(resp>0)
		{
			for(int p=1;p<=Integer.parseInt(forma.getCentrosGeneradosMap("numRegistros").toString());p++)
			{
				if(forma.getCentrosGeneradosMap("checkcc_"+p).toString().equals("0"))
				{
					tipos.eliminarCentro(con, tipos.getCodigo(), Integer.parseInt(forma.getCentrosGeneradosMap("codcc_"+p).toString()));
				}
				if(forma.getCentrosGeneradosMap("checkcc_"+p).toString().equals("1"))
				{
					tipos.insertarCentros(con, tipos.getCodigo(), Integer.parseInt(forma.getCentrosGeneradosMap("codcc_"+p).toString()));
				}
			}
		}
		else
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en TiposMonitoreoAction", "errors.sinActualizar",true);
		}
		
		forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
		
		forma.setTiposMonitoreo(tipos.cargarTiposMonitoreo(con,usuario.getCodigoInstitucionInt()));
		forma.setCentrosPorTipo(tipos.cargarCentrosPorTipo(con));
		forma.setNumRegistros(Integer.parseInt(forma.getTiposMonitoreo("numRegistros")+""));
		forma.setCentrosCostoMap(Utilidades.consultaCentrosCostoFiltros(con));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método usado para verificar si un registro fue modificado
	 * @param registro
	 * @param tipos
	 * @return
	 */
	private boolean fueModificado(HashMap registro,TiposMonitoreo tipos)
	{
		boolean cambio=false;
		//revision de las descripciones
		if(tipos.getNombre().compareTo(registro.get("nombre_0")+"")!=0)
			cambio=true;
		//revision de las prioridades de cobro
		int prioridad=-1;
		if(!(registro.get("prioridad_0")+"").equals("")&&!(registro.get("prioridad_0")+"").equals("null"))
			prioridad=Integer.parseInt(registro.get("prioridad_0")+"");
		if(tipos.getPrioridad()!=prioridad)
			cambio=true;
		//revision de los servicios
		int servicio=-1;
		if(!(registro.get("servicio_0")+"").equals("")&&!(registro.get("servicio_0")+"").equals("null"))
			servicio=Integer.parseInt(registro.get("servicio_0")+"");
		if(tipos.getServicio()!=servicio)
			cambio=true;
		//revision de los centros
		int centro=-1;
		if(!(registro.get("cc_0")+"").equals("")&&!(registro.get("cc_0")+"").equals("null"))
			centro=Integer.parseInt(registro.get("cc_0")+"");
		if(tipos.getCentroCosto()!=centro)
			cambio=true;
		//revision de los check
		String check="";
		if(!(registro.get("reqval_0")+"").equals("")&&!(registro.get("reqval_0")+"").equals("null"))
			check=registro.get("reqval_0")+"";
		if(!tipos.getCheck().equals(check))
			cambio=true;
		
		logger.info("CHECK BD>>>>>>>>>>"+registro.get("reqval_0")+"");
		logger.info("CHECK FORMA>>>>>>>>>>"+tipos.getCheck());
		
		return cambio;
	}
	/**
	 * Método usado para generar el log
	 * @param registro
	 * @param tiposMonitoreo
	 * @param tipo
	 * @param usuario
	 */
	private void generarLog(HashMap registro, TiposMonitoreo tipos, int tipo, UsuarioBasico usuario) {
		String log="";
		String prioridad="";
		String servicio="";
	    
		if(tipo==ConstantesBD.tipoRegistroLogModificacion)
		{
			//***verificación de algunos datos******************
		    if((registro.get("prioridad_0")+"").equals("")||(registro.get("prioridad_0")+"").equals("null"))
		    	registro.put("prioridad_0","Ninguna");
		    if((registro.get("sevicio_0")+"").equals("")||(registro.get("servicio_0")+"").equals("null"))
		    	registro.put("servicio_0","Ninguno");
		    //****************************************************
			
		    log="\n            ====INFORMACION ORIGINAL TIPO DE MONITOREO===== " +
			"\n*  Codigo [" +registro.get("codigo_0")+"] "+
			"\n*  Descripción ["+registro.get("nombre_0")+"] " +
			"\n*  Prioridad de Cobro ["+registro.get("prioridad_0")+"] " +
			"\n*  Servicio ["+registro.get("servicio_0")+"] " +
			""  ;
			
		    //***verificación de algunos datos******************
		    if(tipos.getPrioridad()>0)
		    	prioridad=tipos.getPrioridad()+"";
		    else
		    	prioridad="Ninguna";
		    if(tipos.getServicio()>0)
		    	servicio=tipos.getServicio()+"";
		    else
		    	servicio="Ninguno";
		    
		    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN DEL TIPO DE MONITOREO===== " +
			"\n*  Código [" +tipos.getCodigo()+"] "+
			"\n*  Descripción ["+tipos.getNombre()+"] " +
			"\n*  Prioridad de Cobro ["+prioridad+"] " +
			"\n*  Servicio ["+servicio+"] " +
			""  ;
		}
		else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
		{
			//***verificación de algunos datos******************
		    if((registro.get("prioridad_0")+"").equals("")||(registro.get("prioridad_0")+"").equals("null"))
		    	registro.put("prioridad_0","Ninguna");
		    if((registro.get("sevicio_0")+"").equals("")||(registro.get("servicio_0")+"").equals("null"))
		    	registro.put("servicio_0","Ninguno");
		    //****************************************************
			
		    log="\n            ====INFORMACION ELIMINADA DE TIPO DE MONITOREO===== " +
			"\n*  Codigo [" +registro.get("codigo_0")+"] "+
			"\n*  Descripción ["+registro.get("nombre_0")+"] " +
			"\n*  Prioridad de Cobro ["+registro.get("prioridad_0")+"] " +
			"\n*  Servicio ["+registro.get("servicio_0")+"] " +
			""  ;
		}
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logTiposMonitoreoCodigo, log, tipo,usuario.getLoginUsuario()); 
		
	}

	/**
	 * @param tiposMonitoreoForm
	 * @param tipos
	 * @param i
	 */
	private void llenarMundo(TiposMonitoreoForm tiposMonitoreoForm, TiposMonitoreo tipos, int i) {
		String prioridad="";
		if(tiposMonitoreoForm.getEstado().equals("guardar"))
		{
			tipos.setCodigo(Integer.parseInt(tiposMonitoreoForm.getTiposMonitoreo("codigo_"+i)+""));
			tipos.setNombre(tiposMonitoreoForm.getTiposMonitoreo("nombre_"+i)+"");
			logger.info("IMPRESION PRUEEBAAAA>>>>>>>>>>>"+tiposMonitoreoForm.getTiposMonitoreo("reqval_"+i).toString());
			if(tiposMonitoreoForm.getTiposMonitoreo("reqval_"+i).toString().equals("S") || tiposMonitoreoForm.getTiposMonitoreo("reqval_"+i).toString().equals("1"))
				tipos.setCheck("S");
			else
				tipos.setCheck("N");
			/*if(k>0 && tiposMonitoreoForm.getNumCentrosGenerados()>0)
				tipos.setCentroCosto(Integer.parseInt(tiposMonitoreoForm.getCentrosGeneradosMap("codcc_"+i+"_"+k)+""));
			else
				tipos.setCentroCosto(Integer.parseInt(tiposMonitoreoForm.getTiposMonitoreo("cc_"+i)+""));*/
			prioridad=tiposMonitoreoForm.getTiposMonitoreo("prioridad_"+i)+"";
			if(prioridad.equals("")||prioridad.equals("null"))
				tipos.setPrioridad(-1);
			else
				tipos.setPrioridad(Integer.parseInt(prioridad));
			tipos.setServicio(Integer.parseInt(tiposMonitoreoForm.getTiposMonitoreo("servicio_"+i)+""));
			
			for(int z=0;z<Integer.parseInt(tiposMonitoreoForm.getTiposMonitoreo("numRegistros").toString());z++)
			{
				if(i!=z)
				{
					if(Integer.parseInt(tiposMonitoreoForm.getTiposMonitoreo("codigo_"+i)+"")==Integer.parseInt(tiposMonitoreoForm.getTiposMonitoreo("codigo_"+z)+""))
					{
						tiposMonitoreoForm.setTiposMonitoreo("nombre_"+z, tiposMonitoreoForm.getTiposMonitoreo("nombre_"+i)+"");
						tiposMonitoreoForm.setTiposMonitoreo("prioridad_"+z, tiposMonitoreoForm.getTiposMonitoreo("prioridad_"+i)+"");
						tiposMonitoreoForm.setTiposMonitoreo("servicio_"+z, tiposMonitoreoForm.getTiposMonitoreo("servicio_"+i)+"");
						tiposMonitoreoForm.setTiposMonitoreo("reqval_"+z, tiposMonitoreoForm.getTiposMonitoreo("reqval_"+i)+"");
					}
				}
			}
			
		}
		else if(tiposMonitoreoForm.getEstado().equals("eliminar"))
		{
			tipos.setCodigo(tiposMonitoreoForm.getCodigoRegistro());
		}
		
	}
	
	/**
	 * @param tiposMonitoreoForm
	 * @param tipos
	 * @param i
	 */
	private void llenarMundoCentros(TiposMonitoreoForm tiposMonitoreoForm, TiposMonitoreo tipos, int i,int k) {
		if(k>0 && tiposMonitoreoForm.getNumCentrosGenerados()>0)
			tipos.setCentroCosto(Integer.parseInt(tiposMonitoreoForm.getCentrosGeneradosMap("codcc_"+i+"_"+k)+""));
		else
			tipos.setCentroCosto(Integer.parseInt(tiposMonitoreoForm.getTiposMonitoreo("cc_"+i)+""));		
	}

	/**
	 * Método usado para cargar los tipos de monitoreo
	 * @param con
	 * @param tiposMonitoreoForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, TiposMonitoreoForm tiposMonitoreoForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) {
		PersonaBasica paciente=new PersonaBasica();
		try
		{
			String estado=tiposMonitoreoForm.getEstado();
			tiposMonitoreoForm.reset();
			tiposMonitoreoForm.setEstado(estado);
			//**Se instancia objeto de tipos de monitoreo******
			TiposMonitoreo tipos=new TiposMonitoreo();
			//se cargan los tipos de monitoreo actuales
			tiposMonitoreoForm.setTiposMonitoreo(tipos.cargarTiposMonitoreo(con,usuario.getCodigoInstitucionInt()));
			tiposMonitoreoForm.setCentrosPorTipo(tipos.cargarCentrosPorTipo(con));
			//almacenar el número de registros consultados
			tiposMonitoreoForm.setNumRegistros(Integer.parseInt(tiposMonitoreoForm.getTiposMonitoreo("numRegistros")+""));
			//Carga en el Mapa los centros de Costo por via de ingreso
			tiposMonitoreoForm.setCentrosCostoMap(Utilidades.consultaCentrosCostoFiltros(con));
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		catch(SQLException e)
		{
			logger.error("Error en accionEmpezar de TiposMonitoreoAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en TiposMonitoreoAction", "errors.problemasDatos", true);
		}
	}
} 