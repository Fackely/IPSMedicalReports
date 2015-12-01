package com.servinte.axioma.mundo.impl.facturasvarias;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.reportes.ConsultasBirt;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.consultaExterna.DtoConceptoFacturaVaria;
import com.princetonsa.dto.consultaExterna.DtoMultasCitas;
import com.princetonsa.dto.facturasVarias.DtoFacturaVaria;
import com.princetonsa.dto.facturasVarias.DtoFacturaVariaGenerico;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.ConsecutivosCentroAtencion;
import com.princetonsa.mundo.facturacion.ValidacionesFacturaOdontologica;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.dao.fabrica.facturasvarias.FacturasVariasDAOFabrica;
import com.servinte.axioma.dao.interfaz.facturaVarias.IFacturasVariasDAO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.consultaexterna.ConsultaExternaFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroAtencionMundo;
import com.servinte.axioma.mundo.interfaz.consultaexterna.IMultasCitasMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IHistoricoEncabezadoMundo;
import com.servinte.axioma.mundo.interfaz.facturasvarias.IFacturasVariasMundo;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.CentrosCosto;
import com.servinte.axioma.orm.ConceptosFacturasVarias;
import com.servinte.axioma.orm.ConsecutivosCentroAten;
import com.servinte.axioma.orm.Deudores;
import com.servinte.axioma.orm.FacturasVarias;
import com.servinte.axioma.orm.HistoricoEncabezado;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.MultasCitas;
import com.servinte.axioma.orm.MultasFacturasVarias;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.facturasVarias.DeudoresDelegate;

public class FacturasVariasMundo implements IFacturasVariasMundo{

	
	@Override
	public void generarFacturaVaria(DtoFacturaVaria dtoFacturaVaria) {
		FacturasVarias facturaVaria=new FacturasVarias();
		
		Long consecutivo = new Long(ConstantesBD.codigoNuncaValidoLong);
		
		UsuarioBasico usuarioBasico = new UsuarioBasico();
		
		try {
			
			usuarioBasico.cargarUsuarioBasico(dtoFacturaVaria.getUsuarioModifica());
			usuarioBasico.setCodigoCentroAtencion(dtoFacturaVaria.getCentroAtencion());
			
		} catch (SQLException e) {

			e.printStackTrace();
		}
		
		/*
		 *  public static String acronimoTipoConsecutivoUnicoFacturaFacturasVarias = "CFFV";
			public static String acronimoTipoConsecutivoPropiFacturasVarias = "CPFV";
		 */
		
		
		
		int tipoFuenteDatos = ConstantesBD.codigoNuncaValido;
		HistoricoEncabezado historicoEncabezado = new HistoricoEncabezado();
		
		Connection con = HibernateUtil.obtenerConexion();

		/*
		 * Se va a manejar el consecutivo Propio de Facturas Varias.
		 */
		if(ConstantesIntegridadDominio.acronimoTipoConsecutivoPropiFacturasVarias.equals(ValoresPorDefecto.getTipoConsecutivoManejar(dtoFacturaVaria.getInstitucion()))){
			
			tipoFuenteDatos = obtenerTipoFuenteDatosFacturaVaria(usuarioBasico);
			if(tipoFuenteDatos==0)
			{
				consecutivo = ConsecutivosCentroAtencion.incrementarConsecutivoXCentroAtencion(usuarioBasico.getCodigoCentroAtencion(), ConstantesBD.nombreConsecutivoFacturasVarias, usuarioBasico.getCodigoInstitucionInt()).longValue();
			}
			else{
				consecutivo = new Long(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoFacturasVarias, dtoFacturaVaria.getInstitucion()));
			}
			obtenerDatosParametrizacionParaFacturaVaria(historicoEncabezado, tipoFuenteDatos, usuarioBasico.getCodigoCentroAtencion());
			
		}else{
			
			/*
			 * Se maneja un único consecutivo
			 */
			
			ValidacionesFacturaOdontologica validaciones= new ValidacionesFacturaOdontologica();
			
			tipoFuenteDatos = validaciones.obtenerTipoFuenteDatosFactura(usuarioBasico);
			consecutivo = validaciones.obtenerSiguienteConsecutivoFactura(con, usuarioBasico, tipoFuenteDatos).longValue();
			validaciones.obtenerDatosParametrizacionParaFactura(historicoEncabezado, tipoFuenteDatos, usuarioBasico.getCodigoCentroAtencion());
		}
	
		dtoFacturaVaria.setConsecutivo(consecutivo);

		facturaVaria.setConsecutivo(dtoFacturaVaria.getConsecutivo());
		facturaVaria.setEstadoFactura(dtoFacturaVaria.getEstadoFactura());
		
		CentroAtencion centroAtencion=new CentroAtencion();
		centroAtencion.setConsecutivo(dtoFacturaVaria.getCentroAtencion());
		facturaVaria.setCentroAtencion(centroAtencion);
		
		CentrosCosto centroCosto=new CentrosCosto();
		centroCosto.setCodigo(dtoFacturaVaria.getCentroCosto());
		facturaVaria.setCentrosCosto(centroCosto);
		
		Deudores deudor=new DeudoresDelegate().findById(dtoFacturaVaria.getDeudor());
		/*Deudores deudor=new Deudores();
		deudor.setCodigo(dtoFacturaVaria.getDeudor());*/
		facturaVaria.setDeudores(deudor);
		
		Usuarios usuario=new Usuarios();
		usuario.setLogin(dtoFacturaVaria.getUsuarioModifica());
		facturaVaria.setUsuariosByUsuarioModifica(usuario);
		facturaVaria.setUsuariosByUsuarioAprobacion(usuario);
		
		facturaVaria.setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
		facturaVaria.setHoraModifica(UtilidadFecha.getHoraActual());
		
		ConceptosFacturasVarias concepto=new ConceptosFacturasVarias();
		concepto.setConsecutivo(dtoFacturaVaria.getConcepto());
		facturaVaria.setConceptosFacturasVarias(concepto);

		Instituciones institucion=new Instituciones();
		institucion.setCodigo(dtoFacturaVaria.getInstitucion());
		facturaVaria.setInstituciones(institucion);
		
		Date fecha=UtilidadFecha.conversionFormatoFechaStringDate(dtoFacturaVaria.getFecha());
		facturaVaria.setFecha(fecha);
		
		Date fechaAprobacion=UtilidadFecha.conversionFormatoFechaStringDate(dtoFacturaVaria.getFecha());
		fecha=UtilidadFecha.conversionFormatoFechaStringDate(dtoFacturaVaria.getFechaAprobacion());
		facturaVaria.setFechaAprobacion(fechaAprobacion);
		
		facturaVaria.setFechaGenAprobacion(UtilidadFecha.getFechaActualTipoBD());
		
		facturaVaria.setValorFactura(dtoFacturaVaria.getValorFactura());
		
		facturaVaria.setContabilizado(ConstantesBD.acronimoNo);
		facturaVaria.setContabilizadoAnulacion(ConstantesBD.acronimoNo);

		facturaVaria.setHistoricoEncabezado(historicoEncabezado);
		
		IFacturasVariasDAO facturaDAO=FacturasVariasDAOFabrica.crearFacturasVariasDAO();
		
		if(dtoFacturaVaria.getMultasCitas()!=null && dtoFacturaVaria.getMultasCitas().size() > 0){
			
			asociarMultasAFactura (dtoFacturaVaria.getMultasCitas(), usuario, fecha, facturaVaria);
		}
		
		facturaDAO.generarFacturaVaria(facturaVaria);
		
		dtoFacturaVaria.setCodigoFacturaVaria(facturaVaria.getCodigoFacVar());

	
		/*
		 * Consecutivo Propio de Facturas Varias.
		 */
		if(ConstantesIntegridadDominio.acronimoTipoConsecutivoPropiFacturasVarias.equals(ValoresPorDefecto.getTipoConsecutivoManejar(dtoFacturaVaria.getInstitucion()))){
			
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoFacturasVarias, dtoFacturaVaria.getInstitucion(), consecutivo.toString(), ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);

		}else{

			ArrayList<Double> listaConsecutivos= new ArrayList<Double>();
			listaConsecutivos.add(new Double(consecutivo));
			
			ValidacionesFacturaOdontologica.finalizarConsecutivos(con, listaConsecutivos, usuarioBasico.getCodigoInstitucionInt());
		}
		
		
	}

	/**
	 * 
	 * Método que se encarga de asociar a las multas existentes, las multas de la cita y 
	 * luego a la factura Varia que se va a generar.
	 * 
	 * @param multasCitas
	 * @param fecha 
	 * @param usuario 
	 * @param facturaVaria 
	 * @return 
	 */
	private void asociarMultasAFactura(ArrayList<DtoMultasCitas> multasCitas, Usuarios usuario, Date fecha, FacturasVarias facturaVaria) {
		
		HashSet<MultasFacturasVarias> multasFacturasVarias = new HashSet<MultasFacturasVarias>();
		
		IMultasCitasMundo multasCitasMundo =  ConsultaExternaFabricaMundo.crearMultasCitasMundo();
		
		for (DtoMultasCitas multa : multasCitas) {
			
			if(multa!=null){
				
				MultasCitas multaCita = multasCitasMundo.obtenerMultaCitaPorCodigo(multa.getCodigoMulta());
				
				if(multaCita!=null){
					
					MultasFacturasVarias multaFacturasVaria = new MultasFacturasVarias();
					
					multaFacturasVaria.setFacturasVarias(facturaVaria);
					multaFacturasVaria.setMultasCitas(multaCita);
					multaFacturasVaria.setFechaModifica(fecha);
					multaFacturasVaria.setUsuarios(usuario);
					multaFacturasVaria.setHoraModifica(UtilidadFecha.getHoraActual());
					
					multasFacturasVarias.add(multaFacturasVaria);
				}
			}
		}
		
		facturaVaria.setMultasFacturasVariases(multasFacturasVarias);
	}

	@Override
	public ArrayList<DtoConceptoFacturaVaria> listarConceptosFacturasVarias() {
		IFacturasVariasDAO facturaDAO=FacturasVariasDAOFabrica.crearFacturasVariasDAO();
		return facturaDAO.listarConceptosFacturasVarias();
	}
	
	@Override
	public boolean generarReporte(Connection con, DtoFacturaVariaGenerico dto, HttpServletRequest request){
		

		String nombreRptDesign = "ConsultaImpresionFacturasVarias.rptdesign";
		
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		 DesignEngineApi comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturasVarias/",nombreRptDesign);
		 
		 boolean manejaMultiInstitucion= UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(dto.getCodigoInstitucion()));
		 int codigoInstitucion=0;
		 
		if(Utilidades.convertirAEntero(ValoresPorDefecto.getFormatoFacturaVaria(dto.getCodigoInstitucion()))==ConstantesBD.codigoFormatoImpresionEstandar) // carta
		{
			nombreRptDesign = "ConsultaImpresionFacturasVarias.rptdesign";
			
			
			
			//Informacion del Cabezote
			comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturasVarias/",nombreRptDesign);
			
	        
	        comp.insertImageHeaderOfMasterPage1(0, 0, institucion.getLogoReportes());
	        comp.insertGridHeaderOfMasterPage(0,1,1,5);
	        
	        Vector v = new Vector();
	        
	        if (institucion.getRazonSocial() != null) {
	        	 v.add(institucion.getRazonSocial());
			}
	        if (dto.getCentroAtencionUsuarioActual() != null) {
	        	v.add(dto.getCentroAtencionUsuarioActual());
			}
	       
	        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
	        
	        if (institucion.getDireccion() != null) {
	        	 v.add(institucion.getDireccion());
			}
	        
	        if (institucion.getTelefono() != null) {
	        	v.add("Tels. "+institucion.getTelefono());
			}
	       
	        comp.insertLabelInGridOfMasterPage(0, 1, v);
	        
	        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+dto.getLoginUsuarioActual());
	        comp.insertLabelInGridPpalOfFooter(0, 1, "Fecha: "+UtilidadFecha.getFechaActual());
	        
	        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS

		}
		if(Utilidades.convertirAEntero(ValoresPorDefecto.getFormatoFacturaVaria(dto.getCodigoInstitucion()))==ConstantesBD.codigoFormatoImpresionSonria) // POS
		{
			nombreRptDesign = "ConsultaImpresionFacturasVariasPOS.rptdesign";
			comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturasVarias/",nombreRptDesign);
			
			
			Vector v = new Vector();
//			
//			if(formGenModFacturasVarias.getEstadoFactura().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
//			{
//				v.add(formGenModFacturasVarias.getEstadoFactura());
//				cantidadFilas=4;
//			}
//			String tipoId=institucion.getTipoIdentificacion();
//			if(institucion.getTipoIdentificacion().equals("NI"))
//			{
//				tipoId=institucion.getDescripcionTipoIdentificacion();
//			}
//			v.add(institucion.getRazonSocial());
//			v.add(tipoId+" "+institucion.getNit());
//			v.add("Sucursal: "+usuario.getCentroAtencion());
	//	
//			comp.insertGridHeaderOfMasterPageWithName(0, 1, 1, cantidadFilas, "titulo");
//			
//			comp.insertLabelInGridOfMasterPage(0, 1, v);
		}

		
		
		if(manejaMultiInstitucion){
			codigoInstitucion= Utilidades.convertirAEntero(institucion.getCodigo());
		}
		else{
			codigoInstitucion= dto.getCodigoInstitucion();
		}
		
		comp.obtenerComponentesDataSet("ConsultaFacturasVarias");
		String newquery=ConsultasBirt.impresionFacturaVaria(codigoInstitucion,Utilidades.convertirAEntero(dto.getCodigoFactura()), manejaMultiInstitucion);
		comp.modificarQueryDataSet(newquery);
		
		//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
		comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
		comp.updateJDBCParameters(newPathReport);
		
		// se mandan los parámetros al reporte
		newPathReport += "&institucion="+codigoInstitucion+"&consecutivoFactura="+Utilidades.convertirAEntero(dto.getCodigoFactura());
		Log4JManager.info(newPathReport);
	        
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
		
	
        UtilidadBD.closeConnection(con);
		return true;
	}

	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.facturasvarias.IFacturasVariasMundo#obtenerFacturaVaria(long)
	 */
	public FacturasVarias obtenerFacturaVaria (long codigoFacVar){
		
		IFacturasVariasDAO facturaDAO=FacturasVariasDAOFabrica.crearFacturasVariasDAO();
		return facturaDAO.obtenerFacturaVaria(codigoFacVar);
		
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.facturasvarias.IFacturasVariasMundo#obtenerPrefijoConsecutivo(long)
	 */
	@Override
	public String obtenerPrefijoConsecutivo(long codigoFacVar) {
		
		IFacturasVariasDAO facturaDAO=FacturasVariasDAOFabrica.crearFacturasVariasDAO();
		return facturaDAO.obtenerPrefijoConsecutivo(codigoFacVar);
	}

	@Override
	/**
	 * Método que se encarga de consultar los conceptos de las facturas varias
	 * 
	 * @return
	 */
	public ArrayList<DtoConceptoFacturaVaria> listarConceptosFacturasV() {
		IFacturasVariasDAO facturaDAO=FacturasVariasDAOFabrica.crearFacturasVariasDAO();
		return facturaDAO.listarConceptosFacturasV();
	}
	
	@Override
	/**
	 * Método que se encarga de consultar los estados de facturas varias
	 * 
	 * @return
	 */
	public ArrayList<DtoIntegridadDominio> listarEstadoFacturasVarias(){
		
		String[] listadoEstado = new String[]{
				ConstantesIntegridadDominio.acronimoEstadoGenerado,
				ConstantesIntegridadDominio.acronimoEstadoAprobado,
				ConstantesIntegridadDominio.acronimoEstadoAnulado};
		
		Connection con=UtilidadBD.abrirConexion();
		
		ArrayList<DtoIntegridadDominio> listaEstadoFacturavaria = Utilidades.generarListadoConstantesIntegridadDominio(
				con, listadoEstado, false);
		UtilidadBD.closeConnection(con);
		
		for (DtoIntegridadDominio registro : listaEstadoFacturavaria) {
			
			String indicativo =  registro.getAcronimo();
			
			if (indicativo.trim().equals(ConstantesIntegridadDominio.acronimoEstadoGenerado)) {
				registro.setDescripcion("Generada");
			}
			
			if (indicativo.trim().equals(ConstantesIntegridadDominio.acronimoEstadoAprobado)) {
				registro.setDescripcion("Aprobada");
			}
			
			if (indicativo.trim().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado)) {
				registro.setDescripcion("Anulada");
			}
			
		}
		
		
		
		return listaEstadoFacturavaria;
	}
	
	/**
	 * Método que determina de donde se debe obtener la información para
	 * la generación de la factura.
	 * 
	 * @param usuario
	 * @return
	 */
	public int obtenerTipoFuenteDatosFacturaVaria(UsuarioBasico usuario)
	{
		//1. Maneja Consecutivo Factura por Centro de Atención
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConsecutivoFacturasVariasPorCentroAtencion(usuario.getCodigoInstitucionInt())))
		{
			return 0;
		}
		//2. Institución es multiempresa
		else if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt())))
		{
			return 1;
		}
		//3. Institución
		else 
		{
			return 2;
		}
	}
	
	/**
	 * Método que se encarga de consultar la información necesaria para la
	 * generación de la Factura Varia dependiendo de la parametrización del sistema.
	 * 
	 * tipoFuenteDatos = 0 ----> Centro de Atención
	 * tipoFuenteDatos = 1 ----> Empresa Institución
	 * tipoFuenteDatos = 2 ----> Institución
	 * 
	 * @param historicoEncabezado 
	 * @param tipoFuenteDatos
	 * @param codigoFuenteInformacion
	 */
	public void obtenerDatosParametrizacionParaFacturaVaria (HistoricoEncabezado historicoEncabezado, int tipoFuenteDatos, int codigoFuenteInformacion){
		
		ICentroAtencionMundo centroAtencionMundo = AdministracionFabricaMundo.crearCentroAtencionMundo();
		
		com.servinte.axioma.orm.CentroAtencion centroAtencion = centroAtencionMundo.buscarPorCodigo(codigoFuenteInformacion);
		
		if(centroAtencion!=null){
			
			/*
			 * Sección Encabezado
			 *
			 */
			if (tipoFuenteDatos == 0 || tipoFuenteDatos == 1){
			
				historicoEncabezado.setNombreInstitucion(centroAtencion.getEmpresasInstitucion().getRazonSocial());
				historicoEncabezado.setNitInstitucion(centroAtencion.getEmpresasInstitucion().getNit());
				historicoEncabezado.setDigitoVerificacion(centroAtencion.getEmpresasInstitucion().getDigitoVerificacion()+"");
				historicoEncabezado.setActividadEconomica(centroAtencion.getEmpresasInstitucion().getActividadEco());
				historicoEncabezado.setPiePagina(centroAtencion.getEmpresasInstitucion().getPieFacturaVaria());
				historicoEncabezado.setEncabezado(centroAtencion.getEmpresasInstitucion().getEncabezadoFacturaVaria());
				historicoEncabezado.setSucursal(centroAtencion.getDescripcion());
				historicoEncabezado.setTipoIdentificacionInst(centroAtencion.getEmpresasInstitucion().getTiposIdentificacion().getNombre());
				historicoEncabezado.setCiudadInstitucion(centroAtencion.getEmpresasInstitucion().getCiudades().getDescripcion());
				historicoEncabezado.setDireccionInstitucion(centroAtencion.getEmpresasInstitucion().getDireccion());
				historicoEncabezado.setTelefonoInstitucion(centroAtencion.getEmpresasInstitucion().getTelefono());
				historicoEncabezado.setLogoInstitucion(centroAtencion.getEmpresasInstitucion().getLogo()); 
			}else if (tipoFuenteDatos == 2){
				
				historicoEncabezado.setNombreInstitucion(centroAtencion.getInstituciones().getRazonSocial());
				historicoEncabezado.setNitInstitucion(centroAtencion.getInstituciones().getNit());
				historicoEncabezado.setDigitoVerificacion(centroAtencion.getInstituciones().getDigitoVerificacion()+"");
				historicoEncabezado.setActividadEconomica(centroAtencion.getInstituciones().getActividadEco());
				historicoEncabezado.setPiePagina(centroAtencion.getInstituciones().getPieFacturaVaria());
				historicoEncabezado.setEncabezado(centroAtencion.getInstituciones().getEncabezadoFacturaVaria());
				historicoEncabezado.setSucursal(centroAtencion.getDescripcion());
				historicoEncabezado.setTipoIdentificacionInst(centroAtencion.getInstituciones().getTiposIdentificacion().getNombre());
				historicoEncabezado.setCiudadInstitucion(centroAtencion.getInstituciones().getCiudades().getDescripcion());
				historicoEncabezado.setDireccionInstitucion(centroAtencion.getInstituciones().getDireccion());
				historicoEncabezado.setTelefonoInstitucion(centroAtencion.getInstituciones().getTelefono());
				historicoEncabezado.setLogoInstitucion(centroAtencion.getInstituciones().getLogo());
			}
			
			/*
			 * Información por Centro de Atención
			 */
			if(tipoFuenteDatos == 0){
				
				if(centroAtencion!=null){
					
					historicoEncabezado.setRangoInicial(centroAtencion.getRgoInicFactVaria()+"");
					historicoEncabezado.setRangoFinal(centroAtencion.getRgoFinFactVaria()+"");
					historicoEncabezado.setResolucion(centroAtencion.getResolucionFacturaVaria());
					historicoEncabezado.setPrefijoFactura(centroAtencion.getPrefFacturaVaria());
				}
				
			}else if (tipoFuenteDatos == 1){
				
				/*
				 * Información por Institucion - Multiempresa
				 */
				
				historicoEncabezado.setRangoInicial(centroAtencion.getEmpresasInstitucion().getRgoInicFactVaria()+"");
				historicoEncabezado.setRangoFinal(centroAtencion.getEmpresasInstitucion().getRgoFinFactVaria()+"");
				historicoEncabezado.setResolucion(centroAtencion.getEmpresasInstitucion().getResolucionFacturaVaria());
				historicoEncabezado.setPrefijoFactura(centroAtencion.getEmpresasInstitucion().getPrefFacturaVaria());
	
				
			}else if (tipoFuenteDatos == 2){
				
				/*
				 * Información por Institución
				 */

				historicoEncabezado.setRangoInicial(centroAtencion.getInstituciones().getRgoInicFactVaria()+"");
				historicoEncabezado.setRangoFinal(centroAtencion.getInstituciones().getRgoFinFactVaria()+"");
				historicoEncabezado.setResolucion(centroAtencion.getInstituciones().getResolucionFacturaVaria());
				historicoEncabezado.setPrefijoFactura(centroAtencion.getInstituciones().getPrefFacturaVaria());
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.facturasvarias.IFacturasVariasMundo#obtenerHistoricoEncabezadoFacturaVaria(long)
	 */
	public HistoricoEncabezado obtenerHistoricoEncabezadoFacturaVaria(long consecutivo) {
		IHistoricoEncabezadoMundo historicoEncabezadoMundo = FacturacionFabricaMundo.crearHistoricoEncabezadoMundo();
		long codigoFacturaVaria = this.obtenerCodigoFacturaVaria(consecutivo);
		HistoricoEncabezado historicoEncabezado = null;
		if (codigoFacturaVaria > 0) {
			historicoEncabezado = historicoEncabezadoMundo.findById(
					this.obtenerFacturaVaria(codigoFacturaVaria).getHistoricoEncabezado().getCodigoPk());
		}
		return historicoEncabezado;
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.facturasvarias.IFacturasVariasMundo#obtenerCodigoFacturaVaria(long)
	 */
	@Override
	public long obtenerCodigoFacturaVaria(long consecutivo) {
		IFacturasVariasDAO facturaDAO = FacturasVariasDAOFabrica.crearFacturasVariasDAO();
		return facturaDAO.obtenerCodigoFacturaVaria(consecutivo);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.facturasvarias.IFacturasVariasMundo#obtenerConsecutivoFacturaVaria(long)
	 */
	@Override
	public long obtenerConsecutivoFacturaVaria(long codigoFacVar) {
		IFacturasVariasDAO facturaDAO = FacturasVariasDAOFabrica.crearFacturasVariasDAO();
		return facturaDAO.obtenerConsecutivoFacturaVaria(codigoFacVar);
	}
	
}
