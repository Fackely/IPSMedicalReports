package com.princetonsa.dao.oracle.glosas;

import java.util.ArrayList;

import com.princetonsa.dao.glosas.EdadGlosaXFechaRadicacionDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseEdadGlosaXFechaRadicacionDao;
import com.princetonsa.dto.glosas.DtoGlosa;

public class OracleEdadGlosaXFechaRadicacionDao implements EdadGlosaXFechaRadicacionDao
{
	public ArrayList<DtoGlosa> accionBuscarDocs(DtoGlosa dto, boolean radicadas)
	{
		return SqlBaseEdadGlosaXFechaRadicacionDao.consultarGlosas(dto, radicadas);
	}
}