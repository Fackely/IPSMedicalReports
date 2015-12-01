package com.princetonsa.action.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

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
import util.UtilidadBD;
import util.odontologia.UtilidadOdontologia;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.historiaClinica.VerResumenOdontologicoForm;
import com.princetonsa.dto.odontologia.DtoAntecendenteOdontologico;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.historiaClinica.VerResumenOdontologico;

/**
 * VerResumenOdontologicoAction
 */
@SuppressWarnings("rawtypes")
public class VerResumenOdontologicoAction extends Action  {

	private static Logger logger=Logger.getLogger(VerResumenOdontologicoAction.class);
	VerResumenOdontologico mundo;
	
	@SuppressWarnings("unused")
	public ActionForward execute(ActionMapping mapping,
			 ActionForm form, 
			 HttpServletRequest request, 
			 HttpServletResponse response) throws Exception
			 {

		Connection con = null;

		try {
			if(response == null);

			if (form instanceof VerResumenOdontologicoForm) 
			{			 
				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//Usuario cargado en session
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				//paciente cargado en sesion 
				PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				//Institucion
				InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");  

				mundo = new VerResumenOdontologico();
				ActionErrors errores = new ActionErrors();
				VerResumenOdontologicoForm forma = (VerResumenOdontologicoForm)form;		
				String estado = forma.getEstado();		 

				logger.info("-------------------------------------");
				logger.info("Valor del Estado    >> "+forma.getEstado());			 
				logger.info("-------------------------------------");
				logger.info("-------------------------------------");

				if(estado.equals("empezar"))
				{ 
					forma.reset(request);
					ActionForward forward = new ActionForward();
					forward=accionValidarPaciente(con, paciente, usuario, request, mapping);
					if(forward != null)
						return forward;	
					return accionEmpezar(con, forma, paciente,mapping, usuario,request);			  

				}else if (estado.equals("consultar"))
				{

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if (estado.equals("mostrarDetalleAntPrev"))
				{
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("detAntecVersionAnt");	
				}
				else if (estado.equals("mostrarDetalleAntecAct"))
				{				
					return accionCargarDetalleAntecedetneActual(con, forma,mapping, request);				
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
	 * Metodo utilizado para cargar el Detalle del Antecedente de la Version Actual de Axioma
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionCargarDetalleAntecedetneActual(Connection con,VerResumenOdontologicoForm forma, ActionMapping mapping,HttpServletRequest request) {
		
		String datosGeneralesDetalleAnt="";
		HashMap parametros= new HashMap();
		UsuarioBasico profesional= new UsuarioBasico();
		forma.resetDetalleAntecedente();
		forma.resetComponenteDetalle();
		
		forma.setDetalleAntecedente(forma.getArrayAntecedentesActuales().get(forma.getPosAntecedente()));
		try{
			   profesional.cargarUsuarioBasico(con, forma.getUltimoAntecedente().getCodigoMedico());
			   }catch(SQLException e){}
			   
			   datosGeneralesDetalleAnt=forma.getDetalleAntecedente().getFechaModifica()+"  " +
			  		""+forma.getDetalleAntecedente().getHoraModifica()+ "  "+profesional.getNombreyRMPersonalSalud()+" " +profesional.getEspecialidadesMedico(); 
		  
			  forma.setDatosGeneralesDetalleAnt(datosGeneralesDetalleAnt);
			  
			  parametros.put("codPaciente", forma.getDetalleAntecedente().getCodigoPaciente());
			  parametros.put("codValoracionOdo", forma.getDetalleAntecedente().getValoracion());
			  parametros.put("codEvolucionOdo", forma.getDetalleAntecedente().getEvolucion());
			  
			  
			  forma.setComponenteDetalle(Plantillas.cargarComponenteAntecedentesOdonto(parametros));
			  logger.info("Codigo Componente Detalle>>"+forma.getComponenteDetalle().getCodigo()); 
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleAntecActual");
	}



	/**
	 * Metodo para cargar el Form y todos los valores iniciales de la funcionalidad
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	private ActionForward accionEmpezar(Connection con,	VerResumenOdontologicoForm forma, PersonaBasica paciente,ActionMapping mapping, UsuarioBasico usuario,HttpServletRequest request) {
		
		ActionErrors errores = new ActionErrors();
		HashMap parametros= new HashMap();
		logger.info("entro a la accion empezar CodPaciente >> "+paciente.getCodigoPersona());
		String datosGeneralesUltAnt="";
		UsuarioBasico profesional= new UsuarioBasico();
		forma.setAntecedentesOdontoPrev(mundo.cagarAntecedentesOdontoPrevios(paciente.getCodigoPersona()));
		forma.setArrayAntecedentesActuales(UtilidadOdontologia.obtenerAntecedentesOdontologicos(paciente.getCodigoPersona(), ConstantesBD.codigoNuncaValido, "", ""));
		 
		if(forma.getArrayAntecedentesActuales().size() > 0)
		  {			  
			  forma.setUltimoAntecedente((DtoAntecendenteOdontologico)forma.getArrayAntecedentesActuales().get(0));
			 // logger.info("Nombre Servicio "+forma.getUltimoAntecedente().getTratamientosInternos().get(0).getNombreServicio());
			 // logger.info("Nombre Programa "+forma.getUltimoAntecedente().getTratamientosInternos().get(0).getNombrePrograma());
			  try{
			   profesional.cargarUsuarioBasico(con, forma.getUltimoAntecedente().getCodigoMedico());
			   }catch(SQLException e){}
			   
			  datosGeneralesUltAnt=forma.getUltimoAntecedente().getFechaModifica()+"  " +
			  		""+forma.getUltimoAntecedente().getHoraModifica()+ "  "+profesional.getNombreyRMPersonalSalud()+" " +profesional.getEspecialidadesMedico(); 
		  
			  forma.setDatosGeneralesUltimoAnt(datosGeneralesUltAnt);
			  forma.setFechaHora(forma.getUltimoAntecedente().getFechaModifica() + "  /  " + forma.getUltimoAntecedente().getHoraModifica());
			  forma.setNombreMedico(forma.getUltimoAntecedente().getNombresMedico());
			  parametros.put("codPaciente", paciente.getCodigoPersona());
			  parametros.put("codValoracionOdo", forma.getUltimoAntecedente().getValoracion());
			  parametros.put("codEvolucionOdo", forma.getUltimoAntecedente().getEvolucion());
			  
			  forma.setComponentePpal(Plantillas.cargarComponenteAntecedentesOdonto(parametros));
			  logger.info("Codigo Componente >>"+forma.getComponentePpal().getCodigo());
		  }
		 
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}


	/**
	 * Metodo para validar si paciente esta cargado en sesion
	 * @param con
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward accionValidarPaciente(Connection con, PersonaBasica paciente,UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
		if(paciente==null || paciente.getCodigoPersona()<=0)
		{
			UtilidadBD.closeConnection(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.noCargado", "errors.paciente.noCargado", true);
		}	
		UtilidadBD.closeConnection(con);
		return null;
		
	}
	
}
