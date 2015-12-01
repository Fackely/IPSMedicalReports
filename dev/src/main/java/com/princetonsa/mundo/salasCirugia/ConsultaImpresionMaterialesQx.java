/*
 * Oct 4, 2005
 *
 */
package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;

import util.ConstantesBD;
import util.InfoDatos;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.ConsultaImpresionMaterialesQxDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.MaterialesQxDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Luis Gabriel Chavez Salazar
 *
 * Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Consulta Materiales Quirúrgicos
 */
public class ConsultaImpresionMaterialesQx 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(MaterialesQx.class);
	
	/**
	 * DAO para el manejo de MaterialesQxDao
	 */ 
	private ConsultaImpresionMaterialesQxDao consultaImpresionMaterialesDao=null;
	
		//**************CONSTRUCTORES E INICIALIZADORES********************
	/**
	 * Constructor
	 */
	public ConsultaImpresionMaterialesQx() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (consultaImpresionMaterialesDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			consultaImpresionMaterialesDao = myFactory.getConsultaImpresionMaterialesQxDao();
		}	
	}
	
	/**
	 * 
	 * @param con
	 * @param criterios
	 * @param espaciente
	 * @return
	 */
	public static HashMap consultaIngresosPaciente(Connection con, HashMap criterios){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaImpresionMaterialesQxDao().consultaIngresosPaciente(con, criterios);	
		}
	
	/**
	 * 
	 * @param con
	 * @param criterios
	 * @return
	 */
	public HashMap cargarSolicitudesCx(Connection con, HashMap criterios, boolean espaciente) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaImpresionMaterialesQxDao().cargarSolicitudesCx(con, criterios,espaciente);
	}
	
	/**
	 * 
	 * @param con
	 * @param criterios
	 * @param esacto
	 * @return
	 */
	public HashMap cargarConsumoMaterialesCirugias (Connection con, HashMap criterios, boolean esacto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaImpresionMaterialesQxDao().cargarConsumoMaterialesCirugias(con,criterios,esacto);
	}
	
}
