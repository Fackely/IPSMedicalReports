package com.princetonsa.action.consultaExterna;


import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.Utilidades;

import com.princetonsa.actionform.consultaExterna.MotivosAnulacionCondonacionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.consultaExterna.MotivosAnulacionCondonacion;

public class MotivosAnulacionCondonacionAction extends Action {

	Logger logger = Logger.getLogger(MotivosAnulacionCondonacionAction.class);
	String estado="";
	String bandera="";
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		UsuarioBasico usuario;
		Connection con = null;
		MotivosAnulacionCondonacionForm motivosAnulacionCondonacionForm = null;

		try {

			// se obtiene el usuario cargado en sesion.
			usuario = (UsuarioBasico) request.getSession().getAttribute(
			"usuarioBasico");

			if (form instanceof MotivosAnulacionCondonacionForm) {
				motivosAnulacionCondonacionForm = (MotivosAnulacionCondonacionForm) form;

				estado = motivosAnulacionCondonacionForm.getEstado();
				logger.warn("Estado Motivos Anulacion Condonacion Form [" + estado + "]");
			}
			try {
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			} catch (SQLException e) {
				logger.warn("No se pudo abrir la conexión" + e.toString());
				motivosAnulacionCondonacionForm.clean();

				logger.warn("Problemas con la base de datos " + e);
				request
				.setAttribute("codigoDescripcionError",
				"errors.problemasBd");
				return mapping.findForward("paginaError");
			}

			// se obtiene el paciente cargado en sesion.
			PersonaBasica paciente = (PersonaBasica) request.getSession()
			.getAttribute("pacienteActivo");

			// se obtiene la institucion
			InstitucionBasica institucion = (InstitucionBasica) request
			.getSession().getAttribute("institucionBasica");

			// se instancia la forma
			MotivosAnulacionCondonacionForm forma = (MotivosAnulacionCondonacionForm) form;

			// se instancia el mundo
			MotivosAnulacionCondonacion mundo = new MotivosAnulacionCondonacion();

			// se instancia la variable para manejar los errores.
			ActionErrors errores = new ActionErrors();

			/*
			 * cuando necesites mostrar mensajes de error forma.setMensaje(new
			 * ResultadoBoolean(false));
			 */

			logger.info("\n\n\n\n estado -----------> "+estado);

			if(estado == null)
			{
				forma.clean();
				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);

				return mapping.findForward("paginaError");
			}
			else if (estado.equals("empezar")) {
				return this.accionEmpezar(forma, mundo, mapping,con, usuario, request);
			} else if (estado.equals("ingresar")){
				return this.accionIngresar(forma,mundo, mapping, con, usuario, request);
			} else if (estado.equals("eliminar")){
				return this.accionEliminar(forma,mundo, mapping, con, usuario, request);
			}else if(estado.equals("modificar")){
				return this.accionModificar(forma,mundo, mapping, con, usuario, request);
			} else if (estado.equals("guardar")){
				return this.accionGuardar(forma, mundo, mapping, con, usuario, request);
			}
			else if (estado.equals("ordenar"))
			{
				return accionOrdenar(forma, mundo, mapping, con, usuario, request);
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

	private ActionForward accionOrdenar(MotivosAnulacionCondonacionForm forma,
			MotivosAnulacionCondonacion mundo, ActionMapping mapping,
			Connection con, UsuarioBasico usuario, HttpServletRequest request) {
		
		
		String[] indices = mundo.indicesListado;
		
		int num = forma.getMotivosAnulacionCondonacion1();
	
		
		
		
		forma.setMotivosAnulacionCondonacion(Listado.ordenarMapa
				(indices,
				forma.getIndice(),
				forma.getUltimoIndice(),
				forma.getMotivosAnulacionCondonacion(),
				num));
		
				
	
		
		forma.setMotivosAnulacionCondonacion("numRegistros",num+"");
		
		forma.setUltimoIndice(forma.getIndice());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
		
	}

	private ActionForward accionGuardar(MotivosAnulacionCondonacionForm forma,
			MotivosAnulacionCondonacion mundo, ActionMapping mapping,
			Connection con, UsuarioBasico usuario, HttpServletRequest request) {
		
	
		forma.setTemporal1(UtilidadCadena.tieneCaracteresEspecialesGeneral(forma.getCodigo()));
		
			
		if(bandera.equals("ingresar") ){
			
				if(mundo.InsertarMotivosAnulacionCondonacionMulta(forma.getCodigo(),forma.getDescripcion(),forma.isCheck(),usuario.getCodigoInstitucion()))
						forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
					else
					 forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
				
				forma.clean();
		}
		else if (bandera.equals("modificar") ){
			
			  int pos =forma.getPosicion(); 
			  			  
			  forma.setConsecutivo(Utilidades.convertirAEntero(forma.getMotivosAnulacionCondonacion("consecutivo_"+pos)+""));
			  
			  forma.setCodigo(forma.getCodigo());
			  		 	
			  forma.setDescripcion(forma.getDescripcion()); 
			   
			  forma.setCheck(forma.isCheck());
			  
			 if(mundo.ModificarMotivosAnulacionCondonacionMulta(forma.getConsecutivo(),forma.getCodigo(),forma.getDescripcion(),forma.isCheck(),usuario.getCodigoInstitucion()))
					forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
				else
				 forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
				
		}
		
		bandera="empezar";
		
		forma.setMotivosAnulacionCondonacion(mundo.consultarMotivosAnulacionCondonacionMultas());
		
		return mapping.findForward("principal");
		
	}



	private ActionForward accionModificar(
			MotivosAnulacionCondonacionForm forma,
			MotivosAnulacionCondonacion mundo, ActionMapping mapping,
			Connection con, UsuarioBasico usuario, HttpServletRequest request) {
	
		forma.setMensaje(new ResultadoBoolean(false));	
		bandera="modificar";
	
	
		   int pos =forma.getPosicion(); 
		   
		   forma.setConsecutivo(Integer.parseInt(forma.getMotivosAnulacionCondonacion("consecutivo_"+pos).toString()));
		   
		   forma.setCodigo(forma.getMotivosAnulacionCondonacion("codigo_"+pos).toString());
		   		   
		   forma.setDescripcion(forma.getMotivosAnulacionCondonacion("descripcion_"+pos).toString());
		   
		   forma.setActivo(forma.getMotivosAnulacionCondonacion("activo_"+pos).toString());
		   
		  return mapping.findForward("principal");
	}


	private ActionForward accionEliminar(MotivosAnulacionCondonacionForm forma,
			MotivosAnulacionCondonacion mundo, ActionMapping mapping,
			Connection con, UsuarioBasico usuario, HttpServletRequest request) {
		
		forma.setMensaje(new ResultadoBoolean(false));
	
		forma.setTemporal(mundo.consultarEliminacion(forma.getConsecutivo()));
		
		if(forma.isTemporal())
			return mapping.findForward("principal");
		else
		{
			if(mundo.eliminarMotivosAnulacionCondonacionMultas(forma.getConsecutivo()))
				forma.setMensaje(new ResultadoBoolean(true,"Operaciones Realizadas con Exito"));
			else
				forma.setMensaje(new ResultadoBoolean(true,"No se puede eliminar el motivo por que ya esta siendo referenciado en el sistema"));
			
			forma.setMotivosAnulacionCondonacion(mundo.consultarMotivosAnulacionCondonacionMultas());
		
			return mapping.findForward("principal");		
		}    
	}


	private ActionForward accionIngresar(MotivosAnulacionCondonacionForm forma,
			MotivosAnulacionCondonacion mundo, ActionMapping mapping,
			Connection con, UsuarioBasico usuario, HttpServletRequest request) {
		
		forma.setMensaje(new ResultadoBoolean(false));
			bandera="ingresar";
			
			forma.clean();
			
			forma.setConsecutivo(ConstantesBD.codigoNuncaValido);
			
			forma.setMotivosAnulacionCondonacion(mundo.consultarMotivosAnulacionCondonacionMultas());
			
			return mapping.findForward("principal");
	        	
	}


	private ActionForward accionEmpezar(MotivosAnulacionCondonacionForm forma,MotivosAnulacionCondonacion mundo,ActionMapping mapping, Connection con, UsuarioBasico usuario,HttpServletRequest request) throws SQLException {
	
		forma.setMensaje(new ResultadoBoolean(false));
		
		forma.clean();
		
		forma.setMotivosAnulacionCondonacion(mundo.consultarMotivosAnulacionCondonacionMultas());

		return mapping.findForward("principal");
	}
	
	

}
