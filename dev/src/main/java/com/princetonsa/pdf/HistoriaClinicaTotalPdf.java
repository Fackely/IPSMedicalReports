package com.princetonsa.pdf;

import java.sql.Connection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.UtilidadBD;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

public class HistoriaClinicaTotalPdf 
{
	
	public static ActionForward generarReporte(ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente )
	{
		Connection con= UtilidadBD.abrirConexion();
		
		DesignEngineApi comp;        
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"historiaClinica/","historiaClinicaTotal.rptdesign");
		
		generarEstructuraBasica(comp, con, request, usuario, paciente);
		
		
		//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        if(!newPathReport.equals(""))
        {
            request.setAttribute("isOpenReport", "true");
            request.setAttribute("newPathReport", newPathReport);
        }            
        //por ultimo se modifica la conexion a BD
        comp.updateJDBCParameters(newPathReport);
		
		
		return mapping.findForward("resumenIngreso");
	}

	

	private static void generarEstructuraBasica(DesignEngineApi comp, Connection con, HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		InstitucionBasica ins = new InstitucionBasica();
		ins.cargarXConvenio(usuario.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
		
		comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"             "+ins.getNit());  
        if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
            v.add("Actividad Económica: "+ins.getActividadEconomica());
        v.add(ins.getDireccion()+"              "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
   }
	
	
	
}
