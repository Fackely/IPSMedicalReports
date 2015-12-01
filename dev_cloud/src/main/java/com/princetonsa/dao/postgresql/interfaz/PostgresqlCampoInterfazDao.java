/*
 * Creado en Apr 26, 2006
 * Andr�s Mauricio Ruiz V�lez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.dao.postgresql.interfaz;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dao.interfaz.CampoInterfazDao;
import com.princetonsa.dao.sqlbase.interfaz.SqlBaseCampoInterfazDao;

public class PostgresqlCampoInterfazDao implements CampoInterfazDao
{
	/**
	 * M�todo para cargar la informaci�n general de la parametrizaci�n de campos interfaz 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public Collection cargarInformacionGral(Connection con, int institucion)
	{
		return SqlBaseCampoInterfazDao.cargarInformacionGral (con, institucion);
	}
	
	/**
	 * M�todo para guardar la informaci�n general de la parametrizaci�n de los campos de interfaz
	 * @param con
	 * @param esInsertar
	 * @param institucion
	 * @param nombreArchivoSalida
	 * @param pathArchivoSalida
	 * @param nombreArchivoInconsistencias
	 * @param pathArchivoInconsistencias
	 * @param separadorCampos
	 * @param separadorDecimales
	 * @param identificadorFinArchivo
	 * @param presentaDevolucionPaciente
	 * @param valorNegativoDevolPaciente
	 * @param descripcionDebito
	 * @param descripcionCredito
	 * @return
	 */
		public int insertarActualizarInfoGral(Connection con, boolean esInsertar, int institucion, String nombreArchivoSalida, 
																			String pathArchivoSalida, String nombreArchivoInconsistencias, String pathArchivoInconsistencias, 
																			String separadorCampos, int separadorDecimales, String identificadorFinArchivo, String presentaDevolucionPaciente, 
																			String valorNegativoDevolPaciente, String descripcionDebito, String descripcionCredito)
		{
			return SqlBaseCampoInterfazDao.insertarActualizarInfoGral (con, esInsertar, institucion, nombreArchivoSalida, pathArchivoSalida, 
																															nombreArchivoInconsistencias, pathArchivoInconsistencias, separadorCampos, 
																															separadorDecimales, identificadorFinArchivo, presentaDevolucionPaciente, valorNegativoDevolPaciente, 
																															descripcionDebito, descripcionCredito);
		}
		
		/**
		 *  M�todo para consultar los campos de interfaz parametrizados hasta el momento para 
		 * la instituci�n
		 * @param con
		 * @param institucion
		 * @return
		 */
		public HashMap consultarCamposInterfaz(Connection con, int institucion)
		{
			return SqlBaseCampoInterfazDao.consultarCamposInterfaz (con, institucion);
		}
		
		/**
		 * M�todo para insertar/actualizar la informaci�n ingresada en la segunda secci�n
		 * de campos de interfaz
		 * @param con
		 * @param esInsertar
		 * @param institucion
		 * @param tipoCampoInterfaz
		 * @param ordenCampo
		 * @param indicativoRequerido
		 * @param ordenCampoAnt
		 * @return
		 */
		public int insertarActualizarCamposInterfaz(Connection con, boolean esInsertar, int institucion, int tipoCampoInterfaz, int ordenCampo, String indicativoRequerido, int ordenCampoAnt)
		{
			return SqlBaseCampoInterfazDao.insertarActualizarCamposInterfaz (con, esInsertar, institucion, tipoCampoInterfaz, ordenCampo, indicativoRequerido, ordenCampoAnt);
		}
}
