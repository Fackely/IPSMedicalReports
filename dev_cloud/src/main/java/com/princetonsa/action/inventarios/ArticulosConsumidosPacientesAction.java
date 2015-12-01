package com.princetonsa.action.inventarios;

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
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.inventarios.ArticulosConsumidosPacientesForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.ArticulosConsumidosPacientes;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Mauricio Jaramillo
 */

public class ArticulosConsumidosPacientesAction extends Action 
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger =Logger.getLogger(ArticulosConsumidosPacientesAction.class);
	
	/**
	 * Metodo execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if (form instanceof ArticulosConsumidosPacientesForm) 
			{
				ArticulosConsumidosPacientesForm forma=(ArticulosConsumidosPacientesForm) form;
				ArticulosConsumidosPacientes mundo= new ArticulosConsumidosPacientes();
				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuarioActual=Utilidades.getUsuarioBasicoSesion(request.getSession());
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ArticulosConsumidosPacientesAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset(usuarioActual.getCodigoCentroAtencion()+"",usuarioActual.getCodigoInstitucion());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("generar"))
				{
					return accionConsultarArticulosConsumidos(con, forma, mundo, usuarioActual, mapping, request, institucion);
				}
				else if(estado.equals("consultaAlmacenes"))
				{
					forma.setAlmacen("");
					return mapping.findForward("principal");
				}
				else if(estado.equals("nuevoArticulo"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else
				{
					forma.reset(usuarioActual.getCodigoCentroAtencion()+"",usuarioActual.getCodigoInstitucion());
					logger.warn("Estado no valido dentro del flujo de ARTICULOS CONSUMIDOS PACIENTES ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de ArticulosConsumidosPacientesForm");
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
	 * Metodo que sirve para seleccionar el debido tipo de salida
	 * seleccionado por el usuario
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuarioActual
	 * @param mapping
	 * @param request
	 * @param institucion
	 * @return
	 */
	private ActionForward accionConsultarArticulosConsumidos(Connection con, ArticulosConsumidosPacientesForm forma, ArticulosConsumidosPacientes mundo, UsuarioBasico usuarioActual, ActionMapping mapping, HttpServletRequest request, InstitucionBasica institucion)
	{
		if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
			return this.generarReporte(con, forma, mundo, usuarioActual, request, mapping, institucion);
		else if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
			return this.generarArchivoPlano(con, forma, mundo, usuarioActual, request, mapping, institucion);
		
		return null;
	}

	/**
	 * Metodo que sirve para generar en PDF el tipo de Formato
	 * establecido por el usuario
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuarioActual
	 * @param request
	 * @param mapping
	 * @param institucion
	 * @return
	 */
	private ActionForward generarReporte(Connection con, ArticulosConsumidosPacientesForm forma, ArticulosConsumidosPacientes mundo, UsuarioBasico usuarioActual, HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion)
	{
		HashMap<String, Object> CondicionesParametros = new HashMap<String, Object>();
		//Informacion del Cabezote
        DesignEngineApi comp;
        String nombreRptDesign = "", dataSet = "";
        
        //Se evalua que tipo de reporte es llamado por el usuario
        if(forma.getTipoInforme().equals(ConstantesIntegridadDominio.acronimoConsumoPacienteArticulo))
        {
        	nombreRptDesign = "ArticulosConsumoPacientes.rptdesign";
        	dataSet = "ArticulosConsumidosPacientes";
        }
        else if(forma.getTipoInforme().equals(ConstantesIntegridadDominio.acronimoTotalesConsumoArticulo))
        {
        	nombreRptDesign = "ArticulosTotalesConsumo.rptdesign";
        	dataSet = "ArticulosTotalesConsumo";
        }
        
        logger.info("===>Reporte: "+nombreRptDesign+" ===>DataSet: "+dataSet);
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v = new Vector();
        v.add(institucion.getRazonSocial());
        
        if(Utilidades.convertirAEntero(institucion.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+". "+institucion.getNit()+" - "+institucion.getDigitoVerificacion());
        else
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+". "+institucion.getNit());
        
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0, 1, v);
        comp.insertLabelInGridPpalOfHeader(1, 1, "ARTICULOS CONSUMIDOS POR PACIENTES");
        comp.obtenerComponentesDataSet(dataSet);
        
        //Consultamos la condiciones seleccionadas para la consulta de movimiento deudor
        CondicionesParametros = mundo.consultarCondicionesArticulosConsumidos(con, forma, usuarioActual);
        
        //Insertamos los parametros de busqueda seleccionados por el usuario
        comp.insertLabelInGridPpalOfHeader(2, 0, CondicionesParametros.get("parametros")+"");
        
        logger.info("===>Tipo Codigo Articulo: "+forma.getTipoCodigoArticulo());
        String newQuery = CondicionesParametros.get("consulta").toString().replace("?", "'"+forma.getTipoCodigoArticulo()+"'");
        logger.info("===>Consulta en el BIRT con Condiciones: "+newQuery);
        
        //Se modifica el query
        comp.modificarQueryDataSet(newQuery);
        
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
	 * Metodo utilizado para generar el archivo plano con extension
	 * CVS para la funcionalidad de Articulos Consumidos Pacientes
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuarioActual
	 * @param request
	 * @param mapping
	 * @param institucion
	 * @return
	 */
	private ActionForward generarArchivoPlano(Connection con, ArticulosConsumidosPacientesForm forma, ArticulosConsumidosPacientes mundo, UsuarioBasico usuarioActual, HttpServletRequest request, ActionMapping mapping, InstitucionBasica institucion)
	{
		StringBuffer datosArchivos = new StringBuffer();
		String extCsv = ".csv", extZip = ".zip", encabezado = "", periodo = "", nombreReporte = "", nombreArchivo = "" ;
		
		//Llenamos el mapa con los datos arrojados por la consulta
		forma.setArticulosConsumidos(mundo.consultarArticulosConsumidos(con, forma, usuarioActual));
		periodo = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"-"+UtilidadFecha.getHoraActual();
		
		/*Segun el tipo de reporte, se debe generar un encabezado diferente, un nombre de archivo
		un nombre de reporte y una cargada de datos diferente; ya que todos tienen estructuras distintas*/
		if(forma.getTipoInforme().equals(ConstantesIntegridadDominio.acronimoConsumoPacienteArticulo))
		{
			encabezado = "Fecha/Hora, Codigo, Descripcion, Unidad, No. Ingreso, Id. Paciente, Paciente, Id. Entidad, Nombre Entidad, Cantidad";
			nombreReporte = "Consumo Paciente por Articulo e Ingreso";
			nombreArchivo = "consumosPacienteXArticuloIngreso";
			datosArchivos = mundo.cargarMapaConsumoPacienteArticulo(forma.getArticulosConsumidos(), nombreReporte, encabezado, usuarioActual.getLoginUsuario());
		}
		else if(forma.getTipoInforme().equals(ConstantesIntegridadDominio.acronimoTotalesConsumoArticulo))
		{
			
		}
		
		//Iniciamos Transaccion
		UtilidadBD.iniciarTransaccion(con);
		if(Utilidades.convertirAEntero(forma.getArticulosConsumidos("numRegistros")+"") > 0)
		{
			//Cargamos en path la ruta definida en parametros generales para validar si esta ruta no esta vacia
			String path = ValoresPorDefecto.getArchivosPlanosReportesInventarios(usuarioActual.getCodigoInstitucionInt());
			logger.info("====>Path Valor por Defecto: "+path);
			//Validamos si el path esta vacio o lleno
	    	if(UtilidadTexto.isEmpty(path))
			{
	    		forma.setMensaje(new ResultadoBoolean(true, ""));
	    		forma.setArchivo(false);
		    	forma.setZip(false);
		    	UtilidadBD.abortarTransaccion(con);
		    	UtilidadBD.closeConnection(con);
		    	return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Inventarios", "error.inventarios.rutaNoDefinida", true);
			}
			else
			{
				//Cargamos el path original. La ruta establecida en el web.xml concatenada con la ruta establecida en el parametro general
	    		path = ValoresPorDefecto.getReportPath()+ValoresPorDefecto.getArchivosPlanosReportesInventarios(usuarioActual.getCodigoInstitucionInt());
	    		//Generar el Nombre del Archivo
	    		String nombre = util.TxtFile.armarNombreArchivoConPeriodo(nombreArchivo, periodo, usuarioActual.getLoginUsuario());
	    		logger.info("====>Path Definitivo: "+path);
	    		logger.info("====>Nombre Archivo: "+nombre);
	    		boolean archivo = util.TxtFile.generarTxt(datosArchivos, nombre, path, extCsv);
				//Validamos si el archivo plano se creo
				if(archivo)
			    {
					//Validamos si el .zip del archivo plano si se ejecuto
					if(BackUpBaseDatos.EjecutarComandoSO("zip -j "+path+nombre+extZip+" "+path+nombre+extCsv) != ConstantesBD.codigoNuncaValido)
		    		{
						logger.info("====>Se genero el archivo ZIP");
						forma.setPathArchivoTxt(ValoresPorDefecto.getReportUrl()+ValoresPorDefecto.getArchivosPlanosReportesInventarios(usuarioActual.getCodigoInstitucionInt())+nombre+extZip);
			    		forma.setMensaje(new ResultadoBoolean(true, "ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+path+nombre+extCsv+"!!!!!"));
			    		forma.setArchivo(true);
			    		forma.setZip(true);
			    		UtilidadBD.finalizarTransaccion(con);
			    		UtilidadBD.closeConnection(con);
			    		return mapping.findForward("principal");
		    		}
		    		else
		    		{
		    			logger.info("====>No se genero el archivo ZIP");
		    			forma.setMensaje(new ResultadoBoolean(true, "ARCHIVO PLANO GENERADO CORRECTAMENTE EN LA RUTA \n"+path+nombre+extCsv+"!!!!!"));
		    			forma.setArchivo(true);
				    	forma.setZip(false);
				    	UtilidadBD.finalizarTransaccion(con);
				    	UtilidadBD.closeConnection(con);
				    	return mapping.findForward("principal");
		    		}
			    }
			    else
			    {
			    	forma.setMensaje(new ResultadoBoolean(true, "INCONSISTENCIAS EN LA GENERACION DEL ARCHIVO PLANO \n"+path+nombre+extCsv+"!!!!!"));
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
	    	return mapping.findForward("principal");
	    }
	}
	
}