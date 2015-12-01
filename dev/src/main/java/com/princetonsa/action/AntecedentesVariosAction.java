/*
 * @(#)AntecedentesVariosAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;
import util.UtilidadTexto;
import util.UtilidadValidacion;

import com.princetonsa.actionform.AntecedentesVariosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.antecedentes.AntecedenteVario;

/**
 * Esta clase es la encargada de hacer las últimas validaciones y relaciónes con el mundo 
 * relacionadas con el manejo de los antecedentes varios
 * @author Diego Ramírez &lt;dramirez@princetonsa.com&gt;   
 * @version 1.0, Apr 25, 2003
 */
public class AntecedentesVariosAction extends Action {

	/**
	 * Clase encargada de llevar los logs del sistema
	 */
	private Logger logger = Logger.getLogger(AntecedentesVariosAction.class);

		/**
		 * Constructor del Action
		 */
	public AntecedentesVariosAction() {
	}

		/**
		 * Es el médodo encargado de ejecutar la accion de este objeto action.
		 * 
		 * @param mapping Objeto que contiene los mapping de los forwards de la 
		 *     aplicación
		 * @param form El objeto form asociado a este action
		 * @param request El request generado por el cliente
		 * @param response Respuesta que se le va a enviar al cliente.
		 * @exception Exception Si algun error ocurre
		 * @return La pagina a la que se debe ir despues de realizada la acciÃ³n
		 */
	@SuppressWarnings("null")
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
		throws Exception{

		Connection con = null;
		try {
			if (response==null); //Para evitar que salga el warning

			if(form instanceof AntecedentesVariosForm)
			{
				AntecedentesVariosForm antecedenteF = (AntecedentesVariosForm) form;
				AntecedenteVario antecedente = new AntecedenteVario();
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
					
				con = myFactory.getConnection();
				
				int idAntecedente = 0;			
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				String estado = antecedenteF.getEstado();
				logger.warn("[AntecedentesVariosAction] estado->"+estado);
				if(usuario==null){
					request.setAttribute("codigoDescripcionError","errors.usuario.noCargado");					
					antecedenteF.reset();
					
					if( con != null || !con.isClosed() )
	                    UtilidadBD.closeConnection(con);
					
					return mapping.findForward("descripcionError");									
				}
				else
				if( !UtilidadValidacion.esProfesionalSalud(usuario) )
				{
					if( con != null && !con.isClosed() )
	                    UtilidadBD.closeConnection(con);

					antecedenteF.reset();
				
					logger.warn("El usuario no tiene permisos de acceder a esta funcionalidad "+usuario.getLoginUsuario());				
					request.setAttribute("codigoDescripcionError", "errors.usuario.noAutorizado");
					return mapping.findForward("paginaError");								
				}	

				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
																		
				if( paciente == null || paciente.getCodigoTipoIdentificacionPersona().equals("") )
				{
					request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
					
					if( con != null || !con.isClosed() )
	                    UtilidadBD.closeConnection(con);
					
					return mapping.findForward("descripcionError");
				}

				if(estado!=null && estado.equals("resumen"))
				{
					if( con != null || !con.isClosed() )
	                    UtilidadBD.closeConnection(con);

					return mapping.findForward("paginaResumen");                
				}
				if(estado!=null && estado.equals("inicio"))
				{
					if( con != null || !con.isClosed() )
	                    UtilidadBD.closeConnection(con);

					return mapping.findForward("paginaPrincipal");                
				}
				if(estado!=null && estado.equals("registro"))
				{
					if( con != null || !con.isClosed() )
	                    UtilidadBD.closeConnection(con);
					
					return mapping.findForward("paginaResumen");                
				}
				if(estado!=null && estado.equals("voverAntecedentesPrincipal"))
				{
					if( con != null || !con.isClosed() ){
	                    UtilidadBD.closeConnection(con);
					}
					
					return mapping.findForward("voverAntecedentesPrincipal");                
				}
				if(estado!=null && estado.equals("regresarResumenAntecedentes"))
				{
					if( con != null || !con.isClosed() ){
	                    UtilidadBD.closeConnection(con);
					}
					
					return mapping.findForward("regresarResumenAntecedentes");                
				}
				if(estado!=null && estado.equals("enlaceUnicoOtrosAntecedenetes"))
				{
					if( con != null || !con.isClosed() ){
	                    UtilidadBD.closeConnection(con);
					}
					
					return mapping.findForward("enlaceUnicoOtrosAntecedenetes");                
				}

				antecedente.setPaciente(paciente);
				antecedente.setCodigoTipoAntecedente(Integer.parseInt(antecedenteF.getTipoAntecedente()));
				antecedente.setDescripcionAntecedente(UtilidadTexto.agregarTextoAObservacion(null,antecedenteF.getDescripcionAntecedente(), usuario, true));
				antecedente.setUsuario(usuario.getLoginUsuario() );
				idAntecedente = antecedente.insertar(con); 
								
				if(idAntecedente ==0)
				{
					request.setAttribute("descripcionError","Error en la creaci&oacute;n del Antecedente<br>Por favor utilize los menus");
					antecedenteF.reset();
					
					if( con != null || !con.isClosed() )
	                    UtilidadBD.closeConnection(con);
					
					return mapping.findForward("descripcionError");
				}
				
				/**@todo Arreglar esto para que no toque hacerlo por request*/
				/*
					ActionMessages mensaje = new ActionMessages();
					mensaje.add("exito", new ActionMessage("prompt.varios.exito"));
				*/
				request.setAttribute("exito", "true");
				antecedenteF.reset();
				
				if( con != null || !con.isClosed() )
                    UtilidadBD.closeConnection(con);
							
				return mapping.findForward("paginaResumen");
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

}