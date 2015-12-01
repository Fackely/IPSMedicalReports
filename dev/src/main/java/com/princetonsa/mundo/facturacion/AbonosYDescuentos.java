package com.princetonsa.mundo.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.AbonosYDescuentosDao;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.dto.tesoreria.DtoFiltroConsultaAbonos;
import com.princetonsa.dto.tesoreria.DtoMovmimientosAbonos;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Convenio;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * 
 * @author wilson
 *
 */
public class AbonosYDescuentos 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(AbonosYDescuentos.class);
	
	 /**
     * interaccion con la fuente de datos
     */
    private AbonosYDescuentosDao abonosDescuentosDao;
	
    /**
     * mapa que almacena los datos generales,
     * de la aplicacion de abonos y descuentos.
     */
    private HashMap mapaGeneral;
    
	/**
     * limpia e inicializa los atributos de la clase
     */
    public void reset ()
    {
    	this.mapaGeneral = new HashMap ();
    }
    
    /**
     * Constructor de la clase
     */
    public AbonosYDescuentos()
    {
        this.reset();
        this.init (System.getProperty("TIPOBD"));
    }
    
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
		    abonosDescuentosDao = myFactory.getAbonosYDescuentosDao();
			wasInited = (abonosDescuentosDao != null);
		}
		return wasInited;
	}

	/**
	 * metodo para cargar la informacion de los abonos en la generacion de la factura
	 * @param con
	 * @param facturasPropuestas
	 * @param paciente
	 */
	@SuppressWarnings("unchecked")
	public void cargarAbonosYDescuentos(Connection con, ArrayList<DtoFactura> facturasPropuestas, PersonaBasica paciente, int codigoInstitucion) throws IPSException
	{
		for(int w=0; w<facturasPropuestas.size(); w++)
		{
			DtoFactura dtoFactura= facturasPropuestas.get(w);
			
			//comenzamos a llenar los valores de los abonos
			
			this.setMapaGeneral("numRegistros", facturasPropuestas.size());
			this.setMapaGeneral("esGeneradoNetoFavor","false");
		    this.setMapaGeneral("esGeneradoNetoCargo","false");
		    
		    //INFORMACION DEL ENCABEZADO
			HashMap encabezadoMap= new HashMap();
			
			Integer idIngreso=null;
			
			boolean controlarAbonoPaciente=UtilidadTexto.getBoolean(ValoresPorDefecto.getControlarAbonoPacientePorNroIngreso(codigoInstitucion));
			if(controlarAbonoPaciente)
			{
				idIngreso=paciente.getCodigoIngreso();
			}
			encabezadoMap.put("abonoDisponible", abonosDescuentosDao.consultarAbonosDisponibles(con, paciente.getCodigoPersona(), idIngreso));
			//encabezadoMap.put("abonoDisponible",50000+"");
			encabezadoMap.put("numeroCuenta", dtoFactura.getCuentas().get(0).toString());
			encabezadoMap.put("viaIngreso", dtoFactura.getViaIngreso().getNombre()+"-"+UtilidadesManejoPaciente.obtenerTipoPacienteCuenta(con, dtoFactura.getCuentas().get(0).toString()).getNombre());
			int codigoTipoComplejidad=Cuenta.obtenerTipoComplejidad(con, dtoFactura.getCuentas().get(0).toString());
			logger.info("codigo tipo complejidad-->"+codigoTipoComplejidad);
			encabezadoMap.put("tipoComplejidad", Utilidades.obtenerNombreTipoComplejidad(con, codigoTipoComplejidad));
			String tipoEvento=Cuenta.obtenerCodigoTipoEventoCuenta(con, dtoFactura.getCuentas().get(0).toString());
			logger.info("tipoEvento->"+tipoEvento);
			encabezadoMap.put("tipoEvento", ValoresPorDefecto.getIntegridadDominio(tipoEvento));
			
			this.setMapaGeneral("encabezado", encabezadoMap);
			
			//FIN ENCABEZADO
		    
		    this.setMapaGeneral("idSubcuenta_"+w, dtoFactura.getSubCuenta()+"");
		    this.setMapaGeneral("codConvenio_"+w, dtoFactura.getConvenio().getCodigo()+"");	
			this.setMapaGeneral("nomConvenio_"+w, dtoFactura.getConvenio().getNombre());
			this.setMapaGeneral("regimen_"+w, dtoFactura.getTipoRegimenNombre());
			this.setMapaGeneral("clSocEco_"+w, dtoFactura.getEstratoSocial().getNombre());
			String carnet=Utilidades.obtenerNumeroCarnet(con, dtoFactura.getSubCuenta())+"";
			if(UtilidadTexto.isEmpty(carnet))	
				this.setMapaGeneral("carnet_"+w,"");
			else
				this.setMapaGeneral("carnet_"+w,carnet);
			this.setMapaGeneral("monto_"+w, dtoFactura.getTipoMonto().getNombre());
			
			InfoDatosInt naturalezaSubCuenta= ValidacionesFactura.obtenerNaturalezaPacienteSubCuenta(con, paciente.getCodigoIngreso()+"", dtoFactura.getConvenio().getCodigo() , ConstantesBD.acronimoNo/*facturado*/);
			if(dtoFactura.getPacienteTieneExcepcionNaturaleza())
			{
				this.setMapaGeneral("excNat_"+w, naturalezaSubCuenta.getNombre()+"");
				this.setMapaGeneral("excNatCodigo_"+w, naturalezaSubCuenta.getCodigo()+"");
			}
			else
			{
				this.setMapaGeneral("excNat_"+w, "");
				this.setMapaGeneral("excNatCodigo_"+w, ConstantesBD.codigoNuncaValido+"");
			}
			
			this.setMapaGeneral("boolFaltanRequisitosPaciente_"+w, UtilidadValidacion.faltanRequisitosPacienteXCuenta(con, String.valueOf((long)dtoFactura.getSubCuenta()))+"");
			this.setMapaGeneral("boolValorBrutoPacXTopeCalendario_"+w, dtoFactura.getValorBrutoPacienteModificadoXAnioCalendario()+"");
			this.setMapaGeneral("boolValorBrutoPacXTopeEvento_"+w, dtoFactura.getValorBrutoPacienteModificadoXEvento());
			this.setMapaGeneral("totalFacturar_"+w, UtilidadTexto.formatearValores(dtoFactura.getValorTotal()+"","#.##"));
			this.setMapaGeneral("vlrConvenio_"+w,UtilidadTexto.formatearValores(dtoFactura.getValorConvenio()+"","#.##"));
			if(dtoFactura.getValorAFavorConvenio()>=0)
				this.setMapaGeneral("vlrFavorConvenio_"+w, UtilidadTexto.formatearValores(dtoFactura.getValorAFavorConvenio()+"","#.##"));
			else
				this.setMapaGeneral("vlrFavorConvenio_"+w,"0");
					
			this.setMapaGeneral("vlrLiquidadoPaciente_"+w,UtilidadTexto.formatearValores(dtoFactura.getValorBrutoPacSinModParamConvenio()+"","#.##"));
			
			this.setMapaGeneral("vlrPaciente_"+w,UtilidadTexto.formatearValores(dtoFactura.getValorBrutoPac()+"","#.##"));
			this.setMapaGeneral("vlrPacienteOld_"+w,UtilidadTexto.formatearValores(dtoFactura.getValorBrutoPac()+"","#.##"));
			this.setMapaGeneral("vlrFaltaAsignarVenezuela_"+w, UtilidadTexto.formatearValores(dtoFactura.getValorFaltaAsignarVenezuela()));
			this.setMapaGeneral("vlrFaltaAsignarVenezuelaDouble_"+w, dtoFactura.getValorFaltaAsignarVenezuela());	
			
			this.setMapaGeneral("netoPaciente_"+w,this.getMapaGeneral("vlrPaciente_"+w));
			this.setMapaGeneral("esParticular_"+w, dtoFactura.getEsParticular());
			
			//Evalua el indicador de Asignar en la factura como valor del paciente el valor de abono 
			Convenio convenio = new Convenio();
			convenio.cargarResumen(con,dtoFactura.getConvenio().getCodigo());
			
			//---------------------------------------------------
			//Cambio requerimiento Anexo 661. a.2			
			
			this.setMapaGeneral("indicadorAsigFacValPacValAbo_"+w,ConstantesBD.acronimoNo);
			if(!convenio.getTipoRegimen().equals(ConstantesBD.codigoTipoRegimenParticular) && 
					convenio.getAsignarFactValorPacValorAbono().equals(ConstantesBD.acronimoSi))
			{
				//Tarea id=59512
				//this.setMapaGeneral("vlrPaciente_"+w,"0");
				this.setMapaGeneral("indicadorAsigFacValPacValAbo_"+w,ConstantesBD.acronimoSi);
			}
			//---------------------------------------------------				
			
			this.setMapaGeneral("abonoAplicado_"+w, "0");
			
		}
	}
	
	  /**
	  * Metodo para cargar el Log con los datos actuales, 
	  * y despues de la modificación, del valor del paciente.
	  * @param usuario
	  * @param session
	  */
	 public static void generarLogHistorial(UsuarioBasico usuario, HashMap mapaGeneral)
	 {
	     int numReg=0;
	     boolean esModificado=false;
	     numReg = Integer.parseInt(mapaGeneral.get("numRegistros")+"");
	     String log="";
	     for(int w=0; w<numReg; w++)
	     {	         
	         if(!(mapaGeneral.get("vlrPaciente_"+w)+"").equals((mapaGeneral.get("vlrPacienteOld_"+w)+"")) )
	         {
	             esModificado = true;
	             log += 	"\n			  =====INFORMACION ORIGINAL===== " +
	                     		"\n*  Número Cuenta ["+mapaGeneral.get("numeroCuenta")+""+"] " +
	                     		"\n*  Via Ingreso  ["+mapaGeneral.get("viaIngreso")+""+"] "+	            	 			 			
	                     		"\n*  Valor Paciente  ["+mapaGeneral.get("vlrPacienteOld_"+w)+""+"] "+
	             				"\n\n"+
	             				"\n            ====INFORMACION DESPUES DE LA MODIFICACION===== "+
	             				"\n*  Valor Paciente  ["+mapaGeneral.get("vlrPaciente_"+w)+""+"] "+"\n\n";   
	         }
	     }
	     if(esModificado);
	     	LogsAxioma.enviarLog(ConstantesBD.logAbonosYDescuentosCodigo,log,ConstantesBD.tipoRegistroLogModificacion,usuario.getLoginUsuario());
	 }     
	
	/**
	 * Método para insertar un movimiento de abonos
	 * @param con
	 * @param codigoPaciente
	 * @param codigoDocumento
	 * @param tipoDocumento
	 * @param valor
	 * @param institucion
	 * @param ingreso
	 * @return
	 */
	public static int insertarMovimientoAbonos(	Connection con,int codigoPaciente,int codigoDocumento,int tipoDocumento,double valor,int institucion, Integer ingreso, int codigoCentroAtencion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAbonosYDescuentosDao().insertarMovimientoAbonos(con,codigoPaciente,codigoDocumento,tipoDocumento,valor,institucion, ingreso, codigoCentroAtencion);
	} 
	

	/**
	 * Método para insertar transaccionalmente un clon de un insert de mov de abonos, diferenciado por el tipo
	 * @param con
	 * @param tipoMovimiento
	 * @param codigoMovimiento
	 * @param usuario 
	 * @param ingreso
	 * @param estado
	 * @return
	 * @throws SQLException
	 */	
	public static int insertarClonMovimientoAbonosDadoCodigo (	Connection con, int tipoMovimiento,int codigoMovimiento, int codigoCentroAtencion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAbonosYDescuentosDao().insertarClonMovimientoAbonosDadoCodigo(con, codigoMovimiento, tipoMovimiento, codigoCentroAtencion);
	}
	

	/**
	 * Carga el codigo de la tabla movimientos_abonos dado el codigo de la factura y el tipo de mov
	 * @param con
	 * @param tipoMovAbonos
	 * @param codigoFactura
	 * @return
	 */
	public static int cargarCodigoPKMovimientosAbono(Connection con, int tipoMovAbonos, int codigoFactura)
	{
	    return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAbonosYDescuentosDao().cargarCodigoPKMovimientosAbono(con, tipoMovAbonos, codigoFactura);
	}
	
	/**
	 * Método para consultar los abonos disponibles del paciente
	 * @param codigoPaciente
	 * @param ingreso
	 * @param codigoInstitucion
	 * @return
	 */
	public static double consultarAbonosDisponibles(int codigoPaciente, Integer ingreso, int codigoInstitucion)
	{
		Integer idIngreso=null;
		Connection con = UtilidadBD.abrirConexion();
		if(ingreso!=null)
		{
			boolean controlarAbonoPaciente=UtilidadTexto.getBoolean(ValoresPorDefecto.getControlarAbonoPacientePorNroIngreso(codigoInstitucion));
			if(controlarAbonoPaciente)
			{
				idIngreso=ingreso;
			}
		}
		double retorna= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAbonosYDescuentosDao().consultarAbonosDisponibles(con, codigoPaciente, idIngreso);
		UtilidadBD.closeConnection(con);
		return retorna;
	}
	
	/**
	 * Método para consultar los abonos disponibles del paciente
	 * @param con
	 * @param codigoPaciente
	 * @param ingreso
	 * @return
	 */
	public static double consultarAbonosDisponibles(Connection con,int codigoPaciente, Integer ingreso)
	{
		double retorna= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAbonosYDescuentosDao().consultarAbonosDisponibles(con, codigoPaciente, ingreso);
		
		return retorna;
	}
	
	/**
	 * Método implementado para eliminar los abonos de un documento especifico
	 * @param con
	 * @param codigoMovimientoAbono
	 * @param tipoMovimientoAbono
	 * @return
	 */
	public static ResultadoBoolean eliminarAbonos(Connection con,BigDecimal codigoMovimientoAbono,int tipoMovimientoAbono)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAbonosYDescuentosDao().eliminarAbonos(con, codigoMovimientoAbono, tipoMovimientoAbono);
	}
	
	public static BigDecimal obtenerValorReservadoAbono(int codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAbonosYDescuentosDao().obtenerValorReservadoAbono(codigoPaciente);
	}
	
	/**
     * @return Retorna mapaGeneral.
     */
    public HashMap getMapaGeneral() {
        return mapaGeneral;
    }
    /**
     * @param mapaGeneral Asigna mapaGeneral.
     */
    public void setMapaGeneral(HashMap mapaGeneral) {
        this.mapaGeneral = mapaGeneral;
    }
    
    /**
     * retorna el valor del objeto, segun la llave.
     * @param key, String con la llave.
     * @return object, Con el dato que contiene la llave.
     */
    public Object getMapaGeneral(String key) {
        return mapaGeneral.get(key);
    }
    /**
     * asigna un dato, a la llave correspondiente en el 
     * mapa.
     * @param key, String llave del objeto.
     * @param value, Object dato para adicionar.
     */
    public void setMapaGeneral(String key, Object value) {
        this.mapaGeneral.put(key,value);
    }   
	

    /**
     * 
     * @param codigoPersona
     * @return
     */
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosRecibosCaja(int codigoPersona) 
	{
		return abonosDescuentosDao.obtenerAbonosRecibosCaja(codigoPersona);
	}

	/**
	 * 
	 * @param codigoPersona
	 * @return
	 */
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosAplicadosFacturas(int codigoPersona) 
	{
		return abonosDescuentosDao.obtenerAbonosAplicadosFacturas(codigoPersona);
	}

	/**
	 * 
	 * @param codigoPersona
	 * @return
	 */
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosDevolucionRC(int codigoPersona) 
	{
		return abonosDescuentosDao.obtenerAbonosDevolucionRC(codigoPersona);
	}   
	
	/**
	 * Lista todos los abonos de los pacientes
	 * @param codigoPersona
	 * @return {@link ArrayList}<{@link DtoMovmimientosAbonos}> Lista con los elementos retornados
	 * @author Juan David Ram&iacute;rez L&oacute;pez
	 */
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosGeneral(int codigoPersona)
	{
		return abonosDescuentosDao.obtenerAbonosGeneral(codigoPersona);
	}

	/**
	 * Lista los movimientos de los abonos relacionados a las devoluciones de los recibos de caja
	 * @param codigoPersona
	 * @return {@link ArrayList}<{@link DtoMovmimientosAbonos}> Lista con los elementos retornados
	 * @author Juan David Ram&iacute;rez L&oacute;pez
	 */
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosAnulacionesRecibosCaja(int codigoPersona)
	{
		return abonosDescuentosDao.obtenerAbonosAnulaciones(codigoPersona);
	}

	/**
	 * 
	 * @param codigoPersona
	 * @param dtoFiltro
	 * @return
	 */
	public ArrayList<DtoMovmimientosAbonos> obtenerAbonosGeneralAvanzada(
			int codigoPersona, DtoFiltroConsultaAbonos dtoFiltro) {
		return abonosDescuentosDao.obtenerAbonosGeneralAvanzada(codigoPersona,dtoFiltro);
	}

	
}
