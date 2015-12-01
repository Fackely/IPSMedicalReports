package com.princetonsa.action.enfermeria;

import java.sql.Connection;
import java.util.Vector;

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
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.enfermeria.ConsultaProgramacionCuidadosPacienteForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.enfermeria.ConsultaProgramacionCuidadosPaciente;
import com.princetonsa.mundo.enfermeria.ProgramacionCuidadoEnfer;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

public class ConsultaProgramacionCuidadosPacienteAction extends Action 
{
	Logger logger =Logger.getLogger(ConsultaProgramacionCuidadosAreaAction.class);		
		
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		
		Connection con=null;
		try{
		if (form instanceof ConsultaProgramacionCuidadosPacienteForm) 
		{
			ConsultaProgramacionCuidadosPacienteForm forma=(ConsultaProgramacionCuidadosPacienteForm) form;
			
			String estado=new String("");
			estado=forma.getEstado();
			
			logger.info("Estado -->"+estado);
			
			con=UtilidadBD.abrirConexion();
			HttpSession session=request.getSession();
			UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
			PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
			ConsultaProgramacionCuidadosPaciente mundo = new ConsultaProgramacionCuidadosPaciente();	
		   
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de ConsutaProgramacionCuidadosPacienteAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.closeConnection(con);
			 	return mapping.findForward("paginaError");
			 }
			else if(estado.equals("empezarConsulPaciente"))
			{
			    forma.reset();
			    ActionForward forward = new ActionForward();
				forward=accionValidarProgramacionPaciente(con, forma, mundo, paciente, usuario, request, mapping);
				if(forward != null)
					return forward;
				
				forma.setProgramacionCuidados(
				ProgramacionCuidadoEnfer.consultarProgCuidadosEnfer(con, paciente.getCodigoIngreso(),ConstantesBD.codigoNuncaValido,ConstantesBD.codigoNuncaValido,"",false,false,false,false));								
				
				UtilidadBD.closeConnection(con);
			    return mapping.findForward("principal");
			}
			else if(estado.equals("imprimir")){
				
				return this.generarReporte(con, forma, mapping, request, usuario,paciente,mundo);	
			}
			else	
			{
			   forma.reset();
			   logger.warn("Estado no valido dentro del flujo de CONSULTA PROGRAMAICON POR PACIENTE ");
			   request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			   UtilidadBD.cerrarConexion(con);
			   return mapping.findForward("paginaError");
			}
		}
		else
		{
			logger.error("El form no es compatible con el form de ConsultaProgramacionCuidadosPacienteForm");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
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
    * 
    * @param con
    * @param forma
    * @param mundo
    * @param paciente
    * @param usuario
    * @param request
    * @param mapping
    * @return
    */
		private ActionForward accionValidarProgramacionPaciente(Connection con,ConsultaProgramacionCuidadosPacienteForm forma, ConsultaProgramacionCuidadosPaciente mundo, PersonaBasica paciente,UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
		{
			if(paciente==null || paciente.getCodigoPersona()<=0)
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.noCargado", "errors.paciente.noCargado", true);
			}
			else if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.ingresoEstadoDiferente", "errors.ingresoEstadoDiferenteAbierto", true);
			}
			
			return null;
			
		}
		
		/**
		 * 
		 * @param con
		 * @param forma
		 * @param mapping
		 * @param request
		 * @param usuarioActual
		 * @param paciente
		 * @param mundo
		 * @return
		 */
		private ActionForward generarReporte(Connection con, ConsultaProgramacionCuidadosPacienteForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual,PersonaBasica paciente, ConsultaProgramacionCuidadosPaciente mundo) 
		{
			String nombreRptDesign = "ConsultaProgramacionCuidadosEspecialesPaciente.rptdesign";
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
			DesignEngineApi comp;
	        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"enfermeria/",nombreRptDesign);
	        comp.insertImageHeaderOfMasterPage1(0, 1, institucion.getLogoReportes());
	        comp.insertGridHeaderOfMasterPage(0,0,1,5);
	        Vector v=new Vector();
	        v.add(institucion.getRazonSocial());
	        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit()+(institucion.getDigitoVerificacion().equals("")?"":" - "+institucion.getDigitoVerificacion()));
	        v.add(institucion.getDireccion());
	        v.add("Tels. "+institucion.getTelefono());
	        v.add("Centro Anteción: "+usuarioActual.getCentroAtencion());
	        comp.insertLabelInGridOfMasterPage(0,0,v);
	        comp.insertLabelInGridPpalOfHeader(2,0, "PROGRAMACION CUIDADOS ESPECIALES POR PACIENTE");	        
	        comp.insertLabelInGridPpalOfHeader(3,0,"NOMBRE: "+ paciente.getApellidosNombresPersona());
	        comp.insertLabelInGridPpalOfHeader(3,1,"INGRESO: "+paciente.getCodigoIngreso()+"                 FECHA INGRESO: "+UtilidadFecha.conversionFormatoFechaAAp(paciente.getFechaIngreso()));
	        
	        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuarioActual.getLoginUsuario());
	        //Se habre el dataSet y modifica la consulta
	        comp.obtenerComponentesDataSet("cuidadosEspecialesPaciente");	        
	    	
	        String consulta = ProgramacionCuidadoEnfer.getStringSqlConsulProgCuidEnferPaciente(
	        		paciente.getCodigoIngreso(),
	        		ConstantesBD.codigoNuncaValido,
	        		ConstantesBD.codigoNuncaValido,
	        		false,
	        		false,
	        		true).get("sql").toString();
	        
	        
	        comp.modificarQueryDataSet(consulta);
	        
	        logger.info("Query consulta nueva >>>>>>>>> "+consulta);  
	        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
	        comp.updateJDBCParameters(newPathReport);       
	        logger.info("valor del new path report "+newPathReport);
	       
	        
	       if(!newPathReport.equals(""))
	       {
	    	   request.setAttribute("isOpenReport", "true");
	    	   request.setAttribute("newPathReport", newPathReport);
	       }
	        
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}	

	
}
