package com.princetonsa.action.facturasVarias;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JasperPrint;

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
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.facturasVarias.ConsultaMovimientoFacturaForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.consultaExterna.DtoConceptoFacturaVaria;
import com.princetonsa.dto.facturasVarias.DtoConsolodadoFacturaVaria;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.enu.impresion.EnumTiposSalida;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturasVarias.ConsultaMovimientoFactura;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.generadorReporte.facturasVarias.consultaMovimientosFacturasVarias.GeneradorConsultamovimientosFacturaVaria;
import com.servinte.axioma.generadorReporte.facturasVarias.consultaMovimientosFacturasVarias.generadorConsultaMovimientosFacturaVariaPlano;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Ciudades;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.facturasvarias.FacturasVariasFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;
import com.servinte.axioma.servicio.interfaz.facturasvarias.IFacturasVariasServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ILocalizacionServicio;

/**
 * @author Mauricio Jaramillo
 * Fecha: Agosto de 2008
 */

public class ConsultaMovimientoFacturaAction extends Action
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger =Logger.getLogger(ConsultaMovimientoFacturaAction.class);
	
	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{

		Connection con = null;
		try{
			if (form instanceof ConsultaMovimientoFacturaForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				ConsultaMovimientoFacturaForm forma = (ConsultaMovimientoFacturaForm) form;
				ConsultaMovimientoFactura mundo = new ConsultaMovimientoFactura();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				String estado = forma.getEstado();
				logger.warn("[ConsultaMovimientoFactura]--->Estado: "+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo estado is NULL");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					mapping.findForward("principal");
					return cargarDatosReportePresupuestosOdontologicosContProm(con,
							mapping, forma, institucion, usuario);
					//return mapping.findForward("principal");
				}
				if(estado.equals("cambiarPais")){
					listarCiudades(forma);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					return mapping.findForward("principal");
				}
				if(estado.equals("cambiarCiudad")){
					listarCiudades(forma);
					listarCentrosAtencion(forma);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					return mapping.findForward("principal");
				}
				if(estado.equals("cambiarRegion")){
					listarRegiones(forma);
					listarCentrosAtencion(forma);
					forma.setTipoSalida(null);
					forma.setEnumTipoSalida(null);
					forma.setNombreArchivoGenerado(null);
					return mapping.findForward("principal");
				}
				if(estado.equals("cambiarEmpresaInstitucion")){
					listarCentrosAtencion(forma);
					return mapping.findForward("principal");
				}if(estado.equals("inicializarNomArch")){
					forma.setNombreArchivoGenerado("");
					return mapping.findForward("principal");
				}
				else if(estado.equals("recargar"))
				{
					return accionRecargar(con, forma, mapping, mundo, usuario);
				}
				else if(estado.equals("buscar"))
				{
					return accionBuscar(con, forma, mapping, mundo, usuario, session);
				}
				else if(estado.equals("imprimirReporte"))
				{
					String nombreUsuario = usuario.getNombreUsuario();
					forma.setNombreUsuarioProceso(nombreUsuario);
					return Imprimir(con, forma, institucion, mapping, mundo,usuario,session);
				}
				else if(estado.equals("mostrarEstadoPresupuesto"))
				{
					return mapping.findForward("mostrarEstadoPresupuesto");
				}
				else if(estado.equals("detalleDeudor"))
				{
					return accionDetalleDeudor(con, forma, mapping, mundo, usuario);
				}
				else if(estado.equals("volverListado"))
				{
					return accionVolverListado(con, forma, mapping, mundo, usuario,session);
				}
				//ESTADO UTILIZADO PARA EL PAGER
				else if (estado.equals("redireccion")) 
				{			    
					UtilidadBD.cerrarConexion(con);
					forma.getLinkSiguiente();
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
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
				logger.error("El form no es compatible con el form de ConsultaMovimientoFacturaForm");
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

	
	private ActionForward cargarDatosReportePresupuestosOdontologicosContProm(
			Connection con, ActionMapping mapping,
			ConsultaMovimientoFacturaForm forma,
			InstitucionBasica ins, UsuarioBasico usuario) {
		//forma.setTipoReporte(ConstantesBD.codigoNuncaValido);
		try{
			forma.reset();	
			HibernateUtil.beginTransaction();
			forma.setPath(Utilidades.obtenerPathFuncionalidad(con, 
			ConstantesBD.codigoFuncionalidadmenuReportesFacturasVarias));
			
			
			int codigoInstitucion = usuario.getCodigoInstitucionInt();
			String codigoPaisResidencia = ValoresPorDefecto.getPaisResidencia(codigoInstitucion);
			String[] codigosPais=codigoPaisResidencia.split("-");
			
			String razonSocial = ins.getRazonSocial();
			
			forma.setRazonSocial(razonSocial);
			
			
			if(!codigoPaisResidencia.trim().equals("-")){
				forma.setCodigoPaisResidencia(codigosPais[0]);
			}
			
			ArrayList<Paises> listaPaises = AdministracionFabricaServicio.crearLocalizacionServicio().listarPaises();
			
			forma.setListaPaises(listaPaises);
			if (listaPaises.size() == 1) {
				forma.setCodigoPaisResidencia(listaPaises.get(0).getCodigoPais());
			}
			
			listarCiudades(forma);
			listarRegiones(forma);
			
			forma.setEsMultiempresa(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion));
			if (forma.getEsMultiempresa().equals(ConstantesBD.acronimoSi)) {
				forma.setListaEmpresaInstitucion(FacturacionServicioFabrica.crearEmpresasInstitucionServicio().listarEmpresaInstitucion());
				//forma.setEsMultiempresa("true");
			}
					
			listarCentrosAtencion(forma);
			forma.setCiudadDeptoPais(new String());
			
			
			listarConceptos(forma);
			listarEstadoFacturasVarias(forma);	
			
			//cargar los usuarios.
			IUsuariosServicio usuarioServicio=AdministracionFabricaServicio.crearUsuariosServicio();
			forma.setUsuarios(usuarioServicio.obtenerUsuariosSistemas(usuario.getCodigoInstitucionInt(),false));
			
			//***** Define si se Utiliza Programas Odontologicos en la Institución 
			int institucion = usuario.getCodigoInstitucionInt();
			String utilizaProgramasOdonto = ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(institucion);
			//forma.setUtilizaProgramasOdonto(utilizaProgramasOdonto);
			
			//*****Define si se Utiliza Programas Odontologicos en la Institución 
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error("ERROR ", e);
		}
		
		return mapping.findForward("principal");
	}
	
	/**
	 * metodo para listar los conceptos de factura varia
	 * @param forma 
	 */
	
	private void listarConceptos(
			ConsultaMovimientoFacturaForm forma) {
		IFacturasVariasServicio servicio = FacturasVariasFabricaServicio.crearFacturasVariasServicio();
		
		ArrayList<DtoConceptoFacturaVaria> listasConceptos = servicio.listarConceptosFacturasV();
		if (listasConceptos != null && listasConceptos.size() > 0) {
			forma.setListaConceptos(listasConceptos);
		} else {
			forma.setListaConceptos(null);
		}
	}
	
	/**
	 * metodo para listar los estados de factura varia
	 * @param forma 
	 */
	private void listarEstadoFacturasVarias(
			ConsultaMovimientoFacturaForm forma) {
		IFacturasVariasServicio servicio = FacturasVariasFabricaServicio.crearFacturasVariasServicio();
		
		ArrayList<DtoIntegridadDominio> listaEstadoFacturaV = servicio.listarEstadoFacturasVarias();
		
		if (listaEstadoFacturaV != null && listaEstadoFacturaV.size() > 0) {
			forma.setListaEstados(listaEstadoFacturaV);
					
			for (DtoIntegridadDominio registro : listaEstadoFacturaV) {
					forma.setNombreEstadoFacturaVaria(registro.getDescripcion());	
			}
			
		} else {
			forma.setListaEstados(null);
		}
	}
	
	
	/**
	 * metodo para listar los centros de atencion de factura varia
	 * @param forma 
	 */
	private void listarCentrosAtencion(
			ConsultaMovimientoFacturaForm forma) {
		try{
			HibernateUtil.beginTransaction();
			ILocalizacionServicio servicio = AdministracionFabricaServicio.crearLocalizacionServicio();
			long empresaInstitucion = forma.getCodigoEmpresaInstitucion();
			String ciudadDtoPais= forma.getCiudadDeptoPais();
			long codigoRegion = forma.getCodigoRegion();
			ArrayList<DtoCentrosAtencion> listaCentrosAtencion = null;
			
			if (codigoRegion <= 0 && empresaInstitucion <=0 &&
					UtilidadTexto.isEmpty(ciudadDtoPais) || ciudadDtoPais.trim().equals(ConstantesBD.codigoNuncaValido +"")) {
				
				//lista todos los centros de atención del sistema
				 listaCentrosAtencion =  AdministracionFabricaServicio.crearLocalizacionServicio().listarTodosCentrosAtencion();
				
			}else if (!UtilidadTexto.isEmpty(ciudadDtoPais) && !ciudadDtoPais.trim().equals(ConstantesBD.codigoNuncaValido + "")) {
				
				String vec[]=forma.getCiudadDeptoPais().split(ConstantesBD.separadorSplit);
				forma.setCodigoCiudad(vec[0]);
				forma.setCodigoDpto(vec[1]);
				forma.setCodigoPais(vec[2]);
				
				if (empresaInstitucion <= 0) {	
					//lista todos por ciudad
					listaCentrosAtencion = servicio.listarTodosPorCiudad(vec[0], vec[2], vec[1]);
				} else {
					//lista todos por empresa institucion y ciudad
					listaCentrosAtencion = servicio.listarTodosPorEmpresaInstitucionYCiudad(
							empresaInstitucion, forma.getCodigoCiudad(),
							forma.getCodigoPais(), 
							forma.getCodigoDpto());
				}
				
			}else if (codigoRegion > 0) {
				if (empresaInstitucion > 0) {
					//lista todos por region y empresa institucion
					listaCentrosAtencion = servicio.listarTodosPorEmpresaInstitucionYRegion(
							empresaInstitucion, codigoRegion);
					
				} else {
					//listar por region
					listaCentrosAtencion = servicio.listarTodosPorRegion(codigoRegion);
				}
				
			} else {
				//lista todos por institucion
				listaCentrosAtencion = servicio.listarTodosPorEmpresaInstitucion(empresaInstitucion);
			}
			
			if (listaCentrosAtencion != null && listaCentrosAtencion.size()>0) {
				forma.setListaCentrosAtencion(listaCentrosAtencion);
			}else{
				forma.setListaCentrosAtencion(null);
			}
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
		}
	}

	/**
	 * metodo para listar las regiones 
	 * @param forma 
	 */
	private void listarRegiones(
			ConsultaMovimientoFacturaForm forma) {
		Long codigoRegion= forma.getCodigoRegion();
		try{
			HibernateUtil.beginTransaction();
			
			if (codigoRegion == 0 || codigoRegion == ConstantesBD.codigoNuncaValidoLong  ) {
				forma.setListaRegiones(AdministracionFabricaServicio.crearLocalizacionServicio()
						.listarRegionesCoberturaActivas());
				forma.setDeshabilitaCiudad(false);
				forma.setDeshabilitaRegion(false);
			} else {
				forma.setListaRegiones(AdministracionFabricaServicio.crearLocalizacionServicio()
						.listarRegionesCoberturaActivas());
				forma.setDeshabilitaCiudad(true);
			}
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error("ERROR ", e);
		}
		
	}
 
	/**
	 * metodo para listar las ciudades
	 * @param forma 
	 */
	private void listarCiudades(
			ConsultaMovimientoFacturaForm forma) {
		try{
			HibernateUtil.beginTransaction();
		
			String ciudadDeptoPais= forma.getCiudadDeptoPais();
			
			if(UtilidadTexto.isEmpty(ciudadDeptoPais) || ciudadDeptoPais.trim().equals(ConstantesBD.codigoNuncaValido + "")){
				
				forma.setListaCiudades(AdministracionFabricaServicio.crearLocalizacionServicio()
						.listarCiudadesPorPais(forma.getCodigoPaisResidencia()));
				forma.setDeshabilitaCiudad(false);
				forma.setDeshabilitaRegion(false);
				
				if(forma.getListaCiudades()!=null && forma.getListaCiudades().size()==1){
					Ciudades ciudad = forma.getListaCiudades().get(0);
					
					String codigoCiudad=ciudad.getId().getCodigoCiudad()+ ConstantesBD.separadorSplit
					+ ciudad.getDepartamentos().getId().getCodigoDepartamento()+ ConstantesBD.separadorSplit
					+ ciudad.getPaises().getCodigoPais();
					
					forma.setCiudadDeptoPais(codigoCiudad);
				}
			}else{
				forma.setListaCiudades(AdministracionFabricaServicio.crearLocalizacionServicio()
						.listarCiudadesPorPais(forma.getCodigoPaisResidencia()));
				forma.setDeshabilitaRegion(true);
			}
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error("ERROR ", e);
		}
		
	}
	
	/**
	 * metodo para imprimir
	 * @param con 
	 * @param forma 
	 * @param ins 
	 * @param mapping 
	 * @param mundo 
	 * @param usuario 
	 * @param request 
	 */
	private ActionForward Imprimir(Connection con, ConsultaMovimientoFacturaForm forma, InstitucionBasica ins, ActionMapping mapping, ConsultaMovimientoFactura mundo, UsuarioBasico usuario, HttpSession request){
		
		int tipoSalida = Integer.parseInt(forma.getTipoSalida());
        String nombreArchivo="";
        //String estadoRetorno="";
        forma.setConsultaMovimientoFactura(mundo.consultarMovimientosFactura(con, forma));
		int numRegistros = Utilidades.convertirAEntero(forma.getConsultaMovimientoFactura("numRegistros")+"");
        if (tipoSalida > 0) {
        	GeneradorConsultamovimientosFacturaVaria generadorReporte=null;
			JasperPrint reporte=null;
			//Llenamos el mapa con los resultados arrojados por la consulta de movimientos de deudores
			
										
				//Recorremos el mapa con el fin de sacar de el los objetos para agregarlos a un Dto
				int w=1;
				
				ArrayList<DtoConsolodadoFacturaVaria> consolidar = new ArrayList<DtoConsolodadoFacturaVaria>();
				
				for(int i=0; i<numRegistros; i++)
				{	DtoConsolodadoFacturaVaria consol = new DtoConsolodadoFacturaVaria();
					consol.setConsecutivofactura(forma.getConsultaMovimientoFactura("consecutivofactura_"+i)+"");
					consol.setUsuario(forma.getConsultaMovimientoFactura("usuario_"+i)+"");
					consol.setPrimerNombreProfesionalContrato(forma.getConsultaMovimientoFactura("prinomuscont_"+i)+"");
					consol.setSegundoNombreProfesionalContrato(forma.getConsultaMovimientoFactura("segnomuscont_"+i)+"");
					consol.setPrimerApellidoProfesionalContrato(forma.getConsultaMovimientoFactura("priapuscont_"+i)+"");
					consol.setSegundoApellidoProfesionalContrato(forma.getConsultaMovimientoFactura("segapuscont_"+i)+"");
					consol.setEstado(forma.getConsultaMovimientoFactura("estado_"+i)+"");
					consol.setConsecutivo(forma.getConsultaMovimientoFactura("consecutivo_"+i)+"");
					consol.setDescripcioncentroatencion(forma.getConsultaMovimientoFactura("descripcioncentroatencion_"+i)+"");
					consol.setDescripcionPais(forma.getConsultaMovimientoFactura("pais_"+i)+"");
					consol.setDescripcionRegionCobertura(forma.getConsultaMovimientoFactura("region_"+i)+"");
					consol.setDescripcionCiudad(forma.getConsultaMovimientoFactura("nombreciudad_"+i)+"");
					consol.setCodigofactura(forma.getConsultaMovimientoFactura("codigofactura_"+i)+"");
					consol.setDescripcion(forma.getConsultaMovimientoFactura("descripcion_"+i)+"");
					consol.setFechaelaboracion(forma.getConsultaMovimientoFactura("fechaelaboracion_"+i)+"");
					consol.setDeudor(forma.getConsultaMovimientoFactura("deudor_"+i)+"");
					consol.setDesdeudor(forma.getConsultaMovimientoFactura("desdeudor_"+i)+"");
					consol.setIdtercero(forma.getConsultaMovimientoFactura("idtercero_"+i)+"");
					consol.setTipodeudor(forma.getConsultaMovimientoFactura("tipodeudor_"+i)+"");
					consol.setValorinicial(forma.getConsultaMovimientoFactura("valorinicial_"+i)+"");
					consol.setAjustesdebito(forma.getConsultaMovimientoFactura("ajustesdebito_"+i)+"");
					consol.setAjustescredito(forma.getConsultaMovimientoFactura("ajustescredito_"+i)+"");
					consol.setPagosaplicados(forma.getConsultaMovimientoFactura("pagosaplicados_"+i)+"");
					consol.setSaldo(forma.getConsultaMovimientoFactura("saldo_"+i)+"");
					if(forma.getEsMultiempresa().equals(ConstantesBD.acronimoSi)){
						consol.setRazonSocial(forma.getConsultaMovimientoFactura("razsolinstempresa_"+i)+"");
						//dtoInterno.setUbicacionLogoReporte(rs.getString("ublogoreporteinstemp"));
						
					}else{
						consol.setRazonSocial(forma.getConsultaMovimientoFactura("razonsocial_"+i)+"");
						//dtoInterno.setUbicacionLogoReporte(rs.getString("ublogoreporteins"));
					}
					consolidar.add(consol);
					
				}
				Log4JManager.info("------------------- tamano consolidar:   " +consolidar.size());
			ArrayList<String> consecutivosCentrosAtencion = new ArrayList<String>();
			ArrayList<String> estados = new ArrayList<String>();
			ArrayList<DtoConsolodadoFacturaVaria> lista = new ArrayList<DtoConsolodadoFacturaVaria>();
			double  totalValorInicial = 0, totalValorAjusteDebito = 0, totalValorAjusteCredito = 0, totalPagosAplicados = 0, totalSaldo = 0;
			for (int i = 0; i < consolidar.size(); i++) {
					lista = new ArrayList<DtoConsolodadoFacturaVaria>();
					totalValorInicial = 0;
					totalValorAjusteDebito = 0;
					totalValorAjusteCredito = 0;
					totalPagosAplicados = 0;
					totalSaldo = 0;
										
					if (!consecutivosCentrosAtencion.contains(consolidar
							.get(i).getConsecutivo())){
						estados = new ArrayList<String>();
						
					}
					if (!estados.contains(consolidar
							.get(i).getEstado())) {
						for (int j = i ; j < consolidar.size(); j++) {
							if (consolidar.get(i)
									.getConsecutivo().compareTo(consolidar.get(j).getConsecutivo()) == 0
									&& 
									consolidar.get(i).getEstado().compareTo(consolidar.get(j).getEstado()) == 0) {
								
								
							lista.add(consolidar.get(j));
							totalValorInicial = totalValorInicial + Utilidades.convertirADouble(consolidar.get(j).getValorinicial());
							totalValorAjusteDebito = totalValorAjusteDebito + Utilidades.convertirADouble(consolidar.get(j).getAjustesdebito());
							totalValorAjusteCredito = totalValorAjusteCredito + Utilidades.convertirADouble(consolidar.get(j).getAjustescredito());
							totalPagosAplicados = totalPagosAplicados + Utilidades.convertirADouble(consolidar.get(j).getPagosaplicados());
							totalSaldo = totalSaldo + Utilidades.convertirADouble(consolidar.get(j).getSaldo());
							consolidar.get(j).setTotalValorInicial(totalValorInicial);	
							consolidar.get(j).setTotalValorAjusteDebito(totalValorAjusteDebito);
							consolidar.get(j).setTotalValorAjusteCredito(totalValorAjusteCredito);
							consolidar.get(j).setTotalPagosAplicados(totalPagosAplicados);
							consolidar.get(j).setTotalSaldo(totalSaldo);
							}
						}
						consecutivosCentrosAtencion.add(consolidar.get(i)
								.getConsecutivo());	
						estados.add(consolidar.get(i).getEstado());
						consolidar.get(i)
								.setListadoConsolidado(lista);
					}
					
				}
				
				//Quita registros repetidos de codigo promocion y los registros vacios de su lista por promocion
				ArrayList<DtoConsolodadoFacturaVaria> listadefinitiva = new ArrayList<DtoConsolodadoFacturaVaria>();
				for(DtoConsolodadoFacturaVaria retorno:consolidar){
					if (!Utilidades
							.isEmpty(retorno.getListadoConsolidado())) {
						listadefinitiva.add(retorno);
						
					}
				}
				forma.setUbicacionLogo(ins.getUbicacionLogo());
        		String rutaLogo = ins.getLogoJsp();
        		forma.setRutaLogo(rutaLogo);
			generadorConsultaMovimientosFacturaVariaPlano generadorReportePlano=null;
		    if (tipoSalida == EnumTiposSalida.PDF.getCodigo()) {
            		generadorReporte =
                    new GeneradorConsultamovimientosFacturaVaria(listadefinitiva,forma);
            		reporte = generadorReporte.generarReporte();
                    forma.setEnumTipoSalida(EnumTiposSalida.PDF);
                    
            } else if (tipoSalida == EnumTiposSalida.HOJA_CALCULO.getCodigo() ) {
            		
            		generadorReporte=
                    new GeneradorConsultamovimientosFacturaVaria(listadefinitiva,forma);
            		reporte = generadorReporte.generarReporte();
                    forma.setEnumTipoSalida(EnumTiposSalida.HOJA_CALCULO);
                    
            } else if (tipoSalida == EnumTiposSalida.PLANO.getCodigo() ) {
            		generadorReportePlano =
            			
                    new generadorConsultaMovimientosFacturaVariaPlano(consolidar,forma);
            		reporte = generadorReportePlano.generarReporte();
                    forma.setEnumTipoSalida(EnumTiposSalida.PLANO);
            }
            
            switch (forma.getEnumTipoSalida()) {
            
            case PDF:
                    nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ConsultaMovimientosFacturaVaria");
                    break;
                    
            case PLANO:
            		nombreArchivo = generadorReportePlano.exportarReporteTextoPlano(reporte,  "ConsultaMovimientosFacturaVaria");
                    break;
                    
            case HOJA_CALCULO:
                    nombreArchivo = generadorReporte.exportarReporteExcel(reporte, "ConsultaMovimientosFacturaVaria");
                    break;
            }
            
            forma.setNombreArchivoGenerado(nombreArchivo);
        }else{
		   	ActionErrors errores=new ActionErrors();
	            errores.add("No se encontraron resultados", new ActionMessage("errors.modFactsVarsReporteFacturasVarias"));
	            saveErrors(request, errores);
	          
	        }
        forma.setTipoSalida("");
        UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoDeudoresFacturas");
	}
	
	/**
	 * Método implementado para volver a la página del listado
	 * de consulta de movimientos de deudor por facturas
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionVolverListado(Connection con, ConsultaMovimientoFacturaForm forma, ActionMapping mapping, ConsultaMovimientoFactura mundo, UsuarioBasico usuario,HttpSession request)
	{
		//Reseteamos el mapa general y la variable posicion
		forma.resetDatosVolver();
		//Realizamos la consulta nuevamente de los movimientos por deudor en general
		this.accionBuscar(con, forma, mapping, mundo, usuario,request);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listadoDeudoresFacturas");
	}

	/**
	 * Método que consulta la información del detalle
	 * de la Consulta de Movimiento de Deudor seleccionada
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionDetalleDeudor(Connection con, ConsultaMovimientoFacturaForm forma, ActionMapping mapping, ConsultaMovimientoFactura mundo, UsuarioBasico usuario)
	{
		logger.info("====>Posicion Seleccionada: "+forma.getPosicion());
		String consecutivoFactura = forma.getConsultaMovimientoFactura("consecutivofactura_"+forma.getPosicion())+"";
		String codigoFactura = forma.getConsultaMovimientoFactura("codigofactura_"+forma.getPosicion())+"";

		//*********INICIO DE LLENADOS DE LOS MAPA INDEPENDIENTES PARA LA INFORMACIÓN DEL DETALLE*********
		//Llenamos el mapa con la información del detalle (Información Factura e Información Deudor)
		forma.setConsultaDetalleMovimientoFactura(mundo.consultarDetalleMovimientosFactura(con, consecutivoFactura));
		//Llenamos el mapa con la información del detalle (Información Ajustes)
		forma.setConsultaDetalleAjustesFactura(mundo.consultarDetalleAjustesFactura(con, codigoFactura));
		//Llenamos el mapa con la información del detalle (Información Pagos)
		forma.setConsultaDetallePagosFactura(mundo.consultarDetallePagosFactura(con, codigoFactura));
		//Llenamos el mapa con la información del detalle (Información Resumen Movimientos)
		forma.setConsultaDetalleResumenFactura(mundo.consultarDetalleResumenMovimientosFactura(con, codigoFactura));
		//*********FIN DE LLENADOS DE LOS MAPA INDEPENDIENTES PARA LA INFORMACIÓN DEL DETALLE**********
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleDeudoresFacturas");
	}

	/**
	 * Metodo implementado para imprimir la Consulta de Movimientos
	 * de Deudor (Todos los arrojados por la consulta)
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, ConsultaMovimientoFacturaForm forma, ActionMapping mapping, HttpServletRequest request, ConsultaMovimientoFactura mundo, UsuarioBasico usuario)
	{
		String nombreRptDesign = "ConsultaMovimientoFactura.rptdesign", condiciones = "";
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
        DesignEngineApi comp;
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturasVarias/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v = new Vector();
        v.add(institucion.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
        v.add(institucion.getDireccion());
        v.add("Tels. "+institucion.getTelefono());
        comp.insertLabelInGridOfMasterPage(0, 1, v);
        
        //Párametros de Búsqueda
        v = new Vector();
        v.add("FACTURAS VARIAS - MOVIMIENTO POR FACTURA");
        v.add("PERIODO: "+forma.getFechaInicial()+" - "+forma.getFechaFinal());
        //Insertamos el vector con los parametros de consulta
        comp.insertLabelInGridOfMasterPageWithProperties(1, 1, v, DesignChoiceConstants.TEXT_ALIGN_CENTER);
        
        //Información de Pie de Pagina
        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuario.getLoginUsuario());
        
        //Consultamos la condiciones seleccionadas para la consulta de movimiento deudor
        condiciones = mundo.consultarCondicionesMovimientosFactura(con, forma);
        
        //Obtenemos el DataSet y lo modificamos
        comp.obtenerComponentesDataSet("MovimientoFactura");
		String newQuery = comp.obtenerQueryDataSet().replace("1=2", condiciones);
        logger.info("=====>Consulta en el BIRT: "+newQuery);
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
		return mapping.findForward("listadoDeudoresFacturas");
	}

	/**
	 * Método utilizado para consultar los movimientos
	 * de deudores según los criterios seleccionados en 
	 * la vista de la funcionalidad 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBuscar(Connection con, ConsultaMovimientoFacturaForm forma, ActionMapping mapping, ConsultaMovimientoFactura mundo, UsuarioBasico usuario,HttpSession request)
	{
		//Llenamos el mapa con los resultados arrojados por la consulta de movimientos de deudores
		forma.setConsultaMovimientoFactura(mundo.consultarMovimientosFactura(con, forma));
		//Número de registros del mapa de Consulta de Movimiento de Deudores 
		int numRegistros = Utilidades.convertirAEntero(forma.getConsultaMovimientoFactura("numRegistros")+"");
		
		
		
		//Validamos si numRegistros es mayor a cero para realizar el Totalizado
		if(numRegistros > 0)
		{
			ArrayList<Double> totales= new ArrayList() ;
			double  totalValorInicial = 0, totalValorAjusteDebito = 0, totalValorAjusteCredito = 0, totalPagosAplicados = 0, totalSaldo = 0;  
			//Recorremos el mapa con el fin de sumar cada registros en unas variables totalizadas que serán ingresadas en el mapa después de salir del ciclo
			int w=1;
			int i=0;
			for(i=0; i<numRegistros-1; i++)
			{ 
			 if(ValoresPorDefecto.getIntegridadDominio(forma.getConsultaMovimientoFactura("estado_"+i)+"") == ValoresPorDefecto.getIntegridadDominio(forma.getConsultaMovimientoFactura("estado_"+w)+"")  && forma.getConsultaMovimientoFactura("consecutivo_"+i).equals(forma.getConsultaMovimientoFactura("consecutivo_"+w))){
				totalValorInicial = totalValorInicial + Utilidades.convertirADouble(forma.getConsultaMovimientoFactura("valorinicial_"+i)+"");
				totalValorAjusteDebito = totalValorAjusteDebito + Utilidades.convertirADouble(forma.getConsultaMovimientoFactura("ajustesdebito_"+i)+"");
				totalValorAjusteCredito = totalValorAjusteCredito + Utilidades.convertirADouble(forma.getConsultaMovimientoFactura("ajustescredito_"+i)+"");
				totalPagosAplicados = totalPagosAplicados + Utilidades.convertirADouble(forma.getConsultaMovimientoFactura("pagosaplicados_"+i)+"");
				totalSaldo = totalSaldo + Utilidades.convertirADouble(forma.getConsultaMovimientoFactura("saldo_"+i)+"");
				
			 }else{
				 	totalValorInicial = totalValorInicial + Utilidades.convertirADouble(forma.getConsultaMovimientoFactura("valorinicial_"+i)+"");
					totalValorAjusteDebito = totalValorAjusteDebito + Utilidades.convertirADouble(forma.getConsultaMovimientoFactura("ajustesdebito_"+i)+"");
					totalValorAjusteCredito = totalValorAjusteCredito + Utilidades.convertirADouble(forma.getConsultaMovimientoFactura("ajustescredito_"+i)+"");
					totalPagosAplicados = totalPagosAplicados + Utilidades.convertirADouble(forma.getConsultaMovimientoFactura("pagosaplicados_"+i)+"");
					totalSaldo = totalSaldo + Utilidades.convertirADouble(forma.getConsultaMovimientoFactura("saldo_"+i)+"");
					
					//Agregamos los totales calculados en el mapa de datos de Consulta de Movimiento de Deudores
					forma.setConsultaMovimientoFactura("totalValorInicial", totalValorInicial);
					forma.setConsultaMovimientoFactura("totalValorAjusteDebito", totalValorAjusteDebito);
					forma.setConsultaMovimientoFactura("totalValorAjusteCredito", totalValorAjusteCredito);
					forma.setConsultaMovimientoFactura("totalPagosAplicados", totalPagosAplicados);
					forma.setConsultaMovimientoFactura("totalSaldo", totalSaldo);
					
					totalValorInicial = 0; totalValorAjusteDebito = 0; totalValorAjusteCredito = 0; totalPagosAplicados = 0; totalSaldo = 0; 
			 }
			 w++;
			}
			
			//Agregamos los totales calculados en el mapa de datos de Consulta de Movimiento de Deudores
			forma.setConsultaMovimientoFactura("totalValorInicial", totalValorInicial);
			forma.setConsultaMovimientoFactura("totalValorAjusteDebito", totalValorAjusteDebito);
			forma.setConsultaMovimientoFactura("totalValorAjusteCredito", totalValorAjusteCredito);
			forma.setConsultaMovimientoFactura("totalPagosAplicados", totalPagosAplicados);
			forma.setConsultaMovimientoFactura("totalSaldo", totalSaldo);
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("listadoDeudoresFacturas");
		}else{
		   	ActionErrors errores=new ActionErrors();
            errores.add("No se encontraron registros", new ActionMessage("errors.modFactsVarsReporteFacturasVarias"));
            saveErrors(request, errores);
            UtilidadBD.closeConnection(con);
            return mapping.findForward("principal");
        }
	
	}

	/**
	 * Metodo que llena y carga el select de deudores
	 * según lo seleccionado en el select de tipo de
	 * deudor  
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private ActionForward accionRecargar(Connection con, ConsultaMovimientoFacturaForm forma, ActionMapping mapping, ConsultaMovimientoFactura mundo, UsuarioBasico usuario)
	{
		ArrayList temp = new ArrayList<HashMap<String,Object>>();
		//Validamos que se halla seleccionado un tipo de deudor para llenar el arraylist de deudores
		if(UtilidadCadena.noEsVacio(forma.getTipoDeudor()))
			forma.setDeudores(Utilidades.obtenerDeudores(con, forma.getTipoDeudor()));
		else
			forma.setDeudores(temp);
			
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
}