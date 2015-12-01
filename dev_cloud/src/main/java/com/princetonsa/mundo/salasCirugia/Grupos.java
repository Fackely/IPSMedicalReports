/*
 * @(#)Grupos.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 */
package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.InfoDatosInt;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.GruposDao;

/**
 * Clase para el manejo de grupos
 * @version 1.0, Sep 06, 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class Grupos 
{
    /**
	 * DAO utilizado por el objeto parra acceder a la fuente de datos
	 */
	private static GruposDao gruposDao = null;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(Grupos.class);
	
	/**
	 * codigo interno del grupo
	 */
	private int codigoPKGrupo;
	
	/**
	 * codigo - nombre esquema tarifario
	 */
	private InfoDatosInt esquemaTarifario;
	
	/**
	 * codigo del grupo
	 */
	private int grupo;
	
	/**
	 * codigo - nombre(acronimoTiposolicitud)
	 */
	private InfoDatosInt asocio; 
	
	/**
	 * codigo - nombre tipos liquidacion
	 */
	private InfoDatosInt tipoLiquidacion;
	
	/**
	 * unidades soat del asocio
	 */
	private String unidades;
	
	/**
	 * valor del grupo
	 */
	private double valor;
	
	/**
	 * 
	 */
	private String convenio;
	
	/**
	 * 
	 */
	private String tipoServicio;
	
	/**
	 * Campo para saber si el esquema es general o particular
	 */
	private boolean esquemaGeneral;
	
	
	private String liquidarPor ="";
	

	/**
	 * reset de los atributos
	 *
	 */
	public void reset()
	{
	    this.codigoPKGrupo= ConstantesBD.codigoNuncaValido;
	    this.esquemaTarifario= new InfoDatosInt();
	    this.grupo= ConstantesBD.codigoNuncaValido;
	    this.asocio= new InfoDatosInt();
	    this.tipoLiquidacion=new InfoDatosInt();
	    this.unidades="";
	    this.valor=ConstantesBD.codigoNuncaValidoDouble;
	    this.convenio="";
	    this.tipoServicio="";
	    this.esquemaGeneral = false;
	    this.liquidarPor="";
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
			gruposDao = myFactory.getGruposDao();
			wasInited = (gruposDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Constructor vacio de la clase
	 *
	 */
	public Grupos()
	{
	    this.reset();
	    this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inserta un grupo dentro de una transaccion
	 * @param con
	 * @param usuario 
	 * @param codigoInstitucion
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public int insertarXesquemaTarifario (Connection con, String usuario, int codigoInstitucion, String estado,int codigoGrupo) throws SQLException
	{
	    int insertado=0;
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
	        insertado=gruposDao.insertarXesquemaTarifario(con, this.getGrupo(), this.getAsocio().getCodigo(), this.getEsquemaTarifario().getCodigo(), this.getTipoLiquidacion().getCodigo(), this.getUnidades(), this.getValor(), codigoInstitucion,this.getConvenio(),this.getTipoServicio(),"","",usuario,codigoGrupo,this.esquemaGeneral,this.getLiquidarPor());
            if (insertado==0)
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
	    return insertado;
	}
	
	/**
	 * metodo que modifica un grupo en una transaccion
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */ 
	public boolean modificarGrupoTransaccional (Connection con, String estado) throws SQLException
	{
	    boolean insertados=false;
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
	        insertados=gruposDao.modificar(con, this.getGrupo(), this.getAsocio().getCodigo(),this.tipoLiquidacion.getCodigo(), this.getUnidades(), this.getValor(), this.getCodigoPKGrupo(),this.tipoServicio,this.getLiquidarPor());
	        if (!insertados)
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
	    return insertados;
	}
	
	/**
	 * Elimina un grupo dentro de una transaccion
	 * @param con
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public boolean eliminarGrupoTransaccional (Connection con, String estado) throws SQLException
	{
	    boolean eliminados=false;
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
	        eliminados=gruposDao.eliminar(con, this.getCodigoPKGrupo());
	        
	        if (!eliminados)
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
	    return eliminados;
	}
	
	/**
	 * 
	 * @param con
	 * @param grupo
	 * @param codigoAsocio
	 * @param codigoTipoLiquidacion
	 * @param activo
	 * @param codigoInstitucion
	 * @param tipService
	 * @param codigoEsquemaTarifario
	 * @param convenio
	 * @param fechainicial
	 * @param fechafinal
	 * @return
	 */
	public HashMap busquedaAvanzadaGrupos(Connection con,int grupo,int codigoAsocio,int codigoTipoLiquidacion,int codigoInstitucion,String tipService,int codigoEsquemaTarifario,String convenio,String fechainicial,String fechafinal,	String liquidarPor,String cups)
	{
		return gruposDao.busquedaAvanzadaGrupos(con, grupo, codigoAsocio, codigoTipoLiquidacion, codigoInstitucion,tipService,codigoEsquemaTarifario,convenio,fechainicial,fechafinal,liquidarPor,cups);
	}
	
	/**
	 * Metodo que lista los tipos de servicios
	 * @param con
	 * @return
	 */
	public ArrayList listarTiposServicio(Connection con)
	{
		return gruposDao.listarTiposServicio(con);
	}
	
	/**
	 * Metodo que lista los tarifarios oficiales para ser parametrizados en lugar de solo pedir el codigo soat y cups
	 * @param con
	 * @return
	 */
	public ArrayList listarTarifariosOficiales(Connection con)
	{
		return gruposDao.listarTarifariosOficiales(con);
	}
	
	public HashMap listadoGrupos(Connection con, int codigoInstitucion, int codigoEsquemaTarifario,int codigoGrupo, boolean esEsquemaGeneral)
	{
		return gruposDao.listadoGrupos(con, codigoInstitucion, codigoEsquemaTarifario, codigoGrupo, esEsquemaGeneral);
	}
	
	public boolean eliminarCodigosGruposTotal(Connection con,HashMap vo)
	{
		return gruposDao.eliminarCodigosGruposTotal(con, vo);
	}
	
    /**
     * @return Returns the asocio.
     */
    public InfoDatosInt getAsocio() {
        return asocio;
    }
    /**
     * @param asocio The asocio to set.
     */
    public void setAsocio(InfoDatosInt asocio) {
        this.asocio = asocio;
    }
    /**
     * @return Returns the codigoPKGrupo.
     */
    public int getCodigoPKGrupo() {
        return codigoPKGrupo;
    }
    /**
     * @param codigoPKGrupo The codigoPKGrupo to set.
     */
    public void setCodigoPKGrupo(int codigoPKGrupo) {
        this.codigoPKGrupo = codigoPKGrupo;
    }
    /**
     * @return Returns the esquemaTarifario.
     */
    public InfoDatosInt getEsquemaTarifario() {
        return esquemaTarifario;
    }
    /**
     * @param esquemaTarifario The esquemaTarifario to set.
     */
    public void setEsquemaTarifario(InfoDatosInt esquemaTarifario) {
        this.esquemaTarifario = esquemaTarifario;
    }
    /**
     * @return Returns the grupo.
     */
    public int getGrupo() {
        return grupo;
    }
    /**
     * @param grupo The grupo to set.
     */
    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }
    /**
     * @return Returns the tipoLiquidacion.
     */
    public InfoDatosInt getTipoLiquidacion() {
        return tipoLiquidacion;
    }
    /**
     * @param tipoLiquidacion The tipoLiquidacion to set.
     */
    public void setTipoLiquidacion(InfoDatosInt tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }
    /**
     * @return Returns the unidades.
     */
    public String getUnidades() {
        return unidades;
    }
    /**
     * @param unidades The unidades to set.
     */
    public void setUnidades(String unidades) {
        this.unidades = unidades;
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
	 * @return the convenio
	 */
	public String getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	/**
	 * @return the tipoServicio
	 */
	public String getTipoServicio() {
		return tipoServicio;
	}

	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	public boolean actualizarDetCodigosGrupos(Connection con, HashMap<String, Object> vo) 
	{
		return gruposDao.actualizarDetCodigosGrupos(con,vo);
	}

	public HashMap listadoGruposLLave(Connection con, String tempoCodPKGrupo) 
	{
		return gruposDao.listadoGruposLLave(con,tempoCodPKGrupo);
	}
	
	/**
	 * 
	 * @param con
	 * @param convenio
	 * @return
	 */
	public HashMap listarVigenciasConvenio(Connection con,int convenio)
	{
		return gruposDao.listarVigenciasConvenio(con, convenio);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEsquemaTarifario
	 * @param convenio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoInstitucion
	 * @param usuario
	 * @return
	 */
	public int insertarVigenciaGrupo(Connection con, int codigoEsquemaTarifario, String convenio, String fechaInicial, String fechaFinal, int codigoInstitucion, String usuario, boolean esquemaGeneral)
	{
		return gruposDao.insertarVigenciaGrupo(con, codigoEsquemaTarifario, convenio, fechaInicial, fechaFinal, codigoInstitucion, usuario,esquemaGeneral);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 */
	public boolean modificarVigenciasConvenio(Connection con,HashMap vo)
	{
		return gruposDao.modificarVigenciasConvenio(con, vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param convenio
	 * @param codigo
	 */
	public boolean eliminarGrupoMaestro(Connection con,int convenio,int codigo)
	{
		return gruposDao.eliminarGrupoMaestro(con, convenio, codigo);
	}

	/**
	 * @return the esquemaGeneral
	 */
	public boolean isEsquemaGeneral() {
		return esquemaGeneral;
	}

	/**
	 * @param esquemaGeneral the esquemaGeneral to set
	 */
	public void setEsquemaGeneral(boolean esquemaGeneral) {
		this.esquemaGeneral = esquemaGeneral;
	}
	

	public String getLiquidarPor() {
		return liquidarPor;
	}

	public void setLiquidarPor(String liquidarPor) {
		this.liquidarPor = liquidarPor;
	}
}
