/*
 * TarifasInventario.java 
 * Autor			:  Juan David Ramírez
 * Creado el	:  01-sep-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.mundo.cargos;


import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import java.util.HashMap;

import util.UtilidadTexto;


import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TarifasInventarioDao;

/**
 * 
 * @author Juan David Ramírez López
 *
 * Princeton S.A.
 */
public class TarifasInventario {
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private TarifasInventarioDao tarifasInventarioDao;

	/**
	 * Código del esquema tarifario
	 */
	private int codigoEsquemaTarifario;
	
	/**
	 * Código de registro
	 */
	private int codigo;
	
	/**
	 * Código del articulo seleccionado
	 */
	private int codigoArticulo;
	
	/**
	 * Valor de la tarifa
	 */
	private double valorTarifa;
	
	/**
	 * Porcentaje del iva
	 */
	private double porcentajeIva;
	
	/**
	 * Porcentaje de la tarifa
	 */
	private double porcentaje;

	/**
	 * Campo q indica si la tarifa es actualizable
	 */
	private String actualizAutomatic;
	
	/**
	 * El tipo de tarifa de inventario a seleccionar
	 */
	private String tipoTarifa;
	
	/**
	 * Usuario q inserto o modifico la tarifa de inventario
	 */
	private String usuarioModifica;
	
	/**
	 * Fecha de ingreso o modificacion de la tarifa inventario
	 */
	private String fechaModifica;
	
	/**
	 * Hora de insercion o modificacion de la tarifa inventario
	 */
	private String horaModifica;
	
	/**
	 * Fecha de vigencia de la tarifa
	 */
	private String fechaVigencia;
	
	
	
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( this.tarifasInventarioDao == null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			this.tarifasInventarioDao = myFactory.getTarifasInventarioDao();
			if( this.tarifasInventarioDao != null )
				return true;
		}

		return false;
	}
	
	/**
	 * Metodo para resetear
	 *
	 */
	public void reset(){
		this.codigo = 0;
		this.codigoEsquemaTarifario = 0;
		this.codigoArticulo = 0;
		this.valorTarifa = 0;
		this.porcentajeIva = 0;
		this.porcentaje= 0;
		this.actualizAutomatic="";
		this.tipoTarifa="";
		this.usuarioModifica="";
		this.fechaModifica="";
		this.horaModifica="";
		this.fechaVigencia="";
		
	}

	/**
	 * Constructor por defecto
	 */
	public TarifasInventario()
	{
		reset();
		init(System.getProperty("TIPOBD"));
	}

	/**
	 * 
	 * @param codigo
	 * @param codigoEsquemaTarifario
	 * @param codigoArticulo
	 * @param valorTarifa
	 * @param porcentajeIva
	 * @param porcentaje
	 * @param actualizAutomatic
	 * @param tipoTarifa
	 * constructor por defecto
	 */
	public TarifasInventario(int codigo, int codigoEsquemaTarifario, int codigoArticulo, double valorTarifa, double porcentajeIva, double porcentaje, String actualizAutomatic, String tipoTarifa)
	{
		reset();
		this.codigo = codigo;
		this.codigoEsquemaTarifario = codigoEsquemaTarifario;
		this.codigoArticulo = codigoArticulo;
		this.valorTarifa = valorTarifa;
		this.porcentajeIva = porcentajeIva;
		this.porcentaje = porcentaje;
		this.actualizAutomatic = actualizAutomatic;
		this.tipoTarifa = tipoTarifa;
		init(System.getProperty("TIPOBD"));
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	public int insertar(Connection con)
	{
		return tarifasInventarioDao.insertar( con, this.codigoEsquemaTarifario, this.codigoArticulo, this.valorTarifa, this.porcentajeIva, this.porcentaje, this.actualizAutomatic, this.tipoTarifa, this.usuarioModifica, this.fechaModifica, this.horaModifica, this.fechaVigencia );
	}
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public int modificar(Connection con)
	{
		return tarifasInventarioDao.modificar( con, this.codigo, this.valorTarifa, this.porcentajeIva, this.porcentaje, this.actualizAutomatic, this.tipoTarifa, this.usuarioModifica, this.fechaModifica, this.horaModifica, this.codigoEsquemaTarifario, this.fechaVigencia );
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	public int eliminar(Connection con)
	{
		return tarifasInventarioDao.eliminar( con, this.codigo);
	}

	/**
	 * Método para cargar una tarifa de inventario dado el código del articulo
	 * y el esquema tarifario
	 * @param con conexión con la BD
	 * @param codigoArticulo Código del articulo 
	 * @param esquemaTarifario Código del esquema tarifario
	 * @return true si se cargó correctamente
	 */
	public boolean cargar(Connection con, int articulo, int esquemaTarifario)
	{
		this.codigoArticulo=articulo;
		this.codigoEsquemaTarifario=esquemaTarifario;
		return this.cargar(con);
	}


	/**
	 * Método para cargar una tarifa de inventario tomando el código del articulo
	 * y del esquema tarifario de los atributos propios
	 * @param con Conexión con la BD
	 * @return true si se cargó correctamente
	 */
	private boolean cargar(Connection con)
	{
		if(this.codigoArticulo!=0 && this.codigoEsquemaTarifario!=0)
		{
			Iterator iterador=tarifasInventarioDao.cargar(con, this.codigoArticulo, this.codigoEsquemaTarifario).iterator();
			if(iterador.hasNext())
			{
				HashMap tarifa=(HashMap)iterador.next();
				this.codigo=Integer.parseInt(tarifa.get("codigo")+"");
				this.valorTarifa=Double.parseDouble(tarifa.get("valor")+"");
				this.porcentajeIva=Double.parseDouble(tarifa.get("iva")+"");
				this.porcentaje=Double.parseDouble(UtilidadTexto.isEmpty(tarifa.get("porcentaje")+"")?"0":tarifa.get("porcentaje")+"");
				this.actualizAutomatic=tarifa.get("actualizautomatic")+"";
				this.tipoTarifa=tarifa.get("tipotarifa")+"";
				this.usuarioModifica = tarifa.get("usuario_modifica").toString();
				this.fechaModifica = tarifa.get("fecha_modifica").toString();
				this.horaModifica = tarifa.get("hora_modifica").toString();
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param con
	 * @param articulo
	 * @param descripcionArticulo
	 * @param esquemaTarifario
	 * @param tarifa
	 * @param iva
	 * @param porcentaje
	 * @param actualizAutomatic
	 * @param tipoTarifa
	 * @return
	 */
	public Collection buscar(Connection con, String articulo, String descripcionArticulo, String naturalezaArticulo, String formaFarmaceutica, String concentracionArticulo, int esquemaTarifario, double tarifa, double iva, double porcentaje, String actualizAutomatic, String tipoTarifa, int institucion, String remite)
	{
		return tarifasInventarioDao.buscar(con, articulo, descripcionArticulo, naturalezaArticulo, formaFarmaceutica, concentracionArticulo, esquemaTarifario, tarifa, iva, porcentaje, actualizAutomatic, tipoTarifa, institucion, remite);
	}
	
	/**
	 * Metodo que consulta fechas vigencia inventario por esquema articulo
	 * @param con
	 * @param esquemaTarifario
	 * @param articulo
	 * @param codigosArticulos
	 * @return
	 */
	public HashMap consultarFechasVigencia(Connection con, String esquemaTarifario, String articulo, String cadenaCodigosArticulos)
	{
		return tarifasInventarioDao.consultarFechasVigencia(con, esquemaTarifario, articulo, cadenaCodigosArticulos);
	}
	
	/**
	 * Metodo que consulta todas las tarifas inventarios por esquema articulo
	 * @param con
	 * @param esquemaTarifario
	 * @param codArticulo
	 * @return
	 */
	public HashMap consultarTodasTarifasInventarios(Connection con, String esquemaTarifario, String codArticulo)
	{
		return tarifasInventarioDao.consultarTodasTarifasInventarios(con, esquemaTarifario, codArticulo);
	}
	
//----------------------------------------------- Getters And Setters--------------------------------------
	
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
	 * @return Retorna codigoArticulo.
	 */
	public int getCodigoArticulo()
	{
		return codigoArticulo;
	}
	/**
	 * @param codigoArticulo Asigna codigoArticulo.
	 */
	public void setCodigoArticulo(int codigoArticulo)
	{
		this.codigoArticulo = codigoArticulo;
	}
	/**
	 * @return Retorna codigoEsquemaTarifario.
	 */
	public int getCodigoEsquemaTarifario()
	{
		return codigoEsquemaTarifario;
	}
	/**
	 * @param codigoEsquemaTarifario Asigna codigoEsquemaTarifario.
	 */
	public void setCodigoEsquemaTarifario(int codigoEsquemaTarifario)
	{
		this.codigoEsquemaTarifario = codigoEsquemaTarifario;
	}
	/**
	 * @return Retorna porcentajeIva.
	 */
	public double getPorcentajeIva()
	{
		return porcentajeIva;
	}
	/**
	 * @param porcentajeIva Asigna porcentajeIva.
	 */
	public void setPorcentajeIva(double porcentajeIva)
	{
		this.porcentajeIva = porcentajeIva;
	}
	/**
	 * @return Retorna valorTarifa.
	 */
	public double getValorTarifa()
	{
		return valorTarifa;
	}
	/**
	 * @param valorTarifa Asigna valorTarifa.
	 */
	public void setValorTarifa(double valorTarifa)
	{
		this.valorTarifa = valorTarifa;
	}

	/**
	 * @return the actualizAutomatic
	 */
	public String getActualizAutomatic() {
		return actualizAutomatic;
	}

	/**
	 * @param actualizAutomatic the actualizAutomatic to set
	 */
	public void setActualizAutomatic(String actualizAutomatic) {
		this.actualizAutomatic = actualizAutomatic;
	}


	/**
	 * @return the porcentaje
	 */
	public double getPorcentaje() {
		return porcentaje;
	}


	/**
	 * @param porcentaje the porcentaje to set
	 */
	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}


	/**
	 * @return the tipoTarifa
	 */
	public String getTipoTarifa() {
		return tipoTarifa;
	}


	/**
	 * @param tipoTarifa the tipoTarifa to set
	 */
	public void setTipoTarifa(String tipoTarifa) {
		this.tipoTarifa = tipoTarifa;
	}


	/**
	 * @return the fechaModifica
	 */
	public String getFechaModifica() {
		return fechaModifica;
	}


	/**
	 * @param fechaModifica the fechaModifica to set
	 */
	public void setFechaModifica(String fechaModifica) {
		this.fechaModifica = fechaModifica;
	}


	/**
	 * @return the horaModifica
	 */
	public String getHoraModifica() {
		return horaModifica;
	}


	/**
	 * @param horaModifica the horaModifica to set
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}


	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}


	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the fechaVigencia
	 */
	public String getFechaVigencia() {
		return fechaVigencia;
	}

	/**
	 * @param fechaVigencia the fechaVigencia to set
	 */
	public void setFechaVigencia(String fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}

}
