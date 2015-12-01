/*
 * @(#)PagosEmpresa.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.cartera;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.PagosEmpresaDao;

import util.ConstantesBD;
import util.InfoDatosInt;

/**
 * Clase para el manejo de Pagos Empresa
 * @version 1.0, Abril 08, 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class PagosEmpresa 
{
	/**
	 * DAO utilizado por el objeto parra acceder a la fuente de datos
	 */
	private static PagosEmpresaDao pagosDao = null;
    
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(PagosEmpresa.class);

	/** 
	 * codigo del ajuste empresa
	 */
	private int codigo;
	
	/**
	 * codigo de la factura
	 */
    private int codigoFactura;
    
    /**
     * cod - desc tipo doc
     */
    private InfoDatosInt tipoDoc;

    /** 
     * fecha
     */
    private String fecha;
    
    /**
     * cod - desc estadoCartera
     */
    private InfoDatosInt estadoCartera;
    
    /**
     * valor
     */
    private double valor;
    
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
				pagosDao = myFactory.getPagosEmpresaDao();
				wasInited = (pagosDao != null);
			}
			return wasInited;
	}
	
	/**
	 * Constructor con todos los atributos de la clase
	 * @param codigo
	 * @param codigoFactura
	 * @param codigoTipoDoc
	 * @param descTipoDocumento
	 * @param fecha
	 * @param codigoEstadoCartera
	 * @param descripcionEstadoCartera
	 * @param valor
	 */
	public PagosEmpresa(	int codigo, int codigoFactura, int codigoTipoDoc, 
	        							String descTipoDocumento, String fecha,  int codigoEstadoCartera,
	        							String descripcionEstadoCartera, double valor)
	{
	    this.codigo=codigo;
	    this.codigoFactura= codigoFactura;
	    this.setCodigoTipoDoc(codigoTipoDoc);
	    this.setDescripcionTipoDoc(descTipoDocumento);
	    this.fecha=fecha;
	    this.setCodigoEstadoCartera(codigoEstadoCartera);
	    this.setDescripcionEstadoCartera(descripcionEstadoCartera);
	    this.valor= valor;
	    this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * Constructor vacio de la clase
	 *
	 */
	public PagosEmpresa() 
    {
	    this.reset();
		this.init (System.getProperty("TIPOBD"));   
	}
    
	/**
	 * resetea los datos pertinentes al registro de terceros
	 */
	public void reset()
	{
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.codigoFactura=ConstantesBD.codigoNuncaValido;
		this.tipoDoc= new InfoDatosInt();
		this.fecha= "";
		this.estadoCartera= new InfoDatosInt();
		this.valor=0;
	}
	
	/**
	 * metodo para insertar pagos de empresa
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public int insertarPagosEmpresa(Connection con) throws SQLException 
	{
		int  resp1=0;
		if (pagosDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (PagosEmpresaDao - insertarPagosEmpresa )");
		}
		resp1=pagosDao.insertar(	con, this.getCodigoFactura(), this.getCodigoTipoDoc(), 
												this.getFecha(), this.getCodigoEstadoCartera(), this.getValor());
		return resp1;
	}
	
	/**
	 * Inserta los pagos de empresa en una transaccion dado su estado
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public int insertarPagosEmpresaTransaccional (Connection con, String estado) throws SQLException
	{
	    int numElementosInsertados=0;
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    if (estado.equals(ConstantesBD.inicioTransaccion))
	    {
	        if (!myFactory.beginTransaction(con))
	        {
	            myFactory.abortTransaction(con);
	        }
	    }
	    try
	    {
	        numElementosInsertados=pagosDao.insertar(		con, this.getCodigoFactura(), this.getCodigoTipoDoc(), 
	                																this.getFecha(), this.getCodigoEstadoCartera(), this.getValor());
	        if (numElementosInsertados<=0)
	        {
	            myFactory.abortTransaction(con);
	        }
	    }
	    catch (SQLException e)
	    {
	        myFactory.abortTransaction(con);
	        throw e;
	    }
	      
	    if (estado.equals(ConstantesBD.finTransaccion))
	    {
	        myFactory.endTransaction(con);
	    }
	    return numElementosInsertados;
	}
	

    /**
     * @return Returns the codigo.
     */
    public int getCodigo() {
        return codigo;
    }
    /**
     * @param codigo The codigo to set.
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    /**
     * @return Returns the codigoFactura.
     */
    public int getCodigoFactura() {
        return codigoFactura;
    }
    /**
     * @param codigoFactura The codigoFactura to set.
     */
    public void setCodigoFactura(int codigoFactura) {
        this.codigoFactura = codigoFactura;
    }
    /**
     * @return Returns the estadoCartera.
     */
    public InfoDatosInt getEstadoCartera() {
        return estadoCartera;
    }
    /**
     * @param estadoCartera The estadoCartera to set.
     */
    public void setEstadoCartera(InfoDatosInt estadoCartera) {
        this.estadoCartera = estadoCartera;
    }
    /**
     * @return Returns the fecha.
     */
    public String getFecha() {
        return fecha;
    }
    /**
     * @param fecha The fecha to set.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    /**
     * @return Returns the tipoDoc.
     */
    public InfoDatosInt getTipoDoc() {
        return tipoDoc;
    }
    /**
     * @param tipoDoc The tipoDoc to set.
     */
    public void setTipoDoc(InfoDatosInt tipoDoc) {
        this.tipoDoc = tipoDoc;
    }
    /**
     * @return Returns the valor.
     */
    public double getValor() {
        return valor;
    }
    /**
     * @param valor The valor to set.
     */
    public void setValor(double valor) {
        this.valor = valor;
    }
    
    /**
     * Asigna el cod del tipo de doc al objeto infodatos
     * @param codTipoDoc
     */
    public void setCodigoTipoDoc(int codTipoDoc)
    {
        this.tipoDoc.setCodigo(codTipoDoc);
    }
    
    /**
     * Asigna la desc del tipo de Doc al objeto infodatos
     * @param descTipoDoc
     */
    public void setDescripcionTipoDoc(String descTipoDoc)
    {
        this.tipoDoc.setDescripcion(descTipoDoc);
    }
    
    /**
     * Asigna el cod del estado cartera al objeto infodatos
     * @param codEstadoCartera
     */
    public void setCodigoEstadoCartera(int codEstadoCartera)
    {
        this.estadoCartera.setCodigo(codEstadoCartera);
    }
    
    /**
     * Asigna la desc del tipo de Doc al objeto infodatos
     * @param descTipoDoc
     */
    public void setDescripcionEstadoCartera(String descEstadoCartera)
    {
        this.estadoCartera.setDescripcion(descEstadoCartera);
    }
    
    /**
     * Retorna el cod del tipo de Doc al objeto infodatos
     * @return codTipoDoc
     */
    public int getCodigoTipoDoc()
    {
        return this.tipoDoc.getCodigo();
    }
    
    /**
     * Retorna la desc del tipo de Doc al objeto infodatos
     * @return descTipoDoc
     */
    public String getDescripcionTipoDoc()
    {
        return this.tipoDoc.getDescripcion();
    }
    
    /**
     * Retorna el cod del estado cartera al objeto infodatos
     * @return codEstadoCartera
     */
    public int getCodigoEstadoCartera()
    {
        return this.estadoCartera.getCodigo();
    }
    
    /**
     * Retorna la desc del tipo de Doc al objeto infodatos
     * @return descTipoDoc
     */
    public String getDescripcionEstadoCartera()
    {
        return this.estadoCartera.getDescripcion();
    }
}
