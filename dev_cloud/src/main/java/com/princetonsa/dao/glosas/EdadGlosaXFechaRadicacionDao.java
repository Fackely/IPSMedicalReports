package com.princetonsa.dao.glosas;

import java.util.ArrayList;

import com.princetonsa.dto.glosas.DtoGlosa;

public interface EdadGlosaXFechaRadicacionDao
{
	public ArrayList<DtoGlosa> accionBuscarDocs(DtoGlosa dto, boolean radicadas);
}