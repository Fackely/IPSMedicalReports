/*
 * @(#)ExcepcionesFarmacia.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_05
 *
 */

package com.princetonsa.mundo.medicamentos;

import java.sql.Connection;
import java.sql.SQLException;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ExcepcionesFarmaciaDao;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.RespuestaHashMap;
import util.UtilidadBD;

/**
 * Clase que se encarga de manejar las 
 * excepciones de farmacia 
 * 
 * @version 1.0 Nov 29, 2004
 */
public class ExcepcionesFarmacia 
{

    /**
     * Código de la excepción de farmacia
     */
    private int codigo;
    
    /**
     * Convenio manejado por esta excepción
     */
    private InfoDatosInt convenio;

    /**
     * Convenio manejado por esta excepción
     */
    private InfoDatosInt centroCosto;

    /**
     * Convenio manejado por esta excepción
     */
    private InfoDatosInt articulo;
    
    /**
     * Número de tipo double que indica el 
     * porcentaje de este artículo 
     */
    private double noCubre;
    
	/**
	 * DAO de este objeto, para trabajar con Servicio en
	 * la fuente de datos
	 */
    private ExcepcionesFarmaciaDao excepcionesFarmaciaDao;
    
    /**
     * Constructor vacío de esta clase
     */
    public ExcepcionesFarmacia ()
    {
        this.clean();
        this.init(System.getProperty("TIPOBD"));
    }
    
    /**
     * Método que limpia este objeto
     */
    public void clean()
    {
        codigo=ConstantesBD.codigoNuncaValido;
        convenio=new InfoDatosInt();
        convenio.setCodigo(ConstantesBD.codigoNuncaValido);
        centroCosto=new InfoDatosInt();
        centroCosto.setCodigo(ConstantesBD.codigoNuncaValido);
        articulo=new InfoDatosInt();
        articulo.setCodigo(ConstantesBD.codigoNuncaValido);
        articulo.setNombre("");
        noCubre=0.0;
    }

	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( excepcionesFarmaciaDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			excepcionesFarmaciaDao= myFactory.getExcepcionesFarmaciaDao();
			if( excepcionesFarmaciaDao!= null )
				return true;
		}
		return false;
	}
	 
	/**
	 * Método que dado un objeto de tipo
	 * RespuestaHashMap con toda la información
	 * insertada
	 * 
	 * @param mapaAntiguo Objeto de tipo
	 * RespuestaHashMap  donde se almacenarán
	 *  
	 * @param con Conexión con la fuente de datos
	 * @throws SQLException
	 */
	public void cargarExcepcionesBaseFarmacia (RespuestaHashMap mapaAntiguo, Connection con) throws SQLException
	{
	    String nombreColumnas[]={"codigo", "codigoConvenio", "convenio", "codigoCentroCosto", "centroCosto", "codigoArticulo", "articulo", "noCubre", "vieneBD", "esPos"};
	    mapaAntiguo.setTamanio(UtilidadBD.resultSet2HashMap(mapaAntiguo, nombreColumnas, excepcionesFarmaciaDao.consultarBaseExcepcionFarmacia(con, this.convenio.getCodigo(), this.centroCosto.getCodigo(), this.articulo.getCodigo(), this.articulo.getNombre()), false, true) );
	}
	
	/**
	 * Método que elimina una excepción de farmacia 
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public int eliminarExcepcionBaseFarmacia (Connection con) throws SQLException
	{
	    return excepcionesFarmaciaDao.eliminarBaseExcepcionFarmacia(con, codigo);
	}
	
	/**
	 * Método que revisa si el elemento actual ya tiene
	 * uno repetido en la fuente de datos
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public boolean existeBaseExcepcionFarmaciaPrevio (Connection con) throws SQLException
	{
	    return excepcionesFarmaciaDao.existeBaseExcepcionFarmaciaPrevio(con, convenio.getCodigo(), centroCosto.getCodigo(), articulo.getCodigo(), this.codigo);
	}
	
	public int insertarBaseExcepcionFarmacia(Connection con) throws SQLException
	{
	    return excepcionesFarmaciaDao.insertarBaseExcepcionFarmacia(con, convenio.getCodigo(), centroCosto.getCodigo(), articulo.getCodigo(), this.noCubre);
	}
	
	public int modificarBaseExcepcionFarmacia(Connection con) throws SQLException
	{
	    return excepcionesFarmaciaDao.modificarBaseExcepcionFarmacia(con,codigo,convenio.getCodigo(), centroCosto.getCodigo(), articulo.getCodigo(),this.noCubre);
	}
	
    /**
     * @return Retorna el/la articulo.
     */
    public InfoDatosInt getArticulo() {
        return articulo;
    }
    /**
     * @return Retorna el/la centroCosto.
     */
    public InfoDatosInt getCentroCosto() {
        return centroCosto;
    }
    /**
     * @return Retorna el/la codigo.
     */
    public int getCodigo() {
        return codigo;
    }
    /**
     * @return Retorna el/la convenio.
     */
    public InfoDatosInt getConvenio() {
        return convenio;
    }
    /**
     * @return Retorna el/la excepcionesFarmaciaDao.
     */
    public ExcepcionesFarmaciaDao getExcepcionesFarmaciaDao() {
        return excepcionesFarmaciaDao;
    }
    /**
     * @return Retorna el/la noCubre.
     */
    public double getNoCubre() {
        return noCubre;
    }
    /**
     * El/La codigo a establecer.
     * @param codigo 
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    
    /**
     * Método que establece el código del
     * convenio
     * 
     * @param codConv Código del Convenio
     */
    public void setCodigoConvenio(int codConv)
    {
        this.convenio.setCodigo(codConv);
    }

    /**
     * Método que establece el código del
     * centro de costo
     * 
     * @param ccosto Código del centro de costo
     */
    public void setCodigoCentroCosto(int ccosto)
    {
        this.centroCosto.setCodigo(ccosto);
    }

    /**
     * Método que establece el código del
     * artículo
     * 
     * @param codArticulo Código del artículo
     */
    public void setCodigoArticulo (int codArticulo)
    {
        this.articulo.setCodigo(codArticulo);
    }
    
    /**
     * El/La noCubre a establecer.
     * @param noCubre 
     */
    public void setNoCubre(double noCubre) {
        this.noCubre = noCubre;
    }
}
