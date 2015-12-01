package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.RipsEntidadesSubcontratadasForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.facturacion.DtoInconsistenciasRipsEntidadesSub;
import com.princetonsa.dto.facturacion.DtoResultadoProcesarRipsEntidadesSub;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.CausasExternas;
import com.servinte.axioma.orm.EntidadesSubcontratadas;
import com.servinte.axioma.orm.FinalidadesConsulta;
import com.servinte.axioma.orm.FinalidadesServicio;
import com.servinte.axioma.orm.LogRipsEntSubInconsisArch;
import com.servinte.axioma.orm.LogRipsEntSubInconsisCamp;
import com.servinte.axioma.orm.LogRipsEntSubRegValor;
import com.servinte.axioma.orm.LogRipsEntidadesSub;
import com.servinte.axioma.orm.LogRipsEntidadesSubArchivo;
import com.servinte.axioma.orm.LogRipsEntidadesSubRegistr;
import com.servinte.axioma.orm.TarifariosOficiales;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.historiaClinica.HistoriaClinicaServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.IEntidadesSubcontratadasServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IFinalidadesServicioServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntSubInconsisArchServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntSubInconsisCampServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntSubRegValorServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntidadesSubArchivoServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntidadesSubRegistrServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ILogRipsEntidadesSubcontratadasServicio;
import com.servinte.axioma.servicio.interfaz.historiaClinica.ICausasExternasServicio;
import com.servinte.axioma.servicio.interfaz.historiaClinica.IFinalidadesConsultaServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IDiagnosticosServicio;

public class RipsEntidadesSubcontratadasAction extends Action {
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, 
	 * HttpServletRequest, HttpServletResponse)
	*/
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response )throws Exception
	{
		
		if(form instanceof RipsEntidadesSubcontratadasForm){
			
			RipsEntidadesSubcontratadasForm forma = (RipsEntidadesSubcontratadasForm)form;
			String estado = forma.getEstado(); 
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			if(estado == null)
			{
				request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			
			//----------- Empezar
			else if(estado.equals("empezar"))
			{
				this.accionEmpezar(forma,usuario, mapping, ins);
			
				return mapping.findForward("principal");
			}
			else if(estado.equals("procesar")){
				this.procesarArchivos(forma,usuario, mapping, ins, request);
				return mapping.findForward("principal");
			}
		}
		return null;
	}
	
	/**
	 * Este método se encarga de inicializar los valores de los objetos de la
	 * página para procesar rips de entidades subcontratadas
	 * @param forma
	 * @param usuario Usuario que genera el proceso
	 * @param mapping Para hacer redirrección a los JSP
	 * @param ins Institución del usuario en sessión
	 * 
	 * @author Fabian Becerra
	 */
	@SuppressWarnings("unchecked")
	private void accionEmpezar(RipsEntidadesSubcontratadasForm forma, UsuarioBasico usuario,  
			ActionMapping mapping, InstitucionBasica ins){
		
		forma.reset();
		
		try {
			Connection con=UtilidadBD.abrirConexion();
			forma.setPath(Utilidades.obtenerPathFuncionalidad(con,
			        ConstantesBD.codigoFuncionalidadRipsEntidadesSubcontratadas));
			UtilidadBD.cerrarConexion(con);
		} catch (SQLException e) {
			Log4JManager.error(e);
		}
		try{
			HibernateUtil.beginTransaction();
			IEntidadesSubcontratadasServicio servicioEntidadesSub=FacturacionServicioFabrica.crearEntidadesSubcontratadasServicio();
			forma.setListaEntidadesSub(servicioEntidadesSub.listarEntidadesSubActivasEnSistema());
			
			this.listarTipoCodificacionMedicamInsum(forma,usuario,mapping);
			
			Connection con=UtilidadBD.abrirConexion();
			forma.setTarifariosOficiales(Utilidades.obtenerTarifariosOficiales(con, ""));
			UtilidadBD.closeConnection(con);
			
			IDiagnosticosServicio servicioDiag=ManejoPacienteServicioFabrica.crearDiagnosticosServicio();
			forma.getFiltroProcesarRipsEntidadesSub().setDiagnosticosEnSistema(servicioDiag.obtenerAcronimosDiagnosticosEnSistema());
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error(e);
			HibernateUtil.abortTransaction();
		}
	}

	/**
	 * Este método se encarga de cargar los valores de los tipos de codificación de 
	 * medicamentos e insumos interfaz y axioma
	 * @param forma 
	 * @param usuario Usuario que genera el proceso
	 * @param mapping Para hacer redirrección a los JSP
	 */
	private void listarTipoCodificacionMedicamInsum(RipsEntidadesSubcontratadasForm forma,
			UsuarioBasico usuario, ActionMapping mapping) {
		
		String[] listadoTiposCodMed = new String[]{ConstantesIntegridadDominio.acronimoInterfaz,
				ConstantesIntegridadDominio.acronimoAxioma};
		
		Connection con=UtilidadBD.abrirConexion();
		
		ArrayList<DtoIntegridadDominio> listadoTipoCodMedInsu=Utilidades.generarListadoConstantesIntegridadDominio(
				con, listadoTiposCodMed, false);
		
		UtilidadBD.closeConnection(con);
		
		forma.setListadoTipoCodMedInsu(listadoTipoCodMedInsu);
		
	}
	
	
	/**
	 * Este método se encarga de llamar al proceso de rips entidades subcontratadas
	 * y guardar las inconsistencias generadas 
	 * @param forma
	 * @param usuario Usuario que genera el proceso
	 * @param mapping Para hacer redirrección a los JSP
	 * @param ins Institución del usuario en sessión
	 * 
	 * @author Fabian Becerra
	 */
	private void procesarArchivos(RipsEntidadesSubcontratadasForm forma, UsuarioBasico usuario,  
			ActionMapping mapping, InstitucionBasica ins, HttpServletRequest request){
		
		ActionErrors errores=null;
		
		/** Contiene los mensajes genéricos para esta funcionalidad * */
		MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.facturacion.RipsEntidadesSubcontratadasForm");
		
		try{
			//INSERTAR LOS DETALLES GENERALES DEL PROCESO EN LA TABLA log_rips_entidades_sub
			HibernateUtil.beginTransaction();
				errores=new ActionErrors();
				
				//ALMACENO LOS NOMBRES DE LOS ARCHIVOS UTILIZADOS PARA EL PROCESO
				forma.getFiltroProcesarRipsEntidadesSub().setNombreArchivoCT(forma.getFiltroProcesarRipsEntidadesSub().getArchivoCT().getFileName());
				forma.getFiltroProcesarRipsEntidadesSub().setNombreArchivoAF(forma.getFiltroProcesarRipsEntidadesSub().getArchivoAF().getFileName());
				forma.getFiltroProcesarRipsEntidadesSub().setNombreArchivoUS(forma.getFiltroProcesarRipsEntidadesSub().getArchivoUS().getFileName());
				forma.getFiltroProcesarRipsEntidadesSub().setNombreArchivoAD(forma.getFiltroProcesarRipsEntidadesSub().getArchivoAD().getFileName());
				forma.getFiltroProcesarRipsEntidadesSub().setNombreArchivoAC(forma.getFiltroProcesarRipsEntidadesSub().getArchivoAC().getFileName());
				forma.getFiltroProcesarRipsEntidadesSub().setNombreArchivoAP(forma.getFiltroProcesarRipsEntidadesSub().getArchivoAP().getFileName());
				//forma.getFiltroProcesarRipsEntidadesSub().setNombreArchivoAH(forma.getFiltroProcesarRipsEntidadesSub().getArchivoAH().getFileName());
				//forma.getFiltroProcesarRipsEntidadesSub().setNombreArchivoAU(forma.getFiltroProcesarRipsEntidadesSub().getArchivoAU().getFileName());
				forma.getFiltroProcesarRipsEntidadesSub().setNombreArchivoAM(forma.getFiltroProcesarRipsEntidadesSub().getArchivoAM().getFileName());
				forma.getFiltroProcesarRipsEntidadesSub().setNombreArchivoAT(forma.getFiltroProcesarRipsEntidadesSub().getArchivoAT().getFileName());
				
				
				//CARGAR DATOS BASICOS PARA COMPARAR LOS ARCHIVOS PLANOS EN EL PROCESO
				
				//Cargar las finalidades del servicio
				IFinalidadesServicioServicio servicioFinaliServ=FacturacionServicioFabrica.crearFinalidadesServicioServicio();
				ArrayList<FinalidadesServicio> listaFinalidades=servicioFinaliServ.consultarFinalidadesServicio();
				for(FinalidadesServicio finalid:listaFinalidades)
					forma.getFiltroProcesarRipsEntidadesSub().getFinalidadesServicioEnSistema().add(finalid.getCodigo());
				
				//Cargar las finalidadesConsulta en el sistema
				IFinalidadesConsultaServicio servicioFinalidadConsulta=HistoriaClinicaServicioFabrica.crearFinalidadesConsultaServicio();
				ArrayList<FinalidadesConsulta> listaFinaliConsulta=servicioFinalidadConsulta.consultarFinalidadesConsulta();
				for(FinalidadesConsulta finalidadConsulta:listaFinaliConsulta)
					forma.getFiltroProcesarRipsEntidadesSub().getFinalidadesConsultaEnSistema().add(finalidadConsulta.getAcronimo());
				
				//Cargar las causas externas que estan registradas en el sistema
				ICausasExternasServicio servicioCausas=HistoriaClinicaServicioFabrica.crearCausasExternasServicio();
				ArrayList<CausasExternas> listaCausas=servicioCausas.consultarCausasExternas();
				for(CausasExternas causa:listaCausas)
					forma.getFiltroProcesarRipsEntidadesSub().getCausasExternasEnSistema().add(causa.getCodigo());
				
				//Cargar la institucion
				forma.getFiltroProcesarRipsEntidadesSub().setInstitucion(ins.getCodigoInstitucionBasica());
				
				//Cargar el usuario
				forma.getFiltroProcesarRipsEntidadesSub().setUsuario(usuario);
				
				
				//GENERAR PROCESO RIPS ENTIDADES SUBCONTRATADAS
				ILogRipsEntidadesSubcontratadasServicio servicioLogRips=FacturacionServicioFabrica.crearLogRipsEntidadesSubServicio();
				DtoResultadoProcesarRipsEntidadesSub dtoResultado=servicioLogRips.procesarRipsEntidadesSub(forma.getFiltroProcesarRipsEntidadesSub());
				
				
				//SERVICIOS UTILIZADOS PARA ALMACENAR LOS DATOS
				ILogRipsEntidadesSubArchivoServicio servicioArchivo=FacturacionServicioFabrica.crearLogRipsEntidadesSubArchivoServicio();
				ILogRipsEntSubInconsisArchServicio servicioInconsArchivo=FacturacionServicioFabrica.crearLogRipsEntSubInconsisArchServicio();
				ILogRipsEntidadesSubRegistrServicio servicioRegistro=FacturacionServicioFabrica.crearLogRipsEntidadesSubRegistrServicio();
				ILogRipsEntSubInconsisCampServicio servicioInconsCampos=FacturacionServicioFabrica.crearLogRipsEntSubInconsisCampServicio();
				ILogRipsEntSubRegValorServicio servicioValores=FacturacionServicioFabrica.crearLogRipsEntSubRegValorServicio();
				
				
				if(!dtoResultado.getErrores().isEmpty()){
					errores.add(dtoResultado.getErrores());
					
				}
				if(!Utilidades.isEmpty(dtoResultado.getDtoInconsistenciasArchivos())||!Utilidades.isEmpty(dtoResultado.getDtoInconsistenciasCamposReg())){
					errores.add("inconsistencias", new ActionMessage("errors.notEspecific", 
							fuenteMensaje.getMessage("RipsEntidadesSubcontratadasForm.procesoCancelado")));
					saveErrors(request,errores);
					forma.setExito(ConstantesBD.acronimoNo);
				}else{
					forma.setExito(ConstantesBD.acronimoSi);
				}
				LogRipsEntidadesSub log; log=new LogRipsEntidadesSub();
				log.setCodificacionMedicainsum(forma.getFiltroProcesarRipsEntidadesSub().getAcronimoCodMedicInsum());
				EntidadesSubcontratadas entSub=new EntidadesSubcontratadas();
				entSub.setCodigoPk(forma.getFiltroProcesarRipsEntidadesSub().getCodigoPkEntidadSub());
				log.setEntidadesSubcontratadas(entSub);
				log.setFechaProceso(UtilidadFecha.getFechaActualTipoBD());
				log.setHoraProceso(UtilidadFecha.getHoraActual());
				Usuarios usuarios=new Usuarios();
				usuarios.setLogin(usuario.getLoginUsuario());
				log.setUsuarios(usuarios);
				TarifariosOficiales tarifOfi=new TarifariosOficiales();
				tarifOfi.setCodigo(forma.getFiltroProcesarRipsEntidadesSub().getTarifarioSeleccionadoCodServicios());
				log.setTarifariosOficiales(tarifOfi);
				servicioLogRips.guardarLogRipsEntidadesSub(log);
				
				//INSERTAR LOS DETALLES DE LOS ARCHIVOS EN LA TABLA log_rips_entidades_sub_archivo
				
				ArrayList<Integer> numerosFila=new ArrayList<Integer>();
				
				for(String nombreArchivo:dtoResultado.getNombresArchivoCargue()){
						
						LogRipsEntidadesSubArchivo logArchivo=new LogRipsEntidadesSubArchivo();
						logArchivo.setLogRipsEntidadesSub(log);
						logArchivo.setNombreArchivo(nombreArchivo);
						if(nombreArchivo.equals(ConstantesBD.ripsCT))
							logArchivo.setCantidadRegistrosLeidos(dtoResultado.getRegistrosArchivoCT());
						else if(nombreArchivo.equals(ConstantesBD.ripsAF))
							logArchivo.setCantidadRegistrosLeidos(dtoResultado.getRegistrosArchivoAF());
						else if(nombreArchivo.equals(ConstantesBD.ripsUS))
							logArchivo.setCantidadRegistrosLeidos(dtoResultado.getRegistrosArchivoUS());
						else if(nombreArchivo.equals(ConstantesBD.ripsAD))
							logArchivo.setCantidadRegistrosLeidos(dtoResultado.getRegistrosArchivoAD());
						else if(nombreArchivo.equals(ConstantesBD.ripsAC))
							logArchivo.setCantidadRegistrosLeidos(dtoResultado.getRegistrosArchivoAC());
						else if(nombreArchivo.equals(ConstantesBD.ripsAP))
							logArchivo.setCantidadRegistrosLeidos(dtoResultado.getRegistrosArchivoAP());
						/*if(nombreArchivo.equals(ConstantesBD.ripsAH))
							logArchivo.setCantidadRegistrosLeidos(dtoResultado.getRegistrosArchivoAH());
						if(nombreArchivo.equals(ConstantesBD.ripsAU))
							logArchivo.setCantidadRegistrosLeidos(dtoResultado.getRegistrosArchivoAU());*/
						else if(nombreArchivo.equals(ConstantesBD.ripsAM))
							logArchivo.setCantidadRegistrosLeidos(dtoResultado.getRegistrosArchivoAM());
						else if(nombreArchivo.equals(ConstantesBD.ripsAT))
							logArchivo.setCantidadRegistrosLeidos(dtoResultado.getRegistrosArchivoAT());
						else 
							logArchivo.setCantidadRegistrosLeidos(new Long(0));
						
						if(nombreArchivo.equals(ConstantesBD.ripsCT))
							logArchivo.setCantidadRegistrosProcesados(dtoResultado.getRegistrosProcesadosArchivoCT());
						else if(nombreArchivo.equals(ConstantesBD.ripsAF))
							logArchivo.setCantidadRegistrosProcesados(dtoResultado.getRegistrosProcesadosArchivoAF());
						else if(nombreArchivo.equals(ConstantesBD.ripsUS))
							logArchivo.setCantidadRegistrosProcesados(dtoResultado.getRegistrosProcesadosArchivoUS());
						else if(nombreArchivo.equals(ConstantesBD.ripsAD))
							logArchivo.setCantidadRegistrosProcesados(dtoResultado.getRegistrosProcesadosArchivoAD());
						else if(nombreArchivo.equals(ConstantesBD.ripsAC))
							logArchivo.setCantidadRegistrosProcesados(dtoResultado.getRegistrosProcesadosArchivoAC());
						else if(nombreArchivo.equals(ConstantesBD.ripsAP))
							logArchivo.setCantidadRegistrosProcesados(dtoResultado.getRegistrosProcesadosArchivoAP());
						/*if(nombreArchivo.equals(ConstantesBD.ripsAH))
							logArchivo.setCantidadRegistrosProcesados(dtoResultado.getRegistrosProcesadosArchivoAH());
						if(nombreArchivo.equals(ConstantesBD.ripsAU))
							logArchivo.setCantidadRegistrosProcesados(dtoResultado.getRegistrosProcesadosArchivoAU());*/
						else if(nombreArchivo.equals(ConstantesBD.ripsAM))
							logArchivo.setCantidadRegistrosProcesados(dtoResultado.getRegistrosProcesadosArchivoAM());
						else if(nombreArchivo.equals(ConstantesBD.ripsAT))
							logArchivo.setCantidadRegistrosProcesados(dtoResultado.getRegistrosProcesadosArchivoAT());
						else 
							logArchivo.setCantidadRegistrosProcesados(new Long(0));
						servicioArchivo.guardarLogRipsEntidadesSubArchivo(logArchivo);
						
							//ALMACENAR INCONSISTENCIAS DE ARCHIVO
							for(DtoInconsistenciasRipsEntidadesSub inconsisArchivosSub:dtoResultado.getDtoInconsistenciasArchivos()){
								
								if(inconsisArchivosSub.getNombreArchivo().equals(nombreArchivo)){
									LogRipsEntSubInconsisArch logInconsArchivo; logInconsArchivo=new LogRipsEntSubInconsisArch();
									
									logInconsArchivo.setLogRipsEntidadesSubArchivo(logArchivo);
									logInconsArchivo.setTipoInconsistencia(inconsisArchivosSub.getObservaciones());
									
									servicioInconsArchivo.guardarLogRipsEntSubInconsistenciaArchivo(logInconsArchivo);
								}
							}
							
						
					
							numerosFila=new ArrayList<Integer>();
							//ALMACENA LOS NUMEROS DE FILA QUE TIENEN INCONSISTENCIAS POR ARCHIVO RECORRIDO
							for(DtoInconsistenciasRipsEntidadesSub inconsisCampos:dtoResultado.getDtoInconsistenciasCamposReg()){
								if(nombreArchivo.equals(inconsisCampos.getNombreArchivo())&&!numerosFila.contains(inconsisCampos.getNumeroFila())){
									numerosFila.add(inconsisCampos.getNumeroFila());
								}
							}
							
							//ALMACENAR LOS DETALLES DE LOS REGISTROS
							for(Integer numeroFila:numerosFila){
								
										LogRipsEntidadesSubRegistr logRegistro=new LogRipsEntidadesSubRegistr();
										logRegistro.setNumeroFila(numeroFila);
										logRegistro.setLogRipsEntidadesSubArchivo(logArchivo);
										servicioRegistro.guardarLogRipsEntidadesSubRegistr(logRegistro);
										
										//ALMACENAR INCONSISTENCIAS DE REGISTRO O CAMPOS
										for(DtoInconsistenciasRipsEntidadesSub inconsisCamposSub:dtoResultado.getDtoInconsistenciasCamposReg()){
											
											if(inconsisCamposSub.getNumeroFila()==numeroFila
													&&inconsisCamposSub.getNombreArchivo().equals(nombreArchivo))
													{
												
												LogRipsEntSubInconsisCamp logInconsCamp=new LogRipsEntSubInconsisCamp();
												logInconsCamp.setLogRipsEntidadesSubRegistr(logRegistro);
												logInconsCamp.setNombreCampo(inconsisCamposSub.getCampo());
												logInconsCamp.setTipoInconsistencia(inconsisCamposSub.getObservaciones());
												
												servicioInconsCampos.guardarLogRipsEntSubInconsistenciaCampo(logInconsCamp);
											}
										}
										
										//ALMACENAR VALORES DE CAMPOS DE UN REGISTRO
										for(DtoInconsistenciasRipsEntidadesSub valoresCamp:dtoResultado.getDtoValoresCampos()){
											
											if(valoresCamp.getNumeroFila()==numeroFila
													&&valoresCamp.getNombreArchivo().equals(nombreArchivo))
													{
												
												LogRipsEntSubRegValor logValores=new LogRipsEntSubRegValor();
												logValores.setCampoObligatorio(valoresCamp.getCampo());
												logValores.setLogRipsEntidadesSubRegistr(logRegistro);
												logValores.setValor(valoresCamp.getObservaciones());
												servicioValores.guardarLogRipsEntSubRegValores(logValores);
											}
										}
										
								
							}
							
							numerosFila=new ArrayList<Integer>();
							//SI NO SE ENCONTRARON INCONSISTENCIAS SE BUSCA LAS AUTORIZACIONES VALIDAS
							for(DtoInconsistenciasRipsEntidadesSub autorValida:dtoResultado.getListaAutorizacionesValidas()){
								if(nombreArchivo.equals(autorValida.getNombreArchivo())&&!numerosFila.contains(autorValida.getNumeroFila())){
									numerosFila.add(autorValida.getNumeroFila());
								}
							}
							
							//ALMACENAR LOS DETALLES DE LOS REGISTROS
							for(Integer numeroFila:numerosFila){
								
										LogRipsEntidadesSubRegistr logRegistro=new LogRipsEntidadesSubRegistr();
										logRegistro.setNumeroFila(numeroFila);
										logRegistro.setLogRipsEntidadesSubArchivo(logArchivo);
										servicioRegistro.guardarLogRipsEntidadesSubRegistr(logRegistro);
										
										//ALMACENAR VALORES DE CAMPOS DE UN REGISTRO
										for(DtoInconsistenciasRipsEntidadesSub valoresCamp:dtoResultado.getDtoValoresCampos()){
											
											if(valoresCamp.getNumeroFila()==numeroFila
													&&valoresCamp.getNombreArchivo().equals(nombreArchivo))
													{
												
												LogRipsEntSubRegValor logValores=new LogRipsEntSubRegValor();
												logValores.setCampoObligatorio(valoresCamp.getCampo());
												logValores.setLogRipsEntidadesSubRegistr(logRegistro);
												logValores.setValor(valoresCamp.getObservaciones());
												servicioValores.guardarLogRipsEntSubRegValores(logValores);
											}
										}
										
								
							}
							
					}
					
					
				HibernateUtil.endTransaction();
			}catch (Exception e) {
				Log4JManager.error("Error al guardar el log del proceso rips de entidades sub: ", e);
				HibernateUtil.abortTransaction();
			}					
		
	}
	
	
}


