package com.princetonsa.action.facturasVarias;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadN2T;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturasVarias.GenModFacturasVariasForm;
import com.princetonsa.dto.consultaExterna.DtoMultasCitas;
import com.princetonsa.dto.facturasVarias.DtoDeudor;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ValidacionesFacturaOdontologica;
import com.princetonsa.mundo.facturasVarias.AprobacionAnulacionFacturasVarias;
import com.princetonsa.mundo.facturasVarias.Deudores;
import com.princetonsa.mundo.facturasVarias.GenModFacturasVarias;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturasvarias.FacturasVariasMundoFabrica;
import com.servinte.axioma.mundo.interfaz.facturasvarias.IFacturasVariasMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITurnoDeCajaMundo;
import com.servinte.axioma.orm.HistoricoEncabezado;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * autor: juan Sebastian castaño
 * modificado: Sebastián Gómez
 */

public class GenModFacturasVariasAction extends Action {
	
	/**
	 * Log para el manejo de errores y excepciones presentados en la clase
	 */
	Logger logger = Logger.getLogger(GenModFacturasVariasAction.class);
	
	/**
	 * Mensajes parametrizados de error.
	 */
	private MessageResources messageResource = MessageResources
			.getMessageResources("com.servinte.mensajes.tesoreria.generacionFacturasVariasForm");
	
	
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if (form instanceof GenModFacturasVariasForm) 
			{
				GenModFacturasVariasForm formGenModFacturasVarias=(GenModFacturasVariasForm) form;

				// recuperar el estado actual
				String estado=formGenModFacturasVarias.getEstado();		


				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				ActionErrors errores = new ActionErrors();
				//System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n ************ " + ValoresPorDefecto.getTipoConsecutivoManejar(usuario.getCodigoInstitucionInt()));

				logger.info("\n\nESTADO GENERACION MODIFICACION FACTURAS VARIAS >>>>>>>>>>"+formGenModFacturasVarias.getEstado()+"\n\n");

				//forma.setMensaje(new ResultadoBoolean(false));
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de GenModFacturasVarias (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				// verificar que el parametro del tipo de consecutivo a manejar en el modulo de facturas varias este parametrizado y/o que exista.
				else if(ValoresPorDefecto.getTipoConsecutivoManejar(usuario.getCodigoInstitucionInt()).equals(""))
				{				
					//return ComunAction.accionSalirCasoError(mapping, request, con,logger, "Falta definir el parametro de 'Tipo de Consecutivo a Manejar'","errors.paciente.noCargado", true);
					return ComunAction.accionSalirCasoError(mapping, request, con,logger, "Falta definir el parametro de 'Tipo de Consecutivo a Manejar'","Falta definir el parametro de 'Tipo de Consecutivo a Manejar'. Por favor verifique", false);
				}
				//************************ESTADOS INGRESAR FACTURA***************************************
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,formGenModFacturasVarias,mapping,usuario,request,errores);

				}
				else if(estado.equals("cargarDeudor"))
				{
					return accionCargarDeudor(con,formGenModFacturasVarias,mapping, usuario);
				}
				else if (estado.equals("buscarMultas"))
				{
					return accionBuscarMultas(con,formGenModFacturasVarias,mapping);
				}
				else if (estado.equals("asignarMultas"))
				{
					return accionAsignarMultas(con,formGenModFacturasVarias,mapping);
				}
				else if (estado.equals("recargar"))
				{
					//Refrescar el formulario principal para mostrar las multas
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				// estado de guardado de facturas
				else if (estado.equals("guardar"))
				{

					return accionGuardar(con, formGenModFacturasVarias, mapping, usuario, request, response);
				}
				//Método implementado para borrar las multas por AJAX
				else if (estado.equals("eliminarMultas"))
				{
					formGenModFacturasVarias.setMultasAsignadas(new ArrayList<DtoMultasCitas>());
					UtilidadBD.closeConnection(con);
					return null;
				}
				else if(estado.equals("eliminarMulta"))
				{
					return accionEliminarMulta(con,formGenModFacturasVarias,mapping);
				}
				//eSTADO DE IMPRESOION
				else if (estado.equals("imprimirGuardar"))
				{

					return accionImprimirReporteGuardar(con, formGenModFacturasVarias, mapping, request, usuario);   
				}
				//********************ESTADOS BUSQUEDA FACTURAS***********************************************
				/// inicia la busqueda facturas varias
				else if (estado.equals("initBuscar"))
				{
					formGenModFacturasVarias.setTipDeudor("");
					formGenModFacturasVarias.setDatosDeudor(new DtoDeudor());

					return mapping.findForward("principal");
				}
				// buscar facturas varias
				else if (estado.equals("buscar") || estado.equals("volverBuscar"))
				{

					return accionBuscarFacturasVarias(con, formGenModFacturasVarias, mapping, usuario);
				}
				else if (estado.equals("ordenar"))
				{
					return accionOrdenar(con,formGenModFacturasVarias,mapping);
				}
				/// cargar la factura seleccionada en la plantilla de modificacion e impresion
				else if (estado.equals("facturaSelecCargPlantilla")||estado.equals("cargarSelectEnModificacion"))
				{
					return accionFacturaSelecCargaPlantilla(con,formGenModFacturasVarias,mapping);


				}
				else if(estado.equals("cargarDeudorModificar"))
				{

					return accionCargarDeudorModificar(con,formGenModFacturasVarias,mapping);
				}
				else if(estado.equals("cargarDeudorBusqueda"))
				{
					return accionCargarDeudorBusqueda(con,formGenModFacturasVarias,mapping);
				}
				else if(estado.equals("eliminarMultaModificacion"))
				{

					return accionEliminarMultaModificacion(con,formGenModFacturasVarias,mapping);
				}
				//eSTADO DE IMPRESOION
				else if (estado.equals("imprimir"))
				{

					return accionImprimirReporte(con, formGenModFacturasVarias, mapping, request, usuario);   
				}
				/// guardar los datos modificados
				else if (estado.equals("guardarModificado"))
				{

					return accionGuardarModificado(con, formGenModFacturasVarias, mapping, usuario, request);

				}else if (estado.equals("volverDesdeRecibosCaja")){

					/*
					 * Este volver hace que se regrese a la funcionalidad de Facturas Varias
					 * desde la funcionalidad Recibos de Caja
					 */

					formGenModFacturasVarias.setEstado("guardar");
					return mapping.findForward("principal");
				}

			}else
			{
				logger.error("El form no es compatible con el form de GenModFacturasVariasForm");
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
	
	
	private ActionForward accionImprimirReporteGuardar(Connection con, GenModFacturasVariasForm formGenModFacturasVarias, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) 
	{
		
    	/*String nombreRptDesign = "GenModFacturasvarias.rptdesign";
    	
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturasVarias/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,5);
        Vector v = new Vector();
        v.add(ins.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+"  -  "+ins.getNit());
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        String i=formGenModFacturasVarias.getPosicionMapa();
        
        // nro factura
        String variableImpresion = "Factura No. "+formGenModFacturasVarias.getCodigo() ;
        // agregar variable al reporte        
        comp.insertLabelInGridPpalOfHeader(3,0, variableImpresion);
        
        // valor
        variableImpresion = "Valor: "+formGenModFacturasVarias.getValorFactura();
        // agregar variable al reporte
        comp.insertLabelInGridPpalOfHeader(3,1, variableImpresion);
        
             
        // valor
        variableImpresion = "Estado : "+ValoresPorDefecto.getIntegridadDominio(formGenModFacturasVarias.getEstadoFactura());
        // agregar variable al reporte
        comp.insertLabelInGridPpalOfHeader(4,0, variableImpresion);
        
        
        //valor
        variableImpresion = "Fecha Elaboración: "+formGenModFacturasVarias.getFecha();
        // agregar variable al reporte
        comp.insertLabelInGridPpalOfHeader(4,1, variableImpresion);
        
        
        // crear mundo para la ejecucion de la consulta de la info del concepto de facturas varias
        GenModFacturasVarias mundo = new GenModFacturasVarias();
        
        //valor
        variableImpresion = "Concepto: "+formGenModFacturasVarias.getNombreConcepto();
        // agregar variable al reporte
        comp.insertLabelInGridPpalOfHeader(5,0, variableImpresion);
        
           
        
        	
    	// 	valor
        variableImpresion = "Deudor: "+formGenModFacturasVarias.getDatosDeudor().getRazonSocial();
        // agregar variable al reporte 
        comp.insertLabelInGridPpalOfHeader(6,0, variableImpresion);
    	
    	 // valor
        variableImpresion = "Dirección: "+formGenModFacturasVarias.getDatosDeudor().getDireccion();
        // agregar variable al reporte
        comp.insertLabelInGridPpalOfHeader(7,0, variableImpresion);
        
        
        //valor
        variableImpresion = "Teléfono: "+formGenModFacturasVarias.getDatosDeudor().getTelefono();
        // agregar variable al reporte
        comp.insertLabelInGridPpalOfHeader(7,1, variableImpresion);
        
        
       
        
        
        
        //valor
        variableImpresion = "Observaciones: "+formGenModFacturasVarias.getObservaciones();
        // agregar variable al reporte
        comp.insertLabelInGridPpalOfHeader(8,0, variableImpresion);
        
        
        //valor
        variableImpresion = "Valor en Letras: "+UtilidadN2T.convertirLetras(Utilidades.convertirADouble(formGenModFacturasVarias.getValorFactura(), true));
        // agregar variable al reporte
        comp.insertLabelInGridPpalOfHeader(10,0, variableImpresion);
        
        
        
        // agregar variable al reporte
        comp.insertLabelInGridPpalOfHeader(12,1, "");
        
        //valor
        if(formGenModFacturasVarias.getEstadoFactura().equals(ConstantesIntegridadDominio.acronimoEstadoAprobado))
        {
        	variableImpresion = "Firma Aprobación ("+formGenModFacturasVarias.getFechaAprobacion()+") ";
        }
        else
        {
        	variableImpresion = "Firma Aprobación ";
        }
        // agregar variable al reporte
        comp.insertLabelInGridPpalOfHeader(13,1, variableImpresion);
        
        */
        
        /*
        comp.insertLabelInGridOfMasterPage(1,1,factura);
        comp.insertLabelInGridOfMasterPage(2,1,factura);
        */
        /*comp.insertGridHeaderOfMasterPage(1,0,1,1);
        comp.insertLabelInGridOfMasterPage(2, 0, factura);
        */
        
        
        /*
        comp.insertGridInBodyPageWithName(1, 1, 1, 1, "grilla");
        comp.insertLabelInGridOfBodyPage(1, 1, factura);*/
        
        
        /*
        comp.insertGridHeaderOfMasterPageWithName(1, 1, 1,1, "grilla");
        comp.insertLabelInGridOfMasterPage(1, 1, factura);
        */
        /*
        comp.insertGridHeaderOfMasterPage(2,0,1,5);
        factura.add("valor");//formGenModFacturasVarias.getNroFactura());
        comp.insertLabelInGridOfMasterPage(2,0,factura);
        */
        
        /*
        
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
    	//cambiar al estado original de carga de registro en plantilla de modificacion*/
//		String nombreRptDesign = "" ;
//		if(Utilidades.convertirAEntero(ValoresPorDefecto.getFormatoFacturaVaria(usuario.getCodigoInstitucionInt()))==ConstantesBD.codigoFormatoImpresionEstandar) // carta
//		{
//			nombreRptDesign = "ConsultaImpresionFacturasVarias.rptdesign";
//		}
//		if(Utilidades.convertirAEntero(ValoresPorDefecto.getFormatoFacturaVaria(usuario.getCodigoInstitucionInt()))==ConstantesBD.codigoFormatoImpresionSonria) // POS
//		{
//			nombreRptDesign = "ConsultaImpresionFacturasVariasPOS.rptdesign";
//		}
//			
//	
//		
		
		//Informacion del Cabezote
//		DesignEngineApi comp;
//		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturasVarias/",nombreRptDesign);
//		
//		int cantidadFilas=3;
//			
//		Vector v = new Vector();
//		
//		if(formGenModFacturasVarias.getEstadoFactura().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
//		{
//			v.add(formGenModFacturasVarias.getEstadoFactura());
//			cantidadFilas=4;
//		}
//		String tipoId=institucion.getTipoIdentificacion();
//		if(institucion.getTipoIdentificacion().equals("NI"))
//		{
//			tipoId=institucion.getDescripcionTipoIdentificacion();
//		}
//		v.add(institucion.getRazonSocial());
//		v.add(tipoId+" "+institucion.getNit());
//		v.add("Sucursal: "+usuario.getCentroAtencion());
//	
//		comp.insertGridHeaderOfMasterPageWithName(0, 1, 1, cantidadFilas, "titulo");
//		
//		comp.insertLabelInGridOfMasterPage(0, 1, v);

//		boolean manejaMultiInstitucion= UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt()));
//		int codigoInstitucion=0;
//		
//		if(manejaMultiInstitucion){
//			codigoInstitucion= Utilidades.convertirAEntero(institucion.getCodigo());
//		}
//		else{
//			codigoInstitucion= usuario.getCodigoInstitucionInt();
//		}
//		
//		comp.obtenerComponentesDataSet("ConsultaFacturasVarias");
//		String newquery=ConsultasBirt.impresionFacturaVaria(codigoInstitucion,Utilidades.convertirAEntero(formGenModFacturasVarias.getCodigo()), manejaMultiInstitucion);
//		comp.modificarQueryDataSet(newquery);
//		
//		//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
//		comp.lowerAliasDataSet();
//		String newPathReport = comp.saveReport1(false);
//		comp.updateJDBCParameters(newPathReport);
//		// se mandan los parámetros al reporte
//		newPathReport += "&institucion="+codigoInstitucion+"&consecutivoFactura="+Utilidades.convertirAEntero(formGenModFacturasVarias.getCodigo());
//		
//		
//		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
//			
//			//Informacion del Cabezote
//	        DesignEngineApi comp;
//	        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturasVarias/",nombreRptDesign);
//	        
//	        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
//	        comp.insertGridHeaderOfMasterPage(0,1,1,5);
//	        
//	        Vector v = new Vector();
//	        v.add(institucion.getRazonSocial());
//	        v.add(usuario.getCentroAtencion());
//	        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
//	        v.add(institucion.getDireccion());
//	        v.add("Tels. "+institucion.getTelefono());
//	        comp.insertLabelInGridOfMasterPage(0, 1, v);
//	        
//	        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
//	        comp.insertLabelInGridPpalOfFooter(0, 1, "Fecha: "+UtilidadFecha.getFechaActual());
//	        
//	        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
//	      	comp.lowerAliasDataSet();
//			String newPathReport = comp.saveReport1(false);
//	        comp.updateJDBCParameters(newPathReport);
//	        // se mandan los parámetros al reporte
//	        newPathReport += "&institucion="+usuario.getCodigoInstitucion()+"&consecutivoFactura="+Utilidades.convertirAEntero(formGenModFacturasVarias.getCodigo());
//	        logger.info(newPathReport);
	        
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		GenModFacturasVarias genModFacturasVariasMundo = new GenModFacturasVarias();
	
		String newPathReport = genModFacturasVariasMundo.imprimirFacturaVaria(usuario.getCodigoInstitucionInt(), Utilidades.convertirAEntero(institucion.getCodigo()),
														Utilidades.convertirAEntero(formGenModFacturasVarias.getCodigo()), usuario);
		
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
		
		formGenModFacturasVarias.setEstado("guardar");
		return mapping.findForward("principal");
	}


	/**
	 * Método para eliminar una multa en la modificacion
	 * @param con
	 * @param formGenModFacturasVarias
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarMultaModificacion(Connection con,GenModFacturasVariasForm formGenModFacturasVarias,	ActionMapping mapping) 
	{
		// cambiar al estado original de carga de registro en plantilla de modificacion
		formGenModFacturasVarias.setEstado("facturaSelecCargPlantilla");
		double valorFactura = 0;
		for(DtoMultasCitas multa:formGenModFacturasVarias.getMultasAsignadas())
		{
			if(multa.isSeleccionado())
			{
				valorFactura += Utilidades.convertirADouble(multa.getValorString(), true);
			}
		}
		logger.info("VALOR FACTURA: "+valorFactura);
		formGenModFacturasVarias.setMapaBusquedaFacVarias("valorFactura_"+formGenModFacturasVarias.getPosicionMapa(), valorFactura);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}


	/**
	 * Método implementado para elimianr una multa
	 * @param con
	 * @param formGenModFacturasVarias
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarMulta(Connection con,GenModFacturasVariasForm formGenModFacturasVarias,	ActionMapping mapping) 
	{
		double valorFactura = 0;
		for(DtoMultasCitas multa:formGenModFacturasVarias.getMultasAsignadas())
		{
			if(multa.isSeleccionado())
			{
				valorFactura += Utilidades.convertirADouble(multa.getValorString(), true);
			}
		}
		logger.info("VALOR FACTURA: "+valorFactura);
		formGenModFacturasVarias.setValorFactura(valorFactura+"");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}


	/**
	 * Método implementado para cargar el deudor en la modificacion
	 * @param con
	 * @param formGenModFacturasVarias
	 * @param mapping
	 * @return
	 */
	private ActionForward accionCargarDeudorModificar(Connection con,GenModFacturasVariasForm formGenModFacturasVarias,	ActionMapping mapping) 
	{
		formGenModFacturasVarias.setDatosDeudor(Deudores.cargar(con, formGenModFacturasVarias.getDatosDeudor().getCodigo()));
		//Se inicia el concepto al cambiar el deudor
		formGenModFacturasVarias.setMapaBusquedaFacVarias("concepto_"+formGenModFacturasVarias.getPosicionMapa(),ConstantesBD.codigoNuncaValido+"");
		formGenModFacturasVarias.setMultasAsignadas(new ArrayList<DtoMultasCitas>());
		///Se reestablece el estado
		formGenModFacturasVarias.setEstado("facturaSelecCargPlantilla");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}


	/**
	 * Método para cargar el deudor de la busqueda avanzada
	 * @param con
	 * @param formGenModFacturasVarias
	 * @param mapping
	 * @return
	 */
	private ActionForward accionCargarDeudorBusqueda(Connection con,
			GenModFacturasVariasForm formGenModFacturasVarias,
			ActionMapping mapping) 
	{
		formGenModFacturasVarias.setDatosDeudorBusqueda(Deudores.cargar(con, formGenModFacturasVarias.getDatosDeudorBusqueda().getCodigo()));
		//Se inicia el concepto al cambiar el deudor
		formGenModFacturasVarias.setConcepto(ConstantesBD.codigoNuncaValido+"");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}


	private ActionForward accionFacturaSelecCargaPlantilla(Connection con,GenModFacturasVariasForm formGenModFacturasVarias,ActionMapping mapping) 
	{
		
		if(formGenModFacturasVarias.getEstado().equals("facturaSelecCargPlantilla"))
		{
			String posMapa = formGenModFacturasVarias.getPosicionMapa();
			//Se reedita el concepto de la factura agregando el tipo
			formGenModFacturasVarias.setMapaBusquedaFacVarias(
				"concepto_"+posMapa, 
				formGenModFacturasVarias.getMapaBusquedaFacVarias("concepto_"+posMapa)+ConstantesBD.separadorSplit+formGenModFacturasVarias.getMapaBusquedaFacVarias("tipoConcepto_"+posMapa)
				);
				
			//Se carga la informacion del deudor
			formGenModFacturasVarias.setDatosDeudor(Deudores.cargar(con, formGenModFacturasVarias.getMapaBusquedaFacVarias("deudor_"+posMapa).toString()));
			
			GenModFacturasVarias mundoFacturasVarias = new GenModFacturasVarias();
			formGenModFacturasVarias.setMultasAsignadas(mundoFacturasVarias.obtenerMultasFactura(con, formGenModFacturasVarias.getMapaBusquedaFacVarias("codigoFacVar_"+posMapa).toString()));
		}
		
		// cambiar al estado original de carga de registro en plantilla de modificacion
		formGenModFacturasVarias.setEstado("facturaSelecCargPlantilla");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}


	/**
	 * Método para ordenar el listado de facturas encontradas
	 * @param con
	 * @param formGenModFacturasVarias
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con,GenModFacturasVariasForm formGenModFacturasVarias,ActionMapping mapping) 
	{
		String[] indices = (String[]) formGenModFacturasVarias.getMapaBusquedaFacVarias("INDICES");
		int numRegistros = Utilidades.convertirAEntero(formGenModFacturasVarias.getMapaBusquedaFacVarias("numRegistros")+"");
		
		formGenModFacturasVarias.setMapaBusquedaFacVarias(
			Listado.ordenarMapa(
				indices, 
				formGenModFacturasVarias.getPatronOrdenar(), 
				formGenModFacturasVarias.getUltimoPatron(), 
				formGenModFacturasVarias.getMapaBusquedaFacVarias(), 
				numRegistros));
		formGenModFacturasVarias.setMapaBusquedaFacVarias("numRegistros", numRegistros);
		formGenModFacturasVarias.setMapaBusquedaFacVarias("INDICES", indices);
		formGenModFacturasVarias.setUltimoPatron(formGenModFacturasVarias.getPatronOrdenar());
		//Se reestablece eñ estado
		formGenModFacturasVarias.setEstado("buscar");
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}


	/**
	 * Método para iniciar el flujo de la generacion modioficacion de la factura varia
	 * @param con
	 * @param formGenModFacturasVarias
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param errores 
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con,
			GenModFacturasVariasForm formGenModFacturasVarias,
			ActionMapping mapping, UsuarioBasico usuario,
			HttpServletRequest request, ActionErrors errores) 
	{
		
		
		
		// inicializar el formulario
		formGenModFacturasVarias.reset();	
		
		//validacion de mensaje de caja abierta 
		validarCaja(con, formGenModFacturasVarias, mapping, usuario, request, errores);
		
		// cargar el el select de centros de atencion por defecto el centro atencion en sesion, aunque este sera reelegible
		formGenModFacturasVarias.setCentroAtencion(String.valueOf(usuario.getCodigoCentroAtencion()));
		
		// cargar el centro de costo del usuario en sesion
		formGenModFacturasVarias.setCentroCosto(String.valueOf(usuario.getCodigoCentroCosto()));
		
		
		//TurnoDeCaja turnoDeCaja = getMovimientosCajaServicio().obtenerTurnoCajaAbiertoPorCajaCajero(forma.getCajero(), forma.getCaja().getConsecutivo(), usuario.getCodigoCentroAtencion());
		
		
		// cargar la fecha actual
		if (formGenModFacturasVarias.getFecha().equals(""))
		{
			formGenModFacturasVarias.setFecha(UtilidadFecha.getFechaActual(con));
		}
		//validacion si tiene definido el consecutivo
		errores=ValidadExistenciaConsecutivo(con,usuario);
		if(!errores.isEmpty())
		{
			logger.info("\n entre a guardar los errores");
			saveErrors(request, errores);
			return ComunAction.accionSalirCasoError(mapping, request, con,logger, "Falta definir el consecutivo de facturas Varias.","Falta definir el consecutivo de facturas Varias. Por favor verifique", false);
			
		}	
		//Se verifica si el usuario tiene permisos para la funcionalidad de aprobacion/anulacion facturas varias
		if(Utilidades.tieneRolFuncionalidad(con, usuario.getLoginUsuario(), 736))
		{
			formGenModFacturasVarias.setTieneRolAprobacionFactura(true);
		}
		else
		{
			formGenModFacturasVarias.setTieneRolAprobacionFactura(false);
		}
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}


	private ActionForward accionAsignarMultas(Connection con,
			GenModFacturasVariasForm formGenModFacturasVarias,
			ActionMapping mapping) 
	{
		formGenModFacturasVarias.setMultasAsignadas(new ArrayList<DtoMultasCitas>());
		double valorTotal = 0;
		
		for(DtoMultasCitas multa:formGenModFacturasVarias.getMultasEncontradas())
		{
			if(multa.isSeleccionado())
			{
				formGenModFacturasVarias.getMultasAsignadas().add(multa);
				valorTotal += Utilidades.convertirADouble(multa.getValorString(), true);
			}
		}
		logger.info("valor total de la factura: "+valorTotal);
		formGenModFacturasVarias.setValorFactura(valorTotal+"");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("multasPaciente");
	}


	/**
	 * Método implementado para buscar las multas del paciente
	 * @param con
	 * @param formGenModFacturasVarias
	 * @param mapping
	 * @return
	 */
	private ActionForward accionBuscarMultas(Connection con,GenModFacturasVariasForm formGenModFacturasVarias,	ActionMapping mapping) 
	{
		formGenModFacturasVarias.setMultasEncontradas(new ArrayList<DtoMultasCitas>());
		GenModFacturasVarias facturaVaria = new GenModFacturasVarias();
		formGenModFacturasVarias.setMultasEncontradas(facturaVaria.obtenerMultasPaciente(con, formGenModFacturasVarias.getDatosDeudor().getCodigoPaciente()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("multasPaciente");
	}


	/**
	 * Método para cargar el deudor elegido
	 * @param con
	 * @param formGenModFacturasVarias
	 * @param mapping
	 * @param string 
	 * @param usuario.getCodigoInstitucionInt() 
	 * @return
	 */
	private ActionForward accionCargarDeudor(Connection con,GenModFacturasVariasForm formGenModFacturasVarias,ActionMapping mapping, UsuarioBasico usuario) 
	{
		DtoDeudor deudor = new DtoDeudor();
		
		//Si es solo paciente pero no existe aun como deudor
		//se cargan los datos desde la informacion de persona (issue 129)
		if(formGenModFacturasVarias.getDatosDeudor().isExistePacienteNoDeudor()){
			deudor = Deudores.cargarPacienteNoDeudor(formGenModFacturasVarias.getDatosDeudor().getCodigo(), usuario);
		}
		//Si ya es deudor se carga la información ya guardada en deudores
		else{
			deudor = Deudores.cargar(con, formGenModFacturasVarias.getDatosDeudor().getCodigo());
		}
		
		formGenModFacturasVarias.setDatosDeudor(deudor);
		
		//Se inicia el concepto al cambiar el deudor
		formGenModFacturasVarias.setConcepto(ConstantesBD.codigoNuncaValido+"");
		formGenModFacturasVarias.setMultasAsignadas(new ArrayList<DtoMultasCitas>());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}


	public  ActionErrors ValidadExistenciaConsecutivo (Connection connection,UsuarioBasico usuario)
	{
		logger.info("\n entre a ValidadExistenciaConsecutivo");
		ActionErrors errores = new ActionErrors();
		GenModFacturasVarias mundoFacturas = new GenModFacturasVarias();
		
		String nombreConsecutivo=mundoFacturas.obtenerNombreConsecutivoFacturasVarias(usuario);
		String consecutivo=UtilidadBD.obtenerValorActualTablaConsecutivos(connection,nombreConsecutivo, usuario.getCodigoInstitucionInt());
		logger.info("\n\n valor del consecutivo ");
		if (Utilidades.convertirAEntero(consecutivo)<0)
			errores.add("",new ActionMessage("errors.noExiste","El consecutivo de facturas varias"));
		
		return errores;
	}
		
	private ActionForward accionImprimirReporte(Connection con, GenModFacturasVariasForm formGenModFacturasVarias, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) {
		
    	String nombreRptDesign = "GenModFacturasvarias.rptdesign";
    	
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturasVarias/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,5);
        Vector v = new Vector();
        v.add(ins.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+"  -  "+ins.getNit());
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        
        String i=formGenModFacturasVarias.getPosicionMapa();
        
        // nro factura
        String variableImpresion = "Factura No. "+formGenModFacturasVarias.getMapaBusquedaFacVarias("consecutivo_"+formGenModFacturasVarias.getPosicionMapa()) ;
        // agregar variable al reporte        
        comp.insertLabelInGridPpalOfHeader(3,0, variableImpresion);
        
        // valor
        variableImpresion = "Valor: "+formGenModFacturasVarias.getMapaBusquedaFacVarias("valorFactura_"+formGenModFacturasVarias.getPosicionMapa());
        // agregar variable al reporte
        comp.insertLabelInGridPpalOfHeader(3,1, variableImpresion);
        
             
        // valor
        variableImpresion = "Estado : "+ValoresPorDefecto.getIntegridadDominio(formGenModFacturasVarias.getMapaBusquedaFacVarias("estadoFactura_"+formGenModFacturasVarias.getPosicionMapa())+"");
        // agregar variable al reporte
        comp.insertLabelInGridPpalOfHeader(4,0, variableImpresion);
        
        
        //valor
        variableImpresion = "Fecha Elaboración: "+formGenModFacturasVarias.getMapaBusquedaFacVarias("fecha_"+formGenModFacturasVarias.getPosicionMapa());
        // agregar variable al reporte
        comp.insertLabelInGridPpalOfHeader(4,1, variableImpresion);
        
        
        // crear mundo para la ejecucion de la consulta de la info del concepto de facturas varias
        GenModFacturasVarias mundo = new GenModFacturasVarias();
        
        //valor
        variableImpresion = "Concepto: "+formGenModFacturasVarias.getMapaBusquedaFacVarias("nomConcepto_"+formGenModFacturasVarias.getPosicionMapa());
        // agregar variable al reporte
        comp.insertLabelInGridPpalOfHeader(5,0, variableImpresion);
        
    	// 	valor
        variableImpresion = "Deudor: "+formGenModFacturasVarias.getDatosDeudor().getRazonSocial();
        // agregar variable al reporte 
        comp.insertLabelInGridPpalOfHeader(6,0, variableImpresion);
    	
    	 // valor
        variableImpresion = "Dirección: "+formGenModFacturasVarias.getDatosDeudor().getDireccion();
        // agregar variable al reporte
        comp.insertLabelInGridPpalOfHeader(7,0, variableImpresion);
        
        //valor
        variableImpresion = "Teléfono: "+formGenModFacturasVarias.getDatosDeudor().getTelefono();
        // agregar variable al reporte
        comp.insertLabelInGridPpalOfHeader(7,1, variableImpresion);
        
        
        //valor
        variableImpresion = "Observaciones: "+formGenModFacturasVarias.getObservaciones();
        // agregar variable al reporte
        comp.insertLabelInGridPpalOfHeader(8,0, variableImpresion);
        
        
        //valor
        variableImpresion = "Valor en Letras: "+UtilidadN2T.convertirLetras(Utilidades.convertirADouble(formGenModFacturasVarias.getMapaBusquedaFacVarias("valorFactura_"+formGenModFacturasVarias.getPosicionMapa())+"", true));
        // agregar variable al reporte
        comp.insertLabelInGridPpalOfHeader(10,0, variableImpresion);
        
        
        // agregar variable al reporte
        comp.insertLabelInGridPpalOfHeader(12,1, "");
        
        //valor
        if((formGenModFacturasVarias.getMapaBusquedaFacVarias("estadoFactura_"+formGenModFacturasVarias.getPosicionMapa())+"").equals(ConstantesIntegridadDominio.acronimoEstadoAprobado))
        {
        	variableImpresion = "Firma Aprobación ("+formGenModFacturasVarias.getMapaBusquedaFacVarias("fechaAprobacion_"+formGenModFacturasVarias.getPosicionMapa())+") ";
        }
        else
        {
        	variableImpresion = "Firma Aprobación ";
        }
        // agregar variable al reporte
        comp.insertLabelInGridPpalOfHeader(13,1, variableImpresion);
        
        
        
        /*
        comp.insertLabelInGridOfMasterPage(1,1,factura);
        comp.insertLabelInGridOfMasterPage(2,1,factura);
        */
        /*comp.insertGridHeaderOfMasterPage(1,0,1,1);
        comp.insertLabelInGridOfMasterPage(2, 0, factura);
        */
        
        
        /*
        comp.insertGridInBodyPageWithName(1, 1, 1, 1, "grilla");
        comp.insertLabelInGridOfBodyPage(1, 1, factura);*/
        
        
        /*
        comp.insertGridHeaderOfMasterPageWithName(1, 1, 1,1, "grilla");
        comp.insertLabelInGridOfMasterPage(1, 1, factura);
        */
        /*
        comp.insertGridHeaderOfMasterPage(2,0,1,5);
        factura.add("valor");//formGenModFacturasVarias.getNroFactura());
        comp.insertLabelInGridOfMasterPage(2,0,factura);
        */
        
        
        
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
    	//cambiar al estado original de carga de registro en plantilla de modificacion
		formGenModFacturasVarias.setEstado("facturaSelecCargPlantilla");
		return mapping.findForward("principal");
	}


	
	
	/**
	 * Metodo de guardado de los cambios realizados a un registro de facturas varias
	 * @param con
	 * @param formGenModFacturasVarias
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionGuardarModificado(Connection con, GenModFacturasVariasForm formGenModFacturasVarias, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) {
		
		ActionErrors errores = new ActionErrors();		
		int codigoDeudor=ConstantesBD.codigoNuncaValido;
		// crear el nuevo objeto del mundo
		GenModFacturasVarias mundo = new GenModFacturasVarias();
		
			
		// Guardar Modificaciones
		
		// llenar mundo con los datos
	
		logger.info("\n codigo Deudor -->"+codigoDeudor);
		if (codigoDeudor>0)
			formGenModFacturasVarias.setMapaBusquedaFacVarias("deudor_"+formGenModFacturasVarias.getPosicionMapa(),codigoDeudor);
		
		llenarMundoConsulta(mundo, usuario, formGenModFacturasVarias);
		
		// cargar consecutivo del registro modificado
		int consecutivo=Integer.parseInt(formGenModFacturasVarias.getMapaBusquedaFacVarias("consecutivo_"+formGenModFacturasVarias.getPosicionMapa())+"");
		if(consecutivo>0)
		{
			mundo.setConsecutivo(consecutivo);
			//mundo.setCodigoFacVar(formGenModFacturasVarias.getCodigo());
			mundo.setCodigoFacVar(formGenModFacturasVarias.getMapaBusquedaFacVarias("codigoFacVar_"+formGenModFacturasVarias.getPosicionMapa()).toString());
			
			// realizar Modificacion
			if (!mundo.guardarModFacturaVaria(con, usuario.getLoginUsuario()))
				errores.add("",new ActionMessage("errors.sinActualizar","el registro. Proceso Cancelado"));
			
		}
		else
		{
			errores.add("",new ActionMessage("errors.noExiste","El consecutivo de facturas varias"));			
		}
		
		
		///*************PROCESO DE APROBACIÓN AUTOMÁTICO**************************
		//Se verifica si se debe realizar la confirmación de la aprobación
		if(formGenModFacturasVarias.isConfirmarAprobacion()&&errores.isEmpty())
		{
			AprobacionAnulacionFacturasVarias aprobacion = new AprobacionAnulacionFacturasVarias();
			//Nota * En respuestaInsercion.getDescipcion() está el codigo de la factura recién insertada
			HashMap resultado = aprobacion.aprobacionFacturaVaria(con,mundo.getCodigoFacVar(),UtilidadFecha.getFechaActual(con), "", usuario.getLoginUsuario());
			if(!UtilidadTexto.getBoolean(resultado.get("exito")+""))
			{
				errores = (ActionErrors)resultado.get("error");
			}
		}
		//***********************************************************************
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);		
			//formGenModFacturasVarias.setEstado("");
			formGenModFacturasVarias.setModificacionExitosa(-1);
			UtilidadBD.abortarTransaccion(con);
		}
		else
		{
			formGenModFacturasVarias.setModificacionExitosa(1);
			UtilidadBD.finalizarTransaccion(con);
		}		
					
		UtilidadBD.closeConnection(con);
		//cambiar al estado original de carga de registro en plantilla de modificacion
		formGenModFacturasVarias.setEstado("facturaSelecCargPlantilla");
		return mapping.findForward("principal");
	
	}

	
	/*
	private ActionForward accionCargarFacturaSelecPlantilla(Connection con, GenModFacturasVariasForm formGenModFacturasVarias, ActionMapping mapping, UsuarioBasico usuario) {
		
		System.out.print("\n\n\n\n\n\n\n\n\n Llega al estado de carga de plantilla pos del mapa es : " + formGenModFacturasVarias.getPosicionMapa() + "\n\n\n\n\n\n");
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	 */
	/**
	 * .Metodo de consulta de las facturas varias creadas.
	 * @param con
	 * @param formGenModFacturasVarias
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBuscarFacturasVarias(Connection con, GenModFacturasVariasForm formGenModFacturasVarias, ActionMapping mapping, UsuarioBasico usuario) 
	{

		//crear el nuevo objeto del mundo
		GenModFacturasVarias mundo = new GenModFacturasVarias();
		
		// preparar el mundo para realizar la consulta
		this.llenarMundoParaConsulta(mundo, usuario, formGenModFacturasVarias);
		// realizar la consulta
		formGenModFacturasVarias.setMapaBusquedaFacVarias(mundo.buscarFacturasVarias(con, formGenModFacturasVarias.getFechaInicial(), formGenModFacturasVarias.getFechaFinal()));
		
		//XPLANNER 104953
		if(Integer.parseInt(formGenModFacturasVarias.getMapaBusquedaFacVarias("numRegistros").toString())==1 && formGenModFacturasVarias.getEstado().equals("buscar"))
		{
			formGenModFacturasVarias.setEstado("facturaSelecCargPlantilla");
			formGenModFacturasVarias.setPosicionMapa("0");
			return accionFacturaSelecCargaPlantilla(con, formGenModFacturasVarias, mapping);
		}
		formGenModFacturasVarias.setEstado("buscar");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Llenar mundo para consulta de facturas varias
	 * @param mundo
	 * @param usuario
	 * @param formGenModFacturasVarias
	 */
	private void llenarMundoParaConsulta (GenModFacturasVarias mundo, UsuarioBasico usuario, GenModFacturasVariasForm formGenModFacturasVarias)
	{
		/**
		 * preparar atributos, en el mundo, para ejecutar la consulta
		 */
		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
		
		if (!formGenModFacturasVarias.getNroFactura().equals(""))	
			mundo.setCodigoFacVar(formGenModFacturasVarias.getNroFactura());
		if (!formGenModFacturasVarias.getCentroAte().equals(""))
			mundo.setCentroAtencion(Integer.parseInt(formGenModFacturasVarias.getCentroAte()));
		if (!formGenModFacturasVarias.getTipDeudor().equals(""))
			mundo.setTipoDeudor(formGenModFacturasVarias.getTipDeudor());
		if (!formGenModFacturasVarias.getDatosDeudorBusqueda().getCodigo().equals(""))
			mundo.setDeudor(Integer.parseInt(formGenModFacturasVarias.getDatosDeudorBusqueda().getCodigo()));
	}
	
	
	
	/**
	 * Metodo encargado de identificar si hay cambios
	 * en el deudor
	 * @param deudores
	 * @param deudoresClone
	 * @return
	 */
	private boolean esModificacion (HashMap deudores,HashMap deudoresClone)
	{
		logger.info("\n  deudores --> "+deudores+" deudoresClone -->"+deudoresClone );
		
		if (!(deudores.get("direccion_0")+"").equals(deudoresClone.get("direccion_0")+"")
			||!(deudores.get("telefono_0")+"").equals(deudoresClone.get("telefono_0")+"")
			||!(deudores.get("email_0")+"").equals(deudoresClone.get("email_0")+"")
			||!(deudores.get("repreLegal_0")+"").equals(deudoresClone.get("repreLegal_0")+"")
			||!(deudores.get("nomContacto_0")+"").equals(deudoresClone.get("nomContacto_0")+""))
		{
			return true;
		}
		
		return false;
		
	}
	
	/**
	 * Metodo de guardado de un registro en la tabla facturas_varias
	 * @param con
	 * @param formGenModFacturasVarias
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @return
	 * @throws ParseException 
	 */
	private ActionForward accionGuardar(Connection con, GenModFacturasVariasForm formGenModFacturasVarias, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request,HttpServletResponse response) throws ParseException 
	{
		ActionErrors errores = new ActionErrors();		
		
		MessageResources message=MessageResources.getMessageResources("com.servinte.mensajes.facturasvarias.GenModFacturasVariasForm");
		
		ResultadoBoolean respuestaInsercion= new ResultadoBoolean(true,"");
		// crear el nuevo objeto del mundo
		GenModFacturasVarias mundo = new GenModFacturasVarias();
		
		//En el caso que el deudor seleccionado sea un paciente pero actualmente no se encuentre registrado
		//se crea un nuevo deudor con la informacion del paciente
		if(formGenModFacturasVarias.getDatosDeudor().isExistePacienteNoDeudor()){
			if(!Deudores.ingresar(con, formGenModFacturasVarias.getDatosDeudor()).isTrue()){
				errores.add("",new ActionMessage("errors.ingresoDatos",message.getMessage("GenModFacturasVarias.deudor")));
			}
			logger.info("Deudor creado desde Mod Facturas");
		}
		
		//Anexo 958
		String reciboAutimaticoGeneracionFV=ValoresPorDefecto.getReciboCajaAutomaticoGeneracionFacturaVaria(usuario.getCodigoInstitucionInt());
		
		if (reciboAutimaticoGeneracionFV.equals(ConstantesBD.acronimoSi))
		{
			formGenModFacturasVarias.setEstadoFactura(ConstantesIntegridadDominio.acronimoEstadoAprobado);
		}
		
		//OBTENER EL CONSECUTIVO
		//String nombreConsecutivo=mundo.obtenerNombreConsecutivoFacturasVarias(usuario);
		
		//String consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible(nombreConsecutivo, usuario.getCodigoInstitucionInt());
		
		String consecutivo = "";
		boolean consecutivoPropioFV = false;
		
		ValidacionesFacturaOdontologica validaciones= new ValidacionesFacturaOdontologica();
		
		HistoricoEncabezado historicoEncabezado = new HistoricoEncabezado();
		
		
		int tipoFuenteDatos = ConstantesBD.codigoNuncaValido;
		
		/*
		 * Se va a manejar el consecutivo Propio de Facturas Varias.
		 */
		if(ConstantesIntegridadDominio.acronimoTipoConsecutivoPropiFacturasVarias.equals(ValoresPorDefecto.getTipoConsecutivoManejar(usuario.getCodigoInstitucionInt()))){
			
			try{
				HibernateUtil.beginTransaction();
				IFacturasVariasMundo facturasVariasMundo=FacturasVariasMundoFabrica.crearFacturasVariasMundo();
				tipoFuenteDatos = facturasVariasMundo.obtenerTipoFuenteDatosFacturaVaria(usuario);
				consecutivo = UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoFacturasVarias, usuario.getCodigoInstitucionInt());
				facturasVariasMundo.obtenerDatosParametrizacionParaFacturaVaria(historicoEncabezado, tipoFuenteDatos, usuario.getCodigoCentroAtencion());
				consecutivoPropioFV = true;
				HibernateUtil.endTransaction();
			}
			catch (Exception e) {
				Log4JManager.error(e);
				HibernateUtil.abortTransaction();
			}
			
		}else{
			
			/*
			 * Se maneja un único consecutivo, por lo tanto se debe realizar la consulta
			 * directamente desde la Factura Odontológica
			 */
			tipoFuenteDatos = validaciones.obtenerTipoFuenteDatosFactura(usuario);
			consecutivo = validaciones.obtenerSiguienteConsecutivoFactura(con,  usuario, tipoFuenteDatos).toString();
			
			validaciones.obtenerDatosParametrizacionParaFactura(historicoEncabezado, tipoFuenteDatos, usuario.getCodigoCentroAtencion());
		}

		mundo.setConsecutivo(Utilidades.convertirAEntero(consecutivo));
		
		// llenar mundo con los datos
		this.llenarMundo(mundo, usuario, formGenModFacturasVarias);

		// realizar insercion
		respuestaInsercion = mundo.insertarFacturaVaria(con, usuario.getLoginUsuario(), historicoEncabezado);
	
		if(!respuestaInsercion.isTrue()){
			
			if (consecutivoPropioFV){
				
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoFacturasVarias, usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);

			}else{
				
				ArrayList<Double> listaConsecutivos= new ArrayList<Double>();
				listaConsecutivos.add(new Double(consecutivo));
				
				ValidacionesFacturaOdontologica.liberarConsecutivos(con, listaConsecutivos, usuario.getCodigoInstitucionInt());
			}
			
			errores.add("",new ActionMessage("errors.notEspecific" , respuestaInsercion.getDescripcion()));
			
		}else {
			
			if (consecutivoPropioFV){
				
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoFacturasVarias, usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
				
			}else{
				
				ArrayList<Double> listaConsecutivos= new ArrayList<Double>();
				listaConsecutivos.add(new Double(consecutivo));
				
				ValidacionesFacturaOdontologica.finalizarConsecutivos(con, listaConsecutivos, usuario.getCodigoInstitucionInt());
				
			}
		}
		
		//*************PROCESO DE APROBACIÓN AUTOMÁTICO**************************
		//Se verifica si se debe realizar la confirmación de la aprobación
		if(formGenModFacturasVarias.isConfirmarAprobacion()&&errores.isEmpty())
		{
			AprobacionAnulacionFacturasVarias aprobacion = new AprobacionAnulacionFacturasVarias();
			//Nota * En respuestaInsercion.getDescipcion() está el codigo de la factura recién insertada
			HashMap resultado = aprobacion.aprobacionFacturaVaria(con,respuestaInsercion.getDescripcion(),formGenModFacturasVarias.getFecha(), "", usuario.getLoginUsuario());
			if(!UtilidadTexto.getBoolean(resultado.get("exito")+""))
			{
				errores = (ActionErrors)resultado.get("error");
			}
		}
		//***********************************************************************
			
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			formGenModFacturasVarias.setEstado("");
			UtilidadBD.abortarTransaccion(con);
			
		}
		else
		{
			UtilidadBD.finalizarTransaccion(con);
		}		
					
		mundo.buscarFacturaVar(con, Integer.parseInt(mundo.getCodigoFacVar()), usuario.getCodigoInstitucion(), formGenModFacturasVarias);
	
		//Anexo 958
		//Si el parametro esta en No se realiza normalmente la generacion de la ftra varia, sino, se redirije a la funcionalidad de rc
		
		boolean automaticoRC=false;
		
		if (reciboAutimaticoGeneracionFV.equals(ConstantesBD.acronimoSi))
		{

			//Adicionalmente se pregunta si el usuario tiene el rol de generacion de recibos de caja
			//La 322 es el nro de la funcionalidad a la que le corresponde a RC en tesoreria
			if (Utilidades.tieneRolFuncionalidad(con, usuario.getLoginUsuario(), 322))
				automaticoRC=true;
			else
				automaticoRC=false;
			
			//Consulto las cajas que hay asociadas al usuario en sesión
			HashMap cajas=new HashMap();
			cajas=Utilidades.obtenerCajasCajero(usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt(), ConstantesBD.codigoNuncaValido, usuario.getCodigoCentroAtencion());
			int nroCajas=Utilidades.convertirAEntero(cajas.get("numRegistros")+"");
			
			//Si el usuario posee 1 o mas cajas activas peude acceder a la funcionalidad de RC
			if (nroCajas>0)
				automaticoRC=true;
			else
				automaticoRC=false;
		}
		
		
		//Pregunto por el paranmetro general y si paso todas las validaciones
		if (reciboAutimaticoGeneracionFV.equals(ConstantesBD.acronimoNo)&&!automaticoRC)
			return mapping.findForward("principal");
		
		else
		{
			try 
			{
				response.sendRedirect("../recibosCaja/recibosCaja.do?estado=empezarSeleccionFV&nroFactura="+mundo.getCodigoFacVar()+"&tipoDeudor="+formGenModFacturasVarias.getDatosDeudor().getTipoDeudor()+"&codigoDeudor="+formGenModFacturasVarias.getDatosDeudor().getCodigo()+"&conceptoFacVarias="+formGenModFacturasVarias.getConcepto().split(ConstantesBD.separadorSplit)[0]);
//				response.sendRedirect("../recibosCaja/recibosCaja.do?estado=empezarSeleccionFV&nroFactura="+consecutivo+"&tipoDeudor="+formGenModFacturasVarias.getDatosDeudor().getTipoDeudor()+"&codigoDeudor="+formGenModFacturasVarias.getDatosDeudor().getCodigo()+"&conceptoFacVarias="+ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC);
			}
			catch (IOException e)
			{
				logger.error("Error tratando de redireccionar a recibos de caja ",e);
			}
		}
		
		UtilidadBD.closeConnection(con);
		return null;
	
	}
	
	/**
	 * Cargar los datos en el mundo
	 * @param mundo
	 * @param usuario
	 * @param formGenModFacturasVarias 
	 */
	private void llenarMundo(GenModFacturasVarias mundo, UsuarioBasico usuario, GenModFacturasVariasForm formGenModFacturasVarias)
	{
		/**
		 * Cargar atributos de insercion
		 */
		// institucion
		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
		
		// estado factura
		mundo.setEstadoFactura(ConstantesIntegridadDominio.acronimoEstadoFacturaGenerada);
		// centro atencion
		mundo.setCentroAtencion(Integer.parseInt(formGenModFacturasVarias.getCentroAtencion()));
		// centro costo
		mundo.setCentroCosto(Integer.parseInt(formGenModFacturasVarias.getCentroCosto()));
		// fecha
		mundo.setFecha(Date.valueOf((UtilidadFecha.conversionFormatoFechaABD(formGenModFacturasVarias.getFecha()))));
		// concepto
		String[] datosConcepto = formGenModFacturasVarias.getConcepto().split(ConstantesBD.separadorSplit);
		mundo.setConcepto(Integer.parseInt(datosConcepto[0]));
		// valor factura
		mundo.setValorFactura(formGenModFacturasVarias.getValorFactura());
		
		// deudor
		
		mundo.setDeudor(Integer.parseInt(formGenModFacturasVarias.getDatosDeudor().getCodigo()));
		// observaciones
		mundo.setObservaciones(formGenModFacturasVarias.getObservaciones());
		
		mundo.setMultasFactura(formGenModFacturasVarias.getMultasAsignadas());
		
	}
	
	
	private void llenarMundoConsulta(GenModFacturasVarias mundo, UsuarioBasico usuario, GenModFacturasVariasForm formGenModFacturasVarias)
	{
		/**
		 * Cargar atributos de insercion
		 */
		// institucion
		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
		
		// estado factura
		mundo.setEstadoFactura(ConstantesIntegridadDominio.acronimoEstadoFacturaGenerada);
		// centro atencion
		mundo.setCentroAtencion(Utilidades.convertirAEntero(formGenModFacturasVarias.getMapaBusquedaFacVarias("centroAtencion_"+formGenModFacturasVarias.getPosicionMapa())+""));
		// centro costo
		mundo.setCentroCosto(Utilidades.convertirAEntero(formGenModFacturasVarias.getMapaBusquedaFacVarias("centroCosto_"+formGenModFacturasVarias.getPosicionMapa())+""));
		// fecha
		mundo.setFecha(Date.valueOf((UtilidadFecha.conversionFormatoFechaABD(formGenModFacturasVarias.getMapaBusquedaFacVarias("fecha_"+formGenModFacturasVarias.getPosicionMapa())+""))));
		// concepto
		String[] datosConcepto = formGenModFacturasVarias.getMapaBusquedaFacVarias("concepto_"+formGenModFacturasVarias.getPosicionMapa()).toString().split(ConstantesBD.separadorSplit);
		
		mundo.setConcepto(Utilidades.convertirAEntero(datosConcepto[0]));
		// valor factura
		mundo.setValorFactura(formGenModFacturasVarias.getMapaBusquedaFacVarias("valorFactura_"+formGenModFacturasVarias.getPosicionMapa())+"");
		
		// deudor
		mundo.setDeudor(Utilidades.convertirAEntero(formGenModFacturasVarias.getDatosDeudor().getCodigo()));
		// observaciones
		mundo.setObservaciones(formGenModFacturasVarias.getMapaBusquedaFacVarias("observaciones_"+formGenModFacturasVarias.getPosicionMapa())+"");
		
		mundo.setMultasFactura(formGenModFacturasVarias.getMultasAsignadas());
		
	}
	
	public void validarCaja(Connection con,
			GenModFacturasVariasForm formGenModFacturasVarias,
			ActionMapping mapping, UsuarioBasico usuario,
			HttpServletRequest request, ActionErrors errores){
		try{
			HibernateUtil.beginTransaction();
			ITurnoDeCajaMundo turnoDeCajaMundo =TesoreriaFabricaMundo.crearTurnoDeCajaMundo();
			formGenModFacturasVarias.setEstadoCaja(turnoDeCajaMundo.cajaAbierta(usuario.getLoginUsuario()));
			if (!formGenModFacturasVarias.getEstadoCaja()) {
				formGenModFacturasVarias.setMensajeCajaCerrada(messageResource
				.getMessage("generacion_facturas_varias_mensaje_caja_cerrada"));
			}else{
				formGenModFacturasVarias.setMensajeCajaCerrada("");
			}
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}
	}
	
	

}
