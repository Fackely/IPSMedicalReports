package com.sysmedica.mundo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

import com.princetonsa.dao.DaoFactory;
import com.sysmedica.dao.FichasAnterioresDao;

public class FichasAnteriores {

	Collection fichasAnteriores;
	FichasAnterioresDao fichasAnterioresDao;
	
	private int codigoPaciente;
	private String codigoDx;
	private String diagnostico;
	
	public FichasAnteriores()
	{
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el acceso a base de datos de este objeto
	 * @param tipoBD
	 * @return
	 */
	public boolean init(String tipoBD)
	{
			boolean wasInited = false;
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			if (myFactory != null)
			{
				fichasAnterioresDao = myFactory.getFichasAnterioresDao();
				wasInited = (fichasAnterioresDao != null);
			}
			return wasInited;
	}
	
	public void reset()
	{
		
	}
	
	public Collection consultaFichasPorPaciente(Connection con)
	{
		return fichasAnterioresDao.consultaFichasPorPaciente(con,codigoPaciente,diagnostico,codigoDx);
	}
	

	public Collection getFichasAnteriores() {
		return fichasAnteriores;
	}

	public void setFichasAnteriores(Collection fichasAnteriores) {
		this.fichasAnteriores = fichasAnteriores;
	}

	public String getCodigoDx() {
		return codigoDx;
	}

	public void setCodigoDx(String codigoDx) {
		this.codigoDx = codigoDx;
	}

	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}
}
