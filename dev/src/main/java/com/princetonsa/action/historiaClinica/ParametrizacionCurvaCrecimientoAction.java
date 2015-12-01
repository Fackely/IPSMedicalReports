package com.princetonsa.action.historiaClinica;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.PathSolver;
import util.UtilidadTexto;

import com.princetonsa.actionform.historiaClinica.ParametrizacionCurvaCrecimientoForm;
import com.servinte.axioma.bl.administracion.facade.AdministracionFacade;
import com.servinte.axioma.bl.historiaClinica.facade.HistoriaClinicaFacade;
import com.servinte.axioma.dto.administracion.DtoSexo;
import com.servinte.axioma.dto.historiaClinica.CurvaCrecimientoParametrizabDto;
import com.servinte.axioma.dto.historiaClinica.ImagenParametrizadaDto;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Action para la funcionalidad parametrizacion de la curva de desarrollo y crecimiento
 * @author Juan Camilo Gaviria Acosta
 */
public class ParametrizacionCurvaCrecimientoAction extends DispatchAction
{	
	public static final int PARAMETRIZAR = 1;
	public static final int CREAR = 2;
	public static final int VISUALIZAR = 3;
	public static final int BUSCAR = 4;
	public static final int BUSCANDO = 5;
	private List<DtoSexo> dtosexos;
	
	/**
	 * se ejecuta por defecto al ingresar a la funcionalidad y carga las curvas existentes
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward empezar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ParametrizacionCurvaCrecimientoForm pcf = null;
		try 
		{
			pcf = (ParametrizacionCurvaCrecimientoForm) form;
			pcf.resetMensajes();
			if(pcf.getUbicacion()!=BUSCANDO)
			{
				pcf.setCurvasExistentes(new ArrayList<CurvaCrecimientoParametrizabDto>(ordenarCurvas("tituloGrafica", new HistoriaClinicaFacade().consultarCurvasParametrizadas())));
				pcf.setUbicacion(PARAMETRIZAR);
			}			
			pcf.setListaSexos(new ArrayList<String>());
			pcf.getListaSexos().add("Ambos");
			dtosexos = new AdministracionFacade().consultarSexos();
			for (int i = 0; i < dtosexos.size(); i++)
				pcf.getListaSexos().add(dtosexos.get(i).getNombre());
		}
		catch (IPSException e) {
			pcf.addMensajeError(e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			Log4JManager.error(e);
		}
		return mapping.findForward("empezar");
	}
	
	/**
	 * Resetea valores y redirecciona a la pagina nuevaParametrizacionCurvaCrecimiento.jsp
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward crearGrafica(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ParametrizacionCurvaCrecimientoForm pcf = (ParametrizacionCurvaCrecimientoForm) form;
		pcf.reset2();
		pcf.setUbicacion(CREAR);
		return mapping.findForward("crearGrafica");
	}
	
	/**
	 * Valida los datos ingresados y guarda la curva
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward guardarGrafica(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ParametrizacionCurvaCrecimientoForm pcf = null;
		try 
		{
			pcf = (ParametrizacionCurvaCrecimientoForm) form;
			pcf.resetMensajes();
			CurvaCrecimientoParametrizabDto dtoccp = validarDatos(pcf);
			if(dtoccp!= null){
				new HistoriaClinicaFacade().guardarCurvaParametrizada(dtoccp);
				pcf.setCurvaSeleccionada(dtoccp);
				//pcf.reset2();
				pcf.addMensajeInformacion("Proceso exitoso");
				pcf.setCurvasExistentes(new ArrayList<CurvaCrecimientoParametrizabDto>(ordenarCurvas("tituloGrafica", new HistoriaClinicaFacade().consultarCurvasParametrizadas())));
				pcf.setUbicacion(PARAMETRIZAR);
				return mapping.findForward("empezar");
			}
		}
		catch (ClassCastException e){
			Log4JManager.error(e);
		}
		catch (Exception e) {
			pcf.addMensajeError(e.getMessage());
			e.printStackTrace();
		}
		pcf.setUbicacion(CREAR);
		return mapping.findForward("crearGrafica");
	}
	
	/**
	 * Valida los datos ingresados y agrega los mensajes de error
	 * @param pcf
	 * @return
	 * @throws IOException
	 * @throws IPSException
	 */
	public CurvaCrecimientoParametrizabDto validarDatos(ParametrizacionCurvaCrecimientoForm pcf) throws IOException, IPSException{
		CurvaCrecimientoParametrizabDto dtoccp = new CurvaCrecimientoParametrizabDto();
		ImagenParametrizadaDto dtoip = new ImagenParametrizadaDto();
					
		pcf.setMensajesError(new ArrayList<String>());
		boolean hayErrores = false;
		boolean cambioLaGraficaCurva = false;
		
		String dImg = System.getProperty("directorioImagenes");
		String subCarpeta = "curvasCrecimiento/imagenesParametrizadas/";
		String carpetaImagenesParametrizadas = PathSolver.getWEBINFPath() + "/" + dImg + subCarpeta;
		if(System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") >= 0){
			subCarpeta = "curvasCrecimiento\\imagenesParametrizadas\\";
			carpetaImagenesParametrizadas = PathSolver.getWEBINFPath() + "\\" + dImg + subCarpeta;
		}
		File carpeta = new File(carpetaImagenesParametrizadas); 
		if(!carpeta.exists())
			carpeta.mkdirs();
		
		File imgCur = new File("");
		File imgDer = new File("");
		File imgIzq = new File("");
		String hf = new Date(System.currentTimeMillis()).toString().replaceAll(" ", "_").replaceAll(":", "-");
		String tituloImagen = UtilidadTexto.cambiarCaracteresEspeciales(pcf.getTituloGrafica().replaceAll(" ", "-").replaceAll("/", "").replaceAll("\\\\", ""));
		
		if(!pcf.getImagenCurva().getFileName().equals("")) //es nueva imagen
		{
			imgCur = subirArchivo(pcf.getImagenCurva(), carpetaImagenesParametrizadas + tituloImagen + "-imgCur-" + hf + extension(pcf.getImagenCurva().getFileName()));
			dtoip.setImagenCurva(subCarpeta + imgCur.getName());
			cambioLaGraficaCurva = true;
		}
		else // si NO ingreso nada es porque tiene algo guardado o realmente no ingreso nada
		{
			if(pcf.getCurvaSeleccionada()==null) // no ingreso nada
			{
				pcf.addMensajeError("Debe seleccionar una imagen para la gráfica");
				hayErrores = true;
			}
			else // tiene algo guardado
			{
				if(pcf.getCurvaSeleccionada().getDtoImagenesParametrizadas().getImagenCurva()==null || pcf.getCurvaSeleccionada().getDtoImagenesParametrizadas().getImagenCurva().equals(""))
				{
					pcf.addMensajeError("Debe seleccionar una imagen para la gráfica");
					hayErrores = true;
				}
				else
					dtoip.setImagenCurva(pcf.getCurvaSeleccionada().getDtoImagenesParametrizadas().getImagenCurva());
			}
		}
		
		if(!pcf.getImagenDerecha().getFileName().equals("")) //es nueva imagen
		{
			imgDer = subirArchivo(pcf.getImagenDerecha(), carpetaImagenesParametrizadas + tituloImagen + "-imgDer-" + hf + extension(pcf.getImagenDerecha().getFileName()));
			dtoip.setImagenDerecha(subCarpeta + imgDer.getName());
		}
		else // si NO ingreso nada es porque tiene algo guardado o realmente no ingreso nada
		{
			if(pcf.getCurvaSeleccionada()==null) //no ingreso nada
				dtoip.setImagenDerecha("");
			else // tiene algo guardado
				dtoip.setImagenDerecha(pcf.getCurvaSeleccionada().getDtoImagenesParametrizadas().getImagenDerecha());
		}
		
		if(!pcf.getImagenIzquierda().getFileName().equals("")) //es nueva imagen
		{
			imgIzq = subirArchivo(pcf.getImagenIzquierda(), carpetaImagenesParametrizadas + tituloImagen + "-imgIzq-" + hf + extension(pcf.getImagenIzquierda().getFileName()));
			dtoip.setImagenIzquierda(subCarpeta + imgIzq.getName());
		}
		else // si NO ingreso nada es porque tiene algo guardado o realmente no ingreso nada
		{
			if(pcf.getCurvaSeleccionada()==null) // no ingreso nada
				dtoip.setImagenIzquierda("");
			else // tiene algo guardado
				dtoip.setImagenIzquierda(pcf.getCurvaSeleccionada().getDtoImagenesParametrizadas().getImagenIzquierda());
		}
		
		// compara el codigo sexo con el nombre
		Integer codigoSexo = null;
		for (int i = 0; i < dtosexos.size(); i++)
			if(pcf.getSexoSeleccionado().equals(dtosexos.get(i).getNombre()))
				codigoSexo = dtosexos.get(i).getCodigoSexo();
		
		// el color del titulo por defecto es negro
		if(pcf.getColorTitulo().equals("") || pcf.getColorTitulo().equals("#000000"))
			if(pcf.getCurvaSeleccionada()==null)
				pcf.setColorTitulo("00,00,00");
			else
				pcf.setColorTitulo(htmlToHex(pcf.getCurvaSeleccionada().getColorTitulo()));
		
		// el color de la descripcion por defecto es negro
		if(pcf.getColorDescripcion().equals("") || pcf.getColorDescripcion().equals("#000000"))
			if(pcf.getCurvaSeleccionada()==null)
				pcf.setColorDescripcion("00,00,00");
			else
				pcf.setColorDescripcion(htmlToHex(pcf.getCurvaSeleccionada().getColorDescripcion()));
		
		String colorTitulo = pcf.getColorTitulo();
		String colorDescripcion = pcf.getColorDescripcion();
		
		//se realiza la conversion del formato entregado por el selector de color (r,g,b) en decimal a formato Html (#RRGGBB) en exadecimal
		if(pcf.getColorTitulo().contains(","))
			colorTitulo = hexToHtml(rgbToHex(csvToRgb(pcf.getColorTitulo())));
		if(pcf.getColorDescripcion().contains(","))
			colorDescripcion = hexToHtml(rgbToHex(csvToRgb(pcf.getColorDescripcion())));
		
		pcf.setColorTitulo(colorTitulo);
		pcf.setColorDescripcion(colorDescripcion);
		
		//validacion para el titulo obligatorio
		if(!pcf.getTituloGrafica().equals("")){
			dtoccp.setTituloGrafica(pcf.getTituloGrafica());
		}
		else
		{
			pcf.addMensajeError("Titulo obligatorio");
			hayErrores = true;
		}
		
		//validacion para la edad inicial obligatoria
		if(pcf.getEdadInicial() != null)
		{
			dtoccp.setEdadInicial(pcf.getEdadInicial());
		}
		else
		{
			pcf.addMensajeError("Edad inicial obligatoria");
			hayErrores = true;
		}
		
		//validacion para la edad final obligatoria
		if(pcf.getEdadFinal() != null)
		{
			dtoccp.setEdadFinal(pcf.getEdadFinal());
		}
		else
		{
			pcf.addMensajeError("Edad final obligatoria");
			hayErrores = true;
		}
		
		//validacion para la edad final mayor o igual a la edad inicial
		if(dtoccp.getEdadInicial()!=null && dtoccp.getEdadFinal()!=null)
		{
			if(dtoccp.getEdadInicial() > dtoccp.getEdadFinal())
			{
				pcf.addMensajeError("La edad final debe ser mayor o igual a la edad inicial");
				hayErrores = true;
			}
		}
		
		if (pcf.getIdCurvaSeleccionada()==null) // si la curva es nueva
		{
			dtoccp.setDtoImagenesParametrizadas(dtoip);
			dtoip.setActivo(true);
		}
		else // si no es nueva es porque la esta editando la imagen de la grafica o porque está editando los demas datos
		{
			if(!pcf.getCurvaSeleccionada().getDtoImagenesParametrizadas().equals(dtoip))
			{
				if(cambioLaGraficaCurva) // edito la imagen de la curva
				{
					dtoccp.setDtoImagenParametrizadaAntigua(new ImagenParametrizadaDto(null, dtoip.getImagenIzquierda(), dtoip.getImagenDerecha(), pcf.getCurvaSeleccionada().getDtoImagenesParametrizadas().getImagenCurva(), false, pcf.getCurvaSeleccionada().getDtoImagenesParametrizadas().getFechaCreacion()));
				}
				dtoip.setActivo(true);
				dtoip.setId(pcf.getCurvaSeleccionada().getDtoImagenesParametrizadas().getId());
				dtoccp.setId(pcf.getCurvaSeleccionada().getId());
				dtoccp.setDtoImagenesParametrizadas(dtoip);
			}
			else // edito los demas datos
			{
				dtoccp.setId(pcf.getIdCurvaSeleccionada());
				dtoccp.setDtoImagenesParametrizadas(pcf.getCurvaSeleccionada().getDtoImagenesParametrizadas());
			}
		}
		
		dtoccp.setCodigoSexo(codigoSexo);
		dtoccp.setColorTitulo(colorTitulo);
		dtoccp.setDescripcion(pcf.getDescripcion());
		dtoccp.setColorDescripcion(colorDescripcion);
		dtoccp.setActivo(pcf.getActivo());
		dtoccp.setIndicadorError(pcf.getIndicadorError());
		pcf.setCurvaSeleccionada(dtoccp);
		
		if(hayErrores){
			//pcf.setCurvaSeleccionada(null);
			return null;
		}
		
		return dtoccp;
	}
	
	/**
	 * subirArchivo
	 * @param archivoForm
	 * @param rutaDestino
	 * @return
	 * @throws IOException
	 */
	public File subirArchivo(FormFile archivoForm, String rutaDestino) throws IOException{
		File f = new File(rutaDestino);
		FileOutputStream out = new FileOutputStream(f);
		out.write(archivoForm.getFileData());
		out.flush();
		out.close();
		return f;
	}
	
	/**
	 * obtiene las extensiones de las imagenes permitidas
	 * @param ruta
	 * @return
	 * @throws IOException
	 */
	public String extension(String ruta) throws IOException{
		String extension = ruta.substring(ruta.length()-4, ruta.length());
		if(!extension.startsWith("."))
			extension = ruta.substring(ruta.length()-5, ruta.length());
		if(extension.equals(".jpg"));
		else if(extension.equals(".jpeg"));
		else if(extension.equals(".png"));
		else if(extension.equals(".gif"));
		else if(extension.equals(".JPG"));
		else if(extension.equals(".JPEG"));
		else if(extension.equals(".PNG"));
		else if(!extension.equals(".GIF"))
			throw new IOException("El formato de la imagen no es compatible");
		return extension;
	}
	
	/**
	 * Recibe una cadena con el color en formato r,g,b y la convierte a arreglo de enteros [r,g,b]
	 * @param rgbComas
	 * @return
	 */
	public ArrayList<Integer> csvToRgb(String rgbComas){
		ArrayList<Integer> rgb = new ArrayList<Integer>();
		String temp = "";
		for (int i = 0; i < rgbComas.length(); i++) {
			if(rgbComas.charAt(i)!=',')
				temp = temp + rgbComas.charAt(i);
			else
			{ 
				rgb.add(Integer.valueOf(temp));
				temp = "";
			}
		}
		rgb.add(Integer.valueOf(temp));
		return rgb;
	}
	
	/**
	 * Recibe un arreglo de enteros con el color [r,g,b] y lo convierte a arreglo de hexadecimales [R,G,B]
	 * @param rgb
	 * @return
	 */
	public ArrayList<String> rgbToHex(ArrayList<Integer> rgb){
		ArrayList<String> resp = new ArrayList<String>();
		for (int i = 0; i < rgb.size(); i++)
			resp.add(Integer.toHexString(rgb.get(i)).toUpperCase());
		return resp;
	}
	
	/**
	 * Recibe un arreglo de exadecimales [R,G,B] y lo convierte a un color valido para html #RRGGBB 
	 * @param hex
	 * @return
	 */
	public String hexToHtml(ArrayList<String> hex){
		String resp = "#";
		for (int i = 0; i < hex.size(); i++) {
			try 
			{
				if(hex.get(i).equals("A") || hex.get(i).equals("B") || hex.get(i).equals("C") || hex.get(i).equals("D") || hex.get(i).equals("E") || hex.get(i).equals("F") || Integer.parseInt(hex.get(i)) < 10)
					resp = resp + "0" + hex.get(i);
				else
					resp = resp + hex.get(i);
			}
			catch (Exception e) 
			{
				resp = resp + hex.get(i);
			}
		}
		return resp;
	}
	
	/**
	 * Recibe un color tipo HTML  y lo convierte a hexadecimal
	 * @param colorHtml
	 * @return
	 */
	private String htmlToHex(String colorHtml) {
		String resp = colorHtml.replaceAll("#", "");
		String r = resp.substring(0, 2);
		String g = resp.substring(2, 4);
		String b = resp.substring(4, 6);
		return Integer.parseInt(r, 16) + "," + Integer.parseInt(g, 16) + "," + Integer.parseInt(b, 16);
	}
	
	/**
	 * previsualizarGrafica
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward previsualizarGrafica(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ParametrizacionCurvaCrecimientoForm pcf = (ParametrizacionCurvaCrecimientoForm) form;
		try
		{
			CurvaCrecimientoParametrizabDto dtoccp = validarDatos(pcf);
			if(dtoccp!=null){
				pcf.setCurvaSeleccionada(dtoccp);
				pcf.setUbicacion(VISUALIZAR);
				return mapping.findForward("crearGrafica");
			}
		}
		catch(Exception e) 
		{
			pcf.addMensajeError(e.getMessage());
			e.printStackTrace();
		}
		pcf.setUbicacion(VISUALIZAR);
		return mapping.findForward("crearGrafica");
	}
	
	/**
	 * eliminarGrafica
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward eliminarGrafica(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ParametrizacionCurvaCrecimientoForm pcf = (ParametrizacionCurvaCrecimientoForm) form;
		pcf.resetMensajes();
		try 
		{
			for (int i = 0; i < pcf.getCurvasExistentes().size(); i++)
				if(pcf.getCurvasExistentes().get(i).getId().equals(pcf.getIdCurvaSeleccionada()))
					pcf.getCurvasExistentes().get(i).setColorParaEliminar("#958D8D");
			new HistoriaClinicaFacade().eliminarCurvaParametrizada(pcf.getIdCurvaSeleccionada());
			return empezar(mapping, form, request, response);
		}
		catch(Exception e)
		{
			pcf.addMensajeError(e.getCause().getCause().getCause().getMessage());
		}
		return mapping.findForward("empezar");
	}
	
	/**
	 * mostrarDetalles
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward mostrarDetalles(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ParametrizacionCurvaCrecimientoForm pcf = null; 
		try 
		{
			pcf = (ParametrizacionCurvaCrecimientoForm) form;
			pcf.resetMensajes();
			CurvaCrecimientoParametrizabDto curva = new HistoriaClinicaFacade().consultarCurvaParametrizada(pcf.getIdCurvaSeleccionada());
			pcf.setTituloGrafica(curva.getTituloGrafica());
			pcf.setColorTitulo(curva.getColorTitulo());
			pcf.setDescripcion(curva.getDescripcion());
			pcf.setColorDescripcion(curva.getColorDescripcion());
			pcf.setEdadInicial(curva.getEdadInicial());
			pcf.setEdadFinal(curva.getEdadFinal());
			pcf.setActivo(curva.getActivo());
			pcf.setIndicadorError(curva.getIndicadorError());
			pcf.setCurvaSeleccionada(curva);
			
			HashMap<Integer, String> mapaSexos = new HashMap<Integer, String>();			
			for (int i = 0; i < dtosexos.size(); i++)
				mapaSexos.put(dtosexos.get(i).getCodigoSexo(), dtosexos.get(i).getNombre());
			
			if(mapaSexos.get(curva.getCodigoSexo())!=null)
				pcf.setSexoSeleccionado(mapaSexos.get(curva.getCodigoSexo()));
			else
				pcf.setSexoSeleccionado("Ambos");
		}
		catch(ClassCastException e)
		{
			Log4JManager.error(e);
		}
		catch(Exception e)
		{
			pcf.addMensajeError(e.getMessage());
			e.printStackTrace();
		}
		return mapping.findForward("crearGrafica");
	}
	
	/**
	 * volver
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward volver(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		try 
		{
			ParametrizacionCurvaCrecimientoForm pcf = (ParametrizacionCurvaCrecimientoForm) form;
			pcf.setMensajesError(new ArrayList<String>());
			if(pcf.getUbicacion()==CREAR){
				pcf.reset2();
				return empezar(mapping, form, request, response);
			}
			if(pcf.getUbicacion()==BUSCAR){
				pcf.reset2();
				return empezar(mapping, form, request, response);
			}
			if(pcf.getUbicacion()==BUSCANDO){
				
				return busquedaAvanzada(mapping, pcf, request, response);
			}
			pcf.reset2();
		}
		catch(ClassCastException e)
		{
			Log4JManager.error(e);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return empezar(mapping, form, request, response);
	}
	
	/**
	 * busquedaAvanzada
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward busquedaAvanzada(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		try
		{
			ParametrizacionCurvaCrecimientoForm pcf = (ParametrizacionCurvaCrecimientoForm) form;
			pcf.resetBusqueda();
			pcf.setUbicacion(BUSCAR);
			pcf.getListaSexosBusqueda().add("");
			pcf.getListaSexosBusqueda().add("Ambos");
			for (int i = 0; i < dtosexos.size(); i++)
				pcf.getListaSexosBusqueda().add(dtosexos.get(i).getNombre());
			
			pcf.getActivoBusqueda().add("");
			pcf.getActivoBusqueda().add("Si");
			pcf.getActivoBusqueda().add("No");
			
			pcf.getIndicadorErrorBusqueda().add("");
			pcf.getIndicadorErrorBusqueda().add("Si");
			pcf.getIndicadorErrorBusqueda().add("No");
		}
		catch(ClassCastException e)
		{
			Log4JManager.error(e);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return mapping.findForward("busquedaAvanzada");
	}
	
	/**
	 * buscar
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward buscar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		ParametrizacionCurvaCrecimientoForm pcf = null;
		try 
		{
			pcf = (ParametrizacionCurvaCrecimientoForm) form;
			Integer codigoSexo = null;
			
			if(pcf.getSexoSeleccionadoBusqueda().equals("Ambos"))
				codigoSexo = ConstantesBD.codigoNuncaValido;
			else
				for (int i = 0; i < dtosexos.size(); i++)
					if(pcf.getSexoSeleccionadoBusqueda().equals(dtosexos.get(i).getNombre()))
						codigoSexo = dtosexos.get(i).getCodigoSexo();
			
			Boolean activo = null;
			if(pcf.getActivoSeleccionadoBusqueda().equals("Si"))
				activo = true;
			else if(pcf.getActivoSeleccionadoBusqueda().equals("No"))
				activo = false;
			
			Boolean indicadorError = null;
			if(pcf.getIndicadorErrorSeleccionadoBusqueda().equals("Si"))
				indicadorError = true;
			else if(pcf.getIndicadorErrorSeleccionadoBusqueda().equals("No"))
				indicadorError = false;
			
			pcf.setCurvasExistentes(new HistoriaClinicaFacade().buscarCurvaCriterios(pcf.getTituloGrafica(),pcf.getDescripcion(),pcf.getEdadInicial(),pcf.getEdadFinal(),codigoSexo,activo,indicadorError));
			
			if(pcf.getCurvasExistentes() == null)
				pcf.setCurvasExistentes(new ArrayList<CurvaCrecimientoParametrizabDto>());
			pcf.setUbicacion(BUSCANDO);
		}
		catch(ClassCastException e)
		{
			Log4JManager.error(e);
		}
		catch(Exception e)
		{
			pcf.addMensajeError(e.getMessage());
			e.printStackTrace();
		}
		return empezar(mapping, pcf, request, response);
	}
	
	/**
	 * ordenar
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	public ActionForward ordenar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		try
		{	
			ParametrizacionCurvaCrecimientoForm pcf = (ParametrizacionCurvaCrecimientoForm) form;
			String campo = pcf.getCriterioOrden();
			pcf.setCurvasExistentes(new ArrayList<CurvaCrecimientoParametrizabDto>(ordenarCurvas(campo , pcf.getCurvasExistentes())));
		}
		catch (IllegalArgumentException e){
				e.printStackTrace();
		}
		catch (IllegalAccessException e) {
				e.printStackTrace();
		}
		catch (InvocationTargetException e) {
				e.printStackTrace();
		}
		catch (IntrospectionException e) {
				e.printStackTrace();
		}
		catch(ClassCastException e){
				Log4JManager.error(e);
		}
		return mapping.findForward("empezar");
	}

	/**
	 * ordena la curvas existentes dado el atributo de la clase CurvaCrecimientoParametrizabDto por el cual se desea ordenar
	 * @param atributo
	 * @param curvasExistentes
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws IntrospectionException
	 */
	private Collection<CurvaCrecimientoParametrizabDto> ordenarCurvas(String atributo, List<CurvaCrecimientoParametrizabDto> curvasExistentes) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, IntrospectionException{
		LinkedHashSet<CurvaCrecimientoParametrizabDto> curvasOrdenadas = new LinkedHashSet<CurvaCrecimientoParametrizabDto>();
		Object[] columna = new Object[curvasExistentes.size()];
		
		for (int i = 0; i < curvasExistentes.size(); i++)
		{
			if(new PropertyDescriptor(atributo, CurvaCrecimientoParametrizabDto.class).getReadMethod().invoke(curvasExistentes.get(i))!=null)
			{
				columna[i] = new PropertyDescriptor(atributo, CurvaCrecimientoParametrizabDto.class).getReadMethod().invoke(curvasExistentes.get(i));
				if(columna[i] instanceof String)
					columna[i] = ((String)columna[i]).toUpperCase(); 
			}
			else
			{
				columna[i] = "";
				curvasOrdenadas.add(curvasExistentes.get(i));
			}
		}

		Arrays.sort(columna);

		for (int i = 0; i < columna.length; i++)
		{
			for (int j = 0; j < curvasExistentes.size(); j++)
			{
				if(new PropertyDescriptor(atributo, CurvaCrecimientoParametrizabDto.class).getReadMethod().invoke(curvasExistentes.get(j)) instanceof String)
				{
					if(columna[i].equals(((String)new PropertyDescriptor(atributo, CurvaCrecimientoParametrizabDto.class).getReadMethod().invoke(curvasExistentes.get(j))).toUpperCase()))
						curvasOrdenadas.add(curvasExistentes.get(j));
				}
				else
				{
					if(columna[i].equals(new PropertyDescriptor(atributo, CurvaCrecimientoParametrizabDto.class).getReadMethod().invoke(curvasExistentes.get(j))))
						curvasOrdenadas.add(curvasExistentes.get(j));
				}
			}
		}
		return curvasOrdenadas;
	}
}