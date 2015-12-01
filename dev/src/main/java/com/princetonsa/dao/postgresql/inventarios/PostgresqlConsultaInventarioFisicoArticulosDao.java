package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.ConsultaInventarioFisicoArticulosDao;

import com.princetonsa.dao.sqlbase.inventarios.SqlBaseConsultaInventarioFisicoArticulosDao;

import com.princetonsa.mundo.inventarios.ConsultaInventarioFisicoArticulos;




public class PostgresqlConsultaInventarioFisicoArticulosDao implements ConsultaInventarioFisicoArticulosDao{
	

	
	public HashMap filtrarArticulos(Connection con, ConsultaInventarioFisicoArticulos cuc)
	{
		return SqlBaseConsultaInventarioFisicoArticulosDao.filtrarArticulos(con, cuc);
	}
	
	public HashMap consultarConteosArticulo(Connection con, ConsultaInventarioFisicoArticulos cuc)
	{
		return SqlBaseConsultaInventarioFisicoArticulosDao.consultarConteosArticulo(con, cuc);
	}
	public HashMap consultarPreparacionesArticulo(Connection con, ConsultaInventarioFisicoArticulos cuc)
	{
		return SqlBaseConsultaInventarioFisicoArticulosDao.consultarPreparacionesArticulo(con, cuc);
	}
	public HashMap consultarUbicacionArticulo(Connection con, ConsultaInventarioFisicoArticulos cuc)
	{
		return SqlBaseConsultaInventarioFisicoArticulosDao.consultarUbicacionArticulo(con, cuc);
	}
	public HashMap consultarAjusteArticulo(Connection con, ConsultaInventarioFisicoArticulos cuc)
	{
		return SqlBaseConsultaInventarioFisicoArticulosDao.consultarAjusteArticulo(con, cuc);
	}
	
}