/*
 * Ene 18, 2006
 *
 */
package com.princetonsa.action.facturacion;

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
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.GruposServiciosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.GruposServicios;

/**
 * @author Sebastián Gómez 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Parametrización de Grupos de Servicios
 */
public class GruposServiciosAction extends Action 
{
	
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(GruposServiciosAction.class);
	
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
			if(form instanceof GruposServiciosForm)
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
				GruposServiciosForm gruposForm =(GruposServiciosForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=gruposForm.getEstado(); 
				logger.info("estado Grupos Servicios-->"+estado);


				if(estado == null)
				{
					gruposForm.reset();	
					logger.warn("Estado no valido dentro del flujo de GRupos Servicios (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

				//*******ESTADOS DE INGRESAR/MODIFICAR GRUPOS DE SERVICIOS**********************
				else if (estado.equals("empezarIngresar"))
				{
					return accionEmpezarIngresar(con,gruposForm,mapping, usuario.getCodigoInstitucionInt());
				}
				else if (estado.equals("nuevo"))
				{
					return accionNuevo(con,gruposForm,mapping);
				}
				else if (estado.equals("guardar"))
				{
					return accionGuardar(con,gruposForm,mapping,usuario,request);
				}
				else if (estado.equals("ordenar"))
				{
					return accionOrdenar(con,gruposForm,mapping);
				}
				else if(estado.equals("eliminar"))
				{
					return accionEliminar(con,gruposForm,mapping,usuario,request);
				}
				else if(estado.equals("diasTramite"))
				{
					String diasUrgente=request.getParameter("numDiasUrgente");
					String acroUrgente=request.getParameter("acroDiasUrgente");
					String diasNormal=request.getParameter("numDiasNormal");
					String acroNormal=request.getParameter("acroDiasNormal");
					String index=request.getParameter("index");
					gruposForm.setGruposMap("numDiasUrgente_"+index, diasUrgente);
					gruposForm.setGruposMap("acroDiasUrgente_"+index, acroUrgente);
					gruposForm.setGruposMap("numDiasNormal_"+index, diasNormal);
					gruposForm.setGruposMap("acroDiasNormal_"+index, acroNormal);
					return null;
				}
				else
				{
					gruposForm.reset();
					logger.warn("Estado no valido dentro del flujo de Grupos de Servicios (null) ");
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
	 * Método implementado para eliminar un grupo dentro del listado
	 * de registros existentes y de la BD en el caso de que exista
	 * @param con
	 * @param gruposForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEliminar(Connection con, GruposServiciosForm gruposForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//se toma la posición a eliminar
		int pos = gruposForm.getPos();
		//se toma el código del registro a eliminar
		int auxI0 = Integer.parseInt(gruposForm.getGruposMap("codigo_"+pos)+"");
		int auxI1 = 0;
		boolean exito = true;
		
		//se verifica si el registro existe
		if(auxI0>0)
		{
			//SE DEBE ELIMINAR DESDE LA BD
			
			GruposServicios mundo = new GruposServicios();
			this.llenarMundo(gruposForm,mundo,pos);
			
			
				
			auxI1 = mundo.eliminar(con);
			
			if(auxI1>0)
				//se genera el LOG Eliminación
				this.generarLog(null,mundo,ConstantesBD.tipoRegistroLogEliminacion,usuario);
			else
			{
				//Se reporta el error
				ActionErrors errores = new ActionErrors();
				errores.add("Error al eliminar",new ActionMessage("errors.sinEliminar"));
				saveErrors(request,errores);
				exito = false;
			}
				
			
		}
		
		
		//Se elimina registro del mapa
		//si la transacción tuvo éxito
		if(exito)
		{
			for(int i=pos;i<(gruposForm.getNumGrupos()-1);i++)
			{
				gruposForm.setGruposMap("codigo_"+i,gruposForm.getGruposMap("codigo_"+(i+1)));
				gruposForm.setGruposMap("descripcion_"+i,gruposForm.getGruposMap("descripcion_"+(i+1)));
				gruposForm.setGruposMap("acronimo_"+i,gruposForm.getGruposMap("acronimo_"+(i+1)));
				gruposForm.setGruposMap("activo_"+i,gruposForm.getGruposMap("activo_"+(i+1)));
				gruposForm.setGruposMap("es_usado_"+i,gruposForm.getGruposMap("es_usado_"+(i+1)));
				gruposForm.setGruposMap("multiple_"+i,gruposForm.getGruposMap("multiple_"+(i+1)));
				gruposForm.setGruposMap("tipo_"+i,gruposForm.getGruposMap("tipo_"+(i+1)));
				gruposForm.setGruposMap("numDiasUrgente_"+i,gruposForm.getGruposMap("numDiasUrgente_"+(i+1)));
				gruposForm.setGruposMap("acroDiasUrgente_"+i,gruposForm.getGruposMap("acroDiasUrgente_"+(i+1)));
				gruposForm.setGruposMap("numDiasNormal_"+i,gruposForm.getGruposMap("numDiasNormal_"+(i+1)));
				gruposForm.setGruposMap("acroDiasNormal_"+i,gruposForm.getGruposMap("acroDiasNormal_"+(i+1)));
				gruposForm.setGruposMap("tipoMonto_"+i,gruposForm.getGruposMap("tipoMonto_"+(i+1)));
			}
			
			pos = gruposForm.getNumGrupos();
			
			gruposForm.getGruposMap().remove("codigo_"+pos);
			gruposForm.getGruposMap().remove("descripcion_"+pos);
			gruposForm.getGruposMap().remove("acronimo_"+pos);
			gruposForm.getGruposMap().remove("activo_"+pos);
			gruposForm.getGruposMap().remove("es_usado_"+pos);
			gruposForm.getGruposMap().remove("multiple_"+pos);
			gruposForm.getGruposMap().remove("tipo_"+pos);
			gruposForm.getGruposMap().remove("numDiasUrgente_"+pos);
			gruposForm.getGruposMap().remove("acroDiasUrgente_"+pos);
			gruposForm.getGruposMap().remove("numDiasNormal_"+pos);
			gruposForm.getGruposMap().remove("acroDiasNormal_"+pos);
			gruposForm.getGruposMap().remove("tipoMonto_"+pos);
			
			pos--;
			
			gruposForm.setNumGrupos(pos);
			gruposForm.setGruposMap("numRegistros",pos+"");
			
			gruposForm.setEstado("guardar");
		}
		else
			gruposForm.setEstado("empezarIngresar");
		
		
		this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * Método implementado para ordenar el listado
	 * de grupos de servicios
	 * @param con
	 * @param gruposForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, GruposServiciosForm gruposForm, ActionMapping mapping) 
	{
		
		int numRegistros=Utilidades.convertirAEntero(gruposForm.getGruposMap("numRegistros")+"");
		
		String[] indices={
				"codigo_",
				"acronimo_",
				"descripcion_",
				"activo_",
				"es_usado_",
				"multiple_",
				"tipo_",
				"tipoSalaStandar_",
				"tipoMonto_"
			};
		
		
		
		gruposForm.setGruposMap(Listado.ordenarMapa(indices,
				gruposForm.getIndice(),
				gruposForm.getUltimoIndice(),
				gruposForm.getGruposMap(),
				numRegistros));
		
		gruposForm.setGruposMap("numRegistros",numRegistros);
		gruposForm.setUltimoIndice(gruposForm.getIndice());
		
		gruposForm.setEstado("empezarIngresar");
		this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * Método implementado para insertar/modificar los registros de grupos de servicios
	 * listados en la vista 
	 * @param con
	 * @param gruposForm
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, GruposServiciosForm gruposForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//variables auxiliares
		int auxI0 = 0;
		int auxI1 = 0;
		String insertarError = "";
		String modificarError = "";
		
		
		//se instancia mundo de Grupos de Servicios
		GruposServicios mundo = new GruposServicios();
		
		//iteración de los registros a insertar/modificar
		for(int i=0;i<gruposForm.getNumGrupos();i++)
		{
			mundo.clean();
			auxI0 = Integer.parseInt(gruposForm.getGruposMap("codigo_"+i)+"");
			
			
			//se verifica si el registro es nuevo o existente
			if(auxI0<0)
			{
				//EL REGISTRO NO EXISTE: SE DEBE INSERTAR
				this.llenarMundo(gruposForm,mundo,i);
				auxI1 = mundo.insertar(con, usuario.getCodigoInstitucionInt());
				
				//se verifica si hubo éxito al insertar el registro
				if(auxI1<=0)
				{
					if(!insertarError.equals(""))
						insertarError += ",";
					insertarError += mundo.getAcronimo();
				}
			}
			else
			{
				//EL REGISTRO EXISTE: SE DEBE MODIFICAR
				
				//se consulta registro antiguo
				mundo.setCodigo(auxI0);
				HashMap registroAntiguo = mundo.busquedaGrupos(con);
				
				this.llenarMundo(gruposForm,mundo,i);
				
				//se verifica si fue modificado
				if(this.fueModificado(registroAntiguo,mundo))
				{
					auxI1 = mundo.modificar(con);
					
					//se verifica si hubo éxito al insertar el registro
					if(auxI1<=0)
					{
						if(!modificarError.equals(""))
							modificarError += ",";
						modificarError += mundo.getAcronimo();
					}
					else
					{
						//se genera LOG tipo archivo
						this.generarLog(registroAntiguo,mundo,ConstantesBD.tipoRegistroLogModificacion,usuario);
					}
				}
			}
			
		}
		
		//Edicion de los errores
		ActionErrors errores = new ActionErrors();
		
		//se verifica si hubo errores en la insercion
		if(!insertarError.equals(""))
		{
			if(insertarError.split(",").length>1)
				insertarError = "Los registros "+ insertarError + " no pudieron ser insertados ";
			else
				insertarError = "El registro "+ insertarError + " no pudo ser insertado ";
				
			errores.add("Registros no insertados",new ActionMessage("errors.notEspecific",insertarError));
			
			gruposForm.setEstado("empezarIngresar");
		}
		
		if(!modificarError.equals(""))
		{
			if(modificarError.split(",").length>1)
				modificarError = "Los registros "+ modificarError + " no pudieron ser modificados ";
			else
				modificarError = "El registro "+ modificarError + " no pudo ser modificado ";
				
			errores.add("Registros no modificados",new ActionMessage("errors.notEspecific",modificarError));
			
			gruposForm.setEstado("empezarIngresar");
		}
		
		saveErrors(request,errores);
		
		return accionEmpezarIngresar(con,gruposForm,mapping, usuario.getCodigoInstitucionInt());
	}

	/**
	 * Método implementado para generar el LOG Modificacion
	 * de registros Grupos de Servicios
	 * @param registro (antiguo)
	 * @param mundo (nuevo)
	 * @param tipo
	 * @param usuario
	 */
	private void generarLog(HashMap registro, GruposServicios mundo, int tipo, UsuarioBasico usuario) 
	{
		String log="";
	    
		if(tipo==ConstantesBD.tipoRegistroLogModificacion)
		{
		    log="\n            ====INFORMACION ORIGINAL GRUPO DE SERVICIO===== " +
			"\n*  Código [" +registro.get("acronimo_0")+"] "+
			"\n*  Descripción ["+registro.get("descripcion_0")+"] " +
			""  ;
		    
		    if(UtilidadTexto.getBoolean(registro.get("multiple_0")+""))
		    	log+="\n*  Es Multiple? [SI]";
		    else
		    	log+="\n*  Es Multiple? [NO]";
		    
		    log += "\n*  Tipo ["+registro.get("tipo_0")+"] " +
		    ""  ;
		    
		    if(UtilidadTexto.getBoolean(registro.get("activo_0")+""))
		    	log+="\n*  Es Activo? [SI]";
		    else
		    	log+="\n*  Es Activo? [NO]";
		    
		    log += "\n*  Tipo Sala Standar["+registro.get("tipoSalaStandar_0")+"] " +
		    ""  ;
		    
		    log += "\n*  Numero Dias Urgente["+registro.get("numDiasUrgente_0")+"] " +
		    ""  ;
		    
		    log += "\n*  Acronimo Dias Urgente["+registro.get("acroDiasUrgente_0")+"] " +
		    ""  ;
		    
		    log += "\n*  Numero Dias Normal["+registro.get("numDiasNormal_0")+"] " +
		    ""  ;
		    
		    log += "\n*  Acronimo Dias Normal["+registro.get("acroDiasNormal_0")+"] " +
		    ""  ;
		    
		    log += "\n*  Tipo Monto Autorizacion["+registro.get("tipoMonto_0")+"] " +
		    ""  ;

		    log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN GRUPO DE SERVICIO===== " +
			"\n*  Código [" +mundo.getAcronimo()+"] "+
			"\n*  Descripción ["+mundo.getDescripcion()+"] " +
			""  ;
		    
		    if(UtilidadTexto.getBoolean(mundo.getMultiple()))
		    	log+="\n*  Es Multiple? [SI]";
		    else
		    	log+="\n*  Es Multiple? [NO]";

		    log+= "\n*  Tipo ["+mundo.getTipo()+"] " +
		    ""  ;
		    
		    if(UtilidadTexto.getBoolean(mundo.getActivo()))
		    	log+="\n*  Es Activo? [SI]";
		    else
		    	log+="\n*  Es Activo? [NO]";
		    
		    log += "\n*  Tipo Sala Standar["+mundo.getTipoSalaStandar()+"] " +
		    ""  ;
		    
		    log += "\n*  Numero Dias Urgente["+mundo.getNumDiasUrgente()+"] " +
		    ""  ;
		    
		    log += "\n*  Acronimo Dias Urgente["+mundo.getAcroDiasUrgente()+"] " +
		    ""  ;
		    
		    log += "\n*  Numero Dias Normal["+mundo.getNumDiasNormal()+"] " +
		    ""  ;
		    
		    log += "\n*  Acronimo Dias Normal["+mundo.getAcroDiasNormal()+"] " +
		    ""  ;
		    
		    log += "\n*  Tipo Monto Autorizacion["+mundo.getTipoMonto()+"] " +
		    ""  ;
		}
		else if(tipo==ConstantesBD.tipoRegistroLogEliminacion)
		{
		
		    log="\n            ====INFORMACION ELIMINADA DE GRUPO DE SERVICIO===== " +
			"\n*  Código [" +mundo.getAcronimo()+"] "+
			"\n*  Descripción ["+mundo.getDescripcion()+"] " +
			""  ;
		    
		    if(UtilidadTexto.getBoolean(mundo.getMultiple()))
		    	log+="\n*  Es Multiple? [SI]";
		    else
		    	log+="\n*  Es Multiple? [NO]";
		    
		    log+= "\n*  Tipo ["+mundo.getTipo()+"] " +
		    ""  ;
		    
		    if(UtilidadTexto.getBoolean(mundo.getActivo()))
		    	log+="\n*  Es Activo? [SI]";
		    else
		    	log+="\n*  Es Activo? [NO]";
		    
		    log += "\n*  Tipo Sala Standar["+mundo.getTipoSalaStandar()+"] " +
		    ""  ;
		    
		    log += "\n*  Numero Dias Urgente["+mundo.getNumDiasUrgente()+"] " +
		    ""  ;
		    
		    log += "\n*  Acronimo Dias Urgente["+mundo.getAcroDiasUrgente()+"] " +
		    ""  ;
		    
		    log += "\n*  Numero Dias Normal["+mundo.getNumDiasNormal()+"] " +
		    ""  ;
		    
		    log += "\n*  Acronimo Dias Normal["+mundo.getAcroDiasNormal()+"] " +
		    ""  ;
		    
		    log += "\n*  Tipo Monto Autorizacion["+mundo.getTipoMonto()+"] " +
		    ""  ;
		}
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logGruposServiciosCodigo, log, tipo,usuario.getLoginUsuario());
		
	}

	/**
	 * Método implementado para verificar si un registro de grupos de servicios
	 * fue modificado
	 * @param mapa (antiguo)
	 * @param mundo (nuevo)
	 * @return
	 */
	private boolean fueModificado(HashMap mapa, GruposServicios mundo) 
	{
		boolean fueModificado = false;
		String auxS0 = "";
		
		//se verifica descripcion
		auxS0 = mapa.get("descripcion_0") + "";
		if(!auxS0.equals(mundo.getDescripcion()))
			fueModificado = true;
		
		//se verifica multiple
		auxS0 = mapa.get("multiple_0") + "";
		if(!auxS0.equals(mundo.getMultiple()))
			fueModificado = true;
		
		//se verifica tipo
		auxS0 = mapa.get("tipo_0") + "";
		if(!auxS0.equals(mundo.getTipo()))
			fueModificado = true;
		
		//se verifica activo
		auxS0 = mapa.get("activo_0") + "";
		if(!auxS0.equals(mundo.getActivo()))
			fueModificado = true;
		
		//se verifica tipo Sala Standar
		auxS0 = mapa.get("tipoSalaStandar_0") + "";
		if(!auxS0.equals(mundo.getTipoSalaStandar()))
			fueModificado = true;
		
		//se verifica Número de Dias Urgente
		auxS0 = mapa.get("numDiasUrgente_0") + "";
		if(!auxS0.equals(mundo.getNumDiasUrgente()+""))
			fueModificado = true;
		
		//se verifica Acronimo Dias Urgente
		auxS0 = mapa.get("acroDiasUrgente_0") + "";
		if(!auxS0.equals(mundo.getAcroDiasUrgente()))
			fueModificado = true;
		
		//se verifica Número Dias Normal
		auxS0 = mapa.get("numDiasNormal_0") + "";
		if(!auxS0.equals(mundo.getNumDiasNormal()+""))
			fueModificado = true;
		
		//se verifica Acronimo Dias Normal
		auxS0 = mapa.get("acroDiasNormal_0") + "";
		if(!auxS0.equals(mundo.getAcroDiasNormal()))
			fueModificado = true;
		
		//se verifica tipo Afiliado
		auxS0 = mapa.get("tipoAfiliado_0") + "";
		if(!auxS0.equals(mundo.getTipoMonto()))
			fueModificado = true;
		
		return fueModificado;
	}

	/**
	 * Método implementado para cargar el  mundo de GruposServicios
	 * con los datos de la forma
	 * @param gruposForm
	 * @param mundo
	 * @param pos (posicion del registro del mapa)
	 */
	private void llenarMundo(GruposServiciosForm gruposForm, GruposServicios mundo, int pos) 
	{
		mundo.setCodigo(Integer.parseInt(gruposForm.getGruposMap("codigo_"+pos)+""));
		mundo.setAcronimo(gruposForm.getGruposMap("acronimo_"+pos)+"");
		mundo.setDescripcion(gruposForm.getGruposMap("descripcion_"+pos)+"");
		mundo.setActivo(gruposForm.getGruposMap("activo_"+pos)+"");
		mundo.setMultiple(gruposForm.getGruposMap("multiple_"+pos)+"");
		mundo.setTipo(gruposForm.getGruposMap("tipo_"+pos)+"");
		mundo.setTipoSalaStandar(gruposForm.getGruposMap("tipoSalaStandar_"+pos)+"");
		mundo.setNumDiasUrgente(gruposForm.getGruposMap("numDiasUrgente_"+pos)+"");
		mundo.setAcroDiasUrgente(gruposForm.getGruposMap("acroDiasUrgente_"+pos)+"");
		mundo.setNumDiasNormal(gruposForm.getGruposMap("numDiasNormal_"+pos)+"");
		mundo.setAcroDiasNormal(gruposForm.getGruposMap("acroDiasNormal_"+pos)+"");
		mundo.setTipoMonto(Integer.parseInt(gruposForm.getGruposMap("tipoMonto_"+pos)+""));
		
	}

	/**
	 * Método implementado para ingresar un registro vacío en el mapa de grupos de Servicios
	 * @param con
	 * @param gruposForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionNuevo(Connection con, GruposServiciosForm gruposForm, ActionMapping mapping) 
	{
		int pos = gruposForm.getNumGrupos();
		
		//creación de un nuevo registro
		gruposForm.setGruposMap("codigo_"+pos,"-1");
		gruposForm.setGruposMap("acronimo_"+pos,"");
		gruposForm.setGruposMap("descripcion_"+pos,"");
		gruposForm.setGruposMap("activo_"+pos,"true");
		gruposForm.setGruposMap("es_usado_"+pos,"false");
		gruposForm.setGruposMap("multiple_"+pos,"false");
		gruposForm.setGruposMap("tipo_"+pos,"");
		gruposForm.setGruposMap("numDiasUrgente_"+pos,"");
		gruposForm.setGruposMap("acroDiasUrgente_"+pos,"");
		gruposForm.setGruposMap("numDiasNormal_"+pos,"");
		gruposForm.setGruposMap("acroDiasNormal_"+pos,"");
		gruposForm.setGruposMap("tipoMonto_"+pos,"-1");
		
		pos++;
		//se asigna un nuevo tamaño
		gruposForm.setNumGrupos(pos);
		
		this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");
	}

	/**
	 * Método implementado para postular el inicio de la funcionalidad
	 * Ingresar/Modificar de Grupos de Servicios
	 * @param con
	 * @param gruposForm
	 * @param mapping
	 * @param codInstitucion 
	 * @return
	 */
	private ActionForward accionEmpezarIngresar(Connection con, GruposServiciosForm gruposForm, ActionMapping mapping, int codInstitucion) 
	{
		String estado = gruposForm.getEstado();
		//se limpia formulario
		gruposForm.reset();
		gruposForm.setEstado(estado);
		
		//se instancia mundo de GruposServicios
		GruposServicios mundo = new GruposServicios();
		
		//se consultan los grupos de servicios existentes
		gruposForm.setGruposMap(mundo.consultarGrupos(con, codInstitucion));
		//logger.info("===> Los datos del mapa son: "+gruposForm.getGruposMap());
		//se asigna el tamaño de la consulta
		gruposForm.setNumGrupos(Utilidades.convertirAEntero(gruposForm.getGruposMap("numRegistros")+""));
		
		/*
		 * Se cargan todos tipos de salas que tengan es_quirurgica == true
		 */
		gruposForm.setTiposSalas(mundo.obtenerListaSalas(con, codInstitucion));
		logger.info("===> Voy a cargar los tipos de salas que tengan es_quirurgica = true"+gruposForm.getTiposSalas());
		
		/*
		 * Se cargan todos los tipos de montos
		 */
		gruposForm.setTiposMontos(mundo.obtenerListaTiposMontos(con));
		
		this.cerrarConexion(con);
		return mapping.findForward("paginaPrincipal");
	}
	
	
	
	
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	private void cerrarConexion (Connection con)
	{
	    try
		{
	        UtilidadBD.cerrarConexion(con);
	    }
	    catch(Exception e)
		{
	        logger.error(e+"Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: " +e.toString());
	    }
	}
}
