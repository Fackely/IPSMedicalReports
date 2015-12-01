package com.princetonsa.mundo;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dao.CamasDao;
import com.princetonsa.dao.DaoFactory;

/**
 * @author l-caball
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Camas
{
	private ArrayList listadoCamas;
	
	private CamasDao camasDao;
	
	private int indiceCamaOcupada;
	
	/**
	 * Para la búsqueda
	 * numero de cama a buscar
	 */
	private String numeroCama;
	
	/**
	 * Para la búsqueda
	 * rango inicial Fecha de traslado del paciente
	 */
	private String fechaTrasladoInicial;

	/**
	 * Para la búsqueda
	 * rango final Fecha de traslado del paciente
	 */
	private String fechaTrasladoFinal;
	
	/**
	 * Para la búsqueda
	 * rango inicial Hora del traslado del paciente
	 */
	private String horaTrasladoInicial;
	
	/**
	 * Para la búsqueda
	 * rango Final Hora del traslado del paciente
	 */
	private String horaTrasladoFinal;
	
	/**
	 * Para la búsqueda
	 * Usuario que hizo el traslado
	 */
	private String usuario;

	/**
	 * cod del paciente para la busqueda 
	 * de traslado de camas por paciente cargado
	 */
	private int codigoPaciente;
	
	public Camas()
	{
		listadoCamas = new ArrayList();
		this.init(System.getProperty("TIPOBD"));
	}
	
	public void init (String tipoBD) 
	{
		if (camasDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			camasDao = myFactory.getCamasDao();
		}
	}

	/**
	 * Returns the listadoCamas.
	 * @return ArrayList
	 */
	public ArrayList getListadoCamas()
	{
		return listadoCamas;
	}

	/**
	 * Sets the listadoCamas.
	 * @param listadoCamas The listadoCamas to set
	 */
	public void setListadoCamas(ArrayList listadoCamas)
	{
		this.listadoCamas = listadoCamas;
	}

	
	
	private Cama cargarCama(int codCama)
	{
		int tam = this.listadoCamas.size();
		
		Cama camaTemp = null;
		
		for(int i=0; i<tam; i++)
		{
			camaTemp = (Cama)listadoCamas.get(i);
			if( Integer.parseInt(camaTemp.getCodigoCama()) == codCama )
			{
				this.indiceCamaOcupada = i;
				return camaTemp;
			}
		}
		
		return camaTemp;
	}
	
	/**
	 * Método que contiene los resultados de la búsqueda de traslados de camas,
	 * según los criterios dados en la búsqueda avanzada. 
	 * @param con
	 * @param numeroCama
	 * @param fechaInicialTraslado
	 * @param fechaFinalTraslado
	 * @param horaInicialTraslado
	 * @param horaFinalTraslado
	 * @param usuario
	 * @return
	 */	
	public Collection resultadoBusquedaTrasladoCamas(Connection con)
	{
		Collection resultado=camasDao.consultaTrasladoCamas(	 con, 
																										 numeroCama, 
																										 fechaTrasladoInicial, 
																										 fechaTrasladoFinal,
																										 horaTrasladoInicial, 
																										 horaTrasladoFinal,
																										 usuario);
			return resultado;
	}
	
	public void resetBusqueda()
	{
		this.numeroCama="";
		this.fechaTrasladoInicial="";
		this.fechaTrasladoFinal="";
		this.horaTrasladoInicial="";
		this.horaTrasladoFinal="";
		this.usuario="";
	}

	/**
	 * Método que carga los links pertenecientes a la consulta de traslado 
	 * con búsqueda por  paciente
	 * @param con, conexión con la fuente de datos
	 * @param codigoPaciente, código del paciente cargado en la sesión
	 * @return
	 * @throws SQLException
	 */
	public Collection linksConsultaTrasladoCamasPorPaciente(Connection con, int codigoPaciente) throws SQLException
	{	
		Collection resultado=camasDao.linksConsultaTrasladoCamasPorPaciente(con,codigoPaciente);
		return resultado;
	}

	/**
	 * Método que obtiene los datos pertinentes desde el link 
	 * see=linksConsultaTrasladoCamasPorPaciente para cargar 
	 * los datos de la consulta de traslado de camas
	 * @param con, conexión con la fuente de datos
	 * @param cuenta, cuenta del paciente
	 * @return
	 */
	public Collection busquedaConsultaTrasladoCamasPorPaciente (	Connection con,	int cuenta ) throws SQLException 
	{	
		Collection resultado=camasDao.busquedaConsultaTrasladoCamasPorPaciente(con,cuenta);
		return resultado;
	}

	/**
	 * @return rango final Fecha de traslado del paciente
	 */
	public String getFechaTrasladoFinal()
	{
		return fechaTrasladoFinal;
	}

	/**
	 * @return rango inicial Hora de traslado del paciente
	 */
	public String getHoraTrasladoInicial()
	{
		return horaTrasladoInicial;
	}

	/**
	 * @return Número de Cama
	 */
	public String getNumeroCama()
	{
		return numeroCama;
	}

	/**
	 * @return Usuario que hizo el traslado
	 */
	public String getUsuario()
	{
		return usuario;
	}

	/**
	 * Asignar el rango final de la fecha de traslado del paciente
	 * @param string
	 */
	public void setFechaTrasladoFinal(String string)
	{
		fechaTrasladoFinal = string;
	}

	/**
	 * Asigna rango inicial la hora de traslado del paciente
	 * @param string
	 */
	public void setHoraTrasladoInicial(String string)
	{
		horaTrasladoInicial = string;
	}

	/**
	 * Asigna el número de Cama
	 * @param string
	 */
	public void setNumeroCama(String string)
	{
		numeroCama = string;
	}

	/**
	 * Asigna el usuario que hizo el traslado
	 * @param string
	 */
	public void setUsuario(String string)
	{
		usuario = string;
	}

	/**
	 * @return el rango de fecha inicial del traslado
	 */
	public String getFechaTrasladoInicial() {
		return fechaTrasladoInicial;
	}

	/**
	 * @return la hora final del rango de búsqueda
	 */
	public String getHoraTrasladoFinal() {
		return horaTrasladoFinal;
	}

	/**
	 * Asigna la fecha inicial en un rango de búsqueda
	 * @param string
	 */
	public void setFechaTrasladoInicial(String string) {
		fechaTrasladoInicial = string;
	}

	/**
	 * Asigna la hora final en un rango de búsqueda
	 * @param string
	 */
	public void setHoraTrasladoFinal(String string) {
		horaTrasladoFinal = string;
	}


	/**
	 * Retorna cod del paciente para la busqueda 
	 * de traslado de camas por paciente cargado
	 * @return
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 *  Asigna cod del paciente para la busqueda 
	 * de traslado de camas por paciente cargado
	 * @param i
	 */
	public void setCodigoPaciente(int i) {
		codigoPaciente = i;
	}

}
