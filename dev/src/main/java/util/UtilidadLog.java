package util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.princetonsa.action.odontologia.ServicioHonorarioAction;
import com.princetonsa.mundo.UsuarioBasico;

public class UtilidadLog {

	private static Logger logger = Logger
			.getLogger(ServicioHonorarioAction.class);

	/**
	 * 
	 * @param o
	 * @return Metodo que devuelve los valores de todos los atributos de una
	 *         clase
	 * 
	 * 
	 *         mostrarNull muestra o no los valores nulos
	 */

	public static String obtenerStringHerencia(Object o, boolean mostrarNulls) {
		StringBuilder buf = new StringBuilder();
		buf.append(
				"********************" + "   " + o.getClass().getSimpleName())
				.append("\n");
		Class<? extends Object> clase = o.getClass();
		Field[] field = UtilidadLog.getAllFields(clase);

		for (int i = 0; i < field.length; i++) {
			Object resultado = new Object();

			try {
				String nombreCampo = field[i].getName();
				Method metodo;
				logger.info(nombreCampo);
				nombreCampo = nombreCampo.substring(0, 1).toUpperCase()
						+ nombreCampo.substring(1, nombreCampo.length());
				try {
					metodo = clase.getMethod("get" + nombreCampo);
				} catch (NoSuchMethodError e) {
					metodo = clase.getMethod("is" + nombreCampo);
				}

				resultado = metodo.invoke(o, (Object[]) null);

				if (resultado instanceof Object
						&& !(resultado instanceof String)
						&& !(resultado instanceof Integer)
						&& !(resultado instanceof Double)
						&& !(resultado instanceof Boolean)
						&& !(resultado instanceof InfoDatosInt)
						&& !(resultado instanceof InfoDatosStr)
						&& !(resultado instanceof ArrayList)
						&& !(resultado instanceof BigDecimal)) {
					buf.append(nombreCampo).append("\t => \t").append(
							obtenerString(resultado, mostrarNulls))
							.append("\n");

				} else if (((resultado instanceof String)
						|| (resultado instanceof Integer) || (resultado instanceof Double))
						&& mostrarNulls == false) {
					logger
							.info("***********************************************************************************************NO ES BIG DECIMAL");
					if ((resultado instanceof Integer)
							|| (resultado instanceof Double)) {
						if (Integer.parseInt((String) resultado) == 0
								|| Integer.parseInt((String) resultado) == -1) {
							buf.append(nombreCampo).append(": Vacio").append(
									"\n");
						}
					} else if (resultado instanceof String) {
						if (resultado.toString().equals("")) {
							buf.append(nombreCampo).append(": Vacio").append(
									"\n");
						}
					}

				} else {

					if (resultado instanceof InfoDatosInt) {
						InfoDatosInt res = (InfoDatosInt) resultado;
						if (res.getCodigo() > -1) {
							buf.append(nombreCampo).append("\t => \t").append(
									res.getCodigo() + " "
											+ res.getDescripcion())
									.append("\n");
						}
					} else if (resultado instanceof ArrayList) {
						ArrayList<Object> array = (ArrayList) resultado;
						for (Object x : array) {

							buf.append(nombreCampo).append("\t => \t").append(
									obtenerString(x, mostrarNulls))
									.append("\n");

						}
					} else if (resultado instanceof InfoDatosStr) {
						InfoDatosStr res = (InfoDatosStr) resultado;

						buf.append(nombreCampo).append("\t => \t").append(
								res.getCodigo() + " " + res.getNombre())
								.append("\n");

					} else if (resultado instanceof BigDecimal) {
						BigDecimal res = (BigDecimal) resultado;
						buf.append(nombreCampo).append("\t => \t").append(
								res.doubleValue()).append("\n");
					} else {
						buf.append(nombreCampo).append("\t => \t").append(
								resultado).append("\n");
					}
				}
			} catch (Exception ex) {
				logger.error("Error Imprimir" , ex);

			}

		}

		return buf.toString();

	}

	public static String obtenerString(Object o, boolean mostrarNulls) {
		StringBuilder buf = new StringBuilder();
		buf.append(
				"********************" + "   " + o.getClass().getSimpleName())
				.append("\n");
		Class<? extends Object> clase = o.getClass();
		Field[] field = clase.getDeclaredFields();

		for (int i = 0; i < field.length; i++) {
			Object resultado = new Object();

			try {
				String nombreCampo = field[i].getName();
				Method metodo;
				logger.info(nombreCampo);
				nombreCampo = nombreCampo.substring(0, 1).toUpperCase()
						+ nombreCampo.substring(1, nombreCampo.length());
				try 
				{
					metodo = clase.getMethod("get" + nombreCampo);
				} 
				catch (Exception e) 
				{
					try
					{
						metodo = clase.getMethod("is" + nombreCampo);
					}
					catch(Exception e1) 
					{
						//el atributo no tiene metodo get ni is
						metodo=null;
					}
					
				}

				if(metodo!=null)
				{
					resultado = metodo.invoke(o, (Object[]) null);
	
					if (resultado instanceof Object
							&& !(resultado instanceof String)
							&& !(resultado instanceof Integer)
							&& !(resultado instanceof Double)
							&& !(resultado instanceof Boolean)
							&& !(resultado instanceof InfoDatosInt)
							&& !(resultado instanceof InfoDatosStr)
							&& !(resultado instanceof ArrayList)
							&& !(resultado instanceof BigDecimal)) {
						buf.append(nombreCampo).append("\t => \t").append(
								obtenerString(resultado, mostrarNulls))
								.append("\n");
	
					} else if (((resultado instanceof String)
							|| (resultado instanceof Integer) || (resultado instanceof Double))) {
						if ((resultado instanceof Integer)
								|| (resultado instanceof Double)) {
							if (resultado instanceof Integer) {
								if (Integer.parseInt(resultado+"") <= 0) {
									buf.append(nombreCampo).append(": Vacio")
											.append("\n");
								} else {
									buf.append(nombreCampo).append("\t => \t")
											.append(resultado).append("\n");
								}
							} else {
								if (Double.parseDouble(String
										.valueOf((Double) (resultado))) <= 0) {
									buf.append(nombreCampo).append(": Vacio")
											.append("\n");
								} else {
									buf.append(nombreCampo).append("\t => \t")
											.append(resultado).append("\n");
								}
	
							}
						} else if (resultado instanceof String) {
							if (resultado.toString().equals("")) {
								buf.append(nombreCampo).append(": Vacio").append(
										"\n");
							} else {
								buf.append(nombreCampo).append("\t => \t").append(
										resultado).append("\n");
							}
						}
	
					} else {
	
						if (resultado instanceof InfoDatosInt) {
							InfoDatosInt res = (InfoDatosInt) resultado;
							if (res.getCodigo() > -1) {
								buf.append(nombreCampo).append("\t => \t").append(
										res.getCodigo() + " "
												+ res.getDescripcion())
										.append("\n");
							} else {
								buf.append(nombreCampo).append(": Vacio").append(
										"\n");
							}
						} else if (resultado instanceof ArrayList) {
							ArrayList<Object> array = (ArrayList) resultado;
							for (Object x : array) {
	
								buf.append(nombreCampo).append("\t => \t").append(
										obtenerString(x, mostrarNulls))
										.append("\n");
	
							}
						} else if (resultado instanceof InfoDatosStr) {
							InfoDatosStr res = (InfoDatosStr) resultado;
							if (!res.getCodigo().isEmpty()) {
								buf.append(nombreCampo).append("\t => \t").append(
										res.getCodigo() + " " + res.getNombre())
										.append("\n");
							} else {
								buf.append(nombreCampo).append(": Vacio").append(
										"\n");
							}
	
						} else if (resultado instanceof BigDecimal) {
	
							BigDecimal res = (BigDecimal) resultado;
							if (res.doubleValue() > -1) {
								buf.append(nombreCampo).append("\t => \t").append(
										res).append("\n");
							} else {
								buf.append(nombreCampo).append(": Vacio").append(
										"\n");
							}
						} else {
							buf.append(nombreCampo).append("\t => \t").append(
									resultado).append("\n");
						}
					}
				}
				
			} catch (Exception ex) {
				logger.error("Error Imprimir" , ex);

			}

		}

		return buf.toString();

	}

	/**
	 * 
	 * @param usuario
	 * @param dtoWhere
	 * @param dtoNuevo
	 */

	public static void generarLog(UsuarioBasico usuario, Object dtoWhere,
			Object dtoNuevo, int tipoLogNuevo, int codigoLog) {

		String log = "";
		int tipoLog = tipoLogNuevo;
		if (tipoLog == ConstantesBD.tipoRegistroLogEliminacion) {
			log += "ELIMINACIÒN DE REGISTRO";
		} else if (tipoLog == ConstantesBD.tipoRegistroLogModificacion) {
			log += "MODIFICACIÒN DE REGISTRO";
		}
		String separador = System.getProperty("file.separator");
		log += "\n**********NUEVO REGISTRO LOG **************\n" + "\nFecha: "
				+ UtilidadFecha.getFechaActual() + "\nHora: "
				+ UtilidadFecha.getHoraActual() + "\nUsuario: "
				+ usuario.getLoginUsuario() +

				"\nTipo de Reporte: Archivo Plano"
				+ "\n===============DATOS==================== \n"
				+ obtenerString(dtoWhere, true);

		if (dtoNuevo != null) {
			log += "\n==========   DATOS NUEVOS ========== \n"
					+ obtenerString(dtoNuevo, true);
		}

		LogsAxioma.enviarLog(codigoLog, log, tipoLog, usuario.getLoginUsuario());

	}

	/**
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */

	public static Field[] getAllFields(Class<?> clazz) {
		List<Field> allFields = new ArrayList<Field>();
		for (Class<?> superThing : getAllSupers(clazz)) {
			for (Field f : superThing.getDeclaredFields()) {
				allFields.add(f);
			}
		}
		return allFields.toArray(new Field[allFields.size()]);
	}

	public static Set<Class<?>> getAllSupers(Class<?> clazz) {
		Set<Class<?>> result = new HashSet<Class<?>>();
		if (clazz != null) {
			result.add(clazz);
			result.addAll(getAllSupers(clazz.getSuperclass()));
			for (Class<?> interfaceClass : clazz.getInterfaces()) {
				result.addAll(getAllSupers(interfaceClass));
			}
		}
		return result;
	}

}
