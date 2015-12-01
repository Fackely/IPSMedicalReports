/*
 * Creado el 28/12/2005
 * Jorge Armando Osorio Velasquez
 */
package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.facturacion.ConsultaHonorariosMedicosForm;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoHonorariosMedico;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.manejoPaciente.DtoRegionesCobertura;
import com.princetonsa.dto.odontologia.DtoDetalleProgramas;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ConsultaHonorariosMedicos;
import com.princetonsa.mundo.manejoPaciente.RegionesCobertura;
import com.princetonsa.mundo.odontologia.ProgramasOdontologicos;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ExtractCSV;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.generadorReporte.facturacion.valorHonorariosMedico.GeneradorReporteValorHonorariosMedico;

/**
 * 
 * @author Jorge Armando Osorio Velasquez
 * 
 * CopyRight Princeton S.A.
 * 28/12/2005
 */
public class ConsultaHonorariosMedicosAction extends Action
{
	
    /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */ 
	private Logger logger = Logger.getLogger(ConsultaHonorariosMedicosAction.class);
	
	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
							        ActionForm form, 
							        HttpServletRequest request, 
							        HttpServletResponse response) throws Exception
							        {
		Connection con = null;
		try{
			if(form instanceof ConsultaHonorariosMedicosForm)
			{


				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				ConsultaHonorariosMedicosForm forma=(ConsultaHonorariosMedicosForm)form;
				ConsultaHonorariosMedicos mundo=new ConsultaHonorariosMedicos();

				String estado = forma.getEstado();
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptoTesoreriaAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					return accionEmpezar(mapping, request, con, usuario,institucion, forma);
				}
				else if(estado.equals("buscar"))
				{
					this.accionBuscarProfesionalesSalud(con,forma,mundo);
					/*if(Integer.parseInt(forma.getMapaProfesionales("numRegistros")+"")==1)
				{
					forma.setIndex(0);
					this.accionDetalleHonorariosProfesional(con,forma,mundo);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleHonorarios");
				}*/
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("detalle"))
				{
					this.accionDetalleHonorariosProfesional(con,forma,mundo);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleHonorarios");
				}
				else if(estado.equals("ordenarProfesionales"))
				{
					this.accionOrdenar(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("ordenarDetalle"))
				{
					this.accionOrdenarDetalle(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleHonorarios");
				}
				else if(estado.equals("volverPrincipal"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("generarReporte"))
				{
					accionGenerarReporte(con,forma,request,usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("cambiarProfesional"))
				{
					forma.setPooles(Utilidades.obtenerPoolesMedicoArrayMap(con, UtilidadFecha.getFechaActual(), forma.getProfesional()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("cargarCiudades"))
				{
					forma.setCiudades(Utilidades.obtenerCiudadesXPais(con, forma.getPais()+""));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("cargarCentrosAtencion"))
				{
					cargarCentrosAtencion(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("cargarDetallePrograma"))
				{	
					accionCargaDetallePrograma(usuario, forma);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("cargarProfesionalesxEspecialidad"))
				{	
					accionCargarProfesionalesXEspecilidad(con, usuario, forma);
					return mapping.findForward("paginaPrincipal");
				}


			}
			else
			{
				logger.error("El form no es compatible con el form de CierreInventariosForm");
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
	 * Accion cargar profesionales por Especialidad
	 * @author Edgar Carvajal Ruiz
	 * @param con
	 * @param usuario
	 * @param forma
	 */
	private void accionCargarProfesionalesXEspecilidad(Connection con,
			UsuarioBasico usuario, ConsultaHonorariosMedicosForm forma) {
		
		String codigoEspecialidad=forma.getEspecialidad()+"";
		forma.setProfesionales(UtilidadesManejoPaciente.obtenerProfesionales(con, usuario.getCodigoInstitucionInt(), true, false, "", "", codigoEspecialidad));
	}

	
	
	
	
	/**
	 * Metodo que Carga el detalle de un programa
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param usuario
	 * @param forma
	 */
	private void accionCargaDetallePrograma(UsuarioBasico usuario,
			ConsultaHonorariosMedicosForm forma) {
		if(!UtilidadTexto.isEmpty(forma.getCodigoPrograma()) )
		{	
			ArrayList<DtoDetalleProgramas> listaTmp= ProgramasOdontologicos.cargarDetallePrograma( Utilidades.convertirADouble(forma.getCodigoPrograma()), ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt()) );
			forma.setListaDetallesPrograma(listaTmp);
		}
		else
		{
			forma.setListaDetallesPrograma(new ArrayList<DtoDetalleProgramas>());
		}
	}

	/**
	 * @param mapping
	 * @param request
	 * @param con
	 * @param usuario
	 * @param institucion
	 * @param forma
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, 
										HttpServletRequest request, 
										Connection con, 
										UsuarioBasico usuario,
										InstitucionBasica institucion, 
										ConsultaHonorariosMedicosForm forma) 
	{
		
		
		forma.reset(usuario.getCodigoInstitucionInt());
		ValoresPorDefecto.cargarValoresIniciales(con);
		
		forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(Utilidades.getUsuarioBasicoSesion(request.getSession()).getCodigoInstitucionInt())));
		
		forma.setPaises(Utilidades.obtenerPaises(con));
		
		forma.setEspecialidades(Utilidades.obtenerEspecialidadesEnArray(con, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido));
		
		if(!forma.isEsEmpresaInstitucion())
		{	
		    forma.setInstituciones(Utilidades.obtenerInstituciones(con));
		    forma.setInstitucionSel(usuario.getCodigoInstitucionInt());
		}
		else
		{
			forma.setInstituciones(Utilidades.obtenerEmpresasInstitucion(con, usuario.getCodigoInstitucionInt()));
			forma.setInstitucionSel(ConstantesBD.codigoNuncaValido);
		}
		
		
		
		// Centro Atención
		forma.setCentroAtencion(usuario.getCodigoCentroAtencion());
		// Cargar Todos los centros de atención
		cargarCentrosAtencion(forma);
		// Cargar por defecto la ciudad y el pais del centro de atención por el cual se encuentra logueado el usuario
		cargarPaisCiudadXcentroAtencion(forma);
		
		
		
		forma.setCiudades(Utilidades.obtenerCiudadesXPais(con, forma.getPais()+""));
		
		ArrayList pooles = Utilidades.obtenerPoolesMedicoArrayMap(con, UtilidadFecha.getFechaActual(), ConstantesBD.codigoNuncaValido);
		forma.setPooles(pooles);
		
		if (pooles != null && pooles.size() > 0) {
			forma.setMostrarPooles("true");
		}else{
			forma.setMostrarPooles("false");
		}
		
		
		forma.setRegiones(RegionesCobertura.cargar(new DtoRegionesCobertura()));
		
		
		
		forma.setProfesionales(UtilidadesManejoPaciente.obtenerProfesionales(con, usuario.getCodigoInstitucionInt(), true, false, "", "", ""));
		
		//forma.setEspecialidades(Utilidades.obtenerEspecialidades());
		
		
	
		this.accionAplicaProgramasInstitucion(usuario, forma);
		
		forma.setTipoBusquedaServicio(ValoresPorDefecto.getNombreManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt()));
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}

	
	
	/**
	 * Accion Aplica Programas Institucion
	 * @author Edgar Carvajal Ruiz
	 * @param usuario
	 * @param forma
	 */
	private void accionAplicaProgramasInstitucion(UsuarioBasico usuario,
												ConsultaHonorariosMedicosForm forma) 
	{
		
		
		if( ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo) )
		  {
			  forma.setUtilizarProgramasInstitucion(Boolean.FALSE);
		  }
		  else
		  {
			  forma.setUtilizarProgramasInstitucion(Boolean.TRUE);
		  }
		
	}

	/**
	 * Metodo para cargar la ciudad y el pais segun un centro de atención
	 * @param forma
	 * @return
	 */
	private void cargarPaisCiudadXcentroAtencion(ConsultaHonorariosMedicosForm forma) {
		for(int i=0; i<forma.getCentrosAtencion().size(); i++){
			if(forma.getCentrosAtencion().get(i).getConsecutivo() == forma.getCentroAtencion()){
				forma.setDeptoCiudad(forma.getCentrosAtencion().get(i).getDepartamento()+ConstantesBD.separadorSplit+forma.getCentrosAtencion().get(i).getCiudad());
				forma.setPais(Utilidades.convertirAEntero(forma.getCentrosAtencion().get(i).getPais()));
			}
		}
	}

	/**
	 * Metodo para cargar los centros de atención
	 * @param forma
	 */
	private void cargarCentrosAtencion(ConsultaHonorariosMedicosForm forma) {
		
		DtoCentrosAtencion ca = new DtoCentrosAtencion();
		ca.setRegionCobertura(Utilidades.convertirAEntero(forma.getRegion()));
		ca.setActivo(true);
		forma.setCentrosAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(ca));
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param request
	 * @param usuario
	 */
	private void accionGenerarReporte(Connection con,
			ConsultaHonorariosMedicosForm forma, HttpServletRequest request,
			UsuarioBasico usuario) {
		HashMap resul = new HashMap();		
		
		try
		{
			  if(!forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
		        {
			resul = generarReporteValorHonorario(con,forma,(InstitucionBasica)request.getSession().getAttribute("institucionBasica"),usuario);
		        }
			  else {
		    resul = generarReporteValorHonorarioPlano(con,forma,(InstitucionBasica)request.getSession().getAttribute("institucionBasica"),usuario);
				  
			  }
			if(!resul.get("descripcion").toString().equals(""))
	        {
	        	request.setAttribute("isOpenReport", "true");
	        	request.setAttribute("newPathReport",resul.get("descripcion").toString());
	        	forma.setMapaArchivoPlano(resul);
	        }
		}
		catch (Exception e) {
			logger.error("ERROR", e);
		}
	}

	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private HashMap generarReporteValorHonorario(Connection con,
			ConsultaHonorariosMedicosForm forma, InstitucionBasica ins,
			UsuarioBasico usuario) {
		//HashMap condicionesWhere;		
		HashMap result = new HashMap();
    	
		Vector v = new Vector();
		
		String nombreRptDesign= "";
		//la salida del archivo es cvs		
      
        	if(forma.getTipoReporte().equals("valorHonorario"))
				nombreRptDesign = "HonorariosValHonorario.rptdesign";
			else
				nombreRptDesign = "HonorariosValFactura.rptdesign";
       
		
		//***************** INFORMACIÓN DEL CABEZOTE
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
         
        // Logo
        comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        v.add(ins.getRazonSocial());
        if(Utilidades.convertirAEntero(ins.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit()+" - "+ins.getDigitoVerificacion());
        else
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit());
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0, 1, v);
        
        //Información del reporte
        comp.insertLabelInGridPpalOfHeader(1,0,"CONSULTA HONORARIOS MÉDICOS VALOR HONORARIO");
        
        //Información del reporte
        String infoReporte = "Fecha de Generación Inicial: "+forma.getFechaInicial()+"  -  Fecha de Generación Final: "+forma.getFechaFinal()+"             Pool: "+obtenerDescripcionPool(forma);
       
        comp.insertLabelInGridPpalOfHeader(2,0,infoReporte);
        
        //Fecha Hora      
        comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual(con)+" Hora: "+UtilidadFecha.getHoraActual(con));
        
        //Usuario        
        comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
    	
        // Crear objeto filtros para obtener las consultas necesarias
        HashMap filtros = new HashMap();
        filtros.put("pool", forma.getPool());
        filtros.put("profesional", forma.getProfesional());
        filtros.put("centroAtencion", forma.getCentroAtencion());
        filtros.put("fechaInicial", forma.getFechaInicial());
        filtros.put("fechaFinal", forma.getFechaFinal());
        filtros.put("facturaInicial", forma.getFacturaInicial());
        filtros.put("facturaFinal", forma.getFacturaFinal());
        filtros.put("institucionSel", forma.getInstitucionSel());
        filtros.put("especialidad", forma.getEspecialidad());
        filtros.put("nroOrden", forma.getConsecutivoOrdenMedica());
        filtros.put("ciudad",forma.getDeptoCiudad().split(ConstantesBD.separadorSplit)[1]);
        filtros.put("pais", forma.getPais());
        filtros.put("soloAnuladasXProfesional", ConstantesBD.acronimoNo);
        filtros.put("anuladasXFecha", ConstantesBD.acronimoNo);
        filtros.put("anuladasXFechaSinCodigo", ConstantesBD.acronimoNo);
        filtros.put("anuladasSinCodigo", ConstantesBD.acronimoNo);
        
        // Si se selecciono que se debian mostrar tambien las facturas anuladas entonces se debe realizar una union para traerlas con el valor factura cero
        String newqueryDataSet= "";
        String newqueryTotalXPool= "";
        String newqueryFacturasAnuladas= "";
        String newqueryDataSet1="", newqueryDataSet2="";
       
        if(forma.getMostrarFacturasAnuladas().toString().equals(ConstantesBD.acronimoSi)){
        	if(forma.getTipoReporte().equals("valorHonorario")){
        		filtros.put("mostrarFacturasAnuladas", ConstantesBD.acronimoNo);
        		filtros.put("valorFacturaCero", ConstantesBD.acronimoNo);
        		newqueryDataSet1 = ConsultasBirt.honorariosMedicosValHonorario(filtros);
    			filtros.put("mostrarFacturasAnuladas", ConstantesBD.acronimoSi);
    			filtros.put("valorFacturaCero", ConstantesBD.acronimoNo);
    			newqueryDataSet2 = ConsultasBirt.honorariosMedicosValHonorario(filtros);
    			newqueryDataSet = "("+newqueryDataSet1+") UNION ("+newqueryDataSet2+")";
    			
    			filtros.put("anuladasXFechaSinCodigo", ConstantesBD.acronimoSi);
    			newqueryDataSet1 = ConsultasBirt.honorariosMedicosValHonorario(filtros);
    			filtros.put("anuladasXFecha", ConstantesBD.acronimoSi);
    			filtros.put("anuladasXFechaSinCodigo", ConstantesBD.acronimoNo);
    			newqueryDataSet2 = ConsultasBirt.honorariosMedicosValHonorario(filtros);
    			newqueryTotalXPool = "("+newqueryDataSet1+") UNION ("+newqueryDataSet2+")";
    			
        	} else {
        		filtros.put("mostrarFacturasAnuladas", ConstantesBD.acronimoNo);
        		filtros.put("valorFacturaCero", ConstantesBD.acronimoNo);
        		newqueryDataSet1 = ConsultasBirt.honorariosMedicosValFacturaValHonorario(filtros);
    			filtros.put("mostrarFacturasAnuladas", ConstantesBD.acronimoSi);
    			filtros.put("valorFacturaCero", ConstantesBD.acronimoNo);
    			newqueryDataSet2 = ConsultasBirt.honorariosMedicosValFacturaValHonorario(filtros);
    			newqueryDataSet = "("+newqueryDataSet1+") UNION ("+newqueryDataSet2+")";
    			
    			filtros.put("anuladasXFechaSinCodigo", ConstantesBD.acronimoSi);
    			newqueryDataSet1 = ConsultasBirt.honorariosMedicosValFacturaValHonorario(filtros);
    			filtros.put("anuladasXFecha", ConstantesBD.acronimoSi);
    			filtros.put("anuladasXFechaSinCodigo", ConstantesBD.acronimoNo);
    			newqueryDataSet2 = ConsultasBirt.honorariosMedicosValFacturaValHonorario(filtros);
    			newqueryTotalXPool = "("+newqueryDataSet1+") UNION ("+newqueryDataSet2+")";
        	}
        } else {
        	filtros.put("mostrarFacturasAnuladas", ConstantesBD.acronimoNo);
        	filtros.put("valorFacturaCero", ConstantesBD.acronimoNo);
        	if(forma.getTipoReporte().equals("valorHonorario")) {
        		newqueryDataSet1 = ConsultasBirt.honorariosMedicosValHonorario(filtros);
        		filtros.put("anuladasSinCodigo", ConstantesBD.acronimoSi);
        		newqueryDataSet2 = ConsultasBirt.honorariosMedicosValHonorario(filtros);
        	} else {
    			newqueryDataSet1 = ConsultasBirt.honorariosMedicosValFacturaValHonorario(filtros);
    			filtros.put("anuladasSinCodigo", ConstantesBD.acronimoSi);
    			newqueryDataSet2 = ConsultasBirt.honorariosMedicosValFacturaValHonorario(filtros);
        	}
        	newqueryDataSet = "("+newqueryDataSet1+") UNION ("+newqueryDataSet2+")";
        	newqueryTotalXPool = newqueryDataSet;
        }
        comp.obtenerComponentesDataSet("dataSet");
        comp.modificarQueryDataSet("SELECT * FROM ("+newqueryDataSet+") datos ORDER BY getnombrepersona(CAST (\"codigo_medico_responde\" AS INTEGER)),\"factura\"");
     
        // Consulta para el resumen de honorarios medicos solo se muestra si se ha seleccionado algun pool
        //para el totalxpool, nunca se mirarn las anuladas.
        /*filtros.put("mostrarFacturasAnuladas", ConstantesBD.acronimoNo);
    	filtros.put("valorFacturaCero", ConstantesBD.acronimoNo);
    	if(forma.getTipoReporte().equals("valorHonorario"))
			newquery = ConsultasBirt.honorariosMedicosValHonorario(filtros);
		else
			newquery = ConsultasBirt.honorariosMedicosValFacturaValHonorario(filtros);
        */
    	
    	//Se pasa la misma consulta generada para el dataset 'dataSet' ya que se necesitan
        //consultar las facturas anuladas pero se valida que se haya seleccionado un pool
        //para mostrar el resumen de las facturas
        
        comp.obtenerComponentesDataSet("totalXPool");
        if(forma.getPool()!=ConstantesBD.codigoNuncaValido)
        	//comp.modificarQueryDataSet("SELECT * FROM ("+newquery+")");
        { comp.modificarQueryDataSet("SELECT * FROM ("+newqueryTotalXPool+") datos ORDER BY  getnombrepersona(CAST (\"codigo_medico_responde\" AS INTEGER)),\"factura\"");
       
        }
        else
        	comp.modificarQueryDataSet("SELECT * FROM ("+newqueryTotalXPool+") datos WHERE 1=2 ");
        // se modifica la consulta para el data set que muestra las facturas anuladas
        
        filtros.put("mostrarFacturasAnuladas", ConstantesBD.acronimoSi);
    	filtros.put("valorFacturaCero", ConstantesBD.acronimoNo);
    	filtros.put("soloAnuladasXProfesional", ConstantesBD.acronimoSi);
    	if(forma.getTipoReporte().equals("valorHonorario")){
    		newqueryFacturasAnuladas = ConsultasBirt.honorariosMedicosValHonorario(filtros);
    
    	} else {
        	newqueryFacturasAnuladas = ConsultasBirt.honorariosMedicosValFacturaValHonorario(filtros)+"";
        }
        
    	// No mostrar la seccion de facturas anuladas si no ha sido requerida
    	if(forma.getMostrarFacturasAnuladas().toString().equals(ConstantesBD.acronimoNo)){
    		newqueryFacturasAnuladas = "SELECT * FROM ("+newqueryFacturasAnuladas+") ta WHERE 1=2 ";
    	}
    	
    	//System.out.println("**************************\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    	//System.out.println(newquery);
    	//System.out.println("**************************\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    	
        comp.obtenerComponentesDataSet("facturasAnuladas");
        comp.modificarQueryDataSet(newqueryFacturasAnuladas);
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);	
		
		//newPathReport+="&fechaInicial="+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaInicial());
		
        comp.updateJDBCParameters(newPathReport);
	   
        result.put("descripcion",newPathReport);
        result.put("resultado",true);
        result.put("urlArchivoPlano","");
		result.put("pathArchivoPlano","");
        
        //la salida del archivo es cvs		
        if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
        {        	       	
        	logger.info("ENTRO A IMPRIMIR ARCHIVO PLANO 1");
        	logger.info("newPathReport "+newPathReport);
        	logger.info("nombreRptDesign "+nombreRptDesign);
        	ResultadoBoolean resultado = ExtractCSV.extraerArchivoCsvComprimido(newPathReport,nombreRptDesign);
        	if(resultado.isTrue())
        	{
        		logger.info("ENTRO A IMPRIMIR ARCHIVO PLANO 2");
        		//Se toman las rutas
        		String[] rutas = resultado.getDescripcion().split(ConstantesBD.separadorSplit);
        		result.put("urlArchivoPlano",rutas[0]);
        		result.put("pathArchivoPlano",rutas[1]);
        	}
        	else        	
        		result.put("resultado",false);       
        }
        return result;
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private HashMap generarReporteValorHonorarioPlano(Connection con,
			ConsultaHonorariosMedicosForm forma, InstitucionBasica ins,
			UsuarioBasico usuario) {
		//HashMap condicionesWhere;		
		HashMap result = new HashMap();
		HashMap resultadosPrincipal= new HashMap();
		HashMap resultadosAnuladas= new HashMap();
		HashMap resultadosResumen= new HashMap();
    	
		ArrayList<DtoHonorariosMedico> listaProfesionales	=new ArrayList<DtoHonorariosMedico>();
		
		ArrayList<DtoHonorariosMedico> listaFacturas	=new ArrayList<DtoHonorariosMedico>();
		ArrayList<DtoHonorariosMedico> listaAnuladas	=new ArrayList<DtoHonorariosMedico>();
		ArrayList<DtoHonorariosMedico> listaResumen		=new ArrayList<DtoHonorariosMedico>();
		
		DtoHonorariosMedico DtoFinalReporte = new DtoHonorariosMedico();
		DtoFinalReporte.setFechaInicial(forma.getFechaInicial());
		DtoFinalReporte.setFechaFinal(forma.getFechaFinal());
		
		Vector v = new Vector();
		
		String nombreRptDesign= "";
		//la salida del archivo es cvs		
      
			if(forma.getTipoReporte().equals("valorHonorario"))
				nombreRptDesign = "HonorariosValHonorario.rptdesign";
			else
				nombreRptDesign = "HonorariosValFactura.rptdesign";
      
		
		//***************** INFORMACIÓN DEL CABEZOTE
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
         
        // Crear objeto filtros para obtener las consultas necesarias
        HashMap filtros = new HashMap();
        filtros.put("pool", forma.getPool());
        filtros.put("profesional", forma.getProfesional());
        filtros.put("centroAtencion", forma.getCentroAtencion());
        filtros.put("fechaInicial", forma.getFechaInicial());
        filtros.put("fechaFinal", forma.getFechaFinal());
        filtros.put("facturaInicial", forma.getFacturaInicial());
        filtros.put("facturaFinal", forma.getFacturaFinal());
        filtros.put("institucionSel", forma.getInstitucionSel());
        filtros.put("especialidad", forma.getEspecialidad());
        filtros.put("nroOrden", forma.getConsecutivoOrdenMedica());
        filtros.put("ciudad",forma.getDeptoCiudad().split(ConstantesBD.separadorSplit)[1]);
        filtros.put("pais", forma.getPais());
        filtros.put("soloAnuladasXProfesional", ConstantesBD.acronimoNo);
        filtros.put("anuladasXFecha", ConstantesBD.acronimoNo);
        filtros.put("anuladasXFechaSinCodigo", ConstantesBD.acronimoNo);
        filtros.put("anuladasSinCodigo", ConstantesBD.acronimoNo);
        
        // Si se selecciono que se debian mostrar tambien las facturas anuladas entonces se debe realizar una union para traerlas con el valor factura cero
        String newqueryDataSet= "";
        String newqueryTotalXPool= "";
        String newqueryFacturasAnuladas= "";
        String newqueryDataSet1="", newqueryDataSet2="";
        
        // consultas para cuando es archivo plano
        String consultaPrincipal="";
        String consultaAnuladas="";
        String consultaResumen="";
        
        //las facturas se obtiene del reporte en birt, ya que son muy complejas para hacerlas de nuevo y hay poco tiempo.
        if(forma.getMostrarFacturasAnuladas().toString().equals(ConstantesBD.acronimoSi)){
        	if(forma.getTipoReporte().equals("valorHonorario")){
        		filtros.put("mostrarFacturasAnuladas", ConstantesBD.acronimoNo);
        		filtros.put("valorFacturaCero", ConstantesBD.acronimoNo);
        		newqueryDataSet1 = ConsultasBirt.honorariosMedicosValHonorario(filtros);
    			filtros.put("mostrarFacturasAnuladas", ConstantesBD.acronimoSi);
    			filtros.put("valorFacturaCero", ConstantesBD.acronimoNo);
    			newqueryDataSet2 = ConsultasBirt.honorariosMedicosValHonorario(filtros);
    			newqueryDataSet = "("+newqueryDataSet1+") UNION ("+newqueryDataSet2+")";
    			
    			filtros.put("anuladasXFechaSinCodigo", ConstantesBD.acronimoSi);
    			newqueryDataSet1 = ConsultasBirt.honorariosMedicosValHonorario(filtros);
    			filtros.put("anuladasXFecha", ConstantesBD.acronimoSi);
    			filtros.put("anuladasXFechaSinCodigo", ConstantesBD.acronimoNo);
    			newqueryDataSet2 = ConsultasBirt.honorariosMedicosValHonorario(filtros);
    			newqueryTotalXPool = "("+newqueryDataSet1+") UNION ("+newqueryDataSet2+")";
    			
        	} else {
        		filtros.put("mostrarFacturasAnuladas", ConstantesBD.acronimoNo);
        		filtros.put("valorFacturaCero", ConstantesBD.acronimoNo);
        		newqueryDataSet1 = ConsultasBirt.honorariosMedicosValFacturaValHonorario(filtros);
    			filtros.put("mostrarFacturasAnuladas", ConstantesBD.acronimoSi);
    			filtros.put("valorFacturaCero", ConstantesBD.acronimoNo);
    			newqueryDataSet2 = ConsultasBirt.honorariosMedicosValFacturaValHonorario(filtros);
    			newqueryDataSet = "("+newqueryDataSet1+") UNION ("+newqueryDataSet2+")";
    			
    			filtros.put("anuladasXFechaSinCodigo", ConstantesBD.acronimoSi);
    			newqueryDataSet1 = ConsultasBirt.honorariosMedicosValFacturaValHonorario(filtros);
    			filtros.put("anuladasXFecha", ConstantesBD.acronimoSi);
    			filtros.put("anuladasXFechaSinCodigo", ConstantesBD.acronimoNo);
    			newqueryDataSet2 = ConsultasBirt.honorariosMedicosValFacturaValHonorario(filtros);
    			newqueryTotalXPool = "("+newqueryDataSet1+") UNION ("+newqueryDataSet2+")";
        	}
        } else {
        	filtros.put("mostrarFacturasAnuladas", ConstantesBD.acronimoNo);
        	filtros.put("valorFacturaCero", ConstantesBD.acronimoNo);
        	if(forma.getTipoReporte().equals("valorHonorario")) {
        		newqueryDataSet1 = ConsultasBirt.honorariosMedicosValHonorario(filtros);
        		filtros.put("anuladasSinCodigo", ConstantesBD.acronimoSi);
        		newqueryDataSet2 = ConsultasBirt.honorariosMedicosValHonorario(filtros);
        	} else {
    			newqueryDataSet1 = ConsultasBirt.honorariosMedicosValFacturaValHonorario(filtros);
    			filtros.put("anuladasSinCodigo", ConstantesBD.acronimoSi);
    			newqueryDataSet2 = ConsultasBirt.honorariosMedicosValFacturaValHonorario(filtros);
        	}
        	newqueryDataSet = "("+newqueryDataSet1+") UNION ("+newqueryDataSet2+")";
        	newqueryTotalXPool = newqueryDataSet;
        }
        comp.obtenerComponentesDataSet("dataSet");
        comp.modificarQueryDataSet("SELECT * FROM ("+newqueryDataSet+") datos ORDER BY  getnombrepersona(CAST (\"codigo_medico_responde\" AS INTEGER)),\"factura\"");
        consultaPrincipal="SELECT * FROM ("+newqueryDataSet+") datos";
    
        
        comp.obtenerComponentesDataSet("totalXPool");
   
     
        // se modifica la consulta para el data set que muestra las facturas anuladas
        
    	
    		
        filtros.put("mostrarFacturasAnuladas", ConstantesBD.acronimoSi);
        filtros.put("valorFacturaCero", ConstantesBD.acronimoNo);
    	filtros.put("soloAnuladasXProfesional", ConstantesBD.acronimoSi);
    	
    	if(forma.getTipoReporte().equals("valorHonorario")){
    		newqueryFacturasAnuladas = ConsultasBirt.honorariosMedicosValHonorario(filtros);
    		consultaAnuladas= newqueryFacturasAnuladas;
    	} else {
        	newqueryFacturasAnuladas = ConsultasBirt.honorariosMedicosValFacturaValHonorario(filtros)+"";
            consultaAnuladas= newqueryFacturasAnuladas;
    	}
    	
        
     	
    	//System.out.println("**************************\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    	//System.out.println(newquery);
    	//System.out.println("**************************\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    	
    	// se buscan los datos para el dto
    	try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaPrincipal, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			resultadosPrincipal=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
						
			ps.close();
			
	
		}
		catch (SQLException e)
		{
			logger.info("\n\nERROR. CONSULTANDO HONORARIOS PRINCIPAL------>>>>>>"+e);
			e.printStackTrace();
		}
    	
        // Se pasan los resultados a un Dto para enviar al reporte
    	 
    	// Se carga el Dto con los resultados de los honorarios
    	 
    	 String[] medicos = new String[Utilidades.convertirAEntero(resultadosPrincipal.get("numRegistros").toString())];
    	 int nuevo=0;
    	 
    	 for (int i=0; i<(Utilidades.convertirAEntero(resultadosPrincipal.get("numRegistros").toString())); i++)
    	 { 
    		 //codigo para no repetir PROFESIONALES
    		 boolean repetido=true;
    		  DtoHonorariosMedico Temp= new DtoHonorariosMedico();
    		try{
    		 if (nuevo==0)
    		 { medicos[0]=resultadosPrincipal.get("profesional_"+i).toString(); 
    		 nuevo++;
    		 repetido=false;}
    		 else {
    			 repetido=false;
    			 for (int j=0; j<medicos.length; j++)
    			 {
    				 if ((resultadosPrincipal.get("profesional_"+i).toString()).equals(medicos[j]))
    				 {
    					repetido=true; 
    				 }
    			 }
    			 if (!repetido)
    			 {
    				 medicos[nuevo]=resultadosPrincipal.get("profesional_"+i).toString(); 
    	    		 nuevo++; 
    			 }
    			 
    		 }
    		} catch (Exception e){ e.printStackTrace();}
    		 
    		// LLENAMOS LAS FACTURAS DE CADA PROFESIONAL
    		 if (!repetido)
    		 {
    			 Temp.setNombreProfesional(resultadosPrincipal.get("profesional_"+i).toString());
    			 listaFacturas	=new ArrayList<DtoHonorariosMedico>();
    			 DtoHonorariosMedico temp2= new DtoHonorariosMedico();
    			 DtoHonorariosMedico temp3= new DtoHonorariosMedico();
    			 for (int k=0; k<(Utilidades.convertirAEntero(resultadosPrincipal.get("numRegistros").toString())); k++)
    	    	 { 
    				 if ((resultadosPrincipal.get("profesional_"+i).toString()).equals(resultadosPrincipal.get("profesional_"+k).toString()))
    				 {
    				 temp2= new DtoHonorariosMedico();
    				 temp2.setCentroAtencion(resultadosPrincipal.get("centroatencion_"+k).toString());
    				 temp2.setConvenio(resultadosPrincipal.get("convenio_"+k).toString());
    				 temp2.setFactura(resultadosPrincipal.get("factura_"+k).toString());
    				 temp2.setOrdenMedica(resultadosPrincipal.get("nroorden_"+k).toString());
    				 temp2.setFechaGeneracion(resultadosPrincipal.get("fecha_"+k).toString()); 
    				 temp2.setIdentificacionPac(resultadosPrincipal.get("tiponumidpac_"+k).toString());
    				 temp2.setPaciente(resultadosPrincipal.get("nombrepac_"+k).toString());
    				 temp2.setServicio(resultadosPrincipal.get("servicio_"+k).toString());
    				 temp2.setValorHonorario(resultadosPrincipal.get("valorpool_"+k).toString());  
    				
    				 if(!forma.getTipoReporte().equals("valorHonorario")){
    					 temp2.setEsFacturado(1);
    				 temp2.setValorFacturado(resultadosPrincipal.get("valorfactura_"+k).toString());}
    				 
    				 String linea="";
    				 linea+= temp2.getFactura()+" | "+temp2.getOrdenMedica()+" | "+temp2.getIdentificacionPac()+" | "+temp2.getPaciente()+" | "+temp2.getServicio()+" | "+temp2.getConvenio()+" | "+temp2.getFechaGeneracion()+" | "+temp2.getCentroAtencion()+" | "+temp2.getValorHonorario();
    				 String titulo="factura | orden medica | tipo y numero de id | paciente | servicio | convenio | fecha | centro atencion | valor honorario";
    				  if(!forma.getTipoReporte().equals("valorHonorario")){
    					 linea+=" | "+temp2.getValorFacturado();
    					 titulo+="| valor facturado";
    				 }
    				 temp2.setLineaFacturas(linea);
    				 temp2.setTituloFacturas(titulo);
    				 
    				 listaFacturas.add(temp2);
    				 
    				 }
    			 }
    			 
    			 // llenamos facturas anuladas
    			 if(forma.getMostrarFacturasAnuladas().toString().equals(ConstantesBD.acronimoSi)){
    				HashMap prof= new HashMap();
    				int m=0;
    				//revisamos que profesionales tienen facturas anuladas
    				 for (int k=0; k<(Utilidades.convertirAEntero(resultadosPrincipal.get("numRegistros").toString())); k++)
    				 {
    					 if(resultadosPrincipal.get("consecutivoAnulacion_"+k)!=null && resultadosPrincipal.get("consecutivoAnulacion_"+k)!="")
    					 {
    					 prof.put("medico"+m,resultadosPrincipal.get("profesional_"+k).toString());
    					 m++;
    					 }
    				 }
    				 prof.put("numReg",m);
    				 boolean valido=false;
    				 for (int k=0; k<(Utilidades.convertirAEntero(prof.get("numReg").toString())); k++)
    				 {
    					 if ((resultadosPrincipal.get("profesional_"+i).toString()).equals(prof.get("medico"+k).toString()))
    					 {
    						 valido=true;
    					 }
    				 }
    				
    				 if (valido)
    			 {
    				 for (int k=0; k<(Utilidades.convertirAEntero(resultadosPrincipal.get("numRegistros").toString())); k++)
    	    	 { 
    			     if(resultadosPrincipal.get("consecutivoAnulacion_"+k)!=null && resultadosPrincipal.get("consecutivoAnulacion_"+k)!=""){
    					 if ((resultadosPrincipal.get("profesional_"+i).toString()).equals(resultadosPrincipal.get("profesional_"+k).toString()))
    				 {
    				 	 Temp.setEsAnulado(1);
    				 temp3= new DtoHonorariosMedico();
    				 temp3.setCentroAtencion(resultadosPrincipal.get("centroatencion_"+k).toString());
    				 temp3.setConvenio(resultadosPrincipal.get("convenio_"+k).toString());
    				 temp3.setFactura(resultadosPrincipal.get("factura_"+k).toString());
    				 temp3.setOrdenMedica(resultadosPrincipal.get("nroorden_"+k).toString());
    				 temp3.setFechaGeneracion(resultadosPrincipal.get("fecha_"+k).toString()); 
    				 temp3.setIdentificacionPac(resultadosPrincipal.get("tiponumidpac_"+k).toString());
    				 temp3.setPaciente(resultadosPrincipal.get("nombrepac_"+k).toString());
    				 temp3.setServicio(resultadosPrincipal.get("servicio_"+k).toString());
    				 temp3.setValorHonorario(resultadosPrincipal.get("valorpool_"+k).toString());  
    				
    				 String linea="";
    				 linea+= temp3.getFactura()+" | "+temp3.getOrdenMedica()+" | "+temp3.getIdentificacionPac()+" | "+temp3.getPaciente()+" | "+temp3.getServicio()+" | "+temp3.getConvenio()+" | "+temp3.getFechaGeneracion()+" | "+temp3.getCentroAtencion()+" | "+temp3.getValorHonorario();
    				 String titulo="factura| orden medica| tipo y numero de id| paciente| servicio| convenio| fecha| centro atencion| valor honorario";
    				
    				 if(!forma.getTipoReporte().equals("valorHonorario")){
    					 temp3.setEsFacturado(1);
    				 temp3.setValorFacturado(resultadosPrincipal.get("valorfactura_"+k).toString());
    				 linea+=" | "+temp3.getValorFacturado();
 					 titulo+="| valor facturado";
    				 }
    			
     				 temp3.setLineaAnuladas(linea);
     				 temp3.setTituloAnuladas(titulo);
    				 
    				 listaAnuladas.add(temp3);
    				 }
    				 }
    				
    			 }}
    			 }
    			
    			 if(forma.getMostrarFacturasAnuladas().toString().equals(ConstantesBD.acronimoSi)){
    					HashMap prof= new HashMap();
        				int m=0;
        				//revisamos que profesionales tienen facturas anuladas
        				 for (int k=0; k<(Utilidades.convertirAEntero(resultadosPrincipal.get("numRegistros").toString())); k++)
        				 {
        					 if(resultadosPrincipal.get("consecutivoAnulacion_"+k)!=null && resultadosPrincipal.get("consecutivoAnulacion_"+k)!="")
        					 {
        					 prof.put("medico"+m,resultadosPrincipal.get("profesional_"+k).toString());
        					 m++;
        					 }
        				 }
        				 prof.put("numReg",m);
        				 boolean valido=false;
        				 for (int k=0; k<(Utilidades.convertirAEntero(prof.get("numReg").toString())); k++)
        				 {
        					 if ((resultadosPrincipal.get("profesional_"+i).toString()).equals(prof.get("medico"+k).toString()))
        					 {
        						 valido=true;
        					 }
        				 }
        				 if (valido)
        				 { Temp.setEsAnulado(1); }
    			 }
    			 if(!forma.getTipoReporte().equals("valorHonorario")){
    				 Temp.setEsFacturado(1);
    			 }
    			 Temp.setListaFacturas(listaFacturas); 
    			 Temp.setListaFacturasAnuladas(listaAnuladas);
    			 listaAnuladas=new ArrayList<DtoHonorariosMedico>();
    			 listaProfesionales.add(Temp);
    		 }
    	    		 
    	 } 
    	// Se calculan los totales
    	 for (DtoHonorariosMedico dtoTemp:listaProfesionales)
    	 {
    		 double totalHonorarios=0;
    		 double totalHonorariosF=0;
    		 double totalFacturas=0;
    		 double totalFacturasF=0;
    		 
    		 for (DtoHonorariosMedico dtoTemp1: dtoTemp.getListaFacturas())
    		 {  totalHonorarios= totalHonorarios+ Double.parseDouble(dtoTemp1.getValorHonorario());
    		
    		 if(!forma.getTipoReporte().equals("valorHonorario")){    		
    			 totalHonorariosF=totalHonorariosF+ Double.parseDouble(dtoTemp1.getValorFacturado());
    		 }
    		 }
	         if(forma.getMostrarFacturasAnuladas().toString().equals(ConstantesBD.acronimoSi)){
    			 
    			 for (DtoHonorariosMedico dtoTemp1: dtoTemp.getListaFacturasAnuladas())
        		 {
        			 totalFacturas= totalFacturas+ Double.parseDouble(dtoTemp1.getValorHonorario());
        			 if(!forma.getTipoReporte().equals("valorHonorario")){   
        			 totalFacturasF=totalFacturasF+ Double.parseDouble(dtoTemp1.getValorFacturado());
        			 }
        		 } 
    			 
    		 }
    	    		 
    		 dtoTemp.setValorHonorarioTotal(String.valueOf(totalHonorarios));
    		 String totales="Totales | ";
    		 totales+=String.valueOf(totalHonorarios);
    		 if(!forma.getTipoReporte().equals("valorHonorario")){   
    			 dtoTemp.setValorFacturadoTotal(String.valueOf(totalHonorariosF));
    			 totales+=" | "+String.valueOf(totalHonorariosF);
    		 }
    		 dtoTemp.setTotalesFactura(totales);
    		 totales="Totales | ";
    		 if(forma.getMostrarFacturasAnuladas().toString().equals(ConstantesBD.acronimoSi)){
    	     totales+=totalFacturas;
    		 dtoTemp.setValorHonorarioATotal(String.valueOf(totalFacturas));
    		 if(!forma.getTipoReporte().equals("valorHonorario")){ 
    			 totales+=" | "+totalFacturasF;
    			dtoTemp.setValorFacturadoATotal(String.valueOf(totalFacturasF)); 
    		 } 
    		 dtoTemp.setTotalesAnuladas(totales);
    		 }
    		 
    		
    		
    		 
    		 //Si es necesario se hace el resumen del pool
    		 if(forma.getPool()!=ConstantesBD.codigoNuncaValido)
        	 { 
    			  String lineaR = dtoTemp.getNombreProfesional()+" | "+String.valueOf(totalHonorarios);
    			 DtoHonorariosMedico dtoResumen = new DtoHonorariosMedico();
    			 dtoResumen.setNombreProfesionalRes(dtoTemp.getNombreProfesional());
    			 dtoResumen.setValorHonorarioRes(String.valueOf(totalHonorarios));
    			 if(forma.getMostrarFacturasAnuladas().toString().equals(ConstantesBD.acronimoSi)){
    				 dtoResumen.setEsAnulado(1);
    				 dtoResumen.setValorHonorarioARes(String.valueOf(totalFacturas));
    				 dtoResumen.setValorNetHonorario(String.valueOf(totalHonorarios-totalFacturas));
    				 lineaR+= " | "+dtoResumen.getValorHonorarioARes()+" | "+dtoResumen.getValorNetHonorario();
    			 }
    			 if(!forma.getTipoReporte().equals("valorHonorario")){   
    				 dtoResumen.setEsFacturado(1);
    				 dtoResumen.setValorFacturadoRes(String.valueOf(totalHonorariosF));
    				 lineaR+=" | "+dtoResumen.getValorFacturadoRes();
    				 if(forma.getMostrarFacturasAnuladas().toString().equals(ConstantesBD.acronimoSi)){
    					 dtoResumen.setValorFacturadoARes(String.valueOf(totalFacturasF));
    					 dtoResumen.setValorNetFacturado(String.valueOf(totalHonorariosF-totalFacturasF));
    					 lineaR+=" | "+dtoResumen.getValorFacturadoARes()+" | "+dtoResumen.getValorNetFacturado();
    				 }
    			 }
    		 dtoResumen.setLineasResumen(lineaR);
    		 listaResumen.add(dtoResumen);	 
        	 }
    	 
    	 }
    	 
    	 // Calculamos los totales del resumen si es necesario
    	 if(forma.getPool()!=ConstantesBD.codigoNuncaValido)
    	 { 
    		DtoFinalReporte.setListaResumenPool(listaResumen); 
    		String tituloR="profesional de la salud | valor honorarios";
    		String lineaR="totales | ";
    	 double totalHono=0;
    	 double totalHonoA=0;
    	 double totalFact=0;
    	 double totalFactA=0;
    	 if(forma.getMostrarFacturasAnuladas().toString().equals(ConstantesBD.acronimoSi)){
			 tituloR+="| valor honorario anulado| valor neto honorarios";}
    	 
    	 if(!forma.getTipoReporte().equals("valorHonorario")){  
			 tituloR+="| valor facturado";
			 if(forma.getMostrarFacturasAnuladas().toString().equals(ConstantesBD.acronimoSi)){
					tituloR+="| valor facturado anulado | valor neto facturado";}
    	 }
    	 
    	 
    	 for (DtoHonorariosMedico dtoTemp1: listaResumen)
		 { 
    		 totalHono=totalHono+ Double.parseDouble(dtoTemp1.getValorHonorarioRes());
    		 if(forma.getMostrarFacturasAnuladas().toString().equals(ConstantesBD.acronimoSi)){
    		     totalHonoA=totalHonoA+ Double.parseDouble(dtoTemp1.getValorHonorarioARes());    			
    		 }
    		 if(!forma.getTipoReporte().equals("valorHonorario")){  
    			
				 totalFact=totalFact+ Double.parseDouble(dtoTemp1.getValorFacturadoRes());
				 
    			 if(forma.getMostrarFacturasAnuladas().toString().equals(ConstantesBD.acronimoSi)){
    				totalFactA=Double.parseDouble(totalFactA+dtoTemp1.getValorFacturadoARes()); 
 				
    			 }    			 
    		 }
		 }
    	 DtoFinalReporte.setValorHonorarioResTotal(String.valueOf(totalHono));
    	 DtoFinalReporte.setValorFacturadoResTotal(String.valueOf(totalFact));
    	 DtoFinalReporte.setValorHonorarioAResTotal(String.valueOf(totalHonoA));
    	 DtoFinalReporte.setValorFacturadoAResTotal(String.valueOf(totalFactA));
    	 DtoFinalReporte.setValorNetHonorarioTotal(String.valueOf(totalHono-totalHonoA));
    	 DtoFinalReporte.setValorNetFacturadoTotal(String.valueOf(totalFact-totalFactA));
    	 
    	 lineaR+=String.valueOf(totalHono);
    	 if(forma.getMostrarFacturasAnuladas().toString().equals(ConstantesBD.acronimoSi)){
    		 lineaR+= "|"+String.valueOf(totalHonoA)+"| "+String.valueOf(totalHono-totalHonoA);}
    	 
    	 if(!forma.getTipoReporte().equals("valorHonorario")){  
    		 lineaR+="| "+String.valueOf(totalFact);
			 if(forma.getMostrarFacturasAnuladas().toString().equals(ConstantesBD.acronimoSi)){
					lineaR+="| "+String.valueOf(totalFactA)+"| "+String.valueOf(totalFact-totalFactA);}
    	 }
    	 
    	 DtoFinalReporte.setTituloResumen(tituloR);
    	 DtoFinalReporte.setTotalesResumen(lineaR);
    	 
    	 }
    	 
    	 DtoFinalReporte.setListaProfesionales(listaProfesionales);
    	 
    	 if(forma.getPool()!=ConstantesBD.codigoNuncaValido)
    	 { 
    		 DtoFinalReporte.setEsResumen(1);
    		 if(!forma.getTipoReporte().equals("valorHonorario")){
    			 DtoFinalReporte.setEsFacturado(1);
    		 }
    		 if(forma.getMostrarFacturasAnuladas().toString().equals(ConstantesBD.acronimoSi)){
    			 DtoFinalReporte.setEsAnulado(1);
    		 }
    		 
    	 }
    	 
    	 //Generador del Reporte
    	 String nombreArchivoOriginal ="";
    	 GeneradorReporteValorHonorariosMedico generadorReporte= null;
    	 generadorReporte= new GeneradorReporteValorHonorariosMedico(DtoFinalReporte);
    	 JasperPrint reporteOriginal=null;
		 reporteOriginal = generadorReporte.generarReporte();
		 String separador=",";
		 nombreArchivoOriginal= generadorReporte.exportarReporteTextoPlano(reporteOriginal, "HonorariosMedicosPlano");
		 result.put("descripcion",nombreArchivoOriginal);
		 result.put("resultado",true);
	     result.put("urlArchivoPlano",nombreArchivoOriginal);
	     result.put("pathArchivoPlano",nombreArchivoOriginal);
		
	        return result;
	}

	/**
	 * Obtiene la descripcion de un pool
	 * @param forma
	 * @return
	 */
	private String obtenerDescripcionPool(ConsultaHonorariosMedicosForm forma) {
		String descripcion = "";
		
		if(forma.getPool()==ConstantesBD.codigoNuncaValido)
			descripcion="Todos";
		else {
			for (int i=0; i <forma.getPooles().size(); i++) {
				HashMap pool = (HashMap) forma.getPooles().get(i);
				if(forma.getPool()==Utilidades.convertirAEntero(pool.get("codPool")+""))
					descripcion = pool.get("descripcion")+"";
			}
		}
		
		return descripcion;
	}

	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenarDetalle(ConsultaHonorariosMedicosForm forma)
	{
		String[] indices={
        		"codigofactura_",
        		"factura_",
                "descestado_",
        		"fechafactura_",
        		"orden_",
        		"codigoservicio_",
        		"codigoespecialidad_",
        		"codigoaxioma_",
        		"nombreservicio_",
        		"valorservicio_",
        		"valorajuste_",
        		"codigopool_",
        		"descpool_",
        		"porcentajepool_",
        		"porcentajemedico_",
        		"fechasolicitud_",
        		"valorajustesmedico_",
        		"nombrecentroatencion_",
        		"nomconvenio_"
        		};
        int numReg=Integer.parseInt(forma.getMapaHonorarios("numRegistros")+"");
		forma.setMapaHonorarios(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaHonorarios(),numReg));
		forma.setMapaHonorarios("numRegistros", numReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());
	}

	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenar(ConsultaHonorariosMedicosForm forma)
	{
		String[] indices={
        		"codigomedico_",
        		"profesional_",
        		"tipoid_",
        		"numeroid_",
        		"registro_",
        		"ocupacion_"
        		};
        int numReg=Integer.parseInt(forma.getMapaProfesionales("numRegistros")+"");
		forma.setMapaProfesionales(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaProfesionales(),numReg));
		forma.setMapaProfesionales("numRegistros", numReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 */
	private void accionDetalleHonorariosProfesional(Connection con, ConsultaHonorariosMedicosForm forma, ConsultaHonorariosMedicos mundo)
	{
        HashMap vo=new HashMap();
        vo.put("pool",forma.getPool());
        vo.put("fechaInicial",forma.getFechaInicial());
        vo.put("fechaFinal",forma.getFechaFinal());
        vo.put("facturaInicial",forma.getFacturaInicial());
        vo.put("facturaFinal",forma.getFacturaFinal());
		vo.put("centroAtencion", forma.getCentroAtencion());
		vo.put("especialidad", forma.getEspecialidad());
		vo.put("mostrarFacturasAnuladas", forma.getMostrarFacturasAnuladas());
        forma.setMapaHonorarios(mundo.consultarHonorariosProfesional(con,forma.getInstitucion(),Integer.parseInt(forma.getMapaProfesionales("codigomedico_"+forma.getIndex())+""),vo));
	}

	/**
	 * Accion Buscar Profesional de Las Salud.
	 * Metodo que arma un mapa dependiendo de los parametros de busqueada.
	 * y Llenar un mapa de Profesionales de la salud
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 */
	private void accionBuscarProfesionalesSalud(Connection con, ConsultaHonorariosMedicosForm forma, ConsultaHonorariosMedicos mundo)
	{
		HashMap vo=new HashMap();
		
		vo.put("pool",forma.getPool());
		vo.put("profesional",forma.getProfesional());
		vo.put("fechaInicial",forma.getFechaInicial());
		vo.put("fechaFinal",forma.getFechaFinal());
		vo.put("facturaInicial",forma.getFacturaInicial());
		vo.put("facturaFinal",forma.getFacturaFinal());
		vo.put("institucion",forma.getInstitucion());
		vo.put("centroAtencion", forma.getCentroAtencion());
		vo.put("ciudad", forma.getDeptoCiudad().split(ConstantesBD.separadorSplit)[1]);
		vo.put("pais", forma.getPais());
		vo.put("institucionSel", forma.getInstitucionSel());
		vo.put("nroOrden", forma.getConsecutivoOrdenMedica());
		vo.put("especialidad", forma.getEspecialidad());
		vo.put("mostrarFacturasAnuladas", forma.getMostrarFacturasAnuladas());
		vo.put("codigoPrograma", forma.getCodigoPrograma());
		vo.put("codigoServicio", forma.getCodigoServicio());
		
		forma.setMapaProfesionales(mundo.consultarProfesionalesSalud(con,vo));
	}	
}