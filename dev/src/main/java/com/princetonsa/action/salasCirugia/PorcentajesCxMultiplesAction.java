/*
 * Sep 09 / 2005
 */
package com.princetonsa.action.salasCirugia;

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
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.salasCirugia.PorcentajesCxMultiplesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.salasCirugia.PorcentajesCxMultiples;

/**
 * @author Sebastián Gómez R
 * @author (Modificado Nov 2007) Jose Eduardo Arias Doncel
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Parametrización de PorcentajesCxMultiples
 */
public class PorcentajesCxMultiplesAction extends Action {
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(PorcentajesCxMultiplesAction.class);
	
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
			if(form instanceof PorcentajesCxMultiplesForm)
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
				PorcentajesCxMultiplesForm porcentajesForm =(PorcentajesCxMultiplesForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=porcentajesForm.getEstado(); 
				ActionErrors errores = new ActionErrors();
				logger.warn("estado Porcentajes-->"+estado);


				if(estado == null)
				{
					porcentajesForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Porcentajes Cirugías Múltiples (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				//*******ESTADOS DE INGRESAR/MODIFICAR PORCENTAJES**********************
				//Estado de inicial del enbezado 
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,porcentajesForm,mapping,usuario);			
				}
				//Estado para mostrar la informacion inicial dependiendo del tipo de ingreso 
				else if(estado.equals("mostrarIngreso"))
				{
					if(porcentajesForm.getIndicadorIngreso().equals("convenio") && 
							!porcentajesForm.getConvenioMap("codigo").toString().equals(ConstantesBD.codigoNuncaValido+""))
						return accionCargarInformacionVigencia(con, porcentajesForm, usuario, mapping);
					if(porcentajesForm.getIndicadorIngreso().equals("esquema") && 
							!porcentajesForm.getEsquemaTarifario("codigo").toString().equals(ConstantesBD.codigoNuncaValido+""))
						return accionCargarInformacionDetalleEsquema(con, porcentajesForm, usuario, mapping);

					porcentajesForm.setPorcentajes(new HashMap());
					porcentajesForm.setVigenciasMap(new HashMap());
					return mapping.findForward("principal");
				}			
				else if (estado.equals("nuevo"))
				{
					return accionNuevo(con,porcentajesForm,mapping,request,usuario,response);
				}
				else if (estado.equals("nuevoConvenio"))
				{				
					return accionNuevoVigencia(con, porcentajesForm, mapping, request, usuario, response);
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con,porcentajesForm,mapping,usuario,request);
				}
				else if (estado.equals("guardarConvenio"))
				{
					errores = accionValidarGuardarConvenio(porcentajesForm,errores);

					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						return mapping.findForward("convenio");					
					}

					accionGuardarConvenio(con,porcentajesForm,mapping,usuario,request);				
					return mapping.findForward("convenio");		
				}
				else if(estado.equals("eliminar"))
				{
					return accionEliminar(con,porcentajesForm,mapping,usuario,request);
				}
				else if(estado.equals("eliminarConvenio"))
				{
					return accionEliminarConvenio(con, porcentajesForm, mapping, usuario, request);
				}
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,porcentajesForm,response,mapping,request);
				}
				else if(estado.equals("mostrarDetalle"))
				{
					return accionCargarInformacionDetalleEsquema(con, porcentajesForm, usuario, mapping);
				}

				//*****ESTADOS DE CONSULTAR PORCENTAJES*************************************
				else if(estado.equals("consultar"))
				{
					return accionEmpezar(con,porcentajesForm,mapping,usuario);			
				}		
				else if(estado.equals("ordenar"))
				{
					return accionOrdenarDetalle(con,porcentajesForm,mapping,request);
				}
				else if(estado.equals("ordenarVigencia"))
				{
					return accionOrdenarConvenio(con,porcentajesForm,mapping,request);
				}			
				else if(estado.equals("buscar"))
				{
					return accionConsultarAvanzada(con, porcentajesForm, usuario, mapping);
				}
				//*******************************************************************************
				else
				{
					porcentajesForm.reset();
					logger.warn("Estado no valido dentro del flujo de Porcentajes Cirugías Múltiples (null) ");
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
	
	
	//**************************************************************************************************************
	//Metodos*******************************************************************************************************
	
	
	/**
	 * Método inicial que carga los datos del encabezadp
	 * de la institución
	 * @param con
	 * @param porcentajesForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(
			Connection con, 
			PorcentajesCxMultiplesForm forma, 
			ActionMapping mapping, 
			UsuarioBasico usuario) 
	{
		//se reinicia información
		String estado=forma.getEstado();
		forma.reset();
		
		//actualiza el estado 
		forma.setEstado(estado);
								
		//Toma la informacion de esquemas tarifarios
		forma.setEsquemasTarifariosArray(Utilidades.obtenerEsquemasTarifariosGenPartInArray(con,usuario.getCodigoInstitucionInt(),"porcentajescx"));
		//Toma la informacion de los convenio
		forma.setConveniosArray(Utilidades.obtenerConvenios(con, "","",false,"",false));
		
		//Inicializa los valores del encabezado
		forma.setConvenioMap("codigo",ConstantesBD.codigoNuncaValido+"");
		forma.setConvenioMap("nombre_convenio","");
		forma.setFechaFinal("");
		forma.setFechaInicial("");				
		
		forma.setEsquemaTarifario("codigo",ConstantesBD.codigoNuncaValido+"");
		forma.setEsquemaTarifario("tipo_esquema",ConstantesBD.codigoNuncaValido+"");
		forma.setEsquemaTarifario("nombre_esquema","");
		
		
		//Inicializa los valores del HashMap de la Busqueda
		forma.setMapaBusqueda("tipo_servicio",ConstantesBD.codigoNuncaValido+"");
		forma.setMapaBusqueda("tipo_cirugia",ConstantesBD.codigoNuncaValido+"");
		forma.setMapaBusqueda("tipo_asocio",ConstantesBD.codigoNuncaValido+"");
		forma.setMapaBusqueda("tipo_especialista",ConstantesBD.codigoNuncaValido+"");
		forma.setMapaBusqueda("via_acceso",ConstantesBD.codigoNuncaValido+"");
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");		
	}
	
	
	/**
	 * Cargar informcion del Esquema o del Detalle del Convenio  
	 * @param Connection con
	 * @param PorcentajesCxMultiplesForm forma
	 * @param UsuarioBasico usuario
	 * @param ActionMapping mapping 
	 * */
	private ActionForward accionCargarInformacionDetalleEsquema(Connection con,
			PorcentajesCxMultiplesForm forma,
			UsuarioBasico usuario, 
			ActionMapping mapping)
	{
		
		//*****Carga la informacion general del detalle
		PorcentajesCxMultiples porcen = new PorcentajesCxMultiples();
		HashMap parametros = new HashMap();
		
		//resetea los parametros del pager
		forma.setNumRegistros(0);
		forma.setOffsetHash(0);
			
		//Tipo Cirugia, se carga desde la JSP
		//Tipo Especialista se carga desde la JSP
		//Via de Acceso se carga desde la JSP		
		
		//Consulta el tipo de servicio
		forma.setTiposServicioArray(PorcentajesCxMultiples.listarTiposServicio(con));		
		
		//Tipos Salas 
		forma.setTiposSalaArray(Utilidades.obtenerTiposSala(con,usuario.getCodigoInstitucionInt(),"","f"));
		
		//********Valida el ingreso a la funcionalidad
				
		
		//Por el Convenio
		if(forma.getIndicadorIngreso().equals("convenio"))
		{						
			parametros.put("codigo_encab",forma.getVigenciasMap("codigo_encab_"+forma.getIndiceVigencias()));			
		}//Por el esquema
		else if(forma.getIndicadorIngreso().equals("esquema") 
				&& !forma.getEsquemaTarifario("codigo").toString().equals(ConstantesBD.codigoNuncaValido+""))
		{
			String []cadena = forma.getEsquemaTarifario("codigo").toString().split(ConstantesBD.separadorSplit);
			
			forma.setEsquemaTarifario("codigo_tari",cadena[0]);
			forma.setEsquemaTarifario("tipo_esquema",cadena[1]);
			forma.setEsquemaTarifario("codigo_encab",cadena[2]);	
			forma.setEsquemaTarifario("institucion",usuario.getCodigoInstitucionInt());
			
			
			parametros= forma.getEsquemaTarifario();
		}	
		
		//carga la informacion del Detalle
		logger.info("cargar resumen");
		forma.setPorcentajes(porcen.cargarPorcentajes(con, usuario.getCodigoInstitucionInt(),parametros));
		forma.setNumRegistros(Utilidades.convertirAEntero(forma.getPorcentajes("numRegistros").toString()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	
	/**
	 * Carga la Informacion de las Vigencias por convenio
	 * @param Connection con
	 * @param PorcentajesCxMultiplesForm forma
	 * @param UsuarioBasico usuario
	 * @param ActionMapping mapping
	 * */
	private ActionForward accionCargarInformacionVigencia(
			Connection con,
			PorcentajesCxMultiplesForm forma,
			UsuarioBasico usuario, 
			ActionMapping mapping)
	{
		//*********Carga la Informacion de las vigencias por convenio
		PorcentajesCxMultiples porcen = new PorcentajesCxMultiples();
		HashMap parametros = new HashMap();
		
		//resetea los parametros del pager
		forma.setNumRegistros(0);
		forma.setOffsetHash(0);
		
		forma.setIndiceVigencias("");
		
		parametros.put("convenio",forma.getConvenioMap("codigo"));
		forma.setVigenciasMap(porcen.cargarEncaPorcentajes(con,usuario.getCodigoInstitucionInt(), parametros));	
		forma.setNumRegistros(Utilidades.convertirAEntero(forma.getVigenciasMap("numRegistros").toString()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("convenio");
	}
	
	
	/**
	 * Método usado para insertar un nuevo elemento en el listado de porcentajes Cx Multiples
	 * @param con
	 * @param porcentajesForm
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionNuevo(Connection con, 
			PorcentajesCxMultiplesForm porcentajesForm, 
			ActionMapping mapping, 
			HttpServletRequest request, 
			UsuarioBasico usuario, 
			HttpServletResponse response) 
	{
			try
			{				
			 	int pos=porcentajesForm.getNumRegistros();
			 	boolean nuevo = false;
			 			 
			 	for(int i=0; i<pos && !nuevo; i++)
			 	{
			 		if(porcentajesForm.getPorcentajes("estabd_"+i).toString().equals(ConstantesBD.acronimoNo))
			 			nuevo = true;			 			
			 	}
			 	
			 	//Valida que no exista un registro nuevo
			 	if(!nuevo)
			 	{			 	
				    porcentajesForm.setPorcentajes("codigo_"+pos,"-1");
				    porcentajesForm.setPorcentajes("codigo_encab_"+pos,"-1");
				    //por defecto se toma un registro particular con código esquema 0 (inválido)			   
				    porcentajesForm.setPorcentajes("tipo_cirugia_"+pos,"0");
				    porcentajesForm.setPorcentajes("tipo_asocio_"+pos,"0");
				    porcentajesForm.setPorcentajes("tipo_especialista_"+pos,ConstantesBD.codigoNuncaValido+"");			    
				    porcentajesForm.setPorcentajes("via_acceso_"+pos,ConstantesBD.codigoNuncaValido+"");
				    porcentajesForm.setPorcentajes("liquidacion_"+pos,"");
				    porcentajesForm.setPorcentajes("adicional_"+pos,"");
				    porcentajesForm.setPorcentajes("politra_"+pos,"");			    
				    
				    porcentajesForm.setPorcentajes("tipo_servicio_"+pos,"");
				    porcentajesForm.setPorcentajes("tipo_sala_"+pos,"");
				    porcentajesForm.setPorcentajes("estabd_"+pos,ConstantesBD.acronimoNo);			    
				    
				    
				    //se aumenta tamaño del listado
				    pos++;
				    porcentajesForm.setNumRegistros(pos);
				    porcentajesForm.setPorcentajes("numRegistros",pos+"");
			 	}
			    
			    UtilidadBD.cerrarConexion(con);
			    int maxPageItems=Utilidades.convertirAEntero(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt()));
			    if(request.getParameter("ultimaPagina")==null)
			    {
			        if(porcentajesForm.getNumRegistros()>(porcentajesForm.getOffsetHash()+maxPageItems))
				        porcentajesForm.setOffsetHash(porcentajesForm.getOffsetHash()+maxPageItems);
			        
			        response.sendRedirect("../ingresarPorcentajesCxMultiples/ingresarPorcentajesCxMultiples.jsp?pager.offset="+porcentajesForm.getOffsetHash());
			    }
			    else
			    {    
				    String ultimaPagina=request.getParameter("ultimaPagina");
				    int posOffSet=ultimaPagina.indexOf("offset=")+7;
				    porcentajesForm.setOffsetHash((Utilidades.convertirAEntero(ultimaPagina.substring(posOffSet, ultimaPagina.length() ))));
				    
				    if(porcentajesForm.getNumRegistros()>(porcentajesForm.getOffsetHash()+maxPageItems))
				        porcentajesForm.setOffsetHash(porcentajesForm.getOffsetHash()+maxPageItems);
				    
				    response.sendRedirect(ultimaPagina.substring(0, posOffSet)+porcentajesForm.getOffsetHash());
			    }
			    return null;
			}
		    catch(Exception e)
			{
				logger.error("Error en accionNuevo de PorcentajesCxMultiplesAction: "+e);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en PorcentajesCxMultiplesAction", "errors.problemasDatos", true);
			}
	}
	
	
	
	/**
	 * Método usado para insertar un nuevo elemento en el listado de porcentajes Cx Multiples
	 * @param con
	 * @param porcentajesForm
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @param response
	 * @return
	 */
	private ActionForward accionNuevoVigencia(Connection con, 
			PorcentajesCxMultiplesForm porcentajesForm, 
			ActionMapping mapping, 
			HttpServletRequest request, 
			UsuarioBasico usuario, 
			HttpServletResponse response) 
	{
			try
			{				
			 	int pos=porcentajesForm.getNumRegistros();
			 	boolean nuevo = false;
	 			 
			 	for(int i=0; i<pos && !nuevo; i++)
			 	{
			 		if(porcentajesForm.getVigenciasMap("estabd_"+i).toString().equals(ConstantesBD.acronimoNo))
			 			nuevo = true;			 			
			 	}
			 	
			 	//Valida que no exista un registro nuevo
			 	if(!nuevo)
			 	{	
				    porcentajesForm.setVigenciasMap("codigo_"+pos,"-1");
				    porcentajesForm.setVigenciasMap("codigo_encab_"+pos,"-1");
				    porcentajesForm.setVigenciasMap("esq_tar_particular_"+pos,"");
				    porcentajesForm.setVigenciasMap("esq_tar_general_"+pos,"");
				    porcentajesForm.setVigenciasMap("codigo_encab_"+pos,"-1");
				    porcentajesForm.setVigenciasMap("institucion_"+pos,usuario.getCodigoInstitucionInt());
				    porcentajesForm.setVigenciasMap("convenio_"+pos,porcentajesForm.getConvenioMap("codigo"));
				    porcentajesForm.setVigenciasMap("fecha_inicial_"+pos,"");
				    porcentajesForm.setVigenciasMap("fecha_final_"+pos,"");		    
				    porcentajesForm.setVigenciasMap("estabd_"+pos,ConstantesBD.acronimoNo);   
				    			    
				    //se aumenta tamaño del listado
				    pos++;
				    porcentajesForm.setNumRegistros(pos);
				    porcentajesForm.setVigenciasMap("numRegistros",pos+"");
			 	}
			 	else
			 	{
			 		ActionErrors error= new ActionErrors();
			 		error.add("descripcion",new ActionMessage("errors.notEspecific","Ya Existe un Registro Nuevo, debe almacenarlo para ingresar uno adicional"));
			 		saveErrors(request,error);
			 	}
			    
			    UtilidadBD.cerrarConexion(con);
			    int maxPageItems=Utilidades.convertirAEntero(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt()));
			    if(request.getParameter("ultimaPagina")==null)
			    {
			        if(porcentajesForm.getNumRegistros()>(porcentajesForm.getOffsetHash()+maxPageItems))
				        porcentajesForm.setOffsetHash(porcentajesForm.getOffsetHash()+maxPageItems);
			        
			        response.sendRedirect("../ingresarPorcentajesCxMultiples/ingresarPorcCxMultiplesConvenio.jsp?pager.offset="+porcentajesForm.getOffsetHash());
			    }
			    else
			    {    
				    String ultimaPagina=request.getParameter("ultimaPagina");
				    int posOffSet=ultimaPagina.indexOf("offset=")+7;
				    porcentajesForm.setOffsetHash((Utilidades.convertirAEntero(ultimaPagina.substring(posOffSet, ultimaPagina.length() ))));
				    
				    if(porcentajesForm.getNumRegistros()>(porcentajesForm.getOffsetHash()+maxPageItems))
				        porcentajesForm.setOffsetHash(porcentajesForm.getOffsetHash()+maxPageItems);
				    
				    response.sendRedirect(ultimaPagina.substring(0, posOffSet)+porcentajesForm.getOffsetHash());
			    }
			    return null;
			}
		    catch(Exception e)
			{
				logger.error("Error en accionNuevo de PorcentajesCxMultiplesConvenioAction: "+e);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en PorcentajesCxMultiplesAction", "errors.problemasDatos", true);
			}
	}
	
	
	
	
	
	/**
	 * Método usado para eliminar un registro del listado de porcentajes
	 * @param con
	 * @param porcentajesForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEliminar(
			Connection con, 
			PorcentajesCxMultiplesForm porcentajesForm, 
			ActionMapping mapping, 
			UsuarioBasico usuario, 
			HttpServletRequest request) 
	{			
		int resp=0; //variable para almacenar el resultado de la eliminación
		int offset=0; //variable para manejar el offset del listado
		boolean esUtilizado=false;
		HashMap parametros = new HashMap();
		//se instancia objeto de Porcentajes
		PorcentajesCxMultiples porcentaje=new PorcentajesCxMultiples();
		//se llena el mundo con los datos del formulario	
		this.llenarMundo(porcentajesForm.getEstado(),porcentajesForm.getPorcentajes(),porcentaje,porcentajesForm,0);
		
		//se verifica que sea un registro existente 
		if(porcentaje.getCodigo()>0)
		{
			//se consulta si el registro está siendo utilizado en otras tablas
			esUtilizado=UtilidadValidacion.esDetallePorcentajeCxMultiUsado(con,porcentaje.getCodigo(),porcentaje.getCodigo_enca());			
			//se verifica que el registro no se esté utilizando
			if(!esUtilizado)
			{	
				
				//se realiza la eliminación del detalle del porcentaje
				resp=porcentaje.eliminarPorcentaje(con,porcentaje.getCodigo());
				//se verifica resultado de la transacción
				
				if(resp<=0)
				{
					logger.error("No se pudo eliminar el registro");
					UtilidadBD.closeConnection(con);
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en PorcentajesCxMultiplesAction", "errors.sinEliminar", true);
				}				
				
				//se inserta LOG
				this.generarLog(con,porcentaje,null,porcentajesForm,"detalle",ConstantesBD.tipoRegistroLogEliminacion,usuario);					
			}
			else
			{
				String mensaje=porcentajesForm.getPos()+"";
				porcentajesForm.setRegistrosUsados(mensaje);
			}
			///se cambia estado según resultado
			if(esUtilizado)
				porcentajesForm.setEstado("error");
			else
				porcentajesForm.setEstado("mostrarIngreso");
			
		}
		
		if(!esUtilizado)
		{						
			porcentajesForm.getPorcentajes().remove("codigo_"+porcentajesForm.getPos());
			porcentajesForm.getPorcentajes().remove("codigo_encab_"+porcentajesForm.getPos());				
			porcentajesForm.getPorcentajes().remove("tipo_cirugia_"+porcentajesForm.getPos());
			porcentajesForm.getPorcentajes().remove("nombre_tcirugia_"+porcentajesForm.getPos());
			porcentajesForm.getPorcentajes().remove("tipo_asocio_"+porcentajesForm.getPos());
			porcentajesForm.getPorcentajes().remove("tipo_especialista_"+porcentajesForm.getPos());
			porcentajesForm.getPorcentajes().remove("via_acceso_"+porcentajesForm.getPos());
			porcentajesForm.getPorcentajes().remove("liquidacion_"+porcentajesForm.getPos());
			porcentajesForm.getPorcentajes().remove("adicional_"+porcentajesForm.getPos());
			porcentajesForm.getPorcentajes().remove("politra_"+porcentajesForm.getPos());
			porcentajesForm.getPorcentajes().remove("tipo_servicio_"+porcentajesForm.getPos());
			porcentajesForm.getPorcentajes().remove("nombre_tservicio_"+porcentajesForm.getPos());
			porcentajesForm.getPorcentajes().remove("tipo_sala_"+porcentajesForm.getPos());
			porcentajesForm.getPorcentajes().remove("estabd_"+porcentajesForm.getPos());
						
			HashMap tmp = new HashMap();
			int cont = 0 ;		
						
			//se corren posiciones dentro del mapa para borrar el registro
			for(int i=0;i<porcentajesForm.getNumRegistros();i++)
			{				
				if(i != porcentajesForm.getPos())
				{				
					tmp.put("codigo_"+cont,porcentajesForm.getPorcentajes("codigo_"+(i)));
					tmp.put("codigo_encab_"+cont,porcentajesForm.getPorcentajes("codigo_encab_"+(i)));				
					tmp.put("tipo_cirugia_"+cont,porcentajesForm.getPorcentajes("tipo_cirugia_"+(i)));
					tmp.put("nombre_tcirugia_"+cont,porcentajesForm.getPorcentajes("nombre_tcirugia_"+(i)));
					tmp.put("tipo_asocio_"+cont,porcentajesForm.getPorcentajes("tipo_asocio_"+(i)));
					tmp.put("tipo_especialista_"+cont,porcentajesForm.getPorcentajes("tipo_especialista_"+(i)));
					tmp.put("via_acceso_"+cont,porcentajesForm.getPorcentajes("via_acceso_"+(i)));
					tmp.put("liquidacion_"+cont,porcentajesForm.getPorcentajes("liquidacion_"+(i)));
					tmp.put("adicional_"+cont,porcentajesForm.getPorcentajes("adicional_"+(i)));
					tmp.put("politra_"+cont,porcentajesForm.getPorcentajes("politra_"+(i)));
					tmp.put("tipo_servicio_"+cont,porcentajesForm.getPorcentajes("tipo_servicio_"+(i)));
					tmp.put("nombre_tservicio_"+cont,porcentajesForm.getPorcentajes("nombre_tservicio_"+(i)));
					tmp.put("tipo_sala_"+cont,porcentajesForm.getPorcentajes("tipo_sala_"+(i)));
					tmp.put("estabd_"+cont,porcentajesForm.getPorcentajes("estabd_"+(i)));
					
					cont++;
				}
			}
						
			//se actualiza
			porcentajesForm.setPorcentajes(new HashMap());
			porcentajesForm.setPorcentajes(tmp);
			porcentajesForm.setNumRegistros(cont);
			porcentajesForm.setPorcentajes("numRegistros",cont+"");
			
			//***SE valida que en la posición del pager hayan registros****
			if(porcentajesForm.getOffsetHash()>=porcentajesForm.getNumRegistros())
			{
				int maxPageItems=Utilidades.convertirAEntero(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt()));
				offset=porcentajesForm.getNumRegistros()-maxPageItems;
				if(offset<0)
					offset=0;
				porcentajesForm.setOffsetHash(offset);
			}
		}
			
		try
		{
			//logger.info("valor del HashMap Porcentajes >> "+porcentajesForm.getPorcentajes());
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		catch(SQLException e)
		{
			logger.error("Error en accionEliminar de PorcentajesCxMultiplesAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en PorcentajesCxMultiplesAction", "errors.problemasDatos", true);
		}		
	}
	
	
	/**
	 * Método usado para eliminar un registro del listado de porcentajes por Convenio
	 * @param con
	 * @param porcentajesForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEliminarConvenio(
			Connection con, 
			PorcentajesCxMultiplesForm porcentajesForm, 
			ActionMapping mapping, 
			UsuarioBasico usuario, 
			HttpServletRequest request) 
	{			
		int resp=0; //variable para almacenar el resultado de la eliminación
		int offset=0; //variable para manejar el offset del listado
		boolean esUtilizado=false;
		HashMap parametros = new HashMap();
		//se instancia objeto de Porcentajes
		PorcentajesCxMultiples porcentaje=new PorcentajesCxMultiples();
		
		//se llena el mundo con los datos del formulario	
		this.llenarMundoEncabezado(porcentajesForm.getVigenciasMap(),porcentaje,Utilidades.convertirAEntero(porcentajesForm.getIndiceVigencias()));
		
		//se verifica que sea un registro existente 
		if(porcentaje.getCodigo_enca()>0)
		{
			//se consulta si el registro está siendo utilizado en otras tablas
			esUtilizado=UtilidadValidacion.esEncaPorcentajeCxMultiUsado(con,usuario.getCodigoInstitucionInt(),porcentaje.getCodigo_enca());			
			
			//se verifica que el registro no se esté utilizando
			if(!esUtilizado)
			{	
				
				//se realiza la eliminación del detalle del porcentaje
				resp=porcentaje.eliminarEncaPorcentaje(con,porcentaje.getCodigo_enca());				
				//se verifica resultado de la transacción
				
				if(resp<=0)
				{
					logger.error("No se pudo eliminar el registro de Encabezado");
					UtilidadBD.closeConnection(con);						
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en PorcentajesCxMultiplesAction", "errors.sinEliminar", true);
				}				
				
				//se inserta LOG
				this.generarLog(con,porcentaje,null,porcentajesForm,"encabezado",ConstantesBD.tipoRegistroLogEliminacion,usuario);					
			}
			else
			{
				String mensaje=porcentajesForm.getIndiceVigencias()+"";
				porcentajesForm.setRegistrosUsados(mensaje);
			}
			///se cambia estado según resultado
			if(esUtilizado)
				porcentajesForm.setEstado("error");
			else
				porcentajesForm.setEstado("mostrarIngreso");
			
		}
		
		if(!esUtilizado)
		{			
			porcentajesForm.getVigenciasMap().remove("codigo_encab_"+porcentajesForm.getIndiceVigencias());
			porcentajesForm.getVigenciasMap().remove("institucion_"+porcentajesForm.getIndiceVigencias());
			porcentajesForm.getVigenciasMap().remove("esq_tar_particular_"+porcentajesForm.getIndiceVigencias());
			porcentajesForm.getVigenciasMap().remove("nombre_esq_tar_part_"+porcentajesForm.getIndiceVigencias());
			porcentajesForm.getVigenciasMap().remove("esq_tar_general_"+porcentajesForm.getIndiceVigencias());
			porcentajesForm.getVigenciasMap().remove("nombre_esq_tar_gen_"+porcentajesForm.getIndiceVigencias());
			porcentajesForm.getVigenciasMap().remove("convenio_"+porcentajesForm.getIndiceVigencias());
			porcentajesForm.getVigenciasMap().remove("fecha_inicial_"+porcentajesForm.getIndiceVigencias());
			porcentajesForm.getVigenciasMap().remove("fecha_final_"+porcentajesForm.getIndiceVigencias());
			porcentajesForm.getVigenciasMap().remove("cuanto_detalle_"+porcentajesForm.getIndiceVigencias());
			porcentajesForm.getVigenciasMap().remove("estabd_"+porcentajesForm.getIndiceVigencias());			
						
			HashMap tmp = new HashMap();
			int cont = 0 ;
			//se corren posiciones dentro del mapa para borrar el registro
			for(int i=0;i<porcentajesForm.getNumRegistros();i++)
			{				
				if(i != Utilidades.convertirAEntero(porcentajesForm.getIndiceVigencias()))
				{									
					tmp.put("codigo_encab_"+cont,porcentajesForm.getVigenciasMap("codigo_encab_"+(i)).toString());
					tmp.put("institucion_"+cont,porcentajesForm.getVigenciasMap("institucion_"+(i)).toString());				
					tmp.put("esq_tar_particular_"+cont,porcentajesForm.getVigenciasMap("esq_tar_particular_"+(i)).toString());
					
					if(porcentajesForm.getVigenciasMap().containsKey("nombre_esq_tar_part_"+(i)))
						tmp.put("nombre_esq_tar_part_"+cont,porcentajesForm.getVigenciasMap("nombre_esq_tar_part_"+(i)).toString());
					else
						tmp.put("nombre_esq_tar_part_"+cont,"");
					
					tmp.put("esq_tar_general_"+cont,porcentajesForm.getVigenciasMap("esq_tar_general_"+(i)).toString());
					
					if(porcentajesForm.getVigenciasMap().containsKey("nombre_esq_tar_gen_"+(i)))
						tmp.put("nombre_esq_tar_gen_"+cont,porcentajesForm.getVigenciasMap("nombre_esq_tar_gen_"+(i)).toString());
					else 
						tmp.put("nombre_esq_tar_gen_"+cont,"");
					
					tmp.put("convenio_"+cont,porcentajesForm.getVigenciasMap("convenio_"+(i)).toString());
					tmp.put("fecha_inicial_"+cont,porcentajesForm.getVigenciasMap("fecha_inicial_"+(i)).toString());
					tmp.put("fecha_final_"+cont,porcentajesForm.getVigenciasMap("fecha_final_"+(i)).toString());
					
					if(porcentajesForm.getVigenciasMap().containsKey("cuanto_detalle_"+(i)))
						tmp.put("cuanto_detalle_"+cont,porcentajesForm.getVigenciasMap("cuanto_detalle_"+(i)).toString());
					else
						tmp.put("cuanto_detalle_"+cont,"0");
					
					tmp.put("estabd_"+cont,porcentajesForm.getVigenciasMap("estabd_"+(i)).toString());
					
					cont++;
				}
			}
			
			//se actualiza
			porcentajesForm.setVigenciasMap(new HashMap());			
			porcentajesForm.setNumRegistros(cont);			
			porcentajesForm.setVigenciasMap(tmp);
			porcentajesForm.setVigenciasMap("numRegistros",cont+"");
			
			
			//***SE valida que en la posición del pager hayan registros****
			if(porcentajesForm.getOffsetHash()>=porcentajesForm.getNumRegistros())
			{
				int maxPageItems=Utilidades.convertirAEntero(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt()));
				offset=porcentajesForm.getNumRegistros()-maxPageItems;
				if(offset<0)
					offset=0;
				porcentajesForm.setOffsetHash(offset);
			}
		}
			
		try
		{
			//logger.info("valor del HashMap Porcentajes >> "+porcentajesForm.getPorcentajes());
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("convenio");
		}
		catch(SQLException e)
		{
			logger.error("Error en accionEliminar de PorcentajesCxMultiplesAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en PorcenCxMultiplesAction", "errors.problemasDatos", true);
		}		
	}
	
	
	
	/**
	 * Método usado para cargar un registro del formulario al mundo
	 * @param String estado
	 * @param HashMap datos
	 * @param PorcentajesCxMultiples porcentajes,
	 * @param PorcentajesCxMultiplesForm porcentajesForm,
	 * @param pos
	 */
	private PorcentajesCxMultiples llenarMundo(
			String estado,
			HashMap datos, 
			PorcentajesCxMultiples porcentajes, 
			PorcentajesCxMultiplesForm porcentajesForm,  
			int pos) 
	{
		String aux="";//variable auxiliar
		if(estado.equals("guardar"))
		{
			porcentajes.setCodigo(Utilidades.convertirAEntero(datos.get("codigo_"+pos)+""));			
			porcentajes.setCodigo_enca(Utilidades.convertirAEntero(datos.get("codigo_encab_"+pos)+""));
			porcentajes.setTipoCirugia(datos.get("tipo_cirugia_"+pos)+"");
			porcentajes.setAsocio(Utilidades.convertirAEntero(datos.get("tipo_asocio_"+pos)+""));
			porcentajes.setMedico(datos.get("tipo_especialista_"+pos)+"");
			porcentajes.setVia(datos.get("via_acceso_"+pos)+"");			
			porcentajes.setLiquidacion(Float.parseFloat(datos.get("liquidacion_"+pos)+""));
			porcentajes.setAdicional(Float.parseFloat(datos.get("adicional_"+pos)+""));
			porcentajes.setPolitra(Float.parseFloat(datos.get("politra_"+pos)+""));			
			porcentajes.setTipos_servicio(datos.get("tipo_servicio_"+pos)+"");
			porcentajes.setTipos_salas(Utilidades.convertirAEntero(datos.get("tipo_sala_"+pos)+""));
			porcentajes.setEstabd(datos.get("estabd_"+pos)+"");			
		}
		else if(estado.equals("eliminar"))
		{
			porcentajesForm.setCodigoRegistro(Utilidades.convertirAEntero(datos.get("codigo_"+porcentajesForm.getPos())+""));
			porcentajes.setCodigo(porcentajesForm.getCodigoRegistro());			
			
			porcentajes.setCodigo(Utilidades.convertirAEntero(datos.get("codigo_"+porcentajesForm.getPos())+""));			
			porcentajes.setCodigo_enca(Utilidades.convertirAEntero(datos.get("codigo_encab_"+porcentajesForm.getPos())+""));
			porcentajes.setTipoCirugia(datos.get("tipo_cirugia_"+porcentajesForm.getPos())+"");
			porcentajes.setAsocio(Utilidades.convertirAEntero(datos.get("tipo_asocio_"+porcentajesForm.getPos())+""));
			porcentajes.setMedico(datos.get("tipo_especialista_"+porcentajesForm.getPos())+"");
			porcentajes.setVia(datos.get("via_acceso_"+porcentajesForm.getPos())+"");
			
			if(!datos.get("liquidacion_"+porcentajesForm.getPos()).toString().equals(""))
				porcentajes.setLiquidacion(Float.parseFloat(datos.get("liquidacion_"+porcentajesForm.getPos())+""));
			else
				porcentajes.setLiquidacion(0);
			
			if(!datos.get("adicional_"+porcentajesForm.getPos()).toString().equals(""))
				porcentajes.setAdicional(Float.parseFloat(datos.get("adicional_"+porcentajesForm.getPos())+""));
			else
				porcentajes.setAdicional(0);
			
			if(!datos.get("politra_"+porcentajesForm.getPos()).toString().equals(""))
				porcentajes.setPolitra(Float.parseFloat(datos.get("politra_"+porcentajesForm.getPos())+""));
			else
				porcentajes.setPolitra(0);
			
			porcentajes.setTipos_servicio(datos.get("tipo_servicio_"+porcentajesForm.getPos())+"");
			porcentajes.setTipos_salas(Utilidades.convertirAEntero(datos.get("tipo_sala_"+porcentajesForm.getPos())+""));
			porcentajes.setEstabd(datos.get("estabd_"+porcentajesForm.getPos())+"");
		}
		
		return porcentajes;
	}
	
	
	/**
	 * Método usado para cargar un registro del formulario al mundo
	 * @param porcentajesForm
	 * @param porcentajes
	 * @param pos
	 */
	private PorcentajesCxMultiples llenarMundoEncabezado(HashMap datos, PorcentajesCxMultiples porcentajes, int pos) 
	{				
		porcentajes.setCodigo_enca(Utilidades.convertirAEntero(datos.get("codigo_encab_"+pos)+""));
		porcentajes.setInstitucion(Utilidades.convertirAEntero(datos.get("institucion_"+pos)+""));
		porcentajes.setEsq_tar_particular(datos.get("esq_tar_general_"+pos)+"");
		porcentajes.setNombre_esq_tar_part(datos.get("nombre_esq_tar_par_"+pos)+"");
		porcentajes.setEsq_tar_general(datos.get("esq_tar_general_"+pos)+"");
		porcentajes.setNombre_esq_tar_gen(datos.get("nombre_esq_tar_gen_"+pos)+"");
		porcentajes.setConvenio(Utilidades.convertirAEntero(datos.get("convenio_"+pos)+""));
		porcentajes.setFecha_inicial(datos.get("fecha_inicial_"+pos)+"");
		porcentajes.setFecha_final(datos.get("fecha_final_"+pos)+"");			
		porcentajes.setEstabd(datos.get("estabd_"+pos)+"");
		
		return porcentajes;
	}
	
	
	
	/**
	 * Validaciones para Guardar el Convenio
	 * @param PorcentajesCxMultiplesForm porcentajesForm
	 * @param ActionErrors errores
	 * */
	private ActionErrors accionValidarGuardarConvenio(PorcentajesCxMultiplesForm forma,ActionErrors errores)
	{
		for(int i = 0; i< forma.getNumRegistros(); i++)
		{		
			
			if(forma.getVigenciasMap("fecha_inicial_"+i).equals(""))
				errores.add("fechainicial", new ActionMessage("errors.required","La Fecha inicial del registro "+(i+1)));    		
			
    		if(forma.getVigenciasMap("fecha_final_"+i).equals(""))
    			errores.add("fechafinal", new ActionMessage("errors.required","La Fecha final del registro "+(i+1)));    		

    		if(!forma.getVigenciasMap("fecha_inicial_"+i).equals("") && 
					!forma.getVigenciasMap("fecha_final_"+i).equals(""))
    		{
    			if(!UtilidadFecha.validarFecha(forma.getVigenciasMap("fecha_inicial_"+i)+"") ||
    					!UtilidadFecha.validarFecha(forma.getVigenciasMap("fecha_final_"+i)+""))
    						errores.add("fechas", new ActionMessage("errors.required","Verifique el Formato de Fechas para el campo "+(i+1)+". "));
    		}
    		
    		if(!forma.getVigenciasMap("fecha_inicial_"+i).equals("") && 
					!forma.getVigenciasMap("fecha_final_"+i).equals("") &&
			    		UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(forma.getVigenciasMap("fecha_final_"+i)+""), UtilidadFecha.conversionFormatoFechaAAp(forma.getVigenciasMap("fecha_inicial_"+i)+"")))
			    			errores.add("fechafinal", new ActionMessage("errors.debeSerNumeroMayor", "La Fecha Final debe ser Mayor o Igual a la Inicial ",UtilidadFecha.conversionFormatoFechaAAp(forma.getVigenciasMap("fecha_inicial_"+i)+"")));

    		if(!forma.getVigenciasMap("fecha_inicial_"+i).equals("") && 
					!forma.getVigenciasMap("fecha_final_"+i).equals("")&&
						forma.getVigenciasMap("estabd_"+i).equals(ConstantesBD.acronimoNo)&&    			    		
							UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(forma.getVigenciasMap("fecha_final_"+i)+""), UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual())))	    	
	    						errores.add("fechafinal", new ActionMessage("errors.debeSerNumeroMayor", "La Fecha Final debe ser Mayor a la Fecha del Sistema ",UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual())));
	    				
			for(int j = i; j < forma.getNumRegistros(); j++)
			{
				if(!forma.getVigenciasMap("fecha_inicial_"+i).equals("") && 
						!forma.getVigenciasMap("fecha_final_"+i).equals(""))
				{
					if(j>i &&  UtilidadFecha.existeTraslapeEntreFechas(
								forma.getVigenciasMap("fecha_inicial_"+i).toString(),
								forma.getVigenciasMap("fecha_final_"+i).toString(),
								forma.getVigenciasMap("fecha_inicial_"+j).toString(),
								forma.getVigenciasMap("fecha_final_"+j).toString()))
								{
									errores.add("descripcion",new ActionMessage("errors.notEspecific","Existe Cruce de Fechas. la Fecha "+forma.getVigenciasMap("fecha_inicial_"+i).toString()+" - "+forma.getVigenciasMap("fecha_final_"+i).toString()+" del Registro Numero "+(i+1)+". Y la Fecha "+forma.getVigenciasMap("fecha_inicial_"+j).toString()+" - "+forma.getVigenciasMap("fecha_final_"+j).toString()+" del Registro Numero "+(j+1)+"."));
									j = forma.getNumRegistros();								
								}
				}
			}
		}		
		return errores;	
	}
	
	
	/**
	 * Método usado para actualizar registros o ingresar nuevos al listado de porcentajes
	 * @param con
	 * @param porcentajesForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardar(
			Connection con, 
			PorcentajesCxMultiplesForm porcentajesForm, 
			ActionMapping mapping, 
			UsuarioBasico usuario, 
			HttpServletRequest request) 
	{
		try
		{
						
			//se inicia transacción
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).beginTransaction(con);
			
			HashMap registroAntiguo=new HashMap(); //objeto usado para almacenar el registro antes de ser modificado
			HashMap parametros = new HashMap();
			
			boolean exitoActualizacion=true;
			boolean exitoInsercion=true;
			boolean esUtilizado=false; //variable para verificar si el registro está siendo usado
			boolean huboRegistrosUtilizados=false; //variable que indica si existieron registros utilizados que no se pudieron modificar
			String registrosUsados=""; //cadena que almacena los códigos de los registros que no pudieron ser modificados por que estaban en uso
			String codigoEnca = ConstantesBD.codigoNuncaValido+"";
			int resp=0;
			
			//--------------------------------------------------------------
			//se instancia objeto Porcentajes
			PorcentajesCxMultiples porcentajes=new PorcentajesCxMultiples();
			//se itera arreglo de registros
						
			codigoEnca = this.getCodigoEncabezado(con,porcentajesForm, usuario);
			
			
			
			if(Utilidades.convertirAEntero(codigoEnca) < 0)
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en PorcentajesCxMultiplesAction","errors.sinActualizar",true);
			
			for(int i=0;i<porcentajesForm.getNumRegistros();i++)
			{				
				//Almacena o actualiza la informacion del codigo del encabezado 
				porcentajesForm.setPorcentajes("codigo_encab_"+i,codigoEnca);
				
				//carga el objeto con los valores de porcentajes
				this.llenarMundo("guardar",porcentajesForm.getPorcentajes(),porcentajes,porcentajesForm,i);
				
				//se verifica que el registro sea nuevo o de modificación
				// -1 => registro nuevo
				if(porcentajes.getCodigo()>0)
				{
					//se consulta si el registro está siendo utilizado en otras tablas
					esUtilizado=UtilidadValidacion.esDetallePorcentajeCxMultiUsado(con,porcentajes.getCodigo(),Utilidades.convertirAEntero(codigoEnca));
					
					//obtener registro viejo
					parametros.put("codigo_encab",codigoEnca);
					parametros.put("codigo_detalle",porcentajes.getCodigo());
					
					registroAntiguo=porcentajes.cargarPorcentajes(con,usuario.getCodigoInstitucionInt(), parametros);
					
					//logger.info("registro antiguo >> "+registroAntiguo);
					
					if(fueModificado(registroAntiguo,porcentajes))
					{						
						//se verifica que el registro no se esté utilizando						
						if(!esUtilizado)
						{
							//se actualizan los datos
							logger.info("*****1**************");
						
							parametros = llenarHashMapBD(porcentajesForm.getPorcentajes(),i, usuario);							
							resp=porcentajes.actualizarPorcentaje(con, parametros);							
							
							//se revisa estado actualizacion						
							if(resp<=0)
								exitoActualizacion=false;
							else
								this.generarLog(
										con,
										this.llenarMundo("guardar",registroAntiguo,new PorcentajesCxMultiples(),porcentajesForm,0),										
										this.llenarMundo("guardar",porcentajesForm.getPorcentajes(),porcentajes,porcentajesForm,i),
										porcentajesForm,
										"detalle",
										ConstantesBD.tipoRegistroLogModificacion,
										usuario);					
						}						
					}
				}
				//se inserta nuevo registro
				else
				{
					//se insertan los datos	
					logger.info("PARAMETROS ACTION");
					parametros = llenarHashMapBD(porcentajesForm.getPorcentajes(),i, usuario);
					resp=porcentajes.insertarPorcentaje(con, parametros);
					//se revisa estado actualizacion
					if(resp<=0)
						exitoInsercion=false;					
				}
			}
			if(huboRegistrosUtilizados)
			{
				porcentajesForm.setEstado("error");
				porcentajesForm.setRegistrosUsados(registrosUsados);
			}
			
			logger.info("valor de exitos >> "+exitoActualizacion+" >> "+exitoInsercion);
			
			//verificación de la transacción			
			if(exitoActualizacion&&exitoInsercion)
			{
				//éxito!!!!
				logger.info("******2**************");
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);				
				return accionCargarInformacionDetalleEsquema(con,porcentajesForm,usuario,mapping);
				
			}
			//el problema fue en la insercion
			else if(!exitoInsercion)
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en PorcentajesCxMultiplesAction", "errors.sinIngresar",true);
				
			}
			// o el problema fue en la actualización
			else
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en PorcentajesCxMultiplesAction", "errors.sinActualizar",true);
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
			logger.error("Error en accionGuardar de PorcentajesCxMultiplesAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en PorcentajesCxMultiplesAction", "errors.problemasDatos", true);
		}
	}
	
	
	
	/**
	 * Método usado para actualizar registros o ingresar del encabezado de Porcentajes
	 * @param con
	 * @param porcentajesForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarConvenio(
			Connection con, 
			PorcentajesCxMultiplesForm porcentajesForm, 
			ActionMapping mapping, 
			UsuarioBasico usuario, 
			HttpServletRequest request) 
	{
		try
		{		
			logger.info("INTENTAR GUARDAR EL CONVENIO.");
			//se inicia transacción
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).beginTransaction(con);
			
			HashMap registroAntiguo=new HashMap(); //objeto usado para almacenar el registro antes de ser modificado
			HashMap parametros = new HashMap();
			
			boolean exitoActualizacion=true;
			boolean exitoInsercion=true;
			boolean esUtilizado=false; //variable para verificar si el registro está siendo usado
			boolean huboRegistrosUtilizados=false; //variable que indica si existieron registros utilizados que no se pudieron modificar
			String registrosUsados=""; //cadena que almacena los códigos de los registros que no pudieron ser modificados por que estaban en uso			
			int resp=0;
			
			//--------------------------------------------------------------
			//se instancia objeto Porcentajes
			PorcentajesCxMultiples porcentajes=new PorcentajesCxMultiples();
			
			logger.info("SE VERFICA ACCION, SI ES INSERTAR ENTONCES -1="+porcentajes.getCodigo_enca());
			
			//se itera arreglo de registros		
			for(int i=0;i<porcentajesForm.getNumRegistros();i++)
			{					
				//carga el objeto con los valores de Vigencias
				this.llenarMundoEncabezado(porcentajesForm.getVigenciasMap(),porcentajes,i);
				
				//se verifica que el registro sea nuevo o de modificación
				// -1 => registro nuevo
				if(porcentajes.getCodigo_enca()>0)
				{
					//se consulta si el registro está siendo utilizado en otras tablas
					esUtilizado=UtilidadValidacion.esEncaPorcentajeCxMultiUsado(con,usuario.getCodigoInstitucionInt(),porcentajes.getCodigo_enca());
					
					//obtener registro viejo
					parametros.put("codigo_encab",porcentajes.getCodigo_enca());					
					registroAntiguo=porcentajes.cargarEncaPorcentajes(con, usuario.getCodigoInstitucionInt(), parametros);
					
					
					if(fueModificadoEncabezado(registroAntiguo,porcentajes))
					{						
						//se verifica que el registro no se esté utilizando
						if(!esUtilizado)
						{
							//se actualizan los datos
							logger.info("*****1**************");
						
							parametros = llenarHashMapEncabezadoBD(porcentajesForm.getVigenciasMap(),i, usuario);							
							resp=porcentajes.actualizarEncaPorcentaje(con, parametros);							
							
							//se revisa estado actualizacion						
							if(resp<=0)
								exitoActualizacion=false;
							else
								this.generarLog(
										con,
										this.llenarMundoEncabezado(registroAntiguo,new PorcentajesCxMultiples(),0),										
										this.llenarMundoEncabezado(porcentajesForm.getVigenciasMap(),porcentajes,0),
										porcentajesForm,
										"encabezado",
										ConstantesBD.tipoRegistroLogModificacion,
										usuario);					
						}						
					}
				}
				//se inserta nuevo registro
				else
				{
					//se insertan los datos	
					parametros = llenarHashMapEncabezadoBD(porcentajesForm.getVigenciasMap(),i, usuario);
					logger.info("insertando parametros");
					Utilidades.imprimirMapa(parametros);
					resp=porcentajes.insertarEncaPorcentaje(con, parametros);
					//se revisa estado actualizacion
					logger.info("se insertaron "+resp);
					if(resp<=0)
						exitoInsercion=false;					
				}
			}
			if(huboRegistrosUtilizados)
			{
				porcentajesForm.setEstado("error");
				porcentajesForm.setRegistrosUsados(registrosUsados);
			}
			
			logger.info("valor de exitos >> "+exitoActualizacion+" >> "+exitoInsercion);
			
			//verificación de la transacción			
			if(exitoActualizacion&&exitoInsercion)
			{
				//éxito!!!!
				logger.info("******2**************");
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);				
				return accionCargarInformacionVigencia(con, porcentajesForm, usuario, mapping);
				
			}
			//el problema fue en la insercion
			else if(!exitoInsercion)
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en PorcentajesCxMultiplesAction", "errors.sinIngresar",true);
				
			}
			// o el problema fue en la actualización
			else
			{
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en PorcentajesCxMultiplesAction", "errors.sinActualizar",true);
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
			logger.error("Error en accionGuardar de PorcentajesCxMultiplesAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en PorcentajesCxMultiplesAction", "errors.problemasDatos", true);
		}
	}
	
	
	/**
	 * @param con
	 * @param porcentajesForm
	 * @param usuario
	 * @param request
	 * @return
	 * */
	public String accionGuardarEncabezado(
			Connection con, 
			PorcentajesCxMultiplesForm forma,		 
			UsuarioBasico usuario)
	{
		HashMap parametros = new HashMap();
		PorcentajesCxMultiples porcentajes = new PorcentajesCxMultiples();
		
		
		//Via de Ingreso Esquema
		if(forma.getIndicadorIngreso().equals("esquema"))
		{
			parametros.put("institucion",usuario.getCodigoInstitucionInt());
			
			//esquema particular 
			if(forma.getEsquemaTarifario("tipo_esquema").toString().equals("1"))
			{
				parametros.put("esq_tar_particular",forma.getEsquemaTarifario("codigo_tari"));
				parametros.put("esq_tar_general","");
			}
			else
			{
				parametros.put("esq_tar_particular","");
				parametros.put("esq_tar_general",forma.getEsquemaTarifario("codigo_tari"));					
			}
			
			parametros.put("convenio","");
			parametros.put("fecha_inicial","");
			parametros.put("fecha_final","");
			
			parametros.put("usuario_modifica",usuario.getLoginUsuario());
			parametros.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			parametros.put("hora_modifica",UtilidadFecha.getHoraActual());
			
			return porcentajes.insertarEncaPorcentaje(con, parametros)+"";		
		}		
		//--------------------------------------
		
		return ConstantesBD.codigoNuncaValido+"";
	}
	
	
	/**
	 * Consulta la informacion del encabezado 
	 * @param Connection con
	 * @param PorcentajesCxMultiplesForm forma
	 * @param UsuarioBasico usuario
	 * */
	public String getCodigoEncabezado(Connection con,PorcentajesCxMultiplesForm forma,UsuarioBasico usuario)
	{
		String respuesta="";
		HashMap parametros = new HashMap();
		PorcentajesCxMultiples porcentajes = new PorcentajesCxMultiples();
		
		if(forma.getIndicadorIngreso().equals("esquema"))
		{
			if(forma.getEsquemaTarifario("codigo_encab").toString().equals(ConstantesBD.codigoNuncaValido+""))
			{
				//esquema particular 
				if(forma.getEsquemaTarifario("tipo_esquema").toString().equals("1"))
				{
					parametros.put("esq_tar_particular",forma.getEsquemaTarifario("codigo_tari"));
					parametros.put("esq_tar_general",null);
				}
				else
				{
					parametros.put("esq_tar_particular",null);
					parametros.put("esq_tar_general",forma.getEsquemaTarifario("codigo_tari"));					
				}
					
				parametros = porcentajes.cargarEncaPorcentajes(con,usuario.getCodigoInstitucionInt(), parametros);
				
				
				if(parametros.get("numRegistros").toString().equals("0"))
				{				
					respuesta = accionGuardarEncabezado(con, forma, usuario);				
					
					if(Utilidades.convertirAEntero(respuesta) > 0)
					{
						//Toma la informacion de esquemas tarifarios
						forma.setEsquemasTarifariosArray(Utilidades.obtenerEsquemasTarifariosGenPartInArray(con,usuario.getCodigoInstitucionInt(),"porcentajescx"));
						forma.setEsquemaTarifario("codigo",forma.getEsquemaTarifario("codigo_tari").toString()+ConstantesBD.separadorSplit+forma.getEsquemaTarifario("tipo_esquema").toString()+ConstantesBD.separadorSplit+respuesta);
					}
				}
				else
				{
					respuesta = parametros.get("codigo_encab_0").toString();					
				}
			}
			else
			{
				respuesta = forma.getEsquemaTarifario("codigo_encab").toString();
			}
		}
		else if(forma.getIndicadorIngreso().equals("convenio"))
		{
			respuesta = forma.getVigenciasMap("codigo_encab_"+forma.getIndiceVigencias()).toString();						
		}			
				
		return respuesta;				
	}
	
	
	/**
	 * Método usado para verificar si un registor fue modificado
	 * comparando el registro cargado con el registro de la BD
	 * @param registro (antiguo)
	 * @param porcentaje (nuevo)
	 * @return
	 */
	private boolean fueModificado(HashMap registro, PorcentajesCxMultiples porcentaje) 
	{
		boolean cambio=false;
		//REVISIÓN DE ESQUEMAS		
		
		//revisión del campo tipo cirugia
		if(porcentaje.getTipoCirugia().compareTo(registro.get("tipo_cirugia_0")+"")!=0)		
			cambio=true;	
		
		//revisión del campo asocio
		if(porcentaje.getAsocio()!=Utilidades.convertirAEntero(registro.get("tipo_asocio_0")+""))
			cambio=true;
		
		//revision tipo especialista
		if(!porcentaje.getMedico().equals(registro.get("tipo_especialista_0")+""))
			cambio=true;
		
		//revision de vía
		if(!porcentaje.getVia().equals(registro.get("via_acceso_0")+""))		
			cambio=true;
			
		//revision de % liquidacion
		if(porcentaje.getLiquidacion()!=Float.parseFloat(registro.get("liquidacion_0")+""))
			cambio=true;
		
		//revisión de % Adicional
		if(porcentaje.getAdicional()!=Float.parseFloat(registro.get("adicional_0")+""))
			cambio=true;
		
		//revisión de % Politraumatismo
		if(porcentaje.getPolitra()!=Float.parseFloat(registro.get("politra_0")+""))
			cambio=true;
		
		
		//Tipo Servicio
		if(!porcentaje.getTipos_servicio().equals(registro.get("tipo_servicio_0")+""))
			cambio=true;
		
		//Tipo Sala	
		if(porcentaje.getTipos_salas()!=Utilidades.convertirAEntero(registro.get("tipo_sala_0")+""))
			cambio=true;		
		
		return cambio;
	}
	
	
	
	/**
	 * Método usado para verificar si un registor fue modificado del encabezado
	 * comparando el registro cargado con el registro de la BD
	 * @param registro (antiguo)
	 * @param porcentaje (nuevo)
	 * @return
	 */
	private boolean fueModificadoEncabezado(HashMap registro, PorcentajesCxMultiples porcentaje) 
	{
		boolean cambio=false;
		//REVISIÓN DE ENCABEZADO DE ESQUEMAS		
		
		//Esquema tarifario particular
		if(porcentaje.getEsq_tar_particular().compareTo(registro.get("esq_tar_particular_0")+"")!=0)		
			cambio=true;	
		
		//Esquema tarifario general
		if(porcentaje.getEsq_tar_general().compareTo(registro.get("esq_tar_general_0")+"")!=0)
			cambio=true;
		
		//Convenio
		if(porcentaje.getConvenio()!=Utilidades.convertirAEntero(registro.get("convenio_0")+""))
			cambio=true;
		
		//Fecha Inicial
		if(!porcentaje.getFecha_inicial().equals(registro.get("fecha_inicial_0")+""))		
			cambio=true;
			

		//Fecha Final
		if(!porcentaje.getFecha_final().equals(registro.get("fecha_final_0")+""))		
			cambio=true;
		
		return cambio;
	}
	
		
	/**
	 * Inicializa un HashMap sin indices
	 * */
	public HashMap llenarHashMapBD(HashMap datos, int pos,UsuarioBasico usuario)
	{
		HashMap respuesta = new HashMap();
		
		respuesta.put("codigo",datos.get("codigo_"+pos));
		respuesta.put("codigo_encab",datos.get("codigo_encab_"+pos));
		respuesta.put("tipo_cirugia",datos.get("tipo_cirugia_"+pos));
		respuesta.put("tipo_asocio",datos.get("tipo_asocio_"+pos));
		respuesta.put("tipo_especialista",datos.get("tipo_especialista_"+pos));
		respuesta.put("via_acceso",datos.get("via_acceso_"+pos));
		respuesta.put("liquidacion",datos.get("liquidacion_"+pos));
		respuesta.put("adicional",datos.get("adicional_"+pos));
		respuesta.put("politra",datos.get("politra_"+pos));
		respuesta.put("tipo_servicio",datos.get("tipo_servicio_"+pos));
		respuesta.put("tipo_sala",datos.get("tipo_sala_"+pos));
		respuesta.put("usuario_modifica",usuario.getLoginUsuario());
		respuesta.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		respuesta.put("hora_modifica",UtilidadFecha.getHoraActual());		
		return respuesta;		
	}
	
	
	/**
	 * Inicializa un HashMap sin indices
	 * */
	public HashMap llenarHashMapEncabezadoBD(HashMap datos, int pos,UsuarioBasico usuario)
	{
		HashMap respuesta = new HashMap();
				
		respuesta.put("codigo_encab",datos.get("codigo_encab_"+pos));
		respuesta.put("institucion",datos.get("institucion_"+pos));		
		respuesta.put("esq_tar_particular",datos.get("esq_tar_particular_"+pos));
		respuesta.put("esq_tar_general",datos.get("esq_tar_general_"+pos));		
		respuesta.put("convenio",datos.get("convenio_"+pos));
		respuesta.put("fecha_inicial",UtilidadFecha.conversionFormatoFechaABD(datos.get("fecha_inicial_"+pos)+""));
		respuesta.put("fecha_final",UtilidadFecha.conversionFormatoFechaABD(datos.get("fecha_final_"+pos)+""));	
		respuesta.put("usuario_modifica",usuario.getLoginUsuario());
		respuesta.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		respuesta.put("hora_modifica",UtilidadFecha.getHoraActual());		
		return respuesta;		
	}
	
	
	
	
	/**
	 * Método usado para ornder el listado de porcentajes de
	 * la opción consulta
	 * @param con
	 * @param porcentajesForm
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionOrdenarDetalle(Connection con, PorcentajesCxMultiplesForm porcentajesForm, ActionMapping mapping, HttpServletRequest request) {
		try
		{
			String[] indices={
					"codigo_",
					"codigo_encab_",
					"tipo_cirugia_",				
					"nombre_tcirugia_",
					"tipo_asocio_",
					"nombre_asocio_",
					"tipo_especialista_",
					"via_acceso_",
					"liquidacion_",
					"adicional_",
					"politra_",				
					"tipo_servicio_",
					"nombre_tservicio_",
					"tipo_sala_",
					"nombre_tsala_",
					"estabd_"
				};
			
			
			
			porcentajesForm.setPorcentajes(Listado.ordenarMapa(indices,
					porcentajesForm.getIndice(),
					porcentajesForm.getUltimoIndice(),
					porcentajesForm.getPorcentajes(),
					porcentajesForm.getNumRegistros()));
			
			porcentajesForm.setPorcentajes("numRegistros",porcentajesForm.getNumRegistros()+"");
			porcentajesForm.setUltimoIndice(porcentajesForm.getIndice());
			
			
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("principal");
		}
		catch(SQLException e)
		{
			logger.error("Error en accionOrdenar de PorcentajesCxMultiplesAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en PorcentajesCxMultiplesAction", "errors.problemasDatos", true);
		}
	}
	
	
	/**
	 * @param con
	 * @param porcentajesForm
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionOrdenarConvenio(Connection con, PorcentajesCxMultiplesForm porcentajesForm, ActionMapping mapping, HttpServletRequest request) {
		try
		{
			String[] indices={
					"codigo_encab_",
					"institucion_",
					"esq_tar_particular_",
					"nombre_esq_tar_part_",
					"esq_tar_general_",
					"nombre_esq_tar_gen_",
					"convenio_",
					"fecha_inicial_",
					"fecha_final_",
					"cuanto_detalle_",
					"estabd_"				
				};
			
			
			
			porcentajesForm.setVigenciasMap(Listado.ordenarMapa(indices,
					porcentajesForm.getIndice(),
					porcentajesForm.getUltimoIndice(),
					porcentajesForm.getVigenciasMap(),
					porcentajesForm.getNumRegistros()));
			
			porcentajesForm.setPorcentajes("numRegistros",porcentajesForm.getNumRegistros()+"");
			porcentajesForm.setUltimoIndice(porcentajesForm.getIndice());
			
			
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("convenio");
		}
		catch(SQLException e)
		{
			logger.error("Error en accionOrdenar de PorcentajesCxMultiplesAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en PorcentajesCxMultiplesAction", "errors.problemasDatos", true);
		}
	}
	
	
	
	/**
	 * Cargar informcion del Esquema o del Detalle del Convenio en Base a la Busqueda Avanzada 
	 * @param Connection con
	 * @param PorcentajesCxMultiplesForm forma
	 * @param UsuarioBasico usuario
	 * @param ActionMapping mapping 
	 * */
	private ActionForward accionConsultarAvanzada(Connection con,
			PorcentajesCxMultiplesForm forma,
			UsuarioBasico usuario, 
			ActionMapping mapping)
	{
		
		//*****Carga la informacion general del detalle
		PorcentajesCxMultiples porcen = new PorcentajesCxMultiples();
		HashMap parametros = new HashMap();
		
		//resetea los parametros del pager
		forma.setNumRegistros(0);
		forma.setOffsetHash(0);
			
		//Tipo Cirugia, se carga desde la JSP
		//Tipo Especialista se carga desde la JSP
		//Via de Acceso se carga desde la JSP		
		
		//Consulta el tipo de servicio
		//forma.setTiposServicioArray(PorcentajesCxMultiples.listarTiposServicio(con));		
		
		//Tipos Salas 
		//forma.setTiposSalaArray(Utilidades.obtenerTiposSala(con,usuario.getCodigoInstitucionInt(),"","f"));
		
		//********Valida el ingreso a la funcionalidad
				
		
		//Por el Convenio
		if(forma.getIndicadorIngreso().equals("convenio"))
		{						
			parametros.put("codigo_encab",forma.getVigenciasMap("codigo_encab_"+forma.getIndiceVigencias()));			
		}//Por el esquema
		else if(forma.getIndicadorIngreso().equals("esquema"))
		{
			String []cadena = forma.getEsquemaTarifario("codigo").toString().split(ConstantesBD.separadorSplit);
			parametros.put("codigo_encab",cadena[2]);			
		}	
		
		
		//Validaciones de la Busqueda
		if(!forma.getMapaBusqueda("tipo_servicio").equals(ConstantesBD.codigoNuncaValido+""))
			parametros.put("tipo_servicioB", forma.getMapaBusqueda("tipo_servicio"));
		
		if(!forma.getMapaBusqueda("tipo_cirugia").equals(ConstantesBD.codigoNuncaValido+""))
			parametros.put("tipo_cirugiaB", forma.getMapaBusqueda("tipo_cirugia"));
			
		if(!forma.getMapaBusqueda("tipo_asocio").equals(ConstantesBD.codigoNuncaValido+""))
			parametros.put("tipo_asocioB", forma.getMapaBusqueda("tipo_asocio"));
			
		if(!forma.getMapaBusqueda("tipo_especialista").equals(ConstantesBD.codigoNuncaValido+""))
			parametros.put("tipo_especialistaB", forma.getMapaBusqueda("tipo_especialista"));
			
		if(!forma.getMapaBusqueda("via_acceso").equals(ConstantesBD.codigoNuncaValido+""))
			parametros.put("via_accesoB", forma.getMapaBusqueda("via_acceso"));
			
		
		//carga la informacion del Detalle
		forma.setPorcentajes(porcen.cargarPorcentajes(con, usuario.getCodigoInstitucionInt(),parametros));
		forma.setNumRegistros(Utilidades.convertirAEntero(forma.getPorcentajes("numRegistros").toString()));
		
		return mapping.findForward("principal");
	}
	
	
	/**
	 * @param con
	 * @param registro (antiguo)	 
	 * @param registroNuevo
	 * @param Forma 
	 * @param parteTabla
	 * @param tipo (tipo de Log)
	 * @param usuario
	 */
	private void generarLog(
			Connection con, 
			PorcentajesCxMultiples registro, 
			PorcentajesCxMultiples registroNuevo,
			PorcentajesCxMultiplesForm forma,
			String parteTabla,
			int tipo, 
			UsuarioBasico usuario) 
	{
		String log="";
	    //***********************************************************
		
		if(parteTabla.equals("detalle"))
		{
			if(tipo==ConstantesBD.tipoRegistroLogModificacion)
			{			
			    log="\n            ====INFORMACION ORIGINAL DEL DETALLE DE PORCENTAJE CIRUGÍA===== " +
			    
			    "\n*  ENCABEZADO " +
			    "\n*  Codigo Esquema Tarifario ["+forma.getEsquemaTarifario("codigo_tari")+"] " +
			    "\n*  Nombre Esquema Tarifario ["+forma.getEsquemaTarifario("nombre_esquema")+"] " ;
			    
			    if(!forma.getConvenioMap("codigo").equals("")
			    		&& !forma.getConvenioMap("codigo").equals(ConstantesBD.codigoNuncaValido+""))
			    	log+="\n*  Codigo Convenio ["+forma.getConvenioMap("codigo")+"] " ;
			    else
			    	log+="\n*  Codigo Convenio [] " ;
			    	
			    log+=
			    	
			    "\n*  Nombre Convenio ["+forma.getConvenioMap("nombre_convenio")+"] " +
				"\n*  Fecha Inicial ["+forma.getFechaInicial()+"] " +
				"\n*  Fecha Final ["+forma.getFechaFinal()+"] " +			
	
				"\n*  DETALLE " +
				"\n*  Codigo [" +registro.getCodigo()+"] "+
				"\n*  Codigo Encabezado ["+registro.getCodigo_enca()+"] " +
				"\n*  Tipos Servicio ["+registro.getTipos_servicio()+"] " ;
				
				
				if(registro.getTipos_salas()>0)			    		
			    	log+="\n*  Tipos Salas ["+registro.getTipos_salas()+"] ";
				else
					log+="\n*  Tipos Salas [] ";	    		
			    	
			    log+=	
				
				"\n*  Tipo de Cirugía ["+registro.getTipoCirugia()+"] " +
				"\n*  Tipo de Asocio ["+registro.getAsocio()+"] " +
				"\n*  Tipo Especialista ["+registro.getMedico()+"] " +
				"\n*  Vía ["+registro.getVia()+"] " +
				"\n*  Liquidación ["+registro.getLiquidacion()+"] " +
				"\n*  Adicional ["+registro.getAdicional()+"] " +
				"\n*  Politraumatismo ["+registro.getPolitra()+"] " +
				"";
	
				//***************************************************
			    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN DEL DETALLE PORCENTAJE CIRUGÍA===== " +
			    "\n*  ENCABEZADO " +
			    "\n*  Codigo Esquema Tarifario ["+forma.getEsquemaTarifario("codigo_tari")+"] " +
			    "\n*  Nombre Esquema Tarifario ["+forma.getEsquemaTarifario("nombre_esquema")+"] ";
			    
			    if(!forma.getConvenioMap("codigo").equals("")
			    		&& !forma.getConvenioMap("codigo").equals(ConstantesBD.codigoNuncaValido+""))
			    	log+="\n*  Codigo Convenio ["+forma.getConvenioMap("codigo")+"] " ;
			    else
			    	log+="\n*  Codigo Convenio [] " ;
			    	
			    log+=			    
			    "\n*  Nombre Convenio ["+forma.getConvenioMap("nombre_convenio")+"] " +
				"\n*  Fecha Inicial ["+forma.getFechaInicial()+"] " +
				"\n*  Fecha Final ["+forma.getFechaFinal()+"] " +			
	
				"\n*  DETALLE " +
				"\n*  Codigo [" +registroNuevo.getCodigo()+"] "+
				"\n*  Codigo Encabezado ["+registroNuevo.getCodigo_enca()+"] " +
				"\n*  Tipos Servicio ["+registroNuevo.getTipos_servicio()+"] " ;
			    
			    if(registroNuevo.getTipos_salas()>0)			    		
			    	log+="\n*  Tipos Salas ["+registroNuevo.getTipos_salas()+"] ";
				else
					log+="\n*  Tipos Salas [] ";	    		
			    	
			    log+=				
				"\n*  Tipo de Cirugía ["+registroNuevo.getTipoCirugia()+"] " +
				"\n*  Tipo de Asocio ["+registroNuevo.getAsocio()+"] " +
				"\n*  Tipo Especialista ["+registroNuevo.getMedico()+"] " +
				"\n*  Vía ["+registroNuevo.getVia()+"] " +
				"\n*  Liquidación ["+registroNuevo.getLiquidacion()+"] " +
				"\n*  Adicional ["+registroNuevo.getAdicional()+"] " +
				"\n*  Politraumatismo ["+registroNuevo.getPolitra()+"] " +				
				"";
			}
			else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
			{
				
				//***************************************************
			    log="\n            ====INFORMACION ELIMINADA DEL DETALLE PORCENTAJE CIRUGÍA===== " +
			    "\n*  ENCABEZADO " +
			    "\n*  Codigo Esquema Tarifario ["+forma.getEsquemaTarifario("codigo_tari")+"] " +
			    "\n*  Nombre Esquema Tarifario ["+forma.getEsquemaTarifario("nombre_esquema")+"] ";
			    
			    
			    if(!forma.getConvenioMap("codigo").equals("")
			    		&& !forma.getConvenioMap("codigo").equals(ConstantesBD.codigoNuncaValido+""))
			    	log+="\n*  Codigo Convenio ["+forma.getConvenioMap("codigo")+"] " ;
			    else
			    	log+="\n*  Codigo Convenio [] " ;
			    
			    log+=
			    "\n*  Codigo Convenio ["+forma.getConvenioMap("codigo")+"] " +
			    "\n*  Nombre Convenio ["+forma.getConvenioMap("nombre_convenio")+"] " +
				"\n*  Fecha Inicial ["+forma.getFechaInicial()+"] " +
				"\n*  Fecha Final ["+forma.getFechaFinal()+"] " +
				
				"\n*  DETALLE " +
				"\n*  Codigo [" +registro.getCodigo()+"] "+
				"\n*  Codigo Encabezado ["+registro.getCodigo_enca()+"] " +
				"\n*  Tipos Servicio ["+registro.getTipos_servicio()+"] " ;
				
				
				 if(registro.getTipos_salas()>0)			    		
				    	log+="\n*  Tipos Salas ["+registro.getTipos_salas()+"] ";
					else
						log+="\n*  Tipos Salas [] ";	    		
				    	
				log+=			
				"\n*  Tipos Salas ["+registro.getTipos_salas()+"] " +
				"\n*  Tipo de Cirugía ["+registro.getTipoCirugia()+"] " +
				"\n*  Tipo de Asocio ["+registro.getAsocio()+"] " +
				"\n*  Tipo Especialista ["+registro.getMedico()+"] " +
				"\n*  Vía ["+registro.getVia()+"] " +
				"\n*  Liquidación ["+registro.getLiquidacion()+"] " +
				"\n*  Adicional ["+registro.getAdicional()+"] " +
				"\n*  Politraumatismo ["+registro.getPolitra()+"] " +							
				"";
			}
		} else if(parteTabla.equals("encabezado"))
		{
			if(tipo==ConstantesBD.tipoRegistroLogModificacion)
			{				
			    log="\n            ====INFORMACION ORIGINAL DEL ENCABEZADO DE PORCENTAJE CIRUGÍA===== " +
			    
			    "\n*  ENCABEZADO " +
			    "\n*  Codigo Encabezado ["+registro.getCodigo_enca()+"] " +
			    "\n*  Codigo Esquema Tarifario [] " +
			    "\n*  Nombre Esquema Tarifario [] " +
			    "\n*  Codigo Convenio ["+forma.getConvenioMap("codigo")+"] " +
			    "\n*  Nombre Convenio ["+forma.getConvenioMap("nombre_convenio")+"] " +
				"\n*  Fecha Inicial ["+registro.getFecha_inicial()+"] " +
				"\n*  Fecha Final ["+registro.getFecha_final()+"] " +			
				"";
	
				//***************************************************
			    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN DEL ENCABEZADO PORCENTAJE CIRUGÍA===== " +
			    "\n*  ENCABEZADO " +
			    "\n*  Codigo Encabezado ["+registroNuevo.getCodigo_enca()+"] " +
			    "\n*  Codigo Esquema Tarifario [] " +
			    "\n*  Nombre Esquema Tarifario [] " +
			    "\n*  Codigo Convenio ["+forma.getConvenioMap("codigo")+"] " +
			    "\n*  Nombre Convenio ["+forma.getConvenioMap("nombre_convenio")+"] " +
				"\n*  Fecha Inicial ["+registroNuevo.getFecha_inicial()+"] " +
				"\n*  Fecha Final ["+registroNuevo.getFecha_final()+"] " +			
				"";
			}
			else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
			{
				
				//***************************************************
				 log="\n            ====INFORMACION ELIMINADA DEL ENCABEZADO DE PORCENTAJE CIRUGÍA===== " +				    
			    "\n*  ENCABEZADO " +
			    "\n*  Codigo Encabezado ["+registro.getCodigo_enca()+"] " +
			    "\n*  Codigo Esquema Tarifario [] " +
			    "\n*  Nombre Esquema Tarifario [] " +
			    "\n*  Codigo Convenio ["+forma.getConvenioMap("codigo")+"] " +
			    "\n*  Nombre Convenio ["+forma.getConvenioMap("nombre_convenio")+"] " +
				"\n*  Fecha Inicial ["+registro.getFecha_inicial()+"] " +
				"\n*  Fecha Final ["+registro.getFecha_final()+"] " +			
				"";
			}			
		}		
		
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logPorcentajesCxMultiplesCodigo, log, tipo,usuario.getLoginUsuario());
		
	}	
	
	//**************************************************************************************
	

	/**
	 * Método usado para realizar procesos entre cambio de página del pager
	 * @param con
	 * @param porcentajesForm
	 * @param response
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, PorcentajesCxMultiplesForm porcentajesForm, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) {
		try
		{
			
		    UtilidadBD.cerrarConexion(con);
			response.sendRedirect(porcentajesForm.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de PorcentajesCxMultiplesAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en PorcentajesCxMultiplesAction", "errors.problemasDatos", true);
		}
	}	
}	