
/*
 * Creado   28/09/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.mundo.tesoreria;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatos;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.FacturasVarias.UtilidadesFacturasVarias;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.RecibosCajaDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDeudoresDatosFinan;
import com.princetonsa.dto.facturasVarias.DtoRecibosCaja;
import com.princetonsa.dto.odontologia.DtoBeneficiarioCliente;
import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;
import com.princetonsa.dto.tesoreria.DtoBonoSerialValor;
import com.princetonsa.dto.tesoreria.DtoDetallePagosBonos;
import com.princetonsa.enu.administracion.EmunConsecutivosTesoreriaCentroAtencion;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.ConsecutivosCentroAtencion;
import com.princetonsa.mundo.carteraPaciente.DatosFinanciacion;
import com.princetonsa.mundo.carteraPaciente.DocumentosGarantia;
import com.princetonsa.mundo.consultaExterna.Multas;
import com.princetonsa.mundo.facturasVarias.AprobacionAnulacionPagosFacturasVarias;
import com.princetonsa.mundo.facturasVarias.PagosFacturasVarias;
import com.princetonsa.mundo.odontologia.BeneficiariosTarjetaCliente;
import com.princetonsa.mundo.odontologia.VentasTarjetasCliente;
import com.servinte.axioma.orm.RecibosCajaId;
import com.servinte.axioma.orm.RecibosCajaXTurno;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.orm.delegate.tesoreria.CajasDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.RecibosCajaDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.RecibosCajaXTurnoDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.TurnoDeCajaDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * 
 *
 * @version 1.0, 28/09/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class RecibosCaja 
{
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(RecibosCaja.class);	
	/**
	 * DAO de este objeto, para trabajar con cierre de saldos iniciales 
	 * cartera en la fuente de datos
	 */	
	private static RecibosCajaDao recibosDao;
	/**
	 * cï¿½digo de la instituciï¿½n
	 */
	private int institucion;
	/**
	 * cï¿½digo del concepto
	 */
	private String codConcepto;
	/**
	 * almacena las sentencias de la
	 * consulta
	 */
	private HashMap mapaConsulta;
	/**
	 * almacena el valor del concepto segun
	 * su tipo, ej, si es por paciente el valor, 
	 * puede ser ninguno o una via de ingreso
	 */
	private String valorConcepto;
	/**
	 * almacena el filtro segun el tipo de 
	 * concepto todos
	 */
	private final String filtroTodos="todos";
	/**
	 * almacena el filtro segun el tipo de 
	 * concepto ninguno
	 */
	private final String fltroNinguno="ninguno";
	/**
	 * consecutivo de la factura para
	 * realizar busqueda
	 */
	private int consecutivoFact;
	/**
	 * almacena la fecha de la factura para 
	 * realizar la busqueda
	 */
	private String fechaFact;
	/**
	 * almacena el nombre del paciente
	 * para realizar la busqueda
	 */
	private String identificacionPaciente;	
	/**
	 * tipo de identificacion del paciente
	 */
	private String tipoIdentificacion;
	/**
	 * almacena el filtro que se genera segun
	 * el concepto seleccionado.
	 */
	private String fltro;
	/**
	 * primer nombre del paciente
	 */
	private String primerNombre;
	/**
	 * Segundo Nombre del paciente
	 */
	private String segundoNombre;
	/**
	 * primer apellido del paciente
	 */
	private String primerApellido;
	/**
	 * segundo apellido del paciente
	 */
	private String segundoApellido;
	/**
	 * almacena el login del usuario
	 */
	private String usuario;
	/**
	 * almacena el nombre de la persona/entidad
	 * que realiza el pago
	 */
	private String recibidoDe;
	/**
	 * observaciones del recibo de caja
	 */
	private String observaciones;
	/**
	 * caja a la cual se encuentra asociado
	 * el usuario
	 */
	private String caja;
	/**
	 * almacena todos los datos para
	 * generar el recibo de caja
	 */
	private HashMap mapaRecibosCaja;
	/**
	 * alamcena el recibo de caja insertado,
	 * solo para el resumen
	 */
	private String numReciboCaja;
	
	/**
	 * 
	 */
	private String consecutivoReciboCaja;
	
	/**
	 * 
	 */
	private String empresaFiltro;
	
	/**
	 * 
	 */
	private String convenioFiltro;
	
	/**
	 * 
	 */
	private String identificacionTerceroFiltro;
	
	
	//*************************************
	//  Facturas Varias 
	private String codigoAplicacion;
	private String codigoPagGenEmp;
	private ArrayList<DtoRecibosCaja> facturasVarias;
	private int posSelConcepto;
   	private int regSeleccionado;
   	private int tipoConcepto;
   	private String tipoFiltro;
	//*************************************
	
   	
   	//*************************************
   	// Anexo 762
   	/**
	 * Datos de Financiacion
	 */
	private DtoDatosFinanciacion datosFinanciacion; 
   	//*************************************
   	
	//*************************************
	// Anexo 875
	private int codigoTipoIngresoTesoreria;
	private int codigoContrato;
	// Fin Anexo 875
	//*************************************
	
	
   	private int codigoCentroAtencion;
   	
   	/**
   	 * Ingrso asociado al recibo de caja
   	 */
   	private Integer ingreso;

	/**
	 * Metodo para inicializar los atributos de la clase.
	 *
	 */
	public void reset ()
	{		
		this.mapaConsulta=new HashMap();	   
		this.mapaRecibosCaja=new HashMap();	  
		this.consecutivoFact=ConstantesBD.codigoNuncaValido;
		//this.codConcepto="-1";
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.fechaFact="";
		this.identificacionPaciente="";
		this.tipoIdentificacion="";
		this.primerApellido="";
		this.segundoApellido="";
		this.segundoNombre="";
		this.primerNombre="";   
		this.usuario="";
		this.recibidoDe="";
		this.observaciones="";
		this.caja="";
		this.valorConcepto="";
		this.fltro="";
		this.numReciboCaja="";
		this.consecutivoReciboCaja="";
		
		this.empresaFiltro="";
		this.convenioFiltro="";
		this.identificacionTerceroFiltro="";
		
		//  Facturas Varias 
		this.codigoAplicacion = "";
		this.codigoPagGenEmp = "";
		this.facturasVarias = new ArrayList<DtoRecibosCaja> ();
		this.posSelConcepto = 0;
	   	this.regSeleccionado = 0;
	   	this.tipoConcepto = 0 ;
	   	this.tipoFiltro = "" ;
	   	
	   	this.codigoCentroAtencion = 0;
	   	
	   	//*************************************
		// Anexo 875
		this.codigoTipoIngresoTesoreria = ConstantesBD.codigoNuncaValido;
		this.codigoContrato = ConstantesBD.codigoNuncaValido;
		// Fin Anexo 875
		//*************************************
	   	this.ingreso=null;

	}


	/**
	 * @return the tipoIdentificacion
	 */
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}


	/**
	 * @return the identificacionPaciente
	 */
	public String getIdentificacionPaciente() {
		return identificacionPaciente;
	}


	/**	 * Metodo que resetea los atributos de la clase cuando se cambia de concepto	 */
	public void resetConcepto(int seleccion) {		
		this.mapaConsulta=new HashMap();	   
		this.mapaRecibosCaja=new HashMap();	  
		this.identificacionPaciente="";
		this.tipoIdentificacion="";
		this.primerApellido="";
		this.segundoApellido="";
		this.segundoNombre="";
		this.primerNombre="";   
		this.recibidoDe="";
		this.observaciones="";
		this.fltro="";
		this.numReciboCaja="";
		this.consecutivoReciboCaja="";
		this.empresaFiltro="";
		this.convenioFiltro="";
		this.identificacionTerceroFiltro="";

		this.valorConcepto="";

		
		//this.consecutivoFact=ConstantesBD.codigoNuncaValido;
		this.codConcepto = Integer.toString(seleccion); //-1
		
		/*this.fechaFact="";
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.usuario="";
		this.caja="";
		*/
	}
	
	
	
	
	
	
	
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores vï¿½lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( recibosDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			recibosDao= myFactory.getRecibosCajaDao();
			
			if( recibosDao!= null )
				return true;
		}
		return false;
	}
	/**
	 * Constructor
	 *
	 */
	public RecibosCaja ()
	{
		this.reset();
		init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Metodo para construir la consulta de ajustes
	 *
	 */
	private void construirConsultaConceptos()
	{
		int numRegBusqueda=0;		
		InfoDatos campo;
		this.mapaConsulta=new HashMap();
		String columnas="codigo_tipo_ingreso as codigo_tipo_ingreso," +
						"valor as valor";
		this.mapaConsulta.put("camposTraer",columnas);		  
		this.mapaConsulta.put("tabla","conceptos_ing_tesoreria");
		campo=new InfoDatos("codigo",this.codConcepto+"","=");  
		this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
		numRegBusqueda++;
		campo=new InfoDatos("institucion",this.institucion,"=");
		this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
		numRegBusqueda++;
		this.mapaConsulta.put("numRegBusqueda",numRegBusqueda+"");		
	}
	/**
	 * metodo para generar la consulta de los conceptos
	 * @param con
	 */
	public String generarConsultaAjustes(Connection con)
	{
		String array="";
		this.construirConsultaConceptos();
		ResultSetDecorator rs=UtilidadBD.consultaGenerica(con,this.mapaConsulta);
		try 
		{
			while(rs.next())
			{
				if(UtilidadTexto.isEmpty(rs.getString("valor")))
				{
					array=rs.getInt("codigo_tipo_ingreso")+""+"@0";
				}
				else
				{	
					array=rs.getInt("codigo_tipo_ingreso")+""+"@"+rs.getString("valor");
				}	
			}
		}catch (SQLException e) 
		{		   
			e.printStackTrace();
		} 
		return array;
	}
	/**
	 * Metodo para consultar el tipo detalle
	 * de formas de pago
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public int generarConsultaTipoDetalleFormaPago(Connection con,int consecutivo)
	{
		int resp=ConstantesBD.codigoNuncaValido;
		ResultSetDecorator rs=recibosDao.generarConsultaFormaPago(con,consecutivo);
		try 
		{
		   while(rs.next())
		   {
			  resp=rs.getInt("tipo_detalle"); 
		   }
		}catch (SQLException e) 
		{		   
			e.printStackTrace();
		} 
		return resp;
	}
	/**
	 * metodo para realizar la consulta de facturas
	 * @param con
	 * @return
	 */
	public HashMap generarConsultaFacturas(Connection con)
	{
	   HashMap facturas=new HashMap ();	   
	   int pos=0;
	   boolean existenReg=false;
	   ResultSetDecorator rs=recibosDao.generarConsultaFacturas(con,this.consecutivoFact,this.fechaFact,this.identificacionPaciente,this.tipoIdentificacion,this.institucion,this.valorConcepto);
		try 
		{
		   while(rs.next())
		   {
			   logger.info("fecha: "+UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha")+""));
			   
			 facturas.put("nombre_via_ingreso_"+pos,rs.getString("nombre_via_ingreso")+"");
			 facturas.put("consecutivo_factura_"+pos,rs.getInt("consecutivo_factura")+"");
			 facturas.put("codigo_factura_"+pos,rs.getInt("codigo_factura")+"");
			 facturas.put("fecha_"+pos,UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha")+""));
			 facturas.put("nombre_paciente_"+pos,rs.getString("nombre_paciente")+"");
			 facturas.put("valor_neto_paciente_"+pos,rs.getDouble("valor_neto_paciente")+"");
			 facturas.put("pagosPaciente_"+pos,rs.getDouble("pagosPaciente")+"");
			 facturas.put("numeroIdentificacion_"+pos,rs.getString("numero_identificacion")+"");
			 facturas.put("tipoIdentificacion_"+pos,rs.getString("tipo_identificacion")+"");
			 facturas.put("saldoPaciente_"+pos,(rs.getDouble("valor_neto_paciente")-rs.getDouble("pagosPaciente"))+"");
			 facturas.put("nombre_centro_atencion_"+pos,rs.getString("nombre_centro_atencion"));
			 //facturas.put("codigo_paciente_"+pos, rs.getInt("codigo_paciente")+""); Se elimina por que la consulta no devuelve codigo_paciente Tarea Xplanner ID 183330
			 pos ++;
			 existenReg=true;
		   }
		   if(existenReg)
			   facturas.put("numReg",pos+"");
		}catch (SQLException e) 
		{		   
			e.printStackTrace();
		} 
	   return facturas;
	}
	
	
	
	/**
	 *Metodo para construir la consulta de convenios 
	 *
	 */
	private void construirConsultaConvenios()
	{
		int numRegBusqueda=0;	
		int numRegInner=0;
		InfoDatos campo;
		this.mapaConsulta=new HashMap();
		
		String columnas="e.razon_social as nombre_empresa," +
						"c.nombre as nombre_convenio," +
						"c.codigo as codigo_convenio," +
						"getnomtiporegimen(c.tipo_regimen) as regimen," +
						"t.numero_identificacion as identificacion," +
						"'NI' as tipo_identificacion " ; //TODO tipo de Identificacion sin documentar
		
		if(this.codigoTipoIngresoTesoreria==ConstantesBD.codigoTipoIngresoTesoreriaAnticipoConvenioOdon)
			columnas += ", con.codigo as codigo_contrato, " +
					"coalesce(con.numero_contrato,' ') as num_contrato, " +
					"to_char(con.fecha_inicial,'dd/mm/yyyy') as fecha_ini_con, " +
					"to_char(con.fecha_final,'dd/mm/yyyy') as fecha_fin_con ";
		
		this.mapaConsulta.put("camposTraer",columnas);		  
		this.mapaConsulta.put("tabla","convenios c");
		campo=new InfoDatos("empresas e","e.codigo=c.empresa");
		this.mapaConsulta.put("inner_"+numRegInner,campo);  
		numRegInner++;
		campo=new InfoDatos("terceros t","t.codigo=e.tercero");
		this.mapaConsulta.put("inner_"+numRegInner,campo);  
		numRegInner++;
		
		if(this.codigoTipoIngresoTesoreria==ConstantesBD.codigoTipoIngresoTesoreriaAnticipoConvenioOdon)
		{
			//String whereContratos="con.convenio = c.codigo AND con.controla_anticipos = '"+ConstantesBD.acronimoSi+"' ";
			String whereContratos="con.convenio = c.codigo ";
			campo=new InfoDatos("facturacion.contratos con ",whereContratos);
			this.mapaConsulta.put("inner_"+numRegInner,campo);  
			numRegInner++;
		}
		
		campo=new InfoDatos("t.institucion",this.institucion,"=");
		this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
		numRegBusqueda++;
		if(!UtilidadTexto.isEmpty(this.valorConcepto)&&!this.valorConcepto.equals("0"))
		{
			if(this.codigoTipoIngresoTesoreria==ConstantesBD.codigoTipoIngresoTesoreriaAnticipoConvenioOdon)
			{ // Anexo 875 - Cambios en Func X Sonria (Agendamiento Ayudas Dx)
				
				// se adiciona que el tipo de atencion a filtar sea de Odontologia
				campo=new InfoDatos("c.tipo_atencion",ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico,"="); 
				this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);
				numRegBusqueda++;
				
				// se verifica el valor del concepto
				if(this.valorConcepto.equals(ConstantesIntegridadDominio.acronimoAcompanianteNinguno) || 
							this.valorConcepto.equals(ConstantesBD.codigoTipoRegimenParticular+"")){
					campo=new InfoDatos("c.tipo_regimen",String.valueOf(ConstantesBD.codigoTipoRegimenParticular),"<>");
					this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);
				}else{
					campo=new InfoDatos("c.tipo_regimen",this.valorConcepto,"=");
					this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);
				}
			}else{ // manejo de conceptos tipo convenio
				campo=new InfoDatos("c.tipo_regimen",this.valorConcepto,"=");
				this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);
			}
			numRegBusqueda++;
		}
		if(!UtilidadTexto.isEmpty(this.convenioFiltro))
		{
			campo=new InfoDatos("c.nombre",this.convenioFiltro,"LIKE");
			this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
			numRegBusqueda++;  
		}
		if(!UtilidadTexto.isEmpty(this.empresaFiltro))
		{
			campo=new InfoDatos("e.razon_social",this.empresaFiltro,"LIKE");
			this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
			numRegBusqueda++;  
		}
		if(!UtilidadTexto.isEmpty(this.identificacionTerceroFiltro))
		{
			campo=new InfoDatos("t.numero_identificacion",this.identificacionTerceroFiltro,"LIKE");
			this.mapaConsulta.put("campoBusqueda_"+numRegBusqueda,campo);  
			numRegBusqueda++;  
		}
		
		
		this.mapaConsulta.put("numRegBusqueda",numRegBusqueda+"");
		this.mapaConsulta.put("numRegInner",numRegInner+"");
		campo=new InfoDatos("e.razon_social,c.nombre","asc");
		this.mapaConsulta.put("order",campo);
	}
	/**
	 * metodo para generar la consulta de los convenios
	 * @param con
	 */
	public HashMap generarConsultaConvenios(Connection con)
	{
		logger.info("\n\n generarConsultaConvenios--> convenio="+this.convenioFiltro+" this.empresa->"+this.empresaFiltro+" idtercero->"+this.identificacionTerceroFiltro+" valor concetp->"+this.valorConcepto+"\n\n");
		HashMap convenios=new HashMap ();
		int pos=0;
		this.construirConsultaConvenios();
		boolean existenReg=false;
		ResultSetDecorator rs=UtilidadBD.consultaGenerica(con,this.mapaConsulta);
		try 
		{
			while(rs.next())
			{
				convenios.put("nombreEmpresa_"+pos,rs.getString("nombre_empresa")+"");
				convenios.put("nombreConvenio_"+pos,rs.getString("nombre_convenio")+"");
				convenios.put("codigoConvenio_"+pos,rs.getString("codigo_convenio")+"");
				convenios.put("regimen_"+pos,rs.getString("regimen")+"");
				convenios.put("identificacion_"+pos,rs.getString("identificacion")+"");
				convenios.put("tipoIdentificacion_"+pos,rs.getString("tipo_identificacion")+"");
				//************ ANEXO 875 *************************
				if(this.codigoTipoIngresoTesoreria==ConstantesBD.codigoTipoIngresoTesoreriaAnticipoConvenioOdon)
				{
					convenios.put("codigoContrato_"+pos,rs.getString("codigo_contrato")+"");
					convenios.put("numeroContrato_"+pos,rs.getString("num_contrato")+"");
					convenios.put("fechaIniContrato_"+pos,rs.getString("fecha_ini_con")+"");
					convenios.put("fechaFinContrato_"+pos,rs.getString("fecha_fin_con")+"");
				}
				//************ ANEXO 875 *************************
				if(!existenReg)
					existenReg=true;
				pos++;
			}
			if(existenReg)
				convenios.put("numReg",pos+"");  
		}catch (SQLException e) 
		{		   
			e.printStackTrace();
		} 
		return convenios;
	}
	/**
	 * metodo para generar las sentencias de 
	 * la consulta de pacientes.
	 *
	 */
	public HashMap generarConsultaPacientes(Connection con, boolean controlarAbonoPaciente)
	{
	   HashMap pacientes=new HashMap ();	   
	   int pos=0;
	   boolean existenReg=false;
	   ResultSetDecorator rs=recibosDao.generarConsultaPacientes(con,this.primerNombre,this.segundoNombre,this.primerApellido,this.segundoApellido,this.tipoIdentificacion,this.identificacionPaciente,this.institucion, controlarAbonoPaciente);
		try 
		{
		   while(rs.next())
		   {
			   pacientes.put("codigoPaciente_"+pos,rs.getInt("codigo_paciente")+"");
			   pacientes.put("primerNombre_"+pos,rs.getString("primer_nombre")+"");			 
			   pacientes.put("segundoNombre_"+pos,rs.getString("segundo_nombre")+"");
			   pacientes.put("primerApellido_"+pos,rs.getString("primer_apellido")+"");
			   pacientes.put("segundoApellido_"+pos,rs.getString("segundo_apellido")+"");
			   pacientes.put("tipoIdentificacacion_"+pos,rs.getString("tipo_identificacion")+"");
			   pacientes.put("numeroIdentificacion_"+pos,rs.getString("numero_identificacion")+"");			   
			   int ingreso=rs.getInt("ingreso");
			   pacientes.put("ingreso_"+pos, ingreso>0?ingreso:null);
			 pos ++;
			 existenReg=true;
		   }
		   if(existenReg)
			   pacientes.put("numReg",pos+"");
		}catch (SQLException e) 
		{		   
			logger.error("Ocurrio un error en la consulta del paciente en el metodo generarConsultaPacientes: " +e);
			//e.printStackTrace();
			
		} 
	   return pacientes;  
	}
	/**
	 * Metodo para realizar la generaciï¿½n del 
	 * recibo de caja
	 * @param con
	 * @return boolean true transaccion efectiva
	 */
	public boolean generarReciboCajaTrans (Connection con,int estadoRC, ArrayList<DtoDetallePagosBonos> formaPagoBonos, int codigoIngreso)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean inicioTrans,enTransaccion=true;
		String consecutivo="";
		int codigoRC=ConstantesBD.codigoNuncaValido;
		ArrayList<Integer> listaValoresConsecutivos=new ArrayList<Integer>();
		try 
		{
			if (recibosDao==null)
			{
				throw new SQLException ("No se pudo inicializar la conexiï¿½n con la fuente de datos");
			} 
			inicioTrans=myFactory.beginTransaction(con);
			if (!inicioTrans )
			{				
				myFactory.abortTransaction(con);
				logger.warn("Transaction Aborted-No se inicio la transacciï¿½n");				
			}
			else
			{
				boolean manejaConsecutivoTesoreriaXCentroAtencion=UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConsecutivosTesoreriaPorCentroAtencion(institucion));

				if(manejaConsecutivoTesoreriaXCentroAtencion)
				{
					consecutivo=ConsecutivosCentroAtencion.incrementarConsecutivoXCentroAtencion(
							codigoCentroAtencion, 
							EmunConsecutivosTesoreriaCentroAtencion.AnulacionReciboCaja.getNombreConsecutivoBaseDatos(), institucion).toString();
				}
				else
				{                
					boolean consecutivoValido=false;
					int valorConsecutivo=Utilidades.convertirAEntero(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoReciboCaja, this.institucion));
					consecutivoValido=esConsecutivoValido(con, valorConsecutivo,this.institucion);
					if(!consecutivoValido)
					{
						listaValoresConsecutivos.add(valorConsecutivo);
						int contIntentos=0;
						valorConsecutivo=0;
						while(valorConsecutivo<=0&&contIntentos<100)
						{
							valorConsecutivo=Utilidades.convertirAEntero(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoReciboCaja, this.institucion));
							contIntentos++;
							consecutivoValido=esConsecutivoValido(con, valorConsecutivo,this.institucion);
							if(!consecutivoValido)
							{
								listaValoresConsecutivos.add(valorConsecutivo);
								valorConsecutivo=0;
							}
						}
					}
					
					this.actualizarUsoConsecutivos(con, listaValoresConsecutivos, valorConsecutivo);
					
					if(valorConsecutivo>0)
					{
						consecutivo=valorConsecutivo+"";
					}
					else
					{
						logger.warn("Transacción Abortada");
					    myFactory.abortTransaction(con);
					    return false;
					}
				}

				
				logger.info("Consecutivo ReciboCaja->"+consecutivo);	
			   this.consecutivoReciboCaja=consecutivo;
			   
			   UtilidadTransaccion.getTransaccion().begin();
			   codigoRC=recibosDao.insertarReciboCaja(con,consecutivo,this.institucion,this.usuario,this.caja,
															   UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),
															   UtilidadFecha.getHoraActual(),
															   this.recibidoDe,this.observaciones,this.codigoCentroAtencion,estadoRC);
			   UtilidadTransaccion.getTransaccion().commit();
			   
			   this.numReciboCaja=codigoRC+"";
			   enTransaccion=codigoRC>0;
			   if(enTransaccion)
				   this.setCodigoPagGenEmp(consecutivo);
			   
			   
			   if(!enTransaccion)
			   {
				   logger.warn("Transacciï¿½n Abortada");
				   myFactory.abortTransaction(con);
				   if(manejaConsecutivoTesoreriaXCentroAtencion)
				   {
					   ConsecutivosCentroAtencion.liberarConsecutivo(con, ConstantesBD.nombreConsecutivoReciboCaja, codigoCentroAtencion, new BigDecimal(consecutivo));
				   }
				   else
				   {
					   UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoReciboCaja, this.institucion, consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
				   }
				   
			   }
			   else
			   {
				   //logger.info("existe deudor: "+this.datosFinanciacion.getDeudor().getExiteDeudor());
				   //logger.info("existe codeudor: "+this.datosFinanciacion.getCodeudor().getExiteDeudor());
				   enTransaccion=generarDetalleConceptos(con,myFactory, codigoIngreso);
				   if(!enTransaccion)
				   {
					   logger.warn("Transacciï¿½n Abortada");
					   if(manejaConsecutivoTesoreriaXCentroAtencion)
					   {
							ConsecutivosCentroAtencion.liberarConsecutivo(con, ConstantesBD.nombreConsecutivoReciboCaja, codigoCentroAtencion, new BigDecimal(consecutivo));
					   }
					   else
					   {
						   enTransaccion=UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoReciboCaja, this.institucion, consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
					   }

					   myFactory.abortTransaction(con);
				   }
				   else
				   {
					   UtilidadTransaccion.getTransaccion().begin();						   
					   enTransaccion=generarDetalleReciboCaja(con,myFactory, formaPagoBonos);						   
					   UtilidadTransaccion.getTransaccion().commit();
					   if(manejaConsecutivoTesoreriaXCentroAtencion)
					   {
						   ConsecutivosCentroAtencion.finalizarConsecutivo(con, ConstantesBD.nombreConsecutivoReciboCaja, codigoCentroAtencion, new BigDecimal(consecutivo));
					   }
					   else
					   {
						   enTransaccion=UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoReciboCaja, this.institucion, consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
					   }

					   if(!enTransaccion)
					   {
						   logger.warn("Transacciï¿½n Abortada");
						   myFactory.abortTransaction(con);
					   }
				   }
			   }
			}
			if(enTransaccion)
			{
				myFactory.endTransaction(con);
				logger.warn("Transacciï¿½n Finalizada");	
			}
			return enTransaccion;
		}
		catch (SQLException e) 
		{		   
			e.printStackTrace();
			return false;
		}   
	}
	
	/**
	 * Metodo para realizar la generaciï¿½n del 
	 * recibo de caja
	 * @param con
	 * @param formaPagoBonos
	 * @param codigoCentroAtencion 
	 * @param codigoIngreso 
	 * @return boolean true transaccion efectiva
	 */
	public boolean generarReciboCajaTrans (Connection con,boolean transaccion,int estadoRC, ArrayList<DtoDetallePagosBonos> formaPagoBonos, int codigoCentroAtencion, int codigoIngreso)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		boolean inicioTrans,enTransaccion=true;
		String consecutivo="";
		int codigoRC=ConstantesBD.codigoNuncaValido;
		ArrayList<Integer> listaValoresConsecutivos=new ArrayList<Integer>();
		try 
		{
			if (recibosDao==null)
			{
				throw new SQLException ("No se pudo inicializar la conexiï¿½n con la fuente de datos");
			} 
			inicioTrans=transaccion;
			if (!inicioTrans )
			{				
				myFactory.abortTransaction(con);
				logger.warn("Transaction Aborted-No se inicio la transacciï¿½n");				
			}
			else
			{	
				boolean manejaConsecutivoTesoreriaXCentroAtencion=UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConsecutivosTesoreriaPorCentroAtencion(institucion));
				if(manejaConsecutivoTesoreriaXCentroAtencion)
				{
					consecutivo=ConsecutivosCentroAtencion.incrementarConsecutivoXCentroAtencion(
							codigoCentroAtencion, 
							EmunConsecutivosTesoreriaCentroAtencion.ReciboCaja.getNombreConsecutivoBaseDatos(), institucion).toString();
				}
				else
				{
					boolean consecutivoValido=false;
					int valorConsecutivo=Utilidades.convertirAEntero(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoReciboCaja, this.institucion));
					consecutivoValido=esConsecutivoValido(con, valorConsecutivo,this.institucion);
					if(!consecutivoValido)
					{
						listaValoresConsecutivos.add(valorConsecutivo);
						int contIntentos=0;
						valorConsecutivo=0;
						while(valorConsecutivo<=0&&contIntentos<100)
						{
							valorConsecutivo=Utilidades.convertirAEntero(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoReciboCaja, this.institucion));
							contIntentos++;
							consecutivoValido=esConsecutivoValido(con, valorConsecutivo,this.institucion);
							if(!consecutivoValido)
							{
								listaValoresConsecutivos.add(valorConsecutivo);
								valorConsecutivo=0;
							}
						}
					}
					
					this.actualizarUsoConsecutivos(con, listaValoresConsecutivos, this.institucion);
					
					if(valorConsecutivo>0)
					{
						consecutivo=valorConsecutivo+"";
					}
					else
					{
						logger.warn("Transacción Abortada");
					    myFactory.abortTransaction(con);
					    return false;
					}
					
				}
				logger.info("Consecutivo ReciboCaja->"+consecutivo);	
				this.consecutivoReciboCaja=consecutivo;
				
				
				
				
				/**
				 * INSERTAR RECIBO DE CAJA
				 * ********************************************************************************
				 */
				codigoRC=recibosDao.insertarReciboCaja(con,consecutivo,this.institucion,this.usuario,this.caja,
															   UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),
															   UtilidadFecha.getHoraActual(),
															   this.recibidoDe,this.observaciones,this.codigoCentroAtencion,estadoRC);		
				   con.commit(); 
				   this.numReciboCaja=codigoRC+"";
				enTransaccion=codigoRC>0;
				if(!enTransaccion)
				{
					logger.warn("Transacción Abortada");
					myFactory.abortTransaction(con);
					if(manejaConsecutivoTesoreriaXCentroAtencion)
					{
						ConsecutivosCentroAtencion.liberarConsecutivo(con, ConstantesBD.nombreConsecutivoReciboCaja, codigoCentroAtencion, new BigDecimal(consecutivo));
					}
					else
					{
						UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoReciboCaja, this.institucion, consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
					}
				}
				else
				{
					if(manejaConsecutivoTesoreriaXCentroAtencion)
					{
						ConsecutivosCentroAtencion.finalizarConsecutivo(con, ConstantesBD.nombreConsecutivoReciboCaja, codigoCentroAtencion, new BigDecimal(consecutivo));
					}
					else
					{
						enTransaccion=UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoReciboCaja, this.institucion, consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
					}
					if(!enTransaccion)
					{
						logger.warn("Transacciï¿½n Abortada");
						myFactory.abortTransaction(con);
					}
					else
					{	
						/**
						 * el siguiente tramo de codigo se reorganizï¿½ y adapto por tarea 137002 ya que no estaba haciendo aplicacion de pagos para conceptos que no fueran de tipo venta tarjeta
						 * y apartir de un cambio en cï¿½digo que habia quedado mal realizado
						 */
						//GENERO EL REGISTRO EN pagos_general_empresa
					   this.consecutivoReciboCaja=consecutivo+"";
						this.numReciboCaja=codigoRC+"";
						
						
						enTransaccion=generarDetalleConceptos(con,myFactory, codigoIngreso);
						//con.commit();
						
						if(!enTransaccion)
						{
							logger.warn("Transacciï¿½n Abortada");
							myFactory.abortTransaction(con);
						}
						else
						{
							enTransaccion=generarDetalleReciboCaja(con,myFactory, formaPagoBonos);
							
							//---------------------------------
							// Facturas Varias  
							logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
							logger.info(" FACTURAS VARIAS  TIPO CONCEPTO-------------  ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC 3==------->>>>"+tipoConcepto);
							logger.info("\n\n\n");
							
							if(tipoConcepto == ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC)
							{						   		
								if(this.getTipoFiltro().equals(ConstantesIntegridadDominio.acronimoFactura))
								{
									//luego hago la aplicacion de pago, menos para el concepto de tipo acronimoTipoVentaTarjetaodontologica, ya que estos deben ahcer la aplicacion directamente en la funcionalidad de aplicacion de pagos
									if(facturasVarias!=null && 
											facturasVarias.size()>0 && 
											facturasVarias.get(this.getRegSeleccionado()).getConceptoFactura().equals(ConstantesIntegridadDominio.acronimoTipoVentaTarjetaodontologica)
											)
									{
									
										// SE CAMBIA EL ESTADO DE LA TARJETA CLIENTE
										accionCambiarEstadoVentaTarjetaActivo(con);
									}
										
									
									if(!facturasVarias.get(this.getRegSeleccionado()).getConceptoFactura().equals(ConstantesIntegridadDominio.acronimoTipoVentaTarjetaodontologica))
									{
										
										logger.info("SI ENTRE ACA ES PORQUE ENTRE A APLICAR PAGOS!!");
										this.setCodigoAplicacion(accionGuardarConceptos(con, usuario));
										if(!this.getCodigoAplicacion().equals(""))
										{
											if(this.accionGuardarAplicPagosFacVarias(con, usuario)>0)
											{
												if(this.accionModificarEstadoAplicPagFacVar(con, usuario))
												{
													if(!this.accionModificarValorFacVaria(con, usuario))
													{
														logger.info("************NO SE HIZO LA APLICACION DE PAGOS!!!!!!");
														enTransaccion = false;
													}
												}else
												{
													enTransaccion = false;
												}
											}else
											{
												enTransaccion = false;
											}
										}else
										{
											enTransaccion = false;
										}
									}
										
									if(facturasVarias!=null && facturasVarias.size()>0 && facturasVarias.get(this.getRegSeleccionado()).getConceptoFactura().equals(ConstantesIntegridadDominio.acronimoMultas))
									{
						   			 logger.info("******************************** ES DE MULTAS*********************************************!!!!!!!");
						   				if(!facturasVarias.get(this.getRegSeleccionado()).getMultasCitas().equals(""))
											if(!this.accionModificarEstadoMultaCitas(con, facturasVarias.get(this.getRegSeleccionado()).getMultasCitas()))
												enTransaccion = false;
									}	
									
						   		}	
							//---------------------------------
						   	
							}
						   if(!enTransaccion)
						   {
							   logger.warn("Transacciï¿½n Abortada");
							   myFactory.abortTransaction(con);
						   }
					   }
				   }
			   }
			}
			if(enTransaccion)
			{
				myFactory.endTransaction(con);
				logger.warn("Transacciï¿½n Finalizada");	
			}
			
			UtilidadTransaccion.getTransaccion().begin();
			ArrayList<TurnoDeCaja> listaTurnos=new TurnoDeCajaDelegate().obtenerTurnoCajaAbiertoPorCaja(new CajasDelegate().findById(Integer.parseInt(caja)));
			if(listaTurnos!=null && listaTurnos.size()>0)
			{
				/* Solamente puede existir un turno de caja abierto*/
				TurnoDeCaja turno=listaTurnos.get(0);
				// Solamente se guarda en el caso de que sea una caja que requiere turno abierto 
				if(turno!=null)
				{
					RecibosCajaXTurno recibosCajaXTurno=new RecibosCajaXTurno();
					recibosCajaXTurno.setRecibosCaja(new RecibosCajaDelegate().findById(new RecibosCajaId(codigoRC+"", institucion)));
					recibosCajaXTurno.setTurnoDeCaja(turno);
					new RecibosCajaXTurnoDelegate().persist(recibosCajaXTurno);
				}
			}
			UtilidadTransaccion.getTransaccion().commit();
			
			return enTransaccion;
		}
		catch (SQLException e) 
		{		   
			e.printStackTrace();
			return false;
		}   
	}
	
	/**
	 * Metodo para hacer las posibles validaciones que puede tener el sistema.
	 * @param con
	 * @param valorConsecutivo
	 * @param codigoInstitucionInt
	 * @return
	 */
	private boolean esConsecutivoValido(Connection con, int valorConsecutivo, int institucion) 
	{
		//valida si el consecutivo ya se asigno a otra factura para evitar facturas repetidas.
		if(UtilidadValidacion.esConsecutvioReciboCajaUsado(institucion,valorConsecutivo+""))
		{
			//UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoReciboCaja, institucion, valorConsecutivo+"", ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			return false;
		}
		return true;

	}
	
	/**
	 * Este método se encarga de actualizar los consecutivos en la tabla uso_consecutivos
	 * que ya han sido usados 
	 * @param con 
	 * @param listaValoresConsecutivos lista que almacena los valores a actualizar
	 * @param institucion código de la institución
	 */
	private void actualizarUsoConsecutivos(Connection con, ArrayList<Integer> listaValoresConsecutivos, int institucion) 
	{
		UtilidadBD.iniciarTransaccion(con);
		for (int i=0;i<listaValoresConsecutivos.size();i++) 
		{
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoReciboCaja, institucion, listaValoresConsecutivos.get(i).intValue()+"", ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
		}
		UtilidadBD.finalizarTransaccion(con);
		
	}
	
	/**
	 * ACCION CAMBIAR ESTADO LOS BENEFICIARIOS DE LA TARJETA CLIENTE= ACTIVO
	 * @param con Conexiï¿½n con la BD
	 */
	private void accionCambiarEstadoVentaTarjetaActivo(Connection con) {
		 ArrayList<DtoVentaTarjetasCliente> listaVentas = new ArrayList<DtoVentaTarjetasCliente>();
		 DtoVentaTarjetasCliente dtowhereventas = new DtoVentaTarjetasCliente();
		 dtowhereventas.setCodigoPkFacturaVaria(Integer.parseInt(facturasVarias.get(this.getRegSeleccionado()).getNroFactura()));
		 listaVentas = VentasTarjetasCliente.cargar(dtowhereventas);
		 for(DtoVentaTarjetasCliente i : listaVentas)
		 {
			BeneficiariosTarjetaCliente.activarTarjetaBeneficiario(con, ((Double)i.getCodigoPk()).intValue());
		 }
	}
	
	/**
	 * metodo para realizar el insert del detalle
	 * de el/los conceptos de recibos de caja
	 * @param con
	 * @param myFactory
	 * @param codigoIngreso 
	 * @return
	 */
	private boolean generarDetalleConceptos(Connection con, DaoFactory myFactory, int codigoIngreso)
	{
		boolean enTransaccion=false;
		try 
		{			
			String numReciboCaja=this.numReciboCaja.trim().equals("")?recibosDao.ultimoReiboCajaInsertado(con,this.institucion)+"":this.numReciboCaja;	
			logger.info("ReciboCaja->"+numReciboCaja);
			//****************************************
			// Anexo 762
			String claseDeudor="";
			String numeroIdentificacion="";
			int instituccion=ConstantesBD.codigoNuncaValido;
			int ingreso=ConstantesBD.codigoNuncaValido;
			// Fin Anexo 762
			//****************************************
		   
			logger.info("numero de recibos de caja: "+this.mapaRecibosCaja.get("numRegC"));
			
			for(int k=0;k<Integer.parseInt(this.mapaRecibosCaja.get("numRegC")+"");k++)
			{				
				//**********************************************
				// Anexo 762
				
				if(Utilidades.convertirAEntero(this.mapaRecibosCaja.get("codigoTipoConcepto_"+k)+"")==ConstantesBD.codigoTipoIngresoTesoreriaCarteraParticular){
					claseDeudor=this.datosFinanciacion.getDeudor().getClaseDeudor();
					numeroIdentificacion=this.datosFinanciacion.getDeudor().getNumeroIdentificacion();
					instituccion=this.datosFinanciacion.getDeudor().getInstitucion();
					ingreso=this.datosFinanciacion.getDeudor().getIngreso();
				}else{
					if(Utilidades.convertirAEntero(this.mapaRecibosCaja.get("codigoTipoConcepto_"+k)+"")==ConstantesBD.codigoTipoIngresoTesoreriaPacientes){
						logger.info("insertar deudor y/o codeudor");
						logger.info("existe deudor: "+this.datosFinanciacion.getDeudor().getExiteDeudor());
						logger.info("existe codeudor: "+this.datosFinanciacion.getCodeudor().getExiteDeudor());
						logger.info("es un documento nuevo: "+this.datosFinanciacion.getIsNuevoDoc());
						if(this.datosFinanciacion.getIsNuevoDoc().equals(ConstantesBD.acronimoSi)){
							logger.info("entra a esta parte");
							enTransaccion = generarDeudorCO(con);
						}
					}
				}
				logger.info("esta listo para insertar detalle concepto recibo de caja");
				// Fin Anexo 762
				//**********************************************
				if (instituccion==ConstantesBD.codigoNuncaValido)
					instituccion=this.institucion;
				
				logger.info(this.mapaRecibosCaja.get("codigo_concepto_"+k)+"");
				logger.info(this.mapaRecibosCaja.get("docSoporte_"+k)+"");
				logger.info(this.mapaRecibosCaja.get("valorConcepto_"+k)+"");
				logger.info(this.mapaRecibosCaja.get("tipoBeneficiario_"+k)+"");
				logger.info(this.mapaRecibosCaja.get("numeroBeneficiario_"+k)+"");
				logger.info(this.mapaRecibosCaja.get("nombreBeneficiario_"+k)+"");
				logger.info(this.mapaRecibosCaja.get("codDeudor_"+k)+"");
				
				
				enTransaccion=recibosDao.insertarDetalleConceptosRecibosCaja(con,
																				numReciboCaja,this.institucion+"",
																				this.mapaRecibosCaja.get("codigo_concepto_"+k)+"",
																				this.mapaRecibosCaja.get("docSoporte_"+k)+"",
																				this.mapaRecibosCaja.get("valorConcepto_"+k)+"",
																				this.mapaRecibosCaja.get("tipoBeneficiario_"+k)+"",
																				this.mapaRecibosCaja.get("numeroBeneficiario_"+k)+"",
																				this.mapaRecibosCaja.get("nombreBeneficiario_"+k)+"",
																				this.mapaRecibosCaja.get("codDeudor_"+k)+"",claseDeudor,
																				numeroIdentificacion,instituccion,ingreso,
																				this.mapaRecibosCaja.get("codigoPaciente_"+k)+"", 
																				this.mapaRecibosCaja.get("codigoTipoConcepto_"+k)+"" );
			  				
				if(!(mapaRecibosCaja.get("conceptoFactura_"+k)+"").equals("") && !(mapaRecibosCaja.get("conceptoFactura_"+k)+"").equals(null))
					if((mapaRecibosCaja.get("conceptoFactura_"+k)+"").equals(ConstantesIntegridadDominio.acronimoTipoVentaTarjetaodontologica))
						enTransaccion=recibosDao.actualizarEstadoTarjeta(con, this.mapaRecibosCaja.get("conceptoFactura"+k)+"", this.mapaRecibosCaja.get("codFacturaVaria_"+k)+"");
					
				if(!enTransaccion)
				{
					myFactory.abortTransaction(con);
					logger.warn("Transacciï¿½n Abortada-insert detalleConceptos");
					break;
				}
				else
				{
					//************************
					//Anexo 762
					logger.info("insertio bien e detalle conceptorecibo de caja");
					Log4JManager.info("Concepto "+this.mapaRecibosCaja.get("codigoTipoConcepto_"+k));
					this.datosFinanciacion.setDetalleConceptoRC(UtilidadBD.obtenerUltimoValorSecuencia(con, "tesoreria.seq_detalle_conceptos_rc"));
					//************************
					
					if(Integer.parseInt(this.mapaRecibosCaja.get("codigoTipoConcepto_"+k)+"")==ConstantesBD.codigoTipoIngresoTesoreriaNinguno)
					{
						//@todo No se guarda informaciï¿½n adicional, pendiente para alguna implementaciï¿½n   
					}
					else if(Integer.parseInt(this.mapaRecibosCaja.get("codigoTipoConcepto_"+k)+"")==ConstantesBD.codigoTipoIngresoTesoreriaPacientes)
					{
						enTransaccion=this.conceptoTipoPaciente(con,myFactory,k,numReciboCaja);
					}
					else if(Integer.parseInt(this.mapaRecibosCaja.get("codigoTipoConcepto_"+k)+"")==ConstantesBD.codigoTipoIngresoTesoreriaConvenios 
							|| Integer.parseInt(this.mapaRecibosCaja.get("codigoTipoConcepto_"+k)+"")==ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC
							|| Integer.parseInt(this.mapaRecibosCaja.get("codigoTipoConcepto_"+k)+"")==ConstantesBD.codigoTipoIngresoTesoreriaAnticipoConvenioOdon)
					{
						enTransaccion=this.conceptoTipoConvenio(con,myFactory,k,numReciboCaja);
					}
					else if(Integer.parseInt(this.mapaRecibosCaja.get("codigoTipoConcepto_"+k)+"")==ConstantesBD.codigoTipoIngresoTesoreriaAbonos)
					{
						int codDetalleConcepto=recibosDao.ultimoCodigoDetalleConceptos(con,this.institucion);
						logger.info("codDetalleConcepto->"+codDetalleConcepto);
						
						if ((this.ingreso == null || this.ingreso<= 0) && codigoIngreso > 0 ) {
							
							this.ingreso = codigoIngreso;
						}
						
						enTransaccion=this.conceptoTipoAbono(con,myFactory,k,numReciboCaja,codDetalleConcepto, this.ingreso);
					}
					else if(Integer.parseInt(this.mapaRecibosCaja.get("codigoTipoConcepto_"+k)+"")==ConstantesBD.codigoTipoIngresoTesoreriaCarteraParticular)
					{
						//@todo No se guarda informaciï¿½n adicional, pendiente para alguna implementaciï¿½n 
					}
					else if(Integer.parseInt(this.mapaRecibosCaja.get("codigoTipoConcepto_"+k)+"")==ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC)
					{
						//@todo No se guarda informaciï¿½n adicional, pendiente para alguna implementaciï¿½n 
					}
				}
			   }		 
		}
		catch (SQLException e) 
		{		   
			e.printStackTrace();
			return false;
		} 
		return enTransaccion;	
	}

	//*************************************************************************************************
	// Anexo 762
	/**
	 * generar DeudorCO
	 * @param con
	 * @return
	 */
	private boolean generarDeudorCO(Connection con) 
	{
		boolean enTrasaccion = false;
		logger.info("entra 1");
		logger.info("existe deudor: "+this.datosFinanciacion.getDeudor().getExiteDeudor());
		if(this.datosFinanciacion.getDeudor().getExiteDeudor().equals(ConstantesBD.acronimoSi))
		{// hacer la modificacin de los campos
			logger.info("entra 2");
			if(DatosFinanciacion.actualizarDeudor(con, this.datosFinanciacion.getDeudor()))
			{
				logger.info("entra 3");
				if(!this.datosFinanciacion.getCodeudor().getTipoDeudor().equals(""))
				{
					logger.info("existe codeudor: "+this.datosFinanciacion.getCodeudor().getExiteDeudor());
					if(this.datosFinanciacion.getCodeudor().getExiteDeudor().equals(ConstantesBD.acronimoSi)){
						logger.info("entra 4");
						enTrasaccion = DatosFinanciacion.actualizarDeudor(con, this.datosFinanciacion.getCodeudor())?true:false;
					}else{
						// insertar nuenvo codeudor
						logger.info("entra 5");
						this.datosFinanciacion.getCodeudor().setCodigoPK(DocumentosGarantia.insertarDeudorCo(con, llenarParametrosDeudorIng(this.datosFinanciacion.getCodeudor())));
						enTrasaccion = this.datosFinanciacion.getCodeudor().getCodigoPK()>0;
					}
				}else
					enTrasaccion = true;
			}
		}else{
			if(this.datosFinanciacion.getDeudor().getExiteDeudor().equals(ConstantesBD.acronimoNo))
			{// insertar nuevo deudor
				logger.info("entra 6");
				logger.info("valor de ingreso del deudor a insertar: "+this.datosFinanciacion.getDeudor().getIngreso());
				this.datosFinanciacion.getDeudor().setCodigoPK(DocumentosGarantia.insertarDeudorCo(con, llenarParametrosDeudorIng(this.datosFinanciacion.getDeudor())));
				if(this.datosFinanciacion.getDeudor().getCodigoPK()>0)
				{
					logger.info("inserto el nuevo deudor");
					if(!this.datosFinanciacion.getCodeudor().getTipoDeudor().equals(""))
					{
						logger.info("existe codeudor: "+this.datosFinanciacion.getCodeudor().getExiteDeudor());
						if(this.datosFinanciacion.getCodeudor().getExiteDeudor().equals(ConstantesBD.acronimoSi)){
							logger.info("entra 7");
							enTrasaccion = DatosFinanciacion.actualizarDeudor(con, this.datosFinanciacion.getCodeudor())?true:false;
						}else{
							// insertar nuenvo codeudor
							logger.info("entra 8");
							this.datosFinanciacion.getCodeudor().setCodigoPK(DocumentosGarantia.insertarDeudorCo(con, llenarParametrosDeudorIng(this.datosFinanciacion.getCodeudor())));
							enTrasaccion = this.datosFinanciacion.getCodeudor().getCodigoPK()>0;
						}
					}else
						enTrasaccion = true;
				}else
					enTrasaccion = false;
			}else
				enTrasaccion = false;
		}
		return enTrasaccion;
	}
	
	/**
	 * llenar parametros deudor ingreso
	 * @param dto
	 * @return
	 */
	public HashMap llenarParametrosDeudorIng(DtoDeudoresDatosFinan dto)
	{
		HashMap parametros = new HashMap();
		logger.info("entra llenar deudor ingreso");
		parametros.put("ingreso",dto.getIngreso());
		parametros.put("codigoPaciente", this.datosFinanciacion.getCodigoPacienteFac()/*dto.getCodigoPaciente()*/);
		parametros.put("institucion", dto.getInstitucion());
		parametros.put("claseDeudorCo", dto.getClaseDeudor());
		parametros.put("tipoDeudorCo", dto.getTipoDeudor());
		parametros.put("tipoIdentificacion", dto.getTipoIdentificacion());
		parametros.put("numeroIdentificacion", dto.getNumeroIdentificacion());
		parametros.put("primerNombre", dto.getPrimerNombre());
		parametros.put("segundoNombre", dto.getSegundoNombre());
		parametros.put("primerApellido", dto.getPrimerApellido());
		parametros.put("segundoApellido", dto.getSegundoApellido());
		parametros.put("direccionReside", dto.getDireccion());
		parametros.put("telefonoReside", dto.getTelefono());
		parametros.put("tipoOcupacion", dto.getOcupacion());
		parametros.put("ocupacion", dto.getDetalleocupacion());
		parametros.put("empresa", dto.getEmpresa());
		parametros.put("cargo", dto.getCargo());
		parametros.put("antiguedad", dto.getAntiguedad());
		parametros.put("direccionOficina", dto.getDireccionOficina());
		parametros.put("telefonoOficina", dto.getTelefonoOficina());
		parametros.put("usuarioModifica", this.datosFinanciacion.getUsuarioModifica());
		parametros.put("fechaModifica", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica", UtilidadFecha.getHoraActual());
		parametros.put("nombresReferencia", dto.getNombresReferenciaFamiliar());
		parametros.put("direccionReferencia", dto.getDireccionReferenciaFamiliar());
		parametros.put("telefonoReferencia", dto.getTelefonoReferenciaFamiliar());
		logger.info("salellenar deudor ingreso");
		return parametros;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap llenarDocumentoGarantia()
	{
		HashMap parametros = new HashMap(); 
		logger.info("entra llenar documento garantia");
		parametros.put("ingreso", this.datosFinanciacion.getIngreso());
		parametros.put("codigopaciente", this.datosFinanciacion.getCodigoPacienteFac());
		parametros.put("institucion", this.institucion);
		parametros.put("consecutivo", this.datosFinanciacion.getConsecutivo());
		parametros.put("anioconsecutivo", this.datosFinanciacion.getAnioConsecutivo());
		parametros.put("tipodocumento", this.datosFinanciacion.getTipoDocumento());
		parametros.put("fechageneracion", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horageneracion", UtilidadFecha.getHoraActual());
		parametros.put("estado", ConstantesIntegridadDominio.acronimoPolizaVigente);
		parametros.put("usuariomodifica", this.datosFinanciacion.getUsuarioModifica());
		parametros.put("fechamodifica", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horamodifica", UtilidadFecha.getHoraActual());
		parametros.put("garantiaIngreso", ConstantesBD.acronimoNo);
		parametros.put("cartera", ConstantesBD.acronimoSi);
		parametros.put("valor", this.datosFinanciacion.getValorForPago().doubleValue());
		logger.info("sale llenar documento garantia");
		return parametros;  
	}
	
	// Fin Anexo 762
	//*****************************************************************************************************************
	
	/**
	 * metodo para realizar los inserts adicionales
	 * del oncepto paciente y sus respectivas validaciones.
	 * verificar que se esta cancelando el valor neto paciente
	 * de la factura y actualizar el estado paciente de la
	 * factura a cancelado.
	 * @param con
	 * @param myFactory
	 * @return
	 */
	private boolean conceptoTipoPaciente(Connection con, DaoFactory myFactory,int index,String numReciboCaja)
	{
		boolean enTransaccion=true;
		try 
		{			
			if(Double.parseDouble(this.mapaRecibosCaja.get("valorConcepto_"+index)+"")==Double.parseDouble(this.mapaRecibosCaja.get("saldoPaciente_"+index)+""))
			{
				logger.warn("SE ACTUALIZA EL ESTADO PACIENTE DE LA FACTURA A [CANCELADO]");
				logger.info("valor Neto Paciente[Factura]->"+this.mapaRecibosCaja.get("saldoPaciente_"+index));
				logger.info("valor concepto cancelado->"+this.mapaRecibosCaja.get("valorConcepto_"+index));	 
				enTransaccion=recibosDao.actualizarEstadoPacienteFactura(con,this.institucion,Integer.parseInt(this.mapaRecibosCaja.get("codigoFactura_"+index)+""));
				if(!enTransaccion)
				{
					myFactory.abortTransaction(con);
					logger.warn("Transacciï¿½n Abortada-actualizando estado en la factura");				
				}
			}
			if(enTransaccion)
			{
				enTransaccion=recibosDao.insertarPagosFacturaPaciente(con,
																			ConstantesBD.codigoTipoDocumentoPagosReciboCaja+"",
																			numReciboCaja,
																			this.mapaRecibosCaja.get("codigoFactura_"+index)+"",
																			this.mapaRecibosCaja.get("valorConcepto_"+index)+"",
																			ConstantesBD.codigoEstadoReciboCajaRecaudado,this.institucion);
				if(!enTransaccion)
				{
					myFactory.abortTransaction(con);
					logger.warn("Transacciï¿½n Abortada-insertando pagos factura paciente");				
				}  
			}			
		}
		catch (SQLException e) 
		{		   
			e.printStackTrace();
			return false;
		} 
		return enTransaccion;
	}
	/**
	 * metodo para insertar informacion adicional
	 * en la tabla de pagos_general_empresa
	 * @param con
	 * @param myFactory
	 * @param index
	 * @return
	 */
	private boolean conceptoTipoConvenio(Connection con, DaoFactory myFactory,int index,String numReciboCaja)
	{
		boolean enTransaccion=true;
		try 
		{
			int codigoPk=Utilidades.getSiguienteValorSecuencia(con, "cartera.seq_pagos_general_empresa");
			enTransaccion=recibosDao.insertarPagosGeneralEmpresa(con,
																	Utilidades.convertirAEntero(this.mapaRecibosCaja.get("codigoConvenio_"+index)+""),
																	ConstantesBD.codigoTipoDocumentoPagosReciboCaja,
																	numReciboCaja,
																	ConstantesBD.codigoEstadoReciboCajaRecaudado,
																	this.mapaRecibosCaja.get("valorConcepto_"+index)+"",this.institucion,
																	UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),
																	this.mapaRecibosCaja.get("codDeudor_"+index)+"",
																	this.mapaRecibosCaja.containsKey("codigoContrato_"+index)?Utilidades.convertirAEntero(this.mapaRecibosCaja.get("codigoContrato_"+index)+""):ConstantesBD.codigoNuncaValido, codigoPk);
			
			this.setCodigoPagGenEmp(codigoPk+"");
			
			if(!enTransaccion)
			{
				myFactory.abortTransaction(con);
				logger.warn("Transacciï¿½n Abortada-insertando pagos factura paciente");				
			}else{ //****************** ANEXO 875 **************************
				if(this.mapaRecibosCaja.containsKey("codigoContrato_"+index) 
					&& Utilidades.convertirAEntero(this.mapaRecibosCaja.get("codigoContrato_"+index)+"")>0)
				{// se recalcula valor anticipo disponible
					if(!recalcularValorAnticipoDisponible(con, this.usuario, Utilidades.convertirAEntero(this.mapaRecibosCaja.get("codigoContrato_"+index)+""),this.mapaRecibosCaja.get("valorConcepto_"+index)+""))
					{
						myFactory.abortTransaction(con);
						logger.warn("Transacciï¿½n Abortada-Recalculando Valor Anticipo Disponible");
					}
				}
			}//****************** FIN ANEXO 875 **************************
		}
		catch (SQLException e) 
		{		   
			e.printStackTrace();
			return false;
		} 
		return enTransaccion;
	}
	/**
	 * metodo para insertar los movimientos de 
	 * abonos de recibos de caja
	 * @param con
	 * @param myFactory
	 * @param index
	 * @return
	 */
	private boolean conceptoTipoAbono(Connection con, DaoFactory myFactory,int index,String numReciboCaja,int codDetalleConcepto, Integer Ingreso)
	{
		boolean enTransaccion=true;
		try 
		{							
			//Se bsuca la informaciï¿½n del paciente para obtener el centro de atencion dueï¿½o
			com.princetonsa.mundo.Paciente paciente=new com.princetonsa.mundo.Paciente();
			ArrayList<com.princetonsa.mundo.Paciente> arrayPaciente=new ArrayList<com.princetonsa.mundo.Paciente>();
			paciente.setNumeroIdentificacion(this.mapaRecibosCaja.get("numeroBeneficiario_"+index)+"");
			arrayPaciente=UtilidadesManejoPaciente.obtenerDatosPaciente(paciente);
			
			//Se agrega el centro de atencion dueï¿½o Aneno 958
			enTransaccion=recibosDao.insertarMovimientosAbonos(con,
																	this.mapaRecibosCaja.get("codigoPaciente_"+index)+"",
																	numReciboCaja,
																	ConstantesBD.tipoMovimientoAbonoIngresoReciboCaja+"",
																	this.mapaRecibosCaja.get("valorConcepto_"+index)+"",
																	UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),
																	UtilidadFecha.getHoraActual(),this.institucion,arrayPaciente.get(0).getCentroAtencionDuenio(), ingreso, codigoCentroAtencion);
			if(!enTransaccion)
			{
				myFactory.abortTransaction(con);
				logger.warn("Transacciï¿½n Abortada-insertando movimiento abono");				
			}  
			else
			{
				int codigoMovAbono=recibosDao.ultimoCodigoMovimientoAbonos(con,this.institucion);
				enTransaccion=recibosDao.actualizarDocumentoDetalleConceptos(con,codigoMovAbono+"",codDetalleConcepto,this.institucion);
				if(!enTransaccion)
				{
					myFactory.abortTransaction(con);
					logger.warn("Transacciï¿½n Abortada-actualizando documento detalle conceptos");				
				} 
			}
		}
		catch (SQLException e) 
		{		   
			e.printStackTrace();
			return false;
		} 
		return enTransaccion;
	}
	/**
	 * metodo para realizar la insercion de el 
	 * detalle de pagos y los inserts de las
	 * diferentes formas de pago aplicadas
	 * @param con
	 * @param myFactory
	 * @param formaPagoBonos 
	 * @return
	 */
	private boolean generarDetalleReciboCaja(Connection con,DaoFactory myFactory, ArrayList<DtoDetallePagosBonos> formaPagoBonos)
	{
		boolean enTransaccion=false;
		HashMap mapaCheques=new HashMap();
		HashMap mapaTarjetas=new HashMap();
		
		String consecutivo=this.numReciboCaja.trim().equals("")?recibosDao.ultimoReiboCajaInsertado(con,this.institucion)+"":this.numReciboCaja;	
		try 
		{
			for(int k=0;k<Integer.parseInt(this.mapaRecibosCaja.get("numRegFP")+"");k++)
			{
				logger.info("\n\n\n\n*******************EL RC--------->"+consecutivo);
				/*
				 * Por cada bono se debe ingresar un detalle pago RC!
				 */
				if(Integer.parseInt(this.mapaRecibosCaja.get("tipoDetalle_"+k)+"")!=ConstantesBD.codigoTipoDetalleFormasPagoBono)
				{
					enTransaccion=recibosDao.insertarDetallePagoRC(con,consecutivo,this.institucion,this.mapaRecibosCaja.get("codigoFormaPago_"+k)+"",this.mapaRecibosCaja.get("valorFormaPago_"+k)+"");
				}
				if(Integer.parseInt(this.mapaRecibosCaja.get("tipoDetalle_"+k)+"")==ConstantesBD.codigoTipoDetalleFormasPagoNinguno)
				{
					
				}
				else if(Integer.parseInt(this.mapaRecibosCaja.get("tipoDetalle_"+k)+"")==ConstantesBD.codigoTipoDetalleFormasPagoCheque)
				{
					String consecutivo1=recibosDao.ultimoCodigoDetallePagos(con,this.institucion)+"";
					mapaCheques=(HashMap)this.mapaRecibosCaja.get("mapaCheques");  
					if(!mapaCheques.containsKey("esOtraFormaPago_"+k)) // esta variable solo es asignada en el form validate del estado generar recibos caja
					{ 	   
						enTransaccion=recibosDao.insertarMovimientoCheque(con,
																				   consecutivo1,
																				   mapaCheques.get("numeroCheque_"+k)+"",
																				   mapaCheques.get("codigoBanco_"+k)+"",
																				   mapaCheques.get("numeroCuenta_"+k)+"",
																				   mapaCheques.get("codigoCiudadPlaza_"+k)+"",					   
																				   mapaCheques.get("codigoDeptoPlaza_"+k)+"",
																				   UtilidadFecha.conversionFormatoFechaABD(mapaCheques.get("fechaGiro_"+k)+""),
																				   mapaCheques.get("valor_"+k)+"",
																				   mapaCheques.get("girador_"+k)+"",
																				   mapaCheques.get("direccionGirador_"+k)+"",
																				   mapaCheques.get("codigoCiudadGirador_"+k)+"",
																				   mapaCheques.get("codigoDeptoGirador_"+k)+"",
																				   mapaCheques.get("telefonoGirador_"+k)+"",
																				   mapaCheques.get("observaciones_"+k)+"",
																				   mapaCheques.get("codigoPaisPlaza_"+k)+"",
																				   mapaCheques.get("codigoPaisGirador_"+k)+"",
																				   mapaCheques.get("autorizacion_"+k)+"");	
				   }
				   else
					   enTransaccion=true;
				   if(!enTransaccion)
				   {
					   logger.warn("Transacciï¿½n Abortada-insert cheques");
					   myFactory.abortTransaction(con);
				   }
			   }
			   else if(Integer.parseInt(this.mapaRecibosCaja.get("tipoDetalle_"+k)+"")==ConstantesBD.codigoTipoDetalleFormasPagoTarjeta)
			   {
				   String consecutivo2=recibosDao.ultimoCodigoDetallePagos(con,this.institucion)+""; 
				   mapaTarjetas=(HashMap)this.mapaRecibosCaja.get("mapaTarjetas");   
				   logger.info("\n\n\nMapa tarjetas-->"+mapaTarjetas+" \n\n\n");
				   if(!mapaTarjetas.containsKey("esOtraFormaPago_"+k)) // esta variable solo es asignada en el form validate del estado generar recibos caja
				   {
					   enTransaccion=recibosDao.insertarMovimientosTarjetas(con,consecutivo2,
																						   mapaTarjetas.get("codigoTarjeta_"+k)+"",
																						   mapaTarjetas.get("numeroTarjeta_"+k)+"",
																						   mapaTarjetas.get("numeroComprobante_"+k)+"",
																						   mapaTarjetas.get("numeroAutorizacion_"+k)+"",
																						   UtilidadFecha.conversionFormatoFechaABD(mapaTarjetas.get("fecha_"+k)+""),
																						   mapaTarjetas.get("valor_"+k)+"",
																						   mapaTarjetas.get("girador_"+k)+"",					   
																						   mapaTarjetas.get("direccion_"+k)+"",
																						   mapaTarjetas.get("codigoCiudadGirador_"+k)+"",
																						   mapaTarjetas.get("codigoDeptoGirador_"+k)+"",
																						   mapaTarjetas.get("telefono_"+k)+"",
																						   mapaTarjetas.get("observaciones_"+k)+"",
					   																	   mapaTarjetas.get("codigoPaisGirador_"+k)+"",
					   																	   mapaTarjetas.get("fechaVencimiento_"+k)+"",
					   																	   mapaTarjetas.get("codigoSeguridad_"+k)+"",
					   																	   Integer.parseInt(mapaTarjetas.get("entidad_"+k)+""));
				   }
				   else
					   enTransaccion=true;
				   if(!enTransaccion)
				   {
					   logger.warn("Transacciï¿½n Abortada-insert tarjetas credito");
					   myFactory.abortTransaction(con);
				   }
			   }
			   //********************************************************************************
			   // Anexo 762
			   else if(Integer.parseInt(this.mapaRecibosCaja.get("tipoDetalle_"+k)+"")==ConstantesBD.codigoTipoDetalleFormasPagoLetra
					   || Integer.parseInt(this.mapaRecibosCaja.get("tipoDetalle_"+k)+"")==ConstantesBD.codigoTipoDetalleFormasPagoPagare)
			   {
				   //logger.info("entra 111");
				   this.datosFinanciacion.setDetallePagoRC(Utilidades.convertirAEntero(recibosDao.ultimoCodigoDetallePagos(con,this.institucion)+""));
				   logger.info("es nuevo documento: "+this.datosFinanciacion.getIsNuevoDoc());
				   if(this.datosFinanciacion.getIsNuevoDoc().equals(ConstantesBD.acronimoSi))
				   {// insertar documento de garantia, sino existe o si desearon crear uno nuevo
					   // se cargan los consecutivos al dto
					   logger.info("entra a generar el documento de garantia y el los datos de fuinanciacion");
					   if(Integer.parseInt(this.mapaRecibosCaja.get("tipoDetalle_"+k)+"")==ConstantesBD.codigoTipoDetalleFormasPagoLetra)
					   {
						   //logger.info("entra 222");
						   String aux = UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoLetraCambio,this.institucion);
						   logger.info("consecutivo auxiliar: "+aux);
						   this.datosFinanciacion.setConsecutivo(aux);
						   logger.info("consecutivo letra: "+this.getDatosFinanciacion().getConsecutivo());
						   String anioCo = UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoLetraCambio,this.institucion, this.datosFinanciacion.getConsecutivo());
						   logger.info("anio consecutivo letra: "+anioCo);
						   if(anioCo.equals(""))
							   this.datosFinanciacion.setAnioConsecutivo(" ");
						   else
							   this.datosFinanciacion.setAnioConsecutivo(anioCo);
						   this.datosFinanciacion.setTipoDocumento(ConstantesIntegridadDominio.acronimoTipoDocumentoLetra);
					   }else{
						   //logger.info("entra 333");
						   this.datosFinanciacion.setConsecutivo(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoPagare,this.institucion));
						   logger.info("consecutivo pagare: "+this.getDatosFinanciacion().getConsecutivo());
						   String anioCo = UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoPagare,this.institucion, this.datosFinanciacion.getConsecutivo());
						   logger.info("anio consecutivo pagare: "+anioCo);
						   if(anioCo.equals(""))
							   this.datosFinanciacion.setAnioConsecutivo(" ");
						   else
							   this.datosFinanciacion.setAnioConsecutivo(anioCo);
						   //this.datosFinanciacion.setAnioConsecutivo(UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoPagare,this.institucion, this.datosFinanciacion.getConsecutivo()));
						   this.datosFinanciacion.setTipoDocumento(ConstantesIntegridadDominio.acronimoTipoDocumentoPagare);
					   }
					   //logger.info("entra 444");
					   this.datosFinanciacion.setCodigoDocumentoGarantia(DocumentosGarantia.insertarDocumentoGarantia(con, llenarDocumentoGarantia()));
					   enTransaccion=this.datosFinanciacion.getCodigoDocumentoGarantia()>0;
					   //logger.info("entra 555");
					   if(enTransaccion)
					   {// insertar datos de financiacion
						   //logger.info("entra 666");
						   enTransaccion = DatosFinanciacion.insertDatosFinanciacion(con, this.datosFinanciacion)>0?true:false;
						   //logger.info("entra 777");
						   if(!enTransaccion){
							   logger.warn("Transacciï¿½n Abortada-insert datos financiaciï¿½n");
							   myFactory.abortTransaction(con);
							   if(Integer.parseInt(this.mapaRecibosCaja.get("tipoDetalle_"+k)+"")==ConstantesBD.codigoTipoDetalleFormasPagoLetra)
							   {
								   UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, 
									   ConstantesBD.nombreConsecutivoLetraCambio,
									   this.institucion,
									   this.datosFinanciacion.getConsecutivo(),
									   ConstantesBD.acronimoNo, ConstantesBD.acronimoNo );
							   }else{
								   UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, 
									   ConstantesBD.nombreConsecutivoPagare,
									   this.institucion,
									   this.datosFinanciacion.getConsecutivo(),
									   ConstantesBD.acronimoNo, ConstantesBD.acronimoNo );
							   }
						   }
						   
						   if(Integer.parseInt(this.mapaRecibosCaja.get("tipoDetalle_"+k)+"")==ConstantesBD.codigoTipoDetalleFormasPagoLetra)
						   {
							   UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, 
								   ConstantesBD.nombreConsecutivoLetraCambio,
								   this.institucion,
								   this.datosFinanciacion.getConsecutivo(),
								   ConstantesBD.acronimoSi, ConstantesBD.acronimoSi );
						   }else{
							   UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, 
								   ConstantesBD.nombreConsecutivoPagare,
								   this.institucion,
								   this.datosFinanciacion.getConsecutivo(),
								   ConstantesBD.acronimoSi, ConstantesBD.acronimoSi );
						   }
						   
					   }else{
						   logger.warn("Transacciï¿½n Abortada-insert documento garantia");
						   myFactory.abortTransaction(con);
					   }
				   }else{
					   if(this.datosFinanciacion.getIsNuevoDoc().equals(ConstantesBD.acronimoNo))
					   {// insertar los datos de financiacion de un documento asociado
						   if(enTransaccion)
						   {// insertar datos de financiacion de un documento asociado
							   
							   //logger.info("se actualizar el documento de garantia");
							   enTransaccion = DatosFinanciacion.actualizarDocGarAsociado(con, 
									   ConstantesBD.acronimoSi, 
									   this.datosFinanciacion.getValorForPago().doubleValue(), 
									   this.datosFinanciacion.getUsuarioModifica(), 
									   this.datosFinanciacion.getUsuarioModifica(), 
									   this.datosFinanciacion.getIngreso(), 
									   this.datosFinanciacion.getTipoDocumento(), 
									   Utilidades.convertirAEntero(this.datosFinanciacion.getConsecutivo()), 
									   this.datosFinanciacion.getAnioConsecutivo());
							   
							   this.datosFinanciacion.setCodigoDocumentoGarantia(DatosFinanciacion.consultaCodigoDocuGarantia(this.datosFinanciacion.getIngreso(),Utilidades.convertirAEntero(this.datosFinanciacion.getConsecutivo()),this.datosFinanciacion.getAnioConsecutivo()));
							   logger.info("valor de la transaccion: "+enTransaccion);
							   if(enTransaccion)
							   {							   
								   //logger.info("entra 666");
								   enTransaccion = DatosFinanciacion.insertDatosFinanciacion(con, this.datosFinanciacion)>0?true:false;
								   //logger.info("entra 777");
								   if(!enTransaccion){
									   logger.warn("Transacciï¿½n Abortada-insert datos financiaciï¿½n");
									   myFactory.abortTransaction(con);
								   }
							   }
						   }else{
							   logger.warn("Transacciï¿½n Abortada-insert documento garantia");
							   myFactory.abortTransaction(con);
						   } 
					   }
				   }
			   }
			   // Fin Anexo 762
			   //********************************************************************************
			   // Adiciï¿½n 157
			   else if(Integer.parseInt(this.mapaRecibosCaja.get("tipoDetalle_"+k)+"")==ConstantesBD.codigoTipoDetalleFormasPagoBono)
			   {
				   if(formaPagoBonos!=null && !formaPagoBonos.isEmpty())
				   {
					   DtoDetallePagosBonos bonoFormaPago=formaPagoBonos.get(k);
					   if(bonoFormaPago.isUtilizado() && bonoFormaPago.getCantidadBonos()>0)
					   {
						   for(DtoBonoSerialValor bono:bonoFormaPago.getSerialesBonos())
						   {
							   enTransaccion=recibosDao.insertarDetallePagoRC(con,consecutivo,this.institucion,this.mapaRecibosCaja.get("codigoFormaPago_"+k)+"",bono.getValor()+"");
							   String consecutivoDetallePagosRC=recibosDao.ultimoCodigoDetallePagos(con,this.institucion)+"";
							   enTransaccion=recibosDao.insertarMovimientoBono(con,bono.getSerial(), consecutivoDetallePagosRC, bonoFormaPago.getObservaciones());
						   }
					   }
					   
				   }
			   }
			}
		}
		catch (SQLException e) 
		{		   
			e.printStackTrace();
			return false;
		}   				   
		return enTransaccion;	   
	}
	
	/**
	 * @param con
	 * @param numeroReciboCaja2
	 * @param institucion2
	 * @param codigoEstadoReciboCajaAnulado
	 * @return
	 */
	public boolean actualizarEstadoReciboCaja(Connection con, String numeroReciboCaja, int institucion, int nuevoEstado)
	{
		return recibosDao.actualizarEstadoReciboCaja(con,numeroReciboCaja,institucion,nuevoEstado);
	}
	
	/**
	 * @param institucion Asigna institucion.
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	/**
	 * @param codConcepto Asigna codConcepto.
	 */
	public void setCodConcepto(String codConcepto) {
		this.codConcepto = codConcepto;
	}
	/**
	 * @param fltro Asigna fltro.
	 */
	public void setFltro(String fltro) {
		this.fltro = fltro;
	}
	/**
	 * @param consecutivoFact Asigna consecutivoFact.
	 */
	public void setConsecutivoFact(int consecutivoFact) {
		this.consecutivoFact = consecutivoFact;
	}
	/**
	 * @param fechaFact Asigna fechaFact.
	 */
	public void setFechaFact(String fechaFact) {
		this.fechaFact = fechaFact;
	}
	/**
	 * @param identificacionPaciente Asigna identificacionPaciente.
	 */
	public void setIdentificacionPaciente(String identificacionPaciente) {
		this.identificacionPaciente = identificacionPaciente;
	}
	/**
	 * @param tipoIdentificacion Asigna tipoIdentificacion.
	 */
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}
	/**
	 * @param valorConcepto Asigna valorConcepto.
	 */
	public void setValorConcepto(String valorConcepto) {
		this.valorConcepto = valorConcepto;
	}
	/**
	 * @return Retorna primerApellido.
	 */
	public String getPrimerApellido() {
		return primerApellido;
	}
	/**
	 * @param primerApellido Asigna primerApellido.
	 */
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}
	/**
	 * @return Retorna primerNombre.
	 */
	public String getPrimerNombre() {
		return primerNombre;
	}
	/**
	 * @param primerNombre Asigna primerNombre.
	 */
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}
	/**
	 * @return Retorna segundoApellido.
	 */
	public String getSegundoApellido() {
		return segundoApellido;
	}
	/**
	 * @param segundoApellido Asigna segundoApellido.
	 */
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}
	/**
	 * @return Retorna segundoNombre.
	 */
	public String getSegundoNombre() {
		return segundoNombre;
	}
	/**
	 * @param segundoNombre Asigna segundoNombre.
	 */
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}
	/**
	 * @return Retorna usuario.
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario Asigna usuario.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	/**
	 * @param observaciones Asigna observaciones.
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	/**
	 * @param recibidoDe Asigna recibidoDe.
	 */
	public void setRecibidoDe(String recibidoDe) {
		this.recibidoDe = recibidoDe;
	}
	/**
	 * @param caja Asigna caja.
	 */
	public void setCaja(String caja) {
		this.caja = caja;
	}	
	/**
	 * @param mapaRecibosCaja Asigna mapaRecibosCaja.
	 */
	public void setMapaRecibosCaja(HashMap mapaRecibosCaja) {
		this.mapaRecibosCaja = mapaRecibosCaja;
	}
	/**
	 * @return Retorna numReciboCaja.
	 */
	public String getNumReciboCaja() {
		return numReciboCaja;
	}
	/**
	 * @return Returns the caja.
	 */
	public String getCaja() {
		return caja;
	}
	/**
	 * @return the convenioFiltro
	 */
	public String getConvenioFiltro() {
		return convenioFiltro;
	}
	/**
	 * @param convenioFiltro the convenioFiltro to set
	 */
	public void setConvenioFiltro(String convenioFiltro) {
		this.convenioFiltro = convenioFiltro;
	}
	/**
	 * @return the empresaFiltro
	 */
	public String getEmpresaFiltro() {
		return empresaFiltro;
	}
	/**
	 * @param empresaFiltro the empresaFiltro to set
	 */
	public void setEmpresaFiltro(String empresaFiltro) {
		this.empresaFiltro = empresaFiltro;
	}
	/**
	 * @return the identificacionTerceroFiltro
	 */
	public String getIdentificacionTerceroFiltro() {
		return identificacionTerceroFiltro;
	}
	/**
	 * @param identificacionTerceroFiltro the identificacionTerceroFiltro to set
	 */
	public void setIdentificacionTerceroFiltro(String identificacionTerceroFiltro) {
		this.identificacionTerceroFiltro = identificacionTerceroFiltro;
	}
	
	/**
	 * 
	 * @param reciboCaja
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static String obtenerCajaCajeroRC(String reciboCaja, int codigoInstitucionInt) 
	{
		String respuesta=""+ConstantesBD.separadorSplit;
		Connection con=UtilidadBD.abrirConexion();
		respuesta=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRecibosCajaDao().obtenerCajaCajeroRC(con,reciboCaja,codigoInstitucionInt);
		UtilidadBD.closeConnection(con);
		return respuesta;
		
	}
	
	
	  /**
   * Metodo encargado de verificar si un recibo de caja
   * se encuentra registrado en aplicacion de pagos de facturas varias.
   * @param connection
   * @param codigoResivoCaja
   * @param institucion
   * @return
   */
  public static boolean estaRegistradoEnAplicacionPagosFacturasVarias (Connection connection, String codigoResivoCaja,String institucion)
  {
  	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRecibosCajaDao().estaRegistradoEnAplicacionPagosFacturasVarias(connection, codigoResivoCaja, institucion);
  }
  
  
  /**
	 * Busqueda
	 * @param con
	 * @param HashMap parametros
	 * @return ArrayList<DtoRecibosCaja>
	 */
	public ArrayList<DtoRecibosCaja> facturasVarias(Connection con, String cod_factura, String tipo_deudor, String cod_deudor,
			String cod_concepto, String cod_multas_citas, String fecha_generacion, UsuarioBasico usuario)
	{
		HashMap parametros = new HashMap();
		
		if((!cod_factura.equals(""))&&cod_factura!=null)
			{
			parametros.put("factura", cod_factura);
			parametros.put("codigo_pk_facturas_varias", UtilidadesFacturasVarias.obtenerPkFacturaVaria( new BigDecimal(cod_factura), usuario.getCodigoInstitucionInt()));
			}
		
		if((!tipo_deudor.equals(""))&&tipo_deudor!=null)
			parametros.put("tipo_deudor", tipo_deudor);
		
		if((!cod_deudor.equals(""))&&cod_deudor!=null)
			parametros.put("deudor", cod_deudor);
		
		if((!cod_concepto.equals(""))&&cod_concepto!=null)
			parametros.put("concepto", cod_concepto);
		
		if((!cod_multas_citas.equals(""))&&cod_multas_citas!=null)
			parametros.put("consectivo_mulcitas", cod_multas_citas);
		
		if((!fecha_generacion.equals(""))&&fecha_generacion!=null)
			parametros.put("fecha_generacion", fecha_generacion);

		logger.info("\n\nmapa parametros:: "+parametros);
				
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRecibosCajaDao().facturasVarias(con, parametros);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public boolean tipoConceptoIsMulta(Connection con, int concepto_fvarias)
	{
		HashMap parametros = new HashMap();
		parametros.put("consecutivo", concepto_fvarias);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRecibosCajaDao().tipoConceptoIsMulta(con, parametros);
	}
	
	
	/**
	 * Metodo de Ordenamiento por columna
	 * @param lista
	 * @param ultimaPropiedad
	 * @param propiedad
	 * @return
	 */
public static ArrayList<DtoRecibosCaja> ordenarColumna(String[] indices, ArrayList<DtoRecibosCaja> lista, String ultimaPropiedad,String propiedad){
		
		
		ArrayList<DtoRecibosCaja> listaOrdenada = new ArrayList<DtoRecibosCaja>();
		HashMap mapaFacVarias=new HashMap();
		mapaFacVarias.put("numRegistros","0");
		HashMap mapaOrdenado= new HashMap();
		mapaOrdenado.put("numRegistros","0");
		
		for(int i=0; i<lista.size();i++)
		{	
		   // Datos Visibles	
		   DtoRecibosCaja dtoFacVarias = (DtoRecibosCaja) lista.get(i);
		   mapaFacVarias.put("consecutivo_"+i, dtoFacVarias.getConsecutivo());
		   mapaFacVarias.put("nroFactura_"+i, dtoFacVarias.getNroFactura());
		   mapaFacVarias.put("fecha_"+i, dtoFacVarias.getFecha());
		   mapaFacVarias.put("saldoFactura_"+i, dtoFacVarias.getSaldoFactura());
		   mapaFacVarias.put("deudor_"+i, dtoFacVarias.getDeudor());
		   mapaFacVarias.put("identificacionDeudor_"+i, dtoFacVarias.getIdentificacionDeudor());
		   mapaFacVarias.put("conceptoFactura_"+i, dtoFacVarias.getConceptoFactura());
		   mapaFacVarias.put("multasCitas_"+i, dtoFacVarias.getMultasCitas());
		   
		   mapaFacVarias.put("numRegistros", i+1);
		  	   
		} 
		mapaOrdenado = Listado.ordenarMapa(indices, propiedad, ultimaPropiedad, mapaFacVarias, Utilidades.convertirAEntero(mapaFacVarias.get("numRegistros").toString()));
		mapaOrdenado.put("numRegistros", mapaFacVarias.get("numRegistros").toString());
		
		
		for(int j=0; j< Utilidades.convertirAEntero(mapaOrdenado.get("numRegistros").toString()); j++)
	 	   {
			DtoRecibosCaja dto = new DtoRecibosCaja();
			
			dto.setConsecutivo(mapaOrdenado.get("consecutivo_"+j).toString());
			dto.setNroFactura(mapaOrdenado.get("nroFactura_"+j).toString());
			dto.setFecha(mapaOrdenado.get("fecha_"+j).toString());
			dto.setSaldoFactura(mapaOrdenado.get("saldoFactura_"+j).toString());
			dto.setDeudor(mapaOrdenado.get("deudor_"+j).toString());
			dto.setIdentificacionDeudor(mapaOrdenado.get("identificacionDeudor_"+j).toString());
			dto.setConceptoFactura(mapaOrdenado.get("conceptoFactura_"+j).toString());
			dto.setMultasCitas(mapaOrdenado.get("multasCitas_"+j).toString());
			
			listaOrdenada.add(dto);
		   }
		
		return listaOrdenada;
		
		
	}

	 /**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param esConsulta 
	 */
	private String accionGuardarConceptos(Connection con, String usuario)
	{
		String codigoAplicacion = "";
		HashMap mapa = new HashMap();
		mapa.put("numRegistros",0);
		mapa.put("modificacion","false");
		mapa.put("fechaaplicacion", UtilidadFecha.getFechaActual());
		mapa.put("observaciones", "");
		mapa.put("estadoaplicacion", ConstantesBD.codigoEstadoAplicacionPagosPendiente+"");
		mapa.put("estadopago", ConstantesBD.codigoEstadoPagosAplicado+"");
		mapa.put("numeroaplicacion", "1");//Se coloca 1 porque por este flujo es el único valor válido   
		//mapa.put("pagogeneraempresa", this.getCodigoPagGenEmp());
		mapa.put("pagogeneraempresa", UtilidadBD.obtenerUltimoValorSecuencia(con, "seq_pagos_general_empresa"));
		mapa.put("usuario", usuario);
		mapa.put("fechagrabacion", UtilidadFecha.getFechaActual());
		mapa.put("horagrabacion", UtilidadFecha.getHoraActual());
		PagosFacturasVarias mundo = new PagosFacturasVarias();
		
		Utilidades.imprimirMapa(mapa);
		
		codigoAplicacion = mundo.guardarConceptosAplicacionPagosFacturasVarias(con, mapa);
		logger.info("codigo de aplicacion >>>> "+codigoAplicacion);
		return codigoAplicacion;
	}
	
	
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param esConsulta 
	 */
	private int accionGuardarAplicPagosFacVarias(Connection con, String usuario)
	{
		String codigoAplicacion = "";
		HashMap mapa = new HashMap();
		mapa.put("numRegistros",1);
		mapa.put("bd_0","false");
		mapa.put("eliminado_0","false");
		mapa.put("codaplicacion", this.getCodigoAplicacion());
		mapa.put("codigofactura_0", facturasVarias.get(this.getRegSeleccionado()).getNroFactura());
		mapa.put("valpago_0",facturasVarias.get(this.getRegSeleccionado()).getSaldoFactura());
		
		PagosFacturasVarias mundo = new PagosFacturasVarias();
		if(mundo.guardarAplicacionFacturas(con, mapa)>0)
			return 1;
		else
			return ConstantesBD.codigoNuncaValido;
	}
	
	
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param esConsulta 
	 */ 
	private boolean accionModificarEstadoAplicPagFacVar(Connection con, String usuario)
	{
		String codigoAplicacion = "";
		HashMap mapa = new HashMap();
		mapa.put("usuarioaprobanul",usuario);
		mapa.put("fechaaprobanul",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mapa.put("horaaprobanul",UtilidadFecha.getHoraActual());
		mapa.put("estado","2"); //modificar a estado aprobado
		mapa.put("codigoAplicacion",this.getCodigoAplicacion());
		AprobacionAnulacionPagosFacturasVarias aprAnuPagFacVar = new AprobacionAnulacionPagosFacturasVarias();
		return aprAnuPagFacVar.modificarAprobado(con, mapa);
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param esConsulta 
	 */ 
	private boolean accionModificarValorFacVaria(Connection con, String usuario)
	{
		String codigoAplicacion = "";
		HashMap mapa = new HashMap();
		mapa.put("valorPagoFacturaVarias",facturasVarias.get(this.getRegSeleccionado()).getSaldoFactura());
		mapa.put("codigoFacturaVarias",facturasVarias.get(this.getRegSeleccionado()).getNroFactura());
		AprobacionAnulacionPagosFacturasVarias aprAnuPagFacVar = new AprobacionAnulacionPagosFacturasVarias();
		return aprAnuPagFacVar.modificarAprobadoFacturas(con, mapa);
	}
	
	
	/**
	 * modificacion de estado de las multas de citas asociadas a una factura varia
	 * 
	 */
	public boolean accionModificarEstadoMultaCitas(Connection con, String consecutivosMultaCita)
	{
		Multas multaCita = new Multas();
		String aux[];
		boolean resul = true ;
		aux = consecutivosMultaCita.split(",");
		for(int i=0 ; i < aux.length ;i++)
		{
			if(multaCita.verificarEstadoMultaCita(con, ConstantesIntegridadDominio.acronimoEstadoFacturado, aux[i].toString()))
				if(!multaCita.actualizarEstadoMultaCita(con, "PAG", aux[i].toString()))
					resul = false;
		}
		return resul;
	}
	
	
	/**
	 * Actualizar deudor
	 * @param con
	 * @param HashMap parametros
	 * @return boolean
	 */
	public boolean actualizarDeudorPagosGenEmp(Connection con, String deudor, String codigo)
	{
		HashMap mapa = new HashMap();
		mapa.put("deudor", deudor);
		mapa.put("codigo", codigo);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRecibosCajaDao().actualizarDeudorPagosGenEmp(con, mapa);
	}
	
	/**
	 * valor concepto ingreso tesoreria
	 * @param con
	 * @param parametros
	 * @return
	 */
	public String getValorConceptoIngresoTesoreria(Connection con, String codigo)
	{
		HashMap mapa = new HashMap();
		mapa.put("codigo", codigo);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRecibosCajaDao().getValorConceptoIngresoTesoreria(con, mapa);
	}
	
	/**
	 * metodo que recalcula el valor de anticipo disponible
	 * @param con
	 * @param usuario
	 * @param codigoContrato
	 * @return
	 */
	public static boolean recalcularValorAnticipoDisponible(Connection con, String usuario, int codigoContrato, String valorAnticipo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRecibosCajaDao().recalcularValorAnticipoDisponible(con, usuario, codigoContrato, valorAnticipo);
	}
	
	/**
	 * @return the codigoAplicacion
	 */
	public String getCodigoAplicacion() {
		return codigoAplicacion;
	}

	/**
	 * @param codigoAplicacion the codigoAplicacion to set
	 */
	public void setCodigoAplicacion(String codigoAplicacion) {
		this.codigoAplicacion = codigoAplicacion;
	}

	/**
	 * @return the codigoPagGenEmp
	 */
	public String getCodigoPagGenEmp() {
		return codigoPagGenEmp;
	}

	/**
	 * @param codigoPagGenEmp the codigoPagGenEmp to set
	 */
	public void setCodigoPagGenEmp(String codigoPagGenEmp) {
		this.codigoPagGenEmp = codigoPagGenEmp;
	}


	/**
	 * @return the facturasVarias
	 */
	public ArrayList<DtoRecibosCaja> getFacturasVarias() {
		return facturasVarias;
	}


	/**
	 * @param facturasVarias the facturasVarias to set
	 */
	public void setFacturasVarias(ArrayList<DtoRecibosCaja> facturasVarias) {
		this.facturasVarias = facturasVarias;
	}


	/**
	 * @return the posSelConcepto
	 */
	public int getPosSelConcepto() {
		return posSelConcepto;
	}


	/**
	 * @param posSelConcepto the posSelConcepto to set
	 */
	public void setPosSelConcepto(int posSelConcepto) {
		this.posSelConcepto = posSelConcepto;
	}


	/**
	 * @return the regSeleccionado
	 */
	public int getRegSeleccionado() {
		return regSeleccionado;
	}


	/**
	 * @param regSeleccionado the regSeleccionado to set
	 */
	public void setRegSeleccionado(int regSeleccionado) {
		this.regSeleccionado = regSeleccionado;
	}


	/**
	 * @return the tipoConcepto
	 */
	public int getTipoConcepto() {
		return tipoConcepto;
	}


	/**
	 * @param tipoConcepto the tipoConcepto to set
	 */
	public void setTipoConcepto(int tipoConcepto) {
		this.tipoConcepto = tipoConcepto;
	}


	/**
	 * @return the tipoFiltro
	 */
	public String getTipoFiltro() {
		return tipoFiltro;
	}


	/**
	 * @param tipoFiltro the tipoFiltro to set
	 */
	public void setTipoFiltro(String tipoFiltro) {
		this.tipoFiltro = tipoFiltro;
	}


	/**
	 * @return the codigoCentroAtencion
	 */
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}


	/**
	 * @param codigoCentroAtencion the codigoCentroAtencion to set
	 */
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}


	/**
	 * @return the datosFinanciacion
	 */
	public DtoDatosFinanciacion getDatosFinanciacion() {
		return datosFinanciacion;
	}


	/**
	 * @param datosFinanciacion the datosFinanciacion to set
	 */
	public void setDatosFinanciacion(DtoDatosFinanciacion datosFinanciacion) {
		this.datosFinanciacion = datosFinanciacion;
	}


	/**
	 * @return the codigoTipoIngresoTesoreria
	 */
	public int getCodigoTipoIngresoTesoreria() {
		return codigoTipoIngresoTesoreria;
	}


	/**
	 * @param codigoTipoIngresoTesoreria the codigoTipoIngresoTesoreria to set
	 */
	public void setCodigoTipoIngresoTesoreria(int codigoTipoIngresoTesoreria) {
		this.codigoTipoIngresoTesoreria = codigoTipoIngresoTesoreria;
	}

	/**
	 * @return the codigoContrato
	 */
	public int getCodigoContrato() {
		return codigoContrato;
	}

	/**
	 * @param codigoContrato the codigoContrato to set
	 */
	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}


	/**
	 * @return Retorna atributo ingreso
	 */
	public Integer getIngreso()
	{
		return ingreso;
	}


	/**
	 * @param ingreso Asigna atributo ingreso
	 */
	public void setIngreso(Integer ingreso)
	{
		this.ingreso = ingreso;
	}


	public String getConsecutivoReciboCaja() {
		return consecutivoReciboCaja;
	}


	public void setConsecutivoReciboCaja(String consecutivoReciboCaja) {
		this.consecutivoReciboCaja = consecutivoReciboCaja;
	}
	
	public Integer obtenerIngresoPacientePorConsecutivoReciboCaja(String consecutivoReciboCaja) {
		return recibosDao.obtenerIngresoPacientePorConsecutivoReciboCaja(consecutivoReciboCaja);
	}
	
 }
