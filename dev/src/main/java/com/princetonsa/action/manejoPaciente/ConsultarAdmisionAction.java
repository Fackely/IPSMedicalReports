package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ResultadoBoolean;
import util.UtilidadBD;
import util.Utilidades;
import util.reportes.ConsultasBirt;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.ConsultarAdmisionForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.ConsultarAdmision;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class ConsultarAdmisionAction extends Action {
	
	/**
	 * Manejo de los logs
	 */
	private Logger logger=Logger.getLogger(ConsultarAdmisionAction.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{
		Connection con=null;
		try{
		if(form instanceof ConsultarAdmisionForm)
		{
			UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
			PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			
			ConsultarAdmisionForm forma=(ConsultarAdmisionForm) form;
			ConsultarAdmision mundo=new ConsultarAdmision();
			
			con=UtilidadBD.abrirConexion();
			
			String estado=forma.getEstado();
			
			logger.warn("Estado de ConsultarAdmisionAction--->> "+estado);
			
//			Validar que el paciente este cargado en sesion
			if(paciente.getCodigoPersona()<1)
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no Cargado", "errors.paciente.noCargado", true);
			}
			forma.setMensaje(new ResultadoBoolean(false));
			
			if(estado.equals("empezar"))
			{
				forma.reset();
				forma.setCuentaPacienteMap(mundo.consultarIngresoCuentaPaciente(con, paciente.getCodigoPersona()));
				
				if(Utilidades.convertirAEntero(forma.getCuentaPacienteMap().get("numRegistros")+"",false)>0)
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoAdmisiones");
				}
				else
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente cargado no tiene admisiones", "El paciente cargado no tiene admisiones registradas", false);
				}
			}
			else if(estado.equals("cargarAdmision"))
			{
				this.accionCargarAdmisiones(con,forma,mundo,paciente);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("detalleAdmision");
			}
			else if(estado.equals("generarReporte"))
			{
				this.generarReporte(con,forma,mapping,request,usuario,paciente,Integer.parseInt(forma.getEntidadResponsbaleMap().get("numsubcuenta_0").toString()));
				UtilidadBD.closeConnection(con);
				return mapping.findForward("detalleAdmision");
			}
			else
			{
				forma.reset();
				logger.warn("Estado no valido dentro del flujo de Devolucion de Inventarios Paciente ");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
		}
		else
		{
			logger.error("El form no es compatible con el form de DevolucionInventariosPacienteForm");
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
	
	public void accionCargarAdmisiones(Connection con,ConsultarAdmisionForm forma,ConsultarAdmision mundo,PersonaBasica paciente)
	{
		forma.setPacienteMap(mundo.consultarPaciente(con, paciente.getCodigoPersona()));
		forma.setEntidadResponsbaleMap(mundo.consultarEntidadResponsable(con, Integer.parseInt(forma.getCuentaPacienteMap().get("idingreso_"+forma.getIndiceIngresoSeleccionado())+""),forma.getCuenta()));
		forma.setResponsablePacienteMap(mundo.consultarResponsablePaciente(con, forma.getCuenta()));
	}
	
	private ActionForward generarReporte(Connection con, ConsultarAdmisionForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual, PersonaBasica paciente, int codSubCuenta) 
	{
		String nombreRptDesign = "ConsultarAdmision.rptdesign";
		
		InstitucionBasica ins= new InstitucionBasica();
		ins.cargarXConvenio(usuarioActual.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfHeader(2,1, "HOJA DE INGRESO.");
        //comp.insertLabelInGridPpalOfHeader(2,0, "Fecha Admisión: "+forma.getCuentaPacienteMap().get("fechaadmision_"+forma.getIndiceIngresoSeleccionado())+"   Hora de Admisión: "+forma.getCuentaPacienteMap().get("horaadmision_"+forma.getIndiceIngresoSeleccionado()));
        comp.obtenerComponentesDataSet("numeroAutorizacion");
        logger.info("\n NUMERO CUENTA >> "+forma.getCuenta() +"  NUMERO SUB_CUENTA >> "+codSubCuenta);
        comp.modificarQueryDataSet(ConsultasBirt.consultarNumeroAutorizacion(forma.getCuenta(),codSubCuenta));
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        
        // se mandan los parámetros al reporte
        newPathReport += "&codigoPaciente="+paciente.getCodigoPersona()+"&codigoPaciente2="+paciente.getCodigoPersona()+
        					"&codigoPaciente3="+paciente.getCodigoPersona()+"&cuenta="+forma.getCuenta();
        	
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleAdmision");
	}

}
