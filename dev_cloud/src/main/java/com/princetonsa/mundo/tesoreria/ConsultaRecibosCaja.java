/*
 * Created on 5/10/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.mundo.tesoreria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;

import com.princetonsa.dao.ConsultaReciboCajaDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.SqlBaseConsultaRecibosCajaDao;
import com.princetonsa.dto.tesoreria.DtoDevolRecibosCaja;
 
/**
 * @version 1.0, 5/10/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class ConsultaRecibosCaja
{
    /**
     * Constructor de la Clase
     */
    public ConsultaRecibosCaja()
    {
        this.reset();
        this.init(System.getProperty("TIPOBD"));
    }
    /**
	 * DAO de este objeto, para trabajar con ConsultaReciboCaja
	 * en la fuente de datos
	 */    
   private static ConsultaReciboCajaDao consultaRecibosCajaDao;
   
   /**
    * Variable para manejar la istitucion del usuario que se encuentra en el sisetma
    */
   private int institucion;
   
   /**
    * Para menjar el rango inicial del numero de ricibos de caja a consultar
    */
   private String reciboCajaInicial;
   /**
    * Para manejar el rango final de numero de recibos de caja a consultar
    */
   private String reciboCajaFinal;
   /**
    * Para manejar la fecha inicial del rango por el que se buscan los recibos de caja.
    */
   private String fechaReciboCajaInicial;
   /**
    * Para manejar la fecha final del rango por el que se buscan los recibos de caja.
    */
   private String fechaReciboCajaFina;
   
   /**
    * código del concepto del recibo de caja para hacer la busqueda
    */
   private String codigoConceptoReciboCaja;
   
   /**
    * Codigo del estado del recibo de caja
    */
   private int estadoReciboCaja;
   
   /**
    * Login del usuario que elaboro el recibo de caja.
    */
   private String usuarioElaboraReciboCaja;
   
   /**
    * Codigo de la caja que elaboro el recibo de caja
    */
   private int cajaElaboraReciboCaja;
   
   /**
    * centro atencion
    */
   private int codigoCentroAtencion;
   
   /**
    * Mapa que contiene todos los recibos de caja que se obtuvieron en la consulta.
    */
   private HashMap recibosCaja;
   
   /**
    * Variable que inidica si la busqueda se debe realiza por rango de recibos de caja.
    */
   private boolean buscarPorRecibosCajas;
   
   /**
    * Variable que inidica si la busqueda se debe realizar por concepto.
    */
   private boolean buscarPorConcepto;
   /**
    * Variable que inidica si la busqueda se debe realizar por el estado
    */
   private boolean buscarPorEstados;
   /**
    * Variable que indica si la busqueda se debe realizar por usuario
    */
   private boolean buscarPorUsuario;
   /**
    * Variable que indica si la busqueda se debe realiza por caja
    */
   private boolean buscarPorCaja;
   /**
    * 
    */
   private boolean buscarPorFormaPago;
   
   
   /**
    * Mapa para manejar los conceptos del ReciboCaja
    */
   private HashMap conceptosRC;
   
   /**
    * Mapa para manejar las Formas de Pago
    */
   private HashMap formasPagoRC;
   

	/**
	 * Mapa para manejar la informacion de la Anulacion
	 */
   private HashMap anulacionRC;
   
   /**
    * 
    * 
    */
   private int codigoFormaPago;
   
   /**
    * documento soporte
    */
   private String docSoporte;
   
   /**
    * tipoIdentificacion
    */
   private String tipoIdBeneficiario;
   
   /**
    * 
    */
   private String numeroIdBeneficiario;
   
   
	/**
     * Método que limpia este objeto
     */
    public void reset()
    {
        this.reciboCajaInicial="";
        this.reciboCajaFinal="";
        this.fechaReciboCajaInicial="";
        this.fechaReciboCajaFina="";
        this.codigoConceptoReciboCaja="";
        this.estadoReciboCaja=ConstantesBD.codigoNuncaValido;
        this.usuarioElaboraReciboCaja="";
        this.cajaElaboraReciboCaja=ConstantesBD.codigoNuncaValido;
        this.codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
        this.codigoFormaPago=ConstantesBD.codigoNuncaValido;
        this.recibosCaja=new HashMap();
        this.buscarPorRecibosCajas=false;
        this.buscarPorConcepto=false;
        this.buscarPorEstados=false;
        this.buscarPorUsuario=false;
        this.buscarPorCaja=false;
        this.buscarPorFormaPago=false;
        this.conceptosRC=new HashMap();
        this.formasPagoRC=new HashMap();
        this.anulacionRC=new HashMap();
        this.docSoporte="";
        this.tipoIdBeneficiario="";
        this.numeroIdBeneficiario="";
    }
     
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( consultaRecibosCajaDao== null ) 
		{ 
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			consultaRecibosCajaDao= myFactory.getConsultaReciboCajaDao();
			if( consultaRecibosCajaDao!= null )
				return true;
		}
		return false;
	}
	
	/**
	 * obtiene el codigo - nombre del centro de atencion de un recibo de caja
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 */
	public static InfoDatosInt getCentroAtencionReciboCaja( String numeroReciboCaja, int codigoInstitucion)
	{
		Connection con= UtilidadBD.abrirConexion();
		InfoDatosInt info=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaReciboCajaDao().getCentroAtencionReciboCaja(con, numeroReciboCaja, codigoInstitucion);
		UtilidadBD.closeConnection(con);
		return info;
	}
	
	
	/**
	 * @return Returns the cajaElaboraReciboCaja.
	 */
	public int getCajaElaboraReciboCaja()
	{
	    return cajaElaboraReciboCaja;
	}
	/**
	 * @param cajaElaboraReciboCaja The cajaElaboraReciboCaja to set.
	 */
	public void setCajaElaboraReciboCaja(int cajaElaboraReciboCaja)
	{
	    this.cajaElaboraReciboCaja = cajaElaboraReciboCaja;
	}
	/**
	 * @return Returns the codigoConceptoReciboCaja.
	 */
	public String getCodigoConceptoReciboCaja()
	{
	    return codigoConceptoReciboCaja;
	}
	/**
	 * @param codigoConceptoReciboCaja The codigoConceptoReciboCaja to set.
	 */
	public void setCodigoConceptoReciboCaja(String codigoConceptoReciboCaja)
	{
	    this.codigoConceptoReciboCaja = codigoConceptoReciboCaja;
	}
	/**
	 * @return Returns the estadoReciboCaja.
	 */
	public int getEstadoReciboCaja()
	{
	    return estadoReciboCaja;
	}
	/**
	 * @param estadoReciboCaja The estadoReciboCaja to set.
	 */
	public void setEstadoReciboCaja(int estadoReciboCaja)
	{
	    this.estadoReciboCaja = estadoReciboCaja;
	}
	/**
	 * @return Returns the fechaReciboCajaFina.
	 */
	public String getFechaReciboCajaFina()
	{
	    return fechaReciboCajaFina;
	}
	/**
	 * @param fechaReciboCajaFina The fechaReciboCajaFina to set.
	 */
	public void setFechaReciboCajaFina(String fechaReciboCajaFina)
	{
	    this.fechaReciboCajaFina = fechaReciboCajaFina;
	}
	/**
	 * @return Returns the fechaReciboCajaInicial.
	 */
	public String getFechaReciboCajaInicial()
	{
	    return fechaReciboCajaInicial;
	}
	/**
	 * @param fechaReciboCajaInicial The fechaReciboCajaInicial to set.
	 */
	public void setFechaReciboCajaInicial(String fechaReciboCajaInicial)
	{
	    this.fechaReciboCajaInicial = fechaReciboCajaInicial;
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
	    this.institucion = institucion;
	}
	/**
	 * @return Returns the reciboCajaFinal.
	 */
	public String getReciboCajaFinal()
	{
	    return reciboCajaFinal;
	}
	/**
	 * @param reciboCajaFinal The reciboCajaFinal to set.
	 */
	public void setReciboCajaFinal(String reciboCajaFinal)
	{
	    this.reciboCajaFinal = reciboCajaFinal;
	}
	/**
	 * @return Returns the reciboCajaInicial.
	 */
	public String getReciboCajaInicial()
	{
	    return reciboCajaInicial;
	}
	/**
	 * @param reciboCajaInicial The reciboCajaInicial to set.
	 */
	public void setReciboCajaInicial(String reciboCajaInicial)
	{
	    this.reciboCajaInicial = reciboCajaInicial;
	}
	/**
	 * @return Returns the recibosCaja.
	 */
	public HashMap getRecibosCaja()
	{
	    return recibosCaja;
	}
	/**
	 * @param recibosCaja The recibosCaja to set.
	 */
	public void setRecibosCaja(HashMap recibosCaja)
	{
	    this.recibosCaja = recibosCaja;
	}
	/**
	 * @return Returns the usuarioElaboraReciboCaja.
	 */
	public String getUsuarioElaboraReciboCaja()
	{
	    return usuarioElaboraReciboCaja;
	}
	/**
	 * @param usuarioElaboraReciboCaja The usuarioElaboraReciboCaja to set.
	 */
	public void setUsuarioElaboraReciboCaja(String usuarioElaboraReciboCaja)
	{
	    this.usuarioElaboraReciboCaja = usuarioElaboraReciboCaja;
	}

    
    /**
     * 
     */
    public void ejecutarBusquedaAvanzada()
    {
       if(!this.reciboCajaInicial.trim().equals("")&&!this.reciboCajaFinal.trim().equals(""))
       {
           this.buscarPorRecibosCajas=true;
       }
       if(!this.codigoConceptoReciboCaja.trim().equals("")&&!this.codigoConceptoReciboCaja.trim().equals("-1"))
       {
           this.buscarPorConcepto=true;
       }
       if(estadoReciboCaja>0)
       {
           this.buscarPorEstados=true;
       }
       if(!this.usuarioElaboraReciboCaja.trim().equals("")&&!this.usuarioElaboraReciboCaja.trim().equals("-1"))
       {
           this.buscarPorUsuario=true;
       }
       if(this.cajaElaboraReciboCaja>0)
       {
           this.buscarPorCaja=true;
       }
       if(this.codigoFormaPago>0)
       {
    	   this.buscarPorFormaPago=true;
       }
       this.recibosCaja=consultaRecibosCajaDao.busquedaAvanzada(this.fechaReciboCajaInicial,this.fechaReciboCajaFina,this.institucion,
               													this.reciboCajaInicial,this.reciboCajaFinal,this.buscarPorRecibosCajas,this.codigoConceptoReciboCaja,
               													this.buscarPorConcepto,this.estadoReciboCaja,this.buscarPorEstados,this.usuarioElaboraReciboCaja,
               													this.buscarPorUsuario,this.cajaElaboraReciboCaja,this.buscarPorCaja,this.codigoFormaPago,this.buscarPorFormaPago, this.getCodigoCentroAtencion(),
               													this.getDocSoporte(), this.getTipoIdBeneficiario(), this.getNumeroIdBeneficiario());
    }

    
    /**
     * @param institucion
     * @param numeroReciboCaja
     */
    public void consultarConceptosReciboCaja(int institucion, String numeroReciboCaja)
    {
        this.conceptosRC=consultaRecibosCajaDao.consultarConceptosReciboCaja(institucion,numeroReciboCaja);
    }
	
	/**
	 * @param institucion2
	 * @param numeroReciboCaja
	 */
	public void consultarFormasPagoReciboCaja(int institucion, String numeroReciboCaja)
	{
	   this.formasPagoRC=consultaRecibosCajaDao.consultarFormasPagoReciboCaja(institucion,numeroReciboCaja);
	}
	/**
	 * @return Returns the conceptosRC.
	 */
	public HashMap getConceptosRC()
	{
	    return conceptosRC;
	}
	/**
	 * @param conceptosRC The conceptosRC to set.
	 */
	public void setConceptosRC(HashMap conceptosRC)
	{
	    this.conceptosRC = conceptosRC;
	}	
	/**
	 * @return Returns the formasPagoRC.
	 */
	public HashMap getFormasPagoRC()
	{
	    return formasPagoRC;
	}
	/**
	 * @param formasPagoRC The formasPagoRC to set.
	 */
	public void setFormasPagoRC(HashMap formasPagoRC)
	{
	    this.formasPagoRC = formasPagoRC;
	}

    
    /**
     * @param institucion
     * @param numeroReciboCaja
     */
    public void consultarAnulacionReciboCaja(int institucion, String numeroReciboCaja)
    {
        this.anulacionRC=consultaRecibosCajaDao.consultarAnulacionReciboCaja(institucion,numeroReciboCaja);
    }
    /**
     * @return Returns the anulacionRC.
     */
    public HashMap getAnulacionRC()
    {
        return anulacionRC;
    }
    /**
     * @param anulacionRC The anulacionRC to set.
     */
    public void setAnulacionRC(HashMap anulacionRC)
    {
        this.anulacionRC = anulacionRC;
    }

    
    /**
     * Metodo que realiza la consulta en la fuente de datos, del total de pagos
     * en efectivo, tarjetas Credito, tarjetas Debito y cheques. para un recibo de
     * caja específico.
     * @return
     */
    public HashMap consultarTotalesPagos(int institucion, String numeroReciboCaja)
    {
        return consultaRecibosCajaDao.consultarTotalesPagos(institucion,numeroReciboCaja);
    }
    
    /**
     * metodo para generar la busqueda avanzada
     * de consulta anulaciones recibos de caja
     * @param con Connection
     * @param vo HashMap
     * @return HashMap 
     * @author jarloc
     */
    public HashMap ejecutarBusquedaAvanzadaAnulacionesRC(Connection con, HashMap vo)
    {
        return consultaRecibosCajaDao.ejecutarBusquedaAvanzadaAnulacionesRC(con, vo);
    }
    
    /**
     * Consulta los recibos de caja para utilizarlos en las funcionalidades de cierre caja y arqueos
     * @param con
     * @param codigoInstitucion, codigoInstitucion
     * @param fechaDDMMYYYY, aca va la fecha generacion del arqueo o gecha generacion cierre
     * @param loginUsuarioCajero, login del usuario/cajero al que se va ha realizar  el arqueo o cierre 
     * @param consecutivoCaja, caja a la cual se le esta realizando el cierre o arqueo 
     * @param restriccionEstadosSeparadosXComas, restricciones por los estados eje 1,4 recaudado-anulado
     * @param loginUsuarioGenera
     * @param fechaGeneracionConsulta
     * @param horaGeneracionConsulta
     * @param bloquearRegistro, LE COLOCA UN SELECT FOR UPDATE EN CASO DE QUE VENGA EN TRUE, TENGA 
     * EN CUENTA QUE DEBE VENIR CONNECTION EN UNA TRANSACCION CUANDO bloquearRegistro=TRUE
     * @param consecutivoArqueoOCierre. en caso de que no sea vacio "" entonces busca por el consecutivo arqueo
     * @param codigoTipoArqueo
     * @return
     */
	public HashMap consultaRecibosCaja(	Connection con, 
										int codigoInstitucion, 
										String fechaDDMMYYYY, 
										String loginUsuarioCajero, 
										String consecutivoCaja, 
										String restriccionEstadosSeparadosXComas,
										String loginUsuarioGenera,
										String fechaGeneracionConsulta,
										String horaGeneracionConsulta,
										boolean bloquearRegistro,
										String consecutivoArqueoOCierre,
										int codigoTipoArqueo,
										String consecutivoCajaPpal)
	{
		return consultaRecibosCajaDao.consultaRecibosCaja(con, codigoInstitucion, fechaDDMMYYYY, loginUsuarioCajero, consecutivoCaja, restriccionEstadosSeparadosXComas, loginUsuarioGenera, fechaGeneracionConsulta, horaGeneracionConsulta, bloquearRegistro, consecutivoArqueoOCierre, codigoTipoArqueo, consecutivoCajaPpal);
	}

	public int getCodigoFormaPago() {
		return codigoFormaPago;
	}

	public void setCodigoFormaPago(int codigoFormaPago) {
		this.codigoFormaPago = codigoFormaPago;
	}

	/**
	 * @return Returns the codigoCentroAtencion.
	 */
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion The codigoCentroAtencion to set.
	 */
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	/**
	 * @return Returns the docSoporte.
	 */
	public String getDocSoporte()
	{
		return docSoporte;
	}

	/**
	 * @param docSoporte The docSoporte to set.
	 */
	public void setDocSoporte(String docSoporte)
	{
		this.docSoporte = docSoporte;
	}

	/**
	 * @return Returns the numeroIdBeneficiario.
	 */
	public String getNumeroIdBeneficiario()
	{
		return numeroIdBeneficiario;
	}

	/**
	 * @param numeroIdBeneficiario The numeroIdBeneficiario to set.
	 */
	public void setNumeroIdBeneficiario(String numeroIdBeneficiario)
	{
		this.numeroIdBeneficiario = numeroIdBeneficiario;
	}

	/**
	 * @return Returns the tipoIdBeneficiario.
	 */
	public String getTipoIdBeneficiario()
	{
		return tipoIdBeneficiario;
	}

	/**
	 * @param tipoIdBeneficiario The tipoIdBeneficiario to set.
	 */
	public void setTipoIdBeneficiario(String tipoIdBeneficiario)
	{
		this.tipoIdBeneficiario = tipoIdBeneficiario;
	}
	
	/**
	 * 
	 */
	public ArrayList<DtoDevolRecibosCaja> consultarDevolucionesAprobadas(int institucion, String numeroReciboCaja)
	{
		return consultaRecibosCajaDao.consultarDevolucionesAprobadas(institucion, numeroReciboCaja);
	}
}
