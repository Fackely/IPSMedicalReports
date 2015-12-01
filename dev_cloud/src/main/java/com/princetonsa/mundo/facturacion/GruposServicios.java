/*
 * Ene 18, 2006
 * Modificacion Nov 16, 2006 FC
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.GruposServiciosDao;



/**
 * @author Sebastián Gómez 
 *
 *Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Parametrización de Grupos de Servicios
 */
public class GruposServicios 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(GruposServicios.class);
	
	/**
	 * DAO para el manejo de GruposServiciosDao
	 */
	private GruposServiciosDao gruposDao=null;
	
	//**********ATRIBUTOS********************************************
	
	/**
	 * Código Axioma del registro
	 */
	private int codigo;
	
	/**
	 * Abreviatura del Grupo Servicio
	 */
	private String acronimo;
	
	/**
	 * Descripción del Grupo Servicio
	 */
	private String descripcion;
	
	/**
	 * indicador de activo/inactivo
	 */
	private String activo;

	/**
	 * indicador de marca multiple
	 */
	private String multiple;
	
	/**
	 * Indicador de manejo de Laboratorio
	 */
	private String tipo;
	
	/**
	 * Tipo de Sala
	 */
	private String tipoSalaStandar;
	
	/**
	 * Numero de Dias Urgente
	 */
	private String numDiasUrgente;
	
	/**
	 * Acronimo Dias Urgente
	 */
	private String acroDiasUrgente;
	
	/**
	 * Numero de Dias Normal
	 */
	private String numDiasNormal;
	
	/**
	 * Acronimo Dias Normal
	 */
	private String acroDiasNormal;
	
	/**
	 * Tipo de Afiliado
	 */
	private int tipoMonto;
	
	
	
	//*********************************************************************
	//*****************CONSTRUCTOR E INICIALIZADORES***********************
	/**
	 * Constructor
	 */
	public GruposServicios() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.codigo = 0;
		this.descripcion = "";
		this.acronimo = "";
		this.activo = "";
		this.multiple = "";
		this.tipo = "";
		this.setTipoSalaStandar("");
		this.tipoMonto=ConstantesBD.codigoNuncaValido;
		this.numDiasNormal="";
		this.numDiasUrgente="";
		this.acroDiasNormal="";
		this.acroDiasUrgente="";
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (gruposDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			gruposDao = myFactory.getGruposServiciosDao();
		}	
	}
	//****************************************************************************
	//********************MÉTODOS**************************************************
	
	/**
	 * Método implementado para consultar los grupos de servicios
	 * de una institucion
	 * @param con
	 * @return
	 */
	public HashMap consultarGrupos(Connection con, int codIntitucion)
	{
		return gruposDao.consultarGrupos(con, codIntitucion );
	}
	
	/**
	 * Método usado para realizar una búsqueda avanzada de
	 * grupos de servicios
	 * @param con
	 * @return
	 */
	public HashMap busquedaGrupos(Connection con)
	{
		return gruposDao.busquedaGrupos(con,this.codigo,this.acronimo,this.descripcion,this.activo, this.multiple, this.tipo, this.tipoSalaStandar,
							this.numDiasUrgente, this.acroDiasUrgente, this.numDiasNormal, this.acroDiasNormal, this.tipoMonto);
	}
	
	/**
	 * Método implementado para insertar un registro de
	 * grupos de servicios
	 * @param con
	 * @return si la transacción es exitosa retorna el consecutivo del registro.
	 */
	public int insertar(Connection con, int codIntitucion)
	{
		return gruposDao.insertar(
				con,
				this.acronimo,
				this.descripcion,
				UtilidadTexto.getBoolean(this.activo),
				UtilidadTexto.getBoolean(this.multiple), 
				this.tipo, 
				codIntitucion,
				this.tipoSalaStandar,
				this.numDiasUrgente,
				this.acroDiasUrgente,
				this.numDiasNormal,
				this.acroDiasNormal,
				this.tipoMonto);
	}
	
	/**
	 * Método implementado para modificar un registro de
	 * grupos de servicios
	 * 
	 * @param con
	 * @return
	 */
	public int modificar(Connection con)
	{
		return gruposDao.modificar(
				con,
				this.descripcion,
				UtilidadTexto.getBoolean(activo),
				this.codigo,
				UtilidadTexto.getBoolean(multiple), 
				this.tipo,
				this.tipoSalaStandar,
				this.numDiasUrgente,
				this.acroDiasUrgente,
				this.numDiasNormal,
				this.acroDiasNormal,
				this.tipoMonto);
	}
	
	/**
	 * Método implementado para eliminar un registro de grupos de servicios
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminar(Connection con)
	{
		return gruposDao.eliminar(con,this.codigo);
	}
	
	
	//****************************************************************************
	//*******************GETTERS & SETTERS*****************************************
	
	/**
	 * @return Returns the acronimo.
	 */
	public String getAcronimo() {
		return acronimo;
	}
	/**
	 * @param acronimo The acronimo to set.
	 */
	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}
	/**
	 * @return Returns the activo.
	 */
	public String getActivo() {
		return activo;
	}
	/**
	 * @param activo The activo to set.
	 */
	public void setActivo(String activo) {
		this.activo = activo;
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
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getMultiple() {
		return multiple;
	}
	
	/**
	 * 
	 * @param multiple
	 */
	public void setMultiple(String multiple) {
		this.multiple = multiple;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTipo() {
		return tipo;
	}
	
	/**
	 * 
	 * @param tipo
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	/**
	 * Método encargado de obtener los tipos de salas con es_quirurgica = true
	 * @param Coneection con
	 * @param int codInstitucion
	 * @return HashMap con los resultados de la obtención de los tipos de salas con es_quirurgica = true
	 */
	public HashMap obtenerListaSalas (Connection con, int codInstitucion)
	{
		logger.info("===> Entré a obtenerListaSalas ");
		return gruposDao.obtenerListaSalas(con, codInstitucion);
	}
	
	/**
	 * Método encargado de obtener los tipos de montos
	 * @param Coneection con
	 * @return HashMap 
	 */
	
	public HashMap obtenerListaTiposMontos(Connection con)
	{
		return gruposDao.obtenerListaTiposMontos(con);
	}
	
	public void setTipoSalaStandar(String tipoSalaStandar) {
		this.tipoSalaStandar = tipoSalaStandar;
	}
	public String getTipoSalaStandar() {
		return tipoSalaStandar;
	}
	/**
	 * @return the numDiasUrgente
	 */
	public String getNumDiasUrgente() {
		return numDiasUrgente;
	}
	/**
	 * @param numDiasUrgente the numDiasUrgente to set
	 */
	public void setNumDiasUrgente(String numDiasUrgente) {
		this.numDiasUrgente = numDiasUrgente;
	}
	/**
	 * @return the acroDiasUrgente
	 */
	public String getAcroDiasUrgente() {
		return acroDiasUrgente;
	}
	/**
	 * @param acroDiasUrgente the acroDiasUrgente to set
	 */
	public void setAcroDiasUrgente(String acroDiasUrgente) {
		this.acroDiasUrgente = acroDiasUrgente;
	}
	/**
	 * @return the numDiasNormal
	 */
	public String getNumDiasNormal() {
		return numDiasNormal;
	}
	/**
	 * @param numDiasNormal the numDiasNormal to set
	 */
	public void setNumDiasNormal(String numDiasNormal) {
		this.numDiasNormal = numDiasNormal;
	}
	/**
	 * @return the acroDiasNormal
	 */
	public String getAcroDiasNormal() {
		return acroDiasNormal;
	}
	/**
	 * @param acroDiasNormal the acroDiasNormal to set
	 */
	public void setAcroDiasNormal(String acroDiasNormal) {
		this.acroDiasNormal = acroDiasNormal;
	}
	/**
	 * @return the tipoMonto
	 */
	public int getTipoMonto() {
		return tipoMonto;
	}
	/**
	 * @param tipoMonto the tipoMonto to set
	 */
	public void setTipoMonto(int tipoMonto) {
		this.tipoMonto = tipoMonto;
	}
	
	
}
