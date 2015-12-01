/*
 * @(#)HistoricoAtenciones.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.atencion;


import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import org.apache.log4j.Logger;

/**
 * Esta clase 
 *
 * @version 1.0, Sept. 09, 2003
 * @author 	<a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>.
 */

public class HistoricoAtenciones {
	
	private Logger logger = Logger.getLogger(HistoricoAtenciones.class);

	/**
	 * C�digo del paciente
	 */
	int codigoPaciente;
	
	/**
	 * Codigo de la instituci�n
	 */
	private int codigoInstitucion;
	private DaoFactory myFactory;
	/**
	 * Collection con todos los ingresos que tiene el paciente en la instituci�n.
	 * Cada objeto de esta collection es un ArrayList de objetos 'Atencion', 
	 * que estan asociados al mismo ingreso.
	 */
	private ArrayList ingresos;
	
	public HistoricoAtenciones(){
		clean();
	}
	public void init(String tipoBD)
	{
		myFactory = DaoFactory.getDaoFactory(tipoBD);
	}	
	public HistoricoAtenciones(int codigoPaciente, int codigoInstitucion)
	{
		this.codigoPaciente=codigoPaciente;
		this.codigoInstitucion = codigoInstitucion;
		this.ingresos = new ArrayList();
		init(System.getProperty("TIPOBD"));			
	}
	
	public HistoricoAtenciones(Connection con, int codigoPaciente, int codigoInstitucion)
	{
		this.codigoPaciente=codigoPaciente;
		this.codigoInstitucion = codigoInstitucion;
		this.ingresos = new ArrayList();
		init(System.getProperty("TIPOBD"));
		cargar(con, codigoPaciente);
	}
	
	public void clean()
	{
		this.codigoPaciente=-1;
		this.codigoInstitucion = -1;
		this.ingresos = new ArrayList();
	}
	public int size()
	{
		return this.ingresos.size();
	}
	/**
	 * Carga el arreglo ingresos
	 * @param con
	 * @return
	 */
	public boolean cargar(Connection con, int codigoPaciente)
	{
		//Contiene en la primero posicion un Integer con el codigo del ingreso y en la segunda un arrayList con las cuentas que corresponden a ese ingreso
		Object [] ingreso;
		try
		{		
			init(System.getProperty("TIPOBD"));
			//Obteniendo los codigos de los diferentes ingresos del paciente a la institucion
			this.codigoPaciente=codigoPaciente;
			ResultSetDecorator rs_ingresos = this.myFactory.getIngresoGeneralDao().getCodigosIngresosPaciente(con, codigoPaciente, this.codigoInstitucion);
			ResultSetDecorator rs_cuentas;
			Atencion atencion;
			ArrayList ingresos = new ArrayList();
			ArrayList cuentas;
			//Por cada codigo de un ingreso obtener los diferentes codigos de las cuentas asociadas a ese ingreso		
			while(rs_ingresos.next())
			{
				rs_cuentas = this.myFactory.getCuentaDao().getCodigosCuentasIngreso(con, rs_ingresos.getInt("idIngreso"));				
				cuentas = new ArrayList();
				//Por cada codigo de cuenta cargar un objeto Atencion			
				while (rs_cuentas.next())
				{
					atencion = new Atencion (con, rs_cuentas.getInt("idCuenta"));
					cuentas.add(atencion);	
				}
				ingreso = new Object[] { new Integer(rs_ingresos.getString("idIngreso")), cuentas };
				
				ingresos.add(ingreso);
			}
			this.ingresos = ingresos;
			return true;
		}
		catch (SQLException sql)
		{
			logger.warn(sql);
			
			
		}
		return false;
	}
	
	/**
	 * @return
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * @return
	 */
	public ArrayList getIngresos() {
		return ingresos;
	}

	/**
	 * @param i
	 */
	public void setCodigoInstitucion(int i) {
		codigoInstitucion = i;
	}

	/**
	 * @param list
	 */
	public void setIngresos(ArrayList list) {
		ingresos = list;
	}

}