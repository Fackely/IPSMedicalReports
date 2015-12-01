/**
 * Author: Juan Sebastian Castaño
 */
package com.princetonsa.mundo.facturasVarias;

import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.facturasVarias.GenModFacturasVariasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturasVarias.GenModFacturasVariasDao;
import com.princetonsa.dto.consultaExterna.DtoMultasCitas;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.mundo.fabrica.facturasvarias.FacturasVariasMundoFabrica;
import com.servinte.axioma.mundo.interfaz.facturasvarias.IFacturasVariasMundo;
import com.servinte.axioma.orm.HistoricoEncabezado;
import com.servinte.axioma.persistencia.UtilidadTransaccion;



public class GenModFacturasVarias {
	
	
	GenModFacturasVariasDao dao = null;
	
	
	/**
	 * Atributos de la clase facturas varias
	 */
	
	private int consecutivo;
	private String codigoFacVar;
	private String estadoFactura;
	private int centroAtencion;
	private int centroCosto;
	private Date fecha;
	private int concepto;
	private String valorFactura;
	private String tipoDeudor;
	private int deudor;
	private String observaciones;
	
	private ArrayList<DtoMultasCitas> multasFactura = new ArrayList<DtoMultasCitas>();
	

	
	/**
	 * codigo institucion
	 *
	 */
	private int institucion; 
	


	public GenModFacturasVarias()
	{
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	
	public void clean()
	{
		this.institucion = 0;
		this.consecutivo = 0;
		this.codigoFacVar = "";
		this.estadoFactura = "";
		this.centroAtencion = 0;
		this.centroCosto = 0;
		this.fecha = null;
		this.concepto = 0;
		this.valorFactura = "0";
		this.tipoDeudor = "";
		this.deudor = 0;
		this.observaciones = "";
		
		this.multasFactura = new ArrayList<DtoMultasCitas>();
		
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD)
	{
		if (dao == null)
		{
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			dao = myFactory.getGenModFacturasVariasDao();
		}
	}
	
	public HashMap<String, Object> cargarAsocioDeudor(Connection con, String tipoDeudor, int codigoDeudor)
	{
		return dao.cargarAsocioDeudor(con, tipoDeudor, codigoDeudor, institucion);
	}
	
	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	
	/**
	 * Metodo de insercion de un  uevo registro de facturas varias
	 * @param con
	 * @param usuario
	 * @return
	 */
	public ResultadoBoolean insertarFacturaVaria (Connection con, String usuario, HistoricoEncabezado historicoEncabezado)
	{
		//System.out.print("\n el deudore es -->"+deudor );
		int codigoFacturaVaria = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_facturas_varias");
		this.setCodigoFacVar(codigoFacturaVaria+"");
		return dao.insertarFacturaVaria(con, consecutivo, estadoFactura, centroAtencion, centroCosto, fecha, concepto, Utilidades.convertirADouble(valorFactura), deudor, observaciones, usuario, institucion, multasFactura, codigoFacturaVaria, historicoEncabezado);
	}

	
	/**
	 * Metodo de guardado de datos modificados de facturas varias
	 * @param con
	 * @param usuario
	 * @return
	 */
	public boolean guardarModFacturaVaria(Connection con, String usuario)
	{
		return dao.guardarModFacturaVaria(con, consecutivo, codigoFacVar, estadoFactura, centroAtencion, centroCosto, fecha, concepto, Utilidades.convertirADouble(valorFactura),  deudor, observaciones, usuario, institucion,multasFactura);
	}
	
	/**
	 * Metodo encargado de consultar la informacion de las facturas varias.
	 * @param connection
	 * @param codigoFacturaVaria
	 * @param institucion
	 * @return
	 */
	public HashMap buscarFacVar (Connection connection,int codigoFacturaVaria, String institucion)
	{
		return dao.buscarFacturasVarias(connection, codigoFacturaVaria, institucion);
	}
	
	/**
	 * Metodo de consulta de facturas varias
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public HashMap<String, Object> buscarFacturasVarias (Connection con, String fechaInicial, String fechaFinal)
	{
		return dao.buscarFacturasVarias(con, codigoFacVar, centroAtencion, tipoDeudor, deudor, fechaInicial, fechaFinal, institucion);
	}
	
	
	/**
	 * Metodo de consulta de conceptos de facturas varias para realizar el reporte
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public String consultarConceptosFacVarias (Connection con, int consecutivo)
	{
		return dao.consultarConceptosFacVarias(con, consecutivo);
	}
	
	/**
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the centroCosto
	 */
	public int getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @param centroCosto the centroCosto to set
	 */
	public void setCentroCosto(int centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @return the codigoFacVar
	 */
	public String getCodigoFacVar() {
		return codigoFacVar;
	}

	/**
	 * @param codigoFacVar the codigoFacVar to set
	 */
	public void setCodigoFacVar(String codigoFacVar) {
		this.codigoFacVar = codigoFacVar;
	}

	/**
	 * @return the concepto
	 */
	public int getConcepto() {
		return concepto;
	}

	/**
	 * @param concepto the concepto to set
	 */
	public void setConcepto(int concepto) {
		this.concepto = concepto;
	}

	/**
	 * @return the consecutivo
	 */
	public int getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(int consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @return the deudor
	 */
	public int getDeudor() {
		return deudor;
	}

	/**
	 * @param deudor the deudor to set
	 */
	public void setDeudor(int deudor) {
		this.deudor = deudor;
	}

	/**
	 * @return the estadoFactura
	 */
	public String getEstadoFactura() {
		return estadoFactura;
	}

	/**
	 * @param estadoFactura the estadoFactura to set
	 */
	public void setEstadoFactura(String estadoFactura) {
		this.estadoFactura = estadoFactura;
	}

	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return the tipoDeudor
	 */
	public String getTipoDeudor() {
		return tipoDeudor;
	}

	/**
	 * @param tipoDeudor the tipoDeudor to set
	 */
	public void setTipoDeudor(String tipoDeudor) {
		this.tipoDeudor = tipoDeudor;
	}

	/**
	 * @return the valorFactura
	 */
	public String getValorFactura() {
		return valorFactura;
	}

	/**
	 * @param valorFactura the valorFactura to set
	 */
	public void setValorFactura(String valorFactura) {
		this.valorFactura = valorFactura;
	}
	
	
	public void buscarFacturaVar (Connection connection,int codigoFacturaVaria,String institucion, GenModFacturasVariasForm forma) throws ParseException
	{
		HashMap result = new HashMap ();		
		result=buscarFacVar(connection, codigoFacturaVaria, institucion);
		
		if (Utilidades.convertirAEntero(result.get("numRegistros")+"")>0)
		{
			forma.setCodigo(result.get("consecutivo")+"");
			this.codigoFacVar=result.get("codigoFacVar")+"";
			forma.setCodigoFacVar(Utilidades.convertirAEntero(result.get("codigoFacVar")+""));
			forma.setCentroAtencion(result.get("centroAtencion")+"");
			forma.setCentroCosto(result.get("centroCosto")+"");
			forma.setEstadoFactura(result.get("consecutivoFactura")+"");
			forma.setFecha(result.get("fecha")+"");
			forma.setConcepto(result.get("concepto")+ConstantesBD.separadorSplit+result.get("tipoConcepto"));
			forma.setNombreConcepto(result.get("nombreConcepto")+"");
			forma.setValorFactura(result.get("valorFactura")+"");
			forma.setFechaAprobacion(result.get("fechaAprobacion")+"");
			forma.setDeudor(result.get("deudor")+"");
			forma.setObservaciones(result.get("observaciones")+"");
			forma.setDatosDeudor(Deudores.cargar(connection, forma.getDeudor()));
			
			forma.setMultasEncontradas(new ArrayList<DtoMultasCitas>());
			//Si el concepto es tipo multas se cargan las multas asociadas a la factura
			if(result.get("tipoConcepto").toString().equals(ConstantesIntegridadDominio.acronimoMultas))
			{
				forma.setMultasEncontradas(obtenerMultasFactura(connection, this.codigoFacVar));
			}
		}
		
	}
	
	/**
	 * Metodo de consulta del valor del consecutivo que se esta utilizando
	 * @param usuario
	 * @return
	 */
	public String obtenerNombreConsecutivoFacturasVarias(UsuarioBasico usuario)
	{
		String consecutivoManejar = "";
		//verificar que tipo de consecutivo utilizar -- consecutivo tipo_consecutivo_manejar_facturas_varias y acronimo -- CPFAV  Consecutivo propio facturas varias
		
		if (ValoresPorDefecto.getTipoConsecutivoManejar(usuario.getCodigoInstitucionInt()).equals(ConstantesIntegridadDominio.acronimoTipoConsecutivoPropiFacturasVarias))
		{
			consecutivoManejar = ConstantesBD.nombreConsecutivoFacturasVarias;
		
		}else{
		
			if (ValoresPorDefecto.getTipoConsecutivoManejar(usuario.getCodigoInstitucionInt()).equals(ConstantesIntegridadDominio.acronimoTipoConsecutivoUnicoFacturaFacturasVarias))
			{
				consecutivoManejar = ConstantesBD.nombreConsecutivoFacturas;
			}
		}
		
		return consecutivoManejar;
		
	}
	
	
	/**
	 * Método para obtener las multas del paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<DtoMultasCitas> obtenerMultasPaciente(Connection con,String codigoPaciente)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPaciente",codigoPaciente);
		return dao.obtenerMultasPaciente(con, campos);
	}
	
	/**
	 * Método para obtener las multas de la factura
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<DtoMultasCitas> obtenerMultasFactura(Connection con,String consecutivoFactura)
	{
		HashMap campos = new HashMap();
		campos.put("consecutivoFactura",consecutivoFactura);
		return dao.obtenerMultasFactura(con, campos);
	}

	/**
	 * @return the multasFactura
	 */
	public ArrayList<DtoMultasCitas> getMultasFactura() {
		return multasFactura;
	}

	/**
	 * @param multasFactura the multasFactura to set
	 */
	public void setMultasFactura(ArrayList<DtoMultasCitas> multasFactura) {
		this.multasFactura = multasFactura;
	}
	
	
	/**
	 * Método centralizado de impresión de la factura varia
	 * 
	 * @param codigoInstitucion
	 * @param institucion
	 * @param codigoFacturaVaria
	 * @return
	 */
	public String imprimirFacturaVaria (int codigoInstitucion, int institucion, int codigoFacturaVaria, UsuarioBasico usuario){

		String nombreRptDesign = "" ;
		boolean requiereEncabezado = false;
		String rutaLogoInstitucion = "";
		String directorioImagenes = ValoresPorDefecto.getDirectorioImagenes();
		
		if(Utilidades.convertirAEntero(ValoresPorDefecto.getFormatoFacturaVaria(codigoInstitucion))==ConstantesBD.codigoFormatoImpresionEstandar) // carta
		{
			nombreRptDesign = "ConsultaImpresionFacturasVarias.rptdesign";
			requiereEncabezado = true;
		}
		if(Utilidades.convertirAEntero(ValoresPorDefecto.getFormatoFacturaVaria(codigoInstitucion))==ConstantesBD.codigoFormatoImpresionSonria) // POS
		{
			nombreRptDesign = "ConsultaImpresionFacturasVariasPOS.rptdesign";
		}
			
		//Informacion del Cabezote
		DesignEngineApi comp;
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturasVarias/",nombreRptDesign);

		boolean manejaMultiInstitucion= UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion));
		int codigoInstitucionUnico=0;
		
		if(manejaMultiInstitucion){
			codigoInstitucionUnico= institucion;
			
		}
		else{
			codigoInstitucionUnico= codigoInstitucion;

		
		}
		
		if (requiereEncabezado) {
			IFacturasVariasMundo facturasVariasMundo=FacturasVariasMundoFabrica.crearFacturasVariasMundo();
			HistoricoEncabezado historicoEncabezado = facturasVariasMundo.obtenerHistoricoEncabezadoFacturaVaria(codigoFacturaVaria);
			if (historicoEncabezado != null) {
				if (historicoEncabezado.getLogoInstitucion() != null) {
					if(historicoEncabezado.getLogoInstitucion().indexOf("/") > -1) {
						rutaLogoInstitucion = historicoEncabezado.getLogoInstitucion();
					} else {
						rutaLogoInstitucion = directorioImagenes + historicoEncabezado.getLogoInstitucion();
					}
				} else {
					rutaLogoInstitucion = directorioImagenes + "logo_clinica_reporte.gif";
				}
				
				comp.insertImageHeaderOfMasterPage1(0, 0, rutaLogoInstitucion);
				comp.insertGridHeaderOfMasterPage(0,1,1,6);
				comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: " + usuario.getLoginUsuario());
				
				Vector<String> v = new Vector<String>();
				v.add((historicoEncabezado.getNombreInstitucion() != null) ? historicoEncabezado.getNombreInstitucion() : "");
				v.add("");
				v.add((historicoEncabezado.getSucursal() != null) ? historicoEncabezado.getSucursal() : "");
				v.add(historicoEncabezado.getTipoIdentificacionInst() + " " + historicoEncabezado.getNitInstitucion() + "  -  " + historicoEncabezado.getDigitoVerificacion());
				v.add((historicoEncabezado.getDireccionInstitucion() != null) ? historicoEncabezado.getDireccionInstitucion() : "");
				v.add((historicoEncabezado.getCiudadInstitucion() != null) ? historicoEncabezado.getCiudadInstitucion() : "");
				
				
				comp.insertLabelInGridOfMasterPage(0,1, v);
			}
		}
		
		comp.obtenerComponentesDataSet("ConsultaFacturasVarias");
		String newquery=ConsultasBirt.impresionFacturaVaria(codigoInstitucionUnico, codigoFacturaVaria, manejaMultiInstitucion);
		comp.modificarQueryDataSet(newquery);
		
		//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
		comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
		comp.updateJDBCParameters(newPathReport);
		// se mandan los parámetros al reporte
		newPathReport += "&consecutivoFactura="+codigoFacturaVaria;
		
	    UtilidadTransaccion.getTransaccion().commit();  
        if(!UtilidadTexto.isEmpty(newPathReport))
        {
        	return newPathReport;
        	
        }else{
        	
        	return null;
        }
	}
}
