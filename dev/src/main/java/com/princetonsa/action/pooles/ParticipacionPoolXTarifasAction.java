/*
 * @(#)ParticipacionPoolXTarifasAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.pooles;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.pooles.ParticipacionPoolXTarifasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.EsquemaTarifario;
import com.princetonsa.mundo.pooles.ParticipacionPoolXTarifas;

/**
 *   Action, controla todas las opciones dentro de la participación
 *   pool X tarifas, incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @version 1.0, Nov 30, 2004
 * @author wrios
 */
public class ParticipacionPoolXTarifasAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ParticipacionPoolXTarifasAction.class);
	
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
		try{
			if (response==null); //Para evitar que salga el warning
			if(form instanceof ParticipacionPoolXTarifasForm)
			{

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}
				ParticipacionPoolXTarifasForm forma =(ParticipacionPoolXTarifasForm)form;
				String estado=forma.getEstado(); 
				logger.info("\n\n ESTADO Participacion pool-->"+estado+"\n\n");
				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				if(estado == null)
				{
					forma.reset();	
					logger.warn("Estado no valido dentro del flujo de la Participacion pool por Tarifas (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return this.accionEmpezar(forma,mapping, con);
				}
				else if (estado.equals("empezarIngresarModificarEliminar"))
				{
					return this.accionEmpezarIngresarModificarEliminar(forma,mapping, con,usuario);
				}
				else if(estado.equals("ingresarNuevoElementoMapa"))
				{
					return this.accionIngresarNuevoElementoMapa(forma,con, request, response,usuario);
				}
				else if(estado.equals("eliminarElementoMapa"))
				{
					return this.accionEliminarElementoMapa(forma, mapping, con,  response);
				}
				else if (estado.equals("modificarEsquemaTarifario"))
				{
					// si todo sale bien en el form entonces
					EsquemaTarifario mundoEsquema= new EsquemaTarifario();
					mundoEsquema.setCodigo(Integer.parseInt(forma.getMapaPoolXTarifas("codigoEsquemaTarifario_"+forma.getIndexMapa())+""));
					mundoEsquema.cargar(con, usuario.getCodigoInstitucionInt());
					forma.setMapaPoolXTarifas("nombreEsquemaTarifario_"+forma.getIndexMapa(), mundoEsquema.getNombre());

					this.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");		
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.cerrarConexion(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("guardar"))
				{
					return this.accionGuardar(forma,mapping,request,usuario,con);
				}
				else if(estado.equals("empezarListarConsultar"))
				{
					return this.accionEmpezarListarConsultar(forma,mapping,con);
				}
				else if(estado.equals("listarConsultar"))
				{
					return this.accionListarConsultar(forma,mapping,con,estado,usuario);
				}
				else if(estado.equals("busquedaAvanzada"))
				{
					return this.accionBusquedaAvanzada(forma,mapping,con);
				}
				else if(estado.equals("resultadoBusquedaAvanzada") )
				{
					return this.accionResultadoBusquedaAvanzada(forma,mapping,request,con,false,usuario );
				}
				else if(estado.equals("resultadoBusquedaIngreso"))
				{
					return this.accionResultadoBusquedaAvanzada(forma,mapping,request,con, true,usuario);
				}
				else if(estado.equals("resumen"))
				{
					return this.accionResumen(forma,mapping,con, estado,usuario);
				}
				/*-----------------------------------------------------------------
				 * ESTADOS ADICIONADOS POR JHONY ALEXANDER DUQUE A.
	 ------------------------------------------------------------------*/
				/*-----------------------------
				 * ESTADO GUARDARCONVENIO
			 -----------------------------*/
				else if(estado.equals("guardarConvenio"))
				{

					return this.accionGuardarModificarEliminar(con, forma, mapping, usuario);
				}
				/*-----------------------------
				 * ESTADO BUSQUEDAABANZADA
			 -----------------------------*/
				else if(estado.equals("busquedaAbanzada"))
				{

					return this.accionGuardarModificarEliminar(con, forma, mapping, usuario);
				}


				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de la Participacion pool por Tarifas (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}			
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;	
}
	
	
	
	

	
	
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezar.
	 * 
	 * @param forma ParticipacionPoolXTarifasForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "participacionPoolXTarifas.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(	ParticipacionPoolXTarifasForm forma, 
																ActionMapping mapping,
																Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		forma.reset();
		forma.resetMapa();
		forma.setEstado("empezar");
		this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezarIngresarModificarEliminar.
	 * 
	 * @param forma ParticipacionPoolXTarifasForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "participacionPoolXTarifas.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezarIngresarModificarEliminar(ParticipacionPoolXTarifasForm forma, 
																 ActionMapping mapping,
																 Connection con, UsuarioBasico usuario) throws SQLException
	{
		forma.setEstado("empezarIngresarModificarEliminar");
		//logger.info("jhony--- hashmanp entro a  empezarIngresarModificarEliminar");
		if(forma.getCodigoPool()!=ConstantesBD.codigoNuncaValido && 
				forma.getTipoParame().toString().equals(ConstantesIntegridadDominio.acronimoTarifario))
		{
			ParticipacionPoolXTarifas mundo= new ParticipacionPoolXTarifas();
		
			mundo.setCodigoPool(forma.getCodigoPool());
			mundo.setInstitucion(usuario.getCodigoInstitucionInt());
			forma.resetMapa();
			forma.setMapaPoolXTarifas(mundo.listadoMapaParticipacionPoolXTarifas(con));//cargar
			forma.setMapaPoolXTarifasNoModificado((HashMap)forma.getMapaPoolXTarifas().clone());//retornar
		}
		else
			if(forma.getCodigoPool()!=ConstantesBD.codigoNuncaValido && 
					forma.getTipoParame().toString().equals(ConstantesIntegridadDominio.acronimoConvenio))
			{
				forma.initConvenio(con);
				ParticipacionPoolXTarifas mundo= new ParticipacionPoolXTarifas();
				mundo.setCodigoPool(forma.getCodigoPool());
				mundo.setInstitucion(usuario.getCodigoInstitucionInt());
				
				forma.setMapaPoolXTarifas(mundo.listadoMapaParticipacionPoolXConvenio(con));
				forma.setMapaPoolXTarifasNoModificado((HashMap)forma.getMapaPoolXTarifas().clone());
			
				
			}
		
	
		this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");		
	}
	
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * guardar.
	 * 
	 * @param ParticipacionPoolXTarifasForm forma
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward 
	 * @throws SQLException
	 */
	private ActionForward accionGuardar (	ParticipacionPoolXTarifasForm forma,
																ActionMapping mapping,
																HttpServletRequest request, 
																UsuarioBasico usuario,
																Connection con) throws SQLException
	{
	    boolean validarActualizacionTransaccional=actualizarGuardarNuevosEliminarViejosBDTransaccional(forma,usuario,con);
	    
	    if(!validarActualizacionTransaccional)
	    {
	        ActionErrors errores = new ActionErrors();
			errores.add("actualización Pool X Tarifas", new ActionMessage("errors.ingresoDatos","los datos de la participación del Pool por Tarifas (Transacción)"));
			logger.warn("error en la actualización de los datos del pool X Tarifas");
			saveErrors(request, errores);	
			this.cerrarConexion(con);									
			return mapping.findForward("paginaPrincipal");
	    }
	    return this.accionResumen(forma, mapping, con, "resumen",usuario);
	}
	
	
	/**
	 * Guarda, Modifica o Elimina un Registro en Cobertura
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return ActionForward
	 * */
	private ActionForward accionGuardarModificarEliminar(Connection connection, ParticipacionPoolXTarifasForm forma, ActionMapping mapping,UsuarioBasico usuario)  throws SQLException
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(connection);
		String estadoEliminar="ninguno";
		ParticipacionPoolXTarifas mundo = new ParticipacionPoolXTarifas ();
		mundo.reset();
		String [] indices = {"codigoPool","cuentaPool_","cuentaInstitucion_","cuentaInstitucionAnterior_","codigoConvenio_","porcentajeParticipacion_","valorParticipacion_"};
		//logger.info("valor del hasmap conceptosPagoPoolesMap al entar a accionGuardarRegistros "+forma.getConceptosPagoPoolesMap());
		//logger.info("valor del hasmap conceptosPagoPoolesEliminadosMap al entar a accionGuardarRegistros "+forma.getConceptosPagoPoolesEliminadosMap());
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getMapaPoolXTarifas("numRegistros")+"");i++)
		{
			logger.info("Entro a eliminar BD :::::::::::::contador I...>"+i);
			logger.info("Hashmap:::::::::::"+forma.getMapaPoolXTarifas());
			 if((forma.getMapaPoolXTarifas("estaBD_"+i)+"").equals("t") && (forma.getMapaPoolXTarifas("esEliminada_"+i)+"").equals("t"))
             { 
				if(eliminarBD(connection, forma, i)>0);			
				 {				
					 //se genera el log tipo harchivo
					Utilidades.generarLogGenerico(forma.getMapaPoolXTarifas(), null, usuario.getLoginUsuario(), true, i, ConstantesBD.logParticipacionPoolXTarifasCodigo, indices);
					estadoEliminar="operacionTrue";
					transacction=true;						 
				}
			         
             }
		}
		
		for(int i=0;i<Integer.parseInt(forma.getMapaPoolXTarifas("numRegistros")+"");i++)
		{	
			//modificar			
			 logger.info("paso por MODIFICAR::::::::..");
			 if((forma.getMapaPoolXTarifas("estaBD_"+i)+"").equals("t") )
             {
				 logger.info("ENTRO A EXISTEMODIFICACION::::::::..");
				if(this.existeModificacion(connection, forma, mundo, i, usuario))
				{	
												
					/*logger.info("HashMap:::::::::"+forma.getConsentimientoInfMap());
					logger.info("subindice:::::..."+i);*/
					logger.info("ENTRO A MODIFICAR::::::::..");
					HashMap tmp = new HashMap();
						
					mundo.reset();
					mundo.setCodigoPool(forma.getCodigoPool());
					mundo.setConvenio(forma.getMapaPoolXTarifasNoModificado("codigoConvenio_"+i).toString());
					mundo.setInstitucion(Integer.parseInt(forma.getMapaPoolXTarifasNoModificado("institucion_"+i).toString()));
					
					tmp=mundo.listadoMapaParticipacionPoolXConvenio(connection);
								
					//logger.info("EL VALOR DE TMP ES "+tmp);
					transacction = modificarBD(connection, forma, i,usuario);
					//se genera el log tipo harchivo
					
					Utilidades.generarLogGenerico( forma.getMapaPoolXTarifas(),tmp, usuario.getLoginUsuario(), false, i, ConstantesBD.logParticipacionPoolXTarifasCodigo, indices);
					estadoEliminar="operacionTrue";
				}	
				
			}
			
			//insertar	
			
			else
				if ((forma.getMapaPoolXTarifas("esEliminada_"+i)+"").equals("f"))
			{
				 logger.info("paso por INSERTAR::::::::..");
				if(!forma.getMapaPoolXTarifas("numRegistros").toString().equals("0") && forma.getMapaPoolXTarifas("estaBD_"+i).equals("f"))
			{
				logger.info("Entro a insertar  "); 
				 transacction = insertarBD(connection, forma, i, usuario);
				 
				 estadoEliminar="operacionTrue";
			}
			}
			// logger.info("\n\n valor i >> "+i);
		}
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(connection);			
			logger.info("----->INSERTO 100% CONCEPTOS PAGO POOLES");
			
		}
		else
		{
			UtilidadBD.abortarTransaccion(connection);
		}
		if (estadoEliminar=="operacionTrue")
			forma.setEstado("operacionTrue");
		
		return this.accionEmpezarIngresarModificarEliminar(forma, mapping, connection, usuario);		 		
	}
	
	
	
	/**
	 * Este metodo se encarga de insertar en la BD los nuevos datos; pordentro el crea un HashMap
	 * con los nuevos datos que se van a ingresar a la BD
	 * @param connection
	 * @param forma
	 * @param i
	 * @param usuario
	 * @return
	 * @throws SQLException 
	 */
	public boolean insertarBD (Connection connection, ParticipacionPoolXTarifasForm forma,int i,UsuarioBasico usuario) throws SQLException
	{
		logger.info("entre a insertar BD "+forma.getMapaPoolXTarifas());
		
		 ParticipacionPoolXTarifas mundo = new ParticipacionPoolXTarifas();
		//logger.info("\n\n entro a insertarDB "+forma.getConceptosPagoPoolesMap());	
		//logger.info("\n\n el valor de i  es "+i);	
		 //se inicializa el hashmap para que el sql lo pueda entender
		mundo.reset();	
		mundo.setConvenio(forma.getMapaPoolXTarifas("codigoConvenio_"+i).toString());
		if(!UtilidadTexto.isEmpty(forma.getMapaPoolXTarifas("porcentajeParticipacion_"+i)+""))
		{
			mundo.setPorcentajeParticipacion(Double.parseDouble(forma.getMapaPoolXTarifas("porcentajeParticipacion_"+i).toString()));
		}
		else
		{
			mundo.setPorcentajeParticipacion(ConstantesBD.codigoNuncaValido);
		}
		mundo.setCuentaPool(Integer.parseInt(forma.getMapaPoolXTarifas("cuentaPool_"+i).toString()));
		
		mundo.setCuentaInstitucion(Integer.parseInt(forma.getMapaPoolXTarifas("cuentaInstitucion_"+i)+""));
		mundo.setUsuario(usuario.getLoginUsuario());
		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
		
		mundo.setCuentaInstitucionAnterior(Integer.parseInt(forma.getMapaPoolXTarifas("cuentaInstitucionAnterior_"+i)+""));
		mundo.setCodigoPool(Integer.parseInt(forma.getCodigoPool()+""));
		if(!UtilidadTexto.isEmpty(forma.getMapaPoolXTarifas("valorParticipacion_"+i)+""))
		{
			mundo.setValorParticipacion(Double.parseDouble(forma.getMapaPoolXTarifas("valorParticipacion_"+i)+""));
		}
		else
		{
			mundo.setValorParticipacion(ConstantesBD.codigoNuncaValido);
		}
		
	
		logger.info("YA PASE POR ESTE PEDAZO JEJE");
		 
			
			 //logger.info("\n\n saliendo de insertarDB el valor de temp"+temp);
		return mundo.insertarParticipacionPoolXConvenioTransaccional(connection, ConstantesBD.inicioTransaccion);
	}
	
	
	
	/**
	 * este metodo se encarga de guardar los cambios en la BD; el parametro "i"
	 * es el encargado de manejar la posicion dentro del HashMap
	 * @param connection
	 * @param forma
	 * @param i 
	 * @return
	 * @throws SQLException 
	 */
	public boolean modificarBD (Connection connection,ParticipacionPoolXTarifasForm forma,int i,UsuarioBasico usuario ) throws SQLException
	{
		//logger.info("valor de haspmap al entrar"+forma.getConceptosPagoPoolesMap());
		ParticipacionPoolXTarifas mundo = new ParticipacionPoolXTarifas();
		mundo.reset();	
		mundo.setConvenioOld(forma.getMapaPoolXTarifasNoModificado("codigoConvenio_"+i)+"");
		mundo.setConvenio(forma.getMapaPoolXTarifas("codigoConvenio_"+i)+"");
		if(!UtilidadTexto.isEmpty(forma.getMapaPoolXTarifas("porcentajeParticipacion_"+i)+""))
		{
			mundo.setPorcentajeParticipacion(Double.parseDouble(forma.getMapaPoolXTarifas("porcentajeParticipacion_"+i)+""));
		}
		else
		{
			mundo.setPorcentajeParticipacion(ConstantesBD.codigoNuncaValido);
		}
		mundo.setCuentaPool(Integer.parseInt(forma.getMapaPoolXTarifas("cuentaPool_"+i)+""));
		mundo.setCuentaInstitucion(Integer.parseInt(forma.getMapaPoolXTarifas("cuentaInstitucion_"+i)+""));
		mundo.setUsuario(usuario.getLoginUsuario());
		mundo.setCuentaInstitucionAnterior(Integer.parseInt(forma.getMapaPoolXTarifas("cuentaInstitucionAnterior_"+i)+""));
		mundo.setCodigoPool(Integer.parseInt(forma.getMapaPoolXTarifas("codigoPool_"+i)+""));
		if(!UtilidadTexto.isEmpty(forma.getMapaPoolXTarifas("valorParticipacion_"+i)+""))
		{
			mundo.setValorParticipacion(Double.parseDouble(forma.getMapaPoolXTarifas("valorParticipacion_"+i)+""));
		}
		else
		{
			mundo.setValorParticipacion(ConstantesBD.codigoNuncaValido);
		}
		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
		
		return mundo.modificarParticipacionPoolXconvenioTransaccional(connection, ConstantesBD.inicioTransaccion);
	}
	
	
	
	
	
	
	/**
	 * verifica si el registro se ha modificado
	 * @param connection
	 * @param forma
	 * @param mundo
	 * @param int pos
	 * */
	private boolean existeModificacion(Connection connection,ParticipacionPoolXTarifasForm forma,ParticipacionPoolXTarifas mundo, int pos , UsuarioBasico usuario)
	{
		HashMap temp = new HashMap();
		
		//iniciamos los datos para la consulta
		mundo.reset();
		mundo.setCodigoPool(forma.getCodigoPool());
		mundo.setConvenio(forma.getMapaPoolXTarifasNoModificado("codigoConvenio_"+pos).toString());
		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
		temp=mundo.listadoMapaParticipacionPoolXConvenio(connection);
		
			
		logger.info("HashMap de tmp existeModificacion ::::::"+temp);
		logger.info("HashMap de la forma existeModificacion ::::::"+forma.getMapaPoolXTarifas());
		String[] indices = (String[])forma.getMapaPoolXTarifas("INDICES_MAPA");		
			
		for(int i=0;i<indices.length;i++)
		{		
			logger.info("1");
			if(temp.containsKey(indices[i]+"0")&&forma.getMapaPoolXTarifas().containsKey(indices[i]+pos))
			{				
				logger.info("2");	
				if(!((temp.get(indices[i]+"0").toString().equals((forma.getMapaPoolXTarifas(indices[i]+pos)).toString()))))
				{
					//this.generarlog(forma, temp, usuario, false, pos);
					logger.info("3");
					return true;
					
				}				
			}
		}	
		
		return false;		
	}
	
	
	/**
	 * Carga los datos a eliminar en la BD; el parametro "i"
	 * es el encargado de manejar la posicion dentro del HashMap
	 * @param connection
	 * @param forma
	 * @return
	 * @throws SQLException 
	 */
	public int eliminarBD (Connection connection, ParticipacionPoolXTarifasForm  forma,int i) throws SQLException
	{
		//logger.info(":::::::::ENTRO A ELIMINARBD::::::::::::");
		HashMap parametros = new HashMap ();
		ParticipacionPoolXTarifas mundo = new ParticipacionPoolXTarifas ();
        
		//logger.info("el valor del hashmap es "+forma.getConceptosPagoPoolesEliminadosMap()+" y el valor de la i es "+i);
		
		//iniciamos los datos para la consulta
		mundo.reset();
		mundo.setCodigoPool(forma.getCodigoPool());
		mundo.setConvenio(forma.getMapaPoolXTarifas("codigoConvenio_"+i).toString());
		mundo.setInstitucion(Integer.parseInt(forma.getMapaPoolXTarifas("institucion_"+i).toString()));
		
				
		return mundo.eliminarParticipacionPoolXConvenioTransaccional(connection, ConstantesBD.inicioTransaccion);
	}
	
	
	
	
	/**
	 * Método transaccional que comparando el HashMap Original y el modificado,
	 * entonces inserta en BD los que en el hash Map tienen como key del
	 * estaBD=  y esEliminada= los nuevos, y los que han sido eliminados que
	 * estan en BD  estaBD= y esEliminada=, por otra parte modifica los datos 
	 * pasandolos a un nuevo mapa que contiene solo la info modificada para enviar 
	 * a la BD. 
	 * 
	 * @param ParticipacionPoolXTarifasForm forma
	 * @param con
	 * @throws SQLException
	 * 
	 * @return true cuando todo salió bien 
	 */
	private boolean actualizarGuardarNuevosEliminarViejosBDTransaccional(	ParticipacionPoolXTarifasForm forma,
	        																										UsuarioBasico usuario,
	        																										Connection con) throws SQLException
	{
	    ParticipacionPoolXTarifas mundo= new ParticipacionPoolXTarifas();
	    mundo.setCodigoPool(forma.getCodigoPool());
	    mundo.setInstitucion(usuario.getCodigoInstitucionInt());
	    mundo.setUsuario(usuario.getLoginUsuario());
	    boolean yaComenzoTransaccion=false;
	    int numeroInsertados=0;  

	    
	    for(int k=0; k<forma.getNumeroRealFilasMapa(); k++)
	    {
	        /*PRIMERA PARTE PARA LA ELIMINACIÓN DE DATOS QUE ESTAN EN BD*/
	        if(  (forma.getMapaPoolXTarifas("esEliminada_"+k)+"").equals("t"))
	         {
	             if((forma.getMapaPoolXTarifas("estaBD_"+k)+"").equals("t"))
	             {
	                 numeroInsertados=0;
	                 mundo.setCodigoEsquemaTarifario(Integer.parseInt(forma.getMapaPoolXTarifasNoModificado("codigoEsquemaTarifario_"+k)+""));
	                 mundo.setPorcentajeParticipacion(0);
	                 mundo.setCuentaPool(ConstantesBD.codigoNuncaValido);
	                 mundo.setCuentaInstitucion(ConstantesBD.codigoNuncaValido);
	                 mundo.setCuentaInstitucionAnterior(ConstantesBD.codigoNuncaValido);
	                    
	                 if(!yaComenzoTransaccion)
	                 {    
	                     numeroInsertados=mundo.eliminarParticipacionPoolXTarifasTransaccional(con, ConstantesBD.inicioTransaccion);
	                     if(numeroInsertados<=0)
	                         return false;
	                     else
	                         generarLog(forma, k, usuario, false);
	                     yaComenzoTransaccion=true;
	                 }    
	                 else
	                 {    
	                     numeroInsertados=mundo.eliminarParticipacionPoolXTarifasTransaccional(con, ConstantesBD.continuarTransaccion);
	                 	 if(numeroInsertados<=0)
	                 	     return false;
	                 	else
	                         generarLog(forma, k, usuario, false);
	                 }	 
	             }
	         }
	    }  
	    /*SEGUNDA PARTE PARA LA MODIFICACION DE LOS DATOS QUE ESTAN EN BD*/
		//en este punto se carga el mapa solo con los valores que han sido modificados
		forma.setMapaPoolXTarifasAux(forma.comparar2HashMap());
		
		if(forma.getNumeroRealFilasMapaAux()>0)
		{
		     for(int k=0; k<forma.getNumeroRealFilasMapaNoMod();k++)
		     {
		         if(  (forma.getMapaPoolXTarifas("esEliminada_"+k)+"").equals("f"))
		         {
				     if((forma.getMapaPoolXTarifas("estaBD_"+k)+"").equals("t"))
			         {
				         String tempoCodEsq=forma.getMapaPoolXTarifasAux("codigoEsquemaTarifario_"+k)+"";
				         if(tempoCodEsq!=null && !tempoCodEsq.equals("") && !tempoCodEsq.equals("null") && !tempoCodEsq.equals("-1"))
				         {
				             numeroInsertados=0;
				             mundo.setCodigoEsquemaTarifario(Integer.parseInt(forma.getMapaPoolXTarifasNoModificado("codigoEsquemaTarifario_"+k)+""));
				             mundo.setPorcentajeParticipacion(Utilidades.convertirADouble(forma.getMapaPoolXTarifasAux("porcentajeParticipacion_"+k)+""));
				             mundo.setValorParticipacion( Utilidades.convertirADouble(forma.getMapaPoolXTarifasAux("valorParticipacion_"+k)+""));
					         mundo.setCuentaPool(Integer.parseInt(forma.getMapaPoolXTarifasAux("cuentaPool_"+k)+""));
				             mundo.setCuentaInstitucion(Integer.parseInt(forma.getMapaPoolXTarifasAux("cuentaInstitucion_"+k)+""));
				             mundo.setCuentaInstitucionAnterior(Integer.parseInt(forma.getMapaPoolXTarifasAux("cuentaInstitucionAnterior_"+k)+""));
				           
				             if(!yaComenzoTransaccion)
				             {    
				                 numeroInsertados=mundo.modificarParticipacionPoolXTarifasTransaccional(con, ConstantesBD.inicioTransaccion, Integer.parseInt(forma.getMapaPoolXTarifasAux("codigoEsquemaTarifario_"+k)+""));
				                 if(numeroInsertados<=0)
				                     return false;
				                 else
			                         generarLog(forma, k, usuario, true);
				                  yaComenzoTransaccion=true;
				             }    
				             else
				             {    
				                  numeroInsertados=mundo.modificarParticipacionPoolXTarifasTransaccional(con, ConstantesBD.continuarTransaccion, Integer.parseInt(forma.getMapaPoolXTarifasAux("codigoEsquemaTarifario_"+k)+""));
				                  if(numeroInsertados<=0)
				                     return false;
				                  else
				                         generarLog(forma, k, usuario, true);
				              }    
				          }
			         }  
		         }
		      }
		}    
        
		for(int k=0; k<forma.getNumeroRealFilasMapa(); k++)
		{      
	       /*TERCERA PARTE PARA LA INSERCIÓN DE LOS NUEVOS DATOS*/
		    if(  (forma.getMapaPoolXTarifas("esEliminada_"+k)+"").equals("f"))
		    {
		        if((forma.getMapaPoolXTarifas("estaBD_"+k)+"").equals("f"))
	            {
	                numeroInsertados=0;
	                mundo.setCodigoEsquemaTarifario(Integer.parseInt(forma.getMapaPoolXTarifas("codigoEsquemaTarifario_"+k)+""));
	                mundo.setPorcentajeParticipacion(Utilidades.convertirADouble(forma.getMapaPoolXTarifas("porcentajeParticipacion_"+k)+""));
	                mundo.setValorParticipacion(Utilidades.convertirADouble(forma.getMapaPoolXTarifas("valorParticipacion_"+k)+""));
	                mundo.setCuentaPool(Integer.parseInt(forma.getMapaPoolXTarifas("cuentaPool_"+k)+""));
	                mundo.setCuentaInstitucion(Integer.parseInt(forma.getMapaPoolXTarifas("cuentaInstitucion_"+k)+""));
	                mundo.setCuentaInstitucionAnterior(Integer.parseInt(forma.getMapaPoolXTarifas("cuentaInstitucionAnterior_"+k)+""));
	                
	                 if(!yaComenzoTransaccion)
	                 {    
	                     numeroInsertados=mundo.insertarParticipacionPoolXTarifasTransaccional(con, ConstantesBD.inicioTransaccion);
	                     if(numeroInsertados<=0)
	                         return false;
	                     yaComenzoTransaccion=true;
	                 }    
	                 else
	                 {    
	                     numeroInsertados=mundo.insertarParticipacionPoolXTarifasTransaccional(con, ConstantesBD.continuarTransaccion);
	                     if(numeroInsertados<=0)
	                         return false;
	                 }    
	             }
	         }
	    }
	    // SI LA TRANSACCIÓN YA FUÉ INICIADA ENTONCES QUE LA FINALICE
	    if(yaComenzoTransaccion)
	        mundo.terminarTransaccion(con); 
	    return true;
	}
	
	/**
	 * Método que genera los Logs de Modificación y borrado 
	 * @param forma
	 * @param indexKeyCodigoMapaMod, indice de la llave.
	 * @param usuario, user
	 */
	private void generarLog(	ParticipacionPoolXTarifasForm forma, int indexKeyCodigoMapaMod, UsuarioBasico usuario, boolean esModificacion)
	{
	    String log;
	    
	    String tempoCuentaPool= "";
	    String tempoCuentaInstitucion="";
	    String tempoCuentaInstitucionAnterior="";
	        
	    if(esModificacion)
	    {    
	        tempoCuentaPool= (forma.getMapaPoolXTarifasNoModificado("cuentaPool_"+indexKeyCodigoMapaMod)+"");
		    tempoCuentaInstitucion=(forma.getMapaPoolXTarifasNoModificado("cuentaInstitucion_"+indexKeyCodigoMapaMod)+"");
	        tempoCuentaInstitucionAnterior=(forma.getMapaPoolXTarifasNoModificado("cuentaInstitucionAnterior_"+indexKeyCodigoMapaMod)+"");
	      
		    
		    if(tempoCuentaPool.equals(ConstantesBD.codigoNuncaValido+""))
		        tempoCuentaPool="";
		    if(tempoCuentaInstitucion.equals(ConstantesBD.codigoNuncaValido+""))
		        tempoCuentaInstitucion="";
		    if(tempoCuentaInstitucionAnterior.equals(ConstantesBD.codigoNuncaValido+""))
		        tempoCuentaInstitucionAnterior="";
			
		    		    
		    log="\n            ====INFORMACION ORIGINAL PARTICIPACION POOL X TARIFAS===== " +
			"\n*  Código Pool [" +forma.getCodigoPool() +"] "+
			"\n*  Código Esquema Tarifario ["+forma.getMapaPoolXTarifasNoModificado("codigoEsquemaTarifario_"+indexKeyCodigoMapaMod)+"] " +
			"\n*  % Participación ["+forma.getMapaPoolXTarifasNoModificado("porcentajeParticipacion_"+indexKeyCodigoMapaMod)+"] " +
			"\n*  Valor Participacion  ["+forma.getMapaPoolXTarifasNoModificado("valorParticipacion_"+indexKeyCodigoMapaMod)+"] " +
			"\n*  Cuenta Pool ["+tempoCuentaPool+"] " +
			"\n*  Cuenta Institución ["+tempoCuentaInstitucion+"] " +
			"\n*  Cuenta Vigencia Anterior Institución ["+tempoCuentaInstitucionAnterior+"] " +
			""  ;
			
		    tempoCuentaPool= (forma.getMapaPoolXTarifasAux("cuentaPool_"+indexKeyCodigoMapaMod)+"");
		    tempoCuentaInstitucion=(forma.getMapaPoolXTarifasAux("cuentaInstitucion_"+indexKeyCodigoMapaMod)+"");
		    tempoCuentaInstitucionAnterior=(forma.getMapaPoolXTarifasAux("cuentaInstitucionAnterior_"+indexKeyCodigoMapaMod)+"");
		    
		    if(tempoCuentaPool.equals(ConstantesBD.codigoNuncaValido+""))
		        tempoCuentaPool="";
		    if(tempoCuentaInstitucion.equals(ConstantesBD.codigoNuncaValido+""))
		        tempoCuentaInstitucion="";
		    if(tempoCuentaInstitucionAnterior.equals(ConstantesBD.codigoNuncaValido+""))
		        tempoCuentaInstitucionAnterior="";
		    
			log+="\n          =====INFORMACION DESPUES DE LA MODIFICACION PARTICIPACION POOL X TARIFAS===== " +
			"\n*  Código Pool [" +forma.getCodigoPool() +"] "+
			"\n*  Código Esquema Tarifario ["+forma.getMapaPoolXTarifasAux("codigoEsquemaTarifario_"+indexKeyCodigoMapaMod)+""+"] " +
			"\n*  % Participación ["+forma.getMapaPoolXTarifasAux("porcentajeParticipacion_"+indexKeyCodigoMapaMod)+"] " +
			"\n*  Valor Participacion ["+forma.getMapaPoolXTarifasAux("valorParticipacion_"+indexKeyCodigoMapaMod)+"] " +
			"\n*  Cuenta Pool ["+tempoCuentaPool+"] " +
			"\n*  Cuenta Institución ["+tempoCuentaInstitucion+"] " +
			"\n*  Cuenta Vigencia Anterior Institución ["+tempoCuentaInstitucionAnterior+"] " +
			""  ;
			log+="\n========================================================\n\n\n " ;	
			
			LogsAxioma.enviarLog(ConstantesBD.logParticipacionPoolXTarifasCodigo, log, ConstantesBD.tipoRegistroLogModificacion,usuario.getInformacionGeneralPersonalSalud()); 
	    }
	    else
	    {
	        tempoCuentaPool= (forma.getMapaPoolXTarifasNoModificado("cuentaPool_"+indexKeyCodigoMapaMod)+"");
		    tempoCuentaInstitucion=(forma.getMapaPoolXTarifasNoModificado("cuentaInstitucion_"+indexKeyCodigoMapaMod)+"");
		    tempoCuentaInstitucionAnterior=(forma.getMapaPoolXTarifasNoModificado("cuentaInstitucionAnterior_"+indexKeyCodigoMapaMod)+"");
		    
		    if(tempoCuentaPool.equals(ConstantesBD.codigoNuncaValido+""))
		        tempoCuentaPool="";
		    if(tempoCuentaInstitucion.equals(ConstantesBD.codigoNuncaValido+""))
		        tempoCuentaInstitucion="";
		    if(tempoCuentaInstitucionAnterior.equals(ConstantesBD.codigoNuncaValido+""))
		        tempoCuentaInstitucionAnterior="";
	        
	        log="\n            ====INFORMACION ORIGINAL PARTICIPACION POOL X TARIFAS ELIMINADO===== " +
			"\n*  Código Pool [" +forma.getCodigoPool() +"] "+
			"\n*  Código Esquema Tarifario ["+forma.getMapaPoolXTarifasNoModificado("codigoEsquemaTarifario_"+indexKeyCodigoMapaMod)+"] " +
			"\n*  % Participación ["+forma.getMapaPoolXTarifasNoModificado("porcentajeParticipacion_"+indexKeyCodigoMapaMod)+"] " +
			"\n*  Valor participacion ["+forma.getMapaPoolXTarifasNoModificado("valorParticipacion_"+indexKeyCodigoMapaMod)+"] " +
			"\n*  Cuenta Pool ["+tempoCuentaPool+"] " +
			"\n*  Cuenta Institución ["+tempoCuentaInstitucion+"] " +
			"\n*  Cuenta Vigencia Anterior Institución ["+tempoCuentaInstitucionAnterior+"] " +
			""  ;
	        log+="\n========================================> FUE ELIMINADO\n\n\n " ;	
			
			LogsAxioma.enviarLog(ConstantesBD.logParticipacionPoolXTarifasCodigo, log, ConstantesBD.tipoRegistroLogEliminacion,usuario.getInformacionGeneralPersonalSalud());
	    }
	}
	
	
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * ingresarNuevoElementoMapa.
	 * 
	 * @param ParticipacionPoolXTarifasForm forma
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal 
	 * @throws SQLException
	 */
	private ActionForward accionIngresarNuevoElementoMapa(		ParticipacionPoolXTarifasForm forma, 
	        																					Connection con,
																								HttpServletRequest request,
																								HttpServletResponse response, UsuarioBasico usuario) throws Exception
	{
		 int tempoTamanioRealMapa=forma.getNumeroRealFilasMapa();
		   
		 if (forma.getTipoParame().equals(ConstantesIntegridadDominio.acronimoTarifario))
		 {
			forma.setMapaPoolXTarifas("codigoEsquemaTarifario_"+tempoTamanioRealMapa,"-1");
		    forma.setMapaPoolXTarifas("nombreEsquemaTarifario_"+tempoTamanioRealMapa,"Seleccione");
		    forma.setMapaPoolXTarifas("porcentajeParticipacion_"+tempoTamanioRealMapa,"");
		    forma.setMapaPoolXTarifas("cuentaPool_"+tempoTamanioRealMapa,ConstantesBD.codigoNuncaValido+"");
		    forma.setMapaPoolXTarifas("cuentaInstitucion_"+tempoTamanioRealMapa,ConstantesBD.codigoNuncaValido+"");
		    forma.setMapaPoolXTarifas("cuentaInstitucionAnterior_"+tempoTamanioRealMapa,ConstantesBD.codigoNuncaValido+"");
		    forma.setMapaPoolXTarifas("estaBD_"+tempoTamanioRealMapa,"f");
		    forma.setMapaPoolXTarifas("esEliminada_"+tempoTamanioRealMapa,"f");
		    forma.setMapaPoolXTarifas("usuario_"+tempoTamanioRealMapa,usuario.getLoginUsuario());
		    forma.setMapaPoolXTarifas("institucion_"+tempoTamanioRealMapa,usuario.getCodigoInstitucionInt());
		    forma.setMapaPoolXTarifas("valorParticipacion_"+tempoTamanioRealMapa,"");
		    forma.setMapaPoolXTarifas("numRegistros", Integer.parseInt(forma.getMapaPoolXTarifas("numRegistros").toString())+1);

		 }
		 else 
			 if (forma.getTipoParame().equals(ConstantesIntegridadDominio.acronimoConvenio))
			 {
				forma.setMapaPoolXTarifas("codigoConvenio_"+tempoTamanioRealMapa,"-1");
			    forma.setMapaPoolXTarifas("nombreConvenio_"+tempoTamanioRealMapa,"Seleccione");
			    forma.setMapaPoolXTarifas("porcentajeParticipacion_"+tempoTamanioRealMapa,"");
			    forma.setMapaPoolXTarifas("cuentaPool_"+tempoTamanioRealMapa,ConstantesBD.codigoNuncaValido+"");
			    forma.setMapaPoolXTarifas("cuentaInstitucion_"+tempoTamanioRealMapa,ConstantesBD.codigoNuncaValido+"");
			    forma.setMapaPoolXTarifas("cuentaInstitucionAnterior_"+tempoTamanioRealMapa,ConstantesBD.codigoNuncaValido+"");
			    forma.setMapaPoolXTarifas("estaBD_"+tempoTamanioRealMapa,"f");
			    forma.setMapaPoolXTarifas("esEliminada_"+tempoTamanioRealMapa,"f");
			    forma.setMapaPoolXTarifas("usuario_"+tempoTamanioRealMapa,usuario.getLoginUsuario());
			    forma.setMapaPoolXTarifas("institucion_"+tempoTamanioRealMapa,usuario.getCodigoInstitucionInt());
			    forma.setMapaPoolXTarifas("valorParticipacion_"+tempoTamanioRealMapa,"");
			    forma.setMapaPoolXTarifas("numRegistros", Integer.parseInt(forma.getMapaPoolXTarifas("numRegistros").toString())+1);
			 }
		    
		    
	    this.cerrarConexion(con);
	    
	    if(request.getParameter("ultimaPagina")==null)
	    {
	        if(forma.getNumeroRealFilasMapa()>(forma.getOffsetHash()+forma.maxPagesItemsHash))
		        forma.setOffsetHash(forma.getOffsetHash()+forma.maxPagesItemsHash);
	       logger.info("se fue por ultima pagina == null"+forma.getOffsetHash());
	       logger.info("numero real filas "+forma.getNumeroRealFilasMapa());
	       logger.info("offsethash "+forma.getOffsetHash());
	       logger.info("max filas "+forma.maxPagesItemsHash);
	       
	       
	        response.sendRedirect("participacionPoolXTarifas.jsp?pager.offset="+forma.getOffsetHash());
	    }
	    else
	    {    
		    String ultimaPagina=request.getParameter("ultimaPagina");
		    int posOffSet=ultimaPagina.indexOf("offset=")+7;
		    forma.setOffsetHash((Integer.parseInt(ultimaPagina.substring(posOffSet, ultimaPagina.length() ))));
		    
		    if(forma.getNumeroRealFilasMapa()>(forma.getOffsetHash()+forma.maxPagesItemsHash))
		        forma.setOffsetHash(forma.getOffsetHash()+forma.maxPagesItemsHash);
		   logger.info("se fue por ultima pagina"+forma.getOffsetHash()); 
		    response.sendRedirect(ultimaPagina.substring(0, posOffSet)+forma.getOffsetHash());
	    }
	    //return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),8,Integer.parseInt(forma.getMapaPoolXTarifas("numRegistros").toString()), response, request, "participacionPoolXTarifas.jsp",true);
	    return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * ingresarNuevoElementoMapa.
	 * 
	 * @param ParticipacionPoolXTarifasForm forma, 
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "requisitosPaciente.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEliminarElementoMapa(		ParticipacionPoolXTarifasForm forma, 
	    																				ActionMapping mapping,
																						Connection con,
																						HttpServletResponse response) throws Exception
	{
	    forma.setMapaPoolXTarifas("esEliminada_"+forma.getIndexMapa(),"t");
	    this.cerrarConexion(con);
	    
	    if(!forma.getLinkSiguiente().equals(""))
	        response.sendRedirect(forma.getLinkSiguiente());
	    else
	        return mapping.findForward("paginaPrincipal");		
		return null;
	}

	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezarListarConsultar.
	 * 
	 * @param forma ParticipacionPoolXTarifasForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward "listadoPoolXTarifas.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezarListarConsultar(	ParticipacionPoolXTarifasForm forma, 
																					 ActionMapping mapping, 
																					 Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		forma.reset();
		forma.resetMapa();   
		forma.setEstado("empezarListarConsultar");
		this.cerrarConexion(con);
		return mapping.findForward("paginaListar");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * listarConsultar
	 * @param forma ParticipacionPoolXTarifasForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward  a la página "listadoPoolXTarifas.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionListarConsultar(	ParticipacionPoolXTarifasForm forma, 
																		ActionMapping mapping,
																		Connection con,String estado,UsuarioBasico usuario) throws SQLException 
	{
		if(forma.getCodigoPool()!=ConstantesBD.codigoNuncaValido && 
				forma.getTipoParame().toString().equals(ConstantesIntegridadDominio.acronimoTarifario))
		{
			
		    ParticipacionPoolXTarifas mundo= new ParticipacionPoolXTarifas();
		    mundo.reset();
			forma.setEstado(estado);
			mundo.setCodigoPool(forma.getCodigoPool());
			mundo.setInstitucion(usuario.getCodigoInstitucionInt());
			forma.setCol(mundo.listadoParticipacionPoolXTarifas(con));
			
		}
		else
			if(forma.getCodigoPool()!=ConstantesBD.codigoNuncaValido && 
					forma.getTipoParame().toString().equals(ConstantesIntegridadDominio.acronimoConvenio))
			{
				forma.initConvenio(con);
				ParticipacionPoolXTarifas mundo= new ParticipacionPoolXTarifas();
				mundo.reset();
				mundo.setCodigoPool(forma.getCodigoPool());
				mundo.setInstitucion(usuario.getCodigoInstitucionInt());
				
				forma.setMapaPoolXTarifas(mundo.listadoMapaParticipacionPoolXConvenio(con));
				forma.setMapaPoolXTarifasNoModificado((HashMap)forma.getMapaPoolXTarifas().clone());
			
				
			}
		
		
		this.cerrarConexion(con);
		return mapping.findForward("paginaListar")	;	
	}	
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * busquedaAvanzada
	 * 
	 * @param forma ParticipacionPoolXTarifasForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward "busquedaAvanzada.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada(	ParticipacionPoolXTarifasForm forma, 
																				ActionMapping mapping, 
																				Connection con) throws SQLException
	{
		forma.setEstado("busquedaAvanzada");
		this.cerrarConexion(con);
		return mapping.findForward("paginaBusqueda");		
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * resultadoBusquedaAvanzada.
	 * 
	 * @param forma ParticipacionPoolXTarifasForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @param esActionIngresar, undica si el flujo debe ser para Ingreso o Consulta
	 * @return ActionForward a la página "resultadoBusqueda.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResultadoBusquedaAvanzada(		ParticipacionPoolXTarifasForm forma,  
																								ActionMapping mapping, 
																								HttpServletRequest request,
																								Connection con,
																								boolean esActionIngresar, UsuarioBasico usurio) throws SQLException
	{
	 logger.info("el vlaor de tipo de parametro "+forma.getTipoParame().toString());
		ParticipacionPoolXTarifas mundo= new ParticipacionPoolXTarifas();
		mundo.setInstitucion(usurio.getCodigoInstitucionInt());
	    if(forma.getCodigoPool()>=0)
	        mundo.setCodigoPool(forma.getCodigoPool());
	    if(forma.getCodigoEsquemaTarifario()>0)
	        mundo.setCodigoEsquemaTarifario(forma.getCodigoEsquemaTarifario());
	    
	    try
	    {
	        if(forma.getPorcentajeParticipacionString().equals(""))
	        {    
	            mundo.setPorcentajeParticipacion(-1);
	        }    
	        else
	        {    
	            mundo.setPorcentajeParticipacion(Double.parseDouble(forma.getPorcentajeParticipacionString()));
		        if(mundo.getPorcentajeParticipacion()>100 || mundo.getPorcentajeParticipacion()<0)
		        {
		            mundo.setPorcentajeParticipacion(-1);
			        ActionErrors errores = new ActionErrors();
			        errores.add("% participacion", new ActionMessage("errors.range", "El % participación debe ser entero o decimal y", "cero (0)", " cien (100)"));
					logger.warn("error en la actualización de los datos del pool X Tarifas");
					saveErrors(request, errores);	
					this.cerrarConexion(con);									
					return mapping.findForward("paginaBusqueda");
		        }
	        }   
	    }
	    catch(NumberFormatException e)
	    {
	        mundo.setPorcentajeParticipacion(-1);
	        ActionErrors errores = new ActionErrors();
	        errores.add("% participacion", new ActionMessage("errors.range", "El % participación debe ser entero o decimal y", "cero (0)", " cien (100)"));
			logger.warn("error en la actualización de los datos del pool X Tarifas");
			saveErrors(request, errores);	
			this.cerrarConexion(con);									
			return mapping.findForward("paginaBusqueda");
	    }
	    catch(NullPointerException e)
	    {
	        mundo.setPorcentajeParticipacion(-1);
	        ActionErrors errores = new ActionErrors();
	        errores.add("% participacion", new ActionMessage("errors.range", "El % participación debe ser entero o decimal y", "cero (0)", " cien (100)"));
			logger.warn("error en la actualización de los datos del pool X Tarifas");
			saveErrors(request, errores);	
			this.cerrarConexion(con);									
			return mapping.findForward("paginaBusqueda");
	    }
	    
	    /*Para el solo caso de consulta, en esta parte se trabaja es con collection*/
	    if(!esActionIngresar && forma.getTipoParame().toString().equals(ConstantesIntegridadDominio.acronimoTarifario))
	    {    
	    	logger.info("entre a tarifario consultar ");
		    forma.setCol(mundo.busquedaAvanzadaPoolXTarifas(con));
		   //iterando la coleccion para mostrar el log 
		    Iterator i = forma.getCol().iterator();
		    if (forma.getCol()==null)
		    	logger.info("la coleccion es null ");
		    else
			    while (i.hasNext())
			    {
			    	logger.info("valor --> "+(HashMap)i.next());
			    }
		    //-----------------------------------
			this.cerrarConexion(con);
			forma.setEstado("listarConsultar");
			return mapping.findForward("paginaListar");
	    }
	    else
	    	if(!esActionIngresar && forma.getTipoParame().toString().equals(ConstantesIntegridadDominio.acronimoConvenio))
		    { 
	    		logger.info("entre a convenio consultar");
	    		forma.setMapaPoolXTarifas("numRegistros",0);
	    		mundo.setCodigoPool(forma.getCodigoPool());
	    		mundo.setConvenio(forma.getCodigoConvenio());
	    		forma.setMapaPoolXTarifas(mundo.listadoMapaParticipacionPoolXConvenio(con));
	    		 this.cerrarConexion(con);
	    		 forma.setEstado("listarConsultar");
	    		return mapping.findForward("paginaListar");
		    }
		
			    
	    
	    
	    //aqui voy ****************************************************
	    /*Para el caso de Ingreso -Modificación- borrado de datos en esta parte se trabaha con Hash Map*/
	    else
	    	if(esActionIngresar && forma.getTipoParame().toString().equals(ConstantesIntegridadDominio.acronimoTarifario))
	 	    
		    {
	    		logger.info("entre a convenio ingresar ");
	    		forma.setOffsetHash(0);
		        
		        forma.setMapaPoolXTarifas(mundo.busquedaAvanzadaIngresoPoolXTarifas(con));
		        forma.setMapaPoolXTarifasNoModificado(mundo.busquedaAvanzadaIngresoPoolXTarifas(con));
		        forma.setMapaPoolXTarifasAux(mundo.datosNoCargadosConBusquedaAvanzada(con));
		        
		        this.cerrarConexion(con);
				return mapping.findForward("paginaPrincipal");	
	    	}
	    	else
	    		if(esActionIngresar && forma.getTipoParame().toString().equals(ConstantesIntegridadDominio.acronimoConvenio))
	    		{
	    			logger.info("entre a convenio ingresar");
	    			mundo.setCodigoPool(forma.getCodigoPool());
		    		mundo.setConvenio(forma.getCodigoConvenio());
		    		forma.setMapaPoolXTarifas(mundo.listadoMapaParticipacionPoolXConvenio(con));
		    		this.cerrarConexion(con);
		    		return mapping.findForward("paginaPrincipal");	
	    		}
	    return null;
	}
	
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * resumen
	 * @param forma ParticipacionPoolXTarifasForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward  a la página "paginaLimpieza.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionResumen		(	ParticipacionPoolXTarifasForm forma, 
																		ActionMapping mapping,
																		Connection con,String estado,UsuarioBasico usuario) throws SQLException 
	{
	    ParticipacionPoolXTarifas mundo= new ParticipacionPoolXTarifas();
		forma.setEstado(estado);
		forma.setOffset(0);
		forma.setOffsetHash(0);
		
		mundo.setCodigoPool(forma.getCodigoPool());
		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
		forma.setCol(mundo.listadoParticipacionPoolXTarifas(con));
		this.cerrarConexion(con);
		
		//esta página borra lo que esta en el request (offset) y esta carga el resumenPoolXTarifas.jsp
		return mapping.findForward("paginaLimpieza")	;	
	}	
	
	
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer
	 * un forward
	 * @param con Conexión con la fuente de datos
	 * @throws SQLException
	 */
	public void cerrarConexion (Connection con) throws SQLException
	{
		if (con!=null&&!con.isClosed())
		{
			UtilidadBD.closeConnection(con);
		}
	}

}
