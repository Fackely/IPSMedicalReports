package com.servinte.axioma.mundo.impl.tesoreria;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.management.RuntimeErrorException;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadN2T;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.reportes.ConsultasBirt;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ParamInstitucionDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoTiposMoneda;
import com.princetonsa.dto.tesoreria.DtoAnticiposRecibidosConvenio;
import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.princetonsa.dto.tesoreria.DtoFormaPagoReport;
import com.princetonsa.dto.tesoreria.DtoReciboDevolucion;
import com.princetonsa.dto.tesoreria.DtoRecibosConceptoAnticiposRecibidosConvenio;
import com.princetonsa.dto.tesoreria.DtoReporteAnticiposRecibidosConvenio;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.tesoreria.ConsultaRecibosCaja;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IRecibosCajaDAO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.interfaz.tesoreria.IRecibosCajaMundo;
import com.servinte.axioma.orm.EstadosRecibosCaja;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.RecibosCaja;
import com.servinte.axioma.orm.RecibosCajaId;
import com.servinte.axioma.orm.RecibosCajaXTurno;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.orm.delegate.tesoreria.RecibosCajaXTurnoDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.TurnoDeCajaDelegate;


/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con los Recibos de Cajas
 * 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see IRecibosCajaMundo
 */
public class RecibosCajaMundo implements IRecibosCajaMundo {

	/**
	 * DAO de los Recibos de Caja.
	 */
	private IRecibosCajaDAO recibosCajaDAO;

	public RecibosCajaMundo() {
		inicializar();
	}
	
	private void inicializar() {
		recibosCajaDAO = TesoreriaFabricaDAO.crearRecibosCajaDAO();
	}
	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IRecibosCajaMundo#obtenerRecibosXMovimientoCaja(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoReciboDevolucion> obtenerRecibosXMovimientoCaja(MovimientosCaja movimientosCaja)  {

		return recibosCajaDAO.obtenerRecibosXMovimientoCaja(movimientosCaja);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IRecibosCajaMundo#obtenerRecibosXMovimientoCajaFecha(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<DtoReciboDevolucion> obtenerRecibosXMovimientoCajaFecha(MovimientosCaja movimientosCaja)  {

		return recibosCajaDAO.obtenerRecibosXMovimientoCajaFecha(movimientosCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IRecibosCajaMundo#actualizarRecibosAsociadosCierre(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public List<RecibosCaja> actualizarRecibosAsociadosCierre(MovimientosCaja movimientosCaja) {
		
		return recibosCajaDAO.actualizarRecibosAsociadosCierre(movimientosCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IRecibosCajaMundo#obtenerFechaUltimoMovimientoRecibo(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public Date obtenerFechaUltimoMovimientoRecibo(TurnoDeCaja turnoDeCaja) {
		
		return recibosCajaDAO.obtenerFechaUltimoMovimientoRecibo(turnoDeCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IRecibosCajaMundo#obtenerRecibosNoAnuladosNoFormaPagoNinguna(int, com.servinte.axioma.orm.TurnoDeCaja, boolean)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerRecibosNoAnuladosNoFormaPagoNinguna(	int institucion, TurnoDeCaja turnoDeCaja, boolean directoBanco) {
		
		return recibosCajaDAO.obtenerRecibosNoAnuladosNoFormaPagoNinguna(institucion, turnoDeCaja, directoBanco);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IRecibosCajaMundo#obtenerTotalRecibosNoAnulFormaPagoNinguno(int, com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerTotalesRecibosNoAnulFormaPagoNinguno(int institucion, TurnoDeCaja turnoDeCaja) {
		
		return recibosCajaDAO.obtenerTotalesRecibosNoAnulFormaPagoNinguno(institucion, turnoDeCaja);
	}

	@Override
	public void generarReciboCaja(RecibosCaja dtoRecibo) {
		
		recibosCajaDAO.generarReciboCaja(dtoRecibo);
		
		ArrayList<TurnoDeCaja> listaTurnos=new TurnoDeCajaDelegate().obtenerTurnoCajaAbiertoPorCaja(dtoRecibo.getCajasCajeros().getCajas());
		if(listaTurnos!=null && listaTurnos.size()>0)
		{
			RecibosCajaXTurno recibosCajaXTurno=new RecibosCajaXTurno();
			recibosCajaXTurno.setRecibosCaja(dtoRecibo);
			recibosCajaXTurno.setTurnoDeCaja(listaTurnos.get(0));
			new RecibosCajaXTurnoDelegate().persist(recibosCajaXTurno);
		}
		else{
			throw new RuntimeErrorException(new Error("Usuario no tiene turno de caja abierto"));
		}
	}

	@Override
	public RecibosCaja findById(RecibosCajaId id) {
		return recibosCajaDAO.findById(id);
	}
	
	
	/**
	 * M�todo que se encarga de realizar la impresi�n del recibo de caja
	 * 
	 * @param usuario
	 * @param institucion
	 * @param parametros
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String imprimirReciboCaja(UsuarioBasico usuario, InstitucionBasica institucion, HashMap<String, String> parametros) {
		
	
		String numReciboCaja = parametros.get("numReciboCaja");
		String consecutivoRC = parametros.get("consecutivorc");
		
		IRecibosCajaDAO recibosCajaDAO = TesoreriaFabricaDAO.crearRecibosCajaDAO();
		
		RecibosCajaId recibosCajaId = new RecibosCajaId();
	
		recibosCajaId.setInstitucion(usuario.getCodigoInstitucionInt());
		recibosCajaId.setNumeroReciboCaja(numReciboCaja);
		RecibosCaja recibo = recibosCajaDAO.findById(recibosCajaId);
		
		String fechaRecibo = "";
		String horaRecibo = "";
		
		if(recibo!=null){
			
			fechaRecibo = UtilidadFecha.conversionFormatoFechaAAp(recibo.getFecha());
			horaRecibo =  UtilidadFecha.conversionFormatoHoraAAp(recibo.getHora());
		
		}else{
			
			fechaRecibo = UtilidadFecha.getFechaActual();
			horaRecibo =  UtilidadFecha.getHoraActual();
		}
		
		
		String nombreRptDesign="";
		
		Vector<String> v;
		String newPathReport = "";
		
		String formatoImpresion=ValoresPorDefecto.getTamanioImpresionRC(usuario.getCodigoInstitucionInt());
		if (formatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionRCPOS))
			nombreRptDesign="ReciboCajaPOS.rptdesign";
		else //if (formatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionRCCarta))
			nombreRptDesign="ReciboCajaCarta.rptdesign";
		
		// ***************** INFORMACI�N DEL CABEZOTE
		DesignEngineApi comp;
		comp = new DesignEngineApi(ParamsBirtApplication.getReportsPath()+ "tesoreria/", nombreRptDesign);

		// Logo
		if (formatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionRCCarta))
		{
			String logo="";
			Connection con = null;
			try {
				ParamInstitucionDao institucionDao=null;
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				if (myFactory != null) {
					institucionDao = myFactory.getParamInstitucionDao();
				}
				con = UtilidadBD.abrirConexion();
				ResultSetDecorator rs=institucionDao.consultaInstituciones(con,
						institucion.getCodigoInstitucionBasica(), "0", "0", "0", false);
				if (rs.next()) {
					if (!UtilidadFileUpload.existeArchivoRutaCompelta(rs
							.getString("logo"))) {
						logo = "";
					} else {
						logo=rs.getString("logo");
					}
					
				}
				rs.close();
				
				UtilidadBD.cerrarConexion(con);
			} catch (SQLException e) {
				UtilidadBD.cerrarObjetosPersistencia(null, null, con);
				HibernateUtil.abortTransaction();
				e.printStackTrace();
			}
			
			String filePath="";//ValoresPorDefecto.getFilePath();
			if(UtilidadTexto.isEmpty(logo))
			{
		        filePath= institucion.getLogoReportes();
			}
			else
			{
				filePath=logo;
			}
	        
	        Log4JManager.info("filepath image->"+filePath);
	        Log4JManager.info("Ubicacion del Logo en el Reporte: " + institucion.getUbicacionLogo());
	        
	        comp.insertImageHeaderOfMasterPage1(0, 0, filePath);
		}
			

		// Nombre Instituci�n, titulo 
		comp.insertGridHeaderOfMasterPageWithName(0, 1, 1, 2, "titulo");
			
		v = new Vector<String>();
		
		String digitoVerificacion = !UtilidadTexto.isEmpty(institucion.getDigitoVerificacion()) ? " - " + institucion.getDigitoVerificacion() : "";
	
		v.add(institucion.getRazonSocial()+ " NIT " + institucion.getNit() + digitoVerificacion);
		v.add(institucion.getDireccion()+ " Tels: "+institucion.getTelefono());
		
		comp.insertLabelInGridOfMasterPage(0, 1, v);

		// Fecha hora de proceso y usuario
		comp.insertLabelInGridPpalOfFooter(0, 0, "Fecha: "+ UtilidadFecha.getFechaActual() + " Hora:"+ UtilidadFecha.getHoraActual());
		comp.insertLabelInGridPpalOfFooter(0, 1, "Usuario:"+ usuario.getLoginUsuario());
		// ****************** FIN INFORMACI�N DEL CABEZOTE"
		
		// ***************** NUEVAS CONSULTAS DEL REPORTE
		String newquery = "";
		
		//Obtengo la neuva consulta de conceptos rc
		comp.obtenerComponentesDataSet("conceptosRC");
		newquery=ConsultasBirt.impresionConceptosReciboCaja(usuario.getCodigoInstitucionInt(),numReciboCaja);
		comp.modificarQueryDataSet(newquery);
		
		//Obtengo la nueva consulta de pagos RC
		comp.obtenerComponentesDataSet("pagosRC");
		newquery=ConsultasBirt.impresionTotalesReciboCaja(usuario.getCodigoInstitucionInt(),numReciboCaja);
		comp.modificarQueryDataSet(newquery);
  
		comp.lowerAliasDataSet(); 			
		newPathReport = comp.saveReport1(false);
		comp.updateJDBCParameters(newPathReport);
		
		//Consulto el total para luego convertirlo en letras
		ConsultaRecibosCaja mundoConsultaRecibos=new ConsultaRecibosCaja();
		HashMap<String, Object> mapaTotalPagos=mundoConsultaRecibos.consultarTotalesPagos(usuario.getCodigoInstitucionInt(),numReciboCaja+"");
		double totalPagos=0;
		
		for(int i=0;i<Utilidades.convertirAEntero(mapaTotalPagos.get(("numRegistros"))+"");i++)
			totalPagos+=Utilidades.convertirADouble(mapaTotalPagos.get("valor_"+i)+"");
		
		//FIXME La unidad Decimales no debe pasarse de esta manera.
		//DtoTiposMoneda tipoMoneda=UtilConversionMonedas.obtenerTipoMonedaManejaInstitucion(usuario.getCodigoInstitucionInt());
		
		/**
		 * Para el metodo onvertirLetras se postula el tipo de moneda 'pesos' ya que la parametrica por instituci�n 
		 * para el tipo de moneda no se encuentra completo.
		 * @author Diana Ruiz
		 * @since 30/08/2011
		 */
		String monedaLetras=UtilidadN2T.convertirLetras(UtilidadTexto.formatearValores(totalPagos+"" ).replaceAll(",", "")," peso","centavos MCTE");
		
		
		
		
		
		//Envio los elementos que se necesitan mostrar por parametro
		newPathReport += 	"&recibocaja="+consecutivoRC+
							"&fechaemision="+fechaRecibo+
							"&horaemision="+horaRecibo+
							"&caja="+usuario.getDescripcionCaja()+
							"&estado="+(Utilidades.obtenerEstadoReciboCaja(numReciboCaja,usuario.getCodigoInstitucionInt())).split(ConstantesBD.separadorSplit)[1]+
							"&recibidode="+Utilidades.reciboCajaRecibidoDe(usuario.getCodigoInstitucionInt(),numReciboCaja)+
							"&tipoid="+ parametros.get("tipoIdentificacion") +
							"&nroid="+ parametros.get("identificacionPaciente")+
							"&observaciones="+ parametros.get("observacionesImprimir")+
							"&usuariogenero="+parametros.get("usuarioElabora")+
							"&totalletras="+monedaLetras+
							"&numReciboCaja="+numReciboCaja;
		
		if (parametros.get("funcionalidadOrigen")!=null && parametros.get("funcionalidadOrigen").equals("ConsultaRecibosCaja")){
		
			Connection con = HibernateUtil.obtenerConexion();
			
			newPathReport +="&reimpresion="+usuario.getNombreUsuario()+
							"&fechaReimpresion="+UtilidadFecha.getFechaActual(con)+" "+UtilidadFecha.getHoraActual();
		}
		
		HibernateUtil.endTransaction();
		
		return newPathReport.replaceAll("\\n", " ").replaceAll("\r", " ");

	}

	@Override
	public ArrayList<EstadosRecibosCaja> obtenerEstadosRC() {
		return recibosCajaDAO.obtenerEstadosRC();
	}

	@Override
	public ArrayList<DtoAnticiposRecibidosConvenio> obtenerCentrosAtencionRecibosCajaConceptoAnticipos(
			DtoReporteAnticiposRecibidosConvenio dto) {
		return recibosCajaDAO.obtenerCentrosAtencionRecibosCajaConceptoAnticipos(dto);
	}

	@Override
	public ArrayList<DtoRecibosConceptoAnticiposRecibidosConvenio> obtenerRecibosCajaConceptoAnticiposConvenioOdont(
			int consCentroAtencion, DtoReporteAnticiposRecibidosConvenio dto) {
		return recibosCajaDAO.obtenerRecibosCajaConceptoAnticiposConvenioOdont(consCentroAtencion, dto);
	}
	
	@Override
	public ArrayList<DtoFormaPagoReport> cargarFormasPago(
			List<RecibosCajaId> numeroRC) {
		return recibosCajaDAO.cargarFormasPago(numeroRC);
	}

	/**
	 * M&eacute;todo encargado de consolidar la consulta
	 * de Anticipos recibidos del convenio
	 * 
	 * @param DtoReporteAnticiposRecibidosConvenio
	 * @return ArrayList<DtoAnticiposRecibidosConvenio>
	 * @author Diana Carolina G
	 */
	
	public ArrayList<DtoAnticiposRecibidosConvenio>   consolidarConsultaAnticiposRecibidosConvenio (
			DtoReporteAnticiposRecibidosConvenio dto){
		
		IRecibosCajaDAO dao=TesoreriaFabricaDAO.crearRecibosCajaDAO();
		ArrayList<DtoAnticiposRecibidosConvenio> listaAnticipos=dao.obtenerCentrosAtencionRecibosCajaConceptoAnticipos(dto);
		
		List<RecibosCajaId> numeroRC=new ArrayList<RecibosCajaId>();
		
		if(!Utilidades.isEmpty(listaAnticipos)){
			
			int consCentroAtencion=ConstantesBD.codigoNuncaValido;
			
			for(DtoAnticiposRecibidosConvenio registro: listaAnticipos){
				
				consCentroAtencion=registro.getConsCentroAtencion();
				ArrayList<DtoRecibosConceptoAnticiposRecibidosConvenio> listaDetalleRecibosCaja= dao.obtenerRecibosCajaConceptoAnticiposConvenioOdont(consCentroAtencion, dto);
				registro.setListaRecibosCajaXCentroAtencion(listaDetalleRecibosCaja);
				
				if(!Utilidades.isEmpty(listaDetalleRecibosCaja)){
					
					for(DtoRecibosConceptoAnticiposRecibidosConvenio registroRC: listaDetalleRecibosCaja)
					{
						RecibosCajaId id = new RecibosCajaId();
						numeroRC=new ArrayList<RecibosCajaId>();
						id.setInstitucion(registroRC.getCodInstRC());
						id.setNumeroReciboCaja(registroRC.getNumeroRC());
						numeroRC.add(id);
						
						ArrayList<DtoFormaPagoReport> listaFormasPago= dao.cargarFormasPago(numeroRC);
						registroRC.setListaFormaPago(listaFormasPago);
					}
				}
			}
			
			
			
		}
		
		else{
			return null;
		}
		return listaAnticipos;
		
	}

}
