package com.princetonsa.action.enfermeria;

import java.sql.Connection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.enfermeria.ConsultaProgramacionCuidadosAreaForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.enfermeria.ConsultaProgramacionCuidadosArea;
import com.princetonsa.mundo.enfermeria.ProgramacionCuidadoEnfer;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

public class ConsultaProgramacionCuidadosAreaAction extends Action {

Logger logger=Logger.getLogger(ConsultaProgramacionCuidadosAreaAction.class);		
	
	

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
	
		
		Connection con=null;
	 try{
	if (form instanceof ConsultaProgramacionCuidadosAreaForm) 
	{
		ConsultaProgramacionCuidadosAreaForm forma=(ConsultaProgramacionCuidadosAreaForm) form;		
		String estado=forma.getEstado();
		
		logger.info("Estado -->"+estado);
		
		con=UtilidadBD.abrirConexion();
		HttpSession session=request.getSession();
		UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());		
		PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
		ConsultaProgramacionCuidadosArea mundo = new ConsultaProgramacionCuidadosArea();
		
		if(estado == null)
		{
			logger.warn("Estado no valido dentro del flujo de ProgramacionCuidadosEnfermeriaAction (null) ");
			request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaError");
		}
		//Comienza a realizar la consulta y parametriza la busqueda
		else if(estado.equals("empezarConsulArea"))
		{
			forma.reset();
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}
		// Muestra la consulta realizada segun los parametros establecidos
		else if(estado.equals("hacerConsulta")){
			ActionErrors errores = new ActionErrors();
		    errores = mundo.validacionBusquedaporRango(forma.getFechaProg(),forma.getHoraProg(),forma.getAreaFiltro(),forma.getPisoFiltro(),forma.getHabitacionFiltro());
		    if(errores.isEmpty())
		    {
		  	   forma.setMapaListaPacientes(mundo.consultarListadoPacientes(con, forma.getAreaFiltro(), forma.getPisoFiltro(), forma.getHabitacionFiltro(), forma.getFechaProg(),forma.getHoraProg()));
			   UtilidadBD.closeConnection(con);
			  return mapping.findForward("listadoPacientes");
		    }else
		    {
		    	saveErrors(request, errores);	
				return mapping.findForward("principal");
		    }
		}
		
        else if(estado.equals("listadoCuidadosEnfermeria"))
        {        	
        	paciente.setCodigoPersona(Utilidades.convertirAEntero(forma.getMapaListaPacientes().get("codigopaciente_"+forma.getIndicePaciente()).toString()));
        	UtilidadesManejoPaciente.cargarPaciente(con,usuario,paciente,request);
        	
        	logger.info("VALOR DEL PACIENTE >> "+paciente.getCodigoPersona());        	
        	
        	forma.setProgramacionCuidados(ProgramacionCuidadoEnfer.consultarProgCuidadosEnfer(
        				con, 
        				paciente.getCodigoIngreso(),
        				ConstantesBD.codigoNuncaValido,
        				ConstantesBD.codigoNuncaValido,
        				UtilidadFecha.conversionFormatoFechaABD(forma.getFechaProg())+" "+forma.getHoraProg(),
        				false,
        				false,
        				true,
        				false));
        				
			UtilidadBD.closeConnection(con);
			return mapping.findForward("listadoCuidados");
		}
        else if(estado.equals("imprimir")){
			
        	paciente.setCodigoPersona(Utilidades.convertirAEntero(forma.getMapaListaPacientes().get("codigopaciente_"+forma.getIndicePaciente()).toString()));
          	UtilidadesManejoPaciente.cargarPaciente(con,usuario,paciente,request);
          	
        	return this.generarReporte(con, forma, mapping, request, usuario,paciente,mundo);			
		}
		else  
		{
			forma.reset();
			logger.warn("Estado no valido dentro del flujo de CONSULTA FACTURAS VARIAS ");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("paginaError");
		}
	}
	else
	{
		logger.error("El form no es compatible con el form de ConsultaFacturasVariasForm");
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
	 * @param mapping
	 * @param request
	 * @param usuarioActual
	 * @param paciente
	 * @param mundo
	 * @return
	 */
	private ActionForward generarReporte(Connection con, ConsultaProgramacionCuidadosAreaForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual,PersonaBasica paciente, ConsultaProgramacionCuidadosArea mundo) 
	{
		String nombreRptDesign = "ConsultaProgramacionCuidadosEspecialesArea.rptdesign";
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
        comp.insertLabelInGridPpalOfHeader(2,0, "PROGRAMACION CUIDADOS ESPECIALES POR AREA");
        comp.insertLabelInGridPpalOfHeader(3,0,"Parametros de Busqueda. Fecha: "+(forma.getFechaProg().equals("")?"":forma.getFechaProg())+(forma.getHoraProg().equals("")?"":", Hora: "+forma.getHoraProg()) +(forma.getDescripcionArea().equals("")?"":", Area: "+forma.getDescripcionArea())+(forma.getDescripcionPiso().equals("")?"":", Piso: "+forma.getDescripcionPiso())+ (forma.getDescripcionHabitacion().equals("")?"":", Habitación: "+forma.getDescripcionHabitacion()) );
        comp.insertLabelInGridPpalOfHeader(4,0,"NOMBRE: "+ forma.getMapaListaPacientes().get("paciente_"+forma.getIndicePaciente()));
        comp.insertLabelInGridPpalOfHeader(4,1,"INGRESO: "+forma.getMapaListaPacientes().get("codigoingreso_"+forma.getIndicePaciente()) +"              FECHA INGRESO: "+UtilidadFecha.conversionFormatoFechaAAp(forma.getMapaListaPacientes().get("fechaingreso_"+forma.getIndicePaciente()).toString()));
        
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuarioActual.getLoginUsuario());
        //Se habre el dataSet y modifica la consulta
        comp.obtenerComponentesDataSet("cuidadosEspecialesArea");     
    	
        String consulta = ProgramacionCuidadoEnfer.getStringSqlConsulProgCuidEnfer(
        		paciente.getCodigoIngreso(),
        		ConstantesBD.codigoNuncaValido,
        		ConstantesBD.codigoNuncaValido,
        		UtilidadFecha.conversionFormatoFechaABD(forma.getFechaProg())+" "+forma.getHoraProg(),
        		false,
        		false,
        		true,
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
		return mapping.findForward("listadoCuidados");
	}	
	
	
}