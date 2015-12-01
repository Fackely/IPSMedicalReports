package com.princetonsa.dao.administracion;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.administracion.DtoDetConceptosRetencion;
import com.princetonsa.dto.administracion.DtoDetVigConRet;
import com.princetonsa.dto.administracion.DtoDetVigConRetencion;

public interface DetConceptosRetencionDao
{
	public ArrayList<DtoDetConceptosRetencion> consultaDetConceptosRetencion();
	
	public boolean insertarDetConceptosRetencion(DtoDetConceptosRetencion dto);
	
	public boolean validarExisteDetConceptosRetencion(DtoDetConceptosRetencion dto);
	
	public boolean validarDetalleXConcepto(String consecutivo);
	
	public ArrayList<DtoDetVigConRetencion> consultarDetVigenciaConceptosRetencion(String consecutivo);
	
	public boolean insertarDetVigConRetencion(DtoDetVigConRetencion dto);
	
	public ArrayList<DtoDetVigConRet> consultarDetRetXGrupoServicio(String consecutivo);
	
	public boolean insertarDetRetXGrupoServicio(DtoDetVigConRet dto);
	
	public boolean inactivarDetRetXGrupoServicio(DtoDetVigConRet dto);
	
	public ArrayList<DtoDetVigConRet> consultarDetRetXClaseInv(String consecutivo);
	
	public boolean insertarDetRetXClaseInv(DtoDetVigConRet dto);
	
	public boolean inactivarDetRetXClaseInv(DtoDetVigConRet dto);
	
	public ArrayList<DtoDetVigConRet> consultarDetRetXConceptos (String consecutivo);
	
	public boolean insertarDetRetXConcepto(DtoDetVigConRet dto); 
	
	public boolean inactivarDetRetXConcepto(DtoDetVigConRet dto);
	
	public boolean detPoseeDetalles(String consecutivo);
	
	public boolean inactivarDetVigConRetencion(DtoDetVigConRetencion dto);
	
	public boolean ingresarLog(DtoDetVigConRetencion dto);
	
	public boolean actualizarGrupo(int consecutivoNuevo,int consecutivoViejo);
	
	public boolean actualizarClase(int consecutivoNuevo,int consecutivoViejo);
	
	public boolean actualizarCfv(int consecutivoNuevo,int consecutivoViejo);
	
	public boolean poseeDetalles(int consecutivo);
	
}