/*
 * @(#)EsquemaTarifario.java
 * 
 * Created on 03-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
 package com.princetonsa.mundo.cargos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import java.util.HashMap;
import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.EsquemaTarifarioDao;
import com.princetonsa.dto.facturacion.DtoEsquemasTarifarios;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.EsquemasTarifarios;
import com.servinte.axioma.orm.delegate.facturacion.EsquemasTarifariosDelegate;

import util.InfoDatos;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.ResultadoCollectionDB;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * Clase para el manejo de un esquema tarifario
 * 
 * @version 1.0, Mayo 03 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Raul@PrincetonSA.com">Raúl Cancino</a>
 */
public class EsquemaTarifario 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(EsquemaTarifario.class);
	
	/**
	 * Tipo Manual (Tarifario Oficial) asociado al esquema
	 */
	private InfoDatosInt  tarifarioOficial;
	
	/**
	 * Metodo de ajuste asociado al esquema
	 */
	private InfoDatos  metodoAjuste;
	
	/**
	 * Codigo del esquema tarifario
	 */
	private int codigo;
	/**
	 * 
	 * Descripcion del esquema tarifario
	 */
	private String nombre;
	
	/**
	 * Dice si el esquema tarifario es de servicios o de inventarios
	 */
	private boolean esInventario;
	
	/**
	 * Boolean que me indica si el esquema tarifario está
	 * activo o no
	 */
	private boolean activo;
	
	/**
	 * Si es ISS almacena UVRs
	 * Si es SOAT almacena Salario Mínimo
	 */
	private float cantidad;
	
	/**
	 * El DAO usado por el objeto <code>EsquemaTarifario</code> para acceder a la fuente de datos.
	 */
	private EsquemaTarifarioDao esquemaTarifarioDao = null;
	
	/**
	 * String para esInventario, en busqueda avanzada
	 *
	 */
	private char inventarioAux;
	
	/**
	 * Creadora de la clase EsquemaTarifario.java
	 */
	public EsquemaTarifario()
	{
		this.clean();
		this.init(System.getProperty("TIPOBD"));		
	}

	/**
	 * Este método inicializa en valores vacíos, -mas no nulos- los atributos de este objeto.
	 */
	public void clean ()
	{
		this.tarifarioOficial = new InfoDatosInt();
		this.metodoAjuste = new InfoDatos();
		this.codigo = 0;
		this.nombre = "";
		this.esInventario = false;
		this.activo=true;
		this.inventarioAux = ' ';
		this.cantidad = 0;
	}
	
	/**
	 * Creadora de la clase EsquemaTarifario.java
	 * @param codigo. int, codigo del esquema tarifario
	 * @param nombre. String, descripción del esquema tarifario
	 * @param esInventario. boolean, si es inventario o no
	 * @param tarifarioOficial. InfoDatosInt, Tipo de manual asociado al esquema
	 * @param metodoAjuste. InfoDatosInt, Método de ajuste asociado al esquema 
	 */
	public EsquemaTarifario(	int codigo,
												String nombre,
												boolean esInventario,
												InfoDatosInt tarifarioOficial,
												InfoDatos metodoAjuste	)
	{
		this.tarifarioOficial = tarifarioOficial;
		this.metodoAjuste = metodoAjuste;
		this.codigo = codigo;
		this.nombre = nombre;
		this.esInventario = esInventario;
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public void init(String tipoBD)
	{
		if( esquemaTarifarioDao == null)
		{
			if(tipoBD==null)
			{
				logger.error("No esta llegando el tipo de base de datos");
				System.exit(1);
			}
			else
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
				if (myFactory != null)
				{
					esquemaTarifarioDao = myFactory.getEsquemaTarifarioDao();
				}					
			}
		}
	}
	
	/**
	 * metodo q obtiene el tarifario oficial
	 * @param con
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public static int obtenerTarifarioOficialXCodigoEsquemaTar(Connection con, int codigoEsquemaTarifario) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEsquemaTarifarioDao().obtenerTarifarioOficialXCodigoEsquemaTar(con, codigoEsquemaTarifario);
	}
	
	/**
	 * metodo q obtiene el tarifario oficial
	 * @param con
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public static int obtenerTarifarioOficialXCodigoEsquemaTar(int codigoEsquemaTarifario) throws IPSException
	{
		Connection con= UtilidadBD.abrirConexion();
		int returna= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEsquemaTarifarioDao().obtenerTarifarioOficialXCodigoEsquemaTar(con, codigoEsquemaTarifario);
		UtilidadBD.closeConnection(con);
		return returna;
	}
	
	/**
	 * Retorna el tipo Manual (Tarifario Oficial) asociado al esquema
	 * @return
	 */
	public InfoDatosInt getTarifarioOficial()
	{
		return tarifarioOficial;
	}

	/**
	 * Asigna el tipo Manual (Tarifario Oficial) asociado al esquema
	 * @param tipoManual
	 */
	public void setTarifarioOficial(InfoDatosInt tipoManual)
	{
		this.tarifarioOficial = tipoManual;
	}

	/**
	 * Retorna el metodo de ajuste asociado al esquema
	 * @return
	 */
	public InfoDatos getMetodoAjuste()
	{
		return metodoAjuste;
	}

	/**
	 * Asigna el metodo de ajuste asociado al esquema
	 * @param metodoAjuste
	 */
	public void setMetodoAjuste(InfoDatos metodoAjuste)
	{
		this.metodoAjuste = metodoAjuste;
	}

	/**
	 * Retorna el codigo del esquema tarifario
	 * @return
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * Asigna el codigo del esquema tarifario
	 * @param codigo
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	/**
	 * Retorna la descripcion del esquema tarifario
	 * @return
	 */
	public String getNombre()
	{
		return nombre;
	}

	/**
	 * Asigna la descripcion del esquema tarifario
	 * @param nombre
	 */
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	/**
	 * Retorna ssi el esquema tarifario es de servicios o de inventarios
	 * @return
	 */
	public boolean isEsInventario()
	{
		return esInventario;
	}

	/**
	 * Asigna si el esquema tarifario es de servicios o de inventarios
	 * @param esInventario
	 */
	public void setEsInventario(boolean esInventario)
	{
		this.esInventario = esInventario;
	}
	
	/**
	 * Inserta el esquema tarifario
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 */
	public ResultadoBoolean insertar(Connection con, String codigoInstitucion) throws SQLException
	{
		return this.esquemaTarifarioDao.insertar(con, nombre, this.tarifarioOficial.getCodigo(), this.metodoAjuste.getAcronimo(), this.esInventario, codigoInstitucion, activo,this.cantidad);
	}
	
	/**
	 * Modifica el esquema tarifario
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 */
	public ResultadoBoolean modificar(Connection con) throws SQLException
	{
		return esquemaTarifarioDao.modificar (con, this.codigo, nombre, this.metodoAjuste.getAcronimo(),this.cantidad, this.activo);
	}
	
	/**
	 * Método que dado un código carga el esquema tarifario asociado
	 * a ese código
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public ResultadoBoolean  cargar (Connection con, int institucion) throws SQLException, BDException
	{
		ResultadoBoolean resp= new ResultadoBoolean (false, "No se encontraron Resultados");

		Collection coll=esquemaTarifarioDao.consultar(con, this.codigo, institucion).getFilasRespuesta();
		Iterator it=coll.iterator();
		if (it.hasNext())
		{
			HashMap dyna=(HashMap )it.next();
			
			this.nombre= (String)dyna.get("nombre");
			this.metodoAjuste.setAcronimo((String)dyna.get("acronimometodoajuste"));
			this.metodoAjuste.setNombre((String)dyna.get("metodoajuste"));
			this.esInventario=UtilidadTexto.getBoolean(dyna.get("esinventario")+"");
			this.activo=UtilidadTexto.getBoolean(dyna.get("activoboolean")+"");
			this.cantidad=Float.parseFloat(dyna.get("cantidad")+"");
			
			
			if(!this.esInventario)
			{
				this.tarifarioOficial.setCodigo(Utilidades.convertirAEntero(""+dyna.get("codigotarifariooficial")));
				this.tarifarioOficial.setNombre( (String) dyna.get("tarifariooficial"));
			}	
			resp.setResultado(true);
		}
		
		if (resp.isTrue())
		{
			resp.setDescripcion("");
		}
		return resp;
	}

	
	/**
	 * Método que dado un código carga el esquema tarifario asociado
	 * a ese código
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public boolean  cargarXcodigo (Connection con, int codigoEsquema, int institucion) throws SQLException, IPSException
	{
		
		try{
			Collection coll=esquemaTarifarioDao.consultar(con, codigoEsquema, institucion).getFilasRespuesta();
			Iterator it=coll.iterator();
			if (it.hasNext())
			{
				HashMap dyna=(HashMap )it.next();
				
				this.codigo=codigoEsquema;
				this.nombre= (String)dyna.get("nombre");
				this.metodoAjuste.setAcronimo((String)dyna.get("acronimometodoajuste"));
				this.metodoAjuste.setNombre((String)dyna.get("metodoajuste"));
				this.esInventario=UtilidadTexto.getBoolean(dyna.get("esinventario")+"");
				this.activo=UtilidadTexto.getBoolean(dyna.get("activoboolean")+"");
				this.cantidad=Float.parseFloat(dyna.get("cantidad")+"");
				
				if(!this.esInventario)
				{
					this.tarifarioOficial.setCodigo( Utilidades.convertirAEntero((dyna.get("codigotarifariooficial"))+""));
					this.tarifarioOficial.setNombre( (String) dyna.get("tarifariooficial"));
				}	
				return true;
			}
			else
				return false;
				
		}		
		catch (IPSException ipsme) {
			throw ipsme;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		
	}
	
	
	
	/**
	 * Consulta los datos del esquema tarifario
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si 
	 * fue exitosa la consulta, false y con la descripción de lo contrario
	 */
	@SuppressWarnings("unused")
	private ResultadoCollectionDB consultar(	Connection con, int institucion	) throws SQLException, BDException
	{
		return this.esquemaTarifarioDao.consultar(con, this.codigo, institucion);	
	}
	
	@SuppressWarnings("unchecked")
	public Collection busqueda(Connection con, int institucion)
	{
	    return esquemaTarifarioDao.busqueda(con, codigo, nombre, tarifarioOficial.getCodigo(), metodoAjuste.getAcronimo(), esInventario, institucion, inventarioAux);
	}
	
	/**
	 * Método para obtener el esquema tarifario que aplica segun el contrato de la entidad subcontratada
	 * @param con
	 * @param codigoContrato
	 * @param servArt
	 * @param fechaCalculoVigencia
	 * @param esServicio
	 * @return
	 */
	public void obtenerEsquemaTarifarioServicioArticuloEntidadSub(Connection con,String codigoContrato,int servArt,String fechaCalculoVigencia,boolean esServicio)
	{
		EsquemaTarifario esqTemp = esquemaTarifarioDao.obtenerEsquemaTarifarioServicioArticuloEntidadSub(con, codigoContrato, servArt, fechaCalculoVigencia, esServicio);
		this.setCodigo(esqTemp.getCodigo());
		this.setNombre(esqTemp.getNombre());
		this.setEsInventario(esqTemp.isEsInventario());
		this.setTarifarioOficial(esqTemp.getTarifarioOficial());
		this.setMetodoAjuste(esqTemp.getMetodoAjuste());
	}

    /**
     * @return Returns the activo.
     */
    public boolean getActivo()
    {
        return activo;
    }
    /**
     * @param activo The activo to set.
     */
    public void setActivo(boolean activo)
    {
        this.activo = activo;
    }
    /**
     * @return Returns the inventarioAux.
     */
    public char getInventarioAux() {
        return inventarioAux;
    }
    /**
     * @param inventarioAux The inventarioAux to set.
     */
    public void setInventarioAux(char inventarioAux) {
        this.inventarioAux = inventarioAux;
    }
	/**
	 * @return Returns the cantidad.
	 */
	public float getCantidad() {
		return cantidad;
	}
	/**
	 * @param cantidad The cantidad to set.
	 */
	public void setCantidad(float cantidad) {
		this.cantidad = cantidad;
	}

	public static ArrayList<DtoEsquemasTarifarios> cargarEsquemasTarifariosServicios() 
	{
		ArrayList<DtoEsquemasTarifarios> resultado=new ArrayList<DtoEsquemasTarifarios>();
		EsquemasTarifariosDelegate dao=new EsquemasTarifariosDelegate();
		ArrayList<EsquemasTarifarios> esquemas= dao.listarEsquemasTarifarios(false,true);
		for(EsquemasTarifarios esquema:esquemas)
		{
			DtoEsquemasTarifarios dto=new DtoEsquemasTarifarios();
			dto.setCodigo(esquema.getCodigo());
			dto.setCantidad(esquema.getCantidad().intValue());
			dto.setEsInventario(esquema.isEsInventario());
			dto.setInstitucion(esquema.getInstituciones().getCodigo());
			dto.setMetodoAjuste(esquema.getMetodosAjuste().getAcronimo()+"");
			dto.setNombre(esquema.getNombre());
			dto.setTarifarioOfocial(esquema.getTarifariosOficiales().getCodigo());
			resultado.add(dto);
		}
		HibernateUtil.endTransaction();
		return resultado;
	}

	public static String obtenerNombreEsquema(int codigoEsquema) 
	{
		EsquemasTarifariosDelegate dao=new EsquemasTarifariosDelegate();
		return (dao.findById(codigoEsquema)).getNombre();
	}
}
