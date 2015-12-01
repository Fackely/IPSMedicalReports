package com.princetonsa.action.carteraPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.carteraPaciente.PazYSalvoCarteraPacienteForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.carteraPaciente.PazYSalvoCarteraPaciente;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;


public class PazYSalvoCarteraPacienteAction extends Action{
	
	private Logger logger = Logger.getLogger(PazYSalvoCarteraPacienteAction.class);
	
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{	
		Connection con = null;
		try{
			if(form instanceof PazYSalvoCarteraPacienteForm)
			{


				//se obtiene el usuario cargado en sesion.
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				//se obtiene el paciente cargado en sesion.
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				//se obtiene la institucion
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				//se instancia la forma
				PazYSalvoCarteraPacienteForm forma = (PazYSalvoCarteraPacienteForm)form;		

				//se instancia el mundo
				PazYSalvoCarteraPaciente mundo = new PazYSalvoCarteraPaciente();

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				//se instancia la variable para manejar los errores.
				ActionErrors errores=new ActionErrors();

				forma.setMensaje(new ResultadoBoolean(false));

				//se instancia la variable para manejar los errores.


				logger.info("\n\n***************************************************************************");
				logger.info(" 	  EL ESTADO DE PAZ Y SALVO CARTERA PACIENTE ES ====>> "+estado);
				logger.info("\n***************************************************************************");

				// ESTADO --> NULL
				if(estado == null)
				{
					forma.reset(usuario.getCodigoInstitucionInt());
					request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");

					return mapping.findForward("paginaError");
				}			
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(forma, mundo, mapping, usuario,request);					   			
				}
				else if(estado.equals("buscarDocXDeudor"))
				{
					return this.accionBuscarDocXDeudor(forma, mundo, mapping, usuario,request);	
				}
				else if(estado.equals("realizarBusquedaDoc"))
				{
					return this.accionRealizarBusquedaDoc(forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("generarPazYSalvo"))
				{
					return this.accionGenerarPazYSalvo(forma, mundo, mapping, usuario,request);
				}
				else if(estado.equals("imprimirPyS"))
				{
					return this.accionImprimirPyS(forma, mundo, mapping, usuario,request);
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
     * Genera el paz y salvo correspondiente al documento seleccionado
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionImprimirPyS(PazYSalvoCarteraPacienteForm forma, PazYSalvoCarteraPaciente mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{	    	
		int i= forma.getIndice();
			
		Connection con;
		
		con= UtilidadBD.abrirConexion();
		
		String nombreRptDesign = "PazYSalvo.rptdesign";
		String nombreCsv = "PazYSalvo";
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				
		Vector v;
		
		//***************** INFORMACIï¿½N DEL CABEZOTE
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"carteraPaciente/",nombreRptDesign);
        
        // Logo
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        
        // Nombre Instituciï¿½n, titulo y rango de fechasLeft To Right
        comp.insertGridHeaderOfMasterPageWithName(0,1,1,2, "titulo");
        v=new Vector();
        v.add(ins.getRazonSocial());
        v.add("NIT: "+ins.getNit()+"\n"+ins.getDireccion()+"\n"+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
              
        // Fecha hora de proceso y usuario
        comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
        comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
        
       comp.obtenerComponentesDataSet("dataSet");
       String newquery = ConsultasBirt.consultarPazYSalvo(forma.getListaDocXDeudor().get(i).getCodigoPk());
              
       comp.modificarQueryDataSet(newquery);
       //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
       comp.lowerAliasDataSet();
       String newPathReport = comp.saveReport1(false);
       comp.updateJDBCParameters(newPathReport);
       
       if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
            
       UtilidadBD.closeConnection(con);
        
       return mapping.findForward("principal");
	}
	
	/**
     * Genera el paz y salvo correspondiente al documento seleccionado
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionGenerarPazYSalvo(PazYSalvoCarteraPacienteForm forma, PazYSalvoCarteraPaciente mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{	
		int secuencia=0;
		
		int i= forma.getIndice();
		Connection con=UtilidadBD.abrirConexion();
		// FIXME Cambiar este método a transaccional
		String consecutivo= UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoPazYSalvo, usuario.getCodigoInstitucionInt());
		String anioConsecutivo=UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoPazYSalvo,usuario.getCodigoInstitucionInt(),consecutivo);
		forma.getListaDocXDeudor().get(i).setConsecutivo(consecutivo);
		if(anioConsecutivo.equals(""))
			anioConsecutivo=" ";
		forma.getListaDocXDeudor().get(i).setAnioConsecutivo(anioConsecutivo);
						
		secuencia= mundo.generarPazYSalvo(forma.getListaDocXDeudor().get(i), usuario.getLoginUsuario());
				
		if(secuencia > 0)
			forma.setGuardo(true);
			 
		if(forma.isGuardo())
		{
			forma.getListaDocXDeudor().get(i).setCodigoPk(secuencia+"");
			forma.getListaDocXDeudor().set(i, mundo.consultarPazYSalvo(Utilidades.convertirAEntero(forma.getListaDocXDeudor().get(i).getCodigoPk())));
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(
					con,
					ConstantesBD.nombreConsecutivoPazYSalvo,
					usuario.getCodigoInstitucionInt(),
					consecutivo,
					ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			forma.setMensaje(new ResultadoBoolean(true,"El paz y Salvo se Genero Satisfactoriamente"));
		}
		else
		{
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(
					con,
					ConstantesBD.nombreConsecutivoPazYSalvo,
					usuario.getCodigoInstitucionInt(),
					consecutivo,
					ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
			forma.setMensaje(new ResultadoBoolean(true,"Las Operaciones NO finalizaron satisfactoriamente."));
		}
		UtilidadBD.closeConnection(con);
				
		return mapping.findForward("principal");
	}
	
	/**
     * Realiza la busqueda de documentos de garantia por deudor
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionRealizarBusquedaDoc(PazYSalvoCarteraPacienteForm forma, PazYSalvoCarteraPaciente mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{	
		forma.setListaDocXDeudor(mundo.consultarDocPorDeudor(forma.getTipoIdDeudor()+"",forma.getNumeroIdDeudor()+"",forma.getNombreDeudor()+"",forma.getApellidoDeudor()+"",
				forma.getTipoIdPaciente()+"",forma.getNumeroIdPaciente()+"",forma.getNombrePaciente()+"",forma.getApellidoPaciente()+"",
				forma.getCodGarantia()+"",forma.getEstadoGarantia()+"",forma.getNumFactura()+""));
		
		return mapping.findForward("principal");
	}
	
	/**
     * Prepara vista para la busqueda
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionBuscarDocXDeudor(PazYSalvoCarteraPacienteForm forma, PazYSalvoCarteraPaciente mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{
		forma.resetBusqueda(usuario.getCodigoInstitucionInt());
		forma.setTiposId(mundo.consultarTiposId());
		logger.info("\n\narray tipos id::: "+forma.getTiposId().toString());
				
		return mapping.findForward("principal");
	}
	
	/**
     * Inicia en el forward de Conceptos Retencion
     * @param forma
     * @param con
     * @param mapping
     * @return
	 * @throws SQLException 
     */
	private ActionForward accionEmpezar(PazYSalvoCarteraPacienteForm forma, PazYSalvoCarteraPaciente mundo, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request)
	{		
		forma.reset(usuario.getCodigoInstitucionInt());
			
		return mapping.findForward("principal");
	}
}