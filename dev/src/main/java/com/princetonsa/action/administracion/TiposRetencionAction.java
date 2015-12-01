package com.princetonsa.action.administracion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.Utilidades;
import util.FacturasVarias.UtilidadesFacturasVarias;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.actionform.administracion.TiposRetencionForm;
import com.princetonsa.dto.administracion.DtoDetTiposRetencionClaseInv;
import com.princetonsa.dto.administracion.DtoDetTiposRetencionConceptoFV;
import com.princetonsa.dto.administracion.DtoDetTiposRetencionGrupoSer;
import com.princetonsa.dto.administracion.DtoTiposRetencion;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.TiposRetencion;
import com.princetonsa.sort.administracion.SortTiposRetencion;
import com.princetonsa.sort.administracion.SortTiposRetencionClaseInv;
import com.princetonsa.sort.administracion.SortTiposRetencionConceptoFraVaria;
import com.princetonsa.sort.administracion.SortTiposRetencionGrupoSer;
/**
 * @author Víctor Hugo Gómez L.
 */

public class TiposRetencionAction extends Action {
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private Logger logger = Logger.getLogger(TiposRetencionAction.class);

	/**
	 * Metodo excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, 
								ActionForm form,
								HttpServletRequest request, 
								HttpServletResponse response)
			throws Exception {
		Connection con = null;
		try{
		if (response == null);
		if (form instanceof TiposRetencionForm) {
			
			con = UtilidadBD.abrirConexion();
			if (con == null) {
				request.setAttribute("codigoDescripcionError","errors.problemasBd");
				return mapping.findForward("paginaError");
			}

			UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
			TiposRetencionForm forma = (TiposRetencionForm) form;
			TiposRetencion tiposRetencion = new TiposRetencion(); 
			String estado = forma.getEstado();
			
			logger.warn("\n\n\nEl estado en Tipos Retencion es------->"+ estado + "\n");

			if (estado == null) {
				forma.reset();
				logger.warn("Estado no valido dentro del flujo de Tipo Retencion (null) ");
				request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			
			else if (estado.equals("empezar")) {
				return cargatTiposRetencion(mapping, con, usuario, forma,
						tiposRetencion);
			}else if (estado.equals("agregarTipoRetencion")) {
				return agregarTipoRetencion(mapping, con, forma);
			}else if (estado.equals("cargarModTipRen")) {
				return cargarModTipoRetencion(mapping, con, forma);
			}else if (estado.equals("grabar")) {
				return grabarCambiosTiposRetencion(mapping, con, usuario,
						forma, tiposRetencion);
			}else if (estado.equals("eliminar")) {
				return deleteTipoRetencion(mapping, con, usuario, forma, tiposRetencion, request);
			}else if (estado.equals("detalleGrupoSer")) {
				return detalleGrupoServicio(mapping, con, usuario, forma);
			}else if (estado.equals("adicionarGrupoSer")) {
				return adicionarGrupoServicio(mapping, con, usuario, forma,
						tiposRetencion,request);
			}else if (estado.equals("eliminarGrpSer")) {
				return eliminarGrupoServicio(mapping, con, usuario, forma,
						tiposRetencion);
			}else if (estado.equals("detalleClaseInv")) {
				return detalleClaseInventario(mapping, con, usuario, forma);
			}else if (estado.equals("adicionarClaseInv")) {
				return adicionarClaseInventario(mapping, request, con, usuario,
						forma, tiposRetencion);
			}else if (estado.equals("eliminarClaseInv")) {
				return eliminarClaseInventario(mapping, con, usuario, forma,
						tiposRetencion);
			}else if (estado.equals("detalleConcFV")) {
				return detalleConceptoFraVarias(mapping, con, usuario, forma);
			}else if (estado.equals("adicionarConcFraVaria")) {
				return adicionarConceptoFacturaVaria(mapping, request, con,
						usuario, forma, tiposRetencion);
			}else if (estado.equals("eliminarConcFraVaria")) {
				return eliminarConceptoFraVaria(mapping, con, usuario, forma,
						tiposRetencion);
			}else if (estado.equals("ordenar")) {
				return ordenarTiposRetencion(mapping, con, forma);
			}
			
			
			
			else {
				forma.reset();
				logger.warn("Estado no valido dentro del flujo de Tipos  Retencion -> "+ estado);
				request.setAttribute("codigoDescripcionError","errors.formaTipoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
		}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}

	private ActionForward ordenarTiposRetencion(ActionMapping mapping,
			Connection con, TiposRetencionForm forma) {
		SortTiposRetencion sort= new SortTiposRetencion();
		sort.setPatronOrdenar(forma.getPatronOrdenar());
		Collections.sort(forma.getDtoTiposRetencion(), sort);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("empezar");
	}

	private ActionForward eliminarConceptoFraVaria(ActionMapping mapping,
			Connection con, UsuarioBasico usuario, TiposRetencionForm forma,
			TiposRetencion tiposRetencion) throws SQLException {
		DtoDetTiposRetencionConceptoFV dtoDetConcFV = new DtoDetTiposRetencionConceptoFV();
		dtoDetConcFV = (DtoDetTiposRetencionConceptoFV) forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetConceptoFV().get(forma.getPosicionConcFV());
		dtoDetConcFV.setUsuarioAnulacion(usuario.getLoginUsuario());
		if(tiposRetencion.inactivarTRConceptoFraVarias(con, dtoDetConcFV)!=ConstantesBD.codigoNuncaValido)
		{
			forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetConceptoFV().get(forma.getPosicionConcFV()).setEliminar(ConstantesBD.acronimoSi);
			SortTiposRetencionConceptoFraVaria sort= new SortTiposRetencionConceptoFraVaria();
			sort.setPatronOrdenar("descripcion");
			Collections.sort(forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetConceptoFV(), sort);
			forma.setTieneError(false);
		}else
			forma.setTieneError(true);
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("detalleConcFV");
	}

	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param con
	 * @param usuario
	 * @param forma
	 * @param tiposRetencion
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward adicionarConceptoFacturaVaria(ActionMapping mapping,
			HttpServletRequest request, Connection con, UsuarioBasico usuario,
			TiposRetencionForm forma, TiposRetencion tiposRetencion) throws SQLException {
		ActionErrors errores = new ActionErrors();
		
		if(forma.getCodigoConcFraVaria()!=ConstantesBD.codigoNuncaValido)
		{
			for(int k=0;k<forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetConceptoFV().size();k++)
			{
				DtoDetTiposRetencionConceptoFV dtoDetConcFVaux = (DtoDetTiposRetencionConceptoFV) forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetConceptoFV().get(k); 
				if(dtoDetConcFVaux.getCodigoConcepto() == forma.getCodigoConcFraVaria() 
						&& dtoDetConcFVaux.getEliminar().equals(ConstantesBD.acronimoNo))
					errores.add("Concepto Factura Varia", new ActionMessage("errors.notEspecific","El Concepto Factura Varia ya se Encuentra Vinculado al Tipo de Retención Selecionado. "));
			}
		}else{
			errores.add("Concepto Factura Varia", new ActionMessage("errors.required","El Campo Concepto Factura Varia "));	
		}
		
		if(errores.isEmpty())
		{
			DtoDetTiposRetencionConceptoFV dtoDetConcFV = new DtoDetTiposRetencionConceptoFV();
			int consecutivoTRConcFV = ConstantesBD.codigoNuncaValido;
			dtoDetConcFV.setCodigoTipoRetencion(forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getConsecutivo());
			dtoDetConcFV.setCodigoConcepto(forma.getCodigoConcFraVaria());
			dtoDetConcFV.setActivo(ConstantesBD.acronimoSi);
			dtoDetConcFV.setUsuarioModificacion(usuario.getLoginUsuario());
			dtoDetConcFV.setDescripcionConcFV(forma.getNombreConcFraVaria());
			if((consecutivoTRConcFV=tiposRetencion.insertarTRConceptoFraVaria(con, dtoDetConcFV))>0)
			{
				dtoDetConcFV.setConsecutivo(consecutivoTRConcFV);
				forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetConceptoFV().add(dtoDetConcFV);
				SortTiposRetencionConceptoFraVaria sort= new SortTiposRetencionConceptoFraVaria();
				sort.setPatronOrdenar("descripcion");
				Collections.sort(forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetConceptoFV(), sort);
				forma.setTieneError(false);
			}else
				forma.setTieneError(true);
		}else{
			forma.setTieneError(true);
			saveErrors(request, errores);
		}
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("detalleConcFV");
	}

	private ActionForward detalleConceptoFraVarias(ActionMapping mapping,
			Connection con, UsuarioBasico usuario, TiposRetencionForm forma)
			throws SQLException {
		forma.resetTRConceptoFraVaria();
		forma.setArrayConcFV(UtilidadesFacturasVarias.obtenerConceptosFraVarias(con, usuario.getCodigoInstitucionInt(), true));
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("detalleConcFV");
	}

	/**
	 * 
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param forma
	 * @param tiposRetencion
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward eliminarClaseInventario(ActionMapping mapping,
			Connection con, UsuarioBasico usuario, TiposRetencionForm forma,
			TiposRetencion tiposRetencion) throws SQLException {
		DtoDetTiposRetencionClaseInv dtoDetClaseInv = new DtoDetTiposRetencionClaseInv();
		dtoDetClaseInv = (DtoDetTiposRetencionClaseInv) forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetClaseInv().get(forma.getPosicionClaseInv());
		dtoDetClaseInv.setUsuarioAnulacion(usuario.getLoginUsuario());
		if(tiposRetencion.inactivarTRClaseInventario(con, dtoDetClaseInv)!=ConstantesBD.codigoNuncaValido)
		{
			forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetClaseInv().get(forma.getPosicionClaseInv()).setEliminar(ConstantesBD.acronimoSi);
			SortTiposRetencionClaseInv sort= new SortTiposRetencionClaseInv();
			sort.setPatronOrdenar("descripcion");
			Collections.sort(forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetClaseInv(), sort);
			forma.setTieneError(false);
		}else
			forma.setTieneError(true);
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("detalleClaseInv");
	}

	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param con
	 * @param usuario
	 * @param forma
	 * @param tiposRetencion
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward adicionarClaseInventario(ActionMapping mapping,
			HttpServletRequest request, Connection con, UsuarioBasico usuario,
			TiposRetencionForm forma, TiposRetencion tiposRetencion) throws SQLException {
		ActionErrors errores = new ActionErrors();
		
		if(forma.getCodigoClaseInv()!=ConstantesBD.codigoNuncaValido)
		{
			for(int k=0;k<forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetClaseInv().size();k++)
			{
				DtoDetTiposRetencionClaseInv dtoDetClaseInvaux = (DtoDetTiposRetencionClaseInv) forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetClaseInv().get(k); 
				if(dtoDetClaseInvaux.getCodigoClaseInv() == forma.getCodigoClaseInv()
						&& dtoDetClaseInvaux.getEliminar().equals(ConstantesBD.acronimoNo))
					errores.add("Clase Inventario", new ActionMessage("errors.notEspecific","La Clase Inventario ya se Encuentra Vinculado al Tipo de Retención Selecionado. "));
			}
		}else{
			errores.add("Clase Inventario", new ActionMessage("errors.required","El Campo Clase Inventario "));	
		}
		
		if(errores.isEmpty())
		{
			DtoDetTiposRetencionClaseInv dtoDetClaseInv = new DtoDetTiposRetencionClaseInv();
			int consecutivoTRClaseInv = ConstantesBD.codigoNuncaValido;
			dtoDetClaseInv.setCodigoTipoRetencion(forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getConsecutivo());
			dtoDetClaseInv.setCodigoClaseInv(forma.getCodigoClaseInv());
			dtoDetClaseInv.setActivo(ConstantesBD.acronimoSi);
			dtoDetClaseInv.setUsuarioModificacion(usuario.getLoginUsuario());
			dtoDetClaseInv.setNombreClaseInv(forma.getNombreClaseInv());
			if((consecutivoTRClaseInv=tiposRetencion.insertarTRClaseInventario(con, dtoDetClaseInv))>0)
			{
				dtoDetClaseInv.setConsecutivo(consecutivoTRClaseInv);
				forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetClaseInv().add(dtoDetClaseInv);
				SortTiposRetencionClaseInv sort= new SortTiposRetencionClaseInv();
				sort.setPatronOrdenar("descripcion");
				Collections.sort(forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetClaseInv(), sort);
				forma.setTieneError(false);
			}else
				forma.setTieneError(true);
		}else{
			forma.setTieneError(true);
			saveErrors(request, errores);
		}
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("detalleClaseInv");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param forma
	 * @return
	 * @throws SQLException
	 */
	private ActionForward detalleClaseInventario(ActionMapping mapping,
			Connection con, UsuarioBasico usuario, TiposRetencionForm forma)
			throws SQLException {
		forma.resetTRClaseInventario();
		forma.setArrayClaseInv(UtilidadInventarios.obtenerClasesInventarioArray(con, usuario.getCodigoInstitucionInt()));
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("detalleClaseInv");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param forma
	 * @param tiposRetencion
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward eliminarGrupoServicio(ActionMapping mapping,
			Connection con, UsuarioBasico usuario, TiposRetencionForm forma,
			TiposRetencion tiposRetencion) throws SQLException {
		DtoDetTiposRetencionGrupoSer dtoDetGrpSer = new DtoDetTiposRetencionGrupoSer();
		dtoDetGrpSer = (DtoDetTiposRetencionGrupoSer) forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetGrupoSer().get(forma.getPosicionGrpSer());
		dtoDetGrpSer.setUsuarioAnulacion(usuario.getLoginUsuario());
		if(tiposRetencion.inactivarTRGrupoServicio(con, dtoDetGrpSer)!=ConstantesBD.codigoNuncaValido)
		{
			forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetGrupoSer().get(forma.getPosicionGrpSer()).setEliminar(ConstantesBD.acronimoSi);
			SortTiposRetencionGrupoSer sort= new SortTiposRetencionGrupoSer();
			sort.setPatronOrdenar("descripcion");
			Collections.sort(forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetGrupoSer(), sort);
			forma.setTieneError(false);
		}else
			forma.setTieneError(true);
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("detalleGrupSer");
	}
	
	/**
	 * 
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param forma
	 * @param tiposRetencion
	 * @return
	 */
	private ActionForward adicionarGrupoServicio(ActionMapping mapping,
			Connection con, UsuarioBasico usuario, TiposRetencionForm forma,
			TiposRetencion tiposRetencion, HttpServletRequest request) {
		
		ActionErrors errores = new ActionErrors();
		
		if(forma.getCodigoGrpServicio()!=ConstantesBD.codigoNuncaValido)
		{
			for(int k=0;k<forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetGrupoSer().size();k++)
			{
				DtoDetTiposRetencionGrupoSer dtoDetGrpSeraux = (DtoDetTiposRetencionGrupoSer) forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetGrupoSer().get(k); 
				if(dtoDetGrpSeraux.getCodigoGrupoSer() == forma.getCodigoGrpServicio() 
						&& dtoDetGrpSeraux.getEliminar().equals(ConstantesBD.acronimoNo))
					errores.add("Grupo Servicio", new ActionMessage("errors.notEspecific","El Grupo Servicio ya se Encuentra Vinculado al Tipo de Retención Selecionado. "));
			}
		}else{
			errores.add("Grupo Servicio", new ActionMessage("errors.required","El Campo Grupo Servicio "));	
		}
		
		if(errores.isEmpty())
		{
			DtoDetTiposRetencionGrupoSer dtoDetGrpSer = new DtoDetTiposRetencionGrupoSer();
			String[] aux;
			int consecutivoTRGrpSer = ConstantesBD.codigoNuncaValido;
			aux = forma.getDescripcionGrupoSer().split(" - ");
			dtoDetGrpSer.setCodigoTipoRetencion(forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getConsecutivo());
			dtoDetGrpSer.setCodigoGrupoSer(forma.getCodigoGrpServicio());
			dtoDetGrpSer.setActivo(ConstantesBD.acronimoSi);
			dtoDetGrpSer.setUsuarioModificacion(usuario.getLoginUsuario());
			if(aux.length==2)
			{
				dtoDetGrpSer.setAcronimoGrupoSer(aux[0]);
				dtoDetGrpSer.setDescripcionGrupoSer(aux[1]);
			}else{
				if(aux.length<2)
					dtoDetGrpSer.setDescripcionGrupoSer(aux[0]);
				else{
					dtoDetGrpSer.setAcronimoGrupoSer(aux[0]);
					for(String elemaxu:aux)
						dtoDetGrpSer.setDescripcionGrupoSer(dtoDetGrpSer.getDescripcionGrupoSer()+" "+elemaxu);
				}
			}
			if((consecutivoTRGrpSer=tiposRetencion.insertarTRGrupoServicio(con, dtoDetGrpSer))>0)
			{
				dtoDetGrpSer.setConsecutivo(consecutivoTRGrpSer);
				forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetGrupoSer().add(dtoDetGrpSer);
				SortTiposRetencionGrupoSer sort= new SortTiposRetencionGrupoSer();
				sort.setPatronOrdenar("descripcion");
				Collections.sort(forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDtoDetGrupoSer(), sort);
				forma.setTieneError(false);
			}else
				forma.setTieneError(true);
		}else{
			forma.setTieneError(true);
			saveErrors(request, errores);
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalleGrupSer");
	}

	/**
	 * 
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param forma
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward detalleGrupoServicio(ActionMapping mapping,
			Connection con, UsuarioBasico usuario, TiposRetencionForm forma) throws SQLException {
		forma.resetTRGrupoServicio();
		forma.setArrayGrupoSer(Utilidades.obtenerGrupoServicios(con, usuario.getCodigoInstitucionInt(), true));
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("detalleGrupSer");
	}

	/**
	 * 
	 * @param mapping
	 * @param con
	 * @param forma
	 * @param tiposRetencion
	 * @return
	 * @throws SQLException
	 */
	private ActionForward deleteTipoRetencion(ActionMapping mapping,
			Connection con, UsuarioBasico usuario, TiposRetencionForm forma,
			TiposRetencion tiposRetencion, HttpServletRequest request) throws SQLException {
		ActionErrors errores = new ActionErrors();
		DtoTiposRetencion dto = new DtoTiposRetencion(); 
		dto = (DtoTiposRetencion) forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion());
		if(forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getIngresar().equals(ConstantesBD.acronimoNo))
		{
			if(tiposRetencion.inactivarTipoRetencion(con, usuario, dto)!=ConstantesBD.codigoNuncaValido){
				forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).setEliminar(ConstantesBD.acronimoSi);
				forma.setTieneError(false);
			}else{
				forma.setTieneError(true);
				errores.add("tipo retencion", new ActionMessage("errors.notEspecific","No de puede Eliminar el Tipo de Retención "+
						forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).getDescripcion()+" tiene registros asociados. "));
				saveErrors(request, errores);
			}
		}else{
			forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).setEliminar(ConstantesBD.acronimoSi);
			forma.setTieneError(false);
		}
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("empezar");
	}

	/**
	 * 
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param forma
	 * @param tiposRetencion
	 * @return
	 * @throws SQLException
	 */
	private ActionForward grabarCambiosTiposRetencion(ActionMapping mapping,
			Connection con, UsuarioBasico usuario, TiposRetencionForm forma,
			TiposRetencion tiposRetencion) throws SQLException {
		tiposRetencion.setDtoTiposRetencion(forma.getDtoTiposRetencion());
		if(tiposRetencion.grabarCambiosTiposRetencion(con, usuario)!=ConstantesBD.codigoNuncaValido)
			forma.setTieneError(false);
		else
			forma.setTieneError(true);
		return cargatTiposRetencion(mapping, con, usuario, forma, tiposRetencion);
	}

	/**
	 * Cargar campos Tipo Retencion para modificar
	 * @param mapping
	 * @param con
	 * @param forma
	 * @return
	 * @throws SQLException
	 */
	private ActionForward cargarModTipoRetencion(ActionMapping mapping,
			Connection con, TiposRetencionForm forma) throws SQLException {
		forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).setIngresar(ConstantesBD.acronimoNo);
		forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).setEliminar(ConstantesBD.acronimoNo);
		forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).setModificar(ConstantesBD.acronimoSi);
		forma.getDtoTiposRetencion().get(forma.getPosTipoRetencion()).cargarDtoLogTipoRetencion();
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("empezar");
	}
	
	/**
	 * Agregar un Tipo Retencion
	 * @param mapping
	 * @param con
	 * @param forma
	 * @return
	 * @throws SQLException
	 */
	private ActionForward agregarTipoRetencion(ActionMapping mapping,
			Connection con, TiposRetencionForm forma) throws SQLException {
		DtoTiposRetencion dto = new DtoTiposRetencion();
		dto.setIngresar(ConstantesBD.acronimoSi);
		forma.getDtoTiposRetencion().add(dto);
		SortTiposRetencion sort= new SortTiposRetencion();
		sort.setPatronOrdenar("codigo");
		Collections.sort(forma.getDtoTiposRetencion(), sort);
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("empezar");
	}

	/**
	 * Carga los Tipos de Retencion
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param forma
	 * @param tiposRetencion
	 * @return
	 * @throws SQLException
	 */
	private ActionForward cargatTiposRetencion(ActionMapping mapping,
			Connection con, UsuarioBasico usuario, TiposRetencionForm forma,
			TiposRetencion tiposRetencion) throws SQLException {
		boolean aux = forma.isTieneError();
		forma.reset();
		forma.setDtoTiposRetencion(tiposRetencion.cargarTiposRetencion(con, usuario.getCodigoInstitucionInt()));
		forma.setTieneError(aux);
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("empezar");
	}
}
