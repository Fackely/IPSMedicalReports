package com.princetonsa.action.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
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

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.PacientesConAtencionForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.RevisionCuenta;
import com.princetonsa.mundo.manejoPaciente.PacientesConAtencion;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public class PacientesConAtencionAction extends Action
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(PacientesConAtencionAction.class);

	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if (form instanceof PacientesConAtencionForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				PacientesConAtencionForm forma = (PacientesConAtencionForm) form;
				PacientesConAtencion mundo = new PacientesConAtencion();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				String estado = forma.getEstado();
				logger.warn("[PacientesConAtencion]--->Estado: "+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo estado is NULL");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					return accionEmpezar(con, forma, mundo, usuario, mapping);
				}
				else if(estado.equals("recargar"))
				{
					return this.accionRecargar(con, forma, mapping, mundo);
				}
				else if(estado.equals("buscar"))
				{
					return this.accionConsultarPacientesConAtencion(con, forma, mundo, usuario, mapping, request, institucion);
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de PacientesHospitalizadosForm");
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
	 * Metodo que inicializa los datos y los criterios de busqueda inicial
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, PacientesConAtencionForm forma, PacientesConAtencion mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.reset();
		//Cargamos el select con todos los centros de atencion
		forma.setCentroAtencion(Utilidades.obtenerCentrosAtencion(usuario.getCodigoInstitucionInt()));
		//Se selecciona el Centro de Atencion de Sesion
		forma.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion()+"");
		//Cargamos el select con todos los convenios
		forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", false, "", true));
		//Cargamos el select con todas las vias ingreso
		forma.setViasIngresos(Utilidades.obtenerViasIngreso(con,""));
		//Cargamos el select con todos los tipos de solicitud
		forma.setTiposSolicitudes(RevisionCuenta.consultarTiposSolicitud(con));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Metodo encargado de cargar el select tipo paciente
	 * segun lo seleccionado en el select via ingreso 
	 * @param con
	 * @param forma
	 * @param mapping 
	 * @param mundo 
	 * @return
	 */
	private ActionForward accionRecargar(Connection con, PacientesConAtencionForm forma, ActionMapping mapping, PacientesConAtencion mundo)
	{
		ArrayList tiposPaciente = new ArrayList();
		if(!forma.getViaIngreso().equals("") && !forma.getViaIngreso().equals("null"))
		{
			tiposPaciente = Utilidades.obtenerTiposPacientePorViaIngreso(con, forma.getViaIngreso());
			forma.setTiposPaciente(tiposPaciente);
		}
		//Reseteamos el select de tipo paciente
		else
			forma.setTiposPaciente(tiposPaciente);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Metodo encargado de consultar los pacientes
	 * hospitalizados con atencion por convenio y 
	 * valor
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @param institucion 
	 * @return
	 */
	private ActionForward accionConsultarPacientesConAtencion(Connection con, PacientesConAtencionForm forma, PacientesConAtencion mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, InstitucionBasica institucion)
	{
		if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			return this.generarReporte(con, forma, mundo, usuario, request, mapping);
		else if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
			return this.generarArchivoPlano(con, forma, mundo, usuario, request, mapping, institucion);
		
		UtilidadBD.closeConnection(con);
    	return mapping.findForward("principal");
	}

	/**
	 * Metodo que permite exportar los resultados de la consulta a PDF
	 * @param con
	 * @param forma
	 * @param mundo 
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @param institucion
	 * @return
	 */
	private ActionForward generarArchivoPlano(Connection con, PacientesConAtencionForm forma, PacientesConAtencion mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion)
	{
		StringBuffer datosArchivos = new StringBuffer();
		String extCsv = ".csv", extZip = ".zip" ;
		String encabezado = "Habitación, Paciente, Diagnóstico, ID, HC, Ingreso, Fecha/Hora Ingreso, Convenio, Total Cuenta, Total Facturado, Saldo Total por Facturar";
		String[] hora = UtilidadFecha.getHoraActual().split(":");
		String periodo = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"-"+hora[0]+hora[1];
		//Llenamos el mapa con los datos arrojados por la consulta
		forma.setBusquedaPacientesConAtencion(mundo.consultarPacientesConAtencion(con, forma));
		//Iniciamos Transaccion
		UtilidadBD.iniciarTransaccion(con);
		if(Utilidades.convertirAEntero(forma.getBusquedaPacientesConAtencion("numRegistros")+"") > 0)
		{
			//Cargamos en path la ruta definida en parametros generales para validar si esta ruta no esta vacia
			String path = ValoresPorDefecto.getArchivosPlanosReportes(usuario.getCodigoInstitucionInt());
			logger.info("====>Path: "+path);
			//Validamos si el path esta vacio o lleno
	    	if(UtilidadTexto.isEmpty(path))
			{
	    		forma.setMensaje(new ResultadoBoolean(true, ""));
	    		forma.setArchivo(false);
		    	forma.setZip(false);
		    	UtilidadBD.abortarTransaccion(con);
		    	UtilidadBD.closeConnection(con);
		    	return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Manejo Paciente", "error.manejoPacientes.rutaNoDefinida", true);
			}
			else
			{
				//Cargamos el path original. La ruta establecida en el web.xml concatenada con la ruta establecida en el parametro general
	    		path = ValoresPorDefecto.getReportPath()+ValoresPorDefecto.getArchivosPlanosReportes(usuario.getCodigoInstitucionInt());
	    		//Generar el Nombre del Archivo
	    		String nombre = util.TxtFile.armarNombreArchivoConPeriodo("pacienteConAtencionXConvenioYValor", periodo, usuario.getLoginUsuario());
	    		datosArchivos = mundo.cargarMapaPacientesConAtencion(forma.getBusquedaPacientesConAtencion(), "Pacientes con Atención por Convenio y Valor", encabezado, usuario.getLoginUsuario());
				boolean archivo = util.TxtFile.generarTxt(datosArchivos, nombre, path, extCsv);
				//Validamos si el archivo plano se creo
				if(archivo)
			    {
					//Validamos si el .zip del archivo plano si se ejecuto
					if(BackUpBaseDatos.EjecutarComandoSO("zip -j "+path+nombre+extZip+" "+path+nombre+extCsv) != ConstantesBD.codigoNuncaValido)
		    		{
						forma.setPathArchivoTxt(ValoresPorDefecto.getReportUrl()+ValoresPorDefecto.getArchivosPlanosReportes(usuario.getCodigoInstitucionInt())+nombre+extZip);
			    		forma.setMensaje(new ResultadoBoolean(true, "ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+path+nombre+extCsv+"!!!!!"));
			    		forma.setArchivo(true);
			    		forma.setZip(true);
			    		logger.info("===>Se genero el ZIP..."+forma.isZip());
			    		logger.info("===>Path definitivo..."+forma.getPathArchivoTxt());
			    		UtilidadBD.finalizarTransaccion(con);
			    		UtilidadBD.closeConnection(con);
			    		return mapping.findForward("principal");
		    		}
		    		else
		    		{
		    			forma.setMensaje(new ResultadoBoolean(true, "ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+path+nombre+extCsv+"!!!!!"));
		    			forma.setArchivo(true);
				    	forma.setZip(false);
				    	logger.info("===>No se genero el ZIP..."+forma.isZip());
				    	UtilidadBD.finalizarTransaccion(con);
				    	UtilidadBD.closeConnection(con);
				    	return mapping.findForward("principal");
		    		}
			    }
			    else
			    {
			    	forma.setMensaje(new ResultadoBoolean(true, "INCONSISTENCIAS EN LA GENERACIÓN DEL ARCHIVO PLANO \n"+path+nombre+extCsv+"!!!!!"));
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
	    	forma.setMensaje(new ResultadoBoolean(true, "SIN RESULTADOS PARA GENERAR EL ARCHIVO PLANO!!!!!"));
	    	forma.setArchivo(false);
	    	forma.setZip(false);
	    	UtilidadBD.finalizarTransaccion(con);
	    	UtilidadBD.closeConnection(con);
	    	return mapping.findForward("principal");
	    }
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @return
	 */
	private ActionForward generarReporte(Connection con, PacientesConAtencionForm forma, PacientesConAtencion mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping)
	{
		String nombreRptDesign = "PacientesConAtencion.rptdesign", parametros = "", condiciones = "";
		HashMap<String, Object> tiposSolicitudes = new HashMap<String, Object>();
		tiposSolicitudes = mundo.tiposSolicitudesEscogidas(con, forma);
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		//Informacion del Cabezote
        DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"manejoPaciente/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(institucion.getRazonSocial());
        
        if(Utilidades.convertirAEntero(institucion.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+". "+institucion.getNit()+" - "+institucion.getDigitoVerificacion());
        else
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+". "+institucion.getNit());
        
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfHeader(1,1, "PACIENTES CON ATENCIÓN POR CONVENIO Y VALOR");
        
        //Parámetros de Búsqueda
        parametros = "Centro Atención ["+Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getCodigoCentroAtencion()))+"], ";
        parametros += "Convenio ["+Utilidades.obtenerNombreConvenioOriginal(con, Utilidades.convertirAEntero(forma.getConvenioSeleccionado()))+"], ";
        parametros += "Vía Ingreso ["+Utilidades.obtenerNombreViaIngreso(con, Utilidades.convertirAEntero(forma.getViaIngreso()))+"], ";
        parametros += "Tipo Paciente ["+Utilidades.obtenerNombreTipoPaciente(con, forma.getTipoPaciente())+"], ";
        if(UtilidadCadena.noEsVacio(tiposSolicitudes.get("nombreSolicitudes")+""))
        	parametros += "Tipos Solicitud ["+tiposSolicitudes.get("nombreSolicitudes")+"]";
        else
        	parametros += "Tipos Solicitud [Todos]";
        comp.insertLabelInGridPpalOfHeader(2, 0, parametros);
        
        //Informacion Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
        
        comp.obtenerComponentesDataSet("PacientesConAtencion");
        //Filtramos el estado del ingreso por abierto
        condiciones += "i.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' ";
        //Filtramos el estado de la cuenta
        condiciones += "AND cu.estado_cuenta IN ("+ConstantesBD.codigoEstadoCuentaActiva+", "+ConstantesBD.codigoEstadoCuentaFacturadaParcial+", "+ConstantesBD.codigoEstadoCuentaAsociada+") ";
        //Filtramos el esatdo del egreso
        condiciones += "AND getexisteegreso(coalesce(getcuentafinalasocioint(i.id, cu.id), cu.id)) = "+ValoresPorDefecto.getValorFalseParaConsultas()+" ";
        //Filtramos que la cuenta no este en estado cerrada
        condiciones += "AND cu.estado_cuenta NOT IN ("+ConstantesBD.codigoEstadoCuentaCerrada+") ";
        //Filtramos la consulta por el centro de atencion. Como es requerido no se valida
		condiciones += "AND cc.centro_atencion = "+forma.getCodigoCentroAtencion()+" ";
		//Filtramos la consulta por el convenio seleccionado
		condiciones += "AND sc.convenio = "+forma.getConvenioSeleccionado()+" ";
		//Filtramos la consulta por la via de ingreso. Como es requerido no se valida
		condiciones += "AND cu.via_ingreso = "+forma.getViaIngreso()+" ";
		//Filtramos la consulta por el tipo de paciente. Como es requerido no se valida
		condiciones += "AND cu.tipo_paciente = '"+forma.getTipoPaciente()+"' ";
		//Filtramos la consulta por los tipos de solicitud escogidos. Como no es requerido se debe validar
		if(UtilidadCadena.noEsVacio(tiposSolicitudes.get("codigoSolicitudes")+""))
			condiciones += "AND s.tipo IN("+tiposSolicitudes.get("codigoSolicitudes")+")";
        
        /*String newQuery = comp.obtenerQueryDataSet().replace("-1", tiposSolicitudes.get("codigoSolicitudes")+"");
        comp.modificarQueryDataSet(newQuery);*/
		
        String newQuery = comp.obtenerQueryDataSet().replace("1=2", condiciones);
        comp.modificarQueryDataSet(newQuery);
        newQuery = comp.obtenerQueryDataSet().replace("'campo1'", "getTotalConvenioIngreso(i.id, c.codigo, 'S', s.tipo, "+forma.getViaIngreso()+", '"+forma.getTipoPaciente()+"')");
        comp.modificarQueryDataSet(newQuery);
        newQuery = comp.obtenerQueryDataSet().replace("'campo2'", "getTotalConvenioIngreso(i.id, c.codigo, 'N', s.tipo, "+forma.getViaIngreso()+", '"+forma.getTipoPaciente()+"')");
        comp.modificarQueryDataSet(newQuery);
        logger.info("===>Consulta en el BIRT con Condiciones: "+newQuery);
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
	
}