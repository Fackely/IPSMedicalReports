package com.princetonsa.action.tesoreria;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.tesoreria.ArqueosCajaForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.princetonsa.dto.tesoreria.DtoCuadreCaja;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.generadorReporte.tesoreria.impresionArqueoCaja.GeneradorReporteArqueoCajaDetallado;
import com.servinte.axioma.generadorReporte.tesoreria.impresionArqueoCaja.GeneradorReporteArqueoCajaResumido;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.impl.tesoreria.ETipoMovimiento;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.persistencia.UtilidadPersistencia;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IConsultaArqueoCierreCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio;

/**
 * Action que maneja todo lo relacionado con el Anexo 226 - Arqueos Caja.
 * 
 * Los procesos que son comunes entre los Arqueos (Arqueo caja, Arqueo Entrega parcial 
 * y Cierre Turno de Caja) se encuentran definidos en ComunMovimientosCaja y en MovimientosCajaAction
 *
 * @author Jorge Armando Agudelo Quintero 
 * @see ComunMovimientosCaja
 * @see MovimientosCajaAction
 */

public class ArqueosCaja extends ComunMovimientosCaja{

	/**
	 * M&eacute;todo que se encarga de procesar las peticiones realizadas desde la opci&oacute;n
	 * Arqueos Caja
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param movimientosCaja
	 *
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, MovimientosCaja movimientosCaja) {

		try {
			if(form instanceof ArqueosCajaForm)
			{
				ActionErrors errores=new ActionErrors();

				UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");


				ArqueosCajaForm forma=(ArqueosCajaForm) form;

				InstitucionBasica institucionBasica=(InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				if(institucionBasica!=null){

					forma.setInstitucionBasica(institucionBasica);
				}

				String estado=forma.getEstado();

				Log4JManager.info("Estado en arqueo action: "+estado);

				forma.setListaNombresReportes(new ArrayList<String>());

				/*
				 * El estado "generarArqueo" es el siguiente una vez se ingresa a la opci&oacute;n 
				 * Arqueo Caja, aca se realiza una consulta de todos los movimientos registrados para
				 * la caja en un turno espec&iacute;fico y que intervienen en el proceso de arqueo.
				 */
				if(estado.equals("generarArqueo")){
					try{
						HibernateUtil.beginTransaction();
	
						Log4JManager.info("Estado en Arqueo Action"+estado);
	
						IMovimientosCajaServicio movimientosCajaServicio = getMovimientosCajaServicio();
	
						DtoConsolidadoMovimiento consolidadoMovimientoDTO = movimientosCajaServicio.obtenerConsolidadoMovimientoArqueoCaja(movimientosCaja);
						consolidadoMovimientoDTO.setETipoMovimiento(ETipoMovimiento.ARQUEO_CAJA);
	
						boolean existenDocumentos = consolidadoMovimientoDTO.getNumeroTotalDocumentos() > 0 ? true :  false;
						forma.setExistenDocumentos(existenDocumentos);
						
						forma.setConsolidadoMovimientoDTO(consolidadoMovimientoDTO);
	
						this.cargarTiposReporte(forma);
						HibernateUtil.endTransaction();
					}catch (Exception e) {
						Log4JManager.error(e);
						HibernateUtil.abortTransaction();
					}
					return mapping.findForward("busqueda");
				}
				/*
				 * Cuando el estado es "mostrarSeccionCuadreCaja" se muestran los valores (valor sistema
				 * y valor diferencia) y se recarga la secci&oacute;n cuadre de caja
				 */
				else if(estado.equals("mostrarSeccionCuadreCaja")){

					forma.setMostrarCuadreCaja(true);

					return mapping.findForward("seccionCuadreCaja");
				}
				/*
				 * Cuando el estado es "cuadrarCaja" se procesa la informaci&oacute;n ingresada desde la p&aacute;gina de Arqueo de Caja 
				 * y se muestran los valores (valor sistema y valor diferencia) relacionados en el proceso de cuadre de caja
				 */
				else if(estado.equals("cuadrarCaja")){

					forma.setMostrarCuadreCaja(true);

					calcularCuadreCaja(forma);

					return mapping.findForward("seccionCuadreCaja");

				}
				/*
				 * Cuando el estado es "guardarArqueo" se procesa la informaci&oacute;n ingresada desde la p&aacute;gina de Arqueo de Caja 
				 * y si es posible, se realiza el proceso de registro del movimiento de Arqueo junto con el cuadre resultante
				 */
				else if(estado.equals("guardarArqueo")){

					try{
						HibernateUtil.beginTransaction();
						Log4JManager.info("Estado guardarArqueo "+estado);
	
						forma.setMostrarCuadreCaja(true);
	
						if(calcularCuadreCaja(forma)){
	
							IMovimientosCajaServicio movimientosCajaServicio = getMovimientosCajaServicio();
	
							BigDecimal consecutivo=new BigDecimal(Utilidades.convertirADouble(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoArqueosCaja, usuario.getCodigoInstitucionInt())));
	
							forma.getConsolidadoMovimientoDTO().getMovimientosCaja().setConsecutivo(consecutivo.longValue());
	
							boolean registrado = movimientosCajaServicio.guardarMovimientoCaja(forma.getConsolidadoMovimientoDTO());
	
							if(registrado){
								Connection conH=UtilidadPersistencia.getPersistencia().obtenerConexion();
								// si se insert&oacute; bien entonces se cambia el uso de los consecutivos a Si, Si
								UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH,ConstantesBD.nombreConsecutivoArqueosCaja, usuario.getCodigoInstitucionInt(), consecutivo.toString(), ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
								forma.setEstado("exitoso");
	
							}else{
								Connection con=UtilidadBD.abrirConexion();
								// si no se insert&oacute;, entonces se cambia el uso de los consecutivos a No, No
								UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoArqueosCaja, usuario.getCodigoInstitucionInt(), consecutivo.toString(), ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
								forma.setEstado("NoExitoso");
								UtilidadBD.closeConnection(con);
							}
	
						}else{
	
							forma.setEstado("NoExitoso");
						}
						HibernateUtil.endTransaction();
					}catch (Exception e) {
						Log4JManager.error(e);
						HibernateUtil.abortTransaction();
					}

					return mapping.findForward("busqueda");

				}else if(estado.equals("imprimirArqueoCaja") || estado.equals("imprimeArqueoSinCuadreCaja")){

					imprimirArqueoCaja(forma, estado);

					return mapping.findForward("consultarArqueo");
				}
				else if(estado.equals("impresion")){

					return mapping.findForward("impresionArqueoCaja");
				}
				else if(estado.equals("impresionTipoFormato"))
				{
					forma.setEstado("generarArqueo");
					if(forma.isExito())
						forma.setEstado("exitoso");	
					this.imprimirArqueoCaja(forma, request, usuario, institucionBasica);
					return mapping.findForward("busqueda");
				}
				else{ //secci&oacute;n de errores

					errores.add("estado invalido", new ActionMessage("errors.estadoInvalido"));
					errores.add("valor requerido", new ActionMessage("errors.usuarioSinRolFuncionalidad",usuario.getNombreUsuario(), "esta funcionalidad"));
					saveErrors(request, errores);
				}
			}
		} catch (Exception e) {
			Log4JManager.error("Error Arqueos caja : " + e);
			HibernateUtil.abortTransaction();
			return mapping.findForward("paginaError");
		}
		return mapping.findForward("paginaError");
	}
	
	/**
	 * M&eacute;todo que se encarga de devolver una instancia del objeto
	 * MovimientosCajaServicio
	 *
	 * @return IMovimientosCajaServicio
	 */
	private IMovimientosCajaServicio getMovimientosCajaServicio() {
		
		return TesoreriaFabricaServicio.crearMovimientosCajaServicio();
	}
	
	/**
	 * M&eacute;todo que se encarga de realizar el c&aacute;lculo del proceso de cuadre de caja y
	 * determina si es posible o no realizar el registro respectivo
	 *
	 * @param form
	 * @return boolean indicando si es posible o no realizar el registro 
	 */
	private boolean calcularCuadreCaja(ArqueosCajaForm form){

		boolean registrarCuadre = false;
		
		for (DtoCuadreCaja cuadreCaja : form.getConsolidadoMovimientoDTO().getCuadreCajaDTOs()) {
			
			if(cuadreCaja.getValorCaja()!=0){
				cuadreCaja.setValorDiferencia(cuadreCaja.getValorCaja() - cuadreCaja.getValorSistema());
				
				registrarCuadre = true;
				
			}else{
				cuadreCaja.setValorDiferencia(cuadreCaja.getValorSistema()*-1);
			}
		}
		
		return registrarCuadre;
	}
	
	/**
	 * M&eacute;todo que se encarga de realizar la consulta y de permitir la impresi&oacute;n 
	 * del Arqueo caja. Se tiene en cuenta el estado para saber si se realiza la consulta o
	 * se imprime el Arqueo Caja sin el cuadre caja correspondiente.
	 * 
	 * @param form
	 * @param estado
	 */
	private void imprimirArqueoCaja (ArqueosCajaForm form, String estado){
		
		if("imprimeArqueoSinCuadreCaja".equals(estado)){
			
			form.setImprimeCuadreCaja(false);
			
		}else {
			try{
				HibernateUtil.beginTransaction();
				IConsultaArqueoCierreCajaServicio consultaArqueoCierreCajaServicio = TesoreriaFabricaServicio.crearConsultaArqueoCierreCajaServicio();
				
				UtilidadTransaccion.getTransaccion().begin();
				
				form.setConsolidadoMovimientoDTO(consultaArqueoCierreCajaServicio.consultarArqueoCaja(form.getConsolidadoMovimientoDTO().getMovimientosCaja()));
				
				form.setImprimeCuadreCaja(true);
				HibernateUtil.endTransaction();
			}
			catch (Exception e) {
				Log4JManager.error(e);
				HibernateUtil.abortTransaction();
			}
		}
	}
	
	/**
	 * Método que se encarga de cargar los tipos de reporte arqueo caja a imprimir
	 * 
	 * @param form
	 * @author Fabián Becerra
	 */
	private void cargarTiposReporte(ArqueosCajaForm form){
		
		String[] listadoTipoRep = new String[]{ConstantesIntegridadDominio.acronimoTipoReporteImpArqueoCajaResumido,
													ConstantesIntegridadDominio.acronimoTipoReporteImpArqueoCajaDetallado,
													ConstantesIntegridadDominio.acronimoTipoReporteAmbos};
		
		Connection con=UtilidadBD.abrirConexion();
		ArrayList<DtoIntegridadDominio> listadoTiposReporte=Utilidades.generarListadoConstantesIntegridadDominio(
				con, listadoTipoRep, false);
		UtilidadBD.closeConnection(con);
		form.setListadoTiposReporte(listadoTiposReporte);
		
	}
	
	/**
	 * Método que se encarga de generar la el reporte detallado, resumido o ambos del arqueo caja
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param ins
	 */
	private void imprimirArqueoCaja(ArqueosCajaForm forma,  HttpServletRequest request, UsuarioBasico usuario, InstitucionBasica ins) {
        
        String tipoSalida         = forma.getTipoReporte();
        String nombreArchivo="";
        if (!tipoSalida.equals(ConstantesBD.codigoNuncaValido+"")&&!UtilidadTexto.isEmpty(tipoSalida)) {
        	HibernateUtil.beginTransaction();
        	UsuariosDelegate usu= new UsuariosDelegate();
			Usuarios usuarioCompleto=usu.findById(usuario.getLoginUsuario());
			
        	//TITULO REPORTE
    		String razonSocial = ins.getRazonSocial();
    		forma.getDtoFiltroReporte().setRazonSocial(razonSocial);
    		forma.getDtoFiltroReporte().setNit(ins.getNit());
        	forma.getDtoFiltroReporte().setUbicacionLogo(ins.getUbicacionLogo());
        	forma.getDtoFiltroReporte().setActividadEconomica(ins.getActividadEconomica());
        	forma.getDtoFiltroReporte().setDireccion(ins.getDireccion());
        	forma.getDtoFiltroReporte().setTelefono(ins.getTelefono());
        	forma.getDtoFiltroReporte().setCentroAtencion(usuario.getCentroAtencion());
        	String rutaLogo = ins.getLogoJsp();
	    	forma.getDtoFiltroReporte().setRutaLogo(rutaLogo);
        	
        	//ENCABEZADO REPORTE
	    	forma.getDtoFiltroReporte().setExito(forma.isExito());
        	if(forma.isExito())
        		forma.getDtoFiltroReporte().setNroConsecutivo(forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getConsecutivo()+"");
        	String fecha = UtilidadFecha.getFechaActual();
			String hora	 = UtilidadFecha.getHoraActual();
			if(forma.isEsConsultaConsolidadoCierre())
			{
				fecha = UtilidadFecha.conversionFormatoFechaAAp(forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getFecha());
				hora  = forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getHora();
			}
			if(forma.isConsulta())
			{
				fecha = UtilidadFecha.conversionFormatoFechaAAp(forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getFecha());
				hora  = forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getHora();
			}
        	forma.getDtoFiltroReporte().setFechaGeneracion(fecha);
        	forma.getDtoFiltroReporte().setHoraGeneracion(hora);
        	forma.getDtoFiltroReporte().setUsuarioGeneracion(usuarioCompleto.getPersonas().getPrimerApellido()
        			+" "+usuarioCompleto.getPersonas().getPrimerNombre()
        			+" - "+usuarioCompleto.getLogin());
        	
        	String fechaArqueo = (forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getFechaMovimiento() == null || forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getIngresaFechaArqueo() == ConstantesBD.acronimoNoChar) ? 
        			UtilidadFecha.conversionFormatoFechaAAp(forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getTurnoDeCaja().getFechaApertura()) + " - " + fecha : 
        				UtilidadFecha.conversionFormatoFechaAAp(forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getFechaMovimiento()); 
        	forma.getDtoFiltroReporte().setFechaArqueo(fechaArqueo);
        	
        	Usuarios cajero = forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getTurnoDeCaja().getUsuarios();
        	forma.getDtoFiltroReporte().setCajero(cajero.getPersonas().getPrimerApellido()
        			+" "+cajero.getPersonas().getPrimerNombre()
        			+" - "+cajero.getLogin());
        	forma.getDtoFiltroReporte().setCaja("(" + forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getTurnoDeCaja().getCajas().getCodigo() 
        			+ ") " +forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getTurnoDeCaja().getCajas().getDescripcion()
        			+ " - "+forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getTurnoDeCaja().getCajas().getCentroAtencion().getDescripcion());
			forma.getDtoFiltroReporte().setNombreUsuario(usuarioCompleto.getPersonas().getPrimerNombre()
					+" "+usuarioCompleto.getPersonas().getPrimerApellido()
					+" ("+usuarioCompleto.getLogin()+")"
					);
        	
	    	JasperReportBuilder reporte=null;
			if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteImpArqueoCajaResumido))
            {
				GeneradorReporteArqueoCajaResumido generadorReporte =
    	            new GeneradorReporteArqueoCajaResumido(forma.getDtoFiltroReporte(), forma.getConsolidadoMovimientoDTO());
            		reporte = generadorReporte.generarReporte();
            		nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ImpresionArqueoCajaResumido");
            		forma.getListaNombresReportes().add(nombreArchivo);
            }else
            	if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteImpArqueoCajaDetallado))
            	{
            		GeneradorReporteArqueoCajaDetallado generadorReporte =
            			new GeneradorReporteArqueoCajaDetallado(forma.getDtoFiltroReporte(), forma.getConsolidadoMovimientoDTO());
            		reporte = generadorReporte.generarReporte();
            		nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ImpresionArqueoCajaDetallado");
            		forma.getListaNombresReportes().add(nombreArchivo);
            	}else
            		if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteAmbos))
            		{
            			GeneradorReporteArqueoCajaResumido generadorReporteRes =
            	            new GeneradorReporteArqueoCajaResumido(forma.getDtoFiltroReporte(), forma.getConsolidadoMovimientoDTO());
                		reporte = generadorReporteRes.generarReporte();
                		String nombreArchivoRes = generadorReporteRes.exportarReportePDF(reporte, "ImpresionArqueoCajaResumido");
                		
                		GeneradorReporteArqueoCajaDetallado generadorReporteDeta =
                			new GeneradorReporteArqueoCajaDetallado(forma.getDtoFiltroReporte(), forma.getConsolidadoMovimientoDTO());
                		reporte = generadorReporteDeta.generarReporte();
                		String nombreArchivoDet = generadorReporteDeta.exportarReportePDF(reporte, "ImpresionArqueoCajaDetallado");
                		
                		forma.getListaNombresReportes().add(nombreArchivoRes);
                		forma.getListaNombresReportes().add(nombreArchivoDet);
            		}
			forma.setTipoReporte(ConstantesBD.codigoNuncaValido+"");
	        HibernateUtil.endTransaction();    
	     }
	}
	
}