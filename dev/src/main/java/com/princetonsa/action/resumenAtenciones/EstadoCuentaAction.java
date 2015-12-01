/*
 * @(#)EstadoCuentaAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action.resumenAtenciones;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
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
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import util.ConstantesBD;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.reportes.ConsultasBirt;
import util.salas.UtilidadesSalas;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.resumenAtenciones.EstadoCuentaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.facturacion.RevisionCuenta;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.resumenAtenciones.EstadoCuenta;
import com.princetonsa.mundo.salasCirugia.LiquidacionServicios;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Clase encargada del control de la funcionalidad de que muestra el estado de
 * cuenta
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 5/08/2004
 */
public class EstadoCuentaAction extends Action 
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action de Estado de
	 * Cuenta
	 */
	private Logger logger= Logger.getLogger(EstadoCuentaAction.class);

	/**
	 * Método estándar para las clases de tipo Action, el cual define el modelo
	 * de control de esta funcionalidad. A su vez este método llama a métodos
	 * particulares, cada uno de los cuales responde a cada petición del usuario
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if(form instanceof EstadoCuentaForm)
			{
				if(response == null); /**Para evitar que salga el warning**/
				if(logger.isDebugEnabled())
				{
					logger.debug("Entro al Action de Estado de Cuenta");
				}
				EstadoCuentaForm estadoCuentaForm= (EstadoCuentaForm) form;
				String estado= estadoCuentaForm.getEstado();
				logger.warn("EstadoCuentaAction estado = >"+estado);


				try
				{
					String tipoBD= System.getProperty("TIPOBD");
					DaoFactory myFactory= DaoFactory.getDaoFactory(tipoBD);
					con= myFactory.getConnection();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					/**No se cierra conexión porque si llega aca ocurrió un	error al abrirla**/
					logger.error("Problemas abriendo la conexión en EstadoCuentaAction");
					request.setAttribute("codigoDescripcionError","errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				/**Lo primero que vamos a hacer es validar que se cumplan las condiciones.**/

				HttpSession session= request.getSession();
				UsuarioBasico medico= (UsuarioBasico) session.getAttribute("usuarioBasico");
				PersonaBasica paciente= (PersonaBasica) session.getAttribute("pacienteActivo");
				EstadoCuenta estadoCuenta= new EstadoCuenta();
				/**Primera Condición: El usuario debe existir**/
				if(medico == null)
				{
					return ComunAction.accionSalirCasoError(mapping, request, con,logger, "No existe el usuario","errors.usuario.noCargado", true);
				}
				else if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
				{
					/**Segunda Condición: Debe haber un paciente cargado**/
					return ComunAction.accionSalirCasoError(mapping, request, con,logger, "paciente null o sin id","errors.paciente.noCargado", true);
				}
				else if(estado == null || estado.equals(""))
				{
					return ComunAction.accionSalirCasoError(mapping,request,con,logger,"La accion a finalizar no esta definida (EstadoCuentaAction)","errors.estadoInvalido", true);
				}
				//*********ESTADO QUE REALIZA LA CONSULTA DE LAS CUENTAS DEL PACIENTE**********************
				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(mapping,  estadoCuentaForm,con, paciente, estadoCuenta,medico);
				}
				else if (estado.equals("volverListadoCuentas"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoCuentas");
				}
				//*********ESTADO QUE REALIZA LA CONSULTA DE LOS CONVENIOS DEL INGRESO SELECCIONADO**********************
				else if (estado.equals("volverListadoConvenios"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoConvenios");
				}
				else if(estado.equals("listadoConvenios"))
				{
					return this.accionListadoConvenios(con,estadoCuentaForm,mapping,estadoCuenta,medico,paciente);
				}
				else if(estado.equals("conversionMoneda"))
				{
					return this.accionListadoSolicitudes(con,estadoCuentaForm,mapping,estadoCuenta,medico,paciente);
				}
				//********ESTADOS DE LA SECCION DEL LISTADO DE SOLICITUDES x CONVENIO***********************************
				else if (estado.equals("listadoSolicitudes"))
				{
					return this.accionListadoSolicitudes(con,estadoCuentaForm,mapping,estadoCuenta,medico,paciente);
				}
				else if (estado.equals("volverListadoSolicitudes"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoSolicitudes");
				}
				else if (estado.equals("detalleSolicitud"))
				{
					return this.accionDetalleSolicitud(con,estadoCuentaForm,mapping,estadoCuenta,request,medico);
				}
				else if (estado.equals("redireccion"))
				{
					return this.accionRedireccion(con,estadoCuentaForm,response,request,mapping,estadoCuenta,medico);

				}
				else if (estado.equals("ordenar"))
				{
					return this.accionOrdenar(con,estadoCuentaForm,mapping,estadoCuenta,medico);
				}
				else if (estado.equals("buscar")) //Se realiza la busqueda avanzada
				{
					return accionBuscar(con,estadoCuentaForm,mapping,estadoCuenta,medico);
				}
				else if (estado.equals("imprimirDetSolCue"))
				{
					return accionImprimirDetSolCue(con,estadoCuentaForm,estadoCuenta,mapping,request,medico,paciente,1);
				}
				else if (estado.equals("imprimirResSolCue"))
				{
					return accionImprimirResSolCue(con,estadoCuentaForm,estadoCuenta,mapping,request,medico,paciente,1);
				}
				else if (estado.equals("imprimirDetSolCueF"))
				{
					return accionImprimirDetSolCue(con,estadoCuentaForm,estadoCuenta,mapping,request,medico,paciente,2);
				}
				else if (estado.equals("imprimirResSolCueF"))
				{
					return accionImprimirResSolCue(con,estadoCuentaForm,estadoCuenta,mapping,request,medico,paciente,2);
				}
				else if (estado.equals("imprimirDetSolCon"))
				{
					return accionImprimirDetSolCon(con,estadoCuentaForm,estadoCuenta,mapping,request,medico,paciente,1);
				}
				else if (estado.equals("imprimirDetSolSol"))
				{
					return accionImprimirDetSolCon(con,estadoCuentaForm,estadoCuenta,mapping,request,medico,paciente,1);
				}
				else if (estado.equals("imprimirDetSolDetSol"))
				{
					return accionImprimirDetSolCon(con,estadoCuentaForm,estadoCuenta,mapping,request,medico,paciente,1);
				}
				else if (estado.equals("imprimirDetSolConF"))
				{
					return accionImprimirDetSolCon(con,estadoCuentaForm,estadoCuenta,mapping,request,medico,paciente,2);
				}
				else if (estado.equals("imprimirResSolCon"))
				{
					return accionImprimirResSolCon(con,estadoCuentaForm,estadoCuenta,mapping,request,medico,paciente,1);
				}
				else if (estado.equals("imprimirResSolSol"))
				{
					return accionImprimirResSolCon(con,estadoCuentaForm,estadoCuenta,mapping,request,medico,paciente,1);
				}
				else if (estado.equals("imprimirResSolDetSol"))
				{
					return accionImprimirResSolCon(con,estadoCuentaForm,estadoCuenta,mapping,request,medico,paciente,1);
				}
				else if (estado.equals("imprimirResSolConF"))
				{
					return accionImprimirResSolCon(con,estadoCuentaForm,estadoCuenta,mapping,request,medico,paciente,2);
				}
				else if (estado.equals("imprimirResumidoItem"))
				{
					return accionImprimirItem(con,estadoCuentaForm,estadoCuenta,mapping,request,medico,paciente,1);
				}
				else if (estado.equals("imprimirResumidoItemSol"))
				{
					return accionImprimirItem(con,estadoCuentaForm,estadoCuenta,mapping,request,medico,paciente,1);
				}
				else if (estado.equals("imprimirResumidoItemDetSol"))
				{
					return accionImprimirItem(con,estadoCuentaForm,estadoCuenta,mapping,request,medico,paciente,1);
				}
				else if (estado.equals("imprimirResumidoItemAF"))
				{
					return accionImprimirItem(con,estadoCuentaForm,estadoCuenta,mapping,request,medico,paciente,2);
				}
				else
				{
					return ComunAction.accionSalirCasoError(mapping,request,con,logger,"La accion a finalizar no esta definida (EstadoCuentaAction)","errors.estadoInvalido", true);
				}

			}
			else
			{
				/**Todavía no existe conexión, por eso no se cierra**/
				request.setAttribute("codigoDescripcionError","errors.formaTipoInvalido");
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
	 * Metodo para imprimir El estado Cuenta detallado por solicitud desde Cuentas
	 * @param con
	 * @param estadoCuentaForm
	 * @param mapping
	 * @param request
	 * @param medico
	 * @param paciente
	 * @return
	 */
	private ActionForward accionImprimirDetSolCue(Connection con, EstadoCuentaForm estadoCuentaForm, EstadoCuenta estadoCuenta, ActionMapping mapping, HttpServletRequest request, UsuarioBasico medico, PersonaBasica paciente, int remite) 
	{
		String oldQuery="";		
    	String nombreRptDesign = "EstadoCuentaDetalladoCuenta.rptdesign";
    	
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
		/*DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+"  -  "+ins.getNit());
        v.add(ins.getDireccion()+" / "+ins.getTelefono());
        v.add("ESTADO DE CUENTA DETALLADO POR SOLICITUD");
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        //Se inserta grilla del encabezado del paciente
        v = new Vector();
        v.add(paciente.getPrimerApellido()+" "+paciente.getSegundoApellido()+" "+paciente.getPrimerNombre()+" "+paciente.getSegundoNombre());
        v.add(paciente.getCodigoTipoIdentificacionPersona()+" "+paciente.getNumeroIdentificacionPersona());
        v.add("N° H.C.  "+paciente.getHistoriaClinica());
        v.add("Edad: "+paciente.getEdadDetallada());
        v.add("Vía Ingreso: "+estadoCuentaForm.getViaIngreso());
        v.add("Centro de Atención: "+estadoCuentaForm.getCentroAtencion());
        v.add("N° Ingreso: "+estadoCuentaForm.getConsecutivoIngreso());
        v.add("Fecha Ingreso: "+estadoCuentaForm.getFechaIngreso());
        
        EstadoCuenta mundo=new EstadoCuenta();
        HashMap<String, Object> mapa = new HashMap<String, Object> ();
        mapa=mundo.fechaEgreso(con, Integer.parseInt(estadoCuentaForm.getIdIngreso()));
        if(Integer.parseInt(mapa.get("numRegistros").toString())>0){
	        if(mapa.get("estado").equals(ConstantesBD.codigoEstadoCuentaFacturada))
	        	v.add("Fecha de Egreso: "+UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fechae").toString()));
	        if(mapa.get("estado").equals(ConstantesBD.codigoEstadoCuentaFacturadaParcial))
	        	v.add("Fecha de Egreso: Factura Parcial");
        }
        
        v.add("Estado Ingreso: "+estadoCuentaForm.getEstadoIngreso());
        if(!estadoCuentaForm.getDescEmpresaSubContratada().equals(""))
        	v.add("Entidad SubContratada: "+estadoCuentaForm.getDescEmpresaSubContratada());        	
        
        v.add("N° Cuenta: "+estadoCuentaForm.getIdCuenta());
        v.add("Estado Cuenta: "+estadoCuentaForm.getEstadoCuenta());
        v.add("Dirección: "+paciente.getDireccion());
        v.add("Teléfono: "+paciente.getTelefono());
        comp.insertLabelInGridOfMasterPageWithProperties(3,0,v,DesignChoiceConstants.TEXT_ALIGN_LEFT);*/
        
		//***************PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        String nombreReporte = "ESTADO DE CUENTA DETALLADO POR SOLICITUD";
        armarEncabezado(comp, con, ins, estadoCuentaForm, request, paciente, nombreReporte);
        //*******************************************************************************************************
		
        comp.obtenerComponentesDataSet("detalleCuentasS");
        oldQuery = ConsultasBirt.consultaImprimirDetsolCueEstadoCuenta(estadoCuentaForm.getIdIngreso(), remite);
        
        logger.info("CONSULTA REPORTEEEE>>>>>>>>>>>>>>>>>>>>"+oldQuery);
        
        comp.modificarQueryDataSet(oldQuery);
        
        //*******TOTALES CONVENIO*************//
        
        estadoCuentaForm.setListadoConvenios(estadoCuenta.cargarTodosConvenioIngreso(con, estadoCuentaForm.getIdIngreso()));
        
        HashMap<String, Object> mapaC = new HashMap<String, Object> ();
        mapaC = estadoCuenta.calcularTotalesPorIngreso(con, estadoCuentaForm.getListadoConvenios());
        Vector v = new Vector();
        for(int i=0;i<estadoCuentaForm.getSizeListadoConvenios();i++)
        {
        	if(Double.parseDouble(mapaC.get("totalcar_"+i).toString())!=0)
        	{
        		v.add(mapaC.get("nombre_"+i));
        		v.add("");
        		v.add("Valor Total de Cargos:");
        		v.add(UtilidadTexto.formatearValores(mapaC.get("totalcar_"+i).toString()));
        		if(Double.parseDouble(mapaC.get("totalcon_"+i).toString())!=0)
            	{
            		v.add("Total Convenio:");
            		v.add(UtilidadTexto.formatearValores(mapaC.get("totalcon_"+i).toString()));
            	}
            	if(Double.parseDouble(mapaC.get("totalpac_"+i).toString())!=0)
            	{
            		v.add("Valor Total Paciente:");
            		v.add(UtilidadTexto.formatearValores(mapaC.get("totalpac_"+i).toString()));
            	}
            	v.add("");
            	v.add("");
        	}
        }
        
        comp.insertLabelInGridOfBodyPage(0, 0, v);
        
        //***********************************//
         
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
		return mapping.findForward("listadoCuentas");
	}
	
	/**
	 * Método que arma el encabezado de los reportes
	 * de estado de la cuenta
	 * @param comp
	 * @param connection
	 * @param ins
	 * @param forma
	 * @param request
	 * @param paciente 
	 * @param nombreReporte 
	 */
	private void armarEncabezado(DesignEngineApi comp, Connection con, InstitucionBasica ins, EstadoCuentaForm estadoCuentaForm, HttpServletRequest request, PersonaBasica paciente, String nombreReporte)
	{
		//Se inserta la información de la isntitucion
        comp.insertGridHeaderOfMasterPage(0, 0, 1, 2);
        Vector v = new Vector();
        v.add(ins.getRazonSocial());
        if(Utilidades.convertirAEntero(ins.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit()+" - "+ins.getDigitoVerificacion());
        else
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit());
        comp.insertLabelInGridOfMasterPage(0, 0, v);
        comp.insertLabelInGridPpalOfHeader(0, 1, nombreReporte);
        
        //Se inserta grilla del encabezado del paciente
        v = new Vector();
        EstadoCuenta mundo = new EstadoCuenta();
        int sol = 0;
        if(estadoCuentaForm.getEstado().equals("imprimirResumidoItemDetSol"))
        	sol = Utilidades.convertirAEntero(estadoCuentaForm.getNumeroSolicitud());
        logger.info("\n\nVALOR DE LA SUB CUENTA PARA LOS CODIGOS DE LAS FACTURAS>>>>>>>>>>>>>"+estadoCuentaForm.getIdSubCuenta()+"\n\n");
        String codigosFacturas = mundo.codigosFacturas(con, Utilidades.convertirAEntero(estadoCuentaForm.getIdSubCuenta()), sol);
        String numCarnet = "";
        String segApell="";
        if (paciente.getSegundoApellido()!=null)
        {
        	segApell=paciente.getSegundoApellido() .toUpperCase();
        }
        String segNom="";
        if (paciente.getSegundoNombre()!=null)
        {
        	segApell=paciente.getSegundoNombre().toUpperCase();
        }
        String nombreApellido = paciente.getPrimerApellido().toUpperCase()+" "+segApell+" "+paciente.getPrimerNombre().toUpperCase()+" "+segNom;
        logger.info("===>Código Convenio: "+estadoCuentaForm.getCodigoConvenio()+"");
        logger.info("===>Ingreso: "+estadoCuentaForm.getIdIngreso()+"");
        numCarnet = mundo.obtenerCarnet(con, Utilidades.convertirAEntero(estadoCuentaForm.getCodigoConvenio()+""), Utilidades.convertirAEntero(estadoCuentaForm.getIdIngreso()+""));
        String codigoInterfazConvenio = Utilidades.obtenerCodigoInterfazConvenioDeCodigo(Utilidades.convertirAEntero(estadoCuentaForm.getCodigoConvenio()));
        
        v.add("N° INGRESO: "+estadoCuentaForm.getConsecutivoIngreso());
        v.add("FACTURA: "+codigosFacturas);
        if(estadoCuentaForm.getNombreConvenio().length() > 45)
        	v.add("PLAN: "+(UtilidadCadena.noEsVacio(codigoInterfazConvenio)?codigoInterfazConvenio+" - ":"")+estadoCuentaForm.getNombreConvenio().toUpperCase().substring(0, 45));
        else
        	v.add("PLAN: "+(UtilidadCadena.noEsVacio(codigoInterfazConvenio)?codigoInterfazConvenio+" - ":"")+estadoCuentaForm.getNombreConvenio().toUpperCase());
        v.add(" ");
        
        if(nombreApellido.length() > 30)
        	v.add("PACIENTE: "+nombreApellido.substring(0, 30));
        else
        	v.add("PACIENTE: "+nombreApellido);
        
        v.add("DOCUMENTO: "+paciente.getCodigoTipoIdentificacionPersona()+" "+paciente.getNumeroIdentificacionPersona());
        v.add("N° H.C.: "+paciente.getHistoriaClinica());
        v.add("CARNET: "+numCarnet);
        
        if(paciente.getDireccion().length() > 20)
        	v.add("DIRECCIÓN: "+paciente.getDireccion().substring(0, 20));
        else
        	v.add("DIRECCIÓN: "+paciente.getDireccion());
        v.add("TELÉFONO: "+paciente.getTelefono());
        v.add("FECHA INGRESO: "+estadoCuentaForm.getFechaIngreso());
        HashMap<String, Object> mapa = new HashMap<String, Object> ();
        mapa = mundo.fechaEgreso(con, Integer.parseInt(estadoCuentaForm.getIdIngreso()));
        if(Integer.parseInt(mapa.get("numRegistros").toString())>0){
	        if(mapa.get("estado").equals(ConstantesBD.codigoEstadoCuentaFacturada))
	        	v.add("FECHA EGRESO: "+UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fechae").toString()));
	        if(mapa.get("estado").equals(ConstantesBD.codigoEstadoCuentaFacturadaParcial))
	        	v.add("FECHA EGRESO: Factura Parcial");
        }
        comp.insertLabelInGridOfMasterPageWithProperties(1, 0, v, DesignChoiceConstants.TEXT_ALIGN_LEFT);
    }
	
	/**
	 * Metodo que Imprime Estado Cuenta Resumido desde Cuentas
	 * @param con
	 * @param estadoCuentaForm
	 * @param mapping
	 * @param request
	 * @param medico
	 * @param paciente
	 * @param remite
	 * @return
	 */
	private ActionForward accionImprimirResSolCue(Connection con, EstadoCuentaForm estadoCuentaForm, EstadoCuenta estadoCuenta,ActionMapping mapping, HttpServletRequest request, UsuarioBasico medico, PersonaBasica paciente, int remite) 
	{
		String oldQuery="";		
    	String nombreRptDesign = "EstadoCuentaResumidoCuenta.rptdesign";
    	
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Modificado por Solicitud de la Clinica Shaio
		//Informacion del Cabezote
		/*DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+"  -  "+ins.getNit());
        v.add(ins.getDireccion()+"          "+ins.getTelefono());
        v.add("ESTADO DE CUENTA RESUMIDO POR CENTRO DE COSTO");
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        //Se inserta grilla del encabezado del paciente
        v = new Vector();
        v.add(paciente.getPrimerApellido()+" "+paciente.getSegundoApellido()+" "+paciente.getPrimerNombre()+" "+paciente.getSegundoNombre());
        v.add(paciente.getCodigoTipoIdentificacionPersona()+" "+paciente.getNumeroIdentificacionPersona());
        v.add("No.H.C.  "+paciente.getHistoriaClinica());
        v.add("Edad: "+paciente.getEdadDetallada());
        v.add("Vía ingreso: "+estadoCuentaForm.getViaIngreso());
        v.add("Centro de Atención: "+estadoCuentaForm.getCentroAtencion());
        v.add("N° ingreso: "+estadoCuentaForm.getConsecutivoIngreso());
        v.add("Fecha ingreso: "+estadoCuentaForm.getFechaIngreso());
        
        EstadoCuenta mundo=new EstadoCuenta();
        HashMap<String, Object> mapa = new HashMap<String, Object> ();
        mapa=mundo.fechaEgreso(con, Integer.parseInt(estadoCuentaForm.getIdIngreso()));
        if(Integer.parseInt(mapa.get("numRegistros").toString())>0){
	        if(mapa.get("estado").equals(ConstantesBD.codigoEstadoCuentaFacturada))
	        	v.add("Fecha de Egreso: "+UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fechae").toString()));
	        if(mapa.get("estado").equals(ConstantesBD.codigoEstadoCuentaFacturadaParcial))
	        	v.add("Fecha de Egreso: Factura Parcial");
        }
        
        v.add("Estado ingreso: "+estadoCuentaForm.getEstadoIngreso());
        if(!estadoCuentaForm.getDescEmpresaSubContratada().equals(""))
        	v.add("Entidad SubContratada: "+estadoCuentaForm.getDescEmpresaSubContratada());        	
        
        v.add("N° Cuenta: "+estadoCuentaForm.getIdCuenta());
        v.add("Estado Cuenta: "+estadoCuentaForm.getEstadoCuenta());
        v.add("Dirección: "+paciente.getDireccion());
        v.add("Teléfono: "+paciente.getTelefono());
        comp.insertLabelInGridOfMasterPageWithProperties(3,0,v,DesignChoiceConstants.TEXT_ALIGN_LEFT);*/
        
		//***************PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE******************************************
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        String nombreReporte = "ESTADO DE CUENTA RESUMIDO POR CENTRO DE COSTO";
        armarEncabezado(comp, con, ins, estadoCuentaForm, request, paciente, nombreReporte);
        //*******************************************************************************************************
		
        comp.obtenerComponentesDataSet("resumidoCuentasS");
        oldQuery = ConsultasBirt.consultaImprimirResSolCueEstadoCuenta(estadoCuentaForm.getIdIngreso(), remite);
        
        logger.info("CONSULTA REPORTEEEE>>>>>>>>>>>>>>>>>>>>"+oldQuery);

        comp.modificarQueryDataSet(oldQuery);
        
        //*******TOTALES CONVENIO*************//
        
        estadoCuentaForm.setListadoConvenios(estadoCuenta.cargarTodosConvenioIngreso(con, estadoCuentaForm.getIdIngreso()));
        
        HashMap<String, Object> mapaC = new HashMap<String, Object> ();
        mapaC=estadoCuenta.calcularTotalesPorIngreso(con, estadoCuentaForm.getListadoConvenios());
        Vector v = new Vector();
        for(int i=0;i<estadoCuentaForm.getSizeListadoConvenios();i++)
        {
        	if(Double.parseDouble(mapaC.get("totalcar_"+i).toString())!=0)
        	{
        		v.add(mapaC.get("nombre_"+i));
        		v.add("");
        		v.add("Valor Total de Cargos:");
        		v.add(UtilidadTexto.formatearValores(mapaC.get("totalcar_"+i).toString()));
        		if(Double.parseDouble(mapaC.get("totalcon_"+i).toString())!=0)
            	{
            		v.add("Total Convenio:");
            		v.add(UtilidadTexto.formatearValores(mapaC.get("totalcon_"+i).toString()));
            	}
            	if(Double.parseDouble(mapaC.get("totalpac_"+i).toString())!=0)
            	{
            		v.add("Valor Total Paciente:");
            		v.add(UtilidadTexto.formatearValores(mapaC.get("totalpac_"+i).toString()));
            	}
            	v.add("");
            	v.add("");
        	}
        }
        
        comp.insertLabelInGridOfBodyPage(0, 0, v);
        
        //***********************************//
         
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
		return mapping.findForward("listadoCuentas");
	}
	
	/**
	 * Metodo para Imprimir el listado de solicitudes por convenio
	 * @param con
	 * @param estadoCuentaForm
	 * @param mapping
	 * @param request
	 * @param medico
	 * @param paciente
	 * @return
	 */
	private ActionForward accionImprimirDetSolCon(Connection con, EstadoCuentaForm estadoCuentaForm, EstadoCuenta estadoCuenta, ActionMapping mapping, HttpServletRequest request, UsuarioBasico medico, PersonaBasica paciente, int remite) 
	{
		String nombreRptDesign = "";
		nombreRptDesign = "EstadoCuentaDetallado.rptdesign";
				
		InstitucionBasica ins = new InstitucionBasica();
		ins.cargarXConvenio(medico.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
		
		//***************PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE*****************************************
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        String nombreReporte = "ESTADO DE LA CUENTA DETALLADO POR SOLICITUD";
        armarEncabezado(comp, con, ins, estadoCuentaForm, request, paciente, nombreReporte);
        //*******************************************************************************************************
                
        //*********************SEGUNDO SE MODIFICA LA CONSULTA*************************************************** 
        comp.obtenerComponentesDataSet("detalleSolicitud");            
        String oldQuery=comp.obtenerQueryDataSet();
        
        logger.info("\n\n CONSULTA ANTES DE MODIFICAR----->"+oldQuery+"\n\n");
                   
        /**
		 * Validar: 
		 * 1. Si la solicitud tiene despacho de equivalentes 
		 * 2. El estado del sparametro "Mostrar en las solicitudes art&iacute;los principales con despacho CERO S/R?"
		 * @author Diana Ruiz 		
		 */ 		
		
		estadoCuentaForm.setMostrarArticPrinciDespachoCero(validarParametroMostrarArtiPrinciDespachoCero(medico));
	
		if(estadoCuentaForm.getEstado().equals("imprimirDetSolDetSol")){
			if(estadoCuenta.despachoEquivalentes(con, Integer.parseInt(estadoCuentaForm.getNumeroSolicitud()), estadoCuentaForm.getCodigoTipoSolicitud(), Integer.parseInt(estadoCuentaForm.getIdSubCuenta()), medico.getCodigoInstitucionInt()))
			{
				 if (estadoCuentaForm.getMostrarArticPrinciDespachoCero().equals(ConstantesBD.acronimoNo))
				 {
					 oldQuery = ConsultasBirt.consultaImprimirDetSolConEstadoCuentaEquivalente(estadoCuentaForm.getIdSubCuenta(), estadoCuentaForm.getNumeroSolicitud());
				 }else{
					 oldQuery = ConsultasBirt.consultaImprimirDetSolConEstadoCuenta(estadoCuentaForm.getEstado(), remite, estadoCuentaForm.getIdSubCuenta(), estadoCuentaForm.getNumeroSolicitud());
				 }
					 
			}else{
				oldQuery = ConsultasBirt.consultaImprimirDetSolConEstadoCuenta(estadoCuentaForm.getEstado(), remite, estadoCuentaForm.getIdSubCuenta(), estadoCuentaForm.getNumeroSolicitud());
			}						
		}else{
			//Según tipo de impresión se modifica la consulta
			oldQuery = ConsultasBirt.consultaImprimirDetSolConEstadoCuenta(estadoCuentaForm.getEstado(), remite, estadoCuentaForm.getIdSubCuenta(), estadoCuentaForm.getNumeroSolicitud());
		}   
        
		logger.info("\n\n CONSULTA DESPUES DE VALIDACIONES EQUIVALENTES----->"+ oldQuery+"\n\n");
			
        //se actualiza Query
        comp.modificarQueryDataSet(oldQuery);
        
		//********************CONSULTA DE LOS TOTALES******************************************************************************
		estadoCuenta.calcularTotalesCargos(
			con, 
			estadoCuentaForm.getIdSubCuenta(), 
			estadoCuentaForm.getIdCuenta(), 
			paciente.getCodigoPersona()+"", 
			estadoCuentaForm.getEsConvenioPoliza(),
			estadoCuentaForm.getCodigoTipoRegimen(),
			medico.getCodigoInstitucionInt(),
			paciente.getCodigoIngreso()
		);
		estadoCuentaForm.llenarFormTotales(estadoCuenta);
		//*************************************************************************************************************************
        
        //*******************SE INGRESAN LOS TOTALES*******************************************************
        Vector v = new Vector();
        v.add("Valor Total de Cargos:");
        v.add(estadoCuentaForm.getValorTotalCargos());
        
        //Si no es particular se muestra el valor del convenio
        if(!estadoCuentaForm.getCodigoTipoRegimen().equals(ConstantesBD.codigoTipoRegimenParticular+""))
        {
        	if(UtilidadTexto.getBoolean(estadoCuentaForm.getEsConvenioPoliza()))
        		v.add("Saldo Monto Autorizado:");
        	else
        		v.add("Valor Total a Cargo del Convenio:");
            v.add(estadoCuentaForm.getValorTotalConvenio());
        }
        
        v.add("Valor Total a Cargo del Paciente:");
        v.add(estadoCuentaForm.getValorTotalPaciente());
        
        v.add("Valor Total Abonos del Paciente:");
        v.add(estadoCuentaForm.getValorTotalAbonos());
        
        //Si hay devolucion, se muestra, dde lo contrario se muestra el neto a cargo del paciente
        if(!estadoCuentaForm.getValorDevolucionPaciente().equals(""))
        {
        	v.add("Valor Devolución al Paciente:");
            v.add(estadoCuentaForm.getValorDevolucionPaciente());
        }
        else
        {
        	v.add("Valor Neto a Cargo del Paciente:");
            v.add(estadoCuentaForm.getValorNetoPaciente());
        }
        
        //Si es particular se muestra las ultimas celdas en blanco
        if(estadoCuentaForm.getCodigoTipoRegimen().equals(ConstantesBD.codigoTipoRegimenParticular+""))
        {
        	v.add("");
            v.add("");
        }
        comp.insertLabelInGridOfBodyPage(0, 0, v);
        
        //SE ENVIAN LOS ATRIBUTOS AL JSP PARA IMPRIMIR******************************************************
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        //SE ACTUALIZAN LOS PARAMETROS DE CONEXION CON LA BD***************************************************
        comp.updateJDBCParameters(newPathReport);
        
        UtilidadBD.closeConnection(con);
        if(estadoCuentaForm.getEstado().equals("imprimirDetSolCon") || estadoCuentaForm.getEstado().equals("imprimirDetSolConF"))
        	return mapping.findForward("listadoConvenios");
        if(estadoCuentaForm.getEstado().equals("imprimirDetSolDetSol"))
        	return mapping.findForward("detalleSolicitud");
        return mapping.findForward("listadoSolicitudes");
	}
	
	/**
	 * Metodo de Impresion de solicitudes por convenio Resumido por Centro de Costo
	 * @param con
	 * @param estadoCuentaForm
	 * @param mapping
	 * @param request
	 * @param medico
	 * @param paciente
	 * @param remite
	 * @return
	 */
	private ActionForward accionImprimirResSolCon(Connection con, EstadoCuentaForm estadoCuentaForm, EstadoCuenta estadoCuenta, ActionMapping mapping, HttpServletRequest request, UsuarioBasico medico, PersonaBasica paciente, int remite) 
	{
		String nombreRptDesign = "";
		//nombreRptDesign = "EstadoCuentaResumido.rptdesign";
		nombreRptDesign = "EstadoCuentaResumidoCuenta.rptdesign";
				
		InstitucionBasica ins = new InstitucionBasica();
		ins.cargarXConvenio(medico.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
		
		//***************PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE*****************************************
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        String nombreReporte = "ESTADO DE CUENTA RESUMIDO POR CENTRO DE COSTO";
        armarEncabezado(comp, con, ins, estadoCuentaForm, request, paciente, nombreReporte);
        //*******************************************************************************************************
        
        //*********************SEGUNDO SE MODIFICA LA CONSULTA*************************************************** 
        comp.obtenerComponentesDataSet("detalleSolicitud");            
        String oldQuery=comp.obtenerQueryDataSet();
        
        //Segun tipo de impresión se modifica la consulta
        /*oldQuery = "SELECT "+ 
		    	"t.fecha_cargo, "+
		    	"t.convenio, " +
		    	"t.codigo, "+
		    	"t.descripcion, "+
		    	"sum(t.cantidad) As cantidad, "+
		    	"sum(t.total_cargo) AS total_cargo, "+
		    	"sum(t.total_recargo) AS total_recargo, "+
		    	"sum(t.total_dcto) AS total_dcto "+ 
		    	"FROM "+
		    	"( "+
		    		"( "+
		    			"SELECT "+ 
						"0 as solicitud, "+ //se deja vacío para acomodarse con el union de cirugias
						"CASE WHEN c.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"' THEN getNomDeudorIngreso(sbc.ingreso) ELSE c.nombre END AS convenio, " +
						"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
						"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
							"getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") "+ 
						"ELSE "+ 
							"coalesce(getobtenercodigointerfaz(dc.articulo),'') "+ 
						"END AS codigo, "+
						"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
							"getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") "+ 
						"ELSE "+ 
							"getdescripcionarticulo(dc.articulo) "+ 
						"END AS descripcion, "+
						"sum(coalesce(dc.cantidad_cargada,0)) AS cantidad, "+
						"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
						"sum(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)) AS total_recargo, "+
						"sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)) AS total_dcto "+ 
						"FROM det_cargos dc "+
						"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
						"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
						"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
						"WHERE " +
						"dc.sub_cuenta = "+estadoCuentaForm.getIdSubCuenta()+" AND ";
				        if(estadoCuentaForm.getEstado().equals("imprimirResSolDetSol"))
							oldQuery+="dc.solicitud="+estadoCuentaForm.getNumeroSolicitud()+" AND ";
				        if(remite==2)
							oldQuery+="dc.facturado='S' AND ";
				        oldQuery+="dc.eliminado='"+ConstantesBD.acronimoNo+"' AND ";
				        if(!estadoCuentaForm.getEstado().equals("imprimirResSolDetSol"))
				        {
				        	oldQuery+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND " +
				        	"dc.valor_total_cargado > 0 AND ";
				        }
						oldQuery+="dc.paquetizado = '"+ConstantesBD.acronimoNo+"' AND " +
						"dc.tipo_solicitud <> "+ConstantesBD.codigoTipoSolicitudCirugia+" "+ 
						"GROUP BY dc.fecha_modifica,dc.servicio,dc.articulo,c.nombre,c.tipo_regimen,sbc.ingreso "+ 
		    		") "+
		    		"UNION "+
		    		"( "+
		    			"SELECT "+ 
		    			"dc.solicitud, "+
		    			"CASE WHEN c.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"' THEN getNomDeudorIngreso(sbc.ingreso) ELSE c.nombre END AS convenio, " +
		    			"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
		    			"getcodigoespecialidad(dc.servicio_cx) || '-' || dc.servicio_cx  AS codigo, "+
		    			"getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+")  AS descripcion, "+
		    			"1 AS cantidad, "+
		    			"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
		    			"sum(coalesce(dc.valor_unitario_recargo,0)) AS total_recargo, "+
		    			"sum(coalesce(dc.valor_unitario_dcto,0)) AS total_dcto "+ 
		    			"FROM det_cargos dc "+
		    			"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
						"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
						"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
		    			"WHERE " +
		    			"dc.sub_cuenta = "+estadoCuentaForm.getIdSubCuenta()+" AND ";
				        if(estadoCuentaForm.getEstado().equals("imprimirResSolDetSol"))
							oldQuery+="dc.solicitud="+estadoCuentaForm.getNumeroSolicitud()+" AND ";
		    			if(remite==2)
							oldQuery+="dc.facturado='S' AND ";
		    			if(!estadoCuentaForm.getEstado().equals("imprimirResSolDetSol"))
		    				oldQuery+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND ";
		    			oldQuery+="dc.eliminado='"+ConstantesBD.acronimoNo+"' AND ";
		    			if(!estadoCuentaForm.getEstado().equals("imprimirResSolDetSol"))
		    				oldQuery+="dc.valor_total_cargado > 0 AND ";
		    			oldQuery+="dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
		    			"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
		    			"dc.articulo is null "+
		    			"GROUP BY dc.fecha_modifica,dc.solicitud,dc.servicio_cx,c.nombre,c.tipo_regimen,sbc.ingreso "+ 
		    		") "+
		    		//La consulta de los materiales especiales
		    		"UNION "+
		    		"( "+
		    			"SELECT "+ 
		    			"dc.solicitud, "+
		    			"CASE WHEN c.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"' THEN getNomDeudorIngreso(sbc.ingreso) ELSE c.nombre END AS convenio, " +
		    			"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
		    			"''  AS codigo, "+
		    			"'Materiales Especiales Orden ' || getconsecutivosolicitud(dc.solicitud)   AS descripcion, "+
		    			"sum(dc.cantidad_cargada) AS cantidad, "+
		    			"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
		    			"sum(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)) AS total_recargo, "+
		    			"sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)) AS total_dcto "+ 
		    			"FROM det_cargos dc "+
		    			"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
						"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
						"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
		    			"WHERE " +
		    			"dc.sub_cuenta = "+estadoCuentaForm.getIdSubCuenta()+" AND ";
		    			if(estadoCuentaForm.getEstado().equals("imprimirResSolDetSol"))
							oldQuery+="dc.solicitud="+estadoCuentaForm.getNumeroSolicitud()+" AND ";
		    			if(remite==2)
							oldQuery+="dc.facturado='S' AND ";
		    			if(!estadoCuentaForm.getEstado().equals("imprimirResSolDetSol"))
		    				oldQuery+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND ";
		    			oldQuery+="dc.eliminado='"+ConstantesBD.acronimoNo+"' AND ";
		    			if(!estadoCuentaForm.getEstado().equals("imprimirResSolDetSol"))
		    				oldQuery+="dc.valor_total_cargado > 0 AND ";
		    			oldQuery+="dc.paquetizado = '"+ConstantesBD.acronimoNo+"' and " +
		    			"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
		    			"dc.articulo is not null "+
		    			"GROUP BY dc.fecha_modifica,dc.solicitud,c.nombre,c.tipo_regimen,sbc.ingreso "+ 
		    		") "+
		    	") t "+
		    "GROUP BY t.fecha_cargo,t.codigo,t.descripcion,t.convenio "+
		    "ORDER BY convenio, fecha_cargo, codigo";*/

        /*
		    oldQuery = "SELECT "+ 
				    	"t.fecha_cargo, "+
				    	"t.convenio, " +
				    	"t.asocio, " +
				    	"t.centro_costo_solicitado, " +
				    	"t.nomcc, " +
				    	"t.codigo_manual, " +
				    	"t.profesional, " +
				    	"t.valoru, " +
						"t.valort, " +
				    	"t.codigo, "+
				    	"t.descripcion, "+
				    	"sum(t.cantidad) As cantidad, "+
				    	"sum(t.total_cargo) AS total_cargo, "+
				    	"sum(t.total_recargo) AS total_recargo, "+
				    	"sum(t.total_dcto) AS total_dcto, "+
				    	"to_char(current_date, 'DD/MM/YYYY') AS fechaActual," +
				    	"t.esquema_tarifario," +
				    	"t.identificadorcc  " +
				    	"FROM "+
				    	"( "+
				    		"( "+
				    			"SELECT "+ 
								"0 as solicitud, "+ //se deja vacío para acomodarse con el union de cirugias
								"CASE WHEN c.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"' THEN UPPER(getNomDeudorIngreso(sbc.ingreso)) ELSE UPPER(c.nombre) END AS convenio, " +
								"'' AS asocio, " +
								"sol.centro_costo_solicitado AS centro_costo_solicitado, " +
								"UPPER(cc.nombre) AS nomcc, " +
								"getidentificadorcentrocosto(sol.centro_costo_solicitado) As identificadorcc," +
								"c.tipo_codigo AS codigo_manual, " +
								"CASE WHEN dc.servicio IS NOT NULL THEN getnombremedico(sol.codigo_medico_responde) ELSE '' END AS profesional, "+
								
								//Modificado por la Tarea 51069
								//"((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0)))) AS valoru, " +
								"coalesce(dc.valor_unitario_cargado,0) AS valoru, "+
								
								"((((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0))))*1) AS valort, " +
								"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
								"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
									"getobtenercodigocupsserv(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") "+ 
								"ELSE "+ 
									"coalesce(getobtenercodigointerfaz(dc.articulo),'') "+ 
								"END AS codigo, "+
								"CASE WHEN dc.servicio IS NOT NULL THEN "+ 
									"UPPER(getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+")) "+ 
								"ELSE "+ 
									"UPPER(getdescripcionalternaarticulo(dc.articulo)) "+ 
								"END AS descripcion, "+
								"sum(coalesce(dc.cantidad_cargada,0)) AS cantidad, "+
								"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
								"sum(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)) AS total_recargo, "+
								"sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)) AS total_dcto, "+
								"getnombreesquematarifario(dc.esquema_tarifario) As esquema_tarifario  "+		
								"FROM det_cargos dc "+
								"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
								"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
								"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
								"INNER JOIN centros_costo cc ON (sol.centro_costo_solicitado=cc.codigo) " +
								"WHERE " +
								"dc.sub_cuenta = "+estadoCuentaForm.getIdSubCuenta()+" AND ";
						        if(estadoCuentaForm.getEstado().equals("imprimirResSolDetSol"))
									oldQuery+="dc.solicitud="+estadoCuentaForm.getNumeroSolicitud()+" AND ";
						        if(remite==2)
									oldQuery+="dc.facturado='S' AND ";
						        oldQuery+="dc.eliminado='"+ConstantesBD.acronimoNo+"' AND ";
						        if(!estadoCuentaForm.getEstado().equals("imprimirResSolDetSol"))
						        {
						        	oldQuery+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND " +
						        	"dc.valor_total_cargado > 0 AND ";
						        }
								oldQuery+="dc.paquetizado = '"+ConstantesBD.acronimoNo+"' AND " +
								"dc.tipo_solicitud <> "+ConstantesBD.codigoTipoSolicitudCirugia+" "+ 
								"GROUP BY 2,3,4,5,dc.fecha_modifica,dc.servicio,dc.articulo,sol.centro_costo_solicitado,c.tipo_codigo,c.tipo_regimen,sbc.ingreso,cc.nombre,sol.codigo_medico_responde,dc.valor_unitario_cargado,dc.esquema_tarifario,identificadorcc "+ 
				    		") "+
				    		"UNION "+
				    		"( "+
				    			"SELECT "+ 
				    			"dc.solicitud, "+
				    			"CASE WHEN c.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"' THEN UPPER(getNomDeudorIngreso(sbc.ingreso)) ELSE UPPER(c.nombre) END AS convenio, " +
				    			"tpa.codigo_asocio || '-' || tpa.nombre_asocio AS asocio, " +
				    			"sol.centro_costo_solicitado AS centro_costo_solicitado, " +
				    			"getidentificadorcentrocosto(sol.centro_costo_solicitado) As identificadorcc," +
								"UPPER(cc.nombre) AS nomcc, " +
				    			"c.tipo_codigo AS codigo_manual, " +
				    			"coalesce(getNomProfesionalAsocio(dc.det_cx_honorarios),'') AS profesional, "+
				    			
				    			//Modificado por la Tarea 51069
				    			//"((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0)))) AS valoru, " +
				    			"coalesce(dc.valor_unitario_cargado,0) AS valoru, "+
				    			
								"((((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0))))*1) AS valort, " +
				    			"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
				    			"getcodigoespecialidad(dc.servicio_cx) || '-' || dc.servicio_cx  AS codigo, "+
				    			"substr(UPPER(getnombreservicio(dc.servicio_cx,"+ConstantesBD.codigoTarifarioCups+")),0,28) || ' - ' || substr(tpa.nombre_asocio,0,28)  AS descripcion, "+
				    			"1 AS cantidad, "+
				    			"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
				    			"sum(coalesce(dc.valor_unitario_recargo,0)) AS total_recargo, "+
				    			"sum(coalesce(dc.valor_unitario_dcto,0)) AS total_dcto, "+
				    			"getnombreesquematarifario(dc.esquema_tarifario) As esquema_tarifario  "+		
				    			"FROM det_cargos dc "+
				    			"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
								"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
								"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
								"INNER JOIN tipos_asocio tpa ON (dc.tipo_asocio=tpa.codigo) " +
								"INNER JOIN centros_costo cc ON (sol.centro_costo_solicitado=cc.codigo) " +
				    			"WHERE " +
				    			"dc.sub_cuenta = "+estadoCuentaForm.getIdSubCuenta()+" AND ";
						        if(estadoCuentaForm.getEstado().equals("imprimirResSolDetSol"))
									oldQuery+="dc.solicitud="+estadoCuentaForm.getNumeroSolicitud()+" AND ";
						        if(remite==2)
									oldQuery+="dc.facturado='S' AND ";
						        oldQuery+="dc.eliminado='"+ConstantesBD.acronimoNo+"' AND ";
						        if(!estadoCuentaForm.getEstado().equals("imprimirResSolDetSol"))
						        {
						        	oldQuery+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND " +
						        	"dc.valor_total_cargado > 0 AND ";
						        }
								oldQuery+="dc.paquetizado = '"+ConstantesBD.acronimoNo+"' AND " +
				    			"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
				    			"dc.articulo is null "+
				    			"GROUP BY dc.fecha_modifica,dc.solicitud,c.nombre,tpa.codigo_asocio,tpa.nombre_asocio,sol.centro_costo_solicitado,c.tipo_codigo,dc.det_cx_honorarios,dc.servicio_cx,c.tipo_regimen,sbc.ingreso,cc.nombre,dc.valor_unitario_cargado,dc.esquema_tarifario,identificadorcc,dc.servicio " +
				    		") "+
				    		//La consulta de los materiales especiales
				    		"UNION "+
				    		"( "+
				    			"SELECT "+ 
				    			"dc.solicitud, "+
				    			"CASE WHEN c.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"' THEN UPPER(getNomDeudorIngreso(sbc.ingreso)) ELSE UPPER(c.nombre) END AS convenio, " +
				    			"'' AS asocio, " +
				    			"sol.centro_costo_solicitado AS centro_costo_solicitado, " +
				    			"getidentificadorcentrocosto(sol.centro_costo_solicitado) As identificadorcc," +
								"UPPER(cc.nombre) AS nomcc, " +
				    			"c.tipo_codigo AS codigo_manual, " +
				    			"'' AS profesional, " +
				    			
				    			//Modificado por la Tarea 51069
				    			//"((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0)))) AS valoru, " +
				    			"coalesce(dc.valor_unitario_cargado,0) AS valoru, "+
				    			
				    			"((((sum(coalesce(dc.valor_total_cargado,0)))+(sum(coalesce(dc.valor_unitario_recargo,0))))-(sum(coalesce(dc.valor_unitario_dcto,0))))*1) AS valort, " +
				    			"to_char(dc.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_cargo, "+
				    			"''  AS codigo, "+
				    			"'Materiales Especiales Orden ' || getconsecutivosolicitud(dc.solicitud)   AS descripcion, "+
				    			"sum(dc.cantidad_cargada) AS cantidad, "+
				    			"sum(coalesce(dc.valor_total_cargado,0)) AS total_cargo, "+
				    			"sum(coalesce(dc.valor_unitario_recargo*dc.cantidad_cargada,0)) AS total_recargo, "+
				    			"sum(coalesce(dc.valor_unitario_dcto*dc.cantidad_cargada,0)) AS total_dcto, "+
				    			"getnombreesquematarifario(dc.esquema_tarifario) As esquema_tarifario  "+		
				    			"FROM det_cargos dc "+
				    			"INNER JOIN solicitudes sol ON(sol.numero_solicitud=dc.solicitud) "+
								"INNER JOIN sub_cuentas sbc ON(dc.sub_cuenta=sbc.sub_cuenta) " +
								"INNER JOIN convenios c ON(dc.convenio=c.codigo) " +
								"INNER JOIN centros_costo cc ON (sol.centro_costo_solicitado=cc.codigo) " +
				    			"WHERE " +
				    			"dc.sub_cuenta = "+estadoCuentaForm.getIdSubCuenta()+" AND ";
						        if(estadoCuentaForm.getEstado().equals("imprimirResSolDetSol"))
									oldQuery+="dc.solicitud="+estadoCuentaForm.getNumeroSolicitud()+" AND ";
						        if(remite==2)
									oldQuery+="dc.facturado='S' AND ";
						        oldQuery+="dc.eliminado='"+ConstantesBD.acronimoNo+"' AND ";
						        if(!estadoCuentaForm.getEstado().equals("imprimirResSolDetSol"))
						        {
						        	oldQuery+="dc.estado = "+ConstantesBD.codigoEstadoFCargada+" AND " +
						        	"dc.valor_total_cargado > 0 AND ";
						        }
								oldQuery+="dc.paquetizado = '"+ConstantesBD.acronimoNo+"' AND " +
				    			"dc.tipo_solicitud = "+ConstantesBD.codigoTipoSolicitudCirugia+" and " +
				    			"dc.articulo is not null "+
				    			"GROUP BY dc.fecha_modifica,dc.solicitud,c.nombre,sol.centro_costo_solicitado,c.tipo_codigo,c.tipo_regimen,sbc.ingreso,cc.nombre,dc.valor_unitario_cargado,dc.esquema_tarifario,identificadorcc "+ 
				    		") "+
				    	") t "+
				    "GROUP BY t.fecha_cargo,t.codigo,t.descripcion,t.convenio,t.asocio,t.centro_costo_solicitado,t.codigo_manual,t.profesional,t.valoru,t.valort,t.nomcc,t.esquema_tarifario,t.identificadorcc "+
				    "ORDER BY convenio, fecha_cargo, codigo"; 
		    			*/
        
         /**
		 * Validar: 
		 * 1. Si la solicitud tiene despacho de equivalentes 
		 * 2. El estado del parametro "Mostrar en las solicitudes art&iacute;los principales con despacho CERO S/R?"
		 * @author Diana Ruiz 		
		 */ 		
		
		estadoCuentaForm.setMostrarArticPrinciDespachoCero(validarParametroMostrarArtiPrinciDespachoCero(medico));
	
		if(estadoCuentaForm.getEstado().equals("imprimirResSolDetSol")){
			if(estadoCuenta.despachoEquivalentes(con, Integer.parseInt(estadoCuentaForm.getNumeroSolicitud()), estadoCuentaForm.getCodigoTipoSolicitud(), Integer.parseInt(estadoCuentaForm.getIdSubCuenta()), medico.getCodigoInstitucionInt()))
			{
				 if (estadoCuentaForm.getMostrarArticPrinciDespachoCero().equals(ConstantesBD.acronimoNo))
				 {
					 oldQuery = ConsultasBirt.consultaImprimirResSolConEstadoCuentaEquivalente(estadoCuentaForm.getIdIngreso(), remite);
				 }else{
					 oldQuery = ConsultasBirt.consultaImprimirResSolConEstadoCuenta(estadoCuentaForm.getIdIngreso(), remite);
				 }
					 
			}else{
				oldQuery = ConsultasBirt.consultaImprimirResSolConEstadoCuenta(estadoCuentaForm.getIdIngreso(), remite);
			}						
		}else{
			//Según tipo de impresión se modifica la consulta
			oldQuery = ConsultasBirt.consultaImprimirResSolConEstadoCuenta(estadoCuentaForm.getIdIngreso(), remite);
		}   
        
		logger.info("\n\n CONSULTA DESPUES DE VALIDACIONES EQUIVALENTES----->"+ oldQuery+"\n\n");
        
		
             
        
        /*
        
        oldQuery = ConsultasBirt.consultaImprimirResSolConEstadoCuenta(estadoCuentaForm.getIdIngreso(), remite);
        
        logger.info("CONSULTA IMPRESIOIN=> "+oldQuery);*/
		
		
		
		
        
		//se actualiza Query
        comp.modificarQueryDataSet(oldQuery);
        
        //********************CONSULTA DE LOS TOTALES******************************************************************************
		estadoCuenta.calcularTotalesCargos(
			con, 
			estadoCuentaForm.getIdSubCuenta(), 
			estadoCuentaForm.getIdCuenta(), 
			paciente.getCodigoPersona()+"", 
			estadoCuentaForm.getEsConvenioPoliza(),
			estadoCuentaForm.getCodigoTipoRegimen(),
			medico.getCodigoInstitucionInt(),
			paciente.getCodigoIngreso()
		);
		estadoCuentaForm.llenarFormTotales(estadoCuenta);
        
        //*******************SE INGRESAN LOS TOTALES*******************************************************
        Vector v = new Vector();
        v.add("Valor Total de Cargos:");
        v.add(estadoCuentaForm.getValorTotalCargos());
        
        //Si no es particular se muestra el valor del convenio
        if(!estadoCuentaForm.getCodigoTipoRegimen().equals(ConstantesBD.codigoTipoRegimenParticular+""))
        {
        	if(UtilidadTexto.getBoolean(estadoCuentaForm.getEsConvenioPoliza()))
        		v.add("Saldo Monto Autorizado:");
        	else
        		v.add("Valor Total a Cargo del Convenio:");
            v.add(estadoCuentaForm.getValorTotalConvenio());
        }
        
        v.add("Valor Total a Cargo del Paciente:");
        v.add(estadoCuentaForm.getValorTotalPaciente());
        
        v.add("Valor Total Abonos del Paciente:");
        v.add(estadoCuentaForm.getValorTotalAbonos());
        
        //Si hay devolucion, se muestra, dde lo contrario se muestra el neto a cargo del paciente
        if(!estadoCuentaForm.getValorDevolucionPaciente().equals(""))
        {
        	v.add("Valor Devolución al Paciente:");
            v.add(estadoCuentaForm.getValorDevolucionPaciente());
        }
        else
        {
        	v.add("Valor Neto a Cargo del Paciente:");
            v.add(estadoCuentaForm.getValorNetoPaciente());
        }
        
        //Si es particular se muestra las ultimas celdas en blanco
        if(estadoCuentaForm.getCodigoTipoRegimen().equals(ConstantesBD.codigoTipoRegimenParticular+""))
        {
        	v.add("");
            v.add("");
        }
        comp.insertLabelInGridOfBodyPage(0, 0, v);
        
        //SE INGRESAR LOS TOTALES CONVERTIDOS
        if(estadoCuentaForm.getManejaConversionMoneda() && estadoCuentaForm.getIndex()>=0)
        {
        	//*******************SE INGRESAN LOS TOTALES*******************************************************
        	v = new Vector();
        	v.add("Valores Convertidos "+estadoCuentaForm.getTiposMonedaTagMap("descripciontipomoneda_"+estadoCuentaForm.getIndex())+" "+estadoCuentaForm.getTiposMonedaTagMap("simbolotipomoneda_"+estadoCuentaForm.getIndex())+" "+estadoCuentaForm.getTiposMonedaTagMap("factorconversion_"+estadoCuentaForm.getIndex()));
        	//v.add("");
            v.add("Valor Total de Cargos Convertido:");
            v.add(estadoCuentaForm.getValorTotalCargosConvertido());
            
            //Si no es particular se muestra el valor del convenio
            if(!estadoCuentaForm.getCodigoTipoRegimen().equals(ConstantesBD.codigoTipoRegimenParticular+""))
            {
            	if(UtilidadTexto.getBoolean(estadoCuentaForm.getEsConvenioPoliza()))
            		v.add("Saldo Monto Autorizado Convertido:");
            	else
            		v.add("Valor Total a Cargo del Convenio Convertido:");
                v.add(estadoCuentaForm.getValorTotalConvenioConvertido());
            }
            
            v.add("Valor Total a Cargo del Paciente Convertido:");
            v.add(estadoCuentaForm.getValorTotalPacienteConvertido());
            
            v.add("Valor Total Abonos del Paciente Convertido:");
            v.add(estadoCuentaForm.getValorTotalAbonosConvertido());
            
            //Si hay devolucion, se muestra, dde lo contrario se muestra el neto a cargo del paciente
            if(!estadoCuentaForm.getValorDevolucionPaciente().equals(""))
            {
            	v.add("Valor Devolución al Paciente Convertido:");
                v.add(estadoCuentaForm.getValorDevolucionPacienteConvertido());
            }
            else
            {
            	v.add("Valor Neto a Cargo del Paciente Convertido:");
                v.add(estadoCuentaForm.getValorNetoPacienteConvertido());
            }
            
            //Si es particular se muestra las ultimas celdas en blanco
            if(estadoCuentaForm.getCodigoTipoRegimen().equals(ConstantesBD.codigoTipoRegimenParticular+""))
            {
            	v.add("");
                v.add("");
            }
            comp.insertLabelInGridOfBodyPage(1, 0, v);
        }
        
        //SE ENVIAN LOS ATRIBUTOS AL JSP PARA IMPRIMIR******************************************************
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        //SE ACTUALIZAN LOS PARAMETROS DE CONEXION CON LA BD***************************************************
        comp.updateJDBCParameters(newPathReport);
        
        UtilidadBD.closeConnection(con);
        if(estadoCuentaForm.getEstado().equals("imprimirResSolCon") || estadoCuentaForm.getEstado().equals("imprimirResSolConF"))
        	return mapping.findForward("listadoConvenios");
        if(estadoCuentaForm.getEstado().equals("imprimirResSolDetSol"))
        	return mapping.findForward("detalleSolicitud");
        return mapping.findForward("listadoSolicitudes");
	}

	
	/**
	 * Método que realiza la impresión resumida de l estado de la cuenta
	 * @param con
	 * @param estadoCuentaForm
	 * @param mapping
	 * @param request
	 * @param medico 
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionImprimirItem(Connection con, EstadoCuentaForm estadoCuentaForm, EstadoCuenta estadoCuenta, ActionMapping mapping, HttpServletRequest request, UsuarioBasico medico, PersonaBasica paciente, int remite) 
	{
		String nombreRptDesign = "";
		nombreRptDesign = "EstadoCuentaItem.rptdesign";
		
		InstitucionBasica ins = new InstitucionBasica();
		ins.cargarXConvenio(medico.getCodigoInstitucionInt(), paciente.getCodigoConvenio());
		
		//***************PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        String nombreReporte = "REPORTE DETALLADO DE CUENTA";
        armarEncabezado(comp, con, ins, estadoCuentaForm, request, paciente, nombreReporte);
        //*******************************************************************************************************
        
        //*********************SEGUNDO SE MODIFICA LA CONSULTA*************************************************** 
        comp.obtenerComponentesDataSet("detalleItem");            
        String oldQuery=comp.obtenerQueryDataSet();
        
        /**
		 * Validar: 
		 * 1. Si la solicitud tiene despacho de equivalentes 
		 * 2. El estado del sparametro "Mostrar en las solicitudes art&iacute;los principales con despacho CERO S/R?"
		 * @author Diana Ruiz 		
		 */ 		
		
		estadoCuentaForm.setMostrarArticPrinciDespachoCero(validarParametroMostrarArtiPrinciDespachoCero(medico));
	
		if(estadoCuentaForm.getEstado().equals("imprimirResumidoItemDetSol")){
			if(estadoCuenta.despachoEquivalentes(con, Integer.parseInt(estadoCuentaForm.getNumeroSolicitud()), estadoCuentaForm.getCodigoTipoSolicitud(), Integer.parseInt(estadoCuentaForm.getIdSubCuenta()), medico.getCodigoInstitucionInt()))
			{
				 if (estadoCuentaForm.getMostrarArticPrinciDespachoCero().equals(ConstantesBD.acronimoNo))
				 {
					 oldQuery = ConsultasBirt.consultaImprimirItemEstadoCuentaEquivalente(estadoCuentaForm.getIdSubCuenta(), estadoCuentaForm.getNumeroSolicitud());
				 }else{
					 oldQuery = ConsultasBirt.consultaImprimirItemEstadoCuenta(estadoCuentaForm.getEstado(), remite, estadoCuentaForm.getIdSubCuenta(), estadoCuentaForm.getNumeroSolicitud());
				 }
					 
			}else{
				oldQuery = ConsultasBirt.consultaImprimirItemEstadoCuenta(estadoCuentaForm.getEstado(), remite, estadoCuentaForm.getIdSubCuenta(), estadoCuentaForm.getNumeroSolicitud());
			}						
		}else{
			//Según tipo de impresión se modifica la consulta
			oldQuery = ConsultasBirt.consultaImprimirItemEstadoCuenta(estadoCuentaForm.getEstado(), remite, estadoCuentaForm.getIdSubCuenta(), estadoCuentaForm.getNumeroSolicitud());
		}   
        
		logger.info("\n\n CONSULTA DESPUES DE VALIDACIONES EQUIVALENTES----->"+ oldQuery+"\n\n");
		        
        /*        
        oldQuery = ConsultasBirt.consultaImprimirItemEstadoCuenta(estadoCuentaForm.getEstado(), remite, estadoCuentaForm.getIdSubCuenta(), estadoCuentaForm.getNumeroSolicitud());        			
        logger.info("CONSULTA IMPRESIOIN=> "+oldQuery);*/
        
		//se actualiza Query
        comp.modificarQueryDataSet(oldQuery);
        
        comp.obtenerComponentesDataSet("detalleItem2");
        String oldQuery2=comp.obtenerQueryDataSet();
        
        oldQuery2=ConsultasBirt.consultaImprimirItem2EstadoCuenta(estadoCuentaForm.getIdSubCuenta());
        
        logger.info("CONSULTA IMPRESIOIN 22222=> "+oldQuery2);
        
		//se actualiza Query
        if(estadoCuentaForm.getEstado().equals("imprimirResumidoItemAF"))
        {
        	oldQuery2=ConsultasBirt.consultaImprimirItem2EstadoCuenta(estadoCuentaForm.getIdSubCuenta());
        	
        }
        else
        {
        	oldQuery2=ConsultasBirt.consultaImprimirItem2EstadoCuenta(ConstantesBD.codigoNuncaValido+"");
        }
        	
        comp.modificarQueryDataSet(oldQuery2);
        
        comp.lowerAliasDataSet();
        //********************CONSULTA DE LOS TOTALES******************************************************************************
		estadoCuenta.calcularTotalesCargos(
			con, 
			estadoCuentaForm.getIdSubCuenta(), 
			estadoCuentaForm.getIdCuenta(), 
			paciente.getCodigoPersona()+"", 
			estadoCuentaForm.getEsConvenioPoliza(),
			estadoCuentaForm.getCodigoTipoRegimen(),
			medico.getCodigoInstitucionInt(),
			paciente.getCodigoIngreso()
		);
		estadoCuentaForm.llenarFormTotales(estadoCuenta);
		
		comp.obtenerComponentesDataSet("detalleItem");
		boolean resultadoconsulta=estadoCuenta.resultadosConsulta(con, comp.obtenerQueryDataSet());
		
		if(!resultadoconsulta)
		{
			if(estadoCuentaForm.getEstado().equals("imprimirResumidoItemAF"))
	        {
	        	estadoCuentaForm.setValorTotalCargos("");
	        	estadoCuentaForm.setValorTotalConvenio("");
	        	estadoCuentaForm.setValorTotalPaciente("");
	        	estadoCuentaForm.setValorTotalAbonos("");
	        }
		}
        
        //*******************SE INGRESAN LOS TOTALES*******************************************************
        Vector v = new Vector();
        v.add("Valor Total de Cargos:");
        v.add(estadoCuentaForm.getValorTotalCargos());
        
        //Si no es particular se muestra el valor del convenio
        if(!estadoCuentaForm.getCodigoTipoRegimen().equals(ConstantesBD.codigoTipoRegimenParticular+""))
        {
        	if(UtilidadTexto.getBoolean(estadoCuentaForm.getEsConvenioPoliza()))
        		v.add("Saldo Monto Autorizado:");
        	else
        		v.add("Valor Total a Cargo del Convenio:");
            v.add(estadoCuentaForm.getValorTotalConvenio());
        }
        
        v.add("Valor Total a Cargo del Paciente:");
        v.add(estadoCuentaForm.getValorTotalPaciente());
        
        v.add("Valor Total Abonos del Paciente:");
        v.add(estadoCuentaForm.getValorTotalAbonos());
        
        //Si hay devolucion, se muestra, dde lo contrario se muestra el neto a cargo del paciente
        if(!estadoCuentaForm.getValorDevolucionPaciente().equals(""))
        {
        	v.add("Valor Devolución al Paciente:");
            v.add(estadoCuentaForm.getValorDevolucionPaciente());
        }
        else
        {
        	v.add("Valor Neto a Cargo del Paciente:");
            v.add(estadoCuentaForm.getValorNetoPaciente());
        }
        
        //Si es particular se muestra las ultimas celdas en blanco
        if(estadoCuentaForm.getCodigoTipoRegimen().equals(ConstantesBD.codigoTipoRegimenParticular+""))
        {
        	v.add("");
            v.add("");
        }
        comp.insertLabelInGridOfBodyPage(2, 0, v);
        
        //SE ENVIAN LOS ATRIBUTOS AL JSP PARA IMPRIMIR******************************************************
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	
		String newPathReport = comp.saveReport1(false);
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        //SE ACTUALIZAN LOS PARAMETROS DE CONEXION CON LA BD***************************************************
        comp.updateJDBCParameters(newPathReport);
        UtilidadBD.closeConnection(con);
        if(estadoCuentaForm.getEstado().equals("imprimirResumidoItemAF") || estadoCuentaForm.getEstado().equals("imprimirResumidoItem"))
        	return mapping.findForward("listadoConvenios");
        if(estadoCuentaForm.getEstado().equals("imprimirResumidoItemDetSol"))
        	return mapping.findForward("detalleSolicitud");
        
        return mapping.findForward("listadoSolicitudes");
	}


	/**
	 * Método que realiza la busqueda avanzada de solicitudes
	 * @param con
	 * @param estadoCuentaForm
	 * @param mapping
	 * @param estadoCuenta 
	 * @param medico 
	 * @return
	 */
	private ActionForward accionBuscar(Connection con, EstadoCuentaForm estadoCuentaForm, ActionMapping mapping, EstadoCuenta estadoCuenta, UsuarioBasico medico) 
	{
		ActionErrors errores = new ActionErrors();
		//***********SE REALIZAN LAS VALIDACIONES DE LOS CAMPOS***************************************
		if(!estadoCuentaForm.getBusquedaAvanzada("fechaOrden").toString().equals("")&&
			!UtilidadFecha.validarFecha(estadoCuentaForm.getBusquedaAvanzada("fechaOrden").toString()))
			errores.add("",new ActionMessage("errors.formatoFechaInvalido","de la Orden"));
		
		if(!estadoCuentaForm.getBusquedaAvanzada("fechaGrabacion").toString().equals("")&&
				!UtilidadFecha.validarFecha(estadoCuentaForm.getBusquedaAvanzada("fechaGrabacion").toString()))
				errores.add("",new ActionMessage("errors.formatoFechaInvalido","de Grabación"));
		//*********************************************************************************************
		
		if(errores.isEmpty())
		{
			//Si no hay errores se llama la busqueda de solicitudes
			estadoCuentaForm.setSolicitudes(estadoCuenta.buscarSolicitudesSubCuenta(
				con, 
				estadoCuentaForm.getIdIngreso(),
				estadoCuentaForm.getCodigoConvenio(),
				estadoCuentaForm.getIdSubCuenta(), 
				estadoCuentaForm.getBusquedaAvanzada("tipoSolicitud").toString(), 
				estadoCuentaForm.getBusquedaAvanzada("orden").toString(), 
				estadoCuentaForm.getBusquedaAvanzada("fechaOrden").toString(), 
				estadoCuentaForm.getBusquedaAvanzada("fechaGrabacion").toString(), 
				estadoCuentaForm.getBusquedaAvanzada("estadoMedico").toString(), 
				estadoCuentaForm.getBusquedaAvanzada("centroCostoSolicitante").toString(), 
				estadoCuentaForm.getBusquedaAvanzada("centroCostoSolicitado").toString(),
				estadoCuentaForm.getBusquedaAvanzada("estadofacturacion").toString()
			));
			//Se toma el número de registros del listado de solicitudes
			estadoCuentaForm.setNumSolicitudes(Integer.parseInt(estadoCuentaForm.getSolicitudes("numRegistros").toString()));
			//Se actualiza el max page items
			estadoCuentaForm.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(medico.getCodigoInstitucionInt()));
			estadoCuentaForm.setOffset(0);
			//Se consultan los detalles de las primeras solicitudes
					
				for(int i=0;(i<estadoCuentaForm.getNumSolicitudes()&&i<estadoCuentaForm.getMaxPageItems());i++)
					estadoCuentaForm.setSolicitudes("detalleOrden_"+i, estadoCuenta.cargarDetalleServicioArticuloSolicitud(con, Integer.parseInt(estadoCuentaForm.getSolicitudes("numeroSolicitud_"+i).toString()), Integer.parseInt(estadoCuentaForm.getSolicitudes("codigoTipoSolicitud_"+i).toString()), Integer.parseInt(estadoCuentaForm.getIdSubCuenta()), medico.getCodigoInstitucionInt()));
						
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoSolicitudes");
	}


	/**
	 * Método que realiza la ordenación del listado de solicitudes
	 * @param con
	 * @param estadoCuentaForm
	 * @param mapping
	 * @param estadoCuenta 
	 * @param medico 
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, EstadoCuentaForm estadoCuentaForm, ActionMapping mapping, EstadoCuenta estadoCuenta, UsuarioBasico medico) 
	{
		///columnas del listado
		String[] indices = {
				"numeroSolicitud_",
				"orden_",
				"codigoTipoSolicitud_",
				"nombreTipoSolicitud_",
				"fechaGrabacion_",
				"fechaSolicitud_",
				"estadoMedico_",
				"centroCostoSolicitante_",
				"centroCostoSolicitado_",
				"esPyp_",
				"valor_",
				"recargo_",
				"iva_",
				"nombreViaIngreso_",
				"tieneincluidos_",
				"detalleOrden_"
			};

		
		//Se pasa la fecha a formato BD
		for(int i=0;i<estadoCuentaForm.getNumSolicitudes();i++)
		{
			estadoCuentaForm.setSolicitudes("fechaGrabacion_"+i,UtilidadFecha.conversionFormatoFechaABD(estadoCuentaForm.getSolicitudes("fechaGrabacion_"+i).toString()));
			estadoCuentaForm.setSolicitudes("fechaSolicitud_"+i,UtilidadFecha.conversionFormatoFechaABD(estadoCuentaForm.getSolicitudes("fechaSolicitud_"+i).toString()));
		}
		
		estadoCuentaForm.setSolicitudes(Listado.ordenarMapa(indices,
				estadoCuentaForm.getIndice(),
				estadoCuentaForm.getUltimoIndice(),
				estadoCuentaForm.getSolicitudes(),
				estadoCuentaForm.getNumSolicitudes()));
		
		///Se pasa la fecha a formato Aplicacion
		for(int i=0;i<estadoCuentaForm.getNumSolicitudes();i++)
		{
			estadoCuentaForm.setSolicitudes("fechaGrabacion_"+i,UtilidadFecha.conversionFormatoFechaAAp(estadoCuentaForm.getSolicitudes("fechaGrabacion_"+i).toString()));
			estadoCuentaForm.setSolicitudes("fechaSolicitud_"+i,UtilidadFecha.conversionFormatoFechaAAp(estadoCuentaForm.getSolicitudes("fechaSolicitud_"+i).toString()));
		}
		
		
		estadoCuentaForm.setSolicitudes("numRegistros",estadoCuentaForm.getNumSolicitudes()+"");
		
		estadoCuentaForm.setUltimoIndice(estadoCuentaForm.getIndice());
		
		for(int i=estadoCuentaForm.getOffset();(i<estadoCuentaForm.getNumSolicitudes()&&i<(estadoCuentaForm.getMaxPageItems()+estadoCuentaForm.getOffset()));i++)
						
				if(estadoCuentaForm.getSolicitudes("detalleOrden_"+i)==null)
					estadoCuentaForm.setSolicitudes("detalleOrden_"+i, estadoCuenta.cargarDetalleServicioArticuloSolicitud(con, Integer.parseInt(estadoCuentaForm.getSolicitudes("numeroSolicitud_"+i).toString()), Integer.parseInt(estadoCuentaForm.getSolicitudes("codigoTipoSolicitud_"+i).toString()), Integer.parseInt(estadoCuentaForm.getIdSubCuenta()), medico.getCodigoInstitucionInt()));
					
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoSolicitudes");
	}


	/**
	 * Métodop que realiza la redireccion del listado de solicitudes
	 * @param con
	 * @param estadoCuentaForm
	 * @param response
	 * @param mapping 
	 * @param request 
	 * @param estadoCuenta 
	 * @param medico 
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, EstadoCuentaForm estadoCuentaForm, HttpServletResponse response, HttpServletRequest request, ActionMapping mapping, EstadoCuenta estadoCuenta, UsuarioBasico medico) 
	{
		try
		{
			//Se consultan los detalles de las solicitudes que faltan
			for(int i=estadoCuentaForm.getOffset();(i<estadoCuentaForm.getNumSolicitudes()&&i<(estadoCuentaForm.getMaxPageItems()+estadoCuentaForm.getOffset()));i++)
								
					if(estadoCuentaForm.getSolicitudes("detalleOrden_"+i)==null)
						estadoCuentaForm.setSolicitudes("detalleOrden_"+i, estadoCuenta.cargarDetalleServicioArticuloSolicitud(con, Integer.parseInt(estadoCuentaForm.getSolicitudes("numeroSolicitud_"+i).toString()), Integer.parseInt(estadoCuentaForm.getSolicitudes("codigoTipoSolicitud_"+i).toString()), Integer.parseInt(estadoCuentaForm.getIdSubCuenta()), medico.getCodigoInstitucionInt(), estadoCuentaForm.getCodigoConvenio()));
								
		    UtilidadBD.closeConnection(con);
			response.sendRedirect(estadoCuentaForm.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion : "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en EstadoCuentaAction", "errors.problemasDatos", true);
		}
	}


	/**
	 * Método implementado para realizar la consulta del detalle de la solicitud
	 * @param con
	 * @param estadoCuentaForm
	 * @param mapping
	 * @param estadoCuenta
	 * @param request 
	 * @return
	 */
	private ActionForward accionDetalleSolicitud(Connection con, EstadoCuentaForm estadoCuentaForm, ActionMapping mapping, EstadoCuenta estadoCuenta, HttpServletRequest request, UsuarioBasico medico) 
	{
		//Se llenan los datos de la solicitud
		int pos = estadoCuentaForm.getPosicion();
		double totalGeneralCargado = 0, totalGeneralRecargo = 0, totalGeneralDescuento = 0;
		estadoCuentaForm.setNumeroSolicitud(estadoCuentaForm.getSolicitudes("numeroSolicitud_"+pos).toString());
		estadoCuentaForm.setCodigoTipoSolicitud(Integer.parseInt(estadoCuentaForm.getSolicitudes("codigoTipoSolicitud_"+pos).toString()));
		estadoCuentaForm.setNombreTipoSolicitud(estadoCuentaForm.getSolicitudes("nombreTipoSolicitud_"+pos).toString());
		estadoCuentaForm.setConsecutivoOrden(estadoCuentaForm.getSolicitudes("orden_"+pos).toString());
		
		//*********SEGUN TIPO DE SOLICITUD SE CONSULTA EL DETALLE**************************************
		//* CIRUGIAS ----------------------------------------------------------------------------------
		if(estadoCuentaForm.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudCirugia)
		{
			return this.accionDetalleSolicitudCirugia(con, estadoCuentaForm, mapping, request, estadoCuenta);
		}
		//* DEMAS TIPOS DE SOLICITUD -------------------------------------------------------------------
		else    
		{
			
			 /**Control de cambio Anexo de la cuenta (3463)
			  * desarrollador: leoquico
			  * fecha: 23-Abril-2013
			 * */
				 
			 //1. Validacion para tipo de solicitud medicamentos y cargos directos medicamentos
			 String tipoCodigo = "";
			 //2. Consultar parametrizacion Convenios tipo de articulo
			
			 int tipoConvenio = UtilidadesFacturacion.consultarTipoConvenioArticulo(con, estadoCuentaForm.getCodigoConvenio());
			 //3. realizar consulta tipo convenio
		     if(tipoConvenio >= 0 ){
		    	 if(tipoConvenio == 0 ){
		    	//flujo tipo codigo CUM
		    		tipoCodigo = "CUM";
		    	}
				if(tipoConvenio == 1){
				   	 //flujo normal
					tipoCodigo = "AXM";
				}
			 }
			 else
			 {
			 //4. Valiadar parametrizacion por el codigo manual estandar 
			 InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			 String codigoManual = ValoresPorDefecto.getCodigoManualEstandarBusquedaArticulosLargo((institucion.getCodigoInstitucionBasica()));
			 String [] codigoManualAbv = codigoManual.split("@@");
			 codigoManual = codigoManualAbv[0];
			 if( codigoManual.equals("INZ")){
			   //mostrar codigo interfaz 
				tipoCodigo= "INZ";
			 }
			 if(codigoManual.equals("AXM")){
			   //flujo normal
				tipoCodigo = "AXM";
			 }
			} 
		     /**
				 * Validar: 
				 * 1. Si la solicitud tiene despacho de equivalentes 
				 * 2. El estado del sparametro "Mostrar en las solicitudes art&iacute;los principales con despacho CERO S/R?"
				 * @author Diana Ruiz 		
				 */ 		
				
				estadoCuentaForm.setMostrarArticPrinciDespachoCero(validarParametroMostrarArtiPrinciDespachoCero(medico));
			if(estadoCuenta.despachoEquivalentes(con, Integer.parseInt(estadoCuentaForm.getNumeroSolicitud()), estadoCuentaForm.getCodigoTipoSolicitud(), Integer.parseInt(estadoCuentaForm.getIdSubCuenta()), medico.getCodigoInstitucionInt()))
			{
				 if (estadoCuentaForm.getMostrarArticPrinciDespachoCero().equals(ConstantesBD.acronimoNo))
				 {
					 estadoCuentaForm.setDetalleSolicitud(
								estadoCuenta.cargarDetalleCargoArticuloConEquivalente(
									con, 
									estadoCuentaForm.getIdCuenta(), 
									estadoCuentaForm.getCodigoConvenio(), 
									estadoCuentaForm.getNumeroSolicitud(), 
									estadoCuentaForm.getIdSubCuenta(),
									tipoCodigo
								)
							);	
			
				 }else {
					 estadoCuentaForm.setDetalleSolicitud(
								estadoCuenta.cargarDetalleCargoServicioArticulo(
									con, 
									estadoCuentaForm.getIdCuenta(), 
									estadoCuentaForm.getCodigoConvenio(), 
									estadoCuentaForm.getNumeroSolicitud(), 
									estadoCuentaForm.getIdSubCuenta(),
									tipoCodigo
								)
							);
				 }					 
			}else {
					 
		        //tipo de solicitud procedimiento
				//Sumatoria de los Totales para ser mostrados en una tercera fila en la jsp
				if(estadoCuentaForm.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudProcedimiento)
				{
					if(Utilidades.convertirAEntero(estadoCuentaForm.getSolicitudes("codigoPortatil_"+pos)+"") != ConstantesBD.codigoNuncaValido)
					{
						for(int i=0; i < Utilidades.convertirAEntero(estadoCuentaForm.getDetalleSolicitud("numRegistros")+""); i++)
						{
							totalGeneralCargado = totalGeneralCargado + Utilidades.convertirADouble(estadoCuentaForm.getDetalleSolicitud("totalCargo_"+i)+"");
							totalGeneralRecargo = totalGeneralRecargo + Utilidades.convertirADouble(estadoCuentaForm.getDetalleSolicitud("totalRecargo_"+i)+"");
							totalGeneralDescuento = totalGeneralDescuento + Utilidades.convertirADouble(estadoCuentaForm.getDetalleSolicitud("totalDescuento_"+i)+"");
						}
						estadoCuentaForm.setDetalleSolicitud("totalGeneralCargado", totalGeneralCargado);
						estadoCuentaForm.setDetalleSolicitud("totalGeneralRecargo", totalGeneralRecargo);
						estadoCuentaForm.setDetalleSolicitud("totalGeneralDescuento", totalGeneralDescuento);
						estadoCuentaForm.setDetalleSolicitud("totalGeneral", "Total Exámen - Portátil");
					}
				}
				
				/**Control de cambio Anexo de la cuenta (3463)
				 * desarrollador: leoquico
				 * fecha: 23-Abril-2013
				* */
			//1. Validacion para tipo de solicitud medicamentos y cargos directos medicamentos

			if(estadoCuentaForm.getCodigoTipoSolicitud() == ConstantesBD.codigoTipoSolicitudMedicamentos 
			 	 || estadoCuentaForm.getCodigoTipoSolicitud() == ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos){
						 
				 //2. Carga detalle para los medicamentos

			     estadoCuentaForm.setDetalleSolicitud(
							estadoCuenta.cargarDetalleCargoServicioArticulo(
								con, 
								estadoCuentaForm.getIdCuenta(), 
								estadoCuentaForm.getCodigoConvenio(), 
								estadoCuentaForm.getNumeroSolicitud(), 
								estadoCuentaForm.getIdSubCuenta(),
								tipoCodigo
							)
						);
			   }
			   else
			   {
			   	 //tipo de solicitud diferente a medicamento			 
				 //flujo normal
				 estadoCuentaForm.setDetalleSolicitud(
						estadoCuenta.cargarDetalleCargoServicioArticulo(
							con, 
							estadoCuentaForm.getIdCuenta(), 
							estadoCuentaForm.getCodigoConvenio(), 
							estadoCuentaForm.getNumeroSolicitud(), 
							estadoCuentaForm.getIdSubCuenta(),
							""
						)
					);
				}				
			}
			//Se verifica si la solicitud es de servicios o de articulos
			if(estadoCuentaForm.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos||
					estadoCuentaForm.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudMedicamentos)
				estadoCuentaForm.setServicio(false);
			else
				estadoCuentaForm.setServicio(true);
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("detalleSolicitud");
		}
		//**********************************************************************************************
		
		
	}


	/**
	 * Método que consulta las solicitudes de un convenio
	 * @param con
	 * @param estadoCuentaForm
	 * @param mapping
	 * @param estadoCuenta
	 * @param medico 
	 * @param paciente 
	 * @return
	 */
	
	
	
	/**
	 * Este M&eacute;todo permite validar el parametro "Mostrar en las solicitudes art&iacute;los principales con despacho CERO S/R?"
	 * @param usuario
	 * @author Diana Ruiz
	 * @return String
	 */
	private String validarParametroMostrarArtiPrinciDespachoCero(UsuarioBasico medico)
	{
		int institucion = medico.getCodigoInstitucionInt();
		String  mostrarArticPrinciDespachoCero= ValoresPorDefecto.getMostrarAdminMedicamentosArticulosDespachoCero(institucion); 
		return mostrarArticPrinciDespachoCero;
	}

		
	private ActionForward accionListadoSolicitudes(Connection con, EstadoCuentaForm estadoCuentaForm, ActionMapping mapping, EstadoCuenta estadoCuenta, UsuarioBasico medico, PersonaBasica paciente) throws IPSException 
	{
		//Se reinicia mapa de busqueda avanzada
		estadoCuentaForm.setBusquedaAvanzada(new HashMap<String, Object>());
		
		//Se actualiza el max page items
		estadoCuentaForm.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(medico.getCodigoInstitucionInt()));
		estadoCuentaForm.setOffset(0);
		
		//**********************CONSULTA DE LAS SOLICITUDES***************************************************************************
		//Se consulta el listado de solicituddes
		estadoCuentaForm.setSolicitudes(estadoCuenta.cargarTodasSolicitudesSubCuenta(con, estadoCuentaForm.getIdIngreso(),estadoCuentaForm.getCodigoConvenio(),estadoCuentaForm.getIdSubCuenta()));
		//Se toma el número de registros del listado de solicitudes
		estadoCuentaForm.setNumSolicitudes(Integer.parseInt(estadoCuentaForm.getSolicitudes("numRegistros").toString()));
		//Se consultan los detalles de las primeras solicitudes
		
		/**
		 * Validar: 
		 * 1. Si la solicitud tiene despacho de equivalentes 
		 * 2. El estado del parametro "Mostrar en las solicitudes art&iacute;los principales con despacho CERO S/R?"
		 * @author Diana Ruiz 		
		 */ 		
		
		estadoCuentaForm.setMostrarArticPrinciDespachoCero(validarParametroMostrarArtiPrinciDespachoCero(medico));		
		
		for(int i=0;(i<estadoCuentaForm.getNumSolicitudes()&&i<estadoCuentaForm.getMaxPageItems());i++){
	            if(estadoCuenta.despachoEquivalentes(con, Integer.parseInt(estadoCuentaForm.getSolicitudes(
	                    "numeroSolicitud_"+i).toString()), Integer.parseInt(estadoCuentaForm.getSolicitudes(
	                            "codigoTipoSolicitud_"+i).toString()), Integer.parseInt(estadoCuentaForm.getIdSubCuenta()), medico.getCodigoInstitucionInt())){
	                if (estadoCuentaForm.getMostrarArticPrinciDespachoCero().equals(ConstantesBD.acronimoNo))
	                {                               
	                    estadoCuentaForm.setSolicitudes("detalleOrden_"+i, estadoCuenta.cargarDetalleServicioArticuloSolicitudPopUp(con, Integer.parseInt(estadoCuentaForm.getSolicitudes("numeroSolicitud_"+i).toString()), Integer.parseInt(estadoCuentaForm.getSolicitudes("codigoTipoSolicitud_"+i).toString()), Integer.parseInt(estadoCuentaForm.getIdSubCuenta()), medico.getCodigoInstitucionInt(), estadoCuentaForm.getCodigoConvenio()));
	                }
	                else{
	                    estadoCuentaForm.setSolicitudes("detalleOrden_"+i, estadoCuenta.cargarDetalleServicioArticuloSolicitud(con, Integer.parseInt(estadoCuentaForm.getSolicitudes("numeroSolicitud_"+i).toString()), Integer.parseInt(estadoCuentaForm.getSolicitudes("codigoTipoSolicitud_"+i).toString()), Integer.parseInt(estadoCuentaForm.getIdSubCuenta()), medico.getCodigoInstitucionInt(), estadoCuentaForm.getCodigoConvenio()));
	                }                           
	            }else{
	                estadoCuentaForm.setSolicitudes("detalleOrden_"+i, estadoCuenta.cargarDetalleServicioArticuloSolicitud(con, Integer.parseInt(estadoCuentaForm.getSolicitudes("numeroSolicitud_"+i).toString()), Integer.parseInt(estadoCuentaForm.getSolicitudes("codigoTipoSolicitud_"+i).toString()), Integer.parseInt(estadoCuentaForm.getIdSubCuenta()), medico.getCodigoInstitucionInt(), estadoCuentaForm.getCodigoConvenio()));
	            }
	        }		
		
		//***************************************************************************************************************************
		
		//********************CONSULTA DE LOS TOTALES******************************************************************************
		estadoCuenta.calcularTotalesCargos(
			con, 
			estadoCuentaForm.getIdSubCuenta(), 
			estadoCuentaForm.getIdCuenta(), 
			paciente.getCodigoPersona()+"", 
			estadoCuentaForm.getEsConvenioPoliza(),
			estadoCuentaForm.getCodigoTipoRegimen(),
			medico.getCodigoInstitucionInt(),
			Integer.parseInt(estadoCuentaForm.getIdIngreso())
		);
		estadoCuentaForm.llenarFormTotales(estadoCuenta);
		//*************************************************************************************************************************
		
		int codigoContrato=UtilidadesFacturacion.obtenerContratoSubCuenta(con,estadoCuentaForm.getIdSubCuenta());
		Contrato contrato=new Contrato();
		contrato.cargar(con, codigoContrato+"");
		String fechaValEsquemas=estadoCuenta.obtenerFechaValidacionEsquemas(con,estadoCuentaForm.getIdSubCuenta());
		estadoCuentaForm.setEsquemasInventario(Contrato.obtenerEsquemasTarifariosInventariosVigentesFecha(con, codigoContrato+"", fechaValEsquemas));
		estadoCuentaForm.setEsquemasProcedimientos(contrato.obtenerEsquemasTarifariosProcedimientosVigentesFecha(con, codigoContrato+"", fechaValEsquemas));
		
		//***********SE INICIALIZAN DATOS PARA LA BUSQUEDA AVANZADA*******************************
		estadoCuentaForm.setBusquedaAvanzada("seccionBusquedaAvanzada", ConstantesBD.acronimoNo);
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoSolicitudes");
	}

	/**
	 * Método que carga el listado de los convenios asociados al ingreso
	 * @param con
	 * @param estadoCuentaForm
	 * @param mapping
	 * @param estadoCuenta
	 * @param paciente 
	 * @param medico 
	 * @return
	 */
	private ActionForward accionListadoConvenios(
			Connection con, 
			EstadoCuentaForm estadoCuentaForm, 
			ActionMapping mapping, 
			EstadoCuenta estadoCuenta, 
			UsuarioBasico medico, 
			PersonaBasica paciente) throws IPSException 
	{
		logger.info("ID INGRESOOO CUENTAA>>>>>>>>>>>>>"+estadoCuentaForm.getIdIngreso());
		//Se consultan los convenios del ingreso
		estadoCuentaForm.setListadoConvenios(estadoCuenta.cargarTodosConvenioIngreso(con, estadoCuentaForm.getIdIngreso()));
		
		//Si solo hay un convenio , se redirecciona la salida directo al detalle de las solicitudes
		if(estadoCuentaForm.getListadoConvenios().size()==1)
		{
			HashMap<String, Object> elemento = (HashMap<String, Object>)estadoCuentaForm.getListadoConvenios().get(0);
			
			estadoCuentaForm.setIdSubCuenta(elemento.get("idSubCuenta").toString());
			estadoCuentaForm.setCodigoConvenio(elemento.get("codigoConvenio").toString());
			estadoCuentaForm.setNombreConvenio(elemento.get("nombreConvenio").toString());
			estadoCuentaForm.setCodigoTipoRegimen(elemento.get("codigoTipoRegimen").toString());
			estadoCuentaForm.setNombreEstratoSocial(elemento.get("nombreEstratoSocial").toString());
			estadoCuentaForm.setNombreMontoCobro(elemento.get("nombreMontoCobro").toString());
			estadoCuentaForm.setNombreTipoMonto(elemento.get("nombreTipoMonto").toString());
			estadoCuentaForm.setCodigoNaturaleza(elemento.get("codigoNaturaleza").toString());
			estadoCuentaForm.setNombreNaturaleza(elemento.get("nombreNaturaleza").toString());
			estadoCuentaForm.setEsConvenioPoliza(elemento.get("esConvenioPoliza").toString());			
			
			//Se redirecciona al listado de solicitudes
			estadoCuentaForm.setEstado("listadoSolicitudes");
			return accionListadoSolicitudes(con, estadoCuentaForm, mapping, estadoCuenta, medico, paciente);
			
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoConvenios");
	}

	/**
	 * Método implementado para consultar el detalle de una orden de cirugia
	 * @param con
	 * @param estadoCuentaForm
	 * @param mapping
	 * @param request
	 * @param estadoCuenta 
	 * @return
	 */
	private ActionForward accionDetalleSolicitudCirugia(Connection con, EstadoCuentaForm estadoCuentaForm, ActionMapping mapping, HttpServletRequest request, EstadoCuenta estadoCuenta) 
	{
		//****CARGA DE ENCABEZADO SOLICITUD***********
		Solicitud solicitud = new Solicitud();
		try
		{
			solicitud.cargar(con,Integer.parseInt(estadoCuentaForm.getNumeroSolicitud()));
		}
		catch(SQLException e)
		{
			logger.error("Hubo error aqui=> "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.problemasBd", "errors.problemasBd", true);
		}
		estadoCuentaForm.setDetalleSolicitud("orden",solicitud.getConsecutivoOrdenesMedicas()+"");
		estadoCuentaForm.setDetalleSolicitud("fechaOrden",solicitud.getFechaSolicitud());
		//estadoCuentaForm.setDetalleSolicitud("autorizacion",solicitud.getNumeroAutorizacion());
		estadoCuentaForm.setDetalleSolicitud("horaOrden",solicitud.getHoraSolicitud());
		estadoCuentaForm.setDetalleSolicitud("consecutivoFactura",UtilidadesFacturacion.obtenerConsecutivoFacturaXSolicitud(con, estadoCuentaForm.getIdSubCuenta(), estadoCuentaForm.getNumeroSolicitud()));
		
		
		//****CARGAR DETALLE CIRUGÍAS DE LA SOLICITUD***********
		SolicitudesCx solicitudCx = new SolicitudesCx();
		HashMap servicioSolicitud = solicitudCx.cargarServiciosXSolicitudCx(con,estadoCuentaForm.getNumeroSolicitud(),false);
		double totalOrden = 0;
		double totalCirugia;
		
		for(int i=0;i<Integer.parseInt(servicioSolicitud.get("numRegistros").toString());i++)
		{
			//Se cargan las cirugias
			estadoCuentaForm.setDetalleSolicitud("numeroServicio_"+i, servicioSolicitud.get("numeroServicio_"+i));
			estadoCuentaForm.setDetalleSolicitud("descripcionServicio_"+i, servicioSolicitud.get("descripcionServicio_"+i));
			estadoCuentaForm.setDetalleSolicitud("nombreEsquemaTarifario_"+i, servicioSolicitud.get("nombreEsquemaTarifario_"+i));
			estadoCuentaForm.setDetalleSolicitud("nombreTipoCirugia_"+i, servicioSolicitud.get("nombreTipoCirugia_"+i));
			estadoCuentaForm.setDetalleSolicitud("nombreEspecialidadInterviene_"+i, servicioSolicitud.get("nombreEspecialidadInterviene_"+i));
			estadoCuentaForm.setDetalleSolicitud("grupoUvr_"+i, servicioSolicitud.get("grupoUvr_"+i));
			estadoCuentaForm.setDetalleSolicitud("nombreViaCx_"+i, servicioSolicitud.get("nombreViaCx_"+i));
			estadoCuentaForm.setDetalleSolicitud("liquidarServicio_"+i, servicioSolicitud.get("liquidarServicio_"+i));
			
			//Se cargan los asocios de cada cirugía
			HashMap asociosSolicitud = estadoCuenta.cargarDetalleCargoCirugia(
				con, 
				estadoCuentaForm.getNumeroSolicitud(), 
				estadoCuentaForm.getIdSubCuenta(), 
				servicioSolicitud.get("codigoServicio_"+i).toString(),
				false);
			
			totalCirugia = 0;
			
			for(int j=0;j<Integer.parseInt(asociosSolicitud.get("numRegistros").toString());j++)
			{
				estadoCuentaForm.setDetalleSolicitud("totalCargo_"+i+"_"+j, asociosSolicitud.get("totalCargo_"+j));
				estadoCuentaForm.setDetalleSolicitud("fechaCargo_"+i+"_"+j, asociosSolicitud.get("fechaCargo_"+j));
				estadoCuentaForm.setDetalleSolicitud("estadoCargo_"+i+"_"+j, asociosSolicitud.get("estado_"+j));
				estadoCuentaForm.setDetalleSolicitud("nombreAsocio_"+i+"_"+j, asociosSolicitud.get("nombreAsocio_"+j));
				estadoCuentaForm.setDetalleSolicitud("nombreProfesional_"+i+"_"+j, asociosSolicitud.get("nombreProfesional_"+j));
				estadoCuentaForm.setDetalleSolicitud("nombreServicioAsocio_"+i+"_"+j, asociosSolicitud.get("nombreServicioAsocio_"+j).toString().toLowerCase());
				if(Integer.parseInt(asociosSolicitud.get("codigoEstado_"+j).toString())==ConstantesBD.codigoEstadoFCargada)
					totalCirugia += Double.parseDouble(asociosSolicitud.get("totalCargo_"+j).toString());
			}
			
			estadoCuentaForm.setDetalleSolicitud("valorCirugia_"+i, UtilidadTexto.formatearValores(totalCirugia));
			estadoCuentaForm.setDetalleSolicitud("numAsocios_"+i, asociosSolicitud.get("numRegistros"));
			totalOrden += totalCirugia;
			
		}
		
		estadoCuentaForm.setDetalleSolicitud("numCirugias", servicioSolicitud.get("numRegistros"));
		
		//****CARGA DE HOJA QUIRÚRGICA ***********************
		LiquidacionServicios mundoLiquidacion = new LiquidacionServicios();
		mundoLiquidacion.setCon(con);
		mundoLiquidacion.setNumeroSolicitud(estadoCuentaForm.getNumeroSolicitud());
		mundoLiquidacion.cargarDetalleOrden();
		
		estadoCuentaForm.setDetalleSolicitud("fechaInicial",mundoLiquidacion.getDatosActoQx().get("fechaInicialCx"));
		estadoCuentaForm.setDetalleSolicitud("horaInicial",mundoLiquidacion.getDatosActoQx().get("horaInicialCx"));
		estadoCuentaForm.setDetalleSolicitud("fechaFinal",mundoLiquidacion.getDatosActoQx().get("fechaFinalCx"));
		estadoCuentaForm.setDetalleSolicitud("horaFinal",mundoLiquidacion.getDatosActoQx().get("horaFinalCx"));
		try
		{
			String[] duracion = mundoLiquidacion.getDatosActoQx().get("duracionCirugia").toString().split(":");
			estadoCuentaForm.setDetalleSolicitud("duracion",duracion[0]+" Horas "+duracion[1]+" Minutos");
		}
		catch(Exception e)
		{
			estadoCuentaForm.setDetalleSolicitud("duracion","");
		}
		
		estadoCuentaForm.setDetalleSolicitud("sala",mundoLiquidacion.getDatosActoQx().get("codigoSala"));
		estadoCuentaForm.setDetalleSolicitud("nombreSala",mundoLiquidacion.getDatosActoQx().get("nombreSala"));
		estadoCuentaForm.setDetalleSolicitud("politrauma",mundoLiquidacion.getDatosActoQx().get("politraumatismo"));
		
		//***CARGA DE HOJA DE ANESTESIA*********************
		estadoCuentaForm.setDetalleSolicitud("nombreTipoAnestesia",mundoLiquidacion.getDatosActoQx().get("nombreTipoAnestesia"));
		estadoCuentaForm.setDetalleSolicitud("codigoTipoAnestesia",mundoLiquidacion.getDatosActoQx().get("codigoTipoAnestesia"));
		estadoCuentaForm.setDetalleSolicitud("codigoAnestesiologo",mundoLiquidacion.getDatosActoQx().get("codigoAnestesiologo"));
		estadoCuentaForm.setDetalleSolicitud("nombreAnestesiologo",mundoLiquidacion.getDatosActoQx().get("politraumatismo"));
		
		//***SE CARGAN MATERIALES ESPECIALES**************************
		estadoCuentaForm.setMaterialesEspeciales(UtilidadesSalas.obtenerListadoMaterialesEspeciales(con, estadoCuentaForm.getNumeroSolicitud(),estadoCuentaForm.getIdSubCuenta(),false,false));
		
		for(HashMap<String, Object> elemento:estadoCuentaForm.getMaterialesEspeciales())
			totalOrden += Double.parseDouble(elemento.get("valorTotal").toString());
		
		estadoCuentaForm.setDetalleSolicitud("valorOrden", UtilidadTexto.formatearValores(totalOrden));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleSolicitudCx");
	}

	/**
	 * Método que maneja la primera acción del usuario al acceder a la
	 * funcionalidad, empezar. En esta funcionalidad simplemente se carga el
	 * listado de las cuentas
	 * @param mapping
	 * @param estadoCuentaForm
	 * @param con
	 * @param paciente
	 * @param estadoCuenta
	 * @param medico 
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionEmpezar(ActionMapping mapping,EstadoCuentaForm estadoCuentaForm,Connection con, PersonaBasica paciente, EstadoCuenta estadoCuenta, UsuarioBasico medico)
	{
		estadoCuentaForm.reset(medico.getCodigoInstitucionInt());
		estadoCuentaForm.setListadoCuentas(estadoCuenta.cargarTodasCuentaPaciente(con, paciente.getCodigoPersona()));
		
		//Se cargan las estructuras para la busqueda avanzada
		estadoCuentaForm.setTiposSolicitud(RevisionCuenta.consultarTiposSolicitud(con));
		estadoCuentaForm.setEstadosMedicos(RevisionCuenta.consultarEstadosSolicitudHistoriaC(con));
		estadoCuentaForm.setCentrosCosto(Utilidades.obtenerCentrosCosto(con, medico.getCodigoInstitucionInt(), "", false, 0,false));
		estadoCuentaForm.setEstadosFacturacion(RevisionCuenta.consultarEstadosSolFactura(con));

		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoCuentas");
	}	
}