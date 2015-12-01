package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.ListadoCamasHospitalizacionForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.ListadoCamasHospitalizacion;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

public class ListadoCamasHospitalizacionAction extends Action 
{
	
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	
	Logger logger =Logger.getLogger(ListadoCamasHospitalizacionAction.class);
		
	/**
	 * Método excute del Action
	 */
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if (form instanceof ListadoCamasHospitalizacionForm) 
			{
				ListadoCamasHospitalizacionForm forma = (ListadoCamasHospitalizacionForm) form;
				String estado=forma.getEstado();
				logger.info("Estado -->"+estado);
				con = UtilidadBD.abrirConexion();
				UsuarioBasico usuarioActual = Utilidades.getUsuarioBasicoSesion(request.getSession());
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica"); 
				ListadoCamasHospitalizacion mundo= new ListadoCamasHospitalizacion();
				forma.setMensaje(new ResultadoBoolean(false));

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ListadoCamasHospitalizacionAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					this.accionEmpezar(con, forma, usuarioActual, mapping);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("imprimir"))
				{
					this.accionBuscar(con, forma, mundo, usuarioActual, mapping, request, institucion);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de LISTADO MERCADEO DE CAMAS DE HOSPITALIZACION ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de ListadoCamasHospitalizacionForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}
		}catch (Exception e) {
			Log4JManager.error(e);
			e.printStackTrace();
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param usuarioActual
	 * @param mapping
	 */
	private void accionEmpezar(Connection con, ListadoCamasHospitalizacionForm forma, UsuarioBasico usuarioActual, ActionMapping mapping)
	{
		forma.reset();
		//Cargamos el select con todos los centros de atencion
		forma.setCentroAtencion(Utilidades.obtenerCentrosAtencion(usuarioActual.getCodigoInstitucionInt()));
		//Se selecciona el Centro de Atencion de Sesion
		forma.setCodigoCentroAtencion(usuarioActual.getCodigoCentroAtencion()+"");
	}

	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param institucion
	 * @return
	 */
	private ActionForward accionBuscar(Connection con, ListadoCamasHospitalizacionForm forma, ListadoCamasHospitalizacion mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, InstitucionBasica institucion)
	{
		if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			return this.generarReportePDF(con, forma, mundo, usuario, request, mapping);
		else if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
			return this.generarArchivoPlano(con, forma, mundo, usuario, request, mapping, institucion);
		
		return null;
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param request
	 * @param mapping
	 */
	private ActionForward generarReportePDF(Connection con, ListadoCamasHospitalizacionForm forma, ListadoCamasHospitalizacion mundo, UsuarioBasico usuarioActual, HttpServletRequest request, ActionMapping mapping)
	{
		String nombreRptDesign = "ListadoCamasHospitalizacion.rptdesign";
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		//Informacion del Cabezote
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfHeader(2,1, "MERCADEO DE CAMAS DE HOSPITALIZACIÓN");
        comp.insertLabelInGridPpalOfHeader(2,0, "CENTRO ATENCIÓN: "+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getCodigoCentroAtencion())));
        String cadenaEstados="";
        String separador=" - ";
        int contador=0;
        if(forma.getOcupadas().equals(ConstantesBD.acronimoSi))
        {
           	cadenaEstados+="Ocupadas";
           	contador++;
        }
        if(forma.getPendienteTrasladar().equals(ConstantesBD.acronimoSi) && contador>0)
        {
        	cadenaEstados+=separador;
        	cadenaEstados+="Pendientes por Trasladar";
        	contador++;
        }
        else if(forma.getPendienteTrasladar().equals(ConstantesBD.acronimoSi) && contador==0)
        {
        	cadenaEstados+="Pendientes por Trasladar";
        	contador++;
        }
        
        if(forma.getSalida().equals(ConstantesBD.acronimoSi) && contador>0)
        {
        	cadenaEstados+=separador;
        	cadenaEstados+="Con Salida";
        }
        else if(forma.getSalida().equals(ConstantesBD.acronimoSi))
          	cadenaEstados+="Con Salida";
                
        if(!cadenaEstados.equals(""))
        {	
        	cadenaEstados="ESTADOS CAMA: "+cadenaEstados;
        	comp.insertLabelInGridPpalOfHeader(3,0, ""+cadenaEstados );
        }
        comp.insertLabelInGridPpalOfHeader(4,0, "USUARIO: "+usuarioActual.getLoginUsuario());
        
        comp.obtenerComponentesDataSet("ListadoCamas");
        String condiciones="";
        if((forma.getOcupadas().equals(ConstantesBD.acronimoSi))&&forma.getPendienteTrasladar().equals(ConstantesBD.acronimoSi)&&forma.getSalida().equals(ConstantesBD.acronimoSi))
        	condiciones += ConstantesBD.codigoEstadoCamaOcupada +","+ ConstantesBD.codigoEstadoCamaPendientePorTrasladar +","+ ConstantesBD.codigoEstadoCamaConSalida;
        else if((forma.getOcupadas().equals(ConstantesBD.acronimoSi))&&forma.getPendienteTrasladar().equals(ConstantesBD.acronimoNo)&&forma.getSalida().equals(ConstantesBD.acronimoNo))
        	condiciones += ConstantesBD.codigoEstadoCamaOcupada;
        else if((forma.getOcupadas().equals(ConstantesBD.acronimoNo))&&forma.getPendienteTrasladar().equals(ConstantesBD.acronimoSi)&&forma.getSalida().equals(ConstantesBD.acronimoNo))
        	condiciones += ConstantesBD.codigoEstadoCamaPendientePorTrasladar;
        else if((forma.getOcupadas().equals(ConstantesBD.acronimoNo))&&forma.getPendienteTrasladar().equals(ConstantesBD.acronimoNo)&&forma.getSalida().equals(ConstantesBD.acronimoSi))
        	condiciones += ConstantesBD.codigoEstadoCamaConSalida;
        else if((forma.getOcupadas().equals(ConstantesBD.acronimoSi))&&forma.getPendienteTrasladar().equals(ConstantesBD.acronimoSi)&&forma.getSalida().equals(ConstantesBD.acronimoNo))
        	condiciones += ConstantesBD.codigoEstadoCamaOcupada +","+ ConstantesBD.codigoEstadoCamaPendientePorTrasladar;
        else if((forma.getOcupadas().equals(ConstantesBD.acronimoSi))&&forma.getPendienteTrasladar().equals(ConstantesBD.acronimoNo)&&forma.getSalida().equals(ConstantesBD.acronimoSi))
        	condiciones += ConstantesBD.codigoEstadoCamaOcupada +","+ ConstantesBD.codigoEstadoCamaConSalida;
        else if((forma.getOcupadas().equals(ConstantesBD.acronimoNo))&&forma.getPendienteTrasladar().equals(ConstantesBD.acronimoSi)&&forma.getSalida().equals(ConstantesBD.acronimoSi))
        	condiciones += ConstantesBD.codigoEstadoCamaPendientePorTrasladar +","+ ConstantesBD.codigoEstadoCamaConSalida;
       
        String newQuery=comp.obtenerQueryDataSet().replace("-1", condiciones);
        //Se modifica el query
        comp.modificarQueryDataSet(newQuery);
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        //Se mandan los parámetros al reporte
        newPathReport += "&centro_atencion="+forma.getCodigoCentroAtencion();
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        UtilidadBD.closeConnection(con);
        return mapping.findForward("principal");
	}

	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @param institucion
	 */
	private ActionForward generarArchivoPlano(Connection con, ListadoCamasHospitalizacionForm forma, ListadoCamasHospitalizacion mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion)
	{
		HashMap listadoCamas = new HashMap();
		boolean archivo = false;
		int codInstitucion = Utilidades.convertirAEntero(institucion.getCodigo()+"");
		String nombreReporte = "listadoMercadeoCamas", nombre = "", condiciones="";
		StringBuffer datos = new StringBuffer();
		UtilidadBD.iniciarTransaccion(con);

		if((forma.getOcupadas().equals(ConstantesBD.acronimoSi))&&forma.getPendienteTrasladar().equals(ConstantesBD.acronimoSi)&&forma.getSalida().equals(ConstantesBD.acronimoSi))
        	condiciones += ConstantesBD.codigoEstadoCamaOcupada +","+ ConstantesBD.codigoEstadoCamaPendientePorTrasladar +","+ ConstantesBD.codigoEstadoCamaConSalida;
        else if((forma.getOcupadas().equals(ConstantesBD.acronimoSi))&&forma.getPendienteTrasladar().equals(ConstantesBD.acronimoNo)&&forma.getSalida().equals(ConstantesBD.acronimoNo))
        	condiciones += ConstantesBD.codigoEstadoCamaOcupada;
        else if((forma.getOcupadas().equals(ConstantesBD.acronimoNo))&&forma.getPendienteTrasladar().equals(ConstantesBD.acronimoSi)&&forma.getSalida().equals(ConstantesBD.acronimoNo))
        	condiciones += ConstantesBD.codigoEstadoCamaPendientePorTrasladar;
        else if((forma.getOcupadas().equals(ConstantesBD.acronimoNo))&&forma.getPendienteTrasladar().equals(ConstantesBD.acronimoNo)&&forma.getSalida().equals(ConstantesBD.acronimoSi))
        	condiciones += ConstantesBD.codigoEstadoCamaConSalida;
        else if((forma.getOcupadas().equals(ConstantesBD.acronimoSi))&&forma.getPendienteTrasladar().equals(ConstantesBD.acronimoSi)&&forma.getSalida().equals(ConstantesBD.acronimoNo))
        	condiciones += ConstantesBD.codigoEstadoCamaOcupada +","+ ConstantesBD.codigoEstadoCamaPendientePorTrasladar;
        else if((forma.getOcupadas().equals(ConstantesBD.acronimoSi))&&forma.getPendienteTrasladar().equals(ConstantesBD.acronimoNo)&&forma.getSalida().equals(ConstantesBD.acronimoSi))
        	condiciones += ConstantesBD.codigoEstadoCamaOcupada +","+ ConstantesBD.codigoEstadoCamaConSalida;
        else if((forma.getOcupadas().equals(ConstantesBD.acronimoNo))&&forma.getPendienteTrasladar().equals(ConstantesBD.acronimoSi)&&forma.getSalida().equals(ConstantesBD.acronimoSi))
        	condiciones += ConstantesBD.codigoEstadoCamaPendientePorTrasladar +","+ ConstantesBD.codigoEstadoCamaConSalida;

		String encabezado = "Convenio, Ocupación, Porcentaje";
		
		llenarMundo(mundo, forma);
		listadoCamas = mundo.listadoCamasHospitalizacion(con, mundo, condiciones);
	    
	    //Validar si la consulta arrojo resultados
	    if(Utilidades.convertirAEntero(listadoCamas.get("numRegistros")+"") > 0)
	    {
	    	
	    	//Cargamos en path la ruta definida en parametros generales para validar si esta ruta no esta vacia
			String path = ValoresPorDefecto.getArchivosPlanosReportes(codInstitucion);
			logger.info("====>Path: "+path);
			//Validamos si el path esta vacio o lleno
	    	if(UtilidadTexto.isEmpty(path))
			{
	    		forma.setMensaje(new ResultadoBoolean(true, ""));
	    		forma.setArchivo(false);
		    	forma.setZip(false);
				UtilidadBD.abortarTransaccion(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Facturación", "error.facturacion.rutaNoDefinida", true);
			}
	    	else
	    	{
	    		//Cargamos el path original. La ruta establcecida en el web.xml concatenada con la ruta establecida en el parametro general
	    		path = ValoresPorDefecto.getReportPath()+ValoresPorDefecto.getArchivosPlanosReportes(codInstitucion);
	    		//Generar el Nombre del Archivo
	    		nombre = util.CsvFile.armarNombreArchivo(nombreReporte, usuario);
	    		//Cargamos los datos de la consulta en un StringBuffer para ser impreso en el archivo plano
	    		datos = mundo.cargarMapaMercadeo(listadoCamas, nombreReporte, usuario.getLoginUsuario(), encabezado);
	    		logger.info("===>Nombre Archivo: "+nombre);
	    		logger.info("===>Path Final Archivo: "+path);
	    		archivo = util.TxtFile.generarTxt(datos, nombre, path, ".csv");
	    		//Validamos si el archivo si se creo
	    		if(archivo)
			    {
	    			//Validamos si se genero el .zip o no?. Con la intención de indicarle al usuario que el zip no se genero
	    			if(BackUpBaseDatos.EjecutarComandoSO("zip  -j "+path+nombre+".zip "+path+nombre+".csv") != ConstantesBD.codigoNuncaValido)
		    		{
		    			forma.setPathArchivoTxt(ValoresPorDefecto.getReportUrl()+ValoresPorDefecto.getArchivosPlanosReportes(codInstitucion)+nombre+".zip");
		    			logger.info("===>Path Archivo: "+forma.getPathArchivoTxt());
			    		forma.setMensaje(new ResultadoBoolean(true,"ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+path+nombre+".csv"+"!!!!!"));
			    		forma.setArchivo(true);
			    		forma.setZip(true);
			    		UtilidadBD.finalizarTransaccion(con);
			    		UtilidadBD.closeConnection(con);
			    		return mapping.findForward("principal");
		    		}
		    		else
		    		{
		    			forma.setMensaje(new ResultadoBoolean(true,"ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+path+nombre+".csv"+"!!!!!"));
		    			forma.setArchivo(true);
				    	forma.setZip(false);
				    	UtilidadBD.finalizarTransaccion(con);
				    	UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
		    		}
			    }
	    		else
			    {
			    	forma.setMensaje(new ResultadoBoolean(true,"INCONSISTENCIAS EN LA GENERACION DEL ARCHIVO PLANO \n"+path+nombre+".csv"+"!!!!!"));
			    	forma.setArchivo(false);
			    	forma.setZip(false);
			    	UtilidadBD.abortarTransaccion(con);
			    	UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
			    }
	    	}
	    }
	    else
	    {
	    	forma.setMensaje(new ResultadoBoolean(true,"SIN RESULTADOS PARA GENERAR EL ARCHIVO PLANO!!!!!"));
	    	forma.setArchivo(false);
	    	forma.setZip(false);
	    	UtilidadBD.abortarTransaccion(con);
	    	UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
	    }
	}
	
	/**
	 * @param mundo
	 * @param forma
	 */
	private void llenarMundo(ListadoCamasHospitalizacion mundo, ListadoCamasHospitalizacionForm forma) 
	{
		mundo.setCentroAtencion(forma.getCodigoCentroAtencion());
		mundo.setEstadoCama(forma.getEstadoCama());
		mundo.setOcupadas(forma.getOcupadas());
		mundo.setPendienteTrasladar(forma.getPendienteTrasladar());
		mundo.setSalida(forma.getSalida());
	}
}