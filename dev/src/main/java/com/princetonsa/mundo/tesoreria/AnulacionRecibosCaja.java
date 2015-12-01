/*
 * Created on 30/09/2005
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dao.AnulacionRecibosCajaDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.odontologia.DtoBeneficiarioCliente;
import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.BeneficiariosTarjetaCliente;
import com.princetonsa.mundo.odontologia.VentasTarjetasCliente;

/**
 * @version 1.0, 30/09/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class AnulacionRecibosCaja
{

    /**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(AnulacionRecibosCaja.class);
	
	/**
	 * DAO de este objeto, para trabajar con AnulacionRecibosCaja
	 * en la fuente de datos
	 */    
    private static AnulacionRecibosCajaDao anulacionRecibosCajaDao;
    
	/**
	 * Variable que hace referencia al consecutivo obtenido de la tabla consecutivos asignado para la anulacion
	 */
	private String numeroAnulacionReciboCaja;
	
	/**
	 * 
	 */
	private String consecutivoAnulacionReciboCaja;
	
	/**
	 * Variable para manejar el numero del recibo de caja que se desea anular.
	 */
	private String numeroReciboCaja;
	
	/**
	 * Variable para manejar la fecha del Recibo de Caja
	 */
	private String fechaReciboCaja;
	
	/**
	 * codigoCentroAtencion
	 */
	private int codigoCentroAtencion;
	
	/**
	 * nombreCentroAtencion
	 */
	private String nombreCentroAtencion;
	
	/**
	 * Variable para manejar la hora del recibo de caja.
	 */
	private String horaReciboCaja;
	
	/**
	 * Consecutivo de la caja de Recibo
	 */
	private int consecutivoCaja;
	
	/**
	 * Codigo de la caja de recibo(Es el codigo visible para el usuario)
	 */
	private int codigoCaja;
	
	/**
	 * Variable para manejar la descripcion de la Caja.
	 */
	private String descripcionCaja;
	
	/**
	 * Variable para manejar el login de usuario asignado a la caja del RC.
	 */
	private String loginUsuarioCaja;
	
	/**
	 * Variable para manejar el nombre del usuario relacionado a la caja del RC.
	 */
	private String nombreUsuarioCaja;
	
	/**
	 * Campo para manejar el valor que tiene este campo en la tabla RC.
	 */
	private String recibidoDe;
	
	/**
	 * Mapa para manejar los conceptos del RC 
	 */
	@SuppressWarnings("rawtypes")
	private HashMap conceptosRC;
	
	/**
	 * Variable para tener el total del recibo de caja (Suma del valor conceptos.)
	 */
	private double totalRecibidoCaja;
	
	/**
	 * Variable para manejar el motivo por el cual se anula el RC.
	 */
	private int motivo;
	
	/**
	 * Variable para manejar las observaciones del RC.
	 */
	private String Observaciones;
	
	/**
	 * Variable para manejar la institucion.
	 */
	private int institucion;
	
	/**
	 * Ususario que realiza la anulacion
	 */
	private String usuarioAnulacion;
	
	/**
	 * Fecha en que se realiza la anulacion
	 */
	private String fechaAnulacion;
	
	/**
	 * Hora en que se realiza la anulacion
	 */
	private String horaAnulacion;
	
	/**
	 * atributo que determian si se puede eliminar el recibo de caja
	 */
	private String eliminarRC;
	
	/**
	 * Atributo que contiene el valor del Anticipo de Recibido del Convenio  
	 */
	private double valorAntRecConvenio;
	
	/**
	 * N&uacute;mero del ingreso de recibo de caja
	 */
	private Integer ingresoReciboCaja;
	
	/**
     * Método que limpia este objeto
     */
    @SuppressWarnings("rawtypes")
	public void reset()
    {
        this.numeroAnulacionReciboCaja="";
        this.consecutivoAnulacionReciboCaja="";
        this.numeroReciboCaja="";
        this.fechaReciboCaja="";
        this.codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
        this.nombreCentroAtencion="";
        this.horaReciboCaja="";
        this.consecutivoCaja=ConstantesBD.codigoNuncaValido;
        this.codigoCaja=ConstantesBD.codigoNuncaValido;
        this.descripcionCaja="";
        this.loginUsuarioCaja="";
        this.nombreUsuarioCaja="";
        this.recibidoDe="";
        this.conceptosRC=new HashMap();
        this.totalRecibidoCaja=ConstantesBD.codigoNuncaValido;
        this.motivo=ConstantesBD.codigoNuncaValido;
        this.Observaciones="";
        this.institucion=ConstantesBD.codigoNuncaValido;
        this.usuarioAnulacion="";
        this.fechaAnulacion="";
        this.horaAnulacion="";
        this.eliminarRC = ConstantesBD.acronimoNo;
        this.valorAntRecConvenio=ConstantesBD.codigoNuncaValidoDouble;
        
    }
     
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( anulacionRecibosCajaDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			anulacionRecibosCajaDao= myFactory.getAnulacionRecibosCajaDao();
			if( anulacionRecibosCajaDao!= null )
				return true;
		}
		return false;
	}
	
	/**
	 * Constructor de la Clase
	 */
	public AnulacionRecibosCaja()
    {
		this.reset();
		init(System.getProperty("TIPOBD"));
	}
    /**
     * @return Returns the codigoCaja.
     */
    public int getCodigoCaja()
    {
        return codigoCaja;
    }
    /**
     * @param codigoCaja The codigoCaja to set.
     */
    public void setCodigoCaja(int codigoCaja)
    {
        this.codigoCaja = codigoCaja;
    }
    /**
     * @return Returns the conceptosRC.
     */
    @SuppressWarnings("rawtypes")
	public HashMap getConceptosRC()
    {
        return conceptosRC;
    }
    /**
     * @param conceptosRC The conceptosRC to set.
     */
    @SuppressWarnings("rawtypes")
	public void setConceptosRC(HashMap conceptosRC)
    {
        this.conceptosRC = conceptosRC;
    }
    /**
     * @return Returns the consecutivoCaja.
     */
    public int getConsecutivoCaja()
    {
        return consecutivoCaja;
    }
    /**
     * @param consecutivoCaja The consecutivoCaja to set.
     */
    public void setConsecutivoCaja(int consecutivoCaja)
    {
        this.consecutivoCaja = consecutivoCaja;
    }
    /**
     * @return Returns the descripcionCaja.
     */
    public String getDescripcionCaja()
    {
        return descripcionCaja;
    }
    /**
     * @param descripcionCaja The descripcionCaja to set.
     */
    public void setDescripcionCaja(String descripcionCaja)
    {
        this.descripcionCaja = descripcionCaja;
    }
    /**
     * @return Returns the fechaReciboCaja.
     */
    public String getFechaReciboCaja()
    {
        return fechaReciboCaja;
    }
    /**
     * @param fechaReciboCaja The fechaReciboCaja to set.
     */
    public void setFechaReciboCaja(String fechaReciboCaja)
    {
        this.fechaReciboCaja = fechaReciboCaja;
    }
    /**
     * @return Returns the horaReciboCaja.
     */
    public String getHoraReciboCaja()
    {
        return horaReciboCaja;
    }
    /**
     * @param horaReciboCaja The horaReciboCaja to set.
     */
    public void setHoraReciboCaja(String horaReciboCaja)
    {
        this.horaReciboCaja = horaReciboCaja;
    }
    /**
     * @return Returns the loginUsuarioCaja.
     */
    public String getLoginUsuarioCaja()
    {
        return loginUsuarioCaja;
    }
    /**
     * @param loginUsuarioCaja The loginUsuarioCaja to set.
     */
    public void setLoginUsuarioCaja(String loginUsuarioCaja)
    {
        this.loginUsuarioCaja = loginUsuarioCaja;
    }
    /**
     * @return Returns the motivo.
     */
    public int getMotivo()
    {
        return motivo;
    }
    /**
     * @param motivo The motivo to set.
     */
    public void setMotivo(int motivo)
    {
        this.motivo = motivo;
    }
    /**
     * @return Returns the nombreUsuarioCaja.
     */
    public String getNombreUsuarioCaja()
    {
        return nombreUsuarioCaja;
    }
    /**
     * @param nombreUsuarioCaja The nombreUsuarioCaja to set.
     */
    public void setNombreUsuarioCaja(String nombreUsuarioCaja)
    {
        this.nombreUsuarioCaja = nombreUsuarioCaja;
    }
    /**
     * @return Returns the numeroAnulacionReciboCaja.
     */
    public String getNumeroAnulacionReciboCaja()
    {
        return numeroAnulacionReciboCaja;
    }
    /**
     * @param numeroAnulacionReciboCaja The numeroAnulacionReciboCaja to set.
     */
    public void setNumeroAnulacionReciboCaja(String numeroAnulacionReciboCaja)
    {
        this.numeroAnulacionReciboCaja = numeroAnulacionReciboCaja;
    }
    /**
     * @return Returns the numeroReciboCaja.
     */
    public String getNumeroReciboCaja()
    {
        return numeroReciboCaja;
    }
    /**
     * @param numeroReciboCaja The numeroReciboCaja to set.
     */
    public void setNumeroReciboCaja(String numeroReciboCaja)
    {
        this.numeroReciboCaja = numeroReciboCaja;
    }
    /**
     * @return Returns the observaciones.
     */
    public String getObservaciones()
    {
        return Observaciones;
    }
    /**
     * @param observaciones The observaciones to set.
     */
    public void setObservaciones(String observaciones)
    {
        Observaciones = observaciones;
    }
    /**
     * @return Returns the recibidoDe.
     */
    public String getRecibidoDe()
    {
        return recibidoDe;
    }
    /**
     * @param recibidoDe The recibidoDe to set.
     */
    public void setRecibidoDe(String recibidoDe)
    {
        this.recibidoDe = recibidoDe;
    }
    /**
     * @return Returns the totalRecibidoCaja.
     */
    public double getTotalRecibidoCaja()
    {
        return totalRecibidoCaja;
    }
    /**
     * @param totalRecibidoCaja The totalRecibidoCaja to set.
     */
    public void setTotalRecibidoCaja(double totalRecibidoCaja)
    {
        this.totalRecibidoCaja = totalRecibidoCaja;
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
     * @param con
     * @param numeroReciboCaja2
     * @param institucion
     */
    @SuppressWarnings("unchecked")
	public boolean cargarReciboCaja(Connection con, String numeroReciboCaja, int institucion)
    {
        ResultSetDecorator rs=anulacionRecibosCajaDao.cargarReciboCaja(con,numeroReciboCaja,institucion);
        ResultSetDecorator rs1=null;
        int i=0;
        double valTotal=0;
        try
        {
            if(rs.next())
            {
                this.numeroReciboCaja=numeroReciboCaja;
                this.institucion=institucion;
                
                this.fechaReciboCaja= UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecharecibocaja"));
                try{
                	this.horaReciboCaja=UtilidadFecha.conversionFormatoHoraABD(rs.getTime("horarecibocaja"));
                }
                catch (Exception e) {
                	this.horaReciboCaja=rs.getString("horarecibocaja");
				}
                this.consecutivoCaja=rs.getInt("consecutivocaja");
                this.codigoCaja=rs.getInt("codigocaja");
                this.descripcionCaja=rs.getString("descripcioncaja");
                this.loginUsuarioCaja=rs.getString("loginusuario");
                this.nombreUsuarioCaja=rs.getString("nombreusuario");
                this.recibidoDe=rs.getString("recibidode");
                this.codigoCentroAtencion=rs.getInt("codigocentroatencion");
                this.nombreCentroAtencion=rs.getString("nombrecentroatencion");
                this.eliminarRC=rs.getString("is_eliminar_rc");
                int ingreso=rs.getInt("ingreso");
                this.ingresoReciboCaja=ingreso>0?ingreso:null;
                
                rs1=anulacionRecibosCajaDao.consultarDetalleConceptosRecibosCaja(con,this.numeroReciboCaja,this.institucion);
                while(rs1.next())
                {
                    this.conceptosRC.put("consecutivoconceptorc_"+i,rs1.getString("consecutivoconceptorc"));
                    this.conceptosRC.put("codigoconceptorc_"+i,rs1.getString("codigoconceptorc"));
                    this.conceptosRC.put("descripcionconceptorc_"+i,rs1.getString("descripcionconceptorc"));
                    this.conceptosRC.put("tipoconceptorc_"+i,rs1.getString("tipoconceptorc"));
                    this.conceptosRC.put("valortipoconceptorc_"+i,rs1.getString("valortipoconceptorc"));
                    this.conceptosRC.put("docsoporteconceptorc_"+i,rs1.getString("docsoporteconceptorc"));
                    this.conceptosRC.put("valorconceptorc_"+i,rs1.getString("valorconceptorc"));
                    this.conceptosRC.put("tipoidbenefeciario_"+i,rs1.getString("tipoidbenefeciario"));
                    this.conceptosRC.put("numeroidbeneficiario_"+i,rs1.getString("numeroidbeneficiario"));
                    this.conceptosRC.put("nombrebeneficiario_"+i,rs1.getString("nombrebeneficiario"));
                    valTotal=valTotal+rs1.getDouble("valorconceptorc");
                    i++;
                }
                this.conceptosRC.put("numeroregistros",i+"");
                this.totalRecibidoCaja=valTotal;
                
              
                
            }
        }
        catch (SQLException e)
        {
            logger.error("Error consultado el recibo caja [AnulacionRecibosCaja.java]");
            e.printStackTrace();
        }
        return false;
    }

    
    public boolean validarValorReciboCajaVsValorAnticipoDispContr(Connection con, int numContrato ,String valorReciboCaja)
    {
    	boolean esMenor = false;
    	ResultSetDecorator rs1=null;
    	this.valorAntRecConvenio= ConstantesBD.codigoNuncaValidoDouble;
    	try{
    	   rs1=obtenerValorAnticipoDipoContratConveRecibCaja(con, numContrato);
        
    	   if (rs1.next())  
    	      if(rs1.getDouble("valor") >= Utilidades.convertirADouble(valorReciboCaja))
               {  
    	    	this.valorAntRecConvenio = rs1.getDouble("valorantconv");  
        	    esMenor = true;
               }
    	  
    	}catch(SQLException e) {
    		logger.error("Error realizando Validacion Valor Recibo Caja [AnulacionRecibosCaja.java]");
            e.printStackTrace();
    	}
       
    	return esMenor;
    }
     
   
    /**
     * 
     * @param con
     * @param numReciboCaja
     * @return
     */
	private ResultSetDecorator obtenerValorAnticipoDipoContratConveRecibCaja(Connection con,int  numContrato) {
		
		return anulacionRecibosCajaDao.obtenerValorAnticipoDipoContratConveRecibCaja(con, numContrato);
	}

	/**
	 * 
	 * @param con
	 * @param numeroReciboCaja
	 * @return
	 */
	public int esReciboCajaXConceptAnticipConvenioOdonto(Connection con,String numeroReciboCaja) {
		
		
		int codContrato = 0;
    	ResultSetDecorator rs1=null;
    	try{
    	   rs1=anulacionRecibosCajaDao.esReciboCajaXConceptAnticipConvenioOdonto(con,numeroReciboCaja );
        
    	   if (rs1.next())  
    	   {
    		   codContrato=rs1.getInt("codcontrato");    		   
    	   }
    	  
    	}catch(SQLException e) {
    		logger.error("Error realizando Validacion Valor Recibo Caja [AnulacionRecibosCaja.java]");
            e.printStackTrace();
    	}
		
		return codContrato;
	}
	
	
	public boolean actualizarValorAnticipo(Connection con, int numContrato, double valorAntRecConvenio, String usuario) {
		
		return anulacionRecibosCajaDao.actualizarValorAnticipo(con, numContrato, valorAntRecConvenio, usuario);
	}
	
	/**
     * @return Returns the fechaAnulacion.
     */
    public String getFechaAnulacion()
    {
        return fechaAnulacion;
    }
    /**
     * @param fechaAnulacion The fechaAnulacion to set.
     */
    public void setFechaAnulacion(String fechaAnulacion)
    {
        this.fechaAnulacion = fechaAnulacion;
    }
    /**
     * @return Returns the horaAnulacion.
     */
    public String getHoraAnulacion()
    {
        return horaAnulacion;
    }
    /**
     * @param horaAnulacion The horaAnulacion to set.
     */
    public void setHoraAnulacion(String horaAnulacion)
    {
        this.horaAnulacion = horaAnulacion;
    }
    /**
     * @return Returns the usuarioAnulacion.
     */
    public String getUsuarioAnulacion()
    {
        return usuarioAnulacion;
    }
    /**
     * @param usuarioAnulacion The usuarioAnulacion to set.
     */
    public void setUsuarioAnulacion(String usuarioAnulacion)
    {
        this.usuarioAnulacion = usuarioAnulacion;
    }
    
	public Integer getIngresoReciboCaja() {
		return ingresoReciboCaja;
	}

	public void setIngresoReciboCaja(Integer ingresoReciboCaja) {
		this.ingresoReciboCaja = ingresoReciboCaja;
	}
    
    /**
     * @param con
     * @return
     */
    public int ingresarAnulacionReciboCaja(Connection con)
    {
    	int codigoAnulacion=anulacionRecibosCajaDao.ingresarAnulacionReciboCaja(con,this.consecutivoAnulacionReciboCaja,this.numeroReciboCaja,this.institucion,this.fechaAnulacion,this.horaAnulacion,this.usuarioAnulacion,this.motivo,this.Observaciones,this.codigoCentroAtencion);
        this.numeroAnulacionReciboCaja=codigoAnulacion+"";
        return codigoAnulacion;
    }

    
    /**
     * Metodo que retorna un array con los codigos de los pagos que se han realizado con un recibo de caja determinado
     * @param con
     * @param codigoTipoDocumentoPagosReciboCaja
     * @param numeroReciboCaja2
     * @param institucion2
     * @return
     */
    public String[] getPagosFacturaPacienteReciboCaja(Connection con, int codigoTipoDocumento, String numeroReciboCaja, int institucion)
    {
        ResultSetDecorator rs=anulacionRecibosCajaDao.consultarPagosFacturaPacienteReciboCaja(con,codigoTipoDocumento,numeroReciboCaja,institucion);
        String codigo="";
        int numeroCodigo=0;
        try
        {
            while(rs.next())
            {
                if(numeroCodigo==0)
                {
                    codigo=codigo+rs.getString("codigo");
                }
                else
                {
                    codigo=codigo+ConstantesBD.separadorSplit+rs.getString("codigo");
                }
                numeroCodigo++;
            }
        }
        catch (SQLException e)
        {
            logger.error("Error consultado codigos [AnulacionRecibosCaja.java]");
            e.printStackTrace();
        }
        return codigo.split(ConstantesBD.separadorSplit);
    }

    
    /**
     * Metodo que retorna un array con los codigos de los pagos que se han realizdo para la empresa a través de los recibos de caja
     * @param con
     * @param codigoTipoDocumento
     * @param numeroReciboCaja
     * @param institucion
     * @return
     */
    public String[] getPagosEmpresaReciboCaja(Connection con, int codigoTipoDocumento, String numeroReciboCaja, int institucion)
    {
        ResultSetDecorator rs=anulacionRecibosCajaDao.consultarPagosEmpresaReciboCaja(con,codigoTipoDocumento,numeroReciboCaja,institucion);
        String codigo="";
        int numeroCodigo=0;
        try
        {
            while(rs.next())
            {
                if(numeroCodigo==0)
                {
                    codigo=codigo+rs.getString("codigo");
                }
                else
                {
                    codigo=codigo+ConstantesBD.separadorSplit+rs.getString("codigo");
                }
                numeroCodigo++;
            }
        }
        catch (SQLException e)
        {
            logger.error("Error consultado codigos [AnulacionRecibosCaja.java]");
            e.printStackTrace();
        }
        return codigo.split(ConstantesBD.separadorSplit);
    }

    
    /**
     * @param con
     * @return
     */
    public boolean generarAbonos(Connection con,String[] codigos)
    {
        return anulacionRecibosCajaDao.generarAbonos(con,this.numeroAnulacionReciboCaja,ConstantesBD.tipoMovimientoAbonoAnulacionReciboCaja,UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(),codigos);
    }

    
    /**
     * @param con
     * @param codigoTipoDocumentoPagosReciboCaja
     * @param numeroReciboCaja2
     * @param institucion2
     * @return
     */
    public String[] getAbonosReciboCaja(Connection con, int codigoTipoDocumento, String numeroReciboCaja, int institucion)
    {
        ResultSetDecorator rs=anulacionRecibosCajaDao.consultarAbonosReciboCaja(con,codigoTipoDocumento,numeroReciboCaja,institucion);
        String codigo="";
        int numeroCodigo=0;
        try
        {
            while(rs.next())
            {
                if(numeroCodigo==0)
                {
                    codigo=codigo+rs.getString("codigo");
                }
                else
                {
                    codigo=codigo+ConstantesBD.separadorSplit+rs.getString("codigo");
                }
                numeroCodigo++;
            }
        }
        catch (SQLException e)
        {
            logger.error("Error consultado codigos [AnulacionRecibosCaja.java]");
            e.printStackTrace();
        }
        return codigo.split(ConstantesBD.separadorSplit);
    }

    
    /**
     * @param con
     * @param i
     * @return
     */
    public String consultarValorOperacionAbono(Connection con, int codigoAbono)
    {
        ResultSetDecorator rs=anulacionRecibosCajaDao.consultarValorOperacionAbono(con,codigoAbono);
        try
        {
            if(rs.next())
            {
                return rs.getString("valor")+ConstantesBD.separadorSplit+rs.getString("operacion");
            }
        }
        catch (SQLException e)
        {
            logger.error("Error consultado codigos [AnulacionRecibosCaja.java]");
            e.printStackTrace();
        }
        return "";
    }

    
    /**
     * @param con
     * @param institucion2
     * @param i
     * @param paciente
     * @param ingreso Integer con el id del ingreso, si llega null no filtra por este campo
     * @return
     */
    public String[] getTodosAbonos(Connection con, int paciente, Integer ingreso)
    {
        ResultSetDecorator rs=anulacionRecibosCajaDao.consultarTodosAbonosReciboCaja(con,paciente, ingreso);
        String codigo="";
        int numeroCodigo=0;
        try
        {
            while(rs.next())
            {
                if(numeroCodigo==0)
                {
                    codigo=codigo+rs.getString("codigo");
                }
                else
                {
                    codigo=codigo+ConstantesBD.separadorSplit+rs.getString("codigo");
                }
                numeroCodigo++;
            }
        }
        catch (SQLException e)
        {
            logger.error("Error consultado codigos [AnulacionRecibosCaja.java]");
            e.printStackTrace();
        }
        return codigo.split(ConstantesBD.separadorSplit);
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
	 * @return Returns the nombreCentroAtencion.
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * @param nombreCentroAtencion The nombreCentroAtencion to set.
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}
	
	
	/**
     * Consulta el pago general de empresa
     * @param Connection con
     * @param HashMap parametros
     * @return HashMap<String,Object>
     */
    public HashMap<String,Object> consultarPagoGeneralEmpresa(Connection con, String tipo, String institucion, String documento)
    {
    	HashMap parametros = new HashMap();
    	parametros.put("tipo_doc", tipo);
		parametros.put("institucion", institucion);
		parametros.put("documento", documento);
    	return anulacionRecibosCajaDao.consultarPagoGeneralEmpresa(con, parametros);
    }
    
    /**
     * Actualizar estado pagos genral empresa
     * @param con
     * @param HashMap
     * @return boolean
     */
    public boolean actualizarEstadoPagGenEmp(Connection con, String estado, String codigo_pge)
    {
    	HashMap parametros = new HashMap();
    	parametros.put("estado", estado);
		parametros.put("codigo_pge", codigo_pge);
    	return anulacionRecibosCajaDao.actualizarEstadoPagGenEmp(con, parametros);
    }
    
    /**
     * Actualizar estado pagos genral empresa
     * @param con
     * @param HashMap
     * @return boolean
     */
    public boolean actualizarEstadoAplPagFacVar(Connection con, String estado, String codigo_apfv)
    {
    	HashMap parametros = new HashMap();
    	parametros.put("estado", estado);
		parametros.put("codigo_apfv", codigo_apfv);
    	return anulacionRecibosCajaDao.actualizarEstadoAplPagFacVar(con, parametros);
    }
    
    /**
     * Consulta la Factura Varia
     * @param Connection con
     * @param HashMap parametros
     * @return HashMap<String,Object>
     */
    public HashMap<String,Object> consultarFacturaVaria(Connection con, String codigo_apfv)
    {
    	HashMap parametros = new HashMap();
    	parametros.put("codigo_apfv", codigo_apfv);
    	return anulacionRecibosCajaDao.consultarFacturaVaria(con, parametros);
    }
    
    /**
     * Consulta los consecutivos de las multas asociadas a la factura varia
     * @param Connection con
     * @param HashMap parametros
     * @return HashMap<String,Object>
     */
    public ArrayList<String> consultarConsecutivosMultaCitas(Connection con, String estado, String codigo_fv)
    {
    	HashMap parametros = new HashMap();
    	parametros.put("estado", estado);
		parametros.put("codigo_fv", codigo_fv);
    	return anulacionRecibosCajaDao.consultarConsecutivosMultaCitas(con, parametros);
    }
    
    /**
     * actualizar el estado de las multas de citas asociada a una factura varia
     * @param con
     * @param HashMap
     * @return boolean
     */
    public boolean actualizarEstadoMultaCita(Connection con, String estado, String codigo_fv)
    {
    	HashMap parametros = new HashMap();
    	parametros.put("estado", estado);
		parametros.put("codigo_fv", codigo_fv);
    	return anulacionRecibosCajaDao.actualizarEstadoMultaCita(con, parametros);
    }
    
    /**
     * actualizar el estado de las multas de citas asociada a una factura varia
     * @param con
     * @param HashMap
     * @return boolean
     */
    public boolean actualizarValorPagoFacVaria(Connection con, String valor_pago, String codigo_fv)
    {
    	HashMap parametros = new HashMap();
    	parametros.put("valor_pago", valor_pago);
		parametros.put("codigo_fv", codigo_fv);
    	return anulacionRecibosCajaDao.actualizarValorPagoFacVaria(con, parametros);
    }
    
    /**
     * 
     */
    public int anularReciboCajaFacturasVarias(Connection con, String numeroReciboCaja, UsuarioBasico usuario)
    {
    	
       InstitucionBasica institucion = new InstitucionBasica();
       boolean enTransaccion = false ;
       int result = ConstantesBD.codigoNuncaValido;
       HashMap<String,Object> datos = new HashMap<String,Object>();
	   datos = this.consultarPagoGeneralEmpresa(con, String.valueOf(ConstantesBD.codigoTipoDocumentoPagosReciboCaja), usuario.getCodigoInstitucion(), numeroReciboCaja);
	   if(datos.size()>1)
	   {
		   enTransaccion = this.actualizarEstadoPagGenEmp(con, String.valueOf(ConstantesBD.codigoEstadoPagosAnulado), datos.get("codigo_pge").toString());
		   if(enTransaccion)
		   {
			   enTransaccion = this.actualizarEstadoAplPagFacVar(con, String.valueOf(ConstantesBD.codigoEstadoAplicacionPagosAnulado), datos.get("codigo_apfv").toString());
			   if(enTransaccion)
			   {
				   HashMap<String, Object> datosFacVar = new HashMap<String, Object>();
				   datosFacVar = this.consultarFacturaVaria(con, datos.get("codigo_apfv").toString());
				   if(datosFacVar.size()>1)
				   {
					   enTransaccion = this.actualizarValorPagoFacVaria(con, 
							   String.valueOf( Utilidades.convertirADouble(datosFacVar.get("valor_pagos").toString()) - Utilidades.convertirADouble(datos.get("valor_aplicacion").toString())), 
							   datosFacVar.get("cod_factura_varia").toString());
					   if(enTransaccion)
					   {
						  
							if((datosFacVar.get("tipo_concepto").toString()).equals(ConstantesIntegridadDominio.acronimoMultas)){
						   ArrayList<String> conMultCita = new ArrayList<String>(); 
						   conMultCita = this.consultarConsecutivosMultaCitas(con, "PAG" , datosFacVar.get("cod_factura_varia").toString());
						   if(conMultCita.size()>0)
						   {
							   for(int k=0 ; k< conMultCita.size(); k++)
							   {
								   if(!this.actualizarEstadoMultaCita(con, 
										   ConstantesIntegridadDominio.acronimoEstadoFacturado, 
										   conMultCita.get(k).toString()))
									   enTransaccion = false;
							   }
						   }
					   
					   
				   
							}else{
					   if((datosFacVar.get("tipo_concepto").toString()).equals(ConstantesIntegridadDominio.acronimoTipoVentaTarjetaodontologica)){
						   
					   logger.info("************************************************************************** ES VTO************************************************");
					   
					   ArrayList<DtoBeneficiarioCliente> listaBeneficiarios = new ArrayList<DtoBeneficiarioCliente>();
		        		ArrayList<DtoVentaTarjetasCliente> listaVentas = new ArrayList<DtoVentaTarjetasCliente>();
		        		 DtoVentaTarjetasCliente dtowhereventas = new DtoVentaTarjetasCliente();
		        		 dtowhereventas.setCodigoPkFacturaVaria(Integer.parseInt( datosFacVar.get("cod_factura_varia").toString())); ///arreglo
		        		listaVentas = VentasTarjetasCliente.cargar(dtowhereventas);
		        		DtoBeneficiarioCliente dtoWhereBeneficiario=new DtoBeneficiarioCliente();
		        		for(DtoVentaTarjetasCliente i : listaVentas){
		        		 
		        			
		        			dtoWhereBeneficiario = new DtoBeneficiarioCliente();
		        			
		        			dtoWhereBeneficiario.setVentaTarjetaCliente(i.getCodigoPk());
		        			
		        			DtoBeneficiarioCliente dtoNuevoBeneficiario = new DtoBeneficiarioCliente();
		        			dtoNuevoBeneficiario.setEstadoTarjeta("INAC");
		        			dtoNuevoBeneficiario.setVentaTarjetaCliente(i.getCodigoPk());
		        			
		        			
		        			BeneficiariosTarjetaCliente.modificar(dtoNuevoBeneficiario,dtoWhereBeneficiario);
		        			
		        		}
					   
					   
				   }else
					   result = 0;
				   }
					   }
				   
				   }
			   }
		   }
	   }else
		   result = 0;
	   if(enTransaccion)
		   return 1;
	   else
		   return ConstantesBD.codigoNuncaValido;
    }

	/**
	 * @return the eliminarRC
	 */
	public String getEliminarRC() {
		return eliminarRC;
	}

	/**
	 * @param eliminarRC the eliminarRC to set
	 */
	public void setEliminarRC(String eliminarRC) {
		this.eliminarRC = eliminarRC;
	}

	/**
	 * @return the valorAntRecConvenio
	 */
	public double getValorAntRecConvenio() {
		return valorAntRecConvenio;
	}

	/**
	 * @param valorAntRecConvenio the valorAntRecConvenio to set
	 */
	public void setValorAntRecConvenio(double valorAntRecConvenio) {
		this.valorAntRecConvenio = valorAntRecConvenio;
	}

	public String getConsecutivoAnulacionReciboCaja() {
		return consecutivoAnulacionReciboCaja;
	}

	public void setConsecutivoAnulacionReciboCaja(
			String consecutivoAnulacionReciboCaja) {
		this.consecutivoAnulacionReciboCaja = consecutivoAnulacionReciboCaja;
	}

	

	
	
	
    
}
