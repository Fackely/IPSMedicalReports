/*
 * Creado en 2/08/2004
 *
 */
package com.princetonsa.mundo.cargos;

import java.sql.Connection;
import java.util.HashMap;
import org.apache.log4j.Logger;

import util.ResultadoBoolean;
import com.princetonsa.dao.ActivacionCargosDao;
import com.princetonsa.dao.DaoFactory;

/**
 * @author Juan David Ramírez López
 *
 * Princeton S.A.
 */
public class ActivacionCargos
{
	private Logger logger=Logger.getLogger(ActivacionCargos.class);
	
	/**
	 * Obtener el acceso a la base de datos
	 * Manejar las transacciones
	 */
	private DaoFactory daoFactory;
	
	/**
	 * Interactuar con la BD
	 */
	private ActivacionCargosDao activacionCargosDao;
	
	/**
	 * Codigo del procedimiento(axioma)
	 */
	private String codigoAxioma;
	
	/**
	 * Descripción del procedimiento
	 */
	private String descripcion;
	
	/**
	 * Cantidad de procedimientos
	 */
	private int cantidad;
	
	/**
	 * Valor del procedimiento
	 */
	private double valor;
	
	/**
	 * Consecutivo de la activacion inactivacion
	 */
	private int codigo;

	/**
	 * Numero de solicitud a la cual se le activo ó inactivo el cargo
	 */
	private int numeroSolicitud;

	/**
	 * Usuario que realizo la modificacion
	 */
	private String usuario;
	
	/**
	 * Si se activa o inactiva el cargo
	 */
	private boolean activacion;
	
	/**
	 * Moitvo de la modificación (activación ó inactivación) 
	 */
	private String motivo;
	
	//********ATRIBUTOS PARA CIRUGIAS********************************
	/**
	 * Objeto que almacena la información de cirugías 
	 */
	private HashMap cirugias = new HashMap();
	/**
	 * Varibale que almacena el número de registros del mapa cirugias
	 */
	private int numCirugias;
	
	/**
	 * Posición del registro cirugía que se desea acitvar/inactivar
	 */
	private int posCirugia;
	
	/**
	 * Posición del registro asocio de la cirugia que se
	 * desea activar/inactivar
	 */
	private int posAsocio;
	
	
	//******************************************************************
	
	/**
	 * @return Returns the posCirugia.
	 */
	public int getPosCirugia() {
		return posCirugia;
	}
	/**
	 * @param posCirugia The posCirugia to set.
	 */
	public void setPosCirugia(int posCirugia) {
		this.posCirugia = posCirugia;
	}
	/**
	 * @return Returns the posAsocio.
	 */
	public int getPosAsocio() {
		return posAsocio;
	}
	/**
	 * @param posAsocio The posAsocio to set.
	 */
	public void setPosAsocio(int posAsocio) {
		this.posAsocio = posAsocio;
	}
	/**
	 * @return Returns the cirugias.
	 */
	public HashMap getCirugias() {
		return cirugias;
	}
	/**
	 * @param cirugias The cirugias to set.
	 */
	public void setCirugias(HashMap cirugias) {
		this.cirugias = cirugias;
	}
	/**
	 * @return Returns the numCirugias.
	 */
	public int getNumCirugias() {
		return numCirugias;
	}
	/**
	 * @param numCirugias The numCirugias to set.
	 */
	public void setNumCirugias(int numCirugias) {
		this.numCirugias = numCirugias;
	}
	/**
	 * @return Retorna codigo.
	 */
	public int getCodigo()
	{
    	return codigo;
	}

	/**
	 * @param codigo Asigna codigo.
	 */
	public void setCodigo(int codigo)
	{
    	this.codigo = codigo;
	}

	/**
	 * @return Retorna numeroSolicitud.
	 */
	public int getNumeroSolicitud()
	{
    	return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud Asigna numeroSolicitud.
	 */
	public void setNumeroSolicitud(int numeroSolicitud)
	{
    	this.numeroSolicitud = numeroSolicitud;
	}
    
	/**
	 * Método que consulta las solicitudes de la sub_cuenta
	 * @param con
	 * @param idSubCuenta
	 * @return
	 */
	public HashMap listar(Connection con, String idSubCuenta)
	{
		return activacionCargosDao.listar(con, idSubCuenta);
	}

	public void init()
	{
		daoFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		if(daoFactory !=null)
		{
			activacionCargosDao=daoFactory.getActivacionCargosDao();
		}
	}
	
	/**
	 * @return Retorna activacion.
	 */
	public boolean getActivacion()
	{
		return activacion;
	}
	
	/**
	 * @param activacion Asigna activacion.
	 */
	public void setActivacion(boolean activacion)
	{
		this.activacion = activacion;
	}
	
	/**
	 * @return Retorna activacionCargosDao.
	 */
	public ActivacionCargosDao getActivacionCargosDao()
	{
		return activacionCargosDao;
	}
	
	/**
	 * @param activacionCargosDao Asigna activacionCargosDao.
	 */
	public void setActivacionCargosDao(ActivacionCargosDao activacionCargosDao)
	{
		this.activacionCargosDao = activacionCargosDao;
	}
	
	/**
	 * @return Retorna motivo.
	 */
	public String getMotivo()
	{
		return motivo;
	}
	
	/**
	 * @param motivo Asigna motivo.
	 */
	public void setMotivo(String motivo)
	{
		this.motivo = motivo;
	}
	
	/**
	 * @return Retorna usuario.
	 */
	public String getUsuario()
	{
		return usuario;
	}
	
	/**
	 * @param usuario Asigna usuario.
	 */
	public void setUsuario(String usuario)
	{
		this.usuario = usuario;
	}

	/**
	 * Cargar el detalle de una solicitud
	 * @param con
	 */
	public HashMap<String, Object> detallarSolicitud(
		Connection con,
		String idSubCuenta,
		String numeroSolicitud,
		int tipoSolicitud,
		String codigoServicioCx,
		String tipoAsocio,
		boolean conMotivo,
		String tipoServicioAsocio,
		String consecutivoAsocio,
		String esPortatil)
	{
		HashMap campos = new HashMap();
		campos.put("idSubCuenta", idSubCuenta);
		campos.put("numeroSolicitud", numeroSolicitud);
		campos.put("codigoTipoSolicitud", tipoSolicitud);
		campos.put("conMotivo", conMotivo); //para saber si se debe consultar el motivo
		campos.put("codigoServicioCx", codigoServicioCx);
		campos.put("tipoAsocio", tipoAsocio);
		campos.put("tipoServicioAsocio", tipoServicioAsocio);
		campos.put("consecutivoAsocio", consecutivoAsocio);
		campos.put("esPortatil", esPortatil);
		return activacionCargosDao.detallarSolicitud(con, campos);
		
	}
	
	
	/**
	 * Método que realiza la activacion/inactivacion de un cargo, teniendo en cuenta que cada inactivacion 
	 * requiere la creación de un registro de cargo inactivo dejando el registro activo con la cantidad restante
	 * o viceversa, debido a que la activacion de cargos se hace por cantidad
	 * @param con
	 * @param codigoCargo
	 * @param codSolSubcuenta
	 * @param codigoEstadoCargo
	 * @param cantidadActual
	 * @param cantidadNueva
	 * @param valorUnitario
	 * @param loginUsuario
	 * @param idIngreso,
	 * @param numeroSolicitud,
	 * @param tipoSolicitud
		@param codigoConvenio,
		@param motivo,
		@param codigoServicio,
		@param tipoASocio,
	 * @param paquetizado
	 * @param existeOtroCargo
	 * @param codigoOtroCargo
	 * @param codSolSubcuentaOtro
	 * @param cantidadOtroCargo
	 * @param esPortatil 
	 * @return
	 */
	public ResultadoBoolean activarInactivarCargo(Connection con,
		String codigoCargo,
		String codSolSubcuenta,
		int codigoEstadoCargo,
		int cantidadActual,
		int cantidadNueva,
		double valorUnitario,
		String loginUsuario,
		String numeroSolicitud,
		int tipoSolicitud,
		String idIngreso,
		String codigoConvenio,
		String motivo,
		String codigoServicio,
		String tipoAsocio,
		String paquetizado,
		boolean existeOtroCargo,
		String codigoOtroCargo,
		String codSolSubcuentaOtro,
		int cantidadOtroCargo,
		String tipoServicioAsocio,
		String consecutivoAsocio
		)
	{
		HashMap campos = new HashMap();
		campos.put("codigoCargo", codigoCargo);
		campos.put("codSolSubcuenta", codSolSubcuenta);
		campos.put("codigoEstadoCargo", codigoEstadoCargo);
		campos.put("cantidadActual", cantidadActual);
		campos.put("cantidadNueva", cantidadNueva);
		campos.put("valorUnitario", valorUnitario);
		campos.put("loginUsuario", loginUsuario);
		campos.put("numeroSolicitud", numeroSolicitud);
		campos.put("tipoSolicitud", tipoSolicitud);
		campos.put("idIngreso", idIngreso);
		campos.put("codigoConvenio", codigoConvenio);
		campos.put("motivo", motivo);
		campos.put("codigoServicio", codigoServicio);
		campos.put("tipoAsocio", tipoAsocio);
		campos.put("paquetizado", paquetizado);
		campos.put("existeOtroCargo", existeOtroCargo);
		campos.put("codigoOtroCargo", codigoOtroCargo);
		campos.put("codSolSubcuentaOtro", codSolSubcuentaOtro);
		campos.put("cantidadOtroCargo", cantidadOtroCargo);
		campos.put("tipoServicioAsocio", tipoServicioAsocio);
		campos.put("consecutivoAsocio", consecutivoAsocio);
		return activacionCargosDao.activarInactivarCargo(con, campos);
	}
	
	
	/**
	 * Método que realiza la consulta de los cargos de una solicitud cirugia para una subcuenta específica
	 * @param con
	 * @param numeroSolicitud
	 * @param idSubCuenta
	 * @return
	 */
	public HashMap<String,Object> consultarDetalleCargosCirugia(Connection con,String numeroSolicitud,String idSubCuenta)
	{
		HashMap campos = new HashMap();
		campos.put("numeroSolicitud", numeroSolicitud);
		campos.put("idSubCuenta", idSubCuenta);
		return activacionCargosDao.consultarDetalleCargosCirugia(con, campos);
	}
	
	/**
	 * Método que realiza la consulta de las inactivaciones de cargos
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public HashMap<String, Object> consultarInactivacionesCargos(Connection con,String idIngreso,String codigoConvenio)
	{
		HashMap campos = new HashMap();
		campos.put("idIngreso",idIngreso);
		campos.put("codigoConvenio",codigoConvenio);
		return activacionCargosDao.consultarInactivacionesCargos(con, campos);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * @return Retorna codigoAxioma.
	 */
	public String getCodigoAxioma()
	{
		return codigoAxioma;
	}
	
	/**
	 * @param codigoAxioma Asigna codigoAxioma.
	 */
	public void setCodigoAxioma(String codigoAxioma)
	{
		this.codigoAxioma = codigoAxioma;
	}
	
	/**
	 * @return Retorna cantidad.
	 */
	public int getCantidad()
	{
		return cantidad;
	}
	
	/**
	 * @param cantidad Asigna cantidad.
	 */
	public void setCantidad(int cantidad)
	{
		this.cantidad = cantidad;
	}
	
	/**
	 * @return Retorna valor.
	 */
	public double getValor()
	{
		return valor;
	}
	
	/**
	 * @param valor Asigna valor.
	 */
	public void setValor(double valor)
	{
		this.valor = valor;
	}
	
	/**
	 * @return Retorna descripcion.
	 */
	public String getDescripcion()
	{
		return descripcion;
	}
	
	/**
	 * @param descripcion Asigna descripcion.
	 */
	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}

	
	


}
