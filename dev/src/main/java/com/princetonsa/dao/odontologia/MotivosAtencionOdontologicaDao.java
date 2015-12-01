package com.princetonsa.dao.odontologia;

import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.odontologia.DtoMotivosAtencion;


public interface MotivosAtencionOdontologicaDao
{
	public HashMap consultarTiposMotivo();

	public boolean insertarMotivoAtencionO(String codigoNuevo, String nombreNuevo,int tipoNuevo, int institucion, String usuario);	
	
	public ArrayList<DtoMotivosAtencion> consultarMotivoAtencionO(ArrayList<Integer> tipos, int institucion);

	public boolean modificarMotivoAtencionO(int codigoPk, String codigoNuevo,String nombreNuevo, int tipoNuevo, int codigoInstitucionInt,String loginUsuario);

	public boolean eliminarMotivoAtencionO(int codigoPk);
}