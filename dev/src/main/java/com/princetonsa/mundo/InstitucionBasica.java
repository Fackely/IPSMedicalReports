package com.princetonsa.mundo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ParamInstitucionDao;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.princetonsa.mundo.parametrizacion.CentroAtencion;

/**
 * Clase que contiene la informacion de la institucion u multiempresa 
 * @author wilson
 *
 */
public class InstitucionBasica  implements Observer, Serializable, HttpSessionBindingListener 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2836951565344799212L;

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(InstitucionBasica.class);
	
	/**
	 * El DAO usado por el objeto para acceder a la fuente de datos.
	 */
	private static ParamInstitucionDao institucionDao = null;
	
	/**
	 * 
	 */
	private String codigo;
	
	/**
	 * Código de la institución general
	 */
	private int codigoInstitucionBasica;
	
	/**
	 * 
	 */
	private String razonSocial;
	
	/**
	 * Nombre de la institución básica del sistema, es la misma razón social si el 
	 * parámetro general esMultiempresa esta en false
	 */
	private String razonSocialInstitucionBasica;
	
	/**
	 * 
	 */
	private String pais;
	private String codigoPais;
	
	/**
	 * 
	 */
	private String depto;
	private String codigoDepto;
	
	/**
	 * 
	 */
	private String ciudad;
	private String codigoCiudad;
	
	/**
	 * 
	 */
	private String direccion;
	
	/**
	 * 
	 */
	private String telefono;
	
	/**
	 * 
	 */
	private String codMinsalud;
	
	/**
	 * 
	 */
	private String actividadEconomica;
	
	/**
	 * 
	 */
	private String resolucion;
	
	/**
	 * 
	 */
	private String prefijofactura;
	
	/**
	 * 
	 */
	private String encabezadoFactura; 
	
	/**
	 * 
	 */
	private String pieFactura;
	
	/**
	 * 
	 */
	private String pieHistoriaClinica;
	
	/**
	 * 
	 */
	private String pieAmbMedicamentos;
	
	/**
	 * 
	 */
	private String logoJsp;
	
	/**
	 * 
	 */
	private String logoReportes;
	
	/**
	 * 
	 */
	private String nit;
	
	/**
	 * 
	 */
	private String tipoIdentificacion;
	
	/**
	 * 
	 */
	private String descripcionTipoIdentificacion;
	
	/**
	 * Observable al cual está registrado este Observer.
	 */
	private Observable observable;
	
	/**
	 * 
	 */
	private double rangoInicialFactura;
	
	/**
	 * 
	 */
	private double rangoFinalFactura;

	/**
	 * Digito de Verificación
	 */
	private String digitoVerificacion;
	
	/**
	 * 
	 */
	private boolean esEmpresaInstitucion;
	
	
	/**
	 * 
	 */
	private String ubicacionLogo;
	
	/**
	 * 
	 */
	private String indicativo;
	
	/**
	 * 
	 */
	private String extension;
	
	/**
	 * 
	 */
	private String codigoInterfaz;
	
	/**
	 * Almacena el logo de la institución según el parámetro si
	 * maneja logo por institución general o por institución empresa
	 */
	private String logoInstitucion;
	
	/**
	 * Indica si el logo se maneja a nivel de institución o de empresa isntitución
	 */
	private String nivelLogoPorEmpresaInstitucion;
	
	
	/**
	 * N&uacute;mero de resoluci&oacute;n de la Factura Varia
	 */
	private String resolucionFacturaVaria;

	/**
	 * Prefijo de las facturas Varias
	 */
	private String prefijoFacturaVaria;

	/**
	 * N&uacute;mero inicial de la Factura Varia autorizado por la Dian
	 */
	private BigDecimal rangoInicialFacturaVaria;

	/**
	 * N&uacute;mero final de la Factura Varia autorizado por la Dian
	 */
	private BigDecimal rangoFinalFacturaVaria;

	/**
	 * Encabezado factura Varia
	 */
	private String encabezadoFacturaVaria;

	/**
	 * Pie de página factura Varia
	 */
	private String pieFacturaVaria;
	
	
	
	/**
	 * 
	 *
	 */
	private void clean()
	{
		this.actividadEconomica="";
		this.ciudad="";
		this.codigo="";
		this.codMinsalud="";
		this.depto="";
		this.direccion="";
		this.encabezadoFactura="";
		this.logoJsp="";
		this.nit="";
		this.digitoVerificacion="";
		this.pais="";
		this.pieFactura="";
		this.prefijofactura="";
		this.razonSocial="";
		this.resolucion="";
		this.telefono="";
		this.tipoIdentificacion="";
		this.descripcionTipoIdentificacion = "";
		this.rangoInicialFactura=ConstantesBD.codigoNuncaValido;
		this.rangoFinalFactura=ConstantesBD.codigoNuncaValido;
		this.esEmpresaInstitucion=false;
		this.ubicacionLogo = "";
		this.indicativo = "";
		this.extension = "";
		this.codigoPais = "";
		this.codigoDepto = "";
		this.codigoCiudad = "";
		this.codigoInterfaz = "";
		this.logoInstitucion = "";
		
		this.setResolucionFacturaVaria("");
		this.setPrefijoFacturaVaria("");
		this.setRangoInicialFacturaVaria(null);
		this.setRangoFinalFacturaVaria(null);
		
		
		
		this.setEncabezadoFacturaVaria("");
		this.setPieFacturaVaria("");
		this.pieAmbMedicamentos = "";
		
	}
	
	

	/**
	 * Constructor vacio, necesario para poder usar esta clase como un JavaBean
	 */
	public InstitucionBasica() 
	{
		clean();
		init(System.getProperty("TIPOBD"));
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (institucionDao == null) {
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			institucionDao = myFactory.getParamInstitucionDao();
		}
	}
	
	/**
	 * 
	 * @param codigoInstitucion
	 * @param consecutivoCentroAtencion
	 */
	public void cargar(int codigoInstitucion, int consecutivoCentroAtencion)
	{
		double empresaInstitucion= CentroAtencion.obtenerEmpresaInstitucionCentroAtencion(consecutivoCentroAtencion);
		cargarGeneral(codigoInstitucion, empresaInstitucion);
	}	
	
	/**
	 * 
	 * @param codigoInstitucion
	 * @param empresaInstitucion
	 */
	@SuppressWarnings("unchecked")
	public void cargarGeneral(int codigoInstitucion, double empresaInstitucion)
	{
		Connection con= UtilidadBD.abrirConexion();
		//primero evaluamos si no se maneja multiempresa
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion)) && 
				empresaInstitucion>0)
		{
			HashMap criterios= new HashMap();
			criterios.put("codigo1_", empresaInstitucion+"");
			HashMap empresaInstitucionMap=ParametrizacionInstitucion.consultarEmpresas(con, criterios);
			
			this.codigo= empresaInstitucionMap.get("codigo1_0")+"";
			this.actividadEconomica=empresaInstitucionMap.get("actividad10_0")+"";
			this.ciudad=empresaInstitucionMap.get("nombreCiudad24_0")+"";
			this.codMinsalud=empresaInstitucionMap.get("minSalud9_0")+"";
			this.depto=empresaInstitucionMap.get("nombreDepto23_0")+"";
			this.direccion=empresaInstitucionMap.get("direccion7_0")+"";
			this.encabezadoFactura=empresaInstitucionMap.get("encabezado19_0")+"";
			
			this.logoJsp=System.getProperty("ADJUNTOS")+System.getProperty("file.separator")+"logos"+System.getProperty("file.separator")+empresaInstitucionMap.get("logo17_0")+"";
			this.logoReportes=ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+"logos"+System.getProperty("file.separator")+empresaInstitucionMap.get("logo17_0")+"";
			
			this.nit=empresaInstitucionMap.get("nit2_0")+"";
			this.digitoVerificacion=empresaInstitucionMap.get("digv32_0")+"";
			this.pais=empresaInstitucionMap.get("nombrePais25_0")+"";
			this.codigoPais=empresaInstitucionMap.get("pais21_0")+"";
			this.pieFactura=empresaInstitucionMap.get("pie20_0")+"";
			this.pieHistoriaClinica = empresaInstitucionMap.get("pieHisCli36_0")+"";
			this.prefijofactura=empresaInstitucionMap.get("prefijo12_0")+"";
			this.razonSocial=empresaInstitucionMap.get("razonSocial4_0")+"";
			this.resolucion=empresaInstitucionMap.get("resolucion11_0")+"";
			this.telefono=empresaInstitucionMap.get("telefono8_0")+"";
			this.tipoIdentificacion=empresaInstitucionMap.get("tipoNit15_0")+"";
			this.descripcionTipoIdentificacion = empresaInstitucionMap.get("nombreTipoNit16_0")+"";
			//this.tipoIdentificacion=empresaInstitucionMap.get("desTipoIdent22_0")+"";
			this.rangoInicialFactura=Utilidades.convertirADouble(empresaInstitucionMap.get("rangoInicial13_0")+"");
			this.rangoFinalFactura=Utilidades.convertirADouble(empresaInstitucionMap.get("rangoFinal14_0")+"");
			this.codigoInterfaz= empresaInstitucionMap.get("codinterfaz37_0").toString();
			this.esEmpresaInstitucion=true;
			
			this.ubicacionLogo = empresaInstitucionMap.get("ubicacionLogoReportes35_0")+"";

			ParametrizacionInstitucion institucion= new ParametrizacionInstitucion();
			institucion.cargar(con, codigoInstitucion);
			this.logoInstitucion=institucion.getLogoJsp();
			this.razonSocialInstitucionBasica=empresaInstitucionMap.get("razonSocialInstitucionBasica_0")+"";
			this.codigoInstitucionBasica=codigoInstitucion;
			this.nivelLogoPorEmpresaInstitucion=empresaInstitucionMap.get("nivelLogo_0")+"";
			
			this.setResolucionFacturaVaria(empresaInstitucionMap.get("resolucionFacturaVaria_0")+"");
			this.setPrefijoFacturaVaria(empresaInstitucionMap.get("prefFacturaVaria_0")+"");
			
			if (empresaInstitucionMap.get("rgoInicFactVaria_0") !=null 
					&& !UtilidadTexto.isEmpty(empresaInstitucionMap.get("rgoInicFactVaria_0")+"")
					&& UtilidadTexto.isNumber(empresaInstitucionMap.get("rgoInicFactVaria_0")+"")){
				
				this.setRangoInicialFacturaVaria((BigDecimal) empresaInstitucionMap.get("rgoInicFactVaria_0"));
			}
			
			if(empresaInstitucionMap.get("rgoFinFactVaria_0")!=null 
					&& !UtilidadTexto.isEmpty(empresaInstitucionMap.get("rgoFinFactVaria_0")+"")
				&& UtilidadTexto.isNumber(empresaInstitucionMap.get("rgoInicFactVaria_0")+"")){
				
				this.setRangoFinalFacturaVaria((BigDecimal) empresaInstitucionMap.get("rgoFinFactVaria_0"));
			}
			
			this.setEncabezadoFacturaVaria(empresaInstitucionMap.get("encabezadoFacturaVaria_0")+"");
			this.setPieFacturaVaria(empresaInstitucionMap.get("pieFacturaVaria_0")+"");
			
			this.setPieAmbMedicamentos(empresaInstitucionMap.get("pieAmbMed_0")+"");
		}
		else
		{
			ParametrizacionInstitucion institucion= new ParametrizacionInstitucion();
			institucion.cargar(con, codigoInstitucion);
			
			this.codigo= institucion.getCodigo()+"";
			this.codigoInstitucionBasica=institucion.getCodigo();
			this.actividadEconomica= institucion.getActividadEconomica();
			this.ciudad= institucion.getCiudad().getNombre();
			this.codigoCiudad = institucion.getCiudad().getCodigo();
			this.codMinsalud=institucion.getCodMinSalud();
			this.depto=institucion.getDepartamento().getNombre();
			this.codigoDepto=institucion.getDepartamento().getCodigo();
			this.direccion=institucion.getDireccion();
			this.encabezadoFactura=institucion.getEncabezado();
			
			this.logoJsp=institucion.getLogoJsp();
			this.logoReportes=institucion.getLogoReportes();
			
			if(UtilidadTexto.isEmpty(institucion.getLogoJsp().toString())) {
				this.logoJsp=System.getProperty("directorioImagenes")+"logo_clinica_reporte.gif";
			}
			if(UtilidadTexto.isEmpty(institucion.getLogoReportes().toString())) {
				this.logoReportes=ValoresPorDefecto.getDirectorioImagenes()+"logo_clinica_reporte.gif";
			}
			
			this.nit=institucion.getNit();
			this.digitoVerificacion=institucion.getDigitoVerificacion();
			this.pais=institucion.getPais().getNombre();
			this.codigoPais=institucion.getPais().getCodigo();
			this.pieFactura=institucion.getPie();
			this.pieHistoriaClinica = institucion.getPieHisCli();
			this.prefijofactura=institucion.getPrefijoFacturas();
			this.razonSocial=institucion.getRazonSocial();
			this.resolucion=institucion.getResolucion();
			this.telefono=institucion.getTelefono();
			this.tipoIdentificacion=institucion.getIdentificacion();
			this.descripcionTipoIdentificacion = institucion.getDescripcionTipoIdentificacion();
			this.rangoInicialFactura=Utilidades.convertirADouble(institucion.getRangoInicialFactura());
			this.rangoFinalFactura=Utilidades.convertirADouble(institucion.getRangoFinalFactura());
			this.esEmpresaInstitucion=false;
			this.ubicacionLogo = institucion.getUbicacionLogo();
			this.codigoInterfaz= institucion.getCodigoInterfaz();
			
			this.indicativo = institucion.getIndicativo();
			this.extension = institucion.getExtension();
			this.logoInstitucion=institucion.getLogoJsp();
			this.razonSocialInstitucionBasica=this.razonSocial;
			
			
			this.setResolucionFacturaVaria(institucion.getResolucionFacturaVaria());
			this.setPrefijoFacturaVaria(institucion.getPieFacturaVaria());
			this.setRangoInicialFacturaVaria(institucion.getRangoInicialFacturaVaria());
			this.setRangoFinalFacturaVaria(institucion.getRangoFinalFacturaVaria());
			this.setEncabezadoFacturaVaria(institucion.getEncabezadoFacturaVaria());
			this.setPieFacturaVaria(institucion.getPieFacturaVaria());
			
			this.setPieAmbMedicamentos(institucion.getPieAmbMed());
		}
		log();
		UtilidadBD.closeConnection(con);
	}
	
	/**
	 * 
	 * @param codigoInstitucion
	 * @param empresaInstitucion
	 */
	@SuppressWarnings("unchecked")
	public void cargarGeneral(Connection con, int codigoInstitucion, double empresaInstitucion)
	{
		//primero evaluamos si no se maneja multiempresa
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion)) && empresaInstitucion>0)
		{
			HashMap criterios= new HashMap();
			criterios.put("codigo1_", empresaInstitucion+"");
			HashMap empresaInstitucionMap=ParametrizacionInstitucion.consultarEmpresas(con, criterios);
			
			this.codigo= empresaInstitucionMap.get("codigo1_0")+"";
			this.actividadEconomica=empresaInstitucionMap.get("actividad10_0")+"";
			this.ciudad=empresaInstitucionMap.get("nombreCiudad24_0")+"";
			this.codMinsalud=empresaInstitucionMap.get("minSalud9_0")+"";
			this.depto=empresaInstitucionMap.get("nombreDepto23_0")+"";
			this.direccion=empresaInstitucionMap.get("direccion7_0")+"";
			this.encabezadoFactura=empresaInstitucionMap.get("encabezado19_0")+"";
			
			this.logoJsp=System.getProperty("ADJUNTOS")+System.getProperty("file.separator")+"logos"+System.getProperty("file.separator")+empresaInstitucionMap.get("logo17_0")+"";
			this.logoReportes=ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+"logos"+System.getProperty("file.separator")+empresaInstitucionMap.get("logo17_0")+"";
			
			this.nit=empresaInstitucionMap.get("nit2_0")+"";
			this.digitoVerificacion=empresaInstitucionMap.get("digv32_0")+"";
			this.pais=empresaInstitucionMap.get("nombrePais25_0")+"";
			this.codigoPais=empresaInstitucionMap.get("pais21_0")+"";
			this.pieFactura=empresaInstitucionMap.get("pie20_0")+"";
			this.pieHistoriaClinica = empresaInstitucionMap.get("pieHisCli36_0")+"";
			this.prefijofactura=empresaInstitucionMap.get("prefijo12_0")+"";
			this.razonSocial=empresaInstitucionMap.get("razonSocial4_0")+"";
			this.resolucion=empresaInstitucionMap.get("resolucion11_0")+"";
			this.telefono=empresaInstitucionMap.get("telefono8_0")+"";
			this.tipoIdentificacion=empresaInstitucionMap.get("tipoNit15_0")+"";
			this.descripcionTipoIdentificacion = empresaInstitucionMap.get("nombreTipoNit16_0")+"";
			//this.tipoIdentificacion=empresaInstitucionMap.get("desTipoIdent22_0")+"";
			this.rangoInicialFactura=Utilidades.convertirADouble(empresaInstitucionMap.get("rangoInicial13_0")+"");
			this.rangoFinalFactura=Utilidades.convertirADouble(empresaInstitucionMap.get("rangoFinal14_0")+"");
			this.codigoInterfaz= empresaInstitucionMap.get("codinterfaz37_0").toString();
			this.esEmpresaInstitucion=true;
			
			this.ubicacionLogo = empresaInstitucionMap.get("ubicacionLogoReportes35_0")+"";

			ParametrizacionInstitucion institucion= new ParametrizacionInstitucion();
			institucion.cargar(con, codigoInstitucion);
			this.logoInstitucion=institucion.getLogoJsp();
			this.razonSocialInstitucionBasica=empresaInstitucionMap.get("razonSocialInstitucionBasica_0")+"";
			this.codigoInstitucionBasica=codigoInstitucion;
			this.nivelLogoPorEmpresaInstitucion=empresaInstitucionMap.get("nivelLogo_0")+"";
			
			this.setResolucionFacturaVaria(empresaInstitucionMap.get("resolucionFacturaVaria_0")+"");
			this.setPrefijoFacturaVaria(empresaInstitucionMap.get("prefFacturaVaria_0")+"");
			
			if (empresaInstitucionMap.get("rgoInicFactVaria_0") !=null 
					&& !UtilidadTexto.isEmpty(empresaInstitucionMap.get("rgoInicFactVaria_0")+"")
					&& UtilidadTexto.isNumber(empresaInstitucionMap.get("rgoInicFactVaria_0")+"")){
				
				this.setRangoInicialFacturaVaria((BigDecimal) empresaInstitucionMap.get("rgoInicFactVaria_0"));
			}
			
			if(empresaInstitucionMap.get("rgoFinFactVaria_0")!=null 
					&& !UtilidadTexto.isEmpty(empresaInstitucionMap.get("rgoFinFactVaria_0")+"")
				&& UtilidadTexto.isNumber(empresaInstitucionMap.get("rgoInicFactVaria_0")+"")){
				
				this.setRangoFinalFacturaVaria((BigDecimal) empresaInstitucionMap.get("rgoFinFactVaria_0"));
			}
			
			this.setEncabezadoFacturaVaria(empresaInstitucionMap.get("encabezadoFacturaVaria_0")+"");
			this.setPieFacturaVaria(empresaInstitucionMap.get("pieFacturaVaria_0")+"");
			
			this.setPieAmbMedicamentos(empresaInstitucionMap.get("pieAmbMed_0")+"");
		}
		else
		{
			ParametrizacionInstitucion institucion= new ParametrizacionInstitucion();
			institucion.cargar(con, codigoInstitucion);
			
			this.codigo= institucion.getCodigo()+"";
			this.codigoInstitucionBasica=institucion.getCodigo();
			this.actividadEconomica= institucion.getActividadEconomica();
			this.ciudad= institucion.getCiudad().getNombre();
			this.codigoCiudad = institucion.getCiudad().getCodigo();
			this.codMinsalud=institucion.getCodMinSalud();
			this.depto=institucion.getDepartamento().getNombre();
			this.codigoDepto=institucion.getDepartamento().getCodigo();
			this.direccion=institucion.getDireccion();
			this.encabezadoFactura=institucion.getEncabezado();
			
			this.logoJsp=institucion.getLogoJsp();
			this.logoReportes=institucion.getLogoReportes();
			
			if(UtilidadTexto.isEmpty(institucion.getLogoJsp().toString())) {
				this.logoJsp=System.getProperty("directorioImagenes")+"logo_clinica_reporte.gif";
			}
			if(UtilidadTexto.isEmpty(institucion.getLogoReportes().toString())) {
				this.logoReportes=ValoresPorDefecto.getDirectorioImagenes()+"logo_clinica_reporte.gif";
			}
			
			this.nit=institucion.getNit();
			this.digitoVerificacion=institucion.getDigitoVerificacion();
			this.pais=institucion.getPais().getNombre();
			this.codigoPais=institucion.getPais().getCodigo();
			this.pieFactura=institucion.getPie();
			this.pieHistoriaClinica = institucion.getPieHisCli();
			this.prefijofactura=institucion.getPrefijoFacturas();
			this.razonSocial=institucion.getRazonSocial();
			this.resolucion=institucion.getResolucion();
			this.telefono=institucion.getTelefono();
			this.tipoIdentificacion=institucion.getIdentificacion();
			this.descripcionTipoIdentificacion = institucion.getDescripcionTipoIdentificacion();
			this.rangoInicialFactura=Utilidades.convertirADouble(institucion.getRangoInicialFactura());
			this.rangoFinalFactura=Utilidades.convertirADouble(institucion.getRangoFinalFactura());
			this.esEmpresaInstitucion=false;
			this.ubicacionLogo = institucion.getUbicacionLogo();
			this.codigoInterfaz= institucion.getCodigoInterfaz();
			
			this.indicativo = institucion.getIndicativo();
			this.extension = institucion.getExtension();
			this.logoInstitucion=institucion.getLogoJsp();
			this.razonSocialInstitucionBasica=this.razonSocial;
			
			
			this.setResolucionFacturaVaria(institucion.getResolucionFacturaVaria());
			this.setPrefijoFacturaVaria(institucion.getPieFacturaVaria());
			this.setRangoInicialFacturaVaria(institucion.getRangoInicialFacturaVaria());
			this.setRangoFinalFacturaVaria(institucion.getRangoFinalFacturaVaria());
			this.setEncabezadoFacturaVaria(institucion.getEncabezadoFacturaVaria());
			this.setPieFacturaVaria(institucion.getPieFacturaVaria());
			
			this.setPieAmbMedicamentos(institucion.getPieAmbMed());
		}
		log();
	}
	
	/**
	 * 
	 * @param codigoInstitucion
	 * @param codigoConvenio
	 */
	public void cargarXConvenio(int codigoInstitucion, int codigoConvenio)
	{
		double empresaInstitucion= Convenio.obtenerEmpresaInstitucionConvenio(codigoConvenio);
		cargarGeneral(codigoInstitucion, empresaInstitucion);
	}
	
	/**
	 * 
	 * @param codigoInstitucion
	 * @param codigoConvenio
	 */
	public void cargarXConvenio(Connection con, int codigoInstitucion, int codigoConvenio)
	{
		double empresaInstitucion= Convenio.obtenerEmpresaInstitucionConvenio(con, codigoConvenio);
		cargarGeneral(con, codigoInstitucion, empresaInstitucion);
	}
	
	
	/**
	 * 
	 *
	 */
	public void log ()
	{
		logger.info("\n\n------------------> codigo ="+this.codigo+" act eco="+this.actividadEconomica+" ciudad->"+this.ciudad+" minsalud->"+this.codMinsalud+" depto->"+this.getDepto()+" dir->"+this.direccion+" encFactura->"+this.encabezadoFactura+" logoJsp->"+this.logoJsp+" logoReportes->"+this.logoReportes+" nit->"+this.nit+" pais->"+this.pais+" pieFactura->"+this.pieFactura+" razonsocial->"+this.razonSocial+" resolucion->"+this.resolucion+" tel->"+this.telefono+" tipoId->"+this.tipoIdentificacion+" logo-- "+this.ubicacionLogo+" \n\n");
	}
	
	public void update(Observable arg0, Object arg1) 
	{
		//@todo implementarlo
	}

	public void valueBound(HttpSessionBindingEvent arg0) {
		// @TODO Auto-generated method stub
		
	}

	public void valueUnbound(HttpSessionBindingEvent arg0) 
	{
		//Cuando me remueven de un session, bien sea explícitamente (session.removeAttribute()),
		// por un logout (session.invalidate()), o por timeout, me des-registro como Observer

		if (observable != null) {
			observable.deleteObserver(this);
		}
	}

	/**
	 * @return the actividadEconomica
	 */
	public String getActividadEconomica() {
		return actividadEconomica;
	}

	/**
	 * @param actividadEconomica the actividadEconomica to set
	 */
	public void setActividadEconomica(String actividadEconomica) {
		this.actividadEconomica = actividadEconomica;
	}

	/**
	 * @return the ciudad
	 */
	public String getCiudad() {
		return ciudad;
	}

	/**
	 * @param ciudad the ciudad to set
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the codMinsalud
	 */
	public String getCodMinsalud() {
		return codMinsalud;
	}

	/**
	 * @param codMinsalud the codMinsalud to set
	 */
	public void setCodMinsalud(String codMinsalud) {
		this.codMinsalud = codMinsalud;
	}

	/**
	 * @return the depto
	 */
	public String getDepto() {
		return depto;
	}

	/**
	 * @param depto the depto to set
	 */
	public void setDepto(String depto) {
		this.depto = depto;
	}

	/**
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return the encabezadoFactura
	 */
	public String getEncabezadoFactura() {
		return encabezadoFactura;
	}

	/**
	 * @param encabezadoFactura the encabezadoFactura to set
	 */
	public void setEncabezadoFactura(String encabezadoFactura) {
		this.encabezadoFactura = encabezadoFactura;
	}

	/**
	 * @return the nit
	 */
	public String getNit() {
		return nit;
	}

	/**
	 * @param nit the nit to set
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}

	/**
	 * @return the pais
	 */
	public String getPais() {
		return pais;
	}

	/**
	 * @param pais the pais to set
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}

	/**
	 * @return the pieFactura
	 */
	public String getPieFactura() {
		return pieFactura;
	}

	/**
	 * @param pieFactura the pieFactura to set
	 */
	public void setPieFactura(String pieFactura) {
		this.pieFactura = pieFactura;
	}

	/**
	 * @return the prefijofactura
	 */
	public String getPrefijofactura() {
		return prefijofactura;
	}

	/**
	 * @param prefijofactura the prefijofactura to set
	 */
	public void setPrefijofactura(String prefijofactura) {
		this.prefijofactura = prefijofactura;
	}

	/**
	 * @return the razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * @param razonSocial the razonSocial to set
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	/**
	 * @return the resolucion
	 */
	public String getResolucion() {
		return resolucion;
	}

	/**
	 * @param resolucion the resolucion to set
	 */
	public void setResolucion(String resolucion) {
		this.resolucion = resolucion;
	}

	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return the tipoIdentificacion
	 */
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}

	/**
	 * @param tipoIdentificacion the tipoIdentificacion to set
	 */
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}

	/**
	 * @return the observable
	 */
	public Observable getObservable() {
		return observable;
	}


	/**
	 * @param observable the observable to set
	 */
	public void setObservable(Observable observable) {
		this.observable = observable;
	}


	/**
	 * @return the logoJsp
	 */
	public String getLogoJsp() {
		if(UtilidadTexto.getBoolean(this.getNivelLogoPorEmpresaInstitucion()))
		{
			return logoInstitucion;
		}
		else
		{
			return logoJsp;
		}
	}


	/**
	 * @param logoJsp the logoJsp to set
	 */
	public void setLogoJsp(String logoJsp) {
		this.logoJsp = logoJsp;
	}


	/**
	 * @return the logoReportes
	 */
	public String getLogoReportes() {
		return logoReportes;
	}


	/**
	 * @param logoReportes the logoReportes to set
	 */
	public void setLogoReportes(String logoReportes) {
		this.logoReportes = logoReportes;
	}


	public double getRangoFinalFactura() {
		return rangoFinalFactura;
	}


	public void setRangoFinalFactura(double rangoFinalFactura) {
		this.rangoFinalFactura = rangoFinalFactura;
	}


	public double getRangoInicialFactura() {
		return rangoInicialFactura;
	}


	public void setRangoInicialFactura(double rangoInicialFactura) {
		this.rangoInicialFactura = rangoInicialFactura;
	}


	/**
	 * @return the digitoVerificacion
	 */
	public String getDigitoVerificacion() {
		return digitoVerificacion;
	}


	/**
	 * @param digitoVerificacion the digitoVerificacion to set
	 */
	public void setDigitoVerificacion(String digitoVerificacion) {
		this.digitoVerificacion = digitoVerificacion;
	}


	/**
	 * @return the descripcionTipoIdentificacion
	 */
	public String getDescripcionTipoIdentificacion() {
		return descripcionTipoIdentificacion;
	}


	/**
	 * @param descripcionTipoIdentificacion the descripcionTipoIdentificacion to set
	 */
	public void setDescripcionTipoIdentificacion(
			String descripcionTipoIdentificacion) {
		this.descripcionTipoIdentificacion = descripcionTipoIdentificacion;
	}

	/**
	 * @return the esEmpresaInstitucion
	 */
	public boolean isEsEmpresaInstitucion() {
		return esEmpresaInstitucion;
	}

	/**
	 * @return the esEmpresaInstitucion
	 */
	public boolean getEsEmpresaInstitucion() {
		return esEmpresaInstitucion;
	}

	/**
	 * @param esEmpresaInstitucion the esEmpresaInstitucion to set
	 */
	public void setEsEmpresaInstitucion(boolean esEmpresaInstitucion) {
		this.esEmpresaInstitucion = esEmpresaInstitucion;
	}		

	

	/**
	 * GET AND SET de Ubicacion Logo Reportes	 */
	public String getUbicacionLogo() {	return ubicacionLogo;	}
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de 
	 * @param ubicacionLogo
	 */
	public void setUbicacionLogo(String ubicacionLogo) {	this.ubicacionLogo = ubicacionLogo;	}

	/**
	 * @return the pieHistoriaClinica
	 */
	public String getPieHistoriaClinica() {
		return pieHistoriaClinica;
	}
	/**
	 * @param pieHistoriaClinica the pieHistoriaClinica to set
	 */
	public void setPieHistoriaClinica(String pieHistoriaClinica) {
		this.pieHistoriaClinica = pieHistoriaClinica;
	}
	/**
	 * @return the indicativo
	 */
	public String getIndicativo() {
		return indicativo;
	}
	/**
	 * @param indicativo the indicativo to set
	 */
	public void setIndicativo(String indicativo) {
		this.indicativo = indicativo;
	}
	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}
	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}



	/**
	 * @return the codigoPais
	 */
	public String getCodigoPais() {
		return codigoPais;
	}



	/**
	 * @param codigoPais the codigoPais to set
	 */
	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}



	/**
	 * @return the codigoDepto
	 */
	public String getCodigoDepto() {
		return codigoDepto;
	}



	/**
	 * @param codigoDepto the codigoDepto to set
	 */
	public void setCodigoDepto(String codigoDepto) {
		this.codigoDepto = codigoDepto;
	}



	/**
	 * @return the codigoCiudad
	 */
	public String getCodigoCiudad() {
		return codigoCiudad;
	}



	/**
	 * @param codigoCiudad the codigoCiudad to set
	 */
	public void setCodigoCiudad(String codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}



	public String getCodigoInterfaz() {
		return codigoInterfaz;
	}



	public void setCodigoInterfaz(String codigoInterfaz) {
		this.codigoInterfaz = codigoInterfaz;
	}
	
	public String getLogoParametrizadoJSP()
	{
		String dir=System.getProperty("directorioImagenes");
		String ruta=getLogoReportes();
		if(UtilidadTexto.isEmpty(ruta))
		{
			return logoJsp;
		}
		String logo=ruta.substring(ruta.lastIndexOf('/')+1, ruta.length());
		return dir+logo;
	}



	/**
	 * Obtiene el valor del atributo logoInstitucion
	 *
	 * @return Retorna atributo logoInstitucion
	 */
	public String getLogoInstitucion()
	{
		return logoInstitucion;
	}



	/**
	 * Establece el valor del atributo logoInstitucion
	 *
	 * @param valor para el atributo logoInstitucion
	 */
	public void setLogoInstitucion(String logoInstitucion)
	{
		this.logoInstitucion = logoInstitucion;
	}



	/**
	 * Obtiene el valor del atributo razonSocialInstitucionBasica
	 *
	 * @return Retorna atributo razonSocialInstitucionBasica
	 */
	public String getRazonSocialInstitucionBasica()
	{
		return razonSocialInstitucionBasica;
	}

	/**
	 * Obtiene el valor del atributo razonSocialInstitucionBasica
	 *
	 * @return Retorna atributo razonSocialInstitucionBasica
	 */
	public String getRazonSocialInstitucionCompleta()
	{
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(this.codigoInstitucionBasica)))
		{
			return razonSocialInstitucionBasica+" "+razonSocial;
		}
		return razonSocialInstitucionBasica;
	}



	/**
	 * Establece el valor del atributo razonSocialInstitucionBasica
	 *
	 * @param valor para el atributo razonSocialInstitucionBasica
	 */
	public void setRazonSocialInstitucionBasica(String razonSocialInstitucionBasica)
	{
		this.razonSocialInstitucionBasica = razonSocialInstitucionBasica;
	}



	/**
	 * Obtiene el valor del atributo codigoInstitucionBasica
	 *
	 * @return Retorna atributo codigoInstitucionBasica
	 */
	public int getCodigoInstitucionBasica()
	{
		return codigoInstitucionBasica;
	}



	/**
	 * Establece el valor del atributo codigoInstitucionBasica
	 *
	 * @param valor para el atributo codigoInstitucionBasica
	 */
	public void setCodigoInstitucionBasica(int codigoInstitucionBasica)
	{
		this.codigoInstitucionBasica = codigoInstitucionBasica;
	}



	/**
	 * Obtiene el valor del atributo nivelLogoPorInstitucion
	 *
	 * @return Retorna atributo nivelLogoPorInstitucion
	 */
	public String getNivelLogoPorEmpresaInstitucion()
	{
		return nivelLogoPorEmpresaInstitucion;
	}



	/**
	 * Establece el valor del atributo nivelLogoPorInstitucion
	 *
	 * @param valor para el atributo nivelLogoPorInstitucion
	 */
	public void setNivelLogoPorEmpresaInstitucion(String nivelLogoPorInstitucion)
	{
		this.nivelLogoPorEmpresaInstitucion = nivelLogoPorInstitucion;
	}



	/**
	 * @param resolucionFacturaVaria the resolucionFacturaVaria to set
	 */
	public void setResolucionFacturaVaria(String resolucionFacturaVaria) {
		this.resolucionFacturaVaria = resolucionFacturaVaria;
	}



	/**
	 * @return the resolucionFacturaVaria
	 */
	public String getResolucionFacturaVaria() {
		return resolucionFacturaVaria;
	}



	/**
	 * @param prefijoFacturaVaria the prefijoFacturaVaria to set
	 */
	public void setPrefijoFacturaVaria(String prefijoFacturaVaria) {
		this.prefijoFacturaVaria = prefijoFacturaVaria;
	}



	/**
	 * @return the prefijoFacturaVaria
	 */
	public String getPrefijoFacturaVaria() {
		return prefijoFacturaVaria;
	}



	/**
	 * @param rangoInicialFacturaVaria the rangoInicialFacturaVaria to set
	 */
	public void setRangoInicialFacturaVaria(BigDecimal rangoInicialFacturaVaria) {
		this.rangoInicialFacturaVaria = rangoInicialFacturaVaria;
	}



	/**
	 * @return the rangoInicialFacturaVaria
	 */
	public BigDecimal getRangoInicialFacturaVaria() {
		return rangoInicialFacturaVaria;
	}



	/**
	 * @param rangoFinalFacturaVaria the rangoFinalFacturaVaria to set
	 */
	public void setRangoFinalFacturaVaria(BigDecimal rangoFinalFacturaVaria) {
		this.rangoFinalFacturaVaria = rangoFinalFacturaVaria;
	}



	/**
	 * @return the rangoFinalFacturaVaria
	 */
	public BigDecimal getRangoFinalFacturaVaria() {
		return rangoFinalFacturaVaria;
	}



	/**
	 * @param encabezadoFacturaVaria the encabezadoFacturaVaria to set
	 */
	public void setEncabezadoFacturaVaria(String encabezadoFacturaVaria) {
		this.encabezadoFacturaVaria = encabezadoFacturaVaria;
	}



	/**
	 * @return the encabezadoFacturaVaria
	 */
	public String getEncabezadoFacturaVaria() {
		return encabezadoFacturaVaria;
	}



	/**
	 * @param pieFacturaVaria the pieFacturaVaria to set
	 */
	public void setPieFacturaVaria(String pieFacturaVaria) {
		this.pieFacturaVaria = pieFacturaVaria;
	}



	/**
	 * @return the pieFacturaVaria
	 */
	public String getPieFacturaVaria() {
		return pieFacturaVaria;
	}



	public String getPieAmbMedicamentos() {
		return pieAmbMedicamentos;
	}



	public void setPieAmbMedicamentos(String pieAmbMedicamentos) {
		this.pieAmbMedicamentos = pieAmbMedicamentos;
	}




}

