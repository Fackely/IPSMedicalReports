package com.princetonsa.mundo.cargos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.SalarioMinimoDao;

import util.ResultadoBoolean;
import util.ResultadoCollectionDB;
import util.UtilidadTexto;
import util.Utilidades;

import java.util.HashMap;
import org.apache.log4j.Logger;
/**
 * @author rcancino
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class SalarioMinimo
{
	/**
	 * acceso a BD
	 */
	private SalarioMinimoDao salarioMinimoDao = null;
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(RecargoTarifa.class);
	/**
	 * codigo correspondiente al salario
	 */
	private int codigo;
	/**
	 * fecha inicial de vigencia del salario
	 */
	private String fechaInicial;
	/**
	 * fecha de vencimiento del salario
	 */
	private String fechaFinal;
	/**
	 * Monto del salario
	 */
	private String salario;
	/**
	 * Returns the fecha_final.
	 * @return String
	 */
	public String getFechaFinal()
	{
		return fechaFinal;
	}

	/**
	 * Returns the fecha_inicial.
	 * @return String
	 */
	public String getFechaInicial()
	{
		return fechaInicial;
	}

	/**
	 * @return Returns the salario.
	 */
	public String getSalario()
	{
		return salario;
	}
	/**
	 * @param salario The salario to set.
	 */
	public void setSalario(String salario)
	{
		this.salario = salario;
	}
	/**
	 * Sets the fecha_final.
	 * @param fecha_final The fecha_final to set
	 */
	public void setFechaFinal(String fecha_final)
	{
		this.fechaFinal = fecha_final;
	}

	/**
	 * Sets the fecha_inicial.
	 * @param fecha_inicial The fecha_inicial to set
	 */
	public void setFechaInicial(String fecha_inicial)
	{
		this.fechaInicial = fecha_inicial;
	}

	public ResultadoBoolean insertar(	Connection con	)throws SQLException{
	    String nuevoFormatoSalario=UtilidadTexto.formatearValores(salario+"",2,false,false);
	    
		return salarioMinimoDao.insertar(con,Double.parseDouble(nuevoFormatoSalario),fechaInicial,fechaFinal);
	}
	public boolean existeCruzeSalario(Connection con)throws SQLException{
		return salarioMinimoDao.existeCruzeSalario(con,fechaInicial,fechaFinal);
	}
	
	/**
	 * Metodo para realizar la consulta general de los registros existentes
	 * de Salario Minimo 
	 * @param con, Connection con la fuente de datos.
	 * @return Collecction con el resultado de la consulta
	 * @throws SQLException
	 */
	
	public Collection consultarSalarios(Connection con) throws SQLException
	{
	    ResultadoCollectionDB resultado = this.salarioMinimoDao.consultar(con);
	    
	    return resultado.getFilasRespuesta();
		
	}
	
	public ResultadoBoolean consultar(Connection con) throws SQLException{
		ResultadoCollectionDB resultado = this.salarioMinimoDao.consultar(con);
		if( !resultado.isTrue() )
			return new ResultadoBoolean(false, resultado.getDescripcion());
		else
		{
			ArrayList listado = (ArrayList)resultado.getFilasRespuesta();
			int tam = 0;			
			if( listado != null && (tam = listado.size()) > 0 )
			{		
				for( int i=0; i < tam; i++ )
				{
					HashMap salario = (HashMap)(listado).get(i);					
					this.setCodigo(Utilidades.convertirAEntero((salario.get("codigo")+"")));
					this.setFechaInicial(salario.get("fechainicial")+"");
					this.setFechaFinal(salario.get("fechafinal")+"");
					this.setSalario(salario.get("salario")+"");
					
						
				}
			}
		}
		return new ResultadoBoolean(true);
	}
	
	public ResultadoBoolean consultarVigente(Connection con) throws SQLException{
		ResultadoCollectionDB resultado = this.salarioMinimoDao.consultarVigente(con);
		if( !resultado.isTrue() )
			return new ResultadoBoolean(false, resultado.getDescripcion());
		else
		{
			ArrayList listado = (ArrayList)resultado.getFilasRespuesta();
			int tam = 0;			
			if( listado != null && (tam = listado.size()) > 0 )
			{		
				for( int i=0; i < tam; i++ )
				{
					HashMap salario = (HashMap)(listado).get(i);					
					this.setCodigo(Utilidades.convertirAEntero(salario.get("codigo")+""));
					this.setFechaInicial(salario.get("fechainicial").toString());
					this.setFechaFinal(salario.get("fechafinal").toString());
					//this.setSalario(((Double)salario.get("salario")).doubleValue());
					this.setSalario(salario.get("salario").toString());
					
						
				}
			}
		}
		return new ResultadoBoolean(true);
	}
	/**
	 * Modifica un recargo a una tarifa 
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @return ResultadoBoolean, true si la modificacion fue exitosa, false y con la descripción 
	 * de lo contrario
	 * @see com.princetonsa.dao.RecargoTarifaDao#modificar(java.sql.Connection, int, double, double, int, int, int, int, int)
	 */
	public ResultadoBoolean modificar(	Connection con	)throws SQLException
	{
		logger.info("Salario-->"+salario+"\n");
	    String nuevoFormatoSalario=UtilidadTexto.formatearValores(salario+"",2,false,false);
	    logger.info("nuevoFormatoSalario-->"+nuevoFormatoSalario+"\n");
	    
		return this.salarioMinimoDao.modificar(con,codigo,Double.parseDouble(nuevoFormatoSalario),fechaInicial,fechaFinal);
																		
	}
	/**
	 * Creadora de la clase RecargoTarifa.java
	 */
	public SalarioMinimo()
	{
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.salario = "0.0";
		this.codigo = -1;
		this.init(System.getProperty("TIPOBD"));			
	}

	/**
	 * Returns the codigo.
	 * @return String
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * Sets the codigo.
	 * @param codigo The codigo to set
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
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
			if( salarioMinimoDao == null)
			{
				if(tipoBD==null)
				{
					this.logger.error("No esta llegando el tipo de base de datos");
					System.exit(1);
				}
				else
				{
					DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
					if (myFactory != null)
					{
						salarioMinimoDao = myFactory.getSalarioMinimoDao();
					}					
				}
			}
		}

}
