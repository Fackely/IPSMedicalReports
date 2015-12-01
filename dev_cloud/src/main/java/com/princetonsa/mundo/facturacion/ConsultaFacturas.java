/*
 * @(#)ConsultaFacturas.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.mundo.facturacion;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.ConsultaFacturasDao;
import com.princetonsa.dao.DaoFactory;

/**
 * Objeto que maneja la interacción entre la capa
 * de control y el acceso a la información de la 
 * Consulta de Facturas
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 09 /Jun/ 2005
 */
public class ConsultaFacturas
{
	
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ConsultaFacturas.class);
	
	/**
	 * Consecutivo de las Facturas
	 */
	private int consecutivoFactura;
	
	/**
	 * Codigo de la factura
	 */
	private double idFactura;
	
	/**
	 * Numero de la cuenta del paciente cargado
	 */
	private int cuenta;
	
	/**
	 * Valor total de la factura
	 */
	private double valorTotal;
	
	/**
	 * Fecha y hora de elaboracion de la factura
	 */
	private String fechaHoraElaboracion;
	
	/**
	 * String de la via de Ingreso
	 */
	private String viaIngreso;
	
	/**
	 * Descripcion CUPS del Servicio de la Solicitud
	 */
	private String descripcionServicio;
	
	/**
	 * Codigo del paciente cargado en session
	 */
	private int codigoPaciente;
	
	/**
	 * Responsable de la cuenta
	 */
	private String convenio;
	
	/**
	 * Estado de la Factura
	 */
	private String estadoFactura;
	
	/**
	 * Estado del Paciente con respecto a la Factura
	 */
	private String estadoPaciente;
	
	/**
	 * Numero de Autorizacion
	 */
	private String numeroAutorizacion;
	
	/**
	 * Entero para la institucion del usuario cargado en session
	 */
	private int institucion;
    /**
     * Constructor del objeto
     * (Solo inicializa el acceso a la 
     * fuente de datos)
     */
    public ConsultaFacturas()
    {
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
	 * El DAO usado por el objeto <code>consultaFacturasDao</code> 
	 * para acceder a la fuente de datos. 
	 */
	private ConsultaFacturasDao consultaFacturasDao ;

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
			consultaFacturasDao = myFactory.getConsultaFacturasDao();
			wasInited = (consultaFacturasDao != null);
		}

		return wasInited;
	}
	
	
	
	/**
	 * @return Returns the institucion.
	 */
	public int getInstitucion()
	{
		return institucion;
	}
	/**
	 * @param institucion The institucion to set.
	 */
	public void setInstitucion(int institucion)
	{
		this.institucion= institucion;
	}
	/**
	 * @return Returns the codigoPaciente.
	 */
	public int getCodigoPaciente()
	{
		return codigoPaciente;
	}
	/**
	 * @param codigoPaciente The codigoPaciente to set.
	 */
	public void setCodigoPaciente(int codigoPaciente)
	{
		this.codigoPaciente= codigoPaciente;
	}
	/**
	 * @return Returns the consecutivoFactura.
	 */
	public int getConsecutivoFactura()
	{
		return consecutivoFactura;
	}
	/**
	 * @param consecutivoFactura The consecutivoFactura to set.
	 */
	public void setConsecutivoFactura(int consecutivoFactura)
	{
		this.consecutivoFactura= consecutivoFactura;
	}
	/**
	 * @return Returns the convenio.
	 */
	public String getConvenio()
	{
		return convenio;
	}
	/**
	 * @param convenio The convenio to set.
	 */
	public void setConvenio(String convenio)
	{
		this.convenio= convenio;
	}
	/**
	 * @return Returns the cuenta.
	 */
	public int getCuenta()
	{
		return cuenta;
	}
	/**
	 * @param cuenta The cuenta to set.
	 */
	public void setCuenta(int cuenta)
	{
		this.cuenta= cuenta;
	}
	/**
	 * @return Returns the descripcionServicio.
	 */
	public String getDescripcionServicio()
	{
		return descripcionServicio;
	}
	/**
	 * @param descripcionServicio The descripcionServicio to set.
	 */
	public void setDescripcionServicio(String descripcionServicio)
	{
		this.descripcionServicio= descripcionServicio;
	}
	/**
	 * @return Returns the estadoFactura.
	 */
	public String getEstadoFactura()
	{
		return estadoFactura;
	}
	/**
	 * @param estadoFactura The estadoFactura to set.
	 */
	public void setEstadoFactura(String estadoFactura)
	{
		this.estadoFactura= estadoFactura;
	}
	/**
	 * @return Returns the estadoPaciente.
	 */
	public String getEstadoPaciente()
	{
		return estadoPaciente;
	}
	/**
	 * @param estadoPaciente The estadoPaciente to set.
	 */
	public void setEstadoPaciente(String estadoPaciente)
	{
		this.estadoPaciente= estadoPaciente;
	}
	/**
	 * @return Returns the fechaHoraElaboracion.
	 */
	public String getFechaHoraElaboracion()
	{
		return fechaHoraElaboracion;
	}
	/**
	 * @param fechaHoraElaboracion The fechaHoraElaboracion to set.
	 */
	public void setFechaHoraElaboracion(String fechaHoraElaboracion)
	{
		this.fechaHoraElaboracion= fechaHoraElaboracion;
	}
	/**
	 * @return Returns the idFactura.
	 */
	public double getIdFactura()
	{
		return idFactura;
	}
	/**
	 * @param idFactura The idFactura to set.
	 */
	public void setIdFactura(double idFactura)
	{
		this.idFactura= idFactura;
	}
	/**
	 * @return Returns the valorTotal.
	 */
	public double getValorTotal()
	{
		return valorTotal;
	}
	/**
	 * @param valorTotal The valorTotal to set.
	 */
	public void setValorTotal(double valorTotal)
	{
		this.valorTotal= valorTotal;
	}
	/**
	 * @return Returns the viaIngreso.
	 */
	public String getViaIngreso()
	{
		return viaIngreso;
	}
	/**
	 * @param viaIngreso The viaIngreso to set.
	 */
	public void setViaIngreso(String viaIngreso)
	{
		this.viaIngreso= viaIngreso;
	}
	
	/**
	 * Método que carga las facturas dado el Codigo del paciente
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoPaciente Código del paciente
	 * al que se desean revisar sus cuentas
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	public HashMap cargarFacturasPaciente (Connection con, int codigoPaciente) throws SQLException
	{
		consultaFacturasDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaFacturasDao();
		HashMap map= null;
		try
		{
		    String[] colums={"factura","consecutivo","valorTotal","fechaHoraElaboracion","viaIngreso","codigoIngreso","responsable","estadoFactura","estadoPaciente","nombreCentroAtencion","entidadsubcontratada","descentidadsubcontratada","empresainstitucion","descempresainstitucion"};
			map=UtilidadBD.resultSet2HashMap(colums, consultaFacturasDao.cargarFacturasPaciente(con, codigoPaciente), true, true).getMapa();
		}
		catch(Exception e)
		{
			logger.warn("Error mundo Consulta de Facturas Cargando las Facturas Por Paciente" +e.toString());
			map=null;
		}
		return map;
	}
	
	/**
	 * Carga el detalle de la factura
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	public HashMap cargarDetalleFactura (Connection con, double codigoFactura) throws SQLException
	{
		consultaFacturasDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaFacturasDao();
		HashMap map= null;
		try
		{
			//MT6082 se agrega el campo prefijoFactura
		    String[] colums={"consecutivo","fechaHoraElaboracion","codfactura","estadoFactura","responsable","viaIngreso","tipoMonto","prefijoFactura","totalFactura","totalResponsable","totalBrutoPaciente","totalDescuentos","totalAbonos","totalNetoPaciente","valorfavorconvenio","estadoPaciente",  "cuentaCobro","nombreCentroAtencion","subcuenta","cuenta","reciboCaja","montoCobro"};
			//Fin MT
		    //MT 6142 se envia como parametro el codigo de la factura enviado al metodo y se cambia el paremetro true en false para el toLowerCase ya es un dato parametrizado
		    map=UtilidadBD.resultSet2HashMap(colums, consultaFacturasDao.cargarDetalleFactura(con, codigoFactura/*this.idFactura*/), false, true).getMapa();
		    // Fin MT
		}
		catch(Exception e)
		{
			logger.warn("Error mundo Consulta Facturas del Detalle de la Factura" +e.toString());
			map=null;
		}
		return map;
	}
	
	/**
	 * Método para la busuqeda de facturas (TODOS)
	 * @param con
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param fechaElaboracionIncial
	 * @param fechaElaboracionFinal
	 * @param responsable
	 * @param viaIngreso
	 * @param estadoFactura
	 * @param estadoPaciente
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap busquedaFacturasTodos(Connection con,int facturaInicial, int facturaFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, int responsable, int viaIngreso,String tipoPaciente, int estadoFactura, int estadoPaciente, String usuario, int codigoCentroAtencion, double entidadSubcontratada, double empresaInstitucion)
	{
		consultaFacturasDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaFacturasDao();
		try	{
			return consultaFacturasDao.busquedaFacturasTodos(con, facturaInicial, facturaFinal, fechaElaboracionIncial, fechaElaboracionFinal, responsable, viaIngreso,tipoPaciente, estadoFactura, estadoPaciente, usuario, codigoCentroAtencion, entidadSubcontratada, empresaInstitucion);
		}
		catch(Exception e)
		{
			logger.warn("Error mundo Consulta de Facturas en la Busqueda  " +e.toString());
			return null;
		}
		
	}
	
	
	/**
	 * Método que carga las solicitudes de una factura dado su consecutivo
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("rawtypes")
	public HashMap cargarSolicitudesFactura (Connection con, double codigoFactura, String tipoArticulo, int tipoServicio) throws SQLException
	{
		consultaFacturasDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaFacturasDao();
		HashMap map= null;
		try
		{
		    String[] colums={"codigodetallefactura","numero_solicitud","area","codigo","codigopropietario","descripcionServicio","cantidad","valUnitario","valTotal","es_qx","es_material_especial"};
			map=UtilidadBD.resultSet2HashMap(colums, consultaFacturasDao.cargarSolicitudesFactura(con, codigoFactura, tipoArticulo, tipoServicio), true, true).getMapa();
		}
		catch(Exception e)
		{
			logger.warn("Error mundo Consulta de Facturas Cargando las Solicitudes por Facturas" +e.toString());
			map=null;
		}
		return map;
	}



	/**
	 * Metodo para cargar los recibos de caja Asociados a una Factura.
	 * @param con
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Collection cargarListadoRecibosCaja(Connection con, double codigoFactura) 
	{
		consultaFacturasDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaFacturasDao();
		
		logger.info("\n\n El numero de la Factura  ------>" + this.consecutivoFactura + "<-----------------\n\n");
		
		return consultaFacturasDao.cargarListadoRecibosCaja(con, codigoFactura);
	}
	
	/**
	 * Metodo que genera el StringBuffer que organiza los datos del mapa en
	 * el archivo plano para el tipo de reporte "Convenio/Paciente" 
	 * @param mapaFacturasTodos
	 * @param nomRep
	 * @param tipoReporte
	 * @param encabezado
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public StringBuffer cargarMapaConvenioPaciente(HashMap mapa, String nombreReporte, String tipoReporte, String periodo, String encabezado)
	{
		StringBuffer datos = new StringBuffer();
		double totalFacturadoConvenio = 0, totalFacturadoPaciente = 0;
		double totalFacturasAnuladasConvenio = 0, totalFacturasAnuladasPaciente = 0;
		double totalNetoFacturadoConvenio = 0, totalNetoFacturadoPaciente = 0;
		double totalGeneralFacturado = 0, totalGeneralFacturasAnuladas = 0, totalGeneralNetoFacturado;
		
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("TIPO REPORTE: "+tipoReporte+"\n");
		
		if(!periodo.trim().equals("-"))
			datos.append("PERIODO: "+periodo+"\n");
		
		datos.append(encabezado+"\n");
		
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			datos.append(mapa.get("consecutivo_"+i)+","+mapa.get("descentidadsubcontratada_"+i)+","+mapa.get("fechaHoraElaboracion_"+i)+","+mapa.get("usuario_"+i)+","+mapa.get("viaIngreso_"+i)+","+mapa.get("codigoIngreso_"+i)+","+mapa.get("paciente_"+i)+","+mapa.get("tipoIdentificacion_"+i)+","+mapa.get("numIdentificacion_"+i)+","+mapa.get("responsable_"+i)+","+mapa.get("nombreCentroAtencion_"+i)+","+mapa.get("estadoFactura_"+i)+","+mapa.get("estadoPaciente_"+i)+","+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorTotal_"+i)+""))+","+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorConvenio_"+i)+""))+","+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorPaciente_"+i)+""))+"\n");
			if(Utilidades.convertirAEntero(mapa.get("codigoEstadoFactura_"+i)+"") == ConstantesBD.codigoEstadoFacturacionFacturada)
			{
				totalFacturadoConvenio = totalFacturadoConvenio + Utilidades.convertirADouble(mapa.get("valorConvenio_"+i)+"");
				totalFacturadoPaciente = totalFacturadoPaciente + Utilidades.convertirADouble(mapa.get("valorPaciente_"+i)+"");
			}
			else if(Utilidades.convertirAEntero(mapa.get("codigoEstadoFactura_"+i)+"") == ConstantesBD.codigoEstadoFacturacionAnulada)
			{
				totalFacturasAnuladasConvenio = totalFacturasAnuladasConvenio + Utilidades.convertirADouble(mapa.get("valorConvenio_"+i)+"");
				totalFacturasAnuladasPaciente = totalFacturasAnuladasPaciente + Utilidades.convertirADouble(mapa.get("valorPaciente_"+i)+"");
			}
		}
		
		//Se genera el resultado neto de la diferencia entre los resultados de facturado y anulado
		totalNetoFacturadoConvenio = totalFacturadoConvenio - totalFacturasAnuladasConvenio;
		totalNetoFacturadoPaciente = totalFacturadoPaciente - totalFacturasAnuladasPaciente;
		
		//Se suman los totales generales por convenio y paciente en facturado, anulado, y neto
		totalGeneralFacturado = totalFacturadoConvenio + totalFacturadoPaciente;
		totalGeneralFacturasAnuladas = totalFacturasAnuladasConvenio + totalFacturasAnuladasPaciente;
		totalGeneralNetoFacturado = totalNetoFacturadoConvenio + totalNetoFacturadoPaciente;
		
		datos.append("Totales, Total Facturado, Total Facturas Anuladas, Neto Facturado\n");
		datos.append("Total Convenio, "+UtilidadTexto.formatearExponenciales(totalFacturadoConvenio)+","+UtilidadTexto.formatearExponenciales(totalFacturasAnuladasConvenio)+","+UtilidadTexto.formatearExponenciales(totalNetoFacturadoConvenio)+"\n");
		datos.append("Total Paciente, "+UtilidadTexto.formatearExponenciales(totalFacturadoPaciente)+","+UtilidadTexto.formatearExponenciales(totalFacturasAnuladasPaciente)+","+UtilidadTexto.formatearExponenciales(totalNetoFacturadoPaciente)+"\n");
		datos.append("Total General, "+UtilidadTexto.formatearExponenciales(totalGeneralFacturado)+","+UtilidadTexto.formatearExponenciales(totalGeneralFacturasAnuladas)+","+UtilidadTexto.formatearExponenciales(totalGeneralNetoFacturado)+"\n");
		
		return datos;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param fechaElaboracionIncial
	 * @param fechaElaboracionFinal
	 * @param responsable
	 * @param viaIngreso
	 * @param estadoFactura
	 * @param estadoPaciente
	 * @param usuario
	 * @param codigoCentroAtencion
	 * @param entidadSubcontratada
	 * @param empresaInstitucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarListadoConvenio(Connection con, int facturaInicial, int facturaFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, int responsable, int viaIngreso, String tipoPaciente, int estadoFactura, int estadoPaciente, String usuario, int codigoCentroAtencion, double entidadSubcontratada, double empresaInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaFacturasDao().cargarListadoConvenio(con, facturaInicial, facturaFinal, fechaElaboracionIncial, fechaElaboracionFinal, responsable, viaIngreso, tipoPaciente, estadoFactura, estadoPaciente, usuario, codigoCentroAtencion, entidadSubcontratada, empresaInstitucion);
	}

	/**
	 * Metodo que genera el StringBuffer que organiza los datos del mapa en
	 * el archivo plano para el tipo de reporte "Convenio"
	 * @param mapaFacturasTodos
	 * @param nomRep
	 * @param tipoReporte
	 * @param encabezado
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public StringBuffer cargarMapaConvenio(HashMap mapa, String nombreReporte, String tipoReporte, String periodo, String encabezado)
	{
		StringBuffer datos = new StringBuffer();
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("TIPO REPORTE: "+tipoReporte+"\n");
		
		if(!periodo.trim().equals("-"))
			datos.append("PERIODO: "+periodo+"\n");
		
		datos.append(encabezado+"\n");
		
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			datos.append(mapa.get("nombretipoconvenio_"+i)+","+mapa.get("nombreconvenio_"+i)+","+mapa.get("consecutivofactura_"+i)+","+mapa.get("fechafactura_"+i)+","+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorfactura_"+i)+""))+","+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("ajustesdebito_"+i)+""))+","+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("ajustescredito_"+i)+""))+","+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("totalfacturado_"+i)+""))+"\n");
		}
		
		return datos;
	}
	
	/**
	 * Metodo que genera el StringBuffer que organiza los datos del mapa en
	 * el archivo plano para el tipo de reporte "Convenio"
	 * @param mapaFacturasTodos
	 * @param nomRep
	 * @param tipoReporte
	 * @param encabezado
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public StringBuffer cargarMapaConvenioFacturado(HashMap mapa, String nombreReporte, String tipoReporte, String periodo, String encabezado)
	{
		StringBuffer datos = new StringBuffer();
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("TIPO REPORTE: "+tipoReporte+"\n");
		
		if(!periodo.trim().equals("-"))
			datos.append("PERIODO: "+periodo+"\n");
		
		datos.append(encabezado+"\n");
		
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			datos.append(mapa.get("nombretipoconvenio_"+i)+","+mapa.get("nombreconvenio_"+i)+","+mapa.get("consecutivofactura_"+i)+","+mapa.get("fechafactura_"+i)+","+mapa.get("fecharadicacion_"+i)+","+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorradicado_"+i)+""))+","+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("ajustesdebito_"+i)+""))+","+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("ajustescredito_"+i)+""))+","+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("totalradicado_"+i)+""))+"\n");
		}
		
		return datos;
	}
	
	/**
	 * 
	 * @param con
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param fechaElaboracionIncial
	 * @param fechaElaboracionFinal
	 * @param responsable
	 * @param viaIngreso
	 * @param estadoFactura
	 * @param estadoPaciente
	 * @param usuario
	 * @param codigoCentroAtencion
	 * @param entidadSubcontratada
	 * @param empresaInstitucion
	 * @return
	 */
	public static String armarWhereFiltroFacturas(int facturaInicial, int facturaFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, int responsable, int viaIngreso,String tipoPaciente,int estadoFactura, int estadoPaciente, String usuario, int codigoCentroAtencion, double entidadSubcontratada, double empresaInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaFacturasDao().armarWhereFiltroFacturas(facturaInicial, facturaFinal, fechaElaboracionIncial, fechaElaboracionFinal, responsable, viaIngreso,tipoPaciente, estadoFactura, estadoPaciente, usuario, codigoCentroAtencion, entidadSubcontratada, empresaInstitucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param fechaElaboracionIncial
	 * @param fechaElaboracionFinal
	 * @param responsable
	 * @param viaIngreso
	 * @param estadoFactura
	 * @param estadoPaciente
	 * @param usuario
	 * @param codigoCentroAtencion
	 * @param entidadSubcontratada
	 * @param empresaInstitucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarListadoFacturadoRadicado(Connection con, int facturaInicial, int facturaFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, int responsable, int viaIngreso,String tipoPaciente,int estadoFactura, int estadoPaciente, String usuario, int codigoCentroAtencion, double entidadSubcontratada, double empresaInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaFacturasDao().cargarListadoFacturadoRadicado(con, facturaInicial, facturaFinal, fechaElaboracionIncial, fechaElaboracionFinal, responsable, viaIngreso,tipoPaciente, estadoFactura, estadoPaciente, usuario, codigoCentroAtencion, entidadSubcontratada, empresaInstitucion);
	}
	
	/**
	 * Metodo de consulta los datos de la factura para reporte por solicitud
	 * @param con
	 * @param codFactura
	 * @return
	 */
	public static HashMap<Object, Object> consultaDatosFactura(Connection con, String codFactura)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaFacturasDao().consultaDatosFactura(con, codFactura);
	}
	
	/**
	 * 
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param fechaElaboracionIncial
	 * @param fechaElaboracionFinal
	 * @param responsable
	 * @param viaIngreso
	 * @param tipoPaciente
	 * @param estadoFactura
	 * @param estadoPaciente
	 * @param usuario
	 * @param codigoCentroAtencion
	 * @param entidadSubcontratada
	 * @param empresaInstitucion
	 * @return
	 */
	public static double cantidadRegistrosBusqueda(int facturaInicial, int facturaFinal, String fechaElaboracionIncial, String fechaElaboracionFinal, int responsable, int viaIngreso,String tipoPaciente, int estadoFactura, int estadoPaciente, String usuario, int codigoCentroAtencion, double entidadSubcontratada, double empresaInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaFacturasDao().cantidadRegistrosBusqueda(facturaInicial, facturaFinal, fechaElaboracionIncial, fechaElaboracionFinal, responsable, viaIngreso, tipoPaciente, estadoFactura, estadoPaciente, usuario, codigoCentroAtencion, entidadSubcontratada, empresaInstitucion);
	}
	
	
	
	public static String consultarNumeroAutorizacion(int codigoCuenta, int codSubCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaFacturasDao().consultarNumeroAutorizacion(codigoCuenta,codSubCuenta);
	}



	/**
	 * @return the numeroAutorizacion
	 */
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}



	/**
	 * @param numeroAutorizacion the numeroAutorizacion to set
	 */
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}
	
}
