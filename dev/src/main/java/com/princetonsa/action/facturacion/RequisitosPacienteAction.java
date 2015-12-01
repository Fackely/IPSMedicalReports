/*
 * @(#)RequisitosPacienteAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.facturacion.RequisitosPacienteForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.RequisitosPaciente;

/**
 *   Action, controla todas las opciones dentro del registro de
 *   requisitos paciente o requisitos radicacion, incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @version 1.0, Nov. 22, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class RequisitosPacienteAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(RequisitosPacienteAction.class);

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
			if(form instanceof RequisitosPacienteForm)
			{

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				RequisitosPacienteForm reqForm =(RequisitosPacienteForm)form;
				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				String estado=reqForm.getEstado(); 

				logger.warn("El estado Requisitos-------->"+estado);

				if(estado == null)
				{
					reqForm.reset();	
					reqForm.resetMapa();
					logger.warn("Estado no valido dentro del flujo de Requisitos Paciente - Radicacion (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					this.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return this.accionEmpezar(reqForm,mapping,usuario, con);
				}
				else if (estado.equals("refrescar"))
				{
					return this.accionRefrescar(reqForm,mapping,usuario, con);
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.cerrarConexion(con);
					response.sendRedirect(reqForm.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("ingresarNuevo"))
				{
					return this.accionIngresarNuevo(reqForm,request, response, con);
				}
				else if(estado.equals("eliminarDelMapa"))
				{
					return this.accionEliminarDelMapa(reqForm,mapping,con);
				}
				else if(estado.equals("guardar"))
				{
					return this.accionGuardar(reqForm,mapping,request,usuario,con);
				}
				else if(estado.equals("listarCollectionResumen") || estado.equals("listarCollectionConsulta") || estado.equals("refrescarListado"))
				{
					if(estado.equals("listarCollectionConsulta"))
					{
						reqForm.resetTipoRequisito();
					}
					return accionListarCollectionResumenOconsulta(	reqForm, mapping, request, usuario, con, estado);
				}
				else
				{
					reqForm.reset();
					reqForm.resetMapa();
					logger.warn("Estado no valido dentro del flujo de requisitos paciente - radicacion (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					this.cerrarConexion(con);
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
	 * guardar.
	 * 
	 * @param reqForm RequisitosPacienteForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "requisitosPaciente.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionGuardar (	RequisitosPacienteForm reqForm, 
																ActionMapping mapping,
																HttpServletRequest request, 
																UsuarioBasico usuario,
																Connection con) throws SQLException
	{
		boolean procesoRealizado = false;
	    if(guardarNuevosEnBD(reqForm,usuario,con)!=ConstantesBD.codigoNuncaValido)
	    	procesoRealizado=true;
	    else
	    	procesoRealizado=false;
	    
	    if(modificarRequisitosAntiguosBD(reqForm,usuario, con)!=ConstantesBD.codigoNuncaValido)
	    	procesoRealizado=true;
	    else
	    	procesoRealizado=false;
	    
	    if(procesoRealizado)
	    	reqForm.setProcesoRealizado(ConstantesBD.acronimoSi);
    	else
    		reqForm.setProcesoRealizado(ConstantesBD.acronimoNo);
	    reqForm.setEstado("listarCollectionResumen");
	    return this.accionListarCollectionResumenOconsulta(reqForm, mapping, request, usuario, con, "listarCollectionResumen"); 
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * listarCollectionResumen - listarCollectionConsulta
	 * @param reqForm RequisitosPacienteForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward  a la página "listadoRequisitosResumen.jsp" - "listadoRequisitosConsulta.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionListarCollectionResumenOconsulta(	RequisitosPacienteForm reqForm, 
																									ActionMapping mapping,
																									HttpServletRequest request, 
																									UsuarioBasico usuario,
																									Connection con, 
																									String estado) throws SQLException 
	{
		RequisitosPaciente mundo= new RequisitosPaciente();
		mundo.setCodigoInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
		reqForm.setEstado(estado);
		if(reqForm.getTipoRequisitos().equals("Paciente"))
		{    
		    reqForm.setCol(mundo.listadoRequisitosCollection(con));
		}
		else if(reqForm.getTipoRequisitos().equals("Radicacion"))
		{
		    reqForm.setCol(mundo.listadoRequisitosRadicacionCollection(con));
		}
		this.cerrarConexion(con);
		
		if(estado.equals("listarCollectionResumen"))
		    return mapping.findForward("paginaListarCollectionResumen");
		else if(estado.equals("listarCollectionConsulta") || estado.equals("refrescarListado"))
		    return mapping.findForward("paginaListarCollectionConsulta");
		else //no esta dentro del flujo entonces saque error
		{
			reqForm.reset();
			reqForm.resetTipoRequisito();
			reqForm.resetMapa();
			logger.warn("Estado no valido dentro del flujo de requisitos paciente (null) ");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			this.cerrarConexion(con);
			return mapping.findForward("paginaError");
		}
	}	
	
	/**
	 * Método que modifica los valores de la tabla requisitos paciente o requisitos radicacion (Descripción - Activo)
	 * 
	 * @param reqForm
	 * @param usuario
	 * @param con
	 * @throws SQLException
	 */
	private int modificarRequisitosAntiguosBD(	RequisitosPacienteForm reqForm, 
	        															UsuarioBasico usuario,
																		Connection con)
    {
	    RequisitosPaciente mundo= new RequisitosPaciente();
	    //en este punto se carga el mapa solo con los valores que han sido modificados
	    int pudoMod=ConstantesBD.codigoNuncaValido;
	    boolean band = false;
	    reqForm.setMapaRequisitos(reqForm.comparar2HashMap());
	    if(reqForm.getNumeroRealFilasMapa()>0)
	    {
	        for(int k=0; k<reqForm.getNumeroRealFilasMapaNoMod();k++)
	        {
	            
	            String tempoKeyCod=reqForm.getMapaRequisitos("codigo_"+k)+"";
	            if(tempoKeyCod!=null && !tempoKeyCod.equals("") && !tempoKeyCod.equals("null") && !tempoKeyCod.equals("vacio"))
	            {
	            	try
	            	{
		                mundo.setCodigoRequisito(Integer.parseInt(reqForm.getMapaRequisitos("codigo_"+k)+""));
		                mundo.setDescripcionRequisito(reqForm.getMapaRequisitos("descripcion_"+k)+"");
		                mundo.setTipoRequisito(reqForm.getMapaRequisitos("tipo_requisito_"+k)+"");
		                mundo.setActivoRequisito(UtilidadTexto.getBoolean(reqForm.getMapaRequisitos("activo_"+k).toString()));
			            
		                
		                if(reqForm.getTipoRequisitos().equals("Paciente"))
		                    pudoMod=mundo.modificarRequisitosPaciente(con);
		                else if(reqForm.getTipoRequisitos().equals("Radicacion"))
		                    pudoMod=mundo.modificarRequisitosRadicacion(con);
		                
		                logger.info("¿pudo modificar N° "+k+"? "+ pudoMod);
		                
		                if(pudoMod>0)
		                {
		                	
		                    generarLogModificacion(reqForm, k, usuario);
		                }
	            	}
	            	catch(SQLException e)
	            	{
	            		e.printStackTrace();
	            	}
	            	if(!band)
		            	band=true;
	            }
	        }
	        if(band)
	        	return pudoMod;
	        else
	        	return 0;
	    }
	    return pudoMod;
    }
	
	/**
	 * Método que genera los Logs de Modificación de los requisitos de un paciente
	 * o los requisitos de radicacion
	 * @param reqForm, Forma
	 * @param indexKeyCodigoMapaMod, indice de la llave.
	 * @param usuario, user
	 */
	private void generarLogModificacion(	RequisitosPacienteForm reqForm, int indexKeyCodigoMapaMod, UsuarioBasico usuario)
	{
	    String log="";
	    if(reqForm.getTipoRequisitos().equals("Paciente"))
	        log="\n            ====INFORMACION ORIGINAL REQUISITOS PACIENTE===== " ;
	    else if(reqForm.getTipoRequisitos().equals("Radicacion"))
	        log="\n            ====INFORMACION ORIGINAL REQUISITOS RADICACION===== " ;
	    
		log+="\n*  Código [" +reqForm.getMapaRequisitosNoModificado("codigo_"+indexKeyCodigoMapaMod) +"] "+
		"\n*  Descripción ["+reqForm.getMapaRequisitosNoModificado("descripcion_"+indexKeyCodigoMapaMod)+"] "  +
		"\n*  Clasificación ["+ValoresPorDefecto.getIntegridadDominio(reqForm.getMapaRequisitosNoModificado("tipo_requisito_"+indexKeyCodigoMapaMod).toString())+"] "  ;
		
		if(UtilidadTexto.getBoolean(reqForm.getMapaRequisitosNoModificado("activo_"+indexKeyCodigoMapaMod).toString()))
			log += "\n*  Activa [ SI ]";
		else
			log += "\n*  Activa [ NO ]";
		
		if(reqForm.getTipoRequisitos().equals("Paciente"))
		    log+="\n          =====INFORMACION DESPUES DE LA MODIFICACION REQUISITOS PACIENTE===== ";
		else if(reqForm.getTipoRequisitos().equals("Radicacion"))
		    log+="\n          =====INFORMACION DESPUES DE LA MODIFICACION REQUISITOS RADICACION===== ";
		log+="\n*  Código [" +reqForm.getMapaRequisitos("codigo_"+indexKeyCodigoMapaMod) +"] "+
		"\n*  Descripción ["+reqForm.getMapaRequisitos("descripcion_"+indexKeyCodigoMapaMod)+"] "  +
		"\n*  Clasificación ["+ValoresPorDefecto.getIntegridadDominio(reqForm.getMapaRequisitos("tipo_requisito_"+indexKeyCodigoMapaMod).toString())+"] "  ;
		
		if(UtilidadTexto.getBoolean(reqForm.getMapaRequisitos("activo_"+indexKeyCodigoMapaMod).toString()))
			log += "\n*  Activa [ SI ]";
		else
			log += "\n*  Activa [ NO ]";
		log+="\n========================================================\n\n\n " ;	
		
		if(reqForm.getTipoRequisitos().equals("Paciente"))
		    LogsAxioma.enviarLog(ConstantesBD.logRequisitosPacienteCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getInformacionGeneralPersonalSalud());
		else if(reqForm.getTipoRequisitos().equals("Radicacion"))
		    LogsAxioma.enviarLog(ConstantesBD.logRequisitosRadicacionCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getInformacionGeneralPersonalSalud());
	}
	
	/**
	 * Método que comparando el HashMap Original y el modificado,
	 * entonces inserta en BD los que en el hash Map tienen como key del
	 * codigo= 'vacio', ya que esto indica que son nuevos debido a que los originales 
	 * que estan en BD tienen un cod de sequence.
	 * @param reqForm
	 * @param usuario
	 * @param con
	 * @throws SQLException
	 */
	private int guardarNuevosEnBD(	RequisitosPacienteForm reqForm, 
														UsuarioBasico usuario,
														Connection con) throws SQLException
	{
	    RequisitosPaciente mundo= new RequisitosPaciente();
	    mundo.setCodigoInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
	    int resp = ConstantesBD.codigoNuncaValido;
	    /*Si es mayor entonces es ue hay nuevos para insertar*/
	    if(reqForm.getMapaRequisitos().size()>reqForm.getMapaRequisitosNoModificado().size())
	    {
	        for(int k=0; k<reqForm.getNumeroRealFilasMapa(); k++)
	        {
	            if((reqForm.getMapaRequisitos("codigo_"+k)+"").equals("vacio"))
	            {
	                mundo.setActivoRequisito(UtilidadTexto.getBoolean(reqForm.getMapaRequisitos("activo_"+k).toString()));
		            
		            
		            mundo.setDescripcionRequisito(reqForm.getMapaRequisitos("descripcion_"+k)+"");
		            
		            mundo.setTipoRequisito(reqForm.getMapaRequisitos("tipo_requisito_"+k)+"");
		            
		            if(reqForm.getTipoRequisitos().equals("Paciente"))
		            	resp=mundo.insertarRequisitosPaciente(con);
		            else if(reqForm.getTipoRequisitos().equals("Radicacion"))
		            	resp=mundo.insertarRequisitosRadicacion(con);
	            }
	        }
	    }else
	    	return 0;
	    return resp;
	}

	/**
	 * Este método especifica las acciones a realizar en el estado
	 * eliminarDelMapa.
	 * 
	 * @param reqForm RequisitosPacienteForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "requisitosPaciente.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEliminarDelMapa(	RequisitosPacienteForm reqForm, 
																			ActionMapping mapping,
																			Connection con) throws SQLException
	{
	    int tamanioMapaAntesEliminar=reqForm.getNumeroRealFilasMapa();
	    reqForm.setProcesoRealizado(ConstantesBD.acronimoNo);
	    reqForm.getMapaRequisitos().remove("codigo_"+reqForm.getKeyDelete());
	    reqForm.getMapaRequisitos().remove("descripcion_"+reqForm.getKeyDelete());
	    reqForm.getMapaRequisitos().remove("tipo_requisito_"+reqForm.getKeyDelete());
	    reqForm.getMapaRequisitos().remove("activo_"+reqForm.getKeyDelete());
	    reqForm.getMapaRequisitos().remove("puedoborrar_"+reqForm.getKeyDelete());
	    
	    /*El siguiente cod, lo que verifica, es que si eliminan un elemento del mapa y atrás de el existen más datos entonces
	     * se debe correr el index del hash Map de los que estan después del eliminado una posición menos para que existe 
	     * concordancia entre el size real del mapa y el index de elementos */
	    int indexEliminado= Integer.parseInt(reqForm.getKeyDelete().substring(reqForm.getKeyDelete().indexOf("_")+1));
	    
	    if(tamanioMapaAntesEliminar>indexEliminado)
	    {
	        for(int k=indexEliminado; k<tamanioMapaAntesEliminar; k++)
	        {
	            reqForm.setMapaRequisitos("codigo_"+k, reqForm.getMapaRequisitos("codigo_"+(k+1)));
	            reqForm.setMapaRequisitos("descripcion_"+k, reqForm.getMapaRequisitos("descripcion_"+(k+1)));
	            reqForm.setMapaRequisitos("tipo_requisito_"+k, reqForm.getMapaRequisitos("tipo_requisito_"+(k+1)));
	            reqForm.setMapaRequisitos("activo_"+k, reqForm.getMapaRequisitos("activo_"+(k+1)));
	            reqForm.setMapaRequisitos("puedoborrar_"+k, reqForm.getMapaRequisitos("puedoborrar_"+(k+1)));
	        }
	        reqForm.getMapaRequisitos().remove("codigo_"+(tamanioMapaAntesEliminar-1));
		    reqForm.getMapaRequisitos().remove("descripcion_"+(tamanioMapaAntesEliminar-1));
		    reqForm.getMapaRequisitos().remove("tipo_requisito_"+(tamanioMapaAntesEliminar-1));
		    reqForm.getMapaRequisitos().remove("activo_"+(tamanioMapaAntesEliminar-1));
		    reqForm.getMapaRequisitos().remove("puedoborrar_"+(tamanioMapaAntesEliminar-1));
	    }
	    reqForm.setMapaRequisitos("numRegistros",(tamanioMapaAntesEliminar-1));
	    
	    reqForm.reset();
	    this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");	
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * ingresarNuevo.
	 * 
	 * @param reqForm RequisitosPacienteForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "requisitosPaciente.jsp"
	 * @throws SQLException
	 * @throws Exception
	 */
	private ActionForward accionIngresarNuevo(	RequisitosPacienteForm reqForm, 
																		HttpServletRequest request,
																		HttpServletResponse response,
																		Connection con) throws SQLException, Exception
	{
	    int tempoTamanioRealMapa=reqForm.getNumeroRealFilasMapa();
	    reqForm.setProcesoRealizado(ConstantesBD.acronimoNo);
	    reqForm.setMapaRequisitos("codigo_"+tempoTamanioRealMapa, "vacio");
	    reqForm.setMapaRequisitos("descripcion_"+tempoTamanioRealMapa, reqForm.getDescripcion());
	    reqForm.setMapaRequisitos("tipo_requisito_"+tempoTamanioRealMapa, "");
	    reqForm.setMapaRequisitos("activo_"+tempoTamanioRealMapa,ConstantesBD.acronimoSi);
	    reqForm.setMapaRequisitos("puedoborrar_"+tempoTamanioRealMapa, "t");
	    tempoTamanioRealMapa++;
	    reqForm.setMapaRequisitos("numRegistros", tempoTamanioRealMapa+"");
	    reqForm.reset();
	    
	    if(request.getParameter("ultimaPagina")==null)
	    {
	        if(reqForm.getNumeroRealFilasMapa()>(reqForm.getOffsetHash()+reqForm.maxPagesItemsHash))
	            reqForm.setOffsetHash(reqForm.getOffsetHash()+reqForm.maxPagesItemsHash);
	        this.cerrarConexion(con);
	        
	        response.sendRedirect("requisitosPaciente.jsp?pager.offset="+reqForm.getOffsetHash());
	    }
	    else
	    {    
		    String ultimaPagina=request.getParameter("ultimaPagina");
		    int posOffSet=ultimaPagina.indexOf("offset=")+7;
		    reqForm.setOffsetHash((Integer.parseInt(ultimaPagina.substring(posOffSet, ultimaPagina.length() ))));
		    
		    if(reqForm.getNumeroRealFilasMapa()>(reqForm.getOffsetHash()+reqForm.maxPagesItemsHash))
		        reqForm.setOffsetHash(reqForm.getOffsetHash()+reqForm.maxPagesItemsHash);
		    
		    this.cerrarConexion(con);
		    response.sendRedirect(ultimaPagina.substring(0, posOffSet)+reqForm.getOffsetHash());
	    }
	    return null;
	    
	    //this.cerrarConexion(con);
		//return mapping.findForward("paginaPrincipal");	
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezar.
	 * 
	 * @param reqForm RequisitosPacienteForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "requisitosPaciente.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(	RequisitosPacienteForm reqForm, 
																ActionMapping mapping,
																UsuarioBasico usuario,
																Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		reqForm.reset();
		reqForm.resetTipoRequisito();
		reqForm.resetMapa();
		reqForm.setEstado("empezar");
		RequisitosPaciente mundo= new RequisitosPaciente();
		
		mundo.setCodigoInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
		
		if(reqForm.getTipoRequisitos().equals("Paciente"))
		{    
		    reqForm.setMapaRequisitos(mundo.listadoRequisitos(con));
		    reqForm.setMapaRequisitosNoModificado(mundo.listadoRequisitos(con));
		}    
		else if(reqForm.getTipoRequisitos().equals("Radicacion"))
		{    
		    reqForm.setMapaRequisitos(mundo.listadoRequisitosRadicacion(con));
		    reqForm.setMapaRequisitosNoModificado(mundo.listadoRequisitosRadicacion(con));
		}
		
		this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * refrescar. Este a diferencia del empezar lo que hace es un refresh 
	 * de la pagina pero sin borrar el tipo de requisito.
	 * 
	 * @param reqForm RequisitosPacienteForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "requisitosPaciente.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionRefrescar(	RequisitosPacienteForm reqForm, 
																ActionMapping mapping,
																UsuarioBasico usuario,
																Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		reqForm.reset();
		reqForm.resetMapa();
		reqForm.setEstado("empezar");
		RequisitosPaciente mundo= new RequisitosPaciente();
		
		mundo.setCodigoInstitucion(Integer.parseInt(usuario.getCodigoInstitucion()));
		
		if(reqForm.getTipoRequisitos().equals("Paciente"))
		{  
			reqForm.setMapaRequisitos(mundo.listadoRequisitos(con));
			reqForm.setMapaRequisitosNoModificado(mundo.listadoRequisitos(con));
		}
		else if(reqForm.getTipoRequisitos().equals("Radicacion"))
		{    
		    reqForm.setMapaRequisitos(mundo.listadoRequisitosRadicacion(con));
		    reqForm.setMapaRequisitosNoModificado(mundo.listadoRequisitosRadicacion(con));
		}
		this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");		
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