package com.sysmedica.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;

public interface FichasAnterioresDao {

	public Collection consultaFichasPorPaciente(Connection con, 
														int codigoPaciente, 
														String diagnostico, 
														String codigoDx);
}
