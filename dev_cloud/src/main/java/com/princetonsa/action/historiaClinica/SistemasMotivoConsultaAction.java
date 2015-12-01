/*
 * @(#)CentrosCostoAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;

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

import com.princetonsa.actionform.historiaClinica.SistemasMotivoConsultaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.SistemasMotivoConsulta;

/**
 * Clase encargada del control de la funcionalidad de 
 * Sistemas Motivos de Consulta de Urgencias

 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 31 /May/ 2006
 */
public class SistemasMotivoConsultaAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(SistemasMotivoConsultaAction.class);
	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try {
			if(form instanceof SistemasMotivoConsultaForm)
			{
				/**Intentamos abrir una conexion con la fuente de datos**/ 
				con = openDBConnection(con); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				HttpSession session = request.getSession();
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");

				SistemasMotivoConsulta mundo = new SistemasMotivoConsulta();
				SistemasMotivoConsultaForm forma = (SistemasMotivoConsultaForm)form;

				String estado = forma.getEstado();
				logger.warn("[SistemasMotivoConsultaAction] estado->"+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de SistemasMotivoConsultaAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(forma, mapping, con, mundo, usuario);
				}
				else if(estado.equals("eliminarDelMapa"))
				{
					return this.accionEliminarDelMapa(forma, request, mapping, con, mundo, usuario);
				}
				else if(estado.equals("guardar"))
				{
					return this.accionGuardar(forma, mapping, con, mundo, usuario);
				}
				else if(estado.equals("ordenarColumna"))
				{
					return this.accionOrdenarColumna(con, mapping, forma);
				}
				else if(estado.equals("nuevo"))
				{
					return this.accionAgregarNuevo(forma, mapping, con, usuario);
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de SistemasMotivoConsultaAction");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
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
	 * Action para empezar el flujo de la funcionalidad consultando los motivos existentes
	 * En caso de que no exista ningun motivo se le adicina un registro al mapa para poder
	 * continuar el flujo de la funcionalidad
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param mundo
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(SistemasMotivoConsultaForm forma, ActionMapping mapping,  Connection con, SistemasMotivoConsulta mundo, UsuarioBasico usuario) throws SQLException
	{
		forma.reset();
		forma.resetMensaje();
		forma.setMapaMotivosConsulta(mundo.consultarMotivosConsultaUrg(con, usuario.getCodigoInstitucionInt()));
		forma.setMapaMotivosConsultaNoModificado(mundo.consultarMotivosConsultaUrg(con, usuario.getCodigoInstitucionInt()));
		if(Integer.parseInt(forma.getMapaMotivosConsulta("numRegistros").toString()) <= 0)
		{
			forma.setMapaMotivosConsulta("numRegistros", 1);
			forma.getMapaMotivosConsulta().put("codigo_0", 0);
			forma.getMapaMotivosConsulta().put("identificador_0", "");
			forma.getMapaMotivosConsulta().put("descripcion_0", "");
			forma.getMapaMotivosConsulta().put("institucion_0", "");
			forma.getMapaMotivosConsulta().put("existebd_0", "no");
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");		
		
	}
	
	/**
	 * Action para elimianr del mapa un registro. Si el registro si existia en la
	 * Base de Datos entonces genera el LOG de Eliminacion, de lo contrario
	 * solo lo elimina del Mapa
	 * @param forma
	 * @param request
	 * @param mapping
	 * @param con
	 * @param mundo
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEliminarDelMapa(SistemasMotivoConsultaForm forma, HttpServletRequest request, ActionMapping mapping, Connection con, SistemasMotivoConsulta mundo, UsuarioBasico usuario) throws SQLException
	{
	    int tamanioMapaAntesEliminar = Integer.parseInt(forma.getMapaMotivosConsulta("numRegistros")+"");
	    ActionErrors errores = new ActionErrors();
	    int eliminacion = 0;
	    String existeBD = forma.getMapaMotivosConsulta("existebd_"+forma.getPosicionMapa()).toString();
	    UtilidadBD.iniciarTransaccion(con);
	    if(existeBD.equals("si"))
	    {
				eliminacion = mundo.eliminarMotivoConsultaUrg(con, Integer.parseInt(forma.getMapaMotivosConsulta("codigo_"+forma.getPosicionMapa()).toString()));
				if(eliminacion == ConstantesBD.codigoNuncaValido)
				{
					errores.add("problemas Eliminando", new ActionMessage("error.historiaClinica.MotivosConsultaUrgencias.noSeEliminoRegistro", forma.getMapaMotivosConsulta("descripcion_"+forma.getPosicionMapa()).toString()));
				}
				else if(eliminacion == ConstantesBD.excepcionViolacionLlaveForanea)
				{
					errores.add("registro utilizado", new ActionMessage("error.historiaClinica.MotivosConusltaUrgencias.motivoUtilizadoEnFuncionalidad", forma.getMapaMotivosConsulta("descripcion_"+forma.getPosicionMapa()).toString()));
				}
				else
				{
					//Si lo borra Bien entonces generamos el LOG
					generarLogEliminacion(forma, usuario, forma.getPosicionMapa());
					forma.setMensaje("Proceso de Eliminación realizado con Éxito");
				}
	    }
	    if(errores.size() > 0)
		{
	    	UtilidadBD.abortarTransaccion(con);
	        saveErrors(request, errores);
	        UtilidadBD.closeConnection(con);
	        return mapping.findForward("paginaPrincipal");
		}
	    
	    //Si pasa todas las validaciones entonces finalizamos la transaccion
	    UtilidadBD.finalizarTransaccion(con);
	    
	    forma.getMapaMotivosConsulta().remove("codigo_"+forma.getPosicionMapa());
	    forma.getMapaMotivosConsulta().remove("identificador_"+forma.getPosicionMapa());
	    forma.getMapaMotivosConsulta().remove("descripcion_"+forma.getPosicionMapa());
	    forma.getMapaMotivosConsulta().remove("institucion_"+forma.getPosicionMapa());
	    forma.getMapaMotivosConsulta().remove("existebd_"+forma.getPosicionMapa());
	    forma.setMapaMotivosConsulta("numRegistros",tamanioMapaAntesEliminar-1+"");
	    
	   //El siguiente cod, lo que verifica, es que si eliminan un elemento del mapa y atrás de el existen más datos entonces
	   //se debe correr el index del hash Map de los que estan después del eliminado una posición menos para que existe 
	   //concordancia entre el size real del mapa y el index de elementos
	   int indexEliminado = forma.getPosicionMapa();
	    if(tamanioMapaAntesEliminar > indexEliminado)
	    {
	        for(int k = indexEliminado ; k < tamanioMapaAntesEliminar ; k++)
	        {
	        	forma.setMapaMotivosConsulta("codigo_"+k, forma.getMapaMotivosConsulta("codigo_"+(k+1)));
	        	forma.setMapaMotivosConsulta("identificador_"+k, forma.getMapaMotivosConsulta("identificador_"+(k+1)));
	        	forma.setMapaMotivosConsulta("descripcion_"+k, forma.getMapaMotivosConsulta("descripcion_"+(k+1)));
	        	forma.setMapaMotivosConsulta("institucion_"+k, forma.getMapaMotivosConsulta("institucion_"+(k+1)));
	        	forma.setMapaMotivosConsulta("existebd_"+k, forma.getMapaMotivosConsulta("existebd_"+(k+1)));
	        }
	        //Removemos la ultima posicion del mapa que quedo vacias despues de mover los otros registros
	        forma.getMapaMotivosConsulta().remove("codigo_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaMotivosConsulta().remove("identificador_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaMotivosConsulta().remove("descripcion_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaMotivosConsulta().remove("institucion_"+(tamanioMapaAntesEliminar-1));
	        forma.getMapaMotivosConsulta().remove("existebd_"+(tamanioMapaAntesEliminar-1));
	    }
	    
	    UtilidadBD.closeConnection(con);
	    return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * Action para guardar las modificaciones y/o las nuevas inserciones de los
	 * Sistemas motivos de consultas de urgencias
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param mundo
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGuardar(SistemasMotivoConsultaForm forma, ActionMapping mapping, Connection con, SistemasMotivoConsulta mundo, UsuarioBasico usuario) throws SQLException
	{
		int insercion = 0;
		String codigo = "";
		UtilidadBD.iniciarTransaccion(con);
		for(int i = 0 ; i < Integer.parseInt(forma.getMapaMotivosConsulta("numRegistros").toString()) ; i++)
		{
			if(!forma.getMapaMotivosConsulta("codigo_"+i).toString().equals(""))
    		{
    			codigo = forma.getMapaMotivosConsulta("codigo_"+i).toString();
    		}
   			insercion = mundo.insertarMotivoConsultaUrg(con, codigo, forma.getMapaMotivosConsulta("descripcion_"+i).toString(), forma.getMapaMotivosConsulta("identificador_"+i).toString(), usuario.getCodigoInstitucionInt());
   			if(insercion == ConstantesBD.tipoRegistroLogModificacion)
   			{
   				//Si la insercion devuelve el codigo de modificacion(2) generamos el los de modificacion
   				generarLogModificacion(forma, usuario, i);
   			}
    		if(insercion == ConstantesBD.codigoNuncaValido)
    		{
    			UtilidadBD.abortarTransaccion(con);
    			forma.setMensaje("Problemas con la Inserción de los datos. Por favor verifique");
    			UtilidadBD.closeConnection(con);
    	        return mapping.findForward("paginaPrincipal");
    		}
	    }
	    
    	UtilidadBD.finalizarTransaccion(con);
    	forma.setMensaje("Proceso de Inserción realizado con Éxito");
    	forma.setMapaMotivosConsulta(mundo.consultarMotivosConsultaUrg(con, usuario.getCodigoInstitucionInt()));
    	UtilidadBD.cerrarConexion(con);
        return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * Action para agregar un nuevo registro al mapa
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionAgregarNuevo(SistemasMotivoConsultaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) throws SQLException
	{
		forma.resetMensaje();
	    int index=Integer.parseInt(forma.getMapaMotivosConsulta("numRegistros").toString());
		forma.getMapaMotivosConsulta().put("codigo_"+index, "0");
		forma.getMapaMotivosConsulta().put("identificador_"+index, "");
		forma.getMapaMotivosConsulta().put("descripcion_"+index, "");
		forma.getMapaMotivosConsulta().put("institucion_"+index, usuario.getCodigoInstitucionInt());
		forma.getMapaMotivosConsulta().put("existebd_"+index, "no");
		index=index+1;
		forma.setMapaMotivosConsulta("numRegistros", index+"");
				
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * Action para permitir el ordenamiento por cada una de las columnas
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionOrdenarColumna(Connection con, ActionMapping mapping, SistemasMotivoConsultaForm forma) 
    {
		forma.resetMensaje();
        String[] indices = {
				            "codigo_", 
				            "identificador_", 
				            "descripcion_", 
							"institucion_",
				            "existebd_"
	            		   };
        
        int tmp = Integer.parseInt(forma.getMapaMotivosConsulta("numRegistros")+"");
        forma.setMapaMotivosConsulta(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaMotivosConsulta(),Integer.parseInt(forma.getMapaMotivosConsulta("numRegistros")+"")));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.setMapaMotivosConsulta("numRegistros",tmp+"");
        UtilidadBD.closeConnection(con);
        return mapping.findForward("paginaPrincipal");  
    }
	
	
	/**
	 * Metodo para generar Log de Modificacion
	 * @param forma
	 * @param usuario
	 * @param index
	 */
	private void generarLogModificacion(SistemasMotivoConsultaForm forma,  UsuarioBasico usuario, int index)
	{
	    String log="\n            ==== INFORMACION ORIGINAL SISTEMAS MOTIVO CONSULTA DE URGENCIAS ===== ";

	    	log+=
		    "\n*  Código [" + forma.getMapaMotivosConsultaNoModificado("identificador_"+index) +"] "+
			"\n*  Descripción ["+forma.getMapaMotivosConsultaNoModificado("descripcion_"+index)+"] " ;
	    	
		log+="\n          =====INFORMACION DESPUES DE LA MODIFICACION SISTEMAS MOTIVO CONSULTA DE URGENCIAS ===== " ;
		
			log+=
				"\n*  Código [" + forma.getMapaMotivosConsulta("identificador_"+index) +"] "+
				"\n*  Descripción ["+forma.getMapaMotivosConsulta("descripcion_"+index)+"] " ;
		log+="\n========================================================\n\n\n " ;	
		
		LogsAxioma.enviarLog(ConstantesBD.logSistemasMotivoConsultaUrgCodigo, log, ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
	}
	
	
	/**
	 * Metodo para generar el LOG de la eliminacion de un Motivo de Consulta
	 * de Urgencias
	 * @param forma
	 * @param usuario
	 * @param index
	 */
	private void generarLogEliminacion(SistemasMotivoConsultaForm forma,  UsuarioBasico usuario, int index)
	{
	    String log="\n          =====INFORMACION DEL MOTIVO CONSULTA URGENCIAS ELIMINADO ===== " ;
		log+=
			"\n*  Código ["+forma.getMapaMotivosConsulta("identificador_"+index).toString()+"] " +
			"\n*  Descripción  ["+forma.getMapaMotivosConsulta("descripcion_"+index).toString()+"] " ;
		log+="\n========================================================\n\n\n " ;	
		
		LogsAxioma.enviarLog(ConstantesBD.logSistemasMotivoConsultaUrgCodigo, log, ConstantesBD.tipoRegistroLogEliminacion, usuario.getLoginUsuario());
	}
	
	/**
	 * Abrir la conceccion con la Base de Datos
	 * @param con
	 * @return
	 */
	public Connection openDBConnection(Connection con)
	{
		if(con != null)
		{
			return con;
		}
		try
		{
			String tipoBD = System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			con = myFactory.getConnection();
		}
		catch(Exception e)
		{
			logger.warn("Problemas con la base de datos al abrir la conexion "+e.toString());
			return null;
		}
	
		return con;
	}
	
}