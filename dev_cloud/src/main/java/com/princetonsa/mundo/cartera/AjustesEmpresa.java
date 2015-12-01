/*
 * @(#)AjustesEmpresa.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.cartera;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.AjustesEmpresaDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Clase para el manejo de Ajustes Empresa solo la parte del encabezado.
 * Este clase contendra un coleccion llena con objetos facturas.
 * 
 * @version 1.0, Julio 22 / 2005	
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 */
public class AjustesEmpresa 
{
	/**
	 * DAO utilizado por el objeto parra acceder a la fuente de datos
	 */
	private static AjustesEmpresaDao ajustesDao = null;
    
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(AjustesEmpresa.class);

	
	//private String nombreSecuencia="seq_ajustes_empresa";
	/**
	 * Coleccion para manejar los ajustes de una factura
	 */
	private Collection colAjustesFacturaEmpresa;
	
	/**
	 * Objeto que contiene los ajustes de una factura y de sus respectivos ajustes.
	 */
	private AjustesFacturaEmpresa ajustesFacturaEmpresa;
	
	/**
	 * Variable que contien el codigo de la factura
	 */
	private double codigo;
	
	/**
	 * Variable que contiene el consecutivo del ajuste tomado de la tabla consecutivos.
	 */
	private String consecutivoAjuste;
	
	/**
	 * variable que maneja el tipo de ajuste
	 */
	private int tipoAjuste;
	
	/**
	 * variable que maneja el tipo de ajuste
	 */
	private String tipoAjusteStr;
	
	/**
	 * variable que maneja la institucion del ajuste
	 */
	private int institucion;
	
	/**
	 * variable que indica si el ajuste es de castigoCartera
	 */
	private boolean castigoCartera;
	
	/**
	 * Encaso de ser un castigo esta variable maneja el concepto del castigo.
	 */
	private String conceptoCastigoCartera;
	
	/**
	 * Para manejar la fecha del ajuste.
	 */
	private String fechaAjuste;
	
	/**
	 * Variable para manejar la fecha de elaboracion del ajuste
	 */
	private String fechaElaboracion;
	
	/**
	 * Variable para manejar la hora de elaboracion del ajuste.
	 */
	private String horaElaboracion;
	
	/**
	 * variable para manejar el login del usuario que genera el ajuste.
	 */
	private String usuario;
	
	/**
	 * Variable para manejar la cuenta de cobro a la que se le aplica el ajuste.
	 * pude ser null cuando el ajuste se hace directamente a una factura.
	 */
	private double cuentaCobro;
	private double saldoCuentCobro;
	
	/**
	 * El concepto que se maneja del ajuste.
	 */
	private String conceptoAjuste;
	
	/**
	 * Metodo que se utiliza para la generacion de ajuste.
	 */
	private String metodoAjuste;
	
	/**
	 * valor total del ajuste.
	 */
	private double valorAjuste;
	
	/**
	 * Observaciones creadas en la generacion de ajuste.
	 */
	private String observaciones;
	
	/**
	 * Estado del ajuste.
	 */
	private int estado;
	
	private boolean ajustePorCuentaCobro;
	/**
	 * variable para almacenar el nombre del consecutivo para los ajustes.
	 */
	private String nombreConsecutivo;
	
	private InfoDatosInt convenio;
	
	
	/**
	 * Variable que indica si un ajuste se encuentra reversado.
	 */
	private boolean ajusteResversado;
	
	/**
	 * En caso de que un ajustes sea para reversar otro, se almana el codigo del ajustes
	 */
	private double codAjusteReversado;
	
	/**
	 * VAriable para manejar la fecha de aprobacion del ajuste.
	 */
	private String fechaAprobacionAjuste;
	    
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
			boolean wasInited = false;
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
	
			if (myFactory != null)
			{
				ajustesDao = myFactory.getAjustesEmpresaDao();
				wasInited = (ajustesDao != null);
			}
			return wasInited;
	}

	public void reset()
	{
		this.colAjustesFacturaEmpresa=new ArrayList();
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.consecutivoAjuste="";
		this.tipoAjuste=ConstantesBD.codigoNuncaValido;
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.castigoCartera=false;
		this.conceptoCastigoCartera="";
		this.fechaAjuste="";
		this.fechaElaboracion="";
		this.horaElaboracion="";
		this.usuario="";
		this.cuentaCobro=ConstantesBD.codigoNuncaValido;
		this.conceptoAjuste="";
		this.metodoAjuste="";
		this.valorAjuste=ConstantesBD.codigoNuncaValido;
		this.observaciones="";
		this.estado=ConstantesBD.codigoNuncaValido;
		this.ajustesFacturaEmpresa=new AjustesFacturaEmpresa();
		this.nombreConsecutivo="";
		this.saldoCuentCobro=0;
		this.convenio=new InfoDatosInt();
		this.tipoAjusteStr=ConstantesBD.codigoNuncaValido+"";
		this.codAjusteReversado=ConstantesBD.codigoNuncaValido;
		this.ajusteResversado=false;
		this.fechaAprobacionAjuste="";
	}
	
	public AjustesEmpresa()
	{
		this.reset();
		this.init (System.getProperty("TIPOBD"));
	}
	/**
	 * @return Returns the castigoCartera.
	 */
	public boolean isCastigoCartera() {
		return castigoCartera;
	}
	/**
	 * @param castigoCartera The castigoCartera to set.
	 */
	public void setCastigoCartera(boolean castigoCartera) {
		this.castigoCartera = castigoCartera;
	}
	/**
	 * @return Returns the codigo.
	 */
	public double getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(double codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return Returns the conceptoAjuste.
	 */
	public String getConceptoAjuste() {
		return conceptoAjuste;
	}
	/**
	 * @param conceptoAjuste The conceptoAjuste to set.
	 */
	public void setConceptoAjuste(String conceptoAjuste) {
		this.conceptoAjuste = conceptoAjuste;
	}
	/**
	 * @return Returns the conceptoCastigoCartera.
	 */
	public String getConceptoCastigoCartera() {
		return conceptoCastigoCartera;
	}
	/**
	 * @param conceptoCastigoCartera The conceptoCastigoCartera to set.
	 */
	public void setConceptoCastigoCartera(String conceptoCastigoCartera) {
		this.conceptoCastigoCartera = conceptoCastigoCartera;
	}
	/**
	 * @return Returns the consecutivoAjuste.
	 */
	public String getConsecutivoAjuste() {
		return consecutivoAjuste;
	}
	/**
	 * @param consecutivoAjuste The consecutivoAjuste to set.
	 */
	public void setConsecutivoAjuste(String consecutivoAjuste) {
		this.consecutivoAjuste = consecutivoAjuste;
	}
	/**
	 * @return Returns the cuentaCobro.
	 */
	public double getCuentaCobro() {
		return cuentaCobro;
	}
	/**
	 * @param cuentaCobro The cuentaCobro to set.
	 */
	public void setCuentaCobro(double cuentaCobro) {
		this.cuentaCobro = cuentaCobro;
	}
	/**
	 * @return Returns the estado.
	 */
	public int getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(int estado) {
		this.estado = estado;
	}
	/**
	 * @return Returns the fechaAjuste.
	 */
	public String getFechaAjuste() {
		return fechaAjuste;
	}
	/**
	 * @param fechaAjuste The fechaAjuste to set.
	 */
	public void setFechaAjuste(String fechaAjuste) {
		this.fechaAjuste = fechaAjuste;
	}
	/**
	 * @return Returns the fechaElaboracion.
	 */
	public String getFechaElaboracion() {
		return fechaElaboracion;
	}
	/**
	 * @param fechaElaboracion The fechaElaboracion to set.
	 */
	public void setFechaElaboracion(String fechaElaboracion) {
		this.fechaElaboracion = fechaElaboracion;
	}
	/**
	 * @return Returns the horaElaboracion.
	 */
	public String getHoraElaboracion() {
		return horaElaboracion;
	}
	/**
	 * @param horaElaboracion The horaElaboracion to set.
	 */
	public void setHoraElaboracion(String horaElaboracion) {
		this.horaElaboracion = horaElaboracion;
	}
	/**
	 * @return Returns the institucion.
	 */
	public int getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion The institucion to set.
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	/**
	 * @return Returns the metodoAjuste.
	 */
	public String getMetodoAjuste() {
		return metodoAjuste;
	}
	/**
	 * @param metodoAjuste The metodoAjuste to set.
	 */
	public void setMetodoAjuste(String metodoAjuste) {
		this.metodoAjuste = metodoAjuste;
	}
	/**
	 * @return Returns the observaciones.
	 */
	public String getObservaciones() {
		return observaciones;
	}
	/**
	 * @param observaciones The observaciones to set.
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	/**
	 * @return Returns the tipoAjuste.
	 */
	public int getTipoAjuste() {
		return tipoAjuste;
	}
	/**
	 * @param tipoAjuste The tipoAjuste to set.
	 */
	public void setTipoAjuste(int tipoAjuste) {
		this.tipoAjuste = tipoAjuste;
	}
	/**
	 * @return Returns the usuario.
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	/**
	 * @return Returns the valorAjuste.
	 */
	public double getValorAjuste() {
		return valorAjuste;
	}
	/**
	 * @param valorAjuste The valorAjuste to set.
	 */
	public void setValorAjuste(double valorAjuste) {
		this.valorAjuste = valorAjuste;
	}
	/**
	 * @return Returns the ajustesFacturaEmpresa.
	 */
	public AjustesFacturaEmpresa getAjustesFacturaEmpresa() {
		return ajustesFacturaEmpresa;
	}
	/**
	 * @param ajustesFacturaEmpresa The ajustesFacturaEmpresa to set.
	 */
	public void setAjustesFacturaEmpresa(
			AjustesFacturaEmpresa ajustesFacturaEmpresa) {
		this.ajustesFacturaEmpresa = ajustesFacturaEmpresa;
	}
	/**
	 * @return Returns the colAjustesFacturaEmpresa.
	 */
	public Collection getColAjustesFacturaEmpresa() {
		return colAjustesFacturaEmpresa;
	}
	/**
	 * @param colAjustesFacturaEmpresa The colAjustesFacturaEmpresa to set.
	 */
	public void setColAjustesFacturaEmpresa(Collection colAjustesFacturaEmpresa) {
		this.colAjustesFacturaEmpresa = colAjustesFacturaEmpresa;
	}
	/**
	 * @return Returns the iteAjustesFacturaEmpres.
	 */
	public Iterator getIteAjustesFacturaEmpres() {
		return colAjustesFacturaEmpresa.iterator();
	}

	/**
	 * @param con
	 * @param codigoFactura
	 * @param castigoCartera
	 * @param ajustesCxCRadicada
	 * @param modificacion
	 * @param codigoAjuste
	 */
	public boolean cargarUnaFactura(Connection con, int codigoFactura, boolean castigoCartera, boolean ajustesCxCRadicada, boolean modificacion, double codigoAjuste) 
	{ 
		return ajustesFacturaEmpresa.cargarUnaFactura(con,codigoFactura,castigoCartera,ajustesCxCRadicada,modificacion,codigoAjuste);
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public boolean insertarAjusteGeneral(Connection con) 
	{
		String tipoAjusteStr="";
		logger.info("tipo ajuste-->"+this.tipoAjuste);
		if((ajustesDao.ingresarAjusteGeneral(con,this.consecutivoAjuste,this.tipoAjuste,this.institucion,this.castigoCartera,this.conceptoCastigoCartera,this.fechaAjuste,this.fechaElaboracion,this.horaElaboracion,this.usuario,this.cuentaCobro,this.conceptoAjuste,this.metodoAjuste,this.valorAjuste,this.observaciones,this.estado,this.ajusteResversado,this.codAjusteReversado)>0))
		{
			if((this.tipoAjuste==ConstantesBD.codigoAjusteCreditoCuentaCobro)||(this.tipoAjuste==ConstantesBD.codigoAjusteCreditoFactura))
				tipoAjusteStr=ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo();
			else
				tipoAjusteStr=ConstantesBD.ajustesDebitoFuncionalidadAjustes.getAcronimo();
			
			this.codigo=Utilidades.obtenercodigoAjusteEmpresa(con,this.consecutivoAjuste,tipoAjusteStr,this.institucion);
			logger.info("EL CODIGO ES -------->"+this.codigo);
			
			return true;
		}
		return false;
		
	}
	/**
	 * @return Returns the nombreConsecutivo.
	 */
	public String getNombreConsecutivo() {
		return nombreConsecutivo;
	}
	/**
	 * @param nombreConsecutivo The nombreConsecutivo to set.
	 */
	public void setNombreConsecutivo(String nombreConsecutivo) {
		this.nombreConsecutivo = nombreConsecutivo;
	}
	/**
	 * @return Returns the saldoCuentCobro.
	 */
	public double getSaldoCuentCobro() {
		return saldoCuentCobro;
	}
	/**
	 * @param saldoCuentCobro The saldoCuentCobro to set.
	 */
	public void setSaldoCuentCobro(double saldoCuentCobro) {
		this.saldoCuentCobro = saldoCuentCobro;
	}

	/**
	 * @param con
	 * @param modificacion
	 * @param cuentaCobro2
	 * @param b
	 * @param boolean1
	 * @return
	 */
	public boolean cargarCuentaCobro(Connection con, double cuentaCobro, boolean castigoCartera, boolean ajustesCxCRadicada, boolean modificacion)
	{
		ResultSetDecorator rs=ajustesDao.cargarCuentaCobro(con,cuentaCobro,this.institucion,castigoCartera,ajustesCxCRadicada,modificacion);
		try
		{
			if(rs.next())
			{
				this.cuentaCobro=cuentaCobro;
				/*if(!modificacion)
					this.valorAjuste=0;*/
				this.saldoCuentCobro=rs.getDouble("saldo");
				this.convenio.setCodigo(rs.getInt("codigoconvenio"));
				this.convenio.setNombre(rs.getString("nombreconvenio"));
				return true;
			}
			else
			{
				logger.error("La consulta no arrojo resultados.");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error(Exception) al obtener los datos del ResultSetDecorator y cargarlos en el Objeto.");
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * @return Returns the convenio.
	 */
	public InfoDatosInt getConvenio() {
		return convenio;
	}
	/**
	 * @param convenio The convenio to set.
	 */
	public void setConvenio(InfoDatosInt convenio) {
		this.convenio = convenio;
	}

	/**
	 * @param con
	 */
	public ResultSetDecorator cargarFacturasCuentaCobro(Connection con) 
	{
		return ajustesDao.cargarFacturasCuentaCobro(con,this.cuentaCobro,this.institucion);
	}

	/**
	 * @param con
	 * @return
	 */
	public ResultSetDecorator cargarFacturasCuentaCobroAjuste(Connection con) 
	{
		return ajustesDao.cargarFacturasCuentaCobroAjuste(con,this.codigo);
	}

	/**
	 * @param con
	 * @return
	 */
	public double existeAjuste(Connection con) 
	{
		String cadena="SELECT codigo as codigo from ajustes_empresa where consecutivo_ajuste="+this.consecutivoAjuste+" and tipo_ajuste="+this.tipoAjuste+" and institucion="+this.institucion;
		ResultSetDecorator rs;
		try {
			rs = UtilidadBD.executeSqlGenerico(con,cadena);
			if(rs.next())
				return rs.getDouble("codigo"); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;	
		}

	/**
	 * @param con
	 */
	public void eliminarAjuste(Connection con,int nivel) 
	{
		ajustesDao.eliminarAjuste(con,this.codigo,nivel);
	}

	/**
	 * @param con
	 */
	public void cargarEncabezadoAjuste(Connection con)
	{
		ResultSetDecorator rs=null;
		try
		{
			rs=ajustesDao.cargarEncabezadoAjuste(con,this.consecutivoAjuste,this.tipoAjusteStr,this.institucion,this.estado);
			if(rs.next())
			{
				this.codigo=rs.getDouble("codigo");
				this.castigoCartera=UtilidadTexto.getBoolean(rs.getString("castigocartera"));
				this.conceptoCastigoCartera=rs.getString("conceptocastigocartera");
				this.fechaAjuste=UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaajuste"));
				if(rs.getObject("cuentacobro")==null)
				{
					this.cuentaCobro=ConstantesBD.codigoNuncaValido;
				}
				else
				{
					this.cuentaCobro=rs.getDouble("cuentacobro");
				}
				this.conceptoAjuste=rs.getString("conceptoajuste");
				this.metodoAjuste=rs.getString("metodoajuste");
				this.valorAjuste=rs.getDouble("valorajuste");
				this.observaciones=rs.getString("observaciones");
				if(this.estado==ConstantesBD.codigoEstadoCarteraAprobado)
				{
					this.fechaAprobacionAjuste=Utilidades.obtenerFechaAprobacionAjustes(con,this.codigo);
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
 

	/**
	 * @return Returns the tipoAjusteStr.
	 */
	public String getTipoAjusteStr() {
		return tipoAjusteStr;
	}
	/**
	 * @param tipoAjusteStr The tipoAjusteStr to set.
	 */
	public void setTipoAjusteStr(String tipoAjusteStr) {
		this.tipoAjusteStr = tipoAjusteStr;
	}
	/**
	 * @return Returns the ajustePorCuentaCobro.
	 */
	public boolean isAjustePorCuentaCobro() {
		return ajustePorCuentaCobro;
	}
	/**
	 * @param ajustePorCuentaCobro The ajustePorCuentaCobro to set.
	 */
	public void setAjustePorCuentaCobro(boolean ajustePorCuentaCobro) {
		this.ajustePorCuentaCobro = ajustePorCuentaCobro;
	}

	/**
	 * @param con
	 */
	public boolean actualizarAjuste(Connection con) 
	{
		return (ajustesDao.actualizarAjuste(con,this.codigo,this.castigoCartera,this.conceptoCastigoCartera,this.fechaAjuste,this.fechaElaboracion,this.horaElaboracion,this.conceptoAjuste,this.metodoAjuste,this.valorAjuste,this.observaciones)>0);	
	}

	/**
	 * Metodo para cambiar el estado de un ajuste,
	 * Debe estar cargado en el mundo el codigo del ajuste.
	 * @param con
	 * @param codigoEstadoCarteraAnulado
	 */
	public boolean cambiarEstadoAjuste(Connection con, int codigoEstadoCarteraAnulado) 
	{
		return (ajustesDao.cambiarEstadoAjuste(con,this.codigo,codigoEstadoCarteraAnulado)>0);
	}

	/**
	 * @param con
	 * @param valorCampoBusqueda
	 * @return
	 */
	public ResultSetDecorator cargarFacturasCuentaCobroAvanzada(Connection con, String valorCampoBusqueda) 
	{
		return ajustesDao.cargarFacturasCuentaCobroAvanzada(con,this.cuentaCobro,this.institucion,valorCampoBusqueda);
	}

	/**
	 * @param con
	 * @param motivoAnulacion
	 * @param loginUsuario
	 * @param fechaActual
	 * @param horaActual
	 */
	public boolean anularAjuste(Connection con, String motivoAnulacion, String loginUsuario, String fechaActual, String horaActual) 
	{
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		enTransaccion=cambiarEstadoAjuste(con,ConstantesBD.codigoEstadoCarteraAnulado);
		enTransaccion=ajustesDao.anularAjuste(con,this.codigo,motivoAnulacion,loginUsuario,fechaActual,horaActual);
		if(enTransaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
			return true;
		}
		UtilidadBD.finalizarTransaccion(con);
		return false;
	}
	/**
	 * @return Returns the ajusteResversado.
	 */
	public boolean isAjusteResversado() {
		return ajusteResversado;
	}
	/**
	 * @param ajusteResversado The ajusteResversado to set.
	 */
	public void setAjusteResversado(boolean ajusteResversado) {
		this.ajusteResversado = ajusteResversado;
	}
	/**
	 * @return Returns the codAjusteReversado.
	 */
	public double getCodAjusteReversado() {
		return codAjusteReversado;
	}
	/**
	 * @param codAjusteReversado The codAjusteReversado to set.
	 */
	public void setCodAjusteReversado(double codAjusteReversado) {
		this.codAjusteReversado = codAjusteReversado;
	}
	/**
	 * @return Returns the fechaAprobacionAjuste.
	 */
	public String getFechaAprobacionAjuste() {
		return fechaAprobacionAjuste;
	}
	/**
	 * @param fechaAprobacionAjuste The fechaAprobacionAjuste to set.
	 */
	public void setFechaAprobacionAjuste(String fechaAprobacionAjuste) {
		this.fechaAprobacionAjuste = fechaAprobacionAjuste;
	}

	/**
	 * Metodo que carga en un mapa el consecutivo de la factura, el valor del ajuste y la descripcion del concepto
	 * y retorna el mapa. (tabla ajus_fact_empresa).
	 * @param con
	 * @param codigoAjuste codigo del ajuste
	 * @return mapa con datos.
	 */
	public HashMap cargarDistribucionAjusteFacturasCuentaCobro(Connection con, double codigoAjuste) 
	{
		ResultSetDecorator rs=null;
		HashMap mapa=new HashMap();
		rs=ajustesDao.cargarFacturasCuentaCobroAjuste(con,codigoAjuste);
		int index=0;
		try {
			while(rs.next())
			{
				mapa.put("codigofactura_"+index,rs.getString("codigofactura"));
				mapa.put("consecutivofactura_"+index,rs.getString("consecutivofactura"));
				mapa.put("codigocentroatencion_"+index,rs.getString("codigocentroatencion"));
				mapa.put("nombrecentroatencion_"+index,rs.getString("nombrecentroatencion"));
				mapa.put("saldofactura_"+index,rs.getString("saldofactura"));
				mapa.put("metodoajuste_"+index,rs.getString("metodoajuste"));
				mapa.put("valorajuste_"+index,rs.getString("valorajuste"));
				mapa.put("conceptoajuste_"+index,rs.getString("conceptoajuste"));
				mapa.put("descconceptoajuste_"+index,rs.getString("descconceptoajuste"));
				mapa.put("facturasistema_"+index,rs.getString("facturasistema"));
				index++;
			}
			mapa.put("numeroregistros",index+"");
		} catch (SQLException e) 
		{
			logger.error("Error consultando los datos"+e.getStackTrace());
		}
		return mapa;
	}

	/**
	 * @param con
	 */
	public boolean insertarAjusteReversion(Connection con) 
	{
		
		String tipoAjusteStr="";
		logger.info("tipo ajuste-->"+this.tipoAjuste);
		if(ajustesDao.ingresarAjusteReversion(con,this.consecutivoAjuste,this.tipoAjuste,this.fechaAjuste,this.fechaElaboracion,this.horaElaboracion,this.usuario,this.observaciones,this.estado,this.ajusteResversado,this.codAjusteReversado))
		{
			if((this.tipoAjuste==ConstantesBD.codigoAjusteCreditoCuentaCobro)||(this.tipoAjuste==ConstantesBD.codigoAjusteCreditoFactura))
				tipoAjusteStr=ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo();
			else
				tipoAjusteStr=ConstantesBD.ajustesDebitoFuncionalidadAjustes.getAcronimo();
			
			this.codigo=Utilidades.obtenercodigoAjusteEmpresa(con,this.consecutivoAjuste,tipoAjusteStr,this.institucion);
			logger.info("EL CODIGO ES -------->"+this.codigo);
			
			return true;
		}
		return false;
	}

	/**
	 * metodo que cambia el valor del campo ajuste_reversado de la tabla ajustes empresa
	 * por el resivido como parametro.
	 * @param con
	 * @param valorCampo
	 * @return
	 */
	public boolean cambiarAtributoReversion(Connection con, boolean valorCampo) 
	{
		return ajustesDao.cambiarAtributoReversion(con,this.codAjusteReversado,valorCampo);
	}

	/**
	 * @param con
	 * @return
	 */
	public HashMap buscarAjustesAprobadosPorFechaParaReversion(Connection con) 
	{
		logger.info("\n entre a buscarAjustesAprobadosPorFechaParaReversion");
		ResultSetDecorator rs=null;
		HashMap mapa=new HashMap();
		rs=ajustesDao.buscarAjustesAprobadosPorFechaParaReversion(con,this.fechaAjuste,this.estado);
		int index=0;
		try {
			while(rs.next())
			{
				mapa.put("codigo_"+index,rs.getString("codigo"));
				mapa.put("tipoajuste_"+index,rs.getString("tipoajuste"));
				if(rs.getInt("tipoajuste")==ConstantesBD.codigoAjusteCreditoCuentaCobro||rs.getInt("tipoajuste")==ConstantesBD.codigoAjusteCreditoFactura)
				{
					mapa.put("tipoajuste_"+index,ConstantesBD.ajustesCreditoFuncionalidadAjustes.getAcronimo());
				}
				else if(rs.getInt("tipoajuste")==ConstantesBD.codigoAjusteDebitoCuentaCobro||rs.getInt("tipoajuste")==ConstantesBD.codigoAjusteDebitoFactura)
				{
					mapa.put("tipoajuste_"+index,ConstantesBD.ajustesDebitoFuncionalidadAjustes.getAcronimo());
				}
				mapa.put("consecutivoajuste_"+index,rs.getString("consecutivoajuste"));
				mapa.put("fechaajuste_"+index,rs.getString("fechaajuste"));
				mapa.put("valorajuste_"+index,rs.getString("valorajuste"));
				mapa.put("cuentacobro_"+index,rs.getString("cuentacobro"));
				mapa.put("factura_"+index,rs.getString("factura"));
				mapa.put("convenio_"+index,rs.getString("convenio"));
				mapa.put("nombrepaciente_"+index,rs.getString("nombrepaciente"));
				mapa.put("ajustecuentacobro_"+index,rs.getString("ajustecuentacobro"));
				index++;
			}
			mapa.put("numeroregistros",index+"");
		} catch (SQLException e) 
		{
			logger.error("Error consultando los datos"+e.getStackTrace());
		}
		return mapa;
	}

	/**
	 * 
	 * @param con
	 * @param codigoAjuste
	 * @return
	 */
	public int obtenerCodigoFacturaAjuste(Connection con, double codigoAjuste) 
	{
		return ajustesDao.obtenerCodigoFacturaAjuste(con,codigoAjuste);
	}
}
